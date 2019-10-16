/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.dto.skf3022Sc002common;

import java.util.List;
import java.util.Map;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3022Sc002CommonAsyncDto 駐車場入力支援非同期処理共通Dto
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3022Sc002CommonAsyncDto extends AsyncBaseDto {

	private static final long serialVersionUID = 1L;

	//社宅管理番号
	private String shatakuNo;
	//使用者
	private String shiyosha;
	//備考
	private String parkingBiko;
	//空き駐車場
	private boolean chushajo;
	private String akiParking;
	//２台使用
	private boolean nidaishiyo;
	//開始日
	private String kaisiDate;
	// リストテーブル情報
	private List<Map<String, Object>> listTableList;
	
	private int dataCount;
	
	private String selParkingBlock;
}
