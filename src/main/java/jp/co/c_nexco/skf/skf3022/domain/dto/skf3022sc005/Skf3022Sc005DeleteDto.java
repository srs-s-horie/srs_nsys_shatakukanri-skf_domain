/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc005;

import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022Sc005common.Skf3022Sc005CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3022Sc005DeleteDto　Skf3022_Sc005画面のDeleteDto。
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3022Sc005DeleteDto extends Skf3022Sc005CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
	
	//削除時パラメータ
	private String delTeijiNo;
	private String delShainNo;
	private String delNyutaikyoKbn;
    private String delUpdateDate;
    private String delUpdateDateNtk;
    private String delUpdateDateShataku;
    private String delUpdateDateParkOne;
    private String delUpdateDateParkTwo;
    private String delShatakuNo;
    private String delRoomNo;
    private String delParkOne;
    private String delParkTwo;
	
}
