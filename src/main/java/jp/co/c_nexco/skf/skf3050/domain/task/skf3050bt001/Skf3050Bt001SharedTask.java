/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3050.domain.task.skf3050bt001;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetBihinGenbutsuShikyugokeigakuExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetBihinMeisaiExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetBihinMeisaiExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetCompanyAgencyNameExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetHrRenkeiDataSakuseiMaeSyoriDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetShainSoshikiDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetShainSoshikiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetShatakiKanriDaityoDataTodoufukenDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetShatakiKanriDaityoDataTodoufukenDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetShatakuKanriDaityoSogoriyoDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetShatakuKihonJyohoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetShatakuSyainIdoRirekiDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetShatakuSyainIdoRirekiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetTsukibetuSyoryoRirekiDataJoinDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetTsukibetuSyoryoRirekiDataJoinDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetTsukibetuTyusyajyoRirekiDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetTsukibetuTyusyajyoRirekiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateBihinGoukeiExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateBihinMeisaiExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateGenbutsuSanteigakuExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateGetsujiShoriKanriDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateShozokuRirekiExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateTsukibetsuShiyoryoAccountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateTsukibetsuSiyoryorirekiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateTsukibetsuSiyoryorirekiGenbutsuSanteiExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBaseBusinessLogicUtils.SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBaseBusinessLogicUtils.SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MCompany;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3050MAccount;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001GetBihinGenbutsuShikyugokeigakuExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001GetBihinMeisaiExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001GetCompanyAgencyNameExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001GetDataForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001GetHrRenkeiDataSakuseiMaeSyoriDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001GetShainSoshikiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001GetShatakiKanriDaityoDataTodoufukenDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001GetShatakuKanriDaityoSogoriyoDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001GetShatakuKihonJyohoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001GetShatakuSyainIdoRirekiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001GetTsukibetuSyoryoRirekiDataJoinDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001GetTsukibetuTyusyajyoRirekiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateBihinGoukeiExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateBihinMeisaiExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateGenbutsuSanteigakuExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateGetsujiShoriKanriDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateShozokuRirekiExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateTsukibetsuShiyoryoAccountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateTsukibetsuSiyoryorirekiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateTsukibetsuSiyoryorirekiGenbutsuSanteiExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MCompanyRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3050MAccountRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.common.utils.PropertyUtils;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.SkfBatchBusinessLogicUtils;

/**
 * Skf3050Bt001AsyncCloseTask 締め処理（オンラインバッチ）共通クラス
 *
 * @author NEXCOシステムズ
 */
@Scope("prototype")
@Component
public class Skf3050Bt001SharedTask {

	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	@Autowired
	private SkfBatchBusinessLogicUtils skfBatchBusinessLogicUtils;
	@Autowired
	private Skf3050Bt001UpdateGenbutsuSanteigakuExpRepository skf3050Bt001UpdateGenbutsuSanteigakuExpRepository;
	@Autowired
	private Skf3050Bt001UpdateBihinGoukeiExpRepository skf3050Bt001UpdateBihinGoukeiExpRepository;
	@Autowired
	private Skf3050Bt001GetTsukibetuSyoryoRirekiDataJoinDataExpRepository skf3050Bt001GetTsukibetuSyoryoRirekiDataJoinDataExpRepository;
	@Autowired
	private Skf3050Bt001GetShatakuKanriDaityoSogoriyoDataExpRepository skf3050Bt001GetShatakuKanriDaityoSogoriyoDataExpRepository;
	@Autowired
	private Skf3050Bt001GetTsukibetuTyusyajyoRirekiDataExpRepository skf3050Bt001GetTsukibetuTyusyajyoRirekiDataExpRepository;
	@Autowired
	private Skf3050Bt001GetBihinMeisaiExpRepository skf3050Bt001GetBihinMeisaiExpRepository;
	@Autowired
	private Skf3050Bt001UpdateBihinMeisaiExpRepository skf3050Bt001UpdateBihinMeisaiExpRepository;
	@Autowired
	private Skf3050Bt001GetBihinGenbutsuShikyugokeigakuExpRepository skf3050Bt001GetBihinGenbutsuShikyugokeigakuExpRepository;
	@Autowired
	private Skf3050Bt001UpdateTsukibetsuSiyoryorirekiDataExpRepository skf3050Bt001UpdateTsukibetsuSiyoryorirekiDataExpRepository;
	@Autowired
	private Skf3050Bt001GetShatakuSyainIdoRirekiDataExpRepository skf3050Bt001GetShatakuSyainIdoRirekiDataExpRepository;
	@Autowired
	private Skf3050Bt001GetShainSoshikiDataExpRepository skf3050Bt001GetShainSoshikiDataExpRepository;
	@Autowired
	private Skf3050Bt001GetCompanyAgencyNameExpRepository skf3050Bt001GetCompanyAgencyNameExpRepository;
	@Autowired
	private Skf3050Bt001UpdateShozokuRirekiExpRepository skf3050Bt001UpdateShozokuRirekiExpRepository;
	@Autowired
	private Skf3050Bt001GetShatakiKanriDaityoDataTodoufukenDataExpRepository skf3050Bt001GetShatakiKanriDaityoDataTodoufukenDataExpRepository;
	@Autowired
	private Skf3050Bt001UpdateTsukibetsuSiyoryorirekiGenbutsuSanteiExpRepository skf3050Bt001UpdateTsukibetsuSiyoryorirekiGenbutsuSanteiExpRepository;
	@Autowired
	private Skf3050Bt001GetHrRenkeiDataSakuseiMaeSyoriDataExpRepository skf3050Bt001GetHrRenkeiDataSakuseiMaeSyoriDataExpRepository;
	@Autowired
	private Skf3050Bt001GetShatakuKihonJyohoExpRepository skf3050Bt001GetShatakuKihonJyohoExpRepository;
	@Autowired
	private Skf3050Bt001UpdateTsukibetsuShiyoryoAccountExpRepository skf3050Bt001UpdateTsukibetsuShiyoryoAccountExpRepository;
	@Autowired
	private Skf3050Bt001UpdateGetsujiShoriKanriDataExpRepository skf3050Bt001UpdateGetsujiShoriKanriDataExpRepository;
	@Autowired
	private Skf3050Bt001GetDataForUpdateExpRepository skf3050Bt001GetDataForUpdateExpRepository;
	@Autowired
	private Skf1010MCompanyRepository skf1010MCompanyRepository;
	@Autowired
	private Skf3050MAccountRepository skf3050MAccountRepository;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;

	public static final int PARAMETER_NUM = 5;
	public static final int RETURN_STATUS_OK = 0;
	public static final int RETURN_STATUS_NG = 9;
	public static final String SHORI_RESULT_STS_KEY = "shoriSts";
	public static final String SHORI_RESULT_SIYO_UPD_CNT_KEY = "shiyouUpdCount";
	public static final String SHORI_RESULT_GEN_UPD_CNT_KEY = "genUpdCount";
	public static final String BATCH_ID_B5001 = "B5001";

	public static final String BATCH_PRG_ID = "closeBatchPrgId";
	public static final String COMPANY_CD = "closeCompanyCd";
	public static final String USER_ID = "closeUserId";
	public static final String SHORI_NENGETSU = "closeShoriNengetsu";
	public static final String SHIME_SHORI_FLG = "shimeShoriFlg";

	public static final String PARAM_NAME_PRG_ID = "プログラムID";
	public static final String PARAM_NAME_COMPANY_CD = "会社コード";
	public static final String PARAM_NAME_USER_ID = "ユーザID";
	public static final String PARAM_NAME_SHORI_NENGETSU = "処理年月";
	public static final String PARAM_NAME_SHIME_SHORI_FLG = "締め処理フラグ";

	private static final String SHIYORYO_GETSUGAKU_SHORI_KBN = "2";
	private static final String CHUSHAJO_SHIYORYO_GETSUGAKU_SHORI_KBN = "4";
	private static final String SHIME_SHORI = "1";

	private static final String ERRMSG_SHATAKUKANRIDAICHOU_0 = "社宅管理台帳相互利用基本";
	private static final String ERRMSG_SHATAKUKANRIDAICHOU_1 = "社宅管理台帳ID";
	private static final String ERRMSG_GENSHIKYU_0 = "現物支給価額設定";
	private static final String ERRMSG_GENSHIKYU_TEKIYO_1 = "適用日";
	private static final String ERRMSG_GENSHIKYU_TODOFU_1 = "都道府県コード";
	private static final String ERRMSG_SHATAKUKIHON_0 = "社宅基本情報";
	private static final String ERRMSG_SHATAKUKIHON_1 = "社宅管理番号";
	private static final String ERRMSG_KAIKEI_0 = "会計転記用計上勘定科目設定";
	private static final String ERRMSG_KAIKEI_ACCOUNT_ID_1 = "勘定科目ID";
	private static final String ERRMSG_PARAM_SAI_0 = "月別使用料履歴（再計算）";
	private static final String ERRMSG_PARAM_GEN_0 = "月別使用料履歴（現物算定額）";

	private static final String ACCOUNT_ID_1 = "1";
	private static final String ACCOUNT_ID_2 = "2";
	private static final String ACCOUNT_ID_3 = "3";
	private static final String ACCOUNT_ID_4 = "4";
	private static final String ACCOUNT_ID_5 = "5";
	private static final String KEIJOU_KAMOKU_ACNT_CD_KEY = "accountCd";
	private static final String KEIJOU_KAMOKU_ACNT_NM_KEY = "accountName";
	private static final String KEIJOU_KAMOKU_RTN_STS_KEY = "returnStatus";
	private static final String KEIJOU_KAMOKU_ERR_MSG_KEY = "errMsg";

