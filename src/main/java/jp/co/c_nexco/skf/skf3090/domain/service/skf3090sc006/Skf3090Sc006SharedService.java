package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc006;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc006.Skf3090Sc006GetSoshikiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc006.Skf3090Sc006GetSoshikiInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3090Sc006.Skf3090Sc006GetSoshikiInfoExpRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;

@Service
public class Skf3090Sc006SharedService {

	@Autowired
	private Skf3090Sc006GetSoshikiInfoExpRepository skf3090Sc006GetSoshikiInfoExpRepository;

	/**
	 * 組織情報を取得します
	 * 
	 * @param companyCd
	 * @param agencyCd
	 * @param affiliation1Cd
	 * @param affiliation2Cd
	 * @param businessAreaCd
	 * @return
	 */
	public List<Skf3090Sc006GetSoshikiInfoExp> getSoshikiInfo(String companyCd, String agencyCd, String affiliation1Cd,
			String affiliation2Cd, String businessAreaCd) {
		List<Skf3090Sc006GetSoshikiInfoExp> soshikiInfoList = new ArrayList<Skf3090Sc006GetSoshikiInfoExp>();

		Skf3090Sc006GetSoshikiInfoExpParameter param = new Skf3090Sc006GetSoshikiInfoExpParameter();
		param.setCompanyCd(companyCd);
		param.setAgencyCd(agencyCd);
		param.setAffiliation1Cd(affiliation1Cd);
		param.setAffiliation2Cd(affiliation2Cd);
		param.setBusinessAreaCd(businessAreaCd);

		LogUtils.debugByMsg("組織マスタ一覧-組織情報取得SQL");

		// SQL実行
		soshikiInfoList = skf3090Sc006GetSoshikiInfoExpRepository.getSoshikiInfo(param);
		return soshikiInfoList;

	}

	public List<Map<String, Object>> createListTableData(List<Skf3090Sc006GetSoshikiInfoExp> originalList) {

		List<Map<String, Object>> setViewList = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < originalList.size(); i++) {
			Skf3090Sc006GetSoshikiInfoExp tmpData = originalList.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			tmpMap.put("companyName", tmpData.getCompanyName());
			tmpMap.put("agencyName", tmpData.getAgencyName());
			tmpMap.put("affiliation1Name", tmpData.getAffiliation1Name());
			tmpMap.put("affiliation2Name", tmpData.getAffiliation2Name());
			tmpMap.put("businessAreaName", tmpData.getBusinessAreaName());
			tmpMap.put("companyCd", tmpData.getCompanyCd());
			tmpMap.put("agencyCd", tmpData.getAgencyCd());
			tmpMap.put("details", "");

			setViewList.add(tmpMap);
		}
		return setViewList;

	}
}
