/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.dto.skf2100Sc003common;

import java.util.List;
import java.util.Map;

import jp.co.c_nexco.skf.skf2100.domain.dto.common.Skf2100CommonDto;
import lombok.EqualsAndHashCode;

/**
 * モバイルルーター返却申請書画面の共通Dto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2100Sc003CommonDto extends Skf2100CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 社員情報のリスト
	private List<Map<String, String>> shainList;
	
	// 搬入待ち専用レイアウトフラグ
	private boolean bihinReadOnly;
	// 「申請中」フラグ
	private boolean status01Flag;
	// モバイルルータ情報表示フラグ
	private boolean routerInfoViewFlag;
	// 最終利用日編集フラグ
	private boolean lastUseDayEditFlag;
	
	// 貸出管理簿ID
	private String hdnRouterKanriId;
	
	// モバイルルーター通しNo
	private Long mobileRouterNo;
	// ICCID
	private String iccid;
	// IMEI
	private String imei;
	//ルーター返却申請書
	// 故障フラグ
	private String faultFlag;
	// 最終利用日
	private String lastUseDay;
	private String lastUseDaylbl;
	// 窓口返却日
	private String returnDay;
	private String returnDaylbl;
	// コメント
	private String commentNote;
	// コメントボタン表示フラグ
	private boolean commentViewFlag;
	// コメント入力欄表示フラグ
	private boolean commentInputFlag;


	// ボタン表示フラグ
	// 申請条件を確認ボタン
	private boolean btnRequirementVisibled;

	/** ボタン非活性フラグ */
	//　申請要件を確認ボタン
	private String btnCheckDisabled;
	// 一時保存ボタン
	private String btnSaveDisabled;
	// 取下げボタン
	private String btnCancelDisabled;
	// 申請ボタン
	private String btnApplicationDisabled;
	// 故障フラグ
	private String faultFlagDisabled;
	
	/** 「故障」チェックボックス選択状態 */
	private String hdnChkFaultSelect;
	
	/** エラー状態*/
	private String lastUseDayErr;
	private String telErr;

}
