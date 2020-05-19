/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3030.domain.service.skf3030sc002;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetCompanyAgencyListExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetBihinInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetBihinInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetBihinInfoForNoIdExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetBihinInfoForNoIdExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetMaxParkingEndDayExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetMaxParkingEndDayExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetParkingUpdateTaikyoCheckExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetParkingUpdateTaikyoCheckExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetRentalPatternInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetRentalPatternInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetShatakuDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBaseBusinessLogicUtils.SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBaseBusinessLogicUtils.SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShain;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShainKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuParkingBlock;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuRoom;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuRoomBihin;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuRoomKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3022TTeijiData;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TParkingRireki;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TParkingRirekiKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuBihin;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuLedger;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuMutual;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuMutualRireki;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuRentalRireki;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3050TMonthlyManageData;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetCompanyAgencyListExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc002.Skf3030Sc002GetBihinInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc002.Skf3030Sc002GetBihinInfoForNoIdExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc002.Skf3030Sc002GetMaxParkingEndDayExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc002.Skf3030Sc002GetMaxRentalPatternIdExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc002.Skf3030Sc002GetParkingUpdateTaikyoCheckExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc002.Skf3030Sc002GetRentalPatternInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc002.Skf3030Sc002GetShatakuDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MShainRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuRoomRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3022TTeijiDataRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3030TParkingRirekiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3050TMonthlyManageDataRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.CodeCacheUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.LoginUserInfoUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.SkfPageBusinessLogicUtils;
import jp.co.c_nexco.skf.skf3030.domain.dto.skf3030Sc002common.Skf3030Sc002CommonAsyncDto;
import jp.co.c_nexco.skf.skf3030.domain.dto.skf3030Sc002common.Skf3030Sc002CommonDto;

