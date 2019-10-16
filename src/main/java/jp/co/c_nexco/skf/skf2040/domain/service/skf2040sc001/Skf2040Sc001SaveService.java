/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.service.skf2040sc001;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReport;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040sc001.Skf2040Sc001SaveDto;

/**
 * Skf2040Sc001 退居（自動車の保管場所返還）届画面の一時保存処理。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2040Sc001SaveService extends BaseServiceAbstract<Skf2040Sc001SaveDto> {
    
    @Autowired
    private SkfOperationLogUtils skfOperationLogUtils;
    
    @Autowired
    private Skf2040Sc001SharedService skf2040Sc001SharedService;

    /**
     * サービス処理を行う。　
     * 
     * @param saveDto インプットDTO
     * @return 処理結果
     * @throws Exception 例外
     */
    @Override
    public Skf2040Sc001SaveDto index(Skf2040Sc001SaveDto saveDto) throws Exception {
        // 操作ログを出力する
        skfOperationLogUtils.setAccessLog("一時保存", CodeConstant.C001, saveDto.getPageId());
        
        // 申請書情報の取得
        skf2040Sc001SharedService.setSkfApplInfo(saveDto);
        // 新規登録したかを判定するため、処理前の申請ステータスを保持しておく
        String beforeApplStatus = saveDto.getStatus();
        
        // バイトカット処理
        skf2040Sc001SharedService.cutByte(saveDto);
        
        // 一時保存処理を行う
        if (!this.execSave(saveDto)) {
            return saveDto;
        }
        
        // プルダウンリストの再設定（選択初期値反映）
        this.setDropdownList(saveDto);
        // 正常終了
        if (CodeConstant.STATUS_MISAKUSEI.equals(beforeApplStatus)) {
            // 新規登録時
            ServiceHelper.addResultMessage(saveDto, MessageIdConstant.I_SKF_1012);
        } else {
            // 更新時
            ServiceHelper.addResultMessage(saveDto, MessageIdConstant.I_SKF_1011);
        }
        return saveDto;
    }
    
    /**
     * 退居届画面での一時保存処理を実行する。
     * <pre>
     * 下記のテーブルについて、すでにレコードが存在する場合は更新処理、
     * レコードが存在しない場合は新規登録処理を行う。
     *   ・申請履歴
     *   ・退居届
     *   ・備品返却申請
     * </pre>
     * @param dto
     * @param applInfoMap
     * @return 登録成功 true 失敗 false
     */
    private boolean execSave(Skf2040Sc001SaveDto saveDto){
        boolean isExecSave = false;
        
        if (CodeConstant.STATUS_MISAKUSEI.equals(saveDto.getStatus())) {
            // 新規保存の場合
            
            // 申請履歴、退居届の登録
            isExecSave = skf2040Sc001SharedService.saveNewTaikyoData(saveDto);
            
            // 退居社宅がある場合は備品返却の作成 ※旧システムでは駐車場返還のみの場合でも備品返却情報を作成している
            // 備品返却申請テーブル登録処理
            skf2040Sc001SharedService.registrationBihinShinsei(saveDto);
            
        } else {
            // 更新の場合
            
            // 排他チェック（退居届テーブルの更新有無で判定）
            Skf2040TTaikyoReport taikyoInfo = 
                    skf2040Sc001SharedService.getExistTaikyoInfo(saveDto.getApplNo());
            if (taikyoInfo == null) {
                // 退居届情報が見つからなかった場合エラーメッセージを表示して処理終了
                skf2040Sc001SharedService.setDisableBtn(saveDto);
                ServiceHelper.addErrorResultMessage(saveDto, null, MessageIdConstant.E_SKF_1077);
            }
            
            super.checkLockException(
                    saveDto.getLastUpdateDate(saveDto.UPDATE_TABLE_PREFIX_TAIKYO_REPORT + saveDto.getApplNo()),
                    taikyoInfo.getUpdateDate()
            );
            
            // 申請履歴、退居届の更新
            isExecSave = skf2040Sc001SharedService.updateTaikyoData(saveDto);
            
            // 備品返却申請テーブル登録処理
            skf2040Sc001SharedService.registrationBihinShinsei(saveDto);
        }
        return isExecSave;
    }
    
    /**
     * 退居届画面のドロップダウンリスト情報を設定する。
     * @param saveDto
     */
    private void setDropdownList(Skf2040Sc001SaveDto saveDto){
        // 退居(返還)理由ドロップダウンリストをDtoに設定
        saveDto.setDdlTaikyoRiyuKbnList(
                skf2040Sc001SharedService.getTaikyoHenkanRiyuList(saveDto.getTaikyoRiyuKbn()));
        
        // 現居住社宅名ドロップダウンリストをDtoに設定
        List<Map<String, Object>> nowShatakuNameList = 
                skf2040Sc001SharedService.getNowShatakuNameList(saveDto.getShainNo(), saveDto.getShatakuNo());
        if ( null != nowShatakuNameList && nowShatakuNameList.size() > 0 ) {
            saveDto.setDdlNowShatakuNameList(nowShatakuNameList);
            // リスト一件目の物件の社宅管理IDを取得してDTOに設定しておく
            long firstShatakuKanriId = (long) nowShatakuNameList.get(0).get("value");
            saveDto.setShatakuKanriId(firstShatakuKanriId);
        } else {
            // データが取得できなかった場合、エラーメッセージを表示して初期表示処理を終了
            skf2040Sc001SharedService.setDisableBtn(saveDto);
            // エラーメッセージ表示
            ServiceHelper.addErrorResultMessage(saveDto, null, MessageIdConstant.E_SKF_2015);
            throwBusinessExceptionIfErrors(saveDto.getResultMessages());
        }
        
        // 返却立合希望日（時）ドロップダウンリストをDtoに設定
        saveDto.setDdlReturnWitnessRequestDateList(
                skf2040Sc001SharedService.getSessionTimeList(saveDto.getSessionTime()));
    }
}
