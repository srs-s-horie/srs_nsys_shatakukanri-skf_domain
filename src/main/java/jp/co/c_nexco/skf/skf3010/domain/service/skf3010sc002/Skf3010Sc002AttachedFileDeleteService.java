/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc002;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc002.Skf3010Sc002AttachedFileDeleteDto;
import jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc002.Skf3010Sc002SharedService;

/**
 * Skf3010Sc002AttachedFileDeleteService 補足ファイル削除処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf3010Sc002AttachedFileDeleteService extends BaseServiceAbstract<Skf3010Sc002AttachedFileDeleteDto> {

	@Autowired
	private Skf3010Sc002SharedService skf3010Sc002SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	// 社宅補足
	private static final String SHATAKU_HOSOKU = "shataku";
	// 駐車場補足
	private static final String PARKING_HOSOKU = "parking";
	
	/**
	 * サービス処理を行う。
	 * 
	 * @param deleteDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf3010Sc002AttachedFileDeleteDto index(Skf3010Sc002AttachedFileDeleteDto deleteDto) throws Exception {

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("補足資料削除", CodeConstant.C001, FunctionIdConstant.SKF3010_SC002);
		// デバッグログ
		LogUtils.debugByMsg("補足資料削除");

		/** JSON(連携用) */
		// 駐車場区画情報リスト
		List<Map<String, Object>> parkingList = new ArrayList<Map<String, Object>>();
		// 備品情報リスト
		List<Map<String, Object>> bihinList = new ArrayList<Map<String, Object>>();
		// ドロップダウン選択値リスト
		List<Map<String, Object>> drpDwnSelectedList = new ArrayList<Map<String, Object>>();
		// 可変ラベルリスト
		List<Map<String, Object>> labelList = new ArrayList<Map<String, Object>>();
		//ファイル名
		String fileName = CodeConstant.DOUBLE_QUOTATION;
		//リンクID
		String hosokuLink = CodeConstant.DOUBLE_QUOTATION;
		//ファイルサイズ
		String fileSize = CodeConstant.DOUBLE_QUOTATION;
		//ファイル情報
		byte[] fileStream = null;
		//ファイル番号
		String fileNo = deleteDto.getFileNo();
		//補足種別
		String hosokuType = deleteDto.getHosokuType();

		// List変換
		parkingList.addAll(skf3010Sc002SharedService.jsonArrayToArrayList(deleteDto.getJsonParking()));
		bihinList.addAll(skf3010Sc002SharedService.jsonArrayToArrayList(deleteDto.getJsonBihin()));
		drpDwnSelectedList.addAll(skf3010Sc002SharedService.jsonArrayToArrayList(deleteDto.getJsonDrpDwnList()));
		labelList.addAll(skf3010Sc002SharedService.jsonArrayToArrayList(deleteDto.getJsonLabelList()));
		// エラーコントロール初期化
		skf3010Sc002SharedService.clearVaridateErr(deleteDto);
		// 一旦画面を戻す
		skf3010Sc002SharedService.setBeforeInfo(drpDwnSelectedList, labelList, bihinList, parkingList, deleteDto);

		//ファイル情報をDTOクリア
		if(SHATAKU_HOSOKU.equals(hosokuType)){
			//社宅
			switch (fileNo){
			case "1":
				deleteDto.setShatakuHosokuFileName1(fileName);
				deleteDto.setShatakuHosokuLink1(hosokuLink);
				deleteDto.setShatakuHosokuSize1(fileSize);
				deleteDto.setShatakuHosokuFile1(fileStream);
				break;
			case "2":
				deleteDto.setShatakuHosokuFileName2(fileName);
				deleteDto.setShatakuHosokuLink2(hosokuLink);
				deleteDto.setShatakuHosokuSize2(fileSize);
				deleteDto.setShatakuHosokuFile2(fileStream);
				break;
			case "3":
				deleteDto.setShatakuHosokuFileName3(fileName);
				deleteDto.setShatakuHosokuLink3(hosokuLink);
				deleteDto.setShatakuHosokuSize3(fileSize);
				deleteDto.setShatakuHosokuFile3(fileStream);
				break;
			}
		}else if(PARKING_HOSOKU.equals(hosokuType)){
			//駐車場
			switch (fileNo){
			case "1":
				deleteDto.setParkingHosokuFileName1(fileName);
				deleteDto.setParkingHosokuLink1(hosokuLink);
				deleteDto.setParkingHosokuSize1(fileSize);
				deleteDto.setParkingHosokuFile1(fileStream);
				break;
			case "2":
				deleteDto.setParkingHosokuFileName2(fileName);
				deleteDto.setParkingHosokuLink2(hosokuLink);
				deleteDto.setParkingHosokuSize2(fileSize);
				deleteDto.setParkingHosokuFile2(fileStream);
				break;
			case "3":
				deleteDto.setParkingHosokuFileName3(fileName);
				deleteDto.setParkingHosokuLink3(hosokuLink);
				deleteDto.setParkingHosokuSize3(fileSize);
				deleteDto.setParkingHosokuFile3(fileStream);
				break;
			}
		}
		return deleteDto;
	}
}