/**
 * Skf3030Sc002SharedService 入退居情報登録共通処理Service
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf3030Sc002SharedService {

	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	@Autowired
	private SkfPageBusinessLogicUtils skfPageBusinessLogicUtils;
	@Autowired
	private SkfDropDownUtils skfDropDownUtils;
	@Autowired
	private CodeCacheUtils codeCacheUtils;
	@Autowired
	private SkfDropDownUtils ddlUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private Skf1010MShainRepository skf1010MShainRepository;
	@Autowired
	private Skf3010MShatakuRoomRepository skf3010MShatakuRoomRepository;
	@Autowired
	private Skf3022TTeijiDataRepository skf3022TTeijiDataRepository;
	@Autowired
	private Skf3050TMonthlyManageDataRepository skf3050TMonthlyManageDataRepository;
	@Autowired
	private Skf3030TParkingRirekiRepository skf3030TParkingRirekiRepository;
	@Autowired
	private Skf3022Sc006GetCompanyAgencyListExpRepository skf3022Sc006GetCompanyAgencyListExpRepository;
	@Autowired
	private Skf3030Sc002GetRentalPatternInfoExpRepository skf3030Sc002GetRentalPatternInfoExpRepository;
	@Autowired
	private Skf3030Sc002GetBihinInfoForNoIdExpRepository skf3030Sc002GetBihinInfoForNoIdExpRepository;
	@Autowired
	private Skf3030Sc002GetBihinInfoExpRepository skf3030Sc002GetBihinInfoExpRepository;
	@Autowired
	private Skf3030Sc002GetShatakuDataExpRepository skf3030Sc002GetShatakuDataExpRepository;
	@Autowired
	private Skf3030Sc002GetMaxParkingEndDayExpRepository skf3030Sc002GetMaxParkingEndDayExpRepository;
	@Autowired
	private Skf3030Sc002GetMaxRentalPatternIdExpRepository skf3030Sc002GetMaxRentalPatternIdExpRepository;
	@Autowired
	private Skf3030Sc002GetParkingUpdateTaikyoCheckExpRepository skf3030Sc002GetParkingUpdateTaikyoCheckExpRepository;
	
	//比較用文字列1
	private static final String STR_ONE = "1";
	//使用料計算
	private static final String MAX_END_DATE = "99991231";
	private static final String TRUE = "true";

	
	/**
	 * 「締め処理実行区分」取得
	 * @param nengetsu 年月
	 * @return 締め処理実行区分
	 */
	public String getBillingActKbn(String nengetsu){
		
		String billingActKbn = CodeConstant.DOUBLE_QUOTATION;
		//「締め処理実行区分」を取得する
		if(!SkfCheckUtils.isNullOrEmpty(nengetsu)){
			Skf3050TMonthlyManageData da = skf3050TMonthlyManageDataRepository.selectByPrimaryKey(nengetsu);
			if(da != null){
				billingActKbn = da.getBillingActKbn();
			}
			da = null;
		}
		
		return billingActKbn;
	}
	
	/**
	 * ドロップダウンリスト作成
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param sc006KyojyusyaKbnSelect			居住者区分選択値
	 * @param sc006KyojyusyaKbnSelectList		*居住者区分ドロップダウンリスト
	 * @param sc006YakuinSanteiSelect			役員算定選択値
	 * @param sc006YakuinSanteiSelectList		*役員算定ドロップダウンリスト
	 * @param sc006KyoekihiPayMonthSelect		共益費支払月選択値
	 * @param sc006KyoekihiPayMonthSelectList	*共益費支払月ドロップダウンリスト
	 * @param sc006KibouTimeInSelect			 搬入希望時間選択値
	 * @param sc006KibouTimeInSelectList		* 搬入希望時間ドロップダウンリスト
	 * @param sc006KibouTimeOutSelect			搬出希望時間選択値
	 * @param sc006KibouTimeOutSelectList		*搬出希望時間ドロップダウンリスト
	 * @param sc006SogoRyojokyoSelect			出向の有無(相互利用状況)選択値
	 * @param sc006SogoRyojokyoSelectList		*出向の有無(相互利用状況)ドロップダウンリスト
	 * @param sc006SogoHanteiKbnSelect			相互利用判定区分選択値
	 * @param sc006SogoHanteiKbnSelectList		*相互利用判定区分ドロップダウンリスト
	 * @param sc006SokinShatakuSelect			社宅使用料会社間送金区分選択値
	 * @param sc006SokinShatakuSelectList		*社宅使用料会社間送金区分ドロップダウンリスト
	 * @param sc006SokinKyoekihiSelect			共益費会社間送付区分選択値
	 * @param sc006SokinKyoekihiSelectList		*共益費会社間送付区分ドロップダウンリスト
	 * @param sc006OldKaisyaNameSelect			原籍会社選択値
	 * @param sc006OldKaisyaNameSelectList		*原籍会社ドロップダウンリスト
	 * @param sc006KyuyoKaisyaSelect			給与支給会社名選択値
	 * @param sc006KyuyoKaisyaSelectList		*給与支給会社名ドロップダウンリスト
	 * @param sc006HaizokuKaisyaSelect			配属会社名選択値
	 * @param sc006HaizokuKaisyaSelectList		*配属会社名ドロップダウンリスト
	 * @param sc006TaiyoKaisyaSelect			貸付会社選択値
	 * @param sc006TaiyoKaisyaSelectList		*貸付会社ドロップダウンリスト
	 * @param sc006KariukeKaisyaSelect			借受会社選択値
	 * @param sc006KariukeKaisyaSelectList		*借受会社ドロップダウンリスト
	 */
	public void setDdlControlValues(
			String sc006KyojyusyaKbnSelect, List<Map<String, Object>> sc006KyojyusyaKbnSelectList,
			String sc006YakuinSanteiSelect, List<Map<String, Object>> sc006YakuinSanteiSelectList,
			String sc006KyoekihiPayMonthSelect, List<Map<String, Object>> sc006KyoekihiPayMonthSelectList,
			String sc006KibouTimeInSelect, List<Map<String, Object>> sc006KibouTimeInSelectList,
			String sc006KibouTimeOutSelect, List<Map<String, Object>> sc006KibouTimeOutSelectList,
			String sc006SogoRyojokyoSelect, List<Map<String, Object>> sc006SogoRyojokyoSelectList,
			String sc006SogoHanteiKbnSelect, List<Map<String, Object>> sc006SogoHanteiKbnSelectList,
			String sc006SokinShatakuSelect, List<Map<String, Object>> sc006SokinShatakuSelectList,
			String sc006SokinKyoekihiSelect, List<Map<String, Object>> sc006SokinKyoekihiSelectList,
			String sc006OldKaisyaNameSelect, List<Map<String, Object>> sc006OldKaisyaNameSelectList,
			String sc006KyuyoKaisyaSelect, List<Map<String, Object>> sc006KyuyoKaisyaSelectList,
			String sc006HaizokuKaisyaSelect, List<Map<String, Object>> sc006HaizokuKaisyaSelectList,
			String sc006TaiyoKaisyaSelect, List<Map<String, Object>> sc006TaiyoKaisyaSelectList,
			String sc006KariukeKaisyaSelect, List<Map<String, Object>> sc006KariukeKaisyaSelectList) {

		// 居住者区分
		sc006KyojyusyaKbnSelectList.clear();
		sc006KyojyusyaKbnSelectList.addAll(skfDropDownUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_KYOJUSHA_KBN, sc006KyojyusyaKbnSelect, false));
		// 役員算定
		sc006YakuinSanteiSelectList.clear();
		sc006YakuinSanteiSelectList.addAll(skfDropDownUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_YAKUIN_KBN, sc006YakuinSanteiSelect, false));
		// 共益費支払月
		sc006KyoekihiPayMonthSelectList.clear();
		sc006KyoekihiPayMonthSelectList.addAll(skfDropDownUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_KYOEKIHI_PAY_MONTH_KBN, sc006KyoekihiPayMonthSelect, true));
		// 搬入希望日時時間帯
		sc006KibouTimeInSelectList.clear();
		sc006KibouTimeInSelectList.addAll(skfDropDownUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_REQUESTTIME_KBN, sc006KibouTimeInSelect, true));
		// 搬出希望日時時間帯
		sc006KibouTimeOutSelectList.clear();
		sc006KibouTimeOutSelectList.addAll(skfDropDownUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_REQUESTTIME_KBN, sc006KibouTimeOutSelect, true));
		// 出向の有無(相互利用状況)
		sc006SogoRyojokyoSelectList.clear();
		sc006SogoRyojokyoSelectList.addAll(skfDropDownUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_MUTUAL_USE_JOKYO, sc006SogoRyojokyoSelect, false));
		// 相互利用判定区分
		sc006SogoHanteiKbnSelectList.clear();
		sc006SogoHanteiKbnSelectList.addAll(skfDropDownUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_MUTUALUSE_KBN, sc006SogoHanteiKbnSelect, true));
		// 会社間送金区分（社宅使用料）
		sc006SokinShatakuSelectList.clear();
		sc006SokinShatakuSelectList.addAll(skfDropDownUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_COMPANY_TRANSFER_KBN, sc006SokinShatakuSelect, true));
		// 会社間送金区分（共益費）
		sc006SokinKyoekihiSelectList.clear();
		sc006SokinKyoekihiSelectList.addAll(skfDropDownUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_COMPANY_TRANSFER_KBN, sc006SokinKyoekihiSelect, true));
		// 会社リスト（外部機関含む）取得
		List<Skf3022Sc006GetCompanyAgencyListExp> companyAgencyList = new ArrayList<Skf3022Sc006GetCompanyAgencyListExp>();
		companyAgencyList = skf3022Sc006GetCompanyAgencyListExpRepository.getCompanyAgencyList();
		// 原籍会社
		sc006OldKaisyaNameSelectList.clear();
		sc006OldKaisyaNameSelectList.addAll(getCompanyAgencyDoropDownList(companyAgencyList, sc006OldKaisyaNameSelect, true));
		// 給与支給会社名
		sc006KyuyoKaisyaSelectList.clear();
		sc006KyuyoKaisyaSelectList.addAll(getCompanyAgencyDoropDownList(companyAgencyList, sc006KyuyoKaisyaSelect, true));
		// 配属会社
		sc006HaizokuKaisyaSelectList.clear();
		sc006HaizokuKaisyaSelectList.addAll(getCompanyAgencyDoropDownList(companyAgencyList, sc006HaizokuKaisyaSelect, true));
		// 貸付会社名
		sc006TaiyoKaisyaSelectList.clear();
		sc006TaiyoKaisyaSelectList.addAll(getCompanyAgencyDoropDownList(companyAgencyList, sc006TaiyoKaisyaSelect, true));
		// 借受会社名
		sc006KariukeKaisyaSelectList.clear();
		sc006KariukeKaisyaSelectList.addAll(getCompanyAgencyDoropDownList(companyAgencyList, sc006KariukeKaisyaSelect, true));
	}
	
	/**
	 * 会社リスト（外部機関含む）ドロップダウンリスト作成
	 * 
	 * @param companyAgencyList	会社リスト（外部機関含む）
	 * @param selectedValue		選択値
	 * @param isFirstRowEmpty	先頭行の空行有無。true:空行あり false：空行なし
	 * @return	会社リスト（外部機関含む）ドロップダウンリスト
	 */
	private List<Map<String, Object>> getCompanyAgencyDoropDownList(
			List<Skf3022Sc006GetCompanyAgencyListExp> companyAgencyList, String selectedValue, boolean isFirstRowEmpty) {

		// リスト定義
		List<Map<String, Object>> doropDownList = new ArrayList<Map<String, Object>>();
		Map<String, Object> forListMap = new HashMap<String, Object>();
		if (isFirstRowEmpty) {
			forListMap.put("value", CodeConstant.DOUBLE_QUOTATION);
			forListMap.put("label", CodeConstant.DOUBLE_QUOTATION);
			doropDownList.add(forListMap);
		}

		for (Skf3022Sc006GetCompanyAgencyListExp entity : companyAgencyList) {

			// 表示・値を設定
			forListMap = new HashMap<String, Object>();
			forListMap.put("value", entity.getCompanyCd());
			forListMap.put("label", entity.getCompanyName());
			if (!CheckUtils.isEmpty(selectedValue)) {
				if (Objects.equals(entity.getCompanyCd(), selectedValue)) {
					forListMap.put("selected", true);
				}
			}
			doropDownList.add(forListMap);
		}
		return doropDownList;
	}
	
	/**
	 * 使用料パターン情報取得
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param comDto	*DTO
	 */
	public void getShiyoryoKeisanSessionInfo(Skf3030Sc002CommonDto comDto) {

		SimpleDateFormat dateFormat = new SimpleDateFormat(Skf3030Sc002CommonDto.DATE_FORMAT);
		// 使用料入力支援戻り値クリア
		clearShiyoryoShienData(comDto);
		// 使用料パターンID（hidden変数）が存在する場合、使用料計算情報を取得する。
		if (!CheckUtils.isEmpty(comDto.getHdnShiyoryoKeisanPatternId())) {
			List<Skf3030Sc002GetRentalPatternInfoExp> rentalPatternInfoList = new ArrayList<Skf3030Sc002GetRentalPatternInfoExp>();
			Skf3030Sc002GetRentalPatternInfoExpParameter param = new Skf3030Sc002GetRentalPatternInfoExpParameter();
			param.setRentalPatternId(Long.parseLong(comDto.getHdnShiyoryoKeisanPatternId()));

			// 使用料パターン情報取得
			rentalPatternInfoList = skf3030Sc002GetRentalPatternInfoExpRepository.getRentalPatternInfo(param);
			param = null;
			// 取得結果判定
			if (rentalPatternInfoList.size() < 1) {
				// デバッグログ
				LogUtils.debugByMsg("使用料パターン情報取得結果：0件");
				// 使用料パターンが存在しても、使用料情報が存在しない場合（使用料支援未操作の場合）作成しない。
				//comDto.setHdnSiyouryoIdOld("");
				//comDto.setHdnSiyouryoId("");
				comDto.setHdnChangeBeforeRentalPatternId("");
				comDto.setHdnRentalPatternId("");
				return;
			}
			Skf3030Sc002GetRentalPatternInfoExp rentalPatternInfo = rentalPatternInfoList.get(0);
			rentalPatternInfoList = null;
			/** 使用料計算入力支援情報 */
			// 貸与規格（ヘッダ表示用項目）(使用料パターン名)
			if (!CheckUtils.isEmpty(rentalPatternInfo.getRentalPatternName())) {
				comDto.setHdnRateShienPatternName(rentalPatternInfo.getRentalPatternName());
			} else {
				comDto.setHdnRateShienPatternName("");
			}
			// 規格
			if (!CheckUtils.isEmpty(rentalPatternInfo.getKikaku())) {
				comDto.setHdnRateShienKikaku(rentalPatternInfo.getKikaku());
				// 規格名
				comDto.setHdnRateShienKikakuName(codeCacheUtils.getGenericCodeName(
						FunctionIdConstant.GENERIC_CODE_KIKAKU_KBN, rentalPatternInfo.getKikaku()));
			} else {
				comDto.setHdnRateShienPatternName("");
				comDto.setHdnRateShienKikakuName("");
			}
			// 基準使用料算定上延べ面積
			if (rentalPatternInfo.getBaseCalcMenseki() != null) {
				comDto.setHdnRateShienKijunMenseki(rentalPatternInfo.getBaseCalcMenseki().toPlainString());
			} else {
				comDto.setHdnRateShienKijunMenseki(CodeConstant.STRING_ZERO);
			}
			// 社宅使用料算定上延べ面積
			if (rentalPatternInfo.getShatakuCalcMenseki() != null) {
				comDto.setHdnRateShienShatakuMenseki(rentalPatternInfo.getShatakuCalcMenseki().toString());
			} else {
				comDto.setHdnRateShienShatakuMenseki(CodeConstant.STRING_ZERO);
			}
			// 経年残価率
			if (rentalPatternInfo.getAgingResidualRate() != null) {
				comDto.setHdnRateShienKeinenZankaRitsu(rentalPatternInfo.getAgingResidualRate().toPlainString());
			} else {
				comDto.setHdnRateShienKeinenZankaRitsu(CodeConstant.STRING_ZERO);
			}
			// 用途
			if (!CheckUtils.isEmpty(rentalPatternInfo.getAuse())) {
				comDto.setHdnRateShienYoto(rentalPatternInfo.getAuse());
				// 用途名
				comDto.setHdnRateShienYotoName(codeCacheUtils.getGenericCodeName(
						FunctionIdConstant.GENERIC_CODE_AUSE_KBN, rentalPatternInfo.getAuse()));
			} else {
				comDto.setHdnRateShienYoto("");
				comDto.setHdnRateShienYotoName("");
			}
			// 経年
			if (rentalPatternInfo.getAging() != null) {
				comDto.setHdnRateShienKeinen(rentalPatternInfo.getAging().toString());
			} else {
				comDto.setHdnRateShienKeinen(CodeConstant.STRING_ZERO);
			}
			// 基本使用料
			if (rentalPatternInfo.getBaseRental() != null) {
				comDto.setHdnRateShienKihonShiyoryo(rentalPatternInfo.getBaseRental().toString());
			} else {
				comDto.setHdnRateShienKihonShiyoryo(CodeConstant.STRING_ZERO);
			}
			// 単価
			if (rentalPatternInfo.getPrice() != null) {
				comDto.setHdnRateShienTanka(rentalPatternInfo.getPrice().toPlainString());
			} else {
				comDto.setHdnRateShienTanka(CodeConstant.STRING_ZERO);
			}
			// 社宅使用料月額
			if (rentalPatternInfo.getRental() != null) {
				comDto.setHdnRateShienShatakuGetsugaku(rentalPatternInfo.getRental().toString());
			} else {
				comDto.setHdnRateShienShatakuGetsugaku(CodeConstant.STRING_ZERO);
			}
			// 延べ面積
			if (rentalPatternInfo.getMenseki() != null) {
				comDto.setHdnRateShienNobeMenseki(rentalPatternInfo.getMenseki().toPlainString());
			} else {
				comDto.setHdnRateShienNobeMenseki(CodeConstant.STRING_ZERO);
			}
			// サンルーム面積
			if (rentalPatternInfo.getSunRoomMenseki() != null) {
				comDto.setHdnRateShienSunroomMenseki(rentalPatternInfo.getSunRoomMenseki().toPlainString());
			} else {
				comDto.setHdnRateShienSunroomMenseki(CodeConstant.STRING_ZERO);
			}
			// 階段面積
			if (rentalPatternInfo.getStairsMenseki() != null) {
				comDto.setHdnRateShienKaidanMenseki(rentalPatternInfo.getStairsMenseki().toPlainString());
			} else {
				comDto.setHdnRateShienKaidanMenseki(CodeConstant.STRING_ZERO);
			}
			// 物置面積
			if (rentalPatternInfo.getBarnMenseki() != null) {
				comDto.setHdnRateShienMonookiMenseki(rentalPatternInfo.getBarnMenseki().toPlainString());
			} else {
				comDto.setHdnRateShienMonookiMenseki(CodeConstant.STRING_ZERO);
			}
			// 更新日時
			if (rentalPatternInfo.getUpdateDate() != null) {
				comDto.setHdnRentalPatternUpdateDate(dateFormat.format(rentalPatternInfo.getUpdateDate()));
			}
		}
	}
	
	/**
	 * 使用料計算入力支援戻り値クリア
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param comDto	*DTO
	 */
	public void clearShiyoryoShienData(Skf3030Sc002CommonDto comDto) {

		comDto.setHdnRateShienKikaku("");
		comDto.setHdnRateShienYoto("");
		comDto.setHdnRateShienNobeMenseki("");
		comDto.setHdnRateShienKijunMenseki("");
		comDto.setHdnRateShienShatakuMenseki("");
		comDto.setHdnRateShienKikakuName("");
		comDto.setHdnRateShienYotoName("");
		comDto.setHdnRateShienSunroomMenseki("");
		comDto.setHdnRateShienKaidanMenseki("");
		comDto.setHdnRateShienMonookiMenseki("");
		comDto.setHdnRateShienTanka("");
		comDto.setHdnRateShienKeinen("");
		comDto.setHdnRateShienKeinenZankaRitsu("");
		comDto.setHdnRateShienPatternName("");
		comDto.setHdnRateShienPatternGetsugaku("");
		comDto.setHdnRateShienShatakuGetsugaku("");
		comDto.setHdnRateShienKihonShiyoryo("");
	}
	
	/**
	 * 円表示形式に変換し取得する
	 * @param value 変換文字列
	 * @param isAppndYen True:戻り値に円をつける、False:戻り値に円をつけない
	 * @return
	 */
	public String convertYenFormat(Integer value,Boolean isAppndYen){
		String result = CodeConstant.DOUBLE_QUOTATION;
		
		if(value == null){
			return result;
		}
		
		if(isAppndYen){
			result =  String.format("%,d", value) + "円";
		}else{
			result =  String.format("%,d", value);
		}
		
		return result;
	}
	
	/**
	 * ヘッダーの備品提示のフォントらかーを設定する。
	 * パラメータのステータス区分別のフォントカラー文字列を返却する
	 * 
	 * @param bihinTeijiStatusKbn	b品提示ステータス
	 * @return	フォントカラー文字列
	 */
	public String setBihinTeijiStatusCss(String bihinTeijiStatusKbn) {

		String colorStr = "color:black;";
		// null空チェック
		if (CheckUtils.isEmpty(bihinTeijiStatusKbn)) {
			return colorStr;
		}
		// 備品ステータス判定
		switch (bihinTeijiStatusKbn) {
		case CodeConstant.BIHIN_STATUS_MI_SAKUSEI:
			colorStr = Skf3030Sc002CommonDto.BIHIN_FONT_MI_SAKUSEI;
			break;
		case CodeConstant.BIHIN_STATUS_SAKUSEI_CHU:
			colorStr = Skf3030Sc002CommonDto.BIHIN_FONT_SAKUSEI_CHU;
			break;
		case CodeConstant.BIHIN_STATUS_SAKUSEI_SUMI:
			colorStr = Skf3030Sc002CommonDto.BIHIN_FONT_SAKUSEI_SUMI;
			break;
		case CodeConstant.BIHIN_STATUS_TEIJI_CHU:
			colorStr = Skf3030Sc002CommonDto.BIHIN_FONT_TEIJI_CHU;
			break;
		case CodeConstant.BIHIN_STATUS_DOI_SUMI:
			colorStr = Skf3030Sc002CommonDto.BIHIN_FONT_DOI_SUMI;
			break;
		case CodeConstant.BIHIN_STATUS_HANNYU_MACHI:
			colorStr = Skf3030Sc002CommonDto.BIHIN_FONT_HANNYU_MACHI;
			break;
		case CodeConstant.BIHIN_STATUS_HANNYU_SUMI:
			colorStr = Skf3030Sc002CommonDto.BIHIN_FONT_HANNYU_SUMI;
			break;
		case CodeConstant.BIHIN_STATUS_HANSHUTSU_MACHI:
			colorStr = Skf3030Sc002CommonDto.BIHIN_FONT_HANSHUTSU_MACHI;
			break;
		case CodeConstant.BIHIN_STATUS_HANSHUTSU_SUMI:
			colorStr = Skf3030Sc002CommonDto.BIHIN_FONT_HANSHUTSU_SUMI;
			break;
		case CodeConstant.BIHIN_STATUS_SHONIN:
			colorStr = Skf3030Sc002CommonDto.BIHIN_FONT_SHONIN;
			break;
		default :
			break;
		};
		return colorStr;
	}

	/**
	 * ヘッダーの社宅提示のフォントカラーを設定する。
	 * パラメータのステータス区分別のフォントカラー文字列を返却する
	 * 
	 * @param shatakuTeijiStatusKbn	社宅提示ステータス
	 * @return	フォントカラー文字列
	 */
	public String setShatakuTeijiStatusCss(String shatakuTeijiStatusKbn) {

		String colorStr = "color:black;";
		// null空チェック
		if (CheckUtils.isEmpty(shatakuTeijiStatusKbn)) {
			return colorStr;
		}
		// 提示ステータス判定
		switch (shatakuTeijiStatusKbn) {
		case CodeConstant.PRESENTATION_SITUATION_SAKUSEI_CHU:
			colorStr = Skf3030Sc002CommonDto.TEIJI_FONT_SAKUSEI_CHU;
			break;
		case CodeConstant.PRESENTATION_SITUATION_SAKUSEI_SUMI:
			colorStr = Skf3030Sc002CommonDto.TEIJI_FONT_SAKUSEI_SUMI;
			break;
		case CodeConstant.PRESENTATION_SITUATION_TEIJI_CHU:
			colorStr = Skf3030Sc002CommonDto.TEIJI_FONT_TEIJI_CHU;
			break;
		case CodeConstant.PRESENTATION_SITUATION_DOI_SUMI:
			colorStr = Skf3030Sc002CommonDto.TEIJI_FONT_DOI_SUMI;
			break;
		case CodeConstant.PRESENTATION_SITUATION_SHONIN:
			colorStr = Skf3030Sc002CommonDto.TEIJI_FONT_SHONIN;
			break;
		default :
			break;
		};
		return colorStr;
	}
	
	/**
	 * 使用料計算(提示データ登録内部)用パラメータ作成(同期)
	 * 
	 * @param comDto	DTO
	 * @return			パラメータマップ
	 */
	public Map<String, String> createSiyoryoKeiSanParam(Skf3030Sc002CommonDto comDto) {

		Map<String, String> paramMap = new HashMap<String, String>();
		
		paramMap.put("sc006YakuinSanteiSelect", comDto.getSc006YakuinSanteiSelect());
		paramMap.put("hdnShatakuKanriNo", comDto.getHdnShatakuKanriNo());
		paramMap.put("hdnRateShienYoto", comDto.getHdnRateShienYoto());
		paramMap.put("hdnRateShienNobeMenseki", getMensekiText(comDto.getHdnRateShienNobeMenseki()));
		paramMap.put("hdnRateShienSunroomMenseki", getMensekiText(comDto.getHdnRateShienSunroomMenseki()));
		paramMap.put("hdnRateShienKaidanMenseki", getMensekiText(comDto.getHdnRateShienKaidanMenseki()));
		paramMap.put("hdnRateShienMonookiMenseki", getMensekiText(comDto.getHdnRateShienMonookiMenseki()));
		paramMap.put("hdnRateShienKijunMenseki", getMensekiText(comDto.getHdnRateShienKijunMenseki()));
		paramMap.put("hdnRateShienShatakuMenseki", getMensekiText(comDto.getHdnRateShienShatakuMenseki()));
		paramMap.put("hdnBirthday", comDto.getHdnBirthday());
		paramMap.put("sc006ChintaiRyo", comDto.getSc006ChintaiRyo());
		paramMap.put("sc006TyusyajoRyokin", comDto.getSc006TyusyajoRyokin());
		paramMap.put("sc006SiyoroTyoseiPay", comDto.getSc006SiyoroTyoseiPay());
		//paramMap.put("hdnNyutaikyoKbn", comDto.getHdnNyutaikyoKbn());
		paramMap.put("sc006NyukyoYoteiDay", comDto.getSc006NyukyoYoteiDay());
		//paramMap.put("hdnNyukyoDate", comDto.getHdnNyukyoDate());
		paramMap.put("sc006TaikyoYoteiDay", comDto.getSc006TaikyoYoteiDay());
		paramMap.put("sc006TyusyaDayPayOne", comDto.getSc006TyusyaDayPayOne());
		paramMap.put("sc006TyusyaDayPayTwo", comDto.getSc006TyusyaDayPayTwo());
		paramMap.put("sc006RiyouStartDayOne", comDto.getSc006RiyouStartDayOne());
		//paramMap.put("hdnRiyouStartDayOne", comDto.getHdnRiyouStartDayOne());
		paramMap.put("sc006RiyouEndDayOne", comDto.getSc006RiyouEndDayOne());
		paramMap.put("sc006RiyouStartDayTwo", comDto.getSc006RiyouStartDayTwo());
		//paramMap.put("hdnRiyouStartDayTwo", comDto.getHdnRiyouStartDayTwo());
		paramMap.put("sc006RiyouEndDayTwo", comDto.getSc006RiyouEndDayTwo());
		paramMap.put("sc006TyusyaTyoseiPay", comDto.getSc006TyusyaTyoseiPay());
		
		paramMap.put("sc006KyoekihiMonthPay", comDto.getSc006KyoekihiMonthPay());
		paramMap.put("sc006KyoekihiTyoseiPay", comDto.getSc006KyoekihiTyoseiPay());
		paramMap.put("sc006Kyoekihi", comDto.getSc006Kyoekihi());
		paramMap.put("hdnRentalPatternId", comDto.getHdnRentalPatternId());
		paramMap.put("hdnChushajoKanriNo1", comDto.getHdnChushajoKanriNo1());
		paramMap.put("hdnChushajoKanriNo2", comDto.getHdnChushajoKanriNo2());
		paramMap.put("hdnNengetsu", comDto.getHdnNengetsu());
		paramMap.put("hdnKaiSanAfterShatakuShiyoryoHiwariKingaku", comDto.getHdnKaiSanAfterShatakuShiyoryoHiwariKingaku());
		paramMap.put("hdnKaiSanAfterChushajoShiyoryoGetsugakuChoseigo", comDto.getHdnKaiSanAfterChushajoShiyoryoGetsugakuChoseigo());
		paramMap.put("hdnKaiSanAfterKukaku1ChushajoShiyoroHiwariKingaku", comDto.getHdnKaiSanAfterKukaku1ChushajoShiyoroHiwariKingaku());
		paramMap.put("hdnKaiSanAfterKukaku2ChushajoShiyoroHiwariKingaku", comDto.getHdnKaiSanAfterKukaku2ChushajoShiyoroHiwariKingaku());

		return paramMap;
	}

	/**
	 * 使用料計算(提示データ登録内部)戻り値DTO設定(同期)
	 * 使用料計算の戻り値をDTOに設定する
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param comDto	*DTO
	 * @return			パラメータマップ
	 */
	public void setSiyoryoKeiSanParam(Map<String, String> resultMap, Skf3030Sc002CommonDto comDto) {

		if (resultMap.containsKey("sc006TyusyaMonthPayAfter")) {
			comDto.setSc006TyusyaMonthPayAfter(resultMap.get("sc006TyusyaMonthPayAfter"));
		}
		if (resultMap.containsKey("sc006SiyoryoHiwariPay")) {
			comDto.setSc006SiyoryoHiwariPay(resultMap.get("sc006SiyoryoHiwariPay"));
		}
		if (resultMap.containsKey("sc006SyatauMonthPayAfter")) {
			comDto.setSc006SyatauMonthPayAfter(resultMap.get("sc006SyatauMonthPayAfter"));
		}
		if (resultMap.containsKey("sc006ShiyoryoTsukigaku")) {
			comDto.setSc006ShiyoryoTsukigaku(resultMap.get("sc006ShiyoryoTsukigaku"));
		}
		if (resultMap.containsKey("sc006TyusyaDayPayOne")) {
			comDto.setSc006TyusyaDayPayOne(resultMap.get("sc006TyusyaDayPayOne"));
		}
		if (resultMap.containsKey("sc006TyusyaMonthPayOne")) {
			comDto.setSc006TyusyaMonthPayOne(resultMap.get("sc006TyusyaMonthPayOne"));
		}
		if (resultMap.containsKey("sc006TyusyaDayPayTwo")) {
			comDto.setSc006TyusyaDayPayTwo(resultMap.get("sc006TyusyaDayPayTwo"));
		}
		if (resultMap.containsKey("sc006TyusyaMonthPayTwo")) {
			comDto.setSc006TyusyaMonthPayTwo(resultMap.get("sc006TyusyaMonthPayTwo"));
		}
		
		if (resultMap.containsKey("hdnKaiSanAfterShatakuShiyoryoGetsugaku")) {
			comDto.setHdnKaiSanAfterShatakuShiyoryoGetsugaku(resultMap.get("hdnKaiSanAfterShatakuShiyoryoGetsugaku"));
		}
		if (resultMap.containsKey("hdnKaiSanAfterShatakuShiyoryoHiwariKingaku")) {
			comDto.setHdnKaiSanAfterShatakuShiyoryoHiwariKingaku(resultMap.get("hdnKaiSanAfterShatakuShiyoryoHiwariKingaku"));
		}
		if (resultMap.containsKey("hdnKaiSanAfterShatakuShiyoryoGetsugakuChoseigo")) {
			comDto.setHdnKaiSanAfterShatakuShiyoryoGetsugakuChoseigo(resultMap.get("hdnKaiSanAfterShatakuShiyoryoGetsugakuChoseigo"));
		}
		if (resultMap.containsKey("hdnKaiSanAfterChushajoShiyoryoGetsugakuChoseigo")) {
			comDto.setHdnKaiSanAfterChushajoShiyoryoGetsugakuChoseigo(resultMap.get("hdnKaiSanAfterChushajoShiyoryoGetsugakuChoseigo"));
		}
		if (resultMap.containsKey("hdnKaiSanAfterKukaku1ChushajoShiyoroGetsugaku")) {
			comDto.setHdnKaiSanAfterKukaku1ChushajoShiyoroGetsugaku(resultMap.get("hdnKaiSanAfterKukaku1ChushajoShiyoroGetsugaku"));
		}
		if (resultMap.containsKey("hdnKaiSanAfterKukaku1ChushajoShiyoroHiwariKingaku")) {
			comDto.setHdnKaiSanAfterKukaku1ChushajoShiyoroHiwariKingaku(resultMap.get("hdnKaiSanAfterKukaku1ChushajoShiyoroHiwariKingaku"));
		}
		if (resultMap.containsKey("hdnKaiSanAfterKukaku2ChushajoShiyoroGetsugaku")) {
			comDto.setHdnKaiSanAfterKukaku2ChushajoShiyoroGetsugaku(resultMap.get("hdnKaiSanAfterKukaku2ChushajoShiyoroGetsugaku"));
		}
		if (resultMap.containsKey("hdnKaiSanAfterKukaku2ChushajoShiyoroHiwariKingaku")) {
			comDto.setHdnKaiSanAfterKukaku2ChushajoShiyoroHiwariKingaku(resultMap.get("hdnKaiSanAfterKukaku2ChushajoShiyoroHiwariKingaku"));
		}
		if (resultMap.containsKey("hdnKaiSanAfterKojinFutanKyoekihiGetsugakuChoseigo")) {
			comDto.setHdnKaiSanAfterKojinFutanKyoekihiGetsugakuChoseigo(resultMap.get("hdnKaiSanAfterKojinFutanKyoekihiGetsugakuChoseigo"));
		}
		if (resultMap.containsKey("sc006KyoekihiPayAfter")) {
			comDto.setSc006KyoekihiPayAfter(resultMap.get("sc006KyoekihiPayAfter"));
		}
	}

	/**
	 * 使用料計算(提示データ登録内部)戻り値DTO設定(非同期)
	 * 使用料計算の戻り値をDTOに設定する
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param resultMap 使用料計算結果Map
	 * @param asyncDto	*DTO
	 * @return			パラメータマップ
	 */
	public void setSiyoryoKeiSanParamAsync(Map<String, String> resultMap, Skf3030Sc002CommonAsyncDto asyncDto) {

		if (resultMap.containsKey("sc006TyusyaMonthPayAfter")) {
			asyncDto.setSc006TyusyaMonthPayAfter(resultMap.get("sc006TyusyaMonthPayAfter"));
		}
		if (resultMap.containsKey("sc006SiyoryoHiwariPay")) {
			asyncDto.setSc006SiyoryoHiwariPay(resultMap.get("sc006SiyoryoHiwariPay"));
		}
		if (resultMap.containsKey("sc006SyatauMonthPayAfter")) {
			asyncDto.setSc006SyatauMonthPayAfter(resultMap.get("sc006SyatauMonthPayAfter"));
		}
		if (resultMap.containsKey("sc006ShiyoryoTsukigaku")) {
			asyncDto.setSc006ShiyoryoTsukigaku(resultMap.get("sc006ShiyoryoTsukigaku"));
		}
		if (resultMap.containsKey("sc006TyusyaDayPayOne")) {
			asyncDto.setSc006TyusyaDayPayOne(resultMap.get("sc006TyusyaDayPayOne"));
		}
		if (resultMap.containsKey("sc006TyusyaMonthPayOne")) {
			asyncDto.setSc006TyusyaMonthPayOne(resultMap.get("sc006TyusyaMonthPayOne"));
		}
		if (resultMap.containsKey("sc006TyusyaDayPayTwo")) {
			asyncDto.setSc006TyusyaDayPayTwo(resultMap.get("sc006TyusyaDayPayTwo"));
		}
		if (resultMap.containsKey("sc006TyusyaMonthPayTwo")) {
			asyncDto.setSc006TyusyaMonthPayTwo(resultMap.get("sc006TyusyaMonthPayTwo"));
		}
		
		if (resultMap.containsKey("hdnKaiSanAfterShatakuShiyoryoGetsugaku")) {
			asyncDto.setHdnKaiSanAfterShatakuShiyoryoGetsugaku(resultMap.get("hdnKaiSanAfterShatakuShiyoryoGetsugaku"));
		}
		if (resultMap.containsKey("hdnKaiSanAfterShatakuShiyoryoHiwariKingaku")) {
			asyncDto.setHdnKaiSanAfterShatakuShiyoryoHiwariKingaku(resultMap.get("hdnKaiSanAfterShatakuShiyoryoHiwariKingaku"));
		}
		if (resultMap.containsKey("hdnKaiSanAfterShatakuShiyoryoGetsugakuChoseigo")) {
			asyncDto.setHdnKaiSanAfterShatakuShiyoryoGetsugakuChoseigo(resultMap.get("hdnKaiSanAfterShatakuShiyoryoGetsugakuChoseigo"));
		}
		if (resultMap.containsKey("hdnKaiSanAfterChushajoShiyoryoGetsugakuChoseigo")) {
			asyncDto.setHdnKaiSanAfterChushajoShiyoryoGetsugakuChoseigo(resultMap.get("hdnKaiSanAfterChushajoShiyoryoGetsugakuChoseigo"));
		}
		if (resultMap.containsKey("hdnKaiSanAfterKukaku1ChushajoShiyoroGetsugaku")) {
			asyncDto.setHdnKaiSanAfterKukaku1ChushajoShiyoroGetsugaku(resultMap.get("hdnKaiSanAfterKukaku1ChushajoShiyoroGetsugaku"));
		}
		if (resultMap.containsKey("hdnKaiSanAfterKukaku1ChushajoShiyoroHiwariKingaku")) {
			asyncDto.setHdnKaiSanAfterKukaku1ChushajoShiyoroHiwariKingaku(resultMap.get("hdnKaiSanAfterKukaku1ChushajoShiyoroHiwariKingaku"));
		}
		if (resultMap.containsKey("hdnKaiSanAfterKukaku2ChushajoShiyoroGetsugaku")) {
			asyncDto.setHdnKaiSanAfterKukaku2ChushajoShiyoroGetsugaku(resultMap.get("hdnKaiSanAfterKukaku2ChushajoShiyoroGetsugaku"));
		}
		if (resultMap.containsKey("hdnKaiSanAfterKukaku2ChushajoShiyoroHiwariKingaku")) {
			asyncDto.setHdnKaiSanAfterKukaku2ChushajoShiyoroHiwariKingaku(resultMap.get("hdnKaiSanAfterKukaku2ChushajoShiyoroHiwariKingaku"));
		}
		if (resultMap.containsKey("hdnKaiSanAfterKojinFutanKyoekihiGetsugakuChoseigo")) {
			asyncDto.setHdnKaiSanAfterKojinFutanKyoekihiGetsugakuChoseigo(resultMap.get("hdnKaiSanAfterKojinFutanKyoekihiGetsugakuChoseigo"));
		}
		if (resultMap.containsKey("sc006KyoekihiPayAfter")) {
			asyncDto.setSc006KyoekihiPayAfter(resultMap.get("sc006KyoekihiPayAfter"));
		}
	}

	/**
	 * 使用料計算(提示データ登録内部)戻り値初期化(非同期)
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param asyncDto	*DTO
	 * @return			パラメータマップ
	 */
	public void initializeSiyoryoKeiSanParamAsync(Skf3030Sc002CommonAsyncDto asyncDto) {

		asyncDto.setSc006TyusyaMonthPayAfter(null);
		asyncDto.setSc006SiyoryoHiwariPay(null);
		asyncDto.setSc006SyatauMonthPayAfter(null);
		asyncDto.setSc006ShiyoryoTsukigaku(null);
		asyncDto.setSc006TyusyaDayPayOne(null);
		asyncDto.setSc006TyusyaMonthPayOne(null);
		asyncDto.setSc006TyusyaDayPayTwo(null);
		asyncDto.setSc006TyusyaMonthPayTwo(null);
	
		asyncDto.setSc006KyoekihiPayAfter(null);
		asyncDto.setHdnKaiSanAfterShatakuShiyoryoGetsugaku(null);
		asyncDto.setHdnKaiSanAfterShatakuShiyoryoHiwariKingaku(null);
		asyncDto.setHdnKaiSanAfterShatakuShiyoryoGetsugakuChoseigo(null);
		asyncDto.setHdnKaiSanAfterChushajoShiyoryoGetsugakuChoseigo(null);
		asyncDto.setHdnKaiSanAfterKukaku1ChushajoShiyoroGetsugaku(null);
		asyncDto.setHdnKaiSanAfterKukaku1ChushajoShiyoroHiwariKingaku(null);
		asyncDto.setHdnKaiSanAfterKukaku2ChushajoShiyoroGetsugaku(null);
		asyncDto.setHdnKaiSanAfterKukaku2ChushajoShiyoroHiwariKingaku(null);
		asyncDto.setHdnKaiSanAfterKojinFutanKyoekihiGetsugakuChoseigo(null);
		
	}
	
	
	/**
	 * 使用料入力値の形式チェック
	 * @param payStr
	 * @return
	 */
	private boolean checkPayInput(String payStr,int size) throws Exception{
		boolean isResult = false;
	
		//空文字はスルー
		if(SkfCheckUtils.isNullOrEmpty(payStr)){
			return true;
		}
		
		if(CheckUtils.isMoreThanByteSize(payStr, size)){
			//指定桁オーバー
			return false;
		}
		//形式チェック
		isResult = payStr.matches("^(0)$|^(-?[1-9]+[0-9]*)$");
			
		return isResult;
	}
	
	/**
	 * 使用料の計算(同期)
	 * @param comDto
	 */
	public void siyoryoKeiSanSync(Skf3030Sc002CommonDto comDto)  throws Exception{
		
		//計算前の入力チェック
		Boolean errorFlg = false;
		
		//社宅使用料調整金額の入力チェック
		if(!checkPayInput(comDto.getSc006SiyoroTyoseiPay(), 6)){
			comDto.setSc006SiyoroTyoseiPayErr(CodeConstant.NFW_VALIDATION_ERROR);
			errorFlg = true;
		}
		//駐車場使用料調整金額の入力チェック
		if(!checkPayInput(comDto.getSc006TyusyaTyoseiPay(), 6)){
			comDto.setSc006TyusyaTyoseiPayErr(CodeConstant.NFW_VALIDATION_ERROR);
			errorFlg = true;
		}
		//社宅賃貸料の入力チェック
		if(!checkPayInput(comDto.getSc006ChintaiRyo(), 6)){
			comDto.setSc006ChintaiRyoErr(CodeConstant.NFW_VALIDATION_ERROR);
			errorFlg = true;
		}
		//駐車場賃貸料の入力チェック
		if(!checkPayInput(comDto.getSc006TyusyajoRyokin(), 6)){
			comDto.setSc006TyusyajoRyokinErr(CodeConstant.NFW_VALIDATION_ERROR);
			errorFlg = true;
		}
		//個人負担共益費月額の入力チェック
		if(!checkPayInput(comDto.getSc006KyoekihiMonthPay(), 6)){
				comDto.setSc006KyoekihiMonthPayErr(CodeConstant.NFW_VALIDATION_ERROR);
				errorFlg = true;
		}
		//個人負担共益費調整金額の入力チェック
		if(!checkPayInput(comDto.getSc006KyoekihiTyoseiPay(), 6)){
				comDto.setSc006KyoekihiTyoseiPayErr(CodeConstant.NFW_VALIDATION_ERROR);
				errorFlg = true;
		}
		//共益費（事業者負担）の入力チェック（2016.06.10 提示テータ画面と動作を合わせる）
		if(!checkPayInput(comDto.getSc006Kyoekihi(), 6)){
				comDto.setSc006KyoekihiErr(CodeConstant.NFW_VALIDATION_ERROR);
				errorFlg = true;
		}

		if(errorFlg){
			return;
		}
		
		Map<String, String> paramMap = createSiyoryoKeiSanParam(comDto);
		Map<String, String> resultMap = new HashMap<String, String>();
		StringBuffer errMsg = new StringBuffer();
		siyoryoKeiSan(paramMap, resultMap, errMsg, false);
		
		if(errMsg != null){	
			if(!SkfCheckUtils.isNullOrEmpty(errMsg.toString())){
				ServiceHelper.addErrorResultMessage(comDto, null,MessageIdConstant.SKF3020_ERR_MSG_COMMON,errMsg.toString().substring(11));
				return;
			}
		}
		
		setSiyoryoKeiSanParam(resultMap, comDto);
	}
	
	/**
	 * 使用料の計算(非同期)
	 * @param comDto
	 * @throws Exception
	 */
	public void siyoryoKeiSanAsync(Skf3030Sc002CommonAsyncDto asyncDto)  throws Exception{
		
		//計算前の入力チェック
		//入力側で制御
		
		// 使用料計算用Map
		Map<String, String> paramMap = asyncDto.getMapParam();
		// 戻り値初期化
		initializeSiyoryoKeiSanParamAsync(asyncDto);
		// 使用料計算処理
		Map<String, String> resultMap = new HashMap<String, String>();	// 使用料計算戻り値
		StringBuffer errMsg = new StringBuffer();						// エラーメッセージ
		siyoryoKeiSan(paramMap, resultMap, errMsg, false);
		// 使用料計算でエラー
		if(errMsg != null){	
			if(!SkfCheckUtils.isNullOrEmpty(errMsg.toString())){
				ServiceHelper.addErrorResultMessage(asyncDto, null, MessageIdConstant.SKF3020_ERR_MSG_COMMON, errMsg.toString().substring(11));
				return;
			}
		}
	
		// 使用料計算戻り値設定
		setSiyoryoKeiSanParamAsync(resultMap, asyncDto);
		
	}
	
	/**
	 * 使用料の計算
	 * @param paramMap
	 * @param resultMap
	 * @param errMsg
	 * @param lblFlg
	 * @throws Exception
	 */
	public void siyoryoKeiSan(Map<String, String> paramMap, Map<String, String> resultMap, StringBuffer errMsg , Boolean lblFlg) throws Exception{

		//計算前の入力チェック→呼び出し元で考える

		resultMap.clear();
		
		SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp outputEntity = new SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp();
		//駐車場使用料
		String chushajoRiyoOne = CodeConstant.STRING_ZERO;
		String chushajoRiyoTwo = CodeConstant.STRING_ZERO;
		//社宅使用料
		String shatakuRiyoryou = CodeConstant.STRING_ZERO;

		//社宅使用料計算
		if(!SkfCheckUtils.isNullOrEmpty(paramMap.get("hdnRentalPatternId"))){
			outputEntity = comSiyoryoKeiSan(paramMap, "");
			//正常に計算できていたら、値をセット
			if(SkfCheckUtils.isNullOrEmpty(outputEntity.getErrMessage())){
				//社宅使用料月額
				shatakuRiyoryou = outputEntity.getShatakuShiyouryouGetsugaku().toPlainString();
				resultMap.put("hdnKaiSanAfterShatakuShiyoryoGetsugaku",shatakuRiyoryou);
				resultMap.put("sc006ShiyoryoTsukigaku",convertYenFormat(Integer.parseInt(shatakuRiyoryou), false));
				//画面初期表示する時
				if(lblFlg){ 
					return;
				}
			}else{
				errMsg.append(outputEntity.getErrMessage());
				//ServiceHelper.addErrorResultMessage(comDto, null, outputEntity.getErrMessage());
				LogUtils.infoByMsg("siyoryoKeiSan, 使用料計算失敗①:" + outputEntity.getErrMessage());
				return;
			}

			//予定日入力されている場合
			String hdnNengetsu = paramMap.get("hdnNengetsu");
			String nyukyoYoteibi = getDateText(paramMap.get("sc006NyukyoYoteiDay"));
			String taikyoYoteibi = getDateText(paramMap.get("sc006TaikyoYoteiDay"));
			if(!SkfCheckUtils.isNullOrEmpty(taikyoYoteibi)){
				//退居予定日から日割を算出
				resultMap.put("hdnKaiSanAfterShatakuShiyoryoHiwariKingaku",hiwariKeisan(nyukyoYoteibi, taikyoYoteibi, shatakuRiyoryou,hdnNengetsu));
				resultMap.put("sc006SiyoryoHiwariPay",convertYenFormat(Integer.parseInt(resultMap.get("hdnKaiSanAfterShatakuShiyoryoHiwariKingaku")), false));
	
				//社宅使用料月額（調整後） = 社宅使用料日割金額 + 社宅使用料調整金額
				Integer choseigo = Integer.parseInt(resultMap.get("hdnKaiSanAfterShatakuShiyoryoHiwariKingaku")) + Integer.parseInt(getPayText(paramMap.get("sc006SiyoroTyoseiPay")));
				resultMap.put("hdnKaiSanAfterShatakuShiyoryoGetsugakuChoseigo",getToString(choseigo));
				resultMap.put("sc006SyatauMonthPayAfter",convertYenFormat(choseigo,false));
			}else{
				//退居日未入力
				//入居予定日が入力されている場合
				if(!SkfCheckUtils.isNullOrEmpty(nyukyoYoteibi)){
					resultMap.put("hdnKaiSanAfterShatakuShiyoryoHiwariKingaku",hiwariKeisan(nyukyoYoteibi, taikyoYoteibi, shatakuRiyoryou,hdnNengetsu));
					resultMap.put("sc006SiyoryoHiwariPay",convertYenFormat(Integer.parseInt(resultMap.get("hdnKaiSanAfterShatakuShiyoryoHiwariKingaku")), false));
				
					//社宅使用料月額（調整後） = 社宅使用料日割金額 + 社宅使用料調整金額
					Integer choseigo = Integer.parseInt(resultMap.get("hdnKaiSanAfterShatakuShiyoryoHiwariKingaku")) + Integer.parseInt(getPayText(paramMap.get("sc006SiyoroTyoseiPay")));
					resultMap.put("hdnKaiSanAfterShatakuShiyoryoGetsugakuChoseigo",getToString(choseigo));
					resultMap.put("sc006SyatauMonthPayAfter",convertYenFormat(choseigo,false));
				}
			}

		}
		else{
			resultMap.put("hdnKaiSanAfterShatakuShiyoryoGetsugaku",CodeConstant.STRING_ZERO);
			resultMap.put("hdnKaiSanAfterShatakuShiyoryoHiwariKingaku",CodeConstant.STRING_ZERO);
		}

		//共益費計算
		kyoekihiKeiSan(paramMap,resultMap);

		//駐車場使用料月額（調整後）を”0”に設定する
		resultMap.put("hdnKaiSanAfterChushajoShiyoryoGetsugakuChoseigo",CodeConstant.STRING_ZERO);

		//駐車場使用料１計算
		if(!SkfCheckUtils.isNullOrEmpty(paramMap.get("hdnChushajoKanriNo1"))){
			outputEntity = comSiyoryoKeiSan(paramMap,paramMap.get("hdnChushajoKanriNo1"));
			//正常に計算できていたら、値をセット
			if(SkfCheckUtils.isNullOrEmpty(outputEntity.getErrMessage())){
				//駐車場使用料１
				chushajoRiyoOne = outputEntity.getChushajouShiyoryou().toPlainString();
				resultMap.put("hdnKaiSanAfterKukaku1ChushajoShiyoroGetsugaku",chushajoRiyoOne);
				//駐車場使用料月額１（役員区分を考慮する）
				resultMap.put("sc006TyusyaMonthPayOne",convertYenFormat(outputEntity.getChushajouShiyoryou().intValue(), false));
			}else{
				errMsg.append(outputEntity.getErrMessage());
				//ServiceHelper.addErrorResultMessage(comDto, null, outputEntity.getErrMessage());
				LogUtils.infoByMsg("siyoryoKeiSan, 使用料計算失敗②:" + outputEntity.getErrMessage());
				return;
			}
			//利用終了日（区画１）が入力された場合
			String kukaku1RiyoKaishibi = getDateText(paramMap.get("sc006RiyouStartDayOne"));
			String kukaku1RiyoShuryobi = getDateText(paramMap.get("sc006RiyouEndDayOne"));
			if(!SkfCheckUtils.isNullOrEmpty(kukaku1RiyoShuryobi)){
				//駐車場使用料日割金額（区画１）
				resultMap.put("hdnKaiSanAfterKukaku1ChushajoShiyoroHiwariKingaku",hiwariKeisan(kukaku1RiyoKaishibi, kukaku1RiyoShuryobi, chushajoRiyoOne, paramMap.get("hdnNengetsu")));
				resultMap.put("sc006TyusyaDayPayOne",convertYenFormat(Integer.parseInt(resultMap.get("hdnKaiSanAfterKukaku1ChushajoShiyoroHiwariKingaku")), false));
			}else{
				//利用終了日（区画１）未入力
				//利用開始日（区画１）が変更された場合
				if(!SkfCheckUtils.isNullOrEmpty(kukaku1RiyoKaishibi)){
					//駐車場使用料日割金額（区画１）
					resultMap.put("hdnKaiSanAfterKukaku1ChushajoShiyoroHiwariKingaku",hiwariKeisan(kukaku1RiyoKaishibi, kukaku1RiyoShuryobi, chushajoRiyoOne, paramMap.get("hdnNengetsu")));
				}else{
					resultMap.put("hdnKaiSanAfterKukaku1ChushajoShiyoroHiwariKingaku",getPayText(paramMap.get("Sc006TyusyaDayPayOne")));
				}
				resultMap.put("sc006TyusyaDayPayOne",convertYenFormat(Integer.parseInt(resultMap.get("hdnKaiSanAfterKukaku1ChushajoShiyoroHiwariKingaku")),false));
			}
			//駐車場使用料月額（調整後） = 駐車場使用料月額（調整後） + 駐車場使用料日割金額（区画1）
			Integer hdnKaiSanAfterChushajoShiyoryoGetsugakuChoseigo = Integer.parseInt(resultMap.get("hdnKaiSanAfterChushajoShiyoryoGetsugakuChoseigo")) + Integer.parseInt(resultMap.get("hdnKaiSanAfterKukaku1ChushajoShiyoroHiwariKingaku"));
			resultMap.put("hdnKaiSanAfterChushajoShiyoryoGetsugakuChoseigo",getToString(hdnKaiSanAfterChushajoShiyoryoGetsugakuChoseigo));
		}else{
			resultMap.put("hdnKaiSanAfterKukaku1ChushajoShiyoroGetsugaku",CodeConstant.STRING_ZERO);
			resultMap.put("hdnKaiSanAfterKukaku1ChushajoShiyoroHiwariKingaku",CodeConstant.STRING_ZERO);
		}

		//駐車場使用料２計算
		if(!SkfCheckUtils.isNullOrEmpty(paramMap.get("hdnChushajoKanriNo2"))){
			outputEntity = comSiyoryoKeiSan(paramMap, paramMap.get("hdnChushajoKanriNo2"));
			//正常に計算できていたら、値をセット
			if(SkfCheckUtils.isNullOrEmpty(outputEntity.getErrMessage())){
				//駐車場使用料２
				chushajoRiyoTwo = outputEntity.getChushajouShiyoryou().toPlainString();
				resultMap.put("hdnKaiSanAfterKukaku2ChushajoShiyoroGetsugaku",chushajoRiyoTwo);
				//駐車場使用料２（役員区分を考慮する）
				resultMap.put("sc006TyusyaMonthPayTwo",convertYenFormat(outputEntity.getChushajouShiyoryou().intValue(), false));
			}else{
				errMsg.append(outputEntity.getErrMessage());
				//ServiceHelper.addErrorResultMessage(comDto, null, outputEntity.getErrMessage());
				LogUtils.infoByMsg("siyoryoKeiSan, 使用料計算失敗③:" + outputEntity.getErrMessage());
				return;
			}
			//利用終了日（区画２）が入力された場合
			String kukaku2RiyoKaishibi = getDateText(paramMap.get("sc006RiyouStartDayTwo"));
			String kukaku2RiyoShuryobi = getDateText(paramMap.get("sc006RiyouEndDayTwo"));
			if(!SkfCheckUtils.isNullOrEmpty(kukaku2RiyoShuryobi)){
				//駐車場使用料日割金額（区画２）
				resultMap.put("hdnKaiSanAfterKukaku2ChushajoShiyoroHiwariKingaku",hiwariKeisan(kukaku2RiyoKaishibi, kukaku2RiyoShuryobi, chushajoRiyoTwo, paramMap.get("hdnNengetsu")));
				resultMap.put("sc006TyusyaDayPayTwo",convertYenFormat(Integer.parseInt(resultMap.get("hdnKaiSanAfterKukaku2ChushajoShiyoroHiwariKingaku")), false));
			}else{
				//利用終了日（区画２）未入力
				//利用開始日（区画２）が変更された場合
				if(!SkfCheckUtils.isNullOrEmpty(kukaku2RiyoKaishibi)){
					//駐車場使用料日割金額（区画２）
					resultMap.put("hdnKaiSanAfterKukaku2ChushajoShiyoroHiwariKingaku",hiwariKeisan(kukaku2RiyoKaishibi, kukaku2RiyoShuryobi, chushajoRiyoTwo, paramMap.get("hdnNengetsu")));
				}else{
					resultMap.put("hdnKaiSanAfterKukaku2ChushajoShiyoroHiwariKingaku",getPayText(paramMap.get("sc006TyusyaDayPayTwo")));
				}
				resultMap.put("sc006TyusyaDayPayTwo",convertYenFormat(Integer.parseInt(resultMap.get("hdnKaiSanAfterKukaku2ChushajoShiyoroHiwariKingaku")), false));
			}
			//駐車場使用料月額（調整後） = 駐車場使用料月額（調整後） + 駐車場使用料日割金額（区画２）
			Integer hdnKaiSanAfterChushajoShiyoryoGetsugakuChoseigo = Integer.parseInt(resultMap.get("hdnKaiSanAfterChushajoShiyoryoGetsugakuChoseigo")) + Integer.parseInt(resultMap.get("hdnKaiSanAfterKukaku2ChushajoShiyoroHiwariKingaku"));
			resultMap.put("hdnKaiSanAfterChushajoShiyoryoGetsugakuChoseigo",getToString(hdnKaiSanAfterChushajoShiyoryoGetsugakuChoseigo));
		}else{
			resultMap.put("hdnKaiSanAfterKukaku2ChushajoShiyoroGetsugaku",CodeConstant.STRING_ZERO);
			resultMap.put("hdnKaiSanAfterKukaku2ChushajoShiyoroHiwariKingaku",CodeConstant.STRING_ZERO);
		}

		//駐車場使用料月額（調整後） =  駐車場使用料月額（調整後） + 駐車場使用料調整金額
		Integer setValue = Integer.parseInt(resultMap.get("hdnKaiSanAfterChushajoShiyoryoGetsugakuChoseigo")) + Integer.parseInt(getPayText(paramMap.get("sc006TyusyaTyoseiPay")));
		resultMap.put("hdnKaiSanAfterChushajoShiyoryoGetsugakuChoseigo",getToString(setValue));
		resultMap.put("sc006TyusyaMonthPayAfter",convertYenFormat(Integer.parseInt(resultMap.get("hdnKaiSanAfterChushajoShiyoryoGetsugakuChoseigo")), false));

	}
	
	/**
	 * 使用料の計算
	 * @param comDto
	 * @param chushajoKanriNo 駐車場管理番号
	 * @return 社宅使用料計算情報結果保持クラス
	 * @throws Exception
	 */
	private SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp comSiyoryoKeiSan(Map<String, String> paramMap, String chushajoKanriNo) throws Exception{

		//使用料計算に必要な項目を格納
		SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp inputEntity = new SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp();
		SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp outputEntity = new SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp();
		//String shatakuRiyoryou = CodeConstant.DOUBLE_QUOTATION;
		//String chushajoRiyoryou = CodeConstant.DOUBLE_QUOTATION;
		//処理年月
		inputEntity.setShoriNengetsu(skfBaseBusinessLogicUtils.getSystemProcessNenGetsu());
		//役員区分
		inputEntity.setYakuinKbn(paramMap.get("sc006YakuinSanteiSelect") );
		if(SkfCheckUtils.isNullOrEmpty(chushajoKanriNo)){
			
			//処理区分
			inputEntity.setShoriKbn("1");

			//使用料計算に必要な情報を取得
			List<Skf3030Sc002GetShatakuDataExp> dt = new ArrayList<Skf3030Sc002GetShatakuDataExp>();
			dt = skf3030Sc002GetShatakuDataExpRepository.getShatakuData(Long.parseLong(paramMap.get("hdnShatakuKanriNo")));
			if(dt.size() > 0){
				//建築年月日
				inputEntity.setKenchikuNengappi(dt.get(0).getBuildDate());
				//地域区分
				inputEntity.setAreaKbn(dt.get(0).getAreaKbn());
				//構造区分
				inputEntity.setStructureKbn(dt.get(0).getStructureKbn());
				//駐車場構造区分
				inputEntity.setChuushajoKouzouKbn(dt.get(0).getParkingStructureKbn());
			}

//		    //セッションの使用料情報を取得
//		    Dim shiyoryokeisanPatternShienInfo As New ShiyoryokeisanShienOutputEntity()
//		    shiyoryokeisanPatternShienInfo = DirectCast(Me.Session.Item(Constant.SessionId.SHIYORYO_KEISAN_SHIEN_OUTPUT_INFO),  _
//								ShiyoryokeisanShienOutputEntity)
//		    //セッションの使用料情報が設定されている場合
//		    If shiyoryokeisanPatternShienInfo IsNot Nothing Then
			//用途区分
			inputEntity.setAuseKbn(getMensekiText(paramMap.get("hdnRateShienYoto")));
			//延べ面積
			inputEntity.setNobeMenseki(getMensekiText(paramMap.get("hdnRateShienNobeMenseki")));
			//サンルーム面積
			inputEntity.setSunroomMenseki(getMensekiText(paramMap.get("hdnRateShienSunroomMenseki")));
			//階段面積
			inputEntity.setKaidanMenseki(getMensekiText(paramMap.get("hdnRateShienKaidanMenseki")));
			//物置面積
			inputEntity.setMonookiMenseki(getMensekiText(paramMap.get("hdnRateShienMonookiMenseki")));
			//基準使用料算定上延べ面積
			inputEntity.setKijunMenseki(getMensekiText(paramMap.get("hdnRateShienKijunMenseki")));
			//社宅使用料算定上延べ面積
			inputEntity.setShatakuMenseki(getMensekiText(paramMap.get("hdnRateShienShatakuMenseki")));
//		    End If

			//生年月日
			inputEntity.setSeinengappi(paramMap.get("hdnBirthday"));
			//社宅賃貸料
			inputEntity.setShatakuChintairyou(getPayText(paramMap.get("sc006ChintaiRyo")));
			//駐車場賃貸料
			inputEntity.setChyshajoChintairyou(getPayText(paramMap.get("sc006TyusyajoRyokin")));
		}else{
				//処理区分
				inputEntity.setShoriKbn("4");
				//駐車場賃貸料
				inputEntity.setChyshajoChintairyou(getPayText(paramMap.get("sc006TyusyajoRyokin")));
				//駐車場管理番号
				inputEntity.setChushajoKanriBangou(chushajoKanriNo);
				//社宅管理番号
				inputEntity.setShatakuKanriBangou(paramMap.get("hdnShatakuKanriNo"));
//			    //セッションの使用料情報を取得
//			    Dim shiyoryokeisanPatternShienInfo As New ShiyoryokeisanShienOutputEntity()
//			    shiyoryokeisanPatternShienInfo = DirectCast(Me.Session.Item(Constant.SessionId.SHIYORYO_KEISAN_SHIEN_OUTPUT_INFO),  _
//									ShiyoryokeisanShienOutputEntity)
//			    //セッションの使用料情報が設定されている場合
//			    If shiyoryokeisanPatternShienInfo IsNot Nothing Then
				//用途区分
			    inputEntity.setAuseKbn(paramMap.get("hdnRateShienYoto"));
	//		    End If
	
		}
		// 使用料計算結果取得
		outputEntity = skfBaseBusinessLogicUtils.getShatakuShiyouryouKeisan(inputEntity);
		return outputEntity;
	}
	
	/**
	 * 日割計算()
	 * @param startDate
	 * @param endDate
	 * @param val
	 * @param nengetu hdnNengetsuの値
	 * @return
	 */
	private String hiwariKeisan(String startDate, String endDate, String val,String nengetu){
		// 戻り値
		String hiwari = CodeConstant.STRING_ZERO;
		// 日付が空白の場合、"0"を返却する
		if (CheckUtils.isEmpty(startDate)) {
			return hiwari;
		}
		// 最大終了日を設定する
		if (CheckUtils.isEmpty(endDate)) {
			endDate = MAX_END_DATE;
		}
		
		//開始日、終了日が日付形式で無い時、0円を返却する
		if(!SkfCheckUtils.isSkfDateFormat(startDate, CheckUtils.DateFormatType.YYYYMMDD) ||
			!SkfCheckUtils.isSkfDateFormat(endDate, CheckUtils.DateFormatType.YYYYMMDD)){
			return hiwari;
		}
		
		//入居日数算出
		int nisu = 0;
		int nisuMonth = YearMonth.of(Integer.parseInt(nengetu.substring(0, 4)),
				Integer.parseInt(nengetu.substring(4, 6))).lengthOfMonth();
		/** 計算開始日 */
		// 開始年月日の日
		int startDay = Integer.parseInt(startDate.substring(6, 8));
		// 開始年月日の年月
		String startYearMonth = startDate.substring(0, 6);
		// 開始年月の月の日数
		int dayOfMonthStart = YearMonth.of(Integer.parseInt(startDate.substring(0, 4)),
								Integer.parseInt(startDate.substring(4, 6))).lengthOfMonth();
		/** 計算終了日 */
		// 終了年月日の日
		int endDay = Integer.parseInt(endDate.substring(6, 8));
		// 終了年月日の年月
		String endYearMonth = endDate.substring(0, 6);
//		// 終了年月日の月の日数
//		int dayOfMonthEnd = YearMonth.of(Integer.parseInt(endDate.substring(0, 4)),
//							Integer.parseInt(endDate.substring(4, 6))).lengthOfMonth();
		
		// 処理年月をもとに、使用料計算日数を計算する
		if (Objects.equals(nengetu, startYearMonth) && Objects.equals(nengetu, endYearMonth)) {
			LogUtils.debugByMsg("処理年月と計算開始年月が同年同月、且つ、計算開始年月と計算終了年月が同一");
			nisu = endDay - startDay + 1;
		} else if (Objects.equals(nengetu, startYearMonth) && !Objects.equals(nengetu, endYearMonth)) {
			LogUtils.debugByMsg("処理年月と計算開始年月が同年同月、且つ、計算開始年月と計算終了年月が異なる");
			nisu = dayOfMonthStart - startDay + 1;
		} else if (!Objects.equals(nengetu, startYearMonth) && Objects.equals(nengetu, endYearMonth)) {
			LogUtils.debugByMsg("処理年月と計算開始年月が異なる、且つ、計算開始年月と計算終了年月が同一");
			nisu = endDay;
		} else if (Integer.parseInt(nengetu) < Integer.parseInt(startYearMonth)) {
			LogUtils.debugByMsg("処理年月より計算開始年月が未来");
			nisu = 0;
		} else if (Integer.parseInt(nengetu) > Integer.parseInt(endYearMonth)) {
			LogUtils.debugByMsg("計算終了年月より処理年月が未来");
			nisu = 0;
		} else {
			LogUtils.debugByMsg("その他");
			nisu = nisuMonth;
		}
		
		/** 日割金額算出 */
		if (nisuMonth != 0) {
			// 当月利用日数 * 使用料月額 / 当月(処理年月)の日数
			hiwari = Integer.toString(nisu * Integer.parseInt(val) / nisuMonth);
		}
		
		return hiwari;
	}
	
	/**
	 * 個人共益費計算
	 * @param comDto
	 */
	private void kyoekihiKeiSan(Map<String, String> paramMap, Map<String, String> resultMap){
		
		//個人負担共益費月額（調整後）の計算
		Integer valKyoekihiGetsugaku = 0;
		
		valKyoekihiGetsugaku = Integer.parseInt(getPayText(paramMap.get("sc006KyoekihiMonthPay")));
		
		Integer valKyoekihiChosei = Integer.parseInt(getPayText(paramMap.get("sc006KyoekihiTyoseiPay")));
		Integer valKyoekihiChoseigo = 0;

		//個人負担共益費月額（調整後） = 個人負担共益費月額 + 個人負担共益費調整金額
		valKyoekihiChoseigo = valKyoekihiGetsugaku + valKyoekihiChosei;

		resultMap.put("hdnKaiSanAfterKojinFutanKyoekihiGetsugakuChoseigo",getToString(valKyoekihiChoseigo));
		resultMap.put("sc006KyoekihiPayAfter",convertYenFormat(valKyoekihiChoseigo,false));

	}
	
	/**
	 * 金額テキストボックスより","を削除した文字列を取得
	 * @param sPay
	 * @return
	 */
	public String getPayText(String sPay){
		
		if(SkfCheckUtils.isNullOrEmpty(sPay)){
			return "0";
		}
		
		return sPay.replace(",", "").replace(SkfCommonConstant.FORMAT_EN, "").trim();
	}
	
	/**
	 * 文字列の取得
	 * @param ob
	 * @return
	 */
	public String getToString(Object ob){
		if(ob != null){
			return ob.toString();
		}else{
			return CodeConstant.DOUBLE_QUOTATION;
		}
	}
	
	/**
	 * 日付文字列
	 * @param sDay
	 * @return
	 */
	public String getDateText(String sDay){
		if(SkfCheckUtils.isNullOrEmpty(sDay)){
			return CodeConstant.DOUBLE_QUOTATION;
		}
		
		return sDay.replace(CodeConstant.SLASH, CodeConstant.DOUBLE_QUOTATION).replace(CodeConstant.UNDER_SCORE, CodeConstant.DOUBLE_QUOTATION).trim();
	}
	
	/**
	 * 面積項目より単位を削除した文字列を取得
	 * null、空文字は「"0"」を返却する
	 * 
	 * @param str	面積項目
	 * @return		面積文字列の数値のみ
	 */
	private String getMensekiText(String str) {

		String changeString = str;
		if (CheckUtils.isEmpty(changeString)) {
			return CodeConstant.STRING_ZERO;
		}
		// 変換
		changeString = changeString.replace(SkfCommonConstant.SQUARE_MASTER, "").replace(",", "").trim();
		return changeString;
	}
	
	/**
	 * 登録用文字列の取得
	 * 空の場合NULLを返却する
	 * @param ob
	 * @return
	 */
	public String getToRegistString(String str){
		if(SkfCheckUtils.isNullOrEmpty(str)){
			return null;
		}else{
			return str;
		}
	}
	
	/**
	 * 日付文字列
	 * @param sDay
	 * @return
	 */
	public String getRegistDateText(String sDay){
		if(SkfCheckUtils.isNullOrEmpty(sDay)){
			return null;
		}
		
		return sDay.replace(CodeConstant.SLASH, CodeConstant.DOUBLE_QUOTATION).replace(CodeConstant.UNDER_SCORE, CodeConstant.DOUBLE_QUOTATION).trim();
	}
	
	/**
	 * 画面項目制御
	 * @param comDto
	 */
	public void setControlStatus(Skf3030Sc002CommonDto comDto){
		//以下は未使用で非表示固定
//        '使用料計算の切替フラグ
//        Dim switchFlg As String = Com_SettingManager.GetSettingInfo(Constant.SettingsId.SHIYORYO_KEISAN_SWITCH_FLG)
//        '狭小/寒冷地の制御
//        If Constant.ShiyoryoKeisanSwitchFlg.KANREITIKYOSYODISPLAY.Equals(switchFlg) Then
//            Me.trKanreiti.Style.Add("display", "none")
//            Me.trKyosyo.Style.Add("display", "none")
//        Else
//            Me.trKanreiti.Style.Remove("display")
//            Me.trKyosyo.Style.Remove("display")
//        End If
		
		if(CodeConstant.BILLINGACTKBN_JIKKO_SUMI.equals(comDto.getHdnBillingActKbn())){
			//締め処理 実行済
			setActivateFalse(comDto);
			return;
		}
		
		//「社宅管理台帳ID」が”0”の場合
		if(Skf3030Sc002CommonDto.NO_SHATAKU_KANRI_ID.equals(comDto.getHdnShatakuKanriId())){
			//提示データ なし
			if(CodeConstant.ZEN_HYPHEN.equals(comDto.getSc006ShatakuStts()) && 
					CodeConstant.ZEN_HYPHEN.equals(comDto.getSc006BihinStts())	){
				//社員情報 入力済
				if(!SkfCheckUtils.isNullOrEmpty(comDto.getSc006ShainNo())){
					//使用料パターン 入力済
					if(!SkfCheckUtils.isNullOrEmpty(comDto.getSc006SiyoryoMonthPay())){
						setActivateStateNew3(comDto);
					}else{
						//使用料パターン 未入力
						 setActivateStateNew2(comDto);
					}
				}else{
					//社員情報 未入力
					setActivateStateNew1(comDto);
				}
			}else{
				//提示データ あり
				setActivateFalse(comDto);
			}
			
			//相互利用状況が”なし”の場合
			if(CodeConstant.MUTUAL_USE_KBN_UNAVAILABLE.equals(comDto.getSc006SogoRyojokyoSelect())){
				comDto.setSc006SogoHanteiKbnSelectDisableFlg(true);
				comDto.setSc006SokinShatakuSelectDisableFlg(true);
				comDto.setSc006SokinKyoekihiSelectDisableFlg(true);
				//貸付会社
				comDto.setSc006TaiyoKaisyaSelectDisableFlg(true);
				//借受会社
				comDto.setSc006KariukeKaisyaSelectDisableFlg(true);
				//開始日
				comDto.setSc006StartDayDisableFlg(true);
				//終了日
				comDto.setSc006EndDayDisableFlg(true);
				//カレンダーボタン活性制御
				//配属会社名
				comDto.setSc006HaizokuKaisyaSelectDisableFlg(true);
				//所属機関
				comDto.setSc006SyozokuKikanDisableFlg(true);
				//室・部名
				comDto.setSc006SituBuNameDisableFlg(true);
				//課等名
				comDto.setSc006KanadoMeiDisableFlg(true);
				//配属データコード番号
				comDto.setSc006HaizokuNoDisableFlg(true);
			}
			
			return;
		}
		
		//「社宅管理台帳ID」が値を持つ場合
		//提示データ なし
		if(CodeConstant.ZEN_HYPHEN.equals(comDto.getSc006ShatakuStts()) && 
				CodeConstant.ZEN_HYPHEN.equals(comDto.getSc006BihinStts())	){
			setActivateTrue(comDto);
		}
		
		if(CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(comDto.getHdnNYUTAIKYO_KBN())){
			//入居
			if(!CodeConstant.PRESENTATION_SITUATION_SHONIN.equals(comDto.getSc006ShatakuSttsCd())){
				//社宅提示ステータス 承認以外
				setActivateFalse(comDto);
			}else if(CodeConstant.PRESENTATION_SITUATION_SHONIN.equals(comDto.getSc006ShatakuSttsCd())){
				//社宅提示ステータス 承認済
				if(CodeConstant.DOUBLE_QUOTATION.equals(comDto.getSc006BihinSttsCd())){
					//備品提示ステータス －
					setActivateTrue(comDto);
				}else if(CodeConstant.BIHIN_STATUS_MI_SAKUSEI.equals(comDto.getSc006BihinSttsCd())){
					//備品提示ステータス 未作成
					setActivateStateNyukyo(comDto);
				}else if(CodeConstant.BIHIN_STATUS_SAKUSEI_CHU.equals(comDto.getSc006BihinSttsCd())){
					//備品提示ステータス 作成中
					setActivateStateNyukyo(comDto);
				}else if(CodeConstant.BIHIN_STATUS_SAKUSEI_SUMI.equals(comDto.getSc006BihinSttsCd())){
					//備品提示ステータス 作成済
					setActivateStateNyukyo(comDto);
				}else if(CodeConstant.BIHIN_STATUS_HANNYU_MACHI.equals(comDto.getSc006BihinSttsCd())){
					//備品提示ステータス 搬入待ち
					setActivateStateNyukyo(comDto);
				}else if(CodeConstant.BIHIN_STATUS_HANNYU_SUMI.equals(comDto.getSc006BihinSttsCd())){
					//備品提示ステータス搬入済
					setActivateStateNyukyo(comDto);
				}else if(CodeConstant.BIHIN_STATUS_SHONIN.equals(comDto.getSc006BihinSttsCd())){
					//備品提示ステータス 承認済
					setActivateTrue(comDto);
				}
			}
		}else if(CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(comDto.getHdnNYUTAIKYO_KBN())){
			//退居
			if(CodeConstant.PRESENTATION_SITUATION_SAKUSEI_CHU.equals(comDto.getSc006ShatakuSttsCd())){
				//社宅提示ステータス 作成中
				setActivateFalse(comDto);
			}else if(CodeConstant.PRESENTATION_SITUATION_SAKUSEI_SUMI.equals(comDto.getSc006ShatakuSttsCd())){
				//社宅提示ステータス 作成済 
				if(CodeConstant.DOUBLE_QUOTATION.equals(comDto.getSc006BihinSttsCd())){
					//備品提示ステータス －
					setActivateFalse(comDto);
				}else if(CodeConstant.BIHIN_STATUS_SAKUSEI_CHU.equals(comDto.getSc006BihinSttsCd())){
					//備品提示ステータス  作成中
					setActivateFalse(comDto);
				}else if(CodeConstant.BIHIN_STATUS_SAKUSEI_SUMI.equals(comDto.getSc006BihinSttsCd())){
					//備品提示ステータス  作成済
					setActivateFalse(comDto);
				}else if(CodeConstant.BIHIN_STATUS_TEIJI_CHU.equals(comDto.getSc006BihinSttsCd())){
					//備品提示ステータス  提示中
					setActivateFalse(comDto);
				}else if(CodeConstant.BIHIN_STATUS_DOI_SUMI.equals(comDto.getSc006BihinSttsCd())){
					//備品提示ステータス  同意済
					setActivateFalse(comDto);
				}else if(CodeConstant.BIHIN_STATUS_HANSHUTSU_MACHI.equals(comDto.getSc006BihinSttsCd())){
					//備品提示ステータス  搬出待ち
					setActivateFalse(comDto);
				}else if(CodeConstant.BIHIN_STATUS_HANSHUTSU_SUMI.equals(comDto.getSc006BihinSttsCd())){
					//備品提示ステータス  搬出済
					setActivateFalse(comDto);
				}else if(CodeConstant.BIHIN_STATUS_SHONIN.equals(comDto.getSc006BihinSttsCd())){
					//備品提示ステータス 承認済
					setActivateStateTaikyo2(comDto);
				}
			}else if(CodeConstant.PRESENTATION_SITUATION_SHONIN.equals(comDto.getSc006ShatakuSttsCd())){
				//社宅提示ステータス 承認
				if(CodeConstant.DOUBLE_QUOTATION.equals(comDto.getSc006BihinSttsCd())){
					//備品提示ステータス －
					setActivateTrue(comDto);
				}else if(CodeConstant.BIHIN_STATUS_SAKUSEI_CHU.equals(comDto.getSc006BihinSttsCd())){
					//備品提示ステータス  作成中
					setActivateStateTaikyo1(comDto);
				}else if(CodeConstant.BIHIN_STATUS_SAKUSEI_SUMI.equals(comDto.getSc006BihinSttsCd())){
					//備品提示ステータス  作成済
					setActivateStateTaikyo1(comDto);
				}else if(CodeConstant.BIHIN_STATUS_TEIJI_CHU.equals(comDto.getSc006BihinSttsCd())){
					//備品提示ステータス  提示中
					setActivateStateTaikyo1(comDto);
				}else if(CodeConstant.BIHIN_STATUS_DOI_SUMI.equals(comDto.getSc006BihinSttsCd())){
					//備品提示ステータス  同意済
					setActivateStateTaikyo1(comDto);
				}else if(CodeConstant.BIHIN_STATUS_HANSHUTSU_MACHI.equals(comDto.getSc006BihinSttsCd())){
					//備品提示ステータス  搬出待ち
					setActivateStateTaikyo1(comDto);
				}else if(CodeConstant.BIHIN_STATUS_HANSHUTSU_SUMI.equals(comDto.getSc006BihinSttsCd())){
					//備品提示ステータス  搬出済
					setActivateStateTaikyo1(comDto);
				}else if(CodeConstant.BIHIN_STATUS_SHONIN.equals(comDto.getSc006BihinSttsCd())){
					//備品提示ステータス 承認済
					setActivateTrue(comDto);
				}
			}
		}else{
			//変更
			if(CodeConstant.BIHIN_STATUS_SAKUSEI_CHU.equals(comDto.getSc006BihinSttsCd())){
				//備品提示ステータス  作成中
				setActivateFalse(comDto);
			}else if(CodeConstant.BIHIN_STATUS_SHONIN.equals(comDto.getSc006BihinSttsCd())){
				//備品提示ステータス 承認済
				setActivateTrue(comDto);
			}else{
				//上記の組合せに該当しない場合
			}
		}
	}
	
	/**
	 * 画面項目制御：相互利用状況
	 * @param comDto
	 */
	public void setControlStatusSogoRiyo(Skf3030Sc002CommonDto comDto){
		if(TRUE.equals(comDto.getHdnSougoRiyouFlg())){
			//相互利用タブ有効な場合
			if(CodeConstant.MUTUAL_USE_KBN_UNAVAILABLE.equals(comDto.getSc006SogoRyojokyoSelect())){
				//相互利用状況(なし) ⇒ 相互利用関連項目は非活性
				setSougoRiyoJokyoControlStatus(comDto, false);
			}else{
				//相互利用(あり)
				setSougoRiyoJokyoControlStatus(comDto, true);
			}
		}
	}
	
	/**
	 * 画面項目制御：すべて非活性（運用ガイド除く）
	 * @param comDto
	 */
	public void setActivateFalse(Skf3030Sc002CommonDto comDto){
		//社員入力支援ボタン
		comDto.setSc006ShinseiNaiyoDisableFlg(true);
		//使用料入力支援ボタン
		comDto.setShiyoryoShienDisableFlg(true);
		//社宅情報タブ
		setShatakuInfoControlStatus(comDto,false);
		//備品情報タブ
		setBihinInfoControlStatus(comDto, false);
		//相互利用情報タブ
		setSougoRiyouInfoControlStatus(comDto, false);
		//運用ガイドボタン
		comDto.setBtnUnyonGuideDisableFlg(false);
		//次月予約ボタン
		comDto.setBtnJigetuYoyakuDisableFlg(true);
		//登録ボタン
		comDto.setBtnRegistDisableFlg(true);
		//削除ボタン
		comDto.setBtnDeleteDisableFlg(true);
	}
	
	/**
	 * 画面項目制御：編集可能（社員入力支援除く）
	 * @param comDto
	 */
	public void setActivateTrue(Skf3030Sc002CommonDto comDto){
		//社員入力支援ボタン
		comDto.setSc006ShinseiNaiyoDisableFlg(true);
		//使用料入力支援ボタン
		comDto.setShiyoryoShienDisableFlg(false);
		//社宅情報タブ
		setShatakuInfoControlStatus(comDto,true);
		//備品情報タブ
		setBihinInfoControlStatus(comDto, true);
		//相互利用情報タブ
		setSougoRiyouInfoControlStatus(comDto, true);
		//運用ガイドボタン
		comDto.setBtnUnyonGuideDisableFlg(false);
		//次月予約ボタン
		comDto.setBtnJigetuYoyakuDisableFlg(false);
		//登録ボタン
		comDto.setBtnRegistDisableFlg(false);
		//削除ボタン
		comDto.setBtnDeleteDisableFlg(false);
	}
	
	/**
	 * 画面項目制御：新規登録（社員入力）
	 * @param comDto
	 */
	public void setActivateStateNew1(Skf3030Sc002CommonDto comDto){
		//社員入力支援ボタン
		comDto.setSc006ShinseiNaiyoDisableFlg(false);
		//使用料入力支援ボタン
		comDto.setShiyoryoShienDisableFlg(true);
		//社宅情報タブ
		setShatakuInfoControlStatus(comDto,false);
		//備品情報タブ
		setBihinInfoControlStatus(comDto, false);
		//相互利用情報タブ
		setSougoRiyouInfoControlStatus(comDto, false);
		//運用ガイドボタン
		comDto.setBtnUnyonGuideDisableFlg(false);
		//次月予約ボタン
		comDto.setBtnJigetuYoyakuDisableFlg(true);
		//登録ボタン
		comDto.setBtnRegistDisableFlg(true);
		//削除ボタン
		comDto.setBtnDeleteDisableFlg(true);
	}
	
	/**
	 * 画面項目制御：新規登録（使用料入力）
	 * @param comDto
	 */
	public void setActivateStateNew2(Skf3030Sc002CommonDto comDto){
		//社員入力支援ボタン
		comDto.setSc006ShinseiNaiyoDisableFlg(false);
		//使用料入力支援ボタン
		comDto.setShiyoryoShienDisableFlg(false);
		//社宅情報タブ
		setShatakuInfoControlStatus(comDto,false);
		//備品情報タブ
		setBihinInfoControlStatus(comDto, false);
		//相互利用情報タブ
		setSougoRiyouInfoControlStatus(comDto, false);
		//運用ガイドボタン
		comDto.setBtnUnyonGuideDisableFlg(false);
		//次月予約ボタン
		comDto.setBtnJigetuYoyakuDisableFlg(true);
		//登録ボタン
		comDto.setBtnRegistDisableFlg(true);
		//削除ボタン
		comDto.setBtnDeleteDisableFlg(true);
	}
	
	/**
	 * 画面項目制御：新規登録
	 * @param comDto
	 */
	public void setActivateStateNew3(Skf3030Sc002CommonDto comDto){
		//社員入力支援ボタン
		comDto.setSc006ShinseiNaiyoDisableFlg(false);
		//使用料入力支援ボタン
		comDto.setShiyoryoShienDisableFlg(false);
		//社宅情報タブ
		setShatakuInfoControlStatus(comDto,true);
		//備品情報タブ
		setBihinInfoControlStatus(comDto, true);
		//相互利用情報タブ
		setSougoRiyouInfoControlStatus(comDto, true);
		//運用ガイドボタン
		comDto.setBtnUnyonGuideDisableFlg(false);
		//次月予約ボタン
		comDto.setBtnJigetuYoyakuDisableFlg(true);
		//登録ボタン
		comDto.setBtnRegistDisableFlg(false);
		//削除ボタン
		comDto.setBtnDeleteDisableFlg(true);
	}
	
	/**
	 * 画面項目制御：入居データ（社宅提示承認・備品提示中）
	 * @param comDto
	 */
	public void setActivateStateNyukyo(Skf3030Sc002CommonDto comDto){
		//社員入力支援ボタン
		comDto.setSc006ShinseiNaiyoDisableFlg(true);
		//使用料入力支援ボタン
		comDto.setShiyoryoShienDisableFlg(false);
		//社宅情報タブ
		setShatakuInfoControlStatus(comDto,true);
		//備品情報タブ
		setBihinInfoControlStatus(comDto, false);
		//相互利用情報タブ
		setSougoRiyouInfoControlStatus(comDto, true);
		//運用ガイドボタン
		comDto.setBtnUnyonGuideDisableFlg(false);
		//次月予約ボタン
		comDto.setBtnJigetuYoyakuDisableFlg(false);
		//登録ボタン
		comDto.setBtnRegistDisableFlg(false);
		//削除ボタン
		comDto.setBtnDeleteDisableFlg(true);
	}
	
	/**
	 * 画面項目制御：退居データ（社宅提示承認・備品提示中）
	 * @param comDto
	 */
	public void setActivateStateTaikyo1(Skf3030Sc002CommonDto comDto){
		//社員入力支援ボタン
		comDto.setSc006ShinseiNaiyoDisableFlg(true);
		//使用料入力支援ボタン
		comDto.setShiyoryoShienDisableFlg(false);
		//社宅情報タブ
		setShatakuInfoControlStatus(comDto,true);
		//備品情報タブ
		setBihinInfoControlStatus(comDto, false);
		//相互利用情報タブ
		setSougoRiyouInfoControlStatus(comDto, true);
		//運用ガイドボタン
		comDto.setBtnUnyonGuideDisableFlg(false);
		//次月予約ボタン
		comDto.setBtnJigetuYoyakuDisableFlg(false);
		//登録ボタン
		comDto.setBtnRegistDisableFlg(false);
		//削除ボタン
		comDto.setBtnDeleteDisableFlg(true);
	}
	
	/**
	 * 画面項目制御：退居データ（社宅提示中・備品提示承認）
	 * @param comDto
	 */
	public void setActivateStateTaikyo2(Skf3030Sc002CommonDto comDto){
		//社員入力支援ボタン
		comDto.setSc006ShinseiNaiyoDisableFlg(true);
		//使用料入力支援ボタン
		comDto.setShiyoryoShienDisableFlg(true);
		//社宅情報タブ
		setShatakuInfoControlStatus(comDto,false);
		//備品情報タブ
		setBihinInfoControlStatus(comDto, true);
		//相互利用情報タブ
		setSougoRiyouInfoControlStatus(comDto, false);
		//運用ガイドボタン
		comDto.setBtnUnyonGuideDisableFlg(false);
		//次月予約ボタン
		comDto.setBtnJigetuYoyakuDisableFlg(true);
		//登録ボタン
		comDto.setBtnRegistDisableFlg(false);
		//削除ボタン
		comDto.setBtnDeleteDisableFlg(true);
	}
	
	/**
	 * 画面社宅情報タブ項目制御の設定
	 * @param comDto
	 * @param status true:活性 false:非活性
	 */
	public void setShatakuInfoControlStatus(Skf3030Sc002CommonDto comDto, Boolean status){
		Boolean setStatus = true;
		if(status){
			//活性にする
			setStatus = false;
		}
		
		comDto.setSc006OldKaisyaNameSelectDisableFlg(setStatus); // 原籍会社
		comDto.setSc006KyuyoKaisyaSelectDisableFlg(setStatus); // 給与支給会社
		comDto.setSc006NyukyoYoteiDayDisableFlg(setStatus); //入居予定日
		comDto.setSc006TaikyoYoteiDayDisableFlg(setStatus); //退居予定日
		comDto.setSc006KyojyusyaKbnSelectDisableFlg(setStatus);//居住者区分
		comDto.setSc006YakuinSanteiSelectDisableFlg(setStatus);//役員算定
		comDto.setSc006SiyoroTyoseiPayDisableFlg(setStatus);//社宅使用料調整金額
		comDto.setSc006KyoekihiMonthPayDisableFlg(setStatus);//個人負担共益費月額
		comDto.setSc006KyoekihiTyoseiPayDisableFlg(setStatus);//個人負担共益費調整金額
		comDto.setSc006KyoekihiPayMonthSelectDisableFlg(setStatus);//共益費支払月

		comDto.setParkingShien1DisableFlg(setStatus); //駐車場入力支援（区画１）
		comDto.setSc006RiyouStartDayOneDisableFlg(setStatus); //利用開始日（区画１）
		comDto.setClearParking1DisableFlg(setStatus); //クリアボタン（区画１）
		comDto.setSc006RiyouEndDayOneDisableFlg(setStatus); //利用終了日（区画１）

		comDto.setParkingShien2DisableFlg(setStatus); //駐車場入力支援（区画２）
		comDto.setSc006RiyouStartDayTwoDisableFlg(setStatus); //利用開始日（区画２）
		comDto.setClearParking2DisableFlg(setStatus); //クリアボタン（区画２）
		comDto.setSc006RiyouEndDayTwoDisableFlg(setStatus); //利用終了日（区画２）
		comDto.setSc006TyusyaTyoseiPayDisableFlg(setStatus); //駐車場使用料調整金額
		comDto.setSc006BicouDisableFlg(setStatus); //備考

		comDto.setGShatakuInfoControlStatusFlg(status);
	}
	
	/**
	 * 画面備品情報タブ項目制御の設定
	 * @param comDto
	 * @param status true:活性 false:非活性
	 */
	public void setBihinInfoControlStatus(Skf3030Sc002CommonDto comDto, Boolean status){
		Boolean setStatus = true;
		if(status){
			//活性にする
			setStatus = false;
		}
		
		//貸与状態はリスト作成時に設定
//		For i = 0 To Me.grvBihinList.Rows.Count - 1
//		    Dim ddlBihinTaiyoStts As DropDownList = DirectCast(Me.grvBihinList.Rows(i).FindControl(DDL_BIHIN_TAIYO_JOTAI), DropDownList)
//		    Dim lblBihinCd As Label = DirectCast(Me.grvBihinList.Rows(i).FindControl(CONTROL_LBL_BIHINCD), Label)
//		    //下取り備品は常に非活性のため、活性制御しない
//		    If Not SHITADORI.Equals(Mid(lblBihinCd.Text, 1, 1)) Then
//			ddlBihinTaiyoStts.Enabled = status   //備品貸与状態
//		    End If
//		Next
		List<Map<String,Object>> BihinDt = new ArrayList<Map<String, Object>>();
		
		// 備品情報リスト
		List<Map<String, Object>> bihinList = new ArrayList<Map<String, Object>>();
		bihinList.addAll(jsonArrayToArrayList(comDto.getJsonBihin()));
		
		for(Map<String,Object> bihinRow : bihinList){
			Map<String,Object> tmpMap = new HashMap<String,Object>();
		
			tmpMap.put("bihinCd", bihinRow.get("bihinCd").toString());
			tmpMap.put("bihinName", bihinRow.get("bihinName").toString());
			tmpMap.put("heyaSonaetukeSttsStr", bihinRow.get("heyaSonaetukeSttsStr").toString());
			tmpMap.put("bihinTaiyoStts", bihinRow.get("bihinTaiyoStts").toString());
			tmpMap.put("bihinTaiyoSttsList", bihinRow.get("bihinTaiyoSttsList").toString());
			tmpMap.put("bihinStatusOld", bihinRow.get("bihinStatusOld").toString());
			tmpMap.put("updateFlg", bihinRow.get("updateFlg").toString());
			tmpMap.put("updateDate", bihinRow.get("updateDate").toString());
			tmpMap.put("heyaSonaetukeStts", bihinRow.get("heyaSonaetukeStts").toString());
			tmpMap.put("bihinTaiyoSttsKbn", bihinRow.get("bihinTaiyoSttsKbn").toString());
			
			BihinDt.add(tmpMap);
		}
		comDto.setBihinInfoListTableData(getBihinListTableDataViewColumnFromView(BihinDt,status));
		

		comDto.setSc006TaiyoDayDisableFlg(setStatus); //貸与日
		comDto.setSc006HenkyakuDayDisableFlg(setStatus); //返却日
		comDto.setSc006KibouDayInDisableFlg(setStatus); //搬入希望日
		comDto.setSc006KibouTimeInSelectDisableFlg(setStatus);//搬入希望日時時間帯
		comDto.setSc006HonninAddrInDisableFlg(setStatus);//搬入本人連絡先
		comDto.setSc006UketoriDairiInNameDisableFlg(setStatus);//受取代理人
		comDto.setSc006UketoriDairiInShienDisableFlg(setStatus);//搬入社員入力支援
		comDto.setSc006UketoriDairiAddrDisableFlg(setStatus);//受取代理人連絡先
		comDto.setSc006KibouDayOutDisableFlg(setStatus);//搬出希望日
		comDto.setSc006KibouTimeOutSelectDisableFlg(setStatus);//搬出希望日時時間帯
		comDto.setSc006HonninAddrOutDisableFlg(setStatus);//搬出本人連絡先
		comDto.setSc006TachiaiDairiDisableFlg(setStatus);//立会代理人
		comDto.setSc006TachiaiDairiShienDisableFlg(setStatus);//搬出社員入力支援
		comDto.setSc006TachiaiDairiAddrDisableFlg(setStatus);//立会代理人連絡先
		comDto.setSc006DairiBikoDisableFlg(setStatus);//代理人備考
		comDto.setSc006BihinBikoDisableFlg(setStatus);//備考

		comDto.setGBihinInfoControlStatusFlg(status);
	}
	
	/**
	 * 画面相互利用情報タブ項目制御の設定
	 * @param comDto
	 * @param status true:活性 false:非活性
	 */
	public void setSougoRiyouInfoControlStatus(Skf3030Sc002CommonDto comDto, Boolean status){
		Boolean setStatus = true;
		if(status){
			//活性にする
			setStatus = false;
		}
		
		comDto.setSc006SogoRyojokyoSelectDisableFlg(setStatus);//相互利用状況
		comDto.setSc006TaiyoKaisyaSelectDisableFlg(setStatus);//貸付会社
		comDto.setSc006KariukeKaisyaSelectDisableFlg(setStatus);//借受会社
		comDto.setSc006ChintaiRyoDisableFlg(setStatus);//社宅賃貸料
		comDto.setSc006TyusyajoRyokinDisableFlg(setStatus);//駐車場料金
		comDto.setSc006KyoekihiDisableFlg(setStatus);//共益費（事業者負担）
		comDto.setSc006SogoHanteiKbnSelectDisableFlg(setStatus);//相互利用判定区分
		comDto.setSc006StartDayDisableFlg(setStatus);//開始日
		comDto.setSc006EndDayDisableFlg(setStatus);//終了日
		comDto.setSc006HaizokuKaisyaSelectDisableFlg(setStatus);//配属会社名
		comDto.setSc006SyozokuKikanDisableFlg(setStatus);//所属機関
		comDto.setSc006SituBuNameDisableFlg(setStatus);//室・部名
		comDto.setSc006KanadoMeiDisableFlg(setStatus);//課等名
		comDto.setSc006HaizokuNoDisableFlg(setStatus);//配属データコード番号

		comDto.setSc006SokinKyoekihiSelectDisableFlg(setStatus);//共益費会社間送金区分
		comDto.setSc006SokinShatakuSelectDisableFlg(setStatus);//社宅使用料会社間送金区分

		if(status){
			comDto.setHdnSougoRiyouFlg("true");
		}else{
			comDto.setHdnSougoRiyouFlg("false");
		}
		
	}
	
	/**
	 * 画面相互利用状況(あり/なし)による画面制御
	 * @param status true:活性 false:非活性
	 */
	public void setSougoRiyoJokyoControlStatus(Skf3030Sc002CommonDto comDto, Boolean status){
		Boolean setStatus = true;
		if(status){
			setStatus = false;
		}
		
		comDto.setSc006TaiyoKaisyaSelectDisableFlg(setStatus); //貸付会社
		comDto.setSc006KariukeKaisyaSelectDisableFlg(setStatus); //借受会社
		//相互利用判定区分
		comDto.setSc006SogoHanteiKbnSelectDisableFlg(setStatus);
		//共益費会社間送金区分
		comDto.setSc006SokinKyoekihiSelectDisableFlg(setStatus);
		//社宅使用料会社間送金区分
		comDto.setSc006SokinShatakuSelectDisableFlg(setStatus);
		//開始日
		comDto.setSc006StartDayDisableFlg(setStatus);
		//終了日
		comDto.setSc006EndDayDisableFlg(setStatus);
		//カレンダーボタン活性制御
		//配属会社名
		comDto.setSc006HaizokuKaisyaSelectDisableFlg(setStatus);
		//所属機関
		comDto.setSc006SyozokuKikanDisableFlg(setStatus);
		//室・部名
		comDto.setSc006SituBuNameDisableFlg(setStatus);
		//課等名
		comDto.setSc006KanadoMeiDisableFlg(setStatus);
		//配属データコード番号
		comDto.setSc006HaizokuNoDisableFlg(setStatus);
	}
	
	/**
	 * 現行のページのロードが完了した時のイベントメソッド
	 * @param comDto
	 */
	public void setBihinListPageLoadComplete(Skf3030Sc002CommonDto comDto){
		//提示備品データを取得
		if(Objects.equals(comDto.getBihinItiranFlg(), 0) && Objects.equals(comDto.getBihinItiranReloadFlg(), false)){
			
			if(Skf3030Sc002CommonDto.NO_SHATAKU_KANRI_ID.equals(comDto.getHdnShatakuKanriId())){
				//「社宅管理台帳ID」が”0”の場合
				//「備品コード」、「備品名称」、「部屋備付状態」取得
				Skf3030Sc002GetBihinInfoForNoIdExpParameter bihinParam = new Skf3030Sc002GetBihinInfoForNoIdExpParameter();
				bihinParam.setShatakuKanriNo(Long.parseLong(comDto.getHdnShatakuKanriNo()));
				bihinParam.setShatakuRoomKanriNo(Long.parseLong(comDto.getHdnShatakuRoomKanriNo()));
				List<Skf3030Sc002GetBihinInfoForNoIdExp> grvBihinList = skf3030Sc002GetBihinInfoForNoIdExpRepository.getBihinInfoForNoId(bihinParam);
				comDto.setBihinInfoListTableData(getBihinForNoIdListTableDataViewColumn(grvBihinList, comDto.getGBihinInfoControlStatusFlg()));
				
			}else{
				//備品情報取得
				Long shatakuKanriId = Long.parseLong(comDto.getHdnShatakuKanriId());
				Long shatakuKanriNo = Long.parseLong(comDto.getHdnShatakuKanriNo());
				Long shatakuRoomKanriNo = Long.parseLong(comDto.getHdnShatakuRoomKanriNo());
				String nengetu = comDto.getHdnNengetsu();
				Skf3030Sc002GetBihinInfoExpParameter bihinParam = new Skf3030Sc002GetBihinInfoExpParameter();
				bihinParam.setShatakuKanriId(shatakuKanriId);
				bihinParam.setShatakuKanriNo(shatakuKanriNo);
				bihinParam.setShatakuRoomKanriNo(shatakuRoomKanriNo);
				bihinParam.setYearMonth(nengetu);
				List<Skf3030Sc002GetBihinInfoExp> bihinDt = skf3030Sc002GetBihinInfoExpRepository.getBihinInfo(bihinParam);
				comDto.setBihinInfoListTableData(getBihinListTableDataViewColumn(bihinDt, comDto.getGBihinInfoControlStatusFlg()));
				
			}
		}else{
			comDto.setBihinItiranReloadFlg(false);
		}
	}
	
	/**
	 * リストテーブルに出力するリストを取得する。
	 * 
	 * @param originList
	 * @return リストテーブルに出力するリスト
	 */
	public List<Map<String, Object>> getBihinForNoIdListTableDataViewColumn(List<Skf3030Sc002GetBihinInfoForNoIdExp> originList,Boolean status) {

		List<Map<String, Object>> setViewList = new ArrayList<Map<String, Object>>();
		//備品貸与状態リスト
		List<Map<String, Object>> statusList = ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_BIHINLENTSTATUS_KBN, "",false);
		
		
		//備品備付区分コード取得
		Map<String, String> gcmBihinStatusKbn = new HashMap<String, String>();
		gcmBihinStatusKbn = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_BIHINSTATUS_KBN);
		
		for (int i = 0; i < originList.size(); i++) {
			
			Skf3030Sc002GetBihinInfoForNoIdExp tmpData = originList.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();

			//備品コード
			tmpMap.put("bihinCd", HtmlUtils.htmlEscape(tmpData.getBihinCd()));
			tmpMap.put("bihinName", HtmlUtils.htmlEscape(tmpData.getBihinName()));
			//部屋備付状態
			tmpMap.put("heyaSonaetukeStts", tmpData.getBihinStatusKbn());
			tmpMap.put("heyaSonaetukeSttsStr", HtmlUtils.htmlEscape(gcmBihinStatusKbn.get(tmpData.getBihinStatusKbn())));

			//備品貸与状態
			String bihinTaiyoStts = tmpData.getBihinLentStatusKbn();
			String statusListCode = createBihinStatusSelect(bihinTaiyoStts,statusList,tmpData.getBihinCd());
			if(Skf3030Sc002CommonDto.SHITADORI.equals(tmpData.getBihinCd().substring(0, 1)) || !status){
				//下取りの備品貸与状態ドロップダウンを非活性にセット
				tmpMap.put("bihinTaiyoStts","<select id='bihinTaiyoStatus" + i + "' name='bihinTaiyoStatus" + i + "' disabled>" + statusListCode + "</select>");
			}else{
				tmpMap.put("bihinTaiyoStts","<select id='bihinTaiyoStatus" + i + "' name='bihinTaiyoStatus" + i + "'>" + statusListCode + "</select>");
			}
			
			tmpMap.put("bihinStatusOld", tmpData.getBihinSatausOld());
			tmpMap.put("bihinTaiyoSttsKbn", tmpData.getBihinLentStatusKbn());
			
			setViewList.add(tmpMap);
		}

		return setViewList;
	}
	
	/**
	 * リストテーブルに出力するリストを取得する。
	 * 
	 * @param originList
	 * @return リストテーブルに出力するリスト
	 */
	public List<Map<String, Object>> getBihinListTableDataViewColumn(List<Skf3030Sc002GetBihinInfoExp> originList,Boolean status) {

		List<Map<String, Object>> setViewList = new ArrayList<Map<String, Object>>();
		//備品貸与状態リスト
		List<Map<String, Object>> statusList = ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_BIHINLENTSTATUS_KBN, "",false);
		
		
		//備品備付区分コード取得
		Map<String, String> gcmBihinStatusKbn = new HashMap<String, String>();
		gcmBihinStatusKbn = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_BIHINSTATUS_KBN);
		
		for (int i = 0; i < originList.size(); i++) {
			
			Skf3030Sc002GetBihinInfoExp tmpData = originList.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();

			//備品コード
			tmpMap.put("bihinCd", HtmlUtils.htmlEscape(tmpData.getBihinCd()));
			tmpMap.put("bihinName", HtmlUtils.htmlEscape(tmpData.getBihinName()));
			//部屋備付状態
			tmpMap.put("heyaSonaetukeStts", tmpData.getBihinStatusKbn());
			tmpMap.put("heyaSonaetukeSttsStr", HtmlUtils.htmlEscape(gcmBihinStatusKbn.get(tmpData.getBihinStatusKbn())));

			//備品貸与状態
			String bihinTaiyoStts = tmpData.getBihinLentStatusKbn();
			String statusListCode = createBihinStatusSelect(bihinTaiyoStts,statusList,tmpData.getBihinCd());
			if(Skf3030Sc002CommonDto.SHITADORI.equals(tmpData.getBihinCd().substring(0, 1)) || !status){
				//下取りの備品貸与状態ドロップダウンを非活性にセット
				tmpMap.put("bihinTaiyoStts","<select id='bihinTaiyoStatus" + i + "' name='bihinTaiyoStatus" + i + "' disabled>" + statusListCode + "</select>");
			}else{
				tmpMap.put("bihinTaiyoStts","<select id='bihinTaiyoStatus" + i + "' name='bihinTaiyoStatus" + i + "'>" + statusListCode + "</select>");
			}
			
			tmpMap.put("bihinStatusOld", tmpData.getBihinSatausOld());
			tmpMap.put("bihinTaiyoSttsKbn", tmpData.getBihinLentStatusKbn());
			
			setViewList.add(tmpMap);
		}

		return setViewList;
	}
	
	/**
	 * リストテーブルに出力するリストを取得する。
	 * (非活制御有)
	 * 
	 * @param originList
	 * @return リストテーブルに出力するリスト
	 */
	public List<Map<String, Object>> getBihinListTableDataViewColumnFromView(List<Map<String,Object>> originList,Boolean status) {

		List<Map<String, Object>> setViewList = new ArrayList<Map<String, Object>>();

		//備品貸与状態リスト
		List<Map<String, Object>> statusList = ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_BIHINLENTSTATUS_KBN, "",false);
				
		int i=0;
		for(Map<String,Object> tmpData : originList){

			Map<String, Object> tmpMap = new HashMap<String, Object>();

			//備品コード
			tmpMap.put("bihinCd", HtmlUtils.htmlEscape(tmpData.get("bihinCd").toString()));
			tmpMap.put("bihinName", HtmlUtils.htmlEscape(tmpData.get("bihinName").toString()));
			//部屋備付状態
			tmpMap.put("heyaSonaetukeStts", tmpData.get("heyaSonaetukeStts").toString());
			tmpMap.put("heyaSonaetukeSttsStr", HtmlUtils.htmlEscape(tmpData.get("heyaSonaetukeSttsStr").toString()));

			//備品貸与状態
			String bihinTaiyoStts = tmpData.get("bihinTaiyoStts").toString();
			String statusListCode = createBihinStatusSelect(bihinTaiyoStts,statusList,tmpData.get("bihinCd").toString());
			if(Skf3030Sc002CommonDto.SHITADORI.equals(tmpData.get("bihinCd").toString().substring(0, 1)) || !status){
				//下取りの備品貸与状態ドロップダウンを非活性にセット
				tmpMap.put("bihinTaiyoStts","<select id='bihinTaiyoStatus" + i + "' name='bihinTaiyoStatus" + i + "' disabled>" + statusListCode + "</select>");
			}else{
				tmpMap.put("bihinTaiyoStts","<select id='bihinTaiyoStatus" + i + "' name='bihinTaiyoStatus" + i + "'>" + statusListCode + "</select>");
			}
			tmpMap.put("bihinStatusOld", tmpData.get("bihinStatusOld").toString());
			tmpMap.put("bihinTaiyoSttsKbn", tmpData.get("bihinTaiyoSttsKbn").toString());
			
			setViewList.add(tmpMap);
			i++;
		}

		return setViewList;
	}

	
	/**
	 * 貸与状態SelectのHTMLコード生成処理
	 * @param bihinStatus 備付状況
	 * @param statusList 備付状況リスト
	 * @return
	 */
	private String createBihinStatusSelect(String bihinStatus,List<Map<String, Object>> statusList,String bihinCd){
		String returnListCode = "";
		
		for(Map<String, Object> obj : statusList){
			String value = obj.get("value").toString();
			String label = obj.get("label").toString();
			if(Skf3030Sc002CommonDto.NON_TAX.equals(bihinCd.substring(0,1)) || Skf3030Sc002CommonDto.SHITADORI.equals(bihinCd.substring(0, 1))){
				//課税対象外または下取り
				if(CodeConstant.BIHIN_STATE_RENTAL.equals(value)){
					continue;
				}else if(CodeConstant.BIHIN_STATE_KYOYO.equals(value)){
					continue;
				}
			}
			if(bihinStatus!=null && bihinStatus.compareTo(value)==0){
				//貸与状態とリスト値が一致する場合、選択中にする
				returnListCode += "<option value='" + value + "' selected>" + label + "</option>";
			}else{
				returnListCode += "<option value='" + value + "'>" + label + "</option>";	
			}
			
		}
				
		return returnListCode;
	}
	
	
	/**
	 * @param comDto
	 * @return
	 * @throws ParseException
	 */
	public boolean validateInput(Skf3030Sc002CommonDto comDto) throws ParseException{
		
		// DefaultColorに戻す
		//setDefaultColor()
		//エラーメッセージ
		StringBuilder errMsg = new StringBuilder();
		//次回タブ表示位置
		comDto.setNextTabIndex("");
		comDto.setHdnTabIndex("0");

		// 必須チェック
		isErrEmpty(comDto,errMsg);

		//バイト数チェック→画面側を信用する
		//Me.IsErrByteCount(errMsg)

		// 文字種チェック→画面を信用する
		//isErrValueType(comDto,errMsg);

		//整合性チェック
		if (0 == errMsg.toString().length()){
			//必須エラーなしの場合
			isErrDateFormat(comDto,errMsg);
		}

		//Me.hdnFieldMessage.Value = errMsg.toString();

		if (!CheckUtils.isEmpty(comDto.getNextTabIndex())){
			//Me.tbcNyutaikyoInfo.ActiveTabIndex = CInt(nextTabIndex)
			comDto.setHdnTabIndex(comDto.getNextTabIndex());
		}
		
		//エラーが存在する場合
		if (0 < errMsg.toString().length()){
			if (!CheckUtils.isEmpty(comDto.getNextTabIndex())){
				//Me.tbcNyutaikyoInfo.ActiveTabIndex = CInt(nextTabIndex)
				comDto.setHdnTabIndex(comDto.getNextTabIndex());
			}
			LogUtils.infoByMsg("validateInput, " + errMsg.toString());
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * エラー変数初期化
	 * @param comDto
	 */
	public void errReset(Skf3030Sc002CommonDto comDto){
		// 原籍会社
		comDto.setSc006OldKaisyaNameSelectErr(CodeConstant.DOUBLE_QUOTATION);
		// 給与支給会社名
		comDto.setSc006KyuyoKaisyaSelectErr(CodeConstant.DOUBLE_QUOTATION);
		// 入居予定日
		comDto.setSc006NyukyoYoteiDayErr(CodeConstant.DOUBLE_QUOTATION);
		// 退居予定日
		comDto.setSc006TaikyoYoteiDayErr(CodeConstant.DOUBLE_QUOTATION);
		// 利用開始日1
		comDto.setSc006RiyouStartDayOneErr(CodeConstant.DOUBLE_QUOTATION);
		// 利用終了日1
		comDto.setSc006RiyouEndDayOneErr(CodeConstant.DOUBLE_QUOTATION);
		// 居住者区分
		comDto.setSc006KyojyusyaKbnSelectErr(CodeConstant.DOUBLE_QUOTATION);
		// 利用開始日2
		comDto.setSc006RiyouStartDayTwoErr(CodeConstant.DOUBLE_QUOTATION);
		// 利用終了日2
		comDto.setSc006RiyouEndDayTwoErr(CodeConstant.DOUBLE_QUOTATION);
		// 役員算定
		comDto.setSc006YakuinSanteiSelectErr(CodeConstant.DOUBLE_QUOTATION);
		// 社宅使用料調整金額
		comDto.setSc006SiyoroTyoseiPayErr(CodeConstant.DOUBLE_QUOTATION);
		// 駐車場使用料調整金額
		comDto.setSc006TyusyaTyoseiPayErr(CodeConstant.DOUBLE_QUOTATION);
		// 個人負担共益費月額
		comDto.setSc006KyoekihiMonthPayErr(CodeConstant.DOUBLE_QUOTATION);
		// 個人負担共益費調整金額
		comDto.setSc006KyoekihiTyoseiPayErr(CodeConstant.DOUBLE_QUOTATION);
		// 共益費支払月選択値
		comDto.setSc006KyoekihiPayMonthSelectErr(CodeConstant.DOUBLE_QUOTATION);
		// 貸与日
		comDto.setSc006TaiyoDayErr(CodeConstant.DOUBLE_QUOTATION);
		// 返却日
		comDto.setSc006HenkyakuDayErr(CodeConstant.DOUBLE_QUOTATION);
		// 搬入希望日
		comDto.setSc006KibouDayInErr(CodeConstant.DOUBLE_QUOTATION);
		// 搬入希望時間
		comDto.setSc006KibouTimeInSelectErr(CodeConstant.DOUBLE_QUOTATION);
		// 搬入本人連絡先
		comDto.setSc006HonninAddrInErr(CodeConstant.DOUBLE_QUOTATION);
		// 搬入受取代理人名
		comDto.setSc006UketoriDairiInNameErr(CodeConstant.DOUBLE_QUOTATION);
		// 搬入受取代理人連絡先
		comDto.setSc006UketoriDairiAddrErr(CodeConstant.DOUBLE_QUOTATION);
		// 搬出希望日
		comDto.setSc006KibouDayOutErr(CodeConstant.DOUBLE_QUOTATION);
		// 搬出希望時間
		comDto.setSc006KibouTimeOutSelectErr(CodeConstant.DOUBLE_QUOTATION);
		// 搬出本人連絡先
		comDto.setSc006HonninAddrOutErr(CodeConstant.DOUBLE_QUOTATION);
		// 搬出立会代理人名
		comDto.setSc006TachiaiDairiErr(CodeConstant.DOUBLE_QUOTATION);
		// 搬出立会代理人連絡先
		comDto.setSc006TachiaiDairiAddrErr(CodeConstant.DOUBLE_QUOTATION);
		// 配属会社名選択値
		comDto.setSc006HaizokuKaisyaSelectErr(CodeConstant.DOUBLE_QUOTATION);
		// 出向の有無(相互利用状況)
		comDto.setSc006SogoRyojokyoSelectErr(CodeConstant.DOUBLE_QUOTATION);
		// 所属機関
		comDto.setSc006SyozokuKikanErr(CodeConstant.DOUBLE_QUOTATION);
		// 貸付会社選択値
		comDto.setSc006TaiyoKaisyaSelectErr(CodeConstant.DOUBLE_QUOTATION);
		// 室・部名
		comDto.setSc006SituBuNameErr(CodeConstant.DOUBLE_QUOTATION);
		// 借受会社
		comDto.setSc006KariukeKaisyaSelectErr(CodeConstant.DOUBLE_QUOTATION);
		// 課等名
		comDto.setSc006KanadoMeiErr(CodeConstant.DOUBLE_QUOTATION);
		// 相互利用判定区分
		comDto.setSc006SogoHanteiKbnSelectErr(CodeConstant.DOUBLE_QUOTATION);
		// 配属データコード番号
		comDto.setSc006HaizokuNoErr(CodeConstant.DOUBLE_QUOTATION);
		// 社宅使用料会社間送金区分
		comDto.setSc006SokinShatakuSelectErr(CodeConstant.DOUBLE_QUOTATION);
		// 共益費会社間送付区分
		comDto.setSc006SokinKyoekihiSelectErr(CodeConstant.DOUBLE_QUOTATION);
		// 開始日
		comDto.setSc006StartDayErr(CodeConstant.DOUBLE_QUOTATION);
		// 終了日
		comDto.setSc006EndDayErr(CodeConstant.DOUBLE_QUOTATION);
		// 社宅賃貸料
		comDto.setSc006ChintaiRyoErr(CodeConstant.DOUBLE_QUOTATION);
		// 駐車場料金
		comDto.setSc006TyusyajoRyokinErr(CodeConstant.DOUBLE_QUOTATION);
		// 共益費(事業者負担)
		comDto.setSc006KyoekihiErr(CodeConstant.DOUBLE_QUOTATION);
	}
	
	/**
	 * 必須チェック
	 * @param comDto
	 * @param errMsg エラーメッセージ
	 */
	private void isErrEmpty(Skf3030Sc002CommonDto comDto ,StringBuilder errMsg){
		//使用料計算パターン未入力場合
		if(SkfCheckUtils.isNullOrEmpty(comDto.getSc006SiyoryoPatName())){
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "貸与規格");
			errMsg.append("必須チェックNG:貸与規格");
		}

		//原籍会社名
		comDto.setSc006OldKaisyaNameSelectErr("");
		if(SkfCheckUtils.isNullOrEmpty(comDto.getSc006OldKaisyaNameSelect())){
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "原籍会社名");
			comDto.setSc006OldKaisyaNameSelectErr("nfw-validation-error");
			comDto.setNextTabIndex(setDisplayTabIndex(Skf3030Sc002CommonDto.SELECT_TAB_INDEX_SHATAKU,comDto));
			errMsg.append("必須チェックNG:原籍会社名");
		}

		//給与支給会社
		comDto.setSc006KyuyoKaisyaSelectErr("");
		if(SkfCheckUtils.isNullOrEmpty(comDto.getSc006KyuyoKaisyaSelect())){
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "給与支給会社");
			comDto.setSc006KyuyoKaisyaSelectErr("nfw-validation-error");
			comDto.setNextTabIndex(setDisplayTabIndex(Skf3030Sc002CommonDto.SELECT_TAB_INDEX_SHATAKU,comDto));
			errMsg.append("必須チェックNG:給与支給会社");
		}

		//入居予定日未入力場合(★)
		comDto.setSc006NyukyoYoteiDayErr("");
		if(SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getSc006NyukyoYoteiDay()))){
			ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "入居予定日");
			comDto.setSc006NyukyoYoteiDayErr("nfw-validation-error");
			comDto.setNextTabIndex(setDisplayTabIndex(Skf3030Sc002CommonDto.SELECT_TAB_INDEX_SHATAKU,comDto));
			errMsg.append("必須チェックNG:入居予定日");
		}

		//区画１番号、区画１利用開始日未入力チェック
		comDto.setSc006RiyouStartDayOneErr("");
		if(!SkfCheckUtils.isNullOrEmpty(comDto.getSc006KukakuNoOne())){
			//区画１利用開始日未入力
			if(SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getSc006RiyouStartDayOne()))){
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "区画１利用開始日");
				comDto.setSc006RiyouStartDayOneErr("nfw-validation-error");
				comDto.setNextTabIndex(setDisplayTabIndex(Skf3030Sc002CommonDto.SELECT_TAB_INDEX_SHATAKU,comDto));
				errMsg.append("必須チェックNG：区画1利用開始日");
			}
		}else{
			if(!SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getSc006RiyouStartDayOne()))){
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "区画１区画番号");
				comDto.setNextTabIndex(setDisplayTabIndex(Skf3030Sc002CommonDto.SELECT_TAB_INDEX_SHATAKU,comDto));
				errMsg.append("必須チェックNG：区画1区画番号");
			}
		}

		////区画２番号、区画２利用開始日未入力チェック
		comDto.setSc006RiyouStartDayTwoErr("");
		if(!SkfCheckUtils.isNullOrEmpty(comDto.getSc006KukakuNoTwo())){
			//区画２利用開始日未入力
			if(SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getSc006RiyouStartDayTwo()))){
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "区画２利用開始日");
				comDto.setSc006RiyouStartDayTwoErr("nfw-validation-error");
				comDto.setNextTabIndex(setDisplayTabIndex(Skf3030Sc002CommonDto.SELECT_TAB_INDEX_SHATAKU,comDto));
				errMsg.append("必須チェックNG:区画2利用開始日");
			}
		}else{
			if(!SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getSc006RiyouStartDayTwo()))){
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "区画２区画番号");
				comDto.setNextTabIndex(setDisplayTabIndex(Skf3030Sc002CommonDto.SELECT_TAB_INDEX_SHATAKU,comDto));
				errMsg.append("必須チェックNG：区画2区画番号");
			}
		}

		//区画１利用終了日入力、区画１利用開始日未入力場合
		if(!SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getSc006RiyouEndDayOne()))){
			if(SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getSc006RiyouStartDayOne()))){
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "区画１利用開始日");
				comDto.setSc006RiyouStartDayOneErr("nfw-validation-error");
				comDto.setNextTabIndex(setDisplayTabIndex(Skf3030Sc002CommonDto.SELECT_TAB_INDEX_SHATAKU,comDto));
				errMsg.append("必須チェックNG：区画1利用開始日");
			}
		}

		//区画２利用終了日入力、区画２利用開始日未入力場合
		if(!SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getSc006RiyouEndDayTwo()))){
			if(SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getSc006RiyouStartDayTwo()))){
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "区画２利用開始日");
				comDto.setSc006RiyouStartDayTwoErr("nfw-validation-error");
				comDto.setNextTabIndex(setDisplayTabIndex(Skf3030Sc002CommonDto.SELECT_TAB_INDEX_SHATAKU,comDto));
				errMsg.append("必須チェックNG：区画2利用開始日");
			}
		}

		//備品ステータスのチェック
		Boolean isAllNashi = true;
		// 備品情報リスト
		List<Map<String, Object>> bihinList = new ArrayList<Map<String, Object>>();
		bihinList.addAll(jsonArrayToArrayList(comDto.getJsonBihin()));
		for (Map<String, Object> tmpBihin : bihinList) {
			//備品貸与状態ドロップダウン
			if(!CodeConstant.BIHIN_STATE_NONE.equals(tmpBihin.get("bihinTaiyoStts").toString()) ||
					SkfCheckUtils.isNullOrEmpty(tmpBihin.get("bihinTaiyoStts").toString())){
				isAllNashi = false;
				break;
			}
		}
		//備品貸与状態が1つでも"なし"以外の場合
		comDto.setSc006TaiyoDayErr("");
		if(!isAllNashi){
			//備品貸与日
			if( SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getSc006TaiyoDay()))){
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "貸与日");
				comDto.setSc006TaiyoDayErr("nfw-validation-error");
				comDto.setNextTabIndex(setDisplayTabIndex(Skf3030Sc002CommonDto.SELECT_TAB_INDEX_BIHIN,comDto));
				errMsg.append("必須チェックNG：貸与日①");
			}
		}else{
			//備品返却日入力、貸与日未入力場合
			if(!SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getSc006HenkyakuDay()))){
				if( SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getSc006TaiyoDay()))){
					ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "貸与日");
					comDto.setSc006TaiyoDayErr("nfw-validation-error");
					comDto.setNextTabIndex(setDisplayTabIndex(Skf3030Sc002CommonDto.SELECT_TAB_INDEX_BIHIN,comDto));
					errMsg.append("必須チェックNG：貸与日②");
				}
			}
		}

		//相互利用情報があり、且つ相互利用判定区分が”あり”の場合
		comDto.setSc006TaiyoKaisyaSelectErr("");
		comDto.setSc006KariukeKaisyaSelectErr("");
		comDto.setSc006StartDayErr("");
		if(comDto.getSc006SogoRyojokyoSelect().compareTo("1") == 0 &&
			comDto.getSc006SogoHanteiKbnSelect().compareTo("1") == 0 ){
			//貸付会社
			if( SkfCheckUtils.isNullOrEmpty(comDto.getSc006TaiyoKaisyaSelect())){
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "貸付会社");
				comDto.setSc006TaiyoKaisyaSelectErr("nfw-validation-error");
				comDto.setNextTabIndex(setDisplayTabIndex(Skf3030Sc002CommonDto.SELECT_TAB_INDEX_YAKUIN,comDto));
				errMsg.append("必須チェックNG：貸付会社");
			}
			//借受会社
			if( SkfCheckUtils.isNullOrEmpty(comDto.getSc006KariukeKaisyaSelect())){
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "借受会社");
				comDto.setSc006KariukeKaisyaSelectErr("nfw-validation-error");
				comDto.setNextTabIndex(setDisplayTabIndex(Skf3030Sc002CommonDto.SELECT_TAB_INDEX_YAKUIN,comDto));
				errMsg.append("必須チェックNG：借受会社");
			}
			//開始日
			if( SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getSc006StartDay()))){
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "開始日");
				comDto.setSc006StartDayErr("nfw-validation-error");
				comDto.setNextTabIndex(setDisplayTabIndex(Skf3030Sc002CommonDto.SELECT_TAB_INDEX_YAKUIN,comDto));
				errMsg.append("必須チェックNG：開始日");
			}
		}
	}
	
	/**
	 * 整合性チェック
	 * @param comDto
	 * @param errMsg エラーメッセージ<
	 */
	private void isErrDateFormat(Skf3030Sc002CommonDto comDto ,StringBuilder errMsg) throws ParseException{
		//退居予定日
		comDto.setSc006TaikyoYoteiDayErr("");
		if(!SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getSc006TaikyoYoteiDay()))){
			if(validateDateCorrelation(comDto.getSc006NyukyoYoteiDay(),comDto.getSc006TaikyoYoteiDay())){
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1133, "退居予定日");
				comDto.setSc006TaikyoYoteiDayErr("nfw-validation-error");
				comDto.setNextTabIndex(setDisplayTabIndex(Skf3030Sc002CommonDto.SELECT_TAB_INDEX_SHATAKU,comDto));
				errMsg.append("整合性チェックNG：貸与予定日");
			}else{
				comDto.setSc006TaikyoYoteiDayErr("");
			}
		}

		//区画１利用終了日
		comDto.setSc006RiyouEndDayOneErr("");
		if(!SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getSc006RiyouEndDayOne()))){
			if(validateDateCorrelation(comDto.getSc006RiyouStartDayOne(),comDto.getSc006RiyouEndDayOne())){
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1133, "区画１利用終了日");
				comDto.setSc006RiyouEndDayOneErr("nfw-validation-error");
				comDto.setNextTabIndex(setDisplayTabIndex(Skf3030Sc002CommonDto.SELECT_TAB_INDEX_SHATAKU,comDto));
				errMsg.append("整合性チェックNG：区画1利用終了日");
			}else{
				comDto.setSc006RiyouEndDayOneErr("");
			}
		}

		//区画２利用終了日
		comDto.setSc006RiyouEndDayTwoErr("");
		if(!SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getSc006RiyouEndDayTwo()))){
			if(validateDateCorrelation(comDto.getSc006RiyouStartDayTwo(),comDto.getSc006RiyouEndDayTwo())){
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1133, "区画２利用終了日");
				comDto.setSc006RiyouEndDayTwoErr("nfw-validation-error");
				comDto.setNextTabIndex(setDisplayTabIndex(Skf3030Sc002CommonDto.SELECT_TAB_INDEX_SHATAKU,comDto));
				errMsg.append("整合性チェックNG：区画2利用終了日");
			}else{
				comDto.setSc006RiyouEndDayTwoErr("");
			}
		}

		//返却日
		comDto.setSc006HenkyakuDayErr("");
		if(!SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getSc006HenkyakuDay()))){
			if(!SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getSc006TaiyoDay()))){
				if(validateDateCorrelation(comDto.getSc006TaiyoDay(),comDto.getSc006HenkyakuDay())){
					ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1133, "返却日");
					comDto.setSc006HenkyakuDayErr("nfw-validation-error");
					comDto.setNextTabIndex(setDisplayTabIndex(Skf3030Sc002CommonDto.SELECT_TAB_INDEX_BIHIN,comDto));
					errMsg.append("整合性チェックNG：返却日");
				}else{
					comDto.setSc006HenkyakuDayErr("");
				}
			}
			
		}

		//終了日
		comDto.setSc006EndDayErr("");
		if(!SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getSc006EndDay()))){
			if(!SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getSc006StartDay()))){
				if(validateDateCorrelation(comDto.getSc006StartDay(),comDto.getSc006EndDay())){
					ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1133, "終了日");
					comDto.setSc006EndDayErr("nfw-validation-error");
					comDto.setNextTabIndex(setDisplayTabIndex(Skf3030Sc002CommonDto.SELECT_TAB_INDEX_YAKUIN,comDto));
					errMsg.append("整合性チェックNG：終了日");
				}else{
					comDto.setSc006EndDayErr("");
				}
			}
			
		}


		//String negGetsu = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu(); //未使用

		//区画１と区画２一致チェック
		if(!SkfCheckUtils.isNullOrEmpty(comDto.getSc006KukakuNoOne()) && 
				!SkfCheckUtils.isNullOrEmpty(comDto.getSc006KukakuNoTwo())){
			if(Objects.equals(comDto.getSc006KukakuNoOne(), comDto.getSc006KukakuNoTwo())){
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_3047);
				comDto.setNextTabIndex(setDisplayTabIndex(Skf3030Sc002CommonDto.SELECT_TAB_INDEX_SHATAKU,comDto));
				errMsg.append("整合性チェックNG：区画1と区画2が同一");
			}
		}
	}
	
	/**
	 * パラメータのJSON文字列配列をリスト形式に変換して返却する
	 * 
	 * @param jsonStr	JSON文字列配列
	 * @return			List<Map<String, Object>>
	 */
	public List<Map<String, Object>> jsonArrayToArrayList (String jsonStr) {

		// 返却用リスト
		List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		// JSON文字列判定
		if (Objects.equals(jsonStr, null) || jsonStr.length() <= 0) {
			LogUtils.debugByMsg("文字列未設定");
			// 文字列未設定のため処理しない
			return listData;
		}
		try {
			JSONArray arr = null;
			arr = new JSONArray(jsonStr);
			if (arr != null) {
				int arrCnt = arr.length();
				ObjectMapper mapper = new ObjectMapper();
				for(int i = 0; i < arrCnt; i++) {
					listData.add(mapper.readValue(arr.get(i).toString(), new TypeReference<Map<String, Object>>(){}));
				}
				arr = null;
				mapper = null;
			}
		} catch (JSONException e) {
			LogUtils.infoByMsg("jsonArrayToArrayList, " + e.getMessage());
		} catch (JsonParseException e) {
			LogUtils.infoByMsg("jsonArrayToArrayList, " + e.getMessage());
		} catch (JsonMappingException e) {
			LogUtils.infoByMsg("jsonArrayToArrayList, " + e.getMessage());
		} catch (IOException e) {
			LogUtils.infoByMsg("jsonArrayToArrayList, " + e.getMessage());
		}
		return listData;
	}
	
	/**
	 * 日付整合性チェック
	 * 
	 * @param fromDate			from日付
	 * @param toDate			to日付
	 * @return					エラー：true(toがfromより過去日)、正常：false
	 * @throws ParseException
	 */
	private Boolean validateDateCorrelation(String fromDate, String toDate) throws ParseException {

		Boolean isCorrelationError = false;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date fDate = null;
		Date tDate = null;

		fDate = dateFormat.parse(getDateText(fromDate));
		tDate = dateFormat.parse(getDateText(toDate));
		// 整合性判定
		if(tDate.before(fDate)){
			// toDateがfromDateより過去日
			isCorrelationError = true;
		}
		return isCorrelationError;
	}
	
	/**
	 * 「月別駐車場履歴の利用終了日の最大値」（yyyyMMdd）取得メソッド
	 * @param parkingKanriNo 駐車場管理番号
	 * @param shatakuKanriNo 社宅管理番号
	 * @param shatakuKanriId 社宅管理台帳ID
	 * @return 利用終了日の最大値（yyyyMMdd）
	 */
	public String getMaxParkingEndDay(String parkingKanriNo, String shatakuKanriNo, String shatakuKanriId){
		
		String maxParkingEndDate = CodeConstant.DOUBLE_QUOTATION;
		
		Skf3030Sc002GetMaxParkingEndDayExpParameter maxEndDayParam = new Skf3030Sc002GetMaxParkingEndDayExpParameter();
		maxEndDayParam.setYearMonth(skfBaseBusinessLogicUtils.getSystemProcessNenGetsu());
		maxEndDayParam.setParkingKanriNo(Long.parseLong(parkingKanriNo));
		maxEndDayParam.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
		maxEndDayParam.setShatakuKanriId(Long.parseLong(shatakuKanriId));
		//「月別駐車場履歴の利用終了日の最大値」を取得する
		List<Skf3030Sc002GetMaxParkingEndDayExp> result = skf3030Sc002GetMaxParkingEndDayExpRepository.getMaxParkingEndDay(maxEndDayParam);
		
		if(result != null){
			if( result.size() > 0 && result.get(0) != null){
				maxParkingEndDate = result.get(0).getEnddate();
			}
		}
		
		return maxParkingEndDate;
	}
	
	/**
	 * 使用料計算パターンテーブルへのの登録・更新のための登録項目を設定
	 * 
	 * @param comDto	DTO
	 * @return
	 */
	public Map<Skf3030Sc002CommonDto.RENTAL_PATTERN, String> setRentalPatternList(Skf3030Sc002CommonDto comDto) {

		// 処理方式の判別
		if (CheckUtils.isEmpty(comDto.getHdnShiyoryoKeisanPatternId())) {
			// 使用料パターンID（hidden項目）が空白の場合、登録項目設定を行う。
			return setRentalPatternTorokuList(true, comDto);
		} else {
			// 使用料パターンID（hidden項目）が空白以外の場合、更新項目設定を行う
			return setRentalPatternTorokuList(false, comDto);
		}
	}

	/**
	 * 登録項目リストへのデータを設定するメソッド
	 * 
	 * @param torokuFlg	登録フラグ（true：登録、false：更新）
	 * @param comDto	DTO
	 * @return			登録項目リスト
	 */
	private Map<Skf3030Sc002CommonDto.RENTAL_PATTERN, String>
		setRentalPatternTorokuList(Boolean torokuFlg, Skf3030Sc002CommonDto comDto) {

		//		Dim list As New List(Of String)
		Map<Skf3030Sc002CommonDto.RENTAL_PATTERN, String> rentalPtMap =
				new HashMap<Skf3030Sc002CommonDto.RENTAL_PATTERN, String>();

		// データ設定に必要なセッション情報を取得。
//		Dim sessionInfo As New ShiyoryokeisanShienOutputEntity()
//		sessionInfo = DirectCast(Me.Session.Item(Constant.SessionId.SHIYORYO_KEISAN_SHIEN_OUTPUT_INFO),  _
//													ShiyoryokeisanShienOutputEntity)
//
		// 社宅管理番号
//		list.Add(Me.hdnShatakuKanriNo.Value)
		rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.SHATAKUKANRI_NO,
													comDto.getHdnShatakuKanriNo());
		// 使用料パターンID（登録：連番、更新：hidden使用料パターンID）
