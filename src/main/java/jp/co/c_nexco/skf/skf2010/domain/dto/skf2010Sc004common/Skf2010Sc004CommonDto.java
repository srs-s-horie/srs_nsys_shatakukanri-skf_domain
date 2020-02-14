/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010Sc004common;

import jp.co.c_nexco.skf.skf2010.domain.dto.common.Skf2010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * TestPrjTop画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010Sc004CommonDto extends Skf2010CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 現社宅の退居日
	private String taikyobi;
	// 現社宅の駐車場返還日
	private String henkanbi;
	// 新社宅の入居日
	private String nyukyobi;
	// 新社宅の駐車場使用開始日
	private String shiyobi;
	// 新社宅の駐車場使用開始日2
	private String shiyobi2;
	// 各エラーClass設定
	private String taikyobiErr;
	private String henkanbiErr;
	private String nyukyobiErr;
	private String shiyobiErr;
	private String shiyobi2Err;

	// 社宅退居区分
	private String shatakuTaikyoKbn;
	// 駐車場返還区分
	private String shatakuTaikyoKbn2;

	// 表示レベル
	private int displayLevel;

	private String commentViewFlag;

	// コメント欄表示フラグ
	private boolean commentAreaVisible = true;

	// 入力フォーム表示フラグ
	private String inputAreaVisible;

	// 初期退居日
	private String syokiTaikyoDate;
	// 初期駐車場返還日
	private String syokiParkingDate;

	// 退居フラグ
	private boolean notTaikyo = false;

	// 操作ガイド
	private String operationGuide;
}
