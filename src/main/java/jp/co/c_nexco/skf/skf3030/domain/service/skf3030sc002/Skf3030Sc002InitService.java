/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3030.domain.service.skf3030sc002;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc001.Skf3030Sc001GetShatakuKanriDaichoInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetBihinInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetBihinInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetBihinInfoForNoIdExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetBihinInfoForNoIdExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetParkingInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetParkingInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetParkingRirekiCountExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetParkingRirekiCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetRentalPatternInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetRentalPatternInfoExp2;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetRentalPatternInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetRentalPatternInfoExpParameter2;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetShatakuShainInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetSogoRiyoInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetSogoRiyoInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetTeijiStatusExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetTeijiStatusExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetTeijiStatusForNoIdExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetTeijiStatusForNoIdExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuBihin;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuLedger;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuRentalRireki;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuRentalRirekiKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc002.Skf3030Sc002GetBihinInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc002.Skf3030Sc002GetBihinInfoForNoIdExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc002.Skf3030Sc002GetParkingInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc002.Skf3030Sc002GetParkingRirekiCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc002.Skf3030Sc002GetRentalPatternInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc002.Skf3030Sc002GetShatakuShainInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc002.Skf3030Sc002GetSogoRiyoInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc002.Skf3030Sc002GetTeijiStatusExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc002.Skf3030Sc002GetTeijiStatusForNoIdExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3030TShatakuBihinRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3030TShatakuLedgerRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3030TShatakuRentalRirekiRepository;
import jp.co.c_nexco.nfw.common.bean.ApplicationScopeBean;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.CodeCacheUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.PropertyUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3030.domain.dto.skf3030Sc002common.Skf3030Sc002CommonDto;
import jp.co.c_nexco.skf.skf3030.domain.dto.skf3030sc002.Skf3030Sc002InitDto;
import jp.co.c_nexco.skf.skf3030.domain.service.common.Skf303010CommonSharedService;

