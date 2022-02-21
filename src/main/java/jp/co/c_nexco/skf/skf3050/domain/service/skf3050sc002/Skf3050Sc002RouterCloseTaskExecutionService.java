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

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3050.domain.dto.skf3050sc002.Skf3050Sc002RouterCloseTaskExecutionDto;

/**
 * Skf3050Sc002CloseTaskExecutionService 月次運用管理画面のモバイルルーター締め処理
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3050Sc002RouterCloseTaskExecutionService extends SkfServiceAbstract<Skf3050Sc002RouterCloseTaskExecutionDto> {
	

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3050Sc002SharedService skf3050Sc002SharedService;

	private static final String BATCH_NAME = "モバイルルーター締め処理";

	
	@Value("${skf3050.skf3050_bt005.batch_prg_id}")
	private String batchPrgId;

	@Override
	protected BaseDto index(Skf3050Sc002RouterCloseTaskExecutionDto closeTaskDto) throws Exception {

		skfOperationLogUtils.setAccessLog("モバイルルーター締め処理", CodeConstant.C001, FunctionIdConstant.SKF3050_SC002);

		LogUtils.info(MessageIdConstant.I_SKF_1022, BATCH_NAME);
		String jikkouShijiYoteiNengetsu = closeTaskDto.getHdnJikkouShijiYoteiNengetsu();

		closeTaskDto.setResultMessages(null);

		
		//▼締め処理起動事前チェック
		String errMsg = skf3050Sc002SharedService.checkRouterShimeShori(jikkouShijiYoteiNengetsu,
				Skf3050Sc002SharedService.SHIME_SHORI_ON);

		if (!"".equals(errMsg)) {
			//締め処理事前チェックが異常の場合、エラーメッセージを表示し処理を中断する
			ServiceHelper.addErrorResultMessage(closeTaskDto, null, MessageIdConstant.E_SKF_3013, errMsg);
			return closeTaskDto;
		}
		
		closeTaskDto.setHdnWarnMsg("");


		Map<String, String> dataMap = new HashMap<>();

		dataMap.put(Skf3050Sc002SharedService.BATCH_PRG_ID_KEY, batchPrgId);
		dataMap.put(Skf3050Sc002SharedService.COMPANY_CD_KEY, CodeConstant.C001);
		dataMap.put(Skf3050Sc002SharedService.USER_ID_KEY, skf3050Sc002SharedService.getUserId());
		dataMap.put(Skf3050Sc002SharedService.SHORI_NENGETSU_KEY, jikkouShijiYoteiNengetsu);
		dataMap.put(Skf3050Sc002SharedService.SHIME_SHORI_FLG, Skf3050Sc002SharedService.SHIME_SHORI_ON);

		Date sysDate = skf3050Sc002SharedService.getSystemDate();
		List<String> endList = skf3050Sc002SharedService.setEndListRouter();
		//トランザクションAを開始
		int registResult = skf3050Sc002SharedService.registBatchControl(dataMap, sysDate, endList);

		if (Skf3050Sc002SharedService.RETURN_STATUS_NG == registResult) {
			ServiceHelper.addErrorResultMessage(closeTaskDto, null, MessageIdConstant.E_SKF_3013, "バッチ制御テーブル更新に失敗したため");
			return closeTaskDto;
		}
		String result = SkfCommonConstant.ABNORMAL;
		try {
			//トランザクションBの開始
			result = skf3050Sc002SharedService.updateRouterRireki(dataMap, endList);
			if (SkfCommonConstant.ABNORMAL.equals(result)) {
				LogUtils.info(MessageIdConstant.E_SKF_1079);
			}
		} catch (Exception e) {
			LogUtils.infoByMsg("異常終了:モバイルルーター締め処理(" + e.getMessage() + ")");
		}

		//トランザクションCの開始
		skf3050Sc002SharedService.endProc(result, dataMap.get(Skf3050Sc002SharedService.COMPANY_CD_KEY),
																			batchPrgId, SkfCommonConstant.PROCESSING);

		if (!SkfCommonConstant.ABNORMAL.equals(result)) {
			String targetNengetsu = skf3050Sc002SharedService.editDisplayNengetsu(jikkouShijiYoteiNengetsu);
			ServiceHelper.addResultMessage(closeTaskDto, MessageIdConstant.I_SKF_3089, targetNengetsu);
		} else {
			ServiceHelper.addErrorResultMessage(closeTaskDto, null, MessageIdConstant.E_SKF_1079);
		}
		//skf3050Sc002SharedService.outputManagementLogEndProc(endList);
		LogUtils.info(MessageIdConstant.I_SKF_1023, BATCH_NAME);
		// 画面リフレッシュ
		TransferPageInfo nextPage = TransferPageInfo.prevPage(FunctionIdConstant.SKF3050_SC002, "init");
		closeTaskDto.setTransferPageInfo(nextPage);
		return closeTaskDto;
	}

	

	
}
