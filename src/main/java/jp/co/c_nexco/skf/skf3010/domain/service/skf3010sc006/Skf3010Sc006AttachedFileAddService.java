/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc006;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtils;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc006.Skf3010Sc006AttachedFileAddDto;

/**
 * Skf3010Sc006AttachedFileAddService 補足ファイル追加処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf3010Sc006AttachedFileAddService extends SkfServiceAbstract<Skf3010Sc006AttachedFileAddDto> {

	@Autowired
	private Skf3010Sc006SharedService skf3010Sc006SharedService;
	@Autowired
	private SkfAttachedFileUtils skfAttachedFileUtiles;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	@Value("${skf2010.skf2010_sc009.max_search_count}")
	private String maxSearchCount;
	@Value("${skf3010.hosoku_max_file_size}")
	private String maxFileSize;
	@Value("${skf.common.validate_error}")
	private String validationErrorCode;

	// 社宅補足リンクプレフィックス
	private static final String SHATAKU_HOSOKU_LINK = "attached_shataku";
	// 駐車場補足プレフィックス
	private static final String PARKING_HOSOKU_LINK = "attached_parking";
	// 社宅補足
	private static final String SHATAKU_HOSOKU = "shataku";
	// 駐車場補足
	private static final String PARKING_HOSOKU = "parking";
	
	/**
	 * サービス処理を行う。
	 * 
	 * @param addDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf3010Sc006AttachedFileAddDto index(Skf3010Sc006AttachedFileAddDto addDto) throws Exception {

		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("補足資料追加", CodeConstant.C001, FunctionIdConstant.SKF3010_SC006);
				
		//ファイル名
		String fileName = CodeConstant.DOUBLE_QUOTATION;
		//リンクID
		String hosokuLink = CodeConstant.DOUBLE_QUOTATION;
		
		boolean errFlag = false;
		//社宅管理番号
		String shatakuKanriNo = addDto.getHdnShatakuKanriNo();
		//ファイル番号
		String fileNo = addDto.getFileNo();
		//補足種別
		String hosokuType = addDto.getHosokuType();
		//ファイル情報
		MultipartFile fileData = null;
		
		//ファイル情報取得
		if(SHATAKU_HOSOKU.equals(hosokuType)){
			//社宅
			switch (fileNo){
			case "1":
				fileData = addDto.getTmpFileBoxshataku1();
				break;
			case "2":
				fileData = addDto.getTmpFileBoxshataku2();
				break;
			case "3":
				fileData = addDto.getTmpFileBoxshataku3();
				break;
			}
		}else if(PARKING_HOSOKU.equals(hosokuType)){
			//駐車場
			switch (fileNo){
			case "1":
				fileData = addDto.getTmpFileBoxparking1();
				break;
			case "2":
				fileData = addDto.getTmpFileBoxparking2();
				break;
			case "3":
				fileData = addDto.getTmpFileBoxparking3();
				break;
			}
		}
		
		
		if (fileData == null) {
			ServiceHelper.addErrorResultMessage(addDto, null, MessageIdConstant.E_SKF_2003);
			throwBusinessExceptionIfErrors(addDto.getResultMessages());
		}
		
		fileName = fileData.getOriginalFilename();
		// ファイル名の妥当性判定
		if(!validateFileName(fileName, hosokuType, fileNo , addDto)){
			errFlag = true;
		}

		// 作業用添付資料情報に格納
		byte[] fileStream = fileData.getBytes();
				
		// バイト数が0の場合はエラー
		if (!errFlag && fileStream.length <= 0) {
			ServiceHelper.addErrorResultMessage(addDto, null, MessageIdConstant.E_SKF_2003);
			errFlag = true;
		}

		// バイト数判定
		long byteNum = fileData.getSize();
		if (!errFlag && byteNum > Long.parseLong(maxFileSize)) {
			ServiceHelper.addErrorResultMessage(addDto, null, MessageIdConstant.E_SKF_1039);
			errFlag = true;
		}
		throwBusinessExceptionIfErrors(addDto.getResultMessages());
		
		//ファイルサイズ
		String fileSize = String.valueOf(byteNum);
		
		//リンクID生成
		if(SHATAKU_HOSOKU.equals(hosokuType)){
			hosokuLink = SHATAKU_HOSOKU_LINK + "_" + shatakuKanriNo + "_" + fileNo;
		}else if(PARKING_HOSOKU.equals(hosokuType)){
			hosokuLink = PARKING_HOSOKU_LINK + "_" + shatakuKanriNo + "_" + fileNo;
		}
		
		//ファイル情報をDTOに設定
		if(SHATAKU_HOSOKU.equals(hosokuType)){
			//社宅
			switch (fileNo){
			case "1":
				addDto.setShatakuHosokuFileName1(fileName);
				addDto.setShatakuHosokuLink1(hosokuLink);
				addDto.setShatakuHosokuSize1(fileSize);
				addDto.setShatakuHosokuFile1(fileStream);
				break;
			case "2":
				addDto.setShatakuHosokuFileName2(fileName);
				addDto.setShatakuHosokuLink2(hosokuLink);
				addDto.setShatakuHosokuSize2(fileSize);
				addDto.setShatakuHosokuFile2(fileStream);
				break;
			case "3":
				addDto.setShatakuHosokuFileName3(fileName);
				addDto.setShatakuHosokuLink3(hosokuLink);
				addDto.setShatakuHosokuSize3(fileSize);
				addDto.setShatakuHosokuFile3(fileStream);
				break;
			}
		}else if(PARKING_HOSOKU.equals(hosokuType)){
			//駐車場
			switch (fileNo){
			case "1":
				addDto.setParkingHosokuFileName1(fileName);
				addDto.setParkingHosokuLink1(hosokuLink);
				addDto.setParkingHosokuSize1(fileSize);
				addDto.setParkingHosokuFile1(fileStream);
				break;
			case "2":
				addDto.setParkingHosokuFileName2(fileName);
				addDto.setParkingHosokuLink2(hosokuLink);
				addDto.setParkingHosokuSize2(fileSize);
				addDto.setParkingHosokuFile2(fileStream);
				break;
			case "3":
				addDto.setParkingHosokuFileName3(fileName);
				addDto.setParkingHosokuLink3(hosokuLink);
				addDto.setParkingHosokuSize3(fileSize);
				addDto.setParkingHosokuFile3(fileStream);
				break;
			}
		}
		
		//画面入力内容情報の保持
		skf3010Sc006SharedService.setBeforeInfo(addDto);

		return addDto;
	}


	/**
	 * ファイルチェック
	 * （同名チェックも実施）
	 * @param fileName
	 * @param hosokuType
	 * @param fileNo
	 * @param dto
	 * @return
	 */
	private boolean validateFileName(String fileName, String hosokuType, String fileNo ,Skf3010Sc006AttachedFileAddDto dto) {
		
		String fileName1 = CodeConstant.DOUBLE_QUOTATION;
		String fileName2 = CodeConstant.DOUBLE_QUOTATION;
		String fileName3 = CodeConstant.DOUBLE_QUOTATION;
		
		if (fileName == null || CheckUtils.isEmpty(fileName)) {
			// ファイルなし
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1040);
			return false;
		}

		//同名ファイルチェック
		boolean sameName = false;
		if(SHATAKU_HOSOKU.equals(hosokuType)){
			
			fileName1 = (SkfCheckUtils.isNullOrEmpty(dto.getShatakuHosokuFileName1())) ? "" : dto.getShatakuHosokuFileName1();
			fileName2 = (SkfCheckUtils.isNullOrEmpty(dto.getShatakuHosokuFileName2())) ? "" : dto.getShatakuHosokuFileName2();
			fileName3 = (SkfCheckUtils.isNullOrEmpty(dto.getShatakuHosokuFileName3())) ? "" : dto.getShatakuHosokuFileName3();
			//社宅
			switch (fileNo){
			case "1":
				if(Objects.equals(fileName2, fileName) || Objects.equals(fileName3, fileName)){
					sameName = true;
				}
				break;
			case "2":
				if(Objects.equals(fileName1, fileName) || Objects.equals(fileName3, fileName)){
					sameName = true;
				}
				break;
			case "3":
				if(Objects.equals(fileName1, fileName) || Objects.equals(fileName2, fileName)){
					sameName = true;
				}
				break;
			}
		}else if(PARKING_HOSOKU.equals(hosokuType)){
			
			fileName1 = (SkfCheckUtils.isNullOrEmpty(dto.getParkingHosokuFileName1())) ? "" : dto.getParkingHosokuFileName1();
			fileName2 = (SkfCheckUtils.isNullOrEmpty(dto.getParkingHosokuFileName2())) ? "" : dto.getParkingHosokuFileName2();
			fileName3 = (SkfCheckUtils.isNullOrEmpty(dto.getParkingHosokuFileName3())) ? "" : dto.getParkingHosokuFileName3();
			//社宅
			switch (fileNo){
			case "1":
				if(Objects.equals(fileName2, fileName) || Objects.equals(fileName3, fileName)){
					sameName = true;
				}
				break;
			case "2":
				if(Objects.equals(fileName1, fileName) || Objects.equals(fileName3, fileName)){
					sameName = true;
				}
				break;
			case "3":
				if(Objects.equals(fileName1, fileName) || Objects.equals(fileName2, fileName)){
					sameName = true;
				}
				break;
			}
		}
		
		if(sameName){
			//同名ファイル有の場合エラー
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1038);
			return false;
		}
		

		
		// 拡張子チェック
		String extension = SkfAttachedFileUtils.getExtension(fileName);
		if (CheckUtils.isEmpty(extension)) {
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1042,"添付資料");
			return false;
		}
		switch (extension) {
			case CodeConstant.EXTENSION_JPG:
			case CodeConstant.EXTENSION_JPEG:
			case CodeConstant.EXTENSION_PNG:
			case CodeConstant.EXTENSION_BMP:
			case CodeConstant.EXTENSION_PDF:
			case CodeConstant.EXTENSION_XDW:
			case CodeConstant.EXTENSION_DOC:
			case CodeConstant.EXTENSION_DOCX:
			case CodeConstant.EXTENSION_XLS:
			case CodeConstant.EXTENSION_XLSX:
			case CodeConstant.EXTENSION_PPT:
			case CodeConstant.EXTENSION_PPTX:
				break;
			default:
				ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1042,"添付資料");
				return false;
		}


		return true;
	}

	public List<Map<String, Object>> addShatakuAttachedFile(String fileName, byte[] file, String fileSize, int attachedNo,
			List<Map<String, Object>> shatakuAttachedFileList) {
		// 添付資料のコレクションをSessionより取得

		// リンクリストチェック
		boolean findFlg = false;
		if (shatakuAttachedFileList != null) {
			for (Map<String, Object> attachedFileMap : shatakuAttachedFileList) {
				if (Objects.equals(fileName, attachedFileMap.get("attachedName"))) {
					findFlg = true;
					break;
				}
			}
		} else {
			shatakuAttachedFileList = new ArrayList<Map<String, Object>>();
		}

		// 添付ファイルリストに無い場合
		if (!findFlg) {
			Map<String, Object> addAttachedFileInfo = new HashMap<String, Object>();

			addAttachedFileInfo.put("attachedNo", attachedNo);

			// 添付資料名
			addAttachedFileInfo.put("attachedName", fileName);
			// ファイルサイズ
			addAttachedFileInfo.put("attachedFileSize", fileSize);
			// 更新日
			addAttachedFileInfo.put("registDate", new Date());
			// 添付資料
			addAttachedFileInfo.put("fileStream", file);
			// 添付ファイルステータス
			// ファイルタイプ
			addAttachedFileInfo.put("fileType", skfAttachedFileUtiles.getFileTypeInfo(fileName));

			shatakuAttachedFileList.add(addAttachedFileInfo);
		}

		return shatakuAttachedFileList;
	}

}
