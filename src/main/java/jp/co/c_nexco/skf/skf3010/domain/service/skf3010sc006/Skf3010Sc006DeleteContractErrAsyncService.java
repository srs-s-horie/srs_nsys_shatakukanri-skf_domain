/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc006;

import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.skf.common.SkfAsyncServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc006.Skf3010Sc006DeleteContractErrAsyncDto;


/**
 * Skf3010Sc006DeleteContractErrAsyncService 借上社宅登録画面の契約情報削除不可メッセージ表示サービス処理クラス。　 
 * 非同期
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3010Sc006DeleteContractErrAsyncService extends SkfAsyncServiceAbstract<Skf3010Sc006DeleteContractErrAsyncDto> {

	/**
	 * 借上社宅登録画面の契約情報削除不可メッセージ表示を行う。　
	 * 
	 * @param initDto	インプットDTO
	 * @return 処理結果
	 * @throws Exception	例外
	 */
	@Override
	public AsyncBaseDto index(Skf3010Sc006DeleteContractErrAsyncDto asyncDto) throws Exception {

		ServiceHelper.addErrorResultMessage(asyncDto, null, MessageIdConstant.E_SKF_3035);
		throwBusinessExceptionIfErrors(asyncDto.getResultMessages());

		return asyncDto;
	}
}
