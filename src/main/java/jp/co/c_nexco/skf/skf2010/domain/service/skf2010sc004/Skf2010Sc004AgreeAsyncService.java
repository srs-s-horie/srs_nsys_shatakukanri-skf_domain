/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc004;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetApplHistoryInfoByParameterExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetShatakuKiboForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchi;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReport;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfRollBack.SkfRollBackExpRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.common.utils.PropertyUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.SkfAsyncServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfCommentUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.SkfShinseiUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.SkfBatchBusinessLogicUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc004.Skf2010Sc004AgreeAsyncDto;

/**
 * Skf2010Sc004 申請内容表示/引戻し同意する非同期処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc004AgreeAsyncService extends SkfAsyncServiceAbstract<Skf2010Sc004AgreeAsyncDto> {

	@Autowired
	private Skf2010Sc004SharedService skf2010Sc004SharedService;

	@Autowired
	private SkfShinseiUtils skfShinseiUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfBatchBusinessLogicUtils skfBatchBusinessLogicUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfRollBackExpRepository skfRollBackExpRepository;
	@Autowired
	private SkfCommentUtils skfCommentUtils;

	@Value("${skf.common.validate_error}")
	private String validationErrorCode;

	// 基準会社コード
	private String companyCd = CodeConstant.C001;

	private final String BIHIN_HENKYAKU_ADD_STRING = "備品返却申請テーブルの登録時に";

	/**
	 * サービス処理を行う。
	 * 
	 * @param agreeDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public AsyncBaseDto index(Skf2010Sc004AgreeAsyncDto agreeDto) throws Exception {
		// 操作ログの出力
		skfOperationLogUtils.setAccessLog("同意する", companyCd, FunctionIdConstant.SKF2010_SC004);

		// 初期化処理
		init(agreeDto);

		// 申請書類ID
		String applId = agreeDto.getApplId();
		// 申請書番号
		String applNo = agreeDto.getApplNo();
		// エラーメッセージ保持用
		Map<String, String> errorMsg = new HashMap<String, String>();

		// 入力チェック
		boolean validateResult = true;
			
		//日付入力欄の必須入力チェック
		validateResult = inputValidate(agreeDto);
		if(!validateResult){
			throwBusinessExceptionIfErrors(agreeDto.getResultMessages());
			return agreeDto;
		}
		
		// 承認者へのコメント欄の入力チェック
		validateResult = validateReason(agreeDto);
		if (!validateResult) {
			throwBusinessExceptionIfErrors(agreeDto.getResultMessages());
			return agreeDto;
		}

		// 申請情報の取得を行う
		getApplInfo(agreeDto);

		// 「現社宅の退居日」
		String taikyobi = agreeDto.getTaikyobi();

		// 退居日が変更されていた場合、退居日変更フラグに1:変更ありをセットする。
		// 退居日変更フラグ(0：変更なし)
		String taikyoChangeFlag = SkfCommonConstant.NOT_CHANGE;

		// 初期退居日
		String syokiTaikyobi = agreeDto.getSyokiTaikyoDate();

		// 退居日の有無をチェック
		if (NfwStringUtils.isEmpty(syokiTaikyobi)) {
			// 退居日が無い時にテキストボックスの入力があった場合、変更フラグを1:変更ありにする。
			if (!CheckUtils.isEmpty(taikyobi)) {
				// 退居日変更フラグに1：変更ありを設定
				taikyoChangeFlag = SkfCommonConstant.DATE_CHANGE;
			}
		} else {
			if (!syokiTaikyobi.equals(taikyobi)) {
				// 退居日変更フラグに1：変更ありを設定
				taikyoChangeFlag = SkfCommonConstant.DATE_CHANGE;
			}
		}

		// 「現社宅先の駐車場使用返還日」
		String henkanbi = agreeDto.getHenkanbi();

		// 初期返還日
		String syokiHenkanbi = agreeDto.getSyokiParkingDate();
		// 返還日変更フラグ(0：変更なし)
		String henkanChangeFlag = SkfCommonConstant.NOT_CHANGE;

		// 返還日の有無をチェック
		if (NfwStringUtils.isEmpty(syokiHenkanbi)) {
			// 返還日が無い時にテキストボックスの入力があった場合、変更フラグを1:変更ありにする。
			if (NfwStringUtils.isNotEmpty(henkanbi)) {
				// 返還日変更フラグに1：変更ありを設定
				henkanChangeFlag = SkfCommonConstant.DATE_CHANGE;
			}
		} else {
			String henkanbiNoSlash = skfDateFormatUtils.dateFormatFromString(henkanbi,
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT);
			String syokiHenkanbiNoSlash = skfDateFormatUtils.dateFormatFromString(syokiHenkanbi,
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT);
			if (!CheckUtils.isEqual(henkanbiNoSlash, syokiHenkanbiNoSlash)) {
				// 返還日変更フラグに1：変更ありを設定
				henkanChangeFlag = SkfCommonConstant.DATE_CHANGE;
			}
		}

		// 「新社宅の入居日」
		String nyukyobi = agreeDto.getNyukyobi();

		// 初期入居日
		String syokiNyukyobi = agreeDto.getSyokiNyukyoDate();
		// 入居日変更フラグ(0：変更なし)
		String nyukyoChangeFlag = SkfCommonConstant.NOT_CHANGE;

		// 入居日の有無をチェック
		if (NfwStringUtils.isEmpty(syokiNyukyobi)) {
			// 入居日が無い時にテキストボックスの入力があった場合、変更フラグを1:変更ありにする。
			if (NfwStringUtils.isNotEmpty(nyukyobi)) {
				// 入居日変更フラグに1：変更ありを設定
				nyukyoChangeFlag = SkfCommonConstant.DATE_CHANGE;
			}
		} else {
			// 念のため比較対象の形式を合わせる
			String syokiNyukyobiNoSlash = skfDateFormatUtils.dateFormatFromString(syokiNyukyobi,
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT);
			String nyukyobiNoSlash = skfDateFormatUtils.dateFormatFromString(nyukyobi,
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT);
			if (!CheckUtils.isEqual(syokiNyukyobiNoSlash, nyukyobiNoSlash)) {
				// 入居日変更フラグに1：変更ありを設定
				nyukyoChangeFlag = SkfCommonConstant.DATE_CHANGE;
			}
		}

		// 「新社宅先の駐車場使用開始日」
		String shiyobi = agreeDto.getShiyobi();
		String shiyobi2 = agreeDto.getShiyobi2();

		// 初期駐車場使用開始日
		String syokiShiyobi = agreeDto.getSyokiParkingUseDate();
		String syokiShiyobi2 = agreeDto.getSyokiParkingUseDate2();

		String kaishiChangeFlg = SkfCommonConstant.NOT_CHANGE;

		// 駐車場使用開始日の有無をチェック
		// 駐車場使用開始日1,2が無い時にテキストボックスの入力があった場合、
		// (1台目,2台目)変更フラグを1:変更ありにする。
		if (!CheckUtils.isEqual(syokiShiyobi, shiyobi) && !CheckUtils.isEqual(syokiShiyobi2, shiyobi2)) {
			// 駐車場使用開始日変更フラグに3：変更ありを設定
			kaishiChangeFlg = SkfCommonConstant.DATE_CHANGE_COM;
		} else if (!CheckUtils.isEqual(syokiShiyobi, shiyobi)) {
			// 駐車場使用開始日変更フラグに1：変更あり(1台目)を設定
			kaishiChangeFlg = SkfCommonConstant.DATE_CHANGE;
		} else if (!CheckUtils.isEqual(syokiShiyobi2, shiyobi2)) {
			// 駐車場使用開始日変更フラグに2：変更あり(2台目)を設定
			kaishiChangeFlg = SkfCommonConstant.DATE_CHANGE2;
		}

		// 「現社宅の退居日」、｢現社宅先の駐車場使用返還日」のいずれかが入力されている場合のみ、
		// 退居届の申請書類番号と身上の現住所を取得する
		String newApplNo = CodeConstant.NONE;
		String nowAddress = CodeConstant.NONE;

		Map<String, String> loginUserInfo = skfLoginUserInfoUtils
				.getSkfLoginUserInfoFromAlterLogin(menuScopeSessionBean);
		String shainNo = loginUserInfo.get("shainNo");

		if (NfwStringUtils.isNotEmpty(taikyobi) || NfwStringUtils.isNotEmpty(henkanbi)) {
			newApplNo = skf2010Sc004SharedService.getApplNo(shainNo, FunctionIdConstant.R0103);

			nowAddress = skf2010Sc004SharedService.getAddressInfo(shainNo);
		}

		Date commentUpdateDate = new Date();
		String commentName = loginUserInfo.get("userName");
		String commentNote = agreeDto.getCommentNote();

		boolean updRes = updateApplHistoryAgree(newApplNo, agreeDto, shainNo, nowAddress, commentUpdateDate,
				commentName, commentNote, nyukyoChangeFlag, kaishiChangeFlg, taikyoChangeFlag, henkanChangeFlag);
		if (!updRes) {
			// エラーが出た場合処理を中断する
			throwBusinessExceptionIfErrors(agreeDto.getResultMessages());
			return agreeDto;
		}

		// 社宅管理データ連携処理実行
		Skf2010Sc004GetApplHistoryInfoByParameterExp tApplHistoryData = new Skf2010Sc004GetApplHistoryInfoByParameterExp();
		tApplHistoryData = skf2010Sc004SharedService.getApplHistoryInfo(applNo);
		if (tApplHistoryData == null) {
			ServiceHelper.addErrorResultMessage(agreeDto, null, MessageIdConstant.E_SKF_1073);
			throwBusinessExceptionIfErrors(agreeDto.getResultMessages());
			return agreeDto;
		}
		String afterApplStatus = tApplHistoryData.getApplStatus();
		List<String> resultBatch = new ArrayList<String>();
		resultBatch = skf2010Sc004SharedService.doShatakuRenkei(menuScopeSessionBean, applNo, newApplNo,
				afterApplStatus, applId, FunctionIdConstant.SKF2010_SC004, shainNo);
		menuScopeSessionBean.remove(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2010SC004);
		if (resultBatch != null) {
			skfBatchBusinessLogicUtils.addResultMessageForDataLinkage(agreeDto, resultBatch);
			skfRollBackExpRepository.rollBack();
			throwBusinessExceptionIfErrors(agreeDto.getResultMessages());
			return agreeDto;
		}

		// Hidden領域の備品希望要否
		String applNoBihinShinsei = CodeConstant.NONE;
		String bihinKibo = agreeDto.getBihinKibo();
		if (!NfwStringUtils.isEmpty(bihinKibo) && bihinKibo.equals(CodeConstant.BIHIN_KIBO_SHINSEI_HITSUYO)) {
			// 備品希望申請の申請書類履歴テーブルを作成する
			applNoBihinShinsei = skf2010Sc004SharedService.insertApplHistoryBihinKibo(applNo, applId, shainNo,
					errorMsg);
			if (applNoBihinShinsei == null) {
				// エラーの時のみNULL
				ServiceHelper.addErrorResultMessage(agreeDto, null, errorMsg.get("error"));
				throwBusinessExceptionIfErrors(agreeDto.getResultMessages());
				return agreeDto;
			}

			// 備品申請の申請書類管理番号をセットされている場合は自動遷移のダイアログ表示
			if (NfwStringUtils.isNotEmpty(applNoBihinShinsei)) {
				ServiceHelper.addResultMessage(agreeDto, MessageIdConstant.I_SKF_2047);
				// ダイアログ表示フラグをセットする
				agreeDto.setDialogFlg(true);
				agreeDto.setBihinApplNo(applNoBihinShinsei);
			}

		}
		// セッション内にある排他処理用最終更新日を削除
		menuScopeSessionBean.remove(skf2010Sc004SharedService.KEY_LAST_UPDATE_DATE_HISTORY);

		return agreeDto;
	}

	/**
	 * 申請情報を更新する
	 * 
	 * @param newApplNo
	 * @param dto
	 * @param shainNo
	 * @param nowAddress
	 * @param commentUpdateDate
	 * @param commentName
	 * @param commentNote
	 * @param nyukyoChangeFlag
	 * @param kaishiChangeFlg
	 * @param taikyoChangeFlag
	 * @param henkanChangeFlag
	 * @return
	 */
	private boolean updateApplHistoryAgree(String newApplNo, Skf2010Sc004AgreeAsyncDto dto, String shainNo,
			String nowAddress, Date commentUpdateDate, String commentName, String commentNote, String nyukyoChangeFlag,
			String kaishiChangeFlg, String taikyoChangeFlag, String henkanChangeFlag) {
		String applId = dto.getApplId();
		String applNo = dto.getApplNo();

		String nyukyobi = dto.getNyukyobi();
		String taikyobi = dto.getTaikyobi();
		String shiyobi = dto.getShiyobi();
		String shiyobi2 = dto.getShiyobi2();
		String henkanbi = dto.getHenkanbi();
		Map<String, String> errorMsg = new HashMap<String, String>();

		if (applId.equals(FunctionIdConstant.R0100)) {
			// 案内内容が「社宅入居希望等調書」の場合

			// 退居予定を設定する
			String taikyoYotei = CodeConstant.NONE;

			// 入居希望等調書・入居決定通知テーブル更新処理
			List<Skf2010Sc004GetShatakuKiboForUpdateExp> shatakuKibouList = new ArrayList<Skf2010Sc004GetShatakuKiboForUpdateExp>();

			shatakuKibouList = skf2010Sc004SharedService.getShatakuKiboForUpdate(applNo);
			if (shatakuKibouList == null || shatakuKibouList.size() <= 0) {
				ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
				return false;
			}

			// 現社宅の退居日が入力されていない場合
			if (CheckUtils.isEmpty(taikyobi)) {
				// 退居しない（継続使用する）
				taikyoYotei = CodeConstant.NOT_LEAVE;
			} else {
				// 退居する
				taikyoYotei = CodeConstant.LEAVE;
			}

			// 入居可能日、駐車場利用可能日、入居日変更フラグ、退居日変更フラグ、保管場所利用開始日変更フラグ
			// 入居希望等調書・入居決定通知テーブルの更新
			boolean res = skf2010Sc004SharedService.updateTaikyobi(nyukyobi, taikyobi, shiyobi, shiyobi2, taikyoYotei,
					nyukyoChangeFlag, taikyoChangeFlag, kaishiChangeFlg, henkanChangeFlag, applNo);
			if (!res) {
				ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
				return false;
			}

			// 「現社宅の退居日」、｢現社宅先の駐車場使用返還日」のいずれかが入力されている場合
			if (!CheckUtils.isEmpty(taikyobi) || !CheckUtils.isEmpty(henkanbi)) {
				// 退居する社宅区分を設定
				String shatakuKubun = CodeConstant.NONE;

				if (!CheckUtils.isEmpty(taikyobi) && !CheckUtils.isEmpty(henkanbi)) {
					// 「現社宅の退居日」、「現社宅先の駐車場使用返還日」のいずれも入力されている場合
					shatakuKubun = CodeConstant.SHATAKU_PARK_TAIKYO;
				} else if (!CheckUtils.isEmpty(taikyobi) && CheckUtils.isEmpty(henkanbi)) {
					// 「現社宅の退居日」のみ入力されている場合
					shatakuKubun = CodeConstant.SHATAKU_TAIKYO;
				} else if (CheckUtils.isEmpty(taikyobi) && !CheckUtils.isEmpty(henkanbi)) {
					// 「現社宅先の駐車場使用返還日」のみ入力されている場合
					shatakuKubun = CodeConstant.PARK_TAIKYO;
				}

				// 退居届の自動作成判定
				boolean bAutoTaikyo = true;
				// 入退居予定データの入退居区分「2:退居」の申請書類管理番号が「1:入居」と同一の場合True
				bAutoTaikyo = skf2010Sc004SharedService.getNYDTaikyoCheckInfo(shainNo, applNo);

				if (bAutoTaikyo) {

					boolean rInsertTaikyoReport = skf2010Sc004SharedService.insertTaikyoReportInfo(newApplNo, applNo,
							taikyoYotei, shatakuKubun, henkanbi, nowAddress, CodeConstant.NOW_SHATAKU_NAME_ADD_STRING,
							taikyoChangeFlag, henkanChangeFlag);
					// 備品返却申請を新規追加
					String bihinHenkyakuShinseiApplNo = CodeConstant.NONE;
					// 備品返却申請テーブルから備品返却申請の書類管理番号を取得
					bihinHenkyakuShinseiApplNo = skf2010Sc004SharedService.getBihinHenkyakuInfo(newApplNo);

					// なければ備品返却申請の書類管理番号を新規発行
					if (CheckUtils.isEmpty(bihinHenkyakuShinseiApplNo)) {
						// 備品返却申請用の申請書類管理番号を取得
						bihinHenkyakuShinseiApplNo = skfShinseiUtils.getBihinHenkyakuShinseiNewApplNo(companyCd,
								shainNo);
						// 備品返却申請テーブルを新規発行
						boolean insBihinRes = skf2010Sc004SharedService
								.insertBihinHenkyakuInfo(bihinHenkyakuShinseiApplNo, applNo, newApplNo, false);
						if (!insBihinRes) {
							ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1078,
									BIHIN_HENKYAKU_ADD_STRING);
							return false;
						}
					} else {
						// 備品返却申請テーブルを更新
						boolean updBihinRes = skf2010Sc004SharedService
								.insertBihinHenkyakuInfo(bihinHenkyakuShinseiApplNo, applNo, newApplNo, true);
						if (!updBihinRes) {
							ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1078,
									BIHIN_HENKYAKU_ADD_STRING);
							return false;
						}
					}
					if (rInsertTaikyoReport) {
						// 退居（自動車の保管場所返還）届テーブル登録できた場合

						// 「申請書類履歴テーブル」を登録
						Date applDate = new Date();
						boolean insApplHistoryRes = skf2010Sc004SharedService.insertApplHistoryInfo(shainNo, newApplNo,
								applDate);
						if (!insApplHistoryRes) {
							ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1073);

							return false;
						}
					}
				}

			}
		}
		Date applDate = new Date();
		String applStatus = CodeConstant.STATUS_DOI_ZUMI;
		String applTacFlg = CodeConstant.NONE;

		// 排他処理用最終更新日取得
		Date lastUpdateDate = (Date) menuScopeSessionBean.get(skf2010Sc004SharedService.KEY_LAST_UPDATE_DATE_HISTORY);

		boolean agreeStatusRes = skf2010Sc004SharedService.updateApplHistoryAgreeStatus(applNo, applDate, applTacFlg,
				applStatus, CodeConstant.NONE, CodeConstant.NONE, CodeConstant.NONE, lastUpdateDate, errorMsg);
		if (!agreeStatusRes) {
			ServiceHelper.addErrorResultMessage(dto, null, errorMsg.get("error"), errorMsg.get("value"));
			return false;
		}

		// コメントの更新
		boolean commentErrorMessage = skfCommentUtils.insertComment(CodeConstant.C001, applNo, applStatus, commentNote,
				errorMsg);
		if (!commentErrorMessage) {
			return false;
		}

		return true;
	}

	/**
	 * 申請情報の取得を行います
	 * 
	 * @param agreeDto
	 */
	private void getApplInfo(Skf2010Sc004AgreeAsyncDto agreeDto) {
		String applId = agreeDto.getApplId();
		String applNo = agreeDto.getApplNo();

		if (applId.equals(FunctionIdConstant.R0100)) {
			Skf2020TNyukyoChoshoTsuchi nyukyoChoshoTsuchi = new Skf2020TNyukyoChoshoTsuchi();
			nyukyoChoshoTsuchi = skf2010Sc004SharedService.getNyukyoChoshoTsuchiInfo(applNo);
			if (nyukyoChoshoTsuchi == null) {
				ServiceHelper.addErrorResultMessage(agreeDto, null, MessageIdConstant.E_SKF_1078, "初期表示中に");
				LogUtils.infoByMsg("同意するボタン押下時： 社宅入居希望等申請情報を取得失敗 　 申請書番号：" + agreeDto.getApplNo());
				return;
			}

			// 入居予定日
			agreeDto.setNyukyoYoteiDate(nyukyoChoshoTsuchi.getNyukyoYoteiDate());
			// 駐車場使用開始日
			agreeDto.setParkingUserDate(nyukyoChoshoTsuchi.getParkingUseDate());
			// 駐車場使用開始日2
			agreeDto.setParkingUserDate2(nyukyoChoshoTsuchi.getParkingUseDate2());

		} else if (applId.equals(FunctionIdConstant.R0103)) {
			Skf2040TTaikyoReport taikyoReport = new Skf2040TTaikyoReport();
			taikyoReport = skf2010Sc004SharedService.getTaikkyoReportInfo(applNo);
			if (taikyoReport == null) {
				ServiceHelper.addErrorResultMessage(agreeDto, null, MessageIdConstant.E_SKF_1078, "初期表示中に");
				LogUtils.infoByMsg("同意するボタン押下時： 退居申請情報を取得失敗 　 申請書番号：" + agreeDto.getApplNo());
				return;
			}
			// 退居日
			agreeDto.setTaikyoDate(taikyoReport.getTaikyoDate());
			// 駐車場返還日
			agreeDto.setParkingHenkanDate(taikyoReport.getParkingHenkanDate());

		}
	}

	/**
	 * コメントの入力チェック
	 * 
	 * @param agreeDto
	 * @return
	 */
	private boolean validateReason(Skf2010Sc004AgreeAsyncDto agreeDto) {
		String reasonText = agreeDto.getCommentNote();
		int commentMaxLength = Integer.parseInt(PropertyUtils.getValue("skf.common.comment_max_length"));
		if (reasonText != null) {
			int byteCnt = reasonText.getBytes(Charset.forName("UTF-8")).length;
			if (byteCnt >= commentMaxLength) {
				ServiceHelper.addErrorResultMessage(agreeDto, new String[] { "commentNote" },
						MessageIdConstant.E_SKF_1049, "承認者へのコメント", Long.valueOf(commentMaxLength) / 2);
				return false;
			}
		}
		return true;
	}

	/**
	 * バリデーションチェックをします
	 * 
	 * @param agreeDto
	 * @return
	 */
	private boolean inputValidate(Skf2010Sc004AgreeAsyncDto agreeDto) {
		//必須入力チェック
		boolean validateResult = true;
		
		//現社宅の退居日
		if (!NfwStringUtils.isBlank(agreeDto.getSyokiTaikyoDate()) && NfwStringUtils.isBlank(agreeDto.getTaikyobi())) {
			ServiceHelper.addErrorResultMessage(agreeDto, new String[] {"taikyobi"}, MessageIdConstant.E_SKF_1048, "現社宅の退居日");
			validateResult = false;
			LogUtils.debugByMsg("退居日テスト 初期→" + agreeDto.getSyokiTaikyoDate() + "入力後→" + agreeDto.getTaikyobi());
		}
		//現社宅の駐車場返還日
		if (!NfwStringUtils.isBlank(agreeDto.getSyokiParkingDate()) && NfwStringUtils.isBlank(agreeDto.getHenkanbi())) {
		ServiceHelper.addErrorResultMessage(agreeDto, new String[] {"henkanbi"}, MessageIdConstant.E_SKF_1048, "現社宅の駐車場返還日");
			validateResult = false;
			LogUtils.debugByMsg("返還日テスト 初期→" + agreeDto.getSyokiParkingDate() + "入力後→" + agreeDto.getHenkanbi());
		}
		//新社宅入居日
		if (!NfwStringUtils.isBlank(agreeDto.getSyokiNyukyoDate()) && NfwStringUtils.isBlank(agreeDto.getNyukyobi())) {
			ServiceHelper.addErrorResultMessage(agreeDto, new String[] {"nyukyobi"}, MessageIdConstant.E_SKF_1048, "新社宅の入居日");
			validateResult = false;
			LogUtils.debugByMsg("入居日テスト 初期→" + agreeDto.getSyokiNyukyoDate() + "入力後→" + agreeDto.getNyukyobi());
		}
		//新社宅の駐車場使用開始日
		if (!NfwStringUtils.isBlank(agreeDto.getSyokiParkingUseDate()) && NfwStringUtils.isBlank(agreeDto.getShiyobi())) {
			ServiceHelper.addErrorResultMessage(agreeDto, new String[] {"shiyobi"}, MessageIdConstant.E_SKF_1048, "新社宅先の駐車場使用開始日");
			validateResult = false;
			
			LogUtils.debugByMsg("駐車開始日１テスト 初期→" + agreeDto.getSyokiParkingUseDate() + "入力後→" + agreeDto.getShiyobi());
		}
		//新社宅の駐車場使用開始日２
		if (!NfwStringUtils.isBlank(agreeDto.getSyokiParkingUseDate2()) && NfwStringUtils.isBlank(agreeDto.getShiyobi2())) {
			ServiceHelper.addErrorResultMessage(agreeDto, new String[] {"shiyobi2"}, MessageIdConstant.E_SKF_1048, "新社宅先の駐車場使用開始日2");
			validateResult = false;
			LogUtils.debugByMsg("エラーに値入ってるか確認" +  agreeDto.getShiyobi2Err());
			LogUtils.debugByMsg("駐車開始日２テスト 初期→" + agreeDto.getSyokiParkingUseDate2() + "入力後→" + agreeDto.getShiyobi2());
		}
		
		return validateResult;
	}

	/**
	 * 初期化処理
	 * 
	 * @param agreeDto
	 */
	private void init(Skf2010Sc004AgreeAsyncDto agreeDto) {
		// エラー背景色初期化
		agreeDto.setTaikyobiErr("");
		agreeDto.setHenkanbiErr("");
		agreeDto.setNyukyobiErr("");
		agreeDto.setShiyobiErr("");
		agreeDto.setShiyobi2Err("");

		return;
	}

}
