/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc005;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc005.Skf3010Sc005GetBihinInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc005.Skf3010Sc005GetBihinInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc005.Skf3010Sc005GetRoomInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc005.Skf3010Sc005GetRoomInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuRoomBihin;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuRoomBihinKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc005.Skf3010Sc005GetBihinInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc005.Skf3010Sc005GetRoomInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuRoomBihinRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;

/**
 * Skf3010Sc004SharedService 社宅部屋登録内共通クラス
 * 
 * @author NEXCOシステムズ
 *
 */

@Service
public class Skf3010Sc005SharedService {


	@Autowired
	private SkfDropDownUtils ddlUtils;
	@Autowired
	private Skf3010Sc005GetRoomInfoExpRepository skf3010Sc005GetRoomInfoExpRepository;
	@Autowired
	private Skf3010Sc005GetBihinInfoExpRepository skf3010Sc005GetBihinInfoExpRepository;
	@Autowired
	private Skf3010MShatakuRoomBihinRepository skf3010MShatakuRoomBihinRepository;

	/**
	 * ドロップダウンリストに設定するリストを取得する.
	 * 
	 * @param originalKikaku 本来規格
	 * @param originalKikakuList
	 * @param originalAuse　本来用途
	 * @param auseList
	 * @param lendKbn　貸与区分
	 * @param lendList
	 * @param coldExemptionKbn　寒冷地減免事由区分
	 * @param coldExemptionKbnList
	 */
	public void getDoropDownList(String originalKikaku,List<Map<String, Object>> kikakuList,
			String originalAuse, List<Map<String, Object>> auseList, 
			String lendKbn,	List<Map<String, Object>> lendList,
			String coldExemptionKbn,List<Map<String, Object>> coldExemptionKbnList) {

		boolean isFirstRowEmpty = true;

		//本来規格リスト
		kikakuList.clear();
		kikakuList.addAll(ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_KIKAKU_KBN, originalKikaku,
				isFirstRowEmpty));
		
