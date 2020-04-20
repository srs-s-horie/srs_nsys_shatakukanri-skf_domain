/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc005;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.skf.common.SkfAsyncServiceAbstract;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc005.Skf3090Sc005ChangeDropDownAsyncDto;

/**
 * ドロップダウンリスト選択時に非同期で処理されるクラス.
 * 
 * @author s.hirai
 *
 */
@Service
public class Skf3090Sc005ChangeDropDownAsyncService
		extends SkfAsyncServiceAbstract<Skf3090Sc005ChangeDropDownAsyncDto> {

	@Autowired
	private Skf3090Sc005SharedService skf3090Sc005SharedService;

	/**
	 * 最初に呼び出されるメソッド.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public AsyncBaseDto index(Skf3090Sc005ChangeDropDownAsyncDto asyncDto) throws Exception {

		// ドロップダウンリスト用リストのインスタンス生成
		List<Map<String, Object>> companyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> agencyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> affiliation1List = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> affiliation2List = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> businessAreaList = new ArrayList<Map<String, Object>>();

		// ドロップダウンリストを取得
		Map<String, Object> returnMap = skf3090Sc005SharedService.getDropDownLists(asyncDto.getOriginalCompanyCd(),
				asyncDto.getAgencyCd(), asyncDto.getAffiliation1Cd(), asyncDto.getAffiliation2Cd(),
				asyncDto.getBusinessAreaCd());

		// 画面表示するドロップダウンリストを設定
		companyList.addAll((List<Map<String, Object>>) returnMap.get(Skf3090Sc005SharedService.KEY_COMPANY_LIST));
		agencyList.addAll((List<Map<String, Object>>) returnMap.get(Skf3090Sc005SharedService.KEY_AGENCY_LIST));
		affiliation1List
				.addAll((List<Map<String, Object>>) returnMap.get(Skf3090Sc005SharedService.KEY_AFFILIATION1_LIST));
		affiliation2List
				.addAll((List<Map<String, Object>>) returnMap.get(Skf3090Sc005SharedService.KEY_AFFILIATION2_LIST));
		businessAreaList
				.addAll((List<Map<String, Object>>) returnMap.get(Skf3090Sc005SharedService.KEY_BUSINESS_AREA_LIST));

		// Dtoに戻り値をセット
		asyncDto.setCompanyList(companyList);
		asyncDto.setAgencyList(agencyList);
		asyncDto.setAffiliation1List(affiliation1List);
		asyncDto.setAffiliation2List(affiliation2List);
		asyncDto.setBusinessAreaList(businessAreaList);

		return asyncDto;
	}

}
