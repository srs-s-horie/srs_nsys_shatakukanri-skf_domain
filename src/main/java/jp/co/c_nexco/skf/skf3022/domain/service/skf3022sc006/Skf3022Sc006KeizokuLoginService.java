/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc006;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3022TTeijiData;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.LoginUserInfoUtils;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022Sc006common.Skf3022Sc006CommonDto;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc006.Skf3022Sc006KeizokuLoginDto;
import jp.co.intra_mart.mirage.integration.guice.Transactional;

/**
 * Skf3022Sc006KeizokuLoginService 提示データ登録画面：入居情報の継続登録ボタン押下時処理クラス。　 
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf3022Sc006KeizokuLoginService extends SkfServiceAbstract<Skf3022Sc006KeizokuLoginDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3022Sc006SharedService skf3022Sc006SharedService;
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	@Value("${skf.common.company_cd}")
	private String companyCd;

	/**
	 * サービス処理を行う。　
	 * 
	 * @param initDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public Skf3022Sc006KeizokuLoginDto index(Skf3022Sc006KeizokuLoginDto initDto) throws Exception {

		initDto.setPageTitleKey(MessageIdConstant.SKF3022_SC006_TITLE);

		// デバッグログ
		LogUtils.debugByMsg("継続登録");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("継続登録", CodeConstant.C001, FunctionIdConstant.SKF3022_SC006);
		// ドロップダウンリスト
		List<Map<String, Object>> sc006KyojyusyaKbnSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006YakuinSanteiSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006KyoekihiPayMonthSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006KibouTimeInSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006KibouTimeOutSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006SogoRyojokyoSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006SogoHanteiKbnSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006SokinShatakuSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006SokinKyoekihiSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006OldKaisyaNameSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006KyuyoKaisyaSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006HaizokuKaisyaSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006TaiyoKaisyaSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006KariukeKaisyaSelectList = new ArrayList<Map<String, Object>>();
		// 可変ラベルリスト
		List<Map<String, Object>> labelList = new ArrayList<Map<String, Object>>();
		labelList.addAll(skf3022Sc006SharedService.jsonArrayToArrayList(initDto.getJsonLabelList()));

		// エラーコントロールクリア
		skf3022Sc006SharedService.clearVaridateErr(initDto);
		// 非活性制御クリア
		skf3022Sc006SharedService.setDisableCtrlAll(false, initDto);
		// 現在のラベル値をDTOに設定
		skf3022Sc006SharedService.setErrVariableLabel(labelList, initDto);
		// 備品は再取得しない
		initDto.setBihinItiranFlg(false);

		// ドロップダウンリスト作成
		skf3022Sc006SharedService.setDdlControlValues(
				initDto.getSc006KyojyusyaKbnSelect(), sc006KyojyusyaKbnSelectList,
				initDto.getSc006YakuinSanteiSelect(), sc006YakuinSanteiSelectList,
				initDto.getSc006KyoekihiPayMonthSelect(), sc006KyoekihiPayMonthSelectList,
				initDto.getSc006KibouTimeInSelect(), sc006KibouTimeInSelectList,
				initDto.getSc006KibouTimeOutSelect(), sc006KibouTimeOutSelectList,
				initDto.getSc006SogoRyojokyoSelect(), sc006SogoRyojokyoSelectList,
				initDto.getSc006SogoHanteiKbnSelect(), sc006SogoHanteiKbnSelectList,
				initDto.getSc006SokinShatakuSelect(), sc006SokinShatakuSelectList,
				initDto.getSc006SokinKyoekihiSelect(), sc006SokinKyoekihiSelectList,
				initDto.getSc006OldKaisyaNameSelect(), sc006OldKaisyaNameSelectList,
				initDto.getSc006KyuyoKaisyaSelect(), sc006KyuyoKaisyaSelectList,
				initDto.getSc006HaizokuKaisyaSelect(), sc006HaizokuKaisyaSelectList,
				initDto.getSc006TaiyoKaisyaSelect(), sc006TaiyoKaisyaSelectList,
				initDto.getSc006KariukeKaisyaSelect(), sc006KariukeKaisyaSelectList);

		// ドロップダウンリスト設定
		initDto.setSc006KyojyusyaKbnSelectList(sc006KyojyusyaKbnSelectList);
		initDto.setSc006YakuinSanteiSelectList(sc006YakuinSanteiSelectList);
		initDto.setSc006KyoekihiPayMonthSelectList(sc006KyoekihiPayMonthSelectList);
		initDto.setSc006KibouTimeInSelectList(sc006KibouTimeInSelectList);
		initDto.setSc006KibouTimeOutSelectList(sc006KibouTimeOutSelectList);
		initDto.setSc006SogoRyojokyoSelectList(sc006SogoRyojokyoSelectList);
		initDto.setSc006SogoHanteiKbnSelectList(sc006SogoHanteiKbnSelectList);
		initDto.setSc006SokinShatakuSelectList(sc006SokinShatakuSelectList);
		initDto.setSc006SokinKyoekihiSelectList(sc006SokinKyoekihiSelectList);
		initDto.setSc006OldKaisyaNameSelectList(sc006OldKaisyaNameSelectList);
		initDto.setSc006KyuyoKaisyaSelectList(sc006KyuyoKaisyaSelectList);
		initDto.setSc006HaizokuKaisyaSelectList(sc006HaizokuKaisyaSelectList);
		initDto.setSc006TaiyoKaisyaSelectList(sc006TaiyoKaisyaSelectList);
		initDto.setSc006KariukeKaisyaSelectList(sc006KariukeKaisyaSelectList);

		// 選択タブインデックス初期値
		String setHdnTabIndexOld = initDto.getHdnTabIndex();
		// 画面ステータス設定
		skf3022Sc006SharedService.pageLoadComplete(initDto);
		initDto.setHdnTabIndex("999");
		// 処理状態クリア
		initDto.setSc006Status("");

		// チェック処理
//		Dim checkMessage As New StringBuilder
//		Dim appBihinFlg As Boolean = False
//		Dim bihinErrFlg As Boolean = False
//		nextTabIndex = Nothing
		Boolean appBihinFlg = false;
		Boolean bihinErrFlg = false;
		
		// 備品ステータスのチェック処理
		appBihinFlg = skf3022Sc006SharedService.checkBihinTaiyoStts(initDto);
		// 備品エラー判定
		if (Skf3022Sc006CommonDto.SELECT_TAB_INDEX_BIHIN.equals(initDto.getHdnTabIndex())) {
			bihinErrFlg = true;
		}
		// 入居の場合
		if (Objects.equals(codeCacheUtils.getGenericCodeName(FunctionIdConstant.GENERIC_CODE_NYUTAIKYO_KBN,
					CodeConstant.NYUTAIKYO_KBN_NYUKYO), initDto.getSc006NyutaikyoKbn())) {
			// 入居入力チェック
			if (skf3022Sc006SharedService.checkForNyukyo(false, bihinErrFlg, appBihinFlg, initDto)) {
//				// 画面ステータス設定
//				skf3022Sc006SharedService.pageLoadComplete(initDto);
				return initDto;
			}
		// 退居の場合
		} else if (Objects.equals(codeCacheUtils.getGenericCodeName(FunctionIdConstant.GENERIC_CODE_NYUTAIKYO_KBN,
					CodeConstant.NYUTAIKYO_KBN_TAIKYO), initDto.getSc006NyutaikyoKbn())) {
			// 退居入力チェック
			if (skf3022Sc006SharedService.checkForTaikyo(false, bihinErrFlg, initDto)) {
//				// 画面ステータス設定
//				skf3022Sc006SharedService.pageLoadComplete(initDto);
				return initDto;
			}
		// 変更の場合
		} else {
			// 変更入力チェック
			if (skf3022Sc006SharedService.checkForHenko(false, initDto)) {
//				// 画面ステータス設定
//				skf3022Sc006SharedService.pageLoadComplete(initDto);
				return initDto;
			}
		}
		// DB更新
		update(initDto);
//		// 備品再取得しない
//		initDto.setBihinItiranFlg(false);
		// 備品再取得する
		initDto.setBihinItiranFlg(true);
		// メッセージ設定
		// 更新が完了しました。
		ServiceHelper.addResultMessage(initDto, MessageIdConstant.I_SKF_1011);
		// 画面ステータス設定
		skf3022Sc006SharedService.pageLoadComplete(initDto);
		// 表示タブ設定
		initDto.setHdnTabIndex(setHdnTabIndexOld);
		return initDto;
	}

	@Transactional
	private void update(Skf3022Sc006KeizokuLoginDto initDto) {

		// 日付フォーマット
		SimpleDateFormat dateFormat = new SimpleDateFormat(Skf3022Sc006CommonDto.DATE_FORMAT);
		// システム日時の取得
		Date sysDateTime = skfBaseBusinessLogicUtils.getSystemDateTime();
		// 社員番号
		String shainNo = CheckUtils.isEmpty(initDto.getSc006ShainNo()) ? "" : initDto.getSc006ShainNo().replace(CodeConstant.ASTERISK, "");
		Map<Skf3022Sc006CommonDto.TEIJIDATA_PARAM, String> paramMap
						= new HashMap<Skf3022Sc006CommonDto.TEIJIDATA_PARAM, String>();
		// 提示番号
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.TEIJI_NO, initDto.getHdnTeijiNo());
		// 社員番号
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHAIN_NO, shainNo);
		// 入退居区分
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.NYUTAIKYO_KBN, initDto.getHdnNyutaikyoKbnOld());
		// 社宅管理番号元
		paramMap.put(
				Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKU_KANRINO_OLD, initDto.getHdnShatakuKanriNoOld());
		// 部屋管理番号元
		paramMap.put(
				Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKU_ROOMKANRINO_OLD, initDto.getHdnRoomKanriNoOld());
		// 駐車場管理番号1元
		paramMap.put(
				Skf3022Sc006CommonDto.TEIJIDATA_PARAM.CHUSHAJONO_ONE_OLD, initDto.getHdnChushajoNoOneOld());
		// 駐車場管理番号2元
		paramMap.put(
				Skf3022Sc006CommonDto.TEIJIDATA_PARAM.CHUSHAJONO_TWO_OLD, initDto.getHdnChushajoNoTwoOld());
		// 社宅管理番号
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKU_KANRINO, initDto.getHdnShatakuKanriNo());
		// 部屋管理番号
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKU_ROOMKANRINO, initDto.getHdnRoomKanriNo());
		// 駐車場管理番号1
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.CHUSHAJONO_ONE, initDto.getHdnChushajoNoOne());
		// 駐車場管理番号2
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.CHUSHAJONO_TWO, initDto.getHdnChushajoNoTwo());
		// 入居予定日
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.NYUKYOYOTEI_DATE,
				skf3022Sc006SharedService.getDateText(initDto.getSc006NyukyoYoteiDay()));
		// 退居予定日
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.TAIKYOYOTEI_DATE,
				skf3022Sc006SharedService.getDateText(initDto.getSc006TaikyoYoteiDay()));
		// 継続登録
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.BUTTON_FLG, Skf3022Sc006CommonDto.KEIZOKU_LOGIN);
		// 提示データ更新日
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.TEIJIDATA_UPDATEDATE,
												initDto.getHdnTeijiDataUpdateDate());
		// 入退居予定更新日
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.NYUTAIKYOYOTEI_UPDATEDATE,
													initDto.getHdnNyutaikyoYoteiUpdateDate());
		// 社宅部屋情報マスタ元更新日
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKUROOM_OLD_UPDATEDATE,
												initDto.getHdnShatakuRoomOldUpdateDate());
		// 社宅部屋情報マスタ更新日
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKUROOM_UPDATEDATE,
												initDto.getHdnShatakuRoomUpdateDate());
		// 社宅駐車場区画情報マスタ元更新日（区画１）
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKU_PARKINGBLOCK_OLD1_UPDATEDATE,
												initDto.getHdnShatakuParkingBlockOld1UpdateDate());
		// 社宅駐車場区画情報マスタ元更新日（区画１）
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKU_PARKINGBLOCK_OLD11_UPDATEDATE,
												initDto.getHdnShatakuParkingBlockOld11UpdateDate());
		// 社宅駐車場区画情報マスタ更新日（区画１）
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKU_PARKINGBLOCK_1_UPDATEDATE,
												initDto.getHdnShatakuParkingBlock1UpdateDate());
		// 社宅駐車場区画情報マスタ元更新日（区画２）
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKU_PARKINGBLOCK_OLD2_UPDATEDATE,
												initDto.getHdnShatakuParkingBlockOld2UpdateDate());
		// 社宅駐車場区画情報マスタ元更新日（区画２）
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKU_PARKINGBLOCK_OLD21_UPDATEDATE,
												initDto.getHdnShatakuParkingBlockOld21UpdateDate());
		// 社宅駐車場区画情報マスタ更新日（区画２）
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKU_PARKINGBLOCK_2_UPDATEDATE,
												initDto.getHdnShatakuParkingBlock2UpdateDate());
		//'更新者
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.UPDATE_USER_ID, LoginUserInfoUtils.getUserCd());
		//'更新機能ID
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.UPDATE_PROGRAM_ID, initDto.getPageId());
		// 使用料変更フラグ
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHIYOURYO_FLG, initDto.getHdnSiyouryoFlg());
		//
		//'提示備品一覧情報設定
		//Me.SetBihinList()
		//
		// 使用料パターン一覧情報設定
		String rentalPatternUpdateDate = initDto.getHdnRentalPatternUpdateDate();
		Map<Skf3022Sc006CommonDto.RENTAL_PATTERN, String> rentalPatternTorokuList =
							new HashMap<Skf3022Sc006CommonDto.RENTAL_PATTERN, String>();
		rentalPatternTorokuList = skf3022Sc006SharedService.setRentalPatternList(initDto);
		// 継続登録処理
//		Select Case S2007_TeijiDataRegistBusinessLogic.TmpSaveAndCreateAndShatakuLogin( _
//										param, _
//										True, _
//										String.Empty, _
//										False, _
//										Me.GetColumnInfoList(KEIZOKU_LOGIN, True, sysDateTime), _
//										bihinCd, _
//										sysDateTime, _
//										updCountTJ, _
//										updCountNY, _
//										updCountTBD, _
//										updCountOldSR, _
//										updCountSR, _
//										updCountOldSPB1, _
//										updCountSPB1, _
//										updCountOldSPB2, _
//										updCountSPB2, _
//										HttpUtility.HtmlEncode(MyBase.publicInfo.CompanyCd), _
//										HttpUtility.HtmlEncode(MyBase.userInfo.UserId()), _
//										HttpUtility.HtmlEncode(MyBase.publicInfo.IpAddress()), _
//										Me.GetDateText(HttpUtility.HtmlEncode(Me.txtTaiyoDay.Text)), _
//										updCountRP, _
//										rentalPatternUpdateDate, _
//										rentalPatternUpdateDateForRegist, _
//										rentalPatternTorokuList)
		// 更新カウンタMap
		Map<Skf3022Sc006CommonDto.UPDATE_COUNTER, Integer> updCountMap =
				new HashMap<Skf3022Sc006CommonDto.UPDATE_COUNTER, Integer>();
		// 更新提示データ情報
		Skf3022TTeijiData columnInfoList = skf3022Sc006SharedService.getColumnInfoList(
						Skf3022Sc006CommonDto.KEIZOKU_LOGIN, true, sysDateTime, initDto);
		int updateCnt = 0;
		try {
			updateCnt = skf3022Sc006SharedService.tmpSaveAndCreateAndShatakuLogin(
					paramMap,
					true,
					"",
					false,
					columnInfoList,
					initDto.getBihinInfoListTableData(),
					sysDateTime,
					updCountMap,
					companyCd,
					skf3022Sc006SharedService.getDateText(initDto.getSc006TaiyoDay()),
					rentalPatternUpdateDate,
					sysDateTime,
					rentalPatternTorokuList);
		} catch (Exception e) {
			// 登録時にエラーが発生しました。ヘルプデスクへ連絡してください。
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1073);
			LogUtils.infoByMsg("update, 登録時に例外発生：" + e.toString());
			// ロールバック
			throwBusinessExceptionIfErrors(initDto.getResultMessages());
		}
		// 結果判定
		switch (updateCnt) {
			case 0:
				// 登録時にエラーが発生しました。ヘルプデスクへ連絡してください。
				ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1073);
				// ロールバック
				throwBusinessExceptionIfErrors(initDto.getResultMessages());
			case -1:
				// 他のユーザによって更新されています。もう一度画面を更新してください。
				ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.W_SKF_1009);
				// ロールバック
				throwBusinessExceptionIfErrors(initDto.getResultMessages());
			default:
				break;
		};
		// 旧社員番号の場合、末尾に＊
		if (Skf3022Sc006CommonDto.SHAINNO_CHANGE_AVAILABLE.equals(initDto.getHdnShainNoChangeFlg())) {
			initDto.setSc006ShainNo(initDto.getSc006ShainNo() + CodeConstant.ASTERISK);
		}
		// 入退居区分
		initDto.setHdnNyutaikyoKbn(CodeConstant.NYUTAIKYO_KBN_NYUKYO);
		initDto.setSc006NyutaikyoKbn(codeCacheUtils.getGenericCodeName(
						FunctionIdConstant.GENERIC_CODE_NYUTAIKYO_KBN, CodeConstant.NYUTAIKYO_KBN_NYUKYO));
		// 継続登録フラグ（hidden変数）
		initDto.setHdnKeizokuBtnFlg(true);
		// 入居予定日
//				Dim nyukyoAfter As Date = DateAndTime.DateAdd(DateInterval.Day, 1, _
//								DateUtil.ConbersionFomatStringToDate(Me.GetDateText(Me.txtTaikyoYoteiDay.Text)))
//				Me.txtNyukyoYoteiDay.Text = DateUtil.ConvertSlashDateString(DateUtil.ConversionFormatYYYYMMDD(nyukyoAfter))
		initDto.setSc006NyukyoYoteiDay(skf3022Sc006SharedService.addDay(initDto.getSc006TaikyoYoteiDay(), 1));
//				Me.hdnTaikyoDay.Value = Me.GetDateText(Me.txtTaikyoYoteiDay.Text)
		initDto.setHdnTaikyoDate(skf3022Sc006SharedService.getDateText(initDto.getSc006TaikyoYoteiDay()));
//				Me.hdnNyukyoDay.Value = Me.GetDateText(Me.txtNyukyoYoteiDay.Text)
		initDto.setHdnNyukyoDate(skf3022Sc006SharedService.getDateText(initDto.getSc006NyukyoYoteiDay()));
		// 社宅使用料再計算
//				Me.SiyoryoKeiSan(String.Empty, String.Empty)
		Map<String, String> calcParamMap = skf3022Sc006SharedService.createSiyoryoKeiSanParam(initDto);	// 使用料計算パラメータ
		Map<String, String> resultMap = new HashMap<String, String>();	// 使用料計算戻り値
		StringBuffer errMsg = new StringBuffer();						// エラーメッセージ
		if (skf3022Sc006SharedService.siyoryoKeiSan("", "", calcParamMap, resultMap, errMsg)) {
			// 使用料計算でエラー
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.SKF3020_ERR_MSG_COMMON, errMsg);
			LogUtils.debugByMsg("社宅使用料計算で異常:" + errMsg);
			// ロールバック
			throwBusinessExceptionIfErrors(initDto.getResultMessages());
		} else {
			// 使用料計算戻り値設定
			skf3022Sc006SharedService.setSiyoryoKeiSanParam(resultMap, initDto);
		}
		// 退居予定日
//				Me.txtTaikyoYoteiDay.Text = String.Empty
		initDto.setSc006TaikyoYoteiDay(CodeConstant.DOUBLE_QUOTATION);
		// 区画１利用開始日
//				If Not String.IsNullOrEmpty(Me.GetDateText(Me.txtRiyouEndDayOne.Text)) Then
		if (!CheckUtils.isEmpty(skf3022Sc006SharedService.getDateText(initDto.getSc006RiyouEndDayOne()))) {
//					Dim startOneAfter As Date = DateAndTime.DateAdd(DateInterval.Day, 1, _
//					DateUtil.ConbersionFomatStringToDate(Me.GetDateText(Me.txtRiyouEndDayOne.Text)))
//					Me.txtRiyouStartDayOne.Text = DateUtil.ConvertSlashDateString(DateUtil.ConversionFormatYYYYMMDD(startOneAfter))
			initDto.setSc006RiyouStartDayOne(skf3022Sc006SharedService.addDay(initDto.getSc006RiyouEndDayOne(), 1));
			// 区画１利用終了日
//					Me.txtRiyouEndDayOne.Text = String.Empty
//					Me.hdnRiyouEndDayOne.Value = String.Empty
			initDto.setSc006RiyouEndDayOne("");
			initDto.setHdnRiyouEndDayOne("");
//					'
			// 駐車場区画１使用料再計算
//					Me.SiyoryoKeiSan(Me.hdnChushajoNoOne.Value, DATA_1)
			if (skf3022Sc006SharedService.siyoryoKeiSan(initDto.getHdnChushajoNoOne(), "1", calcParamMap, resultMap, errMsg)) {
				// 使用料計算でエラー
				ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.SKF3020_ERR_MSG_COMMON, errMsg);
				LogUtils.debugByMsg("区画1使用料計算で異常:" + errMsg);
				// ロールバック
				throwBusinessExceptionIfErrors(initDto.getResultMessages());
			} else {
				// 使用料計算戻り値設定
				skf3022Sc006SharedService.setSiyoryoKeiSanParam(resultMap, initDto);
			}
		}
		// 区画２利用開始日
//				If Not String.IsNullOrEmpty(Me.GetDateText(Me.txtRiyouEndDayTwo.Text)) Then
		if (!CheckUtils.isEmpty(skf3022Sc006SharedService.getDateText(initDto.getSc006RiyouEndDayTwo()))) {
//					Dim startTwoAfter As Date = DateAndTime.DateAdd(DateInterval.Day, 1, _
//					DateUtil.ConbersionFomatStringToDate(Me.GetDateText(Me.txtRiyouEndDayTwo.Text)))
//					Me.txtRiyouStartDayTwo.Text = DateUtil.ConvertSlashDateString(DateUtil.ConversionFormatYYYYMMDD(startTwoAfter))
			initDto.setSc006RiyouStartDayTwo(skf3022Sc006SharedService.addDay(initDto.getSc006RiyouEndDayTwo(), 1));
			// 区画２利用終了日
//					Me.txtRiyouEndDayTwo.Text = String.Empty
//					Me.hdnRiyouEndDayTwo.Value = String.Empty
			initDto.setSc006RiyouEndDayTwo("");
			initDto.setHdnRiyouEndDayTwo("");
			// 駐車場区画2使用料再計算
//					Me.SiyoryoKeiSan(Me.hdnChushajoNoTwo.Value, DATA_2)
			if (skf3022Sc006SharedService.siyoryoKeiSan(initDto.getHdnChushajoNoTwo(), "2", calcParamMap, resultMap, errMsg)) {
				// 使用料計算でエラー
				ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.SKF3020_ERR_MSG_COMMON, errMsg);
				LogUtils.debugByMsg("区画2使用料計算で異常:" + errMsg);
				// ロールバック
				throwBusinessExceptionIfErrors(initDto.getResultMessages());
			} else {
				// 使用料計算戻り値設定
				skf3022Sc006SharedService.setSiyoryoKeiSanParam(resultMap, initDto);
			}
		}
		// 貸与日
//				If Not String.IsNullOrEmpty(Me.GetDateText(Me.txtHenkyakuDay.Text)) Then
		if (!CheckUtils.isEmpty(skf3022Sc006SharedService.getDateText(initDto.getSc006HenkyakuDay()))) {
//					Dim taiyoAfter As Date = DateAndTime.DateAdd(DateInterval.Day, 1, _
//					DateUtil.ConbersionFomatStringToDate(Me.GetDateText(Me.txtHenkyakuDay.Text)))
//					Me.txtTaiyoDay.Text = DateUtil.ConvertSlashDateString(DateUtil.ConversionFormatYYYYMMDD(taiyoAfter))
			initDto.setSc006TaiyoDay(skf3022Sc006SharedService.addDay(initDto.getSc006HenkyakuDay(), 1));
			// 返却日
//					Me.txtHenkyakuDay.Text = String.Empty
			initDto.setSc006HenkyakuDay("");
		}
		// 相互利用情報の開始日
//				If Not String.IsNullOrEmpty(Me.GetDateText(Me.txtEndDay.Text)) Then
		if (!CheckUtils.isEmpty(initDto.getSc006EndDay())) {
//					Dim startAfter As Date = DateAndTime.DateAdd(DateInterval.Day, 1, _
//					DateUtil.ConbersionFomatStringToDate(Me.GetDateText(Me.txtEndDay.Text)))
//					Me.txtStartDay.Text = DateUtil.ConvertSlashDateString(DateUtil.ConversionFormatYYYYMMDD(startAfter))
			initDto.setSc006StartDay(skf3022Sc006SharedService.addDay(initDto.getSc006EndDay(), 1));
			// 相互利用情報の終了日
//					Me.txtEndDay.Text = String.Empty
			initDto.setSc006EndDay("");
		}
//
//				If 0 < updCountTJ Then
		if (updCountMap.get(Skf3022Sc006CommonDto.UPDATE_COUNTER.UPD_COUNT_TJ) > 0) {
			// 社宅提示ステータス
//					Me.hdnShatakuTeijiStatus.Value = Constant.ShatakuTeijiStatusKbn.SAKUSEI_CHU
			initDto.setHdnShatakuTeijiStatus(CodeConstant.PRESENTATION_SITUATION_SAKUSEI_CHU);
			// 提示データ更新日
			initDto.setHdnTeijiDataUpdateDate(dateFormat.format(sysDateTime));
		}
		if (updCountMap.get(Skf3022Sc006CommonDto.UPDATE_COUNTER.UPD_COUNT_NY) > 0) {
			// 入退居予定データ更新日
			initDto.setHdnNyutaikyoYoteiUpdateDate(dateFormat.format(sysDateTime));
		}
		if (updCountMap.get(Skf3022Sc006CommonDto.UPDATE_COUNTER.UPD_COUNT_OLD_SR) > 0) {
			// 社宅部屋情報マスタ元更新日
			initDto.setHdnShatakuRoomOldUpdateDate(dateFormat.format(sysDateTime));
		}
		if (updCountMap.get(Skf3022Sc006CommonDto.UPDATE_COUNTER.UPD_COUNT_SR) > 0) {
			// 社宅部屋情報マスタ更新日
			initDto.setHdnShatakuRoomUpdateDate(dateFormat.format(sysDateTime));
		}
		if (updCountMap.get(Skf3022Sc006CommonDto.UPDATE_COUNTER.UPD_COUNT_OLD_SPB_1) > 0) {
			if (!CheckUtils.isEmpty(initDto.getHdnShatakuKanriNo())) {
				// 社宅駐車場区画情報マスタ元（区画１）更新日
				initDto.setHdnShatakuParkingBlockOld1UpdateDate(dateFormat.format(sysDateTime));
			} else {
				// 社宅駐車場区画情報マスタ元（区画１）更新日
				initDto.setHdnShatakuParkingBlockOld11UpdateDate(dateFormat.format(sysDateTime));
			}
		}
		if (updCountMap.get(Skf3022Sc006CommonDto.UPDATE_COUNTER.UPD_COUNT_SPB_1) > 0) {
			// 社宅駐車場区画情報マスタ（区画１）更新日
			initDto.setHdnShatakuParkingBlock1UpdateDate(dateFormat.format(sysDateTime));
		}
		if (updCountMap.get(Skf3022Sc006CommonDto.UPDATE_COUNTER.UPD_COUNT_OLD_SPB_2) > 0) {
			if (!CheckUtils.isEmpty(initDto.getHdnShatakuKanriNo())) {
				// 社宅駐車場区画情報マスタ元（区画２）更新日
				initDto.setHdnShatakuParkingBlockOld2UpdateDate(dateFormat.format(sysDateTime));
			} else {
				// 社宅駐車場区画情報マスタ元（区画２）更新日
				initDto.setHdnShatakuParkingBlockOld21UpdateDate(dateFormat.format(sysDateTime));
			}
		}
		if (updCountMap.get(Skf3022Sc006CommonDto.UPDATE_COUNTER.UPD_COUNT_SPB_2) > 0) {
			// 社宅駐車場区画情報マスタ（区画２）更新日
			initDto.setHdnShatakuParkingBlock2UpdateDate(dateFormat.format(sysDateTime));
		}
		if (updCountMap.get(Skf3022Sc006CommonDto.UPDATE_COUNTER.UPD_COUNT_RP) > 0) {
			// 使用料パターンID（使用料月額の存在有無で判断）
			if (CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.SHATAKU_GETSUGAKU))) {
				initDto.setHdnSiyouryoIdOld("");
				initDto.setHdnSiyouryoId("");
			} else {
				initDto.setHdnSiyouryoIdOld(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.RENTAL_PATTERNID));
				initDto.setHdnSiyouryoId(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.RENTAL_PATTERNID));
			}
			initDto.setHdnShiyoryoKeisanPatternId("");
		}
	}
}

