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
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040sc002.Skf2040Sc002PresentDto;

/**
 * Skf2040Sc002 退居（自動車の保管場所返還）届(アウトソース用）画面提示処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2040Sc002PresentService extends BaseServiceAbstract<Skf2040Sc002PresentDto> {

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
	protected BaseDto index(Skf2040Sc002PresentDto preDto) throws Exception {

		// 操作ログ出力メソッドを呼び出す
		skfOperationLogUtils.setAccessLog("提示", CodeConstant.C001, preDto.getPageId());

		// 申請書類履歴情報の取得
		Skf2040Sc002GetApplHistoryInfoForUpdateExp applInfo = new Skf2040Sc002GetApplHistoryInfoForUpdateExp();
		Skf2040Sc002GetApplHistoryInfoForUpdateExpParameter param = new Skf2040Sc002GetApplHistoryInfoForUpdateExpParameter();
		param.setCompanyCd(CodeConstant.C001);
		param.setApplNo(preDto.getApplNo());
		applInfo = skf2040Sc002GetApplHistoryInfoForUpdateExpRepository.getApplHistoryInfoForUpdate(param);

		// 一般添付資料取得
		List<Map<String, Object>> attachedFileList = skfAttachedFileUtiles.getAttachedFileInfo(menuScopeSessionBean,
				preDto.getApplNo(), SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY);
		String applTacFlg = CodeConstant.DOUBLE_QUOTATION;
		if (attachedFileList != null && attachedFileList.size() > 0) {
			// 添付ファイルあり
			applTacFlg = CodeConstant.YES;
		} else {
			applTacFlg = CodeConstant.NO;
		}

		// コメント入力欄のチェック
		boolean validate = skf2040Sc002SharedService.checkValidation(preDto, sFalse);
		if (!validate) {
			// 添付資料だけはセッションから再取得の必要あり
			List<Map<String, Object>> reAttachedFileList = skf2040Sc002SharedService.setAttachedFileList();
			preDto.setAttachedFileList(reAttachedFileList);
			return preDto;
		}
		String comment = preDto.getCommentNote();

		// 承認者情報の取得
		Map<String, String> userInfo = new HashMap<String, String>();
		userInfo = skfLoginUserInfoUtils.getSkfLoginUserInfo();

		String nextStatus = CodeConstant.DOUBLE_QUOTATION;
		String mailKbn = CodeConstant.DOUBLE_QUOTATION;
		String shoninName1 = CodeConstant.DOUBLE_QUOTATION;
		String shoninName2 = CodeConstant.DOUBLE_QUOTATION;

		// 次のステータスを設定する
		switch (preDto.getApplStatus()) {
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

		// メール区分の設定
		preDto.setMailKbn(mailKbn);

		switch (preDto.getApplId()) {
		case FunctionIdConstant.R0103:
			// ◆退居（自動車の保管場所返還）届
			/* 申請書類履歴テーブルの更新（退居届） */
			// 申請書類履歴テーブル」よりステータスを更新
			String resultUpdateApplInfo = skf2040Sc002SharedService.updateApplHistoryAgreeStatus(nextStatus,
					preDto.getShainNo(), preDto.getApplNo(), shoninName1, shoninName2, preDto.getApplId(), applTacFlg,
					userInfo.get("userCd"), preDto.getPageId(), applInfo.getUpdateDate(),
					preDto.getLastUpdateDate(Skf2040Sc002SharedService.KEY_LAST_UPDATE_DATE_HISTORY_TAIKYO));
			if ("updateError".equals(resultUpdateApplInfo)) {
				// 更新エラー
				ServiceHelper.addErrorResultMessage(preDto, null, MessageIdConstant.E_SKF_1075);
				return preDto;
			} else if ("exclusiveError".equals(resultUpdateApplInfo)) {
				// 排他チェックエラー
				ServiceHelper.addErrorResultMessage(preDto, null, MessageIdConstant.E_SKF_1134,
						"skf2010_t_appl_history");
				return preDto;
			}

			// コメントがある場合は更新
			if (NfwStringUtils.isNotEmpty(comment)) {
				if (!skf2040Sc002SharedService.insertCommentTable(userInfo, preDto.getApplNo(), nextStatus, errorMsg,
						comment)) {
					ServiceHelper.addErrorResultMessage(preDto, null, MessageIdConstant.E_SKF_1075);
					return preDto;
				}
			} else {
				comment = CodeConstant.NONE;
			}

			// 添付ファイル管理テーブル更新処理
			boolean resultUpdateFile = skf2040Sc002SharedService.updateAttachedFileInfo(nextStatus, preDto.getApplNo(),
					preDto.getShainNo(), attachedFileList, applTacFlg, applInfo, errorMsg, userInfo.get("userCd"),
					preDto.getPageId());
			if (!resultUpdateFile) {
				// 更新エラー
				ServiceHelper.addErrorResultMessage(preDto, null, MessageIdConstant.E_SKF_1075);
				return preDto;
			}

			// 備品返却の提示作成がある場合
			if (sFalse.equals(preDto.getHenkyakuBihinNothing())) {
				// 申請書類履歴テーブル 備品返却申請を登録/更新処理
				if (!skf2040Sc002SharedService.insertOrUpdateApplHistoryForBihinHenkyaku(nextStatus, applTacFlg, preDto,
						shoninName1, shoninName2, FunctionIdConstant.R0105, userInfo.get("userCd"))) {
					// エラーがある場合、処理を中断
					ServiceHelper.addErrorResultMessage(preDto, null, MessageIdConstant.E_SKF_1075);
					return preDto;
				}

				// 備品申請テーブル 登録/更新処理
				String resultBihinShinsei = skf2040Sc002SharedService.insertOrUpdateBihinShinseiTable(preDto,
						userInfo.get("userCd"));
				if ("updateError".equals(resultBihinShinsei)) {
					// 更新エラー
					ServiceHelper.addErrorResultMessage(preDto, null, MessageIdConstant.E_SKF_1075);
					return preDto;
				} else if ("exclusiveError".equals(resultBihinShinsei)) {
					// 排他チェックエラー
					ServiceHelper.addErrorResultMessage(preDto, null, MessageIdConstant.E_SKF_1134, "skf2030_t_bihin");
					return preDto;
				}
			}

			// メール送信処理
			skf2040Sc002SharedService.sendMail(preDto.getApplNo(), preDto.getApplId(), preDto.getShainNo(), comment,
					preDto.getMailKbn(), true);

			// TODO 社宅管理データ連携処理実行

			break;

		case FunctionIdConstant.R0105:
			// ◆備品返却希望

			// /申請書類履歴テーブルの更新（備品返却希望）
			preDto.setHenkyakuBihinNothing(sFalse);

			// 備品返却の申請書類管理番号を格納
			preDto.setHdnBihinHenkyakuApplNo(preDto.getApplNo());

			// 申請書類履歴テーブル 備品返却申請を登録/更新処理
			if (!skf2040Sc002SharedService.insertOrUpdateApplHistoryForBihinHenkyaku(nextStatus, applTacFlg, preDto,
					shoninName1, shoninName2, FunctionIdConstant.R0105, userInfo.get("userCd"))) {
				// エラーがある場合、処理を中断
				ServiceHelper.addErrorResultMessage(preDto, null, MessageIdConstant.E_SKF_1075);
				return preDto;
			}

			// 備品申請テーブル 登録/更新処理
			String resultBihinShinsei = skf2040Sc002SharedService.insertOrUpdateBihinShinseiTable(preDto,
					userInfo.get("userCd"));
			if ("updateError".equals(resultBihinShinsei)) {
				// 更新エラー
				ServiceHelper.addErrorResultMessage(preDto, null, MessageIdConstant.E_SKF_1075);
				return preDto;
			} else if ("exclusiveError".equals(resultBihinShinsei)) {
				// 排他チェックエラー
				ServiceHelper.addErrorResultMessage(preDto, null, MessageIdConstant.E_SKF_1134, "skf2030_t_bihin");
				return preDto;
			}

			// コメント更新
			if (NfwStringUtils.isNotEmpty(comment)) {
				if (!skf2040Sc002SharedService.insertCommentTable(userInfo, preDto.getApplNo(),
						CodeConstant.STATUS_KAKUNIN_IRAI, errorMsg, preDto.getCommentNote())) {
					ServiceHelper.addErrorResultMessage(preDto, null, MessageIdConstant.E_SKF_1075);
					return preDto;
				}
			} else {
				comment = CodeConstant.NONE;
			}

			// メール送信処理
			skf2040Sc002SharedService.sendMail(preDto.getApplNo(), preDto.getApplId(), preDto.getShainNo(), comment,
					preDto.getMailKbn(), true);

			break;
		}

		// 備品返却の提示作成がある場合
		if (sFalse.equals(preDto.getHenkyakuBihinNothing())) {
			// TODO 社宅管理データ連携処理実行 ステータス審査中
			// TODO 社宅管理データ連携処理実行 ステータス確認依頼
		}

		// 終了メッセージ出力
		// 承認一覧に画面遷移
		TransferPageInfo tpi = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC005);
		tpi.addResultMessage(MessageIdConstant.I_SKF_2047);
		preDto.setTransferPageInfo(tpi);

		return preDto;

	}

}
