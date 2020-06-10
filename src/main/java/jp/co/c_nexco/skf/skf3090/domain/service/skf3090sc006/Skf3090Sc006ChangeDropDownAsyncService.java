/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc006;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.skf.common.SkfAsyncServiceAbstract;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc006.Skf3090Sc006ChangeDropDownAsyncDto;

/**
 * Skf3090Sc006ChangeDropDownAsyncService 組織マスタ一覧ドロップダウンリスト変更時非同期処理クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3090Sc006ChangeDropDownAsyncService
		extends SkfAsyncServiceAbstract<Skf3090Sc006ChangeDropDownAsyncDto> {

	@Autowired
	private SkfDropDownUtils skfDropDownUtils;

	/**
	 * ドロップダウンリスト表示データを生成する。
	 */
	@Override
	public AsyncBaseDto index(Skf3090Sc006ChangeDropDownAsyncDto asyncDto) throws Exception {

		// 「会社」ドロップダウンの設定
		List<Map<String, Object>> companyList = new ArrayList<Map<String, Object>>();
		companyList = skfDropDownUtils.getDdlCompanyByCd(asyncDto.getCompanyCd(), true);
		asyncDto.setCompanyList(companyList);
		setListValue(asyncDto);

		return asyncDto;

	}

	private void setListValue(Skf3090Sc006ChangeDropDownAsyncDto asyncDto) {
		String companyCd = asyncDto.getCompanyCd();
		String agencyCd = asyncDto.getAgencyCd();
		String affiliation1Cd = asyncDto.getAffiliation1Cd();
		String affiliation2Cd = asyncDto.getAffiliation2Cd();
		String businessAreaCd = asyncDto.getBusinessAreaCd();

		// 「機関」ドロップダウンリストの設定
		List<Map<String, Object>> agencyList = new ArrayList<Map<String, Object>>();
		agencyList = skfDropDownUtils.getDdlAgencyByCd(companyCd, agencyCd, true);
		asyncDto.setAgencyList(agencyList);

		// 「部等」ドロップダウンの設定
		List<Map<String, Object>> affiliation1List = new ArrayList<Map<String, Object>>();
		affiliation1List = skfDropDownUtils.getDdlAffiliation1ByCd(companyCd, agencyCd, affiliation1Cd, true);
		asyncDto.setAffiliation1List(affiliation1List);

		// 「室・課等」ドロップダウンの設定
		List<Map<String, Object>> affiliation2List = new ArrayList<Map<String, Object>>();
		affiliation2List = skfDropDownUtils.getDdlAffiliation2ByCd(companyCd, agencyCd, affiliation1Cd, affiliation2Cd,
				true);
		asyncDto.setAffiliation2List(affiliation2List);

		// 「事業領域」ドロップダウンの設定
		List<Map<String, Object>> businessAreaList = new ArrayList<Map<String, Object>>();
		businessAreaList = skfDropDownUtils.getDdlBusinessAreaByCd(companyCd, agencyCd, businessAreaCd, true);
		asyncDto.setBusinessAreaList(businessAreaList);

	}

}
