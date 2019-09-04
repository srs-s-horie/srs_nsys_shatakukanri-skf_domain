/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.dto.skf2040Sc002common;

import java.util.List;
import java.util.Map;
import jp.co.c_nexco.skf.skf2040.domain.dto.common.Skf204010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2040_Sc002 退居（自動車の保管場所返還）届(アウトソース用）画面の共通Dto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2040Sc002CommonDto extends Skf204010CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 社宅退居区分
	private String shatakuTaikyoKbn;

	/*
	 * リスト
	 */
	// 備品の返却リスト
	List<Map<String, Object>> henkyakuList;

	/*
	 * 活性制御類
	 */

	// 承認ボタン
	private String btnApproveDisabled;
	// 提示ボタン
	private String btnPresentDisabeld;

	/*
	 * フラグ類
	 */
	// 備品返却欄表示フラグ
	private boolean bihinVisible;
	// 添付資料欄表示フラグ
	private String tenpViewFlag;
	// 退居（自動車の保管場所変換）届表示フラグ
	private String taikyoViewFlag;

	// 返却備品なしフラグ true:あり false:なし
	private String isHenkyakuBihinNothing;

	// コメントボタン表示フラグ
	private String commentBtnViewFlag;
	// 退居（自動車の保管場所変換）届PDF出力ボタン表示フラグ
	private boolean TaikyoPdfViewBtnFlag;
	// 提示ボタン表示フラグ
	private boolean presentBtnViewFlag;
	// 承認ボタン表示フラグ
	private boolean approveBtnViewFlag;
	// 修正依頼ボタン表示フラグ
	private boolean revisionBtnViewFlag;
	// 差戻しボタン表示フラグ
	private boolean remandBtnViewFlag;
	// 資料を添付
	private boolean shiryoBtnViewFlag;

	/*
	 * hidden
	 */
	// 備品返却申請の書類管理番号
	private String hdnBihinHenkyakuApplNo;
}
