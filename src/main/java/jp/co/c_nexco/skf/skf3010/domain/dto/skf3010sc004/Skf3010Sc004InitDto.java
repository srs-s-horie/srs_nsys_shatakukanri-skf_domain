/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc004;

import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc004common.Skf3010Sc004CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3010_Sc004画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3010Sc004InitDto extends Skf3010Sc004CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
	
	// リストテーブルの１ページ最大表示行数
	private String listTableMaxRowCount;
	
	/** 社宅一覧画面連携用 */
    // 対象行の社宅区分
    private String hdnRowShatakuKbn;
    // 対象行の社宅管理番号
    private String hdnRowShatakuKanriNo;
    // 対象行の社宅名
    private String hdnRowShatakuName;
    // 対象行の地域区分
    private String hdnRowAreaKbn;
    // 対象行の空き部屋数
    private String hdnRowEmptyRoomCount;
    // 対象行の空き駐車場数
    private String hdnRowEmptyParkingCount;

}
