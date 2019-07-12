/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc007;

import jp.co.c_nexco.nfw.webcore.domain.model.FileDownloadDto;

/**
 * Skf2010Sc007DownloadDto 申請条件確認画面、申請要件確認押下時のサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
public class Skf2010Sc007DownloadDto extends FileDownloadDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 申請書類ファイル名
	private String downloadFileName;

	// 機能ID
	private String functionId;

}
