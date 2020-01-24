/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2030.domain.service.skf2030fc001;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBatchUtils.SkfBatchUtilsGetMultipleTablesUpdateDateExp;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.util.batch.SkfBatchUtils;
import jp.co.c_nexco.skf.skf2030.domain.dto.skf2030fc001.Skf2030Fc001InitDto;

/**
 * Skf2040Fc001InitService 従業員マスタ登録初期表示処理クラス.
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf2030Fc001InitService extends BaseServiceAbstract<Skf2030Fc001InitDto> {

	@Autowired
	SkfBatchUtils skfBatchUtils;

	// マップのキーはSessionCacheKeyConstantクラスに定義すること！
	private static final String DATA_LINKAGE_KEY_SKF2030FC001 = "keyForUpdate";

	/**
	 * 画面初期表示のメイン処理
	 */
	@Override
	protected BaseDto index(Skf2030Fc001InitDto initDto) throws Exception {

		// データ連携機能使用時に更新対象となるテーブルの現在の更新日時を全て取得
		Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> forUpdateMap = skfBatchUtils
				.getUpdateDateForUpdateSQL(null);

		// 取得したMapをmenuScopeSessionBeanにMapでセットする
		menuScopeSessionBean.put(DATA_LINKAGE_KEY_SKF2030FC001, forUpdateMap);

		return initDto;
	}

}
