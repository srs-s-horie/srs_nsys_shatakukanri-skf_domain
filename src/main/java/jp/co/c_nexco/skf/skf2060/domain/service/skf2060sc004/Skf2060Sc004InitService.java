/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc004;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc004.Skf2060Sc004InitDto;
import jp.co.intra_mart.system.repackage.bouncycastle_1_44.org.bouncycastle.asn1.ocsp.Request;

/**
 * TestPrjTop画面のInitサービス処理クラス。　 
 * 
 */
@Service
public class Skf2060Sc004InitService extends BaseServiceAbstract<Skf2060Sc004InitDto> {
	
    @Autowired
    Skf2060Sc004SharedService skf2060Sc004SharedService;
    @Autowired
    private SkfDateFormatUtils skfDateFormatUtils;
    
	/**
	 * サービス処理を行う。　
	 * 
	 * @param initDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public Skf2060Sc004InitDto index(Skf2060Sc004InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF2060_SC004_TITLE);
		
        // リストチェック状態を解除
        initDto.setCompleteChkVal(null);
        initDto.setReminderChkVal(null);

        
        
        
        initDto.setListTableMaxRowCount("50"); //TODO
		return initDto;
	}
	
	private void setInitInfo(Skf2060Sc004InitDto initDto){
	    // 提示日From
	    String currentMonthFirstDate = skfDateFormatUtils.dateFormatFromDate(new Date(), "yyyyMMdd");
	    initDto.setCandidateDateFrom(currentMonthFirstDate);
	    // 提示状況
	    // チェックボックスの初期値セット
        List<String> defaultStatusList = getDefaultCandidateStatusList();
	    initDto.setCandidateStatus(defaultStatusList.toArray(new String[defaultStatusList.size()]));
	    // 
	    // 
	    // 
	    // 
	    // 
	    // 
	    // 
	    // 
	    // 
	    
	}
	
	/**
	 * 借上げ候補物件一覧画面の初期表示時にチェック済とする提示状況をリストで返す
	 * @return
	 */
	private List<String> getDefaultCandidateStatusList(){
	    List<String> initStats = new ArrayList<String>();
	    // ステータス：確認依頼
	    initStats.add(CodeConstant.STATUS_KAKUNIN_IRAI);
	    // ステータス：選択済
	    initStats.add(CodeConstant.STATUS_SENTAKU_ZUMI);
	    // ステータス：選択しない
	    initStats.add(CodeConstant.STATUS_SENTAKU_SHINAI);
	    return initStats;
	}
}