/**
 * Skf3030Sc002InitService 入退居情報登録画面のInitサービス処理クラス。　 
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf3030Sc002InitService extends BaseServiceAbstract<Skf3030Sc002InitDto> {
	
	@Autowired
	private ApplicationScopeBean bean;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private CodeCacheUtils codeCacheUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private Skf3030TShatakuLedgerRepository skf3030TShatakuLedgerRepository;
	@Autowired
	private Skf3030TShatakuRentalRirekiRepository skf3030TShatakuRentalRirekiRepository;
	@Autowired
	private Skf3030TShatakuBihinRepository skf3030TShatakuBihinRepository;
	@Autowired
	private Skf3030Sc002SharedService skf3030Sc002SharedService;
	@Autowired
	private Skf3030Sc002GetTeijiStatusForNoIdExpRepository skf3030Sc002GetTeijiStatusForNoIdExpRepository;
	@Autowired
	private Skf3030Sc002GetBihinInfoForNoIdExpRepository skf3030Sc002GetBihinInfoForNoIdExpRepository;
	@Autowired
	private Skf3030Sc002GetShatakuShainInfoExpRepository skf3030Sc002GetShatakuShainInfoExpRepository;
	@Autowired
	private Skf3030Sc002GetRentalPatternInfoExpRepository skf3030Sc002GetRentalPatternInfoExpRepository;
	@Autowired
	private Skf3030Sc002GetTeijiStatusExpRepository skf3030Sc002GetTeijiStatusExpRepository;
	@Autowired
	private Skf3030Sc002GetParkingInfoExpRepository skf3030Sc002GetParkingInfoExpRepository;
	@Autowired
	private Skf3030Sc002GetBihinInfoExpRepository skf3030Sc002GetBihinInfoExpRepository;
	@Autowired
	private Skf3030Sc002GetSogoRiyoInfoExpRepository skf3030Sc002GetSogoRiyoInfoExpRepository;
	@Autowired
	private Skf3030Sc002GetParkingRirekiCountExpRepository skf3030Sc002GetParkingRirekiCountExpRepository;
	
	
	//社宅管理番号なし(0番)
	private static final String NO_SHATAKU_KANRI_ID = "0";
	//文字列："yyyyMM"
	private static final String NO_DATE = "____/__/__";
	private static final String TRUE = "true";
	
	/**
	 * サービス処理を行う。　
	 * 
	 * @param initDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public Skf3030Sc002InitDto index(Skf3030Sc002InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF3030_SC002_TITLE);
 		
		// デバッグログ
		LogUtils.debugByMsg("初期表示");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, FunctionIdConstant.SKF3030_SC002);
		
		//フラグ初期化
		initDto.setBihinItiranFlg(0);
		initDto.setBihinItiranReloadFlg(false);
		
		//備品一覧再取得フラグ
//      If MyBase.IsPostBack() Then
//        '備品一覧再取得フラグ
//        bihinItiranFlg = 0
//        Return
//      End If
		initDto.setBihinItiranFlg(1); 
		
		//エラー変数初期化
		skf3030Sc002SharedService.errReset(initDto);
		
		//画面初期化
		initialize(initDto);
		
		/** 運用ガイド保留(仕様確認中) */
		// 運用ガイドのパスを設定
		initDto.setOperationGuidePath("/skf/template/skf3022/skf3022mn006/"
				+ PropertyUtils.getValue("skf3022.skf3022_sc006.operationGuideFile"));
		
		//Page_LoadComplete
		skf3030Sc002SharedService.setBihinListPageLoadComplete(initDto);
		
		return initDto;
	}
	
	@SuppressWarnings("unchecked")
	private void initialize(Skf3030Sc002InitDto initDto) throws Exception{
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(Skf3030Sc002InitDto.DATE_FORMAT);

		//セッション情報をチェック
		Map<String, Object> searchSessionData = (Map<String, Object>) bean
				.get(SessionCacheKeyConstant.SHATAKUKANRI_DAICHO_SEARCH);
		
		if (searchSessionData != null) {
			// 検索条件データ
			String kanriKaisha = (String) searchSessionData.get(Skf303010CommonSharedService.SEARCH_INFO_KANRI_KAISHA_KEY);
			String agency = (String) searchSessionData.get(Skf303010CommonSharedService.SEARCH_INFO_AGENCY_KEY);
			String shainNo = (String) searchSessionData.get(Skf303010CommonSharedService.SEARCH_INFO_SHAIN_NO_KEY);
			String shainName = (String) searchSessionData.get(Skf303010CommonSharedService.SEARCH_INFO_SHAIN_NAME_KEY);
			String shatakName = (String) searchSessionData.get(Skf303010CommonSharedService.SEARCH_INFO_SHATAK_NAME_KEY);
			String shatakKbn = (String) searchSessionData.get(Skf303010CommonSharedService.SEARCH_INFO_SHATAK_KBN_KEY);
			String sogoriyo = (String) searchSessionData.get(Skf303010CommonSharedService.SEARCH_INFO_SOGORIYO_KEY);
			String nengetsu = (String) searchSessionData.get(Skf303010CommonSharedService.SEARCH_INFO_NENGETSU_KEY);
			String shimeShori = (String) searchSessionData.get(Skf303010CommonSharedService.SEARCH_INFO_SHIME_SHORI_KEY);
			String positiveRenkei = (String) searchSessionData.get(Skf303010CommonSharedService.SEARCH_INFO_POSITIVE_RENKEI_KEY);
			String kaishakanSokin = (String) searchSessionData.get(Skf303010CommonSharedService.SEARCH_INFO_KAISHAKAN_SOKIN_KEY);
			String gensekiKaisha = (String) searchSessionData.get(Skf303010CommonSharedService.SEARCH_INFO_GENSEKI_KAISHA_KEY);
			String kyuyoSikyuKaisha = (String) searchSessionData.get(Skf303010CommonSharedService.SEARCH_INFO_SIKYU_KAISHA_KEY);
			String kyojushaKbn = (String) searchSessionData.get(Skf303010CommonSharedService.SEARCH_INFO_KYOJUSHA_KBN_KEY);
			String akiHeya = (String) searchSessionData.get(Skf303010CommonSharedService.SEARCH_INFO_AKI_HEYA_KEY);
			String parkingSiyoryo = (String) searchSessionData.get(Skf303010CommonSharedService.SEARCH_INFO_PARKING_KEY);
			String honraiYoto = (String) searchSessionData.get(Skf303010CommonSharedService.SEARCH_INFO_HONRAI_YOTO_KEY);
			String honraiKikaku = (String) searchSessionData.get(Skf303010CommonSharedService.SEARCH_INFO_HONRAI_KIKAKU_KEY);
			String yakuin = (String) searchSessionData.get(Skf303010CommonSharedService.SEARCH_INFO_YAKUIN_KEY);
			String shukkosha = (String) searchSessionData.get(Skf303010CommonSharedService.SEARCH_INFO_SHUKKOSHA_KEY);
			String biko = (String) searchSessionData.get(Skf303010CommonSharedService.SEARCH_INFO_BIKO_KEY);
			
			//DTOに設定
			initDto.setSerachKanriKaisha(kanriKaisha);
			initDto.setSerachAgency(agency);
			initDto.setSerachShainNo(shainNo);
			initDto.setSerachShainName(shainName);
			initDto.setSerachShatakName(shatakName);
			initDto.setSerachShatakKbn(shatakKbn);
			initDto.setSerachSogoriyo(sogoriyo);
			initDto.setSerachNengetsu(nengetsu);
			initDto.setSerachShimeShori(shimeShori);
			initDto.setSerachPositiveRenkei(positiveRenkei);
			initDto.setSerachKaishakanSokin(kaishakanSokin);
			initDto.setSerachGensekiKaisha(gensekiKaisha);
			initDto.setSerachKyuyoSikyuKaisha(kyuyoSikyuKaisha);
			initDto.setSerachKyojushaKbn(kyojushaKbn);
			initDto.setSerachAkiHeya(akiHeya);
			initDto.setSerachParkingSiyoryo(parkingSiyoryo);
			initDto.setSerachHonraiYoto(honraiYoto);
			initDto.setSerachHonraiKikaku(honraiKikaku);
			initDto.setSerachYakuin(yakuin);
			initDto.setSerachShukkosha(shukkosha);
			initDto.setSerachBiko(biko);

		}
		//削除後動作エラー回避のためDTOクリア
		initDto.setHdnShatakuKanriId("");
		initDto.setHdnShatakuKanriNo("");
		initDto.setHdnShatakuRoomKanriNo("");
		initDto.setSc006ShainNo("");
		initDto.setSc006SiyoryoPatName("");
		initDto.setSc006ShainName("");
		initDto.setSc006ShatakuName("");
		initDto.setSc006HeyaNo("");
		initDto.setHdnNengetsu("");
		initDto.setSc006SiyoryoMonthPay("");
		initDto.setSc006KanriKaisya("");
		initDto.setHdnCompanyCode("");
		initDto.setHdnBillingActKbn("");
		
		Map<String, Object> selListSessionData = (Map<String, Object>) bean
				.get(SessionCacheKeyConstant.SHATAKUKANRI_DAICHO_INFO);
		
		if (selListSessionData != null) {
			// 選択した検索リストの社宅管理データ
			String selShatakuName = (String) selListSessionData.get(Skf303010CommonSharedService.SEL_LIST_INFO_SHATAK_NAME_KEY);
			String selRoomNo = (String) selListSessionData.get(Skf303010CommonSharedService.SEL_LIST_INFO_ROOM_NO_KEY);
			Long selRoomKanriNo = (Long) selListSessionData.get(Skf303010CommonSharedService.SEL_LIST_INFO_ROOM_KANRI_NO_KEY);
			Long selDaichoKanriId = (Long) selListSessionData.get(Skf303010CommonSharedService.SEL_LIST_INFO_DAICHO_KANRI_ID_KEY);
			String selKanriKaisyaName = (String) selListSessionData.get(Skf303010CommonSharedService.SEL_LIST_INFO_KANRI_KAISHA_NAME_KEY);
			String selKanriCd = (String) selListSessionData.get(Skf303010CommonSharedService.SEL_LIST_INFO_KANRI_CD_KEY);
			String selNengetsu = (String) selListSessionData.get(Skf303010CommonSharedService.SEL_LIST_INFO_NENGETSU_KEY);
			Long selShatakKanriNo = (Long) selListSessionData.get(Skf303010CommonSharedService.SEL_LIST_INFO_SHATAK_KANRI_NO_KEY);
			
			//画面項目
			//「社宅名」を画面項目に設定する
			initDto.setSc006ShatakuName(selShatakuName);
			//｢部屋番号」を画面項目に設定する
			initDto.setSc006HeyaNo(selRoomNo);
			//「年月」を「対象年月」に設定する
			initDto.setHdnNengetsu(selNengetsu);
			if(!CheckUtils.isEmpty(selNengetsu) && selNengetsu.length() == 6){
				String taishoNengetsu = selNengetsu.substring(0, 4) + "年" + selNengetsu.substring(4, 6) + "月";
				initDto.setSc006TaishoNengetsu(taishoNengetsu);
			}
			//「管理会社名」を「相互利用」タブの「管理会社」に設定する
			initDto.setSc006KanriKaisya(selKanriKaisyaName);
			//「社宅管理番号」をhidden項目に設定する
			initDto.setHdnShatakuKanriNo(selShatakKanriNo.toString());
			// ｢部屋管理番号」をhidden項目に設定する
			initDto.setHdnShatakuRoomKanriNo(selRoomKanriNo.toString());
			//「社宅管理台帳ID」をhidden項目に設定する
			if(selDaichoKanriId != null){
				initDto.setHdnShatakuKanriId(selDaichoKanriId.toString());
			}else{
				initDto.setHdnShatakuKanriId(NO_SHATAKU_KANRI_ID);
			}
			//「管理会社コード」をhidden項目に設定する
			initDto.setHdnCompanyCode(selKanriCd);
			//「締め処理実行区分」を取得
			String billingActKbn = skf3030Sc002SharedService.getBillingActKbn(selNengetsu);
			initDto.setHdnBillingActKbn(billingActKbn);
		}
		
		bean.clear();
		
		// ドロップダウンリスト
		List<Map<String, Object>> sc006SogoRyojokyoSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006TaiyoKaisyaSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006KariukeKaisyaSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006SogoHanteiKbnSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006HaizokuKaisyaSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006OldKaisyaNameSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006KyuyoKaisyaSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006KyojyusyaKbnSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006YakuinSanteiSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006KyoekihiPayMonthSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006KibouTimeInSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006KibouTimeOutSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006SokinShatakuSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006SokinKyoekihiSelectList = new ArrayList<Map<String, Object>>();
		
		
		// 備品一覧初期値(空っぽ)
		initDto.setBihinInfoListTableData(new ArrayList<Map<String, Object>>());
		//項目初期化
		//社宅使用料月額
		initDto.setSc006ShiyoryoTsukigaku("0");
		//社宅使用料日割金額
		initDto.setSc006SiyoryoHiwariPay("0");
		//社宅使用料調整金額
		initDto.setSc006SiyoroTyoseiPay("0");
		//社宅使用料月額（調整後）
		initDto.setSc006SyatauMonthPayAfter("0");
		//個人負担共益費月額
		initDto.setSc006KyoekihiMonthPay("0");
		//個人負担共益費調整金額
		initDto.setSc006KyoekihiTyoseiPay("0");
		//個人負担共益費月額（調整後）
		initDto.setSc006KyoekihiPayAfter("0");
		//入居予定日
		initDto.setSc006NyukyoYoteiDay("");
		//退居予定日
		initDto.setSc006TaikyoYoteiDay("");
		//備考
		initDto.setSc006Bicou("");
		initDto.setSc006TaiyoYouto("");
		initDto.setSc006TaiyoKikaku("");

		//駐車場使用料月額（区画1）
		initDto.setSc006TyusyaMonthPayOne("0");
		//駐車場使用料日割金額（区画1）
		initDto.setSc006TyusyaDayPayOne("0");
		//利用開始日（区画1）
		initDto.setSc006RiyouStartDayOne("");
		//利用終了日（区画1）
		initDto.setSc006RiyouEndDayOne("");
		initDto.setSc006KukakuNoOne("");
		initDto.setHdnChushajoKanriNo1("");

		//駐車場使用料月額（区画2）
		initDto.setSc006TyusyaMonthPayTwo("0");
		//駐車場使用料日割金額（区画2）
		initDto.setSc006TyusyaDayPayTwo("0");
		//利用開始日（区画2）
		initDto.setSc006RiyouStartDayTwo("");
		//利用終了日（区画2）
		initDto.setSc006RiyouEndDayTwo("");
		initDto.setSc006KukakuNoTwo("");
		initDto.setHdnChushajoKanriNo2("");
		
		//駐車場使用料調整金額
		initDto.setSc006TyusyaTyoseiPay("0");
		//駐車場使用料月額（調整後）
		initDto.setSc006TyusyaMonthPayAfter("0");
		
		//備品情報
		initDto.setSc006TaiyoDay("");
		initDto.setSc006HenkyakuDay("");
		initDto.setSc006KibouDayIn("");
		initDto.setSc006HonninAddrIn("");
		initDto.setSc006UketoriDairiInName("");
		initDto.setSc006UketoriDairiAddr("");
		initDto.setSc006HannyuShijisyoHakkoubi("");
		initDto.setSc006KibouDayOut("");
		initDto.setSc006HonninAddrOut("");
		initDto.setSc006TachiaiDairi("");
		initDto.setSc006TachiaiDairiAddr("");
		initDto.setSc006HanshutuShijisyoHakkoubi("");
		initDto.setSc006DairiBiko("");
		initDto.setSc006BihinBiko("");
		
		//相互利用情報
		//社宅賃貸料
		initDto.setSc006ChintaiRyo("0");
		//駐車場料金
		initDto.setSc006TyusyajoRyokin("0");
		//共益費(事業者負担)
		initDto.setSc006Kyoekihi("0");
		initDto.setSc006SyozokuKikan("");
		initDto.setSc006SituBuName("");
		initDto.setSc006KanadoMei("");
		initDto.setSc006HaizokuNo("");
		initDto.setSc006StartDay("");
		initDto.setSc006EndDay("");
		//タブ位置
		initDto.setHdnTabIndex("0");
		
		//「社宅管理台帳ID」が”0”の場合
		if(NO_SHATAKU_KANRI_ID.equals(initDto.getHdnShatakuKanriId())){
			//社宅提示ステータス」、「備品提示ステータス」を取得し設定する
			Skf3030Sc002GetTeijiStatusForNoIdExpParameter teijiStatusParam = new Skf3030Sc002GetTeijiStatusForNoIdExpParameter();
			teijiStatusParam.setShatakuKanriNo(Long.parseLong(initDto.getHdnShatakuKanriNo()));
			teijiStatusParam.setShatakuRoomKanriNo(Long.parseLong(initDto.getHdnShatakuRoomKanriNo()));
			List<Skf3030Sc002GetTeijiStatusForNoIdExp> teijiStatusDt = skf3030Sc002GetTeijiStatusForNoIdExpRepository.getTeijiStatusForNoId(teijiStatusParam);
			
			initDto.setSc006ShatakuSttsCd(CodeConstant.DOUBLE_QUOTATION);
			initDto.setSc006BihinSttsCd(CodeConstant.DOUBLE_QUOTATION);
			if(teijiStatusDt != null && teijiStatusDt.size() > 0){
				
				//社宅提示状況区分
				Map<String, String> genericCodeShatakuTeijiStatus = new HashMap<String, String>();
				genericCodeShatakuTeijiStatus = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_SHATAKU_TEIJI_STATUS_KBN);
				//備品提示状況区分
				Map<String, String> genericCodeBihinTeijiStatus = new HashMap<String, String>();
				genericCodeBihinTeijiStatus = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_BIHINTEIJISTATUS_KBN);
				
				//社宅提示ステータス
				String shatakuTeijiStatus = teijiStatusDt.get(0).getShatakuTeijiStatus();
				if(!SkfCheckUtils.isNullOrEmpty(shatakuTeijiStatus)){
					initDto.setSc006ShatakuStts(genericCodeShatakuTeijiStatus.get(shatakuTeijiStatus));
					initDto.setSc006ShatakuSttsCd(shatakuTeijiStatus);
				}else{
					initDto.setSc006ShatakuStts(CodeConstant.ZEN_HYPHEN);
				}
				//備品提示ステータス
				String bihinTeijiStatus = teijiStatusDt.get(0).getBihinTeijiStatus();
				if(!SkfCheckUtils.isNullOrEmpty(bihinTeijiStatus)){
					initDto.setSc006BihinStts(genericCodeBihinTeijiStatus.get(bihinTeijiStatus));
					initDto.setSc006BihinSttsCd(bihinTeijiStatus);
				}else{
					initDto.setSc006BihinStts(CodeConstant.ZEN_HYPHEN);
				}
			}else{
				initDto.setSc006ShatakuStts(CodeConstant.ZEN_HYPHEN);
				initDto.setSc006BihinStts(CodeConstant.ZEN_HYPHEN);
			}
			
			
			//【使用料計算機能対応】使用料計算情報の取得
			skf3030Sc002SharedService.getShiyoryoKeisanSessionInfo(initDto);

