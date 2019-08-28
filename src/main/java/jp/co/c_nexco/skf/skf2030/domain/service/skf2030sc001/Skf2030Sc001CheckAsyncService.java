/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2030.domain.service.skf2030sc001;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2030.domain.dto.skf2030sc001.Skf2030Sc001CheckAsyncDto;

/**
 * Skf2030Sc001 備品希望申請（申請者用)申請処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2030Sc001CheckAsyncService extends AsyncBaseServiceAbstract<Skf2030Sc001CheckAsyncDto> {

	@Autowired
	private Skf2030Sc001SharedService skf2030Sc001SharedService;

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;

	// 排他処理用最終更新日付
	public static final String APPL_HISTORY_KEY_LAST_UPDATE_DATE = "skf2010_t_appl_history_UpdateDate";
	public static final String BIHIN_KIBO_SHINSEI_KEY_LAST_UPDATE_DATE = "skf2030_t_bihin_kibo_shinsei_UpdateDate";
	public static final String BIHIN_KEY_LAST_UPDATE_DATE = "skf2030_t_bihin_UpdateDate";

	/**
	 * サービス処理を行う。
	 * 
	 * @param chkDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public AsyncBaseDto index(Skf2030Sc001CheckAsyncDto chkDto) throws Exception {
		// 操作ログ出力
		skfOperationLogUtils.setAccessLog("搬入完了日チェック処理開始", CodeConstant.C001, FunctionIdConstant.SKF2030_SC001);

		// 申請情報設定
		Map<String, String> applInfo = new HashMap<String, String>();
		applInfo.put("status", chkDto.getApplStatus());
		applInfo.put("applNo", chkDto.getApplNo());
		applInfo.put("applId", chkDto.getApplId());
		applInfo.put("shainNo", chkDto.getHdnShainNo());

		String completeDate = chkDto.getCompletionDay();

		if (!checkValidate(completeDate, chkDto)) {
			throwBusinessExceptionIfErrors(chkDto.getResultMessages());
		}

		boolean result = skf2030Sc001SharedService.sokanCheck(applInfo, completeDate);
		if (result) {
			chkDto.setNyukyobi(skfDateFormatUtils.dateFormatFromString(applInfo.get("nyukyobi"),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH));
		}

		chkDto.setShowDialogFlag(String.valueOf(result));
		return chkDto;
	}

	private boolean checkValidate(String completeDate, Skf2030Sc001CheckAsyncDto chkDto) {
		boolean result = true;
		if (NfwStringUtils.isEmpty(completeDate)) {
			ServiceHelper.addErrorResultMessage(chkDto, new String[] { "completionDay" }, MessageIdConstant.E_SKF_1048,
					"搬入完了日");
			result = false;
		}
		if (!SkfCheckUtils.isSkfDateFormat(completeDate, CheckUtils.DateFormatType.YYYYMMDD)) {
			ServiceHelper.addErrorResultMessage(chkDto, new String[] { "completionDay" }, MessageIdConstant.E_SKF_1055,
					"搬入完了日");
			result = false;
		}

		return result;
	}

}
