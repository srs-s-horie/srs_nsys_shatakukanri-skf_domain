/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.dto.skf3020sc005;

import jp.co.c_nexco.skf.skf3020.domain.dto.skf3020Sc005common.Skf3020Sc005CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3020_Sc005 社員入力支援DTO
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3020Sc005SupportDto extends Skf3020Sc005CommonDto {

	private static final long serialVersionUID = -2554998006065418486L;
	
	private String hdnSelShainNo;

}
