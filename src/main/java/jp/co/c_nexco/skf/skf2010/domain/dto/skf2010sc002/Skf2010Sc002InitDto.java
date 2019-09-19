/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc002;

import java.util.List;
import java.util.Map;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010Sc002common.Skf2010Sc002CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2010_Sc002 申請内容確認画面のInitDto。
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010Sc002InitDto extends Skf2010Sc002CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	/**
	 * 表示フラグ（true:表示、false:非表示）
	 */
	// 提示ボタン表示フラグ
	private String presenBtnViewFlg;
	// 申請ボタン表示フラグ
	private String applyBtnViewFlg;
	// コメント表示フラグ
	private String commentViewFlag;

	// 添付資料情報
	private List<Map<String, Object>> attachedFileList;

	/** 画面表示用 **/
	private int displayLevel; // 表示レベル（１～３）
	// アコーディオン
	// 社宅入居希望等調書
	private String level1;
	// 貸与社宅などのご案内
	private String level2;
	// 退居届
	private String level3;

	// アコーディオン初期表示用フラグ
	private String level1Open;
	private String level2Open;
	private String level3Open;

}
