/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc004;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc004.Skf2060Sc004GetKariageListExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc004.Skf2060Sc004GetKariageListExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfShinseiUtils.SkfShinseiUtilsGetApplHistoryInfoByApplNoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplHistory;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc004.Skf2060Sc004GetKariageListExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TApplHistoryRepository;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060Sc004common.Skf2060Sc004CommonDto;


/**
 * SKF2060Sc004 借上候補物件状況一覧画面のサービス共通処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2060Sc004SharedService {

    //Repository
    @Autowired
    private Skf2060Sc004GetKariageListExpRepository skf2060Sc004GetKariageListExpRepository;
    @Autowired
    private Skf2010TApplHistoryRepository skf2010TApplHistoryRepository;
    
    //Utils
    @Autowired
    private SkfGenericCodeUtils skfGenericCodeUtils;
    
    /**
     * 借上候補物件状況一覧を取得して、画面のlistTableに表示するための情報を作成する。
     * @param param
     * @return listTableに表示する情報を格納したList
     */
    public <DTO extends Skf2060Sc004CommonDto> List<Map<String, Object>> getListTableData(
            Skf2060Sc004GetKariageListExpParameter param, DTO dto ){
        List<Map<String, Object>> tableDataList = new ArrayList<Map<String, Object>>();
        
        // 検索を実行
        List<Skf2060Sc004GetKariageListExp> kariageExpList  = this.searchKariageList(param);
        
        Map<String, Date> lastUpdateDateMap = new HashMap<String, Date>();
        
        // 提示状況汎用コード取得
        Map<String, String> candidateStatusGenCodeMap = new HashMap<String, String>();
        candidateStatusGenCodeMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_STATUS);
        
        // 同意しない理由区分汎用コード取得
        Map<String, String> riyuGenCodeMap = new HashMap<String, String>();
        riyuGenCodeMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_NO_AGREE_REASON);
        
        SimpleDateFormat sdf = new SimpleDateFormat(SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
        for (int i = 0; i < kariageExpList.size(); i++) {
            Skf2060Sc004GetKariageListExp tmpData = kariageExpList.get(i);
            // 排他チェック用に最終更新日を取得
            lastUpdateDateMap.put(dto.UPDATE_TABLE_PREFIX_APPL_HIST + tmpData.getApplNo(), tmpData.getLastUpdateDate());
            
            Map<String, Object> tmpMap = new HashMap<String, Object>();
            
            // 完了チェックボックス非活性制御
            String completeChkDisabled = "";
            // 督促チェックボックス非活性制御
            String reminderChkDisabled = "";
            
            // 完了、督促、再提示ボタン
            if( !StringUtils.isEmpty(tmpData.getCandidateStatus()) ){
                if( tmpData.getCandidateStatus().equals(CodeConstant.STATUS_SENTAKU_ZUMI)
                        || tmpData.getCandidateStatus().equals(CodeConstant.STATUS_SENTAKU_SHINAI)
                ){
                    // ステータスが「選択済」または「選択しない」である場合
                    // 再提示ボタンを表示する
                    
                    tmpMap.put("recandidate", "");
                }
                
                if( tmpData.getCandidateStatus().equals(CodeConstant.STATUS_KAKUNIN_IRAI)
                        || tmpData.getCandidateStatus().equals(CodeConstant.STATUS_KANRYOU) ){
                    // ステータスが「確認依頼」または「完了」である場合
                    // 完了チェックボックスを非活性とする
                    completeChkDisabled = "disabled";
                }
                
                if( !tmpData.getCandidateStatus().equals(CodeConstant.STATUS_KAKUNIN_IRAI) ){
                    // ステータスが「確認依頼」でない場合
                    // 督促チェックボックスを非活性とする
                    reminderChkDisabled = "disabled";
                }
            }
            String applNo = tmpData.getApplNo();
            tmpMap.put("completeChk", "<INPUT type='checkbox' name='completeChkVal' id='completeChkVal" + i + "'"
                       + " value='" + applNo + "' " + completeChkDisabled + ">");
            tmpMap.put("reminderChk", "<INPUT type='checkbox' name='reminderChkVal' id='reminderChkVal" + i + "'"
                       + " value='" + applNo + "' " + reminderChkDisabled + ">");
    
            // 提示状況表示文言を汎用コードから取得
            String candidateStatus = candidateStatusGenCodeMap.get(tmpData.getCandidateStatus());
            tmpMap.put("candidateStatus", HtmlUtils.htmlEscape(candidateStatus));
            // 提示日をyyyy/MM/dd形式で表示
            tmpMap.put("candidateDate", HtmlUtils.htmlEscape(sdf.format(tmpData.getCandidateDate()).toString()));
            tmpMap.put("candidatePersonNo", HtmlUtils.htmlEscape(tmpData.getCandidatePersonNo()));
            tmpMap.put("candidatePersonName", HtmlUtils.htmlEscape(tmpData.getCandidatePersonName()));
            tmpMap.put("shatakuName", HtmlUtils.htmlEscape(tmpData.getShatakuName()));
            tmpMap.put("shatakuAddress", HtmlUtils.htmlEscape(tmpData.getShatakuAddressName()));
            
            // 備考
            if( !StringUtils.isEmpty(tmpData.getRiyu()) ){
                if(tmpData.getRiyu().equals(CodeConstant.RELATION_OTHERS) ){
                    // 選択しない理由が「99:その他」の場合
                    tmpMap.put("biko", HtmlUtils.htmlEscape("その他：" + tmpData.getBiko()));
                }else{
                    // 選択しない理由が「99:その他」以外の場合
                    // 理由区分文言を汎用コードから取得
                    String riyuStr = riyuGenCodeMap.get(tmpData.getRiyu());
                    tmpMap.put("biko", HtmlUtils.htmlEscape(riyuStr));
                }
            }

            tmpMap.put("confirm", "");
            // 隠し項目
            tmpMap.put("applNo", tmpData.getApplNo());
            tmpMap.put("applId", tmpData.getApplId());
            tmpMap.put("applStatusCd", tmpData.getCandidateStatus());
            tableDataList.add(tmpMap);
        }
        
        dto.setLastUpdateDateMap(lastUpdateDateMap);
        return tableDataList;
    }
    
    /**
     * 借上候補物件状況一覧情報を取得する。
     * @param param
     * @return 借上候補物件状況一覧Exp
     */
    public List<Skf2060Sc004GetKariageListExp> searchKariageList(
            Skf2060Sc004GetKariageListExpParameter param) {
        List<Skf2060Sc004GetKariageListExp> kariageExpList = new ArrayList<Skf2060Sc004GetKariageListExp>();
        kariageExpList = skf2060Sc004GetKariageListExpRepository.getKariageList(param);

        return kariageExpList;
    }
    
    /**
     * 申請履歴情報の更新
     * @param inputExp 検索キー情報を保持したExp
     * @param status 更新するステータス
     * @param agreDate 承認日付
     * @param agreUserName 承認者名
     * @return 更新件数
     */
    public int updateApplStatus(SkfShinseiUtilsGetApplHistoryInfoByApplNoExp inputExp, String status,
            Date agreDate, String agreUserName) {
        Skf2010TApplHistory record = new Skf2010TApplHistory();
        // キー情報
        record.setCompanyCd(inputExp.getCompanyCd());
        record.setShainNo(inputExp.getShainNo());
        record.setApplDate(inputExp.getApplDate());
        record.setApplNo(inputExp.getApplNo());
        record.setApplId(inputExp.getApplId());

        // ステータス更新(完了とする)
        record.setApplStatus(status);
        // 承認日付
        record.setAgreDate(agreDate);
        // 承認者名１
        record.setAgreName1(agreUserName);
        // 最終更新日付
        record.setLastUpdateDate(inputExp.getLastUpdateDate());

        // DB更新実行
        return skf2010TApplHistoryRepository.updateByPrimaryKeySelective(record);
    }

    


}
