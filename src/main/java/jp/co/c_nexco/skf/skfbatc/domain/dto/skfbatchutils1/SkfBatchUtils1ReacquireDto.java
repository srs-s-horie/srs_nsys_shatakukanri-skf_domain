/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skfbatc.domain.dto.skfbatchutils1;

import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import lombok.EqualsAndHashCode;

@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class SkfBatchUtils1ReacquireDto extends BaseDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 結果コード
	private String returnStatus;
	/** 提示データ */
	private String teijiDataTeijiNo;

}
