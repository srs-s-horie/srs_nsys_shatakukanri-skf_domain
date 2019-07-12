package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc006;

import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import lombok.EqualsAndHashCode;

@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010Sc006AttachedFileAreaAsyncDto extends AsyncBaseDto {

	/**
	 * シリアルナンバー
	 */
	private static final long serialVersionUID = -1902278406295003652L;

	private String applNo;
	private String attachedFileArea;

}
