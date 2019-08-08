/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.dto.skf2060Sc004common;

import java.util.List;
import java.util.Map;

import jp.co.c_nexco.skf.skf2060.domain.dto.common.Skf206010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * TestPrjTop画面の共通Dto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2060Sc004CommonDto extends Skf206010CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
	
	// リストテーブルデータ
    private List<Map<String, Object>> listTableData;
    // チェックされた完了チェックボックス値の配列
    private String[] completeChkVal;
    // チェックされた督促チェックボックス値の配列
    private String[] reminderChkVal;
    // リストテーブルの１ページ最大表示行数
    private String listTableMaxRowCount;

    /**
     * 検索フォーム用
     */
    // 提示日From
    private String candidateDateFrom;
    // 提示日To
    private String candidateDateTo;
    // 提示対象者名
    private String candidatePersonName;
    // 借上社宅名
    private String shatakuName;
    // 社宅所在地
    private String shatakuAddressName;
    // 提示状況
    private String[] candidateStatus;

    /**
     * 操作ガイド
     */
    // 操作ガイド
    private String operationGuide;
}
