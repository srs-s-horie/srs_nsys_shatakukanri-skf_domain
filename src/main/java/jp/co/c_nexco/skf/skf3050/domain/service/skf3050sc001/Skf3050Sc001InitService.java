/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3050.domain.service.skf3050sc001;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3050.domain.dto.skf3050sc001.Skf3050Sc001InitDto;

/**
 * Skf3050Sc001InitService 社員番号一括設定のInitサービス処理クラス。　 
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3050Sc001InitService extends SkfServiceAbstract<Skf3050Sc001InitDto> {
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3050Sc001SharedService skf3050Sc001SharedService;
	
	// リストテーブルの１ページ最大表示行数
	@Value("${skf3050.skf3050_sc001.max_row_count}")
	private String listTableMaxRowCount;
	
	/**
	 * サービス処理を行う。　
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf3050Sc001InitDto index(Skf3050Sc001InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF3050_SC001_TITLE);
 		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, FunctionIdConstant.SKF3050_SC001);
		
		//検索結果一覧用
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
		
		int count = skf3050Sc001SharedService.setGrvShainBangoList(listTableData);
		
		if(count == 0){
			//社員情報確認ボタンを非活性
			initDto.setBtnShainInfoCheckDisabled("true");
		}else{
			//社員情報確認ボタンを活性
			initDto.setBtnShainInfoCheckDisabled("false");
		}
		
		//登録ボタンを非活性に設定
		initDto.setBtnRegistDisabled("true");
		
		initDto.setListTableData(listTableData);
		//最大行数を設定
		initDto.setListPage("1");//初期表示時は1ページ目
		initDto.setListTableMaxRowCount(listTableMaxRowCount);
		
		return initDto;
	}
	
}
