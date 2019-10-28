/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc006;

import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc006common.Skf3010Sc006CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3010Sc006ParkingAddressSearchAsyncDto 駐車場契約情報住所検索Dto
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3010Sc006ParkingAddressSearchAsyncDto extends Skf3010Sc006CommonAsyncDto {

	private static final long serialVersionUID = -1902278406295003652L;
	
	//郵便番号
	private String parkingZipCd;
	//住所
	private String parkingContractAddress;
	//郵便番号エラー
	private String parkingZipCdError;
}
