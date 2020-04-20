/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3030.domain.service.skf3030sc002;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.skf.common.SkfAsyncServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3030.domain.dto.skf3030sc002.Skf3030Sc002ChangeNyutaikyoYoteiAsyncDto;

/**
 * Skf3030Sc002ChangeNyutaikyoYoteiAsyncService 入退居情報登録画面：入退居予定日変更非同期呼出処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3030Sc002ChangeNyutaikyoYoteiAsyncService
	extends SkfAsyncServiceAbstract<Skf3030Sc002ChangeNyutaikyoYoteiAsyncDto> {

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
	public Skf3030Sc002ChangeNyutaikyoYoteiAsyncDto index(Skf3030Sc002ChangeNyutaikyoYoteiAsyncDto asyncDto) throws Exception {

		// デバッグログ
		LogUtils.debugByMsg("入退居予定日変更");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("入退居予定日変更", CodeConstant.C001, FunctionIdConstant.SKF3030_SC002);

		//日付チェック→入力を信用する
		
		//使用料計算
		skf3030Sc002SharedService.siyoryoKeiSanAsync(asyncDto);
		throwBusinessExceptionIfErrors(asyncDto.getResultMessages());
		
		return asyncDto;
	}
}
