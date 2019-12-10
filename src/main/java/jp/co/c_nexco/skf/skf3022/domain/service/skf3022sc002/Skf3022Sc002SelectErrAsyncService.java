/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc002;

import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc002.Skf3022Sc002SelectErrAsyncDto;

/**
 * Skf3022Sc002SelectErrAsyncService 駐車場入力支援未選択エラーメッセージ表示非同期処理クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3022Sc002SelectErrAsyncService
		extends AsyncBaseServiceAbstract<Skf3022Sc002SelectErrAsyncDto> {

	/**
	 * 駐車場情報の取得サービス
	 */
	@Override
	public AsyncBaseDto index(Skf3022Sc002SelectErrAsyncDto asyncDto) throws Exception {

		// デバッグログ
		LogUtils.debugByMsg("未選択");

		ServiceHelper.addErrorResultMessage(asyncDto, null, MessageIdConstant.E_SKF_3004);
		throwBusinessExceptionIfErrors(asyncDto.getResultMessages());

		return asyncDto;
	}
}
