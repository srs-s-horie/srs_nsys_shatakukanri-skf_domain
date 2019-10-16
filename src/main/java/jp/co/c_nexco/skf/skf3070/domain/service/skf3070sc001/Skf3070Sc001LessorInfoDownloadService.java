/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3070.domain.service.skf3070sc001;

import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.skf3070.domain.dto.skf3070sc001.Skf3070Sc001LessorInfoDownloadDto;

/**
 * Skf3070Sc001 法定調書データ管理画面の賃貸人（代理人）情報出力処理クラス。
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf3070Sc001LessorInfoDownloadService extends BaseServiceAbstract<Skf3070Sc001LessorInfoDownloadDto> {

	/**
	 * サービス処理を行う。
	 * 
	 * @param Skf3070Sc001LessorInfoDownloadDto lessorDlDto
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public BaseDto index(Skf3070Sc001LessorInfoDownloadDto lessorDlDto) throws Exception {

		return lessorDlDto;

	}

}
