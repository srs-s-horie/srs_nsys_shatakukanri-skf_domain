/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc004;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc004.Skf3090Sc004ChangeDropDownAsyncDto;

/**
 * Skf3090Sc004ChangeDropDownAsyncService 従業員マスタ一覧ドロップダウンリスト変更時非同期処理クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3090Sc004ChangeDropDownAsyncService
		extends AsyncBaseServiceAbstract<Skf3090Sc004ChangeDropDownAsyncDto> {

	@Autowired
	private Skf3090Sc004SharedService skf3090Sc004SharedService;

	/**
	 * ドロップダウンリスト表示データを生成する。
	 */
	@Override
	public AsyncBaseDto index(Skf3090Sc004ChangeDropDownAsyncDto asyncDto) throws Exception {

		// ドロップダウンリストデータ初期化
		List<Map<String, Object>> companyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> agencyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> affiliation1List = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> affiliation2List = new ArrayList<Map<String, Object>>();

		// ドロップダウン作成
		skf3090Sc004SharedService.getDropDownList(asyncDto.getSelectedCompanyCd(), companyList, asyncDto.getAgencyCd(),
				agencyList, asyncDto.getAffiliation1Cd(), affiliation1List, asyncDto.getAffiliation2Cd(),
				affiliation2List);

		// ドロップダウンリストをDTOにセット
		asyncDto.setCompanyList(companyList);
		asyncDto.setAgencyList(agencyList);
		asyncDto.setAffiliation1List(affiliation1List);
		asyncDto.setAffiliation2List(affiliation2List);

		return asyncDto;
	}

}
