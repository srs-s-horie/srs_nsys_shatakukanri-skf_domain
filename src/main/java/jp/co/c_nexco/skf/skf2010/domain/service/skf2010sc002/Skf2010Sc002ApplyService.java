/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc002;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBatchUtils.SkfBatchUtilsGetMultipleTablesUpdateDateExp;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfRollBack.SkfRollBackExpRepository;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.SkfShinseiUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2020Fc001NyukyoKiboSinseiDataImport;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2040Fc001TaikyoTodokeDataImport;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc002.Skf2010Sc002ApplyDto;
import jp.co.intra_mart.foundation.context.Contexts;
import jp.co.intra_mart.foundation.user_context.model.UserContext;
import jp.co.intra_mart.foundation.user_context.model.UserProfile;

/**
 * Skf2010Sc002ApplyService 申請書類確認画面の申請処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc002ApplyService extends BaseServiceAbstract<Skf2010Sc002ApplyDto> {

	@Autowired
	private SkfShinseiUtils skfShinseiUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf2010Sc002SharedService skf2010Sc002SharedService;
	@Autowired
	private Skf2040Fc001TaikyoTodokeDataImport skf2040Fc001TaikyoTodokeDataImport;
	@Autowired
	private Skf2020Fc001NyukyoKiboSinseiDataImport skf2020Fc001NyukyoKiboSinseiDataImport;
	@Autowired
	private SkfRollBackExpRepository skfRollBackExpRepository;

	// 承認者更新フラグ
	private String agreNameNoUpdate = "0";

	@Override
	public BaseDto index(Skf2010Sc002ApplyDto applyDto) throws Exception {

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("申請", CodeConstant.C001, applyDto.getPageId());

		// 申請書類IDの有無チェック
		if (applyDto.getApplId() == null) {
			ServiceHelper.addErrorResultMessage(applyDto, null, MessageIdConstant.E_SKF_1078, "");
			return applyDto;
		}
		// ステータスの有無チェック
		if (applyDto.getApplStatus() == null) {
			ServiceHelper.addErrorResultMessage(applyDto, null, MessageIdConstant.E_SKF_1078, "");
			return applyDto;
		}

		// 申請前に申請可能か判定を行う。
		if (!skfShinseiUtils.checkSKSTeijiStatus(applyDto.getShainNo(), applyDto.getApplId(), applyDto.getApplNo())) {
			// 申請不可の場合
			ServiceHelper.addErrorResultMessage(applyDto, null, MessageIdConstant.I_SKF_1005, "承認されていない申請書類が存在し",
					"「社宅申請状況一覧」から確認", "");
			throwBusinessExceptionIfErrors(applyDto.getResultMessages());
			return applyDto;
		}

		// コメント欄の入力チェック
		if (!(skf2010Sc002SharedService.validateComment(applyDto.getCommentNote()))) {
			// エラーがある場合はメッセージを出力して処理を中断
			ServiceHelper.addErrorResultMessage(applyDto, new String[] { "commentNote" }, MessageIdConstant.E_SKF_1071,
					"承認者へのコメント", "2000");
			return applyDto;
		}

		// 次のステータスを設定
		String status = CodeConstant.STATUS_SHINSEICHU;

		// 「申請書類履歴テーブル」よりステータスを更新 + 承認者へのコメントを更新
		Map<String, String> applMap = new HashMap<String, String>();
		applMap.put("shainNo", applyDto.getShainNo());
		applMap.put("applNo", applyDto.getApplNo());
		applMap.put("name", applyDto.getName());
		applMap.put("status", status);
		applMap.put("commentNote", applyDto.getCommentNote());

		// 申請書類履歴の更新を行う。承認者の名前は更新しない
		String res = skf2010Sc002SharedService.updateShinseiHistory(applMap,
				applyDto.getLastUpdateDate(Skf2010Sc002SharedService.KEY_LAST_UPDATE_DATE_HISTORY), agreNameNoUpdate);
		if ("updateError".equals(res)) {
			// 更新エラー
			ServiceHelper.addErrorResultMessage(applyDto, null, MessageIdConstant.E_SKF_1075);
			return applyDto;
		} else if ("exclusiveError".equals(res)) {
			// 排他チェックエラー
			ServiceHelper.addErrorResultMessage(applyDto, null, MessageIdConstant.E_SKF_1134, "skf2010_t_appl_history");
			return applyDto;
		}

		// TODO 支社担当者、事務所担当者にメールを送付→承認権限がないため不要と思われる

		// 社宅管理データ連携処理実行
		// ユーザIDの取得
		UserContext userContext = Contexts.get(UserContext.class);
		UserProfile profile = userContext.getUserProfile();
		String userId = profile.getUserCd();
		// menuScopeSessionBeanからオブジェクトを取得
		Object forUpdateObject = menuScopeSessionBean.get(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2010SC002);
		if (FunctionIdConstant.R0100.equals(applyDto.getApplId())) {
			// 社宅入居希望等調書データ連携
			// 連携用意
			Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> dateLinkNyukyoMap = skf2020Fc001NyukyoKiboSinseiDataImport
					.forUpdateMapDownCaster(forUpdateObject);
			skf2020Fc001NyukyoKiboSinseiDataImport.setUpdateDateForUpdateSQL(dateLinkNyukyoMap);
			// 連携実行
			List<String> resultList = skf2020Fc001NyukyoKiboSinseiDataImport.doProc(CodeConstant.C001,
					applyDto.getShainNo(), applyDto.getApplNo(), CodeConstant.NONE, status, userId,
					applyDto.getPageId());
			// セッション情報の削除
			menuScopeSessionBean.remove(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2010SC002);

			// データ連携の戻り値がnullではない場合は、エラーメッセージを出して処理中断
			if (resultList != null) {
				skf2020Fc001NyukyoKiboSinseiDataImport.addResultMessageForDataLinkage(applyDto, resultList);
				skfRollBackExpRepository.rollBack();
				throwBusinessExceptionIfErrors(applyDto.getResultMessages());
			}

		} else if (FunctionIdConstant.R0103.equals(applyDto.getApplId())) {
			// 退居届データ連携
			// 連携用意
			Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> dateLinkNyukyoMap = skf2040Fc001TaikyoTodokeDataImport
					.forUpdateMapDownCaster(forUpdateObject);
			skf2040Fc001TaikyoTodokeDataImport.setUpdateDateForUpdateSQL(dateLinkNyukyoMap);
			// 連携実行
			List<String> resultList = skf2040Fc001TaikyoTodokeDataImport.doProc(CodeConstant.C001,
					applyDto.getShainNo(), applyDto.getApplNo(), status, userId, applyDto.getPageId());
			// セッション情報の削除
			menuScopeSessionBean.remove(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2010SC002);

			// データ連携の戻り値がnullではない場合は、エラーメッセージを出して処理中断
			if (resultList != null) {
				skf2040Fc001TaikyoTodokeDataImport.addResultMessageForDataLinkage(applyDto, resultList);
				skfRollBackExpRepository.rollBack();
				throwBusinessExceptionIfErrors(applyDto.getResultMessages());
			}
		}

		// 画面遷移（申請条件一覧へ）
		TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC003, "init");
		nextPage.addResultMessage(MessageIdConstant.I_SKF_2047);
		applyDto.setTransferPageInfo(nextPage);

		return applyDto;
	}

}
