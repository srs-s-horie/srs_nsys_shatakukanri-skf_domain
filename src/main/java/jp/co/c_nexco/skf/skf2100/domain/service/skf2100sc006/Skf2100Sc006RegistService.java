/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc006;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfRouterInfoUtils.SkfRouterInfoUtilsGetEquipmentPaymentExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2100TMobileRouterLedger;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2100TMobileRouterRentalRireki;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2100TMobileRouterRentalRirekiMeisai;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3050MGeneralEquipmentItem;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2100Sc006.Skf2100Sc006UpdateTMobileRouterLedgerExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2100TMobileRouterLedgerRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2100TMobileRouterRentalRirekiMeisaiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2100TMobileRouterRentalRirekiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3050MGeneralEquipmentItemRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.LoginUserInfoUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.SkfRouterInfoUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100Sc006common.Skf2100Sc006CommonDto;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc006.Skf2100Sc006RegistDto;
import jp.co.intra_mart.mirage.integration.guice.Transactional;

/**
 * Skf2100Sc006RegistService モバイルルーター貸出管理簿登録画面の登録処理クラス。　 
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf2100Sc006RegistService extends SkfServiceAbstract<Skf2100Sc006RegistDto> {
	
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfRouterInfoUtils skfRouterInfoUtils;
	@Autowired
	private Skf2100Sc006SharedService skf2100Sc006SharedService;
	@Autowired
	private Skf2100TMobileRouterLedgerRepository skf2100TMobileRouterLedgerRepository;
	@Autowired
	private Skf2100TMobileRouterRentalRirekiRepository skf2100TMobileRouterRentalRirekiRepository;
	@Autowired
	private Skf2100TMobileRouterRentalRirekiMeisaiRepository skf2100TMobileRouterRentalRirekiMeisaiRepository;
	@Autowired
	private Skf3050MGeneralEquipmentItemRepository skf3050MGeneralEquipmentItemRepository;
	@Autowired
	private Skf2100Sc006UpdateTMobileRouterLedgerExpRepository skf2100Sc006UpdateTMobileRouterLedgerExpRepository;

	/**
	 * サービス処理を行う。　
	 * 
	 * @param registDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public Skf2100Sc006RegistDto index(Skf2100Sc006RegistDto registDto) throws Exception {

		registDto.setPageTitleKey(MessageIdConstant.SKF2100_SC006_TITLE);
		// デバッグログ
		LogUtils.debugByMsg("登録");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("登録", CodeConstant.C001, FunctionIdConstant.SKF2100_SC006);

		// ドロップダウンリスト
		List<Map<String, Object>> originalCompanyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> payCompanyList = new ArrayList<Map<String, Object>>();
		
		//可変ラベル値
		skf2100Sc006SharedService.setVariableLabel(skf2100Sc006SharedService.jsonArrayToArrayList(registDto.getJsonLabelList()), registDto);

		// ドロップダウンリストを設定
		skf2100Sc006SharedService.setDdlControlValues(
				registDto.getOriginalCompanySelect(), originalCompanyList,
				registDto.getPayCompanySelect(), payCompanyList);
		// ドロップダウンリスト設定
		registDto.setOriginalCompanyList(originalCompanyList);
		registDto.setPayCompanyList(payCompanyList);
		
		// 「貸出管理簿ID」が”0”の場合
		if (Skf2100Sc006CommonDto.NO_KANRI_ID.equals(registDto.getHdnRouterKanriId()) ){
			//共通チェック
			if (skf2100Sc006SharedService.validateInput(registDto) ){
				
				String routerKanriId = registClickInsert(registDto);
				//登録した内容を画面に反映する
				//取得した管理簿IDをhdnに設定
				registDto.setHdnRouterKanriId(routerKanriId);
				// モバイルルーター貸出管理簿情報を取得
				Long routerKanriIdL = Long.parseLong(routerKanriId);
				
				Skf2100TMobileRouterLedger routerLdata = skf2100TMobileRouterLedgerRepository.selectByPrimaryKey(routerKanriIdL);
				if(routerLdata != null){
					// 更新日時
					registDto.addLastUpdateDate(Skf2100Sc006CommonDto.ROUTER_KEY_LAST_UPDATE_DATE, routerLdata.getUpdateDate());
				}
				//画面項目
				skf2100Sc006SharedService.setControlStatus(registDto);
		
			}
		}else{
			//「社宅管理台帳ID」が値を持つ場合
			//共通チェック
			if( skf2100Sc006SharedService.validateInput(registDto) ){

				registClickUpdate(registDto);
				
				// モバイルルーター貸出管理簿情報を取得
				Long routerKanriIdL = Long.parseLong(registDto.getHdnRouterKanriId());
				Skf2100TMobileRouterLedger routerLdata = skf2100TMobileRouterLedgerRepository.selectByPrimaryKey(routerKanriIdL);
				if(routerLdata != null){
					// 更新日時更新
					registDto.addLastUpdateDate(Skf2100Sc006CommonDto.ROUTER_KEY_LAST_UPDATE_DATE, routerLdata.getUpdateDate());
				}
				//画面項目
				skf2100Sc006SharedService.setControlStatus(registDto);
			}
		}
		
		//画面項目制御
		skf2100Sc006SharedService.setControlStatus(registDto);

		return registDto;
	}
	

	/**
	 * 登録処理(トランザクション部分)
	 * 
	 * @param registDto
	 *            DTO
	 * @return 新規モバイルルーター貸出管理簿ID
	 * @throws Exception
	 *             例外
	 */
	@Transactional
	private String registClickInsert(Skf2100Sc006RegistDto registDto){
		
		String routerKanriId = CodeConstant.DOUBLE_QUOTATION;
		
		// モバイルルーター貸出管理簿IDの取得
		Long newKanriId = skfRouterInfoUtils.getMaxRouterKanriId();
		LogUtils.infoByMsg("モバイルルーター管理簿登録:新規貸出管理簿ID" + newKanriId);
		
		// モバイルルーター貸出管理簿登録
		Skf2100TMobileRouterLedger routerLRecord = new Skf2100TMobileRouterLedger();
		routerLRecord.setMobileRouterKanriId(newKanriId);
		routerLRecord.setMobileRouterNo(Long.parseLong(registDto.getRouterNo()));
		routerLRecord.setShainNo(registDto.getHdnShainNo());
		routerLRecord.setOriginalCompanyCd(registDto.getOriginalCompanySelect());
		routerLRecord.setPayCompanyCd(registDto.getPayCompanySelect());
		routerLRecord.setHannyuTel(registDto.getHannyuTel());
		routerLRecord.setHannyuMailaddress(registDto.getHannyuMailaddress());
		routerLRecord.setUseStartHopeDay(skf2100Sc006SharedService.getRegistDateText(registDto.getUseStartHopeDay()));
		routerLRecord.setShippingDate(skf2100Sc006SharedService.getRegistDateText(registDto.getShippingDate()));
		routerLRecord.setReceivedDate(skf2100Sc006SharedService.getRegistDateText(registDto.getReceivedDate()));
		routerLRecord.setUseStopDay(skf2100Sc006SharedService.getRegistDateText(registDto.getUseStopDay()));
		routerLRecord.setHansyutuTel(skf2100Sc006SharedService.getToRegistString(registDto.getHansyutuTel()));
		routerLRecord.setReturnDay(skf2100Sc006SharedService.getRegistDateText(registDto.getReturnDay()));
		routerLRecord.setBiko(registDto.getBiko());
		
		LogUtils.infoByMsg("モバイルルーター管理簿登録:貸出管理簿ID:" + newKanriId +",社員番号:" + registDto.getHdnShainNo() + ",通しNo" + registDto.getRouterNo());
		int inCount = skf2100TMobileRouterLedgerRepository.insertSelective(routerLRecord);
		if(inCount <= 0){
			// 登録失敗
			LogUtils.infoByMsg("モバイルルーター管理簿登録異常:更新件数" + inCount);
			// エラーメッセージを設定
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1073);
			throwBusinessExceptionIfErrors(registDto.getResultMessages());
		}
		
		// 月次処理月管理テーブルを読み込み当月処理月取得
		String yearMonth = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu();
		
		// 月別モバイルルーター使用料明細、月別モバイルルーター使用料履歴登録
		// 汎用備品項目設定取得
