/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2050.domain.service.skf2050sc001;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.common.utils.PropertyUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfMailUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2050.domain.dto.skf2050sc001.Skf2050Sc001NotAgreeDto;

/**
 * Skf2050Sc001 備品返却確認（申請者用)同意しない処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2050Sc001NotAgreeService extends SkfServiceAbstract<Skf2050Sc001NotAgreeDto> {

	private final String COMMENT_LABEL = "承認者へのコメント";

	@Autowired
	private Skf2050Sc001SharedService skf2050Sc001SharedService;

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private SkfMailUtils skfMailUtils;

	/**
	 * サービス処理を行う。
	 * 
	 * @param notAgreeDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public BaseDto index(Skf2050Sc001NotAgreeDto notAgreeDto) throws Exception {
		// タイトル設定
		notAgreeDto.setPageTitleKey(MessageIdConstant.SKF2050_SC001_TITLE);
		// 操作ログ出力
		skfOperationLogUtils.setAccessLog("同意しない", CodeConstant.C001, FunctionIdConstant.SKF2050_SC001);

		// 入力チェック
		boolean validateResult = checkValidate(notAgreeDto);
		if (!validateResult) {
			throwBusinessExceptionIfErrors(notAgreeDto.getResultMessages());
			return notAgreeDto;
		}

		String newApplStatus = CodeConstant.STATUS_DOI_SHINAI;
		skf2050Sc001SharedService.setMenuScopeSessionBean(menuScopeSessionBean);
		if (!skf2050Sc001SharedService.saveBihinHenkyakuInfo(newApplStatus, notAgreeDto, menuScopeSessionBean)) {
			throwBusinessExceptionIfErrors(notAgreeDto.getResultMessages());
			return notAgreeDto;
		}

		Map<String, String> loginUserInfo = skfLoginUserInfoUtils
				.getSkfLoginUserInfoFromAlterLogin(menuScopeSessionBean);
		String applShainNo = loginUserInfo.get("shainNo");

		// メールを送信する
		Map<String, String> applInfo = new HashMap<String, String>();
		applInfo.put(CodeConstant.KEY_APPL_ID, notAgreeDto.getApplId());
		applInfo.put(CodeConstant.KEY_APPL_NO, notAgreeDto.getApplNo());
		applInfo.put(CodeConstant.KEY_APPL_STATUS, newApplStatus);
		applInfo.put(CodeConstant.KEY_APPL_SHAIN_NO, applShainNo);

		String urlBase = "/skf/Skf2010Sc005/init?SKF2010_SC005&menuflg=1&tokenCheck=0";
		skfMailUtils.sendApplTsuchiMail(CodeConstant.HUDOI_KANRYO_TSUCHI, applInfo, notAgreeDto.getCommentNote(), null,
				null, null, urlBase);

		// 前の画面に遷移する
		TransferPageInfo tpi = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC003);
		tpi.addResultMessage(MessageIdConstant.I_SKF_2047);
		notAgreeDto.setTransferPageInfo(tpi);

		return notAgreeDto;
	}

	/**
	 * 入力チェック
	 * 
	 * @param initDto
	 * @return
	 * @throws Exception
	 */
	private boolean checkValidate(Skf2050Sc001NotAgreeDto initDto) throws Exception {
		boolean result = true;

		// コメントの入力チェック
		String commentNote = initDto.getCommentNote();
		if (CheckUtils.isEmpty(commentNote)) {
			ServiceHelper.addErrorResultMessage(initDto, new String[] { "commentNote" }, MessageIdConstant.E_SKF_1048,
					COMMENT_LABEL);
			result = false;
		}
		// コメントの最大入力桁数チェック
		// 値が4000バイトを超えた際はエラー
		int commentMaxLength = Integer.parseInt(PropertyUtils.getValue("skf.common.comment_max_length"));
		if (NfwStringUtils.isNotEmpty(commentNote) && commentMaxLength < commentNote.getBytes("UTF-8").length) {
			ServiceHelper.addErrorResultMessage(initDto, new String[] { "commentNote" }, MessageIdConstant.E_SKF_1049,
					COMMENT_LABEL, commentMaxLength / 2);
			result = false;
		}
		return result;

	}

}
