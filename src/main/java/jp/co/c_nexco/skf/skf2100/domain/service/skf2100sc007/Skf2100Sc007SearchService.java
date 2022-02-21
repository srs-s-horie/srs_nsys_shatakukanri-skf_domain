/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc007;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2100Sc007.Skf2100Sc007GetListTableDataExpParameter;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc007.Skf2100Sc007SearchDto;

/**
 * Skf2100Sc005SearchService モバイルルーターマスタ検索処理Service
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2100Sc007SearchService extends SkfServiceAbstract<Skf2100Sc007SearchDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private Skf2100Sc007SharedService skf2100Sc007SharedService;

	@Value("${skf2100.skf2100_sc007.max_search_count}")
	private String maxSearchCount;
	@Value("${skf2100.skf2100_sc007.max_row_count}")
	private String maxRowCount;
	// 半角数字チェック正規表現
	private static final String NO_CHECK_REG = "^[0-9]*$";

	@Override
	protected BaseDto index(Skf2100Sc007SearchDto inDto) throws Exception {

		skfOperationLogUtils.setAccessLog("検索", CodeConstant.C001, FunctionIdConstant.SKF2100_SC007);

		List<Map<String, Object>> initSearchData = new ArrayList<Map<String, Object>>();
		inDto.setSearchDataList(initSearchData);
		inDto.setResultMessages(null);
		inDto.setSearchParam(null);
		
		inDto = (Skf2100Sc007SearchDto) skf2100Sc007SharedService.setDropDownSelect(inDto);
		if(!"true".equals(inDto.getHdnChkFaultSelect())){
			inDto.setHdnChkFaultSelect(null);
		}
		
		
		//日付の整合性判定
		if(!isErrDateFormat(inDto)){
			return inDto;
		}

		Skf2100Sc007GetListTableDataExpParameter searchParam = skf2100Sc007SharedService
				.createSearchParam(inDto);
		inDto.setSearchParam(searchParam);

		long searchCount = skf2100Sc007SharedService.getRouterListCount(searchParam);
		long maxCnt = Long.parseLong(maxSearchCount);

		if (searchCount == 0) {
			ServiceHelper.addErrorResultMessage(inDto, null, MessageIdConstant.W_SKF_1007);
			return inDto;

		} else if (searchCount > maxCnt) {
			ServiceHelper.addErrorResultMessage(inDto, null, MessageIdConstant.E_SKF_1046, maxCnt);
			return inDto;

		} else {
			List<Map<String, Object>> searchDataList = skf2100Sc007SharedService.createSearchList(searchParam, inDto);
			inDto.setSearchDataList(searchDataList);
			inDto.setSearchParam(searchParam);
		}

		return inDto;
	}
	
	/**
	 * 整合性チェック
	 * @param comDto
	 * @param errMsg エラーメッセージ<
	 */
	private boolean isErrDateFormat(Skf2100Sc007SearchDto comDto) throws ParseException{
		
		boolean result= true;
		
		// 通しNoチェック
		comDto.setTxtRouterNoErr("");
		String routerNo = (comDto.getTxtRouterNo() != null) ? comDto.getTxtRouterNo() : "";
		if( !SkfCheckUtils.isNullOrEmpty(routerNo) && !routerNo.matches(NO_CHECK_REG)){
			//数値以外
			comDto.setTxtRouterNoErr(CodeConstant.NFW_VALIDATION_ERROR);
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1050,"通しNo");
			result =  false;
		}
		
		// ルーター入荷日のFromとToが入力されている場合
		comDto.setTxtArrivalDateFromErr("");
		comDto.setTxtArrivalDateToErr("");
		if(!SkfCheckUtils.isNullOrEmpty(comDto.getTxtArrivalDateFrom())
			&& !SkfCheckUtils.isNullOrEmpty(comDto.getTxtArrivalDateTo())){
			if(skfDateFormatUtils.validateDateCorrelation(comDto.getTxtArrivalDateFrom(),comDto.getTxtArrivalDateTo())){
				comDto.setTxtArrivalDateFromErr(CodeConstant.NFW_VALIDATION_ERROR);
				comDto.setTxtArrivalDateToErr(CodeConstant.NFW_VALIDATION_ERROR);
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1133,"ルーター入荷日");
				result =  false;
			}
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
				result = false;
			}
		}
		

		
		return result;
	
	}

}
