/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc003;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc003.Skf3090Sc003GetShatakuInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc003.Skf3090Sc003GetShatakuInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MAgencyKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MBusinessArea;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MBusinessAreaKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3090Sc003.Skf3090Sc003GetShatakuInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MAgencyRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MBusinessAreaRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc003.Skf3090Sc003DeleteDto;
import jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc003.Skf3090Sc003SharedService;
import jp.co.intra_mart.mirage.integration.guice.Transactional;

/**
 * Skf3090Sc003DeleteService 事業領域マスタ登録画面のDeleteサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3090Sc003DeleteService extends SkfServiceAbstract<Skf3090Sc003DeleteDto> {
	
	@Autowired
	Skf3090Sc003SharedService skf3090Sc003SharedService;
	
	@Autowired
	private Skf3090Sc003GetShatakuInfoExpRepository skf3090Sc003GetShatakuInfoExpRepository;
	
	@Autowired
	private Skf1010MBusinessAreaRepository skf1010MBusinessAreaRepository;
	
	@Autowired
	private Skf1010MAgencyRepository skf1010MAgencyRepository;
	
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
	 * @param deleteDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	@Transactional
	public Skf3090Sc003DeleteDto index(Skf3090Sc003DeleteDto deleteDto) throws Exception {

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("削除", CodeConstant.C001, deleteDto.getPageId());		

		// 追加領域
		// ---- エラー時用に、画面入力情報で置き換える（置き換えないと、検索した時の情報に戻ってしまう）
		// エラー状態チェック
		Boolean companyCdError = false;
		Boolean bAreaCdError = false;
		Boolean bAreaNameError = false;
		Boolean agencyCdError = false;
		if(CollectionUtils.isNotEmpty(deleteDto.getErrorListAddTable())){
			if(true == deleteDto.getErrorListAddTable().get(0)){
				companyCdError = true;
			}
			if(true == deleteDto.getErrorListAddTable().get(1)){
				bAreaCdError = true;
			}
			if(true == deleteDto.getErrorListAddTable().get(2)){
				bAreaNameError = true;
			}
			if(true == deleteDto.getErrorListAddTable().get(3)){
				agencyCdError = true;
			}
		}		
		Map<String, Object> listAddTableData = deleteDto.getListAddTableData().get(0);				
		// 管理会社コード
		String companyListCode = "";
		List<Map<String, Object>> manageCompanyKubunList = new ArrayList<Map<String, Object>>();
		skf3090Sc003SharedService.getDoropDownManageCompanyList(deleteDto.getHdnAddCompanyCd(), manageCompanyKubunList);
		if (NfwStringUtils.isNotEmpty(deleteDto.getHdnAddCompanyCd())) {
			companyListCode = skf3090Sc003SharedService.createManageCompanySelect(deleteDto.getHdnAddCompanyCd(), manageCompanyKubunList);
		} else {
			companyListCode = skf3090Sc003SharedService.createManageCompanySelect(null, manageCompanyKubunList);
		}
		listAddTableData.put("colAdd1", skf3090Sc003SharedService.createColAdd1Str(companyListCode, companyCdError));
		// 事業領域コード
		String bAreaCd = "";
		if (NfwStringUtils.isNotEmpty(deleteDto.getHdnAddBusinessAreaCd())) {
			bAreaCd = deleteDto.getHdnAddBusinessAreaCd();
		}
		listAddTableData.put("colAdd2", skf3090Sc003SharedService.createColAdd2Str(bAreaCd, bAreaCdError));
		// 管理機関
		// ----- エラー時用に、画面入力情報で管理機関を置き換える（置き換えないと、検索した時の情報に戻ってしまう）
		String bAreaName = "";
		if (NfwStringUtils.isNotEmpty(deleteDto.getHdnAddBusinessAreaName())) {
			bAreaName = deleteDto.getHdnAddBusinessAreaName();
		}
		listAddTableData.put("colAdd3", skf3090Sc003SharedService.createColAdd3Str(bAreaName, bAreaNameError));
		// 事業領域名
		// ----- エラー時用に、画面入力情報で事業領域名を置き換える（置き換えないと、検索した時の情報に戻ってしまう）
		if (NfwStringUtils.isNotEmpty(deleteDto.getHdnAddCompanyCd())) {
			String agencyListCode = "";
			if (NfwStringUtils.isNotEmpty(deleteDto.getHdnAddAgencyCd())) {
				agencyListCode = skf3090Sc003SharedService.createAgencySelect(deleteDto.getHdnAddAgencyCd(), skf3090Sc003SharedService.getAgencyCdList(deleteDto.getHdnAddCompanyCd(), true));
			} else {
				agencyListCode = skf3090Sc003SharedService.createAgencySelect(null, skf3090Sc003SharedService.getAgencyCdList(deleteDto.getHdnAddCompanyCd(), true));
			}
			listAddTableData.put("colAdd4",skf3090Sc003SharedService.createColAdd4Str(agencyListCode, agencyCdError, false));
		} else {
			listAddTableData.put("colAdd4",skf3090Sc003SharedService.createColAdd4Str(null, agencyCdError, true));
		}
		
		// 検索結果一覧
		// ----- エラー時用に、画面入力情報で置き換える（置き換えないと、検索した時の情報に戻ってしまう）
		String[] editInfoList = deleteDto.getRegistEditData().split("EndOfEditData");
		// 機関リスト
		List<Map<String, Object>> manageAgencyList = new ArrayList<Map<String, Object>>();
		for (int rowIndex = 0; rowIndex < deleteDto.getListTableData().size(); rowIndex++){
			// 行データの取得
			Map<String, Object> listTableData = deleteDto.getListTableData().get(rowIndex);	
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
		
		// 社宅貸与履歴をチェック
		List<Skf3090Sc003GetShatakuInfoExp> resultShatakuList = new ArrayList<Skf3090Sc003GetShatakuInfoExp>();
		Skf3090Sc003GetShatakuInfoExpParameter param = new Skf3090Sc003GetShatakuInfoExpParameter();
		param.setManageCompanyCd(deleteDto.getHdnRowCompanyCd());
		//param.setBusinessAreaCd(deleteDto.getHdnRowAgencyCd());
		param.setBusinessAreaCd(deleteDto.getHdnRowBusinessAreaCd());
		resultShatakuList = skf3090Sc003GetShatakuInfoExpRepository.getShatakuInfo(param);
		
		if(resultShatakuList.size() > 0){
			// 取得件数が1件以上の場合、エラーメッセージを設定し、処理を終了する。
			ServiceHelper.addErrorResultMessage(deleteDto, null, MessageIdConstant.E_SKF_3020);
			if(deleteDto.getListTableData().size() > 0){
				// 登録ボタン活性化
				deleteDto.setRegistButtonDisabled(false);			
			}else{
				// 登録ボタン非活性化
				deleteDto.setRegistButtonDisabled(true);			
			}		
		}else{
			int delCount = deleteJigyoryoikiInfo(deleteDto);
			if(delCount == -2){
				// 削除できなかった場合、エラーメッセージを設定し、処理を終了する。
				ServiceHelper.addErrorResultMessage(deleteDto, null, MessageIdConstant.W_SKF_1009);
				if(deleteDto.getListTableData().size() > 0){
					// 登録ボタン活性化
					deleteDto.setRegistButtonDisabled(false);			
				}else{
					// 登録ボタン非活性化
					deleteDto.setRegistButtonDisabled(true);			
				}		
				// 例外時はthrowしてRollBack
				throwBusinessExceptionIfErrors(deleteDto.getResultMessages());
			}else if(delCount == 0){
				// 削除できなかった場合、エラーメッセージを設定し、処理を終了する。
				ServiceHelper.addErrorResultMessage(deleteDto, null, MessageIdConstant.E_SKF_1076);
				if(deleteDto.getListTableData().size() > 0){
					// 登録ボタン活性化
					deleteDto.setRegistButtonDisabled(false);			
				}else{
					// 登録ボタン非活性化
					deleteDto.setRegistButtonDisabled(true);			
				}		
				// 例外時はthrowしてRollBack
				throwBusinessExceptionIfErrors(deleteDto.getResultMessages());
			}else{
				// 完了メッセージを表示する。
				ServiceHelper.addResultMessage(deleteDto, MessageIdConstant.I_SKF_1013);

		        // 再検索（事業領域情報取得）
				List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
				int listCount = skf3090Sc003SharedService.getListTableData(deleteDto.getSelectedManageCompanyCd(), deleteDto.getBusinessAreaCd(), deleteDto.getBusinessAreaName(), null, listTableData);
				if(listCount == 0){
					// 取得レコード0件のワーニング
					ServiceHelper.addWarnResultMessage(deleteDto, MessageIdConstant.W_SKF_1007, String.valueOf(listCount));
					// 登録ボタン非活性化
					deleteDto.setRegistButtonDisabled(true);
				}else{
					// 最大行数を設定
					deleteDto.setListTableMaxRowCount(listTableMaxRowCount);
					// 登録ボタン活性化
					deleteDto.setRegistButtonDisabled(false);
				}
				deleteDto.setListTableData(listTableData);
				
				// 画面遷移(検索して表示)
				//TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF3090_SC003, "search");
				//deleteDto.setTransferPageInfo(nextPage);	
			}
		}
		
		// 追加領域の情報ももとに戻す
		List<Map<String, Object>> listAddTableDataNew = new ArrayList<Map<String, Object>>();
		listAddTableDataNew = skf3090Sc003SharedService.getListAddTableDataViewColumn(deleteDto.getHdnAddCompanyCd(), manageCompanyKubunList, deleteDto.getHdnAddBusinessAreaCd(), deleteDto.getHdnAddBusinessAreaName(), deleteDto.getHdnAddAgencyCd(), deleteDto.getErrorListAddTable());
		deleteDto.setListAddTableData(listAddTableDataNew);
		
		return deleteDto;
	}
	
	
	/**
	 * 事業領域削除処理
	 * 
	 * @param deleteDto インプットDTO
	 * @return 処理結果
	 */
	private int deleteJigyoryoikiInfo(Skf3090Sc003DeleteDto deleteDto){
		int delCount = 0;
		
		// 日付返還
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		Date mapDate = null;
		try{
			mapDate = dateFormat.parse(deleteDto.getHdnRowUpdateDate().toString());
		}catch(ParseException ex){
			LogUtils.infoByMsg("deleteJigyoryoikiInfo, 事業領域情報-更新日時変換失敗 :" + deleteDto.getHdnRowUpdateDate().toString());
			return -2;
		}		

		// 排他チェック（skf.skf1010_m_business_area）
		Skf1010MBusinessAreaKey key = new Skf1010MBusinessAreaKey();
		key.setCompanyCd(deleteDto.getHdnRowCompanyCd());
		key.setBusinessAreaCd(skf3090Sc003SharedService.escapeString(deleteDto.getHdnRowBusinessAreaCd()));
		Skf1010MBusinessArea bussinessArea = skf1010MBusinessAreaRepository.selectByPrimaryKey(key);
		if(bussinessArea == null){
			LogUtils.debugByMsg("事業領域情報データ取得結果NULL");
			return -2;
		}
		super.checkLockException(mapDate, bussinessArea.getUpdateDate());			
		
		// 事業領域情報削除
		delCount = skf1010MBusinessAreaRepository.deleteByPrimaryKey(key);
		
		// 管理会社が外部機関の場合は機関マスタも削除する
		if(GAIBU_COMPANY_CD.equals(deleteDto.getHdnRowCompanyCd())){
			Skf1010MAgencyKey agencyKey = new Skf1010MAgencyKey();
			agencyKey.setCompanyCd(deleteDto.getHdnRowCompanyCd());
			agencyKey.setAgencyCd(deleteDto.getHdnRowBusinessAreaCd());
			int delAgencyCount = skf1010MAgencyRepository.deleteByPrimaryKey(agencyKey);
			if(delAgencyCount == 0){
				// 機関マスタを削除できなかった場合は0件で呼びもとへ返却（ロールバック）
				delCount = 0;
			}
		}
		
		return delCount;
	}
	
}
