package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc001;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc001.Skf2010Sc001GetAllShainInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc001.Skf2010Sc001GetAllShainInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc001.Skf2010Sc001GetGaitouShatakuShainInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc001.Skf2010Sc001GetGaitouShatakuShainInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc001.Skf2010Sc001GetGaitouShatakuShainInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc001.Skf2010Sc001GetGaitouShatakuShainInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc001.Skf2010Sc001GetAllShainInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc001.Skf2010Sc001GetGaitouShatakuShainInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc001.Skf2010Sc001GetGaitouShatakuShainInfoExpRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.CopyUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;

@Service
public class Skf2010Sc001SharedService {

	@Autowired
	private Skf2010Sc001GetAllShainInfoExpRepository skf2010Sc001GetAllShainInfoExpRepository;
	@Autowired
	private Skf2010Sc001GetGaitouShatakuShainInfoExpRepository skf2010Sc001GetGaitouShatakuShainInfoExpRepository;

	/**
	 * 社員情報の一覧を取得する
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
	public List<Skf2010Sc001GetAllShainInfoExp> getShainMasterInfo(String companyCd, String shainNo, String name,
			String nameKk, String agencyName, int shatakuKanriNo) throws IllegalAccessException, Exception {
		List<Skf2010Sc001GetAllShainInfoExp> resultList = new ArrayList<Skf2010Sc001GetAllShainInfoExp>();
		Skf2010Sc001GetAllShainInfoExpParameter param = new Skf2010Sc001GetAllShainInfoExpParameter();
		// 会社コード
		param.setCompanyCd(companyCd);
		// 社員番号
		if (NfwStringUtils.isNotEmpty(shainNo)) {
			param.setShainNo(shainNo);
		}
		// 社員名
		if (NfwStringUtils.isNotEmpty(name)) {
			param.setName(name);
		}
		// 社員名カナ
		if (NfwStringUtils.isNotEmpty(nameKk)) {
			param.setNameKk(nameKk);
		}
		// 現所属
		if (NfwStringUtils.isNotEmpty(agencyName)) {
			param.setAgencyName(agencyName);
		}

		// 入居関連チェック
		if (shatakuKanriNo > 0) {
			List<Skf2010Sc001GetGaitouShatakuShainInfoExp> gaitoResultList = new ArrayList<Skf2010Sc001GetGaitouShatakuShainInfoExp>();
			Skf2010Sc001GetGaitouShatakuShainInfoExpParameter gaitoParam = new Skf2010Sc001GetGaitouShatakuShainInfoExpParameter();
			CopyUtils.copyProperties(gaitoParam, param);
			gaitoParam.setShatakuKanriNo(shatakuKanriNo);

			// 提示データテーブルを紐づけて取得
			gaitoResultList = skf2010Sc001GetGaitouShatakuShainInfoExpRepository.getGaitouShatakuShainInfo(gaitoParam);
			if (gaitoResultList != null) {
				// CopyUtils.copyProperties(resultList, gaitoResultList);
				for (Skf2010Sc001GetGaitouShatakuShainInfoExp data : gaitoResultList) {
					Skf2010Sc001GetAllShainInfoExp tmpData = new Skf2010Sc001GetAllShainInfoExp();
					CopyUtils.copyProperties(tmpData, data);
					resultList.add(tmpData);
				}

			}
		} else {
			// 社員情報のみで取得
			resultList = skf2010Sc001GetAllShainInfoExpRepository.getAllShainInfo(param);
		}

		return resultList;
	}

	/**
	 * 申請情報からリストテーブルのデータを作成します
	 * 
	 * @param shainInfoList
	 * @return
	 */
	public List<Map<String, Object>> createListTable(List<Skf2010Sc001GetAllShainInfoExp> shainInfoList) {
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		if (shainInfoList.size() <= 0) {
			return returnList;
		}

		for (int i = 0; i < shainInfoList.size(); i++) {
			Skf2010Sc001GetAllShainInfoExp tmpData = shainInfoList.get(i);

			Map<String, Object> tmpMap = new HashMap<String, Object>();
			tmpMap.put("shainNo", tmpData.getShainNo()); // 社員番号
			tmpMap.put("name", tmpData.getName()); // 氏名
			tmpMap.put("agency", tmpData.getGenshozoku()); // 所属機関

			returnList.add(tmpMap);
		}
		return returnList;
	}

}
