/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3040.domain.dto.skf3040Sc001common;

import jp.co.c_nexco.skf.skf3040.domain.dto.common.Skf304010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3040Sc001CommonDto レンタル備品指示書出力画面の共通Dto。
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3040Sc001CommonDto extends Skf304010CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
	
	// 希望日（From）
	private String desiredTermFrom;
	private String desiredTermFromErr;
	
	// 希望日（To）
	private String desiredTermTo;
	private String desiredTermToErr;
	
	// 再発行
	private String reIssuance;
	
}
