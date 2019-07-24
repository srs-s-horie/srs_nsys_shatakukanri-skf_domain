/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.dto.skf3020Sc004common;

import java.util.List;
import java.util.Map;
import jp.co.c_nexco.skf.skf3020.domain.dto.common.Skf302010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * 転任者一覧画面のCommonDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3020Sc004CommonDto extends Skf302010CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
	
	/**
	 * 検索フォーム用
	 */
	// 社員番号
	private String shainNo;
	// 社員氏名
	private String shainName;
	// 入居
	private String nyukyo;
	// 入居
	private String[] chkNyukyo;
	// 退去
	private String taikyo;
	// 変更
	private String henko;
	// 現社宅区分
	private String genShatakuKubun;
	// 現所属
	private String genShozoku;
	// 新所属
	private String shinShozoku;
	// 入退居予定作成区分
	private String nyutaikyoYoteiSakuseiKubun;
	// 備考
	private String biko;
	// 入退居予定作成区分リスト
	private List<Map<String, Object>> yoteiSakuseiList;
	// 現社宅区分リスト
	private List<Map<String, Object>> genShatakuKubunList;

	/** hidden項目連携用 */
	// 社員番号
	private String hdnShainNo;
	// 社員氏名
	private String hdnShainName;
	// 入居
	private String hdnNyukyo;
	// 退居
	private String hdnTaikyo;
	// 入居状態変更
	private String hdnHenko;
	// 現社宅区分
	private String hdnGenShataku;
	// 現所属
	private String hdnGenShozoku;
	// 新所属
	private String hdnShinShozoku;
	// 入退居予定作成区分
	private String hdnNyutaikyoYoteiSakuseiKubun;
	// 備考
	private String hdnBiko;

}
