/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2020.domain.service.skf2020sc002;

/**
 * Skf2020Sc002 社宅入居希望等調書（申請者用)の非同期処理クラス。
 * 
 * @author NEXCOシステムズ
 */
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetBihinItemToBeReturnExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetBihinItemToBeReturnExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetDdlNowShatakuNameByCdExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetDdlNowShatakuNameByCdExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetShatakuInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetShatakuInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc002.Skf2020Sc002GetBihinItemToBeReturnExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc002.Skf2020Sc002GetDdlNowShatakuNameByCdExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc002.Skf2020Sc002GetShatakuInfoExpRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.DateUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfHtmlCreateUtils;
import jp.co.c_nexco.skf.skf2020.domain.dto.skf2020sc002.Skf2020Sc002ChangeDropDownAsyncDto;

@Service
public class Skf2020Sc002ChangeDropDownAsyncService
		extends AsyncBaseServiceAbstract<Skf2020Sc002ChangeDropDownAsyncDto> {

	// 戻り値Map用定数
	public static final String KEY_AGENCY_LIST = "AGENCY_LIST";
	public static final String KEY_AFFILIATION1_LIST = "AFFILIATION1_LIST";
	public static final String KEY_AFFILIATION2_LIST = "AFFILIATION2_LIST";
	public static final String KEY_NOW_SHATAKU_NAME_LIST = "NOW_SHATAKU_NAME";
	public static final String KEY_TAIKYO_RIYU_KBN_LIST = "TAIKYO_RIYU_KBN";
	public static final String KEY_SESSION_TIME_LIST = "SESSION_TIME_LIST";

	public static final String FALSE = "false";
	public static final String TRUE = "true";

	@Autowired
	private SkfDropDownUtils skfDropDownUtils;
	@Autowired
	private Skf2020Sc002SharedService skf2020Sc002SharedService;
	@Autowired
	private Skf2020Sc002GetDdlNowShatakuNameByCdExpRepository skf2020Sc002GetDdlNowShatakuNameByCdExpRepository;
	@Autowired
	private Skf2020Sc002GetShatakuInfoExpRepository skf2020Sc002GetShatakuInfoExpRepository;
	@Autowired
	private Skf2020Sc002GetBihinItemToBeReturnExpRepository skf2020Sc002GetBihinItemToBeReturnExpRepository;
	@Autowired
	private SkfHtmlCreateUtils SkfHtmlCreationUtils;

	// 駐車場の有無チェック用
	private enum enmCheckParking {
		ParkingNone(0), Parking1st(1), Parking2nd(2), ParkingBoth(3);

		// フィールドの定義
		private int id;

		// コンストラクタの定義
		private enmCheckParking(int id) {
			this.id = id;
		}

		public int getInt() {
			return this.id;
		}
	};

	@Override
	public AsyncBaseDto index(Skf2020Sc002ChangeDropDownAsyncDto dto) throws Exception {

		String companyCd = CodeConstant.C001;
		String newAgencyCd = dto.getAgencyCd();
		String newAffiliation1Cd = dto.getAffiliation1Cd();
		long newShatakuKanriId = 0;
		if (dto.getShatakuKanriId() > 0) {
			dto.setShatakuKanriId(dto.getShatakuKanriId());
		}
		dto.setShainNo(dto.getShainNo());

		// 初期化処理
		List<Map<String, Object>> affiliation1List = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> affiliation2List = new ArrayList<Map<String, Object>>();

		// 機関コードが設定されていた場合は部等コードリストを作成
		if (newAgencyCd != null && !CheckUtils.isEmpty(newAgencyCd)) {
			affiliation1List = skfDropDownUtils.getDdlAffiliation1ByCd(companyCd, newAgencyCd, null, true);
			// その他を追加
			dto.getNewAgencyErr();
			Map<String, Object> soshikiMap = new HashMap<String, Object>();
			soshikiMap.put("value", "99");
			soshikiMap.put("label", "その他");
			affiliation1List.add(soshikiMap);
			LogUtils.debugByMsg("返却する部等リスト：" + affiliation1List.toString());

		}
		// 部等コードが設定されていた場合は室、チーム又は課コードリストを作成
		if (newAffiliation1Cd != null && !CheckUtils.isEmpty(newAffiliation1Cd)) {
			affiliation2List = skfDropDownUtils.getDdlAffiliation2ByCd(companyCd, newAgencyCd, newAffiliation1Cd, null,
					true);
			// その他を追加
			Map<String, Object> teamMap = new HashMap<String, Object>();
			teamMap.put("value", "99");
			teamMap.put("label", "その他");
			affiliation2List.add(teamMap);
			LogUtils.debugByMsg("返却室、チーム又は課：" + affiliation2List.toString());
		}

		// 保有社宅名が設定されていた場合
		if (dto.getShatakuKanriId() > 0) {
			// 社宅情報のクリア
			clearShatakuInfo(dto);
			// 備品情報のクリア
			dto.setReturnEquipment(CodeConstant.DOUBLE_QUOTATION);
			// 社宅管理番号が0以上の場合は条件をセット
			if (dto.getShatakuKanriId() > 0) {
				// 社宅情報の設定
				setShatakuInfo(dto);
				// 返却備品の設定
				setReturnBihinInfo(dto);
				// コントロールの活性設定
				// setControlValue(dto);
			}
		}

		dto.setDdlAffiliation1List(affiliation1List);
		dto.setDdlAffiliation2List(affiliation2List);

		return dto;
	}

	/**
	 * 
	 * 社宅情報の設定
	 * 
	 * @param initDto
	 */
	protected void setShatakuInfo(Skf2020Sc002ChangeDropDownAsyncDto dto) {
		// 保有社宅で選択された社宅の社宅管理ID

		// 現在日付の取得
		Timestamp sysDate = new Timestamp(System.currentTimeMillis());
		// Hidden
		Long hdnShatakuRoomKanriNo = CodeConstant.LONG_ZERO;
		Long hdnShatakuKanriNo = CodeConstant.LONG_ZERO;
		Long hdnNowShatakuRoomKanriNo = CodeConstant.LONG_ZERO;// 現居住社宅部屋管理番号
		Long hdnNowShatakuKanriNo = CodeConstant.LONG_ZERO;// 現居住社宅管理番号
		String hdnShatakuKikakuKbn = "";// 規格(間取り)

		// 社宅管理台帳の取得
		List<Skf2020Sc002GetDdlNowShatakuNameByCdExp> resultNowShatakuNameList = new ArrayList<Skf2020Sc002GetDdlNowShatakuNameByCdExp>();
		Skf2020Sc002GetDdlNowShatakuNameByCdExpParameter param = new Skf2020Sc002GetDdlNowShatakuNameByCdExpParameter();
		param.setShainNo(dto.getShainNo());
		param.setNyukyoDate(DateUtils.getSysDateString(SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT));
		param.setShatakuKanriId(dto.getShatakuKanriId());
		resultNowShatakuNameList = skf2020Sc002GetDdlNowShatakuNameByCdExpRepository.getDdlNowShatakuNameByCd(param);

		long shatakuKanriId = CodeConstant.LONG_ZERO;
		if (resultNowShatakuNameList != null && resultNowShatakuNameList.size() > 0) {
			shatakuKanriId = resultNowShatakuNameList.get(0).getShatakuKanriId();
			dto.setShatakuKanriId(shatakuKanriId);
			dto.setHdnSelectedNowShatakuName(resultNowShatakuNameList.get(0).getShatakuName());
		}

		// 現居住宅の情報取得
		List<Skf2020Sc002GetShatakuInfoExp> shatakuList = new ArrayList<Skf2020Sc002GetShatakuInfoExp>();
		shatakuList = getShatakuInfo(shatakuKanriId, sysDate, dto.getShainNo(), shatakuList);

		// 取得できた場合は現居住社宅の情報設定
		if (shatakuList.size() > 0) {

			// 室番号
			if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getRoomNo())) {
				dto.setNowShatakuNo(shatakuList.get(0).getRoomNo());
			}
			// 規格(間取り)
			// 規格があった場合は、貸与規格。それ以外は本来規格
			if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getKikaku())) {
				hdnShatakuKikakuKbn = shatakuList.get(0).getKikaku();// 貸与規格
				dto.setHdnShatakuKikakuKbn(hdnShatakuKikakuKbn);
				dto.setNowShatakuKikaku(hdnShatakuKikakuKbn);
				dto.setNowShatakuKikakuName(shatakuList.get(0).getKikakuName());
			} else {
				if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getOriginalKikaku())) {
					hdnShatakuKikakuKbn = shatakuList.get(0).getOriginalKikaku();// 本来規格
					dto.setHdnShatakuKikakuKbn(hdnShatakuKikakuKbn);
					dto.setNowShatakuKikaku(hdnShatakuKikakuKbn);
					dto.setNowShatakuKikakuName(shatakuList.get(0).getOriginalKikakuName());
				}
			}

			// 面積
			if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getLendMenseki())) {
				dto.setNowShatakuMenseki(shatakuList.get(0).getLendMenseki());
			}

			// 駐車場 都道府県コード（保有社宅のみ設定される）
			String wkPrefName = CodeConstant.DOUBLE_QUOTATION;
			String prefCode = CodeConstant.DOUBLE_QUOTATION;
			// 取得できたら汎用コードマスタから名称を取得
			if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getPrefCdParking())) {
				prefCode = shatakuList.get(0).getPrefCdParking();
				wkPrefName = codeCacheUtils.getElementCodeName(FunctionIdConstant.GENERIC_CODE_PREFCD, prefCode);
			}

			// 駐車場 １台目 保管場所
			if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getParkingAddress1())) {
				dto.setParking1stPlace(wkPrefName + shatakuList.get(0).getParkingAddress1());
			}

			// 駐車場 １台目 位置番号
			if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getParkingBlock1())) {
				dto.setHdnParking1stNumber(shatakuList.get(0).getParkingBlock1());
			}

			// 駐車場 ２台目 保管場所
			if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getParkingAddress2())) {
				dto.setParking2stPlace(wkPrefName + shatakuList.get(0).getParkingAddress2());
			}

			// 駐車場 ２台目 位置番号
			if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getParkingBlock2())) {
				dto.setHdnParking2stNumber(shatakuList.get(0).getParkingBlock2());
			}

			// 現在の社宅管理番号
			if (shatakuList.get(0).getShatakuKanriNo() != null) {
				hdnNowShatakuKanriNo = shatakuList.get(0).getShatakuKanriNo();
				dto.setHdnNowShatakuKanriNo(hdnNowShatakuKanriNo);
				dto.setHdnShatakuKanriNo(hdnNowShatakuKanriNo);
			}

			// 現在の部屋管理番号
			if (shatakuList.get(0).getShatakuRoomKanriNo() != null) {
				hdnNowShatakuRoomKanriNo = shatakuList.get(0).getShatakuRoomKanriNo();
				dto.setHdnShatakuRoomKanriNo(hdnNowShatakuRoomKanriNo);
				dto.setHdnShatakuRoomKanriNo(hdnNowShatakuRoomKanriNo);
			}

		}
	}

	/**
	 * 現居住社宅の取得
	 * 
	 * @param shatakuKanriId
	 * @param sysDate
	 * @param shainNo
	 * @param shatakuList
	 * @param yearMonth
	 * @return
	 */
	protected List<Skf2020Sc002GetShatakuInfoExp> getShatakuInfo(long shatakuKanriId, Timestamp sysDate, String shainNo,
			List<Skf2020Sc002GetShatakuInfoExp> shatakuList) {

		String yearMonth = DateUtils.getSysDateString(SkfCommonConstant.YMD_STYLE_YYYYMM_FLAT);

		// データの取得
		Skf2020Sc002GetShatakuInfoExpParameter param = new Skf2020Sc002GetShatakuInfoExpParameter();
		param.setShatakuKanriId(shatakuKanriId);
		param.setYearMonth(yearMonth);
		param.setShainNo(shainNo);
		shatakuList = skf2020Sc002GetShatakuInfoExpRepository.getShatakuInfo(param);

		return shatakuList;

	}

	/**
	 * 
	 * 返却備品の設定
	 * 
	 * @param dto
	 */
	private void setReturnBihinInfo(Skf2020Sc002ChangeDropDownAsyncDto dto) {

		// 返却備品有無に「0:備品返却しない」を設定
		dto.setHdnBihinHenkyakuUmu(CodeConstant.BIHIN_HENKYAKU_SHINAI);

		// 備品状態が2:保有備品または3:レンタルの表示
		List<Skf2020Sc002GetBihinItemToBeReturnExp> resultBihinItemList = new ArrayList<Skf2020Sc002GetBihinItemToBeReturnExp>();
		resultBihinItemList = getBihinItemToBeReturn(dto.getShatakuKanriId(), dto.getShainNo(), resultBihinItemList);

		// 件数が取得できた場合
		if (resultBihinItemList.size() > 0 && CollectionUtils.isNotEmpty(resultBihinItemList)) {

			// 返却備品有無に「1:備品返却する」を設定
			dto.setHdnBihinHenkyakuUmu(CodeConstant.BIHIN_HENKYAKU_SURU);

			// 【ラベル部分】
			// 要返却備品の取得
			List<String> bihinItemList = new ArrayList<String>();
			List<List<String>> bihinItemNameList = new ArrayList<List<String>>();
			for (Skf2020Sc002GetBihinItemToBeReturnExp dt : resultBihinItemList) {
				// 表示・値を設定
				bihinItemList = new ArrayList<String>();
				bihinItemList.add(dt.getBihinName());
				bihinItemNameList.add(bihinItemList);
			}
			// HTMLの作成
			String bihinItem = SkfHtmlCreationUtils.htmlBihinCreateTable(bihinItemNameList, 2);
			dto.setReturnEquipment(bihinItem);
		}
	}

	/**
	 * 
	 * 要返却備品の取得
	 * 
	 * @param shatakuKanriId
	 * @param shainNo
	 * @param yearMonth
	 * @param resultBihinItemList
	 * @return resultBihinItemList
	 */
	private List<Skf2020Sc002GetBihinItemToBeReturnExp> getBihinItemToBeReturn(long shatakuKanriId, String shainNo,
			List<Skf2020Sc002GetBihinItemToBeReturnExp> resultBihinItemList) {

		String yearMonth = DateUtils.getSysDateString(SkfCommonConstant.YMD_STYLE_YYYYMM_FLAT);

		Skf2020Sc002GetBihinItemToBeReturnExpParameter param = new Skf2020Sc002GetBihinItemToBeReturnExpParameter();
		param.setShainNo(shainNo);
		param.setShatakuKanriId(shatakuKanriId);
		param.setYearMonth(yearMonth);
		resultBihinItemList = skf2020Sc002GetBihinItemToBeReturnExpRepository.getBihinItemToBeReturn(param);
		return resultBihinItemList;

	}

	/**
	 * デフォルトの選択状態を設定
	 * 
	 * @param checkDto
	 */
	protected void setControlValue(Skf2020Sc002ChangeDropDownAsyncDto dto) {

		// 必要とする社宅初期表示制御
		// dto.setRdoKikonDisabled(TRUE);
		// dto.setRdoHitsuyoSetaiDisabled(TRUE);
		// dto.setRdoHitsuyoTanshinDisabled(TRUE);
		// dto.setRdoHitsuyoDokushinDisabled(TRUE);
		//
		// // 駐車場のみは2台貸与されている場合には駐車場のみは申請不可
		// if (skf2020Sc002SharedService.checkParking(dto.getParking1stPlace(),
		// dto.getParking2stPlace()) == 3) {
		// dto.setRdoParkingOnlyDisabled(TRUE);
		// }
		//
		// // 社宅を必要としますか？
		// if (CodeConstant.ASKED_SHATAKU_HITSUYO.equals(dto.getTaiyoHituyo()))
		// {
		// // 社宅を必要としますか？が必要の場合、入居日のカレンダーを活性
		// dto.setNyukyoYoteiDateClDisabled(FALSE);
		// // 退居項目を表示
		// dto.setTaikyoViewFlag(TRUE);
		// dto.setLblShatakuFuyouMsgRemove(FALSE);
		// } else if
		// (CodeConstant.ASKED_SHATAKU_FUYOU.equals(dto.getTaiyoHituyo())) {
		// // 退居項目を非表示
		// dto.setTaikyoViewFlag(FALSE);
		// // 退居届を促すメッセージを表示
		// dto.setLblShatakuFuyouMsgRemove(TRUE);
		// } else if
		// (CodeConstant.ASKED_SHATAKU_PARKING_ONLY.equals(dto.getTaiyoHituyo()))
		// {
		// // 社宅を必要としますか？が駐車場のみの場合、以下項目をチェック状態にする
		// dto.setHitsuyoRiyu(CodeConstant.HITUYO_RIYU_OTHERS);
		// dto.setFuhitsuyoRiyu(CodeConstant.FUYO_RIYU_OTHERS);
		// dto.setTaikyoYotei(CodeConstant.NOT_LEAVE);
		// // 退居項目を表示
		// dto.setTaikyoViewFlag(TRUE);
		// dto.setLblShatakuFuyouMsgRemove(FALSE);
		// } else {
		// dto.setTaikyoViewFlag(TRUE);
		// }
		//
		// // 社宅を必要とする理由→jsp dynamicMaskListで制御
		//
		// // 必要とする社宅
		// if (CodeConstant.SETAI.equals(dto.getHitsuyoShataku())) {
		// dto.setRdoKikonDisabled(TRUE);
		// dto.setRdoHitsuyoSetaiDisabled(FALSE);
		// dto.setRdoHitsuyoTanshinDisabled(FALSE);
		// dto.setRdoHitsuyoDokushinDisabled(FALSE);
		// } else if (CodeConstant.TANSHIN.equals(dto.getHitsuyoShataku())) {
		// dto.setRdoKikonDisabled(TRUE);
		// dto.setRdoHitsuyoSetaiDisabled(FALSE);
		// dto.setRdoHitsuyoTanshinDisabled(FALSE);
		// dto.setRdoHitsuyoDokushinDisabled(FALSE);
		// } else if (CodeConstant.DOKUSHIN.equals(dto.getHitsuyoShataku())) {
		// dto.setRdoKikonDisabled(FALSE);
		// dto.setRdoHitsuyoSetaiDisabled(TRUE);
		// dto.setRdoHitsuyoTanshinDisabled(TRUE);
		// dto.setRdoHitsuyoDokushinDisabled(FALSE);
		// }
		//
		// // 保管場所を必要とするか
		// if (CodeConstant.CAR_PARK_HITUYO.equals(dto.getParkingUmu())) {
		// // 必要の場合、カレンダーを活性
		// dto.setCarExpirationDateClDisabled(FALSE);
		// dto.setParkingUseDateClDisabled(FALSE);
		// dto.setCarExpirationDate2ClDisabled(FALSE);
		// dto.setParkingUseDate2ClDisabled(FALSE);
		//
		// if (CodeConstant.CAR_HOYU.equals(dto.getCarNoInputFlg())) {
		// dto.setCarExpirationDateClDisabled(FALSE);
		// dto.setParkingUseDateClDisabled(FALSE);
		// } else if (CodeConstant.CAR_YOTEI.equals(dto.getCarNoInputFlg())) {
		// dto.setCarExpirationDateClDisabled(TRUE);
		// dto.setParkingUseDateClDisabled(FALSE);
		// }
		//
		// if (CodeConstant.CAR_HOYU.equals(dto.getCarNoInputFlg2())) {
		// dto.setCarExpirationDate2ClDisabled(FALSE);
		// dto.setParkingUseDate2ClDisabled(FALSE);
		// } else if (CodeConstant.CAR_YOTEI.equals(dto.getCarNoInputFlg2())) {
		// dto.setCarExpirationDate2ClDisabled(TRUE);
		// dto.setParkingUseDate2ClDisabled(FALSE);
		// }
		// } else {
		// dto.setCarExpirationDateClDisabled(TRUE);
		// dto.setParkingUseDateClDisabled(TRUE);
		// dto.setCarExpirationDate2ClDisabled(TRUE);
		// dto.setParkingUseDate2ClDisabled(TRUE);
		//
		// }
		//
		// // 保有社宅が存在する場合
		// LogUtils.debugByMsg("保有社宅が存在する場合" + dto.getShatakuList());
		// if (dto.getShatakuList() != null) {
		// // 現居住宅 保有(会社借上を含む)をチェック状態にする
		// dto.setNowShataku(CodeConstant.GENNYUKYO_SHATAKU_KBN_HOYU);
		// // その他項目を非活性にする
		// dto.setRdoNowJutakuJitakuDisabeld(TRUE);
		// dto.setRdoNowJutakuKariageDisabled(TRUE);
		// dto.setRdoNowJutakuSonotaDisabled(TRUE);
		//
		// // 退居予定の場合、カレンダーを活性
		// if (CodeConstant.LEAVE.equals(dto.getTaikyoYotei())) {
		// dto.setTaikyoYoteiDateClDisabled(FALSE);
		// // 退居届を促すメッセージを非表示
		// dto.setLblShatakuFuyouMsgRemove(FALSE);
		// } else if (CodeConstant.NOT_LEAVE.equals(dto.getTaikyoYotei())) {
		// dto.setTaikyoYoteiDateClDisabled(TRUE);
		// // 退居届を促すメッセージを表示
		// dto.setLblShatakuFuyouMsgRemove(TRUE);
		// }
		//
		// } else {
		// // 現居住社宅が無い場合は駐車場のみ、現居住宅を非活性にする
		// dto.setRdoParkingOnlyDisabled(TRUE);
		// dto.setRdoNowJutakuHoyuDisabled(TRUE);
		// dto.setRdoNowJutakuJitakuDisabeld(TRUE);
		// dto.setRdoNowJutakuKariageDisabled(TRUE);
		// dto.setRdoNowJutakuSonotaDisabled(TRUE);
		// // 退居項目のカレンダーは非活性化させる
		// dto.setTaikyoYoteiDateClDisabled(TRUE);
		// }
		//
		// // 現居社宅→jsp dynamicMaskListで制御
		//
		// // 備品制御
		// if (NfwStringUtils.isNotEmpty(dto.getReturnEquipment())) {
		// dto.setSessionDayClDisabled(FALSE);
		// } else {
		// dto.setSessionDayClDisabled(TRUE);
		// }

	}

	/**
	 * ドロップダウンリストの値設定
	 * 
	 * @param dto
	 */
	@SuppressWarnings("unchecked")
	private void setControlDdl(Skf2020Sc002ChangeDropDownAsyncDto dto) {

		List<Map<String, Object>> agencyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> affiliation1List = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> affiliation2List = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> nowShatakuNameList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> taikyoRiyuKbnList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> returnWitnessRequestDateList = new ArrayList<Map<String, Object>>();

		// ドロップダウンリストを取得する
		Map<String, Object> returnMap = getDropDownLists(dto);

		// 画面表示するドロップダウンリストを取得
		agencyList.addAll((List<Map<String, Object>>) returnMap.get(Skf2020Sc002SharedService.KEY_AGENCY_LIST));
		affiliation1List
				.addAll((List<Map<String, Object>>) returnMap.get(Skf2020Sc002SharedService.KEY_AFFILIATION1_LIST));
		affiliation2List
				.addAll((List<Map<String, Object>>) returnMap.get(Skf2020Sc002SharedService.KEY_AFFILIATION2_LIST));
		nowShatakuNameList
				.addAll((List<Map<String, Object>>) returnMap.get(Skf2020Sc002SharedService.KEY_NOW_SHATAKU_NAME_LIST));
		taikyoRiyuKbnList
				.addAll((List<Map<String, Object>>) returnMap.get(Skf2020Sc002SharedService.KEY_TAIKYO_RIYU_KBN_LIST));
		returnWitnessRequestDateList
				.addAll((List<Map<String, Object>>) returnMap.get(Skf2020Sc002SharedService.KEY_SESSION_TIME_LIST));

		// dtoに値をセット
		dto.setDdlAgencyList(agencyList);
		dto.setDdlAffiliation1List(affiliation1List);
		dto.setDdlAffiliation2List(affiliation2List);
		dto.setDdlNowShatakuNameList(nowShatakuNameList);
		dto.setDdlTaikyoRiyuKbnList(taikyoRiyuKbnList);
		dto.setDdlReturnWitnessRequestDateList(returnWitnessRequestDateList);
	}

	/**
	 * ドロップダウンリストの取得
	 * 
	 * @param dto
	 * @return
	 */
	private Map<String, Object> getDropDownLists(Skf2020Sc002ChangeDropDownAsyncDto dto) {

		// 戻り値用Mapのインスタンス生成
		Map<String, Object> returnMap = new HashMap<String, Object>();

		// 機関ドロップダウンリスト
		List<Map<String, Object>> agencyList = new ArrayList<Map<String, Object>>();
		agencyList.addAll(skfDropDownUtils.getDdlAgencyByCd(CodeConstant.C001, dto.getAgencyCd(), true));
		returnMap.put(KEY_AGENCY_LIST, agencyList);

		// 部等ドロップダウンリスト
		List<Map<String, Object>> affiliation1List = new ArrayList<Map<String, Object>>();
		affiliation1List.addAll(skfDropDownUtils.getDdlAffiliation1ByCd(CodeConstant.C001, dto.getAgencyCd(),
				dto.getAffiliation1Cd(), true));
		// その他を追加
		if (affiliation1List.size() > 1) {
			Map<String, Object> soshikiMap = new HashMap<String, Object>();
			soshikiMap.put("value", "99");
			soshikiMap.put("label", "その他");
			affiliation1List.add(soshikiMap);
		}
		returnMap.put(KEY_AFFILIATION1_LIST, affiliation1List);
		LogUtils.debugByMsg("返却する部等リスト：" + affiliation1List);

		// 室、チーム又は課ドロップダウンリスト
		List<Map<String, Object>> affiliation2List = new ArrayList<Map<String, Object>>();
		affiliation2List.addAll(skfDropDownUtils.getDdlAffiliation2ByCd(CodeConstant.C001, dto.getAgencyCd(),
				dto.getAffiliation1Cd(), dto.getAffiliation2Cd(), true));
		// その他を追加
		if (affiliation2List.size() > 1) {
			Map<String, Object> teamMap = new HashMap<String, Object>();
			teamMap.put("value", "99");
			teamMap.put("label", "その他");
			affiliation2List.add(teamMap);
		}
		returnMap.put(KEY_AFFILIATION2_LIST, affiliation2List);

		// 保有社宅名ドロップダウンリストの設定
		List<Map<String, Object>> nowShatakuNameList = new ArrayList<Map<String, Object>>();
		String yearMonth = DateUtils.getSysDateString(SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT);
		nowShatakuNameList.addAll(
				skfDropDownUtils.getDdlNowShatakuNameByCd(dto.getShainNo(), yearMonth, dto.getShatakuKanriId(), false));
		returnMap.put(KEY_NOW_SHATAKU_NAME_LIST, nowShatakuNameList);

		// 退居理由ドロップダウンリストの設定
		List<Map<String, Object>> taikyoRiyuKbnList = new ArrayList<Map<String, Object>>();
		taikyoRiyuKbnList.addAll(
				skfDropDownUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_TAIKYO_RIYU, "", true));
		returnMap.put(KEY_TAIKYO_RIYU_KBN_LIST, taikyoRiyuKbnList);

		// 返却立会希望日(時)ドロップダウンリストの設定
		List<Map<String, Object>> returnWitnessRequestDateList = new ArrayList<Map<String, Object>>();
		returnWitnessRequestDateList.addAll(
				skfDropDownUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_REQUESTTIME_KBN, "", true));
		returnMap.put(KEY_SESSION_TIME_LIST, returnWitnessRequestDateList);

		return returnMap;
	}

	/**
	 * 社宅情報のクリア
	 * 
	 * @param screenId
	 * @return
	 */
	protected void clearShatakuInfo(Skf2020Sc002ChangeDropDownAsyncDto dto) {
		// 室番号
		dto.setNowShatakuNo(CodeConstant.DOUBLE_QUOTATION);
		// 規格(間取り)
		dto.setNowShatakuKikaku(CodeConstant.DOUBLE_QUOTATION);
		// 面積
		dto.setNowShatakuMenseki(CodeConstant.DOUBLE_QUOTATION);
		// 駐車場 １台目 保管場所
		dto.setParking1stPlace(CodeConstant.DOUBLE_QUOTATION);
		// 駐車場 １台目 位置番号
		dto.setHdnParking1stNumber(CodeConstant.DOUBLE_QUOTATION);
		// 駐車場 ２台目 保管場所
		dto.setParking2stPlace(CodeConstant.DOUBLE_QUOTATION);
		// 駐車場 ２台目 位置番号
		dto.setHdnParking2stNumber(CodeConstant.DOUBLE_QUOTATION);
	}

}
