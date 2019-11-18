/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skfcomb.domain.dto.skfcombatch2;

import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import lombok.EqualsAndHashCode;

@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class SkfComBatch2ExecuteDto extends BaseDto {

	private static final long serialVersionUID = -1902278406295003652L;

	private String teijiNo;
	private String yearMonth;
	private String returnStatus;

}
