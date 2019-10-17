/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2050.domain.dto.skf2050sc001;

import jp.co.c_nexco.skf.skf2050.domain.dto.skf2050Sc001common.Skf2050Sc001CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2050Sc001 備品返却申請（申請者用)搬出完了非同期処理Dto
 *
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2050Sc001CarryOutAsyncDto extends Skf2050Sc001CommonAsyncDto {

	private static final long serialVersionUID = -1902278406295003652L;

	private String showDialogFlag = "false";
	private String dialogTaikyoDay;

}
