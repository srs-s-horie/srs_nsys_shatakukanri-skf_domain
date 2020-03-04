/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3050.domain.service.skf3050sc002;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3050TMonthlyManageData;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.LoginUserInfoUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceOnBatchAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.nfw.webcore.domain.task.AsyncTaskHelper;
import jp.co.c_nexco.ptp.common.constants.CommonConstant;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3050.domain.dto.skf3050sc002.Skf3050Sc002ConfirmPositiveCooperationTaskExecutionAsyncDto;
import jp.co.intra_mart.foundation.asynchronous.TaskManager;
import jp.co.intra_mart.foundation.asynchronous.TaskMessage;

/**
 * Skf3050Sc002ConfirmPositiveCooperationTaskExecutionAsyncService 月次運用管理画面のPOSITIVE連携データ確定処理
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3050Sc002ConfirmPositiveCooperationTaskExecutionAsyncService extends AsyncBaseServiceOnBatchAbstract<Skf3050Sc002ConfirmPositiveCooperationTaskExecutionAsyncDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3050Sc002SharedService skf3050Sc002SharedService;
	
	@Value("${skf3050.skf3050_bt004.batch_prg_id}")
	private String batchPrgId;
	
	@Override
	protected AsyncBaseDto index(Skf3050Sc002ConfirmPositiveCooperationTaskExecutionAsyncDto confirmPositiveCoopDto) throws Exception {
		
		skfOperationLogUtils.setAccessLog("POSITIVE連携データ確定処理", CodeConstant.C001, "Skf3050Sc002");
		
		confirmPositiveCoopDto.setResultMessages(null);
		
		String jikkouShijiYoteiNengetsu = confirmPositiveCoopDto.getHdnJikkouShijiYoteiNengetsu();
		
		//▼連携データ確定処理起動事前チェック
		String errMsg = checkBeforeStartup(jikkouShijiYoteiNengetsu);
		if (!"".equals(errMsg)) {
			ServiceHelper.addErrorResultMessage(confirmPositiveCoopDto, null, MessageIdConstant.E_SKF_3026, errMsg);
			throwBusinessExceptionIfErrors(confirmPositiveCoopDto.getResultMessages());
		}
		
		//HR連携データ確定処理バッチを起動
		Map<String, Object> param = new HashMap<>();

		// タスク設定ユーザー
		param.put(AsyncTaskHelper.REGISTE_USER_CD, LoginUserInfoUtils.getUserCd());

		Map<String, String> dataMap = new HashMap<>();

		dataMap.put("confirmPostvCoopBatchPrgId", batchPrgId);
		dataMap.put("confirmPostvCoopCompanyCd", CodeConstant.C001);
		dataMap.put("confirmPostvCoopUserId", skf3050Sc002SharedService.getUserId());
		dataMap.put("confirmPostvCoopShoriNengetsu", jikkouShijiYoteiNengetsu);

		param.put("parameter", dataMap);

		// タスクキューの登録
		String queueId = "jp.co.c_nexco.skf.skf3050.domain.service.skf3050sc002.Skf3050Sc002ConfirmPositiveCooperationTaskExecutionAsyncService";
		TaskManager.addSerializedTaskQueue(queueId, true);

		String taskClassName = "jp.co.c_nexco.skf.skf3050.domain.task.Skf3050Bt004.Skf3050Bt004AsyncConfirmPositiveCooperationTask";
		TaskMessage taskMsg = addSerializedTaskWithInfo(confirmPositiveCoopDto, queueId, taskClassName, param,
				CommonConstant.C_SYSTEM_ID.toUpperCase(), "Skf3050bt004", "POSITIVE連携データ確定処理制御", "POSITIVE連携データ確定処理");

		String msgId = "";
		if (taskMsg != null) {
			msgId = msgId + taskMsg.getMessageId();
			LogUtils.debugByMsg("タスク処理メッセージID：" + taskMsg.getMessageId());
		}

		confirmPositiveCoopDto.setTaskMsgId(msgId);
		
		//▼処理年月が3月の場合、対象年度リストも更新する
		//⇒非同期バッチのため、実装不可
		
		return confirmPositiveCoopDto;
	}
	
	/**
	 * POSITIVE連携データ確定処理前のチェック
	 * 
	 * @param jikkouShijiYoteiNengetsu 実行指示予定年月
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
		
		//▼連携データ確定処理可能チェック
		Skf3050TMonthlyManageData renkeiKbnData = skf3050Sc002SharedService.getShimePositiveRenkeiKbn(jikkouShijiYoteiNengetsu);
		if (renkeiKbnData == null) {
			//月次処理管理データが取得できない場合エラー
			errMsg = Skf3050Sc002SharedService.ERRMSG_SHIME_IMPOSSIBLE;
			return errMsg;
		
		} else {
			if (NfwStringUtils.isEmpty(renkeiKbnData.getBillingActKbn()) || 
				!CodeConstant.LINKDATA_CREATE_KBN_JIKKO_SUMI.equals(renkeiKbnData.getLinkdataCreateKbn()) || 
				CodeConstant.LINKDATA_COMMIT_KBN_JIKKO_SUMI.equals(renkeiKbnData.getLinkdataCommitKbn())) {
				//締め処理＝Null、またはHR連携データ作成≠実行済、またはHR連携データ確定実行区分＝実行済の場合エラー
				errMsg = Skf3050Sc002SharedService.ERRMSG_SHIME_IMPOSSIBLE;
				return errMsg;
			}
		}
		
		return errMsg;
	}
	
	

}
