/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3060.domain.service.skf3060sc001;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.nfw.webcore.utils.filetransfer.FileOutput;
import jp.co.c_nexco.nfw.webcore.utils.filetransfer.FileOutput.FileEncode;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3060.domain.service.skf3060sc001.Skf3060Sc001SharedService;
import jp.co.c_nexco.skf.skf3060.domain.dto.skf3060sc001.Skf3060Sc001DownloadDto;

/**
 * Skf3060Sc001DownloadService 事業領域名マスタ登録画面のDownloadサービス処理クラス。　 
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3060Sc001DownloadService extends BaseServiceAbstract<Skf3060Sc001DownloadDto> {

	@Autowired
	Skf3060Sc001SharedService skf3060Sc001SharedService;
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
    
	// 最大検索数
	@Value("${skf3060.skf3060_sc001.search_max_count}")
	private String listTableSearchMaxCount;	

	// 最大検索数
	@Value("${skf3060.skf3060_sc001.csvFileName}")
	private String csvFileName;	

	@Value("${skf.common.validate_error}")
    private String validationErrorCode;	
    
	private String fileName = "skf3060.skf3060_sc001.FileId";
	
	/**
	 * CSV出力用カンマ
	 */
	public String COMMA = ",";
	
	/**
	 * サービス処理を行う。　
	 * 
	 * @param searchDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public Skf3060Sc001DownloadDto index(Skf3060Sc001DownloadDto downloadDto) throws Exception {
		
		downloadDto.setPageTitleKey(MessageIdConstant.SKF3060_SC001_TITLE);
 		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("CSV出力", CodeConstant.C001, downloadDto.getPageId());
		
		// エラー状態クリア
		downloadDto.setBaseTermFromErr(null);
		downloadDto.setBaseTermToErr(null);
		
		// ドロップダウンリスト再設定
		// - 原籍会社名
		// - 給与支給会社名
		// - 送信状態
		List<Map<String, Object>> originalCompanyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> salaryCompanyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sendMailStatusList = new ArrayList<Map<String, Object>>();
        skf3060Sc001SharedService.getDoropDownList(downloadDto.getSelectedOriginalCompanyCd(),originalCompanyList,
        		downloadDto.getSelectedSalaryCompanyCd(), salaryCompanyList,
        		downloadDto.getSelectedSendMailStatus(), sendMailStatusList);
        downloadDto.setOriginalCompanyCdList(originalCompanyList);
        downloadDto.setSalaryCompanyCdList(salaryCompanyList);
        downloadDto.setSendMailStatusList(sendMailStatusList);
		
        // 入力チェック
        // ★現行、入力チェックしていない。よって、日付Toとか空にすると現行は落ちる。さすがに落ちるのはまずいので、日付の入力チェック入れる。
        if(true == isValidateInputDownload(downloadDto)){
        	// 入力チェックエラーなし
        	downloadDto.setErrorFlag(false);
        	
	        // 検索条件のセット
			// --　システム年月を取得
			String setGrvAgeKasanInfo = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu();
			
			// 検索実行
	        // - 年齢加算対象者データ一覧を取得（CSVボタン押下時に設定されていた検索条件で検索）
			if(setGrvAgeKasanInfoDownload(downloadDto, setGrvAgeKasanInfo)){
				
				// CSVデータ作成
				List<String[]> rowdatas = new ArrayList<String[]>();
				for(Map<String, Object> mapData : downloadDto.getListTableData()){
					// 原籍会社名
					String originalName = "";
					if(mapData.get("col2") != null && NfwStringUtils.isNotEmpty(mapData.get("col2").toString())){
						originalName = mapData.get("col2").toString();
					}
					// 給与支払い会社名
					String payName = "";
					if(mapData.get("col3") != null && NfwStringUtils.isNotEmpty(mapData.get("col3").toString())){
						payName = mapData.get("col3").toString();
					}
					// 社員番号
					String shainNo = "";
					if(mapData.get("col4") != null && NfwStringUtils.isNotEmpty(mapData.get("col4").toString())){
						shainNo = mapData.get("col4").toString();
					}
					// 氏名
					String name = "";
					if(mapData.get("col5") != null && NfwStringUtils.isNotEmpty(mapData.get("col5").toString())){
						name = mapData.get("col5").toString();
					}
					// 生年月日
					String birthDay ="";
					if(mapData.get("col6") != null && NfwStringUtils.isNotEmpty(mapData.get("col6").toString())){
						birthDay = skf3060Sc001SharedService.replaceDateFormat(mapData.get("col6").toString());
					}
					// 年齢
					String age = "";
					if(mapData.get("col7") != null && NfwStringUtils.isNotEmpty(mapData.get("col7").toString())){
						age = mapData.get("col7").toString();
					}
					// 社宅名
					String shatakuName = "";
					if(mapData.get("col8") != null && NfwStringUtils.isNotEmpty(mapData.get("col8").toString())){
						shatakuName = mapData.get("col8").toString();
					}
					// 部屋番号
					String roomNo = "";
					if(mapData.get("col9") != null && NfwStringUtils.isNotEmpty(mapData.get("col9").toString())){
						roomNo = mapData.get("col9").toString();
					}
					// 現使用料
					String rentalMonth = "";
					if(mapData.get("col10") != null && NfwStringUtils.isNotEmpty(mapData.get("col10").toString())){
						rentalMonth = mapData.get("col10").toString().replace(",", "").replace("円", "");
					}
					// 年齢加算使用料
					String ageKasanRentalMonth = "";
					if(mapData.get("col11") != null && NfwStringUtils.isNotEmpty(mapData.get("col11").toString())){
						ageKasanRentalMonth = mapData.get("col11").toString().replace(",", "").replace("円", "");
					}
					// 送信状況（送信日）
					String sendMailDate = "";
					if(mapData.get("col12") != null && NfwStringUtils.isNotEmpty(mapData.get("col12").toString())){
						sendMailDate = skf3060Sc001SharedService.replaceDateFormat(mapData.get("col12").toString());
					}
					// 入居日
					String nyukyoDate = "";
					if(mapData.get("col13") != null && NfwStringUtils.isNotEmpty(mapData.get("col13").toString())){
						nyukyoDate = skf3060Sc001SharedService.replaceDateFormat(mapData.get("col13").toString());
					}
					// 退居日
					String taikyoDate = "";
					if(mapData.get("col14") != null && NfwStringUtils.isNotEmpty(mapData.get("col14").toString())){
						taikyoDate = skf3060Sc001SharedService.replaceDateFormat(mapData.get("col14").toString());
					}
					// 用途
					String ause = "";
					if(mapData.get("col15") != null && NfwStringUtils.isNotEmpty(mapData.get("col15").toString())){
						ause = mapData.get("col15").toString();
					}
					// 役員区分
					String yakuinKbn = "";
					if(mapData.get("col16") != null && NfwStringUtils.isNotEmpty(mapData.get("col16").toString())){
						yakuinKbn = mapData.get("col16").toString();
					}
					// 所属
					String syozoku = "";
					if(mapData.get("col17") != null && NfwStringUtils.isNotEmpty(mapData.get("col17").toString())){
						syozoku = mapData.get("col17").toString();
					}
					
					String[] csvData = new String[]{
							originalName, 
							payName, 
							shainNo,
							name,
							birthDay, 
							age, 
							shatakuName, 
							roomNo, 
							rentalMonth, 
							ageKasanRentalMonth, 
							sendMailDate, 
							nyukyoDate, 
							taikyoDate, 
							ause, 
							yakuinKbn, 
							syozoku
							};
					rowdatas.add(csvData);
				}
				
				//CSV出力を行う
				FileOutput.fileOutputCsv(rowdatas, fileName, "skf3060fl001", 1, null, downloadDto,
						new FileOutput().new OutputFileCsvProperties(null, FileEncode.BOM_UTF8, null, null));
				String uploadFileName = DateTime.now().toString("YYYYMMdd") + "_" + csvFileName + ".csv";
				downloadDto.setUploadFileName(uploadFileName);
			}
        }else{
        	downloadDto.setErrorFlag(true);
        }
		
		return downloadDto;
	}	
	
	/**
	 * 入力チェックを行う.
	 * (Download用)
	 * @param commonDto *indexメソッドの引数のDTO。入力チェックエラーの場合、エラーメッセージを詰める。
	 * @throws ParseException 日付フォーマット変換エラー
	 */
	private boolean isValidateInputDownload(Skf3060Sc001DownloadDto downloadDto) throws ParseException {

		boolean isCheckOk = true;
		String debugMessage = "";
		
		/** 基準期間 **/
		// 必須入力チェック
		// - 基準期間From
		if(NfwStringUtils.isEmpty(downloadDto.getBaseTermFrom())){
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(downloadDto, null, MessageIdConstant.E_SKF_1048, "基準期間(From)");
			debugMessage += "　必須入力チェック - " + "基準期間(From)";
			downloadDto.setBaseTermFromErr(validationErrorCode);
		}
		// - 基準期間To
		if(NfwStringUtils.isEmpty(downloadDto.getBaseTermTo())){
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(downloadDto, null, MessageIdConstant.E_SKF_1048, "基準期間(To)");
			debugMessage += "　必須入力チェック - " + "基準期間(To)";
			downloadDto.setBaseTermToErr(validationErrorCode);
		}
		
		// 日付形式のチェック
		// - 基準期間From
		if(NfwStringUtils.isNotEmpty(downloadDto.getBaseTermFrom())){
			if(!SkfCheckUtils.isSkfDateFormat(downloadDto.getBaseTermFrom(),CheckUtils.DateFormatType.YYYYMMDD)){
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(downloadDto, null, MessageIdConstant.E_SKF_1042, "基準期間(From)");
				debugMessage += "　日付形式チェック - " + "基準期間(From)";
				downloadDto.setBaseTermFromErr(validationErrorCode);
			}
		}
		// - 基準期間To
		if(NfwStringUtils.isNotEmpty(downloadDto.getBaseTermTo())){
			if(!SkfCheckUtils.isSkfDateFormat(downloadDto.getBaseTermTo(),CheckUtils.DateFormatType.YYYYMMDD)){
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(downloadDto, null, MessageIdConstant.E_SKF_1042, "基準期間(To)");
				debugMessage += "　日付形式チェック - " + "基準期間(To)";
				downloadDto.setBaseTermToErr(validationErrorCode);
			}
		}
		
		// 基準期間From < To整合性
        Date fromDate = null;
        Date toDate = null;
        int diff = 0;
        if (true == isCheckOk) {
        	// isCheckOkがTrue（エラーがない）の時のみ実施
            //SimpleDateFormat sdf = new SimpleDateFormat(SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT);
            SimpleDateFormat sdf = new SimpleDateFormat(SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
            fromDate = sdf.parse(downloadDto.getBaseTermFrom());
            toDate = sdf.parse(downloadDto.getBaseTermTo());
            diff = fromDate.compareTo(toDate);
            if (diff > 0) {
				isCheckOk = false;
                ServiceHelper.addErrorResultMessage(downloadDto, null, MessageIdConstant.E_SKF_1133, "基準期間");
                downloadDto.setBaseTermFromErr(validationErrorCode);
                downloadDto.setBaseTermToErr(validationErrorCode);
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
	
	/**
	 * 年齢加算対象者データ一覧を取得
	 * 
	 * @param commonDto
	 * 			DTO
	 * @param sysDate
	 * 			システム処理年月
	 */	
	private boolean setGrvAgeKasanInfoDownload(Skf3060Sc001DownloadDto downloadDto, String sysDate){
        // 検索条件をもとに検索結果カウントの取得
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
		boolean csvDownloadGo = true;
		int maxCount = Integer.parseInt(listTableSearchMaxCount);
		// 表示件数を取得
		int listCount = skf3060Sc001SharedService.getListTableData(
				downloadDto.getBaseTermFrom(), 
				downloadDto.getBaseTermTo(), 
				downloadDto.getShainNo(), 
				downloadDto.getName(), 
				downloadDto.getSelectedOriginalCompanyCd(), 
				downloadDto.getSelectedSalaryCompanyCd(), 
				downloadDto.getSelectedSendMailStatus(), 
				skf3060Sc001SharedService.SEARCH_PATTERN_COUNT, 
				sysDate,
				listTableData);
		if(maxCount < listCount){
			// 終了
			LogUtils.debugByMsg("年齢加算対象者データ（CSV出力）：　最大件数（" + maxCount + ") < 取得件数（" + downloadDto.getListTableData().size() + "）");
			ServiceHelper.addWarnResultMessage(downloadDto, MessageIdConstant.W_SKF_1002, String.valueOf(maxCount), "");
			csvDownloadGo = false;
			return csvDownloadGo;
		}else if(listCount > 0){
	        // データ取得
			int dataCount = skf3060Sc001SharedService.getListTableData(
					downloadDto.getBaseTermFrom(), 
					downloadDto.getBaseTermTo(), 
					downloadDto.getShainNo(), 
					downloadDto.getName(), 
					downloadDto.getSelectedOriginalCompanyCd(), 
					downloadDto.getSelectedSalaryCompanyCd(), 
					downloadDto.getSelectedSendMailStatus(),
					skf3060Sc001SharedService.SEARCH_PATTERN_DATA, 
					sysDate,
					listTableData);	   
			listCount = dataCount; // 一応。。
			
		}
		downloadDto.setListTableData(listTableData);		
		
		return csvDownloadGo;
	}
	
}
