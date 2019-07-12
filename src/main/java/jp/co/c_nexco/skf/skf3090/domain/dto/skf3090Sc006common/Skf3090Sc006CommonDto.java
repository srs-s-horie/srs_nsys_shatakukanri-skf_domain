/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.dto.skf3090Sc006common;

import java.util.List;
import java.util.Map;
import jp.co.c_nexco.skf.skf3090.domain.dto.common.Skf309040CommonDto;
import lombok.EqualsAndHashCode;

/**
 * TestPrjTop画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3090Sc006CommonDto extends Skf309040CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// value値
	// 会社コード
	private String companyCd;

	// 機関コード
	private String agencyCd;

	// 部等コード
	private String affiliation1Cd;

	// 課
	private String affiliation2Cd;

	// 事業領域
	private String businessAreaCd;

	// 「管理会社」ドロップダウンリスト
	private List<Map<String, Object>> companyList;

	// 「機関」ドロップダウンリスト
	private List<Map<String, Object>> agencyList;

	// 「部等」ドロップダウンリスト
	private List<Map<String, Object>> affiliation1List;

	// 「室、チーム又は課」ドロップダウンリスト
	private List<Map<String, Object>> affiliation2List;

	// 「事業領域」ドロップダウンリスト
	private List<Map<String, Object>> businessAreaList;

	// リストテーブルデータ
	private List<Map<String, Object>> listTableData;

}
