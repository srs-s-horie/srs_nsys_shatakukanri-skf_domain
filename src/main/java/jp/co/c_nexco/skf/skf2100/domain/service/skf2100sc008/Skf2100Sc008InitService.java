/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc008;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2100MMobileRouterWithBLOBs;
import jp.co.c_nexco.nfw.common.bean.ApplicationScopeBean;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.SkfRouterInfoUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100Sc008common.Skf2100Sc008CommonDto;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc008.Skf2100Sc008InitDto;
import jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc007.Skf2100Sc007CommonSharedService;

/**
 * Skf2100Sc008InitService モバイルルーターマスタ登録画面のInitサービス処理クラス。　 
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf2100Sc008InitService extends SkfServiceAbstract<Skf2100Sc008InitDto> {
	
	@Autowired
	private ApplicationScopeBean bean;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfRouterInfoUtils skfRouterInfoUtils;
	@Autowired
	private SkfDropDownUtils skfDropDownUtils;
	@Autowired
	private Skf2100Sc008SharedService skf2100Sc008SharedService;

	
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
	public Skf2100Sc008InitDto index(Skf2100Sc008InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF2100_SC008_TITLE);
 		
		// デバッグログ
		LogUtils.debugByMsg("初期表示");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, FunctionIdConstant.SKF2100_SC008);
		skf2100Sc008SharedService.initialize(initDto);
		
		//画面初期化
		initialize(initDto);
		
		return initDto;
	}
	
	@SuppressWarnings("unchecked")
	private void initialize(Skf2100Sc008InitDto initDto) throws Exception{
		
		//セッション情報をチェック
		Map<String, Object> searchSessionData = (Map<String, Object>) bean
				.get(SessionCacheKeyConstant.MOBILEROUTER_MASTER_SEARCH);
		
		if (searchSessionData != null) {
			// 検索条件データ
			String routerNo = (String) searchSessionData.get(Skf2100Sc007CommonSharedService.SEARCH_INFO_ROUTER_NO_KEY);
			String tel = (String) searchSessionData.get(Skf2100Sc007CommonSharedService.SEARCH_INFO_TEL_KEY);
			String iccid = (String) searchSessionData.get(Skf2100Sc007CommonSharedService.SEARCH_INFO_ICCID_KEY);
			String imei = (String) searchSessionData.get(Skf2100Sc007CommonSharedService.SEARCH_INFO_IMEI_KEY);
			String contractKbn = (String) searchSessionData.get(Skf2100Sc007CommonSharedService.SEARCH_INFO_CONTRACT_KBN_KEY);
			String faultFlag = (String) searchSessionData.get(Skf2100Sc007CommonSharedService.SEARCH_INFO_FAULT_KEY);
			String contractEndDateFrom = (String) searchSessionData.get(Skf2100Sc007CommonSharedService.SEARCH_INFO_CONTRACT_END_DATE_FROM_KEY);
			String contractEndDateTo = (String) searchSessionData.get(Skf2100Sc007CommonSharedService.SEARCH_INFO_CONTRACT_END_DATE_TO_KEY);
			String arrivalDateFrom = (String) searchSessionData.get(Skf2100Sc007CommonSharedService.SEARCH_INFO_ARRIVAL_DATE_FROM_KEY);
			String arrivalDateTo = (String) searchSessionData.get(Skf2100Sc007CommonSharedService.SEARCH_INFO_ARRIVAL_DATE_TO_KEY);
	
			//DTOに設定
			initDto.setSearchInfoRouterNo(routerNo);
			initDto.setSearchInfoTel(tel);
			initDto.setSearchInfoIccid(iccid);
			initDto.setSearchInfoImei(imei);
			initDto.setSearchInfoContractKbn(contractKbn);
			initDto.setSearchInfoContractEndDateFrom(contractEndDateFrom);
			initDto.setSearchInfoContractEndDateTo(contractEndDateTo);
			initDto.setSearchInfoArrivalDateFrom(arrivalDateFrom);
			initDto.setSearchInfoArrivalDateTo(arrivalDateTo);
			initDto.setSearchInfoFaultFlag(faultFlag);
		}

		
		Map<String, Object> selListSessionData = (Map<String, Object>) bean
				.get(SessionCacheKeyConstant.MOBILEROUTER_MASTER_INFO);
		
		if (selListSessionData != null) {
			// 選択した検索リストのモバイルルーター通しNo
			//String routerNoStr = selListSessionData.get(Skf2100Sc007CommonSharedService.SEL_LIST_INFO_ROUTER_NO_KEY);
			Long selRouterNo = (Long) selListSessionData.get(Skf2100Sc007CommonSharedService.SEL_LIST_INFO_ROUTER_NO_KEY);
			
			// 画面項目に設定する
			// 通しNo
			if(selRouterNo != null && selRouterNo > 0){
				initDto.setRouterNo(selRouterNo.toString());
			}else{
				initDto.setRouterNo(CodeConstant.DOUBLE_QUOTATION);
			}
		}
		
		bean.clear();
		
		// 新規登録フラグ
		initDto.setNewDataFlag(false);
		
		//「モバイルルーター通しNo」が未設定の場合
		if(SkfCheckUtils.isNullOrEmpty(initDto.getRouterNo())){
			//新規

			// 契約区分ドロップダウンリスト
			List<Map<String, Object>> contractKbnDropDownList = skfDropDownUtils
					.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_ROUTER_CONTRACT_KBN, "", true);
			initDto.setContractKbnDropDownList(contractKbnDropDownList);
			initDto.setContractKbnSelect("");
			
			// 新規通しNo取得
			Long routerNo = skfRouterInfoUtils.getMaxRouterNo();
			if(routerNo != null){
				initDto.setRouterNo(routerNo.toString());
			}else{
				initDto.setBtnDeleteDisableFlg(true);
				initDto.setBtnRegistDisableFlg(true);
				ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1077);
				return;
			}
			// 削除ボタン非活性
			initDto.setBtnDeleteDisableFlg(true);
			// 新規登録フラグ
			initDto.setNewDataFlag(true);
			
		}else{
			// 更新
			// モバイルルーターマスタ情報を取得
			Skf2100MMobileRouterWithBLOBs routerMasterData = skfRouterInfoUtils.getMobileRouterInfo(Long.parseLong(initDto.getRouterNo()));
			if(routerMasterData == null){
				initDto.setBtnDeleteDisableFlg(true);
				initDto.setBtnRegistDisableFlg(true);
				ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1077);
				return;
			}
		
			// 通しNo
			initDto.setRouterNo(initDto.getRouterNo());
			initDto.setEditDisableFlg(true);
			// 取得データから画面項目設定
			// 電話番号
			initDto.setTel(routerMasterData.getTel());
			// ICCID
			initDto.setIccid(routerMasterData.getIccid());
			// IMEI
			initDto.setImei(routerMasterData.getImei());
			// SSID A
			initDto.setSsidA(routerMasterData.getSsidA());
			// WPA Key
			initDto.setWpaKey(routerMasterData.getWpaKey());
			// 入荷日
			initDto.setArrivalDate(skf2100Sc008SharedService.setDispDateStr(routerMasterData.getRouterArrivalDate()));
			// 故障フラグ			
			String faultFlag = routerMasterData.getFaultFlag();
			initDto.setHdnFaultFlag(faultFlag);
			initDto.setFaultFlag(faultFlag);
			if(CodeConstant.ROUTER_FAULT_FLAG_FAULT.equals(faultFlag)){
				initDto.setHdnChkFaultSelect("true");
			}

			// 契約区分
			initDto.setContractKbnSelect(routerMasterData.getRouterContractKbn());
			// 契約区分ドロップダウンリスト
			List<Map<String, Object>> contractKbnDropDownList = skfDropDownUtils
					.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_ROUTER_CONTRACT_KBN, routerMasterData.getRouterContractKbn(), true);
			initDto.setContractKbnDropDownList(contractKbnDropDownList);
			// 契約開始日
			initDto.setContractStartDate(skf2100Sc008SharedService.setDispDateStr(routerMasterData.getRouterContractStartDate()));
			// 契約終了日
			initDto.setContractEndDate(skf2100Sc008SharedService.setDispDateStr(routerMasterData.getRouterContractEndDate()));
			// 備考
			initDto.setBiko(routerMasterData.getBiko());
			// 貸出可否判定
			initDto.setHdnLendingJudgment(routerMasterData.getRouterLendingJudgment());
			
			// 補足ファイル
			// 補足1
			if (!SkfCheckUtils.isNullOrEmpty(routerMasterData.getRouterSupplementName1())) {
				initDto.setHosokuFileName1(routerMasterData.getRouterSupplementName1());
				initDto.setHosokuLink1(null);
				initDto.setHosokuLink1(Skf2100Sc008CommonDto.HOSOKU_LINK + "_" + initDto.getRouterNo() + "_1");
				initDto.setHosokuFile1(routerMasterData.getRouterSupplementFile1());
				initDto.setHosokuSize1(routerMasterData.getRouterSupplementSize1());
			}
			// 補足2
			if (!SkfCheckUtils.isNullOrEmpty(routerMasterData.getRouterSupplementName2())) {
				initDto.setHosokuFileName2(routerMasterData.getRouterSupplementName2());
				initDto.setHosokuLink2(Skf2100Sc008CommonDto.HOSOKU_LINK + "_" + initDto.getRouterNo() + "_2");
				initDto.setHosokuFile2(routerMasterData.getRouterSupplementFile2());
				initDto.setHosokuSize2(routerMasterData.getRouterSupplementSize2());
			}
			// 補足3
			if (!SkfCheckUtils.isNullOrEmpty(routerMasterData.getRouterSupplementName3())) {
				initDto.setHosokuFileName3(routerMasterData.getRouterSupplementName3());
				initDto.setHosokuLink3(Skf2100Sc008CommonDto.HOSOKU_LINK + "_" + initDto.getRouterNo() + "_3");
				initDto.setHosokuFile3(routerMasterData.getRouterSupplementFile3());
				initDto.setHosokuSize3(routerMasterData.getRouterSupplementSize3());
			}
			
			// 更新日時
			initDto.addLastUpdateDate(Skf2100Sc008CommonDto.ROUTER_KEY_LAST_UPDATE_DATE, routerMasterData.getUpdateDate());
			
			// 貸与履歴取得
			int rirekiCount = skfRouterInfoUtils.getRouterRirekiCount(Long.parseLong(initDto.getRouterNo()));
			if(rirekiCount > 0){
				// 履歴あり
				// 削除ボタン非活性
				initDto.setBtnDeleteDisableFlg(true);
			}else{
				// 削除ボタン活性
				initDto.setBtnDeleteDisableFlg(false);
			}
			
		}



	}
	

}
