/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2030.domain.service.skf2030sc001;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc001.Skf2030Sc001GetApplHistoryInfoInDescendingOrderExp;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.common.utils.PropertyUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2030.domain.dto.skf2030sc001.Skf2030Sc001ApplyDto;

/**
 * Skf2030Sc001 備品希望申請（申請者用)搬入完了処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2030Sc001ApplyService extends BaseServiceAbstract<Skf2030Sc001ApplyDto> {

	@Autowired
	private Skf2030Sc001SharedService skf2030Sc001SharedService;

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	private final String MSG_MISAKUSEI_BIHIN = "該当する社宅入居希望等調書がないか申請できない状態で備品希望申請され";
	private final String MSG_SAISAKUSEI_BIHIN = "社宅入居希望等調書の申請を確認";

	// 排他処理用最終更新日付
	public static final String APPL_HISTORY_KEY_LAST_UPDATE_DATE = "skf2010_t_appl_history_UpdateDate";
	public static final String BIHIN_KIBO_SHINSEI_KEY_LAST_UPDATE_DATE = "skf2030_t_bihin_kibo_shinsei_UpdateDate";
	public static final String BIHIN_KEY_LAST_UPDATE_DATE = "skf2030_t_bihin_UpdateDate";

	/**
	 * サービス処理を行う。
	 * 
	 * @param applyDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2030Sc001ApplyDto index(Skf2030Sc001ApplyDto applyDto) throws Exception {
		// 操作ログ出力
		skfOperationLogUtils.setAccessLog("入力内容をクリア処理開始", CodeConstant.C001, FunctionIdConstant.SKF2030_SC001);

		// タイトル設定
		applyDto.setPageTitleKey(MessageIdConstant.SKF2030_SC001_TITLE);

		// 申請情報設定
		Map<String, String> applInfo = new HashMap<String, String>();
		applInfo.put("status", applyDto.getSendApplStatus());
		applInfo.put("applNo", applyDto.getApplNo());
		applInfo.put("applId", applyDto.getApplId());
		applInfo.put("shainNo", applyDto.getHdnShainNo());

		// ログインユーザー情報取得
		Map<String, String> loginUserInfo = skfLoginUserInfoUtils.getSkfLoginUserInfo();

		// 申請前に申請可能か判定を行う
		// 該当社員の社宅入居希望等調書の最新申請書類のステータスが、22:同意済、31～32:承認中、40:承認済の場合 かつ
		// １．該当社員の備品希望申請が存在しない場合 → 申請書類履歴テーブルを一時保存で新規作成
		// ２．該当社員の備品希望申請が存在する場合かつ申請書類のステータスが、99:差戻し（否認）の場合 →
		// 申請書類履歴テーブルのステータスを一時保存にして画面遷移
		List<Skf2030Sc001GetApplHistoryInfoInDescendingOrderExp> applHistoryInfoList = new ArrayList<Skf2030Sc001GetApplHistoryInfoInDescendingOrderExp>();
		applHistoryInfoList = skf2030Sc001SharedService.getApplHistoryInfoInDescendingOrder(applInfo.get("shainNo"),
				FunctionIdConstant.R0100);
		if (applHistoryInfoList != null && applHistoryInfoList.size() > 0) {
			Skf2030Sc001GetApplHistoryInfoInDescendingOrderExp applHistoryInfo = applHistoryInfoList.get(0);
			switch (applHistoryInfo.getApplStatus()) {
			case CodeConstant.STATUS_DOI_ZUMI:
			case CodeConstant.STATUS_SHONIN1:
			case CodeConstant.STATUS_SHONIN2:
			case CodeConstant.STATUS_SHONIN_ZUMI:
				String nyukyoKiboApplNo = applHistoryInfo.getApplNo();

				if (skf2030Sc001SharedService.checkSKSTeijiStatus(loginUserInfo.get("shainNo"),
						FunctionIdConstant.R0104, nyukyoKiboApplNo)) {
					ServiceHelper.addResultMessage(applyDto, MessageIdConstant.I_SKF_1005, MSG_MISAKUSEI_BIHIN,
							MSG_SAISAKUSEI_BIHIN);
					return applyDto;
				}

				break;
			default:
				ServiceHelper.addResultMessage(applyDto, MessageIdConstant.I_SKF_1005, MSG_MISAKUSEI_BIHIN,
						MSG_SAISAKUSEI_BIHIN);
				return applyDto;
			}
		}

		// 入力チェック
		if (!checkDispInfo(applyDto)) {
			throwBusinessExceptionIfErrors(applyDto.getResultMessages());
		}

		// 更新処理
		if (skf2030Sc001SharedService.updateDispInfo(applInfo, applyDto)) {
			ServiceHelper.addErrorResultMessage(applyDto, null, MessageIdConstant.E_SKF_1075);
			throwBusinessExceptionIfErrors(applyDto.getResultMessages());
			return applyDto;
		}

		// 申請状況一覧に画面遷移
		TransferPageInfo tpi = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC003);
		tpi.addResultMessage(MessageIdConstant.I_SKF_2047);
		applyDto.setTransferPageInfo(tpi);

		return applyDto;
	}

	/**
	 * 入力チェックを行う
	 * 
	 * @param applyDto
	 * @return
	 * @throws Exception
	 */
	private boolean checkDispInfo(Skf2030Sc001ApplyDto applyDto) throws Exception {
		boolean result = true;
		// 備品希望の「申請する」が選択されている場合は必須チェック必要
		boolean bihinCheckFlag = false;
		if (NfwStringUtils.isNotEmpty(applyDto.getBihinCheckFlag())) {
			bihinCheckFlag = Boolean.parseBoolean(applyDto.getBihinCheckFlag());
		}

		List<String> validateFlag = new ArrayList<String>();
		if (bihinCheckFlag) {
			// tel
			validateFlag.add("tel");
			// 連絡先
			validateFlag.add("renrakuSaki");
			// 搬入希望日
			validateFlag.add("sessionDay");
			// 搬入希望時刻
			validateFlag.add("sessionTime");
		}

		List<String> errorTarget = new ArrayList<String>();
		// 必須入力確認
		result = checkControlEmpty(applyDto, validateFlag, errorTarget);
		// バイト数確認
		result = checkByteCount(applyDto, validateFlag, errorTarget, result);
		// 日付確認
		result = checkDate(applyDto, validateFlag, errorTarget, result);

		return result;
	}

	/**
	 * 日付チェックを行う
	 * 
	 * @param applyDto
	 * @param validateFlag
	 * @param errorTarget
	 * @param result
	 * @return
	 */
	private boolean checkDate(Skf2030Sc001ApplyDto applyDto, List<String> validateFlag, List<String> errorTarget,
			boolean result) {
		if (!result) {
			return result;
		}
		// 搬入希望日
		if (!applyDto.isBihinReadOnly() && validateFlag.indexOf("sessionDay") >= 0) {
			if (!SkfCheckUtils.isSkfDateFormat(applyDto.getSessionDay().trim(), CheckUtils.DateFormatType.YYYYMMDD)) {
				errorTarget.add("sessionDay");
				ServiceHelper.addErrorResultMessage(applyDto, new String[] { "comment" }, MessageIdConstant.E_SKF_1055,
						"搬入希望日");
				// ServiceHelper.addErrorResultMessage(applyDto,
				// errorTarget.toArray(new String[errorTarget.size()]),
				// MessageIdConstant.E_SKF_1055, "搬入希望日");
				result = false;
			}
		}
		// 搬入完了日
		if (applyDto.isBihinReadOnly()) {
			if (!SkfCheckUtils.isSkfDateFormat(applyDto.getCompletionDay(), CheckUtils.DateFormatType.YYYYMMDD)) {
				errorTarget.add("completionDay");
				ServiceHelper.addErrorResultMessage(applyDto, new String[] { "completionDay" },
						MessageIdConstant.E_SKF_1055, "搬入完了日");
				// ServiceHelper.addErrorResultMessage(applyDto,
				// errorTarget.toArray(new String[errorTarget.size()]),
				// MessageIdConstant.E_SKF_1055, "搬入完了日");
				result = false;
			}
		}
		return result;
	}

	/**
	 * バイト数チェックを行います
	 * 
	 * @param applyDto
	 * @param validateFlag
	 * @param errorTarget
	 * @param result
	 * @return
	 * @throws Exception
	 */
	private boolean checkByteCount(Skf2030Sc001ApplyDto applyDto, List<String> validateFlag, List<String> errorTarget,
			boolean result) throws Exception {
		if (applyDto.isStatus01Flag() && result) {
			int commentMaxCnt = Integer.parseInt(PropertyUtils.getValue("skf2030.skf2030_sc001.comment_max_count"));
			if (CheckUtils.isMoreThanByteSize(applyDto.getCommentNote(), commentMaxCnt)) {
				errorTarget.add("comment");
				ServiceHelper.addErrorResultMessage(applyDto, new String[] { "comment" }, MessageIdConstant.E_SKF_1049,
						"承認者へのコメント", commentMaxCnt / 2);
				// ServiceHelper.addErrorResultMessage(applyDto,
				// errorTarget.toArray(new String[errorTarget.size()]),
				// MessageIdConstant.E_SKF_1048, "勤務先のTEL");
				result = false;
			}
		}
		return result;
	}

	/**
	 * 必須入力チェックを行います
	 * 
	 * @param applyDto
	 * @param validateFlag
	 * @param errorTarget
	 * @return
	 */
	private boolean checkControlEmpty(Skf2030Sc001ApplyDto applyDto, List<String> validateFlag,
			List<String> errorTarget) {
		boolean result = true;
		// 勤務先のTEL
		if (!applyDto.isBihinReadOnly() && validateFlag.indexOf("tel") >= 0) {
			if (NfwStringUtils.isEmpty(applyDto.getTel())) {
				errorTarget.add("tel");
				ServiceHelper.addErrorResultMessage(applyDto, new String[] { "tel" }, MessageIdConstant.E_SKF_1048,
						"勤務先のTEL");
				// ServiceHelper.addErrorResultMessage(applyDto,
				// errorTarget.toArray(new String[errorTarget.size()]),
				// MessageIdConstant.E_SKF_1048, "勤務先のTEL");
				result = false;
			}
		}
		// 連絡先
		if (!applyDto.isBihinReadOnly() && validateFlag.indexOf("renrakuSaki") >= 0) {
			if (NfwStringUtils.isEmpty(applyDto.getRenrakuSaki())) {
				errorTarget.add("renrakuSaki");
				ServiceHelper.addErrorResultMessage(applyDto, new String[] { "renrakuSaki" },
						MessageIdConstant.E_SKF_1048, "連絡先");
				// ServiceHelper.addErrorResultMessage(applyDto,
				// errorTarget.toArray(new String[errorTarget.size()]),
				// MessageIdConstant.E_SKF_1048, "連絡先");
				result = false;
			}
		}
		// 搬入希望日
		if (!applyDto.isBihinReadOnly() && validateFlag.indexOf("sessionDay") >= 0) {
			if (NfwStringUtils.isEmpty(applyDto.getSessionDay())) {
				errorTarget.add("sessionDay");
				ServiceHelper.addErrorResultMessage(applyDto, new String[] { "sessionDay" },
						MessageIdConstant.E_SKF_1048, "搬入希望日");
				// ServiceHelper.addErrorResultMessage(applyDto,
				// errorTarget.toArray(new String[errorTarget.size()]),
				// MessageIdConstant.E_SKF_1048, "搬入希望日");
				result = false;
			}
		}
		// 搬入希望時刻
		// TODO 搬入希望時刻はドロップダウンなので不要？
		// if (!applyDto.isBihinReadOnly() &&
		// validateFlag.indexOf("sessionTime") >= 0) {
		// if (NfwStringUtils.isEmpty(applyDto.getSessionTime())) {
		// errorTarget.add("sessionTime");
		// ServiceHelper.addErrorResultMessage(applyDto, new String[] {
		// "sessionTime" },
		// MessageIdConstant.E_SKF_1048, "搬入希望時刻");
		// // ServiceHelper.addErrorResultMessage(applyDto,
		// // errorTarget.toArray(new String[errorTarget.size()]),
		// // MessageIdConstant.E_SKF_1048, "搬入希望時刻");
		// result = false;
		// }
		// }
		return result;
	}

}
