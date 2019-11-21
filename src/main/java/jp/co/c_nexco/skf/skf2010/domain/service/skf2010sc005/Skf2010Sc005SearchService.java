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
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
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
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	private String companyCd = CodeConstant.C001;

	@Value("${skf2010.skf2010_sc005.search_max_count}")
	private String searchMaxCount;
	@Value("${skf.common.validate_error}")
	private String validationErrorCode;

	@Override
	public BaseDto index(Skf2010Sc005SearchDto searchDto) throws Exception {
		// 操作ログ出力
		skfOperationLogUtils.setAccessLog("検索処理開始", CodeConstant.C001, FunctionIdConstant.SKF2010_SC005);
		// タイトル設定
		searchDto.setPageTitleKey(MessageIdConstant.SKF2010_SC005_TITLE);

		// ドロップダウンセット
		skf2010Sc005SharedService.setDropDown(searchDto, companyCd, searchDto.getAgency(), searchDto.getAffiliation1(),
				searchDto.getAffiliation2());

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
		skf2010Sc005SharedService.setDropDown(dto, companyCd, dto.getAgency(), dto.getAffiliation1(),
				dto.getAffiliation2());

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

		// 申請状況
		if (dto.getApplStatus() == null || dto.getApplStatus().length == 0) {
			ServiceHelper.addErrorResultMessage(dto, new String[] { "applStatusArea" }, MessageIdConstant.E_SKF_1054,
					"申請状況");
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
		param = skf2010Sc005SharedService.setParam(dto);
		// 検索処理
		tApplHistoryData = skf2010Sc005SharedService.searchApplList(param);
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

}
