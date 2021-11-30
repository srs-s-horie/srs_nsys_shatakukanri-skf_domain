/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc002;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc002.Skf3022Sc002GetChushajoInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc002.Skf3022Sc002GetChushajoInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc002.Skf3022Sc002GetChushajoInfoExpRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;

/**
 * Skf3022Sc002SharedService 駐車場入力支援共通処理クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3022Sc002SharedService {

	@Autowired
	private Skf3022Sc002GetChushajoInfoExpRepository skf3022Sc002GetChushajoInfoExpRepository;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	
	@Value("${skf3022.skf3022_sc002.max_search_count}")
	private String maxCount;
	/**
	 * 駐車場情報を取得する
	 * @param shatakuNo
	 * @param shiyosha
	 * @param biko
	 * @param chushajo
	 * @param nidaishiyo
	 * @param kaisiDate
	 * @param year_month
	 * @param listTableData
	 * @throws IllegalAccessException
	 * @throws Exception
	 */
	public int getChushajoInfo(String shatakuNo, String shiyosha,
			String biko, boolean chushajo, boolean nidaishiyo, String kaisiDate, String year_month
			, List<Map<String, Object>> listTableData)
			throws IllegalAccessException, Exception {
		
		List<Skf3022Sc002GetChushajoInfoExp> resultList = new ArrayList<Skf3022Sc002GetChushajoInfoExp>();
		Skf3022Sc002GetChushajoInfoExpParameter param = new Skf3022Sc002GetChushajoInfoExpParameter();
		//パラメータ設定
		LogUtils.debugByMsg("検索条件 [社宅管理番号：" + shatakuNo + ",使用者:" + shiyosha + ",備考:" + biko 
				+ ",空き駐車場:" + chushajo + ",開始日:" + kaisiDate + ",システム年月:" + year_month + "]");
		param.setShatakuNo(Long.parseLong(shatakuNo));
		param.setShiyosha(escapeParameter(shiyosha));
		param.setBiko(escapeParameter(biko));
		param.setChushajo(chushajo);
		param.setNidaishiyo(nidaishiyo);
		param.setKaisiDate(kaisiDate);
		param.setYear_month(year_month);
		//件数の取得
		int parkingcount = skf3022Sc002GetChushajoInfoExpRepository.getChushajoCount(param);
		if(parkingcount == 0){
			//0件
			resultList.clear();
		}else if(parkingcount > Integer.parseInt(maxCount)){
			//検索結果が指定されている最大件数を超えている場合
			parkingcount = -1;
			resultList.clear();
		}else{
			//駐車場情報取得
			resultList = skf3022Sc002GetChushajoInfoExpRepository.getChushajoInfo(param);
			
			parkingcount = resultList.size();
		}

		
		listTableData.clear();
		listTableData.addAll(createListTable(resultList));
		
		return parkingcount;
	}

	/**
	 * 申請情報からリストテーブルのデータを作成します
	 * 
	 * @param shainInfoList
	 * @return
	 */
	public List<Map<String, Object>> createListTable(List<Skf3022Sc002GetChushajoInfoExp> parkingInfoList) {
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		if (parkingInfoList.size() <= 0) {
			return returnList;
		}
		
		//貸与区分
		Map<String, String> genericCodeLendKbn = new HashMap<String, String>();
		genericCodeLendKbn = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_LEND_KBN);
				
		//貸与状況
		Map<String, String> genericCodeLendJokyo = new HashMap<String, String>();
		genericCodeLendJokyo = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_LENDJOKYO);
		for (int i = 0; i < parkingInfoList.size(); i++) {
			Skf3022Sc002GetChushajoInfoExp tmpData = parkingInfoList.get(i);
			
            //貸与状況が「なし」かつ使用者の名前が取得できたかつ空き駐車場のチェックが入っている場合はスキップする
            if(CodeConstant.LEND_JOKYO_NASHI.equals(tmpData.getParkingLendJokyo())&&
                    NfwStringUtils.isNotBlank(tmpData.getName())&&chushajo){            
                continue;
            }

			Map<String, Object> tmpMap = new HashMap<String, Object>();
			tmpMap.put("colSelect", tmpData.getChushajoState()); 
			tmpMap.put("colhdnSelect", tmpData.getChushajoState()); 
			tmpMap.put("colParkingBlock", tmpData.getParkingBlock()); 
			String lendKbn = tmpData.getParkingLendKbn();
			tmpMap.put("colLendKbn", genericCodeLendKbn.get(lendKbn));
			//貸与状況が「なし」かつ    使用者の名前が取得できた場合は「貸与中」で設定する
            String lendJokyo = CodeConstant.DOUBLE_QUOTATION;
            if(CodeConstant.LEND_JOKYO_NASHI.equals(tmpData.getParkingLendJokyo())&&	
                    NfwStringUtils.isNotBlank(tmpData.getName())){
                lendJokyo = CodeConstant.LEND_JOKYO_TAIYOCHU;
                //貸与中の場合は駐車場選択を不可に変更に設定する
                tmpMap.put("colSelect", CodeConstant.STRING_ZERO); 
                tmpMap.put("colhdnSelect", CodeConstant.STRING_ZERO); 
            }else{
                lendJokyo = tmpData.getParkingLendJokyo();
            }		
			tmpMap.put("collendJokyo", genericCodeLendJokyo.get(lendJokyo));
			tmpMap.put("colShiyosha", tmpData.getName()); 
			tmpMap.put("colBiko", tmpData.getParkingBiko()); 
			tmpMap.put("colParkRentalAsjust", tmpData.getRental()); 
			tmpMap.put("colParkingKanriNo", tmpData.getParkingKanriNo()); 
			tmpMap.put("colhdnEndDate1", tmpData.getEndDate()); 
			tmpMap.put("colhdnEndDate2", tmpData.getEndDateOne()); 
			tmpMap.put("colhdnEndDate3", tmpData.getEndDateTwo()); 
			String hdnEndDate=CodeConstant.DOUBLE_QUOTATION;

			String hdnEndDate1 = createObjToString(tmpData.getEndDate());
			String hdnEndDate2 = createObjToString(tmpData.getEndDateOne());
			String hdnEndDate3 = createObjToString(tmpData.getEndDateTwo());
			if(hdnEndDate1.compareTo(hdnEndDate2) > 0){
				hdnEndDate = hdnEndDate1;
			}else{
				hdnEndDate = hdnEndDate2;
			}
			
			if(hdnEndDate.compareTo(hdnEndDate3) > 0){
				//hdnEndDate = hdnEndDate;
			}else{
				hdnEndDate = hdnEndDate3;
			}
			tmpMap.put("colhdnEndDate", hdnEndDate); 


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
