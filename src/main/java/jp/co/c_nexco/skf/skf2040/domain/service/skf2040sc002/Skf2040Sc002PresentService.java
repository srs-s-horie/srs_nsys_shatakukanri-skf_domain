/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.service.skf2040sc002;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetApplHistoryInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetApplHistoryInfoForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBatchUtils.SkfBatchUtilsGetMultipleTablesUpdateDateExp;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc002.Skf2040Sc002GetApplHistoryInfoForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfRollBack.SkfRollBackExpRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtils;
import jp.co.c_nexco.skf.common.util.SkfCommentUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.batch.SkfBatchUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2040Fc001TaikyoTodokeDataImport;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2050Fc001BihinHenkyakuSinseiDataImport;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040sc002.Skf2040Sc002PresentDto;

/**
 * Skf2040Sc002 退居（自動車の保管場所返還）届(アウトソース用）画面提示処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2040Sc002PresentService extends SkfServiceAbstract<Skf2040Sc002PresentDto> {

	@Autowired
	private SkfBatchUtils skfBatchUtils;
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
	@Autowired
	private Skf2040Fc001TaikyoTodokeDataImport skf2040Fc001TaikyoTodokeDataImport;
	@Autowired
	private Skf2050Fc001BihinHenkyakuSinseiDataImport skf2050Fc001BihinHenkyakuSinseiDataImport;
	@Autowired
	private SkfCommentUtils skfCommentUtils;
	@Autowired
	private SkfRollBackExpRepository skfRollBackExpRepository;

	private String sTrue = "true";
	private String sFalse = "false";
	Map<String, String> errorMsg = new HashMap<String, String>();

	@Override
	protected BaseDto index(Skf2040Sc002PresentDto preDto) throws Exception {

		// 操作ログ出力メソッドを呼び出す
		skfOperationLogUtils.setAccessLog("提示", CodeConstant.C001, FunctionIdConstant.SKF2040_SC002);

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

		String nextStatus = null;
		String mailKbn = null;
		String shoninName1 = null;
		String shoninName2 = null;
		Date agreDate = null;

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
			shoninName1 = applInfo.getAgreName1();
			shoninName2 = userInfo.get("userName");
			agreDate = new Date();
			break;
		}

		// メール区分の設定
		preDto.setMailKbn(mailKbn);

		switch (preDto.getApplId()) {
		case FunctionIdConstant.R0103:
			// ◆退居（自動車の保管場所返還）届
			LogUtils.debugByMsg("退居（自動車の保管場所返還）届(アウトソース用）提示ボタン押下 　退居（自動車の保管場所返還）届");
			/* 申請書類履歴テーブルの更新（退居届） */
			// 申請書類履歴テーブル」よりステータスを更新
			String resultUpdateApplInfo = skf2040Sc002SharedService.updateApplHistoryAgreeStatus(nextStatus,
					preDto.getShainNo(), preDto.getApplNo(), agreDate, shoninName1, shoninName2, preDto.getApplId(),
					applTacFlg, userInfo.get("userCd"), FunctionIdConstant.SKF2040_SC002, applInfo.getUpdateDate(),
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
				String commentNote = preDto.getCommentNote();
				boolean commentErrorMessage = skfCommentUtils.insertComment(CodeConstant.C001, preDto.getApplNo(),
						nextStatus, commentNote, errorMsg);
				if (!commentErrorMessage) {
					ServiceHelper.addErrorResultMessage(preDto, null, errorMsg.get("error"));
					throwBusinessExceptionIfErrors(preDto.getResultMessages());
				}
			}

			// 添付ファイル管理テーブル更新処理
			boolean resultUpdateFile = skf2040Sc002SharedService.updateAttachedFileInfo(nextStatus, preDto.getApplNo(),
					preDto.getShainNo(), attachedFileList, applTacFlg, applInfo, errorMsg, userInfo.get("userCd"),
					FunctionIdConstant.SKF2040_SC002);
			if (!resultUpdateFile) {
				// 更新エラー
				ServiceHelper.addErrorResultMessage(preDto, null, MessageIdConstant.E_SKF_1075);
				return preDto;
			}

			// 備品返却の提示作成がある場合
			if (sFalse.equals(preDto.getHenkyakuBihinNothing())) {
				LogUtils.debugByMsg("退居（自動車の保管場所返還）届(アウトソース用）提示ボタン押下 　備品返却の提示作成がある場合");
				// 申請書類履歴テーブル 備品返却申請を登録/更新処理
				if (!skf2040Sc002SharedService.insertOrUpdateApplHistoryForBihinHenkyaku(nextStatus, applTacFlg, preDto,
						agreDate, shoninName1, shoninName2, FunctionIdConstant.R0105, userInfo.get("userCd"))) {
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

			// 退居届データ連携
			// menuScopeSessionBeanからオブジェクトを取得
			Object forUpdateObject = menuScopeSessionBean.get(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2040SC002);
			// 連携用意
			Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> dateLinkTaikyoMap = skf2040Fc001TaikyoTodokeDataImport
					.forUpdateMapDownCaster(forUpdateObject);
			skf2040Fc001TaikyoTodokeDataImport.setUpdateDateForUpdateSQL(dateLinkTaikyoMap);
			// 連携実行
			List<String> resultList = skf2040Fc001TaikyoTodokeDataImport.doProc(CodeConstant.C001, preDto.getShainNo(),
					preDto.getApplNo(), nextStatus, userInfo.get("userCd"), FunctionIdConstant.SKF2040_SC002);
			// セッション情報の削除
			menuScopeSessionBean.remove(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2040SC002);

			// データ連携の戻り値がnullではない場合は、エラーメッセージを出して処理中断
			if (resultList != null) {
				skf2040Fc001TaikyoTodokeDataImport.addResultMessageForDataLinkage(preDto, resultList);
				skfRollBackExpRepository.rollBack();
				return preDto;
			}

			break;

		case FunctionIdConstant.R0105:
			// ◆備品返却希望
			LogUtils.debugByMsg("退居（自動車の保管場所返還）届(アウトソース用）提示ボタン押下 　備品返却希望");
			// /申請書類履歴テーブルの更新（備品返却希望）
			preDto.setHenkyakuBihinNothing(sFalse);

			// 備品返却の申請書類管理番号を格納
			preDto.setHdnBihinHenkyakuApplNo(preDto.getApplNo());

			// 申請書類履歴テーブル 備品返却申請を登録/更新処理
			if (!skf2040Sc002SharedService.insertOrUpdateApplHistoryForBihinHenkyaku(nextStatus, applTacFlg, preDto,
					agreDate, shoninName1, shoninName2, FunctionIdConstant.R0105, userInfo.get("userCd"))) {
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
			String commentNote = preDto.getCommentNote();
			boolean commentErrorMessage = skfCommentUtils.insertComment(CodeConstant.C001, preDto.getApplNo(),
					nextStatus, commentNote, errorMsg);
			if (!commentErrorMessage) {
				ServiceHelper.addErrorResultMessage(preDto, null, errorMsg.get("error"));
				throwBusinessExceptionIfErrors(preDto.getResultMessages());
			}

			// メール送信処理
			skf2040Sc002SharedService.sendMail(preDto.getApplNo(), preDto.getApplId(), preDto.getShainNo(), comment,
					preDto.getMailKbn(), true);

			break;
		}

		// 備品返却の提示作成がある場合
		if (sFalse.equals(preDto.getHenkyakuBihinNothing())) {

			// 社宅管理データ連携処理実行
			List<String> resultListBihin = doBihinHenkyakuShatakuRenkei(preDto, userInfo);

			// データ連携の戻り値がnullではない場合は、エラーメッセージを出して処理中断
			if (resultListBihin != null) {
				skf2050Fc001BihinHenkyakuSinseiDataImport.addResultMessageForDataLinkage(preDto, resultListBihin);
				skfRollBackExpRepository.rollBack();
				return preDto;
			}
		}

		// 終了メッセージ出力
		// 承認一覧に画面遷移
		TransferPageInfo tpi = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC005);
		tpi.addResultMessage(MessageIdConstant.I_SKF_2047);
		preDto.setTransferPageInfo(tpi);

		return preDto;

	}

	/**
	 * 備品返却の提示作成がある場合の社宅連携
	 * 
	 * @param preDto
	 * @param userInfo
	 * @return List<String>
	 */
	private List<String> doBihinHenkyakuShatakuRenkei(Skf2040Sc002PresentDto preDto, Map<String, String> userInfo) {
		// 備品返却
		// 社宅管理データ連携処理実行 ステータス審査中
		// menuScopeSessionBeanからオブジェクトを取得
		Object forUpdateObject = menuScopeSessionBean.get(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2040SC002);
		// 連携用意
		Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> dateLinkHenkyakuMap = skf2050Fc001BihinHenkyakuSinseiDataImport
				.forUpdateMapDownCaster(forUpdateObject);
		skf2050Fc001BihinHenkyakuSinseiDataImport.setUpdateDateForUpdateSQL(dateLinkHenkyakuMap);
		// 連携実行
		List<String> resultList = skf2050Fc001BihinHenkyakuSinseiDataImport.doProc(CodeConstant.C001,
				preDto.getShainNo(), preDto.getHdnBihinHenkyakuApplNo(), CodeConstant.STATUS_SHINSACHU,
				userInfo.get("userCd"), FunctionIdConstant.SKF2040_SC002);
		// セッション情報の削除
		menuScopeSessionBean.remove(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2040SC002);

		// データ連携の戻り値がnullではない場合は、エラーメッセージを出して処理中断
		if (resultList != null) {
			return resultList;
		}

		// 再度排他制御用更新日取得
		// データ連携用の排他制御用更新日を取得
		Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> dateLinkageMap = skfBatchUtils
				.getUpdateDateForUpdateSQL(preDto.getShainNo());
		menuScopeSessionBean.put(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2040SC002, dateLinkageMap);

		// 社宅管理データ連携処理実行 ステータス確認依頼
		// menuScopeSessionBeanからオブジェクトを取得
		Object forUpdateObject2 = menuScopeSessionBean.get(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2040SC002);
		// 連携用意
		Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> dateLinkHenkyakuMap2 = skf2050Fc001BihinHenkyakuSinseiDataImport
				.forUpdateMapDownCaster(forUpdateObject2);
		skf2050Fc001BihinHenkyakuSinseiDataImport.setUpdateDateForUpdateSQL(dateLinkHenkyakuMap2);
		// 連携実行
		List<String> resultList2 = skf2050Fc001BihinHenkyakuSinseiDataImport.doProc(CodeConstant.C001,
				preDto.getShainNo(), preDto.getHdnBihinHenkyakuApplNo(), CodeConstant.STATUS_KAKUNIN_IRAI,
				userInfo.get("userCd"), FunctionIdConstant.SKF2040_SC002);
		// セッション情報の削除
		menuScopeSessionBean.remove(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2040SC002);

		// データ連携の戻り値がnullではない場合は、エラーメッセージを出して処理中断
		if (resultList2 != null) {
			return resultList2;
		}

		return null;
	}

}
