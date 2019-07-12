package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc001;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc001.Skf2010Sc001GetNyukyoShainMasterInfoByParameterExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc001.Skf2010Sc001GetNyukyoShainMasterInfoByParameterExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc001.Skf2010Sc001GetShainMasterInfoByParameterExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc001.Skf2010Sc001GetShainMasterInfoByParameterExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc001.Skf2010Sc001GetNyukyoShainMasterInfoByParameterExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc001.Skf2010Sc001GetShainMasterInfoByParameterExpRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.CopyUtils;

@Service
public class Skf2010Sc001SharedService {

	@Autowired
	private Skf2010Sc001GetNyukyoShainMasterInfoByParameterExpRepository skf2010Sc001GetNyukyoShainMasterInfoByParameterExpRepository;
	@Autowired
	private Skf2010Sc001GetShainMasterInfoByParameterExpRepository skf2010Sc001GetShainMasterInfoByParameterExpRepository;

	/**
	 * 社員情報の一覧を取得する
	 * 
	 * @param companyCd
	 * @param shainNo
	 * @param name
	 * @param nameKk
	 * @param agencyName
	 * @return
	 * @throws Exception
	 * @throws IllegalAccessException
	 */
	public List<Skf2010Sc001GetShainMasterInfoByParameterExp> getShainMasterInfo(String companyCd, String shainNo,
			String name, String nameKk, String agencyName, boolean nyukyoFlag)
			throws IllegalAccessException, Exception {
		List<Skf2010Sc001GetShainMasterInfoByParameterExp> resultList = new ArrayList<Skf2010Sc001GetShainMasterInfoByParameterExp>();
		Skf2010Sc001GetShainMasterInfoByParameterExpParameter param = new Skf2010Sc001GetShainMasterInfoByParameterExpParameter();
		param.setCompanyCd(companyCd);
		if (shainNo != null && !CheckUtils.isEmpty(shainNo)) {
			param.setShainNo(shainNo);
		}
		if (name != null && !CheckUtils.isEmpty(name)) {
			param.setName(name);
		}
		if (nameKk != null && !CheckUtils.isEmpty(nameKk)) {
			param.setNameKk(nameKk);
		}
		if (agencyName != null && !CheckUtils.isEmpty(agencyName)) {
			param.setAgencyName(agencyName);
		}

		// 入居関連チェック
		if (nyukyoFlag) {
			List<Skf2010Sc001GetNyukyoShainMasterInfoByParameterExp> nyukyoResultList = new ArrayList<Skf2010Sc001GetNyukyoShainMasterInfoByParameterExp>();
			Skf2010Sc001GetNyukyoShainMasterInfoByParameterExpParameter nyukyoParam = new Skf2010Sc001GetNyukyoShainMasterInfoByParameterExpParameter();
			CopyUtils.copyProperties(nyukyoParam, param);

			// 提示データテーブルを紐づけて取得
			nyukyoResultList = skf2010Sc001GetNyukyoShainMasterInfoByParameterExpRepository
					.getNyukyoShainMasterInfoByParameter(nyukyoParam);
			if (nyukyoResultList != null) {
				CopyUtils.copyProperties(resultList, nyukyoResultList);
			}
		} else {
			// 社員情報のみで取得
			resultList = skf2010Sc001GetShainMasterInfoByParameterExpRepository.getShainMasterInfoByParameter(param);
		}

		return resultList;
	}

	/**
	 * 申請情報からリストテーブルのデータを作成します
	 * 
	 * @param shainInfoList
	 * @return
	 */
	public List<Map<String, Object>> createListTable(List<Skf2010Sc001GetShainMasterInfoByParameterExp> shainInfoList) {
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		if (shainInfoList.size() <= 0) {
			return returnList;
		}

		for (int i = 0; i < shainInfoList.size(); i++) {
			Skf2010Sc001GetShainMasterInfoByParameterExp tmpData = shainInfoList.get(i);

			Map<String, Object> tmpMap = new HashMap<String, Object>();
			tmpMap.put("shainNo", tmpData.getShainNo()); // 申請書ID
			tmpMap.put("name", tmpData.getName()); // 申請状況
			tmpMap.put("nameKk", tmpData.getNameKk()); // 申請書番号
			tmpMap.put("agency", tmpData.getAgencyName()); // 申請日

			returnList.add(tmpMap);
		}
		return returnList;
	}

}
