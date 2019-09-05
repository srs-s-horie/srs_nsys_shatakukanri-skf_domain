package jp.co.c_nexco.skf.skf3020.domain.dto.skf3020sc003;

import java.util.List;
import java.util.Map;

import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3020Sc003 転任者調書確認取込実行DTO
 *
 * @author NEXCOシステムズ
*/
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3020Sc003ImportDto extends BaseDto {

	private static final long serialVersionUID = -1902278406295003652L;
	
	/**
	 * 転任者情報table用
	 */
	private List<Map<String, Object>> tenninshaChoshoDataTable;
	
}
