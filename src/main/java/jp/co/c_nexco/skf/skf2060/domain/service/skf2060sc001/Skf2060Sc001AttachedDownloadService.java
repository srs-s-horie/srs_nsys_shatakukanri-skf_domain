/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc001;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.nfw.webcore.utils.filetransfer.FileOutput;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtiles;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc001.Skf2060Sc001AttachedDownloadDto;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc001.Skf2060Sc001DownloadDto;

import static jp.co.c_nexco.nfw.core.constants.CommonConstant.NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH;

/**
 * TestPrjTop画面のInitサービス処理クラス。　 
 * 
 */
@Service
public class Skf2060Sc001AttachedDownloadService extends BaseServiceAbstract<Skf2060Sc001AttachedDownloadDto> {

	@Autowired
	private Skf2060Sc001SharedService skf2060Sc001SharedService;
	@Autowired
	private SkfAttachedFileUtiles skfAttachedFileUtiles;
	
	private String companyCd = CodeConstant.C001;
	private String fileName = "skf2060.skf2060_sc001.FileId";
	

	/**
	 * サービス処理を行う。　
	 * 
	 * @param initDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public Skf2060Sc001AttachedDownloadDto index(Skf2060Sc001AttachedDownloadDto adlDto) throws Exception {
		
		//TODO 操作ログ
		
		long candidateNo = adlDto.getHdnCandidateNo();
		String attachedNo = adlDto.getHdnAttachedNo();
		
		List<Map<String, Object>> attachedFileList = new ArrayList<Map<String, Object>>();
		attachedFileList = skfAttachedFileUtiles.getKariageBukkenFileInfo(menuScopeSessionBean, candidateNo, attachedNo, SessionCacheKeyConstant.KARIAGE_ATTACHED_FILE_SESSION_KEY+attachedNo);
		
		if(!(attachedFileList != null && attachedFileList.size() > 0)){
			ServiceHelper.addErrorResultMessage(adlDto, null, MessageIdConstant.E_SKF_1040);
			throwBusinessExceptionIfErrors(adlDto.getResultMessages());
		}
		
		Map<String, Object> attachedFile = attachedFileList.get(0);
		
		adlDto.setFileData((byte[])attachedFile.get("fileStream"));
		adlDto.setUploadFileName(attachedFile.get("attachedName").toString());
		adlDto.setViewPath(NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH);
			
		return adlDto;
	}
	
}