//			//「居住者区分」の初期値を設定する
//			Me.ddlKyojushaKubun.SelectedValue = Constant.KyojushaKbn.HONNIN_DOKYO
//			//「役員算定」の初期値を設定する
//			Me.ddlYakuinSantei.SelectedValue() = Constant.YakuinKbn.NASHI
//			//「共益費支払月」の初期値を設定する
//			Me.ddlKyoekihiShiharaiduki.SelectedValue = Constant.KyoekihiPayMonthKbn.NEXT_MONTH
			//ドロップダウンリストを設定
			skf3030Sc002SharedService.setDdlControlValues(
					CodeConstant.KYOJUSHAKBN_HONNIN_DOKYO, sc006KyojyusyaKbnSelectList,
					CodeConstant.YAKUIN_KBN_NASHI, sc006YakuinSanteiSelectList,
					CodeConstant.NEXT_MONTH, sc006KyoekihiPayMonthSelectList,
					CodeConstant.DOUBLE_QUOTATION, sc006KibouTimeInSelectList,
					CodeConstant.DOUBLE_QUOTATION, sc006KibouTimeOutSelectList,
					CodeConstant.DOUBLE_QUOTATION, sc006SogoRyojokyoSelectList,
					CodeConstant.DOUBLE_QUOTATION, sc006SogoHanteiKbnSelectList,
					CodeConstant.DOUBLE_QUOTATION, sc006SokinShatakuSelectList,
					CodeConstant.DOUBLE_QUOTATION, sc006SokinKyoekihiSelectList,
					CodeConstant.DOUBLE_QUOTATION, sc006OldKaisyaNameSelectList,
					CodeConstant.DOUBLE_QUOTATION, sc006KyuyoKaisyaSelectList,
					CodeConstant.DOUBLE_QUOTATION, sc006HaizokuKaisyaSelectList,
					CodeConstant.DOUBLE_QUOTATION, sc006TaiyoKaisyaSelectList,
					CodeConstant.DOUBLE_QUOTATION, sc006KariukeKaisyaSelectList);

			//「備品コード」、「備品名称」、「部屋備付状態」取得
