/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3070.domain.service.skf3070sc001;

import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.skf3070.domain.dto.skf3070sc001.Skf3070Sc001SearchDto;

/**
 * Skf3070Sc001 法定調書データ管理画面の検索処理サービス処理クラス。
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf3070Sc001SearchService extends BaseServiceAbstract<Skf3070Sc001SearchDto> {

	/**
	 * サービス処理を行う。
	 * 
	 * @param searchDto DTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public BaseDto index(Skf3070Sc001SearchDto searchDto) throws Exception {

		return searchDto;
	}

}