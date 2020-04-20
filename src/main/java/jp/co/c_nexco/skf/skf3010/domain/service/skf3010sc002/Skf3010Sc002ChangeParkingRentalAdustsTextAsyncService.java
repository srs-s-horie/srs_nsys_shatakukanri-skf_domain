/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc002;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.skf.common.SkfAsyncServiceAbstract;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc002.Skf3010Sc002ChangeParkingRentalAdustsTextAsyncDto;

/**
 * Skf3010Sc002ChangeParkingRentalAdustsTextAsyncService 保有社宅登録の駐車場調整金額変更時非同期処理クラス
 * 駐車場情報タブ「駐車場調整金額」チェンジ
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3010Sc002ChangeParkingRentalAdustsTextAsyncService
		extends SkfAsyncServiceAbstract<Skf3010Sc002ChangeParkingRentalAdustsTextAsyncDto> {

	@Autowired
	private Skf3010Sc002SharedService skf3010Sc002SharedService;

	/**
	 * 駐車場情報タブ「駐車場調整金額」変更時イベント
	 */
	@Override
	public AsyncBaseDto index(Skf3010Sc002ChangeParkingRentalAdustsTextAsyncDto asyncDto) throws Exception {

		/** DTO設定用 */
		// 駐車場区画使用料リスト
		List <String> parkingBlockRentManys  = new ArrayList<String>();

		/** DTO取得用 */
		// 駐車場基本使用料
		Long parkingRentalValue = 0L;
		// 駐車場調整金額
		List <String> parkingRentalAdjusts  = new ArrayList<String>();

		/** DTO取得 */
		// 駐車場基本使用料取得
		try {
			parkingRentalValue = Long.parseLong(asyncDto.getParkingBasicRent());
		} catch (NumberFormatException e) {}
		// 駐車場調整金額リスト取得
		if (asyncDto.getParkingRentalAdjusts() != null) {
			parkingRentalAdjusts = asyncDto.getParkingRentalAdjusts();
		}

		// 駐車場区画月額使用料算出
		skf3010Sc002SharedService.calcParkingBlockListRentMany(
				parkingRentalValue, parkingRentalAdjusts, parkingBlockRentManys);

		// セッションクリア
		asyncDto.setParkingBasicRent(null);
		asyncDto.setParkingRentalAdjusts(null);
		asyncDto.setParkingBlockRentManys(null);
		// 戻り値設定
		asyncDto.setParkingBlockRentManys(parkingBlockRentManys);

		return asyncDto;
	}
}
