/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc002;

import jp.co.c_nexco.nfw.webcore.domain.model.FileDownloadDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2010Sc002 申請内容確認の調書ダウンロード処理のDto。
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010Sc002DownloadDto extends FileDownloadDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 社員番号
	private String shainNo;
	// 申請書類識別番号
	private String applNo;
	// 添付資料番号
	private String attachedNo;

}
