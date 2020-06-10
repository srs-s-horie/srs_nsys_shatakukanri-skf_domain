/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc005;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShain;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShainKey;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MShainRepository;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;

/**
 * Skf3090Sc005SharedService 従業員マスタ登録内共通クラス.
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3090Sc005SharedService {

	@Autowired
	private SkfDropDownUtils ddlUtils;

	// 戻り値Map用定数
	public static final String KEY_COMPANY_LIST = "COMPANY_LIST";
	public static final String KEY_AGENCY_LIST = "AGENCY_LIST";
	public static final String KEY_AFFILIATION1_LIST = "AFFILIATION1_LIST";
	public static final String KEY_AFFILIATION2_LIST = "AFFILIATION2_LIST";
	public static final String KEY_BUSINESS_AREA_LIST = "BUSINESS_AREA_LIST";

	// 最終更新日付のキャッシュキー
	public static final String KEY_LAST_UPDATE_DATE = "skf1010_m_shainUpdateDate";

	@Autowired
	private Skf1010MShainRepository skf1010MShainRepository;

	/**
	 * ドロップダウンリストを取得する。
	 * 
	 * @param originalCompanyCd 原籍会社コード
	 * @param agencyCd 機関コード
	 * @param affiliation1Cd 部等コード
	 * @param affiliation2Cd 室・課等コード
	 * @param businessAreaCd 事業領域コード
	 * @return 以下を含むMap<br>
	 *         原籍会社ドロップダウンリスト（中日本、高速道路総合研究所限定）、機関ドロップダウンリスト、部等ドロップダウンリスト、室・課等ドロップダウンリスト、事業領域ドロップダウンリスト
	 */
	public Map<String, Object> getDropDownLists(String originalCompanyCd, String agencyCd, String affiliation1Cd,
			String affiliation2Cd, String businessAreaCd) {

		// 戻り値用Mapのインスタンス生成
		Map<String, Object> returnMap = new HashMap<String, Object>();

		/** ドロップダウンリストの取得 */
		// 原籍会社ドロップダウンリスト
		List<Map<String, Object>> companyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> tempCompanyList = ddlUtils.getDdlCompanyByCd(originalCompanyCd, true);
		// 「NEXCO中日本」と「高速道路総合研究所」に絞る
		for (Map<String, Object> companyMap : tempCompanyList) {
			String companyValue = (String) companyMap.get("value");
			if (CodeConstant.DOUBLE_QUOTATION.equals(companyValue) || CodeConstant.C001.equals(companyValue)
					|| CodeConstant.R001.equals(companyValue)) {
				// ブランク行か「NEXCO中日本」か「高速道路総合研究所」の場合、Mapに詰めるリストにセット
				companyList.add(companyMap);
			}
		}

		returnMap.put(KEY_COMPANY_LIST, companyList);

		// 機関ドロップダウンリスト
		List<Map<String, Object>> agencyList = new ArrayList<Map<String, Object>>();
		agencyList.addAll(ddlUtils.getDdlAgencyByCd(CodeConstant.C001, agencyCd, true));
		returnMap.put(KEY_AGENCY_LIST, agencyList);

		// 部等ドロップダウンリスト
		List<Map<String, Object>> affiliation1List = new ArrayList<Map<String, Object>>();
		affiliation1List.addAll(ddlUtils.getDdlAffiliation1ByCd(CodeConstant.C001, agencyCd, affiliation1Cd, true));
		returnMap.put(KEY_AFFILIATION1_LIST, affiliation1List);

		// 室・課等ドロップダウンリスト
		List<Map<String, Object>> affiliation2List = new ArrayList<Map<String, Object>>();
		affiliation2List.addAll(
				ddlUtils.getDdlAffiliation2ByCd(CodeConstant.C001, agencyCd, affiliation1Cd, affiliation2Cd, true));
		returnMap.put(KEY_AFFILIATION2_LIST, affiliation2List);

		// TODO 事業領域のデフォルト選択行処理追加（Utils）
		// 事業領域ドロップダウンリスト
		List<Map<String, Object>> businessAreaList = new ArrayList<Map<String, Object>>();
		businessAreaList.addAll(ddlUtils.getDdlBusinessAreaByCd(CodeConstant.C001, agencyCd, businessAreaCd, true));
		returnMap.put(KEY_BUSINESS_AREA_LIST, businessAreaList);

		return returnMap;

	}

	/**
	 * 社員マスタテーブルからプライマリキーによって社員情報を取得する。.
	 * 
	 * @param companyCd 会社コード
	 * @param shainNo 社員番号
	 * @return 社員マスタテーブルの結果セット
	 */
	public Skf1010MShain getShainByPrimaryKey(String companyCd, String shainNo) {

		// キーセット
		Skf1010MShainKey keySet = new Skf1010MShainKey();
		keySet.setShainNo(shainNo);
		keySet.setCompanyCd(companyCd);
		// 社員情報を取得する
		Skf1010MShain resultValue = skf1010MShainRepository.selectByPrimaryKey(keySet);

		return resultValue;

	}

}
