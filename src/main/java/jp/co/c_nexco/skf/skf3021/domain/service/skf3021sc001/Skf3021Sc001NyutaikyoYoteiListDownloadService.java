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
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3021Rp001.Skf3021Rp001GetCurrentShatakuInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3021Rp001.Skf3021Rp001GetCurrentShatakuInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3021Rp001.Skf3021Rp001GetNyutaikyoYoteiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3021Rp001.Skf3021Rp001GetNyutaikyoYoteiInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3021Rp001.Skf3021Rp001GetShatakuLedgerInfoByshainNoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3021Rp001.Skf3021Rp001GetShatakuLedgerInfoByshainNoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3021Rp001.Skf3021Rp001NyutaikyoYoteiListExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchi;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchiKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReport;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReportKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3021Rp001.Skf3021Rp001GetCurrentShatakuInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3021Rp001.Skf3021Rp001GetNyutaikyoYoteiInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3021Rp001.Skf3021Rp001GetShatakuLedgerInfoByshainNoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2020TNyukyoChoshoTsuchiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2040TTaikyoReportRepository;
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
import jp.co.c_nexco.skf.skf3021.domain.dto.skf3021sc001.Skf3021Sc001NyutaikyoYoteiListDownloadDto;


/**
 * 入退居予定一覧画面の入退居予定リスト出力サービス処理クラス。　 
 * 
 */
@Service
public class Skf3021Sc001NyutaikyoYoteiListDownloadService extends BaseServiceAbstract<Skf3021Sc001NyutaikyoYoteiListDownloadDto> {
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private Skf3021Rp001GetNyutaikyoYoteiInfoExpRepository skf3021Rp001GetNyutaikyoYoteiInfoExpRepository;
	@Autowired
	private Skf3021Rp001GetCurrentShatakuInfoExpRepository skf3021Rp001GetCurrentShatakuInfoExpRepository;
	@Autowired
	private Skf3021Rp001GetShatakuLedgerInfoByshainNoExpRepository skf3021Rp001GetShatakuLedgerInfoByshainNoExpRepository;
	@Autowired
	private Skf2020TNyukyoChoshoTsuchiRepository skf2020TNyukyoChoshoTsuchiRepository;
	@Autowired
	private Skf2040TTaikyoReportRepository skf2040TTaikyoReportRepository;
	//入退居フラグ(ON=1)
	private static final String FLG_ON = "1"; 
	//仮社員
	private static final String STRING_K = "K";
	//居住者区分 -本人同居
	private static final String HONNIN_DOKYO = "1";
	//居住者区分 -留守家族
	private static final String RUSU_KAZOKU = "2";
	//入居
	private static final String NYUTAIKYO_NYUKYO = "入居";
	//退居
	private static final String NYUTAIKYO_TAIKYO = "退居";
	
