/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc001;

import java.util.*;

import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060Sc001common.Skf2060Sc001CommonAsyncDto;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060Sc001common.Skf2060Sc001CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2060_Sc001画面のSearchAdrressDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2060Sc001CheckAsyncDto extends Skf2060Sc001CommonAsyncDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
	
	//入力・重複チェックフラグ
	private boolean checkFlg;
	//確認ダイアログフラグ
	private boolean dialogFlg;
	
	//確認ダイアログ用社員名
	private String dialogShainName;

}