//		If torokuFlg Then
		if (torokuFlg) {
//			list.Add(S2007_TeijiDataRegistBusinessLogic.GetMaxRentalPatternId())
			rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.RENTAL_PATTERNID,
					skf3030Sc002GetMaxRentalPatternIdExpRepository.getMaxRentalPatternId());
//		Else
		} else {
//			list.Add(Me.hdnShiyoryoKeisanPatternId.Value)
			rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.RENTAL_PATTERNID,
												comDto.getHdnShiyoryoKeisanPatternId());
//		End If
		}

//		Me.hdnSiyouryoId.Value = list(1)
		comDto.setHdnRentalPatternId(
				rentalPtMap.get(Skf3030Sc002CommonDto.RENTAL_PATTERN.RENTAL_PATTERNID));
		// パターン名
//		If Not sessionInfo Is Nothing Then
		if (!CheckUtils.isEmpty(comDto.getHdnRateShienPatternName())) {
//			list.Add(sessionInfo.PatternName)
			rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.PATTERN_NAME,
											comDto.getHdnRateShienPatternName());
//		Else
		} else {
//			list.Add(String.Empty)
			rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.PATTERN_NAME,
													null);
//		End If
		}
		// 規格
//		If Not sessionInfo Is Nothing Then
		if (!CheckUtils.isEmpty(comDto.getHdnRateShienKikaku())) {
//			list.Add(sessionInfo.Kikaku)
			rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.KIKAKU,
													comDto.getHdnRateShienKikaku());
