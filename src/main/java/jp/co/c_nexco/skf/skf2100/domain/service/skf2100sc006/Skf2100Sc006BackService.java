/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc006;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.bean.ApplicationScopeBean;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc006.Skf2100Sc006BackDto;
import jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc005.Skf2100Sc005CommonSharedService;

/**
 * Skf2100Sc006BackService 前の画面サービス処理クラス。　 
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf2100Sc006BackService extends SkfServiceAbstract<Skf2100Sc006BackDto> {
	
	@Autowired
	private ApplicationScopeBean bean;
	
	/**
	 * サービス処理を行う。　
	 * 
	 * @param backDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public Skf2100Sc006BackDto index(Skf2100Sc006BackDto backDto) throws Exception {
		
		// デバッグログ
		LogUtils.debugByMsg("前の画面へ");
		//社宅管理台帳画面へ遷移
		TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF2100_SC005);
		backDto.setTransferPageInfo(nextPage);
		
		//セッション情報設定
		Map<String, Object> searchInfoSessionMap = new HashMap<String, Object>();

		searchInfoSessionMap.put(Skf2100Sc005CommonSharedService.SEARCH_INFO_ROUTER_NO_KEY, backDto.getSearchInfoRouterNo());
		searchInfoSessionMap.put(Skf2100Sc005CommonSharedService.SEARCH_INFO_TEL_KEY, backDto.getSearchInfoTel());
		searchInfoSessionMap.put(Skf2100Sc005CommonSharedService.SEARCH_INFO_SHAIN_NO_KEY, backDto.getSearchInfoShainNo());
		searchInfoSessionMap.put(Skf2100Sc005CommonSharedService.SEARCH_INFO_SHAIN_NAME_KEY, backDto.getSearchInfoShainName());
		searchInfoSessionMap.put(Skf2100Sc005CommonSharedService.SEARCH_INFO_CONTRACT_KBN_KEY, backDto.getSearchInfoContractKbn());
		searchInfoSessionMap.put(Skf2100Sc005CommonSharedService.SEARCH_INFO_NENGETSU_KEY, backDto.getSearchInfoYearMonth());
		searchInfoSessionMap.put(Skf2100Sc005CommonSharedService.SEARCH_INFO_AKI_ROUTER_KEY, backDto.getSearchInfoAkiRouter());
		searchInfoSessionMap.put(Skf2100Sc005CommonSharedService.SEARCH_INFO_CONTRACT_END_DATE_FROM_KEY, backDto.getSearchInfoContractEndDateFrom());
		searchInfoSessionMap.put(Skf2100Sc005CommonSharedService.SEARCH_INFO_CONTRACT_END_DATE_TO_KEY, backDto.getSearchInfoContractEndDateTo());
		bean.put(SessionCacheKeyConstant.MOBILEROUTER_LEDGER_SEARCH, searchInfoSessionMap);
		
		return backDto;
	}
	

}
