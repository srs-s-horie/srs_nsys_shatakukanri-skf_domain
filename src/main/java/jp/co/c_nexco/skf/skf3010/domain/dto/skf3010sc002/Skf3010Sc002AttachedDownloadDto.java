/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc002;

import jp.co.c_nexco.nfw.webcore.domain.model.FileDownloadDto;
import lombok.EqualsAndHashCode;

/**
* Skf3010Sc002AttachedDownloadDto　保有社宅登録ダウンロード処理Dto
* 
* @author NEXCOシステムズ
*
*/
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3010Sc002AttachedDownloadDto extends FileDownloadDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
	//補足種別
	private String hdnHosoku;
	//添付ファイル番号
	private String hdnAttachedNo;
	// 社宅補足ファイル名1
	private String shatakuHosokuFileName1;
	// 社宅補足ファイル名2
	private String shatakuHosokuFileName2;
	// 社宅補足ファイル名3
	private String shatakuHosokuFileName3;
	// 社宅補足ファイル1
	private byte[] shatakuHosokuFile1;
	// 社宅補足ファイル2
	private byte[] shatakuHosokuFile2;
	// 社宅補足ファイル3
	private byte[] shatakuHosokuFile3;
	// 駐車場補足ファイル名1
	private String parkingHosokuFileName1;
	// 駐車場補足ファイル名2
	private String parkingHosokuFileName2;
	// 駐車場補足ファイル名3
	private String parkingHosokuFileName3;
	// 駐車場補足ファイル1
	private byte[] parkingHosokuFile1;
	// 駐車場補足ファイル2
	private byte[] parkingHosokuFile2;
	// 駐車場補足ファイル3
	private byte[] parkingHosokuFile3;
}
