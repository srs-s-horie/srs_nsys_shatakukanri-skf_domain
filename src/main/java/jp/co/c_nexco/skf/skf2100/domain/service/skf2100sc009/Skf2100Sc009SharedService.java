/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc009;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2100Sc009.Skf2100Sc009GetListTableDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2100Sc009.Skf2100Sc009GetListTableDataExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2100Sc009.Skf2100Sc009GetListTableDataExpRepository;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;

/**
 * Skf2100Sc009SharedService モバイルルーター機器入力支援共通処理クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf2100Sc009SharedService {

	@Autowired
	private Skf2100Sc009GetListTableDataExpRepository skf2100Sc009GetListTableDataExpRepository;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;

	@Value("${skf2100.skf2100_sc009.max_row_count}")
	private String maxRowCount;
	@Value("${skf2100.skf2100_sc009.max_search_count}")
	private String maxSearchCount;


	/**
	 * モバイルルーター機器を取得する
	 * 
	 * 「※」項目はアドレスとして戻り値になる。 
	 * 
	 * @param routerNo		検索条件：通しNo
	 * @param tel				検索条件：電話番号
	 * @param contractEndDateFrom			検索条件：契約終了日From
	 * @param contractEndDateTo					検索条件：契約終了日To
	 * @param biko		検索条件：備考
	 * @param listTableData				*モバイルルーター機器リスト
	 * @return
	 * @throws IllegalAccessException
	 * @throws Exception
	 */
	public int getRouterList(String routerNo, String tel,
			String contractEndDateFrom, String contractEndDateTo, String biko,
			List<Map<String, Object>> listTableData)
			throws IllegalAccessException, Exception {

		// パラメータ設定
		List<Skf2100Sc009GetListTableDataExp> resultList = new ArrayList<Skf2100Sc009GetListTableDataExp>();
		Skf2100Sc009GetListTableDataExpParameter param = new Skf2100Sc009GetListTableDataExpParameter();
		if(!SkfCheckUtils.isNullOrEmpty(routerNo)){
			param.setMobileRouterNo(Long.parseLong(escapeParameter(routerNo)));
		}else{
			param.setMobileRouterNo(null);
		}
		param.setTel(escapeParameter(tel));
		param.setContractEndDateFrom(escapeParameter(contractEndDateFrom));
		param.setContractEndDateTo(escapeParameter(contractEndDateTo));
		param.setBiko(escapeParameter(biko));

		//件数の取得
		int routerCount = skf2100Sc009GetListTableDataExpRepository.getListCount(param);
		if(routerCount == 0){
			// 0件
			resultList.clear();
		}else if(routerCount > Integer.parseInt(maxSearchCount)){
			// 検索結果が指定されている最大件数を超えている場合
			routerCount = -1;
			resultList.clear();
		}else{
			// 検索条件をもとに、社宅部屋の一覧情報を取得
			resultList = skf2100Sc009GetListTableDataExpRepository.getListTableData(param);
			routerCount = resultList.size();
		}

		listTableData.clear();
		listTableData.addAll(createListTable(resultList));

		return routerCount;
	}

	/**
	 * 取得情報からリストテーブルのデータを作成
	 * 
	 * @param shainInfoList
	 * @return
	 */
	public List<Map<String, Object>> createListTable(List<Skf2100Sc009GetListTableDataExp> routerList) {

		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		if (routerList.size() <= 0) {
			return returnList;
		}
		
		for (Skf2100Sc009GetListTableDataExp routerInfo : routerList) {
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			tmpMap.put("colSelect", routerInfo.getMobileRouterNo());
			tmpMap.put("colhdnSelect", routerInfo.getMobileRouterNo());
			tmpMap.put("colRouterNo", HtmlUtils.htmlEscape(routerInfo.getMobileRouterNo().toString()));
			tmpMap.put("colTel", HtmlUtils.htmlEscape(routerInfo.getTel()));
			String endDate = skfDateFormatUtils.dateFormatFromString(routerInfo.getRouterContractEndDate(), "yyyy/MM/dd");
			tmpMap.put("colContractEndDate", HtmlUtils.htmlEscape(endDate));
			tmpMap.put("colBiko", HtmlUtils.htmlEscape(routerInfo.getBiko()));
			tmpMap.put("colhdnIccid", HtmlUtils.htmlEscape(routerInfo.getIccid()));
			tmpMap.put("colhdnImei", HtmlUtils.htmlEscape(routerInfo.getImei()));
			returnList.add(tmpMap);
		}
		return returnList;
	}

	/**
	 * objctをStringに変換する（NULLの場合は空文字を返却する）
	 * @param obj
	 * @return
	 */
	public String createObjToString(Object obj){
		String resultTxt = CodeConstant.DOUBLE_QUOTATION;
		
		if(obj != null){
			resultTxt = obj.toString();
		}
		
		return resultTxt;
	}

	/**
	 * パラメータ文字列のエスケープ処理
	 * @param param
	 * @return
	 */
	public String escapeParameter(String param){
		
		String resultStr=CodeConstant.DOUBLE_QUOTATION;
		
		// 文字エスケープ(% _ ' \)
		if (param != null) {
			// 「\」を「\\」に置換
			resultStr = param.replace("\\", "\\\\");
			// 「%」を「\%」に置換、「_」を「\_」に置換、「'」を「''」に置換
			resultStr = resultStr.replace("%", "\\%").replace("_", "\\_").replace("'", "''");
		}
		return resultStr;
	}
}
