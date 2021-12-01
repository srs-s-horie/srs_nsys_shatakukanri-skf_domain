/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.dto.skf2100Sc004common;

import java.util.Date;
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
public class Skf2100Sc004CommonDto extends Skf2100CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 社員情報のリスト
	private List<Map<String, String>> shainList;
	
	// 日付
	private String yearMonthDay;
	
	// ボタン表示モードレベル
	private int dispMode;
	// モバイルルータ情報表示フラグ
	private boolean routerInfoViewFlag;
	//窓口返却日編集フラグ
	private boolean returnDayEditFlag;	
	// 承認可否フラグ
	private boolean hdnApprovalFlg;
	
	/** 「故障」チェックボックス選択状態 */
	private String hdnChkFaultSelect;
	
	// 貸出管理簿ID
	private Long hdnRouterKanriId;
	
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
	// 承認ボタン
	private String btnApproveDisabled;
	//　承認者用ボタン表示フラグ
	private boolean allButtonEscape;
	// 故障フラグ
	private String faultFlagDisabled;
	
	/** エラー */
	// 窓口返却日
	private String returnDayErr;
	
	// 社員番号
	private String hdnApplShainNo;
	// 申請書類履歴テーブル申請日
	private Date applHistoryDate;
	
	// 閲覧フラグ
	private boolean viewFlag;
}
