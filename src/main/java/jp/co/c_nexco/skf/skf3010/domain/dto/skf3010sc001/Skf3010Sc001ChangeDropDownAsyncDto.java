/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc001;

import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc001common.Skf3010Sc001CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3090Sc004ChangeDropDownAsyncDto 従業員マスタ一覧ドロップダウン変更時Dto
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3010Sc001ChangeDropDownAsyncDto extends Skf3010Sc001CommonAsyncDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// TODO 共通FWが改修してくれなかった場合はこれで実装
	private String localPrePageId;

}
