/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc003;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc003.Skf3022Sc003GetNyukyoInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc003.Skf3022Sc003GetNyukyoInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc003.Skf3022Sc003GetShatakuInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc003.Skf3022Sc003GetShatakuInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc003.Skf3022Sc003GetShatakuRoomInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc003.Skf3022Sc003GetShatakuRoomInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc003.Skf3022Sc003GetShatakuShainInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc003.Skf3022Sc003GetShatakuShainInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBaseBusinessLogicUtils.SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBaseBusinessLogicUtils.SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc003.Skf3022Sc003GetNyukyoInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc003.Skf3022Sc003GetShatakuInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc003.Skf3022Sc003GetShatakuRoomInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc003.Skf3022Sc003GetShatakuShainInfoExpRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
//import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022Sc003common.Skf3022Sc003CommonDto;
import jp.co.intra_mart.common.platform.log.Logger;

/**
 * Skf3022Sc003SharedService 使用料入力支援共通処理クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3022Sc003SharedService {

	@Autowired
	private Skf3022Sc003GetShatakuInfoExpRepository skf3022Sc003GetShatakuInfoExpRepository;
	@Autowired
	private Skf3022Sc003GetShatakuRoomInfoExpRepository skf3022Sc003GetShatakuRoomInfoExpRepository;
	@Autowired
	private Skf3022Sc003GetShatakuShainInfoExpRepository skf3022Sc003GetShatakuShainInfoExpRepository;
	@Autowired
	private Skf3022Sc003GetNyukyoInfoExpRepository skf3022Sc003GetNyukyoInfoExpRepository;
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	/** ロガー。 */
	private static Logger logger = LogUtils.getLogger(SkfFileOutputUtils.class);
	@Value("${skf.common.settings.shiyouryou_keisan_kbn}")
	private String shiyouryouKbn;

	/**
	 * 入居希望等調書・入居決定通知情報取得
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param companyCd			会社コード
	 * @param applNo			書類管理番号
	 * @param nyukyoInfoList	*入居情報リスト
	 */
	public void getNyuukyoInfo(String companyCd, String applNo, List<Skf3022Sc003GetNyukyoInfoExp> nyukyoInfoList) {

		List<Skf3022Sc003GetNyukyoInfoExp> resultList = new ArrayList<Skf3022Sc003GetNyukyoInfoExp>();
		Skf3022Sc003GetNyukyoInfoExpParameter param = new Skf3022Sc003GetNyukyoInfoExpParameter();
		// パラメータ設定
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		// 入居情報取得
		resultList = skf3022Sc003GetNyukyoInfoExpRepository.getNyukyoInfo(param);
		// 件数確認
		if (resultList.size() > 0) {
			nyukyoInfoList.clear();
			nyukyoInfoList.addAll(resultList);
		}
	}

	/**
	 * 社宅情報取得
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param shatakuKanriNo	社宅管理番号
	 * @param shatakuInfoList	*社宅情報リスト
	 */
	public void getShatakuInfo(String shatakuKanriNo, List<Skf3022Sc003GetShatakuInfoExp> shatakuInfoList) {

		List<Skf3022Sc003GetShatakuInfoExp> resultList = new ArrayList<Skf3022Sc003GetShatakuInfoExp>();
		Skf3022Sc003GetShatakuInfoExpParameter param = new Skf3022Sc003GetShatakuInfoExpParameter();
		// パラメータ設定
		param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
		// 社宅情報取得
		resultList = skf3022Sc003GetShatakuInfoExpRepository.getShatakuInfo(param);
		// 件数確認
		if (resultList.size() > 0) {
			shatakuInfoList.clear();
			shatakuInfoList.addAll(resultList);
		}
	}

	/**
	 * 社宅部屋情報取得
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param shatakuKanriNo		社宅管理番号
	 * @param shatakuRoomKanriNo	社宅部屋管理番号
	 * @param shatakuRoomInfoList	*社宅部屋情報リスト
	 */
	public void getShatakuRoomInfo(String shatakuKanriNo, String shatakuRoomKanriNo, List<Skf3022Sc003GetShatakuRoomInfoExp> shatakuRoomInfoList) {

		List<Skf3022Sc003GetShatakuRoomInfoExp> resultList = new ArrayList<Skf3022Sc003GetShatakuRoomInfoExp>();
		// パラメータ判定
		if (!CheckUtils.isEmpty(shatakuRoomKanriNo)) {
			Skf3022Sc003GetShatakuRoomInfoExpParameter param = new Skf3022Sc003GetShatakuRoomInfoExpParameter();
			// パラメータ設定
			param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
			param.setShatakuRoomKanriNo(Long.parseLong(shatakuRoomKanriNo));
			// 社宅部屋情報取得
			resultList = skf3022Sc003GetShatakuRoomInfoExpRepository.getShatakuRoomInfo(param);
			// 件数確認
			if (resultList.size() > 0) {
				shatakuRoomInfoList.clear();
				shatakuRoomInfoList.addAll(resultList);
			}
		}
	}

	/**
	 * 社宅社員情報取得
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param companyCd		会社コード
	 * @param shainNo		社員番号
	 * @param shainInfoList	*社宅部屋情報リスト
	 */
	public void getShatakuShainInfo(String companyCd, String shainNo, List<Skf3022Sc003GetShatakuShainInfoExp> shainInfoList) {

		List<Skf3022Sc003GetShatakuShainInfoExp> resultList = new ArrayList<Skf3022Sc003GetShatakuShainInfoExp>();
		Skf3022Sc003GetShatakuShainInfoExpParameter param = new Skf3022Sc003GetShatakuShainInfoExpParameter();
		// パラメータ設定
		param.setCompanyCd(companyCd);
		param.setShainNo(shainNo);
		// 社宅社員情報取得
		resultList = skf3022Sc003GetShatakuShainInfoExpRepository.getShatakuShainInfo(param);
		// 件数確認
		if (resultList.size() > 0) {
			shainInfoList.clear();
			shainInfoList.addAll(resultList);
		}
	}

	/**
	 * 再計算処理
	 * 
	 * 「※」項目はアドレスとして戻り値になる。 
	 * 
	 * @param initDto	*DTO
	 * @return
	 * @throws ParseException
	 */
	public Boolean saiKeisan(Map<String, String> paramMap) throws ParseException {

		// 社宅利用料計算結果
		SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp calcResult = null;

//		// 用途判定
//		if (CheckUtils.isEmpty(initDto.getSc003YoutoSelect())) {
//			logger.debug("「用途」が選択されていないため、再計算を行わない");
//			return false;
//		}
//		// 基準使用料算定上延べ面積設定
//		if (!setKijunMenseki2(initDto)) {
//			return false;
//		}
//		// 社宅使用料算定上延べ面積を算出し設定
//		if (!setShatakuMenseki2(initDto)) {
//			return false;
//		}
//		// 使用料計算
//		calcResult = getShatakuShiyouryouKeisanJouhou2(initDto);
//		// 計算結果判定
//		if (calcResult != null) {
//			// ⑧基準単価2設定
//			initDto.setSc003KijunTanka2(getKingakuEdit(calcResult.getKijunShiyouryou().stripTrailingZeros().toPlainString()));
//			// ⑫使用料月額2設定
//			initDto.setSc003PatternShiyoryo2(
//					getKingakuEdit(calcResult.getPatternShiyoryouGetsugaku().stripTrailingZeros().toPlainString()));
//			// ⑬年齢加算係数
//			initDto.setSc003NenreikasanKeisu(getFloatEdit(calcResult.getNenreiKasanKeisu()));
//			// 社宅使用料計算月額2
//			initDto.setSc003ShatakuShiyoryo2(getKingakuEdit(
//					calcResult.getShatakuShiyouryouGetsugaku().stripTrailingZeros().toPlainString()));
//		} else {
//			// ⑧基準単価2設定
//			initDto.setSc003KijunTanka2("");
//			// ⑫使用料月額2設定
//			initDto.setSc003PatternShiyoryo2("");
//			// ⑬年齢加算係数
//			initDto.setSc003NenreikasanKeisu("");
//			// 社宅使用料計算月額2
//			initDto.setSc003ShatakuShiyoryo2("");
//		}
//		// 経年調整なし使用料を算出し設定
//		setKeinenChoseiNashiShiyoryo2(initDto);

		// 用途判定
		if (CheckUtils.isEmpty(paramMap.get("youtoSelect"))) {
			logger.debug("「用途」が選択されていないため、再計算を行わない");
			return false;
		}
		// 基準使用料算定上延べ面積設定
		if (!setKijunMenseki2(paramMap)) {
			return false;
		}
		// 社宅使用料算定上延べ面積を算出し設定
		if (!setShatakuMenseki2(paramMap)) {
			return false;
		}
		// 使用料計算
		calcResult = getShatakuShiyouryouKeisanJouhou2(paramMap);
		// 計算結果判定
		if (calcResult != null) {
			// ⑧基準単価2設定
			paramMap.put("kijunTanka2", getKingakuEdit(calcResult.getKijunShiyouryou().stripTrailingZeros().toPlainString()));
			// ⑫使用料月額2設定
			paramMap.put("patternShiyoryo2", 
					getKingakuEdit(calcResult.getPatternShiyoryouGetsugaku().stripTrailingZeros().toPlainString()));
			// ⑬年齢加算係数
			paramMap.put("nenreikasanKeisu", getFloatEdit(calcResult.getNenreiKasanKeisu()));
			// 社宅使用料計算月額2
			paramMap.put("shatakuShiyoryo2", getKingakuEdit(
					calcResult.getShatakuShiyouryouGetsugaku().stripTrailingZeros().toPlainString()));
			// 単価(親画面への戻り値用)
			paramMap.put("tanka", calcResult.getTanka().toPlainString());
			// 算定経年(親画面への戻り値用)
			paramMap.put("santeiKeinen", Integer.toString(calcResult.getSanteiKeinen()));
			// 経年残価率(親画面への戻り値用)
			paramMap.put("keinenZankaristu", calcResult.getKeinenZankaristu().toPlainString());
		} else {
			// ⑧基準単価2設定
			paramMap.put("kijunTanka2", "");
			// ⑫使用料月額2設定
			paramMap.put("patternShiyoryo2", "");
			// ⑬年齢加算係数
			paramMap.put("nenreikasanKeisu", "");
			// 社宅使用料計算月額2
			paramMap.put("shatakuShiyoryo2","");
			// 単価(親画面への戻り値用)
			paramMap.put("tanka", CodeConstant.STRING_ZERO);
			// 算定経年(親画面への戻り値用)
			paramMap.put("santeiKeinen", CodeConstant.STRING_ZERO);
			// 経年残価率(親画面への戻り値用)
			paramMap.put("keinenZankaristu", CodeConstant.STRING_ZERO);
		}
		// 経年調整なし使用料を算出し設定
		setKeinenChoseiNashiShiyoryo2(paramMap);
		return true;
	}

	/**
	 * 基準使用料算定上延べ面積の設定（右側）
	 * 
	 * 「※」項目はアドレスとして戻り値になる。 
	 * 
	 * @param initDto	*DTO
	 * @return
	 */
