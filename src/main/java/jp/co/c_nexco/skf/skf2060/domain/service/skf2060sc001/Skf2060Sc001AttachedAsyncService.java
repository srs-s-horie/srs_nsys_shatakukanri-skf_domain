/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc001;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001DeleteAttachedFileExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2060TKariageBukkenFile;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc001.Skf2060Sc001DeleteAttachedFileExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2060TKariageBukkenFileRepository;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtils;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc001.Skf2060Sc001AttachedAsyncDto;

/**
 * TestPrjTop画面のInitサービス処理クラス。
 * 
 */
@Service
public class Skf2060Sc001AttachedAsyncService extends AsyncBaseServiceAbstract<Skf2060Sc001AttachedAsyncDto> {

	@Autowired
	private Skf2060Sc001SharedService skf2060Sc001SharedService;
	@Autowired
	private SkfAttachedFileUtils skfAttachedFileUtils;
	@Autowired
	private Skf2060TKariageBukkenFileRepository skf2060TKariageBukkenFileRepository;
	@Autowired
	private Skf2060Sc001DeleteAttachedFileExpRepository skf2060Sc001DeleteAttachedFileExpRepository;

	private String companyCd = CodeConstant.C001;

	/**
	 * サービス処理を行う。
	 * 
	 * @param attachedDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2060Sc001AttachedAsyncDto index(Skf2060Sc001AttachedAsyncDto attachedDto) throws Exception {

		String sessionKey = SessionCacheKeyConstant.KARIAGE_ATTACHED_FILE_SESSION_KEY + attachedDto.getCandidateNo();
		List<Map<String, Object>> fileDataList = skfAttachedFileUtils.getAttachedFileInfo(menuScopeSessionBean, null,
				sessionKey);
		if (fileDataList == null) {
			fileDataList = new ArrayList<Map<String, Object>>();
		}

		// 添付ファイル情報を削除
		int deleteCount = 0;
		Skf2060Sc001DeleteAttachedFileExp deleteData = new Skf2060Sc001DeleteAttachedFileExp();
		deleteData.setCompanyCd(companyCd);
		deleteData.setCandidateNo(attachedDto.getCandidateNo());
		deleteCount = skf2060Sc001DeleteAttachedFileExpRepository.deleteAttachedFile(deleteData);

		if (deleteCount < 0) {
			// TODO エラー
			return attachedDto;
		}

		int attachedNo = 1;

		String baseLinkTag = "<a id=\"attached_$CANDIDATENO$_$ATACCHEDNO$\">$ATTACHEDNAME$</a>";
		List<String> linkTagList = new ArrayList<String>();
		// 添付ファイル情報を追加
		for (Map<String, Object> fileData : fileDataList) {
			Skf2060TKariageBukkenFile insertData = new Skf2060TKariageBukkenFile();
			insertData.setCompanyCd(companyCd);
			insertData.setCandidateNo(attachedDto.getCandidateNo());
			insertData.setAttachedName(fileData.get("attachedName").toString());
			insertData.setAttachedNo(String.valueOf(attachedNo));
			insertData.setFileSize(fileData.get("fileSize").toString());
			insertData.setFileStream((byte[]) fileData.get("fileStream"));

			int res = skf2060TKariageBukkenFileRepository.insertSelective(insertData);

			if (res <= 0) {
				// TODO エラー
				return attachedDto;
			}

			String linkTag = baseLinkTag;
			linkTag = linkTag.replace("$CANDIDATENO$", insertData.getCandidateNo().toString());
			linkTag = linkTag.replace("$ATACCHEDNO$", insertData.getAttachedNo().toString());
			linkTag = linkTag.replace("$ATTACHEDNAME$", insertData.getAttachedName());

			linkTagList.add(linkTag);

			attachedNo++;

		}

		attachedDto.setAttachedFileLink(String.join("<br />", linkTagList));

		menuScopeSessionBean.remove(SessionCacheKeyConstant.KARIAGE_ATTACHED_FILE_SESSION_KEY);

		return attachedDto;
	}

}
