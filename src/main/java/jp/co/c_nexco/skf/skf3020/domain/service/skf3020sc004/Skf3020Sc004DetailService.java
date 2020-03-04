/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.service.skf3020sc004;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3020.domain.dto.skf3020sc004.Skf3020Sc004DetailDto;

/**
 * Skf3020Sc004DetailService 転任者一覧画面のDetailサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3020Sc004DetailService extends BaseServiceAbstract<Skf3020Sc004DetailDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;	
	
	/**
	 * サービス処理を行う。
	 * （転入者情報登録画面への遷移のみのため、操作ログのみ）
	 * 
	 * @param detailDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf3020Sc004DetailDto index(Skf3020Sc004DetailDto detailDto) throws Exception {
		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("詳細", CodeConstant.C001, detailDto.getPageId());		
		
		return detailDto;
	}	
}
