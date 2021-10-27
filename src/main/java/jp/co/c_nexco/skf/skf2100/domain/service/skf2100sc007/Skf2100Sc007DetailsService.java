/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc007;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2100Sc007.Skf2100Sc007GetListTableDataExpParameter;
import jp.co.c_nexco.nfw.common.bean.ApplicationScopeBean;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc007.Skf2100Sc007DetailsDto;

/**
 * Skf2100Sc005DetailsService モバイルルーターマスタ詳細表示処理Service
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2100Sc007DetailsService extends SkfServiceAbstract<Skf2100Sc007DetailsDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private ApplicationScopeBean bean;

	@Override
	protected BaseDto index(Skf2100Sc007DetailsDto inDto) throws Exception {

		if(!SkfCheckUtils.isNullOrEmpty(inDto.getHdnSelIdx())){
			skfOperationLogUtils.setAccessLog("詳細", CodeConstant.C001, FunctionIdConstant.SKF2100_SC007);
		}else{
			skfOperationLogUtils.setAccessLog("新規", CodeConstant.C001, FunctionIdConstant.SKF2100_SC007);
		}
		
		
		createSessionData(inDto);

		TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF2100_SC008, "init");
		inDto.setTransferPageInfo(nextPage);

		return inDto;
	}

	/**
	 * 登録画面へ渡すセッションデータを作成する。
	 * 
	 * @param inDto
	 *            Skf2100Sc007DetailsDto
	 * @return セッションデータ
	 */
	private void createSessionData(Skf2100Sc007DetailsDto inDto) {
		
		Map<String, Object> listSelDataSessionMap = new HashMap<String, Object>();
	
		int selIdx = 0;
		if(!SkfCheckUtils.isNullOrEmpty(inDto.getHdnSelIdx())){
			// リストの詳細ボタンから選択
			selIdx = Integer.parseInt(inDto.getHdnSelIdx());
			Map<String, Object> searchData = inDto.getSearchDataList().get(selIdx - 1);

			listSelDataSessionMap.put(Skf2100Sc007CommonSharedService.SEL_LIST_INFO_ROUTER_NO_KEY, 
					searchData.get(Skf2100Sc007CommonSharedService.SEARCH_COL_ROUTER_NO_KEY));
			
		}else{
			// 新規ボタンから
			listSelDataSessionMap.put(Skf2100Sc007CommonSharedService.SEL_LIST_INFO_ROUTER_NO_KEY, 0L);
		}
		
		bean.put(SessionCacheKeyConstant.MOBILEROUTER_MASTER_INFO, listSelDataSessionMap);
		
		//検索条件
		Map<String, Object> searchInfoSessionMap = new HashMap<String, Object>();
		Skf2100Sc007GetListTableDataExpParameter searchParam = inDto.getSearchParam();

		if(searchParam.getMobileRouterNo() != null){
			searchInfoSessionMap.put(Skf2100Sc007CommonSharedService.SEARCH_INFO_ROUTER_NO_KEY, searchParam.getMobileRouterNo().toString());
		}else{
			searchInfoSessionMap.put(Skf2100Sc007CommonSharedService.SEARCH_INFO_ROUTER_NO_KEY, "");
		}
		searchInfoSessionMap.put(Skf2100Sc007CommonSharedService.SEARCH_INFO_TEL_KEY, searchParam.getTel());
		searchInfoSessionMap.put(Skf2100Sc007CommonSharedService.SEARCH_INFO_ICCID_KEY, searchParam.getIccid());
		searchInfoSessionMap.put(Skf2100Sc007CommonSharedService.SEARCH_INFO_IMEI_KEY, searchParam.getImei());
		searchInfoSessionMap.put(Skf2100Sc007CommonSharedService.SEARCH_INFO_CONTRACT_KBN_KEY, searchParam.getRouterContractKbn());
		searchInfoSessionMap.put(Skf2100Sc007CommonSharedService.SEARCH_INFO_CONTRACT_END_DATE_FROM_KEY, searchParam.getContractEndDateFrom());
		searchInfoSessionMap.put(Skf2100Sc007CommonSharedService.SEARCH_INFO_CONTRACT_END_DATE_TO_KEY, searchParam.getContractEndDateTo());
		searchInfoSessionMap.put(Skf2100Sc007CommonSharedService.SEARCH_INFO_FAULT_KEY, searchParam.getFaultFlag());
		searchInfoSessionMap.put(Skf2100Sc007CommonSharedService.SEARCH_INFO_ARRIVAL_DATE_FROM_KEY, searchParam.getArrivalDateFrom());
		searchInfoSessionMap.put(Skf2100Sc007CommonSharedService.SEARCH_INFO_ARRIVAL_DATE_TO_KEY, searchParam.getArrivalDateTo());
		
		bean.put(SessionCacheKeyConstant.MOBILEROUTER_MASTER_SEARCH, searchInfoSessionMap);
		
	}

}
