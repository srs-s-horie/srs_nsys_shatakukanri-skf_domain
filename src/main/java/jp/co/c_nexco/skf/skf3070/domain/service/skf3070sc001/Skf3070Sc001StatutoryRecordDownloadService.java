/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3070.domain.service.skf3070sc001;

import static jp.co.c_nexco.nfw.core.constants.CommonConstant.NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi_v3_8.hssf.util.CellReference;
import org.apache.poi_v3_9.ss.usermodel.Cell;
import org.apache.poi_v3_9.ss.usermodel.IndexedColors;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3070Rp001.Skf3070Rp001GetStatutoryRecordDataListExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3070Rp001.Skf3070Rp001GetStatutoryRecordDataListExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3070Sc001.Skf3070Sc001GetOwnerContractListExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3070Sc001.Skf3070Sc001GetOwnerContractListExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3070Rp001.Skf3070Rp001GetStatutoryRecordDataListExpRepository;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.nfw.webcore.utils.bean.CellDataBean;
import jp.co.c_nexco.nfw.webcore.utils.bean.RowDataBean;
import jp.co.c_nexco.nfw.webcore.utils.bean.SheetDataBean;
import jp.co.c_nexco.nfw.webcore.utils.bean.WorkBookDataBean;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3070.domain.dto.skf3070sc001.Skf3070Sc001StatutoryRecordDownloadDto;

