/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3050.domain.service.skf3050sc002;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Sc002.Skf3050Sc002GetBihinHenkyakuDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Sc002.Skf3050Sc002GetBihinTaiyoDataExp;
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
import jp.co.c_nexco.skf.skf3050.domain.dto.skf3050sc002.Skf3050Sc002CloseTaskExecutionAsyncDto;
import jp.co.intra_mart.foundation.asynchronous.TaskManager;
import jp.co.intra_mart.foundation.asynchronous.TaskMessage;

/**
 * Skf3050Sc002CloseTaskExecutionAsyncService 月次運用管理画面の締め処理
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3050Sc002CloseTaskExecutionAsyncService
		extends AsyncBaseServiceOnBatchAbstract<Skf3050Sc002CloseTaskExecutionAsyncDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3050Sc002SharedService skf3050Sc002SharedService;

	private static final String ERRMSG_SHIME_BIHIN_NYUKYO = "入居情報について備品貸与日が設定されていません。";
	private static final String ERRMSG_SHIME_BIHIN_TAIKYO = "退居情報について備品返却日が設定されていません。備品返却日に退居日を設定し、";

	private static final String BATCH_PRG_ID = "closeBatchPrgId";
	private static final String COMPANY_CD = "closeCompanyCd";
	private static final String USER_ID = "closeUserId";
	private static final String SHORI_NENGETSU = "closeShoriNengetsu";
	private static final String SHIME_SHORI_FLG = "shimeShoriFlg";

	private static final String FLG_ON = "1";
	
	@Value("${skf3050.skf3050_bt001.batch_prg_id}")
	private String batchPrgId;

	@Override
	protected AsyncBaseDto index(Skf3050Sc002CloseTaskExecutionAsyncDto closeTaskDto) throws Exception {

		skfOperationLogUtils.setAccessLog("締め処理", CodeConstant.C001, "Skf3050Sc002");

		String jikkouShijiYoteiNengetsu = closeTaskDto.getHdnJikkouShijiYoteiNengetsu();
		String bihinTaiyoWarnContinueFlg = closeTaskDto.getHdnBihinTaiyoWarnContinueFlg();
		String bihinHenkyakuWarnContinueFlg = closeTaskDto.getHdnBihinHenkyakuWarnContinueFlg();

		closeTaskDto.setResultMessages(null);

		if (!FLG_ON.equals(bihinTaiyoWarnContinueFlg) && !FLG_ON.equals(bihinHenkyakuWarnContinueFlg)) {
			//▼締め処理起動事前チェック
			String errMsg = skf3050Sc002SharedService.checkShimeShori(jikkouShijiYoteiNengetsu,
					Skf3050Sc002SharedService.SHIME_SHORI_ON);

			if (!"".equals(errMsg)) {
				//締め処理事前チェックが異常の場合、エラーメッセージを表示し処理を中断する
				ServiceHelper.addErrorResultMessage(closeTaskDto, null, MessageIdConstant.E_SKF_3013, errMsg);
				throwBusinessExceptionIfErrors(closeTaskDto.getResultMessages());
			}
		}

		closeTaskDto.setHdnWarnMsg("");

		if (!FLG_ON.equals(bihinTaiyoWarnContinueFlg)) {
			//▼入居情報について備品貸与日の設定確認
			String bihinTaiyoWarningMsg = checkBihinTaiyoData(jikkouShijiYoteiNengetsu);

			if (!"".equals(bihinTaiyoWarningMsg)) {
				//備品の貸与日が設定されていない場合、警告メッセージを表示
				String msg = skf3050Sc002SharedService.editMsg(MessageIdConstant.I_SKF_3008, bihinTaiyoWarningMsg);
				closeTaskDto.setHdnWarnMsg(msg);
				closeTaskDto.setHdnBihinTaiyoWarnContinueFlg(FLG_ON);
				return closeTaskDto;
			}
		}

		if (!FLG_ON.equals(bihinHenkyakuWarnContinueFlg)) {
			//▼退居情報について備品返却日の設定確認（※エラーとしない）
			String bihinHenkyakuWarningMsg = checkBihinHenkyakuData(jikkouShijiYoteiNengetsu);

			if (!"".equals(bihinHenkyakuWarningMsg)) {
				//品の返却日が設定されていない場合、警告メッセージを表示
				String msg = skf3050Sc002SharedService.editMsg(MessageIdConstant.I_SKF_3008, bihinHenkyakuWarningMsg);
				closeTaskDto.setHdnWarnMsg(msg);
				closeTaskDto.setHdnBihinHenkyakuWarnContinueFlg(FLG_ON);
				return closeTaskDto;
			}
		}

		closeTaskDto.setHdnBihinTaiyoWarnContinueFlg("");
		closeTaskDto.setHdnBihinHenkyakuWarnContinueFlg("");

		//▼締め処理バッチを起動
		Map<String, Object> param = new HashMap<>();

		// タスク設定ユーザー
		param.put(AsyncTaskHelper.REGISTE_USER_CD, LoginUserInfoUtils.getUserCd());

		Map<String, String> dataMap = new HashMap<>();

		dataMap.put(BATCH_PRG_ID, batchPrgId);
		dataMap.put(COMPANY_CD, CodeConstant.C001);
		dataMap.put(USER_ID, skf3050Sc002SharedService.getUserId());
		dataMap.put(SHORI_NENGETSU, jikkouShijiYoteiNengetsu);
		dataMap.put(SHIME_SHORI_FLG, Skf3050Sc002SharedService.SHIME_SHORI_ON);

		param.put("parameter", dataMap);

		// タスクキューの登録
		String queueId = "jp.co.c_nexco.skf.skf3050.domain.service.skf3050sc002.Skf3050Sc002CloseTaskExecutionAsyncService";
		TaskManager.addSerializedTaskQueue(queueId, true);

		String msgId = "";
		String taskClassName = "jp.co.c_nexco.skf.skf3050.domain.task.skf3050bt001.Skf3050Bt001AsyncCloseTask";

		TaskMessage taskMsg = addSerializedTaskWithInfo(closeTaskDto, queueId, taskClassName, param,
				CommonConstant.C_SYSTEM_ID.toUpperCase(), "Skf3050bt001", "締め処理制御", "締め処理");

		if (taskMsg != null) {
			msgId = msgId + taskMsg.getMessageId();
			LogUtils.debugByMsg("タスク処理メッセージID：" + taskMsg.getMessageId());
		}

		//▼締め処理完了メッセージ表示
		closeTaskDto.setTaskMsgId(msgId);

		return closeTaskDto;
	}

	/**
	 * 入居情報について備品貸与日の設定をチェックする。（※エラーとしない） 備品の貸与日が設定されていない場合、警告メッセージを表示する。
	 * 
	 * @param jikkouShijiYoteiNengetsu
	 *            実行指示予定年月
	 * @return 警告メッセージ
	 */
	private String checkBihinTaiyoData(String jikkouShijiYoteiNengetsu) {

		String rtnErrMsg = "";

		List<Skf3050Sc002GetBihinTaiyoDataExp> nyukyoDataList = skf3050Sc002SharedService
				.getBihinTaiyoData(jikkouShijiYoteiNengetsu);
		if (nyukyoDataList.size() > 0) {

			for (Skf3050Sc002GetBihinTaiyoDataExp nyukyoData : nyukyoDataList) {
				if (NfwStringUtils.isEmpty(nyukyoData.getTaiyoHenkyakuDate())) {
					String jikkoNengetsu = skf3050Sc002SharedService.editDisplayNengetsu(jikkouShijiYoteiNengetsu);
					rtnErrMsg = ERRMSG_SHIME_BIHIN_NYUKYO + jikkoNengetsu;
				}
			}
		}

		return rtnErrMsg;
	}

	/**
	 * 退居情報について備品返却日の設定をチェックする。（※エラーとしない）
	 * 
	 * @param jikkouShijiYoteiNengetsu
	 *            実行指示予定年月
	 * @return 警告メッセージ
	 */
	private String checkBihinHenkyakuData(String jikkouShijiYoteiNengetsu) {

		String rtnErrMsg = "";

		List<Skf3050Sc002GetBihinHenkyakuDataExp> bihinHenkyakuDataList = skf3050Sc002SharedService
				.getBihinHenkyakuData(jikkouShijiYoteiNengetsu);
		if (bihinHenkyakuDataList.size() > 0) {

			for (Skf3050Sc002GetBihinHenkyakuDataExp bihinHenkyakuData : bihinHenkyakuDataList) {
				if (NfwStringUtils.isEmpty(bihinHenkyakuData.getTaiyoHenkyakuDate())) {
					String jikkoNengetsu = skf3050Sc002SharedService.editDisplayNengetsu(jikkouShijiYoteiNengetsu);
					rtnErrMsg = ERRMSG_SHIME_BIHIN_TAIKYO + jikkoNengetsu;
				}
			}
		}

		return rtnErrMsg;
	}

}
