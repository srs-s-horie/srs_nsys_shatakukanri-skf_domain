/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc007;

import java.text.MessageFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc007.Skf2010Sc007InitDto;
import jp.co.intra_mart.foundation.service.client.file.PublicStorage;

/**
 * Skf2010Sc007InitService 申請条件確認初期表示処理クラス.
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf2010Sc007InitService extends SkfServiceAbstract<Skf2010Sc007InitDto> {

	// 画面表示判定 社宅入居希望等調書
	private static final String NYUKYO = "1";
	// 画面表示判定 退居（自動車の保管場所返還）届
	private static final String TAIKYO = "2";
	// 画面表示判定 モバイルルーター借用希望申請書
	private static final String ROUTER_KIBO = "3";
	// 画面表示判定 モバイルルーター返却申請書
	private static final String ROUTER_HENKYAKU = "4";
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
		skfOperationLogUtils.setAccessLog("初期表示", companyCd, FunctionIdConstant.SKF2010_SC007);

		// 申請条件内容の設定を行う
		setApplTac(initDto);

		return initDto;
	}

	/**
	 * 申請条件の表示内容設定を行う
	 * 
	 * @param initDto
	 */
	private void setApplTac(Skf2010Sc007InitDto initDto) {

		String downloadFileName = "";
		
		// 表示名称の設定
		String applName = CodeConstant.DOUBLE_QUOTATION;

		if (NYUKYO.equals(initDto.getConfirmationKbn())) {
			// 区分が入居だった場合
			// 申請書類名と申請書IDを「社宅入居希望等調書」に設定
			applName = "社宅入居希望等調書";
			initDto.setApplId(FunctionIdConstant.R0100);
			
			// ダウンロードファイル名
			downloadFileName = "skf.skf_appl_requirement.FileId_Nyukyo";

		} else if (TAIKYO.equals(initDto.getConfirmationKbn())) {
			// 区分が退居だった場合はメッセージを「社宅（自動車保管場所）退居届」に設定
			applName = "社宅（自動車保管場所）退居届";
			initDto.setApplId(FunctionIdConstant.R0103);
			
			// ダウンロードファイル名
			downloadFileName = "skf.skf_appl_requirement.FileId_Taikyo";
		}
		// モバイルルーター機能追加対応 2021/9 add start
		else if (ROUTER_KIBO.equals(initDto.getConfirmationKbn())) {
			// 区分がルーター借用希望だった場合はメッセージを「モバイルルーター借用希望申請書」に設定
			applName = "モバイルルーター借用希望申請書";
			initDto.setApplId(FunctionIdConstant.R0107);
			// ダウンロードファイル名
			downloadFileName = "skf.skf_appl_requirement.FileId_RouterKibo";
			
		} else if (ROUTER_HENKYAKU.equals(initDto.getConfirmationKbn())) {
			// 区分がルーター返却だった場合はメッセージを「モバイルルーター返却申請書」に設定
			applName = "モバイルルーター返却申請書";
			initDto.setApplId(FunctionIdConstant.R0108);
			// ダウンロードファイル名
			downloadFileName = "skf.skf_appl_requirement.FileId_RouterHenkyaku";
		}
		// モバイルルーター機能追加対応 2021/9 add end
		// 申請条件確認用のメッセージを設定
		initDto.setApplYoken(MessageFormat.format(MES, applName));

		// 機能ID
		String functionId = "skfapplrequirement";

		//ファイル存在チェック
		initDto.setBtnCheckDisabled(null);
		try{
			String templateFilePath = SkfFileOutputUtils.getTemplateFilePath(functionId, downloadFileName);
			PublicStorage storage = SkfFileOutputUtils.getFilePublicStorage(templateFilePath);
			if (storage == null) {
				//テンプレートファイルが存在しません
				initDto.setBtnCheckDisabled("true");
			}
		}
		catch(Exception e){
			initDto.setBtnCheckDisabled("true");
		}

	}
}
