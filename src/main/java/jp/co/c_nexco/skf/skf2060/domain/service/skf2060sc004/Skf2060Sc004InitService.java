/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc004;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc004.Skf2060Sc004GetKariageListExpParameter;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationGuideUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc004.Skf2060Sc004InitDto;

/**
 * SKF2060Sc004 借上候補物件状況一覧画面の初期表示サービス処理クラス。　 
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2060Sc004InitService extends BaseServiceAbstract<Skf2060Sc004InitDto> {
    
    @Autowired
    private MenuScopeSessionBean sessionBean;
    @Autowired
    private SkfOperationGuideUtils skfOperationGuideUtils;
    @Autowired
    private Skf2060Sc004SharedService skf2060Sc004SharedService;
    @Autowired
    private SkfOperationLogUtils skfOperationLogUtils;
    
    @Value("${skf2060.skf2060_sc004.search_max_count}")
    private String searchMaxCount;
    
    /**
     * サービス処理を行う。　
     * 
     * @param initDto インプットDTO
     * @return 処理結果
     * @throws Exception  例外
     */
    @Override
    public Skf2060Sc004InitDto index(Skf2060Sc004InitDto initDto) throws Exception {
        
        initDto.setPageTitleKey(MessageIdConstant.SKF2060_SC004_TITLE);
        
        // 操作ログを出力する
        skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, initDto.getPageId());
        // リストチェック状態を解除
        initDto.setCompleteChkVal(null);
        initDto.setReminderChkVal(null);

        // 初期表示情報を取得
        this.setInitInfo(initDto);

        // 初期検索実行
        initDto.setListTableData(skf2060Sc004SharedService.getListTableData(setDefaultSearchParam(initDto), initDto));
        initDto.setListTableMaxRowCount(searchMaxCount);
        return initDto;
    }
    
    /**
     * 初期表示時の画面情報をDtoに設定する
     * @param initDto
     */
    private void setInitInfo(Skf2060Sc004InitDto initDto){
        
        // セッションに保持された検索条件が無いかを検索
        Skf2060Sc004GetKariageListExpParameter sessionParam;
        sessionParam = (Skf2060Sc004GetKariageListExpParameter) sessionBean.get(SessionCacheKeyConstant.SKF2060SC004_SEARCH_COND_SESSION_KEY);
        
        if (sessionParam != null) {
            // セッションから検索条件パラメータが取得できた場合、パラメータから画面に表示する情報を復元
            initDto.setCandidateDateFrom( sessionParam.getCandidateDateFrom() );
            initDto.setCandidateDateTo( sessionParam.getCandidateDateTo() );
            initDto.setCandidatePersonNo( sessionParam.getCandidatePersonNo() );
            initDto.setShatakuName( sessionParam.getShatakuName() );
            initDto.setShatakuAddressName( sessionParam.getShatakuAddressName() );
            
            List<String> applStatusList = sessionParam.getApplStatus();
            if (applStatusList != null && applStatusList.size() > 0) {
                initDto.setCandidateStatus( applStatusList.toArray(new String[applStatusList.size()]) );
            }
        }else{
            // セッションから検索条件パラメータが取得できなかった場合、デフォルト検索条件を設定
            // 提示日From
            DateTimeFormatter format = DateTimeFormatter.ofPattern(SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
            LocalDate currentMonthFirstDate = LocalDate.now().withDayOfMonth(1); //当月初日を初期選択
            initDto.setCandidateDateFrom(currentMonthFirstDate.format(format));
            // 提示状況
            List<String> defaultStatusList = getDefaultCandidateStatusList(); // チェックボックスの初期値セット
            initDto.setCandidateStatus(defaultStatusList.toArray(new String[defaultStatusList.size()]));
        }

        // 操作ガイド取得
        initDto.setOperationGuide(skfOperationGuideUtils.getOperationGuide(initDto.getPageId()));

        // 画面ID保持
        initDto.setPrePageId(FunctionIdConstant.SKF2060_SC004);

    }
    
    /**
     * 借上げ候補物件一覧画面の初期表示時にチェック済とする提示状況をリストで返す
     * @return
     */
    private List<String> getDefaultCandidateStatusList(){
        List<String> initStats = new ArrayList<String>();
        // ステータス：確認依頼
        initStats.add(CodeConstant.STATUS_KAKUNIN_IRAI);
        // ステータス：選択済
        initStats.add(CodeConstant.STATUS_SENTAKU_ZUMI);
        // ステータス：選択しない
        initStats.add(CodeConstant.STATUS_SENTAKU_SHINAI);
        return initStats;
    }
    
    /**
     * 初期表示時 検索条件設定
     * @param dto 初期表示時検索条件Dto
     * @return 借上社宅一覧データ取得SQLパラメータクラス
     */
    private Skf2060Sc004GetKariageListExpParameter setDefaultSearchParam(Skf2060Sc004InitDto dto){
        Skf2060Sc004GetKariageListExpParameter param = new Skf2060Sc004GetKariageListExpParameter();

        param.setCandidateDateFrom( dto.getCandidateDateFrom());
        param.setCandidateDateTo(dto.getCandidateDateTo());
        param.setCandidatePersonNo(dto.getCandidatePersonNo());
        param.setShatakuName(dto.getShatakuName());
        param.setShatakuAddressName(dto.getShatakuAddressName());
        param.setApplStatus(Arrays.asList(dto.getCandidateStatus()));
        
        // 初期検索条件をセッションに格納
        sessionBean.put(SessionCacheKeyConstant.SKF2060SC004_SEARCH_COND_SESSION_KEY, param);
        
        return param;
    }
}
