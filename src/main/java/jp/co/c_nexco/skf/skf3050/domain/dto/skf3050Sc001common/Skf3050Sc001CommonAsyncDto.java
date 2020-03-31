/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3050.domain.dto.skf3050Sc001common;

import java.util.List;
import java.util.Map;

import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3050Sc001CommonAsyncDto 社員番号一括設定画面の非同期共通Dto
 * 
 *  @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3050Sc001CommonAsyncDto extends AsyncBaseDto {
	
	private static final long serialVersionUID = -3199702538331253492L;
	
	//検索結果一覧用
	private List<Map<String, Object>> listTableData;
	
	//表示ページ番号
	private String listPage;
}
