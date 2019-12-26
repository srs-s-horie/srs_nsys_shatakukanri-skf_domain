/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3040.domain.service.skf3040sc002;

import static jp.co.c_nexco.nfw.core.constants.CommonConstant.NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi_v3_8.ss.usermodel.Cell;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3040Sc002.Skf3040Sc002GetHannyuDataInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3040Sc002.Skf3040Sc002GetHannyuDataInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3040Sc002.Skf3040Sc002GetHenkanDataInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3040Sc002.Skf3040Sc002GetHenkanDataInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3040Sc002.Skf3040Sc002GetShitadoriDataInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3040Sc002.Skf3040Sc002GetShitadoriDataInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBaseBusinessLogicUtils.SkfBaseBusinessLogicUtilsGetSystemDateTimeExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuBihin;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3040Sc002.Skf3040Sc002GetHannyuDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3040Sc002.Skf3040Sc002GetHenkanDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3040Sc002.Skf3040Sc002GetShitadoriDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfBaseBusinessLogicUtils.SkfBaseBusinessLogicUtilsGetSystemDateTimeExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3030TShatakuBihinRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.nfw.webcore.utils.bean.RowDataBean;
import jp.co.c_nexco.nfw.webcore.utils.bean.SheetDataBean;
import jp.co.c_nexco.nfw.webcore.utils.bean.WorkBookDataBean;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3040.domain.dto.skf3040sc002.Skf3040Sc002DownloadDto;
import jp.co.intra_mart.mirage.integration.guice.Transactional;

/**
 * 備品搬入・搬出確認リスト出力画面のDownloadサービス処理クラス。　 
 * 
 */
@Service
public class Skf3040Sc002DownloadService extends BaseServiceAbstract<Skf3040Sc002DownloadDto> {

	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;	
	
	@Autowired
	private Skf3040Sc002GetHannyuDataExpRepository skf3040Sc002GetHannyuDataExpRepository;
	
	@Autowired
	private Skf3040Sc002GetHenkanDataExpRepository skf3040Sc002GetHenkanDataExpRepository;
	
	@Autowired
	private Skf3040Sc002GetShitadoriDataExpRepository skf3040Sc002GetShitadoriDataExpRepository;
	
	@Autowired
	private Skf3030TShatakuBihinRepository skf3030TShatakuBihinRepository;
	
	@Autowired
	private SkfBaseBusinessLogicUtilsGetSystemDateTimeExpRepository skfBaseBusinessLogicUtilsGetSystemDateTimeExpRepository;
	
	@Value("${skf.common.validate_error}")
    private String validationErrorCode;	
    
	private String fileName = "skf3040.skf3040_sc002.FileId";
	
	// ファイル名
	@Value("${skf3040.skf3040_sc002.excelFileName}")
	private String excelFileName;		

	@Value("${skf3040.skf3040_sc002.excelOutPutStartLine}")
	private Integer excelOutPutStartLine;
	
	@Value("${skf3040.skf3040_sc002.excelWorkSheetName}")
	private String excelWorkSheetName;		
	
	/**
	 * 正常終了：0
	 */
	private int OUTPUT_OK = 0;
	/**
	 * データなし：-1
	 */
	private int NO_DATA = -1;
	/**
	 * DB更新エラー：-2
	 */
	private int DATA_ERROR = -2;
	/**
	 * 帳票作成エラー：-3
	 */
	private int OUTPUT_ERROR = -3;
	
	/**
	 * 都道府県コード（その他：48）
	 */
	private String CD_PREF_OTHER = "48";
	
