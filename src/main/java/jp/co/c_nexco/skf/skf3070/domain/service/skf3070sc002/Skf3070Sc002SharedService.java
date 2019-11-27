/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3070.domain.service.skf3070sc002;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.skf3070.domain.dto.skf3070Sc002common.Skf3070Sc002CommonDto;

/**
 * Skf3070Sc002 賃貸人（代理人）情報登録画面の共通処理クラス。
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf3070Sc002SharedService {

	// 戻り値Map用定数
	private static final String KEY_BUSINESS_KBN_LIST = "BUSINESS_KBN_LIST";
	private static final String KEY_ACCEPT_FLG_LIST = "ACCEPT_FLG_LIST";

	// 最終更新日付のキャッシュキー
	protected static final String KEY_LAST_UPDATE_DATE = "skf3070_t_owner_info";

	@Autowired
	private SkfDropDownUtils skfDropDownUtils;

	/**
	 * ドロップダウンリストの値設定
	 * 
	 * @param Skf3070Sc002CommonDto
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	protected void getDropDownList(Skf3070Sc002CommonDto dto) throws ParseException {

		List<Map<String, Object>> businessKbnList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> acceptFlgList = new ArrayList<Map<String, Object>>();

		// ドロップダウンリストを取得する
		Map<String, Object> returnMap = getDropDownLists(dto.getBusinessKbn(), dto.getAcceptFlg());

		// 画面表示するドロップダウンリストを取得
		businessKbnList.addAll((List<Map<String, Object>>) returnMap.get(KEY_BUSINESS_KBN_LIST));
		acceptFlgList.addAll((List<Map<String, Object>>) returnMap.get(KEY_ACCEPT_FLG_LIST));

		// dtoに値をセット
		dto.setDdlBusinessKbnList(businessKbnList);
		dto.setDdlAcceptFlgList(acceptFlgList);
	}

	/**
	 * ドロップダウンリストの値を取得
	 * 
	 * @param dto
	 * @return
	 * @throws ParseException
	 */
	private Map<String, Object> getDropDownLists(String businessKbn, String acceptFlg) throws ParseException {

		// 戻り値用Mapのインスタンス生成
		Map<String, Object> returnMap = new HashMap<String, Object>();

		// 個人法人区分ドロップダウンリストの設定
		List<Map<String, Object>> returnBusinessKbnList = new ArrayList<Map<String, Object>>();
		returnBusinessKbnList.addAll(skfDropDownUtils
				.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_KOJIN_HOJIN_KUBUN, businessKbn, true));
		returnMap.put(KEY_BUSINESS_KBN_LIST, returnBusinessKbnList);

		// 賃貸人（代理人）個人番号督促状況ドロップダウンリストの設定
		List<Map<String, Object>> returnAcceptFlgList = new ArrayList<Map<String, Object>>();
		returnAcceptFlgList.addAll(skfDropDownUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_AGENT_INDIVIDUAL_NUMBER_DEMAND_SITUATION, acceptFlg, true));
		returnMap.put(KEY_ACCEPT_FLG_LIST, returnAcceptFlgList);

		return returnMap;
	}

}
