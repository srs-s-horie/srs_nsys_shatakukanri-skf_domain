/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.dto.skf2060Sc002common;

import jp.co.c_nexco.skf.skf2060.domain.dto.common.Skf206010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * TestPrjTop画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2060Sc002CommonDto extends Skf206010CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;

	/** 楽観的排他制御に使用するテーブルプレフィクス */
	public final String applHistoryLastUpdateDate = "Skf2060Sc002applHistoryLastUpdateDate";
	public final String KariageBukkenLastUpdateDate = "Skf2060Sc002kariageBukkenLastUpdateDate";
	public final String KariageTeijiLastUpdateDate = "Skf2060Sc002kariageTeijiLastUpdateDate";
	public final String KariageTeijiDetailLastUpdateDate = "Skf2060Sc002kariageTeijiDetailLastUpdateDate";
	
    //理由ドロップダウン選択された要素
    private String riyuDropdown;
    //備考
    private String biko;
    
    //隠し要素
    //申請書類管理番号
    private String applNo;
    //提示回数
    private String teijiKaisu;
    
    //ラジオボタン用借上物件番号
    private String radioCandidateNo;
}
