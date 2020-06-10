/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.dto.skf3090Sc006common;

import java.util.List;
import java.util.Map;
import jp.co.c_nexco.skf.skf3090.domain.dto.common.Skf309040CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3090Sc006CommonDto 組織マスタ一覧同期処理共通Dto
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3090Sc006CommonDto extends Skf309040CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// リストテーブルデータ
	private List<Map<String, Object>> createTableList;

	// ページの最大表示数
	private int maxPageCount;

	// 更新フラグ
	private String updateFlag;

	// 登録フラグ
	private String registFlag;

	/** 組織マスタ登録画面hidden項目連携用 */
	// 会社コード
	private String hdnCompanyCd;
	// 機関コード
	private String hdnAgencyCd;
	// 部等コード
	private String hdnAffiliation1Cd;
	// 室・課等
	private String hdnAffiliation2Cd;
	// 事業領域
	private String hdnBusinessAreaCd;

}
