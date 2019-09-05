/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc001;

import java.util.*;

import jp.co.c_nexco.nfw.webcore.domain.model.FileDownloadDto;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060Sc001common.Skf2060Sc001CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2060_Sc001画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2060Sc001DownloadDto extends FileDownloadDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
	
	//提示対象者番号
	private String shainNo;
	//申請書類管理番号
	private String applNo;
	
}
