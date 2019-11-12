/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc006;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc006.Skf3010Sc006ChangeParkingStructureDrpDwnAsyncDto;
import jp.co.intra_mart.common.platform.log.Logger;

/**
 * Skf3010Sc006ChangeParkingStructureDrpDwnAsyncService 駐車場構造ドロップダウンリスト変更時非同期処理クラス
 * 駐車場情報タブの「構造」ドロップダウンチェンジ 
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3010Sc006ChangeParkingStructureDrpDwnAsyncService
		extends AsyncBaseServiceAbstract<Skf3010Sc006ChangeParkingStructureDrpDwnAsyncDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3010Sc006SharedService skf3010Sc006SharedService;
	/** ロガー。 */
	private static Logger logger = LogUtils.getLogger(SkfFileOutputUtils.class);

	/**
	 * 駐車場情報タブの「構造」ドロップダウンリスト変更時イベント
	 */
	@Override
	public AsyncBaseDto index(Skf3010Sc006ChangeParkingStructureDrpDwnAsyncDto asyncDto) throws Exception {
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("駐車場構造変更", CodeConstant.C001, asyncDto.getLocalPrePageId());
		// デバッグログ
		logger.debug("駐車場構造変更");

		/** DTO取得用 */
		String areaKbnCd = "";
		String parkingStructure = "";
		String parkingBasicRent = "";
		String parkingRentalAdjust = "";

		/** DTO取得 */
		// 地域区分コード
		if (asyncDto.getAreaKbnCd() != null) {
			areaKbnCd = asyncDto.getAreaKbnCd();
		}
		// 駐車場構造区分コード
		if (asyncDto.getParkingStructure() != null) {
			parkingStructure = asyncDto.getParkingStructure();
		}
		// 駐車場調整金額
		if (asyncDto.getParkingRentalAdjust() != null) {
			parkingRentalAdjust = asyncDto.getParkingRentalAdjust();
		}
		
		// 駐車場基本使用料、駐車場使用料算出
		Long parkingBasicRentValue = skf3010Sc006SharedService.setParkingRentalValue(areaKbnCd,parkingStructure);
		parkingBasicRent = String.valueOf(parkingBasicRentValue);
		
		//駐車場月額使用料
		Long parkingShiyoMonthFei = skf3010Sc006SharedService.setParkingMonthFei(parkingBasicRent, parkingRentalAdjust);


//        '駐車場情報の貸与区分ラベルを設定
//        Me.setParkingLendKbn()
//
//
//        If String.IsNullOrEmpty(Me.ddlKeiyakusu.Text) Then
//            '駐車場契約数が空白の場合、契約情報の一部を除き非活性とする
//            Me.SetParkingDisabled()
//        ElseIf Constant.ParkingStructureKbn.NASHI.Equals(Me.ddlParkingStructureKbn.Text) _
//            Or EMPTY.Equals(Me.ddlParkingStructureKbn.Text) Then
//            '“なし”・“空白”が選択された場合、契約情報の一部を除き非活性とする
//            Me.SetParkingDisabled()
//        Else
//            '上記に当てはまらない場合、契約情報を活性とする
//            Me.SetParkingEnabled()
//        End If
		
		// 戻り値設定
		asyncDto.setParkingBasicRent(String.format("%,d", parkingBasicRentValue));
		asyncDto.setParkingShiyoMonthFei(String.format("%,d", parkingShiyoMonthFei));

		return asyncDto;
	}

}
