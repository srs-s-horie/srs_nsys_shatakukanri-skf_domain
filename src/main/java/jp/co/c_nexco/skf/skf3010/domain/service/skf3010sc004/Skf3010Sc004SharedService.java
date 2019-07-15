/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc004;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc004.Skf3010Sc004GetEmptyRoomCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc004.Skf3010Sc004GetListTableDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc004.Skf3010Sc004GetListTableDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc004.Skf3010Sc004GetParkingCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc004.Skf3010Sc004GetRoomCountExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc004.Skf3010Sc004GetEmptyRoomCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc004.Skf3010Sc004GetListTableDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc004.Skf3010Sc004GetParkingCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc004.Skf3010Sc004GetRoomCountExpRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;

/**
 * Skf3010Sc004SharedService 社宅部屋一覧内共通クラス
 * 
 * @author NEXCOシステムズ
 *
 */

@Service
public class Skf3010Sc004SharedService {

	@Value("${skf3010.skf3010_sc004.max_search_count}")
	private Integer maxGetRecordCount;

	@Autowired
	private SkfDropDownUtils ddlUtils;
	@Autowired
	private Skf3010Sc004GetListTableDataExpRepository skf3010Sc004GetListTableDataExpRepository;
	@Autowired
	private Skf3010Sc004GetParkingCountExpRepository skf3010Sc004GetParkingCountExpRepository;
	@Autowired
	private Skf3010Sc004GetRoomCountExpRepository skf3010Sc004GetRoomCountExpRepository;
	@Autowired
	private Skf3010Sc004GetEmptyRoomCountExpRepository skf3010Sc004GetEmptyRoomCountExpRepository;
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;

	public static List<Skf3010Sc004GetListTableDataExp> resultList;

	private String UNIT_MENSEKI = "㎡";

	/**
	 * ドロップダウンリストに設定するリストを取得する。<br>
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param originalAuse 本来用途
	 * @param auseList 本来用途リスト
	 * @param lendKbn 機関
	 * @param lendList 機関リスト
	 */
	public void getDoropDownList(String originalAuse, List<Map<String, Object>> auseList, String lendKbn,
			List<Map<String, Object>> lendList) {

		boolean isFirstRowEmpty = true;

		// 本来用途リスト
		auseList.clear();
		auseList.addAll(ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_AUSE_KBN, originalAuse,
				isFirstRowEmpty));

