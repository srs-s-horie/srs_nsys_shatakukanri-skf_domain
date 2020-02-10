/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc009;

import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010Sc009common.Skf2010Sc009CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2010Sc009 添付資料入力支援ファイル削除処理Dto
 *
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010Sc009DeleteAsyncDto extends Skf2010Sc009CommonAsyncDto {

	private static final long serialVersionUID = -1902278406295003652L;

	private String popAttachedNo;
}