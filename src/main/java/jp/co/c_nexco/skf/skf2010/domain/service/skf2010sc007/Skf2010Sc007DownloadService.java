/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc007;

import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc007.Skf2010Sc007DownloadDto;

/**
 * Skf2010Sc007DownloadService 申請条件確認画面、申請要件確認押下時のサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 *
 */

@Service
public class Skf2010Sc007DownloadService extends BaseServiceAbstract<Skf2010Sc007DownloadDto> {

	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */

	@Override
	protected BaseDto index(Skf2010Sc007DownloadDto dto) throws Exception {
		// TODO 自動生成されたメソッド・スタブ

		// ダウンロードファイル名
		String downloadFileName = "skf2010.skf2010_sc007.FileId";

		// 機能ID
		String functionId = "skf201000007";

		// DTOに値をセット
		dto.setDownloadFileName(downloadFileName);
		dto.setFunctionId(functionId);

		// ファイル出力処理
		SkfFileOutputUtils.fileOutputPdf(downloadFileName, functionId, dto);

		return dto;

	}

}
