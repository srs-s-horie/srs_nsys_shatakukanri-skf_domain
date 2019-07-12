/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc004;

import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090Sc004common.Skf3090Sc004CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3090Sc004InitDto 従業員マスタ一覧初期表示Dto
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3090Sc004InitDto extends Skf3090Sc004CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 原籍会社コード
	private String originalCompanyCd;

	// リストテーブルの１ページ最大表示行数
	private String listTableMaxRowCount;

}
