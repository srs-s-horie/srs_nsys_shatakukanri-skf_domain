/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc004;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc004.Skf2060Sc004RecandidateDto;

/**
 * TestPrjTop画面のCandidete（提示）サービス処理クラス。　 
 * 
 */
@Service
public class Skf2060Sc004RecandidateService extends BaseServiceAbstract<Skf2060Sc004RecandidateDto> {
	
    @Autowired
    Skf2060Sc004SharedService skf2060Sc004SharedService;
    @Autowired
    private SkfOperationLogUtils skfOperationLogUtils;

	/**
	 * サービス処理を行う。　
	 * 
	 * @param recandidateDto DTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2060Sc004RecandidateDto index(Skf2060Sc004RecandidateDto recandidateDto) throws Exception {
		// 操作ログを出力する
        skfOperationLogUtils.setAccessLog("再提示", CodeConstant.C001, recandidateDto.getPageId());
        
        //借上社宅情報登録画面へ遷移させる
        TransferPageInfo nextPage = TransferPageInfo.nextPage("Skf2060Sc001", "recandidate");
        recandidateDto.setTransferPageInfo(nextPage);
		return recandidateDto;
	}
	
}
