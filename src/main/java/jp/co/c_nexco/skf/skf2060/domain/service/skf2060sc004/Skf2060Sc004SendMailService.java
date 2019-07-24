/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc004;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc004.Skf2060Sc004InitDto;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc004.Skf2060Sc004SendMailDto;
import jp.co.intra_mart.system.repackage.bouncycastle_1_44.org.bouncycastle.asn1.ocsp.Request;

/**
 * TestPrjTop画面のCandidete（提示）サービス処理クラス。　 
 * 
 */
@Service
public class Skf2060Sc004SendMailService extends BaseServiceAbstract<Skf2060Sc004SendMailDto> {
	
    @Autowired
    Skf2060Sc004SharedService skf2060Sc004SharedService;

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
		
        // リストチェック状態を解除
        sendMailDto.setCompleteChkVal(null);
        sendMailDto.setReminderChkVal(null);

        sendMailDto.setListTableMaxRowCount("50"); //TODO
		return sendMailDto;
	}
	
}
