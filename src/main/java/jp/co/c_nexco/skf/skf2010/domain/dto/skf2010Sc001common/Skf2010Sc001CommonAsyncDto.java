/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010Sc001common;

import java.util.List;
import java.util.Map;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.model.GridRelationDto;
import lombok.EqualsAndHashCode;

/**
 * TestPrjTop画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010Sc001CommonAsyncDto extends AsyncBaseDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 社員番号
	private String popShainNo;
	// 氏名
	private String popName;
	// 氏名（カナ）
	private String popNameKk;
	// 現所属
	private String popAgency;

	// リストテーブル
	private List<Map<String, Object>> popListTableList;
	// 入居関連付けフラグ
	private String nyukyoFlag;

	// エラー関連
	private String errShainNo;
	private String errName;
	private String errNameKk;
	private String errAgency;
}
