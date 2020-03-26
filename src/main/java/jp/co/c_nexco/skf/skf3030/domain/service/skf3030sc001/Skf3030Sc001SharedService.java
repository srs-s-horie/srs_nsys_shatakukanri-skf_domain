/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3030.domain.service.skf3030sc001;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.poi_v3_8.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Rp002.Skf3030Rp002GetShatakuDaichoInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Rp002.Skf3030Rp002GetShatakuDaichoInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Rp003.Skf3030Rp003GetShatakuDaichoDiffDataInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Rp003.Skf3030Rp003GetShatakuDaichoInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Rp003.Skf3030Rp003GetShatakuDaichoInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc001.Skf3030Sc001GetGetsujishoriStatusExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc001.Skf3030Sc001GetNengetsuListExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc001.Skf3030Sc001GetShatakuKanriDaichoInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc001.Skf3030Sc001GetShatakuKanriDaichoInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Rp002.Skf3030Rp002GetShatakuDaichoInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Rp003.Skf3030Rp003GetShatakuDaichoDiffDataInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Rp003.Skf3030Rp003GetShatakuDaichoInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc001.Skf3030Sc001GetGetsujishoriStatusExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc001.Skf3030Sc001GetNengetsuListExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc001.Skf3030Sc001GetShatakuKanriDaichoEmptyRoomCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc001.Skf3030Sc001GetShatakuKanriDaichoIdCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc001.Skf3030Sc001GetShatakuKanriDaichoInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc001.Skf3030Sc001GetShatakuKanriDaichoKariShainNoCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc001.Skf3030Sc001GetShatakuKanriDaichoTaikyoYoteiCountExpRepository;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.skf3030.domain.dto.skf3030Sc001common.Skf3030Sc001CommonDto;
import jp.co.c_nexco.skf.skf3030.domain.service.common.Skf303010CommonSharedService;