		// 本来用途リスト
		auseList.clear();
		auseList.addAll(ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_AUSE_KBN, originalAuse,
				isFirstRowEmpty));

		// 貸与区分リスト
		lendList.clear();
		lendList.addAll(ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_LEND_KBN, lendKbn,
				isFirstRowEmpty));
		
		// 寒冷地減免事由区分リスト
		coldExemptionKbnList.clear();
		coldExemptionKbnList.addAll(ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_KANREITI_KBN, coldExemptionKbn,
						isFirstRowEmpty));	

	}

	/**
	 * 部屋情報を取得
	 * 
	 * @param shatakuKanriNo 社宅管理番号
	 * @param shatakuRoomKanriNo 部屋管理番号
	 * @return
	 */
	public Skf3010Sc005GetRoomInfoExp getRoomInfo(String shatakuKanriNo,String shatakuRoomKanriNo){
		LogUtils.debugByMsg("部屋情報取得処理開始");
		LogUtils.debugByMsg("引数から取得した値：" + "社宅管理番号=" + shatakuKanriNo + ",社宅部屋管理番号=" + shatakuRoomKanriNo);
		
		int resultCount = 0;
		Skf3010Sc005GetRoomInfoExpParameter param = new Skf3010Sc005GetRoomInfoExpParameter();
		param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
		param.setShatakuRoomKanriNo(Long.parseLong(shatakuRoomKanriNo));
		
		List<Skf3010Sc005GetRoomInfoExp> roomInfoList = new ArrayList<Skf3010Sc005GetRoomInfoExp>();
		roomInfoList = skf3010Sc005GetRoomInfoExpRepository.getRoomInfo(param);

		// 取得レコード数を設定
		resultCount = roomInfoList.size();

		// 取得データレコード数判定
		if (resultCount == 0 ) {
			// 取得データレコード数が0件の場合、何もせず処理終了
			return null;
		}
		
		return roomInfoList.get(0);
	}
	
	/**
	 * 備品情報リストの取得.
	 * @param shatakuKanriNo 社宅管理番号
	 * @param shatakuRoomKanriNo 社宅部屋管理番号
	 * @param bihinListData 備品情報リスト
	 * @param hdnBihinStatusList 非表示備品情報リスト
	 * @return
	 */
	public int setBihinInfo(String shatakuKanriNo,String shatakuRoomKanriNo,
			List<Map<String, Object>> bihinListData,List<Map<String, Object>> hdnBihinStatusList){
		
		LogUtils.debugByMsg("備品情報取得処理開始");
		LogUtils.debugByMsg("引数から取得した値：" + "社宅管理番号=" + shatakuKanriNo + ",社宅部屋管理番号=" + shatakuRoomKanriNo);
		
		int resultCount = 0;
		Skf3010Sc005GetBihinInfoExpParameter param = new Skf3010Sc005GetBihinInfoExpParameter();
		param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
		param.setShatakuRoomKanriNo(Long.parseLong(shatakuRoomKanriNo));
		
		List<Skf3010Sc005GetBihinInfoExp> bihinInfoList = new ArrayList<Skf3010Sc005GetBihinInfoExp>();
		bihinInfoList = skf3010Sc005GetBihinInfoExpRepository.getBihinInfo(param);

		// 取得レコード数を設定
		resultCount = bihinInfoList.size();

		// 取得データレコード数判定
		if (resultCount == 0 ) {
			// 取得データレコード数が0件の場合、何もせず処理終了
			return resultCount;
		}
		
		//備品情報リスト取得
		bihinListData.clear();
		bihinListData.addAll(getBihinListColumn(bihinInfoList));
		//非表示備品リスト取得
		hdnBihinStatusList.clear();
		hdnBihinStatusList.addAll(getHdnBihinListColumn(bihinInfoList));
		
		return resultCount;
	}
	
	/**
	 * 新規時の備品情報リストの取得.
	 * @param shatakuKanriNo 社宅管理番号
	 * @param shatakuRoomKanriNo 社宅部屋管理番号
	 * @param bihinListData 備品情報リスト
	 * @param hdnBihinStatusList 非表示備品情報リスト
	 * @return
	 */
	public int setBihinInfoOfShinki(String shatakuKanriNo,String shatakuRoomKanriNo
			,List<Map<String, Object>> bihinListData,List<Map<String, Object>> hdnBihinStatusList){
		
		LogUtils.debugByMsg("備品情報（新規）取得処理開始");
		LogUtils.debugByMsg("引数から取得した値：" + "社宅管理番号=" + shatakuKanriNo + ",社宅部屋管理番号=" + shatakuRoomKanriNo);
		
		int resultCount = 0;
		Skf3010Sc005GetBihinInfoExpParameter param = new Skf3010Sc005GetBihinInfoExpParameter();
		param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
		
		List<Skf3010Sc005GetBihinInfoExp> bihinInfoList = new ArrayList<Skf3010Sc005GetBihinInfoExp>();
		bihinInfoList = skf3010Sc005GetBihinInfoExpRepository.getBihinInfoOfShinki(param);

		// 取得レコード数を設定
		resultCount = bihinInfoList.size();

		// 取得データレコード数判定
		if (resultCount == 0 ) {
			// 取得データレコード数が0件の場合、何もせず処理終了
			return resultCount;
		}
		
		//備品情報リスト取得
		bihinListData.clear();
		bihinListData.addAll(getBihinListColumn(bihinInfoList));
		//非表示備品リスト取得
		hdnBihinStatusList.clear();
		hdnBihinStatusList.addAll(getHdnBihinListColumn(bihinInfoList));
		
		return resultCount;
	}
	
	/**
	 * 備品リストに出力するリストを取得する。
	 * 
	 * @param originList
	 * @return リストテーブルに出力するリスト
	 */
	private List<Map<String, Object>> getBihinListColumn(List<Skf3010Sc005GetBihinInfoExp> originList) {

		List<Map<String, Object>> setViewList = new ArrayList<Map<String, Object>>();

		List<Map<String, Object>> statusList = ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_BIHINSTATUS_KBN, "",false);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
		
		for (int i = 0; i < originList.size(); i++) {
			String bihinStatus = "";

			Skf3010Sc005GetBihinInfoExp tmpData = originList.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();

			if(!tmpData.getDispFlg().equals("0")){
				tmpMap.put("bihinCode", HtmlUtils.htmlEscape(tmpData.getBihinCd()));
				tmpMap.put("bihinName", HtmlUtils.htmlEscape(tmpData.getBihinName()));
				// 「備付状況」の設定
				if (tmpData.getBihinStatusKbn() != null) {
					bihinStatus = tmpData.getBihinStatusKbn();
				}else{
					//未指定時は「なし(0)」を選択
					bihinStatus = "0";
				}
				//tmpMap.put("bihinStatus","<imui:select id='bihinStatusList' name='bihinStatusList' list='${form.bihinStatusList}' width='110'/>");
				//tmpMap.put("bihinStatus","<select id='bihinStatus" + i + "' name='bihinStatus" + i + "'> <option value='0'>なし</option> <option value='1'>備付</option><option value='2' selected>レンタル</option></select>");
				
				String statusListCode = createBihinStatusSelect(bihinStatus,statusList);
				tmpMap.put("bihinStatus","<select id='bihinStatus" + i + "' name='bihinStatus" + i + "'>" + statusListCode + "</select>");
				tmpMap.put("bihinLatestStatus", HtmlUtils.htmlEscape(tmpData.getBihinLatestStatusKbn()));
				tmpMap.put("updateDate", HtmlUtils.htmlEscape(dateFormat.format(tmpData.getUpdateDate())));

				setViewList.add(tmpMap);
			}
		}

		return setViewList;
	}
	
	/**
	 * 非表示備品リストに出力するリストを取得する。
	 * 
	 * @param originList
	 * @return リストテーブルに出力するリスト
	 */
	private List<Map<String, Object>> getHdnBihinListColumn(List<Skf3010Sc005GetBihinInfoExp> originList) {

		List<Map<String, Object>> setViewList = new ArrayList<Map<String, Object>>();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
		
		for (int i = 0; i < originList.size(); i++) {
			String bihinStatus = "";

			Skf3010Sc005GetBihinInfoExp tmpData = originList.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();

			if(tmpData.getDispFlg().equals("0")){
				//DispFlgが0（非表示）
				tmpMap.put("bihinCode", tmpData.getBihinCd());
				tmpMap.put("bihinName", tmpData.getBihinName());
				// 「備付状況」の設定
				if (tmpData.getBihinStatusKbn() != null) {
					bihinStatus = tmpData.getBihinStatusKbn();
				}else{
					//未指定時は「なし(1)」を選択
					bihinStatus = "1";
				}
				tmpMap.put("bihinStatus",bihinStatus);
				tmpMap.put("bihinLatestStatus", tmpData.getBihinLatestStatusKbn());
				tmpMap.put("updateDate", dateFormat.format(tmpData.getUpdateDate()));

				setViewList.add(tmpMap);
			}
		}

		return setViewList;
	}
	
	/**
	 * 備付状況SelectのHTMLコード生成処理
	 * @param bihinStatus 備付状況
	 * @param statusList 備付状況リスト
	 * @return
	 */
	private String createBihinStatusSelect(String bihinStatus,List<Map<String, Object>> statusList){
		String returnListCode = "";
		
		for(Map<String, Object> obj : statusList){
			String value = obj.get("value").toString();
			String label = obj.get("label").toString();
			if(bihinStatus.compareTo(value)==0){
				//備付状況とリスト値が一致する場合、選択中にする
				returnListCode += "<option value='" + value + "' selected>" + label + "</option>";
			}else{
				returnListCode += "<option value='" + value + "'>" + label + "</option>";	
			}
			
		}
				
		return returnListCode;
	}
	
	/**
	 * 備品情報リストの復元
	 * 備品情報リストと非表示備品情報リストを1つのリストに統合する
	 * @param bihinInfoTxt
	 * @param hdnBihinStatusList
	 * @return
	 */
	public List<Map<String,Object>> createShatakuRoomBihinInfo(String bihinInfoTxt,List<Map<String, Object>> hdnBihinStatusList){
		//返却リスト初期化
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		
		//備品情報リスト（表示分）
		//画面からは文字列で取得するので、まず行ごとに分割
		String[] infoList = bihinInfoTxt.split(";");
		for(String info : infoList){
			//行データを項目ごとに分割
			String[] bihin = info.split(",");
			if(bihin.length >= 5){
				Map<String, Object> forListMap = new HashMap<String, Object>();
				forListMap.put("bihinCode", bihin[0]);
				forListMap.put("bihinName", bihin[1]);
				forListMap.put("bihinStatus", bihin[2]);
				forListMap.put("bihinLatestStatus", bihin[3]);
				if(!bihin[4].isEmpty()){
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
					try{
						forListMap.put("updateDate", dateFormat.parse(bihin[4]));	
					}catch(Exception ex){
						//文字列→Dateの変換エラー時、null設定
						forListMap.put("updateDate", null);
					}
					
				}else{
					forListMap.put("updateDate", null);
				}
				
				resultList.add(forListMap);
			}
		}
		
		//非表示分
		for(Map<String, Object> map : hdnBihinStatusList){
			Map<String, Object> forListMap = new HashMap<String, Object>();
			forListMap.put("bihinCode", map.get("bihinCode"));
			forListMap.put("bihinName", map.get("bihinName"));
			forListMap.put("bihinStatus", map.get("bihinStatus"));
			forListMap.put("bihinLatestStatus", map.get("bihinLatestStatus"));
			if(!map.get("updateDate").toString().isEmpty()){
				//更新日時はDate型に変換して格納
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
				try{
					forListMap.put("updateDate", dateFormat.parse(map.get("updateDate").toString()));	
				}catch(Exception ex){
					forListMap.put("updateDate", null);
				}
				
			}else{
				forListMap.put("updateDate", null);
			}
			
			resultList.add(forListMap);
		}
		
		return resultList;
	}
	
	/**
	 * 社宅部屋備品情報の更新日を一括でチェックする.
	 * 不一致がある場合はfalseを返却する.
	 * @param bihinInfoList
	 * @return
	 */
	public boolean checkRoomBihinInfoForUpdate(Long shatakuKanriNo,Long shatakuRoomKanriNo,List<Map<String,Object>> bihinInfoList){
		
		try{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
			
			//備品
			for(Map<String,Object> map : bihinInfoList){
				
				//更新処理を行う。
				//排他チェック
				Skf3010MShatakuRoomBihinKey bihinKey = new Skf3010MShatakuRoomBihinKey();
				bihinKey.setShatakuKanriNo(shatakuKanriNo);
				bihinKey.setShatakuRoomKanriNo(shatakuRoomKanriNo);
				bihinKey.setBihinCd(map.get("bihinCode").toString());
				Skf3010MShatakuRoomBihin bihinInfo = skf3010MShatakuRoomBihinRepository.selectByPrimaryKey(bihinKey);
				
				Date mapDate = dateFormat.parse(map.get("updateDate").toString());	
				LogUtils.debugByMsg("mapUpdateDate：" + mapDate);
				LogUtils.debugByMsg("bihinInfoUpdateDate：" + bihinInfo.getUpdateDate());
				if(mapDate.compareTo(bihinInfo.getUpdateDate()) != 0){
					//更新日時が不一致の場合、false返却
					return false;
				}
			}
		}
		catch(Exception ex){
			LogUtils.debugByMsg("exception：" + ex.getMessage());
			return false;
		}
		
		//更新OK
		return true;
	}
	
}
