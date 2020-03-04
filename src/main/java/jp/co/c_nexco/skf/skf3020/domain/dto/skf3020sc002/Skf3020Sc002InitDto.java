/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.dto.skf3020sc002;

import jp.co.c_nexco.skf.skf3020.domain.dto.skf3020Sc002common.Skf3020Sc002CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3020Sc002InitDto 転任者調書取込画面のInitDto
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3020Sc002InitDto extends Skf3020Sc002CommonDto {

	private static final long serialVersionUID = 6750607356593193752L;

	/** 「前に戻る」ボタン非表示フラグ（hidden） */
	private boolean backBtnHiddenFlg;
	
}
