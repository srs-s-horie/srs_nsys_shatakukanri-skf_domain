/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2030.domain.dto.skf2030Sc001common;

import jp.co.c_nexco.skf.skf2030.domain.dto.common.Skf203010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * TestPrjTop画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2030Sc001CommonDto extends Skf203010CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 搬入待ち専用レイアウトフラグ
	private boolean bihinReadOnly;
	// 「申請中」フラグ
	private boolean status01Flag;
	// 「搬入済」フラグ
	private boolean status24Flag;
	// 「搬入完了日」非活性フラグ
	private boolean completionDayDisabled;
	// 備品申請ラジオボタンフラグ
	private String bihinCheckFlag;

	// 備品希望設定
	// 洗濯機
	private String bihinAppl11;
	private String bihinApplText11;
	private String bihinAdjust11;
	private String bihinState11;
	private boolean bihinDisabled11;
	// 冷蔵庫
	private String bihinAppl12;
	private String bihinApplText12;
	private String bihinAdjust12;
	private String bihinState12;
	private boolean bihinDisabled12;
	// オーブンレンジ
	private String bihinAppl13;
	private String bihinApplText13;
	private String bihinAdjust13;
	private String bihinState13;
	private boolean bihinDisabled13;
	// 掃除機
	private String bihinAppl14;
	private String bihinApplText14;
	private String bihinAdjust14;
	private String bihinState14;
	private boolean bihinDisabled14;
	// 電子炊飯ジャー
	private String bihinAppl15;
	private String bihinApplText15;
	private String bihinAdjust15;
	private String bihinState15;
	private boolean bihinDisabled15;
	// テレビ
	private String bihinAppl16;
	private String bihinApplText16;
	private String bihinAdjust16;
	private String bihinState16;
	private boolean bihinDisabled16;
	// テレビ台
	private String bihinAppl17;
	private String bihinApplText17;
	private String bihinAdjust17;
	private String bihinState17;
	private boolean bihinDisabled17;
	// 座卓（こたつ）
	private String bihinAppl18;
	private String bihinApplText18;
	private String bihinAdjust18;
	private String bihinState18;
	private boolean bihinDisabled18;
	// キッチンキャビネット
	private String bihinAppl19;
	private String bihinApplText19;
	private String bihinAdjust19;
	private String bihinState19;
	private boolean bihinDisabled19;

	// コメント
	private String commentNote;
	// コメントボタン表示フラグ
	private boolean commentViewFlag;

	// ボタン表示フラグ
	// 申請条件を確認ボタン
	private boolean btnRequirementVisibled;
	
	/** ボタン表示非表示 */
	// 搬入完了ボタン
	private String btnImportFinidhedDisabled;
	
	// 一時保存ボタン
	private String btnSaveDisabled;
	
	// 申請ボタン
	private String btnApplicationDisabled;
	
}
