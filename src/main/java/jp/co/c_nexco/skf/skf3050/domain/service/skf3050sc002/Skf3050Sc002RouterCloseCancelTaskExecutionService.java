/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3050.domain.service.skf3050sc002;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt006.Skf3050Bt006UpdateGetsujiShoriKanriPositiveMiSakuseiExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt006.Skf3050Bt006UpdateGetsujiShoriKanriPositiveSakuseiZumiExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3050TMonthlyManageData;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt005.Skf3050Bt005GetDataForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt006.Skf3050Bt006GetPositiveRenkeiSakuseiJikkouKbnExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt006.Skf3050Bt006UpdateGetsujiShoriKanriPositiveMiSakuseiExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt006.Skf3050Bt006UpdateGetsujiShoriKanriPositiveSakuseiZumiExpRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3050.domain.dto.skf3050sc002.Skf3050Sc002RouterCloseCancelTaskExecutionDto;

/**
 * Skf3050Sc002CloseCancelTaskExecutionService 月次運用管理画面の締め解除処理
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3050Sc002RouterCloseCancelTaskExecutionService extends SkfServiceAbstract<Skf3050Sc002RouterCloseCancelTaskExecutionDto> {
	

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3050Sc002SharedService skf3050Sc002SharedService;
	@Autowired
	private Skf3050Bt005GetDataForUpdateExpRepository skf3050Bt005GetDataForUpdateExpRepository;
	@Autowired
	private Skf3050Bt006GetPositiveRenkeiSakuseiJikkouKbnExpRepository skf3050Bt006GetPositiveRenkeiSakuseiJikkouKbnExpRepository;
	@Autowired
	private Skf3050Bt006UpdateGetsujiShoriKanriPositiveSakuseiZumiExpRepository skf3050Bt006UpdateGetsujiShoriKanriPositiveSakuseiZumiExpRepository;
	@Autowired
	private Skf3050Bt006UpdateGetsujiShoriKanriPositiveMiSakuseiExpRepository skf3050Bt006UpdateGetsujiShoriKanriPositiveMiSakuseiExpRepository;

	@Value("${skf3050.skf3050_bt006.batch_prg_id}")
	private String batchPrgId;
	private static final String UPDATE_GETSUJI_DATA_RESULT_KEY = "updateGetsujiDataResult";
	private static final String UPDATE_GETSUJI_DATA_MSG_KEY = "updateGetsujiDatamsg";

	private static final String BATCH_NAME = "モバイルルーター締め解除処理";
	private static final String PARAM_1_POSITIVERENKEI = "POSITIVE連携";
	private static final String PARAM_1_SHIME = "締め処理";

	@Override
	protected BaseDto index(Skf3050Sc002RouterCloseCancelTaskExecutionDto closeCancelDto) throws Exception {

		skfOperationLogUtils.setAccessLog("モバイルルーター締め解除処理", CodeConstant.C001, FunctionIdConstant.SKF3050_SC002);

		closeCancelDto.setResultMessages(null);

		String jikkouShijiYoteiNengetsu = closeCancelDto.getHdnJikkouShijiYoteiNengetsu();

		//▼締め解除処理起動事前チェック
		String errMsg = checkBeforeStartup(jikkouShijiYoteiNengetsu);
		if (!"".equals(errMsg)) {
			ServiceHelper.addErrorResultMessage(closeCancelDto, null, MessageIdConstant.E_SKF_3025, errMsg);
			throwBusinessExceptionIfErrors(closeCancelDto.getResultMessages());
		}

		Map<String, String> dataMap = new HashMap<>();
		dataMap.put(Skf3050Sc002SharedService.BATCH_PRG_ID_KEY, batchPrgId);
		dataMap.put(Skf3050Sc002SharedService.COMPANY_CD_KEY, CodeConstant.C001);
		dataMap.put(Skf3050Sc002SharedService.USER_ID_KEY, skf3050Sc002SharedService.getUserId());
		dataMap.put(Skf3050Sc002SharedService.SHORI_NENGETSU_KEY, jikkouShijiYoteiNengetsu);

		Date sysDate = skf3050Sc002SharedService.getSystemDate();
		List<String> endList = skf3050Sc002SharedService.setEndListRouter();
		//トランザクションAを開始
		LogUtils.info(MessageIdConstant.I_SKF_1022, BATCH_NAME);
		int registResult = skf3050Sc002SharedService.registBatchControl(dataMap, sysDate, endList);

		if (Skf3050Sc002SharedService.RETURN_STATUS_NG == registResult) {
			ServiceHelper.addErrorResultMessage(closeCancelDto, null, MessageIdConstant.E_SKF_3025, "バッチ制御テーブル更新に失敗したため");
			return closeCancelDto;
		}
		//トランザクションBの開始
		String endFlag = SkfCommonConstant.ABNORMAL;
		String updateGetsujiDataMsg = "";
		try {
//			boolean canLock = canLockTableData(jikkouShijiYoteiNengetsu);
//	
//			if (canLock) {
				Map<String, String> result = skf3050Sc002SharedService.updateCancelGetsujiData(dataMap);
				endFlag = result.get(UPDATE_GETSUJI_DATA_RESULT_KEY);
				updateGetsujiDataMsg = result.get(UPDATE_GETSUJI_DATA_MSG_KEY);
				LogUtils.debugByMsg("トランザクションB終了:" + BATCH_NAME + "(" + updateGetsujiDataMsg + ")");
//			} else {
//				LogUtils.info(MessageIdConstant.E_SKF_1079, "");
//			}
		} catch (Exception e) {
			LogUtils.infoByMsg("異常終了:" + BATCH_NAME + "(" + e.getMessage() + ")");
		}

		//トランザクションCの開始
		//終了処理
		skf3050Sc002SharedService.endProc(endFlag,dataMap.get(
				Skf3050Sc002SharedService.COMPANY_CD_KEY), batchPrgId,SkfCommonConstant.PROCESSING);

		skf3050Sc002SharedService.outputEndProcLog(BATCH_NAME);

		if (SkfCommonConstant.ABNORMAL.equals(endFlag)) {
			ServiceHelper.addErrorResultMessage(closeCancelDto, null, MessageIdConstant.E_SKF_1079);
		}
		LogUtils.info(MessageIdConstant.I_SKF_1023, BATCH_NAME);
		// 画面リフレッシュ
		TransferPageInfo nextPage = TransferPageInfo.prevPage(FunctionIdConstant.SKF3050_SC002, "init");
		closeCancelDto.setTransferPageInfo(nextPage);

		return closeCancelDto;
	}

	/**
	 * 締め解除処理前のチェック
	 * 
	 * @param jikkouShijiYoteiNengetsu
	 *            実行指示予定年月
	 * @return エラーメッセージ
	 */
	private String checkBeforeStartup(String jikkouShijiYoteiNengetsu) {

		String errMsg = "";

		//▼二重起動チェック
		boolean notDoubleStartup = skf3050Sc002SharedService.checkDoubleStartup();
		if (!notDoubleStartup) {
			errMsg = Skf3050Sc002SharedService.ERRMSG_DOUBLE_START;
			return errMsg;
		}

		//▼締め解除処理可能チェック
		Skf3050TMonthlyManageData renkeiKbnData = skf3050Sc002SharedService
				.getShimePositiveRenkeiKbn(jikkouShijiYoteiNengetsu);
		if (renkeiKbnData == null) {
			//月次処理管理データが取得できない場合エラー
			errMsg = Skf3050Sc002SharedService.ERRMSG_SHIME_IMPOSSIBLE;
			return errMsg;

		} else {
			if (NfwStringUtils.isEmpty(renkeiKbnData.getMobileRouterBillingActKbn())
					|| CodeConstant.LINKDATA_CREATE_KBN_JIKKO_SUMI.equals(renkeiKbnData.getLinkdataCommitKbn())) {
				//モバイルルーター締め処理＝Null、またはHR連携データ確定実行区分＝実行済の場合エラー
				errMsg = Skf3050Sc002SharedService.ERRMSG_SHIME_IMPOSSIBLE;
				return errMsg;
			}
		}

		return errMsg;
	}

	/**
	 * 対象のテーブルのロック制御
	 * 
	 * @param key
	 *            主キー
	 * @return 結果
	 */
	public boolean canLockTableData(String key) {

		List<String> shatakuRentalRirekiData = skf3050Bt005GetDataForUpdateExpRepository
				.getSkf2100TMobileRouterRentalRirekiData(key);
		if (shatakuRentalRirekiData.size() == 0) {
			return false;
		}

		List<String> monthlyManageData = skf3050Bt005GetDataForUpdateExpRepository
				.getSkf3050TMonthlyManageData(key);
		if (monthlyManageData.size() == 0) {
			return false;
		}

		return true;
	}

	/**
	 * 月次処理月管理データを更新する。
	 * 
	 * @param parameter
	 *            パラメータ
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Map<String, String> updateCancelGetsujiData(Map<String, String> parameter) {

		Map<String, String> rtnMap = new HashMap<String, String>();

		String paramUserId = parameter.get(Skf3050Sc002SharedService.USER_ID_KEY);
		String paramShoriNengetsu = parameter.get(Skf3050Sc002SharedService.SHORI_NENGETSU_KEY);
		

		//▼連携作成区分取得▼
		int updateCnt = 0;
		String hrRenkeiSakuseiKbn = getHrRenkeiSakuseiJikkouKbn(paramShoriNengetsu);

		if (CodeConstant.LINKDATA_CREATE_KBN_JIKKO_SUMI.equals(hrRenkeiSakuseiKbn)) {
			//▼連携作成区分＝実行済の場合▼
			updateCnt = updateGetsujiShoriKanriHrSakuseiZumi(paramUserId, paramShoriNengetsu);
			rtnMap.put(UPDATE_GETSUJI_DATA_MSG_KEY, PARAM_1_POSITIVERENKEI);

		} else if (CodeConstant.LINKDATA_CREATE_KBN_MI_JIKKO.equals(hrRenkeiSakuseiKbn)
				|| CodeConstant.LINKDATA_CREATE_KBN_KAIJO_CHU.equals(hrRenkeiSakuseiKbn)) {
			//▼連携作成区分＝未行済または解除中の場合▼
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
	 * POSITIVE連携作成実行区分
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @return POSITIVE連携作成実行区分
	 */
	private String getHrRenkeiSakuseiJikkouKbn(String shoriNengetsu) {

		String rtnVal = skf3050Bt006GetPositiveRenkeiSakuseiJikkouKbnExpRepository
				.getPositiveRenkeiSakuseiJikkouKbn(shoriNengetsu);

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

		Skf3050Bt006UpdateGetsujiShoriKanriPositiveSakuseiZumiExpParameter param = new Skf3050Bt006UpdateGetsujiShoriKanriPositiveSakuseiZumiExpParameter();

		if (updateUser == null) {
			param.setUpdateUserId(null);
		} else {
			param.setUpdateUserId(updateUser);
		}

		param.setCycleBillingYymm(shoriNengetsu);
		param.setUpdateProgramId(batchPrgId);

		Integer updateCnt = skf3050Bt006UpdateGetsujiShoriKanriPositiveSakuseiZumiExpRepository
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

		Skf3050Bt006UpdateGetsujiShoriKanriPositiveMiSakuseiExpParameter param = new Skf3050Bt006UpdateGetsujiShoriKanriPositiveMiSakuseiExpParameter();

		if (updateUser == null) {
			param.setUpdateUserId(null);
		} else {
			param.setUpdateUserId(updateUser);
		}

		param.setCycleBillingYymm(shoriNengetsu);
		param.setUpdateProgramId(batchPrgId);

		Integer updateCnt = skf3050Bt006UpdateGetsujiShoriKanriPositiveMiSakuseiExpRepository
				.updateGetsujiShoriKanriPositiveMiSakusei(param);

		return updateCnt;
	}
}
