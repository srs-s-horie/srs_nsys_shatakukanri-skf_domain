/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3030.domain.service.skf3030sc002;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3030.domain.dto.skf3030sc002.Skf3030Sc002SiyoryoKeisanAsyncDto;

/**
 * Skf3030Sc002SiyoryoKeisanAsyncService 入退居情報登録画面：使用料計算呼び出し非同期処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3030Sc002SiyoryoKeisanAsyncService
	extends AsyncBaseServiceAbstract<Skf3030Sc002SiyoryoKeisanAsyncDto> {

	@Autowired
	private Skf3030Sc002SharedService skf3030Sc002SharedService;
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
	public Skf3030Sc002SiyoryoKeisanAsyncDto index(Skf3030Sc002SiyoryoKeisanAsyncDto asyncDto) throws Exception {

		// デバッグログ
		LogUtils.debugByMsg("使用料再計算");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("使用料再計算", CodeConstant.C001, FunctionIdConstant.SKF3030_SC002);

		//使用料を再計算
		skf3030Sc002SharedService.siyoryoKeiSanAsync(asyncDto);
		throwBusinessExceptionIfErrors(asyncDto.getResultMessages());
		
		return asyncDto;
	}
}
