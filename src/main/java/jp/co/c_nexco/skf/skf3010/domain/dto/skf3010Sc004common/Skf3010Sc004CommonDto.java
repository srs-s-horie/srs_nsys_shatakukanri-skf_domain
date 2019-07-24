/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc004common;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import jp.co.c_nexco.skf.skf3010.domain.dto.common.Skf301010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * TestPrjTop画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3010Sc004CommonDto extends Skf301010CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
	
	// リストテーブルデータ
	private List<Map<String, Object>> listTableData;

	/**
	 * 社宅部屋情報マスタ
	 */
	// 部屋管理番号
	private Long shatakuRoomKanriNo;
	// 部屋名ROOM_NO
	private String roomNo;
	// 本来社宅用途ORIGINAL_AUSE
	private String originalAuse;
	// 本来規格ORIGINAL_KIKAKU
	private String originalKikaku;
	// 本来延面積ORIGINAL_MENSEKI
	private BigDecimal originalMenseki;
	// 貸与区分LEND_KBN
	private String lendKbn;
	// 備考BIKO
	private String biko;

	/**
	 * 社宅社員マスタ
	 */
	// 氏名漢字
	private String name;

	/**
	 * 検索フォーム用
	 */
	// 社宅名
	private String shatakuName;
	// 地域区分
	private String areaKbn;
	// 社宅区分
	private String shatakuKbn;
	// 空き部屋数
	private String emptyRoomCount;
	// 空き駐車場数
	private String emptyParkingCount;
	// 社宅管理番号
	private Long shatakuKanriNo;
    
	/** 部屋登録画面hidden項目連携用 */
	// 社宅管理番号
	private String hdnShatakuKanriNo;
	// 部屋管理番号
	private String hdnRoomKanriNo;
	// 社宅名
	private String hdnShatakuName;
	// 地域区分
	private String hdnAreaKbn;
	// 社宅区分
	private String hdnShatakuKbn;
	// 本来用途
	private String hdnOriginalAuse;
	// 貸与区分
	private String hdnLendKbn;
	// 空き部屋数
	private String hdnEmptyRoomCount;
	// 空き駐車場数
	private String hdnEmptyParkingCount;

	// 更新フラグ
	private String updateFlag;

	/** ドロップダウンリスト系 */
	// 本来用途リスト
	private List<Map<String, Object>> auseList; // IM標準版

	// 貸与区分リスト
	private List<Map<String, Object>> lendList; // IM標準版
}
