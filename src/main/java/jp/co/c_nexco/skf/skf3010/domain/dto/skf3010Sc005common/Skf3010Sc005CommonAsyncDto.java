/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc005common;

import java.util.List;
import java.util.Map;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3010Sc005CommonAsyncDto Skf3010Sc005非同期処理共通Dto
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3010Sc005CommonAsyncDto extends AsyncBaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 本来用途
	private String originalAuse;
	//物置面積
	private String barnMenseki;
	//物置調整面積
	private String barnMensekiAdjust;
	

}
