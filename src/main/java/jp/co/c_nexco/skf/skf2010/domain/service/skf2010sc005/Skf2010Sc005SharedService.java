package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc005;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetAffiliation1ListExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetAffiliation1ListExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetAffiliation2ListExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetAffiliation2ListExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetAgencyNameExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetAgencyNameExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetAgreeAuthorityCountExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetAgreeAuthorityCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetAgreeAuthorityGroupIdExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetAgreeAuthorityGroupIdExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetApplHistoryInfoByParameterExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetApplHistoryInfoByParameterExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetApplHistoryStatusInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetApplHistoryStatusInfoForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetCommentInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetCommentInfoForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetSendApplMailInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetSendApplMailInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetShoninIchiranShoninExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetShoninIchiranShoninExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetTeijiDataInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetTeijiDataInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005UpdateApplHistoryAgreeStatusExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005UpdateCommentInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetShatakuNyukyoKiboInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetShatakuNyukyoKiboInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBatchUtils.SkfBatchUtilsGetMultipleTablesUpdateDateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBihinInfoUtils.SkfBihinInfoUtilsGetBihinInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShain;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShainKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010MApplication;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010MApplicationKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplCommentKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplHistory;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchi;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchiKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2030TBihinKiboShinsei;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2030TBihinKiboShinseiKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReport;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReportKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2050TBihinHenkyakuShinsei;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2050TBihinHenkyakuShinseiKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc005.Skf2010Sc005GetAffiliation1ListExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc005.Skf2010Sc005GetAffiliation2ListExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc005.Skf2010Sc005GetAgencyNameExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc005.Skf2010Sc005GetAgreeAuthorityCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc005.Skf2010Sc005GetAgreeAuthorityGroupIdExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc005.Skf2010Sc005GetApplHistoryInfoByParameterExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc005.Skf2010Sc005GetApplHistoryStatusInfoForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc005.Skf2010Sc005GetCommentInfoForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc005.Skf2010Sc005GetSendApplMailInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc005.Skf2010Sc005GetShoninIchiranShoninExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc005.Skf2010Sc005GetTeijiDataInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc005.Skf2010Sc005UpdateApplHistoryAgreeStatusExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc005.Skf2010Sc005UpdateCommentInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc003.Skf2020Sc003GetShatakuNyukyoKiboInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfRollBack.SkfRollBackExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MShainRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010MApplicationRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TApplHistoryRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2020TNyukyoChoshoTsuchiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2030TBihinKiboShinseiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2040TTaikyoReportRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2050TBihinHenkyakuShinseiRepository;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.NfwSendMailUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfCommentUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfTeijiDataInfoUtils;
import jp.co.c_nexco.skf.common.util.batch.SkfBatchUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2020Fc001NyukyoKiboSinseiDataImport;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2030Fc001BihinKiboShinseiDataImport;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2040Fc001TaikyoTodokeDataImport;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2050Fc001BihinHenkyakuSinseiDataImport;
import jp.co.c_nexco.skf.common.util.datalinkage.SkfBatchBusinessLogicUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010Sc005common.Skf2010Sc005CommonDto;
import jp.co.intra_mart.foundation.BaseUrl;

