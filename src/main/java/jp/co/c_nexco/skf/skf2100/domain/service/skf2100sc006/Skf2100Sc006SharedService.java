/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc006;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetCompanyAgencyListExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShain;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShainKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3050TMonthlyManageData;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetCompanyAgencyListExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MShainRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3050TMonthlyManageDataRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100Sc006common.Skf2100Sc006CommonDto;

/**
 * Skf2100Sc006SharedService モバイルルーター貸出管理簿登録共通処理Service
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2100Sc006SharedService {

	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	@Autowired
	private SkfDropDownUtils skfDropDownUtils;
	@Autowired
	private Skf1010MShainRepository skf1010MShainRepository;
	@Autowired
	private Skf3050TMonthlyManageDataRepository skf3050TMonthlyManageDataRepository;
	@Autowired
	private Skf3022Sc006GetCompanyAgencyListExpRepository skf3022Sc006GetCompanyAgencyListExpRepository;
	
	
	private static final int DATE_LEN = 8;
	// ステータス文字色
	public static final String FONT_COLOR_GREEN = "color:green;";
	public static final String FONT_COLOR_BLUE = "color:blue;";
	private static final String NO_KANRI_ID = "0";
	// メールアドレスチェック正規表現
	private static final String MAIL_ADDRESS_CHECK_REG = "[!-~]+@[!-~]+\\.[!-~]+";
	// 電話番号チェック正規表現
	private static final String TELNO_CHECK_REG = "^[0-9-]*$";

	
	/**
	 * 「モバイルルーター締め処理実行区分」取得
	 * @param nengetsu 年月
	 * @return 締め処理実行区分
	 */
	public String getBillingActKbn(String nengetsu){
		
		String billingActKbn = CodeConstant.DOUBLE_QUOTATION;
		// 「モバイルルーター締め処理実行区分」を取得する
		if(!SkfCheckUtils.isNullOrEmpty(nengetsu)){
			Skf3050TMonthlyManageData da = skf3050TMonthlyManageDataRepository.selectByPrimaryKey(nengetsu);
			if(da != null){
				billingActKbn = da.getMobileRouterBillingActKbn();
			}
			da = null;
		}
		
		return billingActKbn;
	}
	
	/**
	 * ドロップダウンリスト作成
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param sc006OldKaisyaNameSelect			原籍会社選択値
	 * @param sc006OldKaisyaNameSelectList		*原籍会社ドロップダウンリスト
	 * @param sc006KyuyoKaisyaSelect			給与支給会社名選択値
	 * @param sc006KyuyoKaisyaSelectList		*給与支給会社名ドロップダウンリスト
	 */
	public void setDdlControlValues(
			String originalCompanySelect, List<Map<String, Object>> originalCompanyList,
			String payCompanySelect, List<Map<String, Object>> payCompanyList) {

		// 会社リスト（外部機関含む）取得
		List<Skf3022Sc006GetCompanyAgencyListExp> companyAgencyList = new ArrayList<Skf3022Sc006GetCompanyAgencyListExp>();
		companyAgencyList = skf3022Sc006GetCompanyAgencyListExpRepository.getCompanyAgencyList();
		// 原籍会社
		originalCompanyList.clear();
		originalCompanyList.addAll(getCompanyAgencyDoropDownList(companyAgencyList, originalCompanySelect, true));
		// 給与支給会社
		payCompanyList.clear();
		payCompanyList.addAll(getCompanyAgencyDoropDownList(companyAgencyList, payCompanySelect, true));

	}
	
	/**
	 * 会社リスト（外部機関含む）ドロップダウンリスト作成
	 * 
	 * @param companyAgencyList	会社リスト（外部機関含む）
	 * @param selectedValue		選択値
	 * @param isFirstRowEmpty	先頭行の空行有無。true:空行あり false：空行なし
	 * @return	会社リスト（外部機関含む）ドロップダウンリスト
	 */
	private List<Map<String, Object>> getCompanyAgencyDoropDownList(
			List<Skf3022Sc006GetCompanyAgencyListExp> companyAgencyList, String selectedValue, boolean isFirstRowEmpty) {

		// リスト定義
		List<Map<String, Object>> doropDownList = new ArrayList<Map<String, Object>>();
		Map<String, Object> forListMap = new HashMap<String, Object>();
		if (isFirstRowEmpty) {
			forListMap.put("value", CodeConstant.DOUBLE_QUOTATION);
			forListMap.put("label", CodeConstant.DOUBLE_QUOTATION);
			doropDownList.add(forListMap);
		}

		for (Skf3022Sc006GetCompanyAgencyListExp entity : companyAgencyList) {

			// 表示・値を設定
			forListMap = new HashMap<String, Object>();
			forListMap.put("value", entity.getCompanyCd());
			forListMap.put("label", entity.getCompanyName());
			if (!CheckUtils.isEmpty(selectedValue)) {
				if (Objects.equals(entity.getCompanyCd(), selectedValue)) {
					forListMap.put("selected", true);
				}
			}
			doropDownList.add(forListMap);
		}
		return doropDownList;
	}
	
	/**
	 * ヘッダーのフォントカラーを設定する。
	 * パラメータのステータス区分別のフォントカラー文字列を返却する
	 * 
	 * @param statusKbn	申請ステータス
	 * @return	フォントカラー文字列
	 */
	public String setApplStatusCss(String statusKbn) {

		String colorStr = "color:black;";
		// null空チェック
		if (CheckUtils.isEmpty(statusKbn)) {
			return colorStr;
		}
		// 備品ステータス判定
		switch (statusKbn) {
		case CodeConstant.STATUS_SHONIN_ZUMI:
			colorStr = FONT_COLOR_BLUE;
			break;
		default :
			colorStr = FONT_COLOR_GREEN;
			break;
		};
		return colorStr;
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
	 * 契約区分文字列の取得
	 * 
	 * @param contractKbn 契約区分値
	 * @return	契約区分文字列
	 */
	public String setContractKbnTxt(String contractKbn){

		String contractKbnTxt = CodeConstant.DOUBLE_QUOTATION;
		
		//　契約区分リスト取得
		List<Map<String, Object>> contractKbnDropDownList = skfDropDownUtils
				.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_ROUTER_CONTRACT_KBN, "", true);
		
		// リスト一致する区分名を返却
		contractKbnTxt = getTargetDropDownLabel(contractKbn, contractKbnDropDownList);
		
		return contractKbnTxt;
	}
	
	/**
	 * 申請状況文字列の取得
	 * 
	 * @param entityApplStatus 申請状況ステータス
	 * @return	申請状況文字列
	 */
	public String setApplStatusTxt(String entityApplStatus ,String yearmonth){
		
		// 申請ステータス
		String applStatus = "";
		List<Map<String, Object>> statusDropDownList = skfDropDownUtils
				.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_STATUS, "", true);
		String sysNengetsu = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu();
		
		if (NfwStringUtils.isEmpty(entityApplStatus)) {
			applStatus = CodeConstant.HYPHEN;
		} else {
			if (Integer.parseInt(yearmonth) < Integer.parseInt(sysNengetsu)) {
				applStatus = CodeConstant.HYPHEN;
			} else {
				applStatus = getTargetDropDownLabel(entityApplStatus,statusDropDownList);
			}
		}
		
		return applStatus;
	}
	
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
	 * 画面項目制御
	 * @param comDto
	 */
	public void setControlStatus(Skf2100Sc006CommonDto comDto){

		
		if(CodeConstant.BILLINGACTKBN_JIKKO_SUMI.equals(comDto.getHdnBillingActKbn())){
			//締め処理 実行済
			setActivateFalse(comDto);
			return;
		}
		
		if(SkfCheckUtils.isNullOrEmpty(comDto.getHdnShainNo())){
			// 社員番号設定なし
			// 社員入力支援のみ活性
			setActivateStateNew1(comDto);
			return;
		}
		
		
		// 社員番号設定有り
		if(CodeConstant.STATUS_SHONIN_ZUMI.equals(comDto.getHdnApplStatus())
			|| SkfCheckUtils.isNullOrEmpty(comDto.getHdnApplStatus())){
			// 申請状況が「承認済み」もしくは空（予定データなし)
			// 管理簿ID未設定
			if(NO_KANRI_ID.equals(comDto.getHdnRouterKanriId())){
				// 新規登録
				setActivateStateNew2(comDto);
			}else{
				// 管理簿ID有り
				// 入力支援以外活性
				setActivateTrue(comDto);
			}
			
		}else{
			// 予定データ有で承認以外
			// 編集不可
			setActivateFalse(comDto);
		}
		
	
	}
	
	/**
	 * 画面項目制御：すべて非活性（運用ガイド除く）
	 * @param comDto
	 */
	public void setActivateFalse(Skf2100Sc006CommonDto comDto){
		// 社員入力支援ボタン
		comDto.setShainShienDisableFlg(true);
		// 入力項目
		comDto.setEditDisableFlg(true);
		// 運用ガイドボタン
		comDto.setBtnUnyonGuideDisableFlg(false);
		// 登録ボタン
		comDto.setBtnRegistDisableFlg(true);
		// 削除ボタン
		comDto.setBtnDeleteDisableFlg(true);
	}
	
	/**
	 * 画面項目制御：編集可能（社員入力支援除く）
	 * @param comDto
	 */
	public void setActivateTrue(Skf2100Sc006CommonDto comDto){
		// 社員入力支援ボタン
		comDto.setShainShienDisableFlg(true);
		// 入力項目
		comDto.setEditDisableFlg(false);
		// 運用ガイドボタン
		comDto.setBtnUnyonGuideDisableFlg(false);
		// 登録ボタン
		comDto.setBtnRegistDisableFlg(false);
		// 削除ボタン
		comDto.setBtnDeleteDisableFlg(false);
	}
	
	/**
	 * 画面項目制御：新規登録（社員入力のみ）
	 * @param comDto
	 */
	public void setActivateStateNew1(Skf2100Sc006CommonDto comDto){
		// 社員入力支援ボタン
		comDto.setShainShienDisableFlg(false);
		// 入力項目
		comDto.setEditDisableFlg(true);
		// 運用ガイドボタン
		comDto.setBtnUnyonGuideDisableFlg(false);
		// 登録ボタン
		comDto.setBtnRegistDisableFlg(true);
		// 削除ボタン
		comDto.setBtnDeleteDisableFlg(true);
	}
	
	
	/**
	 * 画面項目制御：新規登録
	 * @param comDto
	 */
	public void setActivateStateNew2(Skf2100Sc006CommonDto comDto){
		// 社員入力支援ボタン
		comDto.setShainShienDisableFlg(false);
		// 入力項目
		comDto.setEditDisableFlg(false);
		// 運用ガイドボタン
		comDto.setBtnUnyonGuideDisableFlg(false);
		// 登録ボタン
		comDto.setBtnRegistDisableFlg(false);
		// 削除ボタン
		comDto.setBtnDeleteDisableFlg(true);
	}
	
	/**
	 * @param comDto
	 * @return
	 * @throws ParseException
	 */
	public boolean validateInput(Skf2100Sc006CommonDto comDto) throws Exception{
		
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
	public void errReset(Skf2100Sc006CommonDto comDto){
		
		/** エラー変数初期化 */
		comDto.setUseStartHopeDayErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setHannyuTelErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setHannyuMailaddressErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setShippingDateErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setReceivedDateErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setUseStopDayErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setHansyutuTelErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setReturnDayErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setOriginalCompanyListErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setPayCompanyListErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setBikoErr(CodeConstant.DOUBLE_QUOTATION);
	}
	
	/**
	 * 必須チェック
	 * @param comDto
	 * @param errMsg エラーメッセージ
	 */
	private void isErrEmpty(Skf2100Sc006CommonDto comDto ,StringBuilder errMsg){


		//原籍会社名
		comDto.setOriginalCompanyListErr("");
		if(SkfCheckUtils.isNullOrEmpty(comDto.getOriginalCompanySelect())){
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1054, "原籍会社");
			comDto.setOriginalCompanyListErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("必須チェックNG:原籍会社");
		}

		//給与支給会社
		comDto.setPayCompanyListErr("");
		if(SkfCheckUtils.isNullOrEmpty(comDto.getPayCompanySelect())){
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1054, "給与支給会社");
			comDto.setPayCompanyListErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("必須チェックNG:給与支給会社");
		}

		// 貸出希望日未入力
		comDto.setUseStartHopeDayErr("");
		if(SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getUseStartHopeDay()))){
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "貸出希望日");
			comDto.setUseStartHopeDayErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("必須チェックNG:貸出希望日");
		}

		// 搬入本人連絡先未入力
		comDto.setHannyuTelErr("");
		if(SkfCheckUtils.isNullOrEmpty(comDto.getHannyuTel())){
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "本人連絡先");
			comDto.setHannyuTelErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("必須チェックNG:本人連絡先（搬入）");
		}
		
		// 搬入本人メールアドレス未入力
		comDto.setHannyuMailaddressErr("");
		if(SkfCheckUtils.isNullOrEmpty(comDto.getHannyuMailaddress())){
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "本人メールアドレス");
			comDto.setHannyuMailaddressErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("必須チェックNG:メールアドレス");
		}

		// 発送日未入力
		comDto.setShippingDateErr("");
		if(SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getShippingDate()))){
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "発送日");
			comDto.setShippingDateErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("必須チェックNG:発送日");
		}
		
		// 納品日未入力
		comDto.setReceivedDateErr("");
		if(SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getReceivedDate()))){
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "納品日（受領日）");
			comDto.setReceivedDateErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("必須チェックNG:納品日");
		}

		// 利用停止日入力有り
		comDto.setHansyutuTelErr("");
		if(!SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getUseStopDay()))){
			// 搬出本人連絡先未入力場合
			if(SkfCheckUtils.isNullOrEmpty(comDto.getHansyutuTel())){
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "本人連絡先");
				comDto.setHansyutuTelErr(CodeConstant.NFW_VALIDATION_ERROR);
				errMsg.append("必須関連チェックNG：本人連絡先");
			}
		}
		
		// 搬出本人連絡先有り
		comDto.setUseStopDayErr("");
		if(!SkfCheckUtils.isNullOrEmpty(comDto.getHansyutuTel())){
			// 利用停止日未入力
			if(SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getUseStopDay()))){
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "利用停止日（最終利用日）");
				comDto.setUseStopDayErr(CodeConstant.NFW_VALIDATION_ERROR);
				errMsg.append("必須関連チェックNG：利用停止日");
			}
		}

		// 窓口返却日入力有り、
		if(!SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getReturnDay()))){
			// 利用停止日未入力
			if(SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getUseStopDay()))){
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "利用停止日（最終利用日）");
				comDto.setUseStopDayErr(CodeConstant.NFW_VALIDATION_ERROR);
				errMsg.append("必須関連チェックNG：利用停止日");
			}
			
			// 搬出本人連絡先未入力場合
			if(SkfCheckUtils.isNullOrEmpty(comDto.getHansyutuTel())){
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "本人連絡先");
				comDto.setHansyutuTelErr(CodeConstant.NFW_VALIDATION_ERROR);
				errMsg.append("必須関連チェックNG：本人連絡先");
			}
		}

	

	}
	
	/**
	 * バイト数チェック
	 * @param comDto
	 * @param errMsg エラーメッセージ
	 */
	private void isErrByteCount(Skf2100Sc006CommonDto comDto ,StringBuilder errMsg) throws Exception{


		// 本人連絡先（搬入）(13バイト)
		if (!SkfCheckUtils.isNullOrEmpty(comDto.getHannyuTel())
				&& CheckUtils.isMoreThanByteSize(comDto.getHannyuTel(), 13)) {
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1071 , "本人連絡先","13");
			comDto.setHannyuTelErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("バイト数チェックNG:本人連絡先（搬入）");
		}
		
		// メールアドレス(255バイト)
		if (!SkfCheckUtils.isNullOrEmpty(comDto.getHannyuMailaddress())
				&& CheckUtils.isMoreThanByteSize(comDto.getHannyuMailaddress(), 255)) {
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1071 , "本人メールアドレス","255");
			comDto.setHannyuMailaddressErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("バイト数チェックNG:本人メールアドレス");
		}
		
		// 本人連絡先（搬出）(13バイト)
		if (!SkfCheckUtils.isNullOrEmpty(comDto.getHansyutuTel())
			&& CheckUtils.isMoreThanByteSize(comDto.getHansyutuTel(), 13)) {
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1071 , "本人連絡先","13");
			comDto.setHansyutuTelErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("バイト数チェックNG:本人連絡先（搬出）");
		}
		
		// 備考(1600バイト)
		comDto.setBikoErr("");
		if (!SkfCheckUtils.isNullOrEmpty(comDto.getBiko())
				&& CheckUtils.isMoreThanByteSize(comDto.getBiko(), 1600)) {
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1071 , "備考","全角800");
			comDto.setBikoErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("バイト数チェックNG:備考");
		}

	}
	
	/**
	 * 形式チェック
	 * @param comDto
	 * @param errMsg エラーメッセージ
	 */
	private void isErrType(Skf2100Sc006CommonDto comDto ,StringBuilder errMsg) throws Exception{

		String telNo = CodeConstant.DOUBLE_QUOTATION;
		// 本人連絡先（搬入）
		telNo = (comDto.getHannyuTel() != null) ? comDto.getHannyuTel() : "";
		if( !SkfCheckUtils.isNullOrEmpty(telNo) && !telNo.matches(TELNO_CHECK_REG)){
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1003 , "本人連絡先");
			comDto.setHannyuTelErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("形式チェックNG:本人連絡先（搬入）");
		
		}
		
		// メールアドレス
		String mailAddress = (comDto.getHannyuMailaddress() != null) ? comDto.getHannyuMailaddress() : "";
		if( !SkfCheckUtils.isNullOrEmpty(mailAddress) && !mailAddress.matches(MAIL_ADDRESS_CHECK_REG)){
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_3032);
			comDto.setHannyuMailaddressErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("形式チェックNG:本人メールアドレス");
			
		}
		
		// 本人連絡先（搬入）
		telNo = (comDto.getHansyutuTel() != null) ? comDto.getHansyutuTel() : "";
		if( !SkfCheckUtils.isNullOrEmpty(telNo) && !telNo.matches(TELNO_CHECK_REG)){
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1003 , "本人連絡先");
			comDto.setHansyutuTelErr(CodeConstant.NFW_VALIDATION_ERROR);
			errMsg.append("形式チェックNG:本人連絡先（搬出）");
		
		}

	}
	
	/**
	 * 整合性チェック
	 * @param comDto
	 * @param errMsg エラーメッセージ<
	 */
	private void isErrDateFormat(Skf2100Sc006CommonDto comDto ,StringBuilder errMsg) throws ParseException{
		
		// 納品日と利用停止日
		if(!SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getUseStopDay()))){
			if(validateDateCorrelation(comDto.getReceivedDate(),comDto.getUseStopDay())){
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1133, "利用停止日（最終利用日）");
				comDto.setUseStopDayErr(CodeConstant.NFW_VALIDATION_ERROR);
				errMsg.append("整合性チェックNG：利用停止日");
			}else{
				comDto.setUseStopDayErr("");
			}
		}

	
	}
	
	/**
	 * パラメータのJSON文字列配列をリスト形式に変換して返却する
	 * 
	 * @param jsonStr	JSON文字列配列
	 * @return			List<Map<String, Object>>
	 */
	public List<Map<String, Object>> jsonArrayToArrayList (String jsonStr) {

		// 返却用リスト
		List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		// JSON文字列判定
		if (Objects.equals(jsonStr, null) || jsonStr.length() <= 0) {
			LogUtils.debugByMsg("文字列未設定");
			// 文字列未設定のため処理しない
			return listData;
		}
		try {
			JSONArray arr = null;
			arr = new JSONArray(jsonStr);
			if (arr != null) {
				int arrCnt = arr.length();
				ObjectMapper mapper = new ObjectMapper();
				for(int i = 0; i < arrCnt; i++) {
					listData.add(mapper.readValue(arr.get(i).toString(), new TypeReference<Map<String, Object>>(){}));
				}
				arr = null;
				mapper = null;
			}
		} catch (JSONException e) {
			LogUtils.infoByMsg("jsonArrayToArrayList, " + e.getMessage());
		} catch (JsonParseException e) {
			LogUtils.infoByMsg("jsonArrayToArrayList, " + e.getMessage());
		} catch (JsonMappingException e) {
			LogUtils.infoByMsg("jsonArrayToArrayList, " + e.getMessage());
		} catch (IOException e) {
			LogUtils.infoByMsg("jsonArrayToArrayList, " + e.getMessage());
		}
		return listData;
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
	 *  社員番号変更フラグ取得メソッド
	 * @param shainNo 社員番号
	 * @return
	 */
	public String getShainNoChangeFlag(String shainNo){
		
		String shainNoChangeFlg = CodeConstant.DOUBLE_QUOTATION;
		
		Skf1010MShainKey key = new Skf1010MShainKey();
		key.setCompanyCd(CodeConstant.C001);
		key.setShainNo(shainNo);
		Skf1010MShain selectShain = skf1010MShainRepository.selectByPrimaryKey(key);
		if(selectShain != null){
			shainNoChangeFlg = selectShain.getShainNoChangeFlg();
		}
		return shainNoChangeFlg;
	}
	

	
	/**
	 * 可変ラベル値を復元
	 * エラー時の可変ラベルの値を設定する。
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param labelList		可変ラベルリスト
	 * @param comDto		*DTO
	 */
	public void setVariableLabel(List<Map<String, Object>> labelList, Skf2100Sc006CommonDto comDto) {
		// 可変ラベル値
		Map <String, Object> labelMap = labelList.get(0);
		// 社員名
		String shainName = (labelMap.get("shainName") != null) ? labelMap.get("shainName").toString() : "";
		// 社員番号
		String shainNo = (labelMap.get("shainNo") != null) ? labelMap.get("shainNo").toString() : "";

		
		/** 戻り値設定 */
		comDto.setShainName(shainName);
		comDto.setShainNo(shainNo);

		
	}

	/**
	 * DTO変数初期化
	 * 「※」項目はアドレスとして戻り値になる。
	 * @param comDto	*DTO
	 */
	public void initialize(Skf2100Sc006CommonDto comDto) {

		/** 検索条件退避 */
		// 社員番号
		comDto.setSearchInfoShainNo(null);
		// 社員氏名
		comDto.setSearchInfoShainName(null);
		// 通しNo
		comDto.setSearchInfoRouterNo(null);
		// 電話番号
		comDto.setSearchInfoTel(null);
		// 契約区分
		comDto.setSearchInfoContractKbn(null);
		// 契約終了日From
		comDto.setSearchInfoContractEndDateFrom(null);
		// 契約終了日To
		comDto.setSearchInfoContractEndDateTo(null);
		// 対象年月
		comDto.setSearchInfoYearMonth(null);
		// 空きルーター
		comDto.setSearchInfoAkiRouter(null);

		/** 非表示項目 */
		// 貸出管理簿ID
		comDto.setHdnRouterKanriId(null);
		// 対象年月
		comDto.setHdnYearMonth(null);
		// 申請ステータス
		comDto.setHdnApplStatus(null);
		// 契約区分
		comDto.setHdnContractKbn(null);
		// 故障フラグ
		comDto.setHdnFaultFlag(null);
		// 社員番号
		comDto.setHdnShainNo(null);
		// 社員名
		comDto.setHdnShainName(null);
		// 原籍会社
		comDto.setHdnOriginalCompanySelect(null);
		// 給与支給会社
		comDto.setHdnPayCompanySelect(null);
		//社員番号変更フラグ
		comDto.setHdnShainNoChangeFlg(null);

		/** 運用ガイド */
		comDto.setOperationGuidePath(null);

		// JSONラベルリスト
		comDto.setJsonLabelList(null);

		/** ラベル、入力値 */
		// 社員番号
		comDto.setShainNo(null);
		// 社員名
		comDto.setShainName(null);
		// 対象年月(ヘッダ）
		comDto.setYearMonthTxt(null);
		// 申請状況（ヘッダ）
		comDto.setApplStatusTxt(null);
		// 申請状況文字色
		comDto.setApplStatusTxtColor(null);
		// 通しNo
		comDto.setRouterNo(null);
		// 電話番号
		comDto.setTel(null);
		// ICCID
		comDto.setIccid(null);
		// IMEI
		comDto.setImei(null);
		// ルーター入荷日
		comDto.setArrivalDate(null);
		// 契約区分
		comDto.setContractKbnTxt(null);
		// 契約終了日
		comDto.setContractEndDate(null);
		// 故障
		comDto.setFaultTxt(null);
		// 利用開始希望日
		comDto.setUseStartHopeDay(null);
		// 本人連絡先（搬入）
		comDto.setHannyuTel(null);
		// 本人メールアドレス
		comDto.setHannyuMailaddress(null);
		// 発送日
		comDto.setShippingDate(null);
		// 納品日
		comDto.setReceivedDate(null);
		// 利用停止日
		comDto.setUseStopDay(null);
		// 本人連絡先（搬出）
		comDto.setHansyutuTel(null);
		// 窓口返却日
		comDto.setReturnDay(null);
		// 備考
		comDto.setBiko(null);

		/** ドロップダウンリスト */
		// 原籍会社選択値
		comDto.setOriginalCompanySelect(null);
		// 原籍会社選択リスト
		comDto.setOriginalCompanyList(null);
		// 給与支給会社名選択値
		comDto.setPayCompanySelect(null);
		// 給与支給会社名選択リスト
		comDto.setPayCompanyList(null);

		/** 非活性制御 */
		// 社員入力
		comDto.setShainShienDisableFlg(false);
		// 編集
		comDto.setEditDisableFlg(false);
		// ボタン
		comDto.setBtnUnyonGuideDisableFlg(false);
		comDto.setBtnDeleteDisableFlg(false);
		comDto.setBtnRegistDisableFlg(false);

		/** エラー変数初期化 */
		comDto.setUseStartHopeDayErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setHannyuTelErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setHannyuMailaddressErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setShippingDateErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setReceivedDateErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setUseStopDayErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setHansyutuTelErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setReturnDayErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setOriginalCompanyListErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setPayCompanyListErr(CodeConstant.DOUBLE_QUOTATION);
		comDto.setBikoErr(CodeConstant.DOUBLE_QUOTATION);

	}
	


}
