/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3070.domain.service.skf3070sc003;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3070Sc003.Skf3070Sc003GetOwnerPropertyExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3070Sc003.Skf3070Sc003GetOwnerPropertyExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3070TOwnerInfo;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3070Sc003.Skf3070Sc003GetOwnerPropertyExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3070TOwnerInfoRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3070.domain.dto.skf3070sc003.Skf3070Sc003InitDto;

/**
 * 賃貸人（代理人）所有物件一覧画面のInitサービス処理クラス。　 
 * 
 */
@Service
public class Skf3070Sc003InitService extends BaseServiceAbstract<Skf3070Sc003InitDto> {
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private Skf3070TOwnerInfoRepository skf3070TOwnerInfoRepository;
	@Autowired
	private Skf3070Sc003GetOwnerPropertyExpRepository skf3070Sc003GetOwnerPropertyExpRepository;
	
	
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
	public Skf3070Sc003InitDto index(Skf3070Sc003InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF3070_SC003_TITLE);
		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, FunctionIdConstant.SKF3070_SC003);
		
		// 前画面から送られてくる値
		LogUtils.debugByMsg("賃貸人（代理人）番号:"+initDto.getOwnerNo()+"  検索条件開始年月:"+initDto.getRecodeDadefrom()+"  検索条件終了年月:"+initDto.getRecodeDadeto());
		
		// 初期表示項目の設定
		this.init(initDto);

		return initDto;
	}
	
	/**
	 * 初期表示時の画面情報をDtoに設定する
	 * 
	 * @param initDto
	 */
	private void init(Skf3070Sc003InitDto initDto) {

		// 個人法人区分汎用コード取得
        Map<String, String> businessKbnGenCodeMap = new HashMap<String, String>();
        businessKbnGenCodeMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_KOJIN_HOJIN_KUBUN);
		// 個人法人区分汎用コード取得
        Map<String, String> acceptFlgGenCodeMap = new HashMap<String, String>();
        acceptFlgGenCodeMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_AGENT_INDIVIDUAL_NUMBER_DEMAND_SITUATION);
        
		// 賃貸人（代理人）情報を取得
		long ownerNo = Long.parseLong(initDto.getOwnerNo());
		Skf3070TOwnerInfo ownerInfo = new Skf3070TOwnerInfo();
		ownerInfo = skf3070TOwnerInfoRepository.selectByPrimaryKey(ownerNo);
		
		if (ownerInfo != null) {
			// 賃貸人（代理人）情報が取得できた場合、画面項目を設定
			initDto.setOwnerName(ownerInfo.getOwnerName());
			initDto.setOwnerNameKk(ownerInfo.getOwnerNameKk());
			initDto.setZipCode(ownerInfo.getZipCd());
			initDto.setAddress(ownerInfo.getAddress());
			String businessKbn = CodeConstant.NONE;
			if(ownerInfo.getBusinessKbn() != null){
				businessKbn = businessKbnGenCodeMap.get(ownerInfo.getBusinessKbn());
			}
			initDto.setBusinessKbn(businessKbn);
			
			String acceptFlg = CodeConstant.NONE;
			if(ownerInfo.getAcceptFlg() != null){
				acceptFlg = acceptFlgGenCodeMap.get(ownerInfo.getAcceptFlg());
			}
			initDto.setAcceptFlg(acceptFlg);
			initDto.setAcceptStatus(ownerInfo.getAcceptStatus());
			initDto.setRemarks(ownerInfo.getRemarks());
		}
		
		// 所有物件情報を取得
		List<Skf3070Sc003GetOwnerPropertyExp> ownerList = new ArrayList<Skf3070Sc003GetOwnerPropertyExp>();
		Skf3070Sc003GetOwnerPropertyExpParameter param2 = new Skf3070Sc003GetOwnerPropertyExpParameter();
		param2.setOwnerNo(ownerNo);
		param2.setRecodeDadefrom(initDto.getRecodeDadefrom());
		param2.setRecodeDadeto(initDto.getRecodeDadeto());
		ownerList = skf3070Sc003GetOwnerPropertyExpRepository.getOwnerProperty(param2);
		
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
		listTableData = this.getOwnerPropertyList(ownerList);
		
		initDto.setListTableData(listTableData);
		
	}
	
	/**
	 * 所有物件一覧情報のマッピングを行う
	 * 
	 * @param ownerList
	 * @return 所有物件一覧情報リストマップ
	 */
	private List<Map<String, Object>> getOwnerPropertyList(List<Skf3070Sc003GetOwnerPropertyExp> ownerList){
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
		
        // 都道府県汎用コード取得
        Map<String, String> prefCdGenCodeMap = new HashMap<String, String>();
        prefCdGenCodeMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_PREFCD);
        // 構造区分汎用コード取得
        Map<String, String> structureKbnGenCodeMap = new HashMap<String, String>();
        structureKbnGenCodeMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_STRUCTURE_KBN);
		
		for(Skf3070Sc003GetOwnerPropertyExp ownerProperty : ownerList){
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			
			String contractStartDate = CodeConstant.NONE;
			if(ownerProperty.getContractStartDate() != null){
				contractStartDate = skfDateFormatUtils.dateFormatFromString(ownerProperty.getContractStartDate(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}
			String contractEndDate = CodeConstant.NONE;
			if(ownerProperty.getContractEndDate() != null){
				contractEndDate = skfDateFormatUtils.dateFormatFromString(ownerProperty.getContractEndDate(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}
			
	        //NumberFormatインスタンスを生成
	        NumberFormat nfNum = NumberFormat.getNumberInstance();
	        
			tmpMap.put("agencyName", ownerProperty.getAgencyName());
			tmpMap.put("shatakuName", ownerProperty.getShatakuName());
			tmpMap.put("contractKbn", ownerProperty.getContractKbnName());
			
			// 住所は都道府県コード＋住所
			String prefectures = CodeConstant.NONE;
			if(ownerProperty.getPrefCd() != null){
				prefectures = prefCdGenCodeMap.get(ownerProperty.getPrefCd());
			}
			tmpMap.put("address", prefectures+ownerProperty.getAddress());
			
			// 「構造（補足）」が無ければ「構造区分」から表示。
			String structure = CodeConstant.NONE;
			if(ownerProperty.getStructureSupplement() == null || CheckUtils.isEmpty(ownerProperty.getStructureSupplement().trim())){
				if(ownerProperty.getStructureKbn() != null){
					structure = structureKbnGenCodeMap.get(ownerProperty.getStructureKbn());
				}
			}else{
				structure = ownerProperty.getStructureSupplement();
			}
			// 契約区分が'社宅'の場合
			if(ownerProperty.getContractKbn().equals("1")){
				tmpMap.put("roomNo", ownerProperty.getRoomNo());
				tmpMap.put("structureSupplement", structure);
				tmpMap.put("originalMenseki", ownerProperty.getOriginalMenseki());
				tmpMap.put("rent", nfNum.format(ownerProperty.getRent()));
				tmpMap.put("kyoekihi", nfNum.format(ownerProperty.getKyoekihi()));	
				
			// 契約区分が'駐車場'の場合「部屋番号」 「構造」「面積」「賃料」「共益費」 は空白
			}else{
				tmpMap.put("roomNo", CodeConstant.NONE);
				tmpMap.put("structureSupplement", CodeConstant.NONE);
				tmpMap.put("originalMenseki", CodeConstant.NONE);
				tmpMap.put("rent", CodeConstant.NONE);
				tmpMap.put("kyoekihi", CodeConstant.NONE);
			}
			
			tmpMap.put("assetRegisterNo", ownerProperty.getAssetRegisterNo());
			tmpMap.put("contractStartDate", contractStartDate);
			tmpMap.put("contractEndDate", contractEndDate);
			tmpMap.put("landRent", nfNum.format(ownerProperty.getLandRent()));

			listTableData.add(tmpMap);
		}

		return listTableData;
	}
	
}
