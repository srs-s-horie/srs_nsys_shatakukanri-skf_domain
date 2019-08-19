/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.dto.common;

import java.util.List;
import java.util.Map;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import lombok.EqualsAndHashCode;

/**
 * Skf301010CommonAsyncDto Skf301010非同期処理共通Dto
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf301010CommonAsyncDto extends AsyncBaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<String, Object> actingUser;

	private String act;

	private Integer limit;
	private Integer offset;
	private Integer pageNo;
	// 管理会社コード
	private String selectedCompanyCd;
	// 管理機関コード
	private String agencyCd;
	// 社宅区分コード
	private String shatakuKbnCd;
	// 空き部屋コード
	private String emptyRoomCd;
	// 利用区分コード
	private String useKbnCd;
	// 空き駐車場コード
	private String emptyParkingCd;
	// 社宅名
	private String shatakuName;
	// 社宅住所
	private String shatakuAddress;
	// 外部機関表示フラグ
	private Boolean agencyDispFlg;

	// 管理会社リスト
	List<Map<String, Object>> manageCompanyList;
	// 社宅区分リスト
	List<Map<String, Object>> shatakuKbnList;
	// 空き部屋リスト
	List<Map<String, Object>> emptyRoomList;
	// 管理機関リスト
	List<Map<String, Object>> manageAgencyList;
	// 利用区分リスト
	List<Map<String, Object>> useKbnList;
	// 空き駐車場リスト
	List<Map<String, Object>> emptyParkingList;

}
