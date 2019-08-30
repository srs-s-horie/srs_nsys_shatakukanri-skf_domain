/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.service.skf3020sc004;

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
import org.springframework.web.util.HtmlUtils;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3020Sc004.Skf3020Sc004GetTenninshaInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3020Sc004.Skf3020Sc004GetTenninshaInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3020Sc004.Skf3020Sc004GetTenninshaInfoExpRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;

/**
 * Skf3020Sc004SharedService 転任者一覧内共通クラス
 * 
 * @author NEXCOシステムズ
 *
 */

@Service
public class Skf3020Sc004SharedService {

	@Autowired
	private SkfDropDownUtils ddlUtils;

	@Autowired
	private Skf3020Sc004GetTenninshaInfoExpRepository skf3020Sc004GetTenninshaInfoExpRepository;

	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	
	/**
	 * ドロップダウンリストに設定するリストを取得する。<br>
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * 
	 */
	public void getDoropDownList(String genShatakuKubun, List<Map<String, Object>> genShatakuKubunList,
			String nyutaikyoYoteiSakuseiKubun, List<Map<String, Object>> yoteiSakuseiList) {

		boolean isFirstRowEmpty = true;

		// // 現社宅区分ドロップダウンリストの設定
		genShatakuKubunList.clear();
		genShatakuKubunList.addAll(ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_NOW_SHATAKU_KBN,
				genShatakuKubun, isFirstRowEmpty));
		
