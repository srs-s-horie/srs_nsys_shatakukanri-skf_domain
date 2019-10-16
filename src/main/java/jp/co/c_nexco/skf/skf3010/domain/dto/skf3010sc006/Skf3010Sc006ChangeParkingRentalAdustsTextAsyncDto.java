/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc006;

import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc006common.Skf3010Sc006CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3010Sc006ChangeParkingRentalAdustsTextAsyncDto 借上社宅登録のテキスト変更時Dto。
 * 駐車場情報タブ「駐車場調整金額」チェンジ
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3010Sc006ChangeParkingRentalAdustsTextAsyncDto extends Skf3010Sc006CommonAsyncDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// TODO 共通FWが改修してくれなかった場合はこれで実装
	private String localPrePageId;
	
}
