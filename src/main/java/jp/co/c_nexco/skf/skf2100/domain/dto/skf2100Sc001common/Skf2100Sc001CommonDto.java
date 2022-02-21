/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.dto.skf2100Sc001common;

import java.util.List;
import java.util.Map;

import jp.co.c_nexco.skf.skf2100.domain.dto.common.Skf2100CommonDto;
import lombok.EqualsAndHashCode;

/**
 * モバイルルーター借用希望申請書画面の共通Dto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2100Sc001CommonDto extends Skf2100CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 社員情報のリスト
	private List<Map<String, String>> shainList;
	
	// 搬入待ち専用レイアウトフラグ
	private boolean bihinReadOnly;
	// 「申請中」フラグ
	private boolean status01Flag;
	// 「搬入済」フラグ
	private boolean status24Flag;
	// 「搬入完了日」非活性フラグ
	private boolean completionDayDisabled;
	// 備品申請ラジオボタンフラグ
	private String bihinCheckFlag;
	// モバイルルータ情報表示フラグ
	private boolean routerInfoViewFlag;
	// 申請可否フラグ
//	private String hdnConfirmFlg;

	
	// ルーター借用希望申請書
	// 利用開始希望日
	private String useStartHopeDay;
	private String useStartHopeDaylbl;
	// モバイルルーター通しNo
	private Long mobileRouterNo;
	// ICCID
	private String iccid;
	// IMEI
	private String imei;
	// 発送日
	private String shippingDate;
	private String shippingDatelbl;
	// 受領日
	private String receivedDate;
	private String receivedDatelbl;
	// モバイルルーター本体受領チェックフラグ
	private String bodyReceiptCheckFlag;
	// モバイルルーター手引き受領チェックフラグ
	private String handbookReceiptCheckFlag;
	// 返却資材受領チェックフラグ
	private String materialsReceivedCheckFlag;
	// 利用開始希望日編集フラグ
	private boolean useStartHopeDayEditFlag;
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

	// モバイルルーター本体受領チェックフラグ
	private String hdnBodyReceiptChecked;
	// モバイルルーター手引き受領チェックフラグ
	private String hdnHandbookReceiptChecked;
	// 返却資材受領チェックフラグ
	private String hdnMaterialsReceivedChecked;

	// ボタン表示フラグ
	// 申請条件を確認ボタン
	private boolean btnRequirementVisibled;

	/** ボタン非活性フラグ */
	// 搬入完了ボタン
	private String btnImportFinidhedDisabled;	
	//　申請要件を確認ボタン
	private String btnCheckDisabled;
	// 一時保存ボタン
	private String btnSaveDisabled;
	// 取下げボタン
	private String btnCancelDisabled;
	// 申請ボタン
	private String btnApplicationDisabled;
	// モバイルルーター本体受領チェックフラグ
	private String bodyReceiptCheckFlagDisabled;
	// モバイルルーター手引き受領チェックフラグ
	private String handbookReceiptCheckFlagDisabled;
	// 返却資材受領チェックフラグ
	private String materialsReceivedCheckFlagDisabled;
	
	// 日付
//	private String yearMonthDay;
	
	/** エラー状態*/
	private String useStartHopeDayErr;
	private String mailAddressErr;
	private String telErr;
	private String receivedDateErr;

}
