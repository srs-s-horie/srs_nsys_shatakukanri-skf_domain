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

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetNyukyoTeijiNoDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetNyukyoTeijiNoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetRentalPatternInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetRentalPatternInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3022TTeijiData;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetNyukyoTeijiNoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetRentalPatternInfoExpRepository;
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
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc006.Skf3022Sc006TmpSaveDto;
import jp.co.intra_mart.mirage.integration.guice.Transactional;

/**
 * Skf3022Sc006TmpSaveService 提示データ登録画面：一時保存ボタン押下時処理クラス。　 
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf3022Sc006TmpSaveService extends SkfServiceAbstract<Skf3022Sc006TmpSaveDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3022Sc006SharedService skf3022Sc006SharedService;
	@Autowired
	private Skf3022Sc006GetNyukyoTeijiNoExpRepository skf3022Sc006GetNyukyoTeijiNoExpRepository;
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	@Autowired
	private Skf3022Sc006GetRentalPatternInfoExpRepository skf3022Sc006GetRentalPatternInfoExpRepository;
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
	public Skf3022Sc006TmpSaveDto index(Skf3022Sc006TmpSaveDto initDto) throws Exception {

		initDto.setPageTitleKey(MessageIdConstant.SKF3022_SC006_TITLE);
		// デバッグログ
		LogUtils.debugByMsg("一時保存");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("一時保存", CodeConstant.C001, FunctionIdConstant.SKF3022_SC006);

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
//		// 可変ラベルリスト
//		List<Map<String, Object>> labelList = new ArrayList<Map<String, Object>>();
//		labelList.addAll(skf3022Sc006SharedService.jsonArrayToArrayList(initDto.getJsonLabelList()));

		// エラーコントロールクリア
		skf3022Sc006SharedService.clearVaridateErr(initDto);
//		// 非活性制御クリア
//		skf3022Sc006SharedService.setDisableCtrlAll(false, initDto);
//		// 現在のラベル値をDTOに設定
//		skf3022Sc006SharedService.setErrVariableLabel(labelList, initDto);
		// 備品は再取得しない
//		initDto.setBihinItiranFlg(false);
		initDto.setBihinItiranFlg(true);

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
		initDto.setHdnTabIndex("999");
		// 処理状態クリア
		initDto.setSc006Status("");

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
						// 備品提示ステータス判定
						switch (initDto.getHdnBihinTeijiStatus()) {
							// 備品提示ステータスが作成中／作成済み
							case CodeConstant.BIHIN_STATUS_SAKUSEI_CHU:
							case CodeConstant.BIHIN_STATUS_SAKUSEI_SUMI:
								// 備品ステータスチェック
								appBihinFlg = skf3022Sc006SharedService.checkBihinTaiyoStts(initDto);
								break;
						};
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
			if (skf3022Sc006SharedService.checkForNyukyo(true, bihinErrFlg, appBihinFlg, initDto)) {
//				// 画面ステータス設定
//				skf3022Sc006SharedService.pageLoadComplete(initDto);
				return initDto;
			}
		// 退居の場合
		} else if (Objects.equals(codeCacheUtils.getGenericCodeName(FunctionIdConstant.GENERIC_CODE_NYUTAIKYO_KBN,
					CodeConstant.NYUTAIKYO_KBN_TAIKYO), initDto.getSc006NyutaikyoKbn())) {
			// 退居入力チェック
			if (skf3022Sc006SharedService.checkForTaikyo(true, bihinErrFlg, initDto)) {
//				// 画面ステータス設定
//				skf3022Sc006SharedService.pageLoadComplete(initDto);
				return initDto;
			}
		// 変更の場合
		} else {
			// 変更入力チェック
			if (skf3022Sc006SharedService.checkForHenko(true, initDto)) {
//				// 画面ステータス設定
//				skf3022Sc006SharedService.pageLoadComplete(initDto);
				return initDto;
			}
		}
		// DB更新
		update(initDto);
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
	private void update(Skf3022Sc006TmpSaveDto initDto) {

		// 日付フォーマット
		SimpleDateFormat dateFormat = new SimpleDateFormat(Skf3022Sc006CommonDto.DATE_FORMAT);
		// システム日時の取得
		Date sysDateTime = skfBaseBusinessLogicUtils.getSystemDateTime();
//		// メッセージ
////		Dim message As String = String.Empty
//		String message = "";
//		// 提示データ更新件数
//		int updCountTJ = 0;
//		// 入退居予定データ更新件数
//		int updCountNY = 0;
//		// 提示備品データ更新件数
//		int updCountTBD = 0;
//		// 社宅部屋情報マスタ更新件数
//		int updCountOldSR = 0;
//		int updCountSR = 0;
//		// 社宅駐車場区画情報マスタ更新件数
//		int updCountOldSPB1 = 0;
//		int updCountSPB1 = 0;
//		int updCountOldSPB2 = 0;
//		int updCountSPB2 = 0;
//		// 使用料パターン更新件数
//		int updCountRP = 0;
		// 更新モード
		Boolean updateMode = true;
		// 社員番号
		String shainNo = CheckUtils.isEmpty(initDto.getSc006ShainNo()) ? "" : initDto.getSc006ShainNo().replace(CodeConstant.ASTERISK, "");
		// 継続ボタン押下フラグ判定
		if (initDto.getHdnKeizokuBtnFlg()) {
			// 入居の提示データを取得
			List<Skf3022Sc006GetNyukyoTeijiNoDataExp> nyukyoTeijiNoList = new ArrayList<Skf3022Sc006GetNyukyoTeijiNoDataExp>();
			Skf3022Sc006GetNyukyoTeijiNoExpParameter param = new Skf3022Sc006GetNyukyoTeijiNoExpParameter();
			Long shatakuKanriNo = CheckUtils.isEmpty(initDto.getHdnShatakuKanriNoOld()) ? null : Long.parseLong(initDto.getHdnShatakuKanriNoOld());
			Long shatakuRoomKanriNo = CheckUtils.isEmpty(initDto.getHdnRoomKanriNoOld()) ? null : Long.parseLong(initDto.getHdnRoomKanriNoOld());
			if (shatakuKanriNo != null && shatakuRoomKanriNo != null) {
				param.setShatakuKanriNo(shatakuKanriNo);
				param.setShatakuRoomKanriNo(shatakuRoomKanriNo);
				param.setShainNo(shainNo);
				nyukyoTeijiNoList = skf3022Sc006GetNyukyoTeijiNoExpRepository.getNyukyoTeijiNo(param);
			}
			// 継続登録場合、退居提示データの提示番号を設定する
			if (CheckUtils.isEmpty(initDto.getHdnTeijiNoOld())) {
				initDto.setHdnTeijiNoOld(initDto.getHdnTeijiNo());
			}
			// 取得結果判定
			if (nyukyoTeijiNoList.size() < 1) {
				updateMode = false;
			} else {
				// 提示番号更新
				initDto.setHdnTeijiNo(nyukyoTeijiNoList.get(0).getTeijiNo().toString());
				// 
				initDto.setHdnTeijiDataUpdateDate(dateFormat.format(nyukyoTeijiNoList.get(0).getUpdateDate()));
			}
		}
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
//		If Constant.NyutaikyoKbn.HENKO.Equals(Me.hdnNyutaikyoKbnOld.Value) And _
//		   Constant.NyutaikyoKbn.TAIKYO.Equals(Me.hdnNyutaikyoKbn.Value) Then
		if (CodeConstant.NYUTAIKYO_KBN_HENKO.equals(initDto.getHdnNyutaikyoKbnOld())
				&& CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(initDto.getHdnNyutaikyoKbn())) {
//			param.Add(Me.hdnNyutaikyoKbnOld.Value)
			paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.NYUTAIKYO_KBN, initDto.getHdnNyutaikyoKbnOld());
//		Else
		} else {
//			param.Add(Me.hdnNyutaikyoKbn.Value)
			paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.NYUTAIKYO_KBN, initDto.getHdnNyutaikyoKbn());
//		End If
		}
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
		// 一時保存
//		param.Add(TMP_SAVE)
		paramMap.put(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.BUTTON_FLG, Skf3022Sc006CommonDto.TMP_SAVE);
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
		// 一時保存処理
//		Select Case S2007_TeijiDataRegistBusinessLogic.TmpSaveAndCreateAndShatakuLogin( _
//										param, _
//										updateMode, _
//										Me.hdnTeijiNoOld.Value, _
//										False, _
//										Me.GetColumnInfoList(TMP_SAVE, updateMode, sysDateTime), _
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
						Skf3022Sc006CommonDto.TMP_SAVE, updateMode, sysDateTime, initDto);
		int updateCnt = 0;
		try {
			updateCnt = skf3022Sc006SharedService.tmpSaveAndCreateAndShatakuLogin(
					paramMap,
					updateMode,
					initDto.getHdnTeijiNoOld(),
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
			// 更新時にエラーが発生しました。ヘルプデスクへ連絡してください。
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1075);
			LogUtils.infoByMsg("update, " + e.toString());
			// ロールバック
			throwBusinessExceptionIfErrors(initDto.getResultMessages());
		}
		// 結果判定
		switch (updateCnt) {
			case 0:
				LogUtils.infoByMsg("update, 更新時異常が発生");
				// 更新時にエラーが発生しました。ヘルプデスクへ連絡してください。
				ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1075);
				// ロールバック
				throwBusinessExceptionIfErrors(initDto.getResultMessages());
			case -1:
				LogUtils.infoByMsg("update, 排他検知");
				// 他のユーザによって更新されています。もう一度画面を更新してください。
				ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.W_SKF_1009);
				// ロールバック
				throwBusinessExceptionIfErrors(initDto.getResultMessages());
			default:
				break;
		};
//			Case 0
//				message = Com_MessageManager.GetScreenMsgInfo(Constant.MessageScreenId.S00002)
//				Me.hdnFieldMessage.Value = HttpUtility.HtmlEncode(message)
//				Return
//			Case -1
//				message = Com_MessageManager.GetScreenMsgInfo(Constant.MessageScreenId.S00005)
//				Me.hdnFieldMessage.Value = HttpUtility.HtmlEncode(message)
//				Return
//			Case Else
//				'エラー背景色をクリアする
//				Me.clearTabError(Me.UpdatePanel1)
//				Me.clearTabError(Me.UpdatePanel2)
//				Me.clearTabError(Me.UpdatePanel3)
		// 使用料項目の再設定
//				Me.ResetShiyoryoInfo()
		skf3022Sc006SharedService.resetShiyoryoInfo(initDto);

		// 提示データ更新件数判定
//				If 0 < updCountTJ Then
		if (updCountMap.get(Skf3022Sc006CommonDto.UPDATE_COUNTER.UPD_COUNT_TJ) > 0) {
			// 備品提示スタータスの設定
//					If Constant.ShatakuTeijiStatusKbn.DOI_SUMI.Equals(Me.hdnShatakuTeijiStatus.Value) Or _
//						Constant.ShatakuTeijiStatusKbn.SHONIN.Equals(Me.hdnShatakuTeijiStatus.Value) Then
			if (CodeConstant.PRESENTATION_SITUATION_DOI_SUMI.equals(initDto.getHdnShatakuTeijiStatus())
					|| CodeConstant.PRESENTATION_SITUATION_SHONIN.equals(initDto.getHdnShatakuTeijiStatus())) {
				// 備品提示ステータス
//						Me.hdnBihinTeijiStatus.Value = Constant.BihinTeijiStatusKbn.SAKUSEI_CHU
				initDto.setHdnBihinTeijiStatus(CodeConstant.BIHIN_STATUS_SAKUSEI_CHU);
//						Me.SetBihinTeijiStatusCss(Me.lblBihinStts, Me.hdnBihinTeijiStatus.Value)
				// 備品提示ステータスの文字色を設定する
				initDto.setSc006BihinSttsColor(
						skf3022Sc006SharedService.setBihinTeijiStatusCss(initDto.getHdnBihinTeijiStatus()));
//						Me.lblBihinStts.Text = Me.GetDispValue(Constant.BihinTeijiStatusKbn.SAKUSEI_CHU, _
//															   Constant.StatusWordSettingId.BIHIN_TEIJI_STATUS_KBN, _
//															   Constant.SettingsId.XML_COM_STATUS)
				// 備品提示ステータス(作成中)
				initDto.setSc006BihinStts(codeCacheUtils.getGenericCodeName(
						FunctionIdConstant.GENERIC_CODE_BIHINTEIJISTATUS_KBN, CodeConstant.BIHIN_STATUS_SAKUSEI_CHU));
			} else {
				// 社宅提示ステータス
//						Me.hdnShatakuTeijiStatus.Value = Constant.ShatakuTeijiStatusKbn.SAKUSEI_CHU
				initDto.setHdnShatakuTeijiStatus(CodeConstant.PRESENTATION_SITUATION_SAKUSEI_CHU);
			}
			// 提示データ更新日
//					Me.hdnTeijiDataUpdateDate.Value = DateUtil.ConversionFormatYYYYMMDDHHMMSSF6WithDelimiter(sysDateTime)
			initDto.setHdnTeijiDataUpdateDate(dateFormat.format(sysDateTime));
		}
		// 退居の場合、備品一覧情報の再取得
//		List<Skf3022Sc006GetTeijiBihinDataExp> roomBihinList = new ArrayList<Skf3022Sc006GetTeijiBihinDataExp>();
//				If Constant.NyutaikyoKbn.TAIKYO.Equals(Me.hdnNyutaikyoKbn.Value) Then
		if (CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(initDto.getHdnNyutaikyoKbn())) {
//					Me.GetBihinData(Me.hdnShatakuKanriNo.Value, _
//									Me.hdnHeyaKanriNo.Value, _
//									Me.hdnNyutaikyoKbn.Value)
//			roomBihinList = skf3022Sc006SharedService.getBihinData(
//					initDto.getHdnShatakuKanriNo(), initDto.getHdnShatakuKanriNoOld(),
//					initDto.getHdnRoomKanriNo(), initDto.getHdnRoomKanriNoOld(),
//					initDto.getHdnNyutaikyoKbn(), initDto.getHdnTeijiNo(), initDto.getHdnShatakuHeyaFlg());
			initDto.setBihinItiranFlg(true);	// ←これで良いのでは？
		}
		// 社宅と部屋のクリア
//				If String.IsNullOrEmpty(Me.hdnHeyaKanriNo.Value) Then
		if (CheckUtils.isEmpty(initDto.getHdnRoomKanriNo())) {
//					Me.hdnHeyaKanriNoOld.Value = String.Empty
//					Me.hdnShatakuKanriNoOld.Value = String.Empty
			initDto.setHdnRoomKanriNoOld("");
			initDto.setHdnShatakuKanriNoOld("");
		} else if (updCountMap.get(Skf3022Sc006CommonDto.UPDATE_COUNTER.UPD_COUNT_SR) > 0) {
//					Me.hdnHeyaKanriNoOld.Value = Me.hdnHeyaKanriNo.Value
//					Me.hdnShatakuKanriNoOld.Value = Me.hdnShatakuKanriNo.Value
			initDto.setHdnRoomKanriNoOld(initDto.getHdnRoomKanriNo());
			initDto.setHdnShatakuKanriNoOld(initDto.getHdnShatakuKanriNo());
			// 社宅部屋情報マスタ元更新日
//					Me.hdnShatakuRoomOldUpdateDate.Value = DateUtil.ConversionFormatYYYYMMDDHHMMSSF6WithDelimiter(sysDateTime)
			initDto.setHdnShatakuRoomOldUpdateDate(dateFormat.format(sysDateTime));
		}
		// 駐車場１のクリア
//				If String.IsNullOrEmpty(Me.hdnChushajoNoOne.Value) Then
		if (CheckUtils.isEmpty(initDto.getHdnChushajoNoOne())) {
//					Me.hdnChushajoNoOneOld.Value = String.Empty
			initDto.setHdnChushajoNoOneOld("");
//				ElseIf 0 < updCountSPB1 Then
		} else if (updCountMap.get(Skf3022Sc006CommonDto.UPDATE_COUNTER.UPD_COUNT_SPB_1) > 0) {
//					Me.hdnChushajoNoOneOld.Value = Me.hdnChushajoNoOne.Value
			initDto.setHdnChushajoNoOneOld(initDto.getHdnChushajoNoOne());
			// 社宅駐車場区画情報マスタ元（区画１）更新日
//					Me.hdnShatakuParkingBlockOld1UpdateDate.Value = DateUtil.ConversionFormatYYYYMMDDHHMMSSF6WithDelimiter(sysDateTime)
			initDto.setHdnShatakuParkingBlockOld1UpdateDate(dateFormat.format(sysDateTime));
		}
		// 駐車場２のクリア
//				If String.IsNullOrEmpty(Me.hdnChushajoNoTwo.Value) Then
		if (CheckUtils.isEmpty(initDto.getHdnChushajoNoTwo())) {
//					Me.hdnChushajoNoTwoOld.Value = String.Empty
			initDto.setHdnChushajoNoTwoOld("");
		} else if (updCountMap.get(Skf3022Sc006CommonDto.UPDATE_COUNTER.UPD_COUNT_SPB_2) > 0) {
//					Me.hdnChushajoNoTwoOld.Value = Me.hdnChushajoNoTwo.Value
			initDto.setHdnChushajoNoTwoOld(initDto.getHdnChushajoNoTwo());
			// 社宅駐車場区画情報マスタ元（区画２）更新日
//					Me.hdnShatakuParkingBlockOld2UpdateDate.Value = DateUtil.ConversionFormatYYYYMMDDHHMMSSF6WithDelimiter(sysDateTime)
			initDto.setHdnShatakuParkingBlockOld2UpdateDate(dateFormat.format(sysDateTime));
		}
//				If 0 < updCountNY Then
		if (updCountMap.get(Skf3022Sc006CommonDto.UPDATE_COUNTER.UPD_COUNT_NY) > 0) {
			// 入退居予定データ更新日
//					Me.hdnNyutaikyoYoteiUpdateDate.Value = DateUtil.ConversionFormatYYYYMMDDHHMMSSF6WithDelimiter(sysDateTime)
			initDto.setHdnNyutaikyoYoteiUpdateDate(dateFormat.format(sysDateTime));
		}
//				If 0 < updCountOldSR Then
		if (updCountMap.get(Skf3022Sc006CommonDto.UPDATE_COUNTER.UPD_COUNT_OLD_SR) > 0) {
			// 社宅部屋情報マスタ元更新日
//					Me.hdnShatakuRoomOldUpdateDate.Value = DateUtil.ConversionFormatYYYYMMDDHHMMSSF6WithDelimiter(sysDateTime)
			initDto.setHdnShatakuRoomOldUpdateDate(dateFormat.format(sysDateTime));
		}
//				If 0 < updCountSR Then
		if (updCountMap.get(Skf3022Sc006CommonDto.UPDATE_COUNTER.UPD_COUNT_SR) > 0) {
			// 社宅部屋情報マスタ更新日
//					Me.hdnShatakuRoomUpdateDate.Value = DateUtil.ConversionFormatYYYYMMDDHHMMSSF6WithDelimiter(sysDateTime)
			initDto.setHdnShatakuRoomUpdateDate(dateFormat.format(sysDateTime));
		}
//				If 0 < updCountOldSPB1 Then
		if (updCountMap.get(Skf3022Sc006CommonDto.UPDATE_COUNTER.UPD_COUNT_OLD_SPB_1) > 0) {
//					If Not String.IsNullOrEmpty(Me.hdnShatakuKanriNo.Value) Then
			if (!CheckUtils.isEmpty(initDto.getHdnShatakuKanriNo())) {
				// 社宅駐車場区画情報マスタ元（区画１）更新日
//						Me.hdnShatakuParkingBlockOld1UpdateDate.Value = DateUtil.ConversionFormatYYYYMMDDHHMMSSF6WithDelimiter(sysDateTime)
				initDto.setHdnShatakuParkingBlockOld1UpdateDate(dateFormat.format(sysDateTime));
			} else {
				// 社宅駐車場区画情報マスタ元（区画１）更新日
//						Me.hdnShatakuParkingBlockOld11UpdateDate.Value = DateUtil.ConversionFormatYYYYMMDDHHMMSSF6WithDelimiter(sysDateTime)
				initDto.setHdnShatakuParkingBlockOld11UpdateDate(dateFormat.format(sysDateTime));
			}
		}
//				If 0 < updCountSPB1 Then
		if (updCountMap.get(Skf3022Sc006CommonDto.UPDATE_COUNTER.UPD_COUNT_SPB_1) > 0) {
			// 社宅駐車場区画情報マスタ（区画１）更新日
//					Me.hdnShatakuParkingBlock1UpdateDate.Value = DateUtil.ConversionFormatYYYYMMDDHHMMSSF6WithDelimiter(sysDateTime)
			initDto.setHdnShatakuParkingBlock1UpdateDate(dateFormat.format(sysDateTime));
		}
//				If 0 < updCountOldSPB2 Then
		if (updCountMap.get(Skf3022Sc006CommonDto.UPDATE_COUNTER.UPD_COUNT_OLD_SPB_2) > 0) {
//					If Not String.IsNullOrEmpty(Me.hdnShatakuKanriNo.Value) Then
			if (!CheckUtils.isEmpty(initDto.getHdnShatakuKanriNo())) {
				// 社宅駐車場区画情報マスタ元（区画２）更新日
//						Me.hdnShatakuParkingBlockOld2UpdateDate.Value = DateUtil.ConversionFormatYYYYMMDDHHMMSSF6WithDelimiter(sysDateTime)
				initDto.setHdnShatakuParkingBlockOld2UpdateDate(dateFormat.format(sysDateTime));
			} else {
				// 社宅駐車場区画情報マスタ元（区画２）更新日
//						Me.hdnShatakuParkingBlockOld21UpdateDate.Value = DateUtil.ConversionFormatYYYYMMDDHHMMSSF6WithDelimiter(sysDateTime)
				initDto.setHdnShatakuParkingBlockOld21UpdateDate(dateFormat.format(sysDateTime));
			}
		}
//				If 0 < updCountSPB2 Then
		if (updCountMap.get(Skf3022Sc006CommonDto.UPDATE_COUNTER.UPD_COUNT_SPB_2) > 0) {
			// 社宅駐車場区画情報マスタ（区画２）更新日
//					Me.hdnShatakuParkingBlock2UpdateDate.Value = DateUtil.ConversionFormatYYYYMMDDHHMMSSF6WithDelimiter(sysDateTime)
			initDto.setHdnShatakuParkingBlock2UpdateDate(dateFormat.format(sysDateTime));
		}
//				If 0 < updCountRP Then
		if (updCountMap.get(Skf3022Sc006CommonDto.UPDATE_COUNTER.UPD_COUNT_RP) > 0) {
			// 使用料パターンID（使用料月額の存在有無で判断）
//					If String.IsNullOrEmpty(rentalPatternTorokuList(14)) Then
			if (CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.SHATAKU_GETSUGAKU))) {
//						Me.hdnSiyouryoIdOld.Value = String.Empty
//						Me.hdnSiyouryoId.Value = String.Empty
				initDto.setHdnSiyouryoIdOld("");
				initDto.setHdnSiyouryoId("");
			} else {
//						Me.hdnSiyouryoIdOld.Value = rentalPatternTorokuList(1)
//						Me.hdnSiyouryoId.Value = rentalPatternTorokuList(1)
				initDto.setHdnSiyouryoIdOld(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.RENTAL_PATTERNID));
				initDto.setHdnSiyouryoId(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.RENTAL_PATTERNID));
			}
//					Me.hdnShiyoryoKeisanPatternId.Value = rentalPatternTorokuList(1)
			initDto.setHdnShiyoryoKeisanPatternId(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.RENTAL_PATTERNID));
			// 使用料パターン排他更新日
//					Me.hdnRentalPatternUpdateDate.Value = HttpUtility.HtmlEncode( _
//									DateUtil.ConversionFormatYYYYMMDDHHMMSSF6WithDelimiter( _
//										S2007_TeijiDataRegistBusinessLogic.GetRentalPatternInfo(Me.hdnShiyoryoKeisanPatternId.Value)(0).UPDATE_DATE))
			List<Skf3022Sc006GetRentalPatternInfoExp> rentalPatternInfoList = new ArrayList<Skf3022Sc006GetRentalPatternInfoExp>();
			Skf3022Sc006GetRentalPatternInfoExpParameter param = new Skf3022Sc006GetRentalPatternInfoExpParameter();
			param.setRentalPatternId(Long.parseLong(initDto.getHdnShiyoryoKeisanPatternId()));

			// 使用料パターン情報取得
			rentalPatternInfoList = skf3022Sc006GetRentalPatternInfoExpRepository.getRentalPatternInfo(param);
			initDto.setHdnRentalPatternUpdateDate(dateFormat.format(rentalPatternInfoList.get(0).getUpdateDate()));
		}
	}
}

