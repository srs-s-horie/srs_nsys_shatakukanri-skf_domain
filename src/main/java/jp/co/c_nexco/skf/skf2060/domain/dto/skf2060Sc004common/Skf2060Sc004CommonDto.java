/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.dto.skf2060Sc004common;

import java.util.List;
import java.util.Map;

import jp.co.c_nexco.skf.skf2060.domain.dto.common.Skf206010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * SKF2060Sc004 借上候補物件状況一覧画面の共通Dto。
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2060Sc004CommonDto extends Skf206010CommonDto {
    
    private static final long serialVersionUID = -1902278406295003652L;
    /** 楽観的排他制御に使用するテーブルプレフィクス */
    public final String UPDATE_TABLE_PREFIX_APPL_HIST = "SKF2010_APPL_HISTORY_";
    
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
    // 提示対象社員番号
    private String candidatePersonNo;
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