	/**
	 * 搬入
	 */
	private String HANNYU = "搬入";
	/**
	 * 返還
	 */
	private String HENKAN = "返還";
	/**
	 * 下取
	 */
	private String SHITADORI = "下取";
	
	
	/**
	 * サービス処理を行う。　
	 * 
	 * @param searchDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Skf3040Sc002DownloadDto index(Skf3040Sc002DownloadDto downloadDto) throws Exception {
		
		downloadDto.setPageTitleKey(MessageIdConstant.SKF3040_SC002_TITLE);
 		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("備品搬入・搬出確認リスト出力", CodeConstant.C001, downloadDto.getPageId());
		
		// エラークリア
		downloadDto.setCarryingInOutTermFromErr(null);
		downloadDto.setCarryingInOutTermToErr(null);
		
		// 入力チェック
		if(false == validateInput(downloadDto)){
			// 出力状況の取得
			String outSituation = downloadDto.getOutSituation();
			int result = getBihinHannyuHanshutsuCheckList(replaceDateFormat(downloadDto.getCarryingInOutTermFrom()),
					replaceDateFormat(downloadDto.getCarryingInOutTermTo()), outSituation, downloadDto);
			
			
			if(OUTPUT_OK == result){
				// 正常終了
				LogUtils.debugByMsg("出力処理終了");
			}else if(NO_DATA == result){
				// 出力対象データ0件の場合
				// warning.skf.w_skf_1007=検索結果がありません。抽出条件を変更してください。
				ServiceHelper.addWarnResultMessage(downloadDto, MessageIdConstant.W_SKF_1007);
			}else if(DATA_ERROR == result){
				// データベース更新時エラーの場合
				// warning.skf.w_skf_1009=他のユーザによって更新されています。もう一度画面を更新してください。
				ServiceHelper.addWarnResultMessage(downloadDto, MessageIdConstant.W_SKF_1009);
				// 例外時はthrowしてRollBack
				throwBusinessExceptionIfErrors(downloadDto.getResultMessages());
			}else{
				// 帳票作成時エラー
				// error.skf.e_skf_1078={0}エラーが発生しました。ヘルプデスクへ連絡してください。
				ServiceHelper.addErrorResultMessage(downloadDto, null, MessageIdConstant.E_SKF_1078, "帳票作成");
				// 例外時はthrowしてRollBack
				throwBusinessExceptionIfErrors(downloadDto.getResultMessages());
			}
			
		}else{
			// エラーのため出力中止
			LogUtils.debugByMsg("入力チェックエラーのため処理終了");
		}
		
		return downloadDto;
	}	
	
	/**
	 * 入力チェックを行う.
	 * (Download用)
	 * @param commonDto *indexメソッドの引数のDTO。入力チェックエラーの場合、エラーメッセージを詰める。
	 * @throws ParseException 日付フォーマット変換エラー
	 */
	private boolean validateInput(Skf3040Sc002DownloadDto downloadDto) throws ParseException {

		boolean isCheckOk = false;
		String debugMessage = "";
		
		/** 希望日 **/
		// - 希望日From
		// 必須入力チェック
		if(NfwStringUtils.isEmpty(downloadDto.getCarryingInOutTermFrom())){
			isCheckOk = true;
			// error.skf.e_skf_1048={0}が未入力です。
			ServiceHelper.addErrorResultMessage(downloadDto, null, MessageIdConstant.E_SKF_1048, "開始日");
			debugMessage += "　必須入力チェック - " + "搬入・搬出日(開始日：From)";
			downloadDto.setCarryingInOutTermFromErr(validationErrorCode);
			return isCheckOk;
		}else{
			// フォーマットチェック
			if(!SkfCheckUtils.isSkfDateFormat(replaceDateFormat(downloadDto.getCarryingInOutTermFrom()), CheckUtils.DateFormatType.YYYYMMDD)){
				isCheckOk = true;
				// error.skf.e_skf_1055={0}は日付を正しく入力してください。
				ServiceHelper.addErrorResultMessage(downloadDto, null, MessageIdConstant.E_SKF_1055, "開始日");
				debugMessage += "　日付形式チェック - " + "搬入・搬出日(開始日：From)";
				downloadDto.setCarryingInOutTermFromErr(validationErrorCode);
				return isCheckOk;
			}			
		}
		// - 希望日To
		// 必須入力チェック
		if(NfwStringUtils.isEmpty(downloadDto.getCarryingInOutTermTo())){
			isCheckOk = true;
			// error.skf.e_skf_1048={0}が未入力です。
			ServiceHelper.addErrorResultMessage(downloadDto, null, MessageIdConstant.E_SKF_1048, "終了日");
			debugMessage += "　必須入力チェック - " + "搬入・搬出日(終了日：To)";
			downloadDto.setCarryingInOutTermToErr(validationErrorCode);
			return isCheckOk;
		}else{
			// フォーマットチェック
			if(!SkfCheckUtils.isSkfDateFormat(replaceDateFormat(downloadDto.getCarryingInOutTermTo()), CheckUtils.DateFormatType.YYYYMMDD)){
				isCheckOk = true;
				// error.skf.e_skf_1055={0}は日付を正しく入力してください。
				ServiceHelper.addErrorResultMessage(downloadDto, null, MessageIdConstant.E_SKF_1055, "終了日");
				debugMessage += "　日付形式チェック - " + "搬入・搬出日(終了日：To)";
				downloadDto.setCarryingInOutTermToErr(validationErrorCode);
				return isCheckOk;
			}			
		}
		// 基準期間From < To整合性
        Date fromDate = null;
        Date toDate = null;
        int diff = 0;
        if (false == isCheckOk) {
        	// isCheckOkがfalse（エラーがない）の時のみ実施
            SimpleDateFormat sdf = new SimpleDateFormat(SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT);
            fromDate = sdf.parse(replaceDateFormat(downloadDto.getCarryingInOutTermFrom()));
            toDate = sdf.parse(replaceDateFormat(downloadDto.getCarryingInOutTermTo()));
            diff = fromDate.compareTo(toDate);
            if (diff > 0) {
				isCheckOk = true;
				// error.skf.e_skf_1057={0}と{1}を正しく入力してください。
                ServiceHelper.addErrorResultMessage(downloadDto, null, MessageIdConstant.E_SKF_1057, "開始日", "終了日");
                downloadDto.setCarryingInOutTermFromErr(validationErrorCode);
                //downloadDto.setCarryingInOutTermToErr(validationErrorCode);
            }
        }

        // デバッグメッセージ出力
		if (isCheckOk) {
			LogUtils.debugByMsg("入力チェックエラー：" + debugMessage);
		} else {
			LogUtils.debugByMsg("入力チェックOK：" + debugMessage);
		}		
		
		return isCheckOk;
	}	
	
