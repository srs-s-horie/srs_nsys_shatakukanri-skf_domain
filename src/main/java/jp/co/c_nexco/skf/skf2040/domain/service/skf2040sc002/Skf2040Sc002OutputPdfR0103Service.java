/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.service.skf2040sc002;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040sc002.Skf2040Sc002OutputPdfR0103Dto;
import jp.co.c_nexco.skf.skf2040.domain.service.common.OutputPdfR0103BaseService;

/**
 * Skf2040Sc002 退居（自動車の保管場所返還（アウトソース用）届）画面
 * の退居（自動車の保管場所返還）届（Skf2040Rp001）PDF出力処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2040Sc002OutputPdfR0103Service extends OutputPdfR0103BaseService<Skf2040Sc002OutputPdfR0103Dto> {

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
	protected void beforeIndexProc(Skf2040Sc002OutputPdfR0103Dto dto) {
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("退居（自動車の保管場所返還）届PDF出力", CodeConstant.C001, FunctionIdConstant.SKF2040_SC002);
	}
}