//	private Boolean setKijunMenseki2(Skf3022Sc003CommonDto initDto) {
	private Boolean setKijunMenseki2(Map<String, String> paramMap) {
// 更新してるのはKijunMenseki2だけ

		BigDecimal nobeMenseki = null;
		BigDecimal sunRoomMenseki = null;
		BigDecimal kaidanMenseki = null;

//		// 延べ面積入力値判定
//		if (!CheckUtils.isEmpty(initDto.getSc003InputNobeMenseki())) {
//			try {
//				nobeMenseki = new BigDecimal(initDto.getSc003InputNobeMenseki());
//				sunRoomMenseki = new BigDecimal(getMensekiText(initDto.getSc003SunRoom()));
//				kaidanMenseki = new BigDecimal(getMensekiText(initDto.getSc003Kaidan()));
//			} catch (NumberFormatException e) {
//				// デバッグログ
//				logger.debug("数値変換失敗");
//				return false;
//			}
//		} else {
//			// デバッグログ
//			logger.debug("面積が未入力のため、再計算は行わない");
//			return false;
//		}
//		// ⑥基準使用料算定上延べ面積2設定
//		// 使用料区分判定
//		if (CodeConstant.SHIYOURYOU_KEISAN_KBN_SHIN.equals(shiyouryouKbn)) {
//			// 延べ面積 - サンルーム面積 - 階段面積
//			initDto.setSc003KijunMenseki2(getMensekiEdit(nobeMenseki.subtract(		// 延べ面積
//							sunRoomMenseki.add(kaidanMenseki) // サンルーム面積 + 階段面積
//							).stripTrailingZeros()));
//		} else {
//			// 延べ面積 - サンルーム面積 - 階段面積 - 物置調整面積
//			initDto.setSc003KijunMenseki2(getMensekiEdit(
//					nobeMenseki.subtract(		// 貸与面積
//							sunRoomMenseki.add(	// サンルーム面積
//									kaidanMenseki.add(initDto.getHdnBarnMensekiAdjust())) // 階段面積 + 物置調整面積
//							).stripTrailingZeros()));
//		}
		// 延べ面積入力値判定
		if (!CheckUtils.isEmpty(paramMap.get("inputNobeMenseki"))) {
			try {
				nobeMenseki = new BigDecimal(paramMap.get("inputNobeMenseki"));
				sunRoomMenseki = new BigDecimal(getMensekiText(paramMap.get("sunRoom")));
				kaidanMenseki = new BigDecimal(getMensekiText(paramMap.get("kaidan")));
			} catch (NumberFormatException e) {
				// デバッグログ
				logger.debug("数値変換失敗");
				return false;
			}
		} else {
			// デバッグログ
			logger.debug("面積が未入力のため、再計算は行わない");
			return false;
		}
		// ⑥基準使用料算定上延べ面積2設定
		// 使用料区分判定
		if (CodeConstant.SHIYOURYOU_KEISAN_KBN_SHIN.equals(shiyouryouKbn)) {
			// 延べ面積 - サンルーム面積 - 階段面積
			paramMap.put("kijunMenseki2", getMensekiEdit(nobeMenseki.subtract(		// 延べ面積
							sunRoomMenseki.add(kaidanMenseki) // サンルーム面積 + 階段面積
							)));
		} else {
			// 延べ面積 - サンルーム面積 - 階段面積 - 物置調整面積
			paramMap.put("kijunMenseki2", getMensekiEdit(
					nobeMenseki.subtract(		// 貸与面積
							sunRoomMenseki.add(	// サンルーム面積
									kaidanMenseki.add(new BigDecimal(paramMap.get("hdnBarnMensekiAdjust")))) // 階段面積 + 物置調整面積
							)));
		}
		return true;
	}

	/**
	 * 社宅使用料算定上延べ面積の設定（右側）
	 * 
	 * 「※」項目はアドレスとして戻り値になる。 
	 * 
	 * @param initDto	*DTO
	 * @return
	 */