/**
 * Skf2010Sc005 承認一覧内部処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc005SharedService {

	@Autowired
	private Skf2010Sc005GetApplHistoryInfoByParameterExpRepository skf2010Sc005GetApplHistoryInfoByParameterExpRepository;
	@Autowired
	private Skf2010Sc005GetShoninIchiranShoninExpRepository skf2010Sc005GetShoninIchiranShoninExpRepository;
	@Autowired
	private Skf2010TApplHistoryRepository skf2010TApplHistoryRepository;
	@Autowired
	private Skf1010MShainRepository skf1010MShainRepository;
	@Autowired
	private Skf2010Sc005GetSendApplMailInfoExpRepository skf2010Sc005GetSendApplMailInfoExpRepository;
	@Autowired
	private Skf2010Sc005GetAgencyNameExpRepository skf2010Sc005GetAgencyNameExpRepository;
	@Autowired
	private Skf2010Sc005GetAffiliation1ListExpRepository skf2010Sc005GetAffiliation1ListExpRepository;
	@Autowired
	private Skf2010Sc005GetAffiliation2ListExpRepository skf2010Sc005GetAffiliation2ListExpRepository;
	@Autowired
	private Skf2010Sc005GetTeijiDataInfoExpRepository skf2010Sc005GetTeijiDataInfoExpRepository;
	@Autowired
	private Skf2010Sc005GetAgreeAuthorityCountExpRepository skf2010Sc005GetAgreeAuthorityCountExpRepository;
	@Autowired
	private Skf2010Sc005GetAgreeAuthorityGroupIdExpRepository skf2010Sc005GetAgreeAuthorityGroupIdExpRepository;
	@Autowired
	private Skf2010MApplicationRepository skf2010MApplicationRepository;
	@Autowired
	private Skf2010Sc005GetCommentInfoForUpdateExpRepository skf2010Sc005GetCommentInfoForUpdateExpRepository;
	@Autowired
	private Skf2010Sc005UpdateCommentInfoExpRepository skf2010Sc005UpdateCommentInfoExpRepository;
	@Autowired
	private Skf2010Sc005GetApplHistoryStatusInfoForUpdateExpRepository skf2010Sc005GetApplHistoryStatusInfoForUpdateExpRepository;
	@Autowired
	private Skf2010Sc005UpdateApplHistoryAgreeStatusExpRepository skf2010Sc005UpdateApplHistoryAgreeStatusExpRepository;
	@Autowired
	private Skf2020Sc003GetShatakuNyukyoKiboInfoExpRepository skf2020Sc003GetShatakuNyukyoKiboInfoExpRepository;
	@Autowired
	private SkfBatchUtils skfBatchUtils;
	@Autowired
	private SkfRollBackExpRepository skfRollBackExpRepository;

	@Autowired
	private Skf2020TNyukyoChoshoTsuchiRepository skf2020TNyukyoChoshoTsuchiRepository;
	@Autowired
	private Skf2040TTaikyoReportRepository skf2040TTaikyoReportRepository;
	@Autowired
	private Skf2030TBihinKiboShinseiRepository skf2030TBihinKiboShinseiRepository;
	@Autowired
	private Skf2050TBihinHenkyakuShinseiRepository skf2050TBihinHenkyakuShinseiRepository;
	@Autowired
	private SkfDropDownUtils skfDropDownUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private SkfTeijiDataInfoUtils skfTeijiDataInfoUtils;
	@Autowired
	private SkfCommentUtils skfCommentUtils;

	@Autowired
	private SkfBatchBusinessLogicUtils skfBatchBusinessLogicUtils;
	@Autowired
	private Skf2020Fc001NyukyoKiboSinseiDataImport skf2020Fc001NyukyoKiboSinseiDataImport;
	@Autowired
	private Skf2040Fc001TaikyoTodokeDataImport skf2040Fc001TaikyoTodokeDataImport;
	@Autowired
	private Skf2030Fc001BihinKiboShinseiDataImport skf2030Fc001BihinKiboShinseiDataImport;
	@Autowired
	private Skf2050Fc001BihinHenkyakuSinseiDataImport skf2050Fc001BihinHenkyakuSinseiDataImport;

	private String companyCd = CodeConstant.C001;
	private MenuScopeSessionBean menuScopeSessionBean;

	private Map<String, Object> changeWordMap = new HashMap<String, Object>();

	public void setMenuScopeSessionBean(MenuScopeSessionBean bean) {
		this.menuScopeSessionBean = bean;
	}

	public void setChangeWordMap(String name, Map<String, String> map) {
		changeWordMap.put(name, map);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getChangeWordMap(String name) {
		return (Map<String, String>) changeWordMap.get(name);
	}

	/**
	 * ドロップダウンリスト作成
	 * 
	 * @param dropDownMap
	 * @param companyCd
	 * @param agencyCd
	 * @param affiliation1Cd
	 * @param affiliation2Cd
	 */
	public void setDropDown(Skf2010Sc005CommonDto dto, String shozoku, String agencyCd, String affiliation1Cd,
			String affiliation2Cd) {
		List<Map<String, Object>> ddlAgencyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> ddlAffiliation1List = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> ddlAffiliation2List = new ArrayList<Map<String, Object>>();

		// 機関ドロップダウンを作成
		ddlAgencyList = skfDropDownUtils.getDdlAgencyByCd(companyCd, agencyCd, true);

		// 所属によりドロップダウン取得元変更
		if (CheckUtils.isEqual(shozoku, CodeConstant.SHOZOKU_SHINSEI)) {
			// 部等ドロップダウンを作成
			ddlAffiliation1List = skfDropDownUtils.getDdlBkAffiliation1ByCd(companyCd, agencyCd, affiliation1Cd, true);
			// 室・課等ドロップダウを作成
			ddlAffiliation2List = skfDropDownUtils.getDdlBkAffiliation2ByCd(companyCd, agencyCd, affiliation1Cd,
					affiliation2Cd, true);

		} else {
			// 部等ドロップダウンを作成
			ddlAffiliation1List = skfDropDownUtils.getDdlAffiliation1ByCd(companyCd, agencyCd, affiliation1Cd, true);
			// 室・課等ドロップダウを作成
			ddlAffiliation2List = skfDropDownUtils.getDdlAffiliation2ByCd(companyCd, agencyCd, affiliation1Cd,
					affiliation2Cd, true);
		}

		dto.setDdlAgencyList(ddlAgencyList);
		dto.setDdlAffiliation1List(ddlAffiliation1List);
		dto.setDdlAffiliation2List(ddlAffiliation2List);
	}

	/**
	 * 検索条件をセットします。
	 * 
	 * @param dto
	 * @return
	 */
	public Skf2010Sc005GetShoninIchiranShoninExpParameter setParam(Skf2010Sc005CommonDto dto) {
		Skf2010Sc005GetShoninIchiranShoninExpParameter param = new Skf2010Sc005GetShoninIchiranShoninExpParameter();
		// 会社コード
		param.setCompanyCd(companyCd);

		// 機関
		if (dto.getAgency() != null && !CheckUtils.isEmpty(dto.getAgency())) {
			param.setAgencyName(getAgencyName(companyCd, dto.getAgency()));
		}
		// 部等
		if (dto.getAffiliation1() != null && !CheckUtils.isEmpty(dto.getAffiliation1())) {
			param.setAffiliation1Name(getAffiliation1Name(companyCd, dto.getAgency(), dto.getAffiliation1()));

		}
		// 室、チーム
		if (dto.getAffiliation2() != null && !CheckUtils.isEmpty(dto.getAffiliation2())) {
			param.setAffiliation2Name(
					getAffiliation2Name(companyCd, dto.getAgency(), dto.getAffiliation1(), dto.getAffiliation2()));

		}
		// 所属機関
		param.setShozokuKikan(dto.getShozokuKikan());
		// 申請日時（FROM）
		if (dto.getApplDateFrom() != null && !CheckUtils.isEmpty(dto.getApplDateFrom())) {
			param.setApplDateFrom(skfDateFormatUtils.dateFormatFromString(dto.getApplDateFrom(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT));
		}
		// 申請日時（TO）
		if (dto.getApplDateTo() != null && !CheckUtils.isEmpty(dto.getApplDateTo())) {
			param.setApplDateTo(skfDateFormatUtils.dateFormatFromString(dto.getApplDateTo(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT));
		}
		// 承認日／修正依頼日（From）
		if (dto.getAgreDateFrom() != null && !CheckUtils.isEmpty(dto.getAgreDateFrom())) {
			param.setAgreDateFrom(skfDateFormatUtils.dateFormatFromString(dto.getAgreDateFrom(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT));
		}
		// 承認日／修正依頼日（To）
		if (dto.getAgreDateTo() != null && !CheckUtils.isEmpty(dto.getAgreDateTo())) {
			param.setAgreDateTo(skfDateFormatUtils.dateFormatFromString(dto.getAgreDateTo(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT));
		}
		// 社員番号
		if (dto.getShainNo() != null && !CheckUtils.isEmpty(dto.getShainNo())) {
			param.setShainNo(dto.getShainNo());
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
			param.setApplStatus(applStatusList);
		}
		// 承認者名
		if (dto.getAgreementName() != null && !CheckUtils.isEmpty(dto.getAgreementName())) {
			param.setAgreeName(dto.getAgreementName());
		}
		return param;
	}

	/**
	 * 申請情報履歴テーブルからデータを取得します
	 * 
	 * @param param
	 * @return
	 */
	public List<Skf2010Sc005GetShoninIchiranShoninExp> searchApplList(
			Skf2010Sc005GetShoninIchiranShoninExpParameter param) {
		List<Skf2010Sc005GetShoninIchiranShoninExp> tApplHistory = new ArrayList<Skf2010Sc005GetShoninIchiranShoninExp>();
		tApplHistory = skf2010Sc005GetShoninIchiranShoninExpRepository.getShoninIchiranShonin(param);

		return tApplHistory;
	}

	/**
	 * 申請状況を更新します
	 * 
	 * @param companyCd
	 * @param applNo
	 * @param dto
	 * @param commentDate
	 * @return
	 * @throws Exception
	 */
	public boolean updateApplStatus(String companyCd, String applNo, Skf2010Sc005CommonDto dto) throws Exception {
		if ((companyCd == null || CheckUtils.isEmpty(companyCd) || (applNo == null || CheckUtils.isEmpty(applNo)))) {
			return false;
		}
		// ログインユーザー情報取得
		Map<String, String> loginUserInfoMap = skfLoginUserInfoUtils.getSkfLoginUserInfo();

		Skf2010Sc005GetApplHistoryInfoByParameterExpParameter param = new Skf2010Sc005GetApplHistoryInfoByParameterExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		// 更新対象の申請情報を取得
		Skf2010Sc005GetApplHistoryInfoByParameterExp tApplHistoryData = new Skf2010Sc005GetApplHistoryInfoByParameterExp();
		tApplHistoryData = skf2010Sc005GetApplHistoryInfoByParameterExpRepository.getApplHistoryInfoByParameter(param);

		// 排他チェック
		Map<String, Date> lastUpdateDateMap = dto.getLastUpdateDateMap();
		Date lastUpdateDate = lastUpdateDateMap.get(applNo);
		if (!CheckUtils.isEqual(tApplHistoryData.getUpdateDate(), lastUpdateDate)) {
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1134);
			return false;
		}

		// 次のステータスを設定する
		Skf2010TApplHistory updateData = new Skf2010TApplHistory();

		String shoninName1 = CodeConstant.NONE;
		String shoninName2 = CodeConstant.NONE;
		String nextStatus = CodeConstant.NONE;
		String mailKbn = CodeConstant.NONE;
		String nextWorkflow = CodeConstant.NONE;
		String sendGroupId = CodeConstant.NONE;
		Map<String, String> applInfo = new HashMap<String, String>();
		Date agreeDate = null;

		Date updateDate = new Date();

		// プライマリキー設定
		updateData.setCompanyCd(companyCd);
		updateData.setShainNo(tApplHistoryData.getShainNo());
		updateData.setApplId(tApplHistoryData.getApplId());
		updateData.setApplNo(applNo);
		updateData.setApplDate(tApplHistoryData.getApplDate());

		// 申請情報設定
		applInfo.put("applId", tApplHistoryData.getApplId());
		applInfo.put("applNo", applNo);
		applInfo.put("applShainNo", tApplHistoryData.getShainNo());

		String nowApplStatus = tApplHistoryData.getApplStatus();
		switch (nowApplStatus) {
		case CodeConstant.STATUS_SHINSEICHU:
		case CodeConstant.STATUS_SHINSACHU:
		case CodeConstant.STATUS_DOI_ZUMI:
		case CodeConstant.STATUS_HANNYU_ZUMI:
		case CodeConstant.STATUS_HANSYUTSU_ZUMI:
			// 申請中、審査中、同意済、搬入済、搬出済
			nextStatus = CodeConstant.STATUS_SHONIN1;
			shoninName1 = loginUserInfoMap.get("userName");
			mailKbn = CodeConstant.SHONIN_IRAI_TSUCHI;
			nextWorkflow = CodeConstant.LEVEL_2;
			break;
		case CodeConstant.STATUS_SHONIN1:
			// 承認１
			nextStatus = CodeConstant.STATUS_SHONIN_ZUMI;
			shoninName2 = loginUserInfoMap.get("userName");
			mailKbn = CodeConstant.SHONIN_KANRYO_TSUCHI;
			agreeDate = updateDate;
			break;
		}

		if (NfwStringUtils.isNotEmpty(nextWorkflow)) {
			// 次のワークフロー設定がある場合、次権限グループを取得
			Skf2010Sc005GetAgreeAuthorityGroupIdExpParameter mApplParam = new Skf2010Sc005GetAgreeAuthorityGroupIdExpParameter();
			Skf2010Sc005GetAgreeAuthorityGroupIdExp mApplResult = new Skf2010Sc005GetAgreeAuthorityGroupIdExp();
			mApplParam.setCompanyCd(companyCd);
			mApplParam.setApplId(tApplHistoryData.getApplId());
			mApplParam.setWfLevel(nextWorkflow);
			mApplResult = skf2010Sc005GetAgreeAuthorityGroupIdExpRepository.getAgreeAuthorityGroupId(mApplParam);
			if (mApplResult != null) {
				sendGroupId = mApplResult.getRoleId();
			}
		}

		if (NfwStringUtils.isEmpty(sendGroupId)) {
			// 次階層が無い場合、承認済みの設定をする
			nextStatus = CodeConstant.STATUS_SHONIN_ZUMI;
			shoninName2 = loginUserInfoMap.get("userName");
			mailKbn = CodeConstant.SHONIN_KANRYO_TSUCHI;
			agreeDate = updateDate;
		}

		// 申請状況
		if (!CheckUtils.isEmpty(nextStatus)) {
			updateData.setApplStatus(nextStatus);
		}
		// 承認者名1
		if (!CheckUtils.isEmpty(shoninName1)) {
			updateData.setAgreName1(shoninName1);
		}
		// 承認者名2
		if (!CheckUtils.isEmpty(shoninName2)) {
			updateData.setAgreName2(shoninName2);
		}
		// 承認日
		if (agreeDate != null) {
			updateData.setAgreDate(updateDate);
		}

		// 申請情報履歴更新
		int applHistoryRes = skf2010TApplHistoryRepository.updateByPrimaryKeySelective(updateData);
		if (applHistoryRes <= 0) {
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
			return false;
		}

		// 申請書類IDが"R0100"【入退居希望申請】かつ次のステータスが承認中または承認済の場合、共益費、使用料を更新する
		if (tApplHistoryData.getApplId().equals(FunctionIdConstant.R0100)
				&& (CodeConstant.STATUS_SHONIN1.equals(nextStatus)
						|| CodeConstant.STATUS_SHONIN_ZUMI.equals(nextStatus))) {
			boolean res = updateShatakuRental(tApplHistoryData.getShainNo(), CodeConstant.NYUTAIKYO_KBN_NYUKYO,
					tApplHistoryData.getApplNo());
			if (!res) {
				return false;
			}

		}

		// 次階層のステータスが”31”【承認中】の場合、承認中コメントを削除
		if (nextStatus.equals(CodeConstant.STATUS_SHONIN1)) {

			Skf2010TApplCommentKey key = new Skf2010TApplCommentKey();
			key.setCompanyCd(companyCd);
			key.setApplNo(applNo);
			key.setApplStatus(nextStatus);
			Map<String, String> errorMsg = new HashMap<String, String>();

			boolean delComRes = skfCommentUtils.deleteComment(companyCd, applNo, nextStatus, errorMsg);
			if (!delComRes) {
				return false;
			}
		}
		// 次階層のステータスが”40”【承認済】の場合、承認中コメントを承認済みコメントに更新
		Skf2010Sc005GetCommentInfoForUpdateExp tmpTApplCommentData = new Skf2010Sc005GetCommentInfoForUpdateExp();
		if (nextStatus.equals(CodeConstant.STATUS_SHONIN_ZUMI)) {
			Skf2010Sc005GetCommentInfoForUpdateExpParameter tApplCommentParam = new Skf2010Sc005GetCommentInfoForUpdateExpParameter();
			tApplCommentParam.setCompanyCd(companyCd);
			tApplCommentParam.setApplNo(applNo);
			tApplCommentParam.setApplStatus(nowApplStatus);
			tmpTApplCommentData = skf2010Sc005GetCommentInfoForUpdateExpRepository
					.getCommentInfoForUpdate(tApplCommentParam);
			if (tmpTApplCommentData != null) {
				Skf2010Sc005UpdateCommentInfoExp updData = new Skf2010Sc005UpdateCommentInfoExp();
				updData.setCompanyCd(companyCd);
				updData.setApplNo(applNo);
				updData.setUpdateApplStatus(nextStatus);
				updData.setApplStatus(nowApplStatus);
				updData.setCommentDate(tmpTApplCommentData.getCommentDate());
				int updComRes = skf2010Sc005UpdateCommentInfoExpRepository.updateCommentInfo(updData);
				if (updComRes <= 0) {
					return false;
				}
			}
		}
		// 承認完了通知の場合のみ
		if (CheckUtils.isEqual(mailKbn, CodeConstant.SHONIN_KANRYO_TSUCHI)) {
			// メール送付
			String comment = CodeConstant.NONE;
			String annai = CodeConstant.NONE;
			String furikomiStartDate = CodeConstant.NONE;
			String sendUser = applInfo.get("applShainNo");
			sendApplTsuchiMail(mailKbn, applInfo, comment, annai, furikomiStartDate, sendUser, sendGroupId);
		}

		// 社宅データ連携
		String shainNo = tApplHistoryData.getShainNo();
		String applId = tApplHistoryData.getApplId();
		String pageId = FunctionIdConstant.SKF2050_SC001;
		List<String> resultBatch = doShatakuRenkei(menuScopeSessionBean, shainNo, applNo, nextStatus, applId, pageId);
		if (resultBatch != null) {
			skfBatchBusinessLogicUtils.addResultMessageForDataLinkage(dto, resultBatch);
			skfRollBackExpRepository.rollBack();
			return false;
		}

		return true;
	}

	/**
	 * 申請情報からリストテーブルのデータを作成します
	 * 
	 * @param tApplHistoryData
	 * @return
	 */
	public List<Map<String, Object>> createListTable(List<Skf2010Sc005GetShoninIchiranShoninExp> tApplHistoryData,
			Skf2010Sc005CommonDto dto) {
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		if (tApplHistoryData.size() <= 0) {
			return returnList;
		}

		// 汎用コード取得
		Map<String, String> genericCodeMap = new HashMap<String, String>();
		genericCodeMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_STATUS);

		Map<String, Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>>> forUpdateListMap = new HashMap<String, Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>>>();
		Map<String, Date> lastUpdateDateMap = new HashMap<String, Date>();

		for (Skf2010Sc005GetShoninIchiranShoninExp tmpData : tApplHistoryData) {
			String applDate = CodeConstant.NONE;
			String agreDate = CodeConstant.NONE;
			String applStatus = CodeConstant.NONE;
			// 承認チェックフラグ
			boolean chkSelectAgreeTarget = true;

			// 承認チェックフラグ判定
			chkSelectAgreeTarget = checkAgree(tmpData);
			String applNo = tmpData.getApplNo();

			String baseChkBoxTag = "<input type=\"checkbox\" name=\"applNo\" id=\"applNo_$applNo$\" value=\"$applNo$\" $disabled$ />";
			String chkBoxTag = baseChkBoxTag.replace("$applNo$", applNo);
			if (chkSelectAgreeTarget) {
				chkBoxTag = chkBoxTag.replace("$disabled$", CodeConstant.NONE);
			} else {
				chkBoxTag = chkBoxTag.replace("$disabled$", "disabled=\"true\"");
			}

			// 日付型を文字列型に変更する
			if (tmpData.getApplDate() != null) {
				applDate = skfDateFormatUtils.dateFormatFromDate(tmpData.getApplDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}
			if (tmpData.getAgreDate() != null) {
				agreDate = skfDateFormatUtils.dateFormatFromDate(tmpData.getAgreDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}
			// 一時保存は申請日を表示しない
			if (CodeConstant.STATUS_ICHIJIHOZON.equals(tmpData.getApplStatus())) {
				applDate = CodeConstant.NONE;
			}

			// 申請状況をコードから汎用コードに変更
			if (tmpData.getApplStatus() != null) {
				applStatus = genericCodeMap.get(tmpData.getApplStatus());
			}

			Map<String, Object> tmpMap = new HashMap<String, Object>();
			tmpMap.put("chkBox", chkBoxTag); // チェックボックス
			tmpMap.put("applId", tmpData.getApplId()); // 申請書ID
			tmpMap.put("applStatusCd", tmpData.getApplStatus()); // 申請状況コード
			tmpMap.put("applStatus", applStatus); // 申請状況
			tmpMap.put("applNo", tmpData.getApplNo()); // 申請書番号
			tmpMap.put("applDate", applDate); // 申請日
			tmpMap.put("shainNo", tmpData.getShainNo()); // 社員番号
			tmpMap.put("name", tmpData.getName()); // 申請者名
			tmpMap.put("applName", tmpData.getApplName()); // 申請書類名
			tmpMap.put("agreDate", agreDate); // 承認/修正依頼日
			tmpMap.put("agreName1", tmpData.getAgreName1()); // 承認者名１/修正依頼社名
			tmpMap.put("agreName2", tmpData.getAgreName2()); // 承認者名２/修正依頼社名
			tmpMap.put("detail", CodeConstant.NONE); // 確認ボタン（アイコン）

			returnList.add(tmpMap);

			// 社宅連携用データセット作成
			Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> forUpdateMap = skfBatchUtils
					.getUpdateDateForUpdateSQL(tmpData.getShainNo());
			forUpdateListMap.put(tmpData.getShainNo(), forUpdateMap);

			// 排他処理用最終更新日セット
			lastUpdateDateMap.put(tmpData.getApplNo(), tmpData.getUpdateDate());
		}
		menuScopeSessionBean.put(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2010SC005, forUpdateListMap);

		dto.setLastUpdateDateMap(lastUpdateDateMap);
		return returnList;
	}

	/**
	 * 機関名を取得します
	 * 
	 * @param companyCd
	 * @param agencyCd
	 * @return
	 */
	public String getAgencyName(String companyCd, String agencyCd) {
		String agencyName = CodeConstant.NONE;
		Skf2010Sc005GetAgencyNameExp agencyData = new Skf2010Sc005GetAgencyNameExp();
		Skf2010Sc005GetAgencyNameExpParameter param = new Skf2010Sc005GetAgencyNameExpParameter();
		param.setCompanyCd(companyCd);
		param.setAgencyCd(agencyCd);

		agencyData = skf2010Sc005GetAgencyNameExpRepository.getAgencyName(param);
		if (agencyData != null) {
			agencyName = agencyData.getAgencyName();
		}

		return agencyName;
	}

	/**
	 * 部等の名称を取得します
	 * 
	 * @param companyCd
	 * @param agencyCd
	 * @param affiliation1Cd
	 * @return
	 */
	public String getAffiliation1Name(String companyCd, String agencyCd, String affiliation1Cd) {
		List<Skf2010Sc005GetAffiliation1ListExp> affiliation1MapList = new ArrayList<Skf2010Sc005GetAffiliation1ListExp>();
		String affiliation1Name = CodeConstant.NONE;

		if (affiliation1Cd == null || CheckUtils.isEmpty(affiliation1Cd)) {
			return affiliation1Name;
		}
		Skf2010Sc005GetAffiliation1ListExpParameter param = new Skf2010Sc005GetAffiliation1ListExpParameter();
		param.setCompanyCd(companyCd);
		param.setAgencyCd(agencyCd);
		param.setAffiliation1Cd(affiliation1Cd);
		affiliation1MapList = skf2010Sc005GetAffiliation1ListExpRepository.getAffiliation1List(param);

		if (affiliation1MapList.size() > 0) {
			Skf2010Sc005GetAffiliation1ListExp tmpData = affiliation1MapList.get(0);
			affiliation1Name = tmpData.getAffiliation1Name();
		}

		return affiliation1Name;
	}

	/**
	 * 室・課等の名称を取得します
	 * 
	 * @param companyCd
	 * @param agencyCd
	 * @param affiliation1Cd
	 * @param affiliation2Cd
	 * @return
	 */
	public String getAffiliation2Name(String companyCd, String agencyCd, String affiliation1Cd, String affiliation2Cd) {
		List<Skf2010Sc005GetAffiliation2ListExp> affiliation2MapList = new ArrayList<Skf2010Sc005GetAffiliation2ListExp>();
		String affiliation2Name = CodeConstant.NONE;

		if (affiliation1Cd == null || CheckUtils.isEmpty(affiliation1Cd)) {
			return affiliation2Name;
		}
		Skf2010Sc005GetAffiliation2ListExpParameter param = new Skf2010Sc005GetAffiliation2ListExpParameter();
		param.setCompanyCd(companyCd);
		param.setAgencyCd(agencyCd);
		param.setAffiliation1Cd(affiliation1Cd);
		param.setAffiliation2Cd(affiliation2Cd);
		affiliation2MapList = skf2010Sc005GetAffiliation2ListExpRepository.getAffiliation2List(param);

		if (affiliation2MapList.size() > 0) {
			Skf2010Sc005GetAffiliation2ListExp tmpData = affiliation2MapList.get(0);
			affiliation2Name = tmpData.getAffiliation2Name();
		}

		return affiliation2Name;
	}

	/**
	 * 社宅入居等申請調書データをマッピングします
	 * 
	 * @param companyCd
	 * @param applNo
	 * @param titleList
	 * @param strList
	 */
	public void setTNyukyoChoshoTsuchi(String companyCd, String applNo, List<String> titleList, List<String> strList) {
		Skf2020TNyukyoChoshoTsuchiKey skf2020TNyukyoChoshoTsuchiKey = new Skf2020TNyukyoChoshoTsuchiKey();
		skf2020TNyukyoChoshoTsuchiKey.setCompanyCd(companyCd);
		skf2020TNyukyoChoshoTsuchiKey.setApplNo(applNo);
		Skf2020TNyukyoChoshoTsuchi result = skf2020TNyukyoChoshoTsuchiRepository
				.selectByPrimaryKey(skf2020TNyukyoChoshoTsuchiKey);
		if (result != null) {

			// 日付にスラッシュを追加
			String nyukyoYoteiDate = CodeConstant.NONE;
			if (result.getNyukyoYoteiDate() != null) {
				nyukyoYoteiDate = skfDateFormatUtils.dateFormatFromString(result.getNyukyoYoteiDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}

			String carExpirationDate = CodeConstant.NONE;
			if (result.getCarExpirationDate() != null) {
				carExpirationDate = skfDateFormatUtils.dateFormatFromString(result.getCarExpirationDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}

			String parkingUseDate = CodeConstant.NONE;
			if (result.getParkingUseDate() != null) {
				parkingUseDate = skfDateFormatUtils.dateFormatFromString(result.getParkingUseDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}

			String taikyoYoteiDate = CodeConstant.NONE;
			if (result.getTaikyoYoteiDate() != null) {
				taikyoYoteiDate = skfDateFormatUtils.dateFormatFromString(result.getTaikyoYoteiDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}

			String nyukyoKanoDate = CodeConstant.NONE;
			if (result.getNyukyoKanoDate() != null) {
				nyukyoKanoDate = skfDateFormatUtils.dateFormatFromString(result.getNyukyoKanoDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}

			String parkingKanoDate = CodeConstant.NONE;
			if (result.getParkingKanoDate() != null) {
				parkingKanoDate = skfDateFormatUtils.dateFormatFromString(result.getParkingKanoDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}

			String seiyakuDate = CodeConstant.NONE;
			if (result.getSeiyakuDate() != null) {
				seiyakuDate = skfDateFormatUtils.dateFormatFromString(result.getSeiyakuDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}

			String tsuchiDate = CodeConstant.NONE;
			if (result.getTsuchiDate() != null) {
				tsuchiDate = skfDateFormatUtils.dateFormatFromString(result.getTsuchiDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}

			String carExpirationDate2 = CodeConstant.NONE;
			if (result.getCarExpirationDate2() != null) {
				carExpirationDate2 = skfDateFormatUtils.dateFormatFromString(result.getCarExpirationDate2(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}

			String parkingUseDate2 = CodeConstant.NONE;
			if (result.getParkingUseDate2() != null) {
				parkingUseDate2 = skfDateFormatUtils.dateFormatFromString(result.getParkingUseDate2(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}

			String parkingKanoDate2 = CodeConstant.NONE;
			if (result.getParkingKanoDate2() != null) {
				parkingKanoDate2 = skfDateFormatUtils.dateFormatFromString(result.getParkingKanoDate2(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}

			titleList.add("company_cd");
			strList.add(result.getCompanyCd());
			titleList.add("agency");
			strList.add(result.getAgency());
			titleList.add("affiliation1");
			strList.add(result.getAffiliation1());
			titleList.add("affiliation2");
			strList.add(result.getAffiliation2());

			titleList.add("taiyo_hitsuyo");
			strList.add(result.getTaiyoHitsuyo());
			titleList.add("hitsuyo_riyu");
			strList.add(result.getHitsuyoRiyu());
			titleList.add("fuhitsuyo_riyu");
			strList.add(result.getFuhitsuyoRiyu());
			titleList.add("tokyu");
			strList.add(result.getTokyu());
			titleList.add("tel");
			strList.add(result.getTel());
			titleList.add("new_agency_other");
			strList.add(result.getNewAgencyOther());
			titleList.add("new_agency");
			strList.add(result.getNewAgency());
			titleList.add("new_affiliation1_other");
			strList.add(result.getNewAffiliation1Other());
			titleList.add("new_affiliation1");
			strList.add(result.getNewAffiliation1());
			titleList.add("new_affiliation2_other");
			strList.add(result.getNewAffiliation2Other());
			titleList.add("new_affiliation2");
			strList.add(result.getNewAffiliation2());
			titleList.add("hitsuyo_shataku");
			strList.add(result.getHitsuyoShataku());
			titleList.add("dokyo_relation1");
			strList.add(result.getDokyoRelation1());
			titleList.add("dokyo_name1");
			strList.add(result.getDokyoName1());
			titleList.add("dokyo_age1");
			strList.add(result.getDokyoAge1());
			titleList.add("dokyo_relation2");
			strList.add(result.getDokyoRelation2());
			titleList.add("dokyo_name2");
			strList.add(result.getDokyoName2());
			titleList.add("dokyo_age2");
			strList.add(result.getDokyoAge2());
			titleList.add("dokyo_relation3");
			strList.add(result.getDokyoRelation3());
			titleList.add("dokyo_name3");
			strList.add(result.getDokyoName3());
			titleList.add("dokyo_age3");
			strList.add(result.getDokyoAge3());
			titleList.add("dokyo_relation4");
			strList.add(result.getDokyoRelation4());
			titleList.add("dokyo_name4");
			strList.add(result.getDokyoName4());
			titleList.add("dokyo_age4");
			strList.add(result.getDokyoAge4());
			titleList.add("dokyo_relation5");
			strList.add(result.getDokyoRelation5());
			titleList.add("dokyo_name5");
			strList.add(result.getDokyoName5());
			titleList.add("dokyo_age5");
			strList.add(result.getDokyoAge5());
			titleList.add("dokyo_relation6");
			strList.add(result.getDokyoRelation6());
			titleList.add("dokyo_name6");
			strList.add(result.getDokyoName6());
			titleList.add("dokyo_age6");
			strList.add(result.getDokyoAge6());
			titleList.add("nyukyo_yotei_date");
			strList.add(nyukyoYoteiDate);
			titleList.add("parking_umu");
			strList.add(result.getParkingUmu());
			titleList.add("car_no_input_flg");
			strList.add(result.getCarNoInputFlg());
			titleList.add("car_name");
			strList.add(result.getCarName());
			titleList.add("car_no");
			strList.add(result.getCarNo());
			titleList.add("car_expiration_date");
			strList.add(carExpirationDate);
			titleList.add("car_user");
			strList.add(result.getCarUser());
			titleList.add("parking_use_date");
			strList.add(parkingUseDate);
			titleList.add("now_shataku");
			strList.add(result.getNowShataku());
			titleList.add("now_shataku_name");
			strList.add(result.getNowShatakuName());
			titleList.add("now_shataku_no");
			strList.add(result.getNowShatakuNo());
			titleList.add("now_shataku_kikaku");
			Map<String, String> kikakuKbnList = this.getChangeWordMap("kikakuKbn");
			strList.add(kikakuKbnList.get(result.getNowShatakuKikaku()));
			titleList.add("now_shataku_menseki");
			strList.add(result.getNowShatakuMenseki());
			titleList.add("taikyo_yotei");
			strList.add(result.getTaikyoYotei());
			titleList.add("taikyo_yotei_date");
			strList.add(taikyoYoteiDate);
			titleList.add("taikyo_riyu");
			strList.add(result.getTaikyoRiyu());
			titleList.add("taikyo_riyu_kbn");
			strList.add(result.getTaikyoRiyuKbn());
			titleList.add("taikyogo_renrakusaki");
			strList.add(result.getTaikyogoRenrakusaki());
			titleList.add("tokushu_jijo");
			strList.add(result.getTokushuJijo());
			titleList.add("new_shataku_kanri_no");
			strList.add(result.getNewShatakuKanriNo());
			titleList.add("new_shozaichi");
			strList.add(result.getNewShozaichi());
			titleList.add("new_shataku_name");
			strList.add(result.getNewShatakuName());
			titleList.add("new_shataku_no");
			strList.add(result.getNewShatakuNo());
			titleList.add("new_shataku_kikaku");
			strList.add(kikakuKbnList.get(result.getNewShatakuKikaku()));
			titleList.add("new_shataku_menseki");
			strList.add(result.getNewShatakuMenseki());
			titleList.add("new_rental");
			strList.add(result.getNewRental());
			titleList.add("kyoekihi_person_kyogichu_flg");
			strList.add(result.getKyoekihiPersonKyogichuFlg());
			titleList.add("new_kyoekihi");
			strList.add(result.getNewKyoekihi());
			titleList.add("nyukyo_kano_date");
			strList.add(nyukyoKanoDate);
			titleList.add("parking_area");
			strList.add(result.getParkingArea());
			titleList.add("car_ichi_no");
			strList.add(CodeConstant.NONE);
			titleList.add("parking_rental");
			strList.add(result.getParkingRental());
			titleList.add("parking_kano_date");
			strList.add(parkingKanoDate);
			titleList.add("seiyaku_date");
			strList.add(seiyakuDate);
			titleList.add("tsuchi_date");
			strList.add(tsuchiDate);
			titleList.add("combo_flg");
			strList.add(result.getComboFlg());
			titleList.add("import_flg");
			strList.add(result.getImportFlg());
			titleList.add("nyukyo_date_flg");
			strList.add(result.getNyukyoDateFlg());
			titleList.add("taikyo_date_flg");
			strList.add(result.getTaikyoDateFlg());
			titleList.add("parking_s_date_flg");
			strList.add(result.getParkingSDateFlg());
			titleList.add("gender");
			String strGender = CodeConstant.NONE;
			if (result.getGender() != null) {
				Map<String, String> genderMap = this.getChangeWordMap("gender");

				strGender = genderMap.get(result.getGender().toString());
			}
			strList.add(strGender);
			titleList.add("shataku_no");
			String strShatakuNo = CodeConstant.NONE;
			if (result.getShatakuNo() != null) {
				strShatakuNo = result.getShatakuNo().toString();
			}
			strList.add(strShatakuNo);
			titleList.add("room_kanri_no");
			String strRoomKanriNo = CodeConstant.NONE;
			if (result.getRoomKanriNo() != null) {
				strRoomKanriNo = result.getRoomKanriNo().toString();
			}
			strList.add(strRoomKanriNo);
			titleList.add("car_no_input_flg2");
			strList.add(result.getCarNoInputFlg2());
			titleList.add("car_name2");
			strList.add(result.getCarName2());
			titleList.add("car_no2");
			strList.add(result.getCarNo2());
			titleList.add("car_expiration_date2");
			strList.add(carExpirationDate2);
			titleList.add("car_user2");
			strList.add(result.getCarUser2());
			titleList.add("parking_use_date2");
			strList.add(parkingUseDate2);
			titleList.add("shataku_jotai");
			strList.add(result.getShatakuJotai());
			titleList.add("session_day");
			strList.add(skfDateFormatUtils.dateFormatFromString(result.getSessionDay(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH));
			titleList.add("session_time");
			Map<String, String> requestTime = this.getChangeWordMap("requestTime");
			strList.add(requestTime.get(result.getSessionTime()));
			titleList.add("renraku_saki");
			strList.add(result.getRenrakuSaki());
			titleList.add("parking_area2");
			strList.add(result.getParkingArea2());
			titleList.add("car_ichi_no2");
			strList.add(CodeConstant.NONE);
			titleList.add("parking_rental2");
			strList.add(result.getParkingRental2());
			titleList.add("parking_kano_date2");
			strList.add(parkingKanoDate2);
			titleList.add("bihin_kibo");
			strList.add(result.getBihinKibo());
			titleList.add("key_manager");
			strList.add(result.getKeyManager());
			titleList.add("key_manager_tel");
			strList.add(result.getKeyManagerTel());
			titleList.add("now_parking_area");
			strList.add(result.getNowParkingArea());
			titleList.add("now_car_ichi_no");
			strList.add(result.getNowCarIchiNo());
			titleList.add("now_parking_area2");
			strList.add(result.getNowParkingArea2());
			titleList.add("now_car_ichi_no2");
			strList.add(result.getNowCarIchiNo2());
			titleList.add("now_shataku_kanri_no");
			String strNowShatakuKanriNo = CodeConstant.NONE;
			if (result.getNowShatakuKanriNo() != null) {
				strNowShatakuKanriNo = result.getNowShatakuKanriNo().toString();
			}
			strList.add(strNowShatakuKanriNo);
		}
		return;
	}

	/**
	 * 退居（自動車の保管場所返還）届のデータをマッピングします
	 * 
	 * @param companyCd
	 * @param applNo
	 * @param titleList
	 * @param strList
	 */
	public void setTTaikyoReport(String companyCd, String applNo, List<String> titleList, List<String> strList) {
		Skf2040TTaikyoReport result = new Skf2040TTaikyoReport();
		Skf2040TTaikyoReportKey param = new Skf2040TTaikyoReportKey();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		result = skf2040TTaikyoReportRepository.selectByPrimaryKey(param);
		if (result != null) {

			// 日付にスラッシュを追加
			String taikyoDate = CodeConstant.NONE;
			if (result.getTaikyoDate() != null) {
				taikyoDate = skfDateFormatUtils.dateFormatFromString(result.getTaikyoDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}

			String parkingHenkanDate = CodeConstant.NONE;
			if (result.getParkingHenkanDate() != null) {
				parkingHenkanDate = skfDateFormatUtils.dateFormatFromString(result.getParkingHenkanDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}

			String sessionDay = CodeConstant.NONE;
			if (result.getSessionDay() != null) {
				sessionDay = skfDateFormatUtils.dateFormatFromString(result.getSessionDay(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}

			titleList.add("company_cd");
			strList.add(result.getCompanyCd());
			titleList.add("agency");
			strList.add(result.getAgency());
			titleList.add("affiliation1");
			strList.add(result.getAffiliation1());
			titleList.add("affiliation2");
			strList.add(result.getAffiliation2());

			titleList.add("gender");
			Map<String, String> genderMap = this.getChangeWordMap("gender");
			strList.add(genderMap.get(result.getGender()));
			titleList.add("address");
			strList.add(result.getAddress());
			titleList.add("taikyo_date");
			strList.add(taikyoDate);
			titleList.add("parking_henkan_date");
			strList.add(parkingHenkanDate);
			titleList.add("shataku_taikyo_kbn");
			strList.add(result.getShatakuTaikyoKbn());
			titleList.add("taikyo_area");
			strList.add(result.getTaikyoArea());
			titleList.add("taikyo_riyu");
			strList.add(result.getTaikyoRiyu());
			titleList.add("taikyo_riyu_kbn");
			strList.add(result.getTaikyoRiyuKbn());
			titleList.add("taikyogo_renrakusaki");
			strList.add(result.getTaikyogoRenrakusaki());
			titleList.add("taikyo_date_flg");
			strList.add(result.getTaikyoDateFlg());
			titleList.add("parking_e_date_flg");
			strList.add(result.getParkingEDateFlg());
			titleList.add("shataku_no");
			String strShatakuNo = CodeConstant.NONE;
			if (result.getShatakuNo() != null) {
				strShatakuNo = result.getShatakuNo().toString();
			}
			strList.add(strShatakuNo);
			titleList.add("room_kanri_no");
			String strRoomKanriNo = CodeConstant.NONE;
			if (result.getRoomKanriNo() != null) {
				strRoomKanriNo = result.getRoomKanriNo().toString();
			}
			strList.add(strRoomKanriNo);
			titleList.add("shataku_taikyo_kbn2");
			strList.add(result.getShatakuTaikyoKbn2());
			titleList.add("taikyo_shataku");
			strList.add(result.getTaikyoShataku());
			titleList.add("taikyo_parking1");
			strList.add(result.getTaikyoParking1());
			titleList.add("taikyo_parking2");
			strList.add(result.getTaikyoParking2());
			titleList.add("shataku_jotai");
			strList.add(result.getShatakuJotai());
			titleList.add("session_day");
			strList.add(sessionDay);
			titleList.add("session_time");
			Map<String, String> requestTime = this.getChangeWordMap("requestTime");
			strList.add(requestTime.get(result.getSessionTime()));
			titleList.add("renraku_saki");
			strList.add(result.getRenrakuSaki());
			titleList.add("parking_address1");
			strList.add(result.getParkingAddress1());
			titleList.add("parking_address2");
			strList.add(result.getParkingAddress2());
		}
		return;
	}

	/**
	 * 備品希望申請のデータをマッピングします
	 * 
	 * @param companyCd
	 * @param applNo
	 * @param titleList
	 * @param strList
	 */
	public void setTBihinKibouShinsei(String companyCd, String applNo, SkfBihinInfoUtilsGetBihinInfoExp bihinInfo,
			List<String> titleList, List<String> strList) {
		Skf2030TBihinKiboShinsei result = new Skf2030TBihinKiboShinsei();
		Skf2030TBihinKiboShinseiKey param = new Skf2030TBihinKiboShinseiKey();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		result = skf2030TBihinKiboShinseiRepository.selectByPrimaryKey(param);
		if (result != null) {
			// 日付にスラッシュを追加
			String sessionDay = CodeConstant.NONE;
			if (result.getSessionDay() != null) {
				sessionDay = skfDateFormatUtils.dateFormatFromString(result.getSessionDay(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}

			String completionDay = CodeConstant.NONE;
			if (result.getCompletionDay() != null) {
				completionDay = skfDateFormatUtils.dateFormatFromString(result.getCompletionDay(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}

			titleList.add("company_cd");
			strList.add(result.getCompanyCd());
			titleList.add("nyukyo_appl_no");
			strList.add(result.getNyukyoApplNo());
			titleList.add("agency");
			strList.add(result.getAgency());
			titleList.add("affiliation1");
			strList.add(result.getAffiliation1());
			titleList.add("affiliation2");
			strList.add(result.getAffiliation2());
			titleList.add("gender");
			Map<String, String> genderMap = this.getChangeWordMap("gender");
			strList.add(genderMap.get(result.getGender()));
			titleList.add("tel");
			strList.add(result.getTel());
			titleList.add("tokyu");
			strList.add(result.getTokyu());
			titleList.add("shataku_no");
			String strShatakuNo = CodeConstant.NONE;
			if (result.getRoomKanriNo() != null) {
				strShatakuNo = result.getShatakuNo().toString();
			}
			strList.add(strShatakuNo);
			titleList.add("room_kanri_no");
			String strRoomKanriNo = CodeConstant.NONE;
			if (result.getRoomKanriNo() != null) {
				strRoomKanriNo = result.getRoomKanriNo().toString();
			}
			strList.add(strRoomKanriNo);
			titleList.add("now_shataku_name");
			strList.add(result.getNowShatakuName());
			titleList.add("now_shataku_no");
			strList.add(result.getNowShatakuNo());
			titleList.add("now_shataku_kikaku");
			Map<String, String> kikakuKbnMap = this.getChangeWordMap("kikakuKbn");
			strList.add(kikakuKbnMap.get(result.getNowShatakuKikaku()));
			titleList.add("now_shataku_menseki");
			strList.add(result.getNowShatakuMenseki());
			titleList.add("renraku_saki");
			strList.add(result.getRenrakuSaki());
			titleList.add("ukeire_dairi_name");
			strList.add(result.getUkeireDairiName());
			titleList.add("ukeire_dairi_apoint");
			strList.add(result.getUkeireDairiApoint());
			titleList.add("session_day");
			strList.add(sessionDay);
			titleList.add("session_time");
			Map<String, String> kiboTimeMap = this.getChangeWordMap("requestTime");
			strList.add(kiboTimeMap.get(result.getSessionTime()));
			titleList.add("completion_day");
			strList.add(completionDay);

			// 以降備品申請データ
			this.setBihinShinseiDataList(titleList, strList, bihinInfo, FunctionIdConstant.R0104);

		}
		return;
	}

	/**
	 * 備品返却申請のデータをマッピングします
	 * 
	 * @param companyCd
	 * @param applNo
	 * @param titleList
	 * @param strList
	 */
	public void setTBihinHenkyakuShinsei(String companyCd, String applNo, SkfBihinInfoUtilsGetBihinInfoExp bihinInfo,
			List<String> titleList, List<String> strList) {
		Skf2050TBihinHenkyakuShinsei result = new Skf2050TBihinHenkyakuShinsei();
		Skf2050TBihinHenkyakuShinseiKey param = new Skf2050TBihinHenkyakuShinseiKey();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		result = skf2050TBihinHenkyakuShinseiRepository.selectByPrimaryKey(param);
		if (result != null) {
			// 日付にスラッシュを追加
			String sessionDay = CodeConstant.NONE;
			if (result.getSessionDay() != null) {
				sessionDay = skfDateFormatUtils.dateFormatFromString(result.getSessionDay(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}

			String completionDay = CodeConstant.NONE;
			if (result.getCompletionDay() != null) {
				completionDay = skfDateFormatUtils.dateFormatFromString(result.getCompletionDay(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}

			titleList.add("company_cd");
			strList.add(result.getCompanyCd());
			titleList.add("taikyo_appl_no");
			strList.add(result.getTaikyoApplNo());
			titleList.add("agency");
			strList.add(result.getAgency());
			titleList.add("affiliation1");
			strList.add(result.getAffiliation1());
			titleList.add("affiliation2");
			strList.add(result.getAffiliation2());
			titleList.add("tel");
			strList.add(result.getTel());
			titleList.add("tokyu");
			strList.add(result.getTokyu());
			titleList.add("gender");
			Map<String, String> genderMap = this.getChangeWordMap("gender");
			strList.add(genderMap.get(result.getGender()));
			titleList.add("shataku_no");
			String strShatakuNo = CodeConstant.NONE;
			if (result.getShatakuNo() != null) {
				strShatakuNo = result.getShatakuNo().toString();
			}
			strList.add(strShatakuNo);
			titleList.add("room_kanri_no");
			String strRoomKanriNo = CodeConstant.NONE;
			if (result.getRoomKanriNo() != null) {
				strRoomKanriNo = result.getRoomKanriNo().toString();
			}
			strList.add(strRoomKanriNo);
			titleList.add("now_shataku_name");
			strList.add(result.getNowShatakuName());
			titleList.add("now_shataku_no");
			strList.add(result.getNowShatakuNo());
			titleList.add("now_shataku_kikaku");
			Map<String, String> kikakuKbnMap = this.getChangeWordMap("kikakuKbn");
			strList.add(kikakuKbnMap.get(result.getNowShatakuKikaku()));
			titleList.add("now_shataku_menseki");
			strList.add(result.getNowShatakuMenseki());
			titleList.add("renraku_saki");
			strList.add(result.getRenrakuSaki());
			titleList.add("tatiai_dairi_name");
			strList.add(result.getTatiaiDairiName());
			titleList.add("tatiai_dairi_apoint");
			strList.add(result.getTatiaiDairiApoint());
			titleList.add("session_day");
			strList.add(sessionDay);
			titleList.add("session_time");
			Map<String, String> requestTimeMap = this.getChangeWordMap("requestTime");
			strList.add(requestTimeMap.get(result.getSessionTime()));
			titleList.add("completion_day");
			strList.add(completionDay);

			// 以降備品申請データ
			this.setBihinShinseiDataList(titleList, strList, bihinInfo, FunctionIdConstant.R0105);
		}
		return;
	}

	/**
	 * 備品申請情報をCSVデータリストにセットする
	 * 
	 * @param titleList
	 * @param strList
	 * @param bihinInfo
	 */
	public void setBihinShinseiDataList(List<String> titleList, List<String> strList,
			SkfBihinInfoUtilsGetBihinInfoExp bihinInfo, String applId) {
		// 備品コード
		titleList.add("bihin_cd");
		strList.add(bihinInfo.getBihinCd());
		// 備品名
		titleList.add("bihin_name");
		strList.add(bihinInfo.getBihinName());
		// 備品申請区分
		titleList.add("bihin_appl");
		Map<String, String> applKbnMap = this.getChangeWordMap("applKbn");
		strList.add(applKbnMap.get(bihinInfo.getBihinAppl()));
		// 備品状態区分
		titleList.add("bihin_status");
		Map<String, String> jotaiKbnMap = this.getChangeWordMap("jotaiKbn");
		strList.add(jotaiKbnMap.get(bihinInfo.getBihinState()));
		// 備品調整区分
		titleList.add("bihin_adjust");
		if (NfwStringUtils.isNotEmpty(bihinInfo.getBihinAdjust())) {
			Map<String, String> choseiKbn = new HashMap<String, String>();
			// 備品申請であれば搬入区分、備品返却であれば搬出区分を取得
			if (CheckUtils.isEqual(applId, FunctionIdConstant.R0104)) {
				choseiKbn = this.getChangeWordMap("hannyuKbn");
			} else {
				choseiKbn = this.getChangeWordMap("hanshutsuKbn");
			}
			String choseiVal = choseiKbn.get(bihinInfo.getBihinAdjust());
			if (NfwStringUtils.isEmpty(choseiVal)) {
				choseiVal = CodeConstant.HYPHEN;
			}
			strList.add(choseiVal);
		} else {
			strList.add(CodeConstant.HYPHEN);
		}
		// 備品希望可否区分
		titleList.add("bihin_hope");
		Map<String, String> wishKbnMap = this.getChangeWordMap("wishKbn");
		strList.add(wishKbnMap.get(bihinInfo.getBihinHope()));

		return;
	}

	public boolean isAgreeAuthority(String applId, String roleId, String applStatus) {
		return this.isAgreeAuthority(companyCd, applId, roleId, applStatus);
	}

	/**
	 * 社宅使用料月額の更新
	 * 
	 * @param shainNo
	 * @param nyutaikyoKbn
	 * @param applNo
	 * @return
	 */
	public boolean updateShatakuRental(String shainNo, String nyutaikyoKbn, String applNo) {
		List<Skf2010Sc005GetTeijiDataInfoExp> teijiDataList = new ArrayList<Skf2010Sc005GetTeijiDataInfoExp>();
		Skf2010Sc005GetTeijiDataInfoExpParameter param = new Skf2010Sc005GetTeijiDataInfoExpParameter();
		param.setShainNo(shainNo);
		param.setNyutaikyoKbn(nyutaikyoKbn);
		param.setApplNo(applNo);
		teijiDataList = skf2010Sc005GetTeijiDataInfoExpRepository.getTeijiDataInfo(param);
		if (teijiDataList.size() > 0) {
			Skf2010Sc005GetTeijiDataInfoExp teijiData = teijiDataList.get(0);
			if (NfwStringUtils.isNotEmpty(teijiData.getRentalAdjust())) {
				Skf2020TNyukyoChoshoTsuchi record = new Skf2020TNyukyoChoshoTsuchi();
				// 主キー設定
				record.setCompanyCd(companyCd);
				record.setApplNo(applNo);
				// 更新設定
				updateShatakuRentalMapping(teijiData, record);

				int result = skf2020TNyukyoChoshoTsuchiRepository.updateByPrimaryKeySelective(record);
				if (result <= 0) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * 使用料と共益費更新用レコードのマッピング
	 * 
	 * @param teijiData
	 * @param record
	 */
	private void updateShatakuRentalMapping(Skf2010Sc005GetTeijiDataInfoExp teijiData,
			Skf2020TNyukyoChoshoTsuchi record) {
		record.setNewRental(teijiData.getRentalAdjust());
		record.setKyoekihiPersonKyogichuFlg(teijiData.getKyoekihiPersonKyogichuFlg());
		record.setNewKyoekihi(teijiData.getKyoekihiPersonAdjust());

		if (NfwStringUtils.isNotEmpty(teijiData.getParkingRental1())) {
			record.setParkingRental(teijiData.getParkingRental1());
		}
		if (NfwStringUtils.isNotEmpty(teijiData.getParkingRental2())) {
			record.setParkingRental2(teijiData.getParkingRental2());
		}
		return;
	}

	/**** ここからprivateメソッド ***/

	/**
	 * 一括承認フラグチェック
	 * 
	 * @param tmpData
	 * @return
	 */
	private boolean checkAgree(Skf2010Sc005GetShoninIchiranShoninExp tmpData) {
		// 「一括承認可能フラグ」に、不可が設定されている場合
		if (tmpData == null || (tmpData != null && "0".equals(tmpData.getSumApprovalFlg()))) {
			return false;
		}
		String applStatus = tmpData.getApplStatus();

		// ログインユーザが承認権限を持たない申請書類である場合
		Map<String, String> loginUserInfoMap = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		if (!isAgreeAuthority(companyCd, tmpData.getApplId(), loginUserInfoMap.get("roleId"), applStatus)) {
			return false;
		}

		String agreName1 = tmpData.getAgreName1();
		String shoninName1 = loginUserInfoMap.get("userName");
		// 「承認者名1」いずれかが、セッションのログインユーザ名と同じ場合
		if (agreName1 != null && agreName1.equals(shoninName1)) {
			return false;
		}

		// 社宅入居の時
		if (FunctionIdConstant.R0100.equals(tmpData.getApplId())) {
			if (CodeConstant.STATUS_DOI_ZUMI.equals(applStatus) || CodeConstant.STATUS_SHONIN1.equals(applStatus)) {
				// 提示データの共益費が協議中だったら一括承認チェックボックスを非活性にする
				if (skfTeijiDataInfoUtils.selectKyoekihiKyogi(tmpData.getShainNo(), CodeConstant.SYS_NYUKYO_KBN,
						tmpData.getApplNo())) {
					return false;
				}
			}
			// ステータスが「同意済」「承認中」以外の場合は一括承認チェックボックスを非活性にする
			switch (applStatus) {
			case CodeConstant.STATUS_DOI_ZUMI:
			case CodeConstant.STATUS_SHONIN1:
				break;
			default:
				return false;
			}
		} else if (FunctionIdConstant.R0104.equals(tmpData.getApplId())
				|| FunctionIdConstant.R0105.equals(tmpData.getApplId())) {
			// 「申請書類ID」が「備品希望申請」「備品返却確認」のいずれかであり、
			// 「ステータス」が搬入済/搬出済/承認中でいずれでもない場合
			switch (applStatus) {
			case CodeConstant.STATUS_HANNYU_ZUMI:
			case CodeConstant.STATUS_HANSYUTSU_ZUMI:
			case CodeConstant.STATUS_SHONIN1:
				break;
			default:
				return false;
			}
		} else if (FunctionIdConstant.R0103.equals(tmpData.getApplId())) {
			// 「申請書類ID」が「社宅（自動車保管場所）退居届」であり、「ステータス」が承認中でない場合
			if (!CheckUtils.isEqual(applStatus, CodeConstant.STATUS_SHONIN1)) {
				return false;
			}
		} else {
			// 「申請書類ID」が前述のいずれでもなく、「ステータス」が審査中/承認中のいずれでもない場合
			switch (applStatus) {
			case CodeConstant.STATUS_SHINSACHU:
			case CodeConstant.STATUS_SHONIN1:
				break;
			default:
				return false;
			}
		}

		return true;
	}

	/**
	 * 承認権限チェックを行う
	 * 
	 * @param companyCd
	 * @param applId
	 * @param roleId
	 * @param applStatus
	 * @return
	 */
	private boolean isAgreeAuthority(String companyCd, String applId, String roleId, String applStatus) {
		boolean result = false;

		// ステータスチェック
		if (CodeConstant.STATUS_SHINSEICHU.equals(applStatus)
				|| CodeConstant.STATUS_SHOZOKUCHO_KAKUNINZUMI.equals(applStatus)
				|| CodeConstant.STATUS_SHINSACHU.equals(applStatus)
				|| CodeConstant.STATUS_TORIKOMI_KANRYO.equals(applStatus)
				|| CodeConstant.STATUS_DOI_ZUMI.equals(applStatus) || CodeConstant.STATUS_DOI_SHINAI.equals(applStatus)
				|| CodeConstant.STATUS_HANSYUTSU_ZUMI.equals(applStatus)
				|| CodeConstant.STATUS_HANNYU_ZUMI.equals(applStatus)
				|| CodeConstant.STATUS_SHONIN1.equals(applStatus)) {

			String wfLevel = CodeConstant.NONE;
			switch (applStatus) {
			case CodeConstant.STATUS_SHINSEICHU:
			case CodeConstant.STATUS_SHOZOKUCHO_KAKUNINZUMI:
			case CodeConstant.STATUS_SHINSACHU:
			case CodeConstant.STATUS_TORIKOMI_KANRYO:
			case CodeConstant.STATUS_DOI_ZUMI:
			case CodeConstant.STATUS_DOI_SHINAI:
			case CodeConstant.STATUS_HANSYUTSU_ZUMI:
			case CodeConstant.STATUS_HANNYU_ZUMI:
				wfLevel = CodeConstant.LEVEL_1;
				break;
			case CodeConstant.STATUS_SHONIN1:
				wfLevel = CodeConstant.LEVEL_2;
				break;
			}

			int cnt = 0;
			Skf2010Sc005GetAgreeAuthorityCountExp mApplicationExp = new Skf2010Sc005GetAgreeAuthorityCountExp();
			Skf2010Sc005GetAgreeAuthorityCountExpParameter param = new Skf2010Sc005GetAgreeAuthorityCountExpParameter();
			param.setCompanyCd(companyCd);
			param.setApplId(applId);
			param.setRoleId(roleId);
			param.setWfLevel(wfLevel);
			mApplicationExp = skf2010Sc005GetAgreeAuthorityCountExpRepository.getAgreeAuthorityCount(param);
			if (mApplicationExp != null) {
				cnt = Integer.valueOf(mApplicationExp.getExpr1());
				if (cnt > 0) {
					result = true;
				}
			}

		}

		return result;
	}

	/**
	 * 申請通知メール送信のメイン処理を行います
	 * 
	 * @param sendMailKbn
	 * @param applInfo
	 * @param comment
	 * @param annai
	 * @param furikomiStartDate
	 * @param sendUser
	 * @param sendGroundId
	 * @throws Exception
	 */
	private void sendApplTsuchiMail(String sendMailKbn, Map<String, String> applInfo, String comment, String annai,
			String furikomiStartDate, String sendUser, String sendGroundId) throws Exception {

		// 申請書類名を取得
		Skf2010MApplication skf2010MApplication = new Skf2010MApplication();
		Skf2010MApplicationKey key = new Skf2010MApplicationKey();
		key.setCompanyCd(companyCd);
		key.setApplId(applInfo.get("applId"));
		skf2010MApplication = skf2010MApplicationRepository.selectByPrimaryKey(key);
		if (skf2010MApplication == null) {
			// 申請書類名が取れない時はエラーログを出力
			return;
		}
		String applName = skf2010MApplication.getApplName();

		// 申請社員名、申請日を取得
		String applShainName = CodeConstant.NONE;
		String applDate = CodeConstant.NONE;
		String applAgencyCd = CodeConstant.NONE;

		// 申請通知メール情報の取得
		Skf2010Sc005GetSendApplMailInfoExp skfMShainData = new Skf2010Sc005GetSendApplMailInfoExp();
		List<Skf2010Sc005GetSendApplMailInfoExp> skfMShainList = new ArrayList<Skf2010Sc005GetSendApplMailInfoExp>();
		Skf2010Sc005GetSendApplMailInfoExpParameter mShainParam = new Skf2010Sc005GetSendApplMailInfoExpParameter();
		mShainParam.setCompanyCd(companyCd);
		mShainParam.setShainNo(applInfo.get("applShainNo"));
		mShainParam.setApplNo(applInfo.get("applNo"));
		skfMShainList = skf2010Sc005GetSendApplMailInfoExpRepository.getSendApplMailInfo(mShainParam);
		if (skfMShainList == null || skfMShainList.size() <= 0) {
			// 申請通知メール情報が取れない時はエラーログを出力
			return;
		}
		skfMShainData = skfMShainList.get(0);

		if (!CheckUtils.isEmpty(skfMShainData.getName())) {
			applShainName = skfMShainData.getName();
		}
		if (skfMShainData.getApplDate() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			applDate = sdf.format(skfMShainData.getApplDate());
		}
		if (!CheckUtils.isEmpty(skfMShainData.getAgencyCd())) {
			applAgencyCd = skfMShainData.getAgencyCd();
		}

		// アウトソース担当者共通のメールアドレスを取得
		String outMailAddress = getApplOutMailInfo(applAgencyCd, applInfo.get("applId"));

		// メール送信
		if (!CheckUtils.isEmpty(sendUser)) {
			String mailAddress = getSendMailAddressByShainNo(companyCd, sendUser);

			// URLを設定
			String urlBase = "/skf/Skf2010Sc003/init?SKF2010_SC003&menuflg=1&tokenCheck=0";
			sendApplTsuchiMailExist(companyCd, sendMailKbn, applInfo.get("applNo"), applDate, applShainName, comment,
					annai, furikomiStartDate, urlBase, sendUser, mailAddress, null, applName);
		} else {
			sendApplTsuchiMailExist(companyCd, sendMailKbn, applInfo.get("applNo"), applDate, applShainName, comment,
					annai, furikomiStartDate, null, sendUser, outMailAddress, null, applName);

		}

		return;
	}

	/**
	 * アウトソース担当者共通にメールを送信します
	 * 
	 * @param agencyCd
	 * @param applId
	 * @return
	 */
	private String getApplOutMailInfo(String agencyCd, String applId) {
		String mailAddress = CodeConstant.NONE;

		// 設定ファイル情報の取得

		if (applId.equals(FunctionIdConstant.R0100)) {
			// 社宅入居希望等調書
		} else if (applId.equals(FunctionIdConstant.R0105)) {
			// 備品返却申請
		}

		return mailAddress;
	}

	/**
	 * 通知メール用の社員情報を取得します
	 * 
	 * @param companyCd
	 * @param shainNo
	 * @return
	 */
	private String getSendMailAddressByShainNo(String companyCd, String shainNo) {
		String mailAddress = CodeConstant.NONE;
		Skf1010MShainKey key = new Skf1010MShainKey();
		key.setCompanyCd(companyCd);
		key.setShainNo(shainNo);
		Skf1010MShain mShainData = skf1010MShainRepository.selectByPrimaryKey(key);
		if (mShainData != null) {
			mailAddress = mShainData.getMailAddress();
		}
		return mailAddress;
	}

	/**
	 * 認識通知メールを送信します
	 * 
	 * @param companyCd
	 * @param mailKbn
	 * @param applNo
	 * @param applDate
	 * @param applShainName
	 * @param comment
	 * @param annai
	 * @param furikomiStartDate
	 * @param urlBase
	 * @param sendUser
	 * @param mailAddress
	 * @param bccAddress
	 * @param applName
	 * @throws Exception
	 */
	private void sendApplTsuchiMailExist(String companyCd, String mailKbn, String applNo, String applDate,
			String applShainName, String comment, String annai, String furikomiStartDate, String urlBase,
			String sendUser, String mailAddress, String bccAddress, String applName) throws Exception {

		// メール送信テスト
		Map<String, String> replaceMap = new HashMap<String, String>();

		// メール件名
		replaceMap.put("【to】", mailAddress); // 送信先メールアドレス
		replaceMap.put("【applno】", applNo); // 申請書類番号
		replaceMap.put("【shinseishorui】", applName); // 申請書類名

		// メール本文
		replaceMap.put("【shainname】", applShainName); // 申請社員名
		replaceMap.put("【appldate】", applDate); // 申請日
		replaceMap.put("【reason】", comment); // 承認者からのコメント

		// URL作成
		if (urlBase != null) {
			String url = BaseUrl.get() + "/" + urlBase.replaceFirst("^/", "");
			replaceMap.put("【url】", url);
		}

		// メール送信
		NfwSendMailUtils.sendMail("SKF_ML06", replaceMap);
		return;
	}

	/**
	 * 申請書類履歴テーブル更新（審査中）
	 * 
	 * @param applId
	 * @param applNo
	 * @param agreeAuthority
	 * @param applStatus
	 * @param errorMsg
	 * @throws Exception
	 */
	public boolean updateStatusShinsachu(String applId, String applNo, boolean agreeAuthority, String applStatus,
			Map<String, String> statusMap, Skf2010Sc005CommonDto dto) throws Exception {
		boolean statusUpdateFlg = true;

		if (CheckUtils.isEqual(applId, FunctionIdConstant.R0100)
				&& (CheckUtils.isEqual(applStatus, CodeConstant.STATUS_SHINSEICHU)
						|| CheckUtils.isEqual(applStatus, CodeConstant.STATUS_TORIKOMI_KANRYO)
						|| CheckUtils.isEqual(applStatus, CodeConstant.STATUS_DOI_SHINAI))) {
			// 申請書類IDが”R0100”【社宅入居希望等調書】かつ、
			// 現在のステータスが”01”【申請中】又は、”15”【取込完了】”21”【同意しない】の場合、処理を継続する。
		} else if (CheckUtils.isEqual(applId, FunctionIdConstant.R0103)
				&& CheckUtils.isEqual(applStatus, CodeConstant.STATUS_SHINSEICHU)) {
			// 申請書類IDが”R0103”【退居（自動車の保管場所返還）届】かつ、
			// 現在のステータスが”01”【申請中】の場合、処理を継続する。
		} else if (CheckUtils.isEqual(applId, FunctionIdConstant.R0104)
				&& CheckUtils.isEqual(applStatus, CodeConstant.STATUS_SHINSEICHU)) {
			// 申請書類IDが”R0104”【備品希望確認】かつ、
			// 現在のステータスが”01”【申請中】の場合、処理を継続する。
		} else if (CheckUtils.isEqual(applId, FunctionIdConstant.R0105)
				&& CheckUtils.isEqual(applStatus, CodeConstant.STATUS_DOI_SHINAI)) {
			// 申請書類IDが”R0105”【備品返却確認】かつ、
			// 現在のステータスが”21”【同意しない】の場合、処理を継続する。
		} else {
			statusUpdateFlg = false;
		}

		// ステータス更新可能かどうか
		if (agreeAuthority) {
			// ステータスを審査中に更新
			if (statusUpdateFlg) {
				Map<String, Date> lastUpdateDateMap = dto.getLastUpdateDateMap();
				Date lastUpdateDate = lastUpdateDateMap.get(applNo);
				Map<String, String> errorMsg = new HashMap<String, String>();
				boolean result = updateApplHistory(applNo, applId, CodeConstant.STATUS_SHINSACHU, lastUpdateDate,
						errorMsg);
				if (!result) {
					if (NfwStringUtils.isNotEmpty(errorMsg.get("error"))) {
						ServiceHelper.addErrorResultMessage(dto, null, errorMsg.get("error"));
					}
					return false;
				}
				// ステータスを審査中に変更
				// 文字列型は参照渡しできないのでMapオブジェクトに一時的に入れて返す
				statusMap.put("applStatus", CodeConstant.STATUS_SHINSACHU);
			}
		}
		return true;
	}

	/**
	 * 申請履歴を一時保存に変更します
	 * 
	 * @param applNo
	 * @return
	 */
	public boolean updateApplHistory(String applNo, String applId, String applStatus, Date lastUpdateDate,
			Map<String, String> errorMsg) throws Exception {
		boolean result = true;

		Map<String, String> loginUserInfo = skfLoginUserInfoUtils.getSkfLoginUserInfo();

		// 更新対象のデータを取得（行ロック実施）
		Skf2010Sc005GetApplHistoryStatusInfoForUpdateExp baseUpdateData = new Skf2010Sc005GetApplHistoryStatusInfoForUpdateExp();
		Skf2010Sc005GetApplHistoryStatusInfoForUpdateExpParameter param = new Skf2010Sc005GetApplHistoryStatusInfoForUpdateExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		param.setApplId(applId);
		baseUpdateData = skf2010Sc005GetApplHistoryStatusInfoForUpdateExpRepository
				.getApplHistoryStatusInfoForUpdate(param);
		if (baseUpdateData == null) {
			return false;
		}

		// 排他チェック実施
		if (!CheckUtils.isEqual(baseUpdateData.getUpdateDate(), lastUpdateDate)) {
			errorMsg.put("error", MessageIdConstant.E_SKF_1134);
			return false;
		}

		String agreDate = baseUpdateData.getAgreDate();
		String agreName1 = baseUpdateData.getAgreName1();
		String agreName2 = baseUpdateData.getAgreName2();

		// ステータスが申請、所属長確認済み、又は搬出済み以外の場合（申請時は承認情報を空にする）
		if (!CheckUtils.isEqual(applStatus, CodeConstant.STATUS_SHINSEICHU)
				&& !CheckUtils.isEqual(applStatus, CodeConstant.STATUS_SHOZOKUCHO_KAKUNINZUMI)
				&& !CheckUtils.isEqual(applStatus, CodeConstant.STATUS_HANSYUTSU_ZUMI)) {
			// 承認日時が設定されている場合
			if (NfwStringUtils.isNotEmpty(agreDate)) {
				agreDate = null;
			}
			// 承認者名1が設定されている場合
			if (NfwStringUtils.isEmpty(agreName1)) {
				agreName1 = CodeConstant.NONE;
			}
			// 承認者名2が設定されている場合
			if (NfwStringUtils.isEmpty(agreName2)) {
				agreName2 = CodeConstant.NONE;
			}
		}

		Skf2010Sc005UpdateApplHistoryAgreeStatusExp updateData = new Skf2010Sc005UpdateApplHistoryAgreeStatusExp();
		// 更新対象のプライマリキー設定
		updateData.setCompanyCd(companyCd);
		updateData.setApplNo(applNo);
		// 更新対象
		updateData.setApplStatus(applStatus); // 申請状況
		updateData.setAgreDate(agreDate); // 承認/修正依頼日
		updateData.setAgreName1(agreName1); // 承認者１
		updateData.setAgreName2(agreName2); // 承認者２
		updateData.setUpdateUserId(loginUserInfo.get("userName")); // 更新者

		int updateResult = skf2010Sc005UpdateApplHistoryAgreeStatusExpRepository
				.updateApplHistoryAgreeStatus(updateData);
		if (updateResult <= 0) {
			result = false;
		}

		return result;
	}

	/**
	 * 社宅貸与不要判定の取得
	 * 
	 * @param applNo
	 * @return true:必要としない、false:必要とする
	 */
	public boolean isShatakuTaiyoFuyo(String applNo) {
		Skf2020Sc003GetShatakuNyukyoKiboInfoExp shatakuNyukyoKiboInfo = new Skf2020Sc003GetShatakuNyukyoKiboInfoExp();
		Skf2020Sc003GetShatakuNyukyoKiboInfoExpParameter param = new Skf2020Sc003GetShatakuNyukyoKiboInfoExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		shatakuNyukyoKiboInfo = skf2020Sc003GetShatakuNyukyoKiboInfoExpRepository.getShatakuNyukyoKiboInfo(param);
		// 該当の社宅入居希望等調書が取得できた場合
		if (shatakuNyukyoKiboInfo != null) {
			if (NfwStringUtils.isNotEmpty(shatakuNyukyoKiboInfo.getTaiyoHitsuyo())
					&& CheckUtils.isEqual(shatakuNyukyoKiboInfo.getTaiyoHitsuyo(), CodeConstant.ASKED_SHATAKU_FUYOU)) {
				// 必要としない場合は判定結果をTrue
				return true;
			}
		}

		return false;
	}

	/**
	 * 社宅連携処理を実施する
	 * 
	 * @param menuScopeSessionBean
	 * @param shainNo
	 * @param applNo
	 * @param status
	 * @param applId
	 * @param pageId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> doShatakuRenkei(MenuScopeSessionBean menuScopeSessionBean, String shainNo, String applNo,
			String status, String applId, String pageId) {
		// ログインユーザー情報取得
		Map<String, String> loginUserInfoMap = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		String userId = loginUserInfoMap.get("userCd");
		// 排他チェック用データ取得
		Map<String, Object> forUpdateObject = (Map<String, Object>) menuScopeSessionBean
				.get(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2010SC005);

		List<String> resultBatch = new ArrayList<String>();

		switch (applId) {
		case FunctionIdConstant.R0100:
			Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> forUpdateMapR0100 = skf2020Fc001NyukyoKiboSinseiDataImport
					.forUpdateMapDownCaster(forUpdateObject.get(shainNo));
			skf2020Fc001NyukyoKiboSinseiDataImport.setUpdateDateForUpdateSQL(forUpdateMapR0100);

			// 連携処理開始
			resultBatch = skf2020Fc001NyukyoKiboSinseiDataImport.doProc(companyCd, shainNo, applNo, CodeConstant.NONE,
					status, userId, pageId);
			break;
		case FunctionIdConstant.R0103:
			Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> forUpdateMapR0103 = skf2040Fc001TaikyoTodokeDataImport
					.forUpdateMapDownCaster(forUpdateObject.get(shainNo));
			skf2040Fc001TaikyoTodokeDataImport.setUpdateDateForUpdateSQL(forUpdateMapR0103);

			// 連携処理開始
			resultBatch = skf2040Fc001TaikyoTodokeDataImport.doProc(companyCd, shainNo, applNo, status, userId, pageId);
			break;
		case FunctionIdConstant.R0104:
			Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> forUpdateMapR0104 = skf2030Fc001BihinKiboShinseiDataImport
					.forUpdateMapDownCaster(forUpdateObject);
			skf2030Fc001BihinKiboShinseiDataImport.setUpdateDateForUpdateSQL(forUpdateMapR0104);

			// 連携処理開始
			resultBatch = skf2030Fc001BihinKiboShinseiDataImport.doProc(companyCd, shainNo, applNo, status, userId,
					pageId);
			break;
		case FunctionIdConstant.R0105:
			Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> forUpdateMapR0105 = skf2050Fc001BihinHenkyakuSinseiDataImport
					.forUpdateMapDownCaster(forUpdateObject);
			skf2050Fc001BihinHenkyakuSinseiDataImport.setUpdateDateForUpdateSQL(forUpdateMapR0105);

			// 連携処理開始
			resultBatch = skf2050Fc001BihinHenkyakuSinseiDataImport.doProc(companyCd, shainNo, applNo, status, userId,
					pageId);
			break;
		default:
			break;
		}

		return resultBatch;
	}
}
