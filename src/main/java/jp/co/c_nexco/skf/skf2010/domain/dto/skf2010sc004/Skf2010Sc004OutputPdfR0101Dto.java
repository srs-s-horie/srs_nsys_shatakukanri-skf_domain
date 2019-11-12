/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc004;

import jp.co.c_nexco.skf.skf2010.domain.dto.common.Skf2010OutputPdfBaseDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2010Sc004 申請内容表示/引戻し画面の貸与（予定）社宅等のご案内PDF出力処理のDto。
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010Sc004OutputPdfR0101Dto extends Skf2010OutputPdfBaseDto {

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

	// 社宅退居区分
	private String shatakuTaikyoKbn;
	// 駐車場返還区分
	private String shatakuTaikyoKbn2;
}
