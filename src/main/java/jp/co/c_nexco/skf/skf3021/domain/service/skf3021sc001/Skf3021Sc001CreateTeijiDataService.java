/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3021.domain.service.skf3021sc001;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3021Sc001.Skf3021Sc001GetBihinHenkyakuCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3021Sc001.Skf3021Sc001GetChoshoInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3021Sc001.Skf3021Sc001GetNyukyoChoshoTsuchiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3021Sc001.Skf3021Sc001GetShatakuBihinRirekiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3021Sc001.Skf3021Sc001GetShatakuLedgerInfoByshainNoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3021Sc001.Skf3021Sc001GetShatakuLedgerInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3021Sc001.Skf3021Sc001GetShatakuLedgerInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3021Sc001.Skf3021Sc001GetShatakuRoomBihinInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3021Sc001.Skf3021Sc001GetShatakuRoomBihinInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3021Sc001.Skf3021Sc001GetTaikyoReportInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuParkingBlock;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuParkingBlockKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3021TNyutaikyoYoteiData;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3021TNyutaikyoYoteiDataKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3022TTeijiBihinData;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3022TTeijiData;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TParkingRireki;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TParkingRirekiKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TRentalPattern;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TRentalPatternKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuBihin;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuLedger;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuMutual;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuMutualRireki;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuMutualRirekiKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuRentalRireki;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuRentalRirekiKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3050MBihinItem;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3021Sc001.Skf3021Sc001GetBihinHenkyakuCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3021Sc001.Skf3021Sc001GetBihinItemInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3021Sc001.Skf3021Sc001GetNyukyoChoshoTsuchiInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3021Sc001.Skf3021Sc001GetShatakuBihinRirekiInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3021Sc001.Skf3021Sc001GetShatakuLedgerInfoByshainNoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3021Sc001.Skf3021Sc001GetShatakuLedgerInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3021Sc001.Skf3021Sc001GetShatakuRoomBihinInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3021Sc001.Skf3021Sc001GetTaikyoReportInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuParkingBlockRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3021TNyutaikyoYoteiDataRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3022TTeijiBihinDataRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3022TTeijiDataRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3030TParkingRirekiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3030TRentalPatternRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3030TShatakuBihinRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3030TShatakuMutualRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3030TShatakuMutualRirekiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3030TShatakuRentalRirekiRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3021.domain.dto.skf3021sc001.Skf3021Sc001CreateTeijiDataDto;

import jp.co.intra_mart.mirage.integration.guice.Transactional;

/**
 * Skf3021Sc001CreateTeijiDataService 入退居予定一覧画面の提示データ作成サービス処理クラス。
 * 
 *  @author NEXCOシステムズ
 *  
 */
@Service
public class Skf3021Sc001CreateTeijiDataService extends SkfServiceAbstract<Skf3021Sc001CreateTeijiDataDto> {
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	@Autowired
	private Skf3021Sc001SharedService skf3021Sc001SharedService;
	@Autowired
	private Skf3022TTeijiBihinDataRepository skf3022TTeijiBihinDataRepository;
	@Autowired
	private Skf3030TShatakuRentalRirekiRepository skf3030TShatakuRentalRirekiRepository;
	@Autowired
	private Skf3030TParkingRirekiRepository skf3030TParkingRirekiRepository;
	@Autowired
	private Skf3010MShatakuParkingBlockRepository skf3010MShatakuParkingBlockRepository;
	@Autowired
	private Skf3030TShatakuBihinRepository skf3030TShatakuBihinRepository;
	@Autowired
	private Skf3030TShatakuMutualRepository skf3030TShatakuMutualRepository;
	@Autowired
	private Skf3030TShatakuMutualRirekiRepository skf3030TShatakuMutualRirekiRepository;
	@Autowired
	private Skf3022TTeijiDataRepository skf3022TTeijiDataRepository;
	@Autowired
	private Skf3021TNyutaikyoYoteiDataRepository skf3021TNyutaikyoYoteiDataRepository;
	@Autowired
	private Skf3030TRentalPatternRepository skf3030TRentalPatternRepository;
	@Autowired
	private Skf3021Sc001GetNyukyoChoshoTsuchiInfoExpRepository skf3021Sc001GetNyukyoChoshoTsuchiInfoExpRepository;
	@Autowired
	private Skf3021Sc001GetTaikyoReportInfoExpRepository skf3021Sc001GetTaikyoReportInfoExpRepository;
	@Autowired
	private Skf3021Sc001GetShatakuLedgerInfoExpRepository skf3021Sc001GetShatakuLedgerInfoExpRepository;
	@Autowired
	private Skf3021Sc001GetShatakuLedgerInfoByshainNoExpRepository skf3021Sc001GetShatakuLedgerInfoByshainNoExpRepository;
	@Autowired
	private Skf3021Sc001GetBihinItemInfoExpRepository skf3021Sc001GetBihinItemInfoExpRepository;
	@Autowired
	private Skf3021Sc001GetShatakuRoomBihinInfoExpRepository skf3021Sc001GetShatakuRoomBihinInfoExpRepository;
	@Autowired
	private Skf3021Sc001GetShatakuBihinRirekiInfoExpRepository skf3021Sc001GetShatakuBihinRirekiInfoExpRepository;
	@Autowired
	private Skf3021Sc001GetBihinHenkyakuCountExpRepository skf3021Sc001GetBihinHenkyakuCountExpRepository;
	//ボタン押下区分：初期化
	private static final int BTNFLG_INIT = 0;
	//ボタン押下区分：検索ボタン押下
	//private static final int BTNFLG_KENSAKU = 1;
	//居住者区分 -本人同居
	private static final String HONNIN_DOKYO = "1";
	//居住者区分 -留守家族
	//private static final String RUSU_KAZOKU = "2";
	//備品コード
	private static final String BIHIN_CD_91 = "91";
	private static final String BIHIN_CD_92 = "92";
	private static final String BIHIN_CD_93 = "93";
	//入退居の退居
	private static final String TAIKYO = "1";
	//退居のみ
	private static final String TAIKYOONLY = "2";
	//変更
	private static final String HENKOU = "3";
	//退居する社宅区分2：駐車場１のみ返却
	private static final String PARKING1 = "1";
	//退居する社宅区分2：駐車場２のみ返却
	private static final String PARKING2 = "2";
	//退居する社宅区分2：駐車場１,２ともに返却
	private static final String PARKING12 = "3";

	//備品貸与状態区分:会社保有
	private static final String BIHIN_LENT_STATUS_KAISHA_HOYU = "2";
	//備品貸与状態区分:レンタル
	private static final String BIHIN_LENT_STATUS_RENTAL = "3";

