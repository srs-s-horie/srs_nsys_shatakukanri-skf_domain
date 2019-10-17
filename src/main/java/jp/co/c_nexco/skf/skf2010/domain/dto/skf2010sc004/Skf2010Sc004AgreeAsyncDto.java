/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc004;

import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010Sc004common.Skf2010Sc004CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2010_Sc004画面のAgreeDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010Sc004AgreeAsyncDto extends Skf2010Sc004CommonAsyncDto {

	private static final long serialVersionUID = -1902278406295003652L;

	private boolean dialogFlg = false;
	private String bihinApplNo;

}
