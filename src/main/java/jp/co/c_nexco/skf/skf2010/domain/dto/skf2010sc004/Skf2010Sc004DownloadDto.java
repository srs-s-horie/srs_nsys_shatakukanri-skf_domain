package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc004;

import jp.co.c_nexco.nfw.webcore.domain.model.FileDownloadDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2010Sc004 申請内容表示/引戻し添付資料ダウンロード処理Dto
 *
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010Sc004DownloadDto extends FileDownloadDto {

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

}
