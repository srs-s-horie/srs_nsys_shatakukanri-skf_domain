/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3040.domain.service.skf3040sc001;

import static jp.co.c_nexco.nfw.core.constants.CommonConstant.NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.poi_v3_8.ss.usermodel.Cell;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3040Sc001.Skf3040Sc001GetHannyuDataInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3040Sc001.Skf3040Sc001GetHannyuDataInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3040Sc001.Skf3040Sc001GetHenkanDataInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3040Sc001.Skf3040Sc001GetHenkanDataInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3040Sc001.Skf3040Sc001GetShitadoriDataInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3040Sc001.Skf3040Sc001GetShitadoriDataInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBaseBusinessLogicUtils.SkfBaseBusinessLogicUtilsGetSystemDateTimeExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3022TTeijiData;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuBihin;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3040Sc001.Skf3040Sc001GetHannyuDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3040Sc001.Skf3040Sc001GetHenkanDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3040Sc001.Skf3040Sc001GetShitadoriDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfBaseBusinessLogicUtils.SkfBaseBusinessLogicUtilsGetSystemDateTimeExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3022TTeijiDataRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3030TShatakuBihinRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.nfw.webcore.utils.bean.RowDataBean;
import jp.co.c_nexco.nfw.webcore.utils.bean.SheetDataBean;
import jp.co.c_nexco.nfw.webcore.utils.bean.WorkBookDataBean;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3040.domain.dto.skf3040sc001.Skf3040Sc001DownloadDto;
import jp.co.intra_mart.mirage.integration.guice.Transactional;

/**
 * レンタル備品指示書出力画面のDownloadサービス処理クラス。　 
 * 
 */
@Service
public class Skf3040Sc001DownloadService extends SkfServiceAbstract<Skf3040Sc001DownloadDto> {

	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
    
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;	
	
	@Autowired
	private Skf3040Sc001GetHannyuDataExpRepository skf3040Sc001GetHannyuDataExpRepository;
	
	@Autowired
	private Skf3040Sc001GetHenkanDataExpRepository skf3040Sc001GetHenkanDataExpRepository;
	
	@Autowired
	private Skf3040Sc001GetShitadoriDataExpRepository skf3040Sc001GetShitadoriDataExpRepository;
	
	@Autowired
	private Skf3030TShatakuBihinRepository skf3030TShatakuBihinRepository;
	
	@Autowired
	private Skf3022TTeijiDataRepository skf3022TTeijiDataRepository;
	
	@Autowired
	private SkfBaseBusinessLogicUtilsGetSystemDateTimeExpRepository skfBaseBusinessLogicUtilsGetSystemDateTimeExpRepository;
	
	@Value("${skf.common.validate_error}")
    private String validationErrorCode;	
    
	private String fileName = "skf3040.skf3040_sc001.FileId";
	
	// ファイル名
	@Value("${skf3040.skf3040_sc001.excelFileName}")
	private String excelFileName;		

	@Value("${skf3040.skf3040_sc001.excelOutPutStartLine}")
	private Integer excelOutPutStartLine;
	
	@Value("${skf3040.skf3040_sc001.excelWorkSheetName}")
	private String excelWorkSheetName;		
	
	/**
	 * 正常終了
	 */
	private int OUTPUT_OK = 0;
	/**
	 * データなし
	 */
	private int NO_DATA = -1;
	/**
	 * DB更新エラー
	 */
	private int DATA_ERROR = -2;
	/**
	 * 帳票作成エラー
	 */
	private int OUTPUT_ERROR = -3;
	
	/**
	 * 都道府県コード（その他）
	 */
	private String CD_PREF_OTHER = "48";
	
	/**
	 * 備品の出力値
	 */
	private String BIHIN_OUTPUT_VALUE = "1";
	
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
	 * 社宅管理台帳備品基本が更新対象
	 */
	private String UPDATE_SHATAKU_BIHIN = "1";
	/**
	 * 提示データが更新対象
	 */
	private String UPDATE_TEIJI_DATA = "2";
	
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
	@Transactional
	public Skf3040Sc001DownloadDto index(Skf3040Sc001DownloadDto downloadDto) throws Exception {
		
		downloadDto.setPageTitleKey(MessageIdConstant.SKF3040_SC001_TITLE);
 		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("指示書出力", CodeConstant.C001, downloadDto.getPageId());
		
		// エラークリア
		downloadDto.setDesiredTermFromErr(null);
		downloadDto.setDesiredTermToErr(null);
		
		// 入力チェック
		if(true == isValidateInputDownload(downloadDto)){
			// 再発行区分の取得
			String reIssuance = downloadDto.getReIssuance();
			int result = getRentalBihinShijisho(replaceDateFormat(downloadDto.getDesiredTermFrom()),
					replaceDateFormat(downloadDto.getDesiredTermTo()), reIssuance, downloadDto);
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
			LogUtils.infoByMsg("index, 入力チェックNGのため処理終了");
		}
		
		return downloadDto;
	}	
	
