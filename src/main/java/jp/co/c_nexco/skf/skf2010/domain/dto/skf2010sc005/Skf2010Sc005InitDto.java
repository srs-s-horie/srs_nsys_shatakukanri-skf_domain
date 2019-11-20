package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc005;

import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010Sc005common.Skf2010Sc005CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2010Sc005 承認一覧初期表示Dtoクラス
 *
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010Sc005InitDto extends Skf2010Sc005CommonDto {

	/**
	 * シリアルナンバー
	 */
	private static final long serialVersionUID = 1L;

	// private List<String> shainNoList;
}
