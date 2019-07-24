/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc004;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc004.Skf2060Sc004BulkCompleteDto;

/**
 * TestPrjTop画面のSkf2060Sc004BulkCompleteDto（一括完了）サービス処理クラス。　 
 * 
 */
@Service
public class Skf2060Sc004BulkCompleteService extends BaseServiceAbstract<Skf2060Sc004BulkCompleteDto> {
	
    @Autowired
    Skf2060Sc004SharedService skf2060Sc004SharedService;

	/**
	 * サービス処理を行う。　
	 * 
	 * @param bulkCompDto DTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2060Sc004BulkCompleteDto index(Skf2060Sc004BulkCompleteDto bulkCompDto) throws Exception {
		
		bulkCompDto.setPageTitleKey(MessageIdConstant.SKF2060_SC004_TITLE);
		
        // リストチェック状態を解除
        bulkCompDto.setCompleteChkVal(null);
        bulkCompDto.setReminderChkVal(null);

        bulkCompDto.setListTableMaxRowCount("50"); //TODO
		return bulkCompDto;
	}
	
}
