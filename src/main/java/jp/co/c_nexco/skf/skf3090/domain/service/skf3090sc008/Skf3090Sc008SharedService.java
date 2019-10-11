/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc008;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc008.Skf3090Sc008GetInformationDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc008.Skf3090Sc008GetInformationDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010TInformation;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010TInformationKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3090Sc008.Skf3090Sc008GetInformationDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010TInformationRepository;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;

/**
 * Skf3090Sc008SharedService お知らせメンテナンス内共通クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3090Sc008SharedService {
	
	@Autowired
	private Skf3090Sc008GetInformationDataExpRepository skf3090Sc008GetInformationDataExpRepository;
	@Autowired
	private Skf1010TInformationRepository skf1010TInformationRepository;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	
	/**
	 * お知らせテーブルリストを取得する。 
	 * 
	 * @param companyCd
	 * @return List<Map<String, Object>>型のテーブルリスト
	 */
	public List<Map<String, Object>> getInformationDataListMap(String companyCd) {
		List<Map<String, Object>> informationdataListMap = new ArrayList<Map<String, Object>>();
		// リストテーブルに格納するデータを取得する
		List<Skf3090Sc008GetInformationDataExp> resultList = new ArrayList<Skf3090Sc008GetInformationDataExp>();
		Skf3090Sc008GetInformationDataExpParameter param = new Skf3090Sc008GetInformationDataExpParameter();
		param.setCompanyCd(companyCd);
		resultList = skf3090Sc008GetInformationDataExpRepository.getInformationData(param);

		// リストテーブルに出力するリストを取得する
		informationdataListMap.clear();
		informationdataListMap.addAll(getListTableDataViewColumn(resultList));
		
		return informationdataListMap;
	}
	
	/**
	 * リストテーブルに出力するリストを取得する。
	 * 
	 * @param resultList
	 * @return リストテーブルに出力するリスト
	 */
	private List<Map<String, Object>> getListTableDataViewColumn(List<Skf3090Sc008GetInformationDataExp> resultList) {

		List<Map<String, Object>> setViewList = new ArrayList<Map<String, Object>>();

		for (Skf3090Sc008GetInformationDataExp resultData:resultList) {
			Map<String, Object> resultDataMap = new HashMap<String, Object>();
			String openDate = skfDateFormatUtils.dateFormatFromString(resultData.getOpenDate(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			resultDataMap.put("edit", "");
			resultDataMap.put("openDate", HtmlUtils.htmlEscape(openDate));
			resultDataMap.put("information", resultData.getNote());
			resultDataMap.put("delete", "");
			setViewList.add(resultDataMap);
		}
		return setViewList;
	}
	
	/**
	 * お知らせテーブルリストを取得する。 
	 * 
	 * @param companyCd
	 * @param openDate
	 * @return　お知らせ情報
	 */
	public Skf1010TInformation getInformation(String companyCd, String openDate) {
		
		Skf1010TInformation informationdata = new Skf1010TInformation();
		Skf1010TInformationKey key = new Skf1010TInformationKey();
		key.setCompanyCd(companyCd);
		key.setOpenDate(openDate);
		informationdata = skf1010TInformationRepository.selectByPrimaryKey(key);
		
		return informationdata;
	}
}
