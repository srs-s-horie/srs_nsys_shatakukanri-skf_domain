/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc002;

import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc002.Skf2010Sc002PresentationDto;

/**
 * Skf2010Sc002 申請書類確認の提示ボタン処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc002PresentationService extends BaseServiceAbstract<Skf2010Sc002PresentationDto> {

	@Override
	protected BaseDto index(Skf2010Sc002PresentationDto preDto) throws Exception {
		// TODO アウトソース画面からの遷移時
		return preDto;
	}

}
