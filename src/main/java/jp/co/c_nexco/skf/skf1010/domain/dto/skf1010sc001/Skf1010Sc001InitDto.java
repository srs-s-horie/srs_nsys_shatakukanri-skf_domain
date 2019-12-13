/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf1010.domain.dto.skf1010sc001;

import java.util.List;
import java.util.Map;

import jp.co.c_nexco.skf.skf1010.domain.dto.skf1010sc001common.Skf1010Sc001CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf1010_Sc001社宅管理TOP画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf1010Sc001InitDto extends Skf1010Sc001CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 会社コード
	private String companyCd;
	// 社員番号
	private String shainNo;
	// ロールID
	private String roleId;
	// 申請書ID
	private String applId;
	// ワークフローレベル1
	private String wfLevel1;
	// ワークフローレベル2
	private String wfLevel2;
	// ユーザ名
	private String userName;
	// 画面表示(申請)
	private String level1;
	// 画面表示(窓口)
	private String level2;
	// 画面表示（窓口 申請書類を承認する）
	private String level2_1;
	// 画面表示（社宅を管理する～社員情報を一括更新する）
	private String level2_2;
	// 画面表示（月締め処理を行う）
	private String level2_3;
	// 画面表示（レンタル備品指示書を作成する、備品搬入・搬出確認リストを作成する）
	private String level2_4;
	// 画面表示（法定調書データを管理する）
	private String level2_5;
	// 画面表示（年齢による使用料の変更通知）
	private String level2_6;
	// 画面表示(管理者）
	private String level3;
	// 画面表示（操作に困ったときは）
	private String level4;
	// 画面表示（マニュアル 管理）
	private String level4_1;
	// 画面表示（マニュアル 社宅管理）
	private String level4_2;
	// 画面表示（未処理情報）
	private String level5;
	// システムに関するお知らせ
	private String note;
	// 入退居区分
	private String nyutaikyoKbn;
	// 入退居申請状況区分
	private String nyutaikyoApplStatusKbn;
	// 未処理情報 上部 「社宅入居希望等調書の申請が無い入居情報があります。」の件数
	private int nyutaikyoCount1;
	// 未処理情報 上部 「退居届の申請が無い退居届があります。」の件数
	private String nyutaikyoCount2;
	// 備品返却の提示が未完了の件数（本社）
	private int bihinHenkyakuUnfinishedTipsHonsha;
	// 備品返却の提示が未完了の件数（東京支社）
	private int bihinHenkyakuUnfinishedTipsTokyo;
	// 備品返却の提示が未完了の件数（八王子支社）
	private int bihinHenkyakuUnfinishedTipsHatiouzi;
	// 備品返却の提示が未完了の件数（名古屋支社）
	private int bihinHenkyakuUnfinishedTipsNagoya;
	// 備品返却の提示が未完了の件数（金沢支社）
	private int bihinHenkyakuUnfinishedTipsKanazawa;
	/**
	 * 未処理情報<br>
	 * 社宅入居希望等調書の申請<br>
	 * 社宅希望者に社宅提示が完了していないデータ<br>
	 * 提示社宅の本人確認が完了していないデータ<br>
	 * 入居希望者の同意済のデータ<br>
	 * 入居希望者の同意されなかったデータ<br>
	 * 退居届の申請<br>
	 */
	private List<Map<String, Object>> nyutaikyoInformationList;
	/**
	 * 未処理情報<br>
	 * 備品希望の申請が無い入居情報<br>
	 * 備品希望の提示が完了していないデータ<br>
	 * 備品希望の搬入が完了していないデータ<br>
	 */
	private List<Map<String, Object>> bihinKiboInformationList;
	/**
	 * 未処理情報<br>
	 * 備品返却提示の本人確認が完了していないデータ<br>
	 * 備品返却提示の同意済のデータ<br>
	 * 備品の搬出が完了していないデータ<br>
	 */
	private List<Map<String, Object>> bihinHenkyakuInformationList;
	// 個人のお知らせリスト
	private List<Map<String, Object>> oshiraseList;
}
