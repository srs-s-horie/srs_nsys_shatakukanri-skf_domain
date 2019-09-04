package jp.co.c_nexco.skf.skf2030.domain.service.skf2030sc001;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc001.Skf2030Sc001GetApplHistoryInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc001.Skf2030Sc001GetApplHistoryInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc001.Skf2030Sc001GetApplHistoryInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc001.Skf2030Sc001GetApplHistoryInfoForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc001.Skf2030Sc001GetApplHistoryInfoInDescendingOrderExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc001.Skf2030Sc001GetApplHistoryInfoInDescendingOrderExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc001.Skf2030Sc001GetBihinInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc001.Skf2030Sc001GetBihinInfoExpParameter;
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
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010MApplication;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010MApplicationKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplHistory;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2030TBihin;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2030TBihinKiboShinsei;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2030TBihinKiboShinseiKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2030Sc001.Skf2030Sc001GetApplHistoryInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2030Sc001.Skf2030Sc001GetApplHistoryInfoForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2030Sc001.Skf2030Sc001GetApplHistoryInfoInDescendingOrderExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2030Sc001.Skf2030Sc001GetBihinInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2030Sc001.Skf2030Sc001GetBihinShinseiInfoForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2030Sc001.Skf2030Sc001GetBihinTeijiStatusCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2030Sc001.Skf2030Sc001GetNYDStatusCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2030Sc001.Skf2030Sc001GetNyukyobiInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2030Sc001.Skf2030Sc001GetSKSDairininInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2030Sc001.Skf2030Sc001GetTeijiStatusCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010MApplicationRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TApplHistoryRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2030TBihinKiboShinseiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2030TBihinRepository;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfCommentUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfHtmlCreateUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationGuideUtils;
import jp.co.c_nexco.skf.skf2030.domain.dto.skf2030Sc001common.Skf2030Sc001CommonDto;

@Service
public class Skf2030Sc001SharedService {

	private String companyCd = CodeConstant.C001;

	private Map<String, String> bihinInfoMap;
	private Map<String, String> bihinApplMap;
	private Map<String, String> bihinAdjustMap;

	private MenuScopeSessionBean menuScopeSessionBean;

	private final String NO_DATA_MESSAGE = "初期表示中に";
	private final String MSG_MISHONIN = "承認されていない申請書類が存在し";
	private final String MSG_KAKUNIN = "「申請書類を確認する」から確認";

	// 排他処理用最終更新日付
	public static final String APPL_HISTORY_KEY_LAST_UPDATE_DATE = "skf2010_t_appl_history_UpdateDate";
	public static final String BIHIN_KIBO_SHINSEI_KEY_LAST_UPDATE_DATE = "skf2030_t_bihin_kibo_shinsei_UpdateDate";
	public static final String BIHIN_KEY_LAST_UPDATE_DATE = "skf2030_t_bihin_UpdateDate";

