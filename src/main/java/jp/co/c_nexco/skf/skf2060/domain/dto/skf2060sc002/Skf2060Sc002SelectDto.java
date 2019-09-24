/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc002;

import java.util.*;

import jp.co.c_nexco.nfw.webcore.domain.model.FileDownloadDto;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060Sc001common.Skf2060Sc001CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2060_Sc001画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2060Sc002SelectDto extends FileDownloadDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
	
    //理由ドロップダウン選択された要素
    private String riyuDropdown;
    //備考
    private String biko;;
    
    //隠し要素
    //申請書類管理番号
    private String applNo;
    //提示回数
    private String teijiKaisu;
    
    //借上物件リスト
    private List<Map<String, String>> kariageTeijiList;
    //ラジオボタン用借上物件番号
    private String radioCandidateNo;
    
	
	
}
