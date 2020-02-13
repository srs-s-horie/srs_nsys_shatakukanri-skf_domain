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
import jp.co.c_nexco.skf.skf2020.domain.dto.skf2020sc003.Skf2020Sc003RemandDto;

/**
 * Skf2020Sc003 社宅入居希望等調書（アウトソース用）差戻し（否認）処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2020Sc003RemandService extends BaseServiceAbstract<Skf2020Sc003RemandDto> {

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
	public BaseDto index(Skf2020Sc003RemandDto rmdDto) throws Exception {
		// 操作ログ出力メソッドを呼び出す
		skfOperationLogUtils.setAccessLog("差戻し", CodeConstant.C001, rmdDto.getPageId());

		Map<String, String> loginUserInfo = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		Map<String, String> errorMsg = new HashMap<String, String>();

		boolean validate = skf2020sc003SharedService.checkValidation(rmdDto);
		if (!validate) {
			// 添付資料だけはセッションから再取得の必要あり
			skf2020sc003SharedService.setAttachedFileList(rmdDto);
			return rmdDto;
		}

		boolean result = skf2020sc003SharedService.saveApplInfo(CodeConstant.STATUS_HININ, rmdDto, errorMsg);
		if (result) {
			// 差戻し（否認）通知メール送信
			Map<String, String> applInfo = new HashMap<String, String>();
			applInfo.put("applNo", rmdDto.getApplNo());
			applInfo.put("applId", rmdDto.getApplId());
			applInfo.put("applShainNo", rmdDto.getShainNo());

			String commentNote = rmdDto.getCommentNote();

			skfMailUtils.sendApplTsuchiMail(CodeConstant.HININ_KANRYO_TSUCHI, applInfo, commentNote, CodeConstant.NONE,
					applInfo.get("applShainNo"), CodeConstant.NONE, CodeConstant.NONE);
			;
		} else if (NfwStringUtils.isNotEmpty(errorMsg.get("error"))) {
			ServiceHelper.addErrorResultMessage(rmdDto, null, errorMsg.get("error"));
			throwBusinessExceptionIfErrors(rmdDto.getResultMessages());
			return rmdDto;
		}

		TransferPageInfo tpi = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC005);
		tpi.addResultMessage(MessageIdConstant.I_SKF_2033);
		rmdDto.setTransferPageInfo(tpi);

		return rmdDto;
	}

}