//		Skf3050MGeneralEquipmentItem equipment = new Skf3050MGeneralEquipmentItem();
//		equipment = skf3050MGeneralEquipmentItemRepository.selectByPrimaryKey(CodeConstant.GECD_MOBILEROUTER);
		SkfRouterInfoUtilsGetEquipmentPaymentExp equipment = new SkfRouterInfoUtilsGetEquipmentPaymentExp();
		equipment = skfRouterInfoUtils.getEquipmentPayment(CodeConstant.GECD_MOBILEROUTER, yearMonth);
		if(equipment == null){
			// 取得失敗
			// エラーメッセージを設定
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1118,"汎用備品項目設定テーブル");
			throwBusinessExceptionIfErrors(registDto.getResultMessages());
		}
		
		// 月別モバイルルーター使用料明細登録
		Skf2100TMobileRouterRentalRirekiMeisai meisaiRecord = new Skf2100TMobileRouterRentalRirekiMeisai();
		meisaiRecord.setMobileRouterKanriId(newKanriId);
		meisaiRecord.setYearMonth(yearMonth);
		meisaiRecord.setGeneralEquipmentCd(CodeConstant.GECD_MOBILEROUTER);
		meisaiRecord.setMobileRouterGenbutsuGaku(equipment.getEquipmentPayment());
		meisaiRecord.setMobileRouterApplKbn(CodeConstant.BIHIN_SHINSEI_KBN_NASHI);//申請無
		meisaiRecord.setMobileRouterReturnKbn(CodeConstant.BIHIN_HENKYAKU_SURU);//要返却
		
		LogUtils.infoByMsg("月別モバイルルーター使用料明細登録:貸出管理簿ID:" + newKanriId +",処理年月:" + yearMonth);
		inCount = skf2100TMobileRouterRentalRirekiMeisaiRepository.insertSelective(meisaiRecord);
		if(inCount <= 0){
			// 登録失敗
			LogUtils.infoByMsg("月別モバイルルーター使用料明細登録異常:更新件数" + inCount);
			// エラーメッセージを設定
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1073);
			throwBusinessExceptionIfErrors(registDto.getResultMessages());
		}
		
		// 月別モバイルルーター使用料登録
		Skf2100TMobileRouterRentalRireki rirekiRecord = new Skf2100TMobileRouterRentalRireki();
		rirekiRecord.setMobileRouterKanriId(newKanriId);
		rirekiRecord.setYearMonth(yearMonth);
		rirekiRecord.setMobileRouterGenbutuGoukei(equipment.getEquipmentPayment());
		LogUtils.infoByMsg("月別モバイルルーター使用料履歴登録:貸出管理簿ID:" + newKanriId +",処理年月:" + yearMonth);
		inCount = skf2100TMobileRouterRentalRirekiRepository.insertSelective(rirekiRecord);
		if(inCount <= 0){
			// 登録失敗
			LogUtils.infoByMsg("月別モバイルルーター使用料履歴登録異常:更新件数" + inCount);
			// エラーメッセージを設定
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1073);
			throwBusinessExceptionIfErrors(registDto.getResultMessages());
		}
		
		// モバイルルーターマスタ更新
		String setLendingJudgment = CodeConstant.ROUTER_LENDING_JUDGMENT_USE;
		if(!SkfCheckUtils.isNullOrEmpty(registDto.getReturnDay())){
			// 窓口返却日設定有
			setLendingJudgment = CodeConstant.ROUTER_LENDING_JUDGMENT_TAIYO;
		}
		if(CodeConstant.ROUTER_FAULT_FLAG_FAULT.equals(registDto.getHdnFaultFlag())){
			// 故障中
			setLendingJudgment = CodeConstant.ROUTER_LENDING_JUDGMENT_TAIYOFUKA;
		}
		boolean result = skfRouterInfoUtils.updateMobileRouterLJudment(Long.parseLong(registDto.getRouterNo()), 
				setLendingJudgment, null);
		if(!result){
			// 更新失敗
			// エラーメッセージ（メッセージID：E_SKF_1075）を設定
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1075);
			throwBusinessExceptionIfErrors(registDto.getResultMessages());
		}
		
		
		// 正常時
		routerKanriId = newKanriId.toString();
		ServiceHelper.addResultMessage(registDto, MessageIdConstant.I_SKF_1012);
		
		return routerKanriId;
	}
	
	/**
	 * 更新処理(トランザクション部分)
	 * 
	 * @param registDto
	 *            DTO
	 * @throws Exception
	 *             例外
	 */
	@Transactional
	private void registClickUpdate(Skf2100Sc006RegistDto registDto){
		
		Long routerKanriId = Long.parseLong(registDto.getHdnRouterKanriId());
		// モバイルルーター貸出管理簿更新
		Skf2100TMobileRouterLedger routerLRecord = new Skf2100TMobileRouterLedger();
		routerLRecord.setMobileRouterKanriId(routerKanriId);
		routerLRecord.setMobileRouterNo(Long.parseLong(registDto.getRouterNo()));
		routerLRecord.setShainNo(registDto.getHdnShainNo());
		routerLRecord.setOriginalCompanyCd(registDto.getOriginalCompanySelect());
		routerLRecord.setPayCompanyCd(registDto.getPayCompanySelect());
		routerLRecord.setHannyuTel(registDto.getHannyuTel());
		routerLRecord.setHannyuMailaddress(registDto.getHannyuMailaddress());
		routerLRecord.setUseStartHopeDay(skf2100Sc006SharedService.getRegistDateText(registDto.getUseStartHopeDay()));
		routerLRecord.setShippingDate(skf2100Sc006SharedService.getRegistDateText(registDto.getShippingDate()));
		routerLRecord.setReceivedDate(skf2100Sc006SharedService.getRegistDateText(registDto.getReceivedDate()));
		routerLRecord.setUseStopDay(skf2100Sc006SharedService.getRegistDateText(registDto.getUseStopDay()));
		routerLRecord.setHansyutuTel(skf2100Sc006SharedService.getToRegistString(registDto.getHansyutuTel()));
		routerLRecord.setReturnDay(skf2100Sc006SharedService.getRegistDateText(registDto.getReturnDay()));
		routerLRecord.setBiko(skf2100Sc006SharedService.getToRegistString(registDto.getBiko()));
		routerLRecord.setUpdateProgramId(FunctionIdConstant.SKF2100_SC006);
		routerLRecord.setUpdateUserId(LoginUserInfoUtils.getUserCd());
		routerLRecord.setLastUpdateDate(registDto.getLastUpdateDate(Skf2100Sc006CommonDto.ROUTER_KEY_LAST_UPDATE_DATE));
		
		LogUtils.infoByMsg("モバイルルーター管理簿更新:貸出管理簿ID:" + routerKanriId +",社員番号:" + registDto.getHdnShainNo() + ",通しNo" + registDto.getRouterNo());
		int inCount = skf2100Sc006UpdateTMobileRouterLedgerExpRepository.updateByPrimaryKeySelective(routerLRecord);
		if(inCount <= 0){
			// 更新失敗
			LogUtils.infoByMsg("モバイルルーター管理簿更新異常:更新件数" + inCount);
			// エラーメッセージを設定
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1075);
			throwBusinessExceptionIfErrors(registDto.getResultMessages());
		}
		
		// 遷移元処理年月取得
		String yearMonth = registDto.getHdnYearMonth();
		
		// 月別モバイルルーター使用料明細、月別モバイルルーター使用料履歴登録
		// 汎用備品項目設定取得
