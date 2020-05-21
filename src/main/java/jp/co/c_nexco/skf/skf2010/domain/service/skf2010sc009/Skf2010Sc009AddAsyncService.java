/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc009;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.SkfAsyncServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc009.Skf2010Sc009AddAsyncDto;

/**
 * Skf2010Sc009 添付資料入力支援ファイル追加処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc009AddAsyncService extends SkfAsyncServiceAbstract<Skf2010Sc009AddAsyncDto> {

	@Autowired
	private Skf2010Sc009SharedService skf2010Sc009SharedService;
	@Autowired
	private MenuScopeSessionBean menuScopeSessionBean;
	@Autowired
	private SkfAttachedFileUtils skfAttachedFileUtiles;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	private String sessionKey = SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY;

	@Value("${skf2010.skf2010_sc009.max_search_count}")
	private String maxSearchCount;
	@Value("${skf2010.skf2010_sc009.max_file_size}")
	private String maxFileSize;
	@Value("${skf.common.validate_error}")
	private String validationErrorCode;

	/**
	 * サービス処理を行う。
	 * 
	 * @param addDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@SuppressWarnings("unchecked")
	@Override
	public AsyncBaseDto index(Skf2010Sc009AddAsyncDto addDto) throws Exception {

		// 操作ログ出力
		skfOperationLogUtils.setAccessLog("添付資料追加処理", CodeConstant.C001, FunctionIdConstant.SKF2010_SC009);

		String applId = addDto.getPopApplId();
		String applNo = addDto.getPopApplNo();
		String candidateNo = addDto.getPopCandidateNo();
		String applName = skf2010Sc009SharedService.getBaseScreenName(applId);
		addDto.setPopApplName(applName);

		MultipartFile fileData = addDto.getAttachedFile();

		sessionKey = SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY;
		if (CheckUtils.isEqual(applId, FunctionIdConstant.R0106)) {
			sessionKey = SessionCacheKeyConstant.KARIAGE_ATTACHED_FILE_SESSION_KEY + candidateNo;
		}

		// ファイル名の妥当性判定
		if (fileData == null) {
			ServiceHelper.addErrorResultMessage(addDto, new String[] { "attachedFile" }, MessageIdConstant.E_SKF_1048,
					"添付資料");
			throwBusinessExceptionIfErrors(addDto.getResultMessages());
		}
		String fileName = fileData.getOriginalFilename();
		boolean validateRes = validateFileName(fileName, applId, addDto);
		if (!validateRes) {
			addDto.setErrorAttachedFile(validationErrorCode);
			throwBusinessExceptionIfErrors(addDto.getResultMessages());
		}

		// 作業用添付資料情報に格納
		byte[] fileStream = fileData.getBytes();

		boolean errFlag = false;

		// バイト数が0の場合はエラー
		if (fileStream.length <= 0) {
			addDto.setErrorAttachedFile(validationErrorCode);
			ServiceHelper.addErrorResultMessage(addDto, null, MessageIdConstant.E_SKF_2003);
			errFlag = true;
		}

		// 同名ファイルの確認
		if (!errFlag && existFileNameGridView(fileName)) {
			addDto.setErrorAttachedFile(validationErrorCode);
			ServiceHelper.addErrorResultMessage(addDto, null, MessageIdConstant.E_SKF_1038);
			errFlag = true;
		}
		// バイト数判定
		long byteNum = fileData.getSize();
		if (!errFlag && byteNum > Long.parseLong(maxFileSize)) {
			ServiceHelper.addErrorResultMessage(addDto, null, MessageIdConstant.E_SKF_1039);
			errFlag = true;
		}

		// 添付ファイル情報の取得
		List<Map<String, Object>> attachedFileList = (List<Map<String, Object>>) menuScopeSessionBean.get(sessionKey);
		if (!errFlag && (attachedFileList != null && attachedFileList.size() >= Integer.parseInt(maxSearchCount))) {
			ServiceHelper.addErrorResultMessage(addDto, null, MessageIdConstant.E_SKF_1092, maxSearchCount);
		}
		throwBusinessExceptionIfErrors(addDto.getResultMessages());

		// Sessionのワークに追加
		String fileSize = String.valueOf(byteNum);
		String fileType = skfAttachedFileUtiles.getFileTypeInfo(fileName);

		List<Map<String, Object>> listTableDataList = skf2010Sc009SharedService.setAttachedFileList(sessionKey, applNo,
				fileName, fileStream, fileSize, fileType);

		// グリッドビューのバインド
		addDto.setPopAttachedFileList(skf2010Sc009SharedService.createListTableData(listTableDataList));

		// ファイル情報は返り値には不要なので削除
		addDto.setAttachedFile(null);

		return addDto;
	}

	/**
	 * 同一ファイルが既に添付されていないかチェックします
	 * 
	 * @param fileName
	 * @param applNo
	 * @return
	 */
	private boolean existFileNameGridView(String fileName) {
		// 添付ファイルリストを取得する
		List<Map<String, Object>> attachedFileList = skf2010Sc009SharedService.getAttachedFileInfo(sessionKey);
		if (attachedFileList == null || attachedFileList.size() <= 0) {
			return false;
		}
		for (Map<String, Object> attachedFileDataMap : attachedFileList) {
			String attachedFileName = attachedFileDataMap.get("attachedName").toString();
			if (fileName.equals(attachedFileName)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * ファイル名の妥当性チェックを行います
	 * 
	 * @param fileName
	 * @param applId
	 * @param dto
	 * @return
	 */
	private boolean validateFileName(String fileName, String applId, Skf2010Sc009AddAsyncDto dto) {
		if (fileName == null || CheckUtils.isEmpty(fileName)) {
			// ファイルなし
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1040);
			return false;
		}

		// 拡張子チェック
		String extension = SkfAttachedFileUtils.getExtension(fileName);
		if (CheckUtils.isEmpty(extension)) {
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1026);
			return false;
		}

		// ファイルタイプが使用可能かチェック
		switch (extension) {
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
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1026);
			return false;
		}

		return true;
	}

}
