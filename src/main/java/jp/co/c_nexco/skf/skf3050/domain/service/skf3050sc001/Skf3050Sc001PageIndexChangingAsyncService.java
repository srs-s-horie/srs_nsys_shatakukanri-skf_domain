/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3050.domain.service.skf3050sc001;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.skf.common.SkfAsyncServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3050.domain.dto.skf3050sc001.Skf3050Sc001PageIndexChangingAsyncDto;


/**
 * Skf3050Sc001PageIndexChangingAsyncService 社員番号一括設定ページ変更非同期処理クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3050Sc001PageIndexChangingAsyncService
		extends SkfAsyncServiceAbstract<Skf3050Sc001PageIndexChangingAsyncDto> {
	@Autowired
	private Skf3050Sc001SharedService skf3050Sc001SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	

	/**
	 * ページング時リセット処理
	 */
	@Override
	public AsyncBaseDto index(Skf3050Sc001PageIndexChangingAsyncDto asyncDto) throws Exception {
		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("ページング", CodeConstant.C001, FunctionIdConstant.SKF3050_SC001);
				
		//検索結果一覧用
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
		
		skf3050Sc001SharedService.setGrvShainBangoList(listTableData);
		
		
		//最大行数を設定
		asyncDto.setListPage(asyncDto.getListPage());//初期表示時は1ページ目
		
		asyncDto.setListTableData(listTableData);
		
		return asyncDto;
	}
	
	

}
