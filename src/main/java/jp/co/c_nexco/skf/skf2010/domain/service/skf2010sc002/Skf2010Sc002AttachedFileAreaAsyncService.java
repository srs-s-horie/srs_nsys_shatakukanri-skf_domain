package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc002;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc002.Skf2010Sc002AttachedFileAreaAsyncDto;

/**
 * Skf2010Sc002 申請書類確認の添付ファイルエリア非同期処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc002AttachedFileAreaAsyncService
		extends AsyncBaseServiceAbstract<Skf2010Sc002AttachedFileAreaAsyncDto> {

	@Autowired
	private MenuScopeSessionBean menuScopeSessionBean;
	@Autowired
	private Skf2010Sc002SharedService skf2010Sc002SharedService;

	@Value("${skf.common.attached_file_session_key}")
	private String sessionKey;

	@SuppressWarnings("unchecked")
	@Override
	protected AsyncBaseDto index(Skf2010Sc002AttachedFileAreaAsyncDto dto) throws Exception {

		// 添付ファイル情報の取得
		String applNo = dto.getApplNo();
		List<Map<String, Object>> attachedFileList = skf2010Sc002SharedService.getAttachedFileInfo(applNo);

		String baseLinkTag = "<a id=\"attached_$ATTACHEDNO$\">$ATTACHEDNAME$</a>";
		List<String> listTagList = new ArrayList<String>();

		// リンクタグの作成
		if (attachedFileList != null && attachedFileList.size() > 0) {
			int attachedNo = attachedFileList.size();
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
