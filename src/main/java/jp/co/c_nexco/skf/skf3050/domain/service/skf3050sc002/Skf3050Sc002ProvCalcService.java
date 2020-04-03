/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3050.domain.service.skf3050sc002;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3050.domain.dto.skf3050sc002.Skf3050Sc002ProvCalcDto;
import jp.co.c_nexco.skf.skf3050.domain.task.skf3050bt001.Skf3050Bt001SharedTask;

/**
 * Skf3050Sc002ProvCalcService 月次運用管理画面の仮計算処理処理
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3050Sc002ProvCalcService extends BaseServiceAbstract<Skf3050Sc002ProvCalcDto> {
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3050Sc002SharedService skf3050Sc002SharedService;
	
	private static final String SHIME_SHORI_OFF = "0";
	private static final String[] INFO_SKF1030_PARAM = { "月別使用料履歴（再計算）", "月別使用料履歴（現物算定額）" };
	
	@Value("${skf3050.skf3050_bt001.batch_prg_id}")
	private String batchPrgId;

	@Override
	protected BaseDto index(Skf3050Sc002ProvCalcDto provCalcDto) throws Exception {
		
		skfOperationLogUtils.setAccessLog("仮計算処理", CodeConstant.C001, provCalcDto.getPageId());
		
		String jikkouShijiYoteiNengetsu = provCalcDto.getHdnJikkouShijiYoteiNengetsu();
		provCalcDto.setResultMessages(null);
		
		//▼締め処理起動事前チェック
		String errMsg = skf3050Sc002SharedService.checkShimeShori(jikkouShijiYoteiNengetsu, SHIME_SHORI_OFF);
		if (!"".equals(errMsg)) {
			//締め処理事前チェックが異常の場合、エラーメッセージを表示し処理を中断する
			ServiceHelper.addErrorResultMessage(provCalcDto, null, MessageIdConstant.E_SKF_3013, errMsg);
			return provCalcDto;
		}
		
		Map<String, String> paramMap = new HashMap<>();

		paramMap.put(Skf3050Sc002SharedService.BATCH_PRG_ID_KEY, batchPrgId);
		paramMap.put(Skf3050Sc002SharedService.COMPANY_CD_KEY, CodeConstant.C001);
		paramMap.put(Skf3050Sc002SharedService.USER_ID_KEY, skf3050Sc002SharedService.getUserId());
		paramMap.put(Skf3050Sc002SharedService.SHORI_NENGETSU_KEY, jikkouShijiYoteiNengetsu);
		paramMap.put(Skf3050Sc002SharedService.SHIME_SHORI_FLG, SHIME_SHORI_OFF);
		
		Date sysDate = skf3050Sc002SharedService.getSystemDate();
		outputHeaderLog(paramMap, sysDate);

		List<String> endList = setEndList();

		if (paramMap.size() == 0 || paramMap.size() != Skf3050Bt001SharedTask.PARAMETER_NUM) {
			LogUtils.error(MessageIdConstant.E_SKF_1092, paramMap.size());
			skf3050Sc002SharedService.outputManagementLogEndProc(endList, sysDate);
			return provCalcDto;
		}

		LogUtils.info(MessageIdConstant.I_SKF_1022, Skf3050Sc002SharedService.SHIME_SHORI_BATCH_NAME);

		int registResult = skf3050Sc002SharedService.registCloseBatchControl(paramMap, sysDate, endList);

		if (Skf3050Bt001SharedTask.RETURN_STATUS_NG == registResult) {
			return provCalcDto;
		}

		String result = SkfCommonConstant.ABNORMAL;
		result = skf3050Sc002SharedService.updateTsukibetsuTsukiji(paramMap, endList);
/* US 仮計算処理排他エラー対応 */
//		Integer dbPrcRtn = skf3050Sc002SharedService.endShimeProc(result, paramMap.get(Skf3050Bt001SharedTask.COMPANY_CD),
//				Skf3050Bt001SharedTask.BATCH_ID_B5001, SkfCommonConstant.PROCESSING);
		Integer dbPrcRtn = skf3050Sc002SharedService.endShimeProc(result, CodeConstant.C001, batchPrgId, SkfCommonConstant.PROCESSING);
/* UE 仮計算処理排他エラー対応 */

		if (dbPrcRtn > 0) {
			skf3050Sc002SharedService.outputManagementLogEndProc(endList, null);
		}
		
		reDisplay(provCalcDto);

		if (!SkfCommonConstant.ABNORMAL.equals(result)) {
			String targetNengetsu = skf3050Sc002SharedService.editDisplayNengetsu(jikkouShijiYoteiNengetsu);
			ServiceHelper.addResultMessage(provCalcDto, MessageIdConstant.I_SKF_3091, targetNengetsu);
		
		} else {
			ServiceHelper.addErrorResultMessage(provCalcDto, null, MessageIdConstant.E_SKF_1079);
		}
		
		return provCalcDto;
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

		String log_1 = "プログラム名：" + Skf3050Sc002SharedService.SHIME_SHORI_BATCH_NAME;
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
			String addMsg = skf3050Sc002SharedService.editMsg(MessageIdConstant.I_SKF_1030, replaceStrArry);

			kanriInfoList.add(addMsg);
		}

		return kanriInfoList;
	}
	
	/**
	 * 画面の再表示を行う。
	 * 
	 * @param inDto Skf3050Sc002ProvCalcDto
	 * @return Skf3050Sc002ProvCalcDto
	 */
	private void reDisplay(Skf3050Sc002ProvCalcDto inDto) {
		
		String targetYyyymm = inDto.getHdnSelectedTaisyonendo();

		List<Map<String, Object>> gridList = skf3050Sc002SharedService.createGetsujiGrid(targetYyyymm);
		inDto.setGetujiGrid(gridList);

		skf3050Sc002SharedService.getJikkoushijiHighlightData(inDto, inDto.getHdnJikkouShijiYoteiNengetsu());
		skf3050Sc002SharedService.setBtnMsg(inDto);
		skf3050Sc002SharedService.changeButtonStatus(inDto);
	}
}
