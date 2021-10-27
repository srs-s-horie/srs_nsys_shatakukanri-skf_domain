/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.dto.skf2100Sc005common;

import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2100Sc005CommonAsyncDto モバイルルーター貸出管理簿共通非同期処理DTO
 *
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2100Sc005CommonAsyncDto extends AsyncBaseDto {

	private static final long serialVersionUID = 5118154462552092190L;


	/** 選択された「年」ドロップダウンリストの選択 */
	private String hdnAsyncYearSelect;
	/** 選択された「月」ドロップダウンリストの選択 */
	private String hdnAsyncMonthSelect;
	/** 選択された 「契約区分」ドロップダウンリストの選択 */
	private String hdnAsyncContaractKbnSelect;

	/** 「締め処理」ラベル */
	private String asyncLabelShimeShori;
	/** 「POSITIVE連携」ラベル */
	private String asyncLabelPositiveRenkei;

}
