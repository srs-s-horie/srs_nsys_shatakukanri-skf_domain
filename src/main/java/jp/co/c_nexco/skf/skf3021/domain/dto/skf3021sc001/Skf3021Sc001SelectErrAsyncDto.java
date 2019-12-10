/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3021.domain.dto.skf3021sc001;

import jp.co.c_nexco.skf.skf3021.domain.dto.skf3021Sc001common.Skf3021Sc001CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3022Sc001SelectErrAsyncDto 入退居予定一覧画面未選択エラーDto
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3021Sc001SelectErrAsyncDto extends Skf3021Sc001CommonAsyncDto {

	private static final long serialVersionUID = -1902278406295003652L;
	
	//エラー番号
	String errCode;
	
}
