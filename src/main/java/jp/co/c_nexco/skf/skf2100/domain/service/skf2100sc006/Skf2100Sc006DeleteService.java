/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc006;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2100Sc006.Skf2100Sc006DeleteRouterLendingYoteiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2100TMobileRouterRentalRireki;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2100TMobileRouterRentalRirekiKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2100TMobileRouterRentalRirekiMeisaiKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2100Sc006.Skf2100Sc006DeleteRouterLendingYoteiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2100TMobileRouterLedgerRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2100TMobileRouterRentalRirekiMeisaiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2100TMobileRouterRentalRirekiRepository;
import jp.co.c_nexco.nfw.common.bean.ApplicationScopeBean;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.SkfRouterInfoUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc006.Skf2100Sc006DeleteDto;
import jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc005.Skf2100Sc005CommonSharedService;
import jp.co.intra_mart.mirage.integration.guice.Transactional;

/**
 * Skf2100Sc006DeleteService モバイルルーター貸出管理簿登録画面の削除処理クラス。　 
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf2100Sc006DeleteService extends SkfServiceAbstract<Skf2100Sc006DeleteDto> {
	
	@Autowired
	private ApplicationScopeBean bean;

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfRouterInfoUtils skfRouterInfoUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private Skf2100Sc006SharedService skf2100Sc006SharedService;
	@Autowired
	private Skf2100TMobileRouterLedgerRepository skf2100TMobileRouterLedgerRepository;
	@Autowired
	private Skf2100TMobileRouterRentalRirekiRepository skf2100TMobileRouterRentalRirekiRepository;
	@Autowired
	private Skf2100TMobileRouterRentalRirekiMeisaiRepository skf2100TMobileRouterRentalRirekiMeisaiRepository;
	@Autowired
	private Skf2100Sc006DeleteRouterLendingYoteiDataExpRepository skf2100Sc006DeleteRouterLendingYoteiDataExpRepository;

	/**
	 * サービス処理を行う。　
	 * 
	 * @param delDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public Skf2100Sc006DeleteDto index(Skf2100Sc006DeleteDto delDto) throws Exception {

		// デバッグログ
		LogUtils.debugByMsg("削除");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("削除", CodeConstant.C001, FunctionIdConstant.SKF2100_SC006);

		// ドロップダウンリスト
		List<Map<String, Object>> originalCompanyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> payCompanyList = new ArrayList<Map<String, Object>>();
		
		//可変ラベル値
		skf2100Sc006SharedService.setVariableLabel(skf2100Sc006SharedService.jsonArrayToArrayList(delDto.getJsonLabelList()), delDto);
		// ドロップダウンリストを設定
		skf2100Sc006SharedService.setDdlControlValues(
				delDto.getOriginalCompanySelect(), originalCompanyList,
				delDto.getPayCompanySelect(), payCompanyList);		
		// ドロップダウンリスト設定
		delDto.setOriginalCompanyList(originalCompanyList);
		delDto.setPayCompanyList(payCompanyList);

		Long mobileRouterKanriId = Long.parseLong(delDto.getHdnRouterKanriId());
		String yearMonth = delDto.getHdnYearMonth();
		
		//処理年月前月
		String beforeYearMonth= skfDateFormatUtils.addYearMonth(yearMonth, -1);
		// 前月の利用実績取得
		Skf2100TMobileRouterRentalRirekiKey rirekiKey = new Skf2100TMobileRouterRentalRirekiKey();
		rirekiKey.setMobileRouterKanriId(mobileRouterKanriId);
		rirekiKey.setYearMonth(beforeYearMonth);
		Skf2100TMobileRouterRentalRireki rirekiData = skf2100TMobileRouterRentalRirekiRepository.selectByPrimaryKey(rirekiKey);
		if(rirekiData != null){
			// 前月履歴有り
			// エラーメッセージを設定
			ServiceHelper.addErrorResultMessage(delDto, null, MessageIdConstant.E_SKF_3060,"モバイルルーター貸出管理簿");
//			throwBusinessExceptionIfErrors(delDto.getResultMessages());
			return delDto;
		}

		// 削除処理
		int returnCount = deleteData(delDto);
		if(returnCount != 0){
			ServiceHelper.addResultMessage(delDto, MessageIdConstant.I_SKF_1013);
			
			// 削除後動作エラー回避のためDTOクリア
			delDto.setHdnRouterKanriId(CodeConstant.DOUBLE_QUOTATION);
			
			// モバイルルーター貸出管理簿画面へ遷移
			TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF2100_SC005);
			delDto.setTransferPageInfo(nextPage);
			
			//セッション情報設定
			Map<String, Object> searchInfoSessionMap = new HashMap<String, Object>();

			searchInfoSessionMap.put(Skf2100Sc005CommonSharedService.SEARCH_INFO_ROUTER_NO_KEY, delDto.getSearchInfoRouterNo());
			searchInfoSessionMap.put(Skf2100Sc005CommonSharedService.SEARCH_INFO_TEL_KEY, delDto.getSearchInfoTel());
			searchInfoSessionMap.put(Skf2100Sc005CommonSharedService.SEARCH_INFO_SHAIN_NO_KEY, delDto.getSearchInfoShainNo());
			searchInfoSessionMap.put(Skf2100Sc005CommonSharedService.SEARCH_INFO_SHAIN_NAME_KEY, delDto.getSearchInfoShainName());
			searchInfoSessionMap.put(Skf2100Sc005CommonSharedService.SEARCH_INFO_CONTRACT_KBN_KEY, delDto.getSearchInfoContractKbn());
			searchInfoSessionMap.put(Skf2100Sc005CommonSharedService.SEARCH_INFO_NENGETSU_KEY, delDto.getSearchInfoYearMonth());
			searchInfoSessionMap.put(Skf2100Sc005CommonSharedService.SEARCH_INFO_AKI_ROUTER_KEY, delDto.getSearchInfoAkiRouter());
			searchInfoSessionMap.put(Skf2100Sc005CommonSharedService.SEARCH_INFO_CONTRACT_END_DATE_FROM_KEY, delDto.getSearchInfoContractEndDateFrom());
			searchInfoSessionMap.put(Skf2100Sc005CommonSharedService.SEARCH_INFO_CONTRACT_END_DATE_TO_KEY, delDto.getSearchInfoContractEndDateTo());
			bean.put(SessionCacheKeyConstant.MOBILEROUTER_LEDGER_SEARCH, searchInfoSessionMap);
		}
		
		return delDto;
	}
	

	/**
	 * 登録処理(トランザクション部分)
	 * 
	 * @param delDto
	 *            DTO
	 * @return 削除件数
	 * @throws Exception
	 *             例外
	 */
	@Transactional
	public int deleteData(Skf2100Sc006DeleteDto delDto){
		
		int returnCount = 0;
		Long mobileRouterKanriId = Long.parseLong(delDto.getHdnRouterKanriId());
		String yearMonth = delDto.getHdnYearMonth();
		
		// モバイルルーター貸出管理簿削除		
		int delCount = skf2100TMobileRouterLedgerRepository.deleteByPrimaryKey(mobileRouterKanriId);
		if(delCount <= 0){
			// 削除失敗
			LogUtils.debugByMsg("モバイルルーター貸出管理簿：削除失敗");
			ServiceHelper.addErrorResultMessage(delDto, null, MessageIdConstant.E_SKF_1076);
			throwBusinessExceptionIfErrors(delDto.getResultMessages());
			return 0;
		}else{
			returnCount += delCount;
		}
		
		// 月別モバイルルーター使用料明細、月別モバイルルーター使用料履歴削除
		// 月別モバイルルーター使用料明細
		Skf2100TMobileRouterRentalRirekiMeisaiKey meisaiKey = new Skf2100TMobileRouterRentalRirekiMeisaiKey();
		meisaiKey.setMobileRouterKanriId(mobileRouterKanriId);
		meisaiKey.setYearMonth(yearMonth);
		meisaiKey.setGeneralEquipmentCd(CodeConstant.GECD_MOBILEROUTER);

		delCount = skf2100TMobileRouterRentalRirekiMeisaiRepository.deleteByPrimaryKey(meisaiKey);
		if(delCount <= 0){
			// 削除失敗
			LogUtils.debugByMsg("月別モバイルルーター使用料明細：削除失敗");
			ServiceHelper.addErrorResultMessage(delDto, null, MessageIdConstant.E_SKF_1076);
			throwBusinessExceptionIfErrors(delDto.getResultMessages());
			return 0;
		}else{
			returnCount += delCount;
		}
		
		// 月別モバイルルーター使用料
		Skf2100TMobileRouterRentalRirekiKey rirekiKey = new Skf2100TMobileRouterRentalRirekiKey();
		rirekiKey.setMobileRouterKanriId(mobileRouterKanriId);
		rirekiKey.setYearMonth(yearMonth);
		delCount = skf2100TMobileRouterRentalRirekiRepository.deleteByPrimaryKey(rirekiKey);
		if(delCount <= 0){
			// 削除失敗
			LogUtils.debugByMsg("月別モバイルルーター使用料履歴：削除失敗");
			ServiceHelper.addErrorResultMessage(delDto, null, MessageIdConstant.E_SKF_1076);
			throwBusinessExceptionIfErrors(delDto.getResultMessages());
			return 0;
		}else{
			returnCount += delCount;
		}
		
		// モバイルルーター貸出予定データ
		Skf2100Sc006DeleteRouterLendingYoteiDataExpParameter yoteiParam = new Skf2100Sc006DeleteRouterLendingYoteiDataExpParameter();
		yoteiParam.setMobileRouterKanriId(mobileRouterKanriId);
		yoteiParam.setShainNo(delDto.getHdnShainNo());
		delCount = skf2100Sc006DeleteRouterLendingYoteiDataExpRepository.deleteRouterLendingYoteiData(yoteiParam);
		if(delCount > 0){
			// 予定データは無くても構わない
			returnCount += delCount;
		}
		
		// モバイルルーターマスタ更新
		String setLendingJudgment = CodeConstant.ROUTER_LENDING_JUDGMENT_TAIYO;
		if(CodeConstant.ROUTER_FAULT_FLAG_FAULT.equals(delDto.getHdnFaultFlag())){
			// 故障中
			setLendingJudgment = CodeConstant.ROUTER_LENDING_JUDGMENT_TAIYOFUKA;
		}
		boolean result = skfRouterInfoUtils.updateMobileRouterLJudment(Long.parseLong(delDto.getRouterNo()), 
				setLendingJudgment, null);
		if(!result){
			// 更新失敗
			// エラーメッセージ（メッセージID：E_SKF_1075）を設定
			ServiceHelper.addErrorResultMessage(delDto, null, MessageIdConstant.E_SKF_1075);
			throwBusinessExceptionIfErrors(delDto.getResultMessages());
			return 0;
		}
		
		LogUtils.debugByMsg("削除件数："+ returnCount);

		return returnCount;
	}
	
	

}
