/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc006;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc006.Skf3090Sc006GetSoshikiInfoExp;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc006.Skf3090Sc006SearchDto;

/**
 * Skf3090Sc006SearchService 組織マスタ一覧検索ボタン押下時処理クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3090Sc006SearchService extends SkfServiceAbstract<Skf3090Sc006SearchDto> {

	@Value("${skf3090.skf3090_sc006.max_search_count}")
	private Integer maxGetRecordCount;

	@Autowired
	private SkfDropDownUtils skfDropDownUtils;

	@Autowired
	private Skf3090Sc006SharedService skf3090Sc006SharedService;

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	@Override
	public BaseDto index(Skf3090Sc006SearchDto searchDto) throws Exception {

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("組織を検索", CodeConstant.C001, searchDto.getPageId());

		// 「会社」ドロップダウンリストの設定
		List<Map<String, Object>> companyList = new ArrayList<Map<String, Object>>();
		companyList = skfDropDownUtils.getDdlCompanyByCd(searchDto.getCompanyCd(), true);
		searchDto.setCompanyList(companyList);
		// 組織登録画面から遷移時、ドロップダウンリストを設定
		int createTableListCount = setListValue(searchDto);

		// エラーメッセージ
		if (createTableListCount == 0) {
			// 検索結果が0件の時のエラーメッセージ
			ServiceHelper.addWarnResultMessage(searchDto, MessageIdConstant.W_SKF_1007, String.valueOf(searchDto));
		} else if (createTableListCount > maxGetRecordCount.intValue()) {
			// 検索結果が最大表示件数を超えた時のエラーメッセージ
			ServiceHelper.addErrorResultMessage(searchDto, null, MessageIdConstant.E_SKF_1046,
					maxGetRecordCount.toString());
			searchDto.setCreateTableList(new ArrayList<Map<String, Object>>());
		}

		/** 戻り値をセット */

		// 画面タイトル
		searchDto.setPageTitleKey(MessageIdConstant.SKF3090_SC006_TITLE);

		return searchDto;

	}

	private int setListValue(Skf3090Sc006SearchDto searchDto) {
		String companyCd = searchDto.getCompanyCd();
		String agencyCd = searchDto.getAgencyCd();
		String affiliation1Cd = searchDto.getAffiliation1Cd();
		String affiliation2Cd = searchDto.getAffiliation2Cd();
		String businessAreaCd = searchDto.getBusinessAreaCd();

		// 「機関」ドロップダウンリストを設定
		List<Map<String, Object>> agencyList = new ArrayList<Map<String, Object>>();
		agencyList = skfDropDownUtils.getDdlAgencyByCd(companyCd, agencyCd, true);
		searchDto.setAgencyList(agencyList);

		// 「部等」ドロップダウンリストを設定
		List<Map<String, Object>> affiliation1List = new ArrayList<Map<String, Object>>();
		affiliation1List = skfDropDownUtils.getDdlAffiliation1ByCd(companyCd, agencyCd, affiliation1Cd, true);
		searchDto.setAffiliation1List(affiliation1List);

		// 「室、チーム又は課」ドロップダウンリストを設定
		List<Map<String, Object>> affiliation2List = new ArrayList<Map<String, Object>>();
		affiliation2List = skfDropDownUtils.getDdlAffiliation2ByCd(companyCd, agencyCd, affiliation1Cd, affiliation2Cd,
				true);
		searchDto.setAffiliation2List(affiliation2List);

		// 「事業領域」ドロップダウンリストを設定
		List<Map<String, Object>> businessAreaList = new ArrayList<Map<String, Object>>();
		businessAreaList = skfDropDownUtils.getDdlBusinessAreaByCd(companyCd, agencyCd, businessAreaCd, true);
		searchDto.setBusinessAreaList(businessAreaList);

		// 画面データの設定
		List<Skf3090Sc006GetSoshikiInfoExp> soshikiInfoList = skf3090Sc006SharedService.getSoshikiInfo(companyCd,
				agencyCd, affiliation1Cd, affiliation2Cd, businessAreaCd);

		// リストテーブルに組織情報を表示
		List<Map<String, Object>> createTableList = new ArrayList<Map<String, Object>>();
		createTableList = skf3090Sc006SharedService.createListTableData(soshikiInfoList);
		searchDto.setCreateTableList(createTableList);

		return createTableList.size();

	}

}
