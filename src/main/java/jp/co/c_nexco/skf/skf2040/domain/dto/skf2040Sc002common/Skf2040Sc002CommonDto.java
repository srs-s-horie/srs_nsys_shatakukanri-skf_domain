/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.dto.skf2040Sc002common;

import java.util.Date;
import java.util.List;
import java.util.Map;
import jp.co.c_nexco.skf.skf2040.domain.dto.common.Skf204010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2040_Sc002 退居（自動車の保管場所返還）届(アウトソース用）画面の共通Dto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2040Sc002CommonDto extends Skf204010CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	/*
	 * 退居（自動車の保管場所返還）届
	 */
	// 社宅管理番号
	private String shatakuKanriNo;
	// 氏名
	private String name;
	// 社員番号
	private String shainNo;
	// 申請書ID
	private String applId;
	// 申請書類管理番号
	private String applNo;
	// ステータス
	private String applStatus;
	// ステータス（表示用）
	private String applStatusText;
	// 現所属：機関
	private String nowAgency;
	// 現所属：部等
	private String nowAffiliation1;
	// 現所属：室、チーム又は課
	private String nowAffiliation2;

	// 申請日
	private String applDate;
	// 現住所
	private String address;
	// 退居場所
	private String taikyoArea;
	// 駐車場１
	private String parkingAddress1;
	// 駐車場2
	private String parkingAddress2;
	// 退居日 社宅等
	private String taikyoDate;
	// 退居日 駐車場
	private String parkingHenkanDate;
	// 退居（返還）理由（区分）
	private String taikyoRiyu;
	// 退居後の連絡先
	private String taikyogoRenrakusaki;
	// 社宅退居区分
	private String shatakuTaikyoKbn;
	// 駐車場返還区分
	private String shatakuTaikyoKbn2;

	// 提示番号
	private long teijiNo;

	// アコーディオン初期表示
	private String levelOpen;

	// 申請書類履歴テーブル申請日
	private Date applHistoryDate;

	// メール区分
	private String mailKbn;

	/*
	 * 添付資料
	 */
	// 添付資料情報
	private List<Map<String, Object>> attachedFileList; // 添付資料
	// 添付資料番号
	private String attachedNo;
	// 添付資料種別
	private String attachedType;
	// 添付書類有無
	private String applTacFlg;

	/*
	 * 返却備品設定
	 */
	// 備品情報
	Map<String, Object> bihinMap;

	// コメント
	private String commentNote;

	/*
	 * リスト
	 */
	// 備品の返却リスト
	List<Map<String, Object>> henkyakuList;

	/*
	 * 活性制御類
	 */

	// 承認ボタン
	private String btnApproveDisabled;
	// 提示ボタン
	private String btnPresentDisabeld;

	/*
	 * フラグ類
	 */
	// 添付資料欄表示フラグ
	private String tenpViewFlg;
	// 退居（自動車の保管場所変換）届表示フラグ
	private String taikyoViewFlg;
	// 社宅状態表示フラグ
	private String shatakuJotaiViewFlg;
	// 退居（自動車の保管場所変換）届PDF出力ボタン表示フラグ
	private String taikyoPdfViewFlg;

	// 資料を添付ボタン表示フラグ
	private boolean shiryoBtnViewFlg;
	// コメントボタン表示フラグ
	private String commentViewFlg;

	// 返却情報表示フラグ true:あり false:なし
	private String henkyakuInfoViewFlg;
	// 返却備品なしフラグ true:あり false:なし
	private String henkyakuBihinNothing;

	// 退居日日付変更フラグ
	private String taikyoDateFlg;
	// 駐車場日付変更フラグ
	private String parkingEDateFlg;

	/*
	 * hidden
	 */
	// 備品返却申請の書類管理番号
	private String hdnBihinHenkyakuApplNo;
	// 社員番号
	private String hdnApplShainNo;

}
