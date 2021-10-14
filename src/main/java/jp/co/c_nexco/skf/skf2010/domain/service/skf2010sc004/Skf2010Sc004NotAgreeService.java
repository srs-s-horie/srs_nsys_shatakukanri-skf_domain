/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc004;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetApplHistoryInfoByParameterExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchi;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReport;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfRollBack.SkfRollBackExpRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfCommentUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfMailUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.SkfBatchBusinessLogicUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc004.Skf2010Sc004NotAgreeDto;

/**
 * Skf2010Sc004 申請内容表示/引戻し同意しない処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc004NotAgreeService extends SkfServiceAbstract<Skf2010Sc004NotAgreeDto> {

	@Autowired
	private Skf2010Sc004SharedService skf2010Sc004SharedService;

	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private SkfMailUtils skfMailUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfBatchBusinessLogicUtils skfBatchBusinessLogicUtils;
	@Autowired
	private SkfRollBackExpRepository skfRollBackExpRepository;
	@Autowired
	private SkfCommentUtils skfCommentUtils;

	@Value("${skf.common.validate_error}")
	private String validationErrorCode;

	// 基準会社コード
	private String companyCd = CodeConstant.C001;

	/**
	 * サービス処理を行う。
	 * 
	 * @param notAgreeDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2010Sc004NotAgreeDto index(Skf2010Sc004NotAgreeDto notAgreeDto) throws Exception {
		// 操作ログの出力
		skfOperationLogUtils.setAccessLog("同意しない", companyCd, FunctionIdConstant.SKF2010_SC004);

		// タイトル設定
		notAgreeDto.setPageTitleKey(MessageIdConstant.SKF2010_SC004_TITLE);
		// 初期化処理
		init(notAgreeDto);

		// 申請書類ID
		String applId = notAgreeDto.getApplId();
		// 申請書番号
		String applNo = notAgreeDto.getApplNo();

		// 承認者へのコメント欄の入力チェック
		boolean validateResult = true;
		validateResult = validateReason(notAgreeDto);
		if (!validateResult) {
			return notAgreeDto;
		}

		// 申請情報の取得を行う
		getApplInfo(notAgreeDto);

		Map<String, String> loginUserInfo = skfLoginUserInfoUtils
				.getSkfLoginUserInfoFromAlterLogin(menuScopeSessionBean);
		String shainNo = loginUserInfo.get("shainNo");

		String commentName = loginUserInfo.get("userName");
		String commentNote = notAgreeDto.getCommentNote();
		Date commentUpdateDate = new Date();

		boolean updRes = updateApplHistoryNotAgree(notAgreeDto, shainNo, commentUpdateDate, commentName, commentNote);
		if (!updRes) {
			// エラーが出た場合処理を中断する
			throwBusinessExceptionIfErrors(notAgreeDto.getResultMessages());
		}

		Map<String, String> applInfo = new HashMap<String, String>();
		applInfo.put("applShainNo", shainNo);
		applInfo.put("applNo", applNo);
		applInfo.put("applId", applId);

		String urlBase = "skf/Skf2010Sc005/init?SKF2010_SC005&menuflg=1&tokenCheck=0";

		// 送信メールにコメントが表示されないようになっている（メール本文に表記箇所が無い）
		skfMailUtils.sendApplTsuchiMail(CodeConstant.HUDOI_KANRYO_TSUCHI, applInfo, commentNote, CodeConstant.NONE,
				null, CodeConstant.NONE, urlBase);

		// 社宅管理データ連携処理実行
		Skf2010Sc004GetApplHistoryInfoByParameterExp tApplHistoryData = new Skf2010Sc004GetApplHistoryInfoByParameterExp();
		tApplHistoryData = skf2010Sc004SharedService.getApplHistoryInfo(applNo);
		if (tApplHistoryData == null) {
			ServiceHelper.addErrorResultMessage(notAgreeDto, null, MessageIdConstant.E_SKF_1073);
			throwBusinessExceptionIfErrors(notAgreeDto.getResultMessages());
			return notAgreeDto;
		}
		String afterApplStatus = tApplHistoryData.getApplStatus();
		List<String> resultBatch = new ArrayList<String>();
		resultBatch = skf2010Sc004SharedService.doShatakuRenkei(menuScopeSessionBean, applNo, CodeConstant.NONE,
				afterApplStatus, applId, FunctionIdConstant.SKF2010_SC004, shainNo);
		menuScopeSessionBean.remove(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2010SC004);
		if (resultBatch != null) {
			skfBatchBusinessLogicUtils.addResultMessageForDataLinkage(notAgreeDto, resultBatch);
			skfRollBackExpRepository.rollBack();
			throwBusinessExceptionIfErrors(notAgreeDto.getResultMessages());
			return notAgreeDto;
		}

		// ページ遷移先は「申請状況一覧」
		TransferPageInfo tpi = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC003);
		tpi.addResultMessage(MessageIdConstant.I_SKF_2047);
		notAgreeDto.setTransferPageInfo(tpi);

		return notAgreeDto;
	}

	private boolean updateApplHistoryNotAgree(Skf2010Sc004NotAgreeDto dto, String shainNo, Date commentUpdateDate,
			String commentName, String commentNote) {
		String applNo = dto.getApplNo();

		// 保存日時を取得
		Date applDate = new Date();
		String applStatus = CodeConstant.STATUS_DOI_SHINAI;
		String applTacFlg = CodeConstant.NONE;
		Map<String, String> errorMsg = new HashMap<String, String>();

		// 排他処理用最終更新日取得
		Date lastUpdateDate = dto.getLastUpdateDate(skf2010Sc004SharedService.KEY_LAST_UPDATE_DATE_HISTORY);

		// 「申請書類履歴テーブル」よりステータスを更新
		boolean agreeStatusRes = skf2010Sc004SharedService.updateApplHistoryAgreeStatus(applNo, applDate, applTacFlg,
				applStatus, CodeConstant.NONE, CodeConstant.NONE, CodeConstant.NONE, lastUpdateDate, errorMsg);
		if (!agreeStatusRes) {
			ServiceHelper.addErrorResultMessage(dto, null, errorMsg.get("error"));
			return false;
		}

		// コメントの更新
		boolean commentErrorMessage = skfCommentUtils.insertComment(CodeConstant.C001, applNo, applStatus, commentNote,
				errorMsg);
		if (!commentErrorMessage) {
			return false;
		}

		return true;
	}

	private void getApplInfo(Skf2010Sc004NotAgreeDto agreeDto) {
		String applId = agreeDto.getApplId();
		String applNo = agreeDto.getApplNo();

		if (applId.equals(FunctionIdConstant.R0100)) {
			Skf2020TNyukyoChoshoTsuchi nyukyoChoshoTsuchi = new Skf2020TNyukyoChoshoTsuchi();
			nyukyoChoshoTsuchi = skf2010Sc004SharedService.getNyukyoChoshoTsuchiInfo(applNo);
			if (nyukyoChoshoTsuchi == null) {
				ServiceHelper.addErrorResultMessage(agreeDto, null, MessageIdConstant.E_SKF_1078, "初期表示中に");
				return;
			}

			// 入居予定日
			agreeDto.setNyukyoYoteiDate(nyukyoChoshoTsuchi.getNyukyoYoteiDate());
			// 駐車場使用開始日
			agreeDto.setParkingUserDate(nyukyoChoshoTsuchi.getParkingUseDate());
			// 駐車場使用開始日2
			agreeDto.setParkingUserDate2(nyukyoChoshoTsuchi.getParkingUseDate2());

		} else if (applId.equals(FunctionIdConstant.R0103)) {
			Skf2040TTaikyoReport taikyoReport = new Skf2040TTaikyoReport();
			taikyoReport = skf2010Sc004SharedService.getTaikkyoReportInfo(applNo);
			if (taikyoReport == null) {
				ServiceHelper.addErrorResultMessage(agreeDto, null, MessageIdConstant.E_SKF_1078, "初期表示中に");
				return;
			}
			// 退居日
			agreeDto.setTaikyoDate(taikyoReport.getTaikyoDate());
			// 駐車場返還日
			agreeDto.setParkingHenkanDate(taikyoReport.getParkingHenkanDate());

		}
	}

	/**
	 * コメント入力チェック。「同意しない」の場合は必須
	 * 
	 * @param agreeDto
	 * @return
	 */
	private boolean validateReason(Skf2010Sc004NotAgreeDto agreeDto) {
		String reasonText = agreeDto.getCommentNote();
		if (reasonText == null || CheckUtils.isEmpty(reasonText)) {
			ServiceHelper.addErrorResultMessage(agreeDto, new String[] { "commentNote" }, MessageIdConstant.E_SKF_1048,
					"承認者へのコメント");
			return false;
		}
		int byteCnt = reasonText.getBytes(Charset.forName("UTF-8")).length;
		if (byteCnt >= 4000) {
			ServiceHelper.addErrorResultMessage(agreeDto, new String[] { "commentNote" }, MessageIdConstant.E_SKF_1049,
					"承認者へのコメント", "2000");
			return false;
		}
		return true;
	}

	/**
	 * 初期化処理
	 * 
	 * @param agreeDto
	 */
	private void init(Skf2010Sc004NotAgreeDto notAgreeDto) {
		// エラー背景色初期化
		notAgreeDto.setTaikyobiErr("");
		notAgreeDto.setHenkanbiErr("");
		notAgreeDto.setNyukyobiErr("");
		notAgreeDto.setShiyobiErr("");
		notAgreeDto.setShiyobi2Err("");

		return;
	}

}
