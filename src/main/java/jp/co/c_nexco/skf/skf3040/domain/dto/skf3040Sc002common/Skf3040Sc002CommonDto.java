/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3040.domain.dto.skf3040Sc002common;

import java.util.*;

import jp.co.c_nexco.skf.skf3040.domain.dto.common.Skf304020CommonDto;
import lombok.EqualsAndHashCode;

/**
 * 
 * 備品搬入・搬出確認リスト出力画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3040Sc002CommonDto extends Skf304020CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;

	// 搬入・搬出日（From）
	private String carryingInOutTermFrom;
	private String carryingInOutTermFromErr;
	
	// 搬入・搬出日（To）
	private String carryingInOutTermTo;
	private String carryingInOutTermToErr;
	
	// 出力状況
	private String outSituation;

}
