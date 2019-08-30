/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.dto.skf3020sc003;

import java.util.*;
import jp.co.c_nexco.skf.skf3020.domain.dto.skf3020Sc003common.Skf3020Sc003CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3020Sc003 転任者調書確認 InitDTO
 *
 * @author NEXCOシステムズ
*/
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3020Sc003InitDto extends Skf3020Sc003CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
	
	/**
	 * 転任者情報table用
	 */
	private List<Map<String, Object>> tenninshaChoshoDataTable;

}
