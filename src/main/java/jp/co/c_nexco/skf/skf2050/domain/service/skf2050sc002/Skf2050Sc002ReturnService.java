/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2050.domain.service.skf2050sc002;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfRollBack.SkfRollBackExpRepository;
import jp.co.c_nexco.nfw.common.utils.CopyUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.common.utils.PropertyUtils;
import jp.co.c_nexco.nfw.webcore.app.BaseForm;
import jp.co.c_nexco.nfw.webcore.app.FormHelper;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfMailUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2050Fc001BihinHenkyakuSinseiDataImport;
import jp.co.c_nexco.skf.skf2050.domain.dto.skf2050sc002.Skf2050Sc002ReturnDto;

/**
 * Skf2050Sc002 備品返却確認（アウトソース用)修正依頼処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2050Sc002ReturnService extends SkfServiceAbstract<Skf2050Sc002ReturnDto> {

	private String companyCd = CodeConstant.C001;
	private final String COMMENT_LABEL = "申請者へのコメント";

	@Autowired
	private Skf2050Sc002SharedService skf2050Sc002SharedService;
	@Autowired
	private SkfRollBackExpRepository skfRollBackExpRepository;
	@Autowired
	private Skf2050Fc001BihinHenkyakuSinseiDataImport skf2050Fc001BihinHenkyakuSinseiDataImport;

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfMailUtils skfMailUtils;

	/**
	 * サービス処理を行う。
	 * 
	 * @param returnDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2050Sc002ReturnDto index(Skf2050Sc002ReturnDto returnDto) throws Exception {
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("修正依頼", companyCd, FunctionIdConstant.SKF2050_SC002);

		returnDto.getApplStatus();

		// コメントの入力チェック
		boolean validateRes = validateReason(returnDto);
		if (!validateRes) {
			throwBusinessExceptionIfErrors(returnDto.getResultMessages());
			return returnDto;
		}

		boolean result = skf2050Sc002SharedService.saveApplHistory(CodeConstant.STATUS_HANSYUTSU_MACHI, returnDto);
		if (!result) {
			throwBusinessExceptionIfErrors(returnDto.getResultMessages());
			return returnDto;
		}

		// メールを送信する
		Map<String, String> applInfo = new HashMap<String, String>();
		applInfo.put(CodeConstant.KEY_APPL_ID, returnDto.getApplId());
		applInfo.put(CodeConstant.KEY_APPL_NO, returnDto.getApplNo());
		applInfo.put(CodeConstant.KEY_APPL_STATUS, CodeConstant.STATUS_HANSYUTSU_MACHI);
		applInfo.put(CodeConstant.KEY_APPL_SHAIN_NO, returnDto.getShainNo());

		String baseUrl = "skf/" + FunctionIdConstant.SKF2010_SC005 + "/init";
		skfMailUtils.sendApplTsuchiMail(CodeConstant.HANSYUTSU_MACHI_TSUCHI, applInfo, returnDto.getCommentNote(), null,
				returnDto.getShainNo(), null, baseUrl);

		// 社宅管理データ連携処理実行
		String shainNo = returnDto.getShainNo();
		String applNo = returnDto.getApplNo();
		String status = CodeConstant.STATUS_SASHIMODOSHI;
		String pageId = FunctionIdConstant.SKF2050_SC002;
		List<String> resultBatch = skf2050Sc002SharedService.doShatakuRenkei(menuScopeSessionBean, shainNo, applNo,
				status, pageId);
		if (resultBatch != null) {
			skf2050Fc001BihinHenkyakuSinseiDataImport.addResultMessageForDataLinkage(returnDto, resultBatch);
			skfRollBackExpRepository.rollBack();
			throwBusinessExceptionIfErrors(returnDto.getResultMessages());
			return returnDto;
		}

		// フォームデータを設定
		returnDto.setPrePageId(FunctionIdConstant.SKF1010_SC001); // TOPページを指定
		BaseForm form = new BaseForm();
		CopyUtils.copyProperties(form, returnDto);
		FormHelper.setFormBean(FunctionIdConstant.SKF2010_SC005, form);

		// 前の画面に遷移する
		TransferPageInfo tpi = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC005);
		tpi.addResultMessage(MessageIdConstant.I_SKF_2047);
		returnDto.setTransferPageInfo(tpi);

		return returnDto;
	}

	/**
	 * コメントの入力チェックを行います
	 * 
	 * @param returnDto
	 * @return boolean
	 * @throws Exception
	 */
	private boolean validateReason(Skf2050Sc002ReturnDto returnDto) throws Exception {
		// コメントの入力チェック
		String commentNote = returnDto.getCommentNote();
		if (NfwStringUtils.isEmpty(commentNote)) {
			ServiceHelper.addErrorResultMessage(returnDto, new String[] { "commentNote" }, MessageIdConstant.E_SKF_1048,
					COMMENT_LABEL);
			return false;
		}
		// コメントの最大入力桁数チェック
		// 値が4000バイトを超えた際はエラー
		int commentMaxLength = Integer.parseInt(PropertyUtils.getValue("skf.common.comment_max_length"));
		if (NfwStringUtils.isNotEmpty(commentNote) && commentMaxLength < commentNote.getBytes("UTF-8").length) {
			ServiceHelper.addErrorResultMessage(returnDto, new String[] { "commentNote" }, MessageIdConstant.E_SKF_1049,
					COMMENT_LABEL, commentMaxLength / 2);
			return false;
		}
		return true;
	}

}
