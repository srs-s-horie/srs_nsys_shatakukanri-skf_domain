/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3050.domain.service.skf3050sc002;

import static jp.co.c_nexco.nfw.core.constants.CommonConstant.NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3050TMonthlyManageData;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3050.domain.dto.skf3050Sc002common.Skf3050Sc002CommonDto;
import jp.co.c_nexco.skf.skf3050.domain.dto.skf3050sc002.Skf3050Sc002CreatePositiveCooperationDataDto;

/**
 * Skf3050Sc002CreatePositiveCooperationDataService 月次運用管理画面のPOSITIVE連携データ作成処理
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3050Sc002CreatePositiveCooperationDataService extends SkfServiceAbstract<Skf3050Sc002CreatePositiveCooperationDataDto> {
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3050Sc002SharedService skf3050Sc002SharedService;
	
	@Value("${skf3050.skf3050_bt003.batch_prg_id}")
	private String batchPrgId;

	@Override
	protected BaseDto index(Skf3050Sc002CreatePositiveCooperationDataDto createPositiveCoopDto) throws Exception {
		
		skfOperationLogUtils.setAccessLog("POSITIVE連携データ作成処理", CodeConstant.C001, "Skf3050Sc002");
		
		createPositiveCoopDto.setResultMessages(null);
		
		String jikkouShijiYoteiNengetsu = createPositiveCoopDto.getHdnJikkouShijiYoteiNengetsu();
		
		//▼連携データ作成処理起動事前チェック
		String errMsg = checkBeforeStartup(jikkouShijiYoteiNengetsu);
		if (!"".equals(errMsg)) {
			//HR連携データ作成処理事前チェックが異常の場合、エラーメッセージを表示し処理を中断する
			ServiceHelper.addErrorResultMessage(createPositiveCoopDto, null, MessageIdConstant.E_SKF_3024, errMsg);
			return createPositiveCoopDto;
		}

		//▼連携データ作成処理バッチを起動
		Map<String, String> dataMap = new HashMap<>();
		dataMap.put(Skf3050Sc002SharedService.BATCH_PRG_ID_KEY, batchPrgId);
		dataMap.put(Skf3050Sc002SharedService.COMPANY_CD_KEY, CodeConstant.C001);
		dataMap.put(Skf3050Sc002SharedService.USER_ID_KEY, skf3050Sc002SharedService.getUserId());
		dataMap.put(Skf3050Sc002SharedService.SHORI_NENGETSU_KEY, jikkouShijiYoteiNengetsu);
		
		outputStartLog(dataMap);

		//パラメータ数のチェック
		if (dataMap.size() != Skf3050Sc002SharedService.PARAMETER_NUM) {
			LogUtils.error(MessageIdConstant.E_SKF_1092, dataMap.size());
			outputEndProcLog();
		}

		//トランザクションAを開始
		int registResult = CodeConstant.SYS_NG;
		registResult = skf3050Sc002SharedService.registBatchControl(dataMap, batchPrgId);

		if (registResult == CodeConstant.SYS_NG) {
			outputEndProcLog();
			return createPositiveCoopDto;
		}

		//トランザクションBの開始
		String endFlg = SkfCommonConstant.COMPLETE;
		Map<String, Object> fileOutputData = null;
		try {
			fileOutputData = skf3050Sc002SharedService.createPositiveCooperationData(dataMap);
		} catch (Exception e) {
			LogUtils.infoByMsg("index, " + e.getMessage());
			fileOutputData = null;
		}
		

		if (fileOutputData != null && fileOutputData.size() != 0) {
			byte[] writeFileData = (byte[]) fileOutputData.get("fileData");
			createPositiveCoopDto.setFileData(writeFileData);
			createPositiveCoopDto.setUploadFileName((String) fileOutputData.get("uploadFileName"));
			createPositiveCoopDto.setViewPath(NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH);
			// 解放
			writeFileData = null;
		
		} else {
			endFlg = SkfCommonConstant.ABNORMAL;
			ServiceHelper.addErrorResultMessage(createPositiveCoopDto, null, MessageIdConstant.E_SKF_1079);
		}
		
		//トランザクションCの開始
		//終了処理
		skf3050Sc002SharedService.endCreatePositiveDataProc(dataMap.get(Skf3050Sc002SharedService.COMPANY_CD_KEY), endFlg);
		
		//▼再表示処理
		reDisplay(createPositiveCoopDto);
		
		return createPositiveCoopDto;
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

		LogUtils.info(MessageIdConstant.I_SKF_1022, Skf3050Sc002SharedService.CREATE_POSITIVE_DATA_BATCH_NAME);

		String shoriNengetsu = CodeConstant.HYPHEN;

		if (prm.size() >= Skf3050Sc002SharedService.PARAMETER_NUM) {
			shoriNengetsu = prm.get(Skf3050Sc002SharedService.SHORI_NENGETSU_KEY);
		}

		String log_2 = "対象年月：" + shoriNengetsu;
		LogUtils.infoByMsg(log_2);

		Date sysDate = skf3050Sc002SharedService.getSystemDate();
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

		Date sysDate = skf3050Sc002SharedService.getSystemDate();
		String strSysDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(sysDate);

		LogUtils.infoByMsg("処理終了時間：" + strSysDate);
		LogUtils.info(MessageIdConstant.I_SKF_1023, Skf3050Sc002SharedService.CREATE_POSITIVE_DATA_BATCH_NAME);
	}
	
	/**
	 * 画面の再表示を行う。
	 * 
	 * @param inDto Skf3050Sc002CreatePositiveCooperationDataDto
	 * @return Skf3050Sc002CreatePositiveCooperationDataDto
	 */
	private void reDisplay(Skf3050Sc002CommonDto inDto) {
		
		String targetYyyymm = inDto.getHdnSelectedTaisyonendo();

		List<Map<String, Object>> gridList = skf3050Sc002SharedService.createGetsujiGrid(targetYyyymm);
		inDto.setGetujiGrid(gridList);

		skf3050Sc002SharedService.getJikkoushijiHighlightData(inDto, inDto.getHdnJikkouShijiYoteiNengetsu());

		skf3050Sc002SharedService.setBtnMsg(inDto);

		skf3050Sc002SharedService.changeButtonStatus(inDto);
	}

}