	/**
	 * サービス処理を行う。　
	 * 
	 * @param teijiDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	@Transactional
	public Skf3021Sc001CreateTeijiDataDto index(Skf3021Sc001CreateTeijiDataDto teijiDto) throws Exception {
		
		teijiDto.setPageTitleKey(MessageIdConstant.SKF3021_SC001_TITLE);
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("提示データ作成", CodeConstant.C001, FunctionIdConstant.SKF3021_SC001);
		
		//入退居区分リスト
		List<Map<String, Object>> nyutaikyoKbnList = new ArrayList<Map<String, Object>>();
		//入退居申請状況リスト
		List<Map<String, Object>> nyuTaikyoShinseiJokyoList = new ArrayList<Map<String, Object>>();
		//特殊事情リスト
		List<Map<String, Object>> tokushuJijoList = new ArrayList<Map<String, Object>>();
		//提示データ作成区分リスト(提示対象)
		List<Map<String, Object>> teijiDetaSakuseiKbnList = new ArrayList<Map<String, Object>>();
		//入退居申請督促リスト
		List<Map<String, Object>> nyuTaikyoShinseiTokusokuList = new ArrayList<Map<String, Object>>();

		
		//getDropDownList
		skf3021Sc001SharedService.getDropDownList(teijiDto.getNyutaikyoKbn(), nyutaikyoKbnList,
				teijiDto.getTeijiDetaSakuseiKbn(), teijiDetaSakuseiKbnList,
				teijiDto.getNyuTaikyoShinseiJokyo() , nyuTaikyoShinseiJokyoList,
				teijiDto.getNyuTaikyoShinseiTokusoku() , nyuTaikyoShinseiTokusokuList,
				teijiDto.getTokushuJijo() , tokushuJijoList);
		//リスト情報
		teijiDto.setNyutaikyoKbnList(nyutaikyoKbnList);
		teijiDto.setTeijiDetaSakuseiKbnList(teijiDetaSakuseiKbnList);
		teijiDto.setNyuTaikyoShinseiJokyoList(nyuTaikyoShinseiJokyoList);
		teijiDto.setNyuTaikyoShinseiTokusokuList(nyuTaikyoShinseiTokusokuList);
		teijiDto.setTokushuJijoList(tokushuJijoList);
		
		
		//提示データ作成情報リスト作成
		List<Map<String,Object>> teijiList = createTeijiList(teijiDto.getTeijiListData());
		//提示データ作成
		int result = 0;
		result = insertTeijiDataInfo(teijiList, teijiDto);
		if(result == -1){
			ServiceHelper.addErrorResultMessage(teijiDto, null,  MessageIdConstant.W_SKF_1009);
			throwBusinessExceptionIfErrors(teijiDto.getResultMessages());
		}else if(result == -2){
			ServiceHelper.addErrorResultMessage(teijiDto, null,  MessageIdConstant.E_SKF_1073);
			throwBusinessExceptionIfErrors(teijiDto.getResultMessages());
		}else if(result == -4){
			ServiceHelper.addErrorResultMessage(teijiDto, null,  MessageIdConstant.E_SKF_3054);
			throwBusinessExceptionIfErrors(teijiDto.getResultMessages());
		}else if(result > 0){
			ServiceHelper.addResultMessage(teijiDto, MessageIdConstant.I_SKF_1012);
		}
		
		//再検索
		// リストデータ取得用
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();

		//入退居予定を取得
		skf3021Sc001SharedService.setGrvNyuTaikyoYoteiIchiran(BTNFLG_INIT, teijiDto, listTableData);

		//一覧情報
		teijiDto.setListTableData(listTableData);
		
		return teijiDto;
	}
	/**
	 * 提示データ作成情報文字列をListに変換
	 * @param mailListData
	 * @return
	 */
	private List<Map<String,Object>> createTeijiList(String teijiListData){
		//返却リスト初期化
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		
		//備品情報リスト（表示分）
		//画面からは文字列で取得するので、まず行ごとに分割
		String[] infoList = teijiListData.split(";");
		for(String info : infoList){
			LogUtils.debugByMsg("送信情報文字列:"+info);
			//行データを項目ごとに分割
			String[] mail = info.split(",");
			if(mail.length >= 9){
				Map<String, Object> forListMap = new HashMap<String, Object>();
				forListMap.put("shainNo", mail[0]);//社員番号
				forListMap.put("nyutaikyoKbn", mail[1]);//入退居区分
				forListMap.put("shainName", mail[2]);//社員氏名
				forListMap.put("applKbn", mail[3]);//申請区分
				forListMap.put("applNo", mail[4]);//申請書類管理番号
				forListMap.put("taikyoYoteiDate", mail[5]);//退居予定日
				forListMap.put("parking1StartDate", mail[6]);//駐車場区画１開始日
				forListMap.put("parking2StartDate", mail[7]);//駐車場区画２開始日
				forListMap.put("hdnUpdateDate", mail[8]);//更新日時hidden変数
/* AS 結合1041対応 */
				if (mail.length > 9) {
					forListMap.put("hdnNowAffiliation", mail[9]);	// 現所属
				}
				if (mail.length > 10) {
					forListMap.put("hdnNewAffiliation", mail[10]);	// 新所属
				}
/* AE 結合1041対応 */
				resultList.add(forListMap);
			}
		}
		
		return resultList;
	}
	
	
	/**
	 * 提示データ作成処理メソッド
	 * @param teijiList 入退居予定一覧
	 * @return
	 */
	private int insertTeijiDataInfo(List<Map<String,Object>> teijiList, Skf3021Sc001CreateTeijiDataDto teijiDto){
		
		//日付形式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		//入退居予定情報更新件数
		int updCount = 0;
		//提示データ登録件数
		int insCount = 0;
		String companyCd = CodeConstant.C001;
		
		//システム日時
//		String systemDate = CodeConstant.DOUBLE_QUOTATION;
		//社員番号
		String shainNo =CodeConstant.DOUBLE_QUOTATION;
		//入退居区分
		String nyutaikyoKbn = CodeConstant.DOUBLE_QUOTATION;
		//社員氏名
		String name = CodeConstant.DOUBLE_QUOTATION;
		//申請区分
		String applKbn = CodeConstant.DOUBLE_QUOTATION;
		//申請書類管理番号
		String applNo = CodeConstant.DOUBLE_QUOTATION;
		//退居予定日
		String taikyoYoteiDate = CodeConstant.DOUBLE_QUOTATION;
		//駐車場区画１開始日
		String parking1StartDate = CodeConstant.DOUBLE_QUOTATION;
		//駐車場区画２開始日
		String parking2StartDate = CodeConstant.DOUBLE_QUOTATION;
		//更新日時hidden変数
		String hdnUpdateDate = CodeConstant.DOUBLE_QUOTATION;
		//入退居予定日
		String nyukyoDate = CodeConstant.DOUBLE_QUOTATION;
		//使用料パターンＩＤ
		String rentalPatternId = CodeConstant.DOUBLE_QUOTATION;
/* US 結合1041対応 */
//		//社宅管理番号
//		String shatakuKanriNo = CodeConstant.DOUBLE_QUOTATION;
//		//部屋管理番号
//		String shatakuRoomKanriNo = CodeConstant.DOUBLE_QUOTATION;
//		//社宅管理台帳ID
//		String shatakuKanriId = CodeConstant.DOUBLE_QUOTATION;
		//社宅管理番号
		Long shatakuKanriNo = null;
		//部屋管理番号
		Long shatakuRoomKanriNo = null;
		//社宅管理台帳ID
		Long shatakuKanriId = null;
/* UE 結合1041対応 */
/* AS 結合1041対応 */
		// 現所属
		String hdnNowAffiliation = CodeConstant.DOUBLE_QUOTATION;
		// 新所属
		String hdnNewAffiliation = CodeConstant.DOUBLE_QUOTATION;
/* AE 結合1041対応 */
		
		for(Map<String,Object> map : teijiList){
			//社員番号
			shainNo = map.get("shainNo").toString().replace(CodeConstant.ASTERISK, "");
			//入退居区分
			nyutaikyoKbn = map.get("nyutaikyoKbn").toString();
			//社員氏名
			name = map.get("shainName").toString();
			//申請区分
			applKbn = map.get("applKbn").toString();
			//申請書類管理番号
			applNo = map.get("applNo").toString();
			//退居予定日
			taikyoYoteiDate = map.get("taikyoYoteiDate").toString();
			//駐車場区画１開始日
			parking1StartDate = map.get("parking1StartDate").toString();
			//駐車場区画２開始日
			parking2StartDate = map.get("parking2StartDate").toString();
			//更新日時hidden変数
			hdnUpdateDate = map.get("hdnUpdateDate").toString();
/* AS 結合1041対応 */
			// 現所属
			if (map.get("hdnNowAffiliation") != null) {
				hdnNowAffiliation = map.get("hdnNowAffiliation").toString();
			}
			// 新所属
			if (map.get("hdnNewAffiliation") != null) {
				hdnNewAffiliation = map.get("hdnNewAffiliation").toString();
			}
/* AE 結合1041対応 */
			//入退居予定日
			nyukyoDate = CodeConstant.DOUBLE_QUOTATION;
			//使用料パターンＩＤ
			rentalPatternId = CodeConstant.DOUBLE_QUOTATION;
/* US 結合1041対応 */
//			//社宅管理番号
//			shatakuKanriNo = CodeConstant.DOUBLE_QUOTATION;
//			//部屋管理番号
//			shatakuRoomKanriNo = CodeConstant.DOUBLE_QUOTATION;
//			//社宅管理台帳ID
//			shatakuKanriId = CodeConstant.DOUBLE_QUOTATION;

			//社宅管理番号
			shatakuKanriNo = null;
			//部屋管理番号
			shatakuRoomKanriNo = null;
			//社宅管理台帳ID
			shatakuKanriId = null;
/* UE 結合1041対応 */
			//システム日時の取得
//			systemDate = dateFormat.format(skfBaseBusinessLogicUtils.getSystemDateTime());
			
			//提示番号
			Long teijiNo = 0L;
			//返却要備品件数
			int returnCount = 0;
			//年月
			String yearMonth = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu();
			//入退居区分（同一社員番号）
			String nyutaikyoKbnOfSameShainNo = CodeConstant.DOUBLE_QUOTATION;
			
			//入居希望等調書・入居決定通知情報を取得
			Skf3021Sc001GetChoshoInfoExpParameter choshoParam = new Skf3021Sc001GetChoshoInfoExpParameter();
			choshoParam.setCompanyCd(companyCd);
			choshoParam.setApplNo(applNo);
			List<Skf3021Sc001GetNyukyoChoshoTsuchiInfoExp> dtNCT = new ArrayList<Skf3021Sc001GetNyukyoChoshoTsuchiInfoExp>();
			//GetNyukyoChoshoTsuchiInfo
			dtNCT = skf3021Sc001GetNyukyoChoshoTsuchiInfoExpRepository.getNyukyoChoshoTsuchiInfo(choshoParam);
			
			//退居（自動車の保管場所返還）届情報を取得
			List<Skf3021Sc001GetTaikyoReportInfoExp> dtTR = new ArrayList<Skf3021Sc001GetTaikyoReportInfoExp>();
			dtTR = skf3021Sc001GetTaikyoReportInfoExpRepository.getTaikyoReportInfo(choshoParam);
			
			List<Skf3021Sc001GetShatakuLedgerInfoExp> dtSL1 = new ArrayList<Skf3021Sc001GetShatakuLedgerInfoExp>();
			if((CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(nyutaikyoKbn) || 
					CodeConstant.NYUTAIKYO_KBN_HENKO.equals(nyutaikyoKbn))
					&& dtNCT.size() > 0){
				//社宅管理台帳基本情報を取得
				Skf3021Sc001GetShatakuLedgerInfoExpParameter ledgerParam = new Skf3021Sc001GetShatakuLedgerInfoExpParameter();
				ledgerParam.setShatakuKanriNo(dtNCT.get(0).getShatakuNo());
				ledgerParam.setShatakuRoomKanriNo(dtNCT.get(0).getRoomKanriNo());
				ledgerParam.setShainNo(shainNo);
				
				dtSL1 = skf3021Sc001GetShatakuLedgerInfoExpRepository.getShatakuLedgerInfo(ledgerParam);
/* US 結合1041対応 */
//				shatakuKanriNo = dtNCT.get(0).getShatakuNo().toString();
//				shatakuRoomKanriNo = dtNCT.get(0).getRoomKanriNo().toString();
				shatakuKanriNo = dtNCT.get(0).getShatakuNo();
				shatakuRoomKanriNo = dtNCT.get(0).getRoomKanriNo();
/* UE 結合1041対応 */
			}else if(CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(nyutaikyoKbn) && dtTR.size() > 0) {
				//社宅管理台帳基本情報を取得
				Skf3021Sc001GetShatakuLedgerInfoExpParameter ledgerParam = new Skf3021Sc001GetShatakuLedgerInfoExpParameter();
				ledgerParam.setShatakuKanriNo(dtTR.get(0).getShatakuNo());
				ledgerParam.setShatakuRoomKanriNo(dtTR.get(0).getRoomKanriNo());
				ledgerParam.setShainNo(shainNo);
				
				dtSL1 = skf3021Sc001GetShatakuLedgerInfoExpRepository.getShatakuLedgerInfo(ledgerParam);
/* US 結合1041対応 */
//				shatakuKanriNo = dtTR.get(0).getShatakuNo().toString();
//				shatakuRoomKanriNo = dtTR.get(0).getRoomKanriNo().toString();
				shatakuKanriNo = dtTR.get(0).getShatakuNo();
				shatakuRoomKanriNo = dtTR.get(0).getRoomKanriNo();
/* UE 結合1041対応 */
			}
			
			//社宅管理台帳基本情報を取得
			Skf3030TShatakuLedger dtSL2Row = null;
			if(SkfCheckUtils.isNullOrEmpty(applNo)){
				//■申請なしの場合
				//社員番号をもとに台帳データを取得
				int honninCount = 0;
				Skf3021Sc001GetShatakuLedgerInfoByshainNoExpParameter shainNoParam = new Skf3021Sc001GetShatakuLedgerInfoByshainNoExpParameter();
				shainNoParam.setShainNo(shainNo);
				List<Skf3030TShatakuLedger> wkDtSL2 = new ArrayList<Skf3030TShatakuLedger>();
				wkDtSL2 = skf3021Sc001GetShatakuLedgerInfoByshainNoExpRepository.getShatakuLedgerInfoByshainNo(shainNoParam);
				if(wkDtSL2.size() == 0){
					//①該当する台帳データが0件の場合（仮社員番号、システム連携なし：正社員番号）
					//退居また変更の場合、エラーメッセージを表示する
					if(CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(nyutaikyoKbn) || 
							CodeConstant.NYUTAIKYO_KBN_HENKO.equals(nyutaikyoKbn)){
						return -4;
					}
					
				}else if(wkDtSL2.size() == 1){
					//②該当する台帳データが1件の場合
					dtSL2Row = wkDtSL2.get(0);
				}else{
					//③－１該当する台帳データが2件ある場合、本人同居を優先設定する
					for(Skf3030TShatakuLedger wkDr : wkDtSL2){
						if(HONNIN_DOKYO.equals(wkDr.getKyojushaKbn())){
							dtSL2Row = wkDr;
							honninCount = 1;
						}
					}
					//③－２台帳データに本人同居が存在しない場合
					if(honninCount == 0){
						dtSL2Row = wkDtSL2.get(0);
					}
				}
			}else{
				//■申請ありの場合
				//申請データをもとに台帳データを取得				
				Skf3021Sc001GetShatakuLedgerInfoExpParameter ledger2Param = new Skf3021Sc001GetShatakuLedgerInfoExpParameter();
/* US 結合1041対応 */
//				ledger2Param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
//				ledger2Param.setShatakuRoomKanriNo(Long.parseLong(shatakuRoomKanriNo));
				ledger2Param.setShatakuKanriNo(shatakuKanriNo);
				ledger2Param.setShatakuRoomKanriNo(shatakuRoomKanriNo);
/* UE 結合1041対応 */
				ledger2Param.setShainNo(shainNo);
				List<Skf3030TShatakuLedger> dtSL2 = new ArrayList<Skf3030TShatakuLedger>();
				dtSL2 = skf3021Sc001GetShatakuLedgerInfoExpRepository.getShatakuLedgerInfo2(ledger2Param);
				if(dtSL2.size() > 0){
					dtSL2Row = dtSL2.get(0);
				}
			}
			
			//提示番号
			teijiNo = skfBaseBusinessLogicUtils.getMaxTeijiNo();
			
			if(CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(nyutaikyoKbn) && 
					!CodeConstant.SHINSEI_KBN_PARKING.equals(applKbn)){
				//入居の場合
				List<Skf3050MBihinItem> dtBI = new ArrayList<Skf3050MBihinItem>();
				//備品項目設定情報を取得
				dtBI = skf3021Sc001GetBihinItemInfoExpRepository.getBihinItemInfo();
				for(Skf3050MBihinItem dr : dtBI){
					//提示備品データ登録処理
					Skf3022TTeijiBihinData teijiBihinData = GetColumnInfoListOfTeijiBihinData(teijiNo, nyutaikyoKbn,
							applKbn, applNo, returnCount, dr.getBihinCd(), CodeConstant.BIHIN_STATE_NONE);
					
					insCount = insCount + insertTeijiBihinDataInfo(teijiBihinData);
					
					teijiBihinData = null;
				}
			}
			//申請なしの退居、且つ、社宅退居申請の場合
			else if((SkfCheckUtils.isNullOrEmpty(applNo) || 
					CodeConstant.SHINSEI_KBN_SHIATAKU.equals(applKbn)) && 
					CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(nyutaikyoKbn)){
				//申請なしの場合
				if(SkfCheckUtils.isNullOrEmpty(applNo)){
/* US 結合1041対応 */
//					shatakuKanriNo = dtSL2Row.getShatakuKanriNo().toString();
//					shatakuRoomKanriNo = dtSL2Row.getShatakuRoomKanriNo().toString();
					shatakuKanriNo = dtSL2Row.getShatakuKanriNo();
					shatakuRoomKanriNo = dtSL2Row.getShatakuRoomKanriNo();
/* UE 結合1041対応 */
				}
/* US 結合1041対応 */
//				//申請があっても無くても、社宅管理台帳IDを代入
//				shatakuKanriId = dtSL2Row.getShatakuKanriId().toString();
				//申請があっても無くても、社宅管理台帳IDを代入
				shatakuKanriId = dtSL2Row.getShatakuKanriId();
/* UE 結合1041対応 */
				//社宅退居の場合
				//社宅部屋備品情報マスタを取得
				//GetShatakuRoomBihinInfo
				Skf3021Sc001GetShatakuRoomBihinInfoExpParameter roomBihinParam = new Skf3021Sc001GetShatakuRoomBihinInfoExpParameter();
/* US 結合1041対応 */
//				roomBihinParam.setShatakuKanriId(Long.parseLong(shatakuKanriId));
				roomBihinParam.setShatakuKanriId(shatakuKanriId);
/* UE 結合1041対応 */
				roomBihinParam.setYearMonth(yearMonth);
				List<Skf3021Sc001GetShatakuRoomBihinInfoExp> dtSRB = new ArrayList<Skf3021Sc001GetShatakuRoomBihinInfoExp>();
				dtSRB = skf3021Sc001GetShatakuRoomBihinInfoExpRepository.getShatakuRoomBihinInfo(roomBihinParam);
				for(Skf3021Sc001GetShatakuRoomBihinInfoExp dr : dtSRB){
					//提示備品データ登録処理
					Skf3022TTeijiBihinData teijiBihinData = GetColumnInfoListOfTeijiBihinData(teijiNo, nyutaikyoKbn,
							applKbn, applNo, returnCount, dr.getBihinCd(), dr.getBihinStatusKbn());
					
					insCount = insCount + insertTeijiBihinDataInfo(teijiBihinData);
					
					teijiBihinData = null;
				}
			}else{
				//月別備品使用料明細情報を取得
				Skf3021Sc001GetShatakuRoomBihinInfoExpParameter bihinRirekiParam = new Skf3021Sc001GetShatakuRoomBihinInfoExpParameter();
				bihinRirekiParam.setShatakuKanriId(dtSL2Row.getShatakuKanriId());
				bihinRirekiParam.setYearMonth(yearMonth);
				List<Skf3021Sc001GetShatakuBihinRirekiInfoExp> dtSBR = new ArrayList<Skf3021Sc001GetShatakuBihinRirekiInfoExp>();
				dtSBR = skf3021Sc001GetShatakuBihinRirekiInfoExpRepository.getShatakuBihinRirekiInfo(bihinRirekiParam);
				for(Skf3021Sc001GetShatakuBihinRirekiInfoExp dr : dtSBR){
					//提示備品データ登録処理
					Skf3022TTeijiBihinData teijiBihinData = GetColumnInfoListOfTeijiBihinData(teijiNo, nyutaikyoKbn,
							applKbn, applNo, returnCount, dr.getBihinCd(), dr.getBihinLentStatusKbn());
					
					insCount = insCount + insertTeijiBihinDataInfo(teijiBihinData);
					
					teijiBihinData = null;
				}
			}

			//変更、退居（申請なし）の場合
			if((CodeConstant.NYUTAIKYO_KBN_HENKO.equals(nyutaikyoKbn) || 
					CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(nyutaikyoKbn)) && SkfCheckUtils.isNullOrEmpty(applNo)){
				if(dtSL2Row != null){
					//入居予定日
					nyukyoDate = dtSL2Row.getNyukyoDate();
					//提示データ登録処理
					Skf3022TTeijiData teijiData = getColumnInfoListOfTeijiData(null,dtSL2Row, dtNCT, shainNo, nyutaikyoKbn, name, applKbn, applNo
/* US 結合1041対応 */
//							, taikyoYoteiDate, companyCd, yearMonth, teijiNo);
							, taikyoYoteiDate, companyCd, yearMonth, hdnNowAffiliation, hdnNewAffiliation, teijiNo);
/* UE 結合1041対応 */
					insCount = insCount + insertTeijiDataInfo(teijiData);
				}

			}else{
				//提示データ登録処理
				Skf3022TTeijiData teijiData = getColumnInfoListOfTeijiData(teijiDto.getListTableData(),dtSL1,dtNCT,dtTR,shainNo,returnCount
						,nyutaikyoKbn,name,applKbn,applNo,taikyoYoteiDate,companyCd,yearMonth
/* US 結合1041対応 */
//						,parking1StartDate,parking2StartDate,teijiNo,nyutaikyoKbnOfSameShainNo,rentalPatternId);
						,parking1StartDate,parking2StartDate, hdnNowAffiliation, hdnNewAffiliation, teijiNo,nyutaikyoKbnOfSameShainNo,rentalPatternId);
/* UE 結合1041対応 */
				insCount = insCount + insertTeijiDataInfo(teijiData);
				if(teijiData.getRentalPatternId() != null){
					rentalPatternId = teijiData.getRentalPatternId().toString();
				}
			}
			
			//GetNyutaikyoYoteiInfoForUpdate
			Skf3021TNyutaikyoYoteiDataKey ntyKey = new Skf3021TNyutaikyoYoteiDataKey();
			ntyKey.setShainNo(shainNo);
			ntyKey.setNyutaikyoKbn(nyutaikyoKbn);
			Skf3021TNyutaikyoYoteiData targetDt = skf3021TNyutaikyoYoteiDataRepository.selectByPrimaryKey(ntyKey);
			//データが存在する場合
			if(targetDt != null){
				//用途
				String ause = CodeConstant.DOUBLE_QUOTATION;
				//変更（申請なし）以外の場合
				if(!CodeConstant.NYUTAIKYO_KBN_HENKO.equals(nyutaikyoKbn) || !SkfCheckUtils.isNullOrEmpty(applNo)){
					nyukyoDate = targetDt.getNyukyoYoteiDate();
				}
				
				//社宅退居、社宅変更、駐車場入居、駐車場退居の場合
				if((CodeConstant.SHINSEI_KBN_SHIATAKU.equals(applKbn) && 
						CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(nyutaikyoKbn)) ||
					(CodeConstant.NYUTAIKYO_KBN_HENKO.equals(nyutaikyoKbn)) ||
					(CodeConstant.SHINSEI_KBN_PARKING.equals(applKbn) && 
						(CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(nyutaikyoKbn)) || CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(nyutaikyoKbn)) ){
					//使用料パターンを取得
					Skf3030TRentalPattern dtRP = null;
					if(!SkfCheckUtils.isNullOrEmpty(rentalPatternId)){
						Skf3030TRentalPatternKey rpKey = new Skf3030TRentalPatternKey();
/* US 結合1041対応 */
//						rpKey.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
						rpKey.setShatakuKanriNo(shatakuKanriNo);
/* UE 結合1041対応 */
						rpKey.setRentalPatternId(Long.parseLong(rentalPatternId));
						dtRP = skf3030TRentalPatternRepository.selectByPrimaryKey(rpKey);
					}
					if(dtRP != null){
						ause = dtRP.getAuse();
					}else{
						ause = targetDt.getAuse();
					}
				}else{
					ause = targetDt.getAuse();
				}
				
				//排他チェック
				Date mapDate = null;
				try{
					mapDate = dateFormat.parse(hdnUpdateDate);	
					LogUtils.debugByMsg("hdnUpdateDate：" + mapDate);
				}	
				catch(ParseException ex){
					LogUtils.infoByMsg("insertTeijiDataInfo, 入退居予定データ-更新日時変換NG :" + hdnUpdateDate);
					return -2;
				}
//				LogUtils.debugByMsg("UpdateDate：" + targetDt.getUpdateDate());
//				super.checkLockException(targetDt.getUpdateDate(), mapDate);
				
				//入退居予定情報を更新				
				updCount = updCount + updateNyutaikyoYoteiInfoOfCreateKbn(nyukyoDate,
						 ause, teijiNo, mapDate, shainNo, nyutaikyoKbn);
			}else{
				return -1;
			}
		}
		
//		if(0 < updCount && 0 < insCount){
//			//コミット
//		}else{
		if(updCount <= 0 && insCount <= 0){
			//登録エラーメッセージ
			return -2;
		}
		
		
		return updCount + insCount;
	}