//			Skf3030Sc002GetBihinInfoForNoIdExpParameter bihinParam = new Skf3030Sc002GetBihinInfoForNoIdExpParameter();
//			bihinParam.setShatakuKanriNo(Long.parseLong(initDto.getHdnShatakuKanriNo()));
//			bihinParam.setShatakuRoomKanriNo(Long.parseLong(initDto.getHdnShatakuRoomKanriNo()));
//			List<Skf3030Sc002GetBihinInfoForNoIdExp> grvBihinList = skf3030Sc002GetBihinInfoForNoIdExpRepository.getBihinInfoForNoId(bihinParam);
//			initDto.setBihinInfoListTableData(skf3030Sc002SharedService.getBihinForNoIdListTableDataViewColumn(grvBihinList, initDto.getGBihinInfoControlStatusFlg()));
			
			//SetBihinListGridViewColumn()
			
		}else{
			//「社宅管理台帳ID」が値を持つ場合
			Long shatakuKanriId = Long.parseLong(initDto.getHdnShatakuKanriId());
			Long shatakuKanriNo = Long.parseLong(initDto.getHdnShatakuKanriNo());
			Long shatakuRoomKanriNo = Long.parseLong(initDto.getHdnShatakuRoomKanriNo());
			String nengetu = initDto.getHdnNengetsu();
			//社員情報取得
			List<Skf3030Sc002GetShatakuShainInfoExp> shatakuShainNoInfo = new ArrayList<Skf3030Sc002GetShatakuShainInfoExp>();
			shatakuShainNoInfo = skf3030Sc002GetShatakuShainInfoExpRepository.getShatakuShainInfo(shatakuKanriId);
			
			if(shatakuShainNoInfo.size() > 0){
				//社員氏名
				initDto.setSc006ShainName(shatakuShainNoInfo.get(0).getName());
				//生年月日
				initDto.setHdnBirthday(shatakuShainNoInfo.get(0).getBirthday());
				//社員番号変更フラグ（hidden）
				initDto.setHdnShainNoChangeFlg(shatakuShainNoInfo.get(0).getShainNoChangeFlg());
				
				if("1".equals(initDto.getHdnShainNoChangeFlg())){
					initDto.setSc006ShainNo(shatakuShainNoInfo.get(0).getShainNo() + CodeConstant.ASTERISK);
				}else{
					initDto.setSc006ShainNo(shatakuShainNoInfo.get(0).getShainNo());
				}
			}
			//使用料パターン情報取得
			Skf3030Sc002GetRentalPatternInfoExpParameter2 rentalParam = new Skf3030Sc002GetRentalPatternInfoExpParameter2();
			rentalParam.setShatakuKanriId(shatakuKanriId);
			rentalParam.setShatakuKanriNo(shatakuKanriNo);
			rentalParam.setYearMonth(nengetu);
			List<Skf3030Sc002GetRentalPatternInfoExp2> rentalPatternInfo = new ArrayList<Skf3030Sc002GetRentalPatternInfoExp2>();
			rentalPatternInfo = skf3030Sc002GetRentalPatternInfoExpRepository.getRentalPatternInfo2(rentalParam);
			
			if(rentalPatternInfo.size() > 0){
				//使用料パターンID（hidden）				
				initDto.setHdnRentalPatternId(rentalPatternInfo.get(0).getRentalPatternId().toString());
				//【使用料計算機能対応】使用料パターンID				
				initDto.setHdnShiyoryoKeisanPatternId(rentalPatternInfo.get(0).getRentalPatternId().toString());
				//【使用料計算機能対応】使用料計算情報の取得
				skf3030Sc002SharedService.getShiyoryoKeisanSessionInfo(initDto);
				//【使用料計算機能対応】使用料パターン排他処理用更新日付
				if(!SkfCheckUtils.isNullOrEmpty(initDto.getHdnShiyoryoKeisanPatternId())){
					List<Skf3030Sc002GetRentalPatternInfoExp> rentalPatternInfoList = new ArrayList<Skf3030Sc002GetRentalPatternInfoExp>();
					Skf3030Sc002GetRentalPatternInfoExpParameter param = new Skf3030Sc002GetRentalPatternInfoExpParameter();
					param.setRentalPatternId(Long.parseLong(initDto.getHdnShiyoryoKeisanPatternId()));
					// 使用料パターン情報取得
					rentalPatternInfoList = skf3030Sc002GetRentalPatternInfoExpRepository.getRentalPatternInfo(param);
					if(rentalPatternInfoList.get(0).getUpdateDate() != null){
						initDto.setHdnRentalPatternUpdateDate(dateFormat.format(rentalPatternInfoList.get(0).getUpdateDate()));
					}
				}
				//使用料計算パターン名
				initDto.setSc006SiyoryoPatName(rentalPatternInfo.get(0).getRentalPatternName());
				//【使用料計算機能対応】社宅使用料月額
				initDto.setSc006SiyoryoMonthPay(skf3030Sc002SharedService.convertYenFormat(rentalPatternInfo.get(0).getRental(), true));
				//貸与用途
				if(!SkfCheckUtils.isNullOrEmpty(rentalPatternInfo.get(0).getAuse())){
					initDto.setSc006TaiyoYouto(codeCacheUtils.getGenericCodeName(
							FunctionIdConstant.GENERIC_CODE_AUSE_KBN, rentalPatternInfo.get(0).getAuse()));
				}
				//貸与規格
				if(!SkfCheckUtils.isNullOrEmpty(rentalPatternInfo.get(0).getKikaku())){
					initDto.setSc006TaiyoKikaku(codeCacheUtils.getGenericCodeName(
							FunctionIdConstant.GENERIC_CODE_KIKAKU_KBN, rentalPatternInfo.get(0).getKikaku()));
				}
				//寒冷地減免
				if(!SkfCheckUtils.isNullOrEmpty(rentalPatternInfo.get(0).getKanreitiFlg())){
					initDto.setLitKanreichiGenmen(codeCacheUtils.getGenericCodeName(
							FunctionIdConstant.GENERIC_CODE_KANREITIADJUST_KBN, rentalPatternInfo.get(0).getKanreitiFlg()));
				}
				//狭小減免
				if(!SkfCheckUtils.isNullOrEmpty(rentalPatternInfo.get(0).getKyosyoFlg())){
					initDto.setLitKyoshoGenmen(codeCacheUtils.getGenericCodeName(
							FunctionIdConstant.GENERIC_CODE_KYOSHOADJUST_KBN, rentalPatternInfo.get(0).getKyosyoFlg()));
				}
			}
			
			//「社宅提示ステータス」「備品提示ステータス」取得
			Skf3030Sc002GetTeijiStatusExpParameter teijiParam = new Skf3030Sc002GetTeijiStatusExpParameter();
			teijiParam.setShatakuKanriNo(shatakuKanriNo);
			teijiParam.setShatakuRoomKanriNo(shatakuRoomKanriNo);
			teijiParam.setShainNo(initDto.getSc006ShainNo());
			List<Skf3030Sc002GetTeijiStatusExp> teijiStatusDt = new ArrayList<Skf3030Sc002GetTeijiStatusExp>();
			teijiStatusDt = skf3030Sc002GetTeijiStatusExpRepository.getTeijiStatus(teijiParam);
			initDto.setSc006ShatakuStts(CodeConstant.ZEN_HYPHEN);
			initDto.setSc006BihinStts(CodeConstant.ZEN_HYPHEN);
			initDto.setSc006ShatakuSttsCd(CodeConstant.DOUBLE_QUOTATION);
			initDto.setSc006BihinSttsCd(CodeConstant.DOUBLE_QUOTATION);
			if(teijiStatusDt.size() > 0){
				//社宅提示ステータス
				if(!SkfCheckUtils.isNullOrEmpty(teijiStatusDt.get(0).getShatakuTeijiStatus())){
					initDto.setSc006ShatakuSttsCd(teijiStatusDt.get(0).getShatakuTeijiStatus());
					initDto.setSc006ShatakuStts(codeCacheUtils.getGenericCodeName(
							FunctionIdConstant.GENERIC_CODE_SHATAKU_TEIJI_STATUS_KBN, teijiStatusDt.get(0).getShatakuTeijiStatus()));
					initDto.setSc006ShatakuSttsColor(skf3030Sc002SharedService.setShatakuTeijiStatusCss(teijiStatusDt.get(0).getShatakuTeijiStatus()));
				}
				//備品提示ステータス
				if(!SkfCheckUtils.isNullOrEmpty(teijiStatusDt.get(0).getBihinTeijiStatus())){
					initDto.setSc006BihinSttsCd(teijiStatusDt.get(0).getBihinTeijiStatus());
					initDto.setSc006BihinStts(codeCacheUtils.getGenericCodeName(
							FunctionIdConstant.GENERIC_CODE_BIHINTEIJISTATUS_KBN, teijiStatusDt.get(0).getBihinTeijiStatus()));
					initDto.setSc006BihinSttsColor(skf3030Sc002SharedService.setBihinTeijiStatusCss(teijiStatusDt.get(0).getBihinTeijiStatus()));
				}
				//備品貸与区分（hidden）
				initDto.setHdnBihinTaiyoKbn(teijiStatusDt.get(0).getBihinTaiyoKbn());
				
				if(!SkfCheckUtils.isNullOrEmpty(teijiStatusDt.get(0).getNyutaikyoKbn())){
					initDto.setHdnNYUTAIKYO_KBN(teijiStatusDt.get(0).getNyutaikyoKbn());
				}
			}else{
				initDto.setSc006ShatakuStts(CodeConstant.ZEN_HYPHEN);
				initDto.setSc006BihinStts(CodeConstant.ZEN_HYPHEN);
			}
			
			//社宅管理台帳基本テーブルの情報取得
			Skf3030TShatakuLedger shatakuKanriDaichoDt = skf3030TShatakuLedgerRepository.selectByPrimaryKey(shatakuKanriId);
			if(shatakuKanriDaichoDt != null){
				//原籍会社コード
				initDto.setSc006OldKaisyaNameSelect(shatakuKanriDaichoDt.getOriginalCompanyCd());
				//給与支給会社コード
				initDto.setSc006KyuyoKaisyaSelect(shatakuKanriDaichoDt.getPayCompanyCd());
				//入居予定日
				initDto.setSc006NyukyoYoteiDay(shatakuKanriDaichoDt.getNyukyoDate());
				//退居予定日
				initDto.setSc006TaikyoYoteiDay(shatakuKanriDaichoDt.getTaikyoDate());
				//居住者区分
				initDto.setSc006KyojyusyaKbnSelect(shatakuKanriDaichoDt.getKyojushaKbn());
				//備考
				initDto.setSc006Bicou(shatakuKanriDaichoDt.getBiko());
			}
			
			//月別使用料履歴の情報取得
			Skf3030TShatakuRentalRirekiKey rentalKey = new Skf3030TShatakuRentalRirekiKey();
			rentalKey.setShatakuKanriId(shatakuKanriId);
			rentalKey.setYearMonth(nengetu);
			Skf3030TShatakuRentalRireki tsukibetsuShiyoryoDt = skf3030TShatakuRentalRirekiRepository.selectByPrimaryKey(rentalKey);
			if(tsukibetsuShiyoryoDt != null){
				//役員算定
				initDto.setSc006YakuinSanteiSelect(tsukibetsuShiyoryoDt.getYakuinSannteiKbn());
				//社宅使用料月額
				initDto.setSc006ShiyoryoTsukigaku(skf3030Sc002SharedService.convertYenFormat(tsukibetsuShiyoryoDt.getRentalMonth(),false));
				//社宅使用料日割金額
				initDto.setSc006SiyoryoHiwariPay(skf3030Sc002SharedService.convertYenFormat(tsukibetsuShiyoryoDt.getRentalDay(),false));
				//社宅使用料調整金額
				initDto.setSc006SiyoroTyoseiPay(skf3030Sc002SharedService.convertYenFormat(tsukibetsuShiyoryoDt.getRentalAdjust(),false).replace(",", ""));
				//社宅使用料月額（調整後）
				initDto.setSc006SyatauMonthPayAfter(skf3030Sc002SharedService.convertYenFormat(tsukibetsuShiyoryoDt.getRentalTotal(),false));
				//個人負担共益費月額
				initDto.setSc006KyoekihiMonthPay(skf3030Sc002SharedService.convertYenFormat(tsukibetsuShiyoryoDt.getKyoekihiPerson(),false).replace(",", ""));
				//個人負担共益費調整金額
				initDto.setSc006KyoekihiTyoseiPay(skf3030Sc002SharedService.convertYenFormat(tsukibetsuShiyoryoDt.getKyoekihiPersonAdjust(),false).replace(",", ""));
				//個人負担共益費月額（調整後）
				initDto.setSc006KyoekihiPayAfter(skf3030Sc002SharedService.convertYenFormat(tsukibetsuShiyoryoDt.getKyoekihiPersonTotal(),false));
				//共益費支払月
				initDto.setSc006KyoekihiPayMonthSelect(tsukibetsuShiyoryoDt.getKyoekihiPayMonth());
				//駐車場使用料月額（区画1）
				initDto.setSc006TyusyaMonthPayOne(skf3030Sc002SharedService.convertYenFormat(tsukibetsuShiyoryoDt.getParking1RentalMonth(),false));
				//駐車場使用料日割金額（区画1）
				initDto.setSc006TyusyaDayPayOne(skf3030Sc002SharedService.convertYenFormat(tsukibetsuShiyoryoDt.getParking1RentalDay(),false));
				//駐車場使用料月額（区画2）
				initDto.setSc006TyusyaMonthPayTwo(skf3030Sc002SharedService.convertYenFormat(tsukibetsuShiyoryoDt.getParking2RentalMonth(),false));
				//駐車場使用料日割金額（区画2）
				initDto.setSc006TyusyaDayPayTwo(skf3030Sc002SharedService.convertYenFormat(tsukibetsuShiyoryoDt.getParking2RentalDay(),false));
				//駐車場使用料調整金額
				initDto.setSc006TyusyaTyoseiPay(skf3030Sc002SharedService.convertYenFormat(tsukibetsuShiyoryoDt.getParkingRentalAdjust(),false).replace(",", ""));

				if(tsukibetsuShiyoryoDt.getParkingRentalTotal() != null && !SkfCheckUtils.isNullOrEmpty(tsukibetsuShiyoryoDt.getParkingRentalTotal().toString())){
					//駐車場使用料月額（調整後）
					initDto.setSc006TyusyaMonthPayAfter(skf3030Sc002SharedService.convertYenFormat(tsukibetsuShiyoryoDt.getParkingRentalTotal(),false));
				}else{
					//DBに値が入っていない場合は再計算
					skf3030Sc002SharedService.siyoryoKeiSanSync(initDto);
					throwBusinessExceptionIfErrors(initDto.getResultMessages());
				}
			}
			
			//駐車場の情報取得
			Skf3030Sc002GetParkingInfoExpParameter parkingParam = new Skf3030Sc002GetParkingInfoExpParameter();
			parkingParam.setShatakuKanriId(shatakuKanriId);
			parkingParam.setShatakuKanriNo(shatakuKanriNo);
			parkingParam.setYearMonth(nengetu);
			List<Skf3030Sc002GetParkingInfoExp> paringDt = skf3030Sc002GetParkingInfoExpRepository.getParkingInfo(parkingParam);
			
			if(paringDt != null){
				if(paringDt.size() > 1){
					//区画１駐車場管理番号（hidden）
					initDto.setHdnChushajoKanriNo1(paringDt.get(0).getParkingKanriNo().toString());
					//区画１利用開始日
					initDto.setSc006RiyouStartDayOne(paringDt.get(0).getParkingStartDate());
					//区画１利用終了日
					initDto.setSc006RiyouEndDayOne(paringDt.get(0).getParkingEndDate());
					//区画１番号
					initDto.setSc006KukakuNoOne(paringDt.get(0).getParkingBlock());
					
					//区画２駐車場管理番号（hidden）
					initDto.setHdnChushajoKanriNo2(paringDt.get(1).getParkingKanriNo().toString());
					//区画２利用開始日
					initDto.setSc006RiyouStartDayTwo(paringDt.get(1).getParkingStartDate());
					//区画２利用終了日
					initDto.setSc006RiyouEndDayTwo(paringDt.get(1).getParkingEndDate());
					//区画２番号
					initDto.setSc006KukakuNoTwo(paringDt.get(1).getParkingBlock());
				}else if(paringDt.size() == 1){
					//月別駐車場区画１がある場合
					if(CodeConstant.PARKING_LEND_NO_FIRST.equals(paringDt.get(0).getParkingLendNo().toString())){
						//区画１駐車場管理番号（hidden）
						initDto.setHdnChushajoKanriNo1(paringDt.get(0).getParkingKanriNo().toString());
						//区画１利用開始日
						initDto.setSc006RiyouStartDayOne(paringDt.get(0).getParkingStartDate());
						//区画１利用終了日
						initDto.setSc006RiyouEndDayOne(paringDt.get(0).getParkingEndDate());
						//区画１番号
						initDto.setSc006KukakuNoOne(paringDt.get(0).getParkingBlock());
					}
					//月別駐車場区画２がある場合
					else{
						//区画２駐車場管理番号（hidden）
						initDto.setHdnChushajoKanriNo2(paringDt.get(0).getParkingKanriNo().toString());
						//区画２利用開始日
						initDto.setSc006RiyouStartDayTwo(paringDt.get(0).getParkingStartDate());
						//区画２利用終了日
						initDto.setSc006RiyouEndDayTwo(paringDt.get(0).getParkingEndDate());
						//区画２番号
						initDto.setSc006KukakuNoTwo(paringDt.get(0).getParkingBlock());
					}	
				}
			}
			
			//備品情報取得
//			Skf3030Sc002GetBihinInfoExpParameter bihinParam = new Skf3030Sc002GetBihinInfoExpParameter();
//			bihinParam.setShatakuKanriId(shatakuKanriId);
//			bihinParam.setShatakuKanriNo(shatakuKanriNo);
//			bihinParam.setShatakuRoomKanriNo(shatakuRoomKanriNo);
//			bihinParam.setYearMonth(nengetu);
//			List<Skf3030Sc002GetBihinInfoExp> bihinDt = skf3030Sc002GetBihinInfoExpRepository.getBihinInfo(bihinParam);
//			initDto.setBihinInfoListTableData(skf3030Sc002SharedService.getBihinListTableDataViewColumn(bihinDt, initDto.getGBihinInfoControlStatusFlg()));
			
			//
			Skf3030TShatakuBihin bihinDtInfo = skf3030TShatakuBihinRepository.selectByPrimaryKey(shatakuKanriId);
			
			if(bihinDtInfo != null){
				//備品貸与日
				initDto.setSc006TaiyoDay(bihinDtInfo.getTaiyoDate());
				//備品返却日
				initDto.setSc006HenkyakuDay(bihinDtInfo.getHenkyakuDate());
				//搬入希望日
				initDto.setSc006KibouDayIn(bihinDtInfo.getHannyuRequestDay());
				//搬入希望時間帯
				initDto.setSc006KibouTimeInSelect(bihinDtInfo.getHannyuRequestKbn());
				//搬入本人連絡先
				initDto.setSc006HonninAddrIn(bihinDtInfo.getUkeireMyApoint());
				//受取代理人
				initDto.setSc006UketoriDairiInName(bihinDtInfo.getUkeireDairiName());
				//受取代理人連絡先
				initDto.setSc006UketoriDairiAddr(bihinDtInfo.getUkeireDairiApoint());
				//搬入レンタル指示書発行日
				String hannyuCarryinInstruction = bihinDtInfo.getHannyuCarryinInstruction();
				if(SkfCheckUtils.isNullOrEmpty(hannyuCarryinInstruction)){
					//Literal のため、文字列 "____/__/__" を挿入
					initDto.setSc006HannyuShijisyoHakkoubi(NO_DATE);
				}else{
					//日付文字列を文字列 "YYYY/MM/DD" に変換
					initDto.setSc006HannyuShijisyoHakkoubi(skfDateFormatUtils.dateFormatFromString(hannyuCarryinInstruction, "yyyy/MM/dd"));
				}
				//搬出希望日
				initDto.setSc006KibouDayOut(bihinDtInfo.getHansyutuRequestDay());
				//搬出希望時間帯
				initDto.setSc006KibouTimeOutSelect(bihinDtInfo.getHansyutuRequestKbn());
				//搬出本人連絡先
				initDto.setSc006HonninAddrOut(bihinDtInfo.getTatiaiMyApoint());
				//立会代理人
				initDto.setSc006TachiaiDairi(bihinDtInfo.getTatiaiDairiName());
				//立会代理人連絡先
				initDto.setSc006TachiaiDairiAddr(bihinDtInfo.getTatiaiDairiApoint());
				//搬出レンタル指示書発行日
				String hansyutuCarryinInstruction = bihinDtInfo.getHansyutuCarryinInstruction();
				if(SkfCheckUtils.isNullOrEmpty(hansyutuCarryinInstruction)){
					//Literal のため、文字列 "____/__/__" を挿入
					initDto.setSc006HanshutuShijisyoHakkoubi(NO_DATE);
				}else{
					//日付文字列を文字列 "YYYY/MM/DD" に変換
					initDto.setSc006HanshutuShijisyoHakkoubi(skfDateFormatUtils.dateFormatFromString(hansyutuCarryinInstruction, "yyyy/MM/dd"));
				}
				//代理人備考
				initDto.setSc006DairiBiko(bihinDtInfo.getDairiBiko());
				//備品情報備考
				initDto.setSc006BihinBiko(bihinDtInfo.getBihinBiko());
			}
			
			//相互利用情報取得
			Skf3030Sc002GetSogoRiyoInfoExpParameter sogoParam = new Skf3030Sc002GetSogoRiyoInfoExpParameter();
			sogoParam.setShatakuKanriId(shatakuKanriId);
			sogoParam.setYearMonth(nengetu);
			List<Skf3030Sc002GetSogoRiyoInfoExp> sogoRiyoDt = new ArrayList<Skf3030Sc002GetSogoRiyoInfoExp>();
			sogoRiyoDt = skf3030Sc002GetSogoRiyoInfoExpRepository.getSogoRiyoInfo(sogoParam);
			if(sogoRiyoDt.size() > 0){
				//相互利用状況
				initDto.setSc006SogoRyojokyoSelect(sogoRiyoDt.get(0).getMutualJokyo());
				//貸付会社
				initDto.setSc006TaiyoKaisyaSelect(sogoRiyoDt.get(0).getKashitukeCompanyCd());
				//借受会社
				initDto.setSc006KariukeKaisyaSelect(sogoRiyoDt.get(0).getKariukeCompanyCd());
				//相互利用判定区分
				initDto.setSc006SogoHanteiKbnSelect(sogoRiyoDt.get(0).getMutualUseKbn());
				//社宅使用料会社間送金区分
				initDto.setSc006SokinShatakuSelect(sogoRiyoDt.get(0).getShatakuCompanyTransferKbn());
				//共益費会社間送金区分
				initDto.setSc006SokinKyoekihiSelect(sogoRiyoDt.get(0).getKyoekihiCompanyTransferKbn());
				//社宅賃貸料
				if(sogoRiyoDt.get(0).getRent() != null){
					initDto.setSc006ChintaiRyo(sogoRiyoDt.get(0).getRent().toString());
				}
				//駐車場料金
				if(sogoRiyoDt.get(0).getParkingRental() != null){
					initDto.setSc006TyusyajoRyokin(sogoRiyoDt.get(0).getParkingRental().toString());
				}
				//共益費(事業者負担)
				if(sogoRiyoDt.get(0).getKyoekihiBusiness() != null){
					initDto.setSc006Kyoekihi(sogoRiyoDt.get(0).getKyoekihiBusiness().toString());
				}
				//開始日
				initDto.setSc006StartDay(sogoRiyoDt.get(0).getMutualUseStartDay());
				//終了日
				initDto.setSc006EndDay(sogoRiyoDt.get(0).getMutualUseEndDay());
				//配属会社名
				initDto.setSc006HaizokuKaisyaSelect(sogoRiyoDt.get(0).getAssignCompanyCd());
				//所属機関
				initDto.setSc006SyozokuKikan(sogoRiyoDt.get(0).getAssignAgencyName());
				//室・部名
				initDto.setSc006SituBuName(sogoRiyoDt.get(0).getAssignAffiliation1());
				//課等名
				initDto.setSc006KanadoMei(sogoRiyoDt.get(0).getAssignAffiliation2());
				//配属データコード番号
				initDto.setSc006HaizokuNo(sogoRiyoDt.get(0).getAssignCd());
			}
			//変更前の情報を非表示項目に設定する
			//変更前使用料パターンID（hidden）
			initDto.setHdnChangeBeforeRentalPatternId(initDto.getHdnRentalPatternId());
			//変更前入居予定日（hidden）
			initDto.setHdnChangeBeforeNyukyoYoteibi(skf3030Sc002SharedService.getDateText(initDto.getSc006NyukyoYoteiDay()));
			//変更前退居予定日（hidden）
			initDto.setHdnChangeBeforeTaikyoYoteibi(skf3030Sc002SharedService.getDateText(initDto.getSc006TaikyoYoteiDay()));
			//変更前役員算定（hidden）
			initDto.setHdnChangeBeforeYakuinSantei(initDto.getSc006YakuinSanteiSelect());
			//変更前社宅使用料調整金額（hidden）
			initDto.setHdnChangeBeforeShatakuShiyoryoChoseiKingaku(initDto.getSc006SiyoroTyoseiPay());
			//変更前個人負担共益費月額（hidden）
			initDto.setHdnChangeBeforeKojinFutanKyoekihiGetsugaku(initDto.getSc006KyoekihiMonthPay());
			//変更前個人負担共益費調整金額（hidden）
			initDto.setHdnChangeBeforeKojinFutanKyoekihiChoseiKingaku(initDto.getSc006KyoekihiTyoseiPay());
			//変更前区画１駐車場管理番号（hidden）
			initDto.setHdnChangeBeforeChushajoKanriNo1(initDto.getHdnChushajoKanriNo1());
			//変更前区画１利用開始日（hidden）
			initDto.setHdnChangeBeforeKukaku1RiyoKaishibi(skf3030Sc002SharedService.getDateText(initDto.getSc006RiyouStartDayOne()));
			//変更前区画１利用終了日（hidden）
			initDto.setHdnChangeBeforeKukaku1RiyoShuryobi(skf3030Sc002SharedService.getDateText(initDto.getSc006RiyouEndDayOne()));
			//変更前区画２駐車場管理番号（hidden）
			initDto.setHdnChangeBeforeChushajoKanriNo2(initDto.getHdnChushajoKanriNo2());
			//変更前区画２利用開始日（hidden）
			initDto.setHdnChangeBeforeKukaku2RiyoKaishibi(skf3030Sc002SharedService.getDateText(initDto.getSc006RiyouStartDayTwo()));
			//変更前区画２利用終了日（hidden）
			initDto.setHdnChangeBeforeKukaku2RiyoShuryobi(skf3030Sc002SharedService.getDateText(initDto.getSc006RiyouEndDayTwo()));
			//開始日（hidden）
			initDto.setHdnChangeBeforeKaishibi(skf3030Sc002SharedService.getDateText(initDto.getSc006StartDay()));
			//終了日（hidden）
			initDto.setHdnChangeBeforeShuryobi(skf3030Sc002SharedService.getDateText(initDto.getSc006EndDay()));
			//備品貸与日（hidden）
			initDto.setHdnChangeBeforeTaiyobi(skf3030Sc002SharedService.getDateText(initDto.getSc006TaiyoDay()));
			//備品返却日（hidden）
			initDto.setHdnChangeBeforeHenkyakubi(skf3030Sc002SharedService.getDateText(initDto.getSc006HenkyakuDay()));

			//社宅の使用実績確認を行い社宅使用実績フラグ（hidden）の設定
			String yearMonth = skfDateFormatUtils.addYearMonth(nengetu,-1);
			Skf3030TShatakuRentalRirekiKey rentRirekiKey = new Skf3030TShatakuRentalRirekiKey();
			rentRirekiKey.setShatakuKanriId(shatakuKanriId);
			rentRirekiKey.setYearMonth(yearMonth);
			Skf3030TShatakuRentalRireki shiyoryoRirekiCount = skf3030TShatakuRentalRirekiRepository.selectByPrimaryKey(rentRirekiKey);
			if(shiyoryoRirekiCount != null){
				initDto.setHdnRentalFlg("1");
			}else{
				initDto.setHdnRentalFlg("0");
			}
			
			Skf3030Sc002GetParkingRirekiCountExpParameter parkRirekiParam = new Skf3030Sc002GetParkingRirekiCountExpParameter();
			parkRirekiParam.setShatakuKanriId(shatakuKanriId);
			parkRirekiParam.setYearMonth(nengetu);
			if(!SkfCheckUtils.isNullOrEmpty(initDto.getHdnChushajoKanriNo1())){
				parkRirekiParam.setParkingKanriNo1(Long.parseLong(initDto.getHdnChushajoKanriNo1()));
			}
			if(!SkfCheckUtils.isNullOrEmpty(initDto.getHdnChushajoKanriNo2())){
				parkRirekiParam.setParkingKanriNo2(Long.parseLong(initDto.getHdnChushajoKanriNo2()));
			}
			List<Skf3030Sc002GetParkingRirekiCountExp> parkingRirekiDt = skf3030Sc002GetParkingRirekiCountExpRepository.getParkingRirekiCount(parkRirekiParam);
			if(parkingRirekiDt != null){
				if(parkingRirekiDt.size() > 1){
					//駐車場区画１使用実績フラグ（hidden）
					Integer flgKukaku1 = parkingRirekiDt.get(0).getCount();
					if(Objects.equals(flgKukaku1, 0)){
						initDto.setHdnShatakuKanriFlg1("0");
					}else{
						initDto.setHdnShatakuKanriFlg1("1");
					}
					
					//駐車場区画２使用実績フラグ（hidden）
					Integer flgKukaku2 = parkingRirekiDt.get(1).getCount();
					if(Objects.equals(flgKukaku2, 0)){
						initDto.setHdnShatakuKanriFlg2("0");
					}else{
						initDto.setHdnShatakuKanriFlg2("1");
					}
				}
			}
			
			// ドロップダウンリスト作成
			skf3030Sc002SharedService.setDdlControlValues(
					initDto.getSc006KyojyusyaKbnSelect(), sc006KyojyusyaKbnSelectList,
					initDto.getSc006YakuinSanteiSelect(), sc006YakuinSanteiSelectList,
					initDto.getSc006KyoekihiPayMonthSelect(), sc006KyoekihiPayMonthSelectList,
					initDto.getSc006KibouTimeInSelect(), sc006KibouTimeInSelectList,
					initDto.getSc006KibouTimeOutSelect(), sc006KibouTimeOutSelectList,
					initDto.getSc006SogoRyojokyoSelect(), sc006SogoRyojokyoSelectList,
					initDto.getSc006SogoHanteiKbnSelect(), sc006SogoHanteiKbnSelectList,
					initDto.getSc006SokinShatakuSelect(), sc006SokinShatakuSelectList,
					initDto.getSc006SokinKyoekihiSelect(), sc006SokinKyoekihiSelectList,
					initDto.getSc006OldKaisyaNameSelect(), sc006OldKaisyaNameSelectList,
					initDto.getSc006KyuyoKaisyaSelect(), sc006KyuyoKaisyaSelectList,
					initDto.getSc006HaizokuKaisyaSelect(), sc006HaizokuKaisyaSelectList,
					initDto.getSc006TaiyoKaisyaSelect(), sc006TaiyoKaisyaSelectList,
					initDto.getSc006KariukeKaisyaSelect(), sc006KariukeKaisyaSelectList);

		}
		
		//画面項目制御
		skf3030Sc002SharedService.setControlStatus(initDto);
		
		//フラグ管理の都合上備品設定はここで実施
		if(Skf3030Sc002CommonDto.NO_SHATAKU_KANRI_ID.equals(initDto.getHdnShatakuKanriId())){
			//「社宅管理台帳ID」が”0”の場合
			//「備品コード」、「備品名称」、「部屋備付状態」取得
			Skf3030Sc002GetBihinInfoForNoIdExpParameter bihinParam = new Skf3030Sc002GetBihinInfoForNoIdExpParameter();
			bihinParam.setShatakuKanriNo(Long.parseLong(initDto.getHdnShatakuKanriNo()));
			bihinParam.setShatakuRoomKanriNo(Long.parseLong(initDto.getHdnShatakuRoomKanriNo()));
			List<Skf3030Sc002GetBihinInfoForNoIdExp> grvBihinList = skf3030Sc002GetBihinInfoForNoIdExpRepository.getBihinInfoForNoId(bihinParam);
			initDto.setBihinInfoListTableData(skf3030Sc002SharedService.getBihinForNoIdListTableDataViewColumn(grvBihinList, initDto.getGBihinInfoControlStatusFlg()));

		}else{
			//備品情報取得
			Skf3030Sc002GetBihinInfoExpParameter bihinParam = new Skf3030Sc002GetBihinInfoExpParameter();
			bihinParam.setShatakuKanriId(Long.parseLong(initDto.getHdnShatakuKanriId()));
			bihinParam.setShatakuKanriNo(Long.parseLong(initDto.getHdnShatakuKanriNo()));
			bihinParam.setShatakuRoomKanriNo(Long.parseLong(initDto.getHdnShatakuRoomKanriNo()));
			bihinParam.setYearMonth(initDto.getHdnNengetsu());
			List<Skf3030Sc002GetBihinInfoExp> bihinDt = skf3030Sc002GetBihinInfoExpRepository.getBihinInfo(bihinParam);
			initDto.setBihinInfoListTableData(skf3030Sc002SharedService.getBihinListTableDataViewColumn(bihinDt, initDto.getGBihinInfoControlStatusFlg()));
			
		}
		
		if(TRUE.equals(initDto.getHdnSougoRiyouFlg())){
			//相互利用タブ有効な場合
			if(CodeConstant.MUTUAL_USE_KBN_UNAVAILABLE.equals(initDto.getSc006SogoRyojokyoSelect())){
				//相互利用状況(なし) ⇒ 相互利用関連項目は非活性
				skf3030Sc002SharedService.setSougoRiyoJokyoControlStatus(initDto,false);
			}else{
				//相互利用(あり)
				skf3030Sc002SharedService.setSougoRiyoJokyoControlStatus(initDto,true);
			}
		}
		
		
		// ドロップダウンリスト設定
		initDto.setSc006KyojyusyaKbnSelectList(sc006KyojyusyaKbnSelectList);
		initDto.setSc006YakuinSanteiSelectList(sc006YakuinSanteiSelectList);
		initDto.setSc006KyoekihiPayMonthSelectList(sc006KyoekihiPayMonthSelectList);
		initDto.setSc006KibouTimeInSelectList(sc006KibouTimeInSelectList);
		initDto.setSc006KibouTimeOutSelectList(sc006KibouTimeOutSelectList);
		initDto.setSc006SogoRyojokyoSelectList(sc006SogoRyojokyoSelectList);
		initDto.setSc006SogoHanteiKbnSelectList(sc006SogoHanteiKbnSelectList);
		initDto.setSc006SokinShatakuSelectList(sc006SokinShatakuSelectList);
		initDto.setSc006SokinKyoekihiSelectList(sc006SokinKyoekihiSelectList);
		initDto.setSc006OldKaisyaNameSelectList(sc006OldKaisyaNameSelectList);
		initDto.setSc006KyuyoKaisyaSelectList(sc006KyuyoKaisyaSelectList);
		initDto.setSc006HaizokuKaisyaSelectList(sc006HaizokuKaisyaSelectList);
		initDto.setSc006TaiyoKaisyaSelectList(sc006TaiyoKaisyaSelectList);
		initDto.setSc006KariukeKaisyaSelectList(sc006KariukeKaisyaSelectList);
	}
	

}
