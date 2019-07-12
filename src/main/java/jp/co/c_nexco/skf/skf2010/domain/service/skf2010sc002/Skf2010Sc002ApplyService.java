/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc002;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
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

	// 入退居区分(入居)
	private static final String NYUTAIKYO_KBN_NYUKYO = "1";
	// 入退居区分(退居)
	private static final String NYUTAIKYO_KBN_TAIKYO = "2";
	// 会社コード
	private String companyCd = CodeConstant.C001;

	/** ロガー。 */
	private static Logger logger = LogUtils.getLogger(SkfFileOutputUtils.class);

	@Value("${skf.common.validate_error}")
	private String validationErrorCode;

	@Autowired
	private SkfShinseiUtils skfShinseiUtils;
	@Autowired
	private Skf2010Sc002SharedService skf2010Sc002SharedService;

	@Override
	public BaseDto index(Skf2010Sc002ApplyDto applyDto) throws Exception {

		// 申請情報のチェックを行う
		checkAndGetApplSession(applyDto);

		// 入退居については、申請前に申請可能か判定を行う。
		if (!skfShinseiUtils.checkSKSTeijiStatus(applyDto.getShainNo(), applyDto.getApplId(), applyDto.getApplNo())) {
			// 申請不可の場合
			return applyDto;
		}

		// コメント欄の入力チェック
		if (!(validateComment(applyDto))) {
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
	private void checkAndGetApplSession(Skf2010Sc002ApplyDto applyDto) throws Exception {

		// 申請書類IDの有無チェック
		if (applyDto.getApplId() == null) {
			String errorMessage = "エラーが発生しました。ヘルプデスクへ連絡してください。";
			logger.error(errorMessage);
			throw new Exception(errorMessage);
		}

		// ステータスの有無チェック
		// 申請書類IDの有無チェック
		if (applyDto.getApplStatus() == null) {
			String errorMessage = "エラーが発生しました。ヘルプデスクへ連絡してください。";
			logger.error(errorMessage);
			throw new Exception(errorMessage);
		}
	}

	/**
	 * コメントエラーチェック
	 * 
	 * @param applyDto
	 * @throws UnsupportedEncodingException
	 */
	private boolean validateComment(Skf2010Sc002ApplyDto applyDto) throws UnsupportedEncodingException {

		// コメント
		LogUtils.debugByMsg("桁数チェック " + "コメント - " + applyDto.getCommentNote());
		if (applyDto.getCommentNote() != null
				&& CheckUtils.isMoreThanByteSize(applyDto.getCommentNote().trim(), 4000)) {
			ServiceHelper.addErrorResultMessage(applyDto, null, MessageIdConstant.E_SKF_1071, "承認者へのコメント", "4000");
			applyDto.setCommentNote(validationErrorCode);
			return false;
		}
		return true;

	}

	/**
	 * 社宅管理 提示データステータスによる申請可否チェック
	 * 
	 * @param applNo
	 * @param applId
	 * @param shainNo
	 */
	private boolean checkSKSTeijiStatus(String shainNo, String applId, String applNo) {

		String nyutaikyoKbn = CodeConstant.DOUBLE_QUOTATION;
		int listCnt = 0;

		// 申請書類管理番号が空かNULLの場合、-にする
		if (applNo == null || applNo.length() == 0) {
			applNo = CodeConstant.HYPHEN;
		}

		// 対象申請書類IDを選択
		switch (applId) {
		case FunctionIdConstant.R0100:
			// R0100: 社宅入居希望等調書
			nyutaikyoKbn = NYUTAIKYO_KBN_NYUKYO;

			// 以下の条件に当てはまる入退居予定データが存在する場合、新規申請不可
			// ①社員番号
			// ②入退居区分
			// ③入退居申請状況区分が'40'（承認）以外
			// かつ申請書類管理番号が異なる場合
			// listCnt += getSksNYDStatusInfo(shainNo, nyutaikyoKbn, applNo);
			break;
		case FunctionIdConstant.R0103:
			// R0103: 退居（自動車の保管場所返還）届
			nyutaikyoKbn = NYUTAIKYO_KBN_TAIKYO;
			break;
		}

		switch (applId) {
		case FunctionIdConstant.R0100:
		case FunctionIdConstant.R0103:
			// R0100: 社宅入居希望等調書
			// R0103: 退居（自動車の保管場所返還）届

			// 以下の条件に当てはまる提示データが存在する場合、新規申請不可
			// ①社員番号
			// ②入退居区分
			// ③備品貸与区分<>'1'（必要）以外の場合、社宅提示ステータス<>'5'（承認）
			// 備品貸与区分＝'1'（必要）の場合、備品提示ステータス<>'9'（承認）

			// listCnt += getTeijiStatusCount(shainNo, nyutaikyoKbn);
		}

		// データが取得できた場合は新規申請不可
		if (listCnt > 0) {
			return false;
		}
		return true;
	}
}
