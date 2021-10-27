/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.dto.skf2100Sc006common;

import java.util.List;
import java.util.Map;

import jp.co.c_nexco.nfw.webcore.domain.model.FileDownloadDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2100Sc005CommonDto モバイルルーター貸出管理簿画面共通同期処理DTO
 *
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2100Sc006CommonDto extends FileDownloadDto {
	
	private static final long serialVersionUID = 3202882792085625462L;
	
	/** ドロップダウンリスト */
	/** 原籍会社 */
	private String originalCompanySelect;
	/** 給与支給会社 */
	private String payCompanySelect;
	/** 原籍会社選択リスト */
	private List<Map<String, Object>> originalCompanyList;
	/** 給与支給会社名選択リスト */
	private List<Map<String, Object>> payCompanyList;
	

	/** 「社員番号」 */
	private String shainNo;
	/** 「社員名」 */
	private String shainName;
	/** 「対象年月」テキスト */
	private String yearMonthTxt;
	/** 「申請状況」テキスト */
	private String applStatusTxt;
	/** 「申請状況」テキストカラー設定 */
	private String applStatusTxtColor;
	/** 「通しNo」 */
	private String routerNo;
	/** 「電話番号」 */
	private String tel;
	/** 「ICCID」 */
	private String iccid;
	/** 「IMEI」 */
	private String imei;
	/** 「ルーター入荷日」 */
	private String arrivalDate;
	/** 「契約区分」 テキスト*/
	private String contractKbnTxt;
	/** 「契約終了日」 */
	private String contractEndDate;
	/** 「故障」 */
	private String faultTxt;
	/** 「貸出希望日」 */
	private String useStartHopeDay;
	/** 「本人連絡先」 */
	private String hannyuTel;
	/** 「本人メールアドレス」 */
	private String hannyuMailaddress;
	/** 「発送日」 */
	private String shippingDate;
	/** 「納品日」 */
	private String receivedDate;
	/** 「利用停止日」 */
	private String useStopDay;
	/** 「本人連絡先」 */
	private String hansyutuTel;
	/** 「窓口返却日」 */
	private String returnDay;
	/** 「備考」 */
	private String biko;
	
	/** 運用ガイド */
	private String operationGuidePath;
	
	/**
	 * hidden 項目
	 */
	/** モバイルルーター貸出管理簿ID */
	private String hdnRouterKanriId;
	/** 対象年月 */
	private String hdnYearMonth;
	/** 申請ステータス */
	private String hdnApplStatus;
	/** 契約区分 */
	private String hdnContractKbn;
	/** 故障フラグ値 */
	private String hdnFaultFlag;
	/** 社員番号 */
	private String hdnShainNo;
	/** 社員名 */
	private String hdnShainName;
	/** 選択された「原籍会社」ドロップダウンリストの選択 */
	private String hdnOriginalCompanySelect;
	/** 選択された「給与支給会社」ドロップダウンリストの選択 */
	private String hdnPayCompanySelect;
	/** 社員番号変更フラグ */
	private String hdnShainNoChangeFlg;
	/** 締め処理実行区分 */
	private String HdnBillingActKbn;

	/**
	 * 非活性制御
	 */
	private boolean shainShienDisableFlg;
	private boolean editDisableFlg;
	private boolean btnUnyonGuideDisableFlg;
	private boolean btnRegistDisableFlg;
	private boolean btnDeleteDisableFlg;
	
	
	/** エラー*/
	private String txtContractEndDateToErr;
	private String txtContractEndDateFromErr;
	private String useStartHopeDayErr;
	private String hannyuTelErr;
	private String hannyuMailaddressErr;
	private String shippingDateErr;
	private String receivedDateErr;
	private String useStopDayErr;
	private String hansyutuTelErr;
	private String returnDayErr;
	private String originalCompanyListErr;
	private String payCompanyListErr;
	private String bikoErr;
	
	/** 検索パラメータ保持 */
	// private Skf2100Sc005GetListTableDataExpParameter searchParam;
	private String searchInfoRouterNo;
	private String searchInfoTel;
	private String searchInfoShainNo;
	private String searchInfoShainName;
	private String searchInfoContractKbn;
	private String searchInfoContractEndDateFrom;
	private String searchInfoContractEndDateTo;
	private String searchInfoYearMonth;
	private String searchInfoAkiRouter;
	
	// JSONラベルリスト
	private String jsonLabelList;
	
	/** 定数 */
	//管理番号なし(0番)
	public static final String NO_KANRI_ID = "0";
	public static final String ROUTER_KEY_LAST_UPDATE_DATE = "skf2100_t_mobile_router_ledger";
}
