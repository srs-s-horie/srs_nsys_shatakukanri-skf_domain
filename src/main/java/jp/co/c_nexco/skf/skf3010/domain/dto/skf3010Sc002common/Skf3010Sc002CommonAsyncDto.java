/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc002common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.co.c_nexco.skf.skf3010.domain.dto.common.Skf301010CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3010Sc002CommonAsyncDto 保有社宅登録内共通DTO(非同期用)
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3010Sc002CommonAsyncDto extends Skf301010CommonAsyncDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 社宅管理番号
	private String hdnShatakuKanriNo;
	// 契約情報表示プルダウンインデックス
	private String hdnDispContractSelectedIndex;
	// 管理事業領域リスト
	private List<Map<String, Object>> manageBusinessAreaList;
	// 都道府県リスト
	private List<Map<String, Object>> prefList;
	// 社宅構造リスト
	private List<Map<String, Object>> shatakuStructureList;
	// エレベーターリスト
	private List<Map<String, Object>> elevatorList;
	// 契約番号リスト
	private List<Map<String, Object>> contractNoList;
	/** ドロップダウンリスト選択値 */
	// 地域区分選択値コード
	private String areaKbnCd;
	// 管理事業領域コード選択値
	private String manageBusinessAreaCd;
	// 都道府県コード選択値
	private String prefCd;
	// 社宅構造区分コード選択値
	private String structureKbn;
	// エレベーター区分選択値
	private String elevatorKbn;
	// 駐車場構造区分コード選択値
	private String parkingStructure;

	/** テキストエリア */
	// 郵便番号
	private String zipCd;
	// 建築年月日
	private String buildDate;
	// 実年数
	private String jituAge;
	// 次回算定年月日
	private String nextCalcDate;
	// 経年
	private String aging;
	// 駐車場基本使用料
	private String parkingBasicRent;
	// 駐車場台数
	private String parkingBlockCount;
	// 貸与可能総数
	private String lendPossibleCount;

	/** リストテーブル */
//	// 駐車場区画情報リスト
//	private List<Map<String, Object>> parkingInfoListTableData;
//	// 駐車場情報リスト(初期表示時：DB取得時)
//	private List<Map<String, Object>> hdnStartingParkingInfoListTableData;
	// 駐車場区画調整金額リスト
	private ArrayList <String> parkingRentalAdjusts;
	// 駐車場区画使用料リスト
	private List <String> parkingBlockRentManys;
	// 駐車場区画情報1行データ(id, 貸与区分, 貸与状況)
	private ArrayList<String> parkingBlockRowData;

	/** エラー系 **/
	// 郵便番号
	private String zipCdErr;
}
