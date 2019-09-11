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
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtils;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc001.Skf2060Sc001AttachedAsyncDto;

/**
 * TestPrjTop画面のAttachedAsyncサービス処理クラス。
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
	 * @param attachedDto DTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2060Sc001AttachedAsyncDto index(Skf2060Sc001AttachedAsyncDto attachedDto) throws Exception {

		// 操作ログを出力
		//skfOperationLogUtils.setAccessLog("再提示", companyCd, initDto.getPageId());
		//セッションキーの設定
		String sessionKey = SessionCacheKeyConstant.KARIAGE_ATTACHED_FILE_SESSION_KEY + attachedDto.getCandidateNo();

		//添付ファイルを取得し、セッションに保存
		List<Map<String, Object>> fileDataList = skfAttachedFileUtils.getAttachedFileInfo(menuScopeSessionBean, null,
				sessionKey);
		//添付ファイルが存在しなかった場合、初期化
		if (fileDataList == null) {
			fileDataList = new ArrayList<Map<String, Object>>();
		}

		// 添付ファイル情報を削除
		int deleteCount = 0;
		Skf2060Sc001DeleteAttachedFileExp deleteData = new Skf2060Sc001DeleteAttachedFileExp();
		deleteData.setCompanyCd(companyCd);
		deleteData.setCandidateNo(attachedDto.getCandidateNo());
		deleteCount = skf2060Sc001DeleteAttachedFileExpRepository.deleteAttachedFile(deleteData);
		//削除できなかった場合
		if (deleteCount < 0) {
			// TODO エラー
			ServiceHelper.addErrorResultMessage(attachedDto, null, MessageIdConstant.E_SKF_2003);
			throwBusinessExceptionIfErrors(attachedDto.getResultMessages());
		}

		int attachedNo = 1;

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
				ServiceHelper.addErrorResultMessage(attachedDto, null, MessageIdConstant.E_SKF_2003);
				throwBusinessExceptionIfErrors(attachedDto.getResultMessages());
			}

			//リンクタグを作成
			String linkTag = skf2060Sc001SharedService.getLinkTag(insertData.getCandidateNo().toString(), insertData.getAttachedNo().toString(), insertData.getAttachedName());

			//リンクタグリスト作成
			linkTagList.add(linkTag);

			attachedNo++;

		}
		//リンクタグリストの間に改行コードをはさむ
		attachedDto.setAttachedFileLink(String.join("<br />", linkTagList));

		//セッションスコープを消去する？
		menuScopeSessionBean.remove(SessionCacheKeyConstant.KARIAGE_ATTACHED_FILE_SESSION_KEY);

		return attachedDto;
	}

}
