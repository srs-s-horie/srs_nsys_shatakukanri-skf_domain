/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc002;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc002.Skf2010Sc002OutputPdfTabularDto;
import jp.co.c_nexco.skf.skf2010.domain.service.common.OutputPdfTabularBaseService;

/**
 * Skf2010Sc007 申請条件確認画面の表形式PDF出力処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc002OutputPdfTabularService extends OutputPdfTabularBaseService<Skf2010Sc002OutputPdfTabularDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	/**
	 * PDF出力前に行う画面追加処理を記述する。
	 * 
	 * <pre>
	 * 本メソッドに記述された処理はPDF出力処理実行前に実行される。
	 * 特に行う処理が無い場合は空実装とすること。
	 * </pre>
	 * 
	 * @param dto
	 */
	@Override
	protected void beforeIndexProc(Skf2010Sc002OutputPdfTabularDto dto) {
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("表形式テストPDF出力", CodeConstant.C001, FunctionIdConstant.SKF2010_SC002);
	}
}
