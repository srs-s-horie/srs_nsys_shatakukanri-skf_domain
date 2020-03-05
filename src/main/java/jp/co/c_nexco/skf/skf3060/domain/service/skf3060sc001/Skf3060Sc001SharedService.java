/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3060.domain.service.skf3060sc001;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3060Sc001.Skf3060Sc001GetAgeKasanTargetCountExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3060Sc001.Skf3060Sc001GetAgeKasanTargetCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3060Sc001.Skf3060Sc001GetAgeKasanTargetDataInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3060Sc001.Skf3060Sc001GetAgeKasanTargetDataInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBaseBusinessLogicUtils.SkfBaseBusinessLogicUtilsGetSystemDateTimeExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBaseBusinessLogicUtils.SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBaseBusinessLogicUtils.SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3060Sc001.Skf3060Sc001GetAgeKasanTargetCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3060Sc001.Skf3060Sc001GetAgeKasanTargetDataInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfBaseBusinessLogicUtils.SkfBaseBusinessLogicUtilsGetSystemDateTimeExpRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.skf3060.domain.dto.skf3060Sc001common.Skf3060Sc001CommonDto;

/**
 * Skf3060Sc001SharedService 年齢加算対象者一覧内共通クラス
 * 
 * @author NEXCOシステムズ
 *
 */

@Service
public class Skf3060Sc001SharedService {
	
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;	
	
	@Autowired
	private SkfDropDownUtils ddlUtils;

	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	
	@Autowired
	private Skf3060Sc001GetAgeKasanTargetCountExpRepository skf3060Sc001GetAgeKasanTargetCountExpRepository;
	
	@Autowired
	private Skf3060Sc001GetAgeKasanTargetDataInfoExpRepository skf3060Sc001GetAgeKasanTargetDataInfoExpRepository;

	@Autowired
	private SkfBaseBusinessLogicUtilsGetSystemDateTimeExpRepository skfBaseBusinessLogicUtilsGetSystemDateTimeExpRepository;
	
	// 定数
	// 送信状態：未送付
	public String SEND_STATUS_NOT_SEND =  "未送付";
	public String SEND_STATUS_NOT_SEND_CD =  "0";
	// 送信状態：送付済
	public String SEND_STATUS_SENDED =  "送付済";
	public String SEND_STATUS_SENDED_CD =  "1";
	/**
	 * 日付From
	 */
	public String TERM_FROM_DATE = "0402";
	/**
	 * 日付To
	 */
	public String TERM_TO_DATE = "0401";
	/**
	 * 検索パターン（カウント取得）
	 */
	public String SEARCH_PATTERN_COUNT =  "0";
	/**
	 * 検索パターン（ListTable情報取得）
	 */
	public String SEARCH_PATTERN_DATA =  "1";

	/**
	 * 処理パターン（初期化）
	 */
	public String PROCESS_PATTERN_INITIALIZE =  "0";
	/**
	 * 処理パターン（検索）
	 */
	public String PROCESS_PATTERN_SEARCH =  "1";
	
	/**
	 * 仮社員番号
	 */
	private String KARI_SHAIN_NO = "K";

	// 年齢加算基準期間のデフォルト値
	// (VB版ではNew DateTime()の値を使用)
	public String DEFAULT_KASAN_START_DATE = "000101"; 
	// リストテーブルの１ページ最大表示行数
	@Value("${skf3060.skf3060_sc001.max_row_count}")
	private String listTableMaxRowCount;	    	
	// 最大検索数
	@Value("${skf3060.skf3060_sc001.search_max_count}")
	private String listTableSearchMaxCount;	   	
    @Value("${skf.common.validate_error}")
    private String validationErrorCode;		
	
	/**
	 * 基準年度を取得する 
	 * 
	 * @param sysDate
	 *            システム年月
	 * @param yearList
	 *            基準年を格納するリスト
	 * @return 基準年
	 */	
	public List<Integer> getBaseYear(Date sysDate, List<Integer> yearList){
		
		//SimpleDateFormat dateFormatFrom = new SimpleDateFormat("yyyyMM");
		//Date dateFrom = null;
		int yearFrom = 0;
		int yearTo = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sysDate);
		
