/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skfbatc.domain.dto.skfbatchutils1;

import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import lombok.EqualsAndHashCode;

@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class SkfBatchUtils1ExecuteDto extends BaseDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 結果コード
	private String returnStatus;

	/** 提示データ */
	private String teijiDataTeijiNo;

	/** 入居予定データ */
	private String ntYoteiDataShainNo;
	private String ntYoteiDataNyutaikyoKbn;

	/** 社宅管理台帳基本 */
	private String sLedgerShatakuKanriId;

	/** 社宅駐車場区画情報マスタ */
	private String sParkingBlockShatakuKanriNo;
	private String sParkingBlockParkingKanriNo;

	/** 社宅部屋情報マスタ */
	private String sRoomShatakuKanriNo;
	private String sRoomRoomKanriNo;

	/** 社宅管理台帳備品基本 */
	private String sBihinShatakuKanriId;

	/** 提示備品データ */
	private String tBihinDataTeijiNo;
	private String tBihinDataBihinCd;

	private String checkFlg;

}
