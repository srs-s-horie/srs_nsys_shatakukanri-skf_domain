/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc007;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.emory.mathcs.backport.java.util.Collections;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShataku;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuParkingBlock;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuParkingContract;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuParkingContractKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc007.Skf3010Sc007UpdateParkingContractExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuParkingBlockRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuParkingContractRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.LoginUserInfoUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc007.Skf3010Sc007RegistDto;
import jp.co.intra_mart.mirage.integration.guice.Transactional;


/**
 * Skf3010Sc005RegistService 社宅部屋登録登録ボタン押下時処理クラス.
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3010Sc007RegistService extends BaseServiceAbstract<Skf3010Sc007RegistDto> {

	@Autowired
	private Skf3010MShatakuParkingContractRepository skf3010MShatakuParkingContractRepository;
	
	@Autowired
	private Skf3010MShatakuParkingBlockRepository skf3010MShatakuParkingBlockRepository;
	
	@Autowired
	private Skf3010Sc007UpdateParkingContractExpRepository skf3010Sc007UpdateParkingContractExpRepository;
	
	@Autowired
	private Skf3010Sc007SharedService skf3010Sc007SharedService;
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private Skf3010MShatakuRepository skf3010MShatakuRepository;
	
	private final static String TRUE = "true";
	private final static String FALSE = "false";
	
	/**
	 * 登録処理メインメソッド.
	 */
	@Override
	@Transactional
	protected BaseDto index(Skf3010Sc007RegistDto registDto) throws Exception {

		registDto.setPageTitleKey(MessageIdConstant.SKF3010_SC007_TITLE);
		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("登録", CodeConstant.C001, registDto.getPageId());
		
		// Debugログで出力
		LogUtils.debugByMsg("駐車場契約情報-区画番号：" + registDto.getHdnParkingBlock());
		LogUtils.debugByMsg("駐車場契約情報-契約番号：" + registDto.getContractPropertyId());
		LogUtils.debugByMsg("駐車場契約情報-駐車場管理番号：" + registDto.getParkingKanriNo());
		
		//選択中区画番号
		String selectParkingBlock = registDto.getHdnParkingBlock();
		String selectParkingKanriNo = registDto.getParkingKanriNo();
		//選択中契約番号
		Long selectContractPropertyId;
		//登録、更新
		boolean modeUpdate = false;
		

		// エラー系のDto値を初期化
		//エラー変数初期化
		registDto.setOwnerNameError(CodeConstant.DOUBLE_QUOTATION);
		registDto.setParkingZipCdError(CodeConstant.DOUBLE_QUOTATION);
		registDto.setParkingAddressError(CodeConstant.DOUBLE_QUOTATION);
		registDto.setParkingNameError(CodeConstant.DOUBLE_QUOTATION);
		registDto.setAssetRegisterNoError(CodeConstant.DOUBLE_QUOTATION);
		registDto.setContractStartDateError(CodeConstant.DOUBLE_QUOTATION);
		registDto.setContractEndDateError(CodeConstant.DOUBLE_QUOTATION);
		registDto.setLandRentError(CodeConstant.DOUBLE_QUOTATION);
		registDto.setParkingContractTypeError(CodeConstant.DOUBLE_QUOTATION);
		registDto.setContractPropertyIdError(CodeConstant.DOUBLE_QUOTATION);
		
		
		// 入力値チェック
		if (isValidateInput(registDto) == false) {
			// 入力チェックエラーの場合、処理を終了する
			//契約形態リスト
			List<Map<String, Object>> parkingContractTypeList = new ArrayList<Map<String, Object>>();
			//貸与区分リスト
			List<Map<String, Object>> parkinglendKbnList = new ArrayList<Map<String, Object>>();
			
			skf3010Sc007SharedService.getDoropDownList(registDto.getParkingContractType(), parkingContractTypeList,
					registDto.getParkinglendKbn(), parkinglendKbnList);
			registDto.setParkinglendKbnList(parkinglendKbnList);
			registDto.setParkingContractTypeList(parkingContractTypeList);
						
			if(registDto.getParkingContractType().compareTo(Skf3010Sc007CommonSharedService.CONTRACT_TYPE_2) == 0){
				//社宅と別契約
				//駐車場契約情報の「支援」「住所検索」ボタンを活性にする。
				registDto.setContractInfoDisabled(FALSE);
			}else{
				//社宅と一括契約の場合
				//駐車場契約情報の「支援」「住所検索」ボタンを非活性にする。
				registDto.setContractInfoDisabled(TRUE);
			}
			return registDto;
		}
		
		if( !SkfCheckUtils.isNullOrEmpty(registDto.getContractPropertyId())){
			selectContractPropertyId = Long.parseLong(registDto.getContractPropertyId());
		}else{
			selectContractPropertyId = CodeConstant.LONG_ZERO;//0設定
		}

		//削除情報が無いかチェック
		if(CodeConstant.STRING_ZERO.compareTo(registDto.getHdnDelInfoFlg()) != 0){
			for(Map<String, Object> map : registDto.getHdnListData()){
				//区画番号の一致確認
				if(selectParkingKanriNo.compareTo(map.get("parkingKanriNo").toString()) == 0 ){
					//削除フラグチェック
					String deleteFlag = map.get("deleteFlag").toString();
					if(deleteFlag.compareTo(Skf3010Sc007CommonSharedService.DELETE_FLAG_ON) == 0){
						//削除フラグON
						//削除用主キー
						Skf3010MShatakuParkingContractKey key = new Skf3010MShatakuParkingContractKey();
						//対象の主キー取得
						Long contractPropertyId = Long.parseLong(map.get("contractPropertyId").toString());
						Long shatakuKanriNo = Long.parseLong(registDto.getHdnShatakuKanriNo());
						Long parkingKanriNo =  Long.parseLong(map.get("parkingKanriNo").toString());
						
						key.setContractPropertyId(contractPropertyId);
						key.setShatakuKanriNo(shatakuKanriNo);
						key.setParkingKanriNo(parkingKanriNo);
						
						//排他用更新日時
						String contractUpdateDate = map.get("contractUpdateDate").toString();
						
						int deleteCount = 0;
						
						LogUtils.debugByMsg("駐車場契約情報-削除実行：" + shatakuKanriNo + "," + parkingKanriNo 
								+"," + contractPropertyId);
						
						deleteCount = deleteParkingContractInfo(key,contractUpdateDate);
						if(deleteCount < 0){
							//件数が0未満（排他エラー）「他のユーザによって更新されています。もう一度画面を更新してください。」
							ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.W_SKF_1009);
							throwBusinessExceptionIfErrors(registDto.getResultMessages());
						}else if(deleteCount == 0){
							//その他エラー「削除時にエラーが発生しました。ヘルプデスクへ連絡してください。」
							ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1076);
							throwBusinessExceptionIfErrors(registDto.getResultMessages());
						}
						//正常時は処理続行
						LogUtils.debugByMsg("駐車場契約情報-削除成功：" + shatakuKanriNo + "," + parkingKanriNo 
								+"," + contractPropertyId);
					}
					
				}
			}
		}
		
		//登録・更新
		for(Map<String, Object> map : registDto.getHdnListData()){
			//区画番号の一致確認
			if(selectParkingKanriNo.compareTo(map.get("parkingKanriNo").toString()) == 0 ){
				//契約番号
				Long mapContractPropertyId = Long.parseLong(map.get("contractPropertyId").toString());
				
				String blockUpdateDate = map.get("blockUpdateDate").toString();
				registDto.setBlockUpdateDate(blockUpdateDate);
				//削除フラグチェック
				String deleteFlag = map.get("deleteFlag").toString();
				//契約番号の一致確認
				if(Objects.equals(selectContractPropertyId, mapContractPropertyId)){
					if(deleteFlag.compareTo(Skf3010Sc007CommonSharedService.DELETE_FLAG_ON) != 0){
						//契約情報有かつ削除フラグ1以外で、更新フラグON
						modeUpdate=true;
						String contractUpdateDate = map.get("contractUpdateDate").toString();
						registDto.setContractUpdateDate(contractUpdateDate);
						break;
					}
				}
				
			}
		}
		
		if (modeUpdate) {
			/** 更新処理 */
			LogUtils.debugByMsg("駐車場契約情報更新");
						
			int resultCount = registParkingContractInfo(registDto,modeUpdate);
			
			if(resultCount < 0){
				//件数が0未満（排他エラー）
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.W_SKF_1009);
				throwBusinessExceptionIfErrors(registDto.getResultMessages());
			}
			else if(resultCount == 0){
				//更新エラー
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1075);
				throwBusinessExceptionIfErrors(registDto.getResultMessages());
			}
			//成功メッセージ
			ServiceHelper.addResultMessage(registDto, MessageIdConstant.I_SKF_1011);
			
		} else {
			/** 登録処理 */
			LogUtils.debugByMsg("駐車場契約情報登録");
			
			int resultCount = registParkingContractInfo(registDto,modeUpdate);
			
			if(resultCount < 0){
				//件数が0未満（排他エラー）
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.W_SKF_1009);
				throwBusinessExceptionIfErrors(registDto.getResultMessages());
			}
			else if(resultCount == 0){
				//登録エラー
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1073);
				throwBusinessExceptionIfErrors(registDto.getResultMessages());
			}
			
			//成功メッセージ
			ServiceHelper.addResultMessage(registDto, MessageIdConstant.I_SKF_1012);

		}

		// リストデータ取得用
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
		//データ保持用リスト
		List<Map<String, Object>> hdnListData = new ArrayList<Map<String, Object>>();
		// リストテーブルの情報を再取得
		skf3010Sc007SharedService.getListTableData(Long.parseLong(registDto.getHdnShatakuKanriNo()), listTableData, hdnListData);
		
		registDto.setListTableData(listTableData);
		registDto.setHdnListData(hdnListData);
		registDto.setHdnDelInfoFlg(CodeConstant.STRING_ZERO);
		
		//再表示用パラメータ
		registDto.setSelectMode("mainList");
		registDto.setHdnParkingBlock(selectParkingBlock);
		registDto.setContractPropertyId(selectContractPropertyId.toString());
		
		//再表示処理
		setRegistedDisp(registDto);
		
		return registDto;
	}

	/**
	 * 入力チェックを行う.
	 * 
	 * @param registDto *indexメソッドの引数のDTO。入力チェックエラーの場合、エラーメッセージを詰める。
	 * @return 入力チェック結果。trueの場合チェックOK。
	 * @throws UnsupportedEncodingException 桁数チェック時の例外
	 */
	private boolean isValidateInput(Skf3010Sc007RegistDto registDto) throws UnsupportedEncodingException {

		boolean isCheckOk = true;

		String debugMessage = "";

		/** 必須入力チェック */
		// 契約番号
		if (SkfCheckUtils.isNullOrEmpty(registDto.getContractPropertyId())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "契約番号");
			registDto.setContractPropertyIdError(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 必須入力チェック - 契約番号";
		}
		// 契約形態
		if (SkfCheckUtils.isNullOrEmpty(registDto.getParkingContractType())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "契約形態");
			registDto.setParkingContractTypeError(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 必須入力チェック - 契約形態";
		}
		//契約形態チェック
		if(registDto.getParkingContractType().compareTo(Skf3010Sc007CommonSharedService.CONTRACT_TYPE_2) == 0) {
			//社宅と別契約の場合
			//賃貸人（代理人）
			if (SkfCheckUtils.isNullOrEmpty(registDto.getOwnerName())) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "賃貸人（代理人）");
				registDto.setOwnerNameError(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += " 必須入力チェック - 賃貸人（代理人）";
			}
			
			// 住所
			if (SkfCheckUtils.isNullOrEmpty(registDto.getParkingAddress())) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "住所");
				registDto.setParkingAddressError(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += " 必須入力チェック - 住所";
			}
			
			// 駐車場名
			if (SkfCheckUtils.isNullOrEmpty(registDto.getParkingName())) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "駐車場名");
				registDto.setParkingNameError(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += " 必須入力チェック - 駐車場名";
			}
			
			// 経理連携用管理番号
			if (SkfCheckUtils.isNullOrEmpty(registDto.getAssetRegisterNo())) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "経理連携用管理番号");
				registDto.setAssetRegisterNoError(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += " 必須入力チェック - 経理連携用管理番号";
			}
			
			// 契約開始日
			if (SkfCheckUtils.isNullOrEmpty(registDto.getSetContractStartDate())) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "契約開始日");
				registDto.setContractStartDateError(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += " 必須入力チェック - 契約開始日";
			}
			
			// 駐車場代（地代） 
			String rentStr = registDto.getLandRentNum();
			if(SkfCheckUtils.isNullOrEmpty(rentStr)){
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "駐車場代（地代） ");
				registDto.setLandRentError(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += " 必須入力チェック - 駐車場代（地代）";
			}