	@Autowired
	private Skf2030Sc001GetApplHistoryInfoExpRepository skf2030Sc001GetApplHistoryInfoExpRepository;
	@Autowired
	private Skf2030Sc001GetSKSDairininInfoExpRepository skf2030Sc001GetSKSDairininInfoExpRepository;
	@Autowired
	private Skf2030Sc001GetBihinInfoExpRepository skf2030Sc001GetBihinInfoExpRepository;
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
	 */
	public boolean setDisplayData(Map<String, String> applInfo, Skf2030Sc001CommonDto initDto) {
		String applNo = applInfo.get("applNo");
		// 申請状況を設定
		if (NfwStringUtils.isNotEmpty(applInfo.get("status"))) {
			// 申請状況を取得
			Map<String, String> applStatusMap = skfGenericCodeUtils.getGenericCode("SKF1001");
			initDto.setApplStatusText(applStatusMap.get(applInfo.get("status")));

			// 申請書類履歴取得
			List<Skf2030Sc001GetApplHistoryInfoExp> applHistoryInfoList = new ArrayList<Skf2030Sc001GetApplHistoryInfoExp>();
			applHistoryInfoList = getApplHistoryInfo(companyCd, applNo);

			if (applHistoryInfoList == null || applHistoryInfoList.size() <= 0) {
				ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1078, NO_DATA_MESSAGE);
				return false;
			}
			Skf2030Sc001GetApplHistoryInfoExp applHistoryInfo = applHistoryInfoList.get(0);
			// 申請書類履歴情報を画面の隠し項目に設定
			initDto.setHdnShainNo(applHistoryInfo.getShainNo());
			// 排他処理用申請履歴最終更新日保持
			initDto.addLastUpdateDate(APPL_HISTORY_KEY_LAST_UPDATE_DATE, applHistoryInfo.getLastUpdateDate());

			// 備品希望申請情報を取得
			Skf2030TBihinKiboShinsei bihinShinseiInfo = new Skf2030TBihinKiboShinsei();
			bihinShinseiInfo = getBihinShinseiInfo(companyCd, applNo);
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
				initDto.setShatakuKikaku(bihinShinseiInfo.getNowShatakuKikaku());
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
			String ApplTime = CodeConstant.NONE;
			if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getSessionTime())) {
				ApplTime = bihinShinseiInfo.getSessionTime();
			}
			// 希望時間取得（ドロップダウン生成時に初期値を設定）
			initDto.setDdlWishTime(skfDropDownUtils.getGenericForDoropDownList("SKF1048", ApplTime, false));
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
			switch (applInfo.get("status")) {
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
			List<Skf2030Sc001GetBihinInfoExp> bihinInfoList = new ArrayList<Skf2030Sc001GetBihinInfoExp>();
			bihinInfoList = getBihinInfo(companyCd, applNo);

			Long count = CodeConstant.LONG_ZERO;

			if (bihinInfoList != null && bihinInfoList.size() > 0) {
				bihinInfoMap = skfGenericCodeUtils.getGenericCode("SKF1051"); // 備品状況
				bihinApplMap = skfGenericCodeUtils.getGenericCode("SKF1049"); // 備品申請区分
				bihinAdjustMap = skfGenericCodeUtils.getGenericCode("SKF1144"); // 備品搬入状況

				int keyIndex = 11;
				// 備品申請情報をレイアウトに合うように整形
				for (Skf2030Sc001GetBihinInfoExp bihinInfo : bihinInfoList) {
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
					if (NfwStringUtils.isNotEmpty(nyukyobiInfoList.get(0).getNyukyoKanoDate())) {
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

	public void setEnabled(Skf2030Sc001CommonDto initDto, Map<String, String> applInfo) {
		// ステータスによりコントロールの活性制御を行う
		// 画面権限等の設定による制御もあるため、非表示化・非活性化のみ行う
		switch (applInfo.get("status")) {
		case CodeConstant.STATUS_ICHIJIHOZON:
		case CodeConstant.STATUS_MISAKUSEI:
		case CodeConstant.STATUS_SASHIMODOSHI:
			// 申請状況が「一時保存」「未作成」「差戻し」の場合
			// 「申請内容（入力用）」の活性・非活性制御
			initDto.setBihinReadOnly(false);
			initDto.setStatus01Flag(false);
			break;
		case CodeConstant.STATUS_SHINSEICHU:
			initDto.setBihinReadOnly(false);
			// 「申請中」の場合は入力項目は全て非活性
			// コメント欄は非表示
			initDto.setStatus01Flag(true);
			initDto.setMaskPattern("ST01");
			break;
		case CodeConstant.STATUS_HANNYU_MACHI:
			initDto.setBihinReadOnly(true);
			initDto.setCompletionDayDisabled(false);
			break;
		default:
			initDto.setBihinReadOnly(true);
			initDto.setCompletionDayDisabled(true);
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
				.getSkfLoginUserInfoFromAfterLogin(menuScopeSessionBean);
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

		if (!updateApplHistoryAgreeStatus(applInfo.get("applNo"), applInfo.get("applId"), applInfo.get("applStatus"),
				updateStatus, applInfo.get("shainNo"))) {
			return false;
		}

		// コメント
		String commentName = loginUserInfo.get("userName");
		String commentNote = skfHtmlCreateUtils.htmlEscapeEncode(dto.getCommentNote());
		if (NfwStringUtils.isNotEmpty(commentNote.trim())) {
			if (!skfCommentUtils.insertComment(companyCd, applInfo.get("applNo"), updateStatus, commentName,
					commentNote, errorMsg)) {
				return false;
			}
		}

		// 備品希望申請テーブルを更新
		if (updateBihinKiboShinseiInfo(applInfo, updateTel, updateRenrakuSakiTel, updateSessionDay, updateTime,
				updateCompleteDate)) {
			return false;
		}

		// 備品申請テーブルテーブル更新処理
		if (!updateDispInfoOfBihin(applInfo.get("applNo"), dto)) {
			return false;
		}

		dto.setApplStatus(updateStatus);

		// TODO 社宅管理データ連携処理実行

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

	private boolean updateBihinKiboShinseiInfo(Map<String, String> applInfo, String updateTel,
			String updateRenrakuSakiTel, String updateSessionDay, String updateTime, String updateCompleteDate) {
		Skf2030TBihinKiboShinsei record = new Skf2030TBihinKiboShinsei();
		// プライマリキー設定
		record.setCompanyCd(companyCd);
		record.setApplNo(applInfo.get("applNo"));
		// 更新項目設定
		// 勤務先のTEL
		if (NfwStringUtils.isNotEmpty(updateTel)) {
			record.setTel(updateTel);
		}
		// 連絡先
		if (NfwStringUtils.isNotEmpty(updateRenrakuSakiTel)) {
			record.setRenrakuSaki(updateRenrakuSakiTel);
		}
		// 搬入希望日
		if (NfwStringUtils.isNotEmpty(updateSessionDay)) {
			record.setSessionDay(updateSessionDay);
		}
		// 搬入希望時刻
		if (NfwStringUtils.isNotEmpty(updateTime)) {
			record.setSessionTime(updateTime);
		}
		// 搬入完了日
		if (NfwStringUtils.isNotEmpty(updateCompleteDate)) {
			record.setCompletionDay(updateCompleteDate);
		}

		boolean result = updateBihinKiboShinsei(record);

		return result;
	}

	/**
	 * 申請履歴の承認者と申請状況を更新します
	 * 
	 * @param applNo
	 * @param applId
	 * @param applStatus
	 * @param newApplStatus
	 * @param shainNo
	 * @return
	 */
	private boolean updateApplHistoryAgreeStatus(String applNo, String applId, String applStatus, String newApplStatus,
			String shainNo) {
		// 更新用データ取得（行ロック）
		Skf2030Sc001GetApplHistoryInfoForUpdateExp tmpData = new Skf2030Sc001GetApplHistoryInfoForUpdateExp();
		tmpData = getApplHistoryInfoForUpdate(applNo, applId, applStatus, shainNo);
		if (tmpData == null) {
			return false;
		}

		Skf2010TApplHistory record = new Skf2010TApplHistory();
		record.setApplStatus(newApplStatus);

		// 条件
		record.setCompanyCd(companyCd);
		record.setApplNo(applNo);
		record.setShainNo(shainNo);
		record.setApplDate(tmpData.getApplDate());
		record.setApplId(applId);

		boolean result = updateApplHistory(record);
		if (!result) {
			return false;
		}
		return true;
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
	private void setBihinData(Skf2030Sc001CommonDto dto, Skf2030Sc001GetBihinInfoExp bihinInfo) {
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

		String bihinStateText = bihinInfoMap.get(bihinInfo.getBihinState());
		String bihinApplText = bihinApplMap.get(bihinAppl);
		String bihinAdjustText = bihinAdjustMap.get(bihinInfo.getBihinAdjust());

		switch (bihinCd) {
		case CodeConstant.BIHIN_WASHER:
			dto.setBihinState11(bihinStateText);
			dto.setBihinAppl11(bihinAppl);
			dto.setBihinApplText11(bihinApplText);
			dto.setBihinAdjust11(bihinAdjustText);
			dto.setBihinDisabled11(btnDisabled);
			break;
		case CodeConstant.BIHIN_FREEZER:
			dto.setBihinState12(bihinStateText);
			dto.setBihinAppl12(bihinAppl);
			dto.setBihinApplText12(bihinApplText);
			dto.setBihinAdjust12(bihinAdjustText);
			dto.setBihinDisabled12(btnDisabled);
			break;
		case CodeConstant.BIHIN_OVEN:
			dto.setBihinState13(bihinStateText);
			dto.setBihinAppl13(bihinAppl);
			dto.setBihinApplText13(bihinApplText);
			dto.setBihinAdjust13(bihinAdjustText);
			dto.setBihinDisabled13(btnDisabled);
			break;
		case CodeConstant.BIHIN_CLENER:
			dto.setBihinState14(bihinStateText);
			dto.setBihinAppl14(bihinAppl);
			dto.setBihinApplText14(bihinApplText);
			dto.setBihinAdjust14(bihinAdjustText);
			dto.setBihinDisabled14(btnDisabled);
			break;
		case CodeConstant.BIHIN_RICE_COOKER:
			dto.setBihinState15(bihinStateText);
			dto.setBihinAppl15(bihinAppl);
			dto.setBihinApplText15(bihinApplText);
			dto.setBihinAdjust15(bihinAdjustText);
			dto.setBihinDisabled15(btnDisabled);
			break;
		case CodeConstant.BIHIN_TV:
			dto.setBihinState16(bihinStateText);
			dto.setBihinAppl16(bihinAppl);
			dto.setBihinApplText16(bihinApplText);
			dto.setBihinAdjust16(bihinAdjustText);
			dto.setBihinDisabled16(btnDisabled);
			break;
		case CodeConstant.BIHIN_TV_STANDS:
			dto.setBihinState17(bihinStateText);
			dto.setBihinAppl17(bihinAppl);
			dto.setBihinApplText17(bihinApplText);
			dto.setBihinAdjust17(bihinAdjustText);
			dto.setBihinDisabled17(btnDisabled);
			break;
		case CodeConstant.BIHIN_KOTATSU:
			dto.setBihinState18(bihinStateText);
			dto.setBihinAppl18(bihinAppl);
			dto.setBihinApplText18(bihinApplText);
			dto.setBihinAdjust18(bihinAdjustText);
			dto.setBihinDisabled18(btnDisabled);
			break;
		case CodeConstant.BIHIN_KICHEN_CABINET:
			dto.setBihinState19(bihinStateText);
			dto.setBihinAppl19(bihinAppl);
			dto.setBihinApplText19(bihinApplText);
			dto.setBihinAdjust19(bihinAdjustText);
			dto.setBihinDisabled19(btnDisabled);
			break;
		}
		return;
	}

	/**
	 * 申請履歴情報を取得します
	 * 
	 * @param companyCd
	 * @param applNo
	 * @return
	 */
	public List<Skf2030Sc001GetApplHistoryInfoExp> getApplHistoryInfo(String companyCd, String applNo) {
		// 申請履歴情報を取得する
		List<Skf2030Sc001GetApplHistoryInfoExp> applHistoryInfoList = new ArrayList<Skf2030Sc001GetApplHistoryInfoExp>();
		Skf2030Sc001GetApplHistoryInfoExpParameter param = new Skf2030Sc001GetApplHistoryInfoExpParameter();
		param.setApplNo(applNo);
		param.setCompanyCd(companyCd);
		applHistoryInfoList = skf2030Sc001GetApplHistoryInfoExpRepository.getApplHistoryInfo(param);
		return applHistoryInfoList;
	}

	public Skf2030TBihinKiboShinsei getBihinShinseiInfo(String companyCd, String applNo) {
		// 備品希望申請情報を取得する
		Skf2030TBihinKiboShinsei bihinShinseiInfo = new Skf2030TBihinKiboShinsei();
		Skf2030TBihinKiboShinseiKey key = new Skf2030TBihinKiboShinseiKey();
		key.setCompanyCd(companyCd);
		key.setApplNo(applNo);
		bihinShinseiInfo = skf2030TBihinKiboShinseiRepository.selectByPrimaryKey(key);
		return bihinShinseiInfo;
	}

	public List<Skf2030Sc001GetSKSDairininInfoExp> getSKSDairininInfo(String shainNo, String sysNyutaikyoKbn) {
		List<Skf2030Sc001GetSKSDairininInfoExp> dairininInfoList = new ArrayList<Skf2030Sc001GetSKSDairininInfoExp>();
		Skf2030Sc001GetSKSDairininInfoExpParameter param = new Skf2030Sc001GetSKSDairininInfoExpParameter();
		param.setShainNo(shainNo);
		param.setNyutaikyoKbn(sysNyutaikyoKbn);
		dairininInfoList = skf2030Sc001GetSKSDairininInfoExpRepository.getSKSDairininInfo(param);

		return dairininInfoList;
	}

	public List<Skf2030Sc001GetBihinInfoExp> getBihinInfo(String companyCd, String applNo) {
		// 備品申請情報取得
		List<Skf2030Sc001GetBihinInfoExp> bihinInfoList = new ArrayList<Skf2030Sc001GetBihinInfoExp>();
		Skf2030Sc001GetBihinInfoExpParameter param = new Skf2030Sc001GetBihinInfoExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		bihinInfoList = skf2030Sc001GetBihinInfoExpRepository.getBihinInfo(param);

		return bihinInfoList;
	}

	public List<Skf2030Sc001GetNyukyobiInfoExp> getNyukyobiInfo(String companyCd, String applNo) {
		List<Skf2030Sc001GetNyukyobiInfoExp> nyukyobiInfo = new ArrayList<Skf2030Sc001GetNyukyobiInfoExp>();
		Skf2030Sc001GetNyukyobiInfoExpParameter param = new Skf2030Sc001GetNyukyobiInfoExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		nyukyobiInfo = skf2030Sc001GetNyukyobiInfoExpRepository.getNyukyobiInfo(param);
		return nyukyobiInfo;
	}

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

	/**
	 * 備品希望申請情報の更新
	 * 
	 * @param updateData
	 * @return
	 */
	public boolean updateBihinKiboShinsei(Skf2030TBihinKiboShinsei updateData) {
		// 申請履歴情報更新処理
		int res = skf2030TBihinKiboShinseiRepository.updateByPrimaryKeySelective(updateData);
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
}
