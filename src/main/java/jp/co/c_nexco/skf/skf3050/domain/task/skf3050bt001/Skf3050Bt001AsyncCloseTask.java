/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3050.domain.task.skf3050bt001;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
 * Skf3050Bt001AsyncCloseTask 締め処理（オンラインバッチ）クラス
 *
 * @author NEXCOシステムズ
 */
@Scope("prototype")
public class Skf3050Bt001AsyncCloseTask extends AsyncTaskAbstract {

	private Skf3050Bt001SharedTask skf3050Bt001SharedTask;

	private static final String BEAN_GET_KEY = "skf3050Bt001SharedTask";
	private static final String BATCH_NAME = "締め処理";
	private static final String[] INFO_SKF1030_PARAM = { "月別使用料履歴（再計算）", "月別使用料履歴（現物算定額）" };

	@Override
	public void init() {

		this.systemId = CommonConstant.C_SYSTEM_ID.toUpperCase();
		this.programId = "skf3050bt001";

		skf3050Bt001SharedTask = (Skf3050Bt001SharedTask) SpringContext.getBean(BEAN_GET_KEY);
	}

	@Override
	public void execute() throws ParseException {
		LogUtils.debugByMsg("オンラインバッチ 締め処理開始");

		@SuppressWarnings("unchecked")
		Map<String, String> paramMap = (Map<String, String>) this.params.get("parameter");

		Date sysDate = skf3050Bt001SharedTask.getStrSystemDate();
		outputHeaderLog(paramMap, sysDate);

		List<String> endList = setEndList();

		//パラメータ数のチェック
		if (paramMap.size() == 0 || paramMap.size() != Skf3050Bt001SharedTask.PARAMETER_NUM) {
			LogUtils.error(MessageIdConstant.E_SKF_1092, paramMap.size());
			skf3050Bt001SharedTask.outputManagementLogEndProc(endList, sysDate);
			return;
		}

		LogUtils.info(MessageIdConstant.I_SKF_1022, BATCH_NAME);

		//トランザクションAを開始
		int registResult = skf3050Bt001SharedTask.registBatchControl(paramMap, sysDate, endList);

		if (Skf3050Bt001SharedTask.RETURN_STATUS_NG == registResult) {
			return;
		}
		//トランザクションBの開始
		String result = skf3050Bt001SharedTask.updateTsukibetsuTsukiji(paramMap, endList);
		if (SkfCommonConstant.ABNORMAL.equals(result)) {
			LogUtils.error(MessageIdConstant.E_SKF_1079);
		}

		//トランザクションCの開始
		Integer dbPrcRtn = skf3050Bt001SharedTask.endProc(result, paramMap.get(Skf3050Bt001SharedTask.COMPANY_CD),
				Skf3050Bt001SharedTask.BATCH_ID_B5001, SkfCommonConstant.PROCESSING);

		//管理ログ終了処理
		if (dbPrcRtn > 0) {
			skf3050Bt001SharedTask.outputManagementLogEndProc(endList, null);
		}

		LogUtils.debugByMsg("オンラインバッチ 締め処理終了");
		return;
	}

	/**
	 * ヘッダーログの出力
	 * 
	 * @param prm
	 *            パラメータリスト
	 * @param systemDate
	 *            現在日付
	 * @return ヘッダーログ
	 */
	private void outputHeaderLog(Map<String, String> prm, Date sysDate) {

		String log_1 = "プログラム名：" + BATCH_NAME;
		LogUtils.infoByMsg(log_1);

		String shoriNengetsu = CodeConstant.HYPHEN;

		if (prm.size() >= Skf3050Bt001SharedTask.PARAMETER_NUM) {
			shoriNengetsu = prm.get(Skf3050Bt001SharedTask.SHORI_NENGETSU);
		}

		String log_2 = "対象年月：" + shoriNengetsu;
		LogUtils.infoByMsg(log_2);

		String strSysDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(sysDate);
		String log_3 = "処理開始時間" + strSysDate;
		LogUtils.infoByMsg(log_3);
	}

	/**
	 * 終了ログ出力
	 * 
	 * @return 終了ログリスト
	 */
	private List<String> setEndList() {

		List<String> kanriInfoList = new ArrayList<String>();

		for (int i = 0; i < INFO_SKF1030_PARAM.length; i++) {
			String[] replaceStrArry = { INFO_SKF1030_PARAM[i], "0" };
			String addMsg = skf3050Bt001SharedTask.editMsg(MessageIdConstant.I_SKF_1030, replaceStrArry);

			kanriInfoList.add(addMsg);
		}

		return kanriInfoList;
	}

}
