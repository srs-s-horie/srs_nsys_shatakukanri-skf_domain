/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.dto.skf3022Sc006common;

import java.util.Map;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3022Sc006CommonAsyncDto 提示データ登録画面の非同期処理共通Dto
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3022Sc006CommonAsyncDto extends AsyncBaseDto {

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
	private String hdnChushajoNoOne;
	private String hdnChushajoNoOneOld;
	private String sc006KukakuNoTwo;
	private String hdnChushajoNoTwo;
	private String hdnChushajoNoTwoOld;
	
	/** 共益費日割計算対応 2021/5/14 add start **/
	// 個人負担共益費調整金額
	private String sc006KyoekihiTyoseiPay;
	// 共益費支払月選択値
	private String sc006KyoekihiPayMonthSelect;
	// 個人負担共益費月額
	private String sc006KyoekihiMonthPay;
	// 共益費支払月に対する支払額
	private String sc006KyoekihiMonth;
	// 個人負担共益費入居時加算額
	private String sc006KyoekihiNyukyoKasan;
	// 個人負担共益費退居時加算額
	private String sc006KyoekihiTaikyoKasan;
	// 個人負担共益費月額(調整後)
	private String sc006KyoekihiPayAfter;
	/** 共益費日割計算対応 2021/5/14 add end **/
}
