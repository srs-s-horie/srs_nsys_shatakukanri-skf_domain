/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc005;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2100Sc005.Skf2100Sc005GetListTableDataExpParameter;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc005.Skf2100Sc005SearchDto;

/**
 * Skf2100Sc005SearchService モバイルルーター貸出管理簿検索処理Service
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2100Sc005SearchService extends SkfServiceAbstract<Skf2100Sc005SearchDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private Skf2100Sc005SharedService skf2100Sc005SharedService;

	@Value("${skf2100.skf2100_sc005.max_search_count}")
	private String maxSearchCount;
	@Value("${skf2100.skf2100_sc005.max_row_count}")
	private String maxRowCount;
	// 半角数字チェック正規表現
	private static final String NO_CHECK_REG = "^[0-9]*$";

	@Override
	protected BaseDto index(Skf2100Sc005SearchDto inDto) throws Exception {

		skfOperationLogUtils.setAccessLog("検索", CodeConstant.C001, FunctionIdConstant.SKF2100_SC005);

		List<Map<String, Object>> initSearchData = new ArrayList<Map<String, Object>>();
		inDto.setSearchDataList(initSearchData);
		inDto.setResultMessages(null);
		inDto.setSearchParam(null);
		inDto = editLabel(inDto);

		if("true".equals(inDto.getHdnChkAkiRouterSelect())){
			inDto.setHdnChkAkiRouterSelect("true");
			inDto.setChkAkiRouter("1");
		}else{
			inDto.setHdnChkAkiRouterSelect(null);
			inDto.setChkAkiRouter(null);
		}
		
		inDto = (Skf2100Sc005SearchDto) skf2100Sc005SharedService.setDropDownSelect(inDto);
		
		// 入力チェック
		if(!isErrDateFormat(inDto)){
			return inDto;
		}

		Skf2100Sc005GetListTableDataExpParameter searchParam = skf2100Sc005SharedService
				.createSearchParam(inDto, null, false);

		long searchCount = skf2100Sc005SharedService.getRouterListCount(searchParam);
		long maxCnt = Long.parseLong(maxSearchCount);

		if (searchCount == 0) {
			ServiceHelper.addErrorResultMessage(inDto, null, MessageIdConstant.W_SKF_1007);
			return inDto;

		} else if (searchCount > maxCnt) {
			ServiceHelper.addErrorResultMessage(inDto, null, MessageIdConstant.E_SKF_1046, maxCnt);
			return inDto;

		} else {
			List<Map<String, Object>> searchDataList = skf2100Sc005SharedService.createSearchList(searchParam, inDto);
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
	private Skf2100Sc005SearchDto editLabel(Skf2100Sc005SearchDto inDto) {
		
		Map<String, String> getsujiShoriJoukyouShoukaiMap = skf2100Sc005SharedService
				.getGetsujiShoriJoukyouShoukai(inDto.getHdnYearSelect() + inDto.getHdnMonthSelect());
		inDto.setLabelShimeShori(getsujiShoriJoukyouShoukaiMap.get(Skf2100Sc005SharedService.SHIME_SHORI_TXT_KEY));
		inDto.setLabelPositiveRenkei(
				getsujiShoriJoukyouShoukaiMap.get(Skf2100Sc005SharedService.POSITIVE_RENKEI_TXT_KEY));
		
		return inDto;
	}

	/**
	 * 整合性チェック
	 * @param comDto
	 * @param errMsg エラーメッセージ<
	 */
	private boolean isErrDateFormat(Skf2100Sc005SearchDto comDto) throws ParseException{
		
		boolean result = true;
		// 通しNoチェック
		comDto.setTxtRouterNoErr("");
		String routerNo = (comDto.getTxtRouterNo() != null) ? comDto.getTxtRouterNo() : "";
		if( !SkfCheckUtils.isNullOrEmpty(routerNo) && !routerNo.matches(NO_CHECK_REG)){
			//数値以外
			comDto.setTxtRouterNoErr(CodeConstant.NFW_VALIDATION_ERROR);
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1050,"通しNo");
			result =  false;
		}
		
		// 契約終了日のFromとToが入力されている場合
		comDto.setTxtContractEndDateFromErr("");
		comDto.setTxtContractEndDateToErr("");
		if(!SkfCheckUtils.isNullOrEmpty(comDto.getTxtContractEndDateFrom())
			&& !SkfCheckUtils.isNullOrEmpty(comDto.getTxtContractEndDateTo())){
			if(skfDateFormatUtils.validateDateCorrelation(comDto.getTxtContractEndDateFrom(),comDto.getTxtContractEndDateTo())){
				comDto.setTxtContractEndDateFromErr(CodeConstant.NFW_VALIDATION_ERROR);
				comDto.setTxtContractEndDateToErr(CodeConstant.NFW_VALIDATION_ERROR);
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1133,"契約終了日");
				result =  false;
			}
		}
		
		return result;
	
	}

}
