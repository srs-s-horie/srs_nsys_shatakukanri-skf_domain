/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc008;


import static jp.co.c_nexco.nfw.core.constants.CommonConstant.NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc008.Skf2100Sc008AttachedFileDownloadDto;

/**
* Skf2100Sc008AttachedDownloadService 補足ファイルダウンロード処理
* 
* @author NEXCOシステムズ
*
*/
@Service
public class Skf2100Sc008AttachedFileDownloadService extends SkfServiceAbstract<Skf2100Sc008AttachedFileDownloadDto> {


	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	
	/**
	 * サービス処理を行う。　
	 * 
	 * @param adlDto　DTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public BaseDto index(Skf2100Sc008AttachedFileDownloadDto adlDto) throws Exception {
		
		String attachedNo = adlDto.getHdnAttachedNo();
		String fileName = "";
		byte[] fileData = null;
		

		switch(attachedNo){
			case "1":
				fileName = adlDto.getHosokuFileName1();
				fileData = adlDto.getHosokuFile1();
				break;
			case "2":
				fileName = adlDto.getHosokuFileName2();
				fileData = adlDto.getHosokuFile2();
				break;
			case "3":
				fileName = adlDto.getHosokuFileName3();
				fileData = adlDto.getHosokuFile3();
				break;
		}
		
		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog(fileName, CodeConstant.C001, FunctionIdConstant.SKF2100_SC008);
		
		if(fileName != null && fileData != null && fileData.length > 0){
			//ファイルデータ、ファイル名、パスの設定
			adlDto.setFileData(fileData);
			adlDto.setUploadFileName(fileName);
			adlDto.setViewPath(NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH);
		}else{
			ServiceHelper.addErrorResultMessage(adlDto, null, MessageIdConstant.E_SKF_1067, "補足資料");
		}
			
		return adlDto;
	}
	
}
