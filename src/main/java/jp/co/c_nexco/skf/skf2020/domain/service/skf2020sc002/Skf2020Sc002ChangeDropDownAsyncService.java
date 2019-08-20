/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2020.domain.service.skf2020sc002;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetBihinItemToBeReturnExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetBihinItemToBeReturnExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetShatakuInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetShatakuInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc002.Skf2020Sc002GetBihinItemToBeReturnExpRepository;
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

	@Autowired
	private SkfDropDownUtils skfDropDownUtils;
	@Autowired
	private Skf2020Sc002GetShatakuInfoExpRepository skf2020Sc002GetShatakuInfoExpRepository;
	@Autowired
	private Skf2020Sc002GetBihinItemToBeReturnExpRepository skf2020Sc002GetBihinItemToBeReturnExpRepository;
	@Autowired
	private SkfHtmlCreateUtils SkfHtmlCreationUtils;

	@Override
	public AsyncBaseDto index(Skf2020Sc002ChangeDropDownAsyncDto dto) throws Exception {

		// 変数を設定
		String companyCd = CodeConstant.C001;
		String newAgencyCd = dto.getAgencyCd();
		String newAffiliation1Cd = dto.getAffiliation1Cd();
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
			// 社宅、備品情報のクリア
			clearShatakuInfo(dto);
			// 社宅情報の設定
			setShatakuInfo(dto);
			// 返却備品の設定
			setReturnBihinInfo(dto);
		}

		dto.setDdlAffiliation1List(affiliation1List);
		dto.setDdlAffiliation2List(affiliation2List);

		return dto;
	}

	/**
	 * 
	 * 社宅情報の設定
	 * 
	 * @param dto Skf2020Sc002ChangeDropDownAsyncDto
	 */
	protected void setShatakuInfo(Skf2020Sc002ChangeDropDownAsyncDto dto) {

		// Hidden
		Long hdnNowShatakuRoomKanriNo = CodeConstant.LONG_ZERO;// 現居住社宅部屋管理番号
		Long hdnNowShatakuKanriNo = CodeConstant.LONG_ZERO;// 現居住社宅管理番号
		String hdnShatakuKikakuKbn = "";// 規格(間取り)
		long shatakuKanriId = CodeConstant.LONG_ZERO;
		if (dto.getShatakuKanriId() > 0) {
			shatakuKanriId = dto.getShatakuKanriId();
			LogUtils.debugByMsg("社宅管理番号" + shatakuKanriId);
		}

		// 現居住宅の情報取得
		List<Skf2020Sc002GetShatakuInfoExp> shatakuList = new ArrayList<Skf2020Sc002GetShatakuInfoExp>();
		shatakuList = getShatakuInfo(shatakuKanriId, dto.getShainNo(), shatakuList);

		// 取得できた場合は現居住社宅の情報設定
		if (shatakuList.size() > 0) {

			// 社宅名
			if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getShatakuName())) {
				dto.setHdnSelectedNowShatakuName(shatakuList.get(0).getShatakuName());
				LogUtils.debugByMsg("社宅名" + dto.getHdnSelectedNowShatakuName());
			}

			// 室番号
			if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getRoomNo())) {
				dto.setNowShatakuNo(shatakuList.get(0).getRoomNo());
				LogUtils.debugByMsg("室番号" + dto.getNowShatakuNo());
			}
			// 規格(間取り)
			// 規格があった場合は、貸与規格。それ以外は本来規格
			if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getKikaku())) {
				hdnShatakuKikakuKbn = shatakuList.get(0).getKikaku();// 貸与規格
				dto.setNowShatakuKikaku(hdnShatakuKikakuKbn);
				dto.setNowShatakuKikakuName(shatakuList.get(0).getKikakuName());
				LogUtils.debugByMsg("規格(間取り)" + dto.getNowShatakuKikaku());
				LogUtils.debugByMsg("規格(間取り)名称" + dto.getNowShatakuKikakuName());
			} else {
				if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getOriginalKikaku())) {
					hdnShatakuKikakuKbn = shatakuList.get(0).getOriginalKikaku();// 本来規格
					dto.setNowShatakuKikaku(hdnShatakuKikakuKbn);
					dto.setNowShatakuKikakuName(shatakuList.get(0).getOriginalKikakuName());
					LogUtils.debugByMsg("規格(間取り)" + dto.getNowShatakuKikaku());
					LogUtils.debugByMsg("規格(間取り)名称" + dto.getNowShatakuKikakuName());
				}
			}

			// 面積
			if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getLendMenseki())) {
				dto.setNowShatakuMenseki(shatakuList.get(0).getLendMenseki());
				LogUtils.debugByMsg("現居社宅-面積" + dto.getNowShatakuMenseki());
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
				LogUtils.debugByMsg("現在の保管場所" + dto.getParking1stPlace());
			}

			// 駐車場 １台目 位置番号
			if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getParkingBlock1())) {
				dto.setHdnParking1stNumber(shatakuList.get(0).getParkingBlock1());
				LogUtils.debugByMsg("駐車場 １台目 位置番号" + dto.getHdnParking1stNumber());
			}

			// 駐車場 ２台目 保管場所
			if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getParkingAddress2())) {
				dto.setParking2stPlace(wkPrefName + shatakuList.get(0).getParkingAddress2());
				LogUtils.debugByMsg("現在の保管場所2" + dto.getParking2stPlace());
			}

			// 駐車場 ２台目 位置番号
			if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getParkingBlock2())) {
				dto.setHdnParking2stNumber(shatakuList.get(0).getParkingBlock2());
				LogUtils.debugByMsg("駐車場 2台目 位置番号" + dto.getHdnParking2stNumber());
			}

			// 現在の社宅管理番号
			if (shatakuList.get(0).getShatakuKanriNo() != null) {
				hdnNowShatakuKanriNo = shatakuList.get(0).getShatakuKanriNo();
				dto.setHdnNowShatakuKanriNo(hdnNowShatakuKanriNo);
			}

			// 現在の部屋管理番号
			if (shatakuList.get(0).getShatakuRoomKanriNo() != null) {
				hdnNowShatakuRoomKanriNo = shatakuList.get(0).getShatakuRoomKanriNo();
				dto.setHdnNowShatakuRoomKanriNo(hdnNowShatakuRoomKanriNo);
			}

		}
	}

	/**
	 * 現居住社宅の取得
	 * 
	 * @param shatakuKanriId 社宅管理ID
	 * @param shainNo 社員番号
	 * @param shatakuList 社宅情報のリスト
	 * @return 社宅情報のリスト
	 */
	protected List<Skf2020Sc002GetShatakuInfoExp> getShatakuInfo(long shatakuKanriId, String shainNo,
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
	 * @param dto Skf2020Sc002ChangeDropDownAsyncDto
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
	 * @param shatakuKanriId 社宅管理ID
	 * @param shainNo 社員番号
	 * @param resultBihinItemList 返却備品リスト
	 * @return 返却備品リスト
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
	 * 社宅、備品情報のクリア 空文字を設定する
	 * 
	 * @param dto
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

		// 備品情報のクリア
		dto.setReturnEquipment(CodeConstant.DOUBLE_QUOTATION);
	}

}
