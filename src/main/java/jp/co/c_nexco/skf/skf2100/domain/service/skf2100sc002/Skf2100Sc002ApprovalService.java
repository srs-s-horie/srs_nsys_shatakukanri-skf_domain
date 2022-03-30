/*
 * Copyright(c) 2021 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc002;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc002.Skf2100Sc002ApprovalDto;

/**
 * Skf2100Sc002 モバイルルーター借用希望申請書（アウトソース)承認処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2100Sc002ApprovalService extends SkfServiceAbstract<Skf2100Sc002ApprovalDto> {

	@Autowired
	private Skf2100Sc002SharedService skf2100Sc002SharedService;

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
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
	public Skf2100Sc002ApprovalDto index(Skf2100Sc002ApprovalDto dto) throws Exception {
		// 操作ログ出力
		skfOperationLogUtils.setAccessLog("承認", CodeConstant.C001, FunctionIdConstant.SKF2100_SC002);
		
		// ログインユーザー情報取得
		Map<String, String> loginUserInfo = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		skf2100Sc002SharedService.setMenuScopeSessionBean(menuScopeSessionBean);
		
		// 申請情報設定
		Map<String, String> applInfo = new HashMap<String, String>();
		applInfo.put("status", dto.getApplStatus());
		applInfo.put("applNo", dto.getApplNo());
		applInfo.put("applId", dto.getApplId());
		
		//複数タブによる添付ファイルセッションチェック		
		boolean checkResults = skfAttachedFileUtiles.attachedFileSessionConflictCheck(menuScopeSessionBean,dto.getApplNo());
		
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
		boolean validateResult = skf2100Sc002SharedService.validateReason(dto, false);
		if (!validateResult) {
//			throwBusinessExceptionIfErrors(dto.getResultMessages());
			return dto;
		}
		
		// 処理名設定
		String execName = "apply";

		// 更新処理
		boolean updResult = skf2100Sc002SharedService.updateDispInfo(execName, dto, applInfo, loginUserInfo);
		if (!updResult) {
			throwBusinessExceptionIfErrors(dto.getResultMessages());
			return dto;
		}

		
		// 前の画面に遷移する
		TransferPageInfo tpi = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC005);
		tpi.addResultMessage(MessageIdConstant.I_SKF_2031);
		dto.setTransferPageInfo(tpi);

		return dto;
	}

}
