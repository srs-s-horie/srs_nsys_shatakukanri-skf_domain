/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.dto.skf3022Sc004common;

import jp.co.c_nexco.skf.skf3022.domain.dto.common.Skf302210CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3022Sc004CommonDto 次月予約登録画面の共通Dto。
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3022Sc004CommonDto extends Skf302210CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
	
	/** 呼び出しもとからのパラメータ **/
	// 提示番号
	private String hdnJigetuYoyakuTeijiNo;
	// 基準年月
	private String hdnJigetuYoyakuYearMonth;
	// 社宅管理台帳ID
	private String hdnJigetuYoyakuShatakuKanriId;
	// 社宅使用料月額
	private String hdnJigetuYoyakuRental;
	// 個人負担共益費月額
	private String hdnJigetuYoyakuKyoekihiPerson;
	// 駐車場1月額
	private String hdnJigetuYoyakuParkingRentalOne;
	// 駐車場2月額
	private String hdnJigetuYoyakuParkingRentalTwo;
	
	/** 画面パラメータ **/
	// 次月
	private String[] jigetu;
	// 次々月
	private String[] jijigetu;

	// 社宅使用料月額
	// - 次月
	private String shatakuPayJigetu;
	// - 次々月
	private String shatakuPayJijigetu;

	// 社宅使用料調整金額
	// - 次月
	private String txtStkTyoseiPayJigetu;
	private String txtStkTyoseiPayJigetuErr;
	private String txtStkTyoseiPayJigetuDisable;
	// - 次々月
	private String txtStkTyoseiPayJijigetu;
	private String txtStkTyoseiPayJijigetuErr;
	private String txtStkTyoseiPayJijigetuDisable;

	// 社宅使用料月額（調整後）
	// - 次月
	private String stkFinalPayJigetu;
	// - 次々月
	private String stkFinalPayJijigetu;

	// 個人負担共益費月額
	// - 次月
	private String cojinPayJigetu;
	// - 次々月
	private String cojinPayJijigetu;

	// 個人負担共益費調整金額
	// - 次月
	private String txtCojinChangePayJigetu;
	private String txtCojinChangePayJigetuErr;
	private String txtCojinChangePayJigetuDisable;
	// - 次々月
	private String txtCojinChangePayJijigetu;
	private String txtCojinChangePayJijigetuErr;
	private String txtCojinChangePayJijigetuDisable;
	
	// 個人負担共益費月額（調整後）
	// - 次月
	private String cojinFinalPayJigetu;
	// - 次々月
	private String cojinFinalPayJijigetu;
	
	// 区画1:駐車場使用料月額 
	// - 次月
	private String parkingPayOneJigetu;
	// - 次々月
	private String parkingPayOneJijigetu;
	
	// 区画2:駐車場使用料月額
	// - 次月
	private String parkingPayTwoJigetu;
	// - 次々月
	private String parkingPayTwoJijigetu;
	
	// 駐車場使用料調整金額
	// - 次月
	private String txtParkingChangePayJigetu;
	private String txtParkingChangePayJigetuErr;
	private String txtParkingChangePayJigetuDisable;
	// - 次々月
	private String txtParkingChangePayJijigetu;
	private String txtParkingChangePayJijigetuErr;
	private String txtParkingChangePayJijigetuDisable;
	
	// 駐車場使用料月額（調整後）
	// - 次月
	private String parkingFinalPayJigetu;
	// - 次々月
	private String parkingFinalPayJijigetu;
	
	// 月データ保持用
	private String hdnJigetu;
	private String hdnJijigetu;
	
	// 登録ボタン用メッセージ
	private String hdnRegistMessage;
	
}
