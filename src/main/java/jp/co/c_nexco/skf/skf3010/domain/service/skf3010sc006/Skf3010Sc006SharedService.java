/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc006;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetHoyuShatakuInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetMaxShatakuKanriNoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetParkingBlockHistroyCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetRoomBihinExlusiveCntrlExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetShatakuContExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetShatakuContractInfoTableDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetShatakuRoomExlusiveCntrlExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc006.Skf3010Sc006GetBihinInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc006.Skf3010Sc006GetBihinInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc006.Skf3010Sc006GetKihonInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc006.Skf3010Sc006GetNewParkingKanriNoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc006.Skf3010Sc006GetParkingContractInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc006.Skf3010Sc006GetParkingInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc006.Skf3010Sc006GetParkingInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc006.Skf3010Sc006GetShatakuInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc006.Skf3010Sc006GetShatakuManegeInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBaseBusinessLogicUtils.SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBaseBusinessLogicUtils.SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetContractInfoTableDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetMaxParkingKanriNoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetMaxShatakuKanriNoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetRoomBihinExlusiveCntrTableDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetRoomExlusiveCntrTableDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetShatakuCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetShatakuInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetTeijiDataCountForParkingBlockExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc006.Skf3010Sc006GetBihinInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc006.Skf3010Sc006GetKihonInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc006.Skf3010Sc006GetNewParkingKanriNoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc006.Skf3010Sc006GetParkingContractInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc006.Skf3010Sc006GetParkingInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc006.Skf3010Sc006GetShatakuManegeInfoExpRepository;
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
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc002common.Skf3010Sc002CommonDto;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc006common.Skf3010Sc006CommonDto;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc002.Skf3010Sc002RegistDto;
import jp.co.intra_mart.common.platform.log.Logger;

/**
 * Skf3010Sc002SharedService 保有社宅登録内共通クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3010Sc006SharedService {

	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	@Autowired
	private SkfDropDownUtils ddlUtils;
	@Autowired
	private Skf3010Sc006GetBihinInfoExpRepository skf3010Sc006GetBihinInfoExpRepository;
	@Autowired
	private Skf3010Sc006GetKihonInfoExpRepository skf3010Sc006GetKihonInfoExpRepository;
	@Autowired
	private Skf3010Sc006GetShatakuManegeInfoExpRepository skf3010Sc006GetShatakuManegeInfoExpRepository;
	@Autowired
	private Skf3010Sc006GetParkingInfoExpRepository skf3010Sc006GetParkingInfoExpRepository;
	@Autowired
	private Skf3010Sc002GetContractInfoTableDataExpRepository skf3010Sc002GetContractInfoTableDataExpRepository;
	@Autowired
	private Skf3010Sc006GetParkingContractInfoExpRepository skf3010Sc006GetParkingContractInfoExpRepository;
	@Autowired
	private Skf3010Sc002GetRoomExlusiveCntrTableDataExpRepository skf3010Sc002GetRoomExlusiveCntrTableDataExpRepository;
	@Autowired
	private Skf3010Sc002GetRoomBihinExlusiveCntrTableDataExpRepository skf3010Sc002GetRoomBihinExlusiveCntrTableDataExpRepository;
	@Autowired
	private Skf3010Sc002GetTeijiDataCountForParkingBlockExpRepository skf3010Sc002GetTeijiDataCountForParkingBlockExpRepository;
	@Autowired
	private Skf3010Sc006GetNewParkingKanriNoExpRepository skf3010Sc006GetNewParkingKanriNoExpRepository;
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
	/** ロガー。 */
	private static Logger logger = LogUtils.getLogger(SkfFileOutputUtils.class);

	// 契約情報：「：」
	private static final String CONTRACT_NO_SEPARATOR = " ： ";
	// 日付上限
	private static final int DATE_MAX = 99940331;
	// 管理者区分：鍵管理者
	private static final String MANAGE_KBN_KEY_MANAGER = "2";
	// 管理者区分：寮母・管理会社
	private static final String MANAGE_KBN_KANRIKAISYA= "3";
	// 社宅補足リンクプレフィックス
	private static final String SHATAKU_HOSOKU_LINK = "attached_shataku";
	// 駐車場補足プレフィックス
	private static final String PARKING_HOSOKU_LINK = "attached_parking";
	// 備品備付状況デフォルト値(なし)
	private static final String DEFAULT_BIHIN_STATUS = "1";
	// 日付フォーマット
	public static final String DATE_FORMAT = "yyyyMMdd HH:mm:ss.SSS";

	//条件
	private final static String TRUE = "true";
	private final static String FALSE = "false";
	//社宅と一括契約
	public static final String CONTRACT_TYPE_1 = "1";
	//社宅と別契約
	public static final String CONTRACT_TYPE_2 = "2";
	//駐車場構造区分
	public static final String PARKING_NASHI = "5";
	
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
	
