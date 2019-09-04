/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc001;

import static jp.co.c_nexco.nfw.core.constants.CommonConstant.NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi_v3_8.ss.usermodel.Cell;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.nfw.webcore.utils.bean.RowDataBean;
import jp.co.c_nexco.nfw.webcore.utils.bean.SheetDataBean;
import jp.co.c_nexco.nfw.webcore.utils.bean.WorkBookDataBean;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc001.Skf3010Sc001ContractDownLoadDto;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc001.Skf3010Sc001GetShatakuContractInfoDataExp;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc001.Skf3010Sc001GetParkingContractInfoDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc001.Skf3010Sc001GetShatakuContractInfoDataExpRepository;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc001.Skf3010Sc001GetContractInfoDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc001.Skf3010Sc001GetParkingContractInfoDataExp;
import jp.co.intra_mart.common.platform.log.Logger;

/**
 * Skf3010Sc001ContractDownLoadService 社宅一覧の契約情報出力ボタン処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3010Sc001ContractDownLoadService extends BaseServiceAbstract<Skf3010Sc001ContractDownLoadDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private Skf3010Sc001GetShatakuContractInfoDataExpRepository skf3010Sc001GetShatakuContractInfoDataExpRepository;
	@Autowired
	private Skf3010Sc001GetParkingContractInfoDataExpRepository skf3010Sc001GetParkingContractInfoDataExpRepository;
	@Value("${skf3010.skf3010_sc001.excelOutPutStartLine}")
	private Integer excelOutPutStartLine;
	@Value("${skf3010.skf3010_sc001.excelPreFileName}")
	private String excelPreFileName;
	@Value("${skf3010.skf3010_sc001.excelWorkSheetNameShataku}")
	private String excelWorkSheetNameShataku;
	@Value("${skf3010.skf3010_sc001.excelWorkSheetNameParking}")
	private String excelWorkSheetNameParking;

	/**
	 * 社宅一覧 契約情報出力ボタン押下処理
	 */
	// 社宅区分:借上
	private static final String SHATAKU_KBN_KARIAGE = "2";
	// 社宅区分:一棟
	private static final String SHATAKU_KBN_ITTO = "4";

	/** ロガー。 */
	private static Logger logger = LogUtils.getLogger(SkfFileOutputUtils.class);

	@Value("${skf.common.validate_error}")
	private String validationErrorCode;

	/**
	 * 契約情報帳票出力
	 * 
	 * Dtoのリストから出力対象を抽出し、DBから帳票に出力に必要なデータを取得する。
	 * DBからの取得データが社宅契約情報、駐車場契約情報の双方が存在しない場合は帳票の出力は行わない。
	 * DBからの取得データ、Dtoから抽出した出力対象データから帳票出力データを作成し 社宅契約情報、駐車場契約情報をExcelファイルに帳票出力する。
	 * 処理結果はメッセージに設定する。
	 */
	@Override
	public BaseDto index(Skf3010Sc001ContractDownLoadDto downloadDto) throws Exception {
		// public BaseDto index(Skf3010Sc001OutContractDto outContraDto) throws
		// Exception {
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("契約情報出力", CodeConstant.C001, downloadDto.getPageId());
		// デバッグログ
		logger.debug("契約情報出力ボタン押下");

		// 契約情報取得リスト
		Map<String, Object> getContractList = new HashMap<String, Object>();
		// 社宅契約情報(DBから取得)
		List<Skf3010Sc001GetShatakuContractInfoDataExp> getShatakuContractList = new ArrayList<Skf3010Sc001GetShatakuContractInfoDataExp>();
		// 駐車場契約情報(DBから取得)
		List<Skf3010Sc001GetParkingContractInfoDataExp> getParkingContractList = new ArrayList<Skf3010Sc001GetParkingContractInfoDataExp>();
		// 社宅契約情報出力リスト
		SheetDataBean shatakuContractWorkSheet = null;
		// 駐車場契約情報出力リスト
		SheetDataBean parkingContractWorkSheet = null;
		// DTOのより契約情報取得対象リストを取得
		List<Map<String, Object>> contractList = downloadDto.getListTableData();
		// 社宅管理番号リスト(SQLパラメータ用)
		List<Long> paramShatakuKanriNoList = null;

		do {
			// 契約情報取得リスト作成
			paramShatakuKanriNoList = createGetContractList(contractList, getContractList);
			// 契約情報リスト件数判定
			if (getContractList.size() < 1) {
				logger.warn("社宅区分「借上/一棟」の検索結果が存在しません。");
				// {0}は存在しません。
				ServiceHelper.addErrorResultMessage(downloadDto, null, MessageIdConstant.E_SKF_1067,
						"検索結果に出力対象データ(「借上」、または「一棟」)");
				break;
			}
			// 社宅契約情報取得(DBから取得)
			int shatakuContractCount = createShatakuContractTableDataList(paramShatakuKanriNoList,
					getShatakuContractList);
			// 駐車場契約情報取得(DBから取得)
			int parkingContractCount = createParkingContractTableDataList(paramShatakuKanriNoList,
					getParkingContractList);
			// 取得結果件数判定
			if ((shatakuContractCount + parkingContractCount) < 1) {
				logger.warn("社宅情報が変更されていますので検索をしなおしてください。");
				// {0}は存在しません。
				ServiceHelper.addErrorResultMessage(downloadDto, null, MessageIdConstant.E_SKF_3059);
				break;
			}
			// 社宅契約情報出力用Excelワークシート作成
			shatakuContractWorkSheet = createWorkSheetShatakuContract(getContractList, getShatakuContractList);
			// 駐車場契約情報出力リスト作成
			parkingContractWorkSheet = createWorkSheetParkingContract(getContractList, getParkingContractList);
			// 帳票作成
			fileOutPutExcelContractInfo(shatakuContractWorkSheet, parkingContractWorkSheet, downloadDto);
		} while (false);

		// 解放
		paramShatakuKanriNoList = null;
		contractList = null;
		getContractList = null;
		getShatakuContractList = null;
		getParkingContractList = null;
		shatakuContractWorkSheet = null;
		parkingContractWorkSheet = null;
		return downloadDto;
	}

	/**
	 * 契約情報取得リスト作成
	 * 
	 * 引数の「契約情報取得対象リスト」より「契約情報取得リスト」作成を作成する。 契約情報取得リストの社宅管理番号は一意とし、重複登録は行わない
	 * 社宅区分が「借上」、「一棟」以外のデータは対象外とする
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param contractList
	 *            契約情報取得対象リスト
	 * @param *getContractList
	 *            契約情報取得リスト
	 * @return 契約情報取得社宅管理番号(カンマ区切り：SQLパラメータで使用)
	 */
	private List<Long> createGetContractList(List<Map<String, Object>> contractList,
			Map<String, Object> getContractList) {

		// 社宅管理番号リスト
		List<Long> shatakukanriNoList = new ArrayList<Long>();
		getContractList.clear();

		// 契約情報取得リスト作成
		for (int i = 0; i < contractList.size(); i++) {
			Map<String, Object> tmpData = contractList.get(i); // パラメータのリスト1行
			Map<String, Object> tmpMap = new HashMap<String, Object>(); // 追加用行データ
			// 社宅管理番号
			String shatakuKanriNo = "";
			if (tmpData.get("hdnShatakuKanriNo") != null) {
				shatakuKanriNo = tmpData.get("hdnShatakuKanriNo").toString();
			}
			// 社宅区分コード
			String hdnShatakuKbn = "";
			if (tmpData.get("hdnShatakuKbn") != null) {
				hdnShatakuKbn = tmpData.get("hdnShatakuKbn").toString();
			}
			if (getContractList.containsKey(shatakuKanriNo)) {
				// 社宅管理番号はリスト内で一意とし重複登録は行わない
				continue;
			}
			// 社宅区分コード判定①
			if (!SHATAKU_KBN_KARIAGE.equals(hdnShatakuKbn) && !SHATAKU_KBN_ITTO.equals(hdnShatakuKbn)) {
				// 社宅区分が「借上/一棟」以外のデータは除外する。
				continue;
			}
			// ・社宅区分コード
			tmpMap.put("hdnShatakuKbn", hdnShatakuKbn);
			// ・社宅区分
			tmpMap.put("shatakuKbn", tmpData.get("shatakuKbn"));
			// ・部屋番号
			tmpMap.put("hdnRoomNo", tmpData.get("hdnRoomNo"));
			// ・所在地
			tmpMap.put("shatakuAddress", tmpData.get("shatakuAddress"));
			// 契約情報取得リストへ追加
			getContractList.put(shatakuKanriNo, tmpMap);
			// 社宅管理番号リストへ追加
			shatakukanriNoList.add(Long.parseLong(shatakuKanriNo));
		}
		return shatakukanriNoList;
	}

	/**
	 * 社宅契約情報出力用Excelワークシート作成
	 * 
	 * 引数の「契約情報取得リスト」、「社宅契約情報リスト」より社宅契約情報出力用Excelワークシートを作成する。
	 * 部屋番号は社宅区分が「借上」の場合のみ設定し、「借上」以外の場合は空文字とする。
	 * 賃貸人番号がDBから取得出来ていない場合、下記データは空文字とする。 ・契約番号 ・賃貸人氏名 ・賃貸人住所 ・法人個人区分 ・所在地
	 * ・経理連携用管理番号 ・契約開始日 ・契約終了日 ・家賃 ・共益費 ・駐車場料（地代）
	 * 
	 * @param getContractList
	 *            契約情報取得リスト
	 * @param getShatakuContractList
	 *            社宅契約情報リスト(DBからの取得値)
	 * @return 社宅契約情報出力用Excelワークシート
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private SheetDataBean createWorkSheetShatakuContract(Map<String, Object> getContractList,
			List<Skf3010Sc001GetShatakuContractInfoDataExp> getShatakuContractList) throws Exception {

		// 個人法人区分リスト取得
		Map<String, String> businessKbnList = new HashMap<String, String>();
		businessKbnList = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_KOJIN_HOJIN_KUBUN);
		// Excelワークシート(契約情報（社宅）)
		SheetDataBean sheetDataBean = new SheetDataBean();
		// Excel行データ
		List<RowDataBean> rowDataBeanList = new ArrayList<>();

		// 社宅契約情報
		for (int i = 0, rowNo = 1; i < getShatakuContractList.size(); i++, rowNo++) {
			// 設定用ワーク変数
			String shatakuKbn = "";
			String shatakuName = "";
			String roomNo = "";
			String contractNo = "";
			String ownerName = "";
			String ownerAddress = "";
			String businessKbn = "";
			String shatakuAddress = "";
			String assetRegisterNo = "";
			String contractStartDate = "";
			String contractEndDate = "";
			String rent = "";
			String kyoekihi = "";
			String landRent = "";

			// 行データ
			RowDataBean rdb = new RowDataBean();
			Skf3010Sc001GetShatakuContractInfoDataExp getRowData = getShatakuContractList.get(i); // DB取得1行
			Map<String, Object> listRowData = null; // 契約情報取得リスト1行

			// 社宅管理番号取得
			if (getRowData.getShatakuKanriNo() == null) {
				logger.error("契約情報取得リストに社宅管理番号なし");
				throw new Exception("契約情報取得リストに社宅管理番号なし");
			}
			if (!getContractList.containsKey(getRowData.getShatakuKanriNo().toString())) {
				logger.error("社宅契約情報リストに社宅管理番号なし");
				throw new Exception("社宅契約情報リストに社宅管理番号なし");
			}
			// 契約情報取得リストより該当社宅管理番号データ取得
			listRowData = (Map<String, Object>) getContractList.get(getRowData.getShatakuKanriNo().toString());

			// 行データ設定
			// 社宅区分(契約情報取得リスト)
			if (listRowData.containsKey("shatakuKbn") && listRowData.get("shatakuKbn") != null) {
				shatakuKbn = listRowData.get("shatakuKbn").toString();
			}
			// 社宅名(DB取得データ)
			if (getRowData.getShatakuName() != null) {
				shatakuName = getRowData.getShatakuName();
			}
			// 部屋番号(社宅区分が「借上」の場合のみ対象とする)
			// 社宅区分判定
			if (listRowData.containsKey("hdnShatakuKbn") && listRowData.get("hdnShatakuKbn") != null
					&& listRowData.get("hdnShatakuKbn").equals(SHATAKU_KBN_KARIAGE)) {
				// 部屋番号設定判定
				if (listRowData.containsKey("hdnRoomNo") && listRowData.get("hdnRoomNo") != null) {
					roomNo = listRowData.get("hdnRoomNo").toString();
				}
			}
			// 賃貸人番号取得結果判定
			// 未設定時は以降のデータは空文字とする
			if (getRowData.getOwnerNo() != null) {
				logger.debug("賃貸人番号あり");
				// 契約番号(DB取得データ)
				if (getRowData.getContractNo() != null) {
					contractNo = getRowData.getContractNo().toString();
				}
				// 賃貸人氏名(DB取得データ)
				if (getRowData.getOwnerName() != null) {
					ownerName = getRowData.getOwnerName();
				}
				// 賃貸人住所 (DB取得データ)
				if (getRowData.getOwnerAddress() != null) {
					ownerAddress = getRowData.getOwnerAddress();
				}
				// 法人個人区分(DB取得データ)
				if (getRowData.getBusinessKbn() != null && businessKbnList.get(getRowData.getBusinessKbn()) != null) {
					// DB取得データのコードから汎用コードマスタより文字列取得
					businessKbn = businessKbnList.get(getRowData.getBusinessKbn());
				}
				// 所在地(契約情報取得リスト)
				if (listRowData.containsKey("shatakuAddress") && listRowData.get("shatakuAddress") != null) {
					shatakuAddress = listRowData.get("shatakuAddress").toString();
				}
				// 経理連携用管理番号(DB取得データ)
				if (getRowData.getAssetRegisterNo() != null) {
					assetRegisterNo = getRowData.getAssetRegisterNo();
				}
				// 契約開始日(DB取得データ)
				if (getRowData.getContractStartDate() != null) {
					contractStartDate = getRowData.getContractStartDate();
				}
				// 契約終了日(DB取得データ)
				if (getRowData.getContractEndDate() != null) {
					contractEndDate = getRowData.getContractEndDate();
				}
				// 家賃(DB取得データ)
				if (getRowData.getRent() != null) {
					rent = getRowData.getRent().toString();
				}
				// 共益費(DB取得データ)
				if (getRowData.getKyoekihi() != null) {
					kyoekihi = getRowData.getKyoekihi().toString();
				}
				// 駐車場料(DB取得データ)
				if (getRowData.getLandRent() != null) {
					landRent = getRowData.getLandRent().toString();
				}
			}
			// Excel行データ設定
			rdb.addCellDataBean("B" + rowNo, shatakuKbn);
			rdb.addCellDataBean("C" + rowNo, shatakuName);
			rdb.addCellDataBean("D" + rowNo, roomNo);
			rdb.addCellDataBean("E" + rowNo, contractNo);
			rdb.addCellDataBean("F" + rowNo, ownerName);
			rdb.addCellDataBean("G" + rowNo, ownerAddress);
			rdb.addCellDataBean("H" + rowNo, businessKbn);
			rdb.addCellDataBean("I" + rowNo, shatakuAddress);
			rdb.addCellDataBean("J" + rowNo, assetRegisterNo);
			// 契約開始日データ型判定
			if (contractStartDate.length() > 0) {
				// 日付型(数値型、日付フォーマット指定)でセル追加
				rdb.addCellDataBean(
						"K" + rowNo, 
						skfDateFormatUtils.dateFormatFromString(contractStartDate, SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH),
						Cell.CELL_TYPE_NUMERIC,
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			} else {
				rdb.addCellDataBean("K" + rowNo, contractStartDate);
			}
			// 契約終了日データ型判定
			if (contractEndDate.length() > 0) {
				// 日付型(数値型、日付フォーマット指定)でセル追加
				rdb.addCellDataBean(
						"L" + rowNo, 
						skfDateFormatUtils.dateFormatFromString(contractEndDate, SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH),
						Cell.CELL_TYPE_NUMERIC,
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			} else {
				rdb.addCellDataBean("L" + rowNo, contractEndDate);
			}
			// 家賃データ型判定
			if (landRent.length() > 0) {
				// 数値型でセル追加
				rdb.addCellDataBean("M" + rowNo, rent, Cell.CELL_TYPE_NUMERIC, "#,###");
			} else {
				rdb.addCellDataBean("M" + rowNo, rent);
			}
			// 共益費データ型判定
			if (landRent.length() > 0) {
				// 数値型でセル追加
				rdb.addCellDataBean("N" + rowNo, kyoekihi, Cell.CELL_TYPE_NUMERIC, "#,###");
			} else {
				rdb.addCellDataBean("N" + rowNo, kyoekihi);
			}
			// 駐車場料データ型判定
			if (landRent.length() > 0) {
				// 数値型でセル追加
				rdb.addCellDataBean("O" + rowNo, landRent, Cell.CELL_TYPE_NUMERIC, "#,###");
			} else {
				rdb.addCellDataBean("O" + rowNo, landRent);
			}
			// 行データ追加
			rowDataBeanList.add(rdb);
			// 解放
			getRowData = null;
			rdb = null;
			listRowData = null;
		}
		sheetDataBean.setRowDataBeanList(rowDataBeanList);
		sheetDataBean.setSheetName(excelWorkSheetNameShataku);
		businessKbnList = null;
		rowDataBeanList = null;
		return sheetDataBean;
	}

	/**
	 * 駐車場契約情報出力用Excelワークシート作成
	 * 
	 * 引数の「契約情報取得リスト」、「駐車場契約情報リスト」より駐車場契約情報出力用Excelワークシートを作成する。
	 * 部屋番号は社宅区分が「借上」の場合のみ設定し、「借上」以外の場合は空文字とする。
	 * 賃貸人番号がDBから取得出来ていない場合、下記データは空文字とする。 ・契約番号 ・契約形態 ・賃貸人氏名 ・賃貸人住所 ・法人個人区分 ・所在地
	 * ・所在地 ・経理連携用管理番号 ・契約開始日 ・契約終了日 ・駐車場料（地代）
	 * 
	 * @param getContractList
	 *            契約情報取得リスト
	 * @param getParkingContractList
	 *            駐車場契約情報リスト(DBからの取得値)
	 * @return 駐車場契約情報出力用Excelワークシート
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private SheetDataBean createWorkSheetParkingContract(Map<String, Object> getContractList,
			List<Skf3010Sc001GetParkingContractInfoDataExp> getParkingContractList) throws Exception {

		// 駐車場契約形態区分リスト取得
		Map<String, String> parkingContractKbnList = new HashMap<String, String>();
		parkingContractKbnList = skfGenericCodeUtils
				.getGenericCode(FunctionIdConstant.GENERIC_CODE_PARKING_CONTRACTTYPE_KBN);
		// 個人法人区分リスト取得
		Map<String, String> businessKbnList = new HashMap<String, String>();
		businessKbnList = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_KOJIN_HOJIN_KUBUN);

		// Excelワークシート(契約情報（駐車場）)
		SheetDataBean sheetDataBean = new SheetDataBean();
		// Excel行データ
		List<RowDataBean> rowDataBeanList = new ArrayList<>();

		for (int i = 0, rowNo = 1; i < getParkingContractList.size(); i++, rowNo++) {
			// 設定用ワーク変数
			String shatakuKbn = "";
			String shatakuName = "";
			String roomNo = "";
			String parkingBlockNo = "";
			String contractNo = "";
			String parkingContractKbn = "";
			String ownerName = "";
			String ownerAddress = "";
			String businessKbn = "";
			String parkingAddress = "";
			String parkingName = "";
			String assetRegisterNo = "";
			String contractStartDate = "";
			String contractEndDate = "";
			String landRent = "";

			// 行データ作成
			RowDataBean rdb = new RowDataBean();
			Skf3010Sc001GetParkingContractInfoDataExp getRowData = getParkingContractList.get(i); // DB取得1行
			Map<String, Object> listRowData = null; // 契約情報取得リスト1行

			// 社宅管理番号取得
			if (getRowData.getShatakuKanriNo() == null) {
				logger.error("契約情報取得リストに社宅管理番号なし");
				throw new Exception("契約情報取得リストに社宅管理番号なし");
			}
			if (!getContractList.containsKey(getRowData.getShatakuKanriNo().toString())) {
				logger.error("社宅契約情報リストに社宅管理番号なし");
				throw new Exception("社宅契約情報リストに社宅管理番号なし");
			}
			// 契約情報取得リストより該当社宅管理番号データ取得
			listRowData = (Map<String, Object>) getContractList.get(getRowData.getShatakuKanriNo().toString());

			// 行データ設定
			// 社宅区分(契約情報取得リスト)
			if (listRowData.containsKey("shatakuKbn") && listRowData.get("shatakuKbn") != null) {
				shatakuKbn = listRowData.get("shatakuKbn").toString();
			}
			// 社宅名(DB取得データ)
			if (getRowData.getShatakuName() != null) {
				shatakuName = getRowData.getShatakuName();
			}
			// 部屋番号(社宅区分が「借上」の場合のみ対象とする)
			// 社宅区分判定
			if (listRowData.containsKey("hdnShatakuKbn") && listRowData.get("hdnShatakuKbn") != null
					&& listRowData.get("hdnShatakuKbn").equals(SHATAKU_KBN_KARIAGE)) {
				// 部屋番号設定判定
				if (listRowData.containsKey("hdnRoomNo") && listRowData.get("hdnRoomNo") != null) {
					roomNo = listRowData.get("hdnRoomNo").toString();
				}
			}
			// 区画番号(DB取得データ)
			if (getRowData.getParkingBlockNo() != null) {
				parkingBlockNo = getRowData.getParkingBlockNo();
			}
			// 賃貸人番号取得結果判定
			if (getRowData.getOwnerNo() != null) {
				// 賃貸人番号がnull、または空文字以外の場合
				// 契約番号(DB取得データ)
				if (getRowData.getContractNo() != null) {
					contractNo = getRowData.getContractNo().toString();
				}
				// 駐車場契約形態(DB取得データ)
				if (getRowData.getParkingContractType() != null
						&& parkingContractKbnList.get(getRowData.getParkingContractType()) != null) {
					// DB取得データのコードから汎用コードマスタより文字列取得
					parkingContractKbn = parkingContractKbnList.get(getRowData.getParkingContractType());
				}
				// 賃貸人氏名(DB取得データ)
				if (getRowData.getOwnerName() != null) {
					ownerName = getRowData.getOwnerName();
				}
				// 賃貸人住所(DB取得データ)
				if (getRowData.getOwnerAddress() != null) {
					ownerAddress = getRowData.getOwnerAddress();
				}
				// 法人個人区分(DB取得データ)
				if (getRowData.getBusinessKbn() != null && businessKbnList.get(getRowData.getBusinessKbn()) != null) {
					// DB取得データのコードから汎用コードマスタより文字列取得
					businessKbn = businessKbnList.get(getRowData.getBusinessKbn());
				}
				// 駐車場所在地 (DB取得データ)
				if (getRowData.getParkingAddress() != null) {
					parkingAddress = getRowData.getParkingAddress();
				}
				// 駐車場名(DB取得データ)
				if (getRowData.getParkingName() != null) {
					parkingName = getRowData.getParkingName();
				}
				// 経理連携用管理番号(DB取得データ)
				if (getRowData.getAssetRegisterNo() != null) {
					assetRegisterNo = getRowData.getAssetRegisterNo();
				}
				// 契約開始日(DB取得データ)
				if (getRowData.getContractStartDate() != null) {
					contractStartDate = getRowData.getContractStartDate();
				}
				// 契約終了日(DB取得データ)
				if (getRowData.getContractEndDate() != null) {
					contractEndDate = getRowData.getContractEndDate();
				}
				// 駐車場料(DB取得データ)
				if (getRowData.getLandRent() != null) {
					landRent = getRowData.getLandRent().toString();
				}
			}
			// Excel行データ設定
			rdb.addCellDataBean("B" + rowNo, shatakuKbn);
			rdb.addCellDataBean("C" + rowNo, shatakuName);
			rdb.addCellDataBean("D" + rowNo, roomNo);
			rdb.addCellDataBean("E" + rowNo, parkingBlockNo);
			rdb.addCellDataBean("F" + rowNo, contractNo);
			rdb.addCellDataBean("G" + rowNo, parkingContractKbn);
			rdb.addCellDataBean("H" + rowNo, ownerName);
			rdb.addCellDataBean("I" + rowNo, ownerAddress);
			rdb.addCellDataBean("J" + rowNo, businessKbn);
			rdb.addCellDataBean("K" + rowNo, parkingAddress);
			rdb.addCellDataBean("L" + rowNo, parkingName);
			rdb.addCellDataBean("M" + rowNo, assetRegisterNo);
			// 契約開始日データ型判定
			if (contractStartDate.length() > 0) {
				// 日付型(数値型、日付フォーマット指定)でセル追加
				rdb.addCellDataBean(
						"N" + rowNo, 
						skfDateFormatUtils.dateFormatFromString(contractStartDate, SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH),
						Cell.CELL_TYPE_NUMERIC,
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			} else {
				rdb.addCellDataBean("N" + rowNo, contractStartDate);
			}
			// 契約終了日データ型判定
			if (contractEndDate.length() > 0) {
				// 日付型(数値型、日付フォーマット指定)でセル追加
				rdb.addCellDataBean(
						"O" + rowNo, 
						skfDateFormatUtils.dateFormatFromString(contractEndDate, SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH),
						Cell.CELL_TYPE_NUMERIC,
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			} else {
				rdb.addCellDataBean("O" + rowNo, contractEndDate);
			}
			// 駐車場料データ型判定
			if (landRent.length() > 0) {
				rdb.addCellDataBean("P" + rowNo, landRent, Cell.CELL_TYPE_NUMERIC, "#,###");
			} else {
				rdb.addCellDataBean("P" + rowNo, landRent);
			}
			// 行データ追加
			rowDataBeanList.add(rdb);
			// 解放
			getRowData = null;
			rdb = null;
			listRowData = null;
		}
		sheetDataBean.setRowDataBeanList(rowDataBeanList);
		sheetDataBean.setSheetName(excelWorkSheetNameParking);
		businessKbnList = null;
		rowDataBeanList = null;
		return sheetDataBean;
	}

	/**
	 * 契約情報帳票出力
	 * 
	 * 引数の社宅契約情報、駐車場契約情報のExcelワークシートからExcelブックを作成し、を帳票に出力する
	 * 
	 * @param outShatakuContractList
	 *            社宅契約情報出力用Excelワークシート
	 * @param outParkingContractList
	 *            駐車場契約情報出力用Excelワークシート
	 * @throws Exception
	 */
	private void fileOutPutExcelContractInfo(SheetDataBean shatakuContractWorkSheet,
			SheetDataBean parkingContractWorkSheet, Skf3010Sc001ContractDownLoadDto downloadDto) throws Exception {

		List<SheetDataBean> sheetDataBeanList = new ArrayList<>();
		sheetDataBeanList.add(shatakuContractWorkSheet);
		sheetDataBeanList.add(parkingContractWorkSheet);

		Map<String, Object> cellparams = new HashMap<>(); // 列書式(背景、フォント)
		Map<String, Object> resultMap = new HashMap<>(); // 列書式(背景、フォント)

		String fileName = excelPreFileName + DateTime.now().toString("YYYYMMddHHmmss") + ".xlsx";
		WorkBookDataBean wbdb = new WorkBookDataBean(fileName);
		wbdb.setSheetDataBeanList(sheetDataBeanList);
		// Excelファイルへ出力
		SkfFileOutputUtils.fileOutputExcel(wbdb, cellparams, "skf3010.skf3010_sc001.excelTemplateFile", "SKF3010SC001",
				excelOutPutStartLine, null, resultMap);
		byte[] writeFileData = (byte[]) resultMap.get("fileData");
		downloadDto.setFileData(writeFileData);
		downloadDto.setUploadFileName(fileName);
		downloadDto.setViewPath(NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH);
		// 解放
		sheetDataBeanList = null;
		cellparams = null;
		resultMap = null;
		wbdb = null;
		writeFileData = null;
	}

	/**
	 * 社宅契約情報取得
	 * 
	 * 引数の社宅管理番号リストを検索キーに社宅の契約情報をデータベースより取得する。
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param paramShatakuKanriNoList
	 *            社宅管理番号リスト
	 * @param *listTableData
	 *            社宅契約情報リスト
	 * @return 取得件数
	 */
	public int createShatakuContractTableDataList(List<Long> paramShatakuKanriNoList,
			List<Skf3010Sc001GetShatakuContractInfoDataExp> listTableData) {
		int resultCount = 0;
		List<Skf3010Sc001GetShatakuContractInfoDataExp> getShatakuContractList = new ArrayList<Skf3010Sc001GetShatakuContractInfoDataExp>();

		Skf3010Sc001GetContractInfoDataExpParameter param = new Skf3010Sc001GetContractInfoDataExpParameter();
		param.setShatakuKanriNoList(paramShatakuKanriNoList);
		getShatakuContractList = skf3010Sc001GetShatakuContractInfoDataExpRepository
				.getShatakuContractListTableData(param);

		do {
			// 取得レコード数を設定
			resultCount = getShatakuContractList.size();
			// 取得データレコード数判定
			if (resultCount == 0) {
				// 取得データレコード数が0件の場合、何もせず処理終了
				break;
			}
			// リストテーブルに出力するリストを取得する
			listTableData.clear();
			listTableData.addAll(getShatakuContractList);
		} while (false);
		getShatakuContractList = null;
		param = null;
		return resultCount;
	}

	/**
	 * 駐車場契約情報取得
	 * 
	 * 引数の社宅管理番号リストを検索キーに駐車場の契約情報をデータベースより取得する。
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param paramShatakuKanriNoList
	 *            社宅管理番号リスト
	 * @param *listTableData
	 *            駐車場契約情報リスト
	 * @return 取得件数
	 */
	public int createParkingContractTableDataList(List<Long> paramShatakuKanriNoList,
			List<Skf3010Sc001GetParkingContractInfoDataExp> listTableData) {
		int resultCount = 0;
		List<Skf3010Sc001GetParkingContractInfoDataExp> getParkingContractList = new ArrayList<Skf3010Sc001GetParkingContractInfoDataExp>();

		Skf3010Sc001GetContractInfoDataExpParameter param = new Skf3010Sc001GetContractInfoDataExpParameter();
		param.setShatakuKanriNoList(paramShatakuKanriNoList);
		getParkingContractList = skf3010Sc001GetParkingContractInfoDataExpRepository
				.getParkingContractListTableData(param);

		do {
			// 取得レコード数を設定
			resultCount = getParkingContractList.size();
			// 取得データレコード数判定
			if (resultCount == 0) {
				// 取得データレコード数が0件の場合、何もせず処理終了
				break;
			}
			// リストテーブルに出力するリストを取得する
			listTableData.clear();
			listTableData.addAll(getParkingContractList);
		} while (false);
		getParkingContractList = null;
		param = null;
		return resultCount;
	}
}