//		Else
		} else {
//			list.Add(String.Empty)
			rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.KIKAKU,
													null);
//		End If
		}
		// 規格（補足）（登録値無し）
//		list.Add(String.Empty)
		rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.KIKAKU_HOSOKU,
													null);
		// 基準使用料算定上延べ面積
//		If Not sessionInfo Is Nothing Then
		if (!CheckUtils.isEmpty(comDto.getHdnRateShienKijunMenseki())) {
//			list.Add(sessionInfo.KijunMenseki.ToString())
			rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.KIJUN_MENSEKI,
								getMensekiText(comDto.getHdnRateShienKijunMenseki()));
//		Else
		} else {
//			list.Add(String.Empty)
			rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.KIJUN_MENSEKI,
					CodeConstant.STRING_ZERO);
//		End If
		}
		// 社宅使用料算定上延べ面積
//		If Not sessionInfo Is Nothing Then
		if (!CheckUtils.isEmpty(comDto.getHdnRateShienShatakuMenseki())) {
//			list.Add(sessionInfo.ShatakuMenseki.ToString())
			rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.SHATAKU_MENSEKI,
									getMensekiText(comDto.getHdnRateShienShatakuMenseki()));
//		Else
		} else {
//			list.Add(String.Empty)
			rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.SHATAKU_MENSEKI,
					CodeConstant.STRING_ZERO);
