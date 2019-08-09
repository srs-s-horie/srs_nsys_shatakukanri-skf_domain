/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010Sc008common;

import java.util.Map;
import jp.co.c_nexco.skf.skf2010.domain.dto.common.Skf201050CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2010_Sc008 代行ログイン画面の共通Dto。
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010Sc008CommonDto extends Skf201050CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 社員番号
	private String shainNo;
	// 社員氏名
	private String shainName;
	// 機関
	private String agency;
	// 部等
	private String affiliation1;
	// 室、チーム又は課
	private String affiliation2;
	// 機関名
	private String agencyName;
	// 部等名
	private String affiliation1Name;
	// 室、チーム又は課名
	private String affiliation2Name;
}
