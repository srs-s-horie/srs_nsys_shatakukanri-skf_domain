/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.dto.skf2040sc002;

import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040Sc002common.Skf2040Sc002CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2040_Sc002 退居（自動車の保管場所返還）届(アウトソース用）画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2040Sc002InitDto extends Skf2040Sc002CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 備品情報の表示エラー警告表示フラグ
	private String warningDispFlag;

}
