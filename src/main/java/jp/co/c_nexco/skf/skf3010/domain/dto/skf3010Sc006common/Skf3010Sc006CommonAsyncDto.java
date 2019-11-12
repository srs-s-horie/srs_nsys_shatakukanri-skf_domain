/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc006common;


import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc002common.Skf3010Sc002CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3010Sc002CommonAsyncDto 借上社宅登録内共通DTO(非同期用)
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3010Sc006CommonAsyncDto extends Skf3010Sc002CommonAsyncDto {

	private static final long serialVersionUID = -1902278406295003652L;

	//駐車場調整金額
	private String parkingRentalAdjust;
	//駐車場月額使用料
	private String parkingShiyoMonthFei;
	// 本来用途
	private String originalAuse;
	// 物置面積
	private String barnMenseki;
	// 物置調整面積
	private String barnMensekiAdjust;	
}
