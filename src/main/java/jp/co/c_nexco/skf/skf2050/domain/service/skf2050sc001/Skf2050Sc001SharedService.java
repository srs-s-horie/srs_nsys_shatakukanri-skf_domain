package jp.co.c_nexco.skf.skf2050.domain.service.skf2050sc001;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2050Sc001.Skf2050Sc001GetTaikyobiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2050Sc001.Skf2050Sc001GetTaikyobiInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfApplHistoryInfoUtils.SkfApplHistoryInfoUtilsGetApplHistoryInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBatchUtils.SkfBatchUtilsGetMultipleTablesUpdateDateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBihinInfoUtils.SkfBihinInfoUtilsGetBihinInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBihinInfoUtils.SkfBihinInfoUtilsUpdateBihinHenkyakuShinseiExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2050TBihinHenkyakuShinsei;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2050Sc001.Skf2050Sc001GetTaikyobiInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfRollBack.SkfRollBackExpRepository;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
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
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfShatakuInfoUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2050Fc001BihinHenkyakuSinseiDataImport;
import jp.co.c_nexco.skf.skf2050.domain.dto.skf2050Sc001common.Skf2050Sc001CommonDto;

@Service
public class Skf2050Sc001SharedService {

	private String companyCd = CodeConstant.C001;
	private MenuScopeSessionBean menuScopeSessionBean;

	private final String NO_DATA_MESSAGE = "初期表示中に";

	private final String BIHIN_NAME = "bihinName";
	private final String BIHIN_STATUS = "bihinStatus";
	private final String BIHIN_ADJUST = "bihinAdjust";

	// 排他処理用
	private final String APPL_HISTORY_KEY_LAST_UPDATE_DATE = "skf2010_t_appl_history_UpdateDate";
	private final String BIHIN_HENKYAKU_SHINSEI_KEY_LAST_UPDATE_DATE = "skf2030_t_bihin_henkyaku_shinsei_UpdateDate";

	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private SkfApplHistoryInfoUtils skfApplHistoryInfoUtils;
	@Autowired
	private SkfBihinInfoUtils skfBihinInfoUtils;
	@Autowired
	private SkfCommentUtils skfCommentUtils;
	@Autowired
	private SkfDropDownUtils skfDropDownUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfShatakuInfoUtils skfShatakuInfoUtils;

	@Autowired
	private Skf2050Sc001GetTaikyobiInfoExpRepository skf2050Sc001GetTaikyobiInfoExpRepository;
	@Autowired
	private Skf2050Fc001BihinHenkyakuSinseiDataImport skf2050Fc001BihinHenkyakuSinseiDataImport;
	@Autowired
	private SkfRollBackExpRepository skfRollBackExpRepository;

	public void setMenuScopeSessionBean(MenuScopeSessionBean bean) {
		this.menuScopeSessionBean = bean;
	}

	/**
	 * 画面情報の設定を行います
	 * 
	 * @param dto
	 * @return
	 */
	public boolean setDisplayData(Skf2050Sc001CommonDto dto) {
		// 申請書類管理番号
		String applNo = dto.getApplNo();
		// 申請状況
		String applStatus = dto.getApplStatus();

		// 申請状況テキストを設定
		Map<String, String> applStatusMap = new HashMap<String, String>();
		applStatusMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_STATUS);
		dto.setApplStatusText(applStatusMap.get(applStatus));

