/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc003;

import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010Sc003common.Skf2010Sc003CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2010_Sc005画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010Sc003TransferDto extends Skf2010Sc003CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	private String applNo;
	private String applId;
	private String sendApplStatus;

}
