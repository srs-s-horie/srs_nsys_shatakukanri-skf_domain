/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc007;

import java.text.MessageFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc007.Skf2010Sc007InitDto;
import jp.co.intra_mart.foundation.context.Contexts;
import jp.co.intra_mart.foundation.user_context.model.UserContext;
import jp.co.intra_mart.foundation.user_context.model.UserProfile;

/**
 * Skf2010Sc007InitService 申請条件確認初期表示処理クラス.
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf2010Sc007InitService extends BaseServiceAbstract<Skf2010Sc007InitDto> {

	// 画面表示判定 社宅入居希望等調書
	private static final String NYUKYO = "1";
	// 画面表示判定 退居（自動車の保管場所返還）届
	private static final String TAIKYO = "2";
	// 基準会社コード
	private String companyCd = CodeConstant.C001;
	// 画面メッセージ
	private static final String MES = "{0}の申請に必要な要件や添付書類を確認する場合は、以下のボタンから確認してください。";
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */

	@Override
	public Skf2010Sc007InitDto index(Skf2010Sc007InitDto initDto) throws Exception {

		// タイトル設定
		initDto.setPageTitleKey(MessageIdConstant.SKF2010_SC007_TITLE);

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", companyCd, initDto.getPageId());

		// 申請条件内容の設定を行う
		setApplTac(initDto);

		return initDto;
	}

	/**
	 * 申請条件内容の設定を行う
	 * 
	 * @param initDto
	 */
	private void setApplTac(Skf2010Sc007InitDto initDto) {

		// ユーザコンテキストを取得
		UserContext userContext = Contexts.get(UserContext.class);
		UserProfile profile = userContext.getUserProfile();
		initDto.setUserId(profile.getUserCd()); // ユーザID

		// 表示名称の設定
		String applName = CodeConstant.DOUBLE_QUOTATION;
		// 申請書IDの設定
		String applId = CodeConstant.DOUBLE_QUOTATION;

		if (NYUKYO.equals(initDto.getConfirmationKbn())) {
			// 区分が入居だった場合
			// 申請書類名と申請書IDを「社宅入居希望等調書」に設定
			applName = "社宅入居希望等調書";
			initDto.setApplId(FunctionIdConstant.R0100);

		} else if (TAIKYO.equals(initDto.getConfirmationKbn())) {
			// 区分が退居だった場合はメッセージを「入居希望申請に設定
			applName = "社宅（自動車保管場所）退居届";
			initDto.setApplId(FunctionIdConstant.R0103);
		}
		// 申請条件確認用のメッセージを設定
		initDto.setApplYoken(MessageFormat.format(MES, applName));

	}
}