		// 月の取り出し
		int month = calendar.get(Calendar.MONTH) + 1;
		if(month < 4){
			yearTo = calendar.get(Calendar.YEAR);
			calendar.add(Calendar.YEAR, -1);
			yearFrom = calendar.get(Calendar.YEAR);
		}else{
			yearFrom = calendar.get(Calendar.YEAR);
			calendar.add(Calendar.YEAR, 1);
			yearTo = calendar.get(Calendar.YEAR);
		}

		yearList.add(0, yearFrom);
		yearList.add(1,yearTo);
		
		return yearList;
	}
	
	
	/**
	 * ドロップダウンリストに設定するリストを取得する。<br>
	 * 「※」項目はアドレスとして戻り値になる。
	 */
	public void getDoropDownList(String originalCompany, List<Map<String, Object>> originalCompanyList,
			String salaryCompany, List<Map<String, Object>> salaryCompanyList,
			String sendMailStatus, List<Map<String, Object>> sendMailStatusList) {

		// 先頭を空にする
		boolean isFirstRowEmpty = true;
		
		// 原籍会社リストを設定
		originalCompanyList.clear();
		originalCompanyList.addAll(ddlUtils.getDdlCompanyByCd(originalCompany, isFirstRowEmpty));
		// 給与支給会社名リストを設定
		salaryCompanyList.clear();
		salaryCompanyList.addAll(ddlUtils.getDdlCompanyByCd(salaryCompany, isFirstRowEmpty));
		// 送信状態リストを設定
		sendMailStatusList.clear();
		sendMailStatusList.addAll(getSendMailStatusList(sendMailStatus, isFirstRowEmpty));

	}
	
	/**
	 * 送信状態リストを取得する 
	 * 
	 * @param sendMailStatus
	 *            選択値
	 * @param isFirstRowEmpty
	 *            先頭行の空行有無。true:空行あり false：空行なし
	 * @return 送信状態リスト
	 */	
	public List<Map<String, Object>> getSendMailStatusList(String sendMailStatus, boolean isFirstRowEmpty) {
	
		List<Map<String, Object>> sendMailStatusList = new ArrayList<Map<String, Object>>();

		Map<String, Object> mailStatusMap = new HashMap<String, Object>();
		
		if (isFirstRowEmpty) {
			mailStatusMap.put("value", CodeConstant.DOUBLE_QUOTATION);
			mailStatusMap.put("label", CodeConstant.DOUBLE_QUOTATION);
			sendMailStatusList.add(mailStatusMap);
		}

		// 送信状態：未送付
		mailStatusMap = new HashMap<String, Object>();
		mailStatusMap.put("value", SEND_STATUS_NOT_SEND_CD);
		mailStatusMap.put("label", SEND_STATUS_NOT_SEND);
		if(Objects.equals(sendMailStatus, SEND_STATUS_NOT_SEND_CD)){
			mailStatusMap.put("selected", true);
		}
		sendMailStatusList.add(mailStatusMap);		
		
		// 送信状態：送付済
		mailStatusMap = new HashMap<String, Object>();
		mailStatusMap.put("value", SEND_STATUS_SENDED_CD);
		mailStatusMap.put("label", SEND_STATUS_SENDED);
		if(Objects.equals(sendMailStatus, SEND_STATUS_SENDED_CD)){
			mailStatusMap.put("selected", true);
		}
		sendMailStatusList.add(mailStatusMap);		
		
		// 返却するリストをDebugログで出力
		LogUtils.debugByMsg("返却する送信状態リスト：" + sendMailStatusList.toString());
		
		return sendMailStatusList;
	}

	
	/**
	 * 特定の文字をエスケープする（検索用）
	 * @param beforStr 変換前の文字列
	 * @return 変換後の文字列
	 */
	public String escapeString(String beforStr){
		String afterStr = null;
		// 文字エスケープ(%, _, '\)
		if (beforStr != null) {
			// 「\」を「\\」に置換
			afterStr = beforStr.replace("\\", "\\\\");
			// 「%」を「\%」に置換、「_」を「\_」に置換、「'」を「''」に置換
			afterStr = afterStr.replace("%", "\\%").replace("_", "\\_").replace("'", "''");
		}
		
		return afterStr;
	}
	
	
	/**
	 * 日付から"/"と"_"を取り除く
	 * 
	 * @param targetDate
	 * 			日付文字列
	 * @return Replace後の日付文字列
	 *  
	 */
	public String replaceDateFormat(String targetDate){
		return targetDate.replace("/", "").replace("_", "");
	}
	
	/**
	 * 年齢加算基準期間
	 * 基準期間toに入っている日付をYYYYMMに変換
	 * 
	 * @param targetDate
	 * 			日付文字列
	 * @return Replace後の日付文字列
	 *  
	 */
	 public String createKasanStartDate(String targetDate){
		 String kasanStartDate = "";
		 // 基準機関Toから"/"、"_"を取り除く
		 kasanStartDate = replaceDateFormat(targetDate);
		 // 文字列を日付型に変換する
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		 Date mapDate = null;
		 try{
	 		 mapDate = dateFormat.parse(kasanStartDate);
	 	 }catch(ParseException ex){
		 	 LogUtils.debugByMsg("年齢加算基準期間-日時変換エラー :" + kasanStartDate);
			 return DEFAULT_KASAN_START_DATE;
		 }		
		 SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyyMM");
		 kasanStartDate = dateFormat2.format(mapDate);
		 
		 return kasanStartDate;
	 }
	
	/**
	 * 日付のフォーマットを「yyyyMMdd」から「yyyy/MM/dd」へ変換する
	 * 
	 * @param targetDate
	 * 			日付文字列
	 * @return 変換後の日付文字列
	 *  
	 */
	 public String changeDateFormat(String targetDate){
		if(StringUtils.isEmpty(targetDate)){
			targetDate = null;
		}else{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			try{
				Date date = dateFormat.parse(targetDate);
				SimpleDateFormat dateFormatStr = new SimpleDateFormat("yyyy/MM/dd");
				targetDate = dateFormatStr.format(date);
			}catch(ParseException ex){
				LogUtils.debugByMsg("年齢加算対象者一覧-日付変換エラー :" + targetDate);
				targetDate = null;
			}
		}
		return targetDate;
	 }
	 
	 
	/**
	 * 金額をのフォーマットをカンマ区切り+円のフォーマットへ変換する
	 * 
	 * @param targetPay
	 * 			変換前の金額
	 * @return 変換後の金額
	 *  
	 */
	 private String changePayFormat(String targetPay){
		 if(StringUtils.isEmpty(targetPay)){
			 targetPay = null;
		 }else{
			// 数値のカンマ区切り設定
			NumberFormat nfNum = NumberFormat.getNumberInstance();
			targetPay = nfNum.format(Long.parseLong(targetPay));
			targetPay = targetPay + "円";
		 }
		 return targetPay;
	 }
	 
	 
		/**
		 * 現在の日時を取得する
		 * 
		 * @param nowTime
		 * 			現在日時
		 */	
	 public Date getSystemDate(){
	        List<SkfBaseBusinessLogicUtilsGetSystemDateTimeExp> systemDate = new ArrayList<SkfBaseBusinessLogicUtilsGetSystemDateTimeExp>();
	        systemDate = skfBaseBusinessLogicUtilsGetSystemDateTimeExpRepository.getSystemDateTime();
	        Date nowTime = systemDate.get(0).getSystemdate();
	        return nowTime;
	 }
	 
	 
	 
	/**
	 * 年齢加算対象者データ一覧を取得
	 * 
	 * @param commonDto
	 * 			DTO
	 * @param sysDate
	 * 			システム処理年月
	 * @param processPattern
	 * 			処理パターン（"0"：初期化、"1"：検索）
	 */	
	public void setGrvAgeKasanInfo(Skf3060Sc001CommonDto commonDto, String sysDate, String processPattern){
        // 検索条件をもとに検索結果カウントの取得
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();

		int maxCount = Integer.parseInt(listTableSearchMaxCount);
		// 表示件数を取得
		int listCount = getListTableData(
				commonDto.getBaseTermFrom(), 
				commonDto.getBaseTermTo(), 
				commonDto.getShainNo(), 
				commonDto.getName(), 
				commonDto.getSelectedOriginalCompanyCd(), 
				commonDto.getSelectedSalaryCompanyCd(), 
				commonDto.getSelectedSendMailStatus(), 
				SEARCH_PATTERN_COUNT, 
				sysDate,
				listTableData);
		if(listCount == 0){
			// 取得レコード0件（初期表示の時はWarningメッセージを出さない）
			LogUtils.debugByMsg("年齢加算対象者データ：0件");
			if(PROCESS_PATTERN_SEARCH.equals(processPattern)){
				ServiceHelper.addWarnResultMessage(commonDto, MessageIdConstant.W_SKF_1007, String.valueOf(listCount));
			}
	        // CSV出力ボタン、メール送信ボタンを非活性化
			commonDto.setCsvOutButtonDisabled(true);
			commonDto.setSendMailButtonDisabled(true);
		}else if(maxCount < listCount){
			// 検索結果が指定された件数よりも多い（初期表示の時はWarningメッセージを出さない）
			LogUtils.debugByMsg("年齢加算対象者データ：　最大件数（" + maxCount + ") < 取得件数（" + listCount + "）");
			if(PROCESS_PATTERN_SEARCH.equals(processPattern)){
				ServiceHelper.addErrorResultMessage(commonDto, null, MessageIdConstant.E_SKF_1046, maxCount);			
			}
	        // CSV出力ボタン、メール送信ボタンを非活性化
			commonDto.setCsvOutButtonDisabled(true);
			commonDto.setSendMailButtonDisabled(true);
		}else{
			// 最大行数を設定
			commonDto.setListTableMaxRowCount(listTableMaxRowCount);
	        // CSV出力ボタン、メール送信ボタンを活性化
			commonDto.setCsvOutButtonDisabled(false);
			commonDto.setSendMailButtonDisabled(false);
	        
	        // データ取得
			getListTableData(
					commonDto.getBaseTermFrom(), 
					commonDto.getBaseTermTo(), 
					commonDto.getShainNo(), 
					commonDto.getName(), 
					commonDto.getSelectedOriginalCompanyCd(), 
					commonDto.getSelectedSalaryCompanyCd(), 
					commonDto.getSelectedSendMailStatus(),
					SEARCH_PATTERN_DATA, 
					sysDate,
					listTableData);	        
		}
		commonDto.setListTableData(listTableData);		
	}
	 
	 
	/**
	 * 年齢加算対象者一覧を取得する。<br>
	 * 
	 * @param baseTermFrom
	 * 			基準期間From
	 * @param baseTermTo
	 * 			基準期間To
	 * @param shainNo
	 * 			社員番号
	 * @param shainName
	 * 			社員名
	 * @param originalCompanyCd
	 * 			原籍会社コード
	 * @param salaryCompanyCd
	 * 			給与支給会社コード
	 * @param sendMailStatus
	 * 			送信状態
	 * @param systemDate
	 * 			処理年月
	 * @param searchFlag
	 * 			検索パターン
	 * @param listTableData
	 * 			空のリストテーブル
	 * @return リストテーブルに出力するリスト
	 *  
	 */
	public int getListTableData(String baseTermFrom, String baseTermTo, String shainNo, String shainName,
			String originalCompanyCd, String salaryCompanyCd, String sendMailStatus, String searchFlag, String systemDate, List<Map<String, Object>> listTableData) {
	
		int resultCount = 0;
		
		if(Objects.equals(searchFlag, SEARCH_PATTERN_COUNT)){
			// データ件数取得
			List<Skf3060Sc001GetAgeKasanTargetCountExp> dataCount = new ArrayList<Skf3060Sc001GetAgeKasanTargetCountExp>();
			Skf3060Sc001GetAgeKasanTargetCountExpParameter param = new Skf3060Sc001GetAgeKasanTargetCountExpParameter();	
			// 基準期間to
			param.setRecodeDadeto(replaceDateFormat(baseTermTo));
			// 年齢加算基準日(未使用？VB版ではSQL検索で使用していない)
			param.setKasanStartDate(createKasanStartDate(baseTermTo));
			// 社員番号
			param.setShainNo(escapeString(shainNo));
			// 社員氏名
			param.setShainName(escapeString(shainName));
			// 原籍会社
			param.setOriginalCompanyCd(originalCompanyCd);
			// 給与支給会社
			param.setSalaryCompanyCd(salaryCompanyCd);
			// 送信状態
			param.setSendMailStatus(sendMailStatus);
			// 処理年月日
			param.setSystemDate(systemDate);
			dataCount = skf3060Sc001GetAgeKasanTargetCountExpRepository.getAgeKasanTargetCount(param);
			// 件数取得
			Skf3060Sc001GetAgeKasanTargetCountExp tmpData = dataCount.get(0);
			resultCount = tmpData.getDataCount();
			
		}else{
			// データ取得
			List<Skf3060Sc001GetAgeKasanTargetDataInfoExp> resultListTableData = new ArrayList<Skf3060Sc001GetAgeKasanTargetDataInfoExp>();
			Skf3060Sc001GetAgeKasanTargetDataInfoExpParameter param = new Skf3060Sc001GetAgeKasanTargetDataInfoExpParameter();			
			// 基準期間to
			param.setRecodeDadeto(replaceDateFormat(baseTermTo));
			// 年齢加算基準日(未使用？VB版ではSQL検索で使用していない)
			param.setKasanStartDate(createKasanStartDate(baseTermTo));
			// 社員番号
			param.setShainNo(escapeString(shainNo));
			// 社員氏名
			param.setShainName(escapeString(shainName));
			// 原籍会社
			param.setOriginalCompanyCd(originalCompanyCd);
			// 給与支給会社
			param.setSalaryCompanyCd(salaryCompanyCd);
			// 送信状態
			param.setSendMailStatus(sendMailStatus);
			// 処理年月日
			param.setSystemDate(systemDate);
			resultListTableData = skf3060Sc001GetAgeKasanTargetDataInfoExpRepository.getAgeKasanTargetDataInfo(param);
			// 件数取得
			resultCount = resultListTableData.size();
			if(resultCount == 0){
				// 取得結果が0件
				return 0;
			}
			// リストテーブルに出力するリストを生成する
			listTableData.clear();
			listTableData.addAll(getListTableDataViewColumn(resultListTableData, createKasanStartDate(baseTermTo)));
		}
		
		return resultCount;		
	}


	/**
	 * リストテーブルに出力するリストを取得する。
	 * 
	 * @param originList
	 * @param kasanStartDate
	 * @return リストテーブルに出力するリスト
	 */
	public List<Map<String, Object>> getListTableDataViewColumn(List<Skf3060Sc001GetAgeKasanTargetDataInfoExp> originList, String kasanStartDate) {
		List<Map<String, Object>> setViewList = new ArrayList<Map<String, Object>>();

		// 用途（SKF1072）
		Map<String, String> auseKbnList = new HashMap<String, String>();
		auseKbnList = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_AUSE_KBN);

		// 役員区分（SKF1083）
		Map<String, String> yakuinKbnList = new HashMap<String, String>();
		yakuinKbnList = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_YAKUIN_KBN);

		
		for (int i = 0; i < originList.size(); i++) {
			
			Skf3060Sc001GetAgeKasanTargetDataInfoExp tmpData = originList.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			// メールチェックボックス（設定しない）
			// 原籍会社名（col2）
			tmpMap.put("col2", tmpData.getOriginalName());
			// 給与支給会社名（col3）
			tmpMap.put("col3", tmpData.getPayName());
			// 社員番号（col4）
			tmpMap.put("col4", tmpData.getShainNo());
			// 氏名（col5）
			tmpMap.put("col5", tmpData.getName());
			// 生年月日（col6）
			tmpMap.put("col6", changeDateFormat(tmpData.getBirthday()));
			// 年齢（col7）
			tmpMap.put("col7", tmpData.getAge());
			// 社宅名（col8）
			tmpMap.put("col8", tmpData.getShatakuName());
			// 部屋番号（col9）
			tmpMap.put("col9", tmpData.getRoomNo());
			// 現使用料（col10）
			tmpMap.put("col10", changePayFormat(tmpData.getRentalMonth()));
			//tmpMap.put("col10", changePayFormat(""));
			
			// 年齢加算使用料（col11）
			SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp shatakuRentCalcInput = new SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp();
			SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp shatakuRentCalcOutput = new SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp();
			String ageKasanRental = "";
			try {
				// 社宅使用料計算用のパラメータを設定
				// - 処理年月(kasanStartDate)
				shatakuRentCalcInput.setShoriNengetsu(kasanStartDate);
				// - 処理区分(2)
				shatakuRentCalcInput.setShoriKbn("2");
				// - 使用料パターンID()
				shatakuRentCalcInput.setShiyouryouPatternId(tmpData.getRentalPatternId());
				// - 役員区分()
				shatakuRentCalcInput.setYakuinKbn(tmpData.getYakuinSannteiKbn());
				// - 社宅賃貸料()
				shatakuRentCalcInput.setShatakuChintairyou(tmpData.getRentalMonth());
				// - 生年月日()
				shatakuRentCalcInput.setSeinengappi(tmpData.getBirthday());
				// 社宅使用料計算を呼び出し
				shatakuRentCalcOutput = skfBaseBusinessLogicUtils.getShatakuShiyouryouKeisan(shatakuRentCalcInput);
				BigDecimal zero = new BigDecimal(0);
				if(shatakuRentCalcOutput.getShatakuShiyouryouGetsugaku().compareTo(zero) == 1){
					// 社宅使用料が0円以上なら、年齢加算した金額を設定
					ageKasanRental = shatakuRentCalcOutput.getShatakuShiyouryouGetsugaku().toPlainString();
				}else{
					ageKasanRental = tmpData.getAgeKasanRentalMonth();
				}
			} catch (ParseException e) {
				LogUtils.debugByMsg("年齢加算使用料 算出エラー" + tmpData.getAgeKasanRentalMonth());
				ageKasanRental = "0";
			}
			tmpMap.put("col11", changePayFormat(ageKasanRental));
			// 解放
			shatakuRentCalcInput = null;
			shatakuRentCalcOutput = null;
			
			// 送信状況（送信日）（col12）
			tmpMap.put("col12", changeDateFormat(tmpData.getSendMailDate()));
			// 入居日（col13）
			tmpMap.put("col13", changeDateFormat(tmpData.getNyukyoDate()));
			// 退居日（col14）
			tmpMap.put("col14", changeDateFormat(tmpData.getTaikyoDate()));
			// 用途（col15）
			tmpMap.put("col15", auseKbnList.get(tmpData.getAuse()));
			// 役員区分（col16）
			String yakuinKbn = null;
			if(StringUtils.isNotEmpty(tmpData.getYakuinSannteiKbn())){
				if(!Objects.equals(tmpData.getYakuinSannteiKbn(), "0")){
					yakuinKbn = yakuinKbnList.get(tmpData.getYakuinSannteiKbn());
				}
			}
			tmpMap.put("col16", yakuinKbn);
			// 所属（col17）
			String syozoku = "";
			if(StringUtils.isNotEmpty(tmpData.getAgencyName())){
				syozoku = syozoku + tmpData.getAgencyName();
			}
			if(StringUtils.isNotEmpty(tmpData.getAffiliation1Name())){
				if(syozoku.length() != 0){
					syozoku = syozoku + " ";
				}
				syozoku = syozoku + tmpData.getAffiliation1Name();
			}
			if(StringUtils.isNotEmpty(tmpData.getAffiliation2Name())){
				if(syozoku.length() != 0){
					syozoku = syozoku + " ";
				}
				syozoku = syozoku + tmpData.getAffiliation2Name();
			}
			if(syozoku.length() == 0){
				syozoku = null;
			}
			tmpMap.put("col17", syozoku);

			// チェックボックスの制御が必要
			// 仮社員番号の場合はチェックボックス非活性
			if(KARI_SHAIN_NO.equals(tmpData.getShainNo().substring(0,1))){
				tmpMap.put("col18", "true");
			}else{
				if(StringUtils.isEmpty(tmpData.getSendMailDate())){
					// 送信日付が入っていない場合
					tmpMap.put("col18", "false");
				}else{
					// 送信日付が入っている場合
					tmpMap.put("col18", "false");
				}
			}
			setViewList.add(tmpMap);
		}
		return setViewList;
	}
	

	/**
	 * 入力チェックを行う.
	 * 
	 * @param commonDto *indexメソッドの引数のDTO。入力チェックエラーの場合、エラーメッセージを詰める。
	 * @throws ParseException 日付フォーマット変換エラー
	 */
	public boolean isValidateInput(Skf3060Sc001CommonDto commonDto) throws ParseException {

		boolean isCheckOk = true;
		String debugMessage = "";
		
		/** 基準期間 **/
		// 必須入力チェック
		// - 基準期間From
		if(NfwStringUtils.isEmpty(commonDto.getBaseTermFrom())){
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(commonDto, null, MessageIdConstant.E_SKF_1048, "基準期間(From)");
			debugMessage += "　必須入力チェック - " + "基準期間(From)";
			commonDto.setBaseTermFromErr(validationErrorCode);
		}
		// - 基準期間To
		if(NfwStringUtils.isEmpty(commonDto.getBaseTermTo())){
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(commonDto, null, MessageIdConstant.E_SKF_1048, "基準期間(To)");
			debugMessage += "　必須入力チェック - " + "基準期間(To)";
			commonDto.setBaseTermToErr(validationErrorCode);
		}
		
		// 日付形式のチェック
		// - 基準期間From
		if(NfwStringUtils.isNotEmpty(commonDto.getBaseTermFrom())){
			if(!SkfCheckUtils.isSkfDateFormat(commonDto.getBaseTermFrom(),CheckUtils.DateFormatType.YYYYMMDD)){
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(commonDto, null, MessageIdConstant.E_SKF_1042, "基準期間(From)");
				debugMessage += "　日付形式チェック - " + "基準期間(From)";
				commonDto.setBaseTermFromErr(validationErrorCode);
			}
		}
		// - 基準期間To
		if(NfwStringUtils.isNotEmpty(commonDto.getBaseTermTo())){
			if(!SkfCheckUtils.isSkfDateFormat(commonDto.getBaseTermTo(),CheckUtils.DateFormatType.YYYYMMDD)){
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(commonDto, null, MessageIdConstant.E_SKF_1042, "基準期間(To)");
				debugMessage += "　日付形式チェック - " + "基準期間(To)";
				commonDto.setBaseTermToErr(validationErrorCode);
			}
		}
		
		// 基準期間From < To整合性
        Date fromDate = null;
        Date toDate = null;
        int diff = 0;
        if (true == isCheckOk) {
        	// isCheckOkがTrue（エラーがない）の時のみ実施
            SimpleDateFormat sdf = new SimpleDateFormat(SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
            fromDate = sdf.parse(commonDto.getBaseTermFrom());
            toDate = sdf.parse(commonDto.getBaseTermTo());
            diff = fromDate.compareTo(toDate);
            if (diff > 0) {
				isCheckOk = false;
                ServiceHelper.addErrorResultMessage(commonDto, null, MessageIdConstant.E_SKF_1133, "基準期間");
//                commonDto.setBaseTermFromErr(validationErrorCode);
                commonDto.setBaseTermToErr(validationErrorCode);
            }
        }

        // デバッグメッセージ出力
		if (isCheckOk) {
			LogUtils.debugByMsg("入力チェックOK：" + debugMessage);
		} else {
			LogUtils.debugByMsg("入力チェックエラー：" + debugMessage);
		}		
		
		return isCheckOk;
	}	
	
	
	
	
	
	


	
}