	/**
	 * 提示備品データ情報の取得
	 * @param teijiNo 提示番号
	 * @param nyutaikyoKbn 入退居区分
	 * @param applKbn 申請区分
	 * @param applNo 申請書類管理番号
	 * @param oldBihinCount
	 * @param bihinCd 備品コード
	 * @param bihinLentStatusKbn 備品貸与状態区分
	 * @return カラム情報リスト
	 */
	private Skf3022TTeijiBihinData GetColumnInfoListOfTeijiBihinData(
			Long teijiNo,
			String nyutaikyoKbn,
			String applKbn,
			String applNo,
			int oldBihinCount,
			String bihinCd,
			String bihinLentStatusKbn){
				
		Skf3022TTeijiBihinData columnInfoList = new Skf3022TTeijiBihinData();
		//提示番号
		columnInfoList.setTeijiNo(teijiNo);
		//備品コード
		columnInfoList.setBihinCd(bihinCd);
		
		//駐車場（入居）／駐車場（退居）／変更
		if((CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(nyutaikyoKbn) && 
				CodeConstant.SHINSEI_KBN_PARKING.equals(applKbn)) ||
			(CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(nyutaikyoKbn) && 
				CodeConstant.SHINSEI_KBN_PARKING.equals(applKbn)) ||
			(CodeConstant.NYUTAIKYO_KBN_HENKO.equals(nyutaikyoKbn))){
			//退居、かつ、備品コードは"91"、"92"、"93"の場合
			if(CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(nyutaikyoKbn) &&
					(BIHIN_CD_91.equals(bihinCd) || BIHIN_CD_92.equals(bihinCd) || BIHIN_CD_93.equals(bihinCd))){
				//旧備品データを取得
				//GetKyuTaiyoBihinInfo⇒対象テーブルが存在しないので、データなし処理のみ実装
				//備品貸与状態区分
				columnInfoList.setBihinLentStatusKbn(bihinLentStatusKbn);
//                '返却チェックは"1"の場合
//                If dt.Rows.Count > 0 AndAlso _
//                    RETURN_FLG.Equals(dt.Rows(0).Item(DATA_KEY_RETURN_FLG).ToString) Then
//                    oldBihinCount = oldBihinCount + 1
//                    '備品貸与状態区分
//                    columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtPublicTeijiBihinData.BIHIN_LENT_STATUS_KBN, _
//                                                            Constant.BihinLentStatusKbn.NASHI, _
//                                                            OracleType.Char))
//                Else
//                    '備品貸与状態区分
//                    columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtPublicTeijiBihinData.BIHIN_LENT_STATUS_KBN, _
//                                                            bihinLentStatusKbn, _
//                                                            OracleType.Char))
//                End If
				
			}else{
				//備品貸与状態区分
				columnInfoList.setBihinLentStatusKbn(bihinLentStatusKbn);
			}
		}
		if(CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(nyutaikyoKbn) && 
			(SkfCheckUtils.isNullOrEmpty(applNo) || CodeConstant.SHINSEI_KBN_SHIATAKU.equals(applKbn))){
			//備品貸与状態区分
			columnInfoList.setBihinLentStatusKbn(bihinLentStatusKbn);
		}
		
		return columnInfoList;
	}
	