	/**
	 * バッチ制御テーブルを登録
	 * 
	 * @param parameter
	 *            パラメータ
	 * @param sysDate
	 *            システム処理日時
	 * @param endList
	 *            管理ログフッタ部の情報リスト
	 * @return 結果
	 * @throws ParseException
	 */
	@Transactional
	public int registBatchControl(Map<String, String> parameter, Date sysDate, List<String> endList)
			throws ParseException {

		String retParameterName = checkParameter(parameter);
		String programId = BATCH_ID_B5001;

		if (!NfwStringUtils.isEmpty(retParameterName)) {

			if (!retParameterName.contains(PARAM_NAME_COMPANY_CD)) {
				int retStat = insertBatchControl(parameter.get(COMPANY_CD), programId, parameter.get(USER_ID),
						SkfCommonConstant.ABNORMAL, sysDate, getStrSystemDate());

				if (retStat < 0) {
					outputManagementLogEndProc(endList, getStrSystemDate());
					return RETURN_STATUS_NG;
				}

				LogUtils.error(MessageIdConstant.E_SKF_1089, retParameterName);
				outputManagementLogEndProc(endList, null);
				return RETURN_STATUS_NG;

			} else {
				LogUtils.error(MessageIdConstant.E_SKF_1089, PARAM_NAME_COMPANY_CD);
				return RETURN_STATUS_NG;
			}
		}

		if (!BATCH_ID_B5001.equals(parameter.get(BATCH_PRG_ID))) {

			int retStat = insertBatchControl(parameter.get(COMPANY_CD), programId, parameter.get(USER_ID),
					SkfCommonConstant.ABNORMAL, sysDate, getStrSystemDate());

			if (retStat < 0) {
				outputManagementLogEndProc(endList, null);
				return RETURN_STATUS_NG;
			}

			LogUtils.error(MessageIdConstant.E_SKF_1090, parameter.get(BATCH_PRG_ID));
			outputManagementLogEndProc(endList, getStrSystemDate());
			return RETURN_STATUS_NG;
		}

		int retStat = insertBatchControl(parameter.get(COMPANY_CD), programId, parameter.get(USER_ID),
				SkfCommonConstant.PROCESSING, sysDate, null);

		if (retStat < 0) {
			outputManagementLogEndProc(endList, null);
			return RETURN_STATUS_NG;
		}

		return RETURN_STATUS_OK;
	}

