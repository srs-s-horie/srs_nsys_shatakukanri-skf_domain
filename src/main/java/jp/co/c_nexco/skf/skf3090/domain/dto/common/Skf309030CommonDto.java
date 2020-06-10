/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.dto.common;

import java.util.List;
import java.util.Map;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import lombok.EqualsAndHashCode;

/**
 * Skf309030CommonDto Skf309030同期処理共通Dto
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf309030CommonDto extends BaseDto {

	private static final long serialVersionUID = 1L;

	// 社員番号
	private String shainNo;
	// 社員名
	private String name;
	// 社員名（フリガナ）
	private String nameKk;
	// 機関コード
	private String agencyCd;
	// 部等コード
	private String affiliation1Cd;
	// 室、チームまたは課コード
	private String affiliation2Cd;

	/** ドロップダウンリスト系 */
	// 会社ドロップダウンリスト
	List<Map<String, Object>> companyList;
	// 機関ドロップダウンリスト
	List<Map<String, Object>> agencyList;
	// 部等ドロップダウンリスト
	List<Map<String, Object>> affiliation1List;
	// 室・課等ドロップダウンリスト
	List<Map<String, Object>> affiliation2List;
	
	// 検索フラグ
	private String searchFlag;
}
