/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc008;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2100Sc008.Skf2100Sc008GetSameImeiDataCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2100MMobileRouterWithBLOBs;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2100Sc008.Skf2100Sc008GetSameImeiDataCountExpRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfRouterInfoUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100Sc008common.Skf2100Sc008CommonDto;

/**
 * Skf2100Sc008SharedService モバイルルーターマスタ登録共通処理Service
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2100Sc008SharedService {

	@Autowired
	private SkfRouterInfoUtils skfRouterInfoUtils;
	@Autowired
	private Skf2100Sc008GetSameImeiDataCountExpRepository skf2100Sc008GetSameImeiDataCountExpRepository;
	
	
	private static final int DATE_LEN = 8;
	// 電話番号チェック正規表現
	private static final String TELNO_CHECK_REG = "^[0-9-]*$";
	// 半角数字チェック正規表現
	private static final String NO_CHECK_REG = "^[0-9]*$";
	// 半角文字チェック正規表現
	private static final String HANKAKU_CHECK_REG = "^[!-~]*$";

	
	/**
	 * 画面表示用日付文字列の変換。
	 * 
	 * @param dateStr
	 *            日付文字(YYYYMMDD)
	 * @return 日付文字列(YYYY/MM/DD) 
	 */
	public String setDispDateStr(String dateStr){
		String rtnStr = CodeConstant.DOUBLE_QUOTATION;
		
		if (!NfwStringUtils.isEmpty(dateStr) && dateStr.length() == DATE_LEN) {
			rtnStr = dateStr.substring(0, 4) + "/" + dateStr.substring(4, 6) + "/"
					+ dateStr.substring(6);
		}
		
		return rtnStr;
	}
	
	/**
	 * 日付文字列
	 * @param sDay
	 * @return
	 */
	public String getDateText(String sDay){
		if(SkfCheckUtils.isNullOrEmpty(sDay)){
			return CodeConstant.DOUBLE_QUOTATION;
		}
		
		return sDay.replace(CodeConstant.SLASH, CodeConstant.DOUBLE_QUOTATION).replace(CodeConstant.UNDER_SCORE, CodeConstant.DOUBLE_QUOTATION).trim();
	}
	
	/**
	 * 登録用文字列の取得
	 * 空の場合NULLを返却する
	 * @param ob
	 * @return
	 */
	public String getToRegistString(String str){
		if(SkfCheckUtils.isNullOrEmpty(str)){
			return null;
		}else{
			return str;
		}
	}
	
	/**
	 * 日付文字列
	 * @param sDay
	 * @return
	 */
	public String getRegistDateText(String sDay){
		if(SkfCheckUtils.isNullOrEmpty(sDay)){
			return null;
		}
		
		return sDay.replace(CodeConstant.SLASH, CodeConstant.DOUBLE_QUOTATION).replace(CodeConstant.UNDER_SCORE, CodeConstant.DOUBLE_QUOTATION).trim();
	}
	
	
	/**
	 * @param comDto
	 * @return
	 * @throws ParseException
	 */
	public boolean validateInput(Skf2100Sc008CommonDto comDto) throws Exception{
		
		// DefaultColorに戻す
		errReset(comDto);
		//エラーメッセージ
		StringBuilder errMsg = new StringBuilder();

		// 必須チェック
		isErrEmpty(comDto,errMsg);

		//バイト数チェック
		isErrByteCount(comDto,errMsg);
		
		//形式チェック
		isErrType(comDto,errMsg);

		//整合性チェック
		if (0 == errMsg.toString().length()){
			//ここまでエラーなしの場合
			isErrDateFormat(comDto,errMsg);
		}
		
		//エラーが存在する場合
		if (0 < errMsg.toString().length()){
			LogUtils.infoByMsg("validateInput, " + errMsg.toString());
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * エラー変数初期化
	 * @param comDto
	 */
	public void errReset(Skf2100Sc008CommonDto comDto){
		
		/** エラー変数初期化 */
		comDto.setRouterNoErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setTelErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setIccidErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setImeiErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setSsidAErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setWpaKeyErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setArrivalDateErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setContractStartDateErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setContractEndDateErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setContractKbnSelectErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setBikoErr(CodeConstant.DOUBLE_QUOTATION);
	}
	
	/**
	 * 必須チェック
	 * @param comDto
	 * @param errMsg エラーメッセージ
	 */
	private void isErrEmpty(Skf2100Sc008CommonDto comDto ,StringBuilder errMsg){

		// 通しNo未入力
		comDto.setRouterNoErr("");
		if(SkfCheckUtils.isNullOrEmpty(comDto.getRouterNo())){
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "通しNo");
			comDto.setRouterNoErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("必須チェックNG:通しNo");
		}
		
		// 電話番号未入力
		comDto.setTelErr("");
		if(SkfCheckUtils.isNullOrEmpty(comDto.getTel())){
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "電話番号");
			comDto.setTelErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("必須チェックNG:電話番号");
		}
		
		// ルーター入荷日未入力
		comDto.setArrivalDateErr("");
		if(SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getArrivalDate()))){
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "ルーター入荷日");
			comDto.setArrivalDateErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("必須チェックNG:ルーター入荷日");
		}
		
		// 契約区分未入力
		comDto.setContractKbnSelectErr("");
		if(SkfCheckUtils.isNullOrEmpty(comDto.getContractKbnSelect())){
			ServiceHelper.addErrorResultMessage(comDto, new String[] { "contractKbnSelect" }, MessageIdConstant.E_SKF_1054, "契約区分");
			comDto.setContractKbnSelectErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("必須チェックNG:契約区分");
		}

		// ルーター契約終了日未入力
		comDto.setContractEndDateErr("");
		if(SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getContractEndDate()))){
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "ルーター契約終了日");
			comDto.setContractEndDateErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("必須チェックNG:ルーター契約終了日");
		}
		
	}
	
	/**
	 * バイト数チェック
	 * @param comDto
	 * @param errMsg エラーメッセージ
	 */
	private void isErrByteCount(Skf2100Sc008CommonDto comDto ,StringBuilder errMsg) throws Exception{

		// 通しNo(10バイト)
		if (!SkfCheckUtils.isNullOrEmpty(comDto.getRouterNo()) 
				&& CheckUtils.isMoreThanByteSize(comDto.getRouterNo(), 13)) {
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1071 , "通しNo","10");
			comDto.setRouterNoErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("バイト数チェックNG:通しNo");
		}
		
		// 電話番号(13バイト)
		if (!SkfCheckUtils.isNullOrEmpty(comDto.getTel()) 
				&& CheckUtils.isMoreThanByteSize(comDto.getTel(), 13)) {
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1071 , "電話番号","13");
			comDto.setTelErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("バイト数チェックNG:電話番号");
		}
		
		// ICCID(40バイト)
		comDto.setIccidErr("");
		if (!SkfCheckUtils.isNullOrEmpty(comDto.getIccid()) 
				&& CheckUtils.isMoreThanByteSize(comDto.getIccid(), 40)) {
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1071 , "ICCID（識別番号）","20");
			comDto.setIccidErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("バイト数チェックNG:ICCID");
		}
		
		// IMEI(30バイト)
		comDto.setImeiErr("");
		if (!SkfCheckUtils.isNullOrEmpty(comDto.getImei()) 
				&& CheckUtils.isMoreThanByteSize(comDto.getImei(), 30)) {
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1071 , "IMEI（製造番号）","15");
			comDto.setImeiErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("バイト数チェックNG:IMEI");
		}
		
		// SSIDA(64バイト)
		comDto.setSsidAErr("");
		if (!SkfCheckUtils.isNullOrEmpty(comDto.getSsidA()) 
				&& CheckUtils.isMoreThanByteSize(comDto.getSsidA(), 64)) {
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1071 , "SSID A","32");
			comDto.setSsidAErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("バイト数チェックNG:SSID A");
		}
		
		// WPA Key(128バイト)
		comDto.setWpaKeyErr("");
		if (!SkfCheckUtils.isNullOrEmpty(comDto.getWpaKey()) 
				&& CheckUtils.isMoreThanByteSize(comDto.getWpaKey(), 128)) {
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1071 , "SSID A","64");
			comDto.setWpaKeyErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("バイト数チェックNG:WPA Key");
		}
		
		// 備考(1600バイト)
		if (!SkfCheckUtils.isNullOrEmpty(comDto.getBiko()) 
				&& CheckUtils.isMoreThanByteSize(comDto.getBiko(), 1600)) {
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1071 , "備考","全角400");
			comDto.setBikoErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("バイト数チェックNG:備考");
		}

	}
	
	/**
	 * 形式チェック
	 * @param comDto
	 * @param errMsg エラーメッセージ
	 */
	private void isErrType(Skf2100Sc008CommonDto comDto ,StringBuilder errMsg) throws Exception{

		
		// 通しNo
		String routerNo = (comDto.getRouterNo() != null) ? comDto.getRouterNo() : "";
		if( !SkfCheckUtils.isNullOrEmpty(routerNo) && !routerNo.matches(NO_CHECK_REG)){
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1050 , "通しNo");
			comDto.setRouterNoErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("形式チェックNG:通しNo");
		
		}
		
		// 電話番号
		String telNo = (comDto.getTel() != null) ? comDto.getTel() : "";
		if( !SkfCheckUtils.isNullOrEmpty(telNo) && !telNo.matches(TELNO_CHECK_REG)){
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1003 , "電話番号");
			comDto.setTelErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("形式チェックNG:電話番号");
		
		}

		// ICCID
		String iccid = (comDto.getIccid() != null) ? comDto.getIccid() : "";
		if( !SkfCheckUtils.isNullOrEmpty(iccid) && !iccid.matches(HANKAKU_CHECK_REG)){
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1002 , "ICCID(識別番号）");
			comDto.setIccidErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("形式チェックNG:ICCID");
		
		}

		// IMEI
		String imei = (comDto.getImei() != null) ? comDto.getImei() : "";
		if( !SkfCheckUtils.isNullOrEmpty(imei) && !imei.matches(HANKAKU_CHECK_REG)){
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1002 , "IMEI(製造番号）");
			comDto.setImeiErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("形式チェックNG:IMEI");
		
		}
		// SSID A
		String ssidA = (comDto.getSsidA() != null) ? comDto.getSsidA() : "";
		if( !SkfCheckUtils.isNullOrEmpty(ssidA) && !ssidA.matches(HANKAKU_CHECK_REG)){
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1002 , "SSID A");
			comDto.setSsidAErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("形式チェックNG:SSID A");
		
		}
		// WPA Key
		String wpaKey = (comDto.getWpaKey() != null) ? comDto.getWpaKey() : "";
		if( !SkfCheckUtils.isNullOrEmpty(wpaKey) && !wpaKey.matches(HANKAKU_CHECK_REG)){
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1002 , "WPA Key");
			comDto.setWpaKeyErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("形式チェックNG:WPA Key");
		
		}

	}
	
	/**
	 * 整合性チェック
	 * @param comDto
	 * @param errMsg エラーメッセージ<
	 */
	private void isErrDateFormat(Skf2100Sc008CommonDto comDto ,StringBuilder errMsg) throws ParseException{
		
		// 契約開始日、契約終了日
		if(!SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getContractStartDate()))){
			// 契約開始日入力有りの場合
			if(validateDateCorrelation(comDto.getContractStartDate(),comDto.getContractEndDate())){
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_3061, "ルーター契約開始日","ルーター契約終了日");
				comDto.setContractStartDateErr(CodeConstant.NFW_VALIDATION_ERROR);
				errMsg.append("整合性チェックNG：契約終了日");
			}else{
				comDto.setContractStartDateErr("");
			}
		}

	
	}
	
	/**
	 * 日付整合性チェック
	 * 
	 * @param fromDate			from日付
	 * @param toDate			to日付
	 * @return					エラー：true(toがfromより過去日)、正常：false
	 * @throws ParseException
	 */
	private Boolean validateDateCorrelation(String fromDate, String toDate) throws ParseException {

		Boolean isCorrelationError = false;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date fDate = null;
		Date tDate = null;

		fDate = dateFormat.parse(getDateText(fromDate));
		tDate = dateFormat.parse(getDateText(toDate));
		// 整合性判定
		if(tDate.before(fDate)){
			// toDateがfromDateより過去日
			isCorrelationError = true;
		}
		return isCorrelationError;
	}

	/**
	 * DTO変数初期化
	 * 「※」項目はアドレスとして戻り値になる。
	 * @param comDto	*DTO
	 */
	public void initialize(Skf2100Sc008CommonDto comDto) {

		/** 検索条件退避 */
		// 通しNo
		comDto.setSearchInfoRouterNo(null);
		// 電話番号
		comDto.setSearchInfoTel(null);
		// ICCID
		comDto.setSearchInfoIccid(null);
		// IMEI
		comDto.setSearchInfoImei(null);
		// 契約区分
		comDto.setSearchInfoContractKbn(null);
		// ルーター入荷日From
		comDto.setSearchInfoArrivalDateFrom(null);
		// ルーター入荷日To
		comDto.setSearchInfoArrivalDateTo(null);
		// 契約終了日From
		comDto.setSearchInfoContractEndDateFrom(null);
		// 契約終了日To
		comDto.setSearchInfoContractEndDateTo(null);
		// 故障
		comDto.setSearchInfoFaultFlag(null);


		/** 非表示項目 */
		// 契約区分
		comDto.setHdnContractKbn(null);
		// 故障フラグ
		comDto.setHdnFaultFlag(null);
		comDto.setHdnChkFaultSelect(null);


		/** ラベル、入力値 */
		// 通しNo
		comDto.setRouterNo(null);
		// 電話番号
		comDto.setTel(null);
		// ICCID
		comDto.setIccid(null);
		// IMEI
		comDto.setImei(null);
		// SSIDA
		comDto.setSsidA(null);
		// WPAKey
		comDto.setWpaKey(null);
		// ルーター入荷日
		comDto.setArrivalDate(null);
		// 契約開始日
		comDto.setContractStartDate(null);
		// 契約終了日
		comDto.setContractEndDate(null);
		// 故障
		comDto.setFaultFlag(null);
		// 備考
		comDto.setBiko(null);

		/** ドロップダウンリスト */
		// 契約区分選択値
		comDto.setContractKbnSelect(null);
		// 契約区分選択リスト
		comDto.setContractKbnDropDownList(null);


		/** 非活性制御 */
		// 編集
		comDto.setEditDisableFlg(false);
		// ボタン
		comDto.setBtnDeleteDisableFlg(false);
		comDto.setBtnRegistDisableFlg(false);

		/** エラー変数初期化 */
		comDto.setRouterNoErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setTelErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setIccidErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setImeiErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setSsidAErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setWpaKeyErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setArrivalDateErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setContractStartDateErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setContractEndDateErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setContractKbnSelectErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setBikoErr(CodeConstant.DOUBLE_QUOTATION);
		
		/** 補足ファイル */
		comDto.setHosokuFileName1(null);
		comDto.setHosokuLink1(null);
		comDto.setHosokuSize1(null);
		comDto.setHosokuFile1(null);
		comDto.setHosokuFileName2(null);
		comDto.setHosokuLink2(null);
		comDto.setHosokuSize2(null);
		comDto.setHosokuFile2(null);
		comDto.setHosokuFileName3(null);
		comDto.setHosokuLink3(null);
		comDto.setHosokuSize3(null);
		comDto.setHosokuFile3(null);

	}
	
	/**
	 * 登録時重複チェック
	 * @param comDto
	 * @return
	 * @throws Exception
	 */
	public boolean checkDuplicate(Skf2100Sc008CommonDto comDto) throws Exception{
		
		//エラーメッセージ
		StringBuilder errMsg = new StringBuilder();

		// 通しNo重複チェック
		if(comDto.isNewDataFlag()){
			// 新規の場合チェック
			// モバイルルーターマスタ情報取得
			Skf2100MMobileRouterWithBLOBs dupData = skfRouterInfoUtils.getMobileRouterInfo(Long.parseLong(comDto.getRouterNo()));
			if(dupData != null){
				// データ有の場合重複エラー
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1033, "通しNo");
				comDto.setRouterNoErr(CodeConstant.NFW_VALIDATION_ERROR);
				errMsg.append("重複チェックNG：通しNo");
			}
			
		}
		
		// IMEI重複チェック
		Skf2100Sc008GetSameImeiDataCountExpParameter chkParam = new Skf2100Sc008GetSameImeiDataCountExpParameter();
		chkParam.setEquipmentCd(CodeConstant.GECD_MOBILEROUTER);
		chkParam.setMobileRouterNo(Long.parseLong(comDto.getRouterNo()));
		chkParam.setImei(comDto.getImei());
		int dupCount = skf2100Sc008GetSameImeiDataCountExpRepository.getSameImeiDataCount(chkParam);
		if(dupCount > 0){
			// 重複データ有
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1033, "IMEI（製造番号）");
			comDto.setImeiErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("重複チェックNG：IMEI");
		}
		
		//エラーが存在する場合
		if (0 < errMsg.toString().length()){
			LogUtils.infoByMsg("checkDuplicate, " + errMsg.toString());
			return false;
		}else{
			return true;
		}
	}
	/**
	 * モバイルルーターマスタ情報設定
	 * 
	 * @param comDto
	 * @return　Skf2100MMobileRouterWithBLOBs
	 */
	public Skf2100MMobileRouterWithBLOBs setRouterData(Skf2100Sc008CommonDto comDto){
		// モバイルルーターマスタ登録
		Skf2100MMobileRouterWithBLOBs routerData = new Skf2100MMobileRouterWithBLOBs();
		routerData.setGeneralEquipmentCd(CodeConstant.GECD_MOBILEROUTER);
		routerData.setMobileRouterNo(Long.parseLong(comDto.getRouterNo()));
		routerData.setTel(comDto.getTel());
		routerData.setIccid(getToRegistString(comDto.getIccid()));
		routerData.setImei(getToRegistString(comDto.getImei()));
		routerData.setSsidA(getToRegistString(comDto.getSsidA()));
		routerData.setWpaKey(getToRegistString(comDto.getWpaKey()));
		routerData.setRouterArrivalDate(getRegistDateText(comDto.getArrivalDate()));
		routerData.setRouterContractStartDate(getRegistDateText(comDto.getContractStartDate()));
		routerData.setRouterContractEndDate(getRegistDateText(comDto.getContractEndDate()));
		String contractKbn = comDto.getContractKbnSelect();
		String faultFlag = "";
		if("true".equals(comDto.getHdnChkFaultSelect())){
			faultFlag = CodeConstant.ROUTER_FAULT_FLAG_FAULT;
		}else{
			faultFlag = CodeConstant.ROUTER_FAULT_FLAG_NORMAL;
		}
		String routerLendingJudgment =  CodeConstant.ROUTER_LENDING_JUDGMENT_TAIYO;
		if(CodeConstant.ROUTER_FAULT_FLAG_FAULT.equals(faultFlag)
				|| CodeConstant.ROUTER_CONTRACT_KBN_KAIYAKU.equals(contractKbn)){
			// 故障フラグONまたは契約区分が解約済みの場合貸与不可
			routerLendingJudgment = CodeConstant.ROUTER_LENDING_JUDGMENT_TAIYOFUKA;
		}
		routerData.setRouterContractKbn(contractKbn);
		routerData.setRouterLendingJudgment(routerLendingJudgment);
		routerData.setFaultFlag(faultFlag);
		routerData.setBiko(getToRegistString(comDto.getBiko()));
		
		if(!SkfCheckUtils.isNullOrEmpty(comDto.getHosokuFileName1())){
			routerData.setRouterSupplementName1(comDto.getHosokuFileName1());
			routerData.setRouterSupplementSize1(comDto.getHosokuSize1());
			routerData.setRouterSupplementFile1(comDto.getHosokuFile1());
		}else{
			routerData.setRouterSupplementName1(null);
			routerData.setRouterSupplementSize1(null);
			routerData.setRouterSupplementFile1(null);
		}
		if(!SkfCheckUtils.isNullOrEmpty(comDto.getHosokuFileName2())){
			routerData.setRouterSupplementName2(comDto.getHosokuFileName2());
			routerData.setRouterSupplementSize2(comDto.getHosokuSize2());
			routerData.setRouterSupplementFile2(comDto.getHosokuFile2());
		}else{
			routerData.setRouterSupplementName2(null);
			routerData.setRouterSupplementSize2(null);
			routerData.setRouterSupplementFile2(null);
		}
		if(!SkfCheckUtils.isNullOrEmpty(comDto.getHosokuFileName3())){
			routerData.setRouterSupplementName3(comDto.getHosokuFileName3());
			routerData.setRouterSupplementSize3(comDto.getHosokuSize3());
			routerData.setRouterSupplementFile3(comDto.getHosokuFile3());
		}else{
			routerData.setRouterSupplementName3(null);
			routerData.setRouterSupplementSize3(null);
			routerData.setRouterSupplementFile3(null);
		}
		
		return routerData;
	}
}
