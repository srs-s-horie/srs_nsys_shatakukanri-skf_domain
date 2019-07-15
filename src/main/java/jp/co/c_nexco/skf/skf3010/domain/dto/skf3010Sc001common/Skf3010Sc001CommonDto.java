/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc001common;

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
public class Skf3010Sc001CommonDto extends Skf301010CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
	
	// リストテーブルデータ
	private List<Map<String, Object>> listTableData;
	// 会社コード
	private String hdnSelectedCompanyCd;
	// 機関コード
	private String hdnAgencyCd;
	// 空き部屋
	private String hdnEmptyRoom;
	// 利用区分
	private String hdnUseKbn;
	// 空き駐車場
	private String hdnEmptyParking;
	// 社宅住所
	private String hdnShatakuAddress;

	/** 画面連携用 */
	// 対象行の社宅区分
	private String hdnShatakuKbn;
	// 対象行の社宅管理番号
	private String hdnShatakuKanriNo;
	// 対象行の社宅名
	private String hdnShatakuName;
	// 対象行の地域区分
	private String hdnAreaKbn;
	// 対象行の空き部屋数
	private String hdnEmptyRoomCount;
	// 対象行の空き駐車場数
	private String hdnEmptyParkingCount;
	// 戻るURL
	// private String backUrl;

}
