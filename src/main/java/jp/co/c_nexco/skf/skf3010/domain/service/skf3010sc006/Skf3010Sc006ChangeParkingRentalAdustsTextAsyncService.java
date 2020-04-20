/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc006;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.skf.common.SkfAsyncServiceAbstract;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc006.Skf3010Sc006ChangeParkingRentalAdustsTextAsyncDto;

/**
 * Skf3010Sc002ChangeParkingRentalAdustsTextAsyncService 保有社宅登録の駐車場調整金額変更時非同期処理クラス
 * 駐車場情報タブ「駐車場調整金額」チェンジ
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3010Sc006ChangeParkingRentalAdustsTextAsyncService
		extends SkfAsyncServiceAbstract<Skf3010Sc006ChangeParkingRentalAdustsTextAsyncDto> {

	@Autowired
	private Skf3010Sc006SharedService skf3010Sc006SharedService;

	/**
	 * 駐車場情報タブ「駐車場調整金額」変更時イベント
	 */
	@Override
	public AsyncBaseDto index(Skf3010Sc006ChangeParkingRentalAdustsTextAsyncDto asyncDto) throws Exception {

		// デバッグログ
		LogUtils.debugByMsg("駐車場調整金額変更イベント");
		
		/** DTO取得用 */
		// 駐車場基本使用料
		String parkingRental = "";
		// 駐車場調整金額
		String parkingRentalAdjust = "";

		/** DTO取得 */
		// 駐車場基本使用料取得
		if(asyncDto.getParkingBasicRent() != null){
			parkingRental = asyncDto.getParkingBasicRent();
		}
		// 駐車場調整金額
		if (asyncDto.getParkingRentalAdjust() != null) {
			parkingRentalAdjust = asyncDto.getParkingRentalAdjust();
		}

		//駐車場月額使用料
		Long parkingShiyoMonthFei = skf3010Sc006SharedService.setParkingMonthFei(parkingRental, parkingRentalAdjust);

		// 戻り値設定
		asyncDto.setParkingShiyoMonthFei(String.format("%,d", parkingShiyoMonthFei));

		return asyncDto;
	}
}
