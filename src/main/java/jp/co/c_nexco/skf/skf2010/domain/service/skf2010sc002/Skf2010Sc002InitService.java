/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc002;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc002.Skf2010Sc002GetApplHistoryInfoByParameterExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBatchUtils.SkfBatchUtilsGetMultipleTablesUpdateDateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchi;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReport;
import jp.co.c_nexco.nfw.common.entity.base.BaseCodeEntity;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.core.constants.CommonConstant;
import jp.co.c_nexco.nfw.webcore.app.BaseForm;
import jp.co.c_nexco.nfw.webcore.app.FormHelper;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfCommentUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.batch.SkfBatchUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc002.Skf2010Sc002InitDto;

/**
 * Skf2010Sc002 申請書類確認画面の初期表示処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc002InitService extends BaseServiceAbstract<Skf2010Sc002InitDto> {

	@Autowired
	private Skf2010Sc002SharedService skf2010Sc002SharedService;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfCommentUtils skfCommentUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfBatchUtils skfBatchUtils;

	private String sTrue = "true";
	private String sFalse = "false";
	private static final String PLAN_TO_BUY_CAR = "購入を予定している";
	private static final String YOTEI_DATE = "(予定)";

	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2010Sc002InitDto index(Skf2010Sc002InitDto initDto) throws Exception {

		// タイトル設定
		initDto.setPageTitleKey(MessageIdConstant.SKF2010_SC002_TITLE);

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, initDto.getPageId());
		// セッション情報引き渡し
		skf2010Sc002SharedService.setMenuScopeSessionBean(menuScopeSessionBean);
		// セッション情報初期化
		skf2010Sc002SharedService.clearMenuScopeSessionBean();

		// 前画面IDの取得
		BaseForm beforeForm = FormHelper.getFormBean(initDto.getPageId(), CommonConstant.C_PAGEMODE_STANDARD);
		String prePageId = beforeForm.getPrePageId();
		initDto.setPrePageId(prePageId);

		// 申請書類履歴情報を取得取得
		Skf2010Sc002GetApplHistoryInfoByParameterExp tApplHistoryData = new Skf2010Sc002GetApplHistoryInfoByParameterExp();
		tApplHistoryData = skf2010Sc002SharedService.getApplHistoryInfoByParameter(initDto.getApplNo());
		if (tApplHistoryData != null) {
			// 申請書類種別IDの設定
			initDto.setApplId(tApplHistoryData.getApplId());

			// 排他制御用の申請書類履歴の最終更新日付保持
			initDto.addLastUpdateDate(Skf2010Sc002SharedService.KEY_LAST_UPDATE_DATE_HISTORY,
					tApplHistoryData.getUpdateDate());
		} else {
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1078, "初期表示中に");
			return initDto;
		}

		// 申請状況の設定
		initDto.setApplStatusText(changeApplStatusText(initDto.getApplStatus()));

		// 帳票イメージの初期表示指定
		Map<String, Object> displayLevelMap = new HashMap<String, Object>();
		displayLevelMap = checkDisplayLevel(initDto.getPrePageId());
		initDto.setLevel1(displayLevelMap.get("level1").toString());
		initDto.setLevel2(displayLevelMap.get("level2").toString());
		initDto.setLevel3(displayLevelMap.get("level3").toString());
		initDto.setLevel1Open(displayLevelMap.get("level1Open").toString());
		initDto.setLevel2Open(displayLevelMap.get("level2Open").toString());
		initDto.setLevel3Open(displayLevelMap.get("level3Open").toString());
		initDto.setMaskPattern(displayLevelMap.get("mask").toString());
		initDto.setCommentDisplayLevel(displayLevelMap.get("commentDisplayLevel").toString());

		// 画面内容の設定
		setDisplayData(initDto);

		// 添付資料情報を取得
		skf2010Sc002SharedService.setAttachedFileList(initDto.getShainNo(), initDto.getApplNo(), initDto);

		// コメント表示ボタンの表示非表示を設定
		String commetFlg = setCommentBtnRemove(initDto.getApplNo());
		if (NfwStringUtils.isNotEmpty(commetFlg)) {
			initDto.setCommentViewFlag(commetFlg);
		}

		// データ連携用の排他制御用更新日を取得
		Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> dateLinkageMap = skfBatchUtils
				.getUpdateDateForUpdateSQL(initDto.getShainNo());
		menuScopeSessionBean.put(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2010SC002, dateLinkageMap);

		return initDto;
	}

	/**
	 * コメント表示ボタンの表示非表示を設定
	 * 
	 * @param initDto
	 */
	private String setCommentBtnRemove(String applNo) {

		List<SkfCommentUtilsGetCommentInfoExp> commentList = new ArrayList<SkfCommentUtilsGetCommentInfoExp>();
		commentList = skfCommentUtils.getCommentInfo(CodeConstant.C001, applNo, null);
		String commentViewFlag = CodeConstant.NONE;
		if (commentList == null || commentList.size() <= 0) {
			// コメントが無ければ非表示
			commentViewFlag = sFalse;
		} else {
			// コメントがあれば表示
			commentViewFlag = sTrue;
		}
		return commentViewFlag;

	}

	/**
	 * 画面内容の設定
	 * 
	 * @param initDto
	 */
	private void setDisplayData(Skf2010Sc002InitDto initDto) {

		switch (initDto.getPrePageId()) {
		case FunctionIdConstant.SKF2020_SC002:
		case FunctionIdConstant.SKF2020_SC003:
			// 入居希望申請情報の取得
			Skf2020TNyukyoChoshoTsuchi tNyukyoChoshoTsuchi = new Skf2020TNyukyoChoshoTsuchi();
			tNyukyoChoshoTsuchi = skf2010Sc002SharedService.getNyukyoChoshoTsuchiInfo(initDto.getApplNo());
			if (tNyukyoChoshoTsuchi != null) {
				// 社宅入居希望等調書の項目設定
				mappingNyukyoChoshoTsuchi(initDto, tNyukyoChoshoTsuchi);

				if (FunctionIdConstant.SKF2020_SC003.equals(initDto.getPrePageId())) {
					// 前の画面が入居希望（アウトソースの場合は、貸与（予定）社宅等のご案内表示設定
					mappingTaiyoShatakuAnnai(initDto, tNyukyoChoshoTsuchi);
				}
			}
			break;
		case FunctionIdConstant.SKF2040_SC001:
			// 退居（自動車の保管場所返還）届情報の取得
			Skf2040TTaikyoReport tTaikyoReport = new Skf2040TTaikyoReport();
			tTaikyoReport = skf2010Sc002SharedService.getTaikyoReportInfo(initDto.getApplNo());
			if (tTaikyoReport != null) {
				// 退居（自動車の保管場所返還）届の項目設定
				mappingTaikyoReport(initDto, tTaikyoReport);
			}
			break;
		default:
			break;
		}

	}

	/**
	 * 申請状況の情報取得
	 * 
	 * @param applStatus
	 * @return
	 */
	private String changeApplStatusText(String applStatus) {

		String applStatusText = "";

		Map<String, BaseCodeEntity> applStatusMap = codeCacheUtils
				.getGenericCode(FunctionIdConstant.GENERIC_CODE_STATUS);

		// 申請状況をコードから汎用コードに変更
		if (applStatus != null) {
			BaseCodeEntity baseCodeEntity = new BaseCodeEntity();
			baseCodeEntity = applStatusMap.get(applStatus);
			applStatusText = baseCodeEntity.getCodeName();
		}

		return applStatusText;
	}

	/**
	 * 帳票イメージの初期表示設定
	 * 
	 * @param applStatus
	 * @return
	 */
	private Map<String, Object> checkDisplayLevel(String prePageId) {

		Map<String, Object> result = new HashMap<String, Object>();
		switch (prePageId) {
		case FunctionIdConstant.SKF2020_SC002:
			// 社宅入居希望等調書（申請者用）
			result.put("level1", sTrue); // 入居希望等調書
			result.put("level2", sFalse); // 貸与社宅などのご案内
			result.put("level3", sFalse);// 退居届
			result.put("mask", "shinsei"); // 申請ボタン表示
			result.put("level1Open", sTrue); // 入居希望等調書のアコーディオン初期表示
			result.put("level2Open", sFalse);// 貸与社宅などのご案内のアコーディオン初期表示
			result.put("level3Open", sFalse);// 退居届のアコーディオン初期表示
			result.put("commentDisplayLevel", CodeConstant.COMMENT_DISPLAY_LEVEL_1); // 申請者から承認者へ
			break;
		case FunctionIdConstant.SKF2020_SC003:
			// 社宅入居希望等調書（アウトソース用）
			result.put("level1", sTrue); // 入居希望等調書
			result.put("level2", sTrue); // 貸与社宅などのご案内
			result.put("level3", sFalse);// 退居届
			result.put("mask", "teiji"); // 提示ボタン表示
			result.put("level1Open", sFalse); // 入居希望等調書のアコーディオン初期表示
			result.put("level2Open", sTrue);// 貸与社宅などのご案内のアコーディオン初期表示
			result.put("level3Open", sFalse);// 退居届のアコーディオン初期表示
			result.put("commentDisplayLevel", CodeConstant.COMMENT_DISPLAY_LEVEL_2);// 承認者から申請者へ
			break;
		case FunctionIdConstant.SKF2040_SC001:
			// 退居（自動車の保管場所返還）届
			result.put("level1", sFalse); // 入居希望等調書
			result.put("level2", sFalse); // 貸与社宅などのご案内
			result.put("level3", sTrue);// 退居届
			result.put("mask", "shinsei"); // 申請ボタン表示
			result.put("level1Open", sFalse); // 入居希望等調書のアコーディオン初期表示
			result.put("level2Open", sFalse);// 貸与社宅などのご案内のアコーディオン初期表示
			result.put("level3Open", sTrue);// 退居届のアコーディオン初期表示
			result.put("commentDisplayLevel", CodeConstant.COMMENT_DISPLAY_LEVEL_1);// 申請者から承認者へ
			break;

		default:
			// 上記画面以外は何も表示しない
			result.put("level1", sFalse); // 入居希望等調書
			result.put("level2", sFalse); // 貸与社宅などのご案内
			result.put("level3", sFalse);// 退居届
			result.put("mask", "none"); // ボタン非表示
			result.put("level1Open", sFalse); // 入居希望等調書のアコーディオン初期表示
			result.put("level2Open", sFalse);// 貸与社宅などのご案内のアコーディオン初期表示
			result.put("level3Open", sFalse);// 退居届のアコーディオン初期表示
			result.put("commentDisplayLevel", CodeConstant.NONE);
			break;

		}
		return result;
	}

	/**
	 * 入居希望等調書の表示データのマッピングを行います
	 * 
	 * @param initDto
	 * @param tNyukyoChoshoTsuchi
	 */
	private void mappingNyukyoChoshoTsuchi(Skf2010Sc002InitDto initDto,
			Skf2020TNyukyoChoshoTsuchi tNyukyoChoshoTsuchi) {

		// 入居希望申請調書
		// 社宅必要可否
		LogUtils.debugByMsg("入居希望等調書の表示データ  社宅必要可否:" + tNyukyoChoshoTsuchi.getTaiyoHitsuyo());
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getTaiyoHitsuyo())) {
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
		} else {
			initDto.setTaiyoHitsuyoTrue(CodeConstant.UNCHECKED);
			initDto.setTaiyoHitsuyoFalse(CodeConstant.UNCHECKED);
			initDto.setTaiyoHitsuyoParking(CodeConstant.UNCHECKED);
		}

		// 社宅必要理由
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getHitsuyoRiyu())) {
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
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getFuhitsuyoRiyu())) {
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
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getShainNo())) {
			initDto.setShainNo(tNyukyoChoshoTsuchi.getShainNo());
		}
		// 氏名
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getName())) {
			initDto.setName(tNyukyoChoshoTsuchi.getName());
		}
		// 等級
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getTokyu())) {
			initDto.setTokyu(tNyukyoChoshoTsuchi.getTokyu());
		}
		// 所属
		// 現所属：機関
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getAgency())) {
			initDto.setNowAgency(tNyukyoChoshoTsuchi.getAgency());
		}
		// 現所属：部等
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getAffiliation1())) {
			initDto.setNowAffiliation1(tNyukyoChoshoTsuchi.getAffiliation1());
		}
		// 現所属：室、チーム又は課
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getAffiliation2())) {
			initDto.setNowAffiliation2(tNyukyoChoshoTsuchi.getAffiliation2());
		}
		// 現所属：勤務先のTEL
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getTel())) {
			initDto.setNowTel(tNyukyoChoshoTsuchi.getTel());
		}
		// 新所属：機関
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getNewAgency())) {
			initDto.setNewAgency(tNyukyoChoshoTsuchi.getNewAgency());
		}
		// 新所属：部等
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getNewAffiliation1())) {
			initDto.setNewAffiliation1(tNyukyoChoshoTsuchi.getNewAffiliation1());
		}
		// 新所属：室、チーム又は課
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getNewAffiliation2())) {
			initDto.setNewAffiliation2(tNyukyoChoshoTsuchi.getNewAffiliation2());
		}
		// 必要とする社宅
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getHitsuyoShataku())) {
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

		// 家族1
		initDto.setDokyoRelation1(tNyukyoChoshoTsuchi.getDokyoRelation1());
		initDto.setDokyoName1(tNyukyoChoshoTsuchi.getDokyoName1());
		initDto.setDokyoAge1(tNyukyoChoshoTsuchi.getDokyoAge1());

		// 家族2
		initDto.setDokyoRelation2(tNyukyoChoshoTsuchi.getDokyoRelation2());
		initDto.setDokyoName2(tNyukyoChoshoTsuchi.getDokyoName2());
		initDto.setDokyoAge2(tNyukyoChoshoTsuchi.getDokyoAge2());

		// 家族3
		initDto.setDokyoRelation3(tNyukyoChoshoTsuchi.getDokyoRelation3());
		initDto.setDokyoName3(tNyukyoChoshoTsuchi.getDokyoName3());
		initDto.setDokyoAge3(tNyukyoChoshoTsuchi.getDokyoAge3());

		// 家族4
		initDto.setDokyoRelation4(tNyukyoChoshoTsuchi.getDokyoRelation4());
		initDto.setDokyoName4(tNyukyoChoshoTsuchi.getDokyoName4());
		initDto.setDokyoAge4(tNyukyoChoshoTsuchi.getDokyoAge4());

		// 家族5
		initDto.setDokyoRelation5(tNyukyoChoshoTsuchi.getDokyoRelation5());
		initDto.setDokyoName5(tNyukyoChoshoTsuchi.getDokyoName5());
		initDto.setDokyoAge5(tNyukyoChoshoTsuchi.getDokyoAge5());

		// 家族6
		initDto.setDokyoRelation6(tNyukyoChoshoTsuchi.getDokyoRelation6());
		initDto.setDokyoName6(tNyukyoChoshoTsuchi.getDokyoName6());
		initDto.setDokyoAge6(tNyukyoChoshoTsuchi.getDokyoAge6());

		// 入居予定日
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getNyukyoYoteiDate())) {
			initDto.setNyukyoYoteiDate(skfDateFormatUtils.dateFormatFromString(tNyukyoChoshoTsuchi.getNyukyoYoteiDate(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_JP_STR));
		}

		// 保管場所
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getParkingUmu())) {
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
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getCarNoInputFlg())) {
			initDto.setCarNoInputFlg(tNyukyoChoshoTsuchi.getCarNoInputFlg());
		}
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
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getCarExpirationDate())) {
			initDto.setCarExpirationDate(skfDateFormatUtils.dateFormatFromString(
					tNyukyoChoshoTsuchi.getCarExpirationDate(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_JP_STR));
		} else {
			initDto.setCarExpirationDate(null);
		}
		// 自動車の保管場所使用開始日
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getParkingUseDate())) {
			initDto.setParkingUserDate(skfDateFormatUtils.dateFormatFromString(tNyukyoChoshoTsuchi.getParkingUseDate(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_JP_STR));
		} else {
			initDto.setCarExpirationDate(null);
		}
		// 自動車番号登録フラグ2
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getCarNoInputFlg2())) {
			initDto.setCarNoInputFlg2(tNyukyoChoshoTsuchi.getCarNoInputFlg2());
		}
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
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getCarExpirationDate2())) {
			initDto.setCarExpirationDate2(skfDateFormatUtils.dateFormatFromString(
					tNyukyoChoshoTsuchi.getCarExpirationDate2(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_JP_STR));
		} else {
			initDto.setCarExpirationDate2(null);
		}
		// 自動車の保管場所使用開始日2
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getParkingUseDate2())) {
			initDto.setParkingUserDate2(skfDateFormatUtils.dateFormatFromString(
					tNyukyoChoshoTsuchi.getParkingUseDate2(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_JP_STR));
		} else {
			initDto.setParkingUserDate2(null);
		}

		// 入居社宅
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getNowShataku())) {
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
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getNowShatakuName())) {
			initDto.setNowShatakuName(tNyukyoChoshoTsuchi.getNowShatakuName());
		}
		// 保有社宅号室
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getNowShatakuNo())) {
			initDto.setNowShatakuNo(tNyukyoChoshoTsuchi.getNowShatakuNo());
		}
		// 保有社宅規格
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getNowShatakuKikaku())) {
			initDto.setNowShatakuKikaku(tNyukyoChoshoTsuchi.getNowShatakuKikaku());
			// 規格名称取得
			String kikakuName = codeCacheUtils.getElementCodeName(FunctionIdConstant.GENERIC_CODE_KIKAKU_KBN,
					initDto.getNowShatakuKikaku());
			initDto.setNowShatakuKikakuName(kikakuName);
		}

		// 保有社宅面積
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getNowShatakuMenseki())) {
			initDto.setNowShatakuMenseki(tNyukyoChoshoTsuchi.getNowShatakuMenseki());
		}

		// 現入居社宅
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getTaikyoYotei())) {
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
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getTaikyoYoteiDate())) {
			if (CodeConstant.STATUS_DOI_ZUMI.equals(initDto.getApplStatus())
					|| CodeConstant.STATUS_SHONIN1.equals(initDto.getApplStatus())
					|| CodeConstant.STATUS_SHONIN_ZUMI.equals(initDto.getApplStatus())) {

				initDto.setTaikyoYoteiDate(skfDateFormatUtils.dateFormatFromString(
						tNyukyoChoshoTsuchi.getTaikyoYoteiDate(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_JP_STR));

			} else {
				initDto.setTaikyoYoteiDate(
						skfDateFormatUtils.dateFormatFromString(tNyukyoChoshoTsuchi.getTaikyoYoteiDate(),
								SkfCommonConstant.YMD_STYLE_YYYYMMDD_JP_STR) + YOTEI_DATE);
			}
		}

		// 特殊事情
		initDto.setTokushuJijo(tNyukyoChoshoTsuchi.getTokushuJijo());

		return;
	}

	/**
	 * 貸与（予定）社宅等のご案内表示のマッピング
	 * 
	 * @param initDto
	 * @param tNyukyoChoshoTsuchi
	 */
	private void mappingTaiyoShatakuAnnai(Skf2010Sc002InitDto initDto, Skf2020TNyukyoChoshoTsuchi tNyukyoChoshoTsuchi) {

		// 社宅を必要とする理由変換用Map
		Map<String, BaseCodeEntity> hitsuyoRiyuMap = new HashMap<String, BaseCodeEntity>();
		hitsuyoRiyuMap = codeCacheUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_SHATAKU_NEED_RIYU_KBN);

		// 規格変換用Map
		Map<String, BaseCodeEntity> shatakuKikakuMap = new HashMap<String, BaseCodeEntity>();
		shatakuKikakuMap = codeCacheUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_KIKAKU_KBN);

		// 案内日
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getTsuchiDate())) {
			initDto.setTsuchiDate(skfDateFormatUtils.dateFormatFromString(tNyukyoChoshoTsuchi.getTsuchiDate(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_JP_STR));
		}
		// 申請日
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getApplDate())) {
			initDto.setApplDate(skfDateFormatUtils.dateFormatFromString(tNyukyoChoshoTsuchi.getApplDate(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_JP_STR));
		}

		// 制約日
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getSeiyakuDate())) {
			initDto.setSeiyakuDate(skfDateFormatUtils.dateFormatFromString(tNyukyoChoshoTsuchi.getSeiyakuDate(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_JP_STR));
		}

		// 社宅を必要とする理由
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getHitsuyoRiyu())) {
			String hitsuyoRiyu = tNyukyoChoshoTsuchi.getHitsuyoRiyu();
			BaseCodeEntity bceHitsuyoRiyu = hitsuyoRiyuMap.get(hitsuyoRiyu);
			initDto.setHitsuyoRiyu(bceHitsuyoRiyu.getCodeName());
		}

		// 社宅所在地
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getNewShozaichi())) {
			initDto.setNewShozaichi(tNyukyoChoshoTsuchi.getNewShozaichi());
		}

		// 社宅名
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getNewShatakuName())) {
			initDto.setNewShatakuName(tNyukyoChoshoTsuchi.getNewShatakuName());
		}

		// 室番号
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getNewShatakuNo())) {
			initDto.setNewShatakuNo(tNyukyoChoshoTsuchi.getNewShatakuNo());
		}

		// 規格
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getNewShatakuKikaku())) {
			String shatakuKikaku = tNyukyoChoshoTsuchi.getNewShatakuKikaku();
			BaseCodeEntity bceKikaku = shatakuKikakuMap.get(shatakuKikaku);
			if (bceKikaku == null) {
				initDto.setNewShatakuKikaku(shatakuKikaku);
			} else {
				initDto.setNewShatakuKikaku(bceKikaku.getCodeName());
			}
		}

		// 面積
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getNewShatakuMenseki())) {
			initDto.setNewShatakuMenseki(tNyukyoChoshoTsuchi.getNewShatakuMenseki());
		}

		// 数値のカンマ区切り設定
		NumberFormat nfNum = NumberFormat.getNumberInstance();

		// 使用料
		String newRental = tNyukyoChoshoTsuchi.getNewRental();
		if (NfwStringUtils.isNotEmpty(newRental)) {
			newRental = nfNum.format(Long.parseLong(newRental));
			initDto.setNewRental(newRental);
		}
		// 共益費
		String newKyoekihi = tNyukyoChoshoTsuchi.getNewKyoekihi();
		if (NfwStringUtils.isNotEmpty(newKyoekihi)) {
			newKyoekihi = nfNum.format(Long.parseLong(newKyoekihi));
			initDto.setNewKyoekihi(newKyoekihi);
		}

		// 自動車１台目
		// 自動車の保管場所
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getParkingArea())) {
			initDto.setParkingArea(tNyukyoChoshoTsuchi.getParkingArea());
		}
		// 自動車の位置番号
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getCarIchiNo())) {
			initDto.setCarIchiNo(tNyukyoChoshoTsuchi.getCarIchiNo());
		}
		// 保管場所使用料
		String parkingRental = tNyukyoChoshoTsuchi.getParkingRental();
		if (NfwStringUtils.isNotEmpty(parkingRental)) {
			parkingRental = nfNum.format(Long.parseLong(parkingRental));
			initDto.setParkingRental(parkingRental);
		}

		// 自動車２台目
		// 自動車の保管場所
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getParkingArea2())) {
			initDto.setParkingArea2(tNyukyoChoshoTsuchi.getParkingArea2());
		}
		// 自動車の位置番号
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getCarIchiNo2())) {
			initDto.setCarIchiNo2(tNyukyoChoshoTsuchi.getCarIchiNo2());
		}
		// 保管場所使用料
		String parkingRental2 = tNyukyoChoshoTsuchi.getParkingRental2();
		if (NfwStringUtils.isNotEmpty(parkingRental2)) {
			parkingRental2 = nfNum.format(Long.parseLong(parkingRental2));
			initDto.setParkingRental2(parkingRental2);
		}

		// 自動車保管場所（１台目）の使用開始予定日
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getParkingKanoDate())) {
			initDto.setParkingKanoDate(skfDateFormatUtils.dateFormatFromString(tNyukyoChoshoTsuchi.getParkingKanoDate(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_JP_STR));
		}
		// 自動車保管場所（２台目）の使用開始予定日
		if (NfwStringUtils.isNotEmpty(tNyukyoChoshoTsuchi.getParkingKanoDate2())) {
			initDto.setParkingKanoDate2(skfDateFormatUtils.dateFormatFromString(
					tNyukyoChoshoTsuchi.getParkingKanoDate2(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_JP_STR));
		}

		return;
	}

	/**
	 * 退居(自動車の変換場所）届表示のマッピング
	 * 
	 * @param initDto
	 * @param tTaikyoReport
	 */
	private void mappingTaikyoReport(Skf2010Sc002InitDto initDto, Skf2040TTaikyoReport taikyoRepDt) {

		// 申請書類タイトル表記設定
		if (NfwStringUtils.isNotEmpty(taikyoRepDt.getShatakuTaikyoKbn())) {
			initDto.setShatakuTaikyoKbn(taikyoRepDt.getShatakuTaikyoKbn()); // 社宅退居
		}
		if (NfwStringUtils.isNotEmpty(taikyoRepDt.getShatakuTaikyoKbn2())) {
			initDto.setShatakuTaikyoKbn2(taikyoRepDt.getShatakuTaikyoKbn2()); // 駐車場返還
		}

		// 社員番号
		if (NfwStringUtils.isNotEmpty(taikyoRepDt.getShainNo())) {
			initDto.setShainNo(taikyoRepDt.getShainNo());
		}
		// 申請書類管理番号
		if (NfwStringUtils.isNotEmpty(taikyoRepDt.getApplNo())) {
			initDto.setApplNo(taikyoRepDt.getApplNo());
		}
		// 申請年月日
		String applDate = taikyoRepDt.getApplDate();
		if (NfwStringUtils.isNotEmpty(applDate)) {
			String applDateText = skfDateFormatUtils.dateFormatFromString(applDate,
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_JP_STR);
			initDto.setApplDate(applDateText);
		}
		// 機関
		if (NfwStringUtils.isNotEmpty(taikyoRepDt.getAgency())) {
			initDto.setNowAgency(taikyoRepDt.getAgency());
		}
		// 部等
		if (NfwStringUtils.isNotEmpty(taikyoRepDt.getAffiliation1())) {
			initDto.setNowAffiliation1(taikyoRepDt.getAffiliation1());
		}
		// 室、チーム又は課
		if (NfwStringUtils.isNotEmpty(taikyoRepDt.getAffiliation2())) {
			initDto.setNowAffiliation2(taikyoRepDt.getAffiliation2());
		}
		// 現住所
		if (NfwStringUtils.isNotEmpty(taikyoRepDt.getAddress())) {
			initDto.setAddress(taikyoRepDt.getAddress());
		}
		// 氏名
		if (NfwStringUtils.isNotEmpty(taikyoRepDt.getName())) {
			initDto.setName(taikyoRepDt.getName());
		}

		// 社宅
		if (NfwStringUtils.isNotEmpty(taikyoRepDt.getTaikyoArea())) {
			initDto.setTaikyoArea(taikyoRepDt.getTaikyoArea());
		}
		// 駐車場1
		if (NfwStringUtils.isNotEmpty(taikyoRepDt.getParkingAddress1())) {
			initDto.setParkingAddress1(taikyoRepDt.getParkingAddress1());
		}
		// 駐車場2
		if (NfwStringUtils.isNotEmpty(taikyoRepDt.getParkingAddress2())) {
			initDto.setParkingAddress2(taikyoRepDt.getParkingAddress2());
		}
		// 退居日 社宅等
		// 退居日
		if ((NfwStringUtils.isNotEmpty(taikyoRepDt.getTaikyoDate()))) {
			// 退居日 年月日形式で設定、日付変更フラグを0にする
			initDto.setTaikyoDate(skfDateFormatUtils.dateFormatFromString(taikyoRepDt.getTaikyoDate(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_JP_STR));
			initDto.setTaikyoDateFlg(SkfCommonConstant.NOT_CHANGE);

			// 日付変更フラグが1:変更ありなら赤文字にする
			if (NfwStringUtils.isNotEmpty(taikyoRepDt.getTaikyoDateFlg())
					&& SkfCommonConstant.DATE_CHANGE.equals(taikyoRepDt.getTaikyoDateFlg())) {
				initDto.setTaikyoDateFlg(taikyoRepDt.getTaikyoDateFlg());
			}
		} else {
			initDto.setTaikyoDateFlg(SkfCommonConstant.NOT_CHANGE);
		}

		// 駐車場返還日
		if ((NfwStringUtils.isNotEmpty(taikyoRepDt.getParkingHenkanDate()))) {

			// 駐車場返還日 年月日形式で設定、日付変更フラグを0にする
			initDto.setParkingHenkanDate(skfDateFormatUtils.dateFormatFromString(taikyoRepDt.getParkingHenkanDate(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_JP_STR));
			initDto.setParkingEDateFlg(SkfCommonConstant.NOT_CHANGE);

			// 日付変更フラグが1:変更ありなら赤文字にする
			if (NfwStringUtils.isNotEmpty(taikyoRepDt.getParkingEDateFlg())
					&& SkfCommonConstant.DATE_CHANGE.equals(taikyoRepDt.getParkingEDateFlg())) {
				initDto.setParkingEDateFlg(taikyoRepDt.getParkingEDateFlg());
			}
		} else {
			// 駐車場返還日がない場合は、退居日を設定
			initDto.setParkingHenkanDate(skfDateFormatUtils.dateFormatFromString(taikyoRepDt.getTaikyoDate(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_JP_STR));
			initDto.setParkingEDateFlg(SkfCommonConstant.NOT_CHANGE);
		}
		// 退居（返還）理由
		Map<String, String> taikyoRiyuMap = skfGenericCodeUtils
				.getGenericCode(FunctionIdConstant.GENERIC_CODE_TAIKYO_HENKAN_RIYU);
		String taikyoRiyu = CodeConstant.DOUBLE_QUOTATION;
		if (taikyoRiyuMap != null) {
			taikyoRiyu = taikyoRiyuMap.get(taikyoRepDt.getTaikyoRiyuKbn());
			initDto.setTaikyoRiyu(taikyoRiyu);
		}
		// 退居後の連絡先
		if (NfwStringUtils.isNotEmpty(taikyoRepDt.getTaikyogoRenrakusaki())) {
			initDto.setTaikyogoRenrakusaki(taikyoRepDt.getTaikyogoRenrakusaki());
		}

		return;

	}

}
