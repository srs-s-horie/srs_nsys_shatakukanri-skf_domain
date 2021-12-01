/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.dto.skf2100Sc002common;

import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.co.c_nexco.skf.skf2100.domain.dto.common.Skf2100CommonDto;
import lombok.EqualsAndHashCode;

/**
 * モバイルルーター借用希望申請書(アウトソース)画面の共通Dto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2100Sc002CommonDto extends Skf2100CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 社員情報のリスト
	private List<Map<String, String>> shainList;
	// 「一時保存」フラグ
	private boolean status00Flag;
	// モバイルルータ情報表示フラグ
	private boolean routerInfoViewFlag;
	// 承認可否フラグ
	private boolean hdnApprovalFlg;
	// ボタン表示モードレベル
	private int dispMode;
	
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
	// コメント
	private String commentNote;
	// コメントボタン表示フラグ
	private boolean commentViewFlag;
	// コメント入力欄表示フラグ
	private boolean commentInputFlag;
	//　承認者用ボタン表示フラグ
	private boolean allButtonEscape;


	// ボタン表示フラグ
	// 申請条件を確認ボタン
	private boolean btnRequirementVisibled;
	
	/** ドロップダウンリスト */
	// 原籍会社選択リスト
	private List<Map<String, Object>> originalCompanySelectList;
	// 給与支給会社名選択リスト
	private List<Map<String, Object>> payCompanySelectList;

	/** エラー */
	// 原籍会社
	private String originalCompanySelectErr;
	// 給与支給会社名
	private String payCompanySelectErr;
	
	/** ボタン非活性フラグ */
	// 承認ボタン
	private String btnApproveDisabled;
	// モバイルルーター本体受領チェックフラグ
	private String bodyReceiptCheckFlagDisabled;
	// モバイルルーター手引き受領チェックフラグ
	private String handbookReceiptCheckFlagDisabled;
	// 返却資材受領チェックフラグ
	private String materialsReceivedCheckFlagDisabled;
	
	// 日付
	private String yearMonthDay;
	
	/** エラー状態*/
	private String useStartHopeDayErr;
	private String mailAddressErr;
	private String telErr;
	private String receivedDateErr;
	private String shippingDateErr;
	
	// 社員番号
	private String hdnApplShainNo;
	// 申請書類履歴テーブル申請日
	private Date applHistoryDate;
	
	private Long hdnMobileRouterNo;
	private String hdnIccid;
	private String hdnImei;
	private String jsonLabelList;
	
	// 閲覧フラグ
	private boolean viewFlag;
}
