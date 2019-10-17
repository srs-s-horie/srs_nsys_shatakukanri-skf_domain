/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc001common;

import java.util.List;
import java.util.Map;
import jp.co.c_nexco.skf.skf3010.domain.dto.common.Skf301010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3010Sc001CommonDto 社宅一覧内共通DTO(同期用)
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3010Sc001CommonDto extends Skf301010CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
	
	// リストテーブルデータ
	private List<Map<String, Object>> listTableData;
	// 管理会社コード
	private String hdnSelectedCompanyCd;
	// 管理機関コード
	private String hdnAgencyCd;
	// 社宅区分コード
	private String hdnShatakuKbnCd;
	// 空き部屋コード
	private String hdnEmptyRoomCd;
	// 利用区分コード
	private String hdnUseKbnCd;
	// 空き駐車場コード
	private String hdnEmptyParkingCd;
	// 社宅名
	private String hdnShatakuName;
	// 社宅住所
	private String hdnShatakuAddress;

	/** 画面連携用 */
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
	// 複写フラグ
	private String copyFlg;

}
