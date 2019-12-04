/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc006;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc006.Skf3022Sc006PreJigetsuYoyakuDto;

/**
 * Skf3022Sc006PreJigetsuYoyakuService 提示データ登録画面:次月予約前処理処理クラス。　 
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf3022Sc006PreJigetsuYoyakuService extends BaseServiceAbstract<Skf3022Sc006PreJigetsuYoyakuDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3022Sc006SharedService skf3022Sc006SharedService;
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;

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
	public Skf3022Sc006PreJigetsuYoyakuDto index(Skf3022Sc006PreJigetsuYoyakuDto initDto) throws Exception {

		initDto.setPageTitleKey(MessageIdConstant.SKF3022_SC006_TITLE);

		// デバッグログ
		LogUtils.debugByMsg("次月予約前処理");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("次月予約前処理", CodeConstant.C001, initDto.getPageId());

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
		skf3022Sc006SharedService.clearDisable(initDto);
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

		// 使用料変更チェック判定
		if (skf3022Sc006SharedService.haveShiyoryoChanged(initDto)) {
			LogUtils.debugByMsg("使用料変更有り");
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_3033);
		}
		// 備品は再取得しない
		initDto.setBihinItiranFlg(false);
		// 画面ステータス設定
		skf3022Sc006SharedService.pageLoadComplete(initDto);

		/** 次月予約パラメータ設定 */
		// 提示番号
		initDto.setHdnJigetuYoyakuTeijiNo(initDto.getHdnTeijiNo());
		// システム日付
		initDto.setHdnJigetuYoyakuYearMonth(skfBaseBusinessLogicUtils.getSystemProcessNenGetsu());
//		// 基準年月
//		String nyukyoYoteiDay = "";
//		String taikyoYoteiDay = "";
//		if (!CheckUtils.isEmpty(initDto.getSc006NyukyoYoteiDay())
//				&& !initDto.getSc006NyukyoYoteiDayDisableFlg()) {
//			nyukyoYoteiDay = skf3022Sc006SharedService.getDateText(initDto.getSc006NyukyoYoteiDay());
//		}
//		if (!CheckUtils.isEmpty(initDto.getSc006TaikyoYoteiDay())
//				&& !initDto.getSc006TaikyoYoteiDayDisableFlg()) {
//			taikyoYoteiDay = skf3022Sc006SharedService.getDateText(initDto.getSc006TaikyoYoteiDay());
//		}
		// 社宅管理台帳ID
		initDto.setHdnJigetuYoyakuShatakuKanriId(CodeConstant.DOUBLE_QUOTATION);
		// 社宅使用料月額
		initDto.setHdnJigetuYoyakuRental(skf3022Sc006SharedService.getKingakuText(initDto.getSc006SiyoryoMonthPay()));
		// 個人負担共益費月額
		initDto.setHdnJigetuYoyakuKyoekihiPerson(skf3022Sc006SharedService.getKingakuText(initDto.getSc006KyoekihiMonthPay()));
		// 区画１_駐車場使用料月額
		initDto.setHdnJigetuYoyakuParkingRentalOne(skf3022Sc006SharedService.getKingakuText(initDto.getSc006TyusyaMonthPayOne()));
		// 区画２_駐車場使用料月額
		initDto.setHdnJigetuYoyakuParkingRentalTwo(skf3022Sc006SharedService.getKingakuText(initDto.getSc006TyusyaMonthPayTwo()));

		return initDto;
	}
}

