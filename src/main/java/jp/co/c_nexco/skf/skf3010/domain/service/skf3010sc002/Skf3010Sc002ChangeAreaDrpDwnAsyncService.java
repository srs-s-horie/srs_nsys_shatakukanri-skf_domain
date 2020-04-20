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
import jp.co.c_nexco.skf.common.SkfAsyncServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc002.Skf3010Sc002ChangeAreaDrpDwnAsyncDto;

/**
 * Skf3010Sc002ChangeAreaDrpDwnAsyncService 保有社宅登録のドロップダウンリスト変更時非同期処理クラス
 * ヘッダの「地域区分」ドロップダウンチェンジ 
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3010Sc002ChangeAreaDrpDwnAsyncService
		extends SkfAsyncServiceAbstract<Skf3010Sc002ChangeAreaDrpDwnAsyncDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3010Sc002SharedService skf3010Sc002SharedService;

	/**
	 * 「地域区分」のドロップダウンリスト変更時イベント
	 */
	@Override
	public AsyncBaseDto index(Skf3010Sc002ChangeAreaDrpDwnAsyncDto asyncDto) throws Exception {

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("地域区分変更", CodeConstant.C001, FunctionIdConstant.SKF3010_SC002);
		// デバッグログ
		LogUtils.debugByMsg("地域区分変更");
		/** DTO設定用 */
		// 次回算定年月日
		StringBuilder nextCalcDate = new StringBuilder();
		// 実経年
		StringBuilder jituAge = new StringBuilder();
		// 経年
		StringBuilder aging = new StringBuilder();
		// 駐車場基本使用料
		StringBuilder parkingRentalValue = new StringBuilder(CodeConstant.STRING_ZERO);
		// 駐車場区画使用料リスト
		List <String> parkingBlockRentManys  = new ArrayList<String>();

		// DTO取得用
		String buildDate = "";
		String areaKbnCd = "";
		String structureKbn = "";
		String parkingStructure = "";
		List <String> parkingRentalAdjusts  = new ArrayList<String>();
		/** DTO取得 */
		// 次回算定年月日
		if (asyncDto.getNextCalcDate() != null) {
			nextCalcDate.append(asyncDto.getNextCalcDate());
		}
		// 実経年
		if (asyncDto.getJituAge() != null) {
			jituAge.append(asyncDto.getJituAge());
		}
		// 経年
		if (asyncDto.getAging() != null) {
			aging.append(asyncDto.getAging());
		}
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
		// 実年経年の設定
		skf3010Sc002SharedService.setNextCalcDateKeinenJitukeinen(nextCalcDate, jituAge,
													aging, buildDate, areaKbnCd, structureKbn);
		// 駐車場情報タブの設定
		// 駐車場基本使用料、駐車場使用料算出
		skf3010Sc002SharedService.parkingCalc(buildDate, structureKbn, areaKbnCd,
				parkingStructure, parkingRentalAdjusts, parkingBlockRentManys, parkingRentalValue);

		// セッションクリア
		asyncDto.setNextCalcDate(null);
		asyncDto.setJituAge(null);
		asyncDto.setAging(null);
		asyncDto.setParkingBasicRent(null);
		asyncDto.setParkingRentalAdjusts(null);
		asyncDto.setParkingBlockRentManys(null);
		// 戻り値設定
		asyncDto.setNextCalcDate(nextCalcDate.toString());
		asyncDto.setJituAge(jituAge.toString());
		asyncDto.setAging(aging.toString());
		asyncDto.setParkingBasicRent(parkingRentalValue.toString());
		asyncDto.setParkingBlockRentManys(parkingBlockRentManys);

		return asyncDto;
	}

}
