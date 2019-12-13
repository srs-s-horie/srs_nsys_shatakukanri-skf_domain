/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2020.domain.dto.skf2020sc003;

import jp.co.c_nexco.skf.skf2020.domain.dto.skf2020Sc003common.Skf2020Sc003CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2020_Sc003画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2020Sc003UpdateDto extends Skf2020Sc003CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 編集前「必要とする社宅」
	private String defaultHitsuyoShataku;
}
