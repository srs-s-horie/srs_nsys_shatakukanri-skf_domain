/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc004;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetApplHistoryInfoByParameterExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetCommentListExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetShatakuInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchi;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReport;
import jp.co.c_nexco.nfw.common.entity.base.BaseCodeEntity;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LoginUserInfoUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationGuideUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc004.Skf2010Sc004InitDto;

/**
 * Skf2010Sc004 申請内容表示/引戻し初期表示処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc004InitService extends BaseServiceAbstract<Skf2010Sc004InitDto> {

	@Autowired
	private Skf2010Sc004SharedService skf2010Sc004SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfOperationGuideUtils skfOperationGuideUtils;

	private String companyCd = CodeConstant.C001;

	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2010Sc004InitDto index(Skf2010Sc004InitDto initDto) throws Exception {
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示処理開始", companyCd, FunctionIdConstant.SKF2010_SC004);

		initDto.setPageTitleKey(MessageIdConstant.SKF2010_SC004_TITLE);

		String applId = initDto.getApplId();
		String applStatus = initDto.getApplStatus();

		checkDisplayLevel(applId, applStatus, initDto);

		// 表示情報セット
		setDisplayData(initDto);

		// 申請状況のテキスト化
		initDto.setApplStatusText(changeApplStatusText(applStatus));

		// コメントボタンの活性非活性処理
		setCommentBtnDisabled(initDto);

		return initDto;
	}

	/**
	 * コメント表示ボタンの表示非表示を設定します
	 * 
	 * @param initDto
	 */
	private void setCommentBtnDisabled(Skf2010Sc004InitDto initDto) {
		List<Skf2010Sc004GetCommentListExp> commentList = new ArrayList<Skf2010Sc004GetCommentListExp>();
		String applStatus = "";
		// 権限チェック
		Set<String> roleIds = LoginUserInfoUtils.getRoleIds();
		if (roleIds == null) {
			return;
		}
		// 一般ユーザーかチェック
		boolean isAdmin = false;
		for (String roleId : roleIds) {
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
		}
		// 一般ユーザーの場合、申請状況に「承認１」をセット
		if (!isAdmin) {
			applStatus = CodeConstant.STATUS_SHONIN1;
		}

		commentList = skf2010Sc004SharedService.getApplCommentList(initDto.getApplNo(), applStatus);
		if (commentList == null || commentList.size() <= 0) {
			// コメントが無ければ非表示
			initDto.setCommentViewFlag("false");
		} else {
			// コメントがあれば表示
			initDto.setCommentViewFlag("true");
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
		}
		return applStatusText;
	}

	/**
	 * 画面表示設定
	 * 
	 * @param applId
	 * @param applStatus
	 * @param dto
	 */
	private void checkDisplayLevel(String applId, String applStatus, Skf2010Sc004InitDto dto) {
		/**
		 * displayLevel : 項目表示レベル アコーディオン項目をどこまで表示するかをこれで指定する。
		 */
		int displayLevel = 1;

		if (applId.equals(FunctionIdConstant.R0100)) {
			// 社宅入居希望等申請
			switch (applStatus) {
			case CodeConstant.STATUS_SHINSEICHU:
			case CodeConstant.STATUS_SHINSACHU:
				/**
				 * 申請中、審査中： 社宅入居希望等調書のみ表示
				 */
				dto.setMaskPattern("PTN_A");
				dto.setLevel1Open("true");
				dto.setRepresentBtnFlg("false");
				break;
			case CodeConstant.STATUS_KAKUNIN_IRAI:
				/**
				 * 確認依頼： 案内＋誓約書追加
				 */
				dto.setMaskPattern("PTN_B");
				displayLevel = 2;
				dto.setLevel2Open("true");
				break;
			case CodeConstant.STATUS_DOI_ZUMI:
			case CodeConstant.STATUS_DOI_SHINAI:
				/**
				 * 同意済、同意しない： 案内＋誓約書追加（ボタンは全て非表示）
				 */
				dto.setMaskPattern("PTN_C");
				displayLevel = 2;
				dto.setLevel2Open("true");
				dto.setRepresentBtnFlg("false");
				break;
			default:
				/**
				 * それ以外（同意済み以降）： 入居決定通知書追加
				 */
				dto.setMaskPattern("PTN_C");
				displayLevel = 3;
				dto.setLevel3Open("true");
				dto.setRepresentBtnFlg("false");
				break;
			}
		} else if (applId.equals(FunctionIdConstant.R0103)) {
			// 退居（自動車の保管場所変換）届
			displayLevel = 4;
			dto.setLevel4Open("true");
			dto.setRepresentBtnFlg("false");
			switch (applStatus) {
			case CodeConstant.STATUS_SHINSEICHU:
				// 申請中、審査中
				dto.setMaskPattern("PTN_A");
				break;
			case CodeConstant.STATUS_SHONIN_ZUMI:
				// 承認済み
				dto.setMaskPattern("PTN_C");
				break;
			}

		}

		dto.setDisplayLevel(displayLevel);
		return;
	}

	private void setDisplayData(Skf2010Sc004InitDto initDto) {
		String applNo = initDto.getApplNo();
		String applId = initDto.getApplId();

		if (applId.equals(FunctionIdConstant.R0100)) {

			Skf2020TNyukyoChoshoTsuchi tNyukyoChoshoTsuchi = new Skf2020TNyukyoChoshoTsuchi();
			tNyukyoChoshoTsuchi = skf2010Sc004SharedService.getNyukyoChoshoTsuchiInfo(applNo);
			if (tNyukyoChoshoTsuchi != null) {
				// 更新用
				String applDate = skfDateFormatUtils.dateFormatFromString(tNyukyoChoshoTsuchi.getApplDate(),
						"yyyy/MM/dd HH:mm:ss");
				initDto.setApplUpdateDate(applDate);
				// 社宅入居希望等調書
				mappingNyukyoChoshoTsuchi(initDto, tNyukyoChoshoTsuchi);
				// 貸与（予定）社宅等のご案内
				mappingTaiyoShatakuAnnai(initDto, tNyukyoChoshoTsuchi);
				// 備品希望
				initDto.setBihinKibo(tNyukyoChoshoTsuchi.getBihinKibo());
			}
		} else if (applId.equals(FunctionIdConstant.R0103)) {
			// 退居（自動車の保管場所返還）届

			// ログインユーザー情報取得
			Map<String, String> loginUserInfo = skfLoginUserInfoUtils.getSkfLoginUserInfo();
			String shainNo = loginUserInfo.get("shainNo");
			// 退居（自動車の保管場所返還）届情報取得
			Skf2040TTaikyoReport tTaikyoReport = new Skf2040TTaikyoReport();
			tTaikyoReport = skf2010Sc004SharedService.getTaikkyoReportInfo(applNo);
			if (tTaikyoReport != null && tTaikyoReport.getShatakuNo() != null) {
				Skf2010Sc004GetShatakuInfoExp shatakuInfo = new Skf2010Sc004GetShatakuInfoExp();
				shatakuInfo = skf2010Sc004SharedService.getShatakuInfo(tTaikyoReport.getShatakuNo(), shainNo);

				if (shatakuInfo == null) {
					ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_2004);
				} else {
					mappingTaikyoReport(initDto, tTaikyoReport, shatakuInfo);
				}
			} else {
				ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_2004);
				// throwBusinessExceptionIfErrors(initDto.getResultMessages());
			}

		}

		// 申請書類種別IDを取得
		Skf2010Sc004GetApplHistoryInfoByParameterExp tApplHistoryData = new Skf2010Sc004GetApplHistoryInfoByParameterExp();
		tApplHistoryData = skf2010Sc004SharedService.getApplHistoryInfo(applNo);
		if (tApplHistoryData != null) {
			initDto.setApplId(tApplHistoryData.getApplId());
			initDto.setApplStatus(tApplHistoryData.getApplStatus());
			// initDto.setShonin1Name(tApplHistoryData.getAgreName1());
		}

		// 添付資料情報取得
		List<Map<String, Object>> attachedFileList = new ArrayList<Map<String, Object>>();
		attachedFileList = skf2010Sc004SharedService.getAttachedFileInfo(applNo);
		initDto.setAttachedFileList(attachedFileList);

		// 操作ガイド取得
		Map<String, String> operationGuideMap = new HashMap<String, String>();
		operationGuideMap = skfOperationGuideUtils.getOperationGuideMap(FunctionIdConstant.SKF2010_SC004);

		String operationGuide = CodeConstant.NONE;
		if (operationGuideMap != null) {
			switch (initDto.getApplStatus()) {
			case CodeConstant.STATUS_SHINSEICHU:
			case CodeConstant.STATUS_SHINSACHU:
				operationGuide = operationGuideMap.get("01");
				break;
			case CodeConstant.STATUS_KAKUNIN_IRAI:
				operationGuide = operationGuideMap.get("02");
				break;
			}
		}
		initDto.setOperationGuide(operationGuide);

	}

	/**
	 * 入居希望等調書の表示データのマッピングを行います
	 * 
	 * @param initDto
	 * @param tNyukyoChoshoTsuchi
	 */
	private void mappingNyukyoChoshoTsuchi(Skf2010Sc004InitDto initDto,
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
		initDto.setCarName(tNyukyoChoshoTsuchi.getCarName());
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
		initDto.setCarName2(tNyukyoChoshoTsuchi.getCarName2());
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
		initDto.setNowShatakuKikaku(tNyukyoChoshoTsuchi.getNowShatakuKikaku());
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
	private void mappingTaiyoShatakuAnnai(Skf2010Sc004InitDto initDto, Skf2020TNyukyoChoshoTsuchi tNyukyoChoshoTsuchi) {
		Map<String, BaseCodeEntity> hitsuyoRiyuMap = new HashMap<String, BaseCodeEntity>();
		hitsuyoRiyuMap = codeCacheUtils.getGenericCode("SKF1006");

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
		BaseCodeEntity bce = hitsuyoRiyuMap.get(hitsuyoRiyu);
		initDto.setHitsuyoRiyu(bce.getCodeName());

		// 社宅所在地
		initDto.setNewShozaichi(tNyukyoChoshoTsuchi.getNewShozaichi());

		// 社宅名
		initDto.setNewShatakuName(tNyukyoChoshoTsuchi.getNewShatakuName());

		// 室番号
		initDto.setNewShatakuNo(tNyukyoChoshoTsuchi.getNewShatakuNo());

		// 規格
		initDto.setNewShatakuKikaku(tNyukyoChoshoTsuchi.getNewShatakuKikaku());

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
		if (newKyoekihi != null && NfwStringUtils.isNotEmpty(newKyoekihi)) {
			newKyoekihi = nfNum.format(Long.parseLong(newKyoekihi));
		}
		initDto.setNewKyoekihi(newKyoekihi);

		// 自動車１台目
		// 自動車の保管場所
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
		// 自動車の保管場所
		initDto.setParkingArea2(tNyukyoChoshoTsuchi.getParkingArea2());
		// 自動車の位置番号
		initDto.setCarIchiNo(tNyukyoChoshoTsuchi.getCarIchiNo2());
		// 保管場所使用料
		String parkingRental2 = tNyukyoChoshoTsuchi.getParkingRental2();
		if (parkingRental2 != null && NfwStringUtils.isNotEmpty(parkingRental2)) {
			parkingRental2 = nfNum.format(Long.parseLong(parkingRental2));
		}
		initDto.setParkingRental2(parkingRental2);

		// 自動車保管場所（１台目）の使用開始予定日
		initDto.setParkingKanoDate(
				skfDateFormatUtils.dateFormatFromString(tNyukyoChoshoTsuchi.getParkingKanoDate(), "yyyy年MM月dd日"));
		// 自動車保管場所（２台目）の使用開始予定日
		initDto.setParkingKanoDate2(
				skfDateFormatUtils.dateFormatFromString(tNyukyoChoshoTsuchi.getParkingKanoDate2(), "yyyy年MM月dd日"));
		return;
	}

	private void mappingTaikyoReport(Skf2010Sc004InitDto initDto, Skf2040TTaikyoReport tTaikyoReport,
			Skf2010Sc004GetShatakuInfoExp shatakuInfo) {
		// 申請書類タイトル表記設定
		initDto.setShatakuTaikyoKbn(tTaikyoReport.getShatakuTaikyoKbn()); // 社宅退居
		initDto.setShatakuTaikyoKbn2(tTaikyoReport.getShatakuTaikyoKbn2()); // 駐車場返還

		// 申請書類管理番号
		initDto.setApplNo(tTaikyoReport.getApplNo());
		// 申請年月日
		String applDate = tTaikyoReport.getApplDate();
		String applDateText = skfDateFormatUtils.dateFormatFromString(applDate, "yyyy年MM月dd日");
		initDto.setApplDate(applDateText);
		// 機関
		initDto.setNowAgency(tTaikyoReport.getAgency());
		// 部等
		initDto.setNowAffiliation1(tTaikyoReport.getAffiliation1());
		// 室、チーム又は課
		initDto.setNowAffiliation2(tTaikyoReport.getAffiliation2());
		// 現住所
		initDto.setAddress(shatakuInfo.getAddress());
		// 氏名
		initDto.setName(tTaikyoReport.getName());
		tTaikyoReport.getShatakuTaikyoKbn();

		// 自動車の保管場所返還取消線フラグ
		// 社宅
		initDto.setShatakuName(shatakuInfo.getShatakuName());
		// 駐車場1
		initDto.setParkingAddress1(shatakuInfo.getParkingAddress1());
		// 駐車場2
		initDto.setParkingAddress2(shatakuInfo.getParkingAddress2());
		// 退居日 社宅等
		String taikyoDate = tTaikyoReport.getTaikyoDate();
		String taikyoDateText = skfDateFormatUtils.dateFormatFromString(taikyoDate, "yyyy年MM月dd日");
		initDto.setTaikyoDate(taikyoDateText);
		// 退居日 駐車場
		String parkingHenkanDate = tTaikyoReport.getParkingHenkanDate();
		String parkingHenkanDateText = skfDateFormatUtils.dateFormatFromString(parkingHenkanDate, "yyyy年MM月dd日");
		initDto.setParkingHenkanDate(parkingHenkanDateText);
		// 退居（返還）理由
		Map<String, String> taikyoRiyuMap = skfGenericCodeUtils.getGenericCode("SKF1142");
		String taikyoRiyu = "";
		if (taikyoRiyuMap != null) {
			taikyoRiyu = taikyoRiyuMap.get(tTaikyoReport.getTaikyoRiyuKbn());
		}
		initDto.setTaikyoRiyu(taikyoRiyu);
		// 退居後の連絡先
		initDto.setTaikyogoRenrakusaki(tTaikyoReport.getTaikyogoRenrakusaki());

	}

}
