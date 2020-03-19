/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc003;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc003.Skf3022Sc003GetNyukyoInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc003.Skf3022Sc003GetShatakuInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc003.Skf3022Sc003GetShatakuRoomInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc003.Skf3022Sc003GetShatakuShainInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBaseBusinessLogicUtils.SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBaseBusinessLogicUtils.SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.CodeCacheUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc003.Skf3022Sc003InitDto;
import jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc003.Skf3022Sc003SharedService;

/**
 * Skf3022Sc003InitService 使用料入力支援画面のInitサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3022Sc003InitService extends BaseServiceAbstract<Skf3022Sc003InitDto> {

	@Autowired
	private Skf3022Sc003SharedService skf3022Sc003SharedService;
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	@Autowired
	private CodeCacheUtils codeCacheUtils;
	@Autowired
	private SkfDropDownUtils ddlUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Value("${skf.common.settings.shiyouryou_keisan_kbn}")
	private String shiyouryouKbn;

	/** 定数 */
	// 残価率文字数
	public static final int ZANKARITSU_LENGTH = 6;

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
	public Skf3022Sc003InitDto index(Skf3022Sc003InitDto initDto) throws Exception {

		initDto.setPageTitleKey(MessageIdConstant.SKF3022_SC003_TITLE);

		// デバッグログ
		LogUtils.debugByMsg("初期表示");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, FunctionIdConstant.SKF3022_SC003);

		// パラメータ判定
		if (CheckUtils.isEmpty(initDto.getHdnShatakuKanriNo()) || CheckUtils.isEmpty(initDto.getHdnRoomKanriNo())
				|| CheckUtils.isEmpty(initDto.getHdnShainNo()) || CheckUtils.isEmpty(initDto.getHdnYakuinKbn())) {
			// デバッグログ
			LogUtils.debugByMsg("パラメータなし");
			return initDto;
		}

		/** DTO取得値用 */
		// 社宅管理番号
		String shatakuKanriNo = initDto.getHdnShatakuKanriNo();
		// 部屋管理番号
		String roomKanriNo = initDto.getHdnRoomKanriNo();
		// 社員番号
		String shainNo = initDto.getHdnShainNo();
