/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc001;

import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010Sc001common.Skf2010Sc001CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2010_Sc002画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010Sc001InitDto extends Skf2010Sc001CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 社員番号
	private String popShainNo;
	// 氏名
	private String popName;
	// 氏名（カナ）
	private String popNameKk;
	// 現所属
	private String popAgency;
	// 社宅管理番号
	private int popShatakuKanriNo;

}
