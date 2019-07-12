/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc009;

import lombok.EqualsAndHashCode;

/**
 * Skf2010_Sc009画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010Sc009DeleteDto extends Skf2010Sc009InitDto {

	private static final long serialVersionUID = -1902278406295003652L;

	private String attachedNo;
}
