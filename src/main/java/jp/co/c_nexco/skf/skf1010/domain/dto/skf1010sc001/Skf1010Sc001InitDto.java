/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf1010.domain.dto.skf1010sc001;

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
	// 画面表示（操作に困ったときは 管理者）
	private String level4_1;
	// 画面表示（未処理情報）
	private String level5;
	// システムに関するお知らせ
	private String note;

}
