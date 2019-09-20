/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2030.domain.service.skf2030sc002;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2030.domain.dto.skf2030sc002.Skf2030Sc002SendbackDto;

/**
 * Skf2030Sc002 備品希望申請（アウトソース用)差戻し処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2030Sc002SendbackService extends BaseServiceAbstract<Skf2030Sc002SendbackDto> {

	@Autowired
	private Skf2030Sc002SharedService skf2030Sc002SharedService;

	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	/**
	 * サービス処理を行う。
	 * 
	 * @param sbDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public BaseDto index(Skf2030Sc002SendbackDto sbDto) throws Exception {
		// 操作ログ出力
		skfOperationLogUtils.setAccessLog("差戻し処理開始", CodeConstant.C001, FunctionIdConstant.SKF2030_SC002);
		// タイトル設定
		sbDto.setPageTitleKey(MessageIdConstant.SKF2030_SC002_TITLE);

		// ログインユーザー情報取得
		Map<String, String> loginUserInfo = skfLoginUserInfoUtils.getSkfLoginUserInfo();

		// 申請情報設定
		Map<String, String> applInfo = new HashMap<String, String>();
		applInfo.put("status", sbDto.getSendApplStatus());
		applInfo.put("applNo", sbDto.getApplNo());
		applInfo.put("applId", sbDto.getApplId());

		// 入力チェック
		if (!skf2030Sc002SharedService.validateReason(sbDto, true)) {
			throwBusinessExceptionIfErrors(sbDto.getResultMessages());
		}

		String execName = "sendback";

		boolean updResult = skf2030Sc002SharedService.updateDispInfo(execName, sbDto, applInfo, loginUserInfo);
		if (!updResult) {
			throwBusinessExceptionIfErrors(sbDto.getResultMessages());
			return sbDto;
		}

		// 前の画面に遷移する
		TransferPageInfo tpi = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC005);
		tpi.addResultMessage(MessageIdConstant.I_SKF_2030);
		sbDto.setTransferPageInfo(tpi);

		return sbDto;
	}

}