	/**
	 * 入力チェックを行う.
	 * (Download用)
	 * @param commonDto *indexメソッドの引数のDTO。入力チェックエラーの場合、エラーメッセージを詰める。
	 * @throws ParseException 日付フォーマット変換エラー
	 */
	private boolean isValidateInputDownload(Skf3040Sc001DownloadDto downloadDto) throws ParseException {

		boolean isCheckOk = true;
		String debugMessage = "";
		
		/** 希望日 **/
		// - 希望日From
		// 必須入力チェック
		if(NfwStringUtils.isEmpty(downloadDto.getDesiredTermFrom())){
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(downloadDto, null, MessageIdConstant.E_SKF_1048, "開始日");
			debugMessage += "　必須入力チェック - " + "希望日(開始日：From)";
			downloadDto.setDesiredTermFromErr(validationErrorCode);
			return isCheckOk;
		}else{
			// フォーマットチェック
			if(!SkfCheckUtils.isSkfDateFormat(replaceDateFormat(downloadDto.getDesiredTermFrom()), CheckUtils.DateFormatType.YYYYMMDD)){
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(downloadDto, null, MessageIdConstant.E_SKF_1055, "開始日");
				debugMessage += "　日付形式チェック - " + "希望日(開始日：From)";
				downloadDto.setDesiredTermFromErr(validationErrorCode);
				return isCheckOk;
			}			
		}
		// - 希望日To
		// 必須入力チェック
		if(NfwStringUtils.isEmpty(downloadDto.getDesiredTermTo())){
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(downloadDto, null, MessageIdConstant.E_SKF_1048, "終了日");
			debugMessage += "　必須入力チェック - " + "希望日(終了日：To)";
			downloadDto.setDesiredTermToErr(validationErrorCode);
			return isCheckOk;
		}else{
			// フォーマットチェック
			if(!SkfCheckUtils.isSkfDateFormat(replaceDateFormat(downloadDto.getDesiredTermTo()), CheckUtils.DateFormatType.YYYYMMDD)){
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(downloadDto, null, MessageIdConstant.E_SKF_1055, "終了日");
				debugMessage += "　日付形式チェック - " + "希望日(終了日：To)";
				downloadDto.setDesiredTermToErr(validationErrorCode);
				return isCheckOk;
			}			
		}
		// 基準期間From < To整合性
        Date fromDate = null;
        Date toDate = null;
        int diff = 0;
        if (true == isCheckOk) {
        	// isCheckOkがTrue（エラーがない）の時のみ実施
            SimpleDateFormat sdf = new SimpleDateFormat(SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT);
            fromDate = sdf.parse(replaceDateFormat(downloadDto.getDesiredTermFrom()));
            toDate = sdf.parse(replaceDateFormat(downloadDto.getDesiredTermTo()));
            diff = fromDate.compareTo(toDate);
            if (diff > 0) {
				isCheckOk = false;
                ServiceHelper.addErrorResultMessage(downloadDto, null, MessageIdConstant.E_SKF_1057, "開始日", "終了日");
                downloadDto.setDesiredTermFromErr(validationErrorCode);
                //downloadDto.setDesiredTermToErr(validationErrorCode);
            }
        }

        // デバッグメッセージ出力
		if (isCheckOk) {
			LogUtils.debugByMsg("isValidateInputDownload, 入力チェックOK：" + debugMessage);
		} else {
			LogUtils.infoByMsg("isValidateInputDownload, 入力チェックNG：" + debugMessage);
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
	 * @param reIssuance 再発行区分
	 * @param downloadDto
	 * @return 処理結果
	 *  
	 */
	private int getRentalBihinShijisho(String startDate, String endData, String reIssuance, Skf3040Sc001DownloadDto downloadDto){
		int result = 0;
		
		// システム処理年月の取得
		String sysProcessDate = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu();		
		// 帳票用データ
		List<Map<String, Object>> setExcelDataList = new ArrayList<Map<String, Object>>();
		// 更新用社宅管理IDリスト
		List<Map<String, Object>> setDbUpdateDataList = new ArrayList<Map<String, Object>>();
		List<String> shatakuKanriIdList = new ArrayList<String>();
		
		try{
			// レンタル備品指示書の出力対象データの取得
			// - 搬入
			List<Skf3040Sc001GetHannyuDataInfoExp> hannyuData = new ArrayList<Skf3040Sc001GetHannyuDataInfoExp>();
			// -- 検索パラメータ設定
			Skf3040Sc001GetHannyuDataInfoExpParameter hannyuParam = new Skf3040Sc001GetHannyuDataInfoExpParameter();
			hannyuParam.setStartDate(startDate);
			hannyuParam.setEndDate(endData);
			hannyuParam.setYearMonth(sysProcessDate);
			hannyuParam.setSaihakkoKbn(reIssuance);
			hannyuData = skf3040Sc001GetHannyuDataExpRepository.getHannyuData(hannyuParam);
			// - 返還
			List<Skf3040Sc001GetHenkanDataInfoExp> henkanData = new ArrayList<Skf3040Sc001GetHenkanDataInfoExp>();
			// -- 検索パラメータ設定
			Skf3040Sc001GetHenkanDataInfoExpParameter henkanParam = new Skf3040Sc001GetHenkanDataInfoExpParameter();
			henkanParam.setStartDate(startDate);
			henkanParam.setEndDate(endData);
			henkanParam.setYearMonth(sysProcessDate);
			henkanParam.setSaihakkoKbn(reIssuance);
			henkanData = skf3040Sc001GetHenkanDataExpRepository.getHenkanData(henkanParam);
			// - 下取
			List<Skf3040Sc001GetShitadoriDataInfoExp> shitadoriData = new ArrayList<Skf3040Sc001GetShitadoriDataInfoExp>();
			// -- 検索パラメータ設定
			Skf3040Sc001GetShitadoriDataInfoExpParameter shitadoriParam = new Skf3040Sc001GetShitadoriDataInfoExpParameter();
			shitadoriParam.setStartDate(startDate);
			shitadoriParam.setEndDate(endData);
			shitadoriParam.setYearMonth(sysProcessDate);
			shitadoriParam.setSaihakkoKbn(reIssuance);
			shitadoriData = skf3040Sc001GetShitadoriDataExpRepository.getShitadoriData(shitadoriParam);
			
			// データの取得結果が0件の場合の処理
			if((0 == hannyuData.size()) && (0 == henkanData.size()) && (0 == shitadoriData.size())){
				LogUtils.debugByMsg("データ取得結果0件（搬入・返還・下取のいずれもなし）");
				return NO_DATA;
			}
			
			// 帳票用データの作成
			getOutPutData(hannyuData, henkanData, shitadoriData, setExcelDataList, shatakuKanriIdList, setDbUpdateDataList);
			
		}catch(Exception ex){
			LogUtils.infoByMsg("getRentalBihinShijisho, " + ex.getMessage());
			return DATA_ERROR;
		}
		
		// 社宅管理台帳備品基本テーブルの更新処理
		for(int i = 0; i < setDbUpdateDataList.size(); i++){
			try{
				Map<String, Object> temp = setDbUpdateDataList.get(i);
				// key1：業務内容　key2：社宅管理ID（提示番号）　key3：更新日時　key4:更新対象テーブル
				long shatakuKanriId = Long.parseLong(temp.get("key2").toString());
				
				if(UPDATE_SHATAKU_BIHIN.equals(temp.get("key4").toString())){
					// 更新対象が社宅管理台帳備品基本テーブル
					Skf3030TShatakuBihin bihinData = new Skf3030TShatakuBihin();
					bihinData = skf3030TShatakuBihinRepository.selectByPrimaryKey(shatakuKanriId);

					// 日付変換
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
					Date mapDate = null;
					try{
						mapDate = dateFormat.parse(temp.get("key3").toString());
					}catch(ParseException ex){
						LogUtils.infoByMsg("getRentalBihinShijisho, 社宅管理台帳備品基本テーブル-更新日時変換失敗 :" + temp.get("key3").toString());
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
					updateParam.setHenkyakuDate(strNullCheck(bihinData.getHenkyakuDate()));
					updateParam.setHansyutuRequestDay(strNullCheck(bihinData.getHansyutuRequestDay()));
					updateParam.setHansyutuRequestKbn(strNullCheck(bihinData.getHansyutuRequestKbn()));
					updateParam.setHansyutuConfirmDate(strNullCheck(bihinData.getHansyutuConfirmDate()));
					updateParam.setTatiaiDairiName(strNullCheck(bihinData.getTatiaiDairiName()));
					updateParam.setTatiaiDairiApoint(strNullCheck(bihinData.getTatiaiDairiApoint()));
					updateParam.setDairiBiko(strNullCheck(bihinData.getDairiBiko()));
					updateParam.setBihinBiko(strNullCheck(bihinData.getBihinBiko()));
					updateParam.setUkeireMyApoint(strNullCheck(bihinData.getUkeireMyApoint()));
					updateParam.setTatiaiMyApoint(strNullCheck(bihinData.getTatiaiMyApoint()));
					updateParam.setHannyuChecklistDate(strNullCheck(bihinData.getHannyuChecklistDate()));
					updateParam.setHansyutuChecklistDate(strNullCheck(bihinData.getHansyutuChecklistDate()));
					updateParam.setDeleteFlag(bihinData.getDeleteFlag());
					updateParam.setInsertDate(bihinData.getInsertDate());
					updateParam.setInsertUserId(bihinData.getInsertUserId());
					updateParam.setInsertProgramId(bihinData.getInsertProgramId());
					
					// 更新対象カラムへ現在日時取得と設定
			        String sysDate = getNowDateStrFormat("yyyyMMdd");
					if(Objects.equals(temp.get("key1"), HANNYU)){
						updateParam.setHannyuCarryinInstruction(sysDate);
						updateParam.setHansyutuCarryinInstruction(strNullCheck(bihinData.getHansyutuCarryinInstruction()));
					}else{
						updateParam.setHannyuCarryinInstruction(strNullCheck(bihinData.getHannyuCarryinInstruction()));
						updateParam.setHansyutuCarryinInstruction(sysDate);
					}
					int retCount = skf3030TShatakuBihinRepository.updateByPrimaryKey(updateParam);
					
					// 更新できなかった
					if(0 >= retCount){
						LogUtils.infoByMsg("getRentalBihinShijisho, 社宅管理台帳備品基本テーブル-更新失敗 　戻り値：" + String.valueOf(retCount));
						// エラーで返却してロールバック
						return DATA_ERROR;
					}
				}else{
					// 更新対象が提示データテーブル
					Skf3022TTeijiData teijiData = new Skf3022TTeijiData();
					// ここでは「shatakuKanriId = teijiNo」 (SQL取得時に提示番号を社宅管理番号とリネームして取得している）
					teijiData = skf3022TTeijiDataRepository.selectByPrimaryKey(shatakuKanriId);
					
					// 日付変換
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
					Date mapDate = null;
					try{
						mapDate = dateFormat.parse(temp.get("key3").toString());
					}catch(ParseException ex){
						LogUtils.infoByMsg("getRentalBihinShijisho, 社宅管理台帳備品基本テーブル-更新日時変換失敗 :" + temp.get("key3").toString());
						return DATA_ERROR;
					}
					
					// 排他チェック
					super.checkLockException(mapDate, teijiData.getUpdateDate());			
				
					// 更新処理
					Skf3022TTeijiData updateParam = new Skf3022TTeijiData();
					// 提示番号（キー項目）
					updateParam.setTeijiNo(shatakuKanriId);
					// その他の項目
					// NULL更新したいカラムがあるので「updateByPrimaryKey」を使う このため、全カラムにデータ詰める
					updateParam.setShainNo(strNullCheck(teijiData.getShainNo()));
					updateParam.setNyutaikyoKbn(strNullCheck(teijiData.getNyutaikyoKbn()));
					updateParam.setName(strNullCheck(teijiData.getName()));
					updateParam.setApplKbn(strNullCheck(teijiData.getApplKbn()));
					updateParam.setShatakuKanriNo(teijiData.getShatakuKanriNo());
					updateParam.setShatakuRoomKanriNo(teijiData.getShatakuRoomKanriNo());
					updateParam.setRentalPatternId(teijiData.getRentalPatternId());
					updateParam.setKyojushaKbn(strNullCheck(teijiData.getKyojushaKbn()));
					updateParam.setYakuinSannteiKbn(strNullCheck(teijiData.getYakuinSannteiKbn()));
					updateParam.setRentalDay(teijiData.getRentalDay());
					updateParam.setRentalAdjust(teijiData.getRentalAdjust());
					updateParam.setKyoekihiPersonKyogichuFlg(strNullCheck(teijiData.getKyoekihiPersonKyogichuFlg()));
					updateParam.setKyoekihiPerson(teijiData.getKyoekihiPerson());
					updateParam.setKyoekihiPersonAdjust(teijiData.getKyoekihiPersonAdjust());
					updateParam.setKyoekihiPayMonth(strNullCheck(teijiData.getKyoekihiPayMonth()));
					updateParam.setParkingBlock1(strNullCheck(teijiData.getParkingBlock1()));
					updateParam.setParking1StartDate(strNullCheck(teijiData.getParking1StartDate()));
					updateParam.setParking1EndDate(strNullCheck(teijiData.getParking1EndDate()));
					updateParam.setParkingBlock2(strNullCheck(teijiData.getParkingBlock2()));
					updateParam.setParking2StartDate(strNullCheck(teijiData.getParking2StartDate()));
					updateParam.setParking2EndDate(strNullCheck(teijiData.getParking2EndDate()));
					updateParam.setParkingRentalAdjust(teijiData.getParkingRentalAdjust());
					updateParam.setParking1RentalDay(teijiData.getParking1RentalDay());
					updateParam.setParking2RentalDay(teijiData.getParking2RentalDay());
					updateParam.setBiko(strNullCheck(teijiData.getBiko()));
					updateParam.setEquipmentStartDate(strNullCheck(teijiData.getEquipmentStartDate()));
					updateParam.setEquipmentEndDate(strNullCheck(teijiData.getEquipmentEndDate()));
					updateParam.setCarryinRequestDay(strNullCheck(teijiData.getCarryinRequestDay()));
					updateParam.setCarryinRequestKbn(strNullCheck(teijiData.getCarryinRequestKbn()));
					updateParam.setUkeireMyApoint(strNullCheck(teijiData.getUkeireMyApoint()));
					updateParam.setUkeireDairiName(strNullCheck(teijiData.getUkeireDairiName()));
					updateParam.setUkeireDairiApoint(strNullCheck(teijiData.getUkeireDairiApoint()));
					updateParam.setCarryoutRequestDay(strNullCheck(teijiData.getCarryoutRequestDay()));
					updateParam.setCarryoutRequestKbn(strNullCheck(teijiData.getCarryoutRequestKbn()));
					updateParam.setTatiaiMyApoint(strNullCheck(teijiData.getTatiaiMyApoint()));
					updateParam.setTatiaiDairiName(strNullCheck(teijiData.getTatiaiDairiName()));
					updateParam.setTatiaiDairiApoint(strNullCheck(teijiData.getTatiaiDairiApoint()));
					updateParam.setBihinConfirmDate(strNullCheck(teijiData.getBihinConfirmDate()));
					updateParam.setDairiKiko(strNullCheck(teijiData.getDairiKiko()));
					updateParam.setBihinBiko(strNullCheck(teijiData.getBihinBiko()));
					updateParam.setKashitukeCompanyCd(strNullCheck(teijiData.getKashitukeCompanyCd()));
					updateParam.setKariukeCompanyCd(strNullCheck(teijiData.getKariukeCompanyCd()));
					updateParam.setRent(teijiData.getRent());
					updateParam.setParkingRental(teijiData.getParkingRental());
					updateParam.setKyoekihiBusiness(teijiData.getKyoekihiBusiness());
					updateParam.setMutualUseStartDay(strNullCheck(teijiData.getMutualUseStartDay()));
					updateParam.setMutualUseEndDay(strNullCheck(teijiData.getMutualUseEndDay()));
					updateParam.setAssignCompanyCd(strNullCheck(teijiData.getAssignCompanyCd()));
					updateParam.setAgency(strNullCheck(teijiData.getAgency()));
					updateParam.setAffiliation1(strNullCheck(teijiData.getAffiliation1()));
					updateParam.setAffiliation2(strNullCheck(teijiData.getAffiliation2()));
					updateParam.setAssignCd(strNullCheck(teijiData.getAssignCd()));
					updateParam.setOriginalCompanyCd(strNullCheck(teijiData.getOriginalCompanyCd()));
					updateParam.setPayCompanyCd(strNullCheck(teijiData.getPayCompanyCd()));
					updateParam.setShatakuTeijiStatus(strNullCheck(teijiData.getShatakuTeijiStatus()));
					updateParam.setShatakuTeijiUrgeDate(strNullCheck(teijiData.getShatakuTeijiUrgeDate()));
					updateParam.setBihinTaiyoKbn(strNullCheck(teijiData.getBihinTaiyoKbn()));
					updateParam.setBihinTeijiStatus(strNullCheck(teijiData.getBihinTeijiStatus()));
					updateParam.setBihinTeijiUrgeDate(strNullCheck(teijiData.getBihinTeijiUrgeDate()));
					updateParam.setBihinInoutUrgeDate(strNullCheck(teijiData.getBihinInoutUrgeDate()));
					updateParam.setCreateCompleteKbn(strNullCheck(teijiData.getCreateCompleteKbn()));
					updateParam.setLandCreateKbn(strNullCheck(teijiData.getLandCreateKbn()));
					updateParam.setParkingKanriNo1(teijiData.getParkingKanriNo1());
					updateParam.setParkingKanriNo2(teijiData.getParkingKanriNo2());
					updateParam.setMutualUseKbn(strNullCheck(teijiData.getMutualUseKbn()));
					updateParam.setShatakuCompanyTransferKbn(strNullCheck(teijiData.getShatakuCompanyTransferKbn()));
					updateParam.setKyoekihiCompanyTransferKbn(strNullCheck(teijiData.getKyoekihiCompanyTransferKbn()));
					updateParam.setShatakuKanriId(teijiData.getShatakuKanriId());
					updateParam.setJssShatakuTeijiDate(strNullCheck(teijiData.getJssShatakuTeijiDate()));
					updateParam.setMutualJokyo(strNullCheck(teijiData.getMutualJokyo()));
					updateParam.setRentalMonth(teijiData.getRentalMonth());
					updateParam.setParking1RentalMonth(teijiData.getParking1RentalMonth());
					updateParam.setParking2RentalMonth(teijiData.getParking2RentalMonth());
					updateParam.setNowAffiliation(strNullCheck(teijiData.getNowAffiliation()));
					updateParam.setNewAffiliation(strNullCheck(teijiData.getNewAffiliation()));
					updateParam.setDeleteFlag(teijiData.getDeleteFlag());
					updateParam.setInsertDate(teijiData.getInsertDate());
					updateParam.setInsertUserId(teijiData.getInsertUserId());
					updateParam.setInsertProgramId(teijiData.getInsertProgramId());					
					// 更新対象カラムへ現在日時取得と設定
			        String sysDate = getNowDateStrFormat("yyyyMMdd");
			        updateParam.setHannyuCarryinInstruction(sysDate);

					int retCount = skf3022TTeijiDataRepository.updateByPrimaryKey(updateParam);
					
					// 更新できなかった
					if(0 >= retCount){
						LogUtils.infoByMsg("getRentalBihinShijisho, 提示データテーブル-更新失敗 　戻り値：" + String.valueOf(retCount));
						// エラーで返却してロールバック
						return DATA_ERROR;
					}
				}
			}catch(Exception ex){
				LogUtils.infoByMsg("getRentalBihinShijisho, 社宅管理台帳備品基本テーブル-更新失敗 " + ex.toString());
				return DATA_ERROR;
			}
		}
		
		// 帳票出力処理
		try{
			SheetDataBean rentalBihinSheet = null;
			rentalBihinSheet = createWorkSheetRentalBihin(setExcelDataList);
			fileOutPutExcelContractInfo(rentalBihinSheet, downloadDto);			
		}catch(Exception ex){
			LogUtils.infoByMsg("getRentalBihinShijisho, 帳票作成時に例外発生：" + ex.getMessage());
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
	private void getOutPutData(List<Skf3040Sc001GetHannyuDataInfoExp> hannyuData,
			List<Skf3040Sc001GetHenkanDataInfoExp> henkanData,
			List<Skf3040Sc001GetShitadoriDataInfoExp> shitadoriData,
			List<Map<String, Object>> setExcelDataList, List<String> shatakuKanriIdList, List<Map<String, Object>> setDbUpdateDataList){

		// 希望時間（SKF1048）
		Map<String, String> kibouJikanList = new HashMap<String, String>();
		kibouJikanList = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_REQUEST_TIME);
		
		// エレベータ区分（SKF1066）
		Map<String, String> evKbnList = new HashMap<String, String>();
		evKbnList = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_ELEVATOR_KBN);
		
		// 都道府県
		Map<String, String> prefCdList = new HashMap<String, String>();
		prefCdList = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_PREFCD);
		
		// 搬入データ
		for(int i=0; i < hannyuData.size(); i++ ){
			Skf3040Sc001GetHannyuDataInfoExp tmpData = hannyuData.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			// 業務内容
			tmpMap.put("col1", HANNYU);
			/** 入居者 **/
			// 社員番号
			tmpMap.put("col2", strNullCheck(tmpData.getShainNo()));
			// 社員氏名
			tmpMap.put("col3", strNullCheck(tmpData.getName()));
			// 社員フリガナ
			tmpMap.put("col4", strNullCheck(tmpData.getName_kk()));
			/** 立会人 **/
			// 氏名
			tmpMap.put("col5", strNullCheck(tmpData.getUkeireDairiName()));
			// 連絡先
			if(NfwStringUtils.isEmpty(tmpData.getUkeireDairiApoint())){
				tmpMap.put("col6", strNullCheck(tmpData.getUkeireMyApoint()));
			}else{
				tmpMap.put("col6", strNullCheck(tmpData.getUkeireDairiApoint()));
			}
			/** 希望日時 **/
			// 日付
			tmpMap.put("col7", strNullCheck(tmpData.getHannyuRequestDay()));
			// 時刻
			if(NfwStringUtils.isEmpty(tmpData.getHannyuRequestKbn())){
				tmpMap.put("col8", null);
			}else{
				tmpMap.put("col8", kibouJikanList.get(tmpData.getHannyuRequestKbn()));
			}
			/** 社宅 **/
			// 郵便番号
			tmpMap.put("col9", strNullCheck(tmpData.getZipCd()));
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
			tmpMap.put("col10", shozaichi);
			// 社宅名
			tmpMap.put("col11", strNullCheck(tmpData.getShatakuName()));
			// 部屋番号
			tmpMap.put("col12", strNullCheck(tmpData.getRoomNo()));
			// EV有無
			if(NfwStringUtils.isEmpty(tmpData.getElevatorKbn())){
				tmpMap.put("col13", null);
			}else{
				tmpMap.put("col13", evKbnList.get(tmpData.getElevatorKbn()));
			}
			/** 備品内訳**/
			// 冷蔵庫
			String reizokoFlg = getBihinValueHannyu(tmpData.getReizokoApplKbn(), tmpData.getReizokoLentStatusKbn());
			tmpMap.put("col14", reizokoFlg);
			// テレビ
			String tvFlg = getBihinValueHannyu(tmpData.getTerebiApplKbn(), tmpData.getTerebiLentStatusKbn());
			tmpMap.put("col15", tvFlg);
			// テレビ台
			String tvDaiFlg = getBihinValueHannyu(tmpData.getTerebidaiApplKbn(), tmpData.getTerebidaiLentStatusKbn());
			tmpMap.put("col16", tvDaiFlg);
			// エアコン
			String eakonFlg = getBihinValueHannyu(tmpData.getEakonApplKbn(), tmpData.getEakonLentStatusKbn());
			tmpMap.put("col17", eakonFlg);
			// 洗濯機
			String sentakukiFlg = getBihinValueHannyu(tmpData.getSentakukiApplKbn(), tmpData.getSentakukiLentStatusKbn());
			tmpMap.put("col18", sentakukiFlg);
			// 電子レンジ
			tmpMap.put("col19", null);
			// オーブンレンジ
			String obunFlg = getBihinValueHannyu(tmpData.getObunApplKbn(), tmpData.getObunLentStatusKbn());
			tmpMap.put("col20", obunFlg);
			// オーブントースター
			String obuntosutaFlg = getBihinValueHannyu(tmpData.getObuntosutaApplKbn(), tmpData.getObuntosutaLentStatusKbn());
			tmpMap.put("col21", obuntosutaFlg);
			// 掃除機
			String sojikiFlg = getBihinValueHannyu(tmpData.getSojikiApplKbn(), tmpData.getSojikiLentStatusKbn());
			tmpMap.put("col22", sojikiFlg);
			// 座卓
			String zatakuFlg = getBihinValueHannyu(tmpData.getZatakuApplKbn(), tmpData.getZatakuLentStatusKbn());
			tmpMap.put("col23", zatakuFlg);
			// 机・テーブル
			String tukueFlg = getBihinValueHannyu(tmpData.getTukueApplKbn(), tmpData.getTukueLentStatusKbn());
			tmpMap.put("col24", tukueFlg);
			// 椅子
			String isuFlg = getBihinValueHannyu(tmpData.getIsuApplKbn(), tmpData.getIsuLentStatusKbn());
			tmpMap.put("col25", isuFlg);
			// 電子炊飯ジャー
			String suihanjaFlg = getBihinValueHannyu(tmpData.getSuihanjaApplKbn(), tmpData.getSuihanjaLentStatusKbn());
			tmpMap.put("col26", suihanjaFlg);
			// キッチンキャビネット
			String kyabinettoFlg = getBihinValueHannyu(tmpData.getKicchinkyabinettoApplKbn(), tmpData.getKyabinettoLentStatusKbn());
			tmpMap.put("col27", kyabinettoFlg);
			// ガステーブル
			String gasuteburuFlg = getBihinValueHannyu(tmpData.getGasuteburuApplKbn(), tmpData.getGasuteburuLentStatusKbn());
			tmpMap.put("col28", gasuteburuFlg);
			// カーテン
			String katenFlg = getBihinValueHannyu(tmpData.getKatenApplKbn(), tmpData.getKatenLentStatusKbn());
			tmpMap.put("col29", katenFlg);
			// 照明機器
			String shomeiFlg = getBihinValueHannyu(tmpData.getShomeiApplKbn(), tmpData.getShomeiLentStatusKbn());
			tmpMap.put("col30", shomeiFlg);
			/** 前回作成日 **/
			// 前回作成日
			tmpMap.put("col31", strNullCheck(tmpData.getHannyuCarryinInstruction()));
			/** 備考 **/
			// 備考
			tmpMap.put("col32", null);
			
			setExcelDataList.add(tmpMap);
			
			// 更新用条件の設定
			checkDuplicateId(shatakuKanriIdList, setDbUpdateDataList, HANNYU, String.valueOf(tmpData.getShatakuKanriId()), tmpData.getUpdateDateTime(), tmpData.getTableFlg());
		}

		// 返還データ
		for(int i=0; i < henkanData.size(); i++ ){
			Skf3040Sc001GetHenkanDataInfoExp tmpData = henkanData.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();

			// 業務内容
			tmpMap.put("col1", HENKAN);
			/** 入居者 **/
			// 社員番号
			tmpMap.put("col2", strNullCheck(tmpData.getShainNo()));
			// 社員氏名
			tmpMap.put("col3", strNullCheck(tmpData.getName()));
			// 社員フリガナ
			tmpMap.put("col4", strNullCheck(tmpData.getName_kk()));
			/** 立会人 **/
			// 氏名
			tmpMap.put("col5", strNullCheck(tmpData.getTatiaiDairiName()));
			// 連絡先
			if(NfwStringUtils.isEmpty(tmpData.getTatiaiDairiApoint())){
				tmpMap.put("col6", strNullCheck(tmpData.getTatiaiMyApoint()));
			}else{
				tmpMap.put("col6", strNullCheck(tmpData.getTatiaiDairiApoint()));
			}
			/** 希望日時 **/
			// 日付
			tmpMap.put("col7", strNullCheck(tmpData.getHansyutuRequestDay()));
			// 時刻
			if(NfwStringUtils.isEmpty(tmpData.getHansyutuRequestKbn())){
				tmpMap.put("col8", null);
			}else{
				tmpMap.put("col8", kibouJikanList.get(tmpData.getHansyutuRequestKbn()));
			}
			/** 社宅 **/
			// 郵便番号
			tmpMap.put("col9", strNullCheck(tmpData.getZipCd()));
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
			tmpMap.put("col10", shozaichi);
			// 社宅名
			tmpMap.put("col11", strNullCheck(tmpData.getShatakuName()));
			// 部屋番号
			tmpMap.put("col12", strNullCheck(tmpData.getRoomNo()));
			// EV有無
			if(NfwStringUtils.isEmpty(tmpData.getElevatorKbn())){
				tmpMap.put("col13", null);
			}else{
				tmpMap.put("col13", evKbnList.get(tmpData.getElevatorKbn()));
			}
			/** 備品内訳**/
			// 冷蔵庫
			String reizokoFlg = getBihinValueHenkan(tmpData.getReizokoReturnKbn());
			tmpMap.put("col14", reizokoFlg);
			// テレビ
			String tvFlg = getBihinValueHenkan(tmpData.getTerebiReturnKbn());
			tmpMap.put("col15", tvFlg);
			// テレビ台
			String tvDaiFlg = getBihinValueHenkan(tmpData.getTerebidaiReturnKbn());
			tmpMap.put("col16", tvDaiFlg);
			// エアコン
			String eakonFlg = getBihinValueHenkan(tmpData.getEakonReturnKbn());
			tmpMap.put("col17", eakonFlg);
			// 洗濯機
			String sentakukiFlg = getBihinValueHenkan(tmpData.getSentakukiReturnKbn());
			tmpMap.put("col18", sentakukiFlg);
			// 電子レンジ
			tmpMap.put("col19", null);
			// オーブンレンジ
			String obunFlg = getBihinValueHenkan(tmpData.getObunReturnKbn());
			tmpMap.put("col20", obunFlg);
			// オーブントースター
			String obuntosutaFlg = getBihinValueHenkan(tmpData.getObuntosutaReturnKbn());
			tmpMap.put("col21", obuntosutaFlg);
			// 掃除機
			String sojikiFlg = getBihinValueHenkan(tmpData.getSojikiReturnKbn());
			tmpMap.put("col22", sojikiFlg);
			// 座卓
			String zatakuFlg = getBihinValueHenkan(tmpData.getZatakuReturnKbn());
			tmpMap.put("col23", zatakuFlg);
			// 机・テーブル
			String tukueFlg = getBihinValueHenkan(tmpData.getTukueReturnKbn());
			tmpMap.put("col24", tukueFlg);
			// 椅子
			String isuFlg = getBihinValueHenkan(tmpData.getIsuReturnKbn());
			tmpMap.put("col25", isuFlg);
			// 電子炊飯ジャー
			String suihanjaFlg = getBihinValueHenkan(tmpData.getSuihanjaReturnKbn());
			tmpMap.put("col26", suihanjaFlg);
			// キッチンキャビネット
			String kyabinettoFlg = getBihinValueHenkan(tmpData.getKicchinkyabinettoReturnKbn());
			tmpMap.put("col27", kyabinettoFlg);
			// ガステーブル
			String gasuteburuFlg = getBihinValueHenkan(tmpData.getGasuteburuReturnKbn());
			tmpMap.put("col28", gasuteburuFlg);
			// カーテン
			String katenFlg = getBihinValueHenkan(tmpData.getKatenReturnKbn());
			tmpMap.put("col29", katenFlg);
			// 照明機器
			String shomeiFlg = getBihinValueHenkan(tmpData.getShomeiReturnKbn());
			tmpMap.put("col30", shomeiFlg);
			/** 前回作成日 **/
			// 前回作成日
			tmpMap.put("col31", strNullCheck(tmpData.getHansyutuCarryinInstruction()));
			/** 備考 **/
			// 備考
			tmpMap.put("col32", null);

			setExcelDataList.add(tmpMap);
			
			// 更新用条件の設定
			checkDuplicateId(shatakuKanriIdList, setDbUpdateDataList, HENKAN, String.valueOf(tmpData.getShatakuKanriId()), tmpData.getUpdateDateTime(), UPDATE_SHATAKU_BIHIN);
		}

		// 下取データ
		for(int i=0; i < shitadoriData.size(); i++ ){
			Skf3040Sc001GetShitadoriDataInfoExp tmpData = shitadoriData.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();

			// 業務内容
			tmpMap.put("col1", SHITADORI);
			/** 入居者 **/
			// 社員番号
			tmpMap.put("col2", strNullCheck(tmpData.getShainNo()));
			// 社員氏名
			tmpMap.put("col3", strNullCheck(tmpData.getName()));
			// 社員フリガナ
			tmpMap.put("col4", strNullCheck(tmpData.getName_kk()));
			/** 立会人 **/
			// 氏名
			tmpMap.put("col5", strNullCheck(tmpData.getTatiaiDairiName()));
			// 連絡先
			if(NfwStringUtils.isEmpty(tmpData.getTatiaiDairiApoint())){
				tmpMap.put("col6", strNullCheck(tmpData.getTatiaiMyApoint()));
			}else{
				tmpMap.put("col6", strNullCheck(tmpData.getTatiaiDairiApoint()));
			}
			/** 希望日時 **/
			// 日付
			tmpMap.put("col7", strNullCheck(tmpData.getHansyutuRequestDay()));
			// 時刻
			if(NfwStringUtils.isEmpty(tmpData.getHansyutuRequestKbn())){
				tmpMap.put("col8", null);
			}else{
				tmpMap.put("col8", kibouJikanList.get(tmpData.getHansyutuRequestKbn()));
			}
			/** 社宅 **/
			// 郵便番号
			tmpMap.put("col9", strNullCheck(tmpData.getZipCd()));
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
			tmpMap.put("col10", shozaichi);
			// 社宅名
			tmpMap.put("col11", strNullCheck(tmpData.getShatakuName()));
			// 部屋番号
			tmpMap.put("col12", strNullCheck(tmpData.getRoomNo()));
			// EV有無
			if(NfwStringUtils.isEmpty(tmpData.getElevatorKbn())){
				tmpMap.put("col13", null);
			}else{
				tmpMap.put("col13", evKbnList.get(tmpData.getElevatorKbn()));
			}
			/** 備品内訳**/
			// 冷蔵庫
			String reizokoFlg = getBihinValueShitadori(tmpData.getReizokoReturnKbn());
			tmpMap.put("col14", reizokoFlg);
			// テレビ
			String tvFlg = getBihinValueShitadori(tmpData.getTerebiReturnKbn());
			tmpMap.put("col15", tvFlg);
			// テレビ台
			String tvDaiFlg = getBihinValueShitadori(tmpData.getTerebidaiReturnKbn());
			tmpMap.put("col16", tvDaiFlg);
			// エアコン
			String eakonFlg = getBihinValueShitadori(tmpData.getEakonReturnKbn());
			tmpMap.put("col17", eakonFlg);
			// 洗濯機
			String sentakukiFlg = getBihinValueShitadori(tmpData.getSentakukiReturnKbn());
			tmpMap.put("col18", sentakukiFlg);
			// 電子レンジ
			tmpMap.put("col19", null);
			// オーブンレンジ
			String obunFlg = getBihinValueShitadori(tmpData.getObunReturnKbn());
			tmpMap.put("col20", obunFlg);
			// オーブントースター
			String obuntosutaFlg = getBihinValueShitadori(tmpData.getObuntosutaReturnKbn());
			tmpMap.put("col21", obuntosutaFlg);
			// 掃除機
			String sojikiFlg = getBihinValueShitadori(tmpData.getSojikiReturnKbn());
			tmpMap.put("col22", sojikiFlg);
			// 座卓
			String zatakuFlg = getBihinValueShitadori(tmpData.getZatakuReturnKbn());
			tmpMap.put("col23", zatakuFlg);
			// 机・テーブル
			String tukueFlg = getBihinValueShitadori(tmpData.getTukueReturnKbn());
			tmpMap.put("col24", tukueFlg);
			// 椅子
			String isuFlg = getBihinValueShitadori(tmpData.getIsuReturnKbn());
			tmpMap.put("col25", isuFlg);
			// 電子炊飯ジャー
			String suihanjaFlg = getBihinValueShitadori(tmpData.getSuihanjaReturnKbn());
			tmpMap.put("col26", suihanjaFlg);
			// キッチンキャビネット
			String kyabinettoFlg = getBihinValueShitadori(tmpData.getKicchinkyabinettoReturnKbn());
			tmpMap.put("col27", kyabinettoFlg);
			// ガステーブル
			String gasuteburuFlg = getBihinValueShitadori(tmpData.getGasuteburuReturnKbn());
			tmpMap.put("col28", gasuteburuFlg);
			// カーテン
			String katenFlg = getBihinValueShitadori(tmpData.getKatenReturnKbn());
			tmpMap.put("col29", katenFlg);
			// 照明機器
			String shomeiFlg = getBihinValueShitadori(tmpData.getShomeiReturnKbn());
			tmpMap.put("col30", shomeiFlg);
			/** 前回作成日 **/
			// 前回作成日
			tmpMap.put("col31", strNullCheck(tmpData.getHansyutuCarryinInstruction()));
			/** 備考 **/
			// 備考
			tmpMap.put("col32", null);

			setExcelDataList.add(tmpMap);

			// 更新用条件の設定
			checkDuplicateId(shatakuKanriIdList, setDbUpdateDataList, SHITADORI, String.valueOf(tmpData.getShatakuKanriId()), tmpData.getUpdateDateTime(), UPDATE_SHATAKU_BIHIN);
		}
		
	}
	
	/**
	 * 備品レンタル値をチェック（搬入）
	 * 
	 */
	private String getBihinValueHannyu(String applKbn, String lentStatusKbn){
	
		String retValue = null;
		if(NfwStringUtils.isNotEmpty(applKbn) && Objects.equals(applKbn, CodeConstant.BIHIN_SHINSEI_KBN_ARI)){
			retValue = BIHIN_OUTPUT_VALUE;
		}
		if(NfwStringUtils.isNotEmpty(lentStatusKbn) && Objects.equals(lentStatusKbn, CodeConstant.BIHIN_STATE_RENTAL)){
			retValue = BIHIN_OUTPUT_VALUE;
		}
		return retValue;
	}

	/**
	 * 備品レンタル値をチェック（返還）
	 * 
	 */
	private String getBihinValueHenkan(String returnKbn){
	
		String retValue = null;
		if(NfwStringUtils.isNotEmpty(returnKbn) && Objects.equals(returnKbn, CodeConstant.BIHIN_HENKYAKU_KBN_RENTAL_HENKYAKU)){
			retValue = BIHIN_OUTPUT_VALUE;
		}
		return retValue;
	}

	/**
	 * 備品レンタル値をチェック（下取）
	 * 
	 */
	private String getBihinValueShitadori(String returnKbn){
	
		String retValue = null;
		if(NfwStringUtils.isNotEmpty(returnKbn) && Objects.equals(returnKbn, CodeConstant.BIHIN_HENKYAKU_KBN_KAISHA_HOYU_HENKYAKU)){
			retValue = BIHIN_OUTPUT_VALUE;
		}
		return retValue;
	}

	
	/**
	 * 社宅管理IDの重複チェック
	 * @param shatakuKanriIdList
	 * @param setDbUpdateDataList
	 * @param gyoumu
	 * @param shatakuKanriId
	 * @param updateTime
	 * @param tableFlg
	 * 
	 */
	private void checkDuplicateId(List<String> shatakuKanriIdList, List<Map<String, Object>> setDbUpdateDataList,
			String gyoumu, String shatakuKanriId, String updateTime, String tableFlg){
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
			// 更新対象テーブル
			if(Objects.equals(gyoumu, HANNYU)){
				if(Objects.equals(tableFlg, UPDATE_SHATAKU_BIHIN)){
					tmpUpdateMap.put("key4", UPDATE_SHATAKU_BIHIN);
				}else{
					tmpUpdateMap.put("key4", UPDATE_TEIJI_DATA);
				}
			}else{
				tmpUpdateMap.put("key4", UPDATE_SHATAKU_BIHIN);
			}
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
	private SheetDataBean createWorkSheetRentalBihin(List<Map<String, Object>> setExcelDataList){
		
		// Excelワークシート
		SheetDataBean sheetDataBean = new SheetDataBean();
		// Excel行データ
		List<RowDataBean> rowDataBeanList = new ArrayList<>();		
		
		// 出力日の設定(AE2)
		RowDataBean rdbOutputDate = new RowDataBean();
		int outputDateRow = excelOutPutStartLine - 10;
        String sysDate = getNowDateStrFormat(SkfCommonConstant.YMD_STYLE_YYYYMMDD_JP_STR);
        //String sysDate = getNowDateStrFormat(SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
		rdbOutputDate.addCellDataBean("AF" + outputDateRow, sysDate);				
		// 行データ追加
		rowDataBeanList.add(rdbOutputDate);	
		
		// 一覧データセット
		for(int i = 0; i < setExcelDataList.size(); i++){
			// 行データ
			RowDataBean rdb = new RowDataBean();
			Map<String, Object> getRowData = setExcelDataList.get(i);
			int rowNo = i + 12;
			//int rowNo = i + 1;
			
			/** 業務内容 **/
			// 業務内容(A:col1)
			rdb.addCellDataBean("A" + rowNo, strNullCheckExcel(getRowData.get("col1")));
			/** 入居者 **/
			// 社員番号(B:col2)
			rdb.addCellDataBean("B" + rowNo, strNullCheckExcel(getRowData.get("col2")));
			// 社員氏名(C:col3)
			rdb.addCellDataBean("C" + rowNo, strNullCheckExcel(getRowData.get("col3")));
			// 社員フリガナ(D:col4)
			rdb.addCellDataBean("D" + rowNo, strNullCheckExcel(getRowData.get("col4")));
			/** 立会人 **/
			// 氏名(D:col4)
			rdb.addCellDataBean("E" + rowNo, strNullCheckExcel(getRowData.get("col5")));
			// 連絡先(E:col5)
			rdb.addCellDataBean("F" + rowNo, strNullCheckExcel(getRowData.get("col6")));
			/** 希望日時 **/
			// 日付(F:col6)
			String kibouDate = strNullCheckExcel(getRowData.get("col7"));
			if(CheckUtils.isEmpty(kibouDate)){
				// 空
				rdb.addCellDataBean("G" + rowNo, kibouDate);
			}else{
				// フォーマット指定して設定
				rdb.addCellDataBean(
						"G" + rowNo, 
						skfDateFormatUtils.dateFormatFromString(kibouDate, SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH),
						Cell.CELL_TYPE_NUMERIC,
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);				
			}
			// 時刻(G:col7)
			rdb.addCellDataBean("H" + rowNo, strNullCheckExcel(getRowData.get("col8")));
			/** 社宅 **/
			// 郵便番号(H:col8)
			String zipCode = strNullCheckExcel(getRowData.get("col9"));
			if(CheckUtils.isEmpty(zipCode)){
				// 空
				rdb.addCellDataBean("I" + rowNo, zipCode);
			}else{
				// フォーマット指定して設定
				if(zipCode.length() == 7){
					String newZipCode = zipCode.substring(0, 3) + "-" + zipCode.substring(3, 7);
					rdb.addCellDataBean("I" + rowNo, newZipCode);
				}else{
					rdb.addCellDataBean("I" + rowNo, zipCode);
				}
			}			
			// 所在地(I:col9)
			rdb.addCellDataBean("J" + rowNo, strNullCheckExcel(getRowData.get("col10")));
			// 社宅名(J:col10)
			rdb.addCellDataBean("K" + rowNo, strNullCheckExcel(getRowData.get("col11")));
			// 部屋番号(K:col11)
			rdb.addCellDataBean("L" + rowNo, strNullCheckExcel(getRowData.get("col12")));
			// EV有無(L:col12)
			rdb.addCellDataBean("M" + rowNo, strNullCheckExcel(getRowData.get("col13")));
			/** 備品内訳 **/
			// 冷蔵庫(M:col13)
			rdb.addCellDataBean("N" + rowNo, strNullCheckExcel(getRowData.get("col14")));
			// テレビ(N:col14)
			rdb.addCellDataBean("O" + rowNo, strNullCheckExcel(getRowData.get("col15")));
			// テレビ台(O:col15)
			rdb.addCellDataBean("P" + rowNo, strNullCheckExcel(getRowData.get("col16")));
			// エアコン(P:col16)
			rdb.addCellDataBean("Q" + rowNo, strNullCheckExcel(getRowData.get("col17")));
			// 洗濯機(Q:col17)
			rdb.addCellDataBean("R" + rowNo, strNullCheckExcel(getRowData.get("col18")));
			// 電子レンジ(R:col18)
			rdb.addCellDataBean("S" + rowNo, strNullCheckExcel(getRowData.get("col19")));
			// オーブンレンジ(S:col19)
			rdb.addCellDataBean("T" + rowNo, strNullCheckExcel(getRowData.get("col20")));
			// オーブントースター(T:col20)
			rdb.addCellDataBean("U" + rowNo, strNullCheckExcel(getRowData.get("col21")));
			// 掃除機(U:col21)
			rdb.addCellDataBean("V" + rowNo, strNullCheckExcel(getRowData.get("col22")));
			// 座卓(V:col22)
			rdb.addCellDataBean("W" + rowNo, strNullCheckExcel(getRowData.get("col23")));
			// 机・テーブル(W:col23)
			rdb.addCellDataBean("X" + rowNo, strNullCheckExcel(getRowData.get("col24")));
			// 椅子(X:col24)
			rdb.addCellDataBean("Y" + rowNo, strNullCheckExcel(getRowData.get("col25")));
			// 電子炊飯ジャー(Y:col25)
			rdb.addCellDataBean("Z" + rowNo, strNullCheckExcel(getRowData.get("col26")));
			// キッチンキャビネット(Z:col26)
			rdb.addCellDataBean("AA" + rowNo, strNullCheckExcel(getRowData.get("col27")));
			// ガステーブル(AA:col27)
			rdb.addCellDataBean("AB" + rowNo, strNullCheckExcel(getRowData.get("col28")));
			// カーテン(AB:col28)
			rdb.addCellDataBean("AC" + rowNo, strNullCheckExcel(getRowData.get("col29")));
			// 照明器具(AC:col29)
			rdb.addCellDataBean("AD" + rowNo, strNullCheckExcel(getRowData.get("col30")));
			/** 前回作成日 **/
			// 前回作成日(AD:col30)
			String prevCreateDate = strNullCheckExcel(getRowData.get("col31"));
			if(CheckUtils.isEmpty(prevCreateDate)){
				// 空
				rdb.addCellDataBean("AE" + rowNo, prevCreateDate);
			}else{
				// フォーマット指定して設定
				rdb.addCellDataBean(
						"AE" + rowNo, 
						skfDateFormatUtils.dateFormatFromString(prevCreateDate, SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH),
						Cell.CELL_TYPE_NUMERIC,
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);				
			}
			
			/** 備考 **/
			// 備考(AE:col31)
			rdb.addCellDataBean("AF" + rowNo, strNullCheckExcel(getRowData.get("col32")));
			
			// 行データ追加
			rowDataBeanList.add(rdb);			
		}
		
		sheetDataBean.setRowDataBeanList(rowDataBeanList);	
		sheetDataBean.setSheetName(excelWorkSheetName);		
		return sheetDataBean;
	}
	
	
	
	/**
	 * 現物支給価額リスト帳票出力
	 * 
	 * @param rentalBihinSheet	レンタル備品指示書ワークシート
	 * @param downloadDto	DTO
	 * @throws Exception
	 */
	private void fileOutPutExcelContractInfo(SheetDataBean rentalBihinSheet,
			Skf3040Sc001DownloadDto downloadDto) throws Exception {

		List<SheetDataBean> sheetDataBeanList = new ArrayList<>();
		sheetDataBeanList.add(rentalBihinSheet);

		Map<String, Object> cellparams = new HashMap<>(); // 列書式(背景、フォント)
		Map<String, Object> resultMap = new HashMap<>(); // 列書式(背景、フォント)

		String uploadFileName = excelFileName + DateTime.now().toString("YYYYMMddHHmmss") + ".xlsx";
		WorkBookDataBean wbdb = new WorkBookDataBean(uploadFileName);
		wbdb.setSheetDataBeanList(sheetDataBeanList);
		// Excelファイルへ出力
		SkfFileOutputUtils.fileOutputExcel(wbdb, cellparams, fileName, "skf3040rp001",
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