		// 機関リスト
		lendList.clear();
		lendList.addAll(ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_LEND_KBN, lendKbn,
				isFirstRowEmpty));

	}

	/**
	 * リストテーブルを取得する。 <br>
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param shatakuKanriNo 社宅管理番号
	 * @param originalAuse 本来用途
	 * @param lendKbn 貸与区分
	 * @param listTableData ※取得リストテーブルデータ
	 * @return 取得データのレコードカウント
	 */
	public int getListTableData(Long shatakuKanriNo, String originalAuse, String lendKbn,
			List<Map<String, Object>> listTableData) {

		LogUtils.debugByMsg("リストテーブルデータ取得処理開始");
		LogUtils.debugByMsg("引数から取得した値：" + "社宅管理番号：" + shatakuKanriNo);

		String activeYearMonth = "";
		// アクティブ年月を取得するGetSystemProcessNenGetsu
		activeYearMonth = skfBaseBusinessLogicUtils.GetSystemProcessNenGetsu();

		// リストテーブルに格納するデータを取得する
		int resultCount = 0;
		List<Skf3010Sc004GetListTableDataExp> resultListTableData = new ArrayList<Skf3010Sc004GetListTableDataExp>();
		Skf3010Sc004GetListTableDataExpParameter param = new Skf3010Sc004GetListTableDataExpParameter();
		param.setShatakuKanriNo(shatakuKanriNo);
		param.setActiveYearMonth(activeYearMonth);
		if (originalAuse != null && !originalAuse.isEmpty()) {
			LogUtils.debugByMsg("originalAuse:" + originalAuse);
			param.setOriginalAuse(originalAuse);
		}
		if (lendKbn != null && !lendKbn.isEmpty()) {
			LogUtils.debugByMsg("lendKbn:" + lendKbn);
			param.setLendKbn(lendKbn);
		}
		resultListTableData = skf3010Sc004GetListTableDataExpRepository.getListTableData(param);

		// 取得レコード数を設定
		resultCount = resultListTableData.size();

		// 取得データレコード数判定
		if (resultCount == 0 || resultCount > maxGetRecordCount) {
			// 取得データレコード数が0件または3000件より多い場合、何もせず処理終了
			return resultCount;
		}

		// リストテーブルに出力するリストを取得する
		listTableData.clear();
		listTableData.addAll(getListTableDataViewColumn(resultListTableData));

		return resultCount;

	}

	/**
	 * リストテーブルに出力するリストを取得する。
	 * 
	 * @param originList
	 * @return リストテーブルに出力するリスト
	 */
	private List<Map<String, Object>> getListTableDataViewColumn(List<Skf3010Sc004GetListTableDataExp> originList) {

		List<Map<String, Object>> setViewList = new ArrayList<Map<String, Object>>();

		// 本来用途汎用コード取得
		Map<String, String> genericCodeMapAuse = new HashMap<String, String>();
		genericCodeMapAuse = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_AUSE_KBN);
		// 本来規格汎用コード取得
		Map<String, String> genericCodeMapKikaku = new HashMap<String, String>();
		genericCodeMapKikaku = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_KIKAKU_KBN);
		// 貸与区分汎用コード取得
		Map<String, String> genericCodeMapLend = new HashMap<String, String>();
		genericCodeMapLend = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_LEND_KBN);

		for (int i = 0; i < originList.size(); i++) {
			String originalAuse = "";
			String originalKikaku = "";
			String lendKbn = "";
			String menseki = "";

			Skf3010Sc004GetListTableDataExp tmpData = originList.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();

			tmpMap.put("col1", tmpData.getRoomNo());
			// 「本来用途」の設定
			// 本来用途をコードから汎用コードに変更
			if (tmpData.getOriginalAuse() != null) {
				originalAuse = genericCodeMapAuse.get(tmpData.getOriginalAuse());
			}
			tmpMap.put("col2", originalAuse);

			// 「本来規格」の設定
			// 本来規格をコードから汎用コードに変更
			if (tmpData.getOriginalKikaku() != null) {
				originalKikaku = genericCodeMapKikaku.get(tmpData.getOriginalKikaku());
			}
			tmpMap.put("col3", originalKikaku);

			// 「本来延面積」の単位の設定
			menseki = tmpData.getOriginalMenseki().toString() + UNIT_MENSEKI;
			tmpMap.put("col4", menseki);

			// 「貸与区分」の設定
			// 貸与区分をコードから汎用コードに変更
			if (tmpData.getLendKbn() != null) {
				lendKbn = genericCodeMapLend.get(tmpData.getLendKbn());
			}
			tmpMap.put("col5", lendKbn);
			tmpMap.put("col6", tmpData.getName());
			tmpMap.put("col7", tmpData.getBiko());
			tmpMap.put("col8", "");
			tmpMap.put("col9", tmpData.getShatakuRoomKanriNo());

			setViewList.add(tmpMap);
		}

		return setViewList;
	}

	/**
	 * 駐車場総数を取得する。
	 * 
	 * @param shatakuKanriNo
	 * @return
	 */
	public int GetParkingCount(Long shatakuKanriNo) {

		LogUtils.debugByMsg("駐車場総数取得処理開始");
		LogUtils.debugByMsg("引数から取得した値：" + "社宅管理番号：" + shatakuKanriNo);

		// 駐車場総数を取得する
		int resultCount = 0;
		Skf3010Sc004GetParkingCountExpParameter param = new Skf3010Sc004GetParkingCountExpParameter();
		param.setShatakuKanriNo(shatakuKanriNo);

		resultCount = skf3010Sc004GetParkingCountExpRepository.getParkingCount(param);
		LogUtils.debugByMsg("resultCount：" + resultCount);

		return resultCount;
	}

	/**
	 * 社宅部屋総数を取得する。
	 * 
	 * @param shatakuKanriNo
	 * @return
	 */
	public int GetRoomCount(Long shatakuKanriNo) {

		LogUtils.debugByMsg("社宅部屋総数取得処理開始");
		LogUtils.debugByMsg("引数から取得した値：" + "社宅管理番号：" + shatakuKanriNo);

		// 社宅部屋総数を取得する
		int resultCount = 0;
		Skf3010Sc004GetRoomCountExpParameter param = new Skf3010Sc004GetRoomCountExpParameter();
		param.setShatakuKanriNo(shatakuKanriNo);

		resultCount = skf3010Sc004GetRoomCountExpRepository.getRoomCount(param);
		LogUtils.debugByMsg("resultCount：" + resultCount);

		return resultCount;
	}

	/**
	 * 空き社宅部屋総数を取得する。
	 * 
	 * @param shatakuKanriNo
	 * @return
	 */
	public int GetEmptyRoomCount(Long shatakuKanriNo) {

		LogUtils.debugByMsg("空き社宅部屋総数取得処理開始");
		LogUtils.debugByMsg("引数から取得した値：" + "社宅管理番号：" + shatakuKanriNo);

		// 空き社宅部屋総数を取得する
		int resultCount = 0;
		Skf3010Sc004GetEmptyRoomCountExpParameter param = new Skf3010Sc004GetEmptyRoomCountExpParameter();
		param.setShatakuKanriNo(shatakuKanriNo);

		resultCount = skf3010Sc004GetEmptyRoomCountExpRepository.getEmptyRoomCount(param);
		LogUtils.debugByMsg("resultCount：" + resultCount);
		return resultCount;
	}
}
