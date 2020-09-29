/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc006;

import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import lombok.EqualsAndHashCode;

@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010Sc006CheckAsyncDto extends AsyncBaseDto {

	/**
	 * シリアルナンバー
	 */
	private static final long serialVersionUID = -1902278406295003652L;

	// 退居申請チェック用社宅管理番号
	private String checkShatakuKanriNo;
	// 退居申請チェック用社宅部屋管理番号
	private String checkRoomKanriNo;

	private String applNo;
	private String shainNo;

	// ダイアログ表示フラグ
	private boolean dialogFlg = false;

}
