/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc005;

import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022Sc005common.Skf3022Sc005CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3022Sc005SendMailDto　Skf3022_Sc005画面のSendMailDto
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3022Sc005SendMailDto extends Skf3022Sc005CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
	
	//メール送信リスト文字列
	private String mailListData;
	//送信対象提示区分
	private String mailTeijiKbn;
	
}
