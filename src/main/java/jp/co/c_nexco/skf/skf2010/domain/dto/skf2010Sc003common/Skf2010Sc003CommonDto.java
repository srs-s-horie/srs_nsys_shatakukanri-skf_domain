/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010Sc003common;

import java.util.List;
import java.util.Map;
import jp.co.c_nexco.skf.skf2010.domain.dto.common.Skf201020CommonDto;
import lombok.EqualsAndHashCode;

/**
 * TestPrjTop画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010Sc003CommonDto extends Skf201020CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 申請日From
	private String applDateFrom;
	// 申請日To
	private String applDateTo;
	// 承認日／修正依頼日From
	private String agreDateFrom;
	// 承認日／修正依頼日To
	private String agreDateTo;
	// 申請書類名
	private String applName;
	// 申請状況
	private String[] applStatus;
	
	private String applNo;
	private String applId;
	private String sendApplStatus;

	// 検索結果一覧 */
	private List<Map<String, Object>> ltResultList;

	// 操作ガイド
	private String operationGuide;

	// エラー項目
	private String applDateFromErr;
	private String applDateToErr;
	private String agreDateFromErr;
	private String agreDateToErr;
}
