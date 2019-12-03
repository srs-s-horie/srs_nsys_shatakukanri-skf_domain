package jp.co.c_nexco.skf.skf3070.domain.service.skf3070sc004;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3070Sc004.Skf3070Sc004GetOwnerInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3070Sc004.Skf3070Sc004GetOwnerInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3070Sc004.Skf3070Sc004GetOwnerInfoExpRepository;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;

@Service
public class Skf3070Sc004SharedService {

	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private Skf3070Sc004GetOwnerInfoExpRepository skf3070Sc004GetOwnerInfoExpRepository;

	/**
	 * 賃貸人(代理人)情報の一覧を取得する
	 * 
	 * @param companyCd
	 * @param shainNo
	 * @param name
	 * @param nameKk
	 * @param agencyName
	 * @param shatakuKanriNo
	 * @return
	 * @throws IllegalAccessException
	 * @throws Exception
	 */
	public List<Skf3070Sc004GetOwnerInfoExp> getOwnerInfo(String ownerName, String ownerNameKk,
			String address, String businessKbn){
		List<Skf3070Sc004GetOwnerInfoExp> resultList = new ArrayList<Skf3070Sc004GetOwnerInfoExp>();
		Skf3070Sc004GetOwnerInfoExpParameter param = new Skf3070Sc004GetOwnerInfoExpParameter();
		// 氏名又は名称
		if (NfwStringUtils.isNotEmpty(ownerName)) {
			param.setOwnerName(ownerName);
		}
		// 氏名又は名称（フリガナ）
		if (NfwStringUtils.isNotEmpty(ownerNameKk)) {
			param.setOwnerNameKk(ownerNameKk);
		}
		// 住所
		if (NfwStringUtils.isNotEmpty(address)) {
			param.setAddress(address);
		}
		// 個人法人区分
		if (NfwStringUtils.isNotEmpty(businessKbn)) {
			param.setBusinessKbn(businessKbn);
		}

		resultList = skf3070Sc004GetOwnerInfoExpRepository.getOwnerInfo(param);

		return resultList;
	}

	/**
	 * 賃貸人(代理人)情報からリストテーブルのデータを作成します
	 * 
	 * @param ownerInfoList
	 * @return
	 */
	public List<Map<String, Object>> createListTable(List<Skf3070Sc004GetOwnerInfoExp> ownerInfoList) {
		
		// 提示状況汎用コード取得
        Map<String, String> candidateStatusGenCodeMap = new HashMap<String, String>();
        candidateStatusGenCodeMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_KOJIN_HOJIN_KUBUN);
        
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		if (ownerInfoList.size() <= 0) {
			return returnList;
		}

		for (Skf3070Sc004GetOwnerInfoExp tmpData : ownerInfoList) {
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			tmpMap.put("ownerName", tmpData.getOwnerName()); // 氏名又は名称
			tmpMap.put("ownerNameKk", tmpData.getOwnerNameKk()); // 氏名又は名称（フリガナ）
			tmpMap.put("address", tmpData.getAddress()); // 住所
			tmpMap.put("businessKbn", candidateStatusGenCodeMap.get(tmpData.getBusinessKbn())); // 個人法人区分

			returnList.add(tmpMap);
		}
		return returnList;
	}

}
