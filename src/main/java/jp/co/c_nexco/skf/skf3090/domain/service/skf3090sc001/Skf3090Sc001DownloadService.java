/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc001;

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

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc001.Skf3090Sc001GetGenbutsuShikyuKagakuReportInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc001.Skf3090Sc001GetGenbutsuShikyuKagakuReportInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3090Sc001.Skf3090Sc001GetGenbutsuShikyuKagakuReportInfoExpRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.nfw.webcore.utils.bean.RowDataBean;
import jp.co.c_nexco.nfw.webcore.utils.bean.SheetDataBean;
import jp.co.c_nexco.nfw.webcore.utils.bean.WorkBookDataBean;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc001.Skf3090Sc001DownloadDto;

/**
 * 現物支給価額ました一覧の帳票出力（Download）サービス処理クラス。　 
 * 
 */
@Service
public class Skf3090Sc001DownloadService extends BaseServiceAbstract<Skf3090Sc001DownloadDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
    
	@Autowired
	private Skf3090Sc001SharedService skf3090Sc001SharedService;
	
	@Autowired
	private Skf3090Sc001GetGenbutsuShikyuKagakuReportInfoExpRepository skf3090Sc001GetGenbutsuShikyuKagakuReportInfoExpRepository;
	
	// テンプレートファイル
	private String fileName = "skf3090.skf3090_sc001.FileId";
	
	// ファイル名
	@Value("${skf3090.skf3090_sc001.excelFileName}")
	private String excelFileName;		

	@Value("${skf3090.skf3090_sc001.excelOutPutStartLine}")
	private Integer excelOutPutStartLine;
	
	@Value("${skf3090.skf3090_sc001.excelWorkSheetName}")
	private String excelWorkSheetName;	
	
	private String ABROAD_CD = "48";
	
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
	public Skf3090Sc001DownloadDto index(Skf3090Sc001DownloadDto downloadDto) throws Exception {
		
		downloadDto.setPageTitleKey(MessageIdConstant.SKF3090_SC001_TITLE);
 		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("現物支給価額リスト出力", CodeConstant.C001, downloadDto.getPageId());
		
		// 検索
		// - 検索条件セット
		Skf3090Sc001GetGenbutsuShikyuKagakuReportInfoExpParameter param = new Skf3090Sc001GetGenbutsuShikyuKagakuReportInfoExpParameter();
		param.setEffectiveDateParam(skf3090Sc001SharedService.replaceDateFormat(downloadDto.getHdnEffectiveDate()));
		// -　検索
		List<Skf3090Sc001GetGenbutsuShikyuKagakuReportInfoExp> reportData = new ArrayList<Skf3090Sc001GetGenbutsuShikyuKagakuReportInfoExp>();
		reportData = skf3090Sc001GetGenbutsuShikyuKagakuReportInfoExpRepository.getGenbutsuShikyuKagakuReport(param);
		
		// 現物支給価額リスト
		try{
			SheetDataBean genbutsuShikyuKagakuSheet = null;		
			genbutsuShikyuKagakuSheet = createWorkSheetShikyuKagakuContract(reportData);
			fileOutPutExcelContractInfo(genbutsuShikyuKagakuSheet, downloadDto);
		}catch(Exception ex){
			LogUtils.infoByMsg("index, 例外発生：" + ex.getMessage());
			// 帳票作成時エラー	（error.skf.e_skf_1078={0}エラーが発生しました。ヘルプデスクへ連絡してください。）
			ServiceHelper.addErrorResultMessage(downloadDto, null, MessageIdConstant.E_SKF_1078, "帳票作成");
		}
		
		return downloadDto;
	}	
	
	/**
	 * 現物支給価額マスタのExcelワークシート作成
	 * 
	 * @param reportData	DBからの取得値
	 * @return 出力用Excelワークシート
	 * @throws Exception
	 */
	private SheetDataBean createWorkSheetShikyuKagakuContract(List<Skf3090Sc001GetGenbutsuShikyuKagakuReportInfoExp> reportData) throws Exception {
		
		// Excelワークシート
		SheetDataBean sheetDataBean = new SheetDataBean();
		// Excel行データ
		List<RowDataBean> rowDataBeanList = new ArrayList<>();		
		
		// １回からで埋める
		for(int i = 0; i < Integer.parseInt(ABROAD_CD); i++){
			// 行データ
			RowDataBean rdb = new RowDataBean();
			int rowNo = i + 1;
			if(rowNo == Integer.parseInt(ABROAD_CD)){
				continue;
			}
			rdb.addCellDataBean("C" + rowNo, "", Cell.CELL_TYPE_NUMERIC, "#,###");
			rdb.addCellDataBean("D" + rowNo, "");
			rowDataBeanList.add(rdb);			
		}
		
		for(int i = 0; i < reportData.size(); i++){
			// 行データ
			RowDataBean rdb = new RowDataBean();
			Skf3090Sc001GetGenbutsuShikyuKagakuReportInfoExp getRowData = reportData.get(i); // DB取得1行
//			Map<String, Object> listRowData = null; // リスト1行

			//// 都道府県コードが一致しない場合はスキップ
			//int targetPrefCd = i + 1;
			//if(false == String.valueOf(targetPrefCd).equals(getRowData.getPrefCd())){
			//	continue;
			//}
			
			// 都道府県コードが48（海外の社宅用のため）は除外
			if(ABROAD_CD.equals(getRowData.getPrefCd())){
				continue;
			}
			
			//int rowNo = Integer.parseInt(getRowData.getPrefCd()) + 1;
			int rowNo = Integer.parseInt(getRowData.getPrefCd());
			
			// C列（現物支給価額）
			if(false == CheckUtils.isEmpty(getRowData.getKyojuRiekigaku())){
				// 空じゃないときに設定
				rdb.addCellDataBean("C" + rowNo, getRowData.getKyojuRiekigaku(), Cell.CELL_TYPE_NUMERIC, "#,###");
			}else{
				rdb.addCellDataBean("C" + rowNo, "0", Cell.CELL_TYPE_NUMERIC, "#,###");
			}
			// D列（改定日）
			if(false == CheckUtils.isEmpty(getRowData.getEffectiveDate())){
				// 空じゃないときに設定
				rdb.addCellDataBean(
						"D" + rowNo, 
						skfDateFormatUtils.dateFormatFromString(getRowData.getEffectiveDate(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH),
						Cell.CELL_TYPE_NUMERIC,
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}else{
				rdb.addCellDataBean("D" + rowNo, "");
			}
			// 行データ追加
			rowDataBeanList.add(rdb);			
		}
		sheetDataBean.setRowDataBeanList(rowDataBeanList);	
		sheetDataBean.setSheetName(excelWorkSheetName);		
		return sheetDataBean;
	}
	
	
	/**
	 * 現物支給価額リスト帳票出力
	 * 
	 * @param genbutsuShikyuKagakuSheet	現物支給価額リストワークシート
	 * @param downloadDto	DTO
	 * @throws Exception
	 */
	private void fileOutPutExcelContractInfo(SheetDataBean genbutsuShikyuKagakuSheet,
			Skf3090Sc001DownloadDto downloadDto) throws Exception {

		List<SheetDataBean> sheetDataBeanList = new ArrayList<>();
		sheetDataBeanList.add(genbutsuShikyuKagakuSheet);

		Map<String, Object> cellparams = new HashMap<>(); // 列書式(背景、フォント)
		Map<String, Object> resultMap = new HashMap<>(); // 列書式(背景、フォント)

		String uploadFileName = excelFileName + DateTime.now().toString("YYYYMMddHHmmss") + ".xlsx";
		WorkBookDataBean wbdb = new WorkBookDataBean(uploadFileName);
		wbdb.setSheetDataBeanList(sheetDataBeanList);
		// Excelファイルへ出力
		SkfFileOutputUtils.fileOutputExcel(wbdb, cellparams, fileName, "skf3090rp001",
				excelOutPutStartLine, null, resultMap);
		byte[] writeFileData = (byte[]) resultMap.get("fileData");
		downloadDto.setFileData(writeFileData);
		downloadDto.setUploadFileName(uploadFileName);
		downloadDto.setViewPath(NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH);
	}	
}
