/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc003;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc003.Skf3090Sc003GetAgencyInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc003.Skf3090Sc003GetAgencyInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc003.Skf3090Sc003GetJigyoryoikiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc003.Skf3090Sc003GetJigyoryoikiInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3090Sc003.Skf3090Sc003GetAgencyInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3090Sc003.Skf3090Sc003GetJigyoryoikiInfoExpRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;

/**
 * Skf3090Sc003SharedService 事業領域マスタ登録内共通クラス
 * 
 * @author NEXCOシステムズ
 *
 */

@Service
public class Skf3090Sc003SharedService {

	@Autowired
	private SkfDropDownUtils ddlUtils;

	@Autowired
	private Skf3090Sc003GetJigyoryoikiInfoExpRepository skf3090Sc003GetJigyoryoikiInfoExpRepository;

	@Autowired
	private Skf3090Sc003GetAgencyInfoExpRepository skf3090Sc003GetAgencyInfoExpRepository;

	/**
	 * 管理会社のドロップダウンリストに設定するリストを取得する。<br>
	 * 「※」項目はアドレスとして戻り値になる。
	 */
	public void getDoropDownManageCompanyList(String selectedCompanyCd, List<Map<String, Object>> manageCompanyList) {

		boolean isFirstRowEmpty = true;

		// 管理会社ドロップダウンリストの設定
		manageCompanyList.clear();
		manageCompanyList.addAll(ddlUtils.getDdlCompanyByCd(selectedCompanyCd, isFirstRowEmpty));

	}

	/**
	 * リストテーブルに出力するリストを取得する。 （検索結果一覧の下にある追加領域）
	 * 
	 * @param companyCd
	 * @param manageCompanyKubunList
	 * @param businessAreaCd
	 * @oaram businessAreaName
	 * @param agency
	 * @param errorList
	 * @return リストテーブルに出力するリスト
	 */
	public List<Map<String, Object>> getListAddTableDataViewColumn(String companyCd,
			List<Map<String, Object>> manageCompanyKubunList, String businessAreaCd, String businessAreaName,
			String agency, List<Boolean> errorListAddTable) {

		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> setViewList = new ArrayList<Map<String, Object>>();
		Map<String, Object> tmpMap = new HashMap<String, Object>();

		// エラー状態チェック
		Boolean companyCdError = false;
		Boolean bAreaCdError = false;
		Boolean bAreaNameError = false;
		Boolean agencyCdError = false;
		if(CollectionUtils.isNotEmpty(errorListAddTable)){
			if(true == errorListAddTable.get(0)){
				companyCdError = true;
			}
			if(true == errorListAddTable.get(1)){
				bAreaCdError = true;
			}
			if(true == errorListAddTable.get(2)){
				bAreaNameError = true;
			}
			if(true == errorListAddTable.get(3)){
				agencyCdError = true;
			}
		}
		
		// 管理会社
		String companyListCode = "";
		if (NfwStringUtils.isNotEmpty(companyCd)) {
			companyListCode = createManageCompanySelect(companyCd, manageCompanyKubunList);
		} else {
			companyListCode = createManageCompanySelect(null, manageCompanyKubunList);
		}
		tmpMap.put("colAdd1", createColAdd1Str(companyListCode, companyCdError));

		// 事業領域コード
		String bAreaCd = "";
		if (NfwStringUtils.isNotEmpty(businessAreaCd)) {
			bAreaCd = businessAreaCd;
		}
		tmpMap.put("colAdd2", createColAdd2Str(bAreaCd, bAreaCdError));
		
		// 事業領域名
		String bAreaName = "";
		if (NfwStringUtils.isNotEmpty(businessAreaName)) {
			bAreaName = businessAreaName;
		}
		tmpMap.put("colAdd3", createColAdd3Str(bAreaName, bAreaNameError));
		
		// 管理機関
		if (NfwStringUtils.isNotEmpty(companyCd)) {
			boolean isFirstRowEmpty = true;
			String agencyListCode = "";
			if (NfwStringUtils.isNotEmpty(agency)) {
				agencyListCode = createAgencySelect(agency, getAgencyCdList(companyCd, isFirstRowEmpty));
			} else {
				agencyListCode = createAgencySelect(null, getAgencyCdList(companyCd, isFirstRowEmpty));
			}
			tmpMap.put("colAdd4",createColAdd4Str(agencyListCode, agencyCdError, false));
		} else {
			tmpMap.put("colAdd4",createColAdd4Str(null, agencyCdError, true));
		}
		// 追加
		tmpMap.put("colAdd5", "");
		// ダミー列
		tmpMap.put("colAdd6", "");

		setViewList.add(tmpMap);

		// リストテーブルに出力するリストを取得する
		listTableData.clear();
		listTableData.addAll(setViewList);

		return listTableData;
	}

