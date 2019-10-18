/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc006;

import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc006common.Skf3010Sc006CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3010Sc006ChangeParkingStructureDrpDwnAsyncDto 駐車場構造ドロップダウンリスト変更時Dto。
 * 駐車場情報タブの「構造」ドロップダウンチェンジ
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3010Sc006ChangeParkingStructureDrpDwnAsyncDto extends Skf3010Sc006CommonAsyncDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// TODO 共通FWが改修してくれなかった場合はこれで実装
	private String localPrePageId;
	
}
