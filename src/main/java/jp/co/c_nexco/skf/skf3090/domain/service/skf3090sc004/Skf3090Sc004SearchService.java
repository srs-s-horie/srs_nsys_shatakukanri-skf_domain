/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc004;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc004.Skf3090Sc004SearchDto;
import jp.co.c_nexco.skf.skf3090.domain.service.common.Skf309030CommonSharedService;

/**
 * Skf3090Sc004SearchService 従業員マスタ一覧検索ボタン押下時処理クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3090Sc004SearchService extends SkfServiceAbstract<Skf3090Sc004SearchDto> {

	@Value("${skf3090.skf3090_sc004.max_search_count}")
	private Integer maxGetRecordCount;

	@Autowired
	private Skf3090Sc004SharedService skf3090Sc004SharedService;
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	
	// 会社コード
	private String companyCd = CodeConstant.C001;

	@Override
	public BaseDto index(Skf3090Sc004SearchDto searchDto) throws Exception {
		
		// 操作ログを出力
		skfOperationLogUtils.setAccessLog("ユーザを検索", companyCd, FunctionIdConstant.SKF3090_SC004);

		// 戻り値に設定するリストのインスタンスを生成
		List<Map<String, Object>> companyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> agencyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> affiliation1List = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> affiliation2List = new ArrayList<Map<String, Object>>();

		// ドロップダウンリストの値を設定
		skf3090Sc004SharedService.getDropDownList(searchDto.getSelectedCompanyCd(), companyList,
				searchDto.getAgencyCd(), agencyList, searchDto.getAffiliation1Cd(), affiliation1List,
				searchDto.getAffiliation2Cd(), affiliation2List);

		// リストテーブルの情報を取得
		int listCount = 0;
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
		listCount = skf3090Sc004SharedService.getListTableData(searchDto.getShainNo(), searchDto.getName(),
				searchDto.getNameKk(), searchDto.getSelectedCompanyCd(), searchDto.getAgencyCd(),
				searchDto.getAffiliation1Cd(), searchDto.getAffiliation2Cd(), listTableData);
		
		searchDto.setSearchFlag(Skf309030CommonSharedService.SEARCH_FLAG_DO);

		// エラーメッセージ設定
		if (listCount == 0) {
			// 取得レコード0件のワーニング
			ServiceHelper.addWarnResultMessage(searchDto, MessageIdConstant.W_SKF_1007, String.valueOf(listCount));
		} else if (listCount > maxGetRecordCount.intValue()) {
			// 取得レコード限界値超のエラー
			searchDto.setSearchFlag(Skf309030CommonSharedService.SEARCH_FLAG_DO_NOT);
			ServiceHelper.addErrorResultMessage(searchDto, null, MessageIdConstant.E_SKF_1046,
					maxGetRecordCount.toString());
			// ServiceHelper.addErrorResultMessage(searchDto, new String[] {
			// "Skf3090Sc004SearchService}" },
			// MessageIdConstant.E_SKF_1046, maxGetRecordCount.toString());
		}

		/** 戻り値をセット */
		// 画面タイトル
		searchDto.setPageTitleKey(MessageIdConstant.SKF3090_SC004_TITLE);
		// ドロップダウン系
		searchDto.setCompanyList(companyList);
		searchDto.setAgencyList(agencyList);
		searchDto.setAffiliation1List(affiliation1List);
		searchDto.setAffiliation2List(affiliation2List);
		// リストテーブル
		searchDto.setListTableData(listTableData);
		// 登録画面遷移用hidden項目
		searchDto.setHdnShainNo(searchDto.getShainNo());
		searchDto.setHdnName(searchDto.getName());
		searchDto.setHdnNameKk(searchDto.getNameKk());
		searchDto.setHdnOriginalCompanyCd(searchDto.getSelectedCompanyCd());
		searchDto.setHdnAgencyCd(searchDto.getAgencyCd());
		searchDto.setHdnAffiliation1Cd(searchDto.getAffiliation1Cd());
		searchDto.setHdnAffiliation2Cd(searchDto.getAffiliation2Cd());
		
		return searchDto;

	}

}
