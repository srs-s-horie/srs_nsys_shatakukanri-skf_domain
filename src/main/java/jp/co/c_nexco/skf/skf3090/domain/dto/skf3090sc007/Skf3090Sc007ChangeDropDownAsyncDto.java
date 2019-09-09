/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc007;

import java.util.List;
import java.util.Map;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090Sc007common.Skf3090Sc007CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3090Sc007ChangeDropDownAsyncDto 組織マスタ登録ドロップダウン変更時Dto
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3090Sc007ChangeDropDownAsyncDto extends Skf3090Sc007CommonAsyncDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 会社コード
	private String companyCd;
	// 機関コード
	private String agencyCd;
	// 部等コード
	private String affiliation1Cd;
	// 室、チーム又は課
	private String affiliation2Cd;
	// 事業領域
	private String businessAreaCd;

	// 会社リスト
	private List<Map<String, Object>> companyList;

	// 機関リスト
	private List<Map<String, Object>> agencyList;

	// 部等リスト
	private List<Map<String, Object>> affiliation1List;

	// 室、リーム又は課
	private List<Map<String, Object>> affiliation2List;

	// 事業領域リスト
	private List<Map<String, Object>> businessAreaList;

}
