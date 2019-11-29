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
import jp.co.c_nexco.skf.skf3050.domain.dto.skf3050sc002.Skf3050Sc002CloseCancelTaskExecutionAsyncDto;
import jp.co.intra_mart.foundation.asynchronous.TaskManager;
import jp.co.intra_mart.foundation.asynchronous.TaskMessage;

/**
 * Skf3050Sc002CloseCancelTaskExecutionAsyncService 月次運用管理画面の締め解除処理
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3050Sc002CloseCancelTaskExecutionAsyncService
		extends AsyncBaseServiceOnBatchAbstract<Skf3050Sc002CloseCancelTaskExecutionAsyncDto> {
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3050Sc002SharedService skf3050Sc002SharedService;

	private static final String BATCH_PRG_ID_KEY = "closeCancelBatchPrgId";
	private static final String COMPANY_CD_KEY = "closeCancelCompanyCd";
	private static final String USER_ID_KEY = "closeCancelUserId";
	private static final String SHORI_NENGETSU_KEY = "closeCancelShoriNengetsu";
	
	@Value("${skf3050.skf3050_bt002.batch_prg_id}")
	private String batchPrgId;

	@Override
	protected AsyncBaseDto index(Skf3050Sc002CloseCancelTaskExecutionAsyncDto closeCancelDto) throws Exception {

		skfOperationLogUtils.setAccessLog("締め解除処理開始", CodeConstant.C001, "Skf3050Sc002");

		closeCancelDto.setResultMessages(null);

		String jikkouShijiYoteiNengetsu = closeCancelDto.getHdnJikkouShijiYoteiNengetsu();

		String errMsg = checkBeforeStartup(jikkouShijiYoteiNengetsu);
		if (!"".equals(errMsg)) {
			ServiceHelper.addErrorResultMessage(closeCancelDto, null, MessageIdConstant.E_SKF_3025, errMsg);
			throwBusinessExceptionIfErrors(closeCancelDto.getResultMessages());
		}

		Map<String, Object> param = new HashMap<>();

		// タスク設定ユーザー
		param.put(AsyncTaskHelper.REGISTE_USER_CD, LoginUserInfoUtils.getUserCd());

		Map<String, String> dataMap = new HashMap<>();

		dataMap.put(BATCH_PRG_ID_KEY, batchPrgId);
		dataMap.put(COMPANY_CD_KEY, CodeConstant.C001);
		dataMap.put(USER_ID_KEY, skf3050Sc002SharedService.getUserId());
		dataMap.put(SHORI_NENGETSU_KEY, jikkouShijiYoteiNengetsu);

		param.put("parameter", dataMap);

		// タスクキューの登録
		String queueId = "jp.co.c_nexco.skf.skf3050.domain.service.skf3050sc002.Skf3050Sc002CloseCancelTaskExecutionAsyncService";
		TaskManager.addSerializedTaskQueue(queueId, true);

		String taskClassName = "jp.co.c_nexco.skf.skf3050.domain.task.skf3050bt002.Skf3050Bt002AsyncCloseCancelTask";
		TaskMessage taskMsg = addSerializedTaskWithInfo(closeCancelDto, queueId, taskClassName, param,
				CommonConstant.C_SYSTEM_ID.toUpperCase(), "Skf3050bt002", "締め解除処理制御", "締め解除処理");

		String msgId = "";
		if (taskMsg != null) {
			msgId = msgId + taskMsg.getMessageId();
			LogUtils.debugByMsg("タスク処理メッセージID：" + taskMsg.getMessageId());
		}

		closeCancelDto.setTaskMsgId(msgId);

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

		boolean notDoubleStartup = skf3050Sc002SharedService.checkDoubleStartup();
		if (!notDoubleStartup) {
			errMsg = Skf3050Sc002SharedService.ERRMSG_DOUBLE_START;
			return errMsg;
		}

		Skf3050TMonthlyManageData renkeiKbnData = skf3050Sc002SharedService
				.getShimePositiveRenkeiKbn(jikkouShijiYoteiNengetsu);
		if (renkeiKbnData == null) {
			errMsg = Skf3050Sc002SharedService.ERRMSG_SHIME_IMPOSSIBLE;
			return errMsg;

		} else {
			if (NfwStringUtils.isEmpty(renkeiKbnData.getBillingActKbn())
					|| CodeConstant.LINKDATA_CREATE_KBN_JIKKO_SUMI.equals(renkeiKbnData.getLinkdataCommitKbn())) {
				errMsg = Skf3050Sc002SharedService.ERRMSG_SHIME_IMPOSSIBLE;
				return errMsg;
			}
		}

		return errMsg;
	}

}
