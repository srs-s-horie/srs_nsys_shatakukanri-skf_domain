package jp.co.c_nexco.skf.skf3090.domain.dto.common;

import java.util.List;
import java.util.Map;

import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import lombok.EqualsAndHashCode;

/**
 * TestPrjTop画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf309030Test03CommonAsyncDto extends AsyncBaseDto {
	/**
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 原籍会社コード
	private String originalCompanyCd;
	// 機関コード
	private String agencyCd;
	// 部等コード
	private String affiliation1Cd;
	// 室、チームまたは課コード
	private String affiliation2Cd;
	// 事業領域コード
	private String businessAreaCd;
	
	
	// 会社リスト
	private List<Map<String, Object>> companyList;			// IM標準版
	private String companyCdListJson;						// 共通FW版
	
	// 機関リスト
	private List<Map<String, Object>> agencyList;			// IM標準版
	private String agencyCdListJson;						// 共通FW版

	// 部等リスト
	private List<Map<String, Object>> affiliation1List;			// IM標準版
	private String affiliation1CdListJson;						// 共通FW版

	// 室・課等リスト
	private List<Map<String, Object>> affiliation2List;			// IM標準版
	private String affiliation2CdListJson;						// 共通FW版

	// 事業領域リスト
	private List<Map<String, Object>> businessAreaList;			// IM標準版
	private String businessAreaListJson;						// 共通FW版


}
