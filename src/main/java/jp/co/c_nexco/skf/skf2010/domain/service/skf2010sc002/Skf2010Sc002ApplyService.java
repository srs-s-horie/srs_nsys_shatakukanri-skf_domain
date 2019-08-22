/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc002;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.SkfShinseiUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc002.Skf2010Sc002ApplyDto;
import jp.co.intra_mart.common.platform.log.Logger;

/**
 * Skf2010Sc002 申請書類確認の申請ボタン処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc002ApplyService extends BaseServiceAbstract<Skf2010Sc002ApplyDto> {

	/** ロガー。 */
	private static Logger logger = LogUtils.getLogger(SkfFileOutputUtils.class);

	@Autowired
	private SkfShinseiUtils skfShinseiUtils;
	@Autowired
	private Skf2010Sc002SharedService skf2010Sc002SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	@Override
	public BaseDto index(Skf2010Sc002ApplyDto applyDto) throws Exception {

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("申請", CodeConstant.C001, applyDto.getPageId());

		// 申請情報のチェックを行う
		checktApplSession(applyDto);

		// 申請前に申請可能か判定を行う。
		if (!skfShinseiUtils.checkSKSTeijiStatus(applyDto.getShainNo(), applyDto.getApplId(), applyDto.getApplNo())) {
			// 申請不可の場合
			ServiceHelper.addErrorResultMessage(applyDto, null, MessageIdConstant.I_SKF_1005, "承認されていない申請書類が存在し",
					"「社宅申請状況一覧」から確認", "");
			throwBusinessExceptionIfErrors(applyDto.getResultMessages());
			return applyDto;
		}

		// コメント欄の入力チェック
		if (!(skf2010Sc002SharedService.validateComment(applyDto))) {
			// エラーメッセージがある場合、処理を中断
			return applyDto;
		}

		// ステータスを設定
		String status = CodeConstant.STATUS_SHINSEICHU;

		// 「申請書類履歴テーブル」よりステータスを更新 + 承認者へのコメントを更新
		Map<String, String> applMap = new HashMap<String, String>();
		applMap.put("shainNo", applyDto.getShainNo());
		applMap.put("applNo", applyDto.getApplNo());
		applMap.put("name", applyDto.getName());
		applMap.put("status", status);
		applMap.put("commentNote", applyDto.getCommentNote());
		boolean res = skf2010Sc002SharedService.updateShinseiHistory(applMap);
		if (!res) {
			ServiceHelper.addErrorResultMessage(applyDto, null, MessageIdConstant.E_SKF_1073);
			throwBusinessExceptionIfErrors(applyDto.getResultMessages());
		}

		// TODO 支社担当者、事務所担当者にメールを送付→承認権限がないため不要と思われる

		// TODO 社宅管理データ連携処理実行

		// 画面遷移（申請条件一覧へ）
		TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC003, "init");
		nextPage.addResultMessage(MessageIdConstant.I_SKF_2047);
		applyDto.setTransferPageInfo(nextPage);

		return applyDto;
	}

	/**
	 * 情報のチェック
	 * 
	 * @param applyDto
	 * @throws Exception
	 */
	private void checktApplSession(Skf2010Sc002ApplyDto applyDto) throws Exception {

		// 申請書類IDの有無チェック
		if (applyDto.getApplId() == null) {
			String errorMessage = "エラーが発生しました。ヘルプデスクへ連絡してください。";
			logger.error(errorMessage);
			throw new Exception(errorMessage);
		}

		// ステータスの有無チェック
		if (applyDto.getApplStatus() == null) {
			String errorMessage = "エラーが発生しました。ヘルプデスクへ連絡してください。";
			logger.error(errorMessage);
			throw new Exception(errorMessage);
		}
	}

}
