/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.service.skf3020sc005;

import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.skf3020.domain.dto.skf3020sc005.Skf3020Sc005InitDto;

/**
 * TestPrjTop画面のInitサービス処理クラス。　 
 * 
 */
@Service
public class Skf3020Sc005InitService extends BaseServiceAbstract<Skf3020Sc005InitDto> {
	

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
	public Skf3020Sc005InitDto index(Skf3020Sc005InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF3020_SC005_TITLE);
 		
		return initDto;
	}
	
}
