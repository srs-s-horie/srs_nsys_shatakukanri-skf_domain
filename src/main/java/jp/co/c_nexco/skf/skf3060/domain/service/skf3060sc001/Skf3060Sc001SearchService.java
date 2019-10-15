/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3060.domain.service.skf3060sc001;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3060.domain.service.skf3060sc001.Skf3060Sc001SharedService;
import jp.co.c_nexco.skf.skf3060.domain.dto.skf3060sc001.Skf3060Sc001SearchDto;

/**
 * 事業領域名マスタ登録画面のSearchサービス処理クラス。　 
 * 
 */
@Service
public class Skf3060Sc001SearchService extends BaseServiceAbstract<Skf3060Sc001SearchDto> {

	@Autowired
	Skf3060Sc001SharedService skf3060Sc001SharedService;
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
 
    @Value("${skf.common.validate_error}")
    private String validationErrorCode;	
    
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
	public Skf3060Sc001SearchDto index(Skf3060Sc001SearchDto searchDto) throws Exception {
		
		searchDto.setPageTitleKey(MessageIdConstant.SKF3060_SC001_TITLE);
 		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("検索", CodeConstant.C001, searchDto.getPageId());
		
		// エラー状態クリア
		searchDto.setBaseTermFromErr("");
		searchDto.setBaseTermToErr("");
		
		// ドロップダウンリスト再設定
		// - 原籍会社名
		// - 給与支給会社名
		// - 送信状態
		List<Map<String, Object>> originalCompanyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> salaryCompanyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sendMailStatusList = new ArrayList<Map<String, Object>>();
        skf3060Sc001SharedService.getDoropDownList(searchDto.getSelectedOriginalCompanyCd(),originalCompanyList,
        		searchDto.getSelectedSalaryCompanyCd(), salaryCompanyList,
        		searchDto.getSelectedSendMailStatus(), sendMailStatusList);
        searchDto.setOriginalCompanyCdList(originalCompanyList);
        searchDto.setSalaryCompanyCdList(salaryCompanyList);
        searchDto.setSendMailStatusList(sendMailStatusList);
		
		// 入力エラーチェック
		if(true == skf3060Sc001SharedService.isValidateInput(searchDto)){
			// エラーなし
	        // 検索条件のセット
			// --　システム年月を取得
			String setGrvAgeKasanInfo = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu();
			
			// 検索実行
	        // - 年齢加算対象者データ一覧を取得
			skf3060Sc001SharedService.setGrvAgeKasanInfo(searchDto, setGrvAgeKasanInfo, 
					skf3060Sc001SharedService.PROCESS_PATTERN_SEARCH);			
		}
		
		return searchDto;
	}	
	
}
