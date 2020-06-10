/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc005;

import java.util.List;
import java.util.Map;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090Sc005common.Skf3090Sc005CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3090Sc005ChangeDropDownAsyncDto 従業員マスタ登録ドロップダウン変更時Dto
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3090Sc005ChangeDropDownAsyncDto extends Skf3090Sc005CommonAsyncDto {

	private static final long serialVersionUID = -1902278406295003652L;

	/** ドロップダウンリスト系 */
	// ドロップダウンを表示するために必要な情報
	private String originalCompanyCd;
	private String agencyCd;
	private String affiliation1Cd;
	private String affiliation2Cd;
	private String businessAreaCd;

	// 会社ドロップダウンリスト
	List<Map<String, Object>> companyList;
	// 機関ドロップダウンリスト
	List<Map<String, Object>> agencyList;
	// 部等ドロップダウンリスト
	List<Map<String, Object>> affiliation1List;
	// 室・課等ドロップダウンリスト
	List<Map<String, Object>> affiliation2List;
	// 事業領域ドロップダウンリスト
	List<Map<String, Object>> businessAreaList;

}
