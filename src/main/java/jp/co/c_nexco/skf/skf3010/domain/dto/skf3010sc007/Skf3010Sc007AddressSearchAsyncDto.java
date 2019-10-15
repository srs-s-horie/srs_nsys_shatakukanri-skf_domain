/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc007;

import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc007common.Skf3010Sc007CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3010Sc007AddressSearchAsyncDto 駐車場契約情報住所検索Dto
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3010Sc007AddressSearchAsyncDto extends Skf3010Sc007CommonAsyncDto {

	private static final long serialVersionUID = -1902278406295003652L;
	
	private String parkingZipCdError;
}
