/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc006;

import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc006common.Skf3010Sc006CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3010Sc006ChangeBarnMensekiAsyncDto 本来用途ドロップダウン変更時Dto
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3010Sc006ChangeBarnMensekiAsyncDto extends Skf3010Sc006CommonAsyncDto {

	private static final long serialVersionUID = -1902278406295003652L;
	
	//本来用途
	private String originalAuse;
	//物置面積
	private String barnMenseki;
	//物置調整面積
	private String barnMensekiAdjust;
	
}
