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
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2040Fc001TaikyoTodokeDataImport;
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
	@Autowired
	private Skf2040Fc001TaikyoTodokeDataImport skf2040Fc001TaikyoTodokeDataImport;

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
		String applTacFlg = CodeConstant.NONE;
		if (attachedFileList != null && attachedFileList.size() > 0) {
			// 添付ファイルあり
			applTacFlg = CodeConstant.YES;
		} else {
			applTacFlg = CodeConstant.NO;
		}
		String applStatus = applInfo.getApplStatus();
		String applNo = applInfo.getApplNo();

		// コメント入力欄のチェック
		if (!skf2040Sc002SharedService.checkValidation(appDto, sFalse)) {
			// 添付資料だけはセッションから再取得の必要あり
			List<Map<String, Object>> reAttachedFileList = skf2040Sc002SharedService.setAttachedFileList();
			appDto.setAttachedFileList(reAttachedFileList);
			return appDto;
		}

		// 承認者情報の取得
		Map<String, String> userInfo = new HashMap<String, String>();
		userInfo = skfLoginUserInfoUtils.getSkfLoginUserInfo();

		String nextStatus = null;
		String mailKbn = null;
		String shoninName1 = null;
		String shoninName2 = null;
		Date agreDate = null;

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
			shoninName1 = applInfo.getAgreName1();
			shoninName2 = userInfo.get("userName");
			agreDate = new Date();
			break;
		}

		appDto.setMailKbn(mailKbn);

		// 申請書類履歴テーブル」よりステータスを更新
		String resultUpdateApplInfo = skf2040Sc002SharedService.updateApplHistoryAgreeStatus(nextStatus,
				appDto.getShainNo(), appDto.getApplNo(), agreDate, shoninName1, shoninName2, appDto.getApplId(),
				applTacFlg, userInfo.get("userCd"), appDto.getPageId(), applInfo.getUpdateDate(),
				appDto.getLastUpdateDate(Skf2040Sc002SharedService.KEY_LAST_UPDATE_DATE_HISTORY_TAIKYO));
		if (resultUpdateApplInfo.equals("updateError")) {
			// 更新エラー
			ServiceHelper.addErrorResultMessage(appDto, null, MessageIdConstant.E_SKF_1075);
			return appDto;
		} else if (resultUpdateApplInfo.equals("exclusiveError")) {
			// 排他チェックエラー
			ServiceHelper.addErrorResultMessage(appDto, null, MessageIdConstant.E_SKF_1134, "skf2010_t_appl_history");
		}

		// コメント更新
		String comment = appDto.getCommentNote();
		if (NfwStringUtils.isNotEmpty(comment)) {
			skf2040Sc002SharedService.deleteComment(applNo, applStatus, errorMsg);
			if (!skf2040Sc002SharedService.insertCommentTable(userInfo, appDto.getApplNo(), nextStatus, errorMsg,
					comment)) {
				ServiceHelper.addErrorResultMessage(appDto, null, MessageIdConstant.E_SKF_1075);
				return appDto;
			}
		} else {
			comment = CodeConstant.NONE;
		}

		// 申請履歴の更新後データを取り直す
		applInfo = new Skf2040Sc002GetApplHistoryInfoForUpdateExp();
		param = new Skf2040Sc002GetApplHistoryInfoForUpdateExpParameter();
		param.setCompanyCd(CodeConstant.C001);
		param.setApplNo(appDto.getApplNo());
		applInfo = skf2040Sc002GetApplHistoryInfoForUpdateExpRepository.getApplHistoryInfoForUpdate(param);

		// 添付ファイル管理テーブル更新処理
		boolean resultUpdateFile = skf2040Sc002SharedService.updateAttachedFileInfo(nextStatus, appDto.getApplNo(),
				appDto.getShainNo(), attachedFileList, applTacFlg, applInfo, errorMsg, userInfo.get("userCd"),
				appDto.getPageId());
		if (!resultUpdateFile) {
			// 更新エラー
			ServiceHelper.addErrorResultMessage(appDto, null, MessageIdConstant.E_SKF_1075);
			return appDto;
		}

		// メール送信処理
		skf2040Sc002SharedService.sendMail(appDto.getApplNo(), appDto.getApplId(), appDto.getShainNo(), comment,
				appDto.getMailKbn(), false);

		// 退居届データ連携
		// menuScopeSessionBeanからオブジェクトを取得
		Object forUpdateObject = menuScopeSessionBean.get(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2040SC002);
		// 連携用意
		Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> dateLinkTaikyoMap = skf2040Fc001TaikyoTodokeDataImport
				.forUpdateMapDownCaster(forUpdateObject);
		skf2040Fc001TaikyoTodokeDataImport.setUpdateDateForUpdateSQL(dateLinkTaikyoMap);
		// 連携実行
		List<String> resultList = skf2040Fc001TaikyoTodokeDataImport.doProc(CodeConstant.C001, appDto.getShainNo(),
				appDto.getApplNo(), nextStatus, userInfo.get("userCd"), appDto.getPageId());
		// セッション情報の削除
		menuScopeSessionBean.remove(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2040SC002);

		// データ連携の戻り値がnullではない場合は、エラーメッセージを出して処理中断
		if (resultList != null) {
			skf2040Fc001TaikyoTodokeDataImport.addResultMessageForDataLinkage(appDto, resultList);
		}

		// 終了メッセージ出力
		// 承認一覧に画面遷移
		TransferPageInfo tpi = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC005);
		tpi.addResultMessage(MessageIdConstant.I_SKF_2047);
		appDto.setTransferPageInfo(tpi);

		return appDto;
	}

}