	/**
	 * 日付から"/"と"_"を取り除く
	 * 
	 * @param targetDate
	 * 			日付文字列
	 * @return Replace後の日付文字列
	 *  
	 */
	private String replaceDateFormat(String targetDate){
		return targetDate.replace("/", "").replace("_", "");
	}		
	
	
	/**
	 * 帳票出力
	 * 
	 * @param startDate 開始日
	 * @param endDate 終了日
	 * @param outSituation 出力状況
	 * @param downloadDto
	 * @return 処理結果
	 *  
	 */
	private int getBihinHannyuHanshutsuCheckList(String startDate, String endData, String outSituation, Skf3040Sc002DownloadDto downloadDto){
		int result = 0;
		
		// システム処理年月の取得
		//String sysProcessDate = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu();		
		// 帳票用データ
		List<Map<String, Object>> setExcelDataList = new ArrayList<Map<String, Object>>();
		// 更新用社宅管理IDリスト
		List<Map<String, Object>> setDbUpdateDataList = new ArrayList<Map<String, Object>>();
		List<String> shatakuKanriIdList = new ArrayList<String>();
		
		try{
			// レンタル備品指示書の出力対象データの取得
			// - 搬入
			List<Skf3040Sc002GetHannyuDataInfoExp> hannyuData = new ArrayList<Skf3040Sc002GetHannyuDataInfoExp>();
			// -- 検索パラメータ設定
			Skf3040Sc002GetHannyuDataInfoExpParameter hannyuParam = new Skf3040Sc002GetHannyuDataInfoExpParameter();
			hannyuParam.setStartDate(startDate);
			hannyuParam.setEndDate(endData);
			hannyuParam.setOutSituation(outSituation);
			hannyuData = skf3040Sc002GetHannyuDataExpRepository.getHannyuData(hannyuParam);
			// - 返還
			List<Skf3040Sc002GetHenkanDataInfoExp> henkanData = new ArrayList<Skf3040Sc002GetHenkanDataInfoExp>();
			// -- 検索パラメータ設定
			Skf3040Sc002GetHenkanDataInfoExpParameter henkanParam = new Skf3040Sc002GetHenkanDataInfoExpParameter();
			henkanParam.setStartDate(startDate);
			henkanParam.setEndDate(endData);
			henkanParam.setOutSituation(outSituation);
			henkanData = skf3040Sc002GetHenkanDataExpRepository.getHenkanData(henkanParam);
			// - 下取
			List<Skf3040Sc002GetShitadoriDataInfoExp> shitadoriData = new ArrayList<Skf3040Sc002GetShitadoriDataInfoExp>();
			// -- 検索パラメータ設定
			Skf3040Sc002GetShitadoriDataInfoExpParameter shitadoriParam = new Skf3040Sc002GetShitadoriDataInfoExpParameter();
			shitadoriParam.setStartDate(startDate);
			shitadoriParam.setEndDate(endData);
			shitadoriParam.setOutSituation(outSituation);
			shitadoriData = skf3040Sc002GetShitadoriDataExpRepository.getShitadoriData(shitadoriParam);
			
			// データの取得結果が0件の場合の処理
			if((0 == hannyuData.size()) && (0 == henkanData.size()) && (0 == shitadoriData.size())){
				LogUtils.debugByMsg("データ取得結果0件（搬入・返還・下取のいずれもなし）");
				return NO_DATA;
			}
			
			// 帳票用データの作成
			getOutPutData(hannyuData, henkanData, shitadoriData, setExcelDataList, shatakuKanriIdList, setDbUpdateDataList);
			
		}catch(Exception ex){
			return DATA_ERROR;
		}
		
		// 社宅管理台帳備品基本テーブルの更新処理
		for(int i = 0; i < setDbUpdateDataList.size(); i++){
			try{
				Map<String, Object> temp = setDbUpdateDataList.get(i);
				// key1：業務内容　key2：社宅管理ID（提示番号）　key3：更新日時
				long shatakuKanriId = Long.parseLong(temp.get("key2").toString());

				// 更新対象が社宅管理台帳備品基本テーブル
				Skf3030TShatakuBihin bihinData = new Skf3030TShatakuBihin();
				bihinData = skf3030TShatakuBihinRepository.selectByPrimaryKey(shatakuKanriId);

				// 日付変換
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
				Date mapDate = null;
				try{
					mapDate = dateFormat.parse(temp.get("key3").toString());
				}catch(ParseException ex){
					LogUtils.debugByMsg("社宅管理台帳備品基本テーブル-更新日時変換エラー :" + temp.get("key3").toString());
					return DATA_ERROR;
				}
				
				// 排他チェック
				super.checkLockException(mapDate, bihinData.getUpdateDate());			
				
				// 更新処理
				Skf3030TShatakuBihin updateParam = new Skf3030TShatakuBihin();
				// 社宅管理ID（キー項目）
				updateParam.setShatakuKanriId(shatakuKanriId);
				// その他の項目
				// NULL更新したいカラムがあるので「updateByPrimaryKey」を使う このため、全カラムにデータ詰める
				updateParam.setTaiyoDate(strNullCheck(bihinData.getTaiyoDate()));
				updateParam.setHannyuRequestDay(strNullCheck(bihinData.getHannyuRequestDay()));
				updateParam.setHannyuRequestKbn(strNullCheck(bihinData.getHannyuRequestKbn()));
				updateParam.setHannyuConfirmDate(strNullCheck(bihinData.getHannyuConfirmDate()));
				updateParam.setUkeireDairiName(strNullCheck(bihinData.getUkeireDairiName()));
				updateParam.setUkeireDairiApoint(strNullCheck(bihinData.getUkeireDairiApoint()));
				updateParam.setHannyuCarryinInstruction(strNullCheck(bihinData.getHannyuCarryinInstruction()));
				updateParam.setHenkyakuDate(strNullCheck(bihinData.getHenkyakuDate()));
				updateParam.setHansyutuRequestDay(strNullCheck(bihinData.getHansyutuRequestDay()));
				updateParam.setHansyutuRequestKbn(strNullCheck(bihinData.getHansyutuRequestKbn()));
				updateParam.setHansyutuConfirmDate(strNullCheck(bihinData.getHansyutuConfirmDate()));
				updateParam.setTatiaiDairiName(strNullCheck(bihinData.getTatiaiDairiName()));
				updateParam.setTatiaiDairiApoint(strNullCheck(bihinData.getTatiaiDairiApoint()));
				updateParam.setHansyutuCarryinInstruction(strNullCheck(bihinData.getHansyutuCarryinInstruction()));
				updateParam.setDairiBiko(strNullCheck(bihinData.getDairiBiko()));
				updateParam.setBihinBiko(strNullCheck(bihinData.getBihinBiko()));
				updateParam.setUkeireMyApoint(strNullCheck(bihinData.getUkeireMyApoint()));
				updateParam.setTatiaiMyApoint(strNullCheck(bihinData.getTatiaiMyApoint()));
				//updateParam.setHannyuChecklistDate(strNullCheck(bihinData.getHannyuChecklistDate()));
				//updateParam.setHansyutuChecklistDate(strNullCheck(bihinData.getHansyutuChecklistDate()));
				updateParam.setDeleteFlag(bihinData.getDeleteFlag());
				updateParam.setInsertDate(bihinData.getInsertDate());
				updateParam.setInsertUserId(bihinData.getInsertUserId());
				updateParam.setInsertProgramId(bihinData.getInsertProgramId());
				
				// 更新対象カラムへ現在日時取得と設定
		        String sysDate = getNowDateStrFormat("yyyyMMdd");
				if(temp.get("key1").toString().equals(HANNYU)){
					updateParam.setHannyuChecklistDate(sysDate);
					updateParam.setHansyutuChecklistDate(strNullCheck(bihinData.getHansyutuChecklistDate()));
				}else{
					updateParam.setHannyuChecklistDate(strNullCheck(bihinData.getHannyuChecklistDate()));
					updateParam.setHansyutuChecklistDate(sysDate);
				}
				int retCount = skf3030TShatakuBihinRepository.updateByPrimaryKey(updateParam);
				
				// 更新できなかった
				if(0 >= retCount){
					LogUtils.debugByMsg("社宅管理台帳備品基本テーブル-更新エラー 　戻り値：" + String.valueOf(retCount));
					// エラーで返却してロールバック
					return DATA_ERROR;
				}
			}catch(Exception ex){
				LogUtils.debugByMsg("社宅管理台帳備品基本テーブル-更新エラー " + ex.toString());
				return DATA_ERROR;
			}
		}
		
		// 帳票出力処理
		try{
			SheetDataBean carryingInOutSheet = null;
			carryingInOutSheet = createWorkSheetRentalBihin(setExcelDataList);
			fileOutPutExcelContractInfo(carryingInOutSheet, downloadDto);			
		}catch(Exception ex){
			LogUtils.debugByMsg("帳票作成時に例外発生");
			return OUTPUT_ERROR;
		}
		
		return result;
	}
	
	
	/**
	 * 帳票出力データとDB更新用データを作成する
	 * 
	 * @param hannyuData 搬入データ
	 * @param henkanData 返還データ
	 * @param shitadoriData 下取データ
	 * @param setExcelDataList 帳票出力データ
	 * @param setDbUpdateDataList DB更新データ
	 * 
	 */
	private void getOutPutData(List<Skf3040Sc002GetHannyuDataInfoExp> hannyuData,
			List<Skf3040Sc002GetHenkanDataInfoExp> henkanData,
			List<Skf3040Sc002GetShitadoriDataInfoExp> shitadoriData,
			List<Map<String, Object>> setExcelDataList, List<String> shatakuKanriIdList, List<Map<String, Object>> setDbUpdateDataList){

		// 都道府県
		Map<String, String> prefCdList = new HashMap<String, String>();
		prefCdList = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_PREFCD);
		
