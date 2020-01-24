/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2030.domain.dto.skf2030fc001;

import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import lombok.EqualsAndHashCode;

@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2030Fc001InitDto extends BaseDto {

	private static final long serialVersionUID = -1902278406295003652L;

	/** Skf2030Fc001備品希望申請データ連携 */
	// 会社コード
	private String skf2030Fc001CompanyCd;
	// 社員番号
	private String skf2030Fc001ShainNo;
	// 申請書類管理番号
	private String skf2030Fc001ApplNo;
	// 申請ステータス
	private String skf2030Fc001ApplStatus;
	// ユーザID
	private String skf2030Fc001UserID;
	// 画面ID
	private String skf2030Fc001PageID;

	// 結果コード
	private String returnStatus;

}
