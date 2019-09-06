/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc007;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc007.Skf3090Sc007ChangeDropDownAsyncDto;

/**
 * Skf3090Sc007ChangeDropDownAsyncService 組織マスタ登録ドロップダウンリスト変更時非同期処理クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3090Sc007ChangeDropDownAsyncService
		extends AsyncBaseServiceAbstract<Skf3090Sc007ChangeDropDownAsyncDto> {

	@Autowired
	private SkfDropDownUtils skfDropDownUtils;

	/**
	 * ドロップダウンリスト表示データを生成する。
	 */
	@Override
	public AsyncBaseDto index(Skf3090Sc007ChangeDropDownAsyncDto asyncDto) throws Exception {

		// 「会社」ドロップダウンの設定
		List<Map<String, Object>> companyList = new ArrayList<Map<String, Object>>();
		companyList = skfDropDownUtils.getDdlCompanyByCd(asyncDto.getRegistCompanyCd(), true);
		asyncDto.setCompanyList(companyList);

		// 「事業領域」ドロップダウンの設定
		List<Map<String, Object>> businessAreaList = new ArrayList<Map<String, Object>>();
		businessAreaList = skfDropDownUtils.getDdlBusinessAreaByCd(asyncDto.getRegistCompanyCd(),
				asyncDto.getRegistAgencyCd(), asyncDto.getRegistBusinessAreaCd(), true);
		asyncDto.setBusinessAreaList(businessAreaList);

		return asyncDto;

	}

}
