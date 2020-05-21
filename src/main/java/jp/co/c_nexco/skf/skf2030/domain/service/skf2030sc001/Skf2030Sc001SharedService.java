package jp.co.c_nexco.skf.skf2030.domain.service.skf2030sc001;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc001.Skf2030Sc001GetApplHistoryInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc001.Skf2030Sc001GetApplHistoryInfoForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc001.Skf2030Sc001GetApplHistoryInfoInDescendingOrderExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc001.Skf2030Sc001GetApplHistoryInfoInDescendingOrderExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc001.Skf2030Sc001GetBihinShinseiInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc001.Skf2030Sc001GetBihinShinseiInfoForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc001.Skf2030Sc001GetBihinTeijiStatusCountExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc001.Skf2030Sc001GetBihinTeijiStatusCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc001.Skf2030Sc001GetNYDStatusCountExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc001.Skf2030Sc001GetNYDStatusCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc001.Skf2030Sc001GetNyukyobiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc001.Skf2030Sc001GetNyukyobiInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc001.Skf2030Sc001GetSKSDairininInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc001.Skf2030Sc001GetSKSDairininInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc001.Skf2030Sc001GetTeijiStatusCountExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc001.Skf2030Sc001GetTeijiStatusCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfApplHistoryInfoUtils.SkfApplHistoryInfoUtilsGetApplHistoryInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBatchUtils.SkfBatchUtilsGetMultipleTablesUpdateDateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBihinInfoUtils.SkfBihinInfoUtilsGetBihinInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010MApplication;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010MApplicationKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplHistory;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2030TBihin;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2030TBihinKiboShinsei;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2030TBihinKiboShinseiKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2030Sc001.Skf2030Sc001GetApplHistoryInfoForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2030Sc001.Skf2030Sc001GetApplHistoryInfoInDescendingOrderExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2030Sc001.Skf2030Sc001GetBihinShinseiInfoForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2030Sc001.Skf2030Sc001GetBihinTeijiStatusCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2030Sc001.Skf2030Sc001GetNYDStatusCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2030Sc001.Skf2030Sc001GetNyukyobiInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2030Sc001.Skf2030Sc001GetSKSDairininInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2030Sc001.Skf2030Sc001GetTeijiStatusCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfRollBack.SkfRollBackExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010MApplicationRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TApplHistoryRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2030TBihinKiboShinseiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2030TBihinRepository;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.DateUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfApplHistoryInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfBihinInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfCommentUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfHtmlCreateUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationGuideUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2030Fc001BihinKiboShinseiDataImport;
import jp.co.c_nexco.skf.skf2030.domain.dto.skf2030Sc001common.Skf2030Sc001CommonDto;

@Service
public class Skf2030Sc001SharedService {

	private String companyCd = CodeConstant.C001;

	private Map<String, String> bihinInfoMap;
	private Map<String, String> bihinApplMap;
	private Map<String, String> bihinAdjustMap;

	private MenuScopeSessionBean menuScopeSessionBean;

	private final String NO_DATA_MESSAGE = "初期表示中に";
	// 排他処理用最終更新日付
	public static final String APPL_HISTORY_KEY_LAST_UPDATE_DATE = "skf2010_t_appl_history_UpdateDate";
	public static final String BIHIN_KIBO_SHINSEI_KEY_LAST_UPDATE_DATE = "skf2030_t_bihin_kibo_shinsei_UpdateDate";
	public static final String BIHIN_KEY_LAST_UPDATE_DATE = "skf2030_t_bihin_UpdateDate";

	@Autowired
	private Skf2030Sc001GetSKSDairininInfoExpRepository skf2030Sc001GetSKSDairininInfoExpRepository;
	@Autowired
	private Skf2030Sc001GetNyukyobiInfoExpRepository skf2030Sc001GetNyukyobiInfoExpRepository;
	@Autowired
	private Skf2030Sc001GetApplHistoryInfoForUpdateExpRepository skf2030Sc001GetApplHistoryInfoForUpdateExpRepository;
	@Autowired
	private Skf2030Sc001GetBihinShinseiInfoForUpdateExpRepository skf2030Sc001GetBihinShinseiInfoForUpdateExpRepository;
	@Autowired
	private Skf2030Sc001GetApplHistoryInfoInDescendingOrderExpRepository skf2030Sc001GetApplHistoryInfoInDescendingOrderExpRepository;
	@Autowired
	private Skf2030Sc001GetNYDStatusCountExpRepository skf2030Sc001GetNYDStatusCountExpRepository;
	@Autowired
	private Skf2030Sc001GetTeijiStatusCountExpRepository skf2030Sc001GetTeijiStatusCountExpRepository;
	@Autowired
	private Skf2030Sc001GetBihinTeijiStatusCountExpRepository skf2030Sc001GetBihinTeijiStatusCountExpRepository;

	@Autowired
	private Skf2030TBihinKiboShinseiRepository skf2030TBihinKiboShinseiRepository;
	@Autowired
	private Skf2010MApplicationRepository skf2010MApplicationRepository;
	@Autowired
	private Skf2010TApplHistoryRepository skf2010TApplHistoryRepository;
	@Autowired
	private Skf2030TBihinRepository skf2030TBihinRepository;
	@Autowired
	private Skf2030Fc001BihinKiboShinseiDataImport skf2030Fc001BihinKiboShinseiDataImport;
	@Autowired
	private SkfRollBackExpRepository skfRollBackExpRepository;

	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfDropDownUtils skfDropDownUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfCommentUtils skfCommentUtils;
	@Autowired
	private SkfOperationGuideUtils skfOperationGuideUtils;
	@Autowired
	private SkfHtmlCreateUtils skfHtmlCreateUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private SkfApplHistoryInfoUtils skfApplHistoryInfoUtils;
	@Autowired
	private SkfBihinInfoUtils skfBihinInfoUtils;

