/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3070.domain.service.skf3070sc001;

import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.skf3070.domain.dto.skf3070sc001.Skf3070Sc001StatutoryRecordDownloadDto;

/**
 * Skf3070Sc001 法定調書データ管理画面の法定調書データ出力処理クラス。
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf3070Sc001StatutoryRecordDownloadService
		extends BaseServiceAbstract<Skf3070Sc001StatutoryRecordDownloadDto> {

	/**
	 * サービス処理を行う。
	 * 
	 * @param Skf3070Sc001StatutoryRecordDownloadDto sRDto
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public BaseDto index(Skf3070Sc001StatutoryRecordDownloadDto sRDto) throws Exception {

		return sRDto;

	}

}