//	/**
//	 * 駐車場区画貸与履歴数取得。<br>
//	 * パラメータの社宅管理番号、駐車場管理番号の貸与履歴数をDBより取得する。
//	 * 
//	 * @param shatakuKanriNo 社宅管理番号
//	 */
//	public int getSyatakuParkingHistroyCount(String shatakuKanriNo, String parkingKanriNo) {
//		int parkingBlockLendHistoryCount = 0;
//		if (shatakuKanriNo.length() > 0 && parkingKanriNo.length() > 0) {
//			Skf3010Sc002GetParkingBlockHistroyCountExpParameter param = new Skf3010Sc002GetParkingBlockHistroyCountExpParameter();
//			param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
//			param.setParkingKanriNo(Long.parseLong(parkingKanriNo));
//			parkingBlockLendHistoryCount = skf3010Sc002GetSyatakuParkingHistroyCountExpRepository.getSyatakuParkingHistroyCount(param);
//			param = null;
//		}
//		return parkingBlockLendHistoryCount;
//	}

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
	 * 新しい駐車場管理番号を生成する
	 * @param shatakuKanriNo
	 * @return 新しい駐車場管理番号
	 */
	public Long GetNewParkingKanriNo(String shatakuKanriNo){
		
		List<Skf3010Sc006GetNewParkingKanriNoExp> resultListTableData =
				new ArrayList<Skf3010Sc006GetNewParkingKanriNoExp>();
		Skf3010Sc006GetShatakuInfoExpParameter param = new Skf3010Sc006GetShatakuInfoExpParameter();
		param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
		resultListTableData = skf3010Sc006GetNewParkingKanriNoExpRepository.getNewParkingKanriNo(param);
		
		if(resultListTableData.size() <= 0){
			return (long)1;
		}
		
		return resultListTableData.get(0).getParkingKanriNo();
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
			List<Skf3010Sc006GetKihonInfoExp> getKihonInfoListTableData) {

		// リストテーブルに格納するデータを取得する
		int resultCount = 0;
		List<Skf3010Sc006GetKihonInfoExp> resultListTableData =
						new ArrayList<Skf3010Sc006GetKihonInfoExp>();
		if (shatakuKanriNo.length() > 0) {
			Skf3010Sc006GetShatakuInfoExpParameter param = new Skf3010Sc006GetShatakuInfoExpParameter();
			param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
			resultListTableData = skf3010Sc006GetKihonInfoExpRepository.getKihonInfo(param);
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
			List<Skf3010Sc006GetShatakuManegeInfoExp> getShatakuManageListTableData) {

		// リストテーブルに格納するデータを取得する
		int resultCount = 0;
		List<Skf3010Sc006GetShatakuManegeInfoExp> resultListTableData =
						new ArrayList<Skf3010Sc006GetShatakuManegeInfoExp>();
		if (shatakuKanriNo.length() > 0) {
			Skf3010Sc006GetShatakuInfoExpParameter param = new Skf3010Sc006GetShatakuInfoExpParameter();
			param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
			resultListTableData = skf3010Sc006GetShatakuManegeInfoExpRepository.getShatakuManegeInfo(param);
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
			List<Skf3010Sc006GetParkingInfoExp> getShatakuParkingInfoTableData) {

		// リストテーブルに格納するデータを取得する
		int resultCount = 0;
		List<Skf3010Sc006GetParkingInfoExp> resultListTableData =
						new ArrayList<Skf3010Sc006GetParkingInfoExp>();
		if (shatakuKanriNo.length() > 0) {
			Skf3010Sc006GetParkingInfoExpParameter param = new Skf3010Sc006GetParkingInfoExpParameter();
			param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
			String yearMonth = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu();
			param.setYearMonth(yearMonth);
			resultListTableData = skf3010Sc006GetParkingInfoExpRepository.getParkingInfomation(param);
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
	 * 駐車場契約情報取得。<br>
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
	private int getParkingContractInfo(String shatakuKanriNo, List<Map<String, Object>> listTableData,
			List<Map<String, Object>> contractNoList, String selectedValue, String deletedConstractNo, String selectMode) {

		// 設定用契約番号リスト
		List<Map<String, Object>> resultContractNoList = new ArrayList<Map<String, Object>>();

		// リストテーブルに格納するデータを取得する
		int resultCount = 0;
		List<Skf3010Sc006GetParkingContractInfoExp> resultListTableData =
						new ArrayList<Skf3010Sc006GetParkingContractInfoExp>();
		if (shatakuKanriNo.length() > 0) {
			Skf3010Sc006GetShatakuInfoExpParameter param = new Skf3010Sc006GetShatakuInfoExpParameter();
			param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
			resultListTableData = skf3010Sc006GetParkingContractInfoExpRepository.getParkingContractInfo(param);
			param = null;

			// 取得レコード数を設定
			resultCount = resultListTableData.size();

			// 取得データレコード数判定
			if (resultCount <= 0) {
				// 契約情報変更モード判定
				if (Skf3010Sc006CommonDto.CONTRACT_MODE_ADD_PARKING.equals(selectMode)) {
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
		listTableData.addAll(setParkingContractInfoListData(
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
	private List<Map<String, Object>> setParkingContractInfoListData(
			List<Skf3010Sc006GetParkingContractInfoExp> getContractListTableData,
			List<Map<String, Object>> contractNoList, String selectedValue, String deletedConstractNo, String selectMode) {

		// 契約情報リスト
		List<Map<String, Object>> contractInfoList = new ArrayList<Map<String, Object>>();
		Map<String, Object> contractMap = null;
		// ドロップダウン選択値設定フラグ
		Boolean setSelectedValue = false;
		
		// 契約情報ループ
		for (Skf3010Sc006GetParkingContractInfoExp tmpData : getContractListTableData) {
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
			//　住所
			String parkingContractAddress = "";
			// 郵便番号
			String parkingZipCd = "";
			// 駐車場名
			String parkingName = "";
			// 駐車場料(地代)
			String landRent = "";
			// 契約形態
			String parkingContractType = "";
			// 備考
			String biko = "";

			// 「契約番号」取得
			if (tmpData.getContractPropertyId() != null) {
				contractNo = tmpData.getContractPropertyId().toString();
			}
			if (tmpData.getParkingContractType() != null) {
				parkingContractType = tmpData.getParkingContractType().toString();
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
			// 「郵便番号」取得
			if (tmpData.getParkingZipCd()!= null) {
				parkingZipCd = tmpData.getParkingZipCd().toString();
			}
			// 「住所」取得
			if (tmpData.getParkingAddress() != null) {
				parkingContractAddress = tmpData.getParkingAddress().toString();
			}
			// 「駐車場名」取得
			if (tmpData.getParkingName() != null) {
				parkingName = tmpData.getParkingName().toString();
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
			tmpMap.put("parkingContractType", parkingContractType);
			tmpMap.put("parkingOwnerName", ownerName);
			tmpMap.put("parkingOwnerNo", ownerNo);
			tmpMap.put("parkingAssetRegisterNo", assetRegisterNo);
			tmpMap.put("parkingContractStartDay", contractStartDate);
			tmpMap.put("parkingContractEndDay", contractEndDate);
			tmpMap.put("parkingZipCd", parkingZipCd);
			tmpMap.put("parkingContractAddress", parkingContractAddress);
			tmpMap.put("parkingLandRent", landRent);
			tmpMap.put("parkingName", parkingName);
			tmpMap.put("parkingContractBiko", biko);
			tmpMap.put("updateDate", tmpData.getUpdateDate());
			contractInfoList.add(tmpMap);
			tmpMap = null;
			// 選択値判定
			if (selectedValue != null && contractNo.equals(selectedValue)) {
				// 契約情報変更モード判定
				if (Skf3010Sc006CommonDto.CONTRACT_MODE_CHANGE_PARKING.equals(selectMode)) {
					// 選択値に設定
					contractMap.put("selected", true);
					setSelectedValue = true;
				}
//				else if (Skf3010Sc002CommonDto.CONTRACT_MODE_DEL.equals(selectMode)) {
//					// 削除対象の為、契約情報番号リストに追加しない
//					contractMap = null;
//					continue;
//				}
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
		if (Skf3010Sc006CommonDto.CONTRACT_MODE_ADD_PARKING.equals(selectMode)) {
			// 追加時処理
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			String selectIndex = Integer.toString(contractNoList.size() + 1);
			// 契約番号リストに最大値+1の選択値を設定
			tmpMap.put("value", selectIndex);
			tmpMap.put("label", selectIndex + "：");
			tmpMap.put("selected", true);
			tmpMap.put("parkingContractType", CONTRACT_TYPE_1);
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
	 * @param areaKbnCd	地域区分
	 * @param parkingStructureKbnCd	駐車場構造区分
	 * @return 社宅使用料計算結果
	 * @throws ParseException 
	 */
	private SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp getParikingShiyouryouKeisanJouhou(
			String areaKbnCd, String parkingStructureKbnCd) throws ParseException {

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

		List<Skf3010Sc006GetBihinInfoExp> bihinInfoList =
					new ArrayList<Skf3010Sc006GetBihinInfoExp>();
		bihinInfoList = skf3010Sc006GetBihinInfoExpRepository.getBihinName();

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
	private int setBihinInfo(String shatakuKanriNo,String shatakuRoomKanriNo,
			List<Map<String, Object>> bihinListData,List<Map<String, Object>> hdnBihinStatusList){

		LogUtils.debugByMsg("備品情報取得処理開始");
		LogUtils.debugByMsg("引数から取得した値：" + "社宅管理番号=" + shatakuKanriNo);
		List<Skf3010Sc006GetBihinInfoExp> bihinInfoList =
					new ArrayList<Skf3010Sc006GetBihinInfoExp>();
		int resultCount = 0;
		if(SkfCheckUtils.isNullOrEmpty(shatakuKanriNo) || SkfCheckUtils.isNullOrEmpty(shatakuRoomKanriNo)){
			bihinInfoList = skf3010Sc006GetBihinInfoExpRepository.getBihinName();
		}else{
			Skf3010Sc006GetBihinInfoExpParameter param = new Skf3010Sc006GetBihinInfoExpParameter();
			param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
			param.setShatakuRoomKanriNo(Long.parseLong(shatakuRoomKanriNo));
			
			bihinInfoList = skf3010Sc006GetBihinInfoExpRepository.getBihinInfo(param);
		}

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
	 * 備品リストに出力するリストを取得する。<br>
	 * 
	 * @param originList
	 * @return リストテーブルに出力するリスト
	 */
	private List<Map<String, Object>> getBihinListColumn(List<Skf3010Sc006GetBihinInfoExp> originList) {

		List<Map<String, Object>> setViewList = new ArrayList<Map<String, Object>>();

		List<Map<String, Object>> statusList = ddlUtils.getGenericForDoropDownList(
							FunctionIdConstant.GENERIC_CODE_BIHINSTATUS_KBN, "",false);

		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

		for (int i = 0; i < originList.size(); i++) {
			/** DTO設定用 */
			// 備品コード
			String bihinCode = "";
			// 備品名
			String bihinName = "";
			// 備付状況選択値
			String bihinStatus = DEFAULT_BIHIN_STATUS;

			Skf3010Sc006GetBihinInfoExp tmpData = originList.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();

			if(!tmpData.getDispFlg().equals("0")){
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
				if (tmpData.getBihinStatusKbn() != null) {
					bihinStatus = tmpData.getBihinStatusKbn();
				}

				// 備付状況
				String statusListCode = createStatusSelect(bihinStatus,statusList);
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
				List<Skf3010Sc006GetBihinInfoExp> originList) {

		List<Map<String, Object>> setViewList = new ArrayList<Map<String, Object>>();

		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

		for (int i = 0; i < originList.size(); i++) {
			/** DTO設定用 */
			// 備品コード
			String bihinCode = "";
			// 備品名
			String bihinName = "";
			// 備付状況選択値
			String bihinStatus = DEFAULT_BIHIN_STATUS;

			Skf3010Sc006GetBihinInfoExp tmpData = originList.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			// 表示/非表示判定
			if(tmpData.getDispFlg().equals("0")){
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
				if (tmpData.getBihinStatusKbn() != null) {
					bihinStatus = tmpData.getBihinStatusKbn();
				}
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
	 * 部屋情報ドロップダウンリストに設定するリストを取得する.
	 * 
	 * @param originalKikaku 本来規格
	 * @param originalKikakuList
	 * @param originalAuse　本来用途
	 * @param auseList
	 * @param lendKbn　貸与区分
	 * @param lendList
	 * @param coldExemptionKbn　寒冷地減免事由区分
	 * @param coldExemptionKbnList
	 */
	public void getRoomDropDownList(String originalKikaku,List<Map<String, Object>> kikakuList,
			String originalAuse, List<Map<String, Object>> auseList, 
			String lendKbn,	List<Map<String, Object>> lendList,
			String coldExemptionKbn,List<Map<String, Object>> coldExemptionKbnList) {

		boolean isFirstRowEmpty = true;

		//本来規格リスト
		kikakuList.clear();
		kikakuList.addAll(ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_KIKAKU_KBN, originalKikaku,
				isFirstRowEmpty));
		
		// 本来用途リスト
		auseList.clear();
		auseList.addAll(ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_AUSE_KBN, originalAuse,
				isFirstRowEmpty));

		// 貸与区分リスト
		lendList.clear();
		lendList.addAll(ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_LEND_KBN, lendKbn,
				isFirstRowEmpty));
		
		// 寒冷地減免事由区分リスト
		coldExemptionKbnList.clear();
		coldExemptionKbnList.addAll(ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_KANREITI_KBN, coldExemptionKbn,
						false));	

	}

	/**
	 * 駐車場基本使用料の設定
	 * @param areaKbnCd
	 * @param parkingStructureKbnCd
	 * @return
	 * @throws ParseException 
	 */
	public Long setParkingRentalValue(String areaKbnCd, String parkingStructureKbnCd) throws ParseException{
		
		Long parkingRentalValue = 0L;
		
		// 地域区分、駐車場構造区分をパラメータ指定
		SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp retEntity =
				getParikingShiyouryouKeisanJouhou(areaKbnCd, parkingStructureKbnCd);
		// 駐車場使用料設定
		if (retEntity.getChushajouShiyoryou() != null) {
			parkingRentalValue = retEntity.getChushajouShiyoryou().longValue();
		}
		LogUtils.debugByMsg("駐車場基本使用料：" + parkingRentalValue);
		return parkingRentalValue;
	}
	
	/**駐車場月額使用料の設定
	 * @param parkingRental 駐車場基本使用料
	 * @param parkingRentalAdjust 駐車場調整金額
	 * @return
	 */
	public Long setParkingMonthFei(String parkingRental,String parkingRentalAdjust){
		
		Long parkingShiyoMonthFeiValue = 0L;
		
		BigDecimal rental = BigDecimal.ZERO;
		BigDecimal rentalAdjust = BigDecimal.ZERO;
		
		if(!SkfCheckUtils.isNullOrEmpty(parkingRental)){
			try{
				rental = new BigDecimal(parkingRental.replace(",", ""));
			}catch(NumberFormatException ex){
				//駐車場基本使用料に数値以外が入力された場合0を設定する
				LogUtils.debugByMsg("駐車場基本使用料変換NG：" + parkingRental);
				rental = BigDecimal.ZERO;
			}
		}
		
		if(!SkfCheckUtils.isNullOrEmpty(parkingRentalAdjust)){
			try{
				rentalAdjust = new BigDecimal(parkingRentalAdjust.replace(",", ""));
			}catch(NumberFormatException ex){
				//調整金額に数値以外が入力された場合0を設定する
				LogUtils.debugByMsg("駐車場調整金額変換NG：" + parkingRental);
				rentalAdjust = BigDecimal.ZERO;
			}
		}
		
		//'駐車場月額使用料を計算する。
        BigDecimal shiyoMonthFeiValue = rental.add(rentalAdjust);
        
        parkingShiyoMonthFeiValue = shiyoMonthFeiValue.longValue();
		
		return parkingShiyoMonthFeiValue;
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
//	public void parkingCalc(String buildDate, String structureKbnCd,
//					String areaKbnCd, String parkingStructure, List <String> parkingRentalAdjusts,
//					List <String> parkingBlockRentManys, StringBuilder parkingRentalValue) throws ParseException {
//
//		// 駐車場基本使用料
//		Long parkingRentalBasicValue = 0L;
//		// 駐車場基本使用料計算情報取得
//		// 建築年月日、構造区分、地域区分、駐車場構造区分をパラメータ指定
//		SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp retEntity =
//				getChyuushajouryouShiyouGaku(buildDate, structureKbnCd, areaKbnCd, parkingStructure);
//		// 駐車場基本使用料設定
//		if (retEntity.getChushajouShiyoryou() != null) {
//			parkingRentalBasicValue = retEntity.getChushajouShiyoryou().longValue();
//		}
//		// 駐車場基本料金が変更となるので、駐車場区分全体に対して再計算処理を行う。
//		calcParkingBlockListRentMany(parkingRentalBasicValue, parkingRentalAdjusts, parkingBlockRentManys);
//		// 駐車場基本使用料設定
//		parkingRentalValue.setLength(0);
//		parkingRentalValue.append(String.format("%,d", parkingRentalBasicValue));
//	}

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
	private void setContractInfoShinki(String selectMode, Skf3010Sc006CommonDto initDto) {

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
		
		//駐車場
		// 契約情報リスト
		List<Map<String, Object>> parkinglistTableData = new ArrayList<Map<String, Object>>();
		// 契約番号リスト(ドロップダウン)
		List<Map<String, Object>> parkingContractNoList = new ArrayList<Map<String, Object>>();
		//契約形態リスト
		List<Map<String, Object>> parkingContractTypeList = new ArrayList<Map<String, Object>>();
		/** 契約情報設定 */
		initDto.setParkingContractInfoListTableData(parkinglistTableData);
		initDto.setParkingContractNoList(parkingContractNoList);
		initDto.setParkingOwnerName(CodeConstant.DOUBLE_QUOTATION);
		initDto.setParkingOwnerNo(null);
		initDto.setParkingAssetRegisterNo(CodeConstant.DOUBLE_QUOTATION);
		initDto.setParkingContractStartDay(CodeConstant.DOUBLE_QUOTATION);
		initDto.setParkingContractEndDay(CodeConstant.DOUBLE_QUOTATION);
		initDto.setParkingZipCd(CodeConstant.DOUBLE_QUOTATION);
		initDto.setParkingContractAddress(CodeConstant.DOUBLE_QUOTATION);
		initDto.setParkingName(CodeConstant.DOUBLE_QUOTATION);
		initDto.setParkingLandRent(CodeConstant.DOUBLE_QUOTATION);
		initDto.setParkingContractBiko(CodeConstant.DOUBLE_QUOTATION);
		initDto.setParkingContractAddDisableFlg(contractAddDisableFlg);
		initDto.setParkingContractDelDisableFlg(contractDelDisableFlg);
		initDto.setParkingContractTypeList(parkingContractTypeList);
		setKeiyakuInfoSession(initDto);
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
	 * @throws ParseException 
	 */
	private void setContractInfo(String shatakuKanriNo, String contractSelectedIndex,
					String deletedConstractNo, String selectMode, Skf3010Sc006CommonDto initDto) throws ParseException {

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
		
		if (Skf3010Sc006CommonDto.CONTRACT_MODE_ADD_PARKING.equals(selectMode)
				|| Skf3010Sc006CommonDto.CONTRACT_MODE_CHANGE_PARKING.equals(selectMode)
				|| Skf3010Sc006CommonDto.CONTRACT_MODE_DEL_PARKING.equals(selectMode)) {
			//駐車場契約情報変更の場合、何もしない
			return;
		}
		
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
		if (contractNoList.size() > 0) {
			for (Map<String, Object> contractDataMap : listTableData) {
				if (contractDataMap.get("contractNo").equals(selectIndex)) {
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
//		initDto.setSelectMode(null);
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
	 * 駐車場契約情報の活性制御
	 * @param dto
	 */
	private void setKeiyakuInfoSession(Skf3010Sc006CommonDto dto){
//        ''駐車場情報を活性に変更
		String parkingStructureKbn = dto.getParkingStructure();
		if(SkfCheckUtils.isNullOrEmpty(parkingStructureKbn) || PARKING_NASHI.equals(parkingStructureKbn)){
			//駐車場構造区分が“なし”・“空白”の場合、契約情報は非活性とする
			dto.setParkingContractAddDisableFlg(true);
			dto.setParkingContractDelDisableFlg(true);
			dto.setParkingContractTypeDisabled(TRUE);
			dto.setParkingContractInfoDisabled(TRUE);
		}else{
			dto.setParkingContractAddDisableFlg(false);
			dto.setParkingContractDelDisableFlg(true);
			dto.setParkingContractTypeDisabled(TRUE);
			dto.setParkingContractInfoDisabled(TRUE);
		}

	}
	/**
	 * 契約情報（駐車場）をセットする。
	 * @param shatakuKanriNo
	 * @param contractSelectedIndex
	 * @param deletedConstractNo
	 * @param selectMode
	 * @param initDto
	 */
	private void setParkingContractInfo(String shatakuKanriNo, String contractSelectedIndex,
			String deletedConstractNo, String selectMode, Skf3010Sc006CommonDto initDto){
		
		// 契約情報リスト
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
		// 契約番号リスト(ドロップダウン)
		List<Map<String, Object>> contractNoList = new ArrayList<Map<String, Object>>();
		//契約形態リスト
		List<Map<String, Object>> parkingContractTypeList = new ArrayList<Map<String, Object>>();
		/** DTO設定値 */
		String parkingContractType = CodeConstant.DOUBLE_QUOTATION;
		// 賃貸人（代理人）名
		String parkingOwnerName = CodeConstant.DOUBLE_QUOTATION;
		// 賃貸人（代理人）番号
		String parkingOwnerNo = CodeConstant.DOUBLE_QUOTATION;
		// 経理連携用管理番号
		String parkingAssetRegisterNo = CodeConstant.DOUBLE_QUOTATION;
		// 契約開始日
		String parkingContractStartDay = CodeConstant.DOUBLE_QUOTATION;
		// 契約終了日
		String parkingContractEndDay = CodeConstant.DOUBLE_QUOTATION;
		// 郵便番号
		String parkingZipCd = CodeConstant.DOUBLE_QUOTATION;
		// 住所
		String parkingContractAddress = CodeConstant.DOUBLE_QUOTATION;
		// 駐車場名
		String parkingName = CodeConstant.DOUBLE_QUOTATION;
		// 駐車場料(地代)
		String parkingLandRent = CodeConstant.DOUBLE_QUOTATION;
		// 備考
		String parkingContractBiko = CodeConstant.DOUBLE_QUOTATION;
		// 契約情報追加ボタン(非活性：true, 活性:false)
		Boolean contractAddDisableFlg = true;
		// 契約情報削除ボタン(非活性：true, 活性:false)
		Boolean contractDelDisableFlg = true;
		// 選択値インデックス
		String selectIndex = contractSelectedIndex;
		
		if (Skf3010Sc006CommonDto.CONTRACT_MODE_ADD.equals(selectMode)
				|| Skf3010Sc006CommonDto.CONTRACT_MODE_CHANGE.equals(selectMode)
				|| Skf3010Sc006CommonDto.CONTRACT_MODE_DEL.equals(selectMode)) {
			//社宅契約情報変更の場合、何もしない
			return;
		}
		
		//データ取得
		getParkingContractInfo(shatakuKanriNo, listTableData, contractNoList, selectIndex, deletedConstractNo, selectMode);
		
		if(contractNoList.size() <= 0){
			setKeiyakuInfoSession(initDto);	
			String parkingStructureKbn = initDto.getParkingStructure();
			if(SkfCheckUtils.isNullOrEmpty(parkingStructureKbn) || PARKING_NASHI.equals(parkingStructureKbn)){
				//駐車場構造区分が“なし”・“空白”の場合、契約情報は非活性とする
				contractAddDisableFlg = true;
				contractDelDisableFlg = true;
				initDto.setParkingContractTypeDisabled(TRUE);
				initDto.setParkingContractInfoDisabled(TRUE);
			}else{
				contractAddDisableFlg = false;
				contractDelDisableFlg = true;
				initDto.setParkingContractTypeDisabled(TRUE);
				initDto.setParkingContractInfoDisabled(TRUE);
			}
		}else{
			initDto.setParkingContractTypeDisabled(FALSE);
			// 選択契約情報
			Map<String, Object> contractMap = new HashMap<String, Object>();
			// 契約情報変更モード判定
			if (Skf3010Sc006CommonDto.CONTRACT_MODE_CHANGE_PARKING.equals(selectMode)
					|| Skf3010Sc006CommonDto.CONTRACT_MODE_DEL_PARKING.equals(selectMode)) {
				// 変更、削除の場合
				// 追加ボタン活性
				contractAddDisableFlg = false;
			} else if (Skf3010Sc006CommonDto.CONTRACT_MODE_ADD_PARKING.equals(selectMode)) {
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
			
			//契約形態
			// 契約形態リスト
			if (contractMap.get("parkingContractType") != null) {
				parkingContractType = contractMap.get("parkingContractType").toString();
			}
			
			parkingContractTypeList.clear();
			parkingContractTypeList.addAll(ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_PARKING_CONTRACTTYPE_KBN, parkingContractType,
					true));
			
			if(CONTRACT_TYPE_2.equals(parkingContractType)){
				//入力可に設定する
				initDto.setParkingContractInfoDisabled(FALSE);
				
				// 「賃貸人(代理人)氏名または名称」取得
				if (contractMap.get("ownerName") != null) {
					parkingOwnerName = contractMap.get("ownerName").toString();
				}
				// 「賃貸人(代理人)番号」取得
				if (contractMap.get("ownerNo") != null) {
					parkingOwnerNo = contractMap.get("ownerNo").toString();
				}
				// 「経理連携用管理番号」取得
				if (contractMap.get("assetRegisterNo") != null) {
					parkingAssetRegisterNo = contractMap.get("assetRegisterNo").toString();
				}
				// 「契約開始日」取得
				if (contractMap.get("contractStartDate") != null) {
					parkingContractStartDay = contractMap.get("contractStartDate").toString();
				}
				// 「契約終了日」取得
				if (contractMap.get("contractEndDate") != null) {
					parkingContractEndDay = contractMap.get("contractEndDate").toString();
				}
				// 「郵便番号」取得
				if (contractMap.get("parkingZipCd") != null) {
					parkingZipCd = contractMap.get("parkingZipCd").toString();
				}
				// 「住所」取得
				if (contractMap.get("parkingContractAddress") != null) {
					parkingContractAddress = contractMap.get("parkingContractAddress").toString();
				}
				// 「駐車場名」取得
				if (contractMap.get("parkingName") != null) {
					parkingName = contractMap.get("parkingName").toString();
				}
				// 「駐車場料(地代)」取得
				if (contractMap.get("landRent") != null) {
					parkingLandRent = contractMap.get("landRent").toString();
				}
				// 「備考」取得
				if (contractMap.get("biko") != null) {
					parkingContractBiko = contractMap.get("biko").toString();
				}
			}else{
				initDto.setParkingContractInfoDisabled(TRUE);
			}

			contractMap = null;
		}
		


		/** 契約情報設定 */
		initDto.setParkingContractInfoListTableData(listTableData);
		initDto.setParkingContractNoList(contractNoList);
		initDto.setParkingOwnerName(parkingOwnerName);
		initDto.setParkingOwnerNo(parkingOwnerNo);
		initDto.setParkingAssetRegisterNo(parkingAssetRegisterNo);
		initDto.setParkingContractStartDay(parkingContractStartDay);
		initDto.setParkingContractEndDay(parkingContractEndDay);
		initDto.setParkingZipCd(parkingZipCd);
		initDto.setParkingContractAddress(parkingContractAddress);
		initDto.setParkingName(parkingName);
		initDto.setParkingLandRent(parkingLandRent);
		initDto.setParkingContractBiko(parkingContractBiko);
		initDto.setParkingContractAddDisableFlg(contractAddDisableFlg);
		initDto.setParkingContractDelDisableFlg(contractDelDisableFlg);
		initDto.setHdnDispParkingContractSelectedIndex(selectIndex);
		initDto.setParkingContractTypeList(parkingContractTypeList);
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
	private void setShatakuManageInfoShinki(Skf3010Sc006CommonDto initDto) {

		// 鍵管理者
		initDto.setKeyManagerCompanyName(CodeConstant.DOUBLE_QUOTATION);
		initDto.setKeyManagerName(CodeConstant.DOUBLE_QUOTATION);
		initDto.setKeyManagerMailAddress(CodeConstant.DOUBLE_QUOTATION);
		initDto.setKeyManagerTelNumber(CodeConstant.DOUBLE_QUOTATION);
		initDto.setKeyManagerBiko(CodeConstant.DOUBLE_QUOTATION);
		// 管理会社
		initDto.setManageCompanyName(CodeConstant.DOUBLE_QUOTATION);
		initDto.setManageName(CodeConstant.DOUBLE_QUOTATION);
		initDto.setManageMailAddress(CodeConstant.DOUBLE_QUOTATION);
		initDto.setManageTelNumber(CodeConstant.DOUBLE_QUOTATION);
		initDto.setManageBiko(CodeConstant.DOUBLE_QUOTATION);
	}

	/**
	 * 社宅管理者情報設定
	 * 「※」項目はアドレスとして戻り値になる。
	 * @param shatakuKanriNo	社宅管理番号
	 * @param initDto			*DTO
	 */
	private void setShatakuManageInfo(String shatakuKanriNo, Skf3010Sc006CommonDto initDto) {

		// 駐車場情報リストデータ(DBから取得したデータ保存用)
		List<Skf3010Sc006GetShatakuManegeInfoExp> getShatakuManageListTableData = 
									new ArrayList<Skf3010Sc006GetShatakuManegeInfoExp>();

		// 社宅管理者情報をDBより取得する
		getShatakuManageInfo(shatakuKanriNo, getShatakuManageListTableData);
		// 管理者情報ループ
		for(Skf3010Sc006GetShatakuManegeInfoExp tmpData : getShatakuManageListTableData){
			/** DTO設定値 */
			// 部屋番号
			String shatakuNo = "";
			// 管理者氏名
			String name = "";
			// メールアドレス
			String mailAddress = "";
			// 電話番号
			String telNo = "";
			// 備考
			String biko = "";

			// 会社名
			if (tmpData.getManageShatakuNo() != null) {
				shatakuNo = tmpData.getManageShatakuNo();
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
			// 備考
			if (tmpData.getBiko() != null) {
				biko = tmpData.getBiko();
			}
			/** 管理者区分判定 */
			//鍵管理者
			if (MANAGE_KBN_KEY_MANAGER.equals(tmpData.getManageKbn())) {
				// 会社名
				initDto.setKeyManagerCompanyName(null);
				initDto.setKeyManagerCompanyName(shatakuNo);
				// 担当者名
				initDto.setKeyManagerName(null);
				initDto.setKeyManagerName(name);
				// メールアドレス
				initDto.setKeyManagerMailAddress(null);
				initDto.setKeyManagerMailAddress(mailAddress);
				// 電話番号
				initDto.setKeyManagerTelNumber(null);
				initDto.setKeyManagerTelNumber(telNo);
				// 備考
				initDto.setKeyManagerBiko(null);
				initDto.setKeyManagerBiko(biko);
				// 更新日時(排他用)
				initDto.setKeyManagerUpdateDate(null);
				initDto.setKeyManagerUpdateDate(tmpData.getUpdateDate());
			} else if (MANAGE_KBN_KANRIKAISYA.equals(tmpData.getManageKbn())) {
				// 部屋番号
				initDto.setManageCompanyName(null);
				initDto.setManageCompanyName(shatakuNo);
				// 管理者氏名
				initDto.setManageName(null);
				initDto.setManageName(name);
				// メールアドレス
				initDto.setManageMailAddress(null);
				initDto.setManageMailAddress(mailAddress);
				// 電話番号
				initDto.setManageTelNumber(null);
				initDto.setManageTelNumber(telNo);
				// 備考
				initDto.setManageBiko(null);
				initDto.setManageBiko(biko);
				// 更新日時(排他用)
				initDto.setManageUpdateDate(null);
				initDto.setManageUpdateDate(tmpData.getUpdateDate());
			}
		}
	}

	/**
	 * 駐車場情報タブをセットします。(新規)<br>
	 * 「※」項目はアドレスとして戻り値になる。
	 * 新規用
	 * @param initDto	*DTO
	 */
	private void setShatakuParkingInfoShinki(Skf3010Sc006CommonDto initDto) {

		// 駐車場構造プルダウンリスト
		List<Map<String, Object>> parkingStructureList = new ArrayList<Map<String, Object>>();
		parkingStructureList.clear();
		parkingStructureList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_PARKING_STRUCTURE_KBN, "", true));
		initDto.setParkingStructureList(null);
		initDto.setParkingStructureList(parkingStructureList);
		
		//駐車場所在地
		initDto.setParkingAddress(CodeConstant.DOUBLE_QUOTATION);

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
		
		//駐車場管理番号
		initDto.setParkingKanriNo(null);
		//区画番号
		initDto.setParkingBlock(CodeConstant.DOUBLE_QUOTATION);
		//貸与区分
		initDto.setParkingLendKbn(CodeConstant.DOUBLE_QUOTATION);
		//使用者
		initDto.setShiyosya(CodeConstant.DOUBLE_QUOTATION);
		//駐車場調整金額
		initDto.setParkingRentalAdjust(CodeConstant.STRING_ZERO);
		//駐車場月額使用料
		initDto.setParkingShiyoMonthFei(CodeConstant.STRING_ZERO);
		// 駐車場備考
		initDto.setParkingBiko(CodeConstant.DOUBLE_QUOTATION);
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
	private void setParkingInfo(String shatakuKanriNo, Skf3010Sc006CommonDto initDto) throws ParseException {

		// 駐車場構造プルダウンリスト
		List<Map<String, Object>> parkingStructureList = new ArrayList<Map<String, Object>>();
		/** DTO設定値 */
		// 駐車場構造区分
		String parkingStructure = "";
		// 駐車場基本使用料
		Long parkingRentalValue = 0L;
		// 駐車場調整金額
		Long parkingRentalAdjustValue = 0L;
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
		String parkingBiko = CodeConstant.DOUBLE_QUOTATION;
		// 区画番号
		String parkingBlock = CodeConstant.DOUBLE_QUOTATION;
		// 駐車場管理番号
		String parkingKanriNo = CodeConstant.DOUBLE_QUOTATION;

		// 駐車場情報リストデータ(DBから取得したデータ保存用)
		List<Skf3010Sc006GetParkingInfoExp> getShatakuParkingInfoTableData = 
									new ArrayList<Skf3010Sc006GetParkingInfoExp>();
		// 駐車場情報をDBより取得する
		getShatakuParkingInfo(shatakuKanriNo, getShatakuParkingInfoTableData);
		
		if(getShatakuParkingInfoTableData.size() <= 0){
			ServiceHelper.addResultMessage(initDto, null, MessageIdConstant.E_SKF_1078,"駐車場情報初期化");
			return;
		}
		
		// 駐車場情報
		for(Skf3010Sc006GetParkingInfoExp tmpData : getShatakuParkingInfoTableData){
			// 「駐車場構造」設定
			if (tmpData.getParkingStructureKbn() != null) {
				parkingStructure = tmpData.getParkingStructureKbn();
			}
			parkingStructureList.clear();
			parkingStructureList.addAll(ddlUtils.getGenericForDoropDownList(
					FunctionIdConstant.GENERIC_CODE_PARKING_STRUCTURE_KBN, parkingStructure, true));
			initDto.setParkingStructureList(null);
			initDto.setParkingStructureList(parkingStructureList);
			initDto.setParkingStructure(parkingStructure);

			// 駐車場基本使用料
			if (tmpData.getParkingRental() != null) {
				parkingRentalValue = tmpData.getParkingRental();
			} else {
				// 駐車場基本使用料計算情報取得
				//SetParkingRentalValue
				// 地域区分、駐車場構造区分をパラメータ指定
				SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp retEntity =
						getParikingShiyouryouKeisanJouhou(initDto.getAreaKbnCd(), parkingStructure);
				// 駐車場使用料設定
				if (retEntity.getChushajouShiyoryou() != null) {
					parkingRentalValue = retEntity.getChushajouShiyoryou().longValue();
				}
			}
			initDto.setParkingRent(null);
			initDto.setParkingRent(String.format("%,d", parkingRentalValue));
			
			//駐車場所在地
			initDto.setParkingAddress(tmpData.getParkingAddress());

			if(FALSE.equals(initDto.getCopyFlg())){
				//複写で無い場合設定する
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
				
				//区画番号
				parkingBlock = tmpData.getParkingBlock();
				
				//駐車場管理番号
				if(tmpData.getParkingKanriNo() != null){
					parkingKanriNo = String.valueOf(tmpData.getParkingKanriNo());
				}else{
					long newNo = GetNewParkingKanriNo(shatakuKanriNo);
					parkingKanriNo = String.valueOf(newNo);
				}
			}
			
			// 駐車場補足1
			initDto.setParkingHosokuFileName1(null);
			initDto.setParkingHosokuFileName1(parkingSupplementName1);
			initDto.setParkingHosokuSize1(null);
			initDto.setParkingHosokuSize1(parkingSupplementSize1);
			initDto.setParkingHosokuFile1(null);
			initDto.setParkingHosokuFile1(parkingSupplementFile1);

			// 駐車場補足2
			initDto.setParkingHosokuFileName2(null);
			initDto.setParkingHosokuFileName2(parkingSupplementName2);
			initDto.setParkingHosokuSize2(null);
			initDto.setParkingHosokuSize2(parkingSupplementSize2);
			initDto.setParkingHosokuFile2(null);
			initDto.setParkingHosokuFile2(parkingSupplementFile2);
			
			// 駐車場補足3
			initDto.setParkingHosokuFileName3(null);
			initDto.setParkingHosokuFileName3(parkingSupplementName3);
			initDto.setParkingHosokuSize3(null);
			initDto.setParkingHosokuSize3(parkingSupplementSize3);
			initDto.setParkingHosokuFile3(null);
			initDto.setParkingHosokuFile3(parkingSupplementFile3);
			
			//区画番号
			initDto.setParkingBlock(parkingBlock);

			//駐車場管理番号
			initDto.setParkingKanriNo(parkingKanriNo);
			
			
			//貸与区分
			String lendKbn = CodeConstant.DOUBLE_QUOTATION;
			if(!PARKING_NASHI.equals(parkingStructure)){
				//構造区分が「なし」以外
				//契約形態コード取得
				Map<String, String> genericCodeMapParkinglendKbn = new HashMap<String, String>();
				genericCodeMapParkinglendKbn = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_LEND_KBN);
				
				if (initDto.getLendKbn() != null) {
					lendKbn = genericCodeMapParkinglendKbn.get(initDto.getLendKbn());
				}
			}
			initDto.setParkingLendKbn(lendKbn);
			
			//使用者
			initDto.setShiyosya(tmpData.getShainName());
			
			//駐車場調整金額
			if(tmpData.getParkingRentalAdjust() != null){
				parkingRentalAdjustValue = tmpData.getParkingRentalAdjust();
			}
			initDto.setParkingRentalAdjust(String.valueOf(parkingRentalAdjustValue));
			
			//駐車場月額使用料
			Long parkingShiyoMonthFei = parkingRentalValue + parkingRentalAdjustValue;
			initDto.setParkingShiyoMonthFei(String.format("%,d", parkingShiyoMonthFei));
			
			// 駐車場備考
			if (tmpData.getParkingBiko() != null) {
				parkingBiko = tmpData.getParkingBiko();
			}
			initDto.setParkingBiko(null);
			initDto.setParkingBiko(parkingBiko);

			// 更新日（排他処理用）
			initDto.setParkingUpdateDate(null);
			initDto.setParkingUpdateDate(tmpData.getUpdateDate());
			
			initDto.setParkUpdateDate(tmpData.getParkingUpdateDate());
			initDto.setBlockUpdateDate(tmpData.getBlockUpdateDate());
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
	private void setKihonInfoShinki(Skf3010Sc006CommonDto initDto) {

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
		//部屋情報リスト
		// ' 「本来用途」ドロップダウンリストの設定
		List<Map<String, Object>> originalAuseList = new ArrayList<Map<String, Object>>();
		// ' 「貸与区分」ドロップダウンリストの設定
		List<Map<String, Object>> lendKbnList = new ArrayList<Map<String, Object>>();
		// ' 「本来規格」ドロップダウンリストの設定
		List<Map<String, Object>> originalKikakuList = new ArrayList<Map<String, Object>>();
		// ' 「寒冷地減免事由区分」ドロップダウンリストの設定
		List<Map<String, Object>> coldExemptionKbnList = new ArrayList<Map<String, Object>>();
	
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
		
		//部屋情報
		//部屋管理番号
		initDto.setShatakuRoomKanriNo(null);
		//部屋番号
		initDto.setRoomNo(CodeConstant.DOUBLE_QUOTATION);
		//本来延面積
		initDto.setOriginalMenseki(CodeConstant.DOUBLE_QUOTATION);
		//階段面積
		initDto.setStairsMenseki(CodeConstant.DOUBLE_QUOTATION);
		//本来規格
		initDto.setOriginalKikaku(CodeConstant.DOUBLE_QUOTATION);
		//貸与面積
		initDto.setLendMenseki(CodeConstant.DOUBLE_QUOTATION);
		//本来用途
		initDto.setOriginalAuse(CodeConstant.DOUBLE_QUOTATION);
		//サンルーム面積
		initDto.setSunRoomMenseki(CodeConstant.DOUBLE_QUOTATION);
		//物置調整面積
		initDto.setBarnMensekiAdjust(CodeConstant.DOUBLE_QUOTATION);
		//貸与区分
		initDto.setLendKbn(CodeConstant.DOUBLE_QUOTATION);
		//貸与区分補足
		initDto.setLendKbnHosoku(CodeConstant.DOUBLE_QUOTATION);
		//寒冷地減免事由区分
		initDto.setColdExemptionKbn(CodeConstant.DOUBLE_QUOTATION);
		//備考
		initDto.setRoomBiko(CodeConstant.DOUBLE_QUOTATION);
		
		//ドロップダウンリストの設定
		getRoomDropDownList("", originalKikakuList, 
				"", originalAuseList,
				"", lendKbnList, 
				"0", coldExemptionKbnList);
		initDto.setOriginalKikakuList(originalKikakuList);
		initDto.setOriginalAuseList(originalAuseList);
		initDto.setLendKbnList(lendKbnList);
		initDto.setColdExemptionKbnList(coldExemptionKbnList);
		
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
	 * 部屋情報タブをセットします。(新規)
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param shatakuKanriNo	社宅管理番号
	 * @param areaKbnCd			地域区分コード
	 * @param initDto			*DTO
	 * @throws ParseException
	 */
	private void setRoomInfoShinki(Skf3010Sc006CommonDto initDto) {

		//部屋情報リスト
		// ' 「本来用途」ドロップダウンリストの設定
		List<Map<String, Object>> originalAuseList = new ArrayList<Map<String, Object>>();
		// ' 「貸与区分」ドロップダウンリストの設定
		List<Map<String, Object>> lendKbnList = new ArrayList<Map<String, Object>>();
		// ' 「本来規格」ドロップダウンリストの設定
		List<Map<String, Object>> originalKikakuList = new ArrayList<Map<String, Object>>();
		// ' 「寒冷地減免事由区分」ドロップダウンリストの設定
		List<Map<String, Object>> coldExemptionKbnList = new ArrayList<Map<String, Object>>();
	
		
		//部屋情報
		//部屋管理番号
		initDto.setShatakuRoomKanriNo(null);
		//部屋番号
		initDto.setRoomNo(CodeConstant.DOUBLE_QUOTATION);
		//本来延面積
		initDto.setOriginalMenseki(CodeConstant.DOUBLE_QUOTATION);
		//階段面積
		initDto.setStairsMenseki(CodeConstant.DOUBLE_QUOTATION);
		//本来規格
		initDto.setOriginalKikaku(CodeConstant.DOUBLE_QUOTATION);
		//貸与面積
		initDto.setLendMenseki(CodeConstant.DOUBLE_QUOTATION);
		//本来用途
		initDto.setOriginalAuse(CodeConstant.DOUBLE_QUOTATION);
		//サンルーム面積
		initDto.setSunRoomMenseki(CodeConstant.DOUBLE_QUOTATION);
		//物置調整面積
		initDto.setBarnMensekiAdjust(CodeConstant.DOUBLE_QUOTATION);
		//貸与区分
		initDto.setLendKbn(CodeConstant.DOUBLE_QUOTATION);
		//貸与区分補足
		initDto.setLendKbnHosoku(CodeConstant.DOUBLE_QUOTATION);
		//寒冷地減免事由区分
		initDto.setColdExemptionKbn(CodeConstant.DOUBLE_QUOTATION);
		//備考
		initDto.setRoomBiko(CodeConstant.DOUBLE_QUOTATION);
		
		//ドロップダウンリストの設定
		getRoomDropDownList("", originalKikakuList, 
				"", originalAuseList,
				"", lendKbnList, 
				"0", coldExemptionKbnList);
		initDto.setOriginalKikakuList(originalKikakuList);
		initDto.setOriginalAuseList(originalAuseList);
		initDto.setLendKbnList(lendKbnList);
		initDto.setColdExemptionKbnList(coldExemptionKbnList);
		
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
			String areaKbnCd, Skf3010Sc006CommonDto initDto) throws ParseException {

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
		
		//部屋情報リスト
		// ' 「本来用途」ドロップダウンリストの設定
		List<Map<String, Object>> originalAuseList = new ArrayList<Map<String, Object>>();
		// ' 「貸与区分」ドロップダウンリストの設定
		List<Map<String, Object>> lendKbnList = new ArrayList<Map<String, Object>>();
		// ' 「本来規格」ドロップダウンリストの設定
		List<Map<String, Object>> originalKikakuList = new ArrayList<Map<String, Object>>();
		// ' 「寒冷地減免事由区分」ドロップダウンリストの設定
		List<Map<String, Object>> coldExemptionKbnList = new ArrayList<Map<String, Object>>();
				
				
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
		List<Skf3010Sc006GetKihonInfoExp> getKihonInfoListTableData = new ArrayList<Skf3010Sc006GetKihonInfoExp>();
		// 基本情報を取得する
		getKihonInfo(shatakuKanriNo, getKihonInfoListTableData);
		
		if(getKihonInfoListTableData.size() <= 0){
			ServiceHelper.addResultMessage(initDto, null, MessageIdConstant.E_SKF_1078,"基本情報初期化");
			return;
		}
		
		// 基本情報
		for(Skf3010Sc006GetKihonInfoExp tmpData : getKihonInfoListTableData){
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
			}else{
				nextCalculateDate = skfBaseBusinessLogicUtils.getJikaiSanteiNengappi(buildDate.replace("/",""),tmpData.getAreaKbn(),tmpData.getStructureKbn());
				nextCalculateDate = skfDateFormatUtils.dateFormatFromString(
						nextCalculateDate, SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}
			initDto.setNextCalcDate(null);
			initDto.setNextCalcDate(nextCalculateDate);

			// 備考
			if (tmpData.getBiko() != null) {
				biko = tmpData.getBiko();
			}
			initDto.setBiko(null);
			initDto.setBiko(biko);
			
			// 更新日（排他処理用）
			initDto.setKihonUpdateDate(null);
			initDto.setKihonUpdateDate(tmpData.getUpdateDate());
			
			//複写フラグチェック
			if(FALSE.equals(initDto.getCopyFlg())){
				//複写フラグFalseの場合のみ実施
				// 社宅補足1
				if (tmpData.getShatakuSupplementName1() != null) {
					shatakuSupplementName1 = tmpData.getShatakuSupplementName1();
					initDto.setShatakuHosokuLink1(null);
					initDto.setShatakuHosokuLink1(SHATAKU_HOSOKU_LINK + "_" + shatakuKanriNo + "_1");
					initDto.setShatakuHosokuFile1(tmpData.getShatakuSupplementFile1());
				}
				
				// 社宅補足2
				if (tmpData.getShatakuSupplementName2() != null) {
					shatakuSupplementName2 = tmpData.getShatakuSupplementName2();
					initDto.setShatakuHosokuLink2(null);
					initDto.setShatakuHosokuLink2(SHATAKU_HOSOKU_LINK + "_" + shatakuKanriNo + "_2");
					initDto.setShatakuHosokuFile2(tmpData.getShatakuSupplementFile2());
				}

				// 社宅補足3
				if (tmpData.getShatakuSupplementName3() != null) {
					shatakuSupplementName3 = tmpData.getShatakuSupplementName3();
					initDto.setShatakuHosokuLink3(null);
					initDto.setShatakuHosokuLink3(SHATAKU_HOSOKU_LINK + "_" + shatakuKanriNo + "_3");
					initDto.setShatakuHosokuFile3(tmpData.getShatakuSupplementFile3());
				}

				//部屋情報			
				//部屋管理番号
				initDto.setShatakuRoomKanriNo(String.valueOf(tmpData.getShatakuRoomKanriNo()));
				//部屋番号
				initDto.setRoomNo(tmpData.getRoomNo());
				//本来延面積
				initDto.setOriginalMenseki(tmpData.getOriginalMenseki().toPlainString());
				//階段面積
				initDto.setStairsMenseki(tmpData.getStairsMenseki().toPlainString());
				//本来規格
				initDto.setOriginalKikaku(tmpData.getOriginalKikaku());
				//貸与面積
				initDto.setLendMenseki(tmpData.getLendMenseki().toPlainString());
				//本来用途
				initDto.setOriginalAuse(tmpData.getOriginalAuse());
				//サンルーム面積
				initDto.setSunRoomMenseki(tmpData.getSunRoomMenseki().toPlainString());
				//物置調整面積
				initDto.setBarnMensekiAdjust(tmpData.getBarnMensekiAdjust().toPlainString());
				//貸与区分
				initDto.setLendKbn(tmpData.getLendKbn());
				//貸与区分補足
				initDto.setLendKbnHosoku(tmpData.getLendKbnHosoku());
				//寒冷地減免事由区分
				initDto.setColdExemptionKbn(tmpData.getColdExemptionKbn());
				//備考
				initDto.setRoomBiko(tmpData.getRoomBiko());
				
				//ドロップダウンリストの設定
				getRoomDropDownList(initDto.getOriginalKikaku(), originalKikakuList, 
						initDto.getOriginalAuse(), originalAuseList,
						initDto.getLendKbn(), lendKbnList, 
						initDto.getColdExemptionKbn(), coldExemptionKbnList);
				
				initDto.setOriginalKikakuList(originalKikakuList);
				initDto.setOriginalAuseList(originalAuseList);
				initDto.setLendKbnList(lendKbnList);
				initDto.setColdExemptionKbnList(coldExemptionKbnList);
			}else{
				//複写の場合
				//部屋情報は新規
				setRoomInfoShinki(initDto);
			}
			initDto.setShatakuHosokuFileName1(null);
			initDto.setShatakuHosokuFileName1(shatakuSupplementName1);
			initDto.setShatakuHosokuFileName2(null);
			initDto.setShatakuHosokuFileName2(shatakuSupplementName2);
			initDto.setShatakuHosokuFileName3(null);
			initDto.setShatakuHosokuFileName3(shatakuSupplementName3);
			
			
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
	}
	
	/**
	 * 各リスト、及びラベルの情報をそのまま反映する。
	 * @param initDto
	 */
	public void setShatakuInfoDropDownStay( Skf3010Sc006CommonDto initDto){
		// 地域区分リスト
		List<Map<String, Object>> areaKbnList = new ArrayList<Map<String, Object>>();
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
		
		//部屋情報リスト
		// ' 「本来用途」ドロップダウンリストの設定
		List<Map<String, Object>> originalAuseList = new ArrayList<Map<String, Object>>();
		// ' 「貸与区分」ドロップダウンリストの設定
		List<Map<String, Object>> lendKbnList = new ArrayList<Map<String, Object>>();
		// ' 「本来規格」ドロップダウンリストの設定
		List<Map<String, Object>> originalKikakuList = new ArrayList<Map<String, Object>>();
		// ' 「寒冷地減免事由区分」ドロップダウンリストの設定
		List<Map<String, Object>> coldExemptionKbnList = new ArrayList<Map<String, Object>>();
		// 駐車場構造リスト
		List<Map<String, Object>> parkingStructureList = new ArrayList<Map<String, Object>>();
		
		// 備品情報リスト
		List<Map<String, Object>> bihinList = new ArrayList<Map<String, Object>>();
		// 可変ラベルリスト
		List<Map<String, Object>> labelList = new ArrayList<Map<String, Object>>();
		
		//ドロップダウンリストの設定
		// ドロップダウンリストの値を設定
		areaKbnList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_AREA_KBN, initDto.getAreaKbn(), true));
		getDoropDownList(
				initDto.getUseKbn(), useKbnList,
				initDto.getManageCompany(), manageCompanyList,
				initDto.getManageAgency(), manageAgencyList,
				initDto.getManageBusinessArea(), manageBusinessAreaList,
				initDto.getPref(), prefList,
				initDto.getShatakuStructure(), shatakuStructureList,
				initDto.getElevator(), elevatorList);
		getRoomDropDownList(initDto.getOriginalKikaku(), originalKikakuList, 
				initDto.getOriginalAuse(), originalAuseList,
				initDto.getLendKbn(), lendKbnList, 
				initDto.getColdExemptionKbn(), coldExemptionKbnList);
		// 駐車場構造プルダウンリスト
		parkingStructureList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_PARKING_STRUCTURE_KBN, initDto.getParkingStructure(), true));
		
		//備品情報リスト
		bihinList.addAll(jsonArrayToArrayList(initDto.getJsonBihin()));
		setBihinList(bihinList,initDto);
		//可変ラベル
		labelList.addAll(jsonArrayToArrayList(initDto.getJsonLabelList()));
		setVariableLabel(labelList,initDto);
		
		initDto.setAreaKbnList(areaKbnList);
		initDto.setUseKbnList(useKbnList);
		initDto.setManageCompanyList(manageCompanyList);
		initDto.setManageAgencyList(manageAgencyList);
		initDto.setManageBusinessAreaList(manageBusinessAreaList);
		initDto.setPrefList(prefList);
		initDto.setShatakuStructureList(shatakuStructureList);
		initDto.setElevatorList(elevatorList);
		initDto.setOriginalKikakuList(originalKikakuList);
		initDto.setOriginalAuseList(originalAuseList);
		initDto.setLendKbnList(lendKbnList);
		initDto.setColdExemptionKbnList(coldExemptionKbnList);
		initDto.setParkingStructureList(parkingStructureList);

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
			String areaKbnCd, Skf3010Sc002CommonDto initDto) {
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

//		// 社宅部屋総数を取得する
//		Integer roomCnt = getRoomCount(shatakuKanriNo);
//		// 社宅部屋総数取得結果判定
//		if (roomCnt > 0) {
//			// （登録時対応）空き社宅数判定
//			if (emptyRoomCount == null || emptyRoomCount.length() == 0) {
//				// （登録時対応）空き社宅数が存在しない場合は、0を設定する。
//				emptyRoomCount = CodeConstant.STRING_ZERO;
//			}
//			// 「空き部屋数」を設定する
//			initDto.setEmptyRoomCount(null);
//			initDto.setEmptyRoomCount(emptyRoomCount + CodeConstant.SLASH + roomCnt.toString());
//		}
//
//		// 「空き駐車場数」を設定する
//		// 駐車場総数を取得する
//		Integer parkingCnt = getParkingCount(shatakuKanriNo);
//		// （登録時対応）空き駐車場数判定
//		if (emptyParkingCount == null || emptyParkingCount.length() == 0) {
//			// （登録時対応）空き社宅数が存在しない場合は、0を設定する。
//			emptyParkingCount = CodeConstant.STRING_ZERO;
//		}
//		initDto.setEmptyParkingCount(null);
//		initDto.setEmptyParkingCount(emptyParkingCount + CodeConstant.SLASH + parkingCnt.toString());
		// 解放
		areaKbnList = null;
		shatakuKbnList = null;
		// データ比較用
		initDto.setStartingAreaKbn(null);
		initDto.setStartingAreaKbn(areaKbnCd);
	}

	/**
	 * 駐車場契約情報
	 * 
	 * @param selectMode				選択モード
	 * @param contractSelectedIndex		契約情報選択プルダウン値
	 * @param initDto					*DTO
	 * @throws Exception
	 */
	public void changePakingContractInfo(String selectMode, 
			String contractSelectedIndex,String parkContractSelectedIndex, Skf3010Sc006CommonDto initDto) throws Exception {
		/** セッション情報取得用 */
		// 社宅管理番号
		String shatakuKanriNo = "";
		
		// 削除済契約番号
		String parkDeletedConstractNo = CodeConstant.STRING_ZERO;
		// 削除済駐車場契約番号
		if (initDto.getHdnDeleteParkingContractSelectedValue() != null
				&& initDto.getHdnDeleteParkingContractSelectedValue().length() > 0) {
			parkDeletedConstractNo = initDto.getHdnDeleteParkingContractSelectedValue();
		}
		if (!NfwStringUtils.isEmpty(initDto.getHdnShatakuKanriNo())) {
			// 自画面
			shatakuKanriNo = initDto.getHdnShatakuKanriNo();
		} 
//        ' 契約情報(駐車場)を設定
        setParkingContractInfo(shatakuKanriNo, parkContractSelectedIndex, parkDeletedConstractNo, selectMode, initDto);
	}
	
	/**
	 * 社宅情報取得設定。<br />
	 * 社宅登録画面のヘッダー部、各タブ情報、プルダウンの取得、設定を行う。
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param selectMode				選択モード
	 * @param contractSelectedIndex		契約情報選択プルダウン値
	 * @param initDto					*DTO
	 * @throws Exception
	 */
	public void setShatakuInfo(String selectMode, 
			String contractSelectedIndex,String parkContractSelectedIndex, Skf3010Sc006CommonDto initDto) throws Exception {

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
		// 削除済契約番号
		String deletedConstractNo = CodeConstant.STRING_ZERO;
		// 削除済契約番号
		String parkDeletedConstractNo = CodeConstant.STRING_ZERO;

		/** セッション情報取得 */
		// 社宅管理番号
		if (initDto.getHdnShatakuKanriNo() != null) {
			shatakuKanriNo = initDto.getHdnShatakuKanriNo();
		}
		// 社宅名
		if (initDto.getHdnShatakuName() != null) {
			shatakuName = initDto.getHdnShatakuName();
		}
		// 社宅区分コード
		if (initDto.getHdnShatakuKbn() != null) {
			shatakuKbnCd = initDto.getHdnShatakuKbn();
		}
		// 地域区分コード
		if (initDto.getHdnAreaKbn() != null) {
			areaKbnCd = initDto.getHdnAreaKbn();
		}
//		// 空き部屋数
//		if (initDto.getHdnEmptyRoomCount() != null) {
//			emptyRoomCount = initDto.getHdnEmptyRoomCount();
//		}
//		// 空き駐車場数
//		if (initDto.getHdnEmptyParkingCount() != null) {
//			emptyParkingCount = initDto.getHdnEmptyParkingCount();
//		}
		// 削除済契約番号
		if (initDto.getHdnDeleteContractSelectedValue() != null
				&& initDto.getHdnDeleteContractSelectedValue().length() > 0) {
			deletedConstractNo = initDto.getHdnDeleteContractSelectedValue();
		}
		// 削除済駐車場契約番号
		if (initDto.getHdnDeleteParkingContractSelectedValue() != null
				&& initDto.getHdnDeleteParkingContractSelectedValue().length() > 0) {
			parkDeletedConstractNo = initDto.getHdnDeleteParkingContractSelectedValue();
		}
		/** セッション情報取得 */
		// 遷移元判定
		if (!NfwStringUtils.isEmpty(initDto.getHdnShatakuKanriNo())) {
			// 自画面
			shatakuKanriNo = initDto.getHdnShatakuKanriNo();
			shatakuName = initDto.getHdnShatakuName();
			shatakuKbnCd = initDto.getHdnShatakuKbn();
			areaKbnCd = initDto.getHdnAreaKbn();
			emptyRoomCount = initDto.getHdnEmptyRoomCount();
			emptyParkingCount = initDto.getHdnEmptyParkingCount();
		} else if (!NfwStringUtils.isEmpty(initDto.getHdnRowShatakuKanriNo())) {
			// 社宅一覧からの遷移
			shatakuKanriNo = initDto.getHdnRowShatakuKanriNo();
			shatakuName = initDto.getHdnRowShatakuName();
			shatakuKbnCd = initDto.getHdnRowShatakuKbn();
			areaKbnCd = initDto.getHdnRowAreaKbn();
			emptyRoomCount = initDto.getHdnRowEmptyRoomCount();
			emptyParkingCount = initDto.getHdnRowEmptyParkingCount();
		} else {
			// 新規
			shatakuKbnCd = initDto.getHdnRowShatakuKbn();
		}
		initDto.setCopyFlg("false");
		// セッション情報存在判定
		if (!NfwStringUtils.isEmpty(shatakuKanriNo)) {
			// ヘッダ部の値をセット
			setSearchInfo(shatakuKanriNo, shatakuName, shatakuKbnCd, 
					areaKbnCd, initDto);
			// 基本情報タブの値をセット
			setKihonInfo(shatakuKanriNo, areaKbnCd, initDto);
			// 駐車場情報タブの値をセット
			setParkingInfo(shatakuKanriNo, initDto);
			
			// 管理者情報タブの値をセット
			setShatakuManageInfo(shatakuKanriNo, initDto);
			
			if(FALSE.equals(initDto.getCopyFlg())){
				//基本から
				// 備品情報タブの値をセット
				String shatakuRoomKanriNo = initDto.getShatakuRoomKanriNo();
				setBihinInfo(shatakuKanriNo, shatakuRoomKanriNo,bihinInfoListTableData, hdnBihinInfoListTableData);
				// 契約情報タブの値設定
				setContractInfo(shatakuKanriNo, contractSelectedIndex, deletedConstractNo, selectMode, initDto);
				// 契約情報(駐車場)を設定
				setParkingContractInfo(shatakuKanriNo, parkContractSelectedIndex, parkDeletedConstractNo, selectMode, initDto);
			}else{
				//複写の場合
				// 備品情報タブの値を新規でセット
				setBihinInfoOfShinki(bihinInfoListTableData, hdnBihinInfoListTableData);
				// 契約情報タブの値設定
				setContractInfoShinki(selectMode, initDto);
				// 駐車場契約情報ボタン(非活性：true, 活性:false)
				parkingContractDisableFlg = true;
				// 複写なので新規扱い
				newShatakuFlg = true;
				//管理番号クリア
				shatakuKanriNo = CodeConstant.DOUBLE_QUOTATION;
			}
			
//			// 社宅部屋情報取得(排他用)
//			getShatakuRoomInfo(shatakuKanriNo, shatakuRoomExlusiveCntrllList);
//			initDto.setRoomeBihinExlusiveCntrllList(null);
//			initDto.setRoomeBihinExlusiveCntrllList(shatakuRoomExlusiveCntrllList);
//			// 社宅部屋備品情報取得(排他用)
//			getShatakuRoomBihinInfo(shatakuKanriNo, roomeBihinExlusiveCntrllList);
//			initDto.setRoomeBihinExlusiveCntrllList(null);
//			initDto.setRoomeBihinExlusiveCntrllList(roomeBihinExlusiveCntrllList);
		} else {
			// 新規の場合
			newShatakuFlg = true;
			// ヘッダ部の値をセット
			setSearchInfoShinki(shatakuKbnCd, initDto);
			// 基本情報タブの値をセット
			setKihonInfoShinki(initDto);
			// 部屋情報タブの値をセット
			setRoomInfoShinki(initDto);
			// 駐車場情報タブの値をセット
			setShatakuParkingInfoShinki(initDto);
			// 備品情報タブの値をセット
			setBihinInfoOfShinki(
					bihinInfoListTableData, hdnBihinInfoListTableData);
			// 管理者情報タブの値をセット
			setShatakuManageInfoShinki(initDto);

			// 契約情報タブの値設定
			setContractInfoShinki(selectMode, initDto);

			// 駐車場契約情報ボタン(非活性：true, 活性:false)
			parkingContractDisableFlg = true;
		}

		// 駐車場情報区画情報追加ボタン用貸与区分
		setLendKbnSelectListString = createLendKbnSelectList();

		/** 戻り値セット */
		// ページタイトル
		// 借上社宅登録
		initDto.setPageTitleKey(MessageIdConstant.SKF3010_SC006_TITLE);
		// 社宅区分ラベルフラグを「false」
		initDto.setIttoFlg(false);
		
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
		if (jsonStr == null || jsonStr.length() <= 0) {
			logger.debug("文字列未設定");
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
			logger.debug(e.getMessage());
		} catch (JsonParseException e) {
			logger.debug(e.getMessage());
		} catch (JsonMappingException e) {
			logger.debug(e.getMessage());
		} catch (IOException e) {
			logger.debug(e.getMessage());
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
	 * 備品情報復元
	 * 備品情報を設定する。
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param bihinList	備品情報リスト
	 * @param dto	*DTO
	 */
	private void setBihinList(List<Map<String, Object>> bihinList, Skf3010Sc006CommonDto dto) {

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

		dto.setBihinInfoListTableData(null);
		dto.setBihinInfoListTableData(setViewList);
	}
	
	/**
	 * 可変ラベル値復元
	 * 可変ラベルの値を設定する。
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param labelList		可変ラベルリスト
	 * @param dto		*DTO
	 */
	private String setVariableLabel(List<Map<String, Object>> labelList, Skf3010Sc006CommonDto dto) {
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
		String parkingRentalAdjust =
				(labelMap.get("parkingRentalAdjust") != null) ? labelMap.get("parkingRentalAdjust").toString() : "0";
		// 貸与可能総数
		String parkingShiyoMonthFei =
				(labelMap.get("parkingShiyoMonthFei") != null) ? labelMap.get("parkingShiyoMonthFei").toString() : "0";

		/** 戻り値設定 */
		// 設定
		dto.setJituAge(jituAge);
		dto.setNextCalcDate(nextCalcDate);
		dto.setAging(aging);
		dto.setParkingRent(parkingRent);
		dto.setParkingRentalAdjust(parkingRentalAdjust.replace(",", ""));
		dto.setParkingShiyoMonthFei(parkingShiyoMonthFei);

		return parkingRent;
	}
}