/**
 * Skf3030Sc001SharedService 社宅管理台帳共通処理Service
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf3030Sc001SharedService {

	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	@Autowired
	private SkfDropDownUtils skfDropDownUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private Skf3030Sc001GetNengetsuListExpRepository skf3030Sc001GetNengetsuListExpRepository;
	@Autowired
	private Skf3030Sc001GetGetsujishoriStatusExpRepository skf3030Sc001GetGetsujishoriStatusExpRepository;
	@Autowired
	private Skf3030Sc001GetShatakuKanriDaichoInfoExpRepository skf3030Sc001GetShatakuKanriDaichoInfoExpRepository;
	@Autowired
	private Skf3030Sc001GetShatakuKanriDaichoIdCountExpRepository skf3030Sc001GetShatakuKanriDaichoIdCountExpRepository;
	@Autowired
	private Skf3030Sc001GetShatakuKanriDaichoTaikyoYoteiCountExpRepository skf3030Sc001GetShatakuKanriDaichoTaikyoYoteiCountExpRepository;
	@Autowired
	private Skf3030Sc001GetShatakuKanriDaichoKariShainNoCountExpRepository skf3030Sc001GetShatakuKanriDaichoKariShainNoCountExpRepository;
	@Autowired
	private Skf3030Sc001GetShatakuKanriDaichoEmptyRoomCountExpRepository skf3030Sc001GetShatakuKanriDaichoEmptyRoomCountExpRepository;
	@Autowired
	private Skf3030Rp002GetShatakuDaichoInfoExpRepository skf3030Rp002GetShatakuDaichoInfoExpRepository;
	@Autowired
	private Skf3030Rp003GetShatakuDaichoInfoExpRepository skf3030Rp003GetShatakuDaichoInfoExpRepository;
	@Autowired
	private Skf3030Rp003GetShatakuDaichoDiffDataInfoExpRepository skf3030Rp003GetShatakuDaichoDiffDataInfoExpRepository;

	private static final String HYPHEN = "―";

	public static final String BILLING_ACT_KBN_0 = "0";
	public static final String BILLING_ACT_KBN_1 = "1";
	public static final String BILLING_ACT_KBN_2 = "2";
	public static final String BILLING_ACT_KBN_0_LABEL = "未実行";
	public static final String BILLING_ACT_KBN_1_LABEL = "実行済";
	public static final String BILLING_ACT_KBN_2_LABEL = "解除中";

	public static final String POSITIVE_LINKDATA_CREATE_MIJIKKOU = "0";
	public static final String POSITIVE_LINKDATA_CREATE_JIKKOUZUMI = "1";
	public static final String POSITIVE_LINKDATA_CREATE_KAIJO = "2";
	public static final String POSITIVE_LINKDATA_COMMIT_MIJIKKOU = "0";
	public static final String POSITIVE_LINKDATA_COMMIT_JIKKOUZUMI = "1";

	public static final String POSITIVE_LINKDATA_KAIJO = "解除中";
	public static final String POSITIVE_LINKDATA_JIKKOUZUMI = "実行済";
	public static final String POSITIVE_LINKDATA_KAKUTEIZUMI = "確定済";
	public static final String POSITIVE_LINKDATA_MIJIKKOU = "未実行";

	public static final String SHIME_SHORI_TXT_KEY = "txtShimeShoriKey";
	public static final String POSITIVE_RENKEI_TXT_KEY = "txtPositiveRenkeiKey";

	public static final String CD_EXTERNAL_AGENCY = "ZZZZ";
	public static final String DISABLED = "true";
	public static final String ABLED = "false";
	public static final String CD_PREF_OTHER = "48";
	public static final String MUTUAL_USE_ARI = "有";

	public static final String STATUS_CD_OK = "0";
	public static final String STATUS_CD_NO_DATA = "1";
	public static final String STATUS_CD_DATA_ERR = "2";
	public static final String STATUS_CD_OUTPUT_ERR = "3";
	public static final String STATUS_CD_PARAM_ERR = "4";

	public static final String KAISHAKAN_SOKIN_NASI = "×";
	public static final String KAISHAKAN_SOKIN_SHIHARAI = "○";
	public static final String KAISHAKAN_SOKIN_SEIKYU = "◎";

	public static final String FONT_NAME_KEY = "fontName";
	public static final String FONT_COLOR_KEY = "fontColor";
	public static final String FONT_SIZE_KEY = "fontSize";
	public static final String FONT_PARAM_KEY = "font";
	public static final String FONT_BG_COLOR_KEY = "bgcolor";

	private static final int DATE_LEN = 8;
	private static final String SHAIN_NO_CHG_AVAILABLE = "1";

	private static final String SHATAK_TEIJI_STS_LABEL_SAKUSEI_CHU = "作成中";
	private static final String SHATAK_TEIJI_STS_LABEL_SAKUSEI_ZUMI = "作成済";
	private static final String SHATAK_TEIJI_STS_LABEL_TEIJI_CHU = "提示中";
	private static final String SHATAK_TEIJI_STS_LABEL_DOI_ZUMI = "同意済";
	private static final String SHATAK_TEIJI_STS_LABEL_SHONIN = "承認";

	private static final String SHATAK_BIHIN_TEIJI_STS_LABEL_MI_SINSEI = "未申請";
	private static final String SHATAK_BIHIN_TEIJI_STS_LABEL_SAKUSEI_CHU = "作成中";
	private static final String SHATAK_BIHIN_TEIJI_STS_LABEL_SAKUSEI_ZUMI = "作成済";
	private static final String SHATAK_BIHIN_TEIJI_STS_LABEL_TEIJI_CHU = "提示中";
	private static final String SHATAK_BIHIN_TEIJI_STS_LABEL_DOI_ZUMI = "同意済";
	private static final String SHATAK_BIHIN_TEIJI_STS_LABEL_HANNYU_MACHI = "搬入待ち";
	private static final String SHATAK_BIHIN_TEIJI_STS_LABEL_HANNYU_ZUMI = "搬入済";
	private static final String SHATAK_BIHIN_TEIJI_STS_LABEL_HANSHUTSU_MACHI = "搬出待ち";
	private static final String SHATAK_BIHIN_TEIJI_STS_LABEL_HANSHUTSU_ZUMI = "搬出済";
	private static final String SHATAK_BIHIN_TEIJI_STS_LABEL_SHONIN = "承認";

	/**
	 * 「年月」データリスト取得
	 * 
	 * @return 「年月」データリスト
	 */
	public List<Skf3030Sc001GetNengetsuListExp> getNengetsuList() {

		List<Skf3030Sc001GetNengetsuListExp> rtnData = skf3030Sc001GetNengetsuListExpRepository.getNengetsuList();

		return rtnData;
	}

	/**
	 * 月次処理ステータスデータ取得
	 * 
	 * @param nengetsu
	 *            対象年月
	 * @return 月次処理ステータスデータ
	 */
	public List<Skf3030Sc001GetGetsujishoriStatusExp> getGetsujishoriStatus(String nengetsu) {

		List<Skf3030Sc001GetGetsujishoriStatusExp> rtnData = skf3030Sc001GetGetsujishoriStatusExpRepository
				.getGetsujishoriStatus(nengetsu);

		return rtnData;
	}

	/**
	 * システムデータの年月を取得する。
	 * 
	 * @return システムデータ年月
	 */
	public String getSystemProcessNenGetsu() {

		String systemProcessNenGetsu = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu();

		return systemProcessNenGetsu;
	}

	/**
	 * 検索件数取得（社宅管理台帳IDの検索）
	 * 
	 * @param inParam
	 *            取得用パラメータ
	 * @return 検索件数
	 */
	public Long getShatakuKanriDaichoIdCount(Skf3030Sc001GetShatakuKanriDaichoInfoExpParameter inParam) {

		Long rtnCnt = skf3030Sc001GetShatakuKanriDaichoIdCountExpRepository.getShatakuKanriDaichoIdCount(inParam);

		return rtnCnt;
	}

	/**
	 * 検索件数取得（退居予定の社宅管理台帳ID検索）
	 * 
	 * @param inParam
	 *            取得用パラメータ
	 * @return 検索件数
	 */
	public Long getShatakuKanriDaichoTaikyoYoteiCount(Skf3030Sc001GetShatakuKanriDaichoInfoExpParameter inParam) {

		Long rtnCnt = skf3030Sc001GetShatakuKanriDaichoTaikyoYoteiCountExpRepository
				.getShatakuKanriDaichoTaikyoYoteiCount(inParam);

		return rtnCnt;
	}

	/**
	 * 検索件数取得（仮社員番号の社宅管理台帳IDの検索）
	 * 
	 * @param inParam
	 *            取得用パラメータ
	 * @return 検索件数
	 */
	public Long getShatakuKanriDaichoKariShainNoCount(Skf3030Sc001GetShatakuKanriDaichoInfoExpParameter inParam) {

		Long rtnCnt = skf3030Sc001GetShatakuKanriDaichoKariShainNoCountExpRepository
				.getShatakuKanriDaichoKariShainNoCount(inParam);

		return rtnCnt;
	}

	/**
	 * 検索件数取得（空き状態の部屋管理番号の検索）
	 * 
	 * @param inParam
	 *            取得用パラメータ
	 * @return 検索件数
	 */
	public Long getShatakuKanriDaichoEmptyRoomCount(Skf3030Sc001GetShatakuKanriDaichoInfoExpParameter inParam) {

		Long rtnCnt = skf3030Sc001GetShatakuKanriDaichoEmptyRoomCountExpRepository
				.getShatakuKanriDaichoEmptyRoomCount(inParam);

		return rtnCnt;
	}

	/**
	 * 検索データ取得用のEntityを作成する。
	 * 
	 * @param inDto
	 *            Skf3030Sc001SearchDto
	 * @param isInitSearch
	 *            初回検索判定
	 * @return 検索データ取得用のEntity
	 */
	public Skf3030Sc001GetShatakuKanriDaichoInfoExpParameter createSearchParam(Skf3030Sc001CommonDto inDto,
			Map<String, Object> sessionData, boolean isInitSearch) {

		Skf3030Sc001GetShatakuKanriDaichoInfoExpParameter rtnParam = new Skf3030Sc001GetShatakuKanriDaichoInfoExpParameter();

		rtnParam.setKanriKaisha(cnvEmptyStrToNull(inDto.getHdnCompanyAgencySelect()));
		rtnParam.setKanriKikan(cnvEmptyStrToNull(inDto.getHdnAgencySelect()));
		rtnParam.setShainNo(cnvEmptyStrToNull(inDto.getTxtShainNo()));
		rtnParam.setShainName(cnvEmptyStrToNull(inDto.getTxtShainName()));
		rtnParam.setShatakuName(cnvEmptyStrToNull(inDto.getTxtShatakuName()));
		rtnParam.setShatakuKbn(cnvEmptyStrToNull(inDto.getHdnShatakuKbnSelect()));
		rtnParam.setSogoriyo(cnvEmptyStrToNull(inDto.getHdnMutualuseSelect()));
		rtnParam.setNengetsu(cnvEmptyStrToNull(inDto.getHdnYearSelect() + inDto.getHdnMonthSelect()));
		rtnParam.setShimeShori(getBillingActKbn(inDto.getLabelShimeShori()));
		rtnParam.setPositiveRenkei(getPositiveRenkeiKbn(inDto.getLabelPositiveRenkei()));

		if (isInitSearch) {
			rtnParam.setInitSeachFlg("1");

			String kaishakanSokin = (String) sessionData
					.get(Skf303010CommonSharedService.NYUTAIKYO_INFO_KAISHAKAN_SOKIN_KEY);
			rtnParam.setKaishakanSokin(cnvEmptyStrToNull(kaishakanSokin));

			String gensekiKaisha = (String) sessionData
					.get(Skf303010CommonSharedService.NYUTAIKYO_INFO_GENSEKI_KAISHA_KEY);
			rtnParam.setGensekiKaisha(cnvEmptyStrToNull(gensekiKaisha));

			String kyuyoShikyuKaisha = (String) sessionData
					.get(Skf303010CommonSharedService.NYUTAIKYO_INFO_SIKYU_KAISHA_KEY);
			rtnParam.setKyuyoShikyuKaisha(cnvEmptyStrToNull(kyuyoShikyuKaisha));

			String kyojushaKbn = (String) sessionData.get(Skf303010CommonSharedService.NYUTAIKYO_INFO_KYOJUSHA_KBN_KEY);
			rtnParam.setKyojushaKbn(cnvEmptyStrToNull(kyojushaKbn));

			String akiHeya = (String) sessionData.get(Skf303010CommonSharedService.NYUTAIKYO_INFO_AKI_HEYA_KEY);
			rtnParam.setAkiHeya(cnvEmptyStrToNull(akiHeya));

			String akiChushajo = (String) sessionData.get(Skf303010CommonSharedService.NYUTAIKYO_INFO_PARKING_KEY);
			rtnParam.setAkiChushajo(cnvEmptyStrToNull(akiChushajo));

			String honraiYoto = (String) sessionData.get(Skf303010CommonSharedService.NYUTAIKYO_INFO_HONRAI_YOTO_KEY);
			rtnParam.setHonraiYoto(cnvEmptyStrToNull(honraiYoto));

			String honraiKikaku = (String) sessionData
					.get(Skf303010CommonSharedService.NYUTAIKYO_INFO_HONRAI_KIKAKU_KEY);
			rtnParam.setHonraiKikaku(cnvEmptyStrToNull(honraiKikaku));

			String yakuin = (String) sessionData.get(Skf303010CommonSharedService.NYUTAIKYO_INFO_YAKUIN_KEY);
			rtnParam.setYakuin(cnvEmptyStrToNull(yakuin));
			
			String shukkosha = (String) sessionData.get(Skf303010CommonSharedService.SEARCH_INFO_SHUKKOSHA_KEY);
			rtnParam.setShukkosha(cnvEmptyStrToNull(shukkosha));

			String biko = (String) sessionData.get(Skf303010CommonSharedService.NYUTAIKYO_INFO_BIKO_KEY);
			rtnParam.setBiko(cnvEmptyStrToNull(biko));

		} else {
			rtnParam.setInitSeachFlg("0");
			rtnParam.setKaishakanSokin("");
			rtnParam.setGensekiKaisha("");
			rtnParam.setKyuyoShikyuKaisha("");
			rtnParam.setKyojushaKbn("");
			rtnParam.setAkiHeya("");
			rtnParam.setAkiChushajo("");
			rtnParam.setHonraiYoto("");
			rtnParam.setHonraiKikaku("");
			rtnParam.setYakuin("");
			rtnParam.setShukkosha("");
			rtnParam.setBiko("");
		}

		rtnParam.setSystemYearMonth(getSystemProcessNenGetsu());

		return rtnParam;
	}

	/**
	 * 社宅管理台帳の検索件数を取得する。
	 * 
	 * @param inParam
	 *            検索用パラメータ
	 * @return 社宅管理台帳の検索件数
	 */
	public long getShatakuKanriDaichoCount(Skf3030Sc001GetShatakuKanriDaichoInfoExpParameter inParam) {

		long rtnCnt = 0;

		rtnCnt += getShatakuKanriDaichoIdCount(inParam);

		rtnCnt += getShatakuKanriDaichoTaikyoYoteiCount(inParam);

		rtnCnt += getShatakuKanriDaichoKariShainNoCount(inParam);

		rtnCnt += getShatakuKanriDaichoEmptyRoomCount(inParam);

		return rtnCnt;
	}

	/**
	 * 社宅管理台帳の検索結果情報を取得する。
	 * 
	 * @param inParam
	 *            社宅管理台帳検索パラメータ
	 * @return 社宅管理台帳の検索結果情報
	 */
	public List<Skf3030Sc001GetShatakuKanriDaichoInfoExp> getShatakuKanriDaichoInfoData(
			Skf3030Sc001GetShatakuKanriDaichoInfoExpParameter inParam) {

		List<Skf3030Sc001GetShatakuKanriDaichoInfoExp> rtnData = skf3030Sc001GetShatakuKanriDaichoInfoExpRepository
				.getShatakuKanriDaichoInfo(inParam);

		return rtnData;
	}

	/**
	 * 処理対象年月の情報を取得する。
	 *
	 * @param nengetsu
	 *            選択されている年月
	 * @return 処理対象年月の情報map
	 */
	public Map<String, String> getGetsujiShoriJoukyouShoukai(String nengetsu) {

		Map<String, String> rtnMap = new HashMap<String, String>();
		List<Skf3030Sc001GetGetsujishoriStatusExp> monthDataList = getGetsujishoriStatus(nengetsu);

		if (monthDataList.size() > 0) {
			String shimeShoriTxt = getBillingActKbnTxt(monthDataList.get(0).getBillingActKbn());
			rtnMap.put(SHIME_SHORI_TXT_KEY, shimeShoriTxt);

			String createKbn = monthDataList.get(0).getLinkdataCreateKbn();
			String commitKbn = monthDataList.get(0).getLinkdataCommitKbn();
			String positiveRenkeiTxt = getPositiveRenkeiValue(createKbn, commitKbn);
			rtnMap.put(POSITIVE_RENKEI_TXT_KEY, positiveRenkeiTxt);

		} else {
			rtnMap.put(SHIME_SHORI_TXT_KEY, "");
			rtnMap.put(POSITIVE_RENKEI_TXT_KEY, "");
		}

		return rtnMap;
	}

	/**
	 * 締め処理区分を取得する。
	 * 
	 * @param billingKbnLabel
	 *            画面上の締め処理
	 * @return 締め処理区分
	 */
	public String getBillingActKbn(String billingKbnLabel) {

		String rtnVal = "";

		if (NfwStringUtils.isEmpty(billingKbnLabel)) {
			return rtnVal;
		}

		switch (billingKbnLabel) {
		case Skf3030Sc001SharedService.BILLING_ACT_KBN_0_LABEL:
			rtnVal = Skf3030Sc001SharedService.BILLING_ACT_KBN_0;
			break;
		case Skf3030Sc001SharedService.BILLING_ACT_KBN_1_LABEL:
			rtnVal = Skf3030Sc001SharedService.BILLING_ACT_KBN_1;
			break;
		case Skf3030Sc001SharedService.BILLING_ACT_KBN_2_LABEL:
			rtnVal = Skf3030Sc001SharedService.BILLING_ACT_KBN_2;
			break;
		default:
			break;
		}

		return rtnVal;
	}

	/**
	 * POSITIVE連携作成実行区分を取得する。
	 * 
	 * @param positiveRenakeiLabel
	 *            画面上のPOSITIVE連携
	 * @return POSITIVE連携作成実行区分
	 */
	public String getPositiveRenkeiKbn(String positiveRenakeiLabel) {

		String rtnVal = "";

		if (NfwStringUtils.isEmpty(positiveRenakeiLabel)) {
			return rtnVal;
		}

		switch (positiveRenakeiLabel) {
		case Skf3030Sc001SharedService.POSITIVE_LINKDATA_MIJIKKOU:
			rtnVal = Skf3030Sc001SharedService.POSITIVE_LINKDATA_CREATE_MIJIKKOU;
			break;
		case Skf3030Sc001SharedService.POSITIVE_LINKDATA_JIKKOUZUMI:
			rtnVal = Skf3030Sc001SharedService.POSITIVE_LINKDATA_CREATE_JIKKOUZUMI;
			break;
		case Skf3030Sc001SharedService.POSITIVE_LINKDATA_KAIJO:
			rtnVal = Skf3030Sc001SharedService.POSITIVE_LINKDATA_CREATE_KAIJO;
			break;
		case Skf3030Sc001SharedService.POSITIVE_LINKDATA_KAKUTEIZUMI:
			rtnVal = Skf3030Sc001SharedService.POSITIVE_LINKDATA_CREATE_JIKKOUZUMI;
			break;
		default:
			break;
		}

		return rtnVal;
	}

	/**
	 * 締め処理テキスト設定値を取得する。
	 * 
	 * @param billingKbn
	 *            締め処理区分
	 * @return テキスト設定値
	 */
	public String getBillingActKbnTxt(String billingKbn) {

		String rtnVal = "";

		if (NfwStringUtils.isEmpty(billingKbn)) {
			return rtnVal;
		}

		switch (billingKbn) {
		case Skf3030Sc001SharedService.BILLING_ACT_KBN_0:
			rtnVal = Skf3030Sc001SharedService.BILLING_ACT_KBN_0_LABEL;
			break;
		case Skf3030Sc001SharedService.BILLING_ACT_KBN_1:
			rtnVal = Skf3030Sc001SharedService.BILLING_ACT_KBN_1_LABEL;
			break;
		case Skf3030Sc001SharedService.BILLING_ACT_KBN_2:
			rtnVal = Skf3030Sc001SharedService.BILLING_ACT_KBN_2_LABEL;
			break;
		default:
			break;
		}

		return rtnVal;
	}

	/**
	 * POSITIVE連携テキスト設定値を取得する。
	 * 
	 * @param createKbn
	 *            POSITIVE連携作成実行区分
	 * @param commitKbn
	 *            POSITIVE連携確定実行区分
	 * @return テキスト設定値
	 */
	public String getPositiveRenkeiValue(String createKbn, String commitKbn) {

		String rtnVal = "";

		if (NfwStringUtils.isEmpty(createKbn) || NfwStringUtils.isEmpty(commitKbn)) {
			return rtnVal;
		}

		if (Skf3030Sc001SharedService.POSITIVE_LINKDATA_CREATE_KAIJO.equals(createKbn)
				&& Skf3030Sc001SharedService.POSITIVE_LINKDATA_COMMIT_MIJIKKOU.equals(commitKbn)) {
			rtnVal = Skf3030Sc001SharedService.POSITIVE_LINKDATA_KAIJO;

		} else if (Skf3030Sc001SharedService.POSITIVE_LINKDATA_CREATE_JIKKOUZUMI.equals(createKbn)
				&& Skf3030Sc001SharedService.POSITIVE_LINKDATA_COMMIT_MIJIKKOU.equals(commitKbn)) {
			rtnVal = Skf3030Sc001SharedService.POSITIVE_LINKDATA_JIKKOUZUMI;

		} else if (Skf3030Sc001SharedService.POSITIVE_LINKDATA_CREATE_JIKKOUZUMI.equals(createKbn)
				&& Skf3030Sc001SharedService.POSITIVE_LINKDATA_COMMIT_JIKKOUZUMI.equals(commitKbn)) {
			rtnVal = Skf3030Sc001SharedService.POSITIVE_LINKDATA_KAKUTEIZUMI;

		} else {
			rtnVal = Skf3030Sc001SharedService.POSITIVE_LINKDATA_MIJIKKOU;
		}

		return rtnVal;
	}

	/**
	 * 「検索結果一覧」リストを作成する。
	 * 
	 * @param inParam
	 *            検索用パラメータ
	 * @return 「検索結果一覧」リスト
	 */
	public List<Map<String, Object>> createSearchList(Skf3030Sc001GetShatakuKanriDaichoInfoExpParameter inParam,
			Skf3030Sc001CommonDto inDto) {

		List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();
		List<Skf3030Sc001GetShatakuKanriDaichoInfoExp> shatakuDaichoInfoList = getShatakuKanriDaichoInfoData(inParam);

		if (shatakuDaichoInfoList.size() == 0) {
			return rtnList;
		}

		String nengetsu = inDto.getHdnYearSelect() + inDto.getHdnMonthSelect();
		String sysNengetsu = getSystemProcessNenGetsu();
		List<Map<String, Object>> shatakKbnList = inDto.getShatakuKbnDropDownList();
		List<Map<String, Object>> mutualuseList = inDto.getMutualuseDropDownList();

		for (Skf3030Sc001GetShatakuKanriDaichoInfoExp shatakuDaichoInfo : shatakuDaichoInfoList) {
			Map<String, Object> targetMap = new HashMap<String, Object>();

			String shatakKbn = getTargetDropDownLabel(shatakuDaichoInfo.getShatakuKbn(), shatakKbnList);
			targetMap.put(Skf303010CommonSharedService.SEARCH_COL_SHATAK_KBN_KEY, shatakKbn);

			String entityTaikyoDate = shatakuDaichoInfo.getTaikyoDate();
			String entityTeijiSts = shatakuDaichoInfo.getShatakuTeijiStatus();
			String entityShainNoChgFlg = shatakuDaichoInfo.getShainNoChangeFlg();
			String shainNo = shatakuDaichoInfo.getShainNo();

			if (NfwStringUtils.isEmpty(entityTaikyoDate)
					&& CodeConstant.PRESENTATION_SITUATION_SHONIN.equals(entityTeijiSts)
					&& SHAIN_NO_CHG_AVAILABLE.equals(entityShainNoChgFlg)) {
				shainNo = shainNo + CodeConstant.ASTERISK;
			}
			targetMap.put(Skf303010CommonSharedService.SEARCH_COL_SHAIN_NO_KEY, shainNo);

			String teijiSts = "";
			if (NfwStringUtils.isEmpty(entityTeijiSts)) {
				teijiSts = HYPHEN;

			} else {
				if (Integer.parseInt(nengetsu) < Integer.parseInt(sysNengetsu)) {
					teijiSts = HYPHEN;
				} else {
					teijiSts = getTeijiStsLabel(entityTeijiSts);
				}
			}
			targetMap.put(Skf303010CommonSharedService.SEARCH_COL_SHATAK_TEIJI_KEY, teijiSts);

			String entityBihinTeijiSts = shatakuDaichoInfo.getBihinTeijiStatus();
			String bihinTeijiSts = "";
			if (NfwStringUtils.isEmpty(entityBihinTeijiSts)) {
				bihinTeijiSts = HYPHEN;

			} else {
				if (Integer.parseInt(nengetsu) < Integer.parseInt(sysNengetsu)) {
					bihinTeijiSts = HYPHEN;
				} else {
					bihinTeijiSts = getBihinTeijiStsLabel(entityBihinTeijiSts);
				}
			}
			targetMap.put(Skf303010CommonSharedService.SEARCH_COL_BIHIN_TEIJI_KEY, bihinTeijiSts);

			Integer entitySiyoryo = shatakuDaichoInfo.getRentalTotal();
			String siyoryo = "";
			if (entitySiyoryo != null) {
				siyoryo = String.format("%,d", entitySiyoryo) + SkfCommonConstant.FORMAT_EN;
			} else {
				siyoryo = "0" + SkfCommonConstant.FORMAT_EN;
			}
			targetMap.put(Skf303010CommonSharedService.SEARCH_COL_SIYORYO_KEY, siyoryo);

			Integer entityKyoekihi = shatakuDaichoInfo.getKyoekihiPersonTotal();
			String kyoekihi = "";
			if (entityKyoekihi != null) {
				kyoekihi = String.format("%,d", entityKyoekihi) + SkfCommonConstant.FORMAT_EN;
			} else {
				kyoekihi = "0" + SkfCommonConstant.FORMAT_EN;
			}
			targetMap.put(Skf303010CommonSharedService.SEARCH_COL_KYOEKIHI_KEY, kyoekihi);

			String parkingSiyoryo = "";
			Integer entityParkingSiyoryo = shatakuDaichoInfo.getParkingRentalTotal();
			if (entityParkingSiyoryo != null) {
				parkingSiyoryo = String.format("%,d", entityParkingSiyoryo) + SkfCommonConstant.FORMAT_EN;
			} else {
				parkingSiyoryo = "0" + SkfCommonConstant.FORMAT_EN;
			}
			targetMap.put(Skf303010CommonSharedService.SEARCH_COL_PARKING_KEY, parkingSiyoryo);

			String entityNyukyoDate = shatakuDaichoInfo.getNyukyoDate();
			String nyukyoDate = "";
			if (!NfwStringUtils.isEmpty(entityNyukyoDate) && entityNyukyoDate.length() == DATE_LEN) {
				nyukyoDate = entityNyukyoDate.substring(0, 4) + "/" + entityNyukyoDate.substring(4, 6) + "/"
						+ entityNyukyoDate.substring(6);
			}
			targetMap.put(Skf303010CommonSharedService.SEARCH_COL_NYUKYO_DATE_KEY, nyukyoDate);

			String taikyoDate = "";
			if (!NfwStringUtils.isEmpty(entityTaikyoDate) && entityTaikyoDate.length() == DATE_LEN) {
				taikyoDate = entityTaikyoDate.substring(0, 4) + "/" + entityTaikyoDate.substring(4, 6) + "/"
						+ entityTaikyoDate.substring(6);
			}
			targetMap.put(Skf303010CommonSharedService.SEARCH_COL_TAIKYO_DATE_KEY, taikyoDate);

			String sogoriyo = getTargetDropDownLabel(shatakuDaichoInfo.getMutualUseKbn(), mutualuseList);
			targetMap.put(Skf303010CommonSharedService.SEARCH_COL_SOGORIYO_KEY, sogoriyo);

			targetMap.put(Skf303010CommonSharedService.SEARCH_COL_DETAIL_KEY, "");
			targetMap.put(Skf303010CommonSharedService.SEARCH_COL_KANRI_COMPANY_KEY,
					shatakuDaichoInfo.getCompanyName());
			targetMap.put(Skf303010CommonSharedService.SEARCH_COL_AGENCY_KEY, shatakuDaichoInfo.getAgencyName());
			targetMap.put(Skf303010CommonSharedService.SEARCH_COL_SHATAK_NAME_KEY, shatakuDaichoInfo.getShatakuName());
			targetMap.put(Skf303010CommonSharedService.SEARCH_COL_ROOM_NO_KEY, shatakuDaichoInfo.getRoomNo());
			targetMap.put(Skf303010CommonSharedService.SEARCH_COL_SHAIN_NAME_KEY, shatakuDaichoInfo.getNameKk());
			targetMap.put(Skf303010CommonSharedService.SEARCH_COL_KANRI_JIGYO_KEY,
					shatakuDaichoInfo.getBusinessAreaName());
			targetMap.put(Skf303010CommonSharedService.SEARCH_COL_SHATAK_KANRI_NO_KEY,
					shatakuDaichoInfo.getShatakuKanriNo());
			targetMap.put(Skf303010CommonSharedService.SEARCH_COL_ROOM_KANRI_NO_KEY,
					shatakuDaichoInfo.getShatakuRoomKanriNo());
			targetMap.put(Skf303010CommonSharedService.SEARCH_COL_DAICHO_KANRI_ID_KEY,
					shatakuDaichoInfo.getShatakuKanriId());
			targetMap.put(Skf303010CommonSharedService.SEARCH_COL_KANRI_CD_KEY, shatakuDaichoInfo.getManegeCompanyCd());

			rtnList.add(targetMap);
		}

		return rtnList;
	}

	/**
	 * ドロップダウンリストから対象のラベルを取得する。
	 * 
	 * @param val
	 *            指定の値
	 * @param dropDownList
	 *            対象のドロップダウンリスト
	 * @return 対象のラベル
	 */
	private String getTargetDropDownLabel(String val, List<Map<String, Object>> dropDownList) {

		String rtn = "";

		for (int i = 0; i < dropDownList.size(); i++) {
			Map<String, Object> targetMap = dropDownList.get(i);

			if (Objects.equals(val, targetMap.get("value"))) {
				rtn = (String) targetMap.get("label");
				break;
			}
		}

		return rtn;
	}

	/**
	 * 社宅提示状態に対応するラベルを取得する。
	 * 
	 * @param sts
	 *            状態
	 * @return ラベル
	 */
	private String getTeijiStsLabel(String sts) {

		String rtn = "";

		switch (sts) {
		case CodeConstant.PRESENTATION_SITUATION_SAKUSEI_CHU:
			rtn = SHATAK_TEIJI_STS_LABEL_SAKUSEI_CHU;
			break;
		case CodeConstant.PRESENTATION_SITUATION_SAKUSEI_SUMI:
			rtn = SHATAK_TEIJI_STS_LABEL_SAKUSEI_ZUMI;
			break;
		case CodeConstant.PRESENTATION_SITUATION_TEIJI_CHU:
			rtn = SHATAK_TEIJI_STS_LABEL_TEIJI_CHU;
			break;
		case CodeConstant.PRESENTATION_SITUATION_DOI_SUMI:
			rtn = SHATAK_TEIJI_STS_LABEL_DOI_ZUMI;
			break;
		case CodeConstant.PRESENTATION_SITUATION_SHONIN:
			rtn = SHATAK_TEIJI_STS_LABEL_SHONIN;
			break;
		}

		return rtn;
	}

	/**
	 * 社宅備品提示状態に対応するラベルを取得する。
	 * 
	 * @param sts
	 *            状態
	 * @return ラベル
	 */
	private String getBihinTeijiStsLabel(String sts) {

		String rtn = "";

		switch (sts) {
		case CodeConstant.BIHIN_STATUS_MI_SAKUSEI:
			rtn = SHATAK_BIHIN_TEIJI_STS_LABEL_MI_SINSEI;
			break;
		case CodeConstant.BIHIN_STATUS_SAKUSEI_CHU:
			rtn = SHATAK_BIHIN_TEIJI_STS_LABEL_SAKUSEI_CHU;
			break;
		case CodeConstant.BIHIN_STATUS_SAKUSEI_SUMI:
			rtn = SHATAK_BIHIN_TEIJI_STS_LABEL_SAKUSEI_ZUMI;
			break;
		case CodeConstant.BIHIN_STATUS_TEIJI_CHU:
			rtn = SHATAK_BIHIN_TEIJI_STS_LABEL_TEIJI_CHU;
			break;
		case CodeConstant.BIHIN_STATUS_DOI_SUMI:
			rtn = SHATAK_BIHIN_TEIJI_STS_LABEL_DOI_ZUMI;
			break;
		case CodeConstant.BIHIN_STATUS_HANNYU_MACHI:
			rtn = SHATAK_BIHIN_TEIJI_STS_LABEL_HANNYU_MACHI;
			break;
		case CodeConstant.BIHIN_STATUS_HANNYU_SUMI:
			rtn = SHATAK_BIHIN_TEIJI_STS_LABEL_HANNYU_ZUMI;
			break;
		case CodeConstant.BIHIN_STATUS_HANSHUTSU_MACHI:
			rtn = SHATAK_BIHIN_TEIJI_STS_LABEL_HANSHUTSU_MACHI;
			break;
		case CodeConstant.BIHIN_STATUS_HANSHUTSU_SUMI:
			rtn = SHATAK_BIHIN_TEIJI_STS_LABEL_HANSHUTSU_ZUMI;
			break;
		case CodeConstant.BIHIN_STATUS_SHONIN:
			rtn = SHATAK_BIHIN_TEIJI_STS_LABEL_SHONIN;
			break;
		}

		return rtn;
	}

	/**
	 * ドロップダウンリストの選択状態を設定する。
	 * 
	 * @param inDto
	 *            Skf3030Sc001SearchDto
	 * @return Skf3030Sc001SearchDto
	 */
	public Skf3030Sc001CommonDto setDropDownSelect(Skf3030Sc001CommonDto inDto) {

		String companyAgencySelect = inDto.getHdnCompanyAgencySelect();
		List<Map<String, Object>> companyAgencyList = inDto.getCompanyAgencyDropDownList();
		companyAgencyList = selectDropDown(companyAgencySelect, companyAgencyList);
		inDto.setCompanyAgencyDropDownList(companyAgencyList);

		List<Map<String, Object>> agencyList = new ArrayList<Map<String, Object>>();
		inDto.setHdnAgencyDisabled(Skf3030Sc001SharedService.ABLED);

		if (!NfwStringUtils.isEmpty(companyAgencySelect)) {
			if (Skf3030Sc001SharedService.CD_EXTERNAL_AGENCY.equals(companyAgencySelect)) {
				inDto.setHdnAgencyDisabled(Skf3030Sc001SharedService.DISABLED);

			} else {
				String agencySelect = inDto.getHdnAgencySelect();
				agencyList = skfDropDownUtils.getDdlAgencyByCd(companyAgencySelect, agencySelect, true);
			}
		}

		inDto.setAgencyDropDownList(agencyList);

		String yearSelect = inDto.getHdnYearSelect();
		List<Map<String, Object>> yearList = inDto.getYearDropDownList();
		yearList = selectDropDown(yearSelect, yearList);
		inDto.setYearDropDownList(yearList);

		String monthSelect = inDto.getHdnMonthSelect();
		List<Map<String, Object>> monthList = inDto.getMonthDropDownList();
		monthList = selectDropDown(monthSelect, monthList);
		inDto.setMonthDropDownList(monthList);

		String shatakuKbnSelect = inDto.getHdnShatakuKbnSelect();
		List<Map<String, Object>> shatakuKbnList = inDto.getShatakuKbnDropDownList();
		shatakuKbnList = selectDropDown(shatakuKbnSelect, shatakuKbnList);
		inDto.setShatakuKbnDropDownList(shatakuKbnList);

		String mutualuseSelect = inDto.getHdnMutualuseSelect();
		List<Map<String, Object>> mutualuseList = inDto.getMutualuseDropDownList();
		mutualuseList = selectDropDown(mutualuseSelect, mutualuseList);
		inDto.setMutualuseDropDownList(mutualuseList);

		return inDto;
	}

	/**
	 * 対象のドロップダウンを選択する。
	 * 
	 * @param val
	 *            選択された値
	 * @param dropDownList
	 *            対象のドロップダウンリスト
	 * @return 対象のドロップダウンリスト
	 */
	private List<Map<String, Object>> selectDropDown(String val, List<Map<String, Object>> dropDownList) {

		for (int i = 0; i < dropDownList.size(); i++) {
			Map<String, Object> targetMap = dropDownList.get(i);

			if (Objects.equals(val, targetMap.get("value"))) {
				targetMap.put("selected", true);
				dropDownList.set(i, targetMap);

			} else {
				targetMap.put("selected", false);
				dropDownList.set(i, targetMap);
			}
		}

		return dropDownList;
	}

	/**
	 * 文字列がnullの場合、空文字に変換する。
	 * 
	 * @param inStr
	 *            対象文字列
	 * @return 変換後の文字列
	 */
	public String cnvEmptyStrToNull(String inStr) {

		if (inStr != null) {
			return inStr;
		}

		return "";
	}

	/**
	 * 社宅台帳情報データを取得
	 * 
	 * @param yearMonth
	 *            システム年月
	 * @param preYearMonth
	 *            システム年月の前月
	 * @param postYearMonth
	 *            システム年月の次月
	 * @param akishatakSearchFlg
	 *            空部屋検索フラグ
	 * @return 社宅台帳情報データ
	 */
	public List<Skf3030Rp002GetShatakuDaichoInfoExp> getShatakuDaichoInfo(String yearMonth,
			boolean akishatakSearchFlg) {

		Skf3030Rp002GetShatakuDaichoInfoExpParameter param = new Skf3030Rp002GetShatakuDaichoInfoExpParameter();
		param.setYearMonth(yearMonth);

		if (akishatakSearchFlg) {
			param.setEmptyRoomSearchFlg("1");
		} else {
			param.setEmptyRoomSearchFlg("0");
		}

		List<Skf3030Rp002GetShatakuDaichoInfoExp> rtnData = skf3030Rp002GetShatakuDaichoInfoExpRepository
				.getShatakuDaichoInfo(param);

		return rtnData;
	}

	/**
	 * 社宅台帳情報データ(前月比較用)を取得
	 * 
	 * @param yearMonth
	 *            システム年月
	 * @param akishatakSearchFlg
	 *            空部屋検索フラグ
	 * @return 社宅台帳情報データ
	 */
	public List<Skf3030Rp003GetShatakuDaichoInfoExp> getShatakuDaichoCompareInfo(String yearMonth,
			boolean akishatakSearchFlg) {

		Skf3030Rp003GetShatakuDaichoInfoExpParameter param = new Skf3030Rp003GetShatakuDaichoInfoExpParameter();
		param.setYearMonth(yearMonth);

		if (akishatakSearchFlg) {
			param.setEmptyRoomSearchFlg("1");
		} else {
			param.setEmptyRoomSearchFlg("0");
		}

		List<Skf3030Rp003GetShatakuDaichoInfoExp> rtnData = skf3030Rp003GetShatakuDaichoInfoExpRepository
				.getShatakuDaichoInfo(param);

		return rtnData;
	}

	/**
	 * 出力判定情報データ取得
	 * 
	 * @param yearMonth
	 *            対象年月
	 * @return 出力判定情報データ
	 */
	public List<Skf3030Rp003GetShatakuDaichoDiffDataInfoExp> getShatakuDaichoDiffDataInfo(String yearMonth) {

		List<Skf3030Rp003GetShatakuDaichoDiffDataInfoExp> rtnData = skf3030Rp003GetShatakuDaichoDiffDataInfoExpRepository
				.getShatakuDaichoDiffDataInfo(yearMonth);

		return rtnData;
	}

	/**
	 * Excelのシリアル日付に変換する。
	 * 
	 * @param inDate
	 *            対象の日付
	 * @return 変換後の日付
	 */
	public String cnvExcelDate(String inDate) {

		Date date = skfDateFormatUtils.formatStringToDate(inDate);
		double time = DateUtil.getExcelDate(date);
		String outDate = String.valueOf(time);

		return outDate;
	}

	/**
	 * 対象終日を取得する。
	 * 
	 * @param inDate
	 *            基準日
	 * @return 対象終日
	 */
	public String getLastDay(String inDate) {

		SimpleDateFormat sdFormat = new SimpleDateFormat(SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT);
		Date forChangeDate;
		try {
			forChangeDate = sdFormat.parse(inDate);
		} catch (ParseException e) {
			return "";
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(forChangeDate);

		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date changedDate = calendar.getTime();

		String outDate = sdFormat.format(changedDate);

		return outDate;
	}

	/**
	 * 日数計算
	 * 
	 * @param startDate
	 *            開始日
	 * @param endDate
	 *            終了日
	 * @param firstDay
	 *            対象初日
	 * @param lastDay
	 *            対象終日
	 * @return 日数
	 */
	public String calcDayNum(String startDate, String endDate, String firstDay, String lastDay) {

		String rtnVal = "0";
		int diffDayCnt = 0;

		if (NfwStringUtils.isEmpty(startDate)) {
			return rtnVal;

		} else {
			int numStartDate = Integer.parseInt(startDate);
			int numFirstDate = Integer.parseInt(firstDay);
			int numLastDate = Integer.parseInt(lastDay);

			if (NfwStringUtils.isEmpty(endDate)) {
				if (numStartDate < numFirstDate) {
					diffDayCnt = numLastDate - numFirstDate;
					rtnVal = String.valueOf(diffDayCnt + 1);

				} else if (numFirstDate <= numStartDate && numStartDate <= numLastDate) {
					diffDayCnt = numLastDate - numStartDate;
					rtnVal = String.valueOf(diffDayCnt + 1);

				} else {
					return rtnVal;
				}

			} else {
				int numEndDate = Integer.parseInt(endDate);

				if (numStartDate > numEndDate) {
					return rtnVal;

				} else if (numStartDate < numFirstDate && numFirstDate <= numEndDate && numEndDate <= numLastDate) {
					diffDayCnt = numEndDate - numFirstDate;

				} else if (numFirstDate <= numStartDate && numStartDate <= numLastDate && numFirstDate <= numEndDate
						&& numEndDate <= numLastDate) {
					diffDayCnt = numEndDate - numStartDate;

				} else if (numFirstDate <= numStartDate && numStartDate <= numLastDate && numLastDate < numEndDate) {
					diffDayCnt = numLastDate - numStartDate;

				} else if (numLastDate < numStartDate && numLastDate < numEndDate) {
					return rtnVal;

				} else if (numStartDate < numFirstDate && numLastDate < numEndDate) {
					diffDayCnt = numLastDate - numFirstDate;

				} else {
					return rtnVal;
				}
			}
		}
		rtnVal = String.valueOf(diffDayCnt + 1);

		return rtnVal;
	}

	public Map<String, String> getBihinLentStsMap() {
		Map<String, String> rtnMap = new HashMap<String, String>();

		rtnMap.put("1", "×");
		rtnMap.put("2", "○");
		rtnMap.put("3", "1");
		rtnMap.put("4", "□");
		rtnMap.put("5", "△");

		return rtnMap;
	}

}
