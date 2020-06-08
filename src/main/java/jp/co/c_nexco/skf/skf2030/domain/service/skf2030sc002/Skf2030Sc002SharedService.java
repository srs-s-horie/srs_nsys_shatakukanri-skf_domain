package jp.co.c_nexco.skf.skf2030.domain.service.skf2030sc002;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc002.Skf2030Sc002GetNyukyobiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc002.Skf2030Sc002GetNyukyobiInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc002.Skf2030Sc002GetTeijiBihinInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc002.Skf2030Sc002GetTeijiBihinInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfApplHistoryInfoUtils.SkfApplHistoryInfoUtilsGetApplHistoryInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfApplHistoryInfoUtils.SkfApplHistoryInfoUtilsGetApplHistoryInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfApplHistoryInfoUtils.SkfApplHistoryInfoUtilsGetApplStatusInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBatchUtils.SkfBatchUtilsGetMultipleTablesUpdateDateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBihinInfoUtils.SkfBihinInfoUtilsGetBihinInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBihinInfoUtils.SkfBihinInfoUtilsGetBihinMasterInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentListExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfTeijiDataInfoUtils.SkfTeijiDataInfoUtilsGetSKSDairininInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2030TBihin;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2030TBihinKiboShinsei;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2030Sc002.Skf2030Sc002GetNyukyobiInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2030Sc002.Skf2030Sc002GetTeijiBihinInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfRollBack.SkfRollBackExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2030TBihinRepository;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.common.utils.PropertyUtils;
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
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfHtmlCreateUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfMailUtils;
import jp.co.c_nexco.skf.common.util.SkfTeijiDataInfoUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2030Fc001BihinKiboShinseiDataImport;
import jp.co.c_nexco.skf.skf2030.domain.dto.skf2030Sc002common.Skf2030Sc002CommonDto;

