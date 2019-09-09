/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.service.skf3020sc003;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.bean.ApplicationScopeBean;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3020.domain.dto.common.Skf302010SaveDto;
import jp.co.c_nexco.skf.skf3020.domain.dto.skf3020sc003.Skf3020Sc003InitDto;

/**
 * Skf3020Sc003 転任者調書確認初期表示処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf3020Sc003InitService extends BaseServiceAbstract<Skf3020Sc003InitDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private ApplicationScopeBean bean;

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
	public Skf3020Sc003InitDto index(Skf3020Sc003InitDto initDto) throws Exception {
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, initDto.getPageId());

		initDto.setPageTitleKey(MessageIdConstant.SKF3020_SC003_TITLE);
		// 画面項目設定
		initDto = setControlStatus(initDto);

		return initDto;
	}

	/**
	 * 画面項目設定
	 * 
	 * @param initDto
	 *            初期表示DTO
	 * @return 初期表示DTO
	 */
	@SuppressWarnings("unchecked")
	private Skf3020Sc003InitDto setControlStatus(Skf3020Sc003InitDto initDto) {
		// 転任者情報テーブル
		List<Map<String, Object>> tenninshaChoshoDataTable = new ArrayList<Map<String, Object>>();
		// セッション情報取得
		List<Skf302010SaveDto> tenninshaRegistInfo = (List<Skf302010SaveDto>) bean
				.get(SessionCacheKeyConstant.TENNINSHAREGIST_INFO);

		if (tenninshaRegistInfo.size() != 0) {
			// 画面表示内容の編集
			setSeissionDataToTenninshaChoshoDataTable(tenninshaChoshoDataTable, tenninshaRegistInfo);
		}

		initDto.setTenninshaChoshoDataTable(tenninshaChoshoDataTable);

		return initDto;
	}

	/**
	 * 転任者情報のlistTableにセッション情報を設定する。
	 * 
	 * @param tenninshaChoshoDataTable
	 *            転任者情報のlistTable
	 * @param sessionDataList
	 *            セッション情報リスト
	 * @return 転任者情報のlistTable
	 */
	private List<Map<String, Object>> setSeissionDataToTenninshaChoshoDataTable(
			List<Map<String, Object>> tenninshaChoshoDataTable, List<Skf302010SaveDto> sessionDataList) {

		for (int i = 0; i < sessionDataList.size(); i++) {
			Skf302010SaveDto sessionData = sessionDataList.get(i); // セッション情報
			Map<String, Object> targetMap = new HashMap<String, Object>(); // データ設定対象map

			targetMap.put(Skf3020Sc003SharedService.SHAIN_NO_COL, sessionData.getShainNo()); // 社員番号
			targetMap.put(Skf3020Sc003SharedService.NAME_COL, sessionData.getName()); // 社員氏名
			targetMap.put(Skf3020Sc003SharedService.TOKYU_COL, sessionData.getTokyu()); // 等級
			targetMap.put(Skf3020Sc003SharedService.AGE_COL, sessionData.getAge()); // 年齢
			targetMap.put(Skf3020Sc003SharedService.NEW_AFFILIATION_COL, sessionData.getNewAffiliation()); // 新所属
			targetMap.put(Skf3020Sc003SharedService.NOW_AFFILIATION_COL, sessionData.getNowAffiliation()); // 現所属
			targetMap.put(Skf3020Sc003SharedService.BIKO_COL, sessionData.getBiko()); // 備考
			targetMap.put(Skf3020Sc003SharedService.ERR_COL, ""); // エラー用
			tenninshaChoshoDataTable.add(targetMap);
		}

		return tenninshaChoshoDataTable;
	}

}
