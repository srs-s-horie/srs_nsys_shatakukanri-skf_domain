/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc002common;

import java.util.*;

import jp.co.c_nexco.skf.skf3010.domain.dto.common.Skf301010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3010Sc002CommonDto 保有社宅登録内共通DTO(同期用)
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3010Sc002CommonDto extends Skf301010CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;
	/** 定数 */
	// 契約情報変更モード：追加
	public static final String CONTRACT_MODE_ADD = "add";
	// 契約情報変更モード：変更
	public static final String CONTRACT_MODE_CHANGE = "change";
	// 契約情報変更モード：削除
	public static final String CONTRACT_MODE_DEL = "del";
	// 契約情報変更モード：初期表示
	public static final String CONTRACT_MODE_INIT = "init";
	// 選択タブインデックス(基本情報タブ)
	public static final String SELECT_TAB_INDEX_KIHON = "0";
	// 選択タブインデックス(駐車場情報タブ)
	public static final String SELECT_TAB_INDEX_PARKING = "1";
	// 選択タブインデックス(管理者情報タブ)
	public static final String SELECT_TAB_INDEX_ADMIN = "3";
	// 選択タブインデックス(契約情報タブ)
	public static final String SELECT_TAB_INDEX_CONTRACT = "4";
	// 管理者区分：寮長・自治会長
	public static final String MANAGE_KBN_DOMITRY_LEADER = "1";
	// 管理者区分：鍵管理者
	public static final String MANAGE_KBN_KEY_MANAGER = "2";
	// 管理者区分：寮母・管理会社
	public static final String MANAGE_KBN_MATRON = "3";

	/** 画面上部 */
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
//	private String shatakuAddress;
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
	/** 寮長・自治会長 */
	// 部屋番号：寮長・自治会長
	private String dormitoryLeaderRoomNo;
	// 氏名：寮長・自治会長
	private String dormitoryLeaderName;
	// 電子メールアドレス：寮長・自治会長
	private String dormitoryLeaderMailAddress;
	// 電話番号：寮長・自治会長
	private String dormitoryLeaderTelNumber;
	// 内線番号：寮長・自治会長
	private String dormitoryLeaderExtentionNo;
	// 備考：寮長・自治会長
	private String dormitoryLeaderBiko;
	// 更新日時(排他用)：寮長・自治会長
	private Date dormitoryLeaderUpdateDate;
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
	/** 寮母・管理会社 */
	// 部屋番号：寮母・管理会社
	private String matronRoomNo;
	// 氏名：寮母・管理会社
	private String matronName;
	// 電子メールアドレス：寮母・管理会社
	private String matronMailAddress;
	// 電話番号：寮母・管理会社
	private String matronTelNumber;
	// 内線番号：寮母・管理会社
	private String matronExtentionNo;
	// 備考：寮母・管理会社
	private String matronBiko;
	// 更新日時(排他用)：寮母・管理会社
	private Date matronUpdateDate;
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
	// 契約情報変更モード
	private String selectMode;

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
	// 駐車場契約情報ボタン(非活性：true, 活性:false)
	private Boolean parkingContractDisableFlg;

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
	// 寮長・自治会長 メールアドレス
	private String dormitoryLeaderMailAddressErr;
	// 鍵管理者 メールアドレス
	private String keyManagerMailAddressErr;
	// 寮母・管理会社 メールアドレス
	private String matronMailAddressErr;
	// 寮長・自治会長 電話番号
	private String dormitoryLeaderTelNumberErr;
	// 鍵管理者 電話番号
	private String keyManagerTelNumberErr;
	// 寮母・管理会社 電話番号
	private String matronTelNumberErr;
	// 寮長・自治会長 内線番号
	private String dormitoryLeaderExtentionNoErr;
	// 鍵管理者 内線番号
	private String keyManagerExtentionNoErr;
	// 寮母・管理会社 内線番号
	private String matronExtentionNoErr;
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
}
