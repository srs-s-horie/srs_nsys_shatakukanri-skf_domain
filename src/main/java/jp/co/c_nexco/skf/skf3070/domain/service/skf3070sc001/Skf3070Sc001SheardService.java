/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3070.domain.service.skf3070sc001;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3070Sc001.Skf3070Sc001GetOwnerContractListExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3070Sc001.Skf3070Sc001GetOwnerContractListExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3070Sc001.Skf3070Sc001GetOwnerContractListExpRepository;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.skf3070.domain.dto.skf3070Sc001common.Skf3070Sc001CommonDto;

/**
 * Skf3070Sc001 法定調書データ管理画面の共通処理クラス。
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf3070Sc001SheardService {

	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfDropDownUtils skfDropDownUtils;
	@Autowired
	private Skf3070Sc001GetOwnerContractListExpRepository skf3070Sc001GetOwnerContractListExpRepository;

	// 戻り値Map用定数
	private static final String KEY_TARGET_YEAR_LIST = "TARGET_YEAR_LIST";
	private static final String KEY_BUSINESS_KBN_LIST = "BUSINESS_KBN_LIST";
	private static final String KEY_ACCEPT_FLG_LIST = "ACCEPT_FLG_LIST";

	/**
	 * 代理人情報一覧を取得して、画面のlistTableに表示するための情報を作成する。
	 * 
	 * @param param
	 * @param initDto
	 * @return listTableに表示する情報を格納したList
	 */
	protected List<Map<String, Object>> getListTableData(Skf3070Sc001GetOwnerContractListExpParameter param,
			Skf3070Sc001CommonDto dto) {

		// 戻り値の表示用リスト
		List<Map<String, Object>> tableDataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> tmpMap = new HashMap<String, Object>();
		Map<String, Date> lastUpdateDateMap = new HashMap<String, Date>();

		// 賃貸人（代理人）情報検索を実行
		List<Skf3070Sc001GetOwnerContractListExp> ownerExpList = getOwnerContractInfo(param);

		// 賃貸人（代理人）情報がある場合
		if (ownerExpList != null && ownerExpList.size() > 0) {
			// 賃貸人（代理人）情報を作成する
			String checkOwnerNo = CodeConstant.NONE; // 判定用賃貸人（代理人）番号
			for (Skf3070Sc001GetOwnerContractListExp list : ownerExpList) {
				tmpMap = new HashMap<String, Object>();
				if (NfwStringUtils.isEmpty(checkOwnerNo)) {
					// 判定用賃貸人（代理人）番号が空の場合
					checkOwnerNo = list.getOwnerNo(); // 賃貸人（代理人）番号

					// 表示内容をmapに格納
					tmpMap.put("ownerNo", HtmlUtils.htmlEscape(list.getOwnerNo()));
					tmpMap.put("ownerName", HtmlUtils.htmlEscape(list.getOwnerName()));
					tmpMap.put("ownerNameKk", HtmlUtils.htmlEscape(list.getOwnerNameKk()));
					tmpMap.put("address", HtmlUtils.htmlEscape(list.getAddress()));
					if (NfwStringUtils.isNotEmpty(list.getBusinessKbn())) {
						// 個人法人区分汎用コードからセット
						String businessKbnName = skfGenericCodeUtils.getGenericCodeNameReverse(
								FunctionIdConstant.GENERIC_CODE_KOJIN_HOJIN_KUBUN, list.getBusinessKbn());
						tmpMap.put("businessKbn", HtmlUtils.htmlEscape(businessKbnName));
					}
					if (NfwStringUtils.isNotEmpty(list.getAcceptFlg())) {
						// 賃貸人（代理人）個人番号督促状況汎用コードからセット
						String acceptFlgName = skfGenericCodeUtils.getGenericCodeNameReverse(
								FunctionIdConstant.GENERIC_CODE_AGENT_INDIVIDUAL_NUMBER_DEMAND_SITUATION,
								list.getBusinessKbn());
						tmpMap.put("acceptFlg", HtmlUtils.htmlEscape(acceptFlgName));
					}
					if (NfwStringUtils.isNotEmpty(list.getDataCount())) {
						// 所有物件件数
						tmpMap.put("propertiesOwnedCnt", HtmlUtils.htmlEscape(list.getDataCount()));
					} else {
						tmpMap.put("propertiesOwnedCnt", HtmlUtils.htmlEscape(CodeConstant.STRING_ZERO));
					}

					tmpMap.put("edit", "");
					tmpMap.put("properties", "");
					// 排他チェック用に最終更新日を取得
					lastUpdateDateMap.put("skf3070_t_owner_info_" + list.getOwnerNo(), list.getLastUpdateDate());
					tableDataList.add(tmpMap);

				} else if (!checkOwnerNo.equals(list.getOwnerNo())) {
					// 判定用賃貸人（代理人）番号と賃貸人（代理人）番号が同じではない場合
					// 表示内容をmapに格納
					tmpMap.put("ownerNo", HtmlUtils.htmlEscape(list.getOwnerNo()));
					tmpMap.put("ownerName", HtmlUtils.htmlEscape(list.getOwnerName()));
					tmpMap.put("ownerNameKk", HtmlUtils.htmlEscape(list.getOwnerNameKk()));
					tmpMap.put("address", HtmlUtils.htmlEscape(list.getAddress()));
					if (NfwStringUtils.isNotEmpty(list.getBusinessKbn())) {
						// 個人法人区分汎用コードからセット
						String businessKbnName = skfGenericCodeUtils.getGenericCodeNameReverse(
								FunctionIdConstant.GENERIC_CODE_KOJIN_HOJIN_KUBUN, list.getBusinessKbn());
						tmpMap.put("businessKbn", HtmlUtils.htmlEscape(businessKbnName));
					}
					if (NfwStringUtils.isNotEmpty(list.getAcceptFlg())) {
						// 賃貸人（代理人）個人番号督促状況汎用コードからセット
						String acceptFlgName = skfGenericCodeUtils.getGenericCodeNameReverse(
								FunctionIdConstant.GENERIC_CODE_AGENT_INDIVIDUAL_NUMBER_DEMAND_SITUATION,
								list.getAcceptFlg());
						tmpMap.put("acceptFlg", HtmlUtils.htmlEscape(acceptFlgName));
					}
					if (NfwStringUtils.isNotEmpty(list.getDataCount())) {
						// 所有物件件数
						tmpMap.put("propertiesOwnedCnt", HtmlUtils.htmlEscape(list.getDataCount()));
					} else {
						tmpMap.put("propertiesOwnedCnt", HtmlUtils.htmlEscape(CodeConstant.STRING_ZERO));
					}

					tmpMap.put("edit", "");
					tmpMap.put("properties", "");
					// 排他チェック用に最終更新日を取得
					lastUpdateDateMap.put("skf3070_t_owner_info_" + list.getOwnerNo(), list.getLastUpdateDate());
					tableDataList.add(tmpMap);

					checkOwnerNo = list.getOwnerNo(); // 判定用賃貸人（代理人）番号
				}
			}

			// 表示用リストに格納
			dto.setLastUpdateDateMap(lastUpdateDateMap);
		}

		return tableDataList;
	}

	/**
	 * 賃貸人（代理人）契約情報を取得する。
	 * 
	 * @param param
	 * @return
	 */
	private List<Skf3070Sc001GetOwnerContractListExp> getOwnerContractInfo(
			Skf3070Sc001GetOwnerContractListExpParameter param) {

		List<Skf3070Sc001GetOwnerContractListExp> ownerExpList = new ArrayList<Skf3070Sc001GetOwnerContractListExp>();
		ownerExpList = skf3070Sc001GetOwnerContractListExpRepository.getOwnerContractInfo(param);
		return ownerExpList;
	}

	/**
	 * ドロップダウンリストの値設定
	 * 
	 * @param Skf3070Sc001CommonDto
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	protected void getDoropDownList(Skf3070Sc001CommonDto dto) throws ParseException {

		List<Map<String, Object>> targetYearList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> businessKbnList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> acceptFlgList = new ArrayList<Map<String, Object>>();

		// ドロップダウンリストを取得する
		Map<String, Object> returnMap = getDropDownLists(dto);

		// 画面表示するドロップダウンリストを取得
		targetYearList.addAll((List<Map<String, Object>>) returnMap.get(KEY_TARGET_YEAR_LIST));
		businessKbnList.addAll((List<Map<String, Object>>) returnMap.get(KEY_BUSINESS_KBN_LIST));
		acceptFlgList.addAll((List<Map<String, Object>>) returnMap.get(KEY_ACCEPT_FLG_LIST));

		// dtoに値をセット
		dto.setDdlTargetYearList(targetYearList);
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
	private Map<String, Object> getDropDownLists(Skf3070Sc001CommonDto dto) throws ParseException {

		// 戻り値用Mapのインスタンス生成
		Map<String, Object> returnMap = new HashMap<String, Object>();

		// 対象年ドロップダウンリストの設定
		List<Map<String, Object>> returnTargetYearList = new ArrayList<Map<String, Object>>();
		returnTargetYearList.addAll(skfDropDownUtils.getDdlYear(dto.getStandardYear(), dto.getTargetYear(), 10,
				CodeConstant.NONE, false, false));
		returnMap.put(KEY_TARGET_YEAR_LIST, returnTargetYearList);

		// 個人法人区分ドロップダウンリストの設定
		List<Map<String, Object>> returnBusinessKbnList = new ArrayList<Map<String, Object>>();
		returnBusinessKbnList.addAll(skfDropDownUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_KOJIN_HOJIN_KUBUN, dto.getBusinessKbn(), true));
		returnMap.put(KEY_BUSINESS_KBN_LIST, returnBusinessKbnList);

		// 賃貸人（代理人）個人番号督促状況ドロップダウンリストの設定
		List<Map<String, Object>> returnAcceptFlgList = new ArrayList<Map<String, Object>>();
		returnAcceptFlgList.addAll(skfDropDownUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_AGENT_INDIVIDUAL_NUMBER_DEMAND_SITUATION, dto.getAcceptFlg(), true));
		returnMap.put(KEY_ACCEPT_FLG_LIST, returnAcceptFlgList);

		return returnMap;
	}

}