	/**
	 * 提示備品データ情報登録メソッド
	 * @param columnInfoList カラム情報
	 * @return 登録件数
	 */
	private int insertTeijiBihinDataInfo(Skf3022TTeijiBihinData columnInfoList){
		int result = 0;
		
		result = skf3022TTeijiBihinDataRepository.insertSelective(columnInfoList);
		
		return result;
	}
	
	
	/**
	 * 提示データ情報の取得（変更（申請なし）の場合）
	 * @param grvNyuTaikyoYoteiIchiran 入退居予定一覧
	 * @param dtSL 社宅管理台帳基本情報
	 * @param dtNCT 入居希望等調書・入居決定通知情報
	 * @param shainNo 社員番号
	 * @param nyutaikyoKbn 入退居区分
	 * @param name 社員氏名
	 * @param applKbn 申請区分
	 * @param applNo 申請書類管理番号
	 * @param taikyoYoteiDate 退居予定日
	 * @param companyCd 会社コード
	 * @param yearMonth 年月
	 * @param teijiNo 提示番号
	 * @return カラム情報リスト
	 */
	private Skf3022TTeijiData getColumnInfoListOfTeijiData(
			List<Map<String,Object>> grvNyuTaikyoYoteiIchiran,
			Skf3030TShatakuLedger dtSL,
			List<Skf3021Sc001GetNyukyoChoshoTsuchiInfoExp> dtNCT,
			String shainNo,
			String nyutaikyoKbn,
			String name,
			String applKbn,
			String applNo,
			String taikyoYoteiDate,
			String companyCd,
			String yearMonth,
/* AS 結合1041対応 */
			String nowAffiliation,
			String newAffiliation,
/* AE 結合1041対応 */
			Long teijiNo){
		
		Skf3022TTeijiData columnInfoList = new Skf3022TTeijiData();
		
		//提示番号
		columnInfoList.setTeijiNo(teijiNo);
		//社員番号
		columnInfoList.setShainNo(shainNo);
		//入退居区分
		columnInfoList.setNyutaikyoKbn(nyutaikyoKbn);
		//社員名
		columnInfoList.setName(name);
		//申請区分
		columnInfoList.setApplKbn(applKbn);
/* AS 結合1041対応 */
		// 現所属
		columnInfoList.setNowAffiliation(nowAffiliation);
		// 新所属
		columnInfoList.setNewAffiliation(newAffiliation);
/* AE 結合1041対応 */
		
		//社宅管理番号
		if(dtSL.getShatakuKanriNo() != null){
			columnInfoList.setShatakuKanriNo(dtSL.getShatakuKanriNo());;
		}
		//部屋管理番号
		if(dtSL.getShatakuRoomKanriNo() != null){
			columnInfoList.setShatakuRoomKanriNo(dtSL.getShatakuRoomKanriNo());
		}
		//居住者区分
		if(dtSL.getKyojushaKbn() != null){
			columnInfoList.setKyojushaKbn(dtSL.getKyojushaKbn());
		}
		
		//月別使用料履歴情報を取得
		Skf3030TShatakuRentalRirekiKey rirekiKey = new Skf3030TShatakuRentalRirekiKey();
		rirekiKey.setShatakuKanriId(dtSL.getShatakuKanriId());
		rirekiKey.setYearMonth(yearMonth);
		Skf3030TShatakuRentalRireki dtSRR = null;
		dtSRR = skf3030TShatakuRentalRirekiRepository.selectByPrimaryKey(rirekiKey);
		if(dtSRR != null){
			//使用料パターンＩＤ
			columnInfoList.setRentalPatternId(dtSRR.getRentalPatternId());
			//役員算定
			columnInfoList.setYakuinSannteiKbn(dtSRR.getYakuinSannteiKbn());
			//社宅使用料日割金額
			columnInfoList.setRentalDay(dtSRR.getRentalDay());
			//社宅使用料調整金額
			columnInfoList.setRentalAdjust(dtSRR.getRentalAdjust());
			//個人負担共益費
			columnInfoList.setKyoekihiPerson(dtSRR.getKyoekihiPerson());
			//個人負担共益費調整金額
			columnInfoList.setKyoekihiPersonAdjust(dtSRR.getKyoekihiPersonAdjust());
			//共益金支払月
			columnInfoList.setKyoekihiPayMonth(dtSRR.getKyoekihiPayMonth());
			//駐車場使用料調整金額
			columnInfoList.setParkingRentalAdjust(dtSRR.getParkingRentalAdjust());
			//駐車場日割金額１
			columnInfoList.setParking1RentalDay(dtSRR.getParking1RentalDay());
			//駐車場日割金額２
			columnInfoList.setParking2RentalDay(dtSRR.getParking2RentalDay());
			//社宅使用料月額
			columnInfoList.setRentalMonth(dtSRR.getRentalMonth());
			//駐車場区画１使用料月額
			columnInfoList.setParking1RentalMonth(dtSRR.getParking1RentalMonth());
			//駐車場区画２使用料月額
			columnInfoList.setParking2RentalMonth(dtSRR.getParking2RentalMonth());
			dtSRR = null;
		}
		
		//月別駐車場履歴情報を取得
		Skf3030TParkingRirekiKey parkRirekiKey = new Skf3030TParkingRirekiKey();
		parkRirekiKey.setShatakuKanriId(dtSL.getShatakuKanriId());
		parkRirekiKey.setYearMonth(yearMonth);
		parkRirekiKey.setParkingLendNo(1L);
		Skf3030TParkingRireki dtPR = null;
		dtPR = skf3030TParkingRirekiRepository.selectByPrimaryKey(parkRirekiKey);
		if(dtPR != null){
			//駐車場管理番号1
			columnInfoList.setParkingKanriNo1(dtPR.getParkingKanriNo());
			if(dtSL.getShatakuKanriNo() != null){
				//社宅駐車場区画情報マスタを取得
				Skf3010MShatakuParkingBlockKey blockKey = new Skf3010MShatakuParkingBlockKey();
				blockKey.setShatakuKanriNo(dtSL.getShatakuKanriNo());
				blockKey.setParkingKanriNo(dtPR.getParkingKanriNo());
				Skf3010MShatakuParkingBlock dtSPB = null;
				dtSPB = skf3010MShatakuParkingBlockRepository.selectByPrimaryKey(blockKey);
				if(dtSPB != null){
					//駐車場区画１番号
					columnInfoList.setParkingBlock1(dtSPB.getParkingBlock());
				}
			}
			//駐車場区画１開始日
			columnInfoList.setParking1StartDate(dtPR.getParkingStartDate());
			//駐車場区画１終了日
			columnInfoList.setParking1EndDate(dtPR.getParkingEndDate());
			
			dtPR = null;
		}
		//月別駐車場履歴情報を取得
		Skf3030TParkingRirekiKey parkRireki2Key = new Skf3030TParkingRirekiKey();
		parkRireki2Key.setShatakuKanriId(dtSL.getShatakuKanriId());
		parkRireki2Key.setYearMonth(yearMonth);
		parkRireki2Key.setParkingLendNo(2L);
		Skf3030TParkingRireki dtPR2 = null;
		dtPR2 = skf3030TParkingRirekiRepository.selectByPrimaryKey(parkRireki2Key);
		if(dtPR2 != null){
			//駐車場管理番号2
			columnInfoList.setParkingKanriNo2(dtPR2.getParkingKanriNo());
			if(dtSL.getShatakuKanriNo() != null){
				//社宅駐車場区画情報マスタを取得
				Skf3010MShatakuParkingBlockKey blockKey = new Skf3010MShatakuParkingBlockKey();
				blockKey.setShatakuKanriNo(dtSL.getShatakuKanriNo());
				blockKey.setParkingKanriNo(dtPR2.getParkingKanriNo());
				Skf3010MShatakuParkingBlock dtSPB = null;
				dtSPB = skf3010MShatakuParkingBlockRepository.selectByPrimaryKey(blockKey);
				if(dtSPB != null){
					//駐車場区画2番号
					columnInfoList.setParkingBlock2(dtSPB.getParkingBlock());
				}
			}
			//駐車場区画2開始日
			columnInfoList.setParking2StartDate(dtPR2.getParkingStartDate());
			//駐車場区画2終了日
			columnInfoList.setParking2EndDate(dtPR2.getParkingEndDate());
			
			dtPR2 = null;
		}
		
		//社宅管理台帳備品基本情報を取得
		Skf3030TShatakuBihin dtSB = null;
		dtSB = skf3030TShatakuBihinRepository.selectByPrimaryKey(dtSL.getShatakuKanriId());
		if(dtSB != null){
			//備品貸与日
			columnInfoList.setEquipmentStartDate(dtSB.getTaiyoDate());
			//搬入希望日
			columnInfoList.setCarryinRequestDay(dtSB.getHannyuRequestDay());
			//搬入希望時間区分
			columnInfoList.setCarryinRequestKbn(dtSB.getHannyuRequestKbn());
			//受入本人連絡先
			columnInfoList.setUkeireMyApoint(dtSB.getUkeireMyApoint());
			//受入代理人氏名
			columnInfoList.setUkeireDairiName(dtSB.getUkeireDairiName());
			//受入代理人連絡先
			columnInfoList.setUkeireDairiApoint(dtSB.getUkeireDairiApoint());
			//備品備考
			columnInfoList.setBihinBiko(dtSB.getBihinBiko());
			
			dtSB = null;
		}
		
		//社宅管理台帳相互利用基本情報を取得
		Skf3030TShatakuMutual dtSM = null;
		dtSM = skf3030TShatakuMutualRepository.selectByPrimaryKey(dtSL.getShatakuKanriId());
		if(dtSM != null){
			//貸付会社コード
			columnInfoList.setKashitukeCompanyCd(dtSM.getKashitukeCompanyCd());
			//借受会社コード
			columnInfoList.setKariukeCompanyCd(dtSM.getKariukeCompanyCd());
			//社宅賃貸料
			columnInfoList.setRent(dtSM.getRent());
			//駐車場料金
			columnInfoList.setParkingRental(dtSM.getParkingRental());
			//共益費（事業者負担）
			columnInfoList.setKyoekihiBusiness(dtSM.getKyoekihiBusiness());
			//相互利用開始日
			columnInfoList.setMutualUseStartDay(dtSM.getMutualUseStartDay());
			//相互利用終了日
			columnInfoList.setMutualUseEndDay(dtSM.getMutualUseEndDay());
			//相互利用判定区分
			columnInfoList.setMutualUseKbn(dtSM.getMutualUseKbn());
			//社宅使用料会社間送金区分
			columnInfoList.setShatakuCompanyTransferKbn(dtSM.getShatakuCompanyTransferKbn());
			//共益費会社間送金区分
			columnInfoList.setKyoekihiCompanyTransferKbn(dtSM.getKyoekihiCompanyTransferKbn());
			//相互利用状況
			columnInfoList.setMutualJokyo(dtSM.getMutualJokyo());
			
			dtSM = null;
		}
		
		//月別相互利用履歴情報を取得
		Skf3030TShatakuMutualRirekiKey mtaRirekiKey = new Skf3030TShatakuMutualRirekiKey();
		mtaRirekiKey.setShatakuKanriId(dtSL.getShatakuKanriId());
		mtaRirekiKey.setYearMonth(yearMonth);
		Skf3030TShatakuMutualRireki dtSMR = null;
		dtSMR = skf3030TShatakuMutualRirekiRepository.selectByPrimaryKey(mtaRirekiKey);
		if(dtSMR != null){
			//配属会社コード
			columnInfoList.setAssignCompanyCd(dtSMR.getAssignCompanyCd());
			//所属機関
			columnInfoList.setAgency(dtSMR.getAssignAgencyName());
			//室・部名
			columnInfoList.setAffiliation1(dtSMR.getAssignAffiliation1());
			//課等名
			columnInfoList.setAffiliation2(dtSMR.getAssignAffiliation2());
			//配属データコード番号
			columnInfoList.setAssignCd(dtSMR.getAssignCd());
			
			dtSMR = null;
		}
		
		//原籍会社コード
		if(dtSL.getOriginalCompanyCd() != null){
			columnInfoList.setOriginalCompanyCd(dtSL.getOriginalCompanyCd());
		}
		//給与支給会社区分
		if(dtSL.getPayCompanyCd() != null){
			columnInfoList.setPayCompanyCd(dtSL.getPayCompanyCd());
		}
		//社宅管理台帳ID
		columnInfoList.setShatakuKanriId(dtSL.getShatakuKanriId());
		//社宅提示ステータス
		columnInfoList.setShatakuTeijiStatus(CodeConstant.PRESENTATION_SITUATION_SAKUSEI_CHU);
		//作成完了区分
		columnInfoList.setCreateCompleteKbn(CodeConstant.MI_SAKUSEI);
		//台帳作成区分
		columnInfoList.setLandCreateKbn(CodeConstant.LAND_CREATE_KBN_MI_SAKUSEI);
		
		return columnInfoList;
		
	}
	