	/**
	 * 月別処理管理、月次処理管理を更新
	 * 
	 * @param parameter
	 *            パラメータ
	 * @param endList
	 *            出力ログリスト
	 * @return 処理結果Map
	 * @throws ParseException
	 */
	@Transactional
	public String updateTsukibetsuTsukiji(Map<String, String> parameter, List<String> endList) throws ParseException {

		String rtn = SkfCommonConstant.COMPLETE;
		String paramUserId = parameter.get(USER_ID);
		String paramShoriNengetsu = parameter.get(SHORI_NENGETSU);

		List<String> lockResult = skf3050Bt001GetDataForUpdateExpRepository
				.getSkf3030TShatakuRentalRirekiData(paramShoriNengetsu);
		if (lockResult.size() == 0) {
			return SkfCommonConstant.ABNORMAL;
		}

		updateGenbutsuSanteigaku(paramUserId, paramShoriNengetsu);

		updateBihinGoukei(paramUserId, paramShoriNengetsu);

		List<Skf3050Bt001GetTsukibetuSyoryoRirekiDataJoinDataExp> renRirekiDtList = getTsukibetuSyoryoRirekiDataJoinData(
				paramShoriNengetsu);

		int shiyouUpdCount = 0;

		for (int i = 0; i < renRirekiDtList.size(); i++) {
			Skf3050Bt001GetTsukibetuSyoryoRirekiDataJoinDataExp renRirekiRow = renRirekiDtList.get(i);

			List<Skf3050Bt001GetShatakuKanriDaityoSogoriyoDataExp> sougoDt = getShatakuKanriDaityoSogoriyoData(
					renRirekiRow.getShatakuKanriId());

			if (sougoDt.size() <= 0) {
				LogUtils.error(MessageIdConstant.E_SKF_1106, ERRMSG_SHATAKUKANRIDAICHOU_0,
						ERRMSG_SHATAKUKANRIDAICHOU_1 + CodeConstant.COLON + renRirekiRow.getShatakuKanriId());
				rtn = SkfCommonConstant.ABNORMAL;
				break;
			}

			SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp shatakuRentCalcOutputData = getShatakuShiyoryoGetsugaku(
					paramShoriNengetsu, renRirekiRow, sougoDt.get(0).getRent());

			if (!NfwStringUtils.isEmpty(shatakuRentCalcOutputData.getErrMessage())) {
				LogUtils.errorByMsg(shatakuRentCalcOutputData.getErrMessage());
				rtn = SkfCommonConstant.ABNORMAL;
				break;
			}

			BigDecimal shatakuShiyouryouHiwari = getShatakuShiyoryoHiwari(paramShoriNengetsu,
					renRirekiRow.getNyukyoDate(), renRirekiRow.getTaikyoDate(),
					shatakuRentCalcOutputData.getShatakuShiyouryouGetsugaku());
			BigDecimal decimalRentalAdjust = BigDecimal.valueOf(renRirekiRow.getRentalAdjust());
			BigDecimal shatakuShiyouryouTyouseigo = shatakuShiyouryouHiwari.add(decimalRentalAdjust);

			BigDecimal chushajoShiyoryoGetsugaku1 = BigDecimal.ZERO;
			BigDecimal tyuushajouShiyouryouHiwari1 = BigDecimal.ZERO;

			List<Skf3050Bt001GetTsukibetuTyusyajyoRirekiDataExp> tsukiDt1List = getTsukibetuTyusyajyoRirekiData(
					renRirekiRow.getShatakuKanriId(), paramShoriNengetsu,
					Long.parseLong(CodeConstant.PARKING_LEND_NO_FIRST));

			if (tsukiDt1List.size() > 0) {
				SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp chushajoShiyoryoGetsugakuData = getChushajoShiyoryoGetsugaku(
						paramShoriNengetsu, renRirekiRow, sougoDt.get(0).getParkingRental(),
						tsukiDt1List.get(0).getParkingKanriNo());

				if (!NfwStringUtils.isEmpty(chushajoShiyoryoGetsugakuData.getErrMessage())) {
					LogUtils.errorByMsg(chushajoShiyoryoGetsugakuData.getErrMessage());
					rtn = SkfCommonConstant.ABNORMAL;
					break;
				}

				chushajoShiyoryoGetsugaku1 = chushajoShiyoryoGetsugakuData.getChushajouShiyoryou();
				tyuushajouShiyouryouHiwari1 = getChushajoShiyoryoHiwari(paramShoriNengetsu,
						tsukiDt1List.get(0).getParkingStartDate(), tsukiDt1List.get(0).getParkingEndDate(),
						chushajoShiyoryoGetsugaku1);
			}

			BigDecimal chushajoShiyoryoGetsugaku2 = BigDecimal.ZERO;
			BigDecimal tyuushajouShiyouryouHiwari2 = BigDecimal.ZERO;

			List<Skf3050Bt001GetTsukibetuTyusyajyoRirekiDataExp> tsukiDt2List = getTsukibetuTyusyajyoRirekiData(
					renRirekiRow.getShatakuKanriId(), paramShoriNengetsu,
					Long.parseLong(CodeConstant.PARKING_LEND_NO_SECOND));

			if (tsukiDt2List.size() > 0) {
				SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp chushajoShiyoryoGetsugakuData = getChushajoShiyoryoGetsugaku(
						paramShoriNengetsu, renRirekiRow, sougoDt.get(0).getParkingRental(),
						tsukiDt2List.get(0).getParkingKanriNo());

				if (!NfwStringUtils.isEmpty(chushajoShiyoryoGetsugakuData.getErrMessage())) {
					LogUtils.errorByMsg(chushajoShiyoryoGetsugakuData.getErrMessage());
					rtn = SkfCommonConstant.ABNORMAL;
					break;
				}

				chushajoShiyoryoGetsugaku2 = chushajoShiyoryoGetsugakuData.getChushajouShiyoryou();
				tyuushajouShiyouryouHiwari2 = getChushajoShiyoryoHiwari(paramShoriNengetsu,
						tsukiDt2List.get(0).getParkingStartDate(), tsukiDt2List.get(0).getParkingEndDate(),
						chushajoShiyoryoGetsugaku2);
			}

			List<Skf3050Bt001GetBihinMeisaiExp> bihinMeisaiDtList = getBihinMeisai(paramShoriNengetsu,
					renRirekiRow.getShatakuKanriId());

			for (int j = 0; j < bihinMeisaiDtList.size(); j++) {

				List<String> lockBihinMeisaiResult = skf3050Bt001GetDataForUpdateExpRepository
						.getSkf3030TShatakuBihinRirekiData(paramShoriNengetsu);
				if (lockBihinMeisaiResult.size() == 0) {
					return SkfCommonConstant.ABNORMAL;
				}

				updateBihinMeisai(renRirekiRow.getShatakuKanriId(), bihinMeisaiDtList.get(j).getBihinPayment(),
						paramUserId, bihinMeisaiDtList.get(j).getBihinCd(), paramShoriNengetsu);

				BigDecimal bihinDt = getBihinGenbutsuShikyugokeigaku(paramShoriNengetsu,
						renRirekiRow.getShatakuKanriId());
				BigDecimal bihinGenbutsuShikyuugakuGoukei = BigDecimal.ZERO;

				if (bihinDt != null) {
					bihinGenbutsuShikyuugakuGoukei = bihinDt;
				}

				BigDecimal decimalParkingRentalAdjust = BigDecimal.valueOf(renRirekiRow.getParkingRentalAdjust());
				BigDecimal tyuushajouShiyouryouTyouseigo = tyuushajouShiyouryouHiwari1.add(tyuushajouShiyouryouHiwari2)
						.add(decimalParkingRentalAdjust);

				Integer kojinHutanKyoekihiGetsugakuTyouseigo = renRirekiRow.getKyoekihiPerson()
						+ renRirekiRow.getKyoekihiPersonAdjust();

				Integer shatakuShiyoryoGetsugaku = shatakuRentCalcOutputData.getShatakuShiyouryouGetsugaku().intValue();

				shiyouUpdCount += updateTsukibetsuSiyoryorirekiData(shatakuShiyoryoGetsugaku,
						shatakuShiyouryouHiwari.intValue(), shatakuShiyouryouTyouseigo.intValue(),
						chushajoShiyoryoGetsugaku1.intValue(), tyuushajouShiyouryouHiwari1.intValue(),
						chushajoShiyoryoGetsugaku2.intValue(), tyuushajouShiyouryouHiwari2.intValue(),
						tyuushajouShiyouryouTyouseigo.intValue(), bihinGenbutsuShikyuugakuGoukei.intValue(),
						kojinHutanKyoekihiGetsugakuTyouseigo, paramUserId, paramShoriNengetsu,
						renRirekiRow.getShatakuKanriId());

				String touJigyoCd = "";
				String touJigyoName = "";
				String companyCd = "";
				String agencyCd = "";
				String agencyName = "";
				String affilCd1 = "";
				String affilName1 = "";
				String affilCd2 = "";
				String affilName2 = "";

				String shoriNengetsuAddMonth = skfDateFormatUtils.addYearMonth(paramShoriNengetsu, 1);
				List<Skf3050Bt001GetShatakuSyainIdoRirekiDataExp> shainIdoDt1List = getShatakuSyainIdoRirekiDataSyutoku(
						shoriNengetsuAddMonth, CodeConstant.BEGINNING_END_KBN_MONTH_BEGIN, parameter.get(COMPANY_CD),
						renRirekiRow.getShainNo());

				if (shainIdoDt1List.size() > 0) {
					Skf3050Bt001GetShatakuSyainIdoRirekiDataExp shainIdoDt1 = shainIdoDt1List.get(0);

					if (!NfwStringUtils.isEmpty(shainIdoDt1.getRirekiBusinessAreaCd())) {
						touJigyoCd = shainIdoDt1.getRirekiBusinessAreaCd();
						touJigyoName = shainIdoDt1.getRirekiBusinessAreaName();
					} else {
						touJigyoCd = shainIdoDt1.getShainBusinessAreaCd();
						touJigyoName = shainIdoDt1.getShainBusinessAreaName();
					}

					if (NfwStringUtils.isEmpty(shainIdoDt1.getRirekiAgencyCd())
							&& NfwStringUtils.isEmpty(shainIdoDt1.getRirekiAffiliation1Cd())
							&& NfwStringUtils.isEmpty(shainIdoDt1.getRirekiAffiliation2Cd())) {
						List<Skf3050Bt001GetShainSoshikiDataExp> dtSoshikiList = getShainSoshikiData(
								parameter.get(COMPANY_CD), renRirekiRow.getShainNo());

						if (dtSoshikiList.size() > 0) {
							Skf3050Bt001GetShainSoshikiDataExp dtSoshiki = dtSoshikiList.get(0);
							companyCd = dtSoshiki.getOriginalCompanyCd();
							agencyCd = dtSoshiki.getSoshikiAgencyCd();
							agencyName = dtSoshiki.getSoshikiAgencyName();
							affilCd1 = dtSoshiki.getSoshikiAffiliation1Cd();
							affilName1 = dtSoshiki.getSoshikiAffiliation1Name();
							affilCd2 = dtSoshiki.getSoshikiAffiliation2Cd();
							affilName2 = dtSoshiki.getSoshikiAffiliation2Name();
						}

					} else {
						companyCd = shainIdoDt1.getCompanyCd();
						agencyCd = shainIdoDt1.getRirekiAgencyCd();
						agencyName = shainIdoDt1.getRirekiAgencyName();
						affilCd1 = shainIdoDt1.getRirekiAffiliation1Cd();
						affilName1 = shainIdoDt1.getRirekiAffiliation1Name();
						affilCd2 = shainIdoDt1.getRirekiAffiliation2Cd();
						affilName2 = shainIdoDt1.getRirekiAffiliation2Name();
					}
				}

				String preJigyoCd = "";
				String preJigyoName = "";

				List<Skf3050Bt001GetShatakuSyainIdoRirekiDataExp> shainIdoDt2List = getShatakuSyainIdoRirekiDataSyutoku(
						paramShoriNengetsu, CodeConstant.BEGINNING_END_KBN_MONTH_END, parameter.get(COMPANY_CD),
						renRirekiRow.getShainNo());

				if (shainIdoDt2List.size() > 0) {
					Skf3050Bt001GetShatakuSyainIdoRirekiDataExp shainIdoDt2 = shainIdoDt2List.get(0);

					if (!NfwStringUtils.isEmpty(shainIdoDt2.getRirekiBusinessAreaCd())) {
						preJigyoCd = shainIdoDt2.getRirekiBusinessAreaCd();
						preJigyoName = shainIdoDt2.getRirekiBusinessAreaName();
					} else {
						preJigyoCd = shainIdoDt2.getShainBusinessAreaCd();
						preJigyoName = shainIdoDt2.getShainBusinessAreaName();
					}
				}

				List<String> lockShozokuRirekiResult = skf3050Bt001GetDataForUpdateExpRepository
						.getSkf3030TShozokuRirekiData(paramShoriNengetsu);
				if (lockShozokuRirekiResult.size() == 0) {
					return SkfCommonConstant.ABNORMAL;
				}

				updateShozokuRireki(companyCd, agencyCd, agencyName, affilCd1, affilName1, affilCd2, affilName2,
						touJigyoCd, touJigyoName, preJigyoCd, preJigyoName, paramUserId,
						renRirekiRow.getShatakuKanriId(), paramShoriNengetsu);
			}
		}

		int genUpdCount = 0;
		List<Skf3050Bt001GetShatakiKanriDaityoDataTodoufukenDataExp> genSanteiDtList = getShatakiKanriDaityoDataTodoufukenData(
				paramShoriNengetsu);

		for (int i = 0; i < genSanteiDtList.size(); i++) {
			Skf3050Bt001GetShatakiKanriDaityoDataTodoufukenDataExp genSanteiRow = genSanteiDtList.get(i);

			if (NfwStringUtils.isEmpty(genSanteiRow.getEffectiveDate())) {
				String errMsg = ERRMSG_GENSHIKYU_TEKIYO_1 + CodeConstant.COLON + getGetsumatsujitu(paramShoriNengetsu)
						+ CodeConstant.COMMA + ERRMSG_GENSHIKYU_TODOFU_1 + CodeConstant.COLON
						+ genSanteiRow.getPrefCd();
				LogUtils.error(MessageIdConstant.E_SKF_1106, ERRMSG_GENSHIKYU_0, errMsg);
				rtn = SkfCommonConstant.ABNORMAL;
				break;
			}

			BigDecimal genSanMenseki = genSanteiRow.getMenseki().add(genSanteiRow.getBarnMenseki());
			genSanMenseki = genSanMenseki.setScale(0, RoundingMode.DOWN);

			BigDecimal genSanteiGaku = calcGenSanteiGaku(genSanMenseki, genSanteiRow);

			genUpdCount += updateTsukibetsuSiyoryorirekiGenbutsuSantei(genSanteiGaku.intValue(), paramUserId,
					paramShoriNengetsu, genSanteiRow.getShatakuKanriId());
		}

		List<Skf3050Bt001GetHrRenkeiDataSakuseiMaeSyoriDataExp> renkeiDataSakuseiMaeDataList = getHrRenkeiDataSakuseiMaeSyoriData(
				paramShoriNengetsu);

		for (int i = 0; i < renkeiDataSakuseiMaeDataList.size(); i++) {
			Skf3050Bt001GetHrRenkeiDataSakuseiMaeSyoriDataExp renkeiDataSakuseiMaeData = renkeiDataSakuseiMaeDataList
					.get(i);

			List<Skf3050Bt001GetShatakuKihonJyohoExp> shatakuKihonDtList = getShatakuKihonJyoho(
					renkeiDataSakuseiMaeData.getShatakuKanriNo());

			if (shatakuKihonDtList.size() <= 0) {
				LogUtils.error(MessageIdConstant.E_SKF_1106, ERRMSG_SHATAKUKIHON_0,
						ERRMSG_SHATAKUKIHON_1 + CodeConstant.COLON + renkeiDataSakuseiMaeData.getShatakuKanriNo());
				rtn = SkfCommonConstant.ABNORMAL;
				break;
			}

			Skf3050Bt001GetShatakuKihonJyohoExp shatakuKihonDt = shatakuKihonDtList.get(0);

			int shiyouryo = renkeiDataSakuseiMaeData.getRentalTotal()
					+ renkeiDataSakuseiMaeData.getParkingRentalTotal();
			Map<String, Object> ret1Map = getKeijouKamoku(shiyouryo, renkeiDataSakuseiMaeData.getPayCompanyCd(),
					shatakuKihonDt.getManegeCompanyCd(), renkeiDataSakuseiMaeData.getKariukeCompanyCd(),
					renkeiDataSakuseiMaeData.getMutualUseKbn(), shatakuKihonDt.getShatakuKbn(),
					CodeConstant.ACCOUNT_KBN_SHATAKU);

			int rtn1Sts = (int) ret1Map.get(KEIJOU_KAMOKU_RTN_STS_KEY);
			if (rtn1Sts == RETURN_STATUS_NG) {
				String msg = (String) ret1Map.get(KEIJOU_KAMOKU_ERR_MSG_KEY);
				LogUtils.error(MessageIdConstant.E_SKF_1106, ERRMSG_KAIKEI_0, msg);
				rtn = SkfCommonConstant.ABNORMAL;
				break;
			}

			Map<String, Object> ret2Map = getKeijouKamoku(renkeiDataSakuseiMaeData.getKyoekihiPersonTotal(),
					renkeiDataSakuseiMaeData.getPayCompanyCd(), shatakuKihonDt.getManegeCompanyCd(),
					renkeiDataSakuseiMaeData.getKariukeCompanyCd(), renkeiDataSakuseiMaeData.getMutualUseKbn(),
					shatakuKihonDt.getShatakuKbn(), CodeConstant.ACCOUNT_KBN_KYOEKI);

			int rtn2Sts = (int) ret2Map.get(KEIJOU_KAMOKU_RTN_STS_KEY);
			if (rtn2Sts == RETURN_STATUS_NG) {
				String msg = (String) ret2Map.get(KEIJOU_KAMOKU_ERR_MSG_KEY);
				LogUtils.error(MessageIdConstant.E_SKF_1106, ERRMSG_KAIKEI_0, msg);
				rtn = SkfCommonConstant.ABNORMAL;
				break;
			}

			String kanjouKamokuCdShataku = (String) ret1Map.get(KEIJOU_KAMOKU_ACNT_CD_KEY);
			String kanjouKamokuMeishouShataku = (String) ret1Map.get(KEIJOU_KAMOKU_ACNT_NM_KEY);
			String kanjouKamokuCdKojin = (String) ret2Map.get(KEIJOU_KAMOKU_ACNT_CD_KEY);
			String kanjouKamokuMeishouKojin = (String) ret2Map.get(KEIJOU_KAMOKU_ACNT_NM_KEY);

			updateTsukibetsuShiyoryoAccount(kanjouKamokuCdShataku, kanjouKamokuMeishouShataku, kanjouKamokuCdKojin,
					kanjouKamokuMeishouKojin, paramUserId, renkeiDataSakuseiMaeData.getShatakuKanriId(),
					paramShoriNengetsu);
		}

		if (SHIME_SHORI.equals(parameter.get(SHIME_SHORI_FLG))) {
			List<String> lockGetsujiShoriKanriResult = skf3050Bt001GetDataForUpdateExpRepository
					.getSkf3050TMonthlyManageData(paramShoriNengetsu);
			if (lockGetsujiShoriKanriResult.size() == 0) {
				return SkfCommonConstant.ABNORMAL;
			}

			updateGetsujiShoriKanriData(paramUserId, paramShoriNengetsu);
		}

		outputKanriInfoLog(String.valueOf(shiyouUpdCount), String.valueOf(genUpdCount));

		return rtn;
	}

