/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3030.domain.service.skf3030sc001;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc001.Skf3030Sc001GetShatakuKanriDaichoInfoExpParameter;
import jp.co.c_nexco.nfw.common.bean.ApplicationScopeBean;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3030.domain.dto.skf3030sc001.Skf3030Sc001InitDto;
import jp.co.c_nexco.skf.skf3030.domain.service.common.Skf303010CommonSharedService;

/**
 * Skf3030Sc001InitService 社宅管理台帳初期表示Service
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf3030Sc001InitService extends BaseServiceAbstract<Skf3030Sc001InitDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfDropDownUtils skfDropDownUtils;
	@Autowired
	private Skf3030Sc001SharedService skf3030Sc001SharedService;
	@Autowired
	private ApplicationScopeBean bean;

	private static final int YEAR = 12;

	@Value("${skf3030.skf3030_sc001.max_past_year}")
	private String maxPastYear;
	@Value("${skf3030.skf3030_sc001.max_search_count}")
	private String maxSearchCount;
	@Value("${skf3030.skf3030_sc001.max_row_count}")
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
	public Skf3030Sc001InitDto index(Skf3030Sc001InitDto initDto) throws Exception {

		initDto.setPageTitleKey(MessageIdConstant.SKF3030_SC001_TITLE);

		skfOperationLogUtils.setAccessLog("社宅管理台帳初期表示処理開始", CodeConstant.C001, initDto.getPageId());
		
		initDto = initItems(initDto);

		if (!NfwStringUtils.isEmpty(initDto.getPrePageId())
				&& (FunctionIdConstant.SKF3030_SC001.equals(initDto.getPrePageId())
				|| FunctionIdConstant.SKF3030_SC002.equals(initDto.getPrePageId()))) {

			@SuppressWarnings("unchecked")
			Map<String, Object> sessionData = (Map<String, Object>) bean
					.get(SessionCacheKeyConstant.SHATAKUKANRI_DAICHO_SEARCH);
			if (sessionData != null) {
				initDto = initDispFromNyutaikyoRegist(initDto, sessionData);
			}
		}
		
		return initDto;
	}

	/**
	 * 画面項目の初期設定。
	 * 
	 * @param initDto
	 *            Skf3030Sc001InitDto
	 * @return Skf3030Sc001InitDto
	 */
	private Skf3030Sc001InitDto initItems(Skf3030Sc001InitDto initDto) {

		/** 管理会社ドロップダウンリスト */
		initDto.setCompanyAgencyDropDownList(null);
		/** 管理機関ドロップダウンリスト */
		initDto.setAgencyDropDownList(null);
		/** 「年」ドロップダウンリスト */
		initDto.setYearDropDownList(null);
		/** 「月」ドロップダウンリスト */
		initDto.setMonthDropDownList(null);
		/** 「社宅区分」ドロップダウンリスト */
		initDto.setShatakuKbnDropDownList(null);
		/** 「相互利用」ドロップダウンリスト */
		initDto.setMutualuseDropDownList(null);
		/** 「検索結果一覧」リスト */
		initDto.setSearchDataList(null);
		
		/** 「締め処理」ラベル */
		initDto.setLabelShimeShori(null);
		/** 「POSITIVE連携」ラベル */
		initDto.setLabelPositiveRenkei(null);
		/** 「社員番号」テキスト */
		initDto.setTxtShainNo(null);
		/** 「社宅名」テキスト */
		initDto.setTxtShatakuName(null);
		/** 「社員名」テキスト */
		initDto.setTxtShainName(null);
		
		/**
		 * hidden 項目
		 */
		/** 選択された管理会社ドロップダウンリスト */
		initDto.setHdnCompanyAgencySelect(null);
		/** 選択された管理機関ドロップダウンリストの選択 */
		initDto.setHdnAgencySelect(null);
		/** 選択された「年」ドロップダウンリストの選択 */
		initDto.setHdnYearSelect(null);
		/** 選択された「月」ドロップダウンリストの選択 */
		initDto.setHdnMonthSelect(null);
		/** 選択された 「社宅区分」ドロップダウンリストの選択 */
		initDto.setHdnShatakuKbnSelect(null);
		/** 選択された「相互利用」ドロップダウンリストの選択 */
		initDto.setHdnMutualuseSelect(null);
		/** 管理機関ドロップダウン非活性 */
		initDto.setHdnAgencyDisabled(null);
		/** 検索結果一覧の選択されたインデックス */
		initDto.setHdnSelIdx(null);
		/** 社宅管理台帳検索パラメータ */
		initDto.setSearchParam(null);

		List<Map<String, Object>> companyList = skfDropDownUtils.getDdlCompanyByCd("", true);
		initDto.setCompanyAgencyDropDownList(companyList);
		if (companyList.size() > 0) {
			initDto.setHdnCompanyAgencySelect((String) companyList.get(0).get("value"));
		} else {
			initDto.setHdnCompanyAgencySelect("");
		}

		List<Map<String, Object>> agencyList = new ArrayList<Map<String, Object>>();
		initDto.setAgencyDropDownList(agencyList);
		initDto.setHdnAgencySelect("");
		initDto.setHdnAgencyDisabled(Skf3030Sc001SharedService.ABLED);

		String systemDateNengetsu = skf3030Sc001SharedService.getSystemProcessNenGetsu();

		List<Map<String, Object>> yearDropDownList = createYearDropDownList(systemDateNengetsu);
		initDto.setYearDropDownList(yearDropDownList);
		initDto.setHdnYearSelect((String) yearDropDownList.get(0).get("value"));

		List<Map<String, Object>> monthDropDownList = createMonthDropDownList(systemDateNengetsu.substring(4, 6));
		initDto.setMonthDropDownList(monthDropDownList);
		initDto.setHdnMonthSelect(systemDateNengetsu.substring(4, 6));

		String selectNengetsu = initDto.getHdnYearSelect() + initDto.getHdnMonthSelect();
		Map<String, String> getsujiShoriJoukyouShoukaiMap = skf3030Sc001SharedService
				.getGetsujiShoriJoukyouShoukai(selectNengetsu);
		initDto.setLabelShimeShori(getsujiShoriJoukyouShoukaiMap.get(Skf3030Sc001SharedService.SHIME_SHORI_TXT_KEY));
		initDto.setLabelPositiveRenkei(
				getsujiShoriJoukyouShoukaiMap.get(Skf3030Sc001SharedService.POSITIVE_RENKEI_TXT_KEY));

		List<Map<String, Object>> shatakuKbnDropDownList = skfDropDownUtils
				.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_SHATAKU_KBN, "", true);
		initDto.setShatakuKbnDropDownList(shatakuKbnDropDownList);
		initDto.setHdnShatakuKbnSelect("");

		List<Map<String, Object>> mutualuseDropDownList = skfDropDownUtils
				.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_MUTUALUSE_KBN, "", true);
		initDto.setMutualuseDropDownList(mutualuseDropDownList);
		initDto.setHdnMutualuseSelect("");

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
	 * 「入退去情報登録」画面から遷移した場合の初期表示処理
	 * 
	 * @param initDto
	 *            Skf3030Sc001InitDto
	 * @param sessionData
	 *            セッション情報
	 * @return Skf3030Sc001InitDto
	 */
	private Skf3030Sc001InitDto initDispFromNyutaikyoRegist(Skf3030Sc001InitDto initDto,
			Map<String, Object> sessionData) {

		String kanriKaisha = (String) sessionData.get(Skf303010CommonSharedService.NYUTAIKYO_INFO_KANRI_KAISHA_KEY);
		List<Map<String, Object>> companyList = skfDropDownUtils.getDdlCompanyByCd(kanriKaisha, true);
		initDto.setCompanyAgencyDropDownList(companyList);
		initDto.setHdnCompanyAgencySelect(kanriKaisha);
		initDto.setHdnAgencyDisabled(Skf3030Sc001SharedService.ABLED);

		if (!NfwStringUtils.isEmpty(kanriKaisha)) {
			if (Skf3030Sc001SharedService.CD_EXTERNAL_AGENCY.equals(kanriKaisha)) {
				initDto.setHdnAgencyDisabled(Skf3030Sc001SharedService.DISABLED);

			} else {
				String agency = (String) sessionData.get(Skf303010CommonSharedService.NYUTAIKYO_INFO_AGENCY_KEY);
				List<Map<String, Object>> agencyList = skfDropDownUtils.getDdlAgencyByCd(kanriKaisha, agency, true);
				initDto.setAgencyDropDownList(agencyList);
				initDto.setHdnAgencySelect(agency);
			}
		}

		String shainNo = (String) sessionData.get(Skf303010CommonSharedService.NYUTAIKYO_INFO_SHAIN_NO_KEY);
		initDto.setTxtShainNo(shainNo);

		String shainName = (String) sessionData.get(Skf303010CommonSharedService.NYUTAIKYO_INFO_SHAIN_NAME_KEY);
		initDto.setTxtShainName(shainName);

		String shatakName = (String) sessionData.get(Skf303010CommonSharedService.NYUTAIKYO_INFO_SHATAK_NAME_KEY);
		initDto.setTxtShatakuName(shatakName);

		String shatakKbn = (String) sessionData.get(Skf303010CommonSharedService.NYUTAIKYO_INFO_SHATAK_KBN_KEY);
		initDto.setHdnShatakuKbnSelect(shatakKbn);

		String sogoriyo = (String) sessionData.get(Skf303010CommonSharedService.NYUTAIKYO_INFO_SOGORIYO_KEY);
		initDto.setHdnMutualuseSelect(sogoriyo);

		String nengetsu = (String) sessionData.get(Skf303010CommonSharedService.NYUTAIKYO_INFO_NENGETSU_KEY);
		String year = "";
		String month = "";
		if (!NfwStringUtils.isEmpty(nengetsu)) {
			year = nengetsu.substring(0, 4);
			month = nengetsu.substring(4, 6);
		}

		initDto.setHdnYearSelect(year);
		initDto.setHdnMonthSelect(month);

		String shimeShori = (String) sessionData.get(Skf303010CommonSharedService.NYUTAIKYO_INFO_SHIME_SHORI_KEY);
		initDto.setLabelShimeShori(skf3030Sc001SharedService.getBillingActKbnTxt(shimeShori));

		String positiveRenkei = (String) sessionData
				.get(Skf303010CommonSharedService.NYUTAIKYO_INFO_POSITIVE_RENKEI_KEY);
		initDto.setLabelPositiveRenkei(getPositiveRenkeiKbnLabel(positiveRenkei));
		
		initDto = (Skf3030Sc001InitDto) skf3030Sc001SharedService.setDropDownSelect(initDto);

		// TODO 当月更新は必要か？

		Skf3030Sc001GetShatakuKanriDaichoInfoExpParameter searchParam = skf3030Sc001SharedService
				.createSearchParam(initDto, sessionData, true);

		long searchCount = skf3030Sc001SharedService.getShatakuKanriDaichoCount(searchParam);
		long maxCnt = Long.parseLong(maxSearchCount);

		initDto.setResultMessages(null);
		initDto.setSearchParam(null);
		initDto = (Skf3030Sc001InitDto) skf3030Sc001SharedService.setDropDownSelect(initDto);

		if (searchCount == 0) {
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.W_SKF_1007);
			return initDto;

		} else if (searchCount > maxCnt) {
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1046, maxCnt);
			return initDto;

		} else {
			List<Map<String, Object>> searchDataList = skf3030Sc001SharedService.createSearchList(searchParam, initDto);
			initDto.setSearchDataList(searchDataList);
			initDto.setSearchParam(searchParam);
		}

		return initDto;
	}

	/**
	 * 表示用のPOSITIVE連携のラベルを取得する。
	 * 
	 * @param kbn
	 *            POSITIVE連携作成実行区分
	 * @return POSITIVE連携のラベル
	 */
	public String getPositiveRenkeiKbnLabel(String kbn) {

		String rtn = "";

		if(Objects.equals(kbn, null)){
			return rtn;
		}
		
		switch (kbn) {
		case Skf3030Sc001SharedService.POSITIVE_LINKDATA_CREATE_MIJIKKOU:
			rtn = Skf3030Sc001SharedService.POSITIVE_LINKDATA_MIJIKKOU;
			break;
		case Skf3030Sc001SharedService.POSITIVE_LINKDATA_CREATE_JIKKOUZUMI:
			rtn = Skf3030Sc001SharedService.POSITIVE_LINKDATA_JIKKOUZUMI;
			break;
		case Skf3030Sc001SharedService.POSITIVE_LINKDATA_CREATE_KAIJO:
			rtn = Skf3030Sc001SharedService.POSITIVE_LINKDATA_KAIJO;
			break;
		default:
			break;
		}

		return rtn;
	}

}
