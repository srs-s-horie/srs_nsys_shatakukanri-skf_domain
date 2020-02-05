/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3030.domain.dto.skf3030sc002;

import jp.co.c_nexco.skf.skf3030.domain.dto.skf3030Sc002common.Skf3030Sc002CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3030Sc002ParkingSupportCallBackAsyncDto 入退居情報登録画面：駐車場区画クリア処理処理(非同期)Dto。
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3030Sc002ParkingBlockClearCheckAsyncDto extends Skf3030Sc002CommonAsyncDto {

	private static final long serialVersionUID = -1902278406295003652L;
	
	private String hdnNengetsu;
	private String hdnShatakuKanriId;
	private String hdnShatakuKanriNo;
	private String hdnChushajoKanriNo;
	private String parkingLendNo;
	private String checkResult;
}
