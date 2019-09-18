/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.service.skf2040sc001;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.DateUtils;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;

/**
 * Skf2040Sc001 退居（自動車の保管場所返還）届の共通サービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2040Sc001SharedService{
    @Autowired
    private SkfDropDownUtils skfDropDownUtils;
    @Autowired
    private SkfGenericCodeUtils skfGenericCodeUtils;
    @Autowired
    private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
    
    /**
     * 退居（返還）理由ドロップダウンリスト取得
     * @param defaultSelectVal 初期選択値 ※汎用コードSKF1142のコード値を指定すること
     * @return List<Map<String, Object>> 退居（返還）理由ドロップダウンリスト
     */
    public List<Map<String, Object>> getTaikyoHenkanRiyuList(String defaultSelectCdVal){
        // 退居（返還）理由プルダウン取得
        // TODO 定数化
        return skfDropDownUtils.getGenericForDoropDownList("SKF1142", defaultSelectCdVal, true);
    }
    
    /**
     * 現住居社宅ドロップダウンリスト取得
     * @param shainNo 対象社員番号
     * @param defaultSelectShatakuId 初期選択値 社宅管理IDを指定する事
     * @return List<Map<String, Object>> 現住居社宅ドロップダウンリスト
     */
    public List<Map<String, Object>> getNowShatakuNameList(String shainNo, long defaultSelectShatakuId){
        //システム日付
        String sysDateYyyyMMdd = DateUtils.getSysDateString(SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT);
        return skfDropDownUtils.getDdlNowShatakuNameByCd(shainNo, sysDateYyyyMMdd, defaultSelectShatakuId, false);
    }
    
    
}
