/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc006;

import jp.co.c_nexco.skf.skf2010.domain.dto.common.Skf2010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2010Sc006 申請書類承認／差戻し／通知初期表示Dtoクラス
 *
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010Sc006InitDto extends Skf2010CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 承認者１社員番号
	private String shonin1Name;
	// 承認ボタン表示フラグ（true:表示、false:非表示）
	private String shoninBtnViewFlag;

	// コメントボタン表示フラグ（true:表示、false:非表示）
	private String commentViewFlag;

	// 再提示ボタンの表示非表示フラグ
	private String representBtnFlg;

}
