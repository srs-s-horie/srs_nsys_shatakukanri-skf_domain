/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.service.skf3020sc005;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3020Sc005.Skf3020Sc005GetTenninshaShatakuInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3020Sc005.Skf3020Sc005GetTenninshaShatakuInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3020Sc005.Skf3020Sc005GetTenninshaShatakuInfoExpRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.intra_mart.common.platform.log.Logger;


/**
 * Skf3020Sc005SharedService 転任者登録内共通クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3020Sc005SharedService {

	@Autowired
	private Skf3020Sc005GetTenninshaShatakuInfoExpRepository skf3020Sc005GetTenninshaShatakuInfoExpRepository;

	/** ロガー。 */
	private static Logger logger = LogUtils.getLogger(SkfFileOutputUtils.class);

	/**
	 * 転任者情報を取得する。
	 * パラメータの社員番号と一致する転任者情報を取得する。
	 * 
	 * @param shainNo 社員番号
	 * @return 転任者情報
	 * @throws ParseException 
	 */
	public Map<String, Object> getTenninshaShatakuInfo(String shainNo) throws ParseException {

		// デバッグログ
		logger.debug("getTenninshaShatakuInfo, 社員番号:" + shainNo);
		// リストテーブルに格納するデータを取得する
		List<Skf3020Sc005GetTenninshaShatakuInfoExp> resultListTableData = new ArrayList<Skf3020Sc005GetTenninshaShatakuInfoExp>();
		Map<String, Object> resultTableData = null;
		Skf3020Sc005GetTenninshaShatakuInfoExpParameter param = new Skf3020Sc005GetTenninshaShatakuInfoExpParameter();
		// SQLパラメータ設定
		param.setShainNo(shainNo);
		resultListTableData = skf3020Sc005GetTenninshaShatakuInfoExpRepository.getTenninshaShatakuInfo(param);
	
		// 取得データレコード数判定
		if (resultListTableData.size() == 0) {
			// 取得データレコード数が0件の場合、何もせず処理終了
			return resultTableData;
		}
		resultTableData = getTableDataViewColumn(resultListTableData.get(0));
		resultListTableData.clear();
	
		return resultTableData;
	}

	/**
	 * 転任者情報を取得する。
	 * DBからの取得値をMapで返却する。
	 * 
	 * @param originList
	 * @return 転任者情報
	 * @throws ParseException
	 */
	private Map<String, Object> getTableDataViewColumn(Skf3020Sc005GetTenninshaShatakuInfoExp getData) throws ParseException {

		Map<String, Object> tmpMap = new HashMap<String, Object>();

		// 社員番号
		tmpMap.put("shainNo", getData.getShainNo());
		// 社員名
		tmpMap.put("shainName", getData.getShainName().trim());
		// 等級
		tmpMap.put("tokyu", getData.getTokyu());
		// 年齢
		tmpMap.put("age", getData.getAge());
		// 現所属
		tmpMap.put("nowAffiliation", getData.getNowAffiliation());
		// 新所属
		tmpMap.put("newAffiliation", getData.getNewAffiliation());
		// 備考
		tmpMap.put("biko", getData.getBiko());
		// 社員番号変更対象区分判定
		String shainNoHenkoKbn = getData.getShainNoHenkoKbn();
		if (shainNoHenkoKbn != null && !shainNoHenkoKbn.equals("0")) {
			// 仮社員番号設定(社員番号の変更が必要)
			tmpMap.put("chkShainNoHenkoKbn", true);
		} else {
			// 仮社員番号設定(社員番号の変更が必要)
			tmpMap.put("chkShainNoHenkoKbn", false);
		}
		tmpMap.put("shainNoHenkoKbn", getData.getShainNoHenkoKbn());
		// 入退居予定作成区分
		tmpMap.put("hdnNyutaikyoYoteiKbn", getData.getNyutaikyoYoteiKbn());
		// 更新日時(転任者)
		tmpMap.put("hdnUpdateDateTenninsha", getData.getUpdateDateTenninsha());
		// 更新日時(社員)
		tmpMap.put("hdnUpdateDateShain", getData.getUpdateDateShain());
		return tmpMap;
	}
}
