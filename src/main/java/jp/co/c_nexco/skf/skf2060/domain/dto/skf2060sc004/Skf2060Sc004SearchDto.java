/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc004;

import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060Sc004common.Skf2060Sc004CommonDto;
import lombok.EqualsAndHashCode;

/**
 * SKF2060Sc004 借上候補物件状況一覧画面の検索Dto。
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2060Sc004SearchDto extends Skf2060Sc004CommonDto {
    
    private static final long serialVersionUID = -1902278406295003652L;
    
    // エラー項目
    private String candidateDateFromErr;
    private String candidateDateToErr;
    private String candidatePersonNameErr;
    private String shatakuNameErr;
    private String shatakuAddressNameErr;
    private String candidateStatusErr;
}
