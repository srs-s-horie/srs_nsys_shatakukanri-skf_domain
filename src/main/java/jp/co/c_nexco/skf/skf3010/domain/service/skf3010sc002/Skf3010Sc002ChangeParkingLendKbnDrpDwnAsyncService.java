/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc002;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.skf.common.SkfAsyncServiceAbstract;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc002.Skf3010Sc002ChangeParkingLendKbnDrpDwnAsyncDto;

/**
 * Skf3010Sc002ChangeParkingLendKbnDrpDwnAsyncService 保有社宅登録のドロップダウンリスト変更時非同期処理クラス
 * 駐車場タブの「貸与区分」ドロップダウンチェンジ 
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3010Sc002ChangeParkingLendKbnDrpDwnAsyncService
		extends SkfAsyncServiceAbstract<Skf3010Sc002ChangeParkingLendKbnDrpDwnAsyncDto> {

	@Autowired
	private Skf3010Sc002SharedService skf3010Sc002SharedService;

	/**
	 * 駐車場情報タブ「貸与区分」のドロップダウンリスト変更時イベント
	 */
	@Override
	public AsyncBaseDto index(Skf3010Sc002ChangeParkingLendKbnDrpDwnAsyncDto asyncDto) throws Exception {

		/** DTO設定用 */
		List <Integer> parkingBlockCalcNum = null;
		// 駐車場台数、貸与可能総数取得
		parkingBlockCalcNum = skf3010Sc002SharedService.parkingLendChangeCalc(
				asyncDto.getParkingBlockRowData(), asyncDto.getParkingBlockCount(), asyncDto.getLendPossibleCount());

		// セッションクリア
		asyncDto.setParkingBlockRowData(null);
		asyncDto.setParkingBlockCount(null);
		asyncDto.setLendPossibleCount(null);

		// 戻り値設定
		// 駐車場台数
		asyncDto.setParkingBlockCount(parkingBlockCalcNum.get(0).toString() + " 台");
		// 貸与可能総数
		asyncDto.setLendPossibleCount(parkingBlockCalcNum.get(1).toString() + " 台");

		return asyncDto;
	}
}
