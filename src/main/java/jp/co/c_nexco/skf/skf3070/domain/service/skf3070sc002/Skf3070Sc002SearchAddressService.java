/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3070.domain.service.skf3070sc002;

import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.skf3070.domain.dto.skf3070sc002.Skf3070Sc002SearchAddressDto;

/**
 * Skf3070_Sc002 賃貸人（代理人）情報登録画面の住所検索処理クラス
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf3070Sc002SearchAddressService extends BaseServiceAbstract<Skf3070Sc002SearchAddressDto> {

	/**
	 * サービス処理を行う。
	 * 
	 * @param dto DTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public BaseDto index(Skf3070Sc002SearchAddressDto dto) throws Exception {

		return dto;
	}
}
