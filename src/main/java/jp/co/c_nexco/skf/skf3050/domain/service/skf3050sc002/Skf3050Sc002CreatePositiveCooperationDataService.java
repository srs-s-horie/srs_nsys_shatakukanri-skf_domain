/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3050.domain.service.skf3050sc002;

import static jp.co.c_nexco.nfw.core.constants.CommonConstant.NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.base.Objects;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.skf3050.domain.dto.skf3050sc002.Skf3050Sc002CreatePositiveCooperationDataDto;

/**
 * Skf3050Sc002CreatePositiveCooperationDataService 月次運用管理画面のPOSITIVE連携データ作成処理
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3050Sc002CreatePositiveCooperationDataService extends SkfServiceAbstract<Skf3050Sc002CreatePositiveCooperationDataDto> {

	@Autowired
	private Skf3050Sc002SharedService skf3050Sc002SharedService;

	@Value("${skf3050.skf3050_bt003.batch_prg_id}")
	private String batchPrgId;

	@Override
	protected BaseDto index(Skf3050Sc002CreatePositiveCooperationDataDto createPositiveCoopDto) throws Exception {

		//給与連携データ作成
		String endFlg = SkfCommonConstant.COMPLETE;
		Map<String, Object> fileOutputData = null;
		createPositiveCoopDto.setResultMessages(null);
		String jikkouShijiYoteiNengetsu = createPositiveCoopDto.getHdnJikkouShijiYoteiNengetsu();

		//▼連携データ作成処理バッチを起動
		Map<String, String> dataMap = new HashMap<>();
		dataMap.put(Skf3050Sc002SharedService.BATCH_PRG_ID_KEY, batchPrgId);
		dataMap.put(Skf3050Sc002SharedService.COMPANY_CD_KEY, CodeConstant.C001);
		dataMap.put(Skf3050Sc002SharedService.USER_ID_KEY, skf3050Sc002SharedService.getUserId());
		dataMap.put(Skf3050Sc002SharedService.SHORI_NENGETSU_KEY, jikkouShijiYoteiNengetsu);

		try {
			// トランザクションB
			fileOutputData = skf3050Sc002SharedService.createPositiveCooperationData(dataMap);
		} catch (Exception e) {
			endFlg = SkfCommonConstant.ABNORMAL;
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
		skf3050Sc002SharedService.endProc(endFlg,dataMap.get(Skf3050Sc002SharedService.COMPANY_CD_KEY), batchPrgId, SkfCommonConstant.PROCESSING);
		skf3050Sc002SharedService.outputEndProcLog(Skf3050Sc002SharedService.CREATE_POSITIVE_DATA_BATCH_NAME);
		// 画面リフレッシュ
		if (Objects.equal(SkfCommonConstant.ABNORMAL, endFlg)) {
			TransferPageInfo nextPage = TransferPageInfo.prevPage(FunctionIdConstant.SKF3050_SC002, "init");
			createPositiveCoopDto.setTransferPageInfo(nextPage);
		}
		return createPositiveCoopDto;
	}
}
