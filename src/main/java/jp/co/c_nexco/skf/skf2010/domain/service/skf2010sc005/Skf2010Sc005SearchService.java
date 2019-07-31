package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc005;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetShoninIchiranShoninExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetShoninIchiranShoninExpParameter;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc005.Skf2010Sc005SearchDto;

/**
 * Skf2010Sc005 承認一覧検索処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc005SearchService extends BaseServiceAbstract<Skf2010Sc005SearchDto> {

	@Autowired
	private Skf2010Sc005SharedService skf2010Sc005SharedService;

	private String companyCd = CodeConstant.C001;

	@Value("${skf2010.skf2010_sc005.search_max_count}")
	private String searchMaxCount;
	@Value("${skf.common.validate_error}")
	private String validationErrorCode;

	@Override
	public BaseDto index(Skf2010Sc005SearchDto searchDto) throws Exception {
		// タイトル設定
		searchDto.setPageTitleKey(MessageIdConstant.SKF2010_SC005_TITLE);
		// 操作ログ出力

		// ドロップダウンセット
		setDropDown(searchDto);

		// 入力チェック
		boolean resultValidate = checkValidate(searchDto);
		if (!resultValidate) {
			return searchDto;
		}

		// 検索処理（リストテーブル作成）
		searchDto.setLtResultList(searchApplList(searchDto));

		return searchDto;
	}

	/**
	 * ドロップダウンを作成します
	 * 
	 * @param dto
	 */
	@SuppressWarnings("unchecked")
	private void setDropDown(Skf2010Sc005SearchDto dto) {
		// ドロップダウン作成
		Map<String, Object> dropDownMap = new HashMap<String, Object>();
		skf2010Sc005SharedService.setDropDown(dropDownMap, companyCd, dto.getAgency(), dto.getAffiliation1(),
				dto.getAffiliation2());
		// 機関ドロップダウンをセット
		dto.setDdlAgencyList((List<Map<String, Object>>) dropDownMap.get("Agency"));
		// 部等ドロップダウンをセット
		dto.setDdlAffiliation1List((List<Map<String, Object>>) dropDownMap.get("Affiliation1"));
		// 室、チーム又は課ドロップダウンをセット
		dto.setDdlAffiliation2List((List<Map<String, Object>>) dropDownMap.get("Affiliation2"));

		return;
	}

	/**
	 * 入力チェックを行います
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	private boolean checkValidate(Skf2010Sc005SearchDto dto) throws Exception {
		boolean result = true;
		dto.setApplDateFromErr("");
		dto.setApplDateToErr("");
		dto.setAgreDateFromErr("");
		dto.setAgreDateToErr("");
		dto.setApplStatusErr("");
		// 申請日FROM
		if ((dto.getApplDateFrom() != null && !CheckUtils.isEmpty(dto.getApplDateFrom())
				&& (!CheckUtils.isFormatDate(dto.getApplDateFrom(), "yyyy/MM/dd") || !SkfCheckUtils
						.isSkfDateFormat(dto.getApplDateFrom(), CheckUtils.DateFormatType.YYYYMMDD)))) {
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1054, "申請日FROM");
			dto.setApplDateFromErr(validationErrorCode);
			dto.setApplDateFrom("");
			result = false;
		}
		// 申請日TO
		if ((dto.getApplDateTo() != null && !CheckUtils.isEmpty(dto.getApplDateTo())
				&& (!CheckUtils.isDateFormat(dto.getApplDateTo(), "yyyy/MM/dd")
						|| !SkfCheckUtils.isSkfDateFormat(dto.getApplDateTo(), CheckUtils.DateFormatType.YYYYMMDD)))) {
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1055, "申請日To");
			dto.setApplDateToErr(validationErrorCode);
			// dto.setApplDateTo("");
			result = false;
		}
		// 承認日FROM
		if ((dto.getAgreDateFrom() != null && !CheckUtils.isEmpty(dto.getAgreDateFrom())
				&& (!CheckUtils.isDateFormat(dto.getAgreDateFrom(), "yyyy/MM/dd") || !SkfCheckUtils
						.isSkfDateFormat(dto.getAgreDateFrom(), CheckUtils.DateFormatType.YYYYMMDD)))) {
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1055, "承認日／修正依頼日From");
			dto.setAgreDateFromErr(validationErrorCode);
			dto.setAgreDateFrom("");
			result = false;
		}
		// 承認日TO
		if ((dto.getAgreDateTo() != null && !CheckUtils.isEmpty(dto.getAgreDateTo())
				&& (!CheckUtils.isDateFormat(dto.getAgreDateTo(), "yyyy/MM/dd")
						|| !SkfCheckUtils.isSkfDateFormat(dto.getAgreDateTo(), CheckUtils.DateFormatType.YYYYMMDD)))) {
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1055, "承認日／修正依頼日From");
			dto.setAgreDateToErr(validationErrorCode);
			dto.setAgreDateTo("");
			result = false;
		}
		// 申請状況
		if (dto.getApplStatus() == null || dto.getApplStatus().length == 0) {
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.W_SKF_1048, "申請状況");
			dto.setApplStatusErr(validationErrorCode);
			result = false;
		}

		// ここから関連項目チェック
		// 申請日のFrom < To整合性
		Date fromDate = null;
		Date toDate = null;
		int diff = 0;
		if ((dto.getApplDateFrom() != null && !CheckUtils.isEmpty(dto.getApplDateFrom()))
				&& (dto.getApplDateTo() != null && !CheckUtils.isEmpty(dto.getApplDateTo()))) {
			fromDate = DateFormat.getDateInstance().parse(dto.getApplDateFrom());
			toDate = DateFormat.getDateInstance().parse(dto.getApplDateTo());

			diff = fromDate.compareTo(toDate);
			if (diff > 0) {
				ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1133, "申請日");
				dto.setApplDateFromErr(validationErrorCode);
				dto.setApplDateToErr(validationErrorCode);
				result = false;
			}

		}
		// 承認日のFrom < To整合性
		if ((dto.getAgreDateFrom() != null && !CheckUtils.isEmpty(dto.getAgreDateFrom()))
				&& (dto.getAgreDateTo() != null && !CheckUtils.isEmpty(dto.getAgreDateTo()))) {
			fromDate = DateFormat.getDateInstance().parse(dto.getAgreDateFrom());
			toDate = DateFormat.getDateInstance().parse(dto.getAgreDateTo());

			diff = fromDate.compareTo(toDate);
			if (diff > 0) {
				ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1133, "承認日／修正依頼日");
				dto.setAgreDateFromErr(validationErrorCode);
				dto.setAgreDateToErr(validationErrorCode);
				result = false;
			}

		}

		return result;
	}

	/**
	 * 申請情報の一覧を取得します
	 * 
	 * @param dto
	 * @return
	 */
	private List<Map<String, Object>> searchApplList(Skf2010Sc005SearchDto dto) {
		List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();

		// 申請一覧を条件から取得
		List<Skf2010Sc005GetShoninIchiranShoninExp> tApplHistoryData = new ArrayList<Skf2010Sc005GetShoninIchiranShoninExp>();
		Skf2010Sc005GetShoninIchiranShoninExpParameter param = new Skf2010Sc005GetShoninIchiranShoninExpParameter();

		// 検索条件セット
		param = setParam(dto);
		// 検索処理
		tApplHistoryData = skf2010Sc005SharedService.SearchApplList(param);
		if (tApplHistoryData == null || tApplHistoryData.size() == 0) {
			// 検索結果0件
			ServiceHelper.addWarnResultMessage(dto, MessageIdConstant.W_SKF_1007);
		} else if (tApplHistoryData.size() > Integer.parseInt(searchMaxCount)) {
			// 検索結果表示最大数以上
			ServiceHelper.addWarnResultMessage(dto, MessageIdConstant.W_SKF_1002, "最大件数");
		}

		// グリッド表示（リストテーブル）作成
		rtnList = skf2010Sc005SharedService.createListTable(tApplHistoryData);

		return rtnList;
	}

	/**
	 * 検索条件をセットします。
	 * 
	 * @param dto
	 * @return
	 */
	private Skf2010Sc005GetShoninIchiranShoninExpParameter setParam(Skf2010Sc005SearchDto dto) {
		Skf2010Sc005GetShoninIchiranShoninExpParameter param = new Skf2010Sc005GetShoninIchiranShoninExpParameter();
		// 会社コード
		param.setCompanyCd(companyCd);

		// 機関
		if (dto.getAgency() != null && !CheckUtils.isEmpty(dto.getAgency())) {
			param.setAgencyName(skf2010Sc005SharedService.getAgencyName(companyCd, dto.getAgency()));
		}
		// 部等
		if (dto.getAffiliation1() != null && !CheckUtils.isEmpty(dto.getAffiliation1())) {
			param.setAffiliation1Name(
					skf2010Sc005SharedService.getAffiliation1Name(companyCd, dto.getAgency(), dto.getAffiliation1()));

		}
		// 室、チーム
		if (dto.getAffiliation2() != null && !CheckUtils.isEmpty(dto.getAffiliation2())) {
			param.setAffiliation2Name(skf2010Sc005SharedService.getAffiliation2Name(companyCd, dto.getAgency(),
					dto.getAffiliation1(), dto.getAffiliation2()));

		}
		// 所属機関
		param.setShozokuKikan(dto.getShozokuKikan());
		// 申請日時（FROM）
		if (dto.getApplDateFrom() != null && !CheckUtils.isEmpty(dto.getApplDateFrom())) {
			param.setApplDateFrom(dto.getApplDateFrom().replace("/", ""));
		}
		// 申請日時（TO）
		if (dto.getApplDateTo() != null && !CheckUtils.isEmpty(dto.getApplDateTo())) {
			param.setApplDateTo(dto.getApplDateTo().replace("/", ""));
		}
		// 承認日／修正依頼日（From）
		if (dto.getAgreDateFrom() != null && !CheckUtils.isEmpty(dto.getAgreDateFrom())) {
			param.setAgreDateFrom(dto.getAgreDateFrom().replace("/", ""));
		}
		// 承認日／修正依頼日（To）
		if (dto.getAgreDateTo() != null && !CheckUtils.isEmpty(dto.getAgreDateTo())) {
			param.setAgreDateTo(dto.getAgreDateTo().replace("/", ""));
		}
		// 申請者名
		if (dto.getName() != null && !CheckUtils.isEmpty(dto.getName())) {
			param.setName(dto.getName());
		}
		// 申請書類種別
		param.setApplCtgryId(dto.getApplCtgry());
		// 申請書類名
		if (NfwStringUtils.isNotEmpty(dto.getApplName())) {
			param.setApplName(dto.getApplName());
		}
		// 申請状況
		if (dto.getApplStatus() != null && dto.getApplStatus().length > 0) {
			List<String> applStatusList = new ArrayList<String>();
			List<String> tmpApplStatus = Arrays.asList(dto.getApplStatus());
			applStatusList.addAll(tmpApplStatus);
			// 申請状況に「30：承認中」がセットされていた場合、承認1～4まで全て設定する
			if (applStatusList.contains(CodeConstant.STATUS_SHONIN)) {
				applStatusList.add(CodeConstant.STATUS_SHONIN1);
				applStatusList.add(CodeConstant.STATUS_SHONIN2);
				applStatusList.add(CodeConstant.STATUS_SHONIN3);
				applStatusList.add(CodeConstant.STATUS_SHONIN4);
			}
			param.setApplStatus(applStatusList);
		}
		// 承認者名
		if (dto.getAgreementName() != null && !CheckUtils.isEmpty(dto.getAgreementName())) {
			param.setAgreeName(dto.getAgreementName());
		}
		return param;
	}
}
