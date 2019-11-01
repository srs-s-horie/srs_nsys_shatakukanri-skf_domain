/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc006common;

import java.util.*;

import org.springframework.web.multipart.MultipartFile;

import jp.co.c_nexco.skf.skf3010.domain.dto.common.Skf301010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3010Sc006CommonDto 借上社宅登録内共通DTO
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3010Sc006CommonDto extends Skf301010CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;

	//社宅登録共通
	/** 画面上部 */
	// 社宅名
	private String shatakuName;
	// 空き部屋数
	private String emptyRoomCount;
	// 空き駐車場数
	private String emptyParkingCount;
	// 一棟借上フラグ(trueの場合は一棟借上)
	private Boolean ittoFlg;
	// 社宅区分(一棟借上フラグがfalseの場合に参照)
	private String shatakuKbn;

	/** テキストボックス */
	/** 基本情報 */
	// 郵便番号
	private String zipCd;
	// 都道府県コード
	private String pref;
	// 社宅住所(継承元にある)
//		private String shatakuAddress;
	// 社宅構造詳細
	private String shatakuStructureDetail;
	// 建築年月日
	private String buildDate;
	// 実年数
	private String jituAge;
	// 次回算定年月日
	private String nextCalcDate;
	// 経年
	private String aging;
	// 社宅補足ファイル名1
	private String shatakuHosokuFileName1;
	// 社宅補足ファイル名2
	private String shatakuHosokuFileName2;
	// 社宅補足ファイル名3
	private String shatakuHosokuFileName3;
	// 社宅補足リンク名1
	private String shatakuHosokuLink1;
	// 社宅補足リンク名2
	private String shatakuHosokuLink2;
	// 社宅補足リンク名3
	private String shatakuHosokuLink3;
	// 社宅補足サイズ1
	private String shatakuHosokuSize1;
	// 社宅補足サイズ2
	private String shatakuHosokuSize2;
	// 社宅補足サイズ3
	private String shatakuHosokuSize3;
	// 社宅補足ファイル1
	private byte[] shatakuHosokuFile1;
	// 社宅補足ファイル2
	private byte[] shatakuHosokuFile2;
	// 社宅補足ファイル3
	private byte[] shatakuHosokuFile3;
	// 備考
	private String biko;
	// 基本情報更新日時(排他用)
	private Date kihonUpdateDate;

	/** 駐車場情報 */
	// 駐車場基本使用料
	private String parkingRent;
	// 駐車場台数
	private String parkingBlockCount;
	// 貸与可能総数
	private String lendPossibleCount;
	// 駐車場補足ファイル名1
	private String parkingHosokuFileName1;
	// 駐車場補足ファイル名2
	private String parkingHosokuFileName2;
	// 駐車場補足ファイル名3
	private String parkingHosokuFileName3;
	// 駐車場補足リンク1
	private String parkingHosokuLink1;
	// 駐車場補足リンク2
	private String parkingHosokuLink2;
	// 駐車場補足リンク3
	private String parkingHosokuLink3;
	// 駐車場補足サイズ1
	private String parkingHosokuSize1;
	// 駐車場補足サイズ2
	private String parkingHosokuSize2;
	// 駐車場補足サイズ3
	private String parkingHosokuSize3;
	// 駐車場補足ファイル1
	private byte[] parkingHosokuFile1;
	// 駐車場補足ファイル2
	private byte[] parkingHosokuFile2;
	// 駐車場補足ファイル3
	private byte[] parkingHosokuFile3;
	// 駐車場備考
	private String parkingBiko;
	// 駐車場情報更新日時(排他用)
	private Date parkingUpdateDate;
	/** 管理者情報 */
	/** 鍵管理者 */
	// 部屋番号 ：鍵管理者
	private String keyManagerRoomNo;
	// 氏名 ：鍵管理者
	private String keyManagerName;
	// 電子メールアドレス ：鍵管理者
	private String keyManagerMailAddress;
	// 電話番号 ：鍵管理者
	private String keyManagerTelNumber;
	// 内線番号 ：鍵管理者
	private String keyManagerExtentionNo;
	// 備考 ：鍵管理者
	private String keyManagerBiko;
	// 更新日時(排他用)：鍵管理者
	private Date keyManagerUpdateDate;

	/** 契約情報 */
	// 賃貸人（代理人）名
	private String contractOwnerName;
	// 賃貸人（代理人）番号
	private String contractOwnerNo;
	// 経理連携用管理番号
	private String assetRegisterNo;
	// 契約開始日
	private String contractStartDay;
	// 契約終了日
	private String contractEndDay;
	// 家賃
	private String contractRent;
	// 共益費
	private String contractKyoekihi;
	// 駐車場料（地代）
	private String contractLandRent;
	// 備考
	private String contractBiko;
	// 契約情報更新日時(排他用)
	private Date contractUpdateDate;
	// 契約情報表示プルダウンインデックス
	private String hdnDispContractSelectedIndex;
	// 契約情報選択プルダウンインデックス
	private String hdnChangeContractSelectedIndex;
	// 契約情報削除プルダウンインデックス
	private String hdnDeleteContractSelectedValue;

	/** 前画面からの連携用 */
	// 対象行の社宅区分
	private String hdnRowShatakuKbn;
	// 対象行の社宅管理番号
	private String hdnRowShatakuKanriNo;
	// 対象行の社宅名
	private String hdnRowShatakuName;
	// 対象行の地域区分
	private String hdnRowAreaKbn;
	// 対象行の空き部屋数
	private String hdnRowEmptyRoomCount;
	// 対象行の空き駐車場数
	private String hdnRowEmptyParkingCount;
	// 選択タブインデックス
	private String hdnNowSelectTabIndex;



	/** 駐車場契約情報への連携用 */
	private String hdnShatakuKanriNo;
	private String hdnShatakuName;
	private String hdnAreaKbn;
	private String hdnShatakuKbn;
	private String hdnEmptyRoomCount;
	private String hdnEmptyParkingCount;

	/** ドロップダウンリスト */
	// 地域区分リスト
	private List<Map<String, Object>> areaKbnList;
	// 地域区分選択値コード
	private String areaKbnCd;
	// 管理事業領域リスト
	private List<Map<String, Object>> manageBusinessAreaList;
	// 都道府県リスト
	private List<Map<String, Object>> prefList;
	// 社宅構造リスト
	private List<Map<String, Object>> shatakuStructureList;
	// 社宅構造選択値コード
	private String structureKbnCd;
	// エレベーターリスト
	private List<Map<String, Object>> elevatorList;
	// 駐車場構造リスト
	private List<Map<String, Object>> parkingStructureList;
	// 契約番号リスト
	private List<Map<String, Object>> contractNoList;
	// 貸与区分選択値リスト文字列(追加ボタン押下用)
	private String lendKbnSelectListString;
	// デフォルト貸与状況(追加ボタン押下用)
	private String defaultParkingLendStatus;

	/** リストテーブル */
	// 駐車場情報リスト(画面表示時に使用するリスト)
	private List<Map<String, Object>> parkingInfoListTableData;
	// 駐車場情報リスト(初期表示時：DB取得時)
	private List<Map<String, Object>> hdnStartingParkingInfoListTableData;
	// 駐車場情報リスト(現在画面に表示されているリスト)
	private List<Map<String, Object>> nowParkingInfoListTableData;
	// 備品情報リスト
	private List<Map<String, Object>> bihinInfoListTableData;
	// 契約情報リスト
	private List<Map<String, Object>> contractInfoListTableData;
	// 非表示備品情報リスト
	private List<Map<String, Object>> hdnBihinInfoListTableData;
	// 社宅部屋情報リスト(排他処理用)
	private List<Map<String, Object>> shatakuRoomExlusiveCntrllList;
	// 社宅部屋備品情報リスト(排他処理用)
	private List<Map<String, Object>> roomeBihinExlusiveCntrllList;

	/** フラグ */
	// 新規保有社宅フラグ(新規：true, 編集：false)
	private Boolean newShatakuFlg;
	// 契約情報追加ボタン(非活性：true, 活性:false)
	private Boolean contractAddDisableFlg;
	// 契約情報削除ボタン(非活性：true, 活性:false)
	private Boolean contractDelDisableFlg;
	
	/** JSON(連携用) */
	// JSON駐車場区画情報 リスト
	private String jsonParking;
	// JSON備品情報 リスト
	private String jsonBihin;
	// ドロップダウン選択値リスト
	private String jsonDrpDwnList;
	// 可変ラベルリスト
	private String jsonLabelList;

	/** データ比較用 */
	// 建築年月日
	private String startingBuildDate;
	// 社宅構造
	private String startingShatakuStructure;
	// 駐車場構造リスト
	private String startingParkingStructure;
	// 地域区分
	private String startingAreaKbn;

	/** エラー系 **/
	// 社宅名
	private String shatakuNameErr;
	// 地域区分
	private String areaKbnErr;
	// 社宅区分
	private String shatakuKbnErr;
	// 利用区分
	private String useKbnKbnErr;
	// 管理会社
	private String manageCompanyErr;
	// 管理機関
	private String manageAgencyErr;
	// 管理事業領域
	private String manageBusinessAreaErr;
	// 郵便番号
	private String zipCdErr;
	// 都道府県
	private String prefErr;
	// 社宅住所
	private String shatakuAddressErr;
	// 社宅構造
	private String shatakuStructureErr;
	// 建築年月日
	private String buildDateErr;
	// 駐車場構造
	private String parkingStructureErr;
	// 鍵管理者 メールアドレス
	private String keyManagerMailAddressErr;
	// 鍵管理者 電話番号
	private String keyManagerTelNumberErr;
	// 賃貸人（代理人）名
	private String contractOwnerNameErr;
	// 経理連携用管理番号
	private String assetRegisterNoErr;
	// 契約開始日
	private String contractStartDayErr;
	// 契約終了日
	private String contractEndDayErr;
	// 家賃
	private String contractRentErr;
	// 共益費
	private String contractKyoekihiErr;
	// 駐車場料（地代）
	private String contractLandRentErr;
	
	//借上社宅登録
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
	//駐車場契約物件ID
	private String parkingPropertyId;
	
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
	// 更新日時(排他用)
	private Date parkingContractUpdateDate;
	
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
	private String hdnHosoku;
	private String hdnAttachedNo;
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
