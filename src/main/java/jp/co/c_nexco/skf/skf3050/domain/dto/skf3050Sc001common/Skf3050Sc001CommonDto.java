/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3050.domain.dto.skf3050Sc001common;

import java.util.*;

import jp.co.c_nexco.skf.skf3050.domain.dto.common.Skf305010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3050Sc001CommonDto 社員番号一括設定画面のInitDto。
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3050Sc001CommonDto extends Skf305010CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;

	//検索結果一覧用
	private List<Map<String, Object>> listTableData;
	private String listTableMaxRowCount;
	
	//活性制御
	//社員情報確認ボタン
	private String btnShainInfoCheckDisabled;
	//登録ボタン
	private String btnRegistDisabled;
	
	//社員情報文字列
	private String shainListData;
	
	//表示ページ番号
	private String listPage;
}
