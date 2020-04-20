/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc007;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.skf.common.SkfAsyncServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc007.Skf3010Sc007ChangeContractTypeAsyncDto;

/**
 * Skf3010Sc007ContractInfoSelectAsyncService 駐車場契約形態変更非同期処理クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3010Sc007ChangeContractTypeAsyncService
		extends SkfAsyncServiceAbstract<Skf3010Sc007ChangeContractTypeAsyncDto> {
	
	@Autowired
	private Skf3010Sc007SharedService skf3010Sc007SharedService;
	
	private final static String TRUE = "true";
	private final static String FALSE = "false";
	/**
	 * 駐車場契約情報の契約形態変更
	 */
	@Override
	public AsyncBaseDto index(Skf3010Sc007ChangeContractTypeAsyncDto changeDto) throws Exception {
		
		// Debugログで出力
		LogUtils.debugByMsg("駐車場契約情報-契約形態：" + changeDto.getParkingContractType());

		//契約形態リスト
		List<Map<String, Object>> parkingContractTypeList = new ArrayList<Map<String, Object>>();
		//貸与区分リスト
		List<Map<String, Object>> parkinglendKbnList = new ArrayList<Map<String, Object>>();
		
		
		//契約形態
		String contractType = changeDto.getParkingContractType();
		if(SkfCheckUtils.isNullOrEmpty(contractType)){
			//NULLか空
			//リスト設定
			skf3010Sc007SharedService.getDoropDownList("", parkingContractTypeList, "", parkinglendKbnList);
			changeDto.setContractInfoDisabled(TRUE);
		}else if(contractType.compareTo(Skf3010Sc007CommonSharedService.CONTRACT_TYPE_1) == 0){
			//社宅と一括契約の場合
			//リスト設定
			skf3010Sc007SharedService.getDoropDownList(Skf3010Sc007CommonSharedService.CONTRACT_TYPE_1, parkingContractTypeList, "", parkinglendKbnList);
			//駐車場契約情報のすべての入力項目を非活性、空白とする。
			//駐車場契約情報の「支援」「住所検索」ボタンを非活性にする。
			changeDto.setContractInfoDisabled(TRUE);
		}else{
			//社宅と別契約の場合
			//リスト設定
			skf3010Sc007SharedService.getDoropDownList(Skf3010Sc007CommonSharedService.CONTRACT_TYPE_2, parkingContractTypeList, "", parkinglendKbnList);
			//駐車場契約情報のすべての入力項目を活性とする。
			//駐車場契約情報の「支援」「住所検索」ボタンを活性にする。
			changeDto.setContractInfoDisabled(FALSE);
		}
		
		//入力項目初期化
		changeDto.setOwnerName(CodeConstant.DOUBLE_QUOTATION);
		changeDto.setOwnerNo(CodeConstant.DOUBLE_QUOTATION);
		changeDto.setParkingZipCd(CodeConstant.DOUBLE_QUOTATION);
		changeDto.setParkingAddress(CodeConstant.DOUBLE_QUOTATION);
		changeDto.setParkingName(CodeConstant.DOUBLE_QUOTATION);
		changeDto.setAssetRegisterNo(CodeConstant.DOUBLE_QUOTATION);
		changeDto.setContractStartDate(CodeConstant.DOUBLE_QUOTATION);
		changeDto.setContractEndDate(CodeConstant.DOUBLE_QUOTATION);
		changeDto.setLandRent(null);
		changeDto.setBiko(CodeConstant.DOUBLE_QUOTATION);
		
		//各リスト
		changeDto.setParkingContractTypeList(parkingContractTypeList);
		changeDto.setParkinglendKbnList(parkinglendKbnList);
		
		return changeDto;
	}

}
