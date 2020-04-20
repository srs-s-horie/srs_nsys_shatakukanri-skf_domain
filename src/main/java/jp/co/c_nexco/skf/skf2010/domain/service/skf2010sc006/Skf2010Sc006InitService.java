/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc006;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc006.Skf2010Sc006GetApplHistoryInfoByParameterExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBatchUtils.SkfBatchUtilsGetMultipleTablesUpdateDateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentListExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchi;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReport;
import jp.co.c_nexco.nfw.common.entity.base.BaseCodeEntity;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.SkfShatakuInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfTeijiDataInfoUtils;
import jp.co.c_nexco.skf.common.util.batch.SkfBatchUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc006.Skf2010Sc006InitDto;

/**
 * Skf2010Sc006 申請書類承認／差戻し／通知初期表示処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc006InitService extends SkfServiceAbstract<Skf2010Sc006InitDto> {

	@Autowired
	private Skf2010Sc006SharedService skf2010Sc006SharedService;

	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfTeijiDataInfoUtils skfTeijiDataInfoUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfBatchUtils skfBatchUtils;
	@Autowired
	private SkfShatakuInfoUtils skfShatakuInfoUtils;

	private String companyCd = CodeConstant.C001;

	private final String PLAN_TO_BUY_CAR = "購入を予定している";
	private final String KYOGICHU_TEXT = "協議中";
	private final String GOJITSU_TEXT = "（後日お知らせ）";

	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2010Sc006InitDto index(Skf2010Sc006InitDto initDto) throws Exception {
		// タイトル設定
		initDto.setPageTitleKey(MessageIdConstant.SKF2010_SC006_TITLE);

		// 操作ログ出力
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, FunctionIdConstant.SKF2010_SC006);

		String applStatus = initDto.getApplStatus();
		initDto.setApplStatusText(changeApplStatusText(applStatus));

		// 表示情報セット
		setDisplayData(initDto);

		String taiyoHitsuyo = initDto.getTaiyoHitsuyo();

		// アコーディオン初期表示指定
		checkDisplayLevel(applStatus, taiyoHitsuyo, initDto);

		// コメントボタンの活性非活性処理
		setCommentBtnDisabled(initDto);

		// 承認ボタン非表示設定
		setShoninBtnRemove(initDto);

		// データ連携用の排他制御用更新日を取得
		// 申請者の社員番号
		String shainNo = initDto.getShainNo();
		Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> dateLinkageMap = skfBatchUtils
				.getUpdateDateForUpdateSQL(shainNo);
		menuScopeSessionBean.put(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2010SC006, dateLinkageMap);

		return initDto;
	}

	/**
	 * 承認者チェックを行います
	 * 
	 * @param initDto
	 */
	private void setShoninBtnRemove(Skf2010Sc006InitDto initDto) {
		// ステータスが審査中以外で承認者が前の承認者と同一の場合、「再提示」「資料ファイル添付」「承認」「コメント表示」ボタンを非表示にする
		Map<String, String> loginUser = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		String userName = loginUser.get("userName");
		String roleId = loginUser.get("roleId");
		if (userName.equals(initDto.getShonin1Name())
				&& !CheckUtils.isEqual(initDto.getApplStatus(), CodeConstant.STATUS_SHINSACHU)) {
			initDto.setShoninBtnViewFlag("false");
			initDto.setCommentAreaVisibled(false);
		} else {
			initDto.setShoninBtnViewFlag("true");
		}

		// ユーザーの権限チェック
		switch (roleId) {
		case CodeConstant.SKF_220:
		case CodeConstant.SKF_230:
		case CodeConstant.SKF_900:
			break;
		default:
			// 承認権限がないユーザーは「再提示」「資料添付」「承認」ボタンを非表示にする。
			initDto.setMaskPattern("NON");
			break;
		}

	}

	/**
	 * コメント表示ボタンの表示非表示を設定します
	 * 
	 * @param initDto
	 * @throws Exception
	 * @throws IllegalAccessException
	 */
	private void setCommentBtnDisabled(Skf2010Sc006InitDto initDto) throws IllegalAccessException, Exception {
		List<SkfCommentUtilsGetCommentListExp> commentList = new ArrayList<SkfCommentUtilsGetCommentListExp>();
		String applStatus = "";
		// 権限チェック
		Map<String, String> loginUserInfo = new HashMap<String, String>();
		loginUserInfo = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		String roleId = loginUserInfo.get("roleId");

		// 一般ユーザーかチェック
		boolean isAdmin = false;
		switch (roleId) {
		case CodeConstant.NAKASA_SHATAKU_TANTO:
		case CodeConstant.NAKASA_SHATAKU_KANRI:
		case CodeConstant.SYSTEM_KANRI:
			isAdmin = true;
			break;
		default:
			isAdmin = false;
			break;
		}
		// 一般ユーザーの場合、申請状況に「承認１」をセット
		if (!isAdmin) {
			applStatus = CodeConstant.STATUS_SHONIN1;
		}

		commentList = skf2010Sc006SharedService.getApplCommentList(initDto.getApplNo(), applStatus);
		if (commentList == null || commentList.size() <= 0) {
			// コメントが無ければ非表示
			initDto.setCommentViewFlag("false");
		} else {
			// コメントがあれば表示
			initDto.setCommentViewFlag("true");

		}
		// ステータスが承認１の時はコメントを取得
		if (CheckUtils.isEqual(initDto.getApplStatus(), CodeConstant.STATUS_SHONIN1)) {
			List<SkfCommentUtilsGetCommentInfoExp> commentInfo = new ArrayList<SkfCommentUtilsGetCommentInfoExp>();
			commentInfo = skf2010Sc006SharedService.getApplCommentInfo(initDto.getApplNo(), initDto.getApplStatus());
			if (commentInfo != null && commentInfo.size() > 0) {
				String commentNote = commentInfo.get(0).getCommentNote();
				initDto.setCommentNote(commentNote);
			}
		}
		return;
	}

	private String changeApplStatusText(String applStatus) {
		String applStatusText = "";

		Map<String, BaseCodeEntity> applStatusMap = codeCacheUtils.getGenericCode("SKF1001");

		// 申請状況をコードから汎用コードに変更
		if (applStatus != null) {
			BaseCodeEntity baseCodeEntity = new BaseCodeEntity();
			baseCodeEntity = applStatusMap.get(applStatus);
			applStatusText = baseCodeEntity.getCodeName();

			// applStatus = "審査中";
		}
		return applStatusText;
	}

	private void checkDisplayLevel(String applStatus, String taiyoHitsuyo, Skf2010Sc006InitDto initDto) {
		/**
		 * displayLevel : 項目表示レベル アコーディオン項目をどこまで表示するかをこれで指定する。
		 */
		int defaultDisplayLevel = 0;
		if (CheckUtils.isEqual(initDto.getApplId(), FunctionIdConstant.R0100)) {
			switch (applStatus) {
			case CodeConstant.STATUS_ICHIJIHOZON:
			case CodeConstant.STATUS_SASHIMODOSHI:
			case CodeConstant.STATUS_HININ:
			case CodeConstant.STATUS_SHINSACHU:
				initDto.setDisplayLevel(1);
				// 貸与必要フラグが「0：不要」だった場合
				if (!CheckUtils.isEqual(taiyoHitsuyo, CodeConstant.ASKED_SHATAKU_FUYOU)) {
					initDto.setMaskPattern("NON");
				}
				initDto.setLevel1Open("true");
				initDto.setLevel2Open("false");
				initDto.setLevel3Open("false");

				if (!CheckUtils.isEqual(applStatus, CodeConstant.STATUS_SHINSACHU)) {
					initDto.setRevisionRemandBtnFlg(true);
				}

				break;
			case CodeConstant.STATUS_KAKUNIN_IRAI:
				initDto.setDisplayLevel(2);
				initDto.setMaskPattern("NON");
				initDto.setLevel1Open("false");
				initDto.setLevel2Open("true");
				initDto.setLevel3Open("false");
				initDto.setRevisionRemandBtnFlg(true);
				break;
			case CodeConstant.STATUS_DOI_ZUMI:
			case CodeConstant.STATUS_SHONIN:
			case CodeConstant.STATUS_SHONIN1:
				defaultDisplayLevel = 3;
				if (CheckUtils.isEqual(taiyoHitsuyo, CodeConstant.ASKED_SHATAKU_FUYOU)) {
					defaultDisplayLevel = 1;
					initDto.setLevel1Open("true");
					initDto.setLevel2Open("false");
					initDto.setLevel3Open("false");
				} else {
					initDto.setLevel1Open("false");
					initDto.setLevel2Open("false");
					initDto.setLevel3Open("true");
				}
				initDto.setDisplayLevel(defaultDisplayLevel);
				initDto.setRevisionRemandBtnFlg(true);
				break;
			case CodeConstant.STATUS_SHONIN_ZUMI:
				defaultDisplayLevel = 3;
				if (CheckUtils.isEqual(taiyoHitsuyo, CodeConstant.ASKED_SHATAKU_FUYOU)) {
					defaultDisplayLevel = 1;
					initDto.setLevel1Open("true");
					initDto.setLevel2Open("false");
					initDto.setLevel3Open("false");
				} else {
					initDto.setLevel1Open("false");
					initDto.setLevel2Open("false");
					initDto.setLevel3Open("true");
				}
				initDto.setDisplayLevel(defaultDisplayLevel);
				initDto.setMaskPattern("NON");
				initDto.setRevisionRemandBtnFlg(true);
				break;
			default:
				initDto.setDisplayLevel(1);
				initDto.setMaskPattern("NON");
				initDto.setLevel1Open("true");
				initDto.setLevel2Open("false");
				initDto.setLevel3Open("false");
				initDto.setRevisionRemandBtnFlg(true);
				break;
			}
		} else if (CheckUtils.isEqual(initDto.getApplId(), FunctionIdConstant.R0103)) {
			// 退居（自動車の保管場所返還）届用
			initDto.setDisplayLevel(4);
			initDto.setMaskPattern("NON");
			initDto.setLevel1Open("false");
			initDto.setLevel2Open("false");
			initDto.setLevel3Open("false");
			initDto.setLevel4Open("true");
			initDto.setRevisionRemandBtnFlg(true);
		}
		return;
	}

	private void setDisplayData(Skf2010Sc006InitDto initDto) {
		String applNo = initDto.getApplNo();
		String applStatus = initDto.getApplStatus();
		String applId = CodeConstant.NONE;
		String applShainNo = CodeConstant.NONE;

		// 申請情報履歴を取得
		Skf2010Sc006GetApplHistoryInfoByParameterExp tApplHistoryData = new Skf2010Sc006GetApplHistoryInfoByParameterExp();
		tApplHistoryData = skf2010Sc006SharedService.getApplHistoryInfo(applNo);
		if (tApplHistoryData != null) {
			initDto.setApplId(tApplHistoryData.getApplId());
			applId = tApplHistoryData.getApplId();
			initDto.setShonin1Name(tApplHistoryData.getAgreName1());
			applShainNo = tApplHistoryData.getShainNo();
			// 排他処理用最終更新日付保存
			initDto.addLastUpdateDate(skf2010Sc006SharedService.KEY_LAST_UPDATE_DATE_HISTORY,
					tApplHistoryData.getUpdateDate());
		}

		if (CheckUtils.isEqual(applId, FunctionIdConstant.R0100)) {

			switch (applStatus) {
			case CodeConstant.STATUS_DOI_ZUMI:
			case CodeConstant.STATUS_SHONIN1:
			case CodeConstant.STATUS_SHONIN_ZUMI:
				// 入居希望等調書テーブルから個人負担金共益費フラグを取得
				boolean result = skfTeijiDataInfoUtils.selectKyoekihiKyogi(applShainNo,
						CodeConstant.NYUTAIKYO_KBN_NYUKYO, applNo);
				if (result) {
					// 協議中だった場合、承認ボタンが非活性かつエラーメッセージを出す
					initDto.setConfirmBtnDisabled(true);
					ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_2018);
				}
			}

			Skf2020TNyukyoChoshoTsuchi tNyukyoChoshoTsuchi = new Skf2020TNyukyoChoshoTsuchi();
			tNyukyoChoshoTsuchi = skf2010Sc006SharedService.getNyukyoChoshoTsuchiInfo(companyCd, applNo);
			if (tNyukyoChoshoTsuchi != null) {
				// 更新用
				String applDate = skfDateFormatUtils.dateFormatFromString(tNyukyoChoshoTsuchi.getApplDate(),
						"yyyy/MM/dd HH:mm:ss");
				initDto.setApplUpdateDate(applDate);
				// 社宅入居希望等調書
				mappingNyukyoChoshoTsuchi(initDto, tNyukyoChoshoTsuchi);
				// 貸与（予定）社宅等のご案内
				mappingTaiyoShatakuAnnai(initDto, tNyukyoChoshoTsuchi);

			}
		} else if (CheckUtils.isEqual(applId, FunctionIdConstant.R0103)) {
			Skf2040TTaikyoReport taikyoReport = new Skf2040TTaikyoReport();
			taikyoReport = skf2010Sc006SharedService.getTaikyoReportInfo(companyCd, applNo);
			// 退居届
			mappingTaikyoTodoke(initDto, taikyoReport);
		}

		// 添付資料情報取得
		List<Map<String, Object>> attachedFileList = new ArrayList<Map<String, Object>>();
		attachedFileList = skf2010Sc006SharedService.getAttachedFileInfo(applNo, false);
		initDto.setAttachedFileList(attachedFileList);

		switch (applStatus) {
		case CodeConstant.STATUS_ICHIJIHOZON:
		case CodeConstant.STATUS_KAKUNIN_IRAI:
		case CodeConstant.STATUS_HININ:
		case CodeConstant.STATUS_SASHIMODOSHI:
		case CodeConstant.STATUS_SHONIN_ZUMI:
			initDto.setCommentAreaVisibled(false);
			break;
		}

	}

	/**
	 * 入居希望等調書の表示データのマッピングを行います
	 * 
	 * @param initDto
	 * @param tNyukyoChoshoTsuchi
	 */
	private void mappingNyukyoChoshoTsuchi(Skf2010Sc006InitDto initDto,
			Skf2020TNyukyoChoshoTsuchi tNyukyoChoshoTsuchi) {
		// 申請番号

		// 入居希望申請調書
		// 社宅必要可否
		if (tNyukyoChoshoTsuchi.getTaiyoHitsuyo() != null) {
			switch (tNyukyoChoshoTsuchi.getTaiyoHitsuyo()) {
			case CodeConstant.ASKED_SHATAKU_HITSUYO:
				// 必要
				initDto.setTaiyoHitsuyoTrue(CodeConstant.CHECKED);
				initDto.setTaiyoHitsuyoFalse(CodeConstant.UNCHECKED);
				initDto.setTaiyoHitsuyoParking(CodeConstant.UNCHECKED);
				break;
			case CodeConstant.ASKED_SHATAKU_FUYOU:
				// 不要
				initDto.setTaiyoHitsuyoTrue(CodeConstant.UNCHECKED);
				initDto.setTaiyoHitsuyoFalse(CodeConstant.CHECKED);
				initDto.setTaiyoHitsuyoParking(CodeConstant.UNCHECKED);

				// 不要の場合、再提示ボタンを非表示
				initDto.setRepresentBtnFlg("true");
				break;
			case CodeConstant.ASKED_SHATAKU_PARKING_ONLY:
				// 駐車場のみ
				initDto.setTaiyoHitsuyoTrue(CodeConstant.UNCHECKED);
				initDto.setTaiyoHitsuyoFalse(CodeConstant.UNCHECKED);
				initDto.setTaiyoHitsuyoParking(CodeConstant.CHECKED);
				break;
			default:
				initDto.setTaiyoHitsuyoTrue(CodeConstant.UNCHECKED);
				initDto.setTaiyoHitsuyoFalse(CodeConstant.UNCHECKED);
				initDto.setTaiyoHitsuyoParking(CodeConstant.UNCHECKED);
				break;
			}
			initDto.setTaiyoHitsuyo(tNyukyoChoshoTsuchi.getTaiyoHitsuyo());
		} else {
			initDto.setTaiyoHitsuyoTrue(CodeConstant.UNCHECKED);
			initDto.setTaiyoHitsuyoFalse(CodeConstant.UNCHECKED);
			initDto.setTaiyoHitsuyoParking(CodeConstant.UNCHECKED);
		}

		// 社宅必要理由
		if (tNyukyoChoshoTsuchi.getHitsuyoRiyu() != null) {
			switch (tNyukyoChoshoTsuchi.getHitsuyoRiyu()) {
			case CodeConstant.IDOU:
				// 異動のため
				initDto.setHitsuyoRiyuIdou(CodeConstant.CHECKED);
				initDto.setHitsuyoRiyuMarry(CodeConstant.UNCHECKED);
				initDto.setHitsuyoRiyuOther(CodeConstant.UNCHECKED);
				break;
			case CodeConstant.KEKKON:
				// 結婚のため
				initDto.setHitsuyoRiyuIdou(CodeConstant.UNCHECKED);
				initDto.setHitsuyoRiyuMarry(CodeConstant.CHECKED);
				initDto.setHitsuyoRiyuOther(CodeConstant.UNCHECKED);
				break;
			case CodeConstant.HITUYO_RIYU_OTHERS:
				// 結婚のため
				initDto.setHitsuyoRiyuIdou(CodeConstant.UNCHECKED);
				initDto.setHitsuyoRiyuMarry(CodeConstant.UNCHECKED);
				initDto.setHitsuyoRiyuOther(CodeConstant.CHECKED);
				break;
			default:
				initDto.setHitsuyoRiyuIdou(CodeConstant.UNCHECKED);
				initDto.setHitsuyoRiyuMarry(CodeConstant.UNCHECKED);
				initDto.setHitsuyoRiyuOther(CodeConstant.UNCHECKED);
				break;
			}
		} else {
			initDto.setHitsuyoRiyuIdou(CodeConstant.UNCHECKED);
			initDto.setHitsuyoRiyuMarry(CodeConstant.UNCHECKED);
			initDto.setHitsuyoRiyuOther(CodeConstant.UNCHECKED);
		}

		// 社宅不必要理由
		if (tNyukyoChoshoTsuchi.getFuhitsuyoRiyu() != null) {
			switch (tNyukyoChoshoTsuchi.getFuhitsuyoRiyu()) {
			case CodeConstant.JITAKU_TSUKIN:
				// 自宅通勤
				initDto.setFuhitsuyoRiyuMyHome(CodeConstant.CHECKED);
				initDto.setFuhitsuyoRiyuSelfRental(CodeConstant.UNCHECKED);
				initDto.setFuhitsuyoRiyuOther(CodeConstant.UNCHECKED);
				break;
			case CodeConstant.JIKO_KARIAGE:
				// 自己借上
				initDto.setFuhitsuyoRiyuMyHome(CodeConstant.UNCHECKED);
				initDto.setFuhitsuyoRiyuSelfRental(CodeConstant.CHECKED);
				initDto.setFuhitsuyoRiyuOther(CodeConstant.UNCHECKED);
				break;
			case CodeConstant.FUYO_RIYU_OTHERS:
				// その他
				initDto.setFuhitsuyoRiyuMyHome(CodeConstant.UNCHECKED);
				initDto.setFuhitsuyoRiyuSelfRental(CodeConstant.UNCHECKED);
				initDto.setFuhitsuyoRiyuOther(CodeConstant.CHECKED);
				break;
			default:
				initDto.setFuhitsuyoRiyuMyHome(CodeConstant.UNCHECKED);
				initDto.setFuhitsuyoRiyuSelfRental(CodeConstant.UNCHECKED);
				initDto.setFuhitsuyoRiyuOther(CodeConstant.UNCHECKED);
				break;
			}
		} else {
			initDto.setFuhitsuyoRiyuMyHome(CodeConstant.UNCHECKED);
			initDto.setFuhitsuyoRiyuSelfRental(CodeConstant.UNCHECKED);
			initDto.setFuhitsuyoRiyuOther(CodeConstant.UNCHECKED);
		}

		// 社員番号
		initDto.setShainNo(tNyukyoChoshoTsuchi.getShainNo());
		// 氏名
		initDto.setName(tNyukyoChoshoTsuchi.getName());
		// 等級
		initDto.setTokyu(tNyukyoChoshoTsuchi.getTokyu());
		// 所属
		// 現所属：機関
		initDto.setNowAgency(tNyukyoChoshoTsuchi.getAgency());
		// 現所属：部等
		initDto.setNowAffiliation1(tNyukyoChoshoTsuchi.getAffiliation1());
		// 現所属：室、チーム又は課
		initDto.setNowAffiliation2(tNyukyoChoshoTsuchi.getAffiliation2());
		// 現所属：勤務先のTEL
		initDto.setNowTel(tNyukyoChoshoTsuchi.getTel());
		// 新所属：機関
		initDto.setNewAgency(tNyukyoChoshoTsuchi.getNewAgency());
		// 新所属：部等
		initDto.setNewAffiliation1(tNyukyoChoshoTsuchi.getNewAffiliation1());
		// 新所属：室、チーム又は課
		initDto.setNewAffiliation2(tNyukyoChoshoTsuchi.getNewAffiliation2());

		// 必要とする社宅
		if (tNyukyoChoshoTsuchi.getHitsuyoShataku() != null) {
			switch (tNyukyoChoshoTsuchi.getHitsuyoShataku()) {
			case CodeConstant.SETAI:
				// 世帯
				initDto.setHitsuyouShatakuFamily(CodeConstant.CHECKED);
				initDto.setHitsuyouShatakuOnly(CodeConstant.UNCHECKED);
				initDto.setHitsuyouShatakuSingle(CodeConstant.UNCHECKED);
				break;
			case CodeConstant.TANSHIN:
				// 単身
				initDto.setHitsuyouShatakuFamily(CodeConstant.UNCHECKED);
				initDto.setHitsuyouShatakuOnly(CodeConstant.CHECKED);
				initDto.setHitsuyouShatakuSingle(CodeConstant.UNCHECKED);
				break;
			case CodeConstant.DOKUSHIN:
				// 独身
				initDto.setHitsuyouShatakuFamily(CodeConstant.UNCHECKED);
				initDto.setHitsuyouShatakuOnly(CodeConstant.UNCHECKED);
				initDto.setHitsuyouShatakuSingle(CodeConstant.CHECKED);
				break;
			default:
				initDto.setHitsuyouShatakuFamily(CodeConstant.UNCHECKED);
				initDto.setHitsuyouShatakuOnly(CodeConstant.UNCHECKED);
				initDto.setHitsuyouShatakuSingle(CodeConstant.UNCHECKED);
				break;
			}
		} else {
			initDto.setHitsuyouShatakuFamily(CodeConstant.UNCHECKED);
			initDto.setHitsuyouShatakuOnly(CodeConstant.UNCHECKED);
			initDto.setHitsuyouShatakuSingle(CodeConstant.UNCHECKED);
		}

		// 同居家族続柄１
		initDto.setDokyoRelation1(tNyukyoChoshoTsuchi.getDokyoRelation1());
		// 同居家族氏名１
		initDto.setDokyoName1(tNyukyoChoshoTsuchi.getDokyoName1());
		// 同居家族年齢１
		initDto.setDokyoAge1(tNyukyoChoshoTsuchi.getDokyoAge1());
		// 同居家族続柄2
		initDto.setDokyoRelation2(tNyukyoChoshoTsuchi.getDokyoRelation2());
		// 同居家族氏名2
		initDto.setDokyoName2(tNyukyoChoshoTsuchi.getDokyoName2());
		// 同居家族年齢2
		initDto.setDokyoAge2(tNyukyoChoshoTsuchi.getDokyoAge2());
		// 同居家族続柄3
		initDto.setDokyoRelation3(tNyukyoChoshoTsuchi.getDokyoRelation3());
		// 同居家族氏名3
		initDto.setDokyoName3(tNyukyoChoshoTsuchi.getDokyoName3());
		// 同居家族年齢3
		initDto.setDokyoAge3(tNyukyoChoshoTsuchi.getDokyoAge3());
		// 同居家族続柄4
		initDto.setDokyoRelation4(tNyukyoChoshoTsuchi.getDokyoRelation4());
		// 同居家族氏名4
		initDto.setDokyoName4(tNyukyoChoshoTsuchi.getDokyoName4());
		// 同居家族年齢4
		initDto.setDokyoAge4(tNyukyoChoshoTsuchi.getDokyoAge4());
		// 同居家族続柄5
		initDto.setDokyoRelation5(tNyukyoChoshoTsuchi.getDokyoRelation5());
		// 同居家族氏名5
		initDto.setDokyoName5(tNyukyoChoshoTsuchi.getDokyoName5());
		// 同居家族年齢5
		initDto.setDokyoAge5(tNyukyoChoshoTsuchi.getDokyoAge5());
		// 同居家族続柄5
		initDto.setDokyoRelation5(tNyukyoChoshoTsuchi.getDokyoRelation5());
		// 同居家族氏名5
		initDto.setDokyoName5(tNyukyoChoshoTsuchi.getDokyoName5());
		// 同居家族年齢5
		initDto.setDokyoAge5(tNyukyoChoshoTsuchi.getDokyoAge5());
		// 同居家族続柄6
		initDto.setDokyoRelation6(tNyukyoChoshoTsuchi.getDokyoRelation6());
		// 同居家族氏名6
		initDto.setDokyoName6(tNyukyoChoshoTsuchi.getDokyoName6());
		// 同居家族年齢6
		initDto.setDokyoAge6(tNyukyoChoshoTsuchi.getDokyoAge6());

		// 入居予定日
		initDto.setNyukyoYoteiDate(
				skfDateFormatUtils.dateFormatFromString(tNyukyoChoshoTsuchi.getNyukyoYoteiDate(), "yyyy年MM月dd日"));
		// 入居可能日
		initDto.setNyukyoKanoDate(
				skfDateFormatUtils.dateFormatFromString(tNyukyoChoshoTsuchi.getNyukyoKanoDate(), "yyyy年MM月dd日"));

		// 保管場所
		if (tNyukyoChoshoTsuchi.getParkingUmu() != null) {
			switch (tNyukyoChoshoTsuchi.getParkingUmu()) {
			case CodeConstant.CAR_PARK_HITUYO:
				// 必要
				initDto.setParkingUmuTrue(CodeConstant.CHECKED);
				initDto.setParkingUmuFalse(CodeConstant.UNCHECKED);
				break;
			case CodeConstant.CAR_PARK_FUYO:
				// 不要
				initDto.setParkingUmuTrue(CodeConstant.UNCHECKED);
				initDto.setParkingUmuFalse(CodeConstant.CHECKED);
				break;
			}
		} else {
			initDto.setParkingUmuTrue(CodeConstant.UNCHECKED);
			initDto.setParkingUmuFalse(CodeConstant.UNCHECKED);
		}

		// 自動車番号登録フラグ
		initDto.setCarNoInputFlg(tNyukyoChoshoTsuchi.getCarNoInputFlg());
		// 自動車の車名
		String carName = tNyukyoChoshoTsuchi.getCarName();
		if (CheckUtils.isEqual(tNyukyoChoshoTsuchi.getCarNoInputFlg(), CodeConstant.CAR_YOTEI)) {
			if (NfwStringUtils.isEmpty(carName)) {
				carName = PLAN_TO_BUY_CAR;
			}
		}
		initDto.setCarName(carName);
		// 自動車の登録番号
		initDto.setCarNo(tNyukyoChoshoTsuchi.getCarNo());
		// 自動車の使用者
		initDto.setCarUser(tNyukyoChoshoTsuchi.getCarUser());
		// 車検の有効期間満了日
		initDto.setCarExpirationDate(
				skfDateFormatUtils.dateFormatFromString(tNyukyoChoshoTsuchi.getCarExpirationDate(), "yyyy年MM月dd日"));
		// 自動車の保管場所使用開始日
		initDto.setParkingUserDate(
				skfDateFormatUtils.dateFormatFromString(tNyukyoChoshoTsuchi.getParkingUseDate(), "yyyy年MM月dd日"));
		// 自動車番号登録フラグ2
		initDto.setCarNoInputFlg2(tNyukyoChoshoTsuchi.getCarNoInputFlg2());
		// 自動車の車名2
		String carName2 = tNyukyoChoshoTsuchi.getCarName2();
		if (CheckUtils.isEqual(tNyukyoChoshoTsuchi.getCarNoInputFlg2(), CodeConstant.CAR_YOTEI)
				&& NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getCarUser2())) {
			if (NfwStringUtils.isEmpty(carName2)) {
				carName2 = PLAN_TO_BUY_CAR;
			}
		}
		initDto.setCarName2(carName2);
		// 自動車の登録番号2
		initDto.setCarNo2(tNyukyoChoshoTsuchi.getCarNo2());
		// 自動車の使用者2
		initDto.setCarUser2(tNyukyoChoshoTsuchi.getCarUser2());
		// 車検の有効期間満了日2
		initDto.setCarExpirationDate2(
				skfDateFormatUtils.dateFormatFromString(tNyukyoChoshoTsuchi.getCarExpirationDate2(), "yyyy年MM月dd日"));
		// 自動車の保管場所使用開始日2
		initDto.setParkingUserDate2(
				skfDateFormatUtils.dateFormatFromString(tNyukyoChoshoTsuchi.getParkingUseDate2(), "yyyy年MM月dd日"));

		// 入居社宅
		if (tNyukyoChoshoTsuchi.getNowShataku() != null) {
			switch (tNyukyoChoshoTsuchi.getNowShataku()) {
			case CodeConstant.GENNYUKYO_SHATAKU_KBN_HOYU:
				// 保有
				initDto.setNowShatakuPossession(CodeConstant.CHECKED);
				initDto.setNowShatakuMyHome(CodeConstant.UNCHECKED);
				initDto.setNowShatakuSelfRental(CodeConstant.UNCHECKED);
				initDto.setNowShatakuOther(CodeConstant.UNCHECKED);
				break;
			case CodeConstant.GENNYUKYO_SHATAKU_KBN_JITAKU:
				// 自宅
				initDto.setNowShatakuPossession(CodeConstant.UNCHECKED);
				initDto.setNowShatakuMyHome(CodeConstant.CHECKED);
				initDto.setNowShatakuSelfRental(CodeConstant.UNCHECKED);
				initDto.setNowShatakuOther(CodeConstant.UNCHECKED);
				break;
			case CodeConstant.GENNYUKYO_SHATAKU_KBN_JIKO_KARIAGE:
				// 自己借上
				initDto.setNowShatakuPossession(CodeConstant.UNCHECKED);
				initDto.setNowShatakuMyHome(CodeConstant.UNCHECKED);
				initDto.setNowShatakuSelfRental(CodeConstant.CHECKED);
				initDto.setNowShatakuOther(CodeConstant.UNCHECKED);
				break;
			case CodeConstant.GENNYUKYO_SHATAKU_KBN_OTHERS:
				// その他
				initDto.setNowShatakuPossession(CodeConstant.UNCHECKED);
				initDto.setNowShatakuMyHome(CodeConstant.UNCHECKED);
				initDto.setNowShatakuSelfRental(CodeConstant.UNCHECKED);
				initDto.setNowShatakuOther(CodeConstant.CHECKED);
				break;
			default:
				initDto.setNowShatakuPossession(CodeConstant.UNCHECKED);
				initDto.setNowShatakuMyHome(CodeConstant.UNCHECKED);
				initDto.setNowShatakuSelfRental(CodeConstant.UNCHECKED);
				initDto.setNowShatakuOther(CodeConstant.UNCHECKED);
				break;
			}
		} else {
			initDto.setNowShatakuPossession(CodeConstant.UNCHECKED);
			initDto.setNowShatakuMyHome(CodeConstant.UNCHECKED);
			initDto.setNowShatakuSelfRental(CodeConstant.UNCHECKED);
			initDto.setNowShatakuOther(CodeConstant.UNCHECKED);
		}

		// 保有社宅名
		initDto.setNowShatakuName(tNyukyoChoshoTsuchi.getNowShatakuName());
		// 保有社宅号室
		initDto.setNowShatakuNo(tNyukyoChoshoTsuchi.getNowShatakuNo());
		// 保有社宅企画
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getNowShatakuKikaku())) {
			initDto.setNowShatakuKikaku(
					skfShatakuInfoUtils.getShatakuKikakuByCode(tNyukyoChoshoTsuchi.getNowShatakuKikaku()));
		}
		// 保有社宅面積
		initDto.setNowShatakuMenseki(tNyukyoChoshoTsuchi.getNowShatakuMenseki());

		// 現入居社宅
		if (tNyukyoChoshoTsuchi.getTaikyoYotei() != null) {
			switch (tNyukyoChoshoTsuchi.getTaikyoYotei()) {
			case CodeConstant.LEAVE:
				// 退居する
				initDto.setTaikyoYoteiTrue(CodeConstant.CHECKED);
				initDto.setTaikyoYoteiFalse(CodeConstant.UNCHECKED);
				break;
			case CodeConstant.NOT_LEAVE:
				// 継続使用する
				initDto.setTaikyoYoteiTrue(CodeConstant.UNCHECKED);
				initDto.setTaikyoYoteiFalse(CodeConstant.CHECKED);
				break;
			}
		} else {
			initDto.setTaikyoYoteiTrue(CodeConstant.UNCHECKED);
			initDto.setTaikyoYoteiFalse(CodeConstant.UNCHECKED);
		}

		// 保有社宅退居予定日
		initDto.setTaikyoYoteiDate(
				skfDateFormatUtils.dateFormatFromString(tNyukyoChoshoTsuchi.getTaikyoYoteiDate(), "yyyy年MM月dd日"));
		// 特殊事情
		initDto.setTokushuJijo(tNyukyoChoshoTsuchi.getTokushuJijo());

		return;
	}

	/**
	 * 貸与（予定）社宅等のご案内表示のマッピングを行います
	 * 
	 * @param initDto
	 * @param tNyukyoChoshoTsuchi
	 */
	private void mappingTaiyoShatakuAnnai(Skf2010Sc006InitDto initDto, Skf2020TNyukyoChoshoTsuchi tNyukyoChoshoTsuchi) {
		Map<String, String> hitsuyoRiyuMap = skfGenericCodeUtils
				.getGenericCode(FunctionIdConstant.GENERIC_CODE_SHATAKU_NEED_RIYU_KBN);

		// 案内日
		initDto.setTsuchiDate(
				skfDateFormatUtils.dateFormatFromString(tNyukyoChoshoTsuchi.getTsuchiDate(), "yyyy年MM月dd日"));
		// 申請日
		initDto.setApplDate(skfDateFormatUtils.dateFormatFromString(tNyukyoChoshoTsuchi.getApplDate(), "yyyy年MM月dd日"));

		// 制約日
		initDto.setSeiyakuDate(
				skfDateFormatUtils.dateFormatFromString(tNyukyoChoshoTsuchi.getSeiyakuDate(), "yyyy年MM月dd日"));

		// 社宅を必要とする理由
		String hitsuyoRiyu = tNyukyoChoshoTsuchi.getHitsuyoRiyu();
		String hitsuyoRiyuText = hitsuyoRiyuMap.get(hitsuyoRiyu);
		initDto.setHitsuyoRiyu(hitsuyoRiyuText);

		// 社宅所在地
		initDto.setNewShozaichi(tNyukyoChoshoTsuchi.getNewShozaichi());

		// 社宅名
		initDto.setNewShatakuName(tNyukyoChoshoTsuchi.getNewShatakuName());

		// 室番号
		initDto.setNewShatakuNo(tNyukyoChoshoTsuchi.getNewShatakuNo());

		// 規格
		String newShatakuKikaku = skfShatakuInfoUtils.getShatakuKikakuByCode(tNyukyoChoshoTsuchi.getNewShatakuKikaku());
		initDto.setNewShatakuKikaku(newShatakuKikaku);
		// initDto.setNewShatakuKikaku(tNyukyoChoshoTsuchi.getNewShatakuKikaku());

		// 面積
		initDto.setNewShatakuMenseki(tNyukyoChoshoTsuchi.getNewShatakuMenseki());

		// 数値のカンマ区切り設定
		NumberFormat nfNum = NumberFormat.getNumberInstance();

		// 使用料
		String newRental = tNyukyoChoshoTsuchi.getNewRental();
		if (newRental != null && NfwStringUtils.isNotEmpty(newRental)) {
			newRental = nfNum.format(Long.parseLong(newRental));
		}
		initDto.setNewRental(newRental);
		// 共益費
		String newKyoekihi = tNyukyoChoshoTsuchi.getNewKyoekihi();
		initDto.setNewKyoekihi(newKyoekihi);
		if (NfwStringUtils.isEmpty(newKyoekihi)) {
			if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getNewShatakuNo())) {
				initDto.setNewKyoekihi(GOJITSU_TEXT);
			}
		} else {
			// 個人負担共益費協議中フラグチェック
			if (CheckUtils.isEqual(tNyukyoChoshoTsuchi.getKyoekihiPersonKyogichuFlg(),
					CodeConstant.KYOEKIHI_KYOGICHU)) {
				initDto.setNewKyoekihi(KYOGICHU_TEXT);
			}
		}

		// 決定通知書用共益費設定
		String ketteiKyoekihi = tNyukyoChoshoTsuchi.getNewKyoekihi();
		initDto.setKetteiKyoekihi(ketteiKyoekihi);
		if (NfwStringUtils.isEmpty(ketteiKyoekihi)) {
			if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getNewShatakuNo())) {
				initDto.setKetteiKyoekihi(GOJITSU_TEXT);
			}
		}

		// 入居日
		initDto.setNyukyoKanoDate(skfDateFormatUtils.dateFormatFromString(tNyukyoChoshoTsuchi.getNyukyoKanoDate(),
				SkfCommonConstant.YMD_STYLE_YYYYMMDD_JP_STR));

		// 自動車１台目
		// 自動車の保管場所
		if (CheckUtils.isEqual(tNyukyoChoshoTsuchi.getParkingUmu(), CodeConstant.CAR_PARK_HITUYO)) {
			initDto.setParkingArea(tNyukyoChoshoTsuchi.getParkingArea());
			// 自動車の位置番号
			initDto.setCarIchiNo(tNyukyoChoshoTsuchi.getCarIchiNo());
			// 保管場所使用料
			String parkingRental = tNyukyoChoshoTsuchi.getParkingRental();
			if (parkingRental != null && NfwStringUtils.isNotEmpty(parkingRental)) {
				parkingRental = nfNum.format(Long.parseLong(parkingRental));
			}
			initDto.setParkingRental(parkingRental);

			// 自動車２台目
			if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getCarNo2())
					|| NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getCarUser2())) {
				// 自動車の保管場所
				initDto.setParkingArea2(tNyukyoChoshoTsuchi.getParkingArea2());
				// 自動車の位置番号
				initDto.setCarIchiNo2(tNyukyoChoshoTsuchi.getCarIchiNo2());
				// 保管場所使用料
				String parkingRental2 = tNyukyoChoshoTsuchi.getParkingRental2();
				if (parkingRental2 != null && NfwStringUtils.isNotEmpty(parkingRental2)) {
					parkingRental2 = nfNum.format(Long.parseLong(parkingRental2));
				}
				initDto.setParkingRental2(parkingRental2);
			}
		}

		// 自動車保管場所（１台目）の使用開始予定日
		initDto.setParkingKanoDate(
				skfDateFormatUtils.dateFormatFromString(tNyukyoChoshoTsuchi.getParkingKanoDate(), "yyyy年MM月dd日"));
		// 自動車保管場所（２台目）の使用開始予定日
		initDto.setParkingKanoDate2(
				skfDateFormatUtils.dateFormatFromString(tNyukyoChoshoTsuchi.getParkingKanoDate2(), "yyyy年MM月dd日"));

		// 入居日変更フラグ
		initDto.setNyukyoDateFlg(tNyukyoChoshoTsuchi.getNyukyoDateFlg());
		// 駐車場使用開始日変更フラグ
		initDto.setParkingSDateFlg(tNyukyoChoshoTsuchi.getParkingSDateFlg());
		return;
	}

	private void mappingTaikyoTodoke(Skf2010Sc006InitDto initDto, Skf2040TTaikyoReport taikyoReport) {
		// 社員番号
		initDto.setShainNo(taikyoReport.getShainNo());
		// 氏名
		initDto.setName(taikyoReport.getName());
		// 現住所
		initDto.setAddress(taikyoReport.getAddress());

		// 退居区分
		initDto.setShatakuTaikyoKbn(taikyoReport.getShatakuTaikyoKbn());
		// 自動車保管場所返還区分
		initDto.setShatakuTaikyoKbn2(taikyoReport.getShatakuTaikyoKbn2());

		// 所属
		// 現所属：機関
		initDto.setNowAgency(taikyoReport.getAgency());
		// 現所属：部等
		initDto.setNowAffiliation1(taikyoReport.getAffiliation1());
		// 現所属：室、チーム又は課
		initDto.setNowAffiliation2(taikyoReport.getAffiliation2());

		// 社宅情報
		// 社宅名
		if ("1".equals(taikyoReport.getTaikyoShataku())) {
			initDto.setTaikyoArea(taikyoReport.getTaikyoArea());
		}

		// 自動車１台目
		// 自動車の保管場所
		if ("1".equals(taikyoReport.getTaikyoParking1())) {
			initDto.setParkingAddress1(taikyoReport.getParkingAddress1());
		}
		// 自動車２台目
		// 自動車の保管場所
		if ("1".equals(taikyoReport.getTaikyoParking2())) {
			initDto.setParkingAddress2(taikyoReport.getParkingAddress2());
		}

		// 退居日変更フラグ
		initDto.setTaikyoDateFlg(taikyoReport.getTaikyoDateFlg());
		// 退居日
		if ("1".equals(taikyoReport.getTaikyoShataku())) {
			String taikyoDate = taikyoReport.getTaikyoDate();
			String taikyoDateText = skfDateFormatUtils.dateFormatFromString(taikyoDate, "yyyy年MM月dd日");
			initDto.setTaikyoDate(taikyoDateText);
		}
		// 駐車場返還日
		if ("1".equals(taikyoReport.getTaikyoParking1()) || "1".equals(taikyoReport.getTaikyoParking2())) {
			String parkingHenkanDate = taikyoReport.getParkingHenkanDate();
			String parkingHenkanDateText = skfDateFormatUtils.dateFormatFromString(parkingHenkanDate, "yyyy年MM月dd日");
			initDto.setParkingHenkanDate(parkingHenkanDateText);
		}

		// 退居理由
		initDto.setTaikyoRiyu(taikyoReport.getTaikyoRiyu());
		// 退居後の連絡先
		initDto.setTaikyogoRenrakusaki(taikyoReport.getTaikyogoRenrakusaki());

	}

}
