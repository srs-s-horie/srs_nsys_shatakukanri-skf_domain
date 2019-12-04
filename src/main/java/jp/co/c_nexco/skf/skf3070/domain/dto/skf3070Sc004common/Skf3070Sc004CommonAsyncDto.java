/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3070.domain.dto.skf3070Sc004common;

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
public class Skf3070Sc004CommonAsyncDto extends AsyncBaseDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 氏名又は名称
	private String popOwnerName;
	// 氏名又は名称（フリガナ）
	private String popOwnerNameKk;
	// 住所
	private String popAddress;
	// 個人法人区分
	private String popBusinessKbn;
	// 個人法人区分
	private List<Map<String, Object>> popBusinessKbnList;
	
	// リストテーブル
	private List<Map<String, Object>> popListTableList;

}