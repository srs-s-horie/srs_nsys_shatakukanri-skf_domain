/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3030.domain.service.skf3030sc001;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc001.Skf3030Sc001GetShatakuKanriDaichoInfoExpParameter;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3030.domain.dto.skf3030sc001.Skf3030Sc001SearchDto;

/**
 * Skf3030Sc001SearchService 社宅管理台帳検索処理Service
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf3030Sc001SearchService extends BaseServiceAbstract<Skf3030Sc001SearchDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3030Sc001SharedService skf3030Sc001SharedService;

	@Value("${skf3030.skf3030_sc001.max_search_count}")
	private String maxSearchCount;
	@Value("${skf3030.skf3030_sc001.max_row_count}")
	private String maxRowCount;

	@Override
	protected BaseDto index(Skf3030Sc001SearchDto inDto) throws Exception {

		skfOperationLogUtils.setAccessLog("社宅管理台帳「検索」処理", CodeConstant.C001, FunctionIdConstant.SKF3030_SC001);

		List<Map<String, Object>> initSearchData = new ArrayList<Map<String, Object>>();
		inDto.setSearchDataList(initSearchData);
		inDto.setResultMessages(null);
		inDto.setSearchParam(null);
		inDto = editLabel(inDto);

		inDto = (Skf3030Sc001SearchDto) skf3030Sc001SharedService.setDropDownSelect(inDto);

		Skf3030Sc001GetShatakuKanriDaichoInfoExpParameter searchParam = skf3030Sc001SharedService
				.createSearchParam(inDto, null, false);

		long searchCount = skf3030Sc001SharedService.getShatakuKanriDaichoCount(searchParam);
		long maxCnt = Long.parseLong(maxSearchCount);

		if (searchCount == 0) {
			ServiceHelper.addErrorResultMessage(inDto, null, MessageIdConstant.W_SKF_1007);
			return inDto;

		} else if (searchCount > maxCnt) {
			ServiceHelper.addErrorResultMessage(inDto, null, MessageIdConstant.E_SKF_1046, maxCnt);
			return inDto;

		} else {
			List<Map<String, Object>> searchDataList = skf3030Sc001SharedService.createSearchList(searchParam, inDto);
			inDto.setSearchDataList(searchDataList);
			inDto.setSearchParam(searchParam);
		}

		return inDto;
	}
	
	/**
	 * 「締め処理」、「給与連携」のラベルを編集する。
	 * 
	 * @param inDto Skf3030Sc001SearchDto
	 * @return Skf3030Sc001SearchDto
	 */
	private Skf3030Sc001SearchDto editLabel(Skf3030Sc001SearchDto inDto) {
		
		Map<String, String> getsujiShoriJoukyouShoukaiMap = skf3030Sc001SharedService
				.getGetsujiShoriJoukyouShoukai(inDto.getHdnYearSelect() + inDto.getHdnMonthSelect());
		inDto.setLabelShimeShori(getsujiShoriJoukyouShoukaiMap.get(Skf3030Sc001SharedService.SHIME_SHORI_TXT_KEY));
		inDto.setLabelPositiveRenkei(
				getsujiShoriJoukyouShoukaiMap.get(Skf3030Sc001SharedService.POSITIVE_RENKEI_TXT_KEY));
		
		return inDto;
	}

}
