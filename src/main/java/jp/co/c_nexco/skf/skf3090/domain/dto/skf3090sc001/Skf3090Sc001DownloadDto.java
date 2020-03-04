/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc001;

import java.util.*;

import jp.co.c_nexco.nfw.webcore.domain.model.FileDownloadDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3090_Sc001画面のDownloadDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3090Sc001DownloadDto extends FileDownloadDto {
	
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
	
}
