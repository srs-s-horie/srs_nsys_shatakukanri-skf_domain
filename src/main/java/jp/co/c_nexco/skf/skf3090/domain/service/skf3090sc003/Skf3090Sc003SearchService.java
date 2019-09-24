/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc003;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc003.Skf3090Sc003SharedService;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc003.Skf3090Sc003SearchDto;

/**
 * 事業領域名マスタ登録画面のSearchサービス処理クラス。　 
 * 
 */
@Service
public class Skf3090Sc003SearchService extends BaseServiceAbstract<Skf3090Sc003SearchDto> {

	@Autowired
	Skf3090Sc003SharedService skf3090Sc003SharedService;

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	
	// リストテーブルの１ページ最大表示行数
	@Value("${skf3090.skf3090_sc003.max_row_count}")
	private String listTableMaxRowCount;	
	
	/**
	 * サービス処理を行う。　
	 * 
	 * @param searchDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public Skf3090Sc003SearchDto index(Skf3090Sc003SearchDto searchDto) throws Exception {
		
		searchDto.setPageTitleKey(MessageIdConstant.SKF3090_SC003_TITLE);
 		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("検索", CodeConstant.C001, searchDto.getPageId());
		
		// グリッドビューデータの転任者一覧取得
		SetGridViewData(searchDto);
		
		// 「管理会社」ドロップダウンリストの設定
		List<Map<String, Object>> manageCompanyKubunList = new ArrayList<Map<String, Object>>();
		skf3090Sc003SharedService.getDoropDownManageCompanyList(searchDto.getSelectedManageCompanyCd(), manageCompanyKubunList);
		searchDto.setManageCompanyList(manageCompanyKubunList);
		
		// 追加領域の管理会社ドロップダウンリストにも設定
		List<Map<String, Object>> listAddTableData = new ArrayList<Map<String, Object>>();
		listAddTableData = skf3090Sc003SharedService.getListAddTableDataViewColumn(searchDto.getHdnAddCompanyCd(), manageCompanyKubunList, searchDto.getHdnAddBusinessAreaCd(), searchDto.getHdnAddBusinessAreaName(), searchDto.getHdnAddAgencyCd(), searchDto.getErrorListAddTable());
		searchDto.setListAddTableData(listAddTableData);
		
		return searchDto;
	}	
	
	/**
	 * 検索部のセット
	 * 
	 * @param initDto
	 */
	private void SetGridViewData(Skf3090Sc003SearchDto searchDto){
		
        // 検索条件をもとに検索結果カウントの取得
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
		
		// 事業領域情報取得
		int listCount = skf3090Sc003SharedService.getListTableData(searchDto.getSelectedManageCompanyCd(), searchDto.getBusinessAreaCd(), searchDto.getBusinessAreaName(), null, listTableData);
		if(listCount == 0){
			// 取得レコード0件のワーニング
			ServiceHelper.addWarnResultMessage(searchDto, MessageIdConstant.W_SKF_1007, String.valueOf(listCount));
			// 登録ボタン非活性化
			searchDto.setRegistButtonDisabled(true);
		}else{
			// 最大行数を設定
			searchDto.setListTableMaxRowCount(listTableMaxRowCount);
			// 登録ボタン活性化
			searchDto.setRegistButtonDisabled(false);
		}
		searchDto.setListTableData(listTableData);
	}
	
}
