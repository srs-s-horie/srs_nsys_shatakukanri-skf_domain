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

import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.common.utils.PropertyUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc002.Skf2100Sc002PresentDto;

/**
 * Skf2100Sc002 モバイルルーター借用希望申請書（アウトソース要)確認依頼処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2100Sc002PresentService extends SkfServiceAbstract<Skf2100Sc002PresentDto> {

	@Autowired
	private Skf2100Sc002SharedService skf2100Sc002SharedService;

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private SkfAttachedFileUtils skfAttachedFileUtils;
	
	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2100Sc002PresentDto index(Skf2100Sc002PresentDto dto) throws Exception {
		// 操作ログ出力
		skfOperationLogUtils.setAccessLog("確認依頼", CodeConstant.C001, FunctionIdConstant.SKF2100_SC002);
		
		// ログインユーザー情報取得
		Map<String, String> loginUserInfo = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		skf2100Sc002SharedService.setMenuScopeSessionBean(menuScopeSessionBean);
		
		// 申請情報設定
		Map<String, String> applInfo = new HashMap<String, String>();
		applInfo.put("status", dto.getApplStatus());
		applInfo.put("applNo", dto.getApplNo());
		applInfo.put("applId", dto.getApplId());
		applInfo.put("shainNo", dto.getShainNo());

		
		if(dto.getHdnMobileRouterNo() != null){
			dto.setMobileRouterNo(dto.getHdnMobileRouterNo());
		}
		
		// ドロップダウンの設定
		// ドロップダウンリスト
		List<Map<String, Object>> originalCompanySelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> payCompanySelectList = new ArrayList<Map<String, Object>>();
		
		skf2100Sc002SharedService.setDdlControlValues( dto.getOriginalCompanyCd(), originalCompanySelectList,
				 dto.getPayCompanyCd(), payCompanySelectList);
		 dto.setOriginalCompanySelectList(originalCompanySelectList);
		 dto.setPayCompanySelectList(payCompanySelectList);
		 
		//複数タブによる添付ファイルセッションチェック		
		boolean checkResults = skfAttachedFileUtils.attachedFileSessionConflictCheck(menuScopeSessionBean,dto.getApplNo());
		
		//申請書管理番号が一致しない
		if (!checkResults) {			
			// セッション情報の削除
			menuScopeSessionBean.remove(SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY);
			menuScopeSessionBean.remove(SessionCacheKeyConstant.COMMON_ATTACHED_FILE_CONFLICT_SESSION_KEY);
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.I_SKF_1005,"セッション情報が異なっ","ブラウザを閉じて操作をやり直","");
			throwBusinessExceptionIfErrors(dto.getResultMessages());
			return dto;
		}	
		
		// 入力チェック
		boolean validateResult = validateCheck(dto);
		if (!validateResult) {
			return dto;
//			throwBusinessExceptionIfErrors(dto.getResultMessages());
		}
		// 処理名設定
		String execName = "present";

		// 更新処理
		boolean updResult = skf2100Sc002SharedService.updateDispInfo(execName, dto, applInfo, loginUserInfo);
		if (!updResult) {
			throwBusinessExceptionIfErrors(dto.getResultMessages());
			return dto;
		}

		
		// 前の画面に遷移する
		TransferPageInfo tpi = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC005);
		tpi.addResultMessage(MessageIdConstant.I_SKF_2047);
		dto.setTransferPageInfo(tpi);

		return dto;
	}

	/**
	 * 入力チェック
	 * 
	 * @param dto
	 * @param checkFlag
	 * @return
	 * @throws Exception
	 */
	private boolean validateCheck(Skf2100Sc002PresentDto dto) throws Exception {

		boolean result = true;

		// モバイルルーター通しNoなし（未選択）の場合
		if (dto.getMobileRouterNo() == null || dto.getMobileRouterNo() < 0) {
			ServiceHelper.addErrorResultMessage(dto, new String[] { "mobileRouterNo" }, MessageIdConstant.E_SKF_1054,
					"モバイルルーター");
			result = false;
		}
		
		// 発送日未入力の場合
		if (NfwStringUtils.isEmpty(dto.getShippingDate())) {
			ServiceHelper.addErrorResultMessage(dto, new String[] { "shippingDate" }, MessageIdConstant.E_SKF_1048,
					"発送日");
			result = false;
		}
		// 原籍会社未選択の場合
		if (NfwStringUtils.isEmpty(dto.getOriginalCompanyCd())) {
			ServiceHelper.addErrorResultMessage(dto, new String[] { "originalCompanyCd" }, MessageIdConstant.E_SKF_1054,
					"原籍会社");
			result = false;
		}
		// 給与支給会社未選択の場合
		if (NfwStringUtils.isEmpty(dto.getPayCompanyCd())) {
			ServiceHelper.addErrorResultMessage(dto, new String[] { "payCompanyCd" }, MessageIdConstant.E_SKF_1054,
					"給与支給会社");
			result = false;
		}
		
		// 申請者へのコメント
		String commentNote = dto.getCommentNote();
		// 値が4000バイトを超えた際はエラー
		int commentMaxLength = Integer.parseInt(PropertyUtils.getValue("skf2100.skf2100_sc002.comment_max_count"));
		if (NfwStringUtils.isNotEmpty(commentNote) && commentMaxLength < commentNote.getBytes("UTF-8").length) {
			ServiceHelper.addErrorResultMessage(dto, new String[] { "commentNote" }, MessageIdConstant.E_SKF_1049,
					"申請者へのコメント", "全角"+commentMaxLength / 2);
			result = false;
		}

		return result;
	}
}
