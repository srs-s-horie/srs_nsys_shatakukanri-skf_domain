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
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.SkfBatchBusinessLogicUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc003.Skf2010Sc003DeleteDto;

/**
 * Skf2010Sc003 申請状況一覧削除処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc003DeleteService extends SkfServiceAbstract<Skf2010Sc003DeleteDto> {

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

	@Value("${skf2010.skf2010_sc005.search_max_count}")
	private String searchMaxCount;
	@Value("${skf.common.validate_error}")
	private String validationErrorCode;

	// 基準会社コード
	private String companyCd = CodeConstant.C001;

	private String pattern = "yyyyMMdd";

	/**
	 * サービス処理を行う。
	 * 
	 * @param delDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public BaseDto index(Skf2010Sc003DeleteDto delDto) throws Exception {
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("削除", companyCd, delDto.getPageId());

		delDto.setPageTitleKey(MessageIdConstant.SKF2010_SC003_TITLE);

		// 申請書類番号
		String applNo = delDto.getApplNo();
		// 申請書ID
		String applId = delDto.getApplId();
		// 帳票テーブル取得
		List<String> saveTableList = getSaveTableName(applId);
		// 排他処理用最終更新日取得
		Map<String, Date> lastUpdateDateMap = delDto.getLastUpdateDateMap();
		Date lastUpdateDate = lastUpdateDateMap.get(applNo);
		// エラーメッセージMap
		// error : メッセージID
		// value : 引数
		Map<String, String> errorMsg = new HashMap<String, String>();

		// 申請書類削除処理
		boolean result = skf2010Sc003SharedService.deleteApplHistory(applNo, applId, saveTableList, lastUpdateDate,
				errorMsg);
		if (!result) {
			if (NfwStringUtils.isNotEmpty(errorMsg.get("error"))) {
				ServiceHelper.addErrorResultMessage(delDto, null, errorMsg.get("error"), errorMsg.get("value"));
			} else {
				ServiceHelper.addErrorResultMessage(delDto, null, MessageIdConstant.E_SKF_1075);
			}
			throwBusinessExceptionIfErrors(delDto.getResultMessages());
			return delDto;
		}

		Map<String, String> loginUserInfo = skfLoginUserInfoUtils
				.getSkfLoginUserInfoFromAlterLogin(menuScopeSessionBean);
		String shainNo = loginUserInfo.get("shainNo");

		// 社宅管理データ連携処理実行（データ連携用Mapは消さない）
		String afterApplStatus = skf2010Sc003SharedService.getApplStatus(applNo);
		List<String> resultBatch = new ArrayList<String>();
		resultBatch = skf2010Sc003SharedService.doShatakuRenkei(menuScopeSessionBean, applNo, afterApplStatus, applId,
				FunctionIdConstant.SKF2010_SC003, shainNo);
		if (resultBatch != null) {
			skfBatchBusinessLogicUtils.addResultMessageForDataLinkage(delDto, resultBatch);
			skfRollBackExpRepository.rollBack();
			throwBusinessExceptionIfErrors(delDto.getResultMessages());
		}

		// 表示データ取得
		List<Skf2010Sc003GetApplHistoryStatusInfoExp> resultList = getApplHistoryList(delDto);
		if (resultList == null || resultList.size() <= 0) {
			ServiceHelper.addWarnResultMessage(delDto, MessageIdConstant.W_SKF_1007);
			delDto.setLtResultList(skf2010Sc003SharedService.createListTable(resultList, delDto));
			return delDto;
		}
		delDto.setLtResultList(skf2010Sc003SharedService.createListTable(resultList, delDto));

		// 完了メッセージ表示
		ServiceHelper.addResultMessage(delDto, MessageIdConstant.I_SKF_2047);

		return delDto;
	}

	/**
	 * 申請書類履歴検索結果を取得します
	 * 
	 * @param dto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Skf2010Sc003GetApplHistoryStatusInfoExp> getApplHistoryList(Skf2010Sc003DeleteDto dto) {
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

	private List<String> getSaveTableName(String applId) {
		List<String> tableList = new ArrayList<String>();
		if (applId == null || CheckUtils.isEmpty(applId)) {
			return tableList;
		}

		switch (applId) {
		case FunctionIdConstant.R0100:
			// 社宅入居希望等調書
			tableList.add("skf2020_t_nyukyo_chosho_tsuchi");
			// 備品希望申請、備品申請、備品返却申請
			tableList.add("skf2030_t_bihin_kibo_shinsei");
			tableList.add("skf2030_t_bihin");
			tableList.add("skf2050_t_bihin_henkyaku_shinsei");
			break;
		case FunctionIdConstant.R0103:
			// 退居（自動車の保管場所返還）届
			// 備品返却申請
			tableList.add("skf2040_t_taikyo_report");
			tableList.add("skf2050_t_bihin_henkyaku_shinsei");
			break;
		case FunctionIdConstant.R0105:
			// 備品返却確認
			// 備品申請
			tableList.add("skf2030_t_bihin");
		}

		// 添付ファイル管理テーブル
		tableList.add("skf2010_t_attached_file");

		// 申請書類コメントテーブル
		tableList.add("skf2010_t_appl_comment");

		return tableList;
	}

}