//		Skf3050MGeneralEquipmentItem equipment = new Skf3050MGeneralEquipmentItem();
//		equipment = skf3050MGeneralEquipmentItemRepository.selectByPrimaryKey(CodeConstant.GECD_MOBILEROUTER);
		SkfRouterInfoUtilsGetEquipmentPaymentExp equipment = new SkfRouterInfoUtilsGetEquipmentPaymentExp();
		equipment = skfRouterInfoUtils.getEquipmentPayment(CodeConstant.GECD_MOBILEROUTER, yearMonth);
		if(equipment == null){
			// 取得失敗
			// エラーメッセージを設定
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1118,"汎用備品項目設定テーブル");
			throwBusinessExceptionIfErrors(registDto.getResultMessages());
		}
		
		// 月別モバイルルーター使用料明細更新
		Skf2100TMobileRouterRentalRirekiMeisai meisaiRecord = new Skf2100TMobileRouterRentalRirekiMeisai();
		meisaiRecord.setMobileRouterKanriId(routerKanriId);
		meisaiRecord.setYearMonth(yearMonth);
		meisaiRecord.setGeneralEquipmentCd(CodeConstant.GECD_MOBILEROUTER);
		meisaiRecord.setMobileRouterGenbutsuGaku(equipment.getEquipmentPayment());
		meisaiRecord.setMobileRouterApplKbn(CodeConstant.BIHIN_SHINSEI_KBN_NASHI);//申請無
		meisaiRecord.setMobileRouterReturnKbn(CodeConstant.BIHIN_HENKYAKU_SURU);//要返却
		LogUtils.infoByMsg("月別モバイルルーター使用料明細更新:貸出管理簿ID:" + routerKanriId +",処理年月:" + yearMonth);
		inCount = skf2100TMobileRouterRentalRirekiMeisaiRepository.updateByPrimaryKeySelective(meisaiRecord);
		if(inCount <= 0){
			// 更新失敗
			LogUtils.infoByMsg("月別モバイルルーター使用料明細更新異常:更新件数" + inCount);
			// エラーメッセージを設定
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1075);
			throwBusinessExceptionIfErrors(registDto.getResultMessages());
		}
		
		// 月別モバイルルーター使用料更新
		Skf2100TMobileRouterRentalRireki rirekiRecord = new Skf2100TMobileRouterRentalRireki();
		rirekiRecord.setMobileRouterKanriId(routerKanriId);
		rirekiRecord.setYearMonth(yearMonth);
		rirekiRecord.setMobileRouterGenbutuGoukei(equipment.getEquipmentPayment());
		LogUtils.infoByMsg("月別モバイルルーター使用料履歴更新:貸出管理簿ID:" + routerKanriId +",処理年月:" + yearMonth);
		inCount = skf2100TMobileRouterRentalRirekiRepository.updateByPrimaryKeySelective(rirekiRecord);
		if(inCount <= 0){
			// 更新失敗
			LogUtils.infoByMsg("月別モバイルルーター使用料履歴更新異常:更新件数" + inCount);
			// エラーメッセージを設定
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1075);
			throwBusinessExceptionIfErrors(registDto.getResultMessages());
		}
		
		// モバイルルーターマスタ更新
		String setLendingJudgment = CodeConstant.ROUTER_LENDING_JUDGMENT_USE;
		if(!SkfCheckUtils.isNullOrEmpty(registDto.getReturnDay())){
			// 窓口返却日設定有
			setLendingJudgment = CodeConstant.ROUTER_LENDING_JUDGMENT_TAIYO;
		}
		if(CodeConstant.ROUTER_FAULT_FLAG_FAULT.equals(registDto.getHdnFaultFlag())){
			// 故障中
			setLendingJudgment = CodeConstant.ROUTER_LENDING_JUDGMENT_TAIYOFUKA;
		}
		boolean result = skfRouterInfoUtils.updateMobileRouterLJudment(Long.parseLong(registDto.getRouterNo()), 
				setLendingJudgment, null);
		if(!result){
			// 更新失敗
			// エラーメッセージ（メッセージID：E_SKF_1075）を設定
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1075);
			throwBusinessExceptionIfErrors(registDto.getResultMessages());
		}
			
		// 正常
		// 更新完了メッセージ
		ServiceHelper.addResultMessage(registDto, MessageIdConstant.I_SKF_1011);
		
	}
	

}
