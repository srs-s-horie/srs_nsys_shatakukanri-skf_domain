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
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetShatakuKiboForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchi;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReport;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.common.utils.PropertyUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfMailUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.SkfShinseiUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc004.Skf2010Sc004AgreeAsyncDto;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc004.Skf2010Sc004AgreeAsyncDto;

/**
 * Skf2010Sc004 申請内容表示/引戻し同意する非同期処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc004AgreeAsyncService extends AsyncBaseServiceAbstract<Skf2010Sc004AgreeAsyncDto> {

	@Autowired
	private Skf2010Sc004SharedService skf2010Sc004SharedService;

	@Autowired
	private SkfShinseiUtils skfShinseiUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private SkfMailUtils skfMailUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

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
		skfOperationLogUtils.setAccessLog("「同意しない」", companyCd, FunctionIdConstant.SKF2010_SC004);

		// 初期化処理
		init(agreeDto);

		// 申請書類ID
		String applId = agreeDto.getApplId();
		// エラーメッセージ保持用
		Map<String, String> errorMsg = new HashMap<String, String>();

		// 入力チェック
		boolean inputAreaVisible = false;
		boolean validateResult = true;
		if (!NfwStringUtils.isEmpty(agreeDto.getInputAreaVisible())) {
			inputAreaVisible = Boolean.valueOf(agreeDto.getInputAreaVisible());
		}
		if (inputAreaVisible) {
			validateResult = inputValidate(agreeDto);
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
		String syokiTaikyobi = agreeDto.getTaikyoDate();

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
		String syokiHenkanbi = agreeDto.getParkingHenkanDate();
		// 返還日変更フラグ(0：変更なし)
		String henkanChangeFlag = SkfCommonConstant.NOT_CHANGE;

		// 返還日の有無をチェック
		if (NfwStringUtils.isEmpty(syokiHenkanbi)) {
			// 返還日が無い時にテキストボックスの入力があった場合、変更フラグを1:変更ありにする。
			if (!NfwStringUtils.isEmpty(henkanbi)) {
				// 返還日変更フラグに1：変更ありを設定
				henkanChangeFlag = SkfCommonConstant.DATE_CHANGE;
			}
		} else {
			if (!syokiHenkanbi.equals(henkanbi)) {
				// 返還日変更フラグに1：変更ありを設定
				henkanChangeFlag = SkfCommonConstant.DATE_CHANGE;
			}
		}

		// 「新社宅の入居日」
		String nyukyobi = agreeDto.getNyukyobi();

		// 初期入居日
		String syokiNyukyobi = agreeDto.getNyukyoYoteiDate();
		// 入居日変更フラグ(0：変更なし)
		String nyukyoChangeFlag = SkfCommonConstant.NOT_CHANGE;

		// 入居日の有無をチェック
		if (NfwStringUtils.isEmpty(syokiNyukyobi)) {
			// 入居日が無い時にテキストボックスの入力があった場合、変更フラグを1:変更ありにする。
			if (!NfwStringUtils.isEmpty(nyukyobi)) {
				// 入居日変更フラグに1：変更ありを設定
				nyukyoChangeFlag = SkfCommonConstant.DATE_CHANGE;
			}
		} else {
			if (!syokiNyukyobi.equals(nyukyobi)) {
				// 入居日変更フラグに1：変更ありを設定
				nyukyoChangeFlag = SkfCommonConstant.DATE_CHANGE;
			}
		}

		// 「新社宅先の駐車場使用開始日」
		String shiyobi = agreeDto.getShiyobi();
		String shiyobi2 = agreeDto.getShiyobi2();

		// 初期駐車場使用開始日
		String syokiShiyobi = agreeDto.getParkingUserDate();
		String syokiShiyobi2 = agreeDto.getParkingUserDate2();

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

		if (NfwStringUtils.isEmpty(taikyobi) || NfwStringUtils.isEmpty(henkanbi)) {
			newApplNo = skf2010Sc004SharedService.getApplNo(shainNo, applId);

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

		// 退居届の申請書類番号が存在する場合
		if (!NfwStringUtils.isEmpty(newApplNo)) {
			// 社宅入居時、退居届けの承認依頼メールを送信する
			Map<String, String> applInfo2 = new HashMap<String, String>();
			applInfo2.put("applNo", newApplNo);
			applInfo2.put("applId", applId);
			applInfo2.put("applShainNo", shainNo);

			boolean sendMailRes = skf2010Sc004SharedService.sendApplTsuchiMailShiShaJimuShoTanto(
					CodeConstant.SHONIN_IRAI_TSUCHI, applInfo2, CodeConstant.NONE, errorMsg);
			if (!sendMailRes) {
				if (!CheckUtils.isEmpty(errorMsg.get("errorValue"))) {
					ServiceHelper.addErrorResultMessage(agreeDto, null, errorMsg.get("error"),
							errorMsg.get("errorValue"));
				} else {
					ServiceHelper.addErrorResultMessage(agreeDto, null, errorMsg.get("error"));
				}
				throwBusinessExceptionIfErrors(agreeDto.getResultMessages());
				return agreeDto;
			}

		}
		// Hidden領域の備品希望要否
		String applNoBihinShinsei = CodeConstant.NONE;
		String bihinKibo = agreeDto.getBihinKibo();
		if (!NfwStringUtils.isEmpty(bihinKibo) && bihinKibo.equals(CodeConstant.BIHIN_KIBO_SHINSEI_HITSUYO)) {
			// 備品希望申請の申請書類履歴テーブルを作成する
			applNoBihinShinsei = skf2010Sc004SharedService.insertApplHistoryBihinKibo(newApplNo, applId, shainNo,
					errorMsg);
			if (applNoBihinShinsei == null) {
				// エラーの時のみNULL
				ServiceHelper.addErrorResultMessage(agreeDto, null, errorMsg.get("error"));
				throwBusinessExceptionIfErrors(agreeDto.getResultMessages());
				return agreeDto;
			}

			// 同意確認通知のメールを送信する
			Map<String, String> applInfoBihin = new HashMap<String, String>();
			applInfoBihin.put("applNo", applNoBihinShinsei);
			applInfoBihin.put("applId", FunctionIdConstant.R0104);
			applInfoBihin.put("applShainNo", shainNo);

			// 案内文取得
			String annai = PropertyUtils.getValue("skf2010.skf2010_sc004.mail_bihin_kibo");

			String urlBase = "/skf/Skf2010Sc005/init";

			// メール送信
			skfMailUtils.sendApplTsuchiMail(CodeConstant.TEJI_TSUCHI, applInfoBihin, agreeDto.getCommentNote(), annai,
					shainNo, CodeConstant.NONE, urlBase);

			// 備品申請の申請書類管理番号をセットされている場合は自動遷移のダイアログ表示
			if (NfwStringUtils.isNotEmpty(applNoBihinShinsei)) {
				ServiceHelper.addResultMessage(agreeDto, MessageIdConstant.I_SKF_2047);
				// ダイアログ表示フラグをセットする
				agreeDto.setDialogFlg(true);
				agreeDto.setBihinApplNo(applInfoBihin.get("applNo"));
			}

		}
		// ページ遷移先は「申請状況一覧」

		return agreeDto;
	}

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
					nyukyoChangeFlag, kaishiChangeFlg, applNo);
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

		boolean agreeStatusRes = skf2010Sc004SharedService.updateApplHistoryAgreeStatus(applNo, applDate, applTacFlg,
				applStatus, CodeConstant.NONE, CodeConstant.NONE, CodeConstant.NONE, errorMsg);
		if (!agreeStatusRes) {
			ServiceHelper.addErrorResultMessage(dto, null, errorMsg.get("error"));
			return false;
		}

		// コメントの更新
		boolean commentRes = skf2010Sc004SharedService.updateCommentInfo(applNo, applStatus, commentName, commentNote,
				errorMsg);
		if (!commentRes) {
			ServiceHelper.addErrorResultMessage(dto, null, errorMsg.get("error"));
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
		// 入力チェック
		boolean validateResult = true;
		// 「現社宅の退居日」
		if (!CheckUtils.isEmpty(agreeDto.getTaikyobi())
				&& (!CheckUtils.isFormatDate(agreeDto.getTaikyobi(), "yyyy/MM/dd") || !SkfCheckUtils
						.isSkfDateFormat(agreeDto.getTaikyobi(), CheckUtils.DateFormatType.YYYYMMDD))) {
			ServiceHelper.addErrorResultMessage(agreeDto, null, MessageIdConstant.E_SKF_1055, "「現社宅の退居日」");
			agreeDto.setTaikyobiErr(validationErrorCode);
			validateResult = false;
		}
		// 「現社宅先の駐車場返還日」
		if (!CheckUtils.isEmpty(agreeDto.getHenkanbi())
				&& (!CheckUtils.isFormatDate(agreeDto.getHenkanbi(), "yyyy/MM/dd") || !SkfCheckUtils
						.isSkfDateFormat(agreeDto.getHenkanbi(), CheckUtils.DateFormatType.YYYYMMDD))) {
			ServiceHelper.addErrorResultMessage(agreeDto, null, MessageIdConstant.E_SKF_1055, "「現社宅の駐車場返還日」");
			agreeDto.setHenkanbiErr(validationErrorCode);
			validateResult = false;
		}
		// 「新社宅の入居日」
		if (!CheckUtils.isEmpty(agreeDto.getNyukyobi())
				&& (!CheckUtils.isFormatDate(agreeDto.getNyukyobi(), "yyyy/MM/dd") || !SkfCheckUtils
						.isSkfDateFormat(agreeDto.getNyukyobi(), CheckUtils.DateFormatType.YYYYMMDD))) {
			ServiceHelper.addErrorResultMessage(agreeDto, null, MessageIdConstant.E_SKF_1055, "「新社宅の入居日」");
			agreeDto.setNyukyobiErr(validationErrorCode);
			validateResult = false;
		}
		// 「新社宅先の駐車場使用開始日」
		if (!CheckUtils.isEmpty(agreeDto.getShiyobi()) && (!CheckUtils.isFormatDate(agreeDto.getShiyobi(), "yyyy/MM/dd")
				|| !SkfCheckUtils.isSkfDateFormat(agreeDto.getShiyobi(), CheckUtils.DateFormatType.YYYYMMDD))) {
			ServiceHelper.addErrorResultMessage(agreeDto, null, MessageIdConstant.E_SKF_1055, "「新社宅先の駐車場使用開始日」");
			agreeDto.setShiyobiErr(validationErrorCode);
			validateResult = false;
		}
		// 「新社宅先の駐車場使用開始日2」
		if (!CheckUtils.isEmpty(agreeDto.getShiyobi2())
				&& (!CheckUtils.isFormatDate(agreeDto.getShiyobi2(), "yyyy/MM/dd") || !SkfCheckUtils
						.isSkfDateFormat(agreeDto.getShiyobi2(), CheckUtils.DateFormatType.YYYYMMDD))) {
			ServiceHelper.addErrorResultMessage(agreeDto, null, MessageIdConstant.E_SKF_1055, "「新社宅先の駐車場使用開始日2」");
			agreeDto.setShiyobi2Err(validationErrorCode);
			validateResult = false;
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
