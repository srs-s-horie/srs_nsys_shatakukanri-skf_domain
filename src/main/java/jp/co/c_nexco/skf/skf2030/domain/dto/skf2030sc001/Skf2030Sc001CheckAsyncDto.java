/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2030.domain.dto.skf2030sc001;

import jp.co.c_nexco.skf.skf2030.domain.dto.skf2030Sc001common.Skf2030Sc001CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2030_Sc001画面のSaveDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2030Sc001CheckAsyncDto extends Skf2030Sc001CommonAsyncDto {

	private static final long serialVersionUID = -1902278406295003652L;

	private String showDialogFlag;
	private String nyukyobi;

}
