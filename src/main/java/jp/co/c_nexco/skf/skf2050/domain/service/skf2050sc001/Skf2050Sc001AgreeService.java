/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2050.domain.service.skf2050sc001;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.common.utils.PropertyUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2050.domain.dto.skf2050sc001.Skf2050Sc001AgreeDto;

/**
 * Skf2050Sc001 備品返却確認（申請者用)同意する処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2050Sc001AgreeService extends SkfServiceAbstract<Skf2050Sc001AgreeDto> {

	private final String SESSION_DAY_LABEL = "備品返却立会日";
	private final String COMMENT_LABEL = "承認者へのコメント";

	@Autowired
	private Skf2050Sc001SharedService skf2050Sc001SharedService;

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	/**
	 * サービス処理を行う。
	 * 
	 * @param agreeDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2050Sc001AgreeDto index(Skf2050Sc001AgreeDto agreeDto) throws Exception {
		// タイトル設定
		agreeDto.setPageTitleKey(MessageIdConstant.SKF2050_SC001_TITLE);
		// 操作ログ出力
		skfOperationLogUtils.setAccessLog("同意する", CodeConstant.C001, FunctionIdConstant.SKF2050_SC001);

		// 入力チェック
		boolean validateResult = checkValidate(agreeDto);
		if (!validateResult) {
			throwBusinessExceptionIfErrors(agreeDto.getResultMessages());
			return agreeDto;
		}

		String newApplStatus = CodeConstant.STATUS_DOI_ZUMI;
		skf2050Sc001SharedService.setMenuScopeSessionBean(menuScopeSessionBean);

		// データを更新する
		if (!skf2050Sc001SharedService.saveBihinHenkyakuInfo(newApplStatus, agreeDto, menuScopeSessionBean)) {
			throwBusinessExceptionIfErrors(agreeDto.getResultMessages());
			return agreeDto;
		}

		// 前の画面に遷移する
		TransferPageInfo tpi = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC003);
		tpi.addResultMessage(MessageIdConstant.I_SKF_2047);
		agreeDto.setTransferPageInfo(tpi);

		return agreeDto;
	}

	private boolean checkValidate(Skf2050Sc001AgreeDto dto) throws Exception {
		boolean result = true;
		// 立会日チェック（必須チェック）
		String sessionDay = dto.getSessionDay();
		if (sessionDay == null || CheckUtils.isEmpty(sessionDay)) {
			ServiceHelper.addErrorResultMessage(dto, new String[] { "sessionDay" }, MessageIdConstant.E_SKF_1048,
					SESSION_DAY_LABEL);
			result = false;
		}

		// コメントの入力チェック
		String commentNote = dto.getCommentNote();
		// コメントの最大入力桁数チェック
		// 値が4000バイトを超えた際はエラー
		int commentMaxLength = Integer.parseInt(PropertyUtils.getValue("skf.common.comment_max_length"));
		if (NfwStringUtils.isNotEmpty(commentNote) && commentMaxLength < commentNote.getBytes("UTF-8").length) {
			ServiceHelper.addErrorResultMessage(dto, new String[] { "commentNote" }, MessageIdConstant.E_SKF_1049,
					COMMENT_LABEL, commentMaxLength / 2);
			result = false;
		}
		return result;

	}

}
