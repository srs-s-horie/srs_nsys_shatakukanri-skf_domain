/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.dto.skf2060Sc003common;

import java.util.*;

import jp.co.c_nexco.skf.skf2060.domain.dto.common.Skf206010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * TestPrjTop画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2060Sc003CommonDto extends Skf206010CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
	
	//申請状況
	private String applStatus;
    
    //隠し要素
    //申請書類管理番号
    private String applNo;
    
    //借上物件リスト
    private List<Map<String, String>> kariageTeijiList;
    //ラジオボタン用借上物件番号
    private String radioCandidateNo;
	
}
