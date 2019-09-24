/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.service.skf2040sc001;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationGuideUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2020.domain.dto.skf2020sc002.Skf2020Sc002SaveDto;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040sc001.Skf2040Sc001SaveDto;

/**
 * Skf2040Sc001 退居（自動車の保管場所返還）届画面の一時保存処理。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2040Sc001SaveService extends BaseServiceAbstract<Skf2040Sc001SaveDto> {
	
    @Autowired
    private SkfOperationLogUtils skfOperationLogUtils;
    @Autowired
    private SkfOperationGuideUtils skfOperationGuideUtils;
    
    @Autowired
    private Skf2040Sc001SharedService skf2040Sc001SharedService;
	/**
	 * サービス処理を行う。　
	 * 
	 * @param saveDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2040Sc001SaveDto index(Skf2040Sc001SaveDto saveDto) throws Exception {
		// 操作ログを出力する
        skfOperationLogUtils.setAccessLog("一時保存", CodeConstant.C001, saveDto.getPageId());
        
        // 申請書情報の取得
        Map<String, String> applInfoMap = new HashMap<String, String>();
        applInfoMap = skf2040Sc001SharedService.getSkfApplInfoMap(saveDto);
        
        // バイトカット処理
        skf2040Sc001SharedService.cutByte(saveDto);
        
        // 一時保存処理を行う
        //if (!skf2040Sc001SharedService.saveNewData(saveDto, applInfoMap)) {
        //    return saveDto;
        //}
        
		// 操作ガイド取得
        LogUtils.debugByMsg("操作ガイド" + saveDto.getPageId());
        saveDto.setOperationGuide(skfOperationGuideUtils.getOperationGuide(saveDto.getPageId()));

        // 正常終了
        if (CodeConstant.STATUS_MISAKUSEI.equals(applInfoMap.get(""))) {
            ServiceHelper.addResultMessage(saveDto, MessageIdConstant.I_SKF_1012);
        } else {
            ServiceHelper.addResultMessage(saveDto, MessageIdConstant.I_SKF_1011);
        }
		return saveDto;
	}
	
	private boolean execSave(Skf2040Sc001SaveDto saveDto, Map<String, String> applInfoMap){
	    boolean isExecSave = false;
	    
	    if (CodeConstant.STATUS_MISAKUSEI.equals(applInfoMap.get("status"))) {
	        // TODO 備品返却情報も作成する必要がある？
	        
	        // 新規保存の場合
	        return skf2040Sc001SharedService.saveNewData(saveDto, applInfoMap);
	        
	        
	        
	    } else {
	        // 更新の場合
	        
	        // 申請履歴テーブルの更新
	        
	        // 退居届テーブルの更新
	        
	        
	        
	        return true;
	    }
	    
	}
}
