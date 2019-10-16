/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.dto.skf3090Sc008common;

import java.util.*;

import jp.co.c_nexco.skf.skf3090.domain.dto.common.Skf309050CommonDto;
import lombok.EqualsAndHashCode;

/**
 * TestPrjTop画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3090Sc008CommonDto extends Skf309050CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
	
	/** 楽観的排他制御に使用するテーブルプレフィクス */
	public final String informationLastUpdateDate = "Skf3090Sc008informationLastUpdateDate";
	
	//お知らせ内容
	private String note;
	//公開開始日カレンダー
	private String openDateBox;
	
	/**
	 * listTable用
	 */
	//リストテーブル
    private List<Map<String, Object>> listTableData;
    //最大表示行数
    private String listTableMaxRowCount;
	
	/**
	 * 隠し項目
	 */
    // 公開開始日
    private String hdnOpenDate;
    // 内容
    private String hdnNote;
	
}
