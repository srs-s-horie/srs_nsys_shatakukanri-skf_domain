/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc002;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.skf.common.SkfAsyncServiceAbstract;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc002.Skf2100Sc002RouterSupportCallBackAsyncDto;

/**
 * Skf2100Sc002 モバイルルーター選択支援画面完了後の非同期クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2100Sc002RouterSupportCallBackAsyncService
		extends SkfAsyncServiceAbstract<Skf2100Sc002RouterSupportCallBackAsyncDto> {

	@Autowired
	private SkfAttachedFileUtils skfAttachedFileUtiles;
	@Autowired
	private Skf2100Sc002SharedService skf2100Sc002SharedService;

	@Override
	public AsyncBaseDto index(Skf2100Sc002RouterSupportCallBackAsyncDto dto) {
				
		// 申請書類番号
		String applNo = dto.getApplNo();
		// 通しNo
		Long routerNo = dto.getMobileRouterNo();
		if(routerNo == null){
			return dto;
		}
		
		// モバイルルーターマスタの添付資料取得
		skf2100Sc002SharedService.getSupplementInfo(applNo, routerNo);
		
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
		dto.setHdnMobileRouterNo(routerNo);
		return dto;
	}
	

}
