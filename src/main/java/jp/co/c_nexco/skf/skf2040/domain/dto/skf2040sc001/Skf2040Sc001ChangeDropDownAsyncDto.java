/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.dto.skf2040sc001;

import java.util.List;
import java.util.Map;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetShatakuInfoExp;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040Sc001common.Skf2040Sc001CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2040Sc001 退居（自動車の保管場所返還）届画面の社宅ドロップダウンリスト変更時Dto（非同期）
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2040Sc001ChangeDropDownAsyncDto extends Skf2040Sc001CommonAsyncDto {

    /**
     * シリアルナンバー
     */
    private static final long serialVersionUID = 1L;

    // 駐車場2台フラグ
    private boolean parkingFullFlg;

    /**
     * ドロップダウン
     */
    /** ドロップダウン：現居住社宅名リスト */
    private List<Map<String, Object>> ddlNowShatakuNameList;
    private List<Skf2020Sc002GetShatakuInfoExp> shatakuList;

    /**
     * 申請者
     */
    // 社員番号
    private String shainNo;

       /**
     * 現居住社宅
     */
    // 現入居社宅
    private String nowShataku;
    // 保有社宅名
    private String nowShatakuName;
    // 室番号
    private String nowShatakuNo;
    // 規格（間取り）
    private String nowShatakuKikaku;
    // 規格名称（間取り）
    private String nowShatakuKikakuName;
    // 面積
    private String nowShatakuMenseki;
    // 社宅管理ID
    private long shatakuKanriId;
    // 駐車場1台目
    private String parking1stPlace;
    // 駐車場2台目
    private String parking2ndPlace;

    
    /**
     * Hidden
     */
    // 社宅管理ID
    private long hdnShatakuKanriId;
    // 選択された社宅名
    private String hdnSelectedNowShatakuName;

    // 規格(間取り)
    private String hdnShatakuKikakuKbn;

    // 現在の位置番号
    private String hdnParking1stNumber;

    // 現在の位置番号2
    private String hdnParking2ndNumber;

    // 現居住社宅管理番号
    private long hdnNowShatakuKanriNo;
    // 現居住社宅部屋管理番号
    private long hdnNowShatakuRoomKanriNo;
    // 備品返却有無
    private String hdnBihinHenkyakuUmu;

}
