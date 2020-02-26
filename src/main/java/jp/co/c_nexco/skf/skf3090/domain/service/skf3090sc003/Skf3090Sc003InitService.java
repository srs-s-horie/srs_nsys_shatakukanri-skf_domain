/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc003;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc003.Skf3090Sc003SharedService;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc003.Skf3090Sc003InitDto;

/**
 * 事業領域名マスタ登録画面のInitサービス処理クラス。　 
 * 
 */
@Service
public class Skf3090Sc003InitService extends BaseServiceAbstract<Skf3090Sc003InitDto> {
	
	@Autowired
	Skf3090Sc003SharedService skf3090Sc003SharedService;

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	
	/**
	 * サービス処理を行う。　
	 * 
	 * @param initDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public Skf3090Sc003InitDto index(Skf3090Sc003InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF3090_SC003_TITLE);
 		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, initDto.getPageId());
		
		// 「管理会社」ドロップダウンリストの設定
		List<Map<String, Object>> manageCompanyKubunList = new ArrayList<Map<String, Object>>();
		//skf3090Sc003SharedService.getDoropDownManageCompanyList(initDto.getSelectedManageCompanyCd(), manageCompanyKubunList);
		skf3090Sc003SharedService.getDoropDownManageCompanyList(null, manageCompanyKubunList);
		initDto.setManageCompanyList(manageCompanyKubunList);
		
		initDto.setBusinessAreaCd(null);
		initDto.setBusinessAreaName(null);
		
		// 登録ボタン非活性
		initDto.setRegistButtonDisabled(true);
		
		// 追加領域の管理会社ドロップダウンリストにも設定
		List<Map<String, Object>> listAddTableData = new ArrayList<Map<String, Object>>();
		listAddTableData = skf3090Sc003SharedService.getListAddTableDataViewColumn(null,manageCompanyKubunList, null, null, null, null);
		initDto.setListAddTableData(listAddTableData);
		
		// 検索結果一覧は空で設定
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
		initDto.setListTableData(listTableData);
		
		return initDto;
	}
	
}