//	private Boolean setShatakuMenseki2(Skf3022Sc003CommonDto initDto) {
	private Boolean setShatakuMenseki2(Map<String, String> paramMap) {
// 更新してるのはShatakuMenseki2だけ
		BigDecimal nobeMenseki = null;
		BigDecimal sunRoomMenseki = null;
		BigDecimal monookiMenseki = null;
//
//		// 延べ面積入力値判定
//		if (!CheckUtils.isEmpty(initDto.getSc003InputNobeMenseki())) {
//			try {
//				nobeMenseki = new BigDecimal(initDto.getSc003InputNobeMenseki());
//				sunRoomMenseki = new BigDecimal(getMensekiText(initDto.getSc003SunRoom()));
//				monookiMenseki = new BigDecimal(getMensekiText(initDto.getSc003Monooki()));
//			} catch (NumberFormatException e) {
//				// デバッグログ
//				logger.debug("数値変換失敗");
//				return false;
//			}
//		} else {
//			// デバッグログ
//			logger.debug("面積が未入力のため、再計算は行わない");
//			return false;
//		}
//
//		// ⑦社宅使用料算定上延べ面積2設定
//		// 使用料区分判定
//		if (CodeConstant.SHIYOURYOU_KEISAN_KBN_SHIN.equals(shiyouryouKbn)) {
//			// 延べ面積 + 物置面積(小数切捨て)
//			initDto.setSc003ShatakuMenseki2(getShatakuMensekiEdit(nobeMenseki.add(monookiMenseki)));
//		} else {
//			// 延べ面積 - サンルーム面積 / 2 - 物置調整面積(小数切捨て)
//			initDto.setSc003ShatakuMenseki2(getShatakuMensekiEdit(
//					nobeMenseki.subtract(		// 貸与面積
//							(sunRoomMenseki.divide(new BigDecimal("2.00"))).subtract(	// サンルーム面積 / 2
//									initDto.getHdnBarnMensekiAdjust()) // 物置調整面積
//							)));
//		}

		// 延べ面積入力値判定
		if (!CheckUtils.isEmpty(paramMap.get("inputNobeMenseki"))) {
			try {
				nobeMenseki = new BigDecimal(paramMap.get("inputNobeMenseki"));
				sunRoomMenseki = new BigDecimal(getMensekiText(paramMap.get("sunRoom")));
				monookiMenseki = new BigDecimal(getMensekiText(paramMap.get("monooki")));
			} catch (NumberFormatException e) {
				// デバッグログ
				logger.debug("数値変換失敗");
				return false;
			}
		} else {
			// デバッグログ
			logger.debug("面積が未入力のため、再計算は行わない");
			return false;
		}

		// ⑦社宅使用料算定上延べ面積2設定
		// 使用料区分判定
		if (CodeConstant.SHIYOURYOU_KEISAN_KBN_SHIN.equals(shiyouryouKbn)) {
			// 延べ面積 + 物置面積(小数切捨て)
			paramMap.put("shatakuMenseki2", getShatakuMensekiEdit(nobeMenseki.add(monookiMenseki)));
		} else {
			// 延べ面積 - サンルーム面積 / 2 - 物置調整面積(小数切捨て)
			paramMap.put("shatakuMenseki2", getShatakuMensekiEdit(
					nobeMenseki.subtract(		// 貸与面積
							(sunRoomMenseki.divide(new BigDecimal("2.00"))).subtract(	// サンルーム面積 / 2
									new BigDecimal(paramMap.get("hdnBarnMensekiAdjust"))) // 物置調整面積
							)));
		}
		return true;
	}

	/**
	 * 社宅使用料計算(右側)
	 * 
	 * @param initDto	DTO
	 * @return
	 * @throws ParseException
	 */