	// システム連携フラグ(0固定でよいと思うが一応フラグのまま)
	@Value("${skf.common.jss_link_flg}")
	private String jssLinkFlg;
	@Value("${skf3021.skf3021_sc001.excelTemplateFile}")
	private String excelTemplateFile;
	@Value("${skf3021.skf3021_sc001.excelOutPutStartLine}")
	private Integer excelOutPutStartLine;
	@Value("${skf3021.skf3021_sc001.excelPreFileName}")
	private String excelPreFileName;
	@Value("${skf3021.skf3021_sc001.excelWorkSheetName}")
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
	public Skf3021Sc001NyutaikyoYoteiListDownloadDto index(Skf3021Sc001NyutaikyoYoteiListDownloadDto downloadDto) throws Exception {
		
		downloadDto.setPageTitleKey(MessageIdConstant.SKF3021_SC001_TITLE);
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("入退居予定リスト出力", CodeConstant.C001, downloadDto.getPageId());

		Skf3021Rp001GetNyutaikyoYoteiInfoExpParameter param = new Skf3021Rp001GetNyutaikyoYoteiInfoExpParameter();
		param.setJssLinkFlg(jssLinkFlg);
		
		//入退居予定データ件数取得
		int dataCount = skf3021Rp001GetNyutaikyoYoteiInfoExpRepository.getNyutaikyoYoteiCount(param);
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
		
		//入退居予定データ取得
		List<Skf3021Rp001GetNyutaikyoYoteiInfoExp> dt1 = new ArrayList<Skf3021Rp001GetNyutaikyoYoteiInfoExp>();
		dt1 = skf3021Rp001GetNyutaikyoYoteiInfoExpRepository.getNyutaikyoYoteiInfo(param);
		
		List<Skf3021Rp001NyutaikyoYoteiListExp> listDt = new ArrayList<Skf3021Rp001NyutaikyoYoteiListExp>();
		
		//入居
		for(Skf3021Rp001GetNyutaikyoYoteiInfoExp nyukyoDr : dt1){
			if(!FLG_ON.equals(nyukyoDr.getNyukyoFlg())){
				continue;
			}
			
			//入居希望等調書・入居決定通知取得
			String applNo = nyukyoDr.getApplNo();
			Skf2020TNyukyoChoshoTsuchi dt2Row = null;
			if(!SkfCheckUtils.isNullOrEmpty(applNo)){
				Skf2020TNyukyoChoshoTsuchiKey nctKey = new Skf2020TNyukyoChoshoTsuchiKey();
				nctKey.setCompanyCd(CodeConstant.C001);
				nctKey.setApplNo(applNo);
				dt2Row = skf2020TNyukyoChoshoTsuchiRepository.selectByPrimaryKey(nctKey);
				if(dt2Row == null){
					//ServiceHelper.addResultMessage(downloadDto, MessageIdConstant.I_SKF_1066, "入居希望等調書・入居決定通知情報の取得"));
					LogUtils.error(Skf3021Sc001NyutaikyoYoteiListDownloadService.class, "入居希望等調書・入居決定通知情報の取得に失敗しました。 " + applNo);
					continue;
				}
				
			}

			//現社宅情報取得
			String shatakuNo = CodeConstant.DOUBLE_QUOTATION;
			String roomNo = CodeConstant.DOUBLE_QUOTATION;
			String shainNo = nyukyoDr.getShainNo();
			List<Skf3021Rp001GetCurrentShatakuInfoExp> dt4 = new ArrayList<Skf3021Rp001GetCurrentShatakuInfoExp>();
			Skf3021Rp001GetCurrentShatakuInfoExp dt4Row = null;
			Skf3021Rp001GetCurrentShatakuInfoExpParameter currentKey = new Skf3021Rp001GetCurrentShatakuInfoExpParameter();
			//申請ありの場合
			if(!SkfCheckUtils.isNullOrEmpty(applNo)){
				String nowShatakuKanriNo = null;
				if(dt2Row.getNowShatakuKanriNo() != null){
					nowShatakuKanriNo = dt2Row.getNowShatakuKanriNo().toString();
				}
				String nowRoomKanriNo = null;
				if(dt2Row.getNowRoomKanriNo() != null){
					nowRoomKanriNo = dt2Row.getNowRoomKanriNo().toString();
				}
			
				if(!SkfCheckUtils.isNullOrEmpty(nowShatakuKanriNo) && !SkfCheckUtils.isNullOrEmpty(nowRoomKanriNo)){
					
					currentKey.setShatakuKanriNo(Long.parseLong(nowShatakuKanriNo));
					currentKey.setShatakuRoomKanriNo(Long.parseLong(nowRoomKanriNo));
					currentKey.setShainNo(shainNo);
					currentKey.setYearMonth(yearMonth);
					dt4 = skf3021Rp001GetCurrentShatakuInfoExpRepository.getCurrentShatakuInfo(currentKey);
					if(dt4.size() > 0){
						dt4Row = dt4.get(0);
					}
				}
			}else if(shainNo.startsWith(STRING_K)){
				//申請ない、且つ、正社員番号の場合
				Map<String,Object> noList = getShainRoomInfo(shainNo);
				shatakuNo = noList.get("shatakuNo").toString();
				roomNo = noList.get("roomNo").toString();
				
				if(!SkfCheckUtils.isNullOrEmpty(shatakuNo) && !SkfCheckUtils.isNullOrEmpty(roomNo)){
					currentKey.setShatakuKanriNo(Long.parseLong(shatakuNo));
					currentKey.setShatakuRoomKanriNo(Long.parseLong(roomNo));
					currentKey.setShainNo(shainNo);
					currentKey.setYearMonth(yearMonth);
					dt4 = skf3021Rp001GetCurrentShatakuInfoExpRepository.getCurrentShatakuInfo(currentKey);
					if(dt4.size() > 0){
						dt4Row = dt4.get(0);
					}
				}
				
			}
			
			//帳票用データ作成
			Skf3021Rp001NyutaikyoYoteiListExp listRow = createChohyoData(nyukyoDr, dt2Row, dt4Row, null);
			listDt.add(listRow);
		}
		
		
		//退居
		for(Skf3021Rp001GetNyutaikyoYoteiInfoExp nyukyoDr : dt1){
			if(FLG_ON.equals(nyukyoDr.getNyukyoFlg()) || !FLG_ON.equals(nyukyoDr.getTaikyoFlg())){
				continue;
			}
			//退居（自動車の保管場所返還）届取得
			String applNo = nyukyoDr.getApplNo();
			Skf2040TTaikyoReport dt2Row = null;
			if(!SkfCheckUtils.isNullOrEmpty(applNo)){
				Skf2040TTaikyoReportKey trKey = new Skf2040TTaikyoReportKey();
				trKey.setCompanyCd(CodeConstant.C001);
				trKey.setApplNo(applNo);
				dt2Row = skf2040TTaikyoReportRepository.selectByPrimaryKey(trKey);
				if(dt2Row == null){
					LogUtils.error(Skf3021Sc001NyutaikyoYoteiListDownloadService.class, "退居（自動車の保管場所返還）届情報の取得に失敗しました。 " + applNo);
					continue;
				}
			}
			
			//現社宅情報取得
			String shatakuNo = CodeConstant.DOUBLE_QUOTATION;
			String roomNo = CodeConstant.DOUBLE_QUOTATION;
			String shainNo = nyukyoDr.getShainNo();
			List<Skf3021Rp001GetCurrentShatakuInfoExp> dt4 = new ArrayList<Skf3021Rp001GetCurrentShatakuInfoExp>();
			Skf3021Rp001GetCurrentShatakuInfoExp dt4Row = null;
			Skf3021Rp001GetCurrentShatakuInfoExpParameter currentKey = new Skf3021Rp001GetCurrentShatakuInfoExpParameter();
			//申請ありの場合
			if(!SkfCheckUtils.isNullOrEmpty(applNo)){
				String shatakuKanriNo = null;
				if(dt2Row.getShatakuNo() != null){
					shatakuKanriNo = dt2Row.getShatakuNo().toString();
				}
				String roomKanriNo = null;
				if(dt2Row.getRoomKanriNo() != null){
					roomKanriNo = dt2Row.getRoomKanriNo().toString();
				}
			
				if(!SkfCheckUtils.isNullOrEmpty(shatakuKanriNo) && !SkfCheckUtils.isNullOrEmpty(roomKanriNo)){
					
					currentKey.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
					currentKey.setShatakuRoomKanriNo(Long.parseLong(roomKanriNo));
					currentKey.setShainNo(shainNo);
					currentKey.setYearMonth(yearMonth);
					dt4 = skf3021Rp001GetCurrentShatakuInfoExpRepository.getCurrentShatakuInfo(currentKey);
					if(dt4.size() > 0){
						dt4Row = dt4.get(0);
					}
				}
			}else if(shainNo.startsWith(STRING_K)){
				//申請ない、且つ、正社員番号の場合
				Map<String,Object> noList = getShainRoomInfo(shainNo);
				shatakuNo = noList.get("shatakuNo").toString();
				roomNo = noList.get("roomNo").toString();
				
				if(!SkfCheckUtils.isNullOrEmpty(shatakuNo) && !SkfCheckUtils.isNullOrEmpty(roomNo)){
					currentKey.setShatakuKanriNo(Long.parseLong(shatakuNo));
					currentKey.setShatakuRoomKanriNo(Long.parseLong(roomNo));
					currentKey.setShainNo(shainNo);
					currentKey.setYearMonth(yearMonth);
					dt4 = skf3021Rp001GetCurrentShatakuInfoExpRepository.getCurrentShatakuInfo(currentKey);
					if(dt4.size() > 0){
						dt4Row = dt4.get(0);
					}
				}
				
			}
			
			//帳票用データ作成
			Skf3021Rp001NyutaikyoYoteiListExp listRow = createChohyoData(nyukyoDr, null, dt4Row, dt2Row);
			listDt.add(listRow);
		}
		
		if(listDt.size() == 0){
			LogUtils.debugByMsg("取得件数0件終了");
			ServiceHelper.addErrorResultMessage(downloadDto, null, MessageIdConstant.E_SKF_1067, "出力対象データ");
			return downloadDto;
		}

		//Excel作成
		SheetDataBean workSheet = createNyutaikyoYoteiList(listDt);
		//Excel出力
		fileOutPutExcel(workSheet,downloadDto);
		
		return downloadDto;
	}
	
	
	/**
	 * 申請なしの社員の社宅情報を取得する
	 * @param shainNo
	 * @param mailList
	 */
	private Map<String,Object> getShainRoomInfo(String shainNo){
		
		Map<String,Object> noList = new HashMap<String,Object>();
		String shatakuNo = CodeConstant.DOUBLE_QUOTATION;
		String roomNo = CodeConstant.DOUBLE_QUOTATION;
		
		Skf3021Rp001GetShatakuLedgerInfoByshainNoExpParameter skParam = new Skf3021Rp001GetShatakuLedgerInfoByshainNoExpParameter();
		List<Skf3021Rp001GetShatakuLedgerInfoByshainNoExp> skDt = new ArrayList<Skf3021Rp001GetShatakuLedgerInfoByshainNoExp>();
		//GetShatakuLedgerInfoByshainNo
		skParam.setShainNo(shainNo);
		skDt = skf3021Rp001GetShatakuLedgerInfoByshainNoExpRepository.getCurrentShatakuInfo(skParam);
		
		int honninCount = 0;
		if(skDt.size() > 1){
			for(Skf3021Rp001GetShatakuLedgerInfoByshainNoExp wkDr : skDt){
				if(HONNIN_DOKYO.equals(wkDr.getKyojushaKbn())){
					shatakuNo = wkDr.getShatakuKanriNo().toString();
					roomNo = wkDr.getShatakuRoomKanriNo().toString();
					honninCount = 1;
					break;
				}
				//本人同居が存在しない場合
				if(honninCount == 0){
					shatakuNo = skDt.get(0).getShatakuKanriNo().toString();
					roomNo = skDt.get(0).getShatakuRoomKanriNo().toString();
				}
			}
			
		}else if(skDt.size() == 1){
			//該当データが1件の場合
			shatakuNo = skDt.get(0).getShatakuKanriNo().toString();
			roomNo = skDt.get(0).getShatakuRoomKanriNo().toString();
		}
		
		noList.clear();
		noList.put("shatakuNo", shatakuNo);
		noList.put("roomNo", roomNo);
		
		return noList;
	}
	
