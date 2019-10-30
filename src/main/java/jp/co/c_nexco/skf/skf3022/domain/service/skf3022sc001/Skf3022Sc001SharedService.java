/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc001;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc001.Skf3022Sc001GetShatakuRoomExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc001.Skf3022Sc001GetShatakuRoomExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc001.Skf3022Sc001GetShatakuRoomExpRepository;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;

/**
 * Skf3022Sc001SharedService 社宅部屋入力支援共通処理クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3022Sc001SharedService {

	@Autowired
	private Skf3022Sc001GetShatakuRoomExpRepository skf3022Sc001GetShatakuRoomExpRepository;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfDropDownUtils ddlUtils;

	@Value("${skf3022.skf3022_sc001.max_search_count}")
	private String maxCount;

	/**
	 * ドロップダウンリストに設定するリストを取得する。<br>
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param sc001EmptyRoomSelect			空き部屋ドロップダウン選択値
	 * @param sc001EmptyRoomSelectList		*空き部屋ドロップダウンリスト
	 * @param sc001EmptyParkingSelect		空き駐車場ドロップダウン選択値
	 * @param sc001EmptyParkingSelectList	*空き駐車場ドロップダウンリスト
	 * @param sc001KikakuSelecte			規格ドロップダウン選択値
	 * @param sc001KikakuSelecteList		*規格ドロップダウンリスト
	 * @param sc001YoutoSelect				用途ドロップダウン選択値
	 * @param sc001YoutoSelectList			*用途ドロップダウンリスト
	 */
	public void getDoropDownList(
			String sc001EmptyRoomSelect, List<Map<String, Object>> sc001EmptyRoomSelectList,
			String sc001EmptyParkingSelect, List<Map<String, Object>> sc001EmptyParkingSelectList,
			String sc001KikakuSelecte, List<Map<String, Object>> sc001KikakuSelecteList,
			String sc001YoutoSelect, List<Map<String, Object>> sc001YoutoSelectList) {

		boolean isFirstRowEmpty = true;

		// 空き部屋ドロップダウンリスト
		sc001EmptyRoomSelectList.clear();
		sc001EmptyRoomSelectList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_AKIROOM_KBN,sc001EmptyRoomSelect, isFirstRowEmpty));

		// 空き駐車場ドロップダウンリスト
		sc001EmptyParkingSelectList.clear();
		sc001EmptyParkingSelectList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_AKIPARKING_KBN,sc001EmptyParkingSelect, isFirstRowEmpty));

		//規格ドロップダウンリスト
		sc001KikakuSelecteList.clear();
		sc001KikakuSelecteList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_KIKAKU_KBN, sc001KikakuSelecte, isFirstRowEmpty));

		// 用途ドロップダウンリスト
		sc001YoutoSelectList.clear();
		sc001YoutoSelectList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_AUSE_KBN, sc001YoutoSelect, isFirstRowEmpty));
	}

	/**
	 * 社宅部屋を取得する
	 * 
	 * 「※」項目はアドレスとして戻り値になる。 
	 * 
	 * @param sc001KikakuSelecte		検索条件：規格
	 * @param shatakuName				検索条件：社宅名
	 * @param sc001YoutoSelect			検索条件：用途
	 * @param roomNo					検索条件：部屋番号
	 * @param sc001EmptyRoomSelect		検索条件：空き部屋
	 * @param sc001EmptyParkingSelect	検索条件：空き駐車場
	 * @param listTableData				*社宅部屋リスト
	 * @return
	 * @throws IllegalAccessException
	 * @throws Exception
	 */
	public int getShatakuRoom(String sc001KikakuSelecte, String shatakuName,
			String sc001YoutoSelect, String roomNo, String sc001EmptyRoomSelect, String sc001EmptyParkingSelect
			, List<Map<String, Object>> listTableData)
			throws IllegalAccessException, Exception {

		// パラメータ設定
		List<Skf3022Sc001GetShatakuRoomExp> resultList = new ArrayList<Skf3022Sc001GetShatakuRoomExp>();
		Skf3022Sc001GetShatakuRoomExpParameter param = new Skf3022Sc001GetShatakuRoomExpParameter();
		param.setShatakuName(escapeParameter(shatakuName));
		param.setYoto(sc001YoutoSelect);
		param.setKikaku(sc001KikakuSelecte);
		param.setRoomNo(escapeParameter(roomNo));
		param.setLend(sc001EmptyRoomSelect);
		param.setJokyo(sc001EmptyParkingSelect);

		//件数の取得
		int roomCount = skf3022Sc001GetShatakuRoomExpRepository.getShatakuRoomCount(param);
		if(roomCount == 0){
			// 0件
			resultList.clear();
		}else if(roomCount > Integer.parseInt(maxCount)){
			// 検索結果が指定されている最大件数を超えている場合
			roomCount = -1;
			resultList.clear();
		}else{
			// 検索条件をもとに、社宅部屋の一覧情報を取得し、「社宅名」「部屋番号」の昇順で表示する
			resultList = skf3022Sc001GetShatakuRoomExpRepository.getShatakuRoom(param);			
			roomCount = resultList.size();
		}

		listTableData.clear();
		listTableData.addAll(createListTable(resultList));

		return roomCount;
	}

	/**
	 * 取得情報からリストテーブルのデータを作成します
	 * 
	 * @param shainInfoList
	 * @return
	 */
	public List<Map<String, Object>> createListTable(List<Skf3022Sc001GetShatakuRoomExp> roomList) {

		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		if (roomList.size() <= 0) {
			return returnList;
		}

		// 用途区分
		Map<String, String> genericCodeAuseKbn = new HashMap<String, String>();
		genericCodeAuseKbn = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_AUSE_KBN);
		// 規格区分
		Map<String, String> genericCodeKikakuKbn = new HashMap<String, String>();
		genericCodeKikakuKbn = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_KIKAKU_KBN);

		for (Skf3022Sc001GetShatakuRoomExp roomInfo : roomList) {
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			tmpMap.put("colSelect", roomInfo.getAkiheyaState());
			tmpMap.put("colhdnSelect", roomInfo.getAkiheyaState());
			tmpMap.put("colShatakuName", HtmlUtils.htmlEscape(roomInfo.getShatakuName()));
			tmpMap.put("colRoomNo", HtmlUtils.htmlEscape(roomInfo.getRoomNo()));
			tmpMap.put("colAuse", HtmlUtils.htmlEscape(genericCodeAuseKbn.get(roomInfo.getOriginalAuse())));
			tmpMap.put("colKikaku", HtmlUtils.htmlEscape(genericCodeKikakuKbn.get(roomInfo.getOriginalKikaku())));
			tmpMap.put("colEmptyRoom", HtmlUtils.htmlEscape(roomInfo.getAkiheya()));
			tmpMap.put("colEmptyParking", HtmlUtils.htmlEscape(roomInfo.getAkichushajo()));
			tmpMap.put("colShatakuKanriNo", roomInfo.getShatakuKanriNo());
			tmpMap.put("colRoomKanriNo", roomInfo.getShatakuRoomKanriNo());
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
