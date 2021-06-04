/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc006;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetShatakuRoomExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetShatakuRoomExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetShatakuRoomExpRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022Sc006common.Skf3022Sc006CommonDto;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc006.Skf3022Sc006ShatakuSupportCallBackDto;

/**
 * Skf3022Sc006ShatakuSupportCallBackService 提示データ登録画面の社宅部屋入力支援コールバック時サービス処理クラス。　 
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf3022Sc006ShatakuSupportCallBackService extends SkfServiceAbstract<Skf3022Sc006ShatakuSupportCallBackDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3022Sc006SharedService skf3022Sc006SharedService;
	@Autowired
	private Skf3022Sc006GetShatakuRoomExpRepository skf3022Sc006GetShatakuRoomExpRepository;

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
	public Skf3022Sc006ShatakuSupportCallBackDto index(Skf3022Sc006ShatakuSupportCallBackDto initDto) throws Exception {

		initDto.setPageTitleKey(MessageIdConstant.SKF3022_SC006_TITLE);

		// デバッグログ
		LogUtils.debugByMsg("社宅部屋入力支援");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("社宅部屋入力支援", CodeConstant.C001, FunctionIdConstant.SKF3022_SC006);

		SimpleDateFormat dateFormat = new SimpleDateFormat(Skf3022Sc006CommonDto.DATE_FORMAT);
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

		// 選択タブインデックス初期値
		initDto.setHdnTabIndex("999");
		// エラーコントロールクリア
		skf3022Sc006SharedService.clearVaridateErr(initDto);
		// 非活性制御クリア
		skf3022Sc006SharedService.setDisableCtrlAll(false, initDto);
		// 現在のラベル値をDTOに設定
		skf3022Sc006SharedService.setErrVariableLabel(labelList, initDto);

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

		// 備品再取得しない
		initDto.setBihinItiranFlg(false);
		// 社宅管理番号／部屋管理番号が変更判定
		if (CheckUtils.isEmpty(initDto.getHdnShatakuKanriNoOld()) || CheckUtils.isEmpty(initDto.getHdnRoomKanriNoOld())
				|| !Objects.equals(initDto.getHdnShatakuKanriNoOld(), initDto.getHdnShatakuKanriNo())
				|| !Objects.equals(initDto.getHdnRoomKanriNoOld(), initDto.getHdnRoomKanriNo())) {
			// 社宅部屋情報マスタを取得
			Long shatakuKanriNo = CheckUtils.isEmpty(initDto.getHdnShatakuKanriNo()) ? null : Long.parseLong(initDto.getHdnShatakuKanriNo());
			Long shatakuRoomKanriNo = CheckUtils.isEmpty(initDto.getHdnRoomKanriNo()) ? null : Long.parseLong(initDto.getHdnRoomKanriNo());
			List<Skf3022Sc006GetShatakuRoomExp> shatakuRoomList = new ArrayList<Skf3022Sc006GetShatakuRoomExp>();
			Skf3022Sc006GetShatakuRoomExpParameter shatakuRoomParam = new Skf3022Sc006GetShatakuRoomExpParameter();
			shatakuRoomParam.setShatakuKanriNo(shatakuKanriNo);
			shatakuRoomParam.setShatakuRoomKanriNo(shatakuRoomKanriNo);
			if (shatakuKanriNo != null && shatakuRoomKanriNo != null) {
				shatakuRoomList = skf3022Sc006GetShatakuRoomExpRepository.getShatakuRoom(shatakuRoomParam);
			}
			// 取得結果判定
			if (shatakuRoomList.size() > 0) {
				// 社宅部屋情報マスタ更新日設定
				initDto.setHdnShatakuRoomUpdateDate(dateFormat.format(shatakuRoomList.get(0).getUpdateDate()));
				// 管理会社設定
				initDto.setSc006KanriKaisya(shatakuRoomList.get(0).getCompanyName());
			}
			// 使用料入力支援ボタンを活性
//		    Me.btnSiyoryoShien.Enabled = True
			initDto.setShiyoryoShienDisableFlg(false);
			// 入居予定日
//		    Me.txtNyukyoYoteiDay.Text = DateUtil.ConvertSlashDateString(Me.hdnNyukyoDay.Value)
			initDto.setSc006NyukyoYoteiDay(initDto.getHdnNyukyoDate());
			// 利用開始日１
//		    Me.txtRiyouStartDayOne.Text = DateUtil.ConvertSlashDateString(Me.hdnRiyouStartDayOne.Value)
			initDto.setSc006RiyouStartDayOne(initDto.getHdnRiyouStartDayOne());
			// 利用開始日２
//		    Me.txtRiyouStartDayTwo.Text = DateUtil.ConvertSlashDateString(Me.hdnRiyouStartDayTwo.Value)
			initDto.setSc006RiyouStartDayTwo(initDto.getHdnRiyouStartDayTwo());
			// 使用料計算パターン
//		    Me.lblSiyoryoPatName.Text = String.Empty
//		    Me.hdnSiyouryoId.Value = String.Empty
//		    Me.lblSiyoryoPatName.BackColor = Color.Empty
			initDto.setSc006SiyoryoPatName(CodeConstant.DOUBLE_QUOTATION);
			initDto.setHdnSiyouryoId(CodeConstant.DOUBLE_QUOTATION);
			// 社宅使用料月額(ヘッダ項目)
//		    Me.lblSiyoryoMonthPay.Text = String.Empty
			initDto.setSc006SiyoryoMonthPay(CodeConstant.DOUBLE_QUOTATION);
			// 貸与用途
//		    Me.lblTaiyoYouto.Text = String.Empty
			initDto.setSc006TaiyoYouto(CodeConstant.DOUBLE_QUOTATION);
			// 貸与規格
//		    Me.lblTaiyoKikaku.Text = String.Empty
			initDto.setSc006TaiyoKikaku(CodeConstant.DOUBLE_QUOTATION);
//		    '寒冷地
//		    Me.lblKanreiti.Text = String.Empty
//		    '狭小
//		    Me.lblKyosyo.Text = String.Empty
			// 社宅使用料月額
//		    Me.lblShiyoryoTsukigaku.Text = DATA_0
			initDto.setSc006ShiyoryoTsukigaku(CodeConstant.STRING_ZERO);
			// 社宅使用料日割金額
//		    Me.lblSiyoryoHiwariPay.Text = DATA_0
			initDto.setSc006SiyoryoHiwariPay(CodeConstant.STRING_ZERO);
			// 社宅使用料調整金額
//		    Me.txtSiyoroTyoseiPay.Text = DATA_0
			initDto.setSc006SiyoroTyoseiPay(CodeConstant.STRING_ZERO);
			// 社宅使用料月額（調整後）
//		    Me.lblSyatauMonthPayAfter.Text = DATA_0
			initDto.setSc006SyatauMonthPayAfter(CodeConstant.STRING_ZERO);
			// 個人負担共益費 協議中
//		    Me.chkKyoekihiKyogichu.Checked = False
			initDto.setSc006KyoekihiKyogichuCheck(false);
			// 個人負担共益費月額
//		    Me.txtKyoekihiMonthPay.Text = DATA_0
			initDto.setSc006KyoekihiMonthPay(CodeConstant.STRING_ZERO);
			// 個人負担共益費調整金額 
//		    Me.txtKyoekihiTyoseiPay.Text = DATA_0
			initDto.setSc006KyoekihiTyoseiPay(CodeConstant.STRING_ZERO);
			// 個人負担共益費月額（調整後） 
//		    Me.lblKyoekihiPayAfter.Text = DATA_0
			initDto.setSc006KyoekihiPayAfter(CodeConstant.STRING_ZERO);
			/** 共益費日割計算対応 2021/5/14 add start **/
			initDto.setSc006KyoekihiMonth(CodeConstant.STRING_ZERO);
			initDto.setSc006KyoekihiNyukyoKasan(CodeConstant.STRING_ZERO);
			initDto.setSc006KyoekihiTaikyoKasan(CodeConstant.STRING_ZERO);
			/** 共益費日割計算対応 2021/5/14 add end **/
			// 駐車場管理番号１
