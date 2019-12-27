/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3030.domain.service.skf3030sc001;

import static jp.co.c_nexco.nfw.core.constants.CommonConstant.NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.poi_v3_8.ss.usermodel.Cell;
import org.apache.poi_v3_9.ss.usermodel.IndexedColors;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Rp003.Skf3030Rp003GetShatakuDaichoDiffDataInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Rp003.Skf3030Rp003GetShatakuDaichoInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Rp003.Skf3030Rp003PreMonthCompareDataExp;
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
import jp.co.c_nexco.skf.skf3030.domain.dto.skf3030sc001.Skf3030Sc001DownloadRp003Dto;

/**
 * Skf3030Sc001DownloadRp003Service 社宅管理台帳「前月比較一覧」出力Service
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf3030Sc001DownloadRp003Service extends BaseServiceAbstract<Skf3030Sc001DownloadRp003Dto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private Skf3030Sc001SharedService skf3030Sc001SharedService;

	private static final String SHOW_DIFF_DATA_COL = "A";
	private static final String FILE_NAME_KEY = "skf3030.skf3030_sc001.compare_premonth_data_template_file";
	private static final int START_LINE = 1;

	/** 前月比較確認リストデータエクセル出力情報 */
	private enum Rp003Info {
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
		RENTAL_ADJUST("CD", "ＭＳ Ｐゴシック", "11"), // 社宅使用料調整金
		PARKING_RENTAL_ADJUST("CE", "ＭＳ Ｐゴシック", "11"), // 駐車場使用料調整金
		KYOEKIHI_PERSON_ADJUST("CF", "ＭＳ Ｐゴシック", "11"), // 個人負担共益費調整金
		SENTAKUKI("CG", "ＭＳ Ｐゴシック", "11"), // 備品貸与データ_単身備品_①洗濯機
		REIZOKO("CH", "ＭＳ Ｐゴシック", "11"), // 備品貸与データ_単身備品_②冷蔵庫
		DENSHIRENJI("CI", "ＭＳ Ｐゴシック", "11"), // 備品貸与データ_単身備品_③電子ｵｰﾌﾞﾝﾚﾝｼﾞ
		SOJIKI("CJ", "ＭＳ Ｐゴシック", "11"), // 備品貸与データ_単身備品_④掃除機
		SUIHANKI("CK", "ＭＳ Ｐゴシック", "11"), // 備品貸与データ_単身備品_⑤電子炊飯ｼﾞｬｰ
		TV("CL", "ＭＳ Ｐゴシック", "11"), // 備品貸与データ_単身備品_⑥テレビ
		TV_STAND("CM", "ＭＳ Ｐゴシック", "11"), // 備品貸与データ_単身備品_⑦テレビ台
		ZATAKU("CN", "ＭＳ Ｐゴシック", "11"), // 備品貸与データ_単身備品_⑧座卓（こたつ）
		CABINET("CO", "ＭＳ Ｐゴシック", "11"), // 備品貸与データ_単身備品_⑨ｷｯﾁﾝｷｬﾋﾞﾈｯﾄ
		AIRCON("CP", "ＭＳ Ｐゴシック", "11"), // 備品貸与データ_課税対象外_⑩ﾙｰﾑｴｱｺﾝ
		KATEN("CQ", "ＭＳ Ｐゴシック", "11"), // 備品貸与データ_課税対象外_⑪カーテン
		SHOMEI("CR", "ＭＳ Ｐゴシック", "11"), // 備品貸与データ_課税対象外_⑫照明器具
		GAS("CS", "ＭＳ Ｐゴシック", "11"), // 備品貸与データ_課税対象外_⑬ｶﾞｽﾃｰﾌﾞﾙ
		BIHIN_TAIYO_DATE("CT", "ＭＳ Ｐゴシック", "11"), // 備品貸与データ_貸与日
		BIHIN_HENKYAKU_DATE("CU", "ＭＳ Ｐゴシック", "11"); // 備品貸与データ_返却日

		public String col;
		public String font;
		public String size;

		Rp003Info(String col, String font, String size) {
			this.col = col;
			this.font = font;
			this.size = size;
		}
	};

	@Value("${skf3030.skf3030_sc001.compare_premonth_data_output_row_Idx}")
	private String startRowIdx;
	@Value("${skf3030.skf3030_sc001.compare_premonth_data_file_name}")
	private String fileName;
	@Value("${skf3030.skf3030_sc001.this_month_data_sheet_name}")
	private String thisMonthSheetName;
	@Value("${skf3030.skf3030_sc001.pre_month_data_sheet_name}")
	private String preMonthSheetName;
	@Value("${skf3030.skf3030_sc001.output_compare_premonth_data_file_function_id}")
	private String funcId;

	@Override
	protected BaseDto index(Skf3030Sc001DownloadRp003Dto inDto) throws Exception {

		skfOperationLogUtils.setAccessLog("社宅管理台帳「前月比較データ」出力処理開始", CodeConstant.C001, inDto.getPageId());

		inDto = (Skf3030Sc001DownloadRp003Dto) skf3030Sc001SharedService.setDropDownSelect(inDto);

		inDto.setResultMessages(null);
		
		String yearMonth = inDto.getHdnYearSelect() + inDto.getHdnMonthSelect();

		Map<String, String> userInfo = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		String userId = userInfo.get("userName");

		if (NfwStringUtils.isEmpty(yearMonth) || NfwStringUtils.isEmpty(userId)) {
			LogUtils.debugByMsg("パラメータ不正。対象年月：" + yearMonth + "、 ユーザーID：" + userId);
			ServiceHelper.addErrorResultMessage(inDto, null, MessageIdConstant.E_SKF_1070);
			return inDto;
		}

		inDto = outputRp003(inDto, yearMonth);

		return inDto;
	}

	/**
	 * 「前月比較一覧」の帳票を出力する。 （データ取得、データ作成、データ出力）
	 * 
	 * @param inDto
	 *            Skf3030Sc001DownloadRp003Dto
	 * @param yearMonth
	 *            対象年月
	 * @return Skf3030Sc001DownloadRp003Dto
	 * @throws Exception
	 */
	private Skf3030Sc001DownloadRp003Dto outputRp003(Skf3030Sc001DownloadRp003Dto inDto, String yearMonth) {

		long procTime = System.currentTimeMillis();

		List<Skf3030Rp003GetShatakuDaichoInfoExp> daichoData = skf3030Sc001SharedService
				.getShatakuDaichoCompareInfo(yearMonth, true);
		if (daichoData.size() == 0) {
			ServiceHelper.addErrorResultMessage(inDto, null, MessageIdConstant.E_SKF_1067, "出力対象データ");
			return inDto;
		}

		String preYearMonth = skfDateFormatUtils.addYearMonth(yearMonth, -1);
		List<Skf3030Rp003GetShatakuDaichoInfoExp> preMonthDaichoData = skf3030Sc001SharedService
				.getShatakuDaichoCompareInfo(preYearMonth, true);

		List<Skf3030Rp003GetShatakuDaichoDiffDataInfoExp> diffData = skf3030Sc001SharedService
				.getShatakuDaichoDiffDataInfo(yearMonth);

		List<Skf3030Rp003GetShatakuDaichoDiffDataInfoExp> preDiffData = skf3030Sc001SharedService
				.getShatakuDaichoDiffDataInfo(preYearMonth);

		long endTime = System.currentTimeMillis();
		LogUtils.debugByMsg("データ取得処理時間：" + (endTime - procTime) + "ms");
		inDto = outputExcelData(inDto, yearMonth, daichoData, preMonthDaichoData, diffData, preDiffData);

		return inDto;
	}

	/**
	 * エクセルデータを作成して出力する。
	 * 
	 * @param daichoDataList
	 *            社宅管理台帳データ
	 * @param inDto
	 *            Skf3030Sc001OutputExcelRp003Dto
	 * @param yearMonth
	 *            対象年月
	 * @param preMonthDaichoDataList
	 *            前月の社宅管理台帳データ
	 * @param diffDataList
	 *            当月の比較対象のデータ
	 * @param preDiffDataList
	 *            前月の比較対象のデータ
	 * @return Skf3030Sc001OutputExcelRp003Dto
	 * @throws Exception
	 */
	private Skf3030Sc001DownloadRp003Dto outputExcelData(Skf3030Sc001DownloadRp003Dto inDto, String yearMonth,
			List<Skf3030Rp003GetShatakuDaichoInfoExp> daichoDataList,
			List<Skf3030Rp003GetShatakuDaichoInfoExp> preMonthDaichoDataList,
			List<Skf3030Rp003GetShatakuDaichoDiffDataInfoExp> diffDataList,
			List<Skf3030Rp003GetShatakuDaichoDiffDataInfoExp> preDiffDataList) {

		List<Skf3030Rp003PreMonthCompareDataExp> nowDataList = new ArrayList<Skf3030Rp003PreMonthCompareDataExp>();
		List<Skf3030Rp003PreMonthCompareDataExp> preDataList = new ArrayList<Skf3030Rp003PreMonthCompareDataExp>();

		for (int i = 0; i < daichoDataList.size(); i++) {
			Skf3030Rp003GetShatakuDaichoInfoExp daichoData = daichoDataList.get(i);

			Long shatakuKanriNo = daichoData.getShatakuKanriNo();
			Long roomKanriNo = daichoData.getShatakuRoomKanriNo();
			Long kanriId = daichoData.getShatakuKanriId();

			Skf3030Rp003GetShatakuDaichoDiffDataInfoExp targetNowDiffData = null;
			for (Skf3030Rp003GetShatakuDaichoDiffDataInfoExp diffData : diffDataList) {
				if (shatakuKanriNo.equals(diffData.getShatakuKanriNo())
						&& roomKanriNo.equals(diffData.getShatakuRoomKanriNo())
						&& kanriId.equals(diffData.getShatakuKanriId())) {
					targetNowDiffData = diffData;
					break;
				}
			}

			Skf3030Rp003GetShatakuDaichoDiffDataInfoExp targetPreDiffData = null;
			for (Skf3030Rp003GetShatakuDaichoDiffDataInfoExp preDiffData : preDiffDataList) {
				if (shatakuKanriNo.equals(preDiffData.getShatakuKanriNo())
						&& roomKanriNo.equals(preDiffData.getShatakuRoomKanriNo())
						&& kanriId.equals(preDiffData.getShatakuKanriId())) {
					targetPreDiffData = preDiffData;
					break;
				}
			}

			if (targetNowDiffData != null && targetPreDiffData != null) {
				if (Objects.deepEquals(targetNowDiffData, targetPreDiffData)) {
					continue;
				}
			}

			Skf3030Rp003PreMonthCompareDataExp nowData = createExcelData(daichoData);

			Skf3030Rp003GetShatakuDaichoInfoExp tagetKanriIdPreData = null;
			for (Skf3030Rp003GetShatakuDaichoInfoExp preMonthDaichoData : preMonthDaichoDataList) {
				if (kanriId.equals(preMonthDaichoData.getShatakuKanriId())) {
					tagetKanriIdPreData = preMonthDaichoData;
					break;
				}
			}

			Skf3030Rp003PreMonthCompareDataExp preData = null;
			if (tagetKanriIdPreData != null) {
				preData = createExcelData(tagetKanriIdPreData);
			}

			if (targetNowDiffData != null) {
				nowData = setDiffDataInfo(targetNowDiffData, targetPreDiffData, nowData);
			}

			if (nowData != null) {
				nowDataList.add(nowData);
			}

			if (preData != null) {
				preDataList.add(preData);
			}
		}

		if (nowDataList.size() == 0 && preDataList.size() == 0) {
			ServiceHelper.addErrorResultMessage(inDto, null, MessageIdConstant.E_SKF_1067, "出力対象データ");
			return inDto;
		}

		inDto = setCompareData(inDto, yearMonth, nowDataList, preDataList);

		return inDto;
	}

	/**
	 * 帳票へ出力する為のデータを作成する。（1行分）
	 * 
	 * @param daichoData
	 *            社宅管理台帳データ
	 * @return 出力データ
	 */
	private Skf3030Rp003PreMonthCompareDataExp createExcelData(Skf3030Rp003GetShatakuDaichoInfoExp daichoData) {

		Skf3030Rp003PreMonthCompareDataExp rtnData = new Skf3030Rp003PreMonthCompareDataExp();

		if (!NfwStringUtils.isEmpty(daichoData.getAgencyExternalKbn())
				&& !"1".equals(daichoData.getAgencyExternalKbn())) {
			rtnData.setManageCompanyCd(
					skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getManageBusinessAreaCd()));
		} else {
			rtnData.setManageCompanyCd(
					skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getManageShatakuCompanyCd()));
		}

		rtnData.setManageCompanyName(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getCompanyName()));
		rtnData.setManageAgencyCd(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getManageBusinessAreaCd()));
		rtnData.setManageAgencyName(
				skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getManageBusinessAreaName()));

		Map<String, String> shatakKbnMap = skfGenericCodeUtils
				.getGenericCode(FunctionIdConstant.GENERIC_CODE_SHATAKU_KBN);
		rtnData.setHoyuKariage(shatakKbnMap.get(daichoData.getShatakKbn()));

		rtnData.setShatakName(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getShatakName()));
		rtnData.setRoomNo(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getRoomNo()));

		if (!NfwStringUtils.isEmpty(daichoData.getLendKbn()) && !"1".equals(daichoData.getLendKbn())) {
			rtnData.setKaihaitoKeikaku(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getLendKbnHosoku()));
		} else {
			rtnData.setKaihaitoKeikaku("");
		}

		String prefCd = skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getPrefCd());
		rtnData.setPrefCd(prefCd);

		String prefName = "";
		Map<String, String> prefCdMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_PREFCD);
		if (prefCdMap.get(prefCd) != null && !Skf3030Sc001SharedService.CD_PREF_OTHER.equals(prefCd)) {
			prefName = prefCdMap.get(prefCd);
		}
		rtnData.setAddress(prefName + skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getAddress()));

		Map<String, String> orgKikakMap = skfGenericCodeUtils
				.getGenericCode(FunctionIdConstant.GENERIC_CODE_KIKAKU_KBN);
		rtnData.setOriginalKikaku(orgKikakMap.get(daichoData.getOriginalKikaku()));

		rtnData.setOriginalMenseki(String.valueOf(daichoData.getOriginalMenseki()));

		Map<String, String> areaKbnMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_AREA_KBN);
		rtnData.setArea(areaKbnMap.get(daichoData.getAreaKbn()));

		Map<String, String> parkStrucKbnMap = skfGenericCodeUtils
				.getGenericCode(FunctionIdConstant.GENERIC_CODE_PARKING_STRUCTURE_KBN);
		rtnData.setParkingStructure(parkStrucKbnMap.get(daichoData.getParkingStructureKbn()));

		rtnData.setTaiyoKikaku(orgKikakMap.get(daichoData.getKikaku()));
		rtnData.setTaishoMenseki(String.valueOf(daichoData.getMenseki()));

		Map<String, String> auseKbnMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_AUSE_KBN);
		rtnData.setRyoGemmen07(auseKbnMap.get(daichoData.getAuse()));

		Map<String, String> kyoshoKbnMap = skfGenericCodeUtils
				.getGenericCode(FunctionIdConstant.GENERIC_CODE_KYOSHOADJUST_KBN);
		rtnData.setKyoshoGemmen(kyoshoKbnMap.get(daichoData.getKyosyoFlg()));

		String kanreitiFlg = skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getKanreitiFlg());
		if (!"0".equals(kanreitiFlg)) {
			Map<String, String> kanreitiKbnMap = skfGenericCodeUtils
					.getGenericCode(FunctionIdConstant.GENERIC_CODE_KANREITIADJUST_KBN);
			rtnData.setKanreichiGemmen(kanreitiKbnMap.get(kanreitiFlg));
		} else {
			rtnData.setKanreichiGemmen("");
		}

		Map<String, String> yakuinKbnMap = skfGenericCodeUtils
				.getGenericCode(FunctionIdConstant.GENERIC_CODE_YAKUIN_KBN);
		rtnData.setYakuinTekiyo(yakuinKbnMap.get(daichoData.getYakuinSannteiKbn()));

		rtnData.setRentalTotal(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getRentalMonth()));
		rtnData.setRentalAdjust(String.valueOf(daichoData.getRentalAdjust()));
		rtnData.setParkingRentalAdjust(String.valueOf(daichoData.getParkingRentalAdjust()));
		rtnData.setParkingRentalTotal(String.valueOf(daichoData.getParkingRentalMonth()));

		Map<String, String> kyoekihiPayMonthMap = skfGenericCodeUtils
				.getGenericCode(FunctionIdConstant.GENERIC_CODE_KYOEKIHI_PAY_MONTH_KBN);
		rtnData.setKyoekihiPayMonth(kyoekihiPayMonthMap.get(daichoData.getKyoekihiPayMonth()));

		rtnData.setKyoekihiPerson(String.valueOf(daichoData.getNowKyoekihiPerson()));

		if (!NfwStringUtils.isEmpty(daichoData.getGensekiHanteiKbn())) {
			Map<String, String> gensekiHanteiKbnMap = skfGenericCodeUtils
					.getGenericCode(FunctionIdConstant.GENERIC_CODE_KYOJUSHA_KBN);
			rtnData.setGensekichigaiHantei(gensekiHanteiKbnMap.get(daichoData.getGensekiHanteiKbn()));
		} else {
			rtnData.setGensekichigaiHantei("");
		}

		rtnData.setKikanName(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getAgencyName()));
		rtnData.setKikanCd(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getAgencyCd()));
		rtnData.setShitsuBuName(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getAffiliation1Name()));
		rtnData.setShitsuBuCd(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getAffiliation1Cd()));
		rtnData.setKaName(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getAffiliation2Name()));
		rtnData.setKaCd(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getAffiliation2Cd()));
		rtnData.setShainName(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getName()));
		rtnData.setShainNo(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getShainNo()));
		rtnData.setBusinessAreaCd(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getBusinessAreaCd()));
		rtnData.setBusinessAreaName(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getBusinessAreaName()));
		rtnData.setPreBusinessAreaCd(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getPreBusinessAreaCd()));
		rtnData.setPreBusinessAreaName(
				skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getPreBusinessAreaName()));
		rtnData.setNyukyoDate(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getNyukyoDate()));
		rtnData.setTaikyoDate(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getTaikyoDate()));
		rtnData.setKyoekihi(String.valueOf(daichoData.getKyoekihiPersonTotal()));
		rtnData.setBiko(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getBikoLedger()));
		rtnData.setOriginalCompanyName(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getOriginalName()));
		rtnData.setPayCompanyName(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getPayName()));

		if (!NfwStringUtils.isEmpty(daichoData.getOriginalAgencyExternalKbn())
				&& !"1".equals(daichoData.getOriginalAgencyExternalKbn())) {
			rtnData.setOriginalCompanyCd(
					skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getOriginalCompanyCd()));
		} else {
			rtnData.setOriginalCompanyCd(
					skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getOriginalShatakuCompanyCd()));
		}

		if (!NfwStringUtils.isEmpty(daichoData.getPayAgencyExternalKbn())
				&& !"1".equals(daichoData.getPayAgencyExternalKbn())) {
			rtnData.setPayCompanyCd(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getPayCompanyCd()));
		} else {
			rtnData.setPayCompanyCd(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getPayShatakuCompanyCd()));
		}

		if (daichoData.getKyoekihiPersonTotal() != 0) {
			rtnData.setKyoekiKojyoCompany(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getPayName()));
		} else {
			rtnData.setKyoekiKojyoCompany("");
		}

		rtnData.setShatakuAccountKamoku(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getShatakuAccountCd())
				+ skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getShatakuAccountName()));
		rtnData.setKyoekiAccountKamoku(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getKyoekiAccountCd())
				+ skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getKyoekiAccountName()));

		Map<String, String> mutualUseKbnMap = skfGenericCodeUtils
				.getGenericCode(FunctionIdConstant.GENERIC_CODE_MUTUALUSE_KBN);
		rtnData.setMutualUse(mutualUseKbnMap.get(daichoData.getMutualUseKbn()));

		rtnData.setKashitukeCompany(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getKashitukeName()));
		rtnData.setKariukeCompany(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getKariukeName()));
		rtnData.setMutualUseStartDay(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getMutualUseStartDay()));
		rtnData.setMutualUseEndDay(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getMutualUseEndDay()));

		Map<String, String> compTransKbnMap = skfGenericCodeUtils
				.getGenericCode(FunctionIdConstant.GENERIC_CODE_COMPANY_TRANSFER_DISP_KBN);
		rtnData.setShatakuCompanyTransfer(compTransKbnMap.get(daichoData.getShatakuCompanyTransferKbn()));
		rtnData.setKyoekihiCompanyTransfer(compTransKbnMap.get(daichoData.getKyoekihiCompanyTransferKbn()));

		rtnData.setAssignCompanyName(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getAssignName()));
		rtnData.setAssignAgencyName(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getAssignAgencyName()));
		rtnData.setAssignAffiliation1(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getAssignAffiliation1()));
		rtnData.setAssignAffiliation2(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getAssignAffiliation2()));
		rtnData.setAssignCd(skf3030Sc001SharedService.cnvEmptyStrToNull(daichoData.getAssignCd()));
		rtnData.setNyukyoYoto(auseKbnMap.get(daichoData.getAuse()));

		if (!NfwStringUtils.isEmpty(daichoData.getParkingCnt())) {
			rtnData.setParkingCount(daichoData.getParkingCnt());
		} else {
			rtnData.setParkingCount("0");
		}

		rtnData.setParkingStartDate("");
		rtnData.setParkingEndDate("");

		String parkStartDate1 = daichoData.getParkingStartDate1();
		String parkStartDate2 = daichoData.getParkingStartDate2();
		String parkEndDate1 = daichoData.getParkingEndDate1();
		String parkEndDate2 = daichoData.getParkingEndDate2();

		if ("1".equals(daichoData.getParkingCnt())) {
			if (!NfwStringUtils.isEmpty(parkStartDate1)) {
				daichoData.setParkingStartDate(parkStartDate1);
				rtnData.setParkingStartDate(parkStartDate1);

				if (!NfwStringUtils.isEmpty(parkEndDate1)) {
					daichoData.setParkingEndDate(parkEndDate1);
					rtnData.setParkingEndDate(parkEndDate1);
				}

			} else if (!NfwStringUtils.isEmpty(parkStartDate2)) {
				daichoData.setParkingStartDate(parkStartDate2);
				rtnData.setParkingStartDate(parkStartDate2);

				if (!NfwStringUtils.isEmpty(parkEndDate2)) {
					daichoData.setParkingEndDate(parkEndDate2);
					rtnData.setParkingEndDate(parkEndDate2);
				}
			}

		} else if ("2".equals(daichoData.getParkingCnt())) {
			if (!NfwStringUtils.isEmpty(parkEndDate1)) {
				daichoData.setParkingStartDate(parkStartDate1);
				rtnData.setParkingStartDate(parkStartDate1);
			}

			if (!NfwStringUtils.isEmpty(parkStartDate2)) {
				if (!NfwStringUtils.isEmpty(parkStartDate1)) {

					if (Integer.parseInt(parkStartDate1) > Integer.parseInt(parkStartDate2)) {
						daichoData.setParkingStartDate(parkStartDate2);
						rtnData.setParkingStartDate(parkStartDate2);

					} else {
						daichoData.setParkingStartDate(parkStartDate1);
						rtnData.setParkingStartDate(parkStartDate1);
					}

				} else {
					daichoData.setParkingStartDate(parkStartDate2);
					rtnData.setParkingStartDate(parkStartDate2);
				}
			}

			if (!NfwStringUtils.isEmpty(parkEndDate1)) {
				daichoData.setParkingEndDate(parkEndDate1);
				rtnData.setParkingEndDate(parkEndDate1);
			}

			if (!NfwStringUtils.isEmpty(parkEndDate2)) {
				if (!NfwStringUtils.isEmpty(parkEndDate1)) {

					if (Integer.parseInt(parkEndDate1) > Integer.parseInt(parkEndDate2)) {
						daichoData.setParkingEndDate(parkEndDate2);
						rtnData.setParkingEndDate(parkEndDate2);

					} else {
						daichoData.setParkingEndDate(parkEndDate1);
						rtnData.setParkingEndDate(parkEndDate1);
					}

				} else {
					daichoData.setParkingEndDate(parkEndDate2);
					rtnData.setParkingEndDate(parkEndDate2);
				}
			}
		}

		rtnData.setKyoekihiPersonAdjust(String.valueOf(daichoData.getKyoekihiPersonAdjust()));

		Map<String, String> bihinStsKbnMap = skf3030Sc001SharedService.getBihinLentStsMap();
		rtnData.setSentakuki(bihinStsKbnMap.get(daichoData.getSentakukiLentStatusKbn()));
		rtnData.setReizoko(bihinStsKbnMap.get(daichoData.getReizokoLentStatusKbn()));
		rtnData.setDenshirenji(bihinStsKbnMap.get(daichoData.getDenshirenjiLentStatusKbn()));
		rtnData.setSojiki(bihinStsKbnMap.get(daichoData.getSojikiLentStatusKbn()));
		rtnData.setSuihanki(bihinStsKbnMap.get(daichoData.getSuihankiLentStatusKbn()));
		rtnData.setTv(bihinStsKbnMap.get(daichoData.getTvLentStatusKbn()));
		rtnData.setTvStand(bihinStsKbnMap.get(daichoData.getTvStandLentStatusKbn()));
		rtnData.setZataku(bihinStsKbnMap.get(daichoData.getZatakuLentStatusKbn()));
		rtnData.setCabinet(bihinStsKbnMap.get(daichoData.getCabinetLentStatusKbn()));
		rtnData.setAircon(bihinStsKbnMap.get(daichoData.getAirconLentStatusKbn()));
		rtnData.setKaten(bihinStsKbnMap.get(daichoData.getKatenLentStatusKbn()));
		rtnData.setShomei(bihinStsKbnMap.get(daichoData.getShomeiLentStatusKbn()));
		rtnData.setGas(bihinStsKbnMap.get(daichoData.getGasLentStatusKbn()));
		rtnData.setTaiyoDate(bihinStsKbnMap.get(daichoData.getTaiyoDate()));
		rtnData.setHenkyakuDate(bihinStsKbnMap.get(daichoData.getHenkyakuDate()));

		return rtnData;
	}

	/**
	 * データを比較し、データが異なる箇所を設定する。
	 * 
	 * @param nowDiffData
	 *            当月データ
	 * @param preDiffData
	 *            前月データ
	 * @param targetData
	 *            結果保持用のデータ
	 * @return 結果保持用のデータ
	 */
	private Skf3030Rp003PreMonthCompareDataExp setDiffDataInfo(Skf3030Rp003GetShatakuDaichoDiffDataInfoExp nowDiffData,
			Skf3030Rp003GetShatakuDaichoDiffDataInfoExp preDiffData, Skf3030Rp003PreMonthCompareDataExp targetData) {

		if (preDiffData == null) {

			if (!NfwStringUtils.isEmpty(nowDiffData.getYakuinSannteiKbn())
					&& !"0".equals(nowDiffData.getYakuinSannteiKbn())) {
				targetData.setDiffYakuinSannteiKbn(true);
			}

			if (!NfwStringUtils.isEmpty(nowDiffData.getRentalMonth())) {
				targetData.setDiffRentalMonth(true);
			}

			if (nowDiffData.getParkingRental() != null) {
				targetData.setDiffParkingRental(true);
			}

			if (nowDiffData.getKyoekihiPerson() != null) {
				targetData.setDiffKyoekihiPerson(true);
			}

			if (!NfwStringUtils.isEmpty(nowDiffData.getName())) {
				targetData.setDiffName(true);
			}

			if (!NfwStringUtils.isEmpty(nowDiffData.getShainNo())) {
				targetData.setDiffShainNo(true);
			}

			if (!NfwStringUtils.isEmpty(nowDiffData.getPreBusinessAreaCd())) {
				targetData.setDiffPreBusinessAreaCd(true);
			}

			if (!NfwStringUtils.isEmpty(nowDiffData.getPreBusinessAreaName())) {
				targetData.setDiffPreBusinessAreaName(true);
			}

			if (!NfwStringUtils.isEmpty(nowDiffData.getBusinessAreaCd())) {
				targetData.setDiffBusinessAreaCd(true);
			}

			if (!NfwStringUtils.isEmpty(nowDiffData.getBusinessAreaName())) {
				targetData.setDiffBusinessAreaName(true);
			}

			if (nowDiffData.getRentalTotal() != 0) {
				targetData.setDiffRentalTotal(true);
			}

			if (nowDiffData.getParkingRentalTotal() != 0) {
				targetData.setDiffParkingRentalTotal(true);
			}

			int nowShatakuOrCar = nowDiffData.getRentalTotal() + nowDiffData.getParkingRentalTotal();
			if (nowShatakuOrCar != 0) {
				targetData.setDiffShatakuOrCar(true);
			}

			if (nowDiffData.getKyoekihiPersonTotal() != 0) {
				targetData.setDiffKyoekihiPersonTotal(true);
			}

			if ((nowShatakuOrCar + nowDiffData.getKyoekihiPersonTotal()) != 0) {
				targetData.setDiffChoshuTotal(true);
			}

			if (nowDiffData.getRentalAdjust() != 0) {
				targetData.setDiffRentalAdjust(true);
			}

			if (nowDiffData.getParkingRentalAdjust() != 0) {
				targetData.setDiffParkingRentalAdjust(true);
			}

			if (nowDiffData.getKyoekihiPersonAdjust() != 0) {
				targetData.setDiffKyoekihiPersonAdjust(true);
			}

			if (!NfwStringUtils.isEmpty(nowDiffData.getAssignCompanyCd())) {
				targetData.setDiffAssignCompanyCd(true);
			}

			if (!NfwStringUtils.isEmpty(nowDiffData.getAssignAffiliation1())) {
				targetData.setDiffAssignAffiliation1(true);
			}

			if (!NfwStringUtils.isEmpty(nowDiffData.getAssignAffiliation2())) {
				targetData.setDiffAssignAffiliation2(true);
			}

			if (!NfwStringUtils.isEmpty(nowDiffData.getAssignAgencyName())) {
				targetData.setDiffAssignAgencyName(true);
			}

			if (!NfwStringUtils.isEmpty(nowDiffData.getNyukyoDate())) {
				targetData.setDiffNyukyoDate(true);
			}

			if (!NfwStringUtils.isEmpty(nowDiffData.getTaikyoDate())) {
				targetData.setDiffTaikyoDate(true);
			}

			if (!NfwStringUtils.isEmpty(nowDiffData.getParkingStartDate())) {
				targetData.setDiffParkingStartDate(true);
			}

			if (!NfwStringUtils.isEmpty(nowDiffData.getParkingEndDate())) {
				targetData.setDiffParkingEndDate(true);
			}

		} else {
			if (isDiffData(nowDiffData.getYakuinSannteiKbn(), preDiffData.getYakuinSannteiKbn())) {
				targetData.setDiffYakuinSannteiKbn(true);
			}

			if (isDiffData(nowDiffData.getRentalMonth(), preDiffData.getRentalMonth())) {
				targetData.setDiffRentalMonth(true);
			}

			if (isDiffData(nowDiffData.getParkingRental(), preDiffData.getParkingRental())) {
				targetData.setDiffParkingRental(true);
			}

			if (isDiffData(nowDiffData.getKyoekihiPerson(), preDiffData.getKyoekihiPerson())) {
				targetData.setDiffKyoekihiPerson(true);
			}

			if (isDiffData(nowDiffData.getShainNo(), preDiffData.getShainNo())) {
				targetData.setDiffShainNo(true);
			}

			if (isDiffData(nowDiffData.getName(), preDiffData.getName())) {
				targetData.setDiffName(true);
			}

			if (isDiffData(nowDiffData.getPreBusinessAreaCd(), preDiffData.getPreBusinessAreaCd())) {
				targetData.setDiffPreBusinessAreaCd(true);
			}

			if (isDiffData(nowDiffData.getPreBusinessAreaName(), preDiffData.getPreBusinessAreaName())) {
				targetData.setDiffPreBusinessAreaName(true);
			}

			if (isDiffData(nowDiffData.getBusinessAreaCd(), preDiffData.getBusinessAreaCd())) {
				targetData.setDiffBusinessAreaCd(true);
			}

			if (isDiffData(nowDiffData.getBusinessAreaName(), preDiffData.getBusinessAreaName())) {
				targetData.setDiffBusinessAreaName(true);
			}

			if (isDiffData(nowDiffData.getRentalTotal(), preDiffData.getRentalTotal())) {
				targetData.setDiffRentalTotal(true);
			}

			if (isDiffData(nowDiffData.getParkingRentalTotal(), preDiffData.getParkingRentalTotal())) {
				targetData.setDiffParkingRentalTotal(true);
			}

			int nowShatakuOrCar = nowDiffData.getRentalTotal() + nowDiffData.getParkingRentalTotal();
			int preShatakuOrCar = preDiffData.getRentalTotal() + preDiffData.getParkingRentalTotal();
			if (nowShatakuOrCar != preShatakuOrCar) {
				targetData.setDiffShatakuOrCar(true);
			}

			if (isDiffData(nowDiffData.getKyoekihiPersonTotal(), preDiffData.getKyoekihiPersonTotal())) {
				targetData.setDiffKyoekihiPersonTotal(true);
			}

			int nowChoshuTotal = nowShatakuOrCar + nowDiffData.getKyoekihiPersonTotal();
			int preChoshuTotal = preShatakuOrCar + preDiffData.getKyoekihiPersonTotal();
			if (nowChoshuTotal != preChoshuTotal) {
				targetData.setDiffChoshuTotal(true);
			}

			if (isDiffData(nowDiffData.getRentalAdjust(), preDiffData.getRentalAdjust())) {
				targetData.setDiffRentalAdjust(true);
			}

			if (isDiffData(nowDiffData.getParkingRentalAdjust(), preDiffData.getParkingRentalAdjust())) {
				targetData.setDiffParkingRentalAdjust(true);
			}

			if (isDiffData(nowDiffData.getKyoekihiPersonAdjust(), preDiffData.getKyoekihiPersonAdjust())) {
				targetData.setDiffKyoekihiPersonAdjust(true);
			}

			if (isDiffData(nowDiffData.getAssignCompanyCd(), preDiffData.getAssignCompanyCd())) {
				targetData.setDiffAssignCompanyCd(true);
			}

			if (isDiffData(nowDiffData.getAssignAffiliation1(), preDiffData.getAssignAffiliation1())) {
				targetData.setDiffAssignAffiliation1(true);
			}

			if (isDiffData(nowDiffData.getAssignAffiliation2(), preDiffData.getAssignAffiliation2())) {
				targetData.setDiffAssignAffiliation2(true);
			}

			if (isDiffData(nowDiffData.getAssignAgencyName(), preDiffData.getAssignAgencyName())) {
				targetData.setDiffAssignAgencyName(true);
			}

			if (isDiffData(nowDiffData.getNyukyoDate(), preDiffData.getNyukyoDate())) {
				targetData.setDiffNyukyoDate(true);
			}

			if (isDiffData(nowDiffData.getTaikyoDate(), preDiffData.getTaikyoDate())) {
				targetData.setDiffTaikyoDate(true);
			}

			if (isDiffData(nowDiffData.getParkingStartDate(), preDiffData.getParkingStartDate())) {
				targetData.setDiffParkingStartDate(true);
			}

			if (isDiffData(nowDiffData.getParkingEndDate(), preDiffData.getParkingEndDate())) {
				targetData.setDiffParkingEndDate(true);
			}
		}

		return targetData;
	}

	/**
	 * データを比較する。
	 * 
	 * @param val_1
	 *            1つ目の比較データ
	 * @param val_2
	 *            2つ目の比較データ
	 * @return 結果
	 */
	private boolean isDiffData(Object val_1, Object val_2) {

		boolean rtn = false;

		if (val_1 == null) {

			if (val_2 == null) {
				rtn = false;
			} else {
				rtn = true;
			}

		} else if (val_2 == null) {
			rtn = true;

		} else {

			if (val_1 instanceof String && val_2 instanceof String) {
				if (val_1.equals(val_2)) {
					rtn = false;
				} else {
					rtn = true;
				}

			} else {
				if (val_1 == val_2) {
					rtn = false;
				} else {
					rtn = true;
				}
			}
		}

		return rtn;
	}

	/**
	 * 「前月比較一覧」のデータをエクセルに出力する。
	 * 
	 * @param inDto
	 *            Skf3030Sc001OutputExcelRp003Dto
	 * @param yearMonth
	 *            対象年月
	 * @param thisMonthData
	 *            今月のデータ
	 * @param preMonthData
	 *            前月のデータ
	 * @return Skf3030Sc001OutputExcelRp003Dto
	 * @throws Exception
	 */
	private Skf3030Sc001DownloadRp003Dto setCompareData(Skf3030Sc001DownloadRp003Dto inDto, String yearMonth,
			List<Skf3030Rp003PreMonthCompareDataExp> thisMonthData,
			List<Skf3030Rp003PreMonthCompareDataExp> preMonthData) {

		List<RowDataBean> thisMonthDataBeanList = new ArrayList<>();
		Map<String, Object> cellParams = new HashMap<>();
		RowDataBean thisMonthDayData = new RowDataBean();

		String firstDay = yearMonth + "01";
		String editFirstDay = skf3030Sc001SharedService.cnvExcelDate(firstDay);
		thisMonthDayData.addCellDataBean(Rp003Info.FIRST_DAY.col, editFirstDay, Cell.CELL_TYPE_NUMERIC);

		String lastDay = skf3030Sc001SharedService.getLastDay(yearMonth + "01");
		String editLastDay = skf3030Sc001SharedService.cnvExcelDate(lastDay);
		thisMonthDayData.addCellDataBean(Rp003Info.LAST_DAY.col, editLastDay, Cell.CELL_TYPE_NUMERIC);

		cellParams = initTargetDayCellParams(cellParams);
		thisMonthDataBeanList.add(thisMonthDayData);

		int dayCnt = Integer.parseInt(lastDay) - Integer.parseInt(firstDay) + 1;

		int startIdx = Integer.parseInt(startRowIdx);

		for (int i = 0; i < thisMonthData.size(); i++) {

			String tagetRowIdx = String.valueOf(i + startIdx);

			Skf3030Rp003PreMonthCompareDataExp data = thisMonthData.get(i);

			RowDataBean rowData = createCellData(data, firstDay, lastDay, dayCnt, tagetRowIdx);

			thisMonthDataBeanList.add(rowData);

			cellParams = initCellParams(cellParams, thisMonthSheetName, tagetRowIdx);
			cellParams = setDiffDataCellParams(cellParams, data, thisMonthSheetName, tagetRowIdx);
		}

		List<RowDataBean> preMonthDataBeanList = new ArrayList<>();
		RowDataBean preMonthDayData = new RowDataBean();

		String preFirstDay = skfDateFormatUtils.addYearMonth(yearMonth, -1);
		String preEditFirstDay = skf3030Sc001SharedService.cnvExcelDate(firstDay);
		preMonthDayData.addCellDataBean(Rp003Info.FIRST_DAY.col, preEditFirstDay, Cell.CELL_TYPE_NUMERIC);

		String preLastDay = skf3030Sc001SharedService.getLastDay(preFirstDay + "01");
		String preEditLastDay = skf3030Sc001SharedService.cnvExcelDate(lastDay);
		preMonthDayData.addCellDataBean(Rp003Info.LAST_DAY.col, preEditLastDay, Cell.CELL_TYPE_NUMERIC);

		cellParams = initTargetDayCellParams(cellParams);
		preMonthDataBeanList.add(preMonthDayData);

		int preDayCnt = Integer.parseInt(preLastDay) - Integer.parseInt(preFirstDay) + 1;

		for (int i = 0; i < preMonthData.size(); i++) {

			String tagetRowIdx = String.valueOf(i + startIdx);

			Skf3030Rp003PreMonthCompareDataExp data = preMonthData.get(i);

			RowDataBean rowData = createCellData(data, preFirstDay, preLastDay, preDayCnt, tagetRowIdx);
			cellParams = initCellParams(cellParams, preMonthSheetName, tagetRowIdx);

			preMonthDataBeanList.add(rowData);
		}

		inDto = outputExcel(inDto, preMonthDataBeanList, thisMonthDataBeanList, cellParams);

		return inDto;
	}

	/**
	 * 1行分のデータを作成する。
	 * 
	 * @param data
	 *            セットするデータ
	 * @param firstDay
	 *            対象初日
	 * @param lastDay
	 *            対象終日
	 * @param dayCnt
	 *            対象日数
	 * @param tagetRowIdx
	 *            対象行のインデックス
	 * @return 行データ
	 */
	private RowDataBean createCellData(Skf3030Rp003PreMonthCompareDataExp data, String firstDay, String lastDay,
			int dayCnt, String tagetRowIdx) {

		RowDataBean rowData = new RowDataBean();

		rowData.addCellDataBean(Rp003Info.MANAGE_COMPANY_CD.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getManageCompanyCd()));
		rowData.addCellDataBean(Rp003Info.MANAGE_COMPANY_NAME.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getManageCompanyName()));
		rowData.addCellDataBean(Rp003Info.MANAGE_AGENCY_CD.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getManageAgencyCd()));
		rowData.addCellDataBean(Rp003Info.MANAGE_AGENCY_NAME.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getManageAgencyName()));
		rowData.addCellDataBean(Rp003Info.HOYU_KARIAGE.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getHoyuKariage()));
		rowData.addCellDataBean(Rp003Info.SHATAK_NAME.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getShatakName()));
		rowData.addCellDataBean(Rp003Info.ROOM_NO.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getRoomNo()));
		rowData.addCellDataBean(Rp003Info.KAIHAITO_KEIKAKU.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getKaihaitoKeikaku()));
		rowData.addCellDataBean(Rp003Info.PREF_CD.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getPrefCd()));
		rowData.addCellDataBean(Rp003Info.ADDRESS.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getAddress()));
		rowData.addCellDataBean(Rp003Info.ORIGINAL_KIKAKU.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getOriginalKikaku()));
		rowData.addCellDataBean(Rp003Info.ORIGINAL_MENSEKI.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getOriginalMenseki()), Cell.CELL_TYPE_NUMERIC,
				"#,#.##");
		rowData.addCellDataBean(Rp003Info.TAIYO_KIKAKU.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getTaiyoKikaku()));
		rowData.addCellDataBean(Rp003Info.TAISHO_MENSEK.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getTaishoMenseki()));
		rowData.addCellDataBean(Rp003Info.AREA.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getArea()));
		rowData.addCellDataBean(Rp003Info.RYO_GENMEN_07.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getRyoGemmen07()));
		rowData.addCellDataBean(Rp003Info.KYOSHO_GENMEN.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getKyoshoGemmen()));
		rowData.addCellDataBean(Rp003Info.KANREICHI_GENMEN.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getKanreichiGemmen()));
		rowData.addCellDataBean(Rp003Info.MENSEKI_GENMEN.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getMensekiGemmen()));
		rowData.addCellDataBean(Rp003Info.MENSEKI_GENMEN.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getMensekiGemmen()));
		rowData.addCellDataBean(Rp003Info.YAKUIN_TEKIYO.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getYakuinTekiyo()));
		rowData.addCellDataBean(Rp003Info.RENTAL_TOTAL.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getRentalTotal()), Cell.CELL_TYPE_NUMERIC, "#,#");
		rowData.addCellDataBean(Rp003Info.PARKING_STRUCTURE.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getParkingStructure()));
		rowData.addCellDataBean(Rp003Info.SHATAK_PARKING_RENTAL_ADJUST.col + tagetRowIdx,
				data.getParkingRentalAdjust());
		rowData.addCellDataBean(Rp003Info.PARKING_RENTAL_TOTAL.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getParkingRentalTotal()), Cell.CELL_TYPE_NUMERIC,
				"#,#");
		rowData.addCellDataBean(Rp003Info.KYOEKIHI_PAY_MONTH.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getKyoekihiPayMonth()));
		rowData.addCellDataBean(Rp003Info.KYOEKIHI_PERSON_TOTAL.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getKyoekihiPerson()), Cell.CELL_TYPE_NUMERIC, "#,#");
		rowData.addCellDataBean(Rp003Info.MUTUAL_USE.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getMutualUse()));
		rowData.addCellDataBean(Rp003Info.KASHITUKE_COMPANY.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getKashitukeCompany()));
		rowData.addCellDataBean(Rp003Info.KARIUKE_COMPANY.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getKariukeCompany()));

		if (!NfwStringUtils.isEmpty(data.getMutualUseStartDay())) {
			rowData.addCellDataBean(Rp003Info.MUTUAL_USE_STARTDAY.col + tagetRowIdx, skfDateFormatUtils
					.dateFormatFromString(data.getMutualUseStartDay(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH));
		} else {
			rowData.addCellDataBean(Rp003Info.MUTUAL_USE_STARTDAY.col + tagetRowIdx, "");
		}

		if (!NfwStringUtils.isEmpty(data.getMutualUseEndDay())) {
			rowData.addCellDataBean(Rp003Info.MUTUAL_USE_ENDDAY.col + tagetRowIdx, skfDateFormatUtils
					.dateFormatFromString(data.getMutualUseEndDay(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH));
		} else {
			rowData.addCellDataBean(Rp003Info.MUTUAL_USE_ENDDAY.col + tagetRowIdx, "");
		}

		rowData.addCellDataBean(Rp003Info.GENSEKI_CHIGAI_HANTEI.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getGensekichigaiHantei()));
		rowData.addCellDataBean(Rp003Info.SHATAK_COMPANY_TRANSFER.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getShatakuCompanyTransfer()));
		rowData.addCellDataBean(Rp003Info.KYOEKI_COMPANY_TRANSFER.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getKyoekihiCompanyTransfer()));
		rowData.addCellDataBean(Rp003Info.ORIGINAL_COMPANY_NAME.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getOriginalCompanyName()));
		rowData.addCellDataBean(Rp003Info.ORIGINAL_COMPANY_CD.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getOriginalCompanyCd()));
		rowData.addCellDataBean(Rp003Info.PAY_COMPANY_NAME.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getPayCompanyName()));
		rowData.addCellDataBean(Rp003Info.PAY_COMPANY_CD.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getPayCompanyCd()));
		rowData.addCellDataBean(Rp003Info.KIKAN_NAME.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getKikanName()));
		rowData.addCellDataBean(Rp003Info.KIKAN_CD.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getKikanCd()));
		rowData.addCellDataBean(Rp003Info.SHITSUBU_NAME.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getShitsuBuName()));
		rowData.addCellDataBean(Rp003Info.SHITSUBU_CD.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getShitsuBuCd()));
		rowData.addCellDataBean(Rp003Info.KA_NAME.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getKaName()));
		rowData.addCellDataBean(Rp003Info.KA_CD.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getKaCd()));
		rowData.addCellDataBean(Rp003Info.SHAIN_NAME.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getShainName()));
		rowData.addCellDataBean(Rp003Info.SHAIN_NO.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getShainNo()));
		rowData.addCellDataBean(Rp003Info.BUSINESS_AREA_CD.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getBusinessAreaCd()));
		rowData.addCellDataBean(Rp003Info.BUSINESS_AREA_NAME.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getBusinessAreaName()));
		rowData.addCellDataBean(Rp003Info.PRE_BUSINESS_AREA_CD.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getPreBusinessAreaCd()));
		rowData.addCellDataBean(Rp003Info.PRE_BUSINESS_AREA_NAME.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getPreBusinessAreaName()));
		rowData.addCellDataBean(Rp003Info.ASSING_COMPANY_NAME.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getAssignCompanyName()));
		rowData.addCellDataBean(Rp003Info.ASSING_AGENCY_NAME.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getAssignAgencyName()));
		rowData.addCellDataBean(Rp003Info.ASSING_AFFILIATION_1.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getAssignAffiliation1()));
		rowData.addCellDataBean(Rp003Info.ASSING_AFFILIATION_2.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getAssignAffiliation2()));
		rowData.addCellDataBean(Rp003Info.ASSING_CD.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getAssignCd()));
		rowData.addCellDataBean(Rp003Info.NYUKYO_YOTO.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getNyukyoYoto()));

		if (!NfwStringUtils.isEmpty(data.getNyukyoDate())) {
			rowData.addCellDataBean(Rp003Info.NYUKYO_DATE.col + tagetRowIdx, skfDateFormatUtils
					.dateFormatFromString(data.getNyukyoDate(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH));
		} else {
			rowData.addCellDataBean(Rp003Info.NYUKYO_DATE.col + tagetRowIdx, "");
		}

		if (!NfwStringUtils.isEmpty(data.getTaikyoDate())) {
			rowData.addCellDataBean(Rp003Info.TAIKYO_DATE.col + tagetRowIdx, skfDateFormatUtils
					.dateFormatFromString(data.getTaikyoDate(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH));
		} else {
			rowData.addCellDataBean(Rp003Info.TAIKYO_DATE.col + tagetRowIdx, "");
		}

		rowData.addCellDataBean(Rp003Info.PARKING_CNT.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getParkingCount()));

		if (!NfwStringUtils.isEmpty(data.getParkingStartDate())) {
			rowData.addCellDataBean(Rp003Info.PARKING_STARTDATE.col + tagetRowIdx, skfDateFormatUtils
					.dateFormatFromString(data.getParkingStartDate(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH));
		} else {
			rowData.addCellDataBean(Rp003Info.PARKING_STARTDATE.col + tagetRowIdx, "");
		}

		if (!NfwStringUtils.isEmpty(data.getParkingEndDate())) {
			rowData.addCellDataBean(Rp003Info.PARKING_ENDDATE.col + tagetRowIdx, skfDateFormatUtils
					.dateFormatFromString(data.getParkingEndDate(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH));
		} else {
			rowData.addCellDataBean(Rp003Info.PARKING_ENDDATE.col + tagetRowIdx, "");
		}

		rowData.addCellDataBean(Rp003Info.BIKO.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getBiko()));

		if (!NfwStringUtils.isEmpty(data.getRentalAdjust())) {
			rowData.addCellDataBean(Rp003Info.RENTAL_ADJUST.col + tagetRowIdx, data.getRentalAdjust(),
					Cell.CELL_TYPE_NUMERIC, "#,#");
		} else {
			rowData.addCellDataBean(Rp003Info.RENTAL_ADJUST.col + tagetRowIdx, "0", Cell.CELL_TYPE_NUMERIC, "#,#");
		}

		if (!NfwStringUtils.isEmpty(data.getParkingRentalAdjust())) {
			rowData.addCellDataBean(Rp003Info.PARKING_RENTAL_ADJUST.col + tagetRowIdx, data.getParkingRentalAdjust(),
					Cell.CELL_TYPE_NUMERIC, "#,#");
		} else {
			rowData.addCellDataBean(Rp003Info.PARKING_RENTAL_ADJUST.col + tagetRowIdx, "0", Cell.CELL_TYPE_NUMERIC,
					"#,#");
		}

		if (!NfwStringUtils.isEmpty(data.getKyoekihiPersonAdjust())) {
			rowData.addCellDataBean(Rp003Info.KYOEKIHI_PERSON_ADJUST.col + tagetRowIdx, data.getKyoekihiPersonAdjust(),
					Cell.CELL_TYPE_NUMERIC, "#,#");
		} else {
			rowData.addCellDataBean(Rp003Info.KYOEKIHI_PERSON_ADJUST.col + tagetRowIdx, "0", Cell.CELL_TYPE_NUMERIC,
					"#,#");
		}

		rowData.addCellDataBean(Rp003Info.SENTAKUKI.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getSentakuki()));
		rowData.addCellDataBean(Rp003Info.REIZOKO.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getReizoko()));
		rowData.addCellDataBean(Rp003Info.DENSHIRENJI.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getDenshirenji()));
		rowData.addCellDataBean(Rp003Info.SOJIKI.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getSojiki()));
		rowData.addCellDataBean(Rp003Info.SUIHANKI.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getSuihanki()));
		rowData.addCellDataBean(Rp003Info.TV.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getTv()));
		rowData.addCellDataBean(Rp003Info.TV_STAND.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getTvStand()));
		rowData.addCellDataBean(Rp003Info.ZATAKU.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getZataku()));
		rowData.addCellDataBean(Rp003Info.CABINET.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getCabinet()));
		rowData.addCellDataBean(Rp003Info.AIRCON.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getAircon()));
		rowData.addCellDataBean(Rp003Info.KATEN.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getKaten()));
		rowData.addCellDataBean(Rp003Info.SHOMEI.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getShomei()));
		rowData.addCellDataBean(Rp003Info.GAS.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getGas()));

		if (!NfwStringUtils.isEmpty(data.getTaiyoDate())) {
			rowData.addCellDataBean(Rp003Info.BIHIN_TAIYO_DATE.col + tagetRowIdx, skfDateFormatUtils
					.dateFormatFromString(data.getTaiyoDate(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH));
		} else {
			rowData.addCellDataBean(Rp003Info.BIHIN_TAIYO_DATE.col + tagetRowIdx, "");
		}

		if (!NfwStringUtils.isEmpty(data.getHenkyakuDate())) {
			rowData.addCellDataBean(Rp003Info.BIHIN_HENKYAKU_DATE.col + tagetRowIdx, skfDateFormatUtils
					.dateFormatFromString(data.getHenkyakuDate(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH));
		} else {
			rowData.addCellDataBean(Rp003Info.BIHIN_HENKYAKU_DATE.col + tagetRowIdx, "");
		}

		rowData.addCellDataBean(Rp003Info.SHATAK_KANJO_KAMOKU.col + tagetRowIdx,
				skf3030Sc001SharedService.cnvEmptyStrToNull(data.getShatakuAccountKamoku()));

		String shatakuNyukyoCnt = skf3030Sc001SharedService.calcDayNum(data.getNyukyoDate(), data.getTaikyoDate(),
				firstDay, lastDay);
		rowData.addCellDataBean(Rp003Info.SHATAK_NYUKYO_CNT.col + tagetRowIdx, shatakuNyukyoCnt);

		String parkingSiyoCnt = skf3030Sc001SharedService.calcDayNum(data.getParkingStartDate(),
				data.getParkingEndDate(), firstDay, lastDay);
		rowData.addCellDataBean(Rp003Info.PARKING_NYUKYO_CNT.col + tagetRowIdx, parkingSiyoCnt);

		int rentalTotal = 0;
		if (!NfwStringUtils.isEmpty(data.getRentalTotal())) {
			rentalTotal = Integer.parseInt(data.getRentalTotal());
		}

		int rentalAdjust = 0;
		if (!NfwStringUtils.isEmpty(data.getRentalAdjust())) {
			rentalAdjust = Integer.parseInt(data.getRentalAdjust());
		}

		int shatakCnt = 0;
		if (!NfwStringUtils.isEmpty(shatakuNyukyoCnt)) {
			shatakCnt = Integer.parseInt(shatakuNyukyoCnt);
		}

		int shatakSiyoryo = ((rentalTotal * shatakCnt) / dayCnt) + rentalAdjust;
		rowData.addCellDataBean(Rp003Info.SHATAK_TOTAL.col + tagetRowIdx, String.valueOf(shatakSiyoryo),
				Cell.CELL_TYPE_NUMERIC, "#,#");

		int parkRentalTotal = 0;
		if (!NfwStringUtils.isEmpty(data.getParkingRentalTotal())) {
			parkRentalTotal = Integer.parseInt(data.getParkingRentalTotal());
		}

		int parkRentalAdjust = 0;
		if (!NfwStringUtils.isEmpty(data.getParkingRentalAdjust())) {
			parkRentalAdjust = Integer.parseInt(data.getParkingRentalAdjust());
		}

		int parkCnt = 0;
		if (!NfwStringUtils.isEmpty(parkingSiyoCnt)) {
			parkCnt = Integer.parseInt(parkingSiyoCnt);
		}

		int parkingSiyoryo = ((parkRentalTotal * parkCnt) / dayCnt) + parkRentalAdjust;
		rowData.addCellDataBean(Rp003Info.PARKING_TOTAL.col + tagetRowIdx, String.valueOf(parkingSiyoryo),
				Cell.CELL_TYPE_NUMERIC, "#,#");

		int shatakuOrCar = shatakSiyoryo + parkingSiyoryo;
		rowData.addCellDataBean(Rp003Info.SHATAK_OR_CAR.col + tagetRowIdx, String.valueOf(shatakuOrCar),
				Cell.CELL_TYPE_NUMERIC, "#,#");

		String shatakuShunyuCompany = "";
		String shatakKojyoCompany = "";

		if (shatakuOrCar != 0) {
			shatakKojyoCompany = data.getPayCompanyName();

			if (Skf3030Sc001SharedService.MUTUAL_USE_ARI.equals(data.getMutualUse())) {
				shatakuShunyuCompany = data.getKariukeCompany();
			} else {
				shatakuShunyuCompany = data.getManageCompanyName();
			}
		} else {
			shatakKojyoCompany = data.getShatakuKojyoCompany();
		}

		rowData.addCellDataBean(Rp003Info.SHATAK_SHUNYU_COMPANY.col + tagetRowIdx, shatakuShunyuCompany);
		rowData.addCellDataBean(Rp003Info.SHATAK_KOJYO_COMPANY.col + tagetRowIdx, shatakKojyoCompany);

		String payCompCd = data.getPayCompanyCd();
		String shatakCompTrans = "";

		if (shatakuShunyuCompany.equals(shatakKojyoCompany)) {
			shatakCompTrans = Skf3030Sc001SharedService.KAISHAKAN_SOKIN_NASI;

		} else {
			if (!NfwStringUtils.isEmpty(payCompCd)) {
				if (Integer.parseInt(payCompCd) > 10) {
					shatakCompTrans = Skf3030Sc001SharedService.KAISHAKAN_SOKIN_SEIKYU;
				} else {
					shatakCompTrans = Skf3030Sc001SharedService.KAISHAKAN_SOKIN_SHIHARAI;
				}

			} else {
				shatakCompTrans = Skf3030Sc001SharedService.KAISHAKAN_SOKIN_NASI;
			}
		}
		rowData.addCellDataBean(Rp003Info.SHATAK_COMPANY_TRANSFER.col + tagetRowIdx, shatakCompTrans);

		String kyoekiUkeireCompany = "";
		String kyoekiKojyoCompany = "";

		if (!"0".equals(data.getKyoekihi())) {
			kyoekiKojyoCompany = data.getPayCompanyName();

			if (Skf3030Sc001SharedService.MUTUAL_USE_ARI.equals(data.getMutualUse())) {
				kyoekiUkeireCompany = data.getKariukeCompany();
			} else {
				kyoekiUkeireCompany = data.getManageCompanyCd();
			}
		}

		String kyoekiCompTrans = "";
		if (kyoekiUkeireCompany.equals(kyoekiKojyoCompany)) {
			kyoekiCompTrans = Skf3030Sc001SharedService.KAISHAKAN_SOKIN_NASI;

		} else {
			if (!NfwStringUtils.isEmpty(payCompCd)) {
				if (Integer.parseInt(payCompCd) > 10) {
					kyoekiCompTrans = Skf3030Sc001SharedService.KAISHAKAN_SOKIN_SEIKYU;
				} else {
					kyoekiCompTrans = Skf3030Sc001SharedService.KAISHAKAN_SOKIN_SHIHARAI;
				}

			} else {
				kyoekiCompTrans = Skf3030Sc001SharedService.KAISHAKAN_SOKIN_NASI;
			}
		}

		String kyoekihi = data.getKyoekihi();
		String kyoekiKanjoKamok = data.getKyoekiAccountKamoku();

		if (NfwStringUtils.isEmpty(kyoekihi)) {
			kyoekihi = "0";
			kyoekiKanjoKamok = CodeConstant.HYPHEN;
			kyoekiUkeireCompany = "";
			kyoekiKojyoCompany = "";
			kyoekiCompTrans = CodeConstant.HYPHEN;
		}

		rowData.addCellDataBean(Rp003Info.KYOEKIHI.col + tagetRowIdx, kyoekihi, Cell.CELL_TYPE_NUMERIC, "#,#");
		rowData.addCellDataBean(Rp003Info.KYOEKIHI_KANJO_KAMOKU.col + tagetRowIdx, kyoekiKanjoKamok);
		rowData.addCellDataBean(Rp003Info.KYOEKI_UKEIRE_COMPANY.col + tagetRowIdx, kyoekiUkeireCompany);
		rowData.addCellDataBean(Rp003Info.KYOEKI_KOJYO_COMPANY.col + tagetRowIdx, kyoekiKojyoCompany);
		rowData.addCellDataBean(Rp003Info.KYOEKI_COMPANY_TRANSFER.col + tagetRowIdx, kyoekiCompTrans);

		int chosyuTotal = shatakuOrCar + Integer.parseInt(kyoekihi);
		rowData.addCellDataBean(Rp003Info.CHOSHU_TOTAL.col + tagetRowIdx, String.valueOf(chosyuTotal),
				Cell.CELL_TYPE_NUMERIC, "#,#");

		// 未設定項目（何もセットしないとセルスタイルが変わらない為、空文字をセットしておく)
		rowData.addCellDataBean(Rp003Info.SONGAI_BAISHO.col + tagetRowIdx, "");
		rowData.addCellDataBean(Rp003Info.WORK_AREA_CD.col + tagetRowIdx, "");
		rowData.addCellDataBean(Rp003Info.WORK_AREA_PREF_NAME.col + tagetRowIdx, "");
		rowData.addCellDataBean(Rp003Info.NYUKYO_CHOSEI.col + tagetRowIdx, "");
		rowData.addCellDataBean(Rp003Info.PARKING_CHOSEI.col + tagetRowIdx, "");
		rowData.addCellDataBean(SHOW_DIFF_DATA_COL + tagetRowIdx, "");

		return rowData;
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
	private Skf3030Sc001DownloadRp003Dto outputExcel(Skf3030Sc001DownloadRp003Dto inDto,
			List<RowDataBean> preMonthDataBeanList, List<RowDataBean> thisMonthDataBeanList,
			Map<String, Object> cellparams) {

		List<SheetDataBean> sheetDataBeanList = new ArrayList<>();

		SheetDataBean preMonthSheetDataBean = new SheetDataBean();
		preMonthSheetDataBean.setSheetName(preMonthSheetName);
		preMonthSheetDataBean.setRowDataBeanList(preMonthDataBeanList);
		sheetDataBeanList.add(preMonthSheetDataBean);

		SheetDataBean thisMonthSheetDataBean = new SheetDataBean();
		thisMonthSheetDataBean.setSheetName(thisMonthSheetName);
		thisMonthSheetDataBean.setRowDataBeanList(thisMonthDataBeanList);
		sheetDataBeanList.add(thisMonthSheetDataBean);

		String excelFileName = fileName + DateTime.now().toString("YYYYMMddHHmmss")
				+ CodeConstant.DOT + CodeConstant.EXTENSION_XLSX;
		WorkBookDataBean wbdb = new WorkBookDataBean(excelFileName);
		wbdb.setSheetDataBeanList(sheetDataBeanList);

		Map<String, Object> resultMap = new HashMap<>();

		try {
			SkfFileOutputUtils.fileOutputExcelMultipleSheet(wbdb, cellparams, FILE_NAME_KEY, funcId, START_LINE, null,
					resultMap, 4);

		} catch (Exception e) {
			ServiceHelper.addErrorResultMessage(inDto, null, MessageIdConstant.E_SKF_1070);
			preMonthSheetDataBean = null;
			thisMonthSheetDataBean = null;
			sheetDataBeanList = null;
			cellparams = null;
			wbdb = null;
			return inDto;
		}

		byte[] writeFileData = (byte[]) resultMap.get("fileData");
		inDto.setFileData(writeFileData);
		inDto.setUploadFileName(excelFileName);
		inDto.setViewPath(NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH);

		preMonthSheetDataBean = null;
		thisMonthSheetDataBean = null;
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

		firstDayFontParam.put(Skf3030Sc001SharedService.FONT_NAME_KEY, Rp003Info.FIRST_DAY.font);
		firstDayFontParam.put(Skf3030Sc001SharedService.FONT_COLOR_KEY, IndexedColors.RED.getIndex());
		firstDayFontParam.put(Skf3030Sc001SharedService.FONT_SIZE_KEY, Short.parseShort(Rp003Info.FIRST_DAY.size));
		firstDayCellParam.put(Skf3030Sc001SharedService.FONT_PARAM_KEY, firstDayFontParam);
		firstDayCellParam.put(Skf3030Sc001SharedService.FONT_BG_COLOR_KEY, IndexedColors.YELLOW.getIndex());
		cellParams.put(Rp003Info.FIRST_DAY.col, firstDayCellParam);

		Map<String, Object> lastDayCellParam = new HashMap<>();
		Map<String, Object> lastDayFontParam = new HashMap<>();

		lastDayFontParam.put(Skf3030Sc001SharedService.FONT_NAME_KEY, Rp003Info.LAST_DAY.font);
		lastDayFontParam.put(Skf3030Sc001SharedService.FONT_COLOR_KEY, IndexedColors.RED.getIndex());
		lastDayFontParam.put(Skf3030Sc001SharedService.FONT_SIZE_KEY, Short.parseShort(Rp003Info.LAST_DAY.size));
		lastDayCellParam.put(Skf3030Sc001SharedService.FONT_PARAM_KEY, lastDayFontParam);
		lastDayCellParam.put(Skf3030Sc001SharedService.FONT_BG_COLOR_KEY, IndexedColors.YELLOW.getIndex());
		cellParams.put(Rp003Info.LAST_DAY.col, lastDayCellParam);

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
	private Map<String, Object> initCellParams(Map<String, Object> cellParams, String sheetName, String targetIdx) {

		Rp003Info[] shatakKanriInfoArry = Rp003Info.values();

		for (int i = 0; i < shatakKanriInfoArry.length; i++) {
			Map<String, Object> cellParam = new HashMap<>();
			Map<String, Object> fontParam = new HashMap<>();

			String col = shatakKanriInfoArry[i].col;
			if (!Rp003Info.FIRST_DAY.col.equals(col) && !Rp003Info.LAST_DAY.col.equals(col)) {
				fontParam.put(Skf3030Sc001SharedService.FONT_NAME_KEY, shatakKanriInfoArry[i].font);
				fontParam.put(Skf3030Sc001SharedService.FONT_COLOR_KEY, IndexedColors.BLACK.getIndex());
				fontParam.put(Skf3030Sc001SharedService.FONT_SIZE_KEY, Short.parseShort(shatakKanriInfoArry[i].size));
				cellParam.put(Skf3030Sc001SharedService.FONT_PARAM_KEY, fontParam);
				cellParams.put(sheetName + col + targetIdx, cellParam);
			}
		}

		return cellParams;
	}

	/**
	 * 対象（前月と異なるデータが存在する）の行のセルスタイルパラメータを設定する。
	 * 
	 * @param cellParams
	 *            セルスタイルパラメータ
	 * @param compareData
	 *            比較データ
	 * @param targetIdx
	 *            対象インデックス
	 * @return セルスタイルパラメータ
	 */
	private Map<String, Object> setDiffDataCellParams(Map<String, Object> cellParams,
			Skf3030Rp003PreMonthCompareDataExp compareData, String sheetName, String targetIdx) {

		boolean isDiffRow = false;

		if (compareData.isDiffYakuinSannteiKbn()) {
			Map<String, Object> cellParam = createDiffCellParam(Rp003Info.YAKUIN_TEKIYO);
			cellParams.put(sheetName + Rp003Info.YAKUIN_TEKIYO.col + targetIdx, cellParam);
			isDiffRow = true;
		}

		if (compareData.isDiffRentalMonth()) {
			Map<String, Object> cellParam = createDiffCellParam(Rp003Info.RENTAL_TOTAL);
			cellParams.put(sheetName + Rp003Info.RENTAL_TOTAL.col + targetIdx, cellParam);
			isDiffRow = true;
		}

		if (compareData.isDiffParkingRental()) {
			Map<String, Object> cellParam = createDiffCellParam(Rp003Info.PARKING_RENTAL_TOTAL);
			cellParams.put(sheetName + Rp003Info.PARKING_RENTAL_TOTAL.col + targetIdx, cellParam);
			isDiffRow = true;
		}

		if (compareData.isDiffKyoekihiPerson()) {
			Map<String, Object> cellParam = createDiffCellParam(Rp003Info.KYOEKIHI_PERSON_TOTAL);
			cellParams.put(sheetName + Rp003Info.KYOEKIHI_PERSON_TOTAL.col + targetIdx, cellParam);
			isDiffRow = true;
		}

		if (compareData.isDiffName()) {
			Map<String, Object> cellParam = createDiffCellParam(Rp003Info.SHAIN_NAME);
			cellParams.put(sheetName + Rp003Info.SHAIN_NAME.col + targetIdx, cellParam);
			isDiffRow = true;
		}

		if (compareData.isDiffShainNo()) {
			Map<String, Object> cellParam = createDiffCellParam(Rp003Info.SHAIN_NO);
			cellParams.put(sheetName + Rp003Info.SHAIN_NO.col + targetIdx, cellParam);
			isDiffRow = true;
		}

		if (compareData.isDiffBusinessAreaCd()) {
			Map<String, Object> cellParam = createDiffCellParam(Rp003Info.BUSINESS_AREA_CD);
			cellParams.put(sheetName + Rp003Info.BUSINESS_AREA_CD.col + targetIdx, cellParam);
			isDiffRow = true;
		}

		if (compareData.isDiffBusinessAreaName()) {
			Map<String, Object> cellParam = createDiffCellParam(Rp003Info.BUSINESS_AREA_NAME);
			cellParams.put(sheetName + Rp003Info.BUSINESS_AREA_NAME.col + targetIdx, cellParam);
			isDiffRow = true;
		}

		if (compareData.isDiffPreBusinessAreaCd()) {
			Map<String, Object> cellParam = createDiffCellParam(Rp003Info.PRE_BUSINESS_AREA_CD);
			cellParams.put(sheetName + Rp003Info.PRE_BUSINESS_AREA_CD.col + targetIdx, cellParam);
			isDiffRow = true;
		}

		if (compareData.isDiffPreBusinessAreaName()) {
			Map<String, Object> cellParam = createDiffCellParam(Rp003Info.PRE_BUSINESS_AREA_NAME);
			cellParams.put(sheetName + Rp003Info.PRE_BUSINESS_AREA_NAME.col + targetIdx, cellParam);
			isDiffRow = true;
		}

		if (compareData.isDiffAssignCompanyCd()) {
			Map<String, Object> cellParam = createDiffCellParam(Rp003Info.ASSING_COMPANY_NAME);
			cellParams.put(sheetName + Rp003Info.ASSING_COMPANY_NAME.col + targetIdx, cellParam);
			isDiffRow = true;
		}

		if (compareData.isDiffAssignAgencyName()) {
			Map<String, Object> cellParam = createDiffCellParam(Rp003Info.ASSING_AGENCY_NAME);
			cellParams.put(sheetName + Rp003Info.ASSING_AGENCY_NAME.col + targetIdx, cellParam);
			isDiffRow = true;
		}

		if (compareData.isDiffAssignAffiliation1()) {
			Map<String, Object> cellParam = createDiffCellParam(Rp003Info.ASSING_AFFILIATION_1);
			cellParams.put(sheetName + Rp003Info.ASSING_AFFILIATION_1.col + targetIdx, cellParam);
			isDiffRow = true;
		}

		if (compareData.isDiffAssignAffiliation2()) {
			Map<String, Object> cellParam = createDiffCellParam(Rp003Info.ASSING_AFFILIATION_2);
			cellParams.put(sheetName + Rp003Info.ASSING_AFFILIATION_2.col + targetIdx, cellParam);
			isDiffRow = true;
		}

		if (compareData.isDiffRentalTotal()) {
			Map<String, Object> cellParam = createDiffCellParam(Rp003Info.SHATAK_TOTAL);
			cellParams.put(sheetName + Rp003Info.SHATAK_TOTAL.col + targetIdx, cellParam);
			isDiffRow = true;
		}

		if (compareData.isDiffParkingRentalTotal()) {
			Map<String, Object> cellParam = createDiffCellParam(Rp003Info.PARKING_TOTAL);
			cellParams.put(sheetName + Rp003Info.PARKING_TOTAL.col + targetIdx, cellParam);
			isDiffRow = true;
		}

		if (compareData.isDiffShatakuOrCar()) {
			Map<String, Object> cellParam = createDiffCellParam(Rp003Info.SHATAK_OR_CAR);
			cellParams.put(sheetName + Rp003Info.SHATAK_OR_CAR.col + targetIdx, cellParam);
			isDiffRow = true;
		}

		if (compareData.isDiffKyoekihiPersonTotal()) {
			Map<String, Object> cellParam = createDiffCellParam(Rp003Info.KYOEKIHI);
			cellParams.put(sheetName + Rp003Info.KYOEKIHI.col + targetIdx, cellParam);
			isDiffRow = true;
		}

		if (compareData.isDiffChoshuTotal()) {
			Map<String, Object> cellParam = createDiffCellParam(Rp003Info.CHOSHU_TOTAL);
			cellParams.put(sheetName + Rp003Info.CHOSHU_TOTAL.col + targetIdx, cellParam);
			isDiffRow = true;
		}

		if (compareData.isDiffRentalAdjust()) {
			Map<String, Object> cellParam = createDiffCellParam(Rp003Info.RENTAL_ADJUST);
			cellParams.put(sheetName + Rp003Info.RENTAL_ADJUST.col + targetIdx, cellParam);
			isDiffRow = true;
		}

		if (compareData.isDiffParkingRentalAdjust()) {
			Map<String, Object> cellParam = createDiffCellParam(Rp003Info.PARKING_RENTAL_ADJUST);
			cellParams.put(sheetName + Rp003Info.PARKING_RENTAL_ADJUST.col + targetIdx, cellParam);
			isDiffRow = true;
		}

		if (compareData.isDiffKyoekihiPersonAdjust()) {
			Map<String, Object> cellParam = createDiffCellParam(Rp003Info.KYOEKIHI_PERSON_ADJUST);
			cellParams.put(sheetName + Rp003Info.KYOEKIHI_PERSON_ADJUST.col + targetIdx, cellParam);
			isDiffRow = true;
		}

		if (compareData.isDiffNyukyoDate()) {
			Map<String, Object> cellParam = createDiffCellParam(Rp003Info.NYUKYO_DATE);
			cellParams.put(sheetName + Rp003Info.NYUKYO_DATE.col + targetIdx, cellParam);
			isDiffRow = true;
		}

		if (compareData.isDiffTaikyoDate()) {
			Map<String, Object> cellParam = createDiffCellParam(Rp003Info.TAIKYO_DATE);
			cellParams.put(sheetName + Rp003Info.TAIKYO_DATE.col + targetIdx, cellParam);
			isDiffRow = true;
		}

		if (compareData.isDiffParkingStartDate()) {
			Map<String, Object> cellParam = createDiffCellParam(Rp003Info.PARKING_STARTDATE);
			cellParams.put(sheetName + Rp003Info.PARKING_STARTDATE.col + targetIdx, cellParam);
			isDiffRow = true;
		}

		if (compareData.isDiffParkingEndDate()) {
			Map<String, Object> cellParam = createDiffCellParam(Rp003Info.PARKING_ENDDATE);
			cellParams.put(sheetName + Rp003Info.PARKING_ENDDATE.col + targetIdx, cellParam);
			isDiffRow = true;
		}

		if (isDiffRow) {
			Map<String, Object> cellParam = new HashMap<>();
			Map<String, Object> fontParam = new HashMap<>();
			cellParam.put(Skf3030Sc001SharedService.FONT_PARAM_KEY, fontParam);
			cellParam.put(Skf3030Sc001SharedService.FONT_BG_COLOR_KEY, IndexedColors.ROSE.getIndex());
			cellParams.put(sheetName + SHOW_DIFF_DATA_COL + targetIdx, cellParam);
		}

		return cellParams;
	}

	/**
	 * 前月データと異なるセルのスタイルパラメータを作成する。
	 * 
	 * @param tagetCell
	 *            対象のセル
	 * @return セルのスタイルパラメータ
	 */
	private Map<String, Object> createDiffCellParam(Rp003Info tagetCell) {

		Map<String, Object> cellParam = new HashMap<>();
		Map<String, Object> fontParam = new HashMap<>();
		fontParam.put(Skf3030Sc001SharedService.FONT_NAME_KEY, tagetCell.font);
		fontParam.put(Skf3030Sc001SharedService.FONT_COLOR_KEY, IndexedColors.BLACK.getIndex());
		fontParam.put(Skf3030Sc001SharedService.FONT_SIZE_KEY, Short.parseShort(tagetCell.size));
		cellParam.put(Skf3030Sc001SharedService.FONT_PARAM_KEY, fontParam);
		cellParam.put(Skf3030Sc001SharedService.FONT_BG_COLOR_KEY, IndexedColors.ROSE.getIndex());

		return cellParam;
	}

}
