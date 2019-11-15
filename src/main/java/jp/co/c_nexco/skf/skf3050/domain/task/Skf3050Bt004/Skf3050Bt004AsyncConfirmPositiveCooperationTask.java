/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3050.domain.task.Skf3050Bt004;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.context.annotation.Scope;

import jp.co.c_nexco.nfw.common.bean.SpringContext;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.task.AsyncTaskAbstract;
import jp.co.c_nexco.ptp.common.constants.CommonConstant;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;

/**
 * Skf3050Bt004AsyncConfirmPositiveCooperationTask POSITIVE連携データ確定（オンラインバッチ）クラス
 *
 * @author NEXCOシステムズ
 */
@Scope("prototype")
public class Skf3050Bt004AsyncConfirmPositiveCooperationTask extends AsyncTaskAbstract {

	private Skf3050Bt004SharedTask skf3050Bt004SharedTask;

	private static final String BEAN_GET_KEY = "skf3050Bt003SharedTask";

	@Override
	public void init() {

		this.systemId = CommonConstant.C_SYSTEM_ID.toUpperCase();
		this.programId = "skf3050Bt004";

		skf3050Bt004SharedTask = (Skf3050Bt004SharedTask) SpringContext.getBean(BEAN_GET_KEY);
	}

	@Override
	protected void execute() throws Exception {

		@SuppressWarnings("unchecked")
		Map<String, String> paramMap = (Map<String, String>) this.params.get("parameter");

		outputStartLog(paramMap);

		if (paramMap.size() != Skf3050Bt004SharedTask.PARAMETER_NUM) {
			LogUtils.error(MessageIdConstant.E_SKF_1092, paramMap.size());
			skf3050Bt004SharedTask.outputEndProcLog();
			return;
		}

		LogUtils.info(MessageIdConstant.I_SKF_1022, Skf3050Bt004SharedTask.BATCH_NAME);

		int registResult = skf3050Bt004SharedTask.registBatchControl(paramMap);

		if (registResult == CodeConstant.SYS_NG) {
			skf3050Bt004SharedTask.outputEndProcLog();
			return;
		}

		String confirmResult = skf3050Bt004SharedTask.confirmData(paramMap);

		skf3050Bt004SharedTask.endProc(confirmResult, paramMap.get(Skf3050Bt004SharedTask.SKF3050BT004_COMPANY_CD_KEY));

		skf3050Bt004SharedTask.outputEndProcLog();
	}

	/**
	 * 処理開始ログの出力
	 * 
	 * @param prm
	 *            パラメータリスト
	 * @param systemDate
	 *            現在日付
	 * @return ヘッダーログ
	 */
	private void outputStartLog(Map<String, String> prm) {

		String log_1 = "プログラム名：" + Skf3050Bt004SharedTask.BATCH_NAME;
		LogUtils.infoByMsg(log_1);

		String shoriNengetsu = CodeConstant.HYPHEN;

		if (prm.size() >= Skf3050Bt004SharedTask.PARAMETER_NUM) {
			shoriNengetsu = prm.get(Skf3050Bt004SharedTask.SKF3050BT004_SHORI_NENGETSU_KEY);
		}

		String log_2 = "対象年月：" + shoriNengetsu;
		LogUtils.infoByMsg(log_2);

		Date sysDate = skf3050Bt004SharedTask.getSystemDate();
		String strSysDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(sysDate);
		String log_3 = "処理開始時間" + strSysDate;
		LogUtils.infoByMsg(log_3);
	}

}
