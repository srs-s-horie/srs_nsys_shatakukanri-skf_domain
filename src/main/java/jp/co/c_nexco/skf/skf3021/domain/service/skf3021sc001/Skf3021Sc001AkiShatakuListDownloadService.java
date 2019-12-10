/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3021.domain.service.skf3021sc001;

import static jp.co.c_nexco.nfw.core.constants.CommonConstant.NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi_v3_8.ss.usermodel.Cell;
import org.apache.poi_v3_8.ss.usermodel.IndexedColors;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3021Rp001.Skf3021Rp001GetNyutaikyoYoteiInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Rp001.Skf3030Rp001AkiShatakuListExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Rp001.Skf3030Rp001GetAkiShatakuInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Rp001.Skf3030Rp001GetParkingContractInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Rp001.Skf3030Rp001GetParkingContractInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Rp001.Skf3030Rp001GetAkiShatakuInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Rp001.Skf3030Rp001GetParkingContractInfoExpRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.nfw.webcore.utils.bean.RowDataBean;
import jp.co.c_nexco.nfw.webcore.utils.bean.SheetDataBean;
import jp.co.c_nexco.nfw.webcore.utils.bean.WorkBookDataBean;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3021.domain.dto.skf3021sc001.Skf3021Sc001AkiShatakuListDownloadDto;


/**
 * 入退居予定一覧画面の空き社宅リスト出力サービス処理クラス。　 
 * 
 */
@Service
public class Skf3021Sc001AkiShatakuListDownloadService extends BaseServiceAbstract<Skf3021Sc001AkiShatakuListDownloadDto> {
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private Skf3030Rp001GetAkiShatakuInfoExpRepository skf3030Rp001GetAkiShatakuInfoExpRepository;
	@Autowired
	private Skf3030Rp001GetParkingContractInfoExpRepository skf3030Rp001GetParkingContractInfoExpRepository;

	//社宅と別契約
	public static final String CONTRACT_TYPE_2 = "2";
	
	// システム連携フラグ(0固定でよいと思うが一応フラグのまま)
	@Value("${skf.common.jss_link_flg}")
	private String jssLinkFlg;
	@Value("${skf3030.skf3030_sc001.excelTemplateFile}")
	private String excelTemplateFile;
	@Value("${skf3030.skf3030_sc001.excelOutPutStartLine}")
	private Integer excelOutPutStartLine;
	@Value("${skf3030.skf3030_sc001.excelPreFileName}")
	private String excelPreFileName;
	@Value("${skf3030.skf3030_sc001.excelWorkSheetName}")
	private String excelWorkSheetName;
	
	/**
	 * サービス処理を行う。　
	 * 
	 * @param downloadDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public Skf3021Sc001AkiShatakuListDownloadDto index(Skf3021Sc001AkiShatakuListDownloadDto downloadDto) throws Exception {
		
		downloadDto.setPageTitleKey(MessageIdConstant.SKF3021_SC001_TITLE);
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("空き社宅リスト出力", CodeConstant.C001, downloadDto.getPageId());

		Skf3021Rp001GetNyutaikyoYoteiInfoExpParameter param = new Skf3021Rp001GetNyutaikyoYoteiInfoExpParameter();
		param.setJssLinkFlg(jssLinkFlg);
		
		//GetAkiShatakuData
		//引数チェック(userIdとIPなので割愛
		List<Skf3030Rp001AkiShatakuListExp> listDt = new ArrayList<Skf3030Rp001AkiShatakuListExp>();
		
		//空き社宅データ件数取得
		int dataCount = skf3030Rp001GetAkiShatakuInfoExpRepository.getAkiShatakuCount();
		LogUtils.debugByMsg("取得件数:" + dataCount);
		//件数チェック
		if(dataCount == 0)
		{
			LogUtils.debugByMsg("取得件数0件終了");
			ServiceHelper.addErrorResultMessage(downloadDto, null, MessageIdConstant.E_SKF_1067, "出力対象データ");
			return downloadDto;
		}
		//システム処理年月取得
		String yearMonth = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu();
		
		//空き社宅データ取得
		List<Skf3030Rp001GetAkiShatakuInfoExp> dt = new ArrayList<Skf3030Rp001GetAkiShatakuInfoExp>();
		dt = skf3030Rp001GetAkiShatakuInfoExpRepository.getAkiShatakuInfo(yearMonth);

		for(Skf3030Rp001GetAkiShatakuInfoExp dr : dt){
			//帳票用データ作成
			listDt.add(createChohyoData(dr));
		}
		
		//Excel作成
		SheetDataBean workSheet = createAkiShatakuList(listDt);
		//Excel出力
		fileOutPutExcel(workSheet,downloadDto);
		
		return downloadDto;
	}
	
	/**
	 * 帳票用データ作成
	 * @param shatakuRow 空き社宅データ
	 * @return 空き社宅リスト
	 */
	private Skf3030Rp001AkiShatakuListExp createChohyoData(Skf3030Rp001GetAkiShatakuInfoExp shatakuRow){

		//社宅区分
		Map<String, String> genericCodeShatakuKbn = new HashMap<String, String>();
		genericCodeShatakuKbn = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_SHATAKU_KBN);
		//本来用途
		Map<String, String> genericCodeAuseKbn = new HashMap<String, String>();
		genericCodeAuseKbn = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_AUSE_KBN);
		//本来規格
		Map<String, String> genericCodeKikakuKbn = new HashMap<String, String>();
		genericCodeKikakuKbn = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_KIKAKU_KBN);
		//都道府県
		Map<String, String> genericCodePrefCd = new HashMap<String, String>();
		genericCodePrefCd = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_PREFCD);

		//空き社宅リスト listRow
		Skf3030Rp001AkiShatakuListExp listRow = new Skf3030Rp001AkiShatakuListExp();
		
