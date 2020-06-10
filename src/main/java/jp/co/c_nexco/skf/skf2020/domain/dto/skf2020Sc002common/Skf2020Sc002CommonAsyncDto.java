/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2020.domain.dto.skf2020Sc002common;

import jp.co.c_nexco.skf.skf2020.domain.dto.common.Skf202010CommonAsyncDto;
import lombok.EqualsAndHashCode;

@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2020Sc002CommonAsyncDto extends Skf202010CommonAsyncDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// ユーザＩＤ
	private String userId;

	// 機関 コード
	private String agencyCd;
	// 部等 コード
	private String affiliation1Cd;
	// 室・課等コード
	private String affiliation2Cd;

	/**
	 * 自動車1台目
	 */
	// 自動車の保有
	private String parkingUmu;
	// 自動車の登録番号入力フラグ
	private String carNoInputFlg;

	/**
	 * 自動車2台目
	 */
	// 自動車の登録番号入力フラグ
	private String carNoInputFlg2;

	/**
	 * 現居住社宅
	 */
	// 現入居社宅
	private String nowShataku;
	// 保有社宅名
	private String nowShatakuName;
	// 室番号
	private String nowShatakuNo;
	// 規格（間取り）
	private String nowShatakuKikaku;
	// 規格名称（間取り）
	private String nowShatakuKikakuName;
	// 面積
	private String nowShatakuMenseki;
	// 社宅管理ID
	private long shatakuKanriId;

	/**
	 * 駐車場
	 */
	// 駐車場1台目
	private String parking1stPlace;
	// 駐車場2台目
	private String parking2stPlace;

	/**
	 * 返却備品
	 */
	// 返却備品
	private String returnEquipment;

	/**
	 * Hidden
	 */
	// 備品返却有無
	private String hdnBihinHenkyakuUmu;

}
