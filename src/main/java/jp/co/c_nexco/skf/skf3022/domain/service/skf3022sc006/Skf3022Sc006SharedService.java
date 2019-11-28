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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetNyutaikyoYoteiDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetNyutaikyoYoteiDataExpParameter;
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
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetShatakuRoomExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetShatakuRoomExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetShatakuYoyakuDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetTeijiBihinDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetTeijiBihinDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetTeijiDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetTeijiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBaseBusinessLogicUtils.SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBaseBusinessLogicUtils.SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetCompanyAgencyListExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetNyutaikyoYoteiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetRentalPatternInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetTeijiBihinDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetSameAppNoCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetSameParkingBlockTeijiInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetSameRoomTeijiInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetShatakuDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetShatakuParkingBlockExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetShatakuRoomExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetShatakuYoyakuDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetTeijiDataExpRepository;
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
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc002common.Skf3010Sc002CommonDto;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022Sc006common.Skf3022Sc006CommonAsyncDto;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022Sc006common.Skf3022Sc006CommonDto;

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
		if (yearMonth.equals(startYearMonth) && yearMonth.equals(endYearMonth)) {
			LogUtils.debugByMsg("処理年月と計算開始年月が同年同月、且つ、計算開始年月と計算終了年月が同一");
			nisu = endDay - startDay + 1;
		} else if (yearMonth.equals(startYearMonth) && !yearMonth.equals(endYearMonth)) {
			LogUtils.debugByMsg("処理年月と計算開始年月が同年同月、且つ、計算開始年月と計算終了年月が異なる");
			nisu = dayOfMonthStart - startDay + 1;
		} else if (!yearMonth.equals(startYearMonth) && yearMonth.equals(endYearMonth)) {
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
			Map<String, String> paramMap, Map<String, String> resultMap, StringBuffer errMsg) throws Exception {

		// 戻り値
		Map<String, String> tmpMap = new HashMap<String, String>();

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
		outputEntity = skfBaseBusinessLogicUtils.getShatakuShiyouryouKeisan(inputEntity);
		// 正常に計算できていたら、値をセット
		// 計算結果判定
		if (CheckUtils.isEmpty(outputEntity.getErrMessage())) {
			// 社宅使用料月額
			shatakuRiyoryou = outputEntity.getShatakuShiyouryouGetsugaku().toPlainString();
			// 駐車場使用料
			chushajoRiyoryou = outputEntity.getChushajouShiyoryou().toPlainString();
		} else {
			errMsg.append(MessageFormat.format(
					PropertyUtils.getValue(MessageIdConstant.SKF3020_ERR_MSG_COMMON),
					outputEntity.getErrMessage()));
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
							hiwariKeisan(getDateText(paramMap.get("c006RiyouStartDayOne")), "", chushajoRiyoryou));

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
			if (comDto.getSc006YakuinSanteiSelect() == null) {
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
			// 社宅提示ステータスの文字色を設定する
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
				// I_SKF_3087メッセージ設定
				setMsgBox(PropertyUtils.getValue(MessageIdConstant.I_SKF_3087), comDto);
			// 申請ありの場合
			} else {
				LogUtils.debugByMsg("変更、申請あり");
				// I_SKF_3088メッセージ設定
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
				if (entity.getCompanyCd().equals(selectedValue)) {
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

		// 社宅提示状況区分が"同意済み"、備品貸与区分が"必要"の場合
		if (CodeConstant.PRESENTATION_SITUATION_DOI_SUMI.equals(comDto.getHdnShatakuTeijiStatus())
				&& CodeConstant.BIHIN_TAIYO_KBN_HITSUYO.equals(comDto.getHdnBihinTaiyoKbn())) {
			// 個人負担共益費調整額にチェックがある場合
			if (comDto.getSc006KyoekihiKyogichuCheck()) {
				// 初期のタブを"社宅情報タブ"に設定
				comDto.setHdnTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_SHATAKU);
			} else if(CodeConstant.BIHIN_STATUS_MI_SAKUSEI.equals(comDto.getHdnBihinTeijiStatus())) {
				// 備品提示状況区分が未申請の場合
				// 初期のタブを"社宅情報タブ"に設定
				comDto.setHdnTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_SHATAKU);
			} else {
				// 初期のタブを"備品タブ"に設定
				comDto.setHdnTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_BIHIN);
			}
		}
		// 社宅提示状況区分が"承認"、備品貸与区分が"必要"の場合
		if (CodeConstant.PRESENTATION_SITUATION_SHONIN.equals(comDto.getHdnShatakuTeijiStatus())
				&& CodeConstant.BIHIN_TAIYO_KBN_HITSUYO.equals(comDto.getHdnBihinTaiyoKbn())) {
			// 初期のタブを"備品タブ"に設定
			comDto.setHdnTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_BIHIN);
		}
		// 入退居区分が"退居"、備品貸与区分が"必要"、社宅提示状況区分が"作成済／同意済／承認"の場合
		if (CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(comDto.getHdnNyutaikyoKbn())
				&& CodeConstant.BIHIN_TAIYO_KBN_HITSUYO.equals(comDto.getHdnBihinTaiyoKbn())
				&& (CodeConstant.PRESENTATION_SITUATION_SAKUSEI_SUMI.equals(comDto.getHdnShatakuTeijiStatus())
					|| CodeConstant.PRESENTATION_SITUATION_DOI_SUMI.equals(comDto.getHdnShatakuTeijiStatus())
					|| CodeConstant.PRESENTATION_SITUATION_SHONIN.equals(comDto.getHdnShatakuTeijiStatus()))) {
			// 初期のタブを"備品タブ"に設定
			comDto.setHdnTabIndex(Skf3022Sc006CommonDto.SELECT_TAB_INDEX_BIHIN);
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
							comDto.setLitMessageCreate(PropertyUtils.getValue(MessageIdConstant.I_SKF_3052));
							break;
						// 同意済
						case CodeConstant.PRESENTATION_SITUATION_DOI_SUMI:
							// 備品提示ステータス判定
							if (comDto.getHdnBihinTeijiStatus() != null) {
								switch (comDto.getHdnBihinTeijiStatus()) {
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
									comDto.setLitMessageCreate(PropertyUtils.getValue(MessageIdConstant.I_SKF_3052));
									break;
								default :
									LogUtils.debugByMsg("申請あり、入退居区分：入居、申請区分：駐車場のみ以外、社宅提示：同意済、備品提示：(貸与不要/未申請/作成中/作成済/搬入待/搬入済)以外");
									break;
								};
							} else {
								LogUtils.debugByMsg("申請あり、入退居区分：入居、申請区分：駐車場のみ以外、社宅提示：同意済、備品提示：null");
							}
						// 承認
						case CodeConstant.PRESENTATION_SITUATION_SHONIN:
							// 備品提示ステータス判定
							if (comDto.getHdnBihinTeijiStatus() != null) {
								switch (comDto.getHdnBihinTeijiStatus()) {
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
							} else {
								LogUtils.debugByMsg("申請あり、入退居区分：入居、申請区分：駐車場のみ以外、社宅提示：承認、備品提示：null");
							}
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
						if (comDto.getHdnBihinTeijiStatus() != null) {
							switch (comDto.getHdnBihinTeijiStatus()) {
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
						} else {
							LogUtils.debugByMsg("申請あり、入退居区分：退居、社宅提示：承認/同意済/作成済、備品提示：null");
						}
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
			if (CodeConstant.BIHIN_STATUS_DOI_SUMI.equals(comDto.getHdnBihinTeijiStatus())) {
				// 同意済
				// 返却日
				comDto.setSc006HenkyakuDayDisableFlg(true);
				// 備品情報一覧
				List<Map<String, Object>> bihinList = comDto.getBihinInfoListTableData();
				for (int i = 0; i < bihinList.size(); i++) {
					Map <String, Object> bihinMap = bihinList.get(i);
					// 備品貸与ステータス
					if (bihinMap.get("bihinTaiyoStts") != null 
							&& !CheckUtils.isEmpty(bihinMap.get("bihinTaiyoStts").toString())) {
						String bihinTaiyoSttsCode = createStatusSelect(bihinMap.get("bihinTaiyoSttsOldKbn").toString(), lendStatusList);
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
				if (CheckUtils.isEmpty(comDto.getHdnBihinTeijiStatus())
						|| CodeConstant.BIHIN_STATUS_MI_SAKUSEI.equals(comDto.getHdnBihinTeijiStatus())) {
					// 未設定、または、未作成
					// 返却日
					comDto.setSc006HenkyakuDayDisableFlg(false);
					// 備品情報一覧
					List<Map<String, Object>> bihinList = comDto.getBihinInfoListTableData();
					for (int i = 0; i < bihinList.size(); i++) {
						Map <String, Object> bihinMap = bihinList.get(i);
						// 備品貸与ステータス(退居用)
						if (bihinMap.get("bihinTaiyoStts") != null 
								&& !CheckUtils.isEmpty(bihinMap.get("bihinTaiyoStts").toString())) {
							String bihinTaiyoSttsCode = createStatusSelect(bihinMap.get("bihinTaiyoSttsOldKbn").toString(), lendStatusList);
							// 活性
							bihinMap.put("bihinTaiyoStts", "<select id='bihinTaiyoStts" + i 
									+ "' name='bihinTaiyoStts" + i + "' style='width:90px;' >"
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

		// 次月予約フラグを設定する
		List<Skf3022Sc006GetShatakuYoyakuDataExp> yoyakuList = new ArrayList<Skf3022Sc006GetShatakuYoyakuDataExp>();
		Skf3022Sc006GetTeijiDataExpParameter param = new Skf3022Sc006GetTeijiDataExpParameter();
		Long teijiNo = CheckUtils.isEmpty(comDto.getHdnTeijiNo()) ? null : Long.parseLong(comDto.getHdnTeijiNo());
		param.setTeijiNo(teijiNo);
		if (teijiNo != null) {
			yoyakuList = skf3022Sc006GetShatakuYoyakuDataExpRepository.getShatakuYoyakuData(param);
		}
		comDto.setHdnYoyakuFlg(yoyakuList.get(0).getYoyakuflg());
		/** 確認ダイアログメッセージ設定 */
		if (!yoyakuList.get(0).getYoyakuflg() || !haveShiyoryoChanged(comDto)) {
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
		comDto.setLitMessageBack(PropertyUtils.getValue(MessageIdConstant.I_SKF_1009));
		// 提示備品一覧
		List<Skf3022Sc006GetTeijiBihinDataExp> roomBihinList = new ArrayList<Skf3022Sc006GetTeijiBihinDataExp>();
		// 備品一覧取得判定
		if (comDto.getBihinItiranFlg()) {
			// 提示備品データを取得
			roomBihinList = getBihinData(comDto.getHdnShatakuKanriNoOld(), comDto.getHdnRoomKanriNoOld(),
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
			if (!roomBihinMap.getShijisho().equals(roomBihinMap.getShijishoOld())) {
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
	private void reverseBihinList(List<Map<String, Object>> bihinList, List<Skf3022Sc006GetTeijiBihinDataExp> roomBihinList) {

		List<Skf3022Sc006GetTeijiBihinDataExp> resultList = new ArrayList<Skf3022Sc006GetTeijiBihinDataExp>();
		SimpleDateFormat dateFormat = new SimpleDateFormat(Skf3022Sc006CommonDto.DATE_FORMAT);
		for (Map<String, Object> tmpBihin : bihinList) {
			Skf3022Sc006GetTeijiBihinDataExp tmpMap = new Skf3022Sc006GetTeijiBihinDataExp();
			tmpMap.setBihinCd(tmpBihin.get("bihinCd").toString());
			tmpMap.setBihinName(tmpBihin.get("bihinName").toString());
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
			resultList.add(tmpMap);
		}
		roomBihinList.clear();
		roomBihinList.addAll(resultList);
	}


	/**
	 * 提示備品データ取得
	 * 
	 * @param shatakuKanriNo		社宅管理番号
	 * @param shatakuRoomKanriNo	部屋管理番号
	 * @param nyutaikyoKbn			入退居区分
	 * @param hdnTeijiNo			提示番号
	 * @param hdnShatakuHeyaFlg		社宅部屋変更フラグ
	 * @return	提示備品データリスト
	 */
	private List<Skf3022Sc006GetTeijiBihinDataExp> getBihinData(String shatakuKanriNo, String shatakuRoomKanriNo,
					String nyutaikyoKbn, String hdnTeijiNo, String hdnShatakuHeyaFlg) {

		List<Skf3022Sc006GetTeijiBihinDataExp> roomBihinList = new ArrayList<Skf3022Sc006GetTeijiBihinDataExp>();
		Skf3022Sc006GetTeijiBihinDataExpParameter param = new Skf3022Sc006GetTeijiBihinDataExpParameter();
		Long shainHouseKanriNo = CheckUtils.isEmpty(shatakuKanriNo) ? null : Long.parseLong(shatakuKanriNo);
		Long roomKanriNo = CheckUtils.isEmpty(shatakuRoomKanriNo) ? null : Long.parseLong(shatakuRoomKanriNo);
		Long teijiNo = CheckUtils.isEmpty(hdnTeijiNo) ? null : Long.parseLong(hdnTeijiNo);
		param.setShatakuKanriNo(shainHouseKanriNo);
		param.setShatakuRoomKanriNo(roomKanriNo);
		param.setTeijiNo(teijiNo);
		param.setNyutaikyoKbn(nyutaikyoKbn);
		if (shainHouseKanriNo != null && roomKanriNo != null && teijiNo != null) {
			// 提示備品データを取得
			if (Skf3022Sc006CommonDto.SHATAKU_HEYA_FLG_YES.equals(hdnShatakuHeyaFlg)) {
				roomBihinList = skf3022Sc006GetTeijiBihinDataExpRepository.getRoomBihinData(param);
			} else if (!CheckUtils.isEmpty(shatakuKanriNo)) {
				roomBihinList = skf3022Sc006GetTeijiBihinDataExpRepository.getTeijiBihinData(param);
			}
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
	private Boolean haveShiyoryoChanged(Skf3022Sc006CommonDto comDto) {

		// 画面の入退居区分が”入居”の場合
		if (codeCacheUtils.getGenericCodeName(
				FunctionIdConstant.GENERIC_CODE_KIKAKU_KBN,
				CodeConstant.NYUTAIKYO_KBN_NYUKYO).equals(comDto.getSc006NyutaikyoKbn())) {
			// 社宅管理番号
			if (!comDto.getHdnShatakuKanriNoOld().equals(comDto.getHdnShatakuKanriNo())) {
				return true;
			}
			// 部屋管理番号
			if (!comDto.getHdnRoomKanriNoOld().equals(comDto.getHdnRoomKanriNo())) {
				return true;
			}
			// 使用料パターン
			if (!comDto.getHdnSiyouryoIdOld().equals(comDto.getHdnSiyouryoId())) {
				return true;
			}
			// 入居予定日
			if (!getDateText(comDto.getSc006NyukyoYoteiDay()).equals(getDateText(comDto.getHdnNyukyoDate()))) {
				return true;
			}
			// 役員算定
			if (!comDto.getSc006YakuinSanteiSelect().equals(comDto.getHdnYakuin())) {
				return true;
			}
			// 個人負担共益費月額
			if (!comDto.getSc006KyoekihiMonthPay().equals(comDto.getHdnKojinFutan())) {
				return true;
			}
			// 駐車場管理番号１
			if (!comDto.getHdnChushajoNoOneOld().equals(comDto.getHdnChushajoNoOne())) {
				return true;
			}
			// 利用開始日１
			if (!getDateText(comDto.getSc006RiyouStartDayOne()).equals(getDateText(comDto.getHdnRiyouStartDayOne()))) {
				return true;
			}
			// 駐車場管理番号２
			if (!comDto.getHdnChushajoNoTwoOld().equals(comDto.getHdnChushajoNoTwo())) {
				return true;
			}
			// 利用開始日２
			if (!getDateText(comDto.getSc006RiyouStartDayTwo()).equals(getDateText(comDto.getHdnRiyouStartDayTwo()))) {
				return true;
			}
		// 画面の入退居区分が”退居”の場合
		} else if (codeCacheUtils.getGenericCodeName(
				FunctionIdConstant.GENERIC_CODE_KIKAKU_KBN,
				CodeConstant.NYUTAIKYO_KBN_TAIKYO).equals(comDto.getSc006NyutaikyoKbn())) {
			// 社宅管理番号
			if (!comDto.getHdnShatakuKanriNoOld().equals(comDto.getHdnShatakuKanriNo())) {
				return true;
			}
			// 部屋管理番号
			if (!comDto.getHdnRoomKanriNoOld().equals(comDto.getHdnRoomKanriNo())) {
				return true;
			}
			// 退居予定日
			if (!getDateText(comDto.getSc006TaikyoYoteiDay()).equals(getDateText(comDto.getHdnTaikyoDate()))) {
				return true;
			}
			// 利用終了日１
			if (!getDateText(comDto.getSc006RiyouEndDayOne()).equals(getDateText(comDto.getHdnRiyouEndDayOne()))) {
				return true;
			}
			// 利用終了日２
			if (!getDateText(comDto.getSc006RiyouEndDayTwo()).equals(getDateText(comDto.getHdnRiyouEndDayTwo()))) {
				return true;
			}
		// 画面の入退居区分が”変更”の場合
		} else {
			// 個人負担共益費月額
			if (!comDto.getSc006KyoekihiMonthPay().equals(comDto.getHdnKojinFutan())) {
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
						+ "' name='bihinTaiyoStts" + i + "' style='width:90px;' >"
												+ bihinTaiyoSttsCode + "</select>");
			}
			// 備品貸与状態選択値元
			tmpMap.put("bihinTaiyoSttsOldKbn", bihinMap.getBihinTaiyoStts());
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
		comDto.setHdnKeizokuBtnFlg(CodeConstant.STRING_ZERO);
		// 次月予約存在フラグ
		comDto.setHdnYoyakuFlg(teijiData.getYoyakuflg());
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
			/* US imart移植 kami 駐車場のみの場合、現状取得出来ていない。取得できないのが正解なのか、取得可能であるべきなのか調査が必要 */
			// comDto.setHdnRentalPatternUpdateDate(dateFormat.format(rentalPatternInfoList.get(0).getUpdateDate()));
			if (rentalPatternInfoList.size() > 0) {
				comDto.setHdnRentalPatternUpdateDate(dateFormat.format(rentalPatternInfoList.get(0).getUpdateDate()));
			} else {
				// 使用料パターンが存在しない為、更新日時を空文字に設定
				comDto.setHdnRentalPatternUpdateDate("");
			}
			/* UE imart移植 kami 駐車場のみの場合、現状取得出来ていない。取得できないのが正解なのか、取得可能であるべきなのか調査が必要 */
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
			if (rentalPatternInfoList.size() < 1) {
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
	private String setBihinTeijiStatusCss(String bihinTeijiStatusKbn) {

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
	 * @param asyncDto	*DTO
	 * @return			パラメータマップ
	 */
	public void setSiyoryoKeiSanParamAsync(Map<String, String> resultMap, Skf3022Sc006CommonAsyncDto asyncDto) {

		if (resultMap.containsKey("sc006TyusyaMonthPayAfter")) {
			asyncDto.setSc006TyusyaMonthPayAfter(resultMap.get("sc006TyusyaMonthPayAfter"));
		} else {
			asyncDto.setSc006TyusyaMonthPayAfter(null);
		}
		if (resultMap.containsKey("sc006SiyoryoHiwariPay")) {
			asyncDto.setSc006SiyoryoHiwariPay(resultMap.get("sc006SiyoryoHiwariPay"));
		} else {
			asyncDto.setSc006SiyoryoHiwariPay(null);
		}
		if (resultMap.containsKey("sc006SyatauMonthPayAfter")) {
			asyncDto.setSc006SyatauMonthPayAfter(resultMap.get("sc006SyatauMonthPayAfter"));
		} else {
			asyncDto.setSc006SyatauMonthPayAfter(null);
		}
		if (resultMap.containsKey("sc006ShiyoryoTsukigaku")) {
			asyncDto.setSc006ShiyoryoTsukigaku(resultMap.get("sc006ShiyoryoTsukigaku"));
		} else {
			asyncDto.setSc006ShiyoryoTsukigaku(null);
		}
		if (resultMap.containsKey("sc006TyusyaDayPayOne")) {
			asyncDto.setSc006TyusyaDayPayOne(resultMap.get("sc006TyusyaDayPayOne"));
		} else {
			asyncDto.setSc006TyusyaDayPayOne(null);
		}
		if (resultMap.containsKey("sc006TyusyaMonthPayOne")) {
			asyncDto.setSc006TyusyaMonthPayOne(resultMap.get("sc006TyusyaMonthPayOne"));
		} else {
			asyncDto.setSc006TyusyaMonthPayOne(null);
		}
		if (resultMap.containsKey("sc006TyusyaDayPayTwo")) {
			asyncDto.setSc006TyusyaDayPayTwo(resultMap.get("sc006TyusyaDayPayTwo"));
		} else {
			asyncDto.setSc006TyusyaDayPayTwo(null);
		}
		if (resultMap.containsKey("sc006TyusyaMonthPayTwo")) {
			asyncDto.setSc006TyusyaMonthPayTwo(resultMap.get("sc006TyusyaMonthPayTwo"));
		} else {
			asyncDto.setSc006TyusyaMonthPayTwo(null);
		}
	}

	/**
	 * 数値の桁にカンマを付与
	 * null、空文字は「"0"」を返却する
	 * 
	 * @param str	数値文字列
	 * @return		数値文字列をカンマ区切りにした文字列
	 */
	private String getKanmaNumEdit(String str) {

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
	private String getKingakuText(String str) {

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
	 * @param str	金額文字列
	 * @return		金額文字列の数値のみ
	 */
	private String getDateText(String str) {

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
			if (value.equals(selectedValue)) {
				// ステータスとリスト値が一致する場合、選択中にする
				returnListCode += "<option value='" + value + "' selected>" + label + "</option>";
			} else {
				returnListCode += "<option value='" + value + "'>" + label + "</option>";	
			}
		}
		return returnListCode;
	}

	/**
	 * パラメータ文字列のエスケープ処理
	 * @param param
	 * @return
	 */
	private String escapeParameter(String param){
		
		String resultStr=CodeConstant.DOUBLE_QUOTATION;
		
		// 文字エスケープ(% _ ' \)
		if (param != null) {
			// 「\」を「\\」に置換
			resultStr = param.replace("\\", "\\\\");
			// 「%」を「\%」に置換、「_」を「\_」に置換、「'」を「''」に置換
			resultStr = resultStr.replace("%", "\\%").replace("_", "\\_").replace("'", "''");
		}
		
		return resultStr;
	}

	/**
	 * 数値項目を小数第2位までの文字列に変換
	 * 小数第3位は四捨五入
	 * nullは「"0.00"」を返却する
	 * 
	 * @param bigDecimal	数値
	 * @return				小数第2位までの文字列
	 */
	private String getFloatEdit(BigDecimal bigDecimal) {

		String changeString = null;
		DecimalFormat df1 = new DecimalFormat("#,##0.00");
		if (bigDecimal == null) {
			return "0.00";
		}
		// 変換
		changeString = df1.format(bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP));
		
		return changeString;
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
	 * パラメータのJSON文字列配列をリスト形式に変換して返却する
	 * 
	 * @param jsonStr	JSON文字列配列
	 * @return			List<Map<String, Object>>
	 */
	public List<Map<String, Object>> jsonArrayToArrayList (String jsonStr) {

		// 返却用リスト
		List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		// JSON文字列判定
		if (jsonStr == null || jsonStr.length() <= 0) {
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
	 * エラー時可変ラベル値復元
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
	}
}