//		End If
		}
		// 経年残価率
//		If Not sessionInfo Is Nothing Then
		if (!CheckUtils.isEmpty(comDto.getHdnRateShienKeinenZankaRitsu())) {
//			list.Add(sessionInfo.KeinenZankaRitsu.ToString())
			rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.KEINEN_ZANKARITSU,
											comDto.getHdnRateShienKeinenZankaRitsu());
//		Else
		} else {
//			list.Add(String.Empty)
			rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.KEINEN_ZANKARITSU,
					CodeConstant.STRING_ZERO);
//		End If
		}
		// 用途
//		If Not sessionInfo Is Nothing Then
		if (!CheckUtils.isEmpty(comDto.getHdnRateShienYoto())) {
//			list.Add(sessionInfo.Yoto)
			rentalPtMap.put(
					Skf3030Sc002CommonDto.RENTAL_PATTERN.YOTO, comDto.getHdnRateShienYoto());
//		Else
		} else {
//			list.Add(String.Empty)
			rentalPtMap.put(
					Skf3030Sc002CommonDto.RENTAL_PATTERN.YOTO, null);
//		End If
		}
		// 寒冷地調整フラグ（登録値無し）
//		list.Add(String.Empty)
		rentalPtMap.put(
				Skf3030Sc002CommonDto.RENTAL_PATTERN.KANREICHI, null);
		// 狭小調整フラグ（登録値無し）
//		list.Add(String.Empty)
		rentalPtMap.put(
				Skf3030Sc002CommonDto.RENTAL_PATTERN.KYOUSYOU, null);
		// 経年
//		If Not sessionInfo Is Nothing Then
		if (!CheckUtils.isEmpty(comDto.getHdnRateShienKeinen())) {
//			list.Add(sessionInfo.Keinen.ToString())
			rentalPtMap.put(
					Skf3030Sc002CommonDto.RENTAL_PATTERN.KEINEN, comDto.getHdnRateShienKeinen());
//		Else
		} else {
//			list.Add(String.Empty)
			rentalPtMap.put(
					Skf3030Sc002CommonDto.RENTAL_PATTERN.KEINEN, CodeConstant.STRING_ZERO);
//		End If
		}
		// 基本使用料
//		If Not sessionInfo Is Nothing Then
		if (!CheckUtils.isEmpty(comDto.getHdnRateShienKihonShiyoryo())) {
//			list.Add(sessionInfo.KihonShiyoryo.ToString())
			rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.KIHON_SHIYORYO,
									getPayText(comDto.getHdnRateShienKihonShiyoryo()));
