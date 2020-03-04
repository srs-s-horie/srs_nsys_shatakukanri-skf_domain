/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3060.domain.service.skf3060sc001;

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
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShain;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShainKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3060TAgeKasanSendmail;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MShainRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3060TAgeKasanSendmailRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwSendMailUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3060.domain.service.skf3060sc001.Skf3060Sc001SharedService;
import jp.co.intra_mart.mirage.integration.guice.Transactional;
import jp.co.c_nexco.skf.skf3060.domain.dto.skf3060sc001.Skf3060Sc001SendMailDto;

/**
 * Skf3060Sc001SendMailService 事業領域名マスタ登録画面のSendMailサービス処理クラス。　 
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3060Sc001SendMailService extends BaseServiceAbstract<Skf3060Sc001SendMailDto> {

	@Autowired
	Skf3060Sc001SharedService skf3060Sc001SharedService;
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	
	@Autowired
	private Skf3060TAgeKasanSendmailRepository skf3060TAgeKasanSendmailRepository;
	
	@Autowired
	private Skf1010MShainRepository skf1010MShainRepository;
	
    @Value("${skf.common.validate_error}")
    private String validationErrorCode;	
    
    private String AGE_40 = "40";
    
    /** メールテンプレートID */
    private final String MAIL_TEMPLATE_ID = "SKF_ML60";
    
	/**
	 * サービス処理を行う。　
	 * 
	 * @param sendMailDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	@Transactional
	public Skf3060Sc001SendMailDto index(Skf3060Sc001SendMailDto sendMailDto) throws Exception {
		
		sendMailDto.setPageTitleKey(MessageIdConstant.SKF3060_SC001_TITLE);
 		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("メール送信", CodeConstant.C001, sendMailDto.getPageId());
		
		// エラー状態クリア
		sendMailDto.setBaseTermFromErr("");
		sendMailDto.setBaseTermToErr("");
		
		// ドロップダウンリスト再設定
		// - 原籍会社名
		// - 給与支給会社名
		// - 送信状態
		List<Map<String, Object>> originalCompanyList = sendMailDto.getOriginalCompanyCdList();
		for(int i=0; i<originalCompanyList.size(); i++){
			Map<String, Object> originalCompany = originalCompanyList.get(i);
			if(true == originalCompany.containsKey("selected")){
				originalCompanyList.get(i).remove("selected");
			}
			if(Objects.equals(sendMailDto.getSelectedOriginalCompanyCd(), originalCompany.get("value").toString())){
				 originalCompanyList.get(i).put("selected", true);
			}
		}
		List<Map<String, Object>> salaryCompanyList = sendMailDto.getSalaryCompanyCdList();
		for(int i=0; i<salaryCompanyList.size(); i++){
			Map<String, Object> salaryCompany = salaryCompanyList.get(i);
			if(true == salaryCompany.containsKey("selected")){
				salaryCompanyList.get(i).remove("selected");
			}
			if(Objects.equals(sendMailDto.getSelectedSalaryCompanyCd(), salaryCompany.get("value").toString())){
				salaryCompanyList.get(i).put("selected", true);
			}
		}
		List<Map<String, Object>> sendMailStatusList = sendMailDto.getSendMailStatusList();
		for(int i=0; i<sendMailStatusList.size(); i++){
			Map<String, Object> sendMailStatus = sendMailStatusList.get(i);
			if(true == sendMailStatus.containsKey("selected")){
				sendMailStatusList.get(i).remove("selected");
			}
			if(Objects.equals(sendMailDto.getSelectedSendMailStatus(), sendMailStatus.get("value").toString())){
				sendMailStatusList.get(i).put("selected", true);
			}
		}

		/*
		List<Map<String, Object>> originalCompanyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> salaryCompanyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sendMailStatusList = new ArrayList<Map<String, Object>>();
        skf3060Sc001SharedService.getDoropDownList(sendMailDto.getSelectedOriginalCompanyCd(),originalCompanyList,
        		sendMailDto.getSelectedSalaryCompanyCd(), salaryCompanyList,
        		sendMailDto.getSelectedSendMailStatus(), sendMailStatusList);
        sendMailDto.setOriginalCompanyCdList(originalCompanyList);
        sendMailDto.setSalaryCompanyCdList(salaryCompanyList);
        sendMailDto.setSendMailStatusList(sendMailStatusList);
        */
		
        // メール送信データ
        if(NfwStringUtils.isEmpty(sendMailDto.getMailSendData())){
			// チェックなし
            ServiceHelper.addErrorResultMessage(sendMailDto, null, MessageIdConstant.E_SKF_3057, "年齢加算に伴う社宅使用料変更通知");
			LogUtils.debugByMsg("送信対象データなし");
        	
        }else{
    		String[] mailSendList = sendMailDto.getMailSendData().split(";");
	        if(null == mailSendList){
	        	// 何かしらのエラーが発生
	            ServiceHelper.addErrorResultMessage(sendMailDto, null, MessageIdConstant.E_SKF_1078, "メール送信");
				LogUtils.debugByMsg("メール送信中にエラー発生");
	        }else{
	        
	        	List<Integer> errorList = new ArrayList<Integer>();
	        	String resultStr = createSendMailData(sendMailDto, mailSendList, errorList);
	        	if(NfwStringUtils.isEmpty(resultStr)){
	        		// 戻り値のメッセージが空
	        		if(errorList.size() == 0){
	        			// エラーなのにエラー情報がない（そんなパターンは存在しないが一応。。。）
    					ServiceHelper.addErrorResultMessage(sendMailDto, null, MessageIdConstant.E_SKF_1078, "メール送信");
	        		}else{
	        			int errorCode = errorList.get(0);
	        			if(errorCode == 0){
	        				// error.skf.e_skf_1075
	    					ServiceHelper.addErrorResultMessage(sendMailDto, null, MessageIdConstant.E_SKF_1075);
	        			}else if(errorCode == -1){
	        				// warning.skf.w_skf_1009
	    					ServiceHelper.addErrorResultMessage(sendMailDto, null, MessageIdConstant.W_SKF_1009);
	        			}else if(errorCode == -9){
	        				// error.skf.e_skf_1078　メール送信
	    					ServiceHelper.addErrorResultMessage(sendMailDto, null, MessageIdConstant.E_SKF_1078, "メール送信");
	        			}
	        		}
					// 例外時はthrowしてRollBack
					throwBusinessExceptionIfErrors(sendMailDto.getResultMessages());
	        	}else{
	        		// 完了メッセージを表示する
					//ServiceHelper.addResultMessage(sendMailDto, resultStr);
					LogUtils.debugByMsg(resultStr);

					// 再検索
					// 入力エラーチェック
					if(true == skf3060Sc001SharedService.isValidateInput(sendMailDto)){
						// エラーなし
				        // 検索条件のセット
						// --　システム年月を取得
						String setGrvAgeKasanInfo = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu();
						
						// 検索実行
				        // - 年齢加算対象者データ一覧を取得
						skf3060Sc001SharedService.setGrvAgeKasanInfo(sendMailDto, setGrvAgeKasanInfo, 
								skf3060Sc001SharedService.PROCESS_PATTERN_SEARCH);			
					}
	        	}
	        }
        }
		
        // 最後に初期化
		sendMailDto.setMailSendData(null);
		return sendMailDto;
	}	
	
	
	/**
	 * メール送信処理　
	 * 
	 * @param sendMailDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	private String createSendMailData(Skf3060Sc001SendMailDto sendMailDto, String[] mailSendList, List<Integer> errorList){
		// メッセージ
		StringBuilder retMessage = new StringBuilder();
		String message = "";
		
        //送付日の取得
        Date nowTime = skf3060Sc001SharedService.getSystemDate();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(nowTime);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String soufuDate = sdf.format(calendar.getTime()).toString();
        //システム日時の取得
//		sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
//        String sysDateTime = sdf.format(calendar);

        // 送信成功件数
        int okCount = 0;
        // 送信失敗件数
        int ngCount = 0;
		
        // 検索結果一覧の中からチェックが入っているデータについてメール送信
        for(int rowIndex = 0; rowIndex < mailSendList.length; rowIndex++){
        	String checkStatus = mailSendList[rowIndex];
        	// チェックが入った行データの取得
        	Map<String, Object> listTableData = sendMailDto.getListTableData().get(Integer.parseInt(checkStatus) - 1);
        	
        	// 社員番号
        	String shainNo = listTableData.get("col4").toString();
        	// 年齢
        	String age = listTableData.get("col7").toString();
        	// データチェック
        	Skf3060TAgeKasanSendmail selectData = new Skf3060TAgeKasanSendmail();
        	selectData = skf3060TAgeKasanSendmailRepository.selectByPrimaryKey(shainNo);

        	// メールアドレスの取得
        	String mailAddress = "";
        	String shainNoShape = "";
        	String checkAsterisk = shainNo.substring(0, shainNo.length() - 1);
        	if(CodeConstant.ASTERISK.equals(checkAsterisk)){
        		// 末尾に*がある
        		shainNoShape = shainNo.substring(shainNo.length() - 2);
        	}else{
        		// 末尾に*がない場合
        		shainNoShape = shainNo;
        	}
        	Skf1010MShainKey key = new Skf1010MShainKey();
        	key.setCompanyCd(CodeConstant.C001);
        	key.setShainNo(shainNoShape);
        	Skf1010MShain shainData = new Skf1010MShain();
        	shainData = skf1010MShainRepository.selectByPrimaryKey(key);
        	mailAddress = shainData.getMailAddress();
        	
         	// メール情報
        	try{
            	boolean result = sendMail(mailAddress, sendMailDto.getHdnWareki());
            	if(true == result){
            		// 送信成功
            		okCount += 1;
					ServiceHelper.addResultMessage(sendMailDto, MessageIdConstant.I_SKF_3081, String.valueOf(shainNo));
            		message = java.text.MessageFormat.format(MessageIdConstant.I_SKF_3081, String.valueOf(shainNo));
            		retMessage.append(message);
            	}else{
            		// 送信失敗
            		ngCount += 1;
					ServiceHelper.addResultMessage(sendMailDto, MessageIdConstant.I_SKF_3080, shainNo.toString());
            		message = java.text.MessageFormat.format(MessageIdConstant.I_SKF_3080, String.valueOf(shainNo));
            		retMessage.append(message);
            		continue;
            	}
        	}catch(Exception ex){
        		errorList.add(0,-9);
        		return null;
        	}
        	
        	int retCount = 0;
    		Skf3060TAgeKasanSendmail record = new Skf3060TAgeKasanSendmail();
        	if(selectData == null){
        		// 登録処理の呼び出し
        		// 社員番号
        		record.setShainNo(shainNo);
        		// 年齢区分チェック
        		if(AGE_40.equals(age)){
        			// 40歳
        			record.setSendMailDate1(soufuDate);
        		}else{
        			// 50歳
        			record.setSendMailDate2(soufuDate);
        		}
        		// 削除フラグ
        		record.setDeleteFlag("0");
        		retCount = skf3060TAgeKasanSendmailRepository.insertSelective(record);
        	}else{
        		// 年齢アラート通知テーブルを更新
        		Skf3060TAgeKasanSendmail selectRecord = new Skf3060TAgeKasanSendmail();
        		selectRecord = skf3060TAgeKasanSendmailRepository.selectByPrimaryKey(shainNo);
        		if(selectRecord != null){
    				// 日付変換
    				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
            		String checkUpdateDate = dateFormat.format(selectRecord.getUpdateDate());
    				Date mapDate = null;
    				try{
    					mapDate = dateFormat.parse(checkUpdateDate);
    				}catch(ParseException ex){
    					// 誰かに先に更新されちゃった
    					LogUtils.debugByMsg("年齢加算対象者一覧 メール送信-更新日時変換エラー :" + checkUpdateDate);
    					// 異常発生
    					errorList.add(0,-1);
    					return null;
    				}			
    				super.checkLockException(mapDate, selectData.getUpdateDate());			
    				// 社員番号
    				record.setShainNo(shainNo);
            		// 年齢区分チェック
            		if(AGE_40.equals(age)){
            			// 40歳
            			record.setSendMailDate1(soufuDate);
            			record.setSendMailDate2(selectRecord.getSendMailDate2());
            		}else{
            			// 50歳
            			record.setSendMailDate1(selectRecord.getSendMailDate1());
            			record.setSendMailDate2(soufuDate);
            		}
            		// 削除フラグ
            		record.setDeleteFlag("0");
            		// 登録日時
            		record.setInsertDate(selectRecord.getInsertDate());
            		//　登録者
            		record.setInsertUserId(selectRecord.getInsertUserId());
            		// 登録機能ID
            		record.setInsertProgramId(selectRecord.getInsertProgramId());
    				retCount = skf3060TAgeKasanSendmailRepository.updateByPrimaryKey(record);
    				if(retCount == 0){
    					// 消されちゃった
    					errorList.add(0,0);
    					return null;
    				}else if(retCount == -1){
    					// 異常発生
    					errorList.add(0,-1);
    					return null;
    				}
        		}else{
        			// 異常終了
            		return null;
        			
        		}
        		
        	}
        }
        
    	// メール送信結果
		ServiceHelper.addResultMessage(sendMailDto, MessageIdConstant.I_SKF_3082, String.valueOf(ngCount));
		message = java.text.MessageFormat.format(MessageIdConstant.I_SKF_3082, String.valueOf(ngCount));
		retMessage.append(message);        
		ServiceHelper.addResultMessage(sendMailDto, MessageIdConstant.I_SKF_3083, String.valueOf(okCount));
		message = java.text.MessageFormat.format(MessageIdConstant.I_SKF_3083, String.valueOf(okCount));
		retMessage.append(message);        
		int all = ngCount + okCount;
		ServiceHelper.addResultMessage(sendMailDto, MessageIdConstant.I_SKF_3084, String.valueOf(all));
		message = java.text.MessageFormat.format(MessageIdConstant.I_SKF_3084, String.valueOf(all));
		retMessage.append(message);        
		
		return retMessage.toString();
	}
	
	
    /**
     * メールを送信する
     * 
     * @param mailTo 送信先
     * @param wareki 西暦年度
     * @throws Exception
     */
	private boolean sendMail(String mailTo, String wareki) throws Exception {
		
		boolean resultFlag = true;
		if(NfwStringUtils.isEmpty(mailTo)){
			// メールアドレスがない場合は送らない
			// NfwSendMailUtils.sendMailにメールアドレスNULLとか空文字で渡すとExceptionが発生する
			resultFlag = false;
		}else{
	        // メール送信データ
	        Map<String, String> replaceMap = new HashMap<String, String>();
			// メール送信元
	        replaceMap.put("【from】", StringUtils.defaultString(""));
			// メール送信先
	        replaceMap.put("【to】", StringUtils.defaultString(mailTo));
	        // 半角、全角の変換
	        String halfWidthYear = wareki.substring(0, 4);
	        String fullWidthYear = changeNumHalfToFull(halfWidthYear);
	        // メール本文
	        replaceMap.put("【wareki】", StringUtils.defaultString(fullWidthYear));
	        
	        // メール送信
	        resultFlag = NfwSendMailUtils.sendMail(this.MAIL_TEMPLATE_ID, replaceMap);
		}
        
		return resultFlag;
	}
	
	
    /**
     * 半角文字を全角文字に変換
     * 
     * @param str 変換対象文字
     */
	private String changeNumHalfToFull(String str) {
		String result = null;
		if(str != null) {
		    StringBuilder sb = new StringBuilder(str);
		    for (int i = 0; i < sb.length(); i++) {
		        int c = (int) sb.charAt(i);
		        if (c >= 0x30 && c <= 0x39) {
		            sb.setCharAt(i, (char) (c + 0xFEE0));
		        }
		    }
		    result = sb.toString();
		}
	    return result;
	}	
	
}
