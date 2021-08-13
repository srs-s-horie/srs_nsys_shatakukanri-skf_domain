package jp.co.c_nexco.skf.skf2050.domain.service.skf2050sc002;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfApplHistoryInfoUtils.SkfApplHistoryInfoUtilsGetApplHistoryInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBatchUtils.SkfBatchUtilsGetMultipleTablesUpdateDateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBihinInfoUtils.SkfBihinInfoUtilsGetBihinInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBihinInfoUtils.SkfBihinInfoUtilsUpdateBihinHenkyakuShinseiExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentListExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfTeijiDataInfoUtils.SkfTeijiDataInfoUtilsGetSKSDairininInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2050TBihinHenkyakuShinsei;
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
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfShatakuInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfTeijiDataInfoUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2050Fc001BihinHenkyakuSinseiDataImport;
import jp.co.c_nexco.skf.skf2050.domain.dto.skf2050Sc002common.Skf2050Sc002CommonDto;

@Service
public class Skf2050Sc002SharedService {

	private String companyCd = CodeConstant.C001;

	private final String NO_DATA_MESSAGE = "初期表示中に";

	private final String BIHIN_NAME = "bihinName";
	private final String BIHIN_STATUS = "bihinStatus";
	private final String BIHIN_ADJUST = "bihinAdjust";

	// 排他処理用
	private final String APPL_HISTORY_KEY_LAST_UPDATE_DATE = "skf2010_t_appl_history_UpdateDate";
	private final String BIHIN_HENKYAKU_SHINSEI_KEY_LAST_UPDATE_DATE = "skf2030_t_bihin_henkyaku_shinsei_UpdateDate";

	@Autowired
	private Skf2050Fc001BihinHenkyakuSinseiDataImport skf2050Fc001BihinHenkyakuSinseiDataImport;

	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private SkfApplHistoryInfoUtils skfApplHistoryInfoUtils;
	@Autowired
	private SkfBihinInfoUtils skfBihinInfoUtils;
	@Autowired
	private SkfTeijiDataInfoUtils skfTeijiDataInfoUtils;
	@Autowired
	private SkfCommentUtils skfCommentUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfShatakuInfoUtils skfShatakuInfoUtils;

	/**
	 * 画面情報の設定を行います
	 * 
	 * @param dto
	 * @return
	 */
	public boolean setDisplayData(Skf2050Sc002CommonDto dto) {
		// 申請書類管理番号
		String applNo = dto.getApplNo();
		// 申請状況
		String applStatus = dto.getApplStatus();
		// 承認者１
		String agreeName1 = CodeConstant.NONE;
		// ビューモード
		boolean viewMode = false;
		// ログインユーザー情報取得
		Map<String, String> loginUserInfo = skfLoginUserInfoUtils.getSkfLoginUserInfo();

		// 申請状況テキストを設定
		Map<String, String> applStatusMap = new HashMap<String, String>();
		applStatusMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_STATUS);
		dto.setApplStatusText(applStatusMap.get(applStatus));