//		Else
		} else {
//			list.Add(String.Empty)
			rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.KIHON_SHIYORYO,
					CodeConstant.STRING_ZERO);
//		End If
		}
		// 単価
//		If Not sessionInfo Is Nothing Then
		if (!CheckUtils.isEmpty(comDto.getHdnRateShienTanka())) {
//			list.Add(sessionInfo.Tanka.ToString())
			rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.TANKA,
									getPayText(comDto.getHdnRateShienTanka()));
//		Else
		} else {
//			list.Add(String.Empty)
			rentalPtMap.put(
					Skf3030Sc002CommonDto.RENTAL_PATTERN.TANKA, CodeConstant.STRING_ZERO);
//		End If
		}
		// 社宅使用料月額
//		If Not sessionInfo Is Nothing Then
		if (!CheckUtils.isEmpty(comDto.getHdnRateShienShatakuGetsugaku())) {
//			list.Add(Me.GetPayText(Me.lblSiyoryoMonthPay.Text))
			// kami 既存バグ？セッションではなくラベルの値を設定しているが・・・
			rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.SHATAKU_GETSUGAKU,
									getPayText(comDto.getSc006SiyoryoMonthPay()));
//		Else
		} else {
//			list.Add(String.Empty)
			rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.SHATAKU_GETSUGAKU,
					CodeConstant.STRING_ZERO);
//		End If
		}
		// 備考（登録値無し）
//		list.Add(String.Empty)
		rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.BIKO, null);
		// 補足資料名（登録値無し）
//		list.Add(String.Empty)
		rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.HOSOKU_SHIRYO_NAME,
														null);
		// 補足資料サイズ（登録値無し）
//		list.Add(String.Empty)
		rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.HOSOKU_SHIRYO_SIZE,
														null);
		// 補足資料ファイル（登録値無し）
//		list.Add(String.Empty)
		rentalPtMap.put(
				Skf3030Sc002CommonDto.RENTAL_PATTERN.HOSOKU_FILE, null);
		// 削除フラグ（登録：”0”、更新：登録値無し）
//		If torokuFlg Then
		if (torokuFlg) {
//			list.Add(DELETE_FLG_NOT_DELETED)
			// 登録
			rentalPtMap.put(
					Skf3030Sc002CommonDto.RENTAL_PATTERN.DELETE_FLAG, CodeConstant.STRING_ZERO);
//		Else
		} else {
//			list.Add(String.Empty)
			rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.DELETE_FLAG,
													null);
//		End If
		}
		// 作成日（システムから設定するため登録値無し）
//		list.Add(String.Empty)
		rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.INSERT_DATE,
													CodeConstant.DOUBLE_QUOTATION);
		// 作成者（登録：作成者情報、更新：登録値無し）
//		If torokuFlg Then
//			list.Add(MyBase.userInfo.UserId)
//		Else
//			list.Add(String.Empty)
//		End If
		rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.INSERT_USER_ID,
													CodeConstant.DOUBLE_QUOTATION);
		// 作成IPアドレス（登録：作成者IPアドレス、更新：登録値無し）
//		If torokuFlg Then
//			list.Add(MyBase.publicInfo.IpAddress)
//		Else
//			list.Add(String.Empty)
//		End If
		rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.INSERT_PROGRAM_ID,
													CodeConstant.DOUBLE_QUOTATION);
		// 更新日（システムから設定するため登録値無し）
//		list.Add(String.Empty)
		rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.UPDATE_DATE,
													CodeConstant.DOUBLE_QUOTATION);
		// 更新者
//		list.Add(MyBase.userInfo.UserId)
		rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.UPDATE_USER_ID,
													CodeConstant.DOUBLE_QUOTATION);
		// 更新IPアドレス
//		list.Add(MyBase.publicInfo.IpAddress)
		rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.UPDATE_PROGRAM_ID,
													CodeConstant.DOUBLE_QUOTATION);
		// 延べ面積
//		If Not sessionInfo Is Nothing Then
		if (!CheckUtils.isEmpty(comDto.getHdnRateShienNobeMenseki())) {
//			list.Add(sessionInfo.NobeMenseki.ToString())
			rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.NOBE_MENSEKI,
									getMensekiText(comDto.getHdnRateShienNobeMenseki()));
//		Else
		} else {
//			list.Add(String.Empty)
			rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.NOBE_MENSEKI,
													CodeConstant.STRING_ZERO);
//		End If
		}
		// サンルーム面積
//		If Not sessionInfo Is Nothing Then
		if (!CheckUtils.isEmpty(comDto.getHdnRateShienSunroomMenseki())) {
//			list.Add(sessionInfo.SunroomMenseki.ToString())
			rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.SUNROOM_MENSEKI,
									getMensekiText(comDto.getHdnRateShienSunroomMenseki()));
//		Else
		} else {
//			list.Add(String.Empty)
			rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.SUNROOM_MENSEKI,
					CodeConstant.STRING_ZERO);
//		End If
		}
		// 階段面積
//		If Not sessionInfo Is Nothing Then
		if (!CheckUtils.isEmpty(comDto.getHdnRateShienKaidanMenseki())) {
//			list.Add(sessionInfo.KaidanMenseki.ToString())
			rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.KAIDAN_MENSEKI,
								getMensekiText(comDto.getHdnRateShienKaidanMenseki()));
//		Else
		} else {
//			list.Add(String.Empty)
			rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.KAIDAN_MENSEKI,
					CodeConstant.STRING_ZERO);
//		End If
		}
		// 物置面積
//		If Not sessionInfo Is Nothing Then
		if (!CheckUtils.isEmpty(comDto.getHdnRateShienMonookiMenseki())) {
//			list.Add(sessionInfo.MonookiMenseki.ToString())
			rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.MONOOKI_MENSEKI,
								getMensekiText(comDto.getHdnRateShienMonookiMenseki()));
//		Else
		} else {
//			list.Add(String.Empty)
			rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.MONOOKI_MENSEKI,
					CodeConstant.STRING_ZERO);
//		End If
		}
		// 登録・更新判断用（登録：0、更新：1）