	/**
	 * バッチ制御テーブルを更新
	 * 
	 * @param endFlag
	 *            終了フラグ
	 * @param companyCd
	 *            会社コード
	 * @param programId
	 *            プログラムID
	 * @param searchEndFlag
	 *            検索用終了フラグ
	 * @return 処理結果
	 * @throws ParseException
	 */
	@Transactional
	public int endProc(String endFlag, String companyCd, String programId, String searchEndFlag) throws ParseException {

		Date endDate = getStrSystemDate();

		int updateCnt = skfBatchBusinessLogicUtils.updateBatchControl(endDate, endFlag, companyCd, programId,
				searchEndFlag);

		if (updateCnt > 0) {
			return RETURN_STATUS_OK;
		}

		return RETURN_STATUS_NG;
	}

	/**
	 * ログ出力メッセージを編集する。 メッセージのフォーマットの文字列を置き換える。
	 *
	 * @param msgId
	 *            メッセージID
	 * @param replaceStrArry
	 *            置換文字列
	 * @return 編集後のメッセージ
	 */
	public String editMsg(String msgId, String[] replaceStrArry) {

		String msgFormat = PropertyUtils.getValue(msgId);
		String outMsg = msgFormat;

		for (int i = 0; i < replaceStrArry.length; i++) {
			String regex = "{" + i + "}";
			String replaceStr = replaceStrArry[i];
			outMsg = outMsg.replace(regex, replaceStr);
		}

		return outMsg;
	}

	/**
	 * システムデータを取得し、文字列に変換した値を取得する。
	 * 
	 * @return 文字列変換後のシステムデータ
	 */
	public Date getStrSystemDate() {

		Date systemDate = skfBaseBusinessLogicUtils.getSystemDateTime();

		return systemDate;
	}

	/**
	 * パラメータ取得可否チェック
	 * 
	 * @param parameter
	 *            パラメータ
	 * @return エラー対象パラメータ名
	 */
	private String checkParameter(Map<String, String> parameter) {

		String retParameterName = "";

		if (isParamEmpty(parameter.get(BATCH_PRG_ID))) {
			retParameterName = PARAM_NAME_PRG_ID;
		}

		if (isParamEmpty(parameter.get(COMPANY_CD))) {
			retParameterName += CodeConstant.COMMA + PARAM_NAME_COMPANY_CD;
		}

		if (isParamEmpty(parameter.get(USER_ID))) {
			retParameterName += CodeConstant.COMMA + PARAM_NAME_USER_ID;
		}

		if (isParamEmpty(parameter.get(SHORI_NENGETSU))) {
			retParameterName += CodeConstant.COMMA + PARAM_NAME_SHORI_NENGETSU;
		}

		if (isParamEmpty(parameter.get(SHIME_SHORI_FLG))) {
			retParameterName += CodeConstant.COMMA + PARAM_NAME_SHIME_SHORI_FLG;
		}

		if (retParameterName.startsWith(CodeConstant.COMMA)) {
			retParameterName = retParameterName.substring(1);
		}

		return retParameterName;
	}

	/**
	 * パラメータが空であるかチェックする。
	 * 
	 * @param inParam
	 *            チェックパラメータ
	 * @return 結果
	 */
	private boolean isParamEmpty(String inParam) {

		if (NfwStringUtils.isEmpty(inParam)) {
			return true;
		}

		String param = inParam.replaceFirst("^[\\h]+", "").replaceFirst("[\\h]+$", "");

		if (NfwStringUtils.isEmpty(param)) {
			return true;
		}

		return false;
	}

	/**
	 * 管理ログの終了処理
	 * 
	 * @param kanriInfoList
	 *            管理情報リスト
	 */
	public void outputManagementLogEndProc(List<String> kanriInfoList, Date sysDate) {

		String strSysDate = CodeConstant.HYPHEN;
		if (sysDate != null) {
			strSysDate = sysDate.toString();
		}

		LogUtils.infoByMsg("処理終了時間：" + strSysDate);

		for (int i = 0; i < kanriInfoList.size(); i++) {
			LogUtils.infoByMsg(kanriInfoList.get(i));
		}
	}

	/**
	 * バッチ制御テーブルを登録する。
	 * 
	 * @param companyCd
	 *            会社コード
	 * @param programId
	 *            プログラムID
	 * @param userId
	 *            ユーザID
	 * @param endFlg
	 *            終了フラグ
	 * @param startDate
	 *            開始日時
	 * @param endDate
	 *            終了日時
	 * @return 登録件数
	 * @throws ParseException
	 */
	private int insertBatchControl(String companyCd, String programId, String userId, String endFlg, Date startDate,
			Date endDate) throws ParseException {

		// Skf1010TBatchControl param = new Skf1010TBatchControl();
		//
		// param.setCompanyCd(companyCd);
		// param.setProgramId(programId);
		// param.setUserId(userId);
		// param.setEndFlg(endFlg);
		// // ログファイルの作成が無しになった為、nullで登録しておく。
		// param.setLogFileName(null);
		//
		// if (startDate == null) {
		// param.setStartDate(null);
		// } else {
		// param.setStartDate(startDate);
		// }
		//
		// if (endDate == null) {
		// param.setEndDate(null);
		// } else {
		// param.setEndDate(endDate);
		// }
		//
		// int insertCount = skf1010TBatchControlRepository.insert(param);

		int insertCount = skfBatchBusinessLogicUtils.insertBatchControl(companyCd, programId, userId, endFlg, startDate,
				endDate);

		return insertCount;
	}

	/**
	 * 月別使用料履歴更新（現物算定額）
	 * 
	 * @param updateUser
	 *            更新ユーザ
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 更新件数
	 */
	private void updateGenbutsuSanteigaku(String updateUser, String shoriNengetsu) {

		Skf3050Bt001UpdateGenbutsuSanteigakuExpParameter param = new Skf3050Bt001UpdateGenbutsuSanteigakuExpParameter();
		param.setUpdateUserId(updateUser);
		param.setYearMonth(shoriNengetsu);

		skf3050Bt001UpdateGenbutsuSanteigakuExpRepository.updateGenbutsuSanteigaku(param);
	}

	/**
	 * 月別使用料履歴更新（備品現物支給合計額）
	 * 
	 * @param updateUser
	 *            更新ユーザ
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 更新件数
	 */
	private void updateBihinGoukei(String updateUser, String shoriNengetsu) {

		Skf3050Bt001UpdateBihinGoukeiExpParameter param = new Skf3050Bt001UpdateBihinGoukeiExpParameter();
		param.setUpdateUserId(updateUser);
		param.setYearMonth(shoriNengetsu);

		skf3050Bt001UpdateBihinGoukeiExpRepository.updateBihinGoukei(param);
	}

	/**
	 * 月別使用料履歴テーブル結合データの社宅使用料月額データ取得
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 月別使用料履歴テーブル結合データ
	 */
	private List<Skf3050Bt001GetTsukibetuSyoryoRirekiDataJoinDataExp> getTsukibetuSyoryoRirekiDataJoinData(
			String shoriNengetsu) {

		Skf3050Bt001GetTsukibetuSyoryoRirekiDataJoinDataExpParameter param = new Skf3050Bt001GetTsukibetuSyoryoRirekiDataJoinDataExpParameter();
		param.setShoriNengetsu(shoriNengetsu);

		List<Skf3050Bt001GetTsukibetuSyoryoRirekiDataJoinDataExp> outData = skf3050Bt001GetTsukibetuSyoryoRirekiDataJoinDataExpRepository
				.getTsukibetuSyoryoRirekiDataJoinData(param);

		return outData;
	}

