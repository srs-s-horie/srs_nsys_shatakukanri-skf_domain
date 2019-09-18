/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.service.skf2040sc001;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
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
    
    
    public List<Map<String, Object>> getTaikyoHenkanRiyuList(String defaultSelectVal){
        // 退居（返還）理由プルダウン取得
        skfDropDownUtils.getGenericForDoropDownList("SKF1142", defaultSelectVal, true);
        
    }
}
