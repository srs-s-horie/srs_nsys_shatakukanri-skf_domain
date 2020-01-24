/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3030.domain.service.skf3030sc001;

import static jp.co.c_nexco.nfw.core.constants.CommonConstant.NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi_v3_8.ss.usermodel.Cell;
import org.apache.poi_v3_9.ss.usermodel.IndexedColors;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Rp002.Skf3030Rp002GetShatakuDaichoInfoExp;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.nfw.webcore.utils.bean.RowDataBean;
import jp.co.c_nexco.nfw.webcore.utils.bean.SheetDataBean;
import jp.co.c_nexco.nfw.webcore.utils.bean.WorkBookDataBean;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3030.domain.dto.skf3030sc001.Skf3030Sc001DownloadRp002Dto;

/**
 * Skf3030Sc001DownloadRp002Service 社宅管理台帳「社宅管理台帳データ」出力Service
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf3030Sc001DownloadRp002Service extends BaseServiceAbstract<Skf3030Sc001DownloadRp002Dto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private Skf3030Sc001SharedService skf3030Sc001SharedService;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;

	private static final String FILE_NAME_KEY = "skf3030.skf3030_sc001.daicho_data_template_file";
	private static final int START_LINE = 1;

	/** 社宅管理台帳エクセル出力情報 */
	private enum Rp002Info {
		FIRST_DAY("BW2", "ＭＳ Ｐゴシック", "12"), // 対象初日
		LAST_DAY("BY2", "ＭＳ Ｐゴシック", "12"), // 対象終日
		MANAGE_COMPANY_CD("B", "ＭＳ Ｐゴシック", "11"), // 社宅基礎データ_管理機関データ_会社コード
		MANAGE_COMPANY_NAME("C", "ＭＳ Ｐゴシック", "11"), // 社宅基礎データ_管理機関データ_会社名
		MANAGE_AGENCY_CD("D", "ＭＳ Ｐゴシック", "11"), // 社宅基礎データ_管理機関データ_機関コード
		MANAGE_AGENCY_NAME("E", "ＭＳ Ｐゴシック", "11"), // 社宅基礎データ_管理機関データ_機関名
		HOYU_KARIAGE("F", "ＭＳ Ｐゴシック", "11"), // 社宅基礎データ_物件データ_保有借上
		SHATAK_NAME("G", "ＭＳ Ｐゴシック", "11"), // 社宅基礎データ_物件データ_社宅名
		ROOM_NO("H", "ＭＳ Ｐゴシック", "11"), // 社宅基礎データ_物件データ_部屋番号
		KAIHAITO_KEIKAKU("I", "ＭＳ Ｐゴシック", "11"), // 社宅基礎データ_物件データ_改廃等計画
		PREF_CD("J", "ＭＳ Ｐゴシック", "11"), // 社宅基礎データ_物件データ_都道府県コード
		ADDRESS("K", "ＭＳ Ｐゴシック", "11"), // 社宅基礎データ_物件データ_所在地
		ORIGINAL_KIKAKU("L", "ＭＳ Ｐゴシック", "11"), // 社宅基礎データ_物件データ_本来規格
		ORIGINAL_MENSEKI("M", "ＭＳ Ｐゴシック", "11"), // 社宅基礎データ_物件データ_本来延面積
		TAIYO_KIKAKU("N", "ＭＳ Ｐゴシック", "11"), // 社宅基礎データ_社宅使用料_貸与規格
		TAISHO_MENSEK("O", "ＭＳ Ｐゴシック", "11"), // 社宅基礎データ_社宅使用料_対象面積
		AREA("P", "ＭＳ Ｐゴシック", "11"), // 社宅基礎データ_社宅使用料_地域
		RYO_GENMEN_07("Q", "ＭＳ Ｐゴシック", "11"), // 社宅基礎データ_社宅使用料_寮(0.7)減免
		KYOSHO_GENMEN("R", "ＭＳ Ｐゴシック", "11"), // 社宅基礎データ_社宅使用料_狭小減免
		KANREICHI_GENMEN("S", "ＭＳ Ｐゴシック", "11"), // 社宅基礎データ_社宅使用料_寒冷地減免
		MENSEKI_GENMEN("T", "ＭＳ Ｐゴシック", "11"), // 社宅基礎データ_社宅使用料_面積減免
		SONGAI_BAISHO("U", "ＭＳ Ｐゴシック", "11"), // 損害賠償
		YAKUIN_TEKIYO("V", "ＭＳ Ｐゴシック", "11"), // 社宅基礎データ_社宅使用料_役員適用
		RENTAL_TOTAL("W", "ＭＳ Ｐゴシック", "11"), // 社宅基礎データ_社宅使用料_月額
		PARKING_STRUCTURE("X", "ＭＳ Ｐゴシック", "11"), // 社宅基礎データ_駐車場_構造
		SHATAK_PARKING_RENTAL_ADJUST("Y", "ＭＳ Ｐゴシック", "11"), // 社宅基礎データ_駐車場_調整
		PARKING_RENTAL_TOTAL("Z", "ＭＳ Ｐゴシック", "11"), // 社宅基礎データ_駐車場_月額
		KYOEKIHI_PAY_MONTH("AA", "ＭＳ Ｐゴシック", "11"), // 社宅基礎データ_個人負担共益費_種別
		KYOEKIHI_PERSON_TOTAL("AB", "ＭＳ Ｐゴシック", "11"), // 社宅基礎データ_個人負担共益費_個人負担月額
		MUTUAL_USE("AC", "ＭＳ Ｐゴシック", "11"), // 社宅等相互利用協定データ_対象_有無
		KASHITUKE_COMPANY("AD", "ＭＳ Ｐゴシック", "11"), // 社宅等相互利用協定データ_賃貸借契約_貸付会社
		KARIUKE_COMPANY("AE", "ＭＳ Ｐゴシック", "11"), // 社宅等相互利用協定データ_賃貸借契約_借受会社
		MUTUAL_USE_STARTDAY("AF", "ＭＳ Ｐゴシック", "11"), // 社宅等相互利用協定データ_賃貸借契約_開始日
		MUTUAL_USE_ENDDAY("AG", "ＭＳ Ｐゴシック", "11"), // 社宅等相互利用協定データ_賃貸借契約_終了日
		GENSEKI_CHIGAI_HANTEI("AH", "ＭＳ Ｐゴシック", "11"), // 社宅等相互利用協定データ_賃貸借契約_原籍違い判定
		SHATAK_SHUNYU_COMPANY("AI", "ＭＳ Ｐゴシック", "11"), // 社宅使用料会計処理 収入機関 会社名
		SHATAK_KOJYO_COMPANY("AJ", "ＭＳ Ｐゴシック", "11"), // 社宅使用料会計処理_給与控除 会社名
		SHATAK_COMPANY_TRANSFER("AK", "ＭＳ Ｐゴシック", "11"), // 社宅使用料会計処理_会社間送金_有無
		KYOEKI_UKEIRE_COMPANY("AL", "ＭＳ Ｐゴシック", "11"), // 共益費預り金会計処理 受入機関 会社名
		KYOEKI_KOJYO_COMPANY("AM", "ＭＳ Ｐゴシック", "11"), // 共益費預り金会計処理 給与控除 会社名
		KYOEKI_COMPANY_TRANSFER("AN", "ＭＳ Ｐゴシック", "11"), // 共益費預り金会計処理_会社間送金_有無（○×）
		ORIGINAL_COMPANY_NAME("AO", "ＭＳ Ｐゴシック", "11"), // 入居者データ_原籍データ_原籍会社名
		ORIGINAL_COMPANY_CD("AP", "ＭＳ Ｐゴシック", "11"), // 入居者データ_原籍データ_会社コード
		PAY_COMPANY_NAME("AQ", "ＭＳ Ｐゴシック", "11"), // 入居者データ_給与支給データ_支給会社名
		PAY_COMPANY_CD("AR", "ＭＳ Ｐゴシック", "11"), // 入居者データ_給与支給データ_会社コード
		KIKAN_NAME("AS", "ＭＳ Ｐゴシック", "11"), // 入居者データ_給与支給データ_機関名
		KIKAN_CD("AT", "ＭＳ Ｐゴシック", "11"), // 入居者データ_給与支給データ_機関コード
		SHITSUBU_NAME("AU", "ＭＳ Ｐゴシック", "11"), // 入居者データ_給与支給データ_室・部名
		SHITSUBU_CD("AV", "ＭＳ Ｐゴシック", "11"), // 入居者データ_給与支給データ_所属Ⅰコード
		KA_NAME("AW", "ＭＳ Ｐゴシック", "11"), // 入居者データ_給与支給データ_課等名
		KA_CD("AX", "ＭＳ Ｐゴシック", "11"), // 入居者データ_給与支給データ_所属Ⅱコード
		SHAIN_NAME("AY", "ＭＳ Ｐゴシック", "11"), // 入居者データ_入居者データ_氏名
		SHAIN_NO("AZ", "ＭＳ Ｐゴシック", "11"), // 入居者データ_入居者データ_社員コード
		BUSINESS_AREA_CD("BA", "ＭＳ Ｐゴシック", "11"), // 入居者データ_当月事業領域_当月コード番号
		BUSINESS_AREA_NAME("BB", "ＭＳ Ｐゴシック", "11"), // 入居者データ_当月事業領域_当月組織名称
		WORK_AREA_CD("BC", "ＭＳ Ｐゴシック", "11"), // 入居者データ_勤務地データ_コード
		WORK_AREA_PREF_NAME("BD", "ＭＳ Ｐゴシック", "11"), // 入居者データ_勤務地データ_都道府県名
		PRE_BUSINESS_AREA_CD("BE", "ＭＳ Ｐゴシック", "11"), // 入居者データ_前月事業領域_前月コード番号
		PRE_BUSINESS_AREA_NAME("BF", "ＭＳ Ｐゴシック", "11"), // 入居者データ_前月事業領域_前月組織名称
		ASSING_COMPANY_NAME("BG", "ＭＳ Ｐゴシック", "11"), // 入居者データ_配属データ_配属会社名
		ASSING_AGENCY_NAME("BH", "ＭＳ Ｐゴシック", "11"), // 入居者データ_配属データ_所属機関
		ASSING_AFFILIATION_1("BI", "ＭＳ Ｐゴシック", "11"), // 入居者データ_配属データ_室・部名
		ASSING_AFFILIATION_2("BJ", "ＭＳ Ｐゴシック", "11"), // 入居者データ_配属データ_課等名
		ASSING_CD("BK", "ＭＳ Ｐゴシック", "11"), // 入居者データ_配属データ_配属データコード番号
		NYUKYO_YOTO("BL", "ＭＳ Ｐゴシック", "11"), // 入居者データ_用途
		NYUKYO_DATE("BM", "ＭＳ Ｐゴシック", "11"), // 入退居管理_入居日
		TAIKYO_DATE("BN", "ＭＳ Ｐゴシック", "11"), // 入退居管理_退居日
		NYUKYO_CHOSEI("BO", "ＭＳ Ｐゴシック", "11"), // 入退居管理_調整
		SHATAK_NYUKYO_CNT("BP", "ＭＳ Ｐゴシック", "11"), // 入退居管理_入居日数
		PARKING_CNT("BQ", "ＭＳ Ｐゴシック", "11"), // 駐車場管理_台数
		PARKING_STARTDATE("BR", "ＭＳ Ｐゴシック", "11"), // 駐車場管理_開始日
		PARKING_ENDDATE("BS", "ＭＳ Ｐゴシック", "11"), // 駐車場管理_返還日
		PARKING_CHOSEI("BT", "ＭＳ Ｐゴシック", "11"), // 駐車場管理_調整
		PARKING_NYUKYO_CNT("BU", "ＭＳ Ｐゴシック", "11"), // 駐車場管理_入居日数
		SHATAK_TOTAL("BV", "ＭＳ Ｐゴシック", "11"), // 徴収すべき使用料等の額_社宅使用料
		PARKING_TOTAL("BW", "ＭＳ Ｐゴシック", "11"), // 徴収すべき使用料等の額_駐車場使用料
		SHATAK_OR_CAR("BX", "ＭＳ Ｐゴシック", "11"), // 徴収すべき使用料等の額_社宅及び車
		KYOEKIHI("BY", "ＭＳ Ｐゴシック", "11"), // 徴収すべき使用料等の額_共益費
		CHOSHU_TOTAL("BZ", "ＭＳ Ｐゴシック", "11"), // 徴収すべき使用料等の額_合計
		SHATAK_KANJO_KAMOKU("CA", "ＭＳ Ｐゴシック", "11"), // 給与控除後の会計転記判定_社宅使用料計上勘定科目
		KYOEKIHI_KANJO_KAMOKU("CB", "ＭＳ Ｐゴシック", "11"), // 給与控除後の会計転記判定_共益費個人負担金勘計上勘定科目
		BIKO("CC", "ＭＳ Ｐゴシック", "11"), // 備考_補足データ等
		ABSENCE_FAMILY("CD", "ＭＳ Ｐゴシック", "11"), // 留守家族情報
		RENTAL_ADJUST("CE", "ＭＳ Ｐゴシック", "11"), // 社宅使用料調整金
		PARKING_RENTAL_ADJUST("CF", "ＭＳ Ｐゴシック", "11"), // 駐車場使用料調整金
		KYOEKIHI_PERSON_ADJUST("CG", "ＭＳ Ｐゴシック", "11"), // 個人負担共益費調整金
		SENTAKUKI("CH", "ＭＳ Ｐゴシック", "11"), // 備品貸与データ_単身備品_①洗濯機
		REIZOKO("CI", "ＭＳ Ｐゴシック", "11"), // 備品貸与データ_単身備品_②冷蔵庫
		DENSHIRENJI("CJ", "ＭＳ Ｐゴシック", "11"), // 備品貸与データ_単身備品_③電子ｵｰﾌﾞﾝﾚﾝｼﾞ
		SOJIKI("CK", "ＭＳ Ｐゴシック", "11"), // 備品貸与データ_単身備品_④掃除機
		SUIHANKI("CL", "ＭＳ Ｐゴシック", "11"), // 備品貸与データ_単身備品_⑤炊飯ｼﾞｬｰ
		TV("CM", "ＭＳ Ｐゴシック", "11"), // 備品貸与データ_単身備品_⑥テレビ
		TV_STAND("CN", "ＭＳ Ｐゴシック", "11"), // 備品貸与データ_単身備品_⑦テレビ台
		ZATAKU("CO", "ＭＳ Ｐゴシック", "11"), // 備品貸与データ_単身備品_⑧座卓（こたつ）
		CABINET("CP", "ＭＳ Ｐゴシック", "11"), // 備品貸与データ_単身備品_⑨ｷｯﾁﾝｷｬﾋﾞﾈｯﾄ
		AIRCON("CQ", "ＭＳ Ｐゴシック", "11"), // 備品貸与データ_課税対象外_⑩ﾙｰﾑｴｱｺﾝ
		KATEN("CR", "ＭＳ Ｐゴシック", "11"), // 備品貸与データ_課税対象外_⑪カーテン
		SHOMEI("CS", "ＭＳ Ｐゴシック", "11"), // 備品貸与データ_課税対象外_⑫照明器具
		GAS("CT", "ＭＳ Ｐゴシック", "11"), // 備品貸与データ_課税対象外_⑬ｶﾞｽﾃｰﾌﾞﾙ
		BIHIN_TAIYO_DATE("CU", "ＭＳ Ｐゴシック", "11"), // 備品貸与データ_貸与日
		BIHIN_HENKYAKU_DATE("CV", "ＭＳ Ｐゴシック", "11"); // 備品貸与データ_返却日

		public String col;
		public String font;
		public String size;

		Rp002Info(String col, String font, String size) {
			this.col = col;
			this.font = font;
			this.size = size;
		}
	};

	@Value("${skf3030.skf3030_sc001.daicho_data_output_row_Idx}")
	private String startRowIdx;
	@Value("${skf3030.skf3030_sc001.daicho_data_sheet_name}")
	private String sheetName;
	@Value("${skf3030.skf3030_sc001.output_daicho_data_file_function_id}")
	private String funcId;

	@Override
	protected BaseDto index(Skf3030Sc001DownloadRp002Dto inDto) {

		skfOperationLogUtils.setAccessLog("社宅管理台帳「社宅管理台帳データ」出力処理開始", CodeConstant.C001, inDto.getPageId());

		inDto = (Skf3030Sc001DownloadRp002Dto) skf3030Sc001SharedService.setDropDownSelect(inDto);

		inDto.setResultMessages(null);

		String sysNengetsu = skf3030Sc001SharedService.getSystemProcessNenGetsu();
		String yearMonth = inDto.getHdnYearSelect() + inDto.getHdnMonthSelect();

		Map<String, String> userInfo = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		String userId = userInfo.get("userName");
		
		if (NfwStringUtils.isEmpty(yearMonth) || NfwStringUtils.isEmpty(userId)) {
			LogUtils.debugByMsg("パラメータ不正。対象年月：" + yearMonth + "、 ユーザーID：" + userId);
			ServiceHelper.addErrorResultMessage(inDto, null, MessageIdConstant.E_SKF_1070);
			return inDto;
		}

		List<Skf3030Rp002GetShatakuDaichoInfoExp> daicyoData = getShatakuKanriDaichoData(yearMonth, sysNengetsu);

		if (daicyoData != null) {
			inDto = outputShatakKanriDaichoData(inDto, daicyoData, yearMonth);
		} else {
			ServiceHelper.addErrorResultMessage(inDto, null, MessageIdConstant.E_SKF_1067, "出力対象データ");
			return inDto;
		}

		if (inDto.getFileData() == null || inDto.getFileData().length == 0) {
			ServiceHelper.addErrorResultMessage(inDto, null, MessageIdConstant.E_SKF_1070);
		}

		return inDto;
	}

	/**
	 * 社宅管理台帳データを取得する。
	 * 
	 * @param yearMonth
	 *            対象年月
	 * @param sysNengetsu
	 *            システム処理年月
	 * @return 社宅管理台帳データ
	 */
	private List<Skf3030Rp002GetShatakuDaichoInfoExp> getShatakuKanriDaichoData(String yearMonth, String sysNengetsu) {

		boolean akishatakSearchFlg = false;
		if (!yearMonth.equals(sysNengetsu)) {
			akishatakSearchFlg = true;
		}

		List<Skf3030Rp002GetShatakuDaichoInfoExp> shatakuDaichoInfoList = skf3030Sc001SharedService
				.getShatakuDaichoInfo(yearMonth, akishatakSearchFlg);

		if (shatakuDaichoInfoList.size() == 0) {
			return null;
		}

		return shatakuDaichoInfoList;
	}

	/**
	 * 社宅管理台帳データを出力する。
	 * 
	 * @param inDto
	 *            Skf3030Sc001OutputExcelRp002Dto
	 * @param shatakuDaichoInfoList
	 *            社宅管理台帳データリスト
	 * @param sysNengetsu
	 *            システム処理年月
	 * @return Skf3030Sc001OutputExcelRp002Dto
	 * @throws Exception
	 */
	private Skf3030Sc001DownloadRp002Dto outputShatakKanriDaichoData(Skf3030Sc001DownloadRp002Dto inDto,
			List<Skf3030Rp002GetShatakuDaichoInfoExp> shatakuDaichoInfoList, String sysNengetsu) {

		List<RowDataBean> rowDataBeanList = new ArrayList<>();
		Map<String, Object> cellParams = new HashMap<>();
		RowDataBean rowDayData = new RowDataBean();

		String firstDay = sysNengetsu + "01";
		String editFirstDay = skf3030Sc001SharedService.cnvExcelDate(firstDay);
		rowDayData.addCellDataBean(Rp002Info.FIRST_DAY.col, editFirstDay, Cell.CELL_TYPE_NUMERIC);

		String lastDay = skf3030Sc001SharedService.getLastDay(sysNengetsu + "01");
		String editLastDay = skf3030Sc001SharedService.cnvExcelDate(lastDay);
		rowDayData.addCellDataBean(Rp002Info.LAST_DAY.col, editLastDay, Cell.CELL_TYPE_NUMERIC);

		int dayCnt = Integer.parseInt(lastDay) - Integer.parseInt(firstDay) + 1;

		rowDataBeanList.add(rowDayData);
		cellParams = initTargetDayCellParams(cellParams);

		int startIdx = Integer.parseInt(startRowIdx);

		for (int i = 0; i < shatakuDaichoInfoList.size(); i++) {
			RowDataBean rowData = new RowDataBean();
			String tagetRowIdx = String.valueOf(i + startIdx);

			Skf3030Rp002GetShatakuDaichoInfoExp data = shatakuDaichoInfoList.get(i);

			cellParams = initCellParams(cellParams, tagetRowIdx);

			if (!"1".equals(skf3030Sc001SharedService.cnvEmptyStrToNull(data.getAgencyExternalKbn()))) {
				rowData.addCellDataBean(Rp002Info.MANAGE_COMPANY_CD.col + tagetRowIdx,
						skf3030Sc001SharedService.cnvEmptyStrToNull(data.getManageBusinessAreaCd()));
			} else {
				rowData.addCellDataBean(Rp002Info.MANAGE_COMPANY_CD.col + tagetRowIdx,
						skf3030Sc001SharedService.cnvEmptyStrToNull(data.getManageShatakuCompanyCd()));
			}

			rowData.addCellDataBean(Rp002Info.MANAGE_COMPANY_NAME.col + tagetRowIdx,
					skf3030Sc001SharedService.cnvEmptyStrToNull(data.getCompanyName()));
			rowData.addCellDataBean(Rp002Info.MANAGE_AGENCY_CD.col + tagetRowIdx,
					skf3030Sc001SharedService.cnvEmptyStrToNull(data.getManageBusinessAreaCd()));
			rowData.addCellDataBean(Rp002Info.MANAGE_AGENCY_NAME.col + tagetRowIdx,
					skf3030Sc001SharedService.cnvEmptyStrToNull(data.getManageBusinessAreaName()));

			Map<String, String> shatakKbnMap = skfGenericCodeUtils
					.getGenericCode(FunctionIdConstant.GENERIC_CODE_SHATAKU_KBN);
			rowData.addCellDataBean(Rp002Info.HOYU_KARIAGE.col + tagetRowIdx, shatakKbnMap.get(data.getShatakKbn()));
			rowData.addCellDataBean(Rp002Info.SHATAK_NAME.col + tagetRowIdx,
					skf3030Sc001SharedService.cnvEmptyStrToNull(data.getShatakName()));
			rowData.addCellDataBean(Rp002Info.ROOM_NO.col + tagetRowIdx,
					skf3030Sc001SharedService.cnvEmptyStrToNull(data.getRoomNo()));

			if (!"1".equals(data.getLendKbn())) {
				rowData.addCellDataBean(Rp002Info.KAIHAITO_KEIKAKU.col + tagetRowIdx,
						skf3030Sc001SharedService.cnvEmptyStrToNull(data.getLendKbnHosoku()));
			} else {
				rowData.addCellDataBean(Rp002Info.KAIHAITO_KEIKAKU.col + tagetRowIdx, "");
			}

			String prefCd = skf3030Sc001SharedService.cnvEmptyStrToNull(data.getPrefCd());
			rowData.addCellDataBean(Rp002Info.PREF_CD.col + tagetRowIdx, prefCd);

			String prefName = "";
			Map<String, String> prefCdMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_PREFCD);

			if (prefCdMap.get(prefCd) != null && !Skf3030Sc001SharedService.CD_PREF_OTHER.equals(prefCd)) {
				prefName = prefCdMap.get(prefCd);
			}
			rowData.addCellDataBean(Rp002Info.ADDRESS.col + tagetRowIdx,
					prefName + skf3030Sc001SharedService.cnvEmptyStrToNull(data.getAddress()));

			Map<String, String> orgKikakMap = skfGenericCodeUtils
					.getGenericCode(FunctionIdConstant.GENERIC_CODE_KIKAKU_KBN);
			rowData.addCellDataBean(Rp002Info.ORIGINAL_KIKAKU.col + tagetRowIdx,
					orgKikakMap.get(data.getOriginalKikaku()));

			if (data.getOriginalMenseki() != null) {
				rowData.addCellDataBean(Rp002Info.ORIGINAL_MENSEKI.col + tagetRowIdx,
						String.valueOf(data.getOriginalMenseki()), Cell.CELL_TYPE_NUMERIC, "#,#.##");
			} else {
				rowData.addCellDataBean(Rp002Info.ORIGINAL_MENSEKI.col + tagetRowIdx, "", Cell.CELL_TYPE_NUMERIC,
						"#,#.##");
			}

			rowData.addCellDataBean(Rp002Info.TAIYO_KIKAKU.col + tagetRowIdx, orgKikakMap.get(data.getKikaku()));
			rowData.addCellDataBean(Rp002Info.TAISHO_MENSEK.col + tagetRowIdx, String.valueOf(data.getMenseki()));

			Map<String, String> areaKbnMap = skfGenericCodeUtils
					.getGenericCode(FunctionIdConstant.GENERIC_CODE_AREA_KBN);
			rowData.addCellDataBean(Rp002Info.AREA.col + tagetRowIdx, areaKbnMap.get(data.getAreaKbn()));

			Map<String, String> auseKbnMap = skfGenericCodeUtils
					.getGenericCode(FunctionIdConstant.GENERIC_CODE_AUSE_KBN);
			rowData.addCellDataBean(Rp002Info.RYO_GENMEN_07.col + tagetRowIdx, auseKbnMap.get(data.getAuse()));

			Map<String, String> kyoshoKbnMap = skfGenericCodeUtils
					.getGenericCode(FunctionIdConstant.GENERIC_CODE_KYOSHOADJUST_KBN);
			rowData.addCellDataBean(Rp002Info.KYOSHO_GENMEN.col + tagetRowIdx, kyoshoKbnMap.get(data.getKyosyoFlg()));

			if (!"0".equals(data.getKanreitiFlg())) {
				Map<String, String> kanreishiKbnMap = skfGenericCodeUtils
						.getGenericCode(FunctionIdConstant.GENERIC_CODE_KANREITIADJUST_KBN);
				rowData.addCellDataBean(Rp002Info.KANREICHI_GENMEN.col + tagetRowIdx,
						kanreishiKbnMap.get(data.getKanreitiFlg()));
			} else {
				rowData.addCellDataBean(Rp002Info.KANREICHI_GENMEN.col + tagetRowIdx, "");
			}

			Map<String, String> yakuinKbnMap = skfGenericCodeUtils
					.getGenericCode(FunctionIdConstant.GENERIC_CODE_SHATAKU_YAKUIN_TEKIYO_KBN);
			rowData.addCellDataBean(Rp002Info.YAKUIN_TEKIYO.col + tagetRowIdx,
					yakuinKbnMap.get(data.getYakuinSannteiKbn()));

			if (data.getRentalMonth() != null) {
				rowData.addCellDataBean(Rp002Info.RENTAL_TOTAL.col + tagetRowIdx, String.valueOf(data.getRentalMonth()),
						Cell.CELL_TYPE_NUMERIC, "#,#");
			} else {
				rowData.addCellDataBean(Rp002Info.RENTAL_TOTAL.col + tagetRowIdx, "", Cell.CELL_TYPE_NUMERIC, "#,#");
			}

			Map<String, String> parkingStructureKbnMap = skfGenericCodeUtils
					.getGenericCode(FunctionIdConstant.GENERIC_CODE_PARKING_STRUCTURE_KBN);
			rowData.addCellDataBean(Rp002Info.PARKING_STRUCTURE.col + tagetRowIdx,
					parkingStructureKbnMap.get(data.getParkingStructureKbn()));
			rowData.addCellDataBean(Rp002Info.SHATAK_PARKING_RENTAL_ADJUST.col + tagetRowIdx,
					String.valueOf(data.getParkingRentalAdjust()));

			if (data.getParkingRentalMonth() != null) {
				rowData.addCellDataBean(Rp002Info.PARKING_RENTAL_TOTAL.col + tagetRowIdx,
						String.valueOf(data.getParkingRentalMonth()), Cell.CELL_TYPE_NUMERIC, "#,#");
			} else {
				rowData.addCellDataBean(Rp002Info.PARKING_RENTAL_TOTAL.col + tagetRowIdx, "", Cell.CELL_TYPE_NUMERIC,
						"#,#");
			}

			Map<String, String> kyoekihiPayMonthKbnMap = skfGenericCodeUtils
					.getGenericCode(FunctionIdConstant.GENERIC_CODE_KYOEKIHI_PAY_MONTH_KBN);
			rowData.addCellDataBean(Rp002Info.KYOEKIHI_PAY_MONTH.col + tagetRowIdx,
					kyoekihiPayMonthKbnMap.get(data.getKyoekihiPayMonth()));

			if (data.getNowKyoekihiPerson() != null) {
				rowData.addCellDataBean(Rp002Info.KYOEKIHI_PERSON_TOTAL.col + tagetRowIdx,
						String.valueOf(data.getNowKyoekihiPerson()), Cell.CELL_TYPE_NUMERIC, "#,#");
			} else {
				rowData.addCellDataBean(Rp002Info.KYOEKIHI_PERSON_TOTAL.col + tagetRowIdx, "", Cell.CELL_TYPE_NUMERIC,
						"#,#");
			}

			Map<String, String> mutualUseKbnMap = skfGenericCodeUtils
					.getGenericCode(FunctionIdConstant.GENERIC_CODE_MUTUALUSE_KBN);
			String mutualUseKbn = mutualUseKbnMap.get(data.getMutualUseKbn());

			rowData.addCellDataBean(Rp002Info.MUTUAL_USE.col + tagetRowIdx, mutualUseKbn);
			rowData.addCellDataBean(Rp002Info.KASHITUKE_COMPANY.col + tagetRowIdx, data.getKashitukeName());
			rowData.addCellDataBean(Rp002Info.KARIUKE_COMPANY.col + tagetRowIdx,
					skf3030Sc001SharedService.cnvEmptyStrToNull(data.getKariukeName()));

			if (!NfwStringUtils.isEmpty(data.getMutualUseStartDay())) {
				String mutualUseStartDay = skfDateFormatUtils.dateFormatFromString(data.getMutualUseStartDay(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				rowData.addCellDataBean(Rp002Info.MUTUAL_USE_STARTDAY.col + tagetRowIdx, mutualUseStartDay);
			} else {
				rowData.addCellDataBean(Rp002Info.MUTUAL_USE_STARTDAY.col + tagetRowIdx, "");
			}

			if (!NfwStringUtils.isEmpty(data.getMutualUseEndDay())) {
				String mutualUseEndDay = skfDateFormatUtils.dateFormatFromString(data.getMutualUseEndDay(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				rowData.addCellDataBean(Rp002Info.MUTUAL_USE_ENDDAY.col + tagetRowIdx, mutualUseEndDay);
			} else {
				rowData.addCellDataBean(Rp002Info.MUTUAL_USE_ENDDAY.col + tagetRowIdx, "");
			}

			Map<String, String> kyojushaKbnMap = skfGenericCodeUtils
					.getGenericCode(FunctionIdConstant.GENERIC_CODE_KYOJUSHA_KBN);
			if (!NfwStringUtils.isEmpty(data.getGensekiHanteiKbn())) {
				rowData.addCellDataBean(Rp002Info.GENSEKI_CHIGAI_HANTEI.col + tagetRowIdx,
						kyojushaKbnMap.get(data.getGensekiHanteiKbn()));
			} else {
				rowData.addCellDataBean(Rp002Info.GENSEKI_CHIGAI_HANTEI.col + tagetRowIdx, "");
			}

			rowData.addCellDataBean(Rp002Info.ORIGINAL_COMPANY_NAME.col + tagetRowIdx,
					skf3030Sc001SharedService.cnvEmptyStrToNull(data.getOriginalName()));

			if (!"1".equals(data.getOriginalAgencyExternalKbn())) {
				rowData.addCellDataBean(Rp002Info.ORIGINAL_COMPANY_CD.col + tagetRowIdx,
						skf3030Sc001SharedService.cnvEmptyStrToNull(data.getOriginalCompanyCd()));
			} else {
				rowData.addCellDataBean(Rp002Info.ORIGINAL_COMPANY_CD.col + tagetRowIdx,
						skf3030Sc001SharedService.cnvEmptyStrToNull(data.getOriginalShatakuCompanyCd()));
			}

			rowData.addCellDataBean(Rp002Info.PAY_COMPANY_NAME.col + tagetRowIdx,
					skf3030Sc001SharedService.cnvEmptyStrToNull(data.getPayName()));

			String payCompanyCd = "";
			if (!"1".equals(data.getPayAgencyExternalKbn())) {
				payCompanyCd = data.getPayCompanyCd();
			} else {
				payCompanyCd = data.getPayShatakuCompanyCd();
			}
			rowData.addCellDataBean(Rp002Info.PAY_COMPANY_CD.col + tagetRowIdx, payCompanyCd);

			rowData.addCellDataBean(Rp002Info.KIKAN_NAME.col + tagetRowIdx,
					skf3030Sc001SharedService.cnvEmptyStrToNull(data.getAgencyName()));
			rowData.addCellDataBean(Rp002Info.KIKAN_CD.col + tagetRowIdx,
					skf3030Sc001SharedService.cnvEmptyStrToNull(data.getAgencyCd()));
			rowData.addCellDataBean(Rp002Info.SHITSUBU_NAME.col + tagetRowIdx,
					skf3030Sc001SharedService.cnvEmptyStrToNull(data.getAffiliation1Name()));
			rowData.addCellDataBean(Rp002Info.SHITSUBU_CD.col + tagetRowIdx,
					skf3030Sc001SharedService.cnvEmptyStrToNull(data.getAffiliation1Cd()));
			rowData.addCellDataBean(Rp002Info.KA_NAME.col + tagetRowIdx,
					skf3030Sc001SharedService.cnvEmptyStrToNull(data.getAffiliation2Name()));
			rowData.addCellDataBean(Rp002Info.KA_CD.col + tagetRowIdx,
					skf3030Sc001SharedService.cnvEmptyStrToNull(data.getAffiliation2Cd()));
			rowData.addCellDataBean(Rp002Info.SHAIN_NAME.col + tagetRowIdx,
					skf3030Sc001SharedService.cnvEmptyStrToNull(data.getName()));
			rowData.addCellDataBean(Rp002Info.SHAIN_NO.col + tagetRowIdx,
					skf3030Sc001SharedService.cnvEmptyStrToNull(data.getShainNo()));
			rowData.addCellDataBean(Rp002Info.BUSINESS_AREA_CD.col + tagetRowIdx,
					skf3030Sc001SharedService.cnvEmptyStrToNull(data.getBusinessAreaCd()));
			rowData.addCellDataBean(Rp002Info.BUSINESS_AREA_NAME.col + tagetRowIdx,
					skf3030Sc001SharedService.cnvEmptyStrToNull(data.getBusinessAreaName()));
			rowData.addCellDataBean(Rp002Info.PRE_BUSINESS_AREA_CD.col + tagetRowIdx,
					skf3030Sc001SharedService.cnvEmptyStrToNull(data.getPreBusinessAreaCd()));
			rowData.addCellDataBean(Rp002Info.PRE_BUSINESS_AREA_NAME.col + tagetRowIdx,
					skf3030Sc001SharedService.cnvEmptyStrToNull(data.getPreBusinessAreaName()));
			rowData.addCellDataBean(Rp002Info.ASSING_COMPANY_NAME.col + tagetRowIdx,
					skf3030Sc001SharedService.cnvEmptyStrToNull(data.getAssignName()));
			rowData.addCellDataBean(Rp002Info.ASSING_AGENCY_NAME.col + tagetRowIdx,
					skf3030Sc001SharedService.cnvEmptyStrToNull(data.getAssignAgencyName()));
			rowData.addCellDataBean(Rp002Info.ASSING_AFFILIATION_1.col + tagetRowIdx,
					skf3030Sc001SharedService.cnvEmptyStrToNull(data.getAssignAffiliation1()));
			rowData.addCellDataBean(Rp002Info.ASSING_AFFILIATION_2.col + tagetRowIdx,
					skf3030Sc001SharedService.cnvEmptyStrToNull(data.getAssignAffiliation2()));
			rowData.addCellDataBean(Rp002Info.NYUKYO_YOTO.col + tagetRowIdx, auseKbnMap.get(data.getAuse()));

			if (!NfwStringUtils.isEmpty(data.getNyukyoDate())) {
				String nyukyoDate = skfDateFormatUtils.dateFormatFromString(data.getNyukyoDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				rowData.addCellDataBean(Rp002Info.NYUKYO_DATE.col + tagetRowIdx, nyukyoDate);
			} else {
				rowData.addCellDataBean(Rp002Info.NYUKYO_DATE.col + tagetRowIdx, "");
			}

			if (!NfwStringUtils.isEmpty(data.getTaikyoDate())) {
				String taikyoDate = skfDateFormatUtils.dateFormatFromString(data.getTaikyoDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				rowData.addCellDataBean(Rp002Info.TAIKYO_DATE.col + tagetRowIdx, taikyoDate);
			} else {
				rowData.addCellDataBean(Rp002Info.TAIKYO_DATE.col + tagetRowIdx, "");
			}

			String parkingCnt = data.getParkingCnt();
			String parkingStartDate = "";
			String parkingEndDate = "";

			if (!NfwStringUtils.isEmpty(parkingCnt)) {
				rowData.addCellDataBean(Rp002Info.PARKING_CNT.col + tagetRowIdx, parkingCnt);

				String parkingStartDate1 = data.getParkingStartDate1();
				String parkingEndDate1 = data.getParkingEndDate1();
				String parkingStartDate2 = data.getParkingStartDate2();
				String parkingEndDate2 = data.getParkingEndDate2();

				if ("1".equals(parkingCnt)) {

					if (!NfwStringUtils.isEmpty(parkingStartDate1)) {
						data.setParkingStartDate(parkingStartDate1);
						parkingStartDate = skfDateFormatUtils.dateFormatFromString(parkingStartDate1,
								SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);

						if (!NfwStringUtils.isEmpty(parkingEndDate1)) {
							data.setParkingEndDate(parkingEndDate1);
							parkingEndDate = skfDateFormatUtils.dateFormatFromString(parkingEndDate1,
									SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
						}

					} else if (!NfwStringUtils.isEmpty(parkingStartDate2)) {
						data.setParkingStartDate(parkingStartDate2);
						parkingStartDate = skfDateFormatUtils.dateFormatFromString(parkingStartDate2,
								SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);

						if (!NfwStringUtils.isEmpty(parkingEndDate2)) {
							data.setParkingEndDate(parkingEndDate2);
							parkingEndDate = skfDateFormatUtils.dateFormatFromString(parkingEndDate2,
									SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
						}
					}

				} else if ("2".equals(parkingCnt)) {

					if (!NfwStringUtils.isEmpty(parkingEndDate1)) {
						data.setParkingStartDate(parkingStartDate1);
						parkingStartDate = skfDateFormatUtils.dateFormatFromString(parkingStartDate1,
								SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
					}

					if (!NfwStringUtils.isEmpty(parkingStartDate2)) {
						if (!NfwStringUtils.isEmpty(parkingStartDate1)) {

							if (Integer.parseInt(parkingStartDate1) > Integer.parseInt(parkingStartDate2)) {
								data.setParkingStartDate(parkingStartDate2);
								parkingStartDate = skfDateFormatUtils.dateFormatFromString(parkingStartDate2,
										SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
							} else {
								data.setParkingStartDate(parkingStartDate1);
								parkingStartDate = skfDateFormatUtils.dateFormatFromString(parkingStartDate1,
										SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
							}

						} else {
							data.setParkingStartDate(parkingStartDate2);
							parkingStartDate = skfDateFormatUtils.dateFormatFromString(parkingStartDate2,
									SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
						}
					}

					if (!NfwStringUtils.isEmpty(parkingEndDate1)) {
						data.setParkingEndDate(parkingEndDate1);
						parkingEndDate = skfDateFormatUtils.dateFormatFromString(parkingEndDate1,
								SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
					}

					if (!NfwStringUtils.isEmpty(parkingEndDate2)) {
						if (!NfwStringUtils.isEmpty(parkingEndDate1)) {

							if (Integer.parseInt(parkingEndDate1) < Integer.parseInt(parkingEndDate2)) {
								data.setParkingEndDate(parkingEndDate2);
								parkingEndDate = skfDateFormatUtils.dateFormatFromString(parkingEndDate2,
										SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
							} else {
								data.setParkingEndDate(parkingEndDate1);
								parkingEndDate = skfDateFormatUtils.dateFormatFromString(parkingEndDate1,
										SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
							}

						} else {
							data.setParkingEndDate(parkingEndDate2);
							parkingEndDate = skfDateFormatUtils.dateFormatFromString(parkingEndDate2,
									SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
						}
					}
				}

			} else {
				rowData.addCellDataBean(Rp002Info.PARKING_CNT.col + tagetRowIdx, "0");
			}

			rowData.addCellDataBean(Rp002Info.PARKING_STARTDATE.col + tagetRowIdx, parkingStartDate);
			rowData.addCellDataBean(Rp002Info.PARKING_ENDDATE.col + tagetRowIdx, parkingEndDate);
			rowData.addCellDataBean(Rp002Info.BIKO.col + tagetRowIdx,
					skf3030Sc001SharedService.cnvEmptyStrToNull(data.getBikoLedger()));

			if (data.getRentalAdjust() != null) {
				rowData.addCellDataBean(Rp002Info.RENTAL_ADJUST.col + tagetRowIdx,
						String.valueOf(data.getRentalAdjust()), Cell.CELL_TYPE_NUMERIC, "#,#");
			} else {
				rowData.addCellDataBean(Rp002Info.RENTAL_ADJUST.col + tagetRowIdx, "", Cell.CELL_TYPE_NUMERIC, "#,#");
			}

			if (data.getParkingRentalAdjust() != null) {
				rowData.addCellDataBean(Rp002Info.PARKING_RENTAL_ADJUST.col + tagetRowIdx,
						String.valueOf(data.getParkingRentalAdjust()), Cell.CELL_TYPE_NUMERIC, "#,#");
			} else {
				rowData.addCellDataBean(Rp002Info.PARKING_RENTAL_ADJUST.col + tagetRowIdx, "", Cell.CELL_TYPE_NUMERIC,
						"#,#");
			}

			if (data.getKyoekihiPersonAdjust() != null) {
				rowData.addCellDataBean(Rp002Info.KYOEKIHI_PERSON_ADJUST.col + tagetRowIdx,
						String.valueOf(data.getKyoekihiPersonAdjust()), Cell.CELL_TYPE_NUMERIC, "#,#");
			} else {
				rowData.addCellDataBean(Rp002Info.KYOEKIHI_PERSON_ADJUST.col + tagetRowIdx, "", Cell.CELL_TYPE_NUMERIC,
						"#,#");
			}

			Map<String, String> bihinStsKbnMap = skf3030Sc001SharedService.getBihinLentStsMap();
			rowData.addCellDataBean(Rp002Info.SENTAKUKI.col + tagetRowIdx,
					bihinStsKbnMap.get(data.getSentakukiLentStatusKbn()));
			rowData.addCellDataBean(Rp002Info.REIZOKO.col + tagetRowIdx,
					bihinStsKbnMap.get(data.getReizokoLentStatusKbn()));
			rowData.addCellDataBean(Rp002Info.DENSHIRENJI.col + tagetRowIdx,
					bihinStsKbnMap.get(data.getDenshirenjiLentStatusKbn()));
			rowData.addCellDataBean(Rp002Info.SOJIKI.col + tagetRowIdx,
					bihinStsKbnMap.get(data.getSojikiLentStatusKbn()));
			rowData.addCellDataBean(Rp002Info.SUIHANKI.col + tagetRowIdx,
					bihinStsKbnMap.get(data.getSuihankiLentStatusKbn()));
			rowData.addCellDataBean(Rp002Info.TV.col + tagetRowIdx, bihinStsKbnMap.get(data.getTvLentStatusKbn()));
			rowData.addCellDataBean(Rp002Info.TV_STAND.col + tagetRowIdx,
					bihinStsKbnMap.get(data.getTvStandLentStatusKbn()));
			rowData.addCellDataBean(Rp002Info.ZATAKU.col + tagetRowIdx,
					bihinStsKbnMap.get(data.getZatakuLentStatusKbn()));
			rowData.addCellDataBean(Rp002Info.CABINET.col + tagetRowIdx,
					bihinStsKbnMap.get(data.getCabinetLentStatusKbn()));
			rowData.addCellDataBean(Rp002Info.AIRCON.col + tagetRowIdx,
					bihinStsKbnMap.get(data.getAirconLentStatusKbn()));
			rowData.addCellDataBean(Rp002Info.KATEN.col + tagetRowIdx,
					bihinStsKbnMap.get(data.getKatenLentStatusKbn()));
			rowData.addCellDataBean(Rp002Info.SHOMEI.col + tagetRowIdx,
					bihinStsKbnMap.get(data.getShomeiLentStatusKbn()));
			rowData.addCellDataBean(Rp002Info.GAS.col + tagetRowIdx, bihinStsKbnMap.get(data.getGasLentStatusKbn()));

			if (!NfwStringUtils.isEmpty(data.getTaiyoDate())) {
				String taiyoDate = skfDateFormatUtils.dateFormatFromString(data.getTaiyoDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				rowData.addCellDataBean(Rp002Info.BIHIN_TAIYO_DATE.col + tagetRowIdx, taiyoDate);
			} else {
				rowData.addCellDataBean(Rp002Info.BIHIN_TAIYO_DATE.col + tagetRowIdx, "");
			}

			if (!NfwStringUtils.isEmpty(data.getHenkyakuDate())) {
				String henkyakDate = skfDateFormatUtils.dateFormatFromString(data.getHenkyakuDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				rowData.addCellDataBean(Rp002Info.BIHIN_HENKYAKU_DATE.col + tagetRowIdx, henkyakDate);
			} else {
				rowData.addCellDataBean(Rp002Info.BIHIN_HENKYAKU_DATE.col + tagetRowIdx, "");
			}

			rowData.addCellDataBean(Rp002Info.SHATAK_KANJO_KAMOKU.col + tagetRowIdx,
					skf3030Sc001SharedService.cnvEmptyStrToNull(data.getShatakuAccountCd())
							+ skf3030Sc001SharedService.cnvEmptyStrToNull(data.getShatakuAccountName()));

			String kyoekihiKanjoKamoku = skf3030Sc001SharedService.cnvEmptyStrToNull(data.getKyoekiAccountCd())
					+ skf3030Sc001SharedService.cnvEmptyStrToNull(data.getKyoekiAccountName());

			String shatakNyukyoCnt = skf3030Sc001SharedService.calcDayNum(data.getNyukyoDate(), data.getTaikyoDate(),
					firstDay, lastDay);
			rowData.addCellDataBean(Rp002Info.SHATAK_NYUKYO_CNT.col + tagetRowIdx, shatakNyukyoCnt);

			String parkingSiyoCnt = skf3030Sc001SharedService.calcDayNum(data.getParkingStartDate(),
					data.getParkingEndDate(), firstDay, lastDay);
			rowData.addCellDataBean(Rp002Info.PARKING_NYUKYO_CNT.col + tagetRowIdx, parkingSiyoCnt);

			int rentalMonth = 0;
			if (!NfwStringUtils.isEmpty(data.getRentalMonth())) {
				rentalMonth = Integer.parseInt(data.getRentalMonth());
			}

			int rentalAdjust = 0;
			if (data.getRentalAdjust() != null) {
				rentalAdjust = data.getRentalAdjust();
			}

			int shatakCnt = 0;
			if (!NfwStringUtils.isEmpty(shatakNyukyoCnt)) {
				shatakCnt = Integer.parseInt(shatakNyukyoCnt);
			}

			int shatakSiyoryo = ((rentalMonth * shatakCnt) / dayCnt) + rentalAdjust;
			rowData.addCellDataBean(Rp002Info.SHATAK_TOTAL.col + tagetRowIdx, String.valueOf(shatakSiyoryo),
					Cell.CELL_TYPE_NUMERIC, "#,#");

			String parkingTotal = "0";
			if (data.getParkingRentalTotal() != null) {
				parkingTotal = String.valueOf(data.getParkingRentalTotal());
			}

			rowData.addCellDataBean(Rp002Info.PARKING_TOTAL.col + tagetRowIdx, parkingTotal, Cell.CELL_TYPE_NUMERIC,
					"#,#");

			int shatakOrCar = shatakSiyoryo + Integer.parseInt(parkingTotal);
			rowData.addCellDataBean(Rp002Info.SHATAK_OR_CAR.col + tagetRowIdx, String.valueOf(shatakOrCar),
					Cell.CELL_TYPE_NUMERIC, "#,#");

			int kyoekihiPersonTotal = 0;
			if (data.getKyoekihiPersonTotal() != null) {
				kyoekihiPersonTotal = data.getKyoekihiPersonTotal();
			}

			int choshuTotal = shatakOrCar + kyoekihiPersonTotal;
			rowData.addCellDataBean(Rp002Info.CHOSHU_TOTAL.col + tagetRowIdx, String.valueOf(choshuTotal),
					Cell.CELL_TYPE_NUMERIC, "#,#");

			String shatakShunyuCompany = "";
			String shatakKojyoCompany = "";
			if (shatakOrCar != 0) {
				shatakKojyoCompany = data.getPayName();

				if (Skf3030Sc001SharedService.MUTUAL_USE_ARI.equals(mutualUseKbn)) {
					shatakShunyuCompany = data.getKariukeName();
				} else {
					shatakShunyuCompany = data.getCompanyName();
				}
			}

			rowData.addCellDataBean(Rp002Info.SHATAK_SHUNYU_COMPANY.col + tagetRowIdx, shatakShunyuCompany);
			rowData.addCellDataBean(Rp002Info.SHATAK_KOJYO_COMPANY.col + tagetRowIdx, shatakKojyoCompany);

			String shatakKaishakanSokin = "";
			if (shatakKojyoCompany.equals(shatakShunyuCompany)) {
				shatakKaishakanSokin = Skf3030Sc001SharedService.KAISHAKAN_SOKIN_NASI;

			} else {
				if (!NfwStringUtils.isEmpty(payCompanyCd)) {
					if (Integer.parseInt(payCompanyCd) > 10) {
						shatakKaishakanSokin = Skf3030Sc001SharedService.KAISHAKAN_SOKIN_SEIKYU;
					} else {
						shatakKaishakanSokin = Skf3030Sc001SharedService.KAISHAKAN_SOKIN_SHIHARAI;
					}

				} else {
					shatakKaishakanSokin = Skf3030Sc001SharedService.KAISHAKAN_SOKIN_NASI;
				}
			}

			rowData.addCellDataBean(Rp002Info.SHATAK_COMPANY_TRANSFER.col + tagetRowIdx, shatakKaishakanSokin);

			String kyoekiUkeireCompany = "";
			if (data.getKyoekihiPersonTotal() != 0) {

				if (Skf3030Sc001SharedService.MUTUAL_USE_ARI.equals(mutualUseKbn)) {
					kyoekiUkeireCompany = data.getKariukeName();
				} else {
					kyoekiUkeireCompany = data.getCompanyName();
				}
			}

			String kyoekiKojyoCompany = "";
			if (data.getKyoekihiPersonTotal() != 0) {
				kyoekiKojyoCompany = data.getPayName();
			}

			String kyoekihiKaishakanSokin = "";
			if (kyoekiUkeireCompany.equals(kyoekiKojyoCompany)) {
				kyoekihiKaishakanSokin = Skf3030Sc001SharedService.KAISHAKAN_SOKIN_NASI;

			} else {
				if (!NfwStringUtils.isEmpty(payCompanyCd)) {
					if (Integer.parseInt(payCompanyCd) > 10) {
						kyoekihiKaishakanSokin = Skf3030Sc001SharedService.KAISHAKAN_SOKIN_SEIKYU;
					} else {
						kyoekihiKaishakanSokin = Skf3030Sc001SharedService.KAISHAKAN_SOKIN_SHIHARAI;
					}

				} else {
					kyoekihiKaishakanSokin = Skf3030Sc001SharedService.KAISHAKAN_SOKIN_NASI;
				}
			}

			rowData.addCellDataBean(Rp002Info.KYOEKIHI.col + tagetRowIdx, String.valueOf(kyoekihiPersonTotal),
					Cell.CELL_TYPE_NUMERIC, "#,#");

			if (kyoekihiPersonTotal == 0) {
				kyoekihiKanjoKamoku = CodeConstant.HYPHEN;
				kyoekiUkeireCompany = "";
				kyoekiKojyoCompany = "";
				kyoekihiKaishakanSokin = CodeConstant.HYPHEN;
			}

			rowData.addCellDataBean(Rp002Info.KYOEKIHI_KANJO_KAMOKU.col + tagetRowIdx, kyoekihiKanjoKamoku);
			rowData.addCellDataBean(Rp002Info.KYOEKI_UKEIRE_COMPANY.col + tagetRowIdx, kyoekiUkeireCompany);
			rowData.addCellDataBean(Rp002Info.KYOEKI_KOJYO_COMPANY.col + tagetRowIdx, kyoekiKojyoCompany);
			rowData.addCellDataBean(Rp002Info.KYOEKI_COMPANY_TRANSFER.col + tagetRowIdx, kyoekihiKaishakanSokin);

			// 未設定項目（何もセットしないとセルスタイルが変わらない為、空文字をセットしておく)
			rowData.addCellDataBean(Rp002Info.MENSEKI_GENMEN.col + tagetRowIdx, "");
			rowData.addCellDataBean(Rp002Info.SONGAI_BAISHO.col + tagetRowIdx, "");
			rowData.addCellDataBean(Rp002Info.WORK_AREA_CD.col + tagetRowIdx, "");
			rowData.addCellDataBean(Rp002Info.WORK_AREA_PREF_NAME.col + tagetRowIdx, "");
			rowData.addCellDataBean(Rp002Info.NYUKYO_CHOSEI.col + tagetRowIdx, "");
			rowData.addCellDataBean(Rp002Info.PARKING_CHOSEI.col + tagetRowIdx, "");
			rowData.addCellDataBean(Rp002Info.ASSING_CD.col + tagetRowIdx, "");
			rowData.addCellDataBean(Rp002Info.ABSENCE_FAMILY.col + tagetRowIdx,
					kyojushaKbnMap.get(data.getKyojushaKbn()));

			rowDataBeanList.add(rowData);

			cellParams = changeCellParams(cellParams, data, tagetRowIdx, sysNengetsu);
		}

		inDto = outputExcel(inDto, rowDataBeanList, cellParams);

		return inDto;
	}

	/**
	 * 「社宅管理台帳データ」エクセルファイルを出力する。
	 * 
	 * @param inDto
	 *            Skf3030Sc001OutputExcelRp002Dto
	 * @param rowDataBeanList
	 *            行ごとのデータ
	 * @param cellparams
	 *            セルのスタイルパラメータ
	 * @return Skf3030Sc001OutputExcelRp002Dto
	 * @throws Exception
	 */
	private Skf3030Sc001DownloadRp002Dto outputExcel(Skf3030Sc001DownloadRp002Dto inDto,
			List<RowDataBean> rowDataBeanList, Map<String, Object> cellparams) {

		SheetDataBean sheetDataBean = new SheetDataBean();
		sheetDataBean.setSheetName(sheetName);
		sheetDataBean.setRowDataBeanList(rowDataBeanList);

		List<SheetDataBean> sheetDataBeanList = new ArrayList<>();
		sheetDataBeanList.add(sheetDataBean);

		String fileName = sheetName + DateTime.now().toString("YYYYMMddHHmmss")
				+ CodeConstant.DOT + CodeConstant.EXTENSION_XLSX;
		WorkBookDataBean wbdb = new WorkBookDataBean(fileName);
		wbdb.setSheetDataBeanList(sheetDataBeanList);

		Map<String, Object> resultMap = new HashMap<>();

		try {
			SkfFileOutputUtils.fileOutputExcel(wbdb, cellparams, FILE_NAME_KEY, funcId, START_LINE, null, resultMap, 4);

		} catch (Exception e) {
			ServiceHelper.addErrorResultMessage(inDto, null, MessageIdConstant.E_SKF_1070);
			sheetDataBean = null;
			sheetDataBeanList = null;
			cellparams = null;
			wbdb = null;
			return inDto;
		}

		byte[] writeFileData = (byte[]) resultMap.get("fileData");
		inDto.setFileData(writeFileData);
		inDto.setUploadFileName(fileName);
		inDto.setViewPath(NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH);

		sheetDataBean = null;
		sheetDataBeanList = null;
		cellparams = null;
		wbdb = null;
		writeFileData = null;

		return inDto;
	}

	/**
	 * 対象日（ヘッダー部分）のセルのスタイル情報を初期化する。
	 * 
	 * @param cellParams
	 *            セルのスタイルパラメータ
	 * @return セルのスタイルパラメータ
	 */
	private Map<String, Object> initTargetDayCellParams(Map<String, Object> cellParams) {

		Map<String, Object> firstDayCellParam = new HashMap<>();
		Map<String, Object> firstDayFontParam = new HashMap<>();

		firstDayFontParam.put(Skf3030Sc001SharedService.FONT_NAME_KEY, Rp002Info.FIRST_DAY.font);
		firstDayFontParam.put(Skf3030Sc001SharedService.FONT_COLOR_KEY, IndexedColors.RED.getIndex());
		firstDayFontParam.put(Skf3030Sc001SharedService.FONT_SIZE_KEY, Short.parseShort(Rp002Info.FIRST_DAY.size));
		firstDayCellParam.put(Skf3030Sc001SharedService.FONT_PARAM_KEY, firstDayFontParam);
		firstDayCellParam.put(Skf3030Sc001SharedService.FONT_BG_COLOR_KEY, IndexedColors.YELLOW.getIndex());
		cellParams.put(Rp002Info.FIRST_DAY.col, firstDayCellParam);

		Map<String, Object> lastDayCellParam = new HashMap<>();
		Map<String, Object> lastDayFontParam = new HashMap<>();

		lastDayFontParam.put(Skf3030Sc001SharedService.FONT_NAME_KEY, Rp002Info.LAST_DAY.font);
		lastDayFontParam.put(Skf3030Sc001SharedService.FONT_COLOR_KEY, IndexedColors.RED.getIndex());
		lastDayFontParam.put(Skf3030Sc001SharedService.FONT_SIZE_KEY, Short.parseShort(Rp002Info.LAST_DAY.size));
		lastDayCellParam.put(Skf3030Sc001SharedService.FONT_PARAM_KEY, lastDayFontParam);
		lastDayCellParam.put(Skf3030Sc001SharedService.FONT_BG_COLOR_KEY, IndexedColors.YELLOW.getIndex());
		cellParams.put(Rp002Info.LAST_DAY.col, lastDayCellParam);

		return cellParams;
	}

	/**
	 * セルのスタイル情報を初期化する。
	 * 
	 * @param cellParams
	 *            セルのスタイルパラメータ
	 * @param targetIdx
	 *            対象のインデックス
	 * @return セルのスタイルパラメータ
	 */
	private Map<String, Object> initCellParams(Map<String, Object> cellParams, String targetIdx) {

		Rp002Info[] shatakKanriInfoArry = Rp002Info.values();

		for (int i = 0; i < shatakKanriInfoArry.length; i++) {
			Map<String, Object> cellParam = new HashMap<>();
			Map<String, Object> fontParam = new HashMap<>();

			String col = shatakKanriInfoArry[i].col;
			if (!Rp002Info.FIRST_DAY.col.equals(col) && !Rp002Info.LAST_DAY.col.equals(col)) {
				fontParam.put(Skf3030Sc001SharedService.FONT_NAME_KEY, shatakKanriInfoArry[i].font);
				fontParam.put(Skf3030Sc001SharedService.FONT_COLOR_KEY, IndexedColors.BLACK.getIndex());
				fontParam.put(Skf3030Sc001SharedService.FONT_SIZE_KEY, Short.parseShort(shatakKanriInfoArry[i].size));
				cellParam.put(Skf3030Sc001SharedService.FONT_BG_COLOR_KEY, IndexedColors.WHITE.getIndex());
				cellParam.put(Skf3030Sc001SharedService.FONT_PARAM_KEY, fontParam);
				cellParams.put(col + targetIdx, cellParam);
			}
		}

		return cellParams;
	}

	/**
	 * 対象の日付をチェックして、文字色と背景色を変更する。
	 * 
	 * @param cellParams
	 *            セルのスタイルパラメータ
	 * @param data
	 *            社宅管理台帳データー
	 * @param targetIdx
	 *            対象のインデックス
	 * @param sysNengetsu
	 *            処理年月
	 * @return セルのスタイルパラメータ
	 */
	private Map<String, Object> changeCellParams(Map<String, Object> cellParams,
			Skf3030Rp002GetShatakuDaichoInfoExp data, String targetIdx, String sysNengetsu) {

		if (!NfwStringUtils.isEmpty(data.getNyukyoDate()) && NfwStringUtils.isEmpty(data.getTaikyoDate())) {
			String nyukyoNengetsu = data.getNyukyoDate().substring(0, 6);

			if (sysNengetsu.equals(nyukyoNengetsu)) {
				cellParams = changeCellStyle(cellParams, Rp002Info.NYUKYO_DATE.col, targetIdx,
						IndexedColors.RED.getIndex(), IndexedColors.YELLOW.getIndex());

			} else if (Integer.parseInt(sysNengetsu) < Integer.parseInt(nyukyoNengetsu)) {
				cellParams = changeCellStyle(cellParams, Rp002Info.NYUKYO_DATE.col, targetIdx,
						IndexedColors.BLUE.getIndex(), IndexedColors.LEMON_CHIFFON.getIndex());
			}
		}

		if (!NfwStringUtils.isEmpty(data.getParkingStartDate()) && NfwStringUtils.isEmpty(data.getParkingEndDate())) {
			String parkingStartNengetsu = data.getParkingStartDate().substring(0, 6);

			if (sysNengetsu.equals(parkingStartNengetsu)) {
				cellParams = changeCellStyle(cellParams, Rp002Info.PARKING_STARTDATE.col, targetIdx,
						IndexedColors.RED.getIndex(), IndexedColors.YELLOW.getIndex());

			} else if (Integer.parseInt(sysNengetsu) < Integer.parseInt(parkingStartNengetsu)) {
				cellParams = changeCellStyle(cellParams, Rp002Info.PARKING_STARTDATE.col, targetIdx,
						IndexedColors.BLUE.getIndex(), IndexedColors.LEMON_CHIFFON.getIndex());
			}
		}

		if (!NfwStringUtils.isEmpty(data.getTaikyoDate())) {
			String taikyoNengetsu = data.getTaikyoDate().substring(0, 6);

			if (sysNengetsu.equals(taikyoNengetsu)) {
				cellParams = changeCellStyle(cellParams, Rp002Info.TAIKYO_DATE.col, targetIdx,
						IndexedColors.RED.getIndex(), IndexedColors.LIME.getIndex());

			} else if (Integer.parseInt(sysNengetsu) < Integer.parseInt(taikyoNengetsu)) {
				cellParams = changeCellStyle(cellParams, Rp002Info.TAIKYO_DATE.col, targetIdx,
						IndexedColors.BLUE.getIndex(), IndexedColors.LIGHT_GREEN.getIndex());
			}
		}

		if (!NfwStringUtils.isEmpty(data.getParkingEndDate())) {
			String parkingEndNengetsu = data.getParkingEndDate().substring(0, 6);

			if (sysNengetsu.equals(parkingEndNengetsu)) {
				cellParams = changeCellStyle(cellParams, Rp002Info.PARKING_ENDDATE.col, targetIdx,
						IndexedColors.RED.getIndex(), IndexedColors.LIME.getIndex());

			} else if (Integer.parseInt(sysNengetsu) < Integer.parseInt(parkingEndNengetsu)) {
				cellParams = changeCellStyle(cellParams, Rp002Info.PARKING_ENDDATE.col, targetIdx,
						IndexedColors.BLUE.getIndex(), IndexedColors.LIGHT_GREEN.getIndex());
			}
		}

		return cellParams;
	}

	/**
	 * セルのスタイルを変更する。
	 * 
	 * @param cellParams
	 *            セルのスタイルパラメータ
	 * @param fontColorChgCol
	 *            フォントカラー変更対象カラム
	 * @param targetIdx
	 *            対象のインデックス
	 * @param fontColor
	 *            フォントカラー
	 * @param bgColor
	 *            バックグラウンドカラー
	 * @return セルのスタイルパラメータ
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> changeCellStyle(Map<String, Object> cellParams, String fontColorChgCol,
			String targetIdx, short fontColor, short bgColor) {

		List<String> cellChgList = getTagetChgCellArry(cellParams);

		for (int i = 0; i < cellChgList.size(); i++) {
			Map<String, Object> cellParam = (Map<String, Object>) cellParams.get(cellChgList.get(i) + targetIdx);
			cellParam.put(Skf3030Sc001SharedService.FONT_BG_COLOR_KEY, bgColor);
			cellParams.put(cellChgList.get(i) + targetIdx, cellParam);

			if (fontColorChgCol.equals((cellChgList.get(i)))) {
				Map<String, Object> fontParam = (Map<String, Object>) cellParam
						.get(Skf3030Sc001SharedService.FONT_PARAM_KEY);
				fontParam.put(Skf3030Sc001SharedService.FONT_COLOR_KEY, fontColor);
				cellParam.put(Skf3030Sc001SharedService.FONT_PARAM_KEY, fontParam);
			}

			cellParams.put(cellChgList.get(i) + targetIdx, cellParam);
		}

		return cellParams;
	}

	/**
	 * セルスタイル変更対象を取得する。
	 * 
	 * @param cellParams
	 *            セルのスタイルパラメータ
	 * @param targetIdx
	 *            対象のセルインデックス
	 * @return セルスタイル変更対象リスト
	 */
	private List<String> getTagetChgCellArry(Map<String, Object> cellParams) {

		Rp002Info[] shatakKanriInfoArry = Rp002Info.values();
		List<String> rtnArry = new ArrayList<String>();

		for (int i = 0; i < shatakKanriInfoArry.length; i++) {
			String col = shatakKanriInfoArry[i].col;

			if (!Rp002Info.FIRST_DAY.col.equals(col) && !Rp002Info.LAST_DAY.col.equals(col)
					&& !Rp002Info.SENTAKUKI.col.equals(col) && !Rp002Info.REIZOKO.col.equals(col)
					&& !Rp002Info.DENSHIRENJI.col.equals(col) && !Rp002Info.SOJIKI.col.equals(col)
					&& !Rp002Info.SUIHANKI.col.equals(col) && !Rp002Info.TV.col.equals(col)
					&& !Rp002Info.TV_STAND.col.equals(col) && !Rp002Info.ZATAKU.col.equals(col)
					&& !Rp002Info.CABINET.col.equals(col) && !Rp002Info.AIRCON.col.equals(col)
					&& !Rp002Info.KATEN.col.equals(col) && !Rp002Info.SHOMEI.col.equals(col)
					&& !Rp002Info.GAS.col.equals(col) && !Rp002Info.BIHIN_TAIYO_DATE.col.equals(col)
					&& !Rp002Info.BIHIN_HENKYAKU_DATE.col.equals(col)) {

				rtnArry.add(shatakKanriInfoArry[i].col);
			}
		}

		return rtnArry;
	}

}
