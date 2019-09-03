/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.service.skf2040fc001;

import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040fc001.Skf2040Fc001InitDto;

/**
 * Skf3090Sc005InitService 従業員マスタ登録初期表示処理クラス.
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf2040Fc001InitService extends BaseServiceAbstract<Skf2040Fc001InitDto> {

	/**
	 * 画面初期表示のメイン処理
	 */
	@Override
	protected BaseDto index(Skf2040Fc001InitDto initDto) throws Exception {
		return initDto;
	}

}