//		// 役員区分(※未使用)
//		String yakuinKbn = initDto.getHdnYakuinKbn();

		/** DB取得データ */
		// 社宅情報
		List<Skf3022Sc003GetShatakuInfoExp> shatakuList = new ArrayList<Skf3022Sc003GetShatakuInfoExp>();
		// 部屋情報
		List<Skf3022Sc003GetShatakuRoomInfoExp> roomList = new ArrayList<Skf3022Sc003GetShatakuRoomInfoExp>();
		// 社員情報
		List<Skf3022Sc003GetShatakuShainInfoExp> shainList = new ArrayList<Skf3022Sc003GetShatakuShainInfoExp>();

		/** ▼DBから必要項目を取得 */
		// ①社宅情報取得
		skf3022Sc003SharedService.getShatakuInfo(shatakuKanriNo, shatakuList);
		// ②社宅部屋情報取得
		skf3022Sc003SharedService.getShatakuRoomInfo(shatakuKanriNo, roomKanriNo, roomList);
		// DB取得結果判定
		if (shatakuList.size() < 1 || roomList.size() < 1) {
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_3059);
			throwBusinessExceptionIfErrors(initDto.getResultMessages());
		}
		// ③社員情報取得
		skf3022Sc003SharedService.getShatakuShainInfo(CodeConstant.C001, shainNo, shainList);

		// 物置調整面積
		initDto.setHdnBarnMensekiAdjust(roomList.get(0).getBarnMensekiAdjust());

		// 画面項目の設定（左側の基本情報）
		setKihonJouhou(shatakuList, roomList, shainList, initDto);
		// 画面項目の設定（右側の入力情報）
		setNyuryokuJouhou(initDto);
		return initDto;
	}

	/**
	 * 画面右側の入力情報を設定
	 * 
	 * 「※」項目はアドレスとして戻り値になる。 
	 * 
	 * @param initDto			*DTO
	 * @throws ParseException
	 */
	private void setNyuryokuJouhou(Skf3022Sc003InitDto initDto) throws ParseException {

		// 規格ドロップダウン
		List<Map<String, Object>> kikakuSelecteList = new ArrayList<Map<String, Object>>();
		// 用途ドロップダウン
		List<Map<String, Object>> youtoSelecteList = new ArrayList<Map<String, Object>>();
		// Map
		Map<String, String> dtoMap = null;
		// 入居リスト
		List<Skf3022Sc003GetNyukyoInfoExp> nyukyoList = new ArrayList<Skf3022Sc003GetNyukyoInfoExp>();

		/** パラメータ取得 */
		// 規格ドロップダウン選択値
		String kikaakuSelectedValue = (initDto.getHdnRateShienKikaku() != null) ? initDto.getHdnRateShienKikaku() : "";
		// 用途ドロップダウン選択値
		String youtoSelectedValue = (initDto.getHdnRateShienYoto() != null) ? initDto.getHdnRateShienYoto() : "";
		// ②延べ面積
		String rateShienNobeMenseki = (initDto.getHdnRateShienNobeMenseki() != null) ? initDto.getHdnRateShienNobeMenseki() : "";
		// ⑥基準使用料算定上延べ面積
		String rateShienKijunMenseki = (initDto.getHdnRateShienKijunMenseki() != null) ? initDto.getHdnRateShienKijunMenseki() : "";
		// ⑦社宅使用料算定上延べ面積
		String rateShienShatakuMenseki = (initDto.getHdnRateShienShatakuMenseki() != null) ? initDto.getHdnRateShienShatakuMenseki() : "";

		// パラメータ判定
		if (kikaakuSelectedValue.length() < 1 || youtoSelectedValue.length() < 1 || rateShienNobeMenseki.length() < 1
				|| rateShienKijunMenseki.length() < 1 || rateShienShatakuMenseki.length() < 1) {
			// 遷移元判定
			if ("true".equals(initDto.getHdnTeijiFlag())
					&& initDto.getHdnSyoruiKanriNo() != null && initDto.getHdnSyoruiKanriNo().length() > 0) {
				// 提示データからの遷移のため入居希望等調書・入居決定通知テーブルから値を取得
				skf3022Sc003SharedService.getNyuukyoInfo(CodeConstant.C001, initDto.getHdnSyoruiKanriNo(), nyukyoList);
			}
			// 入居リスト判定
			if (nyukyoList.size() > 0) {
				Skf3022Sc003GetNyukyoInfoExp nyukyoInfo = nyukyoList.get(0);
				// 規格2
				if (!CheckUtils.isEmpty(nyukyoInfo.getNewShatakuKikaku())) {
					// 入居希望等調書・入居決定通知テーブルの値を設定
					kikaakuSelectedValue = nyukyoInfo.getNewShatakuKikaku();
				}
				// ①用途2
				if (!CheckUtils.isEmpty(nyukyoInfo.getHitsuyoShataku())) {
					// 入居希望等調書・入居決定通知テーブルの値を設定
					youtoSelectedValue = nyukyoInfo.getHitsuyoShataku();
				}
				// ②延べ面積2
				if (!CheckUtils.isEmpty(nyukyoInfo.getNewShatakuMenseki())) {
					// 入居希望等調書・入居決定通知テーブルの値を設定
					rateShienNobeMenseki = nyukyoInfo.getNewShatakuMenseki();
				}
			}
			// ⑥基準使用料算定上延べ面積2
			rateShienKijunMenseki = initDto.getSc003KijunMenseki1();
			// ⑦社宅使用料算定上延べ面積2
			rateShienShatakuMenseki = initDto.getSc003ShatakuMenseki1();
		}
		/** ドロップダウン選択値設定 */
		String kikakuDispStr = "";
		String youtoDispStr = "";
		// 規格2
		if (!CheckUtils.isEmpty(kikaakuSelectedValue)) {
			kikakuDispStr = codeCacheUtils.getGenericCodeName(
				FunctionIdConstant.GENERIC_CODE_KIKAKU_KBN, kikaakuSelectedValue);
		}
		if (CheckUtils.isEmpty(kikakuDispStr)) {
			// 設定されていない場合、基本情報と同じ値を設定
			kikaakuSelectedValue = initDto.getSc003KikakuSelect();
		}
		initDto.setSc003KikakuSelect(kikaakuSelectedValue);

		// ①用途2
		if (!CheckUtils.isEmpty(youtoSelectedValue)) {
			youtoDispStr = codeCacheUtils.getGenericCodeName(
				FunctionIdConstant.GENERIC_CODE_AUSE_KBN, youtoSelectedValue);
		}
		if (CheckUtils.isEmpty(youtoDispStr)) {
			// 設定されていない場合、基本情報と同じ値を設定
			youtoSelectedValue = initDto.getSc003YoutoSelect();
		}
		initDto.setSc003YoutoSelect(youtoSelectedValue);

		/** ドロップダウン作成 */
		// 規格ドロップダウン作成
		kikakuSelecteList.clear();
		kikakuSelecteList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_KIKAKU_KBN, kikaakuSelectedValue, true));
		initDto.setSc003KikakuSelectList(kikakuSelecteList);
		// ②用途ドロップダウン
		youtoSelecteList.clear();
		youtoSelecteList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_AUSE_KBN, youtoSelectedValue, true));
		initDto.setSc003YoutoSelectList(youtoSelecteList);
		// ②延べ面積2
		if (CheckUtils.isEmpty(rateShienNobeMenseki)) {
			// 設定されていない場合、基本情報と同じ値を設定
			rateShienNobeMenseki = initDto.getSc003NobeMenseki();
		}
		// 延べ面積テキストボックス
		initDto.setSc003InputNobeMenseki(rateShienNobeMenseki);
		// 基準使用料算定上延べ面積2
		initDto.setSc003KijunMenseki2(rateShienKijunMenseki);
		// ⑦社宅使用料算定上延べ面積2
		initDto.setSc003ShatakuMenseki2(rateShienShatakuMenseki);
		// Map変換
		dtoMap = createSaikeisanParam(initDto);
		// 使用料再計算処理
		if (skf3022Sc003SharedService.saiKeisan(dtoMap)) {
			setMapToDto(dtoMap, initDto);
		}
	}

	/**
	 * 再計算結果DTO設定
	 * 再計算処理結果のMapをDTOに設定する
	 * 
	 * 「※」項目はアドレスとして戻り値になる。 
	 * 
	 * @param paramMap	再計算処理結果
	 * @param initDto	*DTO
	 */
	private void setMapToDto(Map<String, String> paramMap, Skf3022Sc003InitDto initDto) {

		initDto.setSc003KijunMenseki2(paramMap.get("kijunMenseki2"));
		initDto.setSc003ShatakuMenseki2(paramMap.get("shatakuMenseki2"));
		initDto.setSc003KeinenChouseinashiShiyoryo2(paramMap.get("keinenChouseinashiShiyoryo2"));
		initDto.setSc003KijunTanka2(paramMap.get("kijunTanka2"));
		initDto.setSc003PatternShiyoryo2(paramMap.get("patternShiyoryo2"));
		initDto.setSc003NenreikasanKeisu(paramMap.get("nenreikasanKeisu"));
		initDto.setSc003ShatakuShiyoryo2(paramMap.get("shatakuShiyoryo2"));
		initDto.setSc003HdnRateShienTanka(paramMap.get("tanka"));
		initDto.setSc003HdnRateShienKeinen(paramMap.get("santeiKeinen"));
		initDto.setSc003HdnRateShienKeinenZankaRitsu(paramMap.get("keinenZankaristu"));
	}

	/**
	 * 再計算パラメータ作成
	 * 再計算処理のパラメータをMapに設定する
	 * 
	 * @param initDto
	 * @return	map
	 */
	private Map<String, String> createSaikeisanParam(Skf3022Sc003InitDto initDto) {
	
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("inputNobeMenseki", initDto.getSc003InputNobeMenseki());
		paramMap.put("youtoSelect", initDto.getSc003YoutoSelect());
		paramMap.put("sunRoom", initDto.getSc003SunRoom());
		paramMap.put("kaidan", initDto.getSc003Kaidan());
		paramMap.put("monooki", initDto.getSc003Monooki());
		paramMap.put("kijunTanka2", initDto.getSc003KijunTanka2());
		paramMap.put("kijunMenseki2", initDto.getSc003KijunMenseki2());
		paramMap.put("shatakuMenseki2", initDto.getSc003ShatakuMenseki2());
		paramMap.put("hdnShatakuChintairyo", initDto.getHdnShatakuChintairyo());
		paramMap.put("hdnTeijiFlag", initDto.getHdnTeijiFlag());
		paramMap.put("hdnSyoruiKanriNo", initDto.getHdnSyoruiKanriNo());
		paramMap.put("hdnSeinengappi", initDto.getHdnSeinengappi());
		paramMap.put("hdnBarnMensekiAdjust", initDto.getHdnBarnMensekiAdjust().toPlainString());
		paramMap.put("hdnBuildDate", initDto.getHdnBuildDate());
		paramMap.put("hdnStructureKbn", initDto.getHdnStructureKbn());
		paramMap.put("hdnAreaKbn", initDto.getHdnAreaKbn());
		paramMap.put("patternShiyoryo2", "");
		paramMap.put("nenreikasanKeisu", "");
		paramMap.put("shatakuShiyoryo2", "");
		paramMap.put("keinenChouseinashiShiyoryo2", "");
		return paramMap;
	}

	/**
	 * 画面の基本情報設定(左側)
	 * 
	 * 「※」項目はアドレスとして戻り値になる。 
	 * 
	 * @param shatakuList		社宅情報リスト
	 * @param roomList			部屋情報リスト
	 * @param shainList			社員情報リスト
	 * @param initDto			*DTO
	 * @throws ParseException
	 */
	private void setKihonJouhou(List<Skf3022Sc003GetShatakuInfoExp> shatakuList,
								List<Skf3022Sc003GetShatakuRoomInfoExp> roomList,
								List<Skf3022Sc003GetShatakuShainInfoExp> shainList,
								Skf3022Sc003InitDto initDto) throws ParseException {

		// DB取得データ
		Skf3022Sc003GetShatakuInfoExp shatakuInfo = (shatakuList.size() != 0) ? shatakuList.get(0) : new Skf3022Sc003GetShatakuInfoExp();
		Skf3022Sc003GetShatakuRoomInfoExp roomInfo = (roomList.size() != 0) ? roomList.get(0) : new Skf3022Sc003GetShatakuRoomInfoExp();
		Skf3022Sc003GetShatakuShainInfoExp shainInfo = (shainList.size() != 0) ? shainList.get(0) : new Skf3022Sc003GetShatakuShainInfoExp();
		// 社宅利用料計算結果
		SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp calcResult = null;

		/** 非表示項目設定 */
		// 地域区分
		initDto.setHdnAreaKbn(shatakuInfo.getAreaKbn());
		// 用途区分
		initDto.setHdnAuseKbn(roomInfo.getOriginalAuse());
		// 建築年月日
		initDto.setHdnBuildDate(shatakuInfo.getBuildDate());
		// 構造区分
		initDto.setHdnStructureKbn(shatakuInfo.getStructureKbn());
		// 生年月日
		if (shainInfo != null && shainInfo.getBirthdayDay() != 0) {
			initDto.setHdnSeinengappi(Integer.toString(shainInfo.getBirthdayYear())
					+ String.format("%02d", shainInfo.getBirthdayMonth())
					+ String.format("%02d", shainInfo.getBirthdayDay()));
		} else {
			initDto.setHdnSeinengappi("");
		}

		/** 画面ヘッダ部の設定 */
		// 社宅名
		if (shatakuInfo.getShatakuName() != null) {
			initDto.setSc003ShatakuName(shatakuInfo.getShatakuName());
		} else {
			initDto.setSc003ShatakuName("");
		}
		// 部屋番号
		if (roomInfo.getRoomNo() != null) {
			initDto.setSc003RoomNo(roomInfo.getRoomNo());
		} else {
			initDto.setSc003RoomNo("");
		}
		// 所在地
		if (shatakuInfo.getAddress() != null) {
			initDto.setSc003Address(shatakuInfo.getAddress());
		} else {
			initDto.setSc003Address("");
		}
		// 所在地区分(地域区分)
		// 所在地区分(地域区分)・汎用コード取得
		Map<String, String> genericCodeMapAreaKbn = 
				skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_AREA_KBN);
		// 所在地区分(地域区分)名設定
		initDto.setSc003AddressKbn(genericCodeMapAreaKbn.get(shatakuInfo.getAreaKbn()));

		/** 画面左側（基本情報）の設定 */
		// 規格
		initDto.setSc003Kikaku(codeCacheUtils.getGenericCodeName(
				FunctionIdConstant.GENERIC_CODE_KIKAKU_KBN, roomInfo.getOriginalKikaku()));
		// 規格ドロップダウン選択値
		initDto.setSc003KikakuSelect(roomInfo.getOriginalKikaku());
		// ①用途
		initDto.setSc003Youto(codeCacheUtils.getGenericCodeName(
				FunctionIdConstant.GENERIC_CODE_AUSE_KBN, roomInfo.getOriginalAuse()));
		// 用途ドロップダウン選択値
		initDto.setSc003YoutoSelect(roomInfo.getOriginalAuse());
		// ②延べ面積
		if (roomInfo.getLendMenseki() != null) {
			initDto.setSc003NobeMenseki(skf3022Sc003SharedService.getFloatEdit(roomInfo.getLendMenseki()));
		} else {
			initDto.setSc003NobeMenseki("");
		}
		// ③サンルーム
		if (roomInfo.getSunRoomMenseki() != null) {
			initDto.setSc003SunRoom(skf3022Sc003SharedService.getFloatEdit(roomInfo.getSunRoomMenseki()));
		} else {
			initDto.setSc003SunRoom("");
		}
		// ④階段
		if (roomInfo.getStairsMenseki() != null) {
			initDto.setSc003Kaidan(skf3022Sc003SharedService.getFloatEdit(roomInfo.getStairsMenseki()));
		} else {
			initDto.setSc003Kaidan("");
		}
		// ⑤物置
		if (roomInfo.getBarnMenseki() != null) {
			initDto.setSc003Monooki(skf3022Sc003SharedService.getFloatEdit(roomInfo.getBarnMenseki()));
		} else {
			initDto.setSc003Monooki("");
		}
		// ⑥基準使用料算定上延べ面積1設定
		setKijunMenseki1(roomInfo, initDto);
		// ⑦社宅使用料算定上延べ面積1設定
		setShatakuMenseki1(roomInfo, initDto);
		// 使用料計算
		calcResult = getShatakuShiyouryouKeisanJouhou1(initDto);
		// 使用料計算結果から値を設定
		if (calcResult != null) {
			// ⑧基準単価
			initDto.setSc003KijunTanka1(
					skf3022Sc003SharedService.getKingakuEdit(calcResult.getKijunShiyouryou().stripTrailingZeros().toPlainString()));
			// ⑩経年
			initDto.setSc003Keinen(getNenEdit(calcResult.getSanteiKeinen()));
			// ⑪経年調整　残価率
			BigDecimal b = calcResult.getKeinenZankaristu().stripTrailingZeros();
			String zankaritsu = b.toPlainString();
			if (zankaritsu.contains(".") && zankaritsu.length() < ZANKARITSU_LENGTH) {
				zankaritsu += "0";
			}
//			initDto.setSc003KeinenZankaritsu(calcResult.getKeinenZankaristu().toPlainString());
			initDto.setSc003KeinenZankaritsu(zankaritsu);
			// ⑫使用料月額
			initDto.setSc003PatternShiyoryo1(
					skf3022Sc003SharedService.getKingakuEdit(calcResult.getPatternShiyoryouGetsugaku().stripTrailingZeros().toPlainString()));
		} else {
			// ⑧基準単価
			initDto.setSc003KijunTanka1("");
			// ⑩経年
			initDto.setSc003Keinen("");
			// ⑪経年調整　残価率
			initDto.setSc003KeinenZankaritsu("");
			// ⑫使用料月額
			initDto.setSc003PatternShiyoryo1("");
		}
		// ⑨経年調整なし使用料
		setKeinenChoseiNashiShiyoryo1(initDto);
	}

	/**
	 * 社宅使用料計算(左側)
	 * 
	 * @param initDto	DTO
	 * @return	計算結果
	 * @throws ParseException
	 */
	private SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp
		getShatakuShiyouryouKeisanJouhou1(Skf3022Sc003InitDto initDto) throws ParseException {

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
		inputEntity.setKenchikuNengappi(initDto.getHdnBuildDate());
		// 構造区分(入力した構造区分)
		inputEntity.setStructureKbn(initDto.getHdnStructureKbn());
		// 地域区分(選択された地域区分)
		inputEntity.setAreaKbn(initDto.getHdnAreaKbn());
		// 基準使用料算定上延べ面積
		inputEntity.setKijunMenseki(skf3022Sc003SharedService.getMensekiText(initDto.getSc003KijunMenseki1()));
		// 社宅使用料算定上延べ面積
		inputEntity.setShatakuMenseki(skf3022Sc003SharedService.getMensekiText(initDto.getSc003ShatakuMenseki1()));
		// 用途区分
		inputEntity.setAuseKbn(initDto.getHdnAuseKbn());
		// 役員区分(なし：※パラメータの役員区分未使用)
		inputEntity.setYakuinKbn(CodeConstant.YAKUIN_KBN_NASHI);
		// 延べ面積
		inputEntity.setNobeMenseki(skf3022Sc003SharedService.getMensekiText(initDto.getSc003NobeMenseki()));
		// サンルーム面積
		inputEntity.setSunroomMenseki(skf3022Sc003SharedService.getMensekiText(initDto.getSc003SunRoom()));
		// 階段面積
		inputEntity.setKaidanMenseki(skf3022Sc003SharedService.getMensekiText(initDto.getSc003Kaidan()));
		// 物置面積
		inputEntity.setMonookiMenseki(skf3022Sc003SharedService.getMensekiText(initDto.getSc003Monooki()));

		// 使用料計算
		retEntity = skfBaseBusinessLogicUtils.getShatakuShiyouryouKeisan(inputEntity);
		return retEntity;
	}

	/**
	 * 経年調整なし使用料の設定(左側)
	 * 
	 * 「※」項目はアドレスとして戻り値になる。 
	 * 
	 * @param initDto	*DTO
	 * @return
	 */
	private void setKeinenChoseiNashiShiyoryo1(Skf3022Sc003InitDto initDto) {

		// 経年調整なし使用料 = 基準使用料算定上延べ面積 * 基準単価
		try {
			// 基準使用料算定上延べ面積 
			BigDecimal shatakuMenseki = new BigDecimal(skf3022Sc003SharedService.getMensekiText(initDto.getSc003ShatakuMenseki1()));
			// 基準単価
			BigDecimal kijuntanka = new BigDecimal(skf3022Sc003SharedService.getKingakuText(initDto.getSc003KijunTanka1()));
			// 基準使用料算定上延べ面積 * 基準単価
			BigDecimal keinenChoseiNashiShiyoryo = shatakuMenseki.multiply(kijuntanka);
			// 整数変換して設定(小数切捨て)
			initDto.setSc003KeinenChouseinashiShiyoryo1(
					skf3022Sc003SharedService.getKingakuEdit(keinenChoseiNashiShiyoryo.setScale(0, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString()));
		} catch (ArithmeticException e) {
			// デバッグログ
			LogUtils.debugByMsg("数値変換失敗");
		} catch (NumberFormatException e) {
			// デバッグログ
			LogUtils.debugByMsg("数値変換失敗");
		}
	}

	/**
	 * 基準使用料算定上延べ面積の設定（左側）
	 * 「※」項目はアドレスとして戻り値になる。 
	 * 
	 * @param roomInfo	社宅部屋情報
	 * @param initDto	*DTO
	 */
	private void setKijunMenseki1(Skf3022Sc003GetShatakuRoomInfoExp roomInfo, Skf3022Sc003InitDto initDto) {

		// ⑥基準使用料算定上延べ面積1設定
		// 使用料区分判定
		if (CodeConstant.SHIYOURYOU_KEISAN_KBN_SHIN.equals(shiyouryouKbn)) {
			// 貸与面積 - サンルーム面積 - 階段面積
			initDto.setSc003KijunMenseki1(
					skf3022Sc003SharedService.getMensekiEdit(roomInfo.getLendMenseki().subtract(		// 貸与面積
							roomInfo.getSunRoomMenseki().add(roomInfo.getStairsMenseki()) // サンルーム面積 + 階段面積
							)));
		} else {
			// 貸与面積 - サンルーム面積 - 階段面積 - 物置調整面積
			initDto.setSc003KijunMenseki1(
					skf3022Sc003SharedService.getMensekiEdit(roomInfo.getLendMenseki().subtract(		// 貸与面積
							roomInfo.getSunRoomMenseki().add(	// サンルーム面積
							roomInfo.getStairsMenseki().add(roomInfo.getBarnMensekiAdjust())) // 階段面積 + 物置調整面積
							)));
		}
	}

	/**
	 * 社宅使用料算定上延べ面積の設定（左側）
	 * 
	 * 「※」項目はアドレスとして戻り値になる。 
	 * 
	 * @param roomInfo	社宅部屋情報
	 * @param initDto	*DTO
	 * @return
	 */
	private Boolean setShatakuMenseki1(Skf3022Sc003GetShatakuRoomInfoExp roomInfo, Skf3022Sc003InitDto initDto) {

		// ⑦社宅使用料算定上延べ面積1設定
		// 使用料区分判定
		if (CodeConstant.SHIYOURYOU_KEISAN_KBN_SHIN.equals(shiyouryouKbn)) {
			// 貸与面積 + 物置面積(小数切捨て)
			initDto.setSc003ShatakuMenseki1(skf3022Sc003SharedService.getShatakuMensekiEdit(roomInfo.getLendMenseki().add(roomInfo.getBarnMenseki())));
		} else {
			// 貸与面積 - サンルーム面積 / 2 - 物置調整面積(小数切捨て)
			initDto.setSc003ShatakuMenseki1(
					skf3022Sc003SharedService.getShatakuMensekiEdit(roomInfo.getLendMenseki().subtract(		// 貸与面積
							(roomInfo.getSunRoomMenseki().divide(new BigDecimal("2.00"))).subtract(	// サンルーム面積 / 2
							roomInfo.getBarnMensekiAdjust()) // 物置調整面積
							)));
		}
		return true;
	}

	/**
	 * 年項目に単位を連結した文字列を取得
	 * 
	 * @param nen	年項目の数値
	 * @return		年項目数値に「"年"」を付与した文字列
	 */
	private String getNenEdit(int nen) {

		String changeString = null;
		// 変換
		changeString = Integer.toString(nen) + " 年";

		return changeString;
	}
}
