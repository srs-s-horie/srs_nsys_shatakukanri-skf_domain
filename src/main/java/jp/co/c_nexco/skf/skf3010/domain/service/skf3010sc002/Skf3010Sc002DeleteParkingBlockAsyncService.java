/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc002;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc002.Skf3010Sc002DeleteParkingBlockAsyncDto;

/**
 * Skf3010Sc002DeleteParkingBlockAsyncService 保有社宅登録の駐車場区画削除ボタン押下時非同期処理クラス
 * 駐車場タブの「削除」ボタン押下時イベント 
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3010Sc002DeleteParkingBlockAsyncService
		extends AsyncBaseServiceAbstract<Skf3010Sc002DeleteParkingBlockAsyncDto> {

	@Autowired
	private Skf3010Sc002SharedService skf3010Sc002SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	/**
	 * 駐車場タブの「削除」ボタン押下時イベント 
	 */
	@Override
	public AsyncBaseDto index(Skf3010Sc002DeleteParkingBlockAsyncDto asyncDto) throws Exception {
		
		// デバッグログ
		LogUtils.debugByMsg("駐車場区画削除");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("駐車場区画削除", CodeConstant.C001, FunctionIdConstant.SKF3010_SC002);

		// 貸与履歴提示数
		int historyCount = 0;
		// 駐車場区画削除フラグ
		Boolean parkingBlockDeleteFlg = false;

		/** DTO取得用 */
		// 社宅管理番号
		String shatakuKanriNo = "";
		// 削除駐車場管理番号
		String deleteParkingKanriNo = "";
		// 削除駐車場区画番号
		String deleteParkingBlockNo = "";

		/** DTO設定用 */
		// 駐車場台数、貸与可能総数リスト
		List <Integer> parkingBlockCalcNum = new ArrayList<Integer>();
		// 初期値設定
		parkingBlockCalcNum.add(0);	// 駐車場台数
		parkingBlockCalcNum.add(0);	// 貸与可能総数

		/** DTO取得 */
		// 社宅管理番号
		if (asyncDto.getHdnShatakuKanriNo() != null) {
			shatakuKanriNo = asyncDto.getHdnShatakuKanriNo();
		}
		// 駐車場管理番号
		if (asyncDto.getDeleteParkingKanriNo() != null) {
			deleteParkingKanriNo = asyncDto.getDeleteParkingKanriNo();
		}
		// 駐車場区画番号
		if (asyncDto.getDeleteParkingBlockNo() != null) {
			deleteParkingBlockNo = asyncDto.getDeleteParkingBlockNo();
		}
		// 駐車場区画貸与履歴数取得
		historyCount += skf3010Sc002SharedService.getShatakuParkingHistroyCount(shatakuKanriNo, deleteParkingKanriNo);
		// 駐車場区画提示数取得
		historyCount += skf3010Sc002SharedService.getTeijiDataCountForParkingBlock(shatakuKanriNo, deleteParkingKanriNo);
		// 貸与履歴提示数判定
		if (historyCount != 0) {
			ServiceHelper.addErrorResultMessage(asyncDto, null, MessageIdConstant.E_SKF_3009, deleteParkingBlockNo);
			throwBusinessExceptionIfErrors(asyncDto.getResultMessages());
		} else {
			// 駐車場台数、貸与可能総数取得
			parkingBlockCalcNum = skf3010Sc002SharedService.parkingBlockDeleteCalc(
					asyncDto.getParkingBlockRowData(), asyncDto.getParkingBlockCount(), asyncDto.getLendPossibleCount());
			parkingBlockDeleteFlg = true;
		}
		// セッションクリア
		asyncDto.setParkingBlockRowData(null);
		asyncDto.setParkingBlockCount(null);
		asyncDto.setLendPossibleCount(null);
		asyncDto.setParkingBlockDeleteFlg(null);

		// 戻り値設定
		// 駐車場台数
		asyncDto.setParkingBlockCount(parkingBlockCalcNum.get(0).toString() + " 台");
		// 貸与可能総数
		asyncDto.setLendPossibleCount(parkingBlockCalcNum.get(1).toString() + " 台");
		// 駐車場区画削除フラグ
		asyncDto.setParkingBlockDeleteFlg(parkingBlockDeleteFlg);

		return asyncDto;
	}
}
