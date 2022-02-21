/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc007;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc007.Skf2010Sc007DownloadDto;

/**
 * Skf2010Sc007 申請条件確認画面、申請要件確認押下時のサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 *
 */

@Service
public class Skf2010Sc007DownloadService extends SkfServiceAbstract<Skf2010Sc007DownloadDto> {

	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */

	// 基準会社コード
	private String companyCd = CodeConstant.C001;
	// 画面表示判定 社宅入居希望等調書
	private static final String NYUKYO = "1";
	// 画面表示判定 退居（自動車の保管場所返還）届
	private static final String TAIKYO = "2";
	// 画面表示判定 モバイルルーター借用希望申請書
	private static final String ROUTER_KIBO = "3";
	// 画面表示判定 モバイルルーター返却申請書
	private static final String ROUTER_HENKYAKU = "4";
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	@Override
	protected BaseDto index(Skf2010Sc007DownloadDto dto) throws Exception {

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("申請要件を確認", companyCd, FunctionIdConstant.SKF2010_SC007);

		String downloadFileName = "";

		if (NYUKYO.equals(dto.getConfirmationKbn())) {
			// 区分が入居だった場合
			// ダウンロードファイル名
			downloadFileName = "skf.skf_appl_requirement.FileId_Nyukyo";

		} else if (TAIKYO.equals(dto.getConfirmationKbn())) {
			// 区分が退居だった場合
			// ダウンロードファイル名
			downloadFileName = "skf.skf_appl_requirement.FileId_Taikyo";

		} else if (ROUTER_KIBO.equals(dto.getConfirmationKbn())) {
			// 区分がルーター借用希望だった場合
			// ダウンロードファイル名
			downloadFileName = "skf.skf_appl_requirement.FileId_RouterKibo";

		} else if (ROUTER_HENKYAKU.equals(dto.getConfirmationKbn())) {
			// 区分がルーター返却だった場合
			// ダウンロードファイル名
			downloadFileName = "skf.skf_appl_requirement.FileId_RouterHenkyaku";

		}

		// 機能ID
		String functionId = "skfapplrequirement";
		// DTOに値をセット
		dto.setDownloadFileName(downloadFileName);
		dto.setFunctionId(functionId);

		// ファイル出力処理
		SkfFileOutputUtils.fileOutputPdf(downloadFileName, functionId, dto);

		return dto;

	}

}
