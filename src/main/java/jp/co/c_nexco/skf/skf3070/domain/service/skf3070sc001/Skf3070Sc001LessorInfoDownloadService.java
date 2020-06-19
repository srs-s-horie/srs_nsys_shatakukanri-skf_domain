/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3070.domain.service.skf3070sc001;

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
import org.springframework.web.util.HtmlUtils;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3070Rp002.Skf3070Rp002GetOwnerInfoListExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3070Rp002.Skf3070Rp002GetOwnerInfoListExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3070Rp002.Skf3070Rp002GetOwnerInfoListExpRepository;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.nfw.webcore.utils.bean.RowDataBean;
import jp.co.c_nexco.nfw.webcore.utils.bean.SheetDataBean;
import jp.co.c_nexco.nfw.webcore.utils.bean.WorkBookDataBean;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3070.domain.dto.skf3070sc001.Skf3070Sc001LessorInfoDownloadDto;

/**
 * Skf3070Sc001 法定調書データ管理画面の賃貸人（代理人）情報出力処理クラス。
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf3070Sc001LessorInfoDownloadService extends SkfServiceAbstract<Skf3070Sc001LessorInfoDownloadDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private Skf3070Rp002GetOwnerInfoListExpRepository skf3070Rp002GetOwnerInfoListExpRepository;
	@Value("${skf3070.skf3070_sc001.lessorInfoExcelOutPutStartLine}")
	private Integer excelOutPutStartLine;
	@Value("${skf3070.skf3070_sc001.lessorInfoExcelPreFileName}")
	private String excelPreFileName;
	@Value("${skf3070.skf3070_sc001.lessorInfoExcelWorkSheetName}")
	private String excelWorkSheetName;
	
	private String companyCd = CodeConstant.C001;
	/**
	 * サービス処理を行う。
	 * 
	 * @param Skf3070Sc001LessorInfoDownloadDto lessorDlDto
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public BaseDto index(Skf3070Sc001LessorInfoDownloadDto lessorDlDto) throws Exception {
		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("賃貸人（代理人）情報出力", companyCd, FunctionIdConstant.SKF3070_SC001);
		
		// 賃貸人（代理人）情報リストを取得
		List<Skf3070Rp002GetOwnerInfoListExp>  ownerInfoList = new ArrayList<Skf3070Rp002GetOwnerInfoListExp>();
		Skf3070Rp002GetOwnerInfoListExpParameter param = new Skf3070Rp002GetOwnerInfoListExpParameter();
		param.setRecodeDadeto(lessorDlDto.getRecodeDadeto());
		param.setRecodeDadefrom(lessorDlDto.getRecodeDadefrom());
		ownerInfoList = skf3070Rp002GetOwnerInfoListExpRepository.getOwnerInfoList(param);
		
		// 賃貸人（代理人）情報が存在しない場合
		if(ownerInfoList == null || ownerInfoList.size() == 0){
			//エラーメッセージ出力
			ServiceHelper.addErrorResultMessage(lessorDlDto, null, MessageIdConstant.E_SKF_1029);
			throwBusinessExceptionIfErrors(lessorDlDto.getResultMessages());
		}
		
		// 賃貸人（代理人）情報出力用Excelワークシート作成
		SheetDataBean sheetDataBean = new SheetDataBean();
		sheetDataBean = this.createWorkSheetLessorInfo(ownerInfoList);
		// 帳票作成
		this.fileOutPutExcelContractInfo(sheetDataBean, lessorDlDto);


		return lessorDlDto;

	}
	
	
	/**
	 * 賃貸人（代理人）情報出力用Excelワークシート作成
	 * 
	 * 賃貸人（代理人）情報より賃貸人（代理人）情報出力用Excelワークシートを作成する。
	 * 
	 * @param ownerInfoList			賃貸人（代理人）情報リスト
	 * @return 賃貸人（代理人）情報出力用Excelワークシート
	 * @throws Exception
	 */
	public SheetDataBean createWorkSheetLessorInfo(List<Skf3070Rp002GetOwnerInfoListExp> ownerInfoList) throws Exception {
		
		//Excelシート行数
		int rowNo= 1 ;
		
		// Excelワークシート(賃貸人（代理人）情報)
		SheetDataBean sheetDataBean = new SheetDataBean();
		
		// Excel行データ
		List<RowDataBean> rowDataBeanList = new ArrayList<>();

		for(Skf3070Rp002GetOwnerInfoListExp ownerInfo : ownerInfoList){
			
			// 氏名又は名称
			String ownerName = CodeConstant.NONE;
			if(ownerInfo.getOwnerName() != null){
				ownerName = HtmlUtils.htmlUnescape(ownerInfo.getOwnerName());
			}		
			// 氏名又は名称（フリガナ）
			String ownerNameKk = CodeConstant.NONE;
			if(ownerInfo.getOwnerNameKk() != null){
				ownerNameKk = HtmlUtils.htmlUnescape(ownerInfo.getOwnerNameKk());
			}
			// 郵便番号
			String zipCd = CodeConstant.NONE;
			if(ownerInfo.getZipCd() != null){
				zipCd = HtmlUtils.htmlUnescape(ownerInfo.getZipCd());
			}
			// 住所
			String address = CodeConstant.NONE;
			if(ownerInfo.getAddress() != null){
				address = HtmlUtils.htmlUnescape(ownerInfo.getAddress());
			}
			// 個人法人区分
			String businessKbn = CodeConstant.NONE;
			if(ownerInfo.getBusinessKbn() != null){
				businessKbn = HtmlUtils.htmlUnescape(skfGenericCodeUtils.getGenericCodeNameReverse(
						FunctionIdConstant.GENERIC_CODE_KOJIN_HOJIN_KUBUN, ownerInfo.getBusinessKbn()));
			}
			// 個人番号
			String acceptFlg = CodeConstant.NONE;
			if(ownerInfo.getAcceptFlg() != null){
				acceptFlg = HtmlUtils.htmlUnescape(skfGenericCodeUtils.getGenericCodeNameReverse(
						FunctionIdConstant.GENERIC_CODE_AGENT_INDIVIDUAL_NUMBER_DEMAND_SITUATION,
						ownerInfo.getAcceptFlg()));
			}
			// 督促状況
			String acceptStatus = CodeConstant.NONE;
			if(ownerInfo.getAcceptStatus() != null){
				acceptStatus = HtmlUtils.htmlUnescape(ownerInfo.getAcceptStatus());
			}
			// 所有物件数
			String count = String.valueOf(ownerInfo.getCount());
			// 備考
			String remarks = CodeConstant.NONE;
			if(ownerInfo.getRemarks() != null){
				remarks = HtmlUtils.htmlUnescape(ownerInfo.getRemarks());
			}
			// 行データ
			RowDataBean rdb = new RowDataBean();
			// Excel行データ設定
			rdb.addCellDataBean("A" + rowNo, ownerName);
			rdb.addCellDataBean("B" + rowNo, ownerNameKk);
			rdb.addCellDataBean("C" + rowNo, zipCd);
			rdb.addCellDataBean("D" + rowNo, address);
			rdb.addCellDataBean("E" + rowNo, businessKbn);
			rdb.addCellDataBean("F" + rowNo, acceptFlg);
			rdb.addCellDataBean("G" + rowNo, acceptStatus);
			rdb.addCellDataBean("H" + rowNo, count,Cell.CELL_TYPE_NUMERIC);
			rdb.addCellDataBean("I" + rowNo, remarks);
			
			rowDataBeanList.add(rdb);
			rowNo++;
		}
		
		sheetDataBean.setRowDataBeanList(rowDataBeanList);
		sheetDataBean.setSheetName(excelWorkSheetName);
		
		
		return sheetDataBean;
	}
	
	
	/**
	 * 賃貸人（代理人）情報帳票出力
	 * 
	 * 賃貸人（代理人）情報のExcelワークシートからExcelブックを作成し、を帳票に出力する
	 * 
	 * @param sheetDataBean	賃貸人（代理人）情報出力用Excelワークシート
	 * @throws Exception
	 */
	public void fileOutPutExcelContractInfo(SheetDataBean sheetDataBean, Skf3070Sc001LessorInfoDownloadDto lessorDlDto) throws Exception {
		
		List<SheetDataBean> sheetDataBeanList = new ArrayList<>();
		sheetDataBeanList.add(sheetDataBean);
		
		Map<String, Object> cellparams = new HashMap<>(); // 列書式(背景、フォント)
		Map<String, Object> resultMap = new HashMap<>(); // 作成結果Map
		
		String fileName = excelPreFileName + DateTime.now().toString("YYYYMMddHHmmss") + ".xlsx";
		WorkBookDataBean wbdb = new WorkBookDataBean(fileName);
		wbdb.setSheetDataBeanList(sheetDataBeanList);
		
		// Excelファイルへ出力
		SkfFileOutputUtils.fileOutputExcel(wbdb, cellparams, "skf3070.skf3070_sc001.lessorInfoExcelTemplateFile", "SKF3070RP002",
				excelOutPutStartLine, null, resultMap);
		byte[] writeFileData = (byte[]) resultMap.get("fileData");
		lessorDlDto.setFileData(writeFileData);
		lessorDlDto.setUploadFileName(fileName);
		lessorDlDto.setViewPath(NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH);

	}

}
