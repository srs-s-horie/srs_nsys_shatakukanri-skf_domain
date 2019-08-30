/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc001;

import java.util.List;
import java.util.Map;

import jp.co.c_nexco.nfw.webcore.domain.model.FileDownloadDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3010Sc001ContractDownLoadDto 契約情報出力ボタン処理のDto。
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3010Sc001ContractDownLoadDto extends FileDownloadDto {

	private static final long serialVersionUID = -1902278406295003652L;

	/**
	 * listTable用
	 * 
	 * @author NEXCOシステムズ
	 */
	private List<Map<String, Object>> listTableData;
	private String listTableMaxRowCount;
	/** 検索キー */
	// 会社コード(検索キー)
	private String selectedCompanyCd;
	// 機関コード(検索キー)
	private String agencyCd;
	// 社宅区分コード(検索キー)
	private String shatakuKbnCd;
	// 空き部屋コード(検索キー)
	private String emptyRoomCd;
	// 利用区分コード(検索キー)
	private String useKbnCd;
	// 空き駐車場コード(検索キー)
	private String emptyParkingCd;
	// 社宅名(検索キー)
	private String shatakuName;
	// 社宅住所(検索キー)
	private String shatakuAddress;
	
	/** 表示/非表示フラグ */
	// 外部機関表示フラグ(検索キー)
	private Boolean agencyDispFlg;
	
	/** ドロップダウンリスト */
	// 管理会社リスト
	private List<Map<String, Object>> manageCompanyList;
	// 管理機関リスト
	private List<Map<String, Object>> manageAgencyList;
	// 社宅区分リスト
	private List<Map<String, Object>> shatakuKbnList;
	// 空き部屋リスト
	private List<Map<String, Object>> emptyRoomList;
	// 利用区分リスト
	private List<Map<String, Object>> useKbnList;
	// 空き駐車場リスト
	private List<Map<String, Object>> emptyParkingList;
	
	/** 画面連携用 */
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
    // 対象行の部屋番号
    private String hdnRoomNo;
    
    /** hdn検索キー */
	// 管理会社コード(検索キー)
	private String hdnSelectedCompanyCd;
	// 管理機関コード(検索キー)
	private String hdnAgencyCd;
	// 社宅区分コード(検索キー)
	private String hdnShatakuKbnCd;
	// 利用区分コード(検索キー)
	private String hdnUseKbnCd;
    // 空き部屋コード(検索キー)
    private String hdnEmptyRoomCd;
	// 空き駐車場コード(検索キー)
	private String hdnEmptyParkingCd;
    // 社宅名(検索キー)
    private String hdnShatakuName;
	// 社宅住所(検索キー)
	private String hdnShatakuAddress;
	
	/** 画面表示用 */
	// 会社名
	private String companyName;
	// 管理会社名
	private String agencyName;
}
