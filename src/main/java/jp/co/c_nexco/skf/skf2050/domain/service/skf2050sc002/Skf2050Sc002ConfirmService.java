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
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfMailUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2050Fc001BihinHenkyakuSinseiDataImport;
import jp.co.c_nexco.skf.skf2050.domain.dto.skf2050sc002.Skf2050Sc002ConfirmDto;

/**
 * Skf2050Sc002 備品返却確認（アウトソース用)確認処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2050Sc002ConfirmService extends BaseServiceAbstract<Skf2050Sc002ConfirmDto> {

	// 会社コード
	private String companyCd = CodeConstant.C001;

	private final String COMMENT_LABEL = "申請者へのコメント";

	@Autowired
	private Skf2050Sc002SharedService skf2050Sc002SharedService;

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfMailUtils skfMailUtils;
	@Autowired
	private Skf2050Fc001BihinHenkyakuSinseiDataImport skf2050Fc001BihinHenkyakuSinseiDataImport;

	@Autowired
	private SkfRollBackExpRepository skfRollBackExpRepository;

	/**
	 * サービス処理を行う。
	 * 
	 * @param confDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2050Sc002ConfirmDto index(Skf2050Sc002ConfirmDto confDto) throws Exception {
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("確認", companyCd, FunctionIdConstant.SKF2050_SC002);

		// 入力チェック
		boolean validateRes = validateReason(confDto);
		if (!validateRes) {
			throwBusinessExceptionIfErrors(confDto.getResultMessages());
			return confDto;
		}

		// 申請情報履歴更新処理。次のステータスは搬出待ち
		boolean result = skf2050Sc002SharedService.saveApplHistory(CodeConstant.STATUS_HANSYUTSU_MACHI, confDto);
		if (!result) {
			throwBusinessExceptionIfErrors(confDto.getResultMessages());
			return confDto;
		}

		// メールを送信する
		Map<String, String> applInfo = new HashMap<String, String>();
		applInfo.put(CodeConstant.KEY_APPL_ID, confDto.getApplId());
		applInfo.put(CodeConstant.KEY_APPL_NO, confDto.getApplNo());
		applInfo.put(CodeConstant.KEY_APPL_STATUS, CodeConstant.STATUS_HANSYUTSU_MACHI);
		applInfo.put(CodeConstant.KEY_APPL_SHAIN_NO, confDto.getShainNo());

		String baseUrl = "skf/" + FunctionIdConstant.SKF2010_SC005 + "/init";
		skfMailUtils.sendApplTsuchiMail(CodeConstant.HANSYUTSU_MACHI_TSUCHI, applInfo, confDto.getCommentNote(), null,
				confDto.getShainNo(), null, baseUrl);

		// 社宅管理データ連携処理実行
		String shainNo = confDto.getShainNo();
		String applNo = confDto.getApplNo();
		String status = CodeConstant.STATUS_HANSYUTSU_MACHI;
		String pageId = FunctionIdConstant.SKF2050_SC002;
		List<String> resultBatch = skf2050Sc002SharedService.doShatakuRenkei(menuScopeSessionBean, shainNo, applNo,
				status, pageId);
		if (resultBatch != null) {
			skf2050Fc001BihinHenkyakuSinseiDataImport.addResultMessageForDataLinkage(confDto, resultBatch);
			throwBusinessExceptionIfErrors(confDto.getResultMessages());
			skfRollBackExpRepository.rollBack();
			return confDto;
		}

		// フォームデータを設定
		confDto.setPrePageId(FunctionIdConstant.SKF1010_SC001); // TOPページを指定
		BaseForm form = new BaseForm();
		CopyUtils.copyProperties(form, confDto);
		FormHelper.setFormBean(FunctionIdConstant.SKF2010_SC005, form);

		// 前の画面に遷移する
		TransferPageInfo tpi = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC005);
		tpi.addResultMessage(MessageIdConstant.I_SKF_2047);
		confDto.setTransferPageInfo(tpi);

		return confDto;
	}

	/**
	 * コメントの入力チェックを行います
	 * 
	 * @param confDto
	 * @return boolean
	 * @throws Exception
	 */
	private boolean validateReason(Skf2050Sc002ConfirmDto confDto) throws Exception {
		// コメントの入力チェック
		String commentNote = confDto.getCommentNote();
		// コメントの最大入力桁数チェック
		// 値が4000バイトを超えた際はエラー
		int commentMaxLength = Integer.parseInt(PropertyUtils.getValue("skf.common.comment_max_length"));
		if (NfwStringUtils.isNotEmpty(commentNote) && commentMaxLength < commentNote.getBytes("UTF-8").length) {
			ServiceHelper.addErrorResultMessage(confDto, new String[] { "commentNote" }, MessageIdConstant.E_SKF_1049,
					COMMENT_LABEL, commentMaxLength / 2);
			return false;
		}
		return true;
	}

}
