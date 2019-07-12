/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc010;

import java.util.List;
import java.util.Map;
import jp.co.c_nexco.skf.skf2010.domain.dto.common.Skf2010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2010_Sc006画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010Sc010InitDto extends Skf2010CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	private String applNo;
	private String applStatus;
	private List<Map<String, String>> commentList;
}
