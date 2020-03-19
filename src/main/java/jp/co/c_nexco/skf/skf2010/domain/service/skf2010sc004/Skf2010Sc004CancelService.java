/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc004;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetApplHistoryInfoByParameterExp;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfRollBack.SkfRollBackExpRepository;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfReturnFormEditUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.SkfBatchBusinessLogicUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc004.Skf2010Sc004CancelDto;

/**
 * Skf2010Sc004 申請内容表示/引戻し取り下げ処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc004CancelService extends BaseServiceAbstract<Skf2010Sc004CancelDto> {

	@Autowired
	private Skf2010Sc004SharedService skf2010Sc004SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfBatchBusinessLogicUtils skfBatchBusinessLogicUtils;
	@Autowired
	private SkfRollBackExpRepository skfRollBackExpRepository;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfReturnFormEditUtils skfReturnFormEditUtils;

	private String companyCd = CodeConstant.C001;

	@Value("${skf2010.skf2010_sc005.search_max_count}")
	private String searchMaxCount;
	@Value("${skf.common.validate_error}")
	private String validationErrorCode;

	/**
	 * サービス処理を行う。
	 * 
	 * @param cancelDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public BaseDto index(Skf2010Sc004CancelDto cancelDto) throws Exception {
		// 操作ログの出力
		skfOperationLogUtils.setAccessLog("取下げ", companyCd, FunctionIdConstant.SKF2010_SC004);

		cancelDto.setPageTitleKey(MessageIdConstant.SKF2010_SC003_TITLE);

		// 申請書類ID
		String applId = cancelDto.getApplId();
		// 申請書番号
		String applNo = cancelDto.getApplNo();

		// 「申請書類履歴テーブル」よりステータスを更新
		Map<String, String> errMsg = new HashMap<String, String>();
		Date lastUpdateDate = cancelDto.getLastUpdateDate(skf2010Sc004SharedService.KEY_LAST_UPDATE_DATE_HISTORY);
		boolean result = skf2010Sc004SharedService.updateApplHistoryCancel(applNo, applId, lastUpdateDate, errMsg);
		if (!result) {
			if (NfwStringUtils.isNotEmpty(errMsg.get("error"))) {
				ServiceHelper.addErrorResultMessage(cancelDto, null, errMsg.get("error"), errMsg.get("value"));
			} else {
				ServiceHelper.addErrorResultMessage(cancelDto, null, MessageIdConstant.E_SKF_1075);
			}
			throwBusinessExceptionIfErrors(cancelDto.getResultMessages());
		}

		// 社宅管理データ連携処理実行
		Skf2010Sc004GetApplHistoryInfoByParameterExp tApplHistoryData = new Skf2010Sc004GetApplHistoryInfoByParameterExp();
		tApplHistoryData = skf2010Sc004SharedService.getApplHistoryInfo(applNo);
		if (tApplHistoryData == null) {
			ServiceHelper.addErrorResultMessage(cancelDto, null, MessageIdConstant.E_SKF_1073);
			throwBusinessExceptionIfErrors(cancelDto.getResultMessages());
			return cancelDto;
		}
		String afterApplStatus = CodeConstant.STATUS_TORISAGE;
		List<String> resultBatch = new ArrayList<String>();
		resultBatch = skf2010Sc004SharedService.doShatakuRenkei(menuScopeSessionBean, applNo, CodeConstant.NONE,
				afterApplStatus, applId, FunctionIdConstant.SKF2010_SC004);
		menuScopeSessionBean.remove(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2010SC004);
		if (resultBatch != null) {
			skfBatchBusinessLogicUtils.addResultMessageForDataLinkage(cancelDto, resultBatch);
			skfRollBackExpRepository.rollBack();
		}

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

		// 汎用コード取得
		Map<String, String> genericCodeMap = new HashMap<String, String>();
		genericCodeMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_STATUS);
		
		// 変更データ設定
		Map<String, Object> changeListTableData = new HashMap<String, Object>();
		changeListTableData.put("applStatus", CodeConstant.STATUS_ICHIJIHOZON);
		changeListTableData.put("applStatusText", genericCodeMap.get(CodeConstant.STATUS_ICHIJIHOZON));
		changeListTableData.put("cancel", null);
		changeListTableData.put("delete", CodeConstant.DOUBLE_QUOTATION);
		
		/** 申請状況一覧画面のリストテーブルのFormデータから書き換える */
		boolean changeCheck = skfReturnFormEditUtils.editListTableFormDataByPrimaryKey(FunctionIdConstant.SKF2010_SC003, "ltResultList", 
				"applNo", applNo, changeListTableData);

		// 完了メッセージ表示
		ServiceHelper.addResultMessage(cancelDto, MessageIdConstant.I_SKF_2047);

		Map<String, Object> attribute = new HashMap<String, Object>();
		attribute.put(SkfCommonConstant.KEY_APPL_ID, applId);
		attribute.put(SkfCommonConstant.KEY_APPL_NO, applNo);
		attribute.put(SkfCommonConstant.KEY_SHAIN_NO, cancelDto.getShainNo());
		attribute.put(SkfCommonConstant.KEY_STATUS, CodeConstant.STATUS_ICHIJIHOZON);
		attribute.put("backUrl", "skf/Skf2010Sc003/init");

		TransferPageInfo nextPage = TransferPageInfo.nextPage(nextPageId, "init");
		nextPage.addResultMessage(MessageIdConstant.I_SKF_2047);
		nextPage.setTransferAttributes(attribute);
		cancelDto.setTransferPageInfo(nextPage);

		return cancelDto;
	}

}
