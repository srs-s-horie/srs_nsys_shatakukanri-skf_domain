/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2050.domain.dto.skf2050fc001;

import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import lombok.EqualsAndHashCode;

@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2050Fc001InitDto extends BaseDto {

	private static final long serialVersionUID = -1902278406295003652L;

	/** Skf2050Fc001備品返却確認データ連携 */
	// 会社コード
	private String skf2050Fc001CompanyCd;
	// 社員番号
	private String skf2050Fc001ShainNo;
	// 申請書類管理番号
	private String skf2050Fc001ApplNo;
	// 申請ステータス
	private String skf2050Fc001ApplStatus;
	// ユーザID
	private String skf2050Fc001UserID;
	// 画面ID
	private String skf2050Fc001PageID;

	// 結果コード
	private String returnStatus;

}
