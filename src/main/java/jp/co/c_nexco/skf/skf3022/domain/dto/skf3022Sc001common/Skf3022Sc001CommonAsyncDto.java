/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.dto.skf3022Sc001common;

import java.util.List;
import java.util.Map;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3022Sc001CommonAsyncDto 社宅部屋入力支援非同期処理共通Dto
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3022Sc001CommonAsyncDto extends AsyncBaseDto {

	private static final long serialVersionUID = 1L;

	// リストテーブル情報
	private List<Map<String, Object>> listTableList;
	// リストデータカウント
	private int dataCount;

	/** テキストボックス */
	// 社宅名
	private String shatakuName;
	// 部屋番号
	private String roomNo;

	/** ドロップダウン */
	// 空き部屋ドロップダウン選択値
	private String sc001EmptyRoomSelect;
	// 空き駐車場ドロップダウン選択値
	private String sc001EmptyParkingSelect;
	// 規格ドロップダウン選択値
	private String sc001KikakuSelecte;
	// 用途ドロップダウン選択値
	private String sc001YoutoSelect;
}
