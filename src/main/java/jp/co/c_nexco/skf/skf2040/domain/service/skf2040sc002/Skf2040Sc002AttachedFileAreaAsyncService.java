/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.service.skf2040sc002;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtils;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040sc002.Skf2040Sc002AttachedFileAreaAsyncDto;

/**
 * Skf2040Sc002 添付資料追加時の非同期クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2040Sc002AttachedFileAreaAsyncService
		extends AsyncBaseServiceAbstract<Skf2040Sc002AttachedFileAreaAsyncDto> {

	@Autowired
	private SkfAttachedFileUtils skfAttachedFileUtiles;

	@Override
	public AsyncBaseDto index(Skf2040Sc002AttachedFileAreaAsyncDto dto) {

		// 申請書類番号
		String applNo = dto.getApplNo();

		// 一般添付資料取得
		List<Map<String, Object>> attachedFileList = skfAttachedFileUtiles.getAttachedFileInfo(menuScopeSessionBean,
				applNo, SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY);

		String baseLinkTag = "<a id=\"attached_$ATTACHEDNO$\">$ATTACHEDNAME$</a>";
		List<String> listTagList = new ArrayList<String>();

		// 添付ファイルがあればリンクタグを生成する
		if (attachedFileList != null && attachedFileList.size() > 0) {
			int attachedNo = 0;
			for (Map<String, Object> attachedFileMap : attachedFileList) {
				String linkTag = baseLinkTag;
				linkTag = linkTag.replace("$ATTACHEDNO$", String.valueOf(attachedNo));
				linkTag = linkTag.replace("$ATTACHEDNAME$", attachedFileMap.get("attachedName").toString());
				listTagList.add(linkTag);
				attachedNo++;
			}
		}

		dto.setAttachedFileArea(String.join("&nbsp;", listTagList));
		return dto;
	}
}
