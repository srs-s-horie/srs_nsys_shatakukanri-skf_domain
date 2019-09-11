/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.dto.skf3090Sc007common;

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
public class Skf3090Sc007CommonDto extends Skf309040CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 会社コード
	private String registCompanyCd;
	// 機関コード
	private String registAgencyCd;
	// 機関名称
	private String registAgencyName;
	// 部等コード
	private String registAffiliation1Cd;
	// 部等名称
	private String registAffiliation1Name;
	// 室、チーム又は課コード
	private String registAffiliation2Cd;
	// 室、チーム又は課名称
	private String registAffiliation2Name;
	// 事業領域コード
	private String registBusinessAreaCd;
	/** エラー系 */
	// 機関コード
	private String agencyCdError;
	// 部等コード
	private String affiliation1CdError;
	// 室、チーム又は課コード
	private String affiliation2CdError;
	// 機関
	private String agencyNameError;
	// 部等
	private String affiliation1NameError;
	// 室、チーム又は課
	private String affiliation2NameError;

	// 事業領域ドロップダウンリスト
	List<Map<String, Object>> businessAreaList;

	// 登録フラグ
	private String registFlag;

}
