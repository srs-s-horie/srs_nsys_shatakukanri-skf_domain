/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3050.domain.service.skf3050sc002;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.common.utils.PropertyUtils;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3050.domain.dto.skf3050sc002.Skf3050Sc002InitDto;

/**
 * Skf3050Sc002InitService 月次運用管理初期表示
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3050Sc002InitService extends SkfServiceAbstract<Skf3050Sc002InitDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3050Sc002SharedService skf3050Sc002SharedService;
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;

	private static final String TAISHOUNENDO_NENDO = "年度　";
	private static final String LIST_CNT_KEY = "skf3050.skf3050sc002.list_max_cnt";

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
	public Skf3050Sc002InitDto index(Skf3050Sc002InitDto initDto) throws Exception {

		initDto.setPageTitleKey(MessageIdConstant.SKF3050_SC002_TITLE);

		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, FunctionIdConstant.SKF3050_SC002);

		initDto = init(initDto);

		return initDto;
	}

	/**
	 * 初期表示設定を行う。
	 * 
	 * @param initDto
	 *            初期表示Dto
	 * @return 初期表示Dto
	 */
	private Skf3050Sc002InitDto init(Skf3050Sc002InitDto initDto) {

		String sysDateYyyymm = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu();
		String standardYear = skf3050Sc002SharedService.getStandardYear(sysDateYyyymm);

		List<Map<String, Object>> dropDownList = createTaishouNendoDropDownList(standardYear);
		initDto.setDropDownList(dropDownList);

		if (dropDownList.size() != 0) {
			String selectTaisyonendo = (String) dropDownList.get(0).get("value");
			initDto.setHdnSelectedTaisyonendo(selectTaisyonendo);
		} else {
			LogUtils.infoByMsg("init, 対象年度がありません。");
		}

		List<Map<String, Object>> gridList = skf3050Sc002SharedService.createGetsujiGrid(standardYear);
		initDto.setGetujiGrid(gridList);

		skf3050Sc002SharedService.getJikkoushijiHighlightData(initDto, sysDateYyyymm);
		skf3050Sc002SharedService.setBtnMsg(initDto);
		skf3050Sc002SharedService.changeButtonStatus(initDto);

		return initDto;
	}

	/**
	 * 対象年度ドロップダウンリストを作成する。
	 * 
	 * @param sysDateYyyymm
	 *            システム日付
	 * @return 対象年度ドロップダウンリスト
	 */
	private List<Map<String, Object>> createTaishouNendoDropDownList(String sysDateYyyymm) {

		List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();

		if (NfwStringUtils.isEmpty(sysDateYyyymm) || sysDateYyyymm.length() < Skf3050Sc002SharedService.NENGETSU_LEN) {
			return rtnList;
		}

		int standardYyyy = Integer.parseInt(sysDateYyyymm);

		int listMaxCnt = Integer.parseInt(PropertyUtils.getValue(LIST_CNT_KEY));

		for (int i = 0; i < listMaxCnt; i++) {
			Map<String, Object> dropDownMap = new HashMap<String, Object>();

			String addYyyy = String.valueOf(standardYyyy - i);
			dropDownMap.put("value", addYyyy);
			dropDownMap.put("label", addYyyy + TAISHOUNENDO_NENDO);
			rtnList.add(dropDownMap);
		}

		return rtnList;
	}

//	/**
//	 * システム処理年月を取得する。
//	 * 
//	 * @return システム処理年月
//	 */
//	private String getSystemProcessNenGetsu() {
//
//		String rtn = "";
//		String strNenGetsuMax = skfBaseBusinessLogicUtils.getMaxNenGetsu();
//		String strNenGetsuMin = skfBaseBusinessLogicUtils.getMinNenGetsu();
//
//		if (!NfwStringUtils.isEmpty(strNenGetsuMax) && !NfwStringUtils.isEmpty(strNenGetsuMin)) {
//			String nextNenGetsuMax = skfDateFormatUtils.addYearMonth(strNenGetsuMax, Skf3050Sc002SharedService.MONTH_1);
//
//			if (nextNenGetsuMax.equals(strNenGetsuMin)) {
//				rtn = nextNenGetsuMax;
//			}
//		}
//
//		return rtn;
//	}

}