//		If torokuFlg Then
		if (torokuFlg) {
//			list.Add(DATA_0)
			// 登録
			rentalPtMap.put(
					Skf3030Sc002CommonDto.RENTAL_PATTERN.UPDATE_KIND, CodeConstant.STRING_ZERO);
//		Else
		} else {
//			list.Add(DATA_1)
			// 更新
			rentalPtMap.put(Skf3030Sc002CommonDto.RENTAL_PATTERN.UPDATE_KIND, "1");
//		End If
		}
		return rentalPtMap;
	}
	
	/**
	 * 提示データ情報の取得
	 * @param teijiNo
	 * @param comDto
	 * @return
	 */
	public Skf3022TTeijiData getTbtTeijiDataColumnInfoList(Long teijiNo, Skf3030Sc002CommonDto comDto){
		Skf3022TTeijiData columnInfoList = new Skf3022TTeijiData();
		
		//提示番号
		columnInfoList.setTeijiNo(teijiNo);
		//社員番号
		columnInfoList.setShainNo(comDto.getSc006ShainNo().replace(CodeConstant.ASTERISK, "")); 
		//入退居区分
		columnInfoList.setNyutaikyoKbn(STR_ONE);
		//社員名
		columnInfoList.setName(getToRegistString(comDto.getSc006ShainName()));
		//申請区分
		columnInfoList.setApplKbn(STR_ONE);
		//社宅管理番号
		if (!CheckUtils.isEmpty(comDto.getHdnShatakuKanriNo())) {
			columnInfoList.setShatakuKanriNo(Long.parseLong(comDto.getHdnShatakuKanriNo()));
		}
		//部屋管理番号
		if (!CheckUtils.isEmpty(comDto.getHdnShatakuRoomKanriNo())) {
			columnInfoList.setShatakuRoomKanriNo(Long.parseLong(comDto.getHdnShatakuRoomKanriNo()));
		}
		//使用料パターンＩＤ
		if (!CheckUtils.isEmpty(comDto.getHdnRentalPatternId())) {
			columnInfoList.setRentalPatternId(Long.parseLong(comDto.getHdnRentalPatternId()));
		}
		//居住者区分
		columnInfoList.setKyojushaKbn(getToRegistString(comDto.getSc006KyojyusyaKbnSelect()));
		//役員算定
		columnInfoList.setYakuinSannteiKbn(getToRegistString(comDto.getSc006YakuinSanteiSelect()));
		//社宅使用料日割金額
		columnInfoList.setRentalDay(Integer.parseInt(getPayText(comDto.getHdnKaiSanAfterShatakuShiyoryoHiwariKingaku())));
		//社宅使用料調整金額
		columnInfoList.setRentalAdjust(Integer.parseInt(getPayText(comDto.getSc006SiyoroTyoseiPay())));
		//個人負担共益費
		columnInfoList.setKyoekihiPerson(Integer.parseInt(getPayText(comDto.getSc006KyoekihiMonthPay())));
		//個人負担共益費調整金額
		columnInfoList.setKyoekihiPersonAdjust(
				Integer.parseInt(getPayText(comDto.getSc006KyoekihiTyoseiPay())));
		//共益金支払月
		columnInfoList.setKyoekihiPayMonth(getToRegistString(comDto.getSc006KyoekihiPayMonthSelect()));
		//区画１駐車場管理番号
		if (!CheckUtils.isEmpty(comDto.getHdnChushajoKanriNo1())) {
			columnInfoList.setParkingKanriNo1(Long.parseLong(comDto.getHdnChushajoKanriNo1()));
		}
		//駐車場区画１番号
		columnInfoList.setParkingBlock1(getToRegistString(comDto.getSc006KukakuNoOne()));
		//駐車場区画１開始日
		columnInfoList.setParking1StartDate(getRegistDateText(comDto.getSc006RiyouStartDayOne()));
		//駐車場区画１終了日
		columnInfoList.setParking1EndDate(getRegistDateText(comDto.getSc006RiyouEndDayOne()));
		//区画２駐車場管理番号
		if (!CheckUtils.isEmpty(comDto.getHdnChushajoKanriNo2())) {
			columnInfoList.setParkingKanriNo2(Long.parseLong(comDto.getHdnChushajoKanriNo2()));
		}
		//駐車場区画２番号
		columnInfoList.setParkingBlock2(getToRegistString(comDto.getSc006KukakuNoTwo()));
		//駐車場区画２開始日
		columnInfoList.setParking2StartDate(getRegistDateText(comDto.getSc006RiyouStartDayTwo()));
		//駐車場区画２終了日
		columnInfoList.setParking2EndDate(getRegistDateText(comDto.getSc006RiyouEndDayTwo()));
		//駐車場使用料調整金額
		columnInfoList.setParkingRentalAdjust(
				Integer.parseInt(getPayText(comDto.getSc006TyusyaTyoseiPay())));
		//駐車場日割金額１
		columnInfoList.setParking1RentalDay(
				Integer.parseInt(getPayText(comDto.getHdnKaiSanAfterKukaku1ChushajoShiyoroHiwariKingaku())));
		//駐車場日割金額２
		columnInfoList.setParking2RentalDay(
				Integer.parseInt(getPayText(comDto.getHdnKaiSanAfterKukaku2ChushajoShiyoroHiwariKingaku())));
		//備考
		columnInfoList.setBiko(getToRegistString(comDto.getSc006Bicou()));
		//備品貸与日
		columnInfoList.setEquipmentStartDate(getRegistDateText(comDto.getSc006TaiyoDay()));
		//備品返却日
		columnInfoList.setEquipmentEndDate(getRegistDateText(comDto.getSc006HenkyakuDay()));
		//搬入希望日
		columnInfoList.setCarryinRequestDay(getRegistDateText(comDto.getSc006KibouDayIn()));
		//搬入希望時間区分
		columnInfoList.setCarryinRequestKbn(getToRegistString(comDto.getSc006KibouTimeInSelect()));
		//受入本人連絡先
		columnInfoList.setUkeireMyApoint(getToRegistString(comDto.getSc006HonninAddrIn()));
		//受入代理人氏名
		columnInfoList.setUkeireDairiName(getToRegistString(comDto.getSc006UketoriDairiInName()));
		//受入代理人連絡先
		columnInfoList.setUkeireDairiApoint(getToRegistString(comDto.getSc006UketoriDairiAddr()));
		//搬出希望日
		columnInfoList.setCarryoutRequestDay(getRegistDateText(comDto.getSc006KibouDayOut()));
		//搬出希望時間区分
		columnInfoList.setCarryoutRequestKbn(getToRegistString(comDto.getSc006KibouTimeOutSelect()));
		//立会本人連絡先
		columnInfoList.setTatiaiMyApoint(getToRegistString(comDto.getSc006HonninAddrOut()));
		//立会代理人氏名
		columnInfoList.setTatiaiDairiName(getToRegistString(comDto.getSc006TachiaiDairi()));
		//立会代理人連絡先
		columnInfoList.setTatiaiDairiApoint(getToRegistString(comDto.getSc006TachiaiDairiAddr()));
		//代理人備考
		columnInfoList.setDairiKiko(getToRegistString(comDto.getSc006DairiBiko()));
		//備品備考
		columnInfoList.setBihinBiko(getToRegistString(comDto.getSc006BihinBiko()));
		//相互利用状況
		columnInfoList.setMutualJokyo(getToRegistString(comDto.getSc006SogoRyojokyoSelect()));
		//貸付会社コード
		columnInfoList.setKashitukeCompanyCd(getToRegistString(comDto.getSc006TaiyoKaisyaSelect()));
		//借受会社コード
		columnInfoList.setKariukeCompanyCd(getToRegistString(comDto.getSc006KariukeKaisyaSelect()));
		//相互利用判定区分
		columnInfoList.setMutualUseKbn(getToRegistString(comDto.getSc006SogoHanteiKbnSelect()));
		//社宅賃貸料
		columnInfoList.setRent(Integer.parseInt(getPayText(comDto.getSc006ChintaiRyo())));
		//駐車場料金
		columnInfoList.setParkingRental(Integer.parseInt(getPayText(comDto.getSc006TyusyajoRyokin())));
		//共益費（事業者負担）
		columnInfoList.setKyoekihiBusiness(Integer.parseInt(getPayText(comDto.getSc006Kyoekihi())));
		//相互利用開始日
		columnInfoList.setMutualUseStartDay(getRegistDateText(comDto.getSc006StartDay()));
		//相互利用終了日
		columnInfoList.setMutualUseEndDay(getRegistDateText(comDto.getSc006EndDay()));
		//配属会社コード
		columnInfoList.setAssignCompanyCd(getToRegistString(comDto.getSc006HaizokuKaisyaSelect()));
		//所属機関
		columnInfoList.setAgency(getToRegistString(comDto.getSc006SyozokuKikan()));
		//室・部名
		columnInfoList.setAffiliation1(getToRegistString(comDto.getSc006SituBuName()));
		//課等名
		columnInfoList.setAffiliation2(getToRegistString(comDto.getSc006KanadoMei()));
		//配属データコード番号
		columnInfoList.setAssignCd(getToRegistString(comDto.getSc006HaizokuNo()));
		//原籍会社コード
		columnInfoList.setOriginalCompanyCd(getToRegistString(comDto.getSc006OldKaisyaNameSelect()));
		//給与支給会社区分
		columnInfoList.setPayCompanyCd(getToRegistString(comDto.getSc006KyuyoKaisyaSelect()));
		//社宅使用料会社間送金区分
		columnInfoList.setShatakuCompanyTransferKbn(getToRegistString(comDto.getSc006SokinShatakuSelect()));
		//共益費会社間送金区分
		columnInfoList.setKyoekihiCompanyTransferKbn(getToRegistString(comDto.getSc006SokinKyoekihiSelect()));
		//社宅提示ステータス（承認済み）
		columnInfoList.setShatakuTeijiStatus(CodeConstant.PRESENTATION_SITUATION_SHONIN);

		//備品貸与区分
		if(SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getSc006TaiyoDay()))){
			columnInfoList.setBihinTaiyoKbn(CodeConstant.BIHIN_TAIYO_KBN_FUYO);
		}else{
			columnInfoList.setBihinTaiyoKbn(CodeConstant.BIHIN_TAIYO_KBN_HITSUYO);
		}

		//備品提示ステータス
		if(SkfCheckUtils.isNullOrEmpty(getDateText(comDto.getSc006TaiyoDay()))){
			columnInfoList.setBihinTeijiStatus(CodeConstant.DOUBLE_QUOTATION);
		}else{
			columnInfoList.setBihinTeijiStatus(CodeConstant.BIHIN_STATUS_SHONIN);
		}

		//作成完了区分
		columnInfoList.setCreateCompleteKbn(CodeConstant.MI_SAKUSEI);
		//台帳作成区分
		columnInfoList.setLandCreateKbn(CodeConstant.LAND_CREATE_KBN_MI_SAKUSEI);
		//登録日時
		columnInfoList.setInsertDate(skfBaseBusinessLogicUtils.getSystemDateTime());
		//登録者ID
		columnInfoList.setInsertUserId(LoginUserInfoUtils.getUserCd());
		//登録機能ID
		columnInfoList.setInsertProgramId(comDto.getPageId());

		//更新日時
		columnInfoList.setUpdateDate(skfBaseBusinessLogicUtils.getSystemDateTime());
		//更新者ID
		columnInfoList.setUpdateUserId(LoginUserInfoUtils.getUserCd());
		//更新機能ID
		columnInfoList.setUpdateProgramId(comDto.getPageId());
		
		//【使用料計算機能対応】追加分項目
		//社宅使用料月額
		columnInfoList.setRentalMonth(Integer.parseInt(getPayText(comDto.getSc006ShiyoryoTsukigaku())));
		//駐車場使用料月額１
		columnInfoList.setParking1RentalMonth(Integer.parseInt(getPayText(comDto.getSc006TyusyaMonthPayOne())));
		//駐車場使用料月額２
		columnInfoList.setParking2RentalMonth(
				Integer.parseInt(getPayText(comDto.getSc006TyusyaMonthPayTwo())));
		
		return columnInfoList;
	}
	
	
	/**
	 * 社宅管理台帳基本テーブル情報の取得
	 * @param comDto
	 * @return
	 */
	public Skf3030TShatakuLedger getTbtPublicShatakuLedgerDataColumnInfoList(Skf3030Sc002CommonDto comDto){
		Skf3030TShatakuLedger columnInfoList = new Skf3030TShatakuLedger();
		
		//社宅管理台帳ID
		columnInfoList.setShatakuKanriId(Long.parseLong(comDto.getHdnShatakuKanriId()));
		//入居予定日
		columnInfoList.setNyukyoDate(getRegistDateText(comDto.getSc006NyukyoYoteiDay()));
		//退居予定日
		columnInfoList.setTaikyoDate(getRegistDateText(comDto.getSc006TaikyoYoteiDay()));
		//居住者区分
		columnInfoList.setKyojushaKbn(getToRegistString(comDto.getSc006KyojyusyaKbnSelect()));

		//備考
		columnInfoList.setBiko(getToRegistString(comDto.getSc006Bicou()));

		//更新日時
		columnInfoList.setUpdateDate(skfBaseBusinessLogicUtils.getSystemDateTime());
		//更新者ID
		columnInfoList.setUpdateUserId(LoginUserInfoUtils.getUserCd());
		//更新機能ID
		columnInfoList.setUpdateProgramId(comDto.getPageId());
		
		//原籍会社コード
		columnInfoList.setOriginalCompanyCd(getToRegistString(comDto.getSc006OldKaisyaNameSelect()));
		//給与支給会社コード
		columnInfoList.setPayCompanyCd(getToRegistString(comDto.getSc006KyuyoKaisyaSelect()));
		
		
		return columnInfoList;
	}
	
	/**
	 * 社宅管理台帳備品基本テーブル情報の取得
	 * @param comDto
	 * @return
	 */
	public Skf3030TShatakuBihin getTbtPublicShatakuBihinDataColumnInfoList(Skf3030Sc002CommonDto comDto){
		Skf3030TShatakuBihin columnInfoList = new Skf3030TShatakuBihin();
		
		//社宅管理台帳ID
		columnInfoList.setShatakuKanriId(Long.parseLong(comDto.getHdnShatakuKanriId()));

		//備品貸与日
		columnInfoList.setTaiyoDate(getRegistDateText(comDto.getSc006TaiyoDay()));
		//搬入希望日
		columnInfoList.setHannyuRequestDay(getRegistDateText(comDto.getSc006KibouDayIn()));
		//搬入希望時間帯
		columnInfoList.setHannyuRequestKbn(getToRegistString(comDto.getSc006KibouTimeInSelect()));
		//受入本人連絡先
		columnInfoList.setUkeireMyApoint(getToRegistString(comDto.getSc006HonninAddrIn()));
		//受取代理人
		columnInfoList.setUkeireDairiName(getToRegistString(comDto.getSc006UketoriDairiInName()));
		//受取代理人連絡先
		columnInfoList.setUkeireDairiApoint(getToRegistString(comDto.getSc006UketoriDairiAddr()));
		
		//備品返却日
		columnInfoList.setHenkyakuDate(getRegistDateText(comDto.getSc006HenkyakuDay()));
		//搬出希望日
		columnInfoList.setHansyutuRequestDay(getRegistDateText(comDto.getSc006KibouDayOut()));
		//搬出希望時間帯
		columnInfoList.setHansyutuRequestKbn(getToRegistString(comDto.getSc006KibouTimeOutSelect()));
		//立会本人連絡先
		columnInfoList.setTatiaiMyApoint(getToRegistString(comDto.getSc006HonninAddrOut()));
		//立会代理人
		columnInfoList.setTatiaiDairiName(getToRegistString(comDto.getSc006TachiaiDairi()));
		//立会代理人連絡先
		columnInfoList.setTatiaiDairiApoint(getToRegistString(comDto.getSc006TachiaiDairiAddr()));
		//代理人備考
		columnInfoList.setDairiBiko(getToRegistString(comDto.getSc006DairiBiko()));
		//備品情報備考
		columnInfoList.setBihinBiko(getToRegistString(comDto.getSc006BihinBiko()));

		//更新日時
		columnInfoList.setUpdateDate(skfBaseBusinessLogicUtils.getSystemDateTime());
		//更新者ID
		columnInfoList.setUpdateUserId(LoginUserInfoUtils.getUserCd());
		//更新機能ID
		columnInfoList.setUpdateProgramId(comDto.getPageId());

		return columnInfoList;
	}
	
	/**
	 * 社宅管理台帳相互利用基本テーブル情報の取得
	 * @param comDto
	 * @return
	 */
	public Skf3030TShatakuMutual getTbtPublicShatakuMutualDataColumnInfoList(Skf3030Sc002CommonDto comDto){
		Skf3030TShatakuMutual columnInfoList  = new Skf3030TShatakuMutual();
		
		//社宅管理台帳ID
		columnInfoList.setShatakuKanriId(Long.parseLong(comDto.getHdnShatakuKanriId()));
		//相互利用状況
		columnInfoList.setMutualJokyo(getToRegistString(comDto.getSc006SogoRyojokyoSelect()));
		//貸付会社コード
		columnInfoList.setKashitukeCompanyCd(getToRegistString(comDto.getSc006TaiyoKaisyaSelect()));
		//借受会社コード
		columnInfoList.setKariukeCompanyCd(getToRegistString(comDto.getSc006KariukeKaisyaSelect()));
		//相互利用判定区分
		columnInfoList.setMutualUseKbn(getToRegistString(comDto.getSc006SogoHanteiKbnSelect()));
		//社宅賃貸料
		columnInfoList.setRent(Integer.parseInt(getPayText(comDto.getSc006ChintaiRyo())));
		//駐車場料金
		columnInfoList.setParkingRental(Integer.parseInt(getPayText(comDto.getSc006TyusyajoRyokin())));
		//共益費（事業者負担）
		columnInfoList.setKyoekihiBusiness(Integer.parseInt(getPayText(comDto.getSc006Kyoekihi())));
		//開始日
		columnInfoList.setMutualUseStartDay(getRegistDateText(comDto.getSc006StartDay()));
		//終了日
		columnInfoList.setMutualUseEndDay(getRegistDateText(comDto.getSc006EndDay()));
		//社宅使用料会社間送金区分
		columnInfoList.setShatakuCompanyTransferKbn(getToRegistString(comDto.getSc006SokinShatakuSelect()));
		//共益費会社間送金区分
		columnInfoList.setKyoekihiCompanyTransferKbn(getToRegistString(comDto.getSc006SokinKyoekihiSelect()));
		
		//更新日時
		columnInfoList.setUpdateDate(skfBaseBusinessLogicUtils.getSystemDateTime());
		//更新者ID
		columnInfoList.setUpdateUserId(LoginUserInfoUtils.getUserCd());
		//更新機能ID
		columnInfoList.setUpdateProgramId(comDto.getPageId());

		return columnInfoList;
	}
	
	/**
	 * 月別使用料履歴テーブル情報の取得
	 * @param comDto
	 * @return
	 */
	public Skf3030TShatakuRentalRireki getTbtPublicShatakuRentalRirekiDataColumnInfoList(Skf3030Sc002CommonDto comDto){
		Skf3030TShatakuRentalRireki columnInfoList = new Skf3030TShatakuRentalRireki();
		//社宅管理台帳ID
		columnInfoList.setShatakuKanriId(Long.parseLong(comDto.getHdnShatakuKanriId()));
		//対象年月
		columnInfoList.setYearMonth(comDto.getHdnNengetsu());
		//使用料パターンID
		columnInfoList.setRentalPatternId(Long.parseLong(comDto.getHdnRentalPatternId()));
		//役員算定
		columnInfoList.setYakuinSannteiKbn(getToRegistString(comDto.getSc006YakuinSanteiSelect()));
		//社宅使用料月額 
		columnInfoList.setRentalMonth(Integer.parseInt(getPayText(comDto.getHdnKaiSanAfterShatakuShiyoryoGetsugaku())));
		//社宅使用料日割金額
		columnInfoList.setRentalDay(Integer.parseInt(getPayText(comDto.getHdnKaiSanAfterShatakuShiyoryoHiwariKingaku())));
		//社宅使用料調整金額
		columnInfoList.setRentalAdjust(Integer.parseInt(getPayText(comDto.getSc006SiyoroTyoseiPay())));
		//社宅使用料月額（調整後）
		columnInfoList.setRentalTotal(Integer.parseInt(getPayText(comDto.getHdnKaiSanAfterShatakuShiyoryoGetsugakuChoseigo())));
		//個人負担共益費月額
		columnInfoList.setKyoekihiPerson(Integer.parseInt(getPayText(comDto.getSc006KyoekihiMonthPay())));
		//個人負担共益費調整金額
		columnInfoList.setKyoekihiPersonAdjust(Integer.parseInt(getPayText(comDto.getSc006KyoekihiTyoseiPay())));
		//個人負担共益費月額（調整後）
		columnInfoList.setKyoekihiPersonTotal(Integer.parseInt(getPayText(comDto.getHdnKaiSanAfterKojinFutanKyoekihiGetsugakuChoseigo())));
		//共益費支払月
		columnInfoList.setKyoekihiPayMonth(getToRegistString(comDto.getSc006KyoekihiPayMonthSelect()));
		//区画１駐車場使用料月額
		columnInfoList.setParking1RentalMonth(Integer.parseInt(getPayText(comDto.getHdnKaiSanAfterKukaku1ChushajoShiyoroGetsugaku())));
		//区画１駐車場使用料日割金額 
		columnInfoList.setParking1RentalDay(Integer.parseInt(getPayText(comDto.getHdnKaiSanAfterKukaku1ChushajoShiyoroHiwariKingaku())));
		//区画２駐車場使用料月額
		columnInfoList.setParking2RentalMonth(Integer.parseInt(getPayText(comDto.getHdnKaiSanAfterKukaku2ChushajoShiyoroGetsugaku())));
		//区画２駐車場使用料日割金額
		columnInfoList.setParking2RentalDay(Integer.parseInt(getPayText(comDto.getHdnKaiSanAfterKukaku2ChushajoShiyoroHiwariKingaku())));
		//駐車場使用料調整金額
		columnInfoList.setParkingRentalAdjust(Integer.parseInt(getPayText(comDto.getSc006TyusyaTyoseiPay())));
		//駐車場使用料月額（調整後）
		columnInfoList.setParkingRentalTotal(Integer.parseInt(getPayText(comDto.getHdnKaiSanAfterChushajoShiyoryoGetsugakuChoseigo())));

		//更新日時
		columnInfoList.setUpdateDate(skfBaseBusinessLogicUtils.getSystemDateTime());
		//更新者ID
		columnInfoList.setUpdateUserId(LoginUserInfoUtils.getUserCd());
		//更新機能ID
		columnInfoList.setUpdateProgramId(comDto.getPageId());
		
		return columnInfoList;
	}
	
	/**
	 * 月別駐車場履歴テーブル 区画情報の取得
	 * @param kugakuFlg
	 * @param updateMode
	 * @param comDto
	 * @return
	 */
	public Skf3030TParkingRireki getTbtPublicParkingRirekiDataColumnInfoList(int kugakuFlg,Boolean updateMode,Skf3030Sc002CommonDto comDto){
		Skf3030TParkingRireki columnInfoList = new Skf3030TParkingRireki();
		//社宅管理台帳ID
		columnInfoList.setShatakuKanriId(Long.parseLong(comDto.getHdnShatakuKanriId()));
		//対象年月
		columnInfoList.setYearMonth(comDto.getHdnNengetsu());

		if( kugakuFlg == 1 ){
			//貸与番号
			columnInfoList.setParkingLendNo(1L);
			//駐車場管理番号
			columnInfoList.setParkingKanriNo(stringParseToLong(comDto.getHdnChushajoKanriNo1()));
			//利用開始日
			columnInfoList.setParkingStartDate(getRegistDateText(comDto.getSc006RiyouStartDayOne()));
			//利用終了日
			columnInfoList.setParkingEndDate(getRegistDateText(comDto.getSc006RiyouEndDayOne()));
		}else if( kugakuFlg == 2 ){
			//貸与番号
			columnInfoList.setParkingLendNo(2L);
			//駐車場管理番号
			columnInfoList.setParkingKanriNo(stringParseToLong(comDto.getHdnChushajoKanriNo2()));
			//利用開始日
			columnInfoList.setParkingStartDate(getRegistDateText(comDto.getSc006RiyouStartDayTwo()));
			//利用終了日
			columnInfoList.setParkingEndDate(getRegistDateText(comDto.getSc006RiyouEndDayTwo()));
		}

		//更新SQLでは不要
		if (!updateMode){
			//登録日時
			columnInfoList.setInsertDate(skfBaseBusinessLogicUtils.getSystemDateTime());
			//登録者ID
			columnInfoList.setInsertUserId(LoginUserInfoUtils.getUserCd());
			//登録機能ID
			columnInfoList.setInsertProgramId(comDto.getPageId());
		}

		//更新日時
		columnInfoList.setUpdateDate(skfBaseBusinessLogicUtils.getSystemDateTime());
		//更新者ID
		columnInfoList.setUpdateUserId(LoginUserInfoUtils.getUserCd());
		//更新機能ID
		columnInfoList.setUpdateProgramId(comDto.getPageId());
		
		return columnInfoList;
	}
	
	/**
	 * 同一駐車場（貸与中）利用開始日（yyyyMMdd）取得メソッド
	 * @param parkingKanriNo 駐車場管理番号
	 * @param shatakuKanriNo 社宅管理番号
	 * @param shatakuKanriId 社宅管理台帳ID
	 * @return 同一駐車場（貸与中）利用開始日（yyyyMMdd）
	 */
	private String getParkingUpdateTaikyoCheck(String parkingKanriNo, String shatakuKanriNo, String shatakuKanriId){
		String parkingStartDate = CodeConstant.DOUBLE_QUOTATION;
		
		Skf3030Sc002GetParkingUpdateTaikyoCheckExpParameter param = new Skf3030Sc002GetParkingUpdateTaikyoCheckExpParameter();
		param.setYearMonth(skfBaseBusinessLogicUtils.getSystemProcessNenGetsu());
		param.setParkingKanriNo(Long.parseLong(parkingKanriNo));
		param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
		param.setShatakuKanriId(Long.parseLong(shatakuKanriId));
		//「同一駐車場（貸与中）利用開始日」を取得する
		List<Skf3030Sc002GetParkingUpdateTaikyoCheckExp> result = skf3030Sc002GetParkingUpdateTaikyoCheckExpRepository.getParkingUpdateTaikyoCheck(param);
		
		if(result != null){
			if( result.size() > 0){
				parkingStartDate = result.get(0).getParkingStartDate();
			}
		}
		
		return parkingStartDate;
	}
	
	/**
	 * 社宅駐車場区画マスタ情報の取得
	 * @param blockFlg
	 * @param changeFlg
	 * @param comDto
	 * @return
	 */
	public Skf3010MShatakuParkingBlock getTbtPublicShatakuParkingBlockDataColumnInfoList(int blockFlg, Boolean changeFlg, Skf3030Sc002CommonDto comDto){
		Skf3010MShatakuParkingBlock columnInfoList = new Skf3010MShatakuParkingBlock();
		String kukakuSyuryobi = CodeConstant.DOUBLE_QUOTATION;
		
		//社宅管理番号
		columnInfoList.setShatakuKanriNo(Long.parseLong(comDto.getHdnShatakuKanriNo()));

		//同じ駐車場の貸与日（現在表示している台帳以外）
		String taiyoDate = CodeConstant.DOUBLE_QUOTATION;
		if( blockFlg == 1 ){
			//changeFlg ⇒ Trueの場合は、切替前の駐車場管理Noに対して、更新(貸与状況：0(空き))をするため。
			if (changeFlg) {
				//区画１駐車場管理番号
				if(!SkfCheckUtils.isNullOrEmpty(comDto.getHdnChangeBeforeChushajoKanriNo1())){
					columnInfoList.setParkingKanriNo(Long.parseLong(comDto.getHdnChangeBeforeChushajoKanriNo1()));
				}
			}else{
				//区画１駐車場管理番号
				if(!SkfCheckUtils.isNullOrEmpty(comDto.getHdnChushajoKanriNo1())){
					columnInfoList.setParkingKanriNo(Long.parseLong(comDto.getHdnChushajoKanriNo1()));
				}
			}
			if(!SkfCheckUtils.isNullOrEmpty(comDto.getHdnChushajoKanriNo1()) && 
					!SkfCheckUtils.isNullOrEmpty(comDto.getHdnShatakuKanriNo()) &&
					!SkfCheckUtils.isNullOrEmpty(comDto.getHdnShatakuKanriId())){
				//区画１駐車場の貸与日（現在表示している台帳以外）
				taiyoDate = getParkingUpdateTaikyoCheck(comDto.getHdnChushajoKanriNo1(),
						comDto.getHdnShatakuKanriNo(), comDto.getHdnShatakuKanriId());
			}
			kukakuSyuryobi = getDateText(comDto.getSc006RiyouEndDayOne());
		}else if (blockFlg == 2 ){
			if (changeFlg) {
				//区画２駐車場管理番号
				if(!SkfCheckUtils.isNullOrEmpty(comDto.getHdnChangeBeforeChushajoKanriNo2())){
					columnInfoList.setParkingKanriNo(Long.parseLong(comDto.getHdnChangeBeforeChushajoKanriNo2()));
				}
			}else{
				//区画２駐車場管理番号
				if(!SkfCheckUtils.isNullOrEmpty(comDto.getHdnChushajoKanriNo2())){
					columnInfoList.setParkingKanriNo(Long.parseLong(comDto.getHdnChushajoKanriNo2()));
				}
			}
			if(!SkfCheckUtils.isNullOrEmpty(comDto.getHdnChushajoKanriNo2()) && 
					!SkfCheckUtils.isNullOrEmpty(comDto.getHdnShatakuKanriNo()) &&
					!SkfCheckUtils.isNullOrEmpty(comDto.getHdnShatakuKanriId())){
				//区画２駐車場の貸与日（現在表示している台帳以外）
				taiyoDate =  getParkingUpdateTaikyoCheck(comDto.getHdnChushajoKanriNo2(),
						comDto.getHdnShatakuKanriNo(), comDto.getHdnShatakuKanriId());
			}
			kukakuSyuryobi = getDateText(comDto.getSc006RiyouEndDayTwo());
		}
		
		if( changeFlg ){
			//貸与状況(空き)
			columnInfoList.setParkingLendJokyo("0");
		}else{
			//終了日が入力された場合、返却予定に設定
			if(!SkfCheckUtils.isNullOrEmpty(kukakuSyuryobi)){
				//返却日が前月の場合は、即空きに設定
				if(skfPageBusinessLogicUtils.checkPassedDay(kukakuSyuryobi)){
					//貸与状況(空き)
					columnInfoList.setParkingLendJokyo("0");
				}else{
					//別の台帳にて対象駐車場が“貸与中”の場合は“貸与中”として更新する
					if (SkfCheckUtils.isNullOrEmpty(taiyoDate)) {
						//貸与状況(返却予定)
						columnInfoList.setParkingLendJokyo("3");
					}else{
						//貸与状況(貸与中)
						columnInfoList.setParkingLendJokyo("2");
					}

				}
			}else{	
				//貸与状況(貸与中)
				columnInfoList.setParkingLendJokyo("2");
			}
		}

		//更新日時
		columnInfoList.setUpdateDate(skfBaseBusinessLogicUtils.getSystemDateTime());
		//更新者ID
		columnInfoList.setUpdateUserId(LoginUserInfoUtils.getUserCd());
		//更新機能ID
		columnInfoList.setUpdateProgramId(comDto.getPageId());
		
		
		return columnInfoList;
	}
	
	/**
	 * 月別相互利用履歴テーブルの取得
	 * @param comDto
	 * @return
	 */
	public Skf3030TShatakuMutualRireki getTbtPublicShatakuMutualRirekiDataColumnInfoList(Skf3030Sc002CommonDto comDto){
		Skf3030TShatakuMutualRireki columnInfoList = new Skf3030TShatakuMutualRireki();
		//社宅管理台帳ID
		columnInfoList.setShatakuKanriId(Long.parseLong(comDto.getHdnShatakuKanriId()));
		//対象年月
		columnInfoList.setYearMonth(comDto.getHdnNengetsu());

		//配属会社コード
		columnInfoList.setAssignCompanyCd(getToRegistString(comDto.getSc006HaizokuKaisyaSelect()));
		//所属機関
		columnInfoList.setAssignAgencyName(getToRegistString(comDto.getSc006SyozokuKikan()));
		//室・部名
		columnInfoList.setAssignAffiliation1(getToRegistString(comDto.getSc006SituBuName()));
		//課等名
		columnInfoList.setAssignAffiliation2(getToRegistString(comDto.getSc006KanadoMei()));
		//配属データコード番号
		columnInfoList.setAssignCd(getToRegistString(comDto.getSc006HaizokuNo()));
		
		//更新日時
		columnInfoList.setUpdateDate(skfBaseBusinessLogicUtils.getSystemDateTime());
		//更新者ID
		columnInfoList.setUpdateUserId(LoginUserInfoUtils.getUserCd());
		//更新機能ID
		columnInfoList.setUpdateProgramId(comDto.getPageId());
		
		return columnInfoList;
	}
	
	/**
	 * 社宅部屋情報マスタ情報の取得
	 * @param flg
	 * @param comDto
	 * @return
	 */
	public Skf3010MShatakuRoom getTbtPublicShatakuRoomDataColumnInfoList(String flg,Skf3030Sc002CommonDto comDto){
		Skf3010MShatakuRoom columnInfoList = new Skf3010MShatakuRoom();
		//社宅管理番号
		columnInfoList.setShatakuKanriNo(Long.parseLong(comDto.getHdnShatakuKanriNo()));
		//部屋管理番号
		columnInfoList.setShatakuRoomKanriNo(Long.parseLong(comDto.getHdnShatakuRoomKanriNo()));

		if (Skf3030Sc002CommonDto.STR_UPDATE.equals(flg)) {
			//貸与状況(入居)
			columnInfoList.setLendJokyo(CodeConstant.NYUKYO_TYU);//"2"
		}else if (Skf3030Sc002CommonDto.STR_UPDATE_TAIKYO.equals(flg)) {
			//貸与状況(退居予定)
			columnInfoList.setLendJokyo(CodeConstant.TAIKYO_YOTEI);//"3"
		}else if (Skf3030Sc002CommonDto.STR_DELETE.equals(flg)) {
			//貸与状況(空き)
			columnInfoList.setLendJokyo(CodeConstant.KUSHITSU);//"0"
		}
		
		//更新日時
		columnInfoList.setUpdateDate(skfBaseBusinessLogicUtils.getSystemDateTime());
		//更新者ID
		columnInfoList.setUpdateUserId(LoginUserInfoUtils.getUserCd());
		//更新機能ID
		columnInfoList.setUpdateProgramId(comDto.getPageId());
		
		return columnInfoList;
	}
	
	/**
	 * 社宅部屋備品情報マスタ情報の取得
	 * @param comDto
	 * @return
	 */
	public Skf3010MShatakuRoomBihin getTbtPublicShatakuRoomBihinDataColumnInfoList(Skf3030Sc002CommonDto comDto){
		Skf3010MShatakuRoomBihin columnInfoList = new Skf3010MShatakuRoomBihin();
		
		//社宅管理番号
		columnInfoList.setShatakuKanriNo(Long.parseLong(comDto.getHdnShatakuKanriNo()));
		//部屋管理番号
		columnInfoList.setShatakuRoomKanriNo(Long.parseLong(comDto.getHdnShatakuRoomKanriNo()));
		
		//更新日時
		columnInfoList.setUpdateDate(skfBaseBusinessLogicUtils.getSystemDateTime());
		//更新者ID
		columnInfoList.setUpdateUserId(LoginUserInfoUtils.getUserCd());
		//更新機能ID
		columnInfoList.setUpdateProgramId(comDto.getPageId());
		
		return columnInfoList;
	}
	
	/**
	 * 備品データ情報の取得
	 * @param teijiNo
	 * @param comDto
	 * @return
	 */
	public List<Map<String,Object>> setBihinData(Skf3030Sc002CommonDto comDto,Long teijiNo){
		
		List<Map<String,Object>> teijiBihinDt = new ArrayList<Map<String, Object>>();
		
		// 備品情報リスト
		List<Map<String, Object>> bihinList = new ArrayList<Map<String, Object>>();
		bihinList.addAll(jsonArrayToArrayList(comDto.getJsonBihin()));
		
		for(Map<String,Object> bihinRow : bihinList){
			Map<String,Object> tmpMap = new HashMap<String,Object>();
		
			tmpMap.put("teiji_no", teijiNo.toString());
			tmpMap.put("bihin_cd", bihinRow.get("bihinCd").toString());
			tmpMap.put("bihin_lent_status_kbn", bihinRow.get("bihinTaiyoStts").toString());
			tmpMap.put("bihin_sataus_old", bihinRow.get("bihinStatusOld").toString());
			
			teijiBihinDt.add(tmpMap);
		}
		
		return teijiBihinDt;
	}
	
	/**
	 * 備品データ情報の取得
	 * @param teijiNo
	 * @param comDto
	 * @return
	 */
	public List<Map<String,Object>> setBihinData(Skf3030Sc002CommonDto comDto){
		
		List<Map<String,Object>> shatakuRoomBihinDt = new ArrayList<Map<String, Object>>();
		
		// 備品情報リスト
		List<Map<String, Object>> bihinList = new ArrayList<Map<String, Object>>();
		bihinList.addAll(jsonArrayToArrayList(comDto.getJsonBihin()));
		
		for(Map<String,Object> bihinRow : bihinList){
			Map<String,Object> tmpMap = new HashMap<String,Object>();
		
			tmpMap.put("teiji_no", "1");
			tmpMap.put("bihin_cd", bihinRow.get("bihinCd").toString());
			tmpMap.put("bihin_lent_status_kbn", bihinRow.get("bihinTaiyoStts").toString());
			shatakuRoomBihinDt.add(tmpMap);
		}
		
		return shatakuRoomBihinDt;
	}
	
	/**
	 * 部屋情報マスタの貸与状況取得メソッド
	 * @param shatakuKanriNo 社宅管理番号
	 * @param shatakuRoomNo 社宅部屋管理番号
	 * @return 部屋情報マスタの貸与状況
	 */
	public String getRoomLendJokyo(Long shatakuKanriNo, Long shatakuRoomNo){
		
		String roomLendJokyo = CodeConstant.DOUBLE_QUOTATION;
		
		Skf3010MShatakuRoomKey key = new Skf3010MShatakuRoomKey();
		key.setShatakuKanriNo(shatakuKanriNo);
		key.setShatakuRoomKanriNo(shatakuRoomNo);
		//部屋情報マスタの貸与状況を取得する
		Skf3010MShatakuRoom da = skf3010MShatakuRoomRepository.selectByPrimaryKey(key);
		
		if(da != null){
			roomLendJokyo = da.getLendJokyo();
		}
		da = null;
		return roomLendJokyo;
	}
	
	/**
	 * 社宅管理ID取得メソッド
	 * @param teijiNo 提示番号
	 * @return 社宅管理台帳ID
	 */
	public Long getShatakuKannriIdFromTeijiData(Long teijiNo){
		Long shatakuKanriId = null;
		Skf3022TTeijiData result = skf3022TTeijiDataRepository.selectByPrimaryKey(teijiNo);
		
		if(result != null){
			if(result.getShatakuKanriId() != null){
				shatakuKanriId = result.getShatakuKanriId();
			}
		}
		
		return shatakuKanriId;
	}
	
	/**
	 * 月別駐車場履歴テーブル削除処理メソッド（貸与番号条件あり）
	 * @param shatakuKanriId 社宅管理台帳ID
	 * @param lendNo 貸与番号
	 * @param yearMonth 処理年月
	 * @return 削除件数
	 */
	public int deleteParkingRirekiByLendNo(String shatakuKanriId,String lendNo,String yearMonth){
		
		int result = -1;
		Skf3030TParkingRirekiKey delKey = new Skf3030TParkingRirekiKey();
		delKey.setShatakuKanriId(Long.parseLong(shatakuKanriId));
		delKey.setParkingLendNo(Long.parseLong(lendNo));
		delKey.setYearMonth(yearMonth);
		result = skf3030TParkingRirekiRepository.deleteByPrimaryKey(delKey);
		
		return result;
	}
	
	/**
	 *  社員番号変更フラグ取得メソッド
	 * @param shainNo 社員番号
	 * @return
	 */
	public String getShainNoChangeFlag(String shainNo){
		
		String shainNoChangeFlg = CodeConstant.DOUBLE_QUOTATION;
		
		Skf1010MShainKey key = new Skf1010MShainKey();
		key.setCompanyCd(CodeConstant.C001);
		key.setShainNo(shainNo);
		Skf1010MShain selectShain = skf1010MShainRepository.selectByPrimaryKey(key);
		if(selectShain != null){
			shainNoChangeFlg = selectShain.getShainNoChangeFlg();
		}
		return shainNoChangeFlg;
	}
	
	/**
	 * 生年月日取得メソッド
	 * @param shainNo 社員番号
	 * @return
	 */
	public String getBirthDay(String shainNo){
		
		String birthDay = CodeConstant.DOUBLE_QUOTATION;
		
		Skf1010MShainKey key = new Skf1010MShainKey();
		key.setCompanyCd(CodeConstant.C001);
		key.setShainNo(shainNo);
		Skf1010MShain selectShain = skf1010MShainRepository.selectByPrimaryKey(key);
		if(selectShain != null){
			Short year = selectShain.getBirthdayYear();
			Short month = selectShain.getBirthdayMonth();
			Short day = selectShain.getBirthdayDay();
			
			if(year != null && month != null && day != null){
				birthDay = Short.toString(year) + String.format("%02d", month) + String.format("%02d", day);
			}
			
		}
		return birthDay;
	}
	
	/**
	 * 可変ラベル値と協議中フラグを復元
	 * エラー時の可変ラベルの値を設定する。
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param labelList		可変ラベルリスト
	 * @param comDto		*DTO
	 */
	public void setVariableLabel(List<Map<String, Object>> labelList, Skf3030Sc002CommonDto comDto) {
		// 可変ラベル値
		Map <String, Object> labelMap = labelList.get(0);
		// 社員名
		String sc006ShainName = (labelMap.get("sc006ShainName") != null) ? labelMap.get("sc006ShainName").toString() : "";
		// 社員番号
		String sc006ShainNo = (labelMap.get("sc006ShainNo") != null) ? labelMap.get("sc006ShainNo").toString() : "";
		// 社宅名
		String sc006ShatakuName = (labelMap.get("sc006ShatakuName") != null) ? labelMap.get("sc006ShatakuName").toString() : "";
		// 部屋番号
		String sc006HeyaNo = (labelMap.get("sc006HeyaNo") != null) ? labelMap.get("sc006HeyaNo").toString() : "";
		// 駐車場使用料月額(調整後)
		String sc006TyusyaMonthPayAfter = (labelMap.get("sc006TyusyaMonthPayAfter") != null) ? labelMap.get("sc006TyusyaMonthPayAfter").toString() : "0";
		// 社宅使用料日割金額
		String sc006SiyoryoHiwariPay = (labelMap.get("sc006SiyoryoHiwariPay") != null) ? labelMap.get("sc006SiyoryoHiwariPay").toString() : "0";
		// 社宅使用料月額(調整後)
		String sc006SyatauMonthPayAfter = (labelMap.get("sc006SyatauMonthPayAfter") != null) ? labelMap.get("sc006SyatauMonthPayAfter").toString() : "0";
		// 社宅使用料月額
		String sc006ShiyoryoTsukigaku = (labelMap.get("sc006ShiyoryoTsukigaku") != null) ? labelMap.get("sc006ShiyoryoTsukigaku").toString() : "0";
		// 駐車場使用料日割金額1
		String sc006TyusyaDayPayOne = (labelMap.get("sc006TyusyaDayPayOne") != null) ? labelMap.get("sc006TyusyaDayPayOne").toString() : "0";
		// 駐車場使用料月額1
		String sc006TyusyaMonthPayOne = (labelMap.get("sc006TyusyaMonthPayOne") != null) ? labelMap.get("sc006TyusyaMonthPayOne").toString() : "0";
		// 駐車場使用料日割金額 2
		String sc006TyusyaDayPayTwo = (labelMap.get("sc006TyusyaDayPayTwo") != null) ? labelMap.get("sc006TyusyaDayPayTwo").toString() : "0";
		// 駐車場使用料月額2
		String sc006TyusyaMonthPayTwo = (labelMap.get("sc006TyusyaMonthPayTwo") != null) ? labelMap.get("sc006TyusyaMonthPayTwo").toString() : "0";
		// ヘッダ項目貸与用途(使用料計算パターン名)
		String sc006SiyoryoPatName = (labelMap.get("sc006SiyoryoPatName") != null) ? labelMap.get("sc006SiyoryoPatName").toString() : "";
		// 社宅使用料月額
		String sc006SiyoryoMonthPay = (labelMap.get("sc006SiyoryoMonthPay") != null) ? labelMap.get("sc006SiyoryoMonthPay").toString() : "0";
		// 貸与用途
		String sc006TaiyoYouto = (labelMap.get("sc006TaiyoYouto") != null) ? labelMap.get("sc006TaiyoYouto").toString() : "";
		// 貸与規格
		String sc006TaiyoKikaku = (labelMap.get("sc006TaiyoKikaku") != null) ? labelMap.get("sc006TaiyoKikaku").toString() : "";
		// 区画１ 区画番号
		String sc006KukakuNoOne = (labelMap.get("sc006KukakuNoOne") != null) ? labelMap.get("sc006KukakuNoOne").toString() : "";
		// 区画2 区画番号
		String sc006KukakuNoTwo = (labelMap.get("sc006KukakuNoTwo") != null) ? labelMap.get("sc006KukakuNoTwo").toString() : "";
		// 個人負担共益費月額(調整後)
		String sc006KyoekihiPayAfter = (labelMap.get("sc006KyoekihiPayAfter") != null) ? labelMap.get("sc006KyoekihiPayAfter").toString() : "0";
		// 開始日
		String sc006StartDay =  (labelMap.get("sc006StartDay") != null) ? labelMap.get("sc006StartDay").toString() : "";
		// 終了日
		String sc006EndDay =  (labelMap.get("sc006EndDay") != null) ? labelMap.get("sc006EndDay").toString() : "";

		/** 戻り値設定 */
		comDto.setSc006ShainName(sc006ShainName);
		comDto.setSc006ShainNo(sc006ShainNo);
		comDto.setSc006ShatakuName(sc006ShatakuName);
		comDto.setSc006HeyaNo(sc006HeyaNo);
		comDto.setSc006TyusyaMonthPayAfter(sc006TyusyaMonthPayAfter);
		comDto.setSc006SiyoryoHiwariPay(sc006SiyoryoHiwariPay);
		comDto.setSc006SyatauMonthPayAfter(sc006SyatauMonthPayAfter);
		comDto.setSc006ShiyoryoTsukigaku(sc006ShiyoryoTsukigaku);
		comDto.setSc006TyusyaDayPayOne(sc006TyusyaDayPayOne);
		comDto.setSc006TyusyaMonthPayOne(sc006TyusyaMonthPayOne);
		comDto.setSc006TyusyaDayPayTwo(sc006TyusyaDayPayTwo);
		comDto.setSc006TyusyaMonthPayTwo(sc006TyusyaMonthPayTwo);
		comDto.setSc006SiyoryoPatName(sc006SiyoryoPatName);
		comDto.setSc006SiyoryoMonthPay(sc006SiyoryoMonthPay);
		comDto.setSc006TaiyoYouto(sc006TaiyoYouto);
		comDto.setSc006TaiyoKikaku(sc006TaiyoKikaku);
		comDto.setSc006KukakuNoOne(sc006KukakuNoOne);
		comDto.setSc006KukakuNoTwo(sc006KukakuNoTwo);
		comDto.setSc006TyusyaMonthPayOne(sc006TyusyaMonthPayOne);
		comDto.setSc006KyoekihiPayAfter(sc006KyoekihiPayAfter);
		comDto.setSc006StartDay(getDateText(sc006StartDay));
		comDto.setSc006EndDay(getDateText(sc006EndDay));
		
		List<Map<String,Object>> BihinDt = new ArrayList<Map<String, Object>>();
		
		// 備品情報リスト
		List<Map<String, Object>> bihinList = new ArrayList<Map<String, Object>>();
		bihinList.addAll(jsonArrayToArrayList(comDto.getJsonBihin()));
		
		for(Map<String,Object> bihinRow : bihinList){
			Map<String,Object> tmpMap = new HashMap<String,Object>();
		
			tmpMap.put("bihinCd", bihinRow.get("bihinCd").toString());
			tmpMap.put("bihinName", bihinRow.get("bihinName").toString());
			tmpMap.put("heyaSonaetukeSttsStr", bihinRow.get("heyaSonaetukeSttsStr").toString());
			tmpMap.put("bihinTaiyoStts", bihinRow.get("bihinTaiyoStts").toString());
			tmpMap.put("bihinTaiyoSttsList", bihinRow.get("bihinTaiyoSttsList").toString());
			tmpMap.put("bihinStatusOld", bihinRow.get("bihinStatusOld").toString());
			tmpMap.put("updateFlg", bihinRow.get("updateFlg").toString());
			tmpMap.put("updateDate", bihinRow.get("updateDate").toString());
			tmpMap.put("heyaSonaetukeStts", bihinRow.get("heyaSonaetukeStts").toString());
			tmpMap.put("bihinTaiyoSttsKbn", bihinRow.get("bihinTaiyoSttsKbn").toString());
			
			BihinDt.add(tmpMap);
		}
		comDto.setBihinInfoListTableData(getBihinListTableDataViewColumnFromView(BihinDt,comDto.getGBihinInfoControlStatusFlg()));
	}
	
	/**
	 * 次回再表示するタブ位置を設定する。
	 * @param tabIndex 設定タブインデックス
	 * @return 次回表示タブインデックス
	 */
	private String setDisplayTabIndex(String tabIndex , Skf3030Sc002CommonDto comDto){
		
		int nextTab = -1;
		int tab = 0;
		
		if(!SkfCheckUtils.isNullOrEmpty(comDto.getNextTabIndex())){
			nextTab = Integer.parseInt(comDto.getNextTabIndex());
		}
		
		if(!SkfCheckUtils.isNullOrEmpty(tabIndex)){
			tab = Integer.parseInt(tabIndex);
		}
		if(nextTab == -1 || nextTab > tab){
			nextTab = tab;
		}
		return String.format("%d", nextTab);
		
	}
	
	/**
	 * StringをLongに変換
	 * @param str
	 * @return 変換値（空文字またはnullの場合はnull返却)
	 */
	private Long stringParseToLong(String str){
		Long resultLong = null;
		
		if(!SkfCheckUtils.isNullOrEmpty(str)){
			resultLong = Long.parseLong(str);
		}
		
		return resultLong;
	}

	/**
	 * DTO変数初期化
	 * 「※」項目はアドレスとして戻り値になる。
	 * @param comDto	*DTO
	 */
	public void initialize(Skf3030Sc002CommonDto comDto) {

		/** 提示データ一覧検索条件退避 */
		// 社員番号
		comDto.setSearchInfoShainNo(null);
		// 社員氏名
		comDto.setSearchInfoShainName(null);
		// 社宅名
		comDto.setSearchInfoShatakuName(null);
		// 入退居区分
		comDto.setSearchInfoNyutaikyoKbn(null);
		// 社宅提示状況
		comDto.setSearchInfoStJyokyo(null);
		// 社宅提示確認督促
		comDto.setSearchInfoStKakunin(null);
		// 備品提示状況
		comDto.setSearchInfoBhJyokyo(null);
		// 備品提示確認督促
		comDto.setSearchInfoBhKakunin(null);
		// 備品搬入搬出督促
		comDto.setSearchInfoMoveInout(null);

		/** 次月予約登録パラメータ */
		// 提示番号
		comDto.setHdnJigetuYoyakuTeijiNo(null);
		// 基準年月
		comDto.setHdnJigetuYoyakuYearMonth(null);
		// 社宅管理台帳ID
		comDto.setHdnJigetuYoyakuShatakuKanriId(null);
		// 社宅使用料月額
		comDto.setHdnJigetuYoyakuRental(null);
		// 個人負担共益費月額
		comDto.setHdnJigetuYoyakuKyoekihiPerson(null);
		// 区画1_駐車場使用料月額
		comDto.setHdnJigetuYoyakuParkingRentalOne(null);
		// 区画2_駐車場使用料月額
		comDto.setHdnJigetuYoyakuParkingRentalTwo(null);

		/** 使用料計算入力支援戻り値 */
		// 規格
		comDto.setHdnRateShienKikaku(null);
		// 用途
		comDto.setHdnRateShienYoto(null);
		// 延べ面積
		comDto.setHdnRateShienNobeMenseki(null);
		// 基準面積(基準使用料算定上延べ面積)
		comDto.setHdnRateShienKijunMenseki(null);
		// 社宅面積(社宅使用料算定上延べ面積)
		comDto.setHdnRateShienShatakuMenseki(null);
		// 規格名
		comDto.setHdnRateShienKikakuName(null);
		// 用途名
		comDto.setHdnRateShienYotoName(null);
		// サンルーム面積
		comDto.setHdnRateShienSunroomMenseki(null);
		// 階段面積
		comDto.setHdnRateShienKaidanMenseki(null);
		// 物置面積
		comDto.setHdnRateShienMonookiMenseki(null);
		// 単価
		comDto.setHdnRateShienTanka(null);
		// 経年
		comDto.setHdnRateShienKeinen(null);
		// 経年残価率
		comDto.setHdnRateShienKeinenZankaRitsu(null);
		// 使用料パターン名
		comDto.setHdnRateShienPatternName(null);
		// 使用料パターン月額
		comDto.setHdnRateShienPatternGetsugaku(null);
		// 社宅使用料月額
		comDto.setHdnRateShienShatakuGetsugaku(null);
		// 社宅基本使用料
		comDto.setHdnRateShienKihonShiyoryo(null);

		/** 非表示項目 */
		// 社宅管理台帳ID
		comDto.setHdnShatakuKanriId(null);
		// 社宅管理番号
		comDto.setHdnShatakuKanriNo(null);
		// 社宅管理番号元?
		comDto.setHdnShatakuKanriNoOld(null);
		// 部屋管理番号
		comDto.setHdnRoomKanriNo(null);
		// 部屋管理番号元?
		comDto.setHdnRoomKanriNoOld(null);
		// 管理会社コード
		comDto.setHdnCompanyCode(null);
		// 締め処理実行区分
		comDto.setHdnBillingActKbn(null);
		// 旧提示番号
		comDto.setHdnTeijiNoOld(null);
		// 入退居区分元?
		comDto.setHdnNyutaikyoKbnOld(null);
		// 同一書類管理番号存在フラグ
		comDto.setHdnSameFlg(false);
		// 役員算定区分
		comDto.setHdnYakuin(null);
		// 個人負担共益費月額
		comDto.setHdnKojinFutan(null);
		// 区画番号1
		comDto.setHdnKukakuNoOne(null);
		// 利用開始日1
		comDto.setHdnRiyouStartDayOne(null);
		// 利用終了日1
		comDto.setHdnRiyouEndDayOne(null);
		// 区画番号2
		comDto.setHdnKukakuNoTwo(null);
		// 利用開始日2
		comDto.setHdnRiyouStartDayTwo(null);
		// 利用終了日2
		comDto.setHdnRiyouEndDayTwo(null);
		// 使用料計算パターンID元
		comDto.setHdnSiyouryoIdOld(null);
		// 使用料パターンID
		comDto.setHdnSiyouryoId(null);
		// 使用料計算パターンID
		comDto.setHdnShiyoryoKeisanPatternId(null);
		// 駐車場区画番号元（区画１）
		comDto.setHdnChushajoNoOneOld(null);
		//comDto.setHdnChushajoNoOne(null);
		// 駐車場区画番号元（区画2）
		comDto.setHdnChushajoNoTwoOld(null);
		//comDto.setHdnChushajoNoTwo(null);
		// 継続登録フラグ
		comDto.setHdnKeizokuBtnFlg(false);
		// 原籍会社コード
		comDto.setHdnGensekiKaishaCd(null);
		// 給与支給会社コード
		comDto.setHdnKyuyoKaishaCd(null);
		// 貸付会社コード
		comDto.setHdnKashitsukeKaishaCd(null);
		// 借受会社コード
		comDto.setHdnKariukeKaishaCd(null);
		// 配属会社コード
		comDto.setHdnHaizokuKaishaCd(null);
		// 社宅提示ステータス
		comDto.setHdnShatakuTeijiStatus(null);
		// 備品提示ステータス
		comDto.setHdnBihinTeijiStatus(null);
		// 備品貸与区分
		comDto.setHdnBihinTaiyoKbn(null);
		// 提示データ更新日
		comDto.setHdnTeijiDataUpdateDate(null);
		// 社宅部屋情報マスタ元更新日
		comDto.setHdnShatakuRoomUpdateDate(null);
		comDto.setHdnShatakuRoomOldUpdateDate(null);
		// 社宅駐車場区画情報マスタ元（区画１）更新日
		comDto.setHdnShatakuParkingBlock1UpdateDate(null);
		comDto.setHdnShatakuParkingBlockOld1UpdateDate(null);
		comDto.setHdnShatakuParkingBlockOld11UpdateDate(null);
		// 社宅駐車場区画情報マスタ元（区画２）更新日
		comDto.setHdnShatakuParkingBlock2UpdateDate(null);
		comDto.setHdnShatakuParkingBlockOld2UpdateDate(null);
		comDto.setHdnShatakuParkingBlockOld21UpdateDate(null);
		// 生年月日
		comDto.setHdnBirthday(null);
		// 社宅部屋変更フラグ
		comDto.setHdnShatakuHeyaFlg(null);
		// 使用料パターン排他処理用更新日付
		comDto.setHdnRentalPatternUpdateDate(null);
		// 入退居予定データ更新日
		comDto.setHdnNyutaikyoYoteiUpdateDate(null);
		// 備品搬出待ちフラグ
		comDto.setHdnBihinMoveOutFlg(false);
		// 使用料変更フラグ
		comDto.setHdnSiyouryoFlg(null);
		// ?
		comDto.setHdnFieldMessage(null);
		// 区画１ 終了日
		comDto.setHdnEndDayOne(null);
		// 区画2 終了日
		comDto.setHdnEndDayTwo(null);
		// ?
		comDto.setHdnIsInfo(null);
		comDto.setBihinItiranReloadFlg(false);
		//次回TabIndex
		comDto.setNextTabIndex(null);

		comDto.setHdnShatakuRoomKanriNo(null);
		comDto.setHdnNengetsu(null);
		comDto.setHdnShainNo(null);
		comDto.setHdnShainName(null);
		//社員番号変更フラグ
		comDto.setHdnShainNoChangeFlg(null);
		comDto.setHdnRentalPatternId(null);
		comDto.setHdnChushajoKanriNo1(null);
		comDto.setHdnChushajoKanriNo2(null);
		comDto.setHdnBihinCd(null);
		comDto.setHdnShatakuShiyoryoGetsugaku(null);
		comDto.setHdnChangeBeforeRentalPatternId(null);
		comDto.setHdnChangeBeforeNyukyoYoteibi(null);
		comDto.setHdnChangeBeforeTaikyoYoteibi(null);
		comDto.setHdnChangeBeforeYakuinSantei(null);
		comDto.setHdnChangeBeforeShatakuShiyoryoChoseiKingaku(null);
		comDto.setHdnChangeBeforeKojinFutanKyoekihiGetsugaku(null);
		comDto.setHdnChangeBeforeKojinFutanKyoekihiChoseiKingaku(null);
		comDto.setHdnChangeBeforeChushajoKanriNo1(null);
		comDto.setHdnChangeBeforeKukaku1RiyoKaishibi(null);
		comDto.setHdnChangeBeforeKukaku1RiyoShuryobi(null);
		comDto.setHdnChangeBeforeChushajoKanriNo2(null);
		comDto.setHdnChangeBeforeKukaku2RiyoKaishibi(null);
		comDto.setHdnChangeBeforeKukaku2RiyoShuryobi(null);
		comDto.setHdnChangeBeforeKaishibi(null);
		comDto.setHdnChangeBeforeShuryobi(null);
		comDto.setHdnChangeBeforeTaiyobi(null);
		comDto.setHdnChangeBeforeHenkyakubi(null);
		comDto.setHdnNYUTAIKYO_KBN(null);
		comDto.setHdnRentalFlg(null);
		comDto.setHdnShatakuKanriFlg1(null);
		comDto.setHdnShatakuKanriFlg2(null);
		comDto.setHdnKaiSanAfterShatakuShiyoryoHiwariKingaku(null);
		comDto.setHdnKaiSanAfterShatakuShiyoryoGetsugakuChoseigo(null);
		comDto.setHdnKaiSanAfterKukaku1ChushajoShiyoroHiwariKingaku(null);
		comDto.setHdnKaiSanAfterKukaku2ChushajoShiyoroHiwariKingaku(null);
		comDto.setHdnKaiSanAfterChushajoShiyoryoGetsugakuChoseigo(null);
		comDto.setHdnKaiSanAfterShatakuShiyoryoGetsugaku(null);
		comDto.setHdnKaiSanAfterKukaku1ChushajoShiyoroGetsugaku(null);
		comDto.setHdnKaiSanAfterKukaku2ChushajoShiyoroGetsugaku(null);
		comDto.setHdnKaiSanAfterKojinFutanKyoekihiGetsugakuChoseigo(null);
		//comDto.setHdnEndDay1(null);
		//comDto.setHdnEndDay2(null);
		//使用料パターン名
		comDto.setLitShiyoryoKeisanPattern(null);
		//寒冷地減免
		comDto.setLitKanreichiGenmen(null);
		//狭小減免
		comDto.setLitKyoshoGenmen(null);

		/** メッセージボックス */
		// スタイル
		comDto.setSc006MsgBoxStyle(null);
		// メッセージ
		comDto.setSc006Msg(null);

		/** 運用ガイド */
		comDto.setOperationGuidePath(null);

		/** タブステータス */
		// 社宅タブ
		comDto.setShatakuTabStatus(false);
		// 備品タブ
		comDto.setBihinTabStatus(false);
		// 役員情報/相互利用タブ
		comDto.setSogoTabStatu(false);

		/** サーバー処理連携用 */
		// 表示タブインデックス
		comDto.setHdnTabIndex(null);
		// 作成完了ボタン押下時メッセージ
		comDto.setLitMessageCreate(null);
		// 一時保存ボタン押下時メッセージ
		comDto.setLitMessageTmpSave(null);
		// 社宅管理台帳登録
		comDto.setLitMessageShatakuLogin(null);
		// 継続登録ボタン押下時メッセージ
		comDto.setLitMessageKeizokuLogin(null);
		// 前に戻るボタン押下時メッセージ
		comDto.setLitMessageBack(null);
		// 処理状態
		comDto.setSc006Status(null);
		// JSONラベルリスト
		comDto.setJsonLabelList(null);
		// JSON備品情報 リスト
		comDto.setJsonBihin(null);
		// 協議中フラグ状態
		comDto.setSc006KyoekihiKyogichuCheckState(null);

		/** ラベル値 */
		// 社員番号
		comDto.setSc006ShainNo(null);
		// 社宅名
		comDto.setSc006ShatakuName(null);
		// 貸与規格(使用料計算パターン名)
		comDto.setSc006SiyoryoPatName(null);
		// 社員氏名
		comDto.setSc006ShainName(null);
		// 部屋番号
		comDto.setSc006HeyaNo(null);
		// 社宅使用料月額（ヘッダ項目）
		comDto.setSc006SiyoryoMonthPay(null);
		// 対象年月
		comDto.setSc006TaishoNengetsu(null);
		// 入退居（ヘッダ項目）
		comDto.setSc006NyutaikyoKbn(null);
		// 入退居(フォント色)
		comDto.setSc006NyutaikyoKbnColor(null);
		// 社宅提示(ヘッダ項目)
		comDto.setSc006ShatakuStts(null);
		// 社宅提示(フォント色)
		comDto.setSc006ShatakuSttsColor(null);
		// 社宅提示(ヘッダ項目)
		comDto.setSc006ShatakuSttsCd(null);
		// 備品提示(ヘッダ項目)
		comDto.setSc006BihinStts(null);
		// 備品提示(フォント色)
		comDto.setSc006BihinSttsColor(null);
		// 備品提示(ヘッダ項目)
		comDto.setSc006BihinSttsCd(null);
		// 区画番号1
		comDto.setSc006KukakuNoOne(null);
		// 駐車場使用料月額1
		comDto.setSc006TyusyaMonthPayOne(null);
		// 駐車場使用料日割金額1
		comDto.setSc006TyusyaDayPayOne(null);
		// 貸与用途
		comDto.setSc006TaiyoYouto(null);
		// 区画番号2
		comDto.setSc006KukakuNoTwo(null);
		// 貸与規格
		comDto.setSc006TaiyoKikaku(null);
		// 社宅使用料月額
		comDto.setSc006ShiyoryoTsukigaku(null);
		// 駐車場使用料月額2
		comDto.setSc006TyusyaMonthPayTwo(null);
		// 社宅使用料日割金額
		comDto.setSc006SiyoryoHiwariPay(null);
		// 駐車場使用料日割金額 2
		comDto.setSc006TyusyaDayPayTwo(null);
		// 社宅使用料月額(調整後)
		comDto.setSc006SyatauMonthPayAfter(null);
		// 駐車場使用料月額(調整後)
		comDto.setSc006TyusyaMonthPayAfter(null);
		// 個人負担共益費月額(調整後)
		comDto.setSc006KyoekihiPayAfter(null);
		// 管理会社
		comDto.setSc006KanriKaisya(null);

		/** 入力エリア */
		// 入居予定日
		comDto.setSc006NyukyoYoteiDay(null);
		// 退居予定日
		comDto.setSc006TaikyoYoteiDay(null);
		// 利用開始日1
		comDto.setSc006RiyouStartDayOne(null);
		// 利用終了日1
		comDto.setSc006RiyouEndDayOne(null);
		// 利用開始日2
		comDto.setSc006RiyouStartDayTwo(null);
		// 利用終了日2
		comDto.setSc006RiyouEndDayTwo(null);
		// 社宅使用料調整金額
		comDto.setSc006SiyoroTyoseiPay(null);
		// 駐車場使用料調整金額
		comDto.setSc006TyusyaTyoseiPay(null);
		// 個人負担共益費 協議中
		comDto.setSc006KyoekihiKyogichuCheck(false);
		// 備考
		comDto.setSc006Bicou(null);
		// 個人負担共益費月額
		comDto.setSc006KyoekihiMonthPay(null);
		// 個人負担共益費調整金額
		comDto.setSc006KyoekihiTyoseiPay(null);
		// 貸与日
		comDto.setSc006TaiyoDay(null);
		// 返却日
		comDto.setSc006HenkyakuDay(null);
		// 搬入希望日
		comDto.setSc006KibouDayIn(null);
		// 搬入本人連絡先
		comDto.setSc006HonninAddrIn(null);
		// 搬入受取代理人名
		comDto.setSc006UketoriDairiInName(null);
		// 搬入受取代理人連絡先
		comDto.setSc006UketoriDairiAddr(null);
		// 搬入レンタル指示書発行日
		comDto.setSc006HannyuShijisyoHakkoubi(null);
		// 搬出希望日
		comDto.setSc006KibouDayOut(null);
		// 搬出本人連絡先
		comDto.setSc006HonninAddrOut(null);
		// 搬出立会代理人名
		comDto.setSc006TachiaiDairi(null);
		// 搬出立会代理人連絡先
		comDto.setSc006TachiaiDairiAddr(null);
		// 搬出レンタル指示書発行日
		comDto.setSc006HanshutuShijisyoHakkoubi(null);
		// 代理人備考
		comDto.setSc006DairiBiko(null);
		// 備品備考
		comDto.setSc006BihinBiko(null);
		// 所属機関
		comDto.setSc006SyozokuKikan(null);
		// 室・部名
		comDto.setSc006SituBuName(null);
		// 課等名
		comDto.setSc006KanadoMei(null);
		// 配属データコード番号
		comDto.setSc006HaizokuNo(null);
		// 開始日
		comDto.setSc006StartDay(null);
		// 終了日
		comDto.setSc006EndDay(null);
		// 社宅賃貸料
		comDto.setSc006ChintaiRyo(null);
		// 駐車場料金
		comDto.setSc006TyusyajoRyokin(null);
		// 共益費(事業者負担)
		comDto.setSc006Kyoekihi(null);

		/** ドロップダウンリスト */
		// 原籍会社選択値
		comDto.setSc006OldKaisyaNameSelect(null);
		// 原籍会社選択リスト
		comDto.setSc006OldKaisyaNameSelectList(null);
		// 給与支給会社名選択値
		comDto.setSc006KyuyoKaisyaSelect(null);
		// 給与支給会社名選択リスト
		comDto.setSc006KyuyoKaisyaSelectList(null);
		// 居住者区分選択値
		comDto.setSc006KyojyusyaKbnSelect(null);
		// 居住者区分選択リスト
		comDto.setSc006KyojyusyaKbnSelectList(null);
		// 役員算定選択値
		comDto.setSc006YakuinSanteiSelect(null);
		// 役員算定選択リスト
		comDto.setSc006YakuinSanteiSelectList(null);
		// 共益費支払月選択値
		comDto.setSc006KyoekihiPayMonthSelect(null);
		// 共益費支払月選択リスト
		comDto.setSc006KyoekihiPayMonthSelectList(null);
		// 搬入希望時間選択値
		comDto.setSc006KibouTimeInSelect(null);
		// 搬入希望時間選択リスト
		comDto.setSc006KibouTimeInSelectList(null);
		// 搬出希望時間選択値
		comDto.setSc006KibouTimeOutSelect(null);
		// 搬出希望時間選択リスト
		comDto.setSc006KibouTimeOutSelectList(null);
		// 配属会社名選択値
		comDto.setSc006HaizokuKaisyaSelect(null);
		// 配属会社名選択リスト
		comDto.setSc006HaizokuKaisyaSelectList(null);
		// 出向の有無(相互利用状況)選択値
		comDto.setSc006SogoRyojokyoSelect(null);
		// 出向の有無(相互利用状況)選択リスト
		comDto.setSc006SogoRyojokyoSelectList(null);
		// 貸付会社選択値
		comDto.setSc006TaiyoKaisyaSelect(null);
		// 貸付会社選択リスト
		comDto.setSc006TaiyoKaisyaSelectList(null);
		// 借受会社選択値
		comDto.setSc006KariukeKaisyaSelect(null);
		// 借受会社選択リスト
		comDto.setSc006KariukeKaisyaSelectList(null);
		// 相互利用判定区分選択値
		comDto.setSc006SogoHanteiKbnSelect(null);
		// 相互利用判定区分選択リスト
		comDto.setSc006SogoHanteiKbnSelectList(null);
		// 社宅使用料会社間送金区分選択値
		comDto.setSc006SokinShatakuSelect(null);
		// 社宅使用料会社間送金区分選択リスト
		comDto.setSc006SokinShatakuSelectList(null);
		// 共益費会社間送付区分選択値
		comDto.setSc006SokinKyoekihiSelect(null);
		// 共益費会社間送付区分選択リスト
		comDto.setSc006SokinKyoekihiSelectList(null);

		/** リストテーブル */
		// 備品情報リスト
		comDto.setBihinInfoListTableData(null);

		/** 上部ボタン */
		// 社員入力
		comDto.setSc006ShinseiNaiyoDisableFlg(false);
		// 社宅入力支援
		comDto.setShayakuHeyaShienDisableFlg(false);
		// 社宅使用料入力支援
		comDto.setShiyoryoShienDisableFlg(false);

		/** タブ切替 */
		// 備品情報タブ
		comDto.setTbpBihinInfo(false);
		// 役員情報/相互利用情報タブ
		comDto.setTbpSougoRiyouInfo(false);

		/** 社宅情報 */
		// 原籍会社
		comDto.setSc006OldKaisyaNameSelectDisableFlg(false);
		// 給与支給会社
		comDto.setSc006KyuyoKaisyaSelectDisableFlg(false);
		// 入居予定日
		comDto.setSc006NyukyoYoteiDayDisableFlg(false);
		// 退居予定日
		comDto.setSc006TaikyoYoteiDayDisableFlg(false);
		// 居住者区分
		comDto.setSc006KyojyusyaKbnSelectDisableFlg(false);
		// 役員算定
		comDto.setSc006YakuinSanteiSelectDisableFlg(false);
		// 社宅使用料調整金額
		comDto.setSc006SiyoroTyoseiPayDisableFlg(false);
		// 社宅情報:個人負担共益費 協議中
		comDto.setSc006KyoekihiKyogichuCheckDisableFlg(false);
		// 個人負担共益費月額
		comDto.setSc006KyoekihiMonthPayDisableFlg(false);
		// 個人負担共益費調整金額
		comDto.setSc006KyoekihiTyoseiPayDisableFlg(false);
		// 共益費支払月
		comDto.setSc006KyoekihiPayMonthSelectDisableFlg(false);
		// 駐車場入力支援（区画１）
		comDto.setParkingShien1DisableFlg(false);
		// 利用開始日1
		comDto.setSc006RiyouStartDayOneDisableFlg(false);
		// 区画1クリア
		comDto.setClearParking1DisableFlg(false);
		// 利用終了日1
		comDto.setSc006RiyouEndDayOneDisableFlg(false);
		// 駐車場入力支援（区画2）
		comDto.setParkingShien2DisableFlg(false);
		// 利用開始日2
		comDto.setSc006RiyouStartDayTwoDisableFlg(false);
		// 区画2クリア
		comDto.setClearParking2DisableFlg(false);
		// 利用終了日2
		comDto.setSc006RiyouEndDayTwoDisableFlg(false);
		// 社宅情報:駐車場使用料調整金額
		comDto.setSc006TyusyaTyoseiPayDisableFlg(false);
		// 社宅情報:備考
		comDto.setSc006BicouDisableFlg(false);
		// 社宅情報
		comDto.setGShatakuInfoControlStatusFlg(false);

		/** 備品情報 */
		// 貸与日
		comDto.setSc006TaiyoDayDisableFlg(false);
		// 返却日
		comDto.setSc006HenkyakuDayDisableFlg(false);
		// 搬入希望日
		comDto.setSc006KibouDayInDisableFlg(false);
		// 搬入希望時間帯
		comDto.setSc006KibouTimeInSelectDisableFlg(false);
		// 搬入本人連絡先
		comDto.setSc006HonninAddrInDisableFlg(false);
		// 受取代理人
		comDto.setSc006UketoriDairiInNameDisableFlg(false);
		// 搬入社員入力支援
		comDto.setSc006UketoriDairiInShienDisableFlg(false);
		// 受取代理人連絡先
		comDto.setSc006UketoriDairiAddrDisableFlg(false);
		// 搬出希望日
		comDto.setSc006KibouDayOutDisableFlg(false);
		// 搬出希望日時時間帯
		comDto.setSc006KibouTimeOutSelectDisableFlg(false);
		// 搬出本人連絡先
		comDto.setSc006HonninAddrOutDisableFlg(false);
		// 搬出立会代理人
		comDto.setSc006TachiaiDairiDisableFlg(false);
		// 搬出社員入力支援
		comDto.setSc006TachiaiDairiShienDisableFlg(false);
		// 搬出立会代理人連絡先
		comDto.setSc006TachiaiDairiAddrDisableFlg(false);
		// 代理人備考
		comDto.setSc006DairiBikoDisableFlg(false);
		// 備品備考
		comDto.setSc006BihinBikoDisableFlg(false);
		// 備品情報
		comDto.setGBihinInfoControlStatusFlg(false);

		/** 相互利用/役員情報 */
		// 貸付会社
		comDto.setSc006TaiyoKaisyaSelectDisableFlg(false);
		// 借受会社
		comDto.setSc006KariukeKaisyaSelectDisableFlg(false);
		// 出向の有無(相互利用状況)
		comDto.setSc006SogoRyojokyoSelectDisableFlg(false);
		// 相互利用判定区分
		comDto.setSc006SogoHanteiKbnSelectDisableFlg(false);
		// 社宅使用料会社間送金区分
		comDto.setSc006SokinShatakuSelectDisableFlg(false);
		// 送金区分（共益費）
		comDto.setSc006SokinKyoekihiSelectDisableFlg(false);
		// 社宅賃貸料
		comDto.setSc006ChintaiRyoDisableFlg(false);
		// 駐車場料金
		comDto.setSc006TyusyajoRyokinDisableFlg(false);
		// 共益費(事業者負担)
		comDto.setSc006KyoekihiDisableFlg(false);
		// 開始日
		comDto.setSc006StartDayDisableFlg(false);
		// 終了日
		comDto.setSc006EndDayDisableFlg(false);
		// 配属会社名
		comDto.setSc006HaizokuKaisyaSelectDisableFlg(false);
		// 所属機関
		comDto.setSc006SyozokuKikanDisableFlg(false);
		// 室・部名
		comDto.setSc006SituBuNameDisableFlg(false);
		// 課等名
		comDto.setSc006KanadoMeiDisableFlg(false);
		// 配属データコード番号
		comDto.setSc006HaizokuNoDisableFlg(false);
		// 相互利用情報タブ項目制御
		//private String gSougoRiyouInfoControlStatusFlg(false);
		// 相互利用情報タブ項目制御
		comDto.setHdnSougoRiyouFlg(null);

		/** 下部ボタン */
		// 運用ガイドボタン
		comDto.setBtnUnyonGuideDisableFlg(false);
		// 登録ボタン
		comDto.setBtnRegistDisableFlg(false);
		// 次月予約ボタン
		comDto.setBtnJigetuYoyakuDisableFlg(false);
		// 削除ボタン
		comDto.setBtnDeleteDisableFlg(false);

		/** エラー変数初期化 */
		comDto.setSc006OldKaisyaNameSelectErr(null);
		comDto.setSc006KyuyoKaisyaSelectErr(null);
		comDto.setSc006NyukyoYoteiDayErr(null);
		comDto.setSc006TaikyoYoteiDayErr(null);
		comDto.setSc006RiyouStartDayOneErr(null);
		comDto.setSc006RiyouEndDayOneErr(null);
		comDto.setSc006KyojyusyaKbnSelectErr(null);
		comDto.setSc006RiyouStartDayTwoErr(null);
		comDto.setSc006RiyouEndDayTwoErr(null);
		comDto.setSc006YakuinSanteiSelectErr(null);
		comDto.setSc006SiyoroTyoseiPayErr(null);
		comDto.setSc006TyusyaTyoseiPayErr(null);
		comDto.setSc006KyoekihiMonthPayErr(null);
		comDto.setSc006KyoekihiTyoseiPayErr(null);
		comDto.setSc006KyoekihiPayMonthSelectErr(null);
		comDto.setSc006TaiyoDayErr(null);
		comDto.setSc006HenkyakuDayErr(null);
		comDto.setSc006KibouDayInErr(null);
		comDto.setSc006KibouTimeInSelectErr(null);
		comDto.setSc006HonninAddrInErr(null);
		comDto.setSc006UketoriDairiInNameErr(null);
		comDto.setSc006UketoriDairiAddrErr(null);
		comDto.setSc006KibouDayOutErr(null);
		comDto.setSc006KibouTimeOutSelectErr(null);
		comDto.setSc006HonninAddrOutErr(null);
		comDto.setSc006TachiaiDairiErr(null);
		comDto.setSc006TachiaiDairiAddrErr(null);
		comDto.setSc006HaizokuKaisyaSelectErr(null);
		comDto.setSc006SogoRyojokyoSelectErr(null);
		comDto.setSc006SyozokuKikanErr(null);
		comDto.setSc006TaiyoKaisyaSelectErr(null);
		comDto.setSc006SituBuNameErr(null);
		comDto.setSc006KariukeKaisyaSelectErr(null);
		comDto.setSc006KanadoMeiErr(null);
		comDto.setSc006SogoHanteiKbnSelectErr(null);
		comDto.setSc006HaizokuNoErr(null);
		comDto.setSc006SokinShatakuSelectErr(null);
		comDto.setSc006SokinKyoekihiSelectErr(null);
		comDto.setSc006StartDayErr(null);
		comDto.setSc006EndDayErr(null);
		comDto.setSc006ChintaiRyoErr(null);
		comDto.setSc006TyusyajoRyokinErr(null);
		comDto.setSc006KyoekihiErr(null);
	}
}