		// 搬入データ
		for(int i=0; i < hannyuData.size(); i++ ){
			Skf3040Sc002GetHannyuDataInfoExp tmpData = hannyuData.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			// 業務内容
			tmpMap.put("col1", HANNYU);
			// 搬入日
			tmpMap.put("col2", strNullCheck(tmpData.getHannyuRequestDay()));
			// 確認日
			tmpMap.put("col3", strNullCheck(tmpData.getHannyuConfirmDate()));
			// 社員番号
			tmpMap.put("col4", strNullCheck(tmpData.getShainNo()));
			// 社員氏名
			tmpMap.put("col5", strNullCheck(tmpData.getName()));
			// 所在地
			String shozaichi = "";
			String prefCd = strNullCheck(tmpData.getPrefCd());
			if(NfwStringUtils.isNotEmpty(prefCd) && false == CD_PREF_OTHER.equals(prefCd)){
				// 都道府県名
				String prefName = prefCdList.get(tmpData.getPrefCd());
				shozaichi = prefName + strNullCheck(tmpData.getAddress());
			}else{
				shozaichi = strNullCheck(tmpData.getAddress());
			}
			tmpMap.put("col6", shozaichi);
			// 社宅名
			tmpMap.put("col7", strNullCheck(tmpData.getShatakuName()));
			// 部屋番号
			tmpMap.put("col8", strNullCheck(tmpData.getRoomNo()));
			// 備品名
			tmpMap.put("col9", strNullCheck(tmpData.getBihinName()));
			
			setExcelDataList.add(tmpMap);
			
			// 更新用条件の設定
			checkDuplicateId(shatakuKanriIdList, setDbUpdateDataList, HANNYU, String.valueOf(tmpData.getShatakuKanriId()), tmpData.getUpdateDateTime());
		}

