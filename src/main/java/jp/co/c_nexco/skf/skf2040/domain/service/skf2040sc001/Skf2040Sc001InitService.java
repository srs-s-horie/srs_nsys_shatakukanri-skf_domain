/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.service.skf2040sc001;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfCommentUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationGuideUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040sc001.Skf2040Sc001InitDto;
import jp.co.c_nexco.skf.skf2040.domain.service.skf2040sc002.Skf2040Sc002SharedService;

/**
 * Skf2040Sc001 退居（自動車の保管場所返還）届画面のInit処理。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2040Sc001InitService extends BaseServiceAbstract<Skf2040Sc001InitDto> {
	
    @Autowired
    private SkfOperationLogUtils skfOperationLogUtils;
    @Autowired
    private SkfOperationGuideUtils skfOperationGuideUtils;
    @Autowired
    private SkfCommentUtils skfCommentUtils;
    @Autowired
    private Skf2040Sc001SharedService skf2040Sc001SharedService;
	/**
	 * サービス処理を行う。　
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2040Sc001InitDto index(Skf2040Sc001InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF2040_SC001_TITLE);
 		
		// 操作ログを出力する
        skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, initDto.getPageId());
        
        // 初期表示情報を取得
        this.setInitInfo(initDto);
        
        
		// 操作ガイド取得
        initDto.setOperationGuide(skfOperationGuideUtils.getOperationGuide(initDto.getPageId()));

        // 画面ID保持
        initDto.setPrePageId(FunctionIdConstant.SKF2060_SC004);
		return initDto;
	}
	
	/**
	 * 初期表示時の画面情報をDtoに設定する
     * @param initDto
	 */
	private void setInitInfo(Skf2040Sc001InitDto initDto){
	    
	    // 退居(返還)理由プルダウンリストをDtoに設定
	    //initDto.setDdlTaikyoRiyuKbnList(skf2040Sc001SharedService.getTaikyoHenkanRiyuList(defaultSelectCdVal));
	    
	    // 現居住社宅名プルダウンリストをDtoに設定
//	    List<Map<String, Object>> nowShatakuNameList = 
//	            skf2040Sc001SharedService.getNowShatakuNameList(shainNo, defaultSelectShatakuId);
//	    if (null!=nowShatakuNameList){
//	        initDto.setDdlNowShatakuNameList(nowShatakuNameList);
//	    } else {
	        // データが取得できなかった場合、エラーメッセージを表示して初期表示処理を終了
//	    }
	    
	    
	    
	    
	    
	    
	    // セッションから申請書類管理番号を取得
	        // 取得できた場合、退居届テーブルから情報を取得
	        
	        // セッション値での退居届情報の取得に失敗した場合はエラーメッセージを表示してボタンを使用不可にする
	        
	        // 要返却備品情報を取得（getShatakuBihinInfo
	    
	        // 取得できなかった場合、セッション情報からユーザ情報を取得
	    
	    
	    
	    
	    
	}
}
