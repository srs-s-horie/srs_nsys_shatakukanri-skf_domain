/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc001;


import static jp.co.c_nexco.nfw.core.constants.CommonConstant.NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetKariageBukkenFileExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetKariageBukkenFileExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc001.Skf2060Sc001GetKariageBukkenFileExpRepository;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc001.Skf2060Sc001AttachedDownloadDto;

/**
 * TestPrjTop画面のAttachedDownloadサービス処理クラス。　 
 * 
 */
@Service
public class Skf2060Sc001AttachedDownloadService extends SkfServiceAbstract<Skf2060Sc001AttachedDownloadDto> {

	@Autowired
	private SkfAttachedFileUtils skfAttachedFileUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf2060Sc001GetKariageBukkenFileExpRepository skf2060Sc001GetKariageBukkenFileExpRepository;
	
	private String companyCd = CodeConstant.C001;

	

	/**
	 * サービス処理を行う。　
	 * 
	 * @param adlDto　DTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2060Sc001AttachedDownloadDto index(Skf2060Sc001AttachedDownloadDto adlDto) throws Exception {
		
		long candidateNo = adlDto.getHdnCandidateNo();
		String attachedNo = adlDto.getHdnAttachedNo();
		
		// 添付ファイル情報を取得
		List<Skf2060Sc001GetKariageBukkenFileExp> attachedFileList = new ArrayList<Skf2060Sc001GetKariageBukkenFileExp>();
		Skf2060Sc001GetKariageBukkenFileExpParameter param = new Skf2060Sc001GetKariageBukkenFileExpParameter();
		param.setCompanyCd(companyCd);
		param.setCandidateNo(candidateNo);
		param.setAttachedNo(attachedNo);
		attachedFileList = skf2060Sc001GetKariageBukkenFileExpRepository.getKariageBukkenFile(param);
		
		//添付ファイル情報が取得できなかった場合
		if(!(attachedFileList != null && attachedFileList.size() > 0)){
			//エラーメッセージを表示し終了
			ServiceHelper.addErrorResultMessage(adlDto, null, MessageIdConstant.E_SKF_1040);
			throwBusinessExceptionIfErrors(adlDto.getResultMessages());
		}
		
		Skf2060Sc001GetKariageBukkenFileExp attachedFile = attachedFileList.get(0);
		String fileName = attachedFile.getAttachedName();
		
		// 操作ログを出力
		skfOperationLogUtils.setAccessLog(fileName, CodeConstant.C001, FunctionIdConstant.SKF2060_SC001);
		
		//ファイルデータ、ファイル名、パスの設定
		adlDto.setFileData(attachedFile.getFileStream());
		adlDto.setUploadFileName(fileName);
		adlDto.setViewPath(NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH);
			
		return adlDto;
	}
	
}
