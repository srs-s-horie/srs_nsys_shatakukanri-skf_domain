/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc007;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.emory.mathcs.backport.java.util.Collections;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc007.Skf3010Sc007SelectListDto;

/**
 * 駐車場契約情報登録画面のリスト選択処理サービス処理クラス。　 
 * 
 */
@Service
public class Skf3010Sc007SelectListService extends BaseServiceAbstract<Skf3010Sc007SelectListDto> {
	

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3010Sc007SharedService skf3010Sc007SharedService;
	
	private final static String TRUE = "true";
	private final static String FALSE = "false";

	//遷移元(メインリスト)
	private final static String BUTTON_LIST = "mainList";
	//遷移元(追加ボタン)
	private final static String BUTTON_ADD = "addButton";
	//遷移元(削除ボタン)
	private final static String BUTTON_DELETE = "deleteButton";
	//遷移元(削除ボタン(追加情報))
	private final static String BUTTON_DELETEADD = "deleteButtonAdd";
	
	/**
	 * 駐車場契約情報の表示処理を行う。　
	 * 
	 * @param selectDto
	 *            選択中の契約情報DTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Skf3010Sc007SelectListDto index(Skf3010Sc007SelectListDto selectDto) throws Exception {
		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("選択", CodeConstant.C001, selectDto.getPageId());
				
		// Debugログで出力
		LogUtils.debugByMsg("駐車場契約情報-区画番号：" + selectDto.getHdnParkingBlock());
		LogUtils.debugByMsg("駐車場契約情報-契約番号：" + selectDto.getContractPropertyId());
		LogUtils.debugByMsg("駐車場契約情報-モード：" + selectDto.getSelectMode());
		LogUtils.debugByMsg("駐車場契約情報-駐車場管理番号：" + selectDto.getParkingKanriNo());
		//契約番号リスト
		List<Map<String, Object>> contractPropertyIdList = new ArrayList<Map<String, Object>>();
		List<Long> propertyIdList = new ArrayList<Long>();
		Long deleteContractPropertyId = (long) -1;
		
		//契約形態リスト
		List<Map<String, Object>> parkingContractTypeList = new ArrayList<Map<String, Object>>();
		//貸与区分リスト
		List<Map<String, Object>> parkinglendKbnList = new ArrayList<Map<String, Object>>();
		
		//選択された区画番号
		String selectParkingBlock = selectDto.getHdnParkingBlock();
		//選択された契約番号
		Long selectContractPropertyId;
		
		if( !SkfCheckUtils.isNullOrEmpty(selectDto.getContractPropertyId())){
			selectContractPropertyId = Long.parseLong(selectDto.getContractPropertyId());
		}else{
			selectContractPropertyId = CodeConstant.LONG_ZERO;//0設定
		}
		
		
		//追加ボタンからの遷移かチェック
		if(BUTTON_ADD.compareTo(selectDto.getSelectMode()) == 0){
			//契約番号最大値取得
			selectContractPropertyId = Long.parseLong(selectDto.getHdnBackupMaxContractPropertyId());
			//契約番号最大値に1加算
			selectContractPropertyId += 1;
			LogUtils.debugByMsg("駐車場契約情報追加-契約番号：" + selectContractPropertyId);
			propertyIdList.add(selectContractPropertyId);
			//追加ボタン非活性
			selectDto.setAddButtonDisabled(TRUE);
			//「削除」「登録」「キャンセル」ボタンを活性
			selectDto.setContractListDisabled(FALSE);
			selectDto.setDeleteButtonDisabled(FALSE);
			selectDto.setRegistButtonDisabled(FALSE);
			selectDto.setCancelButtonDisabled(FALSE);
			
			//区画番号
			selectDto.setParkingBlock(selectParkingBlock);
			//社宅と一括契約に設定
			//リスト設定
			skf3010Sc007SharedService.getDoropDownList(Skf3010Sc007CommonSharedService.CONTRACT_TYPE_1, parkingContractTypeList, "", parkinglendKbnList);
			//駐車場契約情報のすべての入力項目を非活性、空白とする。
			//駐車場契約情報の「支援」「住所検索」ボタンを非活性にする。
			selectDto.setContractInfoDisabled(TRUE);
			
			restInput(selectDto);
		}
		
		if(BUTTON_DELETE.compareTo(selectDto.getSelectMode()) == 0){
			Long maxContractPropertyId = Long.parseLong(selectDto.getHdnBackupMaxContractPropertyId());
			if(selectContractPropertyId != maxContractPropertyId){
				//最大の契約番号でない場合、エラー終了
				ServiceHelper.addErrorResultMessage(selectDto, null, MessageIdConstant.E_SKF_3035);
				throwBusinessExceptionIfErrors(selectDto.getResultMessages());
			}
			//削除対象契約番号を設定
			deleteContractPropertyId = selectContractPropertyId;
			//契約番号を1減算する
			selectContractPropertyId = selectContractPropertyId - 1;
			selectDto.setHdnDelInfoFlg("1");
			LogUtils.debugByMsg("駐車場契約情報削除-契約番号：" + deleteContractPropertyId);
		}else if(BUTTON_DELETEADD.compareTo(selectDto.getSelectMode()) == 0 && selectContractPropertyId == 0){
			//チェック用契約番号
			selectDto.setHdnBackupContractPropertyId(selectContractPropertyId.toString());
			//区画番号
			selectDto.setParkingBlock(selectParkingBlock);
			//追加ボタン活性
			selectDto.setAddButtonDisabled(FALSE);
			selectDto.setContractInfoDisabled(TRUE);
			selectDto.setDeleteButtonDisabled(TRUE);
			selectDto.setRegistButtonDisabled(TRUE);
			selectDto.setCancelButtonDisabled(TRUE);
			selectDto.setContractListDisabled(TRUE);
			//リスト設定
			skf3010Sc007SharedService.getDoropDownList("", parkingContractTypeList, "", parkinglendKbnList);
			
			restInput(selectDto);
		}

		
		for(Map<String, Object> map : selectDto.getHdnListData()){
			//区画番号の一致確認
			if(selectParkingBlock.compareTo(map.get("parkingBlock").toString()) == 0 ){
				
				Long mapContractPropertyId = Long.parseLong(map.get("contractPropertyId").toString());
				//契約番号リスト
				if(mapContractPropertyId > 0){
					if(deleteContractPropertyId.intValue() == mapContractPropertyId.intValue()){
						//削除対象と一致の場合削除フラグを「1」に設定
						map.put("deleteFlag", "1");
					}else{
						if(map.get("deleteFlag").toString().compareTo("1") == 0){
							if(selectDto.getSelectMode().compareTo(BUTTON_LIST) == 0){
								map.put("deleteFlag", "0");
								//削除フラグ無効化して表示
								selectDto.setHdnDelInfoFlg("0");
							}else{
								//削除フラグ「1」のデータは無視
								continue;
							}
						}
						//0以上でリスト(仮)に追加
						propertyIdList.add(mapContractPropertyId);
					}
					
				}
				
				//契約番号の一致確認
				if(selectContractPropertyId == mapContractPropertyId || 
						(selectContractPropertyId == 0 && deleteContractPropertyId > 0)){
					LogUtils.debugByMsg("駐車場契約情報-一致：" + selectParkingBlock +  ",契約番号:"+ selectContractPropertyId);
					//チェック用契約番号
					selectDto.setHdnBackupContractPropertyId(selectContractPropertyId.toString());
					//区画番号
					selectDto.setParkingBlock(selectParkingBlock);
					//追加ボタン活性
					selectDto.setAddButtonDisabled(FALSE);
					
					//区画番号、契約番号が一致するものを表示
					if(selectContractPropertyId == 0){
						//契約情報無しの場合(仮の契約番号0の場合)
						// 「契約番号」「契約形態」リスト、削除、登録、キャンセルボタンを非活性にする
						selectDto.setContractInfoDisabled(TRUE);
						selectDto.setDeleteButtonDisabled(TRUE);
						selectDto.setRegistButtonDisabled(TRUE);
						selectDto.setCancelButtonDisabled(TRUE);
						selectDto.setContractListDisabled(TRUE);
						restInput(selectDto);
						//リスト設定
						skf3010Sc007SharedService.getDoropDownList("", parkingContractTypeList, "", parkinglendKbnList);
						//ループ終了
						break;
					}
					//契約情報有
					// 「契約番号」「契約形態」リスト、削除、登録、キャンセルボタンを活性にする
					selectDto.setContractListDisabled(FALSE);
					selectDto.setContractListDisabled(FALSE);
					selectDto.setDeleteButtonDisabled(FALSE);
					selectDto.setRegistButtonDisabled(FALSE);
					selectDto.setCancelButtonDisabled(FALSE);
					
					
					//契約形態
					String contractType = map.get("parkingContractType").toString();
					if(contractType.compareTo(Skf3010Sc007CommonSharedService.CONTRACT_TYPE_1) == 0){
						//社宅と一括契約の場合
						//リスト設定
						skf3010Sc007SharedService.getDoropDownList(Skf3010Sc007CommonSharedService.CONTRACT_TYPE_1, parkingContractTypeList, "", parkinglendKbnList);
						//駐車場契約情報のすべての入力項目を非活性、空白とする。
						//駐車場契約情報の「支援」「住所検索」ボタンを非活性にする。
						selectDto.setContractInfoDisabled(TRUE);
						selectDto.setHdnBackupParkingContractType(Skf3010Sc007CommonSharedService.CONTRACT_TYPE_1);
						selectDto.setHdnBackupParkinglendKbn(CodeConstant.DOUBLE_QUOTATION);
					}else{
						//社宅と別契約の場合
						//リスト設定
						String lendKbn = map.get("parkinglendKbn").toString();
						skf3010Sc007SharedService.getDoropDownList(Skf3010Sc007CommonSharedService.CONTRACT_TYPE_2, parkingContractTypeList, lendKbn, parkinglendKbnList);
						//駐車場契約情報のすべての入力項目を活性とする。
						//駐車場契約情報の「支援」「住所検索」ボタンを活性にする。
						selectDto.setContractInfoDisabled(FALSE);
						selectDto.setHdnBackupParkingContractType(Skf3010Sc007CommonSharedService.CONTRACT_TYPE_2);
						selectDto.setHdnBackupParkinglendKbn(lendKbn);
					}
					
					//入力項目設定(一括は空のはずだが、取得値を入れる)
					String ownerName = skf3010Sc007SharedService.createObjToString(map.get("ownerName"));
					String ownerNo = skf3010Sc007SharedService.createObjToString(map.get("ownerNo"));
					String parkingZipCd = skf3010Sc007SharedService.createObjToString(map.get("parkingZipCd"));
					String parkingAddress = skf3010Sc007SharedService.createObjToString(map.get("parkingAddress"));
					String parkingName = skf3010Sc007SharedService.createObjToString(map.get("parkingName"));
					String assetRegisterNo = skf3010Sc007SharedService.createObjToString(map.get("assetRegisterNo"));
					String contractStartDate = skf3010Sc007SharedService.createObjToString(map.get("contractStartDate"));
					String contractEndDate = skf3010Sc007SharedService.createObjToString(map.get("contractEndDate"));
					String landRent = skf3010Sc007SharedService.createObjToString(map.get("landRent"));
					String biko = skf3010Sc007SharedService.createObjToString(map.get("biko"));
					selectDto.setOwnerName(ownerName);
					selectDto.setOwnerNo(ownerNo);
					selectDto.setParkingZipCd(parkingZipCd);
					selectDto.setParkingAddress(parkingAddress);
					selectDto.setParkingName(parkingName);
					selectDto.setAssetRegisterNo(assetRegisterNo);
					selectDto.setContractStartDate(contractStartDate);
					selectDto.setContractEndDate(contractEndDate);
					selectDto.setLandRent(landRent);
					selectDto.setBiko(biko);
					
					//入力チェック用
					selectDto.setHdnBackupOwnerName(ownerName);
					selectDto.setHdnBackupOwnerNo(ownerNo);
					selectDto.setHdnBackupParkingZipCd(parkingZipCd);
					selectDto.setHdnBackupParkingAddress(parkingAddress);
					selectDto.setHdnBackupParkingName(parkingName);
					selectDto.setHdnBackupAssetRegisterNo(assetRegisterNo);
					selectDto.setHdnBackupContractStartDate(contractStartDate);
					selectDto.setHdnBackupContractEndDate(contractEndDate);
					selectDto.setHdnBackupLandRent(landRent);
					selectDto.setHdnBackupBiko(biko);
					
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
				for(Map<String, Object> map : selectDto.getHdnListData()){
					//区画番号の一致かつ削除フラグが1以外か確認
					if(selectParkingBlock.compareTo(map.get("parkingBlock").toString()) == 0 
						&& (map.get("deleteFlag").toString().compareTo("1") != 0)){
						
						Long mapContractPropertyId = Long.parseLong(map.get("contractPropertyId").toString());
						if(mapContractPropertyId == id){
							//契約開始日を取得
							contractStartDate = map.get("contractStartDate").toString();
						}
					}
				}
				//表示内容設定(契約番号：契約開始日)
				forListMap.put("label", id.toString() + "：" + contractStartDate);
				
				if (id.equals(selectContractPropertyId)) {
					//選択契約番号を選択中にする
					forListMap.put("selected", true);
				}
				contractPropertyIdList.add(forListMap);
			}
		}
		
		selectDto.setHdnBackupMaxContractPropertyId(String.valueOf(propertyIdList.size()));
		if(selectDto.getSelectMode().compareTo(BUTTON_LIST) == 0){
			//リスト選択から遷移時、削除情報は無し（破棄）
			selectDto.setHdnDelInfoFlg("0");
			//登録済みの契約番号最大値を設定
			selectDto.setHdnRegistContractPropertyId(String.valueOf(propertyIdList.size()));
		}
		
		//各リスト
		selectDto.setParkingContractTypeList(parkingContractTypeList);
		selectDto.setParkinglendKbnList(parkinglendKbnList);
		selectDto.setContractPropertyIdList(contractPropertyIdList);
		selectDto.setHdnListData(selectDto.getHdnListData());
		selectDto.setHdnDelInfoFlg(selectDto.getHdnDelInfoFlg());
		//エラー変数初期化
		selectDto.setOwnerNameError(CodeConstant.DOUBLE_QUOTATION);
		selectDto.setParkingZipCdError(CodeConstant.DOUBLE_QUOTATION);
		selectDto.setParkingAddressError(CodeConstant.DOUBLE_QUOTATION);
		selectDto.setParkingNameError(CodeConstant.DOUBLE_QUOTATION);
		selectDto.setAssetRegisterNoError(CodeConstant.DOUBLE_QUOTATION);
		selectDto.setContractStartDateError(CodeConstant.DOUBLE_QUOTATION);
		selectDto.setContractEndDateError(CodeConstant.DOUBLE_QUOTATION);
		selectDto.setLandRentError(CodeConstant.DOUBLE_QUOTATION);
		selectDto.setParkingContractTypeError(CodeConstant.DOUBLE_QUOTATION);
		selectDto.setContractPropertyIdError(CodeConstant.DOUBLE_QUOTATION);
		
		return selectDto;
	}
	
	/**
	 * DTO値初期化
	 * @param selectDto
	 */
	private void restInput(Skf3010Sc007SelectListDto selectDto){
		//入力項目初期化
		selectDto.setOwnerName(CodeConstant.DOUBLE_QUOTATION);
		selectDto.setOwnerNo(CodeConstant.DOUBLE_QUOTATION);
		selectDto.setParkingZipCd(CodeConstant.DOUBLE_QUOTATION);
		selectDto.setParkingAddress(CodeConstant.DOUBLE_QUOTATION);
		selectDto.setParkingName(CodeConstant.DOUBLE_QUOTATION);
		selectDto.setAssetRegisterNo(CodeConstant.DOUBLE_QUOTATION);
		selectDto.setContractStartDate(CodeConstant.DOUBLE_QUOTATION);
		selectDto.setContractEndDate(CodeConstant.DOUBLE_QUOTATION);
		selectDto.setLandRent(CodeConstant.DOUBLE_QUOTATION);
		selectDto.setBiko(CodeConstant.DOUBLE_QUOTATION);
		//入力チェック用初期化
		selectDto.setHdnBackupParkingContractType(CodeConstant.DOUBLE_QUOTATION);
		selectDto.setHdnBackupOwnerName(CodeConstant.DOUBLE_QUOTATION);
		selectDto.setHdnBackupOwnerNo(CodeConstant.DOUBLE_QUOTATION);
		selectDto.setHdnBackupParkingZipCd(CodeConstant.DOUBLE_QUOTATION);
		selectDto.setHdnBackupParkingAddress(CodeConstant.DOUBLE_QUOTATION);
		selectDto.setHdnBackupParkingName(CodeConstant.DOUBLE_QUOTATION);
		selectDto.setHdnBackupAssetRegisterNo(CodeConstant.DOUBLE_QUOTATION);
		selectDto.setHdnBackupParkinglendKbn(CodeConstant.DOUBLE_QUOTATION);
		selectDto.setHdnBackupContractStartDate(CodeConstant.DOUBLE_QUOTATION);
		selectDto.setHdnBackupContractEndDate(CodeConstant.DOUBLE_QUOTATION);
		selectDto.setHdnBackupLandRent(CodeConstant.DOUBLE_QUOTATION);
		selectDto.setHdnBackupBiko(CodeConstant.DOUBLE_QUOTATION);
	}
}
