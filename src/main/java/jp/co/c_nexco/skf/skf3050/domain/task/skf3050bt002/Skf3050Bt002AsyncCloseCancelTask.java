/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3050.domain.task.skf3050bt002;

import java.util.Map;

import org.springframework.context.annotation.Scope;

import jp.co.c_nexco.nfw.common.bean.SpringContext;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.task.AsyncTaskAbstract;
import jp.co.c_nexco.ptp.common.constants.CommonConstant;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;

/**
 * Skf3050Bt002AsyncCloseCancelTask 締め解除処理（オンラインバッチ）クラス
 *
 * @author NEXCOシステムズ
 */
@Scope("prototype")
public class Skf3050Bt002AsyncCloseCancelTask extends AsyncTaskAbstract {

	private Skf3050Bt002SharedTask skf3050Bt002SharedTask;

	private static final String BEAN_GET_KEY = "skf3050Bt002SharedTask";

	@Override
	public void init() {

		this.systemId = CommonConstant.C_SYSTEM_ID.toUpperCase();
		this.programId = "skf3050bt002";

		skf3050Bt002SharedTask = (Skf3050Bt002SharedTask) SpringContext.getBean(BEAN_GET_KEY);
	}

	@Override
	protected void execute() throws Exception {

		@SuppressWarnings("unchecked")
		Map<String, String> paramMap = (Map<String, String>) this.params.get("parameter");

		skf3050Bt002SharedTask.outputStartLog(paramMap.get(Skf3050Bt002SharedTask.SKF3050BT002_SHORI_NENGETSU_KEY));

		//トランザクションAを開始
		//パラメータ数のチェック
		if (paramMap.size() != Skf3050Bt002SharedTask.PARAMETER_NUM) {
			LogUtils.error(MessageIdConstant.E_SKF_1092, paramMap.size());
			skf3050Bt002SharedTask.outputEndProcLog("");
			return;
		}

		LogUtils.info(MessageIdConstant.I_SKF_1022, Skf3050Bt002SharedTask.BATCH_NAME);

		int registResult = skf3050Bt002SharedTask.registBatchControl(paramMap);

		if (registResult == CodeConstant.SYS_NG) {
			skf3050Bt002SharedTask.outputEndProcLog("");
			return;
		}
		//トランザクションBの開始
		String endFlag = SkfCommonConstant.ABNORMAL;
		String updateGetsujiDataMsg = "";
		try {
			boolean canLock = skf3050Bt002SharedTask
					.canLockTableData(paramMap.get(Skf3050Bt002SharedTask.SKF3050BT002_SHORI_NENGETSU_KEY));
	
			if (canLock) {
				Map<String, String> result = skf3050Bt002SharedTask.updateGetsujiData(paramMap);
				endFlag = result.get(Skf3050Bt002SharedTask.UPDATE_GETSUJI_DATA_RESULT_KEY);
				updateGetsujiDataMsg = result.get(Skf3050Bt002SharedTask.UPDATE_GETSUJI_DATA_MSG_KEY);
	
			} else {
				LogUtils.error(MessageIdConstant.E_SKF_1079, "");
			}
		} catch (Exception e) {
			LogUtils.infoByMsg("異常終了:" + Skf3050Bt002SharedTask.BATCH_NAME + "(" + e.getMessage() + ")");
		}

		//トランザクションCの開始
		//終了処理
		Integer dbPrcRtn = skf3050Bt002SharedTask.endProc(endFlag,
				paramMap.get(Skf3050Bt002SharedTask.SKF3050BT002_COMPANY_CD_KEY), paramMap.get(Skf3050Bt002SharedTask.SKF3050BT002_BATCH_PRG_ID_KEY),
				SkfCommonConstant.PROCESSING);

		if (dbPrcRtn == CodeConstant.SYS_OK) {
			skf3050Bt002SharedTask.outputEndProcLog(updateGetsujiDataMsg);
		} else {
			skf3050Bt002SharedTask.outputEndProcLog("");
		}

		LogUtils.info(MessageIdConstant.I_SKF_1023, Skf3050Bt002SharedTask.BATCH_NAME);
	}

}
