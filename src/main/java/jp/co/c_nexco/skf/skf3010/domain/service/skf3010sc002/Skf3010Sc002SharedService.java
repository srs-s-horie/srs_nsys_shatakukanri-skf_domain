/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc002;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetBihinInfoTableDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetHoyuShatakuInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetKihonInfoTableDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetParkingBlockExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetParkingBlockHistroyCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetRoomBihinExlusiveCntrlExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetShatakuContractInfoTableDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetShatakuManageTableDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetShatakuParkingBlockTableDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetShatakuParkingInfoTableDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetShatakuRoomExlusiveCntrlExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBaseBusinessLogicUtils.SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBaseBusinessLogicUtils.SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetBihinInfoTableDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetContractInfoTableDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetKihonInfoTableDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetManageInfoTableDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetParkingBlockTableDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetParkingCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetParkingInfoTableDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetRoomBihinExlusiveCntrTableDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetRoomCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetRoomExlusiveCntrTableDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetSyatakuParkingHistroyCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetTeijiDataCountForParkingBlockExpRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc002common.Skf3010Sc002CommonDto;
import jp.co.intra_mart.common.platform.log.Logger;

/**
 * Skf3010Sc002SharedService 保有社宅登録内共通クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3010Sc002SharedService {

	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	@Autowired
	private SkfDropDownUtils ddlUtils;
	@Autowired
	private Skf3010Sc002GetBihinInfoTableDataExpRepository skf3010Sc002GetBihinInfoTableDataExpRepository;
	@Autowired
	private Skf3010Sc002GetRoomCountExpRepository skf3010Sc002GetRoomCountExpRepository;
	@Autowired
	private Skf3010Sc002GetParkingCountExpRepository skf3010Sc002GetParkingCountExpRepository;
	@Autowired
	private Skf3010Sc002GetKihonInfoTableDataExpRepository skf3010Sc002GetKihonInfoTableDataExpRepository;
	@Autowired
	private Skf3010Sc002GetManageInfoTableDataExpRepository skf3010Sc002GetManageInfoTableDataExpRepository;
	@Autowired
	private Skf3010Sc002GetParkingInfoTableDataExpRepository skf3010Sc002GetParkingInfoTableDataExpRepository;
	@Autowired
	private Skf3010Sc002GetContractInfoTableDataExpRepository skf3010Sc002GetContractInfoTableDataExpRepository;	
	@Autowired
	private Skf3010Sc002GetParkingBlockTableDataExpRepository skf3010Sc002GetParkingBlockTableDataExpRepository;
	@Autowired
	private Skf3010Sc002GetRoomExlusiveCntrTableDataExpRepository skf3010Sc002GetRoomExlusiveCntrTableDataExpRepository;
	@Autowired
	private Skf3010Sc002GetRoomBihinExlusiveCntrTableDataExpRepository skf3010Sc002GetRoomBihinExlusiveCntrTableDataExpRepository;
	@Autowired
	private Skf3010Sc002GetTeijiDataCountForParkingBlockExpRepository skf3010Sc002GetTeijiDataCountForParkingBlockExpRepository;
	@Autowired
	private Skf3010Sc002GetSyatakuParkingHistroyCountExpRepository skf3010Sc002GetSyatakuParkingHistroyCountExpRepository;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	/** ロガー。 */
	private static Logger logger = LogUtils.getLogger(SkfFileOutputUtils.class);

	// 契約情報：「：」
	private static final String CONTRACT_NO_SEPARATOR = "：契約開始日 ";
	// 日付上限
	private static final int DATE_MAX = 99940331;
	// 管理者区分：寮長・自治会長
	private static final String MANAGE_KBN_DOMITRY_LEADER = "1";
	// 管理者区分：鍵管理者
	private static final String MANAGE_KBN_KEY_MANAGER = "2";
	// 管理者区分：寮母・管理会社
	private static final String MANAGE_KBN_MATRON = "3";
	// 社宅補足リンクプレフィックス
	private static final String SHATAKU_HOSOKU_LINK = "attached_shataku";
	// 駐車場補足プレフィックス
	private static final String PARKING_HOSOKU_LINK = "attached_parking";

	/**
	 * 駐車場区画貸与履歴数取得。<br>
	 * パラメータの社宅管理番号、駐車場管理番号の貸与履歴数をDBより取得する。
	 * 
	 * @param shatakuKanriNo 社宅管理番号
	 */
	public int getSyatakuParkingHistroyCount(String shatakuKanriNo, String parkingKanriNo) {
		int parkingBlockLendHistoryCount = 0;
		if (shatakuKanriNo.length() > 0 && parkingKanriNo.length() > 0) {
			Skf3010Sc002GetParkingBlockHistroyCountExpParameter param = new Skf3010Sc002GetParkingBlockHistroyCountExpParameter();
			param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
			param.setParkingKanriNo(Long.parseLong(parkingKanriNo));
			parkingBlockLendHistoryCount = skf3010Sc002GetSyatakuParkingHistroyCountExpRepository.getSyatakuParkingHistroyCount(param);
			param = null;
		}
		return parkingBlockLendHistoryCount;
	}

	/**
	 * 駐車場区画提示数取得。<br>
	 * パラメータの社宅管理番号、駐車場管理番号の提示数をDBより取得する。
	 * 
	 * @param shatakuKanriNo 社宅管理番号
	 * @param 
	 */
	public int getTeijiDataCountForParkingBlock(String shatakuKanriNo, String parkingKanriNo) {
		int parkingBlockTeijiHistoryCount = 0;
		if (shatakuKanriNo.length() > 0 && parkingKanriNo.length() > 0) {
			Skf3010Sc002GetParkingBlockHistroyCountExpParameter param = new Skf3010Sc002GetParkingBlockHistroyCountExpParameter();
			param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
			param.setShatakuKanriNo(Long.parseLong(parkingKanriNo));
			parkingBlockTeijiHistoryCount = skf3010Sc002GetTeijiDataCountForParkingBlockExpRepository.getTeijiDataCountForParkingBlock(param);
			param = null;
		}
		return parkingBlockTeijiHistoryCount;
	}

	/**
	 * 社宅部屋総数取得。<br>
	 * パラメータの社宅管理番号の社宅の部屋総数をDBより取得する。
	 * 
	 * @param shatakuKanriNo 社宅管理番号
	 */
	private int getRoomCount(String shatakuKanriNo) {
		int roomCnt = 0;
		if (shatakuKanriNo.length() > 0) {
			Skf3010Sc002GetHoyuShatakuInfoExpParameter param = new Skf3010Sc002GetHoyuShatakuInfoExpParameter();
			param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
			roomCnt = skf3010Sc002GetRoomCountExpRepository.getRoomCount(param);
			param = null;
		}
		return roomCnt;
	}

	/**
	 * 駐車場総数取得。<br>
	 * パラメータの社宅管理番号の駐車場総数をDBより取得する。
	 * 
	 * @param shatakuKanriNo 社宅管理番号
	 */
	private int getParkingCount(String shatakuKanriNo) {
		int parkingCnt = 0;
		if (shatakuKanriNo.length() > 0) {
			Skf3010Sc002GetHoyuShatakuInfoExpParameter param = new Skf3010Sc002GetHoyuShatakuInfoExpParameter();
			param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
			parkingCnt = skf3010Sc002GetParkingCountExpRepository.getParkingCount(param);
			param = null;
		}
		return parkingCnt;
	}

	/**
	 * 基本情報取得。<br>
	 * 
	 * 引数の社宅管理番号を検索キーに社宅の基本情報をDBより取得する。
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param shatakuKanriNo			社宅管理番号
	 * @param getKihonInfoListTableData	*社宅基本情報リスト
	 * @return 取得件数
	 */
	private void getKihonInfo(String shatakuKanriNo,
			List<Skf3010Sc002GetKihonInfoTableDataExp> getKihonInfoListTableData) {

		// リストテーブルに格納するデータを取得する
		int resultCount = 0;
		List<Skf3010Sc002GetKihonInfoTableDataExp> resultListTableData =
						new ArrayList<Skf3010Sc002GetKihonInfoTableDataExp>();
		if (shatakuKanriNo.length() > 0) {
			Skf3010Sc002GetHoyuShatakuInfoExpParameter param = new Skf3010Sc002GetHoyuShatakuInfoExpParameter();
			param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
			resultListTableData = skf3010Sc002GetKihonInfoTableDataExpRepository.getKihonInfo(param);
			param = null;

			// 取得レコード数を設定
			resultCount = resultListTableData.size();

			// 取得データレコード数判定
			if (resultCount <= 0) {
				// 取得データレコード数が0件場合、何もせず処理終了
				return;
			}
		}
		getKihonInfoListTableData.clear();
		getKihonInfoListTableData.addAll(resultListTableData);
		// 解放
		resultListTableData = null;
	}

	/**
	 * 管理者情報取得。<br>
	 * 
	 * 引数の社宅管理番号を検索キーに社宅の基本情報をDBより取得する。
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param shatakuKanriNo				社宅管理番号
	 * @param getShatakuManageListTableData	*社宅管理者情報リスト
	 * @return 取得件数
	 */
	private void getShatakuManageInfo(String shatakuKanriNo,
			List<Skf3010Sc002GetShatakuManageTableDataExp> getShatakuManageListTableData) {

		// リストテーブルに格納するデータを取得する
		int resultCount = 0;
		List<Skf3010Sc002GetShatakuManageTableDataExp> resultListTableData =
						new ArrayList<Skf3010Sc002GetShatakuManageTableDataExp>();
		if (shatakuKanriNo.length() > 0) {
			Skf3010Sc002GetHoyuShatakuInfoExpParameter param = new Skf3010Sc002GetHoyuShatakuInfoExpParameter();
			param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
			resultListTableData = skf3010Sc002GetManageInfoTableDataExpRepository.getManageInfo(param);
			param = null;

			// 取得レコード数を設定
			resultCount = resultListTableData.size();

			// 取得データレコード数判定
			if (resultCount <= 0) {
				// 取得データレコード数が0件場合、何もせず処理終了
				return;
			}
		}
		getShatakuManageListTableData.clear();
		getShatakuManageListTableData.addAll(resultListTableData);
		// 解放
		resultListTableData = null;
	}

	/**
	 * 駐車場情報取得。<br>
	 * 引数の社宅管理番号を検索キーに駐車場情報をDBより取得する。
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param shatakuKanriNo					社宅管理番号
	 * @param getShatakuParkingInfoTableData	*駐車場情報リスト
	 */
	private void getShatakuParkingInfo(String shatakuKanriNo,
			List<Skf3010Sc002GetShatakuParkingInfoTableDataExp> getShatakuParkingInfoTableData) {

		// リストテーブルに格納するデータを取得する
		int resultCount = 0;
		List<Skf3010Sc002GetShatakuParkingInfoTableDataExp> resultListTableData =
						new ArrayList<Skf3010Sc002GetShatakuParkingInfoTableDataExp>();
		if (shatakuKanriNo.length() > 0) {
			Skf3010Sc002GetHoyuShatakuInfoExpParameter param = new Skf3010Sc002GetHoyuShatakuInfoExpParameter();
			param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
			resultListTableData = skf3010Sc002GetParkingInfoTableDataExpRepository.getParkingInfo(param);
			param = null;

			// 取得レコード数を設定
			resultCount = resultListTableData.size();

			// 取得データレコード数判定
			if (resultCount <= 0) {
				// 取得データレコード数が0件場合、何もせず処理終了
				return;
			}
		}
		getShatakuParkingInfoTableData.clear();
		getShatakuParkingInfoTableData.addAll(resultListTableData);
		// 解放
		resultListTableData = null;
	}

	/**
	 * 契約情報取得。<br>
	 * 引数の社宅管理番号を検索キーに契約情報をDBより取得する。
	 * 同時にプルダウン設定も行う
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param shatakuKanriNo		社宅管理番号
	 * @param listTableData			*契約情報リスト
	 * @param contractNoList		*契約番号リスト(ドロップダウン)
	 * @param selectedValue			契約番号ドロップダウン選択値
	 * @param deletedConstractNo	削除済み契約番号
	 * @param selectMode			契約情報変更モード
	 * @return	契約情報リスト数
	 */
	private int getShatakuContractInfo(String shatakuKanriNo, List<Map<String, Object>> listTableData,
			List<Map<String, Object>> contractNoList, String selectedValue, String deletedConstractNo, String selectMode) {

		// 設定用契約番号リスト
		List<Map<String, Object>> resultContractNoList = new ArrayList<Map<String, Object>>();

		// リストテーブルに格納するデータを取得する
		int resultCount = 0;
		List<Skf3010Sc002GetShatakuContractInfoTableDataExp> resultListTableData =
						new ArrayList<Skf3010Sc002GetShatakuContractInfoTableDataExp>();
		if (shatakuKanriNo.length() > 0) {
			Skf3010Sc002GetHoyuShatakuInfoExpParameter param = new Skf3010Sc002GetHoyuShatakuInfoExpParameter();
			param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
			resultListTableData = skf3010Sc002GetContractInfoTableDataExpRepository.getContractInfo(param);
			param = null;

			// 取得レコード数を設定
			resultCount = resultListTableData.size();

			// 取得データレコード数判定
			if (resultCount <= 0) {
				// 契約情報変更モード判定
				if (Skf3010Sc002CommonDto.CONTRACT_MODE_ADD.equals(selectMode)) {
					// 追加
					// 契約番号リストが存在しない為、先頭に「1：」の選択値を設定
					Map<String, Object> contractMap = new HashMap<String, Object>();
					contractMap.put("value", "1");
					contractMap.put("label", "1：");
					contractMap.put("selected", true);
					// 契約番号リストに追加
					contractNoList.add(contractMap);
					contractMap = null;
				}
				// 取得データレコード数が0件場合、何もせず処理終了
				return resultCount;
			}
		}
		listTableData.clear();
		listTableData.addAll(setContractInfoListData(
				resultListTableData, resultContractNoList, selectedValue, deletedConstractNo, selectMode));
		contractNoList.clear();
		contractNoList.addAll(resultContractNoList);
		// 解放
		resultContractNoList = null;
		resultListTableData = null;

		return resultCount;
	}

	/**
	 * 契約情報設定。<br>
	 * 削除済み契約番号以降の契約番号は契約番号ドロップダウン選択値に設定しない
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param getContractListTableData	契約情報リスト(DB取得値)
	 * @param contractNoList			*契約番号リスト(ドロップダウン)
	 * @param selectedValue				契約番号ドロップダウン選択値
	 * @param deletedConstractNo		削除済み契約番号
	 * @param selectMode				契約情報変更モード
	 * @param 契約情報リスト
	 */
	private List<Map<String, Object>> setContractInfoListData(
			List<Skf3010Sc002GetShatakuContractInfoTableDataExp> getContractListTableData,
			List<Map<String, Object>> contractNoList, String selectedValue, String deletedConstractNo, String selectMode) {

		// 契約情報リスト
		List<Map<String, Object>> contractInfoList = new ArrayList<Map<String, Object>>();
		Map<String, Object> contractMap = null;
		// ドロップダウン選択値設定フラグ
		Boolean setSelectedValue = false;

		// 契約情報ループ
		for (Skf3010Sc002GetShatakuContractInfoTableDataExp tmpData : getContractListTableData) {
			Map<String, Object> tmpMap = new HashMap<String, Object>();

			/** DTO設定値 */
			// 契約番号
			String contractNo = "";
			// 賃貸人(代理人)氏名または名称
			String ownerName = "";
			// 賃貸人(代理人)番号
			String ownerNo = "";
			// 経理連携用管理番号
			String assetRegisterNo = "";
			// 契約開始日
			String contractStartDate = "";
			// 契約終了日
			String contractEndDate = "";
			// 家賃
			String rent = "";
			// 共益費
			String contractKyoekihi = "";
			// 駐車場料(地代)
			String landRent = "";
			// 備考
			String biko = "";

			// 「契約番号」取得
			if (tmpData.getContractPropertyId() != null) {
				contractNo = tmpData.getContractPropertyId().toString();
			}
			// 「賃貸人(代理人)氏名または名称」取得
			if (tmpData.getOwnerName() != null) {
				ownerName = tmpData.getOwnerName();
			}
			// 「賃貸人(代理人)氏名または名称」取得
			if (tmpData.getOwnerNo() != null) {
				ownerNo = tmpData.getOwnerNo().toString();
			}
			// 「経理連携用管理番号」取得
			if (tmpData.getAssetRegisterNo() != null) {
				assetRegisterNo = tmpData.getAssetRegisterNo();
			}
			// 「契約開始日」取得
			if (tmpData.getContractStartDate() != null) {
				contractStartDate = tmpData.getContractStartDate();
			}
			// 「契約終了日」取得
			if (tmpData.getContractStartDate() != null) {
				contractEndDate = tmpData.getContractEndDate();
			}
			// 「家賃」取得
			if (tmpData.getRent()!= null) {
				rent = tmpData.getRent().toString();
			}
			// 「共益費」取得
			if (tmpData.getContractKyoekihi() != null) {
				contractKyoekihi = tmpData.getContractKyoekihi().toString();
			}
			// 「駐車場料(地代)」取得
			if (tmpData.getLandRent() != null) {
				landRent = tmpData.getLandRent().toString();
			}
			// 「備考」取得
			if (tmpData.getBiko() != null) {
				biko = tmpData.getBiko();
			}

			// 戻り値設定
			tmpMap.put("contractNo", contractNo);
			// 表示・値を設定
			contractMap = new HashMap<String, Object>();
			contractMap.put("value", contractNo);
			contractMap.put("label", contractNo + CONTRACT_NO_SEPARATOR + contractStartDate);
			tmpMap.put("ownerName", ownerName);
			tmpMap.put("ownerNo", ownerNo);
			tmpMap.put("assetRegisterNo", assetRegisterNo);
			tmpMap.put("contractStartDate", contractStartDate);
			tmpMap.put("contractEndDate", contractEndDate);
			tmpMap.put("rent", rent);
			tmpMap.put("landRent", landRent);
			tmpMap.put("contractKyoekihi", contractKyoekihi);
			tmpMap.put("biko", biko);
			tmpMap.put("updateDate", tmpData.getUpdateDate());
			contractInfoList.add(tmpMap);
			tmpMap = null;
			// 選択値判定
			if (selectedValue != null && contractNo.equals(selectedValue)) {
				// 契約情報変更モード判定
				if (Skf3010Sc002CommonDto.CONTRACT_MODE_CHANGE.equals(selectMode)) {
					// 選択値に設定
					contractMap.put("selected", true);
					setSelectedValue = true;
				}
			}
			// 削除済み契約番号除外判定
			try {
				int deletedNo = Integer.parseInt(deletedConstractNo);
				if (deletedNo > 0 && deletedNo <= Integer.parseInt(contractNo)) {
					// 削除対象の為、契約情報番号リストに追加しない
					contractMap = null;
					continue;
				}
			} catch (NumberFormatException e) {}
			contractNoList.add(contractMap);
			contractMap = null;
		}
		// 契約情報変更モード判定
		if (Skf3010Sc002CommonDto.CONTRACT_MODE_ADD.equals(selectMode)) {
			// 追加時処理
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			String selectIndex = Integer.toString(contractNoList.size() + 1);
			// 契約番号リストに最大値+1の選択値を設定
			tmpMap.put("value", selectIndex);
			tmpMap.put("label", selectIndex + "：");
			tmpMap.put("selected", true);
			setSelectedValue = true;
			// 契約番号リストに追加
			contractNoList.add(tmpMap);
			tmpMap = null;
		}
		// 契約番号リスト選択値設定判定
		if (!setSelectedValue && contractNoList.size() > 0) {
			// 選択値未設定の為、最大値を選択状態に設定
			contractMap = new HashMap<String, Object>();
			contractMap = contractNoList.get(contractNoList.size() - 1);
			contractMap.put("selected", true);
			contractMap = null;
		}
		return contractInfoList;
	}


	/**
	 * 駐車場基本使用料計算。<br>
	 * 
	 * @param buildDate	建築年月日
	 * @param structureKbnCd	構造区分
	 * @param areaKbnCd	地域区分
	 * @param parkingStructureKbnCd	駐車場構造区分
	 * @return 社宅使用料計算結果
	 * @throws ParseException 
	 */
	private SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp getChyuushajouryouShiyouGaku(
			String buildDate, String structureKbnCd, String areaKbnCd, String parkingStructureKbnCd) throws ParseException {

		// 駐車場基本使用料計算情報取得
		SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp retEntity =
				new SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp();
		// 社宅利用料計算情報引数
		SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp inputEntity =
				new SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp();

		// 処理区分
		inputEntity.setShoriKbn("3");
		// 処理年月日
		inputEntity.setShoriNengetsu(skfBaseBusinessLogicUtils.getSystemProcessNenGetsu());
		// 建築年月日(入力した建築年月日)
		inputEntity.setKenchikuNengappi(buildDate);
		// 構造区分(入力した構造区分)
		inputEntity.setStructureKbn(structureKbnCd);
		// 地域区分(選択された地域区分)
		inputEntity.setAreaKbn(areaKbnCd);
		// 駐車場構造区分(選択された構造区分)
		inputEntity.setChuushajoKouzouKbn(parkingStructureKbnCd);
		// 役員区分フラグ
		inputEntity.setYakuinKbn(CodeConstant.STRING_ZERO);
		// 使用料計算
		retEntity = skfBaseBusinessLogicUtils.getShatakuShiyouryouKeisan(inputEntity);
		// 解放
		inputEntity = null;
		return retEntity;
	}

	/**
	 *  駐車場区画情報取得。<br>
	 *  DBより駐車場区画情報を取得し、画面用のリストに変換する
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param shatakuKanriNo	社宅管理番号
	 * @param parkingRent		駐車場基本使用料
	 * @param parkingBlockList	*駐車場区画リスト
	 */
	private void getShatakuParkingBlockInfo(
			String shatakuKanriNo, String parkingRent, List<Map<String, Object>> parkingBlockList) {

		// 駐車場区画情報リスト(DB取得値用)
		List<Skf3010Sc002GetShatakuParkingBlockTableDataExp> getDbParkingBlockList =
					new ArrayList<Skf3010Sc002GetShatakuParkingBlockTableDataExp>();
		Skf3010Sc002GetParkingBlockExpParameter param = new Skf3010Sc002GetParkingBlockExpParameter();
		// アクティブ年月を取得(システム処理年月)
		String activeNengetsu = "";
		activeNengetsu = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu();

		param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
		param.setActiveNengetsu(activeNengetsu);

		int resultCount = 0;
		// 駐車場情報をDBより取得
		getDbParkingBlockList = skf3010Sc002GetParkingBlockTableDataExpRepository.getParkingBlockInfo(param);

		// 取得レコード数を設定
		resultCount = getDbParkingBlockList.size();
		// 取得データレコード数判定
		if (resultCount == 0 ) {
			// 取得データレコード数が0件の場合、何もせず処理終了
			return;
		}
		parkingBlockList.clear();
		parkingBlockList.addAll(getShatakuParkingBlockListColumn(parkingRent, getDbParkingBlockList));
		// 解放
		getDbParkingBlockList = null;
		param = null;

		return;
	}

	/**
	 * 駐車場区画情報取得設定。<br>
	 * パラメータの駐車場区画情報リストを画面表示用のリストに変換する
	 * 
	 * @param parkingRent	駐車場基本使用料
	 * @param originList	駐車場区画情報リスト(DB取得値)
	 * @return	駐車場区画情報リスト(画面用)
	 */
	private List<Map<String, Object>> getShatakuParkingBlockListColumn(String parkingRent,
			List<Skf3010Sc002GetShatakuParkingBlockTableDataExp> originList) {

		List<Map<String, Object>> setViewList = new ArrayList<Map<String, Object>>();
		// 貸与区分ドロップダウン
		List<Map<String, Object>> lendStatusList =
				ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_LEND_KBN, "",false);
		// 駐車場月額使用料
		Long parkingRentMany = 0L;
		if (parkingRent != null && parkingRent.length() > 0) {
			parkingRentMany = Long.parseLong(parkingRent.replace(",", ""));
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");

		for (int i = 0; i < originList.size(); i++) {
			// 駐車場管理番号
			String parkingKanriNo = "";
			// 区画番号
			String blockNo = "";
			// 区画貸与区分
			String parkingLendKbn = "1";
			// 区画貸与状況
			String parkingLendStatus = "";
			// 使用者
			String shainName = "";
			// 駐車場調整金額
			Long parkingRentalAdjust = 0L;
			// 区画駐車場月額使用料
			Long  parkingBlockRentMany = parkingRentMany;
			// 備考
			String biko = "";

			Skf3010Sc002GetShatakuParkingBlockTableDataExp tmpData = originList.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();

			/** DB取得値設定 */
			// 駐車場管理番号
			if (tmpData.getParkingKanriNo() != null) {
				parkingKanriNo = tmpData.getParkingKanriNo().toString();
			}
			// 区画番号
			if (tmpData.getParkingBlock() != null) {
				blockNo = HtmlUtils.htmlEscape(tmpData.getParkingBlock());
			}
			// 区画貸与区分
			if (tmpData.getParkingLendKbn() != null) {
				parkingLendKbn = tmpData.getParkingLendKbn();
			}
			// 区画貸与状況
			if (tmpData.getParkingLendStatus() != null) {
				parkingLendStatus = tmpData.getParkingLendStatus();
			}
			// 使用者
			if (tmpData.getShainName() != null) {
				shainName = HtmlUtils.htmlEscape(tmpData.getShainName());
			}
			// 駐車場調整金額
			if (tmpData.getParkingRentalAdjust() != null) {
				parkingRentalAdjust = tmpData.getParkingRentalAdjust();
			}
			// 備考
			if (tmpData.getParkingBiko() != null) {
				biko = HtmlUtils.htmlEscape(tmpData.getParkingBiko());
			}
			// RelativeID
			tmpMap.put("rId", i);
			// 区画管理番号
			tmpMap.put("parkingKanriNo", HtmlUtils.htmlEscape(parkingKanriNo));
			// 区画番号
			tmpMap.put("parkingBlockNo", "<input id='parkingBlockNo" + i + "' name='parkingBlockNo" + i 
					+ "' type='text' value='" + blockNo + "' style='width:140px;' maxlength='30'/>");
			// 「貸与区分」プルダウンの設定
			String lendStatusListCode = 
					createStatusSelect(parkingLendKbn,lendStatusList);
			// 貸与区分ステータス判定
			if ("1".equals(parkingLendKbn)) {
				// 貸与可能(アクティブ)
				tmpMap.put("parkingLendKbn", "<select id='parkingLendKbn" + i 
						+ "' name='parkingLendKbn" + i + " 'style='width:90px;'>"
												+ lendStatusListCode + "</select>");
			} else {
				// 貸与不可(非活性)
				tmpMap.put("parkingLendKbn", "<select id='parkingLendKbn" + i 
						+ "' name='parkingLendKbn" + i + " 'style='width:90px;' disabled>"
												+ lendStatusListCode + "</select>");
			}
			// 貸与状況
			tmpMap.put("parkingLendStatus", HtmlUtils.htmlEscape(parkingLendStatus));
			// 使用者
			tmpMap.put("userName", shainName);
			// 駐車場調整金額
			// String.format("%,d", obj);
			tmpMap.put("parkingRentalAdjust", "<input id='parkingRentalAdjust" + i 
					+ "' name='parkingRentalAdjust" + i + "' type='text' class='ime-off' value='"
					+ HtmlUtils.htmlEscape(String.format("%,d", parkingRentalAdjust))
									+ "' style='width:65px;text-align: right;' maxlength='6'/> 円");
			// 駐車場月額使用料
			parkingBlockRentMany += parkingRentalAdjust;
			tmpMap.put("parkingMonthRental", "<label id='parkingMonthRental" + i
					+ "' name='parkingMonthRental" + i + "' >"
					+ HtmlUtils.htmlEscape(String.format("%,d", parkingBlockRentMany) + " 円")
					+ "</label>");
			// 駐車場備考
			tmpMap.put("parkingBlockBiko", "<input id='parkingBlockBiko" + i 
					+ "' name='parkingBlockBiko" + i + "' type='text' value='"
					+ biko + " ' style='width:215px;' maxlength='100'/>");
			// 削除ボタン
			tmpMap.put("parkingBlockDelete", "");
			// 更新日時
			if(tmpData.getUpdateDate() != null){
				tmpMap.put("updateDate", HtmlUtils.htmlEscape(dateFormat.format(tmpData.getUpdateDate())));
			}else{
				tmpMap.put("updateDate", HtmlUtils.htmlEscape(""));
			}
			setViewList.add(tmpMap);
		}
		// 解放
		lendStatusList = null;
		dateFormat = null;

		return setViewList;
	}

	/**
	 * プルダウンの中身作成。<br>
	 * 
	 * パラメータの選択値リストから選択プルダウンの中身を作成する。
	 * パラメータの選択値指定がない場合は未指定で作成する。
	 * 
	 * @param selectedValue	選択値
	 * @param slectValueList	選択値リスト
	 * @return	プルダウンの中身
	 */
	private String createStatusSelect(
			String selectedValue,List<Map<String, Object>> slectValueList){

		String returnListCode = "";

		for(Map<String, Object> obj : slectValueList){
			String value = obj.get("value").toString();
			String label = obj.get("label").toString();
			if (value.compareTo(selectedValue) == 0) {
				// ステータスとリスト値が一致する場合、選択中にする
				returnListCode += "<option value='" + value + "' selected>" + label + "</option>";
			} else {
				returnListCode += "<option value='" + value + "'>" + label + "</option>";	
			}
		}
		return returnListCode;
	}

	/**
	 * 貸与区分プルダウンの中身作成。<br>
	 * 
	 * パラメータの選択値リストから選択プルダウンの中身を作成する。
	 * 
	 * @return	貸与区分プルダウンの中身
	 */
	private String createLendKbnSelectList(){

		String returnListCode = "";
		// 貸与区分ドロップダウン
		List<Map<String, Object>> lendStatusList =
				ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_LEND_KBN, "",false);

		returnListCode = createStatusSelect("", lendStatusList);
		return returnListCode;
	}

	/**
	 * 新規時の備品情報リストの取得.
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param shatakuKanriNo 	社宅管理番号
	 * @param bihinListData 	*備品情報リスト
	 * @param hdnBihinStatusList *非表示備品情報リスト
	 * @return
	 */
	private int setBihinInfoOfShinki(
			List<Map<String, Object>> bihinListData,List<Map<String, Object>> hdnBihinStatusList){

		LogUtils.debugByMsg("備品情報（新規）取得処理開始");

		int resultCount = 0;

		List<Skf3010Sc002GetBihinInfoTableDataExp> bihinInfoList =
					new ArrayList<Skf3010Sc002GetBihinInfoTableDataExp>();
		bihinInfoList = skf3010Sc002GetBihinInfoTableDataExpRepository.getBihinName();

		// 取得レコード数を設定
		resultCount = bihinInfoList.size();

		// 取得データレコード数判定
		if (resultCount == 0 ) {
			// 取得データレコード数が0件の場合、何もせず処理終了
			return resultCount;
		}

		//備品情報リスト取得
		bihinListData.clear();
		bihinListData.addAll(getBihinListColumn(bihinInfoList));
		//非表示備品リスト取得
		hdnBihinStatusList.clear();
		hdnBihinStatusList.addAll(getHdnBihinListColumn(bihinInfoList));
		// 解放
		bihinInfoList = null;

		return resultCount;
	}

	/**
	 * 備品情報リストの取得。<br>
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param shatakuKanriNo 		社宅管理番号
	 * @param bihinListData 		*備品情報リスト
	 * @param hdnBihinStatusList 	*非表示備品情報リスト
	 * @return
	 */
	private int setBihinInfo(String shatakuKanriNo,
			List<Map<String, Object>> bihinListData,List<Map<String, Object>> hdnBihinStatusList){

		LogUtils.debugByMsg("備品情報取得処理開始");
		LogUtils.debugByMsg("引数から取得した値：" + "社宅管理番号=" + shatakuKanriNo);
		List<Skf3010Sc002GetBihinInfoTableDataExp> bihinInfoList =
					new ArrayList<Skf3010Sc002GetBihinInfoTableDataExp>();

		Skf3010Sc002GetHoyuShatakuInfoExpParameter param = new Skf3010Sc002GetHoyuShatakuInfoExpParameter();
		param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));

		int resultCount = 0;
		bihinInfoList = skf3010Sc002GetBihinInfoTableDataExpRepository.getBihinInfo(param);

		// 取得レコード数を設定
		resultCount = bihinInfoList.size();

		// 取得データレコード数判定
		if (resultCount == 0 ) {
			// 取得データレコード数が0件の場合、何もせず処理終了
			return resultCount;
		}

		//備品情報リスト取得
		bihinListData.clear();
		bihinListData.addAll(getBihinListColumn(bihinInfoList));
		//非表示備品リスト取得
		hdnBihinStatusList.clear();
		hdnBihinStatusList.addAll(getHdnBihinListColumn(bihinInfoList));
		// 解放
		bihinInfoList = null;
		param = null;

		return resultCount;
	}

	/**
	 * 備品リストに出力するリストを取得する。<br>
	 * 
	 * @param originList
	 * @return リストテーブルに出力するリスト
	 */
	private List<Map<String, Object>> getBihinListColumn(List<Skf3010Sc002GetBihinInfoTableDataExp> originList) {

		List<Map<String, Object>> setViewList = new ArrayList<Map<String, Object>>();

		List<Map<String, Object>> statusList = ddlUtils.getGenericForDoropDownList(
							FunctionIdConstant.GENERIC_CODE_BIHINSTATUS_KBN, "",false);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");

		for (int i = 0; i < originList.size(); i++) {
			/** DTO設定用 */
			// 備品コード
			String bihinCode = "";
			// 備品名
			String bihinName = "";
			// 備付状況選択値
			String bihinStatus = "0";

			Skf3010Sc002GetBihinInfoTableDataExp tmpData = originList.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();

			if(!tmpData.getDispFlg().equals("0")){
				// 備品コード
				if (tmpData.getBihinCd() != null) {
					bihinCode = HtmlUtils.htmlEscape(tmpData.getBihinCd());
				}
				tmpMap.put("bihinCode", bihinCode);
				// 備品名
				if (tmpData.getBihinName() != null) {
					bihinName = HtmlUtils.htmlEscape(tmpData.getBihinName());
				}
				tmpMap.put("bihinName", bihinName);
				// 「備付状況」選択値
				if (tmpData.getBihinStatusKbn() != null) {
					bihinStatus = tmpData.getBihinStatusKbn();
				}
				// 備付状況
				String statusListCode = createStatusSelect(bihinStatus,statusList);
				tmpMap.put("bihinStatus","<select id='bihinStatus" + i 
						+ "' name='bihinStatus" + i + "'>" + statusListCode + "</select>");
				// 更新日時
				if(tmpData.getUpdateDate() != null){
					tmpMap.put("updateDate", HtmlUtils.htmlEscape(dateFormat.format(tmpData.getUpdateDate())));
				}else{
					tmpMap.put("updateDate", HtmlUtils.htmlEscape(""));
				}
				setViewList.add(tmpMap);
			}
		}
		// 解放
		dateFormat = null;
		statusList = null;

		return setViewList;
	}

	/**
	 * 非表示備品リストに出力するリストを取得する。<br>
	 * 
	 * @param originList
	 * @return リストテーブルに出力するリスト
	 */
	private List<Map<String, Object>> getHdnBihinListColumn(
				List<Skf3010Sc002GetBihinInfoTableDataExp> originList) {

		List<Map<String, Object>> setViewList = new ArrayList<Map<String, Object>>();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");

		for (int i = 0; i < originList.size(); i++) {
			/** DTO設定用 */
			// 備品コード
			String bihinCode = "";
			// 備品名
			String bihinName = "";
			// 備付状況選択値
			String bihinStatus = "0";

			Skf3010Sc002GetBihinInfoTableDataExp tmpData = originList.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			// 表示/非表示判定
			if(tmpData.getDispFlg().equals("0")){
				//DispFlgが0（非表示）
				// 備品コード
				if (tmpData.getBihinCd() != null) {
					bihinCode = tmpData.getBihinCd();
				}
				tmpMap.put("bihinCode", bihinCode);
				// 備品名
				if (tmpData.getBihinName() != null) {
					bihinName = tmpData.getBihinName();
				}
				tmpMap.put("bihinName", bihinName);
				// 「備付状況」選択値
				if (tmpData.getBihinStatusKbn() != null) {
					bihinStatus = tmpData.getBihinStatusKbn();
				}
				// 「備付状況」の設定
				if (tmpData.getBihinStatusKbn() != null) {
					bihinStatus = tmpData.getBihinStatusKbn();
				}
				tmpMap.put("bihinStatus",bihinStatus);
				// 更新日時
				if(tmpData.getUpdateDate() != null){
					tmpMap.put("updateDate", dateFormat.format(tmpData.getUpdateDate()));
				}else{
					tmpMap.put("updateDate", "");
				}
				setViewList.add(tmpMap);
			}
		}
		// 解放
		dateFormat = null;

		return setViewList;
	}

	/**
	 * 社宅部屋情報取得。<br>
	 * 排他制御用に社宅の部屋情報を取得する。
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param shatakuKanriNo		社宅管理番号
	 * @param shatakuRoomListData	*社宅部屋情報リスト
	 * @return	取得レコード数
	 */
	private int getShatakuRoomInfo(String shatakuKanriNo, List<Map<String, Object>> shatakuRoomListData) {

		int resultCnt = 0;
		List<Skf3010Sc002GetShatakuRoomExlusiveCntrlExp> shatakuRoomList
					= new ArrayList<Skf3010Sc002GetShatakuRoomExlusiveCntrlExp>();
		Skf3010Sc002GetHoyuShatakuInfoExpParameter param = new Skf3010Sc002GetHoyuShatakuInfoExpParameter();
		param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
		// DBより社宅部屋情報を取得する
		skf3010Sc002GetRoomExlusiveCntrTableDataExpRepository.getSRoomExlusiveCntr(param);

		// 取得レコード数を設定
		resultCnt = shatakuRoomList.size();

		// 取得データレコード数判定
		if (resultCnt == 0 ) {
			// 取得データレコード数が0件の場合、何もせず処理終了
			return resultCnt;
		}
		//部屋情報リスト設定
		shatakuRoomListData.clear();
		shatakuRoomListData.addAll(getShatakuRoomListColumn(shatakuRoomList));
		// 解放
		shatakuRoomList = null;
		param = null;

		return resultCnt;
	}

	/**
	 * 社宅部屋情報変換。<br>
	 * パラメータのDBより取得した社宅部屋情報リストをDTO変数設定用に加工する
	 * 
	 * @param originList	社宅部屋情報リスト(DB取得値)
	 * @return
	 */
	private List<Map<String, Object>> getShatakuRoomListColumn(
			List<Skf3010Sc002GetShatakuRoomExlusiveCntrlExp> originList) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		// 変換
		for (int i = 0; i < originList.size(); i++) {
			Skf3010Sc002GetShatakuRoomExlusiveCntrlExp tmpData = originList.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			tmpMap.put("shatakuKanriNo",tmpData.getShatakuKanriNo());
			tmpMap.put("shatakuRoomKanriNo",tmpData.getShatakuRoomKanriNo());
			tmpMap.put("updateDate",tmpData.getUpdateDate());
			resultList.add(tmpMap);
		}
		return resultList;
	}

	/**
	 * 社宅部屋備品情報取得。<br>
	 * 排他制御用に社宅の部屋備品情報を取得する。
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param shatakuKanriNo				社宅管理番号
	 * @param roomeBihinExlusiveCntrllList	*社宅部屋備品情報リスト
	 * @return	取得レコード数
	 */
	private int getShatakuRoomBihinInfo(String shatakuKanriNo, List<Map<String, Object>> roomeBihinExlusiveCntrllList) {

		int resultCnt = 0;
		List<Skf3010Sc002GetRoomBihinExlusiveCntrlExp> shatakuRoomList =
						new ArrayList<Skf3010Sc002GetRoomBihinExlusiveCntrlExp>();
		Skf3010Sc002GetHoyuShatakuInfoExpParameter param = new Skf3010Sc002GetHoyuShatakuInfoExpParameter();
		param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
		// DBより社宅部屋備品情報を取得する
		skf3010Sc002GetRoomBihinExlusiveCntrTableDataExpRepository.getRoomBihinExlusiveCntr(param);

		// 取得レコード数を設定
		resultCnt = shatakuRoomList.size();

		// 取得データレコード数判定
		if (resultCnt == 0 ) {
			// 取得データレコード数が0件の場合、何もせず処理終了
			return resultCnt;
		}
		// 備品情報リスト取得
		roomeBihinExlusiveCntrllList.clear();
		roomeBihinExlusiveCntrllList.addAll(getShatakuRoomBihinListColumn(shatakuRoomList));
		// 解放
		shatakuRoomList = null;
		param = null;

		return resultCnt;
	}

	/**
	 * 社宅部屋備品情報変換。<br>
	 * パラメータのDBより取得した社宅部屋備品情報リストをDTO変数設定用に加工する
	 * 
	 * @param originList	社宅部屋備品情報リスト(DB取得値)
	 * @return
	 */
	private List<Map<String, Object>> getShatakuRoomBihinListColumn(
				List<Skf3010Sc002GetRoomBihinExlusiveCntrlExp> originList) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		// 変換
		for (int i = 0; i < originList.size(); i++) {
			Skf3010Sc002GetRoomBihinExlusiveCntrlExp tmpData = originList.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			tmpMap.put("shatakuKanriNo",tmpData.getShatakuKanriNo());
			tmpMap.put("shatakuRoomKanriNo",tmpData.getShatakuRoomKanriNo());
			tmpMap.put("bihinCd",tmpData.getBihinCd());
			tmpMap.put("updateDate",tmpData.getUpdateDate());
			resultList.add(tmpMap);
		}
		return resultList;
	}

	/**
	 * ドロップダウンリストに設定するリストを取得する。<br>
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param useKbn					利用区分コード選択値
	 * @param useKbnList				*利用区分プルダウン
	 * @param manageCompanyCd			管理会社コード選択値
	 * @param manageCompanyList			*管理会社プルダウン
	 * @param manageAgencyCd			管理機関コード選択値
	 * @param manageAgencyList			*管理機関プルダウン
	 * @param manageBusinessAreaCd		管理事業領域コード選択値
	 * @param manageBusinessAreaList	*管理事業領域プルダウン
	 * @param prefCd					都道府県コード選択値
	 * @param prefList					*都道府県プルダウン
	 * @param structureKbn				社宅構造区分コード選択値
	 * @param shatakuStructureList		*社宅構造プルダウン
	 * @param elevatorKbn				エレベーター区分選択値
	 * @param elevatorList				*エレベータープルダウン
	 */
	public void getDoropDownList(
			String useKbn, List<Map<String, Object>> useKbnList,
			String manageCompanyCd, List<Map<String, Object>> manageCompanyList,
			String manageAgencyCd, List<Map<String, Object>> manageAgencyList,
			String manageBusinessAreaCd, List<Map<String, Object>> manageBusinessAreaList,
			String prefCd, List<Map<String, Object>> prefList,
			String structureKbn, List<Map<String, Object>> shatakuStructureList,
			String elevatorKbn, List<Map<String, Object>> elevatorList) {

		// 利用区分プルダウン
		useKbnList.clear();
		useKbnList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_RIYO_KBN, useKbn, false));
		// 管理会社プルダウン
		manageCompanyList.clear();
		manageCompanyList.addAll(ddlUtils.getDdlCompanyByCd(manageCompanyCd, true));
		// 管理機関プルダウン
		manageAgencyList.clear();
		manageAgencyList.addAll(ddlUtils.getDdlAgencyByCd(manageCompanyCd, manageAgencyCd, true));
		// 管理事業領域プルダウン
		manageBusinessAreaList.clear();
		manageBusinessAreaList.addAll(ddlUtils.getDdlBusinessAreaByCd(
				manageCompanyCd, manageAgencyCd, manageBusinessAreaCd, true));
		// 都道府県プルダウン
		prefList.clear();
		prefList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_PREFCD, prefCd, true));
		// 社宅構造プルダウン
		shatakuStructureList.clear();
		shatakuStructureList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_STRUCTURE_KBN, structureKbn, true));
		// エレベータープルダウン
		elevatorList.clear();
		elevatorList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_ELEVATOR_KBN, elevatorKbn, true));
	}

	/**
	 * 実年数、経年、次回算定年月日設定
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param nextCalcDate	*次回算定年月日
	 * @param jituAge		*実経年
	 * @param aging			*経年
	 * @param buildDate		建築年月日
	 * @param areaKbnCd		地域区分コード
	 * @param structureKbn	構造区分コード
	 * @throws Exception
	 * @return true/false
	 */
	public Boolean setNextCalcDateKeinenJitukeinen(StringBuilder nextCalcDate, StringBuilder jituAge, StringBuilder aging,
			String buildDate, String areaKbnCd, String structureKbn) throws Exception {

		String editBuildDate = buildDate.toString();
		// 建築年月日設定判定
		if (editBuildDate != null && editBuildDate.length() > 0) {
			editBuildDate = editBuildDate.replace("/", "");
			// 日付形式チェック
			if (!SkfCheckUtils.isSkfDateFormat(editBuildDate.trim(), CheckUtils.DateFormatType.YYYYMMDD)) {
				logger.debug("日付形式不正");
				return false;
			}
			try {
				// 日付上限チェック
				if (Integer.parseInt(editBuildDate.trim()) > DATE_MAX) {
					logger.debug("日付上限超過");
					return false;
				}
			} catch (NumberFormatException e) {}
			// 実年と経年の設定
			setJituKeinen(jituAge, aging, editBuildDate, areaKbnCd, structureKbn);
			String jikaiNengappi = 
					skfBaseBusinessLogicUtils.getJikaiSanteiNengappi(editBuildDate, areaKbnCd, structureKbn);
			// 次回年月日の設定
			nextCalcDate.setLength(0);
			nextCalcDate.append(skfDateFormatUtils.dateFormatFromString(
							jikaiNengappi, SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH));
		}
		return true;
	}

	/**
	 * 実年数と経年の設定。
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param jituAge		*実経年
	 * @param aging			*経年
	 * @param buildDate		建築年月日
	 * @param areaKbnCd		地域区分コード
	 * @param structureKbn	構造区分コード
	 * @throws ParseException
	 */
	private void setJituKeinen(StringBuilder jituAge, StringBuilder aging,
			String buildDate, String areaKbnCd, String structureKbn) throws ParseException {
		// 実経年の取得
		Integer jitsuKeinen = skfBaseBusinessLogicUtils.getJitsuAging(buildDate);
		// 実経年設定
		jituAge.setLength(0);
		jituAge.append(jitsuKeinen.toString());
		jituAge.append(" 年");
		// 経年取得
		Long keinen = skfBaseBusinessLogicUtils.getAging(buildDate, areaKbnCd, structureKbn);
		// 経年設定
		aging.setLength(0);
		aging.append(keinen.toString());
		aging.append(" 年");
	}

	/**
	 * 駐車場基本使用料、駐車場使用料算出
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param buildDate				建築年月日
	 * @param structureKbnCd		構造区分コード
	 * @param areaKbnCd				地域区分コード
	 * @param parkingStructure		駐車場構造区分コード
	 * @param parkingRentalAdjusts	駐車場調整金額リスト
	 * @param parkingBlockRentManys	*駐車場区画月額使用料リスト
	 * @param parkingRentalValue	*駐車場基本使用料
	 * @throws ParseException
	 */
	public void parkingCalc(String buildDate, String structureKbnCd,
					String areaKbnCd, String parkingStructure, List <String> parkingRentalAdjusts,
					List <String> parkingBlockRentManys, StringBuilder parkingRentalValue) throws ParseException {

		// 駐車場基本使用料
		Long parkingRentalBasicValue = 0L;
		// 駐車場基本使用料計算情報取得
		// 建築年月日、構造区分、地域区分、駐車場構造区分をパラメータ指定
		SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp retEntity =
				getChyuushajouryouShiyouGaku(buildDate, structureKbnCd, areaKbnCd, parkingStructure);
		// 駐車場基本使用料設定
		if (retEntity.getChushajouShiyoryou() != null) {
			parkingRentalBasicValue = retEntity.getChushajouShiyoryou().longValue();
		}
		// 駐車場基本料金が変更となるので、駐車場区分全体に対して再計算処理を行う。
		calcParkingBlockListRentMany(parkingRentalBasicValue, parkingRentalAdjusts, parkingBlockRentManys);
		// 駐車場基本使用料設定
		parkingRentalValue.setLength(0);
		parkingRentalValue.append(String.format("%,d", parkingRentalBasicValue));
	}

	/**
	 * 駐車場全区画使用料計算
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param parkingBasicValue			駐車場基本使用料
	 * @param parkingRentalAdjusts		駐車場調整金額リスト
	 * @param parkingBlockRentManys		*駐車場区画使用料リスト
	 */
	public void calcParkingBlockListRentMany(Long parkingBasicValue, 
			List <String> parkingRentalAdjusts, List <String> parkingBlockRentManys) {

		// 各駐車場区分情報を取得
		for (int i = 0; i < parkingRentalAdjusts.size(); i++) {
			// 駐車場調整金額
			Long parkingRentalAdjust = 0L;

			try {
				parkingRentalAdjust = Long.parseLong(parkingRentalAdjusts.get(i));
			} catch(NumberFormatException e) {}

			// 駐車場区画使用料リスト設定
			parkingBlockRentManys.add(HtmlUtils.htmlEscape(
					String.format("%,d", parkingBasicValue + parkingRentalAdjust) + " 円"));
		}
	}

	/**
	 * 駐車場貸与区分状況変更時計算
	 * パラメータの貸与区分、貸与状況から駐車場台数、貸与可能総数計算を実施する。
	 * 
	 * 
	 * @param parkingBlockRowData	貸与区分、貸与状況のリスト(0番目が貸与区分)
	 * @param parkingBlockCount
	 * @param lendPossibleCount
	 * @return	計算結果(0番目が駐車場台数、1番目が貸与可能総数)
	 */
	public List <Integer> parkingLendChangeCalc(
			List <String> parkingBlockRowData, String parkingBlockCount, String lendPossibleCount) {

		/** 戻り値 */
		List <Integer> parkingBlockCalcNum = new ArrayList<Integer>();
		// 駐車場台数
		int blockCnt = 0;
		// 貸与可能総数
		int possibleCount = 0;

		/** DTO取得 */
		// 貸与区分取得
		String lendKbn = parkingBlockRowData.get(0);
		// 貸与状況取得
		String lendStatus = parkingBlockRowData.get(1);
		// 数値変換
		try {
			// 駐車場台数
			blockCnt = Integer.parseInt(parkingBlockCount);
			// 貸与可能総数
			possibleCount = Integer.parseInt(lendPossibleCount);
		} catch (NumberFormatException e) {}

		// 貸与区分判定
		if (CodeConstant.PARKING_LEND_TAIYO_FUKA.equals(lendKbn)) {
			// 貸与状況判定
			if (CodeConstant.LEND_JOKYO_NASHI.equals(lendStatus) || CodeConstant.LEND_JOKYO_TAIKYO_YOTEI.equals(lendStatus)) {
				/** なし、または退居予定 */
				// 駐車場台数減算
				blockCnt--;
				// 貸与可能総数減残
				possibleCount--;
			} else if (CodeConstant.LEND_JOKYO_TEIJICHU.equals(lendStatus) || CodeConstant.LEND_JOKYO_TAIYOCHU.equals(lendStatus)) {
				/** 提示中、または貸与中 */
				// 駐車場台数減算
				blockCnt--;
			}
		} else if (CodeConstant.PARKING_LEND_TAIYO_KANO.equals(lendKbn)) {
			// 貸与状況判定
			if (CodeConstant.LEND_JOKYO_NASHI.equals(lendStatus) || CodeConstant.LEND_JOKYO_TAIKYO_YOTEI.equals(lendStatus)) {
				/** なし、または退居予定 */
				// 駐車場台数加算
				blockCnt++;
				// 貸与可能総数加算
				possibleCount++;
			} else if (CodeConstant.LEND_JOKYO_TEIJICHU.equals(lendStatus) || CodeConstant.LEND_JOKYO_TAIYOCHU.equals(lendStatus)) {
				/** 提示中、または貸与中 */
				// 駐車場台数加算
				blockCnt++;
			}
		}
		parkingBlockCalcNum.add(blockCnt);
		parkingBlockCalcNum.add(possibleCount);
		return parkingBlockCalcNum;
	}

	/**
	 * 駐車場区画削除時計算
	 * パラメータの貸与区分、貸与状況から駐車場台数、貸与可能総数計算を実施する。
	 * 
	 * 
	 * @param parkingBlockRowData	貸与区分、貸与状況のリスト(0番目が貸与区分)
	 * @param parkingBlockCount
	 * @param lendPossibleCount
	 * @return	計算結果(0番目が駐車場台数、1番目が貸与可能総数)
	 */
	public List <Integer> parkingBlockDeleteCalc(
			List <String> parkingBlockRowData, String parkingBlockCount, String lendPossibleCount) {

		/** 戻り値 */
		List <Integer> parkingBlockCalcNum = new ArrayList<Integer>();
		// 駐車場台数
		int blockCnt = 0;
		// 貸与可能総数
		int possibleCount = 0;

		/** DTO取得 */
		// 貸与区分取得
		String lendKbn = parkingBlockRowData.get(0);
		// 貸与状況取得
		String lendStatus = parkingBlockRowData.get(1);
		// 数値変換
		try {
			// 駐車場台数
			blockCnt = Integer.parseInt(parkingBlockCount);
			// 貸与可能総数
			possibleCount = Integer.parseInt(lendPossibleCount);
		} catch (NumberFormatException e) {}

		// 貸与区分判定
		if (CodeConstant.PARKING_LEND_TAIYO_KANO.equals(lendKbn)) {
			// 貸与状況判定
			if (CodeConstant.LEND_JOKYO_NASHI.equals(lendStatus) || CodeConstant.LEND_JOKYO_TAIKYO_YOTEI.equals(lendStatus)) {
				/** なし、または退居予定 */
				// 駐車場台数減算
				blockCnt--;
				// 貸与可能総数減残
				possibleCount--;
			} else if (CodeConstant.LEND_JOKYO_TEIJICHU.equals(lendStatus) || CodeConstant.LEND_JOKYO_TAIYOCHU.equals(lendStatus)) {
				/** 提示中、または貸与中 */
				// 駐車場台数減算
				blockCnt--;
			}
		} else if (CodeConstant.PARKING_LEND_TAIYO_FUKA.equals(lendKbn)) {
			// 駐車場台数減算
//			blockCnt--;
		}
		parkingBlockCalcNum.add(blockCnt);
		parkingBlockCalcNum.add(possibleCount);
		return parkingBlockCalcNum;
	}

	/**
	 * 契約情報設定。(新規)<br>
	 * 新規用
	 * 
	 * @param initDto			DTO
	 */
	private void setContractInfoShinki(String selectMode, Skf3010Sc002CommonDto initDto) {

		// 契約情報リスト
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
		// 契約番号リスト(ドロップダウン)
		List<Map<String, Object>> contractNoList = new ArrayList<Map<String, Object>>();

		// 契約情報追加ボタン(非活性：true, 活性:false)
		Boolean contractAddDisableFlg = false;
		// 契約情報削除ボタン(非活性：true, 活性:false)
		Boolean contractDelDisableFlg = true;

		// 契約情報変更モード判定
		if (Skf3010Sc002CommonDto.CONTRACT_MODE_ADD.equals(selectMode)) {
			// 契約番号リストが存在しない為、先頭に「1：」の選択値を設定
			Map<String, Object> contractMap = new HashMap<String, Object>();
			contractMap.put("value", "1");
			contractMap.put("label", "1：");
			contractMap.put("selected", true);
			// 契約番号リストに追加
			contractNoList.add(contractMap);
			contractMap = null;
			// 契約情報追加ボタンを非活性
			contractAddDisableFlg = true;
			// 契約情報削除ボタンを活性
			contractDelDisableFlg = false;
		}

		/** 契約情報設定 */
		initDto.setContractNoList(null);
		initDto.setContractOwnerName(null);
		initDto.setContractOwnerNo(null);
		initDto.setAssetRegisterNo(null);
		initDto.setContractStartDay(null);
		initDto.setContractEndDay(null);
		initDto.setContractRent(null);
		initDto.setContractKyoekihi(null);
		initDto.setContractLandRent(null);
		initDto.setContractBiko(null);
		initDto.setContractUpdateDate(null);
		initDto.setContractInfoListTableData(null);
		initDto.setContractAddDisableFlg(null);
		initDto.setContractDelDisableFlg(null);
		initDto.setContractInfoListTableData(listTableData);
		initDto.setContractNoList(contractNoList);
		initDto.setContractOwnerName("");
		initDto.setContractOwnerNo("");
		initDto.setAssetRegisterNo("");
		initDto.setContractStartDay("");
		initDto.setContractEndDay("");
		initDto.setContractRent("");
		initDto.setContractKyoekihi("");
		initDto.setContractLandRent("");
		initDto.setContractBiko("");
		initDto.setContractAddDisableFlg(contractAddDisableFlg);
		initDto.setContractDelDisableFlg(contractDelDisableFlg);
		// 解放
		listTableData = null;
		contractNoList = null;
	}

	/**
	 * 契約情報設定(同期処理専用)
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param shatakuKanriNo			社宅管理番号
	 * @param contractSelectedIndex		契約情報選択値
	 * @param deletedConstractNo		削除済み契約番号
	 * @param selectMode				契約情報変更モード
	 * @param initDto					*DTO
	 */
	private void setContractInfo(String shatakuKanriNo, String contractSelectedIndex,
					String deletedConstractNo, String selectMode, Skf3010Sc002CommonDto initDto) {

		// 契約情報リスト
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
		// 契約番号リスト(ドロップダウン)
		List<Map<String, Object>> contractNoList = new ArrayList<Map<String, Object>>();

		/** DTO設定値 */
		// 賃貸人（代理人）名
		String contractOwnerName = "";
		// 賃貸人（代理人）番号
		String contractOwnerNo = "";
		// 経理連携用管理番号
		String assetRegisterNo = "";
		// 契約開始日
		String contractStartDate = "";
		// 契約終了日
		String contractEndDate = "";
		// 家賃
		String rent = "";
		// 共益費
		String contractKyoekihi = "";
		// 駐車場料(地代)
		String landRent = "";
		// 備考
		String biko = "";
		// 契約情報追加ボタン(非活性：true, 活性:false)
		Boolean contractAddDisableFlg = true;
		// 契約情報削除ボタン(非活性：true, 活性:false)
		Boolean contractDelDisableFlg = true;
		// 選択値インデックス
		String selectIndex = contractSelectedIndex;
		// 契約情報をDBより取得する
		getShatakuContractInfo(shatakuKanriNo, listTableData, contractNoList, selectIndex, deletedConstractNo, selectMode);
		// 選択契約情報
		Map<String, Object> contractMap = new HashMap<String, Object>();
		// 契約情報変更モード判定
		if (Skf3010Sc002CommonDto.CONTRACT_MODE_CHANGE.equals(selectMode)
				|| Skf3010Sc002CommonDto.CONTRACT_MODE_DEL.equals(selectMode)) {
			// 変更、削除の場合
			// 追加ボタン活性
			contractAddDisableFlg = false;
		} else if (Skf3010Sc002CommonDto.CONTRACT_MODE_ADD.equals(selectMode)) {
			// 追加の場合
			// 追加ボタン非活性
			contractAddDisableFlg = true;
		} else {
			// 初期表示
			// 追加ボタン活性
			contractAddDisableFlg = false;
		}
		// 削除ボタン活性/非活性判定
		if (contractNoList.size() > 0) {
			// 削除ボタン活性
			contractDelDisableFlg = false;
		}

		contractMap = new HashMap<String, Object>();
		// 契約情報リスト選択契約番号取得
		for (Map<String, Object> contractNoMap : contractNoList) {
			if (contractNoMap.containsKey("selected")) {
				selectIndex = contractNoMap.get("value").toString();
				break;
			}
		}
		// 選択契約情報取得
		for (Map<String, Object> contractDataMap : listTableData) {
			if (contractDataMap.get("contractNo").equals(selectIndex)) {
				contractMap = contractDataMap;
				break;
			}
		}

		// 「賃貸人(代理人)氏名または名称」取得
		if (contractMap.get("ownerName") != null) {
			contractOwnerName = contractMap.get("ownerName").toString();
		}
		// 「賃貸人(代理人)番号」取得
		if (contractMap.get("ownerNo") != null) {
			contractOwnerNo = contractMap.get("ownerNo").toString();
		}
		// 「経理連携用管理番号」取得
		if (contractMap.get("assetRegisterNo") != null) {
			assetRegisterNo = contractMap.get("assetRegisterNo").toString();
		}
		// 「契約開始日」取得
		if (contractMap.get("contractStartDate") != null) {
			contractStartDate = contractMap.get("contractStartDate").toString();
		}
		// 「契約終了日」取得
		if (contractMap.get("contractEndDate") != null) {
			contractEndDate = contractMap.get("contractEndDate").toString();
		}
		// 「家賃」取得
		if (contractMap.get("rent") != null) {
			rent = contractMap.get("rent").toString();
		}
		// 「共益費」取得
		if (contractMap.get("contractKyoekihi") != null) {
			contractKyoekihi = contractMap.get("contractKyoekihi").toString();
		}
		// 「駐車場料(地代)」取得
		if (contractMap.get("landRent") != null) {
			landRent = contractMap.get("landRent").toString();
		}
		// 「備考」取得
		if (contractMap.get("biko") != null) {
			biko = contractMap.get("biko").toString();
		}
		contractMap = null;

		/** 契約情報設定 */
		initDto.setContractNoList(null);
		initDto.setContractOwnerName(null);
		initDto.setContractOwnerNo(null);
		initDto.setAssetRegisterNo(null);
		initDto.setContractStartDay(null);
		initDto.setContractEndDay(null);
		initDto.setContractRent(null);
		initDto.setContractKyoekihi(null);
		initDto.setContractLandRent(null);
		initDto.setContractBiko(null);
		initDto.setContractUpdateDate(null);
		initDto.setContractInfoListTableData(null);
		initDto.setContractAddDisableFlg(null);
		initDto.setContractDelDisableFlg(null);
		initDto.setHdnChangeContractSelectedIndex(null);
		initDto.setHdnDispContractSelectedIndex(null);
		initDto.setSelectMode(null);
		initDto.setContractInfoListTableData(listTableData);
		initDto.setContractNoList(contractNoList);
		initDto.setContractOwnerName(contractOwnerName);
		initDto.setContractOwnerNo(contractOwnerNo);
		initDto.setAssetRegisterNo(assetRegisterNo);
		initDto.setContractStartDay(contractStartDate);
		initDto.setContractEndDay(contractEndDate);
		initDto.setContractRent(rent);
		initDto.setContractKyoekihi(contractKyoekihi);
		initDto.setContractLandRent(landRent);
		initDto.setContractBiko(biko);
		initDto.setContractAddDisableFlg(contractAddDisableFlg);
		initDto.setContractDelDisableFlg(contractDelDisableFlg);
		initDto.setHdnDispContractSelectedIndex(selectIndex);
		// 解放
		listTableData = null;
		contractNoList = null;
	}

	/**
	 * 社宅管理者情報設定(新規)
	 * 「※」項目はアドレスとして戻り値になる。
	 * @param shatakuKanriNo	社宅管理番号
	 * @param initDto			*DTO
	 */
	private void setShatakuManageInfoShinki(Skf3010Sc002CommonDto initDto) {

		// 既存Dto解放
		initDto.setDormitoryLeaderRoomNo(null);
		initDto.setDormitoryLeaderName(null);
		initDto.setDormitoryLeaderName(null);
		initDto.setDormitoryLeaderMailAddress(null);
		initDto.setDormitoryLeaderTelNumber(null);
		initDto.setDormitoryLeaderExtentionNo(null);
		initDto.setDormitoryLeaderBiko(null);
		initDto.setDormitoryLeaderUpdateDate(null);
		initDto.setKeyManagerRoomNo(null);
		initDto.setKeyManagerName(null);
		initDto.setKeyManagerMailAddress(null);
		initDto.setKeyManagerTelNumber(null);
		initDto.setKeyManagerExtentionNo(null);
		initDto.setKeyManagerBiko(null);
		initDto.setKeyManagerUpdateDate(null);
		initDto.setMatronRoomNo(null);
		initDto.setMatronName(null);
		initDto.setMatronMailAddress(null);
		initDto.setMatronTelNumber(null);
		initDto.setMatronExtentionNo(null);
		initDto.setMatronBiko(null);
		initDto.setMatronUpdateDate(null);

		// 寮長・自治会長設定
		initDto.setDormitoryLeaderRoomNo("");
		initDto.setDormitoryLeaderName("");
		initDto.setDormitoryLeaderMailAddress("");
		initDto.setDormitoryLeaderTelNumber("");
		initDto.setDormitoryLeaderExtentionNo("");
		initDto.setDormitoryLeaderBiko("");
		// 鍵管理者
		initDto.setKeyManagerRoomNo("");
		initDto.setKeyManagerName("");
		initDto.setKeyManagerMailAddress("");
		initDto.setKeyManagerTelNumber("");
		initDto.setKeyManagerExtentionNo("");
		initDto.setKeyManagerBiko("");
		// 寮母・管理会社
		initDto.setMatronRoomNo("");
		initDto.setMatronName("");
		initDto.setMatronMailAddress("");
		initDto.setMatronTelNumber("");
		initDto.setMatronExtentionNo("");
		initDto.setMatronBiko("");
	}

	/**
	 * 社宅管理者情報設定
	 * 「※」項目はアドレスとして戻り値になる。
	 * @param shatakuKanriNo	社宅管理番号
	 * @param initDto			*DTO
	 */
	private void setShatakuManageInfo(String shatakuKanriNo, Skf3010Sc002CommonDto initDto) {

		// 駐車場情報リストデータ(DBから取得したデータ保存用)
		List<Skf3010Sc002GetShatakuManageTableDataExp> getShatakuManageListTableData = 
									new ArrayList<Skf3010Sc002GetShatakuManageTableDataExp>();

		// 社宅管理者情報をDBより取得する
		getShatakuManageInfo(shatakuKanriNo, getShatakuManageListTableData);
		// 管理者情報ループ
		for(Skf3010Sc002GetShatakuManageTableDataExp tmpData : getShatakuManageListTableData){
			/** DTO設定値 */
			// 部屋番号
			String roomNo = "";
			// 管理者氏名
			String name = "";
			// メールアドレス
			String mailAddress = "";
			// 電話番号
			String telNo = "";
			// 内線番号
			String extensinoNo = "";
			// 備考
			String biko = "";

			// 部屋番号
			if (tmpData.getManageShatakuNo() != null) {
				roomNo = tmpData.getManageShatakuNo();
			}
			// 管理者氏名
			if (tmpData.getManageName() != null) {
				name = tmpData.getManageName();
			}
			// メールアドレス
			if (tmpData.getManageMailAddress() != null) {
				mailAddress = tmpData.getManageMailAddress();
			}
			// 電話番号
			if (tmpData.getManageTelNo() != null) {
				telNo = tmpData.getManageTelNo();
			}
			// 内線番号
			if (tmpData.getManageExtensionNo() != null) {
				extensinoNo = tmpData.getManageExtensionNo();
			}
			// 備考
			if (tmpData.getBiko() != null) {
				biko = tmpData.getBiko();
			}
			/** 管理者区分判定 */
			// 寮長・自治会長
			if (MANAGE_KBN_DOMITRY_LEADER.equals(tmpData.getManageKbn())) {
				// 部屋番号
				initDto.setDormitoryLeaderRoomNo(null);
				initDto.setDormitoryLeaderRoomNo(roomNo);
				// 管理者氏名
				initDto.setDormitoryLeaderName(null);
				initDto.setDormitoryLeaderName(name);
				// メールアドレス
				initDto.setDormitoryLeaderMailAddress(null);
				initDto.setDormitoryLeaderMailAddress(mailAddress);
				// 電話番号
				initDto.setDormitoryLeaderTelNumber(null);
				initDto.setDormitoryLeaderTelNumber(telNo);
				// 内線番号
				initDto.setDormitoryLeaderExtentionNo(null);
				initDto.setDormitoryLeaderExtentionNo(extensinoNo);
				// 備考
				initDto.setDormitoryLeaderBiko(null);
				initDto.setDormitoryLeaderBiko(biko);
				// 更新日時(排他用)
				initDto.setDormitoryLeaderUpdateDate(null);
				initDto.setDormitoryLeaderUpdateDate(tmpData.getUpdateDate());
			} else if (MANAGE_KBN_KEY_MANAGER.equals(tmpData.getManageKbn())) {
				// 部屋番号
				initDto.setKeyManagerRoomNo(null);
				initDto.setKeyManagerRoomNo(roomNo);
				// 管理者氏名
				initDto.setKeyManagerName(null);
				initDto.setKeyManagerName(name);
				// メールアドレス
				initDto.setKeyManagerMailAddress(null);
				initDto.setKeyManagerMailAddress(mailAddress);
				// 電話番号
				initDto.setKeyManagerTelNumber(null);
				initDto.setKeyManagerTelNumber(telNo);
				// 内線番号
				initDto.setKeyManagerExtentionNo(null);
				initDto.setKeyManagerExtentionNo(extensinoNo);
				// 備考
				initDto.setKeyManagerBiko(null);
				initDto.setKeyManagerBiko(biko);
				// 更新日時(排他用)
				initDto.setKeyManagerUpdateDate(null);
				initDto.setKeyManagerUpdateDate(tmpData.getUpdateDate());
			} else if (MANAGE_KBN_MATRON.equals(tmpData.getManageKbn())) {
				// 部屋番号
				initDto.setMatronRoomNo(null);
				initDto.setMatronRoomNo(roomNo);
				// 管理者氏名
				initDto.setMatronName(null);
				initDto.setMatronName(name);
				// メールアドレス
				initDto.setMatronMailAddress(null);
				initDto.setMatronMailAddress(mailAddress);
				// 電話番号
				initDto.setMatronTelNumber(null);
				initDto.setMatronTelNumber(telNo);
				// 内線番号
				initDto.setMatronExtentionNo(null);
				initDto.setMatronExtentionNo(extensinoNo);
				// 備考
				initDto.setMatronBiko(null);
				initDto.setMatronBiko(biko);
				// 更新日時(排他用)
				initDto.setMatronUpdateDate(null);
				initDto.setMatronUpdateDate(tmpData.getUpdateDate());
			}
		}
	}

	/**
	 * 駐車場情報タブをセットします。(新規)<br>
	 * 「※」項目はアドレスとして戻り値になる。
	 * 新規用
	 * @param initDto	*DTO
	 */
	private void setShatakuParkingInfoShinki(Skf3010Sc002CommonDto initDto) {

		// 駐車場構造プルダウンリスト
		List<Map<String, Object>> parkingStructureList = new ArrayList<Map<String, Object>>();
		parkingStructureList.clear();
		parkingStructureList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_PARKING_STRUCTURE_KBN, "", true));
		initDto.setParkingStructureList(null);
		initDto.setParkingStructureList(parkingStructureList);

		// 駐車場基本使用料
		initDto.setParkingRent(null);
		initDto.setParkingRent(CodeConstant.STRING_ZERO);
		// 駐車場台数(区画数)
		initDto.setParkingBlockCount(null);
		initDto.setParkingBlockCount(CodeConstant.STRING_ZERO);
		// 貸与可能総数
		initDto.setLendPossibleCount(null);
		initDto.setLendPossibleCount(CodeConstant.STRING_ZERO);

		// 駐車場補足1
		initDto.setParkingHosokuFileName1(null);
		initDto.setParkingHosokuFileName1("");
		// 駐車場補足2
		initDto.setParkingHosokuFileName2(null);
		initDto.setParkingHosokuFileName2("");
		// 駐車場補足3
		initDto.setParkingHosokuFileName3(null);
		initDto.setParkingHosokuFileName3("");
		// 駐車場備考
		initDto.setParkingBiko(null);
		initDto.setParkingBiko("");
		// 解放
		parkingStructureList = null;
	}

	/**
	 * 駐車場情報タブ設定。<br>
	 * 「※」項目はアドレスとして戻り値になる。
	 * 更新用
	 * 
	 * @param shatakuKanriNo	社宅管理番号
	 * @param initDto			*DTO
	 * @throws ParseException 
	 */
	private void setShatakuParkingInfo(String shatakuKanriNo, Skf3010Sc002CommonDto initDto) throws ParseException {

		// 駐車場構造プルダウンリスト
		List<Map<String, Object>> parkingStructureList = new ArrayList<Map<String, Object>>();
		/** DTO設定値 */
		// 駐車場構造区分
		String parkingStructure = "";
		// 駐車場基本使用料
		Long parkingRentalValue = 0L;
		// 駐車場台数(区画数)
		Integer parkingBlockCount = 0;
		// 貸与可能総数
		Integer lendPossibleCount = 0;
		// 駐車場補足(ファイル名)
		String parkingSupplementName1 = "";
		String parkingSupplementName2 = "";
		String parkingSupplementName3 = "";
		// 駐車場備考
		String parkingBiko = "";

		// 駐車場情報リストデータ(DBから取得したデータ保存用)
		List<Skf3010Sc002GetShatakuParkingInfoTableDataExp> getShatakuParkingInfoTableData = 
									new ArrayList<Skf3010Sc002GetShatakuParkingInfoTableDataExp>();
		// 駐車場情報をDBより取得する
		getShatakuParkingInfo(shatakuKanriNo, getShatakuParkingInfoTableData);
		// 駐車場情報
		for(Skf3010Sc002GetShatakuParkingInfoTableDataExp tmpData : getShatakuParkingInfoTableData){
			// 「駐車場構造」設定
			if (tmpData.getParkingStructureKbn() != null) {
				parkingStructure = tmpData.getParkingStructureKbn();
			}
			parkingStructureList.clear();
			parkingStructureList.addAll(ddlUtils.getGenericForDoropDownList(
					FunctionIdConstant.GENERIC_CODE_PARKING_STRUCTURE_KBN, parkingStructure, true));
			initDto.setParkingStructureList(null);
			initDto.setParkingStructureList(parkingStructureList);

			// 駐車場基本使用料
			if (tmpData.getParkingRental() != null) {
				parkingRentalValue = tmpData.getParkingRental();
			} else {
				// 駐車場基本使用料計算情報取得
				// 建築年月日、構造区分、地域区分、駐車場構造区分をパラメータ指定
				SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp retEntity =
						getChyuushajouryouShiyouGaku(initDto.getBuildDate(),
						initDto.getStructureKbnCd(), initDto.getAreaKbnCd(), parkingStructure);
				// 駐車場使用料設定
				if (retEntity.getChushajouShiyoryou() != null) {
					parkingRentalValue = retEntity.getChushajouShiyoryou().longValue();
				}
			}
			initDto.setParkingRent(null);
			initDto.setParkingRent(String.format("%,d", parkingRentalValue));

			// 駐車場台数(区画数)
			if (tmpData.getParkingBlockCount() != null) {
				parkingBlockCount = tmpData.getParkingBlockCount();
			}
			initDto.setParkingBlockCount(null);
			initDto.setParkingBlockCount(parkingBlockCount.toString());

			// 貸与可能総数
			if (tmpData.getParkingLendKanoCount() != null) {
				lendPossibleCount = tmpData.getParkingLendKanoCount();
			}
			initDto.setLendPossibleCount(null);
			initDto.setLendPossibleCount(lendPossibleCount.toString());

			// 駐車場補足1
			if (tmpData.getParkingSupplementName1() != null) {
				parkingSupplementName1 = tmpData.getParkingSupplementName1();
				initDto.setParkingHosokuLink1(null);
				initDto.setParkingHosokuLink1(PARKING_HOSOKU_LINK + "_" + shatakuKanriNo + "_1");
			}
			initDto.setParkingHosokuFileName1(null);
			initDto.setParkingHosokuFileName1(parkingSupplementName1);
			// 駐車場補足2
			if (tmpData.getParkingSupplementName2() != null) {
				parkingSupplementName2 = tmpData.getParkingSupplementName2();
				initDto.setParkingHosokuLink2(null);
				initDto.setParkingHosokuLink2(PARKING_HOSOKU_LINK + "_" + shatakuKanriNo + "_2");
			}
			initDto.setParkingHosokuFileName2(null);
			initDto.setParkingHosokuFileName2(parkingSupplementName2);
			// 駐車場補足3
			if (tmpData.getParkingSupplementName3() != null) {
				parkingSupplementName3 = tmpData.getParkingSupplementName3();
				initDto.setParkingHosokuLink3(null);
				initDto.setParkingHosokuLink3(PARKING_HOSOKU_LINK + "_" + shatakuKanriNo + "_3");
			}
			initDto.setParkingHosokuFileName3(null);
			initDto.setParkingHosokuFileName3(parkingSupplementName3);

			// 駐車場備考
			if (tmpData.getParkingBiko() != null) {
				parkingBiko = tmpData.getParkingBiko();
			}
			initDto.setParkingBiko(null);
			initDto.setParkingBiko(parkingBiko);

			// 更新日（排他処理用）
			initDto.setParkingUpdateDate(null);
			initDto.setParkingUpdateDate(tmpData.getUpdateDate());
			// 解放
			parkingStructureList = null;
			getShatakuParkingInfoTableData = null;
			// 先頭行のみ処理
			break;
		}
	}

	/**
	 * 基本情報タブをセットします。(新規)
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param shatakuKanriNo	社宅管理番号
	 * @param areaKbnCd			地域区分コード
	 * @param initDto			*DTO
	 * @throws ParseException
	 */
	private void setKihonInfoShinki(Skf3010Sc002CommonDto initDto) {

		// 利用区分リスト
		List<Map<String, Object>> useKbnList = new ArrayList<Map<String, Object>>();
		// 管理会社リスト
		List<Map<String, Object>> manageCompanyList = new ArrayList<Map<String, Object>>();
		// 管理機関リスト
		List<Map<String, Object>> manageAgencyList = new ArrayList<Map<String, Object>>();
		// 管理事業領域リスト
		List<Map<String, Object>> manageBusinessAreaList = new ArrayList<Map<String, Object>>();
		// 都道府県リスト
		List<Map<String, Object>> prefList = new ArrayList<Map<String, Object>>();
		// 社宅構造リスト
		List<Map<String, Object>> shatakuStructureList = new ArrayList<Map<String, Object>>();
		// エレベーターリスト
		List<Map<String, Object>> elevatorList = new ArrayList<Map<String, Object>>();
	
		// 利用区分
		useKbnList.clear();
		useKbnList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_RIYO_KBN, "", false));
		initDto.setUseKbnList(null);
		initDto.setUseKbnList(useKbnList);
		// 管理会社
		manageCompanyList.clear();
		manageCompanyList.addAll(ddlUtils.getDdlCompanyByCd("", true));
		initDto.setManageCompanyList(null);
		initDto.setManageCompanyList(manageCompanyList);
		// 管理機関
		manageAgencyList.clear();
		initDto.setManageAgencyList(null);
		initDto.setManageAgencyList(manageAgencyList);
		// 管理事業領域
		manageBusinessAreaList.clear();
		initDto.setManageBusinessAreaList(null);
		initDto.setManageBusinessAreaList(manageBusinessAreaList);
		// 郵便番号
		initDto.setZipCd(null);
		initDto.setZipCd("");
		// 都道府県
		prefList.clear();
		prefList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_PREFCD, "", true));
		initDto.setPrefList(null);
		initDto.setPrefList(prefList);
		// 所在地
		initDto.setShatakuAddress(null);
		initDto.setShatakuAddress("");
		// 構造
		shatakuStructureList.clear();
		shatakuStructureList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_STRUCTURE_KBN, "", true));
		initDto.setShatakuStructureList(null);
		initDto.setShatakuStructureList(shatakuStructureList);
		initDto.setStructureKbnCd(null);
		initDto.setStructureKbnCd("");
		// 構造補足
		initDto.setShatakuStructureDetail(null);
		initDto.setShatakuStructureDetail("");
		// エレベーター
		elevatorList.clear();
		elevatorList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_ELEVATOR_KBN, "", true));
		initDto.setElevatorList(null);
		initDto.setElevatorList(elevatorList);
		// 建築年月日
		initDto.setBuildDate(null);
		initDto.setBuildDate("");
		// 次回算定年月日
		initDto.setNextCalcDate(null);
		initDto.setNextCalcDate("");
		// 社宅補足1
		initDto.setShatakuHosokuFileName1(null);
		initDto.setShatakuHosokuFileName1("");
		// 社宅補足2
		initDto.setShatakuHosokuFileName2(null);
		initDto.setShatakuHosokuFileName2("");
		// 社宅補足3
		initDto.setShatakuHosokuFileName3(null);
		initDto.setShatakuHosokuFileName3("");
		// 備考
		initDto.setBiko(null);
		initDto.setBiko("");
		// 実年数と経年の設定
		// 実経年設定
		initDto.setJituAge(null);
		initDto.setJituAge("");
		// 経年設定
		initDto.setAging(null);
		initDto.setAging("");
		// 解放
		useKbnList = null;
		manageCompanyList = null;
		manageAgencyList = null;
		manageBusinessAreaList = null;
		prefList = null;
		shatakuStructureList = null;
		elevatorList = null;
	}

	/**
	 * 基本情報タブ設定
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param shatakuKanriNo	社宅管理番号
	 * @param areaKbnCd			地域区分コード
	 * @param initDto			*DTO
	 * @throws ParseException 
	 */
	private void setKihonInfo(String shatakuKanriNo,
			String areaKbnCd, Skf3010Sc002CommonDto initDto) throws ParseException {

		// 利用区分リスト
		List<Map<String, Object>> useKbnList = new ArrayList<Map<String, Object>>();
		// 管理会社リスト
		List<Map<String, Object>> manageCompanyList = new ArrayList<Map<String, Object>>();
		// 管理機関リスト
		List<Map<String, Object>> manageAgencyList = new ArrayList<Map<String, Object>>();
		// 管理事業領域リスト
		List<Map<String, Object>> manageBusinessAreaList = new ArrayList<Map<String, Object>>();
		// 都道府県リスト
		List<Map<String, Object>> prefList = new ArrayList<Map<String, Object>>();
		// 社宅構造リスト
		List<Map<String, Object>> shatakuStructureList = new ArrayList<Map<String, Object>>();
		// エレベーターリスト
		List<Map<String, Object>> elevatorList = new ArrayList<Map<String, Object>>();

		/** DTO設定値 */
		StringBuilder jituAge = new StringBuilder();
		StringBuilder aging = new StringBuilder();
		// 利用区分
		String useKbn = "";
		// 管理会社コード
		String manageCompanyCd = "";
		// 管理機関コード
		String manageAgencyCd = "";
		// 事業領域コード
		String manageBusinessAreaCd = "";
		// 郵便番号
		String zipCd = "";
		// 都道府県コード
		String prefCd = "";
		// 社宅所在地
		String shatakuAddress = "";
		// 建築年月日
		String buildDate = "";
		// 社宅構造区分
		String structureKbn = "";
		// 社宅構造(補足)
		String structureSupplement = "";
		// エレベーター区分
		String elevatorKbn = "";
		// 次回算定年月日
		String nextCalculateDate = "";
		// 社宅補足名1
		String shatakuSupplementName1 = "";
		// 社宅補足名2
		String shatakuSupplementName2 = "";
		// 社宅補足名3
		String shatakuSupplementName3 = "";
		// 備考
		String biko = "";

		// 基本情報リストデータ(DBから取得したデータ保存用)
		List<Skf3010Sc002GetKihonInfoTableDataExp> getKihonInfoListTableData = new ArrayList<Skf3010Sc002GetKihonInfoTableDataExp>();
		// 基本情報を取得する
		getKihonInfo(shatakuKanriNo, getKihonInfoListTableData);
		// 基本情報
		for(Skf3010Sc002GetKihonInfoTableDataExp tmpData : getKihonInfoListTableData){
			// 「利用区分」設定
			if (tmpData.getUseKbn() != null) {
				useKbn = tmpData.getUseKbn();
			}
			// 管理会社
			if (tmpData.getManageCompanyCd() != null) {
				manageCompanyCd = tmpData.getManageCompanyCd();
			}
			// 管理機関
			if (tmpData.getManageAgencyCd() != null) {
				manageAgencyCd = tmpData.getManageAgencyCd();
			}
			// 管理事業領域
			if (tmpData.getManageBusinessAreaCd() != null) {
				manageBusinessAreaCd = tmpData.getManageBusinessAreaCd();
			}
			// 都道府県
			if (tmpData.getPrefCd() != null) {
				prefCd = tmpData.getPrefCd();
			}
			// 構造
			if (tmpData.getStructureKbn() != null) {
				structureKbn = tmpData.getStructureKbn();
			}
			// エレベーター
			if (tmpData.getElevatorKbn() != null) {
				elevatorKbn = tmpData.getElevatorKbn();
			}
			// ドロップダウンリストの値を設定
			getDoropDownList(
					useKbn, useKbnList,
					manageCompanyCd, manageCompanyList,
					manageAgencyCd, manageAgencyList,
					manageBusinessAreaCd, manageBusinessAreaList,
					prefCd, prefList,
					structureKbn, shatakuStructureList,
					elevatorKbn, elevatorList);

			// 郵便番号
			if (tmpData.getZipCd() != null) {
				zipCd = tmpData.getZipCd();
			}
			initDto.setZipCd(null);
			initDto.setZipCd(zipCd);

			// 所在地
			if (tmpData.getAddress() != null) {
				shatakuAddress = tmpData.getAddress();
			}
			initDto.setShatakuAddress(null);
			initDto.setShatakuAddress(shatakuAddress);

			// 構造補足
			if (tmpData.getStructureSupplement() != null) {
				structureSupplement = tmpData.getStructureSupplement();
			}
			initDto.setShatakuStructureDetail(null);
			initDto.setShatakuStructureDetail(structureSupplement);

			// 建築年月日
			if (tmpData.getBuildDate() != null) {
				buildDate = tmpData.getBuildDate();
			}
			initDto.setBuildDate(null);
			initDto.setBuildDate(buildDate);

			// 次回算定年月日
			if (tmpData.getNextCalculateDate() != null) {
				nextCalculateDate = skfDateFormatUtils.dateFormatFromString(
						tmpData.getNextCalculateDate(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}
			initDto.setNextCalcDate(null);
			initDto.setNextCalcDate(nextCalculateDate);

			// 社宅補足1
			if (tmpData.getShatakuSupplementName1() != null) {
				shatakuSupplementName1 = tmpData.getShatakuSupplementName1();
				initDto.setShatakuHosokuLink1(null);
				initDto.setShatakuHosokuLink1(SHATAKU_HOSOKU_LINK + "_" + shatakuKanriNo + "_1");
			}
			initDto.setShatakuHosokuFileName1(null);
			initDto.setShatakuHosokuFileName1(shatakuSupplementName1);

			// 社宅補足2
			if (tmpData.getShatakuSupplementName2() != null) {
				shatakuSupplementName2 = tmpData.getShatakuSupplementName2();
				initDto.setShatakuHosokuLink2(null);
				initDto.setShatakuHosokuLink2(SHATAKU_HOSOKU_LINK + "_" + shatakuKanriNo + "_2");
			}
			initDto.setShatakuHosokuFileName2(null);
			initDto.setShatakuHosokuFileName2(shatakuSupplementName2);

			// 社宅補足3
			if (tmpData.getShatakuSupplementName3() != null) {
				shatakuSupplementName3 = tmpData.getShatakuSupplementName3();
				initDto.setShatakuHosokuLink3(null);
				initDto.setShatakuHosokuLink3(SHATAKU_HOSOKU_LINK + "_" + shatakuKanriNo + "_3");
			}
			initDto.setShatakuHosokuFileName3(null);
			initDto.setShatakuHosokuFileName3(shatakuSupplementName3);

			// 備考
			if (tmpData.getBiko() != null) {
				biko = tmpData.getBiko();
			}
			initDto.setBiko(null);
			initDto.setBiko(biko);

			// 更新日（排他処理用）
			initDto.setKihonUpdateDate(null);
			initDto.setKihonUpdateDate(tmpData.getUpdateDate());

			jituAge.append(initDto.getJituAge());
			aging.append(initDto.getAging());
			// 実年数と経年の設定
			// 建築年月日判定
			if (buildDate.length() > 0) {
				setJituKeinen(jituAge, aging, buildDate, areaKbnCd, structureKbn);
				initDto.setJituAge(null);
				initDto.setAging(null);
				initDto.setJituAge(jituAge.toString());
				initDto.setAging(aging.toString());
			}
			// 先頭行のみ処理
			break;
		}
		// セッション情報のドロップダウン削除
		initDto.setUseKbnList(null);
		initDto.setManageCompanyList(null);
		initDto.setManageAgencyList(null);
		initDto.setManageBusinessAreaList(null);
		initDto.setPrefList(null);
		initDto.setShatakuStructureList(null);
		initDto.setElevatorList(null);

		// 戻り値設定
		initDto.setUseKbnList(useKbnList);
		initDto.setManageCompanyList(manageCompanyList);
		initDto.setManageAgencyList(manageAgencyList);
		initDto.setManageBusinessAreaList(manageBusinessAreaList);
		initDto.setPrefList(prefList);
		initDto.setShatakuStructureList(shatakuStructureList);
		initDto.setElevatorList(elevatorList);

		// 解放
		getKihonInfoListTableData = null;
		useKbnList = null;
		manageCompanyList = null;
		manageAgencyList = null;
		manageBusinessAreaList = null;
		prefList = null;
		shatakuStructureList = null;
		elevatorList = null;
	}

	/**
	 * ヘッダ部をセットします（新規）。<br>
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param shatakuKbnCd	社宅区分コード
	 * @param areaKbnCd		地域区分コード
	 * @param initDto		*DTO
	 */
	private void setSearchInfoShinki(String shatakuKbnCd, Skf3010Sc002CommonDto initDto) {
		// 社宅区分リスト
		List<Map<String, Object>> shatakuKbnList = new ArrayList<Map<String, Object>>();
		// 地域区分リスト
		List<Map<String, Object>> areaKbnList = new ArrayList<Map<String, Object>>();

		// 「地域区分」の設定
		areaKbnList.clear();
		areaKbnList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_AREA_KBN, "", true));
		initDto.setAreaKbnList(null);
		initDto.setAreaKbnCd(null);
		initDto.setAreaKbnList(areaKbnList);
		initDto.setAreaKbnCd("");

		// 「社宅区分」を設定
		// 社宅区分判定
		if (!CodeConstant.ITTOU.equals(shatakuKbnCd)) {
			shatakuKbnList.clear();
			shatakuKbnList.addAll(ddlUtils.getGenericForDoropDownList(
					FunctionIdConstant.GENERIC_CODE_SHATAKU_KBN, CodeConstant.HOYU, true));
			initDto.setShatakuKbnList(null);
			initDto.setShatakuKbnList(shatakuKbnList);
		}
		// 解放
		shatakuKbnList = null;
		areaKbnList = null;
	}

	/**
	 * ヘッダ部情報セット
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * ヘッダ部の社宅名、地域区分、社宅区分、空き部屋数、空き駐車場数を設定する
	 * 社宅区分が「一棟」の場合、社宅区分ラベルフラグをtrue、、一棟以外の場合はfalseを設定する
	 * 
	 * @param shatakuKanriNo	社宅管理番号
	 * @param shatakuName		社宅名
	 * @param shatakuKbnCd		社宅区分コード
	 * @param areaKbnCd			地域区分コード
	 * @param emptyRoomCount	空き部屋数
	 * @param emptyParkingCount	空き駐車場数
	 * @param initDto			*DTO
	 */
	private void setSearchInfo(String shatakuKanriNo, String shatakuName, String shatakuKbnCd,
			String areaKbnCd, String emptyRoomCount, String emptyParkingCount, Skf3010Sc002CommonDto initDto) {
		// 地域区分リスト
		List<Map<String, Object>> areaKbnList = new ArrayList<Map<String, Object>>();
		// 社宅区分リスト
		List<Map<String, Object>> shatakuKbnList = new ArrayList<Map<String, Object>>();

		// 「社宅名」の設定
		initDto.setShatakuName(null);
		initDto.setShatakuName(shatakuName);

		// 「地域区分」の設定
		areaKbnList.clear();
		areaKbnList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_AREA_KBN, areaKbnCd, true));
		initDto.setAreaKbnList(null);
		initDto.setAreaKbnList(areaKbnList);
		initDto.setAreaKbnCd(null);
		initDto.setAreaKbnCd(areaKbnCd);

		// 「社宅区分」を設定
		// 社宅区分判定
		if (!CodeConstant.ITTOU.equals(shatakuKbnCd)) {
			shatakuKbnList.clear();
			shatakuKbnList.addAll(ddlUtils.getGenericForDoropDownList(
					FunctionIdConstant.GENERIC_CODE_SHATAKU_KBN, shatakuKbnCd, true));
			initDto.setShatakuKbnList(null);
			initDto.setShatakuKbnList(shatakuKbnList);
		}

		// 社宅部屋総数を取得する
		Integer roomCnt = getRoomCount(shatakuKanriNo);
		// 社宅部屋総数取得結果判定
		if (roomCnt > 0) {
			// （登録時対応）空き社宅数判定
			if (emptyRoomCount == null || emptyRoomCount.length() == 0) {
				// （登録時対応）空き社宅数が存在しない場合は、0を設定する。
				emptyRoomCount = CodeConstant.STRING_ZERO;
			}
			// 「空き部屋数」を設定する
			initDto.setEmptyRoomCount(null);
			initDto.setEmptyRoomCount(emptyRoomCount + CodeConstant.SLASH + roomCnt.toString());
		}

		// 「空き駐車場数」を設定する
		// 駐車場総数を取得する
		Integer parkingCnt = getParkingCount(shatakuKanriNo);
		// （登録時対応）空き駐車場数判定
		if (emptyParkingCount == null || emptyParkingCount.length() == 0) {
			// （登録時対応）空き社宅数が存在しない場合は、0を設定する。
			emptyParkingCount = CodeConstant.STRING_ZERO;
		}
		initDto.setEmptyParkingCount(null);
		initDto.setEmptyParkingCount(emptyParkingCount + CodeConstant.SLASH + parkingCnt.toString());
		// 解放
		areaKbnList = null;
		shatakuKbnList = null;
	}

	/**
	 * 保有社宅情報取得設定。<br />
	 * 保有社宅登録画面のヘッダー部、各タブ情報、プルダウンの取得、設定を行う。
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param selectMode				選択モード
	 * @param contractSelectedIndex		契約情報選択プルダウン値
	 * @param initDto					*DTO
	 * @throws Exception
	 */
	public void setHoyuShatakuInfo(String selectMode, 
			String contractSelectedIndex, Skf3010Sc002CommonDto initDto) throws Exception {

		// リストデータ取得用
		// 駐車場区画情報リスト
		List<Map<String, Object>> parkingBlockListTableData = new ArrayList<Map<String, Object>>();
		// 備品情報リスト
		List<Map<String, Object>> bihinInfoListTableData = new ArrayList<Map<String, Object>>();
		// 非表示備品情報リスト
		List<Map<String, Object>> hdnBihinInfoListTableData = new ArrayList<Map<String, Object>>();
		// 社宅部屋情報リスト(排他処理用)
		List<Map<String, Object>> shatakuRoomExlusiveCntrllList = new ArrayList<Map<String, Object>>();
		// 社宅部屋備品情報リスト(排他処理用)
		List<Map<String, Object>> roomeBihinExlusiveCntrllList = new ArrayList<Map<String, Object>>();
		// 貸与区分選択値リスト文字列(追加ボタン押下用)
		String setLendKbnSelectListString = "";
		// デフォルト貸与状況(追加ボタン押下用)
		String defaultParkingLendStatus = CodeConstant.LEND_JOKYO_NASHI;
		

		// 画面に残っている添付資料情報セッションをクリア
		// 社宅補足ファイル名1
		initDto.setShatakuHosokuFileName1(null);
		// 社宅補足ファイル名2
		initDto.setShatakuHosokuFileName2(null);
		// 社宅補足ファイル名3
		initDto.setShatakuHosokuFileName3(null);
		// 社宅補足リンク1
		initDto.setShatakuHosokuLink1(null);
		// 社宅補足リンク名2
		initDto.setShatakuHosokuLink2(null);
		// 社宅補足リンク名3
		initDto.setShatakuHosokuLink3(null);

		// 画面に残っている駐車場情報セッションをクリア
		// 駐車場補足ファイル名1
		initDto.setParkingHosokuFileName1(null);
		// 駐車場補足ファイル名2
		initDto.setParkingHosokuFileName2(null);
		// 駐車場補足ファイル名3
		initDto.setParkingHosokuFileName3(null);
		// 駐車場補足リンク1
		initDto.setParkingHosokuLink1(null);
		// 駐車場補足リンク2
		initDto.setParkingHosokuLink2(null);
		// 駐車場補足リンク3
		initDto.setParkingHosokuLink3(null);

		/** DTO設定用 */
		// 新規保有社宅フラグ(新規：true, 編集：false)
		Boolean newShatakuFlg = false;
		// 駐車場契約情報ボタン(非活性：true, 活性:false)
		Boolean parkingContractDisableFlg = true;

		/** セッション情報取得用 */
		// 社宅管理番号
		String shatakuKanriNo = "";
		// 社宅名
		String shatakuName = "";
		// 社宅区分コード
		String shatakuKbnCd = "";
		// 地域区分コード
		String areaKbnCd = "";
		// 空き部屋数
		String emptyRoomCount = CodeConstant.STRING_ZERO;
		// 空き駐車場数
		String emptyParkingCount = CodeConstant.STRING_ZERO;
		// 削除済契約番号
		String deletedConstractNo = CodeConstant.STRING_ZERO;

		/** セッション情報取得 */
		/** セッション情報取得 */
		// 遷移元判定
		if (!NfwStringUtils.isEmpty(initDto.getHdnShatakuKanriNo())) {
			// 駐車場契約からの画面遷移 or 自画面
			shatakuKanriNo = (initDto.getHdnShatakuKanriNo() != null) ? initDto.getHdnShatakuKanriNo() : "";
			shatakuKanriNo = (initDto.getHdnShatakuKanriNo() != null) ? initDto.getHdnShatakuKanriNo() : "";
			shatakuName = (initDto.getHdnShatakuName() != null) ? initDto.getHdnShatakuName() : "";
			shatakuKbnCd = (initDto.getHdnShatakuKbn() != null) ? initDto.getHdnShatakuKbn() : "";
			areaKbnCd = (initDto.getHdnAreaKbn() != null) ? initDto.getHdnAreaKbn() : "";
			emptyRoomCount = (initDto.getHdnEmptyRoomCount() != null) ?
					initDto.getHdnEmptyRoomCount() : CodeConstant.STRING_ZERO;
			emptyParkingCount = (initDto.getHdnEmptyParkingCount() != null) ?
					initDto.getHdnEmptyParkingCount() : CodeConstant.STRING_ZERO;
		} else if (!NfwStringUtils.isEmpty(initDto.getHdnRowShatakuKanriNo())) {
			// 社宅一覧からの遷移
			shatakuKanriNo =(initDto.getHdnRowShatakuKanriNo() != null) ? initDto.getHdnRowShatakuKanriNo(): "";
			shatakuName = (initDto.getHdnRowShatakuName() != null) ? initDto.getHdnRowShatakuName() : "";
			shatakuKbnCd = (initDto.getHdnRowShatakuKbn() != null) ? initDto.getHdnRowShatakuKbn() : "";
			areaKbnCd = (initDto.getHdnRowAreaKbn() != null) ? initDto.getHdnRowAreaKbn() : "";
			emptyRoomCount = (initDto.getHdnRowEmptyRoomCount() != null) ?
					initDto.getHdnRowEmptyRoomCount() : CodeConstant.STRING_ZERO;
			emptyParkingCount = (initDto.getHdnRowEmptyParkingCount() != null) ?
						initDto.getHdnRowEmptyParkingCount() : CodeConstant.STRING_ZERO;
		} else {
			// 新規
			shatakuKbnCd = (initDto.getHdnRowShatakuKbn() != null) ? initDto.getHdnRowShatakuKbn() : "";
		}
		// 削除済契約番号
		if (initDto.getHdnDeleteContractSelectedValue() != null
				&& initDto.getHdnDeleteContractSelectedValue().length() > 0) {
			deletedConstractNo = initDto.getHdnDeleteContractSelectedValue();
		}

		// セッション情報存在判定
		if (!NfwStringUtils.isEmpty(shatakuKanriNo)) {
			// ヘッダ部の値をセット
			setSearchInfo(shatakuKanriNo, shatakuName, shatakuKbnCd, 
					areaKbnCd, emptyRoomCount, emptyParkingCount, initDto);
			// 基本情報タブの値をセット
			setKihonInfo(shatakuKanriNo, areaKbnCd, initDto);
			// 駐車場情報タブの値をセット
			setShatakuParkingInfo(shatakuKanriNo, initDto);
			// 駐車場区画情報の値を取得
			getShatakuParkingBlockInfo(shatakuKanriNo, initDto.getParkingRent(), parkingBlockListTableData);
			// 備品情報タブの値をセット
			setBihinInfo(shatakuKanriNo, bihinInfoListTableData, hdnBihinInfoListTableData);
			// 管理者情報タブの値をセット
			setShatakuManageInfo(shatakuKanriNo, initDto);
			// 社宅部屋情報取得(排他用)
			getShatakuRoomInfo(shatakuKanriNo, shatakuRoomExlusiveCntrllList);
			initDto.setRoomeBihinExlusiveCntrllList(null);
			initDto.setRoomeBihinExlusiveCntrllList(shatakuRoomExlusiveCntrllList);
			// 社宅部屋備品情報取得(排他用)
			getShatakuRoomBihinInfo(shatakuKanriNo, roomeBihinExlusiveCntrllList);
			initDto.setRoomeBihinExlusiveCntrllList(null);
			initDto.setRoomeBihinExlusiveCntrllList(roomeBihinExlusiveCntrllList);
			// 社宅区分判定
			if (CodeConstant.ITTOU.equals(shatakuKbnCd)) {
				// 契約情報タブの値設定
				setContractInfo(shatakuKanriNo, contractSelectedIndex, deletedConstractNo, selectMode, initDto);
			}
			// 駐車場契約情報ボタン(非活性：true, 活性:false)
			parkingContractDisableFlg = false;
		} else {
			// 新規の場合
			newShatakuFlg = true;
			// ヘッダ部の値をセット
			setSearchInfoShinki(shatakuKbnCd, initDto);
			// 基本情報タブの値をセット
			setKihonInfoShinki(initDto);
			// 駐車場情報タブの値をセット
			setShatakuParkingInfoShinki(initDto);
			// 備品情報タブの値をセット
			setBihinInfoOfShinki(
					bihinInfoListTableData, hdnBihinInfoListTableData);
			// 管理者情報タブの値をセット
			setShatakuManageInfoShinki(initDto);
			// 社宅区分判定
			if (CodeConstant.ITTOU.equals(shatakuKbnCd)) {
				// 契約情報タブの値設定
				setContractInfoShinki(selectMode, initDto);
			}
			// 駐車場契約情報ボタン(非活性：true, 活性:false)
			parkingContractDisableFlg = true;
		}

		// 駐車場情報区画情報追加ボタン用貸与区分
		setLendKbnSelectListString = createLendKbnSelectList();

		/** 戻り値セット */
		// ページタイトル
		// 社宅区分判定
		if (CodeConstant.ITTOU.equals(shatakuKbnCd)) {
			// 一棟借上社宅登録
			initDto.setPageTitleKey(null);
			initDto.setPageTitleKey(MessageIdConstant.SKF3010_SC002_ITTO_TITLE);
			// 社宅区分ラベルフラグを「true」
			initDto.setIttoFlg(null);
			initDto.setIttoFlg(true);
			// 社宅区分用・汎用コード取得
			Map<String, String> genericCodeMapShataku = new HashMap<String, String>();
			genericCodeMapShataku = skfGenericCodeUtils.getGenericCode(
								FunctionIdConstant.GENERIC_CODE_SHATAKU_KBN);
			// 社宅区分ラベルを設定
			initDto.setShatakuKbn(null);
			initDto.setShatakuKbn(genericCodeMapShataku.get(shatakuKbnCd));
		} else {
			// 保有社宅登録
			initDto.setPageTitleKey(null);
			initDto.setPageTitleKey(MessageIdConstant.SKF3010_SC002_TITLE);
			// 社宅区分ラベルフラグを「false」
			initDto.setIttoFlg(null);
			initDto.setIttoFlg(false);
		}
		// 新規保有社宅フラグ
		initDto.setNewShatakuFlg(null);
		initDto.setNewShatakuFlg(newShatakuFlg);
		// 駐車場区画情報設定
		initDto.setParkingInfoListTableData(null);
		initDto.setParkingInfoListTableData(parkingBlockListTableData);
		initDto.setHdnStartingParkingInfoListTableData(null);
		initDto.setHdnStartingParkingInfoListTableData(parkingBlockListTableData);
		// 備品情報リスト
		initDto.setBihinInfoListTableData(null);
		initDto.setHdnBihinInfoListTableData(null);
		initDto.setBihinInfoListTableData(bihinInfoListTableData);
		initDto.setHdnBihinInfoListTableData(hdnBihinInfoListTableData);
		// 貸与区分選択値リスト文字列(追加ボタン押下用)
		initDto.setLendKbnSelectListString(null);
		initDto.setLendKbnSelectListString(setLendKbnSelectListString);
		// デフォルト貸与状況(追加ボタン押下用)
		initDto.setDefaultParkingLendStatus(null);
		initDto.setDefaultParkingLendStatus(defaultParkingLendStatus);
		// 駐車場契約情報ボタン
		initDto.setParkingContractDisableFlg(null);
		initDto.setParkingContractDisableFlg(parkingContractDisableFlg);
		// 削除済み契約番号
		initDto.setHdnDeleteContractSelectedValue(null);
		initDto.setHdnDeleteContractSelectedValue(deletedConstractNo);

		/** 駐車場契約情報への連携用 */
		initDto.setHdnShatakuKanriNo(null);
		initDto.setHdnShatakuName(null);
		initDto.setHdnAreaKbn(null);
		initDto.setHdnShatakuKbn(null);
		initDto.setHdnEmptyRoomCount(null);
		initDto.setHdnEmptyParkingCount(null);
		initDto.setHdnShatakuKanriNo(shatakuKanriNo);
		initDto.setHdnShatakuName(shatakuName);
		initDto.setHdnAreaKbn(areaKbnCd);
		initDto.setHdnShatakuKbn(shatakuKbnCd);
		initDto.setHdnEmptyRoomCount(emptyRoomCount);
		initDto.setHdnEmptyParkingCount(emptyParkingCount);
	}
}