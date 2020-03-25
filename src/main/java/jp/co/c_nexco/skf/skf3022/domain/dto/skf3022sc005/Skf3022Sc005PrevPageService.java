/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc005;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;

/**
 * Skf3010Sc001PrevPageService 戻るサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3022Sc005PrevPageService extends BaseServiceAbstract<Skf3022Sc005PrevPageDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	/**
	 * 前の画面へ戻るメソッド
	 * 
	 * @param prevPageDto インプットDTO
	 * @return 処理結果
	 */
	@Override
	public Skf3022Sc005PrevPageDto index(Skf3022Sc005PrevPageDto prevPageDto) {

		// デバッグログ
		LogUtils.debugByMsg("戻る");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("戻る", CodeConstant.C001, FunctionIdConstant.SKF3022_SC005);

		// 画面遷移（戻る）
		TransferPageInfo nextPage = TransferPageInfo.prevPage(FunctionIdConstant.SKF1010_SC001, "init");
		prevPageDto.setTransferPageInfo(nextPage);
 
		return prevPageDto;
	}
	
}
