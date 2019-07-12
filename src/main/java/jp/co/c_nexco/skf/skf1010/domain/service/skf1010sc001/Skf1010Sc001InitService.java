/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf1010.domain.service.skf1010sc001;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.skf1010.domain.dto.skf1010sc001.Skf1010Sc001InitDto;

/**
 * TestPrjTop画面のInitサービス処理クラス。　 
 * 
 */
@Service
public class Skf1010Sc001InitService extends BaseServiceAbstract<Skf1010Sc001InitDto> {
	
    @Autowired
    private MenuScopeSessionBean sessionBean;

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
	public Skf1010Sc001InitDto index(Skf1010Sc001InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF1010_SC001_TITLE);

        //セッションから代行ログイン状態を取得
        String sessionVal = (String) sessionBean.get(SessionCacheKeyConstant.ALTER_LOGIN_SESSION_KEY);
        initDto.setAlterLoginFlg(sessionVal);
		return initDto;
	}
	
}
