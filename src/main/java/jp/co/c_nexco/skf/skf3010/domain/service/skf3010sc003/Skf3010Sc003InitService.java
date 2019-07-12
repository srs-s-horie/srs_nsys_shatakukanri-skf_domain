/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc003;

import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc003.Skf3010Sc003InitDto;

/**
 * TestPrjTop画面のInitサービス処理クラス。　 
 * 
 */
@Service
public class Skf3010Sc003InitService extends BaseServiceAbstract<Skf3010Sc003InitDto> {
	

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
	public Skf3010Sc003InitDto index(Skf3010Sc003InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF2010_SC003_TITLE);
 		
		return initDto;
	}
	
}
