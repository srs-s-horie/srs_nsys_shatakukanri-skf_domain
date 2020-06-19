/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc006;


import static jp.co.c_nexco.nfw.core.constants.CommonConstant.NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc006.Skf3010Sc006AttachedDownloadDto;

/**
* Skf3010Sc006AttachedDownloadService　借上社宅登録ファイルダウンロード処理
* 
* @author NEXCOシステムズ
*
*/
@Service
public class Skf3010Sc006AttachedDownloadService extends SkfServiceAbstract<Skf3010Sc006AttachedDownloadDto> {


	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	// 社宅補足リンクプレフィックス
	private static final String SHATAKU_HOSOKU_LINK = "shataku";
	// 駐車場補足プレフィックス
	private static final String PARKING_HOSOKU_LINK = "parking";
	
	/**
	 * サービス処理を行う。　
	 * 
	 * @param adlDto　DTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public BaseDto index(Skf3010Sc006AttachedDownloadDto adlDto) throws Exception {
		
		String hosoku = adlDto.getHdnHosoku();
		String attachedNo = adlDto.getHdnAttachedNo();
		String fileName = "";
		byte[] fileData = null;
		
		if(SHATAKU_HOSOKU_LINK.equals(hosoku)){
			//社宅
			switch(attachedNo){
				case "1":
					fileName = adlDto.getShatakuHosokuFileName1();
					fileData = adlDto.getShatakuHosokuFile1();
					break;
				case "2":
					fileName = adlDto.getShatakuHosokuFileName2();
					fileData = adlDto.getShatakuHosokuFile2();
					break;
				case "3":
					fileName = adlDto.getShatakuHosokuFileName3();
					fileData = adlDto.getShatakuHosokuFile3();
					break;
			}
		}else if(PARKING_HOSOKU_LINK.equals(hosoku)){
			//駐車場
			switch(attachedNo){
			case "1":
				fileName = adlDto.getParkingHosokuFileName1();
				fileData = adlDto.getParkingHosokuFile1();
				break;
			case "2":
				fileName = adlDto.getParkingHosokuFileName2();
				fileData = adlDto.getParkingHosokuFile2();
				break;
			case "3":
				fileName = adlDto.getParkingHosokuFileName3();
				fileData = adlDto.getParkingHosokuFile3();
				break;
			}
		}
		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog(fileName, CodeConstant.C001, FunctionIdConstant.SKF3010_SC006);
		
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