		// 申請書類履歴取得
		List<SkfApplHistoryInfoUtilsGetApplHistoryInfoExp> applHistoryList = skfApplHistoryInfoUtils
				.getApplHistoryInfo(companyCd, applNo);
		if (applHistoryList == null || applHistoryList.size() <= 0) {
			// データ件数0件の場合はメッセージ
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1078, NO_DATA_MESSAGE);
			return false;
		}
		SkfApplHistoryInfoUtilsGetApplHistoryInfoExp applHistory = applHistoryList.get(0);

		agreeName1 = applHistory.getAgreName1();
		// 承認者１がログインユーザーと同じ場合はビューモードに切り替え
		if (CheckUtils.isEqual(agreeName1, loginUserInfo.get("userName"))) {
			viewMode = true;
		}

		// 排他処理用に最終更新日付を保持する
		dto.addLastUpdateDate(APPL_HISTORY_KEY_LAST_UPDATE_DATE, applHistory.getLastUpdateDate());
		dto.setShainNo(applHistory.getShainNo());

		// 画面の表示項目を取得して表示する。
		Skf2050TBihinHenkyakuShinsei bihinHenkyaku = new Skf2050TBihinHenkyakuShinsei();
		bihinHenkyaku = skfBihinInfoUtils.getBihinHenkyakuShinseiInfo(companyCd, applNo);
		if (bihinHenkyaku == null) {
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1078, NO_DATA_MESSAGE);
			return false;
		} else {
			setDispItem(applStatus, bihinHenkyaku, dto, viewMode);
			setShinseiInfo(bihinHenkyaku, dto);
		}

		// 画面の搬出予定備品の項目を取得する
		List<SkfBihinInfoUtilsGetBihinInfoExp> bihinShinseiList = new ArrayList<SkfBihinInfoUtilsGetBihinInfoExp>();

		// 各備品項目の表示情報を取得
		Map<String, String> bihinStatusMap = skfGenericCodeUtils
				.getGenericCode(FunctionIdConstant.GENERIC_CODE_EQUIPMENT_STATE);
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
				// 対象（備品調整区分設定）
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
		boolean commentBtnFlg = true;
		List<SkfCommentUtilsGetCommentListExp> commentList = skfCommentUtils.getCommentList(companyCd, applNo,
				CodeConstant.STATUS_SHONIN1);
		if (commentList == null || commentList.size() <= 0) {
			// コメントがなければコメント表示ボタンを非表示にする
			commentBtnFlg = false;
		}
		dto.setCommentBtnVisibled(commentBtnFlg);
		// ステータスが「31：承認１」の時、承認1のコメントをコメント欄に表示する
		if (CheckUtils.isEqual(applStatus, CodeConstant.STATUS_SHONIN1)) {
			List<SkfCommentUtilsGetCommentInfoExp> commentInfo = new ArrayList<SkfCommentUtilsGetCommentInfoExp>();
			commentInfo = skfCommentUtils.getCommentInfo(companyCd, applNo, applStatus);
			if (commentInfo != null && commentInfo.size() > 0) {
				String commentNote = commentInfo.get(0).getCommentNote();
				dto.setCommentNote(commentNote);
			}
		}

		return true;
	}

	/**
	 * 申請情報履歴テーブルの更新を行います
	 * 
	 * @param newApplStatus
	 * @param dto
	 * @return
	 */
	@Transactional
	public boolean saveApplHistory(String newApplStatus, Skf2050Sc002CommonDto dto) {
		// ログインユーザー情報取得
		Map<String, String> loginUserInfo = skfLoginUserInfoUtils.getSkfLoginUserInfo();

		// 「申請書類履歴テーブル」よりステータスを更新
		String applNo = dto.getApplNo();
		String shainNo = dto.getShainNo();
		String applId = dto.getApplId();
		String applStatus = newApplStatus;

		// 対象申請書類履歴を取得
		List<SkfApplHistoryInfoUtilsGetApplHistoryInfoExp> applHistoryList = new ArrayList<SkfApplHistoryInfoUtilsGetApplHistoryInfoExp>();
		SkfApplHistoryInfoUtilsGetApplHistoryInfoExp applHistoryInfo = new SkfApplHistoryInfoUtilsGetApplHistoryInfoExp();
		applHistoryList = skfApplHistoryInfoUtils.getApplHistoryInfo(companyCd, applNo);
		if (applHistoryList != null && applHistoryList.size() > 0) {
			applHistoryInfo = applHistoryList.get(0);
		}

		String shoninName1 = null;
		String shoninName2 = null;
		Date agreDate = null;
		switch (applStatus) {
		case CodeConstant.STATUS_SHONIN1:
			// 承認1済
			// 承認者名1を設定
			shoninName1 = loginUserInfo.get("userName");
			break;
		case CodeConstant.STATUS_SHONIN_ZUMI:
			// 承認済
			// 承認者名2を設定
			shoninName2 = loginUserInfo.get("userName");
			shoninName1 = applHistoryInfo.getAgreName1();
			agreDate = new Date();
			break;
		default:
			// その他（審査中、同意済等）
			// 承認者名1を設定
			shoninName1 = loginUserInfo.get("userName");
		}

		Map<String, String> errorMsg = new HashMap<String, String>();
		Date lastUpdateDate = dto.getLastUpdateDate(APPL_HISTORY_KEY_LAST_UPDATE_DATE);
		boolean result = updateApplHistoryAgreeStatus(applNo, applId, shainNo, applStatus, agreDate, shoninName1,
				shoninName2, lastUpdateDate, errorMsg);
		if (!result) {
			ServiceHelper.addErrorResultMessage(dto, null, errorMsg.get("error"));
			return false;
		}

		// コメント更新
		String commentNote = dto.getCommentNote();
		boolean commentErrorMessage = skfCommentUtils.insertComment(CodeConstant.C001, applNo, applStatus, commentNote,
				errorMsg);
		if (!commentErrorMessage) {
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
	 * @param viewMode
	 */
	private void setDispItem(String applStatus, Skf2050TBihinHenkyakuShinsei bihinHenkyaku, Skf2050Sc002CommonDto dto,
			boolean viewMode) {
		// 搬出立会日
		if (NfwStringUtils.isNotEmpty(bihinHenkyaku.getSessionDay())) {
			dto.setSessionDay(bihinHenkyaku.getSessionDay());
		}
		// 搬出立会時刻
		if (NfwStringUtils.isNotEmpty(bihinHenkyaku.getSessionTime())) {
			dto.setSessionTime(bihinHenkyaku.getSessionTime());
		}
		// 備品返却完了日
		if (NfwStringUtils.isNotEmpty(bihinHenkyaku.getCompletionDay())) {
			String completionDay = skfDateFormatUtils.dateFormatFromString(bihinHenkyaku.getCompletionDay(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			dto.setCompletionDay(completionDay);
		}

		switch (applStatus) {
		case CodeConstant.STATUS_KAKUNIN_IRAI:
		case CodeConstant.STATUS_HANSYUTSU_MACHI:
			// ステータスが確認依頼、搬出待ちの場合の画面の表示
			// 修正依頼、確認、承認ボタン全て非表示
			dto.setAllButtonEscape(true);
			// 搬出完了日の非表示
			dto.setCarryOut(false);
			// コメント欄非表示
			dto.setCommentAreaVisibled(false);
			// 備品返却立会日時を作成する
			dto.setSessionDate(createSessionDate(dto));

			break;
		case CodeConstant.STATUS_DOI_ZUMI:
			// ステータスが同意済の場合の画面の表示
			// 「修正依頼」「確認」ボタンを表示
			dto.setCarryOut(false);
			// 備品返却立会日時を作成する
			dto.setSessionDate(createSessionDate(dto));

			// 立会代理人情報を取得する
			List<SkfTeijiDataInfoUtilsGetSKSDairininInfoExp> dairiList = new ArrayList<SkfTeijiDataInfoUtilsGetSKSDairininInfoExp>();
			dairiList = skfTeijiDataInfoUtils.getSKSDairininInfo(dto.getShainNo(), CodeConstant.SYS_TAIKYO_KBN);
			if (dairiList != null && dairiList.size() > 0) {
				SkfTeijiDataInfoUtilsGetSKSDairininInfoExp dairiData = dairiList.get(0);
				if (NfwStringUtils.isNotEmpty(dairiData.getTatiaiDairiName())) {
					dto.setDairininName(dairiData.getTatiaiDairiName());
				}
				if (NfwStringUtils.isNotEmpty(dairiData.getTatiaiDairiApoint())) {
					dto.setDairiRenrakuSaki(dairiData.getTatiaiDairiApoint());
				}
			}

			break;
		case CodeConstant.STATUS_HANSYUTSU_ZUMI:
		case CodeConstant.STATUS_SHONIN1:
			// ステータスが搬出済、承認中の場合の画面表示
			// 「承認」ボタンを表示
			dto.setCarryOut(true);
			// ビューモード
			if (viewMode) {
				dto.setAllButtonEscape(true);
			}
			break;
		case CodeConstant.STATUS_SHONIN_ZUMI:
			// ステータスが承認の場合の画面表示
			// 修正依頼、確認、承認ボタン全て非表示
			dto.setAllButtonEscape(true);
			// コメント欄非表示
			dto.setCommentAreaVisibled(false);
			// 搬出完了日表示
			dto.setCarryOut(true);
			break;
		default:
			// その他の場合
			// ステータスが承認の場合の画面表示
			// 修正依頼、確認、承認ボタン全て非表示
			dto.setAllButtonEscape(true);
			// コメント欄非表示
			dto.setCommentAreaVisibled(false);
			// 返却立会日、搬出完了日共に非表示
			dto.setAllDateAreaEscape(true);
			break;
		}
		
		// ログインユーザー情報取得
		Map<String, String> loginUserInfo = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		// 承認可能ロールのみの処理		
		boolean result = skfLoginUserInfoUtils.isAgreeAuthority( CodeConstant.C001, FunctionIdConstant.R0105, loginUserInfo.get("roleId"), applStatus);
		if(!result){
			// 修正依頼、確認、承認ボタン全て非表示
			dto.setAllButtonEscape(true);
			// コメント欄非表示
			dto.setCommentAreaVisibled(false);
		}

		return;
	}

	private String createSessionDate(Skf2050Sc002CommonDto dto) {
		String sessionDay = dto.getSessionDay();
		if (NfwStringUtils.isNotEmpty(sessionDay)) {
			sessionDay = skfDateFormatUtils.dateFormatFromString(sessionDay,
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
		} else {
			sessionDay = CodeConstant.NONE;
		}
		// 返却立会日(時)を取得する
		String sessionTime = dto.getSessionTime();
		if (NfwStringUtils.isNotEmpty(sessionTime)) {
			Map<String, String> sessionTimeMap = skfGenericCodeUtils
					.getGenericCode(FunctionIdConstant.GENERIC_CODE_REQUEST_TIME);
			sessionTime = sessionTimeMap.get(sessionTime);
		} else {
			sessionTime = CodeConstant.NONE;
		}
		// スペース区切りで表示する
		return sessionDay + CodeConstant.SPACE + sessionTime;
	}

	/**
	 * 備品返却確認情報をセットします
	 * 
	 * @param bihinHenkyaku
	 * @param dto
	 */
	private void setShinseiInfo(Skf2050TBihinHenkyakuShinsei bihinHenkyaku, Skf2050Sc002CommonDto dto) {
		// 取得したデータをDtoにセットします
		// 機関
		if (NfwStringUtils.isNotEmpty(bihinHenkyaku.getAgency())) {
			dto.setAgency(bihinHenkyaku.getAgency());
		}
		// 部等
		if (NfwStringUtils.isNotEmpty(bihinHenkyaku.getAffiliation1())) {
			dto.setAffiliation1(bihinHenkyaku.getAffiliation1());
		}
		// 室・課等
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
			dto.setDairiRenrakuSaki(bihinHenkyaku.getTatiaiDairiApoint());
		}

		return;
	}

	public boolean updateApplHistoryAgreeStatus(String applNo, String applId, String shainNo, String applStatus,
			Date agreDate, String shoninName1, String shoninName2, Date lastUpdateDate, Map<String, String> errorMsg) {

		boolean result = skfApplHistoryInfoUtils.updateApplHistoryAgreeStatus(companyCd, shainNo, applNo, applId, null,
				null, applStatus, agreDate, shoninName1, shoninName2, lastUpdateDate, errorMsg);
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
		Object forUpdateObject = menuScopeSessionBean.get(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2050SC002);
		Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> forUpdateMap = skf2050Fc001BihinHenkyakuSinseiDataImport
				.forUpdateMapDownCaster(forUpdateObject);
		skf2050Fc001BihinHenkyakuSinseiDataImport.setUpdateDateForUpdateSQL(forUpdateMap);

		// 連携処理開始
		List<String> resultBatch = skf2050Fc001BihinHenkyakuSinseiDataImport.doProc(companyCd, shainNo, applNo, status,
				userId, pageId);
		// セッション情報削除
		menuScopeSessionBean.remove(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2050SC002);
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
