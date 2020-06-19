/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3060.domain.service.skf3060sc001;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationGuideUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3060.domain.dto.skf3060sc001.Skf3060Sc001InitDto;

/**
 * Skf3060Sc001InitService 年齢加算対象者一覧画面のInitサービス処理クラス。　 
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3060Sc001InitService extends SkfServiceAbstract<Skf3060Sc001InitDto> {
	

	@Autowired
	Skf3060Sc001SharedService skf3060Sc001SharedService;
	
    @Autowired
    private SkfOperationGuideUtils skfOperationGuideUtils;
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;

	
	// リストテーブルの１ページ最大表示行数
	@Value("${skf3060.skf3060_sc001.max_row_count}")
	private String listTableMaxRowCount;	    
	// 最大検索数
	@Value("${skf3060.skf3060_sc001.search_max_count}")
	private String listTableSearchMaxCount;	    
    
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
	public Skf3060Sc001InitDto index(Skf3060Sc001InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF3060_SC001_TITLE);
 		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, FunctionIdConstant.SKF3060_SC001);
		
        // 操作ガイド取得
        initDto.setOperationGuide(skfOperationGuideUtils.getOperationGuide(FunctionIdConstant.SKF3060_SC001));
		
        // 検索条件のセット
        // - 基準期間の設定
        //// --　システム処理年月を取得
		String sysProcessDate = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu();
		// --　システム年月を取得
        Date nowTime = skf3060Sc001SharedService.getSystemDate();
		List <Integer> yearList = new ArrayList <Integer>();
		skf3060Sc001SharedService.getBaseYear(nowTime, yearList);
		/*
		SimpleDateFormat dateFormatFrom = new SimpleDateFormat("yyyyMM");
		Date dateFrom = null;
		int yearFrom = 0;
		int yearTo = 0;
		try{
			dateFrom = dateFormatFrom.parse(sysDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dateFrom);
			// 年（From）の取り出し
			yearFrom = calendar.get(Calendar.YEAR);
			// 年（To）の取り出し
			calendar.add(Calendar.YEAR, 1);
			yearTo = calendar.get(Calendar.YEAR);
		}catch(ParseException ex){
			LogUtils.debugByMsg("年齢加算対象者一覧-日時変換失敗 :" + sysDate);
			yearFrom = 0;
			yearTo = 0;
		}
		*/
		// --　FromとToに日付を設定
		initDto.setBaseTermFromErr("");
		if(yearList.get(0) != 0){
			initDto.setBaseTermFrom(String.valueOf(yearList.get(0)) + skf3060Sc001SharedService.TERM_FROM_DATE);
		}else{
			initDto.setBaseTermFrom(null);
		}
		initDto.setBaseTermToErr("");
		if(yearList.get(1) != 0){
			initDto.setBaseTermTo(String.valueOf(yearList.get(1)) + skf3060Sc001SharedService.TERM_TO_DATE);
			initDto.setHdnKasanStartDate(String.valueOf(yearList.get(1)) + skf3060Sc001SharedService.TERM_TO_DATE);
	        // メール送信用の年度（西暦）を設定
			initDto.setHdnWareki(String.valueOf(yearList.get(1)) + skf3060Sc001SharedService.TERM_TO_DATE);
		}else{
			initDto.setBaseTermTo(null);
			initDto.setHdnKasanStartDate(null);
	        // メール送信用の年度（西暦）を設定
			initDto.setHdnWareki(null);
		}
        // リストの設定
        // - 送信状態は「未送付」を選択状態とする
        initDto.setSelectedSendMailStatus(skf3060Sc001SharedService.SEND_STATUS_NOT_SEND_CD);
		
        // - 原籍会社リストの設定
        // - 給与支給会社名リストの設定
        // - 送信状態の設定
		List<Map<String, Object>> originalCompanyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> salaryCompanyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sendMailStatusList = new ArrayList<Map<String, Object>>();
        skf3060Sc001SharedService.getDoropDownList(initDto.getSelectedOriginalCompanyCd(),originalCompanyList,
        		initDto.getSelectedSalaryCompanyCd(), salaryCompanyList,
        		initDto.getSelectedSendMailStatus(), sendMailStatusList);
        initDto.setOriginalCompanyCdList(originalCompanyList);
        initDto.setSalaryCompanyCdList(salaryCompanyList);
        initDto.setSendMailStatusList(sendMailStatusList);
        
        //年齢加算対象者データ一覧を取得
        skf3060Sc001SharedService.setGrvAgeKasanInfo(initDto, sysProcessDate, 
        		skf3060Sc001SharedService.PROCESS_PATTERN_INITIALIZE);
		
		return initDto;
	}
}
