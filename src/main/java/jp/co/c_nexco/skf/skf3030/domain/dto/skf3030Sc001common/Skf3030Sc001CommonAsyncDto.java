/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3030.domain.dto.skf3030Sc001common;

import java.util.List;
import java.util.Map;

import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3030Sc001CommonDto 社宅管理台帳共通非同期処理DTO
 *
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3030Sc001CommonAsyncDto extends AsyncBaseDto {

	private static final long serialVersionUID = 5118154462552092190L;

	/** 管理機関ドロップダウンリスト */
	private List<Map<String, Object>> agencyDropDownList;

	/** 選択された管理会社ドロップダウンリスト */
	private String hdnAsyncCompanyAgencySelect;
	/** 選択された管理機関ドロップダウンリストの選択 */
	private String hdnAsyncAgencySelect;
	/** 選択された「年」ドロップダウンリストの選択 */
	private String hdnAsyncYearSelect;
	/** 選択された「月」ドロップダウンリストの選択 */
	private String hdnAsyncMonthSelect;
	/** 選択された 「社宅区分」ドロップダウンリストの選択 */
	private String hdnAsyncShatakuKbnSelect;
	/** 選択された「相互利用」ドロップダウンリストの選択 */
	private String hdnAsyncMutualuseSelect;
	/** 管理機関ドロップダウン非活性 */
	private String hdnAsyncAgencyDisabled;

	/** 「締め処理」ラベル */
	private String asyncLabelShimeShori;
	/** 「POSITIVE連携」ラベル */
	private String asyncLabelPositiveRenkei;

}