	/**
	 * 提示データ情報の取得（変更（申請なし）以外の場合）
	 * @param grvNyuTaikyoYoteiIchiran 入退居予定一覧
	 * @param dtSL 社宅管理台帳基本情報
	 * @param dtNCT 入居希望等調書・入居決定通知情報
	 * @param dtTR 退居（自動車の保管場所返還）届情報
	 * @param shainNo 社員番号
	 * @oaram returnCount 返却要件数
	 * @param nyutaikyoKbn 入退居区分
	 * @param name 社員氏名
	 * @param applKbn 申請区分
	 * @param applNo 申請書類管理番号
	 * @param taikyoYoteiDate 退居予定日
	 * @param companyCd 会社コード
	 * @param yearMonth 年月
	 * @param parking1StartDate 駐車場区画１開始日
	 * @param parking2StartDate 駐車場区画２開始日
	 * @param teijiNo 提示番号
	 * @param nyutaikyoKbnOfSameShainNo 入退居区分（同一社員番号）
	 * @param rentalPatternId 使用料パターンＩＤ
	 * @return
	 */
	private Skf3022TTeijiData getColumnInfoListOfTeijiData(
			List<Map<String,Object>> grvNyuTaikyoYoteiIchiran,
			List<Skf3021Sc001GetShatakuLedgerInfoExp> dtSL,
			List<Skf3021Sc001GetNyukyoChoshoTsuchiInfoExp> dtNCT,
			List<Skf3021Sc001GetTaikyoReportInfoExp> dtTR,
			String shainNo,
			int returnCount,
			String nyutaikyoKbn,
			String name,
			String applKbn,
			String applNo,
			String taikyoYoteiDate,
			String companyCd,
			String yearMonth,
			String parking1StartDate,
			String parking2StartDate,
/* AS 結合1041対応 */
			String nowAffiliation,
			String newAffiliation,
/* AE 結合1041対応 */
			Long teijiNo,
			String nyutaikyoKbnOfSameShainNo,
			String rentalPatternId){
		
		Skf3022TTeijiData columnInfoList = new Skf3022TTeijiData();
		
		
		int nyukyoCount = 0;
		int taikyoCount = 0;
		int henkouCount = 0;
		
		for(Map<String,Object> row1 : grvNyuTaikyoYoteiIchiran){
			//社員番号
			String shainNo1 = row1.get("colShainNo").toString();
			//入退居区分
			String nyutaikyoKbn1 = row1.get("hdnNyutaikyoKbnCd").toString();
			if(shainNo.compareTo(shainNo1) == 0){
				if(CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(nyutaikyoKbn1)){
					nyukyoCount += 1;
				}else if(CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(nyutaikyoKbn1)){
					taikyoCount += 1;
				}else if(CodeConstant.NYUTAIKYO_KBN_HENKO.equals(nyutaikyoKbn1)){
					henkouCount += 1;
				}
			}
		}

		if(nyukyoCount == 1 && taikyoCount == 1 && henkouCount == 0 
				&& CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(nyutaikyoKbn)){
			//入退居の退居
			nyutaikyoKbnOfSameShainNo = TAIKYO;
		}else if( nyukyoCount == 0 && taikyoCount == 1 && henkouCount == 0){
			//退居のみ
			nyutaikyoKbnOfSameShainNo = TAIKYOONLY;
		}
		if(CodeConstant.NYUTAIKYO_KBN_HENKO.equals(nyutaikyoKbn)){
			nyutaikyoKbnOfSameShainNo = HENKOU;
		}
		
		//提示番号
		columnInfoList.setTeijiNo(teijiNo);
		//社員番号
		columnInfoList.setShainNo(shainNo);
		//入退居区分
		columnInfoList.setNyutaikyoKbn(nyutaikyoKbn);
		//社員名
		columnInfoList.setName(name);
		//申請区分
		columnInfoList.setApplKbn(applKbn);
/* AS 結合1041対応 */
		// 現所属
		columnInfoList.setNowAffiliation(nowAffiliation);
		// 新所属
		columnInfoList.setNewAffiliation(newAffiliation);
/* AE 結合1041対応 */
		
		//社宅（駐車場を含む）
		if(CodeConstant.SHINSEI_KBN_SHIATAKU.equals(applKbn)){
			//入居
			if(CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(nyutaikyoKbn)){
				if(dtNCT.size() > 0){
					//備品貸与区分
					columnInfoList.setBihinTaiyoKbn(dtNCT.get(0).getBihinKibo());
				}
				columnInfoList.setBihinTeijiStatus(CodeConstant.DOUBLE_QUOTATION);
			}
			//退居／変更
			else if(CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(nyutaikyoKbn) || CodeConstant.NYUTAIKYO_KBN_HENKO.equals(nyutaikyoKbn)){
				//社宅管理番号
				String syatakuNo = CodeConstant.DOUBLE_QUOTATION;
				//部屋管理番号
				String heyaNo = CodeConstant.DOUBLE_QUOTATION;
				//搬出希望日
				String carryoutRequestDay = CodeConstant.DOUBLE_QUOTATION;
				//搬出希望時間区分
				String carryoutRequestKbn = CodeConstant.DOUBLE_QUOTATION;
				//相互利用終了日
				String mutualUseEndDay = CodeConstant.DOUBLE_QUOTATION;
				
				if(HENKOU.equals(nyutaikyoKbnOfSameShainNo)){
					//変更
					if(dtNCT.size() > 0){
						if(dtNCT.get(0).getShatakuNo() != null){
							syatakuNo = dtNCT.get(0).getShatakuNo().toString();
						}
						if(dtNCT.get(0).getRoomKanriNo() != null){
							heyaNo = dtNCT.get(0).getRoomKanriNo().toString();
						}
						mutualUseEndDay = dtNCT.get(0).getTaikyoYoteiDate();
//						if(TAIKYO.equals(nyutaikyoKbnOfSameShainNo)){
//							//入退居の退居(2019/12:このルートは通らないためコメントアウト)
//							carryoutRequestDay = dtNCT.get(0).getSessionDay();
//							carryoutRequestKbn = dtNCT.get(0).getSessionTime();
//						}
					}
					
				}else if(TAIKYO.equals(nyutaikyoKbnOfSameShainNo) || TAIKYOONLY.equals(nyutaikyoKbnOfSameShainNo)){
					//入退居の退居、退居のみ
					if(dtTR.size() > 0){
						if(dtTR.get(0).getShatakuNo() != null){
							syatakuNo = dtTR.get(0).getShatakuNo().toString();
						}
						if(dtTR.get(0).getRoomKanriNo() != null){
							heyaNo = dtTR.get(0).getRoomKanriNo().toString();
						}
						carryoutRequestDay = dtTR.get(0).getSessionDay();
						carryoutRequestKbn = dtTR.get(0).getSessionTime();
						mutualUseEndDay = dtTR.get(0).getTaikyoDate();
					}
				}
				
				if(!SkfCheckUtils.isNullOrEmpty(taikyoYoteiDate)){
					if(dtTR.size() > 0){
						//退居（自動車の保管場所返還）届テーブル.退居する社宅区分2が、"1"または"3"、かつ一覧の駐車場開始日１が空白でない場合
						if((PARKING1.equals(dtTR.get(0).getShatakuTaikyKbn2()) ||
							PARKING12.equals(dtTR.get(0).getShatakuTaikyKbn2())) 
							&& !SkfCheckUtils.isNullOrEmpty(parking1StartDate)){
							//駐車場区画１終了日
							columnInfoList.setParking1EndDate(taikyoYoteiDate);
						}
						//退居（自動車の保管場所返還）届テーブル.退居する社宅区分2が、"2"または"3"、かつ一覧の駐車場開始日２が空白でない場合
						if((PARKING2.equals(dtTR.get(0).getShatakuTaikyKbn2()) ||
							PARKING12.equals(dtTR.get(0).getShatakuTaikyKbn2())) 
							&& !SkfCheckUtils.isNullOrEmpty(parking2StartDate)){
							//駐車場区画２終了日
							columnInfoList.setParking2EndDate(taikyoYoteiDate);
						}
					}
				}
				
				if(dtSL != null){
					//社宅管理番号
					columnInfoList.setShatakuKanriNo(Long.parseLong(syatakuNo));
					//部屋管理番号
					columnInfoList.setShatakuRoomKanriNo(Long.parseLong(heyaNo));
					//居住者区分
					columnInfoList.setKyojushaKbn(dtSL.get(0).getKyojushaKbn());
					
					//月別使用料履歴情報を取得
					Skf3030TShatakuRentalRirekiKey rirekiKey = new Skf3030TShatakuRentalRirekiKey();
					rirekiKey.setShatakuKanriId(dtSL.get(0).getShatakuKanriId());
					rirekiKey.setYearMonth(yearMonth);
					Skf3030TShatakuRentalRireki dtSRR = null;
					dtSRR = skf3030TShatakuRentalRirekiRepository.selectByPrimaryKey(rirekiKey);
					if(dtSRR != null){
						//使用料パターンＩＤ
						//rentalPatternId = dtSRR.getRentalPatternId().toString();
						columnInfoList.setRentalPatternId(dtSRR.getRentalPatternId());
						//役員算定
						columnInfoList.setYakuinSannteiKbn(dtSRR.getYakuinSannteiKbn());
						//社宅使用料日割金額
						columnInfoList.setRentalDay(dtSRR.getRentalDay());
						//社宅使用料調整金額
						columnInfoList.setRentalAdjust(dtSRR.getRentalAdjust());
						//個人負担共益費
						columnInfoList.setKyoekihiPerson(dtSRR.getKyoekihiPerson());
						//個人負担共益費調整金額
						columnInfoList.setKyoekihiPersonAdjust(dtSRR.getKyoekihiPersonAdjust());
						//共益金支払月
						columnInfoList.setKyoekihiPayMonth(dtSRR.getKyoekihiPayMonth());
						//駐車場使用料調整金額
						columnInfoList.setParkingRentalAdjust(dtSRR.getParkingRentalAdjust());
						//駐車場日割金額１
						columnInfoList.setParking1RentalDay(dtSRR.getParking1RentalDay());
						//駐車場日割金額２
						columnInfoList.setParking2RentalDay(dtSRR.getParking2RentalDay());
						//社宅使用料月額
						columnInfoList.setRentalMonth(dtSRR.getRentalMonth());
						//駐車場月額１
						columnInfoList.setParking1RentalMonth(dtSRR.getParking1RentalMonth());
						//駐車場月額２
						columnInfoList.setParking2RentalMonth(dtSRR.getParking2RentalMonth());
						
						dtSRR = null;
					}
					
					//月別駐車場履歴情報を取得
					Skf3030TParkingRirekiKey parkRirekiKey = new Skf3030TParkingRirekiKey();
					parkRirekiKey.setShatakuKanriId(dtSL.get(0).getShatakuKanriId());
					parkRirekiKey.setYearMonth(yearMonth);
					parkRirekiKey.setParkingLendNo(1L);
					Skf3030TParkingRireki dtPR = null;
					dtPR = skf3030TParkingRirekiRepository.selectByPrimaryKey(parkRirekiKey);
					if(dtPR != null){
						if(dtTR.size() > 0){
							//退居（自動車の保管場所返還）届テーブル.退居する社宅区分2が、"1"または"3"、かつ一覧の駐車場開始日１が空白でない場合
							if((PARKING1.equals(dtTR.get(0).getShatakuTaikyKbn2()) ||
								PARKING12.equals(dtTR.get(0).getShatakuTaikyKbn2()))
								&& !SkfCheckUtils.isNullOrEmpty(parking1StartDate)){
								//駐車場管理番号１
								columnInfoList.setParkingKanriNo1(dtPR.getParkingKanriNo());
								//社宅駐車場区画情報マスタを取得
								Skf3010MShatakuParkingBlockKey blockKey = new Skf3010MShatakuParkingBlockKey();
								blockKey.setShatakuKanriNo(Long.parseLong(syatakuNo));
								blockKey.setParkingKanriNo(dtPR.getParkingKanriNo());
								Skf3010MShatakuParkingBlock dtSPB = null;
								dtSPB = skf3010MShatakuParkingBlockRepository.selectByPrimaryKey(blockKey);
								if(dtSPB != null){
									//駐車場区画１番号
									columnInfoList.setParkingBlock1(dtSPB.getParkingBlock());
									dtSPB = null;
								}
								blockKey = null;
							}
						}
						//変更（現社宅の継続利用）の場合
						else if(CodeConstant.NYUTAIKYO_KBN_HENKO.equals(nyutaikyoKbn)){
							//駐車場管理番号１
							columnInfoList.setParkingKanriNo1(dtPR.getParkingKanriNo());
							//駐車場区画１終了日
							columnInfoList.setParking1EndDate(dtPR.getParkingEndDate());
							//社宅駐車場区画情報マスタを取得
							Skf3010MShatakuParkingBlockKey blockKey = new Skf3010MShatakuParkingBlockKey();
							blockKey.setShatakuKanriNo(Long.parseLong(syatakuNo));
							blockKey.setParkingKanriNo(dtPR.getParkingKanriNo());
							Skf3010MShatakuParkingBlock dtSPB = null;
							dtSPB = skf3010MShatakuParkingBlockRepository.selectByPrimaryKey(blockKey);
							if(dtSPB != null){
								//駐車場区画１番号
								columnInfoList.setParkingBlock1(dtSPB.getParkingBlock());
								dtSPB = null;
							}
							blockKey = null;
						}
						dtPR = null;
					}
					//月別駐車場履歴情報を取得
					Skf3030TParkingRirekiKey park2RirekiKey = new Skf3030TParkingRirekiKey();
					park2RirekiKey.setShatakuKanriId(dtSL.get(0).getShatakuKanriId());
					park2RirekiKey.setYearMonth(yearMonth);
					park2RirekiKey.setParkingLendNo(2L);
					Skf3030TParkingRireki dtPR2 = null;
					dtPR2 = skf3030TParkingRirekiRepository.selectByPrimaryKey(park2RirekiKey);
					if(dtPR2 != null){
						if(dtTR.size() > 0){
							//退居（自動車の保管場所返還）届テーブル.退居する社宅区分2が、"2"または"3"、かつ一覧の駐車場開始日2が空白でない場合
							if((PARKING2.equals(dtTR.get(0).getShatakuTaikyKbn2()) ||
								PARKING12.equals(dtTR.get(0).getShatakuTaikyKbn2()))
								&& !SkfCheckUtils.isNullOrEmpty(parking2StartDate)){
								//駐車場管理番号２
								columnInfoList.setParkingKanriNo2(dtPR2.getParkingKanriNo());
								//社宅駐車場区画情報マスタを取得
								Skf3010MShatakuParkingBlockKey blockKey = new Skf3010MShatakuParkingBlockKey();
								blockKey.setShatakuKanriNo(Long.parseLong(syatakuNo));
								blockKey.setParkingKanriNo(dtPR2.getParkingKanriNo());
								Skf3010MShatakuParkingBlock dtSPB = null;
								dtSPB = skf3010MShatakuParkingBlockRepository.selectByPrimaryKey(blockKey);
								if(dtSPB != null){
									//駐車場区画２番号
									columnInfoList.setParkingBlock2(dtSPB.getParkingBlock());
									dtSPB = null;
								}
								blockKey = null;
							}
						}
						//変更（現社宅の継続利用）の場合
						else if(CodeConstant.NYUTAIKYO_KBN_HENKO.equals(nyutaikyoKbn)){
							//駐車場管理番号２
							columnInfoList.setParkingKanriNo2(dtPR2.getParkingKanriNo());
							//駐車場区画２終了日
							columnInfoList.setParking2EndDate(dtPR2.getParkingEndDate());
							//社宅駐車場区画情報マスタを取得
							Skf3010MShatakuParkingBlockKey blockKey = new Skf3010MShatakuParkingBlockKey();
							blockKey.setShatakuKanriNo(Long.parseLong(syatakuNo));
							blockKey.setParkingKanriNo(dtPR2.getParkingKanriNo());
							Skf3010MShatakuParkingBlock dtSPB = null;
							dtSPB = skf3010MShatakuParkingBlockRepository.selectByPrimaryKey(blockKey);
							if(dtSPB != null){
								//駐車場区画２番号
								columnInfoList.setParkingBlock2(dtSPB.getParkingBlock());
								dtSPB = null;
							}
							blockKey = null;
						}
						dtPR2 = null;
					}
					
					//社宅管理台帳備品基本情報を取得
					Skf3030TShatakuBihin dtSB = null;
					dtSB = skf3030TShatakuBihinRepository.selectByPrimaryKey(dtSL.get(0).getShatakuKanriId());
					if(dtSB != null){
						//備品貸与日
						columnInfoList.setEquipmentStartDate(dtSB.getTaiyoDate());
						//搬入希望日
						columnInfoList.setCarryinRequestDay(dtSB.getHannyuRequestDay());
						//搬入希望時間区分
						columnInfoList.setCarryinRequestKbn(dtSB.getHannyuRequestKbn());
						//受入本人連絡先
						columnInfoList.setUkeireMyApoint(dtSB.getUkeireMyApoint());
						//受入代理人氏名
						columnInfoList.setUkeireDairiName(dtSB.getUkeireDairiName());
						//受入代理人連絡先
						columnInfoList.setUkeireDairiApoint(dtSB.getUkeireDairiApoint());
						//備品備考
						columnInfoList.setBihinBiko(dtSB.getBihinBiko());
						
						dtSB = null;
					}
					
					//社宅管理台帳相互利用基本情報を取得
					Skf3030TShatakuMutual dtSM = null;
					dtSM = skf3030TShatakuMutualRepository.selectByPrimaryKey(dtSL.get(0).getShatakuKanriId());
					if(dtSM != null){
						//貸付会社コード
						columnInfoList.setKashitukeCompanyCd(dtSM.getKashitukeCompanyCd());
						//借受会社コード
						columnInfoList.setKariukeCompanyCd(dtSM.getKariukeCompanyCd());
						//社宅賃貸料
						columnInfoList.setRent(dtSM.getRent());
						//駐車場料金
						columnInfoList.setParkingRental(dtSM.getParkingRental());
						//共益費（事業者負担）
						columnInfoList.setKyoekihiBusiness(dtSM.getKyoekihiBusiness());
						//相互利用開始日
						columnInfoList.setMutualUseStartDay(dtSM.getMutualUseStartDay());
						//相互利用終了日
						columnInfoList.setMutualUseEndDay(dtSM.getMutualUseEndDay());
						//相互利用判定区分
						columnInfoList.setMutualUseKbn(dtSM.getMutualUseKbn());
						//社宅使用料会社間送金区分
						columnInfoList.setShatakuCompanyTransferKbn(dtSM.getShatakuCompanyTransferKbn());
						//共益費会社間送金区分
						columnInfoList.setKyoekihiCompanyTransferKbn(dtSM.getKyoekihiCompanyTransferKbn());
						//相互利用状況
						columnInfoList.setMutualJokyo(dtSM.getMutualJokyo());
						
						dtSM = null;
					}
					
					//相互利用終了日
					columnInfoList.setMutualUseEndDay(mutualUseEndDay);
					
					//月別相互利用履歴情報を取得
					Skf3030TShatakuMutualRirekiKey mtaRirekiKey = new Skf3030TShatakuMutualRirekiKey();
					mtaRirekiKey.setShatakuKanriId(dtSL.get(0).getShatakuKanriId());
					mtaRirekiKey.setYearMonth(yearMonth);
					Skf3030TShatakuMutualRireki dtSMR = null;
					dtSMR = skf3030TShatakuMutualRirekiRepository.selectByPrimaryKey(mtaRirekiKey);
					if(dtSMR != null){
						//配属会社コード
						columnInfoList.setAssignCompanyCd(dtSMR.getAssignCompanyCd());
						//所属機関
						columnInfoList.setAgency(dtSMR.getAssignAgencyName());
						//室・部名
						columnInfoList.setAffiliation1(dtSMR.getAssignAffiliation1());
						//課等名
						columnInfoList.setAffiliation2(dtSMR.getAssignAffiliation2());
						//配属データコード番号
						columnInfoList.setAssignCd(dtSMR.getAssignCd());
						
						dtSMR = null;
					}
					
					//原籍会社コード
					columnInfoList.setOriginalCompanyCd(dtSL.get(0).getOriginalCompanyCd());
					//給与支給会社区分
					columnInfoList.setPayCompanyCd(dtSL.get(0).getPayCompanyCd());
					//社宅管理台帳ID
					columnInfoList.setShatakuKanriId(dtSL.get(0).getShatakuKanriId());
				}
				
				//退居
				if(CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(nyutaikyoKbn)){
					//搬出希望日
					columnInfoList.setCarryoutRequestDay(carryoutRequestDay);
					//搬出希望時間区分
					columnInfoList.setCarryoutRequestKbn(carryoutRequestKbn);
				}
			}
			
			if(!SkfCheckUtils.isNullOrEmpty(parking1StartDate)){
				//駐車場区画１開始日
				columnInfoList.setParking1StartDate(parking1StartDate);
			}
			if(!SkfCheckUtils.isNullOrEmpty(parking2StartDate)){
				//駐車場区画２開始日
				columnInfoList.setParking2StartDate(parking2StartDate);
			}
		}
		//駐車場のみ
		else if(CodeConstant.SHINSEI_KBN_PARKING.equals(applKbn)){
			//社宅管理番号
			String syatakuNo = CodeConstant.DOUBLE_QUOTATION;
			
			//入居／退居
			if(CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(nyutaikyoKbn) || CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(nyutaikyoKbn)){
				//入居
				if(CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(nyutaikyoKbn)){
					if(dtNCT.size() > 0){
						if(dtNCT.get(0).getShatakuNo()!=null){
							syatakuNo = dtNCT.get(0).getShatakuNo().toString();
						}
						//社宅管理番号
						columnInfoList.setShatakuKanriNo(dtNCT.get(0).getShatakuNo());
						//部屋管理番号
						columnInfoList.setShatakuRoomKanriNo(dtNCT.get(0).getRoomKanriNo());
					}
					
					Skf3030TParkingRireki dtPR1 = null;
					Skf3030TParkingRireki dtPR2 = null;
					if(dtSL != null){
						//月別駐車場履歴情報を取得（貸与番号 = "1"）
						Skf3030TParkingRirekiKey parkRirekiKey = new Skf3030TParkingRirekiKey();
						parkRirekiKey.setShatakuKanriId(dtSL.get(0).getShatakuKanriId());
						parkRirekiKey.setYearMonth(yearMonth);
						parkRirekiKey.setParkingLendNo(1L);
						dtPR1 = skf3030TParkingRirekiRepository.selectByPrimaryKey(parkRirekiKey);
						//月別駐車場履歴情報を取得（貸与番号 = "2"）
						Skf3030TParkingRirekiKey parkRireki2Key = new Skf3030TParkingRirekiKey();
						parkRireki2Key.setShatakuKanriId(dtSL.get(0).getShatakuKanriId());
						parkRireki2Key.setYearMonth(yearMonth);
						parkRireki2Key.setParkingLendNo(2L);
						dtPR2 = skf3030TParkingRirekiRepository.selectByPrimaryKey(parkRireki2Key);
					}
					//①月別駐車場履歴情報（貸与番号 = "1"）がある、かつ、②月別駐車場履歴情報（貸与番号 = "2"）がないの場合
					if(dtPR1 != null && dtPR2 == null){
						//駐車場管理番号１
						columnInfoList.setParkingKanriNo1(dtPR1.getParkingKanriNo());
						//社宅駐車場区画情報マスタを取得
						Skf3010MShatakuParkingBlockKey blockKey = new Skf3010MShatakuParkingBlockKey();
						blockKey.setShatakuKanriNo(Long.parseLong(syatakuNo));
						blockKey.setParkingKanriNo(dtPR1.getParkingKanriNo());
						Skf3010MShatakuParkingBlock dtSPB = null;
						dtSPB = skf3010MShatakuParkingBlockRepository.selectByPrimaryKey(blockKey);
						if(dtSPB != null){
							//駐車場区画１番号
							columnInfoList.setParkingBlock1(dtSPB.getParkingBlock());
							dtSPB = null;
						}
						blockKey = null;
						
						//駐車場区画１開始日
						columnInfoList.setParking1StartDate(dtPR1.getParkingStartDate());
						//駐車場区画１終了日
						columnInfoList.setParking1EndDate(dtPR1.getParkingEndDate());
						
						//③入退居一覧.利用開始日１があるの場合
						if(!SkfCheckUtils.isNullOrEmpty(parking1StartDate)){
							//駐車場区画２開始日
							columnInfoList.setParking2StartDate(parking1StartDate);
						}
						//④入退居一覧.利用開始日２があるの場合
						if(!SkfCheckUtils.isNullOrEmpty(parking2StartDate)){
							//駐車場区画２開始日
							columnInfoList.setParking2StartDate(parking2StartDate);
						}
					}
					
					//①月別駐車場履歴情報（貸与番号 = "1"）がない、かつ、②月別駐車場履歴情報（貸与番号 = "2"）があるの場合
					if(dtPR1 == null && dtPR2 != null){
						//駐車場管理番号２
						columnInfoList.setParkingKanriNo2(dtPR2.getParkingKanriNo());
						//社宅駐車場区画情報マスタを取得
						Skf3010MShatakuParkingBlockKey blockKey = new Skf3010MShatakuParkingBlockKey();
						blockKey.setShatakuKanriNo(Long.parseLong(syatakuNo));
						blockKey.setParkingKanriNo(dtPR2.getParkingKanriNo());
						Skf3010MShatakuParkingBlock dtSPB = null;
						dtSPB = skf3010MShatakuParkingBlockRepository.selectByPrimaryKey(blockKey);
						if(dtSPB != null){
							//駐車場区画２番号
							columnInfoList.setParkingBlock2(dtSPB.getParkingBlock());
							dtSPB = null;
						}
						blockKey = null;
						//駐車場区画２開始日
						columnInfoList.setParking2StartDate(dtPR2.getParkingStartDate());
						//駐車場区画２終了日
						columnInfoList.setParking2EndDate(dtPR2.getParkingEndDate());
						
						//③入退居一覧.利用開始日１があるの場合
						if(!SkfCheckUtils.isNullOrEmpty(parking1StartDate)){
							//駐車場区画１開始日
							columnInfoList.setParking1StartDate(parking1StartDate);
						}
						//④入退居一覧.利用開始日２があるの場合
						if(!SkfCheckUtils.isNullOrEmpty(parking2StartDate)){
							//駐車場区画１開始日
							columnInfoList.setParking1StartDate(parking2StartDate);
						}
					}
					
					//①月別駐車場履歴情報（貸与番号 = "1"）がある、かつ、②月別駐車場履歴情報（貸与番号 = "2"）があるの場合
					if(dtPR1 != null && dtPR2 != null){
						//駐車場管理番号１
						columnInfoList.setParkingKanriNo1(dtPR1.getParkingKanriNo());
						//社宅駐車場区画情報マスタを取得
						Skf3010MShatakuParkingBlockKey blockKey = new Skf3010MShatakuParkingBlockKey();
						blockKey.setShatakuKanriNo(Long.parseLong(syatakuNo));
						blockKey.setParkingKanriNo(dtPR1.getParkingKanriNo());
						Skf3010MShatakuParkingBlock dtSPB = null;
						dtSPB = skf3010MShatakuParkingBlockRepository.selectByPrimaryKey(blockKey);
						if(dtSPB != null){
							//駐車場区画１番号
							columnInfoList.setParkingBlock1(dtSPB.getParkingBlock());
							dtSPB = null;
						}
						blockKey = null;
						
						//駐車場区画１開始日
						columnInfoList.setParking1StartDate(dtPR1.getParkingStartDate());
						//駐車場区画１終了日
						columnInfoList.setParking1EndDate(dtPR1.getParkingEndDate());
						
						//駐車場管理番号２
						columnInfoList.setParkingKanriNo2(dtPR2.getParkingKanriNo());
						//社宅駐車場区画情報マスタを取得
						Skf3010MShatakuParkingBlockKey block2Key = new Skf3010MShatakuParkingBlockKey();
						block2Key.setShatakuKanriNo(Long.parseLong(syatakuNo));
						block2Key.setParkingKanriNo(dtPR2.getParkingKanriNo());
						Skf3010MShatakuParkingBlock dtSPB2 = null;
						dtSPB2 = skf3010MShatakuParkingBlockRepository.selectByPrimaryKey(block2Key);
						if(dtSPB2 != null){
							//駐車場区画２番号
							columnInfoList.setParkingBlock2(dtSPB2.getParkingBlock());
							dtSPB2 = null;
						}
						blockKey = null;
						//駐車場区画２開始日
						columnInfoList.setParking2StartDate(dtPR2.getParkingStartDate());
						//駐車場区画２終了日
						columnInfoList.setParking2EndDate(dtPR2.getParkingEndDate());
					}
					
					//①月別駐車場履歴情報（貸与番号 = "1"）がない、かつ、②月別駐車場履歴情報（貸与番号 = "2"）がないの場合
					if(dtPR1 == null && dtPR2 == null){
						//③入退居一覧.利用開始日１があるの場合
						if(!SkfCheckUtils.isNullOrEmpty(parking1StartDate)){
							//駐車場区画１開始日
							columnInfoList.setParking1StartDate(parking1StartDate);
							
							//④入退居一覧.利用開始日２があるの場合
							if(!SkfCheckUtils.isNullOrEmpty(parking2StartDate)){
								//駐車場区画２開始日
								columnInfoList.setParking2StartDate(parking2StartDate);
							}
						}else{
							//④入退居一覧.利用開始日２があるの場合
							if(!SkfCheckUtils.isNullOrEmpty(parking2StartDate)){
								//駐車場区画２開始日
								columnInfoList.setParking1StartDate(parking2StartDate);
							}
						}

					}
				}
				//退居
				else{
					if(dtTR.size() > 0){
						if(dtTR.get(0).getShatakuNo() != null){
							syatakuNo = dtTR.get(0).getShatakuNo().toString();
						}
						//社宅管理番号
						columnInfoList.setShatakuKanriNo(dtTR.get(0).getShatakuNo());
						//部屋管理番号
						columnInfoList.setShatakuRoomKanriNo(dtTR.get(0).getRoomKanriNo());
						
						//退居（自動車の保管場所返還）届テーブル.退居する社宅区分2が、"1"または"3"の場合
						if(PARKING1.equals(dtTR.get(0).getShatakuTaikyKbn2()) || 
							PARKING12.equals(dtTR.get(0).getShatakuTaikyKbn2())) {
							//駐車場区画１終了日
							columnInfoList.setParking1EndDate(dtTR.get(0).getParkingHenkanDate());
						}
						//退居（自動車の保管場所返還）届テーブル.退居する社宅区分2が、"2"または"3"の場合
						if(PARKING2.equals(dtTR.get(0).getShatakuTaikyKbn2()) || 
								PARKING12.equals(dtTR.get(0).getShatakuTaikyKbn2())) {
								//駐車場区画２終了日
								columnInfoList.setParking2EndDate(dtTR.get(0).getParkingHenkanDate());
							}
					}
					
					//月別駐車場履歴情報を取得
					Skf3030TParkingRirekiKey parkRirekiKey = new Skf3030TParkingRirekiKey();
					parkRirekiKey.setShatakuKanriId(dtSL.get(0).getShatakuKanriId());
					parkRirekiKey.setYearMonth(yearMonth);
					parkRirekiKey.setParkingLendNo(1L);
					Skf3030TParkingRireki dtPR = null;
					dtPR = skf3030TParkingRirekiRepository.selectByPrimaryKey(parkRirekiKey);
					if(dtPR != null){
						if(dtTR.size() > 0){
							//退居（自動車の保管場所返還）届テーブル.退居する社宅区分2が、"1"または"3"、かつ一覧の駐車場開始日１が空白でない場合
							if((PARKING1.equals(dtTR.get(0).getShatakuTaikyKbn2()) || 
									PARKING12.equals(dtTR.get(0).getShatakuTaikyKbn2())) 
									&& !SkfCheckUtils.isNullOrEmpty(parking1StartDate)) {
								//駐車場管理番号１
								columnInfoList.setParkingKanriNo1(dtPR.getParkingKanriNo());
								//社宅駐車場区画情報マスタを取得
								Skf3010MShatakuParkingBlockKey blockKey = new Skf3010MShatakuParkingBlockKey();
								blockKey.setShatakuKanriNo(Long.parseLong(syatakuNo));
								blockKey.setParkingKanriNo(dtPR.getParkingKanriNo());
								Skf3010MShatakuParkingBlock dtSPB = null;
								dtSPB = skf3010MShatakuParkingBlockRepository.selectByPrimaryKey(blockKey);
								if(dtSPB != null){
									//駐車場区画１番号
									columnInfoList.setParkingBlock1(dtSPB.getParkingBlock());
									dtSPB = null;
								}
								blockKey = null;
							}
						}
					}
					
					//月別駐車場履歴情報を取得
					Skf3030TParkingRirekiKey parkRireki2Key = new Skf3030TParkingRirekiKey();
					parkRireki2Key.setShatakuKanriId(dtSL.get(0).getShatakuKanriId());
					parkRireki2Key.setYearMonth(yearMonth);
					parkRireki2Key.setParkingLendNo(2L);
					Skf3030TParkingRireki dtPR2 = null;
					dtPR2 = skf3030TParkingRirekiRepository.selectByPrimaryKey(parkRireki2Key);
					if(dtPR2 != null){
						if(dtTR.size() > 0){
							//退居（自動車の保管場所返還）届テーブル.退居する社宅区分2が、"2"または"3"、かつ一覧の駐車場開始日２が空白でない場合
							if((PARKING2.equals(dtTR.get(0).getShatakuTaikyKbn2()) || 
									PARKING12.equals(dtTR.get(0).getShatakuTaikyKbn2())) 
									&& !SkfCheckUtils.isNullOrEmpty(parking2StartDate)) {
								//駐車場管理番号２
								columnInfoList.setParkingKanriNo2(dtPR2.getParkingKanriNo());
								//社宅駐車場区画情報マスタを取得
								Skf3010MShatakuParkingBlockKey block2Key = new Skf3010MShatakuParkingBlockKey();
								block2Key.setShatakuKanriNo(Long.parseLong(syatakuNo));
								block2Key.setParkingKanriNo(dtPR2.getParkingKanriNo());
								Skf3010MShatakuParkingBlock dtSPB = null;
								dtSPB = skf3010MShatakuParkingBlockRepository.selectByPrimaryKey(block2Key);
								if(dtSPB != null){
									//駐車場区画２番号
									columnInfoList.setParkingBlock2(dtSPB.getParkingBlock());
									dtSPB = null;
								}
								block2Key = null;
							}
						}
					}
					
					if(!SkfCheckUtils.isNullOrEmpty(parking1StartDate)){
						//駐車場区画１開始日
						columnInfoList.setParking1StartDate(parking1StartDate);
					}
					if(!SkfCheckUtils.isNullOrEmpty(parking2StartDate)){
						//駐車場区画２開始日
						columnInfoList.setParking2StartDate(parking2StartDate);
					}
				}
				
				if(dtSL != null){
					//月別使用料履歴情報を取得
					Skf3030TShatakuRentalRirekiKey rirekiKey = new Skf3030TShatakuRentalRirekiKey();
					rirekiKey.setShatakuKanriId(dtSL.get(0).getShatakuKanriId());
					rirekiKey.setYearMonth(yearMonth);
					Skf3030TShatakuRentalRireki dtSRR = null;
					dtSRR = skf3030TShatakuRentalRirekiRepository.selectByPrimaryKey(rirekiKey);
					if(dtSRR != null){
						//使用料パターンＩＤ
						//rentalPatternId = dtSRR.getRentalPatternId().toString();
						columnInfoList.setRentalPatternId(dtSRR.getRentalPatternId());
						//役員算定
						columnInfoList.setYakuinSannteiKbn(dtSRR.getYakuinSannteiKbn());
						//社宅使用料日割金額
						columnInfoList.setRentalDay(dtSRR.getRentalDay());
						//社宅使用料調整金額
						columnInfoList.setRentalAdjust(dtSRR.getRentalAdjust());
						//個人負担共益費
						columnInfoList.setKyoekihiPerson(dtSRR.getKyoekihiPerson());
						//個人負担共益費調整金額
						columnInfoList.setKyoekihiPersonAdjust(dtSRR.getKyoekihiPersonAdjust());
						//共益金支払月
						columnInfoList.setKyoekihiPayMonth(dtSRR.getKyoekihiPayMonth());
						//社宅使用料月額
						columnInfoList.setRentalMonth(dtSRR.getRentalMonth());
						
						//退居また駐車場のみ申請
						if(CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(nyutaikyoKbn) ||
								CodeConstant.SHINSEI_KBN_PARKING.equals(applKbn)){
							//駐車場使用料調整金額
							columnInfoList.setParkingRentalAdjust(dtSRR.getParkingRentalAdjust());
							//駐車場日割金額１
							columnInfoList.setParking1RentalDay(dtSRR.getParking1RentalDay());
							//駐車場日割金額２
							columnInfoList.setParking2RentalDay(dtSRR.getParking2RentalDay());
							//駐車場月額１
							columnInfoList.setParking1RentalMonth(dtSRR.getParking1RentalMonth());
							//駐車場月額２
							columnInfoList.setParking2RentalMonth(dtSRR.getParking2RentalMonth());
						}
						dtSRR = null;
					}
					
					//社宅管理台帳備品基本情報を取得
					Skf3030TShatakuBihin dtSB = null;
					dtSB = skf3030TShatakuBihinRepository.selectByPrimaryKey(dtSL.get(0).getShatakuKanriId());
					if(dtSB != null){
						//備品貸与日
						columnInfoList.setEquipmentStartDate(dtSB.getTaiyoDate());
						//搬入希望日
						columnInfoList.setCarryinRequestDay(dtSB.getHannyuRequestDay());
						//搬入希望時間区分
						columnInfoList.setCarryinRequestKbn(dtSB.getHannyuRequestKbn());
						//受入本人連絡先
						columnInfoList.setUkeireMyApoint(dtSB.getUkeireMyApoint());
						//受入代理人氏名
						columnInfoList.setUkeireDairiName(dtSB.getUkeireDairiName());
						//受入代理人連絡先
						columnInfoList.setUkeireDairiApoint(dtSB.getUkeireDairiApoint());
						//備品備考
						columnInfoList.setBihinBiko(dtSB.getBihinBiko());
						
						dtSB = null;
					}
					
					//社宅管理台帳相互利用基本情報を取得
					Skf3030TShatakuMutual dtSM = null;
					dtSM = skf3030TShatakuMutualRepository.selectByPrimaryKey(dtSL.get(0).getShatakuKanriId());
					if(dtSM != null){
						//貸付会社コード
						columnInfoList.setKashitukeCompanyCd(dtSM.getKashitukeCompanyCd());
						//借受会社コード
						columnInfoList.setKariukeCompanyCd(dtSM.getKariukeCompanyCd());
						//社宅賃貸料
						columnInfoList.setRent(dtSM.getRent());
						//駐車場料金
						columnInfoList.setParkingRental(dtSM.getParkingRental());
						//共益費（事業者負担）
						columnInfoList.setKyoekihiBusiness(dtSM.getKyoekihiBusiness());
						//相互利用開始日
						columnInfoList.setMutualUseStartDay(dtSM.getMutualUseStartDay());
						//相互利用終了日
						columnInfoList.setMutualUseEndDay(dtSM.getMutualUseEndDay());
						//相互利用判定区分
						columnInfoList.setMutualUseKbn(dtSM.getMutualUseKbn());
						//社宅使用料会社間送金区分
						columnInfoList.setShatakuCompanyTransferKbn(dtSM.getShatakuCompanyTransferKbn());
						//共益費会社間送金区分
						columnInfoList.setKyoekihiCompanyTransferKbn(dtSM.getKyoekihiCompanyTransferKbn());
						//相互利用状況
						columnInfoList.setMutualJokyo(dtSM.getMutualJokyo());
						
						dtSM = null;
					}
					
					//月別相互利用履歴情報を取得
					Skf3030TShatakuMutualRirekiKey mtaRirekiKey = new Skf3030TShatakuMutualRirekiKey();
					mtaRirekiKey.setShatakuKanriId(dtSL.get(0).getShatakuKanriId());
					mtaRirekiKey.setYearMonth(yearMonth);
					Skf3030TShatakuMutualRireki dtSMR = null;
					dtSMR = skf3030TShatakuMutualRirekiRepository.selectByPrimaryKey(mtaRirekiKey);
					if(dtSMR != null){
						//配属会社コード
						columnInfoList.setAssignCompanyCd(dtSMR.getAssignCompanyCd());
						//所属機関
						columnInfoList.setAgency(dtSMR.getAssignAgencyName());
						//室・部名
						columnInfoList.setAffiliation1(dtSMR.getAssignAffiliation1());
						//課等名
						columnInfoList.setAffiliation2(dtSMR.getAssignAffiliation2());
						//配属データコード番号
						columnInfoList.setAssignCd(dtSMR.getAssignCd());
						
						dtSMR = null;
					}
					
					//居住者区分
					columnInfoList.setKyojushaKbn(dtSL.get(0).getKyojushaKbn());
					//社宅管理台帳ID
					columnInfoList.setShatakuKanriId(dtSL.get(0).getShatakuKanriId());
					//原籍会社コード
					columnInfoList.setOriginalCompanyCd(dtSL.get(0).getOriginalCompanyCd());
					//給与支給会社区分
					columnInfoList.setPayCompanyCd(dtSL.get(0).getPayCompanyCd());
				}

			}
		}
		//退居
		if(CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(nyutaikyoKbn)){
			if(dtSL != null){
				Skf3021Sc001GetBihinHenkyakuCountExpParameter bhCountPram = new Skf3021Sc001GetBihinHenkyakuCountExpParameter();
				bhCountPram.setShatakuKanriId(dtSL.get(0).getShatakuKanriId());
				bhCountPram.setYearMonth(yearMonth);
				bhCountPram.setKaishahoyou(BIHIN_LENT_STATUS_KAISHA_HOYU);
				bhCountPram.setLentaru(BIHIN_LENT_STATUS_RENTAL);
				returnCount = returnCount + skf3021Sc001GetBihinHenkyakuCountExpRepository.getBihinHenkyakuCount(bhCountPram);
			}
			if(returnCount > 0){
				//社宅管理台帳備品基本テーブル.貸与日の値あり："1"(必要)
				columnInfoList.setBihinTaiyoKbn(CodeConstant.BIHIN_TAIYO_KBN_HITSUYO);
				//備品提示ステータス　←　1：作成中
				columnInfoList.setBihinTeijiStatus(CodeConstant.BIHIN_STATUS_SAKUSEI_CHU);
				//作成完了区分（社宅の未作成状態）
				columnInfoList.setCreateCompleteKbn(CodeConstant.SHATAKU_SAKUSEI_SUMI);
			}else{
				//社宅管理台帳備品基本テーブル.貸与日の値なし："0"(不要)
				columnInfoList.setBihinTaiyoKbn(CodeConstant.BIHIN_TAIYO_KBN_FUYO);
				//備品提示ステータス　←　空白
				columnInfoList.setBihinTeijiStatus(CodeConstant.DOUBLE_QUOTATION);
				//作成完了区分（備品の作成完了状態）
				columnInfoList.setCreateCompleteKbn(CodeConstant.BIHIN_SAKUSEI_SUMI);
			}
		}else{
			columnInfoList.setCreateCompleteKbn(CodeConstant.MI_SAKUSEI);
		}
		
		//社宅提示ステータス
		columnInfoList.setShatakuTeijiStatus(CodeConstant.PRESENTATION_SITUATION_SAKUSEI_CHU);
		//台帳作成区分
		columnInfoList.setLandCreateKbn(CodeConstant.LAND_CREATE_KBN_MI_SAKUSEI);
		
		return columnInfoList;
		
	}
	