/**
 * Skf2030Sc002 備品希望申請（アウトソース用)共通処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2030Sc002SharedService {

	// 共通会社コード
	private String companyCd = CodeConstant.C001;
	// セッション情報
	private MenuScopeSessionBean menuScopeSessionBean;

	private Map<String, String> bihinInfoMap;
	private Map<String, String> bihinApplMap;
	private Map<String, String> bihinAdjustMap;

	// 排他処理用最終更新日付
	private static final String APPL_HISTORY_KEY_LAST_UPDATE_DATE = "skf2010_t_appl_history_UpdateDate";
	private static final String BIHIN_KIBO_SHINSEI_KEY_LAST_UPDATE_DATE = "skf2030_t_bihin_kibo_shinsei_UpdateDate";
	private static final String CSS_FLOAT_L = " float-L";

	// 更新内容
	private static final String UPDATE_TYPE_PRESENT = "present"; // 提示
	private static final String UPDATE_TYPE_APPLY = "apply"; // 承認
	private static final String UPDATE_TYPE_SENDBACK = "sendback"; // 差戻し
	private static final String UPDATE_TYPE_REVISION = "revision"; // 修正依頼

	// メッセージ用定数
	private static final String NO_DATA_MESSAGE = "初期表示中に";
	private static final String SHATAKU_TEIJI_MSG = "提示データを確認";
	private static final String BIHIN_TEIJI_COMP = "（備品提示データが作成完了されていません。）";
	private static final String NYUKYO_SHONIN_MSG_T = "備品承認前に社宅入居希望等調書を承認";
	private static final String REASON_LABEL = "申請者へのコメント";

	@Autowired
	private Skf2030Sc002GetNyukyobiInfoExpRepository skf2030Sc002GetNyukyobiInfoExpRepository;
	@Autowired
	private Skf2030Sc002GetTeijiBihinInfoExpRepository skf2030Sc002GetTeijiBihinInfoExpRepository;
	@Autowired
	private Skf2030Fc001BihinKiboShinseiDataImport skf2030Fc001BihinKiboShinseiDataImport;

	@Autowired
	private Skf2030TBihinRepository skf2030TBihinRepository;
	@Autowired
	private SkfRollBackExpRepository skfRollBackExpRepository;

	@Autowired
	private SkfApplHistoryInfoUtils skfApplHistoryInfoUtils;
	@Autowired
	private SkfBihinInfoUtils skfBihinInfoUtils;
	@Autowired
	private SkfTeijiDataInfoUtils skfTeijiDataInfoUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfCommentUtils skfCommentUtils;
	@Autowired
	private SkfHtmlCreateUtils skfHtmlCreateUtils;
	@Autowired
	private SkfMailUtils skfMailUtils;

	/**
	 * セッション情報Beanクラスの設定
	 * 
	 * @param bean
	 */
	public void setMenuScopeSessionBean(MenuScopeSessionBean bean) {
		this.menuScopeSessionBean = bean;
	}

	public boolean setDisplayData(Skf2030Sc002CommonDto dto, Map<String, String> loginUserInfo,
			Map<String, String> applInfo) {

		boolean bCreateComplete = true;

		// 画面表示用汎用コード取得
		bihinInfoMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_EQUIPMENT_STATE); // 備品状況
		bihinApplMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_APPL_KBN); // 備品申請区分
		bihinAdjustMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_CARRING_IN_KBN); // 備品搬入状況

		// 申請書類履歴取得
		List<SkfApplHistoryInfoUtilsGetApplHistoryInfoExp> applHistoryList = new ArrayList<SkfApplHistoryInfoUtilsGetApplHistoryInfoExp>();
		applHistoryList = skfApplHistoryInfoUtils.getApplHistoryInfo(companyCd, applInfo.get("applNo"));
		if (applHistoryList == null || applHistoryList.size() <= 0) {
			// データ件数0件の場合はメッセージ
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1078, NO_DATA_MESSAGE);
			return false;
		}

		// 申請状況を取得
		Map<String, String> applStatusMap = skfGenericCodeUtils.getGenericCode("SKF1001");
		dto.setApplStatusText(applStatusMap.get(applInfo.get("status")));

		dto.setHdnShainNo(applHistoryList.get(0).getShainNo());
		String shainNo = applHistoryList.get(0).getShainNo();
		// 排他処理用に最終更新日取得
		dto.addLastUpdateDate(APPL_HISTORY_KEY_LAST_UPDATE_DATE, applHistoryList.get(0).getLastUpdateDate());

		// 備品希望申請情報取得
		Skf2030TBihinKiboShinsei bihinShinseiInfo = new Skf2030TBihinKiboShinsei();
		bihinShinseiInfo = skfBihinInfoUtils.getBihinShinseiInfo(companyCd, applInfo.get("applNo"));
		if (bihinShinseiInfo == null) {
			// 初期表示エラー処理

			return false;
		}
		// 排他処理用備品希望申請最終更新日保持
		dto.addLastUpdateDate(BIHIN_KIBO_SHINSEI_KEY_LAST_UPDATE_DATE, bihinShinseiInfo.getUpdateDate());

		// 所属
		// 機関
		if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getAgency())) {
			dto.setAgency(bihinShinseiInfo.getAgency());
		}
		// 部等
		if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getAffiliation1())) {
			dto.setAffiliation1(bihinShinseiInfo.getAffiliation1());
		}
		// 室、チームまたは課
		if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getAffiliation2())) {
			dto.setAffiliation2(bihinShinseiInfo.getAffiliation2());
		}
		// 勤務先のTEL ※入力項目
		if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getTel())) {
			dto.setTel(bihinShinseiInfo.getTel());
		}
		// 【 申請者 】
		// 社員番号
		if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getShainNo())) {
			dto.setShainNo(bihinShinseiInfo.getShainNo());
		}
		// 氏名
		if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getName())) {
			dto.setName(bihinShinseiInfo.getName());
		}
		// 等級
		if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getTokyu())) {
			dto.setTokyu(bihinShinseiInfo.getTokyu());
		}
		// 性別
		if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getGender())) {
			Map<String, String> genderMap = skfGenericCodeUtils.getGenericCode("SKF1021");
			dto.setGender(genderMap.get(bihinShinseiInfo.getGender()));
		}
		// 【 入居社宅 】
		// 社宅名
		if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getNowShatakuName())) {
			dto.setShatakuName(bihinShinseiInfo.getNowShatakuName());
		}
		// 室番号
		if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getNowShatakuNo())) {
			dto.setShatakuNo(bihinShinseiInfo.getNowShatakuNo());
		}
		// 規格(間取り)
		if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getNowShatakuKikaku())) {
			String newShatakuKikaku = skfGenericCodeUtils.getGenericCodeNameReverse(
					FunctionIdConstant.GENERIC_CODE_KIKAKU_KBN, bihinShinseiInfo.getNowShatakuKikaku());
			dto.setShatakuKikaku(newShatakuKikaku);
		}
		// 面積
		if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getNowShatakuMenseki())) {
			dto.setShatakuMenseki(bihinShinseiInfo.getNowShatakuMenseki() + SkfCommonConstant.SQUARE_MASTER);
		}

		// 【 代理人 】
		// 代理受取人
		if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getUkeireDairiName())) {
			dto.setDairiName(bihinShinseiInfo.getUkeireDairiName());
		}
		// 代理連絡先
		if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getUkeireDairiApoint())) {
			dto.setDairiRenrakusaki(bihinShinseiInfo.getUkeireDairiApoint());
		}

		// 【 備品搬入 】
		// 備品搬入希望日 ※入力項目
		if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getSessionDay())) {
			String sessionDayText = skfDateFormatUtils.dateFormatFromString(bihinShinseiInfo.getSessionDay(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			dto.setSessionDay(sessionDayText);
		}
		// 備品搬入希望時間 ※入力項目
		String applTime = CodeConstant.NONE;
		if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getSessionTime())) {
			applTime = bihinShinseiInfo.getSessionTime();
			Map<String, String> sessionTimeMap = skfGenericCodeUtils
					.getGenericCode(FunctionIdConstant.GENERIC_CODE_REQUEST_TIME);
			dto.setSessionTimeText(sessionTimeMap.get(applTime));
		}
		// 【 連絡先 】 ※入力項目
		if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getRenrakuSaki())) {
			dto.setRenrakuSaki(bihinShinseiInfo.getRenrakuSaki());
		}

		// 【 備品搬入完了 】
		// 備品搬入完了日 ※入力項目
		if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getCompletionDay())) {
			String completionDayText = skfDateFormatUtils.dateFormatFromString(bihinShinseiInfo.getCompletionDay(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			dto.setCompletionDay(completionDayText);
		}

		List<SkfBihinInfoUtilsGetBihinInfoExp> bihinInfoList = new ArrayList<SkfBihinInfoUtilsGetBihinInfoExp>();
		List<Map<String, String>> bihinList = new ArrayList<Map<String, String>>();

		// 搬入待ちまでのステータスの場合、常にSKSスキーマの代理人情報を参照し、最新の情報を表示する
		switch (applInfo.get("status")) {
		case CodeConstant.STATUS_ICHIJIHOZON:
		case CodeConstant.STATUS_MISAKUSEI:
		case CodeConstant.STATUS_SHINSEICHU:
		case CodeConstant.STATUS_SHINSACHU:
			List<SkfTeijiDataInfoUtilsGetSKSDairininInfoExp> dairiInfoList = new ArrayList<SkfTeijiDataInfoUtilsGetSKSDairininInfoExp>();
			dairiInfoList = skfTeijiDataInfoUtils.getSKSDairininInfo(dto.getShainNo(), CodeConstant.SYS_NYUKYO_KBN);
			if (dairiInfoList != null && dairiInfoList.size() > 0) {
				SkfTeijiDataInfoUtilsGetSKSDairininInfoExp dairininInfo = dairiInfoList.get(0);
				// 【 代理人 】
				// 代理受取人
				if (NfwStringUtils.isNotEmpty(dairininInfo.getUkeireDairiName())) {
					dto.setDairiName(dairininInfo.getUkeireDairiName());
				}
				// 代理連絡先
				if (NfwStringUtils.isNotEmpty(dairininInfo.getUkeireDairiApoint())) {
					dto.setDairiRenrakusaki(dairininInfo.getUkeireDairiApoint());
				}

				// 申請状況が「審査中」の場合、社宅提示データ作成完了区分が「未作成」か「社宅作成済み」だった場合
				if (CheckUtils.isEqual(applInfo.get("status"), CodeConstant.STATUS_SHINSACHU)
						&& dairininInfo.getCreateCompleteKbn() != null) {
					switch (dairininInfo.getCreateCompleteKbn()) {
					case CodeConstant.MI_SAKUSEI:
					case CodeConstant.SHATAKU_SAKUSEI_SUMI:
						bCreateComplete = false;
					}
				}
			} else {
				bCreateComplete = false;
			}

			// 備品申請情報を取得する
			bihinInfoList = skfBihinInfoUtils.getBihinInfo(companyCd, applInfo.get("applNo"));

			if (bihinInfoList != null && bihinInfoList.size() > 0) {
				boolean isBihinAppl = false;
				int bihinIndex = 11;

				List<Skf2030Sc002GetTeijiBihinInfoExp> teijiBihinList = new ArrayList<Skf2030Sc002GetTeijiBihinInfoExp>();
				teijiBihinList = getTeijiBihinInfo(shainNo, CodeConstant.SYS_NYUKYO_KBN);
				// 調整区分は提示備品から取得、更新して表示
				for (SkfBihinInfoUtilsGetBihinInfoExp bihinInfo : bihinInfoList) {
					if (teijiBihinList != null) {
						for (Skf2030Sc002GetTeijiBihinInfoExp teijiBihinInfo : teijiBihinList) {
							if (CheckUtils.isEqual(bihinInfo.getBihinCd(), teijiBihinInfo.getBihinCd())) {
								// 備品状態は最新状態に更新
								if (NfwStringUtils.isNotEmpty(teijiBihinInfo.getBihinState())) {
									bihinInfo.setBihinState(teijiBihinInfo.getBihinState());
								}
								// 備品提示データ作成完了状態は社宅管理の調整区分を反映、完了状態でなければ空
								if (bCreateComplete && NfwStringUtils.isNotEmpty(teijiBihinInfo.getBihinAdjust())) {
									bihinInfo.setBihinAdjust(teijiBihinInfo.getBihinAdjust());
								} else {
									bihinInfo.setBihinAdjust(CodeConstant.NONE);
								}
							}
						}
					}
					if (CheckUtils.isEqual(bihinInfo.getBihinAppl(), CodeConstant.BIHIN_APPL_WISH)) {
						isBihinAppl = true;
					}

					Map<String, String> bihinMap = setBihinData(bihinInfo);
					bihinMap.put("bihinIndex", String.valueOf(bihinIndex));
					bihinIndex++;
					bihinList.add(bihinMap);
				}
				// 「申請する」セットフラグ
				dto.setBihinCheckFlag(String.valueOf(isBihinAppl));

			}
			// 社宅提示データが作成完了されていない場合
			if (!bCreateComplete) {
				// 未作成、社宅作成完了は提示不可で継続
				dto.setPresentBtnDisabled(true);

				// ワーニング表示
				ServiceHelper.addWarnResultMessage(dto, MessageIdConstant.W_SKF_1001, SHATAKU_TEIJI_MSG,
						BIHIN_TEIJI_COMP);
			}

			break;
		default:
			// 備品申請情報取得
			bihinInfoList = skfBihinInfoUtils.getBihinInfo(companyCd, applInfo.get("applNo"));

			if (bihinInfoList != null && bihinInfoList.size() > 0) {
				// 備品申請情報をレイアウトに合うように整形
				for (SkfBihinInfoUtilsGetBihinInfoExp bihinInfo : bihinInfoList) {
					Map<String, String> bihinMap = setBihinData(bihinInfo);
					bihinList.add(bihinMap);
				}
			}
			dto.setBihinCheckFlag("false");
			break;
		}
		// 備品申請情報一覧をセット
		dto.setBihinList(bihinList);

		// コメント一覧取得
		List<SkfCommentUtilsGetCommentListExp> commentList = skfCommentUtils.getCommentList(companyCd,
				applInfo.get("applNo"), CodeConstant.STATUS_SHONIN1);
		// コメントがあれば「コメント表示」ボタンを表示
		if (commentList != null && commentList.size() > 0) {
			dto.setCommentBtnVisibled(true);
		} else {
			dto.setCommentBtnVisibled(false);
		}
		if (CheckUtils.isEqual(dto.getApplStatus(), CodeConstant.STATUS_SHONIN1)) {
			List<SkfCommentUtilsGetCommentInfoExp> commentInfo = new ArrayList<SkfCommentUtilsGetCommentInfoExp>();
			commentInfo = skfCommentUtils.getCommentInfo(companyCd, dto.getApplNo(), dto.getApplStatus());
			if (commentInfo != null && commentInfo.size() > 0) {
				String commentNote = commentInfo.get(0).getCommentNote();
				dto.setCommentNote(commentNote);
			}
		}

		// 承認可能ロールのみの処理
		switch (loginUserInfo.get("roleId")) {
		case SkfCommonConstant.ADMIN_ROLE1:
		case SkfCommonConstant.ADMIN_ROLE2:
		case SkfCommonConstant.ADMIN_ROLE3:
			break;
		default:
			dto.setMaskPattern("NONADMIN");
			break;
		}

		return true;
	}

	/**
	 * 提示備品データを取得します
	 * 
	 * @param shainNo
	 * @param nyutaikyoKbn
	 * @return
	 */
	private List<Skf2030Sc002GetTeijiBihinInfoExp> getTeijiBihinInfo(String shainNo, String nyutaikyoKbn) {
		List<Skf2030Sc002GetTeijiBihinInfoExp> teijiBihinList = new ArrayList<Skf2030Sc002GetTeijiBihinInfoExp>();
		Skf2030Sc002GetTeijiBihinInfoExpParameter param = new Skf2030Sc002GetTeijiBihinInfoExpParameter();
		param.setShainNo(shainNo);
		param.setNyutaikyoKbn(nyutaikyoKbn);
		teijiBihinList = skf2030Sc002GetTeijiBihinInfoExpRepository.getTeijiBihinInfo(param);
		return teijiBihinList;
	}

	/**
	 * それぞれの備品に合うようにdtoの変数に値を割り当てます
	 * 
	 * @param dto
	 * @param bihinInfo
	 */
	private Map<String, String> setBihinData(SkfBihinInfoUtilsGetBihinInfoExp bihinInfo) {
		String bihinAppl = bihinInfo.getBihinAppl();

		// 初期値：０の時は「希望する」にチェックが入るようにする
		if (CheckUtils.isEqual(bihinAppl, CodeConstant.BIHIN_APPL_DEFAULT)) {
			bihinAppl = CodeConstant.BIHIN_APPL_WISH;
		}

		String bihinStateText = bihinInfoMap.get(bihinInfo.getBihinState());
		String bihinApplText = bihinApplMap.get(bihinAppl);
		String bihinAdjustText = bihinAdjustMap.get(bihinInfo.getBihinAdjust());
		if (NfwStringUtils.isEmpty(bihinAdjustText)) {
			// 調整区分コードに該当する表記がない場合は「-（ハイフン）」を入れる
			bihinAdjustText = CodeConstant.HYPHEN;
		}

		Map<String, String> bihinMap = new HashMap<String, String>();
		bihinMap.put("bihinName", bihinInfo.getBihinName());
		bihinMap.put("bihinState", bihinInfo.getBihinState());
		bihinMap.put("bihinAppl", bihinAppl);
		bihinMap.put("bihinAdjust", bihinInfo.getBihinAdjust());
		bihinMap.put("bihinStateText", bihinStateText);
		bihinMap.put("bihinApplText", bihinApplText);
		bihinMap.put("bihinAdjustText", bihinAdjustText);

		return bihinMap;
	}

	/**
	 * 入居日を取得します
	 * 
	 * @param companyCd
	 * @param applNo
	 * @return
	 */
	public List<Skf2030Sc002GetNyukyobiInfoExp> getNyukyobiInfo(String companyCd, String applNo) {
		List<Skf2030Sc002GetNyukyobiInfoExp> nyukyobiInfo = new ArrayList<Skf2030Sc002GetNyukyobiInfoExp>();
		Skf2030Sc002GetNyukyobiInfoExpParameter param = new Skf2030Sc002GetNyukyobiInfoExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		nyukyobiInfo = skf2030Sc002GetNyukyobiInfoExpRepository.getNyukyobiInfo(param);
		return nyukyobiInfo;
	}

	/**
	 * 画面表示設定
	 * 
	 * @param dto
	 * @param applInfo
	 */
	public void setEnabled(Skf2030Sc002CommonDto dto, Map<String, String> applInfo) {
		// ステータスによりコントロールの活性制御を行う
		// 画面権限等の設定による制御もあるため、非表示化・非活性化のみ行う
		String applStatus = applInfo.get("status");
		switch (applStatus) {
		case CodeConstant.STATUS_ICHIJIHOZON:
		case CodeConstant.STATUS_SASHIMODOSHI:
		case CodeConstant.STATUS_HININ:
			// ステータス：一時保存、修正依頼、差戻し
			dto.setCompletionDayVisible(false); // 「搬入完了日」欄 【非表示】

			dto.setDispMode(CodeConstant.VIEW_LEVEL_1);

			dto.setFloatL(CodeConstant.NONE);
			// コメント入力欄非表示
			dto.setCommentViewFlag(false);

			break;
		case CodeConstant.STATUS_SHINSEICHU:
		case CodeConstant.STATUS_SHINSACHU:
			// ステータス：申請中、審査中
			dto.setCompletionDayVisible(false); // 「搬入完了日」欄 【非表示】

			dto.setDispMode(CodeConstant.VIEW_LEVEL_2);

			// 右側のボタンがある時のみClassにfloat-lを追加
			dto.setFloatL(CSS_FLOAT_L);

			// コメント入力欄表示
			dto.setCommentViewFlag(true);

			break;
		case CodeConstant.STATUS_HANNYU_MACHI:
			// ステータス：申請中、審査中
			dto.setCompletionDayVisible(false); // 「搬入完了日」欄 【非表示】

			dto.setDispMode(CodeConstant.VIEW_LEVEL_1);
			dto.setFloatL(CodeConstant.NONE);
			// コメント入力欄非表示
			dto.setCommentViewFlag(false);
			break;
		case CodeConstant.STATUS_HANNYU_ZUMI:
		case CodeConstant.STATUS_SHONIN1:
		case CodeConstant.STATUS_SHONIN2:
			// ステータス：申請中、審査中
			dto.setCompletionDayVisible(true); // 「搬入完了日」欄 【非表示】

			dto.setDispMode(CodeConstant.VIEW_LEVEL_3);

			// コメント入力欄表示
			dto.setCommentViewFlag(true);

			// 右側のボタンがある時のみClassにfloat-lを追加
			dto.setFloatL(CSS_FLOAT_L);
			break;
		case CodeConstant.STATUS_SHONIN_ZUMI:
			// ステータス：申請中、審査中
			dto.setCompletionDayVisible(true); // 「搬入完了日」欄 【表示】
			dto.setDispMode(CodeConstant.VIEW_LEVEL_1);
			dto.setFloatL(CodeConstant.NONE);
			// コメント入力欄表示
			dto.setCommentViewFlag(false);
			break;

		default:
			// それ以外
			dto.setCompletionDayVisible(false);
			dto.setDispMode(CodeConstant.VIEW_LEVEL_1);
			dto.setFloatL(CodeConstant.NONE);
			// コメント入力欄非表示
			dto.setCommentViewFlag(false);
			break;
		}

	}

	/**
	 * 更新処理を行います
	 * 
	 * @param execName
	 * @param dto
	 * @param applInfo
	 * @param loginUserInfo
	 * @return
	 * @throws Exception
	 */
	public boolean updateDispInfo(String execName, Skf2030Sc002CommonDto dto, Map<String, String> applInfo,
			Map<String, String> loginUserInfo) throws Exception {
		// 申請社員番号を設定
		applInfo.put("applShainNo", dto.getHdnShainNo());

		String updateStatus = null;
		String shoninName1 = null;
		String shoninName2 = null;
		Date agreDate = null;
		String mailKbn = null;
		String nextWorkflow = null;
		Date sysDateTime = new Date();

		String sendUserId = CodeConstant.NONE;

		String applStatus = applInfo.get("status");
		switch (applStatus) {
		case CodeConstant.STATUS_SHINSACHU:
		case CodeConstant.STATUS_SHINSEICHU:
			// 申請中、審査中
			// 次のステータスを設定
			if (CheckUtils.isEqual(execName, UPDATE_TYPE_APPLY)) {
				// 次のステータス、メール区分、承認者名1、次の階層を設定
				updateStatus = CodeConstant.STATUS_SHONIN1;
				shoninName1 = loginUserInfo.get("userName");
				nextWorkflow = CodeConstant.WORK_FLOW_LEVEL_2;
			} else {
				updateStatus = CodeConstant.STATUS_SHOZOKUCHO_KAKUNINZUMI;
			}
			break;
		case CodeConstant.STATUS_HANNYU_ZUMI:
			// 搬入済
			// 次のステータス、メール区分、承認者名1、次の階層を設定
			updateStatus = CodeConstant.STATUS_SHONIN1;
			shoninName1 = loginUserInfo.get("userName");
			nextWorkflow = CodeConstant.WORK_FLOW_LEVEL_2;
			break;
		case CodeConstant.STATUS_SHONIN1:
			// 承認1済
			// 次のステータス、メール区分、承認者名2、次の階層を設定
			updateStatus = CodeConstant.STATUS_SHONIN_ZUMI;
			shoninName1 = CodeConstant.NONE;
			shoninName2 = loginUserInfo.get("userName");
			agreDate = sysDateTime;
			break;
		}

		switch (execName) {
		case UPDATE_TYPE_REVISION:
			// 修正依頼
			switch (applStatus) {
			case CodeConstant.STATUS_HANNYU_ZUMI:
			case CodeConstant.STATUS_SHONIN1:
				// 押下されたボタンが修正依頼 かつステータスが搬入済み、承認中の場合、更新ステータスを強制的に搬入待ちに変更
				updateStatus = CodeConstant.STATUS_HANNYU_MACHI;
				mailKbn = CodeConstant.HANNYU_MACHI_TSUCHI;
				agreDate = sysDateTime;
				sendUserId = applInfo.get("applShainNo");
				break;
			case CodeConstant.STATUS_SHINSACHU:
				// 押下されたボタンが修正依頼 かつステータスが審査中の場合、更新ステータスを強制的に修正依頼に変更
				updateStatus = CodeConstant.STATUS_SASHIMODOSHI;
				mailKbn = CodeConstant.SASHIMODOSHI_KANRYO_TSUCHI;
				agreDate = sysDateTime;
				sendUserId = applInfo.get("applShainNo");
				break;
			}
			break;
		case UPDATE_TYPE_SENDBACK:
			// 押下されたボタンが差戻しの場合、更新ステータスを強制的に差戻しに変更
			updateStatus = CodeConstant.STATUS_HININ;
			mailKbn = CodeConstant.HININ_KANRYO_TSUCHI;
			agreDate = sysDateTime;
			sendUserId = applInfo.get("applShainNo");
			break;
		case UPDATE_TYPE_PRESENT:
			// 押下されたボタンが提示の場合
			// 社宅入居希望等調書の承認がされていない場合、備品の提示を行えないよう制御する
			if (!checkNyukyoShonin(dto, applInfo.get("applShainNo"), applInfo.get("applNo"), NYUKYO_SHONIN_MSG_T)) {
				return false;
			}
			// 押下されたボタンが提示の場合、更新ステータスを強制的に搬入待ちに変更
			updateStatus = CodeConstant.STATUS_HANNYU_MACHI;
			mailKbn = CodeConstant.HANNYU_MACHI_TSUCHI;
			agreDate = sysDateTime;
			sendUserId = applInfo.get("applShainNo");
			break;
		default:
			// 社宅入居希望等調書の承認がされていない場合、備品の承認を行えないよう制御する
			if (!checkNyukyoShonin(dto, applInfo.get("applShainNo"), applInfo.get("applNo"), NYUKYO_SHONIN_MSG_T)) {
				return false;
			}

			if (NfwStringUtils.isEmpty(nextWorkflow)) {
				// 次のワークフロー設定がない場合、承認済みに設定
				updateStatus = CodeConstant.STATUS_SHONIN_ZUMI;
				mailKbn = CodeConstant.SHONIN_KANRYO_TSUCHI;
				sendUserId = applInfo.get("applShainNo");
			}
			break;
		}

		boolean result = true;
		// 申請書類履歴テーブル更新処理（利用クラス：SkfApplHistoryInfoUtils、利用メソッド：UpdateApplHistoryAgreeStatus）
		Map<String, String> errorMsg = new HashMap<String, String>();
		SkfApplHistoryInfoUtilsGetApplHistoryInfoForUpdateExp updApplInfo = new SkfApplHistoryInfoUtilsGetApplHistoryInfoForUpdateExp();
		updApplInfo = skfApplHistoryInfoUtils.getApplHistoryInfoForUpdate(companyCd, applInfo.get("shainNo"),
				applInfo.get("applNo"), applInfo.get("applId"));
		if (updApplInfo == null) {
			return false;
		}
		// 楽観的排他チェック（申請情報履歴）
		if (!CheckUtils.isEqual(updApplInfo.getUpdateDate(),
				dto.getLastUpdateDate(APPL_HISTORY_KEY_LAST_UPDATE_DATE))) {
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1135);
			return false;
		}

		// 承認者２が更新対象であり、承認者１の値がすでに入っていた場合は更新用の変数に投入
		if (NfwStringUtils.isNotEmpty(shoninName2) && NfwStringUtils.isNotEmpty(updApplInfo.getAgreName1())) {
			shoninName1 = updApplInfo.getAgreName1();
		}
		Date lastUpdateDate = dto.getLastUpdateDate(APPL_HISTORY_KEY_LAST_UPDATE_DATE);
		result = skfApplHistoryInfoUtils.updateApplHistoryAgreeStatusForBihin(companyCd, applInfo.get("applShainNo"),
				applInfo.get("applNo"), applInfo.get("applId"), null, null, updateStatus, agreDate, shoninName1,
				shoninName2, lastUpdateDate, errorMsg);
		if (!result) {
			// エラーメッセージ（メッセージID：S02000）を設定
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
			return false;
		}

		// ステータスを搬入待ちにする場合、代理人の情報を更新する
		// ケースとしては以下の通り
		// ・申請中→審査中→搬入待ち
		// 修正依頼で搬入待ちになる場合は、再提示ではないので代理人は更新しない
		switch (applStatus) {
		case CodeConstant.STATUS_SHINSEICHU:
		case CodeConstant.STATUS_SHINSACHU:
			// 申請中、審査中
			// 備品希望申請テーブルを更新
			Skf2030TBihinKiboShinsei tmpBihinData = new Skf2030TBihinKiboShinsei();
			tmpBihinData = skfBihinInfoUtils.getBihinShinseiInfo(companyCd, applInfo.get("applNo"));
			Date lastUpdateDateBihinShinsei = dto.getLastUpdateDate(BIHIN_KIBO_SHINSEI_KEY_LAST_UPDATE_DATE);
			if (!CheckUtils.isEqual(tmpBihinData.getUpdateDate(), lastUpdateDateBihinShinsei)) {
				// 排他チェックエラー
				ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1135);
				return false;
			}

			result = skfBihinInfoUtils.updateBihinKiboShinseiInfo(applInfo, companyCd, null, null, null, null, null,
					dto.getDairiName(), dto.getDairiRenrakusaki());
			if (!result) {
				// エラーメッセージ（メッセージID：S02000）を設定
				ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
				return false;
			}

			// 備品申請テーブルの更新処理
			result = updateDispInfoOfBihin(applInfo.get("applNo"), dto);
			if (!result) {
				// エラーメッセージ（メッセージID：S02000）を設定
				ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
				return false;
			}
			break;
		}

		// コメント更新
		String commentNote = dto.getCommentNote();
		boolean commentErrorMessage = skfCommentUtils.insertComment(CodeConstant.C001, applInfo.get("applNo"),
				updateStatus, commentNote, errorMsg);
		if (!commentErrorMessage) {
			return false;
		}

		// 承認完了通知・修正依頼完了通知の場合のみ
		if (mailKbn != null) {
			switch (mailKbn) {
			case CodeConstant.SHONIN_KANRYO_TSUCHI:
			case CodeConstant.SASHIMODOSHI_KANRYO_TSUCHI:
			case CodeConstant.HININ_KANRYO_TSUCHI:
			case CodeConstant.HANNYU_MACHI_TSUCHI:
			case CodeConstant.TEJI_TSUCHI:
				// 掲載URL
				String urlBase = "/skf/Skf2010Sc003/init?SKF2010_SC003&menuflg=1&tokenCheck=0";
				skfMailUtils.sendApplTsuchiMail(mailKbn, applInfo, commentNote, null, sendUserId, CodeConstant.NONE,
						urlBase);
				break;

			}
		}
		// ステータス値を更新
		applInfo.put("status", updateStatus);
		dto.setApplStatus(updateStatus);

		// 社宅データ連携
		String shainNo = dto.getShainNo();
		String applNo = dto.getApplNo();
		String pageId = FunctionIdConstant.SKF2030_SC002;
		List<String> resultBatch = doShatakuRenkei(menuScopeSessionBean, shainNo, applNo, updateStatus, pageId);
		if (resultBatch != null) {
			skf2030Fc001BihinKiboShinseiDataImport.addResultMessageForDataLinkage(dto, resultBatch);
			skfRollBackExpRepository.rollBack();
			return false;
		}

		return true;
	}

	/**
	 * 社宅入居希望等調書の承認チェックメソッド
	 * 
	 * @param dto
	 * @param shainNo
	 * @param applNo
	 * @param nyukyoShoninMsg
	 * @return false:承認不可、true:承認可
	 */
	private boolean checkNyukyoShonin(Skf2030Sc002CommonDto dto, String shainNo, String applNo,
			String nyukyoShoninMsg) {
		// 社宅入居希望等調書の承認がされていない場合、備品の提示を行えないよう制御する
		List<SkfApplHistoryInfoUtilsGetApplStatusInfoExp> applHistoryList = new ArrayList<SkfApplHistoryInfoUtilsGetApplStatusInfoExp>();
		applHistoryList = skfApplHistoryInfoUtils.getApplStatusInfo(companyCd, applNo, shainNo);
		if (applHistoryList == null || applHistoryList.size() == 0) {
			// 入居希望等調書の申請書類履歴情報が取得できなかった場合
			// 提示不可で中断
			ServiceHelper.addWarnResultMessage(dto, MessageIdConstant.W_SKF_1001, nyukyoShoninMsg, CodeConstant.NONE);
			return false;
		}
		SkfApplHistoryInfoUtilsGetApplStatusInfoExp applHistory = applHistoryList.get(0);
		// 入居希望等調書の申請ステータスが承認済ではない場合
		if (NfwStringUtils.isNotEmpty(applHistory.getApplStatus())) {
			switch (applHistory.getApplStatus()) {
			case CodeConstant.STATUS_SHONIN_ZUMI:
				break;
			default:
				ServiceHelper.addWarnResultMessage(dto, MessageIdConstant.W_SKF_1001, nyukyoShoninMsg,
						CodeConstant.NONE);
				return false;
			}
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
	public boolean updateDispInfoOfBihin(String applNo, Skf2030Sc002CommonDto dto) {
		// 登録備品情報を設定
		List<SkfBihinInfoUtilsGetBihinMasterInfoExp> bihinMasterList = skfBihinInfoUtils.getBihinMasterList();
		if (bihinMasterList == null || bihinMasterList.size() <= 0) {
			return false;
		}
		for (SkfBihinInfoUtilsGetBihinMasterInfoExp bihinData : bihinMasterList) {
			String bihinCd = bihinData.getBihinCd();
			Skf2030TBihin updData = new Skf2030TBihin();
			updData.setCompanyCd(companyCd);
			updData.setApplNo(applNo);
			updData.setBihinCd(bihinCd);
			String bihinAppl = CodeConstant.NONE;
			String bihinState = CodeConstant.NONE;
			String bihinAdjust = CodeConstant.NONE;
			switch (bihinCd) {
			case CodeConstant.BIHIN_WASHER:
				bihinAppl = dto.getBihinAppl11();
				bihinState = dto.getBihinState11();
				bihinAdjust = dto.getBihinAdjust11();
				break;
			case CodeConstant.BIHIN_FREEZER:
				bihinAppl = dto.getBihinAppl12();
				bihinState = dto.getBihinState12();
				bihinAdjust = dto.getBihinAdjust12();
				break;
			case CodeConstant.BIHIN_OVEN:
				bihinAppl = dto.getBihinAppl13();
				bihinState = dto.getBihinState13();
				bihinAdjust = dto.getBihinAdjust13();
				break;
			case CodeConstant.BIHIN_CLENER:
				bihinAppl = dto.getBihinAppl14();
				bihinState = dto.getBihinState14();
				bihinAdjust = dto.getBihinAdjust14();
				break;
			case CodeConstant.BIHIN_RICE_COOKER:
				bihinAppl = dto.getBihinAppl15();
				bihinState = dto.getBihinState15();
				bihinAdjust = dto.getBihinAdjust15();
				break;
			case CodeConstant.BIHIN_TV:
				bihinAppl = dto.getBihinAppl16();
				bihinState = dto.getBihinState16();
				bihinAdjust = dto.getBihinAdjust16();
				break;
			case CodeConstant.BIHIN_TV_STANDS:
				bihinAppl = dto.getBihinAppl17();
				bihinState = dto.getBihinState17();
				bihinAdjust = dto.getBihinAdjust17();
				break;
			case CodeConstant.BIHIN_KOTATSU:
				bihinAppl = dto.getBihinAppl18();
				bihinState = dto.getBihinState18();
				bihinAdjust = dto.getBihinAdjust18();
				break;
			case CodeConstant.BIHIN_KICHEN_CABINET:
				bihinAppl = dto.getBihinAppl19();
				bihinState = dto.getBihinState19();
				bihinAdjust = dto.getBihinAdjust19();
				break;
			}
			updData.setBihinAppl(bihinAppl);
			updData.setBihinState(bihinState);
			updData.setBihinAdjust(bihinAdjust);
			if (!updateBihinInfo(updData)) {
				ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
				return false;
			}
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
	 * コメント欄入力チェック
	 * 
	 * @param dto
	 * @param checkFlag
	 * @return
	 * @throws Exception
	 */
	public boolean validateReason(Skf2030Sc002CommonDto dto, boolean checkFlag) throws Exception {

		String commentNote = dto.getCommentNote();

		// エラーメッセージがある場合、メッセージを表示して処理を中断
		if (checkFlag && NfwStringUtils.isEmpty(commentNote)) {
			ServiceHelper.addErrorResultMessage(dto, new String[] { "commentNote" }, MessageIdConstant.E_SKF_1048,
					REASON_LABEL);
			return false;
		}

		// 値が4000バイトを超えた際はエラー
		int commentMaxLength = Integer.parseInt(PropertyUtils.getValue("skf2020.skf2020_sc003.commentMaxLength"));
		if (NfwStringUtils.isNotEmpty(commentNote) && commentMaxLength < commentNote.getBytes("UTF-8").length) {
			ServiceHelper.addErrorResultMessage(dto, new String[] { "commentNote" }, MessageIdConstant.E_SKF_1049,
					REASON_LABEL, commentMaxLength / 2);
			return false;
		}

		return true;
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
		Map<String, String> loginUserInfoMap = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		String userId = loginUserInfoMap.get("userCd");
		// 排他チェック用データ取得
		Object forUpdateObject = menuScopeSessionBean.get(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2030SC002);
		Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> forUpdateMap = skf2030Fc001BihinKiboShinseiDataImport
				.forUpdateMapDownCaster(forUpdateObject);
		skf2030Fc001BihinKiboShinseiDataImport.setUpdateDateForUpdateSQL(forUpdateMap);

		// 連携処理開始
		List<String> resultBatch = skf2030Fc001BihinKiboShinseiDataImport.doProc(companyCd, shainNo, applNo, status,
				userId, pageId);
		// セッション情報削除
		menuScopeSessionBean.remove(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2030SC002);
		return resultBatch;
	}

}
