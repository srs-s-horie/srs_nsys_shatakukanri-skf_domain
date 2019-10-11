/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc008;

import java.util.*;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090Sc008common.Skf3090Sc008CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3090_Sc008画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3090Sc008RegistDto extends Skf3090Sc008CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
		
    // 確認ダイアログフラグ
    private String dialogFlg;
	
}
