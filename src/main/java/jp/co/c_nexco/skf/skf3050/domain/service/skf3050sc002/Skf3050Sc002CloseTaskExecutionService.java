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
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Sc002.Skf3050Sc002GetBihinHenkyakuDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Sc002.Skf3050Sc002GetBihinTaiyoDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Sc002.Skf3050Sc002UpdateBihinHenkyakubiExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Sc002.Skf3050Sc002UpdateBihinHenkyakubiExpRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.LoginUserInfoUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3050.domain.dto.skf3050sc002.Skf3050Sc002CloseTaskExecutionDto;

/**
 * Skf3050Sc002CloseTaskExecutionService 月次運用管理画面の締め処理
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3050Sc002CloseTaskExecutionService extends SkfServiceAbstract<Skf3050Sc002CloseTaskExecutionDto> {
	

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3050Sc002SharedService skf3050Sc002SharedService;
	@Autowired
	private Skf3050Sc002UpdateBihinHenkyakubiExpRepository skf3050Sc002UpdateBihinHenkyakubiExpRepository;

	private static final String ERRMSG_SHIME_BIHIN_NYUKYO = "入居情報について備品貸与日が設定されていません。";
	private static final String ERRMSG_SHIME_BIHIN_TAIKYO = "退居情報について備品返却日が設定されていません。備品返却日に退居日を設定し、";
	private static final String BATCH_NAME = "締め処理";

//	public static final String BATCH_PRG_ID_KEY = "batchPrgId";
//	public static final String COMPANY_CD_KEY = "batchCompanyCd";
//	public static final String USER_ID_KEY = "batchUserId";
//	public static final String SHORI_NENGETSU_KEY = "batchShoriNengetsu";
//	public static final String SHIME_SHORI_FLG = "batchShimeShoriFlg";
//	private static final String SHORI_NENGETSU = "closeShoriNengetsu";
//	private static final String SHIME_SHORI_FLG = "shimeShoriFlg";

	private static final String FLG_ON = "1";
	
	@Value("${skf3050.skf3050_bt001.batch_prg_id}")
	private String batchPrgId;

	@Override
	protected BaseDto index(Skf3050Sc002CloseTaskExecutionDto closeTaskDto) throws Exception {

		skfOperationLogUtils.setAccessLog("締め処理", CodeConstant.C001, "Skf3050Sc002");

		LogUtils.info(MessageIdConstant.I_SKF_1022, BATCH_NAME);
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
				return closeTaskDto;
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

		//▼退居情報について備品返却日の設定確認（※エラーとしない）
		String bihinHenkyakuWarningMsg = checkBihinHenkyakuData(jikkouShijiYoteiNengetsu, bihinHenkyakuWarnContinueFlg);

		if (!"".equals(bihinHenkyakuWarningMsg)) {
			//品の返却日が設定されていない場合、警告メッセージを表示
			String msg = skf3050Sc002SharedService.editMsg(MessageIdConstant.I_SKF_3008, bihinHenkyakuWarningMsg);
			closeTaskDto.setHdnWarnMsg(msg);
			closeTaskDto.setHdnBihinHenkyakuWarnContinueFlg(FLG_ON);
			return closeTaskDto;
		}

		closeTaskDto.setHdnBihinTaiyoWarnContinueFlg("");
		closeTaskDto.setHdnBihinHenkyakuWarnContinueFlg("");

		Map<String, String> dataMap = new HashMap<>();

		dataMap.put(Skf3050Sc002SharedService.BATCH_PRG_ID_KEY, batchPrgId);
		dataMap.put(Skf3050Sc002SharedService.COMPANY_CD_KEY, CodeConstant.C001);
		dataMap.put(Skf3050Sc002SharedService.USER_ID_KEY, skf3050Sc002SharedService.getUserId());
		dataMap.put(Skf3050Sc002SharedService.SHORI_NENGETSU_KEY, jikkouShijiYoteiNengetsu);
		dataMap.put(Skf3050Sc002SharedService.SHIME_SHORI_FLG, Skf3050Sc002SharedService.SHIME_SHORI_ON);

		Date sysDate = skf3050Sc002SharedService.getSystemDate();
		List<String> endList = skf3050Sc002SharedService.setEndList();
		//トランザクションAを開始
		int registResult = skf3050Sc002SharedService.registBatchControl(dataMap, sysDate, endList);

		if (Skf3050Sc002SharedService.RETURN_STATUS_NG == registResult) {
			ServiceHelper.addErrorResultMessage(closeTaskDto, null, MessageIdConstant.E_SKF_3013, "バッチ制御テーブル更新に失敗したため");
			return closeTaskDto;
		}
		String result = SkfCommonConstant.ABNORMAL;
		try {
			//トランザクションBの開始
			result = skf3050Sc002SharedService.updateTsukibetsuTsukiji(dataMap, endList);
			if (SkfCommonConstant.ABNORMAL.equals(result)) {
				LogUtils.info(MessageIdConstant.E_SKF_1079);
			}
		} catch (Exception e) {
			LogUtils.infoByMsg("異常終了:締め処理(" + e.getMessage() + ")");
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
		skf3050Sc002SharedService.outputManagementLogEndProc(endList);
		LogUtils.info(MessageIdConstant.I_SKF_1023, BATCH_NAME);
		// 画面リフレッシュ
		TransferPageInfo nextPage = TransferPageInfo.prevPage(FunctionIdConstant.SKF3050_SC002, "init");
		closeTaskDto.setTransferPageInfo(nextPage);
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
	private String checkBihinHenkyakuData(String jikkouShijiYoteiNengetsu, String bihinHenkyakuWarnContinueFlg) {

		String rtnErrMsg = "";
		// ユーザーID取得
		String userId = LoginUserInfoUtils.getUserCd();
		String progId = batchPrgId;

		List<Skf3050Sc002GetBihinHenkyakuDataExp> bihinHenkyakuDataList = skf3050Sc002SharedService
				.getBihinHenkyakuData(jikkouShijiYoteiNengetsu);
		if (bihinHenkyakuDataList.size() > 0) {
			if (!FLG_ON.equals(bihinHenkyakuWarnContinueFlg)) {
				for (Skf3050Sc002GetBihinHenkyakuDataExp bihinHenkyakuData : bihinHenkyakuDataList) {
					if (NfwStringUtils.isEmpty(bihinHenkyakuData.getTaiyoHenkyakuDate())) {
						String jikkoNengetsu = skf3050Sc002SharedService.editDisplayNengetsu(jikkouShijiYoteiNengetsu);
						rtnErrMsg = ERRMSG_SHIME_BIHIN_TAIKYO + jikkoNengetsu;
						break;
					}
				}
			} else {
				// 返却日を退居日で更新
				for (Skf3050Sc002GetBihinHenkyakuDataExp bihinHenkyakuData : bihinHenkyakuDataList) {
					if (NfwStringUtils.isEmpty(bihinHenkyakuData.getTaiyoHenkyakuDate())) {
						Skf3050Sc002UpdateBihinHenkyakubiExpParameter param = new Skf3050Sc002UpdateBihinHenkyakubiExpParameter();
						param.setHenkyakuDate(bihinHenkyakuData.getNyukyoTaikyoDate());
						param.setShatakuKanriId(bihinHenkyakuData.getShatakuKanriId());
						param.setProgId(progId);
						param.setUserId(userId);
						skf3050Sc002UpdateBihinHenkyakubiExpRepository.updateBihinHenkyakubi(param);
					}
				}
			}
		}
		return rtnErrMsg;
	}
}
