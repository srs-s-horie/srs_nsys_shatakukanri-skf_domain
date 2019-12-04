/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc006;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc006.Skf2010Sc006OutputPdfR0101Dto;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc006.Skf2010Sc006OutputPdfR0103Dto;
import jp.co.c_nexco.skf.skf2010.domain.service.common.OutputPdfR0101BaseService;
import jp.co.c_nexco.skf.skf2010.domain.service.common.OutputPdfR0103BaseService;

/**
 * Skf2010Sc006 申請書類承認／差戻し／通知画面の退居（自動車の保管場所返還）届（Skf2040Rp001）PDF出力処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc006OutputPdfR0103Service extends OutputPdfR0103BaseService<Skf2010Sc006OutputPdfR0103Dto> {

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
	protected void beforeIndexProc(Skf2010Sc006OutputPdfR0103Dto dto) {
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("退居（自動車の保管場所返還）届PDF出力", CodeConstant.C001, FunctionIdConstant.SKF2010_SC006);
	}
}