		// 返還データ
		for(int i=0; i < henkanData.size(); i++ ){
			Skf3040Sc002GetHenkanDataInfoExp tmpData = henkanData.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();

			// 業務内容
			tmpMap.put("col1", HENKAN);
			// 返還日
			tmpMap.put("col2", strNullCheck(tmpData.getHansyutuRequestDay()));
			// 確認日
			tmpMap.put("col3", strNullCheck(tmpData.getHansyutuConfirmDate()));
			// 社員番号
			tmpMap.put("col4", strNullCheck(tmpData.getShainNo()));
			// 社員氏名
			tmpMap.put("col5", strNullCheck(tmpData.getName()));
			// 所在地
			String shozaichi = "";
			String prefCd = strNullCheck(tmpData.getPrefCd());
			if(NfwStringUtils.isNotEmpty(prefCd) && false == CD_PREF_OTHER.equals(prefCd)){
				// 都道府県名
				String prefName = prefCdList.get(tmpData.getPrefCd());
				shozaichi = prefName + strNullCheck(tmpData.getAddress());
			}else{
				shozaichi = strNullCheck(tmpData.getAddress());
			}
			tmpMap.put("col6", shozaichi);
			// 社宅名
			tmpMap.put("col7", strNullCheck(tmpData.getShatakuName()));
			// 部屋番号
			tmpMap.put("col8", strNullCheck(tmpData.getRoomNo()));
			// 備品名
			tmpMap.put("col9", strNullCheck(tmpData.getBihinName()));

			setExcelDataList.add(tmpMap);
			
			// 更新用条件の設定
			checkDuplicateId(shatakuKanriIdList, setDbUpdateDataList, HENKAN, String.valueOf(tmpData.getShatakuKanriId()), tmpData.getUpdateDateTime());
		}