	/**
	 * 帳票用データ作成
	 * @param viewRow 入退居予定データ
	 * @param nyukyoRow 入居希望等調書・入居決定通知
	 * @param shatakuRow 現社宅情報
	 * @param taikyoRow 退居（自動車の保管場所返還）届
	 * @return 入退居予定リスト
	 */
	private Skf3021Rp001NyutaikyoYoteiListExp createChohyoData(Skf3021Rp001GetNyutaikyoYoteiInfoExp viewRow,
			Skf2020TNyukyoChoshoTsuchi nyukyoRow,
			Skf3021Rp001GetCurrentShatakuInfoExp shatakuRow,
			Skf2040TTaikyoReport taikyoRow){
		//申請区分
		Map<String, String> genericCodeApplKbn = new HashMap<String, String>();
		genericCodeApplKbn = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_SHINSEI_KBN);
		//用途区分
		Map<String, String> genericCodeAuseKbn = new HashMap<String, String>();
		genericCodeAuseKbn = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_AUSE_KBN);
		//社宅必要理由区分
		Map<String, String> genericCodeNeedRiyuKbn = new HashMap<String, String>();
		genericCodeNeedRiyuKbn = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_SHATAKU_NEED_RIYU_KBN);
		
		//入退居予定リスト listRow
		Skf3021Rp001NyutaikyoYoteiListExp listRow = new Skf3021Rp001NyutaikyoYoteiListExp();
		
		//入退居区分
		String nyutaikyoKbn;
		if(nyukyoRow == null){
			nyutaikyoKbn = NYUTAIKYO_TAIKYO;
		}else{
			nyutaikyoKbn = NYUTAIKYO_NYUKYO;
		}
		listRow.setNyutaikyoKbn(nyutaikyoKbn);
		//申請区分(コードに対する名称)
		listRow.setApplKbn(genericCodeApplKbn.get(viewRow.getApplKbn()));
		//社員番号
		listRow.setShainNo(viewRow.getShainNo());
		//社員氏名
		listRow.setShainName(viewRow.getShainName());
		//新所属
		listRow.setNewAffiliation(viewRow.getNewAffiliation());
		//現所属
		String nowAffiliation = CodeConstant.DOUBLE_QUOTATION;
		if(nyukyoRow != null){
			//・入居の場合:入居希望等調書・入居決定通知の'現所属 機関/現所属 部等/現所属 室、チーム又は課を繋げて表示する。
			nowAffiliation = nyukyoRow.getAgency() +" "+ nyukyoRow.getAffiliation1() +" "+ nyukyoRow.getNewAffiliation2();
		}else{
			//・退居の場合:退居（自動車の保管場所返還）の'所属 機関/所属 部等/所属室、チーム又は課を繋げて表示する。
			nowAffiliation = taikyoRow.getAgency() +" "+ taikyoRow.getAffiliation1() +" "+ taikyoRow.getAffiliation2();
		}
		listRow.setNowAffiliation(nowAffiliation.replace("null", ""));

		//社宅入居希望等申請がある場合
		if(nyukyoRow != null){
			//入居予定日
			listRow.setNyukyoYoteiDate(nyukyoRow.getNyukyoYoteiDate());
			//用途
			listRow.setAuse(genericCodeAuseKbn.get(nyukyoRow.getHitsuyoShataku()));
			//同居家族１
			listRow.setLivingFamily1(livingFamily(nyukyoRow.getDokyoRelation1(),nyukyoRow.getDokyoAge1()));
			//同居家族2
			listRow.setLivingFamily2(livingFamily(nyukyoRow.getDokyoRelation2(),nyukyoRow.getDokyoAge2()));
			//同居家族3
			listRow.setLivingFamily3(livingFamily(nyukyoRow.getDokyoRelation3(),nyukyoRow.getDokyoAge3()));
			//同居家族4
			listRow.setLivingFamily4(livingFamily(nyukyoRow.getDokyoRelation4(),nyukyoRow.getDokyoAge4()));
			//同居家族5
			listRow.setLivingFamily5(livingFamily(nyukyoRow.getDokyoRelation5(),nyukyoRow.getDokyoAge5()));
			//同居家族6
			listRow.setLivingFamily6(livingFamily(nyukyoRow.getDokyoRelation6(),nyukyoRow.getDokyoAge6()));
			//車種１
			listRow.setCarModel1(nyukyoRow.getCarName());
			//使用開始予定日１
			listRow.setParking1StartDate(nyukyoRow.getParkingUseDate());
			//車種２
			listRow.setCarModel2(nyukyoRow.getCarName2());
			//使用開始予定日２
			listRow.setParking2StartDate(nyukyoRow.getParkingUseDate2());
			//特殊事情
			listRow.setTokushuJijo(viewRow.getTokushuJijo());
			//入居理由
			listRow.setHitsuyoRiyu(genericCodeNeedRiyuKbn.get(nyukyoRow.getHitsuyoRiyu()));
			//退居理由
			listRow.setTaikyoRiyu(nyukyoRow.getTaikyoRiyu());
		}else if(taikyoRow != null){
			//退居（自動車の保管場所返還）届がある場合
			//退居理由
			listRow.setTaikyoRiyu(taikyoRow.getTaikyoRiyu());
		}

		//現社宅がある場合
		if(shatakuRow != null){
			//社宅名
			listRow.setShatakuName(shatakuRow.getShatakuName());
			//部屋番号
			listRow.setRoomNo(shatakuRow.getRoomNo());
			//駐車場利用台数
			String parkingCount = CodeConstant.DOUBLE_QUOTATION;
			if(shatakuRow.getParkingCount() != null){
				parkingCount = shatakuRow.getParkingCount().toString();
			}				
			listRow.setParkingCount(parkingCount);
		}


		
		return listRow;
	}

	/**
	 * 同居家族文字列取得
	 * @param relation 続柄
	 * @param age 年齢
	 * @return
	 */
	private String livingFamily(String relation,String age){
		
		String livingfamily = CodeConstant.DOUBLE_QUOTATION;

		if(SkfCheckUtils.isNullOrEmpty(relation)){
			return livingfamily;
		}
		//LIVING_FAMILY As String = "{0}({1})"
		livingfamily = relation + "("+ age +")";
		
		return livingfamily;
	}
	
	
	/**
	 * 入退居予定リスト(Excel)作成
	 * @param dt 入退居予定リスト出力用データ
	 * @return
	 */
	private SheetDataBean createNyutaikyoYoteiList(List<Skf3021Rp001NyutaikyoYoteiListExp> dt){
		
		// Excelワークシート
		SheetDataBean sheetDataBean = new SheetDataBean();
		// Excel行データ
		List<RowDataBean> rowDataBeanList = new ArrayList<>();
		
		//出力日時
		RowDataBean rdbDate = new RowDataBean();
		rdbDate.addCellDataBean("B3", DateTime.now().toString("出力日時:YYYY/MM/dd HH:mm:ss") );//入居予定日
		rowDataBeanList.add(rdbDate);
			
		for (int i = 0, rowNo = 7; i < dt.size(); i++, rowNo++) {
			// 行データ
			RowDataBean rdb = new RowDataBean();
			Skf3021Rp001NyutaikyoYoteiListExp getRowData = dt.get(i);
			
			// Excel行データ設定
			rdb.addCellDataBean("B" + rowNo, getRowData.getNyutaikyoKbn());//入居予定日
			rdb.addCellDataBean("C" + rowNo, getRowData.getApplKbn());//申請区分
			rdb.addCellDataBean("D" + rowNo, getRowData.getNewAffiliation());//新所属
			rdb.addCellDataBean("E" + rowNo, getRowData.getShainNo());//社員番号
			rdb.addCellDataBean("F" + rowNo, getRowData.getShainName());//社員氏名
			rdb.addCellDataBean("G" + rowNo, getRowData.getNowAffiliation());//現所属
			//入居予定日
			String nyukyoYoteiDate = CodeConstant.DOUBLE_QUOTATION;
			if(!SkfCheckUtils.isNullOrEmpty(getRowData.getNyukyoYoteiDate())){
				nyukyoYoteiDate = skfDateFormatUtils.dateFormatFromString(getRowData.getNyukyoYoteiDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				rdb.addCellDataBean("H" + rowNo, nyukyoYoteiDate,
						Cell.CELL_TYPE_NUMERIC,
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}else{
				rdb.addCellDataBean("H" + rowNo, nyukyoYoteiDate);
			}

			rdb.addCellDataBean("I" + rowNo, getRowData.getAuse());//用途
			rdb.addCellDataBean("J" + rowNo, getRowData.getLivingFamily1());//同居家族1
			rdb.addCellDataBean("K" + rowNo, getRowData.getLivingFamily2());//同居家族2
			rdb.addCellDataBean("L" + rowNo, getRowData.getLivingFamily3());//同居家族3
			rdb.addCellDataBean("M" + rowNo, getRowData.getLivingFamily4());//同居家族4
			rdb.addCellDataBean("N" + rowNo, getRowData.getLivingFamily5());//同居家族5
			rdb.addCellDataBean("O" + rowNo, getRowData.getLivingFamily6());//同居家族6
			rdb.addCellDataBean("P" + rowNo, getRowData.getCarModel1());//車種1
			//使用開始予定日1
			String parking1StartDate = CodeConstant.DOUBLE_QUOTATION;
			if(!SkfCheckUtils.isNullOrEmpty(getRowData.getParking1StartDate())){
				nyukyoYoteiDate = skfDateFormatUtils.dateFormatFromString(getRowData.getParking1StartDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}
			rdb.addCellDataBean("Q" + rowNo, parking1StartDate,
					Cell.CELL_TYPE_NUMERIC,
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			rdb.addCellDataBean("R" + rowNo, getRowData.getCarModel2());//車種2
			//使用開始予定日2
			String parking2StartDate = CodeConstant.DOUBLE_QUOTATION;
			if(!SkfCheckUtils.isNullOrEmpty(getRowData.getParking2StartDate())){
				nyukyoYoteiDate = skfDateFormatUtils.dateFormatFromString(getRowData.getParking2StartDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}
			rdb.addCellDataBean("S" + rowNo, parking2StartDate,
					Cell.CELL_TYPE_NUMERIC,
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			rdb.addCellDataBean("T" + rowNo, getRowData.getTokushuJijo());//特殊事情
			rdb.addCellDataBean("U" + rowNo, getRowData.getHitsuyoRiyu());//入居理由
			rdb.addCellDataBean("V" + rowNo, getRowData.getTaikyoRiyu());//退居理由
			rdb.addCellDataBean("W" + rowNo, getRowData.getShatakuKbn());//区分//空文字
			rdb.addCellDataBean("X" + rowNo, getRowData.getShatakuName());//社宅名
			rdb.addCellDataBean("Y" + rowNo, getRowData.getRoomNo());//部屋番号
			rdb.addCellDataBean("Z" + rowNo, getRowData.getParkingCount(),Cell.CELL_TYPE_NUMERIC);//駐車場利用台数
			// 行データ追加
			rowDataBeanList.add(rdb);
		}
		
		sheetDataBean.setRowDataBeanList(rowDataBeanList);
		sheetDataBean.setSheetName(excelWorkSheetName);
		
		return sheetDataBean;
	}
	
	/**
	 * 入退居予定データExcelファイル出力
	 * @param workSheet
	 * @param downloadDto
	 * @throws Exception
	 */
	private void fileOutPutExcel(SheetDataBean workSheet,
			 Skf3021Sc001NyutaikyoYoteiListDownloadDto downloadDto) throws Exception {

		List<SheetDataBean> sheetDataBeanList = new ArrayList<>();
		sheetDataBeanList.add(workSheet);

		Map<String, Object> cellparams = new HashMap<>(); // 列書式(背景、フォント)
		Map<String, Object> resultMap = new HashMap<>(); // 列書式(背景、フォント)

		String fileName = excelPreFileName + DateTime.now().toString("YYYYMMddHHmmss") + ".xlsx";
		WorkBookDataBean wbdb = new WorkBookDataBean(fileName);
		wbdb.setSheetDataBeanList(sheetDataBeanList);
		// Excelファイルへ出力
		SkfFileOutputUtils.fileOutputExcel(wbdb, cellparams, "skf3021.skf3021_sc001.excelTemplateFile", "SKF3021RP001",
				excelOutPutStartLine, null, resultMap,7);
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
