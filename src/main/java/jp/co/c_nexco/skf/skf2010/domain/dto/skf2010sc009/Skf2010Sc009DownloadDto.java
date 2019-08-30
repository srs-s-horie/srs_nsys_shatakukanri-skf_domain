/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc009;

import jp.co.c_nexco.nfw.webcore.domain.model.FileDownloadDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2010_Sc009画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010Sc009DownloadDto extends FileDownloadDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 添付資料番号
	private String attachedNo;
	// 申請書類ID
	private String applId;
	// 借上候補物件番号
	private String candidateNo;

}
