/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc007;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc007.Skf3010Sc007InitDto;


/**
 * Skf3010Sc007InitService 駐車場契約情報登録画面のInitサービス処理クラス。　 
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3010Sc007InitService extends SkfServiceAbstract<Skf3010Sc007InitDto> {
	

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private Skf3010Sc007SharedService skf3010Sc007SharedService;
	
	private final static String TRUE = "true";
	
	// リストテーブルの１ページ最大表示行数
	@Value("${skf3010.skf3010_sc007.max_row_count}")
	private String listTableMaxRowCount;
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
	public Skf3010Sc007InitDto index(Skf3010Sc007InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF3010_SC007_TITLE);
 		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, FunctionIdConstant.SKF3010_SC007);
		// 初期化
		initialize(initDto);
		// リストデータ取得用
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
		//データ保持用リスト
		List<Map<String, Object>> hdnListData = new ArrayList<Map<String, Object>>();
		//契約番号リスト
		List<Map<String, Object>> contractPropertyIdList = new ArrayList<Map<String, Object>>();
		//契約形態リスト
		List<Map<String, Object>> parkingContractTypeList = new ArrayList<Map<String, Object>>();
		//貸与区分リスト
		List<Map<String, Object>> parkinglendKbnList = new ArrayList<Map<String, Object>>();
		
		// 登録画面のhidden項目をinitDtoに詰めなおす
		initDto.setHdnShatakuKanriNo(initDto.getHdnShatakuKanriNo());
		initDto.setShatakuName(initDto.getHdnShatakuName());
		initDto.setAreaKbn(initDto.getHdnAreaKbn());
		initDto.setShatakuKbn(initDto.getHdnShatakuKbn());
		
		setShatakuInfo(initDto);
		
		// リストテーブルの情報を取得
		skf3010Sc007SharedService.getListTableData(Long.parseLong(initDto.getHdnShatakuKanriNo()), listTableData, hdnListData);
		
		//各項目をDisable
		initDto.setContractInfoDisabled(TRUE);
		initDto.setAddButtonDisabled(TRUE);
		initDto.setDeleteButtonDisabled(TRUE);
		initDto.setRegistButtonDisabled(TRUE);
		initDto.setCancelButtonDisabled(TRUE);
		initDto.setContractListDisabled(TRUE);
		
		//入力項目初期化
		//区画番号
		initDto.setParkingBlock(CodeConstant.DOUBLE_QUOTATION);
		initDto.setOwnerName(CodeConstant.DOUBLE_QUOTATION);
		initDto.setOwnerNo(CodeConstant.DOUBLE_QUOTATION);
		initDto.setParkingZipCd(CodeConstant.DOUBLE_QUOTATION);
		initDto.setParkingAddress(CodeConstant.DOUBLE_QUOTATION);
		initDto.setParkingName(CodeConstant.DOUBLE_QUOTATION);
		initDto.setAssetRegisterNo(CodeConstant.DOUBLE_QUOTATION);
		initDto.setContractStartDate(CodeConstant.DOUBLE_QUOTATION);
		initDto.setContractEndDate(CodeConstant.DOUBLE_QUOTATION);
		initDto.setLandRent(CodeConstant.DOUBLE_QUOTATION);
		initDto.setBiko(CodeConstant.DOUBLE_QUOTATION);
		//入力チェック用初期化
		initDto.setSelectMode("init");
		initDto.setHdnBackupContractPropertyId(CodeConstant.DOUBLE_QUOTATION);
		initDto.setHdnBackupMaxContractPropertyId(CodeConstant.DOUBLE_QUOTATION);
		initDto.setHdnBackupParkingContractType(CodeConstant.DOUBLE_QUOTATION);
		initDto.setHdnBackupOwnerName(CodeConstant.DOUBLE_QUOTATION);
		initDto.setHdnBackupParkingZipCd(CodeConstant.DOUBLE_QUOTATION);
		initDto.setHdnBackupParkingAddress(CodeConstant.DOUBLE_QUOTATION);
		initDto.setHdnBackupParkingName(CodeConstant.DOUBLE_QUOTATION);
		initDto.setHdnBackupAssetRegisterNo(CodeConstant.DOUBLE_QUOTATION);
		initDto.setHdnBackupParkinglendKbn(CodeConstant.DOUBLE_QUOTATION);
		initDto.setHdnBackupContractStartDate(CodeConstant.DOUBLE_QUOTATION);
		initDto.setHdnBackupContractEndDate(CodeConstant.DOUBLE_QUOTATION);
		initDto.setHdnBackupLandRent(CodeConstant.DOUBLE_QUOTATION);
		initDto.setHdnBackupBiko(CodeConstant.DOUBLE_QUOTATION);
		//各リスト
		initDto.setParkingContractTypeList(parkingContractTypeList);
		initDto.setParkinglendKbnList(parkinglendKbnList);
		initDto.setContractPropertyIdList(contractPropertyIdList);
		
		initDto.setListTableMaxRowCount(listTableMaxRowCount);
		initDto.setListTableData(listTableData);
		initDto.setHdnListData(hdnListData);
		initDto.setHdnDelInfoFlg(CodeConstant.STRING_ZERO);
		
		//エラー変数初期化
		initDto.setOwnerNameError(CodeConstant.DOUBLE_QUOTATION);
		initDto.setParkingZipCdError(CodeConstant.DOUBLE_QUOTATION);
		initDto.setParkingAddressError(CodeConstant.DOUBLE_QUOTATION);
		initDto.setParkingNameError(CodeConstant.DOUBLE_QUOTATION);
		initDto.setAssetRegisterNoError(CodeConstant.DOUBLE_QUOTATION);
		initDto.setContractStartDateError(CodeConstant.DOUBLE_QUOTATION);
		initDto.setContractEndDateError(CodeConstant.DOUBLE_QUOTATION);
		initDto.setLandRentError(CodeConstant.DOUBLE_QUOTATION);
		initDto.setParkingContractTypeError(CodeConstant.DOUBLE_QUOTATION);
		initDto.setContractPropertyIdError(CodeConstant.DOUBLE_QUOTATION);
		
		return initDto;
	}
	
	/**
	 * 社宅情報のセット
	 * 
	 * @param initDto
	 */
	private void setShatakuInfo(Skf3010Sc007InitDto initDto) {

		// 「地域区分」の設定
		// 汎用コード取得
		Map<String, String> genericCodeMapArea = new HashMap<String, String>();
		genericCodeMapArea = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_AREA_KBN);
		String areaKbn = "";
		if (initDto.getHdnAreaKbn() != null) {
			areaKbn = genericCodeMapArea.get(initDto.getHdnAreaKbn());
		}
		initDto.setAreaKbn(areaKbn);

		// 「社宅区分」を設定
		// 汎用コード取得
		Map<String, String> genericCodeMapShataku = new HashMap<String, String>();
		genericCodeMapShataku = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_SHATAKU_KBN);
		String shatakuKbn = "";
		if (initDto.getHdnShatakuKbn() != null) {
			shatakuKbn = genericCodeMapShataku.get(initDto.getHdnShatakuKbn());
		}
		initDto.setShatakuKbn(shatakuKbn);

	}

	/**
	 * 初期化処理
	 * 「*」項目はアドレスとして戻り値になる
	 * 
	 * @param comDto	*DTO
	 */
	private void initialize(Skf3010Sc007InitDto comDto) {

		// 社宅名
		comDto.setShatakuName(null);
		// 地域区分
		comDto.setAreaKbn(null);
		// 社宅区分
		comDto.setShatakuKbn(null);

		//区画番号
		comDto.setParkingBlock(null);
		comDto.setHdnParkingBlock(null);
		//契約番号
		comDto.setContractPropertyId(null);
		//契約番号リスト
		comDto.setContractPropertyIdList(null);
		//リスト用文字列
		comDto.setContractPropertyIdListData(null);
		//契約形態リスト
		comDto.setParkingContractTypeList(null);
		comDto.setParkingContractType(null);
		//賃貸人（代理人）
		comDto.setOwnerName(null);
		comDto.setOwnerNo(null);
		//郵便番号
		comDto.setParkingZipCd(null);
		//住所
		comDto.setParkingAddress(null);
		//駐車場名
		comDto.setParkingName(null);
		//経理連携用管理番号
		comDto.setAssetRegisterNo(null);
		//貸与区分
		comDto.setParkinglendKbnList(null);
		comDto.setParkinglendKbn(null);
		//契約開始日
		comDto.setContractStartDate(null);
		comDto.setSetContractStartDate(null);
		//契約終了日
		comDto.setContractEndDate(null);
		comDto.setSetContractEndDate(null);
		//駐車場料（地代） 
		comDto.setLandRent(null);
		comDto.setLandRentNum(null);
		//備考
		comDto.setBiko(null);
		//駐車場管理番号
		comDto.setParkingKanriNo(null);
		//更新日時
		comDto.setBlockUpdateDate(null);
		//更新日時
		comDto.setContractUpdateDate(null);


		//駐車場一覧用
		comDto.setListTableData(null);
		comDto.setListTableMaxRowCount(null);
		//データ保持用リスト
		comDto.setHdnListData(null);
		comDto.setHdnListDataJson(null);

		//入力制御
		comDto.setContractInfoDisabled(null);
		comDto.setAddButtonDisabled(null);
		comDto.setDeleteButtonDisabled(null);
		comDto.setRegistButtonDisabled(null);
		comDto.setCancelButtonDisabled(null);
		comDto.setContractListDisabled(null);

		//動作制御
		comDto.setSelectMode(null);
		//削除情報有無
		comDto.setHdnDelInfoFlg(null);

		//入力チェック用
		//契約番号
		comDto.setHdnBackupContractPropertyId(null);
		//契約番号最大値
		comDto.setHdnBackupMaxContractPropertyId(null);
		//登録済み契約番号
		comDto.setHdnRegistContractPropertyId(null);
		//契約形態
		comDto.setHdnBackupParkingContractType(null);
		//賃貸人（代理人）
		comDto.setHdnBackupOwnerName(null);
		//賃貸人（代理人）番号
		comDto.setHdnBackupOwnerNo(null);
		//郵便番号
		comDto.setHdnBackupParkingZipCd(null);
		//住所
		comDto.setHdnBackupParkingAddress(null);
		//駐車場名
		comDto.setHdnBackupParkingName(null);
		//経理連携用資産番号
		comDto.setHdnBackupAssetRegisterNo(null);
		//貸与区分
		comDto.setHdnBackupParkinglendKbn(null);
		//契約開始日
		comDto.setHdnBackupContractStartDate(null);
		//契約終了日
		comDto.setHdnBackupContractEndDate(null);
		//駐車場料（地代） 
		comDto.setHdnBackupLandRent(null);
		//備考
		comDto.setHdnBackupBiko(null);

		/** エラー系 **/
		comDto.setOwnerNameError(null);
		comDto.setParkingZipCdError(null);
		comDto.setParkingAddressError(null);
		comDto.setParkingNameError(null);
		comDto.setAssetRegisterNoError(null);
		comDto.setContractStartDateError(null);
		comDto.setContractEndDateError(null);
		comDto.setLandRentError(null);
		comDto.setParkingContractTypeError(null);
		comDto.setContractPropertyIdError(null);
	}
}
