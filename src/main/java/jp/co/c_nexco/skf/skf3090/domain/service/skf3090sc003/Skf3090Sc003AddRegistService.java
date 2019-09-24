/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc003;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc003.Skf3090Sc003AddRegistDto;
import jp.co.intra_mart.mirage.integration.guice.Transactional;

/**
 * Skf3090Sc003AddRegistService 事業領域マスタ登録画面のAddRegist（追加）サービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3090Sc003AddRegistService extends BaseServiceAbstract<Skf3090Sc003AddRegistDto> {

	@Autowired
	Skf3090Sc003SharedService skf3090Sc003SharedService;	

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;	

	@Autowired
	private Skf1010MBusinessAreaRepository skf1010MBusinessAreaRepository;

	@Autowired
	private Skf1010MAgencyRepository skf1010MAgencyRepository;
	
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
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Skf3090Sc003AddRegistDto index(Skf3090Sc003AddRegistDto addRegistDto) throws Exception {
	
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("追加", CodeConstant.C001, addRegistDto.getPageId());		
		
		// 検索結果一覧
		// ----- エラー時用に、画面入力情報で置き換える（置き換えないと、検索した時の情報に戻ってしまう）
		String[] editInfoList = addRegistDto.getRegistEditData().split("EndOfEditData");
		// 機関リスト
		List<Map<String, Object>> manageAgencyList = new ArrayList<Map<String, Object>>();
		for (int rowIndex = 0; rowIndex < addRegistDto.getListTableData().size(); rowIndex++){
			// 行データの取得
			Map<String, Object> listTableData = addRegistDto.getListTableData().get(rowIndex);	
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

			// - 事業領域名
			// ---- エラー時用に、画面入力情報で事業領域名を置き換える（置き換えないと、検索した時の情報に戻ってしまう）
			String businessAreaNameTmp = bAreaNameTmp;
			if(NfwStringUtils.isNotEmpty(bAreaNameTmp)){
				businessAreaNameTmp = bAreaNameTmp.replace("'", "&#39;").replace("\"", "&quot;");
			}
			listTableData.put("col4", "<input type='text' id='businessAreaName" + rowIndex + "' name='businessAreaName" + rowIndex + "' maxlength='255' style='width:380px;' value='" + businessAreaNameTmp + "'>");
			
			// - 事業領域名（隠し）
			listTableData.put("col5", listTableData.get("col5").toString().replace("'", "&#39;").replace("\"", "&quot;"));

			// - 管理機関
			// ---- エラー時用に、画面入力情報で管理機関を置き換える（置き換えないと、検索した時の情報に戻ってしまう）
			manageAgencyList.clear();
			manageAgencyList.addAll(skf3090Sc003SharedService.getAgencyCdList(listTableData.get("col2").toString(), true));
			String agencyListCode2 = skf3090Sc003SharedService.createAgencySelect(agencyCdTmp, manageAgencyList);
			listTableData.put("col6", "<select id='agency" + rowIndex + "' name='agency" + rowIndex + "' style='width:250px;''>" + agencyListCode2 + "</select>");
		}
		
		// 入力チェック
		List <Boolean> errorListAddTable = new ArrayList <Boolean>();
		List<Map<String, Object>> listAddTableData = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> manageCompanyKubunList = new ArrayList<Map<String, Object>>();
		skf3090Sc003SharedService.getDoropDownManageCompanyList(addRegistDto.getHdnAddCompanyCd(), manageCompanyKubunList);		
		if(isValidateInput(addRegistDto, errorListAddTable) == false){
			// 入力チェックエラーの場合、追加領域の項目を再設定して処理を終了する
			listAddTableData = skf3090Sc003SharedService.getListAddTableDataViewColumn(addRegistDto.getHdnAddCompanyCd(), manageCompanyKubunList, addRegistDto.getHdnAddBusinessAreaCd(), addRegistDto.getHdnAddBusinessAreaName(), addRegistDto.getHdnAddAgencyCd(), errorListAddTable);
			addRegistDto.setListAddTableData(listAddTableData);

			// セッション情報
			addRegistDto.setErrorListAddTable(errorListAddTable);
			addRegistDto.setHdnAddCompanyCd(addRegistDto.getHdnAddCompanyCd());
			addRegistDto.setHdnAddBusinessAreaCd(addRegistDto.getHdnAddBusinessAreaCd());
			addRegistDto.setHdnAddBusinessAreaName(addRegistDto.getHdnAddBusinessAreaName());
			addRegistDto.setHdnAddAgencyCd(addRegistDto.getHdnAddAgencyCd());
			
			if(addRegistDto.getListTableData().size() > 0){
				// 登録ボタン活性化
				addRegistDto.setRegistButtonDisabled(false);			
			}else{
				// 登録ボタン非活性化
				addRegistDto.setRegistButtonDisabled(true);			
			}					
			return addRegistDto;
		}
		
		errorListAddTable.set(0, false);
		errorListAddTable.set(1, false);
		errorListAddTable.set(2, false);
		errorListAddTable.set(3, false);

		// 事業領域の存在チェック
		int exitCount = checkJigyoryoikiInfo(addRegistDto);
		if(exitCount > 0){
			// 事業領域が存在している場合、追加領域の項目を再設定して処理を終了する
			addRegistDto.setErrorListAddTable(errorListAddTable);
			listAddTableData = skf3090Sc003SharedService.getListAddTableDataViewColumn(addRegistDto.getHdnAddCompanyCd(), manageCompanyKubunList, addRegistDto.getHdnAddBusinessAreaCd(), addRegistDto.getHdnAddBusinessAreaName(), addRegistDto.getHdnAddAgencyCd(), errorListAddTable);
			addRegistDto.setListAddTableData(listAddTableData);
			
			ServiceHelper.addErrorResultMessage(addRegistDto, null, MessageIdConstant.E_SKF_3021);
			if(addRegistDto.getListTableData().size() > 0){
				// 登録ボタン活性化
				addRegistDto.setRegistButtonDisabled(false);			
			}else{
				// 登録ボタン非活性化
				addRegistDto.setRegistButtonDisabled(true);			
			}		
		}else{
			// 登録処理
			int resultCount = registJigyoryoikiInfo(addRegistDto);
				
			if(resultCount == 1){
				// 正常終了
				// 追加領域の項目を初期状態にする
				addRegistDto.setErrorListAddTable(errorListAddTable);
				listAddTableData = skf3090Sc003SharedService.getListAddTableDataViewColumn(null, manageCompanyKubunList, null, null, null, null);
				addRegistDto.setListAddTableData(listAddTableData);
				// 完了メッセージを表示する。
				ServiceHelper.addResultMessage(addRegistDto, MessageIdConstant.I_SKF_1012);
				
		        // 検索条件をもとに検索結果カウントの取得
				List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
				
				// 事業領域情報取得
				int listCount = skf3090Sc003SharedService.getListTableData(addRegistDto.getSelectedManageCompanyCd(), addRegistDto.getBusinessAreaCd(), addRegistDto.getBusinessAreaName(), null, listTableData);
				if(listCount == 0){
					// 取得レコード0件のワーニング
					ServiceHelper.addWarnResultMessage(addRegistDto, MessageIdConstant.W_SKF_1007, String.valueOf(listCount));
					// 登録ボタン非活性化
					addRegistDto.setRegistButtonDisabled(true);
				}else{
					// 最大行数を設定
					addRegistDto.setListTableMaxRowCount(listTableMaxRowCount);
					// 登録ボタン活性化
					addRegistDto.setRegistButtonDisabled(false);
				}
				addRegistDto.setListTableData(listTableData);
				
			}else{
				if(resultCount == 0){
					// 登録できなかった場合、エラーメッセージを設定し、処理を終了する。
					ServiceHelper.addErrorResultMessage(addRegistDto, null, MessageIdConstant.W_SKF_1010);
				}else{
					ServiceHelper.addErrorResultMessage(addRegistDto, null, MessageIdConstant.E_SKF_1073);
				}
				if(addRegistDto.getListTableData().size() > 0){
					// 登録ボタン活性化
					addRegistDto.setRegistButtonDisabled(false);			
				}else{
					// 登録ボタン非活性化
					addRegistDto.setRegistButtonDisabled(true);			
				}		
				// 例外時はthrowしてRollBack
				throwBusinessExceptionIfErrors(addRegistDto.getResultMessages());
			}
		}
		
		return addRegistDto;
	}
	
	/**
	 * 入力チェックを行う.
	 * 
	 * @param registDto *indexメソッドの引数のDTO。入力チェックエラーの場合、エラーメッセージを詰める。
	 * @return 入力チェック結果。trueの場合チェックOK。
	 * @throws UnsupportedEncodingException 桁数チェック時の例外
	 */
	private boolean isValidateInput(Skf3090Sc003AddRegistDto addRegistDto, List <Boolean> errorListAddTable) throws UnsupportedEncodingException {
		boolean isCheckOk = true;
		String debugMessage = "";

		// エラー状態初期化
		errorListAddTable.add(0, false);
		errorListAddTable.add(1, false);
		errorListAddTable.add(2, false);
		errorListAddTable.add(3, false);
		
		/** 管理会社 **/
		// 必須入力チェック
		if(NfwStringUtils.isEmpty(addRegistDto.getHdnAddCompanyCd())){
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(addRegistDto, null, MessageIdConstant.E_SKF_1054, "管理会社");
			debugMessage += "　必須入力チェック - " + "管理会社";
			errorListAddTable.set(0, true);
		}

		/** 事業領域コード  **/
		// 必須入力チェック
		if(NfwStringUtils.isEmpty(addRegistDto.getHdnAddBusinessAreaCd()) || NfwStringUtils.isEmpty(addRegistDto.getHdnAddBusinessAreaCd().trim())){
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(addRegistDto, null, MessageIdConstant.E_SKF_1048, "事業領域コード");
			debugMessage += "　必須入力チェック - " + "事業領域コード";
			errorListAddTable.set(1, true);
		}else{
			// 文字（旧バイト）数チェック
			if(CheckUtils.isMoreThanByteSize(addRegistDto.getHdnAddBusinessAreaCd().trim(), 4)){
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(addRegistDto, null, MessageIdConstant.E_SKF_1049, "事業領域コード", "4");
				debugMessage += "　桁数チェック - " + "事業領域コード - " + addRegistDto.getHdnAddBusinessAreaCd();
				errorListAddTable.set(1, true);
			}	
			// 文字種チェック
			String matchPattern = "^[a-zA-Z0-9]*$";
			if(!addRegistDto.getHdnAddBusinessAreaCd().trim().matches(matchPattern)){
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(addRegistDto, null, MessageIdConstant.E_SKF_1052, "事業領域コード");
				debugMessage += "　文字種チェック - " + "事業領域コード - " + addRegistDto.getHdnAddBusinessAreaCd();
				errorListAddTable.set(1, true);
			}
		}
		
		/** 事業領域名 **/
		// 必須入力チェック
		if(NfwStringUtils.isEmpty(addRegistDto.getHdnAddBusinessAreaName())){
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(addRegistDto, null, MessageIdConstant.E_SKF_1048, "事業領域名");
			debugMessage += "　必須入力チェック - " + "事業領域名";
			errorListAddTable.set(2, true);
		}else{
			String bAreaName = addRegistDto.getHdnAddBusinessAreaName();
			// 半角スペース削除
			bAreaName = bAreaName.trim();
			// 全角スペース削除
			bAreaName = bAreaName.replace("　","");
			
			if(NfwStringUtils.isEmpty(bAreaName)){
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(addRegistDto, null, MessageIdConstant.E_SKF_1048, "事業領域名");
				debugMessage += "　必須入力チェック - " + "事業領域名";
				errorListAddTable.set(2, true);
			}else{
				// 文字数チェック
				if(CheckUtils.isMoreThanSize(addRegistDto.getHdnAddBusinessAreaName().trim(), 255)){
					isCheckOk = false;
					ServiceHelper.addErrorResultMessage(addRegistDto, null, MessageIdConstant.E_SKF_1049, "事業領域名", "255");
					debugMessage += "　桁数チェック - " + "事業領域コード - " + addRegistDto.getHdnAddBusinessAreaCd();
					errorListAddTable.set(2, true);
				}	
			}
		}
		
		/** 管理機関 **/
		// 必須入力チェック（管理会社≠外部機関のみ）
		if(NfwStringUtils.isEmpty(addRegistDto.getHdnAddCompanyCd()) && NfwStringUtils.isEmpty(addRegistDto.getHdnAddAgencyCd())){
			// 管理会社が指定されておらず、かつ外部機関も指定されていない
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(addRegistDto, null, MessageIdConstant.E_SKF_1054, "管理機関");
			debugMessage += "　必須入力チェック - " + "管理機関";
			errorListAddTable.set(3, true);
		}else{
			if(NfwStringUtils.isEmpty(addRegistDto.getHdnAddAgencyCd())
					&& !addRegistDto.getHdnAddCompanyCd().equals(GAIBU_COMPANY_CD)){
				
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(addRegistDto, null, MessageIdConstant.E_SKF_1054, "管理機関");
				debugMessage += "　必須入力チェック - " + "管理機関";
				errorListAddTable.set(3, true);
			}
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
	 * 事業領域の存在チェック
	 * 
	 * @param registDto 
	 * @return 確認結果
	 */
	private int checkJigyoryoikiInfo(Skf3090Sc003AddRegistDto addRegistDto){
		int exitCount = 0;
		Skf1010MBusinessAreaKey key = new Skf1010MBusinessAreaKey();
		key.setCompanyCd(addRegistDto.getHdnAddCompanyCd());
		key.setBusinessAreaCd(skf3090Sc003SharedService.escapeString(addRegistDto.getHdnAddBusinessAreaCd()));
		Skf1010MBusinessArea jigyoryoikiInfo = skf1010MBusinessAreaRepository.selectByPrimaryKey(key);
		if(jigyoryoikiInfo != null){
			// null以外（存在する）の場合は1で返却
			exitCount = 1;
		}
		return exitCount;
		
	}
	
	/**
	 * 事業領域の登録を行う.
	 * 
	 * @param registDto 
	 * @return 登録結果
	 */
	private int registJigyoryoikiInfo(Skf3090Sc003AddRegistDto addRegistDto){
		int resultCount = 0;
		try{
			// 管理会社が外部機関の場合、機関コードには事業領域コードと同じものを設定する
			String agencyCd = addRegistDto.getHdnAddAgencyCd();
			if(GAIBU_COMPANY_CD.equals(addRegistDto.getHdnAddCompanyCd())){
				agencyCd = addRegistDto.getHdnAddBusinessAreaCd();
			}
			Map<String, Object> listAddTableData = addRegistDto.getListAddTableData().get(0);				
			
			Skf1010MBusinessArea record = new Skf1010MBusinessArea();
			// 管理会社コード
			record.setCompanyCd(addRegistDto.getHdnAddCompanyCd());
			// ----- エラー時用に、画面入力情報で管理会社コードを置き換える（置き換えないと、検索した時の情報に戻ってしまう）
			String companyListCode = "";
			List<Map<String, Object>> manageCompanyKubunList = new ArrayList<Map<String, Object>>();
			skf3090Sc003SharedService.getDoropDownManageCompanyList(addRegistDto.getHdnAddCompanyCd(), manageCompanyKubunList);
			companyListCode = skf3090Sc003SharedService.createManageCompanySelect(addRegistDto.getHdnAddCompanyCd(), manageCompanyKubunList);
			listAddTableData.put("colAdd1", skf3090Sc003SharedService.createColAdd1Str(companyListCode, false));
			//listAddTableData.put("colAdd1", "<select id='addManageCompany' name='addManageCompany' style='width:200px;' tabindex='6'>" + companyListCode + "</select>");
			
			// 事業領域コード
			record.setBusinessAreaCd(addRegistDto.getHdnAddBusinessAreaCd());
			// ----- エラー時用に、画面入力情報で事業領域コードを置き換える（置き換えないと、検索した時の情報に戻ってしまう）
			listAddTableData.put("colAdd2", skf3090Sc003SharedService.createColAdd2Str(addRegistDto.getHdnAddBusinessAreaCd(), false));
			//listAddTableData.put("colAdd2","<input type='text' id='addBusinessAreaCd' name='addBusinessAreaCd' maxlength='4' style='ime-mode: disabled;width:100px;' placeholder='例 A001' value='" + addRegistDto.getHdnAddBusinessAreaCd() + "' tabindex='7'>");

			// 管理機関
			record.setBusinessAreaName(addRegistDto.getHdnAddBusinessAreaName());
			// ----- エラー時用に、画面入力情報で管理機関を置き換える（置き換えないと、検索した時の情報に戻ってしまう）
			listAddTableData.put("colAdd3", skf3090Sc003SharedService.createColAdd3Str(addRegistDto.getHdnAddBusinessAreaName(), false));
			//listAddTableData.put("colAdd3","<input type='text' id='addBusinessAreaName' name='addBusinessAreaName' maxlength='255' style='width:380px;' placeholder='例 本社 〇〇部' value='" + addRegistDto.getHdnAddBusinessAreaName() + "' tabindex='8'>");

			// 事業領域名
			record.setAgencyCd(agencyCd);
			// ----- エラー時用に、画面入力情報で事業領域名を置き換える（置き換えないと、検索した時の情報に戻ってしまう）
			boolean isFirstRowEmpty = true;
			String agencyListCode = skf3090Sc003SharedService.createAgencySelect(agencyCd, skf3090Sc003SharedService.getAgencyCdList(addRegistDto.getHdnAddCompanyCd(), isFirstRowEmpty));
			listAddTableData.put("colAdd4",skf3090Sc003SharedService.createColAdd4Str(agencyListCode, false, false));
			//listAddTableData.put("colAdd4","<select id='addaAency' name='addaAency' style='width:250px;' tabindex='9'>" + agencyListCode + "</select>");
			
			// エラー情報（ここまでくると、エラーではないので、リセットする）
			if(CollectionUtils.isNotEmpty(addRegistDto.getErrorListAddTable())){
				addRegistDto.getErrorListAddTable().set(0, false);
				addRegistDto.getErrorListAddTable().set(1, false);
				addRegistDto.getErrorListAddTable().set(2, false);
				addRegistDto.getErrorListAddTable().set(3, false);
			}
			
			// 登録処理
			resultCount = skf1010MBusinessAreaRepository.insertSelective(record);
			
			if(resultCount <= 0){
				return 0;
			}
			
			// 管理会社が外部機関の場合のみ、機関マスタも登録する
			if(GAIBU_COMPANY_CD.equals(addRegistDto.getHdnAddCompanyCd())){
			
				Skf1010MAgency recordAgency = new Skf1010MAgency();
				// 会社コード
				recordAgency.setCompanyCd(addRegistDto.getHdnAddCompanyCd());
				// 機関コード
				recordAgency.setAgencyCd(addRegistDto.getHdnAddBusinessAreaCd());
				// 機関名
				recordAgency.setAgencyName(addRegistDto.getHdnAddBusinessAreaName());
				resultCount = skf1010MAgencyRepository.insertSelective(recordAgency);
			}
		}catch(Exception ex){
			LogUtils.debugByMsg("事業領域の登録エラー" + ex.toString());
			return -1;
		}
		
		return resultCount;
	}
	
}
