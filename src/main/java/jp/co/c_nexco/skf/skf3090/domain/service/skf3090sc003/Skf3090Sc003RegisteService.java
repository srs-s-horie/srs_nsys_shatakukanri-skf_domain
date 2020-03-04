/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc003;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MAgency;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MBusinessArea;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MBusinessAreaKey;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MAgencyRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MBusinessAreaRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc003.Skf3090Sc003RegisteDto;
import jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc003.Skf3090Sc003SharedService;
import jp.co.intra_mart.mirage.integration.guice.Transactional;

/**
 * Skf3090Sc003RegistService 事業領域マスタ登録画面のRegistサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3090Sc003RegisteService extends BaseServiceAbstract<Skf3090Sc003RegisteDto>{

	@Autowired
	Skf3090Sc003SharedService skf3090Sc003SharedService;	

	@Autowired
	Skf1010MBusinessAreaRepository skf1010MBusinessAreaRepository;
	
	@Autowired
	Skf1010MAgencyRepository skf1010MAgencyRepository;
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;	

	// リストテーブルの１ページ最大表示行数
	@Value("${skf3090.skf3090_sc003.max_row_count}")
	private String listTableMaxRowCount;	
	
	// 外部機関
	private String GAIBU_COMPANY_CD =  "ZZZZ";	
	
	/**
	 * サービス処理を行う。
	 * 
	 * @param registDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	@Transactional
	public Skf3090Sc003RegisteDto index(Skf3090Sc003RegisteDto registDto) throws Exception {
	
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("登録", CodeConstant.C001, registDto.getPageId());		
		
		// 入力チェック
		List <Boolean> errorList = new ArrayList <Boolean>();
		List<Map<String, Object>> manageCompanyKubunList = new ArrayList<Map<String, Object>>();
		skf3090Sc003SharedService.getDoropDownManageCompanyList(registDto.getHdnAddCompanyCd(), manageCompanyKubunList);	
		if(isValidateInput(registDto, errorList) == false){
			// 入力チェックエラー
			// エラーメッセージを設定する
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1049, "事業領域名", "255");
			
			// 検索結果一覧の状態をエラー（赤色）にする
			List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
			skf3090Sc003SharedService.getListTableData(registDto.getSelectedManageCompanyCd(), registDto.getBusinessAreaCd(), registDto.getBusinessAreaName(), errorList, listTableData);			
			registDto.setListTableData(listTableData);
			
			// 追加領域の情報ももとに戻す
			List<Map<String, Object>> listAddTableData = new ArrayList<Map<String, Object>>();
			listAddTableData = skf3090Sc003SharedService.getListAddTableDataViewColumn(registDto.getHdnAddCompanyCd(), manageCompanyKubunList, registDto.getHdnAddBusinessAreaCd(), registDto.getHdnAddBusinessAreaName(), registDto.getHdnAddAgencyCd(), registDto.getErrorListAddTable());
			registDto.setListAddTableData(listAddTableData);
			
		}else{
			List<String> errList = new ArrayList<String>();
			int resultCount = registJigyoryoikiInfo(registDto, errList);
			if(resultCount > 0){
				// 成功
				// 完了メッセージを表示する。
				ServiceHelper.addResultMessage(registDto, MessageIdConstant.I_SKF_1011);

				// 追加領域の情報ももとに戻す
				List<Map<String, Object>> listAddTableData = new ArrayList<Map<String, Object>>();
				listAddTableData = skf3090Sc003SharedService.getListAddTableDataViewColumn(registDto.getHdnAddCompanyCd(), manageCompanyKubunList, registDto.getHdnAddBusinessAreaCd(), registDto.getHdnAddBusinessAreaName(), registDto.getHdnAddAgencyCd(), registDto.getErrorListAddTable());
				registDto.setListAddTableData(listAddTableData);

		        // 再検索（事業領域情報取得）
				List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
				int listCount = skf3090Sc003SharedService.getListTableData(registDto.getSelectedManageCompanyCd(), registDto.getBusinessAreaCd(), registDto.getBusinessAreaName(), null, listTableData);
				if(listCount == 0){
					// 取得レコード0件のワーニング
					ServiceHelper.addWarnResultMessage(registDto, MessageIdConstant.W_SKF_1007, String.valueOf(listCount));
					// 登録ボタン非活性化
					registDto.setRegistButtonDisabled(true);
				}else{
					// 最大行数を設定
					registDto.setListTableMaxRowCount(listTableMaxRowCount);
					// 登録ボタン活性化
					registDto.setRegistButtonDisabled(false);
				}
				registDto.setListTableData(listTableData);
				
			}else{
				// 異常
				if(resultCount == 0){
					// 事業領域の存在チェックエラーの場合
					ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_3022, errList.get(0));
				}else if(resultCount == -1){
					// 更新できなかった場合
					ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.W_SKF_1009);
				}else{
					// システムエラーの場合
					ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1075);
				}
				// 例外時はthrowしてRollBack
				throwBusinessExceptionIfErrors(registDto.getResultMessages());
			}
		}
		
		return registDto;
	}
	
	/**
	 * 入力チェックを行う.
	 * 
	 * @param registDto *indexメソッドの引数のDTO。入力チェックエラーの場合、エラーメッセージを詰める。
	 * @return 入力チェック結果。trueの場合チェックOK。
	 * @throws UnsupportedEncodingException 桁数チェック時の例外
	 */	
	private boolean isValidateInput(Skf3090Sc003RegisteDto registDto, List <Boolean> errorList) throws UnsupportedEncodingException {
		boolean isCheckOk = true;
		String debugMessage = "";
		
		//String[] editInfoList = registDto.getRegistEditData().split(";");
		String[] editInfoList = registDto.getRegistEditData().split("EndOfEditData");
		
		for (int rowIndex = 0; rowIndex < registDto.getListTableData().size(); rowIndex++){	
			if(isCheckOk == true){
				// 事業領域名
				String rowData = editInfoList[rowIndex];

				// セパレータしかない場合はNULLを入れる
				String bAreaNameTmp = null;
				if(rowData.length() > 15){
					String[] inputData = rowData.split("/--separater--/", -1);
					if(inputData[0].length() != 0){
						bAreaNameTmp = inputData[0]; 
					}
				}				
				if(NfwStringUtils.isNotEmpty(bAreaNameTmp) && CheckUtils.isMoreThanSize(bAreaNameTmp.trim(), 255)){
					// 入力有　かつ 桁数問題あり
					debugMessage += rowIndex + "行目：　桁数チェック - " + "事業領域名 - " + bAreaNameTmp;
					errorList.add(rowIndex, true);
					isCheckOk = false;
					continue;
				}
			}
			errorList.add(rowIndex, false);
		}
		// デバッグメッセージ出力
		if (isCheckOk) {
			LogUtils.debugByMsg("入力チェックOK：" + debugMessage);
		} else {
			LogUtils.debugByMsg("入力チェックエラー：" + debugMessage);
		}
		
		return isCheckOk;
	}

	/**
	 * 登録処理
	 * 
	 * @param registDto *indexメソッドの引数のDTO。入力チェックエラーの場合、エラーメッセージを詰める。
	 * @param errList 
	 * @return 処理件数
	 */	
	private int registJigyoryoikiInfo(Skf3090Sc003RegisteDto registDto, List<String> errList){
		int registCount = 0;
		//String[] editInfoList = registDto.getRegistEditData().split(";");
		String[] editInfoList = registDto.getRegistEditData().split("EndOfEditData");

		// 機関リスト
		boolean isFirstRowEmpty = true;
		List<Map<String, Object>> manageAgencyList = new ArrayList<Map<String, Object>>();
		
		// 追加領域
		// ---- エラー時用に、画面入力情報で置き換える（置き換えないと、検索した時の情報に戻ってしまう）
		// エラー状態チェック
		Boolean companyCdError = false;
		Boolean bAreaCdError = false;
		Boolean bAreaNameError = false;
		Boolean agencyCdError = false;
		if(CollectionUtils.isNotEmpty(registDto.getErrorListAddTable())){
			if(true == registDto.getErrorListAddTable().get(0)){
				companyCdError = true;
			}
			if(true == registDto.getErrorListAddTable().get(1)){
				bAreaCdError = true;
			}
			if(true == registDto.getErrorListAddTable().get(2)){
				bAreaNameError = true;
			}
			if(true == registDto.getErrorListAddTable().get(3)){
				agencyCdError = true;
			}
		}		
		Map<String, Object> listAddTableData = registDto.getListAddTableData().get(0);				
		// 管理会社コード
		String companyListCode = "";
		List<Map<String, Object>> manageCompanyKubunList = new ArrayList<Map<String, Object>>();
		skf3090Sc003SharedService.getDoropDownManageCompanyList(registDto.getHdnAddCompanyCd(), manageCompanyKubunList);
		if (NfwStringUtils.isNotEmpty(registDto.getHdnAddCompanyCd())) {
			companyListCode = skf3090Sc003SharedService.createManageCompanySelect(registDto.getHdnAddCompanyCd(), manageCompanyKubunList);
		} else {
			companyListCode = skf3090Sc003SharedService.createManageCompanySelect(null, manageCompanyKubunList);
		}
		listAddTableData.put("colAdd1", skf3090Sc003SharedService.createColAdd1Str(companyListCode, companyCdError));
		// 事業領域コード
		String bAreaCd = "";
		if (NfwStringUtils.isNotEmpty(registDto.getHdnAddBusinessAreaCd())) {
			bAreaCd = registDto.getHdnAddBusinessAreaCd();
		}
		listAddTableData.put("colAdd2", skf3090Sc003SharedService.createColAdd2Str(bAreaCd, bAreaCdError));

		// 管理機関
		// ----- エラー時用に、画面入力情報で管理機関を置き換える（置き換えないと、検索した時の情報に戻ってしまう）
		String bAreaName = "";
		if (NfwStringUtils.isNotEmpty(registDto.getHdnAddBusinessAreaName())) {
			bAreaName = registDto.getHdnAddBusinessAreaName();
		}
		listAddTableData.put("colAdd3", skf3090Sc003SharedService.createColAdd3Str(bAreaName, bAreaNameError));

		// 事業領域名
		// ----- エラー時用に、画面入力情報で事業領域名を置き換える（置き換えないと、検索した時の情報に戻ってしまう）
		if (NfwStringUtils.isNotEmpty(registDto.getHdnAddCompanyCd())) {
			String agencyListCode = "";
			if (NfwStringUtils.isNotEmpty(registDto.getHdnAddAgencyCd())) {
				agencyListCode = skf3090Sc003SharedService.createAgencySelect(registDto.getHdnAddAgencyCd(), skf3090Sc003SharedService.getAgencyCdList(registDto.getHdnAddCompanyCd(), isFirstRowEmpty));
			} else {
				agencyListCode = skf3090Sc003SharedService.createAgencySelect(null, skf3090Sc003SharedService.getAgencyCdList(registDto.getHdnAddCompanyCd(), isFirstRowEmpty));
			}
			listAddTableData.put("colAdd4",skf3090Sc003SharedService.createColAdd4Str(agencyListCode, agencyCdError, false));
		} else {
			listAddTableData.put("colAdd4",skf3090Sc003SharedService.createColAdd4Str(null, agencyCdError, true));
		}
		
		// 検索結果一覧のデータ処理
		for (int rowIndex = 0; rowIndex < registDto.getListTableData().size(); rowIndex++){
			// 行データの取得
			Map<String, Object> listTableData = registDto.getListTableData().get(rowIndex);	
			String rowData = editInfoList[rowIndex];

			// セパレータしかない場合はNULLを入れる
			String bAreaNameTmp = null;
			String agencyCdTmp = null;
			if(rowData.length() > 15){
				String[] inputData = rowData.split("/--separater--/", -1);
				if(inputData[0].length() != 0){
					bAreaNameTmp = inputData[0]; 
				}
				if(inputData[1].length() != 0){
					agencyCdTmp = inputData[1];
				}
			}

			// - 管理会社名
			// - 管理会社コード
			String companyCd = listTableData.get("col2").toString();
			
			// - 事業領域コード
			String businessAreaCd = listTableData.get("col3").toString();
			
			// - 事業領域名
			String businessAreaName = bAreaNameTmp;
			// ---- エラー時用に、画面入力情報で事業領域名を置き換える（置き換えないと、検索した時の情報に戻ってしまう）
			String businessAreaNameTmp = businessAreaName;
			if(NfwStringUtils.isNotEmpty(businessAreaName)){
				businessAreaNameTmp = businessAreaName.replace("'", "&#39;").replace("\"", "&quot;");
			}
			listTableData.put("col4", "<input type='text' id='businessAreaName" + rowIndex + "' name='businessAreaName" + rowIndex + "' maxlength='255' style='width:380px;' value='" + businessAreaNameTmp + "'>");
			
			// - 事業領域名（隠し）
			String hdnBusinessAreaName = listTableData.get("col5").toString();
			listTableData.put("col5", listTableData.get("col5").toString().replace("'", "&#39;").replace("\"", "&quot;"));
			
			// - 管理機関
			String agencyCd = agencyCdTmp;
			// ---- エラー時用に、画面入力情報で管理機関を置き換える（置き換えないと、検索した時の情報に戻ってしまう）
			manageAgencyList.clear();
			manageAgencyList.addAll(skf3090Sc003SharedService.getAgencyCdList(companyCd, isFirstRowEmpty));
			String agencyListCode = skf3090Sc003SharedService.createAgencySelect(agencyCd, manageAgencyList);
			listTableData.put("col6", "<select id='agency" + rowIndex + "' name='agency" + rowIndex + "' style='width:250px;''>" + agencyListCode + "</select>");
			
			// - 管理機関（隠し）
			String hdnAgencyCd = null;
			if(listTableData.get("col7") != null){
				hdnAgencyCd = listTableData.get("col7").toString();
			}
			
			// - 更新日時
			String updateDateStr = listTableData.get("col9").toString();
			
			// 該当行の事業領域名が未入力の場合、事業領域名を画面初期表示の事業領域名を設定する
			if(NfwStringUtils.isEmpty(businessAreaName)){
				// 現行は空文字許可している
				businessAreaName = hdnBusinessAreaName;
			}else{
				if(Objects.equals(businessAreaName, hdnBusinessAreaName) && Objects.equals(agencyCd, hdnAgencyCd)){
					// 変更がないレコードがある場合、登録対象外です。
					continue;
				}
			}
			
			/** 登録処理を行う **/
			try{
				// 事業領域の存在チェック
				Skf1010MBusinessAreaKey key = new Skf1010MBusinessAreaKey();
				key.setCompanyCd(companyCd);
				key.setBusinessAreaCd(skf3090Sc003SharedService.escapeString(businessAreaCd));
				Skf1010MBusinessArea jigyoryoikiInfo = skf1010MBusinessAreaRepository.selectByPrimaryKey(key);			
				if(jigyoryoikiInfo == null){
					// 取得件数が0件（異常終了）
					errList.add(0, businessAreaCd);
					return 0;
				}
				
				// 事業領域マスタの排他
				// 日付変換
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
				Date mapDate = null;
				try{
					mapDate = dateFormat.parse(updateDateStr);
				}catch(ParseException ex){
					// 誰かに先に更新されちゃった
					LogUtils.debugByMsg("事業領域情報-更新日時変換エラー :" + updateDateStr);
					return -1;
				}			
				super.checkLockException(mapDate, jigyoryoikiInfo.getUpdateDate());			
				
				// 更新を行う
				String registAgencyCd = "";
				if(GAIBU_COMPANY_CD.equals(companyCd)){
					// 管理会社が外部機関の場合、機関コードは事業領域コードで更新する
					registAgencyCd = businessAreaCd;
				}else{
					registAgencyCd = agencyCd;
				}
				Skf1010MBusinessArea record = new Skf1010MBusinessArea();
				// 更新項目
				record.setBusinessAreaName(businessAreaName);
				record.setAgencyCd(registAgencyCd);
				record.setCompanyCd(companyCd);
				record.setBusinessAreaCd(businessAreaCd);
				// SELECTした結果をそのまま利用
				record.setDeleteFlag("0");
				record.setInsertDate(jigyoryoikiInfo.getInsertDate());
				record.setInsertUserId(jigyoryoikiInfo.getInsertUserId());
				record.setInsertProgramId(jigyoryoikiInfo.getInsertProgramId());
				// NULL更新したいカラムがあるので「updateByPrimaryKey」を使う
				int resultCount = skf1010MBusinessAreaRepository.updateByPrimaryKey(record);
				if(resultCount > 0){
					registCount += resultCount;
				}else{
					return -1;
				}
				
				if(GAIBU_COMPANY_CD.equals(companyCd)){
					// 管理会社が外部機関の場合のみ、機関マスタの更新を行う
					// - 更新は「事業領域コード」「事業領域名」で行う
					Skf1010MAgency recordAgency = new Skf1010MAgency();
					recordAgency.setAgencyName(businessAreaName);
					recordAgency.setCompanyCd(companyCd);
					recordAgency.setAgencyCd(businessAreaCd);
					recordAgency.setDeleteFlag("0");
					int resultAgencyCount = skf1010MAgencyRepository.updateByPrimaryKeySelective(recordAgency);
					if(resultAgencyCount > 0){
						registCount += resultAgencyCount;
					}else{
						return -1;
					}
				}
			}catch(Exception ex){
				// 何かしらのエラーが発生
				LogUtils.debugByMsg("事業領域の更新エラー" + ex.toString());
				return -2;
			}
		}
		
		if(registCount == 0){
			// ここで0件の場合は更新対象のデータが1件もなかった（すべて同じだった）
			registCount = 1;
		}
		
		return registCount;
	}
}
