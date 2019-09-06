/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc007;

import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090Sc007common.Skf3090Sc007CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3090_Sc007画面のAffiliation1SearchDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3090Sc007Affiliation1SearchAsyncDto extends Skf3090Sc007CommonAsyncDto {

	private static final long serialVersionUID = -1902278406295003652L;

	private String resultList;
}
