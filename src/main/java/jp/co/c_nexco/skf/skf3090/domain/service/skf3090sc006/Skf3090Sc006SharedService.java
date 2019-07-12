package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc006;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc006.Skf3090Sc006GetSoshikiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc006.Skf3090Sc006GetSoshikiInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3090Sc006.Skf3090Sc006GetSoshikiInfoExpRepository;

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

		soshikiInfoList = skf3090Sc006GetSoshikiInfoExpRepository.getSoshikiInfo(param);
		return soshikiInfoList;

	}

}
