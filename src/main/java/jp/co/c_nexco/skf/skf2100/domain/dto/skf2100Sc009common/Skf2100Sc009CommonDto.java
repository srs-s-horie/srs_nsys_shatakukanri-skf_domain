/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.dto.skf2100Sc009common;

import java.util.List;
import java.util.Map;
import jp.co.c_nexco.nfw.webcore.domain.model.GridRelationDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2100Sc009CommonDto モバイルルーター機器入力支援共通DTO
 *
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2100Sc009CommonDto extends GridRelationDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// リストテーブル情報
	private List<Map<String, Object>> listTableList;
	private String maxCount;
	// リストデータカウント
	private int dataCount;

	/** テキストボックス */
	/** 「通しNo」 */
	private String routerNo;
	/** 「電話番号」 */
	private String tel;
	/** 「契約終了日From」テキスト */
	private String sc009ContractEndDateFrom;
	/** 「契約終了日To」テキスト */
	private String sc009ContractEndDateTo;
	/** 「備考」 */
	private String biko;
}
