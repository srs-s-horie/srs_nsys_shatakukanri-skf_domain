/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc009;

import jp.co.c_nexco.nfw.webcore.domain.model.FileDownloadDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2010Sc009 添付資料選択支援ファイルダウンロード処理Dto
 *
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010Sc009DownloadDto extends FileDownloadDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 添付資料番号
	private String popAttachedNo;
	// 申請書類ID
	private String popApplId;
	// 借上候補物件番号
	private String popCandidateNo;

}
