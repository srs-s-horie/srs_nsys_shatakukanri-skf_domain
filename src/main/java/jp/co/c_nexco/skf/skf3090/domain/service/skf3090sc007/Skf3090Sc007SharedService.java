package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc007;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import com.caucho.config.Service;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;

/**
 * Skf3090Sc007SharedService 組織マスタ登録内共通クラス。
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3090Sc007SharedService {

	@Autowired
	private SkfDropDownUtils ddlUtils;

	// 戻り値用Map用定数
	public static final String KEY_COMPANY_LIST = "COMPANY_LIST";
	public static final String KEY_AGENCY_LIST = "AGENCY_LIST";
	public static final String KEY_BUSINESS_AREA_LIST = "BUSINESS_AREA_LIST";

	/**
	 * ドロップダウンリストを取得する
	 * 
	 * @param companyCd 会社コード
	 * @param agencyCd 機関コード
	 * @param businessAreaCd 事業領域コード
	 * @return Map
	 */
	public Map<String, Object> getDropDownLists(String companyCd, String agencyCd, String businessAreaCd) {

		// 戻り値用Mapインスタンス生成
		Map<String, Object> returnMap = new HashMap<String, Object>();

		/** ドロップダウンの取得 */
		// 会社ドロップダウンリスト
		List<Map<String, Object>> companyList = new ArrayList<Map<String, Object>>();
		companyList.addAll(ddlUtils.getDdlCompanyByCd(CodeConstant.C001, true));
		returnMap.put(KEY_COMPANY_LIST, companyList);

		// 機関ドロップダウンリスト
		List<Map<String, Object>> agencyList = new ArrayList<Map<String, Object>>();
		agencyList.addAll(ddlUtils.getDdlAgencyByCd(CodeConstant.C001, agencyCd, true));
		returnMap.put(KEY_AGENCY_LIST, agencyList);

		// 事業領域ドロップダウンリスト
		List<Map<String, Object>> businessAreaList = new ArrayList<Map<String, Object>>();
		businessAreaList.addAll(ddlUtils.getDdlBusinessAreaByCd(CodeConstant.C001, agencyCd, businessAreaCd, true));
		returnMap.put(KEY_BUSINESS_AREA_LIST, businessAreaList);

		return returnMap;
	}

}
