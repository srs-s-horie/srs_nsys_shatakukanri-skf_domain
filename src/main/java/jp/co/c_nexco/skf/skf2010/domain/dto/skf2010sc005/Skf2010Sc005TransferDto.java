package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc005;

import lombok.EqualsAndHashCode;

/**
 * Skf2010Sc005 承認一覧確認Dtoクラス
 *
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010Sc005TransferDto extends Skf2010Sc005InitDto {

	/**
	 * シリアルナンバー
	 */
	private static final long serialVersionUID = -1902278406295003652L;

	private String applNo;
	private String applId;
	private String sendApplStatus;
	private String applShainNo;
	private String shonin1;
	private String shonin2;

}
