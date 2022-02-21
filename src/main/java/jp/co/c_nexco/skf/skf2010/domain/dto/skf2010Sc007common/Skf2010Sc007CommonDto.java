/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010Sc007common;

import jp.co.c_nexco.skf.skf2010.domain.dto.common.Skf201050CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2010Sc007CommonDto 申請条件確認共通Dto
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010Sc007CommonDto extends Skf201050CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 画面表示区分
	private String confirmationKbn;

	// ユーザコード
	private String userId;
	// 会社コード
	private String companyCd;
	// 社員番号
	private String shainNo;
	// 申請書ID
	private String applId;
	
	// 申請要件を確認ボタン
	private String btnCheckDisabled;

}
