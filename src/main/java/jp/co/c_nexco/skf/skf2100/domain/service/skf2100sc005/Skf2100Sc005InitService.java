/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc005;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2100Sc005.Skf2100Sc005GetListTableDataExpParameter;
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
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc005.Skf2100Sc005InitDto;

/**
 * Skf2100Sc005InitService モバイルルーター貸出管理簿初期表示Service
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2100Sc005InitService extends SkfServiceAbstract<Skf2100Sc005InitDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfDropDownUtils skfDropDownUtils;
	@Autowired
	private Skf2100Sc005SharedService skf2100Sc005SharedService;
	@Autowired
	private ApplicationScopeBean bean;

	private static final int YEAR = 12;

	@Value("${skf2100.skf2100_sc005.max_past_year}")
	private String maxPastYear;
	@Value("${skf2100.skf2100_sc005.max_search_count}")
	private String maxSearchCount;
	@Value("${skf2100.skf2100_sc005.max_row_count}")
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
	public Skf2100Sc005InitDto index(Skf2100Sc005InitDto initDto) throws Exception {

		initDto.setPageTitleKey(MessageIdConstant.SKF2100_SC005_TITLE);

		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, FunctionIdConstant.SKF2100_SC005);
		
		initDto = initItems(initDto);

		if (!NfwStringUtils.isEmpty(initDto.getPrePageId())
				&& FunctionIdConstant.SKF2100_SC006.equals(initDto.getPrePageId())) {

			@SuppressWarnings("unchecked")
			Map<String, Object> sessionData = (Map<String, Object>) bean
					.get(SessionCacheKeyConstant.MOBILEROUTER_LEDGER_SEARCH);
			if (sessionData != null) {
				initDto = initDispFromRegist(initDto, sessionData);
			}
			//セッション情報削除
			bean.remove(SessionCacheKeyConstant.MOBILEROUTER_LEDGER_SEARCH);
			
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
	private Skf2100Sc005InitDto initItems(Skf2100Sc005InitDto initDto) {

		/** 「年」ドロップダウンリスト */
		initDto.setYearDropDownList(null);
		/** 「月」ドロップダウンリスト */
		initDto.setMonthDropDownList(null);
		/** 「社宅区分」ドロップダウンリスト */
		initDto.setContractKbnDropDownList(null);
		
		/** 「検索結果一覧」リスト */
		initDto.setSearchDataList(null);
		
		/** 「締め処理」ラベル */
		initDto.setLabelShimeShori(null);
		/** 「POSITIVE連携」ラベル */
		initDto.setLabelPositiveRenkei(null);
		/** 「社員番号」テキスト */
		initDto.setTxtShainNo(null);
		/** 「社員名」テキスト */
		initDto.setTxtShainName(null);
		/** 「電話番号」テキスト */
		initDto.setTxtTel(null);
		/** 「通しNo」テキスト */
		initDto.setTxtRouterNo(null);
		/** 「契約終了日From」テキスト */
		initDto.setTxtContractEndDateFrom(null);
		/** 「契約終了日To」テキスト */
		initDto.setTxtContractEndDateTo(null);
		/** 「空きモバイルルーター」チェック */
		initDto.setChkAkiRouter(null);
		
		/**
		 * hidden 項目
		 */
		/** 選択された「年」ドロップダウンリストの選択 */
		initDto.setHdnYearSelect(null);
		/** 選択された「月」ドロップダウンリストの選択 */
		initDto.setHdnMonthSelect(null);
		/** 選択された 「契約区分」ドロップダウンリストの選択 */
		initDto.setHdnContractKbnSelect(null);
		/** 検索結果一覧の選択されたインデックス */
		initDto.setHdnSelIdx(null);
		/** 社宅管理台帳検索パラメータ */
		initDto.setSearchParam(null);
		initDto.setHdnChkAkiRouterSelect(null);


		//システム処理年月
		String systemDateNengetsu = skf2100Sc005SharedService.getSystemProcessNenGetsu();

		// 年月ドロップダウンリスト
		List<Map<String, Object>> yearDropDownList = createYearDropDownList(systemDateNengetsu);
		initDto.setYearDropDownList(yearDropDownList);
		initDto.setHdnYearSelect((String) yearDropDownList.get(0).get("value"));

		List<Map<String, Object>> monthDropDownList = createMonthDropDownList(systemDateNengetsu.substring(4, 6));
		initDto.setMonthDropDownList(monthDropDownList);
		initDto.setHdnMonthSelect(systemDateNengetsu.substring(4, 6));

		// 締め処理状態設定
		String selectNengetsu = initDto.getHdnYearSelect() + initDto.getHdnMonthSelect();
		Map<String, String> getsujiShoriJoukyouShoukaiMap = skf2100Sc005SharedService
				.getGetsujiShoriJoukyouShoukai(selectNengetsu);
		initDto.setLabelShimeShori(getsujiShoriJoukyouShoukaiMap.get(Skf2100Sc005SharedService.SHIME_SHORI_TXT_KEY));
		initDto.setLabelPositiveRenkei(
				getsujiShoriJoukyouShoukaiMap.get(Skf2100Sc005SharedService.POSITIVE_RENKEI_TXT_KEY));

		// 契約区分ドロップダウンリスト
		List<Map<String, Object>> contractKbnDropDownList = skfDropDownUtils
				.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_ROUTER_CONTRACT_KBN, "", true);
		initDto.setContractKbnDropDownList(contractKbnDropDownList);
		initDto.setHdnContractKbnSelect("");


		List<Map<String, Object>> serchDataList = new ArrayList<Map<String, Object>>();
		initDto.setSearchDataList(serchDataList);

		return initDto;
	}

	/**
	 * 「年」ドロップダウンリストを作成する。
	 * 
	 * @param systemProcessNenGetsu
	 *            システム処理年月
	 * @return 「年」ドロップダウンリスト
	 */
	private List<Map<String, Object>> createYearDropDownList(String systemProcessNenGetsu) {

		List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();

		if (!NfwStringUtils.isEmpty(systemProcessNenGetsu)) {
			int year = Integer.parseInt(systemProcessNenGetsu.substring(0, 4));
			int maxPastYyyy = Integer.parseInt(maxPastYear);

			for (int i = 0; i < maxPastYyyy; i++) {
				Map<String, Object> yearMap = new HashMap<String, Object>();

				String setVal = String.valueOf(year);
				year--;

				yearMap.put("value", setVal);
				yearMap.put("label", setVal);
				rtnList.add(yearMap);
			}
		}

		return rtnList;
	}

	/**
	 * 「月」ドロップダウンリストを作成する。
	 * 
	 * @return 「月」ドロップダウンリスト
	 */
	private List<Map<String, Object>> createMonthDropDownList(String selectMonth) {

		List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < YEAR; i++) {
			Map<String, Object> monthMap = new HashMap<String, Object>();

			String month = String.format("%02d", i + 1);
			monthMap.put("value", month);
			monthMap.put("label", month);
			if(Objects.equals(month, selectMonth)){
				monthMap.put("selected", true);
			}


			rtnList.add(monthMap);
		}

		return rtnList;
	}

	/**
	 * 「モバイルルーター貸出管理簿登録」画面から遷移した場合の初期表示処理
	 * 
	 * @param initDto
	 *            Skf2100Sc005InitDto
	 * @param sessionData
	 *            セッション情報
	 * @return Skf2100Sc005InitDto
	 */
	private Skf2100Sc005InitDto initDispFromRegist(Skf2100Sc005InitDto initDto,
			Map<String, Object> sessionData) {


		String shainNo = (String) sessionData.get(Skf2100Sc005CommonSharedService.SEARCH_INFO_SHAIN_NO_KEY);
		initDto.setTxtShainNo(shainNo);

		String shainName = (String) sessionData.get(Skf2100Sc005CommonSharedService.SEARCH_INFO_SHAIN_NAME_KEY);
		initDto.setTxtShainName(shainName);

		String tel = (String) sessionData.get(Skf2100Sc005CommonSharedService.SEARCH_INFO_TEL_KEY);
		initDto.setTxtTel(tel);
		
		String routerNo = (String) sessionData.get(Skf2100Sc005CommonSharedService.SEARCH_INFO_ROUTER_NO_KEY);
		initDto.setTxtRouterNo(routerNo);
		
		String endDateFrom = (String) sessionData.get(Skf2100Sc005CommonSharedService.SEARCH_INFO_CONTRACT_END_DATE_FROM_KEY);
		initDto.setTxtContractEndDateFrom(endDateFrom);
		
		String endDateTo = (String) sessionData.get(Skf2100Sc005CommonSharedService.SEARCH_INFO_CONTRACT_END_DATE_TO_KEY);
		initDto.setTxtContractEndDateTo(endDateTo);
		
		String akiRouter = (String) sessionData.get(Skf2100Sc005CommonSharedService.SEARCH_INFO_AKI_ROUTER_KEY);
		initDto.setChkAkiRouter(akiRouter);
		if("1".equals(akiRouter)){
			initDto.setHdnChkAkiRouterSelect("true");
		}else{
			initDto.setHdnChkAkiRouterSelect(null);
		}
		
		String contractKbn = (String) sessionData.get(Skf2100Sc005CommonSharedService.SEARCH_INFO_CONTRACT_KBN_KEY);
		initDto.setHdnContractKbnSelect(contractKbn);

		// 年月リスト
		String nengetsu = (String) sessionData.get(Skf2100Sc005CommonSharedService.SEARCH_INFO_NENGETSU_KEY);
		String year = "";
		String month = "";
		if (!NfwStringUtils.isEmpty(nengetsu)) {
			year = nengetsu.substring(0, 4);
			month = nengetsu.substring(4, 6);
		}

		initDto.setHdnYearSelect(year);
		initDto.setHdnMonthSelect(month);

		// 締め処理
		Map<String, String> getsujiShoriJoukyouShoukaiMap = skf2100Sc005SharedService
				.getGetsujiShoriJoukyouShoukai(initDto.getHdnYearSelect() + initDto.getHdnMonthSelect());
		initDto.setLabelShimeShori(getsujiShoriJoukyouShoukaiMap.get(Skf2100Sc005SharedService.SHIME_SHORI_TXT_KEY));
		initDto.setLabelPositiveRenkei(
				getsujiShoriJoukyouShoukaiMap.get(Skf2100Sc005SharedService.POSITIVE_RENKEI_TXT_KEY));
		
		initDto = (Skf2100Sc005InitDto) skf2100Sc005SharedService.setDropDownSelect(initDto);

		// 一覧情報取得
		Skf2100Sc005GetListTableDataExpParameter searchParam = skf2100Sc005SharedService
				.createSearchParam(initDto, sessionData, true);

		long searchCount = skf2100Sc005SharedService.getRouterListCount(searchParam);
		long maxCnt = Long.parseLong(maxSearchCount);

		initDto.setResultMessages(null);
		initDto.setSearchParam(null);
		initDto = (Skf2100Sc005InitDto) skf2100Sc005SharedService.setDropDownSelect(initDto);

		if (searchCount == 0) {
			// 0件の場合
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.W_SKF_1007);
			return initDto;

		} else if (searchCount > maxCnt) {
			// 最大件数超え
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1046, maxCnt);
			return initDto;

		} else {
			List<Map<String, Object>> searchDataList = skf2100Sc005SharedService.createSearchList(searchParam, initDto);
			initDto.setSearchDataList(searchDataList);
			initDto.setSearchParam(searchParam);
		}

		return initDto;
	}



}
