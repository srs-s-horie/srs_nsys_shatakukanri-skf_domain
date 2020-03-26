/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc002;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetBihinInfoTableDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetHoyuShatakuInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetKihonInfoTableDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetMaxShatakuKanriNoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetParkingBlockExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetParkingBlockHistroyCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetRoomBihinExlusiveCntrlExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetShatakuContExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetShatakuContractInfoTableDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetShatakuManageTableDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetShatakuParkingBlockTableDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetShatakuParkingInfoTableDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetShatakuRoomExlusiveCntrlExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetShatkuInfoTableDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002MShatakuParkingTableDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002MShatakuTableDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBaseBusinessLogicUtils.SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBaseBusinessLogicUtils.SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuBihin;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuContract;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuManege;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuParkingBlock;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetBihinInfoTableDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetContractInfoTableDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetKihonInfoTableDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetManageInfoTableDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetMaxParkingKanriNoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetMaxShatakuKanriNoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetParkingBlockTableDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetParkingCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetParkingInfoTableDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetRoomBihinExlusiveCntrTableDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetRoomCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetRoomExlusiveCntrTableDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetShatakuCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetShatakuInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetShatakuParkingHistroyCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetTeijiDataCountForParkingBlockExpRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc002common.Skf3010Sc002CommonDto;

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
	private Skf3010Sc002GetShatakuParkingHistroyCountExpRepository skf3010Sc002GetShatakuParkingHistroyCountExpRepository;
	@Autowired
	private Skf3010Sc002GetShatakuCountExpRepository skf3010Sc002GetShatakuCountExpRepository;
	@Autowired
	private Skf3010Sc002GetMaxParkingKanriNoExpRepository skf3010Sc002GetMaxParkingKanriNoExpRepository;
	@Autowired
	private Skf3010Sc002GetMaxShatakuKanriNoExpRepository skf3010Sc002GetMaxShatakuKanriNoExpRepository;
	@Autowired
	private Skf3010Sc002GetShatakuInfoExpRepository skf3010Sc002GetShatakuInfoExpRepository;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;

	// 日付上限
	private static final int DATE_MAX = 99940331;
	// 社宅補足リンクプレフィックス
	private static final String SHATAKU_HOSOKU_LINK = "attached_shataku";
	// 駐車場補足プレフィックス
	private static final String PARKING_HOSOKU_LINK = "attached_parking";
	// 備品備付状況デフォルト値(なし)
	private static final String DEFAULT_BIHIN_STATUS = "1";
	// 日付フォーマット
	public static final String DATE_FORMAT = "yyyyMMdd HH:mm:ss.SSS";

	/**
	 * 次社宅管理番号取得
	 * パラメータのシステム処理年月をプリフィックスとする
	 * 社宅管理番号最大値を取得し、1加算した値を返却する
	 * 
	 * @param shoriNengetsu	処理年月
	 * @return	次社宅管理番号
	 */
	public Long getNextShatakuKanriNo(String shoriNengetsu) {

		Long nextShatakuKanriNo = null;
		Long maxShatakuKanriNo = null;
		// 社宅管理番号指定時のみDBから最大値を取得する
		Skf3010Sc002GetMaxShatakuKanriNoExpParameter param = new Skf3010Sc002GetMaxShatakuKanriNoExpParameter();
		param.setSyoriNengetu(shoriNengetsu);
		maxShatakuKanriNo = skf3010Sc002GetMaxShatakuKanriNoExpRepository.getMaxShatakuKanriNo(param);
		if (maxShatakuKanriNo != null) {
			nextShatakuKanriNo = maxShatakuKanriNo + 1L;
		} else {
			nextShatakuKanriNo = Long.parseLong(shoriNengetsu + "0001");
		}
		param = null;
		return nextShatakuKanriNo;
	}

	/**
	 * 次駐車場管理番号取得
	 * 駐車場区画情報の駐車場管理番号の最大値を取得し、1加算した値を返却する
	 * 
	 * @param shatakuKanriNo	社宅管理番号
	 * @return	次駐車場管理番号
	 */
	public Long getNextParkingKanriNo(String shatakuKanriNo) {

		Long nextParkingKanriNo = 1L;
		Long maxParkingKanriNo = null;
		// 社宅管理番号判定
		if (shatakuKanriNo != null && shatakuKanriNo.length() > 0) {
			// 社宅管理番号指定時のみDBから最大値を取得する
			Skf3010Sc002GetHoyuShatakuInfoExpParameter param = new Skf3010Sc002GetHoyuShatakuInfoExpParameter();
			param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
	
			maxParkingKanriNo = skf3010Sc002GetMaxParkingKanriNoExpRepository.getMaxParkingKanriNo(param);
			if (maxParkingKanriNo != null) {
				nextParkingKanriNo = maxParkingKanriNo + 1L;
			}
			param = null;
		}
		return nextParkingKanriNo;
	}

	/**
	 * 社宅件数取得。<br>
	 * パラメータの社宅名、都道府県コード、社宅住所に一致する社宅件数をDBより取得する。
	 * 
	 * @param shatakuName		社宅名
	 * @param prefCd			都道府県コード
	 * @param shatakuAddress	社宅住所
	 * @return	社宅件数
	 */
	public int getShatakuCountFromNameAndAddress(String shatakuName, String prefCd, String shatakuAddress) {
		int shatakuCnt = 0;
		Skf3010Sc002GetShatakuContExpParameter param = new Skf3010Sc002GetShatakuContExpParameter();
		param.setShatakuName(shatakuName);
		param.setPrefCd(prefCd);
		param.setShatakuAddress(shatakuAddress);
		shatakuCnt = skf3010Sc002GetShatakuCountExpRepository.getShatakuCountFromNameAndAddress(param);
		param = null;
		return shatakuCnt;
	}

	/**
	 * 駐車場区画貸与履歴数取得。<br>
	 * パラメータの社宅管理番号、駐車場管理番号の貸与履歴数をDBより取得する。
	 * 
	 * @param shatakuKanriNo 社宅管理番号
	 * @param parkingKanriNo 駐車場管理番号
	 * @return	駐車場区画貸与履歴数
	 */
	public int getShatakuParkingHistroyCount(String shatakuKanriNo, String parkingKanriNo) {
		int parkingBlockLendHistoryCount = 0;
		if (shatakuKanriNo.length() > 0 && parkingKanriNo.length() > 0) {
			Skf3010Sc002GetParkingBlockHistroyCountExpParameter param = new Skf3010Sc002GetParkingBlockHistroyCountExpParameter();
			param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
			param.setParkingKanriNo(Long.parseLong(parkingKanriNo));
			parkingBlockLendHistoryCount = skf3010Sc002GetShatakuParkingHistroyCountExpRepository.getShatakuParkingHistroyCount(param);
			param = null;
		}
		return parkingBlockLendHistoryCount;
	}

	/**
	 * 駐車場区画提示数取得。<br>
	 * パラメータの社宅管理番号、駐車場管理番号の提示数をDBより取得する。
	 * 
	 * @param shatakuKanriNo 社宅管理番号
	 * @param parkingKanriNo 駐車場管理番号
	 * @return	駐車場区画提示数
	 */
	public int getTeijiDataCountForParkingBlock(String shatakuKanriNo, String parkingKanriNo) {
		int parkingBlockTeijiHistoryCount = 0;
		if (shatakuKanriNo.length() > 0 && parkingKanriNo.length() > 0) {
			Skf3010Sc002GetParkingBlockHistroyCountExpParameter param = new Skf3010Sc002GetParkingBlockHistroyCountExpParameter();
			param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
			param.setParkingKanriNo(Long.parseLong(parkingKanriNo));
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
	 * @return	社宅部屋総数
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
	 * @return	駐車場総数
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
	private int getKihonInfo(String shatakuKanriNo,
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
				return resultCount;
			}
		}
		getKihonInfoListTableData.clear();
		getKihonInfoListTableData.addAll(resultListTableData);
		// 解放
		resultListTableData = null;
		return resultCount;
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
	private int getShatakuParkingInfo(String shatakuKanriNo,
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
				return resultCount;
			}
		}
		getShatakuParkingInfoTableData.clear();
		getShatakuParkingInfoTableData.addAll(resultListTableData);
		// 解放
		resultListTableData = null;

		return resultCount;
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
	 * @return	契約情報リスト数
	 */
	private int getShatakuContractInfo(String shatakuKanriNo, List<Map<String, Object>> listTableData,
										List<Map<String, Object>> contractNoList, String selectedValue) {

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
				// 取得データレコード数が0件場合、何もせず処理終了
				return resultCount;
			}
		}
		listTableData.clear();
		listTableData.addAll(
				setContractInfoListData(resultListTableData, resultContractNoList, selectedValue));
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
	 * @param 契約情報リスト
	 */
	private List<Map<String, Object>> setContractInfoListData(
			List<Skf3010Sc002GetShatakuContractInfoTableDataExp> getContractListTableData,
							List<Map<String, Object>> contractNoList, String selectedValue) {

		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
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
			// 更新日時
			String updateDate = "";

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
			contractMap.put("label", contractNo + Skf3010Sc002CommonDto.CONTRACT_NO_SEPARATOR + skfDateFormatUtils.dateFormatFromString(contractStartDate, "yyyy/MM/dd"));
			tmpMap.put("ownerName", ownerName);
			tmpMap.put("ownerNo", ownerNo);
			tmpMap.put("assetRegisterNo", assetRegisterNo);
			tmpMap.put("contractStartDate", contractStartDate);
			tmpMap.put("contractEndDate", contractEndDate);
			tmpMap.put("rent", rent);
			tmpMap.put("landRent", landRent);
			tmpMap.put("contractKyoekihi", contractKyoekihi);
			tmpMap.put("biko", biko);
			if(tmpData.getUpdateDate() != null){
				updateDate = dateFormat.format(tmpData.getUpdateDate());
			}
			tmpMap.put("updateDate", updateDate);
			contractInfoList.add(tmpMap);
			tmpMap = null;
			contractNoList.add(contractMap);
			contractMap = null;
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
		// 処理年月
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
		if (resultCount < 1) {
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
			tmpMap.put("parkingBlock", "<input id='parkingBlockNo" + i + "' name='parkingBlockNo" + i 
					+ "' placeholder='例　01' type='text' value='" + blockNo + "' style='width:140px;' maxlength='30'/>");
			// 「貸与区分」プルダウンの設定
			String lendStatusListCode = 
					createStatusSelect(parkingLendKbn,lendStatusList);
			// 貸与区分ステータス判定
			if ("1".equals(parkingLendKbn)) {
				// 貸与可能(アクティブ)
				tmpMap.put("parkingLendKbn", "<select id='parkingLendKbn" + i 
						+ "' name='parkingLendKbn" + i + "' style='width:90px;'>"
												+ lendStatusListCode + "</select>");
			} else {
				// 貸与不可(非活性)
				tmpMap.put("parkingLendKbn", "<select id='parkingLendKbn" + i 
						+ "' name='parkingLendKbn" + i + "' style='width:90px;' disabled>"
												+ lendStatusListCode + "</select>");
			}
			// 貸与状況
			tmpMap.put("parkingLendStatus", HtmlUtils.htmlEscape(parkingLendStatus));
			// 使用者
			tmpMap.put("shainName", shainName);
			// 駐車場調整金額
			tmpMap.put("parkingRentalAdjust", "<input id='parkingRentalAdjust" + i 
					+ "' name='parkingRentalAdjust" + i + "' type='text' value='"
								+ parkingRentalAdjust.toString() + "' placeholder='例　半角数字'"
								+ " style='ime-mode: disabled; width:75px;text-align: right;' maxlength='6'/> 円");
			// 駐車場月額使用料
			parkingBlockRentMany += parkingRentalAdjust;
			tmpMap.put("parkingMonthRental", "<label id='parkingMonthRental" + i
					+ "' name='parkingMonthRental" + i + "' >"
					+ HtmlUtils.htmlEscape(String.format("%,d", parkingBlockRentMany) + " 円")
					+ "</label>");
			// 駐車場備考
			tmpMap.put("parkingBiko", "<input id='parkingBlockBiko" + i 
					+ "' name='parkingBlockBiko" + i + "' type='text' value='"
					+ biko + "' style='width:215px;' maxlength='100'/>");
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
	public String createStatusSelect(
			String selectedValue,List<Map<String, Object>> slectValueList){

		String returnListCode = "";

		for(Map<String, Object> obj : slectValueList){
			String value = obj.get("value").toString();
			String label = obj.get("label").toString();
			if (Objects.equals(value, selectedValue)) {
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
		if (resultCount < 1) {
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
		if (resultCount < 1) {
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
			String bihinStatus = DEFAULT_BIHIN_STATUS;

			Skf3010Sc002GetBihinInfoTableDataExp tmpData = originList.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();

			if(!Objects.equals(tmpData.getDispFlg(), "0")){
				// 備品コード
				if (tmpData.getBihinCd() != null) {
					bihinCode = HtmlUtils.htmlEscape(tmpData.getBihinCd());
				}
				tmpMap.put("bihinCd", bihinCode);
				// 備品名
				if (tmpData.getBihinName() != null) {
					bihinName = HtmlUtils.htmlEscape(tmpData.getBihinName());
				}
				tmpMap.put("bihinName", bihinName);
				// 「備付状況」選択値
				if (tmpData.getBihinStatusKbn() != null && tmpData.getBihinStatusKbn().length() > 0) {
					bihinStatus = tmpData.getBihinStatusKbn();
				}
				// 備付状況
				String statusListCode = createStatusSelect(bihinStatus, statusList);
				tmpMap.put("bihinStatusKbn","<select id='bihinStatus" + i 
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
			String bihinStatus = DEFAULT_BIHIN_STATUS;

			Skf3010Sc002GetBihinInfoTableDataExp tmpData = originList.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			// 表示/非表示判定
			if(Objects.equals(tmpData.getDispFlg(), "0")){
				//DispFlgが0（非表示）
				// 備品コード
				if (tmpData.getBihinCd() != null) {
					bihinCode = tmpData.getBihinCd();
				}
				tmpMap.put("bihinCd", bihinCode);
				// 備品名
				if (tmpData.getBihinName() != null) {
					bihinName = tmpData.getBihinName();
				}
				tmpMap.put("bihinName", bihinName);
				// 「備付状況」選択値
				tmpMap.put("bihinStatusKbn",bihinStatus);
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
		skf3010Sc002GetRoomExlusiveCntrTableDataExpRepository.getRoomExlusiveCntr(param);

		// 取得レコード数を設定
		resultCnt = shatakuRoomList.size();

		// 取得データレコード数判定
		if (resultCnt < 1) {
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
		if (resultCnt < 1) {
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
				LogUtils.debugByMsg("日付形式不正");
				return false;
			}
			try {
				// 日付上限チェック
				if (Integer.parseInt(editBuildDate.trim()) > DATE_MAX) {
					LogUtils.debugByMsg("日付上限超過");
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
	private void setContractInfoShinki(Skf3010Sc002CommonDto initDto) {

		// 契約情報リスト
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
		// 契約番号リスト(ドロップダウン)
		List<Map<String, Object>> contractNoList = new ArrayList<Map<String, Object>>();

		// 契約情報追加ボタン(非活性：true, 活性:false)
		Boolean contractAddDisableFlg = false;
		// 契約情報削除ボタン(非活性：true, 活性:false)
		Boolean contractDelDisableFlg = true;

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
	 * @param initDto					*DTO
	 * @throws ParseException 
	 */
	private void setContractInfo(String shatakuKanriNo, String contractSelectedIndex,
									Skf3010Sc002CommonDto initDto) throws ParseException {

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
		getShatakuContractInfo(shatakuKanriNo, listTableData, contractNoList, selectIndex);
		// 追加ボタン活性
		contractAddDisableFlg = false;
		// 削除ボタン活性/非活性判定
		if (contractNoList.size() > 0) {
			// 削除ボタン活性
			contractDelDisableFlg = false;
		}
		// 契約情報リスト選択契約番号取得
		for (Map<String, Object> contractNoMap : contractNoList) {
			if (contractNoMap.containsKey("selected")) {
				selectIndex = contractNoMap.get("value").toString();
				break;
			}
		}
		// 選択契約情報
		Map<String, Object> contractMap = new HashMap<String, Object>();
		// 選択契約情報取得
		if (contractNoList.size() > 0) {
			for (Map<String, Object> contractDataMap : listTableData) {
				if (Objects.equals(contractDataMap.get("contractNo"), selectIndex)) {
					contractMap = contractDataMap;
					break;
				}
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
		// 更新日時
		if (contractMap.get("updateDate") != null && contractMap.get("updateDate").toString().length() > 0) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
			initDto.setContractUpdateDate(dateFormat.parse(contractMap.get("updateDate").toString()));
		}
		// 解放
		listTableData = null;
		contractNoList = null;
		contractMap = null;
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
			if (Skf3010Sc002CommonDto.MANAGE_KBN_DOMITRY_LEADER.equals(tmpData.getManageKbn())) {
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
			} else if (Skf3010Sc002CommonDto.MANAGE_KBN_KEY_MANAGER.equals(tmpData.getManageKbn())) {
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
			} else if (Skf3010Sc002CommonDto.MANAGE_KBN_MATRON.equals(tmpData.getManageKbn())) {
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
	private Boolean setShatakuParkingInfo(String shatakuKanriNo, Skf3010Sc002CommonDto initDto) throws ParseException {

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
		// 駐車場補足名1
		String parkingSupplementName1 = "";
		// 駐車場補足サイズ1
		String parkingSupplementSize1 = "";
		// 駐車場補足ファイル1
		byte[] parkingSupplementFile1 = null;
		// 駐車場補足名2
		String parkingSupplementName2 = "";
		// 駐車場補足サイズ2
		String parkingSupplementSize2 = "";
		// 駐車場補足ファイル2
		byte[] parkingSupplementFile2 = null;
		// 駐車場補足名3
		String parkingSupplementName3 = "";
		// 駐車場補足サイズ3
		String parkingSupplementSize3 = "";
		// 駐車場補足ファイル3
		byte[] parkingSupplementFile3 = null;
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
					FunctionIdConstant.GENERIC_CODE_PARKING_STRUCTURE_KBN, parkingStructure, false));
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
			parkingSupplementSize1 = (tmpData.getParkingSupplementSize1() != null) ?
												tmpData.getParkingSupplementSize1() : "";
			parkingSupplementFile1 = tmpData.getParkingSupplementFile1();
			// 駐車場補足1表示判定
			if (tmpData.getParkingSupplementName1() != null
								&& parkingSupplementSize1.length() > 0
								&& parkingSupplementFile1 != null) {
				parkingSupplementName1 = tmpData.getParkingSupplementName1();
				initDto.setParkingHosokuLink1(null);
				initDto.setParkingHosokuLink1(PARKING_HOSOKU_LINK + "_" + shatakuKanriNo + "_1");
			}
			initDto.setParkingHosokuFileName1(null);
			initDto.setParkingHosokuFileName1(parkingSupplementName1);
			initDto.setParkingHosokuSize1(null);
			initDto.setParkingHosokuSize1(parkingSupplementSize1);
			initDto.setParkingHosokuFile1(null);
			initDto.setParkingHosokuFile1(parkingSupplementFile1);

			// 駐車場補足2
			parkingSupplementSize2 = (tmpData.getParkingSupplementSize2() != null) ?
												tmpData.getParkingSupplementSize2() : "";
			parkingSupplementFile2 = tmpData.getParkingSupplementFile2();
			// 駐車場補足2表示判定
			if (tmpData.getParkingSupplementName2() != null
							&& parkingSupplementSize2.length() > 0
							&& parkingSupplementFile2 != null) {
				parkingSupplementName2 = tmpData.getParkingSupplementName2();
				initDto.setParkingHosokuLink2(null);
				initDto.setParkingHosokuLink2(PARKING_HOSOKU_LINK + "_" + shatakuKanriNo + "_2");
			}
			initDto.setParkingHosokuFileName2(null);
			initDto.setParkingHosokuFileName2(parkingSupplementName2);
			initDto.setParkingHosokuSize2(null);
			initDto.setParkingHosokuSize2(parkingSupplementSize2);
			initDto.setParkingHosokuFile2(null);
			initDto.setParkingHosokuFile2(parkingSupplementFile2);
			// 駐車場補足3
			parkingSupplementSize3 = (tmpData.getParkingSupplementSize3() != null) ?
													tmpData.getParkingSupplementSize3() : "";
			parkingSupplementFile3 = tmpData.getParkingSupplementFile3();
			// 駐車場補足3表示判定
			if (tmpData.getParkingSupplementName3() != null
								&& parkingSupplementSize3.length() > 0
								&& parkingSupplementFile3 != null) {
				parkingSupplementName3 = tmpData.getParkingSupplementName3();
				initDto.setParkingHosokuLink3(null);
				initDto.setParkingHosokuLink3(PARKING_HOSOKU_LINK + "_" + shatakuKanriNo + "_3");
			}
			initDto.setParkingHosokuFileName3(null);
			initDto.setParkingHosokuFileName3(parkingSupplementName3);
			initDto.setParkingHosokuSize3(null);
			initDto.setParkingHosokuSize3(parkingSupplementSize3);
			initDto.setParkingHosokuFile3(null);
			initDto.setParkingHosokuFile3(parkingSupplementFile3);

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
		// データ比較用
		initDto.setStartingParkingStructure(null);
		initDto.setStartingParkingStructure(parkingStructure);

		return true;
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
	private Boolean setKihonInfo(String shatakuKanriNo,
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
		// 社宅補足サイズ1
		String shatakuSupplementSize1 = "";
		// 社宅補足ファイル1
		byte[] shatakuSupplementFile1 = null;
		// 社宅補足名2
		String shatakuSupplementName2 = "";
		// 社宅補足サイズ2
		String shatakuSupplementSize2 = "";
		// 社宅補足ファイル2
		byte[] shatakuSupplementFile2 = null;
		// 社宅補足名3
		String shatakuSupplementName3 = "";
		// 社宅補足サイズ3
		String shatakuSupplementSize3 = "";
		// 社宅補足ファイル3
		byte[] shatakuSupplementFile3 = null;
		// 備考
		String biko = "";


		// 基本情報リストデータ(DBから取得したデータ保存用)
		List<Skf3010Sc002GetKihonInfoTableDataExp> getKihonInfoListTableData = new ArrayList<Skf3010Sc002GetKihonInfoTableDataExp>();
		// 基本情報を取得する
		int kihonInfoCnt = 0;
		kihonInfoCnt = getKihonInfo(shatakuKanriNo, getKihonInfoListTableData);
		// 取得結果判定
		if (kihonInfoCnt < 1) {
			LogUtils.debugByMsg("社宅基本情報なし");
			return false;
		}
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
			shatakuSupplementSize1 = (tmpData.getShatakuSupplementSize1() != null) ?
											tmpData.getShatakuSupplementSize1() : "";
			shatakuSupplementFile1 = tmpData.getShatakuSupplementFile1();
			// 社宅補足1表示判定
			if (tmpData.getShatakuSupplementName1() != null
							&& shatakuSupplementSize1.length() > 0
							&& shatakuSupplementFile1 != null) {
				shatakuSupplementName1 = tmpData.getShatakuSupplementName1();
				initDto.setShatakuHosokuLink1(null);
				initDto.setShatakuHosokuLink1(SHATAKU_HOSOKU_LINK + "_" + shatakuKanriNo + "_1");
			}
			initDto.setShatakuHosokuFileName1(null);
			initDto.setShatakuHosokuFileName1(shatakuSupplementName1);
			initDto.setShatakuHosokuSize1(shatakuSupplementSize1);
			initDto.setShatakuHosokuFile1(null);
			initDto.setShatakuHosokuFile1(shatakuSupplementFile1);

			// 社宅補足2
			shatakuSupplementSize2 = (tmpData.getShatakuSupplementSize2() != null) ?
										tmpData.getShatakuSupplementSize2() : "";
			shatakuSupplementFile2 = tmpData.getShatakuSupplementFile2();
			// 社宅補足2表示判定
			if (tmpData.getShatakuSupplementName2() != null
							&& shatakuSupplementSize2.length() > 0
							&& shatakuSupplementFile2 != null) {
				shatakuSupplementName2 = tmpData.getShatakuSupplementName2();
				initDto.setShatakuHosokuLink2(null);
				initDto.setShatakuHosokuLink2(SHATAKU_HOSOKU_LINK + "_" + shatakuKanriNo + "_2");
			}
			initDto.setShatakuHosokuFileName2(null);
			initDto.setShatakuHosokuFileName2(shatakuSupplementName2);
			initDto.setShatakuHosokuSize2(shatakuSupplementSize2);
			initDto.setShatakuHosokuFile2(null);
			initDto.setShatakuHosokuFile2(shatakuSupplementFile2);

			// 社宅補足3
			shatakuSupplementSize3 = (tmpData.getShatakuSupplementSize3() != null) ?
										tmpData.getShatakuSupplementSize3() : "";
			shatakuSupplementFile3 = tmpData.getShatakuSupplementFile3();
			// 社宅補足3表示判定
			if (tmpData.getShatakuSupplementName3() != null
							&& shatakuSupplementSize3.length() > 0
							&& shatakuSupplementFile3 != null) {
				shatakuSupplementName3 = tmpData.getShatakuSupplementName3();
				initDto.setShatakuHosokuLink3(null);
				initDto.setShatakuHosokuLink3(SHATAKU_HOSOKU_LINK + "_" + shatakuKanriNo + "_3");
			}
			initDto.setShatakuHosokuFileName3(null);
			initDto.setShatakuHosokuFileName3(shatakuSupplementName3);
			initDto.setShatakuHosokuSize3(shatakuSupplementSize3);
			initDto.setShatakuHosokuFile3(null);
			initDto.setShatakuHosokuFile3(shatakuSupplementFile3);

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
		// データ比較用
		initDto.setStartingBuildDate(null);
		initDto.setStartingShatakuStructure(null);
		initDto.setStartingBuildDate(buildDate);
		initDto.setStartingShatakuStructure(structureKbn);

		// 解放
		getKihonInfoListTableData = null;
		useKbnList = null;
		manageCompanyList = null;
		manageAgencyList = null;
		manageBusinessAreaList = null;
		prefList = null;
		shatakuStructureList = null;
		elevatorList = null;

		return true;
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
			deleteShatakuKbnDrpDwKariage(initDto);
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
			deleteShatakuKbnDrpDwKariage(initDto);
		}

		// 社宅部屋総数を取得する
		Integer roomCnt = getRoomCount(shatakuKanriNo);
		// 空き社宅数判定
		if (Objects.equals(emptyRoomCount, null) || emptyRoomCount.length() < 1) {
			// （登録時対応）空き社宅数が存在しない場合は、0を設定する。
			emptyRoomCount = CodeConstant.STRING_ZERO;
		}
		// 「空き部屋数」を設定する
		initDto.setEmptyRoomCount(null);
		initDto.setEmptyRoomCount(emptyRoomCount + CodeConstant.SLASH + roomCnt.toString());

		// 「空き駐車場数」を設定する
		// 駐車場総数を取得する
		Integer parkingCnt = getParkingCount(shatakuKanriNo);
		// （登録時対応）空き駐車場数判定
		if (Objects.equals(emptyParkingCount, null) || emptyParkingCount.length() < 1) {
			// （登録時対応）空き社宅数が存在しない場合は、0を設定する。
			emptyParkingCount = CodeConstant.STRING_ZERO;
		}
		initDto.setEmptyParkingCount(null);
		initDto.setEmptyParkingCount(emptyParkingCount + CodeConstant.SLASH + parkingCnt.toString());

		// 解放
		areaKbnList = null;
		shatakuKbnList = null;
		// データ比較用
		initDto.setStartingAreaKbn(null);
		initDto.setStartingAreaKbn(areaKbnCd);
	}

	/**
	 * 保有社宅情報取得設定。<br />
	 * 保有社宅登録画面のヘッダー部、各タブ情報、プルダウンの取得、設定を行う。
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param contractSelectedIndex		契約情報選択プルダウン値
	 * @param initDto					*DTO
	 * @throws Exception
	 */
	public void setHoyuShatakuInfo(String contractSelectedIndex, Skf3010Sc002CommonDto initDto) throws Exception {

		Boolean resultBool = false;

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
		// エラーコントロール初期化
		clearVaridateErr(initDto);
		// 画面に残っている添付資料情報セッションをクリア
		clearSessionTmpFile(initDto);

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

		/** セッション情報取得 */
		// 遷移元判定
		if (!NfwStringUtils.isEmpty(initDto.getHdnShatakuKanriNo()) &&
				(FunctionIdConstant.SKF3010_SC002.equals(initDto.getPrePageId())
						|| FunctionIdConstant.SKF3010_SC007.equals(initDto.getPrePageId()))) {
			// 駐車場契約からの画面遷移 or 自画面
			shatakuKanriNo = (initDto.getHdnShatakuKanriNo() != null) ? initDto.getHdnShatakuKanriNo() : "";

			// 社宅ヘッダ部情報
			List<Skf3010Sc002GetShatkuInfoTableDataExp> shatakuInfoList = new ArrayList<Skf3010Sc002GetShatkuInfoTableDataExp>();
			Skf3010Sc002GetHoyuShatakuInfoExpParameter param = new Skf3010Sc002GetHoyuShatakuInfoExpParameter();
			param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
			shatakuInfoList = skf3010Sc002GetShatakuInfoExpRepository.getShatakuInfo(param);
			if (shatakuInfoList.size() < 1) {
				// 件数が0未満（排他エラー）
				setListMapOnErr(initDto.getShatakuName(), initDto);
				return;
			}
			Skf3010Sc002GetShatkuInfoTableDataExp shatakuInfo = shatakuInfoList.get(0);
			shatakuName = shatakuInfo.getShatakuName();
			shatakuKbnCd = shatakuInfo.getShatakuKbn();
			areaKbnCd = shatakuInfo.getAreaKbn();
			emptyRoomCount = shatakuInfo.getEmptyRoomCount();
			emptyParkingCount = shatakuInfo.getEmptyParkingCount();
		} else if (!NfwStringUtils.isEmpty(initDto.getHdnRowShatakuKanriNo())
				&& FunctionIdConstant.SKF3010_SC001.equals(initDto.getPrePageId())) {
			// 社宅一覧からの遷移
			shatakuKanriNo =(initDto.getHdnRowShatakuKanriNo() != null) ? initDto.getHdnRowShatakuKanriNo(): "";

			// 社宅ヘッダ部情報
			List<Skf3010Sc002GetShatkuInfoTableDataExp> shatakuInfoList = new ArrayList<Skf3010Sc002GetShatkuInfoTableDataExp>();
			Skf3010Sc002GetHoyuShatakuInfoExpParameter param = new Skf3010Sc002GetHoyuShatakuInfoExpParameter();
			param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
			shatakuInfoList = skf3010Sc002GetShatakuInfoExpRepository.getShatakuInfo(param);
			if (shatakuInfoList.size() < 1) {
				//件数が0未満（排他エラー）
				setListMapOnErr(initDto.getHdnRowShatakuName(), initDto);
				return;
			}
			Skf3010Sc002GetShatkuInfoTableDataExp shatakuInfo = shatakuInfoList.get(0);
			shatakuName = shatakuInfo.getShatakuName();
			shatakuKbnCd = shatakuInfo.getShatakuKbn();
			areaKbnCd = shatakuInfo.getAreaKbn();
			emptyRoomCount = shatakuInfo.getEmptyRoomCount();
			emptyParkingCount = shatakuInfo.getEmptyParkingCount();
		} else {
			// 新規
			shatakuKbnCd = (initDto.getHdnRowShatakuKbn() != null) ? initDto.getHdnRowShatakuKbn() : "";
		}

		// セッション情報存在判定
		if (!NfwStringUtils.isEmpty(shatakuKanriNo)) {
			// ヘッダ部の値をセット
			setSearchInfo(shatakuKanriNo, shatakuName, shatakuKbnCd, 
					areaKbnCd, emptyRoomCount, emptyParkingCount, initDto);
			// 基本情報タブの値をセット
			resultBool = setKihonInfo(shatakuKanriNo, areaKbnCd, initDto);
			if (!resultBool) {
				// 排他エラー
				setListMapOnErr(shatakuName, initDto);
				return;
			}
			// 駐車場情報タブの値をセット
			resultBool = setShatakuParkingInfo(shatakuKanriNo, initDto);
			if (!resultBool) {
				// 排他エラー
				setListMapOnErr(shatakuName, initDto);
				return;
			}
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
				setContractInfo(shatakuKanriNo, contractSelectedIndex, initDto);
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
				setContractInfoShinki(initDto);
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

	/**
	 * エラー時ListMap作成処理
	 * 他ユーザーにより社宅情報がDBから削除されている場合などに
	 * 画面用のリストオブジェクトを作成(空っぽ表示用)
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param shatakuName	社宅名
	 * @param initDto		*DTO
	 */
	private void setListMapOnErr(String shatakuName, Skf3010Sc002CommonDto initDto) {

		// 画面用ListMap作成
		List<Map<String, Object>> areaKbnList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> shatakuKbnList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> useKbnList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> manageCompanyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> manageAgencyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> manageBusinessAreaList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> prefList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> shatakuStructureList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> elevatorList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> parkingStructureList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> contractNoList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> parkingInfoListTableData = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> hdnStartingParkingInfoListTableData = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> nowParkingInfoListTableData = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> bihinInfoListTableData = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> contractInfoListTableData = new ArrayList<Map<String, Object>>();

		// 画面用に設定
		initDto.setShatakuName(shatakuName);	// 社宅名だけは設定する
		initDto.setAreaKbnList(areaKbnList);
		initDto.setShatakuKbnList(shatakuKbnList);
		initDto.setUseKbnList(useKbnList);
		initDto.setManageCompanyList(manageCompanyList);
		initDto.setManageAgencyList(manageAgencyList);
		initDto.setManageBusinessAreaList(manageBusinessAreaList);
		initDto.setPrefList(prefList);
		initDto.setShatakuStructureList(shatakuStructureList);
		initDto.setElevatorList(elevatorList);
		initDto.setParkingStructureList(parkingStructureList);
		initDto.setContractNoList(contractNoList);
		initDto.setParkingInfoListTableData(parkingInfoListTableData);
		initDto.setHdnStartingParkingInfoListTableData(hdnStartingParkingInfoListTableData);
		initDto.setNowParkingInfoListTableData(nowParkingInfoListTableData);
		initDto.setBihinInfoListTableData(bihinInfoListTableData);
		initDto.setContractInfoListTableData(contractInfoListTableData);

		// 警告メッセージ
		ServiceHelper.addWarnResultMessage(initDto, MessageIdConstant.E_SKF_3059);
		LogUtils.debugByMsg("社宅情報取得失敗");
	}

	/**
	 * パラメータのJSON文字列配列をリスト形式に変換して返却する
	 * 
	 * @param jsonStr	JSON文字列配列
	 * @return			List<Map<String, Object>>
	 */
	public List<Map<String, Object>> jsonArrayToArrayList (String jsonStr) {

		// 返却用リスト
		List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		// JSON文字列判定
		if (Objects.equals(jsonStr, null) || jsonStr.length() <= 0) {
			LogUtils.debugByMsg("文字列未設定");
			// 文字列未設定のため処理しない
			return listData;
		}
		try {
			JSONArray arr = null;
			arr = new JSONArray(jsonStr);
			if (arr != null) {
				int arrCnt = arr.length();
				ObjectMapper mapper = new ObjectMapper();
				for(int i = 0; i < arrCnt; i++) {
					listData.add(mapper.readValue(arr.get(i).toString(), new TypeReference<Map<String, Object>>(){}));
				}
				arr = null;
				mapper = null;
			}
		} catch (JSONException e) {
			LogUtils.debugByMsg(e.getMessage());
		} catch (JsonParseException e) {
			LogUtils.debugByMsg(e.getMessage());
		} catch (JsonMappingException e) {
			LogUtils.debugByMsg(e.getMessage());
		} catch (IOException e) {
			LogUtils.debugByMsg(e.getMessage());
		}
		return listData;
	}

	/**
	 * セッションの添付ファイル情報クリア
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param initDto	*DTO
	 */
	private void clearSessionTmpFile(Skf3010Sc002CommonDto initDto) {
		// 画面に残っている添付資料情報セッションをクリア
		// 社宅補足ファイル名1
		initDto.setShatakuHosokuFileName1(null);
		// 社宅補足ファイル名2
		initDto.setShatakuHosokuFileName2(null);
		// 社宅補足ファイル名3
		initDto.setShatakuHosokuFileName3(null);
		// 社宅補足リンク名1
		initDto.setShatakuHosokuLink1(null);
		// 社宅補足リンク名2
		initDto.setShatakuHosokuLink2(null);
		// 社宅補足リンク名3
		initDto.setShatakuHosokuLink3(null);
		// 社宅補足サイズ1
		initDto.setShatakuHosokuSize1(null);
		// 社宅補足サイズ2
		initDto.setShatakuHosokuSize2(null);
		// 社宅補足サイズ3
		initDto.setShatakuHosokuSize3(null);
		// 社宅補足ファイル1
		initDto.setShatakuHosokuFile1(null);
		// 社宅補足ファイル2
		initDto.setShatakuHosokuFile2(null);
		// 社宅補足ファイル3
		initDto.setShatakuHosokuFile3(null);

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
		// 駐車場補足サイズ1
		initDto.setParkingHosokuSize1(null);
		// 駐車場補足サイズ2
		initDto.setParkingHosokuSize2(null);
		// 駐車場補足サイズ3
		initDto.setParkingHosokuSize3(null);
		// 駐車場補足ファイル1
		initDto.setParkingHosokuFile1(null);
		// 駐車場補足ファイル2
		initDto.setParkingHosokuFile2(null);
		// 駐車場補足ファイル3
		initDto.setParkingHosokuFile3(null);
	}

	/**
	 * エラーコントロール初期化
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param initDto	*DTO
	 */
	public void clearVaridateErr(Skf3010Sc002CommonDto initDto) {

		// 社宅名
		initDto.setShatakuNameErr(null);
		// 地域区分
		initDto.setAreaKbnErr(null);
		// 社宅区分
		initDto.setShatakuKbnErr(null);
		// 利用区分
		initDto.setUseKbnKbnErr(null);
		// 管理会社
		initDto.setManageCompanyErr(null);
		// 管理機関
		initDto.setManageAgencyErr(null);
		// 管理事業領域
		initDto.setManageBusinessAreaErr(null);
		// 郵便番号
		initDto.setZipCdErr(null);
		// 都道府県
		initDto.setPrefErr(null);
		// 社宅住所
		initDto.setShatakuAddressErr(null);
		// 社宅構造
		initDto.setShatakuStructureErr(null);
		// 建築年月日
		initDto.setBuildDateErr(null);
		// 駐車場構造
		initDto.setParkingStructureErr(null);
		// 寮長・自治会長 メールアドレス
		initDto.setDormitoryLeaderMailAddressErr(null);
		// 鍵管理者 メールアドレス
		initDto.setKeyManagerMailAddressErr(null);
		// 寮母・管理会社 メールアドレス
		initDto.setMatronMailAddressErr(null);
		// 寮長・自治会長 電話番号
		initDto.setDormitoryLeaderTelNumberErr(null);
		// 鍵管理者 電話番号
		initDto.setKeyManagerTelNumberErr(null);
		// 寮母・管理会社 電話番号
		initDto.setMatronTelNumberErr(null);
		// 寮長・自治会長 内線番号
		initDto.setDormitoryLeaderExtentionNoErr(null);
		// 鍵管理者 内線番号
		initDto.setKeyManagerExtentionNoErr(null);
		// 寮母・管理会社 内線番号
		initDto.setMatronExtentionNoErr(null);
		// 賃貸人（代理人）名
		initDto.setContractOwnerNameErr(null);
		// 経理連携用管理番号
		initDto.setAssetRegisterNoErr(null);
		// 契約開始日
		initDto.setContractStartDayErr(null);
		// 契約終了日
		initDto.setContractEndDayErr(null);
		// 家賃
		initDto.setContractRentErr(null);
		// 共益費
		initDto.setContractKyoekihiErr(null);
		// 駐車場料（地代）
		initDto.setContractLandRentErr(null);
	}

	/**
	 * 社宅区分ドロップダウンリスト借上(一棟含む)削除
	 * DTOの社宅区分ドロップダウンリスト選択値から「一棟借上」、「借上」を削除する
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param initDto	*DTO
	 */
	public void deleteShatakuKbnDrpDwKariage(Skf3010Sc002CommonDto initDto) {
		if (Objects.equals(initDto.getShatakuKbnList(), null)) {
			return;
		}
		int ittoNo = -1;
		int kariageNo = -1;
		for (int i = 0; i < initDto.getShatakuKbnList().size(); i++) {
			Map <String, Object> workMap = initDto.getShatakuKbnList().get(i);
			if (CodeConstant.ITTOU.equals(workMap.get("value"))) {
				ittoNo = i;
			} else 	if (CodeConstant.KARIAGE.equals(workMap.get("value"))) {
				kariageNo = i;
			}
		}
		if (ittoNo != -1) {
			initDto.getShatakuKbnList().remove(ittoNo);
		}
		if (kariageNo != -1) {
			initDto.getShatakuKbnList().remove(kariageNo);
		}
	}

	/**
	 * 元の画面に戻す
	 * エラー検出コントロールは背景をエラー背景に設定する
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param drpDwnSelectedList	ドロップダウン選択値リスト
	 * @param labelList				可変ラベル値リスト
	 * @param bihinList				備品リスト
	 * @param parkingList			区画駐車場リスト
	 * @param comDto				*DTO
	 */
	public void setBeforeInfo(
			List<Map<String, Object>> drpDwnSelectedList,
			List<Map<String, Object>> labelList,
			List<Map<String, Object>> bihinList,
			List<Map<String, Object>> parkingList,
			Skf3010Sc002CommonDto comDto) {

		// ドロップダウンリスト復元
		setErrResultDrpDwn(drpDwnSelectedList, comDto);
		// 可変ラベル値復元、駐車場基本使用料取得
		String parkingRent = setErrVariableLabel(labelList, comDto);
		// 駐車場区画情報復元
		setErrParkingBlock(parkingRent, parkingList, comDto);
		// 備品情報復元
		setErrBihin(bihinList, comDto);
		// 契約情報復元
		setErrContract(comDto);
	}

	/**
	 * エラー時ドロップダウンリスト設定処理
	 * ドロップダウンリストを作成し、エラー時の選択値を設定状態に設定する。
	 * 但し、契約番号ドロップダウン以外はDBから再取得している
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param drpDwnSelectedList	エラー時ドロップダウン選択値リスト
	 * @param comDto				*DTO
	 */
	private void setErrResultDrpDwn(
			List<Map<String, Object>> drpDwnSelectedList,
			Skf3010Sc002CommonDto comDto) {

		/** DTO設定用 */
		// ドロップダウンリスト
		// 地域区分リスト
		List<Map<String, Object>> areaKbnList = new ArrayList<Map<String, Object>>();
		// 社宅区分リスト
		List<Map<String, Object>> shatakuKbnList = new ArrayList<Map<String, Object>>();
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
		// 駐車場構造リスト
		List<Map<String, Object>> parkingStructureList = new ArrayList<Map<String, Object>>();

		// ドロップダウンリスト選択値
		Map <String, Object> drpDwnMap = drpDwnSelectedList.get(0);
		// 地域区分
		String areaKbn = (drpDwnMap.get("areaKbn") != null) ? drpDwnMap.get("areaKbn").toString() : "";
		// 社宅区分
		String shatakuKbn = (drpDwnMap.get("shatakuKbn") != null) ? drpDwnMap.get("shatakuKbn").toString() : "";
		// 利用区分
		String useKbn = (drpDwnMap.get("useKbn") != null) ? drpDwnMap.get("useKbn").toString() : "";
		// 管理会社
		String manageCompany = (drpDwnMap.get("manageCompany") != null) ? drpDwnMap.get("manageCompany").toString() : "";
		// 管理機関
		String manageAgency = (drpDwnMap.get("manageAgency") != null) ? drpDwnMap.get("manageAgency").toString() : "";
		// 管理事業領域
		String manageBusinessArea = (drpDwnMap.get("manageBusinessArea") != null) ? drpDwnMap.get("manageBusinessArea").toString() : "";
		// 都道府県
		String pref = (drpDwnMap.get("pref") != null) ? drpDwnMap.get("pref").toString() : "";
		// 社宅構造
		String shatakuStructure = (drpDwnMap.get("shatakuStructure") != null) ? drpDwnMap.get("shatakuStructure").toString() : "";
		// エレベーター
		String elevator = (drpDwnMap.get("elevator") != null) ? drpDwnMap.get("elevator").toString() : "";
		// 駐車場構造
		String parkingStructure = (drpDwnMap.get("parkingStructure") != null) ? drpDwnMap.get("parkingStructure").toString() : "";
		// 契約番号
		String contractNo = (drpDwnMap.get("contractNo") != null) ? drpDwnMap.get("contractNo").toString() : "";

		/** ドロップダウンリスト作成 */
		// 「地域区分」
		areaKbnList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_AREA_KBN, areaKbn, true));
		// 「社宅区分」
		shatakuKbnList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_SHATAKU_KBN, shatakuKbn, true));
		// 社宅区分ドロップダウンより借上削除
		deleteShatakuKbnDrpDwKariage(comDto);
		// 管理系ドロップダウン取得
		getDoropDownList(useKbn, useKbnList, manageCompany, manageCompanyList, manageAgency,
				manageAgencyList, manageBusinessArea, manageBusinessAreaList, pref, prefList,
								shatakuStructure, shatakuStructureList, elevator, elevatorList);
		// 「駐車場構造区分」
		parkingStructureList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_PARKING_STRUCTURE_KBN, parkingStructure, true));
		// 契約番号
		List<Map<String, Object>> contractNoList = new ArrayList<Map<String, Object>>();
		// 契約番号ドロップダウンリスト取得
		if (comDto.getContractNoList() != null) {
			contractNoList.addAll(comDto.getContractNoList());
		}
		// 契約番号指定判定
		if (contractNo.length() > 0) {
			// 契約番号ループ
			for (Map<String, Object> contractMap : contractNoList) {
				// 選択値判定
				if (Objects.equals(contractNo, contractMap.get("value"))) {
					// ドロップダウン選択値設定
					contractMap.put("selected", "true");
				} else {
					// ドロップダウン選択解除
					contractMap.remove("selected");
				}
			}
		}
		/** 戻り値設定 */
		// クリア
		comDto.setAreaKbnList(null);
		comDto.setShatakuKbnList(null);
		comDto.setUseKbnList(null);
		comDto.setManageCompanyList(null);
		comDto.setManageAgencyList(null);
		comDto.setManageBusinessAreaList(null);
		comDto.setPrefList(null);
		comDto.setShatakuStructureList(null);
		comDto.setElevatorList(null);
		comDto.setParkingStructureList(null);
		// 設定
		comDto.setAreaKbnList(areaKbnList);
		comDto.setShatakuKbnList(shatakuKbnList);
		comDto.setUseKbnList(useKbnList);
		comDto.setManageCompanyList(manageCompanyList);
		comDto.setManageAgencyList(manageAgencyList);
		comDto.setManageBusinessAreaList(manageBusinessAreaList);
		comDto.setPrefList(prefList);
		comDto.setShatakuStructureList(shatakuStructureList);
		comDto.setElevatorList(elevatorList);
		comDto.setParkingStructureList(parkingStructureList);
		comDto.setContractNoList(contractNoList);
	}

	/**
	 * エラー時可変ラベル値復元
	 * エラー時の可変ラベルの値を設定する。
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param labelList		可変ラベルリスト
	 * @param comDto		*DTO
	 */
	private String setErrVariableLabel(List<Map<String, Object>> labelList, Skf3010Sc002CommonDto comDto) {
		// 可変ラベル値
		Map <String, Object> labelMap = labelList.get(0);
		// 実年数
		String jituAge =
				(labelMap.get("realYearCount") != null) ? labelMap.get("realYearCount").toString() : "0";
		// 次回算定年月日
		String nextCalcDate =
				(labelMap.get("nextCalculateDate") != null) ? labelMap.get("nextCalculateDate").toString() : "";
		// 経年
		String aging =
				(labelMap.get("aging") != null) ? labelMap.get("aging").toString() : "0";
		// 駐車場基本使用料
		String parkingRent =
				(labelMap.get("parkingBasicRent") != null) ? labelMap.get("parkingBasicRent").toString() : "0";
		// 駐車場台数
		String parkingBlockCount =
				(labelMap.get("parkingBlockCount") != null) ? labelMap.get("parkingBlockCount").toString() : "0";
		// 貸与可能総数
		String lendPossibleCount =
				(labelMap.get("lendPossibleCount") != null) ? labelMap.get("lendPossibleCount").toString() : "0";

		/** 戻り値設定 */
		// クリア
		comDto.setJituAge(null);
		comDto.setNextCalcDate(null);
		comDto.setAging(null);
		comDto.setParkingRent(null);
		comDto.setParkingBlockCount(null);
		comDto.setLendPossibleCount(null);
		// 設定
		comDto.setJituAge(jituAge);
		comDto.setNextCalcDate(nextCalcDate);
		comDto.setAging(aging);
		comDto.setParkingRent(parkingRent);
		comDto.setParkingBlockCount(parkingBlockCount);
		comDto.setLendPossibleCount(lendPossibleCount);

		return parkingRent;
	}

	/**
	 * エラー時駐車場区画情報復元
	 * エラー時の駐車場区画情報を設定する。
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param parkingRent	駐車場月額使用料(基本)
	 * @param parkingList	駐車場区画情報リスト
	 * @param comDto		*DTO
	 */
	private void setErrParkingBlock(String parkingRent,
				List<Map<String, Object>> parkingList, Skf3010Sc002CommonDto comDto) {

		List<Map<String, Object>> setViewList = new ArrayList<Map<String, Object>>();
		// 貸与区分ドロップダウン
		List<Map<String, Object>> lendStatusList =
				ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_LEND_KBN, "",false);
		// 駐車場月額使用料
		Long parkingRentMany = 0L;
		if (parkingRent != null && parkingRent.length() > 0) {
			parkingRentMany = Long.parseLong(parkingRent.replace(",", ""));
		}

		for (Map<String, Object> tmpData : parkingList) {
			// RelativeID
			String rId = tmpData.get("rId").toString();
			// 駐車場管理番号
			String parkingKanriNo =
					(tmpData.get("parkingKanriNo") != null) ? tmpData.get("parkingKanriNo").toString() : "";
			// 区画番号
			String blockNo =
					(tmpData.get("parkingBlock") != null) ? tmpData.get("parkingBlock").toString() : "";
			// 区画貸与区分
			String parkingLendKbn = tmpData.get("parkingLendKbn").toString();
			// 区画貸与状況
			String parkingLendStatus = tmpData.get("parkingLendStatus").toString();
			// 使用者
			String shainName = (tmpData.get("shainName") != null) ? tmpData.get("shainName").toString() : "";
			// 駐車場調整金額
			String parkingRentalAdjust = tmpData.get("parkingRentalAdjust").toString();
			// 区画駐車場月額使用料
			Long  parkingBlockRentMany = parkingRentMany;
			// 備考
			String biko = (tmpData.get("parkingBiko") != null) ? tmpData.get("parkingBiko").toString() : "";

			/** エスケープ設定 */
			// 区画番号
			blockNo = HtmlUtils.htmlEscape(blockNo);
			// 使用者
			shainName = HtmlUtils.htmlEscape(shainName);
			// 駐車場調整金額
			parkingRentalAdjust = HtmlUtils.htmlEscape(parkingRentalAdjust);
			// 備考
			biko = HtmlUtils.htmlEscape(biko);

			Map<String, Object> tmpMap = new HashMap<String, Object>();
			// RelativeID
			tmpMap.put("rId", rId);
			// 区画管理番号
			tmpMap.put("parkingKanriNo", HtmlUtils.htmlEscape(parkingKanriNo));
			// 区画番号エラー表示判定
			if (tmpData.containsKey("parkingBlockNoErr")) {
				// エラー表示設定
				tmpMap.put("parkingBlock", "<input id='parkingBlockNo" + rId + "' name='parkingBlockNo" + rId 
								+ "' placeholder='例　01' type='text' value='"
								+ blockNo + "' class='nfw-validation-error' style='width:140px;' maxlength='30'/>");
			} else {
				tmpMap.put("parkingBlock", "<input id='parkingBlockNo" + rId + "' name='parkingBlockNo" + rId 
								+ "' placeholder='例　01' type='text' value='"
								+ blockNo + "' style='width:140px;' maxlength='30'/>");
			}
			// 「貸与区分」プルダウンの設定
			String lendStatusListCode =  createStatusSelect(parkingLendKbn,lendStatusList);
			// 貸与区分ステータス判定
			if ("1".equals(parkingLendKbn)) {
				// 貸与可能(アクティブ)
				tmpMap.put("parkingLendKbn", "<select id='parkingLendKbn" + rId 
						+ "' name='parkingLendKbn" + rId + "' style='width:90px;'>"
												+ lendStatusListCode + "</select>");
			} else if (parkingKanriNo.length() > 0){
				// 貸与不可(非活性)
				tmpMap.put("parkingLendKbn", "<select id='parkingLendKbn" + rId 
						+ "' name='parkingLendKbn" + rId + "' style='width:90px;' disabled>"
												+ lendStatusListCode + "</select>");
			} else {
				// 貸与不可(活性)
				tmpMap.put("parkingLendKbn", "<select id='parkingLendKbn" + rId 
						+ "' name='parkingLendKbn" + rId + "' style='width:90px;' >"
												+ lendStatusListCode + "</select>");
			}
			// 貸与状況
			tmpMap.put("parkingLendStatus", HtmlUtils.htmlEscape(parkingLendStatus));
			// 使用者
			tmpMap.put("shainName", shainName);
			// 駐車場調整金額エラー表示判定
			if (tmpData.containsKey("parkingRentalAdjustErr")) {
				// エラー表示設定
				tmpMap.put("parkingRentalAdjust", "<input id='parkingRentalAdjust" + rId 
					+ "' name='parkingRentalAdjust" + rId + "' type='text' class='nfw-validation-error' value='"
					+ parkingRentalAdjust + "' placeholder='例　半角数字'  style='ime-mode: disabled; width:75px;text-align: right;' maxlength='6'/> 円");
			} else {
				tmpMap.put("parkingRentalAdjust", "<input id='parkingRentalAdjust" + rId 
						+ "' name='parkingRentalAdjust" + rId + "' type='text' value='"
						+ parkingRentalAdjust + "' placeholder='例　半角数字'  style='ime-mode: disabled; width:75px;text-align: right;' maxlength='6'/> 円");
			}
			// 駐車場月額使用料
			if (CheckUtils.isNumberFormat(parkingRentalAdjust) && parkingRentalAdjust.length() > 0) {
				parkingBlockRentMany += Long.parseLong(parkingRentalAdjust);
			}
			tmpMap.put("parkingMonthRental", "<label id='parkingMonthRental" + rId
					+ "' name='parkingMonthRental" + rId + "' >"
					+ HtmlUtils.htmlEscape(String.format("%,d", parkingBlockRentMany) + " 円")
					+ "</label>");
			// 駐車場備考
			tmpMap.put("parkingBiko", "<input id='parkingBlockBiko" + rId 
					+ "' name='parkingBlockBiko" + rId + "' type='text' value='"
					+ biko + " ' style='width:215px;' maxlength='100'/>");
			// 削除ボタン
			tmpMap.put("parkingBlockDelete", "");
			// 更新日時
			tmpMap.put("updateDate", HtmlUtils.htmlEscape(tmpData.get("updateDate").toString()));
			setViewList.add(tmpMap);
		}
		// 解放
		lendStatusList = null;

		// 駐車場区画情報設定
		comDto.setParkingInfoListTableData(null);
		comDto.setParkingInfoListTableData(setViewList);
	}

	/**
	 * エラー時備品情報復元
	 * エラー時の備品情報を設定する。
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param bihinList	備品情報リスト
	 * @param comDto	*DTO
	 */
	private void setErrBihin(List<Map<String, Object>> bihinList, Skf3010Sc002CommonDto comDto) {

		List<Map<String, Object>> setViewList = new ArrayList<Map<String, Object>>();

		List<Map<String, Object>> statusList = ddlUtils.getGenericForDoropDownList(
							FunctionIdConstant.GENERIC_CODE_BIHINSTATUS_KBN, "",false);

		int i = 0;
		for (Map <String, Object> tmpData : bihinList) {
			/** DTO設定用 */
			// 備品コード
			String bihinCode = HtmlUtils.htmlEscape(tmpData.get("bihinCd").toString());
			// 備品名
			String bihinName = HtmlUtils.htmlEscape(tmpData.get("bihinName").toString());
			// 備付状況選択値
			String bihinStatus = tmpData.get("bihinStatusKbn").toString();

			Map<String, Object> tmpMap = new HashMap<String, Object>();

			// 備品コード
			tmpMap.put("bihinCd", bihinCode);
			// 備品名
			tmpMap.put("bihinName", bihinName);
			// 備品備付状況
			String statusListCode = createStatusSelect(bihinStatus, statusList);
			tmpMap.put("bihinStatusKbn","<select id='bihinStatus" + i 
						+ "' name='bihinStatus" + i + "'>" + statusListCode + "</select>");
			// 更新日時
			tmpMap.put("updateDate", HtmlUtils.htmlEscape(tmpData.get("updateDate").toString()));
			setViewList.add(tmpMap);
			i++;
		}
		// 解放
		statusList = null;

		comDto.setBihinInfoListTableData(null);
		comDto.setBihinInfoListTableData(setViewList);
	}

	/**
	 * エラー時契約情報復元
	 * 契約情報のナンバーボックスの「,」を除去する
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param comDto	*DTO
	 */
	private void setErrContract(Skf3010Sc002CommonDto comDto) {

		// ナンバーボックス取得
		String contractRent = (comDto.getContractRent() != null) ? comDto.getContractRent() : "";
		String contractKyoekihi = (comDto.getContractKyoekihi() != null) ? comDto.getContractKyoekihi() : "";
		String contractLandRent = (comDto.getContractLandRent() != null) ? comDto.getContractLandRent() : "";

		// 「,」を除去して設定
		comDto.setContractRent(contractRent.replace(",", ""));
		comDto.setContractKyoekihi(contractKyoekihi.replace(",", ""));
		comDto.setContractLandRent(contractLandRent.replace(",", ""));
	}

	/**
	 * 社宅基本情報マスタ更新データ作成
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param mShataku				*社宅基本情報マスタエンティティ
	 * @param drpDwnSelectedList	ドロップダウン選択値リスト
	 * @param labelList				可変ラベル値リスト
	 * @param comDto				DTO
	 */
	public void setUpdateColumShatakuKihon(
			Skf3010Sc002MShatakuTableDataExpParameter mShataku, List<Map<String, Object>> drpDwnSelectedList,
				List<Map<String, Object>> labelList, Skf3010Sc002CommonDto comDto) {

		Map <String, Object> drpDwnSelectedMap = drpDwnSelectedList.get(0);
		Map <String, Object> labelMap = labelList.get(0);

		/** 社宅基本情報マスタの更新データを作成 */
		// 郵便番号
		String zipCd = ("".equals(comDto.getZipCd())) ? null : comDto.getZipCd();
		// 社宅構造補足
		String structureSupplement = ("".equals(comDto.getShatakuStructureDetail())) ? null : comDto.getShatakuStructureDetail();
		// エレベーター区分
		String elevatorKbn = ("".equals(drpDwnSelectedMap.get("elevator")) ? null : drpDwnSelectedMap.get("elevator").toString());
		// 備考
		String biko = ("".equals(comDto.getBiko())) ? null : comDto.getBiko();

		// 社宅補足ファイル名
		String shatakuSupplementName1 = ("".equals(comDto.getShatakuHosokuFileName1())) ? null : comDto.getShatakuHosokuFileName1();
		String shatakuSupplementName2 = ("".equals(comDto.getShatakuHosokuFileName2())) ? null : comDto.getShatakuHosokuFileName2();
		String shatakuSupplementName3 = ("".equals(comDto.getShatakuHosokuFileName3())) ? null : comDto.getShatakuHosokuFileName3();
		// 社宅補足ファイルサイズ
		String shatakuSupplementSize1 = ("".equals(comDto.getShatakuHosokuSize1())) ? null : comDto.getShatakuHosokuSize1();
		String shatakuSupplementSize2 = ("".equals(comDto.getShatakuHosokuSize2())) ? null : comDto.getShatakuHosokuSize2();
		String shatakuSupplementSize3 = ("".equals(comDto.getShatakuHosokuSize3())) ? null : comDto.getShatakuHosokuSize3();

		// 社宅名
		mShataku.setShatakuName(comDto.getShatakuName());
		// 社宅区分
		mShataku.setShatakuKbn(drpDwnSelectedMap.get("shatakuKbn").toString());
		// 地域区分
		mShataku.setAreaKbn(drpDwnSelectedMap.get("areaKbn").toString());
		// 利用区分
		mShataku.setUseKbn(drpDwnSelectedMap.get("useKbn").toString());
		// 会社コード
		mShataku.setManegeCompanyCd(drpDwnSelectedMap.get("manageCompany").toString());
		// 管理会社コード
		mShataku.setManegeAgencyCd(drpDwnSelectedMap.get("manageAgency").toString());
		// 管理事業領域コード
		mShataku.setManegeBusinessAreaCd(drpDwnSelectedMap.get("manageBusinessArea").toString());
		// 郵便番号
		mShataku.setZipCd(zipCd);
		// 都道府県コード
		mShataku.setPrefCd(drpDwnSelectedMap.get("pref").toString());
		// 住所
		mShataku.setAddress(comDto.getShatakuAddress());
		// 建築年月日
		mShataku.setBuildDate(comDto.getBuildDate().replace("/", ""));
		// 社宅構造区分
		mShataku.setStructureKbn(drpDwnSelectedMap.get("shatakuStructure").toString());
		// 社宅構造補足
		mShataku.setStructureSupplement(structureSupplement);
		// エレベーター区分
		mShataku.setElevatorKbn(elevatorKbn);
		// 次回算定年月日
		mShataku.setNextCalculateDate(labelMap.get("nextCalculateDate").toString().replace("/", ""));
		// 社宅補足ファイル名
		mShataku.setShatakuSupplementName1(shatakuSupplementName1);
		mShataku.setShatakuSupplementName2(shatakuSupplementName2);
		mShataku.setShatakuSupplementName3(shatakuSupplementName3);
		// 社宅補足ファイルサイズ
		mShataku.setShatakuSupplementSize1(shatakuSupplementSize1);
		mShataku.setShatakuSupplementSize2(shatakuSupplementSize2);
		mShataku.setShatakuSupplementSize3(shatakuSupplementSize3);
		// 社宅補足ファイル
		mShataku.setShatakuSupplementFile1(comDto.getShatakuHosokuFile1());
		mShataku.setShatakuSupplementFile2(comDto.getShatakuHosokuFile2());
		mShataku.setShatakuSupplementFile3(comDto.getShatakuHosokuFile3());
		// 備考
		mShataku.setBiko(biko);
		// 更新日時
		mShataku.setLastUpdateDate(comDto.getKihonUpdateDate());
	}

	/**
	 * 社宅駐車場情報マスタ更新データ作成
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param mShatakuParking		*社宅駐車場情報マスタエンティティ
	 * @param drpDwnSelectedList	ドロップダウン選択値リスト
	 * @param labelList				可変ラベル値リスト
	 * @param comDto				DTO
	 */
	public void setUpdateColumParking(
			Skf3010Sc002MShatakuParkingTableDataExpParameter mShatakuParking, List<Map<String, Object>> drpDwnSelectedList,
			List<Map<String, Object>> labelList, Skf3010Sc002CommonDto comDto) {

		Map <String, Object> drpDwnSelectedMap = drpDwnSelectedList.get(0);
		Map <String, Object> labelMap = labelList.get(0);

		/** 社宅駐車場情報マスタの更新データを作成 */
		// 駐車場補足ファイル名
		String fileName1 = ("".equals(comDto.getParkingHosokuFileName1())) ? null: comDto.getParkingHosokuFileName1();
		String fileName2 = ("".equals(comDto.getParkingHosokuFileName2())) ? null: comDto.getParkingHosokuFileName2();
		String fileName3 = ("".equals(comDto.getParkingHosokuFileName3())) ? null: comDto.getParkingHosokuFileName3();
		// ファイルサイズ
		String fileSize1 = ("".equals(comDto.getParkingHosokuSize1())) ? null: comDto.getParkingHosokuSize1();
		String fileSize2 = ("".equals(comDto.getParkingHosokuSize2())) ? null: comDto.getParkingHosokuSize2();
		String fileSize3 = ("".equals(comDto.getParkingHosokuSize3())) ? null: comDto.getParkingHosokuSize3();
		// 備考
		String biko = ("".equals(comDto.getParkingBiko())) ? null : comDto.getParkingBiko();

		// 駐車場構造
		mShatakuParking.setParkingStructureKbn(drpDwnSelectedMap.get("parkingStructure").toString());
		// 駐車場補足ファイル名
		mShatakuParking.setParkingSupplementName1(fileName1);
		mShatakuParking.setParkingSupplementName2(fileName2);
		mShatakuParking.setParkingSupplementName3(fileName3);
		// 駐車王補足ファイルサイズ
		mShatakuParking.setParkingSupplementSize1(fileSize1);
		mShatakuParking.setParkingSupplementSize2(fileSize2);
		mShatakuParking.setParkingSupplementSize3(fileSize3);
		// 駐車場補足ファイル
		mShatakuParking.setParkingSupplementFile1(comDto.getParkingHosokuFile1());
		mShatakuParking.setParkingSupplementFile2(comDto.getParkingHosokuFile2());
		mShatakuParking.setParkingSupplementFile3(comDto.getParkingHosokuFile3());
		// 備考
		mShatakuParking.setParkingBiko(biko);
		// 駐車場基本使用料
		mShatakuParking.setParkingRental(Integer.parseInt(labelMap.get("parkingBasicRent").toString().replace(",", "")));
		// 更新日時
		mShatakuParking.setLastUpdateDate(comDto.getParkingUpdateDate());
	}

	/**
	 * 社宅駐車場区画情報マスタ更新データ作成
	 * 新規区画には駐車場管理番号に「0」を設定する
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param mShatakuParkingBlockList	*社宅駐車場区画情報マスタエンティティ
	 * @param parkingList				駐車場区画情報リスト
	 * @throws ParseException 
	 */
	public void setUpdateColumParkingBlock(
			List<Skf3010MShatakuParkingBlock> mShatakuParkingBlockList, 
			List<Map<String, Object>> parkingList) throws ParseException {

		/** 社宅駐車場区画情報マスタの更新データ作成 */
		// 区画情報数分ループ
		for (Map<String, Object> parkingMap : parkingList) {
			Skf3010MShatakuParkingBlock mShatakuParkingBlock = new Skf3010MShatakuParkingBlock();
			// 駐車場管理番号
			Long parkingKanriNo = 0L;
			if (parkingMap.get("parkingKanriNo") != null
				&& parkingMap.get("parkingKanriNo").toString().length() > 0) {
				try {
					parkingKanriNo = Long.parseLong(parkingMap.get("parkingKanriNo").toString());
				} catch(NumberFormatException e) {}
			}
			mShatakuParkingBlock.setParkingKanriNo(parkingKanriNo);
			// 区画番号
			mShatakuParkingBlock.setParkingBlock(parkingMap.get("parkingBlock").toString());
			// 貸与区分
			mShatakuParkingBlock.setParkingLendKbn(parkingMap.get("parkingLendKbn").toString());
			// 貸与状況
			mShatakuParkingBlock.setParkingLendJokyo(parkingMap.get("parkingLendStatus").toString());
			// 調整金額
			mShatakuParkingBlock.setParkingRentalAdjust(Integer.parseInt(parkingMap.get("parkingRentalAdjust").toString()));
			// 備考
			String biko = ("".equals(parkingMap.get("parkingBiko").toString())) ? null: parkingMap.get("parkingBiko").toString();
			mShatakuParkingBlock.setParkingBiko(biko);
			// 更新日時
			if (parkingMap.get("updateDate") != null && parkingMap.get("updateDate").toString().length() > 0) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
				mShatakuParkingBlock.setLastUpdateDate(dateFormat.parse(parkingMap.get("updateDate").toString()));
			}
			// リストに追加
			mShatakuParkingBlockList.add(mShatakuParkingBlock);
			mShatakuParkingBlock = null;
		}
	}

	/**
	 * 社宅管理者情報マスタ更新データ作成
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param mShatakuManageList	*社宅管理者情報マスタエンティティ
	 * @param comDto				DTO
	 */
	public void setUpdateColumShatakuManage(
			List<Skf3010MShatakuManege> mShatakuManageList, Skf3010Sc002CommonDto comDto) {

		/** 社宅管理者情報マスタの更新データを作成 */
		String roomNo = null;
		String name = null;
		String mailAddress = null;
		String telNumber = null;
		String extensionNo = null;
		String biko = null;

		/** 寮長・自治会長 */
		Skf3010MShatakuManege domitoryLeaderMap = new Skf3010MShatakuManege();
		// 管理者区分
		domitoryLeaderMap.setManegeKbn(Skf3010Sc002CommonDto.MANAGE_KBN_DOMITRY_LEADER);
		// 部屋番号：寮長・自治会長
		roomNo = ("".equals(comDto.getDormitoryLeaderRoomNo())) ? null: comDto.getDormitoryLeaderRoomNo();
		domitoryLeaderMap.setManegeShatakuNo(roomNo);
		// 氏名：寮長・自治会長
		name = ("".equals(comDto.getDormitoryLeaderName())) ? null: comDto.getDormitoryLeaderName();
		domitoryLeaderMap.setManegeName(name);
		// 電子メールアドレス：寮長・自治会長
		mailAddress = ("".equals(comDto.getDormitoryLeaderMailAddress())) ? null: comDto.getDormitoryLeaderMailAddress();
		domitoryLeaderMap.setManegeMailAddress(mailAddress);
		// 電話番号：寮長・自治会長
		telNumber = ("".equals(comDto.getDormitoryLeaderTelNumber())) ? null: comDto.getDormitoryLeaderTelNumber();
		domitoryLeaderMap.setManegeTelNo(telNumber);
		// 内線番号：寮長・自治会長
		extensionNo = ("".equals(comDto.getDormitoryLeaderExtentionNo())) ? null: comDto.getDormitoryLeaderExtentionNo();
		domitoryLeaderMap.setManegeExtensionNo(extensionNo);
		// 備考：寮長・自治会長
		biko = ("".equals(comDto.getDormitoryLeaderBiko())) ? null: comDto.getDormitoryLeaderBiko();
		domitoryLeaderMap.setBiko(biko);
		// 更新日時
		domitoryLeaderMap.setLastUpdateDate(comDto.getDormitoryLeaderUpdateDate());

		/** 鍵管理者 */
		Skf3010MShatakuManege keyMngMap = new Skf3010MShatakuManege();
		// 管理者区分
		keyMngMap.setManegeKbn(Skf3010Sc002CommonDto.MANAGE_KBN_KEY_MANAGER);
		// 部屋番号 ：鍵管理者
		roomNo = ("".equals(comDto.getKeyManagerRoomNo())) ? null: comDto.getKeyManagerRoomNo();
		keyMngMap.setManegeShatakuNo(roomNo);
		// 氏名：鍵管理者
		name = ("".equals(comDto.getKeyManagerName())) ? null: comDto.getKeyManagerName();
		keyMngMap.setManegeName(name);
		// 電子メールアドレス：鍵管理者
		mailAddress = ("".equals(comDto.getKeyManagerMailAddress())) ? null: comDto.getKeyManagerMailAddress();
		keyMngMap.setManegeMailAddress(mailAddress);
		// 電話番号：鍵管理者
		telNumber = ("".equals(comDto.getKeyManagerTelNumber())) ? null: comDto.getKeyManagerTelNumber();
		keyMngMap.setManegeTelNo(telNumber);
		// 内線番号：鍵管理者
		extensionNo = ("".equals(comDto.getKeyManagerExtentionNo())) ? null: comDto.getKeyManagerExtentionNo();
		keyMngMap.setManegeExtensionNo(extensionNo);
		// 備考：鍵管理者
		biko = ("".equals(comDto.getKeyManagerBiko())) ? null: comDto.getKeyManagerBiko();
		keyMngMap.setBiko(biko);
		// 更新日時
		keyMngMap.setLastUpdateDate(comDto.getKeyManagerUpdateDate());

		/** 寮母・管理会社 */
		Skf3010MShatakuManege matronMap = new Skf3010MShatakuManege();
		// 管理者区分
		matronMap.setManegeKbn(Skf3010Sc002CommonDto.MANAGE_KBN_MATRON);
		// 部屋番号 ：鍵管理者
		roomNo = ("".equals(comDto.getMatronRoomNo())) ? null: comDto.getMatronRoomNo();
		matronMap.setManegeShatakuNo(roomNo);
		// 氏名：鍵管理者
		name = ("".equals(comDto.getMatronName())) ? null: comDto.getMatronName();
		matronMap.setManegeName(name);
		// 電子メールアドレス：鍵管理者
		mailAddress = ("".equals(comDto.getMatronMailAddress())) ? null: comDto.getMatronMailAddress();
		matronMap.setManegeMailAddress(mailAddress);
		// 電話番号：鍵管理者
		telNumber = ("".equals(comDto.getMatronTelNumber())) ? null: comDto.getMatronTelNumber();
		matronMap.setManegeTelNo(telNumber);
		// 内線番号：鍵管理者
		extensionNo = ("".equals(comDto.getMatronExtentionNo())) ? null: comDto.getMatronExtentionNo();
		matronMap.setManegeExtensionNo(extensionNo);
		// 備考：鍵管理者
		biko = ("".equals(comDto.getMatronBiko())) ? null: comDto.getMatronBiko();
		matronMap.setBiko(biko);
		// 更新日時
		matronMap.setLastUpdateDate(comDto.getMatronUpdateDate());
		// リストに追加
		mShatakuManageList.add(domitoryLeaderMap);
		mShatakuManageList.add(keyMngMap);
		mShatakuManageList.add(matronMap);
		domitoryLeaderMap = null;
		keyMngMap = null;
		matronMap = null;
	}

	/**
	 * 社宅備品情報マスタ更新データ作成
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param mShatakuBihinList	*社宅備品情報マスタエンティティ
	 * @param bihinList			備品リスト
	 * @param comDto			DTO
	 * @throws ParseException 
	 */
	public void setUpdateColumBihin(
			List<Skf3010MShatakuBihin> mShatakuBihinList, 
			List<Map<String, Object>> bihinList, Skf3010Sc002CommonDto comDto) throws ParseException {

		/** 社宅備品情報マスタの更新データを作成 */
		// 備品情報数分ループ
		for (Map<String, Object> bihinMap : bihinList) {
			Skf3010MShatakuBihin mShatakuBihin = new Skf3010MShatakuBihin();
			// 備品コード
			mShatakuBihin.setBihinCd(bihinMap.get("bihinCd").toString());
			// 備付区分
			mShatakuBihin.setBihinStatusKbn(bihinMap.get("bihinStatusKbn").toString());
			// 更新日時
			if (bihinMap.get("updateDate") != null && bihinMap.get("updateDate").toString().length() > 0) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
				mShatakuBihin.setLastUpdateDate(dateFormat.parse(bihinMap.get("updateDate").toString()));
			}
			mShatakuBihinList.add(mShatakuBihin);
		}
		// 非表示備品数分ループ
		List<Map<String, Object>> noDispBihin = new ArrayList<Map<String, Object>>();
		noDispBihin.addAll(comDto.getHdnBihinInfoListTableData());
		for (Map<String, Object> bihinMap : noDispBihin) {
			Skf3010MShatakuBihin mShatakuBihin = new Skf3010MShatakuBihin();
			// 備品コード
			mShatakuBihin.setBihinCd(bihinMap.get("bihinCd").toString());
			// 備付区分
			mShatakuBihin.setBihinStatusKbn(bihinMap.get("bihinStatusKbn").toString());
			// 更新日時
			if (bihinMap.get("updateDate") != null && bihinMap.get("updateDate").toString().length() > 0) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
				mShatakuBihin.setLastUpdateDate(dateFormat.parse(bihinMap.get("updateDate").toString()));
			}
			mShatakuBihinList.add(mShatakuBihin);
		}
	}

	/**
	 * 社宅契約情報マスタ更新データ作成
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param mShatakuContract	*契約情報マスタエンティティ
	 * @param comDto			DTO
	 */
	public void setUpdateColumContract(Skf3010MShatakuContract mShatakuContract,
			List<Map<String, Object>> drpDwnSelectedList, Skf3010Sc002CommonDto comDto) {

		Map <String, Object> drpDwnSelectedMap = drpDwnSelectedList.get(0);

		/** 社宅契約情報マスタの更新データを作成 */
		// 経理連携用管理番号設定判定
		if (Objects.equals(comDto.getAssetRegisterNo(), null) || comDto.getAssetRegisterNo().length() <= 0) {
			// 経理連携用管理番号無し＝契約情報なしのため処理なし
			return;
		}
		// 契約番号
		Long contractNo = 1L;
		if (drpDwnSelectedMap.get("contractNo") != null && drpDwnSelectedMap.get("contractNo").toString().length() > 0) {
			contractNo = Long.parseLong(drpDwnSelectedMap.get("contractNo").toString());
		}
		mShatakuContract.setContractPropertyId(contractNo);
		// 賃貸人番号
		mShatakuContract.setOwnerNo(Long.parseLong(comDto.getContractOwnerNo()));
		// 経理連携用管理番号
		mShatakuContract.setAssetRegisterNo(comDto.getAssetRegisterNo());
		// 契約開始日
		mShatakuContract.setContractStartDate(comDto.getContractStartDay().replace("/", ""));
		// 契約終了日
		mShatakuContract.setContractEndDate(comDto.getContractEndDay().replace("/", ""));
		// 家賃
		mShatakuContract.setRent(Integer.parseInt(comDto.getContractRent().replace(",", "")));
		// 共益費
		mShatakuContract.setKyoekihi(Integer.parseInt(comDto.getContractKyoekihi().replace(",", "")));
		// 駐車場料(地代)
		mShatakuContract.setLandRent(Integer.parseInt(comDto.getContractLandRent().replace(",", "")));
		// 備考
		mShatakuContract.setBiko(comDto.getContractBiko());
		// 更新日時
		mShatakuContract.setLastUpdateDate(comDto.getContractUpdateDate());
	}

	/**
	 * 初期化
	 * 「*」項目はアドレスとして戻り値になる
	 * @param comDto	*DTO
	 */
	public void initialize(Skf3010Sc002CommonDto comDto) {
		/** 画面上部 */
		// 空き部屋数
		comDto.setEmptyRoomCount(null);
		// 空き駐車場数
		comDto.setEmptyParkingCount(null);
		// 一棟借上フラグ(trueの場合は一棟借上)
		comDto.setIttoFlg(false);
		// 社宅区分(一棟借上フラグがfalseの場合に参照)
		comDto.setShatakuKbn(null);

		/** テキストボックス */
		/** 基本情報 */
		// 郵便番号
		comDto.setZipCd(null);
		// 都道府県コード
		comDto.setPref(null);
		// 社宅住所(継承元にある)
		comDto.setShatakuAddress(null);
		// 社宅構造詳細
		comDto.setShatakuStructureDetail(null);
		// 建築年月日
		comDto.setBuildDate(null);
		// 実年数
		comDto.setJituAge(null);
		// 次回算定年月日
		comDto.setNextCalcDate(null);
		// 経年
		comDto.setAging(null);
		// 社宅補足ファイル名1
		comDto.setShatakuHosokuFileName1(null);
		// 社宅補足ファイル名2
		comDto.setShatakuHosokuFileName2(null);
		// 社宅補足ファイル名3
		comDto.setShatakuHosokuFileName3(null);
		// 社宅補足リンク名1
		comDto.setShatakuHosokuLink1(null);
		// 社宅補足リンク名2
		comDto.setShatakuHosokuLink2(null);
		// 社宅補足リンク名3
		comDto.setShatakuHosokuLink3(null);
		// 社宅補足サイズ1
		comDto.setShatakuHosokuSize1(null);
		// 社宅補足サイズ2
		comDto.setShatakuHosokuSize2(null);
		// 社宅補足サイズ3
		comDto.setShatakuHosokuSize3(null);
		// 社宅補足ファイル1
		comDto.setShatakuHosokuFile1(null);
		// 社宅補足ファイル2
		comDto.setShatakuHosokuFile2(null);
		// 社宅補足ファイル3
		comDto.setShatakuHosokuFile3(null);
		// 備考
		comDto.setBiko(null);
		// 基本情報更新日時(排他用)
		comDto.setKihonUpdateDate(null);
		/** 駐車場情報 */
		// 駐車場基本使用料
		comDto.setParkingRent(null);
		// 駐車場台数
		comDto.setParkingBlockCount(null);
		// 貸与可能総数
		comDto.setLendPossibleCount(null);
		// 駐車場補足ファイル名1
		comDto.setParkingHosokuFileName1(null);
		// 駐車場補足ファイル名2
		comDto.setParkingHosokuFileName2(null);
		// 駐車場補足ファイル名3
		comDto.setParkingHosokuFileName3(null);
		// 駐車場補足リンク1
		comDto.setParkingHosokuLink1(null);
		// 駐車場補足リンク2
		comDto.setParkingHosokuLink2(null);
		// 駐車場補足リンク3
		comDto.setParkingHosokuLink3(null);
		// 駐車場補足サイズ1
		comDto.setParkingHosokuSize1(null);
		// 駐車場補足サイズ2
		comDto.setParkingHosokuSize2(null);
		// 駐車場補足サイズ3
		comDto.setParkingHosokuSize3(null);
		// 駐車場補足ファイル1
		comDto.setParkingHosokuFile1(null);
		// 駐車場補足ファイル2
		comDto.setParkingHosokuFile2(null);
		// 駐車場補足ファイル3
		comDto.setParkingHosokuFile3(null);
		// 駐車場備考
		comDto.setParkingBiko(null);
		// 駐車場情報更新日時(排他用)
		comDto.setParkingUpdateDate(null);
		/** 管理者情報 */
		/** 寮長・自治会長 */
		// 部屋番号：寮長・自治会長
		comDto.setDormitoryLeaderRoomNo(null);
		// 氏名：寮長・自治会長
		comDto.setDormitoryLeaderName(null);
		// 電子メールアドレス：寮長・自治会長
		comDto.setDormitoryLeaderMailAddress(null);
		// 電話番号：寮長・自治会長
		comDto.setDormitoryLeaderTelNumber(null);
		// 内線番号：寮長・自治会長
		comDto.setDormitoryLeaderExtentionNo(null);
		// 備考：寮長・自治会長
		comDto.setDormitoryLeaderBiko(null);
		// 更新日時(排他用)：寮長・自治会長
		comDto.setDormitoryLeaderUpdateDate(null);
		/** 鍵管理者 */
		// 部屋番号 ：鍵管理者
		comDto.setKeyManagerRoomNo(null);
		// 氏名 ：鍵管理者
		comDto.setKeyManagerName(null);
		// 電子メールアドレス ：鍵管理者
		comDto.setKeyManagerMailAddress(null);
		// 電話番号 ：鍵管理者
		comDto.setKeyManagerTelNumber(null);
		// 内線番号 ：鍵管理者
		comDto.setKeyManagerExtentionNo(null);
		// 備考 ：鍵管理者
		comDto.setKeyManagerBiko(null);
		// 更新日時(排他用)：鍵管理者
		comDto.setKeyManagerUpdateDate(null);
		/** 寮母・管理会社 */
		// 部屋番号：寮母・管理会社
		comDto.setMatronRoomNo(null);
		// 氏名：寮母・管理会社
		comDto.setMatronName(null);
		// 電子メールアドレス：寮母・管理会社
		comDto.setMatronMailAddress(null);
		// 電話番号：寮母・管理会社
		comDto.setMatronTelNumber(null);
		// 内線番号：寮母・管理会社
		comDto.setMatronExtentionNo(null);
		// 備考：寮母・管理会社
		comDto.setMatronBiko(null);
		// 更新日時(排他用)：寮母・管理会社
		comDto.setMatronUpdateDate(null);
		/** 契約情報 */
		// 賃貸人（代理人）名
		comDto.setContractOwnerName(null);
		// 賃貸人（代理人）番号
		comDto.setContractOwnerNo(null);
		// 経理連携用管理番号
		comDto.setAssetRegisterNo(null);
		// 契約開始日
		comDto.setContractStartDay(null);
		// 契約終了日
		comDto.setContractEndDay(null);
		// 家賃
		comDto.setContractRent(null);
		// 共益費
		comDto.setContractKyoekihi(null);
		// 駐車場料（地代）
		comDto.setContractLandRent(null);
		// 備考
		comDto.setContractBiko(null);
		// 契約情報更新日時(排他用)
		comDto.setContractUpdateDate(null);
		// 契約情報表示プルダウンインデックス
		comDto.setHdnDispContractSelectedIndex(null);
		// 契約情報選択プルダウンインデックス
		comDto.setHdnChangeContractSelectedIndex(null);
		// 契約情報削除プルダウンインデックス
		comDto.setHdnDeleteContractSelectedValue(null);

		// 選択タブインデックス
		comDto.setHdnNowSelectTabIndex(null);

		/** 補足ファイル */
		// ファイル番号
		comDto.setFileNo(null);
		// 種別
		comDto.setHosokuType(null);	//補足種別
		//ファイルボックス
		comDto.setTmpFileBoxshataku1(null);
		comDto.setTmpFileBoxshataku2(null);
		comDto.setTmpFileBoxshataku3(null);
		comDto.setTmpFileBoxparking1(null);
		comDto.setTmpFileBoxparking2(null);
		comDto.setTmpFileBoxparking3(null);

		/** 駐車場契約情報への連携用 */
//		comDto.setHdnShatakuKanriNo(null);
		comDto.setHdnShatakuName(null);
		comDto.setHdnAreaKbn(null);
		comDto.setHdnShatakuKbn(null);
		comDto.setHdnEmptyRoomCount(null);
		comDto.setHdnEmptyParkingCount(null);

		/** ドロップダウンリスト */
		// 地域区分リスト
		comDto.setAreaKbnList(null);
		// 地域区分選択値コード
		comDto.setAreaKbnCd(null);
		// 管理事業領域リスト
		comDto.setManageBusinessAreaList(null);
		// 都道府県リスト
		comDto.setPrefList(null);
		// 社宅構造リスト
		comDto.setShatakuStructureList(null);
		// 社宅構造選択値コード
		comDto.setStructureKbnCd(null);
		// エレベーターリスト
		comDto.setElevatorList(null);
		// 駐車場構造リスト
		comDto.setParkingStructureList(null);
		// 契約番号リスト
		comDto.setContractNoList(null);
		// 貸与区分選択値リスト文字列(追加ボタン押下用)
		comDto.setLendKbnSelectListString(null);
		// デフォルト貸与状況(追加ボタン押下用)
		comDto.setDefaultParkingLendStatus(null);

		/** リストテーブル */
		// 駐車場情報リスト(画面表示時に使用するリスト)
		comDto.setParkingInfoListTableData(null);
		// 駐車場情報リスト(初期表示時：DB取得時)
		comDto.setHdnStartingParkingInfoListTableData(null);
		// 駐車場情報リスト(現在画面に表示されているリスト)
		comDto.setNowParkingInfoListTableData(null);
		// 備品情報リスト
		comDto.setBihinInfoListTableData(null);
		// 契約情報リスト
		comDto.setContractInfoListTableData(null);
		// 非表示備品情報リスト
		comDto.setHdnBihinInfoListTableData(null);
		// 社宅部屋情報リスト(排他処理用)
		comDto.setShatakuRoomExlusiveCntrllList(null);
		// 社宅部屋備品情報リスト(排他処理用)
		comDto.setRoomeBihinExlusiveCntrllList(null);

		/** フラグ */
		// 新規保有社宅フラグ(新規：true, 編集：false)
		comDto.setNewShatakuFlg(false);
		// 契約情報追加ボタン(非活性：true, 活性:false)
		comDto.setContractAddDisableFlg(false);
		// 契約情報削除ボタン(非活性：true, 活性:false)
		comDto.setContractDelDisableFlg(false);
		// 駐車場契約情報ボタン(非活性：true, 活性:false)
		comDto.setParkingContractDisableFlg(false);

		/** JSON(連携用) */
		// JSON駐車場区画情報 リスト
		comDto.setJsonParking(null);
		// JSON備品情報 リスト
		comDto.setJsonBihin(null);
		// ドロップダウン選択値リスト
		comDto.setJsonDrpDwnList(null);
		// 可変ラベルリスト
		comDto.setJsonLabelList(null);

		/** データ比較用 */
		// 建築年月日
		comDto.setStartingBuildDate(null);
		// 社宅構造
		comDto.setStartingShatakuStructure(null);
		// 駐車場構造リスト
		comDto.setStartingParkingStructure(null);
		// 地域区分
		comDto.setStartingAreaKbn(null);

		/** エラー系 **/
		// 社宅名
		comDto.setShatakuNameErr(null);
		// 地域区分
		comDto.setAreaKbnErr(null);
		// 社宅区分
		comDto.setShatakuKbnErr(null);
		// 利用区分
		comDto.setUseKbnKbnErr(null);
		// 管理会社
		comDto.setManageCompanyErr(null);
		// 管理機関
		comDto.setManageAgencyErr(null);
		// 管理事業領域
		comDto.setManageBusinessAreaErr(null);
		// 郵便番号
		comDto.setZipCdErr(null);
		// 都道府県
		comDto.setPrefErr(null);
		// 社宅住所
		comDto.setShatakuAddressErr(null);
		// 社宅構造
		comDto.setShatakuStructureErr(null);
		// 建築年月日
		comDto.setBuildDateErr(null);
		// 駐車場構造
		comDto.setParkingStructureErr(null);
		// 寮長・自治会長 メールアドレス
		comDto.setDormitoryLeaderMailAddressErr(null);
		// 鍵管理者 メールアドレス
		comDto.setKeyManagerMailAddressErr(null);
		// 寮母・管理会社 メールアドレス
		comDto.setMatronMailAddressErr(null);
		// 寮長・自治会長 電話番号
		comDto.setDormitoryLeaderTelNumberErr(null);
		// 鍵管理者 電話番号
		comDto.setKeyManagerTelNumberErr(null);
		// 寮母・管理会社 電話番号
		comDto.setMatronTelNumberErr(null);
		// 寮長・自治会長 内線番号
		comDto.setDormitoryLeaderExtentionNoErr(null);
		// 鍵管理者 内線番号
		comDto.setKeyManagerExtentionNoErr(null);
		// 寮母・管理会社 内線番号
		comDto.setMatronExtentionNoErr(null);
		// 賃貸人（代理人）名
		comDto.setContractOwnerNameErr(null);
		// 経理連携用管理番号
		comDto.setAssetRegisterNoErr(null);
		// 契約開始日
		comDto.setContractStartDayErr(null);
		// 契約終了日
		comDto.setContractEndDayErr(null);
		// 家賃
		comDto.setContractRentErr(null);
		// 共益費
		comDto.setContractKyoekihiErr(null);
		// 駐車場料（地代）
		comDto.setContractLandRentErr(null);
	}
}
