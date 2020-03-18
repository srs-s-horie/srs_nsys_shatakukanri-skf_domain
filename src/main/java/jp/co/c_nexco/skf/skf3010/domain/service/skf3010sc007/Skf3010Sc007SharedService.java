/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc007;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc007.Skf3010Sc007GetParkingContractInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc007.Skf3010Sc007GetParkingContractInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc007.Skf3010Sc007GetParkingContractInfoExpRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;

/**
 * Skf3010Sc007SharedService 駐車場契約情報登録内共通クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3010Sc007SharedService {

	@Autowired
	private SkfDropDownUtils ddlUtils;
	@Autowired
	private Skf3010Sc007GetParkingContractInfoExpRepository skf3010Sc007GetParkingContractInfoExpRepository;
	
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;

	/**
	 * ドロップダウンリストに設定するリストを取得する。<br>
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param originalAuse 本来用途
	 * @param auseList 本来用途リスト
	 * @param lendKbn 貸与区分
	 * @param lendList 貸与区分リスト
	 */
	public void getDoropDownList(String contractType, List<Map<String, Object>> contractTypeList, String lendKbn,
			List<Map<String, Object>> lendList) {

		boolean isFirstRowEmpty = true;

		// 契約形態リスト
		contractTypeList.clear();
		contractTypeList.addAll(ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_PARKING_CONTRACTTYPE_KBN, contractType,
				false));

		// 貸与区分リスト
		lendList.clear();
		lendList.addAll(ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_LEND_KBN, lendKbn,
				isFirstRowEmpty));

	}

	/**
	 * リストテーブルを取得する。 <br>
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param shatakuKanriNo 社宅管理番号
	 * @param listTableData リストテーブル用データ
	 * @param hdnListData 情報保持用
	 * @return 取得データのレコードカウント
	 */
	public int getListTableData(Long shatakuKanriNo, List<Map<String, Object>> listTableData
		,List<Map<String, Object>> hdnListData) {

		LogUtils.debugByMsg("リストテーブルデータ取得処理開始");
		LogUtils.debugByMsg("引数から取得した値：" + "社宅管理番号：" + shatakuKanriNo);


		// リストテーブルに格納するデータを取得する
		int resultCount = 0;
		List<Skf3010Sc007GetParkingContractInfoExp> resultListTableData = new ArrayList<Skf3010Sc007GetParkingContractInfoExp>();
		Skf3010Sc007GetParkingContractInfoExpParameter param = new Skf3010Sc007GetParkingContractInfoExpParameter();
		param.setShatakuKanriNo(shatakuKanriNo);
		
		resultListTableData = skf3010Sc007GetParkingContractInfoExpRepository.getParkingContractInfo(param);

		// 取得レコード数を設定
		resultCount = resultListTableData.size();

		// 取得データレコード数判定
		if (resultCount <= 0) {
			// 取得データレコード数が0件場合、何もせず処理終了
			return resultCount;
		}

		//内部保持用リストに全データ格納
		hdnListData.clear();
		hdnListData.addAll(setHdnListData(resultListTableData));
				
		// リストテーブルに出力するリストを取得する
		listTableData.clear();
		listTableData.addAll(getListTableDataViewColumn(resultListTableData));

		return resultCount;

	}

	/**
	 * リストテーブルに出力するリストを取得する。
	 * 
	 * @param originList
	 * @return リストテーブルに出力するリスト
	 */
	private List<Map<String, Object>> getListTableDataViewColumn(List<Skf3010Sc007GetParkingContractInfoExp> originList) {

		List<Map<String, Object>> setViewList = new ArrayList<Map<String, Object>>();
		//表示済み区画番号リスト
		List<String> dispedBlockList =new ArrayList<String>();
		Boolean dispCheck=false;
		
		//契約形態コード取得
		Map<String, String> genericCodeMapContractType = new HashMap<String, String>();
		genericCodeMapContractType = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_PARKING_CONTRACTTYPE_KBN);
		
		for (int i = 0; i < originList.size(); i++) {
			String contractType = "";
			dispCheck=false;
			
			Skf3010Sc007GetParkingContractInfoExp tmpData = originList.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();

			for(String blockNo : dispedBlockList){
				//表示済み管理番号かチェック
				if(blockNo.compareTo(tmpData.getParkingKanriNo().toString()) == 0){
					//表示済みの場合、フラグTrue
					dispCheck=true;
					break;
				}
			}
			if(dispCheck){
				//フラグTrueの場合、次ループ
				continue;
			}
			//表示済みリストに区画番号を追加
			dispedBlockList.add(tmpData.getParkingKanriNo().toString());
			
			tmpMap.put("colParkingBlock", tmpData.getParkingBlock());
			// 「契約形態」の設定
			// 契約形態をコードから変更
			if (tmpData.getParkingContractType() != null) {
				contractType = genericCodeMapContractType.get(tmpData.getParkingContractType());
			}
			tmpMap.put("colParkingContractType", contractType);

			tmpMap.put("colContractPropertyId", tmpData.getContractPropertyId());

			//賃貸人（代理人）名前変換（取得）	
			tmpMap.put("colOwnerName", tmpData.getOwnerName());
			String colParkingAddress = CodeConstant.DOUBLE_QUOTATION;
			if(!Skf3010Sc007CommonSharedService.CONTRACT_TYPE_1.equals(tmpData.getParkingContractType())){
				colParkingAddress = tmpData.getParkingAddress();
			}
			tmpMap.put("colParkingAddress", colParkingAddress);
			tmpMap.put("colParkingName", tmpData.getParkingName());
			tmpMap.put("colAssetRegisterNo", tmpData.getAssetRegisterNo());
			
			//契約開始日、終了日
			tmpMap.put("colContractStartDate", skfDateFormatUtils.dateFormatFromString(tmpData.getContractStartDate(), "yyyy/MM/dd"));
			tmpMap.put("colContractEndDate", skfDateFormatUtils.dateFormatFromString(tmpData.getContractEndDate(), "yyyy/MM/dd"));
			
			//駐車場代変換
//			String landRent = CodeConstant.DOUBLE_QUOTATION;
//			createObjToMoneyString(tmpData.getLandRent());
//			if(tmpData.getLandRent() != null){
//				landRent = String.format("%,d", tmpData.getLandRent());
//			}
			tmpMap.put("colLandRent", createObjToMoneyString(tmpData.getLandRent()));
			
			tmpMap.put("colBiko", tmpData.getBiko());
			tmpMap.put("colParkingKanriNo", tmpData.getParkingKanriNo());

			setViewList.add(tmpMap);
		}

		return setViewList;
	}
	
	
	/**
	 * 内部保持用リストを取得する。
	 * 
	 * @param originList
	 * @return リスト
	 */
	private List<Map<String, Object>> setHdnListData(List<Skf3010Sc007GetParkingContractInfoExp> originList) {

		List<Map<String, Object>> setViewList = new ArrayList<Map<String, Object>>();
		SimpleDateFormat dateFormat = new SimpleDateFormat(Skf3010Sc007CommonSharedService.DATE_FORMAT);
		
		for (int i = 0; i < originList.size(); i++) {
			Skf3010Sc007GetParkingContractInfoExp tmpData = originList.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();

			
			tmpMap.put("parkingBlock", tmpData.getParkingBlock());
			tmpMap.put("parkingContractType", tmpData.getParkingContractType());
			if(tmpData.getContractPropertyId() == null){
				//契約番号なし（契約情報無しの場合、0を設定しておく
				tmpMap.put("contractPropertyId", 0);
			}else{
				tmpMap.put("contractPropertyId", tmpData.getContractPropertyId());
			}
			
			tmpMap.put("ownerName", tmpData.getOwnerName());
			tmpMap.put("ownerNo", tmpData.getOwnerNo());
			tmpMap.put("parkingZipCd",tmpData.getParkingZipCd());
			tmpMap.put("parkingAddress", tmpData.getParkingAddress());
			tmpMap.put("parkingName", tmpData.getParkingName());
			tmpMap.put("assetRegisterNo", tmpData.getAssetRegisterNo());
			tmpMap.put("parkinglendKbn", tmpData.getParkinglendKbn());
			tmpMap.put("contractStartDate", skfDateFormatUtils.dateFormatFromString(tmpData.getContractStartDate(), "yyyy/MM/dd"));
			tmpMap.put("contractEndDate", skfDateFormatUtils.dateFormatFromString(tmpData.getContractEndDate(), "yyyy/MM/dd"));
			tmpMap.put("landRent", tmpData.getLandRent());
			tmpMap.put("biko", tmpData.getBiko());
			tmpMap.put("parkingAddressKbn", tmpData.getParkingAddressKbn());
			
			String blockUpdateDate = CodeConstant.DOUBLE_QUOTATION;
			if(tmpData.getBlockUpdateDate() != null){
				blockUpdateDate = dateFormat.format(tmpData.getBlockUpdateDate());
			}
			tmpMap.put("blockUpdateDate", blockUpdateDate);
			String contractUpdateDate = CodeConstant.DOUBLE_QUOTATION;
			if(tmpData.getContractUpdateDate() != null){
				contractUpdateDate = dateFormat.format(tmpData.getContractUpdateDate());
			}
			tmpMap.put("contractUpdateDate", contractUpdateDate);
			tmpMap.put("parkingKanriNo", tmpData.getParkingKanriNo());
			tmpMap.put("deleteFlag", "0");
			
			setViewList.add(tmpMap);
		}

		return setViewList;
	}
	
	
	/**
	 * objctをStringに変換する（NULLの場合は空文字を返却する）
	 * @param obj
	 * @return
	 */
	public String createObjToString(Object obj){
		String resultTxt = CodeConstant.DOUBLE_QUOTATION;
		
		if(obj != null){
			resultTxt = obj.toString();
		}
		
		return resultTxt;
	}
	
	/**
	 * objctを金額形式Stringに変換する（NULLの場合は空文字を返却する）
	 * @param obj
	 * @return
	 */
	public String createObjToMoneyString(Object obj){
		String resultTxt = CodeConstant.DOUBLE_QUOTATION;
		
		if(obj != null){
			resultTxt = String.format("%,d", obj);
		}
		
		return resultTxt;
	}
	
}
