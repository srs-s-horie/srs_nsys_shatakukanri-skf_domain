/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3050.domain.service.skf3050sc002;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3050TMonthlyManageData;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.SkfAsyncServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3050.domain.dto.skf3050sc002.Skf3050Sc002CreatePositiveCooperationDataAsyncDto;
import jp.co.intra_mart.mirage.integration.guice.Transactional;

/**
 * Skf3050Sc002CreatePositiveCooperationDataAsyncService 月次運用管理画面のPOSITIVE連携データ作成処理
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3050Sc002CreatePositiveCooperationDataAsyncService extends SkfAsyncServiceAbstract<Skf3050Sc002CreatePositiveCooperationDataAsyncDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3050Sc002SharedService skf3050Sc002SharedService;
	@Value("${skf3050.skf3050_bt003.batch_prg_id}")
	private String batchPrgId;

	/**
	 * サービス処理を行う。　
	 * 給与連携データ作成ボタン押下時処理
	 * 
	 * @param asyncDto	インプットDTO
	 * @return 処理結果
	 * @throws Exception	例外
	 */
	@Override
	@Transactional
	public AsyncBaseDto index(Skf3050Sc002CreatePositiveCooperationDataAsyncDto asyncDto) throws Exception {

		skfOperationLogUtils.setAccessLog("POSITIVE連携データ作成処理", CodeConstant.C001, "Skf3050Sc002");

		asyncDto.setResultMessages(null);
		String endFlg = SkfCommonConstant.COMPLETE;
		String jikkouShijiYoteiNengetsu = asyncDto.getHdnJikkouShijiYoteiNengetsu();

		//▼連携データ作成処理起動事前チェック
		String errMsg = checkBeforeStartup(jikkouShijiYoteiNengetsu);
		if (!"".equals(errMsg)) {
			//HR連携データ作成処理事前チェックが異常の場合、エラーメッセージを表示し処理を中断する
			// E_SKF_3024 {0}、給与連携データ作成処理ができません。
			ServiceHelper.addErrorResultMessage(asyncDto, null, MessageIdConstant.E_SKF_3024, errMsg);
			throwBusinessExceptionIfErrors(asyncDto.getResultMessages());
		}

		//▼連携データ作成処理バッチを起動
		Map<String, String> dataMap = new HashMap<>();
		dataMap.put(Skf3050Sc002SharedService.BATCH_PRG_ID_KEY, batchPrgId);
		dataMap.put(Skf3050Sc002SharedService.COMPANY_CD_KEY, CodeConstant.C001);
		dataMap.put(Skf3050Sc002SharedService.USER_ID_KEY, skf3050Sc002SharedService.getUserId());
		dataMap.put(Skf3050Sc002SharedService.SHORI_NENGETSU_KEY, jikkouShijiYoteiNengetsu);
		skf3050Sc002SharedService.outputStartLog(dataMap, Skf3050Sc002SharedService.CREATE_POSITIVE_DATA_BATCH_NAME);

		//パラメータ数のチェック
		if (dataMap.size() != Skf3050Sc002SharedService.PARAMETER_NUM) {
			LogUtils.info(MessageIdConstant.E_SKF_1092, dataMap.size());
			skf3050Sc002SharedService.outputEndProcLog(Skf3050Sc002SharedService.CREATE_POSITIVE_DATA_BATCH_NAME);
			// E_SKF_1092 必要なパラメータ数と一致しません。(パラメータ数：{0})
			ServiceHelper.addErrorResultMessage(asyncDto, null, MessageIdConstant.E_SKF_1092, dataMap.size());
			throwBusinessExceptionIfErrors(asyncDto.getResultMessages());
		}
		//トランザクションAを開始
		int registResult = CodeConstant.SYS_NG;
		Date sysDate = skf3050Sc002SharedService.getSystemDate();
		List<String> endList = skf3050Sc002SharedService.setEndList();
		registResult = skf3050Sc002SharedService.registBatchControl(dataMap, sysDate, endList);
		if (registResult == CodeConstant.SYS_NG) {
			LogUtils.infoByMsg("給与連携データ作成処理に失敗しました。(トランザクションA");
			skf3050Sc002SharedService.outputEndProcLog(Skf3050Sc002SharedService.CREATE_POSITIVE_DATA_BATCH_NAME);
			ServiceHelper.addErrorResultMessage(asyncDto, null, MessageIdConstant.E_SKF_3024, "バッチ制御テーブル更新に失敗したため");
			throwBusinessExceptionIfErrors(asyncDto.getResultMessages());
		}
		Boolean updateResult = false;
		try {
			//トランザクションBの開始
			updateResult = skf3050Sc002SharedService.createPositiveUpdateGetsujiShoriKanri(dataMap);
			if (!updateResult) {
				endFlg = SkfCommonConstant.ABNORMAL;
				LogUtils.infoByMsg("給与連携データ作成処理に失敗しました。(トランザクションB)");
			}
		} catch (Exception e) {
			LogUtils.infoByMsg("index(トランザクションB), " + e.getMessage());
			endFlg = SkfCommonConstant.ABNORMAL;
		}
		//トランザクションCの開始
		if (Objects.equals(endFlg, SkfCommonConstant.ABNORMAL)) {
			//終了処理
			skf3050Sc002SharedService.endCreatePositiveDataProc(dataMap.get(Skf3050Sc002SharedService.COMPANY_CD_KEY), endFlg);
//			asyncDto.setErrMsg("給与連携データ作成処理に失敗しました。");
			LogUtils.infoByMsg("給与連携データ作成処理に失敗しました。(トランザクションC");
			skf3050Sc002SharedService.outputEndProcLog(Skf3050Sc002SharedService.CREATE_POSITIVE_DATA_BATCH_NAME);
			// 排他エラー W_SKF_1009
			ServiceHelper.addErrorResultMessage(asyncDto, null, MessageIdConstant.W_SKF_1009);
			throwBusinessExceptionIfErrors(asyncDto.getResultMessages());
		}
		return asyncDto;
	}

	/**
	 * POSITIVE連携データ作成処理起動前のチェック
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
		
		//▼連携データ作成処理可能チェック
		Skf3050TMonthlyManageData renkeiKbnData = skf3050Sc002SharedService.getShimePositiveRenkeiKbn(jikkouShijiYoteiNengetsu);
		if (renkeiKbnData == null) {
			//月次処理管理データが取得できない場合エラー
			errMsg = Skf3050Sc002SharedService.ERRMSG_SHIME_IMPOSSIBLE;
			return errMsg;
		
		} else {
			if (!CodeConstant.BILLINGACTKBN_JIKKO_SUMI.equals(renkeiKbnData.getBillingActKbn()) || 
				CodeConstant.LINKDATA_COMMIT_KBN_JIKKO_SUMI.equals(renkeiKbnData.getLinkdataCommitKbn())) {
				//締め処理≠実行済、またはHR連携データ確定実行区分＝実行済の場合エラー
				errMsg = Skf3050Sc002SharedService.ERRMSG_SHIME_IMPOSSIBLE;
				return errMsg;
			}
		}
		return errMsg;
	}
}
