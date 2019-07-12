/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc004;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc003.Skf2010Sc003CancelDto;

/**
 * Skf2010Sc003 申請状況一覧取下げ処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc004CancelService extends BaseServiceAbstract<Skf2010Sc003CancelDto> {

	@Autowired
	private Skf2010Sc004SharedService skf2010Sc004SharedService;

	@Value("${skf2010.skf2010_sc005.search_max_count}")
	private String searchMaxCount;
	@Value("${skf.common.validate_error}")
	private String validationErrorCode;

	/**
	 * サービス処理を行う。
	 * 
	 * @param cancelDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public BaseDto index(Skf2010Sc003CancelDto cancelDto) throws Exception {

		cancelDto.setPageTitleKey(MessageIdConstant.SKF2010_SC003_TITLE);

		String applNo = cancelDto.getApplNo();
		String applId = cancelDto.getApplId();

		// 「申請書類履歴テーブル」よりステータスを更新
		boolean result = skf2010Sc004SharedService.updateApplHistoryCancel(applNo, applId);
		if (!result) {
			ServiceHelper.addErrorResultMessage(cancelDto, null, MessageIdConstant.E_SKF_1075);
			throwBusinessExceptionIfErrors(cancelDto.getResultMessages());
		}

		// TODO 社宅管理データ連携処理実行

		// 遷移先画面指定
		String nextPageId = "";
		switch (applId) {
		case FunctionIdConstant.R0100:
			// 社宅希望申請等調書
			nextPageId = FunctionIdConstant.SKF2020_SC002;
			break;
		case FunctionIdConstant.R0103:
			// 退居（自動車の保管場所返還）届
			nextPageId = FunctionIdConstant.SKF2040_SC001;
			break;
		}

		// 完了メッセージ表示
		ServiceHelper.addResultMessage(cancelDto, MessageIdConstant.I_SKF_2047);

		TransferPageInfo nextPage = TransferPageInfo.nextPage(nextPageId, "init");
		nextPage.addResultMessage(MessageIdConstant.I_SKF_2047);
		cancelDto.setTransferPageInfo(nextPage);

		return cancelDto;
	}

}
