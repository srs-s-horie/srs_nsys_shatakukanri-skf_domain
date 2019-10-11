/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.dto.skf3022Sc002common;

import java.util.List;
import java.util.Map;
import jp.co.c_nexco.nfw.webcore.domain.model.GridRelationDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3022Sc002CommonDto 駐車場入力支援共通DTO
 *
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3022Sc002CommonDto extends GridRelationDto {

	private static final long serialVersionUID = -1902278406295003652L;

	//呼び出しパラメータ
	// 社宅名
	private String hdnShatakuKanriNo;
	
	// 社宅名
	private String hdnShatakuName;
	
	//利用開始日
	private String hdnRiyouStartDay;
	
	// リストテーブル
	private List<Map<String, Object>> listTableList;
	private String maxCount;
	
	// 社宅名
	private String shatakuName;
	// 使用者
	private String shiyosha;
	// 空き駐車場
	private String[] akiParking;
	// 備考
	private String parkingBiko;
	
	//連携パラメータ
	//駐車場管理番号
	private String parkNo;
	//区画番号
	private String parkBlock;
	//駐車場使用料月額
	private String parkRentalAsjust;
	//最終日付
	private String endDay;
	
	private String selParkingBlock;

}