//		    Me.hdnChushajoNoOne.Value = String.Empty
			initDto.setHdnChushajoNoOne(CodeConstant.DOUBLE_QUOTATION);
			// 区画１　区画番号
//		    Me.lblKukakuNoOne.Text = String.Empty
			initDto.setSc006KukakuNoOne(CodeConstant.DOUBLE_QUOTATION);
			// 区画１　利用終了日
//		    Me.txtRiyouEndDayOne.Text = String.Empty
			initDto.setSc006RiyouEndDayOne(CodeConstant.DOUBLE_QUOTATION);
			// 区画１　駐車場使用料月額
//		    Me.lblTyusyaMonthPayOne.Text = DATA_0
			initDto.setSc006TyusyaMonthPayOne(CodeConstant.STRING_ZERO);
			// 区画１　駐車場使用料日割金額
//		    Me.lblTyusyaDayPayOne.Text = DATA_0
			initDto.setSc006TyusyaDayPayOne(CodeConstant.STRING_ZERO);
			// 駐車場管理番号２
//		    Me.hdnChushajoNoTwo.Value = String.Empty
			initDto.setHdnChushajoNoTwo(CodeConstant.DOUBLE_QUOTATION);
			// 区画２　区画番号
//		    Me.lblKukakuNoTwo.Text = String.Empty
			initDto.setSc006KukakuNoTwo(CodeConstant.DOUBLE_QUOTATION);
			// 区画２　利用終了日
