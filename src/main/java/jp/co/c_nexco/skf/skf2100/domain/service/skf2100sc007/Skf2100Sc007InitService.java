/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc007;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2100Sc007.Skf2100Sc007GetListTableDataExpParameter;
import jp.co.c_nexco.nfw.common.bean.ApplicationScopeBean;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc007.Skf2100Sc007InitDto;

/**
 * Skf2100Sc007InitService モバイルルーターマスタ一覧初期表示Service
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2100Sc007InitService extends SkfServiceAbstract<Skf2100Sc007InitDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfDropDownUtils skfDropDownUtils;
	@Autowired
	private Skf2100Sc007SharedService skf2100Sc007SharedService;
	@Autowired
	private ApplicationScopeBean bean;


	@Value("${skf2100.skf2100_sc007.max_search_count}")
	private String maxSearchCount;
	@Value("${skf2100.skf2100_sc007.max_row_count}")
	private String maxRowCount;

	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public Skf2100Sc007InitDto index(Skf2100Sc007InitDto initDto) throws Exception {

		initDto.setPageTitleKey(MessageIdConstant.SKF2100_SC007_TITLE);

		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, FunctionIdConstant.SKF2100_SC007);
		
		initDto = initItems(initDto);

		if (!NfwStringUtils.isEmpty(initDto.getPrePageId())
				&& FunctionIdConstant.SKF2100_SC008.equals(initDto.getPrePageId())) {

			@SuppressWarnings("unchecked")
			Map<String, Object> sessionData = (Map<String, Object>) bean
					.get(SessionCacheKeyConstant.MOBILEROUTER_MASTER_SEARCH);
			if (sessionData != null) {
				initDto = initDispFromRegist(initDto, sessionData);
			}
			//セッション情報削除
			bean.remove(SessionCacheKeyConstant.MOBILEROUTER_MASTER_SEARCH);
			
		}
		
		initDto.setResultMessages(null);
		initDto.setSearchParam(null);
		
		// 一覧情報取得
		Skf2100Sc007GetListTableDataExpParameter searchParam = skf2100Sc007SharedService
				.createSearchParam(initDto);
		initDto.setSearchParam(searchParam);

		long searchCount = skf2100Sc007SharedService.getRouterListCount(searchParam);
		long maxCnt = Long.parseLong(maxSearchCount);

		initDto = (Skf2100Sc007InitDto) skf2100Sc007SharedService.setDropDownSelect(initDto);

		if (searchCount == 0) {
			// 0件の場合
			List<Map<String, Object>> searchDataList = new ArrayList<Map<String, Object>>();
			initDto.setSearchDataList(searchDataList);
			//ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.W_SKF_1007);
			return initDto;

		} else if (searchCount > maxCnt) {
			// 最大件数超え
			List<Map<String, Object>> searchDataList = new ArrayList<Map<String, Object>>();
			initDto.setSearchDataList(searchDataList);
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1046, maxCnt);
			return initDto;

		} else {
			List<Map<String, Object>> searchDataList = skf2100Sc007SharedService.createSearchList(searchParam, initDto);
			initDto.setSearchDataList(searchDataList);
			initDto.setSearchParam(searchParam);
		}
		
		return initDto;
	}

	/**
	 * 画面項目の初期設定。
	 * 
	 * @param initDto
	 *            Skf2100Sc005InitDto
	 * @return Skf2100Sc005InitDto
	 */
	private Skf2100Sc007InitDto initItems(Skf2100Sc007InitDto initDto) {


		/** 「社宅区分」ドロップダウンリスト */
		initDto.setContractKbnDropDownList(null);
		
		/** 「検索結果一覧」リスト */
		initDto.setSearchDataList(null);
		
		/** 「通しNo」テキスト */
		initDto.setTxtRouterNo(null);
		/** 「電話番号」テキスト */
		initDto.setTxtTel(null);
		/** 「ICCID」テキスト */
		initDto.setTxtIccid(null);
		/** 「IMEI」テキスト */
		initDto.setTxtImei(null);
		/** 「契約終了日From」テキスト */
		initDto.setTxtContractEndDateFrom(null);
		/** 「契約終了日To」テキスト */
		initDto.setTxtContractEndDateTo(null);
		/** 「ルーター入荷日From」テキスト */
		initDto.setTxtArrivalDateFrom(null);
		/** 「ルーター入荷日To」テキスト */
		initDto.setTxtArrivalDateTo(null);
		/** 「故障」チェック */
		initDto.setChkFault(null);
		
		/**
		 * hidden 項目
		 */
		/** 選択された 「契約区分」ドロップダウンリストの選択 */
		initDto.setHdnContractKbnSelect(null);
		/** 「故障」チェックボックス選択状態 */
		initDto.setHdnChkFaultSelect(null);
		/** 検索結果一覧の選択されたインデックス */
		initDto.setHdnSelIdx(null);
		/** 検索パラメータ */
		initDto.setSearchParam(null);


		List<Map<String, Object>> contractKbnDropDownList = skfDropDownUtils
				.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_ROUTER_CONTRACT_KBN, "", true);
		initDto.setContractKbnDropDownList(contractKbnDropDownList);
		initDto.setHdnContractKbnSelect("");

		return initDto;
	}

	
	/**
	 * 「モバイルルーターマスタ登録」画面から遷移した場合の初期表示処理
	 * 
	 * @param initDto
	 *            Skf2100Sc005InitDto
	 * @param sessionData
	 *            セッション情報
	 * @return Skf2100Sc005InitDto
	 */
	private Skf2100Sc007InitDto initDispFromRegist(Skf2100Sc007InitDto initDto,
			Map<String, Object> sessionData) {

		// 通しNo
		String routerNo = (String) sessionData.get(Skf2100Sc007CommonSharedService.SEARCH_INFO_ROUTER_NO_KEY);
		initDto.setTxtRouterNo(routerNo);
		// 電話番号
		String tel = (String) sessionData.get(Skf2100Sc007CommonSharedService.SEARCH_INFO_TEL_KEY);
		initDto.setTxtTel(tel);
		// ICCID
		String iccid = (String) sessionData.get(Skf2100Sc007CommonSharedService.SEARCH_INFO_ICCID_KEY);
		initDto.setTxtIccid(iccid);
		// IMEI
		String imei = (String) sessionData.get(Skf2100Sc007CommonSharedService.SEARCH_INFO_IMEI_KEY);
		initDto.setTxtImei(imei);
		// 契約終了日From
		String endDateFrom = (String) sessionData.get(Skf2100Sc007CommonSharedService.SEARCH_INFO_CONTRACT_END_DATE_FROM_KEY);
		initDto.setTxtContractEndDateFrom(endDateFrom);
		// 契約終了日To
		String endDateTo = (String) sessionData.get(Skf2100Sc007CommonSharedService.SEARCH_INFO_CONTRACT_END_DATE_TO_KEY);
		initDto.setTxtContractEndDateTo(endDateTo);
		// 契約終了日From
		String arrivalDateFrom = (String) sessionData.get(Skf2100Sc007CommonSharedService.SEARCH_INFO_ARRIVAL_DATE_FROM_KEY);
		initDto.setTxtArrivalDateFrom(arrivalDateFrom);
		// 契約終了日To
		String arrivalDateTo = (String) sessionData.get(Skf2100Sc007CommonSharedService.SEARCH_INFO_ARRIVAL_DATE_TO_KEY);
		initDto.setTxtArrivalDateTo(arrivalDateTo);
		// 故障
		String faultChk = (String) sessionData.get(Skf2100Sc007CommonSharedService.SEARCH_INFO_FAULT_KEY);
		initDto.setChkFault(faultChk);
		if(CodeConstant.ROUTER_FAULT_FLAG_FAULT.equals(faultChk)){
			initDto.setHdnChkFaultSelect("true");
		}
		// 契約区分
		String contractKbn = (String) sessionData.get(Skf2100Sc007CommonSharedService.SEARCH_INFO_CONTRACT_KBN_KEY);
		initDto.setHdnContractKbnSelect(contractKbn);

		
		initDto = (Skf2100Sc007InitDto) skf2100Sc007SharedService.setDropDownSelect(initDto);



		return initDto;
	}



}
