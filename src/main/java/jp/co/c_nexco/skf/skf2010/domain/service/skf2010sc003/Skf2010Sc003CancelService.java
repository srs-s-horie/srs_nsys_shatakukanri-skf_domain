/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc003;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import edu.emory.mathcs.backport.java.util.Arrays;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc003.Skf2010Sc003GetApplHistoryStatusInfoExp;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfRollBack.SkfRollBackExpRepository;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.SkfBatchBusinessLogicUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc003.Skf2010Sc003CancelDto;

/**
 * Skf2010Sc003 申請状況一覧取下げ処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc003CancelService extends BaseServiceAbstract<Skf2010Sc003CancelDto> {

	@Autowired
	private Skf2010Sc003SharedService skf2010Sc003SharedService;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfBatchBusinessLogicUtils skfBatchBusinessLogicUtils;
	@Autowired
	private SkfRollBackExpRepository skfRollBackExpRepository;

	@Value("${skf2010.skf2010_sc003.max_search_count}")
	private String searchMaxCount;
	@Value("${skf.common.validate_error}")
	private String validationErrorCode;

	// 基準会社コード
	private String companyCd = CodeConstant.C001;

	private String pattern = "yyyyMMdd";

	/**
	 * サービス処理を行う。
	 * 
	 * @param cancelDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public BaseDto index(Skf2010Sc003CancelDto cancelDto) throws Exception {
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("取下げ", companyCd, cancelDto.getPageId());

		cancelDto.setPageTitleKey(MessageIdConstant.SKF2010_SC003_TITLE);

		// 申請書類番号
		String applNo = cancelDto.getApplNo();
		// 申請書ID
		String applId = cancelDto.getApplId();
		// 最終更新日
		Map<String, Date> lastUpdateDateMap = cancelDto.getLastUpdateDateMap();
		Date lastUpdateDate = lastUpdateDateMap.get(applNo);
		// エラー処理用Map
		Map<String, String> errorMsg = new HashMap<String, String>();
		// 「申請書類履歴テーブル」よりステータスを更新
		boolean result = skf2010Sc003SharedService.updateApplHistoryCancel(applNo, applId, lastUpdateDate, errorMsg);
		if (!result) {
			ServiceHelper.addErrorResultMessage(cancelDto, null, MessageIdConstant.E_SKF_1075);
			throwBusinessExceptionIfErrors(cancelDto.getResultMessages());
		}

		Map<String, String> loginUserInfo = skfLoginUserInfoUtils
				.getSkfLoginUserInfoFromAlterLogin(menuScopeSessionBean);
		String shainNo = loginUserInfo.get("shainNo");

		// 社宅管理データ連携処理実行（データ連携用Mapは消さない）
		String afterApplStatus = CodeConstant.STATUS_TORISAGE;
		List<String> resultBatch = new ArrayList<String>();
		resultBatch = skf2010Sc003SharedService.doShatakuRenkei(menuScopeSessionBean, applNo, afterApplStatus, applId,
				FunctionIdConstant.SKF2010_SC003, shainNo);
		if (resultBatch != null) {
			skfBatchBusinessLogicUtils.addResultMessageForDataLinkage(cancelDto, resultBatch);
			skfRollBackExpRepository.rollBack();
			throwBusinessExceptionIfErrors(cancelDto.getResultMessages());
		}

		// 表示データ取得
		List<Skf2010Sc003GetApplHistoryStatusInfoExp> resultList = getApplHistoryList(cancelDto);
		if (resultList == null || resultList.size() <= 0) {
			ServiceHelper.addWarnResultMessage(cancelDto, MessageIdConstant.W_SKF_1007);
		} else if (resultList.size() > Integer.parseInt(searchMaxCount)) {
			// 検索結果表示最大数以上
			ServiceHelper.addWarnResultMessage(cancelDto, MessageIdConstant.W_SKF_1002, "100", "抽出条件を変更してください。");
		}
		cancelDto.setLtResultList(skf2010Sc003SharedService.createListTable(resultList, cancelDto));

		// 完了メッセージ表示
		ServiceHelper.addResultMessage(cancelDto, MessageIdConstant.I_SKF_2047);

		return cancelDto;
	}

	@SuppressWarnings("unchecked")
	private List<Skf2010Sc003GetApplHistoryStatusInfoExp> getApplHistoryList(Skf2010Sc003CancelDto dto) {
		Map<String, String> loginUserInfo = skfLoginUserInfoUtils
				.getSkfLoginUserInfoFromAlterLogin(menuScopeSessionBean);
		String shainNo = loginUserInfo.get("shainNo");

		String applDateFrom = skfDateFormatUtils.dateFormatFromString(dto.getApplDateFrom(), pattern);
		String applDateTo = skfDateFormatUtils.dateFormatFromString(dto.getApplDateTo(), pattern);

		String agreDateFrom = skfDateFormatUtils.dateFormatFromString(dto.getAgreDateFrom(), pattern);
		String agreDateTo = skfDateFormatUtils.dateFormatFromString(dto.getAgreDateTo(), pattern);

		String applName = dto.getApplName();

		String[] applStatus = dto.getApplStatus();
		List<String> applStatusList = new ArrayList<String>();
		if (applStatus != null && applStatus.length > 0) {
			applStatusList = Arrays.asList(applStatus);
		}

		// 検索結果取得
		List<Skf2010Sc003GetApplHistoryStatusInfoExp> resultList = skf2010Sc003SharedService.getApplHistoryStatusInfo(
				shainNo, applDateFrom, applDateTo, agreDateFrom, agreDateTo, applName, applStatusList);

		return resultList;
	}

}
