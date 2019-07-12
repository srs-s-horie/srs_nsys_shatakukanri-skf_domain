/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.dto.common;

import java.util.Map;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import lombok.EqualsAndHashCode;

/**
 * Skf309030CommonAsyncDto Skf309030非同期処理共通Dto
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf309030CommonAsyncDto extends AsyncBaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<String, Object> actingUser;

	private String act;

	private Integer limit;
	private Integer offset;
	private Integer pageNo;

}
