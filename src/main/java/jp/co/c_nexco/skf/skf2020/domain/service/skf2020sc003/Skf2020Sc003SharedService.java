package jp.co.c_nexco.skf.skf2020.domain.service.skf2020sc003;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetApplHistoryInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetApplHistoryInfoForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetBihinInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetBihinInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetBihinInfoOutExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetBihinInfoOutExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetBihinItemExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetBihinShinseiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetBihinShinseiInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetMaxCycleBillingYYMMExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetParkingRirekiDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetParkingRirekiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetShatakuNameListExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetShatakuNameListExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetShatakuNyukyoKiboInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetShatakuNyukyoKiboInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetTeijiDataInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetTeijiDataInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003UpdateApplHistoryExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TAttachedFile;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchi;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2030TBihin;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2030TBihinKiboShinsei;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc003.Skf2020Sc003GetApplHistoryInfoForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc003.Skf2020Sc003GetBihinInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc003.Skf2020Sc003GetBihinInfoOutExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc003.Skf2020Sc003GetBihinItemExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc003.Skf2020Sc003GetBihinShinseiInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc003.Skf2020Sc003GetMaxCycleBillingYYMMExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc003.Skf2020Sc003GetParkingRirekiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc003.Skf2020Sc003GetShatakuNameListExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc003.Skf2020Sc003GetShatakuNyukyoKiboInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc003.Skf2020Sc003GetTeijiDataInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc003.Skf2020Sc003UpdateApplHistoryExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TAttachedFileRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2020TNyukyoChoshoTsuchiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2030TBihinKiboShinseiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2030TBihinRepository;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.common.utils.PropertyUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtils;
import jp.co.c_nexco.skf.common.util.SkfCommentUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfHtmlCreateUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfShinseiUtils;
import jp.co.c_nexco.skf.skf2020.domain.dto.skf2020Sc003common.Skf2020Sc003CommonDto;

