/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc002;

import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc002common.Skf3010Sc002CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3010Sc002ChangeStructureDrpDwnAsyncDto 保有社宅登録」のドロップダウンリスト変更時Dto。
 * 基本情報タブの「構造」ドロップダウンチェンジ
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3010Sc002ChangeStructureDrpDwnAsyncDto extends Skf3010Sc002CommonAsyncDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// TODO 共通FWが改修してくれなかった場合はこれで実装
	private String localPrePageId;
}
