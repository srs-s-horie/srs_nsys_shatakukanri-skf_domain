/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf1010.domain.service.skf1010sc001;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf1010.domain.dto.skf1010sc001.Skf1010Sc001DownloadDto;

/**
 * Skf2010Sc007 申請条件確認画面、申請要件確認押下時のサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 *
 */

@Service
public class Skf1010Sc001DownloadService extends BaseServiceAbstract<Skf1010Sc001DownloadDto> {

	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */

	// 基準会社コード
	private String companyCd = CodeConstant.C001;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	@Override
	protected BaseDto index(Skf1010Sc001DownloadDto dto) throws Exception {

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("あいあいあいい", companyCd, dto.getPageId());

		// ダウンロードファイル名
		String downloadFileName = "skf.pdf.skf1010Sc001.pdf_template_manual_" + dto.getManual();

		// 機能ID
		String functionId = "skf1010" + dto.getManual();
		
		// DTOに値をセット
		dto.setDownloadFileName(downloadFileName);
		dto.setFunctionId(functionId);

		// ファイル出力処理
		SkfFileOutputUtils.fileOutputPdf(downloadFileName, functionId, dto);
		
		return dto;

	}

}
