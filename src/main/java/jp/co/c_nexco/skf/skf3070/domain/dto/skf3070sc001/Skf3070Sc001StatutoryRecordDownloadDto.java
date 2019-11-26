/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3070.domain.dto.skf3070sc001;

import jp.co.c_nexco.nfw.webcore.domain.model.FileDownloadDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3070Sc001 法定調書データ管理画面の法定調書データ出力処理Dto。
 * 
 * @author NEXCOシステムズ
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3070Sc001StatutoryRecordDownloadDto extends FileDownloadDto {

	private static final long serialVersionUID = -1902278406295003652L;

}