	/**
	 * 管理ログを出力する。
	 * 
	 * @param shiyouUpdCount
	 *            月別使用料履歴テーブル（社宅使用料、駐車場使用料）更新カウンタ
	 * @param genUpdCount
	 *            月別使用料履歴テーブル（現物算定額）更新カウンタ
	 */
	private void outputKanriInfoLog(String shiyouUpdCount, String genUpdCount) {
		LogUtils.info(MessageIdConstant.I_SKF_1030, ERRMSG_PARAM_SAI_0, shiyouUpdCount);
		LogUtils.info(MessageIdConstant.I_SKF_1030, ERRMSG_PARAM_GEN_0, genUpdCount);
	}

	/**
	 * 社宅管理台帳相互利用基本テーブルのデータ取得
	 * 
	 * @param shatakuKanriDaityouId
	 *            社宅管理台帳ID
	 * @return 社宅管理台帳相互利用基本テーブルデータ
	 */
	private List<Skf3050Bt001GetShatakuKanriDaityoSogoriyoDataExp> getShatakuKanriDaityoSogoriyoData(
			long shatakuKanriDaityouId) {

		List<Skf3050Bt001GetShatakuKanriDaityoSogoriyoDataExp> outData = skf3050Bt001GetShatakuKanriDaityoSogoriyoDataExpRepository
				.getShatakuKanriDaityoSogoriyoData(shatakuKanriDaityouId);

		return outData;
	}

	/**
	 * 社宅使用料月額取得
	 * 
	 * @param shoriNengetsu
	 *            処理年月(yyyyMM)
	 * @param renRirekiRow
	 *            月別使用料履歴レコード
	 * @param shatakuChintairyo
	 *            社宅賃貸料
	 * @return 社宅使用料計算結果保持Entity
	 * @throws ParseException
	 */
	private SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp getShatakuShiyoryoGetsugaku(String shoriNengetsu,
			Skf3050Bt001GetTsukibetuSyoryoRirekiDataJoinDataExp renRirekiRow, int shatakuChintairyo)
			throws ParseException {

		String birthDay = "";

		if (renRirekiRow.getBirthdayYear() != 0) {
			birthDay = String.format("%04d", renRirekiRow.getBirthdayYear());
			birthDay += String.format("%02d", renRirekiRow.getBirthdayMonth());
			birthDay += String.format("%02d", renRirekiRow.getBirthdayDay());

		} else {
			birthDay = "";
		}

		SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp param = new SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp();
		param.setShoriNengetsu(shoriNengetsu);
		param.setShoriKbn(SHIYORYO_GETSUGAKU_SHORI_KBN);
		param.setShiyouryouPatternId(renRirekiRow.getRentalPatternId().toString());
		param.setYakuinKbn(renRirekiRow.getYakuinSannteiKbn());
		param.setShatakuChintairyou(String.valueOf(shatakuChintairyo));
		param.setSeinengappi(birthDay);

		SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp outData = skfBaseBusinessLogicUtils
				.getShatakuShiyouryouKeisan(param);

		return outData;
	}

	/**
	 * 社宅使用料日割額算出
	 * 
	 * @param shoriNengetsu
	 *            処理年月(YYYYMM)
	 * @param nyuukyoDate
	 *            入居日(YYYYMMDD)
	 * @param taikyoDate
	 *            退去日(YYYYMMDD)
	 * @param shatakuRiyouryouGetsugaku
	 *            社宅使用料月額
	 * @return 社宅使用料日割額
	 */
	private BigDecimal getShatakuShiyoryoHiwari(String shoriNengetsu, String nyuukyoDate, String taikyoDate,
			BigDecimal shatakuRiyouryouGetsugaku) {

		String shoriNengetsuShonichi = shoriNengetsu + "01";

		BigDecimal decimalNyuukyoDate = new BigDecimal(nyuukyoDate);
		BigDecimal decimalShoriNengetsuShonichi = new BigDecimal(shoriNengetsuShonichi);

		if (decimalNyuukyoDate.compareTo(decimalShoriNengetsuShonichi) < 0) {
			nyuukyoDate = shoriNengetsuShonichi;
		}

		String shoriNengetsuMatsujitsu = getGetsumatsujitu(shoriNengetsu);
		BigDecimal decimalShoriNengetsuMatsujitsu = new BigDecimal(shoriNengetsuMatsujitsu);

		if (NfwStringUtils.isEmpty(taikyoDate)) {
			taikyoDate = shoriNengetsuMatsujitsu;

		} else {
			BigDecimal decimalTaikyoDate = new BigDecimal(taikyoDate);
			if (decimalTaikyoDate.compareTo(decimalShoriNengetsuMatsujitsu) > 0) {
				taikyoDate = shoriNengetsuMatsujitsu;
			}
		}

		String nextShoriNengetsu = skfDateFormatUtils.addYearMonth(shoriNengetsu, 1);
		BigDecimal decimalNextShoriNengetsu = new BigDecimal(nextShoriNengetsu);
		BigDecimal decimalNyuukyoYymm = new BigDecimal(nyuukyoDate.substring(0, 6));
		BigDecimal taikyoDateYymm = new BigDecimal(taikyoDate.substring(0, 6));
		String beforeShoriNengetsu = skfDateFormatUtils.addYearMonth(shoriNengetsu, -1);
		BigDecimal decimalBeforeShoriNengetsu = new BigDecimal(beforeShoriNengetsu);

		BigDecimal outDate = BigDecimal.ZERO;

		if (nyuukyoDate.equals(shoriNengetsuShonichi) && shoriNengetsuMatsujitsu.equals(taikyoDate)) {
			outDate = shatakuRiyouryouGetsugaku;

		} else if (decimalNextShoriNengetsu.compareTo(decimalNyuukyoYymm) <= 0
				|| taikyoDateYymm.compareTo(decimalBeforeShoriNengetsu) <= 0) {
			outDate = BigDecimal.ZERO;

		} else if (!nyuukyoDate.equals(shoriNengetsuShonichi) || !taikyoDate.equals(shoriNengetsuMatsujitsu)) {
			BigDecimal taikyoDateDd = new BigDecimal(taikyoDate.substring(6, 8));
			BigDecimal nyuukyoDateDd = new BigDecimal(nyuukyoDate.substring(6, 8));
			BigDecimal shoriNengetsuMatsujitsuDd = new BigDecimal(shoriNengetsuMatsujitsu.substring(6, 8));

			outDate = shatakuRiyouryouGetsugaku.multiply(taikyoDateDd).subtract(nyuukyoDateDd)
					.divide(shoriNengetsuMatsujitsuDd, 0, RoundingMode.DOWN);
		}

		outDate = outDate.setScale(0, RoundingMode.DOWN);

		return outDate;
	}

