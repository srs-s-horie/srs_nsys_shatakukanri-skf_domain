/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc002;

import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc002common.Skf3010Sc002CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3010Sc002ChangeParkingLendKbnDrpDwnAsyncDto 保有社宅登録」のドロップダウンリスト変更時Dto。
 * 駐車場情報タブの「貸与区分」ドロップダウンチェンジ
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3010Sc002DeleteParkingBlockAsyncDto extends Skf3010Sc002CommonAsyncDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 駐車場区画削除フラグ
	private Boolean parkingBlockDeleteFlg;
	// 削除駐車場管理番号
	private String deleteParkingKanriNo;
	// 削除駐車場区画番号
	private String deleteParkingBlockNo;
	// 社宅管理番号
	private String hdnShatakuKanriNo;
}
