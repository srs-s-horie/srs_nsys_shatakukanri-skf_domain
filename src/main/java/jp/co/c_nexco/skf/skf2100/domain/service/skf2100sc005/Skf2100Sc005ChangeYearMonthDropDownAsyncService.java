/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc005;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceOnBatchAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc005.Skf2100Sc005ChangeYearMonthDropDownAsyncDto;

/**
 * Skf3030Sc001ChangeYearDropDownAsyncDto 社宅管理台帳「年」ドロップダウン変更処理Service
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2100Sc005ChangeYearMonthDropDownAsyncService
		extends AsyncBaseServiceOnBatchAbstract<Skf2100Sc005ChangeYearMonthDropDownAsyncDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf2100Sc005SharedService skf2100Sc005SharedService;

	@Override
	protected AsyncBaseDto index(Skf2100Sc005ChangeYearMonthDropDownAsyncDto inDto) throws Exception {

		skfOperationLogUtils.setAccessLog("「年月」ドロップダウン変更処理開始", CodeConstant.C001, FunctionIdConstant.SKF2100_SC005);

		Map<String, String> getsujiShoriJoukyouShoukaiMap = skf2100Sc005SharedService
				.getGetsujiShoriJoukyouShoukai(inDto.getHdnAsyncYearSelect() + inDto.getHdnAsyncMonthSelect());
		inDto.setAsyncLabelShimeShori(getsujiShoriJoukyouShoukaiMap.get(Skf2100Sc005SharedService.SHIME_SHORI_TXT_KEY));
		inDto.setAsyncLabelPositiveRenkei(
				getsujiShoriJoukyouShoukaiMap.get(Skf2100Sc005SharedService.POSITIVE_RENKEI_TXT_KEY));

		return inDto;
	}

}
