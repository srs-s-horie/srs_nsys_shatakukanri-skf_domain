/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2020.domain.service.skf2020sc003;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfMailUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2020.domain.dto.skf2020sc003.Skf2020Sc003RevisionDto;

/**
 * Skf2020Sc003 社宅入居希望等調書（アウトソース用）修正依頼処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2020Sc003RevisionService extends BaseServiceAbstract<Skf2020Sc003RevisionDto> {

	@Autowired
	private Skf2020Sc003SharedService skf2020sc003SharedService;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private SkfMailUtils skfMailUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	// カンマ区切りフォーマット
	NumberFormat nfNum = NumberFormat.getNumberInstance();

	/**
	 * サービス処理を行う。
	 * 
	 * @param updDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public BaseDto index(Skf2020Sc003RevisionDto rvsDto) throws Exception {
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("修正依頼", CodeConstant.C001, rvsDto.getPageId());

		boolean validate = skf2020sc003SharedService.checkValidation(rvsDto);
		if (!validate) {
			// 添付資料だけはセッションから再取得の必要あり
			skf2020sc003SharedService.setAttachedFileList(rvsDto);
			return rvsDto;
		}

		Map<String, String> loginUserInfo = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		Map<String, String> errorMsg = new HashMap<String, String>();
		boolean result = skf2020sc003SharedService.saveApplInfo(CodeConstant.STATUS_SASHIMODOSHI, rvsDto, errorMsg);
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
					CodeConstant.NONE, applInfo.get("applShainNo"), CodeConstant.NONE, urlBase);
		} else {
			// エラーメッセージが空ではない場合
			if (NfwStringUtils.isNotEmpty(errorMsg.get("error"))) {
				ServiceHelper.addErrorResultMessage(rvsDto, null, errorMsg.get("error"));
			}
			throwBusinessExceptionIfErrors(rvsDto.getResultMessages());
			return rvsDto;
		}

		TransferPageInfo tpi = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC005);
		tpi.addResultMessage(MessageIdConstant.I_SKF_2030);
		rvsDto.setTransferPageInfo(tpi);

		return rvsDto;
	}

}
