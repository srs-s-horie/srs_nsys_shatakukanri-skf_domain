/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc006;

import jp.co.c_nexco.nfw.webcore.domain.model.FileDownloadDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3022Sc006OperationGuideDownloadDto 提示データ登録画面：「運用ガイド」押下時のサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3022Sc006OperationGuideDownloadDto extends FileDownloadDto {

	private static final long serialVersionUID = -1902278406295003652L;
}
