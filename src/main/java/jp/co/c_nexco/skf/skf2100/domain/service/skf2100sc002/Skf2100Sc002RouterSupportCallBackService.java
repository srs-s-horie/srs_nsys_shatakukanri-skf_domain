/*
 * Copyright(c) 2021 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc002;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc002.Skf2100Sc002RouterSupportCallBackDto;

/**
 * Skf2100Sc002 モバイルルーター借用希望申請書（アウトソース要)確認依頼処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2100Sc002RouterSupportCallBackService extends SkfServiceAbstract<Skf2100Sc002RouterSupportCallBackDto> {

	@Autowired
	private Skf2100Sc002SharedService skf2100Sc002SharedService;

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfAttachedFileUtils skfAttachedFileUtiles;
	
	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2100Sc002RouterSupportCallBackDto index(Skf2100Sc002RouterSupportCallBackDto dto) throws Exception {
		// 操作ログ出力
		skfOperationLogUtils.setAccessLog("ルーター選択", CodeConstant.C001, FunctionIdConstant.SKF2100_SC002);
		
		skf2100Sc002SharedService.setMenuScopeSessionBean(menuScopeSessionBean);
		
		// セッション添付資料情報初期化
		skf2100Sc002SharedService.clearMenuScopeSessionBean();
		
		// 申請情報設定
		Map<String, String> applInfo = new HashMap<String, String>();
		applInfo.put("status", dto.getApplStatus());
		applInfo.put("applNo", dto.getApplNo());
		applInfo.put("applId", dto.getApplId());

		//可変ラベル値
		skf2100Sc002SharedService.setVariableLabel(skf2100Sc002SharedService.jsonArrayToArrayList(dto.getJsonLabelList()), dto);

		
		// ドロップダウンの設定
		// ドロップダウンリスト
		List<Map<String, Object>> originalCompanySelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> payCompanySelectList = new ArrayList<Map<String, Object>>();
		
		skf2100Sc002SharedService.setDdlControlValues( dto.getOriginalCompanyCd(), originalCompanySelectList,
				 dto.getPayCompanyCd(), payCompanySelectList);
		 dto.setOriginalCompanySelectList(originalCompanySelectList);
		 dto.setPayCompanySelectList(payCompanySelectList);
		
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
