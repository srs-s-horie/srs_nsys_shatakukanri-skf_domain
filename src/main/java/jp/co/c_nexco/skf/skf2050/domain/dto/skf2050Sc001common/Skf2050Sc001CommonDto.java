/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2050.domain.dto.skf2050Sc001common;

import java.util.List;
import java.util.Map;

import jp.co.c_nexco.skf.skf2050.domain.dto.common.Skf205010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2050Sc001 備品返却確認（申請者用)共通処理Dto
 *
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2050Sc001CommonDto extends Skf205010CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 申請情報
	// 申請書類管理番号
	private String applNo;
	// 申請書類ID
	private String applId;
	// 申請状況
	private String applStatus;
	private String sendApplStatus;

	// 表示フラグ系
	// 立会人項目
	private boolean dairininVisible = true;
	// 備品返却完了日項目
	private boolean completionVisible = true;
	// 同意する系ボタン表示
	private boolean agreeBtnVisible = true;
	// 搬出済み系ボタン表示
	private boolean carryOutVisible = true;
	// 搬出済みボタン個別表示設定
	private boolean carryOutBtnRemove = true;
	// コメント欄表示
	private boolean commentAreaVisible = true;
	// コメント表示ボタン表示
	private boolean commentBtnVisible = true;
	// 全項目非表示
	private boolean allNotVisible = false;

	// 非活性フラグ
	private String sessionTimeDisabled = "false";

	// 画面表示項目
	// 申請状況（テキスト）
	private String applStatusText;
	// 機関（テキスト）
	private String agency;
	// 部等（テキスト）
	private String affiliation1;
	// 室・課等
	private String affiliation2;
	// 勤務地のTEL
	private String tel;
	// 社員番号
	private String shainNo;
	// 氏名
	private String name;
	// 等級
	private String tokyu;
	// 性別
	private String gender;
	// 社宅名
	private String shatakuName;
	// 室番号
	private String shatakuNo;
	// 規格（間取り
	private String shatakuKikaku;
	// 面積
	private String shatakuMenseki;

	// 備品情報
	private List<Map<String, String>> bihinInfoList;

	// 立会代理人氏名
	private String dairininName;
	// 立会代理人連絡先
	private String dairiRenrakuSaki;
	// 備品返却立会日
	private String sessionDay;
	// 備品返却立会時刻
	private String sessionTime;
	// 備品搬出完了日
	private String completionDay;
	// 連絡先
	private String renrakuSaki;

	// 承認者へのコメント
	private String commentNote;

	// 操作ガイド
	private String operationGuide;

	// 備品返却立会時刻ドロップダウンリスト
	private List<Map<String, Object>> ddSessionTimeList;
	
	/** ボタン表示非表示 */
	// 同意するボタン
	private String btnAgreeDisabled;
	
	// 同意しないボタン
	private String btnNotAgreeDisabled;
	
	// 搬出完了ボタン
	private String btnCarryingOutDisabled;
}
