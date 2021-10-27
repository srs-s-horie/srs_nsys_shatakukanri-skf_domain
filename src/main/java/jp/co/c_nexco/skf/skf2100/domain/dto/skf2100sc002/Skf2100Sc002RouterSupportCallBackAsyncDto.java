/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc002;

import java.util.Map;

import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2100Sc002 モバイルルーター選択後の非同期Dto
 *
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2100Sc002RouterSupportCallBackAsyncDto extends AsyncBaseDto {

	/**
	 * シリアルナンバー
	 */
	private static final long serialVersionUID = -1902278406295003652L;

	// 非同期処理連携パラメータ
	private Map<String, String> mapParam;
	
	private String applNo;
	private String attachedType;
	private String attachedFileArea;
	private Long mobileRouterNo;
	private String iccid;
	private String imei;
	private Long hdnMobileRouterNo;
	

}
