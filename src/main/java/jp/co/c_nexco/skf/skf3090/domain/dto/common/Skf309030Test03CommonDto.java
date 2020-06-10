package jp.co.c_nexco.skf.skf3090.domain.dto.common;

import java.util.List;
import java.util.Map;

import jp.co.c_nexco.nfw.webcore.app.DownloadFile;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import lombok.EqualsAndHashCode;

/**
 * TestPrjTop画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf309030Test03CommonDto extends BaseDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<String, Object> actingUser;
	
	// 社員番号
	private String shainNo;
	// 社員名
	private String name;
	// 社員名（フリガナ）
	private String nameKk;
	// 会社コード
	private String companyCd;
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
	
	/** エラーコード関係 */
	private String nameErr;
	private String nameKkErr;
	private String shainNoErr;
	private String companyCdErr;

	
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
	private String businessAreaCdListJson;						// 共通FW版

	private List<DownloadFile> dlFile;
	
	private String preOpenPage;

	private Integer limit;
	private Integer offset;
	private Integer page;
}
