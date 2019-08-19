/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.dto.skf3020Sc005common;

import java.util.*;

import jp.co.c_nexco.skf.skf3020.domain.dto.common.Skf302010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * TestPrjTop画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3020Sc005CommonDto extends Skf302010CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;

	/** 画面項目 */
	// 社員番号
	private String shainNo;
	// 社員入力支援
	// 仮社員番号設定(社員番号の変更が必要)
	private Boolean chkShainNoHenkoKbn;
	// 社員氏名
	private String shainName;
	// 等級
	private String tokyu;
	// 年齢
	private String age;
	// 新所属
	private String newAffiliation;
	// 現所属
	private String nowAffiliation;
	// 備考
	private String biko;
	// 入退居予定作成区分
	private String hdnNyutaikyoYoteiKbn;
	// 更新日時(転任者)
	private String hdnUpdateDateTenninsha;
	// 更新日時(社員)
	private String hdnUpdateDateShain;
	
	/** ボタン押下時メッセージ */
	// 登録ボタン押下時メッセージ
	private String enterMsg;	
	// 前に戻るボタン押下時メッセージ
	private String backMsg;
	
	/** 画面連携用 */
	// 選択行の社員番号
	private String hdnRowShainNo;
}
