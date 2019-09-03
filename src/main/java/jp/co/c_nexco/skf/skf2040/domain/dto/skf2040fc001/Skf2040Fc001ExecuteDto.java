/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.dto.skf2040fc001;

import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import lombok.EqualsAndHashCode;

@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2040Fc001ExecuteDto extends BaseDto {

	private static final long serialVersionUID = -1902278406295003652L;

	private String companyCd;
	private String shainNo;
	private String applNo;
	private String applStatus;
	private String errorCodeID;
	private String errorStrings;

}
