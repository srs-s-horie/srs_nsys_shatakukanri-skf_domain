/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.service.skf2040sc001;

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
        
        // バイトカット処理
        skf2040Sc001SharedService.cutByte(saveDto);
        
        // 一時保存処理を行う
        if (!this.execSave(saveDto)) {
            return saveDto;
        }
        
        // 正常終了
        if (CodeConstant.STATUS_MISAKUSEI.equals(saveDto.getStatus())) {
            ServiceHelper.addResultMessage(saveDto, MessageIdConstant.I_SKF_1012);
        } else {
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
            
            // 社宅退居のチェック状況を取得する。 //TODO まるっといらない疑惑
            boolean isShatakuTaikyoChecked = false;
            String[] taikyoTypeArray = saveDto.getTaikyoType();
            if (taikyoTypeArray!=null && taikyoTypeArray.length > 0) {
                for(String taikyoType : taikyoTypeArray){
                    if (taikyoType.equals("shataku_checked")) {
                        //「社宅を退居する」にチェックがされている場合
                        isShatakuTaikyoChecked = true;
                    }
                }
            }
            
            // 退居社宅がある場合は備品返却の作成 ※旧システムでは駐車場返還のみの場合でも備品返却情報を作成している
            // 備品返却申請テーブル登録処理
            skf2040Sc001SharedService.registrationBihinShinsei(saveDto);
            
        } else {
            // 更新の場合
            
            // 排他チェック（退居届テーブルの更新有無で判定） TODO 申請履歴でもチェックできなくもないが、必要か？
            Skf2040TTaikyoReport taikyoInfo = 
                    skf2040Sc001SharedService.getExistTaikyoInfo(saveDto.getApplNo());
            
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
}
