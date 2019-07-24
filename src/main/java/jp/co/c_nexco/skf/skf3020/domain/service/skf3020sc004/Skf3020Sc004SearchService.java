/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.service.skf3020sc004;

import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.skf3020.domain.dto.skf3020sc004.Skf3020Sc004SearchDto;

/**
 * 転任者一覧画面のInitサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3020Sc004SearchService extends BaseServiceAbstract<Skf3020Sc004SearchDto> {

	/**
	 * サービス処理を行う。
	 * 
	 * @param searchDto 検索DTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf3020Sc004SearchDto index(Skf3020Sc004SearchDto searchDto) throws Exception {

		searchDto.setPageTitleKey(MessageIdConstant.SKF3020_SC004_TITLE);

		return searchDto;
	}

}
