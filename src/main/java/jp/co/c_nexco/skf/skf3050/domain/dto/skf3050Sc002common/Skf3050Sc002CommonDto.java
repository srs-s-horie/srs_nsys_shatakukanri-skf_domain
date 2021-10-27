/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3050.domain.dto.skf3050Sc002common;

import java.util.*;

import jp.co.c_nexco.nfw.webcore.domain.model.FileDownloadDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3050Sc002CommonDto 月次運用管理画面の同期共通Dto
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3050Sc002CommonDto extends FileDownloadDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
	
	/** ドロップダウンリスト */
	private List<Map<String, Object>> dropDownList;
	/** 月次処理状況グリッド */
	private List<Map<String, Object>> getujiGrid;
	
	/**
	 * hidden 項目
	 */
	/** 実行指示予定処理年月 */
	private String hdnJikkouShijiYoteiNengetsu;
	/** 実行指示予定カラム */
	private String hdnJikkouShijiYoteiShoriCol;
	/** 実行指示予定インデックス */
	private String hdnJikkouShijiYoteiShoriIdx;
	/** 選択された「対象年度」（ドロップダウン） */
	private String hdnSelectedTaisyonendo;
	/** 「仮計算」ボタン非活性判定用 */
	private boolean hdnBtnKariKeisanDisabled;
	/** 「締め処理」ボタン非活性判定用 */
	private boolean hdnBtnShimeShoriDisabled;
	/** 「給与連携データ作成」ボタン非活性判定用 */
	private boolean hdnBtnRenkeiDataSakuseiDisabled;
	/** 「締め解除」ボタン非活性判定用 */
	private boolean hdnBtnShimeKaijoDisabled;
	/** 「給与連携データ確定」ボタン非活性判定用 */
	private boolean hdnBtnRenkeiDataKakuteiDisabled;
	/** 「仮計算」ボタン押下時のメッセージ */
	private String hdnKariKeisanBtnMsg;
	/** 「締め処理」ボタン押下時のメッセージ */
	private String hdnShimeShoriBtnMsg;
	/** 「給与連携データ作成」ボタン押下時のメッセージ */
	private String hdnRenkeiDataSakuseiBtnMsg;
	/** 「締め解除」ボタン押下時のメッセージ */
	private String hdnShimeKaijoBtnMsg;
	/** 「給与連携データ確定」ボタン押下時のメッセージ */
	private String hdnRenkeiDataKakuteiBtnMsg;
	/** 備品貸与日について警告が出ている状態でも処理続行するか判定するフラグ */
	private String hdnBihinTaiyoWarnContinueFlg;
	/** 備品返却日について警告が出ている状態でも処理続行するか判定するフラグ */
	private String hdnBihinHenkyakuWarnContinueFlg;
	/** チェック処理等で表示する警告メッセージ */
	private String hdnWarnMsg;
	
	/** 「モバイルルーター締め処理」ボタン非活性判定用 */
	private boolean hdnBtnRouterShimeShoriDisabled;
	/** 「モバイルルーター締め解除」ボタン非活性判定用 */
	private boolean hdnBtnRouterShimeKaijoDisabled;
	/** 「モバイルルーター締め処理」ボタン押下時のメッセージ */
	private String hdnRouterShimeShoriBtnMsg;
	/** 「モバイルルーター締め解除」ボタン押下時のメッセージ */
	private String hdnRouterShimeKaijoBtnMsg;
	/** 実行指示予定カラム(ルーター用) */
	private String hdnJikkouShijiYoteiRouterShoriCol;
}
