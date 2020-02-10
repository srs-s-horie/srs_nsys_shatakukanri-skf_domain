/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc004;

import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc004.Skf2010Sc004TransferDto;

/**
 * Skf2010Sc004 申請内容表示/引戻し動的遷移処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc004TransferService extends BaseServiceAbstract<Skf2010Sc004TransferDto> {

	/**
	 * サービス処理を行う。
	 * 
	 * @param transferDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public BaseDto index(Skf2010Sc004TransferDto transferDto) throws Exception {

		// 前の画面に遷移する
		TransferPageInfo tpi = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC003);
		tpi.addResultMessage(MessageIdConstant.I_SKF_2047);
		transferDto.setTransferPageInfo(tpi);

		return transferDto;
	}

}
