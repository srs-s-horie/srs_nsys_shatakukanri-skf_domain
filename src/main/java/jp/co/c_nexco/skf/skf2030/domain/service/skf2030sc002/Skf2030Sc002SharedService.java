package jp.co.c_nexco.skf.skf2030.domain.service.skf2030sc002;

import java.util.ArrayList;
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
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBihinInfoUtils.SkfBihinInfoUtilsGetBihinInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfTeijiDataInfoUtils.SkfTeijiDataInfoUtilsGetSKSDairininInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2030TBihinKiboShinsei;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2030Sc002.Skf2030Sc002GetNyukyobiInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2030Sc002.Skf2030Sc002GetTeijiBihinInfoExpRepository;
import jp.co.c_nexco.skf.common.util.SkfBihinInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfCommentUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfTeijiDataInfoUtils;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfApplHistoryInfoUtils;
import jp.co.c_nexco.skf.skf2030.domain.dto.skf2030Sc002common.Skf2030Sc002CommonDto;

@Service
public class Skf2030Sc002SharedService {

	// 共通会社コード
	private String companyCd = CodeConstant.C001;

	private Map<String, String> bihinInfoMap;
	private Map<String, String> bihinApplMap;
	private Map<String, String> bihinAdjustMap;

	// 排他処理用最終更新日付
	private static final String APPL_HISTORY_KEY_LAST_UPDATE_DATE = "skf2010_t_appl_history_UpdateDate";
	private static final String BIHIN_KIBO_SHINSEI_KEY_LAST_UPDATE_DATE = "skf2030_t_bihin_kibo_shinsei_UpdateDate";
	private static final String BIHIN_KEY_LAST_UPDATE_DATE = "skf2030_t_bihin_UpdateDate";

	private static final String CSS_FLOAT_L = " float-L";

	// メッセージ用定数
	private final String NO_DATA_MESSAGE = "初期表示中に";
	private final String SHATAKU_TEIJI_MSG = "社宅管理システムで提示データを確認";
	private final String SHATAKU_TEIJI_COMP = "（社宅提示データが作成完了されていません。）";
	private final String BIHIN_TEIJI_COMP = "（備品提示データが作成完了されていません。）";

	@Autowired
	private Skf2030Sc002GetNyukyobiInfoExpRepository skf2030Sc002GetNyukyobiInfoExpRepository;
	@Autowired
	private Skf2030Sc002GetTeijiBihinInfoExpRepository skf2030Sc002GetTeijiBihinInfoExpRepository;

	@Autowired
	private SkfApplHistoryInfoUtils skfApplHistoryInfoUtils;
	@Autowired
	private SkfBihinInfoUtils skfBihinInfoUtils;
	@Autowired
	private SkfTeijiDataInfoUtils skfTeijiDataInfoUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfCommentUtils skfCommentUtils;

	public boolean setDisplayData(Skf2030Sc002CommonDto dto, Map<String, String> loginUserInfo,
			Map<String, String> applInfo) {

		boolean bCreateComplete = true;

		// 画面表示用汎用コード取得
		bihinInfoMap = skfGenericCodeUtils.getGenericCode("SKF1051"); // 備品状況
		bihinApplMap = skfGenericCodeUtils.getGenericCode("SKF1049"); // 備品申請区分
		bihinAdjustMap = skfGenericCodeUtils.getGenericCode("SKF1144"); // 備品搬入状況

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
		dto.addLastUpdateDate(BIHIN_KIBO_SHINSEI_KEY_LAST_UPDATE_DATE, bihinShinseiInfo.getLastUpdateDate());

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
			dto.setShatakuKikaku(bihinShinseiInfo.getNowShatakuKikaku());
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
			Map<String, String> sessionTimeMap = skfGenericCodeUtils.getGenericCode("SKF1048");
			dto.setSessionTimeText(sessionTimeMap.get(applTime));
		}
		// 【 連絡先 】 ※入力項目
		if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getRenrakuSaki())) {
			dto.setRenrakuSaki(bihinShinseiInfo.getRenrakuSaki());
		}

		// 【 備品搬入完了 】
		// 備品搬入完了日 ※入力項目
		if (NfwStringUtils.isNotEmpty(bihinShinseiInfo.getCompletionDay())) {
			dto.setCompletionDay(bihinShinseiInfo.getCompletionDay());
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

			Long count = CodeConstant.LONG_ZERO;

			if (bihinInfoList != null && bihinInfoList.size() > 0) {
				boolean isBihinAppl = false;

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
					bihinList.add(bihinMap);
				}
				// 「申請する」セットフラグ
				dto.setBihinCheckFlag(String.valueOf(isBihinAppl));

			}
			// 申請状況が「審査中」の場合、社宅提示データが作成完了されていない場合
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
		List<SkfCommentUtilsGetCommentInfoExp> commentList = skfCommentUtils.getCommentInfo(companyCd,
				applInfo.get("applInfo"), null);
		// コメントがあれば「コメント表示」ボタンを表示
		if (commentList != null && commentList.size() > 0) {
			dto.setCommentViewFlag(true);
		} else {
			dto.setCommentViewFlag(false);
		}

		// 承認可能ロールのみの処理
		switch (loginUserInfo.get("roleId")) {
		case SkfCommonConstant.ADMIN_ROLE1:
		case SkfCommonConstant.ADMIN_ROLE2:
		case SkfCommonConstant.ADMIN_ROLE3:
			dto.setBihinReadOnly(false);
			break;
		default:
			dto.setBihinReadOnly(true);
			break;
		}

		return true;
	}

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
		bihinMap.put("bihinState", bihinStateText);
		bihinMap.put("bihinAppl", bihinApplText);
		bihinMap.put("bihinAdjust", bihinAdjustText);

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
		case CodeConstant.STATUS_SHONIN_ZUMI:
			// ステータス：申請中、審査中
			dto.setCompletionDayVisible(true); // 「搬入完了日」欄 【非表示】

			dto.setDispMode(CodeConstant.VIEW_LEVEL_3);

			// コメント入力欄非表示
			dto.setCommentViewFlag(false);

			// 右側のボタンがある時のみClassにfloat-lを追加
			dto.setFloatL(CSS_FLOAT_L);
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
}
