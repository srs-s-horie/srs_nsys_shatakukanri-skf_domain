/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010fc001;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBatchUtils.SkfBatchUtilsGetMultipleTablesUpdateDateExp;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.util.batch.SkfBatchUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010fc001.Skf2010Fc001InitDto;

/**
 * Skf2010Fc001InitService
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf2010Fc001InitService extends BaseServiceAbstract<Skf2010Fc001InitDto> {

	public static final String DATA_LINKAGE_KEY_SKF2010FC001 = "mapKeyOfSkf2010Fc001Test";
	@Autowired
	private SkfBatchUtils skfBatchUtils;

	/**
	 * 画面初期表示のメイン処理
	 */
	@Override
	protected BaseDto index(Skf2010Fc001InitDto initDto) throws Exception {

		Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> forUpdateMap = skfBatchUtils
				.getUpdateDateForUpdateSQL(null);

		menuScopeSessionBean.put(DATA_LINKAGE_KEY_SKF2010FC001, forUpdateMap);
		return initDto;
	}

}
