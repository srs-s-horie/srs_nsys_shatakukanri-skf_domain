/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc004;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc004.Skf2060Sc004GetKariageListExpParameter;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc004.Skf2060Sc004SearchDto;

/**
 * TestPrjTop画面のSearchサービス処理クラス。　 
 * 
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
        // 検索実行
        searchDto.setListTableData(skf2060Sc004SharedService.getListTableData(setSearchParam(searchDto)));
        searchDto.setListTableMaxRowCount(searchMaxCount);
		return searchDto;
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
        param.setCandidatePersonName(dto.getCandidatePersonName());
        param.setShatakuName(dto.getShatakuName());
        param.setShatakuAddressName(dto.getShatakuAddressName());
        param.setApplStatus(Arrays.asList(dto.getCandidateStatus()));
        
        // 検索ボタン押下時の検索条件をセッションに格納
        sessionBean.put(SessionCacheKeyConstant.SKF2060SC004_SEARCH_COND_SESSION_KEY, param);
        
        return param;
    }
}