	/**
	 * 月末日取得
	 * 
	 * @param nengetsu
	 *            処理年月(yyyyMM)
	 * @return 月末日(yyyyMMdd)
	 */
	private String getGetsumatsujitu(String nengetsu) {

		if (NfwStringUtils.isEmpty(nengetsu)) {
			return nengetsu;
		}

		Date dtEnd = skfDateFormatUtils.formatStringToDate(nengetsu + "01");

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dtEnd);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);

		Date changedDate = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat(SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT);

		String outDate = sdf.format(changedDate);

		return outDate;
	}

	/**
	 * 月別駐車場履歴テーブルの駐車場管理番号データ取得
	 * 
	 * @param shatakuKanriDaityouId
	 *            社宅管理台帳ID
	 * @param shoriNengetsu
	 *            処理年月
	 * @param taiyoBangou
	 *            貸与番号
	 * @return 月別駐車場履歴テーブル結合データ
	 */
	private List<Skf3050Bt001GetTsukibetuTyusyajyoRirekiDataExp> getTsukibetuTyusyajyoRirekiData(
			Long shatakuKanriDaityouId, String shoriNengetsu, Long taiyoBangou) {

		Skf3050Bt001GetTsukibetuTyusyajyoRirekiDataExpParameter param = new Skf3050Bt001GetTsukibetuTyusyajyoRirekiDataExpParameter();
		param.setShatakuKanriId(shatakuKanriDaityouId);
		param.setParkingLendNo(taiyoBangou);
		param.setYearMonth(shoriNengetsu);

		List<Skf3050Bt001GetTsukibetuTyusyajyoRirekiDataExp> outData = skf3050Bt001GetTsukibetuTyusyajyoRirekiDataExpRepository
				.getTsukibetuTyusyajyoRirekiData(param);

		return outData;
	}

	/**
	 * 駐車場使用料月額取得
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @param renRirekiRow
	 *            月別使用料履歴レコード
	 * @param chushajoChintairyo
	 *            駐車場賃貸料
	 * @param chushajoKanriBangou
	 *            駐車場管理番号
	 * @return 駐車場使用料月額
	 * @throws ParseException
	 */
	private SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp getChushajoShiyoryoGetsugaku(String shoriNengetsu,
			Skf3050Bt001GetTsukibetuSyoryoRirekiDataJoinDataExp renRirekiRow, Integer chushajoChintairyo,
			Long chushajoKanriBangou) throws ParseException {

		SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp param = new SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp();
		param.setShoriNengetsu(shoriNengetsu);
		param.setShoriKbn(CHUSHAJO_SHIYORYO_GETSUGAKU_SHORI_KBN);
		param.setYakuinKbn(renRirekiRow.getYakuinSannteiKbn());
		param.setChyshajoChintairyou(String.valueOf(chushajoChintairyo));
		param.setShatakuKanriBangou(String.valueOf(renRirekiRow.getShatakuKanriNo()));
		param.setChushajoKanriBangou(String.valueOf(chushajoKanriBangou));
		param.setAuseKbn(renRirekiRow.getAuse());

		SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp outData = skfBaseBusinessLogicUtils
				.getShatakuShiyouryouKeisan(param);

		return outData;
	}

	/**
	 * 駐車場使用料日割額算出
	 * 
	 * @param shoriNengetsu
	 *            処理年月(YYYYMM)
	 * @param parkingStartDate
	 *            駐車場開始日
	 * @param parkingEndDate
	 *            駐車場返却日
	 * @param tyuushajouShiyouryouGetsugaku
	 *            駐車場使用料月額
	 * @return 駐車場使用料日割額
	 */
	private BigDecimal getChushajoShiyoryoHiwari(String shoriNengetsu, String parkingStartDate, String parkingEndDate,
			BigDecimal tyuushajouShiyouryouGetsugaku) {

		String shoriNengetsuShonichi = shoriNengetsu + "01";

		BigDecimal decimalParkingStartDate = new BigDecimal(parkingStartDate);
		BigDecimal decimalShoriNengetsuShonichi = new BigDecimal(shoriNengetsuShonichi);

		if (decimalParkingStartDate.compareTo(decimalShoriNengetsuShonichi) < 0) {
			parkingStartDate = shoriNengetsuShonichi;
		}

		BigDecimal decimalParkingEndDate = BigDecimal.ZERO;
		String shoriNengetsuMatsujitsu = getGetsumatsujitu(shoriNengetsu);
		BigDecimal decimalShoriNengetsuMatsujitsu = new BigDecimal(shoriNengetsuMatsujitsu);

		if (NfwStringUtils.isEmpty(parkingEndDate)) {
			parkingEndDate = shoriNengetsuMatsujitsu;
		} else {
			if (decimalParkingEndDate.compareTo(decimalShoriNengetsuMatsujitsu) > 0) {
				parkingEndDate = shoriNengetsuMatsujitsu;
			}
		}

		String nextShoriNengetsu = skfDateFormatUtils.addYearMonth(shoriNengetsu, 1);
		BigDecimal decimalNextShoriNengetsu = new BigDecimal(nextShoriNengetsu);
		BigDecimal parkingStartDateYymm = new BigDecimal(parkingStartDate.substring(0, 6));
		BigDecimal parkingEndDateYymm = new BigDecimal(parkingEndDate.substring(0, 6));
		String beforeShoriNengetsu = skfDateFormatUtils.addYearMonth(shoriNengetsu, -1);
		BigDecimal decimalBeforeShoriNengetsu = new BigDecimal(beforeShoriNengetsu);

		BigDecimal outDate = BigDecimal.ZERO;

		if (parkingStartDate.equals(shoriNengetsuShonichi) && shoriNengetsuMatsujitsu.equals(parkingEndDate)) {
			outDate = tyuushajouShiyouryouGetsugaku;

		} else if (decimalNextShoriNengetsu.compareTo(parkingStartDateYymm) <= 0
				|| parkingEndDateYymm.compareTo(decimalBeforeShoriNengetsu) <= 0) {
			outDate = BigDecimal.ZERO;

		} else if (!parkingStartDate.equals(shoriNengetsuShonichi) || !parkingEndDate.equals(shoriNengetsuMatsujitsu)) {
			BigDecimal parkingEndDateDd = new BigDecimal(parkingEndDate.substring(6, 8));
			BigDecimal parkingStartDateDd = new BigDecimal(parkingStartDate.substring(6, 8));
			BigDecimal shoriNengetsuMatsujitsuDd = new BigDecimal(shoriNengetsuMatsujitsu.substring(6, 8));

			outDate = tyuushajouShiyouryouGetsugaku.multiply(parkingEndDateDd)
					.subtract((parkingStartDateDd.add(BigDecimal.ONE)))
					.divide(shoriNengetsuMatsujitsuDd, 0, RoundingMode.DOWN);
		}

		outDate = outDate.setScale(0, RoundingMode.DOWN);

		return outDate;
	}

	/**
	 * 月別備品使用料明細テーブル取得
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @param shatakuDaichoId
	 *            社宅管理台帳ID
	 * @return 月別備品使用料明細データテーブル
	 */
	private List<Skf3050Bt001GetBihinMeisaiExp> getBihinMeisai(String shoriNengetsu, Long shatakuDaichoId) {

		Skf3050Bt001GetBihinMeisaiExpParameter param = new Skf3050Bt001GetBihinMeisaiExpParameter();
		param.setYearMonth(shoriNengetsu);
		param.setShatakuKanriId(shatakuDaichoId);

		List<Skf3050Bt001GetBihinMeisaiExp> outData = skf3050Bt001GetBihinMeisaiExpRepository.getBihinMeisai(param);

		return outData;
	}

	/**
	 * 月別備品使用料明細の備品現物額更新
	 * 
	 * @param shatakuDaichoId
	 *            社宅管理台帳ID
	 * @param bihinGenbutsuGaku
	 *            備品現物支給額
	 * @param userId
	 *            更新ユーザID
	 * @param bihinCd
	 *            備品コード
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 更新件数
	 */
	private void updateBihinMeisai(Long shatakuDaichoId, Integer bihinGenbutsuGaku, String userId, String bihinCd,
			String shoriNengetsu) {

		Skf3050Bt001UpdateBihinMeisaiExpParameter param = new Skf3050Bt001UpdateBihinMeisaiExpParameter();

		if (bihinGenbutsuGaku == null) {
			param.setBihinGenbutsuGaku(null);
		} else {
			param.setBihinGenbutsuGaku(bihinGenbutsuGaku);
		}

		if (NfwStringUtils.isEmpty(userId)) {
			param.setUpdateUser(null);
		} else {
			param.setUpdateUser(userId);
		}

		param.setShatakuKanriId(shatakuDaichoId);
		param.setYearMonth(shoriNengetsu);
		param.setBihinCd(bihinCd);

		skf3050Bt001UpdateBihinMeisaiExpRepository.updateBihinMeisai(param);

	}

	/**
	 * 備品現物支給額合計算出データ取得（会社保有、レンタル、備付）
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @param shataKanriId
	 *            社宅管理ID
	 * @return 備品現物支給額算出データ
	 */
	private BigDecimal getBihinGenbutsuShikyugokeigaku(String shoriNengetsu, Long shataKanriId) {

		Skf3050Bt001GetBihinGenbutsuShikyugokeigakuExpParameter param = new Skf3050Bt001GetBihinGenbutsuShikyugokeigakuExpParameter();

		if (shataKanriId == null) {
			param.setShatakuKanriId(null);
		} else {
			param.setShatakuKanriId(shataKanriId);
		}

		if (NfwStringUtils.isEmpty(shoriNengetsu)) {
			param.setYearMonth(null);
		} else {
			param.setYearMonth(shoriNengetsu);
		}

		BigDecimal outData = skf3050Bt001GetBihinGenbutsuShikyugokeigakuExpRepository
				.getBihinGenbutsuShikyugokeigaku(param);

		return outData;
	}

	/**
	 * 月別使用料履歴テーブルのデータ更新
	 * 
	 * @param rentalMonth
	 *            社宅使用料月額
	 * @param rentalDay
	 *            社宅使用料日割
	 * @param rentalTotal
	 *            社宅使用料月額（調整後）
	 * @param parking1RentalMonth
	 *            駐車場区画1使用料月額
	 * @param parking1RentalDay
	 *            駐車場区画1使用料日割
	 * @param parking2RentalMonth
	 *            駐車場区画2使用料月額
	 * @param parking2RentalDay
	 *            駐車場区画2使用料日割
	 * @param parkingRentalTotal
	 *            駐車場使用料月額（調整後）
	 * @param bihinGenbutuGoukei
	 *            備品現物支給合計額
	 * @param updateUser
	 *            更新ユーザ
	 * @param shoriNengetsu
	 *            処理年月
	 * @param shatakuKanriBangou
	 *            社宅管理台帳ID
	 */
	private Integer updateTsukibetsuSiyoryorirekiData(Integer rentalMonth, Integer rentalDay, Integer rentalTotal,
			Integer parking1RentalMonth, Integer parking1RentalDay, Integer parking2RentalMonth,
			Integer parking2RentalDay, Integer parkingRentalTotal, Integer bihinGenbutuGoukei,
			Integer kojinHutanKyoekiTotal, String updateUser, String shoriNengetsu, Long shatakuKanriBangou) {

		Skf3050Bt001UpdateTsukibetsuSiyoryorirekiDataExpParameter param = new Skf3050Bt001UpdateTsukibetsuSiyoryorirekiDataExpParameter();

		if (rentalMonth == null) {
			param.setRentalMonth(null);
		} else {
			param.setRentalMonth(rentalMonth);
		}

		if (rentalDay == null) {
			param.setRentalDay(null);
		} else {
			param.setRentalDay(rentalDay);
		}

		if (rentalTotal == null) {
			param.setRentalTotal(null);
		} else {
			param.setRentalTotal(rentalTotal);
		}

		if (parking1RentalMonth == null) {
			param.setParking1RentalMonth(null);
		} else {
			param.setParking1RentalMonth(parking1RentalMonth);
		}

		if (parking1RentalDay == null) {
			param.setParking1RentalDay(null);
		} else {
			param.setParking1RentalDay(parking1RentalDay);
		}

		if (parking2RentalMonth == null) {
			param.setParking2RentalMonth(null);
		} else {
			param.setParking2RentalMonth(parking2RentalMonth);
		}

		if (parking2RentalDay == null) {
			param.setParking2RentalDay(null);
		} else {
			param.setParking2RentalDay(parking2RentalDay);
		}

		if (parkingRentalTotal == null) {
			param.setParkingRentalTotal(null);
		} else {
			param.setParkingRentalTotal(parkingRentalTotal);
		}

		if (bihinGenbutuGoukei == null) {
			param.setBihinGenbutuGoukei(null);
		} else {
			param.setBihinGenbutuGoukei(bihinGenbutuGoukei);
		}

		if (kojinHutanKyoekiTotal == null) {
			param.setKyoekihiPersonTotal(null);
		} else {
			param.setKyoekihiPersonTotal(kojinHutanKyoekiTotal);
		}

		if (updateUser == null) {
			param.setUpdateUser(null);
		} else {
			param.setUpdateUser(updateUser);
		}

		param.setYearMonth(shoriNengetsu);
		param.setShatakuKanriId(shatakuKanriBangou);

		Integer outUpdateCnt = skf3050Bt001UpdateTsukibetsuSiyoryorirekiDataExpRepository
				.updateTsukibetsuSiyoryorirekiData(param);

		return outUpdateCnt;
	}

	/**
	 * 社宅社員異動履歴テーブルのデータ取得
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @param begEndKbn
	 *            月初月末区分
	 * @param companyCd
	 *            会社コード
	 * @param shainBangou
	 *            社員番号
	 * @return 社宅社員異動履歴テーブル
	 */
	private List<Skf3050Bt001GetShatakuSyainIdoRirekiDataExp> getShatakuSyainIdoRirekiDataSyutoku(String shoriNengetsu,
			String begEndKbn, String companyCd, String shainBangou) {

		Skf3050Bt001GetShatakuSyainIdoRirekiDataExpParameter param = new Skf3050Bt001GetShatakuSyainIdoRirekiDataExpParameter();
		param.setYearMonth(shoriNengetsu);
		param.setBeginningEndKbn(begEndKbn);
		param.setCompanyCd(companyCd);
		param.setShainNo(shainBangou);

		List<Skf3050Bt001GetShatakuSyainIdoRirekiDataExp> outData = skf3050Bt001GetShatakuSyainIdoRirekiDataExpRepository
				.getShatakuSyainIdoRirekiData(param);

		return outData;
	}

	/**
	 * 組織マスタ情報取得
	 * 
	 * @param companyCd
	 *            会社コード
	 * @param shainBangou
	 *            社員番号
	 * @return 組織マスタテーブル
	 */
	private List<Skf3050Bt001GetShainSoshikiDataExp> getShainSoshikiData(String companyCd, String shainBangou) {

		Skf3050Bt001GetShainSoshikiDataExpParameter param = new Skf3050Bt001GetShainSoshikiDataExpParameter();
		param.setCompanyCd(companyCd);
		param.setShainNo(shainBangou);

		List<Skf3050Bt001GetShainSoshikiDataExp> outData = skf3050Bt001GetShainSoshikiDataExpRepository
				.getShainSoshikiData(param);

		return outData;
	}

	/**
	 * 月別所属情報履歴の当月事業領域、前月事業領域の更新
	 * 
	 * @param companyCd
	 *            会社コード
	 * @param agencyCd
	 *            機関コード
	 * @param agencyName
	 *            機関名称
	 * @param affCd1
	 *            所属1コード
	 * @param affName1
	 *            所属1名称
	 * @param affCd2
	 *            所属2コード
	 * @param affName2
	 *            所属2名称
	 * @param touJigyoryoikiCd
	 *            当月事業領域コード
	 * @param touJigyoryoikiName
	 *            当月事業領域名称
	 * @param preJigyoryoikiCd
	 *            前月事業領域コード
	 * @param preJigyoryoikiName
	 *            前月事業領域名称
	 * @param updateUser
	 *            更新ユーザ
	 * @param shatakuKanriId
	 *            社宅管理台帳ID
	 * @param shoriNengetsu
	 *            処理年月
	 */
	private void updateShozokuRireki(String companyCd, String agencyCd, String agencyName, String affCd1,
			String affName1, String affCd2, String affName2, String touJigyoryoikiCd, String touJigyoryoikiName,
			String preJigyoryoikiCd, String preJigyoryoikiName, String updateUser, Long shatakuKanriId,
			String shoriNengetsu) {

		Skf3050Bt001UpdateShozokuRirekiExpParameter param = new Skf3050Bt001UpdateShozokuRirekiExpParameter();

		if (companyCd == null) {
			param.setCompanyCd(null);
		} else {
			param.setCompanyCd(companyCd);
		}

		String companyName = getCompanyName(companyCd);
		param.setCompanyName(companyName);

		if (agencyCd == null) {
			param.setAgencyCd(null);
		} else {
			param.setAgencyCd(agencyCd);
		}

		if (agencyName == null) {
			param.setAgencyName(null);
		} else {
			param.setAgencyName(agencyName);
		}

		if (affCd1 == null) {
			param.setAffiliation1Cd(null);
		} else {
			param.setAffiliation1Cd(affCd1);
		}

		if (affName1 == null) {
			param.setAffiliation1(null);
		} else {
			param.setAffiliation1(affName1);
		}

		if (affCd2 == null) {
			param.setAffiliation2Cd(null);
		} else {
			param.setAffiliation2Cd(affCd2);
		}

		if (affName2 == null) {
			param.setAffiliation2(null);
		} else {
			param.setAffiliation2(affName2);
		}

		if (touJigyoryoikiCd == null) {
			param.setBusinessAreaCd(null);
		} else {
			param.setBusinessAreaCd(touJigyoryoikiCd);
		}

		if (touJigyoryoikiName == null) {
			param.setBusinessAreaName(null);
		} else {
			param.setBusinessAreaName(touJigyoryoikiName);
		}

		if (preJigyoryoikiCd == null) {
			param.setPreBusinessAreaCd(null);
		} else {
			param.setPreBusinessAreaCd(preJigyoryoikiCd);
		}

		if (preJigyoryoikiName == null) {
			param.setPreBusinessAreaName(null);
		} else {
			param.setPreBusinessAreaName(preJigyoryoikiName);
		}

		if (updateUser == null) {
			param.setUpdateUser(null);
		} else {
			param.setUpdateUser(updateUser);
		}

		param.setShatakuKanriId(shatakuKanriId);
		param.setYearMonth(shoriNengetsu);

		skf3050Bt001UpdateShozokuRirekiExpRepository.updateShozokuRireki(param);

	}

	/**
	 * 会社名取得
	 * 
	 * @param companyCd
	 *            会社コード
	 * @return 会社名
	 */
	private String getCompanyName(String companyCd) {

		String outVal = "";

		if (NfwStringUtils.isEmpty(companyCd)) {
			return outVal;
		}

		List<Skf3050Bt001GetCompanyAgencyNameExp> dataTable = skf3050Bt001GetCompanyAgencyNameExpRepository
				.getCompanyAgencyName(companyCd);

		if (dataTable.size() > 0) {
			outVal = dataTable.get(0).getCompanyName();
		}

		return outVal;
	}

	/**
	 * 社宅管理台帳基本テーブル等の現物算定額算出用データ取得
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 現物算定額算出用データ取得
	 */
	private List<Skf3050Bt001GetShatakiKanriDaityoDataTodoufukenDataExp> getShatakiKanriDaityoDataTodoufukenData(
			String shoriNengetsu) {

		Skf3050Bt001GetShatakiKanriDaityoDataTodoufukenDataExpParameter param = new Skf3050Bt001GetShatakiKanriDaityoDataTodoufukenDataExpParameter();

		if (!NfwStringUtils.isEmpty(shoriNengetsu)) {
			param.setYearMonth(shoriNengetsu);

			String nextShoriNengetsu = skfDateFormatUtils.addYearMonth(shoriNengetsu, 1);
			String yokugetsuMatsu = getGetsumatsujitu(nextShoriNengetsu);
			param.setYokugetsuMatsu(yokugetsuMatsu);

			String yokugetsuGessho = nextShoriNengetsu + "01";
			param.setYokugetsuGessho(yokugetsuGessho);
		}

		List<Skf3050Bt001GetShatakiKanriDaityoDataTodoufukenDataExp> outData = skf3050Bt001GetShatakiKanriDaityoDataTodoufukenDataExpRepository
				.getShatakiKanriDaityoDataTodoufukenData(param);

		return outData;
	}

	/**
	 * 現物算定額を取得 ※小数点以下切り捨て、面積は社宅使用料算定上延べ面積
	 * 
	 * @param genSanMenseki
	 *            対象面積
	 * @param genSanteiRow
	 *            現物算定額算出用データ
	 * @return 現物算定額
	 */
	private BigDecimal calcGenSanteiGaku(BigDecimal genSanMenseki,
			Skf3050Bt001GetShatakiKanriDaityoDataTodoufukenDataExp genSanteiRow) {

		BigDecimal kyojuRiekigaku = BigDecimal.valueOf(genSanteiRow.getKyojuRiekigaku());
		BigDecimal rentalMonth = BigDecimal.valueOf(genSanteiRow.getRentalMonth());
		BigDecimal parking1RentalMonth = BigDecimal.valueOf(genSanteiRow.getParking1RentalMonth());
		BigDecimal parking2RentalMonth = BigDecimal.valueOf(genSanteiRow.getParking2RentalMonth());
		String xmlGenbutsuSanteigakuSanshutsuBunshiKeisu = PropertyUtils
				.getValue("skf.common.settings.genbutsu_bunshi_keisuu");
		BigDecimal genbutsuSanteigakuSanshutsuBunshiKeisu = new BigDecimal(xmlGenbutsuSanteigakuSanshutsuBunshiKeisu);
		String xmlGenbutsuSanteigakuSanshutsuBunboKeisu = PropertyUtils
				.getValue("skf.common.settings.genbutsu_bunbo_keisuu");
		BigDecimal genbutsuSanteigakuSanshutsuBunboKeisu = new BigDecimal(xmlGenbutsuSanteigakuSanshutsuBunboKeisu);

		BigDecimal num_1 = genSanMenseki.multiply(kyojuRiekigaku).multiply(genbutsuSanteigakuSanshutsuBunshiKeisu);
		BigDecimal num_2 = num_1.divide(genbutsuSanteigakuSanshutsuBunboKeisu, 4, RoundingMode.DOWN);
		BigDecimal num_3 = num_2.subtract(rentalMonth).subtract(parking1RentalMonth).subtract(parking2RentalMonth);
		BigDecimal outVal = num_3.setScale(0, RoundingMode.DOWN);

		if (outVal.compareTo(BigDecimal.ZERO) < 0) {
			outVal = BigDecimal.ZERO;
		}

		return outVal;
	}

	/**
	 * 月別使用料履歴テーブル現物算定額のデータ更新
	 * 
	 * @param santeiGaku
	 *            現物算定額
	 * @param updateUser
	 *            更新ユーザ
	 * @param shoriNengetsu
	 *            処理年月
	 * @param shataKanriId
	 *            社宅管理台帳ID
	 */
	private Integer updateTsukibetsuSiyoryorirekiGenbutsuSantei(Integer santeiGaku, String updateUser,
			String shoriNengetsu, Long shataKanriId) {

		Skf3050Bt001UpdateTsukibetsuSiyoryorirekiGenbutsuSanteiExpParameter param = new Skf3050Bt001UpdateTsukibetsuSiyoryorirekiGenbutsuSanteiExpParameter();

		if (santeiGaku == null) {
			param.setGenbutuSantei(null);
		} else {
			param.setGenbutuSantei(santeiGaku);
		}

		if (updateUser == null) {
			param.setUpdateUser(null);
		} else {
			param.setUpdateUser(updateUser);
		}

		param.setYearMonth(shoriNengetsu);
		param.setShatakuKanriId(shataKanriId);

		Integer updateCnt = skf3050Bt001UpdateTsukibetsuSiyoryorirekiGenbutsuSanteiExpRepository
				.updateTsukibetsuSiyoryorirekiGenbutsuSantei(param);

		return updateCnt;
	}

	/**
	 * 月別利用料履歴テーブル取得（HR連携データ作成前処理取得）
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 月別利用料履歴テーブルの処理前データ
	 */
	private List<Skf3050Bt001GetHrRenkeiDataSakuseiMaeSyoriDataExp> getHrRenkeiDataSakuseiMaeSyoriData(
			String shoriNengetsu) {

		List<Skf3050Bt001GetHrRenkeiDataSakuseiMaeSyoriDataExp> outData = skf3050Bt001GetHrRenkeiDataSakuseiMaeSyoriDataExpRepository
				.getHrRenkeiDataSakuseiMaeSyoriData(shoriNengetsu);

		return outData;
	}

	/**
	 * 社宅基本情報テーブル取得
	 * 
	 * @param shatakuKanriBangou
	 *            社宅管理番号
	 * @return 社宅基本情報データ
	 */
	private List<Skf3050Bt001GetShatakuKihonJyohoExp> getShatakuKihonJyoho(Long shatakuKanriBangou) {

		List<Skf3050Bt001GetShatakuKihonJyohoExp> outData = skf3050Bt001GetShatakuKihonJyohoExpRepository
				.getShatakuKihonJyoho(shatakuKanriBangou);

		return outData;
	}

	/**
	 * 勘定科目コード、勘定科目名取得
	 * 
	 * @param shiyouryo
	 *            使用料
	 * @param kyuyoCompanyCd
	 *            給与支給会社コード
	 * @param manageCompanyCd
	 *            管理会社コード
	 * @param kariukeCompanyCd
	 *            借受会社コード
	 * @param sougoRiyouKbn
	 *            相互利用判定区分
	 * @param shatakuKbn
	 *            社宅区分
	 * @param kanjouKamokuKbn
	 *            勘定科目区分
	 * @return 処理結果Map
	 */
	private Map<String, Object> getKeijouKamoku(int shiyouryo, String kyuyoCompanyCd, String manageCompanyCd,
			String kariukeCompanyCd, String sougoRiyouKbn, String shatakuKbn, String kanjouKamokuKbn) {

		Map<String, Object> rtnMap = new HashMap<>();
		String accountId = "";

		if (CodeConstant.ACCOUNT_KBN_SHATAKU.equals(kanjouKamokuKbn)) {
			if (shiyouryo == 0) {
				rtnMap.put(KEIJOU_KAMOKU_ACNT_CD_KEY, "");
				rtnMap.put(KEIJOU_KAMOKU_ACNT_NM_KEY, "");
				rtnMap.put(KEIJOU_KAMOKU_RTN_STS_KEY, RETURN_STATUS_OK);
				return rtnMap;

			} else {
				if (CodeConstant.MUTUAL_USE_KBN_AVAILABLE.equals(sougoRiyouKbn)) {
					if (!kariukeCompanyCd.equals(kyuyoCompanyCd)) {
						Skf1010MCompany compDt = skf1010MCompanyRepository.selectByPrimaryKey(kyuyoCompanyCd);
						if (compDt != null) {
							accountId = ACCOUNT_ID_1;
						}
					}

				} else {
					if (!manageCompanyCd.equals(kyuyoCompanyCd)) {
						Skf1010MCompany compDt = skf1010MCompanyRepository.selectByPrimaryKey(kyuyoCompanyCd);
						if (compDt != null) {
							accountId = ACCOUNT_ID_1;
						}
					}
				}
			}

			if (NfwStringUtils.isEmpty(accountId)) {
				if (CodeConstant.KARIAGE.equals(shatakuKbn) || CodeConstant.ITTOU.equals(shatakuKbn)
						|| CodeConstant.C001.equals(kariukeCompanyCd)) {
					accountId = ACCOUNT_ID_2;
				} else {
					accountId = ACCOUNT_ID_3;
				}
			}

		} else {
			if (shiyouryo == 0) {
				rtnMap.put(KEIJOU_KAMOKU_ACNT_CD_KEY, "");
				rtnMap.put(KEIJOU_KAMOKU_ACNT_NM_KEY, "");
				rtnMap.put(KEIJOU_KAMOKU_RTN_STS_KEY, RETURN_STATUS_OK);
				return rtnMap;

			} else {
				if (CodeConstant.MUTUAL_USE_KBN_AVAILABLE.equals(sougoRiyouKbn)) {
					if (!kariukeCompanyCd.equals(kyuyoCompanyCd)) {
						Skf1010MCompany compDt = skf1010MCompanyRepository.selectByPrimaryKey(kyuyoCompanyCd);
						if (compDt != null) {
							accountId = ACCOUNT_ID_4;
						}
					}

				} else {
					if (!manageCompanyCd.equals(kyuyoCompanyCd)) {
						Skf1010MCompany compDt = skf1010MCompanyRepository.selectByPrimaryKey(kyuyoCompanyCd);
						if (compDt != null) {
							accountId = ACCOUNT_ID_4;
						}
					}
				}
			}

			if (NfwStringUtils.isEmpty(accountId)) {
				if (CodeConstant.MUTUAL_USE_KBN_AVAILABLE.equals(sougoRiyouKbn)) {
					accountId = ACCOUNT_ID_4;
				} else {
					accountId = ACCOUNT_ID_5;
				}
			}
		}

		Skf3050MAccount kaikeiShatakuDt = skf3050MAccountRepository.selectByPrimaryKey(accountId);

		if (kaikeiShatakuDt != null) {
			rtnMap.put(KEIJOU_KAMOKU_ACNT_CD_KEY, kaikeiShatakuDt.getAccountCode());
			rtnMap.put(KEIJOU_KAMOKU_ACNT_NM_KEY, kaikeiShatakuDt.getAccountName());
			rtnMap.put(KEIJOU_KAMOKU_RTN_STS_KEY, RETURN_STATUS_OK);
			return rtnMap;

		} else {
			String errMsg = ERRMSG_KAIKEI_ACCOUNT_ID_1 + CodeConstant.COLON + accountId;
			rtnMap.put(KEIJOU_KAMOKU_ERR_MSG_KEY, errMsg);
			rtnMap.put(KEIJOU_KAMOKU_RTN_STS_KEY, RETURN_STATUS_NG);
			return rtnMap;
		}
	}

	/**
	 * 月別使用料履歴テーブル．勘定科目更新
	 * 
	 * @param shatakuAccountCd
	 *            社宅使用料計上勘定科目コード
	 * @param shatakuAccountName
	 *            社宅使用料計上勘定科目
	 * @param kyoeiAccountCd
	 *            共益費個人負担金計上勘定科目コード
	 * @param kyoeiAccountName
	 *            共益費個人負担金計上勘定科目
	 * @param updateUser
	 *            更新ユーザ
	 * @param shatakuKanriDaichouId
	 *            社宅管理台帳ID
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 更新件数
	 */
	private int updateTsukibetsuShiyoryoAccount(String shatakuAccountCd, String shatakuAccountName,
			String kyoeiAccountCd, String kyoeiAccountName, String updateUser, Long shatakuKanriDaichouId,
			String shoriNengetsu) {

		Skf3050Bt001UpdateTsukibetsuShiyoryoAccountExpParameter param = new Skf3050Bt001UpdateTsukibetsuShiyoryoAccountExpParameter();

		if (shatakuAccountCd == null) {
			param.setShatakuAccountCd(null);
		} else {
			param.setShatakuAccountCd(shatakuAccountCd);
		}

		if (shatakuAccountName == null) {
			param.setShatakuAccountName(null);
		} else {
			param.setShatakuAccountName(shatakuAccountName);
		}

		if (kyoeiAccountCd == null) {
			param.setKyoekiAccountCd(null);
		} else {
			param.setKyoekiAccountCd(kyoeiAccountCd);
		}

		if (kyoeiAccountName == null) {
			param.setKyoekiAccountName(null);
		} else {
			param.setKyoekiAccountName(kyoeiAccountName);
		}

		if (updateUser == null) {
			param.setUpdateUser(null);
		} else {
			param.setUpdateUser(updateUser);
		}

		param.setYearMonth(shoriNengetsu);
		param.setShatakuKanriId(shatakuKanriDaichouId);

		int updateCnt = skf3050Bt001UpdateTsukibetsuShiyoryoAccountExpRepository.updateTsukibetsuShiyoryoAccount(param);

		return updateCnt;
	}

	/**
	 * 月次処理管理テーブル（締め処理実行区分）更新
	 * 
	 * @param updateUser
	 *            更新ユーザ
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 更新件数
	 */
	private int updateGetsujiShoriKanriData(String updateUser, String shoriNengetsu) {

		Skf3050Bt001UpdateGetsujiShoriKanriDataExpParameter param = new Skf3050Bt001UpdateGetsujiShoriKanriDataExpParameter();

		if (updateUser == null) {
			param.setUpdateUser(null);
		} else {
			param.setUpdateUser(updateUser);
		}

		param.setCycleBillingYymm(shoriNengetsu);

		int updateCnt = skf3050Bt001UpdateGetsujiShoriKanriDataExpRepository.updateGetsujiShoriKanriData(param);

		return updateCnt;
	}

}