//	private SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp 
//			getShatakuShiyouryouKeisanJouhou2(Skf3022Sc003CommonDto initDto) throws ParseException {
	private SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp 
	getShatakuShiyouryouKeisanJouhou2(Map<String, String> paramMap) throws ParseException {
// DTOの更新なし
//		// 社宅利用料計算結果
//		SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp retEntity = null;
//		// 社宅利用料計算情報引数
//		SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp inputEntity =
//				new SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp();
//
//		// 処理区分
//		inputEntity.setShoriKbn("1");
//		// 処理年月
//		inputEntity.setShoriNengetsu(skfBaseBusinessLogicUtils.getSystemProcessNenGetsu());
//		// 建築年月日
//		inputEntity.setKenchikuNengappi(initDto.getHdnBuildDate());
//		// 構造区分(入力した構造区分)
//		inputEntity.setStructureKbn(initDto.getHdnStructureKbn());
//		// 地域区分(選択された地域区分)
//		inputEntity.setAreaKbn(initDto.getHdnAreaKbn());
//		// 基準使用料算定上延べ面積
//		inputEntity.setKijunMenseki(getMensekiText(initDto.getSc003KijunMenseki2()));
//		// 社宅使用料算定上延べ面積
//		inputEntity.setShatakuMenseki(getMensekiText(initDto.getSc003ShatakuMenseki2()));
//		// 用途区分
//		inputEntity.setAuseKbn(initDto.getSc003YoutoSelect());
//		// 役員区分(なし：※パラメータの役員区分未使用)
//		inputEntity.setYakuinKbn(CodeConstant.YAKUIN_KBN_NASHI);
//		// 社宅賃貸料
//		if (CheckUtils.isEmpty(initDto.getHdnShatakuChintairyo())) {
//			inputEntity.setShatakuChintairyou(CodeConstant.STRING_ZERO);
//		} else {
//			inputEntity.setShatakuChintairyou(initDto.getHdnShatakuChintairyo());
//		}
//		// 延べ面積
//		inputEntity.setNobeMenseki(getMensekiText(initDto.getSc003InputNobeMenseki()));
//		// サンルーム面積
//		inputEntity.setSunroomMenseki(getMensekiText(initDto.getSc003SunRoom()));
//		// 階段面積
//		inputEntity.setKaidanMenseki(getMensekiText(initDto.getSc003Kaidan()));
//		// 物置面積
//		inputEntity.setMonookiMenseki(getMensekiText(initDto.getSc003Monooki()));
//		// 生年月日
//		inputEntity.setSeinengappi(initDto.getHdnSeinengappi());

		// 社宅利用料計算結果
		SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp retEntity = null;
		// 社宅利用料計算情報引数
		SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp inputEntity =
				new SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp();

		// 処理区分
		inputEntity.setShoriKbn("1");
		// 処理年月
		inputEntity.setShoriNengetsu(skfBaseBusinessLogicUtils.getSystemProcessNenGetsu());
		// 建築年月日
		inputEntity.setKenchikuNengappi(paramMap.get("hdnBuildDate"));
		// 構造区分(入力した構造区分)
		inputEntity.setStructureKbn(paramMap.get("hdnStructureKbn"));
		// 地域区分(選択された地域区分)
		inputEntity.setAreaKbn(paramMap.get("hdnAreaKbn"));
		// 基準使用料算定上延べ面積
		inputEntity.setKijunMenseki(getMensekiText(paramMap.get("kijunMenseki2")));
		// 社宅使用料算定上延べ面積
		inputEntity.setShatakuMenseki(getMensekiText(paramMap.get("shatakuMenseki2")));
		// 用途区分
		inputEntity.setAuseKbn(paramMap.get("youtoSelect"));
		// 役員区分(なし：※パラメータの役員区分未使用)
		inputEntity.setYakuinKbn(CodeConstant.YAKUIN_KBN_NASHI);
		// 社宅賃貸料
		if (CheckUtils.isEmpty(paramMap.get("hdnShatakuChintairyo"))) {
			inputEntity.setShatakuChintairyou(CodeConstant.STRING_ZERO);
		} else {
			inputEntity.setShatakuChintairyou(paramMap.get("hdnShatakuChintairyo"));
		}
		// 延べ面積
		inputEntity.setNobeMenseki(getMensekiText(paramMap.get("inputNobeMenseki")));
		// サンルーム面積
		inputEntity.setSunroomMenseki(getMensekiText(paramMap.get("sunRoom")));
		// 階段面積
		inputEntity.setKaidanMenseki(getMensekiText(paramMap.get("kaidan")));
		// 物置面積
		inputEntity.setMonookiMenseki(getMensekiText(paramMap.get("monooki")));
		// 生年月日
		inputEntity.setSeinengappi(paramMap.get("hdnSeinengappi"));

		// 使用料計算
		retEntity = skfBaseBusinessLogicUtils.getShatakuShiyouryouKeisan(inputEntity);
		return retEntity;
	}

	/**
	 * 金額項目にカンマと単位を付与
	 * null、空文字は「"0円"」を返却する
	 * 
	 * @param str	金額文字列
	 * @return		金額文字列をカンマ区切りにし、「"円"」を付与した文字列
	 */
	public String getKingakuEdit(String str) {

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
	 * 数値項目を小数第2位までの文字列に変換
	 * 小数第3位は四捨五入
	 * nullは「"0.00"」を返却する
	 * 
	 * @param bigDecimal	数値
	 * @return				小数第2位までの文字列
	 */
	public String getFloatEdit(BigDecimal bigDecimal) {

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
	 * 経年調整なし使用料の設定（右側）
	 * 
	 * 「※」項目はアドレスとして戻り値になる。 
	 * 
	 * @param initDto	*DTO
	 */
//	private void setKeinenChoseiNashiShiyoryo2(Skf3022Sc003CommonDto initDto) {
	private void setKeinenChoseiNashiShiyoryo2(Map<String, String> paramMap) {
// 更新してるのはKeinenChouseinashiShiyoryo2だけ
//
//		// 経年調整なし使用料 = 基準使用料算定上延べ面積 * 基準単価
//		try {
//			// 基準使用料算定上延べ面積 
//			BigDecimal shatakuMenseki = new BigDecimal(getMensekiText(initDto.getSc003ShatakuMenseki2()));
//			// 基準単価
//			BigDecimal kijuntanka = new BigDecimal(getKingakuText(initDto.getSc003KijunTanka2()));
//			// 基準使用料算定上延べ面積 * 基準単価
//			BigDecimal keinenChoseiNashiShiyoryo = shatakuMenseki.multiply(kijuntanka);
//			// 整数変換して設定(小数切捨て)
//			initDto.setSc003KeinenChouseinashiShiyoryo2(
//					getKingakuEdit(keinenChoseiNashiShiyoryo.setScale(0, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString()));
//		} catch (ArithmeticException e) {
//			// デバッグログ
//			logger.debug("数値変換失敗");
//		} catch (NumberFormatException e) {
//			// デバッグログ
//			logger.debug("数値変換失敗");
//		}

		// 経年調整なし使用料 = 基準使用料算定上延べ面積 * 基準単価
		try {
			// 基準使用料算定上延べ面積 
			BigDecimal shatakuMenseki = new BigDecimal(getMensekiText(paramMap.get("shatakuMenseki2")));
			// 基準単価
			BigDecimal kijuntanka = new BigDecimal(getKingakuText(paramMap.get("kijunTanka2")));
			// 基準使用料算定上延べ面積 * 基準単価
			BigDecimal keinenChoseiNashiShiyoryo = shatakuMenseki.multiply(kijuntanka);
			// 整数変換して設定(小数切捨て)
			paramMap.put("keinenChouseinashiShiyoryo2", 
					getKingakuEdit(keinenChoseiNashiShiyoryo.setScale(0, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString()));
		} catch (ArithmeticException e) {
			// デバッグログ
			logger.debug("数値変換失敗");
		} catch (NumberFormatException e) {
			// デバッグログ
			logger.debug("数値変換失敗");
		}
	}

	/**
	 * 面積項目より単位を削除した文字列を取得
	 * null、空文字は「"0"」を返却する
	 * 
	 * @param str	面積項目
	 * @return		面積文字列の数値のみ
	 */
	public String getMensekiText(String str) {

		String changeString = str;
		if (CheckUtils.isEmpty(changeString)) {
			return CodeConstant.STRING_ZERO;
		}
		// 変換
		changeString = changeString.replace(SkfCommonConstant.SQUARE_MASTER, "").replace(",", "").trim();
		return changeString;
	}

	/**
	 * 面積項目にカンマと単位を付与(小数第2位)
	 * 小数第3位は四捨五入
	 * nullは「"0.00㎡"」を返却する
	 * 
	 * @param str	面積項目
	 * @return		面積文字列をカンマ区切りにし、「"㎡"」を付与した文字列
	 */
	public String getMensekiEdit(BigDecimal menseki) {

		String changeString = null;
		DecimalFormat df1 = new DecimalFormat("#,##0.00");
		if (menseki == null) {
			return ("0.00" + " " + SkfCommonConstant.SQUARE_MASTER);
		}
		// 変換
		changeString = df1.format(menseki.setScale(2, BigDecimal.ROUND_HALF_UP)) + " " + SkfCommonConstant.SQUARE_MASTER;
		return changeString;
	}

	/**
	 * 面積項目にカンマと単位を付与(小数切捨て)
	 * null、空文字は「"0㎡"」を返却する
	 * 
	 * @param str	面積項目
	 * @return		面積文字列をカンマ区切りにし、「"㎡"」を付与した文字列
	 */
	public String getShatakuMensekiEdit(BigDecimal menseki) {

		String changeString = null;
		BigDecimal changeMenseki = new BigDecimal("0.00");
		DecimalFormat df1 = new DecimalFormat("#,##0");
		if (menseki != null) {
			changeMenseki = menseki;
		}
		// 変換
		changeString = df1.format(changeMenseki.setScale(
				0, BigDecimal.ROUND_DOWN).stripTrailingZeros()) + " " + SkfCommonConstant.SQUARE_MASTER;
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
}
