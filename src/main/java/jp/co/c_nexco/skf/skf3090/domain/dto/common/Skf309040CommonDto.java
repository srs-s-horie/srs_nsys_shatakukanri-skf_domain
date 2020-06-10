package jp.co.c_nexco.skf.skf3090.domain.dto.common;

import java.util.List;
import java.util.Map;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import lombok.EqualsAndHashCode;

/**
 * Skf309040CommonDto Skf309040同期処理共通Dto
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf309040CommonDto extends BaseDto {

	private static final long serialVersionUID = 1L;

	// 会社コード
	private String companyCd;
	// 機関コード
	private String agencyCd;
	// 部等コード
	private String affiliation1Cd;
	// 室・課等コード
	private String affiliation2Cd;
	// 事業領域コード
	private String businessAreaCd;
	// 機関
	private String agencyName;
	// 部等
	private String affiliation1Name;
	// 室・課等
	private String affiliation2Name;

	/** ドロップダウン系 */
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
