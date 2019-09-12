/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skfcomb.domain.service.skfcombatch1;

import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.skfcomb.domain.dto.skfcombatch1.SkfComBatch1InitDto;

/**
 * Skf3090Sc005InitService 社宅管理台帳データ登録（社宅情報）更新テスト初期表示クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class SkfComBatch1InitService
		extends BaseServiceAbstract<SkfComBatch1InitDto> {

	/**
	 * 画面初期表示のメイン処理
	 */
	@Override
	protected BaseDto index(SkfComBatch1InitDto initDto) throws Exception {
		return initDto;
	}

}
