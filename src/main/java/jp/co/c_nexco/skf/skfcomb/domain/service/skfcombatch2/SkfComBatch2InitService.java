/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skfcomb.domain.service.skfcombatch2;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBatchUtils.SkfBatchUtilsGetMultipleTablesUpdateDateExp;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.util.batch.SkfBatchUtils;
import jp.co.c_nexco.skf.skfcomb.domain.dto.skfcombatch2.SkfComBatch2InitDto;

/**
 * Skf3090Sc005InitService 社宅管理台帳データ登録（備品）更新テスト初期表示クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class SkfComBatch2InitService extends BaseServiceAbstract<SkfComBatch2InitDto> {

	@Autowired
	private SkfBatchUtils skfBatchUtils;
	private static final String DATA_LINKAGE_KEY_COMBATCH2 = "mapKeyOfComBatch2";

	/**
	 * 画面初期表示のメイン処理
	 */
	@Override
	protected BaseDto index(SkfComBatch2InitDto initDto) throws Exception {

		// 更新候補の更新日時一覧取得
		Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> testMap = skfBatchUtils
				.getUpdateDateForUpdateSQL(null);
		// メニュースコープにput
		menuScopeSessionBean.put(DATA_LINKAGE_KEY_COMBATCH2, testMap);

		return initDto;
	}

}
