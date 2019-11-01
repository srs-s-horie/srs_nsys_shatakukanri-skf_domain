/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3070.domain.dto.skf3070Sc001common;

import java.util.List;
import java.util.Map;
import jp.co.c_nexco.skf.skf3070.domain.dto.common.Skf307010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3070Sc001 法定調書データ管理画面の共通Dto。
 * 
 * @author NEXCOシステムズ
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3070Sc001CommonDto extends Skf307010CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 検索結果表示用
	private List<Map<String, Object>> listTableData;

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
	// 個人番号受領状態
	private String acceptStatus;

}
