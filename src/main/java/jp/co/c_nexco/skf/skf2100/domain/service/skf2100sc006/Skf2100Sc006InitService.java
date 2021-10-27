/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc006;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2100Sc006.Skf2100Sc006GetRouterLendYoteiDataByKanriIdExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2100Sc006.Skf2100Sc006GetRouterLendYoteiDataByKanriIdExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2100Sc006.Skf2100Sc006GetRouterLendYoteiDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2100Sc006.Skf2100Sc006GetRouterLendYoteiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2100Sc006.Skf2100Sc006GetShainInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetIdmPreUserMasterInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetIdmPreUserMasterInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2100MMobileRouterWithBLOBs;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2100TMobileRouterLedger;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2100Sc006.Skf2100Sc006GetRouterLendYoteiDataByKanriIdExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2100Sc006.Skf2100Sc006GetRouterLendYoteiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2100Sc006.Skf2100Sc006GetShainInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetIdmPreUserMasterInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2100TMobileRouterLedgerRepository;
import jp.co.c_nexco.nfw.common.bean.ApplicationScopeBean;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.PropertyUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.SkfRouterInfoUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100Sc006common.Skf2100Sc006CommonDto;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc006.Skf2100Sc006InitDto;
import jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc005.Skf2100Sc005CommonSharedService;

