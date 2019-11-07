/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3070.domain.dto.common;

import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3070 法定調書データ関連画面の共通Dto。
 * 
 * @author NEXCOシステムズ
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf307010CommonDto extends BaseDto {

	private static final long serialVersionUID = 1L;

	// 賃貸人（代理人） ID
	private String ownerNo;
	// 対象年 開始日
	private String recodeDadefrom;
	// 対象年 終了日
	private String recodeDadeto;

	// 賃貸人（代理人） 氏名又は名称
	private String ownerName;
	// 賃貸人（代理人） 氏名又は名称（フリガナ）
	private String ownerNameKk;
	// 住所(居所）又は所在地
	private String address;
	// 個人法人区分
	private String businessKbn;
	// 社宅名
	private String shatakuName;
	// 社宅住所
	private String shatakuAddress;
	// 対象年
	private String targetYear;
	// 個人番号受領フラグ(個人番号督促状況）
	private String acceptFlg;
	// 督促状況（コメント）
	private String acceptStatus;

}
