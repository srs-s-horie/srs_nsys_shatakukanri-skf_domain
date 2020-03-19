/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc003;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import edu.emory.mathcs.backport.java.util.Arrays;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc003.Skf2010Sc003GetApplHistoryStatusInfoExp;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc003.Skf2010Sc003SearchDto;

/**
 * Skf2010Sc003 申請状況一覧検索処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc003SearchService extends BaseServiceAbstract<Skf2010Sc003SearchDto> {

	@Autowired
	private Skf2010Sc003SharedService skf2010Sc003SharedService;

	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

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
	 * @param searchDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Skf2010Sc003SearchDto index(Skf2010Sc003SearchDto searchDto) throws Exception {
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("申請状況を確認", companyCd, searchDto.getPageId());

		searchDto.setPageTitleKey(MessageIdConstant.SKF2010_SC003_TITLE);

		// 入力チェック
		if (!checkValidate(searchDto)) {
			return searchDto;
		}

		// 検索結果取得
		List<Skf2010Sc003GetApplHistoryStatusInfoExp> resultList = getApplHistoryList(searchDto);
		if (resultList == null || resultList.size() <= 0) {
			ServiceHelper.addWarnResultMessage(searchDto, MessageIdConstant.W_SKF_1007);
		} else if (resultList.size() > Integer.parseInt(searchMaxCount)) {
			// 検索結果表示最大数以上
			ServiceHelper.addWarnResultMessage(searchDto, MessageIdConstant.W_SKF_1002, "100", "抽出条件を変更してください。");
		}
		searchDto.setLtResultList(skf2010Sc003SharedService.createListTable(resultList, searchDto));
		return searchDto;
	}

	/**
	 * 入力チェックを行います
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	private boolean checkValidate(Skf2010Sc003SearchDto dto) throws Exception {
		boolean result = true;
		dto.setApplDateFromErr("");
		dto.setApplDateToErr("");
		dto.setAgreDateFromErr("");
		dto.setAgreDateToErr("");

		// 申請状況
		if (dto.getApplStatus() == null || dto.getApplStatus().length == 0) {
			ServiceHelper.addErrorResultMessage(dto, new String[] { "applStatusArea" }, MessageIdConstant.E_SKF_1054,
					"申請状況");
			result = false;
		}
		// 申請日FROM
		if ((dto.getApplDateFrom() != null && !CheckUtils.isEmpty(dto.getApplDateFrom())
				&& (!CheckUtils.isFormatDate(dto.getApplDateFrom(), "yyyy/MM/dd") && !SkfCheckUtils
						.isSkfDateFormat(dto.getApplDateFrom(), CheckUtils.DateFormatType.YYYYMMDD)))) {
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1055, "申請日FROM");
			dto.setApplDateFromErr(validationErrorCode);
			dto.setApplDateFrom("");
			result = false;
		}
		// 申請日TO
		if ((dto.getApplDateTo() != null && !CheckUtils.isEmpty(dto.getApplDateTo())
				&& (!CheckUtils.isFormatDate(dto.getApplDateTo(), "yyyy/MM/dd")
						&& !SkfCheckUtils.isSkfDateFormat(dto.getApplDateTo(), CheckUtils.DateFormatType.YYYYMMDD)))) {
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1055, "申請日To");
			dto.setApplDateToErr(validationErrorCode);
			// dto.setApplDateTo("");
			result = false;
		}
		// 承認日FROM
		if ((dto.getAgreDateFrom() != null && !CheckUtils.isEmpty(dto.getAgreDateFrom())
				&& (!CheckUtils.isDateFormat(dto.getAgreDateFrom(), "yyyy/MM/dd") && !SkfCheckUtils
						.isSkfDateFormat(dto.getAgreDateFrom(), CheckUtils.DateFormatType.YYYYMMDD)))) {
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1055, "承認日／修正依頼日From");
			dto.setAgreDateFromErr(validationErrorCode);
			dto.setAgreDateFrom("");
			result = false;
		}
		// 承認日TO
		if ((dto.getAgreDateTo() != null && !CheckUtils.isEmpty(dto.getAgreDateTo())
				&& (!CheckUtils.isDateFormat(dto.getAgreDateTo(), "yyyy/MM/dd")
						&& !SkfCheckUtils.isSkfDateFormat(dto.getAgreDateTo(), CheckUtils.DateFormatType.YYYYMMDD)))) {
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1055, "承認日／修正依頼日From");
			dto.setAgreDateToErr(validationErrorCode);
			dto.setAgreDateTo("");
			result = false;
		}

		// ここから関連項目チェック
		// 申請日のFrom < To整合性
		Date fromDate = null;
		Date toDate = null;
		int diff = 0;
		if ((dto.getApplDateFrom() != null && !CheckUtils.isEmpty(dto.getApplDateFrom()))
				&& (dto.getApplDateTo() != null && !CheckUtils.isEmpty(dto.getApplDateTo()))) {
			fromDate = skfDateFormatUtils.formatStringToDate(dto.getApplDateFrom());
			toDate = skfDateFormatUtils.formatStringToDate(dto.getApplDateTo());

			diff = fromDate.compareTo(toDate);
			if (diff > 0) {
				ServiceHelper.addErrorResultMessage(dto, new String[] { "applDateFrom", "applDateTo" },
						MessageIdConstant.E_SKF_1133, "申請日");
				dto.setApplDateFromErr(validationErrorCode);
				dto.setApplDateToErr(validationErrorCode);
				result = false;
			}

		}
		// 承認日のFrom < To整合性
		if ((dto.getAgreDateFrom() != null && !CheckUtils.isEmpty(dto.getAgreDateFrom()))
				&& (dto.getAgreDateTo() != null && !CheckUtils.isEmpty(dto.getAgreDateTo()))) {
			fromDate = skfDateFormatUtils.formatStringToDate(dto.getAgreDateFrom());
			toDate = skfDateFormatUtils.formatStringToDate(dto.getAgreDateTo());

			diff = fromDate.compareTo(toDate);
			if (diff > 0) {
				ServiceHelper.addErrorResultMessage(dto, new String[] { "agreDateFrom", "agreDateTo" },
						MessageIdConstant.E_SKF_1133, "承認日／修正依頼日");
				dto.setAgreDateFromErr(validationErrorCode);
				dto.setAgreDateToErr(validationErrorCode);
				result = false;
			}

		}

		return result;
	}

	@SuppressWarnings("unchecked")
	private List<Skf2010Sc003GetApplHistoryStatusInfoExp> getApplHistoryList(Skf2010Sc003SearchDto dto) {
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
