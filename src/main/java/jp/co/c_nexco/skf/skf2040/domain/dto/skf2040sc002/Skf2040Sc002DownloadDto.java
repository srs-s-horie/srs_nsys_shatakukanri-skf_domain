package jp.co.c_nexco.skf.skf2040.domain.dto.skf2040sc002;

/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
import jp.co.c_nexco.nfw.webcore.domain.model.FileDownloadDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2040Sc002DownloadService 添付資料ダウンロード時のクラス
 *
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2040Sc002DownloadDto extends FileDownloadDto {

	/**
	 * シリアルナンバー
	 */
	private static final long serialVersionUID = -1902278406295003652L;

	// 社員番号
	private String shainNo;
	// 申請書類識別番号
	private String applNo;
	// 添付資料番号
	private String attachedNo;
	// 添付資料タイプ
	private String attachedType;

}
