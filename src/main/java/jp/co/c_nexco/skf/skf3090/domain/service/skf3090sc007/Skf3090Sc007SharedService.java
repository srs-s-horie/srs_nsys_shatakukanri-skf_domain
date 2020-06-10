package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc007;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MAgency;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MAgencyKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MSoshiki;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MSoshikiKey;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MAgencyRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MSoshikiRepository;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;

/**
 * Skf3090Sc007SharedService 組織マスタ登録内共通クラス。
 * 
 * @author NEXCOシステムズ
 *
 */
// @Component
@Service
public class Skf3090Sc007SharedService {

	@Autowired
	private SkfDropDownUtils skfDropDownUtils;

	@Autowired
	private Skf1010MSoshikiRepository skf1010MSoshikiRepository;

	@Autowired
	private Skf1010MAgencyRepository skf1010MAgencyRepository;

	// 戻り値用Map用定数
	public static final String KEY_COMPANY_LIST = "COMPANY_LIST";
	public static final String KEY_BUSINESS_AREA_LIST = "BUSINESS_AREA_LIST";

	/**
	 * ドロップダウンリストを取得する
	 * 
	 * @param companyCd 会社コード
	 * @param agencyCd 機関コード
	 * @param businessAreaCd 事業領域コード
	 * @return Map
	 */
	public Map<String, Object> getDropDownLists(String registCompanyCd, String registAgencyCd,
			String registBusinessAreaCd) {

		// 戻り値用Mapのインスタンス生成
		Map<String, Object> returnMap = new HashMap<String, Object>();
		/** ドロップダウンの取得 */
		// 会社ドロップダウンリスト
		List<Map<String, Object>> companyList = new ArrayList<Map<String, Object>>();
		companyList.addAll(skfDropDownUtils.getDdlCompanyByCd(registCompanyCd, true));
		returnMap.put(KEY_COMPANY_LIST, companyList);

		// 事業領域ドロップダウンリスト
		List<Map<String, Object>> businessAreaList = new ArrayList<Map<String, Object>>();
		businessAreaList.addAll(
				skfDropDownUtils.getDdlBusinessAreaByCd(registCompanyCd, registAgencyCd, registBusinessAreaCd, true));
		returnMap.put(KEY_BUSINESS_AREA_LIST, businessAreaList);
		return returnMap;

	}

	/**
	 * 組織テーブルからプライマリーキーによって社員情報を取得する。
	 * 
	 * @param companyCd 会社コード
	 * @param agencyCd 機関コード
	 * @param affiliation1Cd 部等コード
	 * @param affiliation2Cd 室・課等コード
	 */
	public Skf1010MSoshiki getSoshikiByPrimaryKey(String companyCd, String agencyCd, String affiliation1Cd,
			String affiliation2Cd) {

		// キーセット
		Skf1010MSoshikiKey keySet = new Skf1010MSoshikiKey();
		keySet.setCompanyCd(companyCd);
		keySet.setAgencyCd(agencyCd);
		keySet.setAffiliation1Cd(affiliation1Cd);
		keySet.setAffiliation2Cd(affiliation2Cd);
		// 組織情報を取得する
		Skf1010MSoshiki resultValue = skf1010MSoshikiRepository.selectByPrimaryKey(keySet);

		return resultValue;

	}

	/**
	 * 組織テーブルからプライマリーキーによって社員情報を取得する。
	 * 
	 * @param companyCd 会社コード
	 * @param agencyCd 機関コード
	 */
	public Skf1010MAgency getAgencyByPrimaryKey(String companyCd, String agencyCd) {

		// キーセット
		Skf1010MAgencyKey keySet = new Skf1010MAgencyKey();
		keySet.setCompanyCd(companyCd);
		keySet.setAgencyCd(agencyCd);
		// 機関情報を取得する
		Skf1010MAgency resultValue = skf1010MAgencyRepository.selectByPrimaryKey(keySet);

		return resultValue;

	}

}