//		    Me.txtRiyouEndDayTwo.Text = String.Empty
			initDto.setSc006RiyouEndDayTwo(CodeConstant.DOUBLE_QUOTATION);
			// 区画２　駐車場使用料月額
//		    Me.lblTyusyaMonthPayTwo.Text = DATA_0
			initDto.setSc006TyusyaMonthPayTwo(CodeConstant.STRING_ZERO);
			// 区画２　駐車場使用料日割金額
//		    Me.lblTyusyaDayPayTwo.Text = DATA_0
			initDto.setSc006TyusyaDayPayTwo(CodeConstant.STRING_ZERO);
			// 駐車場使用料調整金額
//		    Me.txtTyusyaTyoseiPay.Text = DATA_0
			initDto.setSc006TyusyaTyoseiPay(CodeConstant.STRING_ZERO);
			// 駐車場使用料月額（調整後）
//		    Me.lblTyusyaMonthPayAfter.Text = DATA_0
			initDto.setSc006TyusyaMonthPayAfter(CodeConstant.STRING_ZERO);
			// 社宅部屋変更フラグ
//		    Me.hdnShatakuHeyaFlg.Value = SHATAKU_HEYA_FLG_YES
			initDto.setHdnShatakuHeyaFlg(Skf3022Sc006CommonDto.SHATAKU_HEYA_FLG_YES);
			// 備品再取得する
//		    bihinItiranFlg = 1
			initDto.setBihinItiranFlg(true);
//		    '【使用料計算機能対応】使用料入力支援セッションが残っていた場合、破棄する。
//		    If Not Me.Session.Item(Constant.SessionId.SHIYORYO_KEISAN_SHIEN_OUTPUT_INFO) Is Nothing Then
//		        Me.Session.Remove(Constant.SessionId.SHIYORYO_KEISAN_SHIEN_OUTPUT_INFO)
//		    End If
			// 使用料計算入力支援戻り値クリア
			skf3022Sc006SharedService.clearShiyoryoShienData(initDto);
		}

		// 画面ステータス設定
		skf3022Sc006SharedService.pageLoadComplete(initDto);
		// 処理状態クリア
		initDto.setSc006Status("");
		return initDto;
	}
}

