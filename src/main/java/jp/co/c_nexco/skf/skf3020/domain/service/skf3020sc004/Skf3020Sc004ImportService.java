/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.service.skf3020sc004;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3020.domain.dto.skf3020sc004.Skf3020Sc004ImportDto;

/**
 * Skf3020Sc004ImportService 転任者一覧画面のImportサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3020Sc004ImportService extends SkfServiceAbstract<Skf3020Sc004ImportDto> {


	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;	
	
	/**
	 * サービス処理を行う。
	 * （転入者調書取込画面への遷移のみのため、操作ログのみ）
	 * 
	 * @param importDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf3020Sc004ImportDto index(Skf3020Sc004ImportDto importDto) throws Exception {
		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("転任者調書取込", CodeConstant.C001, FunctionIdConstant.SKF3020_SC004);		
		
		return importDto;
	}

}
