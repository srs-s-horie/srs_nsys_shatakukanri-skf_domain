/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.service.skf2040sc002;

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
import jp.co.c_nexco.skf.common.util.SkfMailUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040sc002.Skf2040Sc002RemandDto;

/**
 * Skf2040Sc002 退居（自動車の保管場所返還）届(アウトソース用）画面差戻し処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2040Sc002RemandService extends BaseServiceAbstract<Skf2040Sc002RemandDto> {

	@Autowired
	private Skf2040Sc002SharedService skf2040sc002SharedService;
	@Autowired
	private SkfMailUtils skfMailUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	private String sTrue = "true";
	private String sFalse = "false";

	@Override
	public BaseDto index(Skf2040Sc002RemandDto remDto) throws Exception {

		// 操作ログ出力メソッドを呼び出す
		skfOperationLogUtils.setAccessLog("差戻し", CodeConstant.C001, remDto.getPageId());

		// コメント欄チェック
		boolean validate = skf2040sc002SharedService.checkValidation(remDto, sTrue);
		if (!validate) {
			// 添付資料だけはセッションから再取得の必要あり
			skf2040sc002SharedService.setAttachedFileList(remDto);
			return remDto;
		}

		// 申請書類履歴保存の処理
		Map<String, String> errorMsg = new HashMap<String, String>();
		boolean result = skf2040sc002SharedService.saveApplInfo(CodeConstant.STATUS_HININ, remDto, errorMsg);

		if (result) {
			// 差戻し（否認）通知メール送信
			Map<String, String> applInfo = new HashMap<String, String>();
			applInfo.put("applNo", remDto.getApplNo());
			applInfo.put("applId", remDto.getApplId());
			applInfo.put("applShainNo", remDto.getShainNo());

			String commentNote = remDto.getCommentNote();

			// メールの記載URLは「申請状況一覧画面」
			String urlBase = "/skf/Skf2010Sc003/init?SKF2010_SC003&menuflg=1&tokenCheck=0";

			skfMailUtils.sendApplTsuchiMail(CodeConstant.HININ_KANRYO_TSUCHI, applInfo, commentNote, CodeConstant.NONE,
					remDto.getShainNo(), CodeConstant.NONE, urlBase);

		}

		TransferPageInfo tpi = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC005);
		tpi.addResultMessage(MessageIdConstant.I_SKF_2033);
		remDto.setTransferPageInfo(tpi);

		return remDto;

	}

}