/**
 * Skf2020Sc003 社宅入居希望等調書（アウトソース用）内部処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2020Sc003SharedService {

	// 共通会社コード
	private String companyCd = CodeConstant.C001;

	// カンマ区切りフォーマット
	private NumberFormat nfNum = NumberFormat.getNumberInstance();
	//
	private MenuScopeSessionBean menuScopeSessionBean;
	// 定数一覧
	private final String SHATAKU_TEIJI_MSG = "社宅管理システムで提示データを確認";
	private final String SHATAKU_TEIJI_NONE = "（社宅提示データが取得できませんでした。）";
	private final String SHATAKU_TEIJI_COMP = "（社宅提示データが作成完了されていません。）";
	private final String BIHIN_STATE_SONAETSUKE = "備付";

	@Autowired
	private Skf2020Sc003GetShatakuNyukyoKiboInfoExpRepository skf2020Sc003GetShatakuNyukyoKiboInfoExpRepository;
	@Autowired
	private Skf2020Sc003GetShatakuNameListExpRepository skf2020Sc003GetShatakuNameListExpRepository;
	@Autowired
	private Skf2020Sc003GetBihinInfoExpRepository skf2020Sc003GetBihinInfoExpRepository;
	@Autowired
	private Skf2020Sc003GetBihinInfoOutExpRepository skf2020Sc003GetBihinInfoOutExpRepository;
	@Autowired
	private Skf2020Sc003GetBihinShinseiInfoExpRepository skf2020Sc003GetBihinShinseiInfoExpRepository;
	@Autowired
	private Skf2020Sc003GetTeijiDataInfoExpRepository skf2020Sc003GetTeijiDataInfoExpRepository;
	@Autowired
	private Skf2020Sc003GetMaxCycleBillingYYMMExpRepository skf2020Sc003GetMaxCycleBillingYYMMExpRepository;
	@Autowired
	private Skf2020Sc003GetParkingRirekiDataExpRepository skf2020Sc003GetParkingRirekiDataExpRepository;
	@Autowired
	private Skf2020Sc003GetApplHistoryInfoForUpdateExpRepository skf2020Sc003GetApplHistoryInfoForUpdateExpRepository;
	@Autowired
	private Skf2020Sc003UpdateApplHistoryExpRepository skf2020Sc003UpdateApplHistoryExpRepository;
	@Autowired
	private Skf2020Sc003GetBihinItemExpRepository skf2020Sc003GetBihinItemExpRepository;
	@Autowired
	private Skf2010TAttachedFileRepository skf2010TAttachedFileRepository;
	@Autowired
	private Skf2030TBihinKiboShinseiRepository skf2030TBihinKiboShinseiRepository;
	@Autowired
	private Skf2030TBihinRepository skf2030TBihinRepository;
	@Autowired
	private Skf2020TNyukyoChoshoTsuchiRepository skf2020TNyukyoChoshoTsuchiRepository;

	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfDropDownUtils skfDropDownUtils;
	@Autowired
	private SkfHtmlCreateUtils skfHtmlCreateUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private SkfCommentUtils skfCommentUtils;
	@Autowired
	private SkfAttachedFileUtils skfAttachedFileUtils;
	@Autowired
	private SkfShinseiUtils skfShinseiUtils;

	public void setMenuScopeSessionBean(MenuScopeSessionBean bean) {
		menuScopeSessionBean = bean;
	}

	/**
	 * セッションの添付資料情報の初期化
	 * 
	 * @param bean
	 */
	public void clearMenuScopeSessionBean() {
		if (menuScopeSessionBean == null) {
			return;
		}
		skfAttachedFileUtils.clearAttachedFileBySessionData(menuScopeSessionBean,
				SessionCacheKeyConstant.SHATAKU_ATTACHED_FILE_SESSION_KEY);
	}

	/**
	 * コメント欄の入力チェック
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public boolean checkValidation(Skf2020Sc003CommonDto dto) throws Exception {
		List<String> errorTarget = new ArrayList<String>();
		errorTarget.add("commentNote");

		// コメント欄入力チェック
		String commentNote = StringUtils.strip(dto.getCommentNote());

		if (NfwStringUtils.isEmpty(commentNote)) {
			ServiceHelper.addErrorResultMessage(dto, errorTarget.toArray(new String[errorTarget.size()]),
					MessageIdConstant.E_SKF_1048, "修正依頼/差戻し理由");

			return false;
		}
		int commentMaxLength = Integer.parseInt(PropertyUtils.getValue("skf2020.skf2020_sc003.commentMaxLength"));
		if (commentMaxLength < commentNote.getBytes("UTF-8").length) {
			ServiceHelper.addErrorResultMessage(dto, errorTarget.toArray(new String[errorTarget.size()]),
					MessageIdConstant.E_SKF_1049, "修正依頼/差戻し理由", "4000");

			return false;
		}

		return true;
	}

	/**
	 * 初期情報を設定します
	 * 
	 * @param dto
	 */
	public void setDispInfo(Skf2020Sc003CommonDto dto) {
		// 申請番号取得
		String applNo = dto.getApplNo();

		// 申請情報の設定（社宅入居希望等調書の表示）
		getSinseiInfo(applNo, dto);

		// 駐車場のみは返却備品表示不要
		String taiyoHitsuyo = dto.getTaiyoHituyo();
		boolean bihinVisible = true;
		if (CodeConstant.ASKED_SHATAKU_PARKING_ONLY.equals(taiyoHitsuyo)) {
			bihinVisible = false;
		} else {
			setBihinItemToBeReturn(applNo, dto);
		}

		// 提示情報の設定
		String shainNo = dto.getShainNo();
		dto.setMaskPattern(CodeConstant.NONE);
		if (!setTeijiDataInfo(shainNo, applNo, dto)) {
			// 備品申請要否の非活性制御
			// 備品非表示制御
			bihinVisible = false;
			dto.setMaskPattern("TeijiNG");
		}
		dto.setBihinVisible(bihinVisible);

		if (!NfwStringUtils.isEmpty(shainNo)) {
			setBihinInfo(applNo, dto);
		}

		dto.setBihinVisible(bihinVisible);

		setDispVisible(dto);

		return;
	}

	/**
	 * 申請書類履歴保存の処理
	 * 
	 * @param newStatus
	 * @param dto
	 * @param errorMsg
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean saveApplInfo(String newStatus, Skf2020Sc003CommonDto dto, Map<String, String> errorMsg) {
		String shainNo = dto.getShainNo();
		String applNo = dto.getApplNo();

		Date operationDate = new Date();

		// セッションの添付資料情報を取得
		List<Map<String, Object>> attachedFileList = (List<Map<String, Object>>) menuScopeSessionBean
				.get(SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY);

		Skf2020Sc003GetApplHistoryInfoForUpdateExp applInfo = new Skf2020Sc003GetApplHistoryInfoForUpdateExp();
		Skf2020Sc003GetApplHistoryInfoForUpdateExpParameter param = new Skf2020Sc003GetApplHistoryInfoForUpdateExpParameter();
		param.setCompanyCd(companyCd);
		param.setShainNo(shainNo);
		param.setApplNo(applNo);
		applInfo = skf2020Sc003GetApplHistoryInfoForUpdateExpRepository.getApplHistoryInfoForUpdate(param);
		if (applInfo == null) {
			errorMsg.put("error", MessageIdConstant.E_SKF_1075);
			return false;
		}

		Map<String, String> userInfo = new HashMap<String, String>();
		userInfo = skfLoginUserInfoUtils.getSkfLoginUserInfo();

		dto.setApplId(applInfo.getApplId());

		// 添付ファイルの有無
		int applTacFlg = 0;
		if (attachedFileList != null && attachedFileList.size() > 0) {
			applTacFlg = 1;
		}

		switch (newStatus) {
		case CodeConstant.STATUS_SASHIMODOSHI:
		case CodeConstant.STATUS_HININ:
			// 承認者設定処理
			String shonin1 = CodeConstant.NONE;
			String shonin2 = CodeConstant.NONE;
			switch (applInfo.getApplStatus()) {
			case CodeConstant.STATUS_SHINSACHU:
			case CodeConstant.STATUS_DOI_ZUMI:
				shonin1 = userInfo.get("userName");
				break;
			case CodeConstant.STATUS_SHONIN1:
				shonin2 = userInfo.get("userName");
				break;
			}

			boolean resultUpdateApplInfo = updateApplHistoryAgreeStatus(newStatus, shainNo, applNo, shonin1, shonin2,
					applInfo);
			if (!resultUpdateApplInfo) {
				errorMsg.put("error", MessageIdConstant.E_SKF_1075);
				return false;
			}

			break;
		}

		// 修正依頼、差戻し時はコメントテーブルを更新
		if (newStatus.equals(CodeConstant.STATUS_SASHIMODOSHI) || newStatus.equals(CodeConstant.STATUS_HININ)) {
			String commentName = CodeConstant.NONE;
			// 室、チームまたは課名を追記
			List<String> tmpNameList = new ArrayList<String>();
			if (userInfo.get("affiliation2Name") != null) {
				tmpNameList.add(userInfo.get("affiliation2Name"));
			}
			tmpNameList.add(StringUtils.trim(userInfo.get("userName")));
			commentName = String.join("\r\n", tmpNameList); // ログインユーザーの名前を取得
			String commentNote = StringUtils.trim(dto.getCommentNote());

			boolean commentErrorMessage = skfCommentUtils.insertComment(companyCd, applNo, newStatus, commentName,
					commentNote, errorMsg);
			if (!commentErrorMessage) {
				return false;
			}
		}

		// 添付ファイル管理テーブル更新処理
		boolean resultUpdateFile = updateAttachedFileInfo(newStatus, applNo, shainNo, attachedFileList, applTacFlg,
				applInfo, errorMsg);
		if (!resultUpdateFile) {
			return false;
		}

		int resultUpdateDisp = updateDispInfo(newStatus, operationDate, dto);
		if (resultUpdateDisp <= 0) {
			return false;
		}

		// TODO 社宅連携フラグ(0：社宅未連携、1：社宅連携)が「1」の場合に実行
		return true;
	}

	/**
	 * 添付資料の設定をします
	 * 
	 * @param dto
	 */
	@SuppressWarnings("unchecked")
	public void setAttachedFileList(Skf2020Sc003CommonDto dto) {
		// セッションの添付資料情報を取得
		List<Map<String, Object>> attachedFileList = (List<Map<String, Object>>) menuScopeSessionBean
				.get(SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY);
		dto.setAttachedFileList(attachedFileList);
		return;
	}

	/** 以下、Privateメソッド */

	/**
	 * 申請情報の設定（社宅入居希望等調書の表示）
	 * 
	 * @param applNo
	 * @param dto
	 */
	private void getSinseiInfo(String applNo, Skf2020Sc003CommonDto dto) {
		// 入居希望等調査・入居決定通知テーブルより社宅入居希望等調書情報を取得する
		Skf2020Sc003GetShatakuNyukyoKiboInfoExp shatakuNyukyoKiboInfo = new Skf2020Sc003GetShatakuNyukyoKiboInfoExp();
		shatakuNyukyoKiboInfo = getShatakuNyukyoKiboInfo(companyCd, applNo);

		if (shatakuNyukyoKiboInfo == null) {
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1077);
			// 更新処理を行わせないようボタンを使用不可に
			// 差戻しのみ使用可能に
			// 備品申請要否の非活性制御
			// 備品非表示制御
			return;
		}

		// 排他処理用最終更新日時保持
		dto.addLastUpdateDate("Skf2020TShatakuChoshoTsuchiUpdateDate", shatakuNyukyoKiboInfo.getLastUpdateDate());

		// 申請内容
		// 申請状況
		Map<String, String> applStatusMap = skfGenericCodeUtils.getGenericCode("SKF1001");
		dto.setApplStatusText(applStatusMap.get(dto.getApplStatus()));

		// 所属 機関
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getAgency())) {
			dto.setAgencyName(shatakuNyukyoKiboInfo.getAgency());
		}
		// 所属 部等
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getAffiliation1())) {
			dto.setAffiliation1Name(shatakuNyukyoKiboInfo.getAffiliation1());
		}
		// 所属 室、チーム又は課
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getAffiliation2())) {
			dto.setAffiliation2Name(shatakuNyukyoKiboInfo.getAffiliation2());
		}
		// 所属 勤務先のTEL
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getTel())) {
			dto.setTel(shatakuNyukyoKiboInfo.getTel());
		}
		// 申請者 社員番号
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getShainNo())) {
			dto.setShainNo(shatakuNyukyoKiboInfo.getShainNo());
		}
		// 申請者 氏名
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getName())) {
			dto.setName(shatakuNyukyoKiboInfo.getName());
		}
		// 申請者 等級
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getTokyu())) {
			dto.setTokyu(shatakuNyukyoKiboInfo.getTokyu());
		}
		// 社宅を必要としますか？
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getTaiyoHitsuyo())) {
			dto.setTaiyoHituyo(shatakuNyukyoKiboInfo.getTaiyoHitsuyo());
		}

		// 社宅を必要とする理由
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getHitsuyoRiyu())) {
			dto.setHitsuyoRiyu(shatakuNyukyoKiboInfo.getHitsuyoRiyu());
		}
		// 社宅を必要としない理由
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getFuhitsuyoRiyu())) {
			dto.setFuhitsuyoRiyu(shatakuNyukyoKiboInfo.getFuhitsuyoRiyu());
		}

		// 新所属 機関
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getNewAgency())) {
			dto.setNewAgency(shatakuNyukyoKiboInfo.getNewAgency());
		}
		// 新所属 部等
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getNewAffiliation1())) {
			dto.setNewAffiliation1(shatakuNyukyoKiboInfo.getNewAffiliation1());
		}
		// 新所属 室、チーム又は課
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getNewAffiliation2())) {
			dto.setNewAffiliation2(shatakuNyukyoKiboInfo.getNewAffiliation2());
		}
		// 必要とする社宅
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getHitsuyoShataku())) {
			dto.setHitsuyoShataku(shatakuNyukyoKiboInfo.getHitsuyoShataku());
		}
		// 同居家族 家族1
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoName1())) {
			dto.setDokyoName1(shatakuNyukyoKiboInfo.getDokyoName1());
		}
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoRelation1())) {
			dto.setDokyoRelation1(shatakuNyukyoKiboInfo.getDokyoRelation1());
		}
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoAge1())) {
			dto.setDokyoAge1(shatakuNyukyoKiboInfo.getDokyoAge1());
		}
		// 同居家族 家族2
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoName2())) {
			dto.setDokyoName2(shatakuNyukyoKiboInfo.getDokyoName2());
		}
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoRelation2())) {
			dto.setDokyoRelation2(shatakuNyukyoKiboInfo.getDokyoRelation2());
		}
		if (shatakuNyukyoKiboInfo.getDokyoAge2() != null
				&& !NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoAge2())) {
			dto.setDokyoAge2(shatakuNyukyoKiboInfo.getDokyoAge2());
		}
		// 同居家族 家族3
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoName3())) {
			dto.setDokyoName3(shatakuNyukyoKiboInfo.getDokyoName3());
		}
		if (shatakuNyukyoKiboInfo.getDokyoRelation3() != null
				&& !NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoRelation3())) {
			dto.setDokyoRelation3(shatakuNyukyoKiboInfo.getDokyoRelation3());
		}
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoAge3())) {
			dto.setDokyoAge3(shatakuNyukyoKiboInfo.getDokyoAge3());
		}
		// 同居家族 家族4
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoName4())) {
			dto.setDokyoName4(shatakuNyukyoKiboInfo.getDokyoName4());
		}
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoRelation4())) {
			dto.setDokyoRelation4(shatakuNyukyoKiboInfo.getDokyoRelation4());
		}
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoAge4())) {
			dto.setDokyoAge4(shatakuNyukyoKiboInfo.getDokyoAge4());
		}
		// 同居家族 家族5
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoName5())) {
			dto.setDokyoName5(shatakuNyukyoKiboInfo.getDokyoName5());
		}
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoRelation5())) {
			dto.setDokyoRelation5(shatakuNyukyoKiboInfo.getDokyoRelation5());
		}
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoAge5())) {
			dto.setDokyoAge5(shatakuNyukyoKiboInfo.getDokyoAge5());
		}
		// 同居家族 家族6
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoName6())) {
			dto.setDokyoName6(shatakuNyukyoKiboInfo.getDokyoName6());
		}
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoRelation6())) {
			dto.setDokyoRelation6(shatakuNyukyoKiboInfo.getDokyoRelation6());
		}
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoAge6())) {
			dto.setDokyoAge6(shatakuNyukyoKiboInfo.getDokyoAge6());
		}
		// 入居希望日
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getNyukyoYoteiDate())) {
			dto.setNyukyoYoteiDate(skfDateFormatUtils.dateFormatFromString(shatakuNyukyoKiboInfo.getNyukyoYoteiDate(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH));
		}
		// １台目
		// 自動者の保管場所
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getParkingUmu())) {
			dto.setParkingUmu(shatakuNyukyoKiboInfo.getParkingUmu());
		}
		// 自動車の登録番号の入力フラグ
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getCarNoInputFlg())) {
			dto.setCarNoInputFlg(shatakuNyukyoKiboInfo.getCarNoInputFlg());
		}
		// 自動車の車名
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getCarName())) {
			dto.setCarName(shatakuNyukyoKiboInfo.getCarName());
		}
		// 自動車の登録番号
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getCarNo())) {
			dto.setCarNo(shatakuNyukyoKiboInfo.getCarNo());
		}
		// 車検の有効期間満了日
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getCarExpirationDate())) {
			dto.setCarExpirationDate(skfDateFormatUtils.dateFormatFromString(
					shatakuNyukyoKiboInfo.getCarExpirationDate(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH));
		}
		// 自動車の使用者
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getCarUser())) {
			dto.setCarUser(shatakuNyukyoKiboInfo.getCarUser());
		}
		// 自動車の保管場所 使用開始日(予定日)
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getParkingUseDate())) {
			dto.setParkingUseDate(skfDateFormatUtils.dateFormatFromString(shatakuNyukyoKiboInfo.getParkingUseDate(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH));
		}
		// ２台目
		// 自動車の登録番号の入力フラグ
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getCarNoInputFlg2())) {
			dto.setCarNoInputFlg2(shatakuNyukyoKiboInfo.getCarNoInputFlg2());
		}
		// 自動車の車名
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getCarName2())) {
			dto.setCarName2(shatakuNyukyoKiboInfo.getCarName2());
		}
		// 自動車の登録番号
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getCarNo2())) {
			dto.setCarNo2(shatakuNyukyoKiboInfo.getCarNo2());
		}
		// 車検の有効期間満了日
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getCarExpirationDate2())) {
			dto.setCarExpirationDate2(skfDateFormatUtils.dateFormatFromString(
					shatakuNyukyoKiboInfo.getCarExpirationDate2(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH));
		}
		// 自動車の使用者
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getCarUser2())) {
			dto.setCarUser2(shatakuNyukyoKiboInfo.getCarUser2());
		}
		// 自動車の保管場所 使用開始日(予定日)
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getParkingUseDate2())) {
			dto.setParkingUseDate2(skfDateFormatUtils.dateFormatFromString(shatakuNyukyoKiboInfo.getParkingUseDate2(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH));
		}
		// 現居住宅
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getNowShataku())) {
			dto.setNowShataku(shatakuNyukyoKiboInfo.getNowShataku());
		}
		// 現居住宅 保有社宅名
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getNowShatakuName())) {
			dto.setNowShatakuName(shatakuNyukyoKiboInfo.getNowShatakuName());
		}
		// 現居住宅 室番号
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getNowShatakuNo())) {
			dto.setNowShatakuNo(shatakuNyukyoKiboInfo.getNowShatakuNo());
		}
		// 現居住宅 規格(間取り)
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getNowShatakuKikaku())) {
			dto.setNowShatakuKikaku(shatakuNyukyoKiboInfo.getNowShatakuKikaku());
		}
		// 現居住宅 面積
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getNowShatakuMenseki())) {
			dto.setNowShatakuMenseki(shatakuNyukyoKiboInfo.getNowShatakuMenseki() + SkfCommonConstant.SQUARE_MASTER);
		}
		// 駐車場 １台目 保管場所
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getParkingArea())) {
			dto.setParking1stPlace(shatakuNyukyoKiboInfo.getParkingArea());
		}
		// 駐車場 ２台目 保管場所
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getParkingArea2())) {
			dto.setParking2stPlace(shatakuNyukyoKiboInfo.getParkingArea2());
		}
		// 特殊事情など
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getTokushuJijo())) {
			dto.setTokushuJijo(shatakuNyukyoKiboInfo.getTokushuJijo());
		}
		// 現保有の社宅（退居予定）
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getTaikyoYotei())) {
			dto.setTaikyoYotei(shatakuNyukyoKiboInfo.getTaikyoYotei());
		}
		// 現保有の社宅 社宅管理番号
		dto.setNowShatakuKanriNo(String.valueOf(shatakuNyukyoKiboInfo.getNowShatakuKanriNo()));
		// 現保有の社宅 部屋管理番号
		dto.setNowShatakuRoomKanriNo(String.valueOf(shatakuNyukyoKiboInfo.getNowShatakuKanriNo()));
		// 退居予定日
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getTaikyoYoteiDate())) {
			dto.setTaikyoYoteiDate(skfDateFormatUtils.dateFormatFromString(shatakuNyukyoKiboInfo.getTaikyoYoteiDate(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH));
		}
		// 社宅の状態
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getShatakuJotai())) {
			dto.setShatakuJotai(shatakuNyukyoKiboInfo.getShatakuJotai());
		}

		// 退居理由
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getTaikyoRiyu())) {
			dto.setTaikyoRiyu(shatakuNyukyoKiboInfo.getTaikyoRiyu());
		}
		// 退居後の連絡先
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getTaikyogoRenrakusaki())) {
			dto.setTaikyogoRenrakuSaki(shatakuNyukyoKiboInfo.getTaikyogoRenrakusaki());
		}
		// 返却立会希望日
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getSessionDay())) {
			dto.setSessionDay(skfDateFormatUtils.dateFormatFromString(shatakuNyukyoKiboInfo.getSessionDay(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH));
		}
		// 連絡先
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getRenrakuSaki())) {
			dto.setRenrakuSaki(shatakuNyukyoKiboInfo.getRenrakuSaki());
		}
		// 更新日時
		if (shatakuNyukyoKiboInfo.getUpdateDate() != null) {
			dto.addLastUpdateDate("skf2020TNyukyoChoshoTsuchiUpdatedate", shatakuNyukyoKiboInfo.getUpdateDate());
		}
		// 備品希望申請の再掲分
		// 申請者 氏名（社宅希望申請の氏名で代用）

		// 申請者 性別
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getGender())) {
			String genderText = CodeConstant.NONE;
			if (CodeConstant.MALE.equals(shatakuNyukyoKiboInfo.getGender())) {
				genderText = SkfCommonConstant.GENDER_MAIL;
			} else if (CodeConstant.FEMALE.equals(shatakuNyukyoKiboInfo.getGender())) {
				genderText = SkfCommonConstant.GENDER_FEMAIL;
			}
			dto.setGender(genderText);
		}
		// 備品希望申請を希望する/しない ラジオボタン
		if (NfwStringUtils.isEmpty(dto.getBihinKibo())
				&& !NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getBihinKibo())) {
			dto.setBihinKibo(shatakuNyukyoKiboInfo.getBihinKibo());
		}

		return;
	}

	/**
	 * 提示データの設定を行います
	 * 
	 * @param shainNo
	 * @param applNo
	 * @param dto
	 */
	@SuppressWarnings("unchecked")
	private boolean setTeijiDataInfo(String shainNo, String applNo, Skf2020Sc003CommonDto dto) {
		List<Skf2020Sc003GetTeijiDataInfoExp> teijiDataInfoList = new ArrayList<Skf2020Sc003GetTeijiDataInfoExp>();
		String nyutaikyoKbn = CodeConstant.NYUTAIKYO_KBN_NYUKYO;
		teijiDataInfoList = getTeijiDataInfo(shainNo, nyutaikyoKbn, applNo);
		if (teijiDataInfoList == null || teijiDataInfoList.size() <= 0) {
			// 社宅提示データが取得できなかった場合
			ServiceHelper.addWarnResultMessage(dto, MessageIdConstant.W_SKF_1001, SHATAKU_TEIJI_MSG,
					SHATAKU_TEIJI_NONE);

			// TODO この時点では添付ファイルは取得されていないため不要？
			// 提示データが存在しない場合は添付資料を表示させない。（クリア処理を行う。）
			// dto.setShatakuAttachedFileList(null);
			return false;
		}

		Skf2020Sc003GetTeijiDataInfoExp teijiDataInfo = new Skf2020Sc003GetTeijiDataInfoExp();
		teijiDataInfo = teijiDataInfoList.get(0);

		String createCompKbn = teijiDataInfo.getCreateCompleteKbn();
		if (createCompKbn != null && !NfwStringUtils.isEmpty(createCompKbn)) {
			switch (createCompKbn) {
			case CodeConstant.MI_SAKUSEI:
				ServiceHelper.addWarnResultMessage(dto, MessageIdConstant.W_SKF_1001, SHATAKU_TEIJI_MSG,
						SHATAKU_TEIJI_COMP);
				// 未作成は提示不可で継続
				dto.setMaskPattern("TeijiNG");
				break;
			}
		}

		// 提示内容
		// 社宅情報及び駐車場 都道府県コード（保有社宅のみ設定される）
		String wkPrefName = CodeConstant.NONE;
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getPrefCd())) {
			wkPrefName = getShatakuPrefName(teijiDataInfo.getPrefCd());
		}
		// 社宅情報 社宅所在地
		String newShozaichi = CodeConstant.NONE;
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getAddress())) {
			newShozaichi = wkPrefName + teijiDataInfo.getAddress();
			dto.setNewShozaichi(newShozaichi);
		}
		// 社宅情報 社宅名
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getShatakuName())) {
			dto.setNewShatakuName(teijiDataInfo.getShatakuName());
		}
		// 社宅情報 室番号
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getRoomNo())) {
			dto.setNewShatakuNo(teijiDataInfo.getRoomNo());
		}
		// 社宅情報 規格(間取り)
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getKikaku())) {
			// 貸与規格
			String newShatakuKikaku = getShatakuKikakuKBN(teijiDataInfo.getKikaku());
			dto.setNewShatakuKikaku(newShatakuKikaku);
		} else {
			// 本来規格
			if (!NfwStringUtils.isEmpty(teijiDataInfo.getOriginalKikaku())) {
				String newShatakuKikaku = getShatakuKikakuKBN(teijiDataInfo.getOriginalKikaku());
				dto.setNewShatakuKikaku(newShatakuKikaku);
			}
		}
		// 社宅情報 面積
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getLendMenseki())) {
			dto.setNewShatakuMenseki(teijiDataInfo.getLendMenseki() + SkfCommonConstant.SQUARE_MASTER);
		}
		// 社宅情報 使用料(月)
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getRentalAdjust())) {
			Long rentalAdjust = Long.parseLong(teijiDataInfo.getRentalAdjust());
			dto.setNewRental(nfNum.format(rentalAdjust) + SkfCommonConstant.FORMAT_EN);
		}
		// 個人負担金共益費協議中フラグ
		String kyoekihiKyogichuFlg = CodeConstant.NONE;
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getKyoekihiPersonKyogichuFlg())) {
			kyoekihiKyogichuFlg = teijiDataInfo.getKyoekihiPersonKyogichuFlg();
		}
		// 社宅情報 共益費
		// 個人負担共益費協議中フラグが1だったら「協議中」を表示する
		if (kyoekihiKyogichuFlg.equals(SkfCommonConstant.KYOGICHU)) {
			dto.setNewKyoekihi("協議中");
		} else {
			if (!NfwStringUtils.isEmpty(teijiDataInfo.getKyoekihiPersonAdjust())) {
				Long kyoekihiAdjust = Long.parseLong(teijiDataInfo.getKyoekihiPersonAdjust());
				dto.setNewKyoekihi(nfNum.format(kyoekihiAdjust) + SkfCommonConstant.FORMAT_EN);
			}

		}
		// 社宅情報 入居可能日
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getNyukyoYoteiDate())) {
			dto.setNyukyoYoteiDate(skfDateFormatUtils.dateFormatFromString(teijiDataInfo.getNyukyoYoteiDate(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH));
		}
		// 社宅情報 寮長・自治会長 部屋名称
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getManegeShatakuNo1())) {
			dto.setManegeShatakuNo1(teijiDataInfo.getManegeShatakuNo1());
		}
		// 社宅情報 寮長・自治会長 氏名
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getManegeName1())) {
			dto.setManegeName1(teijiDataInfo.getManegeName1());
		}
		// 社宅情報 寮長・自治会長 電子メールアドレス
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getManegeMailAddress1())) {
			dto.setManegeMailAddress1(teijiDataInfo.getManegeMailAddress1());
		}
		// 社宅情報 寮長・自治会長 電話番号
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getManegeTelNo1())) {
			dto.setManegeTelNo1(teijiDataInfo.getManegeTelNo1());
		}
		// 社宅情報 寮長・自治会長 内線番号
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getManegeExtensionNo1())) {
			dto.setManegeExtensionNo1(teijiDataInfo.getManegeExtensionNo1());
		}
		// 社宅情報 鍵管理者 部屋名称
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getManegeShatakuNo2())) {
			dto.setManegeShatakuNo2(teijiDataInfo.getManegeShatakuNo2());
		}
		// 社宅情報 鍵管理者 氏名
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getManegeName2())) {
			dto.setManegeName2(teijiDataInfo.getManegeName2());
		}
		// 社宅情報 鍵管理者 電子メールアドレス
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getManegeMailAddress2())) {
			dto.setManegeMailAddress2(teijiDataInfo.getManegeMailAddress2());
		}
		// 社宅情報 鍵管理者 電話番号
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getManegeTelNo2())) {
			dto.setManegeTelNo2(teijiDataInfo.getManegeTelNo2());
		}
		// 社宅情報 鍵管理者 内線番号
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getManegeExtensionNo2())) {
			dto.setManegeExtensionNo2(teijiDataInfo.getManegeExtensionNo2());
		}

		// 社宅情報 寮母・管理会社 部屋名称
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getManegeShatakuNo3())) {
			dto.setManegeShatakuNo3(teijiDataInfo.getManegeShatakuNo3());
		}
		// 社宅情報 寮母・管理会社 氏名
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getManegeName3())) {
			dto.setManegeName3(teijiDataInfo.getManegeName3());
		}
		// 社宅情報 寮母・管理会社 電子メールアドレス
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getManegeMailAddress3())) {
			dto.setManegeMailAddress3(teijiDataInfo.getManegeMailAddress3());
		}
		// 社宅情報 寮母・管理会社 電話番号
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getManegeTelNo3())) {
			dto.setManegeTelNo3(teijiDataInfo.getManegeTelNo3());
		}
		// 社宅情報 寮母・管理会社 内線番号
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getManegeExtensionNo3())) {
			dto.setManegeExtensionNo3(teijiDataInfo.getManegeExtensionNo3());
		}
		// 社宅情報及び駐車場 都道府県コード（保有社宅のみ設定される）
		String wkPrefNameParking = CodeConstant.NONE;
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getPrefCdParking())) {
			wkPrefNameParking = getShatakuPrefName(teijiDataInfo.getPrefCdParking());
		}
		// 駐車場情報 １台目 自動車の保管場所
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getParkingAddress1())) {
			dto.setParkingAddress1(wkPrefNameParking + teijiDataInfo.getParkingAddress1());
		}
		// 駐車場情報 １台目 位置番号等
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getParkingBlock1())) {
			dto.setCarIchiNo1(teijiDataInfo.getParkingBlock1());
		}
		// 駐車場情報 １台目 自動車の保管場所に係わる使用料(月)
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getParkingRental1())) {
			Long parkingRental1 = Long.parseLong(teijiDataInfo.getParkingRental1());
			dto.setParkingRental1(nfNum.format(parkingRental1) + SkfCommonConstant.FORMAT_EN);
		}
		// 駐車場情報 １台目 使用開始可能日
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getParking1StartDate())) {
			dto.setParking1StartDate(skfDateFormatUtils.dateFormatFromString(teijiDataInfo.getParking1StartDate(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH));
		}

		// 駐車場情報 ２台目存在チェック
		boolean bCar2 = false;

		// 駐車場情報 ２台目 自動車の保管場所
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getParkingAddress2())) {
			dto.setParkingAddress2(wkPrefNameParking + teijiDataInfo.getParkingAddress2());
			bCar2 = true;
		}
		// 駐車場情報 ２台目 位置番号等
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getParkingBlock2())) {
			dto.setCarIchiNo2(teijiDataInfo.getParkingBlock2());
			bCar2 = true;
		}
		// 駐車場情報 ２台目 自動車の保管場所に係わる使用料(月)
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getParkingRental2())) {
			Long parkingRental2 = Long.parseLong(teijiDataInfo.getParkingRental2());
			dto.setParkingRental2(nfNum.format(parkingRental2) + SkfCommonConstant.FORMAT_EN);
			bCar2 = true;
		}
		// 駐車場情報 ２台目 使用開始可能日
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getParking2StartDate())) {
			dto.setParking2StartDate(skfDateFormatUtils.dateFormatFromString(teijiDataInfo.getParking2StartDate(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH));
			bCar2 = true;
		}

		// 社宅管理番号
		if (teijiDataInfo.getShatakuKanriNo() != CodeConstant.LONG_ZERO) {
			dto.setNewShatakuKanriNo(String.valueOf(teijiDataInfo.getShatakuKanriNo()));
		}
		// 部屋管理番号
		if (teijiDataInfo.getShatakuRoomKanriNo() != CodeConstant.LONG_ZERO) {
			dto.setNewShatakuRoomKanriNo(String.valueOf(teijiDataInfo.getShatakuRoomKanriNo()));
		}
		// 備品希望申請の必要社宅は申請情報のものを使用
		dto.setBihinHitsuyoShataku(dto.getHitsuyoShataku());

		// 駐車場情報 駐車場のみ割当先判定
		// 社宅貸与必要＝2:駐車場のみ
		if (CodeConstant.ASKED_SHATAKU_PARKING_ONLY.equals(dto.getTaiyoHituyo()) && bCar2) {
			// ２台目 自動車の車名、登録番号、使用者、保管場所使用開始日(予定日)が空白なら１台のみ申請
			if (NfwStringUtils.isEmpty(dto.getCarName2()) && NfwStringUtils.isEmpty(dto.getCarNo2())
					&& NfwStringUtils.isEmpty(dto.getCarUser2())
					&& NfwStringUtils.isEmpty(dto.getParking2StartDate())) {
				// 駐車場のみで1台のみ追加された場合は割当先を判定
				if (teijiDataInfo.getShatakuKanriNo() != CodeConstant.LONG_ZERO
						&& teijiDataInfo.getShatakuRoomKanriNo() != CodeConstant.LONG_ZERO) {
					long shatakuKanriNo = teijiDataInfo.getShatakuKanriNo();
					long shatakuRoomKanriNo = teijiDataInfo.getShatakuRoomKanriNo();

					// 月次処理管理データの対象年月を取得
					String syoriYYMM = getMaxCycleBillingYYMM();

					// 駐車場履歴テーブルから現在割当の駐車場を取得
					List<Skf2020Sc003GetParkingRirekiDataExp> parkingRirekiList = new ArrayList<Skf2020Sc003GetParkingRirekiDataExp>();
					parkingRirekiList = getParkingRirekiData(shatakuKanriNo, shatakuRoomKanriNo, shainNo, syoriYYMM);
					// 駐車場履歴データが取得できた場合
					if (parkingRirekiList != null && parkingRirekiList.size() > 0) {
						Skf2020Sc003GetParkingRirekiDataExp parkingRirekiData = parkingRirekiList.get(0);
						// 今回提示か判定、駐車場2が割当なら移動
						if (parkingRirekiData.getParkingKanriNo1() == CodeConstant.LONG_ZERO
								&& parkingRirekiData.getParkingKanriNo2() != CodeConstant.LONG_ZERO) {
							dto.setParking1stPlace(dto.getParking2stPlace());
							dto.setCarIchiNo1(dto.getCarIchiNo2());
							dto.setParkingRental1(dto.getParkingRental2());
							dto.setParking1StartDate(dto.getParking2StartDate());
							dto.setParking2stPlace(CodeConstant.NONE);
							dto.setCarIchiNo2(CodeConstant.NONE);
							dto.setParkingRental2(CodeConstant.NONE);
							dto.setParking2StartDate(CodeConstant.NONE);
						}
					}
				}
			}
		}

		// 既存の添付資料をクリアする
		List<Map<String, Object>> shatakuAttachedFileList = new ArrayList<Map<String, Object>>();

		// 社宅補足ファイル1
		if (teijiDataInfo.getShatakuSupplementFile1() != null
				&& StringUtils.isNotEmpty(teijiDataInfo.getShatakuSupplementName1())
				&& StringUtils.isNotEmpty(teijiDataInfo.getShatakuSupplementSize1())) {
			addShatakuAttachedFile(teijiDataInfo.getShatakuSupplementName1(), teijiDataInfo.getShatakuSupplementFile1(),
					teijiDataInfo.getShatakuSupplementSize1(), shatakuAttachedFileList.size(), shatakuAttachedFileList);
		}
		// 社宅補足ファイル2
		if (teijiDataInfo.getShatakuSupplementFile2() != null
				&& StringUtils.isNotEmpty(teijiDataInfo.getShatakuSupplementName2())
				&& StringUtils.isNotEmpty(teijiDataInfo.getShatakuSupplementSize2())) {
			addShatakuAttachedFile(teijiDataInfo.getShatakuSupplementName2(), teijiDataInfo.getShatakuSupplementFile2(),
					teijiDataInfo.getShatakuSupplementSize2(), shatakuAttachedFileList.size(), shatakuAttachedFileList);
		}
		// 社宅補足ファイル3
		if (teijiDataInfo.getShatakuSupplementFile3() != null
				&& StringUtils.isNotEmpty(teijiDataInfo.getShatakuSupplementName3())
				&& StringUtils.isNotEmpty(teijiDataInfo.getShatakuSupplementSize3())) {
			addShatakuAttachedFile(teijiDataInfo.getShatakuSupplementName3(), teijiDataInfo.getShatakuSupplementFile3(),
					teijiDataInfo.getShatakuSupplementSize3(), shatakuAttachedFileList.size(), shatakuAttachedFileList);
		}
		// 駐車場補足ファイル1
		if (teijiDataInfo.getParkingSupplementFile1() != null
				&& StringUtils.isNotEmpty(teijiDataInfo.getParkingSupplementName1())
				&& StringUtils.isNotEmpty(teijiDataInfo.getParkingSupplementSize1())) {
			addShatakuAttachedFile(teijiDataInfo.getParkingSupplementName1(), teijiDataInfo.getParkingSupplementFile1(),
					teijiDataInfo.getParkingSupplementSize1(), shatakuAttachedFileList.size(), shatakuAttachedFileList);
		}
		// 駐車場補足ファイル2
		if (teijiDataInfo.getParkingSupplementFile2() != null
				&& StringUtils.isNotEmpty(teijiDataInfo.getParkingSupplementName2())
				&& StringUtils.isNotEmpty(teijiDataInfo.getParkingSupplementSize2())) {
			addShatakuAttachedFile(teijiDataInfo.getParkingSupplementName2(), teijiDataInfo.getParkingSupplementFile2(),
					teijiDataInfo.getParkingSupplementSize2(), shatakuAttachedFileList.size(), shatakuAttachedFileList);
		}
		// 駐車場補足ファイル3
		if (teijiDataInfo.getParkingSupplementFile3() != null
				&& StringUtils.isNotEmpty(teijiDataInfo.getParkingSupplementName3())
				&& StringUtils.isNotEmpty(teijiDataInfo.getParkingSupplementSize3())) {
			addShatakuAttachedFile(teijiDataInfo.getParkingSupplementName3(), teijiDataInfo.getParkingSupplementFile3(),
					teijiDataInfo.getParkingSupplementSize3(), shatakuAttachedFileList.size(), shatakuAttachedFileList);
		}
		// 資料補足ファイル
		if (teijiDataInfo.getShiryoHosokuFile() != null && StringUtils.isNotEmpty(teijiDataInfo.getShiryoHosokuName())
				&& StringUtils.isNotEmpty(teijiDataInfo.getShiryoHosokuSize())) {
			addShatakuAttachedFile(teijiDataInfo.getShiryoHosokuName(), teijiDataInfo.getShiryoHosokuFile(),
					teijiDataInfo.getShiryoHosokuSize(), shatakuAttachedFileList.size(), shatakuAttachedFileList);
		}
		menuScopeSessionBean.put(SessionCacheKeyConstant.SHATAKU_ATTACHED_FILE_SESSION_KEY, shatakuAttachedFileList);

		// 添付ファイル情報設定
		List<Map<String, Object>> tmpAttachedFileList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> attachedFileList = new ArrayList<Map<String, Object>>();
		tmpAttachedFileList = skfAttachedFileUtils.getAttachedFileInfo(menuScopeSessionBean, applNo,
				SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY);
		if (tmpAttachedFileList != null && tmpAttachedFileList.size() > 0) {
			int defaultAttachedNo = 0;
			if (shatakuAttachedFileList != null && shatakuAttachedFileList.size() > 0) {
				defaultAttachedNo = Integer.parseInt(
						shatakuAttachedFileList.get(shatakuAttachedFileList.size() - 1).get("attachedNo").toString())
						+ 1;
			}
			for (Map<String, Object> tmpAttachedFileMap : tmpAttachedFileList) {
				addShatakuAttachedFile(tmpAttachedFileMap.get("attachedName").toString(),
						(byte[]) tmpAttachedFileMap.get("fileStream"), tmpAttachedFileMap.get("fileSize").toString(),
						defaultAttachedNo, attachedFileList);
			}
		}
		// 添付ファイル情報設定（社宅添付ファイルと通常添付ファイルを別に保持）
		dto.setShatakuAttachedFileList(shatakuAttachedFileList);
		dto.setAttachedFileList(attachedFileList);
		return true;
	}

	/**
	 * 申請情報、備品情報の更新を行います
	 * 
	 * @param newStatus
	 * @param applDate
	 * @param dto
	 * @return
	 */
	private int updateDispInfo(String newStatus, Date applDate, Skf2020Sc003CommonDto dto) {
		int returnValue = -1;

		String applNo = dto.getApplNo();

		// 備品申請情報と提示データ情報を取得する
		getSinseiInfo(applNo, dto);
		setTeijiDataInfo(dto.getShainNo(), applNo, dto);

		Long shatakuKanriNo = CodeConstant.LONG_ZERO;
		if (NfwStringUtils.isNotEmpty(dto.getNewShatakuKanriNo())) {
			shatakuKanriNo = Long.parseLong(dto.getNewShatakuKanriNo());
		}
		if (shatakuKanriNo == CodeConstant.LONG_ZERO) {
			// 提示データが存在しない場合または、差戻し、修正依頼時には一時保存不要
			// 戻り値が-1だとエラーメッセージが表示されてしまうため０を戻り値とする。
			return 0;
		}

		// 備品希望申請テーブルから備品申請管理番号
		String bihinShinseiApplNo = CodeConstant.NONE;
		List<Skf2020Sc003GetBihinShinseiInfoExp> bihinKiboShinsei = new ArrayList<Skf2020Sc003GetBihinShinseiInfoExp>();
		Skf2020Sc003GetBihinShinseiInfoExpParameter bihinShinseiParam = new Skf2020Sc003GetBihinShinseiInfoExpParameter();
		bihinShinseiParam.setCompanyCd(companyCd);
		bihinShinseiParam.setApplNo(applNo);
		bihinKiboShinsei = skf2020Sc003GetBihinShinseiInfoExpRepository.getBihinShinseiInfo(bihinShinseiParam);
		if (bihinKiboShinsei != null && bihinKiboShinsei.size() > 0) {
			bihinShinseiApplNo = bihinKiboShinsei.get(0).getApplNo();
		}

		// なければ備品希望申請の書類管理番号を新規発行
		if (NfwStringUtils.isEmpty(bihinShinseiApplNo)) {
			// 備品希望申請用の申請書類管理番号を取得
			bihinShinseiApplNo = skfShinseiUtils.getApplNo(companyCd, dto.getShainNo(), dto.getApplId());

			Skf2030TBihinKiboShinsei record = getColumnInfoListForBihinKiboShinsei(bihinShinseiApplNo, applDate, applNo,
					dto);
			returnValue = skf2030TBihinKiboShinseiRepository.insertSelective(record);
		} else {
			Skf2030TBihinKiboShinsei record = getColumnInfoListForBihinKiboShinsei(bihinShinseiApplNo, applDate, applNo,
					dto);
			returnValue = skf2030TBihinKiboShinseiRepository.updateByPrimaryKeySelective(record);

		}
		if (returnValue <= 0) {
			return returnValue;
		}

		// 申請区分
		String bihinKbn = CodeConstant.BIHIN_SHINSEI_KBN_HAIHUN;

		// 備品申請データの件数取得

		long bihinShinseiCount = getBihinShinseiInfoCount(companyCd, applNo);

		Map<String, String> bihinStateMapReverse = skfGenericCodeUtils.getGenericCodeReverse("SKF1051");

		List<Skf2020Sc003GetBihinItemExp> bihinItemList = new ArrayList<Skf2020Sc003GetBihinItemExp>();
		bihinItemList = skf2020Sc003GetBihinItemExpRepository.getBihinItem();

		if (bihinItemList != null) {
			for (Skf2020Sc003GetBihinItemExp bihinItemInfo : bihinItemList) {
				Skf2030TBihin record = new Skf2030TBihin();

				String bihinHope = CodeConstant.NONE;
				String bihinStateText = CodeConstant.NONE;
				String bihinState = CodeConstant.BIHIN_STATE_NONE;
				String bihinCd = bihinItemInfo.getBihinCd();

				switch (bihinCd) {
				case CodeConstant.BIHIN_WASHER:
					// 11：洗濯機
					bihinHope = dto.getBihinWish11();
					bihinStateText = dto.getBihinState11();
					break;
				case CodeConstant.BIHIN_FREEZER:
					// 12：冷蔵庫
					bihinHope = dto.getBihinWish12();
					bihinStateText = dto.getBihinState12();
					break;
				case CodeConstant.BIHIN_OVEN:
					// 13：オーブンレンジ
					bihinHope = dto.getBihinWish13();
					bihinStateText = dto.getBihinState13();
					break;
				case CodeConstant.BIHIN_CLENER:
					// 14：掃除機
					bihinHope = dto.getBihinWish14();
					bihinStateText = dto.getBihinState14();
					break;
				case CodeConstant.BIHIN_RICE_COOKER:
					// 15:炊飯ジャー
					bihinHope = dto.getBihinWish15();
					bihinStateText = dto.getBihinState15();
					break;
				case CodeConstant.BIHIN_TV:
					// 16：テレビ
					bihinHope = dto.getBihinWish16();
					bihinStateText = dto.getBihinState16();
					break;
				case CodeConstant.BIHIN_TV_STANDS:
					// 17：テレビ台
					bihinHope = dto.getBihinWish17();
					bihinStateText = dto.getBihinState17();
					break;
				case CodeConstant.BIHIN_KOTATSU:
					// 18：こたつ
					bihinHope = dto.getBihinWish18();
					bihinStateText = dto.getBihinState18();
					break;
				case CodeConstant.BIHIN_KICHEN_CABINET:
					// 19：キッチンキャビネット
					bihinHope = dto.getBihinWish19();
					bihinStateText = dto.getBihinState19();
					break;
				}

				bihinState = bihinStateMapReverse.get(bihinStateText);

				// 備品名
				String bihinName = bihinItemInfo.getBihinName();

				// プライマリキー設定
				record.setCompanyCd(companyCd);
				record.setApplNo(applNo);
				record.setBihinCd(bihinCd);

				if (bihinShinseiCount > 0) {
					// 備品申請テーブルの更新
					getColumnInfoListForBihin(true, bihinShinseiApplNo, bihinKbn, bihinCd, bihinName, bihinState,
							bihinHope, record);
					returnValue = skf2030TBihinRepository.updateByPrimaryKeySelective(record);

				} else {
					// 備品申請テーブルの新規追加
					getColumnInfoListForBihin(false, bihinShinseiApplNo, bihinKbn, bihinCd, bihinName, bihinState,
							bihinHope, record);
					returnValue = skf2030TBihinRepository.insertSelective(record);
				}

			}
			// 備品希望申請可否
			String bihinKiboKbn = dto.getBihinKibo();

			if (NfwStringUtils.isNotEmpty(bihinKiboKbn)) {
				// 備品希望申請可否を入居希望調書テーブルに書き込む
				Skf2020TNyukyoChoshoTsuchi nyukyoChoshoTsuchiInfo = new Skf2020TNyukyoChoshoTsuchi();
				nyukyoChoshoTsuchiInfo.setCompanyCd(companyCd);
				nyukyoChoshoTsuchiInfo.setApplNo(applNo);
				nyukyoChoshoTsuchiInfo.setBihinKibo(bihinKiboKbn);
				returnValue = skf2020TNyukyoChoshoTsuchiRepository.updateByPrimaryKeySelective(nyukyoChoshoTsuchiInfo);

			}

		}
		return returnValue;
	}

	/**
	 * 備品情報テーブルへのマッピングを行います
	 * 
	 * @param applNo
	 * @param bihinKbn
	 * @param bihinCd
	 * @param bihinName
	 * @param bihinState
	 * @param bihinHope
	 * @param bihinInfo
	 */
	private void getColumnInfoListForBihin(boolean updateFlg, String applNo, String bihinKbn, String bihinCd,
			String bihinName, String bihinState, String bihinHope, Skf2030TBihin bihinInfo) {
		// 会社コード
		bihinInfo.setCompanyCd(companyCd);
		// 申請書類番号
		bihinInfo.setApplNo(applNo);
		// 備品コード
		bihinInfo.setBihinCd(bihinCd);
		// 備品名
		bihinInfo.setBihinName(bihinName);
		// 申請区分
		bihinInfo.setBihinAppl(bihinKbn);
		// 状態区分
		if (NfwStringUtils.isEmpty(bihinState)) {
			// 値が無ければ「１：なし」に設定
			bihinState = CodeConstant.BIHIN_STATE_NONE;
		}
		bihinInfo.setBihinState(bihinState);
		// 希望可否区分
		bihinInfo.setBihinHope(bihinHope);

		return;
	}

	private Skf2030TBihinKiboShinsei getColumnInfoListForBihinKiboShinsei(String applNo, Date applDate,
			String nyuKyoApplNo, Skf2020Sc003CommonDto dto) {

		Skf2030TBihinKiboShinsei columnInfoList = new Skf2030TBihinKiboShinsei();

		// 更新SQLでは不要
		if (NfwStringUtils.isNotEmpty(applNo)) {
			// 会社コード
			columnInfoList.setCompanyCd(companyCd);

			// 申請書類番号
			columnInfoList.setApplNo(applNo);

		}

		// 入居希望等調書(nyukyoApplNo)
		columnInfoList.setNyukyoApplNo(nyuKyoApplNo);

		// 申請日
		columnInfoList.setApplDate(
				skfDateFormatUtils.dateFormatFromDate(applDate, SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT));

		// 所属 機関
		columnInfoList.setAgency(dto.getAgencyName());

		// 所属 部等
		columnInfoList.setAffiliation1(dto.getAffiliation1Name());

		// 所属 室、チーム又は課
		columnInfoList.setAffiliation2(dto.getAffiliation2Name());

		// 勤務先TEL
		columnInfoList.setTel(dto.getTel());

		// 社員番号
		columnInfoList.setShainNo(dto.getShainNo());

		// 氏名
		columnInfoList.setName(dto.getName());

		// 等級
		columnInfoList.setTokyu(dto.getTokyu());

		// 社宅情報 社宅名
		columnInfoList.setNowShatakuName(dto.getNewShatakuName());

		// 社宅情報 室番号
		columnInfoList.setNowShatakuNo(dto.getNewShatakuNo());

		// 社宅情報 規格(間取り)
		columnInfoList.setNowShatakuKikaku(dto.getNewShatakuKikaku());

		// 社宅情報 面積
		columnInfoList.setNowShatakuMenseki(dto.getNewShatakuMenseki());

		// 部屋管理番号
		columnInfoList.setShatakuNo(Long.parseLong(dto.getNewShatakuKanriNo()));

		// 部屋管理番号
		columnInfoList.setRoomKanriNo(Long.parseLong(dto.getNewShatakuRoomKanriNo()));

		return columnInfoList;
	}

	/**
	 * 申請履歴の承認者と申請状況を更新します
	 * 
	 * @param shainNo
	 * @param applNo
	 * @param shonin1
	 * @param shonin2
	 * @param applInfo
	 * @return
	 */
	private boolean updateApplHistoryAgreeStatus(String newStatus, String shainNo, String applNo, String shonin1,
			String shonin2, Skf2020Sc003GetApplHistoryInfoForUpdateExp applInfo) {
		Skf2020Sc003UpdateApplHistoryExp record = new Skf2020Sc003UpdateApplHistoryExp();
		if (NfwStringUtils.isNotEmpty(shonin1)) {
			record.setAgreName1(shonin1);
		}
		if (NfwStringUtils.isNotEmpty(shonin2)) {
			record.setAgreName1(shonin2);
		}
		record.setAgreDate(new Date());
		record.setApplStatus(newStatus);

		// 条件
		record.setCompanyCd(companyCd);
		record.setApplNo(applNo);
		record.setShainNo(shainNo);
		record.setApplId(applInfo.getApplId());

		int result = skf2020Sc003UpdateApplHistoryExpRepository.updateApplHistory(record);
		if (result <= 0) {
			return false;
		}
		return true;
	}

	/**
	 * 添付資料情報を更新する。
	 * 
	 * @param newStatus
	 * @param applNo
	 * @param shainNo
	 * @param attachedFileList
	 * @param applTacFlg
	 * @param updDto
	 */
	private boolean updateAttachedFileInfo(String newStatus, String applNo, String shainNo,
			List<Map<String, Object>> attachedFileList, int applTacFlg,
			Skf2020Sc003GetApplHistoryInfoForUpdateExp applInfo, Map<String, String> errorMsg) {
		// 添付ファイル管理テーブルを更新する
		if (attachedFileList != null && attachedFileList.size() > 0) {
			// 添付ファイルの更新は削除→登録で行う
			skfAttachedFileUtils.deleteAttachedFile(applNo, shainNo, errorMsg);
			for (Map<String, Object> attachedFileMap : attachedFileList) {
				Skf2010TAttachedFile insertData = new Skf2010TAttachedFile();
				insertData = mappingTAttachedFile(attachedFileMap, applNo, shainNo);
				skf2010TAttachedFileRepository.insertSelective(insertData);
			}
		}
		// 申請書類履歴テーブルの添付書類有無を更新
		String applId = applInfo.getApplId();
		Skf2020Sc003UpdateApplHistoryExp updateData = new Skf2020Sc003UpdateApplHistoryExp();
		updateData.setApplTacFlg(String.valueOf(applTacFlg));
		// 条件項目
		updateData.setCompanyCd(companyCd);
		updateData.setApplNo(applNo);
		updateData.setApplId(applId);
		updateData.setShainNo(shainNo);
		int result = skf2020Sc003UpdateApplHistoryExpRepository.updateApplHistory(updateData);
		if (result <= 0) {
			errorMsg.put("error", MessageIdConstant.E_SKF_1075);
			return false;
		}
		return true;
	}

	private Skf2010TAttachedFile mappingTAttachedFile(Map<String, Object> attachedFileMap, String applNo,
			String shainNo) {
		Skf2010TAttachedFile resultData = new Skf2010TAttachedFile();

		// 会社コード
		resultData.setCompanyCd(companyCd);
		// 社員番号
		resultData.setShainNo(shainNo);
		// 登録日時
		Date nowDate = new Date();
		resultData.setApplDate(nowDate);
		// 申請番号
		resultData.setApplNo(applNo);
		// 添付番号
		resultData.setAttachedNo(attachedFileMap.get("attachedNo").toString());
		// 添付資料名
		resultData.setAttachedName(attachedFileMap.get("attachedName").toString());
		// ファイル
		resultData.setFileStream((byte[]) attachedFileMap.get("fileStream"));
		// ファイルサイズ
		resultData.setFileSize(attachedFileMap.get("fileSize").toString());

		return resultData;
	}

	/**
	 * 備品情報の設定
	 * 
	 * @param applNo
	 * @param dto
	 */
	private void setBihinInfo(String applNo, Skf2020Sc003CommonDto dto) {
		boolean isShonin = false;

		// 遷移元パスがS0010（承認一覧）の場合、社宅部屋マスタの備品備付状態から取得するフラグを立てる。
		if (dto.getPrePageId() != null && dto.getPrePageId().equals(FunctionIdConstant.SKF2010_SC005)) {
			isShonin = true;
		}

		// 備品申請情報を取得
		List<Skf2020Sc003GetBihinShinseiInfoExp> bihinShinseiInfoList = new ArrayList<Skf2020Sc003GetBihinShinseiInfoExp>();
		bihinShinseiInfoList = getBihinShinseiInfo(companyCd, applNo);

		Map<String, String> bihinInfoMap = skfGenericCodeUtils.getGenericCode("SKF1051");
		List<Map<String, Object>> bihinShinseiList = new ArrayList<Map<String, Object>>();
		if (bihinShinseiInfoList != null && bihinShinseiInfoList.size() > 0) {
			for (Skf2020Sc003GetBihinShinseiInfoExp bihinShinseiInfo : bihinShinseiInfoList) {
				String bihinStateText = bihinInfoMap.get(bihinShinseiInfo.getBihinState());
				Map<String, Object> bihinMap = setBihinData(dto, bihinShinseiInfo.getBihinCd(),
						bihinShinseiInfo.getBihinName(), bihinStateText, bihinShinseiInfo.getBihinHope());
				bihinShinseiList.add(bihinMap);
			}
		}
		dto.setBihinShinseiList(bihinShinseiList);

		if (bihinShinseiInfoList == null || bihinShinseiInfoList.size() <= 0 || isShonin) {
			// 初期化
			bihinShinseiList = new ArrayList<Map<String, Object>>();

			Long shatakuKanriNo = CodeConstant.LONG_ZERO;
			if (dto.getNewShatakuKanriNo() != null) {
				shatakuKanriNo = Long.parseLong(dto.getNewShatakuKanriNo());
			}
			Long shatakuRoomKanriNo = CodeConstant.LONG_ZERO;
			if (dto.getNewShatakuRoomKanriNo() != null) {
				shatakuRoomKanriNo = Long.parseLong(dto.getNewShatakuRoomKanriNo());
			}

			List<Skf2020Sc003GetBihinInfoOutExp> bihinInfoList = getBihinInfoOut(shatakuKanriNo, shatakuRoomKanriNo,
					applNo, CodeConstant.NYUTAIKYO_KBN_NYUKYO);
			if (bihinInfoList != null && bihinInfoList.size() > 0) {

				for (Skf2020Sc003GetBihinInfoOutExp bihinInfo : bihinInfoList) {
					String bihinWish = bihinInfo.getBihinHope();
					String bihinStateTxt = bihinInfoMap.get(bihinInfo.getBihinState());
					// 備品状態が会社保有またはレンタルの場合にドロップボックスの初期値を選択不可にするための処理
					if (bihinInfo.getBihinState().equals(CodeConstant.BIHIN_STATE_HOYU)
							|| bihinInfo.getBihinState().equals(CodeConstant.BIHIN_STATE_RENTAL)) {
						bihinWish = "1";
					}
					Map<String, Object> bihinMap = setBihinData(dto, bihinInfo.getBihinCd(), bihinInfo.getBihinName(),
							bihinStateTxt, bihinWish);
					bihinShinseiList.add(bihinMap);
				}

			} else {
				// 備品申請データが無かった場合
				List<Skf2020Sc003GetBihinItemExp> bihinItemList = new ArrayList<Skf2020Sc003GetBihinItemExp>();
				bihinItemList = skf2020Sc003GetBihinItemExpRepository.getBihinItem();
				if (bihinItemList != null && bihinItemList.size() > 0) {
					for (Skf2020Sc003GetBihinItemExp bihinItem : bihinItemList) {
						String bihinWish = "0";
						String bihinStateTxt = bihinInfoMap.get(CodeConstant.BIHIN_STATE_NONE);
						Map<String, Object> bihinMap = setBihinData(dto, bihinItem.getBihinCd(),
								bihinItem.getBihinName(), bihinStateTxt, bihinWish);
						bihinShinseiList.add(bihinMap);
					}
				}
			}
		}
		dto.setBihinShinseiList(bihinShinseiList);
		return;

	}

	/**
	 * 添付資料データを設定します
	 * 
	 * @param fileName
	 * @param file
	 * @param fileSize
	 */
	@SuppressWarnings({ "static-access" })
	private void addShatakuAttachedFile(String fileName, byte[] file, String fileSize, int attachedNo,
			List<Map<String, Object>> shatakuAttachedFileList) {
		// 添付資料のコレクションをSessionより取得

		// リンクリストチェック
		boolean findFlg = false;
		if (shatakuAttachedFileList != null) {
			for (Map<String, Object> attachedFileMap : shatakuAttachedFileList) {
				if (fileName.equals(attachedFileMap.get("attachedName"))) {
					findFlg = true;
					break;
				}
			}
		} else {
			shatakuAttachedFileList = new ArrayList<Map<String, Object>>();
		}

		// 添付ファイルリストに無い場合
		if (!findFlg) {
			Map<String, Object> addAttachedFileInfo = new HashMap<String, Object>();

			addAttachedFileInfo.put("attachedNo", attachedNo);

			// 添付資料名
			addAttachedFileInfo.put("attachedName", fileName);
			// ファイルサイズ
			addAttachedFileInfo.put("attachedFileSize", fileSize);
			// 更新日
			addAttachedFileInfo.put("registDate", new Date());
			// 添付資料
			addAttachedFileInfo.put("fileStream", file);
			// 添付ファイルステータス
			// ファイルタイプ
			addAttachedFileInfo.put("fileType", skfAttachedFileUtils.getFileTypeInfo(fileName));

			shatakuAttachedFileList.add(addAttachedFileInfo);
		}

		return;
	}

	/**
	 * 要返却備品のリスト作成
	 * 
	 * @param applNo
	 * @param dto
	 */
	private void setBihinItemToBeReturn(String applNo, Skf2020Sc003CommonDto dto) {
		String shatakuKanriId = String.valueOf(
				getNowShatakuKanriID(dto.getShainNo(), dto.getNowShatakuKanriNo(), dto.getNowShatakuRoomKanriNo()));
		String yearMonth = skfDateFormatUtils.dateFormatFromDate(new Date(), SkfCommonConstant.YMD_STYLE_YYYYMM_FLAT);
		// 備品状態が2:保有備品または3:レンタルの表示
		List<Skf2020Sc003GetBihinInfoExp> bihinInfoList = new ArrayList<Skf2020Sc003GetBihinInfoExp>();
		bihinInfoList = getBihinInfo(shatakuKanriId, dto.getShainNo(), yearMonth);
		String returnEquipment = CodeConstant.NONE;
		if (bihinInfoList != null && bihinInfoList.size() > 0) {
			List<String> colList = new ArrayList<String>(); // テーブルのTD領域
			List<List<String>> rowList = new ArrayList<List<String>>(); // テーブルのTR領域
			int index = 0;
			for (Skf2020Sc003GetBihinInfoExp bihinInfo : bihinInfoList) {
				if (index <= 2) {
					colList.add(bihinInfo.getBihinName());
				} else {
					rowList.add(colList);
					colList = new ArrayList<String>();
					colList.add(bihinInfo.getBihinName());
					index = 1;
				}
			}
			returnEquipment = skfHtmlCreateUtils.htmlBihinCreateTable(rowList, 2);
		}
		dto.setReturnEquipment(returnEquipment);
		return;
	}

	/**
	 * 画面表示処理
	 * 
	 * @param dto
	 */
	private void setDispVisible(Skf2020Sc003CommonDto dto) {
		// 社宅を必要する場合以外は、編集ボタンを非表示
		String taiyoHitsuyo = dto.getTaiyoHituyo();
		dto.setEditBtnVisible(true);
		if (!taiyoHitsuyo.equals(CodeConstant.ASKED_SHATAKU_HITSUYO)) {
			dto.setEditBtnVisible(false);
		}
	}

	private Map<String, Object> setBihinData(Skf2020Sc003CommonDto dto, String bihinCd, String bihinName,
			String bihinStateText, String bihinWish) {
		String disabledFlag = "false";
		if (BIHIN_STATE_SONAETSUKE.equals(bihinStateText)) {
			bihinWish = CodeConstant.BIHIN_KIBO_FUKA;
			disabledFlag = "true";
		}

		Map<String, Object> bihinMap = new HashMap<String, Object>();
		bihinMap.put("bihinCd", bihinCd);
		bihinMap.put("bihinName", bihinName);
		bihinMap.put("bihinState", bihinStateText);
		bihinMap.put("bihinWish", bihinWish);
		bihinMap.put("dropDown", skfDropDownUtils.getGenericForDoropDownList("SKF1055", bihinWish, false));
		bihinMap.put("disabled", disabledFlag);

		return bihinMap;
	}

	private Skf2020Sc003GetShatakuNyukyoKiboInfoExp getShatakuNyukyoKiboInfo(String companyCd, String applNo) {
		Skf2020Sc003GetShatakuNyukyoKiboInfoExp shatakuNyukyoKiboInfo = new Skf2020Sc003GetShatakuNyukyoKiboInfoExp();
		Skf2020Sc003GetShatakuNyukyoKiboInfoExpParameter param = new Skf2020Sc003GetShatakuNyukyoKiboInfoExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		shatakuNyukyoKiboInfo = skf2020Sc003GetShatakuNyukyoKiboInfoExpRepository.getShatakuNyukyoKiboInfo(param);

		return shatakuNyukyoKiboInfo;
	}

	/**
	 * 保有社宅の社宅管理IDを取得します
	 * 
	 * @param shainNo
	 * @param shatakuKanriNo
	 * @param shatakuRoomKanriNo
	 * @return
	 */
	private long getNowShatakuKanriID(String shainNo, String shatakuKanriNo, String shatakuRoomKanriNo) {
		long shatakuKanriId = 0;
		String nyukyoDate = skfDateFormatUtils.dateFormatFromDate(new Date(), "yyyyMMdd");
		List<Skf2020Sc003GetShatakuNameListExp> shatakuNameList = new ArrayList<Skf2020Sc003GetShatakuNameListExp>();
		Skf2020Sc003GetShatakuNameListExpParameter param = new Skf2020Sc003GetShatakuNameListExpParameter();
		param.setShainNo(shainNo);
		param.setNyukyoDate(nyukyoDate);
		shatakuNameList = skf2020Sc003GetShatakuNameListExpRepository.getShatakuNameList(param);
		if (shatakuNameList != null && shatakuNameList.size() > 0) {
			for (Skf2020Sc003GetShatakuNameListExp shatakuNameData : shatakuNameList) {
				if (shatakuKanriNo.equals(String.valueOf(shatakuNameData.getShatakuKanriNo()))
						&& shatakuRoomKanriNo.equals(String.valueOf(shatakuNameData.getShatakuRoomKanriNo()))) {
					shatakuKanriId = shatakuNameData.getShatakuKanriId();
				}
			}
		}

		return shatakuKanriId;
	}

	private List<Skf2020Sc003GetBihinInfoExp> getBihinInfo(String shatakuKanriId, String shainNo, String yearMonth) {
		List<Skf2020Sc003GetBihinInfoExp> bihinInfo = new ArrayList<Skf2020Sc003GetBihinInfoExp>();
		Skf2020Sc003GetBihinInfoExpParameter param = new Skf2020Sc003GetBihinInfoExpParameter();
		param.setShatakuKanriId(Long.parseLong(shatakuKanriId));
		param.setShainNo(shainNo);
		param.setYearMonth(yearMonth);
		bihinInfo = skf2020Sc003GetBihinInfoExpRepository.getBihinInfo(param);

		return bihinInfo;
	}

	private List<Skf2020Sc003GetBihinInfoOutExp> getBihinInfoOut(Long shatakuKanriNo, Long shatakuRoomKanriNo,
			String applNo, String nyutaikyoKbn) {
		List<Skf2020Sc003GetBihinInfoOutExp> bihinInfo = new ArrayList<Skf2020Sc003GetBihinInfoOutExp>();
		Skf2020Sc003GetBihinInfoOutExpParameter param = new Skf2020Sc003GetBihinInfoOutExpParameter();
		param.setShatakuKanriNo(shatakuKanriNo);
		param.setShatakuRoomKanriNo(shatakuRoomKanriNo);
		param.setApplNo(applNo);
		param.setNyutaikyoKbn(nyutaikyoKbn);
		bihinInfo = skf2020Sc003GetBihinInfoOutExpRepository.getBihinInfoOut(param);

		return bihinInfo;
	}

	/**
	 * 条件を指定して提示データ情報を取得します
	 * 
	 * @param shainNo
	 * @param nyutaikyoKbn
	 * @param applNo
	 * @return
	 */
	private List<Skf2020Sc003GetTeijiDataInfoExp> getTeijiDataInfo(String shainNo, String nyutaikyoKbn, String applNo) {
		List<Skf2020Sc003GetTeijiDataInfoExp> teijiDataList = new ArrayList<Skf2020Sc003GetTeijiDataInfoExp>();
		Skf2020Sc003GetTeijiDataInfoExpParameter param = new Skf2020Sc003GetTeijiDataInfoExpParameter();
		param.setShainNo(shainNo);
		param.setNyutaikyoKbn(nyutaikyoKbn);
		param.setApplNo(applNo);
		teijiDataList = skf2020Sc003GetTeijiDataInfoExpRepository.getTeijiDataInfo(param);
		return teijiDataList;
	}

	/**
	 * 社宅管理システム都道府県名称取得
	 * 
	 * @param prefCd
	 * @return
	 */
	private String getShatakuPrefName(String prefCd) {
		String retPrefName = CodeConstant.NONE;

		// 都道府県名称の取得
		Map<String, String> prefMap = new HashMap<String, String>();
		prefMap = skfGenericCodeUtils.getGenericCode("SKF1064");
		if (prefMap != null) {
			retPrefName = prefMap.get(prefCd);
		}

		return retPrefName;
	}

	/**
	 * 社宅管理システム規格名称取得
	 * 
	 * @param kikakuCd
	 * @return
	 */
	private String getShatakuKikakuKBN(String kikakuCd) {
		String retKikakuName = CodeConstant.NONE;

		// 規格区分の取得
		Map<String, String> kikakuMap = new HashMap<String, String>();
		kikakuMap = skfGenericCodeUtils.getGenericCode("SKF1073");
		if (kikakuMap != null) {
			retKikakuName = kikakuMap.get(kikakuCd);
		}

		return retKikakuName;
	}

	private String getMaxCycleBillingYYMM() {
		String syoriYYMM = CodeConstant.NONE;
		Skf2020Sc003GetMaxCycleBillingYYMMExp resultData = new Skf2020Sc003GetMaxCycleBillingYYMMExp();
		resultData = skf2020Sc003GetMaxCycleBillingYYMMExpRepository.getMaxCycleBillingYYMM();
		if (resultData != null) {
			syoriYYMM = resultData.getMaxCycleBillingYymm();
		}

		return syoriYYMM;
	}

	private List<Skf2020Sc003GetParkingRirekiDataExp> getParkingRirekiData(long shatakuKanriNo, long shatakuRoomKanriNo,
			String shainNo, String yearMonth) {
		List<Skf2020Sc003GetParkingRirekiDataExp> returnList = new ArrayList<Skf2020Sc003GetParkingRirekiDataExp>();
		Skf2020Sc003GetParkingRirekiDataExpParameter param = new Skf2020Sc003GetParkingRirekiDataExpParameter();
		param.setShainNo(shainNo);
		param.setShatakuKanriNo(shatakuKanriNo);
		param.setShatakuRoomKanriNo(shatakuRoomKanriNo);
		param.setYearMonth(yearMonth);
		returnList = skf2020Sc003GetParkingRirekiDataExpRepository.getParkingRirekiData(param);
		return returnList;
	}

	/**
	 * 備品希望申請情報を取得します
	 * 
	 * @param companyCd
	 * @param applNo
	 * @return
	 */
	private List<Skf2020Sc003GetBihinShinseiInfoExp> getBihinShinseiInfo(String companyCd, String applNo) {
		List<Skf2020Sc003GetBihinShinseiInfoExp> bihinShinseiList = new ArrayList<Skf2020Sc003GetBihinShinseiInfoExp>();
		Skf2020Sc003GetBihinShinseiInfoExpParameter param = new Skf2020Sc003GetBihinShinseiInfoExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		bihinShinseiList = skf2020Sc003GetBihinShinseiInfoExpRepository.getBihinShinseiInfo(param);
		return bihinShinseiList;
	}

	/**
	 * 備品情報の件数を取得します
	 * 
	 * @param companyCd
	 * @param applNo
	 * @return
	 */
	private long getBihinShinseiInfoCount(String companyCd, String applNo) {
		List<Skf2020Sc003GetBihinShinseiInfoExp> bihinShinseiList = new ArrayList<Skf2020Sc003GetBihinShinseiInfoExp>();
		Skf2020Sc003GetBihinShinseiInfoExpParameter param = new Skf2020Sc003GetBihinShinseiInfoExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		bihinShinseiList = skf2020Sc003GetBihinShinseiInfoExpRepository.getBihinShinseiInfo(param);
		if (bihinShinseiList == null) {
			return 0;
		}
		return bihinShinseiList.size();
	}

}