/**
 * Skf2100Sc006InitService モバイルルーター貸出管理簿登録画面のInitサービス処理クラス。　 
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf2100Sc006InitService extends SkfServiceAbstract<Skf2100Sc006InitDto> {
	
	@Autowired
	private ApplicationScopeBean bean;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfRouterInfoUtils skfRouterInfoUtils;
	@Autowired
	private Skf2100TMobileRouterLedgerRepository skf2100TMobileRouterLedgerRepository;
	@Autowired
	private Skf2100Sc006SharedService skf2100Sc006SharedService;
	@Autowired
	private Skf3022Sc006GetIdmPreUserMasterInfoExpRepository skf3022Sc006GetIdmPreUserMasterInfoExpRepository;
	@Autowired
	private Skf2100Sc006GetRouterLendYoteiDataExpRepository skf2100Sc006GetRouterLendYoteiDataExpRepository;
	@Autowired
	private Skf2100Sc006GetRouterLendYoteiDataByKanriIdExpRepository skf2100Sc006GetRouterLendYoteiDataByKanriIdExpRepository;
	@Autowired
	private Skf2100Sc006GetShainInfoExpRepository skf2100Sc006GetShainInfoExpRepository;
	
	// 管理番号なし(0番)
	private static final String NO_KANRI_ID = "0";
	
	/** IdM_プレユーザマスタ（従業員区分）定数 */
	// 役員
	private static final String IDM_YAKUIN = "1";
	// 職員
	private static final String IDM_SHOKUIN = "2";
	// 常勤嘱託員
	private static final String IDM_JOKIN_SHOKUTAKU = "3";
	// 非常勤嘱託員
	private static final String IDM_HI_JOKIN_SHOKUTAKU = "4";
	// 再任用職員
	private static final String IDM_SAININ_SHOKUIN = "5";
	// 再任用短時間勤務職員
	private static final String IDM_SAININ_TANJIKAN_SHOKUIN = "6";
	// 有機事務員
	private static final String IDM_YUKI_JIMUIN = "7";
	
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
	public Skf2100Sc006InitDto index(Skf2100Sc006InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF2100_SC006_TITLE);
 		
		// デバッグログ
		LogUtils.debugByMsg("初期表示");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, FunctionIdConstant.SKF2100_SC006);
		// DTO初期化
		skf2100Sc006SharedService.initialize(initDto);
		
		//エラー変数初期化
		skf2100Sc006SharedService.errReset(initDto);
		
		//画面初期化
		initialize(initDto);
		
		// 運用ガイドのパスを設定
		initDto.setOperationGuidePath("/skf/template/skf3022/skf3022mn006/"
				+ PropertyUtils.getValue("skf3022.skf3022_sc006.operationGuideFile"));
		
		
		return initDto;
	}
	
	@SuppressWarnings("unchecked")
	private void initialize(Skf2100Sc006InitDto initDto) throws Exception{
		
		//セッション情報をチェック
		Map<String, Object> searchSessionData = (Map<String, Object>) bean
				.get(SessionCacheKeyConstant.MOBILEROUTER_LEDGER_SEARCH);
		
		if (searchSessionData != null) {
			// 検索条件データ
			String routerNo = (String) searchSessionData.get(Skf2100Sc005CommonSharedService.SEARCH_INFO_ROUTER_NO_KEY);
			String tel = (String) searchSessionData.get(Skf2100Sc005CommonSharedService.SEARCH_INFO_TEL_KEY);
			String shainNo = (String) searchSessionData.get(Skf2100Sc005CommonSharedService.SEARCH_INFO_SHAIN_NO_KEY);
			String shainName = (String) searchSessionData.get(Skf2100Sc005CommonSharedService.SEARCH_INFO_SHAIN_NAME_KEY);
			String contractKbn = (String) searchSessionData.get(Skf2100Sc005CommonSharedService.SEARCH_INFO_CONTRACT_KBN_KEY);
			String nengetu = (String) searchSessionData.get(Skf2100Sc005CommonSharedService.SEARCH_INFO_NENGETSU_KEY);
			String akiRouter = (String) searchSessionData.get(Skf2100Sc005CommonSharedService.SEARCH_INFO_AKI_ROUTER_KEY);
			String contractEndDateFrom = (String) searchSessionData.get(Skf2100Sc005CommonSharedService.SEARCH_INFO_CONTRACT_END_DATE_FROM_KEY);
			String contractEndDateTo = (String) searchSessionData.get(Skf2100Sc005CommonSharedService.SEARCH_INFO_CONTRACT_END_DATE_TO_KEY);
			
			//DTOに設定
			initDto.setSearchInfoRouterNo(routerNo);
			initDto.setSearchInfoTel(tel);
			initDto.setSearchInfoShainNo(shainNo);
			initDto.setSearchInfoShainName(shainName);
			initDto.setSearchInfoContractKbn(contractKbn);
			initDto.setSearchInfoContractEndDateFrom(contractEndDateFrom);
			initDto.setSearchInfoContractEndDateTo(contractEndDateTo);
			initDto.setSearchInfoYearMonth(nengetu);
			initDto.setSearchInfoAkiRouter(akiRouter);
		}
		//削除後動作エラー回避のためDTOクリア
		initDto.setHdnRouterKanriId(CodeConstant.DOUBLE_QUOTATION);

		
		Map<String, Object> selListSessionData = (Map<String, Object>) bean
				.get(SessionCacheKeyConstant.MOBILEROUTER_LEDGER_INFO);
		
		if (selListSessionData != null) {
			// 選択した検索リストの管理簿データ
			Long selRouterKanriId = (Long) selListSessionData.get(Skf2100Sc005CommonSharedService.SEL_LIST_INFO_ROUTER_KANRI_ID_KEY);
			Long selRouterNo = (Long) selListSessionData.get(Skf2100Sc005CommonSharedService.SEL_LIST_INFO_ROUTER_NO_KEY);
			String selShainNo = (String) selListSessionData.get(Skf2100Sc005CommonSharedService.SEL_LIST_INFO_SHAIN_NO_KEY);
			String selShainName = (String) selListSessionData.get(Skf2100Sc005CommonSharedService.SEL_LIST_INFO_SHAIN_NAME_KEY);
			String selYearMonth = (String) selListSessionData.get(Skf2100Sc005CommonSharedService.SEL_LIST_INFO_YEARMONTH_KEY);
			String selApplStatus = (String) selListSessionData.get(Skf2100Sc005CommonSharedService.SEL_LIST_INFO_APPL_STATUS_KEY);

			
			// 画面項目に設定する
			// 貸出管理簿ID
			if(selRouterKanriId != null){
				initDto.setHdnRouterKanriId(selRouterKanriId.toString());
			}else{
				initDto.setHdnRouterKanriId(NO_KANRI_ID);
			}
			
			// 通しNo
			if(selRouterNo == null){
				LogUtils.debugByMsg("通しNo無しで処理続行不可");
				ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1077);
				return;
			}
			initDto.setRouterNo(selRouterNo.toString());
			//「年月」を「対象年月」に設定する
			initDto.setHdnYearMonth(selYearMonth);
			if(!CheckUtils.isEmpty(selYearMonth) && selYearMonth.length() == 6){
				String taishoNengetsu = selYearMonth.substring(0, 4) + "年" + selYearMonth.substring(4, 6) + "月";
				initDto.setYearMonthTxt(taishoNengetsu);
			}
			// 社員番号
			if(!SkfCheckUtils.isNullOrEmpty(selShainNo)){
				initDto.setShainNo(selShainNo);
				initDto.setHdnShainNo(selShainNo);
			}
			// 社員名
			if(!SkfCheckUtils.isNullOrEmpty(selShainName)){
				initDto.setShainName(selShainName);
				initDto.setHdnShainName(selShainName);
			}
			// 申請状況
			if(!SkfCheckUtils.isNullOrEmpty(selApplStatus)){
				initDto.setHdnApplStatus(selApplStatus);
			}
			
			//「モバイルルーター締め処理実行区分」を取得
			String billingActKbn = skf2100Sc006SharedService.getBillingActKbn(selYearMonth);
			initDto.setHdnBillingActKbn(billingActKbn);
		}
		
		bean.clear();
		
		// ドロップダウンリスト
		List<Map<String, Object>> originalCompanyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> payCompanyList = new ArrayList<Map<String, Object>>();
		
		// 項目初期化
		// 貸出希望日
		initDto.setUseStartHopeDay(CodeConstant.DOUBLE_QUOTATION);
		// 本人連絡先（搬入）
		initDto.setHannyuTel(CodeConstant.DOUBLE_QUOTATION);
		// 本人メールアドレス
		initDto.setHannyuMailaddress(CodeConstant.DOUBLE_QUOTATION);
		// 発送日
		initDto.setShippingDate(CodeConstant.DOUBLE_QUOTATION);
		// 納品日
		initDto.setReceivedDate(CodeConstant.DOUBLE_QUOTATION);
		// 利用停止日
		initDto.setUseStopDay(CodeConstant.DOUBLE_QUOTATION);
		// 本人連絡先（搬出）
		initDto.setHansyutuTel(CodeConstant.DOUBLE_QUOTATION);
		// 窓口返却日
		initDto.setReturnDay(CodeConstant.DOUBLE_QUOTATION);
		// 備考
		initDto.setBiko(CodeConstant.DOUBLE_QUOTATION);
		// 原籍会社
		initDto.setOriginalCompanySelect(CodeConstant.DOUBLE_QUOTATION);
		// 給与支給会社
		initDto.setPayCompanySelect(CodeConstant.DOUBLE_QUOTATION);

		
		// 「モバイルルーター貸出管理簿ID」が”0”の場合
		if(NO_KANRI_ID.equals(initDto.getHdnRouterKanriId())){
			
			if (CheckUtils.isEmpty(initDto.getOriginalCompanySelect()) && 
					!CheckUtils.isEmpty(initDto.getHdnShainNo())) {
				// 原籍会社未設定かつ社員番号設定有りの場合
				// ①IdM_プレユーザマスタ（従業員区分）を取得
				List<Skf3022Sc006GetIdmPreUserMasterInfoExp> dtbIdmPreUserMasterInfo = new ArrayList<Skf3022Sc006GetIdmPreUserMasterInfoExp>();
				Skf3022Sc006GetIdmPreUserMasterInfoExpParameter param = new Skf3022Sc006GetIdmPreUserMasterInfoExpParameter();
				param.setPumHrNameCode(initDto.getHdnShainNo());
				dtbIdmPreUserMasterInfo = skf3022Sc006GetIdmPreUserMasterInfoExpRepository.getIdmPreUserMasterInfo(param);
				// ②従業員区分が「1:役員、2:職員、3:常勤嘱託員、4:非常勤嘱託員、5:再任用職員、6:再任用短時間勤務職員、7:有機事務員」の場合
				if (dtbIdmPreUserMasterInfo.size() > 0) {
					if (Objects.equals(dtbIdmPreUserMasterInfo.get(0).getPumHrEmployeeClass_0(), IDM_YAKUIN)
							|| Objects.equals(dtbIdmPreUserMasterInfo.get(0).getPumHrEmployeeClass_0(), IDM_SHOKUIN)
							|| Objects.equals(dtbIdmPreUserMasterInfo.get(0).getPumHrEmployeeClass_0(), IDM_JOKIN_SHOKUTAKU)
							|| Objects.equals(dtbIdmPreUserMasterInfo.get(0).getPumHrEmployeeClass_0(), IDM_HI_JOKIN_SHOKUTAKU)
							|| Objects.equals(dtbIdmPreUserMasterInfo.get(0).getPumHrEmployeeClass_0(), IDM_SAININ_SHOKUIN)
							|| Objects.equals(dtbIdmPreUserMasterInfo.get(0).getPumHrEmployeeClass_0(), IDM_SAININ_TANJIKAN_SHOKUIN)
							|| Objects.equals(dtbIdmPreUserMasterInfo.get(0).getPumHrEmployeeClass_0(), IDM_YUKI_JIMUIN)) {
						// 原籍会社に「NEXCO中日本（C001）」を設定
						initDto.setOriginalCompanySelect(CodeConstant.C001);
					}
				}
			}
			
			
			// モバイルルーターマスタ情報を取得
			Skf2100MMobileRouterWithBLOBs routerMasterData = skfRouterInfoUtils.getMobileRouterInfo(Long.parseLong(initDto.getRouterNo()));
			if(routerMasterData == null){
				ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1077);
				return;
			}
			
			// 画面項目設定
			// 電話番号
			initDto.setTel(routerMasterData.getTel());
			// ICCID
			initDto.setIccid(routerMasterData.getIccid());
			// IMEI
			initDto.setImei(routerMasterData.getImei());
			// 入荷日
			initDto.setArrivalDate(skf2100Sc006SharedService.setDispDateStr(routerMasterData.getRouterArrivalDate()));
			// 契約区分
			initDto.setContractKbnTxt(skf2100Sc006SharedService.setContractKbnTxt(routerMasterData.getRouterContractKbn()));
			// 契約終了日
			initDto.setContractEndDate(skf2100Sc006SharedService.setDispDateStr(routerMasterData.getRouterContractEndDate()));
			// 故障フラグ			
			String faultFlag = routerMasterData.getFaultFlag();
			initDto.setHdnFaultFlag(faultFlag);
			if(CodeConstant.ROUTER_FAULT_FLAG_FAULT.equals(faultFlag)){
				initDto.setFaultTxt(CodeConstant.ROUTER_FAULT_STRING);
			}else{
				initDto.setFaultTxt(CodeConstant.DOUBLE_QUOTATION);
			}
			
			// 社員番号設定有りの場合、貸出予定、借用申請情報を取得する（申請中）
			if(!CheckUtils.isEmpty(initDto.getHdnShainNo())){
				Skf2100Sc006GetRouterLendYoteiDataExpParameter rlyDataParam = new Skf2100Sc006GetRouterLendYoteiDataExpParameter();
				rlyDataParam.setShainNo(initDto.getHdnShainNo());
				rlyDataParam.setMobileRouterNo(Long.parseLong(initDto.getRouterNo()));
				List<Skf2100Sc006GetRouterLendYoteiDataExp> rlyData = 
						skf2100Sc006GetRouterLendYoteiDataExpRepository.getRouterLendYoteiData(rlyDataParam);
				if(rlyData != null && rlyData.size() > 0){
					// データ取得有の場合、画面項目を設定
					initDto.setShainName(rlyData.get(0).getName());
					initDto.setHdnApplStatus(rlyData.get(0).getRouterApplStatus());
					initDto.setOriginalCompanySelect(rlyData.get(0).getOriginalCompanyCd());
					initDto.setPayCompanySelect(rlyData.get(0).getPayCompanyCd());
					initDto.setUseStartHopeDay(rlyData.get(0).getUseStartHopeDay());
					initDto.setHannyuTel(rlyData.get(0).getTel());
					initDto.setHannyuMailaddress(rlyData.get(0).getMailaddress());
					initDto.setShippingDate(rlyData.get(0).getShippingDate());
					initDto.setReceivedDate(rlyData.get(0).getReceivedDate());
				}
				
				// 社員番号変更フラグ設定
				String shainNoChangeFlag = skf2100Sc006SharedService.getShainNoChangeFlag(initDto.getHdnShainNo());
				initDto.setHdnShainNoChangeFlg(shainNoChangeFlag);
				if("1".equals(initDto.getHdnShainNoChangeFlg())){
					initDto.setShainNo(initDto.getHdnShainNo() + CodeConstant.ASTERISK);
				}else{
					initDto.setShainNo(initDto.getHdnShainNo());
				}
			}
			
			// ドロップダウンリストを設定
			skf2100Sc006SharedService.setDdlControlValues(
					initDto.getOriginalCompanySelect(), originalCompanyList,
					initDto.getPayCompanySelect(), payCompanyList);

			// 申請状況の設定
			String statusTxt = skf2100Sc006SharedService.setApplStatusTxt(initDto.getHdnApplStatus(),initDto.getHdnYearMonth());
			initDto.setApplStatusTxt(statusTxt);
			String statusColor = skf2100Sc006SharedService.setApplStatusCss(initDto.getHdnApplStatus());
			initDto.setApplStatusTxtColor(statusColor);
			
		}else{
			// 「モバイルルーター貸出管理簿ID」が値を持つ場合
			Long routerKanriId = Long.parseLong(initDto.getHdnRouterKanriId());
			Long routerNo = Long.parseLong(initDto.getRouterNo());

			// モバイルルーター貸出管理簿情報を取得
			Skf2100TMobileRouterLedger routerLdata = skf2100TMobileRouterLedgerRepository.selectByPrimaryKey(routerKanriId);
			if(routerLdata == null){
				ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1077);
				return;
			}
			
			// モバイルルーターマスタ情報を取得
			Skf2100MMobileRouterWithBLOBs routerMData = skfRouterInfoUtils.getMobileRouterInfo(routerNo);
			if(routerMData == null){
				ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1077);
				return;
			}
			
			// 管理簿IDに対応する貸出予定データ取得
			Skf2100Sc006GetRouterLendYoteiDataByKanriIdExpParameter yoteiParam = new Skf2100Sc006GetRouterLendYoteiDataByKanriIdExpParameter();
			yoteiParam.setMobileRouterKanriId(routerKanriId);
			yoteiParam.setMobileRouterNo(routerNo);
			yoteiParam.setShainNo(initDto.getHdnShainNo());
			List<Skf2100Sc006GetRouterLendYoteiDataByKanriIdExp> yoteiDataList = 
					skf2100Sc006GetRouterLendYoteiDataByKanriIdExpRepository.getRouterLendYoteiDataByKanriId(yoteiParam);
			
			if(yoteiDataList != null && yoteiDataList.size() > 0){
				// データある場合のみ処理
				// 貸与と返却がある場合、返却を優先するS
				String yoteiApplStatus = CodeConstant.DOUBLE_QUOTATION;
				for(Skf2100Sc006GetRouterLendYoteiDataByKanriIdExp yoteiData : yoteiDataList){
					if(CodeConstant.TAIYO_HENKYAKU_KBN_HENKYAKU.equals(yoteiData.getTaiyoHenkyakuKbn())){
						// 返却の場合無条件で設定
						yoteiApplStatus = yoteiData.getRouterApplStatus();
					}else if(SkfCheckUtils.isNullOrEmpty(yoteiApplStatus)){
						// 貸与の場合、空なら設定
						yoteiApplStatus = yoteiData.getRouterApplStatus();
					}
					
				}
				// DTOに申請ステータス設定
				initDto.setHdnApplStatus(yoteiApplStatus);
			}
			
			// 社員情報取得
			List<Skf2100Sc006GetShainInfoExp> shainDataList = skf2100Sc006GetShainInfoExpRepository.getShainInfo(routerKanriId);
			if(shainDataList != null && shainDataList.size() > 0){
				initDto.setShainName(shainDataList.get(0).getName());
				initDto.setHdnShainNoChangeFlg(shainDataList.get(0).getShainNoChangeFlg());
				// 社員番号変更フラグセット
				if("1".equals(initDto.getHdnShainNoChangeFlg())){
					initDto.setShainNo(initDto.getHdnShainNo() + CodeConstant.ASTERISK);
				}else{
					initDto.setShainNo(initDto.getHdnShainNo());
				}
			}else{
				ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1077);
				return;
			}
			
			// 取得データから画面項目設定
			// 原籍会社
			initDto.setOriginalCompanySelect(routerLdata.getOriginalCompanyCd());
			// 給与支給会社
			initDto.setPayCompanySelect(routerLdata.getPayCompanyCd());
			// 電話番号
			initDto.setTel(routerMData.getTel());
			// ICCID
			initDto.setIccid(routerMData.getIccid());
			// IMEI
			initDto.setImei(routerMData.getImei());
			// 入荷日
			initDto.setArrivalDate(skf2100Sc006SharedService.setDispDateStr(routerMData.getRouterArrivalDate()));
			// 契約区分
			initDto.setContractKbnTxt(skf2100Sc006SharedService.setContractKbnTxt(routerMData.getRouterContractKbn()));
			// 契約終了日
			initDto.setContractEndDate(skf2100Sc006SharedService.setDispDateStr(routerMData.getRouterContractEndDate()));
			// 故障フラグ			
			String faultFlag = routerMData.getFaultFlag();
			initDto.setHdnFaultFlag(faultFlag);
			if(CodeConstant.ROUTER_FAULT_FLAG_FAULT.equals(faultFlag)){
				initDto.setFaultTxt(CodeConstant.ROUTER_FAULT_STRING);
			}else{
				initDto.setFaultTxt(CodeConstant.DOUBLE_QUOTATION);
			}
			// 貸出希望日
			initDto.setUseStartHopeDay(routerLdata.getUseStartHopeDay());
			// 本人連絡先(搬入)
			initDto.setHannyuTel(routerLdata.getHannyuTel());
			// メールアドレス
			initDto.setHannyuMailaddress(routerLdata.getHannyuMailaddress());
			// 発送日
			initDto.setShippingDate(routerLdata.getShippingDate());
			// 納品日
			initDto.setReceivedDate(routerLdata.getReceivedDate());
			// 利用停止日
			initDto.setUseStopDay(routerLdata.getUseStopDay());
			// 本人連絡先（搬出）
			initDto.setHansyutuTel(routerLdata.getHansyutuTel());
			// 窓口返却日
			initDto.setReturnDay(routerLdata.getReturnDay());
			// 備考
			initDto.setBiko(routerLdata.getBiko());
			
			// 更新日時
			initDto.addLastUpdateDate(Skf2100Sc006CommonDto.ROUTER_KEY_LAST_UPDATE_DATE, routerLdata.getUpdateDate());
			
		}

		// 申請状況の設定
		String statusTxt = skf2100Sc006SharedService.setApplStatusTxt(initDto.getHdnApplStatus(),initDto.getHdnYearMonth());
		initDto.setApplStatusTxt(statusTxt);
		String statusColor = skf2100Sc006SharedService.setApplStatusCss(initDto.getHdnApplStatus());
		initDto.setApplStatusTxtColor(statusColor);
		
		// ドロップダウンリストを設定
		skf2100Sc006SharedService.setDdlControlValues(
				initDto.getOriginalCompanySelect(), originalCompanyList,
				initDto.getPayCompanySelect(), payCompanyList);
		
		//画面項目制御
		skf2100Sc006SharedService.setControlStatus(initDto);
		
		// ドロップダウンリスト設定
		initDto.setOriginalCompanyList(originalCompanyList);
		initDto.setPayCompanyList(payCompanyList);

	}
	

}
