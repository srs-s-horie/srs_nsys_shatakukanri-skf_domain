/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.service.skf2040sc001;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040sc001.Skf2040Sc001DownloadDto;

/**
 * Skf2040Sc001 退居（自動車の保管場所返還）届画面の申請要件確認押下時のサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2040Sc001DownloadService extends BaseServiceAbstract<Skf2040Sc001DownloadDto> {

    /**
     * サービス処理を行う。
     * 
     * @param Skf2040Sc001DownloadDto
     * @return 処理結果
     * @throws Exception 例外
     * 
     */

    @Autowired
    private SkfOperationLogUtils skfOperationLogUtils;

    @Override
    protected BaseDto index(Skf2040Sc001DownloadDto dto) throws Exception {

        // 操作ログを出力する
        skfOperationLogUtils.setAccessLog("申請要件を確認", CodeConstant.C001, dto.getPageId());

        // ダウンロードファイル名
        String downloadFileName = "skf.skf_appl_requirement.FileId";

        // 機能ID
        String functionId = "skfapplrequirement";

        // DTOに値をセット
        dto.setDownloadFileName(downloadFileName);
        dto.setFunctionId(functionId);

        // ファイル出力処理
        SkfFileOutputUtils.fileOutputPdf(downloadFileName, functionId, dto);

        return dto;

    }

}
