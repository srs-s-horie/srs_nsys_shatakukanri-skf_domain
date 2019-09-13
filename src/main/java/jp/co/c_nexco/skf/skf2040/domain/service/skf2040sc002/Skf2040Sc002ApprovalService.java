/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.service.skf2040sc002;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetApplHistoryInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetApplHistoryInfoForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc002.Skf2040Sc002GetApplHistoryInfoForUpdateExpRepository;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040sc002.Skf2040Sc002ApprovalDto;

/**
 * Skf2040Sc002 退居（自動車の保管場所返還）届(アウトソース用）画面承認処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2040Sc002ApprovalService extends BaseServiceAbstract<Skf2040Sc002ApprovalDto> {

	@Autowired
	private Skf2040Sc002SharedService skf2040Sc002SharedService;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfAttachedFileUtils skfAttachedFileUtiles;
	@Autowired
	private Skf2040Sc002GetApplHistoryInfoForUpdateExpRepository skf2040Sc002GetApplHistoryInfoForUpdateExpRepository;

	private String sTrue = "true";
	private String sFalse = "false";
	Map<String, String> errorMsg = new HashMap<String, String>();

	@Override
	protected BaseDto index(Skf2040Sc002ApprovalDto appDto) throws Exception {

		// 操作ログ出力メソッドを呼び出す
		skfOperationLogUtils.setAccessLog("承認", CodeConstant.C001, appDto.getPageId());

		// 申請書類履歴情報の取得
		Skf2040Sc002GetApplHistoryInfoForUpdateExp applInfo = new Skf2040Sc002GetApplHistoryInfoForUpdateExp();
		Skf2040Sc002GetApplHistoryInfoForUpdateExpParameter param = new Skf2040Sc002GetApplHistoryInfoForUpdateExpParameter();
		param.setCompanyCd(CodeConstant.C001);
		param.setApplNo(appDto.getApplNo());
		applInfo = skf2040Sc002GetApplHistoryInfoForUpdateExpRepository.getApplHistoryInfoForUpdate(param);

		// 一般添付資料取得
		List<Map<String, Object>> attachedFileList = skfAttachedFileUtiles.getAttachedFileInfo(menuScopeSessionBean,
				appDto.getApplNo(), SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY);
		String applTacFlg = CodeConstant.DOUBLE_QUOTATION;
		if (attachedFileList != null && attachedFileList.size() > 0) {
			// 添付ファイルあり
			applTacFlg = CodeConstant.YES;
		} else {
			applTacFlg = CodeConstant.NO;
		}

		// コメント入力欄のチェック
		boolean validate = skf2040Sc002SharedService.checkValidation(appDto, sFalse);
		if (!validate) {
			// 添付資料だけはセッションから再取得の必要あり
			skf2040Sc002SharedService.setAttachedFileList(appDto);
			return appDto;
		}

		// 承認者情報の取得
		Map<String, String> userInfo = new HashMap<String, String>();
		userInfo = skfLoginUserInfoUtils.getSkfLoginUserInfo();

		String nextStatus = CodeConstant.DOUBLE_QUOTATION;
		String mailKbn = CodeConstant.DOUBLE_QUOTATION;
		String shoninName1 = CodeConstant.DOUBLE_QUOTATION;
		String shoninName2 = CodeConstant.DOUBLE_QUOTATION;

		// 次のステータスを設定する
		switch (appDto.getApplStatus()) {
		case CodeConstant.STATUS_SHINSACHU:
		case CodeConstant.STATUS_DOI_ZUMI:
			// 申請状況が「審査中」、「同意済」
			// 次のステータス、メール区分、承認者名1、次の階層を設定
			nextStatus = CodeConstant.STATUS_SHONIN1;
			mailKbn = CodeConstant.SHONIN_IRAI_TSUCHI;
			shoninName1 = userInfo.get("userName");
			break;
		case CodeConstant.STATUS_SHONIN1:
			// 申請状況が「承認1済」
			// 次のステータス、メール区分、承認者名2、承認済みを設定
			nextStatus = CodeConstant.STATUS_SHONIN_ZUMI;
			mailKbn = CodeConstant.SHONIN_KANRYO_TSUCHI;
			shoninName2 = userInfo.get("userName");
			break;
		}

		appDto.setMailKbn(mailKbn);

		// 申請書類履歴テーブル」よりステータスを更新
		boolean resultUpdateApplInfo = skf2040Sc002SharedService.updateApplHistoryAgreeStatus(nextStatus,
				appDto.getShainNo(), appDto.getApplNo(), shoninName1, shoninName2, appDto.getApplId());
		if (!resultUpdateApplInfo) {
			errorMsg.put("error", MessageIdConstant.E_SKF_1075);
			return appDto;
		}

		// コメント更新
		if (NfwStringUtils.isNotEmpty(appDto.getCommentNote())) {
			if (!skf2040Sc002SharedService.updateCommentTable(userInfo, appDto.getApplNo(), nextStatus, errorMsg,
					appDto.getCommentNote())) {
				return appDto;
			}
		}

		// メール送信処理
		skf2040Sc002SharedService.sendMail(appDto);

		// TODO 社宅管理データ連携処理実行

		// 終了メッセージ出力
		// 承認一覧に画面遷移
		TransferPageInfo tpi = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC005);
		tpi.addResultMessage(MessageIdConstant.I_SKF_2047);
		appDto.setTransferPageInfo(tpi);

		return appDto;
	}

}
