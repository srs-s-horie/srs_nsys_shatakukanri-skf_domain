/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skfbatch.domain.service.UpdateShatakuKanriDaichoShatakuData;

import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.skfbatch.domain.dto.UpdateShatakuKanriDaichoShatakuData.UpdateShatakuKanriDaichoShatakuDataInitDto;

/**
 * Skf3090Sc005InitService 社宅管理台帳データ登録（社宅情報）更新テスト初期表示クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class UpdateShatakuKanriDaichoShatakuDataInitService
		extends BaseServiceAbstract<UpdateShatakuKanriDaichoShatakuDataInitDto> {

	/**
	 * 画面初期表示のメイン処理
	 */
	@Override
	protected BaseDto index(UpdateShatakuKanriDaichoShatakuDataInitDto initDto) throws Exception {
		return initDto;
	}

}
