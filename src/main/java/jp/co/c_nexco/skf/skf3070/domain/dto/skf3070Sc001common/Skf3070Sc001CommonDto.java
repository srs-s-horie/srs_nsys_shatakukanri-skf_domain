/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3070.domain.dto.skf3070Sc001common;

import java.util.List;
import java.util.Map;
import jp.co.c_nexco.skf.skf3070.domain.dto.common.Skf307010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3070Sc001 法定調書データ管理画面の共通Dto。
 * 
 * @author NEXCOシステムズ
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3070Sc001CommonDto extends Skf307010CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 検索結果表示用
	private List<Map<String, Object>> listTableData;
	private String listTableMaxRowCount;
	// 対象年プルダウン 基準月
	private String standardYear;

	// 所持物件数
	private String propertiesOwnedCnt;

	/* ドロップダウン */
	// 対象年ドロップダウン
	private List<Map<String, Object>> ddlTargetYearList;
	// 個人法人区分ドロップダウン
	private List<Map<String, Object>> ddlBusinessKbnList;
	// 個人番号ドロップダウン
	private List<Map<String, Object>> ddlAcceptFlgList;

}
