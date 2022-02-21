/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc007;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.poi_v3_8.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2100Rp001.Skf2100Rp001GetRouterLedgerDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2100Rp001.Skf2100Rp001GetRouterLedgerDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2100Sc007.Skf2100Sc007GetListTableDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2100Sc007.Skf2100Sc007GetListTableDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Rp003.Skf3030Rp003GetShatakuDaichoDiffDataInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc001.Skf3030Sc001GetGetsujishoriStatusExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc001.Skf3030Sc001GetNengetsuListExp;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2100Rp001.Skf2100Rp001GetRouterLedgerDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2100Sc007.Skf2100Sc007GetListTableDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Rp003.Skf3030Rp003GetShatakuDaichoDiffDataInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc001.Skf3030Sc001GetGetsujishoriStatusExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc001.Skf3030Sc001GetNengetsuListExpRepository;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100Sc007common.Skf2100Sc007CommonDto;

/**
 * Skf2100Sc007SharedService モバイルルーターマスタ一覧共通処理Service
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2100Sc007SharedService {

	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private Skf3030Sc001GetNengetsuListExpRepository skf3030Sc001GetNengetsuListExpRepository;
	@Autowired
	private Skf3030Sc001GetGetsujishoriStatusExpRepository skf3030Sc001GetGetsujishoriStatusExpRepository;

	@Autowired
	private Skf3030Rp003GetShatakuDaichoDiffDataInfoExpRepository skf3030Rp003GetShatakuDaichoDiffDataInfoExpRepository;
	@Autowired
	private Skf2100Sc007GetListTableDataExpRepository skf2100Sc007GetListTableDataExpRepository;
	@Autowired
	private Skf2100Rp001GetRouterLedgerDataExpRepository skf2100Rp001GetRouterLedgerDataExpRepository;

	public static final String BILLING_ACT_KBN_0 = "0";
	public static final String BILLING_ACT_KBN_1 = "1";
	public static final String BILLING_ACT_KBN_2 = "2";
	public static final String BILLING_ACT_KBN_0_LABEL = "未実行";
	public static final String BILLING_ACT_KBN_1_LABEL = "実行済";
	public static final String BILLING_ACT_KBN_2_LABEL = "解除中";

	public static final String POSITIVE_LINKDATA_CREATE_MIJIKKOU = "0";
	public static final String POSITIVE_LINKDATA_CREATE_JIKKOUZUMI = "1";
	public static final String POSITIVE_LINKDATA_CREATE_KAIJO = "2";
	public static final String POSITIVE_LINKDATA_COMMIT_MIJIKKOU = "0";
	public static final String POSITIVE_LINKDATA_COMMIT_JIKKOUZUMI = "1";

	public static final String POSITIVE_LINKDATA_KAIJO = "解除中";
	public static final String POSITIVE_LINKDATA_JIKKOUZUMI = "実行済";
	public static final String POSITIVE_LINKDATA_KAKUTEIZUMI = "確定済";
	public static final String POSITIVE_LINKDATA_MIJIKKOU = "未実行";

	public static final String SHIME_SHORI_TXT_KEY = "txtShimeShoriKey";
	public static final String POSITIVE_RENKEI_TXT_KEY = "txtPositiveRenkeiKey";

	public static final String CD_EXTERNAL_AGENCY = "ZZZZ";
	public static final String DISABLED = "true";
	public static final String ABLED = "false";
	public static final String CD_PREF_OTHER = "48";
	public static final String MUTUAL_USE_ARI = "有";

	public static final String STATUS_CD_OK = "0";
	public static final String STATUS_CD_NO_DATA = "1";
	public static final String STATUS_CD_DATA_ERR = "2";
	public static final String STATUS_CD_OUTPUT_ERR = "3";
	public static final String STATUS_CD_PARAM_ERR = "4";

	public static final String KAISHAKAN_SOKIN_NASI = "×";
	public static final String KAISHAKAN_SOKIN_SHIHARAI = "○";
	public static final String KAISHAKAN_SOKIN_SEIKYU = "◎";

	public static final String FONT_NAME_KEY = "fontName";
	public static final String FONT_COLOR_KEY = "fontColor";
	public static final String FONT_SIZE_KEY = "fontSize";
	public static final String FONT_PARAM_KEY = "font";
	public static final String FONT_BG_COLOR_KEY = "bgcolor";

	private static final int DATE_LEN = 8;


	/**
	 * 「年月」データリスト取得
	 * 
	 * @return 「年月」データリスト
	 */
	public List<Skf3030Sc001GetNengetsuListExp> getNengetsuList() {

		List<Skf3030Sc001GetNengetsuListExp> rtnData = skf3030Sc001GetNengetsuListExpRepository.getNengetsuList();

		return rtnData;
	}

	/**
	 * 月次処理ステータスデータ取得
	 * 
	 * @param nengetsu
	 *            対象年月
	 * @return 月次処理ステータスデータ
	 */
	public List<Skf3030Sc001GetGetsujishoriStatusExp> getGetsujishoriStatus(String nengetsu) {

		List<Skf3030Sc001GetGetsujishoriStatusExp> rtnData = skf3030Sc001GetGetsujishoriStatusExpRepository
				.getGetsujishoriStatus(nengetsu);

		return rtnData;
	}

	/**
	 * システムデータの年月を取得する。
	 * 
	 * @return システムデータ年月
	 */
	public String getSystemProcessNenGetsu() {

		String systemProcessNenGetsu = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu();

		return systemProcessNenGetsu;
	}


	
	/**
	 * 検索データ取得用のEntityを作成する。
	 * 
	 * @param inDto
	 *            Skf2100Sc007CommonDto
	 * @return 検索データ取得パラメータ
	 */
	public Skf2100Sc007GetListTableDataExpParameter createSearchParam(Skf2100Sc007CommonDto inDto) {

		Skf2100Sc007GetListTableDataExpParameter rtnParam = new Skf2100Sc007GetListTableDataExpParameter();

		if (!NfwStringUtils.isEmpty(inDto.getTxtRouterNo())) {
			rtnParam.setMobileRouterNo(Long.parseLong(inDto.getTxtRouterNo()));
		}else{
			rtnParam.setMobileRouterNo(null);
		}
		rtnParam.setTel(cnvEmptyStrToNull(inDto.getTxtTel()));
		rtnParam.setIccid(cnvEmptyStrToNull(inDto.getTxtIccid()));
		rtnParam.setImei(cnvEmptyStrToNull(inDto.getTxtImei()));
		rtnParam.setRouterContractKbn(cnvEmptyStrToNull(inDto.getHdnContractKbnSelect()));
		rtnParam.setArrivalDateFrom(cnvEmptyStrToNull(inDto.getTxtArrivalDateFrom()));
		rtnParam.setArrivalDateTo(cnvEmptyStrToNull(inDto.getTxtArrivalDateTo()));
		rtnParam.setContractEndDateFrom(cnvEmptyStrToNull(inDto.getTxtContractEndDateFrom()));
		rtnParam.setContractEndDateTo(cnvEmptyStrToNull(inDto.getTxtContractEndDateTo()));
		if("true".equals(inDto.getHdnChkFaultSelect())){
			rtnParam.setFaultFlag(CodeConstant.ROUTER_FAULT_FLAG_FAULT);
		}else{
			rtnParam.setFaultFlag(CodeConstant.ROUTER_FAULT_FLAG_NORMAL);
		}
		
		return rtnParam;
	}

	/**
	 * モバイルルーターマスタの検索件数を取得する。
	 * 
	 * @param inParam
	 *            検索用パラメータ
	 * @return 検索件数
	 */
	public long getRouterListCount(Skf2100Sc007GetListTableDataExpParameter inParam) {

		long rtnCnt = 0;

		rtnCnt = skf2100Sc007GetListTableDataExpRepository.getListCount(inParam);

		return rtnCnt;
	}

	/**
	 * モバイルルーターマスタの検索結果情報を取得する。
	 * 
	 * @param inParam
	 *            検索パラメータ
	 * @return 検索結果情報
	 */
	public List<Skf2100Sc007GetListTableDataExp> getRouterLedgerListData(
			Skf2100Sc007GetListTableDataExpParameter inParam) {
		
		List<Skf2100Sc007GetListTableDataExp> rtnData =
				skf2100Sc007GetListTableDataExpRepository.getListTableData(inParam);

		return rtnData;
	}

	/**
	 * 処理対象年月の情報を取得する。
	 *
	 * @param nengetsu
	 *            選択されている年月
	 * @return 処理対象年月の情報map
	 */
	public Map<String, String> getGetsujiShoriJoukyouShoukai(String nengetsu) {

		Map<String, String> rtnMap = new HashMap<String, String>();
		List<Skf3030Sc001GetGetsujishoriStatusExp> monthDataList = getGetsujishoriStatus(nengetsu);

		if (monthDataList.size() > 0) {
			String shimeShoriTxt = getBillingActKbnTxt(monthDataList.get(0).getBillingActKbn());
			rtnMap.put(SHIME_SHORI_TXT_KEY, shimeShoriTxt);

			String createKbn = monthDataList.get(0).getLinkdataCreateKbn();
			String commitKbn = monthDataList.get(0).getLinkdataCommitKbn();
			String positiveRenkeiTxt = getPositiveRenkeiValue(createKbn, commitKbn);
			rtnMap.put(POSITIVE_RENKEI_TXT_KEY, positiveRenkeiTxt);

		} else {
			rtnMap.put(SHIME_SHORI_TXT_KEY, "");
			rtnMap.put(POSITIVE_RENKEI_TXT_KEY, "");
		}

		return rtnMap;
	}

	/**
	 * 締め処理区分を取得する。
	 * 
	 * @param billingKbnLabel
	 *            画面上の締め処理
	 * @return 締め処理区分
	 */
	public String getBillingActKbn(String billingKbnLabel) {

		String rtnVal = "";

		if (NfwStringUtils.isEmpty(billingKbnLabel)) {
			return rtnVal;
		}

		switch (billingKbnLabel) {
		case Skf2100Sc007SharedService.BILLING_ACT_KBN_0_LABEL:
			rtnVal = Skf2100Sc007SharedService.BILLING_ACT_KBN_0;
			break;
		case Skf2100Sc007SharedService.BILLING_ACT_KBN_1_LABEL:
			rtnVal = Skf2100Sc007SharedService.BILLING_ACT_KBN_1;
			break;
		case Skf2100Sc007SharedService.BILLING_ACT_KBN_2_LABEL:
			rtnVal = Skf2100Sc007SharedService.BILLING_ACT_KBN_2;
			break;
		default:
			break;
		}

		return rtnVal;
	}

	/**
	 * POSITIVE連携作成実行区分を取得する。
	 * 
	 * @param positiveRenakeiLabel
	 *            画面上のPOSITIVE連携
	 * @return POSITIVE連携作成実行区分
	 */
	public String getPositiveRenkeiKbn(String positiveRenakeiLabel) {

		String rtnVal = "";

		if (NfwStringUtils.isEmpty(positiveRenakeiLabel)) {
			return rtnVal;
		}

		switch (positiveRenakeiLabel) {
		case Skf2100Sc007SharedService.POSITIVE_LINKDATA_MIJIKKOU:
			rtnVal = Skf2100Sc007SharedService.POSITIVE_LINKDATA_CREATE_MIJIKKOU;
			break;
		case Skf2100Sc007SharedService.POSITIVE_LINKDATA_JIKKOUZUMI:
			rtnVal = Skf2100Sc007SharedService.POSITIVE_LINKDATA_CREATE_JIKKOUZUMI;
			break;
		case Skf2100Sc007SharedService.POSITIVE_LINKDATA_KAIJO:
			rtnVal = Skf2100Sc007SharedService.POSITIVE_LINKDATA_CREATE_KAIJO;
			break;
		case Skf2100Sc007SharedService.POSITIVE_LINKDATA_KAKUTEIZUMI:
			rtnVal = Skf2100Sc007SharedService.POSITIVE_LINKDATA_CREATE_JIKKOUZUMI;
			break;
		default:
			break;
		}

		return rtnVal;
	}

	/**
	 * 締め処理テキスト設定値を取得する。
	 * 
	 * @param billingKbn
	 *            締め処理区分
	 * @return テキスト設定値
	 */
	public String getBillingActKbnTxt(String billingKbn) {

		String rtnVal = "";

		if (NfwStringUtils.isEmpty(billingKbn)) {
			return rtnVal;
		}

		switch (billingKbn) {
		case Skf2100Sc007SharedService.BILLING_ACT_KBN_0:
			rtnVal = Skf2100Sc007SharedService.BILLING_ACT_KBN_0_LABEL;
			break;
		case Skf2100Sc007SharedService.BILLING_ACT_KBN_1:
			rtnVal = Skf2100Sc007SharedService.BILLING_ACT_KBN_1_LABEL;
			break;
		case Skf2100Sc007SharedService.BILLING_ACT_KBN_2:
			rtnVal = Skf2100Sc007SharedService.BILLING_ACT_KBN_2_LABEL;
			break;
		default:
			break;
		}

		return rtnVal;
	}

	/**
	 * POSITIVE連携テキスト設定値を取得する。
	 * 
	 * @param createKbn
	 *            POSITIVE連携作成実行区分
	 * @param commitKbn
	 *            POSITIVE連携確定実行区分
	 * @return テキスト設定値
	 */
	public String getPositiveRenkeiValue(String createKbn, String commitKbn) {

		String rtnVal = "";

		if (NfwStringUtils.isEmpty(createKbn) || NfwStringUtils.isEmpty(commitKbn)) {
			return rtnVal;
		}

		if (Skf2100Sc007SharedService.POSITIVE_LINKDATA_CREATE_KAIJO.equals(createKbn)
				&& Skf2100Sc007SharedService.POSITIVE_LINKDATA_COMMIT_MIJIKKOU.equals(commitKbn)) {
			rtnVal = Skf2100Sc007SharedService.POSITIVE_LINKDATA_KAIJO;

		} else if (Skf2100Sc007SharedService.POSITIVE_LINKDATA_CREATE_JIKKOUZUMI.equals(createKbn)
				&& Skf2100Sc007SharedService.POSITIVE_LINKDATA_COMMIT_MIJIKKOU.equals(commitKbn)) {
			rtnVal = Skf2100Sc007SharedService.POSITIVE_LINKDATA_JIKKOUZUMI;

		} else if (Skf2100Sc007SharedService.POSITIVE_LINKDATA_CREATE_JIKKOUZUMI.equals(createKbn)
				&& Skf2100Sc007SharedService.POSITIVE_LINKDATA_COMMIT_JIKKOUZUMI.equals(commitKbn)) {
			rtnVal = Skf2100Sc007SharedService.POSITIVE_LINKDATA_KAKUTEIZUMI;

		} else {
			rtnVal = Skf2100Sc007SharedService.POSITIVE_LINKDATA_MIJIKKOU;
		}

		return rtnVal;
	}

	/**
	 * 「検索結果一覧」リストを作成する。
	 * 
	 * @param inParam
	 *            検索用パラメータ
	 * @return 「検索結果一覧」リスト
	 */
	public List<Map<String, Object>> createSearchList(Skf2100Sc007GetListTableDataExpParameter inParam,
			Skf2100Sc007CommonDto inDto) {

		List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();
		List<Skf2100Sc007GetListTableDataExp> routerInfoList = getRouterLedgerListData(inParam);

		if (routerInfoList.size() == 0) {
			return rtnList;
		}
		
		List<Map<String, Object>> contractKbnList = inDto.getContractKbnDropDownList();

		for (Skf2100Sc007GetListTableDataExp routerMInfo : routerInfoList) {
			Map<String, Object> targetMap = new HashMap<String, Object>();


			// 通しNo
			targetMap.put(Skf2100Sc007CommonSharedService.SEARCH_COL_ROUTER_NO_KEY,
					routerMInfo.getMobileRouterNo());
			// 電話番号
			targetMap.put(Skf2100Sc007CommonSharedService.SEARCH_COL_TEL_KEY,
					routerMInfo.getTel());
			// ICCID
			targetMap.put(Skf2100Sc007CommonSharedService.SEARCH_COL_ICCID_KEY,
					routerMInfo.getIccid());
			// IMEI
			targetMap.put(Skf2100Sc007CommonSharedService.SEARCH_COL_IMEI_KEY,
					routerMInfo.getImei());
			// ルーター入荷日
			String entityArrivalDate = routerMInfo.getRouterArrivalDate();
			String arrivalDate = setDispDateStr(entityArrivalDate);
			targetMap.put(Skf2100Sc007CommonSharedService.SEARCH_COL_ARRIVAL_DATE_KEY, arrivalDate);
			// 契約終了日
			String entityEndDate = routerMInfo.getRouterContractEndDate();
			String endDate = setDispDateStr(entityEndDate);
			targetMap.put(Skf2100Sc007CommonSharedService.SEARCH_COL_CONTRACT_END_DATE_KEY, endDate);
			// 契約区分
			String contractKbn = getTargetDropDownLabel(routerMInfo.getRouterContractKbn(), contractKbnList);
			targetMap.put(Skf2100Sc007CommonSharedService.SEARCH_COL_CONTARCT_KBN_KEY, contractKbn);
			// 故障フラグ
			String faultFlag = routerMInfo.getFaultFlag();
			if(CodeConstant.ROUTER_FAULT_FLAG_FAULT.equals(faultFlag)){
				targetMap.put(Skf2100Sc007CommonSharedService.SEARCH_COL_FAULT_KEY, CodeConstant.ROUTER_FAULT_STRING);
			}else{
				targetMap.put(Skf2100Sc007CommonSharedService.SEARCH_COL_FAULT_KEY, CodeConstant.DOUBLE_QUOTATION);
			}
			// 詳細ボタン列
			targetMap.put(Skf2100Sc007CommonSharedService.SEARCH_COL_DETAIL_KEY, "");
			
			rtnList.add(targetMap);
		}

		return rtnList;
	}
	
	/**
	 * 画面表示用日付文字列の変換。
	 * 
	 * @param dateStr
	 *            日付文字(YYYYMMDD)
	 * @return 日付文字列(YYYY/MM/DD) 
	 */
	private String setDispDateStr(String dateStr){
		String rtnStr = CodeConstant.DOUBLE_QUOTATION;
		
		if (!NfwStringUtils.isEmpty(dateStr) && dateStr.length() == DATE_LEN) {
			rtnStr = dateStr.substring(0, 4) + "/" + dateStr.substring(4, 6) + "/"
					+ dateStr.substring(6);
		}
		
		return rtnStr;
	}

	/**
	 * ドロップダウンリストから対象のラベルを取得する。
	 * 
	 * @param val
	 *            指定の値
	 * @param dropDownList
	 *            対象のドロップダウンリスト
	 * @return 対象のラベル
	 */
	private String getTargetDropDownLabel(String val, List<Map<String, Object>> dropDownList) {

		String rtn = "";

		for (int i = 0; i < dropDownList.size(); i++) {
			Map<String, Object> targetMap = dropDownList.get(i);

			if (Objects.equals(val, targetMap.get("value"))) {
				rtn = (String) targetMap.get("label");
				break;
			}
		}

		return rtn;
	}

	/**
	 * ドロップダウンリストの選択状態を設定する。
	 * 
	 * @param inDto
	 *            Skf2100Sc007CommonDto
	 * @return Skf2100Sc007CommonDto
	 */
	public Skf2100Sc007CommonDto setDropDownSelect(Skf2100Sc007CommonDto inDto) {

		String contractKbnSelect = inDto.getHdnContractKbnSelect();
		List<Map<String, Object>> contractKbnList = inDto.getContractKbnDropDownList();
		contractKbnList = selectDropDown(contractKbnSelect, contractKbnList);
		inDto.setContractKbnDropDownList(contractKbnList);

		return inDto;
	}

	/**
	 * 対象のドロップダウンを選択する。
	 * 
	 * @param val
	 *            選択された値
	 * @param dropDownList
	 *            対象のドロップダウンリスト
	 * @return 対象のドロップダウンリスト
	 */
	private List<Map<String, Object>> selectDropDown(String val, List<Map<String, Object>> dropDownList) {

		for (int i = 0; i < dropDownList.size(); i++) {
			Map<String, Object> targetMap = dropDownList.get(i);

			if (Objects.equals(val, targetMap.get("value"))) {
				targetMap.put("selected", true);
				dropDownList.set(i, targetMap);

			} else {
				targetMap.put("selected", false);
				dropDownList.set(i, targetMap);
			}
		}

		return dropDownList;
	}

	/**
	 * 文字列がnullの場合、空文字に変換する。
	 * 
	 * @param inStr
	 *            対象文字列
	 * @return 変換後の文字列
	 */
	public String cnvEmptyStrToNull(String inStr) {

		if (inStr != null) {
			return inStr;
		}

		return "";
	}

	/**
	 * モバイルルーター貸出管理簿出力データを取得
	 * 
	 * @param yearMonth
	 *            システム年月
	 * @param tougetuFlg
	 *            処理当月フラグ
	 * @return モバイルルーター貸出管理簿出力データ
	 */
	public List<Skf2100Rp001GetRouterLedgerDataExp> getShatakuDaichoInfo(String yearMonth,boolean tougetuFlg) {

		Skf2100Rp001GetRouterLedgerDataExpParameter param = new Skf2100Rp001GetRouterLedgerDataExpParameter();
		param.setYearMonth(yearMonth);

		if (tougetuFlg) {
			param.setTougetuFlg("1");
		} else {
			param.setTougetuFlg("0");
		}

		List<Skf2100Rp001GetRouterLedgerDataExp> rtnData = skf2100Rp001GetRouterLedgerDataExpRepository
				.getRouterLedgerData(param);

		return rtnData;
	}

	/**
	 * 出力判定情報データ取得
	 * 
	 * @param yearMonth
	 *            対象年月
	 * @return 出力判定情報データ
	 */
	public List<Skf3030Rp003GetShatakuDaichoDiffDataInfoExp> getShatakuDaichoDiffDataInfo(String yearMonth) {

		List<Skf3030Rp003GetShatakuDaichoDiffDataInfoExp> rtnData = skf3030Rp003GetShatakuDaichoDiffDataInfoExpRepository
				.getShatakuDaichoDiffDataInfo(yearMonth);

		return rtnData;
	}

	/**
	 * Excelのシリアル日付に変換する。
	 * 
	 * @param inDate
	 *            対象の日付
	 * @return 変換後の日付
	 */
	public String cnvExcelDate(String inDate) {

		Date date = skfDateFormatUtils.formatStringToDate(inDate);
		double time = DateUtil.getExcelDate(date);
		String outDate = String.valueOf(time);

		return outDate;
	}

	/**
	 * 対象終日を取得する。
	 * 
	 * @param inDate
	 *            基準日
	 * @return 対象終日
	 */
	public String getLastDay(String inDate) {

		SimpleDateFormat sdFormat = new SimpleDateFormat(SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT);
		Date forChangeDate;
		try {
			forChangeDate = sdFormat.parse(inDate);
		} catch (ParseException e) {
			return "";
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(forChangeDate);

		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date changedDate = calendar.getTime();

		String outDate = sdFormat.format(changedDate);

		return outDate;
	}

	/**
	 * 日数計算
	 * 
	 * @param startDate
	 *            開始日
	 * @param endDate
	 *            終了日
	 * @param firstDay
	 *            対象初日
	 * @param lastDay
	 *            対象終日
	 * @return 日数
	 */
	public String calcDayNum(String startDate, String endDate, String firstDay, String lastDay) {

		String rtnVal = "0";
		int diffDayCnt = 0;

		if (NfwStringUtils.isEmpty(startDate)) {
			return rtnVal;

		} else {
			int numStartDate = Integer.parseInt(startDate);
			int numFirstDate = Integer.parseInt(firstDay);
			int numLastDate = Integer.parseInt(lastDay);

			if (NfwStringUtils.isEmpty(endDate)) {
				if (numStartDate < numFirstDate) {
					diffDayCnt = numLastDate - numFirstDate;
					rtnVal = String.valueOf(diffDayCnt + 1);

				} else if (numFirstDate <= numStartDate && numStartDate <= numLastDate) {
					diffDayCnt = numLastDate - numStartDate;
					rtnVal = String.valueOf(diffDayCnt + 1);

				} else {
					return rtnVal;
				}

			} else {
				int numEndDate = Integer.parseInt(endDate);

				if (numStartDate > numEndDate) {
					return rtnVal;

				} else if (numStartDate < numFirstDate && numFirstDate <= numEndDate && numEndDate <= numLastDate) {
					diffDayCnt = numEndDate - numFirstDate;

				} else if (numFirstDate <= numStartDate && numStartDate <= numLastDate && numFirstDate <= numEndDate
						&& numEndDate <= numLastDate) {
					diffDayCnt = numEndDate - numStartDate;

				} else if (numFirstDate <= numStartDate && numStartDate <= numLastDate && numLastDate < numEndDate) {
					diffDayCnt = numLastDate - numStartDate;

				} else if (numLastDate < numStartDate && numLastDate < numEndDate) {
					return rtnVal;

				} else if (numStartDate < numFirstDate && numLastDate < numEndDate) {
					diffDayCnt = numLastDate - numFirstDate;

				} else {
					return rtnVal;
				}
			}
		}
		rtnVal = String.valueOf(diffDayCnt + 1);

		return rtnVal;
	}

	public Map<String, String> getBihinLentStsMap() {
		Map<String, String> rtnMap = new HashMap<String, String>();

		rtnMap.put("1", "×");
		rtnMap.put("2", "○");
		rtnMap.put("3", "1");
		rtnMap.put("4", "□");
		rtnMap.put("5", "△");

		return rtnMap;
	}

}
