/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3070.domain.dto.skf3070sc001;

import java.util.List;
import java.util.Map;

import jp.co.c_nexco.nfw.webcore.domain.model.FileDownloadDto;
import jp.co.c_nexco.skf.skf3070.domain.dto.skf3070Sc001common.Skf3070Sc001CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3070Sc001 法定調書データ管理画面の賃貸人（代理人）情報出力処理クラス。
 * 
 * @author NEXCOシステムズ
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3070Sc001LessorInfoDownloadDto extends FileDownloadDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 検索結果表示用
	private List<Map<String, Object>> listTableData;

	// 所持物件数
	private String propertiesOwnedCnt;
}
