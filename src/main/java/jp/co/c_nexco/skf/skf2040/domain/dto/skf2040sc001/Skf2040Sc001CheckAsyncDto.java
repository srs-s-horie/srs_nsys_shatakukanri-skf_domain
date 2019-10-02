/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.dto.skf2040sc001;

import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040Sc001common.Skf2040Sc001CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2040Sc001 退居（自動車の保管場所返還）届画面の共通非同期Dto
 * 
 * @author NEXCOシステムズ
 */

@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2040Sc001CheckAsyncDto extends Skf2040Sc001CommonAsyncDto {

    private static final long serialVersionUID = -1902278406295003652L;

    /**
     * 駐車場情報
     */
    private String parking1stPlace;
    private String parking2ndPlace;
    
    /**
     * フォーム情報
     */
    // 退居(返還)日
    private String taikyoHenkanDate;
    // 退居(返還)する社宅又は自動車の保管場所
    private String[] taikyoType;
    private String taikyoTypeShataku;
    private String taikyoTypeParking1;
    private String taikyoTypeParking2;
    // 退居理由区分
    private String taikyoRiyuKbn;
    // 退居(返還)理由
    private String taikyoHenkanRiyu;
    // 社宅の状態 
    private String shatakuJyotai;
    // 退居後の連絡先
    private String taikyogoRenrakuSaki;

    // 返却備品
    private String returnEquipment;
    // 返却立会希望日
    private String sessionDay;
    // 返却立会希望時間
    private String sessionTime;
    // 連絡先
    private String renrakuSaki;
}
