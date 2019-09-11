/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc004;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.emory.mathcs.backport.java.util.Arrays;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc004.Skf2060Sc004GetKariageKohoBukkenCandidateNoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc004.Skf2060Sc004GetKariageListExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfShinseiUtils.SkfShinseiUtilsGetApplHistoryInfoByApplNoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2060TKariageBukken;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc004.Skf2060Sc004GetKariageKohoBukkenCandidateNoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2060TKariageBukkenRepository;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.common.utils.DateUtils;
import jp.co.c_nexco.nfw.common.utils.LoginUserInfoUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc004.Skf2060Sc004BulkCompleteDto;
import jp.co.c_nexco.skf.skf2060.domain.service.common.Skf206010CommonSendMailService;

/**
 * SKF2060Sc004 借上候補物件状況一覧画面の一括完了サービス処理クラス。　 
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2060Sc004BulkCompleteService extends BaseServiceAbstract<Skf2060Sc004BulkCompleteDto> {
    
    @Autowired
    private MenuScopeSessionBean sessionBean;
    @Autowired
    Skf206010CommonSendMailService skf206010CommonSharedService;
    @Autowired
    Skf2060Sc004SharedService skf2060Sc004SharedService;
    @Autowired
    private SkfOperationLogUtils skfOperationLogUtils;
    @Autowired
    private Skf2060TKariageBukkenRepository skf2060TKariageBukkenRepository;
    @Autowired
    private Skf2060Sc004GetKariageKohoBukkenCandidateNoExpRepository skf2060Sc004GetKariageKohoBukkenCandidateNoExpRepository;
    /**
     * サービス処理を行う。　
     * 
     * @param bulkCompDto DTO
     * @return 処理結果
     * @throws Exception 例外
     */
    @Override
    public Skf2060Sc004BulkCompleteDto index(Skf2060Sc004BulkCompleteDto bulkCompDto) throws Exception {
        
        // 操作ログを出力する
        skfOperationLogUtils.setAccessLog("一括完了", CodeConstant.C001, bulkCompDto.getPageId());
        // 完了チェックボックスのチェック状態を取得
        @SuppressWarnings("unchecked")
        List<String> completeChkValList = Arrays.asList(bulkCompDto.getCompleteChkVal());
        
        if (completeChkValList == null || completeChkValList.size() <= 0) {
            // チェックされた完了チェックボックスが存在しない場合は処理終了
            return bulkCompDto;
        }
        
        for (String applNo : completeChkValList) {
            // 処理対象の申請情報を取得する
            SkfShinseiUtilsGetApplHistoryInfoByApplNoExp applInfoExp;
            applInfoExp = skf206010CommonSharedService.getApplInfo(applNo);
            
            if (applInfoExp == null) {
                // 対応する申請履歴が取得できなかった場合次の処理へ
                break;
            }
            try{
                // 楽観的排他チェック
                super.checkLockException(bulkCompDto.getLastUpdateDate(bulkCompDto.UPDATE_TABLE_PREFIX_APPL_HIST + applInfoExp.getApplNo()),
                        applInfoExp.getUpdateDate());
                // 完了対象の申請書類履歴テーブルのステータスを「完了」に変更する
                skf2060Sc004SharedService.updateApplStatus(applInfoExp, CodeConstant.STATUS_KANRYOU,
                        DateUtils.getSysDate(), LoginUserInfoUtils.getUserName());

                List<String> targetCandidateNo;
                // 選択されなかった借上候補物件番号リストを取得
                targetCandidateNo = this.getCandidateNo(applNo, CodeConstant.BUKKEN_NOT_SELECTED);
                // 選択されなかった借上候補物件の提示フラグを「提示可」に変更
                targetCandidateNo.forEach(candidateNo -> this.updateBukkenTeijiFlg(candidateNo, CodeConstant.TEIJI_FLG_SELECTABLE));
                
                // 選択された借上候補物件番号を取得
                targetCandidateNo = this.getCandidateNo(applNo, CodeConstant.BUKKEN_SELECTED);
                // 選択された借上候補物件の提示フラグを「選択済」に変更s
                targetCandidateNo.forEach(candidateNo -> this.updateBukkenTeijiFlg(candidateNo, CodeConstant.TEIJI_FLG_SELECTED));
            }catch(Exception e){
                // 排他チェックエラーとなった場合、エラーメッセージを表示して次の処理へ
                ServiceHelper.addErrorResultMessage(bulkCompDto, null, MessageIdConstant.E_SKF_1134, "社員番号:" + applInfoExp.getShainNo());
            }
        }
        
        // 検索ボタン押下時の検索条件をセッションから取得
        Skf2060Sc004GetKariageListExpParameter param;
        param = (Skf2060Sc004GetKariageListExpParameter) sessionBean.get(SessionCacheKeyConstant.SKF2060SC004_SEARCH_COND_SESSION_KEY);
        //再検索実行
        bulkCompDto.setListTableData(skf2060Sc004SharedService.getListTableData(param, bulkCompDto));
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
        
        List<String> resultList = skf2060Sc004GetKariageKohoBukkenCandidateNoExpRepository.getKariageKohoBukkenCandidateNo(param);
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
        if (StringUtils.isEmpty(candidateNo)) {
            // 借上社宅物件番号がnullまたは空である場合処理を中断
            return 0;
        }
        
        Skf2060TKariageBukken record = new Skf2060TKariageBukken();
        record.setCompanyCd(CodeConstant.C001);
        record.setCandidateNo( Long.valueOf(candidateNo) );
        record.setTeijiFlg(teijiFlg);
        
        return skf2060TKariageBukkenRepository.updateByPrimaryKeySelective(record);
    }
    
}
