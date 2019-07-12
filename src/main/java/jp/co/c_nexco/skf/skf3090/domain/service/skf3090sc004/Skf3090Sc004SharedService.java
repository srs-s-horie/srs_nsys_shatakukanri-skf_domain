/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc004;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc004.Skf3090Sc004GetListTableDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc004.Skf3090Sc004GetListTableDataExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3090Sc004.Skf3090Sc004GetListTableDataExpRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;

/**
 * Skf3090Sc004SharedService 従業員マスタ一覧内共通クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3090Sc004SharedService {

	@Value("${skf3090.skf3090_sc004.max_search_count}")
	private Integer maxGetRecordCount;

	@Autowired
	private SkfDropDownUtils ddlUtils;
	@Autowired
	private Skf3090Sc004GetListTableDataExpRepository skf3090Sc004GetListTableDataExpRepository;

	public static List<Skf3090Sc004GetListTableDataExp> resultList;

	/**
	 * ドロップダウンリストに設定するリストを取得する。<br>
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param companyCd 会社コード
	 * @param companyList ※会社ドロップダウンリスト
	 * @param agencyCd 機関コード
	 * @param agencyList ※機関ドロップダウンリスト
	 * @param affiliation1Cd 部等コード
	 * @param affiliation1List ※部等ドロップダウンリスト
	 * @param affiliation2Cd 室チーム又は課リスト
	 * @param affiliation2List ※室チーム又は課リスト
	 */
	public void getDoropDownList(String originalCompanyCd, List<Map<String, Object>> companyList, String agencyCd,
			List<Map<String, Object>> agencyList, String affiliation1Cd, List<Map<String, Object>> affiliation1List,
			String affiliation2Cd, List<Map<String, Object>> affiliation2List) {

		boolean isFirstRowEmpty = true;

		// 会社リスト
		companyList.clear();
		companyList.addAll(ddlUtils.getDdlCompanyByCd(originalCompanyCd, isFirstRowEmpty));

		// 機関リスト
		agencyList.clear();
		agencyList.addAll(ddlUtils.getDdlAgencyByCd(CodeConstant.C001, agencyCd, isFirstRowEmpty));

		// 部等リスト
		affiliation1List.clear();
		affiliation1List
				.addAll(ddlUtils.getDdlAffiliation1ByCd(CodeConstant.C001, agencyCd, affiliation1Cd, isFirstRowEmpty));

		// 室、チーム又は課リスト
		affiliation2List.clear();
		affiliation2List.addAll(ddlUtils.getDdlAffiliation2ByCd(CodeConstant.C001, agencyCd, affiliation1Cd,
				affiliation2Cd, isFirstRowEmpty));
	}

	/**
	 * リストテーブルを取得する。 <br>
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param shainNo
	 * @param name
	 * @param nameKk
	 * @param companyCd
	 * @param agencyCd
	 * @param affiliation1Cd
	 * @param affiliation2Cd
	 * @param listTableData ※取得リストテーブルデータ
	 * @return 取得データのレコードカウント
	 */
	public int getListTableData(String shainNo, String name, String nameKk, String originalCompanyCd, String agencyCd,
			String affiliation1Cd, String affiliation2Cd, List<Map<String, Object>> listTableData) {

		LogUtils.debugByMsg("リストテーブルデータ取得処理開始");
		LogUtils.debugByMsg("引数から取得した値：" + "社員番号：" + shainNo + "　社員名：" + name + "　カナ：" + nameKk + "　会社コード："
				+ originalCompanyCd + "　機関コード：" + agencyCd + "　部コード：" + affiliation1Cd + "　室コード：" + affiliation2Cd);

		// リストテーブルに格納するデータを取得する
		int resultCount = 0;
		List<Skf3090Sc004GetListTableDataExp> resultListTableData = new ArrayList<Skf3090Sc004GetListTableDataExp>();
		Skf3090Sc004GetListTableDataExpParameter param = new Skf3090Sc004GetListTableDataExpParameter();
		param.setShainNo(shainNo);
		param.setName(name);
		param.setNameKk(nameKk);
		param.setOriginalCompanyCd(originalCompanyCd);
		param.setAgencyCd(agencyCd);
		param.setAffiliation1Cd(affiliation1Cd);
		param.setAffiliation2Cd(affiliation2Cd);
		resultListTableData = skf3090Sc004GetListTableDataExpRepository.getListTableData(param);

		// 取得レコード数を設定
		resultCount = resultListTableData.size();

		// 取得データレコード数判定
		if (resultCount == 0 || resultCount > maxGetRecordCount) {
			// 取得データレコード数が0件または3000件より多い場合、何もせず処理終了
			return resultCount;
		}

		// リストテーブルに出力するリストを取得する
		listTableData.clear();
		listTableData.addAll(getListTableDataViewColumn(resultListTableData));

		return resultCount;

	}

	/**
	 * リストテーブルに出力するリストを取得する。
	 * 
	 * @param originList
	 * @return リストテーブルに出力するリスト
	 */
	private List<Map<String, Object>> getListTableDataViewColumn(List<Skf3090Sc004GetListTableDataExp> originList) {

		List<Map<String, Object>> setViewList = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < originList.size(); i++) {
			Skf3090Sc004GetListTableDataExp tmpData = originList.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			tmpMap.put("col1", tmpData.getShainNo());
			tmpMap.put("col2", tmpData.getName());
			tmpMap.put("col3", tmpData.getCompanyName());
			tmpMap.put("col4", tmpData.getAgencyName());
			tmpMap.put("col5", tmpData.getAffiliation1Name());
			tmpMap.put("col6", tmpData.getAffiliation2Name());
			tmpMap.put("col7", tmpData.getOriginalCompanyCd());
			tmpMap.put("col0", "");

			setViewList.add(tmpMap);
		}

		return setViewList;
	}
}
