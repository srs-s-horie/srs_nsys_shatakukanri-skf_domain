/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.service.skf2040sc002;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfMailUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040sc002.Skf2040Sc002RevisionDto;

/**
 * Skf2040Sc002 退居（自動車の保管場所返還）届(アウトソース用）画面修正依頼処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2040Sc002RevisionService extends SkfServiceAbstract<Skf2040Sc002RevisionDto> {

	@Autowired
	private Skf2040Sc002SharedService skf2040sc002SharedService;
	@Autowired
	private SkfMailUtils skfMailUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	private String sTrue = "true";

	@Override
	public BaseDto index(Skf2040Sc002RevisionDto rvsDto) throws Exception {

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("修正依頼", CodeConstant.C001, rvsDto.getPageId());

		// コメント欄チェック
		boolean validate = skf2040sc002SharedService.checkValidation(rvsDto, sTrue);
		if (!validate) {
			// 添付資料だけはセッションから再取得の必要あり
			List<Map<String, Object>> reAttachedFileList = skf2040sc002SharedService.setAttachedFileList();
			rvsDto.setAttachedFileList(reAttachedFileList);
			return rvsDto;
		}

		// 申請書類履歴保存の処理
		Map<String, String> errorMsg = new HashMap<String, String>();
		boolean result = skf2040sc002SharedService.saveApplInfo(CodeConstant.STATUS_SASHIMODOSHI, rvsDto, errorMsg);

		if (result) {
			// 修正依頼通知メール送信
			Map<String, String> applInfo = new HashMap<String, String>();
			applInfo.put("applNo", rvsDto.getApplNo());
			applInfo.put("applId", rvsDto.getApplId());
			applInfo.put("applShainNo", rvsDto.getShainNo());

			String commentNote = rvsDto.getCommentNote();

			// メールの記載URLは「申請状況一覧画面」
			String urlBase = "/skf/Skf2010Sc003/init?SKF2010_SC003&menuflg=1&tokenCheck=0";

			skfMailUtils.sendApplTsuchiMail(CodeConstant.SASHIMODOSHI_KANRYO_TSUCHI, applInfo, commentNote,
					CodeConstant.NONE, rvsDto.getShainNo(), CodeConstant.NONE, urlBase);
		} else {
			throwBusinessExceptionIfErrors(rvsDto.getResultMessages());
			return rvsDto;
		}

		TransferPageInfo tpi = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC005);
		tpi.addResultMessage(MessageIdConstant.I_SKF_2030);
		rvsDto.setTransferPageInfo(tpi);

		return rvsDto;

	}

}
