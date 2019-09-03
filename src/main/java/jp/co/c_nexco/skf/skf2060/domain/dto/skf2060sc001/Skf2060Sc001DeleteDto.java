/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc001;

import java.util.*;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060Sc001common.Skf2060Sc001CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2060_Sc001画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2060Sc001DeleteDto extends Skf2060Sc001CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;

	/** 借上候補物件登録画面hidden項目連携用 */
	// 会社コード
	private String hdnCompanyCd;
	// 借上候補物件番号
	private String hdnCandidateNo;
}
