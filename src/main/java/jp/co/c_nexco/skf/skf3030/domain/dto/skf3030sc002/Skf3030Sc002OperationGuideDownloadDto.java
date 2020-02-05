/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3030.domain.dto.skf3030sc002;

import jp.co.c_nexco.nfw.webcore.domain.model.FileDownloadDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3030Sc002OperationGuideDownloadDto 入退居情報登録画面：「運用ガイド」押下時のサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3030Sc002OperationGuideDownloadDto extends FileDownloadDto {

	private static final long serialVersionUID = -1902278406295003652L;
}
