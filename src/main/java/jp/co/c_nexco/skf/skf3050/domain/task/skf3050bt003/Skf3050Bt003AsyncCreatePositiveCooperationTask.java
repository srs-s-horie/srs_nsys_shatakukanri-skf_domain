/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3050.domain.task.skf3050bt003;

import java.text.SimpleDateFormat;
import java.util.Date;
//import java.util.HashMap;
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
 * Skf3050Bt003AsyncCreatePositiveCooperationTask POSITIVE連携データ作成（オンラインバッチ）クラス
 *
 * @author NEXCOシステムズ
 */
@Scope("prototype")
public class Skf3050Bt003AsyncCreatePositiveCooperationTask extends AsyncTaskAbstract {

	private Skf3050Bt003SharedTask skf3050Bt003SharedTask;

	private static final String BEAN_GET_KEY = "skf3050Bt003SharedTask";

	@Override
	public void init() {

		this.systemId = CommonConstant.C_SYSTEM_ID.toUpperCase();
		this.programId = "skf3050Bt003";

		skf3050Bt003SharedTask = (Skf3050Bt003SharedTask) SpringContext.getBean(BEAN_GET_KEY);
	}

	@Override
	protected void execute() throws Exception {

		@SuppressWarnings("unchecked")
		Map<String, String> paramMap = (Map<String, String>) this.params.get("parameter");
		String endFlg = SkfCommonConstant.ABNORMAL;

		outputStartLog(paramMap);

		//パラメータ数のチェック
		if (paramMap.size() != Skf3050Bt003SharedTask.PARAMETER_NUM) {
			LogUtils.info(MessageIdConstant.E_SKF_1092, paramMap.size());
			outputEndProcLog();
			return;
		}

		//トランザクションAを開始
		int registResult = skf3050Bt003SharedTask.registBatchControl(paramMap);

		if (registResult == CodeConstant.SYS_NG) {
			outputEndProcLog();
			return;
		}

		try {
			//トランザクションBの開始
			Map<String, Object> fileOutputData = null;
			fileOutputData = skf3050Bt003SharedTask.createPositiveCooperationData(paramMap);
	
			if (fileOutputData != null && fileOutputData.size() != 0) {
				// 呼び出し画面作成後、Excel出力データの渡し方を修正する。⇒同期処置として画面側で実装
				// byte[] writeFileData = (byte[]) fileOutputData.get("fileData");
				// 解放
				// writeFileData = null;
				endFlg = SkfCommonConstant.COMPLETE;
			} 
		} catch (Exception e) {
			LogUtils.infoByMsg("異常終了:" + Skf3050Bt003SharedTask.BATCH_NAME + "(" + e.getMessage() + ")");
		}

		skf3050Bt003SharedTask.endProc(endFlg, paramMap.get(Skf3050Bt003SharedTask.SKF3050BT003_COMPANY_CD_KEY));

		outputEndProcLog();
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

		LogUtils.info(MessageIdConstant.I_SKF_1022, Skf3050Bt003SharedTask.BATCH_NAME);

		String shoriNengetsu = CodeConstant.HYPHEN;

		if (prm.size() >= Skf3050Bt003SharedTask.PARAMETER_NUM) {
			shoriNengetsu = prm.get(Skf3050Bt003SharedTask.SKF3050BT003_SHORI_NENGETSU_KEY);
		}

		String log_2 = "対象年月：" + shoriNengetsu;
		LogUtils.infoByMsg(log_2);

		Date sysDate = skf3050Bt003SharedTask.getSystemDate();
		String strSysDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(sysDate);
		String log_3 = "処理開始時間" + strSysDate;
		LogUtils.infoByMsg(log_3);
	}

	/**
	 * 処理終了ログ
	 * 
	 * @param msg
	 *            出力する置き換えメッセージ
	 */
	public void outputEndProcLog() {

		Date sysDate = skf3050Bt003SharedTask.getSystemDate();
		String strSysDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(sysDate);

		LogUtils.infoByMsg("処理終了時間：" + strSysDate);
		LogUtils.info(MessageIdConstant.I_SKF_1023, Skf3050Bt003SharedTask.BATCH_NAME);
	}
}
