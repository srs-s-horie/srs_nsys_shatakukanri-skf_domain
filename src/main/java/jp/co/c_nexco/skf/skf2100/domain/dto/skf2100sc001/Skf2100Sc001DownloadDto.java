package jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc001;

import jp.co.c_nexco.nfw.webcore.domain.model.FileDownloadDto;
import lombok.EqualsAndHashCode;

/**
 * モバイルルーター借用希望申請書画面のファイルダウンロード用Dto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2100Sc001DownloadDto extends FileDownloadDto {

	private static final long serialVersionUID = -1902278406295003652L;
	
	// 申請書類ファイル名
	private String downloadFileName;
	// 機能ID
	private String functionId;

}
