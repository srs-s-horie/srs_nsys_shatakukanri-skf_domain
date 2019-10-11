/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc006;

import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc006.Skf3022Sc006InitDto;

/**
 * 提示データ登録画面のInitサービス処理クラス。　 
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf3022Sc006InitService extends BaseServiceAbstract<Skf3022Sc006InitDto> {
	

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
	public Skf3022Sc006InitDto index(Skf3022Sc006InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF3022_SC006_TITLE);
 		
		//デバッグコード
		initDto.setHdnShatakuKanriNo("2016010195");
		initDto.setShatakuName("ｶﾞｰﾃﾞﾝﾋﾙｽﾞ原山");
		
		return initDto;
	}
	
}
