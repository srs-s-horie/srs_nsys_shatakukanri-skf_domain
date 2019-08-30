/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.dto.skf2040sc002;

import java.util.Date;
import java.util.List;
import java.util.Map;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040Sc002common.Skf2040Sc002CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2040_Sc002 退居（自動車の保管場所返還）届(あ画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2040Sc002InitDto extends Skf2040Sc002CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	/*
	 * 退居（自動車の保管場所返還）届
	 */

	// 社宅管理番号
	private String shatakuKanriNo;

	// 社員番号
	private String shainNo;
	// 申請書ID
	private String applId;
	// 申請書類管理番号
	private String applNo;
	// ステータス（表示用）
	private String applStatusText;

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
	// 洗濯機
	private String bihinState11;
	private String bihinReturn11;
	// 冷蔵庫
	private String bihinState12;
	private String bihinReturn12;
	// オーブンレンジ
	private String bihinState13;
	private String bihinReturn13;
	// 掃除機
	private String bihinState14;
	private String bihinReturn14;
	// 電子炊飯ジャー
	private String bihinState15;
	private String bihinReturn15;
	// テレビ
	private String bihinState16;
	private String bihinReturn16;
	// テレビ台
	private String bihinState17;
	private String bihinReturn17;
	// 座卓（こたつ）
	private String bihinState18;
	private String bihinReturn18;
	// キッチンキャビネット
	private String bihinState19;
	private String bihinReturn19;

	// 社宅の状態
	private String shatakuStatus;

	// コメント
	private String commentNote;

	/*
	 * hidden
	 */
	// 社員番号
	private String hdnApplShainNo;
	// 更新日
	private Date hdnApplUpdateDate;

	/*
	 * 表示フラグ類
	 */
	// 添付資料表示フラグ
	private String tenpViewFlag;
	// 備品返却欄表示フラグ
	private String bihinVisible;
	// 退居（自動車の保管場所変換）届PDF出力ボタン表示フラグ
	private String taikyoPdfViewFlg;
	// コメントボタン表示フラグ
	private String commentViewFlag;

	// 提示ボタン表示フラグ
	private String presenBtnViewFlg;
	// 修正依頼ボタン表示フラグ
	private String revisionBtnViewFlg;
	// 差戻しボタン表示フラグ
	private String remandBtnViewFlg;
}
