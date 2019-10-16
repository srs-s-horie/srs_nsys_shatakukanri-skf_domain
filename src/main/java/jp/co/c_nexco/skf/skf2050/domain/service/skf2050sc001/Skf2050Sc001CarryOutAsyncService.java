/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2050.domain.service.skf2050sc001;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.common.utils.PropertyUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationGuideUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2050.domain.dto.skf2050sc001.Skf2050Sc001CarryOutAsyncDto;

/**
 * Skf2050Sc001 備品返却申請（申請者用)搬出完了処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2050Sc001CarryOutAsyncService extends AsyncBaseServiceAbstract<Skf2050Sc001CarryOutAsyncDto> {

	private final String COMPLETION_DAY_LABEL = "搬出完了日";
	private final String COMMENT_LABEL = "承認者へのコメント";
	private final String PREFIX_VERIFY_MSG = "搬出完了日が退居日より後ですが";
	private final String SUFFIX_VERIFY_MSG = "(退居日:";

	@Autowired
	private Skf2050Sc001SharedService skf2050Sc001SharedService;

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfOperationGuideUtils skfOperationGuideUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;

	/**
	 * サービス処理を行う。
	 * 
	 * @param coDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public AsyncBaseDto index(Skf2050Sc001CarryOutAsyncDto coDto) throws Exception {
		// 操作ログ出力
		skfOperationLogUtils.setAccessLog("搬出完了処理開始", CodeConstant.C001, FunctionIdConstant.SKF2050_SC001);

		// 入力チェック
		boolean validateResult = checkValidate(coDto);
		if (!validateResult) {
			throwBusinessExceptionIfErrors(coDto.getResultMessages());
			return coDto;
		}

		String applNo = coDto.getApplNo();
		String completionDay = coDto.getCompletionDay();
		completionDay = skfDateFormatUtils.dateFormatFromString(completionDay,
				SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT);

		// 退居日チェック
		// 退居日よりも搬出日が先の場合、警告文を表示
		String taikyoDate = skf2050Sc001SharedService.getTaikyobiInfo(applNo);
		if (NfwStringUtils.isNotEmpty(taikyoDate)) {
			int taikyoDateNum = Integer.parseInt(taikyoDate);
			int completionDayNum = Integer.parseInt(completionDay);
			if (taikyoDateNum < completionDayNum) {
				coDto.setShowDialogFlag("true");
				coDto.setDialogTaikyoDay(skfDateFormatUtils.dateFormatFromString(taikyoDate,
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH));
				return coDto;
			}
		}

		return coDto;
	}

	private boolean checkValidate(Skf2050Sc001CarryOutAsyncDto coDto) throws Exception {
		boolean result = true;
		// 搬出完了日チェック（必須チェック）
		String completionDay = coDto.getCompletionDay();
		if (CheckUtils.isEmpty(completionDay)) {
			ServiceHelper.addErrorResultMessage(coDto, new String[] { "completionDay" }, MessageIdConstant.E_SKF_1055,
					COMPLETION_DAY_LABEL);
			result = false;
		}

		// コメントの入力チェック
		String commentNote = coDto.getCommentNote();
		// コメントの最大入力桁数チェック
		// 値が4000バイトを超えた際はエラー
		int commentMaxLength = Integer.parseInt(PropertyUtils.getValue("skf.common.comment_max_length"));
		if (NfwStringUtils.isNotEmpty(commentNote) && commentMaxLength < commentNote.getBytes("UTF-8").length) {
			ServiceHelper.addErrorResultMessage(coDto, new String[] { "commentNote" }, MessageIdConstant.E_SKF_1049,
					COMMENT_LABEL, commentMaxLength / 2);
			result = false;
		}
		return result;

	}

}
