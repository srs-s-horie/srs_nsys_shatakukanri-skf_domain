/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc006;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc006common.Skf3010Sc006CommonDto;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc006.Skf3010Sc006ChangeParkingContractListDto;
import jp.co.intra_mart.common.platform.log.Logger;

/**
 * Skf3010Sc006ChangeParkingContractListService 借上社宅登録の駐車場契約情報イベントサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3010Sc006ChangeParkingContractListService extends BaseServiceAbstract<Skf3010Sc006ChangeParkingContractListDto> {

	@Autowired
	private Skf3010Sc006SharedService skf3010Sc006SharedService;
	@Autowired
	private SkfDropDownUtils ddlUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	/** ロガー。 */
	private static Logger logger = LogUtils.getLogger(SkfFileOutputUtils.class);

	//条件
	private final static String TRUE = "true";
	private final static String FALSE = "false";
	//社宅と一括契約
	public static final String CONTRACT_TYPE_1 = "1";
	//社宅と別契約
	public static final String CONTRACT_TYPE_2 = "2";
	// 日付フォーマット
	public static final String DATE_FORMAT = "yyyyMMdd HH:mm:ss.SSS";
	
	/**
	 * 保有社宅登録の契約情報追加ボタン押下時処理を行う。　
	 * 
	 * @param initDto	インプットDTO
	 * @return 処理結果
	 * @throws Exception	例外
	 */
	@Override
	public Skf3010Sc006ChangeParkingContractListDto index(Skf3010Sc006ChangeParkingContractListDto initDto) throws Exception {
		// デバッグログ
		logger.debug("駐車場契約情報:" + initDto.getParkingSelectMode());
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("駐車場契約情報", CodeConstant.C001, initDto.getPageId());

		/** DTO設定値 */
		//契約形態リスト
		List<Map<String, Object>> parkingContractTypeList = new ArrayList<Map<String, Object>>();
		String parkingContractType = CodeConstant.DOUBLE_QUOTATION;
		// 賃貸人（代理人）名
		String parkingOwnerName = CodeConstant.DOUBLE_QUOTATION;
		// 賃貸人（代理人）番号
		String parkingOwnerNo = CodeConstant.DOUBLE_QUOTATION;
		// 経理連携用管理番号
		String parkingAssetRegisterNo = CodeConstant.DOUBLE_QUOTATION;
		// 契約開始日
		String parkingContractStartDay = CodeConstant.DOUBLE_QUOTATION;
		// 契約終了日
		String parkingContractEndDay = CodeConstant.DOUBLE_QUOTATION;
		// 郵便番号
		String parkingZipCd = CodeConstant.DOUBLE_QUOTATION;
		// 住所
		String parkingContractAddress = CodeConstant.DOUBLE_QUOTATION;
		// 駐車場名
		String parkingName = CodeConstant.DOUBLE_QUOTATION;
		// 駐車場料(地代)
		String parkingLandRent = CodeConstant.DOUBLE_QUOTATION;
		// 備考
		String parkingContractBiko = CodeConstant.DOUBLE_QUOTATION;
		//更新日時
		Date updateDate = null;
		// 契約情報追加ボタン(非活性：true, 活性:false)
		Boolean contractAddDisableFlg = true;
		// 契約情報削除ボタン(非活性：true, 活性:false)
		Boolean contractDelDisableFlg = true;
		//契約形態
		String parkingContractTypeDisableFlg = TRUE;
		String parkingContractInfoDisabled = TRUE;
		//編集状態
		String parkingEditFlg = FALSE;
		
		// エラーコントロール初期化
		skf3010Sc006SharedService.clearVaridateErr(initDto);
		// 一旦画面を戻す
		skf3010Sc006SharedService.setBeforeInfo(initDto);

		// 契約情報変更モード
		String selectMode = initDto.getParkingSelectMode();
		// 選択タブインデックス
		String selectTabIndex = initDto.getHdnNowSelectTabIndex();
		// 選択契約番号
		String selectedContraceNo = initDto.getHdnChangeParkingContractSelectedIndex();
		
		// 契約情報リスト取得
		List<Map<String, Object>> contractList = new ArrayList<Map<String, Object>>();
		if (initDto.getContractInfoListTableData() != null) {
			contractList.addAll(initDto.getParkingContractInfoListTableData());
		}
		// 契約番号ドロップダウンリスト取得
		List<Map<String, Object>> contractNoList = new ArrayList<Map<String, Object>>();
		if (initDto.getContractNoList() != null) {
			contractNoList.addAll(initDto.getParkingContractNoList());
		}
			
		if (Skf3010Sc006CommonDto.CONTRACT_MODE_ADD_PARKING.equals(selectMode)){
			// 選択設定解除
			for (Map<String, Object> contractNoMap : contractNoList) {
				if (contractNoMap.containsKey("selected")) {
					contractNoMap.remove("selected");
					break;
				}
			}
			// 契約番号最大値取得
			int maxContractNo = 0;
			if (contractNoList.size() > 0) {
				maxContractNo = Integer.parseInt(contractNoList.get(contractNoList.size() - 1).get("value").toString());
			}
			// 契約番号インクリメント
			maxContractNo++;
			// 新規契約番号を契約番号リストに追加し選択状態に設定する
			Map<String, Object> contractMap = new HashMap<String, Object>();
			contractMap.put("value", Integer.toString(maxContractNo) + "M");
			contractMap.put("label", Integer.toString(maxContractNo) + " ： ");
			contractMap.put("selected", true);
			contractNoList.add(contractMap);
			selectedContraceNo = Integer.toString(maxContractNo) + "M";
			
			//契約形態(初期値：社宅と一括）
			parkingContractType = CONTRACT_TYPE_1;

			//契約形態リスト
			parkingContractTypeList.clear();
			parkingContractTypeList.addAll(ddlUtils.getGenericForDoropDownList(
					FunctionIdConstant.GENERIC_CODE_PARKING_CONTRACTTYPE_KBN, parkingContractType, true));

			// 追加ボタン非活性
			contractAddDisableFlg = true;
			// 削除ボタン活性
			contractDelDisableFlg = false;
			
			//編集中フラグON
			parkingEditFlg = TRUE;
			//契約形態編集可
			parkingContractTypeDisableFlg = FALSE;
			//項目編集不可
			initDto.setParkingContractInfoDisabled(TRUE);
		}else {
			Map<String, Object> contractMap = null;
			if(Skf3010Sc006CommonDto.CONTRACT_MODE_CHANGE_PARKING.equals(selectMode)){
			
				// 未登録契約番号取得
				int delContractIndex = -1;
				for (int i = 0; i < contractNoList.size(); i++) {
					Map<String, Object> contractNoMap = contractNoList.get(i);
					// 削除契約番号判定
					if (contractNoMap.get("value").toString().contains("M")) {
						// 削除インデックス取得
						delContractIndex = i;
						break;
					}
				}
				// 未登録契約番号削除
				if (delContractIndex != -1) {
					contractNoList.remove(delContractIndex);
				}
				// 選択契約情報
				contractMap = new HashMap<String, Object>();
				if (contractNoList.size() > 0) {
					// 削除ボタン活性
					contractDelDisableFlg = false;
					for (Map<String, Object> contractDataMap : contractList) {
						if (Objects.equals(contractDataMap.get("contractNo"), selectedContraceNo)) {
							contractMap = contractDataMap;
							break;
						}
					}
				}
	
			}else if(Skf3010Sc006CommonDto.CONTRACT_MODE_DEL_PARKING.equals(selectMode)){
				// 削除済契約番号
				String deletedConstractNo = CodeConstant.STRING_ZERO;
				if (initDto.getHdnDeleteParkingContractSelectedValue() != null
						&& initDto.getHdnDeleteParkingContractSelectedValue().length() > 0) {
					deletedConstractNo = initDto.getHdnDeleteParkingContractSelectedValue();
				}
				// 削除契約番号判定
				if (!CodeConstant.STRING_ZERO.equals(deletedConstractNo)) {
					// 削除契約番号インデックス
					int delContractIndex = -1;
					// 選択設定解除 & 削除契約情報取得
					for (int i = 0; i < contractNoList.size(); i++) {
						Map<String, Object> contractNoMap = contractNoList.get(i);
						// 選択状態判定
						if (contractNoMap.containsKey("selected")) {
							// 選択状態解除
							contractNoMap.remove("selected");
						}
						// 削除契約番号判定
						if (Objects.equals(deletedConstractNo, contractNoMap.get("value"))) {
							// 削除インデックス取得
							delContractIndex = i;
						}
					}
					// 契約番号リストから削除契約番号を削除
					if (delContractIndex != -1) {
						contractNoList.remove(delContractIndex);
					}
				}
				// 選択契約情報
				contractMap = new HashMap<String, Object>();
				if (contractNoList.size() > 0) {
					// 削除ボタン活性
					contractDelDisableFlg = false;
					// 契約情報リストから最大契約番号の契約情報を取得
					String maxContractNo = contractNoList.get(contractNoList.size() - 1).get("value").toString();
					for (Map<String, Object> contractDataMap : contractList) {
						if (Objects.equals(contractDataMap.get("contractNo"), maxContractNo)) {
							contractMap = contractDataMap;
							break;
						}
					}
					// 選択値を最大値に設定
					contractNoList.get(contractNoList.size() - 1).put("selected", "true");
					selectedContraceNo = maxContractNo;
				}
			}
			
			// 追加ボタン活性
			contractAddDisableFlg = false;
			
			if(contractMap != null && contractMap.size() > 0){
				//契約形態編集可
				parkingContractTypeDisableFlg = FALSE;
				
				//契約形態
				if (contractMap.get("parkingContractType") != null) {
					parkingContractType = contractMap.get("parkingContractType").toString();
				}

				
				if(CONTRACT_TYPE_2.equals(parkingContractType)){
					//入力可に設定する
					parkingContractInfoDisabled = FALSE;
					
					// 「賃貸人(代理人)氏名または名称」取得
					if (contractMap.get("parkingOwnerName") != null) {
						parkingOwnerName = contractMap.get("parkingOwnerName").toString();
					}
					// 「賃貸人(代理人)番号」取得
					if (contractMap.get("parkingOwnerNo") != null) {
						parkingOwnerNo = contractMap.get("parkingOwnerNo").toString();
					}
					// 「経理連携用管理番号」取得
					if (contractMap.get("parkingAssetRegisterNo") != null) {
						parkingAssetRegisterNo = contractMap.get("parkingAssetRegisterNo").toString();
					}
					// 「契約開始日」取得
					if (contractMap.get("parkingContractStartDay") != null) {
						parkingContractStartDay = contractMap.get("parkingContractStartDay").toString();
					}
					// 「契約終了日」取得
					if (contractMap.get("parkingContractEndDay") != null) {
						parkingContractEndDay = contractMap.get("parkingContractEndDay").toString();
					}
					// 「郵便番号」取得
					if (contractMap.get("parkingZipCd") != null) {
						parkingZipCd = contractMap.get("parkingZipCd").toString();
					}
					// 「住所」取得
					if (contractMap.get("parkingContractAddress") != null) {
						parkingContractAddress = contractMap.get("parkingContractAddress").toString();
					}
					// 「駐車場名」取得
					if (contractMap.get("parkingName") != null) {
						parkingName = contractMap.get("parkingName").toString();
					}
					// 「駐車場料(地代)」取得
					if (contractMap.get("parkingLandRent") != null) {
						parkingLandRent = contractMap.get("parkingLandRent").toString();
					}
					// 「備考」取得
					if (contractMap.get("parkingContractBiko") != null) {
						parkingContractBiko = contractMap.get("parkingContractBiko").toString();
					}

				}else{
					//一括契約
					//入力不可に設定する
					parkingContractInfoDisabled = TRUE;
				}
				// 更新日時
				if (contractMap.get("updateDate") != null && contractMap.get("updateDate").toString().length() > 0) {
					SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
					updateDate = dateFormat.parse(contractMap.get("updateDate").toString());
				}
			}
			
			//契約形態リスト
			parkingContractTypeList.clear();
			parkingContractTypeList.addAll(ddlUtils.getGenericForDoropDownList(
					FunctionIdConstant.GENERIC_CODE_PARKING_CONTRACTTYPE_KBN, parkingContractType, true));
		}
		
		/** 契約情報設定 */
		initDto.setParkingContractNoList(contractNoList);
		initDto.setParkingContractType(parkingContractType);
		initDto.setParkingOwnerName(parkingOwnerName);
		initDto.setParkingOwnerNo(parkingOwnerNo);
		initDto.setParkingAssetRegisterNo(parkingAssetRegisterNo);
		initDto.setParkingContractStartDay(parkingContractStartDay);
		initDto.setParkingContractEndDay(parkingContractEndDay);
		initDto.setParkingZipCd(parkingZipCd);
		initDto.setParkingContractAddress(parkingContractAddress);
		initDto.setParkingName(parkingName);
		initDto.setParkingLandRent(parkingLandRent);
		initDto.setParkingContractBiko(parkingContractBiko);
		initDto.setParkingContractAddDisableFlg(contractAddDisableFlg);
		initDto.setParkingContractDelDisableFlg(contractDelDisableFlg);
		initDto.setParkingContractTypeDisabled(parkingContractTypeDisableFlg);
		initDto.setParkingContractTypeList(parkingContractTypeList);
		initDto.setParkingContractInfoDisabled(parkingContractInfoDisabled);
		initDto.setParkingEditFlg(parkingEditFlg);
		initDto.setParkingContractUpdateDate(updateDate);
		initDto.setHdnDispParkingContractSelectedIndex(selectedContraceNo);
		
		/** 契約情報編集チェック変数 **/
		initDto.setStartingParkingContractType(parkingContractType);
		initDto.setStartingParkingContractOwnerName(parkingOwnerName);
		initDto.setStartingParkingAssetRegisterNo(parkingAssetRegisterNo);
		initDto.setStartingParkingContractStartDay(parkingContractStartDay);
		initDto.setStartingParkingContractEndDay(parkingContractEndDay);
		initDto.setStartingParkingZipCd(parkingZipCd);
		initDto.setStartingParkingContractAddress(parkingContractAddress);
		initDto.setStartingParkingName(parkingName);
		initDto.setStartingParkingContractLandRent(parkingLandRent);
		initDto.setStartingParkingContractBiko(parkingContractBiko);
		
		// 選択タブインデックス(契約情報タブ)
		initDto.setHdnNowSelectTabIndex(selectTabIndex);


		return initDto;
	}
}
