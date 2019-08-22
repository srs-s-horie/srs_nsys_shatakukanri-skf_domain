/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc004;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.emory.mathcs.backport.java.util.Arrays;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc004.Skf2060Sc004GetKariageKohoBukkenCandidateNoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc004.Skf2060Sc004UpdateKariageBukkenExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfShinseiUtils.SkfShinseiUtilsGetApplHistoryInfoByApplNoExp;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc004.Skf2060Sc004GetKariageKohoBukkenCandidateNoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc004.Skf2060Sc004UpdateKariageBukkenExpRepository;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc004.Skf2060Sc004BulkCompleteDto;

/**
 * TestPrjTop画面のSkf2060Sc004BulkCompleteDto（一括完了）サービス処理クラス。　 
 * 
 */
@Service
public class Skf2060Sc004BulkCompleteService extends BaseServiceAbstract<Skf2060Sc004BulkCompleteDto> {
    
    @Autowired
    Skf2060Sc004SharedService skf2060Sc004SharedService;
    @Autowired
    private SkfOperationLogUtils skfOperationLogUtils;
    @Autowired
    private Skf2060Sc004UpdateKariageBukkenExpRepository skf2060Sc004UpdateKariageBukkenExpRepository;
    
    @Autowired
    private Skf2060Sc004GetKariageKohoBukkenCandidateNoExpRepository skf2060KariageBukkenCandidateNoRepository;
    /**
     * サービス処理を行う。　
     * 
     * @param bulkCompDto DTO
     * @return 処理結果
     * @throws Exception 例外
     */
    @SuppressWarnings("unchecked")
    @Override
    public Skf2060Sc004BulkCompleteDto index(Skf2060Sc004BulkCompleteDto bulkCompDto) throws Exception {
        
        // 操作ログを出力する
        skfOperationLogUtils.setAccessLog("一括完了", CodeConstant.C001, bulkCompDto.getPageId());
        // 完了チェックボックスのチェック状態を取得
        List<String> completeChkValList = Arrays.asList(bulkCompDto.getCompleteChkVal());
        
        if (completeChkValList == null || completeChkValList.size() <= 0) {
            // チェックされた完了チェックボックスが存在しない場合は処理終了
            return bulkCompDto;
        }
        
        for (String applNo : completeChkValList) {
            // 処理対象の申請情報を取得する
            SkfShinseiUtilsGetApplHistoryInfoByApplNoExp applInfoExp;
            applInfoExp = skf2060Sc004SharedService.getApplInfo(applNo);
            
            if (applInfoExp == null) {
                // 対応する申請履歴が取得できなかった場合次の処理へ
                break;
            }
            
            // 完了対象の申請書類履歴テーブルのステータスを「完了」に変更する
            skf2060Sc004SharedService.updateApplStatus(applInfoExp, CodeConstant.STATUS_KANRYOU);
            
            //TODO 操作ログに更新件数を出力？
            
            List<String> targetCandidateNo;
            // 選択されなかった借上候補物件番号リストを取得
            targetCandidateNo = this.getCandidateNo(applNo, CodeConstant.BUKKEN_NOT_SELECTED);
            // 選択されなかった借上候補物件の提示フラグを「提示可」に変更
            targetCandidateNo.forEach(candidateNo -> this.updateBukkenTeijiFlg(candidateNo, CodeConstant.TEIJI_FLG_SELECTABLE));
            
            // 選択された借上候補物件番号を取得
            targetCandidateNo = this.getCandidateNo(applNo, CodeConstant.BUKKEN_SELECTED);
            // 選択された借上候補物件の提示フラグを「選択済」に変更
            targetCandidateNo.forEach(candidateNo -> this.updateBukkenTeijiFlg(candidateNo, CodeConstant.TEIJI_FLG_SELECTED));
        }
        return bulkCompDto;
    }

    /**
     * 借上社宅物件番号を取得する
     * @param applNo 借上げ社宅物件番号
     * @param applCheckFlg 選択フラグ（未選択/選択済）
     * @return 借上社宅物件番号のリスト
     */
    private List<String> getCandidateNo(String applNo, String applCheckFlg){
        Skf2060Sc004GetKariageKohoBukkenCandidateNoExpParameter param =
                new Skf2060Sc004GetKariageKohoBukkenCandidateNoExpParameter();
        param.setCompanyCd(CodeConstant.C001);
        param.setApplNo(applNo);
        param.setApplCheckFlg(applCheckFlg);
        
        List<String> resultList = skf2060KariageBukkenCandidateNoRepository.getKariageKohoBukkenCandidateNo(param);
        if (resultList != null) {
            return resultList;
        }else{
            // 値が見つからない場合は空リストを返す
            return new ArrayList<String>();
        }
    }
    
    /**
     * 引数で指定された借上社宅物件番号の提示フラグを、
     * 引数で指定されたものに更新する。
     * 
     * @param candidateNo 借上社宅物件番号
     * @param teijiFlg 提示フラグ
     * @return 更新件数
     */
    private int updateBukkenTeijiFlg(String candidateNo, String teijiFlg){
        Skf2060Sc004UpdateKariageBukkenExpParameter param =
                new Skf2060Sc004UpdateKariageBukkenExpParameter();
        param.setCompanyCd(CodeConstant.C001);
        param.setCandidateNo(candidateNo);
        param.setTeijiFlg(teijiFlg);
        return skf2060Sc004UpdateKariageBukkenExpRepository.updateKariageBukken(param);
    }
    
}
