/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc002;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc002.Skf2060Sc002AttachedDownloadDto;

import static jp.co.c_nexco.nfw.core.constants.CommonConstant.NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH;

/**
 * 借上候補物件確認画面のAttachedDownloadサービス処理クラス。　 
 * 
 */
@Service
public class Skf2060Sc002AttachedDownloadService extends BaseServiceAbstract<Skf2060Sc002AttachedDownloadDto> {

	@Autowired
	private SkfAttachedFileUtils skfAttachedFileUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	// 会社コード
	private String companyCd = CodeConstant.C001;
	

	/**
	 * サービス処理を行う。　
	 * 
	 * @param adlDto　DTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2060Sc002AttachedDownloadDto index(Skf2060Sc002AttachedDownloadDto adlDto) throws Exception {
		
		long candidateNo = Long.parseLong(adlDto.getHdnCandidateNo());
		String attachedNo = adlDto.getHdnAttachedNo();
		String sessionKey = SessionCacheKeyConstant.KARIAGE_ATTACHED_FILE_SESSION_KEY+adlDto.getHdnCandidateNo()+"_"+attachedNo;
		
		//セッションキーから添付ファイル情報を取得
		List<Map<String, Object>> attachedFileList = new ArrayList<Map<String, Object>>();
		attachedFileList = skfAttachedFileUtils.getKariageTeijiFileInfo(menuScopeSessionBean, candidateNo, attachedNo, sessionKey);
		
		//添付ファイル情報が取得できなかった場合
		if(!(attachedFileList != null && attachedFileList.size() > 0)){
			//エラーメッセージを表示し終了
			ServiceHelper.addErrorResultMessage(adlDto, null, MessageIdConstant.E_SKF_1040);
			throwBusinessExceptionIfErrors(adlDto.getResultMessages());
		}
		
		Map<String, Object> attachedFile = attachedFileList.get(0);
		
		//ファイルデータ、ファイル名、パスの設定
		adlDto.setFileData((byte[])attachedFile.get("fileStream"));
		adlDto.setUploadFileName(attachedFile.get("attachedName").toString());
		adlDto.setViewPath(NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH);
		
		return adlDto;
	}
	
}