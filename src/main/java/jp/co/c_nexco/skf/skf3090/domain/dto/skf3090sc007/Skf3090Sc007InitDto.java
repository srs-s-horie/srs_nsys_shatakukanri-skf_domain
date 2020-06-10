/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc007;

import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090Sc007common.Skf3090Sc007CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3090_Sc007画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3090Sc007InitDto extends Skf3090Sc007CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	/** 画面遷移時に設定されたコード */
	// 会社コード
	private String hdnCompanyCd;
	// 機関コード
	private String hdnAgencyCd;
	// 部等コード
	private String hdnAffiliation1Cd;
	// 室・課等コード
	private String hdnAffiliation2Cd;
	// 事業領域
	private String hdnBusinesAreaCd;
	// 更新フラグ
	private String updateFlag;

	/** コントロール値 */
	// 会社ドロップダウン操作可否判定
	private String companyCdDisabled;
	// 機関コードテキストボックス操作可否判定
	private String agencyCdDisabled;
	// 機関コード名称を検索ボタン操作可判定
	private String agencyCdSearchDisabled;
	// 部等コードテキストボックス操作可否判定
	private String affiliation1CdDisabled;
	// 部等コード名称を検索ボタン操作可否判定
	private String affiliation1CdSearchDisabled;
	// 室・課等コードテキストボックス操作可否判定
	private String affiliation2CdDisabled;
	// 機関テキストボックス操作可否判定
	private String agencyNameDisabled;
	// 部等テキストボックス操作可否判定
	private String affiliation1NameDisabled;
	// 室・課等テキストンボックス操作可否判定
	private String affiliation2NameDisabled;
	// 事業領域ドロップダウン操作可否判定
	private String businessAreaCdDisabled;
	// 登録ボタン操作可否判定
	private String registButtonDisabled;
	// 登録ボタン表示可否判定
	private String registButtonRemove;
	// 削除ボタン操作可否判定
	private String deleteButtonDisabled;
}
