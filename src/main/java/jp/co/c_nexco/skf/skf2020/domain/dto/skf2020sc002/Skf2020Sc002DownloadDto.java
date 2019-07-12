package jp.co.c_nexco.skf.skf2020.domain.dto.skf2020sc002;

import jp.co.c_nexco.nfw.webcore.domain.model.FileDownloadDto;

/**
 * 入居希望申請の画面のファイルダウンロード用Dto。
 * 
 */
@lombok.Data
public class Skf2020Sc002DownloadDto extends FileDownloadDto {

	// 申請書類ファイル名
	private String downloadFileName;
	// 機能ID
	private String functionId;

}
