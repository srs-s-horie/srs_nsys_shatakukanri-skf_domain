/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2020.domain.service.skf2020sc002;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2020.domain.dto.skf2020sc002.Skf2020Sc002DownloadDto;

/**
 * Skf2020Sc002 社宅入居希望等調書（申請者用)の申請要件確認押下時のサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2020Sc002DownloadService extends BaseServiceAbstract<Skf2020Sc002DownloadDto> {

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
	protected BaseDto index(Skf2020Sc002DownloadDto dto) throws Exception {

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("申請要件を確認する", CodeConstant.C001, dto.getPageId());

		// ダウンロードファイル名
		String downloadFileName = "skf2020.skf2020_sc002.FileId";

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
