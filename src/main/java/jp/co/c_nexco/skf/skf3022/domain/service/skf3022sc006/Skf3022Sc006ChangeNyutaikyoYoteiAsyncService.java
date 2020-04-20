/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc006;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.skf.common.SkfAsyncServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc006.Skf3022Sc006ChangeNyutaikyoYoteiAsyncDto;

/**
 * Skf3022Sc006ChangeNyutaikyoYoteiAsyncService 提示データ登録画面：入退居予定日変更非同期呼出処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3022Sc006ChangeNyutaikyoYoteiAsyncService
	extends SkfAsyncServiceAbstract<Skf3022Sc006ChangeNyutaikyoYoteiAsyncDto> {

	@Autowired
	private Skf3022Sc006SharedService skf3022Sc006SharedService;
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
	public Skf3022Sc006ChangeNyutaikyoYoteiAsyncDto index(Skf3022Sc006ChangeNyutaikyoYoteiAsyncDto asyncDto) throws Exception {

		// デバッグログ
		LogUtils.debugByMsg("入退居予定日変更");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("入退居予定日変更", CodeConstant.C001, FunctionIdConstant.SKF3022_SC006);
		/** パラメータ取得 */
		// 使用料計算用Map
		Map<String, String> paramMap = asyncDto.getMapParam();
		// 戻り値初期化
		skf3022Sc006SharedService.initializeSiyoryoKeiSanParamAsync(asyncDto);
		// 使用料計算処理
		Map<String, String> resultMap = new HashMap<String, String>();	// 使用料計算戻り値
		StringBuffer errMsg = new StringBuffer();						// エラーメッセージ
		if (skf3022Sc006SharedService.siyoryoKeiSan("", "", paramMap, resultMap, errMsg)) {
			// 使用料計算でエラー
//			ServiceHelper.addErrorResultMessage(asyncDto, null, MessageIdConstant.SKF3020_ERR_MSG_COMMON, errMsg);
//			throwBusinessExceptionIfErrors(asyncDto.getResultMessages());
		} else {
			// 使用料計算戻り値設定
			skf3022Sc006SharedService.setSiyoryoKeiSanParamAsync(resultMap, asyncDto);
		}
		return asyncDto;
	}
}
