/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.dto.skf3090test03;

import java.util.*;

import jp.co.c_nexco.skf.skf3090.domain.dto.common.Skf309030Test03CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * TestPrjTop画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3090Test03ChangeDropDownAsyncDto extends Skf309030Test03CommonAsyncDto {

	private static final long serialVersionUID = -1902278406295003652L;
	

	
	private String companyName;
	
	private String agencyName;

	private List<Map<String, Object>> listTableData;
	
	private String actingUserName;
}
