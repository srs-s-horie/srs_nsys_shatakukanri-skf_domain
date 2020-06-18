/*

 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc004;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.emory.mathcs.backport.java.util.Arrays;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc004.Skf2060Sc004SendMailDto;
import jp.co.c_nexco.skf.skf2060.domain.service.common.Skf206010CommonSendMailService;

/**
 * SKF2060Sc004 借上候補物件状況一覧画面の督促メール送信サービス処理クラス。　 
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2060Sc004SendMailService extends SkfServiceAbstract<Skf2060Sc004SendMailDto> {
    
    @Autowired
    Skf206010CommonSendMailService skf206010CommonSharedService;
    @Autowired
    Skf2060Sc004SharedService skf2060Sc004SharedService;
    @Autowired
    private SkfOperationLogUtils skfOperationLogUtils;
    
    /**
     * サービス処理を行う。　
     * 
     * @param sendMailDto DTO
     * @return 処理結果
     * @throws Exception 例外
     */
    @Override
    public Skf2060Sc004SendMailDto index(Skf2060Sc004SendMailDto sendMailDto) throws Exception {
        
        sendMailDto.setPageTitleKey(MessageIdConstant.SKF2060_SC004_TITLE);
        // 操作ログを出力する
        skfOperationLogUtils.setAccessLog("督促メール送信", CodeConstant.C001, FunctionIdConstant.SKF2060_SC004);
        // 完了チェックボックスのチェック状態を取得
        @SuppressWarnings("unchecked")
        List<String> reminderChkValList = Arrays.asList(sendMailDto.getReminderChkVal());
        
        if (reminderChkValList == null || reminderChkValList.size() <= 0) {
            // チェックされた完了チェックボックスが存在しない場合は処理終了
            return sendMailDto;
        }
        
        for (String applNo : reminderChkValList) {
            // 督促メール送信
            skf206010CommonSharedService.sendKariageTeijiMail(applNo);
        }
        
        return sendMailDto;
    }
}
