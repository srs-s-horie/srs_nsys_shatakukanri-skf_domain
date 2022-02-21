/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.dto.skf2100Sc008common;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import jp.co.c_nexco.nfw.webcore.domain.model.FileDownloadDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2100Sc008CommonDto モバイルルーターマスタ登録共通同期処理DTO
 *
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2100Sc008CommonDto extends FileDownloadDto {
	
	private static final long serialVersionUID = 3202882792085625462L;
	
	/** ドロップダウンリスト */
	/** 契約区分 */
	private String contractKbnSelect;
	/** 契約区分選択リスト */
	private List<Map<String, Object>> contractKbnDropDownList;
	

	/** 「通しNo」 */
	private String routerNo;
	/** 「電話番号」 */
	private String tel;
	/** 「ICCID」 */
	private String iccid;
	/** 「IMEI」 */
	private String imei;
	/** 「SSID A」 */
	private String ssidA;
	/** 「WPA Key」 */
	private String wpaKey;
	/** 「ルーター入荷日」 */
	private String arrivalDate;
	/** 「契約開始日」 */
	private String contractStartDate;
	/** 「契約終了日」 */
	private String contractEndDate;
	/** 「故障」 */
	private String faultFlag;
	/** 「備考」 */
	private String biko;
	
	
	// 補足ファイル名1
	private String hosokuFileName1;
	// 補足ファイル名2
	private String hosokuFileName2;
	// 補足ファイル名3
	private String hosokuFileName3;
	// 補足リンク名1
	private String hosokuLink1;
	// 補足リンク名2
	private String hosokuLink2;
	// 補足リンク名3
	private String hosokuLink3;
	// 補足サイズ1
	private String hosokuSize1;
	// 補足サイズ2
	private String hosokuSize2;
	// 補足サイズ3
	private String hosokuSize3;
	// 補足ファイル1
	private byte[] hosokuFile1;
	// 補足ファイル2
	private byte[] hosokuFile2;
	// 補足ファイル3
	private byte[] hosokuFile3;
	// 選択補足ファイルNo
	private String fileNo;
	// 添付ファイル番号
	private String hdnAttachedNo;
	//ファイルボックス
	private MultipartFile tmpFileBox1;
	private MultipartFile tmpFileBox2;
	private MultipartFile tmpFileBox3;
	
	/**
	 * hidden 項目
	 */
	/** 契約区分 */
	private String hdnContractKbn;
	/** 故障フラグ値 */
	private String hdnFaultFlag;
	/** 貸出可否判定 */
	private String hdnLendingJudgment;
	/** 新規登録 */
	private boolean newDataFlag;
	/** 「故障」チェックボックス選択状態 */
	private String hdnChkFaultSelect;
	/**
	 * 非活性制御
	 */
	private boolean editDisableFlg;
	private boolean btnRegistDisableFlg;
	private boolean btnDeleteDisableFlg;
	
	
	/** エラー*/
	private String routerNoErr;
	private String telErr;
	private String iccidErr;
	private String imeiErr;
	private String ssidAErr;
	private String wpaKeyErr;
	private String arrivalDateErr;
	private String contractStartDateErr;
	private String contractEndDateErr;
	private String contractKbnSelectErr;
	private String bikoErr;
	
	/** 検索パラメータ保持 */
	// private Skf2100Sc007GetListTableDataExpParameter searchParam;
	private String searchInfoRouterNo;
	private String searchInfoTel;
	private String searchInfoIccid;
	private String searchInfoImei;
	private String searchInfoContractKbn;
	private String searchInfoContractEndDateFrom;
	private String searchInfoContractEndDateTo;
	private String searchInfoFaultFlag;
	private String searchInfoArrivalDateFrom;
	private String searchInfoArrivalDateTo;

	
	/** 定数 */
	//番号なし(0番)
	public static final String NO_ROUTER_NO = "0";
	public static final String HOSOKU_LINK = "attached";
	public static final String ROUTER_KEY_LAST_UPDATE_DATE = "skf2100_m_mobile_router";
}
