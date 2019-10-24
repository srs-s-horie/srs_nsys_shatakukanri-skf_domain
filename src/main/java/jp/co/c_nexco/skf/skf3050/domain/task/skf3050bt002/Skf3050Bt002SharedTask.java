/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3050.domain.task.skf3050bt002;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.icu.text.SimpleDateFormat;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateBihinGoukeiExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateGenbutsuSanteigakuExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt002.Skf3050Bt002UpdateGetsujiShoriKanriPositiveMiSakuseiExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt002.Skf3050Bt002UpdateGetsujiShoriKanriPositiveSakuseiZumiExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateBihinGoukeiExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateGenbutsuSanteigakuExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt002.Skf3050Bt002GetPositiveRenkeiSakuseiJikkouKbnExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt002.Skf3050Bt002UpdateGetsujiShoriKanriPositiveMiSakuseiExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt002.Skf3050Bt002UpdateGetsujiShoriKanriPositiveSakuseiZumiExpRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.SkfBatchBusinessLogicUtils;

/**
 * Skf3050Bt002SharedTask 締め解除処理（オンラインバッチ）共通クラス
 *
 * @author NEXCOシステムズ
 */
@Scope("prototype")
@Component
public class Skf3050Bt002SharedTask {

	@Autowired
	private SkfBatchBusinessLogicUtils skfBatchBusinessLogicUtils;
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	@Autowired
	private Skf3050Bt001UpdateGenbutsuSanteigakuExpRepository skf3050Bt001UpdateGenbutsuSanteigakuExpRepository;
	@Autowired
	private Skf3050Bt001UpdateBihinGoukeiExpRepository skf3050Bt001UpdateBihinGoukeiExpRepository;
	@Autowired
	private Skf3050Bt002GetPositiveRenkeiSakuseiJikkouKbnExpRepository skf3050Bt002GetHrRenkeiSakuseiJikkouKbnExpRepository;
	@Autowired
	private Skf3050Bt002UpdateGetsujiShoriKanriPositiveSakuseiZumiExpRepository skf3050Bt002UpdateGetsujiShoriKanriHrSakuseiZumiExpRepository;
	@Autowired
	private Skf3050Bt002UpdateGetsujiShoriKanriPositiveMiSakuseiExpRepository skf3050Bt002UpdateGetsujiShoriKanriHrMiSakuseiExpRepository;

	public static final String SKF3050BT002_BATCH_PRG_ID_KEY = "closeCancelBatchPrgId";
	public static final String SKF3050BT002_COMPANY_CD_KEY = "closeCancelCompanyCd";
	public static final String SKF3050BT002_USER_ID_KEY = "closeCancelUserId";
	public static final String SKF3050BT002_SHORI_NENGETSU_KEY = "closeCancelShoriNengetsu";
	public static final String UPDATE_GETSUJI_DATA_RESULT_KEY = "updateGetsujiDataResult";
	public static final String UPDATE_GETSUJI_DATA_MSG_KEY = "updateGetsujiDatamsg";

	public static final String BATCH_ID_B5002 = "B5002";
	public static final int PARAMETER_NUM = 4;
	public static final String BATCH_NAME = "締め解除処理";

	private static final String PARAM_NAME_PRG_ID = "バッチプログラムID";
	private static final String PARAM_NAME_COMPANY_CD = "会社コード";
	private static final String PARAM_NAME_USER_ID = "ユーザID";
	private static final String PARAM_NAME_SHORI_NENGETSU = "処理年月";
	private static final String I_SKF_3107_PARAM_0 = "月次処理管理";
	private static final String PARAM_1_POSITIVERENKEI = "POSITIVE連携";
	private static final String PARAM_1_SHIME = "締め処理";