	/**
	 * 管理会社ドロップダウンリストを生成するためにSelectのHTMLコード生成処理
	 * 
	 * @param companyCd
	 *            管理会社
	 * @param statusList
	 *            管理会社リスト
	 * @return
	 */
	public String createManageCompanySelect(String companyCd, List<Map<String, Object>> companyList) {
		String returnListCode = "";

		for (Map<String, Object> obj : companyList) {
			String value = obj.get("value").toString();
			String label = obj.get("label").toString();
			if (NfwStringUtils.isNotEmpty(companyCd)) {
				// 管理会社が空ではない
				if (companyCd.compareTo(value) == 0) {
					// 管理会社とリスト値が一致する場合、選択中にする
					returnListCode += "<option value='" + value + "' selected>" + label + "</option>";
				} else {
					returnListCode += "<option value='" + value + "'>" + label + "</option>";
				}
			} else {
				returnListCode += "<option value='" + value + "'>" + label + "</option>";
			}
		}
		return returnListCode;
	}

	/**
	 * 事業領域一覧を取得する。<br>
	 * 
	 */
	public int getListTableData(String ｍanageCompanyCd, String businessAreaCd, String businessAreaName, List<Boolean> errorList, 
			List<Map<String, Object>> listTableData) {
		// リストテーブルに格納するデータを取得する
		int resultCount = 0;
		List<Skf3090Sc003GetJigyoryoikiInfoExp> resultListTableData = new ArrayList<Skf3090Sc003GetJigyoryoikiInfoExp>();
		Skf3090Sc003GetJigyoryoikiInfoExpParameter param = new Skf3090Sc003GetJigyoryoikiInfoExpParameter();
		param.setManageCompanyCd(ｍanageCompanyCd);
		param.setBusinessAreaCd(escapeString(businessAreaCd));
		param.setBusinessAreaName(escapeString(businessAreaName));
		resultListTableData = skf3090Sc003GetJigyoryoikiInfoExpRepository.getJigyoryoikiInfo(param);
		
		// 取得レコード数判定
		resultCount = resultListTableData.size();
		if (resultCount == 0) {
			// 取得結果が0件の場合、何も処理せず終了
			return 0;
		}

		// リストテーブルに出力するリストを生成する
		listTableData.clear();
		listTableData.addAll(getListTableDataViewColumn(resultListTableData, errorList));

		return resultCount;
	}

