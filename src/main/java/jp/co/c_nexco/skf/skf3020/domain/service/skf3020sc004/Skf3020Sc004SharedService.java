/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.service.skf3020sc004;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3020Sc004.Skf3020Sc004GetTenninshaInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3020Sc004.Skf3020Sc004GetTenninshaInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3020Sc004.Skf3020Sc004GetTenninshaInfoExpRepository;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;

/**
 * Skf3020Sc004SharedService 転任者一覧内共通クラス
 * 
 * @author NEXCOシステムズ
 *
 */

@Service
public class Skf3020Sc004SharedService {

	@Autowired
	private SkfDropDownUtils ddlUtils;

	@Autowired
	private Skf3020Sc004GetTenninshaInfoExpRepository skf3020Sc004GetTenninshaInfoExpRepository;

	/**
	 * ドロップダウンリストに設定するリストを取得する。<br>
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * 
	 */
	public void getDoropDownList(String genShatakuKubun, List<Map<String, Object>> genShatakuKubunList,
			String nyutaikyoYoteiSakuseiKubun, List<Map<String, Object>> yoteiSakuseiList) {

		boolean isFirstRowEmpty = true;

		// // 現社宅区分ドロップダウンリストの設定
		genShatakuKubunList.clear();
		genShatakuKubunList.addAll(ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_NOW_SHATAKU_KBN,
				genShatakuKubun, isFirstRowEmpty));

		// 入退居予定作成区分ドロップダウンリストの設定
		yoteiSakuseiList.clear();
		yoteiSakuseiList.addAll(ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_NYUTAIKYO_YOTEI_KBN,
				nyutaikyoYoteiSakuseiKubun, isFirstRowEmpty));

	}

	public int getListTableData(String shainNo, String shainName, String nyukyo, String taikyo, String henko,
			String genShatakuKubun, String genShozoku, String shinShozoku, String nyutaikyoYoteiSakuseiKubun,
			String biko, List<Map<String, Object>> listTableData) {

		// リストテーブルに格納するデータを取得する
		int resultCount = 0;
		List<Skf3020Sc004GetTenninshaInfoExp> resultListTableData = new ArrayList<Skf3020Sc004GetTenninshaInfoExp>();
		Skf3020Sc004GetTenninshaInfoExpParameter param = new Skf3020Sc004GetTenninshaInfoExpParameter();
		param.setShainNo(shainNo);
		param.setShainName(shainName);
		param.setNyukyo(nyukyo);
		param.setTaikyo(taikyo);
		param.setHenko(henko);
		param.setGenShatakuKubun(genShatakuKubun);
		param.setGenShozoku(genShozoku);
		param.setShinShozoku(shinShozoku);
		param.setNyutaikyoYoteiSakuseiKubun(nyutaikyoYoteiSakuseiKubun);
		param.setBiko(biko);
		resultListTableData = skf3020Sc004GetTenninshaInfoExpRepository.getTenninshaInfo(param);

		// 取得レコード数を設定
		resultCount = resultListTableData.size();

		// 取得データレコード数判定
		if (resultCount == 0) {
			// 取得データレコード数が0件の場合、何もせず処理終了
			return resultCount;
		}

		// リストテーブルに出力するリストを取得する
		listTableData.clear();
		// listTableData.addAll(getListTableDataViewColumn(resultListTableData));

		return resultCount;

	}

	// public FlexGridDefineInfo
	// getMainGridData(List<Skf3020Sc004GetTenninshaInfoExp> coList) throws
	// Exception {
	// FlexGridDefineInfo fgi = new FlexGridDefineInfo();
	// fgi.setColumnsDefine(getColumnsDefine());
	// fgi.setInputComponentDefine(getInputComponentDefine());
	// fgi.setDetailLink(getDetailLink());
	// fgi.setDataSource(coList);
	// fgi.setReadonly(true);
	// String pageSize =
	// PropertyUtils.getValue(PropertyKeyConstant.DEFAULT_GRID_PAGE_SIZE);
	// fgi.setPageSize(Integer.parseInt(pageSize));
	// String height =
	// PropertyUtils.getValue("ptp.PtponleScListEdit.grid.Height");
	// fgi.setHeight(Integer.parseInt(height));
	// // fgi.setPopupLink("ptp/PtponleScListEditPopup/init");
	// return fgi;
	// }
	//
	// private List<FlexGridColumnDefineInfo> getColumnsDefine() {
	// FlexGridHeaderDefineInfo result = new
	// FlexGridHeaderDefineInfo(SelectType.MultiSelect);
	// result.add(new FlexGridColumnDefineInfo("sendReserveNo", "送信予約番号", 130,
	// DataType.String));
	// result.add(new FlexGridColumnDefineInfo("linkTimingControlDivision",
	// "連携タイミング制御区分", 190, DataType.Boolean));
	// result.add(new FlexGridColumnDefineInfo("buttonHeader", "ボタン", 70));
	// return result;
	// }
	//
	// private static Map<String, FlexGridInputDefineInfo>
	// getInputComponentDefine() {
	// Map<String, FlexGridInputDefineInfo> map = new HashMap<String,
	// FlexGridInputDefineInfo>();
	// FlexGridButtonDefineInfo buttonDefineInfo = new
	// FlexGridButtonDefineInfo("参照", false);
	// map.put("buttonHeader", buttonDefineInfo);
	// return map;
	// }
	//
	// private static String getDetailLink() {
	// StringBuilder sb = new StringBuilder();
	// sb.append("{\"1\":['script','doLinkAction']}");
	// return sb.toString();
	// }

}
