/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010Sc002common;

import jp.co.c_nexco.skf.skf2010.domain.dto.common.Skf2010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * 申請情報確認画面の共通Dto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010Sc002CommonDto extends Skf2010CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 申請ステータス
	private String status;
	// コメント表示レベル
	private String commentDisplayLevel;

}
