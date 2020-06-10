/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010Sc005common;

import java.util.List;
import java.util.Map;
import jp.co.c_nexco.skf.skf2010.domain.dto.common.Skf201030CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * TestPrjTop画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010Sc005CommonAsyncDto extends Skf201030CommonAsyncDto {

	private static final long serialVersionUID = -1902278406295003652L;

	/** ドロップダウン */
	/** ドロップダウン：機関リスト */
	private List<Map<String, Object>> ddlAgencyList;
	/** ドロップダウン：部等リスト */
	private List<Map<String, Object>> ddlAffiliation1List;
	/** ドロップダウン：室・課等リスト */
	private List<Map<String, Object>> ddlAffiliation2List;

}
