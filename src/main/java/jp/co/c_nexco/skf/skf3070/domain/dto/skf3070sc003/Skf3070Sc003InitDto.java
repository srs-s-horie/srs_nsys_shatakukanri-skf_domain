/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3070.domain.dto.skf3070sc003;

import java.util.*;
import jp.co.c_nexco.skf.skf3070.domain.dto.skf3070Sc003common.Skf3070Sc003CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3070_Sc003画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3070Sc003InitDto extends Skf3070Sc003CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
	
	// 所有物件一覧表示用
	private List<Map<String, Object>> listTableData;
	
	// リストテーブルの１ページ最大表示行数
	private String listTableMaxRowCount;
	

}
