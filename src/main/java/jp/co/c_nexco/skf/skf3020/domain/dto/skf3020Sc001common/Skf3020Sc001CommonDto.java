/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.dto.skf3020Sc001common;

import jp.co.c_nexco.skf.skf3020.domain.dto.common.Skf302010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * 現社宅照会（ポップアップ）画面のCommonDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3020Sc001CommonDto extends Skf302010CommonDto {
	
	private static final long serialVersionUID = 9177033425617626048L;

	// 社宅入居状況（1つ目）
	// 社員番号
	private String shainNo;
	// 社員氏名
	private String shainName;
	// 居住者区分
	private String jyukyoKbn;
	// 社宅区分
	private String shatakuKbn;
	// 社宅名
	private String shatakuName;
	// 住所
	private String address;
	// 部屋番号
	private String roomNo;
	// 用途
	private String ause;
	// 規格
	private String kikaku;
	// 面積
	private String menseki;

	// 社宅入居状況（2つ目）
	// 表示フラグ
	private boolean remove;
	// 社員番号
	private String shainNo2;
	// 社員氏名
	private String shainName2;
	// 居住者区分
	private String jyukyoKbn2;
	// 社宅区分
	private String shatakuKbn2;
	// 社宅名
	private String shatakuName2;
	// 住所
	private String address2;
	// 部屋番号
	private String roomNo2;
	// 用途
	private String ause2;
	// 規格
	private String kikaku2;
	// 面積
	private String menseki2;
	
	// クリック行のデータ
	// - 社員番号
	private String hdnRowShainNo;
	
}
