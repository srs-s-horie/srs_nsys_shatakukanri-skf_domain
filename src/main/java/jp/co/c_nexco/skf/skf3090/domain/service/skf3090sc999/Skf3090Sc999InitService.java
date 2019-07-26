/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc999;

import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc999.Skf3090Sc999InitDto;

/**
 * Skf3090Sc005InitService 従業員マスタ登録初期表示処理クラス.
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3090Sc999InitService extends BaseServiceAbstract<Skf3090Sc999InitDto> {

	/**
	 * 画面初期表示のメイン処理
	 */
	@Override
	public BaseDto index(Skf3090Sc999InitDto initDto) throws Exception {

		return initDto;
	}

}
