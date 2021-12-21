/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc001;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc001.Skf2100Sc001DownloadDto;

/**
 * Skf2100Sc001 モバイルルーター借用希望申請書（申請者用)の申請要件確認押下時のサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2100Sc001DownloadService extends SkfServiceAbstract<Skf2100Sc001DownloadDto> {

	/**
	 * サービス処理を行う。
	 * 
	 * @param Skf2020Sc002DownloadDto
	 * @return 処理結果
	 * @throws Exception 例外
	 * 
	 */

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	@Override
	protected BaseDto index(Skf2100Sc001DownloadDto dto) throws Exception {

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("申請要件を確認", CodeConstant.C001, FunctionIdConstant.SKF2100_SC001);

		// ダウンロードファイル名
		String downloadFileName = "skf.skf_appl_requirement.FileId_RouterKibo";

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