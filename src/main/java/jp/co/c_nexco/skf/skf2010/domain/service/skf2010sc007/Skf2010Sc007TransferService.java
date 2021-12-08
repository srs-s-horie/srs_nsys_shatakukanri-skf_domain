/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc007;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfGetInfoUtils.SkfGetInfoUtilsGetShainInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfGetInfoUtils.SkfGetInfoUtilsGetShainInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc007.Skf2010Sc007GetApplHistoryInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfGetInfoUtils.SkfGetInfoUtilsGetShainInfoExpRepository;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.SkfShinseiUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc007.Skf2010Sc007TransferDto;
import jp.co.intra_mart.foundation.context.Contexts;
import jp.co.intra_mart.foundation.user_context.model.UserContext;
import jp.co.intra_mart.foundation.user_context.model.UserProfile;

/**
 * 
 * Skf2010Sc007TransferService 申請条件確認画面の「申請書を作成するボタン」押下時のサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */

@Service
public class Skf2010Sc007TransferService extends SkfServiceAbstract<Skf2010Sc007TransferDto> {

	// 基準会社コード
	private String companyCd = CodeConstant.C001;
	@Autowired
	private SkfShinseiUtils skfShinseiUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfGetInfoUtilsGetShainInfoExpRepository skfGetInfoUtilsGetShainInfoExpRepository;
	@Autowired
	private Skf2010Sc007GetApplHistoryInfoExpRepository skf2010Sc007GetApplHistoryInfoExpRepository;

	/**
	 * サービス処理を行う。
	 * 
	 * @param transferDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public BaseDto index(Skf2010Sc007TransferDto transferDto) throws Exception {

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("申請書類を作成", companyCd, FunctionIdConstant.SKF2010_SC007);

		// 画面遷移前のチェック処理
		setRedirectAppl(transferDto);

		return transferDto;
	}

	/**
	 * 遷移前チェック処理
	 * 
	 * @param transferDto
	 */
	@SuppressWarnings("unchecked")
	private void setRedirectAppl(Skf2010Sc007TransferDto transferDto) {

		// 申請書類Idの設定
		String applID = transferDto.getApplId();

		// ユーザコンテキストを取得
		UserContext userContext = Contexts.get(UserContext.class);
		UserProfile profile = userContext.getUserProfile();
		transferDto.setUserId(profile.getUserCd()); // ユーザID

		// セッション情報の取得(代理ログイン情報)
		Map<String, String> resultAlterLoginList = null;
		resultAlterLoginList = (Map<String, String>) menuScopeSessionBean
				.get(SessionCacheKeyConstant.ALTER_LOGIN_USER_INFO_MAP);
		if (resultAlterLoginList != null) {
			transferDto.setAlterLoginFlg((resultAlterLoginList.get(SessionCacheKeyConstant.ALTER_LOGIN_SESSION_KEY)));
		} else {
			transferDto.setAlterLoginFlg(SkfCommonConstant.NOT_USE);
		}

		// 社員番号の設定
		if (SkfCommonConstant.NOT_USE.equals(transferDto.getAlterLoginFlg())) {
			// 社員番号を設定
			SkfGetInfoUtilsGetShainInfoExp shainInfo = new SkfGetInfoUtilsGetShainInfoExp();
			SkfGetInfoUtilsGetShainInfoExpParameter shainParam = new SkfGetInfoUtilsGetShainInfoExpParameter();
			shainParam.setUserId(transferDto.getUserId());
			shainInfo = skfGetInfoUtilsGetShainInfoExpRepository.getShainInfo(shainParam);
			if (shainInfo != null) {
				// 社員番号
				transferDto.setShainNo(shainInfo.getShainNo());
			}
		} else {
			// 代行ログインの場合
			if (NfwStringUtils.isNotEmpty(resultAlterLoginList.get(CodeConstant.ALTER_LOGIN_USER_SHAIN_NO))) {
				// 社員番号の設定
				transferDto.setShainNo(resultAlterLoginList.get(CodeConstant.ALTER_LOGIN_USER_SHAIN_NO));
			}
		}

		// 入退居については、申請前に申請可能か判定を行う。
		switch (applID) {
		case FunctionIdConstant.R0100:
			// 社宅入居希望等調書
			boolean resultN = true;
			int cntN = 0;
			// 申請可能かのチェック
			resultN = skfShinseiUtils.checkSKSTeijiStatus(transferDto.getShainNo(), applID, null);
			if (resultN == false) {
				// チェックがNGの場合カウントアップ
				cntN += 1;
			}

			if (cntN == 0) {
				// 申請可能な場合は次の処理へ
				TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF2020_SC002);
				transferDto.setTransferPageInfo(nextPage);
				break;
			} else {
				// 申請不可の場合、申請条件確認画面にメッセージを表示する。
				transferDto.setResultMessages(null);
				ServiceHelper.addErrorResultMessage(transferDto, null, MessageIdConstant.I_SKF_1005, "承認されていない申請書類が存在し",
						"「申請状況一覧」から確認", "");
				throwBusinessExceptionIfErrors(transferDto.getResultMessages());
				break;
			}

		case FunctionIdConstant.R0103:
			// 退居（自動車の保管場所返還）届
			boolean resultT = true;
			int cntT = 0;
			// 申請可能かのチェック
			resultT = skfShinseiUtils.checkSKSTeijiStatus(transferDto.getShainNo(), applID, null);
			if (resultT == false) {
				cntT += 1;
			}
			if (cntT == 0) {
				// 申請可能な場合は次の処理へ
				TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF2040_SC001);
				transferDto.setTransferPageInfo(nextPage);
				break;
			} else {
				// 申請不可の場合、申請条件確認画面にメッセージを表示する。
				transferDto.setResultMessages(null);
				ServiceHelper.addErrorResultMessage(transferDto, null, MessageIdConstant.I_SKF_1005, "承認されていない申請書類が存在し",
						"「申請状況一覧」から確認", "");
				throwBusinessExceptionIfErrors(transferDto.getResultMessages());
				break;
			}
		// モバイルルーター機能追加対応 2021/9 add start
		case FunctionIdConstant.R0107:
			// モバイルルーター借用希望申請書
			boolean resultK = true;
			int cntK = 0;
			// 申請可能かのチェック
			resultK = skfShinseiUtils.checkRouterShinseiStatus(transferDto.getShainNo(), applID, transferDto.getApplNo());
			
