/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc006;

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
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022Sc006common.Skf3022Sc006CommonDto;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc006.Skf3022Sc006CreateDto;
import jp.co.intra_mart.mirage.integration.guice.Transactional;

/**
 * Skf3022Sc006CreateService 提示データ登録画面：作成完了ボタン押下時処理クラス。　 
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf3022Sc006CreateService extends BaseServiceAbstract<Skf3022Sc006CreateDto> {

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
	public Skf3022Sc006CreateDto index(Skf3022Sc006CreateDto initDto) throws Exception {

		initDto.setPageTitleKey(MessageIdConstant.SKF3022_SC006_TITLE);
		// デバッグログ
		LogUtils.debugByMsg("作成完了");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("作成完了", CodeConstant.C001, FunctionIdConstant.SKF3022_SC006);

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
//		// 非活性制御クリア
//		skf3022Sc006SharedService.setDisableCtrlAll(false, initDto);
//		// 現在のラベル値をDTOに設定
//		skf3022Sc006SharedService.setErrVariableLabel(labelList, initDto);
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
		initDto.setHdnTabIndex("999");

		// チェック処理
		Boolean bihinErrFlg = false;
		Boolean appBihinFlg = false;
		// 備品ステータスのチェック処理
		// 入退居区分が"入居"の場合
		if (CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(initDto.getHdnNyutaikyoKbn())) {
			// 駐車場申請以外の場合			
			if (!CodeConstant.SHINSEI_KBN_PARKING.equals(initDto.getHdnApplKbn())) {
				// 社宅提示ステータス判定
				switch (initDto.getHdnShatakuTeijiStatus()) {
					// 社宅提示ステータス：同意済/承認済み
					case CodeConstant.PRESENTATION_SITUATION_DOI_SUMI:
					case CodeConstant.PRESENTATION_SITUATION_SHONIN:
						if (!CheckUtils.isEmpty(initDto.getHdnBihinTeijiStatus())) {
							// 備品提示ステータス判定
							switch (initDto.getHdnBihinTeijiStatus()) {
								// 備品提示ステータスが作成中／作成済み
								case CodeConstant.BIHIN_STATUS_SAKUSEI_CHU:
								case CodeConstant.BIHIN_STATUS_SAKUSEI_SUMI:
									// 備品ステータスチェック
									appBihinFlg = skf3022Sc006SharedService.checkBihinTaiyoStts(initDto);
									break;
							};
						}
						break;
				};
			}
		} else {
			// 備品ステータスのチェック処理
			appBihinFlg = skf3022Sc006SharedService.checkBihinTaiyoStts(initDto);
		}
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
		// 提示データ一覧画面へ遷移
		TransferPageInfo prevPage = TransferPageInfo.prevPage(FunctionIdConstant.SKF3022_SC005, "init");
		initDto.setTransferPageInfo(prevPage);

		return initDto;
	}

	@Transactional
	private void update(Skf3022Sc006CreateDto initDto) {

		// 社員番号
		String shainNo = CheckUtils.isEmpty(initDto.getSc006ShainNo()) ? "" : initDto.getSc006ShainNo().replace(CodeConstant.ASTERISK, "");
		// システム日時の取得
		Date sysDateTime = skfBaseBusinessLogicUtils.getSystemDateTime();
//
//		Dim param As List(Of String) = New List(Of String)()
		Map<Skf3022Sc006CommonDto.TEIJIDATA_PARAM, String> paramMap
						= new HashMap<Skf3022Sc006CommonDto.TEIJIDATA_PARAM, String>();
		// 提示番号
//		param.Add(Me.hdnTeijiNo.Value)
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.TEIJI_NO, initDto.getHdnTeijiNo());
		// 社員番号
//		param.Add(Me.lblShainNo.Text.Replace(Constant.Sign.ASTERISK, String.Empty))
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHAIN_NO, shainNo);
		// 入退居区分
//		param.Add(Me.hdnNyutaikyoKbn.Value)
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.NYUTAIKYO_KBN, initDto.getHdnNyutaikyoKbn());
		// 社宅管理番号元
//		param.Add(Me.hdnShatakuKanriNoOld.Value)
		paramMap.put(
				Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKU_KANRINO_OLD, initDto.getHdnShatakuKanriNoOld());
		// 部屋管理番号元
//		param.Add(Me.hdnHeyaKanriNoOld.Value)
		paramMap.put(
				Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKU_ROOMKANRINO_OLD, initDto.getHdnRoomKanriNoOld());
		// 駐車場管理番号1元
//		param.Add(Me.hdnChushajoNoOneOld.Value)
		paramMap.put(
				Skf3022Sc006CommonDto.TEIJIDATA_PARAM.CHUSHAJONO_ONE_OLD, initDto.getHdnChushajoNoOneOld());
		// 駐車場管理番号2元
//		param.Add(Me.hdnChushajoNoTwoOld.Value)
		paramMap.put(
				Skf3022Sc006CommonDto.TEIJIDATA_PARAM.CHUSHAJONO_TWO_OLD, initDto.getHdnChushajoNoTwoOld());
		// 社宅管理番号
//		param.Add(Me.hdnShatakuKanriNo.Value)
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKU_KANRINO, initDto.getHdnShatakuKanriNo());
		// 部屋管理番号
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKU_ROOMKANRINO, initDto.getHdnRoomKanriNo());
		// 駐車場管理番号1
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.CHUSHAJONO_ONE, initDto.getHdnChushajoNoOne());
		// 駐車場管理番号2
//		param.Add(Me.hdnChushajoNoTwo.Value)
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.CHUSHAJONO_TWO, initDto.getHdnChushajoNoTwo());
		// 入居予定日
//		param.Add(Me.GetDateText(Me.txtNyukyoYoteiDay.Text))
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.NYUKYOYOTEI_DATE,
				skf3022Sc006SharedService.getDateText(initDto.getSc006NyukyoYoteiDay()));
		// 退居予定日
//		param.Add(Me.GetDateText(Me.txtTaikyoYoteiDay.Text))
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.TAIKYOYOTEI_DATE,
				skf3022Sc006SharedService.getDateText(initDto.getSc006TaikyoYoteiDay()));
		// 作成完了
//		param.Add(CREATE)
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.BUTTON_FLG, Skf3022Sc006CommonDto.CREATE);
		// 提示データ更新日
//		param.Add(Me.hdnTeijiDataUpdateDate.Value)
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.TEIJIDATA_UPDATEDATE,
												initDto.getHdnTeijiDataUpdateDate());
		// 入退居予定更新日
//		param.Add(Me.hdnNyutaikyoYoteiUpdateDate.Value)
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.NYUTAIKYOYOTEI_UPDATEDATE,
													initDto.getHdnNyutaikyoYoteiUpdateDate());
		// 社宅部屋情報マスタ元更新日
//		param.Add(Me.hdnShatakuRoomOldUpdateDate.Value)
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKUROOM_OLD_UPDATEDATE,
												initDto.getHdnShatakuRoomOldUpdateDate());
		// 社宅部屋情報マスタ更新日
//		param.Add(Me.hdnShatakuRoomUpdateDate.Value)
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKUROOM_UPDATEDATE,
												initDto.getHdnShatakuRoomUpdateDate());
		// 社宅駐車場区画情報マスタ元更新日（区画１）
//		param.Add(Me.hdnShatakuParkingBlockOld1UpdateDate.Value)
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKU_PARKINGBLOCK_OLD1_UPDATEDATE,
												initDto.getHdnShatakuParkingBlockOld1UpdateDate());
		// 社宅駐車場区画情報マスタ元更新日（区画１）
//		param.Add(Me.hdnShatakuParkingBlockOld11UpdateDate.Value)
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKU_PARKINGBLOCK_OLD11_UPDATEDATE,
												initDto.getHdnShatakuParkingBlockOld11UpdateDate());
		// 社宅駐車場区画情報マスタ更新日（区画１）
//		param.Add(Me.hdnShatakuParkingBlock1UpdateDate.Value)
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKU_PARKINGBLOCK_1_UPDATEDATE,
												initDto.getHdnShatakuParkingBlock1UpdateDate());
		// 社宅駐車場区画情報マスタ元更新日（区画２）
//		param.Add(Me.hdnShatakuParkingBlockOld2UpdateDate.Value)
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKU_PARKINGBLOCK_OLD2_UPDATEDATE,
												initDto.getHdnShatakuParkingBlockOld2UpdateDate());
		// 社宅駐車場区画情報マスタ元更新日（区画２）
//		param.Add(Me.hdnShatakuParkingBlockOld21UpdateDate.Value)
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKU_PARKINGBLOCK_OLD21_UPDATEDATE,
												initDto.getHdnShatakuParkingBlockOld21UpdateDate());
		// 社宅駐車場区画情報マスタ更新日（区画２）
//		param.Add(Me.hdnShatakuParkingBlock2UpdateDate.Value)
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKU_PARKINGBLOCK_2_UPDATEDATE,
												initDto.getHdnShatakuParkingBlock2UpdateDate());
//		'更新者
//		param.Add(HttpUtility.HtmlEncode(MyBase.userInfo.UserId()))
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.UPDATE_USER_ID, LoginUserInfoUtils.getUserCd());
//		'更新機能ID
//		param.Add(HttpUtility.HtmlEncode(MyBase.publicInfo.IpAddress()))
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.UPDATE_PROGRAM_ID, initDto.getPageId());
		// 使用料変更フラグ
//		param.Add(Me.hdnSiyouryoFlg.Value)
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHIYOURYO_FLG, initDto.getHdnSiyouryoFlg());
//
//		'提示備品一覧情報設定
//		Me.SetBihinList()
//
		// 使用料パターン一覧情報設定
//		Dim rentalPatternUpdateDate As String = Me.hdnRentalPatternUpdateDate.Value
		String rentalPatternUpdateDate = initDto.getHdnRentalPatternUpdateDate();
//		Dim rentalPatternUpdateDateForRegist As Date = MyBase.GetSystemDate()
//		Dim rentalPatternTorokuList As List(Of String)
		Map<Skf3022Sc006CommonDto.RENTAL_PATTERN, String> rentalPatternTorokuList =
							new HashMap<Skf3022Sc006CommonDto.RENTAL_PATTERN, String>();
//		rentalPatternTorokuList = Me.SetRentalPatternList()
		rentalPatternTorokuList = skf3022Sc006SharedService.setRentalPatternList(initDto);
		// 更新カウンタMap
		Map<Skf3022Sc006CommonDto.UPDATE_COUNTER, Integer> updCountMap =
				new HashMap<Skf3022Sc006CommonDto.UPDATE_COUNTER, Integer>();
		// 更新提示データ情報
		Skf3022TTeijiData columnInfoList = skf3022Sc006SharedService.getColumnInfoList(
						Skf3022Sc006CommonDto.CREATE, true, sysDateTime, initDto);
		int updateCnt = 0;
		try {
			updateCnt = skf3022Sc006SharedService.tmpSaveAndCreateAndShatakuLogin(
					paramMap,
					true,
					"",
					true,
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
			// 更新時にエラーが発生しました。ヘルプデスクへ連絡してください。
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1075);
			LogUtils.infoByMsg("update, 更新時に例外発生：" + e.toString());
			// ロールバック
			throwBusinessExceptionIfErrors(initDto.getResultMessages());
		}
		// 結果判定
		switch (updateCnt) {
			case 0:
				// 更新時にエラーが発生しました。ヘルプデスクへ連絡してください。
				ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1075);
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
	}
}