//		//空き区分
//		String vacancyKbn = CodeConstant.DOUBLE_QUOTATION;
//		if(AKI_KBN_AKI.equals(shatakuRow.getAkiKbn())){
//			vacancyKbn = AKI_KBN_NM_AKI;
//		}else if(AKI_KBN_YOTEI.equals(shatakuRow.getAkiKbn())){
//			vacancyKbn = AKI_KBN_NM_YOTEI;
//		}
//		listRow.setVacancyKbn(vacancyKbn);
		//社宅名
		listRow.setShatakuName(shatakuRow.getShatakuName());
		//部屋番号
		listRow.setRoomNo(shatakuRow.getRoomNo());
		//部屋の備考
		listRow.setRoomBiko(shatakuRow.getRoomBiko());
		//管理機関
		listRow.setManegeAgency(shatakuRow.getAgencyName());
		//社宅区分(コードに対する名称)
		listRow.setShatakuKbn(genericCodeShatakuKbn.get(shatakuRow.getShatakuKbn()));
		//本来用途(コードに対する名称)
		listRow.setOriginalAuse(genericCodeAuseKbn.get(shatakuRow.getOriginalAuse()));
		//住所
		String pref = genericCodePrefCd.get(shatakuRow.getPrefCd());
		String shatakuAddress = pref + shatakuRow.getAddress();
		listRow.setShatakuAddress(shatakuAddress);
		//本来規格（本来規格）(コードに対する名称)
		listRow.setOriginalKikaku(genericCodeKikakuKbn.get(shatakuRow.getOriginalKikaku()));
		//面積
		listRow.setOriginalMenseki(shatakuRow.getOriginalMenseki());
		//社宅区分が借上の場合
		if(CodeConstant.KARIAGE.equals(shatakuRow.getShatakuKbn())){
			//賃料
			listRow.setRent(shatakuRow.getRent());
			//共益費
			listRow.setKyoekihi(shatakuRow.getKyoekihi());
			
			//駐車場契約情報取得
			Skf3030Rp001GetParkingContractInfoExpParameter pcKey = new Skf3030Rp001GetParkingContractInfoExpParameter();
			pcKey.setShatakuKanriNo(shatakuRow.getShatakuKanriNo());
			pcKey.setParkingKanriNo(1L);
			List<Skf3030Rp001GetParkingContractInfoExp> contractInfo = new ArrayList<Skf3030Rp001GetParkingContractInfoExp>();
			contractInfo = skf3030Rp001GetParkingContractInfoExpRepository.getParkingContractInfoExp(pcKey);
			if(contractInfo.size() > 0){
				if(CONTRACT_TYPE_2.equals(contractInfo.get(0).getParkingContractType())){
					//社宅と別契約の場合：社宅駐車場契約.駐車場料(地代)
					listRow.setLandRent(contractInfo.get(0).getLandRent());
				}else{
					//一括：社宅契約.駐車場料(地代)
					listRow.setLandRent(shatakuRow.getLandRent());
				}
			}
//			else{
//				//契約情報無し：社宅契約.駐車場料(地代)
//			}
		}else{
			//賃料
			listRow.setRent(null);
			//共益費
			listRow.setKyoekihi(null);
			//駐車場料
			listRow.setLandRent(null);
		}
		//社員番号
		listRow.setShainNo(shatakuRow.getShainNo());
		//社員氏名
		listRow.setName(shatakuRow.getName());
		//現所属
		listRow.setNowAffiliation(shatakuRow.getNowAffiliation());
		//退居予定日
		listRow.setTaikyoYoteiDate(shatakuRow.getTaikyoYoteiDate());
		
		return listRow;
	}


	
	/**
	 * 空き社宅リスト(Excel)作成
	 * @param dt 空き社宅リスト出力用データ
	 * @return
	 */
	private SheetDataBean createAkiShatakuList(List<Skf3030Rp001AkiShatakuListExp> dt){
		
		// Excelワークシート
		SheetDataBean sheetDataBean = new SheetDataBean();
		// Excel行データ
		List<RowDataBean> rowDataBeanList = new ArrayList<>();
		
		//出力日時
		RowDataBean rdbDate = new RowDataBean();
		rdbDate.addCellDataBean("B3", DateTime.now().toString("出力日時:YYYY/MM/dd HH:mm:ss") );//入居予定日
		rowDataBeanList.add(rdbDate);
			
		for (int i = 0, rowNo = 6; i < dt.size(); i++, rowNo++) {
			// 行データ
			RowDataBean rdb = new RowDataBean();
			Skf3030Rp001AkiShatakuListExp getRowData = dt.get(i);
			
			// Excel行データ設定
			rdb.addCellDataBean("B" + rowNo, getRowData.getShatakuName());//社宅名
			rdb.addCellDataBean("C" + rowNo, getRowData.getRoomNo());//部屋番号
			rdb.addCellDataBean("D" + rowNo, getRowData.getRoomBiko());//部屋の備考
			rdb.addCellDataBean("E" + rowNo, getRowData.getManegeAgency());//管理機関
			rdb.addCellDataBean("F" + rowNo, getRowData.getShatakuKbn());//社宅区分
			rdb.addCellDataBean("G" + rowNo, getRowData.getOriginalAuse());//本来用途
			rdb.addCellDataBean("H" + rowNo, getRowData.getShatakuAddress());//住所
			rdb.addCellDataBean("I" + rowNo, getRowData.getOriginalKikaku());//本来規格
			//面積
			String menseki = CodeConstant.DOUBLE_QUOTATION;
			if(getRowData.getOriginalMenseki() != null){
				menseki = getRowData.getOriginalMenseki().toPlainString();
			}
			rdb.addCellDataBean("J" + rowNo, menseki, Cell.CELL_TYPE_NUMERIC, "#,###.##");//面積
			//賃料
			String rent = CodeConstant.DOUBLE_QUOTATION;
			if(getRowData.getRent() != null){
				rent = getRowData.getRent().toString();
			}
			rdb.addCellDataBean("K" + rowNo, rent, Cell.CELL_TYPE_NUMERIC, "#,###");
			//共益費
			String kyoekihi = CodeConstant.DOUBLE_QUOTATION;
			if(getRowData.getKyoekihi() != null){
				kyoekihi = getRowData.getKyoekihi().toString();
			}
			rdb.addCellDataBean("L" + rowNo, kyoekihi, Cell.CELL_TYPE_NUMERIC, "#,###");
			//駐車場料
			String landRent = CodeConstant.DOUBLE_QUOTATION;
			if(getRowData.getLandRent() != null){
				landRent = getRowData.getLandRent().toString();
			}
			rdb.addCellDataBean("M" + rowNo, landRent, Cell.CELL_TYPE_NUMERIC, "#,###");
			rdb.addCellDataBean("N" + rowNo, getRowData.getShainNo());//社員番号
			rdb.addCellDataBean("O" + rowNo, getRowData.getName());//社員氏名
			rdb.addCellDataBean("P" + rowNo, getRowData.getNowAffiliation());//現所属
			//退居予定日
			String taikyoYoteiDate = CodeConstant.DOUBLE_QUOTATION;
			if(!SkfCheckUtils.isNullOrEmpty(getRowData.getTaikyoYoteiDate())){
				taikyoYoteiDate = skfDateFormatUtils.dateFormatFromString(getRowData.getTaikyoYoteiDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}
			rdb.addCellDataBean("Q" + rowNo, taikyoYoteiDate,
					Cell.CELL_TYPE_NUMERIC,
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
					// 行データ追加
			rowDataBeanList.add(rdb);
		}
		
		sheetDataBean.setRowDataBeanList(rowDataBeanList);
		sheetDataBean.setSheetName(excelWorkSheetName);
		
		return sheetDataBean;
	}
	
	/**
	 * 空き社宅リストExcelファイル出力
	 * @param workSheet
	 * @param downloadDto
	 * @throws Exception
	 */
	private void fileOutPutExcel(SheetDataBean workSheet,
			Skf3021Sc001AkiShatakuListDownloadDto downloadDto) throws Exception {

		List<SheetDataBean> sheetDataBeanList = new ArrayList<>();
		sheetDataBeanList.add(workSheet);

		Map<String, Object> cellparams = new HashMap<>(); // 列書式(背景、フォント)
		Map<String, Object> resultMap = new HashMap<>(); // 列書式(背景、フォント)

		String fileName = excelPreFileName + DateTime.now().toString("YYYYMMddHHmmss") + ".xlsx";
		WorkBookDataBean wbdb = new WorkBookDataBean(fileName);
		wbdb.setSheetDataBeanList(sheetDataBeanList);
		
		Map<String, Object> cellparam = new HashMap<>();
		Map<String, Object> fontparam = new HashMap<>();
		fontparam.put("fontName", "ＭＳ Ｐゴシック");
		fontparam.put("fontColor", IndexedColors.BLACK.getIndex());
		fontparam.put("fontSize", (short) 10);
		cellparam.put("font", fontparam);
		cellparams.put("B3", cellparam);
		
		// Excelファイルへ出力
		SkfFileOutputUtils.fileOutputExcel(wbdb, cellparams, "skf3030.skf3030_sc001.excelTemplateFile", "SKF3030RP001",
				excelOutPutStartLine, null, resultMap,6);
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

}