			//未返却ルーター確認
			cntK = skfShinseiUtils.getSksRouterLedgerCount(transferDto.getShainNo());
			if(cntK > 0){
				resultK = false;
			}
			
			if (resultK) {
				// 申請可能な場合は次の処理へ
				TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF2100_SC001);
				transferDto.setTransferPageInfo(nextPage);
				break;
			} else {
				// 申請不可の場合、申請条件確認画面にメッセージを表示する。
				transferDto.setResultMessages(null);
				if(cntK > 0){
					ServiceHelper.addErrorResultMessage(transferDto, null, MessageIdConstant.I_SKF_1005, "貸与中のモバイルルーターが存在し",
							"モバイルルーターの返却申請を", "");
				}else{
					ServiceHelper.addErrorResultMessage(transferDto, null, MessageIdConstant.I_SKF_1005, "承認されていない申請書類が存在し",
							"「申請状況一覧」から確認", "");
				}
				throwBusinessExceptionIfErrors(transferDto.getResultMessages());
				break;
			}
		case FunctionIdConstant.R0108:
			// モバイルルーター返却申請書
			boolean resultH = true;
			// 申請可能かのチェック
			resultH = skfShinseiUtils.checkRouterShinseiStatus(transferDto.getShainNo(), applID, transferDto.getApplNo());
			
			if (resultH) {
				// 申請可能な場合は次の処理へ
				TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF2100_SC003);
				transferDto.setTransferPageInfo(nextPage);
				break;
			} else {
				// 申請不可の場合、申請条件確認画面にメッセージを表示する。
				transferDto.setResultMessages(null);
				ServiceHelper.addErrorResultMessage(transferDto, null, MessageIdConstant.I_SKF_1005, "承認されていない申請書類が存在し",
						"「申請状況一覧」から確認", "");
				
				throwBusinessExceptionIfErrors(transferDto.getResultMessages());
				break;
			}
		// モバイルルーター機能追加対応 2021/9 add end
		}
	}
}
