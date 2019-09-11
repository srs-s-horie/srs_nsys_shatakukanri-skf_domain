/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc007;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc007.Skf3010Sc007ContractInfoSelectAsyncDto;


/**
 * Skf3010Sc007ContractInfoSelectAsyncService 駐車場契約情報変更チェック非同期処理クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3010Sc007ContractInfoSelectAsyncService
		extends AsyncBaseServiceAbstract<Skf3010Sc007ContractInfoSelectAsyncDto> {
	
	/**
	 * 駐車場契約情報の編集有無チェック
	 */
	@Override
	public AsyncBaseDto index(Skf3010Sc007ContractInfoSelectAsyncDto asyncDto) throws Exception {
		
		// Debugログで出力
		LogUtils.debugByMsg("駐車場契約情報-区画番号：" + asyncDto.getParkingBlock());
		LogUtils.debugByMsg("駐車場契約情報-契約番号：" + asyncDto.getContractPropertyId());
		
		//契約番号リスト
		List<Map<String, Object>> contractPropertyIdList = new ArrayList<Map<String, Object>>();
		Long setContractPropertyId;
		//表示時の契約番号
		String hdnBackupContractPropertyId = asyncDto.getHdnBackupContractPropertyId();
		
		//選択された契約番号
		Long selectContractPropertyId;
		if(asyncDto.getContractPropertyId() != null){
			selectContractPropertyId = Long.parseLong(asyncDto.getContractPropertyId());
		}else{
			selectContractPropertyId = (long) 0;
		}
		setContractPropertyId = selectContractPropertyId;
		
		boolean checkResult=false;
		
		//入力内容変更有無チェック
		if(asyncDto.getSelectMode().compareTo("mainList") == 0 
				&& asyncDto.getHdnDelInfoFlg().compareTo("0") != 0){
			LogUtils.debugByMsg("削除情報有 ");
			checkResult=true;
		}else if(selectContractPropertyId==0){
			LogUtils.debugByMsg("契約番号無し(情報無し)");
		}else if(asyncDto.getHdnBackupParkingContractType().compareTo(asyncDto.getParkingContractType()) != 0){
			LogUtils.debugByMsg("契約形態：変更有 " + asyncDto.getHdnBackupParkingContractType() + "!=" +asyncDto.getParkingContractType());
			checkResult=true;
		}else if(asyncDto.getHdnBackupOwnerName().compareTo(asyncDto.getOwnerName()) != 0){
			LogUtils.debugByMsg("賃貸人：変更有" + asyncDto.getHdnBackupOwnerName() + "!=" +asyncDto.getOwnerName());
			checkResult=true;
		}else if(asyncDto.getHdnBackupParkingZipCd().compareTo(asyncDto.getParkingZipCd()) != 0){
			LogUtils.debugByMsg("郵便番号：変更有" + asyncDto.getHdnBackupParkingZipCd() + "!=" +asyncDto.getParkingZipCd());
			checkResult=true;
		}else if(asyncDto.getHdnBackupParkingAddress().compareTo(asyncDto.getParkingAddress()) != 0){
			LogUtils.debugByMsg("住所：変更有" + asyncDto.getHdnBackupParkingAddress() + "!=" +asyncDto.getParkingAddress());
			checkResult=true;
		}else if(asyncDto.getHdnBackupParkingName().compareTo(asyncDto.getParkingName()) != 0){
			LogUtils.debugByMsg("駐車場名：変更有" + asyncDto.getHdnBackupParkingName() + "!=" +asyncDto.getParkingName());
			checkResult=true;
		}else if(asyncDto.getHdnBackupAssetRegisterNo().compareTo(asyncDto.getAssetRegisterNo()) != 0){
			LogUtils.debugByMsg("経理連携用資産番号：変更有" + asyncDto.getHdnBackupAssetRegisterNo() + "!=" +asyncDto.getAssetRegisterNo());
			checkResult=true;
		}else if(asyncDto.getHdnBackupParkinglendKbn().compareTo(asyncDto.getParkinglendKbn()) != 0){
			LogUtils.debugByMsg("貸与区分：変更有" + asyncDto.getHdnBackupParkinglendKbn() + "!=" +asyncDto.getParkinglendKbn());
			checkResult=true;
		}else if(asyncDto.getHdnBackupContractStartDate().compareTo(asyncDto.getContractStartDate()) != 0){
			LogUtils.debugByMsg("契約開始日：変更有" + asyncDto.getHdnBackupContractStartDate() + "!=" +asyncDto.getContractStartDate());
			checkResult=true;
		}else if(asyncDto.getHdnBackupContractEndDate().compareTo(asyncDto.getContractEndDate()) != 0){
			LogUtils.debugByMsg("契約終了日：変更有" + asyncDto.getHdnBackupContractEndDate() + "!=" +asyncDto.getContractEndDate());
			checkResult=true;
		}else if(asyncDto.getHdnBackupLandRent().compareTo(asyncDto.getLandRent()) != 0){
			LogUtils.debugByMsg("駐車場料（地代）：変更有" + asyncDto.getHdnBackupLandRent() + "!=" + asyncDto.getLandRent());
			checkResult=true;
		}else if(asyncDto.getHdnBackupBiko().compareTo(asyncDto.getBiko()) != 0){
			LogUtils.debugByMsg("備考：変更有" + asyncDto.getHdnBackupBiko() + "!=" + asyncDto.getBiko());
			checkResult=true;
		}
		
		
		if(checkResult){
			asyncDto.setCheckResult("1");
			setContractPropertyId = Long.parseLong(hdnBackupContractPropertyId);
		}
		else{
			asyncDto.setCheckResult("0");
		}
		
		//契約番号リストの最大値
		int proIdMax = Integer.parseInt(asyncDto.getHdnBackupMaxContractPropertyId());
		if(proIdMax > 0){
			//1以上で、契約番号リスト生成
			
			Map<String, Object> forListMap = new HashMap<String, Object>();
			for (int i=1; i<=proIdMax; i++) {
				// 表示・値を設定
				forListMap = new HashMap<String, Object>();
				forListMap.put("value", i);
				forListMap.put("label", i);
				if (i == setContractPropertyId.intValue()) {
					//選択契約番号を選択中にする
					forListMap.put("selected", true);
				}
				contractPropertyIdList.add(forListMap);
			}
		}
		
		
		asyncDto.setContractPropertyIdList(contractPropertyIdList);
		
		return asyncDto;
	}

}
