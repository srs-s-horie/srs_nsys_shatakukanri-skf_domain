/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc009;

import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.SkfAsyncServiceAbstract;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc009.Skf2100Sc009SelectErrAsyncDto;

/**
 * Skf3022Sc001SelectErrAsyncService モバイルルーター機器入力支援未選択エラーメッセージ表示非同期処理クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf2100Sc009SelectErrAsyncService
		extends SkfAsyncServiceAbstract<Skf2100Sc009SelectErrAsyncDto> {

	/**
	 * 駐車場情報の取得サービス
	 */
	@Override
	public AsyncBaseDto index(Skf2100Sc009SelectErrAsyncDto asyncDto) throws Exception {

		// デバッグログ
		LogUtils.debugByMsg("未選択");

		ServiceHelper.addErrorResultMessage(asyncDto, null, MessageIdConstant.E_SKF_1054,"モバイルルーター");
		throwBusinessExceptionIfErrors(asyncDto.getResultMessages());

		return asyncDto;
	}
}
