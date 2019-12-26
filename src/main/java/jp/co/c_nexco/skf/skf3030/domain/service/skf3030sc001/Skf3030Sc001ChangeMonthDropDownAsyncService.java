/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3030.domain.service.skf3030sc001;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceOnBatchAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3030.domain.dto.skf3030sc001.Skf3030Sc001ChangeMonthDropDownAsyncDto;

/**
 * Skf3030Sc001ChangeMonthDropDownAsyncService 社宅管理台帳「月」ドロップダウン変更処理Service
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf3030Sc001ChangeMonthDropDownAsyncService
		extends AsyncBaseServiceOnBatchAbstract<Skf3030Sc001ChangeMonthDropDownAsyncDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3030Sc001SharedService skf3030Sc001SharedService;

	@Override
	protected AsyncBaseDto index(Skf3030Sc001ChangeMonthDropDownAsyncDto inDto) throws Exception {

		skfOperationLogUtils.setAccessLog("社宅管理台帳「月」ドロップダウン変更処理", CodeConstant.C001, FunctionIdConstant.SKF3030_SC001);

		Map<String, String> getsujiShoriJoukyouShoukaiMap = skf3030Sc001SharedService
				.getGetsujiShoriJoukyouShoukai(inDto.getHdnAsyncYearSelect() + inDto.getHdnAsyncMonthSelect());
		inDto.setAsyncLabelShimeShori(getsujiShoriJoukyouShoukaiMap.get(Skf3030Sc001SharedService.SHIME_SHORI_TXT_KEY));
		inDto.setAsyncLabelPositiveRenkei(
				getsujiShoriJoukyouShoukaiMap.get(Skf3030Sc001SharedService.POSITIVE_RENKEI_TXT_KEY));

		return inDto;
	}

}