//			if(registDto.getLandRent() == null){
//				isCheckOk = false;
//				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "駐車場代（地代） ");
//				registDto.setLandRentError(CodeConstant.NFW_VALIDATION_ERROR);
//				debugMessage += " 必須入力チェック - 駐車場代（地代）NULL ";
//			}else if (SkfCheckUtils.isNullOrEmpty(registDto.getLandRent().toString())) {
//				isCheckOk = false;
//				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "駐車場代（地代） ");
//				registDto.setLandRentError(CodeConstant.NFW_VALIDATION_ERROR);
//				debugMessage += " 必須入力チェック - 駐車場代（地代） ";
//			}
			
			/** 必須チェックOKなら形式チェック */
			if (isCheckOk) {
				// 契約開始日
				String contractStartDate = registDto.getSetContractStartDate();
				if (!SkfCheckUtils.isSkfDateFormat(contractStartDate,CheckUtils.DateFormatType.YYYYMMDD)) {
					isCheckOk = false;
					ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1055, "契約開始日");
					registDto.setContractStartDateError(CodeConstant.NFW_VALIDATION_ERROR);
					debugMessage += " 形式チェック - 契約開始日 - " + contractStartDate;
				}
				
				// 契約終了日
				String contractEndDate = registDto.getSetContractEndDate();
				if (!SkfCheckUtils.isNullOrEmpty(contractEndDate)) {
					//形式
					if (!SkfCheckUtils.isSkfDateFormat(contractEndDate,CheckUtils.DateFormatType.YYYYMMDD)) {
						isCheckOk = false;
						ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1055, "契約終了日");
						registDto.setContractEndDateError(CodeConstant.NFW_VALIDATION_ERROR);
						debugMessage += " 形式チェック - 契約終了日 - " + contractEndDate;
					}
					
					if(isCheckOk){
						//日付関係
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
						Date startDate = null;
						Date endDate = null;
						try{
							startDate = dateFormat.parse(contractStartDate);
							endDate = dateFormat.parse(contractEndDate);
							
							if(!startDate.before(endDate)){
								//契約開始日＞契約終了日の場合(開始日が終了日より前で無い場合）
								isCheckOk = false;
								ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1055, "契約終了日");
								registDto.setContractEndDateError(CodeConstant.NFW_VALIDATION_ERROR);
								debugMessage += " 形式チェック - 契約終了日(開始日以前) - " +contractEndDate;
							}
							
						}catch(ParseException ex){
							isCheckOk = false;
							if(startDate == null){
								ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1055, "契約開始日");
								registDto.setContractStartDateError(CodeConstant.NFW_VALIDATION_ERROR);
								debugMessage += " 形式チェック - 契約開始日 - " + contractStartDate;
							}
							if(endDate == null){
								ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1055, "契約終了日");
								registDto.setContractEndDateError(CodeConstant.NFW_VALIDATION_ERROR);
								debugMessage += " 形式チェック - 契約終了日 - " + contractEndDate;
							}
							
						}
					}
					
				}
				
				// 経理連携用管理番号
				if (!CheckUtils.isHalfWidth(registDto.getAssetRegisterNo())) {
					isCheckOk = false;
					ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1002, "経理連携用管理番号");
					registDto.setAssetRegisterNoError(CodeConstant.NFW_VALIDATION_ERROR);
					debugMessage += " 形式チェック - 経理連携用管理番号 - " +registDto.getAssetRegisterNo();
				}
			}
		}
		// デバッグメッセージ出力
		if (isCheckOk) {
			LogUtils.debugByMsg("isValidateInput, 入力チェックOK：" + debugMessage);
		} else {
			LogUtils.infoByMsg("isValidateInput, 入力チェックNG：" + debugMessage);
		}

		return isCheckOk;
	}

	
	/**
	 * 社宅駐車場契約情報マスタ、社宅駐車場区画情報マスタに対して登録または更新処理を行う。<br>
	 * 登録、更新の判断は更新フラグによって判断する。.
	 * 
	 * @param dto indexメソッドの引数であるDto
	 * @return　登録または更新件数
	 */
	private int registParkingContractInfo(Skf3010Sc007RegistDto dto,boolean modeUpdate) {

		Skf3010MShatakuParkingContract  setValue = new Skf3010MShatakuParkingContract();
		Skf3010MShatakuParkingBlock blockSetValue = new Skf3010MShatakuParkingBlock();
		

		/** 登録項目をセット */
		//社宅管理番号
		Long shatakuKanriNo = Long.parseLong(dto.getHdnShatakuKanriNo());
		setValue.setShatakuKanriNo(shatakuKanriNo);
		blockSetValue.setShatakuKanriNo(shatakuKanriNo);
		//契約番号（契約物件ID）
		Long contractPropertyId = Long.parseLong(dto.getContractPropertyId());
		setValue.setContractPropertyId(contractPropertyId);		
		//駐車場管理番号
		Long parkingKanriNo =  Long.parseLong(dto.getParkingKanriNo());
		setValue.setParkingKanriNo(parkingKanriNo);
		blockSetValue.setParkingKanriNo(parkingKanriNo);
		
		//賃貸人（代理人）番号
		if(!SkfCheckUtils.isNullOrEmpty(dto.getOwnerNo())){
			Long ownerNo =  Long.parseLong(dto.getOwnerNo());
			setValue.setOwnerNo(ownerNo);
		}
		
		//経理連携用資産番号
		if(dto.getAssetRegisterNo() != null){
			setValue.setAssetRegisterNo(dto.getAssetRegisterNo());
		}
		
		//契約開始日
		if(dto.getSetContractStartDate() != null){
			String startDate = skfDateFormatUtils.dateFormatFromString(dto.getSetContractStartDate(), "yyyyMMdd");
			setValue.setContractStartDate(startDate);
		}
		
		//契約終了日
		if(dto.getSetContractEndDate() != null){
			String endDate = skfDateFormatUtils.dateFormatFromString(dto.getSetContractEndDate(), "yyyyMMdd");
			setValue.setContractEndDate(endDate);
		}
		
		//駐車場代（地代） 
		String landRentNum = dto.getLandRentNum();
		if(!SkfCheckUtils.isNullOrEmpty(landRentNum)){
			String tempLandRent = landRentNum.replace(",", "");
			int landRent =  Integer.parseInt(tempLandRent);
			setValue.setLandRent(landRent);
		}
		
		//備考
		if(dto.getBiko() != null){
			setValue.setBiko(dto.getBiko());
		}
		
		//駐車場契約形態
		String contractType = dto.getParkingContractType();
		setValue.setParkingContractType(contractType);
		//駐車場所在地区分
		setValue.setParkingAddressKbn(contractType);
		
		// 駐車場名
		if(dto.getParkingName() != null){
			setValue.setParkingName(dto.getParkingName());
		}
		
		//駐車場郵便番号
		if(dto.getParkingZipCd() != null){
			setValue.setParkingZipCd(dto.getParkingZipCd());
		}
		//駐車場所在地
		if(Skf3010Sc007CommonSharedService.CONTRACT_TYPE_1.equals(contractType)){
			//一括契約の場合、社宅の住所を設定
			String prefCode = CodeConstant.DOUBLE_QUOTATION;
			String address = CodeConstant.DOUBLE_QUOTATION;
			Skf3010MShataku shatakuInfo = skf3010MShatakuRepository.selectByPrimaryKey(shatakuKanriNo);
			if(shatakuInfo != null){
				prefCode = shatakuInfo.getPrefCd();
				address = shatakuInfo.getAddress();
				Map<String, String> genericCodeMapPref = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_PREFCD);
				String pref ="";
				if (!SkfCheckUtils.isNullOrEmpty(prefCode)) {
					pref = genericCodeMapPref.get(prefCode);
				}
				setValue.setParkingAddress(pref + address);
			}
		}else{
			if(dto.getParkingAddress() != null){
				setValue.setParkingAddress(dto.getParkingAddress());
			}
		}
		
		//区画情報
		//貸与区分
		if(dto.getParkinglendKbn() != null){
			blockSetValue.setParkingLendKbn(dto.getParkinglendKbn());
		}
		
		/** 登録 */
		SimpleDateFormat dateFormat = new SimpleDateFormat(Skf3010Sc007CommonSharedService.DATE_FORMAT);
		LogUtils.debugByMsg("DBCommonItems：" + this.dbCommonItems.toString());
		int registCount = 0;
		if (modeUpdate) {
			// 更新の場合はUPDATE
			setValue.setUpdateUserId(LoginUserInfoUtils.getUserCd());
			setValue.setUpdateProgramId(dto.getPageId());
			try{
				//更新日時設定
				setValue.setLastUpdateDate(dateFormat.parse(dto.getContractUpdateDate()));
			}	
			catch(ParseException ex){
				LogUtils.infoByMsg("registParkingContractInfo, 駐車場契約情報-更新日時変換NG :" + dto.getContractUpdateDate());
				return 0;
			}
			//更新処理
			registCount = updateParkingContract(setValue);
			
		} else {
			// 新規登録の場合はINSERT
			
			//登録処理
			registCount = insertParkingContract(setValue);
			
		}
		//区画情報の更新
		if(registCount > 0 && !SkfCheckUtils.isNullOrEmpty(blockSetValue.getParkingLendKbn())){
			//契約情報更新有、かつ貸与区分が設定ある場合更新
			try{
				//更新日時設定
				blockSetValue.setLastUpdateDate(dateFormat.parse(dto.getBlockUpdateDate()));
			}	
			catch(ParseException ex){
				LogUtils.infoByMsg("registParkingContractInfo, 駐車場区画情報-更新日時変換NG :" + dto.getBlockUpdateDate());
				return 0;
			}
			
			registCount = updateParkingBlock(blockSetValue);
		}
		
		return registCount;

	}

	/**
	 * 駐車場契約情報を登録する.
	 * 
	 * @param setValue 更新値を含むSkf3010MShatakuParkingContractインスタンス
	 * @return 登録件数
	 */
	private int insertParkingContract(Skf3010MShatakuParkingContract setValue) {

		int registCount=0;

		//登録処理
		registCount = skf3010MShatakuParkingContractRepository.insertSelective(setValue);
		
		if(registCount <= 0){
			//登録件数0以下の場合終了
			return 0;
		}
		
		return registCount;

	}
	
	
	/**
	 * 駐車場契約情報の削除
	 * @param key
	 * @return
	 */
	private int deleteParkingContractInfo(Skf3010MShatakuParkingContractKey key,String contractUpdateDate){
		//削除数
		int deleteCount = 0;
		SimpleDateFormat dateFormat = new SimpleDateFormat(Skf3010Sc007CommonSharedService.DATE_FORMAT);
		
		Skf3010MShatakuParkingContract contractInfo =  skf3010MShatakuParkingContractRepository.selectByPrimaryKey(key);
		
		if(contractInfo==null){
			//情報取得無しは排他エラー
			LogUtils.debugByMsg("駐車場契約情報取得結果NULL");
			return -1;
		}
		
		try{
			Date mapDate = dateFormat.parse(contractUpdateDate);	
			LogUtils.debugByMsg("contractUpdateDate：" + mapDate);
			LogUtils.debugByMsg("contractInfoUpdateDate：" + contractInfo.getUpdateDate());
			super.checkLockException(mapDate, contractInfo.getUpdateDate());
		}	
		catch(ParseException ex){
			LogUtils.infoByMsg("deleteParkingContractInfo, 駐車場契約情報-更新日時変換NG :" + contractUpdateDate);
			return 0;
		}	
		
		//削除実行
		deleteCount = skf3010MShatakuParkingContractRepository.deleteByPrimaryKey(key);
		
		return deleteCount;
	}
	
	/**
	 * 駐車場契約情報を更新する.
	 * 
	 * @param setValue 更新値を含むSkf3010MShatakuParkingContractインスタンス
	 * @param bihinInfoList 備品情報リスト
	 * @return 更新件数
	 */
	private int updateParkingContract(Skf3010MShatakuParkingContract setValue) {

		int updateCount = 0;
		
		//排他チェック用データ取得
		Skf3010MShatakuParkingContractKey key = new Skf3010MShatakuParkingContractKey();
		//対象の主キー取得		
		key.setContractPropertyId(setValue.getContractPropertyId());
		key.setShatakuKanriNo(setValue.getShatakuKanriNo());
		key.setParkingKanriNo(setValue.getParkingKanriNo());
		
		Skf3010MShatakuParkingContract contractInfo =  skf3010MShatakuParkingContractRepository.selectByPrimaryKey(key);
		
		if(contractInfo == null){
			//情報取得無しは排他エラー
			LogUtils.debugByMsg("駐車場契約情報取得結果NULL");
			return -1;
		}
		
		//排他チェック
		LogUtils.debugByMsg("contractUpdateDate：" + setValue.getLastUpdateDate());
		LogUtils.debugByMsg("contractInfoUpdateDate：" + contractInfo.getUpdateDate());
		super.checkLockException(setValue.getLastUpdateDate(), contractInfo.getUpdateDate());
		
		
		// 更新//skf3010MShatakuParkingContractRepositoryのカスタム
		updateCount = skf3010Sc007UpdateParkingContractExpRepository.updateByPrimaryKeySelective(setValue);
		
		LogUtils.debugByMsg("駐車場契約情報更新件数：" + updateCount);
		if(updateCount <= 0){
			//更新件数0以下の場合エラーとして終了
			return 0;
		}
			
		return updateCount;
	}
	
	/**
	 * 駐車場区画情報を更新する.
	 * 
	 * @param setValue 更新値を含むSkf3010MShatakuParkingBlockインスタンス
	 * @return 更新件数
	 */
	private int updateParkingBlock(Skf3010MShatakuParkingBlock setValue) {

		int updateCount = 0;
		// 更新
		updateCount = skf3010MShatakuParkingBlockRepository.updateByPrimaryKeySelective(setValue);
		
		LogUtils.debugByMsg("駐車場区画情報更新件数：" + updateCount);
		if(updateCount <= 0){
			//更新件数0以下の場合エラーとして終了
			return 0;
		}
			
		return updateCount;
	}
	
	
	/**
	 * 登録後再表示処理（SelectListServiceから必要部分抜粋）
	 * @param dto
	 */
	private void setRegistedDisp(Skf3010Sc007RegistDto dto){
		
		LogUtils.debugByMsg("駐車場契約情報(再表示)-区画番号：" + dto.getHdnParkingBlock());
		LogUtils.debugByMsg("駐車場契約情報(再表示)-契約番号：" + dto.getContractPropertyId());
		LogUtils.debugByMsg("駐車場契約情報(再表示)-駐車場管理番号：" + dto.getParkingKanriNo());
		
		//契約番号リスト
		List<Map<String, Object>> contractPropertyIdList = new ArrayList<Map<String, Object>>();
		List<Long> propertyIdList = new ArrayList<Long>();
		
		//契約形態リスト
		List<Map<String, Object>> parkingContractTypeList = new ArrayList<Map<String, Object>>();
		//貸与区分リスト
		List<Map<String, Object>> parkinglendKbnList = new ArrayList<Map<String, Object>>();
		
		//選択された区画番号
		String selectParkingBlock = dto.getHdnParkingBlock();
		String selectParkingKanriNo = dto.getParkingKanriNo();
		//選択された契約番号
		Long selectContractPropertyId;
		
		if( !SkfCheckUtils.isNullOrEmpty(dto.getContractPropertyId())){
			selectContractPropertyId = Long.parseLong(dto.getContractPropertyId());
		}else{
			selectContractPropertyId = CodeConstant.LONG_ZERO;//0設定
		}
				
		for(Map<String, Object> map : dto.getHdnListData()){
			//区画番号の一致確認
			if(selectParkingKanriNo.compareTo(map.get("parkingKanriNo").toString()) == 0 ){
				
				Long mapContractPropertyId = Long.parseLong(map.get("contractPropertyId").toString());
				//契約番号リスト
				if(mapContractPropertyId > 0){		
					//0以上でリスト(仮)に追加
					propertyIdList.add(mapContractPropertyId);
				}
				
				//契約番号の一致確認
				if(selectContractPropertyId == mapContractPropertyId){
					LogUtils.debugByMsg("駐車場契約情報-一致：" + selectParkingBlock +  ",契約番号:"+ selectContractPropertyId);
					//チェック用契約番号
					dto.setHdnBackupContractPropertyId(selectContractPropertyId.toString());
					//区画番号
					dto.setParkingBlock(selectParkingBlock);
					//追加ボタン活性
					dto.setAddButtonDisabled(FALSE);
					
					
					//契約情報有
					// 「契約番号」「契約形態」リスト、削除、登録、キャンセルボタンを活性にする
					dto.setContractListDisabled(FALSE);
					dto.setContractListDisabled(FALSE);
					dto.setDeleteButtonDisabled(FALSE);
					dto.setRegistButtonDisabled(FALSE);
					dto.setCancelButtonDisabled(FALSE);
					
					String ownerName = CodeConstant.DOUBLE_QUOTATION;
					String ownerNo = CodeConstant.DOUBLE_QUOTATION;
					String parkingZipCd = CodeConstant.DOUBLE_QUOTATION;
					String parkingAddress = CodeConstant.DOUBLE_QUOTATION;
					String parkingName = CodeConstant.DOUBLE_QUOTATION;
					String assetRegisterNo = CodeConstant.DOUBLE_QUOTATION;
					String contractStartDate = CodeConstant.DOUBLE_QUOTATION;
					String contractEndDate = CodeConstant.DOUBLE_QUOTATION;
					String landRent = CodeConstant.DOUBLE_QUOTATION;
					String biko = CodeConstant.DOUBLE_QUOTATION;
					//契約形態
					String contractType = map.get("parkingContractType").toString();
					if(Skf3010Sc007CommonSharedService.CONTRACT_TYPE_1.equals(contractType)){
						//社宅と一括契約の場合
						//リスト設定
						skf3010Sc007SharedService.getDoropDownList(Skf3010Sc007CommonSharedService.CONTRACT_TYPE_1, parkingContractTypeList, "", parkinglendKbnList);
						//駐車場契約情報のすべての入力項目を非活性、空白とする。
						//駐車場契約情報の「支援」「住所検索」ボタンを非活性にする。
						dto.setContractInfoDisabled(TRUE);
						dto.setHdnBackupParkingContractType(Skf3010Sc007CommonSharedService.CONTRACT_TYPE_1);
						dto.setHdnBackupParkinglendKbn("");
					}else{
						//社宅と別契約の場合
						//リスト設定
						String lendKbn = map.get("parkinglendKbn").toString();
						skf3010Sc007SharedService.getDoropDownList(Skf3010Sc007CommonSharedService.CONTRACT_TYPE_2, parkingContractTypeList, lendKbn, parkinglendKbnList);
						//駐車場契約情報のすべての入力項目を活性とする。
						//駐車場契約情報の「支援」「住所検索」ボタンを活性にする。
						dto.setContractInfoDisabled(FALSE);
						dto.setHdnBackupParkingContractType(Skf3010Sc007CommonSharedService.CONTRACT_TYPE_2);
						dto.setHdnBackupParkinglendKbn(lendKbn);
						
						//入力項目設定(取得値を入れる)
						ownerName = skf3010Sc007SharedService.createObjToString(map.get("ownerName"));
						ownerNo = skf3010Sc007SharedService.createObjToString(map.get("ownerNo"));
						parkingZipCd = skf3010Sc007SharedService.createObjToString(map.get("parkingZipCd"));
						parkingAddress = skf3010Sc007SharedService.createObjToString(map.get("parkingAddress"));
						parkingName = skf3010Sc007SharedService.createObjToString(map.get("parkingName"));
						assetRegisterNo = skf3010Sc007SharedService.createObjToString(map.get("assetRegisterNo"));
						contractStartDate = skf3010Sc007SharedService.createObjToString(map.get("contractStartDate"));
						contractEndDate = skf3010Sc007SharedService.createObjToString(map.get("contractEndDate"));
						landRent = skf3010Sc007SharedService.createObjToString(map.get("landRent"));
						biko = skf3010Sc007SharedService.createObjToString(map.get("biko"));
					}
					
					dto.setOwnerName(ownerName);
					dto.setOwnerNo(ownerNo);
					dto.setParkingZipCd(parkingZipCd);
					dto.setParkingAddress(parkingAddress);
					dto.setParkingName(parkingName);
					dto.setAssetRegisterNo(assetRegisterNo);
					dto.setContractStartDate(contractStartDate);
					dto.setContractEndDate(contractEndDate);
					dto.setLandRent(landRent);
					dto.setBiko(biko);
					
					//入力チェック用
					dto.setHdnBackupOwnerName(ownerName);
					dto.setHdnBackupOwnerNo(ownerNo);
					dto.setHdnBackupParkingZipCd(parkingZipCd);
					dto.setHdnBackupParkingAddress(parkingAddress);
					dto.setHdnBackupParkingName(parkingName);
					dto.setHdnBackupAssetRegisterNo(assetRegisterNo);
					dto.setHdnBackupContractStartDate(contractStartDate);
					dto.setHdnBackupContractEndDate(contractEndDate);
					dto.setHdnBackupLandRent(landRent);
					dto.setHdnBackupBiko(biko);
					
				}
			}
			
			
		}
		
		//契約番号リスト(仮)のサイズチェック
		if(propertyIdList.size() > 0){
			//1以上で、契約番号リスト生成
			Collections.sort(propertyIdList);//番号順にソート
			Map<String, Object> forListMap = new HashMap<String, Object>();
			for (Long id : propertyIdList) {
				String contractStartDate="";
				// 表示・値を設定
				forListMap = new HashMap<String, Object>();
				forListMap.put("value", id);
				
				//契約開始日の取得
				for(Map<String, Object> map : dto.getHdnListData()){
					//区画番号の一致かつ削除フラグが1以外か確認
					if(selectParkingKanriNo.compareTo(map.get("parkingKanriNo").toString()) == 0 
						&& (!"1".equals(map.get("deleteFlag").toString()))){
						
						Long mapContractPropertyId = Long.parseLong(map.get("contractPropertyId").toString());
						if(mapContractPropertyId == id){
							//契約開始日を取得
							contractStartDate = map.get("contractStartDate").toString();
						}
					}
				}
				//表示内容設定
				forListMap.put("label", id.toString() + "：" + contractStartDate);
				
				if (Objects.equals(id, selectContractPropertyId)) {
					//選択契約番号を選択中にする
					forListMap.put("selected", true);
				}
				contractPropertyIdList.add(forListMap);
			}
		}
		
		dto.setHdnBackupMaxContractPropertyId(String.valueOf(propertyIdList.size()));

		//登録済みの契約番号最大値を設定
		dto.setHdnRegistContractPropertyId(String.valueOf(propertyIdList.size()));

		
		//各リスト
		dto.setParkingContractTypeList(parkingContractTypeList);
		dto.setParkinglendKbnList(parkinglendKbnList);
		dto.setContractPropertyIdList(contractPropertyIdList);

		//エラー変数初期化
		dto.setOwnerNameError(CodeConstant.DOUBLE_QUOTATION);
		dto.setParkingZipCdError(CodeConstant.DOUBLE_QUOTATION);
		dto.setParkingAddressError(CodeConstant.DOUBLE_QUOTATION);
		dto.setParkingNameError(CodeConstant.DOUBLE_QUOTATION);
		dto.setAssetRegisterNoError(CodeConstant.DOUBLE_QUOTATION);
		dto.setContractStartDateError(CodeConstant.DOUBLE_QUOTATION);
		dto.setContractEndDateError(CodeConstant.DOUBLE_QUOTATION);
		dto.setLandRentError(CodeConstant.DOUBLE_QUOTATION);
		dto.setParkingContractTypeError(CodeConstant.DOUBLE_QUOTATION);
		dto.setContractPropertyIdError(CodeConstant.DOUBLE_QUOTATION);
	}


}
