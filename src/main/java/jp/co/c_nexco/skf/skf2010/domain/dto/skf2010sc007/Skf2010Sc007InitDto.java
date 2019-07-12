/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc007;

import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010Sc007common.Skf2010Sc007CommonDto;
import lombok.EqualsAndHashCode;

/**
 * 申請条件確認画面のinitDto。
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010Sc007InitDto extends Skf2010Sc007CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	/**
	 * 表示申請書類名用
	 */
	// 申請書類名
	private String applYoken;

}
