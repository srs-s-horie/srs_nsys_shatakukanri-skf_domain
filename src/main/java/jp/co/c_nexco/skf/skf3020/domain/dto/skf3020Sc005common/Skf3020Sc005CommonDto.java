/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.dto.skf3020Sc005common;

import jp.co.c_nexco.skf.skf3020.domain.dto.common.Skf302010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * TestPrjTop画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3020Sc005CommonDto extends Skf302010CommonDto {
	
	private static final long serialVersionUID = -3754681700089628686L;
	
	/********** 
	 * 画面項目 *
	 **********/
	/** 社員番号 */
	private String shainNo;
	/** 「社員番号の変更が必要」チェックボックス */
	private String[] id_check_shainNo;
	/** 社員氏名  */
	private String txtShainMei;
	/** 等級  */
	private String txtTokyu;
	/** 年齢  */
	private String txtNenrei;
	/** 新所属  */
	private String txtShinShozoku;
	/** 現所属  */
	private String txtGenShozoku;
	/** 備考  */
	private String txtBiko;
	
	/********** 
	 * hidden *
	 **********/
	/** 入退居予定作成区分  */
	private String hdnNyutaikyoYoteiKbn;
	/** 更新日時(転任者)  */
	private String hdnUpdateDateTenninsha;
	/** 更新日時(社員)  */
	private String hdnUpdateDateShain;
	/** 登録ボタン押下時メッセージ  */
	private String enterMsg;	
	/** 前に戻るボタン押下時メッセージ  */
	private String backMsg;
	/** 「社宅一覧」の選択行の社員番号 */
	private String hdnRowShainNo;
	/** 画面テキスト項目制御 */
	private boolean txtDisabled;
	/** 「登録」ボタン非活性 */
	private boolean hdnBtnTorokuDisabled;
	/** 「社員番号の変更が必要」チェックボックス非活性 */
	private boolean hdnChkShainNoHenkoKbnDisabled;


}
