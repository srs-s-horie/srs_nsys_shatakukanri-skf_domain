package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc005;

import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010Sc005common.Skf2010Sc005CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2010Sc005 承認一覧ドロップダウン更新Dtoクラス
 *
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010Sc005ChangeDropDownAsyncDto extends Skf2010Sc005CommonAsyncDto {

	/**
	 * シリアルナンバー
	 */
	private static final long serialVersionUID = 1L;

	/** 機関 */
	private String agency;
	/** 部等 */
	private String affiliation1;
	/** 室、チーム又は課 */
	private String affiliation2;
	/** 所属機関 */
	private String shozokuKikan;

}
