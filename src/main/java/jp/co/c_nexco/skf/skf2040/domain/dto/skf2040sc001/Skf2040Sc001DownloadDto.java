/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.dto.skf2040sc001;

import jp.co.c_nexco.nfw.webcore.domain.model.FileDownloadDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2040Sc001 退居（自動車の保管場所返還）届画面のファイルダウンロード用Dto。
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2040Sc001DownloadDto extends FileDownloadDto {

    private static final long serialVersionUID = -1902278406295003652L;
    // 申請書類ファイル名
    private String downloadFileName;
    // 機能ID
    private String functionId;

}
