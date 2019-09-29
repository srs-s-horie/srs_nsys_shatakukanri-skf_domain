/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc001;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc001.Skf3010Sc001GetListTableDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc001.Skf3010Sc001GetListTableDataExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc001.Skf3010Sc001GetListTableDataExpRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.intra_mart.common.platform.log.Logger;

/**
 * Skf3010Sc001SharedService 社宅一覧内共通クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3010Sc001SharedService {

	@Value("${skf3010.skf3010_sc001.max_search_count}")
	private Integer maxGetRecordCount;

	@Autowired
	private SkfDropDownUtils ddlUtils;
	@Autowired
	private Skf3010Sc001GetListTableDataExpRepository skf3010Sc001GetListTableDataExpRepository;
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	/** ロガー。 */
	private static Logger logger = LogUtils.getLogger(SkfFileOutputUtils.class);

	public static List<Skf3010Sc001GetListTableDataExp> resultList;
	/** 単位 */
	// 経年
	private final String AGING_YEAR = "年";
	// 部屋数
	private final String ROOM = "室";
	// 駐車場数
	private final String PARKING = "台";
	// 都道府県コード(その他)
	private final String CD_PREF_OTHER = "48";
	// 借上(社宅区分)
	private final String KARIAGE = "2";

	/**
	 * ドロップダウンリストに設定するリストを取得する。<br>
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param selectedCompanyCd		管理会社コード選択値
	 * @param manageCompanyList	*管理会社プルダウン
	 * @param agencyCd				管理機関コード選択値
	 * @param manageAgencyList		*管理機関プルダウン
	 * @param shatakuKbnCd			社宅区分コード選択値
	 * @param shatakuKbnList		*社宅区分プルダウン
	 * @param emptyRoomCd			空き部屋区分コード選択値
	 * @param emptyRoomList		*空き部屋プルダウン
	 * @param useKbnCd				利用区分コード選択値
	 * @param useKbnList			*利用区分プル団
	 * @param emptyParkingCd		空き駐車場区分コード選択値
	 * @param emptyParkingList		*空き駐車場プルダウン
	 */
	public void getDoropDownList(
			String selectedCompanyCd, List<Map<String, Object>> manageCompanyList,
			String agencyCd, List<Map<String, Object>> manageAgencyList,
			String shatakuKbnCd, List<Map<String, Object>> shatakuKbnList,
			String emptyRoomCd, List<Map<String, Object>> emptyRoomList,
			String useKbnCd, List<Map<String, Object>> useKbnList,
			String emptyParkingCd, List<Map<String, Object>> emptyParkingList) {

		boolean isFirstRowEmpty = true;

		// 管理会社リスト
		manageCompanyList.clear();
		manageCompanyList.addAll(ddlUtils.getDdlCompanyByCd(selectedCompanyCd, isFirstRowEmpty));

		// 管理機関リスト
		manageAgencyList.clear();
		manageAgencyList.addAll(ddlUtils.getDdlAgencyByCd(selectedCompanyCd, agencyCd, isFirstRowEmpty));

		// 社宅区分リスト
		shatakuKbnList.clear();
		shatakuKbnList.addAll(ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_SHATAKU_KBN,
				shatakuKbnCd, isFirstRowEmpty));

		// 空き部屋リスト
		emptyRoomList.clear();
		emptyRoomList.addAll(ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_AKIROOM_KBN, emptyRoomCd,
				isFirstRowEmpty));

		// 利用区分リスト
		useKbnList.clear();
		useKbnList.addAll(
				ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_RIYO_KBN, useKbnCd, isFirstRowEmpty));

		// 空き駐車場リスト
		emptyParkingList.clear();
		emptyParkingList.addAll(ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_AKIPARKING_KBN,
				emptyParkingCd, isFirstRowEmpty));

	}

	/**
	 * リストテーブルを取得する。 <br>
	 * パラメータの検索キーでDBを検索し、結果を戻り値のリストテーブルに設定する
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param selectedCompanyCd	会社コード
	 * @param agencyCd			機関コード
	 * @param shatakuKbnCd		社宅区分
	 * @param emptyRoomCd		空き部屋
	 * @param useKbnCd			利用区分
	 * @param emptyParkingCd	空き駐車場
	 * @param shatakuName		社宅名
	 * @param shatakuAddress	社宅住所
	 * @param listTableData		*リストデータ
	 * @return
	 * @throws ParseException
	 */
	public int getListTableData(String selectedCompanyCd, String agencyCd, String shatakuKbnCd, String emptyRoomCd,
			String useKbnCd, String emptyParkingCd, String shatakuName, String shatakuAddress,
			List<Map<String, Object>> listTableData) throws ParseException {

		logger.debug("社宅検索キー：" + "会社コード：" + selectedCompanyCd + "　機関コード：" + agencyCd + "　社宅区分コード：" + shatakuKbnCd
				+ "　空き部屋コード：" + emptyRoomCd + "　利用区分コード：" + useKbnCd + "　空き駐車場コード：" + emptyParkingCd + "　社宅名：" + shatakuName + " 社宅住所:"
				+ shatakuAddress);

		// リストテーブルに格納するデータを取得する
		int resultCount = 0;
		List<Skf3010Sc001GetListTableDataExp> resultListTableData = new ArrayList<Skf3010Sc001GetListTableDataExp>();
		Skf3010Sc001GetListTableDataExpParameter param = new Skf3010Sc001GetListTableDataExpParameter();
		List<Map<String, Object>> tmpListData = new ArrayList<Map<String, Object>>();

		do {
			String searchShatakuName = null;
			// 文字エスケープ(% _ ' \)
			if (shatakuName != null) {
				// 「\」を「\\」に置換
				searchShatakuName = shatakuName.replace("\\", "\\\\");
				// 「%」を「\%」に置換、「_」を「\_」に置換、「'」を「''」に置換
				searchShatakuName = searchShatakuName.replace("%", "\\%").replace("_", "\\_").replace("'", "''");
			}
			param.setSelectedCompanyCd(selectedCompanyCd);
			param.setAgencyCd(agencyCd);
			param.setShatakuKbnCd(shatakuKbnCd);
			param.setEmptyRoomCd(emptyRoomCd);
			param.setUseKbnCd(useKbnCd);
			param.setEmptyParkingCd(emptyParkingCd);
			param.setShatakuName(searchShatakuName);
			resultListTableData = skf3010Sc001GetListTableDataExpRepository.getListTableData(param);

			// 取得レコード数を設定
			resultCount = resultListTableData.size();

			// 取得データレコード数判定
			if (resultCount == 0) {
				// 取得データレコード数が0件の多い場合、何もせず処理終了
				break;
			}

			// リストテーブルに出力するリストを取得する
			tmpListData.clear();
			tmpListData.addAll(getListTableDataViewColumn(resultListTableData, shatakuAddress));
			// 取得レコード数を設定
			resultCount = tmpListData.size();
			if (resultCount == 0 || resultCount > maxGetRecordCount) {
				// 取得データレコード数が0件または3000件より多い場合、何もせず処理終了
				break;
			}
			// 出力するリストを設定
			listTableData.clear();
			listTableData.addAll(tmpListData);
			
		} while(false);
		tmpListData = null;
		resultListTableData = null;
		param = null;

		return resultCount;

	}
	
	/**
	 * リストテーブルに出力するリストを取得する。
	 * 検索社宅住所が指定されている場合は対象住所を含む場合のみリストへ追加する。
	 * 
	 * @param1 originList		DBからの取得結果
	 * @param2 searchAddress	検索社宅住所
	 * @return リストテーブルに出力するリスト
	 * @throws ParseException
	 */
	private List<Map<String, Object>> getListTableDataViewColumn(
			List<Skf3010Sc001GetListTableDataExp> originList, String searchAddress)
			throws ParseException {

		logger.debug("社宅一覧リスト作成");

		// 社宅区分コード取得
		Map<String, String> genericCodeMapShatakuKbn =
				skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_SHATAKU_KBN);
		// 利用区分コード取得
		Map<String, String> genericCodeMapUseKbn =
				skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_RIYO_KBN);
		
		// 構造区分
		Map<String, String> genericCodeMapStructure =
				skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_STRUCTURE_KBN);
		// 都道府県コード
		Map<String, String> genericCodeMapPref =
				skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_PREFCD);

		List<Map<String, Object>> setViewList = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < originList.size(); i++) {
			String shatakuKbn = "";
			String shatakuAddress = "";
			String useKbn = "";
			String structureKbn = "";
			String emptyRoomCount = "";
			String emptyParkingCount = "";
			String areaKbn = "";
			String buildDate = "";
			Long keinen = 0L;

			Skf3010Sc001GetListTableDataExp tmpData = originList.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();

			// 管理会社名
			tmpMap.put("companyName", tmpData.getCompanyName());
			// 管理機関名
			tmpMap.put("agencyName", tmpData.getAgencyName());
			tmpMap.put("col1", tmpData.getRoomNo());
			// 社宅区分
			// 汎用コードから取得
			if (tmpData.getShatakuKbn() != null) {
				shatakuKbn = genericCodeMapShatakuKbn.get(tmpData.getShatakuKbn());
			}
			tmpMap.put("shatakuKbn", shatakuKbn);
			// 利用区分
			if (tmpData.getUseKbn() != null) {
				useKbn = genericCodeMapUseKbn.get(tmpData.getUseKbn());
			}
			tmpMap.put("useKbn", useKbn);
			// 社宅名
			tmpMap.put("shatakuName", tmpData.getShatakuName());
			// 社宅所在地
			// 都道府県コード判定
			if (tmpData.getPrefCd() != null && !tmpData.getPrefCd().equals(CD_PREF_OTHER)) {
				// 存在し、且つ、その他以外の場合は都道府県名を付与
				shatakuAddress = genericCodeMapPref.get(tmpData.getPrefCd());
			}
			// 住所取得結果判定
			if (tmpData.getShatakuAddress() != null) {
				shatakuAddress += tmpData.getShatakuAddress();
			}
			// 住所検索判定
			if (searchAddress != null && searchAddress.length() > 0) {
				// 住所検索(検索キーが含まれていない
				if (!shatakuAddress.contains(searchAddress)) {
					// リストへ追加せず次の取得データ処理を実施する
					continue;
				}
			}

			tmpMap.put("shatakuAddress", shatakuAddress);
			// 構造
			if (tmpData.getStructureKbn() != null) {
				structureKbn = genericCodeMapStructure.get(tmpData.getStructureKbn());
			}
			tmpMap.put("structureKbn", structureKbn);
			/** 経年 */
			// 地域区分判定
			if (tmpData.getAreaKbn() != null) {
				areaKbn = tmpData.getAreaKbn();
			}
			// 建築年月日
			if (tmpData.getBuildDate() != null) {
				buildDate = tmpData.getBuildDate();
			}
			// 経年取得
			keinen = skfBaseBusinessLogicUtils.getAging(buildDate, areaKbn, tmpData.getStructureKbn());
			tmpMap.put("aging", Long.toString(keinen) + AGING_YEAR);

			// 空きbr部屋数
			if (tmpData.getEmptyRoomCount() != null && !tmpData.getEmptyRoomCount().equals("0")
					&& tmpData.getEmptyRoomCount().length() > 0) {
				emptyRoomCount = tmpData.getEmptyRoomCount() + ROOM;
			} else {
				emptyRoomCount = "0" + ROOM;
			}
			tmpMap.put("emptyRoomCount", emptyRoomCount);
			// 空きbr駐車場数
			if (tmpData.getEmptyParkingCount() != null && !tmpData.getEmptyParkingCount().equals("0")
					&& tmpData.getEmptyParkingCount().length() > 0) {
				emptyParkingCount = tmpData.getEmptyParkingCount() + PARKING;
			} else {
				emptyParkingCount = "0" + PARKING;
			}
			tmpMap.put("emptyParkingCount", emptyParkingCount);
			// 対象行の社宅区分
			tmpMap.put("hdnShatakuKbn", tmpData.getShatakuKbn());
			// 対象行の社宅管理番号
			tmpMap.put("hdnShatakuKanriNo", tmpData.getShatakuKanriNo());
			// 対象行の社宅名
			tmpMap.put("hdnShatakuName", tmpData.getShatakuName());
			// 対象行の地域区分
			tmpMap.put("hdnAreaKbn", tmpData.getAreaKbn());
			// 対象行の空き部屋数
			tmpMap.put("hdnEmptyRoomCount", tmpData.getEmptyRoomCount());
			// 対象行の空き駐車場数
			tmpMap.put("hdnEmptyParkingCount", tmpData.getEmptyParkingCount());
			// 対象行の部屋番号
			tmpMap.put("hdnRoomNo", tmpData.getRoomNo());
			// 基本
			tmpMap.put("col11", "");
			// 社宅区分判定
			if (!KARIAGE.equals(tmpData.getShatakuKbn())) {
				// 借上以外の場合のみ、「部屋」を追加
				tmpMap.put("col12", "");
			}
			setViewList.add(tmpMap);
			tmpMap = null;
			tmpData = null;
		}
		// 解放
		genericCodeMapShatakuKbn = null;
		genericCodeMapUseKbn = null;
		genericCodeMapStructure = null;
		genericCodeMapPref = null;
		return setViewList;
	}
}
