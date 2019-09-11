package jp.co.c_nexco.skf.skf2040.domain.dto.common;

import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import lombok.EqualsAndHashCode;

/**
 * TestPrjTop画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf204010CommonDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 社宅の状態
	private String shatakuJyotai;
	// 返却立会希望日
	private String sessionDay;;
	// 連絡先
	private String renrakuSaki;

	// コメント
	private String commentNote;

}
