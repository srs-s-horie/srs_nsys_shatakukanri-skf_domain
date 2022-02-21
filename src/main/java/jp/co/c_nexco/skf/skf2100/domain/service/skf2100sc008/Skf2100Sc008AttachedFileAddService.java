/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc008;

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
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc008.Skf2100Sc008AttachedFileAddDto;
/**
 * Skf3010Sc006AttachedFileAddService 補足ファイル追加処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2100Sc008AttachedFileAddService extends SkfServiceAbstract<Skf2100Sc008AttachedFileAddDto> {

	@Autowired
	private SkfAttachedFileUtils skfAttachedFileUtiles;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfDropDownUtils skfDropDownUtils;

	@Value("${skf2100.hosoku_max_file_size}")
	private String maxFileSize;

	// 補足リンクプレフィックス
	private static final String HOSOKU_LINK = "attached";
	
	/**
	 * サービス処理を行う。
	 * 
	 * @param addDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2100Sc008AttachedFileAddDto index(Skf2100Sc008AttachedFileAddDto addDto) throws Exception {

		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("補足資料追加", CodeConstant.C001, FunctionIdConstant.SKF2100_SC008);
		
		//画面入力内容情報の保持
		// ドロップダウンリストを設定
		List<Map<String, Object>> contractKbnDropDownList = skfDropDownUtils
				.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_ROUTER_CONTRACT_KBN, addDto.getContractKbnSelect(), true);
		addDto.setContractKbnDropDownList(contractKbnDropDownList);
		if(!"true".equals(addDto.getHdnChkFaultSelect())){
			addDto.setHdnChkFaultSelect(null);
		}
				
		// ファイル名
		String fileName = CodeConstant.DOUBLE_QUOTATION;
		// リンクID
		String hosokuLink = CodeConstant.DOUBLE_QUOTATION;
		
		boolean errFlag = false;
		// 通しNo
		String routerNo = addDto.getRouterNo();
		// ファイル番号
		String fileNo = addDto.getFileNo();
		// ファイル情報
		MultipartFile fileData = null;
		
		// ファイル情報取
		switch (fileNo){
		case "1":
			fileData = addDto.getTmpFileBox1();
			break;
		case "2":
			fileData = addDto.getTmpFileBox2();
			break;
		case "3":
			fileData = addDto.getTmpFileBox3();
			break;
		}

		
		
		if (fileData == null) {
			ServiceHelper.addErrorResultMessage(addDto, null, MessageIdConstant.E_SKF_2003);
			return addDto;
		}
		
		fileName = fileData.getOriginalFilename();
		// ファイル名の妥当性判定
		if(!validateFileName(fileName, fileNo , addDto)){
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
		if(errFlag){
			return addDto;
		}
		//throwBusinessExceptionIfErrors(addDto.getResultMessages());
		
		// ファイルサイズ
		String fileSize = String.valueOf(byteNum);
		
		// リンクID生成
		hosokuLink = HOSOKU_LINK + "_" + routerNo + "_" + fileNo;

		
		// ファイル情報をDTOに設定

		switch (fileNo){
		case "1":
			addDto.setHosokuFileName1(fileName);
			addDto.setHosokuLink1(hosokuLink);
			addDto.setHosokuSize1(fileSize);
			addDto.setHosokuFile1(fileStream);
			break;
		case "2":
			addDto.setHosokuFileName2(fileName);
			addDto.setHosokuLink2(hosokuLink);
			addDto.setHosokuSize2(fileSize);
			addDto.setHosokuFile2(fileStream);
			break;
		case "3":
			addDto.setHosokuFileName3(fileName);
			addDto.setHosokuLink3(hosokuLink);
			addDto.setHosokuSize3(fileSize);
			addDto.setHosokuFile3(fileStream);
			break;
		}

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
	private boolean validateFileName(String fileName, String fileNo ,Skf2100Sc008AttachedFileAddDto dto) {
		
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

		fileName1 = (SkfCheckUtils.isNullOrEmpty(dto.getHosokuFileName1())) ? "" : dto.getHosokuFileName1();
		fileName2 = (SkfCheckUtils.isNullOrEmpty(dto.getHosokuFileName2())) ? "" : dto.getHosokuFileName2();
		fileName3 = (SkfCheckUtils.isNullOrEmpty(dto.getHosokuFileName3())) ? "" : dto.getHosokuFileName3();
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
		
		if(sameName){
			//同名ファイル有の場合エラー
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1038);
			return false;
		}
		
		
		// 拡張子チェック
		String extension = SkfAttachedFileUtils.getExtension(fileName);
		if (CheckUtils.isEmpty(extension)) {
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1026,"添付資料");
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
				ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1026,"添付資料");
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
