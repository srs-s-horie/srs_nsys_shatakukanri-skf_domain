/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc001;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc001.Skf3090Sc001GetGenbutsuShikyuKagakuDataInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc001.Skf3090Sc001GetGenbutsuShikyuKagakuDataInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3090Sc001.Skf3090Sc001GetGenbutsuShikyuKagakuDataInfoExpRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090Sc001common.Skf3090Sc001CommonDto;

/**
 * Skf3090Sc001SharedService 現物支給価額マスタ一覧内共通クラス
 * 
 * @author NEXCOシステムズ
 *
 */

@Service
public class Skf3090Sc001SharedService {

	
	@Autowired
	private Skf3090Sc001GetGenbutsuShikyuKagakuDataInfoExpRepository skf3090Sc001GetGenbutsuShikyuKagakuDataInfoExpRepository;


	/**
	 * 現物支給価額マスタ一覧を取得する
	 * 
	 * @param initDto
	 * 
	 */
	public void getGenbutsuShikyuKagakuDataInfo(Skf3090Sc001CommonDto commonDto){

		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
		// 
		List<Skf3090Sc001GetGenbutsuShikyuKagakuDataInfoExp> resultListTableData = new ArrayList<Skf3090Sc001GetGenbutsuShikyuKagakuDataInfoExp>();
		Skf3090Sc001GetGenbutsuShikyuKagakuDataInfoExpParameter param = new Skf3090Sc001GetGenbutsuShikyuKagakuDataInfoExpParameter();	
		resultListTableData = skf3090Sc001GetGenbutsuShikyuKagakuDataInfoExpRepository.getGenbutsuShikyuKagakuData(param);
		if(resultListTableData.size() > 0){
			listTableData.clear();
			listTableData.addAll(getListTableDataViewColumn(resultListTableData));
		}
		commonDto.setListTableData(listTableData);

	}
	
	/**
	 * リストテーブルに出力するリストを取得する。
	 * 
	 * @param originList
	 * 
	 */
	private List<Map<String, Object>> getListTableDataViewColumn(List<Skf3090Sc001GetGenbutsuShikyuKagakuDataInfoExp> originList) {
		List<Map<String, Object>> setViewList = new ArrayList<Map<String, Object>>();
		
		for (int i = 0; i < originList.size(); i++) {
			Skf3090Sc001GetGenbutsuShikyuKagakuDataInfoExp tmpData = originList.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			// 改定日
			tmpMap.put("col1", changeDateFormat(tmpData.getEffectiveDate()));
			// 備考
			tmpMap.put("col2", tmpData.getBiko());
			// メンテナンス
			tmpMap.put("col3", "");
			tmpMap.put("col4", "");
			
			setViewList.add(tmpMap);
		}
		return setViewList;
	}
	
	
	/**
	 * 日付のフォーマットを「yyyyMMdd」から「yyyy/MM/dd」へ変換する
	 * 
	 * @param targetDate
	 * 			日付文字列
	 * @return 変換後の日付文字列
	 *  
	 */
	 private String changeDateFormat(String targetDate){
		if(StringUtils.isEmpty(targetDate)){
			targetDate = null;
		}else{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			try{
				Date date = dateFormat.parse(targetDate);
				SimpleDateFormat dateFormatStr = new SimpleDateFormat("yyyy/MM/dd");
				targetDate = dateFormatStr.format(date);
			}catch(ParseException ex){
				LogUtils.debugByMsg("現物支給価額マスタ一覧日付変換失敗 :" + targetDate);
				targetDate = null;
			}
		}
		return targetDate;
	 }
	
	 
	/**
	 * 日付から"/"と"_"を取り除く
	 * 
	 * @param targetDate
	 * 			日付文字列
	 * @return Replace後の日付文字列
	 *  
	 */
	public String replaceDateFormat(String targetDate){
		return targetDate.replace("/", "").replace("_", "");
	}	 
		
}
