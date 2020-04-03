package jp.co.c_nexco.skf.skf2050.domain.dto.common;

import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import lombok.EqualsAndHashCode;

/**
 * TestPrjTop画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf205010CommonDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 戻るボタンの遷移先
	private String backUrl;

}
