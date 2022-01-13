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
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
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
import jp.co.c_nexco.skf.skf2050.domain.dto.skf2050sc002.Skf2050Sc002AgreeDto;

/**
 * Skf2050Sc002 備品返却確認（アウトソース用)承認処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2050Sc002AgreeService extends SkfServiceAbstract<Skf2050Sc002AgreeDto> {

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
	 * @param agreeDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2050Sc002AgreeDto index(Skf2050Sc002AgreeDto agreeDto) throws Exception {
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("承認", companyCd, FunctionIdConstant.SKF2050_SC002);

		// 入力チェック
		boolean validateRes = validateReason(agreeDto);
		if (!validateRes) {
			throwBusinessExceptionIfErrors(agreeDto.getResultMessages());
			return agreeDto;
		}

		// 次のステータスを設定
		String newApplStatus = CodeConstant.STATUS_SHONIN1;
		if (CheckUtils.isEqual(agreeDto.getApplStatus(), CodeConstant.STATUS_SHONIN1)) {
			// 現在のステータスが「31：承認１」だった場合は次のステータスを「40：承認済」に設定する
			newApplStatus = CodeConstant.STATUS_SHONIN_ZUMI;
		}

		boolean result = skf2050Sc002SharedService.saveApplHistory(newApplStatus, agreeDto);
		if (!result) {
			throwBusinessExceptionIfErrors(agreeDto.getResultMessages());
			return agreeDto;
		}

		// 社宅管理データ連携処理実行
		String shainNo = agreeDto.getShainNo();
		String applNo = agreeDto.getApplNo();
		String status = newApplStatus;
		String pageId = FunctionIdConstant.SKF2050_SC002;
		List<String> resultBatch = skf2050Sc002SharedService.doShatakuRenkei(menuScopeSessionBean, shainNo, applNo,
				status, pageId);
		if (resultBatch != null) {
			skf2050Fc001BihinHenkyakuSinseiDataImport.addResultMessageForDataLinkage(agreeDto, resultBatch);
			skfRollBackExpRepository.rollBack();
			throwBusinessExceptionIfErrors(agreeDto.getResultMessages());
			return agreeDto;
		}

		// 新ステータスが「40：承認済」の時のみメールを送信する
		if (CheckUtils.isEqual(newApplStatus, CodeConstant.STATUS_SHONIN_ZUMI)) {
			Map<String, String> applInfo = new HashMap<String, String>();
			applInfo.put(CodeConstant.KEY_APPL_ID, agreeDto.getApplId());
			applInfo.put(CodeConstant.KEY_APPL_NO, agreeDto.getApplNo());
			applInfo.put(CodeConstant.KEY_APPL_STATUS, CodeConstant.STATUS_HANSYUTSU_MACHI);
			applInfo.put(CodeConstant.KEY_APPL_SHAIN_NO, agreeDto.getShainNo());

			String baseUrl = "skf/" + FunctionIdConstant.SKF2010_SC003 + "/init";
			skfMailUtils.sendApplTsuchiMail(CodeConstant.SHONIN_KANRYO_TSUCHI, applInfo, agreeDto.getCommentNote(),
					null, agreeDto.getShainNo(), null, baseUrl);
		}

		// フォームデータを設定
		agreeDto.setPrePageId(FunctionIdConstant.SKF1010_SC001); // TOPページを指定
		BaseForm form = new BaseForm();
		CopyUtils.copyProperties(form, agreeDto);
		FormHelper.setFormBean(FunctionIdConstant.SKF2010_SC005, form);
		
		//画面遷移前にデータの初期化を行う
		FormHelper.removeFormBean(FunctionIdConstant.SKF2050_SC002);

		// 前の画面に遷移する
		TransferPageInfo tpi = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC005);
		tpi.addResultMessage(MessageIdConstant.I_SKF_2047);
		agreeDto.setTransferPageInfo(tpi);

		return agreeDto;
	}

	/**
	 * コメントの入力チェックを行います
	 * 
	 * @param agreeDto
	 * @return boolean
	 * @throws Exception
	 */
	private boolean validateReason(Skf2050Sc002AgreeDto agreeDto) throws Exception {
		// コメントの入力チェック
		String commentNote = agreeDto.getCommentNote();
		// コメントの最大入力桁数チェック
		// 値が4000バイトを超えた際はエラー
		int commentMaxLength = Integer.parseInt(PropertyUtils.getValue("skf.common.comment_max_length"));
		if (NfwStringUtils.isNotEmpty(commentNote) && commentMaxLength < commentNote.getBytes("UTF-8").length) {
			ServiceHelper.addErrorResultMessage(agreeDto, new String[] { "commentNote" }, MessageIdConstant.E_SKF_1049,
					COMMENT_LABEL, commentMaxLength / 2);
			return false;
		}
		return true;
	}

}