	/**
	 * リストテーブルに出力するリストを取得する。
	 * 
	 * @param originList
	 * @return リストテーブルに出力するリスト
	 */
	public List<Map<String, Object>> getListTableDataViewColumn(List<Skf3090Sc003GetJigyoryoikiInfoExp> originList, List<Boolean> errorList) {
		List<Map<String, Object>> setViewList = new ArrayList<Map<String, Object>>();

		// 機関リスト
		boolean isFirstRowEmpty = true;
		List<Map<String, Object>> manageAgencyList = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < originList.size(); i++) {
			
			Skf3090Sc003GetJigyoryoikiInfoExp tmpData = originList.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();

			// 管理会名
			tmpMap.put("col1", tmpData.getCompanyName());
			// 管理会コード
			tmpMap.put("col2", tmpData.getCompanyCd());
			// 事業領域コード
			tmpMap.put("col3", tmpData.getBusinessAreaCd());
			// 事業領域名
			String bAreaName = tmpData.getBusinessAreaName().replace("'", "&#39;").replace("\"", "&quot;");
			if(CollectionUtils.isNotEmpty(errorList) && (errorList.get(i) == true)){
				tmpMap.put("col4", "<input type='text' id='businessAreaName" + i + "' name='businessAreaName" + i + "' maxlength='255' style='width:380px;' class='nfw-validation-error' value='" + bAreaName + "'>");
			}else{
				tmpMap.put("col4", "<input type='text' id='businessAreaName" + i + "' name='businessAreaName" + i + "' maxlength='255' style='width:380px;' value='" + bAreaName + "'>");
			}
			// 事業領域名(隠し)
			String bHdnAreaName = tmpData.getHidBusinessAreaName().replace("'", "&#39;").replace("\"", "&quot;");
			tmpMap.put("col5", bHdnAreaName);
			//tmpMap.put("col5", tmpData.getHidBusinessAreaName());
			// 管理機関
			manageAgencyList.clear();
			manageAgencyList.addAll(getAgencyCdList(tmpData.getCompanyCd(), isFirstRowEmpty));
			String agencyListCode = createAgencySelect(tmpData.getAgencyCd(), manageAgencyList);
			tmpMap.put("col6", "<select id='agency" + i + "' name='agency" + i + "' style='width:250px;''>" + agencyListCode + "</select>");
			// 管理機関(隠し)
			tmpMap.put("col7", tmpData.getHidAgencyCd());
			// 削除
			tmpMap.put("col8", "");
			// 更新日時
			tmpMap.put("col9", tmpData.getUpdateDateStr());

			setViewList.add(tmpMap);
		}
		return setViewList;
	}

	/**
	 * 管理会社ドロップダウンリストを生成するためにSelectのHTMLコード生成処理
	 * 
	 * @param agencyCd
	 *            管理機関コード
	 * @param manageAgencyList
	 *            管理機関リスト
	 * @return
	 */
	public String createAgencySelect(String agencyCd, List<Map<String, Object>> manageAgencyList) {
		String returnListCode = "";

		for (Map<String, Object> obj : manageAgencyList) {
			String value = obj.get("value").toString();
			String label = obj.get("label").toString();
			if (NfwStringUtils.isNotEmpty(agencyCd)) {
				// 管理機関が空ではない
				if (agencyCd.compareTo(value) == 0) {
					// 管理機関とリスト値が一致する場合、選択中にする
					returnListCode += "<option value='" + value + "' selected>" + label + "</option>";
				} else {
					returnListCode += "<option value='" + value + "'>" + label + "</option>";
				}
			} else {
				returnListCode += "<option value='" + value + "'>" + label + "</option>";
			}
		}
		return returnListCode;
	}

	/**
	 * 機関リストを取得する (skf1010_m_soshiki：社宅組織マスタ)
	 * 
	 * @param companyCd
	 *            会社コード
	 * @param isFirstRowEmpty
	 *            先頭行の空行有無。true:空行あり false：空行なし
	 * @return 機関リスト
	 */
	public List<Map<String, Object>> getAgencyCdList(String companyCd, boolean isFirstRowEmpty) {

		List<Map<String, Object>> agencyList = new ArrayList<Map<String, Object>>();
		List<Skf3090Sc003GetAgencyInfoExp> resultAgencyList = new ArrayList<Skf3090Sc003GetAgencyInfoExp>();

		Skf3090Sc003GetAgencyInfoExpParameter param = new Skf3090Sc003GetAgencyInfoExpParameter();
		param.setManageCompanyCd(companyCd);
		resultAgencyList = skf3090Sc003GetAgencyInfoExpRepository.getAgencyInfo(param);

		Map<String, Object> agencyMap = new HashMap<String, Object>();

		if (isFirstRowEmpty) {
			agencyMap.put("value", CodeConstant.DOUBLE_QUOTATION);
			agencyMap.put("label", CodeConstant.DOUBLE_QUOTATION);
			agencyList.add(agencyMap);
		}

		if (resultAgencyList == null) {
			return agencyList;
		}

		for (Skf3090Sc003GetAgencyInfoExp dt : resultAgencyList) {

			// 表示・値を設定
			agencyMap = new HashMap<String, Object>();
			agencyMap.put("value", dt.getAgencyCd());
			agencyMap.put("label", dt.getAgencyName());

			agencyList.add(agencyMap);

		}

		// 返却するリストをDebugログで出力
		LogUtils.debugByMsg("返却する機関リスト：" + agencyList.toString());

		return agencyList;

	}

	
	/**
	 * 特定の文字をエスケープする（検索用）
	 * @param beforStr 変換前の文字列
	 * @return 変換後の文字列
	 */
	public String escapeString(String beforStr){
		String afterStr = null;
		// 文字エスケープ(%, _, '\)
		if (beforStr != null) {
			// 「\」を「\\」に置換
			afterStr = beforStr.replace("\\", "\\\\");
			// 「%」を「\%」に置換、「_」を「\_」に置換、「'」を「''」に置換
			afterStr = afterStr.replace("%", "\\%").replace("_", "\\_").replace("'", "''");
		}
		
		return afterStr;
	}
	
	/**
	 * 追加領域のColAdd1のタグを生成
	 * @param data 設定するデータ
	 * @param error エラー情報
	 * @return 生成タグ
	 */
	public String createColAdd1Str(String data, Boolean error){
		String tmpStr = "";
		if(error == true){
			tmpStr = "<select id='addManageCompany' name='addManageCompany' style='width:200px;' class='nfw-validation-error' tabindex='6'>" + data + "</select>";
		}else{
			tmpStr = "<select id='addManageCompany' name='addManageCompany' style='width:200px;' tabindex='6'>" + data + "</select>";
		}
		return tmpStr;
	}

	/**
	 * 追加領域のColAdd2のタグを生成
	 * @param data 設定するデータ
	 * @param error エラー情報
	 * @return 生成タグ
	 */
	public String createColAdd2Str(String data, Boolean error){
		String tmpStr = "";
		if(error == true){
			tmpStr = "<input type='text' id='addBusinessAreaCd' name='addBusinessAreaCd' maxlength='4' style='ime-mode: disabled;width:100px;' placeholder='例 A001' class='nfw-validation-error' value='" + data + "' tabindex='7'>";
		}else{
			tmpStr = "<input type='text' id='addBusinessAreaCd' name='addBusinessAreaCd' maxlength='4' style='ime-mode: disabled;width:100px;' placeholder='例 A001' value='" + data + "' tabindex='7'>";
		}
		return tmpStr;
	}

	/**
	 * 追加領域のColAdd3のタグを生成
	 * @param data 設定するデータ
	 * @param error エラー情報
	 * @return 生成タグ
	 */
	public String createColAdd3Str(String data, Boolean error){
		String tmpStr = "";
		if(error == true){
			tmpStr = "<input type='text' id='addBusinessAreaName' name='addBusinessAreaName' maxlength='255' style='width:380px;' placeholder='例 本社 〇〇部' class='nfw-validation-error' value='" + data + "' tabindex='8'>";
		}else{
			tmpStr = "<input type='text' id='addBusinessAreaName' name='addBusinessAreaName' maxlength='255' style='width:380px;' placeholder='例 本社 〇〇部' value='" + data + "' tabindex='8'>";
		}
		return tmpStr;
	}

	/**
	 * 追加領域のColAdd4のタグを生成
	 * @param data 設定するデータ
	 * @param error エラー情報
	 * @return 生成タグ
	 */
	public String createColAdd4Str(String data, Boolean error, Boolean emptyFlg){
		String tmpStr = "";
		if(emptyFlg == true){
			if(error == true){
				tmpStr = "<select id='addaAency' name='addaAency' style='width:250px;' class='nfw-validation-error' tabindex='9'><option value=''></option></select>";
			}else{
				tmpStr = "<select id='addaAency' name='addaAency' style='width:250px;' tabindex='9'><option value=''></option></select>";
			}
		}else{
			if(error == true){
				tmpStr = "<select id='addaAency' name='addaAency' style='width:250px;' class='nfw-validation-error' tabindex='9'>" + data + "</select>";
			}else{
				tmpStr = "<select id='addaAency' name='addaAency' style='width:250px;' tabindex='9'>" + data + "</select>";
			}
		}
		return tmpStr;
	}

	
}
