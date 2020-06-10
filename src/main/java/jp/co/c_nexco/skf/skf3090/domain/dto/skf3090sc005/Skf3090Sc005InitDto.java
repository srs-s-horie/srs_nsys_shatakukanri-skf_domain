/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc005;

import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090Sc005common.Skf3090Sc005CommonDto;
import lombok.EqualsAndHashCode;

@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3090Sc005InitDto extends Skf3090Sc005CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	/** 画面遷移時に設定されたコード */
	// 社員番号
	private String hdnShainNo;
	// 氏名
	private String hdnName;
	// 氏名カナ
	private String hdnNameKk;
	// 原籍会社コード
	private String hdnOriginalCompanyCd;
	// 機関コード
	private String hdnAgencyCd;
	// 部等コード
	private String hdnAffiliation1Cd;
	// 室・課等コード
	private String hdnAffiliation2Cd;
	// リストテーブルで選択された社員番号
	private String hdnSelectedShainNo;
	// リストテーブルで選択された会社コード
	private String hdnSelectedCompanyCd;
	// 更新フラグ
	private String updateFlag;

	/** DBから取得したhidden項目 */
	// ロールID
	private String roleId;
	// 登録フラグ
	private String registFlg;

	/** コントロール設定値 */
	private String shainNoDisabled;
	private String deleteRemoveFlag;

}
