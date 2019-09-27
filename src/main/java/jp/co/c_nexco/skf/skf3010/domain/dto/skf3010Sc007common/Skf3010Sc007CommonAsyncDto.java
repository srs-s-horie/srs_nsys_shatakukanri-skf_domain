/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc007common;

import java.util.List;
import java.util.Map;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3010Sc007CommonAsyncDto Skf3010Sc007非同期処理共通Dto
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3010Sc007CommonAsyncDto extends AsyncBaseDto {

	private static final long serialVersionUID = 1L;

	//データ保持用リスト
	private List<Map<String, Object>> hdnListData;
	private String hdnListDataJson;
	
	//区画番号
	private String parkingBlock;
	//契約番号
	private String contractPropertyId;
	
	
	//契約番号リスト
	private List<Map<String, Object>> contractPropertyIdList;
	//リスト用文字列
	private String contractPropertyIdListData;
	//契約形態リスト
	private List<Map<String, Object>> parkingContractTypeList;
	private String parkingContractType;
	//賃貸人（代理人）
	private String ownerName;
	private String ownerNo;
	//郵便番号
	private String parkingZipCd;
	//住所
	private String parkingAddress;
	//駐車場名
	private String parkingName;
	//経理連携用資産番号
	private String assetRegisterNo;
	//貸与区分
	private List<Map<String, Object>> parkinglendKbnList;
	private String parkinglendKbn;
	//契約開始日
	private String contractStartDate;
	//契約終了日
	private String contractEndDate;
	//駐車場料（地代） 
	private String landRent;
	//備考
	private String biko;
	//駐車場管理番号
	private String parkingKanriNo;
	
	//入力制御
	private String contractInfoDisabled;
	private String addButtonDisabled;
	private String deleteButtonDisabled;
	private String registButtonDisabled;
	private String cancelButtonDisabled;
	private String contractListDisabled;
	
	//入力チェック用
	//契約番号
	private String hdnBackupContractPropertyId;
	//契約番号最大値
	private String hdnBackupMaxContractPropertyId;
	//契約形態
	private String hdnBackupParkingContractType;
	//賃貸人（代理人）
	private String hdnBackupOwnerName;
	//賃貸人（代理人）番号
	private String hdnBackupOwnerNo;
	//郵便番号
	private String hdnBackupParkingZipCd;
	//住所
	private String hdnBackupParkingAddress;
	//駐車場名
	private String hdnBackupParkingName;
	//経理連携用資産番号
	private String hdnBackupAssetRegisterNo;
	//貸与区分
	private String hdnBackupParkinglendKbn;
	//契約開始日
	private String hdnBackupContractStartDate;
	//契約終了日
	private String hdnBackupContractEndDate;
	//駐車場料（地代） 
	private String hdnBackupLandRent;
	//備考
	private String hdnBackupBiko;
	
	private String checkResult;
	
	//動作制御
	private String selectMode;
	//削除情報有無
	private String hdnDelInfoFlg;

}
