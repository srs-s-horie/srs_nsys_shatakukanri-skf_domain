/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3030.domain.service.skf3030sc002;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Objects;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetRentalPatternForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetRentalPatternForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetRentalPatternInfoExp;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetRentalPatternInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetShatakuUpdateTaikyoCheckExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetShatakuUpdateTaikyoCheckExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002UpdateTeijiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuParkingBlock;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuParkingBlockKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuRoom;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuRoomBihin;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuRoomKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3022TTeijiBihinData;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3022TTeijiBihinDataKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3022TTeijiData;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TParkingRireki;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TParkingRirekiKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TRentalPattern;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuBihin;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuBihinRireki;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuBihinRirekiKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuLedger;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuMutual;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuMutualRireki;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuMutualRirekiKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuRentalRireki;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuRentalRirekiKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006UpdateRentalPatternExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc002.Skf3030Sc002GetRentalPatternInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc002.Skf3030Sc002GetShatakuUpdateTaikyoCheckExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc002.Skf3030Sc002UpdateShatakuKanriDaichoBihinExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc002.Skf3030Sc002UpdateShatakuKanriDaichoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc002.Skf3030Sc002UpdateShatakuKanriDaichoSogoRiyoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc002.Skf3030Sc002UpdateTeijiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc002.Skf3030Sc002UpdateTsukibetsuChushajoRirekiExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc002.Skf3030Sc002UpdateTsukibetsuShiyoryoRirekiExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc002.Skf3030Sc002UpdateTsukibetsuSogoRiyoRirekiExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuParkingBlockRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuRoomRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3022TTeijiBihinDataRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3022TTeijiDataRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3030TParkingRirekiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3030TRentalPatternRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3030TShatakuBihinRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3030TShatakuBihinRirekiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3030TShatakuLedgerRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3030TShatakuMutualRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3030TShatakuMutualRirekiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3030TShatakuRentalRirekiRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.LoginUserInfoUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.SkfPageBusinessLogicUtils;
import jp.co.c_nexco.skf.skf3030.domain.dto.skf3030Sc002common.Skf3030Sc002CommonDto;
import jp.co.c_nexco.skf.skf3030.domain.dto.skf3030sc002.Skf3030Sc002RegistDto;
import jp.co.intra_mart.mirage.integration.guice.Transactional;

