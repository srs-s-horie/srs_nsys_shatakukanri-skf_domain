/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2050.domain.dto.skf2050Sc002common;

import java.util.*;
import jp.co.c_nexco.skf.skf2050.domain.dto.common.Skf205010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2050Sc002 備品返却確認（アウトソース用)共通処理Dto
 *
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2050Sc002CommonDto extends Skf205010CommonDto {

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
	// 全ボタン非表示フラグ
	private boolean isAllButtonEscape = false;
	// 返却立会日、搬出完了日共に非表示フラグ
	private boolean isAllDateAreaEscape = false;
	// 搬出完了かどうか
	private boolean isCarryOut;
	// コメントボタン非表示
	private boolean commentBtnVisibled = false;
	// コメント欄非表示
	private boolean commentAreaVisibled = true;

	// 画面表示項目
	// 申請状況（テキスト）
	private String applStatusText;
	// 機関（テキスト）
	private String agency;
	// 部等（テキスト）
	private String affiliation1;
	// 室、チーム又は課
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
	// 備品返却立会日時（テキスト）
	private String sessionDate;
	// 備品搬出完了日
	private String completionDay;
	// 連絡先
	private String renrakuSaki;

	// コメント
	private String commentNote;
	// コメント登録者名
	private String commentName;
	// コメント登録日付
	private String commentAddDate;

	// 操作ガイド
	private String operationGuide;

}
