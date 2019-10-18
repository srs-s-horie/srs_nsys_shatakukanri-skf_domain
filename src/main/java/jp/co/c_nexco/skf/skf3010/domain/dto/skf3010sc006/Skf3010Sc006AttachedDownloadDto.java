/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc006;

import java.util.*;

import jp.co.c_nexco.nfw.webcore.domain.model.FileDownloadDto;
import lombok.EqualsAndHashCode;

/**
* Skf3010Sc006AttachedDownloadDto　借上社宅登録ダウンロード処理Dto
* 
* @author NEXCOシステムズ
*
*/
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3010Sc006AttachedDownloadDto extends FileDownloadDto {
	
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
