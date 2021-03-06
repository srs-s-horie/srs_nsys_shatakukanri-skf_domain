/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc007;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc007.Skf2010Sc007GetApplHistoryInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc007.Skf2010Sc007GetApplHistoryInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc007.Skf2010Sc007GetApplHistoryInfoExpRepository;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
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
public class Skf2010Sc007TransferService extends BaseServiceAbstract<Skf2010Sc007TransferDto> {

	// 基準会社コード
	private String companyCd = CodeConstant.C001;
	@Autowired
	private SkfShinseiUtils skfShinseiUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
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
		skfOperationLogUtils.setAccessLog("申請書を作成する", companyCd, transferDto.getPageId());

		// 画面遷移前のチェック処理
		setRedirectAppl(transferDto);

		return transferDto;
	}

	/**
	 * 遷移前チェック処理
	 * 
	 * @param transferDto
	 */
	private void setRedirectAppl(Skf2010Sc007TransferDto transferDto) {

		// 申請書類Idの設定
		String applID = transferDto.getApplId();

		// ユーザコンテキストを取得
		UserContext userContext = Contexts.get(UserContext.class);
		UserProfile profile = userContext.getUserProfile();
		transferDto.setUserId(profile.getUserCd()); // ユーザID

		// 社員番号の設定
		List<Skf2010Sc007GetApplHistoryInfoExp> applHistoryList = new ArrayList<Skf2010Sc007GetApplHistoryInfoExp>();
		applHistoryList = getApplHistoryList(transferDto.getUserId(), applID, applHistoryList);
		// 値が取得できた場合は、社員番号を設定
		if (applHistoryList.size() > 0) {
			transferDto.setShainNo(applHistoryList.get(0).getShainNo());
		}

		// 入退居については、申請前に申請可能か判定を行う。
		switch (applID) {
		case FunctionIdConstant.R0100:
			// 社宅入居希望等調書
			boolean resultN = true;
			int cntN = 0;
			// 取得した申請書履歴リストの件数分チェック
			for (int i = 0; i < applHistoryList.size(); i++) {
				resultN = skfShinseiUtils.checkSKSTeijiStatus(transferDto.getShainNo(), applID,
						applHistoryList.get(i).getApplNo());
				if (resultN == false) {
					cntN += 1;
				}
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
						"「社宅申請状況一覧」から確認", "");
				throwBusinessExceptionIfErrors(transferDto.getResultMessages());
				break;
			}

		case FunctionIdConstant.R0103:
			// 退居（自動車の保管場所返還）届
			boolean resultT = true;
			int cntT = 0;
			// 取得した申請書履歴リストの件数分チェック
			for (int i = 0; i < applHistoryList.size(); i++) {
				resultT = skfShinseiUtils.checkSKSTeijiStatus(transferDto.getShainNo(), applID,
						applHistoryList.get(i).getApplNo());
				if (resultT == false) {
					cntT += 1;
				}
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
						"「社宅申請状況一覧」から確認", "");
				throwBusinessExceptionIfErrors(transferDto.getResultMessages());
				break;
			}

		}
	}

	/**
	 * 申請書履歴から社員番号を取得
	 * 
	 * @param userId ユーザーID
	 * @param applId 申請書類番号
	 * @param applHistoryReusltList リスト
	 * @return 取得結果
	 */
	private List<Skf2010Sc007GetApplHistoryInfoExp> getApplHistoryList(String userId, String applId,
			List<Skf2010Sc007GetApplHistoryInfoExp> applHistoryReusltList) {

		// DB検索処理
		Skf2010Sc007GetApplHistoryInfoExpParameter param = new Skf2010Sc007GetApplHistoryInfoExpParameter();
		param.setCompanyCd(CodeConstant.C001);
		param.setUserId(userId);
		param.setApplId(applId);
		applHistoryReusltList = skf2010Sc007GetApplHistoryInfoExpRepository.getApplHistoryInfo(param);
		return applHistoryReusltList;
	}
}
