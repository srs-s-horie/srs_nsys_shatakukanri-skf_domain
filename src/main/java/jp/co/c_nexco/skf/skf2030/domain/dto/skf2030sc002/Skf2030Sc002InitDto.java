/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2030.domain.dto.skf2030sc002;

import java.util.*;
import jp.co.c_nexco.skf.skf2030.domain.dto.skf2030Sc002common.Skf2030Sc002CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2030_Sc002画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2030Sc002InitDto extends Skf2030Sc002CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	private boolean status24Flag;

}
