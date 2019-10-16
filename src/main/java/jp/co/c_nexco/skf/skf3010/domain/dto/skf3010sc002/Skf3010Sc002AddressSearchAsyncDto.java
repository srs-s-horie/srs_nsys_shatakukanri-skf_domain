/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc002;

import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc002common.Skf3010Sc002CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3010Sc002AddressSearchAsyncDto 保有社宅登録基本情報タブの住所検索ボタン押下時のDto。<br>
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3010Sc002AddressSearchAsyncDto extends Skf3010Sc002CommonAsyncDto {
	
	private static final long serialVersionUID = -1902278406295003652L;

	// TODO 共通FWが改修してくれなかった場合はこれで実装
	private String localPrePageId;

}
