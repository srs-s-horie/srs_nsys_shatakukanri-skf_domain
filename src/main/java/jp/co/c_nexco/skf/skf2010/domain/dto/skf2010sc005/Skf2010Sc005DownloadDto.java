package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc005;

import jp.co.c_nexco.nfw.webcore.domain.model.FileDownloadDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2010Sc005 承認一覧CSV出力Dtoクラス
 *
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010Sc005DownloadDto extends FileDownloadDto {

	/**
	 * シリアルナンバー
	 */
	private static final long serialVersionUID = 1L;

	// 申請日From
	private String applDateFrom;
	// 申請日To
	private String applDateTo;
	// 承認日／修正依頼日From
	private String agreDateFrom;
	// 承認日／修正依頼日To
	private String agreDateTo;
	// 所属機関
	private String shozokuKikan;
	// 機関
	private String agency;
	// 部等
	private String affiliation1;
	// 室・課等
	private String affiliation2;
	// 申請者名
	private String name;
	// 申請書類種別
	private String applCtgry;
	// 申請書類名
	private String applName;
	// 承認者名
	private String agreementName;
	// 申請状況
	private String[] applStatus;

}
