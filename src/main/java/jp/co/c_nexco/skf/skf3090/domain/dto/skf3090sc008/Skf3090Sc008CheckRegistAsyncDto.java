/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc008;

import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090Sc008common.Skf3090Sc008CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3090_Sc008画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3090Sc008CheckRegistAsyncDto extends Skf3090Sc008CommonAsyncDto {
	
	private static final long serialVersionUID = -1902278406295003652L;

	//お知らせ内容
	private String note;
	//公開開始日カレンダー
	private String openDateBox;
	
	//確認ダイアログフラグ
	private boolean dialogFlg;
}
