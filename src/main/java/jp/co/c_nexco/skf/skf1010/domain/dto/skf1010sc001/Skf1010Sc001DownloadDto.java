/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf1010.domain.dto.skf1010sc001;

import jp.co.c_nexco.nfw.webcore.domain.model.FileDownloadDto;
import lombok.EqualsAndHashCode;

/**
 * Skf1010_Sc001社宅管理TOP画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf1010Sc001DownloadDto extends FileDownloadDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 申請書類ファイル名
	private String downloadFileName;
	
	// 機能ID
	private String functionId;
	
	// マニュアル区別
	private String manual;
}
