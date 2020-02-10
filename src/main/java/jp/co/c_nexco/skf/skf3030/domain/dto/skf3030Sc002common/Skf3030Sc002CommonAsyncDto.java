/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3030.domain.dto.skf3030Sc002common;

import java.util.Map;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3030Sc002CommonAsyncDto 入退居情報登録画面の非同期処理共通Dto
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3030Sc002CommonAsyncDto extends AsyncBaseDto {

	private static final long serialVersionUID = 1L;

	// 社宅管理番号
	private String hdnShatakuKanriNo;
	// 非同期処理連携パラメータ
	private Map<String, String> mapParam;

	/** 使用料計算(提示データ登録画面)戻り値 */
	private String sc006TyusyaMonthPayAfter;
	private String sc006SiyoryoHiwariPay;
	private String sc006SyatauMonthPayAfter;
	private String sc006ShiyoryoTsukigaku;
	private String sc006TyusyaDayPayOne;
	private String sc006TyusyaMonthPayOne;
	private String sc006TyusyaDayPayTwo;
	private String sc006TyusyaMonthPayTwo;
	private String sc006KyoekihiPayAfter;
	private String hdnKaiSanAfterShatakuShiyoryoHiwariKingaku;
	private String hdnKaiSanAfterShatakuShiyoryoGetsugakuChoseigo;
	private String hdnKaiSanAfterKukaku1ChushajoShiyoroHiwariKingaku;
	private String hdnKaiSanAfterKukaku2ChushajoShiyoroHiwariKingaku;
	private String hdnKaiSanAfterChushajoShiyoryoGetsugakuChoseigo;
	private String hdnKaiSanAfterShatakuShiyoryoGetsugaku;
	private String hdnKaiSanAfterKukaku1ChushajoShiyoroGetsugaku;
	private String hdnKaiSanAfterKukaku2ChushajoShiyoroGetsugaku;
	private String hdnKaiSanAfterKojinFutanKyoekihiGetsugakuChoseigo;

	/** 使用料計算(提示データ登録画面)入力パラメータ */
	// パラメータ：駐車場管理番号
	private String sc006ChushajoKanriNo;
	// パラメータ：駐車場区画種別
	private String sc006ParkBlockKind;

	/** 駐車場変更戻り値 */
	// 駐車場区画更新日時
	private String hdnParkingBlockUpdateDate;

	/** 使用料計算入力パラメータ */
	private String sc006KukakuNoOne;
	private String hdnChushajoKanriNo1;
	//private String hdnChushajoNoOneOld;
	private String sc006KukakuNoTwo;
	private String hdnChushajoKanriNo2;
	//private String hdnChushajoNoTwoOld;
}