		// 申請書類履歴取得
		List<SkfApplHistoryInfoUtilsGetApplHistoryInfoExp> applHistoryList = skfApplHistoryInfoUtils
				.getApplHistoryInfo(companyCd, applNo);
		if (applHistoryList == null || applHistoryList.size() <= 0) {
			if (dto.getApplStatus().equals(CodeConstant.NYUTAIKYO_APPL_STATUS_KAKUNIN_IRAI)) {
				dto.setAllNotVisible(true);
				dto.setCommentBtnVisible(false);
				dto.setCarryOutVisible(false);
			} else if (dto.getApplStatus().equals(CodeConstant.NYUTAIKYO_APPL_STATUS_HANSHUTSU_MACHI)) {
				dto.setCommentBtnVisible(false);
			}
			// データ件数0件の場合はメッセージ
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1135);
			return false;
		}
		SkfApplHistoryInfoUtilsGetApplHistoryInfoExp applHistory = applHistoryList.get(0);

		// 排他処理用に最終更新日付を保持する
		dto.addLastUpdateDate(APPL_HISTORY_KEY_LAST_UPDATE_DATE, applHistory.getLastUpdateDate());

		// 画面の表示項目を取得して表示する。
		Skf2050TBihinHenkyakuShinsei bihinHenkyaku = new Skf2050TBihinHenkyakuShinsei();
		bihinHenkyaku = skfBihinInfoUtils.getBihinHenkyakuShinseiInfo(companyCd, applNo);
		if (bihinHenkyaku == null) {
			if (dto.getApplStatus().equals(CodeConstant.NYUTAIKYO_APPL_STATUS_KAKUNIN_IRAI)) {
				dto.setAllNotVisible(true);
				dto.setCommentBtnVisible(false);
				dto.setCarryOutVisible(false);
			} else if (dto.getApplStatus().equals(CodeConstant.NYUTAIKYO_APPL_STATUS_HANSHUTSU_MACHI)) {
				dto.setCommentBtnVisible(false);
			}
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1135);
			return false;
		} else {
			setDispItem(applStatus, bihinHenkyaku, dto);
			setShinseiInfo(bihinHenkyaku, dto);
		}
		// 排他処理用に最終更新日付を保持する
		dto.addLastUpdateDate(BIHIN_HENKYAKU_SHINSEI_KEY_LAST_UPDATE_DATE, bihinHenkyaku.getUpdateDate());

		// 画面の搬出予定備品の項目を取得する
		List<SkfBihinInfoUtilsGetBihinInfoExp> bihinShinseiList = new ArrayList<SkfBihinInfoUtilsGetBihinInfoExp>();

		// 各備品項目の表示情報を取得
		Map<String, String> bihinStatusMap = skfGenericCodeUtils
				.getGenericCode(FunctionIdConstant.GENERIC_CODE_BIHINSTATUS_KBN);
		Map<String, String> bihinAdjustMap = skfGenericCodeUtils
				.getGenericCode(FunctionIdConstant.GENERIC_CODE_CARRING_OUT_KBN);

		bihinShinseiList = skfBihinInfoUtils.getBihinInfo(companyCd, applNo);
		List<Map<String, String>> bihinList = new ArrayList<Map<String, String>>();
		if (bihinShinseiList != null && bihinShinseiList.size() > 0) {
			for (SkfBihinInfoUtilsGetBihinInfoExp bihinInfo : bihinShinseiList) {
				Map<String, String> bihinMap = new HashMap<String, String>();
				// 備品名
				bihinMap.put(BIHIN_NAME, bihinInfo.getBihinName());
				// 備付状況
				bihinMap.put(BIHIN_STATUS, bihinStatusMap.get(bihinInfo.getBihinState()));
				// 対象
				String bihinAdjustText = bihinAdjustMap.get(bihinInfo.getBihinAdjust());
				if (NfwStringUtils.isEmpty(bihinAdjustText)) {
					bihinAdjustText = CodeConstant.HYPHEN;
				}
				bihinMap.put(BIHIN_ADJUST, bihinAdjustText);
				bihinList.add(bihinMap);
			}
			dto.setBihinInfoList(bihinList);
		}

		// コメント一覧取得
		List<SkfCommentUtilsGetCommentInfoExp> commentList = skfCommentUtils.getCommentInfo(companyCd, applNo, null);
		if (commentList == null || commentList.size() <= 0) {
			// コメントがなければコメント表示ボタンを非表示にする
			dto.setCommentBtnVisible(false);
		}

		return true;
	}

	@Transactional
	public boolean saveBihinHenkyakuInfo(String newApplStatus, Skf2050Sc001CommonDto dto,
			MenuScopeSessionBean sessionBean) {
		// ログインユーザー情報取得（代行ユーザー対応）
		Map<String, String> loginUserInfo = skfLoginUserInfoUtils.getSkfLoginUserInfoFromAlterLogin(sessionBean);

		// 「申請書類履歴テーブル」よりステータスを更新
		String applNo = dto.getApplNo();
		String shainNo = dto.getShainNo();
		String applId = dto.getApplId();
		String applStatus = newApplStatus;
		Map<String, String> errorMsg = new HashMap<String, String>();
		Date lastUpdateDate = dto.getLastUpdateDate(APPL_HISTORY_KEY_LAST_UPDATE_DATE);
		boolean result = updateApplHistoryAgreeStatus(applNo, applId, shainNo, applStatus, lastUpdateDate, errorMsg);
		if (!result) {
			ServiceHelper.addErrorResultMessage(dto, null, errorMsg.get("error"));
			return false;
		}

		// 「同意する」の時のみ備品返却確認テーブル更新
		if (!CheckUtils.isEqual(newApplStatus, CodeConstant.STATUS_DOI_SHINAI)) {
			// 備品返却テーブルの更新
			String sessionDay = dto.getSessionDay();
			String sessionTime = dto.getSessionTime();
			String completionDay = dto.getCompletionDay();
			lastUpdateDate = dto.getLastUpdateDate(BIHIN_HENKYAKU_SHINSEI_KEY_LAST_UPDATE_DATE);
			if (!updateBihinHenkyakuSinseiInfo(applNo, sessionDay, sessionTime, completionDay, lastUpdateDate)) {
				ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
				return false;
			}
		}

		// コメント情報の登録
		String commentNote = dto.getCommentNote();
		String commentName = loginUserInfo.get("userName");
		if (NfwStringUtils.isNotEmpty(loginUserInfo.get("affiliation2Name"))) {
			commentName = loginUserInfo.get("affiliation2Name") + "<br />" + commentName;
		}
		if (!skfCommentUtils.insertComment(companyCd, applNo, applStatus, commentName, commentNote, errorMsg)) {
			ServiceHelper.addErrorResultMessage(dto, null, errorMsg.get("error"));
			return false;
		}

		// 社宅管理データ連携処理実行
		String pageId = FunctionIdConstant.SKF2050_SC001;
		List<String> resultBatch = this.doShatakuRenkei(this.menuScopeSessionBean, shainNo, applNo, newApplStatus,
				pageId);
		if (resultBatch != null) {
			skf2050Fc001BihinHenkyakuSinseiDataImport.addResultMessageForDataLinkage(dto, resultBatch);
			skfRollBackExpRepository.rollBack();
			return false;
		}

		return true;
	}

	/**
	 * 画面表示設定を行います
	 * 
	 * @param applStatus
	 * @param bihinHenkyaku
	 * @param dto
	 */
	private void setDispItem(String applStatus, Skf2050TBihinHenkyakuShinsei bihinHenkyaku, Skf2050Sc001CommonDto dto) {

		switch (applStatus) {
		case CodeConstant.STATUS_SHINSACHU:
		case CodeConstant.STATUS_DOI_ZUMI:
		case CodeConstant.STATUS_DOI_SHINAI:
			// ステータスが審査中,同意しない、同意済の場合の画面の表示
			// 「前に戻る」ボタン以外非表示
			dto.setCarryOutVisible(true);
			dto.setCarryOutBtnRemove(true);
			// 備品返却立会日(日)非活性
			// 備品返却立会日(時)非活性
			dto.setCompletionVisible(false);
			dto.setMaskPattern("ST01");
			dto.setSessionTimeDisabled("true");
			// 搬出完了日の非表示
			dto.setCompletionVisible(false);
			// 立会人項目非表示
			dto.setDairininVisible(false);
			// コメント非表示
			dto.setCommentAreaVisible(false);
			dto.setCommentBtnVisible(false);
			break;
		case CodeConstant.STATUS_KAKUNIN_IRAI:
			// ステータスが確認依頼の場合の画面の表示
			// 「同意する」「同意しない」ボタン表示
			dto.setCarryOutVisible(false);
			// 備品返却立会日(日)活性
			// 備品返却立会日(時)活性
			dto.setCompletionVisible(false);
			// 立会人項目非表示
			dto.setDairininVisible(false);
			// コメント表示
			dto.setCommentAreaVisible(true);
			dto.setCommentBtnVisible(true);
			break;
		case CodeConstant.STATUS_HANSYUTSU_MACHI:
			// ステータスが搬出待ちの場合の画面の表示
			// 「搬出完了」ボタン表示
			dto.setCarryOutBtnRemove(false);
			// 「備品返却完了日」表示
			dto.setCarryOutVisible(true);
			// 立会人項目表示
			dto.setDairininVisible(true);
			// コメント表示
			dto.setCommentAreaVisible(true);
			dto.setCommentBtnVisible(true);
			break;
		case CodeConstant.STATUS_HANSYUTSU_ZUMI:
		case CodeConstant.STATUS_SHONIN1:
		case CodeConstant.STATUS_SHONIN_ZUMI:
			// ステータスが搬出済、承認中、承認の場合の画面表示
			// 「前に戻る」ボタン以外非表示
			dto.setCarryOutVisible(true);
			dto.setCarryOutBtnRemove(true);
			// 「備品返却完了日」表示（非活性）
			dto.setCompletionVisible(true);
			dto.setMaskPattern("ST30");
			// 立会人項目表示
			dto.setDairininVisible(true);
			// 「コメント表示」ボタン表示
			dto.setCommentAreaVisible(false);
			dto.setCommentBtnVisible(true);
			break;
		default:
			// その他の場合
			// 「前に戻る」ボタン以外非表示
			dto.setCarryOutVisible(true);
			// 「備品返却希望日」「備品返却完了日」「承認者へのコメント」全て非表示
			dto.setAllNotVisible(true);
			break;
		}

		// 搬出立会日
		if (NfwStringUtils.isNotEmpty(bihinHenkyaku.getSessionDay())) {
			dto.setSessionDay(bihinHenkyaku.getSessionDay());
		}
		// 搬出立会時刻
		if (NfwStringUtils.isNotEmpty(bihinHenkyaku.getSessionTime())) {
			dto.setSessionTime(bihinHenkyaku.getSessionTime());
		}
		// 搬出立会時刻ドロップダウン
		List<Map<String, Object>> ddSessionTimeList = new ArrayList<Map<String, Object>>();
		ddSessionTimeList = skfDropDownUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_REQUESTTIME_KBN,
				bihinHenkyaku.getSessionTime(), false);
		dto.setDdSessionTimeList(ddSessionTimeList);

		// 備品返却完了日
		if (NfwStringUtils.isNotEmpty(bihinHenkyaku.getCompletionDay())) {
			dto.setCompletionDay(bihinHenkyaku.getCompletionDay());
		}
		return;
	}

	/**
	 * 備品返却確認情報をセットします
	 * 
	 * @param bihinHenkyaku
	 * @param dto
	 */
	private void setShinseiInfo(Skf2050TBihinHenkyakuShinsei bihinHenkyaku, Skf2050Sc001CommonDto dto) {
		// 取得したデータをDtoにセットします
		// 機関
		if (NfwStringUtils.isNotEmpty(bihinHenkyaku.getAgency())) {
			dto.setAgency(bihinHenkyaku.getAgency());
		}
		// 部等
		if (NfwStringUtils.isNotEmpty(bihinHenkyaku.getAffiliation1())) {
			dto.setAffiliation1(bihinHenkyaku.getAffiliation1());
		}
		// 室、チーム又は課
		if (NfwStringUtils.isNotEmpty(bihinHenkyaku.getAffiliation2())) {
			dto.setAffiliation2(bihinHenkyaku.getAffiliation2());
		}
		// 勤務地のTEL
		if (NfwStringUtils.isNotEmpty(bihinHenkyaku.getTel())) {
			dto.setTel(bihinHenkyaku.getTel());
		}
		// 社員番号
		if (NfwStringUtils.isNotEmpty(bihinHenkyaku.getShainNo())) {
			dto.setShainNo(bihinHenkyaku.getShainNo());
		}
		// 氏名
		if (NfwStringUtils.isNotEmpty(bihinHenkyaku.getName())) {
			dto.setName(bihinHenkyaku.getName());
		}
		// 等級
		if (NfwStringUtils.isNotEmpty(bihinHenkyaku.getTokyu())) {
			dto.setTokyu(bihinHenkyaku.getTokyu());
		}
		// 性別
		if (NfwStringUtils.isNotEmpty(bihinHenkyaku.getGender())) {
			Map<String, String> genderList = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_GENDER);
			dto.setGender(genderList.get(bihinHenkyaku.getGender()));
		}
		// 社宅名
		if (NfwStringUtils.isNotEmpty(bihinHenkyaku.getNowShatakuName())) {
			dto.setShatakuName(bihinHenkyaku.getNowShatakuName());
		}
		// 室番号
		if (NfwStringUtils.isNotEmpty(bihinHenkyaku.getNowShatakuNo())) {
			dto.setShatakuNo(bihinHenkyaku.getNowShatakuNo());
		}
		// 規格（間取り）
		if (NfwStringUtils.isNotEmpty(bihinHenkyaku.getNowShatakuKikaku())) {
			String shatakuKikaku = getShatakuKikakuKBN(bihinHenkyaku.getNowShatakuKikaku());
			dto.setShatakuKikaku(shatakuKikaku);
		}
		// 面積
		if (NfwStringUtils.isNotEmpty(bihinHenkyaku.getNowShatakuMenseki())) {
			dto.setShatakuMenseki(bihinHenkyaku.getNowShatakuMenseki() + SkfCommonConstant.SQUARE_MASTER);
		}
		// 連絡先
		if (NfwStringUtils.isNotEmpty(bihinHenkyaku.getRenrakuSaki())) {
			dto.setRenrakuSaki(bihinHenkyaku.getRenrakuSaki());
		}
		// 代理人（立会人）
		// 代理人氏名
		if (NfwStringUtils.isNotEmpty(bihinHenkyaku.getTatiaiDairiName())) {
			dto.setDairininName(bihinHenkyaku.getTatiaiDairiName());
		}
		// 代理人連絡先
		if (NfwStringUtils.isNotEmpty(bihinHenkyaku.getTatiaiDairiApoint())) {
			dto.setDairininName(bihinHenkyaku.getTatiaiDairiApoint());
		}

		return;
	}

	public boolean updateApplHistoryAgreeStatus(String applNo, String applId, String shainNo, String applStatus,
			Date lastUpdateDate, Map<String, String> errorMsg) {

		boolean result = skfApplHistoryInfoUtils.updateApplHistoryAgreeStatus(companyCd, shainNo, applNo, applId, null,
				null, applStatus, null, null, null, lastUpdateDate, errorMsg);
		return result;
	}

	/**
	 * 備品返却確認テーブルのデータを更新します
	 * 
	 * @param applNo
	 * @param sessionDay
	 * @param lastUpdateDate
	 * @param sessionTime
	 * @param completionDay
	 * @param lastUpdateDate
	 * @return
	 */
	public boolean updateBihinHenkyakuSinseiInfo(String applNo, String sessionDay, String sessionTime,
			String completionDay, Date lastUpdateDate) {
		SkfBihinInfoUtilsUpdateBihinHenkyakuShinseiExp record = new SkfBihinInfoUtilsUpdateBihinHenkyakuShinseiExp();
		record.setCompanyCd(companyCd);
		record.setApplNo(applNo);
		if (NfwStringUtils.isNotEmpty(sessionDay)) {
			record.setSessionDay(sessionDay);
		}
		if (NfwStringUtils.isNotEmpty(sessionTime)) {
			record.setSessionTime(sessionTime);
		}
		if (NfwStringUtils.isNotEmpty(completionDay)) {
			record.setCompletionDay(completionDay);
		}

		if (!skfBihinInfoUtils.updateBihinHenkyakuShinsei(record, lastUpdateDate)) {
			return false;
		}

		return true;
	}

	/**
	 * 退居日を取得します
	 * 
	 * @param applNo
	 * @return
	 */
	public String getTaikyobiInfo(String applNo) {
		String taikyoDate = CodeConstant.NONE;
		List<Skf2050Sc001GetTaikyobiInfoExp> taikyoInfoList = new ArrayList<Skf2050Sc001GetTaikyobiInfoExp>();
		Skf2050Sc001GetTaikyobiInfoExpParameter param = new Skf2050Sc001GetTaikyobiInfoExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		taikyoInfoList = skf2050Sc001GetTaikyobiInfoExpRepository.getTaikyobiInfo(param);
		if (taikyoInfoList != null && taikyoInfoList.size() > 0) {
			Skf2050Sc001GetTaikyobiInfoExp taikyoInfo = taikyoInfoList.get(0);
			taikyoDate = skfDateFormatUtils.dateFormatFromString(taikyoInfo.getTaikyoDate(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT);
		}
		return taikyoDate;
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
				.getSkfLoginUserInfoFromAlterLogin(this.menuScopeSessionBean);
		String userId = loginUserInfoMap.get("userCd");
		// 排他チェック用データ取得
		Object forUpdateObject = menuScopeSessionBean.get(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2050SC001);
		Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> forUpdateMap = skf2050Fc001BihinHenkyakuSinseiDataImport
				.forUpdateMapDownCaster(forUpdateObject);
		skf2050Fc001BihinHenkyakuSinseiDataImport.setUpdateDateForUpdateSQL(forUpdateMap);

		// 連携処理開始
		List<String> resultBatch = skf2050Fc001BihinHenkyakuSinseiDataImport.doProc(companyCd, shainNo, applNo, status,
				userId, pageId);
		// セッション情報削除
		menuScopeSessionBean.remove(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2050SC001);
		return resultBatch;
	}
	
	/**
	 * 社宅管理システム規格名称取得
	 * 
	 * @param kikakuCd
	 * @return
	 */
	private String getShatakuKikakuKBN(String kikakuCd) {
		String retKikakuName = CodeConstant.NONE;

		retKikakuName = skfShatakuInfoUtils.getShatakuKikakuByCode(kikakuCd);
		return retKikakuName;
	}

}
