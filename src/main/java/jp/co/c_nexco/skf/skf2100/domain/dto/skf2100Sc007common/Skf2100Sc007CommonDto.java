/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.dto.skf2100Sc007common;

import java.util.List;
import java.util.Map;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2100Sc007.Skf2100Sc007GetListTableDataExpParameter;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2100Sc007CommonDto モバイルルーターマスタ一覧画面共通同期処理DTO
 *
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2100Sc007CommonDto extends BaseDto {
	
	private static final long serialVersionUID = 3202882792085625462L;
	
	/** 「契約区分」ドロップダウンリスト */
	private List<Map<String, Object>> contractKbnDropDownList;

	/** 「検索結果一覧」リスト */
	private List<Map<String, Object>> searchDataList;

	
	/** 「通しNo」テキスト */
	private String txtRouterNo;
	/** 「ICCID」テキスト */
	private String txtIccid;
	/** 「電話番号」テキスト */
	private String txtTel;
	/** 「IMEI」テキスト */
	private String txtImei;
	/** 「ルータ入荷日From」テキスト */
	private String txtArrivalDateFrom;
	/** 「ルーター入荷日To」テキスト */
	private String txtArrivalDateTo;
	/** 「契約終了日From」テキスト */
	private String txtContractEndDateFrom;
	/** 「契約終了日To」テキスト */
	private String txtContractEndDateTo;
	/** 「故障」チェックボックス */
	private String chkFault;

	/**
	 * hidden 項目
	 */
	/** 選択された 「契約区分」ドロップダウンリストの選択 */
	private String hdnContractKbnSelect;
	/** 「故障」チェックボックス選択状態 */
	private String hdnChkFaultSelect;
	/** 検索結果一覧の選択されたインデックス */
	private String hdnSelIdx;

	/** エラー*/
	private String txtContractEndDateToErr;
	private String txtContractEndDateFromErr;
	private String txtArrivalDateFromErr;
	private String txtArrivalDateToErr;
	private String txtRouterNoErr;
	
	/** 検索パラメータ */
	private Skf2100Sc007GetListTableDataExpParameter searchParam;
}
