/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc001;

import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc001common.Skf3010Sc001CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3010_Sc001画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3010Sc001InitDto extends Skf3010Sc001CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
	
	// リストテーブルの１ページ最大表示行数
	private String listTableMaxRowCount;

}