	/**
	 * バッチ制御テーブルへ登録する。
	 * 
	 * @param parameter
	 *            パラメータ
	 * @return 結果
	 * @throws ParseException
	 */
	@Transactional
	public int registBatchControl(Map<String, String> parameter) throws ParseException {

		String retParameterName = checkParameter(parameter);
		String programId = BATCH_ID_B5002;
		Date sysDate = getSystemDate();

		if (!NfwStringUtils.isEmpty(retParameterName)) {

			if (!retParameterName.contains(PARAM_NAME_COMPANY_CD)) {
				skfBatchBusinessLogicUtils.insertBatchControl(parameter.get(SKF3050BT002_COMPANY_CD_KEY),
						programId, parameter.get(SKF3050BT002_USER_ID_KEY), SkfCommonConstant.ABNORMAL, sysDate,
						getSystemDate());

				LogUtils.error(MessageIdConstant.E_SKF_1089, retParameterName);
				return CodeConstant.SYS_NG;

			} else {
				LogUtils.error(MessageIdConstant.E_SKF_1089, PARAM_NAME_COMPANY_CD);
				return CodeConstant.SYS_NG;
			}
		}

		if (!BATCH_ID_B5002.equals(parameter.get(SKF3050BT002_BATCH_PRG_ID_KEY))) {

			skfBatchBusinessLogicUtils.insertBatchControl(parameter.get(SKF3050BT002_COMPANY_CD_KEY),
					programId, parameter.get(SKF3050BT002_USER_ID_KEY), SkfCommonConstant.ABNORMAL, sysDate,
					getSystemDate());

			LogUtils.errorByMsg("バッチプログラムIDが正しくありません。（バッチプログラムID：" + parameter.get(SKF3050BT002_BATCH_PRG_ID_KEY) + "）");
			return CodeConstant.SYS_NG;
		}

		int retStat = skfBatchBusinessLogicUtils.insertBatchControl(parameter.get(SKF3050BT002_COMPANY_CD_KEY),
				programId, parameter.get(SKF3050BT002_USER_ID_KEY), SkfCommonConstant.PROCESSING, sysDate, null);

		if (retStat < 0) {
			return CodeConstant.SYS_NG;
		}

		return CodeConstant.SYS_OK;
	}

	/**
	 * 月次処理月管理データを更新する。
	 * 
	 * @param parameter
	 *            パラメータ
	 */
	@Transactional
	public Map<String, String> updateGetsujiData(Map<String, String> parameter) {
		
		Map<String, String> rtnMap = new HashMap<String, String>();

		String paramUserId = parameter.get(SKF3050BT002_USER_ID_KEY);
		String paramShoriNengetsu = parameter.get(SKF3050BT002_SHORI_NENGETSU_KEY);

		updateGenbutsuSanteigaku(paramUserId, paramShoriNengetsu);

		updateBihinGoukei(paramUserId, paramShoriNengetsu);

		int updateCnt = 0;
		String hrRenkeiSakuseiKbn = getHrRenkeiSakuseiJikkouKbn(paramShoriNengetsu);

		if (CodeConstant.LINKDATA_CREATE_KBN_JIKKO_SUMI.equals(hrRenkeiSakuseiKbn)) {
			updateCnt = updateGetsujiShoriKanriHrSakuseiZumi(paramUserId, paramShoriNengetsu);
			rtnMap.put(UPDATE_GETSUJI_DATA_MSG_KEY, PARAM_1_POSITIVERENKEI);	

		} else if (CodeConstant.LINKDATA_CREATE_KBN_MI_JIKKO.equals(hrRenkeiSakuseiKbn)
				|| CodeConstant.LINKDATA_CREATE_KBN_KAIJO_CHU.equals(hrRenkeiSakuseiKbn)) {
			updateCnt = updateGetsujiShoriKanriHrMiSakusei(paramUserId, paramShoriNengetsu);
			rtnMap.put(UPDATE_GETSUJI_DATA_MSG_KEY, PARAM_1_SHIME);
		}

		if (updateCnt > 0) {
			rtnMap.put(UPDATE_GETSUJI_DATA_RESULT_KEY, SkfCommonConstant.COMPLETE);	
		} else {
			rtnMap.put(UPDATE_GETSUJI_DATA_RESULT_KEY, SkfCommonConstant.ABNORMAL);
		}

		return rtnMap;
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

		Date endDate = getSystemDate();

		int updateCnt = skfBatchBusinessLogicUtils.updateBatchControl(endDate, endFlag, companyCd, programId,
				searchEndFlag);

		if (updateCnt > 0) {
			return CodeConstant.SYS_OK;
		}

		return CodeConstant.SYS_NG;
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

		if (isEmpty(parameter.get(SKF3050BT002_BATCH_PRG_ID_KEY))) {
			retParameterName = PARAM_NAME_PRG_ID;
		}

		if (isEmpty(parameter.get(SKF3050BT002_COMPANY_CD_KEY))) {
			retParameterName = CodeConstant.COMMA + PARAM_NAME_COMPANY_CD;
		}

		if (isEmpty(parameter.get(SKF3050BT002_USER_ID_KEY))) {
			retParameterName = CodeConstant.COMMA + PARAM_NAME_USER_ID;
		}

		if (isEmpty(parameter.get(SKF3050BT002_SHORI_NENGETSU_KEY))) {
			retParameterName = CodeConstant.COMMA + PARAM_NAME_SHORI_NENGETSU;
		}

		if (retParameterName.startsWith(CodeConstant.COMMA)) {
			retParameterName = retParameterName.substring(1);
		}

		return retParameterName;
	}
	
