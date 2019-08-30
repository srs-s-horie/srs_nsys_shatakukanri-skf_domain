/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc001;

import java.util.*;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060Sc001common.Skf2060Sc001CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2060_Sc001画面のSearchAdrressDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2060Sc001InsertKariageDto extends Skf2060Sc001CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
		
	//借上社宅名エラークラス
	private String shatakuNameError;
	//郵便番号エラークラス
	private String postalCdError;
	//住所エラークラス
	private String addressError;

}