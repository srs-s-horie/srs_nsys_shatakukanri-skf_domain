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
	
	// リストテーブルデータ
	private List<Map<String, Object>> listTableData;	
	private String listTableMaxRowCount;
	
	/**
	 * 検索フォーム用
	 */
	// 社員番号
	private String shainNo;
	// 社員氏名
	private String shainName;
	// 入居
	private String[] nyukyo;
	// 退居
	private String[] taikyo;
	// 変更
	private String[] henko;
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

	/** チェック／非チェックフラグ */
	// 入居
	private Boolean nyukyoChkFlg;
	// 退居
	private Boolean taikyoChkFlg;
	// 変更
	private Boolean henkoChkFlg;

	// チェックされた入居チェックボックス値の配列
	private String[] nyukyoChkVal;
	// チェックされた退居チェックボックス値の配列
	private String[] taikyoChkVal;
	// チェックされた変更チェックボックス値の配列
	private String[] henkouChkVal;	
	
	/** hidden項目連携用 */
	// 社員番号
	private String hdnShainNo;
	// 社員氏名
	private String hdnShainName;
	// 入居
	private String[] hdnNyukyo;
	// 退居
	private String[] hdnTaikyo;
	// 入居状態変更
	private String[] hdnHenko;
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
	/** チェック／非チェックフラグ */
	// 入居
	private Boolean hdnNyukyoChkFlg;
	// 退居
	private Boolean hdnTaikyoChkFlg;
	// 変更
	private Boolean hdnHenkoChkFlg;

	
	/** 画面連携用 */
	// 対象行の入居
	private String hdnRowNyukyo;
	// 対象行の退居
	private String hdnRowTaikyo;
	// 対象行の変更
	private String hdnRowHenko;
	// 対象行の社員番号
	private String hdnRowShainNo;
	// 対象行の社員氏名
	private String hdnRowShainName;
	// 対象行の等級
	private String hdnRowTokyu;
	// 対象行の年齢
	private String hdnRowAge;
	// 対象行の新所属
	private String hdnRowNewAffiliation;
	// 対象行の現所属
	private String hdnRowNowAffiliation;
	// 対象行の備考
	private String hdnRowBiko;
	// 対象行の取込日
	private String hdnRowTakingDate;
	// 対象行の入退居予定作成区分
	private String hdnRowNyutaikyoKbn;
	// 対象行の更新日時
	private String hdnRowUpdateDate;	
	
}