	/**
	 * パラメータが空であるかチェックする。
	 * 
	 * @param inVal
	 *            チェックする値
	 * @return 結果
	 */
	public boolean isEmpty(String inVal) {

		if (NfwStringUtils.isEmpty(inVal)) {
			return true;
		}

		String param = inVal.replaceFirst("^[\\h]+", "").replaceFirst("[\\h]+$", "");

		if (NfwStringUtils.isEmpty(param)) {
			return true;
		}

		return false;
	}
	
	/**
	 * システムデータを取得する。
	 * 
	 * @return システムデータ
	 */
	public Date getSystemDate() {

		Date systemDate = skfBaseBusinessLogicUtils.getSystemDateTime();

		return systemDate;
	}

	/**
	 * 処理開始ログの出力
	 * 
	 * @param shoriYymm
	 *            処理年月
	 * @return ヘッダーログ
	 */
	public void outputStartLog(String shoriYymm) {
		
		LogUtils.info(MessageIdConstant.I_SKF_1022, Skf3050Bt002SharedTask.BATCH_NAME);

		String log_1 = "プログラム名：" + BATCH_NAME;
		LogUtils.infoByMsg(log_1);

		String shoriNengetsu = CodeConstant.HYPHEN;

		if (!NfwStringUtils.isEmpty(shoriYymm)) {
			shoriNengetsu = shoriYymm;
		}

		String log_2 = "対象年月：" + shoriNengetsu;
		LogUtils.infoByMsg(log_2);

		Date sysDate = getSystemDate();
		String strSysDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(sysDate);
		String log_3 = "処理開始時間 " + strSysDate;
		LogUtils.infoByMsg(log_3);
	}

	/**
	 * 処理終了ログ
	 * 
	 * @param msg
	 * 			出力する置き換えメッセージ
	 */
	public void outputEndProcLog(String msg) {

		Date sysDate = getSystemDate();
		String strSysDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(sysDate);

		LogUtils.infoByMsg("処理終了時間：" + strSysDate);
		LogUtils.info(MessageIdConstant.I_SKF_3107, I_SKF_3107_PARAM_0, msg);
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

		if (updateUser == null) {
			param.setUpdateUserId(null);
		} else {
			param.setUpdateUserId(updateUser);
		}

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

		if (updateUser == null) {
			param.setUpdateUserId(null);
		} else {
			param.setUpdateUserId(updateUser);
		}

		param.setYearMonth(shoriNengetsu);

		skf3050Bt001UpdateBihinGoukeiExpRepository.updateBihinGoukei(param);
	}

	/**
	 * POSITIVE連携作成実行区分
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @return POSITIVE連携作成実行区分
	 */
	private String getHrRenkeiSakuseiJikkouKbn(String shoriNengetsu) {

		String rtnVal = skf3050Bt002GetHrRenkeiSakuseiJikkouKbnExpRepository.getPositiveRenkeiSakuseiJikkouKbn(shoriNengetsu);

		if (NfwStringUtils.isEmpty(rtnVal)) {
			rtnVal = "";
		}

		return rtnVal;
	}

	/**
	 * 月次処理管理テーブル更新（POSITIVE連携作成済）
	 * 
	 * @param updateUser
	 *            更新ユーザ
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 更新件数
	 */
	private Integer updateGetsujiShoriKanriHrSakuseiZumi(String updateUser, String shoriNengetsu) {

		Skf3050Bt002UpdateGetsujiShoriKanriPositiveSakuseiZumiExpParameter param = new Skf3050Bt002UpdateGetsujiShoriKanriPositiveSakuseiZumiExpParameter();

		if (updateUser == null) {
			param.setUpdateUserId(null);
		} else {
			param.setUpdateUserId(updateUser);
		}

		param.setCycleBillingYymm(shoriNengetsu);

		Integer updateCnt = skf3050Bt002UpdateGetsujiShoriKanriHrSakuseiZumiExpRepository
				.updateGetsujiShoriKanriPositiveSakuseiZumi(param);

		return updateCnt;
	}

	/**
	 * 月次処理管理テーブル更新（POSITIVE連携未作成）
	 * 
	 * @param updateUser
	 *            更新ユーザ
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 更新件数
	 */
	private Integer updateGetsujiShoriKanriHrMiSakusei(String updateUser, String shoriNengetsu) {

		Skf3050Bt002UpdateGetsujiShoriKanriPositiveMiSakuseiExpParameter param = new Skf3050Bt002UpdateGetsujiShoriKanriPositiveMiSakuseiExpParameter();

		if (updateUser == null) {
			param.setUpdateUserId(null);
		} else {
			param.setUpdateUserId(updateUser);
		}

		param.setCycleBillingYymm(shoriNengetsu);

		Integer updateCnt = skf3050Bt002UpdateGetsujiShoriKanriHrMiSakuseiExpRepository
				.updateGetsujiShoriKanriPositiveMiSakusei(param);

		return updateCnt;
	}

}
