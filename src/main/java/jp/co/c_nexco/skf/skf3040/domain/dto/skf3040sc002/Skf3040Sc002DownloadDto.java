/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3040.domain.dto.skf3040sc002;

import java.util.*;

import jp.co.c_nexco.nfw.webcore.domain.model.FileDownloadDto;
import jp.co.c_nexco.skf.skf3060.domain.dto.skf3060Sc001common.Skf3060Sc001CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3040_Sc002画面のDownloadDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3040Sc002DownloadDto extends FileDownloadDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
	
	// 搬入・搬出日（From）
	private String carryingInOutTermFrom;
	private String carryingInOutTermFromErr;
	
	// 搬入・搬出日（To）
	private String carryingInOutTermTo;
	private String carryingInOutTermToErr;
	
	// 出力状況
	private String outSituation;	
}