	/**
	 * 提示データ情報登録メソッド
	 * @param columnInfoList カラム情報
	 * @return 登録件数
	 */
	private int insertTeijiDataInfo(Skf3022TTeijiData columnInfoList){
		int result = 0;
		result = skf3022TTeijiDataRepository.insertSelective(columnInfoList);
		return result;
	}
	
	
	/**
	 * 入退居予定データの提示データ作成区分更新
	 * @param nyukyoDate
	 * @param ause
	 * @param teijiNo
	 * @param updateDate
	 * @param shainNo
	 * @param nyutaikyoKbn
	 * @return
	 */
	private int updateNyutaikyoYoteiInfoOfCreateKbn(String nyukyoDate,
			String ause, Long teijiNo, Date updateDate, String shainNo, String nyutaikyoKbn){
		
		int updCount = 0;
		Skf3021TNyutaikyoYoteiData updData = new Skf3021TNyutaikyoYoteiData();
		updData.setTeijiCreateKbn("1");
		updData.setNyukyoYoteiDate(nyukyoDate);
		updData.setAuse(ause);
		updData.setTeijiNo(teijiNo);
		updData.setLastUpdateDate(updateDate);

		//主キー
		updData.setShainNo(shainNo);
		updData.setNyutaikyoKbn(nyutaikyoKbn);
		
		updCount = skf3021TNyutaikyoYoteiDataRepository.updateByPrimaryKeySelective(updData);
		return updCount;
	}
}



