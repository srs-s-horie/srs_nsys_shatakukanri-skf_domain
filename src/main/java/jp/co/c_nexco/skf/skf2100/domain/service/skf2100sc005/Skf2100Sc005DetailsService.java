/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc005;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2100Sc005.Skf2100Sc005GetListTableDataExpParameter;
import jp.co.c_nexco.nfw.common.bean.ApplicationScopeBean;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc005.Skf2100Sc005DetailsDto;

/**
 * Skf2100Sc005DetailsService モバイルルーター貸出管理簿詳細処理Service
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2100Sc005DetailsService extends SkfServiceAbstract<Skf2100Sc005DetailsDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private ApplicationScopeBean bean;

	@Override
	protected BaseDto index(Skf2100Sc005DetailsDto inDto) throws Exception {

		skfOperationLogUtils.setAccessLog("詳細", CodeConstant.C001, FunctionIdConstant.SKF2100_SC005);

		createSessionData(inDto);

		TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF2100_SC006, "init");
		inDto.setTransferPageInfo(nextPage);

		return inDto;
	}

	/**
	 * 「入退去情報登録」画面へ渡すセッションデータを作成する。
	 * 
	 * @param inDto
	 *            Skf3030Sc001DetailsDto
	 * @return セッションデータ
	 */
	private void createSessionData(Skf2100Sc005DetailsDto inDto) {
		
		Map<String, Object> listSelDataSessionMap = new HashMap<String, Object>();
	
		int selIdx = Integer.parseInt(inDto.getHdnSelIdx());
		Map<String, Object> searchData = inDto.getSearchDataList().get(selIdx - 1);

		listSelDataSessionMap.put(Skf2100Sc005CommonSharedService.SEL_LIST_INFO_ROUTER_KANRI_ID_KEY, 
				searchData.get(Skf2100Sc005CommonSharedService.SEARCH_COL_ROUTER_KANRI_ID_KEY));
		listSelDataSessionMap.put(Skf2100Sc005CommonSharedService.SEL_LIST_INFO_ROUTER_NO_KEY, 
				searchData.get(Skf2100Sc005CommonSharedService.SEARCH_COL_ROUTER_NO_KEY));
		listSelDataSessionMap.put(Skf2100Sc005CommonSharedService.SEARCH_COL_STATUS_CODE_KEY, 
				searchData.get(Skf2100Sc005CommonSharedService.SEARCH_COL_STATUS_KEY));
		listSelDataSessionMap.put(Skf2100Sc005CommonSharedService.SEL_LIST_INFO_SHAIN_NO_KEY, 
				searchData.get(Skf2100Sc005CommonSharedService.SEARCH_COL_SHAIN_NO_KEY));
		listSelDataSessionMap.put(Skf2100Sc005CommonSharedService.SEL_LIST_INFO_SHAIN_NAME_KEY, 
				searchData.get(Skf2100Sc005CommonSharedService.SEARCH_COL_SHAIN_NAME_KEY));
		listSelDataSessionMap.put(Skf2100Sc005CommonSharedService.SEL_LIST_INFO_YEARMONTH_KEY, 
				inDto.getHdnYearSelect() + inDto.getHdnMonthSelect());
		bean.put(SessionCacheKeyConstant.MOBILEROUTER_LEDGER_INFO, listSelDataSessionMap);
		
		Map<String, Object> searchInfoSessionMap = new HashMap<String, Object>();
		Skf2100Sc005GetListTableDataExpParameter searchParam = inDto.getSearchParam();

		if(searchParam.getMobileRouterNo() != null){
			searchInfoSessionMap.put(Skf2100Sc005CommonSharedService.SEARCH_INFO_ROUTER_NO_KEY, searchParam.getMobileRouterNo().toString());
		}else{
			searchInfoSessionMap.put(Skf2100Sc005CommonSharedService.SEARCH_INFO_ROUTER_NO_KEY, "");
		}
		searchInfoSessionMap.put(Skf2100Sc005CommonSharedService.SEARCH_INFO_TEL_KEY, searchParam.getTel());
		searchInfoSessionMap.put(Skf2100Sc005CommonSharedService.SEARCH_INFO_SHAIN_NO_KEY, searchParam.getShainNo());
		searchInfoSessionMap.put(Skf2100Sc005CommonSharedService.SEARCH_INFO_SHAIN_NAME_KEY, searchParam.getShainName());
		searchInfoSessionMap.put(Skf2100Sc005CommonSharedService.SEARCH_INFO_CONTRACT_KBN_KEY, searchParam.getRouterContractKbn());
		searchInfoSessionMap.put(Skf2100Sc005CommonSharedService.SEARCH_INFO_CONTRACT_END_DATE_FROM_KEY, searchParam.getContractEndDateFrom());
		searchInfoSessionMap.put(Skf2100Sc005CommonSharedService.SEARCH_INFO_CONTRACT_END_DATE_TO_KEY, searchParam.getContractEndDateTo());
		searchInfoSessionMap.put(Skf2100Sc005CommonSharedService.SEARCH_INFO_NENGETSU_KEY, searchParam.getYearMonth());
		searchInfoSessionMap.put(Skf2100Sc005CommonSharedService.SEARCH_INFO_AKI_ROUTER_KEY, searchParam.getAkiRouter());

		bean.put(SessionCacheKeyConstant.MOBILEROUTER_LEDGER_SEARCH, searchInfoSessionMap);
		
	}

}
