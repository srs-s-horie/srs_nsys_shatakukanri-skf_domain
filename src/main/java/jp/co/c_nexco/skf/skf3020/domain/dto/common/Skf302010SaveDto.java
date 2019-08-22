/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.dto.common;

import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3020 転任者調書情報保持DTO
 *
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf302010SaveDto extends BaseDto {

	private static final long serialVersionUID = -1902278406295003652L;
	
	/**
	 * 社員番号
	 */
	private String shainNo;
	/**
	 * 社員氏名
	 */
	private String name;
	/**
	 * 等級
	 */
	private String tokyu;
	/**
	 * 年齢
	 */
	private String age;
	/**
	 * 現所属
	 */
	private String nowAffiliation;
	/**
	 * 新所属
	 */
	private String newAffiliation;
	/**
	 * 備考
	 */
	private String biko;

}