		// 入退居予定作成区分ドロップダウンリストの設定
		yoteiSakuseiList.clear();
		yoteiSakuseiList.addAll(ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_NYUTAIKYO_YOTEI_KBN,
				nyutaikyoYoteiSakuseiKubun, isFirstRowEmpty));

	}

	/**
	 * 転任者一覧を取得する。<br>
	 * 
	 */
	public int getListTableData(String shainNo, String shainName, String nyukyo, String taikyo, String henko,
			String genShatakuKubun, String genShozoku, String shinShozoku, String nyutaikyoYoteiSakuseiKubun,
			String biko, List<Map<String, Object>> listTableData) {

		// リストテーブルに格納するデータを取得する
		int resultCount = 0;
		List<Skf3020Sc004GetTenninshaInfoExp> resultListTableData = new ArrayList<Skf3020Sc004GetTenninshaInfoExp>();
		Skf3020Sc004GetTenninshaInfoExpParameter param = new Skf3020Sc004GetTenninshaInfoExpParameter();
		param.setShainNo(shainNo);
		param.setShainName(shainName);
		param.setNyukyo(nyukyo);
		param.setTaikyo(taikyo);
		param.setHenko(henko);
		param.setGenShatakuKubun(genShatakuKubun);
		param.setGenShozoku(genShozoku);
		param.setShinShozoku(shinShozoku);
		param.setNyutaikyoYoteiSakuseiKubun(nyutaikyoYoteiSakuseiKubun);
		param.setBiko(biko);
		resultListTableData = skf3020Sc004GetTenninshaInfoExpRepository.getTenninshaInfo(param);

		// 取得レコード数を設定
		resultCount = resultListTableData.size();

		// 取得データレコード数判定
		if (resultCount == 0) {
			// 取得データレコード数が0件の場合、何もせず処理終了
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
	private List<Map<String, Object>> getListTableDataViewColumn(List<Skf3020Sc004GetTenninshaInfoExp> originList) {

		List<Map<String, Object>> setViewList = new ArrayList<Map<String, Object>>();

		// 入退去予定作成区分を取得
		Map<String, String> genericCodeMapNyutaikyoYoteiKbn = new HashMap<String, String>();
		genericCodeMapNyutaikyoYoteiKbn = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_NYUTAIKYO_YOTEI_KBN);
		
		for (int i = 0; i < originList.size(); i++) {

			Skf3020Sc004GetTenninshaInfoExp tmpData = originList.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			
			// 氏名
			tmpMap.put("col5", HtmlUtils.htmlEscape(tmpData.getName()));			
			// 等級
			tmpMap.put("col6", HtmlUtils.htmlEscape(tmpData.getTokyu()));			
			// 年齢
			tmpMap.put("col7", HtmlUtils.htmlEscape(tmpData.getAge()));			
			// 新所属
			tmpMap.put("col8", HtmlUtils.htmlUnescape(tmpData.getNewAffiliation()));			
			// 現所属
			tmpMap.put("col9", HtmlUtils.htmlUnescape(tmpData.getNowAffiliation()));			
			// 備考
			tmpMap.put("col10", HtmlUtils.htmlEscape(tmpData.getBiko()));			
			// 取込日
			String takingDateStr = tmpData.getDataTakinginDate();
			if(StringUtils.isEmpty(takingDateStr)){
				takingDateStr = null;
			}else{
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
				try{
					Date date = dateFormat.parse(takingDateStr);
					SimpleDateFormat dateFormatStr = new SimpleDateFormat("yyyy/MM/dd");
					takingDateStr = dateFormatStr.format(date);
				}catch(ParseException ex){
					LogUtils.debugByMsg("転任者情報-取込日変換エラー :" + takingDateStr);
					takingDateStr = null;
				}
			}
			tmpMap.put("col11", HtmlUtils.htmlEscape(takingDateStr));			
			
			// 入退去予定作成区分
			// 汎用コードに変更
			String NyutaikyoYoteiKbn = "";
			if (tmpData.getNyutaikyoYoteiKbn() != null) {
				NyutaikyoYoteiKbn = genericCodeMapNyutaikyoYoteiKbn.get(tmpData.getNyutaikyoYoteiKbn());
			}			
			tmpMap.put("col12", NyutaikyoYoteiKbn);			
			
			// 社員番号
            //社員番号変更フラグが｢1｣の場合、社員番号の末尾に｢*｣を付与する。
			String shaiNO = tmpData.getShainNo();
			if(tmpData.getShainNoChangeFlg() != null && tmpData.getShainNoChangeFlg().equals("1")){
				shaiNO = shaiNO + "*";
				// 変更フラグはチェックが付いた状態とする
				tmpData.setHenkouFlg("1");
			}
			tmpMap.put("col4", shaiNO);			
			
            // 現社宅区分が無(0)、入退居予定作成区分が未作成(0)の場合
			if(tmpData.getNowShatakuKbn().equals("0")  && tmpData.getNyutaikyoYoteiKbn().equals("0")){
	            //入居ﾁｪｯｸﾎﾞｯｸｽ活性
				tmpMap.put("col1", nyukyoCheckBoxStr(tmpData.getNyukyoFlg(), false, i));
        		//退居ﾁｪｯｸﾎﾞｯｸｽ非活性
				tmpMap.put("col2", taikyoCheckBoxStr(tmpData.getTaikyoFlg(), true, i));
        		//変更ﾁｪｯｸﾎﾞｯｸｽ非活性
				tmpMap.put("col3", henkouCheckBoxStr(tmpData.getHenkouFlg(), true, i));
        		//現社宅ボタン非表示
				//　-----
        		//削除ボタン活性
				tmpMap.put("col15", "");			
				
                //現社宅区分が無(0)、入退居予定作成区分が作成済(1)の場合
			}else if(tmpData.getNowShatakuKbn().equals("0") && tmpData.getNyutaikyoYoteiKbn().equals("1")){
                //入居フラグ=ON(1)の場合
				if(tmpData.getNyukyoFlg().equals("1")){
                    // 入居ﾁｪｯｸﾎﾞｯｸｽ非活性
					tmpMap.put("col1", nyukyoCheckBoxStr(tmpData.getNyukyoFlg(), true, i));
				}else{
                    // 入居ﾁｪｯｸﾎﾞｯｸｽ活性
					tmpMap.put("col1", nyukyoCheckBoxStr(tmpData.getNyukyoFlg(), false, i));
				}
                //退居ﾁｪｯｸﾎﾞｯｸｽ非活性
				tmpMap.put("col2", taikyoCheckBoxStr(tmpData.getTaikyoFlg(), true, i));
                //変更ﾁｪｯｸﾎﾞｯｸｽ非活性
				tmpMap.put("col3", henkouCheckBoxStr(tmpData.getHenkouFlg(), true, i));
                //現社宅ボタン非表示
				//　-----
                //削除ボタン非活性
				//　-----

                //現社宅区分が有(1、2)、入退居予定作成区分が未作成(0)の場合
			}else if((tmpData.getNowShatakuKbn().equals("1") || tmpData.getNowShatakuKbn().equals("2")) && tmpData.getNyutaikyoYoteiKbn().equals("0")){
	            //入居ﾁｪｯｸﾎﾞｯｸｽ活性
				tmpMap.put("col1", nyukyoCheckBoxStr(tmpData.getNyukyoFlg(), false, i));
        		//退居ﾁｪｯｸﾎﾞｯｸｽ活性
				tmpMap.put("col2", taikyoCheckBoxStr(tmpData.getTaikyoFlg(), false, i));
        		//変更ﾁｪｯｸﾎﾞｯｸｽ活性
				tmpMap.put("col3", henkouCheckBoxStr(tmpData.getHenkouFlg(), false, i));
                //現社宅ボタン表示
				tmpMap.put("col13", "");			
                //削除ボタン活性
				tmpMap.put("col15", "");			
				
                //現社宅区分が有(1、2)、入退居予定作成区分が作成済(1)の場合
			}else if((tmpData.getNowShatakuKbn().equals("1") || tmpData.getNowShatakuKbn().equals("2")) && tmpData.getNyutaikyoYoteiKbn().equals("1")){
                //入居フラグ=ON(1)の場合
				if(tmpData.getNyukyoFlg().equals("1")){
                    // 入居ﾁｪｯｸﾎﾞｯｸｽ非活性
					tmpMap.put("col1", nyukyoCheckBoxStr(tmpData.getNyukyoFlg(), true, i));
				}else{
                    // 入居ﾁｪｯｸﾎﾞｯｸｽ活性
					tmpMap.put("col1", nyukyoCheckBoxStr(tmpData.getNyukyoFlg(), false, i));
				}
                //退居フラグ=ON(1)の場合
				if(tmpData.getTaikyoFlg().equals("1")){
                    // 退居ﾁｪｯｸﾎﾞｯｸｽ非活性
					tmpMap.put("col2", taikyoCheckBoxStr(tmpData.getTaikyoFlg(), true, i));
				}else{
                    // 退居ﾁｪｯｸﾎﾞｯｸｽ活性
					tmpMap.put("col2", taikyoCheckBoxStr(tmpData.getTaikyoFlg(), false, i));
				}
				
                //変更フラグ=ON(1)の場合
				if(tmpData.getHenkouFlg().equals("1")){
                    // 変更ﾁｪｯｸﾎﾞｯｸｽ非活性
					tmpMap.put("col3", henkouCheckBoxStr(tmpData.getHenkouFlg(), true, i));
				}else{
                    // 変更ﾁｪｯｸﾎﾞｯｸｽ活性
					tmpMap.put("col3", henkouCheckBoxStr(tmpData.getHenkouFlg(), false, i));
				}
                //現社宅ボタン表示
				tmpMap.put("col13", "");			
                //削除ボタン非活性
				//　-----
			}
			// 詳細
			tmpMap.put("col14", "");	
			// 更新日時
			tmpMap.put("col16", tmpData.getUpdateDateStr());	

			setViewList.add(tmpMap);
		}
		
		return setViewList;
	}

	
	/**
	 * 入居チェックボックスを生成
	 * 
	 * @param nyukyoFlg
	 * @param enabled
	 * @return
	 */
	private String nyukyoCheckBoxStr(String nyukyoFlg, Boolean enabled, int i){
		String checkBoxStr;
		if(nyukyoFlg.equals("1")){
			// チェックあり
			if(enabled == true){
				// 無効
				checkBoxStr = "<INPUT type='checkbox' checked='checked' disabled='disabled' name='nyukyoChkVal' id='nyukyoChkVal" + i + "' value='" + i + "'>";
			}else{
				// 有効
				checkBoxStr = "<INPUT type='checkbox' checked='checked' name='nyukyoChkVal' id='nyukyoChkVal" + i + "' value='" + i + "'>";
			}
		}else{
			// チェックなし
			if(enabled == true){
				// 無効
				checkBoxStr = "<INPUT type='checkbox' name='nyukyoChkVal' disabled='disabled' id='nyukyoChkVal" + i + "' value='" + i + "'>";
			}else{
				// 有効
				checkBoxStr = "<INPUT type='checkbox' name='nyukyoChkVal' id='nyukyoChkVal" + i + "' value='" + i + "'>";
			}
		}
		return  checkBoxStr;
	}

	/**
	 * 退去チェックボックスを生成
	 * 
	 * @param taikyoFlg
	 * @param enabled
	 * @return
	 */
	private String taikyoCheckBoxStr(String taikyoFlg, Boolean enabled, int i){
		
		String checkBoxStr;
		if(taikyoFlg.equals("1")){
			// チェックあり
			if(enabled == true){
				// 無効
				checkBoxStr = "<INPUT type='checkbox' checked='trcheckedue' disabled='disabled' name='taikyoChkVal' id='taikyoChkVal" + i + "' value='" + i + "'>";
			}else{
				// 有効
				checkBoxStr = "<INPUT type='checkbox' checked='checked' name='taikyoChkVal' id='taikyoChkVal" + i + "' value='" + i + "'>";
			}
		}else{
			// チェックなし
			if(enabled == true){
				// 無効
				checkBoxStr = "<INPUT type='checkbox' disabled='disabled' name='taikyoChkVal' id='taikyoChkVal" + i + "' value='" + i + "'>";
			}else{
				// 有効
				checkBoxStr = "<INPUT type='checkbox' name='taikyoChkVal' id='taikyoChkVal" + i + "' value='" + i + "'>";
			}
		}
		return  checkBoxStr;
	}

	/**
	 * 変更チェックボックスを生成
	 * 
	 * @param henkouFlg
	 * @param enabled
	 * @return
	 */
	private String henkouCheckBoxStr(String henkouFlg, Boolean enabled, int i){
		
		String checkBoxStr;
		if(henkouFlg.equals("1")){
			// チェックあり
			if(enabled == true){
				// 無効
				checkBoxStr = "<INPUT type='checkbox' checked='checked' disabled='disabled' name='henkouChkVal' id='henkouChkVal" + i + "' value='" + i + "'>";
			}else{
				// 有効
				checkBoxStr = "<INPUT type='checkbox' checked='checked' name='henkouChkVal' id='henkouChkVal" + i + "' value='" + i + "'>";
			}
		}else{
			// チェックなし
			if(enabled == true){
				// 無効
				checkBoxStr = "<INPUT type='checkbox' disabled='disabled' name='henkouChkVal' id='henkouChkVal" + i + "' value='" + i + "'>";
			}else{
				// 有効
				checkBoxStr = "<INPUT type='checkbox' name='henkouChkVal' id='henkouChkVal" + i + "' value='" + i + "'>";
			}
		}
		return  checkBoxStr;
	}
	

	/**
	 * 配列の中身チェック
	 * 
	 * @param strArr
	 * @return
	 */
	public String checkBoxcheck(String[] strArr){
		String str = null;
		if(strArr != null && strArr.length != 0){
			str = strArr[0];
		}
		return str;
	}		
	
}
