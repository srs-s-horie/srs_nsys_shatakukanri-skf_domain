/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc005;

import static jp.co.c_nexco.nfw.core.constants.CommonConstant.NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.poi_v3_8.ss.usermodel.Cell;
import org.apache.poi_v3_9.ss.usermodel.IndexedColors;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2100Rp001.Skf2100Rp001GetRouterLedgerDataExp;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.LoginUserInfoUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.nfw.webcore.utils.bean.RowDataBean;
import jp.co.c_nexco.nfw.webcore.utils.bean.SheetDataBean;
import jp.co.c_nexco.nfw.webcore.utils.bean.WorkBookDataBean;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc005.Skf2100Sc005DownloadDto;

/**
 * Skf3030Sc001DownloadRp002Service 社宅管理台帳「社宅管理台帳データ」出力Service
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2100Sc005DownloadService extends SkfServiceAbstract<Skf2100Sc005DownloadDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private Skf2100Sc005SharedService skf2100Sc005SharedService;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;

	private static final String FILE_NAME_KEY = "skf2100.skf2100_sc005.router_ledger_template_file";
	private static final int START_LINE = 1;

	/** エクセル出力情報 */
	private enum Rp001Info {

		ROUTER_NO("B", "ＭＳ Ｐゴシック", "11"), // 通しNo
		TEL("C", "ＭＳ Ｐゴシック", "11"), // 電話番号
		ICCID("D", "ＭＳ Ｐゴシック", "11"), // ICCID
		IMEI("E", "ＭＳ Ｐゴシック", "11"), // IMEI
		SSID_A("F", "ＭＳ Ｐゴシック", "11"), // SSID A
		WPA_KEY("G", "ＭＳ Ｐゴシック", "11"), // WPA Key
		ROUTER_ARRIVAL_DATE("H", "ＭＳ Ｐゴシック", "11"), // ルーター入荷日
		ROUTER_CONTRACT_END_DATE("I", "ＭＳ Ｐゴシック", "11"), // モバイルルーター契約終了日
		HANNYU_APPL_DAY("J", "ＭＳ Ｐゴシック", "11"), // 本人申請日
		USE_START_HOPE_DAY("K", "ＭＳ Ｐゴシック", "11"), // 貸出開始希望日
		SHIPPING_DATE("L", "ＭＳ Ｐゴシック", "11"), // 発送日
		RECEIVED_DATE("M", "ＭＳ Ｐゴシック", "11"), // 納品日(受領日）
		HANSHUTU_APPL_DAY("N", "ＭＳ Ｐゴシック", "11"), // 利用停止申請日
		USE_STOP_DAY("O", "ＭＳ Ｐゴシック", "11"), // 利用停止日（最終利用日）
		RETURN_DAY("P", "ＭＳ Ｐゴシック", "11"), // 窓口返却日
		SHAIN_NO("Q", "ＭＳ Ｐゴシック", "11"), // 対象者社員番号
		SHAIN_NAME("R", "ＭＳ Ｐゴシック", "11"), // 対象者氏名
		SHAIN_NAMEKK("S", "ＭＳ Ｐゴシック", "11"), // 対象者フリガナ
		SHAIN_TEL("T", "ＭＳ Ｐゴシック", "11"), // 連絡先
		FAULT_FLAG("U", "ＭＳ Ｐゴシック", "11"), // 故障
		ROUTER_CONTRACT_KBN("V", "ＭＳ Ｐゴシック", "11"), // 契約区分
		ROUTER_LENDING_JUDGMENT("W", "ＭＳ Ｐゴシック", "11"); // 貸出可否判定

		public String col;
		public String font;
		public String size;

		Rp001Info(String col, String font, String size) {
			this.col = col;
			this.font = font;
			this.size = size;
		}
	};

	@Value("${skf2100.skf2100_sc005.router_ledger_output_row_Idx}")
	private String startRowIdx;
	@Value("${skf2100.skf2100_sc005.router_ledger_sheet_name}")
	private String sheetName;
	@Value("${skf2100.skf2100_sc005.router_ledger_data_file_function_id}")
	private String funcId;

	@Override
	protected BaseDto index(Skf2100Sc005DownloadDto inDto) {

		skfOperationLogUtils.setAccessLog("モバイルルーター貸出管理簿出力", CodeConstant.C001, FunctionIdConstant.SKF2100_SC005);

		inDto = (Skf2100Sc005DownloadDto) skf2100Sc005SharedService.setDropDownSelect(inDto);

		inDto.setResultMessages(null);
		inDto.setFileData(null);

		String sysNengetsu = skf2100Sc005SharedService.getSystemProcessNenGetsu();
		String yearMonth = inDto.getHdnYearSelect() + inDto.getHdnMonthSelect();

		String userId = LoginUserInfoUtils.getUserCd();
		
		if (NfwStringUtils.isEmpty(yearMonth) || NfwStringUtils.isEmpty(userId)) {
			LogUtils.debugByMsg("パラメータ不正。対象年月：" + yearMonth + "、 ユーザーID：" + userId);
			ServiceHelper.addErrorResultMessage(inDto, null, MessageIdConstant.E_SKF_1070);
			return inDto;
		}

		LogUtils.debugByMsg("モバイルルーター貸出管理簿取得開始:" + DateTime.now().toString());
		List<Skf2100Rp001GetRouterLedgerDataExp> ledgerData = getRouterLedgerData(yearMonth, sysNengetsu);
		LogUtils.debugByMsg("モバイルルーター貸出管理簿取得終了:" + DateTime.now().toString());
		
		if (ledgerData != null) {
			inDto = outputrouterLedgerData(inDto, ledgerData, yearMonth);
		} else {
			ServiceHelper.addErrorResultMessage(inDto, null, MessageIdConstant.E_SKF_1067, "出力対象データ");
			return inDto;
		}

		if (inDto.getFileData() == null || inDto.getFileData().length == 0) {
			ServiceHelper.addErrorResultMessage(inDto, null, MessageIdConstant.E_SKF_1070);
		}

		return inDto;
	}

	/**
	 * モバイルルーター貸出管理簿データを取得する。
	 * 
	 * @param yearMonth
	 *            対象年月
	 * @param sysNengetsu
	 *            システム処理年月
	 * @return モバイルルーター貸出管理簿
	 */
	private List<Skf2100Rp001GetRouterLedgerDataExp> getRouterLedgerData(String yearMonth, String sysNengetsu) {

		boolean tougetuFlg = false;
		if (Objects.equals(yearMonth, sysNengetsu)) {
			tougetuFlg = true;
		}

		List<Skf2100Rp001GetRouterLedgerDataExp> routerLedgerDataList = skf2100Sc005SharedService
				.getShatakuDaichoInfo(yearMonth, tougetuFlg);

		if (routerLedgerDataList.size() == 0) {
			return null;
		}

		return routerLedgerDataList;
	}

	/**
	 * 社宅管理台帳データを出力する。
	 * 
	 * @param inDto
	 *            Skf2100Sc005DownloadDto
	 * @param routerLedgerDataList
	 *            モバイルルーター貸出管理簿データリスト
	 * @param sysNengetsu
	 *            システム処理年月
	 * @return Skf3030Sc001OutputExcelRp002Dto
	 * @throws Exception
	 */
	private Skf2100Sc005DownloadDto outputrouterLedgerData(Skf2100Sc005DownloadDto inDto,
			List<Skf2100Rp001GetRouterLedgerDataExp> routerLedgerDataList, String sysNengetsu) {

		List<RowDataBean> rowDataBeanList = new ArrayList<>();
		Map<String, Object> cellParams = new HashMap<>();

		LogUtils.debugByMsg("モバイルルーター貸出管理簿設定処理開始:" + DateTime.now().toString());
		
		int startIdx = Integer.parseInt(startRowIdx);

		for (int i = 0; i < routerLedgerDataList.size(); i++) {
			RowDataBean rowData = new RowDataBean();
			String tagetRowIdx = String.valueOf(i + startIdx);

			Skf2100Rp001GetRouterLedgerDataExp data = routerLedgerDataList.get(i);

			cellParams = initCellParams(cellParams, tagetRowIdx);
			// 通しNo
			rowData.addCellDataBean(Rp001Info.ROUTER_NO.col + tagetRowIdx,String.valueOf(data.getMobileRouterNo())
					,Cell.CELL_TYPE_NUMERIC, "#");
			// 電話番号(ルーター)
			rowData.addCellDataBean(Rp001Info.TEL.col + tagetRowIdx,
					skf2100Sc005SharedService.cnvEmptyStrToNull(data.getTel()));
			// ICCID
			rowData.addCellDataBean(Rp001Info.ICCID.col + tagetRowIdx,
					skf2100Sc005SharedService.cnvEmptyStrToNull(data.getIccid()));
			// IMEI
			rowData.addCellDataBean(Rp001Info.IMEI.col + tagetRowIdx,
					skf2100Sc005SharedService.cnvEmptyStrToNull(data.getImei()));
			// SSID
			rowData.addCellDataBean(Rp001Info.SSID_A.col + tagetRowIdx,
					skf2100Sc005SharedService.cnvEmptyStrToNull(data.getSsidA()));
			// WPA Key
			rowData.addCellDataBean(Rp001Info.WPA_KEY.col + tagetRowIdx,
					skf2100Sc005SharedService.cnvEmptyStrToNull(data.getWpaKey()));
			// ルーター入荷日
			if (!NfwStringUtils.isEmpty(data.getRouterArrivalDate())) {
				String arrivalDate = skfDateFormatUtils.dateFormatFromString(data.getRouterArrivalDate(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				rowData.addCellDataBean(Rp001Info.ROUTER_ARRIVAL_DATE.col + tagetRowIdx, arrivalDate,
					Cell.CELL_TYPE_NUMERIC,SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			} else {
				rowData.addCellDataBean(Rp001Info.ROUTER_ARRIVAL_DATE.col + tagetRowIdx, "");
			}
			// モバイルルーター契約終了日
			if (!NfwStringUtils.isEmpty(data.getRouterContractEndDate())) {
				String contractEndDate = skfDateFormatUtils.dateFormatFromString(data.getRouterContractEndDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				rowData.addCellDataBean(Rp001Info.ROUTER_CONTRACT_END_DATE.col + tagetRowIdx, contractEndDate,
					Cell.CELL_TYPE_NUMERIC,SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			} else {
				rowData.addCellDataBean(Rp001Info.ROUTER_CONTRACT_END_DATE.col + tagetRowIdx, "");
			}
			// 本人申請日
			if (!NfwStringUtils.isEmpty(data.getHannyuApplDay())) {
				String hannyuApplDay = skfDateFormatUtils.dateFormatFromString(data.getHannyuApplDay(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				rowData.addCellDataBean(Rp001Info.HANNYU_APPL_DAY.col + tagetRowIdx, hannyuApplDay,
					Cell.CELL_TYPE_NUMERIC,SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			} else {
				rowData.addCellDataBean(Rp001Info.HANNYU_APPL_DAY.col + tagetRowIdx, "");
			}
			// 貸出開始希望日
			if (!NfwStringUtils.isEmpty(data.getUseStartHopeDay())) {
				String useStartHopeDay = skfDateFormatUtils.dateFormatFromString(data.getUseStartHopeDay(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				rowData.addCellDataBean(Rp001Info.USE_START_HOPE_DAY.col + tagetRowIdx, useStartHopeDay,
					Cell.CELL_TYPE_NUMERIC,SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			} else {
				rowData.addCellDataBean(Rp001Info.USE_START_HOPE_DAY.col + tagetRowIdx, "");
			}
			// 発送日
			if (!NfwStringUtils.isEmpty(data.getShippingDate())) {
				String shippingDate = skfDateFormatUtils.dateFormatFromString(data.getShippingDate(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				rowData.addCellDataBean(Rp001Info.SHIPPING_DATE.col + tagetRowIdx, shippingDate,
					Cell.CELL_TYPE_NUMERIC,SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			} else {
				rowData.addCellDataBean(Rp001Info.SHIPPING_DATE.col + tagetRowIdx, "");
			}
			// 納品日(受領日）
			if (!NfwStringUtils.isEmpty(data.getReceivedDate())) {
				String reveivedDate = skfDateFormatUtils.dateFormatFromString(data.getReceivedDate(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				rowData.addCellDataBean(Rp001Info.RECEIVED_DATE.col + tagetRowIdx, reveivedDate,
					Cell.CELL_TYPE_NUMERIC,SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			} else {
				rowData.addCellDataBean(Rp001Info.RECEIVED_DATE.col + tagetRowIdx, "");
			}
			// 利用停止申請日
			if (!NfwStringUtils.isEmpty(data.getHanshutuApplDay())) {
				String hanshutuApplDay = skfDateFormatUtils.dateFormatFromString(data.getHanshutuApplDay(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				rowData.addCellDataBean(Rp001Info.HANSHUTU_APPL_DAY.col + tagetRowIdx, hanshutuApplDay,
					Cell.CELL_TYPE_NUMERIC,SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			} else {
				rowData.addCellDataBean(Rp001Info.HANSHUTU_APPL_DAY.col + tagetRowIdx, "");
			}
			// 利用停止日（最終利用日）
			if (!NfwStringUtils.isEmpty(data.getUseStopDay())) {
				String useStopDay = skfDateFormatUtils.dateFormatFromString(data.getUseStopDay(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				rowData.addCellDataBean(Rp001Info.USE_STOP_DAY.col + tagetRowIdx, useStopDay,
					Cell.CELL_TYPE_NUMERIC,SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			} else {
				rowData.addCellDataBean(Rp001Info.USE_STOP_DAY.col + tagetRowIdx, "");
			}
			// 窓口返却日
			if (!NfwStringUtils.isEmpty(data.getReturnDay())) {
				String returnDay = skfDateFormatUtils.dateFormatFromString(data.getReturnDay(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				rowData.addCellDataBean(Rp001Info.RETURN_DAY.col + tagetRowIdx, returnDay,
					Cell.CELL_TYPE_NUMERIC,SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			} else {
				rowData.addCellDataBean(Rp001Info.RETURN_DAY.col + tagetRowIdx, "");
			}
			// 対象者社員番号
			// WPA Key
			rowData.addCellDataBean(Rp001Info.SHAIN_NO.col + tagetRowIdx,
					skf2100Sc005SharedService.cnvEmptyStrToNull(data.getShainNo()));
			// 対象者氏名
			rowData.addCellDataBean(Rp001Info.SHAIN_NAME.col + tagetRowIdx,
					skf2100Sc005SharedService.cnvEmptyStrToNull(data.getName()));
			// 対象者フリガナ
			rowData.addCellDataBean(Rp001Info.SHAIN_NAMEKK.col + tagetRowIdx,
					skf2100Sc005SharedService.cnvEmptyStrToNull(data.getNamekk()));
			// 連絡先
			// 搬出連絡先を優先
			if (!NfwStringUtils.isEmpty(data.getHansyutuTel())) {
				rowData.addCellDataBean(Rp001Info.SHAIN_TEL.col + tagetRowIdx,
						skf2100Sc005SharedService.cnvEmptyStrToNull(data.getHansyutuTel()));
			}else{
				rowData.addCellDataBean(Rp001Info.SHAIN_TEL.col + tagetRowIdx,
						skf2100Sc005SharedService.cnvEmptyStrToNull(data.getHannyuTel()));
			}
			// 故障
			if(CodeConstant.ROUTER_FAULT_FLAG_FAULT.equals(data.getFaultFlag())){
				// 故障中
				rowData.addCellDataBean(Rp001Info.FAULT_FLAG.col + tagetRowIdx,
						skf2100Sc005SharedService.cnvEmptyStrToNull(CodeConstant.ROUTER_FAULT_STRING));
			}else{
				rowData.addCellDataBean(Rp001Info.FAULT_FLAG.col + tagetRowIdx,"");
			}
			// 契約区分
			Map<String, String> contractKbn = skfGenericCodeUtils
					.getGenericCode(FunctionIdConstant.GENERIC_CODE_ROUTER_CONTRACT_KBN);
				rowData.addCellDataBean(Rp001Info.ROUTER_CONTRACT_KBN.col + tagetRowIdx,
						contractKbn.get(data.getRouterContractKbn()));
			// 貸出可否判定
			Map<String, String> lendJudgment = skfGenericCodeUtils
					.getGenericCode(FunctionIdConstant.GENERIC_CODE_ROUTER_LENDING_JUDGMENT);
				rowData.addCellDataBean(Rp001Info.ROUTER_LENDING_JUDGMENT.col + tagetRowIdx,
						lendJudgment.get(data.getRouterLendingJudgment()));

			rowDataBeanList.add(rowData);

		}

		LogUtils.debugByMsg("データ設定処理終了:" + DateTime.now().toString());
		
		inDto = outputExcel(inDto, rowDataBeanList, cellParams);

		return inDto;
	}

	/**
	 * 「モバイルルーター貸出管理簿」エクセルファイルを出力する。
	 * 
	 * @param inDto
	 *            Skf3030Sc001OutputExcelRp002Dto
	 * @param rowDataBeanList
	 *            行ごとのデータ
	 * @param cellparams
	 *            セルのスタイルパラメータ
	 * @return Skf3030Sc001OutputExcelRp002Dto
	 * @throws Exception
	 */
	private Skf2100Sc005DownloadDto outputExcel(Skf2100Sc005DownloadDto inDto,
			List<RowDataBean> rowDataBeanList, Map<String, Object> cellparams) {

		SheetDataBean sheetDataBean = new SheetDataBean();
		sheetDataBean.setSheetName(sheetName);
		sheetDataBean.setRowDataBeanList(rowDataBeanList);

		List<SheetDataBean> sheetDataBeanList = new ArrayList<>();
		sheetDataBeanList.add(sheetDataBean);

		String fileName = sheetName + DateTime.now().toString("YYYYMMddHHmmss")
				+ CodeConstant.DOT + CodeConstant.EXTENSION_XLSX;
		WorkBookDataBean wbdb = new WorkBookDataBean(fileName);
		wbdb.setSheetDataBeanList(sheetDataBeanList);

		Map<String, Object> resultMap = new HashMap<>();

		try {
			LogUtils.debugByMsg("モバイルルーター貸出管理簿出力処理開始:" + DateTime.now().toString());
			SkfFileOutputUtils.fileOutputKanriDaichoExcel(wbdb, cellparams, FILE_NAME_KEY, 
					funcId, START_LINE, null, resultMap, 6, false);
			LogUtils.debugByMsg("モバイルルーター貸出管理簿出力処理終了:" + DateTime.now().toString());

		} catch (Exception e) {
			//ServiceHelper.addErrorResultMessage(inDto, null, MessageIdConstant.E_SKF_1070);
			LogUtils.infoByMsg("outputExcel, " + e.getMessage());
			sheetDataBean = null;
			sheetDataBeanList = null;
			cellparams = null;
			wbdb = null;
			return inDto;
		}

		byte[] writeFileData = (byte[]) resultMap.get("fileData");
		inDto.setFileData(writeFileData);
		inDto.setUploadFileName(fileName);
		inDto.setViewPath(NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH);

		sheetDataBean = null;
		sheetDataBeanList = null;
		cellparams = null;
		wbdb = null;
		writeFileData = null;

		return inDto;
	}
	/**
	 * セルのスタイル情報を初期化する。
	 * 
	 * @param cellParams
	 *            セルのスタイルパラメータ
	 * @param targetIdx
	 *            対象のインデックス
	 * @return セルのスタイルパラメータ
	 */
	private Map<String, Object> initCellParams(Map<String, Object> cellParams, String targetIdx) {

		Rp001Info[] routerLedgerDataArry = Rp001Info.values();

		for (int i = 0; i < routerLedgerDataArry.length; i++) {
			Map<String, Object> cellParam = new HashMap<>();
			Map<String, Object> fontParam = new HashMap<>();

			String col = routerLedgerDataArry[i].col;
			
			fontParam.put(Skf2100Sc005SharedService.FONT_NAME_KEY, routerLedgerDataArry[i].font);
			fontParam.put(Skf2100Sc005SharedService.FONT_COLOR_KEY, IndexedColors.BLACK.getIndex());
			fontParam.put(Skf2100Sc005SharedService.FONT_SIZE_KEY, Short.parseShort(routerLedgerDataArry[i].size));
			cellParam.put(Skf2100Sc005SharedService.FONT_BG_COLOR_KEY, IndexedColors.WHITE.getIndex());
			cellParam.put(Skf2100Sc005SharedService.FONT_PARAM_KEY, fontParam);
			cellParams.put(col + targetIdx, cellParam);
			
		}

		return cellParams;
	}

}