/**
 * Skf3070Sc001 法定調書データ管理画面の法定調書データ出力処理クラス。
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf3070Sc001StatutoryRecordDownloadService
		extends BaseServiceAbstract<Skf3070Sc001StatutoryRecordDownloadDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private MenuScopeSessionBean sessionBean;
	@Autowired
	private Skf3070Sc001SharedService skf3070Sc001SharedService;
	@Autowired
	private Skf3070Rp001GetStatutoryRecordDataListExpRepository skf3070Rp001GetStatutoryRecordDataListExpRepository;
	
	@Value("${skf3070.skf3070_sc001.statutoryRecordExcelOutPutStartLine}")
	private Integer excelOutPutStartLine;
	@Value("${skf3070.skf3070_sc001.statutoryRecordExcelPreFileName}")
	private String excelPreFileName;
	@Value("${skf3070.skf3070_sc001.statutoryRecordExcelWorkSheetName}")
	private String excelWorkSheetName;
	
	private String companyCd = CodeConstant.C001;
	
	private String shatakuContractKbn = "1";
	private String parkingContractKbn = "2";
	
	/**
	 * サービス処理を行う。
	 * 
	 * @param Skf3070Sc001StatutoryRecordDownloadDto sRDto
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public BaseDto index(Skf3070Sc001StatutoryRecordDownloadDto sRDto) throws Exception {
		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("法定調書データ出力", companyCd, sRDto.getPageId());
		
		List<Skf3070Sc001GetOwnerContractListExp> ownerExpList = new ArrayList<Skf3070Sc001GetOwnerContractListExp>();
		//セッションからSQLパラメータ情報を取得
		Skf3070Sc001GetOwnerContractListExpParameter param = 
				(Skf3070Sc001GetOwnerContractListExpParameter)sessionBean.get(SessionCacheKeyConstant.SKF3070SC001_SEARCH_COND_SESSION_KEY);
		//賃貸人（代理人）契約情報取得
		ownerExpList = skf3070Sc001SharedService.getOwnerContractInfo(param);

		//賃貸人（代理人）契約情報が存在しない場合
		if(ownerExpList == null || ownerExpList.size() == 0){
			//エラーメッセージ出力
			ServiceHelper.addErrorResultMessage(sRDto, null, MessageIdConstant.E_SKF_1029);
			throwBusinessExceptionIfErrors(sRDto.getResultMessages());
		}
		LogUtils.debugByMsg("ownerExpList長さ："+ownerExpList.size());
		
		// 法定調書データ情報出力用Excelワークシート作成
		SheetDataBean sheetDataBean = new SheetDataBean();
		sheetDataBean = this.createWorkSheetStatutoryRecord(ownerExpList, sRDto);
		
		// 帳票作成
		this.fileOutPutExcelContractInfo(sheetDataBean, sRDto);


		return sRDto;

	}
	
	/**
	 * 法定調書データ情報出力用Excelワークシート作成
	 * 
	 * 検索結果一覧情報より法定調書データ情報出力用Excelワークシートを作成する。
	 * 
	 * @param listTableData			検索結果一覧情報リスト
	 * @return 法定調書データ情報出力用Excelワークシート
	 * @throws Exception
	 */
	public SheetDataBean createWorkSheetStatutoryRecord(List<Skf3070Sc001GetOwnerContractListExp> ownerExpList, Skf3070Sc001StatutoryRecordDownloadDto sRDto) throws Exception {
		
		//Excelシート行数
		int rowNo= 1 ;
		
		// Excelワークシート(法定調書データ)
		SheetDataBean sheetDataBean = new SheetDataBean();
		
		// Excel行データ
		List<RowDataBean> rowDataBeanList = new ArrayList<>();
		
		List<Skf3070Rp001GetStatutoryRecordDataListExp> statutoryRecordList = new ArrayList<Skf3070Rp001GetStatutoryRecordDataListExp>();
		
		
		for(Skf3070Sc001GetOwnerContractListExp ownerExp : ownerExpList){
			// 駐車場管理番号
			// TODO 後でもらえる。。。。
			long parkingKanriNo = 1;
			
			
			
			//法定調書データ取得
			List<Skf3070Rp001GetStatutoryRecordDataListExp> statutoryRecordDataList = new ArrayList<Skf3070Rp001GetStatutoryRecordDataListExp>();
			Skf3070Rp001GetStatutoryRecordDataListExpParameter param2 = new Skf3070Rp001GetStatutoryRecordDataListExpParameter();
			// 会社コード
			param2.setCompanyCd(companyCd);
			// 社宅管理番号
			param2.setShatakuKanriNo(ownerExp.getShatakuKanriNo());
			// 法定調書データ番号
			param2.setOwnerNo(Long.parseLong(ownerExp.getOwnerNo()));
			// 契約区分
			param2.setContractKbn(ownerExp.getContractKbn());
			if(parkingKanriNo != 0){
				param2.setParkingKanriNo(parkingKanriNo);
			}
			statutoryRecordDataList = skf3070Rp001GetStatutoryRecordDataListExpRepository.getStatutoryRecordInfo(param2);
			//法定調書データが取得出来なかった場合
			if(statutoryRecordDataList == null || statutoryRecordDataList.size() == 0){
				//エラーメッセージ出力
				ServiceHelper.addErrorResultMessage(sRDto, null, MessageIdConstant.E_SKF_1070);
				throwBusinessExceptionIfErrors(sRDto.getResultMessages());
			}
			
			if(statutoryRecordDataList.get(0).getShatakuKbn().equals(CodeConstant.ITTOU)){
				statutoryRecordList.add(statutoryRecordDataList.get(0));
			}else{
				for(Skf3070Rp001GetStatutoryRecordDataListExp statutoryRecordData : statutoryRecordDataList){
					statutoryRecordList.add(statutoryRecordData);
				}
			}
		}
		
		// ソートを行う
		statutoryRecordList = this.sortStatutoryRecordList(statutoryRecordList);
		
		for(Skf3070Rp001GetStatutoryRecordDataListExp statutoryRecordData : statutoryRecordList){
			LogUtils.debugByMsg("社宅管理番号："+statutoryRecordData.getShatakuKanriNo()+ "\t法定調書データ番号:"+statutoryRecordData.getOwnerNo()+"\t部屋番号:"+statutoryRecordData.getRoomNo()+"\tcontractKbn:"+statutoryRecordData.getContractKbn());

			//区分
			String[] kubun = {"家賃","共益費","地代","更新料","礼金"};

			// 社宅（contract_kbn=1)
			if(statutoryRecordData.getContractKbn().equals(shatakuContractKbn)){
				for(int i = 0; i < kubun.length; i++){
					// 行データ
					RowDataBean rdb = new RowDataBean();
					// Excel行データ設定
					//　管理機関コード
					rdb.addCellDataBean("A" + rowNo, statutoryRecordData.getManegeAgencyCd());
					//　管理機関
					rdb.addCellDataBean("B" + rowNo, statutoryRecordData.getAgencyName());
					// 経理連携用管理番号
					rdb.addCellDataBean("C" + rowNo, statutoryRecordData.getAssetRegisterNo());
					// 住所・居所・所在地
					rdb.addCellDataBean("D" + rowNo, statutoryRecordData.getJusho());
					// 氏名・名称
					rdb.addCellDataBean("E" + rowNo, statutoryRecordData.getOwnerName());
					// 区分
					rdb.addCellDataBean("F" + rowNo, kubun[i]);

					// 物件の所在地(「所在地」＋「社宅名」＋「部屋番号」で表記)(一棟借上の場合、部屋番号は出力しない)
					String bukkenShozaichi = CodeConstant.NONE;
					if(statutoryRecordData.getShatakuKbn().equals(CodeConstant.ITTOU)){
						bukkenShozaichi = statutoryRecordData.getShozaichi()+statutoryRecordData.getShatakuName();
						rdb.addCellDataBean("G" + rowNo, bukkenShozaichi);
					}else{
						bukkenShozaichi = statutoryRecordData.getShozaichi()+statutoryRecordData.getShatakuName()+statutoryRecordData.getRoomNo();
						rdb.addCellDataBean("G" + rowNo, bukkenShozaichi);
					}
					// 細目
					String structureSupplement = CodeConstant.NONE;
					//5行のうち、3行目は "駐車場" 固定 それ以外は細目
					switch(i){
					case 2:
						structureSupplement = "駐車場";
						break;
					default:
						structureSupplement = statutoryRecordData.getStructureSupplement();
						break;
					}
					rdb.addCellDataBean("H" + rowNo, structureSupplement);

					// 賃貸借期間　初月
					String contractStartMonth = skfDateFormatUtils.dateFormatFromString(statutoryRecordData.getContractStartDate(), "MM");
					rdb.addCellDataBean("I" + rowNo, contractStartMonth, Cell.CELL_TYPE_NUMERIC);
					// ～ 
					rdb.addCellDataBean("J" + rowNo, "～");
					// 賃貸借期間　終月
					String contractEndMonth = skfDateFormatUtils.dateFormatFromString(statutoryRecordData.getContractEndDate(), "MM");
					rdb.addCellDataBean("K" + rowNo, contractEndMonth, Cell.CELL_TYPE_NUMERIC);

					// 延面積(一棟借上の場合、各部屋の合計)
					switch(i){
					case 0:
						double originalMenseki;
						if(statutoryRecordData.getShatakuKbn().equals(CodeConstant.ITTOU)){
							originalMenseki = statutoryRecordData.getTotalOriginalMenseki();
						}else{
							originalMenseki = statutoryRecordData.getOriginalMenseki();
						}
						DecimalFormat formatter = new DecimalFormat("###.##");
						rdb.addCellDataBean("L" + rowNo, formatter.format(originalMenseki), Cell.CELL_TYPE_NUMERIC, "###.##");
						break;
					default:
						rdb.addCellDataBean("L" + rowNo, CodeConstant.NONE);
						break;
					}


					// 賃貸月額(5行のうち、1行目は家賃、2行目は共益費、3行目は地代、4・5行目は空白)
					String rentMonth = CodeConstant.NONE;;
					switch(i){
					case 0:
						rentMonth = String.valueOf(statutoryRecordData.getRent());
						rdb.addCellDataBean("M" + rowNo, rentMonth, Cell.CELL_TYPE_NUMERIC, "###,###");
						break;
					case 1:
						rentMonth = String.valueOf(statutoryRecordData.getKyoekihi());
						rdb.addCellDataBean("M" + rowNo, rentMonth, Cell.CELL_TYPE_NUMERIC, "###,###");
						break;
					case 2:
						rentMonth = String.valueOf(statutoryRecordData.getLandRent());
						rdb.addCellDataBean("M" + rowNo, rentMonth, Cell.CELL_TYPE_NUMERIC, "###,###");
						break;
					default:
						rdb.addCellDataBean("M" + rowNo, rentMonth);
						break;
					}

					// 支払い金額は空白
					rdb.addCellDataBean("N" + rowNo, CodeConstant.NONE);
					// 借地権の存続期間
					rdb.addCellDataBean("O" + rowNo, statutoryRecordData.getRemarks());
					// あっせん等をした者　住所・居所・所在地は空白
					rdb.addCellDataBean("P" + rowNo, CodeConstant.NONE);
					// あっせん等をした者　氏名・名称は空白
					rdb.addCellDataBean("Q" + rowNo, CodeConstant.NONE);
					// 支払年月日は空白
					rdb.addCellDataBean("R" + rowNo, CodeConstant.NONE);
					// あっせん手数料は空白
					rdb.addCellDataBean("S" + rowNo, CodeConstant.NONE);
					// 個人番号または法人番号は空白
					rdb.addCellDataBean("T" + rowNo, CodeConstant.NONE);

					// 個人番号の提出状況の確認
					switch(i){
					case 0:
					String acceptFlgName = skfGenericCodeUtils.getGenericCodeNameReverse(
							FunctionIdConstant.GENERIC_CODE_AGENT_INDIVIDUAL_NUMBER_DEMAND_SITUATION,
							statutoryRecordData.getAcceptFlg());
					rdb.addCellDataBean("U" + rowNo, acceptFlgName);
					break;
					default:
						rdb.addCellDataBean("U" + rowNo, CodeConstant.NONE);
						break;
					}
					// 契約期間　開始日
					String contractStartDate = skfDateFormatUtils.dateFormatFromString(statutoryRecordData.getContractStartDate(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
					rdb.addCellDataBean("V" + rowNo, contractStartDate, Cell.CELL_TYPE_NUMERIC, SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
					// 契約期間　終了日
					String contractEndDate = skfDateFormatUtils.dateFormatFromString(statutoryRecordData.getContractEndDate(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
					rdb.addCellDataBean("W" + rowNo, contractEndDate, Cell.CELL_TYPE_NUMERIC, SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);

					// 備考欄
					rdb.addCellDataBean("X" + rowNo, CodeConstant.NONE);

					rowDataBeanList.add(rdb);
					rowNo++;	
				}
				
			// 駐車場（contract_kbn=2)(「駐車場住所」＋「駐車場名」で表記)
			}else if(statutoryRecordData.getContractKbn().equals(parkingContractKbn)){
				for(int i = 0; i < kubun.length; i++){
					// 行データ
					RowDataBean rdb = new RowDataBean();
					// Excel行データ設定
					//　管理機関コード
					rdb.addCellDataBean("A" + rowNo, statutoryRecordData.getManegeAgencyCd());
					//　管理機関
					rdb.addCellDataBean("B" + rowNo, statutoryRecordData.getAgencyName());
					// 経理連携用管理番号
					rdb.addCellDataBean("C" + rowNo, statutoryRecordData.getAssetRegisterNo());
					// 住所・居所・所在地
					rdb.addCellDataBean("D" + rowNo, statutoryRecordData.getJusho());
					// 氏名・名称
					rdb.addCellDataBean("E" + rowNo, statutoryRecordData.getOwnerName());
					// 区分
					rdb.addCellDataBean("F" + rowNo, kubun[i]);

					// 物件の所在地(「駐車場住所」＋「駐車場名」で表記)(3行目だけ)
					switch(i){
					case 2:
						String parkingShozaichi = statutoryRecordData.getParkingAddress()+statutoryRecordData.getParkingName();
						rdb.addCellDataBean("G" + rowNo, parkingShozaichi);
						break;
					default:
						rdb.addCellDataBean("G" + rowNo, CodeConstant.NONE);
						break;
					}

					// 細目(3行目だけ かつ "駐車場")
					String structureSupplement = CodeConstant.NONE;
					switch(i){
					case 2:
						structureSupplement = "駐車場";
						break;
					default:
						break;
					}
					rdb.addCellDataBean("H" + rowNo, structureSupplement);

					// 賃貸借期間　初月(3行目だけ)
					String contractStartMonth = CodeConstant.NONE;
					switch(i){
					case 2:
						contractStartMonth = skfDateFormatUtils.dateFormatFromString(statutoryRecordData.getContractStartDate(), "MM");
						break;
					default:
						break;
					}
					rdb.addCellDataBean("I" + rowNo, contractStartMonth, Cell.CELL_TYPE_NUMERIC);
					
					// ～ 
					rdb.addCellDataBean("J" + rowNo, "～");
					
					// 賃貸借期間　終月(3行目だけ)
					String contractEndMonth = CodeConstant.NONE;
					switch(i){
					case 2:
						contractEndMonth = skfDateFormatUtils.dateFormatFromString(statutoryRecordData.getContractEndDate(), "MM");
						break;
					default:
						break;
					}
					rdb.addCellDataBean("K" + rowNo, contractEndMonth, Cell.CELL_TYPE_NUMERIC);

					// 延面積は空白
					rdb.addCellDataBean("L" + rowNo, CodeConstant.NONE);

					// 賃貸月額(3行目だけ　かつ　駐車場料(地代))
					String rentMonth = CodeConstant.NONE;
					switch(i){
					case 2:
						rentMonth = String.valueOf(statutoryRecordData.getLandRent());
						break;
					default:
						break;
					}
					rdb.addCellDataBean("M" + rowNo, rentMonth, Cell.CELL_TYPE_NUMERIC, "###,###");

					// 支払い金額は空白
					rdb.addCellDataBean("N" + rowNo, CodeConstant.NONE);
					// 借地権の存続期間(5行のうち、3行目は借地権の存続期間  それ以外は空白)
					switch(i){
					case 2:
						rdb.addCellDataBean("O" + rowNo, statutoryRecordData.getRemarks());
						break;
					default:
						rdb.addCellDataBean("O" + rowNo, CodeConstant.NONE);
						break;
					}
					// あっせん等をした者　住所・居所・所在地は空白
					rdb.addCellDataBean("P" + rowNo, CodeConstant.NONE);
					// あっせん等をした者　氏名・名称は空白
					rdb.addCellDataBean("Q" + rowNo, CodeConstant.NONE);
					// 支払年月日は空白
					rdb.addCellDataBean("R" + rowNo, CodeConstant.NONE);
					// あっせん手数料は空白
					rdb.addCellDataBean("S" + rowNo, CodeConstant.NONE);
					// 個人番号または法人番号は空白
					rdb.addCellDataBean("T" + rowNo, CodeConstant.NONE);

					// 個人番号の提出状況の確認(5行のうち、3行目は個人番号の提出状況の確認  それ以外は空白)
					String acceptFlgName = CodeConstant.NONE;
					switch(i){
					case 2:
						acceptFlgName = skfGenericCodeUtils.getGenericCodeNameReverse(
								FunctionIdConstant.GENERIC_CODE_AGENT_INDIVIDUAL_NUMBER_DEMAND_SITUATION,
								statutoryRecordData.getAcceptFlg());
						break;
					default:
						break;
					}
					rdb.addCellDataBean("U" + rowNo, acceptFlgName);
					
					// 契約期間　開始日(5行のうち、3行目は契約期間　開始日  それ以外は空白)
					String contractStartDate = CodeConstant.NONE;
					switch(i){
					case 2:
						contractStartDate = skfDateFormatUtils.dateFormatFromString(statutoryRecordData.getContractStartDate(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
						break;
					default:
						break;
					}
					rdb.addCellDataBean("V" + rowNo, contractStartDate, Cell.CELL_TYPE_NUMERIC, SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
					
					// 契約期間　終了日(5行のうち、3行目は契約期間　開始日  それ以外は空白)
					String contractEndDate = CodeConstant.NONE;
					switch(i){
					case 2:
						contractEndDate = skfDateFormatUtils.dateFormatFromString(statutoryRecordData.getContractEndDate(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
						break;
					default:
						break;
					}
					rdb.addCellDataBean("W" + rowNo, contractEndDate, Cell.CELL_TYPE_NUMERIC, SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);

					// 備考欄
					rdb.addCellDataBean("X" + rowNo, CodeConstant.NONE);

					rowDataBeanList.add(rdb);
					rowNo++;		
				}
			}
		}

		sheetDataBean.setRowDataBeanList(rowDataBeanList);
		sheetDataBean.setSheetName(excelWorkSheetName);


		return sheetDataBean;
	}

	/**
	 * 法定調書データ帳票出力
	 * 
	 * 法定調書データのExcelワークシートからExcelブックを作成し、を帳票に出力する
	 * 
	 * @param sheetDataBean	法定調書データ出力用Excelワークシート
	 * @throws Exception
	 */
	public void fileOutPutExcelContractInfo(SheetDataBean sheetDataBean, Skf3070Sc001StatutoryRecordDownloadDto sRDto) throws Exception {

		List<SheetDataBean> sheetDataBeanList = new ArrayList<>();
		sheetDataBeanList.add(sheetDataBean);

		Map<String, Object> cellparams = new HashMap<>(); // 列書式(背景、フォント)
		Map<String, Object> resultMap = new HashMap<>(); // 作成結果Map

		String fileName = excelPreFileName + DateTime.now().toString("YYYYMMddHHmmss") + ".xlsx";
		WorkBookDataBean wbdb = new WorkBookDataBean(fileName);
		wbdb.setSheetDataBeanList(sheetDataBeanList);
		
		// 法定調書データExcelシートの背景色を設定するセル項目を取得する
		cellparams = this.getBgColorCellList(sheetDataBean);
				

		// Excelファイルへ出力
		SkfFileOutputUtils.fileOutputExcel(wbdb, cellparams, "skf3070.skf3070_sc001.statutoryRecordExcelTemplateFile", "SKF3070RP001",
				excelOutPutStartLine, null, resultMap);
		byte[] writeFileData = (byte[]) resultMap.get("fileData");
		sRDto.setFileData(writeFileData);
		sRDto.setUploadFileName(fileName);
		sRDto.setViewPath(NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH);

	}
	
	/**
	 * 法定調書データソート
	 * 
	 * 第1ソート「管理機関」第2ソート「社宅名」の昇順にソートを行う
	 * 
	 * @param statutoryRecordList	法定調書データリスト(ソート前)
	 * @return statutoryRecordList	法定調書データリスト(ソート後)
	 * @throws Exception
	 */
	public List<Skf3070Rp001GetStatutoryRecordDataListExp> sortStatutoryRecordList(List<Skf3070Rp001GetStatutoryRecordDataListExp> statutoryRecordList) throws Exception {
	
		// 社宅名ソート
		statutoryRecordList.sort((a,b)-> a.getShatakuName().compareTo(b.getShatakuName()) );
		// 管理機関コードソート
		statutoryRecordList.sort((a,b)-> a.getManegeAgencyCd().compareTo(b.getManegeAgencyCd()) );
		
		return statutoryRecordList;
		
		
	}
	
	/**
	 * 法定調書データExcelシートの背景色を設定するセル項目を取得する
	 * 
	 * @param sheetDataBean
	 * @return colorCellList　背景色を設定するセル項目リスト
	 */
	public Map<String, Object> getBgColorCellList(SheetDataBean sheetDataBean){

		Map<String, Object> cellparams = new HashMap<>();
		
		for (RowDataBean rowDataBean : sheetDataBean.getRowDataBeanList()) {
			for (CellDataBean cellDataBean : rowDataBean.getCellDataBeanList()) {

				CellReference cellReference = new CellReference(cellDataBean.getAddress());
				int rowIndex = cellReference.getRow() + excelOutPutStartLine-1;
				short colIndex = cellReference.getCol();
				String cellPosition = cellReference.formatAsString();
				Map<String, Object> cellparam = new HashMap<>();

				switch(colIndex){
				case 12:
					//1セット分の情報の4,5行目の「賃料月額」列に色を付ける(仕様上最初の1,2行が入ってしまうため取り除く)
					if((rowIndex != 0 && rowIndex != 1) && (rowIndex%5 == 0 || rowIndex%5 == 1)){
						cellparam.put("bgcolor", IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
						cellparams.put(cellPosition, cellparam);
					}
					break;
				case 13:
				case 15:
				case 16:
				case 17:
				case 18:
				case 19:
				case 23:
					//1セット分の情報の各列に色を付ける(仕様上最初の1,2行が入ってしまうため取り除く)
					if(rowIndex != 0 && rowIndex != 1){
						cellparam.put("bgcolor", IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
						cellparams.put(cellPosition, cellparam);
					}
					break;
				default:
					break;
				}
			}
		}
		return cellparams;
	}

}

