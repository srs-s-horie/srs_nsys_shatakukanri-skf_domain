/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.dto.skf3022Sc005common;

import java.util.*;

import jp.co.c_nexco.skf.skf3022.domain.dto.common.Skf302210CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3022Sc005CommonDto 提示データ一覧画面の共通Dto。
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3022Sc005CommonDto extends Skf302210CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
	
	//検索条件
	// 社員番号
	private String shainNo;
	// 社員名
	private String shainName;
	// 社宅名
	private String shatakuName;
	// 入退居区分
	private String nyutaikyoKbn;
	// 入退居区分リスト
	private List<Map<String, Object>> nyutaikyoKbnList;
	// 社宅提示
	// 社宅提示状況
	private String stJyokyo;
	// 社宅提示状況リスト
	private List<Map<String, Object>> stJyokyoList;
	// 社宅提示確認督促
	private String stKakunin;
	// 社宅提示確認督促リスト
	private List<Map<String, Object>> stKakuninList;
	// 備品提示
	// 備品提示状況
	private String bhJyokyo;
	// 備品提示状況リスト
	private List<Map<String, Object>> bhJyokyoList;
	// 備品提示確認督促
	private String bhKakunin;
	// 備品提示確認督促リスト
	private List<Map<String, Object>> bhKakuninList;
	// 備品搬入搬出督促
	private String moveInOut;
	// 備品搬入搬出督促リスト
	private List<Map<String, Object>> moveInOutList;
	
	//検索結果一覧用
	private List<Map<String, Object>> listTableData;
	private String listTableMaxRowCount;
	
	//ボタン制御
	private String btnShatakuTeijiDisabled;
	private String btnBihinTeijiDisabled;
	private String btnBihinInOutDisabled;
	//督促カウント
	private String hdnStTeijiCnt;
	private String hdnBhTeijiCnt;
	private String hdnMoveInOutCnt;
	
}
