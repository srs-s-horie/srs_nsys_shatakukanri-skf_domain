/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc006common;

import java.util.*;

import org.springframework.web.multipart.MultipartFile;

import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc002common.Skf3010Sc002CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3010Sc006CommonDto 借上社宅登録内共通DTO
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3010Sc006CommonDto extends Skf3010Sc002CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
	
	/** 定数 */
	// 選択タブインデックス(基本情報タブ)
	public static final String SELECT_TAB_INDEX_KIHON = "0";
	// 選択タブインデックス(部屋情報タブ)
	public static final String SELECT_TAB_INDEX_ROOM = "1";
	// 選択タブインデックス(駐車場情報タブ)
	public static final String SELECT_TAB_INDEX_PARKING = "2";
	// 選択タブインデックス(備品情報タブ)
	public static final String SELECT_TAB_INDEX_BIHIN = "3";
	// 選択タブインデックス(管理者情報タブ)
	public static final String SELECT_TAB_INDEX_ADMIN = "4";
	// 選択タブインデックス(契約情報タブ)
	public static final String SELECT_TAB_INDEX_CONTRACT = "5";

	// 契約情報変更モード：追加
	public static final String CONTRACT_MODE_ADD_PARKING = "addParking";
	// 契約情報変更モード：変更
	public static final String CONTRACT_MODE_CHANGE_PARKING = "changeParking";
	// 契約情報変更モード：削除
	public static final String CONTRACT_MODE_DEL_PARKING = "delParking";
	
	/** 前画面からの連携用 */
	// 複写フラグ
	private String copyFlg;
	
	/** 社宅情報 */
	private String areaKbn;
	private String useKbn;
	private String manageCompany;
	private String manageAgency;
	private String manageBusinessArea;
	private String pref;
	private String shatakuStructure;
	private String elevator;
	
	/** 部屋情報 */
	//部屋管理番号
	private String shatakuRoomKanriNo;
	//　部屋番号
	private String roomNo;
	// 本来延面積
	private String originalMenseki;
	// 本来規格
	private String originalKikaku;
	// 本来規格（補助）
	private String originalKikakuHosoku;
	// 貸与延面積
	private String lendMenseki;
	// 本来用途
	private String originalAuse;
	//本来用途（補助）			
	private String originalAuseHosoku;
	//サンルーム面積
	private String sunRoomMenseki;
	//貸与区分
	private String lendKbn;
	//貸与区分（補助）
	private String lendKbnHosoku;
	//寒冷地減免区分事由区分
	private String coldExemptionKbn;
	//備考
	private String roomBiko;
	//階段面積
	private String stairsMenseki;
	//物置面積
	private String barnMenseki;
	//物置調整面積
	private String barnMensekiAdjust;	
	// 本来規格リスト
	private List<Map<String, Object>> originalKikakuList;
	// 本来用途リスト
	private List<Map<String, Object>> originalAuseList;
	// 貸与区分リスト
	private List<Map<String, Object>> lendKbnList;
	// 寒冷地減免事由区分リスト
	private List<Map<String, Object>> coldExemptionKbnList;
	//物置調整面積
	private String hdnBarnMensekiAdjust;
	//更新日時（排他用）
	private Date roomUpdateDate;
	
	/** 駐車場情報 */
	//駐車場管理番号
	private String parkingKanriNo;
	//駐車場所在地
	private String parkingAddress;
	//区画番号
	private String parkingBlock;
	//貸与区分
	private String parkingLendKbn;
	//使用者
	private String shiyosya;
	//駐車場調整金額
	private String parkingRentalAdjust;
	//駐車場月額使用料
	private String parkingShiyoMonthFei;
	//駐車場区分情報更新日時
	private Date blockUpdateDate;
	//駐車場構造
	private String parkingStructure;
	
	/** 管理者情報 */
	/** 管理会社 */
	// 会社名：管理会社
	private String manageCompanyName;
	// 担当者名：管理会社
	private String manageName;
	// 電子メールアドレス：管理会社
	private String manageMailAddress;
	// 電話番号：管理会社
	private String manageTelNumber;
	// 備考：管理会社
	private String manageBiko;
	// 更新日時(排他用)：管理会社
	private Date manageUpdateDate;
	/** 鍵管理者 */
	// 会社名 ：鍵管理者
	private String keyManagerCompanyName;
	
	/** 契約情報 （社宅）*/
	//契約番号
	private String contractNo;
	/** 契約情報 （駐車場）*/
	// 契約情報リスト
	private List<Map<String, Object>> parkingContractInfoListTableData;
	//契約番号
	private String parkingContractNo;
	//契約番号リスト
	private List<Map<String, Object>> parkingContractNoList;
	//リスト用文字列
	private String contractPropertyIdListData;
	//契約形態リスト
	private List<Map<String, Object>> parkingContractTypeList;
	private String parkingContractType;
	//賃貸人（代理人）
	private String parkingOwnerName;
	private String parkingOwnerNo;
	//郵便番号
	private String parkingZipCd;
	//住所
	private String parkingContractAddress;
	//駐車場名
	private String parkingName;
	//経理連携用管理番号
	private String parkingAssetRegisterNo;
	//契約開始日
	private String parkingContractStartDay;
	//契約終了日
	private String parkingContractEndDay;
	//駐車場料（地代） 
	private String parkingLandRent;
	//備考
	private String parkingContractBiko;
	// 駐車場契約情報追加ボタン(非活性：true, 活性:false)
	private Boolean parkingContractAddDisableFlg;
	// 駐車場契約情報削除ボタン(非活性：true, 活性:false)
	private Boolean parkingContractDelDisableFlg;
	//駐車場契約形態
	private String parkingContractTypeDisabled;
	//駐車場契約情報
	private String parkingContractInfoDisabled;
	//駐車場契約選択インデックス
	private String hdnDispParkingContractSelectedIndex;
	// 契約情報選択プルダウンインデックス
	private String hdnChangeParkingContractSelectedIndex;
	// 契約情報削除プルダウンインデックス
	private String hdnDeleteParkingContractSelectedValue;
	
	/**エラー**/
	// 部屋番号
	private String roomNoError;
	// 本来延面積
	private String originalMensekiError;
	// 貸与延面積
	private String lendMensekiError;
	// 本来用途
	private String originalAuseError;
	// 本来規格
	private String originalKikakuError;
	//貸与区分
	private String lendKbnError;
	//サンルーム延面積
	private String sunRoomMensekiError;
	//階段面積
	private String stairsMensekiError;
	//物置面積
	private String barnMensekiError;
	//貸与区分（補助）
	private String lendKbnHosokuError;
	//駐車場区画
	private String parkingBlockError;
	//駐車場調整金額
	private String parkingRentalAdjustError;
	//賃貸人
	private String parkingOwnerNameError;
	//郵便番号
	private String parkingZipCdError;
	//所在地
	private String parkingAddressError;
	//駐車場名
	private String parkingNameError;
	//経理連携用管理番号
	private String parkingAssetRegisterNoError;
	//契約開始日
	private String parkingContractStartDateError;
	//契約終了日
	private String parkingContractEndDateError;
	//駐車場料
	private String parkingLandRentError;
	//契約形態
	private String parkingContractTypeError;
	// 会社名：管理会社
	private String manageCompanyNameError;
	// 担当者名：管理会社
	private String manageNameError;
	// 電子メールアドレス：管理会社
	private String manageMailAddressError;
	// 電話番号：管理会社
	private String manageTelNumberError;
	// 備考：管理会社
	private String manageBikoError;

	
	//駐車場契約情報選択モード
	private String parkingSelectMode;
	//駐車場契約編集中フラグ
	private String parkingEditFlg;
	
	/** 添付ファイル **/
	//ファイル番号
	private String fileNo;
	//種別
	private String hosokuType;
	//ファイルボックス
	private MultipartFile tmpFileBoxshataku1;
	private MultipartFile tmpFileBoxshataku2;
	private MultipartFile tmpFileBoxshataku3;
	private MultipartFile tmpFileBoxparking1;
	private MultipartFile tmpFileBoxparking2;
	private MultipartFile tmpFileBoxparking3;
	
	/** 契約情報編集チェック変数 **/
	private String startingParkingContractType;
	private String startingParkingContractOwnerName;
	private String startingParkingAssetRegisterNo;
	private String startingParkingContractStartDay;
	private String startingParkingContractEndDay;
	private String startingParkingZipCd;
	private String startingParkingContractAddress;
	private String startingParkingName;
	private String startingParkingContractLandRent;
	private String startingParkingContractBiko;

}
