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
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc002.Skf3010Sc002ChangeParkingStructureDrpDwnAsyncDto;
import jp.co.intra_mart.common.platform.log.Logger;

/**
 * Skf3010Sc002ChangeParkingStructureDrpDwnAsyncService 保有社宅登録のドロップダウンリスト変更時非同期処理クラス
 * 駐車場情報タブの「構造」ドロップダウンチェンジ 
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3010Sc002ChangeParkingStructureDrpDwnAsyncService
		extends AsyncBaseServiceAbstract<Skf3010Sc002ChangeParkingStructureDrpDwnAsyncDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3010Sc002SharedService skf3010Sc002SharedService;
	/** ロガー。 */
	private static Logger logger = LogUtils.getLogger(SkfFileOutputUtils.class);

	/**
	 * 駐車場情報タブの「構造」ドロップダウンリスト変更時イベント
	 */
	@Override
	public AsyncBaseDto index(Skf3010Sc002ChangeParkingStructureDrpDwnAsyncDto asyncDto) throws Exception {
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("駐車場構造変更", CodeConstant.C001, asyncDto.getLocalPrePageId());
		// デバッグログ
		logger.debug("駐車場構造変更");
		/** DTO設定用 */
		// 駐車場基本使用料
		StringBuilder parkingRentalValue = new StringBuilder(CodeConstant.STRING_ZERO);
		// 駐車場区画使用料リスト
		List <String> parkingBlockRentManys  = new ArrayList<String>();

		/** DTO取得用 */
		String buildDate = "";
		String areaKbnCd = "";
		String structureKbn = "";
		String parkingStructure = "";
		List <String> parkingRentalAdjusts  = new ArrayList<String>();

		/** DTO取得 */
		// 建築年月日
		if (asyncDto.getBuildDate() != null) {
			buildDate = asyncDto.getBuildDate();
		}
		// 地域区分コード
		if (asyncDto.getAreaKbnCd() != null) {
			areaKbnCd = asyncDto.getAreaKbnCd();
		}
		// 社宅構造区分コード
		if (asyncDto.getStructureKbn() != null) {
			structureKbn = asyncDto.getStructureKbn();
		}
		// 駐車場構造区分コード
		if (asyncDto.getParkingStructure() != null) {
			parkingStructure = asyncDto.getParkingStructure();
		}
		// 駐車場調整金額リスト
		if (asyncDto.getParkingRentalAdjusts() != null) {
			parkingRentalAdjusts = asyncDto.getParkingRentalAdjusts();
		}
		// 駐車場基本使用料、駐車場使用料算出
		skf3010Sc002SharedService.parkingCalc(buildDate, structureKbn, areaKbnCd,
						parkingStructure, parkingRentalAdjusts, parkingBlockRentManys, parkingRentalValue);

		// セッションクリア
		asyncDto.setParkingBasicRent(null);
		asyncDto.setParkingRentalAdjusts(null);
		asyncDto.setParkingBlockRentManys(null);
		// 戻り値設定
		asyncDto.setParkingBasicRent(parkingRentalValue.toString());
		asyncDto.setParkingBlockRentManys(parkingBlockRentManys);

		return asyncDto;
	}

}
