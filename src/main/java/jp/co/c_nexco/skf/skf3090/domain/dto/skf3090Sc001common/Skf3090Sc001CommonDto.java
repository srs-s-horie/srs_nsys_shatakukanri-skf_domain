/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.dto.skf3090Sc001common;

import java.util.*;
import jp.co.c_nexco.skf.skf3090.domain.dto.common.Skf309010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * TestPrjTop画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3090Sc001CommonDto extends Skf309010CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;


	/**
	 * listTable用
	 */
	// - 検索結果一覧
	private List<Map<String, Object>> listTableData;
	
	// 改定日
	private String hdnEffectiveDate;	
	// 状態区分
	private String hdnJyotaiKbn;

	private String fileBoxErr;
}
