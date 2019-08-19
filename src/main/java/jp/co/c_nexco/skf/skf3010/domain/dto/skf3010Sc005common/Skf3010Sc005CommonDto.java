/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc005common;

import java.util.List;
import java.util.Map;

import jp.co.c_nexco.skf.skf3010.domain.dto.common.Skf301010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * TestPrjTop画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3010Sc005CommonDto extends Skf301010CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
	
	/**
	 * 検索フォーム用
	 */
	// 社宅名
	private String shatakuName;
	// 地域区分
	private String areaKbn;
	// 社宅区分
	private String shatakuKbn;
	// 空き部屋数
	private String emptyRoomCount;
	// 空き駐車場数
	private String emptyParkingCount;
	// 社宅管理番号
	private Long shatakuKanriNo;

	/** 部屋登録画面hidden項目連携用 */
	// 社宅管理番号
	private String hdnShatakuKanriNo;
	// 部屋管理番号
	private String hdnRoomKanriNo;
	// 社宅名
	private String hdnShatakuName;
	// 地域区分
	private String hdnAreaKbn;
	// 社宅区分
	private String hdnShatakuKbn;
	// 本来用途
	private String hdnOriginalAuse;
	// 貸与区分
	private String hdnLendKbn;
	// 空き部屋数
	private String hdnEmptyRoomCount;
	// 空き駐車場数
	private String hdnEmptyParkingCount;

	// 更新フラグ
	private String updateFlag;
	
	/**
	 * 部屋情報
	 */
	//　部屋番号
	private String roomNo;
	// 本来延面積
	private String originalMenseki;
	// 本来規格
	private String originalKikaku;
	// 本来規格補足
	private String originalKikakuHosoku;
	// 貸与延面積
	private String lendMenseki;
	// 本来用途
	private String originalAuse;
	//本来用途補足			
	private String originalAuseHosoku;
	//サンルーム延面積
	private String sunRoomMenseki;
	//貸与区分
	private String lendKbn;
	//貸与区分補足
	private String lendKbnHosoku;
	//寒冷地減免区分事由区分
	private String coldExemptionKbn;
	//備考
	private String biko;
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
	
	/**
	 * 備品情報
	 */
	//備品名称
	private String bihinName;
	//備付状況
	private String bihinStatus;
	//備付最新状態区分(非表示)
	private String bihinLatestStatus;
	//備品リスト
	private List<Map<String, Object>> bihinListData;
	// 非表示備品情報リスト
	private List<Map<String, Object>> hdnBihinStatusList;
	//登録用備品データ
	private String registBihinData;
	
	/**
	 * 内部フラグ
	 */
	private String hdnRegistFlg;
	
	/** エラー系 **/
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
	// 本来規格（補助）
	private String originalKikakuHosokuError;
	//本来用途（補助）			
	private String originalAuseHosokuError;
	//貸与区分（補助）
	private String lendKbnHosokuError;
	//備考
	private String bikoError;
}
