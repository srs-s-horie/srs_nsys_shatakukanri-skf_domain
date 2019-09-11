/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc004;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc004.Skf2060Sc004GetKariageListExpParameter;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc004.Skf2060Sc004SearchDto;

/**
 * SKF2060Sc004 借上候補物件状況一覧画面の検索サービス処理クラス。　 
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2060Sc004SearchService extends BaseServiceAbstract<Skf2060Sc004SearchDto> {
    
    @Autowired
    private MenuScopeSessionBean sessionBean;
    @Autowired
    Skf2060Sc004SharedService skf2060Sc004SharedService;
    @Autowired
    private SkfOperationLogUtils skfOperationLogUtils;
    @Value("${skf2060.skf2060_sc004.search_max_count}")
    private String searchMaxCount;
    @Value("${skf.common.validate_error}")
    private String validationErrorCode;

    /**
     * サービス処理を行う。　
     * 
     * @param searchDto
     *            インプットDTO
     * @return 処理結果
     * @throws Exception
     *             例外
     */
    @Override
    public Skf2060Sc004SearchDto index(Skf2060Sc004SearchDto searchDto) throws Exception {
        // 操作ログを出力する
        skfOperationLogUtils.setAccessLog("検索", CodeConstant.C001, searchDto.getPageId());
        
        boolean isErr = this.execInputCheck(searchDto);
        if(isErr){
            return searchDto;
        }
        // 検索実行
        List<Map<String, Object>> resultList = skf2060Sc004SharedService.getListTableData(setSearchParam(searchDto));
        
        if (resultList == null || resultList.size() == 0) {
            // 検索結果0件
            ServiceHelper.addWarnResultMessage(searchDto, MessageIdConstant.W_SKF_1007);
        } else if (resultList.size() > Integer.parseInt(searchMaxCount)) {
            // 検索結果表示最大数を超過
            ServiceHelper.addWarnResultMessage(searchDto, MessageIdConstant.W_SKF_1002, searchMaxCount, "抽出条件を変更してください。");
            // リストの表示内容をクリアする
            resultList = new ArrayList<Map<String, Object>>();
        }
        
        searchDto.setListTableData(resultList);
        searchDto.setListTableMaxRowCount(searchMaxCount);
        
        return searchDto;
    }
    
    private boolean execInputCheck(Skf2060Sc004SearchDto dto) throws ParseException{
        
        // 既存エラー情報クリア
        dto.setCandidateDateFromErr("");
        dto.setCandidateDateToErr("");
        dto.setCandidatePersonNameErr("");
        dto.setShatakuNameErr("");
        dto.setShatakuAddressNameErr("");
        dto.setCandidateStatusErr("");
        
        boolean isErr = false;
        /** 単項目チェック */
        // 提示日FROM
        if (!CheckUtils.isEmpty(dto.getCandidateDateFrom())
                && !SkfCheckUtils.isSkfDateFormat(dto.getCandidateDateFrom(),CheckUtils.DateFormatType.YYYYMMDD) ) {
            //フォーマットチェックエラー、日付妥当性チェックエラー時
            ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1055, "提示日FROM");
            dto.setCandidateDateFromErr(validationErrorCode);
            isErr = true;
        }
        // 提示日TO
        if (!CheckUtils.isEmpty(dto.getCandidateDateTo())
                && !SkfCheckUtils.isSkfDateFormat(dto.getCandidateDateTo(),CheckUtils.DateFormatType.YYYYMMDD) ) {
            //フォーマットチェックエラー、日付妥当性チェックエラー時
            ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1055, "提示日TO");
            dto.setCandidateDateToErr(validationErrorCode);
            isErr = true;
        }
        
        /** 関連項目チェック  */
        // 申請日のFrom < To整合性
        Date fromDate = null;
        Date toDate = null;
        int diff = 0;
        if (!CheckUtils.isEmpty(dto.getCandidateDateFrom())
                && !CheckUtils.isEmpty(dto.getCandidateDateTo()) ) {
            SimpleDateFormat sdf = new SimpleDateFormat(SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT);
            fromDate = sdf.parse(dto.getCandidateDateFrom());
            toDate = sdf.parse(dto.getCandidateDateTo());

            diff = fromDate.compareTo(toDate);
            if (diff > 0) {
                ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1133, "提示日");
                dto.setCandidateDateFromErr(validationErrorCode);
                dto.setCandidateDateToErr(validationErrorCode);
                isErr = true;
            }
        }
        // 申請ステータス
        if( dto.getCandidateStatus() == null || dto.getCandidateStatus().length <= 0 ){
            // 申請ステータスが全て未選択の場合
            ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1054, "提示状況");
            dto.setCandidateStatusErr(validationErrorCode);
            isErr = true;
        }
        return isErr;
    }
    
    /**
     * 検索条件設定
     * @param dto 初期表示時検索条件Dto
     * @return 借上社宅一覧データ取得SQLパラメータクラス
     */
    private Skf2060Sc004GetKariageListExpParameter setSearchParam(Skf2060Sc004SearchDto dto){
        Skf2060Sc004GetKariageListExpParameter param = new Skf2060Sc004GetKariageListExpParameter();
        
        param.setCandidateDateFrom(dto.getCandidateDateFrom());
        param.setCandidateDateTo(dto.getCandidateDateTo());
        param.setCandidatePersonNo(dto.getCandidatePersonNo());
        param.setShatakuName(dto.getShatakuName());
        param.setShatakuAddressName(dto.getShatakuAddressName());
        param.setApplStatus(Arrays.asList(dto.getCandidateStatus()));
        
        // 検索ボタン押下時の検索条件をセッションに格納
        sessionBean.put(SessionCacheKeyConstant.SKF2060SC004_SEARCH_COND_SESSION_KEY, param);
        
        return param;
    }
}
