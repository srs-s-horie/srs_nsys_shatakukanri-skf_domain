package jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc002;

import jp.co.c_nexco.nfw.webcore.domain.model.FileDownloadDto;
import lombok.EqualsAndHashCode;

/**
 * モバイルルーター借用希望申請の画面の添付ファイルダウンロード用Dto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2100Sc002AttachedFileDownloadDto extends FileDownloadDto {

	private static final long serialVersionUID = -1902278406295003652L;
	
	// 社員番号
	private String shainNo;
	// 申請書類識別番号
	private String applNo;
	// 添付資料番号
	private String attachedNo;
	// 添付資料タイプ
	private String attachedType;
	//補足種別
	private String hdnHosoku;
	//添付ファイル番号
	private String hdnAttachedNo;
	// 補足ファイル名1
	private String hosokuFileName1;
	// 補足ファイル名2
	private String hosokuFileName2;
	// 補足ファイル名3
	private String hosokuFileName3;
	// 補足リンク名1
	private String hosokuLink1;
	// 補足リンク名2
	private String hosokuLink2;
	// 補足リンク名3
	private String hosokuLink3;
	// 補足サイズ1
	private String hosokuSize1;
	// 補足サイズ2
	private String hosokuSize2;
	// 補足サイズ3
	private String hosokuSize3;
	// 補足ファイル1
	private byte[] hosokuFile1;
	// 補足ファイル2
	private byte[] hosokuFile2;
	// 補足ファイル3
	private byte[] hosokuFile3;

}
