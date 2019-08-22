/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc004;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc004.Skf2060Sc004ConfirmDto;

/**
 * TestPrjTop画面のConfirmサービス処理クラス。　 
 * 
 */
@Service
public class Skf2060Sc004ConfirmService extends BaseServiceAbstract<Skf2060Sc004ConfirmDto> {
	
    @Autowired
    Skf2060Sc004SharedService skf2060Sc004SharedService;
    @Autowired
    private SkfOperationLogUtils skfOperationLogUtils;
	/**
	 * サービス処理を行う。　
	 * 
	 * @param confirmDto DTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2060Sc004ConfirmDto index(Skf2060Sc004ConfirmDto confirmDto) throws Exception {
		
		confirmDto.setPageTitleKey(MessageIdConstant.SKF2060_SC004_TITLE);
		// 操作ログを出力する
        skfOperationLogUtils.setAccessLog("確認", CodeConstant.C001, confirmDto.getPageId());
        // リストチェック状態を解除
        confirmDto.setCompleteChkVal(null);
        confirmDto.setReminderChkVal(null);

        confirmDto.setListTableMaxRowCount("50"); //TODO
		return confirmDto;
	}
	
}
