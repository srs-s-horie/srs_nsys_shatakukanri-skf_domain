/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.service.skf2040sc001;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationGuideUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040sc001.Skf2040Sc001ConfirmDto;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040sc001.Skf2040Sc001InitDto;

/**
 * Skf2040Sc001 退居（自動車の保管場所返還）届画面の一時保存処理。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2040Sc001ConfirmService extends BaseServiceAbstract<Skf2040Sc001ConfirmDto> {
	
    @Autowired
    private SkfOperationLogUtils skfOperationLogUtils;
    @Autowired
    private SkfOperationGuideUtils skfOperationGuideUtils;
	/**
	 * サービス処理を行う。　
	 * 
	 * @param confirmDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2040Sc001ConfirmDto index(Skf2040Sc001ConfirmDto confirmDto) throws Exception {
		confirmDto.setPageTitleKey(MessageIdConstant.SKF2040_SC001_TITLE);
 		
		// 操作ログを出力する
        skfOperationLogUtils.setAccessLog("確認", CodeConstant.C001, confirmDto.getPageId());
        
        // 一時保存処理を行う
        //this.setDispInfo(initDto);
        
		// 操作ガイド取得
        LogUtils.debugByMsg("操作ガイド" + confirmDto.getPageId());
        confirmDto.setOperationGuide(skfOperationGuideUtils.getOperationGuide(confirmDto.getPageId()));

        // 画面ID保持
        confirmDto.setPrePageId(FunctionIdConstant.SKF2060_SC004);
		return confirmDto;
	}
	
}
