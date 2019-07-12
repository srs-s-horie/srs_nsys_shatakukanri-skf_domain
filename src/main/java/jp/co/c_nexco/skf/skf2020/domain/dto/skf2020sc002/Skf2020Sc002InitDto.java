/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2020.domain.dto.skf2020sc002;

import jp.co.c_nexco.skf.skf2020.domain.dto.skf2020Sc002common.Skf2020Sc002CommonDto;
import lombok.EqualsAndHashCode;

/**
 * 入居希望等調書申請画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2020Sc002InitDto extends Skf2020Sc002CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 操作ガイド
	private String operationGuide;

}
