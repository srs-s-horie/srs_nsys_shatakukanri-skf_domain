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
import jp.co.c_nexco.skf.skf3030.domain.dto.skf3030sc002.Skf3030Sc002ParkingSupportCallBackAsyncDto;

/**
 * Skf3030Sc002ParkingSupportCallBackAsyncService 入退居情報登録画面：駐車場入力支援呼び出し後処理非同期処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3030Sc002ParkingSupportCallBackAsyncService
	extends SkfAsyncServiceAbstract<Skf3030Sc002ParkingSupportCallBackAsyncDto> {

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
	public Skf3030Sc002ParkingSupportCallBackAsyncDto index(Skf3030Sc002ParkingSupportCallBackAsyncDto asyncDto) throws Exception {

		// デバッグログ
		LogUtils.debugByMsg("駐車場入力支援(後処理)");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("駐車場入力支援(後処理)", CodeConstant.C001, FunctionIdConstant.SKF3030_SC002);

		//使用料を再計算
		skf3030Sc002SharedService.siyoryoKeiSanAsync(asyncDto);
		throwBusinessExceptionIfErrors(asyncDto.getResultMessages());
		
		//LoadCompleteイベントで備品状態の再読み込みを行わない
		//bihinItiranReloadFlg = True →再読み込みしないので実行しない

		
		return asyncDto;
	}
}
