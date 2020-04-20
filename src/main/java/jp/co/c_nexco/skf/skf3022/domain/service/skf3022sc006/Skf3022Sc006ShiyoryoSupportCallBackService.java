/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc006;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc006.Skf3022Sc006ShiyoryoSupportCallBackDto;

/**
 * Skf3022Sc006ShiyoryoSupportCallBackService 提示データ登録画面:使用料計算入力支援コールバック時処理クラス。　 
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf3022Sc006ShiyoryoSupportCallBackService extends SkfServiceAbstract<Skf3022Sc006ShiyoryoSupportCallBackDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3022Sc006SharedService skf3022Sc006SharedService;

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
	public Skf3022Sc006ShiyoryoSupportCallBackDto index(Skf3022Sc006ShiyoryoSupportCallBackDto initDto) throws Exception {

		initDto.setPageTitleKey(MessageIdConstant.SKF3022_SC006_TITLE);

		// デバッグログ
		LogUtils.debugByMsg("使用料入力支援");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("使用料入力支援", CodeConstant.C001, FunctionIdConstant.SKF3022_SC006);

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

		// 選択タブインデックス初期値
		String setHdnTabIndexOld = initDto.getHdnTabIndex();
		initDto.setHdnTabIndex("999");

		// 使用料項目の再設定(社宅使用料調整金額、個人負担共益費調整金額 を「0」に設定し再計算)
		// 社宅使用料調整金額クリア
		initDto.setSc006SiyoroTyoseiPay("0");
		// 個人負担共益費調整金額クリア 
		initDto.setSc006KyoekihiTyoseiPay("0");
		/* AS imrt移植 個人負担共益費月額（調整後）が画面項目の合計(個人負担共益費月額 + 個人負担共益費調整金額)と異なる問題に対処 */
		if (CheckUtils.isEmpty(initDto.getSc006KyoekihiMonthPay())) {
			// 個人負担共益費月額未指定時、個人負担共益費月額（調整後）を0円とする
			initDto.setSc006KyoekihiPayAfter("0");
		} else {
			// 個人負担共益費月額指定時、個人負担共益費月額（調整後）を個人負担共益費月額とする
			initDto.setSc006KyoekihiPayAfter(skf3022Sc006SharedService.getKanmaNumEdit(initDto.getSc006KyoekihiMonthPay()));
		}
		/* AE imrt移植 個人負担共益費月額（調整後）が画面項目の合計(個人負担共益費月額 + 個人負担共益費調整金額)と異なる問題に対処 */
		Map<String, String> paramMap = skf3022Sc006SharedService.createSiyoryoKeiSanParam(initDto);	// 使用料計算パラメータ
		Map<String, String> resultMap = new HashMap<String, String>();		// 使用料計算戻り値
		StringBuffer errMsg = new StringBuffer();							// エラーメッセージ
		// 使用料計算結果判定
		if (skf3022Sc006SharedService.siyoryoKeiSan("", "", paramMap, resultMap, errMsg)) {
			// 使用料計算でエラー
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.SKF3020_ERR_MSG_COMMON, errMsg.toString());
		} else {
			// 使用料計算戻り値設定
			skf3022Sc006SharedService.setSiyoryoKeiSanParam(resultMap, initDto);
		}
		// 備品は再取得しない
		initDto.setBihinItiranFlg(false);
		// 画面ステータス設定
		skf3022Sc006SharedService.pageLoadComplete(initDto);
		// 表示タブ設定
		initDto.setHdnTabIndex(setHdnTabIndexOld);
		// 処理状態クリア
		initDto.setSc006Status("");
		return initDto;
	}
}

