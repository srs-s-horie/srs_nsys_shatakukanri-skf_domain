/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3050.domain.service.skf3050sc001;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3021Sc001.Skf3021Sc001GetNyutaikyoYoteiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3021Sc001.Skf3021Sc001GetNyutaikyoYoteiInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Sc001.Skf3050Sc001GetShainBangoIkatuInfoExp;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3021Sc001.Skf3021Sc001GetNyutaikyoYoteiInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Sc001.Skf3050Sc001GetShainBangoIkatuInfoExpRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.skf3021.domain.dto.skf3021Sc001common.Skf3021Sc001CommonDto;

/**
 * Skf3050Sc001SharedService 社員番号一括設定共通クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3050Sc001SharedService {

	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private Skf3050Sc001GetShainBangoIkatuInfoExpRepository skf3050Sc001GetShainBangoIkatuInfoExpRepository;
	
	/**
	 * 社員番号一括設定一覧情報を取得 <br>
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param listTableData リストテーブル用データ
	 * @return 取得データのレコードカウント
	 */
	public int setGrvShainBangoList(List<Map<String, Object>> listTableData) {

		LogUtils.debugByMsg("社員番号一括設定一覧取得処理開始");
		int resultCount = 0;

		List<Skf3050Sc001GetShainBangoIkatuInfoExp> dt = new ArrayList<Skf3050Sc001GetShainBangoIkatuInfoExp>();
		dt = skf3050Sc001GetShainBangoIkatuInfoExpRepository.getShainBangoIkatuInfo();
		
		resultCount = dt.size();
		
		if(resultCount == 0){
			return resultCount;
		}else{
			// リストテーブルに出力するリストを取得する
			listTableData.clear();
			listTableData.addAll(getListTableDataViewColumn(dt));
		}
		
		return resultCount;

	}

	/**
	 * リストテーブルに出力するリストを取得する。
	 * 
	 * @param originList
	 * @return リストテーブルに出力するリスト
	 */
	private List<Map<String, Object>> getListTableDataViewColumn(List<Skf3050Sc001GetShainBangoIkatuInfoExp> originList) {

		List<Map<String, Object>> setViewList = new ArrayList<Map<String, Object>>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

		for (int i = 0; i < originList.size(); i++) {

			Skf3050Sc001GetShainBangoIkatuInfoExp tmpData = originList.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			//値セット

			//社員フラグ
			String flg = tmpData.getFlg();
			tmpMap.put("hdnFlg", flg);
			//社員番号
			String shainNo =  tmpData.getShainNo();
			if("1".equals(flg)){
				//'旧社員番号「社員番号＋"*"」で表示する
				shainNo = shainNo + CodeConstant.ASTERISK;
			}
			tmpMap.put("colShainNo", shainNo);
			//所属
			tmpMap.put("colShozoku", tmpData.getAffiliation());
			//社員氏名
			tmpMap.put("colShainName", tmpData.getName());
			//会社コード
			tmpMap.put("hdnCompanyCd", tmpData.getCompanyCd());
			//
			tmpMap.put("hdnShainNoChangeFlg", tmpData.getShainNoChangeFlg());
			//
			tmpMap.put("hdnUpdateDateSS", dateFormat.format(tmpData.getUpdateDateSs()));
			//
			tmpMap.put("hdnUpdateDateSL", dateFormat.format(tmpData.getUpdateDateSl()));
			//
			tmpMap.put("hdnShatakuKanriId", tmpData.getShatakuKanriId());
			//
			tmpMap.put("hdnTaikyoDate", tmpData.getTaikyoDate());
			//
			tmpMap.put("hdnKyojushaKbn", tmpData.getKyojushaKbn());
			
			//新社員番号入力
			tmpMap.put("colTxtShainNo", "<input id='txtShainNo" + i 
					+ "' name='txtShainNo" + i + "' type='text' value='' style='ime-mode: disabled;width: 98%' maxlength='8' tabindex='1' pattern='^[0-9]+$' /> ");
			
			tmpMap.put("hdnTxtBoxName", "txtShainNo" + i);
			
			setViewList.add(tmpMap);
		}

		return setViewList;
	}
	
	
}
