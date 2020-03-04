/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3040.domain.dto.skf3040sc001;

import jp.co.c_nexco.nfw.webcore.domain.model.FileDownloadDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3040Sc001DownloadDto レンタル備品指示書出力画面のDownloadDto。
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3040Sc001DownloadDto extends FileDownloadDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
	
	// 希望日（From）
	private String desiredTermFrom;
	private String desiredTermFromErr;
	
	// 希望日（To）
	private String desiredTermTo;
	private String desiredTermToErr;
	
	// 再発行
	private String reIssuance;

}
