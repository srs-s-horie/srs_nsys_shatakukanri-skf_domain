/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3021.domain.service.skf3021sc001;

import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.skf3021.domain.dto.skf3021sc001.Skf3021Sc001SelectErrAsyncDto;


/**
 * Skf3021Sc001SelectErrAsyncService 入退居予定一覧画面未選択エラーメッセージ表示非同期処理クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3021Sc001SelectErrAsyncService
		extends AsyncBaseServiceAbstract<Skf3021Sc001SelectErrAsyncDto> {

	/**
	 * 駐車場情報の取得サービス
	 */
	@Override
	public AsyncBaseDto index(Skf3021Sc001SelectErrAsyncDto asyncDto) throws Exception {

		// デバッグログ
		LogUtils.debugByMsg("未選択");

		String errCode = asyncDto.getErrCode();
		ServiceHelper.addErrorResultMessage(asyncDto, null, errCode);
		throwBusinessExceptionIfErrors(asyncDto.getResultMessages());

		return asyncDto;
	}
}