	/**
	 * セッション情報を保持します
	 * 
	 * @param sessionBean
	 */
	public void setMenuScopeSessionBean(MenuScopeSessionBean sessionBean) {
		this.menuScopeSessionBean = sessionBean;
	}

	/**
	 * 画面内容の設定を行います
	 * 
	 * @param applInfo
	 * @param force
	 * @param initDto
	 * @throws Exception
	 */
	public boolean setDisplayData(Skf2030Sc001CommonDto initDto) throws Exception {
		String applNo = initDto.getApplNo();
		// 申請状況を設定
		if (NfwStringUtils.isNotEmpty(initDto.getApplStatus())) {
			// 申請状況を取得
			Map<String, String> applStatusMap = skfGenericCodeUtils.getGenericCode("SKF1001");
			initDto.setApplStatusText(applStatusMap.get(initDto.getApplStatus()));

			// 申請書類履歴取得
			List<SkfApplHistoryInfoUtilsGetApplHistoryInfoExp> applHistoryInfoList = new ArrayList<SkfApplHistoryInfoUtilsGetApplHistoryInfoExp>();
			applHistoryInfoList = skfApplHistoryInfoUtils.getApplHistoryInfo(companyCd, applNo);

			if (applHistoryInfoList == null || applHistoryInfoList.size() <= 0) {
				initDto.setBtnImportFinidhedDisabled("true");
				initDto.setBtnSaveDisabled("true");
				initDto.setBtnApplicationDisabled("true");
				ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1135, NO_DATA_MESSAGE);
				return false;
			}
			SkfApplHistoryInfoUtilsGetApplHistoryInfoExp applHistoryInfo = applHistoryInfoList.get(0);
			// 申請書類履歴情報を画面の隠し項目に設定
			initDto.setHdnShainNo(applHistoryInfo.getShainNo());
			// 排他処理用申請履歴最終更新日保持
			initDto.addLastUpdateDate(APPL_HISTORY_KEY_LAST_UPDATE_DATE, applHistoryInfo.getLastUpdateDate());

			// 備品希望申請情報を取得
			Skf2030TBihinKiboShinsei bihinShinseiInfo = new Skf2030TBihinKiboShinsei();
			bihinShinseiInfo = skfBihinInfoUtils.getBihinShinseiInfo(companyCd, applNo);
			if (bihinShinseiInfo == null) {
				setInitializeError(initDto);
				return false;
			}
			// 排他処理用備品希望申請最終更新日保持
			initDto.addLastUpdateDate(BIHIN_KIBO_SHINSEI_KEY_LAST_UPDATE_DATE, bihinShinseiInfo.getLastUpdateDate());

			// 【 所属 】
			// 機関
			if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getAgency())) {
				initDto.setAgency(bihinShinseiInfo.getAgency());
			}
			// 部等
			if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getAffiliation1())) {
				initDto.setAffiliation1(bihinShinseiInfo.getAffiliation1());
			}
			// 室、チームまたは課
			if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getAffiliation2())) {
				initDto.setAffiliation2(bihinShinseiInfo.getAffiliation2());
			}
			// 勤務先のTEL ※入力項目
			if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getTel())) {
				initDto.setTel(bihinShinseiInfo.getTel());
			}
			// 【 申請者 】
			// 社員番号
			if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getShainNo())) {
				initDto.setShainNo(bihinShinseiInfo.getShainNo());
			}
			// 氏名
			if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getName())) {
				initDto.setName(bihinShinseiInfo.getName());
			}
			// 等級
			if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getTokyu())) {
				initDto.setTokyu(bihinShinseiInfo.getTokyu());
			}
			// 性別
			if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getGender())) {
				Map<String, String> genderMap = skfGenericCodeUtils.getGenericCode("SKF1021");
				initDto.setGender(genderMap.get(bihinShinseiInfo.getGender()));
			}
			// 【 入居社宅 】
			// 社宅名
			if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getNowShatakuName())) {
				initDto.setShatakuName(bihinShinseiInfo.getNowShatakuName());
			}
			// 室番号
			if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getNowShatakuNo())) {
				initDto.setShatakuNo(bihinShinseiInfo.getNowShatakuNo());
			}
			// 規格(間取り)
			if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getNowShatakuKikaku())) {
				String newShatakuKikaku = skfGenericCodeUtils.getGenericCodeNameReverse(
						FunctionIdConstant.GENERIC_CODE_KIKAKU_KBN, bihinShinseiInfo.getNowShatakuKikaku());
				initDto.setShatakuKikaku(newShatakuKikaku);
			}
			// 面積
			if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getNowShatakuMenseki())) {
				initDto.setShatakuMenseki(bihinShinseiInfo.getNowShatakuMenseki() + SkfCommonConstant.SQUARE_MASTER);
			}

			// 【 代理人 】
			// 代理受取人
			if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getUkeireDairiName())) {
				initDto.setDairiName(bihinShinseiInfo.getUkeireDairiName());
			}
			// 代理連絡先
			if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getUkeireDairiApoint())) {
				initDto.setDairiRenrakusaki(bihinShinseiInfo.getUkeireDairiApoint());
			}

			// 【 備品搬入 】
			// 備品搬入希望日 ※入力項目
			if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getSessionDay())) {
				String sessionDayText = skfDateFormatUtils.dateFormatFromString(bihinShinseiInfo.getSessionDay(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				initDto.setSessionDay(sessionDayText);
			}
			// 備品搬入希望時間 ※入力項目
			String applTime = CodeConstant.NONE;
			if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getSessionTime())) {
				applTime = bihinShinseiInfo.getSessionTime();
				Map<String, String> sessionTimeMap = skfGenericCodeUtils.getGenericCode("SKF1048");
				initDto.setSessionTimeText(sessionTimeMap.get(applTime));
			}
			// 希望時間取得（ドロップダウン生成時に初期値を設定）
			initDto.setDdlWishTime(skfDropDownUtils.getGenericForDoropDownList("SKF1048", applTime, false));
			// 【 連絡先 】 ※入力項目
			if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getRenrakuSaki())) {
				initDto.setRenrakuSaki(bihinShinseiInfo.getRenrakuSaki());
			}

			// 【 備品搬入完了 】
			// 備品搬入完了日 ※入力項目
			if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getCompletionDay())) {
				initDto.setCompletionDay(bihinShinseiInfo.getCompletionDay());
			}

			// 搬入待ちまでのステータスの場合、常にSKSスキーマの代理人情報を参照し、最新の情報を表示する
			switch (initDto.getApplStatus()) {
			case CodeConstant.STATUS_ICHIJIHOZON:
			case CodeConstant.STATUS_MISAKUSEI:
			case CodeConstant.STATUS_SHINSEICHU:
			case CodeConstant.STATUS_HANNYU_MACHI:
				List<Skf2030Sc001GetSKSDairininInfoExp> dairiInfoList = new ArrayList<Skf2030Sc001GetSKSDairininInfoExp>();
				dairiInfoList = getSKSDairininInfo(initDto.getShainNo(), CodeConstant.SYS_NYUKYO_KBN);
				if (dairiInfoList != null && dairiInfoList.size() > 0) {
					Skf2030Sc001GetSKSDairininInfoExp dairininInfo = dairiInfoList.get(0);
					// 【 代理人 】
					// 代理受取人
					if (NfwStringUtils.isNotEmpty(dairininInfo.getUkeireDairiName())) {
						initDto.setDairiName(dairininInfo.getUkeireDairiName());
					}
					// 代理連絡先
					if (NfwStringUtils.isNotEmpty(dairininInfo.getUkeireDairiApoint())) {
						initDto.setDairiRenrakusaki(dairininInfo.getUkeireDairiApoint());
					}
				}
				break;
			}

			// 備品申請情報を取得する
			List<SkfBihinInfoUtilsGetBihinInfoExp> bihinInfoList = new ArrayList<SkfBihinInfoUtilsGetBihinInfoExp>();
			bihinInfoList = skfBihinInfoUtils.getBihinInfo(companyCd, applNo);

			Long count = CodeConstant.LONG_ZERO;

			if (bihinInfoList != null && bihinInfoList.size() > 0) {
				bihinInfoMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_EQUIPMENT_STATE); // 備品状況
				bihinApplMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_APPL_KBN); // 備品申請区分
				bihinAdjustMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_CARRING_IN_KBN); // 備品搬入状況

				int keyIndex = 11;
				// 備品申請情報をレイアウトに合うように整形
				for (SkfBihinInfoUtilsGetBihinInfoExp bihinInfo : bihinInfoList) {
					setBihinData(initDto, bihinInfo);
					// 調整区分が「2：保有備品搬入」か「3：レンタル搬入」の場合はカウントを１進める
					if (CodeConstant.BIHIN_ADJUST_HOYU.equals(bihinInfo.getBihinAdjust())
							|| CodeConstant.BIHIN_ADJUST_RENTAL.equals(bihinInfo.getBihinAdjust())) {
						count++;
					}
					// 排他処理用備品希望申請最終更新日保持
					initDto.addLastUpdateDate(BIHIN_KIBO_SHINSEI_KEY_LAST_UPDATE_DATE + keyIndex,
							bihinInfo.getLastUpdateDate());
					keyIndex++;
				}
			}

			// 調整区分が「2：保有備品搬入」か「3：レンタル搬入」のものが無かった場合
			if (count == 0) {
				// 提示データから入居可能日を取得する
				List<Skf2030Sc001GetNyukyobiInfoExp> nyukyobiInfoList = new ArrayList<Skf2030Sc001GetNyukyobiInfoExp>();
				nyukyobiInfoList = getNyukyobiInfo(companyCd, applNo);

				if (nyukyobiInfoList != null && nyukyobiInfoList.size() > 0) {
					if (nyukyobiInfoList.get(0) != null
							&& NfwStringUtils.isNotEmpty(nyukyobiInfoList.get(0).getNyukyoKanoDate())) {
						// 入居可能日が設定されていた場合、備品搬入日に入居可能日を設定する
						String nyukyoKanoDate = skfDateFormatUtils.dateFormatFromString(
								nyukyobiInfoList.get(0).getNyukyoKanoDate(),
								SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
						initDto.setHannyuKanryoDate(nyukyoKanoDate);
					}
				}
			}

			// コメント一覧取得
			List<SkfCommentUtilsGetCommentInfoExp> commentList = skfCommentUtils.getCommentInfo(companyCd, applNo,
					null);
			// コメントがあれば「コメント表示」ボタンを表示
			if (commentList != null && commentList.size() > 0) {
				initDto.setCommentViewFlag(true);
			} else {
				initDto.setCommentViewFlag(false);
			}

		}

		// 操作ガイド取得
		initDto.setOperationGuide(skfOperationGuideUtils.getOperationGuide(FunctionIdConstant.SKF2030_SC001));
		return true;
	}

	public void setEnabled(Skf2030Sc001CommonDto dto, Map<String, String> applInfo) {
		// ステータスによりコントロールの活性制御を行う
		// 画面権限等の設定による制御もあるため、非表示化・非活性化のみ行う
		switch (applInfo.get("status")) {
		case CodeConstant.STATUS_ICHIJIHOZON:
		case CodeConstant.STATUS_MISAKUSEI:
		case CodeConstant.STATUS_SASHIMODOSHI:
			// 申請状況が「一時保存」「未作成」「差戻し」の場合
			// 「申請内容（入力用）」の活性・非活性制御
			dto.setBihinReadOnly(false);
			dto.setStatus01Flag(false);
			break;
		case CodeConstant.STATUS_SHINSEICHU:
			dto.setBihinReadOnly(false);
			// 「申請中」の場合は入力項目は全て非活性
			// コメント欄は非表示
			dto.setStatus01Flag(true);
			dto.setMaskPattern("ST01");
			break;
		case CodeConstant.STATUS_HANNYU_MACHI:
			dto.setBihinReadOnly(true);
			dto.setCompletionDayDisabled(false);
			dto.setStatus24Flag(false);
			break;
		default:
			dto.setBihinReadOnly(true);
			dto.setCompletionDayDisabled(true);
			dto.setStatus24Flag(true);
			break;
		}

	}

	/**
	 * 「申請」「搬入完了」更新処理
	 * 
	 * @param applInfo
	 * @param dto
	 * @return
	 */
	public boolean updateDispInfo(Map<String, String> applInfo, Skf2030Sc001CommonDto dto) {
		String updateStatus = null;
		String updateSessionDay = null;
		String updateRenrakuSakiTel = null;
		String updateTime = null;
		String updateTel = null;
		String updateCompleteDate = null;

		Map<String, String> loginUserInfo = skfLoginUserInfoUtils
				.getSkfLoginUserInfoFromAlterLogin(menuScopeSessionBean);
		Map<String, String> errorMsg = new HashMap<String, String>();

		// 更新ステータス等の判定
		switch (applInfo.get("status")) {
		case CodeConstant.STATUS_ICHIJIHOZON:
		case CodeConstant.STATUS_SASHIMODOSHI:
			updateStatus = CodeConstant.STATUS_SHINSEICHU;
			updateSessionDay = skfHtmlCreateUtils.htmlEscapeEncode(dto.getSessionDay());
			updateTime = skfHtmlCreateUtils.htmlEscapeEncode(dto.getSessionTime());
			updateTel = skfHtmlCreateUtils.htmlEscapeEncode(dto.getTel());
			updateRenrakuSakiTel = skfHtmlCreateUtils.htmlEscapeEncode(dto.getRenrakuSaki());
			updateCompleteDate = null;
			break;
		default:
			updateStatus = CodeConstant.STATUS_HANNYU_ZUMI;
			updateSessionDay = null;
			updateRenrakuSakiTel = null;
			updateTime = null;
			updateTel = null;
			updateCompleteDate = skfHtmlCreateUtils.htmlEscapeEncode(dto.getCompletionDay());
			break;
		}

		Date lastUpdateDate = dto.getLastUpdateDate(APPL_HISTORY_KEY_LAST_UPDATE_DATE);

		if (!skfApplHistoryInfoUtils.updateApplHistoryAgreeStatus(companyCd, applInfo.get("shainNo"),
				applInfo.get("applNo"), applInfo.get("applId"), DateUtils.getSysDate(), null, updateStatus, null, CodeConstant.NONE,
				CodeConstant.NONE, lastUpdateDate, errorMsg)) {
			if (NfwStringUtils.isNotEmpty(errorMsg.get("error"))) {
				ServiceHelper.addErrorResultMessage(dto, null, errorMsg.get("error"));
			}
			return false;
		}

		// コメント更新
		String commentNote = dto.getCommentNote();
		boolean commentErrorMessage = skfCommentUtils.insertComment(CodeConstant.C001, applInfo.get("applNo"),
				updateStatus, commentNote, errorMsg);
		if (!commentErrorMessage) {
			return false;
		}

		// 備品希望申請テーブルを更新
		if (!skfBihinInfoUtils.updateBihinKiboShinseiInfo(applInfo, companyCd, updateTel, updateRenrakuSakiTel,
				updateSessionDay, updateTime, updateCompleteDate, null, null)) {
			return false;
		}

		// 備品申請テーブルテーブル更新処理
		if (!updateDispInfoOfBihin(applInfo.get("applNo"), dto)) {
			return false;
		}

		dto.setApplStatus(updateStatus);

		// 社宅管理データ連携処理実行
		String shainNo = dto.getShainNo();
		String applNo = dto.getApplNo();
		String pageId = FunctionIdConstant.SKF2030_SC001;
		List<String> resultBatch = doShatakuRenkei(menuScopeSessionBean, shainNo, applNo, updateStatus, pageId);
		if (resultBatch != null) {
			skf2030Fc001BihinKiboShinseiDataImport.addResultMessageForDataLinkage(dto, resultBatch);
			skfRollBackExpRepository.rollBack();
			return false;
		}

		return true;
	}

	/**
	 * 備品申請テーブルを更新します
	 * 
	 * @param applNo
	 * @param dto
	 * @return
	 */
	public boolean updateDispInfoOfBihin(String applNo, Skf2030Sc001CommonDto dto) {
		// 登録備品情報を設定
		for (int bihinCd = 11; bihinCd <= 19; bihinCd++) {
			Skf2030TBihin updData = new Skf2030TBihin();
			updData.setCompanyCd(companyCd);
			updData.setApplNo(applNo);
			updData.setBihinCd(String.valueOf(bihinCd));
			String bihinAppl = CodeConstant.NONE;
			switch (String.valueOf(bihinCd)) {
			case CodeConstant.BIHIN_WASHER:
				bihinAppl = dto.getBihinAppl11();
				break;
			case CodeConstant.BIHIN_FREEZER:
				bihinAppl = dto.getBihinAppl12();
				break;
			case CodeConstant.BIHIN_OVEN:
				bihinAppl = dto.getBihinAppl13();
				break;
			case CodeConstant.BIHIN_CLENER:
				bihinAppl = dto.getBihinAppl14();
				break;
			case CodeConstant.BIHIN_RICE_COOKER:
				bihinAppl = dto.getBihinAppl15();
				break;
			case CodeConstant.BIHIN_TV:
				bihinAppl = dto.getBihinAppl16();
				break;
			case CodeConstant.BIHIN_TV_STANDS:
				bihinAppl = dto.getBihinAppl17();
				break;
			case CodeConstant.BIHIN_KOTATSU:
				bihinAppl = dto.getBihinAppl18();
				break;
			case CodeConstant.BIHIN_KICHEN_CABINET:
				bihinAppl = dto.getBihinAppl19();
				break;
			}
			updData.setBihinAppl(bihinAppl);
			if (!updateBihinInfo(updData)) {
				ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
				return false;
			}
		}

		return true;
	}

	/**
	 * 入居日と搬入完了日の相関チェックを行います
	 * 
	 * @param applInfo
	 * @param completeDate
	 * @return
	 */
	public boolean sokanCheck(Map<String, String> applInfo, String completeDate) {
		List<Skf2030Sc001GetNyukyobiInfoExp> nyukyobiInfoList = new ArrayList<Skf2030Sc001GetNyukyobiInfoExp>();
		Skf2030Sc001GetNyukyobiInfoExpParameter param = new Skf2030Sc001GetNyukyobiInfoExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applInfo.get("applNo"));
		nyukyobiInfoList = skf2030Sc001GetNyukyobiInfoExpRepository.getNyukyobiInfo(param);
		if (nyukyobiInfoList != null && nyukyobiInfoList.size() > 0) {
			Skf2030Sc001GetNyukyobiInfoExp nyukyobiInfo = nyukyobiInfoList.get(0);
			long nyukyobi = Long.parseLong(nyukyobiInfo.getNyukyoKanoDate());
			long hannyubi = Long.parseLong(
					skfDateFormatUtils.dateFormatFromString(completeDate, SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT));
			if (hannyubi < nyukyobi) {
				applInfo.put("nyukyobi", nyukyobiInfo.getNyukyoKanoDate());
				return true;
			}
		}
		return false;
	}

	private void setInitializeError(Skf2030Sc001CommonDto initDto) {
		// 更新処理を行わせないようボタンを使用不可に
		initDto.setMaskPattern("ERR");
		return;
	}

	/**
	 * それぞれの備品に合うようにdtoの変数に値を割り当てます
	 * 
	 * @param dto
	 * @param bihinInfo
	 */
	private void setBihinData(Skf2030Sc001CommonDto dto, SkfBihinInfoUtilsGetBihinInfoExp bihinInfo) {
		String bihinCd = bihinInfo.getBihinCd();
		String bihinWish = bihinInfo.getBihinHope();
		String bihinAppl = bihinInfo.getBihinAppl();

		boolean btnDisabled = false;

		// 初期値：０の時は「希望する」にチェックが入るようにする
		if (CheckUtils.isEqual(bihinAppl, CodeConstant.BIHIN_APPL_DEFAULT)) {
			bihinAppl = CodeConstant.BIHIN_APPL_WISH;
		}
		// 入力可否制御
		if (CheckUtils.isEqual(bihinWish, CodeConstant.BIHIN_KIBO_FUKA)) {
			bihinAppl = CodeConstant.BIHIN_APPL_NOT_WISH;
			btnDisabled = true;
		}

		String bihinState = bihinInfo.getBihinState();
		String bihinAdjust = bihinInfo.getBihinAdjust();

		String bihinStateText = bihinInfoMap.get(bihinState);
		String bihinApplText = bihinApplMap.get(bihinAppl);
		String bihinAdjustText = bihinAdjustMap.get(bihinAdjust);
		if (NfwStringUtils.isEmpty(bihinAdjustText)) {
			bihinAdjustText = CodeConstant.HYPHEN;
		}

		switch (bihinCd) {
		case CodeConstant.BIHIN_WASHER:
			dto.setBihinState11(bihinState);
			dto.setBihinStateText11(bihinStateText);
			dto.setBihinAppl11(bihinAppl);
			dto.setBihinApplText11(bihinApplText);
			dto.setBihinAdjust11(bihinAdjust);
			dto.setBihinAdjustText11(bihinAdjustText);
			dto.setBihinDisabled11(btnDisabled);
			break;
		case CodeConstant.BIHIN_FREEZER:
			dto.setBihinState12(bihinState);
			dto.setBihinStateText12(bihinStateText);
			dto.setBihinAppl12(bihinAppl);
			dto.setBihinApplText12(bihinApplText);
			dto.setBihinAdjust12(bihinAdjust);
			dto.setBihinAdjustText12(bihinAdjustText);
			dto.setBihinDisabled12(btnDisabled);
			break;
		case CodeConstant.BIHIN_OVEN:
			dto.setBihinState13(bihinState);
			dto.setBihinStateText13(bihinStateText);
			dto.setBihinAppl13(bihinAppl);
			dto.setBihinApplText13(bihinApplText);
			dto.setBihinAdjust13(bihinAdjust);
			dto.setBihinAdjustText13(bihinAdjustText);
			dto.setBihinDisabled13(btnDisabled);
			break;
		case CodeConstant.BIHIN_CLENER:
			dto.setBihinState14(bihinState);
			dto.setBihinStateText14(bihinStateText);
			dto.setBihinAppl14(bihinAppl);
			dto.setBihinApplText14(bihinApplText);
			dto.setBihinAdjust14(bihinAdjust);
			dto.setBihinAdjustText14(bihinAdjustText);
			dto.setBihinDisabled14(btnDisabled);
			break;
		case CodeConstant.BIHIN_RICE_COOKER:
			dto.setBihinState15(bihinState);
			dto.setBihinStateText15(bihinStateText);
			dto.setBihinAppl15(bihinAppl);
			dto.setBihinApplText15(bihinApplText);
			dto.setBihinAdjust15(bihinAdjust);
			dto.setBihinAdjustText15(bihinAdjustText);
			dto.setBihinDisabled15(btnDisabled);
			break;
		case CodeConstant.BIHIN_TV:
			dto.setBihinState16(bihinState);
			dto.setBihinStateText16(bihinStateText);
			dto.setBihinAppl16(bihinAppl);
			dto.setBihinApplText16(bihinApplText);
			dto.setBihinAdjust16(bihinAdjust);
			dto.setBihinAdjustText16(bihinAdjustText);
			dto.setBihinDisabled16(btnDisabled);
			break;
		case CodeConstant.BIHIN_TV_STANDS:
			dto.setBihinState17(bihinState);
			dto.setBihinStateText17(bihinStateText);
			dto.setBihinAppl17(bihinAppl);
			dto.setBihinApplText17(bihinApplText);
			dto.setBihinAdjust17(bihinAdjust);
			dto.setBihinAdjustText17(bihinAdjustText);
			dto.setBihinDisabled17(btnDisabled);
			break;
		case CodeConstant.BIHIN_KOTATSU:
			dto.setBihinState18(bihinState);
			dto.setBihinStateText18(bihinStateText);
			dto.setBihinAppl18(bihinAppl);
			dto.setBihinApplText18(bihinApplText);
			dto.setBihinAdjust18(bihinAdjust);
			dto.setBihinAdjustText18(bihinAdjustText);
			dto.setBihinDisabled18(btnDisabled);
			break;
		case CodeConstant.BIHIN_KICHEN_CABINET:
			dto.setBihinState19(bihinState);
			dto.setBihinStateText19(bihinStateText);
			dto.setBihinAppl19(bihinAppl);
			dto.setBihinApplText19(bihinApplText);
			dto.setBihinAdjust19(bihinAdjust);
			dto.setBihinAdjustText19(bihinAdjustText);
			dto.setBihinDisabled19(btnDisabled);
			break;
		}
		return;
	}

	/**
	 * 代理人情報を取得します
	 * 
	 * @param shainNo
	 * @param sysNyutaikyoKbn
	 * @return
	 */
	public List<Skf2030Sc001GetSKSDairininInfoExp> getSKSDairininInfo(String shainNo, String sysNyutaikyoKbn) {
		List<Skf2030Sc001GetSKSDairininInfoExp> dairininInfoList = new ArrayList<Skf2030Sc001GetSKSDairininInfoExp>();
		Skf2030Sc001GetSKSDairininInfoExpParameter param = new Skf2030Sc001GetSKSDairininInfoExpParameter();
		param.setShainNo(shainNo);
		param.setNyutaikyoKbn(sysNyutaikyoKbn);
		dairininInfoList = skf2030Sc001GetSKSDairininInfoExpRepository.getSKSDairininInfo(param);

		return dairininInfoList;
	}

	/**
	 * 入居日を取得します
	 * 
	 * @param companyCd
	 * @param applNo
	 * @return
	 */
	public List<Skf2030Sc001GetNyukyobiInfoExp> getNyukyobiInfo(String companyCd, String applNo) {
		List<Skf2030Sc001GetNyukyobiInfoExp> nyukyobiInfo = new ArrayList<Skf2030Sc001GetNyukyobiInfoExp>();
		Skf2030Sc001GetNyukyobiInfoExpParameter param = new Skf2030Sc001GetNyukyobiInfoExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		nyukyobiInfo = skf2030Sc001GetNyukyobiInfoExpRepository.getNyukyobiInfo(param);
		return nyukyobiInfo;
	}

	/**
	 * 申請一覧を取得します
	 * 
	 * @param companyCd
	 * @param applId
	 * @return
	 */
	public Skf2010MApplication getApplMaster(String companyCd, String applId) {
		Skf2010MApplication applMaster = new Skf2010MApplication();
		Skf2010MApplicationKey key = new Skf2010MApplicationKey();
		key.setCompanyCd(companyCd);
		key.setApplId(applId);
		applMaster = skf2010MApplicationRepository.selectByPrimaryKey(key);

		return applMaster;
	}

	/**
	 * 更新対象の申請履歴情報を取得
	 * 
	 * @param applNo
	 * @param applId
	 * @param applStatus
	 * @param shainNo
	 * @return
	 */
	public Skf2030Sc001GetApplHistoryInfoForUpdateExp getApplHistoryInfoForUpdate(String applNo, String applId,
			String applStatus, String shainNo) {
		Skf2030Sc001GetApplHistoryInfoForUpdateExp applHistoryData = new Skf2030Sc001GetApplHistoryInfoForUpdateExp();
		Skf2030Sc001GetApplHistoryInfoForUpdateExpParameter param = new Skf2030Sc001GetApplHistoryInfoForUpdateExpParameter();
		param.setCompanyCd(companyCd);
		param.setShainNo(shainNo);
		param.setApplId(applId);
		param.setApplNo(applNo);
		param.setApplStatus(applStatus);
		applHistoryData = skf2030Sc001GetApplHistoryInfoForUpdateExpRepository.getApplHistoryInfoForUpdate(param);
		return applHistoryData;
	}

	/**
	 * 更新対象の申請履歴情報を取得
	 * 
	 * @param applNo
	 * @param applId
	 * @param applStatus
	 * @param shainNo
	 * @return
	 */
	public Skf2030Sc001GetBihinShinseiInfoForUpdateExp getBihinShinseiInfoForUpdate(String applNo) {
		Skf2030Sc001GetBihinShinseiInfoForUpdateExp data = new Skf2030Sc001GetBihinShinseiInfoForUpdateExp();
		Skf2030Sc001GetBihinShinseiInfoForUpdateExpParameter param = new Skf2030Sc001GetBihinShinseiInfoForUpdateExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		data = skf2030Sc001GetBihinShinseiInfoForUpdateExpRepository.getBihinShinseiInfoForUpdate(param);

		return data;
	}

	/**
	 * 申請履歴情報の更新
	 * 
	 * @param updateData
	 * @return
	 */
	public boolean updateApplHistory(Skf2010TApplHistory updateData) {
		// 申請履歴情報更新処理
		int res = skf2010TApplHistoryRepository.updateByPrimaryKeySelective(updateData);
		if (res <= 0) {
			return false;
		}
		return true;
	}

	public Skf2030TBihinKiboShinsei getBihinKiboShinseiData(String applNo) {
		Skf2030TBihinKiboShinsei returnData = new Skf2030TBihinKiboShinsei();
		Skf2030TBihinKiboShinseiKey key = new Skf2030TBihinKiboShinseiKey();
		key.setCompanyCd(companyCd);
		key.setApplNo(applNo);
		returnData = skf2030TBihinKiboShinseiRepository.selectByPrimaryKey(key);

		return returnData;
	}

	/**
	 * 備品希望申請情報の更新
	 * 
	 * @param updateData
	 * @return
	 */
	public boolean updateBihinKiboShinsei(Skf2030TBihinKiboShinsei updateData) {
		// 申請履歴情報更新処理
		int res = skf2030TBihinKiboShinseiRepository.updateByPrimaryKey(updateData);
		if (res <= 0) {
			return false;
		}
		return true;
	}

	/**
	 * 備品申請情報の更新
	 * 
	 * @param updateData
	 * @return
	 */
	public boolean updateBihinShinsei(Skf2030TBihin updateData) {
		// 申請履歴情報更新処理
		int res = skf2030TBihinRepository.updateByPrimaryKeySelective(updateData);
		if (res <= 0) {
			return false;
		}
		return true;
	}

	/**
	 * 備品申請情報の更新
	 * 
	 * @param updateData
	 * @return
	 */
	public boolean updateBihinInfo(Skf2030TBihin record) {
		// 申請履歴情報更新処理
		int res = skf2030TBihinRepository.updateByPrimaryKeySelective(record);
		if (res <= 0) {
			return false;
		}
		return true;
	}

	/**
	 * 申請書類履歴テーブル取得メソッド（申請日付の降順）
	 * 
	 * @param shainNo
	 * @param applId
	 * @return
	 */
	public List<Skf2030Sc001GetApplHistoryInfoInDescendingOrderExp> getApplHistoryInfoInDescendingOrder(String shainNo,
			String applId) {
		List<Skf2030Sc001GetApplHistoryInfoInDescendingOrderExp> applHistoryInfo = new ArrayList<Skf2030Sc001GetApplHistoryInfoInDescendingOrderExp>();
		Skf2030Sc001GetApplHistoryInfoInDescendingOrderExpParameter param = new Skf2030Sc001GetApplHistoryInfoInDescendingOrderExpParameter();
		param.setCompanyCd(companyCd);
		param.setShainNo(shainNo);
		param.setApplId(applId);
		applHistoryInfo = skf2030Sc001GetApplHistoryInfoInDescendingOrderExpRepository
				.getApplHistoryInfoInDescendingOrder(param);

		return applHistoryInfo;
	}

	/**
	 * 社宅管理 提示データステータスによる申請可否チェック
	 * 
	 * @param shainNo
	 * @param applId
	 * @param applNo
	 * @param errorMap
	 * @return
	 */
	public boolean checkSKSTeijiStatus(String shainNo, String applId, String applNo) {
		String nyutaikyoKbn = CodeConstant.NONE;
		// 該当データ数：初期値
		long listCount = CodeConstant.LONG_ZERO;

		if (NfwStringUtils.isEmpty(applNo)) {
			applNo = CodeConstant.HYPHEN;
		}

		switch (applId) {
		case FunctionIdConstant.R0100:
		case FunctionIdConstant.R0104:
			nyutaikyoKbn = CodeConstant.NYUTAIKYO_KBN_NYUKYO;

			// 以下の条件に当てはまる入退居予定データが存在する場合、新規申請不可
			// ①社員番号
			// ②入退居区分
			// ③入退居申請状況区分が'40'（承認）以外
			// かつ申請書類管理番号が異なる場合
			listCount += getSKSNYDStatusInfo(shainNo, nyutaikyoKbn, applNo);
			break;
		case FunctionIdConstant.R0103:
		case FunctionIdConstant.R0105:
			nyutaikyoKbn = CodeConstant.NYUTAIKYO_KBN_TAIKYO;
			break;
		}

		switch (applId) {
		case FunctionIdConstant.R0100:
		case FunctionIdConstant.R0103:
		case FunctionIdConstant.R0105:
			// R0100: 社宅入居希望等調書
			// R0103: 退居（自動車の保管場所返還）届
			// R0105: 備品返却申請

			// 以下の条件に当てはまる提示データが存在する場合、新規申請不可
			// ①社員番号
			// ②入退居区分
			// ③備品貸与区分<>'1'（必要）以外の場合、社宅提示ステータス<>'5'（承認）
			// 備品貸与区分＝'1'（必要）の場合、備品提示ステータス<>'9'（承認）

			listCount += getSKSTeijiStatusInfo(shainNo, nyutaikyoKbn);
			break;
		case FunctionIdConstant.R0104:

			// R0104: 備品希望申請
			//
			// 以下の条件に当てはまる提示データが存在する場合、新規申請不可
			// ①社員番号
			// ②入退居区分
			// ③備品貸与区分<>'1'（必要）以外の場合、社宅提示ステータス<>'5'（承認）
			// 備品貸与区分＝'1'（必要）の場合、備品提示ステータス<>'0'（未作成）かつ <>'9'（承認）

			listCount += getSKSBihinTeijiStatusInfo(shainNo, nyutaikyoKbn);
			break;
		}

		if (listCount > 0) {
			return true;
		}

		return false;
	}

	/**
	 * 備品提示データステータス取得
	 * 
	 * @param shainNo
	 * @param nyutaikyoKbn
	 * @return
	 */
	private long getSKSBihinTeijiStatusInfo(String shainNo, String nyutaikyoKbn) {
		Skf2030Sc001GetBihinTeijiStatusCountExp data = new Skf2030Sc001GetBihinTeijiStatusCountExp();
		Skf2030Sc001GetBihinTeijiStatusCountExpParameter param = new Skf2030Sc001GetBihinTeijiStatusCountExpParameter();
		param.setShainNo(shainNo);
		param.setNyutaikyoKbn(nyutaikyoKbn);
		data = skf2030Sc001GetBihinTeijiStatusCountExpRepository.getBihinTeijiStatusCount(param);
		if (data != null) {
			return data.getNydCount();
		}

		return 0;
	}

	/**
	 * 入退居予定データステータス取得メソッド
	 * 
	 * @param shainNo
	 * @param nyutaikyoKbn
	 * @param applNo
	 * @return
	 */
	private long getSKSNYDStatusInfo(String shainNo, String nyutaikyoKbn, String applNo) {
		Skf2030Sc001GetNYDStatusCountExp data = new Skf2030Sc001GetNYDStatusCountExp();
		Skf2030Sc001GetNYDStatusCountExpParameter param = new Skf2030Sc001GetNYDStatusCountExpParameter();
		param.setShainNo(shainNo);
		param.setNyutaikyoKbn(nyutaikyoKbn);
		param.setApplNo(applNo);
		data = skf2030Sc001GetNYDStatusCountExpRepository.getNYDStatusCount(param);
		if (data != null) {
			return data.getNydCount();
		}

		return 0;
	}

	private long getSKSTeijiStatusInfo(String shainNo, String nyutaikyoKbn) {
		Skf2030Sc001GetTeijiStatusCountExp data = new Skf2030Sc001GetTeijiStatusCountExp();
		Skf2030Sc001GetTeijiStatusCountExpParameter param = new Skf2030Sc001GetTeijiStatusCountExpParameter();
		param.setShainNo(shainNo);
		param.setNyutaikyoKbn(nyutaikyoKbn);
		data = skf2030Sc001GetTeijiStatusCountExpRepository.GetTeijiStatusCount(param);
		if (data != null) {
			return data.getNydCount();
		}
		return 0;
	}

	/**
	 * 社宅連携処理を実施する
	 * 
	 * @param shainNo
	 * @param applNo
	 * @param status
	 * @param userId
	 * @return
	 */
	public List<String> doShatakuRenkei(MenuScopeSessionBean menuScopeSessionBean, String shainNo, String applNo,
			String status, String pageId) {
		// ログインユーザー情報取得
		Map<String, String> loginUserInfoMap = skfLoginUserInfoUtils
				.getSkfLoginUserInfoFromAlterLogin(menuScopeSessionBean);
		String userId = loginUserInfoMap.get("userCd");
		// 排他チェック用データ取得
		Object forUpdateObject = menuScopeSessionBean.get(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2030SC001);
		Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> forUpdateMap = skf2030Fc001BihinKiboShinseiDataImport
				.forUpdateMapDownCaster(forUpdateObject);
		skf2030Fc001BihinKiboShinseiDataImport.setUpdateDateForUpdateSQL(forUpdateMap);

		// 連携処理開始
		List<String> resultBatch = skf2030Fc001BihinKiboShinseiDataImport.doProc(companyCd, shainNo, applNo, status,
				userId, pageId);
		// セッション情報削除
		menuScopeSessionBean.remove(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2030SC001);
		return resultBatch;
	}

}