		// 下取データ
		for(int i=0; i < shitadoriData.size(); i++ ){
			Skf3040Sc002GetShitadoriDataInfoExp tmpData = shitadoriData.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();

			// 業務内容
			tmpMap.put("col1", SHITADORI);
			// 下取日
			tmpMap.put("col2", strNullCheck(tmpData.getHansyutuRequestDay()));
			// 確認日
			tmpMap.put("col3", strNullCheck(tmpData.getHansyutuConfirmDate()));
			// 社員番号
			tmpMap.put("col4", strNullCheck(tmpData.getShainNo()));
			// 社員氏名
			tmpMap.put("col5", strNullCheck(tmpData.getName()));
			// 所在地
			String shozaichi = "";
			String prefCd = strNullCheck(tmpData.getPrefCd());
			if(NfwStringUtils.isNotEmpty(prefCd) && false == CD_PREF_OTHER.equals(prefCd)){
				// 都道府県名
				String prefName = prefCdList.get(tmpData.getPrefCd());
				shozaichi = prefName + strNullCheck(tmpData.getAddress());
			}else{
				shozaichi = strNullCheck(tmpData.getAddress());
			}
			tmpMap.put("col6", shozaichi);
			// 社宅名
			tmpMap.put("col7", strNullCheck(tmpData.getShatakuName()));
			// 部屋番号
			tmpMap.put("col8", strNullCheck(tmpData.getRoomNo()));
			// 備品名
			tmpMap.put("col9", strNullCheck(tmpData.getBihinName()));

			setExcelDataList.add(tmpMap);

			// 更新用条件の設定
			checkDuplicateId(shatakuKanriIdList, setDbUpdateDataList, SHITADORI, String.valueOf(tmpData.getShatakuKanriId()), tmpData.getUpdateDateTime());
		}
	}
	

	/**
	 * 社宅管理IDの重複チェック
	 * @param shatakuKanriIdList
	 * @param setDbUpdateDataList
	 * @param gyoumu
	 * @param shatakuKanriId
	 * @param updateTime
	 * 
	 */
	private void checkDuplicateId(List<String> shatakuKanriIdList, List<Map<String, Object>> setDbUpdateDataList,
			String gyoumu, String shatakuKanriId, String updateTime){
		// 更新用条件の設定
		if(shatakuKanriIdList.size() > 0 && shatakuKanriIdList.contains(shatakuKanriId)){
			//　処理なし
		}else{
			shatakuKanriIdList.add(shatakuKanriId);
			Map<String, Object> tmpUpdateMap = new HashMap<String, Object>();
			// 業務内容
			tmpUpdateMap.put("key1", gyoumu);
			// 社宅管理ID
			tmpUpdateMap.put("key2", shatakuKanriId);
			// 更新日時
			tmpUpdateMap.put("key3", updateTime);

			setDbUpdateDataList.add(tmpUpdateMap);
		}	
	}
	
	
	/**
	 * レンタル備品指示書のExcelワークシート作成
	 * 
	 * @param reportData	DBからの取得値
	 * @return 出力用Excelワークシート
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private SheetDataBean createWorkSheetRentalBihin(List<Map<String, Object>> setExcelDataList){
		
		// Excelワークシート
		SheetDataBean sheetDataBean = new SheetDataBean();
		// Excel行データ
		List<RowDataBean> rowDataBeanList = new ArrayList<>();		
		
		// 一覧データセット
		for(int i = 0; i < setExcelDataList.size(); i++){
			// 行データ
			RowDataBean rdb = new RowDataBean();
			Map<String, Object> getRowData = setExcelDataList.get(i);
			int rowNo = i + 2;
			
			// 業務内容(A:col1)
			rdb.addCellDataBean("A" + rowNo, strNullCheckExcel(getRowData.get("col1")));
			// 搬入日／返還日／下取日(B:col2)
			String dateStr = strNullCheckExcel(getRowData.get("col2"));
			if(CheckUtils.isEmpty(dateStr)){
				// 空
				rdb.addCellDataBean("B" + rowNo, dateStr);
			}else{
				// フォーマット指定して設定
				rdb.addCellDataBean(
						"B" + rowNo, 
						skfDateFormatUtils.dateFormatFromString(dateStr, SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH),
						Cell.CELL_TYPE_NUMERIC,
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);				
			}
			// 確認日(C:col3)
			String kakuninDateStr = strNullCheckExcel(getRowData.get("col3"));
			if(CheckUtils.isEmpty(kakuninDateStr)){
				// 空
				rdb.addCellDataBean("C" + rowNo, kakuninDateStr);
			}else{
				// フォーマット指定して設定
				rdb.addCellDataBean(
						"C" + rowNo, 
						skfDateFormatUtils.dateFormatFromString(kakuninDateStr, SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH),
						Cell.CELL_TYPE_NUMERIC,
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);				
			}
			// 社員番号(D:col4)
			rdb.addCellDataBean("D" + rowNo, strNullCheckExcel(getRowData.get("col4")));
			// 社員氏名(E:col5)
			rdb.addCellDataBean("E" + rowNo, strNullCheckExcel(getRowData.get("col5")));
			// 所在地(F:col6)
			rdb.addCellDataBean("F" + rowNo, strNullCheckExcel(getRowData.get("col6")));
			// 社宅名(G:col7)
			rdb.addCellDataBean("G" + rowNo, strNullCheckExcel(getRowData.get("col7")));
			// 部屋番号(H:col8)
			rdb.addCellDataBean("H" + rowNo, strNullCheckExcel(getRowData.get("col8")));
			// 備品名(I:col9)
			rdb.addCellDataBean("I" + rowNo, strNullCheckExcel(getRowData.get("col9")));
			
			// 行データ追加
			rowDataBeanList.add(rdb);			
		}
		
		sheetDataBean.setRowDataBeanList(rowDataBeanList);	
		sheetDataBean.setSheetName(excelWorkSheetName);		
		return sheetDataBean;
	}
	
	
	
	/**
	 * 備品搬入・搬出確認リスト帳票出力
	 * 
	 * @param carryingInOutSheet	備品搬入・搬出確認リストワークシート
	 * @param downloadDto	DTO
	 * @throws Exception
	 */
	private void fileOutPutExcelContractInfo(SheetDataBean carryingInOutSheet,
			Skf3040Sc002DownloadDto downloadDto) throws Exception {

		List<SheetDataBean> sheetDataBeanList = new ArrayList<>();
		sheetDataBeanList.add(carryingInOutSheet);

		Map<String, Object> cellparams = new HashMap<>(); // 列書式(背景、フォント)
		Map<String, Object> resultMap = new HashMap<>(); // 列書式(背景、フォント)

		String uploadFileName = excelFileName + DateTime.now().toString("YYYYMMddHHmmss") + ".xlsx";
		WorkBookDataBean wbdb = new WorkBookDataBean(uploadFileName);
		wbdb.setSheetDataBeanList(sheetDataBeanList);
		// Excelファイルへ出力
		SkfFileOutputUtils.fileOutputExcel(wbdb, cellparams, fileName, "skf3040rp002",
				1, null, resultMap, excelOutPutStartLine);
		byte[] writeFileData = (byte[]) resultMap.get("fileData");
		downloadDto.setFileData(writeFileData);
		downloadDto.setUploadFileName(uploadFileName);
		downloadDto.setViewPath(NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH);
	}		
	
	
	/**
	 * StringのNULLチェック
	 * 
	 * @param targetStr
	 * 
	 */
	private String strNullCheck(String targetStr){

		if(NfwStringUtils.isEmpty(targetStr)){
			return null;
		}else{
			return targetStr;
		}
	}

	
	/**
	 * StringのNULLチェック
	 * NullならばExcel出力用に空文字を返却する
	 * 
	 * @param targetStr
	 * 
	 */
	private String strNullCheckExcel(Object targetObject){
		if(null == targetObject){
			return "";
		}else{
			return targetObject.toString();
		}
	}
	

	/**
	 * 現在の日時を取得する
	 * 
	 * @param 現在日時（Stringフォーマット）
	 * 
	 */	
	private String getNowDateStrFormat(String dateFormat){
        Date nowTime = getSystemDate();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(nowTime);
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(calendar.getTime()).toString();		
	}
	
	
	/**
	 * 現在の日時を取得する
	 * 
	 * @param nowTime
	 * 			現在日時
	 */	
	 private Date getSystemDate(){
	        List<SkfBaseBusinessLogicUtilsGetSystemDateTimeExp> systemDate = new ArrayList<SkfBaseBusinessLogicUtilsGetSystemDateTimeExp>();
	        systemDate = skfBaseBusinessLogicUtilsGetSystemDateTimeExpRepository.getSystemDateTime();
	        Date nowTime = systemDate.get(0).getSystemdate();
	        return nowTime;
	 }	
	 
}
