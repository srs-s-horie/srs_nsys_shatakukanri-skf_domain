/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc007;

import org.springframework.stereotype.Service;

/**
 * Skf2100Sc007CommonSharedService Skf2100Sc007共通処理Service
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2100Sc007CommonSharedService {

	
	public static final String SEARCH_COL_ROUTER_NO_KEY = "col1";
	public static final String SEARCH_COL_TEL_KEY = "col2";
	public static final String SEARCH_COL_ICCID_KEY = "col3";
	public static final String SEARCH_COL_IMEI_KEY = "col4";
	public static final String SEARCH_COL_ARRIVAL_DATE_KEY = "col5";
	public static final String SEARCH_COL_CONTRACT_END_DATE_KEY = "col6";
	public static final String SEARCH_COL_CONTARCT_KBN_KEY = "col7";
	public static final String SEARCH_COL_FAULT_KEY = "col8";
	public static final String SEARCH_COL_DETAIL_KEY = "col9";


	/** 「モバイルルーターマスタ登録」画面へ渡すセッション情報mapのkey */
	public static final String SEARCH_INFO_ROUTER_NO_KEY = "skf2100sc007RouterNoKey";
	public static final String SEARCH_INFO_ICCID_KEY = "skf2100sc007IccidKey";
	public static final String SEARCH_INFO_IMEI_KEY = "skf2100sc007ImeiKey";
	public static final String SEARCH_INFO_TEL_KEY = "skf2100sc007TelKey";
	public static final String SEARCH_INFO_CONTRACT_KBN_KEY = "skf2100sc007ContractKbnKey";
	public static final String SEARCH_INFO_FAULT_KEY = "skf2100sc007FaultKey";
	public static final String SEARCH_INFO_CONTRACT_END_DATE_FROM_KEY = "skf2100sc007ContractEndDateFromKey";
	public static final String SEARCH_INFO_CONTRACT_END_DATE_TO_KEY = "skf2100sc007ContractEndDateToKey";
	public static final String SEARCH_INFO_ARRIVAL_DATE_FROM_KEY = "skf2100sc007ArrivalDateFromKey";
	public static final String SEARCH_INFO_ARRIVAL_DATE_TO_KEY = "skf2100sc007ArrivalDateToKey";

	
	public static final String SEL_LIST_INFO_ROUTER_NO_KEY = "skf2100sc007SelRouterNoKey";


}
