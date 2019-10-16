/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.dto.skf2040Sc001common;

import jp.co.c_nexco.skf.skf2040.domain.dto.common.Skf204010CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2040Sc001 退居（自動車の保管場所返還）届画面の共通非同期Dto
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2040Sc001CommonAsyncDto extends Skf204010CommonAsyncDto {
    
    private static final long serialVersionUID = -1902278406295003652L;
    
    /**
     * フォーム情報
     */
    // 現入居社宅
    private String nowShataku;
    // 保有社宅名
    private String nowShatakuName;

    // 社員番号
    private String shainNo;

}
