/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc006;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
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
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetNyukyoTeijiNoDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetNyukyoTeijiNoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetNyutaikyoYoteiDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetNyutaikyoYoteiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetRentalPatternForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetRentalPatternForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetRentalPatternInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetRentalPatternInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetSameAppNoCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetSameParkingBlockTeijiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetSameParkingBlockTeijiInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetSameRoomTeijiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetSameRoomTeijiInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetShatakuDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetShatakuDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetShatakuParkingBlockExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetShatakuParkingBlockExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetShatakuParkingBlockForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetShatakuRoomExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetShatakuRoomExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetShatakuRoomForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetShatakuYoyakuDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetTeijiBihinDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetTeijiBihinDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetTeijiDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetTeijiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetTeijiDataForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBaseBusinessLogicUtils.SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBaseBusinessLogicUtils.SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuParkingBlock;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuRoom;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3021TNyutaikyoYoteiData;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3022TShatakuYoyakuData;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3022TTeijiBihinData;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3022TTeijiData;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TRentalPattern;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006DeleteShatakuYoyakuExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetCompanyAgencyListExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetMaxRentalPatternIdExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetNyukyoTeijiNoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetNyutaikyoYoteiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetRentalPatternInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetTeijiBihinDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetSameAppNoCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetSameParkingBlockTeijiInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetSameRoomTeijiInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetShatakuDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetShatakuParkingBlockExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006UpdateParkingBlockExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetShatakuRoomExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetShatakuYoyakuDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetTeijiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006UpdateBihinListDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006UpdateNyutaikyoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006UpdateRentalPatternExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006UpdateShatakuRoomExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006UpdateShatakuYoyakuDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006UpdateTeijiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006UpdateTeijiDataKanriNoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuParkingBlockRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3022TTeijiDataRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3030TRentalPatternRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.CodeCacheUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.PropertyUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.SkfPageBusinessLogicUtils;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022Sc006common.Skf3022Sc006CommonAsyncDto;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022Sc006common.Skf3022Sc006CommonDto;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022Sc006common.Skf3022Sc006CommonDto.UPDATE_COUNTER;

/**
 * Skf3022Sc006SharedService 提示データ登録共通クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3022Sc006SharedService {

	@Autowired
	private SkfDropDownUtils ddlUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private Skf3022Sc006GetTeijiDataExpRepository skf3022Sc006GetTeijiDataExpRepository;
	@Autowired
	private Skf3022Sc006GetSameAppNoCountExpRepository skf3022Sc006GetSameAppNoCountExpRepository;
	@Autowired
	private Skf3022Sc006GetRentalPatternInfoExpRepository skf3022Sc006GetRentalPatternInfoExpRepository;
	@Autowired
	private CodeCacheUtils codeCacheUtils;
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	@Autowired
	private Skf3022Sc006GetShatakuDataExpRepository skf3022Sc006GetShatakuDataExpRepository;
	@Autowired
	private Skf3022Sc006GetNyutaikyoYoteiDataExpRepository skf3022Sc006GetNyutaikyoYoteiDataExpRepository;
	@Autowired
	private Skf3022Sc006GetShatakuRoomExpRepository skf3022Sc006GetShatakuRoomExpRepository;
	@Autowired
	private Skf3022Sc006GetShatakuParkingBlockExpRepository skf3022Sc006GetShatakuParkingBlockExpRepository;
	@Autowired
	private Skf3022Sc006GetSameRoomTeijiInfoExpRepository skf3022Sc006GetSameRoomTeijiInfoExpRepository;
	@Autowired
	private Skf3022Sc006GetSameParkingBlockTeijiInfoExpRepository skf3022Sc006GetSameParkingBlockTeijiInfoExpRepository;
	@Autowired
	private Skf3022Sc006GetCompanyAgencyListExpRepository skf3022Sc006GetCompanyAgencyListExpRepository;
	@Autowired
	private Skf3022Sc006GetShatakuYoyakuDataExpRepository skf3022Sc006GetShatakuYoyakuDataExpRepository;
	@Autowired
	private Skf3022Sc006GetTeijiBihinDataExpRepository skf3022Sc006GetTeijiBihinDataExpRepository;
	@Autowired
	private Skf3022Sc006GetMaxRentalPatternIdExpRepository skf3022Sc006GetMaxRentalPatternIdExpRepository;
	@Autowired
	private Skf3022Sc006UpdateTeijiDataExpRepository skf3022Sc006UpdateTeijiDataExpRepository;
	@Autowired
	private Skf3022Sc006GetNyukyoTeijiNoExpRepository skf3022Sc006GetNyukyoTeijiNoExpRepository;
	@Autowired
	private Skf3022TTeijiDataRepository skf3022TTeijiDataRepository;
	@Autowired
	private Skf3022Sc006UpdateBihinListDataExpRepository skf3022Sc006UpdateBihinListDataExpRepository;
	@Autowired
	private Skf3022Sc006UpdateParkingBlockExpRepository skf3022Sc006UpdateParkingBlockExpRepository;
	@Autowired
	private Skf3010MShatakuParkingBlockRepository skf3010MShatakuParkingBlockRepository;
	@Autowired
	private Skf3022Sc006DeleteShatakuYoyakuExpRepository skf3022Sc006DeleteShatakuYoyakuExpRepository;
	@Autowired
	private Skf3030TRentalPatternRepository skf3030TRentalPatternRepository;
	@Autowired
	private Skf3022Sc006UpdateTeijiDataKanriNoExpRepository skf3022Sc006UpdateTeijiDataKanriNoExpRepository;
	@Autowired
	private Skf3022Sc006UpdateShatakuYoyakuDataExpRepository skf3022Sc006UpdateShatakuYoyakuDataExpRepository;
	@Autowired
	private Skf3022Sc006UpdateRentalPatternExpRepository skf3022Sc006UpdateRentalPatternExpRepository;
	@Autowired
	private Skf3022Sc006UpdateNyutaikyoExpRepository skf3022Sc006UpdateNyutaikyoExpRepository;
	@Autowired
	private Skf3022Sc006UpdateShatakuRoomExpRepository skf3022Sc006UpdateShatakuRoomExpRepository;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private SkfPageBusinessLogicUtils skfPageBusinessLogicUtils;

	/** 定数 */
	// 処理区分
	private static final String MAX_END_DATE = "99991231";
	/** 社宅使用料計算：処理区分 */
	private static final String SHORI_KBN_1 = "1";
	private static final String SHORI_KBN_4 = "4";


	/**
	 * 日割計算
	 * システム処理年月、利用開始日、利用終了日、月額から日割金額を算出する
	 * 
	 * 当月利用日数 * 社宅使用料月額 / 当月(処理年月)の日数
	 * 
	 * @param startDate	利用開始年月日
	 * @param endDate	利用終了年月日
	 * @param val		使用料月額
	 * @return			日割金額
	 */
	private String hiwariKeisan(String startDate, String endDate, String val) {
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
		// 処理年月
		String yearMonth = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu();
		LogUtils.debugByMsg("処理年月：" + yearMonth + ", 利用開始：" + startDate + ", 利用終了：" + endDate + ", 月額：" + val);
		// 処理年月の月の日数
		int nisuMonth = YearMonth.of(Integer.parseInt(yearMonth.substring(0, 4)),
						Integer.parseInt(yearMonth.substring(4, 6))).lengthOfMonth();
		int nisu = 0;
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
		if (Objects.equals(yearMonth, startYearMonth) && Objects.equals(yearMonth, endYearMonth)) {
			LogUtils.debugByMsg("処理年月と計算開始年月が同年同月、且つ、計算開始年月と計算終了年月が同一");
			nisu = endDay - startDay + 1;
		} else if (Objects.equals(yearMonth, startYearMonth) && !Objects.equals(yearMonth, endYearMonth)) {
			LogUtils.debugByMsg("処理年月と計算開始年月が同年同月、且つ、計算開始年月と計算終了年月が異なる");
			nisu = dayOfMonthStart - startDay + 1;
		} else if (!Objects.equals(yearMonth, startYearMonth) && Objects.equals(yearMonth, endYearMonth)) {
			LogUtils.debugByMsg("処理年月と計算開始年月が異なる、且つ、計算開始年月と計算終了年月が同一");
			nisu = endDay;
		} else if (Integer.parseInt(yearMonth) < Integer.parseInt(startYearMonth)) {
			LogUtils.debugByMsg("処理年月より計算開始年月が未来");
			nisu = 0;
		} else if (Integer.parseInt(yearMonth) > Integer.parseInt(endYearMonth)) {
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
	 * 使用料計算
	 * 社宅、駐車場の月額金額、調整後金額、日割金額を算出、設定する
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param chushajoKanriNo	駐車場管理番号
	 * @param parkBlockKind		区画種別："1"(区画1) or 2(区画2)
	 * @param paramMap			パラメータMap
	 * @param resultMap			*リザルトMap
	 * @param errMsg			*エラーメッセージ
	 * @return	true(異常)　/　false(正常)
	 * @throws Exception
	 */
	public Boolean siyoryoKeiSan(String chushajoKanriNo, String parkBlockKind,
			Map<String, String> paramMap, Map<String, String> resultMap, StringBuffer errMsg) {

		// 戻り値
		Map<String, String> tmpMap = new HashMap<String, String>();
		errMsg.delete(0, errMsg.length());

		// 使用料計算結果
		String shatakuRiyoryou = "";
		String chushajoRiyoryou = "";

		// 社宅利用料計算情報引数
		SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp inputEntity = new SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp();
		// 処理年月
		inputEntity.setShoriNengetsu(skfBaseBusinessLogicUtils.getSystemProcessNenGetsu());
		// 役員区分
		inputEntity.setYakuinKbn(paramMap.get("sc006YakuinSanteiSelect"));

		// 駐車場管理番号判定
		if (CheckUtils.isEmpty(chushajoKanriNo)) {
			// 駐車場管理番号なし
			// 処理区分
			inputEntity.setShoriKbn(SHORI_KBN_1);
			// 使用料計算に必要な情報を取得
			List<Skf3022Sc006GetShatakuDataExp> shatakuDataList = new ArrayList<Skf3022Sc006GetShatakuDataExp>();
			Skf3022Sc006GetShatakuDataExpParameter param = new Skf3022Sc006GetShatakuDataExpParameter();
			Long shatakuKanriNo = CheckUtils.isEmpty(paramMap.get("hdnShatakuKanriNo")) ? null : Long.parseLong(paramMap.get("hdnShatakuKanriNo"));
			param.setShatakuKanriNo(shatakuKanriNo);
			if (shatakuKanriNo != null) {
				shatakuDataList = skf3022Sc006GetShatakuDataExpRepository.getShatakuData(param);
			}
			// 取得結果判定
			if (shatakuDataList.size() > 0) {
				Skf3022Sc006GetShatakuDataExp shatakuData = shatakuDataList.get(0);
				// 建築年月日
				inputEntity.setKenchikuNengappi(shatakuData.getBuildDate());
				// 地域区分
				inputEntity.setAreaKbn(shatakuData.getAreaKbn());
				// 社宅構造区分
				inputEntity.setStructureKbn(shatakuData.getStructureKbn());
				// 駐車場構造区分
				inputEntity.setChuushajoKouzouKbn(shatakuData.getParkingStructureKbn());
			}
			// DTOの使用料情報が設定されている場合
			if (!CheckUtils.isEmpty(paramMap.get("hdnRateShienYoto"))
					&& !CheckUtils.isEmpty(paramMap.get("hdnRateShienNobeMenseki"))
					&& !CheckUtils.isEmpty(paramMap.get("hdnRateShienSunroomMenseki"))
					&& !CheckUtils.isEmpty(paramMap.get("hdnRateShienKaidanMenseki"))
					&& !CheckUtils.isEmpty(paramMap.get("hdnRateShienMonookiMenseki"))
					&& !CheckUtils.isEmpty(paramMap.get("hdnRateShienKijunMenseki"))
					&& !CheckUtils.isEmpty(paramMap.get("hdnRateShienShatakuMenseki"))) {

				// 用途区分
				inputEntity.setAuseKbn(paramMap.get("hdnRateShienYoto"));
				// 延べ面積
				inputEntity.setNobeMenseki(getMensekiText(paramMap.get("hdnRateShienNobeMenseki")));
				// サンルーム面積
				inputEntity.setSunroomMenseki(getMensekiText(paramMap.get("hdnRateShienSunroomMenseki")));
				// 階段面積
				inputEntity.setKaidanMenseki(getMensekiText(paramMap.get("hdnRateShienKaidanMenseki")));
				// 物置面積
				inputEntity.setMonookiMenseki(getMensekiText(paramMap.get("hdnRateShienMonookiMenseki")));
				// 基準使用料算定上延べ面積
				inputEntity.setKijunMenseki(getMensekiText(paramMap.get("hdnRateShienKijunMenseki")));
				// 社宅使用料算定上延べ面積
				inputEntity.setShatakuMenseki(getMensekiText(paramMap.get("hdnRateShienShatakuMenseki")));
			}
			// 生年月日
			inputEntity.setSeinengappi(paramMap.get("hdnBirthday"));
			// 社宅賃貸料
			inputEntity.setShatakuChintairyou(getKingakuText(paramMap.get("sc006ChintaiRyo")));
			// 駐車場賃貸料
			inputEntity.setChyshajoChintairyou(getKingakuText(paramMap.get("sc006TyusyajoRyokin")));
		} else {
			// 処理区分
			inputEntity.setShoriKbn(SHORI_KBN_4);
			// 駐車場賃貸料
			inputEntity.setChyshajoChintairyou(getKingakuText(paramMap.get("sc006TyusyajoRyokin")));
			// 駐車場管理番号
			inputEntity.setChushajoKanriBangou(chushajoKanriNo);
			// 社宅管理番号
			inputEntity.setShatakuKanriBangou(paramMap.get("hdnShatakuKanriNo"));
			// DTOの使用料情報が設定されている場合
			if (!CheckUtils.isEmpty(paramMap.get("hdnRateShienYoto"))) {
				// 用途区分
				inputEntity.setAuseKbn(paramMap.get("hdnRateShienYoto"));
			}
		}
		// 使用料計算結果取得
		SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp outputEntity = new SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp();
		try {
			outputEntity = skfBaseBusinessLogicUtils.getShatakuShiyouryouKeisan(inputEntity);
		} catch (ParseException e) {
			outputEntity.setErrMessage(e.getMessage());
		}
		// 正常に計算できていたら、値をセット
		// 計算結果判定
		if (CheckUtils.isEmpty(outputEntity.getErrMessage())) {
			// 社宅使用料月額
			shatakuRiyoryou = outputEntity.getShatakuShiyouryouGetsugaku().toPlainString();
			// 駐車場使用料
			chushajoRiyoryou = outputEntity.getChushajouShiyoryou().toPlainString();
		} else {
			errMsg.append(outputEntity.getErrMessage());
			LogUtils.debugByMsg("使用料計算でエラー検出:" + outputEntity.getErrMessage());
			return true;
		}

		// 駐車場管理番号判定
		if (CheckUtils.isEmpty(chushajoKanriNo)) {
			/** 社宅使用料日割金額算出 */
			// 社宅使用料日割金額
			int hiwariPay = 0;
			// 社宅使用料調整金額
			int siyoroTyoseiPay = Integer.parseInt(getKingakuText(paramMap.get("sc006SiyoroTyoseiPay")));
			// 入居予定日が変更された場合
			if (CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(paramMap.get("hdnNyutaikyoKbn"))) {
				LogUtils.debugByMsg("社宅使用料日割計算：入居予定日変更時");
				// 社宅使用料日割金額算出(当月利用日数 * 社宅使用料月額 / 当月(処理年月)の日数)
				hiwariPay = Integer.parseInt(hiwariKeisan(getDateText(paramMap.get("sc006NyukyoYoteiDay")), "", shatakuRiyoryou));
			} else if (CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(paramMap.get("hdnNyutaikyoKbn"))) {
				// 退居予定日が変更された場合
				LogUtils.debugByMsg("社宅使用料日割計算：退居予定日変更時");
				// 社宅使用料日割金額算出(当月利用日数 * 社宅使用料月額 / 当月(処理年月)の日数)
				hiwariPay = Integer.parseInt(hiwariKeisan(
						getDateText(paramMap.get("hdnNyukyoDate")), getDateText(paramMap.get("sc006TaikyoYoteiDay")), shatakuRiyoryou));
			}
			// 社宅使用料日割金額設定
			tmpMap.put("sc006SiyoryoHiwariPay", getKanmaNumEdit(Integer.toString(hiwariPay)));
			// 社宅使用料月額（調整後）設定(社宅使用料日割金額 + 社宅使用料調整金額)
			tmpMap.put("sc006SyatauMonthPayAfter", getKanmaNumEdit(Integer.toString(hiwariPay + siyoroTyoseiPay)));
			// 社宅使用料月額
			tmpMap.put("sc006ShiyoryoTsukigaku", getKanmaNumEdit(shatakuRiyoryou));
		} else {
			// 駐車場日割金額(区画1)
			int hiwariPay1 = Integer.parseInt(getKingakuText(paramMap.get("sc006TyusyaDayPayOne")));
			// 駐車場日割金額(区画2)
			int hiwariPay2 = Integer.parseInt(getKingakuText(paramMap.get("sc006TyusyaDayPayTwo")));
			// 区画種別判定
			if ("1".equals(parkBlockKind)) {
				/** 駐車場区画1日割金額算出 */
				// 利用開始日（区画１）が変更された場合
				if (CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(paramMap.get("hdnNyutaikyoKbn"))) {
					LogUtils.debugByMsg("駐車場使用料区画1日割計算：利用開始日(区画1)変更時");
					// 駐車場使用料日割金額(区画1)算出(当月利用日数 * 駐車場使用料月額（区画１） / 当月(処理年月)の日数)
					hiwariPay1 = Integer.parseInt(
							hiwariKeisan(getDateText(paramMap.get("sc006RiyouStartDayOne")), "", chushajoRiyoryou));

				} else if (CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(paramMap.get("hdnNyutaikyoKbn"))) {
					// 利用終了日（区画１）が変更された場合
					LogUtils.debugByMsg("駐車場使用料区画1日割計算：利用終了日(区画1)変更時");
					// 駐車場使用料日割金額(区画1)算出(当月利用日数 * 駐車場使用料月額（区画１） / 当月(処理年月)の日数)
					hiwariPay1 = Integer.parseInt(hiwariKeisan(
							getDateText(paramMap.get("hdnRiyouStartDayOne")), getDateText(paramMap.get("sc006RiyouEndDayOne")), chushajoRiyoryou));
				}
				// 駐車場使用料日割金額(区画1)設定
				tmpMap.put("sc006TyusyaDayPayOne", getKanmaNumEdit(Integer.toString(hiwariPay1)));
				// 駐車場月額1設定
				tmpMap.put("sc006TyusyaMonthPayOne", getKanmaNumEdit(chushajoRiyoryou));
			/* US imart移植 現行システムバグ改修 2019.11.14 */
			// } else {
			// 区画種別が「1」以外の場合「2」の処理を実施していたが区画種別は空文字が渡されることもあるため区画種別が「2」であるか判定するよう修正
			} else if("2".equals(parkBlockKind)) {
			/* UE imart移植 現行システムバグ改修 */
				/** 駐車場区画2日割金額算出 */
				// 利用開始日（区画２）が変更された場合
				if (CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(paramMap.get("hdnNyutaikyoKbn"))) {
					LogUtils.debugByMsg("駐車場使用料区画2日割計算：利用開始日(区画2)変更時");
					// 駐車場使用料日割金額(区画2)算出(当月利用日数 * 駐車場使用料月額（区画2） / 当月(処理年月)の日数)
					hiwariPay2 = Integer.parseInt(hiwariKeisan(
							getDateText(paramMap.get("sc006RiyouStartDayTwo")), "", chushajoRiyoryou));
				} else if (CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(paramMap.get("hdnNyutaikyoKbn"))) {
					// 利用終了日（区画２）が変更された場合
					LogUtils.debugByMsg("駐車場使用料区画2日割計算：利用終了日(区画2)変更時");
					// 駐車場使用料日割金額(区画2)算出(当月利用日数 * 駐車場使用料月額（区画１） / 当月(処理年月)の日数)
					hiwariPay2 = Integer.parseInt(hiwariKeisan(
							getDateText(paramMap.get("hdnRiyouStartDayTwo")), getDateText(paramMap.get("sc006RiyouEndDayTwo")), chushajoRiyoryou));
				}
				// 駐車場使用料日割金額(区画2)設定
				tmpMap.put("sc006TyusyaDayPayTwo", getKanmaNumEdit(Integer.toString(hiwariPay2)));
				// 駐車場月額2設定
				tmpMap.put("sc006TyusyaMonthPayTwo", getKanmaNumEdit(chushajoRiyoryou));
			}
			// 駐車場使用料調整金額
			int siyoroTyoseiPay = Integer.parseInt(getKingakuText(paramMap.get("sc006TyusyaTyoseiPay")));
			// 駐車場使用料月額（調整後）設定(駐車場使用料日割金額(区画1) + 駐車場使用料日割金額(区画2) + 駐車場使用料調整金額)
			tmpMap.put("sc006TyusyaMonthPayAfter", getKanmaNumEdit(Integer.toString(hiwariPay1 + hiwariPay2 + siyoroTyoseiPay)));
		}
		resultMap.clear();
		resultMap.putAll(tmpMap);
		return false;
	}

	/**
	 * 使用料再設定
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param comDto	*DTO
	 */
	public void resetShiyoryoInfo(Skf3022Sc006CommonDto comDto) {
		// 画面の入退居区分が”入居”の場合
		if (codeCacheUtils.getGenericCodeName(FunctionIdConstant.GENERIC_CODE_NYUTAIKYO_KBN,
				CodeConstant.NYUTAIKYO_KBN_NYUKYO).equals(comDto.getSc006NyutaikyoKbn())) {
			// 社宅管理番号
			comDto.setHdnShatakuKanriNoOld(comDto.getHdnShatakuKanriNo());
			// 部屋管理番号
			comDto.setHdnRoomKanriNoOld(comDto.getHdnRoomKanriNo());
			// 使用料パターン
			comDto.setHdnSiyouryoIdOld(comDto.getHdnSiyouryoId());
			// 入居予定日
			comDto.setHdnNyukyoDate(getDateText(comDto.getSc006NyukyoYoteiDay()));
			// 役員算定
			comDto.setHdnYakuin(comDto.getSc006YakuinSanteiSelect());
			// 個人負担共益費月額
			comDto.setHdnKojinFutan(getKingakuText(comDto.getSc006KyoekihiMonthPay()));
			// 駐車場管理番号１
			comDto.setHdnChushajoNoOneOld(comDto.getHdnChushajoNoOne());
			// 利用開始日１
			comDto.setHdnRiyouStartDayOne(getDateText(comDto.getSc006RiyouStartDayOne()));
			// 駐車場管理番号２
			comDto.setHdnChushajoNoTwoOld(comDto.getHdnChushajoNoTwo());
			// 利用開始日２
			comDto.setHdnRiyouStartDayTwo(getDateText(comDto.getSc006RiyouStartDayTwo()));
		// 画面の入退居区分が”退居”の場合
		} else if (codeCacheUtils.getGenericCodeName(FunctionIdConstant.GENERIC_CODE_NYUTAIKYO_KBN,
					CodeConstant.NYUTAIKYO_KBN_TAIKYO).equals(comDto.getSc006NyutaikyoKbn())) {
			// 退居予定日
			comDto.setHdnTaikyoDate(getDateText(comDto.getSc006TaikyoYoteiDay()));
			// 利用終了日１
			comDto.setHdnRiyouEndDayOne(getDateText(comDto.getSc006RiyouEndDayOne()));
			// 利用終了日２
			comDto.setHdnRiyouEndDayTwo(getDateText(comDto.getHdnRiyouEndDayTwo()));
		// 画面の入退居区分が”変更”の場合
		} else {
			// 個人負担共益費月額
			comDto.setHdnKojinFutan(getKingakuText(comDto.getSc006KyoekihiMonthPay()));
		}
		// 次月予約存在フラグ
//		Me.hdnYoyakuFlg.Value = DATA_0
	}

	/**
	 * 画面項目設定
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param teijiData		提示データ(DB取得値)
	 * @param comDto		*DTO
	 * @throws Exception
	 */
	public void setControlValues(Skf3022Sc006GetTeijiDataExp teijiData, Skf3022Sc006CommonDto comDto) throws Exception {

		SimpleDateFormat dateFormat = new SimpleDateFormat(Skf3022Sc006CommonDto.DATE_FORMAT);

		// 社員番号
		comDto.setSc006ShainNo(teijiData.getShainNo());
		// 社員氏名
		if (!CheckUtils.isEmpty(teijiData.getShainName())) {
			comDto.setSc006ShainName(teijiData.getShainName());
		}
		// 社宅名
		if (!CheckUtils.isEmpty(teijiData.getShatakuName())) {
			comDto.setSc006ShatakuName(teijiData.getShatakuName());
		}
		// 部屋番号
		if (!CheckUtils.isEmpty(teijiData.getRoomNo())) {
			comDto.setSc006HeyaNo(teijiData.getRoomNo());
		}
		// 貸与規格(使用料計算パターン名)
		if (!CheckUtils.isEmpty(teijiData.getRentalPatternName())) {
			comDto.setSc006SiyoryoPatName(teijiData.getRentalPatternName());
		}
		// 社宅使用料月額（ヘッダ項目）
		if (teijiData.getRental() != null) {
			comDto.setSc006SiyoryoMonthPay(getKingakuEdit(teijiData.getRental().toString()));
			/** AS imart移植 2019.11.19 */
			// .NET版ではドロップダウン選択肢設定時自動で初期値"0"に設定されるため、未設定時は「"0"」を設定する
			if (Objects.equals(comDto.getSc006YakuinSanteiSelect(), null)) {
				comDto.setSc006YakuinSanteiSelect(CodeConstant.STRING_ZERO);
			}
			/** AE imart移植 2019.11.19 */
			Map<String, String> paramMap = createSiyoryoKeiSanParam(comDto);	// 使用料計算パラメータ
			Map<String, String> resultMap = new HashMap<String, String>();		// 使用料計算戻り値
			StringBuffer errMsg = new StringBuffer();							// エラーメッセージ
			if (siyoryoKeiSan("", "", paramMap, resultMap, errMsg)) {
				// 使用料計算でエラー
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.SKF3020_ERR_MSG_COMMON, errMsg.toString());
			} else {
				// 使用料計算戻り値設定
				setSiyoryoKeiSanParam(resultMap, comDto);
			}
		}
		// 入退居区分(ヘッダ項目)
		comDto.setSc006NyutaikyoKbn(codeCacheUtils.getGenericCodeName(
				FunctionIdConstant.GENERIC_CODE_NYUTAIKYO_KBN, comDto.getHdnNyutaikyoKbnOld()));
		// 入退居区分（hidden変数）
		comDto.setHdnNyutaikyoKbn(comDto.getHdnNyutaikyoKbnOld());
		// 「変更」を「退居」と表示するか判定
		if (CodeConstant.NYUTAIKYO_KBN_HENKO.equals(comDto.getHdnNyutaikyoKbnOld())
				&& CheckUtils.isEmpty(comDto.getHdnShoruikanriNo())) {
			// 入退居区分が"変更"、且つ、申請書類管理番号（hidden変数）がない場合、"退居"に設定
			comDto.setSc006NyutaikyoKbn(codeCacheUtils.getGenericCodeName(
					FunctionIdConstant.GENERIC_CODE_NYUTAIKYO_KBN, CodeConstant.NYUTAIKYO_KBN_TAIKYO));
			// 入退居区分（hidden変数）
			comDto.setHdnNyutaikyoKbn(CodeConstant.NYUTAIKYO_KBN_TAIKYO);
		}

		// 社宅提示ステータス
		if (!CheckUtils.isEmpty(teijiData.getShatakuTeijiStatus())) {
			comDto.setSc006ShatakuStts(codeCacheUtils.getGenericCodeName(
					FunctionIdConstant.GENERIC_CODE_SHATAKU_TEIJI_STATUS_KBN, teijiData.getShatakuTeijiStatus()));
			// 社宅提示ステータスの文字色を設定する
			comDto.setSc006ShatakuSttsColor(setShatakuTeijiStatusCss(teijiData.getShatakuTeijiStatus()));
		}
		// 備品提示スタータス
		if (!CheckUtils.isEmpty(teijiData.getBihinTeijiStatus())) {
			comDto.setSc006BihinStts(codeCacheUtils.getGenericCodeName(
					FunctionIdConstant.GENERIC_CODE_BIHINTEIJISTATUS_KBN, teijiData.getBihinTeijiStatus()));
			// 備品提示ステータスの文字色を設定する
			comDto.setSc006BihinSttsColor(setBihinTeijiStatusCss(teijiData.getBihinTeijiStatus()));
		}
		// 原籍会社名
		if (!CheckUtils.isEmpty(teijiData.getOriginalCompanyCd())) {
			comDto.setSc006OldKaisyaNameSelect(teijiData.getOriginalCompanyCd());
		}
		// 給与支給会社
		if (!CheckUtils.isEmpty(teijiData.getPayCompanyCd())) {
			comDto.setSc006KyuyoKaisyaSelect(teijiData.getPayCompanyCd());
		}
		// 入居予定日
		comDto.setSc006NyukyoYoteiDay(comDto.getHdnNyukyoDate());
		// 退居予定日
		comDto.setSc006TaikyoYoteiDay(comDto.getHdnTaikyoDate());
		// 居住者区分
		if (!CheckUtils.isEmpty(teijiData.getKyojushaKbn())) {
			comDto.setSc006KyojyusyaKbnSelect(teijiData.getKyojushaKbn());
		}
		// 貸与用途
		if (!CheckUtils.isEmpty(teijiData.getAuse())) {
			comDto.setSc006TaiyoYouto(codeCacheUtils.getGenericCodeName(
					FunctionIdConstant.GENERIC_CODE_AUSE_KBN, teijiData.getAuse()));
		}
		// '貸与規格
		if (!CheckUtils.isEmpty(teijiData.getKikaku())) {
			comDto.setSc006TaiyoKikaku(codeCacheUtils.getGenericCodeName(
					FunctionIdConstant.GENERIC_CODE_KIKAKU_KBN, teijiData.getKikaku()));
			// "その他"の場合は規格に規格（補足）を設定
			if (Skf3022Sc006CommonDto.OTHER.equals(comDto.getSc006TaiyoKikaku())
					&& !CheckUtils.isEmpty(teijiData.getKikakuHosoku())) {
				comDto.setSc006TaiyoKikaku(teijiData.getKikakuHosoku());
			}
		}
		// 役員算定
		if (!CheckUtils.isEmpty(teijiData.getYakuinSannteiKbn())) {
			comDto.setSc006YakuinSanteiSelect(teijiData.getYakuinSannteiKbn());
		}
//		// 寒冷地減免
//        If Not dt(0).IsKANREITI_FLGNull Then
//            Me.lblKanreiti.Text = Me.GetDispValue(dt(0).KANREITI_FLG, _
//                                                  Constant.SettingsId.KANREITI_ADJUST_KBN, _
//                                                  Constant.SettingsId.XML_COM_CLASS)
//            Me.hdnKanreiti.Value = dt(0).KYOSYO_FLG
//        End If
//        '狭小減免
//        If Not dt(0).IsKYOSYO_FLGNull Then
//            Me.lblKyosyo.Text = Me.GetDispValue(dt(0).KYOSYO_FLG, _
//                                                Constant.SettingsId.KYOSHO_ADJUST_KBN, _
//                                                Constant.SettingsId.XML_COM_CLASS)
//            Me.hdnKyosyo.Value = dt(0).KYOSYO_FLG
//        End If
		// 社宅使用料月額
		if (teijiData.getRentalMonth() != null) {
			comDto.setSc006ShiyoryoTsukigaku(getKanmaNumEdit(teijiData.getRentalMonth().toString()));
		} else {
			comDto.setSc006ShiyoryoTsukigaku(CodeConstant.STRING_ZERO);
		}
		// 社宅使用料日割金額
		if (teijiData.getRentalDay() != null) {
			comDto.setSc006SiyoryoHiwariPay(getKanmaNumEdit(teijiData.getRentalDay().toString()));
		} else {
			comDto.setSc006SiyoryoHiwariPay(CodeConstant.STRING_ZERO);
		}
		// 社宅使用料調整金額
		if (teijiData.getRentalAdjust() != null) {
			comDto.setSc006SiyoroTyoseiPay(teijiData.getRentalAdjust().toString());
		} else {
			comDto.setSc006SiyoroTyoseiPay(CodeConstant.STRING_ZERO);
		}
		// 社宅使用料月額（調整後）
		if (teijiData.getRentalDay() != null && teijiData.getRentalAdjust() != null) {
			comDto.setSc006SyatauMonthPayAfter(
					getKanmaNumEdit(Long.toString(teijiData.getRentalDay() + teijiData.getRentalAdjust())));
		} else {
			comDto.setSc006SyatauMonthPayAfter(CodeConstant.STRING_ZERO);
		}
		// 個人負担金共益費協議中フラグ
		if (!CheckUtils.isEmpty(teijiData.getKyoekihiPersonKyogichuFlg())
				&& !CodeConstant.STRING_ZERO.equals(teijiData.getKyoekihiPersonKyogichuFlg())) {
			// フラグが登録されていて0以外の場合はtrue
			comDto.setSc006KyoekihiKyogichuCheck(true);
		} else {
			comDto.setSc006KyoekihiKyogichuCheck(false);
		}
		// 個人負担共益費月額
		if (teijiData.getKyoekihiPerson() != null) {
			comDto.setSc006KyoekihiMonthPay(teijiData.getKyoekihiPerson().toString());
		} else {
			comDto.setSc006KyoekihiMonthPay(CodeConstant.STRING_ZERO);
		}
		// 個人負担共益費調整金額
		if (teijiData.getKyoekihiPersonAdjust() != null) {
			comDto.setSc006KyoekihiTyoseiPay(teijiData.getKyoekihiPersonAdjust().toString());
		} else {
			comDto.setSc006KyoekihiTyoseiPay(CodeConstant.STRING_ZERO);
		}
		// 個人負担共益費月額（調整後）
		if (teijiData.getKyoekihiPerson() != null && teijiData.getKyoekihiPersonAdjust() != null) {
			comDto.setSc006KyoekihiPayAfter(
					getKanmaNumEdit(Long.toString(teijiData.getKyoekihiPerson() + teijiData.getKyoekihiPersonAdjust())));
		} else {
			comDto.setSc006KyoekihiPayAfter(CodeConstant.STRING_ZERO);
		}
		// 共益費支払月
		if (!CheckUtils.isEmpty(teijiData.getKyoekihiPayMonth())) {
			comDto.setSc006KyoekihiPayMonthSelect(teijiData.getKyoekihiPayMonth());
		}
		// 区画１ 区画番号
		if (!CheckUtils.isEmpty(teijiData.getParkingBlock1())) {
			comDto.setSc006KukakuNoOne(teijiData.getParkingBlock1());
		}
		// 区画１ 利用開始日
		if (!CheckUtils.isEmpty(teijiData.getParking1StartDate())) {
			comDto.setSc006RiyouStartDayOne(teijiData.getParking1StartDate());
		}
		// 区画１ 利用終了日
		if (!CheckUtils.isEmpty(teijiData.getParking1EndDate())) {
			comDto.setSc006RiyouEndDayOne(teijiData.getParking1EndDate());
		}
		// 区画１ 駐車場使用料月額
		if (teijiData.getParking1RentalMonth() != null) {
			comDto.setSc006TyusyaMonthPayOne(getKanmaNumEdit(teijiData.getParking1RentalMonth().toString()));
		} else {
			comDto.setSc006TyusyaMonthPayOne(CodeConstant.STRING_ZERO);
		}
		// 区画１ 駐車場使用料日割金額
		if (teijiData.getParking1RentalDay() != null) {
			comDto.setSc006TyusyaDayPayOne(getKanmaNumEdit(teijiData.getParking1RentalDay().toString()));
		} else {
			comDto.setSc006TyusyaDayPayOne(CodeConstant.STRING_ZERO);
		}
		// 区画２ 区画番号
		if (!CheckUtils.isEmpty(teijiData.getParkingBlock2())) {
			comDto.setSc006KukakuNoTwo(teijiData.getParkingBlock2());
		}
		// 区画２ 利用開始日
		if (!CheckUtils.isEmpty(teijiData.getParking2StartDate())) {
			comDto.setSc006RiyouStartDayTwo(teijiData.getParking2StartDate());
		}
		// 区画２ 利用終了日
		if (!CheckUtils.isEmpty(teijiData.getParking2EndDate())) {
			comDto.setSc006RiyouEndDayTwo(teijiData.getParking2EndDate());
		}
		// 区画２ 駐車場使用料月額
		if (teijiData.getParking2RentalMonth() != null) {
			comDto.setSc006TyusyaMonthPayTwo(getKanmaNumEdit(teijiData.getParking2RentalMonth().toString()));
		} else {
			comDto.setSc006TyusyaMonthPayTwo(CodeConstant.STRING_ZERO);
		}
		// 区画２ 駐車場使用料日割金額
		if (teijiData.getParking2RentalDay() != null) {
			comDto.setSc006TyusyaDayPayTwo(getKanmaNumEdit(teijiData.getParking2RentalDay().toString()));
		} else {
			comDto.setSc006TyusyaDayPayTwo(CodeConstant.STRING_ZERO);
		}
		// 駐車場使用料調整金額
		if (teijiData.getParkingRentalAdjust() != null) {
			comDto.setSc006TyusyaTyoseiPay(teijiData.getParkingRentalAdjust().toString());
		} else {
			comDto.setSc006TyusyaTyoseiPay(CodeConstant.STRING_ZERO);
		}
		// 駐車場使用料月額（調整後）
		if (teijiData.getParking1RentalDay() != null
				&& teijiData.getParking2RentalDay() != null && teijiData.getParkingRentalAdjust() != null) {
			// 区画1 駐車場使用料日割金額 + 区画2 駐車場使用料日割金額 + 駐車場使用料調整金額
			comDto.setSc006TyusyaMonthPayAfter(getKanmaNumEdit(Long.toString(
					teijiData.getParking1RentalDay() + teijiData.getParking2RentalDay() + teijiData.getParkingRentalAdjust())));
		} else {
			comDto.setSc006TyusyaMonthPayAfter(CodeConstant.STRING_ZERO);
		}
		// 社宅備考
		if (!CheckUtils.isEmpty(teijiData.getBiko())) {
			comDto.setSc006Bicou(teijiData.getBiko());
		}
		// 貸与日 
		if (!CheckUtils.isEmpty(teijiData.getEquipmentStartDate())) {
			comDto.setSc006TaiyoDay(teijiData.getEquipmentStartDate());
		}
		// 返却日
		if (!CheckUtils.isEmpty(teijiData.getEquipmentEndDate())) {
			comDto.setSc006HenkyakuDay(teijiData.getEquipmentEndDate());
		}
		// 搬入 希望日
		if (!CheckUtils.isEmpty(teijiData.getCarryinRequestDay())) {
			comDto.setSc006KibouDayIn(teijiData.getCarryinRequestDay());
		}
		// 搬入 希望日時時間帯
		if (!CheckUtils.isEmpty(teijiData.getCarryinRequestKbn())) {
			comDto.setSc006KibouTimeInSelect(teijiData.getCarryinRequestKbn());
		}
		// 搬入 本人連絡先
		if (!CheckUtils.isEmpty(teijiData.getUkeireMyApoint())) {
			comDto.setSc006HonninAddrIn(teijiData.getUkeireMyApoint());
		}
		// 搬入 受取代理人
		if (!CheckUtils.isEmpty(teijiData.getUkeireDairiName())) {
			comDto.setSc006UketoriDairiInName(teijiData.getUkeireDairiName());
		}
		// 搬入 受取代理人連絡先
		if (!CheckUtils.isEmpty(teijiData.getUkeireDairiApoint())) {
			comDto.setSc006UketoriDairiAddr(teijiData.getUkeireDairiApoint());
		}
		// 搬出 希望日
		if (!CheckUtils.isEmpty(teijiData.getCarryoutRequestDay())) {
			comDto.setSc006KibouDayOut(teijiData.getCarryoutRequestDay());
		}
		// 搬出 希望日時時間帯 
		if (!CheckUtils.isEmpty(teijiData.getCarryoutRequestKbn())) {
			comDto.setSc006KibouTimeOutSelect(teijiData.getCarryoutRequestKbn());
		}
		// 搬出 本人連絡先
		if (!CheckUtils.isEmpty(teijiData.getTatiaiMyApoint())) {
			comDto.setSc006HonninAddrOut(teijiData.getTatiaiMyApoint());
		}
		// 搬出 立会代理人
		if (!CheckUtils.isEmpty(teijiData.getTatiaiDairiName())) {
			comDto.setSc006TachiaiDairi(teijiData.getTatiaiDairiName());
		}
		// 搬出 立会代理人連絡先
		if (!CheckUtils.isEmpty(teijiData.getTatiaiDairiApoint())) {
			comDto.setSc006TachiaiDairiAddr(teijiData.getTatiaiDairiApoint());
		}
		// 代理人備考
		if (!CheckUtils.isEmpty(teijiData.getDairiKiko())) {
			comDto.setSc006DairiBiko(teijiData.getDairiKiko());
		}
		// 備品備考
		if (!CheckUtils.isEmpty(teijiData.getBihinBiko())) {
			comDto.setSc006BihinBiko(teijiData.getBihinBiko());
		}
		// 管理会社
		if (!CheckUtils.isEmpty(teijiData.getCompanyName())) {
			comDto.setSc006KanriKaisya(teijiData.getCompanyName());
		}
		// 貸付会社
		if (!CheckUtils.isEmpty(teijiData.getKashitukeCompanyCd())) {
			comDto.setSc006TaiyoKaisyaSelect(teijiData.getKashitukeCompanyCd());
		}
		// 借受会社
		if (!CheckUtils.isEmpty(teijiData.getKariukeCompanyCd())) {
			comDto.setSc006KariukeKaisyaSelect(teijiData.getKariukeCompanyCd());
		}
		// 出向の有無(相互利用状況)
		if (!CheckUtils.isEmpty(teijiData.getMutualJokyo())) {
			comDto.setSc006SogoRyojokyoSelect(teijiData.getMutualJokyo());
		}
		// 相互利用判定区分 
		if (!CheckUtils.isEmpty(teijiData.getMutualUseKbn())) {
			comDto.setSc006SogoHanteiKbnSelect(teijiData.getMutualUseKbn());
		}
		// 会社間送金区分（社宅使用料）
		if (!CheckUtils.isEmpty(teijiData.getShatakuCompanyTransferKbn())) {
			comDto.setSc006SokinShatakuSelect(teijiData.getShatakuCompanyTransferKbn());
		}
		// 会社間送金区分（共益費）
		if (!CheckUtils.isEmpty(teijiData.getKyoekihiCompanyTransferKbn())) {
			comDto.setSc006SokinKyoekihiSelect(teijiData.getKyoekihiCompanyTransferKbn());
		}
		// 社宅賃貸料
		if (teijiData.getRent() != null) {
			comDto.setSc006ChintaiRyo(teijiData.getRent().toString());
		} else {
			comDto.setSc006ChintaiRyo(CodeConstant.STRING_ZERO);
		}
		// 駐車場料金
		if (teijiData.getParkingRental() != null) {
			comDto.setSc006TyusyajoRyokin(teijiData.getParkingRental().toString());
		} else {
			comDto.setSc006TyusyajoRyokin(CodeConstant.STRING_ZERO);
		}
		// 共益費（事業者負担）
		if (teijiData.getKyoekihiBusiness() != null) {
			comDto.setSc006Kyoekihi(teijiData.getKyoekihiBusiness().toString());
		} else {
			comDto.setSc006Kyoekihi(CodeConstant.STRING_ZERO);
		}
		// 開始日
		if (!CheckUtils.isEmpty(teijiData.getMutualUseStartDay())) {
			comDto.setSc006StartDay(teijiData.getMutualUseStartDay());
		}
		// 終了日
		if (!CheckUtils.isEmpty(teijiData.getMutualUseEndDay())) {
			comDto.setSc006EndDay(teijiData.getMutualUseEndDay());
		}
		// 配属会社名
		if (!CheckUtils.isEmpty(teijiData.getAssignCompanyCd())) {
			comDto.setSc006HaizokuKaisyaSelect(teijiData.getAssignCompanyCd());
		}
		// 所属機関
		if (!CheckUtils.isEmpty(teijiData.getAgency())) {
			comDto.setSc006SyozokuKikan(teijiData.getAgency());
		}
		// 室・部名
		if (!CheckUtils.isEmpty(teijiData.getAffiliation1())) {
			comDto.setSc006SituBuName(teijiData.getAffiliation1());
		}
		// 課等名
		if (!CheckUtils.isEmpty(teijiData.getAffiliation2())) {
			comDto.setSc006KanadoMei(teijiData.getAffiliation2());
		}
		// 配属データコード番号
		if (!CheckUtils.isEmpty(teijiData.getAssignCd())) {
			comDto.setSc006HaizokuNo(teijiData.getAssignCd());
		}
		/** 入退居予定データ更新日 */
		// 入退居予定データを取得
		List<Skf3022Sc006GetNyutaikyoYoteiDataExp> nyutaikyoYoteiDataList = new ArrayList<Skf3022Sc006GetNyutaikyoYoteiDataExp>();
		Skf3022Sc006GetNyutaikyoYoteiDataExpParameter nyutaikyoParam = new Skf3022Sc006GetNyutaikyoYoteiDataExpParameter();
		nyutaikyoParam.setShainNo(comDto.getSc006ShainNo().replace("＊", "").replace("*", ""));
		nyutaikyoParam.setNyutaikyoKbn(comDto.getHdnNyutaikyoKbn());
		nyutaikyoYoteiDataList = skf3022Sc006GetNyutaikyoYoteiDataExpRepository.getNyutaikyoYoteiData(nyutaikyoParam);
		// 取得結果判定
		if (nyutaikyoYoteiDataList.size() > 0) {
			// 入退居予定データ更新日設定
			comDto.setHdnNyutaikyoYoteiUpdateDate(dateFormat.format(nyutaikyoYoteiDataList.get(0).getUpdateDate()));
		}
		/** 社宅部屋情報マスタ更新日 */
		Long shatakuKanriNo = CheckUtils.isEmpty(comDto.getHdnShatakuKanriNo()) ? null : Long.parseLong(comDto.getHdnShatakuKanriNo());
		Long shatakuRoomKanriNo = CheckUtils.isEmpty(comDto.getHdnRoomKanriNo()) ? null : Long.parseLong(comDto.getHdnRoomKanriNo());
		List<Skf3022Sc006GetShatakuRoomExp> shatakuRoomList = new ArrayList<Skf3022Sc006GetShatakuRoomExp>();
		Skf3022Sc006GetShatakuRoomExpParameter shatakuRoomParam = new Skf3022Sc006GetShatakuRoomExpParameter();
		shatakuRoomParam.setShatakuKanriNo(shatakuKanriNo);
		shatakuRoomParam.setShatakuRoomKanriNo(shatakuRoomKanriNo);
		if (shatakuKanriNo != null && shatakuRoomKanriNo != null) {
			shatakuRoomList = skf3022Sc006GetShatakuRoomExpRepository.getShatakuRoom(shatakuRoomParam);
		}
		// 取得結果判定
		if (shatakuRoomList.size() > 0) {
			// 社宅部屋情報マスタ更新日設定
			comDto.setHdnShatakuRoomUpdateDate(dateFormat.format(shatakuRoomList.get(0).getUpdateDate()));
		}
		/** 社宅部屋情報マスタ(区画1)更新日 */
		// 社宅駐車場区画情報マスタ（区画１）を取得
		List<Skf3022Sc006GetShatakuParkingBlockExp> parkingBlockList = new ArrayList<Skf3022Sc006GetShatakuParkingBlockExp>();
		Skf3022Sc006GetShatakuParkingBlockExpParameter parkingBlockParam = new Skf3022Sc006GetShatakuParkingBlockExpParameter();
		// 駐車場(区画1)管理番号
		Long parkingKanriNo1 = CheckUtils.isEmpty(comDto.getHdnChushajoNoOne()) ? null : Long.parseLong(comDto.getHdnChushajoNoOne());
		if (shatakuKanriNo != null && parkingKanriNo1 != null) {
			parkingBlockParam.setShatakuKanriNo(shatakuKanriNo);
			parkingBlockParam.setParkingKanriNo(parkingKanriNo1);
			parkingBlockList = skf3022Sc006GetShatakuParkingBlockExpRepository.getShatakuParkingBlock(parkingBlockParam);
		}
		// 結果判定
		if (parkingBlockList.size() > 0) {
			// 社宅部屋情報マスタ(区画1)更新日設定
			comDto.setHdnShatakuParkingBlock1UpdateDate(dateFormat.format(parkingBlockList.get(0).getUpdateDate()));
		}
		parkingBlockList.clear();
		/** 社宅部屋情報マスタ(区画2)更新日 */
		// 社宅駐車場区画情報マスタ（区画２）を取得
		// 駐車場(区画2)管理番号
		Long parkingKanriNo2 = CheckUtils.isEmpty(comDto.getHdnChushajoNoTwo()) ? null : Long.parseLong(comDto.getHdnChushajoNoTwo());
		if (shatakuKanriNo != null && parkingKanriNo2 != null) {
			parkingBlockParam.setParkingKanriNo(parkingKanriNo2);
			parkingBlockList = skf3022Sc006GetShatakuParkingBlockExpRepository.getShatakuParkingBlock(parkingBlockParam);
		}
		// 結果判定
		if (parkingBlockList.size() > 0) {
			// 社宅部屋情報マスタ(区画2)更新日設定
			comDto.setHdnShatakuParkingBlock2UpdateDate(dateFormat.format(parkingBlockList.get(0).getUpdateDate()));
		}
		parkingBlockList.clear();
		/** 初期表示メッセージボックスの設定 */
		// 変更
		if (CodeConstant.NYUTAIKYO_KBN_HENKO.equals(comDto.getHdnNyutaikyoKbnOld())) {
			// 申請なしの場合
			if (CheckUtils.isEmpty(comDto.getHdnShoruikanriNo())) {
				LogUtils.debugByMsg("変更、申請無し");
				// I_SKF_3087メッセージ設定(入居状態を変更する必要があります。退居情報を設定に続いて入居情報を登録してください。)
				setMsgBox(PropertyUtils.getValue(MessageIdConstant.I_SKF_3087), comDto);
			// 申請ありの場合
			} else {
				LogUtils.debugByMsg("変更、申請あり");
				// I_SKF_3088メッセージ設定(社宅が継続利用されています。居住状況を変更してください。)
				setMsgBox(PropertyUtils.getValue(MessageIdConstant.I_SKF_3088), comDto);
			}
		// 退居
		} else if (CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(comDto.getHdnNyutaikyoKbnOld())) {
			// 入居の提示データを取得
			List<Skf3022Sc006GetSameRoomTeijiInfoExp> sameRoomTeijiList = new ArrayList<Skf3022Sc006GetSameRoomTeijiInfoExp>();
			Skf3022Sc006GetSameRoomTeijiInfoExpParameter sameRoomParam = new Skf3022Sc006GetSameRoomTeijiInfoExpParameter();
			Long shatakuKanriNoOld = CheckUtils.isEmpty(comDto.getHdnShatakuKanriNoOld()) ? null : Long.parseLong(comDto.getHdnShatakuKanriNoOld());
			Long shatakuRoomKanriNoOld = CheckUtils.isEmpty(comDto.getHdnRoomKanriNoOld()) ? null : Long.parseLong(comDto.getHdnRoomKanriNoOld());
			sameRoomParam.setShatakuKanriNo(shatakuKanriNoOld);
			sameRoomParam.setShatakuRoomKanriNo(shatakuRoomKanriNoOld);
			sameRoomParam.setNyutaikyoKbn(CodeConstant.NYUTAIKYO_KBN_NYUKYO);
			if (shatakuKanriNoOld != null && shatakuRoomKanriNoOld != null) {
				sameRoomTeijiList = skf3022Sc006GetSameRoomTeijiInfoExpRepository.getSameRoomTeijiInfo(sameRoomParam);
			}
			// 結果判定
			if (sameRoomTeijiList.size() > 0) {
				LogUtils.debugByMsg("退居、入居提示データあり");
				// I_SKF_3085メッセージ設定
				setMsgBox(MessageFormat.format(
						PropertyUtils.getValue(MessageIdConstant.I_SKF_3085),
						sameRoomTeijiList.get(0).getShainNo(),
						sameRoomTeijiList.get(0).getShainName()), comDto);
			} else {
				LogUtils.debugByMsg("退居、入居提示データなし");
				// メッセージボックス非表示
				setMsgBox(null, comDto);
			}
		// 入居
		} else {
			// 退居の提示データを取得
			List<Skf3022Sc006GetSameRoomTeijiInfoExp> sameRoomTeijiList = new ArrayList<Skf3022Sc006GetSameRoomTeijiInfoExp>();
			Skf3022Sc006GetSameRoomTeijiInfoExpParameter sameRoomParam = new Skf3022Sc006GetSameRoomTeijiInfoExpParameter();
			Long shatakuKanriNoOld = CheckUtils.isEmpty(comDto.getHdnShatakuKanriNoOld()) ? null : Long.parseLong(comDto.getHdnShatakuKanriNoOld());
			Long shatakuRoomKanriNoOld = CheckUtils.isEmpty(comDto.getHdnRoomKanriNoOld()) ? null : Long.parseLong(comDto.getHdnRoomKanriNoOld());
			sameRoomParam.setShatakuKanriNo(shatakuKanriNoOld);
			sameRoomParam.setShatakuRoomKanriNo(shatakuRoomKanriNoOld);
			sameRoomParam.setNyutaikyoKbn(CodeConstant.NYUTAIKYO_KBN_TAIKYO);
			if (shatakuKanriNoOld != null && shatakuRoomKanriNoOld != null) {
				sameRoomTeijiList = skf3022Sc006GetSameRoomTeijiInfoExpRepository.getSameRoomTeijiInfo(sameRoomParam);
			}
			// 結果判定
			if (sameRoomTeijiList.size() > 0) {
				LogUtils.debugByMsg("入居、退居提示データあり");
				// I_SKF_3086メッセージ設定
				setMsgBox(MessageFormat.format(
						PropertyUtils.getValue(MessageIdConstant.I_SKF_3086),
						sameRoomTeijiList.get(0).getShainNo(),
						sameRoomTeijiList.get(0).getShainName()), comDto);
				// 備品提示ステータスを判断する
				if (CodeConstant.BIHIN_STATUS_HANSHUTSU_MACHI.equals(sameRoomTeijiList.get(0).getBihinTeijiStatus())
						|| CodeConstant.BIHIN_STATUS_HANNYU_SUMI.equals(sameRoomTeijiList.get(0).getBihinTeijiStatus())
						|| CodeConstant.BIHIN_STATUS_SHONIN.equals(sameRoomTeijiList.get(0).getBihinTeijiStatus())) {
					// 備品搬出待ちフラグをtrue
					LogUtils.debugByMsg("備品搬出待ちフラグをtrue");
					comDto.setHdnBihinMoveOutFlg(true);
				}
			} else {
				// 同じ駐車場区画１の提示データ情報を取得
				List<Skf3022Sc006GetSameParkingBlockTeijiInfoExp> sameParkingBlockList = new ArrayList<Skf3022Sc006GetSameParkingBlockTeijiInfoExp>();
				Skf3022Sc006GetSameParkingBlockTeijiInfoExpParameter param = new Skf3022Sc006GetSameParkingBlockTeijiInfoExpParameter();
				Long parkingKanriNoOld1 = CheckUtils.isEmpty(comDto.getHdnChushajoNoOneOld()) ? null : Long.parseLong(comDto.getHdnChushajoNoOneOld());
				param.setNyutaikyoKbn(CodeConstant.NYUTAIKYO_KBN_TAIKYO);
				param.setShatakuKanriNo(shatakuKanriNoOld);
				param.setParkingKanriNo(parkingKanriNoOld1);
				if (shatakuKanriNoOld != null && parkingKanriNoOld1 != null) {
					sameParkingBlockList = skf3022Sc006GetSameParkingBlockTeijiInfoExpRepository.getSameParkingBlockTeijiInfo(param);
				}
				// 結果判定
				if (sameParkingBlockList.size() > 0) {
					LogUtils.debugByMsg("入居、退居提示データなし、区画1駐車場利用者あり");
					// I_SKF_3092メッセージ設定
					setMsgBox(MessageFormat.format(
							PropertyUtils.getValue(MessageIdConstant.I_SKF_3092),
							sameParkingBlockList.get(0).getShainNo(),
							sameParkingBlockList.get(0).getShainName()), comDto);
				} else {
					sameParkingBlockList.clear();
					// 同じ駐車場区画２の提示データ情報を取得
					Long parkingKanriNoOld2 = CheckUtils.isEmpty(comDto.getHdnChushajoNoTwoOld()) ? null : Long.parseLong(comDto.getHdnChushajoNoTwoOld());
					param.setParkingKanriNo(parkingKanriNoOld2);
					if (shatakuKanriNoOld != null && parkingKanriNoOld2 != null) {
						sameParkingBlockList = skf3022Sc006GetSameParkingBlockTeijiInfoExpRepository.getSameParkingBlockTeijiInfo(param);
					}
					// 結果判定
					if (sameParkingBlockList.size() > 0) {
						LogUtils.debugByMsg("入居、退居提示データなし、区画2駐車場利用者あり");
						// I_SKF_3092メッセージ設定
						setMsgBox(MessageFormat.format(
								PropertyUtils.getValue(MessageIdConstant.I_SKF_3092),
								sameParkingBlockList.get(0).getShainNo(),
								sameParkingBlockList.get(0).getShainName()), comDto);
					} else {
						LogUtils.debugByMsg("入居、退居提示データなし、駐車場利用者なし");
						// メッセージボックス非表示
						setMsgBox(null, comDto);
					}
				}
			}
		}
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
		sc006KyojyusyaKbnSelectList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_KYOJUSHA_KBN, sc006KyojyusyaKbnSelect, false));
		// 役員算定
		sc006YakuinSanteiSelectList.clear();
		sc006YakuinSanteiSelectList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_YAKUIN_KBN, sc006YakuinSanteiSelect, false));
		// 共益費支払月
		sc006KyoekihiPayMonthSelectList.clear();
		sc006KyoekihiPayMonthSelectList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_KYOEKIHI_PAY_MONTH_KBN, sc006KyoekihiPayMonthSelect, true));
		// 搬入希望日時時間帯
		sc006KibouTimeInSelectList.clear();
		sc006KibouTimeInSelectList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_REQUESTTIME_KBN, sc006KibouTimeInSelect, true));
		// 搬出希望日時時間帯
		sc006KibouTimeOutSelectList.clear();
		sc006KibouTimeOutSelectList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_REQUESTTIME_KBN, sc006KibouTimeOutSelect, true));
		// 出向の有無(相互利用状況)
		sc006SogoRyojokyoSelectList.clear();
		sc006SogoRyojokyoSelectList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_MUTUAL_USE_JOKYO, sc006SogoRyojokyoSelect, false));
		// 相互利用判定区分
		sc006SogoHanteiKbnSelectList.clear();
		sc006SogoHanteiKbnSelectList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_MUTUALUSE_KBN, sc006SogoHanteiKbnSelect, true));
		// 会社間送金区分（社宅使用料）
		sc006SokinShatakuSelectList.clear();
		sc006SokinShatakuSelectList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_COMPANY_TRANSFER_KBN, sc006SokinShatakuSelect, true));
		// 会社間送金区分（共益費）
		sc006SokinKyoekihiSelectList.clear();
		sc006SokinKyoekihiSelectList.addAll(ddlUtils.getGenericForDoropDownList(
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

//    ''' <summary>
//    ''' 画面項目制御の設定
//    ''' </summary>
//    ''' <remarks></remarks>
//    Private Sub SetControlStatus()
	private void setControlStatus(List<Skf3022Sc006GetTeijiBihinDataExp> roomBihinList, Skf3022Sc006CommonDto comDto) {

		// 各タブの初期ステータス
		comDto.setShatakuTabStatus(true);
		comDto.setBihinTabStatus(true);
		comDto.setSogoTabStatu(true);
		// 備品貸与区分ドロップダウン
		List<Map<String, Object>> lendStatusList =
				ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_BIHINLENTSTATUS_KBN, "",false);
		// 備品提示ステータス
		String bihinTeijiStatus = comDto.getHdnBihinTeijiStatus() != null ? comDto.getHdnBihinTeijiStatus() : "";
		// 社宅提示状況区分が"同意済み"、備品貸与区分が"必要"の場合
		if (CodeConstant.PRESENTATION_SITUATION_DOI_SUMI.equals(comDto.getHdnShatakuTeijiStatus())
				&& CodeConstant.BIHIN_TAIYO_KBN_HITSUYO.equals(comDto.getHdnBihinTaiyoKbn())) {
			// 個人負担共益費調整額にチェックがある場合
			if (comDto.getSc006KyoekihiKyogichuCheck()) {
				// 初期のタブを"社宅情報タブ"に設定
				setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_SHATAKU, comDto);
			} else if(CodeConstant.BIHIN_STATUS_MI_SAKUSEI.equals(bihinTeijiStatus)) {
				// 備品提示状況区分が未申請の場合
				// 初期のタブを"社宅情報タブ"に設定
				setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_SHATAKU, comDto);
			} else {
				// 初期のタブを"備品タブ"に設定
				setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_BIHIN, comDto);
			}
		}
		// 社宅提示状況区分が"承認"、備品貸与区分が"必要"の場合
		if (CodeConstant.PRESENTATION_SITUATION_SHONIN.equals(comDto.getHdnShatakuTeijiStatus())
				&& CodeConstant.BIHIN_TAIYO_KBN_HITSUYO.equals(comDto.getHdnBihinTaiyoKbn())) {
			// 初期のタブを"備品タブ"に設定
			setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_BIHIN, comDto);
		}
		// 入退居区分が"退居"、備品貸与区分が"必要"、社宅提示状況区分が"作成済／同意済／承認"の場合
		if (CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(comDto.getHdnNyutaikyoKbn())
				&& CodeConstant.BIHIN_TAIYO_KBN_HITSUYO.equals(comDto.getHdnBihinTaiyoKbn())
				&& (CodeConstant.PRESENTATION_SITUATION_SAKUSEI_SUMI.equals(comDto.getHdnShatakuTeijiStatus())
					|| CodeConstant.PRESENTATION_SITUATION_DOI_SUMI.equals(comDto.getHdnShatakuTeijiStatus())
					|| CodeConstant.PRESENTATION_SITUATION_SHONIN.equals(comDto.getHdnShatakuTeijiStatus()))) {
			// 初期のタブを"備品タブ"に設定
			setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_BIHIN, comDto);
		}
		// 申請が"あり"の場合
		if (!CheckUtils.isEmpty(comDto.getHdnShoruikanriNo())) {
			// 入退居区分が"入居"の場合
			if (CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(comDto.getHdnNyutaikyoKbnOld())) {
				// 駐車場のみ申請の場合
				if (CodeConstant.SHINSEI_KBN_PARKING.equals(comDto.getHdnApplKbn())) {
					// 社宅提示ステータス判定
					if (!CheckUtils.isEmpty(comDto.getHdnShatakuTeijiStatus())) {
						// 社宅提示ステータス判定
						switch (comDto.getHdnShatakuTeijiStatus()) {
						// 作成中/作成済
						case CodeConstant.PRESENTATION_SITUATION_SAKUSEI_CHU:
						case CodeConstant.PRESENTATION_SITUATION_SAKUSEI_SUMI:
							LogUtils.debugByMsg("申請あり、入退居区分：入居、申請区分：駐車場、社宅提示：作成中/作成済");
							// 各タブの初期ステータス
							comDto.setShatakuTabStatus(true);
							comDto.setBihinTabStatus(false);
							comDto.setSogoTabStatu(false);
							// 運用ガイド
							comDto.setBtnUnyonGuideDisableFlg(false);
							// 一時保存
							comDto.setBtnTmpSaveDisableFlg(false);
							// 作成完了
							comDto.setBtnCreateDisableFlg(false);
							// 次月予約
							comDto.setBtnJigetuYoyakuDisableFlg(false);
							// 台帳登録
							comDto.setBtnShatakuLoginDisableFlg(true);
							break;
						// 提示中/同意済/承認
						case CodeConstant.PRESENTATION_SITUATION_TEIJI_CHU:
						case CodeConstant.PRESENTATION_SITUATION_DOI_SUMI:
						case CodeConstant.PRESENTATION_SITUATION_SHONIN:
							LogUtils.debugByMsg("申請あり、入退居区分：入居、申請区分：駐車場、社宅提示：提示中/同意済/承認");
							// 各タブの初期ステータス
							comDto.setShatakuTabStatus(false);
							comDto.setBihinTabStatus(false);
							comDto.setSogoTabStatu(false);
							// 運用ガイド
							comDto.setBtnUnyonGuideDisableFlg(false);
							// 一時保存
							comDto.setBtnTmpSaveDisableFlg(true);
							// 作成完了
							comDto.setBtnCreateDisableFlg(true);
							// 次月予約
							comDto.setBtnJigetuYoyakuDisableFlg(true);
							// 台帳登録
							comDto.setBtnShatakuLoginDisableFlg(true);
							break;
						default :
							LogUtils.debugByMsg("申請あり、入退居区分：入居、申請区分：駐車場、社宅提示：(作成中/作成済/提示中/同意済/承認)以外");
							break;
						};
					} else {
						LogUtils.debugByMsg("申請あり、入退居区分：入居、申請区分：駐車場、社宅提示：null/空文字");
					}
				} else {
					// 社宅提示ステータス判定
					if (!CheckUtils.isEmpty(comDto.getHdnShatakuTeijiStatus())) {
						switch (comDto.getHdnShatakuTeijiStatus()) {
						// 作成中
						case CodeConstant.PRESENTATION_SITUATION_SAKUSEI_CHU:
							// 社宅未割当／使用料未割当の場合
							if (CheckUtils.isEmpty(comDto.getHdnShatakuKanriNo())
									|| CheckUtils.isEmpty(comDto.getHdnSiyouryoId())) {
								// 各タブの初期ステータス
								comDto.setShatakuTabStatus(false);
								comDto.setBihinTabStatus(false);
								comDto.setSogoTabStatu(false);
								// タブ切替不可:備品情報
								comDto.setTbpBihinInfo(true);
								// タブ切替不可:役員情報/相互利用
								comDto.setTbpSougoRiyouInfo(true);
								// 運用ガイド
								comDto.setBtnUnyonGuideDisableFlg(false);
								// 作成完了
								comDto.setBtnCreateDisableFlg(true);
								// 次月予約
								comDto.setBtnJigetuYoyakuDisableFlg(true);
								// 台帳登録
								comDto.setBtnShatakuLoginDisableFlg(true);
								// 一時保存
								if (!CheckUtils.isEmpty(comDto.getHdnShatakuKanriNo())) {
									comDto.setBtnTmpSaveDisableFlg(false);
									LogUtils.debugByMsg("申請あり、入退居区分：入居、申請区分：駐車場のみ以外、社宅提示：作成中、且つ、社宅未割当 /使用料未割当、社宅管理番号あり");
								} else {
									comDto.setBtnTmpSaveDisableFlg(true);
									LogUtils.debugByMsg("申請あり、入退居区分：入居、申請区分：駐車場のみ以外、社宅提示：作成中、且つ、社宅未割当 /使用料未割当、社宅管理番号なし");
								}
							} else {
								LogUtils.debugByMsg("申請あり、入退居区分：入居、申請区分：駐車場のみ以外、社宅提示：作成中、且つ、(社宅未割当 or 使用料未割当)以外");
								// 各タブの初期ステータス
								comDto.setShatakuTabStatus(true);
								comDto.setBihinTabStatus(false);
								comDto.setSogoTabStatu(true);
								// 運用ガイド
								comDto.setBtnUnyonGuideDisableFlg(false);
								// 一時保存
								comDto.setBtnTmpSaveDisableFlg(false);
								// 作成完了
								comDto.setBtnCreateDisableFlg(false);
								// 次月予約
								comDto.setBtnJigetuYoyakuDisableFlg(false);
								// 台帳登録
								comDto.setBtnShatakuLoginDisableFlg(true);
							}
							break;
						// 作成済
						case CodeConstant.PRESENTATION_SITUATION_SAKUSEI_SUMI:
							LogUtils.debugByMsg("申請あり、入退居区分：入居、申請区分：駐車場のみ以外、社宅提示：作成済");
							// 各タブの初期ステータス
							comDto.setShatakuTabStatus(true);
							comDto.setBihinTabStatus(false);
							comDto.setSogoTabStatu(true);
							// 運用ガイド
							comDto.setBtnUnyonGuideDisableFlg(false);
							// 一時保存
							comDto.setBtnTmpSaveDisableFlg(false);
							// 作成完了
							comDto.setBtnCreateDisableFlg(false);
							// 次月予約
							comDto.setBtnJigetuYoyakuDisableFlg(false);
							// 台帳登録
							comDto.setBtnShatakuLoginDisableFlg(true);
							break;
						// 提示中
						case CodeConstant.PRESENTATION_SITUATION_TEIJI_CHU:
							LogUtils.debugByMsg("申請あり、入退居区分：入居、申請区分：駐車場のみ以外、社宅提示：提示中");
							// 各タブの初期ステータス
							comDto.setShatakuTabStatus(false);
							comDto.setBihinTabStatus(false);
							comDto.setSogoTabStatu(false);
							// 提示中の場合は一時保存、台帳登録以外は活性
							// 運用ガイド
							comDto.setBtnUnyonGuideDisableFlg(false);
							// 一時保存
							comDto.setBtnTmpSaveDisableFlg(true);
							// 作成完了
							comDto.setBtnCreateDisableFlg(false);
							// 次月予約
							comDto.setBtnJigetuYoyakuDisableFlg(false);
							// 台帳登録
							comDto.setBtnShatakuLoginDisableFlg(true);
							// 作成完了ボタン押下時メッセージ
							// 編集した内容で更新します。よろしいですか？
							comDto.setLitMessageCreate(PropertyUtils.getValue(MessageIdConstant.I_SKF_3052));
							break;
						// 同意済
						case CodeConstant.PRESENTATION_SITUATION_DOI_SUMI:
							// 備品提示ステータス判定
							switch (bihinTeijiStatus) {
								// 貸与不要／未申請
								case CodeConstant.DOUBLE_QUOTATION:
								case CodeConstant.BIHIN_STATUS_MI_SAKUSEI:
									LogUtils.debugByMsg("申請あり、入退居区分：入居、申請区分：駐車場のみ以外、社宅提示：同意済、備品提示：貸与不要/未申請");
									// 各タブの初期ステータス
									comDto.setShatakuTabStatus(false);
									comDto.setBihinTabStatus(false);
									comDto.setSogoTabStatu(false);
									// 運用ガイド
									comDto.setBtnUnyonGuideDisableFlg(false);
									// 一時保存
									comDto.setBtnTmpSaveDisableFlg(true);
									// 作成完了
									comDto.setBtnCreateDisableFlg(false);
									// 次月予約
									comDto.setBtnJigetuYoyakuDisableFlg(false);
									// 台帳登録
									comDto.setBtnShatakuLoginDisableFlg(true);
									// 作成完了ボタン押下時メッセージ
									// 編集した内容で更新します。よろしいですか？
									comDto.setLitMessageCreate(PropertyUtils.getValue(MessageIdConstant.I_SKF_3052));
									break;
								// 作成中／作成済
								case CodeConstant.BIHIN_STATUS_SAKUSEI_CHU:
								case CodeConstant.BIHIN_STATUS_SAKUSEI_SUMI:
									LogUtils.debugByMsg("申請あり、入退居区分：入居、申請区分：駐車場のみ以外、社宅提示：同意済、備品提示：作成中/作成済");
									// 各タブの初期ステータス
									comDto.setShatakuTabStatus(false);
									comDto.setBihinTabStatus(true);
									comDto.setSogoTabStatu(false);
									// 運用ガイド
									comDto.setBtnUnyonGuideDisableFlg(false);
									// 一時保存
									comDto.setBtnTmpSaveDisableFlg(false);
									// 作成完了
									comDto.setBtnCreateDisableFlg(false);
									// 次月予約
									comDto.setBtnJigetuYoyakuDisableFlg(false);
									// 台帳登録
									comDto.setBtnShatakuLoginDisableFlg(true);
									break;
								// 搬入待／搬入済
								case CodeConstant.BIHIN_STATUS_HANNYU_MACHI:
								case CodeConstant.BIHIN_STATUS_HANNYU_SUMI:
									LogUtils.debugByMsg("申請あり、入退居区分：入居、申請区分：駐車場のみ以外、社宅提示：同意済、備品提示：搬入待/搬入済");
									// 各タブの初期ステータス
									comDto.setShatakuTabStatus(false);
									comDto.setBihinTabStatus(false);
									comDto.setSogoTabStatu(false);
									// 運用ガイド
									comDto.setBtnUnyonGuideDisableFlg(false);
									// 一時保存
									comDto.setBtnTmpSaveDisableFlg(true);
									// 作成完了
									comDto.setBtnCreateDisableFlg(false);
									// 次月予約
									comDto.setBtnJigetuYoyakuDisableFlg(false);
									// 台帳登録
									comDto.setBtnShatakuLoginDisableFlg(true);
									// 作成完了ボタン押下時メッセージ
									// 編集した内容で更新します。よろしいですか？
									comDto.setLitMessageCreate(PropertyUtils.getValue(MessageIdConstant.I_SKF_3052));
									break;
								default :
									LogUtils.debugByMsg("申請あり、入退居区分：入居、申請区分：駐車場のみ以外、社宅提示：同意済、備品提示：(貸与不要/未申請/作成中/作成済/搬入待/搬入済)以外");
									break;
							};
							break;
						// 承認
						case CodeConstant.PRESENTATION_SITUATION_SHONIN:
							// 備品提示ステータス判定
							switch (bihinTeijiStatus) {
								// 貸与不要/未申請
								case CodeConstant.DOUBLE_QUOTATION:
								case CodeConstant.BIHIN_STATUS_MI_SAKUSEI:
									LogUtils.debugByMsg("申請あり、入退居区分：入居、申請区分：駐車場のみ以外、社宅提示：承認、備品提示：貸与不要/未申請");
									// 各タブの初期ステータス
									comDto.setShatakuTabStatus(false);
									comDto.setBihinTabStatus(true);
									comDto.setSogoTabStatu(false);
									// 運用ガイド
									comDto.setBtnUnyonGuideDisableFlg(false);
									// 一時保存
									comDto.setBtnTmpSaveDisableFlg(true);
									// 作成完了
									comDto.setBtnCreateDisableFlg(true);
									// 次月予約
									comDto.setBtnJigetuYoyakuDisableFlg(true);
									// 台帳登録
									comDto.setBtnShatakuLoginDisableFlg(true);
									break;
								// 作成中/作成済
								case CodeConstant.BIHIN_STATUS_SAKUSEI_CHU:
								case CodeConstant.BIHIN_STATUS_SAKUSEI_SUMI:
									LogUtils.debugByMsg("申請あり、入退居区分：入居、申請区分：駐車場のみ以外、社宅提示：承認、備品提示：作成中/作成済");
									// 各タブの初期ステータス
									comDto.setShatakuTabStatus(false);
									comDto.setBihinTabStatus(true);
									comDto.setSogoTabStatu(false);
									// 運用ガイド
									comDto.setBtnUnyonGuideDisableFlg(false);
									// 一時保存
									comDto.setBtnTmpSaveDisableFlg(false);
									// 作成完了
									comDto.setBtnCreateDisableFlg(false);
									// 次月予約
									comDto.setBtnJigetuYoyakuDisableFlg(true);
									// 台帳登録
									comDto.setBtnShatakuLoginDisableFlg(true);
									break;
								// 搬入待ち／搬入済み
								case CodeConstant.BIHIN_STATUS_HANNYU_MACHI:
								case CodeConstant.BIHIN_STATUS_HANNYU_SUMI:
									LogUtils.debugByMsg("申請あり、入退居区分：入居、申請区分：駐車場のみ以外、社宅提示：承認、備品提示：搬入待/搬入済");
									// 各タブの初期ステータス
									comDto.setShatakuTabStatus(false);
									comDto.setBihinTabStatus(false);
									comDto.setSogoTabStatu(false);
									// 運用ガイド
									comDto.setBtnUnyonGuideDisableFlg(false);
									// 一時保存
									comDto.setBtnTmpSaveDisableFlg(true);
									// 作成完了
									comDto.setBtnCreateDisableFlg(true);
									// 次月予約
									comDto.setBtnJigetuYoyakuDisableFlg(true);
									// 台帳登録
									comDto.setBtnShatakuLoginDisableFlg(true);
									break;
								default :
									LogUtils.debugByMsg("申請あり、入退居区分：入居、申請区分：駐車場のみ以外、社宅提示：承認、備品提示：(貸与不要/未申請/作成中/作成済/搬入待/搬入済)以外");
									break;
							};
							break;
						default :
							LogUtils.debugByMsg("申請あり、入退居区分：入居、申請区分：駐車場のみ以外、社宅提示：(作成中作成済/提示中/同意済/承認)以外");
							break;
						};
					} else {
						LogUtils.debugByMsg("申請あり、入退居区分：入居、申請区分：駐車場のみ以外、社宅提示：null/空文字");
					}
				}
			// 入退居区分が"退居"の場合
			} else if (CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(comDto.getHdnNyutaikyoKbn())) {
				// 社宅提示ステータス判定
				if (!CheckUtils.isEmpty(comDto.getHdnShatakuTeijiStatus())) {
					switch (comDto.getHdnShatakuTeijiStatus()) {
					// 作成中
					case CodeConstant.PRESENTATION_SITUATION_SAKUSEI_CHU:
						LogUtils.debugByMsg("申請あり、入退居区分：退居、社宅提示：作成中");
						// 各タブの初期ステータス
						comDto.setShatakuTabStatus(true);
						comDto.setBihinTabStatus(true);
						comDto.setSogoTabStatu(true);
						// 運用ガイド
						comDto.setBtnUnyonGuideDisableFlg(false);
						// 一時保存
						comDto.setBtnTmpSaveDisableFlg(false);
						// 作成完了
						comDto.setBtnCreateDisableFlg(false);
						// 次月予約
						comDto.setBtnJigetuYoyakuDisableFlg(false);
						// 台帳登録
						comDto.setBtnShatakuLoginDisableFlg(true);
						break;
						// 承認/同意済/作成済
					case CodeConstant.PRESENTATION_SITUATION_SHONIN:
					case CodeConstant.PRESENTATION_SITUATION_SAKUSEI_SUMI:
					case CodeConstant.PRESENTATION_SITUATION_DOI_SUMI:
						// 備品提示ステータス判定
						switch (bihinTeijiStatus) {
							// 空文字/未申請
							case CodeConstant.DOUBLE_QUOTATION:
							case CodeConstant.BIHIN_STATUS_MI_SAKUSEI:
								LogUtils.debugByMsg("申請あり、入退居区分：退居、社宅提示：承認/同意済/作成済、備品提示：空文字/未申請");
								// 各タブの初期ステータス
								comDto.setShatakuTabStatus(false);
								comDto.setBihinTabStatus(true);
								comDto.setSogoTabStatu(false);
								// 運用ガイド
								comDto.setBtnUnyonGuideDisableFlg(false);
								// 一時保存
								comDto.setBtnTmpSaveDisableFlg(true);
								// 作成完了
								comDto.setBtnCreateDisableFlg(true);
								// 次月予約
								comDto.setBtnJigetuYoyakuDisableFlg(true);
								// 台帳登録
								comDto.setBtnShatakuLoginDisableFlg(true);
								break;
							// 作成中/作成済
							case CodeConstant.BIHIN_STATUS_SAKUSEI_CHU:
							case CodeConstant.BIHIN_STATUS_SAKUSEI_SUMI:
								LogUtils.debugByMsg("申請あり、入退居区分：退居、社宅提示：承認/同意済/作成済、備品提示：作成中/作成済");
								// 各タブの初期ステータス
								comDto.setShatakuTabStatus(false);
								comDto.setBihinTabStatus(true);
								comDto.setSogoTabStatu(false);
								// 運用ガイド
								comDto.setBtnUnyonGuideDisableFlg(false);
								// 一時保存
								comDto.setBtnTmpSaveDisableFlg(false);
								// 作成完了
								comDto.setBtnCreateDisableFlg(false);
								// 次月予約
								comDto.setBtnJigetuYoyakuDisableFlg(true);
								// 台帳登録
								comDto.setBtnShatakuLoginDisableFlg(true);
								break;
							// 提示中/搬出待ち/搬出済
							case CodeConstant.BIHIN_STATUS_TEIJI_CHU:
							case CodeConstant.BIHIN_STATUS_HANSHUTSU_MACHI:
							case CodeConstant.BIHIN_STATUS_HANSHUTSU_SUMI:
								LogUtils.debugByMsg("申請あり、入退居区分：退居、社宅提示：承認/同意済/作成済、備品提示：提示中/搬出待ち/搬出済");
								// 各タブの初期ステータス
								comDto.setShatakuTabStatus(false);
								comDto.setBihinTabStatus(false);
								comDto.setSogoTabStatu(false);
								// 運用ガイド
								comDto.setBtnUnyonGuideDisableFlg(false);
								// 一時保存
								comDto.setBtnTmpSaveDisableFlg(true);
								// 作成完了
								comDto.setBtnCreateDisableFlg(true);
								// 次月予約
								comDto.setBtnJigetuYoyakuDisableFlg(true);
								// 台帳登録
								comDto.setBtnShatakuLoginDisableFlg(true);
								break;
							// 同意済
							case CodeConstant.BIHIN_STATUS_DOI_SUMI:
								LogUtils.debugByMsg("申請あり、入退居区分：退居、社宅提示：承認/同意済/作成済、備品提示：同意済");
								// 各タブの初期ステータス
								comDto.setShatakuTabStatus(false);
								comDto.setBihinTabStatus(true);
								comDto.setSogoTabStatu(false);
								// 運用ガイド
								comDto.setBtnUnyonGuideDisableFlg(false);
								// 一時保存
								comDto.setBtnTmpSaveDisableFlg(true);
								// 作成完了
								comDto.setBtnCreateDisableFlg(false);
								// 次月予約
								comDto.setBtnJigetuYoyakuDisableFlg(true);
								// 台帳登録
								comDto.setBtnShatakuLoginDisableFlg(true);
								break;
							default :
								LogUtils.debugByMsg("申請あり、入退居区分：退居、社宅提示：承認/同意済/作成済、備品提示：(空文字/未申請/作成中/作成済/提示中/搬出待ち/搬出済/同意済)以外");
								break;
						};
					};
				} else {
					LogUtils.debugByMsg("申請あり、入退居区分：退居、社宅提示：null/空文字");
				}
			// 入退居区分が"変更"の場合
			} else if (CodeConstant.NYUTAIKYO_KBN_HENKO.equals(comDto.getHdnNyutaikyoKbn())) {
				// 社宅提示ステータスが"作成中"の場合
				if (CodeConstant.PRESENTATION_SITUATION_SAKUSEI_CHU.equals(comDto.getHdnShatakuTeijiStatus())) {
					LogUtils.debugByMsg("申請あり、入退居区分：変更、社宅提示：作成中");
					// 各タブの初期ステータス
					comDto.setShatakuTabStatus(true);
					comDto.setBihinTabStatus(false);
					comDto.setSogoTabStatu(true);
					// 運用ガイド
					comDto.setBtnUnyonGuideDisableFlg(false);
					// 一時保存
					comDto.setBtnTmpSaveDisableFlg(false);
					// 作成完了
					comDto.setBtnCreateDisableFlg(true);
					// 次月予約
					comDto.setBtnJigetuYoyakuDisableFlg(true);
					// 台帳登録
					comDto.setBtnShatakuLoginDisableFlg(false);
					// 備品ステータス(ヘッダ)
					comDto.setSc006BihinStts(Skf3022Sc006CommonDto.HAIFUN);
				} else {
					LogUtils.debugByMsg("申請あり、入退居区分：変更、社宅提示：作成中以外");
				}
			} else {
				LogUtils.debugByMsg("申請あり、入退居区分：(入居/退居/変更)以外");
			}
		// 申請あり以外
		} else {
			// 入退居区分が"入居"の場合
			if (CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(comDto.getHdnNyutaikyoKbn())) {
				// 社宅提示ステータスが"作成中"の場合
				if (CodeConstant.PRESENTATION_SITUATION_SAKUSEI_CHU.equals(comDto.getHdnShatakuTeijiStatus())) {
					// 社宅が未割当の場合
					if (CheckUtils.isEmpty(comDto.getHdnShatakuKanriNo())) {
						LogUtils.debugByMsg("申請なし、入退居区分：入居、社宅提示：作成中、社宅未割当");
						// 各タブの初期ステータス
						comDto.setShatakuTabStatus(false);
						comDto.setBihinTabStatus(false);
						comDto.setSogoTabStatu(false);
						// タブ切替不可:備品情報
						comDto.setTbpBihinInfo(true);
						// タブ切替不可:役員情報/相互利用
						comDto.setTbpSougoRiyouInfo(true);
						// 運用ガイド
						comDto.setBtnUnyonGuideDisableFlg(false);
						// 一時保存
						comDto.setBtnTmpSaveDisableFlg(true);
						// 作成完了
						comDto.setBtnCreateDisableFlg(true);
						// 次月予約
						comDto.setBtnJigetuYoyakuDisableFlg(true);
						// 台帳登録
						comDto.setBtnShatakuLoginDisableFlg(true);
					} else {
						// 使用料が割当済の場合
						if (!CheckUtils.isEmpty(comDto.getHdnSiyouryoId())) {
							LogUtils.debugByMsg("申請なし、入退居区分：入居、社宅提示：作成中、社宅割当済、使用料割当済");
							// 各タブの初期ステータス
							comDto.setShatakuTabStatus(true);
							comDto.setBihinTabStatus(true);
							comDto.setSogoTabStatu(true);
							// 次月予約
							comDto.setBtnJigetuYoyakuDisableFlg(false);
						// 使用料が未割当の場合
						} else {
							LogUtils.debugByMsg("申請なし、入退居区分：入居、社宅提示：作成中、社宅割当済、使用料未割当");
							// 各タブの初期ステータス
							comDto.setShatakuTabStatus(false);
							comDto.setBihinTabStatus(false);
							comDto.setSogoTabStatu(false);
							// タブ切替不可:備品情報
							comDto.setTbpBihinInfo(true);
							// タブ切替不可:役員情報/相互利用
							comDto.setTbpSougoRiyouInfo(true);
							// 次月予約
							comDto.setBtnJigetuYoyakuDisableFlg(true);
						}
						// 運用ガイド
						comDto.setBtnUnyonGuideDisableFlg(false);
						// 一時保存
						comDto.setBtnTmpSaveDisableFlg(false);
						// 作成完了
						comDto.setBtnCreateDisableFlg(true);
						// 台帳登録
						comDto.setBtnShatakuLoginDisableFlg(false);
					}
				} else {
					LogUtils.debugByMsg("申請なし、入退居区分：入居、社宅提示：作成中以外");
				}
			} else {
				LogUtils.debugByMsg("申請なし、入退居区分：入居以外");
			}

			// 入退居区分(old)が"退居"の場合
			if (CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(comDto.getHdnNyutaikyoKbnOld())) {
				LogUtils.debugByMsg("申請なし、入退居区分(old)：退居");
				// 各タブの初期ステータス
				comDto.setShatakuTabStatus(true);
				comDto.setBihinTabStatus(true);
				comDto.setSogoTabStatu(true);
				// 運用ガイド
				comDto.setBtnUnyonGuideDisableFlg(false);
				// 一時保存
				comDto.setBtnTmpSaveDisableFlg(false);
				// 作成完了
				comDto.setBtnCreateDisableFlg(true);
				// 次月予約
				comDto.setBtnJigetuYoyakuDisableFlg(false);
				// 台帳登録
				comDto.setBtnShatakuLoginDisableFlg(false);
			// 入退居区分(old)が"変更"の場合
			} else if (CodeConstant.NYUTAIKYO_KBN_HENKO.equals(comDto.getHdnNyutaikyoKbnOld())) {
				// 備品ステータス(ヘッダ)
				comDto.setSc006BihinStts(Skf3022Sc006CommonDto.HAIFUN);
				// 社宅提示ステータスが"作成中"の場合
				if (CodeConstant.PRESENTATION_SITUATION_SAKUSEI_CHU.equals(comDto.getHdnShatakuTeijiStatus())) {
					// 退居情報
					if (CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(comDto.getHdnNyutaikyoKbn())) {
						LogUtils.debugByMsg("申請なし、入退居区分(old)：変更、社宅提示：作成中、入退居区分：退居");
						// 各タブの初期ステータス
						comDto.setShatakuTabStatus(true);
						comDto.setBihinTabStatus(true);
						comDto.setSogoTabStatu(true);
						// 運用ガイド
						comDto.setBtnUnyonGuideDisableFlg(false);
						// 一時保存
						comDto.setBtnTmpSaveDisableFlg(false);
						// 作成完了
						comDto.setBtnCreateDisableFlg(true);
						// 次月予約
						comDto.setBtnJigetuYoyakuDisableFlg(true);
						// 台帳登録
						comDto.setBtnShatakuLoginDisableFlg(true);
					// 入居情報
					} else if (CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(comDto.getHdnNyutaikyoKbn())) {
						LogUtils.debugByMsg("申請なし、入退居区分(old)：変更、社宅提示：作成中、入退居区分：入居");
						// 各タブの初期ステータス
						comDto.setShatakuTabStatus(true);
						comDto.setBihinTabStatus(true);
						comDto.setSogoTabStatu(true);
						// 運用ガイド
						comDto.setBtnUnyonGuideDisableFlg(false);
						// 一時保存
						comDto.setBtnTmpSaveDisableFlg(false);
						// 作成完了
						comDto.setBtnCreateDisableFlg(true);
						// 次月予約
						comDto.setBtnJigetuYoyakuDisableFlg(true);
						// 台帳登録
						comDto.setBtnShatakuLoginDisableFlg(false);
					} else {
						LogUtils.debugByMsg("申請なし、入退居区分(old)：変更、社宅提示：作成中、入退居区分：(入居/退居)以外");
					}
				} else {
					LogUtils.debugByMsg("申請なし、入退居区分(old)：変更、社宅提示：作成中以外");
				}
			} else {
				LogUtils.debugByMsg("申請なし、入退居区分(old)：(退居/変更)以外");
			}
		}
		// 社宅情報タブ設定
		setShatakuInfoControlStatus(comDto.getShatakuTabStatus(), comDto);
		// 備品情報タブ設定
		setBihinInfoControlStatus(comDto.getBihinTabStatus(), roomBihinList, comDto);
		// 相互利用タブ判定
		if (comDto.getSogoTabStatu() && CodeConstant.STRING_ZERO.equals(comDto.getSc006SogoRyojokyoSelect())) {
			// 相互利用タブ設定
			setSougoRiyouInfoControlStatus(false, comDto);
			// 相互利用状況
			comDto.setSc006SogoRyojokyoSelectDisableFlg(false);
			// 社宅賃貸料
			comDto.setSc006ChintaiRyoDisableFlg(false);
			// 駐車場賃貸料
			comDto.setSc006TyusyajoRyokinDisableFlg(false);
			// 共益費
			comDto.setSc006KyoekihiDisableFlg(false);
		} else {
			setSougoRiyouInfoControlStatus(comDto.getSogoTabStatu(), comDto);
		}
		// 入退居区分が"入居"の場合
		if (CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(comDto.getHdnNyutaikyoKbn())) {
			// 退居予定日
			comDto.setSc006TaikyoYoteiDayDisableFlg(true);
			// 区画１　利用終了日
			comDto.setSc006RiyouEndDayOneDisableFlg(true);
			// 区画２　利用終了日
			comDto.setSc006RiyouEndDayTwoDisableFlg(true);
			// 返却日
			comDto.setSc006HenkyakuDayDisableFlg(true);
			// 搬出備品情報
			comDto.setSc006KibouDayOutDisableFlg(true);
			comDto.setSc006KibouTimeOutSelectDisableFlg(true);
			comDto.setSc006HonninAddrOutDisableFlg(true);
			comDto.setSc006TachiaiDairiDisableFlg(true);
			comDto.setSc006TachiaiDairiShienDisableFlg(true);
			comDto.setSc006TachiaiDairiAddrDisableFlg(true);
			// 相互利用終了日
			comDto.setSc006EndDayDisableFlg(true);
			// 駐車場のみの場合
			if (CodeConstant.SHINSEI_KBN_PARKING.equals(comDto.getHdnApplKbn())) {
				// 原籍会社
				comDto.setSc006OldKaisyaNameSelectDisableFlg(true);
				// 給与支給会社
				comDto.setSc006KyuyoKaisyaSelectDisableFlg(true);
				// 入居予定日
				comDto.setSc006NyukyoYoteiDayDisableFlg(true);
				// 居住者区分
				comDto.setSc006KyojyusyaKbnSelectDisableFlg(true);
				// 役員算定
				comDto.setSc006YakuinSanteiSelectDisableFlg(true);
				// 社宅使用料調整金額
				comDto.setSc006SiyoroTyoseiPayDisableFlg(true);
				// 個人負担共益費協議中
				comDto.setSc006KyoekihiKyogichuCheckDisableFlg(true);
				// 個人負担共益費月額
				comDto.setSc006KyoekihiMonthPayDisableFlg(true);
				// 個人負担共益費調整金額
				comDto.setSc006KyoekihiTyoseiPayDisableFlg(true);
				// 共益費支払月
				comDto.setSc006KyoekihiPayMonthSelectDisableFlg(true);
			}
		// 入退居区分が"退居"の場合
		} else if (CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(comDto.getHdnNyutaikyoKbn())) {
			// 社宅タブステータス判定
			if (comDto.getShatakuTabStatus()) {
				// 退居予定日
				comDto.setSc006TaikyoYoteiDayDisableFlg(false);
				// 区画１　利用終了日
				comDto.setSc006RiyouEndDayOneDisableFlg(false);
				// 区画２　利用終了日
				comDto.setSc006RiyouEndDayTwoDisableFlg(false);
			}
			// 備品提示ステータス判定
			if (CodeConstant.BIHIN_STATUS_DOI_SUMI.equals(bihinTeijiStatus)) {
				// 同意済
				// 返却日
				comDto.setSc006HenkyakuDayDisableFlg(true);
				// 備品情報一覧
				List<Map<String, Object>> bihinList = comDto.getBihinInfoListTableData();
				for (int i = 0; i < bihinList.size(); i++) {
					Map <String, Object> bihinMap = bihinList.get(i);
					// 備品貸与ステータス
					if (bihinMap.get("bihinTaiyoSttsKbn") != null 
							&& !CheckUtils.isEmpty(bihinMap.get("bihinTaiyoSttsKbn").toString())) {
						String bihinTaiyoSttsCode = createStatusSelect(bihinMap.get("bihinTaiyoSttsKbn").toString(), lendStatusList);
						// 非活性
						bihinMap.put("bihinTaiyoStts", "<select id='bihinTaiyoStts" + i 
								+ "' name='bihinTaiyoStts" + i + "' style='width:90px;' disabled>"
														+ bihinTaiyoSttsCode + "</select>");
					}
				}
				comDto.setBihinInfoListTableData(bihinList);
			}
			// 備品タブステータス判定
			if (comDto.getBihinTabStatus()) {
				// 搬出備品情報
				comDto.setSc006KibouDayOutDisableFlg(false);
				comDto.setSc006KibouTimeOutSelectDisableFlg(false);
				comDto.setSc006HonninAddrOutDisableFlg(false);
				comDto.setSc006TachiaiDairiDisableFlg(false);
				comDto.setSc006TachiaiDairiShienDisableFlg(false);
				comDto.setSc006TachiaiDairiAddrDisableFlg(false);
				comDto.setSc006DairiBikoDisableFlg(false);
				comDto.setSc006BihinBikoDisableFlg(false);
				// 備品提示ステータス判定
				if (CheckUtils.isEmpty(bihinTeijiStatus)
						|| CodeConstant.BIHIN_STATUS_MI_SAKUSEI.equals(bihinTeijiStatus)) {
					// 未設定、または、未作成
					// 返却日
					comDto.setSc006HenkyakuDayDisableFlg(false);
					// 備品情報一覧
					List<Map<String, Object>> bihinList = comDto.getBihinInfoListTableData();
					for (int i = 0; i < bihinList.size(); i++) {
						Map <String, Object> bihinMap = bihinList.get(i);
						// 備品貸与ステータス(退居用)
						if (bihinMap.get("bihinTaiyoSttsKbn") != null 
								&& !CheckUtils.isEmpty(bihinMap.get("bihinTaiyoSttsKbn").toString())) {
							String bihinTaiyoSttsCode = createStatusSelect(bihinMap.get("bihinTaiyoSttsKbn").toString(), lendStatusList);
							// 活性
							bihinMap.put("bihinTaiyoStts", "<select id='bihinTaiyoStts" + i 
									+ "' name='bihinTaiyoStts" + i + "' style='width:90px;' "
									+ bihinMap.get("bihinTaiyoSttsErr").toString() + ">"
															+ bihinTaiyoSttsCode + "</select>");
						}
					}
					comDto.setBihinInfoListTableData(bihinList);
				}
			}
			// 相互利用/役員情報タブステータス判定
			if (comDto.getSogoTabStatu()) {
				// 相互利用終了日
				comDto.setSc006EndDayDisableFlg(false);
			}
			// 上記以外の項目を非活性にする(備考以外)
			// 原籍会社
			comDto.setSc006OldKaisyaNameSelectDisableFlg(true);
			// 給与支給会社
			comDto.setSc006KyuyoKaisyaSelectDisableFlg(true);
			// 入居予定日
			comDto.setSc006NyukyoYoteiDayDisableFlg(true);
			// 居住者区分
			comDto.setSc006KyojyusyaKbnSelectDisableFlg(true);
			// 役員算定
			comDto.setSc006YakuinSanteiSelectDisableFlg(true);
			// 社宅使用料調整金額
			comDto.setSc006SiyoroTyoseiPayDisableFlg(true);
			// 個人負担共益費協議中
			comDto.setSc006KyoekihiKyogichuCheckDisableFlg(true);
			// 個人負担共益費月額
			comDto.setSc006KyoekihiMonthPayDisableFlg(true);
			// 個人負担共益費調整金額
			comDto.setSc006KyoekihiTyoseiPayDisableFlg(true);
			// 共益費支払月
			comDto.setSc006KyoekihiPayMonthSelectDisableFlg(true);
			// 駐車場入力支援（区画１）
			comDto.setParkingShien1DisableFlg(true);
			// 利用開始日（区画１）
			comDto.setSc006RiyouStartDayOneDisableFlg(true);
			// クリアボタン（区画１）
			comDto.setClearParking1DisableFlg(true);
			// 駐車場入力支援（区画２）
			comDto.setParkingShien2DisableFlg(true);
			// 利用開始日（区画２）
			comDto.setSc006RiyouStartDayTwoDisableFlg(true);
			// クリアボタン（区画２）
			comDto.setClearParking2DisableFlg(true);
			// 駐車場使用料調整金額
			comDto.setSc006TyusyaTyoseiPayDisableFlg(true);
			// 備考
			comDto.setSc006BicouDisableFlg(true);
			// 貸与日
			comDto.setSc006TaiyoDayDisableFlg(true);
			// 搬入希望日
			comDto.setSc006KibouDayInDisableFlg(true);
			// 搬入希望日時時間帯
			comDto.setSc006KibouTimeInSelectDisableFlg(true);
			// 搬入本人連絡先
			comDto.setSc006HonninAddrInDisableFlg(true);
			// 受取代理人
			comDto.setSc006UketoriDairiInNameDisableFlg(true);
			// 搬入社員入力支援
			comDto.setSc006UketoriDairiInShienDisableFlg(true);
			// 受取代理人連絡先
			comDto.setSc006UketoriDairiAddrDisableFlg(true);
			// 代理人備考
			comDto.setSc006DairiBikoDisableFlg(false);
			// 備考
			comDto.setSc006BihinBikoDisableFlg(false);
			// 借受会社
			comDto.setSc006KariukeKaisyaSelectDisableFlg(true);
			// 貸付会社
			comDto.setSc006TaiyoKaisyaSelectDisableFlg(true);
			// 出向の有無(相互利用状況)
			comDto.setSc006SogoRyojokyoSelectDisableFlg(true);
			// 相互利用判定区分
			comDto.setSc006SogoHanteiKbnSelectDisableFlg(true);
			// 送金区分（会社）
			comDto.setSc006SokinKyoekihiSelectDisableFlg(true);
			// 送金区分（社宅）
			comDto.setSc006SokinShatakuSelectDisableFlg(true);
			// 社宅賃貸料
			comDto.setSc006ChintaiRyoDisableFlg(true);
			// 駐車場料金
			comDto.setSc006TyusyajoRyokinDisableFlg(true);
			// 共益費（事業者負担）
			comDto.setSc006KyoekihiDisableFlg(true);
			// 開始日
			comDto.setSc006StartDayDisableFlg(true);
			// 配属会社名
			comDto.setSc006HaizokuKaisyaSelectDisableFlg(true);
			// 所属機関
			comDto.setSc006SyozokuKikanDisableFlg(true);
			// 室・部名
			comDto.setSc006SituBuNameDisableFlg(true);
			// 課等名
			comDto.setSc006KanadoMeiDisableFlg(true);
			// 配属データコード番号
			comDto.setSc006HaizokuNoDisableFlg(true);
			// 申請区分が"駐車場"の場合
			if (CodeConstant.SHINSEI_KBN_PARKING.equals(comDto.getHdnApplKbn())) {
				if (CheckUtils.isEmpty(comDto.getSc006RiyouEndDayOne())) {
					// 区画１ 利用終了日
					comDto.setSc006RiyouEndDayOneDisableFlg(true);
				}
				if (CheckUtils.isEmpty(comDto.getSc006RiyouEndDayTwo())) {
					// 区画2 利用終了日
					comDto.setSc006RiyouEndDayTwoDisableFlg(true);
				}
				// 運用ガイド、前に戻る以外のボタンを非活性
				comDto.setBtnTmpSaveDisableFlg(true);
				comDto.setBtnCreateDisableFlg(true);
				comDto.setBtnKeizokuLoginDisableFlg(true);
				comDto.setBtnJigetuYoyakuDisableFlg(true);
				comDto.setBtnShatakuLoginDisableFlg(true);
			}
		// 入退居区分が"変更"の場合
		} else if (CodeConstant.NYUTAIKYO_KBN_HENKO.equals(comDto.getHdnNyutaikyoKbn())) {
			// 相互利用状況
			comDto.setSc006SogoRyojokyoSelectDisableFlg(false);
			// 相互利用が”あり”場合
			if (Skf3022Sc006CommonDto.SOGORIRYOU_ARI.equals(comDto.getSc006SogoHanteiKbnSelect())) {
				// 居住者区分
				comDto.setSc006KyojyusyaKbnSelectDisableFlg(false);
				// 社宅使用料調整金額
				comDto.setSc006SiyoroTyoseiPayDisableFlg(false);
				// 個人負担共益費月額
				comDto.setSc006KyoekihiMonthPayDisableFlg(false);
				// 個人負担共益費調整金額
				comDto.setSc006KyoekihiTyoseiPayDisableFlg(false);
				// 駐車場使用料調整金額
				comDto.setSc006TyusyaTyoseiPayDisableFlg(false);
				// 共益費支払月
				comDto.setSc006KyoekihiPayMonthSelectDisableFlg(false);
				// 相互利用情報
				setSougoRiyouInfoControlStatus(true, comDto);
			}
			// 上記以外の項目を非活性にする
			// 原籍会社
			comDto.setSc006OldKaisyaNameSelectDisableFlg(true);
			// 給与支給会社
			comDto.setSc006KyuyoKaisyaSelectDisableFlg(true);
			// 入居予定日
			comDto.setSc006NyukyoYoteiDayDisableFlg(true);
			// 退居予定日
			comDto.setSc006TaikyoYoteiDayDisableFlg(true);
			// 役員算定
			comDto.setSc006YakuinSanteiSelectDisableFlg(true);
			// 個人負担共益費協議中
			comDto.setSc006KyoekihiKyogichuCheckDisableFlg(true);
			// 駐車場入力支援（区画１）
			comDto.setParkingShien1DisableFlg(true);
			// 利用開始日（区画１）
			comDto.setSc006RiyouStartDayOneDisableFlg(true);
			// クリアボタン（区画１）
			comDto.setClearParking1DisableFlg(true);
			// 利用終了日（区画１）
			comDto.setSc006RiyouEndDayOneDisableFlg(true);
			// 駐車場入力支援（区画２）
			comDto.setParkingShien2DisableFlg(true);
			// 利用開始日（区画２）
			comDto.setSc006RiyouStartDayTwoDisableFlg(true);
			// クリアボタン（区画２）
			comDto.setClearParking2DisableFlg(true);
			// 利用終了日（区画２）
			comDto.setSc006RiyouEndDayTwoDisableFlg(true);
			// 備考
			comDto.setSc006BicouDisableFlg(true);
			// 備品情報
			setBihinInfoControlStatus(false, roomBihinList, comDto);
		}
		// 申請書類管理番号（hidden変数）が""またはNullの場合
		if (CheckUtils.isEmpty(comDto.getHdnShoruikanriNo())) {
			// 申請内容：非活性
			comDto.setSc006ShinseiNaiyoDisableFlg(true);
		} else {
			// 申請内容：活性
			comDto.setSc006ShinseiNaiyoDisableFlg(false);
		}
		// 社宅未割当の場合、使用料入力支援ボタンを非活性
		if (CheckUtils.isEmpty(comDto.getHdnShatakuKanriNo())) {
			comDto.setShiyoryoShienDisableFlg(true);
		} else {
			comDto.setShiyoryoShienDisableFlg(false);
		}
		// 「入退居区分」が"入居"、且つ、申請区分が駐車場のみ、または、社宅提示ステータスが"提示中"、"同意済"、"承認"の場合、社宅入力支援と使用料入力支援ボタンを非活性
		if (codeCacheUtils.getGenericCodeName(FunctionIdConstant.GENERIC_CODE_NYUTAIKYO_KBN,
				CodeConstant.NYUTAIKYO_KBN_NYUKYO).equals(comDto.getSc006NyutaikyoKbn())
				&& (CodeConstant.SHINSEI_KBN_PARKING.equals(comDto.getHdnApplKbn())
				|| CodeConstant.PRESENTATION_SITUATION_TEIJI_CHU.equals(comDto.getHdnShatakuTeijiStatus())
				|| CodeConstant.PRESENTATION_SITUATION_DOI_SUMI.equals(comDto.getHdnShatakuTeijiStatus())
				|| CodeConstant.PRESENTATION_SITUATION_SHONIN.equals(comDto.getHdnShatakuTeijiStatus()))) {
			comDto.setShiyoryoShienDisableFlg(true);
			comDto.setShayakuHeyaShienDisableFlg(true);
		// 入退居区分が"退居"又は"変更"の場合、社宅入力支援と使用料入力支援ボタンを非活性
		} else if (codeCacheUtils.getGenericCodeName(FunctionIdConstant.GENERIC_CODE_NYUTAIKYO_KBN,
				CodeConstant.NYUTAIKYO_KBN_TAIKYO).equals(comDto.getSc006NyutaikyoKbn())
				|| codeCacheUtils.getGenericCodeName(FunctionIdConstant.GENERIC_CODE_NYUTAIKYO_KBN,
						CodeConstant.NYUTAIKYO_KBN_HENKO).equals(comDto.getSc006NyutaikyoKbn())) {
			comDto.setShiyoryoShienDisableFlg(true);
			comDto.setShayakuHeyaShienDisableFlg(true);
		} else {
			// 上記以外は社宅入力支援は活性
			comDto.setShayakuHeyaShienDisableFlg(false);
		}
		// 継続登録ボタン
		// 旧入退居区分が変更、且つ、旧入退居区分と新入退居区分が異なる、且つ、書類管理番号無しは継続登録ボタン活性
		if (CodeConstant.NYUTAIKYO_KBN_HENKO.equals(comDto.getHdnNyutaikyoKbnOld())
				&& !CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(comDto.getHdnNyutaikyoKbn())
				&& CheckUtils.isEmpty(comDto.getHdnShoruikanriNo())) {
			comDto.setBtnKeizokuLoginDisableFlg(false);
		} else {
			comDto.setBtnKeizokuLoginDisableFlg(true);
		}
		// 作成完了ボタン
		if (comDto.getHdnBihinMoveOutFlg()) {
			// 備品搬出待ちフラグがtrueの場合は作成完了ボタンを活性
			comDto.setBtnCreateDisableFlg(false);
		}
		// 同一書類管理番号存在フラグ判定
		if (comDto.getHdnSameFlg()) {
			// 存在する場合は作成完了ボタン、一時保存ボタンを非活性にする
			comDto.setBtnCreateDisableFlg(true);
			comDto.setBtnTmpSaveDisableFlg(true);
		}
		// 駐車場のみ申請場合、備品タブを非活性する
		if (CodeConstant.SHINSEI_KBN_PARKING.equals(comDto.getHdnApplKbn())) {
			setBihinInfoControlStatus(false, roomBihinList, comDto);
		}

//        '狭小/寒冷地の制御
//        Dim switchFlg As String = Com_SettingManager.GetSettingInfo(Constant.SettingsId.SHIYORYO_KEISAN_SWITCH_FLG)
//        If Constant.ShiyoryoKeisanSwitchFlg.KANREITIKYOSYODISPLAY.Equals(switchFlg) Then
//            Me.trKanreiti.Style.Add("display", "none")
//            Me.trKyosyo.Style.Add("display", "none")
//        Else
//            Me.trKanreiti.Style.Remove("display")
//            Me.trKyosyo.Style.Remove("display")
//        End If
	}

	/**
	 * 相互利用情報/役員情報タブ項目制御
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param status	ステータス
	 * @param comDto	*DTO
	 */
	private void setSougoRiyouInfoControlStatus(Boolean status, Skf3022Sc006CommonDto comDto) {

		// 貸付会社
		comDto.setSc006TaiyoKaisyaSelectDisableFlg(!status);
		// 借受会社
		comDto.setSc006KariukeKaisyaSelectDisableFlg(!status);
		// 相互利用状況
		comDto.setSc006SogoRyojokyoSelectDisableFlg(!status);
		// 相互利用判定区分
		comDto.setSc006SogoHanteiKbnSelectDisableFlg(!status);
		// 送金区分（社宅）
		comDto.setSc006SokinShatakuSelectDisableFlg(!status);
		// 送金区分（共益費）
		comDto.setSc006SokinKyoekihiSelectDisableFlg(!status);
		// 社宅賃貸料
		comDto.setSc006ChintaiRyoDisableFlg(!status);
		// 駐車場料金
		comDto.setSc006TyusyajoRyokinDisableFlg(!status);
		// 共益費（事業者負担）
		comDto.setSc006KyoekihiDisableFlg(!status);
		// 開始日
		comDto.setSc006StartDayDisableFlg(!status);
		// 終了日
		comDto.setSc006EndDayDisableFlg(!status);
		// 配属会社名
		comDto.setSc006HaizokuKaisyaSelectDisableFlg(!status);
		// 所属機関
		comDto.setSc006SyozokuKikanDisableFlg(!status);
		// 室・部名
		comDto.setSc006SituBuNameDisableFlg(!status);
		// 課等名
		comDto.setSc006KanadoMeiDisableFlg(!status);
		// 配属データコード番号
		comDto.setSc006HaizokuNoDisableFlg(!status);
	}

	// メソッド名はぜってーへんこーすること！！
	public void pageLoadComplete(Skf3022Sc006CommonDto comDto) {

		Boolean yoyaku = false;
		// 次月予約フラグを設定する
		List<Skf3022Sc006GetShatakuYoyakuDataExp> yoyakuList = new ArrayList<Skf3022Sc006GetShatakuYoyakuDataExp>();
		Skf3022Sc006GetTeijiDataExpParameter param = new Skf3022Sc006GetTeijiDataExpParameter();
		Long teijiNo = CheckUtils.isEmpty(comDto.getHdnTeijiNo()) ? null : Long.parseLong(comDto.getHdnTeijiNo());
		param.setTeijiNo(teijiNo);
		if (teijiNo != null) {
			yoyakuList = skf3022Sc006GetShatakuYoyakuDataExpRepository.getShatakuYoyakuData(param);
			if (yoyakuList.size() > 0) {
				yoyaku = yoyakuList.get(0).getYoyakuflg();
			}
		}
//		comDto.setHdnYoyakuFlg(yoyaku);
		/** 確認ダイアログメッセージ設定 */
		if (!yoyaku || !haveShiyoryoChanged(comDto)) {
			LogUtils.debugByMsg("次月予約データが存在しない、または、使用料変更なし");
			// 使用料変更フラグ
			comDto.setHdnSiyouryoFlg(Skf3022Sc006CommonDto.SIYOURYOFLG_NONE);
			// 一時保存
			// 提示データを仮登録します。よろしいですか？
			comDto.setLitMessageTmpSave(PropertyUtils.getValue(MessageIdConstant.I_SKF_3059));
			// 作成完了
			// 提示可能な社宅情報として、提示データ本登録して作成完了します。よろしいですか？
			comDto.setLitMessageCreate(PropertyUtils.getValue(MessageIdConstant.I_SKF_3040));
			// 社宅管理台帳登録
			// 社員への社宅提示を行わずに、入力内容より社宅管理台帳データを登録します。よろしいですか？
			comDto.setLitMessageShatakuLogin(PropertyUtils.getValue(MessageIdConstant.I_SKF_3041));
		} else {
			LogUtils.debugByMsg("次月予約データが存在、または、使用料変更あり");
			// 使用料変更フラグ
			comDto.setHdnSiyouryoFlg(Skf3022Sc006CommonDto.SIYOURYOFLG_HAVE);
			// 一時保存
			// 月額使用料の計算元データが変更されたため、登録済みの次月予約データを削除して提示データを仮登録します。よろしいですか？
			comDto.setLitMessageTmpSave(PropertyUtils.getValue(MessageIdConstant.I_SKF_3060));
			// 作成完了
			// 月額使用料の計算元データが変更されたため、登録済みの次月予約データを削除して提示データを仮登録します。よろしいですか？
			comDto.setLitMessageCreate(PropertyUtils.getValue(MessageIdConstant.I_SKF_3060));
			// 社宅管理台帳登録
			// 月額使用料の計算元データが変更されたため、登録済みの次月予約データを削除して、入力内容より社宅管理台帳データを登録します。よろしいですか？
			comDto.setLitMessageShatakuLogin(PropertyUtils.getValue(MessageIdConstant.I_SKF_3061));
		}
		// 継続登録
		// 入居情報の提示データを継続登録します。よろしいですか？
		comDto.setLitMessageKeizokuLogin(PropertyUtils.getValue(MessageIdConstant.I_SKF_3065));
		// 前に戻る
		// 前の画面へ戻ります。よろしいですか？編集中の内容は無効になります。
		comDto.setLitMessageBack(PropertyUtils.getValue(MessageIdConstant.I_SKF_1009));
		// 提示備品一覧
		List<Skf3022Sc006GetTeijiBihinDataExp> roomBihinList = new ArrayList<Skf3022Sc006GetTeijiBihinDataExp>();
		// 備品一覧取得判定
		if (comDto.getBihinItiranFlg()) {
			// 提示備品データを取得
			roomBihinList = getBihinData(comDto.getHdnShatakuKanriNo(), comDto.getHdnShatakuKanriNoOld(),
					comDto.getHdnRoomKanriNo(), comDto.getHdnRoomKanriNoOld(),
					comDto.getHdnNyutaikyoKbnOld(), comDto.getHdnTeijiNo(), comDto.getHdnShatakuHeyaFlg());
		} else {
			// 備品情報リスト
			List<Map<String, Object>> bihinList = new ArrayList<Map<String, Object>>();
			bihinList.addAll(jsonArrayToArrayList(comDto.getJsonBihin()));
			// 型変換
			reverseBihinList(bihinList, roomBihinList);

			// 指示書文言設定ループ
			for (Skf3022Sc006GetTeijiBihinDataExp roomBihinMap : roomBihinList) {
				// 指示書文言取得
				String[] shijishoList = outPutShijishoLine(comDto.getHdnNyutaikyoKbnOld(), roomBihinMap.getHeyaSonaetukeStts(),
											roomBihinMap.getBihinTaiyoStts(), roomBihinMap.getBihinTaiyoStts());
				// 指示書文言設定
				roomBihinMap.setShijisho(shijishoList[0]);
			}
		}

		// 指示書背景色設定
		for (Skf3022Sc006GetTeijiBihinDataExp roomBihinMap : roomBihinList) {
			// 新旧指示書文言判定
			if (!Objects.equals(roomBihinMap.getShijisho(), roomBihinMap.getShijishoOld())) {
				roomBihinMap.setShijishoBackColor(Skf3022Sc006CommonDto.COLOR_SHIJISHO_MOVE);
			} else {
				roomBihinMap.setShijishoBackColor(Skf3022Sc006CommonDto.COLOR_SHIJISHO_BASE);
			}
		}
		// 画面項目制御
		setControlStatus(roomBihinList, comDto);
	}

	/**
	 * 備品一覧リスト戻し
	 * Mapリストの備品一覧をDB取得値のリストに変換する
	 * 
	 * @param bihinList			Mapリスト備品一覧
	 * @param roomBihinList		*DB取得値備品一覧
	 */
	public void reverseBihinList(List<Map<String, Object>> bihinList, List<Skf3022Sc006GetTeijiBihinDataExp> roomBihinList) {

		List<Skf3022Sc006GetTeijiBihinDataExp> resultList = new ArrayList<Skf3022Sc006GetTeijiBihinDataExp>();
		SimpleDateFormat dateFormat = new SimpleDateFormat(Skf3022Sc006CommonDto.DATE_FORMAT);
		for (Map<String, Object> tmpBihin : bihinList) {
			Skf3022Sc006GetTeijiBihinDataExp tmpMap = new Skf3022Sc006GetTeijiBihinDataExp();
			tmpMap.setBihinCd(tmpBihin.get("bihinCd").toString());
			tmpMap.setBihinName(tmpBihin.get("bihinName").toString());
			if (!tmpBihin.containsKey("bihinTaiyoStts") || Objects.equals(tmpBihin.get("bihinTaiyoStts"), null)) {
				tmpBihin.put("bihinTaiyoStts", "");
			}
			tmpMap.setBihinTaiyoStts(tmpBihin.get("bihinTaiyoStts").toString());
			tmpMap.setBihinTaiyoSttsOld(tmpBihin.get("bihinTaiyoSttsOldKbn").toString());
			tmpMap.setHeyaSonaetukeStts(tmpBihin.get("heyaSonaetukeStts").toString());
			tmpMap.setShijishoOld(tmpBihin.get("shijishoOld").toString());
			Long bihinTeijiNo = null;
			if (tmpBihin.get("bihinTeijiNo") != null
					&& !CheckUtils.isEmpty(tmpBihin.get("bihinTeijiNo").toString())) {
				bihinTeijiNo =  Long.parseLong(tmpBihin.get("bihinTeijiNo").toString());
			}
			tmpMap.setTeijiNo(bihinTeijiNo);
			Long bihinShatakuKanriNo = null;
			if (tmpBihin.get("bihinShatakuKanriNo") != null
					&& !CheckUtils.isEmpty(tmpBihin.get("bihinShatakuKanriNo").toString())) {
				bihinTeijiNo =  Long.parseLong(tmpBihin.get("bihinShatakuKanriNo").toString());
			}
			tmpMap.setShatakuKanriNo(bihinShatakuKanriNo);
			Long bihinRoomKanriNo = null;
			if (tmpBihin.get("bihinRoomKanriNo") != null
					&& !CheckUtils.isEmpty(tmpBihin.get("bihinRoomKanriNo").toString())) {
				bihinTeijiNo =  Long.parseLong(tmpBihin.get("bihinRoomKanriNo").toString());
			}
			tmpMap.setShatakuRoomKanriNo(bihinRoomKanriNo);
			if (tmpBihin.get("updateDate") != null
				&& !CheckUtils.isEmpty(tmpBihin.get("updateDate").toString())) {
				try {
					tmpMap.setUpdateDate(dateFormat.parse(tmpBihin.get("updateDate").toString()));
				} catch (ParseException e) {
					tmpMap.setUpdateDate(null);
				}
			} else {
				tmpMap.setUpdateDate(null);
			}
			if (tmpBihin.get("updateFlg") != null
					&& "true".equals(tmpBihin.get("updateFlg"))) {
				tmpMap.setUpdateFlg(true);
			} else {
				tmpMap.setUpdateFlg(false);
			}
			tmpMap.setBihinTaiyoSttsErr(tmpBihin.get("bihinTaiyoSttsErr").toString());
			resultList.add(tmpMap);
		}
		roomBihinList.clear();
		roomBihinList.addAll(resultList);
	}

	/**
	 * 提示備品データ取得
	 * 
	 * @param shatakuKanriNo		社宅管理番号
	 * @param shatakuKanriNoOld		旧社宅管理番号
	 * @param shatakuRoomKanriNo	部屋管理番号
	 * @param shatakuRoomKanriNoOld	旧部屋管理番号
	 * @param nyutaikyoKbn			入退居区分
	 * @param hdnTeijiNo			提示番号
	 * @param hdnShatakuHeyaFlg		社宅部屋変更フラグ
	 * @return	提示備品データリスト
	 */
	public List<Skf3022Sc006GetTeijiBihinDataExp> getBihinData(
			String shatakuKanriNo, String shatakuKanriNoOld, String shatakuRoomKanriNo,
			String shatakuRoomKanriNoOld, String nyutaikyoKbn, String hdnTeijiNo, String hdnShatakuHeyaFlg) {

		List<Skf3022Sc006GetTeijiBihinDataExp> roomBihinList = new ArrayList<Skf3022Sc006GetTeijiBihinDataExp>();
		Skf3022Sc006GetTeijiBihinDataExpParameter param = new Skf3022Sc006GetTeijiBihinDataExpParameter();
		// 提示備品データを取得
		if (Skf3022Sc006CommonDto.SHATAKU_HEYA_FLG_YES.equals(hdnShatakuHeyaFlg) && !CheckUtils.isEmpty(shatakuKanriNo)) {
			Long roomKanriNo = CheckUtils.isEmpty(shatakuRoomKanriNo) ? null : Long.parseLong(shatakuRoomKanriNo);
			Long teijiNo = CheckUtils.isEmpty(hdnTeijiNo) ? null : Long.parseLong(hdnTeijiNo);
			param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
			param.setShatakuRoomKanriNo(roomKanriNo);
			param.setTeijiNo(teijiNo);
			param.setNyutaikyoKbn(nyutaikyoKbn);
			roomBihinList = skf3022Sc006GetTeijiBihinDataExpRepository.getRoomBihinData(param);
		} else if (!CheckUtils.isEmpty(shatakuKanriNoOld)) {
			Long roomKanriNoOld = CheckUtils.isEmpty(shatakuRoomKanriNoOld) ? null : Long.parseLong(shatakuRoomKanriNoOld);
			Long teijiNo = CheckUtils.isEmpty(hdnTeijiNo) ? null : Long.parseLong(hdnTeijiNo);
			param.setShatakuKanriNo(Long.parseLong(shatakuKanriNoOld));
			param.setShatakuRoomKanriNo(roomKanriNoOld);
			param.setTeijiNo(teijiNo);
			param.setNyutaikyoKbn(nyutaikyoKbn);
			roomBihinList = skf3022Sc006GetTeijiBihinDataExpRepository.getTeijiBihinData(param);
		}
		for (Skf3022Sc006GetTeijiBihinDataExp roomBihinMap : roomBihinList) {
			// プルダウン初期選択値貸与区分バックアップ
			roomBihinMap.setBihinTaiyoSttsOld(roomBihinMap.getBihinTaiyoStts());
			// 指示書文言取得
			String[] shijishoList = outPutShijishoLine(nyutaikyoKbn, roomBihinMap.getHeyaSonaetukeStts(),
										roomBihinMap.getBihinTaiyoStts(), roomBihinMap.getBihinTaiyoStts());
			// 指示書文言設定
			roomBihinMap.setShijisho(shijishoList[0]);
			// 旧指示書文言
			roomBihinMap.setShijishoOld(shijishoList[1]);
			// 貸与状態エラー(初期は未設定)
			roomBihinMap.setBihinTaiyoSttsErr("");
		}
		return roomBihinList;
	}

	/**
	 * 指示書文言取得
	 * パラメータの値から指示書文言を取得する
	 * 
	 * @param nyutaikyoKbn					入退居区分
	 * @param heyaSonaetukeStts				部屋備付状態
	 * @param bihinTaiyoStts				備品貸与状態
	 * @return	指示書文言
	 */
	private String getShijishoValue(String nyutaikyoKbn, String heyaSonaetukeStts, String bihinTaiyoStts) {

		// 入退居区分判定
		if (CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(nyutaikyoKbn)) {
			// 入退居区分＝"1"（入居）の場合
			if (CodeConstant.BIHIN_STATE_RENTAL.equals(bihinTaiyoStts)
					&& (CodeConstant.BIHIN_STATE_NONE.equals(heyaSonaetukeStts)
					|| CodeConstant.BIHIN_STATE_KYOYO.equals(heyaSonaetukeStts))) {
				// 貸与状態＝"3"（レンタル）で、備付状態＝"1","5"（なし、共有）の場合、「搬入」を出力する。
				return Skf3022Sc006CommonDto.STRING_HANNYU;
			} else {
				return CodeConstant.DOUBLE_QUOTATION;
			}
		} else if (CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(nyutaikyoKbn)) {
			// 入退居区分＝"2"（退居）の場合
			if (CodeConstant.BIHIN_STATE_HOYU.equals(bihinTaiyoStts)
					&& (CodeConstant.BIHIN_STATE_NONE.equals(heyaSonaetukeStts)
					|| CodeConstant.BIHIN_STATE_KYOYO.equals(heyaSonaetukeStts))) {
				// 貸与状態＝"2"（会社保有）で、備付状態＝"1","5"（なし、共有）の場合、「下取」を出力する。
				return Skf3022Sc006CommonDto.STRING_SHITADORI;
			} else if (CodeConstant.BIHIN_STATE_RENTAL.equals(bihinTaiyoStts)
					&& (CodeConstant.BIHIN_STATE_NONE.equals(heyaSonaetukeStts)
					|| CodeConstant.BIHIN_STATE_KYOYO.equals(heyaSonaetukeStts))) {
				// 貸与状態＝"3"（レンタル）で、備付状態＝"1","5"（なし、共有）の場合、「搬出」を出力する。
				return Skf3022Sc006CommonDto.STRING_HANSYUTSU;
			} else {
				return CodeConstant.DOUBLE_QUOTATION;
			}
		} else {
			// 入退居区分が上記以外の場合（変更）
			// 何も出力しない。（空白文字を返却する）
			return CodeConstant.DOUBLE_QUOTATION;
		}
	}

	/**
	 * 備品指示書文言取得
	 * 指示書、旧指示書の文言を取得する。
	 * 
	 * @param nyutaikyoKbn					入退去区分
	 * @param lblHeyaSonaetukeStts			部屋備付区分
	 * @param dbBihinTaiyoStts				備品貸与状態区分(DB取得値)
	 * @param selectBihinTaiyoStts			備品貸与状態区分ドロップダウン選択値
	 * @return	(配列(0)：指示書文言、(1)：旧指示書文言)
	 */
	private String[] outPutShijishoLine(String nyutaikyoKbn, String lblHeyaSonaetukeStts,
									String dbBihinTaiyoStts, String selectBihinTaiyoStts) {

		// 指示書文言取得
		String shijishoValue = getShijishoValue(nyutaikyoKbn, lblHeyaSonaetukeStts, selectBihinTaiyoStts);
		// 旧指示書文言取得
		String shijishoValueOld = getShijishoValue(nyutaikyoKbn, lblHeyaSonaetukeStts, dbBihinTaiyoStts);
		String[] resultStrList = {shijishoValue, shijishoValueOld};
		return resultStrList;
	}

	/**
	 * 使用料変更のチェック
	 * 
	 * @param comDto	DTO
	 * @return	true：変更あり　false：変更なし
	 */
	public Boolean haveShiyoryoChanged(Skf3022Sc006CommonDto comDto) {

		// 旧社宅管理番号
		String hdnShatakuKanriNoOld =
				comDto.getHdnShatakuKanriNoOld() != null ? comDto.getHdnShatakuKanriNoOld() : "";
		// 社宅管理番号
		String hdnShatakuKanriNo =
				comDto.getHdnShatakuKanriNo() != null ? comDto.getHdnShatakuKanriNo() : "";
		// 旧部屋管理番号
		String hdnRoomKanriNoOld =
				comDto.getHdnRoomKanriNoOld() != null ? comDto.getHdnRoomKanriNoOld() : "";
		// 部屋管理番号
		String hdnRoomKanriNo =
				comDto.getHdnRoomKanriNo() != null ? comDto.getHdnRoomKanriNo() : "";
		// 旧使用料パターン
		String hdnSiyouryoIdOld =
				comDto.getHdnSiyouryoIdOld() != null ? comDto.getHdnSiyouryoIdOld() : "";
		// 使用料パターン
		String hdnSiyouryoId =
				comDto.getHdnSiyouryoId() != null ? comDto.getHdnSiyouryoId() : "";
		// 入居予定日
		String sc006NyukyoYoteiDay =
				getDateText(comDto.getSc006NyukyoYoteiDay()) != null ? getDateText(comDto.getSc006NyukyoYoteiDay()) : "";
		// 非表示入居予定日
		String hdnNyukyoDate =
				getDateText(comDto.getHdnNyukyoDate()) != null ? getDateText(comDto.getHdnNyukyoDate()) : "";
		// 役員算定
		String sc006YakuinSanteiSelect =
				comDto.getSc006YakuinSanteiSelect() != null ? comDto.getSc006YakuinSanteiSelect() : "";
		// 非表示役員算定
		String hdnYakuin = comDto.getHdnYakuin() != null ? comDto.getHdnYakuin() : "";
		// 個人負担共益費月額
		String sc006KyoekihiMonthPay =
				getKingakuText(comDto.getSc006KyoekihiMonthPay()) != null ? getKingakuText(comDto.getSc006KyoekihiMonthPay()) : "";
		// 非表示個人負担共益費月額
		String hdnKojinFutan =getKingakuText(comDto.getHdnKojinFutan()) != null ? getKingakuText(comDto.getHdnKojinFutan()) : "";
		// 旧駐車場管理番号1
		String hdnChushajoNoOneOld = comDto.getHdnChushajoNoOneOld() != null ? comDto.getHdnChushajoNoOneOld() : "";
		// 駐車場管理番号1
		String hdnChushajoNoOne = comDto.getHdnChushajoNoOne() != null ? comDto.getHdnChushajoNoOne() : "";
		// 利用開始日１
		String sc006RiyouStartDayOne =
				getDateText(comDto.getSc006RiyouStartDayOne()) != null ? getDateText(comDto.getSc006RiyouStartDayOne()) : "";
		// 非表示利用開始日1
		String hdnRiyouStartDayOne =
				getDateText(comDto.getHdnRiyouStartDayOne()) != null ? getDateText(comDto.getHdnRiyouStartDayOne()) : "";
		// 旧駐車場管理番号2
		String hdnChushajoNoTwoOld = comDto.getHdnChushajoNoTwoOld() != null ? comDto.getHdnChushajoNoTwoOld() : "";
		// 駐車場管理番号2
		String hdnChushajoNoTwo = comDto.getHdnChushajoNoTwo() != null ? comDto.getHdnChushajoNoTwo() : "";
		// 利用開始日2
		String sc006RiyouStartDayTwo =
				getDateText(comDto.getSc006RiyouStartDayTwo()) != null ? getDateText(comDto.getSc006RiyouStartDayTwo()) : "";
		// 非表示利用開始日2
		String hdnRiyouStartDayTwo =
				getDateText(comDto.getHdnRiyouStartDayTwo()) != null ? getDateText(comDto.getHdnRiyouStartDayTwo()) : "";
		// 退居予定日
		String sc006TaikyoYoteiDay =
				getDateText(comDto.getSc006TaikyoYoteiDay()) != null ? getDateText(comDto.getSc006TaikyoYoteiDay()) : "";
		// 非表示退居予定日
		String hdnTaikyoDate = getDateText(comDto.getHdnTaikyoDate()) != null ? getDateText(comDto.getHdnTaikyoDate()) : "";
		// 利用終了日１
		String sc006RiyouEndDayOne =
				getDateText(comDto.getSc006RiyouEndDayOne()) != null ? getDateText(comDto.getSc006RiyouEndDayOne()) : "";
		// 非表示利用終了日１
		String hdnRiyouEndDayOne =
				getDateText(comDto.getHdnRiyouEndDayOne()) != null ? getDateText(comDto.getHdnRiyouEndDayOne()) : "";
		// 利用終了日2
		String sc006RiyouEndDayTwo =
				getDateText(comDto.getSc006RiyouEndDayTwo()) != null ? getDateText(comDto.getSc006RiyouEndDayTwo()) : "";
		// 非表示利用終了日2
		String hdnRiyouEndDayTwo =
				getDateText(comDto.getHdnRiyouEndDayTwo()) != null ? getDateText(comDto.getHdnRiyouEndDayTwo()) : "";

		// 画面の入退居区分が”入居”の場合
		if (codeCacheUtils.getGenericCodeName(
				FunctionIdConstant.GENERIC_CODE_NYUTAIKYO_KBN,
				CodeConstant.NYUTAIKYO_KBN_NYUKYO).equals(comDto.getSc006NyutaikyoKbn())) {
			// 社宅管理番号
			if (!Objects.equals(hdnShatakuKanriNoOld, hdnShatakuKanriNo)) {
				return true;
			}
			// 部屋管理番号
			if (!Objects.equals(hdnRoomKanriNoOld, hdnRoomKanriNo)) {
				return true;
			}
			// 使用料パターン
			if (!Objects.equals(hdnSiyouryoIdOld, hdnSiyouryoId)) {
				return true;
			}
			// 入居予定日
			if (!Objects.equals(sc006NyukyoYoteiDay, hdnNyukyoDate)) {
				return true;
			}
			// 役員算定
			if (!Objects.equals(sc006YakuinSanteiSelect, hdnYakuin)) {
				return true;
			}
			// 個人負担共益費月額
			if (!Objects.equals(sc006KyoekihiMonthPay, hdnKojinFutan)) {
				return true;
			}
			// 駐車場管理番号１
			if (!Objects.equals(hdnChushajoNoOneOld, hdnChushajoNoOne)) {
				return true;
			}
			// 利用開始日１
			if (!Objects.equals(sc006RiyouStartDayOne, hdnRiyouStartDayOne)) {
				return true;
			}
			// 駐車場管理番号２
			if (!Objects.equals(hdnChushajoNoTwoOld, hdnChushajoNoTwo)) {
				return true;
			}
			// 利用開始日２
			if (!Objects.equals(sc006RiyouStartDayTwo, hdnRiyouStartDayTwo)) {
				return true;
			}
		// 画面の入退居区分が”退居”の場合
		} else if (codeCacheUtils.getGenericCodeName(
				FunctionIdConstant.GENERIC_CODE_NYUTAIKYO_KBN,
				CodeConstant.NYUTAIKYO_KBN_TAIKYO).equals(comDto.getSc006NyutaikyoKbn())) {
			// 社宅管理番号
			if (!Objects.equals(hdnShatakuKanriNoOld, hdnShatakuKanriNo)) {
				return true;
			}
			// 部屋管理番号
			if (!Objects.equals(hdnRoomKanriNoOld, hdnRoomKanriNo)) {
				return true;
			}
			// 退居予定日
			if (!Objects.equals(sc006TaikyoYoteiDay, hdnTaikyoDate)) {
				return true;
			}
			// 利用終了日１
			if (!Objects.equals(sc006RiyouEndDayOne, hdnRiyouEndDayOne)) {
				return true;
			}
			// 利用終了日２
			if (!Objects.equals(sc006RiyouEndDayTwo, hdnRiyouEndDayTwo)) {
				return true;
			}
		// 画面の入退居区分が”変更”の場合
		} else {
			// 個人負担共益費月額
			if (!Objects.equals(sc006KyoekihiMonthPay, hdnKojinFutan)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 備品情報タブ設定
	 * 備品情報タブの活性/非活性を設定し画面用の備品一覧を設定する
	 * 
	 * @param status		ステータス
	 * @param roomBihinList	備品リスト
	 * @param comDto		*DTO
	 */
	private void setBihinInfoControlStatus(Boolean status,
			List<Skf3022Sc006GetTeijiBihinDataExp> roomBihinList, Skf3022Sc006CommonDto comDto) {

		// 備品一覧設定
		comDto.setBihinInfoListTableData(setBihinList(status, roomBihinList));
		// 貸与日
		comDto.setSc006TaiyoDayDisableFlg(!status);
		// 返却日
		comDto.setSc006HenkyakuDayDisableFlg(!status);
		// 搬入希望日
		comDto.setSc006KibouDayInDisableFlg(!status);
		// 搬入希望日時時間帯
		comDto.setSc006KibouTimeInSelectDisableFlg(!status);
		// 搬入本人連絡先
		comDto.setSc006HonninAddrInDisableFlg(!status);
		// 受取代理人
		comDto.setSc006UketoriDairiInNameDisableFlg(!status);
		// 搬入社員入力支援
		comDto.setSc006UketoriDairiInShienDisableFlg(!status);
		// 受取代理人連絡先
		comDto.setSc006UketoriDairiAddrDisableFlg(!status);
		// 搬出希望日
		comDto.setSc006KibouDayOutDisableFlg(!status);
		// 搬出希望日時時間帯
		comDto.setSc006KibouTimeOutSelectDisableFlg(!status);
		// 搬出本人連絡先
		comDto.setSc006HonninAddrOutDisableFlg(!status);
		// 立会代理人
		comDto.setSc006TachiaiDairiDisableFlg(!status);
		// 搬出社員入力支援
		comDto.setSc006TachiaiDairiShienDisableFlg(!status);
		// 立会代理人連絡先
		comDto.setSc006TachiaiDairiAddrDisableFlg(!status);
		// 代理人備考
		comDto.setSc006DairiBikoDisableFlg(!status);
		// 備考
		comDto.setSc006BihinBikoDisableFlg(!status);
	}

	/**
	 * 提示備品一覧設定
	 * 画面表示用備品リストを作成する。
	 * 選択プルダウンが「下取り」に設定されている、またはステータスが「false」の場合はプルダウンは非活性とする。
	 * 
	 * @param status			ステータス
	 * @param roomBihinList		取得備品リスト
	 * @return	画面用備品リスト
	 */
	private List<Map<String, Object>> setBihinList(Boolean status, List<Skf3022Sc006GetTeijiBihinDataExp> roomBihinList) {

		List<Map<String, Object>> bihinList = new ArrayList<Map<String, Object>>();
		SimpleDateFormat dateFormat = new SimpleDateFormat(Skf3022Sc006CommonDto.DATE_FORMAT);
		// 貸与区分コード
		Map<String, String> lendStatusCodeList =
				skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_BIHINSTATUS_KBN);
		// 備品貸与区分ドロップダウン
		List<Map<String, Object>> lendStatusList =
				ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_BIHINLENTSTATUS_KBN, "",false);

		// 備品リストループ
		for (int i = 0; i < roomBihinList.size(); i++) {
			Skf3022Sc006GetTeijiBihinDataExp bihinMap = roomBihinList.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			// RelativeID
			tmpMap.put("rId", i);
			// 備品コード
			tmpMap.put("bihinCd", bihinMap.getBihinCd());
			// 備品名
			tmpMap.put("bihinName", bihinMap.getBihinName());
			// 指示書
//			tmpMap.put("shijisho", "<div id='shijisho" + i
//					+ "' name='shijisho" + i + "' style='" + Skf3022Sc006CommonDto.SHIJISHO_STYLE_BASE
//					+ bihinMap.getShijishoBackColor() + "' >"
//					+ HtmlUtils.htmlEscape(bihinMap.getShijisho()) + "</div>");
			tmpMap.put("shijisho", "<input type='text' id='shijisho" + i
					+ "' name='shijisho" + i + "' style='" + Skf3022Sc006CommonDto.SHIJISHO_STYLE_BASE
					+ bihinMap.getShijishoBackColor() + "' value='"
					+ HtmlUtils.htmlEscape(bihinMap.getShijisho()) + "' disabled/>");
			// 旧指示書
			tmpMap.put("shijishoOld", bihinMap.getShijishoOld());
			// 更新日時
			if (bihinMap.getUpdateDate() != null) {
				tmpMap.put("updateDate", dateFormat.format(bihinMap.getUpdateDate()));
			} else {
				tmpMap.put("updateDate", null);
			}
			// 備品提示番号
			tmpMap.put("bihinTeijiNo", bihinMap.getTeijiNo());
			// 備品社宅管理番号
			tmpMap.put("bihinShatakuKanriNo", bihinMap.getShatakuKanriNo());
			// 備品部屋管理番号
			tmpMap.put("bihinRoomKanriNo", bihinMap.getShatakuRoomKanriNo());
			// 矢印
			tmpMap.put("bihinArrow", "");

			// 備品貸与状態区分
			// 備品貸与状態ドロップダウン
			String bihinTaiyoSttsCode = createStatusSelect(bihinMap.getBihinTaiyoStts(), lendStatusList);
			if (Skf3022Sc006CommonDto.SHITADORI.equals(bihinMap.getBihinTaiyoStts()) || !status) {
				// 非活性
				tmpMap.put("bihinTaiyoStts", "<select id='bihinTaiyoStts" + i 
						+ "' name='bihinTaiyoStts" + i + "' style='width:90px;' disabled>"
												+ bihinTaiyoSttsCode + "</select>");
			} else {
				// 活性
				tmpMap.put("bihinTaiyoStts", "<select id='bihinTaiyoStts" + i 
						+ "' name='bihinTaiyoStts" + i + "' style='width:90px;' "
						+ bihinMap.getBihinTaiyoSttsErr() + ">" + bihinTaiyoSttsCode + "</select>");
			}
			// 備品貸与状態選択値
			tmpMap.put("bihinTaiyoSttsKbn", bihinMap.getBihinTaiyoStts());
			// 貸与状態エラー
			tmpMap.put("bihinTaiyoSttsErr", bihinMap.getBihinTaiyoSttsErr());
			// 備品貸与状態選択値元
			tmpMap.put("bihinTaiyoSttsOldKbn", bihinMap.getBihinTaiyoSttsOld());
			// 部屋備付状態
			tmpMap.put("heyaSonaetukeStts", bihinMap.getHeyaSonaetukeStts());
			// 部屋備付状態文言
			tmpMap.put("heyaSonaetukeSttsStr", lendStatusCodeList.get(bihinMap.getHeyaSonaetukeStts()));
			bihinList.add(tmpMap);
		}
		return bihinList;
	}

	/**
	 * 社宅タブコントロール設定
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param status	社宅タブステータスフラグ
	 * @param comDto	*DTO
	 */
	private void setShatakuInfoControlStatus(Boolean status, Skf3022Sc006CommonDto comDto) {

		// 原籍会社
		comDto.setSc006OldKaisyaNameSelectDisableFlg(!status);
		// 給与支給会社
		comDto.setSc006KyuyoKaisyaSelectDisableFlg(!status);
		// 入居予定日
		comDto.setSc006NyukyoYoteiDayDisableFlg(!status);
		// 退居予定日
		comDto.setSc006TaikyoYoteiDayDisableFlg(!status);
		// 居住者区分
		comDto.setSc006KyojyusyaKbnSelectDisableFlg(!status);
		// 役員算定
		comDto.setSc006YakuinSanteiSelectDisableFlg(!status);
		// 社宅使用料調整金額
		comDto.setSc006SiyoroTyoseiPayDisableFlg(!status);

		// 個人負担共益費 協議中設定
		if (!CheckUtils.isEmpty(comDto.getHdnShoruikanriNo())
				&& CodeConstant.NYUTAIKYO_KBN_HENKO.equals(comDto.getHdnNyutaikyoKbn())) {
			LogUtils.debugByMsg("申請あり、入退居区分：変更");
			// 個人負担共益費 協議中
			comDto.setSc006KyoekihiKyogichuCheckDisableFlg(true);
		} else if (CheckUtils.isEmpty(comDto.getHdnShoruikanriNo())) {
			LogUtils.debugByMsg("申請なし");
			// 個人負担共益費 協議中
			comDto.setSc006KyoekihiKyogichuCheckDisableFlg(true);
		} else {
			LogUtils.debugByMsg("申請あり、入退居区分：変更以外");
			// 個人負担共益費 協議中
			comDto.setSc006KyoekihiKyogichuCheckDisableFlg(!status);
		}
		// 個人負担共益費月額
		comDto.setSc006KyoekihiMonthPayDisableFlg(!status);
		// 個人負担共益費調整金額
		comDto.setSc006KyoekihiTyoseiPayDisableFlg(!status);
		// 共益費支払月
		comDto.setSc006KyoekihiPayMonthSelectDisableFlg(!status);
		// 駐車場入力支援（区画１）
		comDto.setParkingShien1DisableFlg(!status);
		// 利用開始日（区画１）
		comDto.setSc006RiyouStartDayOneDisableFlg(!status);
		// クリアボタン（区画１）
		comDto.setClearParking1DisableFlg(!status);
		// 利用終了日（区画１）
		comDto.setSc006RiyouEndDayOneDisableFlg(!status);
		// 駐車場入力支援（区画２）
		comDto.setParkingShien2DisableFlg(!status);
		// 利用開始日（区画２）
		comDto.setSc006RiyouStartDayTwoDisableFlg(!status);
		// クリアボタン（区画２）
		comDto.setClearParking2DisableFlg(!status);
		// 利用終了日（区画２）
		comDto.setSc006RiyouEndDayTwoDisableFlg(!status);
		// 駐車場使用料調整金額
		comDto.setSc006TyusyaTyoseiPayDisableFlg(!status);
		// 備考
		comDto.setSc006BicouDisableFlg(!status);

		// 社宅タブステータス判定
		if (status) {
			// 個人負担共益費協議中にチェックが入っていない
			if (!comDto.getSc006KyoekihiKyogichuCheck()) {
				LogUtils.debugByMsg("社宅タブステータス：true、個人負担共益費協議中：false");
				// 個人負担共益費月額
				comDto.setSc006KyoekihiMonthPayDisableFlg(false);
				// 個人負担共益費調整金額
				comDto.setSc006KyoekihiTyoseiPayDisableFlg(false);
				// 共益費支払月
				comDto.setSc006KyoekihiPayMonthSelectDisableFlg(false);
			} else {
				LogUtils.debugByMsg("社宅タブステータス：true、個人負担共益費協議中：true");
				// 個人負担共益費月額
				comDto.setSc006KyoekihiMonthPayDisableFlg(true);
				// 個人負担共益費調整金額
				comDto.setSc006KyoekihiTyoseiPayDisableFlg(true);
				// 共益費支払月
				comDto.setSc006KyoekihiPayMonthSelectDisableFlg(true);
			}
		} else {
			// 入退居区分が"入居"かつ社宅を必要とする場合
			if (CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(comDto.getHdnNyutaikyoKbnOld())
					&& !CodeConstant.SHINSEI_KBN_PARKING.equals(comDto.getHdnApplKbn())) {
				// 社宅提示ステータス判定
				if (!CheckUtils.isEmpty(comDto.getHdnShatakuTeijiStatus())) {
					switch(comDto.getHdnShatakuTeijiStatus()) {
					// 社宅提示ステータスが作成中
					case CodeConstant.PRESENTATION_SITUATION_SAKUSEI_CHU:
						LogUtils.debugByMsg("社宅タブステータス：false、 入退去区分：入居、 申請区分：駐車場のみ以外、 社宅提示：作成中");
						break;
					// 社宅提示ステータスが提示中/同意済
					case CodeConstant.PRESENTATION_SITUATION_TEIJI_CHU:
					case CodeConstant.PRESENTATION_SITUATION_DOI_SUMI:
						// 個人負担共益費協議中
						comDto.setSc006KyoekihiKyogichuCheckDisableFlg(false);
						// 個人負担共益費協議中にチェックが入っている場合
						if (comDto.getSc006KyoekihiKyogichuCheck()) {
							LogUtils.debugByMsg("社宅タブステータス：false、 入退去区分：入居、 申請区分：駐車場のみ以外、 社宅提示：提示中/同意済、 個人負担共益費協議中:true");
							// 個人負担共益費月額
							comDto.setSc006KyoekihiMonthPayDisableFlg(true);
							// 個人負担共益費調整金額
							comDto.setSc006KyoekihiTyoseiPayDisableFlg(true);
							// 共益費支払月
							comDto.setSc006KyoekihiPayMonthSelectDisableFlg(true);
						} else {
							LogUtils.debugByMsg("社宅タブステータス：false、 入退去区分：入居、 申請区分：駐車場のみ以外、 社宅提示：提示中/同意済、 個人負担共益費協議中:false");
							// 個人負担共益費月額
							comDto.setSc006KyoekihiMonthPayDisableFlg(false);
							// 個人負担共益費調整金額
							comDto.setSc006KyoekihiTyoseiPayDisableFlg(false);
							// 共益費支払月
							comDto.setSc006KyoekihiPayMonthSelectDisableFlg(false);
						}
						break;
					// 社宅提示ステータスが承認
					case CodeConstant.PRESENTATION_SITUATION_SHONIN:
						LogUtils.debugByMsg("社宅タブステータス：false、 入退去区分：入居、 申請区分：駐車場のみ以外、 社宅提示：承認");
						// 個人負担共益費月額
						comDto.setSc006KyoekihiMonthPayDisableFlg(true);
						// 個人負担共益費調整金額
						comDto.setSc006KyoekihiTyoseiPayDisableFlg(true);
						// 共益費支払月
						comDto.setSc006KyoekihiPayMonthSelectDisableFlg(true);
						break;
					default :
						LogUtils.debugByMsg("社宅タブステータス：false、 入退去区分：入居、 申請区分：駐車場のみ以外、 社宅提示：(作成中/提示中/同意済/承認)以外)");
						break;
					};
				} else {
					LogUtils.debugByMsg("社宅タブステータス：false、 入退去区分：入居、 申請区分：駐車場のみ以外、 社宅提示：null/空文字");
				}
			// その他
			} else {
				LogUtils.debugByMsg("社宅タブステータス：false、 (入退去区分：入居以外)/(申請区分：駐車場のみ)");
			}
		}
	}

	/**
	 * 提示データ取得
	 * パラメータの提示番号の提示データをDBより取得する。
	 * パラメータ不正、取得結果無しの場合はnullを返却す。
	 * 
	 * @param teijiNo	提示番号
	 * @return			提示データ
	 */
	public Skf3022Sc006GetTeijiDataExp getTeijiData(String teijiNo) {

		List<Skf3022Sc006GetTeijiDataExp> getDataList = new ArrayList<Skf3022Sc006GetTeijiDataExp>();
		Skf3022Sc006GetTeijiDataExpParameter param = new Skf3022Sc006GetTeijiDataExpParameter();
		// パラメータチェック
		if (CheckUtils.isEmpty(teijiNo) || !CheckUtils.isNumeric(teijiNo)) {
			// デバッグログ
			LogUtils.debugByMsg("パラメータ不正");
			return null;
		}

		param.setTeijiNo(Long.parseLong(teijiNo));
		getDataList = skf3022Sc006GetTeijiDataExpRepository.getTeijiData(param);
		// 取得結果判定
		if (getDataList.size() < 1) {
			// デバッグログ
			LogUtils.debugByMsg("提示データなし");
			return null;
		}

		return getDataList.get(0);
	}

	/**
	 * 画面hidden項目の設定
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param hdnTeijiNo			提示番号
	 * @param hdnNyukyoDate			入居予定日
	 * @param hdnTaikyoDate			退居予定日
	 * @param hdnShoruikanriNo		書類管理番号
	 * @param hdnNyutaikyoKbn		入退居区分
	 * @param hdnApplKbn			申請区分
	 * @param hdnShainNoChangeFlg	社員番号変更フラグ
	 * @param teijiData				提示データ(DB取得値)
	 * @param comDto				*DTO
	 */
	public void setHiddenValues(
			String hdnTeijiNo, String hdnNyukyoDate, String hdnTaikyoDate,
			String hdnShoruikanriNo, String hdnNyutaikyoKbn, String hdnApplKbn,
			String hdnShainNoChangeFlg, Skf3022Sc006GetTeijiDataExp teijiData, Skf3022Sc006CommonDto comDto) {

		SimpleDateFormat dateFormat = new SimpleDateFormat(Skf3022Sc006CommonDto.DATE_FORMAT);

		// 提示番号
//      Me.hdnTeijiNo.Value = teijiDataTourokuInfo.TeijiNo
//      Me.hdnTeijiNoOld.Value = String.Empty
		comDto.setHdnTeijiNoOld("");

		// 入退居区分元
//      Me.hdnNyutaikyoKbnOld.Value = teijiDataTourokuInfo.NyutaikyoKbn
//      Me.hdnNyutaikyoKbn.Value = teijiDataTourokuInfo.NyutaikyoKbn
		comDto.setHdnNyutaikyoKbnOld(hdnNyutaikyoKbn);

		// 申請区分
//      Me.hdnApplKbn.Value = teijiDataTourokuInfo.ApplKbn

		// 入居予定日
//      If Not dt(0).IsNYUKYO_DATENull Then
//          Me.hdnNyukyoDay.Value = dt(0).NYUKYO_DATE
//      Else
//          Me.hdnNyukyoDay.Value = teijiDataTourokuInfo.NyukyoDate
//      End If
		if (!CheckUtils.isEmpty(teijiData.getNyukyoDate())) {
			comDto.setHdnNyukyoDate(teijiData.getNyukyoDate());
		}

		// 退居予定日
//      Me.hdnTaikyoDay.Value = teijiDataTourokuInfo.TaikyoDate

//      '申請書類管理番号
//      Me.hdnShoruikanriNo.Value = teijiDataTourokuInfo.ShoruikanriNo

		// 同一書類管理番号存在チェック (退居の場合に入居で同一管理番号があるか)
		Long hdnSameFlg = 0L;
		if (!CheckUtils.isEmpty(hdnShoruikanriNo)
				&& CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(hdnNyutaikyoKbn)) {
			Skf3022Sc006GetSameAppNoCountExpParameter param = new Skf3022Sc006GetSameAppNoCountExpParameter();
			param.setApplNo(hdnShoruikanriNo);
			param.setDeleteFlag(CodeConstant.STRING_ZERO);
			param.setNyutaikyoKbn(CodeConstant.NYUTAIKYO_KBN_NYUKYO);
			hdnSameFlg = skf3022Sc006GetSameAppNoCountExpRepository.getSameAppNoCount(param);

			if (hdnSameFlg > 0) {
				comDto.setHdnSameFlg(true);
			}
		}
		// 役員算定区分
		if (!CheckUtils.isEmpty(teijiData.getYakuinSannteiKbn())) {
			comDto.setHdnYakuin(teijiData.getYakuinSannteiKbn());
		}
		// 個人負担共益費月額(nullはない)
		comDto.setHdnKojinFutan(teijiData.getKyoekihiPerson().toString());
		// 区画番号1
		if (!CheckUtils.isEmpty(teijiData.getParkingBlock1())) {
			comDto.setHdnKukakuNoOne(teijiData.getParkingBlock1());
		}
		// 利用開始日1
		if (!CheckUtils.isEmpty(teijiData.getParking1StartDate())) {
			comDto.setHdnRiyouStartDayOne(teijiData.getParking1StartDate());
		}
		// 利用終了日1
		if (!CheckUtils.isEmpty(teijiData.getParking1EndDate())) {
			comDto.setHdnRiyouEndDayOne(teijiData.getParking1EndDate());
		}
		// 区画番号2
		if (!CheckUtils.isEmpty(teijiData.getParkingBlock2())) {
			comDto.setHdnKukakuNoTwo(teijiData.getParkingBlock2());
		}
		// 利用開始日2
		if (!CheckUtils.isEmpty(teijiData.getParking2StartDate())) {
			comDto.setHdnRiyouStartDayTwo(teijiData.getParking2StartDate());
		}
		// 利用終了日2
		if (!CheckUtils.isEmpty(teijiData.getParking2EndDate())) {
			comDto.setHdnRiyouEndDayTwo(teijiData.getParking2EndDate());
		}
		// 社宅管理番号元
		if (teijiData.getShatakuKanriNo() != null) {
			comDto.setHdnShatakuKanriNo(teijiData.getShatakuKanriNo().toString());
			comDto.setHdnShatakuKanriNoOld(teijiData.getShatakuKanriNo().toString());
		}
		// 部屋管理番号元
		if (teijiData.getShatakuNoomKanriNo() != null) {
			comDto.setHdnRoomKanriNo(teijiData.getShatakuNoomKanriNo().toString());
			comDto.setHdnRoomKanriNoOld(teijiData.getShatakuNoomKanriNo().toString());
		}
		// 使用料計算パターンID元
		if (teijiData.getRentalPatternId() != null) {
			comDto.setHdnSiyouryoIdOld(teijiData.getRentalPatternId().toString());
			comDto.setHdnSiyouryoId(teijiData.getRentalPatternId().toString());
			comDto.setHdnShiyoryoKeisanPatternId(teijiData.getRentalPatternId().toString());
		}
		// 使用料計算情報取得
		getShiyoryoKeisanSessionInfo(comDto);
		// 駐車場管理番号元（区画１）
		if (teijiData.getParkingKanriNo1() != null) {
			comDto.setHdnChushajoNoOneOld(teijiData.getParkingKanriNo1().toString());
			comDto.setHdnChushajoNoOne(teijiData.getParkingKanriNo1().toString());
		}
		// 駐車場管理番号元（区画２）
		if (teijiData.getParkingKanriNo2() != null) {
			comDto.setHdnChushajoNoTwoOld(teijiData.getParkingKanriNo2().toString());
			comDto.setHdnChushajoNoTwo(teijiData.getParkingKanriNo2().toString());
		}
		// 継続登録フラグ
		comDto.setHdnKeizokuBtnFlg(false);
//		// 次月予約存在フラグ
//		comDto.setHdnYoyakuFlg(teijiData.getYoyakuflg());
		// 原籍会社コード
		if (!CheckUtils.isEmpty(teijiData.getOriginalCompanyCd())) {
			comDto.setHdnGensekiKaishaCd(teijiData.getOriginalCompanyCd());
		}
		// 給与支給会社コード
		if (!CheckUtils.isEmpty(teijiData.getPayCompanyCd())) {
			comDto.setHdnKyuyoKaishaCd(teijiData.getPayCompanyCd());
		}
		// 貸付会社コード
		if (!CheckUtils.isEmpty(teijiData.getKashitukeCompanyCd())) {
			comDto.setHdnKashitsukeKaishaCd(teijiData.getKashitukeCompanyCd());
		}
		// 借受会社コード
		if (!CheckUtils.isEmpty(teijiData.getKariukeCompanyCd())) {
			comDto.setHdnKariukeKaishaCd(teijiData.getKariukeCompanyCd());
		}
		// 配属会社コード
		if (!CheckUtils.isEmpty(teijiData.getAssignCompanyCd())) {
			comDto.setHdnHaizokuKaishaCd(teijiData.getAssignCompanyCd());
		}
		// 社宅提示ステータス
		if (!CheckUtils.isEmpty(teijiData.getShatakuTeijiStatus())) {
			comDto.setHdnShatakuTeijiStatus(teijiData.getShatakuTeijiStatus());
		}
		// 備品提示ステータス
		if (!CheckUtils.isEmpty(teijiData.getBihinTeijiStatus())) {
			comDto.setHdnBihinTeijiStatus(teijiData.getBihinTeijiStatus());
		}
		// 備品貸与区分
		if (!CheckUtils.isEmpty(teijiData.getBihinTaiyoKbn())) {
			comDto.setHdnBihinTaiyoKbn(teijiData.getBihinTaiyoKbn());
		}
		// 提示データ更新日
		if (teijiData.getUpdateDate() != null) {
			comDto.setHdnTeijiDataUpdateDate(dateFormat.format(teijiData.getUpdateDate()));
		}
		// 社宅部屋情報マスタ元更新日
		if (teijiData.getUpdateDateSr() != null) {
			comDto.setHdnShatakuRoomOldUpdateDate(dateFormat.format(teijiData.getUpdateDateSr()));
		}
		// 社宅駐車場区画情報マスタ元（区画１）更新日
		if (teijiData.getUpdateDate1() != null) {
			comDto.setHdnShatakuParkingBlockOld1UpdateDate(dateFormat.format(teijiData.getUpdateDate1()));
		}
		// 社宅駐車場区画情報マスタ元（区画２）更新日
		if (teijiData.getUpdateDate2() != null) {
			comDto.setHdnShatakuParkingBlockOld2UpdateDate(dateFormat.format(teijiData.getUpdateDate2()));
		}
		// 生年月日
		if (!CheckUtils.isEmpty(teijiData.getBirthday())) {
			comDto.setHdnBirthday(teijiData.getBirthday());
		}
		// 社宅部屋変更フラグ
		comDto.setHdnShatakuHeyaFlg(Skf3022Sc006CommonDto.SHATAKU_HEYA_FLG_NO);
//      '社員番号変更フラグ
//      Me.hdnShainNoChangeFlg.Value = teijiDataTourokuInfo.ShainNoChangeFlg

		// 【使用料計算機能対応】使用料パターン排他処理用更新日付
		if (teijiData.getRentalPatternId() != null) {
			List<Skf3022Sc006GetRentalPatternInfoExp> rentalPatternInfoList = new ArrayList<Skf3022Sc006GetRentalPatternInfoExp>();
			Skf3022Sc006GetRentalPatternInfoExpParameter param = new Skf3022Sc006GetRentalPatternInfoExpParameter();
			param.setRentalPatternId(Long.parseLong(comDto.getHdnShiyoryoKeisanPatternId()));

			// 使用料パターン情報取得
			rentalPatternInfoList = skf3022Sc006GetRentalPatternInfoExpRepository.getRentalPatternInfo(param);
			// comDto.setHdnRentalPatternUpdateDate(dateFormat.format(rentalPatternInfoList.get(0).getUpdateDate()));
			if (rentalPatternInfoList.size() > 0) {
				comDto.setHdnRentalPatternUpdateDate(dateFormat.format(rentalPatternInfoList.get(0).getUpdateDate()));
			} else {
				// 使用料パターンが存在しない為、更新日時を空文字に設定
				comDto.setHdnRentalPatternUpdateDate("");
			}
		}
	}

	/**
	 * 使用料パターン情報取得
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param comDto	*DTO
	 */
	private void getShiyoryoKeisanSessionInfo(Skf3022Sc006CommonDto comDto) {

		SimpleDateFormat dateFormat = new SimpleDateFormat(Skf3022Sc006CommonDto.DATE_FORMAT);
		// 使用料入力支援戻り値クリア
		clearShiyoryoShienData(comDto);
		// 使用料パターンID（hidden変数）が存在する場合、使用料計算情報を取得する。
		if (!CheckUtils.isEmpty(comDto.getHdnShiyoryoKeisanPatternId())) {
			List<Skf3022Sc006GetRentalPatternInfoExp> rentalPatternInfoList = new ArrayList<Skf3022Sc006GetRentalPatternInfoExp>();
			Skf3022Sc006GetRentalPatternInfoExpParameter param = new Skf3022Sc006GetRentalPatternInfoExpParameter();
			param.setRentalPatternId(Long.parseLong(comDto.getHdnShiyoryoKeisanPatternId()));

			// 使用料パターン情報取得
			rentalPatternInfoList = skf3022Sc006GetRentalPatternInfoExpRepository.getRentalPatternInfo(param);
			param = null;
			// 取得結果判定
			if (rentalPatternInfoList.size() < 1 || rentalPatternInfoList.get(0).getRental() == null) {
				// デバッグログ
				LogUtils.debugByMsg("使用料パターン情報取得結果：0件");
				// 使用料パターンが存在しても、使用料情報が存在しない場合（使用料支援未操作の場合）作成しない。
				comDto.setHdnSiyouryoIdOld("");
				comDto.setHdnSiyouryoId("");
				return;
			}
			Skf3022Sc006GetRentalPatternInfoExp rentalPatternInfo = rentalPatternInfoList.get(0);
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
	public void clearShiyoryoShienData(Skf3022Sc006CommonDto comDto) {

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
	 * メッセージボックス表示処理
	 * パラメータのメッセージが設定されている場合はメッセージボックスを表示
	 * メッセージ未設定時は非表示に設定する
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param msg		メッセージ
	 * @param comDto	*DTO
	 */
	private void setMsgBox(String msg, Skf3022Sc006CommonDto comDto) {

		if (CheckUtils.isEmpty(msg)) {
			// 表示しない
			comDto.setSc006Msg("");
			comDto.setSc006MsgBoxStyle(Skf3022Sc006CommonDto.OFF_MSG_STYLE);
		} else {
			comDto.setSc006Msg(msg);
			comDto.setSc006MsgBoxStyle(Skf3022Sc006CommonDto.ON_MSGSTYLE_BASE);
		};
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
			colorStr = Skf3022Sc006CommonDto.BIHIN_FONT_MI_SAKUSEI;
			break;
		case CodeConstant.BIHIN_STATUS_SAKUSEI_CHU:
			colorStr = Skf3022Sc006CommonDto.BIHIN_FONT_SAKUSEI_CHU;
			break;
		case CodeConstant.BIHIN_STATUS_SAKUSEI_SUMI:
			colorStr = Skf3022Sc006CommonDto.BIHIN_FONT_SAKUSEI_SUMI;
			break;
		case CodeConstant.BIHIN_STATUS_TEIJI_CHU:
			colorStr = Skf3022Sc006CommonDto.BIHIN_FONT_TEIJI_CHU;
			break;
		case CodeConstant.BIHIN_STATUS_DOI_SUMI:
			colorStr = Skf3022Sc006CommonDto.BIHIN_FONT_DOI_SUMI;
			break;
		case CodeConstant.BIHIN_STATUS_HANNYU_MACHI:
			colorStr = Skf3022Sc006CommonDto.BIHIN_FONT_HANNYU_MACHI;
			break;
		case CodeConstant.BIHIN_STATUS_HANNYU_SUMI:
			colorStr = Skf3022Sc006CommonDto.BIHIN_FONT_HANNYU_SUMI;
			break;
		case CodeConstant.BIHIN_STATUS_HANSHUTSU_MACHI:
			colorStr = Skf3022Sc006CommonDto.BIHIN_FONT_HANSHUTSU_MACHI;
			break;
		case CodeConstant.BIHIN_STATUS_HANSHUTSU_SUMI:
			colorStr = Skf3022Sc006CommonDto.BIHIN_FONT_HANSHUTSU_SUMI;
			break;
		case CodeConstant.BIHIN_STATUS_SHONIN:
			colorStr = Skf3022Sc006CommonDto.BIHIN_FONT_SHONIN;
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
	private String setShatakuTeijiStatusCss(String shatakuTeijiStatusKbn) {

		String colorStr = "color:black;";
		// null空チェック
		if (CheckUtils.isEmpty(shatakuTeijiStatusKbn)) {
			return colorStr;
		}
		// 提示ステータス判定
		switch (shatakuTeijiStatusKbn) {
		case CodeConstant.PRESENTATION_SITUATION_SAKUSEI_CHU:
			colorStr = Skf3022Sc006CommonDto.TEIJI_FONT_SAKUSEI_CHU;
			break;
		case CodeConstant.PRESENTATION_SITUATION_SAKUSEI_SUMI:
			colorStr = Skf3022Sc006CommonDto.TEIJI_FONT_SAKUSEI_SUMI;
			break;
		case CodeConstant.PRESENTATION_SITUATION_TEIJI_CHU:
			colorStr = Skf3022Sc006CommonDto.TEIJI_FONT_TEIJI_CHU;
			break;
		case CodeConstant.PRESENTATION_SITUATION_DOI_SUMI:
			colorStr = Skf3022Sc006CommonDto.TEIJI_FONT_DOI_SUMI;
			break;
		case CodeConstant.PRESENTATION_SITUATION_SHONIN:
			colorStr = Skf3022Sc006CommonDto.TEIJI_FONT_SHONIN;
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
	public Map<String, String> createSiyoryoKeiSanParam(Skf3022Sc006CommonDto comDto) {

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
		paramMap.put("hdnNyutaikyoKbn", comDto.getHdnNyutaikyoKbn());
		paramMap.put("sc006NyukyoYoteiDay", comDto.getSc006NyukyoYoteiDay());
		paramMap.put("hdnNyukyoDate", comDto.getHdnNyukyoDate());
		paramMap.put("sc006TaikyoYoteiDay", comDto.getSc006TaikyoYoteiDay());
		paramMap.put("sc006TyusyaDayPayOne", comDto.getSc006TyusyaDayPayOne());
		paramMap.put("sc006TyusyaDayPayTwo", comDto.getSc006TyusyaDayPayTwo());
		paramMap.put("sc006RiyouStartDayOne", comDto.getSc006RiyouStartDayOne());
		paramMap.put("hdnRiyouStartDayOne", comDto.getHdnRiyouStartDayOne());
		paramMap.put("sc006RiyouEndDayOne", comDto.getSc006RiyouEndDayOne());
		paramMap.put("sc006RiyouStartDayTwo", comDto.getSc006RiyouStartDayTwo());
		paramMap.put("hdnRiyouStartDayTwo", comDto.getHdnRiyouStartDayTwo());
		paramMap.put("sc006RiyouEndDayTwo", comDto.getSc006RiyouEndDayTwo());
		paramMap.put("sc006TyusyaTyoseiPay", comDto.getSc006TyusyaTyoseiPay());

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
	public void setSiyoryoKeiSanParam(Map<String, String> resultMap, Skf3022Sc006CommonDto comDto) {

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
	public void setSiyoryoKeiSanParamAsync(Map<String, String> resultMap, Skf3022Sc006CommonAsyncDto asyncDto) {

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
	}

	/**
	 * 使用料計算(提示データ登録内部)戻り値初期化(非同期)
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param asyncDto	*DTO
	 * @return			パラメータマップ
	 */
	public void initializeSiyoryoKeiSanParamAsync(Skf3022Sc006CommonAsyncDto asyncDto) {

		asyncDto.setSc006TyusyaMonthPayAfter(null);
		asyncDto.setSc006SiyoryoHiwariPay(null);
		asyncDto.setSc006SyatauMonthPayAfter(null);
		asyncDto.setSc006ShiyoryoTsukigaku(null);
		asyncDto.setSc006TyusyaDayPayOne(null);
		asyncDto.setSc006TyusyaMonthPayOne(null);
		asyncDto.setSc006TyusyaDayPayTwo(null);
		asyncDto.setSc006TyusyaMonthPayTwo(null);
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
			LogUtils.debugByMsg(e.getMessage());
		} catch (JsonParseException e) {
			LogUtils.debugByMsg(e.getMessage());
		} catch (JsonMappingException e) {
			LogUtils.debugByMsg(e.getMessage());
		} catch (IOException e) {
			LogUtils.debugByMsg(e.getMessage());
		}
		return listData;
	}

	/**
	 * エラーコントロール初期化
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param initDto	*DTO
	 */
	public void clearVaridateErr(Skf3022Sc006CommonDto comDto) {

		// 原籍会社
		comDto.setSc006OldKaisyaNameSelectErr(null);
		// 給与支給会社名
		comDto.setSc006KyuyoKaisyaSelectErr(null);
		// 入居予定日
		comDto.setSc006NyukyoYoteiDayErr(null);
		// 退居予定日
		comDto.setSc006TaikyoYoteiDayErr(null);
		// 利用開始日1
		comDto.setSc006RiyouStartDayOneErr(null);
		// 利用終了日1
		comDto.setSc006RiyouEndDayOneErr(null);
		// 居住者区分
		comDto.setSc006KyojyusyaKbnSelectErr(null);
		// 利用開始日2
		comDto.setSc006RiyouStartDayTwoErr(null);
		// 利用終了日2
		comDto.setSc006RiyouEndDayTwoErr(null);
		// 役員算定
		comDto.setSc006YakuinSanteiSelectErr(null);
		// 社宅使用料調整金額
		comDto.setSc006SiyoroTyoseiPayErr(null);
		// 駐車場使用料調整金額
		comDto.setSc006TyusyaTyoseiPayErr(null);
		// 個人負担共益費月額
		comDto.setSc006KyoekihiMonthPayErr(null);
		// 個人負担共益費調整金額
		comDto.setSc006KyoekihiTyoseiPayErr(null);
		// 共益費支払月選択値
		comDto.setSc006KyoekihiPayMonthSelectErr(null);
		// 貸与日
		comDto.setSc006TaiyoDayErr(null);
		// 返却日
		comDto.setSc006HenkyakuDayErr(null);
		// 搬入希望日
		comDto.setSc006KibouDayInErr(null);
		// 搬入希望時間
		comDto.setSc006KibouTimeInSelectErr(null);
		// 搬入本人連絡先
		comDto.setSc006HonninAddrInErr(null);
		// 搬入受取代理人名
		comDto.setSc006UketoriDairiInNameErr(null);
		// 搬入受取代理人連絡先
		comDto.setSc006UketoriDairiAddrErr(null);
		// 搬出希望日
		comDto.setSc006KibouDayOutErr(null);
		// 搬出希望時間
		comDto.setSc006KibouTimeOutSelectErr(null);
		// 搬出本人連絡先
		comDto.setSc006HonninAddrOutErr(null);
		// 搬出立会代理人名
		comDto.setSc006TachiaiDairiErr(null);
		// 搬出立会代理人連絡先
		comDto.setSc006TachiaiDairiAddrErr(null);
		// 配属会社名選択値
		comDto.setSc006HaizokuKaisyaSelectErr(null);
		// 出向の有無(相互利用状況)
		comDto.setSc006SogoRyojokyoSelectErr(null);
		// 所属機関
		comDto.setSc006SyozokuKikanErr(null);
		// 貸付会社選択値
		comDto.setSc006TaiyoKaisyaSelectErr(null);
		// 室・部名
		comDto.setSc006SituBuNameErr(null);
		// 借受会社
		comDto.setSc006KariukeKaisyaSelectErr(null);
		// 課等名
		comDto.setSc006KanadoMeiErr(null);
		// 相互利用判定区分
		comDto.setSc006SogoHanteiKbnSelectErr(null);
		// 配属データコード番号
		comDto.setSc006HaizokuNoErr(null);
		// 社宅使用料会社間送金区分
		comDto.setSc006SokinShatakuSelectErr(null);
		// 共益費会社間送付区分
		comDto.setSc006SokinKyoekihiSelectErr(null);
		// 開始日
		comDto.setSc006StartDayErr(null);
		// 終了日
		comDto.setSc006EndDayErr(null);
		// 社宅賃貸料
		comDto.setSc006ChintaiRyoErr(null);
		// 駐車場料金
		comDto.setSc006TyusyajoRyokinErr(null);
		// 共益費(事業者負担)
		comDto.setSc006KyoekihiErr(null);
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
	public void setErrVariableLabel(List<Map<String, Object>> labelList, Skf3022Sc006CommonDto comDto) {
		// 可変ラベル値
		Map <String, Object> labelMap = labelList.get(0);
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
		String sc006KyoekihiPayAfter = (labelMap.get("sc006KyoekihiPayAfter") != null) ? labelMap.get("sc006KyoekihiPayAfter").toString() : "";

		/** 戻り値設定 */
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
		// 協議中フラグ復元
		if ("1".equals(comDto.getSc006KyoekihiKyogichuCheckState())) {
			comDto.setSc006KyoekihiKyogichuCheck(true);
		} else {
			comDto.setSc006KyoekihiKyogichuCheck(false);
		}
	}

	/**
	 * 全項目の活性/非活性制御
	 * パラメータの値で変動する全てのコントロールの活性/非活性を行う
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param state		(true:非活性、 false:活性)
	 * @param comDto	*DTO
	 */
	public void setDisableCtrlAll(Boolean state, Skf3022Sc006CommonDto comDto) {

		/** 上部ボタン */
		// 申請内容
		comDto.setSc006ShinseiNaiyoDisableFlg(state);
		// 社宅入力支援
		comDto.setShayakuHeyaShienDisableFlg(state);
		// 社宅使用料入力支援
		comDto.setShiyoryoShienDisableFlg(state);
		/** タブ切替 */
		// 備品情報タブ
		comDto.setTbpBihinInfo(state);
		// 役員情報/相互利用情報タブ
		comDto.setTbpSougoRiyouInfo(state);
		/** 社宅情報 */
		// 原籍会社
		comDto.setSc006OldKaisyaNameSelectDisableFlg(state);
		// 給与支給会社
		comDto.setSc006KyuyoKaisyaSelectDisableFlg(state);
		// 入居予定日
		comDto.setSc006NyukyoYoteiDayDisableFlg(state);
		// 退居予定日
		comDto.setSc006TaikyoYoteiDayDisableFlg(state);
		// 居住者区分
		comDto.setSc006KyojyusyaKbnSelectDisableFlg(state);
		// 役員算定
		comDto.setSc006YakuinSanteiSelectDisableFlg(state);
		// 社宅使用料調整金額
		comDto.setSc006SiyoroTyoseiPayDisableFlg(state);
		// 社宅情報:個人負担共益費 協議中
		comDto.setSc006KyoekihiKyogichuCheckDisableFlg(state);
		// 個人負担共益費月額
		comDto.setSc006KyoekihiMonthPayDisableFlg(state);
		// 個人負担共益費調整金額
		comDto.setSc006KyoekihiTyoseiPayDisableFlg(state);
		// 共益費支払月
		comDto.setSc006KyoekihiPayMonthSelectDisableFlg(state);
		// 駐車場入力支援（区画１）
		comDto.setParkingShien1DisableFlg(state);
		// 利用開始日1
		comDto.setSc006RiyouStartDayOneDisableFlg(state);
		// 区画1クリア
		comDto.setClearParking1DisableFlg(state);
		// 利用終了日1
		comDto.setSc006RiyouEndDayOneDisableFlg(state);
		// 駐車場入力支援（区画2）
		comDto.setParkingShien2DisableFlg(state);
		// 利用開始日2
		comDto.setSc006RiyouStartDayTwoDisableFlg(state);
		// 区画2クリア
		comDto.setClearParking2DisableFlg(state);
		// 利用終了日2
		comDto.setSc006RiyouEndDayTwoDisableFlg(state);
		// 社宅情報:駐車場使用料調整金額
		comDto.setSc006TyusyaTyoseiPayDisableFlg(state);
		// 社宅情報:備考
		comDto.setSc006BicouDisableFlg(state);
		/** 備品情報 */
		// 貸与日
		comDto.setSc006TaiyoDayDisableFlg(state);
		// 返却日
		comDto.setSc006HenkyakuDayDisableFlg(state);
		// 搬入希望日
		comDto.setSc006KibouDayInDisableFlg(state);
		// 搬入希望時間帯
		comDto.setSc006KibouTimeInSelectDisableFlg(state);
		// 搬入本人連絡先
		comDto.setSc006HonninAddrInDisableFlg(state);
		// 受取代理人
		comDto.setSc006UketoriDairiInNameDisableFlg(state);
		// 搬入社員入力支援
		comDto.setSc006UketoriDairiInShienDisableFlg(state);
		// 受取代理人連絡先
		comDto.setSc006UketoriDairiAddrDisableFlg(state);
		// 搬出希望日
		comDto.setSc006KibouDayOutDisableFlg(state);
		// 搬出希望日時時間帯
		comDto.setSc006KibouTimeOutSelectDisableFlg(state);
		// 搬出本人連絡先
		comDto.setSc006HonninAddrOutDisableFlg(state);
		// 搬出立会代理人
		comDto.setSc006TachiaiDairiDisableFlg(state);
		// 搬出社員入力支援
		comDto.setSc006TachiaiDairiShienDisableFlg(state);
		// 搬出立会代理人連絡先
		comDto.setSc006TachiaiDairiAddrDisableFlg(state);
		// 代理人備考
		comDto.setSc006DairiBikoDisableFlg(state);
		// 備品備考
		comDto.setSc006BihinBikoDisableFlg(state);
		/** 相互利用/役員情報 */
		// 貸付会社
		comDto.setSc006TaiyoKaisyaSelectDisableFlg(state);
		// 借受会社
		comDto.setSc006KariukeKaisyaSelectDisableFlg(state);
		// 出向の有無(相互利用状況)
		comDto.setSc006SogoRyojokyoSelectDisableFlg(state);
		// 相互利用判定区分
		comDto.setSc006SogoHanteiKbnSelectDisableFlg(state);
		// 社宅使用料会社間送金区分
		comDto.setSc006SokinShatakuSelectDisableFlg(state);
		// 送金区分（共益費）
		comDto.setSc006SokinKyoekihiSelectDisableFlg(state);
		// 社宅賃貸料
		comDto.setSc006ChintaiRyoDisableFlg(state);
		// 駐車場料金
		comDto.setSc006TyusyajoRyokinDisableFlg(state);
		// 共益費(事業者負担)
		comDto.setSc006KyoekihiDisableFlg(state);
		// 開始日
		comDto.setSc006StartDayDisableFlg(state);
		// 終了日
		comDto.setSc006EndDayDisableFlg(state);
		// 配属会社名
		comDto.setSc006HaizokuKaisyaSelectDisableFlg(state);
		// 所属機関
		comDto.setSc006SyozokuKikanDisableFlg(state);
		// 室・部名
		comDto.setSc006SituBuNameDisableFlg(state);
		// 課等名
		comDto.setSc006KanadoMeiDisableFlg(state);
		// 配属データコード番号
		comDto.setSc006HaizokuNoDisableFlg(state);
		/** 下部ボタン */
		// 運用ガイドボタン
		comDto.setBtnUnyonGuideDisableFlg(state);
		// 一時保存ボタン
		comDto.setBtnTmpSaveDisableFlg(state);
		// 作成完了ボタン
		comDto.setBtnCreateDisableFlg(state);
		// 次月予約ボタン
		comDto.setBtnJigetuYoyakuDisableFlg(state);
		// 台帳登録ボタン
		comDto.setBtnShatakuLoginDisableFlg(state);
		// 継続登録ボタン
		comDto.setBtnKeizokuLoginDisableFlg(state);
	}

	/**
	 * 使用料計算3種実施
	 * パラメータにより最大3種(社宅、駐車場1、駐車場2)の使用料計算を実施する
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param asyncDto		*DTO
	 * @return
	 * @throws Exception
	 */
	public Boolean threeShiyouryoCalcAsync(Skf3022Sc006CommonAsyncDto asyncDto) throws Exception {

		/** パラメータ取得 */
		// 使用料計算用Map
		Map<String, String> paramMap = asyncDto.getMapParam();
		// 戻り値初期化
		initializeSiyoryoKeiSanParamAsync(asyncDto);
		// 使用料計算処理
		Map<String, String> resultMap = new HashMap<String, String>();	// 使用料計算戻り値
		StringBuffer errMsg = new StringBuffer();						// エラーメッセージ
		if (siyoryoKeiSan("", "", paramMap, resultMap, errMsg)) {
			// 使用料計算でエラー
			ServiceHelper.addErrorResultMessage(asyncDto, null, MessageIdConstant.SKF3020_ERR_MSG_COMMON, errMsg);
			LogUtils.debugByMsg("社宅使用料計算でエラー:" + errMsg);
			return false;
		} else {
			// 使用料計算戻り値設定
			setSiyoryoKeiSanParamAsync(resultMap, asyncDto);
		}
		// 駐車場１の使用料を再計算
		if (!CheckUtils.isEmpty(asyncDto.getSc006KukakuNoOne())) {
			if (CheckUtils.isEmpty(asyncDto.getHdnChushajoNoOne())) {
				LogUtils.debugByMsg("駐車場１(old)の使用料計算");
				if (siyoryoKeiSan(asyncDto.getHdnChushajoNoOneOld(), "1", paramMap, resultMap, errMsg)) {
					// 使用料計算でエラー
					ServiceHelper.addErrorResultMessage(asyncDto, null, MessageIdConstant.SKF3020_ERR_MSG_COMMON, errMsg);
					LogUtils.debugByMsg("駐車場１(old)の使用料計算でエラー:" + errMsg);
					return false;
				}
			} else {
				LogUtils.debugByMsg("駐車場１の使用料計算");
				if (siyoryoKeiSan(asyncDto.getHdnChushajoNoOne(), "1", paramMap, resultMap, errMsg)) {
					// 使用料計算でエラー
					ServiceHelper.addErrorResultMessage(asyncDto, null, MessageIdConstant.SKF3020_ERR_MSG_COMMON, errMsg);
					LogUtils.debugByMsg("駐車場１の使用料計算でエラー:" + errMsg);
					return false;
				}
			}
			// 使用料計算戻り値設定
			setSiyoryoKeiSanParamAsync(resultMap, asyncDto);
		}
		// 駐車場1の日割金額のパラメータを戻り値で更新
		if (resultMap.containsKey("sc006TyusyaDayPayOne")) {
			paramMap.put("sc006TyusyaDayPayOne", resultMap.get("sc006TyusyaDayPayOne"));
		}
		// 駐車場２の使用料を再計算
		if (!CheckUtils.isEmpty(asyncDto.getSc006KukakuNoTwo())) {
			if (CheckUtils.isEmpty(asyncDto.getHdnChushajoNoTwo())) {
				LogUtils.debugByMsg("駐車場2(old)の使用料計算");
				if (siyoryoKeiSan(asyncDto.getHdnChushajoNoTwoOld(), "2", paramMap, resultMap, errMsg)) {
					// 使用料計算でエラー
					ServiceHelper.addErrorResultMessage(asyncDto, null, MessageIdConstant.SKF3020_ERR_MSG_COMMON, errMsg);
					LogUtils.debugByMsg("駐車場2(old)の使用料計算でエラー:" + errMsg);
					return false;
				}
			} else {
				LogUtils.debugByMsg("駐車場2の使用料計算");
				if (siyoryoKeiSan(asyncDto.getHdnChushajoNoTwo(), "2", paramMap, resultMap, errMsg)) {
					// 使用料計算でエラー
					ServiceHelper.addErrorResultMessage(asyncDto, null, MessageIdConstant.SKF3020_ERR_MSG_COMMON, errMsg);
					LogUtils.debugByMsg("駐車場2の使用料計算でエラー:" + errMsg);
					return false;
				}
			}
			// 使用料計算戻り値設定
			setSiyoryoKeiSanParamAsync(resultMap, asyncDto);
		}
		return true;
	}

	/**
	 * 備品ステータスチェック
	 * 備品の備付状態と選択貸与状態を比較しエラー判定を行う
	 * エラー検出箇所はプルダウンをエラー背景色に設定する
	 * 1件でもエラー検出時は次回表示タブを備品タブに設定する
	 * 
	 * 入居で仮社員の場合、貸与区分がなし以外は備品申請フラグを有に設定する
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param comDto	*DTO
	 * @return	仮社員備品申請フラグ(あり：true、 なし：false)
	 */
	public Boolean checkBihinTaiyoStts(Skf3022Sc006CommonDto comDto) {

		// 仮社員備品申請フラグ
		Boolean appBihinFlg = false;
		// 備品エラーフラグ
		Boolean bihinErr = false;
		// 備品貸与区分ドロップダウン
		List<Map<String, Object>> lendStatusList =
				ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_BIHINLENTSTATUS_KBN, "",false);
		// 備品情報一覧取得
		List<Map<String, Object>> bihinList = comDto.getBihinInfoListTableData();
		Boolean tmpFlg = false;
		// 仮社員番号判定
		if (Skf3022Sc006CommonDto.KARI_K.equals(comDto.getSc006ShainNo().substring(0, 1))) {
			// 仮社員フラグをtrueに設定
			tmpFlg = true;
		}
		// 備品ステータスチェック
		for (int i = 0; i < bihinList.size(); i++) {
			Map <String, Object> bihinMap = bihinList.get(i);
			// 部屋備付状態
			String heyaSonaetukeStts = bihinMap.get("heyaSonaetukeStts") != null ? bihinMap.get("heyaSonaetukeStts").toString() : "";
			// 貸与区分ドロップダウン選択値
			String bihinTaiyoSttsKbn = bihinMap.get("bihinTaiyoSttsKbn") != null ? bihinMap.get("bihinTaiyoSttsKbn").toString() : "";
			// 貸与区分エラー
			String bihinTaiyoSttsErr = "";
			// 仮社員申請判定
			if (tmpFlg && CodeConstant.BIHIN_STATE_NONE.equals(bihinTaiyoSttsKbn)
					&& CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(comDto.getHdnNyutaikyoKbn())) {
				// 申請備品有に設定
				appBihinFlg = true;
				LogUtils.debugByMsg("仮社員申請判定:申請備品有");
			}
			// 部屋備付状態判定
			switch (heyaSonaetukeStts) {
				// なし
				case CodeConstant.BIHIN_STATE_NONE:
					// 備付/共有（変更後）
					if (CodeConstant.BIHIN_STATE_SONAETSUKE.equals(bihinTaiyoSttsKbn)
							|| CodeConstant.BIHIN_STATE_KYOYO.equals(bihinTaiyoSttsKbn)) {
						bihinTaiyoSttsErr = Skf3022Sc006CommonDto.BIHIN_TAIYOSTTS_ERR;
						bihinErr = true;
						LogUtils.debugByMsg("備品備付状態判定:備付無し → 備付/共有");
					}
					break;
				// 会社保有
				case CodeConstant.BIHIN_STATE_HOYU:
					// なし／共有／備付／レンタル（変更後）
					if (CodeConstant.BIHIN_STATE_NONE.equals(bihinTaiyoSttsKbn)
							|| CodeConstant.BIHIN_STATE_KYOYO.equals(bihinTaiyoSttsKbn)
							|| CodeConstant.BIHIN_STATE_SONAETSUKE.equals(bihinTaiyoSttsKbn)
							|| CodeConstant.BIHIN_STATE_RENTAL.equals(bihinTaiyoSttsKbn)) {
						bihinTaiyoSttsErr = Skf3022Sc006CommonDto.BIHIN_TAIYOSTTS_ERR;
						bihinErr = true;
						LogUtils.debugByMsg("備品備付状態判定:会社保有 → なし／共有／備付／レンタル");
					}
					break;
				// レンタル
				case CodeConstant.BIHIN_STATE_RENTAL:
					// なし／共有／備付／会社保有（変更後）
					if (CodeConstant.BIHIN_STATE_NONE.equals(bihinTaiyoSttsKbn)
							|| CodeConstant.BIHIN_STATE_KYOYO.equals(bihinTaiyoSttsKbn)
							|| CodeConstant.BIHIN_STATE_SONAETSUKE.equals(bihinTaiyoSttsKbn)
							|| CodeConstant.BIHIN_STATE_HOYU.equals(bihinTaiyoSttsKbn)) {
						bihinTaiyoSttsErr = Skf3022Sc006CommonDto.BIHIN_TAIYOSTTS_ERR;
						bihinErr = true;
						LogUtils.debugByMsg("備品備付状態判定:レンタル → なし／共有／備付／会社保有");
					}
					break;
				// 備付
				case CodeConstant.BIHIN_STATE_SONAETSUKE:
					// なし／共有／会社保有／レンタル（変更後）
					if (CodeConstant.BIHIN_STATE_NONE.equals(bihinTaiyoSttsKbn)
							|| CodeConstant.BIHIN_STATE_KYOYO.equals(bihinTaiyoSttsKbn)
							|| CodeConstant.BIHIN_STATE_HOYU.equals(bihinTaiyoSttsKbn)
							|| CodeConstant.BIHIN_STATE_RENTAL.equals(bihinTaiyoSttsKbn)) {
						bihinTaiyoSttsErr = Skf3022Sc006CommonDto.BIHIN_TAIYOSTTS_ERR;
						bihinErr = true;
						LogUtils.debugByMsg("備品備付状態判定:備付 → なし／共有／会社保有／レンタル");
					}
					break;
				// 共有
				case CodeConstant.BIHIN_STATE_KYOYO:
					// 備付／なし
					if (CodeConstant.BIHIN_STATE_SONAETSUKE.equals(bihinTaiyoSttsKbn)
							|| CodeConstant.BIHIN_STATE_NONE.equals(bihinTaiyoSttsKbn)) {
						bihinTaiyoSttsErr = Skf3022Sc006CommonDto.BIHIN_TAIYOSTTS_ERR;
						bihinErr = true;
						LogUtils.debugByMsg("備品備付状態判定:共有 → 備付／なし");
					}
					break;
			};
			// 備品貸与ドロップダウン作成
			String bihinTaiyoSttsCode = createStatusSelect(bihinTaiyoSttsKbn, lendStatusList);
			bihinMap.put("bihinTaiyoStts", "<select id='bihinTaiyoStts" + i 
					+ "' name='bihinTaiyoStts" + i + "' style='width:90px;'"
					+ bihinTaiyoSttsErr + ">" + bihinTaiyoSttsCode + "</select>");
		}

		// エラー存在判定
		if (bihinErr) {
			// エラーが存在する場合は表示タブインデックスを備品タブに設定
			setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_BIHIN, comDto);
		}
		comDto.setBihinInfoListTableData(bihinList);
		return appBihinFlg;
	}

	/**
	 * 入居入力チェック
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param tmpSaveFlg	一時保存フラグ
	 * @param bihinErrFlg	備品貸与選択値エラーフラグ
	 * @param bihinFlg		備品申請フラグ
	 * @param comDto		*DTO
	 * @return	true:エラー、false：正常
	 */
	public Boolean checkForNyukyo(
			Boolean tmpSaveFlg, Boolean bihinErrFlg, Boolean bihinFlg, Skf3022Sc006CommonDto comDto) {

		Boolean errorFlg = false;

		// 備品エラー判定
		if (bihinErrFlg) {
			// エラーメッセージ設定：{0}に誤りがあります。入力内容を確認してください。
			ServiceHelper.addErrorResultMessage(
					comDto, null, MessageIdConstant.E_SKF_1074, Skf3022Sc006CommonDto.MSG_BIHIN_TAIYO_STTS);
			errorFlg = true;
			setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_BIHIN, comDto);
		}
		// 一時保存判定
		if (!tmpSaveFlg) {
			/** 一時保存以外 */
			// 社宅名
			if (CheckUtils.isEmpty(comDto.getSc006ShatakuName())) {
				// エラーメッセージ設定：{0}が未入力です。
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "社宅名");
				errorFlg = true;
				LogUtils.debugByMsg("入居入力チェック：一時保存以外、社宅名未設定");
			}
			// 貸与規格（ヘッダ項目）
			if (CheckUtils.isEmpty(comDto.getSc006SiyoryoPatName())) {
				// エラーメッセージ設定：{0}が未入力です。
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "貸与規格");
				errorFlg = true;
				LogUtils.debugByMsg("入居入力チェック：一時保存以外、貸与規格(ヘッダ)未設定");
			}
			// 原籍会社
			if (CheckUtils.isEmpty(comDto.getSc006OldKaisyaNameSelect()) && !comDto.getSc006OldKaisyaNameSelectDisableFlg()) {
				// エラーメッセージ設定：{0}が未入力です。
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "原籍会社");
				comDto.setSc006OldKaisyaNameSelectErr(CodeConstant.NFW_VALIDATION_ERROR);
				errorFlg = true;
				// 表示タブを社宅情報タブに設定
				setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_SHATAKU, comDto);
				LogUtils.debugByMsg("入居入力チェック：一時保存以外、原籍会社未設定");
			}
			// 給与支給会社名
			if (CheckUtils.isEmpty(comDto.getSc006KyuyoKaisyaSelect()) && !comDto.getSc006KyuyoKaisyaSelectDisableFlg()) {
				// エラーメッセージ設定：{0}が未入力です。
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "給与支給会社名");
				comDto.setSc006KyuyoKaisyaSelectErr(CodeConstant.NFW_VALIDATION_ERROR);
				errorFlg = true;
				// 表示タブを社宅情報タブに設定
				setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_SHATAKU, comDto);
				LogUtils.debugByMsg("入居入力チェック：一時保存以外、給与会社未設定");
			}
			// 入居予定日
			if (CheckUtils.isEmpty(comDto.getSc006NyukyoYoteiDay()) && !comDto.getSc006NyukyoYoteiDayDisableFlg()) {
				// エラーメッセージ設定：{0}が未入力です。
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "入居予定日");
				comDto.setSc006NyukyoYoteiDayErr(CodeConstant.NFW_VALIDATION_ERROR);
				errorFlg = true;
				// 表示タブを社宅情報タブに設定
				setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_SHATAKU, comDto);
				LogUtils.debugByMsg("入居入力チェック：一時保存以外、入居予定日未設定");
			}
			// 役員算定が役員／執行役員、又は、相互利用情報がありの場合
			if (CodeConstant.YAKUIN_KBN_YAKUIN.equals(comDto.getSc006YakuinSanteiSelect())
					|| CodeConstant.YAKUIN_KBN_SHIKKO.equals(comDto.getSc006YakuinSanteiSelect())
					|| CodeConstant.MUTUAL_USE_KBN_AVAILABLE.equals(comDto.getSc006SogoRyojokyoSelect())) {
				// 社宅賃貸料
				if (CheckUtils.isEmpty(comDto.getSc006ChintaiRyo()) && !comDto.getSc006ChintaiRyoDisableFlg()) {
					// エラーメッセージ設定：{0}が未入力です。
					ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "社宅賃貸料");
					comDto.setSc006ChintaiRyoErr(CodeConstant.NFW_VALIDATION_ERROR);
					errorFlg = true;
					// 表示タブを役員情報/相互利用情報タブに設定
					setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_YAKUIN, comDto);
					LogUtils.debugByMsg("入居入力チェック：一時保存以外、役員算定が役員/執行役員または出向(相互利用状況)あり、社宅賃貸料未設定");
				}
				// 駐車場料金
				if (CheckUtils.isEmpty(comDto.getSc006TyusyajoRyokin()) && !comDto.getSc006TyusyajoRyokinDisableFlg()) {
					// エラーメッセージ設定：{0}が未入力です。
					ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "駐車場料金");
					comDto.setSc006TyusyajoRyokinErr(CodeConstant.NFW_VALIDATION_ERROR);
					errorFlg = true;
					// 表示タブを役員情報/相互利用情報タブに設定
					setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_YAKUIN, comDto);
					LogUtils.debugByMsg("入居入力チェック：一時保存以外、役員算定が役員/執行役員または出向(相互利用状況)あり、社宅賃貸料未設定");
				}
			}
			// 相互利用状況が”あり”の場合
			if (CodeConstant.MUTUAL_USE_KBN_AVAILABLE.equals(comDto.getSc006SogoRyojokyoSelect())) {
				// 相互利用判定区分
				if (CheckUtils.isEmpty(comDto.getSc006SogoHanteiKbnSelect()) && !comDto.getSc006SogoHanteiKbnSelectDisableFlg()) {
					// エラーメッセージ設定：{0}が未入力です。
					ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "相互利用判定区分");
					comDto.setSc006SogoHanteiKbnSelectErr(CodeConstant.NFW_VALIDATION_ERROR);
					errorFlg = true;
					// 表示タブを役員情報/相互利用情報タブに設定
					setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_YAKUIN, comDto);
					LogUtils.debugByMsg("入居入力チェック：一時保存以外、出向(相互利用状況)あり、相互利用判定区分未設定");
				} else {
					// 相互利用判定区分が”あり”の場合
					if (CodeConstant.MUTUAL_USE_KBN_AVAILABLE.equals(comDto.getSc006SogoHanteiKbnSelect())) {
						// 貸付会社
						if (CheckUtils.isEmpty(comDto.getSc006TaiyoKaisyaSelect()) && !comDto.getSc006TaiyoKaisyaSelectDisableFlg()) {
							// エラーメッセージ設定：{0}が未入力です。
							ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "貸付会社");
							comDto.setSc006TaiyoKaisyaSelectErr(CodeConstant.NFW_VALIDATION_ERROR);
							errorFlg = true;
							// 表示タブを役員情報/相互利用情報タブに設定
							setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_YAKUIN, comDto);
							LogUtils.debugByMsg("入居入力チェック：一時保存以外、出向(相互利用状況)あり、相互利用判定区分あり、貸付会社未設定");
						}
						// 借受会社
						if (CheckUtils.isEmpty(comDto.getSc006KariukeKaisyaSelect()) && !comDto.getSc006KariukeKaisyaSelectDisableFlg()) {
							// エラーメッセージ設定：{0}が未入力です。
							ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "借受会社");
							comDto.setSc006KariukeKaisyaSelectErr(CodeConstant.NFW_VALIDATION_ERROR);
							errorFlg = true;
							// 表示タブを役員情報/相互利用情報タブに設定
							setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_YAKUIN, comDto);
							LogUtils.debugByMsg("入居入力チェック：一時保存以外、出向(相互利用状況)あり、相互利用判定区分あり、借受会社未設定");
						}
						// 開始日
						if (CheckUtils.isEmpty(comDto.getSc006StartDay()) && !comDto.getSc006StartDayDisableFlg()) {
							// エラーメッセージ設定：{0}が未入力です。
							ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "開始日");
							comDto.setSc006StartDayErr(CodeConstant.NFW_VALIDATION_ERROR);
							errorFlg = true;
							// 表示タブを役員情報/相互利用情報タブに設定
							setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_YAKUIN, comDto);
							LogUtils.debugByMsg("入居入力チェック：一時保存以外、出向(相互利用状況)あり、相互利用判定区分あり、開始日未設定");
						}
					}
				}
				// 会社間送金区分（社宅使用料）
				if (CheckUtils.isEmpty(comDto.getSc006SokinShatakuSelect()) && !comDto.getSc006SokinShatakuSelectDisableFlg()) {
					// エラーメッセージ設定：{0}が未入力です。
					ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "社宅使用料会社間送金区分");
					comDto.setSc006SokinShatakuSelectErr(CodeConstant.NFW_VALIDATION_ERROR);
					errorFlg = true;
					// 表示タブを役員情報/相互利用情報タブに設定
					setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_YAKUIN, comDto);
					LogUtils.debugByMsg("入居入力チェック：一時保存以外、出向(相互利用状況)あり、会社間送金区分（社宅使用料）未設定");
				}
				// 会社間送金区分（共益費）
				if (CheckUtils.isEmpty(comDto.getSc006SokinKyoekihiSelect()) && !comDto.getSc006SokinKyoekihiSelectDisableFlg()) {
					// エラーメッセージ設定：{0}が未入力です。
					ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "共益費会社間送金区分");
					comDto.setSc006SokinKyoekihiSelectErr(CodeConstant.NFW_VALIDATION_ERROR);
					errorFlg = true;
					// 表示タブを役員情報/相互利用情報タブに設定
					setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_YAKUIN, comDto);
					LogUtils.debugByMsg("入居入力チェック：一時保存以外、出向(相互利用状況)あり、会社間送金区分（共益費）未設定");
				}
				// 共益費（事業者負担）
				if (CheckUtils.isEmpty(comDto.getSc006Kyoekihi()) && !comDto.getSc006KyoekihiDisableFlg()) {
					// エラーメッセージ設定：{0}が未入力です。
					ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "共益費（事業者負担）");
					comDto.setSc006KyoekihiErr(CodeConstant.NFW_VALIDATION_ERROR);
					errorFlg = true;
					// 表示タブを役員情報/相互利用情報タブに設定
					setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_YAKUIN, comDto);
					LogUtils.debugByMsg("入居入力チェック：一時保存以外、出向(相互利用状況)あり、共益費（事業者負担）未設定");
				}
			}
			// 駐車場区画番号１と利用開始日１いずれかに入力された場合
			if (!CheckUtils.isEmpty(comDto.getSc006KukakuNoOne()) || !CheckUtils.isEmpty(comDto.getSc006RiyouStartDayOne())) {
				// 利用開始日１
				if (CheckUtils.isEmpty(comDto.getSc006RiyouStartDayOne()) && !comDto.getSc006RiyouStartDayOneDisableFlg()) {
					// エラーメッセージ設定：{0}が未入力です。
					ServiceHelper.addErrorResultMessage(
							comDto, null, MessageIdConstant.E_SKF_1048, Skf3022Sc006CommonDto.MSG_RIYOU_START_DAY_ONE);
					comDto.setSc006RiyouStartDayOneErr(CodeConstant.NFW_VALIDATION_ERROR);
					errorFlg = true;
					// 表示タブを役員情報/相互利用情報タブに設定
					setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_SHATAKU, comDto);
					LogUtils.debugByMsg("入居入力チェック：一時保存以外、駐車場区画番号１設定あり、利用開始日1未設定");
				}
				// 駐車場区画番号１
				if (CheckUtils.isEmpty(comDto.getSc006KukakuNoOne())) {
					// エラーメッセージ設定：{0}が未入力です。
					ServiceHelper.addErrorResultMessage(
							comDto, null, MessageIdConstant.E_SKF_1048, Skf3022Sc006CommonDto.MSG_KUKAKU_NO_ONE);
					errorFlg = true;
					// 表示タブを社宅情報タブに設定
					setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_SHATAKU, comDto);
					LogUtils.debugByMsg("入居入力チェック：一時保存以外、利用開始日１設定あり、駐車場区画番号１未設定");
				}
			}
			// 駐車場区画番号２と利用開始日２いずれかに入力された場合
			if (!CheckUtils.isEmpty(comDto.getSc006KukakuNoTwo()) || !CheckUtils.isEmpty(comDto.getSc006RiyouStartDayTwo())) {
				// 利用開始日2
				if (CheckUtils.isEmpty(comDto.getSc006RiyouStartDayTwo()) && !comDto.getSc006RiyouStartDayTwoDisableFlg()) {
					// エラーメッセージ設定：{0}が未入力です。
					ServiceHelper.addErrorResultMessage(
							comDto, null, MessageIdConstant.E_SKF_1048, Skf3022Sc006CommonDto.MSG_RIYOU_START_DAY_TWO);
					comDto.setSc006RiyouStartDayTwoErr(CodeConstant.NFW_VALIDATION_ERROR);
					errorFlg = true;
					// 表示タブを役員情報/相互利用情報タブに設定
					setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_SHATAKU, comDto);
					LogUtils.debugByMsg("入居入力チェック：一時保存以外、駐車場区画番号2設定あり、利用開始日2未設定");
				}
				// 駐車場区画番号2
				if (CheckUtils.isEmpty(comDto.getSc006KukakuNoTwo())) {
					// エラーメッセージ設定：{0}が未入力です。
					ServiceHelper.addErrorResultMessage(
							comDto, null, MessageIdConstant.E_SKF_1048, Skf3022Sc006CommonDto.MSG_KUKAKU_NO_TWO);
					errorFlg = true;
					// 表示タブを社宅情報タブに設定
					setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_SHATAKU, comDto);
					LogUtils.debugByMsg("入居入力チェック：一時保存以外、利用開始日2設定あり、駐車場区画番号2未設定");
				}
			}
			// 貸与日
			if (bihinFlg) {
				if (CheckUtils.isEmpty(comDto.getSc006TaiyoDay()) && !comDto.getSc006TaiyoDayDisableFlg()) {
					// エラーメッセージ設定：{0}が未入力です。
					ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "貸与日");
					comDto.setSc006TaiyoDayErr(CodeConstant.NFW_VALIDATION_ERROR);
					errorFlg = true;
					// 表示タブを備品情報タブに設定
					setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_BIHIN, comDto);
					LogUtils.debugByMsg("入居入力チェック：一時保存以外、備品申請フラグture、貸与日未設定");
				}
			}
			// 貸与日あり、継続フラグ「false」
			if (!CheckUtils.isEmpty(comDto.getSc006TaiyoDay()) && !comDto.getHdnKeizokuBtnFlg()) {
				// 搬入希望日
				if (CheckUtils.isEmpty(comDto.getSc006KibouDayIn()) && !comDto.getSc006KibouDayInDisableFlg()) {
					// エラーメッセージ設定：{0}が未入力です。
					ServiceHelper.addErrorResultMessage(
							comDto, null, MessageIdConstant.E_SKF_1048, Skf3022Sc006CommonDto.MSG_KIBOU_DAY_IN);
					comDto.setSc006KibouDayInErr(CodeConstant.NFW_VALIDATION_ERROR);
					errorFlg = true;
					// 表示タブを備品情報タブに設定
					setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_BIHIN, comDto);
					LogUtils.debugByMsg("入居入力チェック：一時保存以外、貸与日あり、搬入希望日未設定");
				}
				// 搬入本人連絡先
				if (CheckUtils.isEmpty(comDto.getSc006HonninAddrIn()) && !comDto.getSc006HonninAddrInDisableFlg()) {
					// エラーメッセージ設定：{0}が未入力です。
					ServiceHelper.addErrorResultMessage(
							comDto, null, MessageIdConstant.E_SKF_1048, Skf3022Sc006CommonDto.MSG_HONNIN_ADDR_IN);
					comDto.setSc006HonninAddrInErr(CodeConstant.NFW_VALIDATION_ERROR);
					errorFlg = true;
					// 表示タブを備品情報タブに設定
					setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_BIHIN, comDto);
					LogUtils.debugByMsg("入居入力チェック：一時保存以外、貸与日あり、搬入本人連絡先未設定");
				}
			}
			// 社宅使用料調整金額
			if (!CheckUtils.isEmpty(comDto.getSc006SiyoroTyoseiPay()) && !comDto.getSc006SiyoroTyoseiPayDisableFlg()) {
				// 社宅使用料日割金額
				int hiwariPay = 0;
				if (!CheckUtils.isEmpty(comDto.getSc006SiyoryoHiwariPay())) {
					hiwariPay = Integer.parseInt(getKingakuText(comDto.getSc006SiyoryoHiwariPay()));
				}
				// 社宅使用料日割金額
				int tyoseiPay = 0;
				if (!CheckUtils.isEmpty(comDto.getSc006SiyoroTyoseiPay())) {
					tyoseiPay = Integer.parseInt(getKingakuText(comDto.getSc006SiyoroTyoseiPay()));
				}
				if ((hiwariPay + tyoseiPay) < 0) {
					// エラーメッセージ設定：{0}に誤りがあります。入力内容を確認してください。
					ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1074, "社宅使用料調整金額");
					comDto.setSc006SiyoroTyoseiPayErr(CodeConstant.NFW_VALIDATION_ERROR);
					errorFlg = true;
					// 表示タブを社宅情報タブに設定
					setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_SHATAKU, comDto);
					LogUtils.debugByMsg("入居入力チェック：一時保存以外、社宅使用料調整金額あり、社宅使用料調整金額不正");
				}
			}
			// 個人負担共益費調整金額
			if (!CheckUtils.isEmpty(comDto.getSc006KyoekihiTyoseiPay()) && !comDto.getSc006KyoekihiTyoseiPayDisableFlg()) {
				// 個人負担共益費月額 
				int KyoekiMonthPay = 0;
				if (!CheckUtils.isEmpty(comDto.getSc006KyoekihiMonthPay())) {
					KyoekiMonthPay = Integer.parseInt(getKingakuText(comDto.getSc006KyoekihiMonthPay()));
				}
				// 個人負担共益費調整金額
				int tyoseiKyoekiPay = 0;
				if (!CheckUtils.isEmpty(comDto.getSc006KyoekihiTyoseiPay())) {
					tyoseiKyoekiPay = Integer.parseInt(getKingakuText(comDto.getSc006KyoekihiTyoseiPay()));
				}
				if ((KyoekiMonthPay + tyoseiKyoekiPay) < 0) {
					// エラーメッセージ設定：{0}に誤りがあります。入力内容を確認してください。
					ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1074, "個人負担共益費調整金額");
					comDto.setSc006KyoekihiTyoseiPayErr(CodeConstant.NFW_VALIDATION_ERROR);
					errorFlg = true;
					// 表示タブを社宅情報タブに設定
					setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_SHATAKU, comDto);
					LogUtils.debugByMsg("入居入力チェック：一時保存以外、個人負担共益費調整金額あり、個人負担共益費調整金額不正");
				}
			}
			// 駐車場使用料調整金額
			if (!CheckUtils.isEmpty(comDto.getSc006TyusyaTyoseiPay()) && !comDto.getSc006TyusyaTyoseiPayDisableFlg()) {
				// 駐車場１日割金額
				int parkingOnePay = 0;
				if (!CheckUtils.isEmpty(comDto.getSc006TyusyaDayPayOne())) {
					parkingOnePay = Integer.parseInt(getKingakuText(comDto.getSc006TyusyaDayPayOne()));
				}
				// 駐車場２日割金額
				int parkingTwoPay = 0;
				if (!CheckUtils.isEmpty(comDto.getSc006TyusyaDayPayTwo())) {
					parkingTwoPay = Integer.parseInt(getKingakuText(comDto.getSc006TyusyaDayPayTwo()));
				}
				// 駐車場調整金額
				int parkingTyoseiPay = 0;
				if (!CheckUtils.isEmpty(comDto.getSc006TyusyaTyoseiPay())) {
					parkingTyoseiPay = Integer.parseInt(getKingakuText(comDto.getSc006TyusyaTyoseiPay()));
					if ((parkingOnePay + parkingTwoPay + parkingTyoseiPay) < 0) {
						// エラーメッセージ設定：{0}に誤りがあります。入力内容を確認してください。
						ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1074, "駐車場使用料調整金額");
						comDto.setSc006TyusyaTyoseiPayErr(CodeConstant.NFW_VALIDATION_ERROR);
						errorFlg = true;
						// 表示タブを社宅情報タブに設定
						setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_SHATAKU, comDto);
						LogUtils.debugByMsg("入居入力チェック：一時保存以外、駐車場使用料調整金額あり、駐車場使用料調整金額不正");
					}
				}
			}
		}
		// 区画１と区画２一致チェック
		/* US imart移植 2019.12.09 区画番号比較ではなく、区画管理番号比較に修正 */
//		if (!CheckUtils.isEmpty(comDto.getSc006KukakuNoOne()) && !CheckUtils.isEmpty(comDto.getSc006KukakuNoTwo())) {
//			if (comDto.getSc006KukakuNoOne().equals(comDto.getSc006KukakuNoTwo())) {
		if (!CheckUtils.isEmpty(comDto.getHdnChushajoNoOne()) && !CheckUtils.isEmpty(comDto.getHdnChushajoNoTwo())) {
			if (Objects.equals(comDto.getHdnChushajoNoOne(), comDto.getHdnChushajoNoTwo())) {
		/* UE imart移植 2019.12.09 区画番号比較ではなく、区画管理番号比較に修正 */
				// 区画１と区画２の区画番号が重複しています。
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_3047);
				errorFlg = true;
				// 表示タブを社宅情報タブに設定
				setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_SHATAKU, comDto);
				LogUtils.debugByMsg("区画1、区画2が同一番号");
			}
		}
		return errorFlg;
	}

	/**
	 * 退居入力チェック
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param tmpSaveFlg	一時保存フラグ
	 * @param bihinErrFlg	備品貸与選択値エラーフラグ
	 * @param comDto		*DTO
	 * @return				true:エラー、false：正常
	 * @throws ParseException
	 */
	public Boolean checkForTaikyo(
			Boolean tmpSaveFlg, Boolean bihinErrFlg, Skf3022Sc006CommonDto comDto) throws ParseException {

		Boolean errorFlg = false;

		// 備品エラー判定
		if (bihinErrFlg) {
			// エラーメッセージ設定：{0}に誤りがあります。入力内容を確認してください。
			ServiceHelper.addErrorResultMessage(
					comDto, null, MessageIdConstant.E_SKF_1074, Skf3022Sc006CommonDto.MSG_BIHIN_TAIYO_STTS);
			errorFlg = true;
			setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_BIHIN, comDto);
		}
		// 一時保存判定
		if (!tmpSaveFlg) {
			/** 一時保存以外 */
			// 退居予定日
			if (CheckUtils.isEmpty(comDto.getSc006TaikyoYoteiDay()) && !comDto.getSc006TaikyoYoteiDayDisableFlg()) {
				// エラーメッセージ設定：{0}が未入力です。
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "退居予定日");
				comDto.setSc006TaikyoYoteiDayErr(CodeConstant.NFW_VALIDATION_ERROR);
				errorFlg = true;
				// 表示タブを社宅情報タブに設定
				setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_SHATAKU, comDto);
				LogUtils.debugByMsg("退居入力チェック：一時保存以外、退居予定日未設定");
			} else if (!comDto.getSc006TaikyoYoteiDayDisableFlg()) {
				// 日付整合性チェック
				if (validateDateCorrelation(comDto.getSc006NyukyoYoteiDay(), comDto.getSc006TaikyoYoteiDay())) {
					// 日付整合性チェックエラー
					// エラーメッセージ設定：{0}は開始と終了を正しく入力してください。
					ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1133, "退居予定日");
					comDto.setSc006TaikyoYoteiDayErr(CodeConstant.NFW_VALIDATION_ERROR);
					errorFlg = true;
					// 表示タブを社宅情報タブに設定
					setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_SHATAKU, comDto);
					LogUtils.debugByMsg("退居入力チェック：一時保存以外、退居予定日不正(入居日より過去)");
				}
			}
			// 区画１利用終了日
			if (!CheckUtils.isEmpty(comDto.getSc006RiyouStartDayOne())) {
				if (CheckUtils.isEmpty(comDto.getSc006RiyouEndDayOne()) && !comDto.getSc006RiyouEndDayOneDisableFlg()) {
					// エラーメッセージ設定：{0}が未入力です。
					ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048,
														Skf3022Sc006CommonDto.MSG_RIYOU_END_DAY_ONE);
					comDto.setSc006RiyouEndDayOneErr(CodeConstant.NFW_VALIDATION_ERROR);
					errorFlg = true;
					// 表示タブを社宅情報タブに設定
					setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_SHATAKU, comDto);
					LogUtils.debugByMsg("退居入力チェック：一時保存以外、区画1利用開始日あり、区画１利用終了日未設定");
				} else if (!comDto.getSc006RiyouEndDayOneDisableFlg()) {
					// 日付整合性チェック
					if (validateDateCorrelation(comDto.getSc006RiyouStartDayOne(), comDto.getSc006RiyouEndDayOne())) {
						// エラーメッセージ設定：{0}は開始と終了を正しく入力してください。
						ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1133,
															Skf3022Sc006CommonDto.MSG_RIYOU_END_DAY_ONE);
						comDto.setSc006RiyouEndDayOneErr(CodeConstant.NFW_VALIDATION_ERROR);
						errorFlg = true;
						// 表示タブを社宅情報タブに設定
						setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_SHATAKU, comDto);
						LogUtils.debugByMsg("退居入力チェック：一時保存以外、区画1利用開始あり、区画１利用終了日不正(利用開始日より過去)");
					}
				}
			}
			// 区画２利用終了日
			if (!CheckUtils.isEmpty(comDto.getSc006RiyouStartDayTwo())) {
				if (CheckUtils.isEmpty(comDto.getSc006RiyouEndDayTwo()) && !comDto.getSc006RiyouEndDayTwoDisableFlg()) {
					// エラーメッセージ設定：{0}が未入力です。
					ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048,
														Skf3022Sc006CommonDto.MSG_RIYOU_END_DAY_TWO);
					comDto.setSc006RiyouEndDayTwoErr(CodeConstant.NFW_VALIDATION_ERROR);
					errorFlg = true;
					// 表示タブを社宅情報タブに設定
					setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_SHATAKU, comDto);
					LogUtils.debugByMsg("退居入力チェック：一時保存以外、区画2利用開始日あり、区画2利用終了日未設定");
				} else if (!comDto.getSc006RiyouEndDayTwoDisableFlg()) {
					// 日付整合性チェック
					if (validateDateCorrelation(comDto.getSc006RiyouStartDayTwo(), comDto.getSc006RiyouEndDayTwo())) {
						// エラーメッセージ設定：{0}は開始と終了を正しく入力してください。
						ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1133,
															Skf3022Sc006CommonDto.MSG_RIYOU_END_DAY_TWO);
						comDto.setSc006RiyouEndDayTwoErr(CodeConstant.NFW_VALIDATION_ERROR);
						errorFlg = true;
						// 表示タブを社宅情報タブに設定
						setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_SHATAKU, comDto);
						LogUtils.debugByMsg("退居入力チェック：一時保存以外、区画2利用開始日あり、区画2利用終了日不正(利用開始日より過去)");
					}
				}
			}
			// 返却日
			if (!CheckUtils.isEmpty(comDto.getSc006TaiyoDay())) {
				if (CheckUtils.isEmpty(comDto.getSc006HenkyakuDay()) && !comDto.getSc006HenkyakuDayDisableFlg()) {
					// エラーメッセージ設定：{0}が未入力です。
					ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "返却日");
					comDto.setSc006HenkyakuDayErr(CodeConstant.NFW_VALIDATION_ERROR);
					errorFlg = true;
					// 表示タブを備品情報タブに設定
					setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_BIHIN, comDto);
					LogUtils.debugByMsg("退居入力チェック：一時保存以外、貸与日あり、返却日未設定");
				} else if (!comDto.getSc006HenkyakuDayDisableFlg()) {
					// 搬出希望日
					if (CheckUtils.isEmpty(comDto.getSc006KibouDayOut()) && !comDto.getSc006KibouDayOutDisableFlg()) {
						// エラーメッセージ設定：{0}が未入力です。
						ServiceHelper.addErrorResultMessage(
								comDto, null, MessageIdConstant.E_SKF_1048, Skf3022Sc006CommonDto.MSG_KIBOU_DAY_OUT);
						comDto.setSc006KibouDayOutErr(CodeConstant.NFW_VALIDATION_ERROR);
						errorFlg = true;
						// 表示タブを備品情報タブに設定
						setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_BIHIN, comDto);
						LogUtils.debugByMsg("退居入力チェック：一時保存以外、貸与日あり、搬出希望日未設定");
					}
					// 搬出本人連絡先
					if (CheckUtils.isEmpty(comDto.getSc006HonninAddrOut()) && !comDto.getSc006HonninAddrOutDisableFlg()) {
						// エラーメッセージ設定：{0}が未入力です。
						ServiceHelper.addErrorResultMessage(
								comDto, null, MessageIdConstant.E_SKF_1048, Skf3022Sc006CommonDto.MSG_HONNIN_ADDR_OUT);
						comDto.setSc006HonninAddrOutErr(CodeConstant.NFW_VALIDATION_ERROR);
						errorFlg = true;
						// 表示タブを備品情報タブに設定
						setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_BIHIN, comDto);
						LogUtils.debugByMsg("退居入力チェック：一時保存以外、貸与日あり、搬出本人連絡先未設定");
					}
					// 日付整合性チェック
					if (validateDateCorrelation(comDto.getSc006TaiyoDay(), comDto.getSc006HenkyakuDay())) {
						// エラーメッセージ設定：{0}は開始と終了を正しく入力してください。
						ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1133, "返却日");
						comDto.setSc006RiyouEndDayTwoErr(CodeConstant.NFW_VALIDATION_ERROR);
						errorFlg = true;
						// 表示タブを社宅情報タブに設定
						setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_SHATAKU, comDto);
						LogUtils.debugByMsg("退居入力チェック：一時保存以外、貸与日あり、返却日不正(貸与日より過去)");
					}
				}
			} else if (!CheckUtils.isEmpty(comDto.getSc006HenkyakuDay())) {
				// 搬出希望日
				if (CheckUtils.isEmpty(comDto.getSc006KibouDayOut()) && !comDto.getSc006KibouDayOutDisableFlg()) {
					// エラーメッセージ設定：{0}が未入力です。
					ServiceHelper.addErrorResultMessage(
							comDto, null, MessageIdConstant.E_SKF_1048, Skf3022Sc006CommonDto.MSG_KIBOU_DAY_OUT);
					comDto.setSc006KibouDayOutErr(CodeConstant.NFW_VALIDATION_ERROR);
					errorFlg = true;
					// 表示タブを備品情報タブに設定
					setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_BIHIN, comDto);
					LogUtils.debugByMsg("退居入力チェック：一時保存以外、貸与日無し、返却日設定あり、搬出希望日未設定");
				}
				// 搬出本人連絡先
				if (CheckUtils.isEmpty(comDto.getSc006HonninAddrOut()) && !comDto.getSc006HonninAddrOutDisableFlg()) {
					// エラーメッセージ設定：{0}が未入力です。
					ServiceHelper.addErrorResultMessage(
							comDto, null, MessageIdConstant.E_SKF_1048, Skf3022Sc006CommonDto.MSG_HONNIN_ADDR_OUT);
					comDto.setSc006HonninAddrOutErr(CodeConstant.NFW_VALIDATION_ERROR);
					errorFlg = true;
					// 表示タブを備品情報タブに設定
					setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_BIHIN, comDto);
					LogUtils.debugByMsg("退居入力チェック：一時保存以外、貸与日無し、返却日設定あり、搬出本人連絡先未設定");
				}
			}
			// 終了日
			if (!CheckUtils.isEmpty(comDto.getSc006StartDay())) {
				if (CheckUtils.isEmpty(comDto.getSc006EndDay()) && !comDto.getSc006EndDayDisableFlg()) {
					// エラーメッセージ設定：{0}が未入力です。
					ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "終了日");
					comDto.setSc006EndDayErr(CodeConstant.NFW_VALIDATION_ERROR);
					errorFlg = true;
					// 表示タブを備品情報タブに設定
					setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_YAKUIN, comDto);
					LogUtils.debugByMsg("退居入力チェック：一時保存以外、開始日あり、終了日未設定");
				} else if (!comDto.getSc006EndDayDisableFlg()) {
					// 日付整合性チェック
					if (validateDateCorrelation(comDto.getSc006StartDay(), comDto.getSc006EndDay())) {
						// エラーメッセージ設定：{0}は開始と終了を正しく入力してください。
						ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1133, "終了日");
						comDto.setSc006EndDayErr(CodeConstant.NFW_VALIDATION_ERROR);
						errorFlg = true;
						// 表示タブを社宅情報タブに設定
						setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_SHATAKU, comDto);
						LogUtils.debugByMsg("退居入力チェック：一時保存以外、開始日あり、終了日不正(開始日より過去)");
					}
				}
			}
			// 搬出希望日
			if (!CheckUtils.isEmpty(comDto.getSc006KibouDayOut())
					&& !CodeConstant.NFW_VALIDATION_ERROR.equals(comDto.getSc006KibouDayOutErr())) {
				if (!CheckUtils.isEmpty(comDto.getSc006KibouDayIn())
						&& validateDateCorrelation(comDto.getSc006KibouDayIn(), comDto.getSc006KibouDayOut())) {
					// エラーメッセージ設定：{0}は開始と終了を正しく入力してください。
					ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1133,
															Skf3022Sc006CommonDto.MSG_KIBOU_DAY_OUT);
					comDto.setSc006EndDayErr(CodeConstant.NFW_VALIDATION_ERROR);
					errorFlg = true;
					// 表示タブを備品情報タブに設定
					setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_SHATAKU, comDto);
					LogUtils.debugByMsg("退居入力チェック：一時保存以外、開始日あり、終了日不正(開始日より過去)");
				}
			}
		}
		return errorFlg;
	}

	/**
	 * 変更入力チェック
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param tmpSaveFlg	一時保存フラグ
	 * @param comDto		*DTO
	 * @return				true:エラー、false：正常
	 * @throws ParseException
	 */
	public Boolean checkForHenko(
			Boolean tmpSaveFlg, Skf3022Sc006CommonDto comDto) throws ParseException {

		Boolean errorFlg = false;

		// 一時保存判定
		if (!tmpSaveFlg) {
			// 出向(相互利用状況)が”あり”の場合
			if (CodeConstant.MUTUAL_USE_KBN_AVAILABLE.equals(comDto.getSc006SogoRyojokyoSelect())) {
				// 相合利用判定区分
				if (CheckUtils.isEmpty(comDto.getSc006SogoHanteiKbnSelect()) && !comDto.getSc006SogoHanteiKbnSelectDisableFlg()) {
					// エラーメッセージ設定：{0}が未入力です。
					ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "相互利用判定区分");
					comDto.setSc006SogoHanteiKbnSelectErr(CodeConstant.NFW_VALIDATION_ERROR);
					errorFlg = true;
					// 表示タブを役員/相互利用情報タブに設定
					setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_YAKUIN, comDto);
					LogUtils.debugByMsg("変更入力チェック：一時保存以外、出向(相互利用状況)あり、相合利用判定区分未設定");
				} else {
					// 相互利用判定区分が”あり”の場合
					if (CodeConstant.MUTUAL_USE_KBN_AVAILABLE.equals(comDto.getSc006SogoHanteiKbnSelect())) {
						// 貸付会社
						if (CheckUtils.isEmpty(comDto.getSc006TaiyoKaisyaSelect()) && !comDto.getSc006TaiyoKaisyaSelectDisableFlg()) {
							// エラーメッセージ設定：{0}が未入力です。
							ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "貸付会社");
							comDto.setSc006TaiyoKaisyaSelectErr(CodeConstant.NFW_VALIDATION_ERROR);
							errorFlg = true;
							// 表示タブを役員/相互利用情報タブに設定
							setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_YAKUIN, comDto);
							LogUtils.debugByMsg("変更入力チェック：一時保存以外、出向(相互利用状況)あり、相合利用判定区分「あり」、貸与会社未設定");
						}
						// 借受会社
						if (CheckUtils.isEmpty(comDto.getSc006KariukeKaisyaSelect()) && !comDto.getSc006KariukeKaisyaSelectDisableFlg()) {
							// エラーメッセージ設定：{0}が未入力です。
							ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "借受会社");
							comDto.setSc006KariukeKaisyaSelectErr(CodeConstant.NFW_VALIDATION_ERROR);
							errorFlg = true;
							// 表示タブを役員/相互利用情報タブに設定
							setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_YAKUIN, comDto);
							LogUtils.debugByMsg("変更入力チェック：一時保存以外、出向(相互利用状況)あり、相合利用判定区分「あり」、借受会社未設定");
						}
						// 開始日
						if (CheckUtils.isEmpty(comDto.getSc006StartDay()) && !comDto.getSc006StartDayDisableFlg()) {
							// エラーメッセージ設定：{0}が未入力です。
							ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "開始日");
							comDto.setSc006StartDayErr(CodeConstant.NFW_VALIDATION_ERROR);
							errorFlg = true;
							// 表示タブを役員/相互利用情報タブに設定
							setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_YAKUIN, comDto);
							LogUtils.debugByMsg("変更入力チェック：一時保存以外、出向(相互利用状況)あり、相合利用判定区分「あり」、開始日未設定");
						}
					}
				}
				// 社宅使用料会社間送金区分
				if (CheckUtils.isEmpty(comDto.getSc006SokinShatakuSelect()) && !comDto.getSc006SokinShatakuSelectDisableFlg()) {
					// エラーメッセージ設定：{0}が未入力です。
					ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "社宅使用料会社間送金区分");
					comDto.setSc006SokinShatakuSelectErr(CodeConstant.NFW_VALIDATION_ERROR);
					errorFlg = true;
					// 表示タブを役員/相互利用情報タブに設定
					setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_YAKUIN, comDto);
					LogUtils.debugByMsg("変更入力チェック：一時保存以外、出向(相互利用状況)あり、社宅使用料会社間送金区分未設定");
				}
				// 共益費会社間送付区分
				if (CheckUtils.isEmpty(comDto.getSc006SokinKyoekihiSelect()) && !comDto.getSc006SokinKyoekihiSelectDisableFlg()) {
					// エラーメッセージ設定：{0}が未入力です。
					ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "共益費会社間送金区分");
					comDto.setSc006SokinKyoekihiSelectErr(CodeConstant.NFW_VALIDATION_ERROR);
					errorFlg = true;
					// 表示タブを役員/相互利用情報タブに設定
					setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_YAKUIN, comDto);
					LogUtils.debugByMsg("変更入力チェック：一時保存以外、出向(相互利用状況)あり、共益費会社間送付区分未設定");
				}
				// 社宅賃貸料
				if (CheckUtils.isEmpty(comDto.getSc006ChintaiRyo()) && !comDto.getSc006ChintaiRyoDisableFlg()) {
					// エラーメッセージ設定：{0}が未入力です。
					ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "社宅賃貸料");
					comDto.setSc006ChintaiRyoErr(CodeConstant.NFW_VALIDATION_ERROR);
					errorFlg = true;
					// 表示タブを役員/相互利用情報タブに設定
					setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_YAKUIN, comDto);
					LogUtils.debugByMsg("変更入力チェック：一時保存以外、出向(相互利用状況)あり、社宅賃貸料未設定");
				}
				// 駐車場料金
				if (CheckUtils.isEmpty(comDto.getSc006TyusyajoRyokin()) && !comDto.getSc006TyusyajoRyokinDisableFlg()) {
					// エラーメッセージ設定：{0}が未入力です。
					ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "駐車場料金");
					comDto.setSc006TyusyajoRyokinErr(CodeConstant.NFW_VALIDATION_ERROR);
					errorFlg = true;
					// 表示タブを役員/相互利用情報タブに設定
					setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_YAKUIN, comDto);
					LogUtils.debugByMsg("変更入力チェック：一時保存以外、出向(相互利用状況)あり、駐車場料金未設定");
				}
				// 共益費（事業者負担）
				if (CheckUtils.isEmpty(comDto.getSc006Kyoekihi()) && !comDto.getSc006KyoekihiDisableFlg()) {
					// エラーメッセージ設定：{0}が未入力です。
					ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1048, "共益費（事業者負担）");
					comDto.setSc006KyoekihiErr(CodeConstant.NFW_VALIDATION_ERROR);
					errorFlg = true;
					// 表示タブを役員/相互利用情報タブに設定
					setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_YAKUIN, comDto);
					LogUtils.debugByMsg("変更入力チェック：一時保存以外、出向(相互利用状況)あり、共益費（事業者負担）未設定");
				}
			}
			// 終了日
			if (!CheckUtils.isEmpty(comDto.getSc006EndDay()) && !CheckUtils.isEmpty(comDto.getSc006StartDay())) {
				// 日付整合性チェック
				if (validateDateCorrelation(comDto.getSc006StartDay(), comDto.getSc006EndDay())) {
					// エラーメッセージ設定：{0}は開始と終了を正しく入力してください。
					ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1133, "終了日");
					comDto.setSc006EndDayErr(CodeConstant.NFW_VALIDATION_ERROR);
					errorFlg = true;
					// 表示タブを役員/相互利用情報タブに設定
					setDisplayTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_YAKUIN, comDto);
					LogUtils.debugByMsg("変更入力チェック：一時保存以外、開始日あり、終了日不正(開始日より過去)");
				}
			}
		}
		return errorFlg;
	}

	/**
	 * 使用料計算パターンテーブルへのの登録・更新のための登録項目を設定
	 * 
	 * @param comDto	DTO
	 * @return
	 */
	public Map<Skf3022Sc006CommonDto.RENTAL_PATTERN, String> setRentalPatternList(Skf3022Sc006CommonDto comDto) {

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
	private Map<Skf3022Sc006CommonDto.RENTAL_PATTERN, String>
		setRentalPatternTorokuList(Boolean torokuFlg, Skf3022Sc006CommonDto comDto) {

		//		Dim list As New List(Of String)
		Map<Skf3022Sc006CommonDto.RENTAL_PATTERN, String> rentalPtMap =
				new HashMap<Skf3022Sc006CommonDto.RENTAL_PATTERN, String>();

		// データ設定に必要なセッション情報を取得。
//		Dim sessionInfo As New ShiyoryokeisanShienOutputEntity()
//		sessionInfo = DirectCast(Me.Session.Item(Constant.SessionId.SHIYORYO_KEISAN_SHIEN_OUTPUT_INFO),  _
//													ShiyoryokeisanShienOutputEntity)
		// 入力支援セッション存在フラグ
		Boolean sessionInfoFlg = false;
		if (!CheckUtils.isEmpty(comDto.getHdnRateShienPatternName())) {
			// 使用料パターン名が存在する場合は
			sessionInfoFlg = true;
		}

		// 社宅管理番号
//		list.Add(Me.hdnShatakuKanriNo.Value)
		rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.SHATAKUKANRI_NO,
													comDto.getHdnShatakuKanriNo());
		// 使用料パターンID（登録：連番、更新：hidden使用料パターンID）
//		If torokuFlg Then
		if (torokuFlg) {
//			list.Add(S2007_TeijiDataRegistBusinessLogic.GetMaxRentalPatternId())
			rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.RENTAL_PATTERNID,
					skf3022Sc006GetMaxRentalPatternIdExpRepository.getMaxRentalPatternId());
//		Else
		} else {
//			list.Add(Me.hdnShiyoryoKeisanPatternId.Value)
			rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.RENTAL_PATTERNID,
												comDto.getHdnShiyoryoKeisanPatternId());
//		End If
		}

//		Me.hdnSiyouryoId.Value = list(1)
		comDto.setHdnSiyouryoId(
				rentalPtMap.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.RENTAL_PATTERNID));
		// パターン名
//		If Not sessionInfo Is Nothing Then
		if (sessionInfoFlg) {
//			list.Add(sessionInfo.PatternName)
			rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.PATTERN_NAME,
											comDto.getHdnRateShienPatternName());
//		Else
		} else {
//			list.Add(String.Empty)
			rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.PATTERN_NAME,
													CodeConstant.DOUBLE_QUOTATION);
//		End If
		}
		// 規格
//		If Not sessionInfo Is Nothing Then
		if (sessionInfoFlg) {
//			list.Add(sessionInfo.Kikaku)
			rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.KIKAKU,
													comDto.getHdnRateShienKikaku());
//		Else
		} else {
//			list.Add(String.Empty)
			rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.KIKAKU,
													CodeConstant.DOUBLE_QUOTATION);
//		End If
		}
		// 規格（補足）（登録値無し）
//		list.Add(String.Empty)
		rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.KIKAKU_HOSOKU,
													CodeConstant.DOUBLE_QUOTATION);
		// 基準使用料算定上延べ面積
//		If Not sessionInfo Is Nothing Then
		if (sessionInfoFlg) {
//			list.Add(sessionInfo.KijunMenseki.ToString())
			rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.KIJUN_MENSEKI,
								getMensekiText(comDto.getHdnRateShienKijunMenseki()));
//		Else
		} else {
//			list.Add(String.Empty)
			rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.KIJUN_MENSEKI,
														CodeConstant.DOUBLE_QUOTATION);
//		End If
		}
		// 社宅使用料算定上延べ面積
//		If Not sessionInfo Is Nothing Then
		if (sessionInfoFlg) {
//			list.Add(sessionInfo.ShatakuMenseki.ToString())
			rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.SHATAKU_MENSEKI,
									getMensekiText(comDto.getHdnRateShienShatakuMenseki()));
//		Else
		} else {
//			list.Add(String.Empty)
			rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.SHATAKU_MENSEKI,
													CodeConstant.DOUBLE_QUOTATION);
//		End If
		}
		// 経年残価率
//		If Not sessionInfo Is Nothing Then
		if (sessionInfoFlg) {
//			list.Add(sessionInfo.KeinenZankaRitsu.ToString())
			rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.KEINEN_ZANKARITSU,
											comDto.getHdnRateShienKeinenZankaRitsu());
//		Else
		} else {
//			list.Add(String.Empty)
			rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.KEINEN_ZANKARITSU,
														CodeConstant.DOUBLE_QUOTATION);
//		End If
		}
		// 用途
//		If Not sessionInfo Is Nothing Then
		if (sessionInfoFlg) {
//			list.Add(sessionInfo.Yoto)
			rentalPtMap.put(
					Skf3022Sc006CommonDto.RENTAL_PATTERN.YOTO, comDto.getHdnRateShienYoto());
//		Else
		} else {
//			list.Add(String.Empty)
			rentalPtMap.put(
					Skf3022Sc006CommonDto.RENTAL_PATTERN.YOTO, CodeConstant.DOUBLE_QUOTATION);
//		End If
		}
		// 寒冷地調整フラグ（登録値無し）
//		list.Add(String.Empty)
		rentalPtMap.put(
				Skf3022Sc006CommonDto.RENTAL_PATTERN.KANREICHI, CodeConstant.DOUBLE_QUOTATION);
		// 狭小調整フラグ（登録値無し）
//		list.Add(String.Empty)
		rentalPtMap.put(
				Skf3022Sc006CommonDto.RENTAL_PATTERN.KYOUSYOU, CodeConstant.DOUBLE_QUOTATION);
		// 経年
//		If Not sessionInfo Is Nothing Then
		if (sessionInfoFlg) {
//			list.Add(sessionInfo.Keinen.ToString())
			rentalPtMap.put(
					Skf3022Sc006CommonDto.RENTAL_PATTERN.KEINEN, comDto.getHdnRateShienKeinen());
//		Else
		} else {
//			list.Add(String.Empty)
			rentalPtMap.put(
					Skf3022Sc006CommonDto.RENTAL_PATTERN.KEINEN, CodeConstant.DOUBLE_QUOTATION);
//		End If
		}
		// 基本使用料
//		If Not sessionInfo Is Nothing Then
		if (sessionInfoFlg) {
//			list.Add(sessionInfo.KihonShiyoryo.ToString())
			rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.KIHON_SHIYORYO,
									getKingakuText(comDto.getHdnRateShienKihonShiyoryo()));
//		Else
		} else {
//			list.Add(String.Empty)
			rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.KIHON_SHIYORYO,
													CodeConstant.DOUBLE_QUOTATION);
//		End If
		}
		// 単価
//		If Not sessionInfo Is Nothing Then
		if (sessionInfoFlg) {
//			list.Add(sessionInfo.Tanka.ToString())
			rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.TANKA,
										getKingakuText(comDto.getHdnRateShienTanka()));
//		Else
		} else {
//			list.Add(String.Empty)
			rentalPtMap.put(
					Skf3022Sc006CommonDto.RENTAL_PATTERN.TANKA, CodeConstant.DOUBLE_QUOTATION);
//		End If
		}
		// 社宅使用料月額
//		If Not sessionInfo Is Nothing Then
		if (sessionInfoFlg) {
//			list.Add(Me.GetPayText(Me.lblSiyoryoMonthPay.Text))
			// kami 既存バグ？セッションではなくラベルの値を設定しているが・・・
			rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.SHATAKU_GETSUGAKU,
										getKingakuText(comDto.getSc006SiyoryoMonthPay()));
//		Else
		} else {
//			list.Add(String.Empty)
			rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.SHATAKU_GETSUGAKU,
															CodeConstant.DOUBLE_QUOTATION);
//		End If
		}
		// 備考（登録値無し）
//		list.Add(String.Empty)
		rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.BIKO, CodeConstant.DOUBLE_QUOTATION);
		// 補足資料名（登録値無し）
//		list.Add(String.Empty)
		rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.HOSOKU_SHIRYO_NAME,
														CodeConstant.DOUBLE_QUOTATION);
		// 補足資料サイズ（登録値無し）
//		list.Add(String.Empty)
		rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.HOSOKU_SHIRYO_SIZE,
														CodeConstant.DOUBLE_QUOTATION);
		// 補足資料ファイル（登録値無し）
//		list.Add(String.Empty)
		rentalPtMap.put(
				Skf3022Sc006CommonDto.RENTAL_PATTERN.HOSOKU_FILE, CodeConstant.DOUBLE_QUOTATION);
		// 削除フラグ（登録：”0”、更新：登録値無し）
//		If torokuFlg Then
		if (torokuFlg) {
//			list.Add(DELETE_FLG_NOT_DELETED)
			// 登録
			rentalPtMap.put(
					Skf3022Sc006CommonDto.RENTAL_PATTERN.DELETE_FLAG, CodeConstant.STRING_ZERO);
//		Else
		} else {
//			list.Add(String.Empty)
			rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.DELETE_FLAG,
													CodeConstant.DOUBLE_QUOTATION);
//		End If
		}
		// 作成日（システムから設定するため登録値無し）
//		list.Add(String.Empty)
		rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.INSERT_DATE,
													CodeConstant.DOUBLE_QUOTATION);
		// 作成者（登録：作成者情報、更新：登録値無し）
//		If torokuFlg Then
//			list.Add(MyBase.userInfo.UserId)
//		Else
//			list.Add(String.Empty)
//		End If
		rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.INSERT_USER_ID,
													CodeConstant.DOUBLE_QUOTATION);
		// 作成IPアドレス（登録：作成者IPアドレス、更新：登録値無し）
//		If torokuFlg Then
//			list.Add(MyBase.publicInfo.IpAddress)
//		Else
//			list.Add(String.Empty)
//		End If
		rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.INSERT_PROGRAM_ID,
													CodeConstant.DOUBLE_QUOTATION);
		// 更新日（システムから設定するため登録値無し）
//		list.Add(String.Empty)
		rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.UPDATE_DATE,
													CodeConstant.DOUBLE_QUOTATION);
		// 更新者
//		list.Add(MyBase.userInfo.UserId)
		rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.UPDATE_USER_ID,
													CodeConstant.DOUBLE_QUOTATION);
		// 更新IPアドレス
//		list.Add(MyBase.publicInfo.IpAddress)
		rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.UPDATE_PROGRAM_ID,
													CodeConstant.DOUBLE_QUOTATION);
		// 延べ面積
//		If Not sessionInfo Is Nothing Then
		if (sessionInfoFlg) {
//			list.Add(sessionInfo.NobeMenseki.ToString())
			rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.NOBE_MENSEKI,
									getMensekiText(comDto.getHdnRateShienNobeMenseki()));
//		Else
		} else {
//			list.Add(String.Empty)
			rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.NOBE_MENSEKI,
													CodeConstant.DOUBLE_QUOTATION);
//		End If
		}
		// サンルーム面積
//		If Not sessionInfo Is Nothing Then
		if (sessionInfoFlg) {
//			list.Add(sessionInfo.SunroomMenseki.ToString())
			rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.SUNROOM_MENSEKI,
									getMensekiText(comDto.getHdnRateShienSunroomMenseki()));
//		Else
		} else {
//			list.Add(String.Empty)
			rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.SUNROOM_MENSEKI,
														CodeConstant.DOUBLE_QUOTATION);
//		End If
		}
		// 階段面積
//		If Not sessionInfo Is Nothing Then
		if (sessionInfoFlg) {
//			list.Add(sessionInfo.KaidanMenseki.ToString())
			rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.KAIDAN_MENSEKI,
								getMensekiText(comDto.getHdnRateShienKaidanMenseki()));
//		Else
		} else {
//			list.Add(String.Empty)
			rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.KAIDAN_MENSEKI,
													CodeConstant.DOUBLE_QUOTATION);
//		End If
		}
		// 物置面積
//		If Not sessionInfo Is Nothing Then
		if (sessionInfoFlg) {
//			list.Add(sessionInfo.MonookiMenseki.ToString())
			rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.MONOOKI_MENSEKI,
								getMensekiText(comDto.getHdnRateShienMonookiMenseki()));
//		Else
		} else {
//			list.Add(String.Empty)
			rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.MONOOKI_MENSEKI,
														CodeConstant.DOUBLE_QUOTATION);
//		End If
		}
		// 登録・更新判断用（登録：0、更新：1）
//		If torokuFlg Then
		if (torokuFlg) {
//			list.Add(DATA_0)
			// 登録
			rentalPtMap.put(
					Skf3022Sc006CommonDto.RENTAL_PATTERN.UPDATE_KIND, CodeConstant.STRING_ZERO);
//		Else
		} else {
//			list.Add(DATA_1)
			// 更新
			rentalPtMap.put(Skf3022Sc006CommonDto.RENTAL_PATTERN.UPDATE_KIND, "1");
//		End If
		}
		return rentalPtMap;
	}

	/**
	 * 更新提示データ情報の取得
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param syoriMode		処理モード
	 * @param updateMode	更新モード
	 * @param sysDateTime	更新日時
	 * @param comDto		*DTO
	 * @return
	 */
	public Skf3022TTeijiData getColumnInfoList(
			String syoriMode, Boolean updateMode, Date sysDateTime, Skf3022Sc006CommonDto comDto) {

		Skf3022TTeijiData columnInfoList = new Skf3022TTeijiData();
		// 更新SQLでは不要
		if (!updateMode) {
			// 提示番号
			Long newTeijiNo = skfBaseBusinessLogicUtils.getMaxTeijiNo();
			columnInfoList.setTeijiNo(newTeijiNo);
		}
		// 社員番号
		columnInfoList.setShainNo(comDto.getSc006ShainNo().replace(CodeConstant.ASTERISK, ""));
		// 継続ログイン判定
		if (Skf3022Sc006CommonDto.KEIZOKU_LOGIN.equals(syoriMode)) {
			// 入退居区分
//			columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.NYUTAIKYO_KBN, _
//													HttpUtility.HtmlEncode(Me.hdnNyutaikyoKbnOld.Value), _
//													OracleType.Char))
			// 旧入退居区分を設定
			columnInfoList.setNyutaikyoKbn(comDto.getHdnNyutaikyoKbnOld());
		} else {
			// 入退居区分
//			If Constant.NyutaikyoKbn.HENKO.Equals(Me.hdnNyutaikyoKbnOld.Value) And _
//				Constant.NyutaikyoKbn.TAIKYO.Equals(Me.hdnNyutaikyoKbn.Value) Then
			if (CodeConstant.NYUTAIKYO_KBN_HENKO.equals(comDto.getHdnNyutaikyoKbnOld())
					&& CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(comDto.getHdnNyutaikyoKbn())) {
//				columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.NYUTAIKYO_KBN, _
//														HttpUtility.HtmlEncode(Me.hdnNyutaikyoKbnOld.Value), _
//														OracleType.Char))
				// 旧入退居区分を設定
				columnInfoList.setNyutaikyoKbn(comDto.getHdnNyutaikyoKbnOld());
			} else {
//
//				columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.NYUTAIKYO_KBN, _
//														HttpUtility.HtmlEncode(Me.hdnNyutaikyoKbn.Value), _
//														OracleType.Char))
				// 現入退居区分を設定
				columnInfoList.setNyutaikyoKbn(comDto.getHdnNyutaikyoKbn());
			}
		}
		// 社員名
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.NAME, _
//												HttpUtility.HtmlEncode(Me.lblShainName.Text), _
//												OracleType.VarChar))
		columnInfoList.setName(comDto.getSc006ShainName());
		// 申請区分
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.APPL_KBN, _
//												 HttpUtility.HtmlEncode(Me.hdnApplKbn.Value), _
//												 OracleType.Char))
		columnInfoList.setApplKbn(comDto.getHdnApplKbn());
		// 社宅管理番号
//		If Not String.IsNullOrEmpty(Me.hdnShatakuKanriNo.Value) Then
		if (!CheckUtils.isEmpty(comDto.getHdnShatakuKanriNo())) {
//			columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.SHATAKU_KANRI_NO, _
//													HttpUtility.HtmlEncode(Me.hdnShatakuKanriNo.Value), _
//													OracleType.Char))
			columnInfoList.setShatakuKanriNo(Long.parseLong(comDto.getHdnShatakuKanriNo()));
		}
		// 部屋管理番号
//		If Not String.IsNullOrEmpty(Me.hdnHeyaKanriNo.Value) Then
		if (!CheckUtils.isEmpty(comDto.getHdnRoomKanriNo())) {
//			columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.SHATAKU_ROOM_KANRI_NO, _
//													HttpUtility.HtmlEncode(Me.hdnHeyaKanriNo.Value), _
//													OracleType.Char))
			columnInfoList.setShatakuRoomKanriNo(Long.parseLong(comDto.getHdnRoomKanriNo()));
		}
		// 使用料パターンＩＤ
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.RENTAL_PATTERN_ID, _
//												HttpUtility.HtmlEncode(Me.hdnSiyouryoId.Value), _
//												OracleType.Char))
		if (!CheckUtils.isEmpty(comDto.getHdnSiyouryoId())) {
			columnInfoList.setRentalPatternId(Long.parseLong(comDto.getHdnSiyouryoId()));
		}
		// 居住者区分
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.KYOJUSHA_KBN, _
//												HttpUtility.HtmlEncode(Me.ddlKyojyusyaKbn.SelectedValue), _
//												OracleType.Char))
		columnInfoList.setKyojushaKbn(comDto.getSc006KyojyusyaKbnSelect());
		// 役員算定
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.YAKUIN_SANNTEI_KBN, _
//												HttpUtility.HtmlEncode(Me.ddlYakuinSantei.SelectedValue), _
//												OracleType.Char))
		columnInfoList.setYakuinSannteiKbn(comDto.getSc006YakuinSanteiSelect());
		// 社宅使用料日割金額
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.RENTAL_DAY, _
//												HttpUtility.HtmlEncode(Me.GetPayText(Me.lblSiyoryoHiwariPay.Text)), _
//												OracleType.Char))
		columnInfoList.setRentalDay(Integer.parseInt(getKingakuText(comDto.getSc006SiyoryoHiwariPay())));
		// 社宅使用料調整金額
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.RENTAL_ADJUST, _
//												HttpUtility.HtmlEncode(Me.GetPayText(Me.txtSiyoroTyoseiPay.Text)), _
//												OracleType.Char))
		columnInfoList.setRentalAdjust(Integer.parseInt(getKingakuText(comDto.getSc006SiyoroTyoseiPay())));
//
		// 個人負担共益費協議中フラグ				
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.KYOEKIHI_PERSON_KYOGICHU_FLG, _
//												Me.GetKyoekihiKyogichuKbn(), _
//												OracleType.Char, _
//												ColumnInfoEntity.ConvertFunctionType.None))
		columnInfoList.setKyoekihiPersonKyogichuFlg(comDto.getSc006KyoekihiKyogichuCheckState());
		// 個人負担共益費協議中にチェックがある場合
//		If Me.chkKyoekihiKyogichu.Checked Then
		if ("1".equals(comDto.getSc006KyoekihiKyogichuCheckState())) {
//
//			Me.txtKyoekihiMonthPay.Text = DATA_0
			comDto.setSc006KyoekihiMonthPay(CodeConstant.STRING_ZERO);
//			Me.txtKyoekihiTyoseiPay.Text = DATA_0
			comDto.setSc006KyoekihiTyoseiPay(CodeConstant.STRING_ZERO);
//			Me.ddlKyoekihiPayMonth.SelectedIndex = 0
			comDto.setSc006KyoekihiPayMonthSelect(CodeConstant.STRING_ZERO);
//
			// 個人負担共益費
//			columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.KYOEKIHI_PERSON, _
//													HttpUtility.HtmlEncode(Me.GetPayText(Me.txtKyoekihiMonthPay.Text)), _
//													OracleType.Char))
			columnInfoList.setKyoekihiPerson(
					Integer.parseInt(getKingakuText(comDto.getSc006KyoekihiMonthPay())));
			// 個人負担共益費調整金額
//			columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.KYOEKIHI_PERSON_ADJUST, _
//													HttpUtility.HtmlEncode(Me.GetPayText(Me.txtKyoekihiTyoseiPay.Text)), _
//													OracleType.Char))
			columnInfoList.setKyoekihiPersonAdjust(
					Integer.parseInt(getKingakuText(comDto.getSc006KyoekihiTyoseiPay())));
			// 共益金支払月
//			columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.KYOEKIHI_PAY_MONTH, _
//													String.Empty, _
//													OracleType.Char))
			columnInfoList.setKyoekihiPayMonth(CodeConstant.DOUBLE_QUOTATION);
//
		} else {
			// 個人負担共益費
//			columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.KYOEKIHI_PERSON, _
//													HttpUtility.HtmlEncode(Me.GetPayText(Me.txtKyoekihiMonthPay.Text)), _
//													OracleType.Char))
			columnInfoList.setKyoekihiPerson(
					Integer.parseInt(getKingakuText(comDto.getSc006KyoekihiMonthPay())));
			// 個人負担共益費調整金額
//			columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.KYOEKIHI_PERSON_ADJUST, _
//													HttpUtility.HtmlEncode(Me.GetPayText(Me.txtKyoekihiTyoseiPay.Text)), _
//													OracleType.Char))
			columnInfoList.setKyoekihiPersonAdjust(
					Integer.parseInt(getKingakuText(comDto.getSc006KyoekihiTyoseiPay())));
			// 共益金支払月
//			columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.KYOEKIHI_PAY_MONTH, _
//													HttpUtility.HtmlEncode(Me.ddlKyoekihiPayMonth.SelectedValue), _
//													OracleType.Char))
			columnInfoList.setKyoekihiPayMonth(comDto.getSc006KyoekihiPayMonthSelect());
//
		}
		// 駐車場管理番号１
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.PARKING_KANRI_NO_1, _
//												HttpUtility.HtmlEncode(Me.hdnChushajoNoOne.Value), _
//												OracleType.Char))
		if (!CheckUtils.isEmpty(comDto.getHdnChushajoNoOne())) {
			columnInfoList.setParkingKanriNo1(Long.parseLong(comDto.getHdnChushajoNoOne()));
		} else {
			columnInfoList.setParkingKanriNo1(null);
		}
		// 駐車場区画１番号
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.PARKING_BLOCK_1, _
//												HttpUtility.HtmlEncode(Me.lblKukakuNoOne.Text), _
//												OracleType.VarChar))
		columnInfoList.setParkingBlock1(comDto.getSc006KukakuNoOne());
		// 駐車場区画１開始日
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.PARKING_1_START_DATE, _
//												HttpUtility.HtmlEncode(Me.GetDateText(Me.txtRiyouStartDayOne.Text)), _
//												OracleType.Char))
		columnInfoList.setParking1StartDate(getDateText(comDto.getSc006RiyouStartDayOne()));
		// 駐車場区画１終了日
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.PARKING_1_END_DATE, _
//												HttpUtility.HtmlEncode(Me.GetDateText(Me.txtRiyouEndDayOne.Text)), _
//												OracleType.Char))
		columnInfoList.setParking1EndDate(getDateText(comDto.getSc006RiyouEndDayOne()));
		// 駐車場管理番号２
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.PARKING_KANRI_NO_2, _
//												HttpUtility.HtmlEncode(Me.hdnChushajoNoTwo.Value), _
//												OracleType.Char))
		if (!CheckUtils.isEmpty(comDto.getHdnChushajoNoTwo())) {
			columnInfoList.setParkingKanriNo2(Long.parseLong(comDto.getHdnChushajoNoTwo()));
		} else {
			columnInfoList.setParkingKanriNo2(null);
		}
		// 駐車場区画２番号
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.PARKING_BLOCK_2, _
//												HttpUtility.HtmlEncode(Me.lblKukakuNoTwo.Text), _
//												OracleType.VarChar))
		columnInfoList.setParkingBlock2(comDto.getSc006KukakuNoTwo());
//		'駐車場区画２開始日
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.PARKING_2_START_DATE, _
//												HttpUtility.HtmlEncode(Me.GetDateText(Me.txtRiyouStartDayTwo.Text)), _
//												OracleType.Char))
		columnInfoList.setParking2StartDate(getDateText(comDto.getSc006RiyouStartDayTwo()));
		// 駐車場区画２終了日
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.PARKING_2_END_DATE, _
//												HttpUtility.HtmlEncode(Me.GetDateText(Me.txtRiyouEndDayTwo.Text)), _
//												OracleType.Char))
		columnInfoList.setParking2EndDate(getDateText(comDto.getSc006RiyouEndDayTwo()));
		// 駐車場使用料調整金額
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.PARKING_RENTAL_ADJUST, _
//												HttpUtility.HtmlEncode(Me.GetPayText(Me.txtTyusyaTyoseiPay.Text)), _
//												OracleType.Char))
		columnInfoList.setParkingRentalAdjust(
				Integer.parseInt(getKingakuText(comDto.getSc006TyusyaTyoseiPay())));
		// 駐車場日割金額１
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.PARKING_1_RENTAL_DAY, _
//												HttpUtility.HtmlEncode(Me.GetPayText(Me.lblTyusyaDayPayOne.Text)), _
//												OracleType.Char))
		columnInfoList.setParking1RentalDay(
				Integer.parseInt(getKingakuText(comDto.getSc006TyusyaDayPayOne())));
		// 駐車場日割金額２
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.PARKING_2_RENTAL_DAY, _
//												HttpUtility.HtmlEncode(Me.GetPayText(Me.lblTyusyaDayPayTwo.Text)), _
//												OracleType.Char))
		columnInfoList.setParking2RentalDay(
				Integer.parseInt(getKingakuText(comDto.getSc006TyusyaDayPayTwo())));
		// 備考
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.BIKO, _
//												HttpUtility.HtmlEncode(Me.txtBicou.Text), _
//												OracleType.VarChar))
		columnInfoList.setBiko(comDto.getSc006Bicou());
		// 備品貸与日
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.EQUIPMENT_START_DATE, _
//												HttpUtility.HtmlEncode(Me.GetDateText(Me.txtTaiyoDay.Text)), _
//												OracleType.Char))
		columnInfoList.setEquipmentStartDate(getDateText(comDto.getSc006TaiyoDay()));
		// 備品返却日
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.EQUIPMENT_END_DATE, _
//												HttpUtility.HtmlEncode(Me.GetDateText(Me.txtHenkyakuDay.Text)), _
//												OracleType.Char))
		columnInfoList.setEquipmentEndDate(getDateText(comDto.getSc006HenkyakuDay()));
		// 搬入希望日
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.CARRYIN_REQUEST_DAY, _
//												HttpUtility.HtmlEncode(Me.GetDateText(Me.txtKibouDayIn.Text)), _
//												OracleType.Char))
		columnInfoList.setCarryinRequestDay(getDateText(comDto.getSc006KibouDayIn()));
		// 搬入希望時間区分
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.CARRYIN_REQUEST_KBN, _
//												HttpUtility.HtmlEncode(Me.ddlKibouTimeIn.SelectedValue), _
//												OracleType.Char))
		columnInfoList.setCarryinRequestKbn(comDto.getSc006KibouTimeInSelect());
		// 受入本人連絡先
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.UKEIRE_MY_APOINT, _
//												HttpUtility.HtmlEncode(Me.txtHonNinAddrIn.Text), _
//												OracleType.VarChar))
		columnInfoList.setUkeireMyApoint(comDto.getSc006HonninAddrIn());
		// 受入代理人氏名
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.UKEIRE_DAIRI_NAME, _
//												HttpUtility.HtmlEncode(Me.txtUketoriDairiIn.Text), _
//												OracleType.VarChar))
		columnInfoList.setUkeireDairiName(comDto.getSc006UketoriDairiInName());
		// 受入代理人連絡先
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.UKEIRE_DAIRI_APOINT, _
//												HttpUtility.HtmlEncode(Me.txtUketoriDairiAddr.Text), _
//												OracleType.VarChar))
		columnInfoList.setUkeireDairiApoint(comDto.getSc006UketoriDairiAddr());
		// 搬出希望日
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.CARRYOUT_REQUEST_DAY, _
//												HttpUtility.HtmlEncode(Me.GetDateText(Me.txtKibouDayOut.Text)), _
//												OracleType.Char))
		columnInfoList.setCarryoutRequestDay(getDateText(comDto.getSc006KibouDayOut()));
		// 搬出希望時間区分
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.CARRYOUT_REQUEST_KBN, _
//												HttpUtility.HtmlEncode(Me.ddlKibouTimeOut.SelectedValue), _
//												OracleType.Char))
		columnInfoList.setCarryoutRequestKbn(comDto.getSc006KibouTimeOutSelect());
		// 立会本人連絡先
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.TATIAI_MY_APOINT, _
//												HttpUtility.HtmlEncode(Me.txtHonNinAddrOut.Text), _
//												OracleType.VarChar))
		columnInfoList.setTatiaiMyApoint(comDto.getSc006HonninAddrOut());
		// 立会代理人氏名
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.TATIAI_DAIRI_NAME, _
//												HttpUtility.HtmlEncode(Me.txtTachiaiDairi.Text), _
//												OracleType.VarChar))
		columnInfoList.setTatiaiDairiName(comDto.getSc006TachiaiDairi());
		// 立会代理人連絡先
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.TATIAI_DAIRI_APOINT, _
//												HttpUtility.HtmlEncode(Me.txtTachiaiDairiAddr.Text), _
//												OracleType.VarChar))
		columnInfoList.setTatiaiDairiApoint(comDto.getSc006TachiaiDairiAddr());
		// 代理人備考
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.DAIRI_KIKO, _
//												HttpUtility.HtmlEncode(Me.txtDairiBicou.Text), _
//												OracleType.VarChar))
		columnInfoList.setDairiKiko(comDto.getSc006DairiBiko());
		// 備品備考
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.BIHIN_BIKO, _
//												HttpUtility.HtmlEncode(Me.txtBihinBicou.Text), _
//												OracleType.VarChar))
		columnInfoList.setBihinBiko(comDto.getSc006BihinBiko());
		// 貸付会社コード
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.KASHITUKE_COMPANY_CD, _
//												HttpUtility.HtmlEncode(Me.ddlTaiyoKaisya.SelectedValue), _
//												OracleType.Char))
		columnInfoList.setKashitukeCompanyCd(comDto.getSc006TaiyoKaisyaSelect());
		// 借受会社コード
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.KARIUKE_COMPANY_CD, _
//												HttpUtility.HtmlEncode(Me.ddlKariukeKaisya.SelectedValue), _
//												OracleType.Char))
		columnInfoList.setKariukeCompanyCd(comDto.getSc006KariukeKaisyaSelect());
		// 相互利用状況
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.MUTUAL_JOKYO, _
//										HttpUtility.HtmlEncode(Me.ddlSogorRyoJokyo.SelectedValue), _
//										OracleType.Char))
		columnInfoList.setMutualJokyo(comDto.getSc006SogoRyojokyoSelect());
		// 相互利用判定区分
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.MUTUAL_USE_KBN, _
//												HttpUtility.HtmlEncode(Me.ddlSogoHanteiKbn.SelectedValue), _
//												OracleType.Char))
		columnInfoList.setMutualUseKbn(comDto.getSc006SogoHanteiKbnSelect());
		// 社宅賃貸料
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.RENT, _
//												HttpUtility.HtmlEncode(Me.GetPayText(Me.txtChintaiRyo.Text)), _
//												OracleType.Char))
		columnInfoList.setRent(Integer.parseInt(getKingakuText(comDto.getSc006ChintaiRyo())));
		// 駐車場料金
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.PARKING_RENTAL, _
//												HttpUtility.HtmlEncode(Me.GetPayText(Me.txtTyusyajoRyokin.Text)), _
//												OracleType.Char))
		columnInfoList.setParkingRental(Integer.parseInt(getKingakuText(comDto.getSc006TyusyajoRyokin())));
		// 共益費（事業者負担）
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.KYOEKIHI_BUSINESS, _
//												HttpUtility.HtmlEncode(Me.GetPayText(Me.txtKyoekihi.Text)), _
//												OracleType.Char))
		columnInfoList.setKyoekihiBusiness(Integer.parseInt(getKingakuText(comDto.getSc006Kyoekihi())));
		// 相互利用開始日
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.MUTUAL_USE_START_DAY, _
//												HttpUtility.HtmlEncode(Me.GetDateText(Me.txtStartDay.Text)), _
//												OracleType.Char))
		columnInfoList.setMutualUseStartDay(getDateText(comDto.getSc006StartDay()));
		// 相互利用終了日
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.MUTUAL_USE_END_DAY, _
//												HttpUtility.HtmlEncode(Me.GetDateText(Me.txtEndDay.Text)), _
//												OracleType.Char))
		columnInfoList.setMutualUseEndDay(getDateText(comDto.getSc006EndDay()));
		// 配属会社コード
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.ASSIGN_COMPANY_CD, _
//												HttpUtility.HtmlEncode(Me.ddlHaizokuKaisya.SelectedValue), _
//												OracleType.Char))
		columnInfoList.setAssignCompanyCd(comDto.getSc006HaizokuKaisyaSelect());
		// 所属機関
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.AGENCY, _
//												HttpUtility.HtmlEncode(Me.txtSyozokuKikan.Text), _
//												OracleType.VarChar))
		columnInfoList.setAgency(comDto.getSc006SyozokuKikan());
		// 室・部名
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.AFFILIATION1, _
//												HttpUtility.HtmlEncode(Me.txtSituBuName.Text), _
//												OracleType.VarChar))
		columnInfoList.setAffiliation1(comDto.getSc006SituBuName());
		// 課等名
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.AFFILIATION2, _
//												HttpUtility.HtmlEncode(Me.txtKaName.Text), _
//												OracleType.VarChar))
		columnInfoList.setAffiliation2(comDto.getSc006KanadoMei());
		// 配属データコード番号
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.ASSIGN_CD, _
//												HttpUtility.HtmlEncode(Me.txtHaizokuNo.Text), _
//												OracleType.Char))
		columnInfoList.setAssignCd(comDto.getSc006HaizokuNo());
		// 原籍会社コード
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.ORIGINAL_COMPANY_CD, _
//												HttpUtility.HtmlEncode(Me.ddlOldKaisya.SelectedValue), _
//												OracleType.Char))
		columnInfoList.setOriginalCompanyCd(comDto.getSc006OldKaisyaNameSelect());
		// 給与支給会社区分
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.PAY_COMPANY_CD, _
//												HttpUtility.HtmlEncode(Me.ddlKyuyoKaisya.SelectedValue), _
//												OracleType.Char))
		columnInfoList.setPayCompanyCd(comDto.getSc006KyuyoKaisyaSelect());
		// 社宅使用料会社間送金区分()
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.SHATAKU_COMPANY_TRANSFER_KBN, _
//												HttpUtility.HtmlEncode(Me.ddlSokinShataku.SelectedValue), _
//												OracleType.Char))
		columnInfoList.setShatakuCompanyTransferKbn(comDto.getSc006SokinShatakuSelect());
		// 共益費会社間送金区分()
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.KYOEKIHI_COMPANY_TRANSFER_KBN, _
//												HttpUtility.HtmlEncode(Me.ddlSokinKyoekihi.SelectedValue), _
//												OracleType.Char))
		columnInfoList.setKyoekihiCompanyTransferKbn(comDto.getSc006SokinKyoekihiSelect());
//		If TMP_SAVE.Equals(syoriMode) Or _
//		   KEIZOKU_LOGIN.Equals(syoriMode) Then
		if (Skf3022Sc006CommonDto.TMP_SAVE.equals(syoriMode)
				|| Skf3022Sc006CommonDto.KEIZOKU_LOGIN.equals(syoriMode)) {
//			'一時保存と継続登録の時にステータス更新処理は行わない。
//			'If Constant.ShatakuTeijiStatusKbn.DOI_SUMI.Equals(Me.hdnShatakuTeijiStatus.Value) Or _
//			'   Constant.ShatakuTeijiStatusKbn.SHONIN.Equals(Me.hdnShatakuTeijiStatus.Value) Then
//			'	'備品提示ステータス
//			'	columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.BIHIN_TEIJI_STATUS, _
//			'											Constant.BihinTeijiStatusKbn.SAKUSEI_CHU, _
//			'											OracleType.Char))
//			'Else
//			'	'社宅提示ステータス（作成中）
//			'	columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.SHATAKU_TEIJI_STATUS, _
//			'											Constant.ShatakuTeijiStatusKbn.SAKUSEI_CHU, _
//			'											OracleType.Char))
//			'End If
//			''作成完了区分（未作成）
//			'columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.CREATE_COMPLETE_KBN, _
//			'										Constant.CreateCompleteKbn.MI_SAKUSEI, _
//			'										OracleType.Char))
//			''台帳作成区分（未作成）
//			'columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.LAND_CREATE_KBN, _
//			'										Constant.LandCreateKbn.MI_SAKUSEI, _
//			'										OracleType.Char))
		} else if (Skf3022Sc006CommonDto.CREATE.equals(syoriMode)) {
			// 入退居区分が退去のとき
//			If Constant.NyutaikyoKbn.TAIKYO.Equals(Me.hdnNyutaikyoKbn.Value) Then
			if (CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(comDto.getHdnNyutaikyoKbn())) {
//				If Constant.BihinTaiyoKbn.HITSUYO.Equals(Me.hdnBihinTaiyoKbn.Value) AndAlso _
//				   Not Constant.BihinTeijiStatusKbn.DOI_SUMI.Equals(Me.hdnBihinTeijiStatus.Value) Then
				if (CodeConstant.BIHIN_TAIYO_KBN_HITSUYO.equals(comDto.getHdnBihinTaiyoKbn())
						&& !CodeConstant.BIHIN_STATUS_DOI_SUMI.equals(comDto.getHdnBihinTeijiStatus())) {
//					'作成完了区分(備品作成済)
//					columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.CREATE_COMPLETE_KBN, _
//															Constant.CreateCompleteKbn.BIHIN_SAKUSEI_SUMI, _
//															OracleType.Char))
					columnInfoList.setCreateCompleteKbn(CodeConstant.BIHIN_SAKUSEI_SUMI);
//					'備品提示ステータス（作成済）
//					columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.BIHIN_TEIJI_STATUS, _
//															Constant.BihinTeijiStatusKbn.SAKUSEI_SUMI, _
//															OracleType.Char))
					columnInfoList.setBihinTeijiStatus(CodeConstant.TEIJI_CREATE_KBN_SAKUSEI_SUMI);
				} else {
//					'作成完了区分（社宅作成済）
//					columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.CREATE_COMPLETE_KBN, _
//															Constant.CreateCompleteKbn.SHATAKU_SAKUSEI_SUMI, _
//															OracleType.Char))
					columnInfoList.setCreateCompleteKbn(CodeConstant.SHATAKU_SAKUSEI_SUMI);
				}
			// 入退居区分が入居のとき
			} else {
				// 社宅提示区分が提示中
//				If Constant.ShatakuTeijiStatusKbn.TEIJI_CHU.Equals(Me.hdnShatakuTeijiStatus.Value) Then
				if (CodeConstant.PRESENTATION_SITUATION_TEIJI_CHU.equals(
											comDto.getHdnShatakuTeijiStatus())) {
//
//					'社宅提示ステータス（提示中）
//					columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.SHATAKU_TEIJI_STATUS, _
//															Constant.ShatakuTeijiStatusKbn.TEIJI_CHU, _
//															OracleType.Char))
					columnInfoList.setShatakuTeijiStatus(CodeConstant.PRESENTATION_SITUATION_TEIJI_CHU);
				// 社宅提示区分が同意済
				} else if (CodeConstant.PRESENTATION_SITUATION_DOI_SUMI.equals(
												comDto.getHdnShatakuTeijiStatus())) {
//					Select Case Me.hdnBihinTeijiStatus.Value
					switch (comDto.getHdnBihinTeijiStatus()) {
						// 貸与不要			
//						Case String.Empty
						case CodeConstant.DOUBLE_QUOTATION:
							// 備品提示ステータス
//							columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.BIHIN_TEIJI_STATUS, _
//																	String.Empty, _
//																	OracleType.Char))
							columnInfoList.setBihinTeijiStatus(CodeConstant.DOUBLE_QUOTATION);
							break;
						// 未作成
//						Case Constant.BihinTeijiStatusKbn.MI_SAKUSEI
						case CodeConstant.BIHIN_STATUS_MI_SAKUSEI:
							// 備品提示ステータス
//							columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.BIHIN_TEIJI_STATUS, _
//																	Constant.BihinTeijiStatusKbn.MI_SAKUSEI, _
//																	OracleType.Char))
							columnInfoList.setBihinTeijiStatus(CodeConstant.BIHIN_STATUS_MI_SAKUSEI);
							break;
						// 搬入待ち
//						Case Constant.BihinTeijiStatusKbn.HANNYU_MACHI
						case CodeConstant.BIHIN_STATUS_HANNYU_MACHI:
							// 備品提示ステータス
//							columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.BIHIN_TEIJI_STATUS, _
//																	Constant.BihinTeijiStatusKbn.HANNYU_MACHI, _
//																	OracleType.Char))
							columnInfoList.setBihinTeijiStatus(CodeConstant.BIHIN_STATUS_HANNYU_MACHI);
							break;
						// 搬入済み
//						Case Constant.BihinTeijiStatusKbn.HANNYU_SUMI
						case CodeConstant.BIHIN_STATUS_HANNYU_SUMI:
							// 備品提示ステータス
//							columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.BIHIN_TEIJI_STATUS, _
//																	Constant.BihinTeijiStatusKbn.HANNYU_SUMI, _
//																	OracleType.Char))
							columnInfoList.setBihinTeijiStatus(CodeConstant.BIHIN_STATUS_HANNYU_SUMI);
							break;
						// 作成中もしくは作成済
//						Case Else
						default:
							// 備品提示ステータス（作成済）
//							columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.BIHIN_TEIJI_STATUS, _
//																	Constant.BihinTeijiStatusKbn.SAKUSEI_SUMI, _
//																	OracleType.Char))
							columnInfoList.setBihinTeijiStatus(CodeConstant.BIHIN_STATUS_SAKUSEI_SUMI);
							// 作成完了区分（備品作成済）
//							columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.CREATE_COMPLETE_KBN, _
//																	Constant.CreateCompleteKbn.BIHIN_SAKUSEI_SUMI, _
//																	OracleType.Char))
							columnInfoList.setCreateCompleteKbn(CodeConstant.BIHIN_STATUS_SAKUSEI_SUMI);
							break;
					};
				// 社宅提示区分が承認済
				} else if (CodeConstant.PRESENTATION_SITUATION_SHONIN.equals(
											comDto.getHdnShatakuTeijiStatus())) {
					// 備品提示ステータス（作成済）
//					columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.BIHIN_TEIJI_STATUS, _
//															Constant.BihinTeijiStatusKbn.SAKUSEI_SUMI, _
//															OracleType.Char))
					columnInfoList.setBihinTeijiStatus(CodeConstant.BIHIN_STATUS_SAKUSEI_SUMI);
					// 作成完了区分（備品作成済）
//					columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.CREATE_COMPLETE_KBN, _
//															Constant.CreateCompleteKbn.BIHIN_SAKUSEI_SUMI, _
//															OracleType.Char))
					columnInfoList.setCreateCompleteKbn(CodeConstant.BIHIN_STATUS_SAKUSEI_SUMI);
				} else {
					// 社宅提示ステータス（作成済）
//					columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.SHATAKU_TEIJI_STATUS, _
//															Constant.ShatakuTeijiStatusKbn.SAKUSEI_SUMI, _
//															OracleType.Char))
					columnInfoList.setShatakuTeijiStatus(CodeConstant.PRESENTATION_SITUATION_SAKUSEI_SUMI);
					// 作成完了区分（社宅作成済）
//					columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.CREATE_COMPLETE_KBN, _
//															Constant.CreateCompleteKbn.SHATAKU_SAKUSEI_SUMI, _
//															OracleType.Char))
					columnInfoList.setCreateCompleteKbn(CodeConstant.SHATAKU_SAKUSEI_SUMI);
				}
			}
			// 台帳作成区分（未作成）
//			columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.LAND_CREATE_KBN, _
//													Constant.LandCreateKbn.MI_SAKUSEI, _
//													OracleType.Char))
			columnInfoList.setLandCreateKbn(CodeConstant.LAND_CREATE_KBN_MI_SAKUSEI);
		} else if (Skf3022Sc006CommonDto.SHATAKU_LOGIN.equals(syoriMode)) {
			// 社宅提示ステータス（承認）
//			columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.SHATAKU_TEIJI_STATUS, _
//													Constant.ShatakuTeijiStatusKbn.SHONIN, _
//													OracleType.Char))
			columnInfoList.setShatakuTeijiStatus(CodeConstant.PRESENTATION_SITUATION_SHONIN);
			// 作成完了区分（未作成）
//			columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.CREATE_COMPLETE_KBN, _
//													Constant.CreateCompleteKbn.MI_SAKUSEI, _
//													OracleType.Char))
			columnInfoList.setCreateCompleteKbn(CodeConstant.MI_SAKUSEI);
			// 仮社員番号、又は、備品貸与区分（hidden変数）が"0"（不要）の場合
//			If KARI_K.Equals(Mid(Me.lblShainNo.Text, 1, 1)) Or _
//				Constant.BihinTaiyoKbn.FUYO.Equals(Me.hdnBihinTaiyoKbn.Value) Then
			if (Skf3022Sc006CommonDto.KARI_K.equals(comDto.getSc006ShainNo().substring(0, 1))
					|| CodeConstant.BIHIN_TAIYO_KBN_FUYO.equals(comDto.getHdnBihinTaiyoKbn())) {
				// 備品提示ステータス
//				columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.BIHIN_TEIJI_STATUS, _
//														String.Empty, _
//														OracleType.Char))
				columnInfoList.setBihinTeijiStatus(CodeConstant.DOUBLE_QUOTATION);
			// 仮者番号ではない、且つ、備品貸与区分（hidden変数）が"1"（必要）の場合
			} else {
				// 備品提示ステータス
//				columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.BIHIN_TEIJI_STATUS, _
//														Constant.BihinTeijiStatusKbn.SHONIN, _
//														OracleType.Char))
				columnInfoList.setBihinTeijiStatus(CodeConstant.BIHIN_STATUS_SHONIN);
			}
		}
		// 更新SQLでは不要
//		If Not updateMode Then
		if (!updateMode) {
			// 作成日時
//			columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.CREATE_DATE, _
//													sysDateTime, _
//													OracleType.Timestamp, _
//													ColumnInfoEntity.ConvertFunctionType.ToTimeStamp))
			columnInfoList.setInsertDate(sysDateTime);
			// 作成者
//			columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.CREATE_USER, _
//													HttpUtility.HtmlEncode(Me.userInfo.UserId), _
//													OracleType.VarChar))
			columnInfoList.setInsertUserId(skfLoginUserInfoUtils.getSkfLoginUserInfo().get("userName"));
			// 作成機能ID
//			columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.CREATE_IP, _
//													HttpUtility.HtmlEncode(Me.publicInfo.IpAddress), _
//													OracleType.Char))
			columnInfoList.setInsertProgramId(comDto.getPageId());
		}
		// 更新日
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.UPDATE_DATE, _
//												sysDateTime, _
//												OracleType.Timestamp, _
//												ColumnInfoEntity.ConvertFunctionType.ToTimeStamp))
		columnInfoList.setUpdateDate(sysDateTime);
		// 更新者
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.UPDATE_USER, _
//												HttpUtility.HtmlEncode(Me.userInfo.UserId), _
//												OracleType.VarChar))
		columnInfoList.setUpdateUserId(skfLoginUserInfoUtils.getSkfLoginUserInfo().get("userName"));
		// 更新機能ID
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.UPDATE_IP, _
//												HttpUtility.HtmlEncode(Me.publicInfo.IpAddress), _
//												OracleType.Char))
		columnInfoList.setUpdateProgramId(comDto.getPageId());
//		'【使用料計算機能対応】追加分項目
		// 社宅使用料月額
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.RENTAL_MONTH, _
//												HttpUtility.HtmlEncode(Me.GetPayText(Me.lblShiyoryoTsukigaku.Text)), _
//												OracleType.Char))
		columnInfoList.setRentalMonth(
				Integer.parseInt(getKingakuText(comDto.getSc006ShiyoryoTsukigaku())));
		// 駐車場使用料月額１
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.PARKING_1_RENTAL_MONTH, _
//												HttpUtility.HtmlEncode(Me.GetPayText(Me.lblTyusyaMonthPayOne.Text)), _
//												OracleType.Char))
		columnInfoList.setParking1RentalMonth(
				Integer.parseInt(getKingakuText(comDto.getSc006TyusyaMonthPayOne())));
		// 駐車場使用料月額２
//		columnInfoList.Add(New ColumnInfoEntity(ConstantTableInfo.TbtTeijiData.PARKING_2_RENTAL_MONTH, _
//												HttpUtility.HtmlEncode(Me.GetPayText(Me.lblTyusyaMonthPayTwo.Text)), _
//												OracleType.Char))
		columnInfoList.setParking2RentalMonth(
				Integer.parseInt(getKingakuText(comDto.getSc006TyusyaMonthPayTwo())));

		return columnInfoList;
	}


//	''' <summary>
//	''' 一時保存／継続登録／作成完了／社宅管理台帳登録処理メソッド
//	''' </summary>
//	''' <param name="param">引数リスト</param>
//	''' <param name="updateMode">提示データ更新区分</param>
//	''' <param name="syoriFlg">処理区分</param>
//	''' <param name="columnInfoList">カラム情報</param>
//	''' <param name="bihinCd">備品情報</param>
//	''' <param name="systemDate">システム日付</param>
//	''' <param name="updCountTJ">提示データ更新件数</param>
//	''' <param name="updCountNY">入退居予定更新件数</param>
//	''' <param name="updCountTBD">提示備品更新件数</param>
//	''' <param name="updCountOldSR">社宅部屋情報元更新件数</param>
//	''' <param name="updCountSR">社宅部屋情報更新件数</param>
//	''' <param name="updCountOldSPB1">社宅駐車場区画情報元更新件数（区画１）</param>
//	''' <param name="updCountSPB1">社宅駐車場区画情報更新件数（区画１）</param>
//	''' <param name="updCountOldSPB2">社宅駐車場区画情報元更新件数（区画２）</param>
//	''' <param name="updCountSPB2">社宅駐車場区画情報更新件数（区画２）</param>
//	''' <param name="companyCd">会社コード</param>
//	''' <param name="userId">ユーザID</param>
//	''' <param name="ipAddress">IPアドレス</param>
//	''' <param name="bihinTaiyobi">備品貸与日</param>
//	''' <param name="updCountRP">使用料パターン更新件数</param>
//	''' <param name="rentalPatternUpdateDate">使用料パターン排他処理用更新日時</param>
//	''' <param name="rentalPatternUpdateDateForRegist">使用料パターン更新日時</param>
//	''' <param name="rentalPatternTorokuList">使用料パターン登録項目リスト</param>
//	''' <returns>更新件数</returns>
//	''' <remarks></remarks>
//	Public Shared Function TmpSaveAndCreateAndShatakuLogin(ByVal param As List(Of String), _
//														   ByVal updateMode As Boolean, _
//														   ByVal teijiNoOld As String, _
//														   ByVal syoriFlg As Boolean, _
//														   ByVal columnInfoList As List(Of ColumnInfoEntity), _
//														   ByVal bihinCd As List(Of BihinInfoEntity), _
//														   ByVal systemDate As Date, _
//														   ByRef updCountTJ As Integer, _
//														   ByRef updCountNY As Integer, _
//														   ByRef updCountTBD As Integer, _
//														   ByRef updCountOldSR As Integer, _
//														   ByRef updCountSR As Integer, _
//														   ByRef updCountOldSPB1 As Integer, _
//														   ByRef updCountSPB1 As Integer, _
//														   ByRef updCountOldSPB2 As Integer, _
//														   ByRef updCountSPB2 As Integer, _
//														   ByVal companyCd As String, _
//														   ByVal userId As String, _
//														   ByVal ipAddress As String, _
//														   ByVal bihinTaiyobi As String, _
//														   ByRef updCountRP As Integer, _
//														   ByVal rentalPatternUpdateDate As String, _
//														   ByVal rentalPatternUpdateDateForRegist As Date, _
//														   ByVal rentalPatternTorokuList As List(Of String)) As Integer

	/**
	 * 
	 * @param param
	 * @param updateMode
	 * @param teijiNoOld
	 * @param syoriFlg
	 * @param columnInfoList
	 * @param bihinCd
	 * @param updCountMap
	 * @param companyCd
	 * @param bihinTaiyobi
	 * @param rentalPatternUpdateDate
	 * @param rentalPatternUpdateDateForRegist
	 * @param rentalPatternTorokuList
	 * @return
	 */
//	@Override
	public int tmpSaveAndCreateAndShatakuLogin(
			Map<Skf3022Sc006CommonDto.TEIJIDATA_PARAM, String> teijiParamMap,
			Boolean updateMode,
			String teijiNoOld,
			Boolean syoriFlg,
			Skf3022TTeijiData columnInfoList,
			List<Map<String, Object>> bihinCd,
			Date systemDate,
			Map<Skf3022Sc006CommonDto.UPDATE_COUNTER, Integer> updCountMap,
			String companyCd,
			String bihinTaiyobi,
			String rentalPatternUpdateDate,
			Date rentalPatternUpdateDateForRegist,
			Map<Skf3022Sc006CommonDto.RENTAL_PATTERN, String> rentalPatternTorokuList) {

//		Dim teijiNo As String = param(0)							 '提示番号
//		Dim shainNo As String = param(1)							 '社員番号
//		Dim nyutaikyoKbn As String = param(2)						'入退居区分
//		Dim shatakuKanriNoOld As String = param(3)				   '社宅管理番号元
//		Dim shatakuRoomKanriNoOld As String = param(4)			   '部屋管理番号元
//		Dim chushajoNoOneOld As String = param(5)					'駐車場管理番号1元
//		Dim chushajoNoTwoOld As String = param(6)					'駐車場管理番号2元
//		Dim shatakuKanriNo As String = param(7)					  '社宅管理番号
//		Dim shatakuRoomKanriNo As String = param(8)				  '部屋管理番号
//		Dim chushajoNoOne As String = param(9)					   '駐車場管理番号1
//		Dim chushajoNoTwo As String = param(10)					  '駐車場管理番号2
//		Dim nyukyoYoteiDate As String = param(11)					'入居予定日
//		Dim taikyoYoteiDate As String = param(12)					'退居予定日
//		Dim buttonFlg As String = param(13)						  'ボタン区分
//		Dim teijiDataUpdateDate As String = param(14)				'提示データ更新日
//		Dim nyutaikyoYoteiUpdateDate As String = param(15)		   '入退居予定更新日
//		Dim shatakuRoomOldUpdateDate As String = param(16)		   '社宅部屋情報マスタ元更新日
//		Dim shatakuRoomUpdateDate As String = param(17)			  '社宅部屋情報マスタ更新日
//		Dim shatakuParkingBlockOld1UpdateDate As String = param(18)  '社宅駐車場区画情報マスタ元更新日（区画１）
//		Dim shatakuParkingBlockOld11UpdateDate As String = param(19) '社宅駐車場区画情報マスタ元更新日（区画１）
//		Dim shatakuParkingBlock1UpdateDate As String = param(20)	 '社宅駐車場区画情報マスタ更新日（区画１）
//		Dim shatakuParkingBlockOld2UpdateDate As String = param(21)  '社宅駐車場区画情報マスタ元更新日（区画２）
//		Dim shatakuParkingBlockOld21UpdateDate As String = param(22) '社宅駐車場区画情報マスタ元更新日（区画２）
//		Dim shatakuParkingBlock2UpdateDate As String = param(23)	 '社宅駐車場区画情報マスタ更新日（区画２）
//		Dim updateUser As String = param(24)						 '更新者
//		Dim updateIp As String = param(25)						   '更新IPアドレス
//		Dim siyouryoFlg As String = param(26)						'使用料変更フラグ


		/** 更新結果カウンタ */
		Integer updCountTJ = 0;
		Integer updCountNY = 0;
		Integer updCountTBD = 0;
		Integer updCountOldSR = 0;
		Integer updCountSR = 0;
		Integer updCountOldSPB1 = 0;
		Integer updCountSPB1 = 0;
		Integer updCountOldSPB2 = 0;
		Integer updCountSPB2 = 0;
		Integer updCountRP = 0;
		updCountMap.put(UPDATE_COUNTER.UPD_COUNT_TJ, updCountTJ);
		updCountMap.put(UPDATE_COUNTER.UPD_COUNT_NY, updCountNY);
		updCountMap.put(UPDATE_COUNTER.UPD_COUNT_TBD, updCountTBD);
		updCountMap.put(UPDATE_COUNTER.UPD_COUNT_OLD_SR, updCountOldSR);
		updCountMap.put(UPDATE_COUNTER.UPD_COUNT_SR, updCountSR);
		updCountMap.put(UPDATE_COUNTER.UPD_COUNT_OLD_SPB_1, updCountOldSPB1);
		updCountMap.put(UPDATE_COUNTER.UPD_COUNT_SPB_1, updCountSPB1);
		updCountMap.put(UPDATE_COUNTER.UPD_COUNT_OLD_SPB_2, updCountOldSPB2);
		updCountMap.put(UPDATE_COUNTER.UPD_COUNT_SPB_2, updCountSPB2);
		updCountMap.put(UPDATE_COUNTER.UPD_COUNT_RP, updCountRP);

		// 提示番号
		String teijiNo = teijiParamMap.get(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.TEIJI_NO);
		// 社員番号
		String shainNo = teijiParamMap.get(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHAIN_NO);
		// 入退居区分
		String nyutaikyoKbn = teijiParamMap.get(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.NYUTAIKYO_KBN);
		// 社宅管理番号元
		String shatakuKanriNoOld =
				teijiParamMap.get(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKU_KANRINO_OLD);
		// 部屋管理番号元
		String shatakuRoomKanriNoOld =
				teijiParamMap.get(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKU_ROOMKANRINO_OLD);
		// 駐車場管理番号1元
		String chushajoNoOneOld =
				teijiParamMap.get(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.CHUSHAJONO_ONE_OLD);
		// 駐車場管理番号2元
		String chushajoNoTwoOld =
				teijiParamMap.get(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.CHUSHAJONO_TWO_OLD);
		// 社宅管理番号
		String shatakuKanriNo =
				teijiParamMap.get(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKU_KANRINO);
		// 部屋管理番号
		String shatakuRoomKanriNo =
				teijiParamMap.get(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKU_ROOMKANRINO);
		// 駐車場管理番号1
		String chushajoNoOne =
				teijiParamMap.get(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.CHUSHAJONO_ONE);
		// 駐車場管理番号2
		String chushajoNoTwo =
				teijiParamMap.get(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.CHUSHAJONO_TWO);
		// 入居予定日
		String nyukyoYoteiDate =
				teijiParamMap.get(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.NYUKYOYOTEI_DATE);
		// 退居予定日
		String taikyoYoteiDate =
				teijiParamMap.get(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.TAIKYOYOTEI_DATE);
		// ボタン区分
		String buttonFlg = teijiParamMap.get(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.BUTTON_FLG);
		// 提示データ更新日
		String teijiDataUpdateDate =
				teijiParamMap.get(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.TEIJIDATA_UPDATEDATE);
		// 社宅部屋情報マスタ元更新日
		String shatakuRoomOldUpdateDate =
				teijiParamMap.get(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKUROOM_OLD_UPDATEDATE);
		// 社宅部屋情報マスタ更新日
		String shatakuRoomUpdateDate =
				teijiParamMap.get(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKUROOM_UPDATEDATE);
		// 社宅駐車場区画情報マスタ元更新日（区画１）
		String shatakuParkingBlockOld1UpdateDate = teijiParamMap.get(
				Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKU_PARKINGBLOCK_OLD1_UPDATEDATE);
		// 社宅駐車場区画情報マスタ更新日（区画１）
		String shatakuParkingBlock1UpdateDate = teijiParamMap.get(
				Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKU_PARKINGBLOCK_1_UPDATEDATE);
		// 社宅駐車場区画情報マスタ元更新日（区画２）
		String shatakuParkingBlockOld2UpdateDate = teijiParamMap.get(
				Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKU_PARKINGBLOCK_OLD2_UPDATEDATE);
		// 社宅駐車場区画情報マスタ更新日（区画２）
		String shatakuParkingBlock2UpdateDate = teijiParamMap.get(
				Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHATAKU_PARKINGBLOCK_2_UPDATEDATE);
		// 更新者
		String updateUser = teijiParamMap.get(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.UPDATE_USER_ID);
		// 更新機能ID
		String updateProgramId = teijiParamMap.get(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.UPDATE_PROGRAM_ID);
		// 使用料変更フラグ
		String siyouryoFlg = teijiParamMap.get(Skf3022Sc006CommonDto.TEIJIDATA_PARAM.SHIYOURYO_FLG);

//		Dim newFlg As Boolean = False								'提示データ新規フラグ
		// 提示データ新規フラグ
		Boolean newFlg = false;
		// 提示番号
//		Dim nyukyoTeijiNo As String = String.Empty
		String nyukyoTeijiNo = "";
		// システム処理年月
		String sysShoriNenGetsu = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu();
		SimpleDateFormat dateFormat = new SimpleDateFormat(Skf3022Sc006CommonDto.DATE_FORMAT);

		// 継続登録処理なし時
		if (updateMode) {
			// 排他処理
			List<Skf3022Sc006GetNyukyoTeijiNoDataExp> targetDt = new ArrayList<Skf3022Sc006GetNyukyoTeijiNoDataExp>();
			Skf3022Sc006GetTeijiDataForUpdateExpParameter t1Param = new Skf3022Sc006GetTeijiDataForUpdateExpParameter();
			t1Param.setTeijiNo(Long.parseLong(teijiNo));
			t1Param.setUpdateDate(teijiDataUpdateDate);
			targetDt = skf3022Sc006UpdateTeijiDataExpRepository.getTeijiDataForUpdate(t1Param);

			// データが存在する場合
			if (targetDt.size() > 0) {
				// 提示データを更新
				Long backupTeijiNo = columnInfoList.getTeijiNo();
				columnInfoList.setTeijiNo(Long.parseLong(teijiNo));
				updCountTJ = skf3022Sc006UpdateTeijiDataExpRepository.updateByPrimaryKeySelective(columnInfoList);
				columnInfoList.setTeijiNo(backupTeijiNo);
				LogUtils.debugByMsg("継続あり、提示データ更新");
			} else {
				// 排他エラー
				LogUtils.debugByMsg("継続あり、提示データ排他エラー");
				return -1;
			}
		} else {
			// 入居の提示データを取得
			List<Skf3022Sc006GetNyukyoTeijiNoDataExp> nyukyoTeijiNoList = new ArrayList<Skf3022Sc006GetNyukyoTeijiNoDataExp>();
			Skf3022Sc006GetNyukyoTeijiNoExpParameter nyukyoTeijiParam = new Skf3022Sc006GetNyukyoTeijiNoExpParameter();
			nyukyoTeijiParam.setShatakuKanriNo(Long.parseLong(shatakuKanriNoOld));
			nyukyoTeijiParam.setShatakuRoomKanriNo(Long.parseLong(shatakuRoomKanriNoOld));
			nyukyoTeijiParam.setShainNo(shainNo);
			nyukyoTeijiNoList = skf3022Sc006GetNyukyoTeijiNoExpRepository.getNyukyoTeijiNo(nyukyoTeijiParam);
			// データが存在する場合
			if (nyukyoTeijiNoList.size() > 0) {
//				nyukyoTeijiNo = dt.Rows(0).Item(DATA_KEY_TEIJI_NO).ToString
				nyukyoTeijiNo = nyukyoTeijiNoList.get(0).getTeijiNo().toString();
//				teijiDataUpdateDate = DateUtil.ConversionFormatYYYYMMDDHHMMSSWithDelimiter(Date.Parse(dt.Rows(0).Item(UPDATE_DATE).ToString))
				teijiDataUpdateDate = dateFormat.format(nyukyoTeijiNoList.get(0).getUpdateDate().toString());
				// 排他処理
//						ta.GetTeijiDataForUpdate(nyukyoTeijiNo, _
//												 teijiDataUpdateDate)
				List<Skf3022Sc006GetNyukyoTeijiNoDataExp> targetDt = new ArrayList<Skf3022Sc006GetNyukyoTeijiNoDataExp>();
				Skf3022Sc006GetTeijiDataForUpdateExpParameter t1Param = new Skf3022Sc006GetTeijiDataForUpdateExpParameter();
				t1Param.setTeijiNo(Long.parseLong(nyukyoTeijiNo));
				t1Param.setUpdateDate(teijiDataUpdateDate);
				targetDt = skf3022Sc006UpdateTeijiDataExpRepository.getTeijiDataForUpdate(t1Param);
				// データが存在する場合
				if (targetDt.size() > 0) {
					// 提示データを更新
					Long backUpTeijiNo = columnInfoList.getTeijiNo();
					columnInfoList.setTeijiNo(Long.parseLong(nyukyoTeijiNo));
					updCountTJ = skf3022Sc006UpdateTeijiDataExpRepository.updateByPrimaryKeySelective(columnInfoList);
					columnInfoList.setTeijiNo(backUpTeijiNo);
					LogUtils.debugByMsg("継続なし、入居提示データあり、提示データ更新");
				} else {
					// 排他エラー
					LogUtils.debugByMsg("継続なし、入居提示データあり、提示データ排他エラー");
					return -1;
				}
			} else {
				// 提示データを登録
				updCountTJ = skf3022TTeijiDataRepository.insertSelective(columnInfoList);
				nyukyoTeijiNo = columnInfoList.getTeijiNo().toString();
				newFlg = true;
				LogUtils.debugByMsg("継続なし、入居提示データなし、提示データ追加登録");
			}
		}
		// 社宅台帳データの登録
		if (Skf3022Sc006CommonDto.SHATAKU_LOGIN.equals(buttonFlg)) {
			// 提示データ新規の場合、採番した提示番号を利用する
//				If newFlg Then
//					teijiNo = GetToString(columnInfoList.Item(0).Value)
//				End If
			if (newFlg) {
				teijiNo = columnInfoList.getTeijiNo().toString();
			}
			// 旧提示データを更新する
//				If Not String.IsNullOrEmpty(teijiNoOld) And Not teijiNo.Equals(teijiNoOld) Then
			if (!CheckUtils.isEmpty(teijiNoOld) && !Objects.equals(teijiNo, teijiNoOld)) {
				// 社宅管理台帳データの登録（変更、申請なしの退居提示データを更新する）
//					Dim shatakuId As Long = 0
//					Select Case Com_PageBusinessLogic.UpdateShatakuKanriDaichoShatakuData(teijiNoOld, shatakuId, True, String.Empty, sysShoriNenGetsu, userId, ipAddress, systemDate)
//						Case Constant.ReturnStatus.OK
//						Case Else
//							Return 0
//					End Select
				Long shatakuId = skfPageBusinessLogicUtils.updateShatakuKanriDaichoShatakuData(
						Long.parseLong(teijiNoOld), true, "", sysShoriNenGetsu, updateUser, updateProgramId, systemDate);
				if (Objects.equals(shatakuId, null)) {
					LogUtils.debugByMsg("社宅管理台帳データ登録（社宅情報）更新失敗①");
					return 0;
				}
				// 申請なしの変更の提示データを更新する
//							Dim updCountTJ2 As Integer = ta.UpdateTeijiDataOld(teijiNoOld, _
//																			systemDate, _
//																			updateUser, _
//																			updateIp)
				Skf3022TTeijiData updateTeijiDataOldParam = new Skf3022TTeijiData();
				updateTeijiDataOldParam.setShatakuTeijiStatus("5");
				updateTeijiDataOldParam.setBihinTeijiStatus("9");
				updateTeijiDataOldParam.setTeijiNo(Long.parseLong(teijiNoOld));
				updateTeijiDataOldParam.setUpdateDate(systemDate);
				updateTeijiDataOldParam.setUpdateUserId(updateUser);
				updateTeijiDataOldParam.setUpdateProgramId(updateProgramId);
				skf3022Sc006UpdateTeijiDataExpRepository.updateTeijiDataOld(updateTeijiDataOldParam);
				LogUtils.debugByMsg("旧提示データステータス更新（社宅提示、備品提示）UpdateTeijiDataOld");
			}
		}
//		
//			If updateMode Then
		if (updateMode) {
			// 入退居予定データを更新
//						updCountNY = ta.UpdateNyutaikyo(nyukyoYoteiDate, _
//														taikyoYoteiDate, _
//														systemDate, _
//														updateUser, _
//														updateIp, _
//														shainNo, _
//														nyutaikyoKbn)
			Skf3021TNyutaikyoYoteiData param = new Skf3021TNyutaikyoYoteiData();
			param.setShainNo(shainNo);
			param.setNyutaikyoKbn(nyutaikyoKbn);
			param.setNyukyoYoteiDate(nyukyoYoteiDate);
			param.setTaikyoYoteiDate(taikyoYoteiDate);
			param.setUpdateDate(systemDate);
			param.setUpdateUserId(updateUser);
			param.setUpdateProgramId(updateProgramId);
			updCountNY = skf3022Sc006UpdateNyutaikyoExpRepository.updateNyutaikyo(param);
			LogUtils.debugByMsg("入退居予定データを更新");
		} else {
				updCountNY = 1;
				LogUtils.debugByMsg("入退居予定データ更新なし");
		}
		// 備品情報が入力されている場合
//			If bihinCd.Count > 0 Then
		if (bihinCd.size() > 0) {
//				For i = 0 To bihinCd.Count - 1
			for (Map<String, Object> bihinMap : bihinCd) {
//		
//					If (SHATAKU_LOGIN.Equals(buttonFlg) OrElse _
//						 TMP_SAVE.Equals(buttonFlg)) AndAlso newFlg Then
				if (Skf3022Sc006CommonDto.SHATAKU_LOGIN.equals(buttonFlg)
						|| Skf3022Sc006CommonDto.TMP_SAVE.equals(buttonFlg) && newFlg) {
//						'提示備品データテーブルアダプター生成
					// 提示備品データを登録
//								ta.InsertBihinListData(bihinCd.Item(i).BihinLentStatusKbn, _
//																	  systemDate, _
//																	  updateUser, _
//																	  updateIp, _
//																	  nyukyoTeijiNo, _
//																	  bihinCd.Item(i).BihinCd, _
//																	  bihinCd.Item(i).BihinLentStatusOldKbn, _
//																	  nyutaikyoKbn)
					Skf3022TTeijiBihinData record = new Skf3022TTeijiBihinData();
					if (!CheckUtils.isEmpty(bihinMap.get("bihinTaiyoSttsKbn").toString())) {
						record.setBihinLentStatusKbn(bihinMap.get("bihinTaiyoSttsKbn").toString());
					}
					record.setUpdateDate(systemDate);
					record.setUpdateUserId(updateUser);
					record.setUpdateProgramId(updateProgramId);
					record.setInsertDate(systemDate);
					record.setInsertUserId(updateUser);
					record.setInsertProgramId(updateProgramId);
					record.setTeijiNo(Long.parseLong(nyukyoTeijiNo));
					record.setBihinCd(bihinMap.get("bihinCd").toString());
					skf3022Sc006UpdateBihinListDataExpRepository.insertSelective(record);
					LogUtils.debugByMsg("提示備品データ追加登録：" + record.getBihinCd());
				} else {
//						'提示備品データテーブルアダプター生成
					// 提示備品データを更新
//								ta.UpdateBihinListData(bihinCd.Item(i).BihinLentStatusKbn, _
//																	  systemDate, _
//																	  updateUser, _
//																	  updateIp, _
//																	  teijiNo, _
//																	  bihinCd.Item(i).BihinCd, _
//																	  bihinCd.Item(i).BihinLentStatusOldKbn, _
//																	  bihinCd.Item(i).BihinRoomStatusKbn, _
//																	  nyutaikyoKbn)
					Skf3022TTeijiBihinData record = new Skf3022TTeijiBihinData();
					if (CodeConstant.BIHIN_STATE_RENTAL.equals(bihinMap.get("heyaSonaetukeStts").toString())
							&& CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(nyutaikyoKbn)
							&& CodeConstant.BIHIN_STATE_RENTAL.equals(bihinMap.get("bihinTaiyoSttsKbn").toString())) {
						// 部屋備付状態（最新）が”レンタル”、備品貸与状態が”レンタル”の場合
						record.setBihinApplKbn("1");
					} else if (CodeConstant.BIHIN_STATE_HOYU.equals(bihinMap.get("heyaSonaetukeStts").toString())
							&& CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(nyutaikyoKbn)
							&& CodeConstant.BIHIN_STATE_HOYU.equals(bihinMap.get("bihinTaiyoSttsKbn").toString())) {
						// 部屋備付状態（最新）が”会社保有”、備品貸与状態が”会社保有”の場合
						record.setBihinApplKbn("1");
					} else if (CodeConstant.BIHIN_STATE_RENTAL.equals(bihinMap.get("bihinTaiyoSttsKbn").toString())
							&& CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(nyutaikyoKbn)
							&& (CodeConstant.BIHIN_STATE_KYOYO.equals(bihinMap.get("bihinTaiyoSttsOldKbn").toString())
									|| CodeConstant.BIHIN_STATE_NONE.equals(bihinMap.get("bihinTaiyoSttsOldKbn").toString())
									|| CodeConstant.BIHIN_STATE_HOYU.equals(bihinMap.get("bihinTaiyoSttsOldKbn").toString()))) {
						// 備品貸与状態変更前”なし”或は”共有”或は”会社保有”、備品貸与状態が”レンタル”の場合
						record.setBihinApplKbn("2");
					} else if (!CodeConstant.BIHIN_STATE_RENTAL.equals(bihinMap.get("bihinTaiyoSttsKbn").toString())
							&& CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(nyutaikyoKbn)
							&& CodeConstant.BIHIN_STATE_RENTAL.equals(bihinMap.get("bihinTaiyoSttsOldKbn").toString())) {
						// 備品貸与状態変更前”レンタル”、備品貸与状態が”レンタル”以外の場合
						record.setBihinApplKbn("1");
					}

//					If bihinLentStatusKbn.Equals(lblRoomBihinCd) AndAlso _
//						Constant.NyutaikyoKbn.TAIKYO.Equals(nyutaikyoKbn) Then
					if (Objects.equals(bihinMap.get("bihinTaiyoSttsKbn"), bihinMap.get("heyaSonaetukeStts"))	// kamiushiya
							&& CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(nyutaikyoKbn)) {
//						If Not Constant.BihinLentStatusKbn.NASHI.Equals(lblRoomBihinCd) AndAlso _
//							Not Constant.BihinLentStatusKbn.SHARE.Equals(lblRoomBihinCd) AndAlso _
//							Not Constant.BihinLentStatusKbn.SONAETSUKE.Equals(lblRoomBihinCd) Then
						if (!CodeConstant.BIHIN_STATE_NONE.equals(bihinMap.get("heyaSonaetukeStts").toString())
								&& !CodeConstant.BIHIN_STATE_KYOYO.equals(bihinMap.get("heyaSonaetukeStts").toString())
								&& !CodeConstant.BIHIN_STATE_SONAETSUKE.equals(bihinMap.get("heyaSonaetukeStts").toString())) {
//							sql.Append(" BIHIN_RETURN_KBN = '0',")
							record.setBihinReturnKbn("0");
						} else {
//							sql.Append(" BIHIN_RETURN_KBN = NULL,")
							record.setBihinReturnKbn(null);
						}
					}
					// 備品備付区分が”共有”の場合も、返却備品として扱う。
					// 備品貸与状態変更前”レンタル”の場合、備品返却区分を”3”に設定する
//					If (Constant.BihinLentStatusKbn.NASHI.Equals(lblRoomBihinCd) Or Constant.BihinLentStatusKbn.SHARE.Equals(lblRoomBihinCd)) AndAlso _
//						Constant.NyutaikyoKbn.TAIKYO.Equals(nyutaikyoKbn) AndAlso _
//						Constant.BihinLentStatusKbn.RENTAL.Equals(bihinLentStatusKbn) Then
					if ((CodeConstant.BIHIN_STATE_NONE.equals(bihinMap.get("heyaSonaetukeStts").toString())
							|| CodeConstant.BIHIN_STATE_KYOYO.equals(bihinMap.get("heyaSonaetukeStts").toString()))
							&& CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(nyutaikyoKbn)
							&& CodeConstant.BIHIN_STATE_RENTAL.equals(bihinMap.get("bihinTaiyoSttsKbn").toString())) {
//						sql.Append(" BIHIN_RETURN_KBN = '3',")
						record.setBihinReturnKbn("3");
					}
					// 備品貸与状態変更前”会社保有”の場合、備品返却区分を”2”に設定する
//					If (Constant.BihinLentStatusKbn.NASHI.Equals(lblRoomBihinCd) Or Constant.BihinLentStatusKbn.SHARE.Equals(lblRoomBihinCd)) AndAlso _
//						Constant.NyutaikyoKbn.TAIKYO.Equals(nyutaikyoKbn) AndAlso _
//						Constant.BihinLentStatusKbn.KAISHA_HOYU.Equals(bihinLentStatusKbn) Then
					if ((CodeConstant.BIHIN_STATE_NONE.equals(bihinMap.get("heyaSonaetukeStts").toString())
							|| CodeConstant.BIHIN_STATE_KYOYO.equals(bihinMap.get("heyaSonaetukeStts").toString()))
							&& CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(nyutaikyoKbn)
							&& CodeConstant.BIHIN_STATE_HOYU.equals(bihinMap.get("bihinTaiyoSttsKbn").toString())) {
//						sql.Append(" BIHIN_RETURN_KBN = '2',")
						record.setBihinReturnKbn("2");
					}
					record.setTeijiNo(Long.parseLong(teijiNo));
					record.setBihinCd(bihinMap.get("bihinCd").toString());
					record.setBihinLentStatusKbn(bihinMap.get("bihinTaiyoSttsKbn").toString());
					record.setUpdateProgramId(updateProgramId);
					record.setUpdateUserId(updateUser);
					record.setUpdateDate(systemDate);

					skf3022Sc006UpdateBihinListDataExpRepository.updateByPrimaryKeySelective(record);
					LogUtils.debugByMsg("提示備品データ更新, bihinCd:" + record.getBihinCd()
										+ ", bihinReturnKbn:" + record.getBihinReturnKbn()
										+ ", bihinApplKbn:" + record.getBihinApplKbn());
				}
				updCountTBD += 1;
			}
		}
//		
//			'社宅部屋情報マスタデータテーブルアダプター生成
//			Using ta As New TB_M_SHATAKU_ROOMTableAdapter
//				If Not String.IsNullOrEmpty(shatakuKanriNo) AndAlso _
//				   Not String.IsNullOrEmpty(shatakuRoomKanriNo) AndAlso _
//				   (Not shatakuKanriNo.Equals(shatakuKanriNoOld) OrElse _
//				   Not shatakuRoomKanriNo.Equals(shatakuRoomKanriNoOld)) Then
		if (!CheckUtils.isEmpty(shatakuKanriNo) && !CheckUtils.isEmpty(shatakuRoomKanriNo)
				&& (!Objects.equals(shatakuKanriNo, shatakuKanriNoOld) || !Objects.equals(shatakuRoomKanriNo, shatakuRoomKanriNoOld))) {
//					If Not String.IsNullOrEmpty(shatakuKanriNoOld) AndAlso _
//					   Not String.IsNullOrEmpty(shatakuRoomKanriNoOld) Then
			if (!CheckUtils.isEmpty(shatakuKanriNoOld) && !CheckUtils.isEmpty(shatakuRoomKanriNoOld)) {
//						'排他処理
//						Using targetDt As TB_M_SHATAKU_ROOMDataTable = _
//							ta.GetShatakuRoomForUpdate(shatakuKanriNoOld, _
//													   shatakuRoomKanriNoOld, _
//													   shatakuRoomOldUpdateDate, _
//													   False)
				Skf3022Sc006GetShatakuRoomForUpdateExpParameter param = new Skf3022Sc006GetShatakuRoomForUpdateExpParameter();
				param.setShatakuKanriNo(Long.parseLong(shatakuKanriNoOld));
				param.setShatakuRoomKanriNo(Long.parseLong(shatakuRoomKanriNoOld));
				param.setUpdateDate(shatakuRoomOldUpdateDate);
				param.setIsnew(false);
				List<Skf3022Sc006GetShatakuRoomExp> targetDt = new ArrayList<Skf3022Sc006GetShatakuRoomExp>();
				targetDt = skf3022Sc006UpdateShatakuRoomExpRepository.getShatakuRoomForUpdate(param);
				// データが存在する場合
//							If DATA_COUNT_0 < targetDt.Rows.Count Then
				if (targetDt.size() > 0) {
					// 社宅部屋情報マスタを更新
//									updCountOldSR = ta.UpdateShatakuRoom(Constant.LendJokyo.NASHI, _
//																		 systemDate, _
//																		 updateUser, _
//																		 updateIp, _
//																		 CDec(shatakuKanriNoOld), _
//																		 CDec(shatakuRoomKanriNoOld))
					Skf3010MShatakuRoom record = new Skf3010MShatakuRoom();
					record.setLendJokyo(CodeConstant.LEND_JOKYO_NASHI);
					record.setShatakuKanriNo(Long.parseLong(shatakuKanriNoOld));
					record.setShatakuRoomKanriNo(Long.parseLong(shatakuRoomKanriNoOld));
					record.setUpdateDate(systemDate);
					record.setUpdateUserId(updateUser);
					record.setUpdateProgramId(updateProgramId);
					updCountOldSR = skf3022Sc006UpdateShatakuRoomExpRepository.updateByPrimaryKeySelective(record);
					LogUtils.debugByMsg("社宅部屋情報マスタ更新, LendJokyo:貸与なし");
				} else {
					// 排他エラーメッセージ
					LogUtils.debugByMsg("社宅部屋情報マスタ排他エラー①");
					return -1;
				}
			}
			// 作成完了／社宅管理台帳登録の場合：貸与状況→"2"
//					If syoriFlg Then
			if (syoriFlg) {
				// 排他処理
//						Using targetDt As TB_M_SHATAKU_ROOMDataTable = _
//							ta.GetShatakuRoomForUpdate(shatakuKanriNo, _
//													   shatakuRoomKanriNo, _
//													   shatakuRoomUpdateDate, _
//													   True)
				Skf3022Sc006GetShatakuRoomForUpdateExpParameter param = new Skf3022Sc006GetShatakuRoomForUpdateExpParameter();
				param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
				param.setShatakuRoomKanriNo(Long.parseLong(shatakuRoomKanriNo));
				param.setUpdateDate(shatakuRoomUpdateDate);
				param.setIsnew(true);
				List<Skf3022Sc006GetShatakuRoomExp> targetDt = new ArrayList<Skf3022Sc006GetShatakuRoomExp>();
				targetDt = skf3022Sc006UpdateShatakuRoomExpRepository.getShatakuRoomForUpdate(param);
				// データが存在する場合
//							If DATA_COUNT_0 < targetDt.Rows.Count Then
				if (targetDt.size() > 0) {
					// 社宅部屋情報マスタを更新
//									updCountSR = ta.UpdateShatakuRoom(Constant.LendJokyo.TAIYOCHU, _
//																	  systemDate, _
//																	  updateUser, _
//																	  updateIp, _
//																	  CDec(shatakuKanriNo), _
//																	  CDec(shatakuRoomKanriNo))
					Skf3010MShatakuRoom record = new Skf3010MShatakuRoom();
					record.setLendJokyo(CodeConstant.LEND_JOKYO_TAIYOCHU);
					record.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
					record.setShatakuRoomKanriNo(Long.parseLong(shatakuRoomKanriNo));
					record.setUpdateDate(systemDate);
					record.setUpdateUserId(updateUser);
					record.setUpdateProgramId(updateProgramId);
					updCountSR = skf3022Sc006UpdateShatakuRoomExpRepository.updateByPrimaryKeySelective(record);
					LogUtils.debugByMsg("社宅部屋情報マスタ更新, LendJokyo:貸与中");
				} else {
					// 排他エラーメッセージ
					LogUtils.debugByMsg("社宅部屋情報マスタ排他エラー②");
					return -1;
				}
			// 作成完了／社宅管理台帳登録以外の場合：貸与状況→"1"
			} else {
				// 排他処理
//						Using targetDt As TB_M_SHATAKU_ROOMDataTable = _
//							ta.GetShatakuRoomForUpdate(shatakuKanriNo, _
//													   shatakuRoomKanriNo, _
//													   shatakuRoomUpdateDate, _
//													   True)
				Skf3022Sc006GetShatakuRoomForUpdateExpParameter param = new Skf3022Sc006GetShatakuRoomForUpdateExpParameter();
				param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
				param.setShatakuRoomKanriNo(Long.parseLong(shatakuRoomKanriNo));
				param.setUpdateDate(shatakuRoomUpdateDate);
				param.setIsnew(true);
				List<Skf3022Sc006GetShatakuRoomExp> targetDt = new ArrayList<Skf3022Sc006GetShatakuRoomExp>();
				targetDt = skf3022Sc006UpdateShatakuRoomExpRepository.getShatakuRoomForUpdate(param);
				// データが存在する場合
//							If DATA_COUNT_0 < targetDt.Rows.Count Then
				if (targetDt.size() > 0) {
					// 社宅部屋情報マスタを更新
//									updCountSR = ta.UpdateShatakuRoom(Constant.LendJokyo.TEIJICHU, _
//																	  systemDate, _
//																	  updateUser, _
//																	  updateIp, _
//																	  CDec(shatakuKanriNo), _
//																	  CDec(shatakuRoomKanriNo))
					Skf3010MShatakuRoom record = new Skf3010MShatakuRoom();
					record.setLendJokyo(CodeConstant.LEND_JOKYO_TEIJICHU);
					record.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
					record.setShatakuRoomKanriNo(Long.parseLong(shatakuRoomKanriNo));
					record.setUpdateDate(systemDate);
					record.setUpdateUserId(updateUser);
					record.setUpdateProgramId(updateProgramId);
					updCountSR = skf3022Sc006UpdateShatakuRoomExpRepository.updateByPrimaryKeySelective(record);
					LogUtils.debugByMsg("社宅部屋情報マスタ更新, LendJokyo:提示中");
				} else {
					// 排他エラーメッセージ
					LogUtils.debugByMsg("社宅部屋情報マスタ排他エラー③");
					return -1;
				}
			}
//		
//					'退居、かつ、作成完了／社宅管理台帳登録の場合
//				ElseIf Constant.NyutaikyoKbn.TAIKYO.Equals(nyutaikyoKbn) AndAlso syoriFlg Then
		} else if (CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(nyutaikyoKbn) && syoriFlg) {
			// 排他処理
//					Using targetDt As TB_M_SHATAKU_ROOMDataTable = _
//						ta.GetShatakuRoomForUpdate(shatakuKanriNoOld, _
//												   shatakuRoomKanriNoOld, _
//												   shatakuRoomOldUpdateDate, _
//												   False)
			Skf3022Sc006GetShatakuRoomForUpdateExpParameter param = new Skf3022Sc006GetShatakuRoomForUpdateExpParameter();
			param.setShatakuKanriNo(Long.parseLong(shatakuKanriNoOld));
			param.setShatakuRoomKanriNo(Long.parseLong(shatakuRoomKanriNoOld));
			param.setUpdateDate(shatakuRoomOldUpdateDate);
			param.setIsnew(false);
			List<Skf3022Sc006GetShatakuRoomExp> targetDt = new ArrayList<Skf3022Sc006GetShatakuRoomExp>();
			targetDt = skf3022Sc006UpdateShatakuRoomExpRepository.getShatakuRoomForUpdate(param);
			// データが存在する場合
//						If DATA_COUNT_0 < targetDt.Rows.Count Then
			if (targetDt.size() > 0) {
				// 社宅部屋情報マスタを更新
//								updCountOldSR = ta.UpdateShatakuRoom(Constant.LendJokyo.TAIKYO_YOTEI, _
//																	 systemDate, _
//																	 updateUser, _
//																	 updateIp, _
//																	 CDec(shatakuKanriNoOld), _
//																	 CDec(shatakuRoomKanriNoOld))
				Skf3010MShatakuRoom record = new Skf3010MShatakuRoom();
				record.setLendJokyo(CodeConstant.LEND_JOKYO_TAIKYO_YOTEI);
				record.setShatakuKanriNo(Long.parseLong(shatakuKanriNoOld));
				record.setShatakuRoomKanriNo(Long.parseLong(shatakuRoomKanriNoOld));
				record.setUpdateDate(systemDate);
				record.setUpdateUserId(updateUser);
				record.setUpdateProgramId(updateProgramId);
				updCountOldSR = skf3022Sc006UpdateShatakuRoomExpRepository.updateByPrimaryKeySelective(record);
				LogUtils.debugByMsg("社宅部屋情報マスタ更新, LendJokyo:退居予定");
			} else {
				// 排他エラーメッセージ
				LogUtils.debugByMsg("社宅部屋情報マスタ排他エラー④");
				return -1;
			}
		}

		/** 駐車場区画1 */
//			'社宅駐車場区画情報マスタデータテーブルアダプター生成
//			Using ta As New TB_M_SHATAKU_PARKING_BLOCKTableAdapter
//				If Not shatakuKanriNo.Equals(shatakuKanriNoOld) OrElse _
//				   Not chushajoNoOne.Equals(chushajoNoOneOld) Then
		if (!Objects.equals(shatakuKanriNo, shatakuKanriNoOld) || !Objects.equals(chushajoNoOne, chushajoNoOneOld)) {
//					If Not String.IsNullOrEmpty(shatakuKanriNoOld) AndAlso _
//					   Not String.IsNullOrEmpty(chushajoNoOneOld) Then
			if (!CheckUtils.isEmpty(shatakuKanriNoOld) && !CheckUtils.isEmpty(chushajoNoOneOld)) {
				// 排他処理
//						Using targetDt As TB_M_SHATAKU_PARKING_BLOCKDataTable = _
//							ta.GetShatakuParkingBlockForUpdate(shatakuKanriNoOld, _
//															   chushajoNoOneOld, _
//															   shatakuParkingBlockOld1UpdateDate, _
//															   False)
				Skf3022Sc006GetShatakuParkingBlockForUpdateExpParameter param = new Skf3022Sc006GetShatakuParkingBlockForUpdateExpParameter();
				param.setShatakuKanriNo(Long.parseLong(shatakuKanriNoOld));
				param.setParkingKanriNo(Long.parseLong(chushajoNoOneOld));
				param.setUpdateDate(shatakuParkingBlockOld1UpdateDate);
				param.setIsnew(false);
				List<Skf3022Sc006GetShatakuParkingBlockExp> targetDt = new ArrayList<Skf3022Sc006GetShatakuParkingBlockExp>();
				targetDt = skf3022Sc006UpdateParkingBlockExpRepository.getShatakuParkingBlockForUpdate(param);
				// データが存在する場合
//							If DATA_COUNT_0 < targetDt.Rows.Count Then
				if (targetDt.size() > 0) {
					// 社宅駐車場区画情報マスタを更新
//									updCountOldSPB1 = ta.UpdateParkingBlock(Constant.LendJokyo.NASHI, _
//																			systemDate, _
//																			updateUser, _
//																			updateIp, _
//																			CDec(shatakuKanriNoOld), _
//																			CDec(chushajoNoOneOld))
					Skf3010MShatakuParkingBlock record = new Skf3010MShatakuParkingBlock();
					record.setParkingLendJokyo(CodeConstant.LEND_JOKYO_NASHI);
					record.setShatakuKanriNo(Long.parseLong(shatakuKanriNoOld));
					record.setParkingKanriNo(Long.parseLong(chushajoNoOneOld));
					record.setUpdateDate(systemDate);
					record.setUpdateUserId(updateUser);
					record.setUpdateProgramId(updateProgramId);
					updCountOldSPB1 = skf3022Sc006UpdateParkingBlockExpRepository.updateByPrimaryKeySelective(record);
					LogUtils.debugByMsg("駐車場区画1:更新①");
				} else {
					// 排他エラーメッセージ
					LogUtils.debugByMsg("駐車場区画1:排他エラー①");
					return -1;
				}
			}
//		
//					If Not String.IsNullOrEmpty(shatakuKanriNo) AndAlso _
//					   Not String.IsNullOrEmpty(chushajoNoOne) Then
			if (!CheckUtils.isEmpty(shatakuKanriNo) && !CheckUtils.isEmpty(chushajoNoOne)) {
				// 作成完了／社宅管理台帳登録の場合：貸与状況→"2"
//						If syoriFlg Then
				if (syoriFlg) {
					// 排他処理
//							Using targetDt As TB_M_SHATAKU_PARKING_BLOCKDataTable = _
//								ta.GetShatakuParkingBlockForUpdate(shatakuKanriNo, _
//																   chushajoNoOne, _
//																   shatakuParkingBlock1UpdateDate, _
//																   True)
					Skf3022Sc006GetShatakuParkingBlockForUpdateExpParameter param = new Skf3022Sc006GetShatakuParkingBlockForUpdateExpParameter();
					param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
					param.setParkingKanriNo(Long.parseLong(chushajoNoOne));
					param.setUpdateDate(shatakuParkingBlock1UpdateDate);
					param.setIsnew(true);
					List<Skf3022Sc006GetShatakuParkingBlockExp> targetDt = new ArrayList<Skf3022Sc006GetShatakuParkingBlockExp>();
					targetDt = skf3022Sc006UpdateParkingBlockExpRepository.getShatakuParkingBlockForUpdate(param);
					// データが存在する場合
//								If DATA_COUNT_0 < targetDt.Rows.Count Then
					if (targetDt.size() > 0) {
						// 社宅駐車場区画情報マスタを更新
//										updCountSPB1 = ta.UpdateParkingBlock(Constant.LendJokyo.TAIYOCHU, _
//																			 systemDate, _
//																			 updateUser, _
//																			 updateIp, _
//																			 CDec(shatakuKanriNo), _
//																			 CDec(chushajoNoOne))
						Skf3010MShatakuParkingBlock record = new Skf3010MShatakuParkingBlock();
						record.setParkingLendJokyo(CodeConstant.LEND_JOKYO_TAIYOCHU);
						record.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
						record.setParkingKanriNo(Long.parseLong(chushajoNoOne));
						record.setUpdateDate(systemDate);
						record.setUpdateUserId(updateUser);
						record.setUpdateProgramId(updateProgramId);
						updCountSPB1 = skf3022Sc006UpdateParkingBlockExpRepository.updateByPrimaryKeySelective(record);
						LogUtils.debugByMsg("駐車場区画1:更新②");
					} else {
						// 排他エラーメッセージ
						LogUtils.debugByMsg("駐車場区画1:排他エラー②");
						return -1;
					}
				// 作成完了／社宅管理台帳登録以外の場合：貸与状況→"1"
				} else {
					// 排他処理
//							Using targetDt As TB_M_SHATAKU_PARKING_BLOCKDataTable = _
//								ta.GetShatakuParkingBlockForUpdate(shatakuKanriNo, _
//																   chushajoNoOne, _
//																   shatakuParkingBlock1UpdateDate, _
//																   True)
					Skf3022Sc006GetShatakuParkingBlockForUpdateExpParameter param = new Skf3022Sc006GetShatakuParkingBlockForUpdateExpParameter();
					param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
					param.setParkingKanriNo(Long.parseLong(chushajoNoOne));
					param.setUpdateDate(shatakuParkingBlock1UpdateDate);
					param.setIsnew(true);
					List<Skf3022Sc006GetShatakuParkingBlockExp> targetDt = new ArrayList<Skf3022Sc006GetShatakuParkingBlockExp>();
					targetDt = skf3022Sc006UpdateParkingBlockExpRepository.getShatakuParkingBlockForUpdate(param);
					// データが存在する場合
//								If DATA_COUNT_0 < targetDt.Rows.Count Then
					if (targetDt.size() > 0) {
						// 社宅駐車場区画情報マスタを更新
//										updCountSPB1 = ta.UpdateParkingBlock(Constant.LendJokyo.TEIJICHU, _
//																			 systemDate, _
//																			 updateUser, _
//																			 updateIp, _
//																			 CDec(shatakuKanriNo), _
//																			 CDec(chushajoNoOne))
						Skf3010MShatakuParkingBlock record = new Skf3010MShatakuParkingBlock();
						record.setParkingLendJokyo(CodeConstant.LEND_JOKYO_TEIJICHU);
						record.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
						record.setParkingKanriNo(Long.parseLong(chushajoNoOne));
						record.setUpdateDate(systemDate);
						record.setUpdateUserId(updateUser);
						record.setUpdateProgramId(updateProgramId);
						updCountSPB1 = skf3022Sc006UpdateParkingBlockExpRepository.updateByPrimaryKeySelective(record);
						LogUtils.debugByMsg("駐車場区画1:更新③");
					} else {
						// 排他エラーメッセージ
						LogUtils.debugByMsg("駐車場区画1:排他エラー③");
						return -1;
					}
				}
			}
		// 退居、かつ、作成完了／社宅管理台帳登録の場合
//				ElseIf Constant.NyutaikyoKbn.TAIKYO.Equals(nyutaikyoKbn) AndAlso _
//				Not String.IsNullOrEmpty(chushajoNoOneOld) AndAlso syoriFlg Then
		} else if (CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(nyutaikyoKbn)
				&& !CheckUtils.isEmpty(chushajoNoOneOld) && syoriFlg) {
			// 排他処理
//					Using targetDt As TB_M_SHATAKU_PARKING_BLOCKDataTable = _
//						ta.GetShatakuParkingBlockForUpdate(shatakuKanriNoOld, _
//														   chushajoNoOneOld, _
//														   shatakuParkingBlockOld1UpdateDate, _
//														   False)
			Skf3022Sc006GetShatakuParkingBlockForUpdateExpParameter param = new Skf3022Sc006GetShatakuParkingBlockForUpdateExpParameter();
			param.setShatakuKanriNo(Long.parseLong(shatakuKanriNoOld));
			param.setParkingKanriNo(Long.parseLong(chushajoNoOneOld));
			param.setUpdateDate(shatakuParkingBlockOld1UpdateDate);
			param.setIsnew(false);
			List<Skf3022Sc006GetShatakuParkingBlockExp> targetDt = new ArrayList<Skf3022Sc006GetShatakuParkingBlockExp>();
			targetDt = skf3022Sc006UpdateParkingBlockExpRepository.getShatakuParkingBlockForUpdate(param);
			// データが存在する場合
//						If DATA_COUNT_0 < targetDt.Rows.Count Then
			if (targetDt.size() > 0) {
				// 社宅駐車場区画情報マスタを更新
//								updCountOldSPB1 = ta.UpdateParkingBlock(Constant.LendJokyo.TAIKYO_YOTEI, _
//																		systemDate, _
//																		updateUser, _
//																		updateIp, _
//																		CDec(shatakuKanriNoOld), _
//																		CDec(chushajoNoOneOld))
				Skf3010MShatakuParkingBlock record = new Skf3010MShatakuParkingBlock();
				record.setParkingLendJokyo(CodeConstant.LEND_JOKYO_TAIKYO_YOTEI);
				record.setShatakuKanriNo(Long.parseLong(shatakuKanriNoOld));
				record.setParkingKanriNo(Long.parseLong(chushajoNoOneOld));
				record.setUpdateDate(systemDate);
				record.setUpdateUserId(updateUser);
				record.setUpdateProgramId(updateProgramId);
				updCountOldSPB1 = skf3022Sc006UpdateParkingBlockExpRepository.updateByPrimaryKeySelective(record);
				LogUtils.debugByMsg("駐車場区画1:更新④");
			} else {
				// 排他エラーメッセージ
				LogUtils.debugByMsg("駐車場区画1:排他エラー④");
				return -1;
			}
		}
		/** 駐車場区画2 */
//			'社宅駐車場区画情報マスタデータテーブルアダプター生成
//			Using ta As New TB_M_SHATAKU_PARKING_BLOCKTableAdapter
//				If Not shatakuKanriNo.Equals(shatakuKanriNoOld) OrElse _
//				   Not chushajoNoTwo.Equals(chushajoNoTwoOld) Then
//					If Not String.IsNullOrEmpty(shatakuKanriNoOld) AndAlso _
//					   Not String.IsNullOrEmpty(chushajoNoTwoOld) Then
		if (!Objects.equals(shatakuKanriNo, shatakuKanriNoOld) || !Objects.equals(chushajoNoTwo, chushajoNoTwoOld)) {
			if (!CheckUtils.isEmpty(shatakuKanriNoOld) && !CheckUtils.isEmpty(chushajoNoTwoOld)) {
				// 排他処理
//						Using targetDt As TB_M_SHATAKU_PARKING_BLOCKDataTable = _
//							ta.GetShatakuParkingBlockForUpdate(shatakuKanriNoOld, _
//															   chushajoNoTwoOld, _
//															   shatakuParkingBlockOld2UpdateDate, _
//															   False)
				Skf3022Sc006GetShatakuParkingBlockForUpdateExpParameter param = new Skf3022Sc006GetShatakuParkingBlockForUpdateExpParameter();
				param.setShatakuKanriNo(Long.parseLong(shatakuKanriNoOld));
				param.setParkingKanriNo(Long.parseLong(chushajoNoTwoOld));
				param.setUpdateDate(shatakuParkingBlockOld2UpdateDate);
				param.setIsnew(false);
				List<Skf3022Sc006GetShatakuParkingBlockExp> targetDt = new ArrayList<Skf3022Sc006GetShatakuParkingBlockExp>();
				targetDt = skf3022Sc006UpdateParkingBlockExpRepository.getShatakuParkingBlockForUpdate(param);
				// データが存在する場合
//							If DATA_COUNT_0 < targetDt.Rows.Count Then
				if (targetDt.size() > 0) {
					// 社宅駐車場区画情報マスタを更新
//									updCountOldSPB2 = ta.UpdateParkingBlock(Constant.LendJokyo.NASHI, _
//																			systemDate, _
//																			updateUser, _
//																			updateIp, _
//																			CDec(shatakuKanriNoOld), _
//																			CDec(chushajoNoTwoOld))
					Skf3010MShatakuParkingBlock record = new Skf3010MShatakuParkingBlock();
					record.setParkingLendJokyo(CodeConstant.LEND_JOKYO_NASHI);
					record.setShatakuKanriNo(Long.parseLong(shatakuKanriNoOld));
					record.setParkingKanriNo(Long.parseLong(chushajoNoTwoOld));
					record.setUpdateDate(systemDate);
					record.setUpdateUserId(updateUser);
					record.setUpdateProgramId(updateProgramId);
					updCountOldSPB2 = skf3022Sc006UpdateParkingBlockExpRepository.updateByPrimaryKeySelective(record);
					LogUtils.debugByMsg("駐車場区画2:更新①");
				} else {
					// 排他エラーメッセージ
					LogUtils.debugByMsg("駐車場区画2:排他エラー①");
					return -1;
				}
			}
//		
//					If Not String.IsNullOrEmpty(shatakuKanriNo) AndAlso _
//					   Not String.IsNullOrEmpty(chushajoNoTwo) Then
			if (!CheckUtils.isEmpty(shatakuKanriNo) && !CheckUtils.isEmpty(chushajoNoTwo)) {
				// 作成完了／社宅管理台帳登録の場合：貸与状況→"2"
//						If syoriFlg Then
				if (syoriFlg) {
					// 排他処理
//							Using targetDt As TB_M_SHATAKU_PARKING_BLOCKDataTable = _
//								ta.GetShatakuParkingBlockForUpdate(shatakuKanriNo, _
//																   chushajoNoTwo, _
//																   shatakuParkingBlock2UpdateDate, _
//																   True)
					Skf3022Sc006GetShatakuParkingBlockForUpdateExpParameter param = new Skf3022Sc006GetShatakuParkingBlockForUpdateExpParameter();
					param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
					param.setParkingKanriNo(Long.parseLong(chushajoNoTwo));
					param.setUpdateDate(shatakuParkingBlock2UpdateDate);
					param.setIsnew(true);
					List<Skf3022Sc006GetShatakuParkingBlockExp> targetDt = new ArrayList<Skf3022Sc006GetShatakuParkingBlockExp>();
					targetDt = skf3022Sc006UpdateParkingBlockExpRepository.getShatakuParkingBlockForUpdate(param);
					// データが存在する場合
//								If DATA_COUNT_0 < targetDt.Rows.Count Then
					if (targetDt.size() > 0) {
						// 社宅駐車場区画情報マスタを更新
//										updCountSPB2 = ta.UpdateParkingBlock(Constant.LendJokyo.TAIYOCHU, _
//																			 systemDate, _
//																			 updateUser, _
//																			 updateIp, _
//																			 CDec(shatakuKanriNo), _
//																			 CDec(chushajoNoTwo))
						Skf3010MShatakuParkingBlock record = new Skf3010MShatakuParkingBlock();
						record.setParkingLendJokyo(CodeConstant.LEND_JOKYO_TAIYOCHU);
						record.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
						record.setParkingKanriNo(Long.parseLong(chushajoNoTwo));
						record.setUpdateDate(systemDate);
						record.setUpdateUserId(updateUser);
						record.setUpdateProgramId(updateProgramId);
						updCountSPB2 = skf3022Sc006UpdateParkingBlockExpRepository.updateByPrimaryKeySelective(record);
						LogUtils.debugByMsg("駐車場区画2:更新②");
					} else {
						// 排他エラーメッセージ
						LogUtils.debugByMsg("駐車場区画2:排他エラー②");
						return -1;
					}
				// 作成完了／社宅管理台帳登録以外の場合：貸与状況→"1"
				} else {
					// 排他処理
//							Using targetDt As TB_M_SHATAKU_PARKING_BLOCKDataTable = _
//								ta.GetShatakuParkingBlockForUpdate(shatakuKanriNo, _
//																   chushajoNoTwo, _
//																   shatakuParkingBlock2UpdateDate, _
//																   True)
					Skf3022Sc006GetShatakuParkingBlockForUpdateExpParameter param = new Skf3022Sc006GetShatakuParkingBlockForUpdateExpParameter();
					param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
					param.setParkingKanriNo(Long.parseLong(chushajoNoTwo));
					param.setUpdateDate(shatakuParkingBlock2UpdateDate);
					param.setIsnew(true);
					List<Skf3022Sc006GetShatakuParkingBlockExp> targetDt = new ArrayList<Skf3022Sc006GetShatakuParkingBlockExp>();
					targetDt = skf3022Sc006UpdateParkingBlockExpRepository.getShatakuParkingBlockForUpdate(param);
					// データが存在する場合
//								If DATA_COUNT_0 < targetDt.Rows.Count Then
					if (targetDt.size() > 0) {
						// 社宅駐車場区画情報マスタを更新
//										updCountSPB2 = ta.UpdateParkingBlock(Constant.LendJokyo.TEIJICHU, _
//																			 systemDate, _
//																			 updateUser, _
//																			 updateIp, _
//																			 CDec(shatakuKanriNo), _
//																			 CDec(chushajoNoTwo))
						Skf3010MShatakuParkingBlock record = new Skf3010MShatakuParkingBlock();
						record.setParkingLendJokyo(CodeConstant.LEND_JOKYO_TEIJICHU);
						record.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
						record.setParkingKanriNo(Long.parseLong(chushajoNoTwo));
						record.setUpdateDate(systemDate);
						record.setUpdateUserId(updateUser);
						record.setUpdateProgramId(updateProgramId);
						updCountSPB2 = skf3022Sc006UpdateParkingBlockExpRepository.updateByPrimaryKeySelective(record);
						LogUtils.debugByMsg("駐車場区画2:更新③");
					} else {
						// 排他エラーメッセージ
						LogUtils.debugByMsg("駐車場区画2:排他エラー③");
						return -1;
					}
				}
			}
			// 退居、かつ、作成完了／社宅管理台帳登録の場合
//				ElseIf Constant.NyutaikyoKbn.TAIKYO.Equals(nyutaikyoKbn) AndAlso _
//				Not String.IsNullOrEmpty(chushajoNoTwoOld) AndAlso syoriFlg Then
		} else if (CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(nyutaikyoKbn)
					&& !CheckUtils.isEmpty(chushajoNoTwoOld) && syoriFlg) {
//					'排他処理
//					Using targetDt As TB_M_SHATAKU_PARKING_BLOCKDataTable = _
//						ta.GetShatakuParkingBlockForUpdate(shatakuKanriNoOld, _
//														   chushajoNoTwoOld, _
//														   shatakuParkingBlockOld2UpdateDate, _
//														   False)
			Skf3022Sc006GetShatakuParkingBlockForUpdateExpParameter param = new Skf3022Sc006GetShatakuParkingBlockForUpdateExpParameter();
			param.setShatakuKanriNo(Long.parseLong(shatakuKanriNoOld));
			param.setParkingKanriNo(Long.parseLong(chushajoNoTwoOld));
			param.setUpdateDate(shatakuParkingBlockOld2UpdateDate);
			param.setIsnew(false);
			List<Skf3022Sc006GetShatakuParkingBlockExp> targetDt = new ArrayList<Skf3022Sc006GetShatakuParkingBlockExp>();
			targetDt = skf3022Sc006UpdateParkingBlockExpRepository.getShatakuParkingBlockForUpdate(param);
				// データが存在する場合
//						If DATA_COUNT_0 < targetDt.Rows.Count Then
			if (targetDt.size() > 0) {
				// 社宅駐車場区画情報マスタを更新
//								updCountOldSPB2 = ta.UpdateParkingBlock(Constant.LendJokyo.TAIKYO_YOTEI, _
//																		systemDate, _
//																		updateUser, _
//																		updateIp, _
//																		CDec(shatakuKanriNoOld), _
//																		CDec(chushajoNoTwoOld))
				Skf3010MShatakuParkingBlock record = new Skf3010MShatakuParkingBlock();
				record.setParkingLendJokyo(CodeConstant.LEND_JOKYO_TAIKYO_YOTEI);
				record.setShatakuKanriNo(Long.parseLong(shatakuKanriNoOld));
				record.setParkingKanriNo(Long.parseLong(chushajoNoTwoOld));
				record.setUpdateDate(systemDate);
				record.setUpdateUserId(updateUser);
				record.setUpdateProgramId(updateProgramId);
				updCountOldSPB2 = skf3010MShatakuParkingBlockRepository.updateByPrimaryKeySelective(record);
				LogUtils.debugByMsg("駐車場区画2:更新④");
			} else {
				// 排他エラーメッセージ
				LogUtils.debugByMsg("駐車場区画2:排他エラー④");
				return -1;
			}
		}
		// 一時保存/作成完了/台帳登録をクリックして、且つ、使用料変更がある場合
//			If (TMP_SAVE.Equals(buttonFlg) Or CREATE.Equals(buttonFlg) Or SHATAKU_LOGIN.Equals(buttonFlg)) And _
//				(SIYOURYOFLG_HAVE.Equals(siyouryoFlg))Then
		if (Skf3022Sc006CommonDto.TMP_SAVE.equals(buttonFlg)
				|| Skf3022Sc006CommonDto.CREATE.equals(buttonFlg)
				|| Skf3022Sc006CommonDto.SHATAKU_LOGIN.equals(buttonFlg)
				&& Skf3022Sc006CommonDto.SIYOURYOFLG_HAVE.equals(siyouryoFlg)) {
			// 社宅使用料予約データを削除
//						Dim delCount As Integer = ta.DeleteShatakuYoyaku(CDec(teijiNo))
			Skf3022Sc006GetTeijiDataExpParameter param = new Skf3022Sc006GetTeijiDataExpParameter();
			param.setTeijiNo(Long.parseLong(teijiNo));
			skf3022Sc006DeleteShatakuYoyakuExpRepository.deleteShatakuYoyaku(param);
			LogUtils.debugByMsg("社宅使用料予約データ削除");
		}
		// 社宅台帳データの登録
//			If SHATAKU_LOGIN.Equals(buttonFlg) Then
		if (Skf3022Sc006CommonDto.SHATAKU_LOGIN.equals(buttonFlg)) {
			// 提示データ新規の場合、採番した提示番号を利用する
//				If newFlg Then
			if (newFlg) {
//					teijiNo = GetToString(columnInfoList.Item(0).Value)
				teijiNo = columnInfoList.getTeijiNo().toString();
			}
//		
//				Dim shatakuKanriId As Long = 0
			// 社宅管理台帳データの登録
//				Select Case Com_PageBusinessLogic.UpdateShatakuKanriDaichoShatakuData(teijiNo, shatakuKanriId, False, nyukyoYoteiDate, sysShoriNenGetsu, userId, ipAddress, systemDate)
//					Case Constant.ReturnStatus.OK
//					Case Else
//						Return 0
//				End Select
			Long shatakuKanriId = skfPageBusinessLogicUtils.updateShatakuKanriDaichoShatakuData(
					Long.parseLong(teijiNoOld), false, nyukyoYoteiDate, sysShoriNenGetsu, updateUser, updateProgramId, systemDate);
			if (Objects.equals(shatakuKanriId, null)) {
				LogUtils.debugByMsg("社宅管理台帳データ登録（社宅情報）更新失敗②");
				return 0;
			}
			// ●社宅利用料予約データの社宅管理台帳IDを更新
//				Using ta As New TB_T_SHATAKU_YOYAKU_DATATableAdapter
//					Try
//						'
			// 社宅利用料予約データの社宅管理台帳IDを更新
//						ta.UpdateShatakuYoyakuData(teijiNo, CStr(shatakuKanriId), userId, ipAddress, systemDate)
			Skf3022TShatakuYoyakuData yoyakuParam = new Skf3022TShatakuYoyakuData();
			yoyakuParam.setTeijiNo(Long.parseLong(teijiNo));
			yoyakuParam.setShatakuKanriId(shatakuKanriId);
			yoyakuParam.setUpdateUserId(updateUser);
			yoyakuParam.setUpdateProgramId(updateProgramId);
			yoyakuParam.setUpdateDate(systemDate);
			skf3022Sc006UpdateShatakuYoyakuDataExpRepository.updateShatakuYoyakuData(yoyakuParam);
			LogUtils.debugByMsg("社宅利用料予約データの社宅管理台帳IDを更新：updateShatakuYoyakuData");
			// 備品情報タブの「貸与日」が入力されている場合、「社宅基本台帳データ更新（備品）」バッチ処理を起動する
//				If Not String.IsNullOrEmpty(bihinTaiyobi) Then
			if (!CheckUtils.isEmpty(bihinTaiyobi)) {
				// 社宅管理台帳備品データの登録
//					Select Case Com_PageBusinessLogic.UpdateShatakuKanriDaichoBihinData(teijiNo, shatakuKanriId, sysShoriNenGetsu, userId, ipAddress, systemDate)
//						Case Constant.ReturnStatus.OK
//						Case Else
//							Return 0
//					End Select
				int ret = skfPageBusinessLogicUtils.updateShatakuKanriDaichoBihinData(
						Long.parseLong(teijiNo), shatakuKanriId, sysShoriNenGetsu, updateUser, updateProgramId, systemDate);
				if (ret != CodeConstant.SYS_OK) {
					LogUtils.debugByMsg("社宅管理台帳データ登録（社宅情報）更新失敗①");
					return 0;
				}
			}
//		
//				'提示データテーブルアダプター生成
//				Using ta As New TB_T_TEIJI_DATATableAdapter
//					Try
//						'
			// 提示データの社宅管理台帳IDを更新
//						Dim updCountTJ2 As Integer = ta.UpdateTeijiData(teijiNo, _
//																		systemDate, _
//																		updateUser, _
//																		updateIp)
			Skf3022TTeijiData tParam = new Skf3022TTeijiData();
			tParam.setTeijiNo(Long.parseLong(teijiNo));
			tParam.setUpdateUserId(updateUser);
			tParam.setUpdateProgramId(updateProgramId);
			tParam.setUpdateDate(systemDate);
			skf3022Sc006UpdateTeijiDataKanriNoExpRepository.updateTeijiDataKanriNo(tParam);
			LogUtils.debugByMsg("提示データの社宅管理台帳IDを更新：updateTeijiDataKanriNo");
		}
		// 使用料パターンテーブル登録・更新
//			If rentalPatternTorokuList(30).Equals("0") Then
		if (CodeConstant.STRING_ZERO.equals(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.UPDATE_KIND))) {
			// 登録処理
//				Try
//					Using ta As New TB_T_RENTAL_PATTERNTableAdapter()
//		
//						If String.IsNullOrEmpty(rentalPatternTorokuList(14)) Then
			if (!CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.SHATAKU_GETSUGAKU))) {
				// データの登録（使用料パターン未計算）
//							updCountRP = ta.InsertRentalPattern(CDec(rentalPatternTorokuList(0)), _
//																CDec(rentalPatternTorokuList(1)), _
//																Nothing, _
//																Nothing, _
//																Nothing, _
//																Nothing, _
//																Nothing, _
//																Nothing, _
//																Nothing, _
//																Nothing, _
//																Nothing, _
//																Nothing, _
//																rentalPatternTorokuList(19), _
//																rentalPatternTorokuList(21), _
//																rentalPatternTorokuList(22), _
//																rentalPatternTorokuList(24), _
//																rentalPatternTorokuList(25), _
//																Nothing, _
//																Nothing, _
//																Nothing, _
//																Nothing)
				Skf3030TRentalPattern patternRecord = new Skf3030TRentalPattern();
				// 社宅管理番号
				patternRecord.setShatakuKanriNo(Long.parseLong(
						rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.SHATAKUKANRI_NO)));
				// 使用料パターンID
				patternRecord.setRentalPatternId(Long.parseLong(
						rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.RENTAL_PATTERNID)));
				updCountRP = skf3030TRentalPatternRepository.insertSelective(patternRecord);
				LogUtils.debugByMsg("データの登録（使用料パターン未計算）");
			} else {
				// データの登録（使用料パターン計算済）
//							updCountRP = ta.InsertRentalPattern(CDec(rentalPatternTorokuList(0)), _
//																CDec(rentalPatternTorokuList(1)), _
//																rentalPatternTorokuList(2), _
//																rentalPatternTorokuList(3), _
//																CDec(rentalPatternTorokuList(5)), _
//																CDec(rentalPatternTorokuList(6)), _
//																CDec(rentalPatternTorokuList(7)), _
//																rentalPatternTorokuList(8), _
//																CDec(rentalPatternTorokuList(11)), _
//																CDec(rentalPatternTorokuList(12)), _
//																CDec(rentalPatternTorokuList(13)), _
//																CDec(rentalPatternTorokuList(14)), _
//																rentalPatternTorokuList(19), _
//																rentalPatternTorokuList(21), _
//																rentalPatternTorokuList(22), _
//																rentalPatternTorokuList(24), _
//																rentalPatternTorokuList(25), _
//																CDec(rentalPatternTorokuList(26)), _
//																CDec(rentalPatternTorokuList(27)), _
//																CDec(rentalPatternTorokuList(28)), _
//																CDec(rentalPatternTorokuList(29)))
				Skf3030TRentalPattern patternRecord = new Skf3030TRentalPattern();
				// 社宅管理番号
				if (!CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.SHATAKUKANRI_NO))) {
					patternRecord.setShatakuKanriNo(Long.parseLong(
							rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.SHATAKUKANRI_NO)));
				}
				// 使用料パターンID
				if (!CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.RENTAL_PATTERNID))) {
					patternRecord.setRentalPatternId(Long.parseLong(
							rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.RENTAL_PATTERNID)));
				}
				// 使用料パターン名
				patternRecord.setRentalPatternName(
						rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.PATTERN_NAME));
				// 規格
				patternRecord.setKikaku(
						rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.KIKAKU));
				// 基準使用料算定上延べ面積
				if (!CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.KIJUN_MENSEKI))) {
					patternRecord.setBaseCalcMenseki(new BigDecimal(
							rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.KIJUN_MENSEKI)));
				}
				// 社宅使用料算定上延べ面積
				if (!CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.SHATAKU_MENSEKI))) {
					patternRecord.setShatakuCalcMenseki(Short.parseShort(
							rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.SHATAKU_MENSEKI)));
				}
				// 経年残価率
				if (!CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.KEINEN_ZANKARITSU))) {
					patternRecord.setAgingResidualRate(new BigDecimal(
							rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.KEINEN_ZANKARITSU)));
				}
				// 用途
				patternRecord.setAuse(
						rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.YOTO));
				// 経年
				if (!CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.KEINEN))) {
					patternRecord.setAging(Short.parseShort(
							rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.KEINEN)));
				}
				// 基本使用料
				if (!CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.KIHON_SHIYORYO))) {
					patternRecord.setBaseRental(Integer.parseInt(
							rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.KIHON_SHIYORYO)));
				}
				// 単価
				if (!CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.TANKA))) {
					patternRecord.setPrice(new BigDecimal(
							rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.TANKA)));
				}
				// 社宅使用料月額
				if (!CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.SHATAKU_GETSUGAKU))) {
					patternRecord.setRental(Integer.parseInt(
							rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.SHATAKU_GETSUGAKU)));
				}
				// 延べ面積
				if (!CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.NOBE_MENSEKI))) {
					patternRecord.setMenseki(new BigDecimal(
							rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.NOBE_MENSEKI)));
				}
				// サンルーム面積
				if (!CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.SUNROOM_MENSEKI))) {
					patternRecord.setSunRoomMenseki(new BigDecimal(
							rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.SUNROOM_MENSEKI)));
				}
				// 階段面積
				if (!CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.KAIDAN_MENSEKI))) {
					patternRecord.setStairsMenseki(new BigDecimal(
							rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.KAIDAN_MENSEKI)));
				}
				// 物置面積
				if (!CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.MONOOKI_MENSEKI))) {
					patternRecord.setBarnMenseki(new BigDecimal(
							rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.MONOOKI_MENSEKI)));
				}
				updCountRP = skf3030TRentalPatternRepository.insertSelective(patternRecord);
				LogUtils.debugByMsg("データの登録（使用料パターン計算済）");
			}
		} else {
			// 更新処理
//				Using ta As New TB_T_RENTAL_PATTERNTableAdapter()
//		
			// 排他処理
//					Using targetDt As TB_T_RENTAL_PATTERNDataTable = _
//						ta.GetRentalPatternForUpdate(rentalPatternTorokuList(1), rentalPatternUpdateDate)
			Skf3022Sc006GetRentalPatternForUpdateExpParameter param = new Skf3022Sc006GetRentalPatternForUpdateExpParameter();
			param.setRentalPatternId(Long.parseLong(
					rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.RENTAL_PATTERNID)));
			param.setUpdateDate(rentalPatternUpdateDate);
			List<Skf3022Sc006GetRentalPatternForUpdateExp> targetDt = new ArrayList<Skf3022Sc006GetRentalPatternForUpdateExp>();
			targetDt = skf3022Sc006UpdateRentalPatternExpRepository.getRentalPatternForUpdate(param);
			// データが存在する場合
//						If 0 < targetDt.Rows.Count Then
			if (targetDt.size() > 0) {
//		
//							Try
//								If String.IsNullOrEmpty(rentalPatternTorokuList(14)) Then
				if (CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.SHATAKU_GETSUGAKU))) {
					// データの更新（使用料パターン未計算）
//									updCountRP = ta.UpdateRentalPattern(CDec(rentalPatternTorokuList(0)), _
//																		CDec(rentalPatternTorokuList(1)), _
//																		Nothing, _
//																		Nothing, _
//																		Nothing, _
//																		Nothing, _
//																		Nothing, _
//																		Nothing, _
//																		Nothing, _
//																		Nothing, _
//																		Nothing, _
//																		Nothing, _
//																		rentalPatternUpdateDateForRegist, _
//																		rentalPatternTorokuList(24), _
//																		rentalPatternTorokuList(25), _
//																		Nothing, _
//																		Nothing, _
//																		Nothing, _
//																		Nothing)
					Skf3030TRentalPattern record = new Skf3030TRentalPattern();
					// 社宅管理番号
					if (!CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.SHATAKUKANRI_NO))) {
						record.setShatakuKanriNo(Long.parseLong(
								rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.SHATAKUKANRI_NO)));
					}
					// 使用料パターンID
					if (!CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.RENTAL_PATTERNID))) {
						record.setRentalPatternId(Long.parseLong(
								rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.RENTAL_PATTERNID)));
					}
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
					record.setUpdateUserId(updateUser);
					// 更新機能ID
					record.setUpdateProgramId(updateProgramId);
					// 延べ面積
					record.setMenseki(null);
					// サンルーム面積
					record.setSunRoomMenseki(null);
					// 階段面積
					record.setStairsMenseki(null);
					// 物置面積
					record.setBarnMenseki(null);
					// 更新
					updCountRP = skf3022Sc006UpdateRentalPatternExpRepository.updateRentalPattern(record);
					LogUtils.debugByMsg("データの更新（使用料パターン未計算）");
				} else {
					// データの更新（使用料パターン計算済）
//									updCountRP = ta.UpdateRentalPattern(CDec(rentalPatternTorokuList(0)), _
//																		CDec(rentalPatternTorokuList(1)), _
//																		rentalPatternTorokuList(2), _
//																		rentalPatternTorokuList(3), _
//																		CDec(rentalPatternTorokuList(5)), _
//																		CDec(rentalPatternTorokuList(6)), _
//																		CDec(rentalPatternTorokuList(7)), _
//																		rentalPatternTorokuList(8), _
//																		CDec(rentalPatternTorokuList(11)), _
//																		CDec(rentalPatternTorokuList(12)), _
//																		CDec(rentalPatternTorokuList(13)), _
//																		CDec(rentalPatternTorokuList(14)), _
//																		rentalPatternUpdateDateForRegist, _
//																		rentalPatternTorokuList(24), _
//																		rentalPatternTorokuList(25), _
//																		CDec(rentalPatternTorokuList(26)), _
//																		CDec(rentalPatternTorokuList(27)), _
//																		CDec(rentalPatternTorokuList(28)), _
//																		CDec(rentalPatternTorokuList(29)))
					Skf3030TRentalPattern record = new Skf3030TRentalPattern();
					// 社宅管理番号
					if (!CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.SHATAKUKANRI_NO))) {
						record.setShatakuKanriNo(Long.parseLong(
								rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.SHATAKUKANRI_NO)));
					} else {
						record.setShatakuKanriNo(null);
					}
					// 使用料パターンID
					if (!CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.RENTAL_PATTERNID))) {
						record.setRentalPatternId(Long.parseLong(
								rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.RENTAL_PATTERNID)));
					} else {
						record.setRentalPatternId(null);
					}
					// 使用料パターン名
					record.setRentalPatternName(
							rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.PATTERN_NAME));
					// 規格
					record.setKikaku(
							rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.KIKAKU));
					// 基準使用料算定上延べ面積
					if (!CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.KIJUN_MENSEKI))) {
						record.setBaseCalcMenseki(new BigDecimal(
								rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.KIJUN_MENSEKI)));
					} else {
						record.setBaseCalcMenseki(null);
					}
					// 社宅使用料算定上延べ面積
					if (!CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.SHATAKU_MENSEKI))) {
						record.setShatakuCalcMenseki(Short.parseShort(
								rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.SHATAKU_MENSEKI)));
					} else {
						record.setShatakuCalcMenseki(null);
					}
					// 経年残価率
					if (!CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.KEINEN_ZANKARITSU))) {
						record.setAgingResidualRate(new BigDecimal(
								rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.KEINEN_ZANKARITSU)));
					} else {
						record.setAgingResidualRate(null);
					}
					// 用途
					record.setAuse(
							rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.YOTO));
					// 経年
					if (!CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.KEINEN))) {
						record.setAging(Short.parseShort(
								rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.KEINEN)));
					} else {
						record.setAging(null);
					}
					// 基本使用料
					if (!CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.KIHON_SHIYORYO))) {
						record.setBaseRental(Integer.parseInt(
								rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.KIHON_SHIYORYO)));
					} else {
						record.setBaseRental(null);
					}
					// 単価
					if (!CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.TANKA))) {
						record.setPrice(new BigDecimal(
								rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.TANKA)));
					} else {
						record.setPrice(null);
					}
					// 社宅使用料月額
					if (!CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.SHATAKU_GETSUGAKU))) {
						record.setRental(Integer.parseInt(
								rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.SHATAKU_GETSUGAKU)));
					} else {
						record.setRental(null);
					}
					// 更新日時
					record.setUpdateDate(rentalPatternUpdateDateForRegist);
					// 更新者ID
					record.setUpdateUserId(updateUser);
					// 更新機能ID
					record.setUpdateProgramId(updateProgramId);
					// 延べ面積
					if (!CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.NOBE_MENSEKI))) {
						record.setMenseki(new BigDecimal(
								rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.NOBE_MENSEKI)));
					} else {
						record.setMenseki(null);
					}
					// サンルーム面積
					if (!CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.SUNROOM_MENSEKI))) {
						record.setSunRoomMenseki(new BigDecimal(
								rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.SUNROOM_MENSEKI)));
					} else {
						record.setSunRoomMenseki(null);
					}
					// 階段面積
					if (!CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.KAIDAN_MENSEKI))) {
						record.setStairsMenseki(new BigDecimal(
								rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.KAIDAN_MENSEKI)));
					} else {
						record.setStairsMenseki(null);
					}
					// 物置面積
					if (!CheckUtils.isEmpty(rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.MONOOKI_MENSEKI))) {
						record.setBarnMenseki(new BigDecimal(
								rentalPatternTorokuList.get(Skf3022Sc006CommonDto.RENTAL_PATTERN.MONOOKI_MENSEKI)));
					} else {
						record.setBarnMenseki(null);
					}
					// 更新
					updCountRP = skf3022Sc006UpdateRentalPatternExpRepository.updateRentalPattern(record);
					LogUtils.debugByMsg("データの更新（使用料パターン計算済）");
				}
			} else {
				// 排他エラーメッセージ
				LogUtils.debugByMsg("使用料パターン排他エラー");
				return -1;
			}
		}
//			If DATA_COUNT_0 < updCountTJ AndAlso _
		if (updCountTJ <= 0 || !Objects.equals(bihinCd.size(), updCountTBD)) {
			LogUtils.debugByMsg("備品更新件数不一致エラー");
			return 0;
		}
//		Return updCountTJ + updCountNY + updCountTBD + updCountOldSR + updCountSR + updCountOldSPB1 + updCountSPB1 + updCountOldSPB2 + updCountSPB2 + updCountRP

		updCountMap.put(UPDATE_COUNTER.UPD_COUNT_TJ, updCountTJ);
		updCountMap.put(UPDATE_COUNTER.UPD_COUNT_NY, updCountNY);
		updCountMap.put(UPDATE_COUNTER.UPD_COUNT_TBD, updCountTBD);
		updCountMap.put(UPDATE_COUNTER.UPD_COUNT_OLD_SR, updCountOldSR);
		updCountMap.put(UPDATE_COUNTER.UPD_COUNT_SR, updCountSR);
		updCountMap.put(UPDATE_COUNTER.UPD_COUNT_OLD_SPB_1, updCountOldSPB1);
		updCountMap.put(UPDATE_COUNTER.UPD_COUNT_SPB_1, updCountSPB1);
		updCountMap.put(UPDATE_COUNTER.UPD_COUNT_OLD_SPB_2, updCountOldSPB2);
		updCountMap.put(UPDATE_COUNTER.UPD_COUNT_SPB_2, updCountSPB2);
		updCountMap.put(UPDATE_COUNTER.UPD_COUNT_RP, updCountRP);
		return (updCountTJ + updCountNY + updCountTBD + updCountOldSR + updCountSR + updCountOldSPB1 + updCountSPB1 + updCountOldSPB2 + updCountSPB2 + updCountRP);
	}

	/**
	 * 表示タブインデックス設定
	 * パラメータの指定タブインデックスが設定済みタブインデックスよりも小さい場合のみ、
	 * パラメータの指定タブインデックスを次回表示タブインデックスに設定する
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param nextIndex	次回表示タブインデックス
	 * @param registDto	*DTO
	 */
	private void setDisplayTabIndex(String nextIndex, Skf3022Sc006CommonDto comDto) {
		int targetTabIndex = 999;
		try {
			targetTabIndex = Integer.parseInt(comDto.getHdnTabIndex());
			// 設定タブインデックスが、次回表示タブインデックスよりも小さい場合、次回表示タブインデックスを更新する。
			if (targetTabIndex > Integer.parseInt(nextIndex)) {
				comDto.setHdnTabIndex(nextIndex);
			}
		} catch (NumberFormatException e) {}
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
	 * 数値の桁にカンマを付与
	 * null、空文字は「"0"」を返却する
	 * 
	 * @param str	数値文字列
	 * @return		数値文字列をカンマ区切りにした文字列
	 */
	public String getKanmaNumEdit(String str) {

		String changeString = str;
		DecimalFormat df1 = new DecimalFormat("#,##0");
		if (CheckUtils.isEmpty(changeString)) {
			changeString = CodeConstant.STRING_ZERO;
		}

		// 変換
		changeString = df1.format(new BigDecimal(changeString.trim()));
		return changeString;
	}

	/**
	 * 金額項目にカンマと単位を付与
	 * null、空文字は「"0円"」を返却する
	 * 
	 * @param str	金額文字列
	 * @return		金額文字列をカンマ区切りにし、「"円"」を付与した文字列
	 */
	private String getKingakuEdit(String str) {

		String changeString = str;
		DecimalFormat df1 = new DecimalFormat("#,##0");
		if (CheckUtils.isEmpty(changeString)) {
			changeString = CodeConstant.STRING_ZERO;
		}

		// 変換
		changeString = df1.format(new BigDecimal(changeString.trim())) + " " + SkfCommonConstant.FORMAT_EN;
		return changeString;
	}

	/**
	 * 金額項目より単位と","を削除した文字列を取得
	 * null、空文字は「"0"」を返却する
	 * 
	 * @param str	金額文字列
	 * @return		金額文字列の数値のみ
	 */
	public String getKingakuText(String str) {

		String changeString = str;
		if (CheckUtils.isEmpty(changeString)) {
			return CodeConstant.STRING_ZERO;
		}
		// 変換
		changeString = changeString.replace(",", "").replace(SkfCommonConstant.FORMAT_EN, "").trim();
		return changeString;
	}

	/**
	 * 日付項目より"/"を削除した文字列を取得
	 * null、空文字はnullを返却する
	 * 
	 * @param str	日付文字列
	 * @return		日付文字列の数値のみ
	 */
	public String getDateText(String str) {

		String changeString = str;
		if (CheckUtils.isEmpty(changeString)) {
			return null;
		}
		// 変換
		changeString = changeString.replace("/", "").trim();
		return changeString;
	}

	/**
	 * プルダウンの中身作成。<br>
	 * 
	 * パラメータの選択値リストから選択プルダウンの中身を作成する。
	 * パラメータの選択値指定がない場合は未指定で作成する。
	 * 
	 * @param selectedValue	選択値
	 * @param slectValueList	選択値リスト
	 * @return	プルダウンの中身
	 */
	private String createStatusSelect(
			String selectedValue, List<Map<String, Object>> slectValueList){

		String returnListCode = "";

		for(Map<String, Object> obj : slectValueList){
			String value = obj.get("value").toString();
			String label = obj.get("label").toString();
			if (Objects.equals(value, selectedValue)) {
				// ステータスとリスト値が一致する場合、選択中にする
				returnListCode += "<option value='" + value + "' selected>" + label + "</option>";
			} else {
				returnListCode += "<option value='" + value + "'>" + label + "</option>";	
			}
		}
		return returnListCode;
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
	 * 日付加算
	 * 
	 * @param targetDate
	 * @param addDay
	 * @return
	 */
	public Date addDay(String targetDate, int addDay) {
		// 日付フォーマット
		SimpleDateFormat dateFormat = new SimpleDateFormat(Skf3022Sc006CommonDto.DATE_FORMAT);
		Date afterDate = null;
		try {
			afterDate = dateFormat.parse(getDateText(targetDate));
		} catch (ParseException e) {
			LogUtils.debugByMsg("日付変換失敗：" + e.getMessage());
			return afterDate;
		}
		// Date型の日時をCalendar型に変換
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(afterDate);
		calendar.add(Calendar.DATE, addDay);
		afterDate = calendar.getTime();
		return afterDate;
	}
}
