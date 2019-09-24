/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc003;

import java.util.List;
import java.util.Map;

import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090Sc003common.Skf3090Sc003CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3090Sc003ChangeDropDownAsyncDto 管理機関マスタ一覧ドロップダウン変更時Dto
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3090Sc003ChangeDropDownAsyncDto extends Skf3090Sc003CommonAsyncDto {

	private static final long serialVersionUID = -1902278406295003652L;

	/**
	 * 追加フォーム用
	 */	
	// - 管理会社
	private String selectedAddManageCompanyCd;
	// - 事業領域コード
	private String addBusinessAreaCd;
	// - 事業領域名
	private String addBusinessAreaName;
	// 管理会社リスト
	List<Map<String, Object>> addManageCompanyList;
	// 管理機関リスト
	List<Map<String, Object>> addManageAgencyList;
	
}