/**
 * Skf3030Sc002RegistService 入退居情報登録画面の登録処理クラス。　 
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf3030Sc002RegistService extends BaseServiceAbstract<Skf3030Sc002RegistDto> {
	
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	@Autowired
	private SkfPageBusinessLogicUtils skfPageBusinessLogicUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3030Sc002SharedService skf3030Sc002SharedService;
	@Autowired
	private Skf3010MShatakuRoomRepository skf3010MShatakuRoomRepository;
	@Autowired
	private Skf3010MShatakuParkingBlockRepository skf3010MShatakuParkingBlockRepository;
	@Autowired
	private Skf3022TTeijiDataRepository skf3022TTeijiDataRepository;
	@Autowired
	private Skf3022TTeijiBihinDataRepository skf3022TTeijiBihinDataRepository;
	@Autowired
	private Skf3030TShatakuLedgerRepository skf3030TShatakuLedgerRepository;
	@Autowired
	private Skf3030TShatakuRentalRirekiRepository skf3030TShatakuRentalRirekiRepository;
	@Autowired
	private Skf3030TShatakuBihinRepository skf3030TShatakuBihinRepository;
	@Autowired
	private Skf3030TRentalPatternRepository skf3030TRentalPatternRepository;
	@Autowired
	private Skf3030TShatakuMutualRepository skf3030TShatakuMutualRepository;
	@Autowired
	private Skf3030TShatakuBihinRirekiRepository skf3030TShatakuBihinRirekiRepository;
	@Autowired
	private Skf3030TParkingRirekiRepository skf3030TParkingRirekiRepository;
	@Autowired
	private Skf3030TShatakuMutualRirekiRepository skf3030TShatakuMutualRirekiRepository;
	@Autowired
	private Skf3022Sc006UpdateRentalPatternExpRepository skf3022Sc006UpdateRentalPatternExpRepository;
	@Autowired
	private Skf3030Sc002UpdateTeijiDataExpRepository skf3030Sc002UpdateTeijiDataExpRepository;
	@Autowired
	private Skf3030Sc002GetRentalPatternInfoExpRepository skf3030Sc002GetRentalPatternInfoExpRepository;
	@Autowired
	private Skf3030Sc002GetShatakuUpdateTaikyoCheckExpRepository skf3030Sc002GetShatakuUpdateTaikyoCheckExpRepository;
	@Autowired
	private Skf3030Sc002UpdateTsukibetsuChushajoRirekiExpRepository skf3030Sc002UpdateTsukibetsuChushajoRirekiExpRepository;
	@Autowired
	private Skf3030Sc002UpdateShatakuKanriDaichoExpRepository skf3030Sc002UpdateShatakuKanriDaichoExpRepository;
	@Autowired
	private Skf3030Sc002UpdateShatakuKanriDaichoBihinExpRepository skf3030Sc002UpdateShatakuKanriDaichoBihinExpRepository;
	@Autowired
	private Skf3030Sc002UpdateShatakuKanriDaichoSogoRiyoExpRepository skf3030Sc002UpdateShatakuKanriDaichoSogoRiyoExpRepository;
	@Autowired
	private Skf3030Sc002UpdateTsukibetsuShiyoryoRirekiExpRepository skf3030Sc002UpdateTsukibetsuShiyoryoRirekiExpRepository;
	@Autowired
	private Skf3030Sc002UpdateTsukibetsuSogoRiyoRirekiExpRepository skf3030Sc002UpdateTsukibetsuSogoRiyoRirekiExpRepository;
	/**
	 * サービス処理を行う。　
	 * 
	 * @param registDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public Skf3030Sc002RegistDto index(Skf3030Sc002RegistDto registDto) throws Exception {
		
		registDto.setPageTitleKey(MessageIdConstant.SKF3030_SC002_TITLE);
 		
		// デバッグログ
		LogUtils.debugByMsg("登録");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("登録", CodeConstant.C001, FunctionIdConstant.SKF3030_SC002);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
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
		//可変ラベル値
		skf3030Sc002SharedService.setVariableLabel(skf3030Sc002SharedService.jsonArrayToArrayList(registDto.getJsonLabelList()), registDto);
		
		//フラグ初期化
		registDto.setBihinItiranFlg(0);
		//LoadCompleteイベントで備品状態の再読み込みを行わない(エラー発生時のみ)
		registDto.setBihinItiranReloadFlg(true);
		
		//駐車場利用開始日チェック
		if(checkRiyouStartDay(registDto)){
			return registDto;
		}
		

		//「社宅管理台帳ID」が”0”の場合
		if (Skf3030Sc002CommonDto.NO_SHATAKU_KANRI_ID.equals(registDto.getHdnShatakuKanriId()) ){
			//共通チェック
			if (skf3030Sc002SharedService.validateInput(registDto) ){
				//再計算処理行う
				skf3030Sc002SharedService.siyoryoKeiSanSync(registDto);
				throwBusinessExceptionIfErrors(registDto.getResultMessages());
				String shatakuKanriId = CodeConstant.DOUBLE_QUOTATION;
				Long teijiNo = skfBaseBusinessLogicUtils.getMaxTeijiNo();
	
				//使用料パターン一覧情報設定
				String rentalPatternUpdateDate = registDto.getHdnRentalPatternUpdateDate();
				Date rentalPatternUpdateDateForRegist = skfBaseBusinessLogicUtils.getSystemDateTime();
				Map<Skf3030Sc002CommonDto.RENTAL_PATTERN, String> rentalPatternTorokuList 
					= new HashMap<Skf3030Sc002CommonDto.RENTAL_PATTERN, String>();
				rentalPatternTorokuList = skf3030Sc002SharedService.setRentalPatternList(registDto);
	
				shatakuKanriId = registClickInsert(registDto,
						rentalPatternUpdateDate,
						rentalPatternUpdateDateForRegist,
						rentalPatternTorokuList,
						teijiNo);
				
//				Map<String,Object> returnMap = insertData(
//						skf3030Sc002SharedService.getTbtTeijiDataColumnInfoList(teijiNo,registDto), 
//						skf3030Sc002SharedService.getTbtPublicShatakuLedgerDataColumnInfoList(registDto),
//						registDto.getHdnShatakuKanriNo(),
//						registDto.getHdnShatakuRoomKanriNo(),
//						registDto.getHdnChushajoKanriNo1(),
//						registDto.getHdnChushajoKanriNo2(),
//						registDto.getSc006RiyouStartDayOne(),
//						registDto.getSc006RiyouStartDayTwo(),
//						skf3030Sc002SharedService.setBihinData(registDto,teijiNo),
//						skf3030Sc002SharedService.getTbtPublicShatakuParkingBlockDataColumnInfoList(1, false,registDto),
//						skf3030Sc002SharedService.getTbtPublicShatakuParkingBlockDataColumnInfoList(2, false,registDto),
//						CodeConstant.C001,
//						LoginUserInfoUtils.getUserCd(),
//						registDto.getPageId(),
//						registDto.getSc006TaiyoDay(),
//						rentalPatternUpdateDate,
//						rentalPatternUpdateDateForRegist,
//						rentalPatternTorokuList);
//				int returnCount = Integer.parseInt(returnMap.get("returnCount").toString());
//				shatakuKanriId = returnMap.get("shatakuKanriId").toString();
//	
//				if(returnCount == 0){
//						ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1073);
//						throwBusinessExceptionIfErrors(registDto.getResultMessages());
//				}else if(returnCount == -1){
//						ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1076);
//						throwBusinessExceptionIfErrors(registDto.getResultMessages());
//				}else if(returnCount == -2){
//						ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.W_SKF_1010);
//						throwBusinessExceptionIfErrors(registDto.getResultMessages());
//				}else{
//					ServiceHelper.addResultMessage(registDto, MessageIdConstant.I_SKF_1012);
					//登録した内容を画面に反映する
					//取得した社宅管理IDをhdnに設定
					//Me.hdnShatakuKanriId.Value() = shatakuKanriId
					registDto.setHdnShatakuKanriId(shatakuKanriId);
					//社宅使用料日割金額
					//Me.litShatakuShiyoryoHiwariKingaku.Text = StringUtil.ConvertYenFormat(Integer.Parse(Me.hdnKaiSanAfterShatakuShiyoryoHiwariKingaku.Value), False)
					registDto.setSc006SiyoryoHiwariPay(skf3030Sc002SharedService.convertYenFormat(Integer.parseInt(registDto.getHdnKaiSanAfterShatakuShiyoryoHiwariKingaku()), false));
					//社宅使用料月額（調整後）
					//Me.litShatakuShiyoryoGetsugakuChoseigo.Text = StringUtil.ConvertYenFormat(Integer.Parse(Me.hdnKaiSanAfterShatakuShiyoryoGetsugakuChoseigo.Value()), False)
					registDto.setSc006SyatauMonthPayAfter(skf3030Sc002SharedService.convertYenFormat(Integer.parseInt(registDto.getHdnKaiSanAfterShatakuShiyoryoGetsugakuChoseigo()),false));
					//駐車場使用料日割金額（区画１）
					//Me.litKukaku1ChushajoShiyoroHiwariKingaku.Text = StringUtil.ConvertYenFormat(Integer.Parse(Me.hdnKaiSanAfterKukaku1ChushajoShiyoroHiwariKingaku.Value()), False)
					registDto.setSc006TyusyaDayPayOne(skf3030Sc002SharedService.convertYenFormat(Integer.parseInt(registDto.getHdnKaiSanAfterKukaku1ChushajoShiyoroHiwariKingaku()),false));
					//駐車場使用料日割金額（区画２）
					//Me.litKukaku2ChushajoShiyoroHiwariKingaku.Text = StringUtil.ConvertYenFormat(Integer.Parse(Me.hdnKaiSanAfterKukaku2ChushajoShiyoroHiwariKingaku.Value()), False)
					registDto.setSc006TyusyaDayPayTwo(skf3030Sc002SharedService.convertYenFormat(Integer.parseInt(registDto.getHdnKaiSanAfterKukaku2ChushajoShiyoroHiwariKingaku()),false));
					//駐車場使用料月額（調整後）
					//Me.litChushajoShiyoryoGetsugakuChoseigo.Text = StringUtil.ConvertYenFormat(Integer.Parse(Me.hdnKaiSanAfterChushajoShiyoryoGetsugakuChoseigo.Value()), False)
					registDto.setSc006TyusyaMonthPayAfter(skf3030Sc002SharedService.convertYenFormat(Integer.parseInt(registDto.getHdnKaiSanAfterChushajoShiyoryoGetsugakuChoseigo()),false));
					//個人負担共益費月額（調整後）
					//Me.litKojinFutanKyoekihiGetsugakuChoseigo.Text = Me.hdnKaiSanAfterKojinFutanKyoekihiGetsugakuChoseigo.Value()
					registDto.setSc006KyoekihiPayAfter(skf3030Sc002SharedService.convertYenFormat(Integer.parseInt(registDto.getHdnKaiSanAfterKojinFutanKyoekihiGetsugakuChoseigo()),false));
					//Hiddenの更新
					//使用料パターンID（hidden）と変更前使用料パターンID（hidden）比較
					//Me.hdnChangeBeforeRentalPatternId.Value = Me.hdnRentalPatternId.Value
					registDto.setHdnChangeBeforeRentalPatternId(registDto.getHdnRentalPatternId());
					//【使用料計算機能対応】使用料パターンID
					//Me.hdnShiyoryoKeisanPatternId.Value = rentalPatternTorokuList(1)
					registDto.setHdnShiyoryoKeisanPatternId(rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.RENTAL_PATTERNID));
					//使用料パターン排他更新日
	//					Me.hdnRentalPatternUpdateDate.Value = HttpUtility.HtmlEncode( _
	//							DateUtil.ConversionFormatYYYYMMDDHHMMSSF6WithDelimiter( _
	//							    S3002_NyutaikyoDataRegistBusinessLogic.GetRentalPatternInfo(Me.hdnShiyoryoKeisanPatternId.Value)(0).UPDATE_DATE))
					List<Skf3030Sc002GetRentalPatternInfoExp> rentalPatternInfoList = new ArrayList<Skf3030Sc002GetRentalPatternInfoExp>();
					Skf3030Sc002GetRentalPatternInfoExpParameter param = new Skf3030Sc002GetRentalPatternInfoExpParameter();
					param.setRentalPatternId(Long.parseLong(registDto.getHdnShiyoryoKeisanPatternId()));
					// 使用料パターン情報取得
					rentalPatternInfoList = skf3030Sc002GetRentalPatternInfoExpRepository.getRentalPatternInfo(param);
					if(rentalPatternInfoList.size()>0){
						String updateDate = dateFormat.format(rentalPatternInfoList.get(0).getUpdateDate());
						registDto.setHdnRentalPatternUpdateDate(updateDate);
					}
					
					//入居予定日と変更前入居予定日（hidden）比較
					//Me.hdnChangeBeforeNyukyoYoteibi.Value = HttpUtility.HtmlEncode(Me.txtNyukyoYoteibi.Text.Replace(Constant.Sign.SLASH, String.Empty).Replace(Constant.Sign.UNDER_SCORE, String.Empty))
					registDto.setHdnChangeBeforeNyukyoYoteibi(skf3030Sc002SharedService.getDateText(registDto.getSc006NyukyoYoteiDay()));
					//退居予定日と変更前退居予定日（hidden）比較
					//Me.hdnChangeBeforeTaikyoYoteibi.Value = HttpUtility.HtmlEncode(Me.txtTaikyoYoskf3030Sc002SharedService.convertYenFormatteibi.Text.Replace(Constant.Sign.SLASH, String.Empty).Replace(Constant.Sign.UNDER_SCORE, String.Empty))
					registDto.setHdnChangeBeforeTaikyoYoteibi(skf3030Sc002SharedService.getDateText(registDto.getSc006TaikyoYoteiDay()));
					//役員算定と変更前役員算定（hidden）比較
					//Me.hdnChangeBeforeYakuinSantei.Value = Me.ddlYakuinSantei.SelectedValue
					registDto.setHdnChangeBeforeYakuinSantei(registDto.getSc006YakuinSanteiSelect());
					//社宅使用料調整金額と変更前社宅使用料調整金額（hidden）比較
					//Me.hdnChangeBeforeShatakuShiyoryoChoseiKingaku.Value = GetPayText(Me.txtShatakuShiyoryoChoseiKingaku.Text)
					registDto.setHdnChangeBeforeShatakuShiyoryoChoseiKingaku(skf3030Sc002SharedService.getPayText(registDto.getSc006SiyoroTyoseiPay()));
					//個人負担共益費月額と変更前個人負担共益費月額（hidden）比較
					//Me.hdnChangeBeforeKojinFutanKyoekihiGetsugaku.Value = GetPayText(Me.txtKojinFutanKyoekihiGetsugaku.Text)
					registDto.setHdnChangeBeforeKojinFutanKyoekihiGetsugaku(skf3030Sc002SharedService.getPayText(registDto.getSc006KyoekihiMonthPay()));
					//個人負担共益費調整金額と変更前個人負担共益費調整金額（hidden）比較
					//Me.hdnChangeBeforeKojinFutanKyoekihiChoseiKingaku.Value = GetPayText(Me.txtKojinFutanKyoekihiChoseiKingaku.Text)
					registDto.setHdnChangeBeforeKojinFutanKyoekihiChoseiKingaku(skf3030Sc002SharedService.getPayText(registDto.getSc006KyoekihiTyoseiPay()));
					//区画１駐車場管理番号（hidden）と変更前区画１駐車場管理番号（hidden）比較
					//Me.hdnChangeBeforeChushajoKanriNo1.Value = Me.hdnChushajoKanriNo1.Value
					registDto.setHdnChangeBeforeChushajoKanriNo1(registDto.getHdnChushajoKanriNo1());
					//区画１利用開始日と変更前区画１利用開始日（hidden）比較
					//Me.hdnChangeBeforeKukaku1RiyoKaishibi.Value = HttpUtility.HtmlEncode(Me.txtKukaku1RiyoKaishibi.Text.Replace(Constant.Sign.SLASH, String.Empty).Replace(Constant.Sign.UNDER_SCORE, String.Empty))
					registDto.setHdnChangeBeforeKukaku1RiyoKaishibi(skf3030Sc002SharedService.getDateText(registDto.getSc006RiyouStartDayOne()));
					//区画１利用終了日と変更前区画１利用終了日（hidden）比較
					//Me.hdnChangeBeforeKukaku1RiyoShuryobi.Value = HttpUtility.HtmlEncode(Me.txtKukaku1RiyoShuryobi.Text.Replace(Constant.Sign.SLASH, String.Empty).Replace(Constant.Sign.UNDER_SCORE, String.Empty))
					registDto.setHdnChangeBeforeKukaku1RiyoShuryobi(skf3030Sc002SharedService.getDateText(registDto.getSc006RiyouEndDayOne()));
					//区画２駐車場管理番号（hidden）と変更前区画２駐車場管理番号（hidden）比較
					//Me.hdnChangeBeforeChushajoKanriNo2.Value = Me.hdnChushajoKanriNo2.Value
					registDto.setHdnChangeBeforeChushajoKanriNo2(registDto.getHdnChushajoKanriNo2());
					//区画２利用開始日と変更前区画２利用開始日（hidden）比較
					//Me.hdnChangeBeforeKukaku2RiyoKaishibi.Value = HttpUtility.HtmlEncode(Me.txtKukaku2RiyoKaishibi.Text.Replace(Constant.Sign.SLASH, String.Empty).Replace(Constant.Sign.UNDER_SCORE, String.Empty))
					registDto.setHdnChangeBeforeKukaku2RiyoKaishibi(skf3030Sc002SharedService.getDateText(registDto.getSc006RiyouStartDayTwo()));
					//区画２利用終了日と変更前区画２利用終了日（hidden）比較
					//Me.hdnChangeBeforeKukaku2RiyoShuryobi.Value = HttpUtility.HtmlEncode(Me.txtKukaku2RiyoShuryobi.Text.Replace(Constant.Sign.SLASH, String.Empty).Replace(Constant.Sign.UNDER_SCORE, String.Empty))
					registDto.setHdnChangeBeforeKukaku2RiyoShuryobi(skf3030Sc002SharedService.getDateText(registDto.getSc006RiyouEndDayTwo()));
					//開始日(hidden)
					//Me.hdnChangeBeforeKaishibi.Value = HttpUtility.HtmlEncode(Me.txtKaishibi.Text.Replace(Constant.Sign.SLASH, String.Empty).Replace(Constant.Sign.UNDER_SCORE, String.Empty))
					registDto.setHdnChangeBeforeKaishibi(skf3030Sc002SharedService.getDateText(registDto.getSc006StartDay()));
					//終了日(hidden)
					//Me.hdnChangeBeforeShuryobi.Value = HttpUtility.HtmlEncode(Me.txtShuryobi.Text.Replace(Constant.Sign.SLASH, String.Empty).Replace(Constant.Sign.UNDER_SCORE, String.Empty))
					registDto.setHdnChangeBeforeShuryobi(skf3030Sc002SharedService.getDateText(registDto.getSc006EndDay()));
					//貸与日(hidden)
					//Me.hdnChangeBeforeTaiyobi.Value = HttpUtility.HtmlEncode(Me.txtBihinTaiyobi.Text.Replace(Constant.Sign.SLASH, String.Empty).Replace(Constant.Sign.UNDER_SCORE, String.Empty))
					registDto.setHdnChangeBeforeTaiyobi(skf3030Sc002SharedService.getDateText(registDto.getSc006TaiyoDay()));
					//返却日(hidden)
					//Me.hdnChangeBeforeHenkyakubi.Value = HttpUtility.HtmlEncode(Me.txtBihinHenkyakubi.Text.Replace(Constant.Sign.SLASH, String.Empty).Replace(Constant.Sign.UNDER_SCORE, String.Empty))
					registDto.setHdnChangeBeforeHenkyakubi(skf3030Sc002SharedService.getDateText(registDto.getSc006HenkyakuDay()));
					//登録成功時は備品の再読み込みを実施
					registDto.setBihinItiranReloadFlg(false);
//				}
			
//			//メッセージを表示
//			Me.hdnFieldMessage.Value() = HttpUtility.HtmlEncode(message)
				//画面項目
				skf3030Sc002SharedService.setControlStatus(registDto);
				//相互利用状況の活性非活性を設定
				skf3030Sc002SharedService.setControlStatusSogoRiyo(registDto);
			}
		}else{
			//「社宅管理台帳ID」が値を持つ場合
			//共通チェック
			if( skf3030Sc002SharedService.validateInput(registDto) ){
//				int changeFlg = 0;
				String taikyoFlg  = Skf3030Sc002CommonDto.STR_UPDATE;
//				//使用料パターンID（hidden）と変更前使用料パターンID（hidden）比較
//				if(registDto.getHdnRentalPatternId().compareTo(registDto.getHdnChangeBeforeRentalPatternId()) != 0){
//					changeFlg += 1;
//				}
//				//入居予定日と変更前入居予定日（hidden）比較
//				if(skf3030Sc002SharedService.getDateText(registDto.getSc006NyukyoYoteiDay()).compareTo(registDto.getHdnChangeBeforeNyukyoYoteibi()) != 0){
//					changeFlg += 1;
//				}
//				//退居予定日と変更前退居予定日（hidden）比較
//				if(skf3030Sc002SharedService.getDateText(registDto.getSc006TaikyoYoteiDay()).compareTo(registDto.getHdnChangeBeforeTaikyoYoteibi()) != 0){
//					changeFlg += 1;
//				}
//				If Not String.IsNullOrEmpty(Me.txtTaikyoYoteibi.Text.Replace(Constant.Sign.SLASH, String.Empty).Replace(Constant.Sign.UNDER_SCORE, String.Empty)) Then
				if(!SkfCheckUtils.isNullOrEmpty(skf3030Sc002SharedService.getDateText(registDto.getSc006TaikyoYoteiDay())) ){
					String nyukyoDate = CodeConstant.DOUBLE_QUOTATION;
					if(!SkfCheckUtils.isNullOrEmpty(registDto.getHdnShatakuKanriNo()) &&
						!SkfCheckUtils.isNullOrEmpty(registDto.getHdnShatakuRoomKanriNo()) &&
						!SkfCheckUtils.isNullOrEmpty(registDto.getHdnShatakuKanriId())){
						//同じ社宅の入居日（現在表示している台帳以外）を取得
						Skf3030Sc002GetShatakuUpdateTaikyoCheckExpParameter sutCheckParam = new Skf3030Sc002GetShatakuUpdateTaikyoCheckExpParameter();
						sutCheckParam.setYearMonth(skfBaseBusinessLogicUtils.getSystemProcessNenGetsu());
						sutCheckParam.setShatakuKanriNo(Long.parseLong(registDto.getHdnShatakuKanriNo()));
						sutCheckParam.setShatakuRoomKanriNo(Long.parseLong(registDto.getHdnShatakuRoomKanriNo()));
						sutCheckParam.setShatakuKanriId(Long.parseLong(registDto.getHdnShatakuKanriId()));
						List<Skf3030Sc002GetShatakuUpdateTaikyoCheckExp> nyukyoDateList = new ArrayList<Skf3030Sc002GetShatakuUpdateTaikyoCheckExp>();
						nyukyoDateList = skf3030Sc002GetShatakuUpdateTaikyoCheckExpRepository.getShatakuUpdateTaikyoCheck(sutCheckParam);
						if(nyukyoDateList.size() > 0){
							nyukyoDate = nyukyoDateList.get(0).getNyukyoDate();
						}
					}
					if(SkfCheckUtils.isNullOrEmpty(nyukyoDate)){
						taikyoFlg = Skf3030Sc002CommonDto.STR_UPDATE_TAIKYO;
					}else{
						taikyoFlg = Skf3030Sc002CommonDto.STR_UPDATE;
					}
	
					//退居日が前月以前であれば、即空きに設定
					if(skfPageBusinessLogicUtils.checkPassedDay(skf3030Sc002SharedService.getDateText(registDto.getSc006TaikyoYoteiDay()))){
						taikyoFlg = Skf3030Sc002CommonDto.STR_DELETE;
					}
				}
				//changeFlg未使用のためコメントアウト
//				//役員算定と変更前役員算定（hidden）比較
////				If Me.ddlYakuinSantei.SelectedValue.CompareTo(Me.hdnChangeBeforeYakuinSantei.Value) <> 0 Then
//				if( !Objects.equal(registDto.getSc006YakuinSanteiSelect(), registDto.getHdnChangeBeforeYakuinSantei()) ){
//					changeFlg += 1;
//				}
//				//社宅使用料調整金額と変更前社宅使用料調整金額（hidden）比較
////				If Me.txtShatakuShiyoryoChoseiKingaku.Text.CompareTo(Me.hdnChangeBeforeShatakuShiyoryoChoseiKingaku.Value) <> 0 Then
//				if(registDto.getSc006SiyoroTyoseiPay().compareTo(registDto.getHdnChangeBeforeShatakuShiyoryoChoseiKingaku()) != 0){
//					changeFlg += 1;
//				}
//				//個人負担共益費月額と変更前個人負担共益費月額（hidden）比較
////				If Me.txtKojinFutanKyoekihiGetsugaku.Text.CompareTo(Me.hdnChangeBeforeKojinFutanKyoekihiGetsugaku.Value) <> 0 Then
//				if(registDto.getSc006KyoekihiMonthPay().compareTo(registDto.getHdnChangeBeforeKojinFutanKyoekihiGetsugaku()) != 0){
//					changeFlg += 1;
//				}
//				//個人負担共益費調整金額と変更前個人負担共益費調整金額（hidden）比較
////				If Me.txtKojinFutanKyoekihiChoseiKingaku.Text.CompareTo(Me.hdnChangeBeforeKojinFutanKyoekihiChoseiKingaku.Value) <> 0 Then
//				if(registDto.getSc006KyoekihiTyoseiPay().compareTo(registDto.getHdnChangeBeforeKojinFutanKyoekihiChoseiKingaku()) != 0){
//					changeFlg += 1;
//				}
//				//区画１駐車場管理番号（hidden）と変更前区画１駐車場管理番号（hidden）比較
////				If Me.hdnChushajoKanriNo1.Value.CompareTo(Me.hdnChangeBeforeChushajoKanriNo1.Value) <> 0 Then
//				if(registDto.getHdnChushajoKanriNo1().compareTo(registDto.getHdnChangeBeforeChushajoKanriNo1()) != 0){
//					changeFlg += 1;
//				}
//				//区画１利用開始日と変更前区画１利用開始日（hidden）比較
////				If Me.txtKukaku1RiyoKaishibi.Text.Replace(Constant.Sign.SLASH, String.Empty).Replace(Constant.Sign.UNDER_SCORE, String.Empty).CompareTo(Me.hdnChangeBeforeKukaku1RiyoKaishibi.Value) <> 0 Then
//				if(skf3030Sc002SharedService.getDateText(registDto.getSc006RiyouStartDayOne()).compareTo(registDto.getHdnChangeBeforeKukaku1RiyoKaishibi()) != 0){
//					changeFlg += 1;
//				}
//				//区画１利用終了日と変更前区画１利用終了日（hidden）比較
////				If Me.txtKukaku1RiyoShuryobi.Text.Replace(Constant.Sign.SLASH, String.Empty).Replace(Constant.Sign.UNDER_SCORE, String.Empty).CompareTo(Me.hdnChangeBeforeKukaku1RiyoShuryobi.Value) <> 0 Then
//				if(skf3030Sc002SharedService.getDateText(registDto.getSc006RiyouEndDayOne()).compareTo(registDto.getHdnChangeBeforeKukaku1RiyoShuryobi()) != 0){
//					changeFlg += 1;
//				}
//				//区画２駐車場管理番号（hidden）と変更前区画２駐車場管理番号（hidden）比較
////				If Me.hdnChushajoKanriNo2.Value.CompareTo(Me.hdnChangeBeforeChushajoKanriNo2.Value) <> 0 Then
//				if(!registDto.getHdnChushajoKanriNo2().equals(registDto.getHdnChangeBeforeChushajoKanriNo2())){
//					changeFlg += 1;
//				}
//				//区画２利用開始日と変更前区画２利用開始日（hidden）比較
////				If Me.txtKukaku2RiyoKaishibi.Text.Replace(Constant.Sign.SLASH, String.Empty).Replace(Constant.Sign.UNDER_SCORE, String.Empty).CompareTo(Me.hdnChangeBeforeKukaku2RiyoKaishibi.Value) <> 0 Then
//				if(!skf3030Sc002SharedService.getDateText(registDto.getSc006RiyouStartDayTwo()).equals(registDto.getHdnChangeBeforeKukaku2RiyoKaishibi())){
//					changeFlg += 1;
//				}
//				//区画２利用終了日と変更前区画２利用終了日（hidden）比較
////				If Me.txtKukaku2RiyoShuryobi.Text.Replace(Constant.Sign.SLASH, String.Empty).Replace(Constant.Sign.UNDER_SCORE, String.Empty).CompareTo(Me.hdnChangeBeforeKukaku2RiyoShuryobi.Value) <> 0 Then
//				if(!skf3030Sc002SharedService.getDateText(registDto.getSc006RiyouEndDayTwo()).equals(registDto.getHdnChangeBeforeKukaku2RiyoShuryobi())){
//					changeFlg += 1;
//				}
	
				//再計算処理行う
				skf3030Sc002SharedService.siyoryoKeiSanSync(registDto);
				throwBusinessExceptionIfErrors(registDto.getResultMessages());
	
				Boolean changeFlg1 = false;
				Boolean changeFlg2 = false;
				Boolean changeFlg3 = false;
				Boolean changeFlg4 = false;
				//変更前区画１駐車場管理番号（hidden）と区画１駐車場管理番号（hidden）比較
//				If Me.hdnChangeBeforeChushajoKanriNo1.Value.CompareTo(Me.hdnChushajoKanriNo1.Value) <> 0 Then
				if(!Objects.equal(registDto.getHdnChangeBeforeChushajoKanriNo1(), registDto.getHdnChushajoKanriNo1())){
					changeFlg1 = true;
				}
				//変更前区画２駐車場管理番号（hidden）と区画２駐車場管理番号（hidden）比較
//				If Me.hdnChangeBeforeChushajoKanriNo2.Value.CompareTo(Me.hdnChushajoKanriNo2.Value) <> 0 Then
				if(!Objects.equal(registDto.getHdnChangeBeforeChushajoKanriNo2(), registDto.getHdnChushajoKanriNo2())){
					changeFlg2 = true;
				}
	
				//変更前区画１駐車場管理番号(hidden)、駐車場開始日(hidden)、駐車場終了日(hidden）比較
				if(!Objects.equal(registDto.getHdnChangeBeforeChushajoKanriNo1(),registDto.getHdnChushajoKanriNo1()) ||
						skf3030Sc002SharedService.getDateText(registDto.getSc006RiyouStartDayOne()).compareTo(registDto.getHdnChangeBeforeKukaku1RiyoKaishibi()) != 0 ||
						skf3030Sc002SharedService.getDateText(registDto.getSc006RiyouEndDayOne()).compareTo(registDto.getHdnChangeBeforeKukaku1RiyoShuryobi()) != 0 ){
//				If Me.hdnChangeBeforeChushajoKanriNo1.Value.CompareTo(Me.hdnChushajoKanriNo1.Value) <> 0 Or _
//				   Me.txtKukaku1RiyoKaishibi.Text.Replace(Constant.Sign.SLASH, String.Empty).Replace(Constant.Sign.UNDER_SCORE, String.Empty).CompareTo(Me.hdnChangeBeforeKukaku1RiyoKaishibi.Value) <> 0 Or _
//				   Me.txtKukaku1RiyoShuryobi.Text.Replace(Constant.Sign.SLASH, String.Empty).Replace(Constant.Sign.UNDER_SCORE, String.Empty).CompareTo(Me.hdnChangeBeforeKukaku1RiyoShuryobi.Value) <> 0 Then
					changeFlg3 = true;
				}
	
				//変更前区画２駐車場管理番号(hidden)、駐車場開始日(hidden)、駐車場終了日(hidden）比較
				if(!Objects.equal(registDto.getHdnChangeBeforeChushajoKanriNo2(), registDto.getHdnChushajoKanriNo2()) ||
						skf3030Sc002SharedService.getDateText(registDto.getSc006RiyouStartDayTwo()).compareTo(registDto.getHdnChangeBeforeKukaku2RiyoKaishibi()) != 0 ||
						skf3030Sc002SharedService.getDateText(registDto.getSc006RiyouEndDayTwo()).compareTo(registDto.getHdnChangeBeforeKukaku2RiyoShuryobi()) != 0 ){
//				If Me.hdnChangeBeforeChushajoKanriNo2.Value.CompareTo(Me.hdnChushajoKanriNo2.Value) <> 0 Or _
//				   Me.txtKukaku2RiyoKaishibi.Text.Replace(Constant.Sign.SLASH, String.Empty).Replace(Constant.Sign.UNDER_SCORE, String.Empty).CompareTo(Me.hdnChangeBeforeKukaku2RiyoKaishibi.Value) <> 0 Or _
//				   Me.txtKukaku2RiyoShuryobi.Text.Replace(Constant.Sign.SLASH, String.Empty).Replace(Constant.Sign.UNDER_SCORE, String.Empty).CompareTo(Me.hdnChangeBeforeKukaku2RiyoShuryobi.Value) <> 0 Then
					changeFlg4 = true;
				}
	
				//使用料パターン一覧情報設定
				String rentalPatternUpdateDate  = registDto.getHdnRentalPatternUpdateDate();
				Date rentalPatternUpdateDateForRegist = skfBaseBusinessLogicUtils.getSystemDateTime();
				Map<Skf3030Sc002CommonDto.RENTAL_PATTERN, String> rentalPatternTorokuList 
				= new HashMap<Skf3030Sc002CommonDto.RENTAL_PATTERN, String>();
				rentalPatternTorokuList = skf3030Sc002SharedService.setRentalPatternList(registDto);

				registClickUpdate(registDto,
						taikyoFlg,
						changeFlg1,
						changeFlg2,
						changeFlg3,
						changeFlg4,
						rentalPatternUpdateDate, 
						rentalPatternUpdateDateForRegist,
						rentalPatternTorokuList);
				
//				int returnCount = updateData(
//							skf3030Sc002SharedService.getTbtPublicShatakuLedgerDataColumnInfoList(registDto),
//							skf3030Sc002SharedService.getTbtPublicShatakuBihinDataColumnInfoList(registDto),
//							skf3030Sc002SharedService.getTbtPublicShatakuMutualDataColumnInfoList(registDto),
//							skf3030Sc002SharedService.getTbtPublicShatakuRentalRirekiDataColumnInfoList(registDto),
//							skf3030Sc002SharedService.setBihinData(registDto,0L),
//							skf3030Sc002SharedService.setBihinData(registDto),
//							skf3030Sc002SharedService.getTbtPublicParkingRirekiDataColumnInfoList(1, true, registDto),
//							skf3030Sc002SharedService.getTbtPublicParkingRirekiDataColumnInfoList(1, false, registDto),
//							changeFlg3,
//							skf3030Sc002SharedService.getDateText(registDto.getSc006RiyouStartDayOne()), 
//							skf3030Sc002SharedService.getTbtPublicShatakuParkingBlockDataColumnInfoList(1, false, registDto),
//							changeFlg1,
//							skf3030Sc002SharedService.getTbtPublicShatakuParkingBlockDataColumnInfoList(1, true, registDto),
//							skf3030Sc002SharedService.getTbtPublicParkingRirekiDataColumnInfoList(2, true, registDto),
//							skf3030Sc002SharedService.getTbtPublicParkingRirekiDataColumnInfoList(2, false, registDto),
//							changeFlg4,
//							skf3030Sc002SharedService.getDateText(registDto.getSc006RiyouStartDayTwo()),
//							skf3030Sc002SharedService.getTbtPublicShatakuParkingBlockDataColumnInfoList(2, false, registDto),
//							changeFlg2,
//							skf3030Sc002SharedService.getTbtPublicShatakuParkingBlockDataColumnInfoList(2, true, registDto),
//							skf3030Sc002SharedService.getTbtPublicShatakuMutualRirekiDataColumnInfoList(registDto),
//							skf3030Sc002SharedService.getTbtPublicShatakuRoomDataColumnInfoList(taikyoFlg,registDto),
//							skf3030Sc002SharedService.getTbtPublicShatakuRoomBihinDataColumnInfoList(registDto),
//							registDto.getHdnShatakuKanriId(),
//							registDto.getHdnNengetsu(),
//							LoginUserInfoUtils.getUserCd(),
//							registDto.getPageId(),
//							rentalPatternUpdateDate, 
//							rentalPatternUpdateDateForRegist,
//							rentalPatternTorokuList);
//				if(returnCount == 0){
//					ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1075);
//					throwBusinessExceptionIfErrors(registDto.getResultMessages());
//				}else if(returnCount == -1){
//					ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.W_SKF_1009);
//					throwBusinessExceptionIfErrors(registDto.getResultMessages());
//				}else{
//					//更新完了メッセージ
//					ServiceHelper.addResultMessage(registDto, MessageIdConstant.I_SKF_1011);
					//更新完了したら、hdn変更後 ⇒変更前へ反映
					//使用料パターンID（hidden）と変更前使用料パターンID（hidden）比較
					//Me.hdnChangeBeforeRentalPatternId.Value = Me.hdnRentalPatternId.Value
					registDto.setHdnChangeBeforeRentalPatternId(registDto.getHdnRentalPatternId());
					//【使用料計算機能対応】使用料パターンID
					//Me.hdnShiyoryoKeisanPatternId.Value = rentalPatternTorokuList(1)
					registDto.setHdnShiyoryoKeisanPatternId(rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.RENTAL_PATTERNID));
					//使用料パターン排他更新日
					//Me.hdnRentalPatternUpdateDate.Value = HttpUtility.HtmlEncode( _
					//		DateUtil.ConversionFormatYYYYMMDDHHMMSSF6WithDelimiter( _
					//		    S3002_NyutaikyoDataRegistBusinessLogic.GetRentalPatternInfo(Me.hdnShiyoryoKeisanPatternId.Value)(0).UPDATE_DATE))
					List<Skf3030Sc002GetRentalPatternInfoExp> rentalPatternInfoList = new ArrayList<Skf3030Sc002GetRentalPatternInfoExp>();
					Skf3030Sc002GetRentalPatternInfoExpParameter param = new Skf3030Sc002GetRentalPatternInfoExpParameter();
					param.setRentalPatternId(Long.parseLong(registDto.getHdnShiyoryoKeisanPatternId()));
					// 使用料パターン情報取得
					rentalPatternInfoList = skf3030Sc002GetRentalPatternInfoExpRepository.getRentalPatternInfo(param);
					if(rentalPatternInfoList.size()>0){
						String updateDate = dateFormat.format(rentalPatternInfoList.get(0).getUpdateDate());
						registDto.setHdnRentalPatternUpdateDate(updateDate);
					}
					//入居予定日と変更前入居予定日（hidden）比較
					//Me.hdnChangeBeforeNyukyoYoteibi.Value = HttpUtility.HtmlEncode(Me.txtNyukyoYoteibi.Text.Replace(Constant.Sign.SLASH, String.Empty).Replace(Constant.Sign.UNDER_SCORE, String.Empty))
					registDto.setHdnChangeBeforeNyukyoYoteibi(skf3030Sc002SharedService.getDateText(registDto.getSc006NyukyoYoteiDay()));
					//退居予定日と変更前退居予定日（hidden）比較
					//Me.hdnChangeBeforeTaikyoYoteibi.Value = HttpUtility.HtmlEncode(Me.txtTaikyoYoskf3030Sc002SharedService.convertYenFormatteibi.Text.Replace(Constant.Sign.SLASH, String.Empty).Replace(Constant.Sign.UNDER_SCORE, String.Empty))
					registDto.setHdnChangeBeforeTaikyoYoteibi(skf3030Sc002SharedService.getDateText(registDto.getSc006TaikyoYoteiDay()));
					//役員算定と変更前役員算定（hidden）比較
					//Me.hdnChangeBeforeYakuinSantei.Value = Me.ddlYakuinSantei.SelectedValue
					registDto.setHdnChangeBeforeYakuinSantei(registDto.getSc006YakuinSanteiSelect());
					//社宅使用料調整金額と変更前社宅使用料調整金額（hidden）比較
					//Me.hdnChangeBeforeShatakuShiyoryoChoseiKingaku.Value = GetPayText(Me.txtShatakuShiyoryoChoseiKingaku.Text)
					registDto.setHdnChangeBeforeShatakuShiyoryoChoseiKingaku(skf3030Sc002SharedService.getPayText(registDto.getSc006SiyoroTyoseiPay()));
					//個人負担共益費月額と変更前個人負担共益費月額（hidden）比較
					//Me.hdnChangeBeforeKojinFutanKyoekihiGetsugaku.Value = GetPayText(Me.txtKojinFutanKyoekihiGetsugaku.Text)
					registDto.setHdnChangeBeforeKojinFutanKyoekihiGetsugaku(skf3030Sc002SharedService.getPayText(registDto.getSc006KyoekihiMonthPay()));
					//個人負担共益費調整金額と変更前個人負担共益費調整金額（hidden）比較
					//Me.hdnChangeBeforeKojinFutanKyoekihiChoseiKingaku.Value = GetPayText(Me.txtKojinFutanKyoekihiChoseiKingaku.Text)
					registDto.setHdnChangeBeforeKojinFutanKyoekihiChoseiKingaku(skf3030Sc002SharedService.getPayText(registDto.getSc006KyoekihiTyoseiPay()));
					//区画１駐車場管理番号（hidden）と変更前区画１駐車場管理番号（hidden）比較
					//Me.hdnChangeBeforeChushajoKanriNo1.Value = Me.hdnChushajoKanriNo1.Value
					registDto.setHdnChangeBeforeChushajoKanriNo1(registDto.getHdnChushajoKanriNo1());
					//区画１利用開始日と変更前区画１利用開始日（hidden）比較
					//Me.hdnChangeBeforeKukaku1RiyoKaishibi.Value = HttpUtility.HtmlEncode(Me.txtKukaku1RiyoKaishibi.Text.Replace(Constant.Sign.SLASH, String.Empty).Replace(Constant.Sign.UNDER_SCORE, String.Empty))
					registDto.setHdnChangeBeforeKukaku1RiyoKaishibi(skf3030Sc002SharedService.getDateText(registDto.getSc006RiyouStartDayOne()));
					//区画１利用終了日と変更前区画１利用終了日（hidden）比較
					//Me.hdnChangeBeforeKukaku1RiyoShuryobi.Value = HttpUtility.HtmlEncode(Me.txtKukaku1RiyoShuryobi.Text.Replace(Constant.Sign.SLASH, String.Empty).Replace(Constant.Sign.UNDER_SCORE, String.Empty))
					registDto.setHdnChangeBeforeKukaku1RiyoShuryobi(skf3030Sc002SharedService.getDateText(registDto.getSc006RiyouEndDayOne()));
					//区画２駐車場管理番号（hidden）と変更前区画２駐車場管理番号（hidden）比較
					//Me.hdnChangeBeforeChushajoKanriNo2.Value = Me.hdnChushajoKanriNo2.Value
					registDto.setHdnChangeBeforeChushajoKanriNo2(registDto.getHdnChushajoKanriNo2());
					//区画２利用開始日と変更前区画２利用開始日（hidden）比較
					//Me.hdnChangeBeforeKukaku2RiyoKaishibi.Value = HttpUtility.HtmlEncode(Me.txtKukaku2RiyoKaishibi.Text.Replace(Constant.Sign.SLASH, String.Empty).Replace(Constant.Sign.UNDER_SCORE, String.Empty))
					registDto.setHdnChangeBeforeKukaku2RiyoKaishibi(skf3030Sc002SharedService.getDateText(registDto.getSc006RiyouStartDayTwo()));
					//区画２利用終了日と変更前区画２利用終了日（hidden）比較
					//Me.hdnChangeBeforeKukaku2RiyoShuryobi.Value = HttpUtility.HtmlEncode(Me.txtKukaku2RiyoShuryobi.Text.Replace(Constant.Sign.SLASH, String.Empty).Replace(Constant.Sign.UNDER_SCORE, String.Empty))
					registDto.setHdnChangeBeforeKukaku2RiyoShuryobi(skf3030Sc002SharedService.getDateText(registDto.getSc006RiyouEndDayTwo()));
					//開始日(hidden)
					//Me.hdnChangeBeforeKaishibi.Value = HttpUtility.HtmlEncode(Me.txtKaishibi.Text.Replace(Constant.Sign.SLASH, String.Empty).Replace(Constant.Sign.UNDER_SCORE, String.Empty))
					registDto.setHdnChangeBeforeKaishibi(skf3030Sc002SharedService.getDateText(registDto.getSc006StartDay()));
					//終了日(hidden)
					//Me.hdnChangeBeforeShuryobi.Value = HttpUtility.HtmlEncode(Me.txtShuryobi.Text.Replace(Constant.Sign.SLASH, String.Empty).Replace(Constant.Sign.UNDER_SCORE, String.Empty))
					registDto.setHdnChangeBeforeShuryobi(skf3030Sc002SharedService.getDateText(registDto.getSc006EndDay()));
					//貸与日(hidden)
					//Me.hdnChangeBeforeTaiyobi.Value = HttpUtility.HtmlEncode(Me.txtBihinTaiyobi.Text.Replace(Constant.Sign.SLASH, String.Empty).Replace(Constant.Sign.UNDER_SCORE, String.Empty))
					registDto.setHdnChangeBeforeTaiyobi(skf3030Sc002SharedService.getDateText(registDto.getSc006TaiyoDay()));
					//返却日(hidden)
					//Me.hdnChangeBeforeHenkyakubi.Value = HttpUtility.HtmlEncode(Me.txtBihinHenkyakubi.Text.Replace(Constant.Sign.SLASH, String.Empty).Replace(Constant.Sign.UNDER_SCORE, String.Empty))
					registDto.setHdnChangeBeforeHenkyakubi(skf3030Sc002SharedService.getDateText(registDto.getSc006HenkyakuDay()));

					//社宅使用料日割
					//Me.litShatakuShiyoryoHiwariKingaku.Text = StringUtil.ConvertYenFormat(Integer.Parse(Me.hdnKaiSanAfterShatakuShiyoryoHiwariKingaku.Value), False)
					registDto.setSc006SiyoryoHiwariPay(skf3030Sc002SharedService.convertYenFormat(Integer.parseInt(registDto.getHdnKaiSanAfterShatakuShiyoryoHiwariKingaku()), false));
					//社宅使用料月額(調整後)
					//Me.litShatakuShiyoryoGetsugakuChoseigo.Text = StringUtil.ConvertYenFormat(Integer.Parse(Me.hdnKaiSanAfterShatakuShiyoryoGetsugakuChoseigo.Value), False)
					registDto.setSc006SyatauMonthPayAfter(skf3030Sc002SharedService.convertYenFormat(Integer.parseInt(registDto.getHdnKaiSanAfterShatakuShiyoryoGetsugakuChoseigo()), false));
					//個人負担共益費(調整後)
					//Me.litKojinFutanKyoekihiGetsugakuChoseigo.Text = StringUtil.ConvertYenFormat(Integer.Parse(Me.hdnKaiSanAfterKojinFutanKyoekihiGetsugakuChoseigo.Value), False)
					registDto.setSc006KyoekihiPayAfter(skf3030Sc002SharedService.convertYenFormat(Integer.parseInt(registDto.getHdnKaiSanAfterKojinFutanKyoekihiGetsugakuChoseigo()), false));
					//駐車場使用料(日割)
					//Me.litKukaku1ChushajoShiyoroHiwariKingaku.Text = StringUtil.ConvertYenFormat(Integer.Parse(Me.hdnKaiSanAfterKukaku1ChushajoShiyoroHiwariKingaku.Value), False)
					//Me.litKukaku2ChushajoShiyoroHiwariKingaku.Text = StringUtil.ConvertYenFormat(Integer.Parse(Me.hdnKaiSanAfterKukaku2ChushajoShiyoroHiwariKingaku.Value), False)
					registDto.setSc006TyusyaDayPayOne(skf3030Sc002SharedService.convertYenFormat(Integer.parseInt(registDto.getHdnKaiSanAfterKukaku1ChushajoShiyoroHiwariKingaku()), false));
					registDto.setSc006TyusyaDayPayTwo(skf3030Sc002SharedService.convertYenFormat(Integer.parseInt(registDto.getHdnKaiSanAfterKukaku2ChushajoShiyoroHiwariKingaku()), false));
					//駐車場使用料(調整後)
					//Me.litChushajoShiyoryoGetsugakuChoseigo.Text = StringUtil.ConvertYenFormat(Integer.Parse(Me.hdnKaiSanAfterChushajoShiyoryoGetsugakuChoseigo.Value), False)
					registDto.setSc006TyusyaMonthPayAfter(skf3030Sc002SharedService.convertYenFormat(Integer.parseInt(registDto.getHdnKaiSanAfterChushajoShiyoryoGetsugakuChoseigo()), false));
					//更新成功時は備品の再読み込みを実施
					registDto.setBihinItiranReloadFlg(false);
//				}
				//駐車場区画１に値が設定されていない場合、月別駐車場履歴から物理削除する
				//If String.IsNullOrEmpty(Me.litKukaku1No.Text) Then sc006KukakuNoOne
				if(SkfCheckUtils.isNullOrEmpty(registDto.getSc006KukakuNoOne())){
					skf3030Sc002SharedService.deleteParkingRirekiByLendNo(registDto.getHdnShatakuKanriId(), 
												"1",
												skfBaseBusinessLogicUtils.getSystemProcessNenGetsu());
				}
				if(SkfCheckUtils.isNullOrEmpty(registDto.getSc006KukakuNoTwo())){
					skf3030Sc002SharedService.deleteParkingRirekiByLendNo(registDto.getHdnShatakuKanriId(), 
												"2",
												skfBaseBusinessLogicUtils.getSystemProcessNenGetsu());
				}

			}

		}
		//メッセージ設定
//		If Not String.IsNullOrEmpty(message) Then
//		    //メッセージを表示
//		    Me.hdnFieldMessage.Value() = HttpUtility.HtmlEncode(message)
//		End If
		// ドロップダウンリスト作成
		skf3030Sc002SharedService.setDdlControlValues(
				registDto.getSc006KyojyusyaKbnSelect(), sc006KyojyusyaKbnSelectList,
				registDto.getSc006YakuinSanteiSelect(), sc006YakuinSanteiSelectList,
				registDto.getSc006KyoekihiPayMonthSelect(), sc006KyoekihiPayMonthSelectList,
				registDto.getSc006KibouTimeInSelect(), sc006KibouTimeInSelectList,
				registDto.getSc006KibouTimeOutSelect(), sc006KibouTimeOutSelectList,
				registDto.getSc006SogoRyojokyoSelect(), sc006SogoRyojokyoSelectList,
				registDto.getSc006SogoHanteiKbnSelect(), sc006SogoHanteiKbnSelectList,
				registDto.getSc006SokinShatakuSelect(), sc006SokinShatakuSelectList,
				registDto.getSc006SokinKyoekihiSelect(), sc006SokinKyoekihiSelectList,
				registDto.getSc006OldKaisyaNameSelect(), sc006OldKaisyaNameSelectList,
				registDto.getSc006KyuyoKaisyaSelect(), sc006KyuyoKaisyaSelectList,
				registDto.getSc006HaizokuKaisyaSelect(), sc006HaizokuKaisyaSelectList,
				registDto.getSc006TaiyoKaisyaSelect(), sc006TaiyoKaisyaSelectList,
				registDto.getSc006KariukeKaisyaSelect(), sc006KariukeKaisyaSelectList);

		// ドロップダウンリスト設定
		registDto.setSc006KyojyusyaKbnSelectList(sc006KyojyusyaKbnSelectList);
		registDto.setSc006YakuinSanteiSelectList(sc006YakuinSanteiSelectList);
		registDto.setSc006KyoekihiPayMonthSelectList(sc006KyoekihiPayMonthSelectList);
		registDto.setSc006KibouTimeInSelectList(sc006KibouTimeInSelectList);
		registDto.setSc006KibouTimeOutSelectList(sc006KibouTimeOutSelectList);
		registDto.setSc006SogoRyojokyoSelectList(sc006SogoRyojokyoSelectList);
		registDto.setSc006SogoHanteiKbnSelectList(sc006SogoHanteiKbnSelectList);
		registDto.setSc006SokinShatakuSelectList(sc006SokinShatakuSelectList);
		registDto.setSc006SokinKyoekihiSelectList(sc006SokinKyoekihiSelectList);
		registDto.setSc006OldKaisyaNameSelectList(sc006OldKaisyaNameSelectList);
		registDto.setSc006KyuyoKaisyaSelectList(sc006KyuyoKaisyaSelectList);
		registDto.setSc006HaizokuKaisyaSelectList(sc006HaizokuKaisyaSelectList);
		registDto.setSc006TaiyoKaisyaSelectList(sc006TaiyoKaisyaSelectList);
		registDto.setSc006KariukeKaisyaSelectList(sc006KariukeKaisyaSelectList);
		//相互利用状況の活性非活性を設定
		skf3030Sc002SharedService.setControlStatusSogoRiyo(registDto);
		
		//Page_LoadComplete
		skf3030Sc002SharedService.setBihinListPageLoadComplete(registDto);
		
		
		return registDto;
	}
	
	//登録処理クラス(トランザクション部分)
	@Transactional
	private String registClickInsert(Skf3030Sc002RegistDto registDto,
			String rentalPatternUpdateDate,
			Date rentalPatternUpdateDateForRegist,
			Map<Skf3030Sc002CommonDto.RENTAL_PATTERN, String> rentalPatternTorokuList,
			Long teijiNo){
		
		String shatakuKanriId = CodeConstant.DOUBLE_QUOTATION;
		
		Map<String,Object> returnMap = insertData(
				skf3030Sc002SharedService.getTbtTeijiDataColumnInfoList(teijiNo,registDto), 
				skf3030Sc002SharedService.getTbtPublicShatakuLedgerDataColumnInfoList(registDto),
				registDto.getHdnShatakuKanriNo(),
				registDto.getHdnShatakuRoomKanriNo(),
				registDto.getHdnChushajoKanriNo1(),
				registDto.getHdnChushajoKanriNo2(),
				registDto.getSc006RiyouStartDayOne(),
				registDto.getSc006RiyouStartDayTwo(),
				skf3030Sc002SharedService.setBihinData(registDto,teijiNo),
				skf3030Sc002SharedService.getTbtPublicShatakuParkingBlockDataColumnInfoList(1, false,registDto),
				skf3030Sc002SharedService.getTbtPublicShatakuParkingBlockDataColumnInfoList(2, false,registDto),
				CodeConstant.C001,
				LoginUserInfoUtils.getUserCd(),
				registDto.getPageId(),
				registDto.getSc006TaiyoDay(),
				rentalPatternUpdateDate,
				rentalPatternUpdateDateForRegist,
				rentalPatternTorokuList);
		int returnCount = Integer.parseInt(returnMap.get("returnCount").toString());
		shatakuKanriId = returnMap.get("shatakuKanriId").toString();

		if(returnCount == 0){
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1073);
				throwBusinessExceptionIfErrors(registDto.getResultMessages());
		}else if(returnCount == -1){
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1076);
				throwBusinessExceptionIfErrors(registDto.getResultMessages());
		}else if(returnCount == -2){
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.W_SKF_1010);
				throwBusinessExceptionIfErrors(registDto.getResultMessages());
		}else{
			ServiceHelper.addResultMessage(registDto, MessageIdConstant.I_SKF_1012);
		}
		
		return shatakuKanriId;
	}
	
	@Transactional
	private void registClickUpdate(Skf3030Sc002RegistDto registDto,
			String taikyoFlg,
			Boolean changeFlg1,
			Boolean changeFlg2,
			Boolean changeFlg3,
			Boolean changeFlg4,
			String rentalPatternUpdateDate,
			Date rentalPatternUpdateDateForRegist,
			Map<Skf3030Sc002CommonDto.RENTAL_PATTERN, String> rentalPatternTorokuList){
	
			int returnCount = updateData(
					skf3030Sc002SharedService.getTbtPublicShatakuLedgerDataColumnInfoList(registDto),
					skf3030Sc002SharedService.getTbtPublicShatakuBihinDataColumnInfoList(registDto),
					skf3030Sc002SharedService.getTbtPublicShatakuMutualDataColumnInfoList(registDto),
					skf3030Sc002SharedService.getTbtPublicShatakuRentalRirekiDataColumnInfoList(registDto),
					skf3030Sc002SharedService.setBihinData(registDto,0L),
					skf3030Sc002SharedService.setBihinData(registDto),
					skf3030Sc002SharedService.getTbtPublicParkingRirekiDataColumnInfoList(1, true, registDto),
					skf3030Sc002SharedService.getTbtPublicParkingRirekiDataColumnInfoList(1, false, registDto),
					changeFlg3,
					skf3030Sc002SharedService.getDateText(registDto.getSc006RiyouStartDayOne()), 
					skf3030Sc002SharedService.getTbtPublicShatakuParkingBlockDataColumnInfoList(1, false, registDto),
					changeFlg1,
					skf3030Sc002SharedService.getTbtPublicShatakuParkingBlockDataColumnInfoList(1, true, registDto),
					skf3030Sc002SharedService.getTbtPublicParkingRirekiDataColumnInfoList(2, true, registDto),
					skf3030Sc002SharedService.getTbtPublicParkingRirekiDataColumnInfoList(2, false, registDto),
					changeFlg4,
					skf3030Sc002SharedService.getDateText(registDto.getSc006RiyouStartDayTwo()),
					skf3030Sc002SharedService.getTbtPublicShatakuParkingBlockDataColumnInfoList(2, false, registDto),
					changeFlg2,
					skf3030Sc002SharedService.getTbtPublicShatakuParkingBlockDataColumnInfoList(2, true, registDto),
					skf3030Sc002SharedService.getTbtPublicShatakuMutualRirekiDataColumnInfoList(registDto),
					skf3030Sc002SharedService.getTbtPublicShatakuRoomDataColumnInfoList(taikyoFlg,registDto),
					skf3030Sc002SharedService.getTbtPublicShatakuRoomBihinDataColumnInfoList(registDto),
					registDto.getHdnShatakuKanriId(),
					registDto.getHdnNengetsu(),
					LoginUserInfoUtils.getUserCd(),
					registDto.getPageId(),
					rentalPatternUpdateDate, 
					rentalPatternUpdateDateForRegist,
					rentalPatternTorokuList);
		if(returnCount == 0){
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1075);
			throwBusinessExceptionIfErrors(registDto.getResultMessages());
		}else if(returnCount == -1){
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.W_SKF_1009);
			throwBusinessExceptionIfErrors(registDto.getResultMessages());
		}else{
			//更新完了メッセージ
			ServiceHelper.addResultMessage(registDto, MessageIdConstant.I_SKF_1011);
		}
	}
	
	/**
	 * 駐車場利用開始日チェック
	 * @param dto
	 * @return True:正常 False:エラー
	 */
	private Boolean checkRiyouStartDay(Skf3030Sc002RegistDto dto){
		
		Boolean isRiyouStartDayError = false;
		dto.setNextTabIndex("");
		
		//hdnEndDay1に値が設定されていない場合、月別駐車場履歴より駐車場利用終了日の最大値を取得し設定する
		if(SkfCheckUtils.isNullOrEmpty(dto.getHdnEndDayOne()) && 
				!SkfCheckUtils.isNullOrEmpty(dto.getHdnChushajoKanriNo1()) &&
				!SkfCheckUtils.isNullOrEmpty(dto.getHdnShatakuKanriId()) ){
			//区画１最大利用終了日（hidden）
			dto.setHdnEndDayOne(skf3030Sc002SharedService.getMaxParkingEndDay(dto.getHdnChushajoKanriNo1(),
					dto.getHdnShatakuKanriNo(),dto.getHdnShatakuKanriId()));
		}

		//チェックする処理がコメントアウトされており、実行する意味が無いためコメントアウト
//		if(!SkfCheckUtils.isNullOrEmpty(skf3030Sc002SharedService.getDateText(dto.getSc006RiyouStartDayOne())) &&
//				!SkfCheckUtils.isNullOrEmpty(dto.getHdnEndDayOne())){
//			//駐車場１ 利用開始日
//			Dim sDateOne As DateTime = DateUtil.ConbersionFomatStringToDate( _
//						    Me.GetDateText(Me.txtKukaku1RiyoKaishibi.Text))
//			//駐車場１ 終了日
//			Dim eDateOne As DateTime = DateUtil.ConbersionFomatStringToDate(Me.hdnEndDay1.Value)
//
//			//駐車場１ 利用開始日 < 駐車場１ 終了日の場合
//		    Me.txtKukaku1RiyoKaishibi.BackColor() = Color.Empty
//		}


		//hdnEndDay2に値が設定されていない場合、月別駐車場履歴より駐車場利用終了日の最大値を取得し設定する
		if(SkfCheckUtils.isNullOrEmpty(dto.getHdnEndDayTwo()) && 
				!SkfCheckUtils.isNullOrEmpty(dto.getHdnChushajoKanriNo2()) &&
				!SkfCheckUtils.isNullOrEmpty(dto.getHdnShatakuKanriId()) ){
			//区画２最大利用終了日（hidden）
			dto.setHdnEndDayTwo(skf3030Sc002SharedService.getMaxParkingEndDay(dto.getHdnChushajoKanriNo2(),
					dto.getHdnShatakuKanriNo(),dto.getHdnShatakuKanriId()));
		}

//		If Not String.IsNullOrEmpty(Me.GetDateText(Me.txtKukaku2RiyoKaishibi.Text)) AndAlso _
//		    Not String.IsNullOrEmpty(Me.hdnEndDay2.Value) Then
//		    //駐車場２ 利用開始日
//		    Dim sDateTwo As DateTime = DateUtil.ConbersionFomatStringToDate( _
//						    Me.GetDateText(Me.txtKukaku2RiyoKaishibi.Text))
//		    //駐車場２ 終了日
//		    Dim eDateTwo As DateTime = DateUtil.ConbersionFomatStringToDate(Me.hdnEndDay2.Value)
//
//		    //駐車場２ 利用開始日 < 駐車場２ 終了日の場合
//		    Me.txtKukaku2RiyoKaishibi.BackColor() = Color.Empty
//		End If

		//１つでもエラーフラグがTrueの場合、処理を終了
//		If isRiyouStartDayError Then
//		    Me.hdnFieldMessage.Value = HttpUtility.HtmlEncode(message.ToString())
//		    If Not nextTabIndex Is Nothing Then
//			Me.tbcNyutaikyoInfo.ActiveTabIndex = CInt(nextTabIndex)
//		    End If
//		End If
		
		return isRiyouStartDayError;
	}
	
	
	/**
	 * 登録処理メソッド
	 * @param columnInfoList 提示データ
	 * @param shatakuLedgercolumnInfoList　
	 * @param shatakuKanriNo 社宅管理番号
	 * @param shatakuRoomKanriNo 部屋管理番号
	 * @param chushajoKanriNo1 駐車場管理番号1
	 * @param chushajoKanriNo2 駐車場管理番号2
	 * @param Kukaku1RiyoKaishibi 区画１の利用開始日
	 * @param Kukaku2RiyoKaishibi 区画２の利用開始日
	 * @param teijiBihinDataDt 提示備品データ
	 * @param shatakuParkingBlockDataColumnInfoList1 社宅駐車場区画マスタ情報1
	 * @param shatakuParkingBlockDataColumnInfoList2 社宅駐車場区画マスタ情報2
	 * @param companyCd 会社コード
	 * @param userId ユーザID
	 * @param ipAddress IPアドレス →　pageId 機能ID
	 * @param bihinTaiyobi 備品貸与日
	 * @param rentalPatternUpdateDate 使用料パターン排他処理用更新日時
	 * @param rentalPatternUpdateDateForRegist >使用料パターン更新日時
	 * @param rentalPatternTorokuList 使用料パターン登録項目リスト
	 * @return 結果　0：正常、0：登録エラー、-1：排他エラー + 社宅管理ID：shatakuKanriId
	 */
	private Map<String,Object> insertData(Skf3022TTeijiData columnInfoList, 
			Skf3030TShatakuLedger shatakuLedgercolumnInfoList,
			String shatakuKanriNo,
			String shatakuRoomKanriNo,
			String chushajoKanriNo1,
			String chushajoKanriNo2,
			String Kukaku1RiyoKaishibi,
			String Kukaku2RiyoKaishibi,
			List<Map<String,Object>> teijiBihinDataDt,
			Skf3010MShatakuParkingBlock shatakuParkingBlockDataColumnInfoList1,
			Skf3010MShatakuParkingBlock shatakuParkingBlockDataColumnInfoList2,
			String companyCd,
			String userId,
			String pageId,
			String bihinTaiyobi,
			//ByRef shatakuKanriId,→Mapで返す
			String rentalPatternUpdateDate,
			Date rentalPatternUpdateDateForRegist,
			Map<Skf3030Sc002CommonDto.RENTAL_PATTERN, String> rentalPatternTorokuList
			){
		
		//int selectCount = 0;//未使用
		int insertCount = 0;
		int updateCount = 0;
		int returnCount = 0;
		Map<String,Object> resultMap = new HashMap<String,Object>();
		String shatakuKanriId = CodeConstant.DOUBLE_QUOTATION;
		
		
		//既に該当社宅が“貸与中”になっている場合、排他エラーとする
		if(CodeConstant.LEND_JOKYO_TAIYOCHU.equals(skf3030Sc002SharedService.getRoomLendJokyo(
				Long.parseLong(shatakuKanriNo), Long.parseLong(shatakuRoomKanriNo)))){
			resultMap.put("returnCount", -2);
			resultMap.put("shatakuKanriId",shatakuKanriId);
			return resultMap;
		}
		
		Skf3022TTeijiData selectTeijiData = skf3022TTeijiDataRepository.selectByPrimaryKey(columnInfoList.getTeijiNo());
		//提示データ重複チェック
		if(selectTeijiData == null){
			//提示番号重複なしの場合、インサート処理
			insertCount = skf3022TTeijiDataRepository.insertSelective(columnInfoList);
			if(insertCount == 0){
				resultMap.put("returnCount", 0);
				resultMap.put("shatakuKanriId",shatakuKanriId);
				return resultMap;
			}else{
				returnCount += 1;
			}
		}
		
		//社宅部屋情報マスタを更新
		Skf3010MShatakuRoomKey shatakuRoomKey = new Skf3010MShatakuRoomKey();
		shatakuRoomKey.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
		shatakuRoomKey.setShatakuRoomKanriNo(Long.parseLong(shatakuRoomKanriNo));
		Skf3010MShatakuRoom selectShatakuRoom = skf3010MShatakuRoomRepository.selectByPrimaryKey(shatakuRoomKey);
		if(selectShatakuRoom != null){
			Skf3010MShatakuRoom updShatakuRoom = new Skf3010MShatakuRoom();
			updShatakuRoom.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
			updShatakuRoom.setShatakuRoomKanriNo(Long.parseLong(shatakuRoomKanriNo));
			updShatakuRoom.setLendJokyo("2");
			updateCount = skf3010MShatakuRoomRepository.updateByPrimaryKeySelective(updShatakuRoom);
			if(updateCount == 0){
				resultMap.put("returnCount", 0);
				resultMap.put("shatakuKanriId",shatakuKanriId);
				return resultMap;
			}else{
				returnCount += 1;
			}
		}
		
		//駐車場区画情報
		//区画１の利用開始日が入力されている場合、社宅駐車場区画マスタ更新
		if(!SkfCheckUtils.isNullOrEmpty(Kukaku1RiyoKaishibi)){
			Skf3010MShatakuParkingBlockKey parkBlockKey = new Skf3010MShatakuParkingBlockKey();
			parkBlockKey.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
			parkBlockKey.setParkingKanriNo(Long.parseLong(chushajoKanriNo1));
			Skf3010MShatakuParkingBlock selectParkingBlock = skf3010MShatakuParkingBlockRepository.selectByPrimaryKey(parkBlockKey);
			if(selectParkingBlock != null){
				shatakuParkingBlockDataColumnInfoList1.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
				shatakuParkingBlockDataColumnInfoList1.setParkingKanriNo(Long.parseLong(chushajoKanriNo1));
				updateCount = skf3010MShatakuParkingBlockRepository.updateByPrimaryKeySelective(shatakuParkingBlockDataColumnInfoList1);
				if(updateCount == 0){
					resultMap.put("returnCount", 0);
					resultMap.put("shatakuKanriId",shatakuKanriId);
					return resultMap;
				}else{
					returnCount += 1;
				}
			}
			selectParkingBlock = null;
		}
		
		//区画２の利用開始日が入力されている場合、社宅駐車場区画マスタ更新
		if(!SkfCheckUtils.isNullOrEmpty(Kukaku2RiyoKaishibi)){
			Skf3010MShatakuParkingBlockKey parkBlockKey = new Skf3010MShatakuParkingBlockKey();
			parkBlockKey.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
			parkBlockKey.setParkingKanriNo(Long.parseLong(chushajoKanriNo2));
			Skf3010MShatakuParkingBlock selectParkingBlock2 = skf3010MShatakuParkingBlockRepository.selectByPrimaryKey(parkBlockKey);
			if(selectParkingBlock2 != null){
				shatakuParkingBlockDataColumnInfoList2.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
				shatakuParkingBlockDataColumnInfoList2.setParkingKanriNo(Long.parseLong(chushajoKanriNo2));
				updateCount = skf3010MShatakuParkingBlockRepository.updateByPrimaryKeySelective(shatakuParkingBlockDataColumnInfoList2);
				if(updateCount == 0){
					resultMap.put("returnCount", 0);
					resultMap.put("shatakuKanriId",shatakuKanriId);
					return resultMap;
				}else{
					returnCount += 1;
				}
			}
			selectParkingBlock2 = null;
		}
		
		//提示備品データテーブルにデータ登録
		for(Map<String,Object> dr : teijiBihinDataDt){
			Skf3022TTeijiBihinDataKey teijiBihinKey = new Skf3022TTeijiBihinDataKey();
			teijiBihinKey.setTeijiNo(Long.parseLong(dr.get("teiji_no").toString()));
			teijiBihinKey.setBihinCd(dr.get("bihin_cd").toString());
			Skf3022TTeijiBihinData selectTeijiBihin = skf3022TTeijiBihinDataRepository.selectByPrimaryKey(teijiBihinKey);
			if(selectTeijiBihin == null){
				Skf3022TTeijiBihinData teijiRecord = new Skf3022TTeijiBihinData();
				teijiRecord.setTeijiNo(Long.parseLong(dr.get("teiji_no").toString()));
				teijiRecord.setBihinCd(dr.get("bihin_cd").toString());
				teijiRecord.setBihinLentStatusKbn(dr.get("bihin_lent_status_kbn").toString());

				insertCount = skf3022TTeijiBihinDataRepository.insertSelective(teijiRecord);
				if(insertCount == 0){
					resultMap.put("returnCount", 0);
					resultMap.put("shatakuKanriId",shatakuKanriId);
					return resultMap;
				}else{
					returnCount += 1;
				}
			}
		}
		
		//システム処理年月を取得する。
		String sysShoriNenGetsu = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu();
		
		Date systemDate = skfBaseBusinessLogicUtils.getSystemDateTime();
		Long shatakuId = 0L;
		
		//「社宅管理台帳データ登録（社宅情報）」バッチ処理を起動する
		shatakuId = skfPageBusinessLogicUtils.updateShatakuKanriDaichoShatakuData(columnInfoList.getTeijiNo(),
				false, "", sysShoriNenGetsu, userId, pageId, systemDate);
		if(shatakuId == null){
			resultMap.put("returnCount", 0);
			resultMap.put("shatakuKanriId",shatakuKanriId);
			return resultMap;
		}
		
		//備品情報タブの「貸与日」が入力されている場合、「社宅基本台帳データ更新（備品）」バッチ処理を起動する
		if(!SkfCheckUtils.isNullOrEmpty(bihinTaiyobi)){
			//社宅管理台帳備品データの登録
			int result = skfPageBusinessLogicUtils.updateShatakuKanriDaichoBihinData(columnInfoList.getTeijiNo(),
					shatakuId, sysShoriNenGetsu, userId, pageId, systemDate);
			if(result !=  CodeConstant.SYS_OK){
				resultMap.put("returnCount", 0);
				resultMap.put("shatakuKanriId",shatakuKanriId);
				return resultMap;
			}
		}
		
		//提示データの社宅管理台帳IDを更新
		Skf3030Sc002UpdateTeijiDataExpParameter updTeijiParam = new Skf3030Sc002UpdateTeijiDataExpParameter();
		updTeijiParam.setTeijiNo(columnInfoList.getTeijiNo());
		updTeijiParam.setUpdateDate(systemDate);
		updTeijiParam.setUpdateUserId(userId);
		updTeijiParam.setUpdateProgramId(pageId);
		//int updCountTJ2 = //戻り値見てない
		skf3030Sc002UpdateTeijiDataExpRepository.updateTeijiData(updTeijiParam);
		
		Long shatakuKanriIdL = skf3030Sc002SharedService.getShatakuKannriIdFromTeijiData(columnInfoList.getTeijiNo());
		shatakuLedgercolumnInfoList.setShatakuKanriId(shatakuKanriIdL);
		shatakuKanriId = skf3030Sc002SharedService.getToString(shatakuKanriIdL);
		//提示データから社宅管理ID、社宅管理No、社宅部屋管理No を削除。
		skf3030Sc002UpdateTeijiDataExpRepository.updateTeijiDataForShatakuId(updTeijiParam);
		
		//「社宅管理台帳基本テーブル」のデータ更新
		Skf3030TShatakuLedger selectSL = skf3030TShatakuLedgerRepository.selectByPrimaryKey(shatakuLedgercolumnInfoList.getShatakuKanriId());
		if(selectSL != null){
			updateCount = skf3030Sc002UpdateShatakuKanriDaichoExpRepository.updateShatakuKanriDaicho(shatakuLedgercolumnInfoList);
			if(updateCount <= 0){
				resultMap.put("returnCount", 0);
				resultMap.put("shatakuKanriId",shatakuKanriId);
				return resultMap;
			}else{
				returnCount += 1;
			}
		}
		
		
		//使用料パターンテーブル登録・更新
		if(CodeConstant.STRING_ZERO.equals(rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.UPDATE_KIND))){
			//登録処理
			if(SkfCheckUtils.isNullOrEmpty(rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.SHATAKU_GETSUGAKU))){
				//データの登録（使用料パターン未計算）
				Skf3030TRentalPattern patternRecord = new Skf3030TRentalPattern();
				// 社宅管理番号
				patternRecord.setShatakuKanriNo(Long.parseLong(
						rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.SHATAKUKANRI_NO)));
				// 使用料パターンID
				patternRecord.setRentalPatternId(Long.parseLong(
						rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.RENTAL_PATTERNID)));
				insertCount = skf3030TRentalPatternRepository.insertSelective(patternRecord);
			}else{
				//データの登録（使用料パターン計算済）
				Skf3030TRentalPattern patternRecord = new Skf3030TRentalPattern();
				// 社宅管理番号
				patternRecord.setShatakuKanriNo(Long.parseLong(
						rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.SHATAKUKANRI_NO)));
				// 使用料パターンID
				patternRecord.setRentalPatternId(Long.parseLong(
						rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.RENTAL_PATTERNID)));
				// 使用料パターン名
				patternRecord.setRentalPatternName(
						rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.PATTERN_NAME));
				// 規格
				patternRecord.setKikaku(
						rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.KIKAKU));
				// 基準使用料算定上延べ面積
				patternRecord.setBaseCalcMenseki(new BigDecimal(
						rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.KIJUN_MENSEKI)));
				// 社宅使用料算定上延べ面積
				patternRecord.setShatakuCalcMenseki(Short.parseShort(
						rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.SHATAKU_MENSEKI)));
				// 経年残価率
				patternRecord.setAgingResidualRate(new BigDecimal(
						rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.KEINEN_ZANKARITSU)));
				// 用途
				patternRecord.setAuse(
						rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.YOTO));
				// 経年
				patternRecord.setAging(Short.parseShort(
						rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.KEINEN)));
				// 基本使用料
				patternRecord.setBaseRental(Integer.parseInt(
						rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.KIHON_SHIYORYO)));
				// 単価
				patternRecord.setPrice(new BigDecimal(
						rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.TANKA)));
				// 社宅使用料月額
				patternRecord.setRental(Integer.parseInt(
						rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.SHATAKU_GETSUGAKU)));
				// 延べ面積
				patternRecord.setMenseki(new BigDecimal(
						rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.NOBE_MENSEKI)));
				// サンルーム面積
				patternRecord.setSunRoomMenseki(new BigDecimal(
						rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.SUNROOM_MENSEKI)));
				// 階段面積
				patternRecord.setStairsMenseki(new BigDecimal(
						rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.KAIDAN_MENSEKI)));
				// 物置面積
				patternRecord.setBarnMenseki(new BigDecimal(
						rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.MONOOKI_MENSEKI)));
				insertCount = skf3030TRentalPatternRepository.insertSelective(patternRecord);
			}
			if(insertCount == 0){
				resultMap.put("returnCount", 0);
				resultMap.put("shatakuKanriId",shatakuKanriId);
				return resultMap;
			}else{
				returnCount += 1;
			}
		}else{
			//更新処理
			Skf3022Sc006GetRentalPatternForUpdateExpParameter param = new Skf3022Sc006GetRentalPatternForUpdateExpParameter();
			param.setRentalPatternId(Long.parseLong(
					rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.RENTAL_PATTERNID)));
			param.setUpdateDate(rentalPatternUpdateDate);
			List<Skf3022Sc006GetRentalPatternForUpdateExp> targetDt = new ArrayList<Skf3022Sc006GetRentalPatternForUpdateExp>();
			targetDt = skf3022Sc006UpdateRentalPatternExpRepository.getRentalPatternForUpdate(param);
			if (targetDt.size() > 0) {
				if(SkfCheckUtils.isNullOrEmpty(rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.SHATAKU_GETSUGAKU))){
					//データの更新（使用料パターン未計算）
					Skf3030TRentalPattern record = new Skf3030TRentalPattern();
					// 社宅管理番号
					record.setShatakuKanriNo(Long.parseLong(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.SHATAKUKANRI_NO)));
					// 使用料パターンID
					record.setRentalPatternId(Long.parseLong(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.RENTAL_PATTERNID)));
					// 使用料パターン名
					record.setRentalPatternName(null);
					// 規格
					record.setKikaku(null);
					// 基準使用料算定上延べ面積
					record.setBaseCalcMenseki(null);
					// 社宅使用料算定上延べ面積
					record.setShatakuCalcMenseki(null);
					// 経年残価率
					record.setAgingResidualRate(null);
					// 用途
					record.setAuse(null);
					// 経年
					record.setAging(null);
					// 基本使用料
					record.setBaseRental(null);
					// 単価
					record.setPrice(null);
					// 社宅使用料月額
					record.setRental(null);
					// 更新日時
					record.setUpdateDate(rentalPatternUpdateDateForRegist);
					// 更新者ID
					record.setUpdateUserId(userId);
					// 更新機能ID
					record.setUpdateProgramId(pageId);
					// 延べ面積
					record.setMenseki(null);
					// サンルーム面積
					record.setSunRoomMenseki(null);
					// 階段面積
					record.setStairsMenseki(null);
					// 物置面積
					record.setBarnMenseki(null);
					// 更新
					updateCount = skf3022Sc006UpdateRentalPatternExpRepository.updateRentalPattern(record);
					
				}else{
					//データの更新（使用料パターン計算済）
					Skf3030TRentalPattern record = new Skf3030TRentalPattern();
					// 社宅管理番号
					record.setShatakuKanriNo(Long.parseLong(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.SHATAKUKANRI_NO)));
					// 使用料パターンID
					record.setRentalPatternId(Long.parseLong(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.RENTAL_PATTERNID)));
					// 使用料パターン名
					record.setRentalPatternName(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.PATTERN_NAME));
					// 規格
					record.setKikaku(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.KIKAKU));
					// 基準使用料算定上延べ面積
					record.setBaseCalcMenseki(new BigDecimal(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.KIJUN_MENSEKI)));
					// 社宅使用料算定上延べ面積
					record.setShatakuCalcMenseki(Short.parseShort(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.SHATAKU_MENSEKI)));
					// 経年残価率
					record.setAgingResidualRate(new BigDecimal(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.KEINEN_ZANKARITSU)));
					// 用途
					record.setAuse(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.YOTO));
					// 経年
					record.setAging(Short.parseShort(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.KEINEN)));
					// 基本使用料
					record.setBaseRental(Integer.parseInt(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.KIHON_SHIYORYO)));
					// 単価
					record.setPrice(new BigDecimal(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.TANKA)));
					// 社宅使用料月額
					record.setRental(Integer.parseInt(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.SHATAKU_GETSUGAKU)));
					// 更新日時
					record.setUpdateDate(rentalPatternUpdateDateForRegist);
					// 更新者ID
					record.setUpdateUserId(userId);
					// 更新機能ID
					record.setUpdateProgramId(pageId);
					// 延べ面積
					record.setMenseki(new BigDecimal(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.NOBE_MENSEKI)));
					// サンルーム面積
					record.setSunRoomMenseki(new BigDecimal(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.SUNROOM_MENSEKI)));
					// 階段面積
					record.setStairsMenseki(new BigDecimal(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.KAIDAN_MENSEKI)));
					// 物置面積
					record.setBarnMenseki(new BigDecimal(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.MONOOKI_MENSEKI)));
					// 更新
					updateCount = skf3022Sc006UpdateRentalPatternExpRepository.updateRentalPattern(record);
				}
				if(updateCount == 0){
					resultMap.put("returnCount", 0);
					resultMap.put("shatakuKanriId",shatakuKanriId);
					return resultMap;
				}else{
					returnCount += 1;
				}
			}else{
				resultMap.put("returnCount", -1);
				resultMap.put("shatakuKanriId",shatakuKanriId);
				return resultMap;
			}
		}
		
		
		resultMap.put("returnCount", returnCount);
		resultMap.put("shatakuKanriId", shatakuKanriId);
		return resultMap;
	}
	
	/**
	 * 登録処理メソッド
	 * @param shatakuLedgerDataColumnInfoList 社宅管理台帳基本テーブル情報
	 * @param shatakuBihinDataColumnInfoList 社宅管理台帳備品基本テーブル情報
	 * @param shatakuMutualDataColumnInfoList 社宅管理台帳相互利用基本テーブル情報
	 * @param shatakuRentalRirekiDataColumnInfoList 月別使用料履歴テーブル情報
	 * @param bihinDataDt 提示備品データ
	 * @param roombihinDataDt 社宅部屋備品データ
	 * @param parkingRirekiDataColumnInfoList1 月別駐車場履歴テーブル情報 区画１
	 * @param parkingRirekiDataInsertColumnInfoList1 
	 * @param changeFlg3
	 * @param kukaku1RiyoKaishibi 区画１の利用開始日
	 * @param shatakuParkingBlockDataColumnInfoList1 社宅駐車場区画マスタ情報 区画１
	 * @param changeFlg1 駐車場管理番号変更フラグ 区画１
	 * @param shatakuParkingBlockDataChangeColumnInfoList1 社宅駐車場区画マスタ情報 駐車場管理番号変更 区画１
	 * @param parkingRirekiDataColumnInfoList2 別駐車場履歴テーブル情報 区画２
	 * @param parkingRirekiDataInsertColumnInfoList2 
	 * @param changeFlg4
	 * @param kukaku2RiyoKaishibi 区画２の利用開始日
	 * @param shatakuParkingBlockDataColumnInfoList2 社宅駐車場区画マスタ情報 区画２
	 * @param changeFlg2 駐車場管理番号変更フラグ 区画２
	 * @param shatakuParkingBlockDataChangeColumnInfoList2 社宅駐車場区画マスタ情報 駐車場管理番号変更 区画２
	 * @param shatakuMutualRirekiDataColumnInfoList 月別相互利用履歴テーブル情報
	 * @param shatakuRoomDataColumnInfoList 社宅部屋情報マスタ情報
	 * @param shatakuRoomBihinDataColumnInfoList 社宅部屋備品情報マスタ情報
	 * @param shatakuKanriId 社宅管理台帳ID
	 * @param nengetsu 対象年月
	 * @param userId ユーザID
	 * @param pageId 機能ID
	 * @param rentalPatternUpdateDate 使用料パターン排他処理用更新日時
	 * @param rentalPatternUpdateDateForRegist 使用料パターン更新日時
	 * @param rentalPatternTorokuList 使用料パターン登録項目リスト
	 * @return 結果　件数：正常、0：登録エラー、-1：排他エラー
	 */
	private int updateData(
			Skf3030TShatakuLedger shatakuLedgerDataColumnInfoList,
			Skf3030TShatakuBihin shatakuBihinDataColumnInfoList,
			Skf3030TShatakuMutual shatakuMutualDataColumnInfoList,
			Skf3030TShatakuRentalRireki shatakuRentalRirekiDataColumnInfoList,
			List<Map<String,Object>> bihinDataDt,
			List<Map<String,Object>> roombihinDataDt,
			Skf3030TParkingRireki parkingRirekiDataColumnInfoList1,
			Skf3030TParkingRireki parkingRirekiDataInsertColumnInfoList1,
			Boolean changeFlg3,
			String kukaku1RiyoKaishibi,
			Skf3010MShatakuParkingBlock shatakuParkingBlockDataColumnInfoList1,
			Boolean changeFlg1,
			Skf3010MShatakuParkingBlock shatakuParkingBlockDataChangeColumnInfoList1,
			Skf3030TParkingRireki parkingRirekiDataColumnInfoList2,
			Skf3030TParkingRireki parkingRirekiDataInsertColumnInfoList2,
			Boolean changeFlg4,
			String kukaku2RiyoKaishibi,
			Skf3010MShatakuParkingBlock shatakuParkingBlockDataColumnInfoList2,
			Boolean changeFlg2,
			Skf3010MShatakuParkingBlock shatakuParkingBlockDataChangeColumnInfoList2,
			Skf3030TShatakuMutualRireki shatakuMutualRirekiDataColumnInfoList,
			Skf3010MShatakuRoom shatakuRoomDataColumnInfoList,
			Skf3010MShatakuRoomBihin shatakuRoomBihinDataColumnInfoList,
			String shatakuKanriId,
			String nengetsu,
			String userId,
			String pageId,
			String rentalPatternUpdateDate,
			Date rentalPatternUpdateDateForRegist,
			Map<Skf3030Sc002CommonDto.RENTAL_PATTERN, String> rentalPatternTorokuList
			){
		
		int insertCount = 0;
		int updateCount = 0;
		int returnCount = 0;
		
		//「社宅管理台帳基本テーブル」のデータ更新
		Skf3030TShatakuLedger selectSL = skf3030TShatakuLedgerRepository.selectByPrimaryKey(shatakuLedgerDataColumnInfoList.getShatakuKanriId());
		if(selectSL != null){
			updateCount = skf3030Sc002UpdateShatakuKanriDaichoExpRepository.updateShatakuKanriDaicho(shatakuLedgerDataColumnInfoList);
			if(updateCount == 0){
				return 0;
			}else{
				returnCount += 1;
			}
		}
		
		//「社宅管理台帳備品基本テーブル」のデータ更新
		Skf3030TShatakuBihin selectShatakuBihin = skf3030TShatakuBihinRepository.selectByPrimaryKey(shatakuBihinDataColumnInfoList.getShatakuKanriId());
		if(selectShatakuBihin != null){
			updateCount = 
					skf3030Sc002UpdateShatakuKanriDaichoBihinExpRepository.updateShatakuKanriDaichoBihin(shatakuBihinDataColumnInfoList);
			if(updateCount == 0){
				return 0;
			}else{
				returnCount += 1;
			}
		}
				
		//「社宅管理台帳相互利用基本テーブル」のデータ更新
		Skf3030TShatakuMutual selectShatakuMutual = skf3030TShatakuMutualRepository.selectByPrimaryKey(shatakuMutualDataColumnInfoList.getShatakuKanriId());
		if(selectShatakuMutual != null){
			updateCount = skf3030Sc002UpdateShatakuKanriDaichoSogoRiyoExpRepository.updateShatakuKanriDaichoSogoRiyo(shatakuMutualDataColumnInfoList);
			if(updateCount == 0){
				return 0;
			}else{
				returnCount += 1;
			}
		}
		
		//「月別使用料履歴テーブル」のデータ更新
		Skf3030TShatakuRentalRirekiKey rirekiKey = new Skf3030TShatakuRentalRirekiKey();
		rirekiKey.setShatakuKanriId(shatakuRentalRirekiDataColumnInfoList.getShatakuKanriId());
		rirekiKey.setYearMonth(shatakuRentalRirekiDataColumnInfoList.getYearMonth());
		Skf3030TShatakuRentalRireki selectRireki = skf3030TShatakuRentalRirekiRepository.selectByPrimaryKey(rirekiKey);
		if(selectRireki != null){
			updateCount = skf3030Sc002UpdateTsukibetsuShiyoryoRirekiExpRepository.updateTsukibetsuShiyoryoRireki(shatakuRentalRirekiDataColumnInfoList);
			if(updateCount == 0){
				return 0;
			}else{
				returnCount += 1;
			}
		}
		
		//月別備品使用料明細テーブルにデータ更新
		for(Map<String,Object> dr : bihinDataDt){
			Skf3030TShatakuBihinRirekiKey sBihinKey = new Skf3030TShatakuBihinRirekiKey();
			sBihinKey.setShatakuKanriId(Long.parseLong(shatakuKanriId));
			sBihinKey.setYearMonth(nengetsu);
			sBihinKey.setBihinCd(dr.get("bihin_cd").toString());
			Skf3030TShatakuBihinRireki selectsBihin = skf3030TShatakuBihinRirekiRepository.selectByPrimaryKey(sBihinKey);
			if(selectsBihin != null){
				Skf3030TShatakuBihinRireki updShatakuBihinRireki = new Skf3030TShatakuBihinRireki();
				updShatakuBihinRireki.setShatakuKanriId(Long.parseLong(shatakuKanriId));
				updShatakuBihinRireki.setYearMonth(nengetsu);
				updShatakuBihinRireki.setBihinCd(dr.get("bihin_cd").toString());
				updShatakuBihinRireki.setBihinLentStatusKbn(dr.get("bihin_lent_status_kbn").toString());
				updateCount = skf3030TShatakuBihinRirekiRepository.updateByPrimaryKeySelective(updShatakuBihinRireki);
				if(updateCount == 0){
					return 0;
				}else{
					returnCount += 1;
				}
			}
			
		}
		
		
		//「月別駐車場履歴テーブル」のデータの区画１の情報更新
		if(changeFlg3){
			Skf3030TParkingRirekiKey parkRirekiKey = new Skf3030TParkingRirekiKey();
			parkRirekiKey.setShatakuKanriId(parkingRirekiDataColumnInfoList1.getShatakuKanriId());
			parkRirekiKey.setYearMonth(parkingRirekiDataColumnInfoList1.getYearMonth());
			parkRirekiKey.setParkingLendNo(parkingRirekiDataColumnInfoList1.getParkingLendNo());
			Skf3030TParkingRireki selectParkRireki = skf3030TParkingRirekiRepository.selectByPrimaryKey(parkRirekiKey);
			if(selectParkRireki != null){
				updateCount = skf3030Sc002UpdateTsukibetsuChushajoRirekiExpRepository.updateTsukibetsuChushajoRireki(parkingRirekiDataColumnInfoList1);
				if(updateCount == 0){
					return 0;
				}else{
					returnCount += 1;
				}
			}else{
				insertCount = skf3030TParkingRirekiRepository.insertSelective(parkingRirekiDataInsertColumnInfoList1);
				if(insertCount == 0){
					return 0;
				}else{
					returnCount += 1;
				}
			}
		}
		//区画１の利用開始日が入力されている場合
		if(!SkfCheckUtils.isNullOrEmpty(kukaku1RiyoKaishibi)){
			//社宅駐車場区画マスタを更新する
			Skf3010MShatakuParkingBlockKey parkBlockKey = new Skf3010MShatakuParkingBlockKey();
			parkBlockKey.setShatakuKanriNo(shatakuParkingBlockDataColumnInfoList1.getShatakuKanriNo());
			parkBlockKey.setParkingKanriNo(shatakuParkingBlockDataColumnInfoList1.getParkingKanriNo());
			Skf3010MShatakuParkingBlock selectParkBlock = skf3010MShatakuParkingBlockRepository.selectByPrimaryKey(parkBlockKey);
			if(selectParkBlock != null){
				updateCount = skf3010MShatakuParkingBlockRepository.updateByPrimaryKeySelective(shatakuParkingBlockDataColumnInfoList1);
				if(updateCount == 0){
					return 0;
				}else{
					returnCount += 1;
				}
			}
		}
		
		//変更前区画１駐車場管理番号（hidden）と区画１駐車場管理番号（hidden）が異なる場合
		if(changeFlg1){
			//社宅駐車場区画マスタを更新する
			Skf3010MShatakuParkingBlockKey parkBlockKey = new Skf3010MShatakuParkingBlockKey();
			parkBlockKey.setShatakuKanriNo(shatakuParkingBlockDataChangeColumnInfoList1.getShatakuKanriNo());
			parkBlockKey.setParkingKanriNo(shatakuParkingBlockDataChangeColumnInfoList1.getParkingKanriNo());
			Skf3010MShatakuParkingBlock selectParkBlock = skf3010MShatakuParkingBlockRepository.selectByPrimaryKey(parkBlockKey);
			if(selectParkBlock != null){
				updateCount = skf3010MShatakuParkingBlockRepository.updateByPrimaryKeySelective(shatakuParkingBlockDataChangeColumnInfoList1);
				if(updateCount == 0){
					return 0;
				}else{
					returnCount += 1;
				}
			}
		}
		//「月別駐車場履歴テーブル」のデータの区画２の情報更新
		if(changeFlg4){
			Skf3030TParkingRirekiKey parkRirekiKey = new Skf3030TParkingRirekiKey();
			parkRirekiKey.setShatakuKanriId(parkingRirekiDataColumnInfoList2.getShatakuKanriId());
			parkRirekiKey.setYearMonth(parkingRirekiDataColumnInfoList2.getYearMonth());
			parkRirekiKey.setParkingLendNo(parkingRirekiDataColumnInfoList2.getParkingLendNo());
			Skf3030TParkingRireki selectParkRireki = skf3030TParkingRirekiRepository.selectByPrimaryKey(parkRirekiKey);
			if(selectParkRireki != null){
				updateCount = skf3030Sc002UpdateTsukibetsuChushajoRirekiExpRepository.updateTsukibetsuChushajoRireki(parkingRirekiDataColumnInfoList2);
				if(updateCount == 0){
					return 0;
				}else{
					returnCount += 1;
				}
			}else{
				insertCount = skf3030TParkingRirekiRepository.insertSelective(parkingRirekiDataInsertColumnInfoList2);
				if(insertCount == 0){
					return 0;
				}else{
					returnCount += 1;
				}
			}
		}
		//区画２の利用開始日が入力されている場合
		if(!SkfCheckUtils.isNullOrEmpty(kukaku2RiyoKaishibi)){
			//社宅駐車場区画マスタを更新する
			Skf3010MShatakuParkingBlockKey parkBlockKey = new Skf3010MShatakuParkingBlockKey();
			parkBlockKey.setShatakuKanriNo(shatakuParkingBlockDataColumnInfoList2.getShatakuKanriNo());
			parkBlockKey.setParkingKanriNo(shatakuParkingBlockDataColumnInfoList2.getParkingKanriNo());
			Skf3010MShatakuParkingBlock selectParkBlock = skf3010MShatakuParkingBlockRepository.selectByPrimaryKey(parkBlockKey);
			if(selectParkBlock != null){
				updateCount = skf3010MShatakuParkingBlockRepository.updateByPrimaryKeySelective(shatakuParkingBlockDataColumnInfoList2);
				if(updateCount == 0){
					return 0;
				}else{
					returnCount += 1;
				}
			}
		}
		
		//変更前区画２駐車場管理番号（hidden）と区画２駐車場管理番号（hidden）が異なる場合
		if(changeFlg2){
			//社宅駐車場区画マスタを更新する
			Skf3010MShatakuParkingBlockKey parkBlockKey = new Skf3010MShatakuParkingBlockKey();
			parkBlockKey.setShatakuKanriNo(shatakuParkingBlockDataChangeColumnInfoList2.getShatakuKanriNo());
			parkBlockKey.setParkingKanriNo(shatakuParkingBlockDataChangeColumnInfoList2.getParkingKanriNo());
			Skf3010MShatakuParkingBlock selectParkBlock = skf3010MShatakuParkingBlockRepository.selectByPrimaryKey(parkBlockKey);
			if(selectParkBlock != null){
				updateCount = skf3010MShatakuParkingBlockRepository.updateByPrimaryKeySelective(shatakuParkingBlockDataChangeColumnInfoList2);
				if(updateCount == 0){
					return 0;
				}else{
					returnCount += 1;
				}
			}
		}
		//「月別相互利用履歴テーブル」のデータを更新
		Skf3030TShatakuMutualRirekiKey smRirekiKey = new Skf3030TShatakuMutualRirekiKey();
		smRirekiKey.setShatakuKanriId(shatakuMutualRirekiDataColumnInfoList.getShatakuKanriId());
		smRirekiKey.setYearMonth(shatakuMutualRirekiDataColumnInfoList.getYearMonth());
		Skf3030TShatakuMutualRireki selectSMRireki = skf3030TShatakuMutualRirekiRepository.selectByPrimaryKey(smRirekiKey);
		if(selectSMRireki != null){
			updateCount = skf3030Sc002UpdateTsukibetsuSogoRiyoRirekiExpRepository.updateTsukibetsuSogoRiyoRireki(shatakuMutualRirekiDataColumnInfoList);
			if(updateCount == 0){
				return 0;
			}else{
				returnCount += 1;
			}
		}
		
		//社宅部屋情報マスタ更新
		Skf3010MShatakuRoomKey shatakuRoomKey = new Skf3010MShatakuRoomKey();
		shatakuRoomKey.setShatakuKanriNo(shatakuRoomDataColumnInfoList.getShatakuKanriNo());
		shatakuRoomKey.setShatakuRoomKanriNo(shatakuRoomDataColumnInfoList.getShatakuRoomKanriNo());
		Skf3010MShatakuRoom selectShatakuRoom = skf3010MShatakuRoomRepository.selectByPrimaryKey(shatakuRoomKey);
		if(selectShatakuRoom != null){
			updateCount = skf3010MShatakuRoomRepository.updateByPrimaryKeySelective(shatakuRoomDataColumnInfoList);
			if(updateCount == 0){
				return 0;
			}else{
				returnCount += 1;
			}
		}
		
		//使用料パターンテーブル登録・更新
		if(CodeConstant.STRING_ZERO.equals(rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.UPDATE_KIND))){
			//データの登録
			Skf3030TRentalPattern patternRecord = new Skf3030TRentalPattern();
			// 社宅管理番号
			patternRecord.setShatakuKanriNo(Long.parseLong(
					rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.SHATAKUKANRI_NO)));
			// 使用料パターンID
			patternRecord.setRentalPatternId(Long.parseLong(
					rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.RENTAL_PATTERNID)));
			// 使用料パターン名
			patternRecord.setRentalPatternName(
					rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.PATTERN_NAME));
			// 規格
			patternRecord.setKikaku(
					rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.KIKAKU));
			// 基準使用料算定上延べ面積
			patternRecord.setBaseCalcMenseki(new BigDecimal(
					rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.KIJUN_MENSEKI)));
			// 社宅使用料算定上延べ面積
			patternRecord.setShatakuCalcMenseki(Short.parseShort(
					rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.SHATAKU_MENSEKI)));
			// 経年残価率
			patternRecord.setAgingResidualRate(new BigDecimal(
					rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.KEINEN_ZANKARITSU)));
			// 用途
			patternRecord.setAuse(
					rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.YOTO));
			// 経年
			patternRecord.setAging(Short.parseShort(
					rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.KEINEN)));
			// 基本使用料
			patternRecord.setBaseRental(Integer.parseInt(
					rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.KIHON_SHIYORYO)));
			// 単価
			patternRecord.setPrice(new BigDecimal(
					rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.TANKA)));
			// 社宅使用料月額
			patternRecord.setRental(Integer.parseInt(
					rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.SHATAKU_GETSUGAKU)));
			// 延べ面積
			patternRecord.setMenseki(new BigDecimal(
					rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.NOBE_MENSEKI)));
			// サンルーム面積
			patternRecord.setSunRoomMenseki(new BigDecimal(
					rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.SUNROOM_MENSEKI)));
			// 階段面積
			patternRecord.setStairsMenseki(new BigDecimal(
					rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.KAIDAN_MENSEKI)));
			// 物置面積
			patternRecord.setBarnMenseki(new BigDecimal(
					rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.MONOOKI_MENSEKI)));
			insertCount = skf3030TRentalPatternRepository.insertSelective(patternRecord);
			if(insertCount == 0){
				return 0;
			}else{
				returnCount += 1;
			}
		}else{
			//更新処理
			Skf3022Sc006GetRentalPatternForUpdateExpParameter param = new Skf3022Sc006GetRentalPatternForUpdateExpParameter();
			param.setRentalPatternId(Long.parseLong(
					rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.RENTAL_PATTERNID)));
			param.setUpdateDate(rentalPatternUpdateDate);
			List<Skf3022Sc006GetRentalPatternForUpdateExp> targetDt = new ArrayList<Skf3022Sc006GetRentalPatternForUpdateExp>();
			targetDt = skf3022Sc006UpdateRentalPatternExpRepository.getRentalPatternForUpdate(param);
			if (targetDt.size() > 0) {
				if(SkfCheckUtils.isNullOrEmpty(rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.SHATAKU_GETSUGAKU))){
					//データの更新（使用料パターン未計算）
					Skf3030TRentalPattern record = new Skf3030TRentalPattern();
					// 社宅管理番号
					record.setShatakuKanriNo(Long.parseLong(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.SHATAKUKANRI_NO)));
					// 使用料パターンID
					record.setRentalPatternId(Long.parseLong(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.RENTAL_PATTERNID)));
					// 使用料パターン名
					record.setRentalPatternName(null);
					// 規格
					record.setKikaku(null);
					// 基準使用料算定上延べ面積
					record.setBaseCalcMenseki(null);
					// 社宅使用料算定上延べ面積
					record.setShatakuCalcMenseki(null);
					// 経年残価率
					record.setAgingResidualRate(null);
					// 用途
					record.setAuse(null);
					// 経年
					record.setAging(null);
					// 基本使用料
					record.setBaseRental(null);
					// 単価
					record.setPrice(null);
					// 社宅使用料月額
					record.setRental(null);
					// 更新日時
					record.setUpdateDate(rentalPatternUpdateDateForRegist);
					// 更新者ID
					record.setUpdateUserId(userId);
					// 更新機能ID
					record.setUpdateProgramId(pageId);
					// 延べ面積
					record.setMenseki(null);
					// サンルーム面積
					record.setSunRoomMenseki(null);
					// 階段面積
					record.setStairsMenseki(null);
					// 物置面積
					record.setBarnMenseki(null);
					// 更新
					updateCount = skf3022Sc006UpdateRentalPatternExpRepository.updateRentalPattern(record);
					
				}else{
					//データの更新（使用料パターン計算済）
					Skf3030TRentalPattern record = new Skf3030TRentalPattern();
					// 社宅管理番号
					record.setShatakuKanriNo(Long.parseLong(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.SHATAKUKANRI_NO)));
					// 使用料パターンID
					record.setRentalPatternId(Long.parseLong(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.RENTAL_PATTERNID)));
					// 使用料パターン名
					record.setRentalPatternName(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.PATTERN_NAME));
					// 規格
					record.setKikaku(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.KIKAKU));
					// 基準使用料算定上延べ面積
					record.setBaseCalcMenseki(new BigDecimal(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.KIJUN_MENSEKI)));
					// 社宅使用料算定上延べ面積
					record.setShatakuCalcMenseki(Short.parseShort(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.SHATAKU_MENSEKI)));
					// 経年残価率
					record.setAgingResidualRate(new BigDecimal(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.KEINEN_ZANKARITSU)));
					// 用途
					record.setAuse(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.YOTO));
					// 経年
					record.setAging(Short.parseShort(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.KEINEN)));
					// 基本使用料
					record.setBaseRental(Integer.parseInt(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.KIHON_SHIYORYO)));
					// 単価
					record.setPrice(new BigDecimal(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.TANKA)));
					// 社宅使用料月額
					record.setRental(Integer.parseInt(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.SHATAKU_GETSUGAKU)));
					// 更新日時
					record.setUpdateDate(rentalPatternUpdateDateForRegist);
					// 更新者ID
					record.setUpdateUserId(userId);
					// 更新機能ID
					record.setUpdateProgramId(pageId);
					// 延べ面積
					record.setMenseki(new BigDecimal(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.NOBE_MENSEKI)));
					// サンルーム面積
					record.setSunRoomMenseki(new BigDecimal(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.SUNROOM_MENSEKI)));
					// 階段面積
					record.setStairsMenseki(new BigDecimal(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.KAIDAN_MENSEKI)));
					// 物置面積
					record.setBarnMenseki(new BigDecimal(
							rentalPatternTorokuList.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.MONOOKI_MENSEKI)));
					// 更新
					updateCount = skf3022Sc006UpdateRentalPatternExpRepository.updateRentalPattern(record);
				}
				if(updateCount == 0){
					return 0;
				}else{
					returnCount += 1;
				}
			}else{
				return -1;
			}
		}
		
		return returnCount;
	}

}
