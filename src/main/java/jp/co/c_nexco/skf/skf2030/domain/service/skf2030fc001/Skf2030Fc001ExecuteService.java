/*
 * 
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2030.domain.service.skf2030fc001;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBatchUtils.SkfBatchUtilsGetMultipleTablesUpdateDateExp;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.util.batch.SkfBatchUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2030Fc001BihinKiboShinseiDataImport;
import jp.co.c_nexco.skf.skf2030.domain.dto.skf2030fc001.Skf2030Fc001ExecuteDto;

/**
 * Skf3090Sc005InitService 従業員マスタ登録初期表示処理クラス.
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf2030Fc001ExecuteService extends BaseServiceAbstract<Skf2030Fc001ExecuteDto> {

	// マップのキーはSessionCacheKeyConstantクラスに定義すること！
	private static final String DATA_LINKAGE_KEY_SKF2030FC001 = "keyForUpdate";

	@Autowired
	SkfBatchUtils skfBatchUtils;
	@Autowired
	Skf2030Fc001BihinKiboShinseiDataImport skf2030Fc001;

	/**
	 * 画面初期表示のメイン処理
	 */
	@Override
	public BaseDto index(Skf2030Fc001ExecuteDto dto) throws Exception {

		LogUtils.debugByMsg("Skf2030Fc001-UTTest：テスト実行クラス処理開始");

		// 「null」と入力されている場合NULL値に置き換える
		String companyCd = dto.getSkf2030Fc001CompanyCd();
		String shainNo = dto.getSkf2030Fc001ShainNo();
		String applNo = dto.getSkf2030Fc001ApplNo();
		String applStatus = dto.getSkf2030Fc001ApplStatus();

		long startTime = System.currentTimeMillis();

		/** 退居のデータ連携機能を例とする */
		// menuScopeSessionBeanからオブジェクトを取得する
		Object forUpdateObject = menuScopeSessionBean.get(DATA_LINKAGE_KEY_SKF2030FC001);

		// 取得したオブジェクトを専用メソッドを使ってダウンキャストする
		Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> forUpdateMap = skf2030Fc001
				.forUpdateMapDownCaster(forUpdateObject);

		// ダウンキャストしたMapをデータ連携クラスに格納する
		skf2030Fc001.setUpdateDateForUpdateSQL(forUpdateMap);

		// データ連携機能をキックする
		List<String> resultList = skf2030Fc001.doProc(companyCd, shainNo, applNo, applStatus,
				dto.getSkf2030Fc001UserID(), dto.getSkf2030Fc001PageID());

		// セッションに保持しているデータ連携用のMapを削除する
		menuScopeSessionBean.remove(DATA_LINKAGE_KEY_SKF2030FC001);

		if (resultList != null) {
			// エラーメッセージ出力
			skf2030Fc001.addResultMessageForDataLinkage(dto, resultList);

		}

		if (resultList != null) {

			String errorMessage = "";
			for (int listIndex = 0; listIndex < resultList.size(); listIndex++) {
				if (listIndex == 0) {
					dto.setReturnStatus(resultList.get(0));
				} else {
					if (errorMessage.equals("") == false) {
						errorMessage += "/";
					}
					errorMessage += resultList.get(listIndex);
				}
			}
			dto.setErrorStrings(errorMessage);
			// throw new RuntimeException();
		}

		// try {
		//
		// /** 退居のデータ連携機能を例とする */
		// // menuScopeSessionBeanからオブジェクトを取得する
		// Object forUpdateObject =
		// menuScopeSessionBean.get(DATA_LINKAGE_KEY_SKF2040FC001);
		//
		// // 取得したオブジェクトを専用メソッドを使ってダウンキャストする
		// Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>>
		// forUpdateMap = skf2040Fc001
		// .forUpdateMapDownCaster(forUpdateObject);
		//
		// // ダウンキャストしたMapをデータ連携クラスに格納する
		// skf2040Fc001.setUpdateDateForUpdateSQL(forUpdateMap);
		//
		// // データ連携機能をキックする
		// List<String> resultList = skf2040Fc001.doProc(companyCd, shainNo,
		// applNo, applStatus, dto.getUserID(),
		// dto.getPageID());
		//
		// // セッションに保持しているデータ連携用のMapを削除する
		// menuScopeSessionBean.remove(DATA_LINKAGE_KEY_SKF2040FC001);
		//
		// if (resultList != null) {
		// // エラーメッセージ出力
		// skf2040Fc001.addResultMessageForDataLinkage(dto, resultList);
		//
		// }
		//
		// if (resultList != null) {
		//
		// String errorMessage = "";
		// for (int listIndex = 0; listIndex < resultList.size(); listIndex++) {
		// if (listIndex == 0) {
		// dto.setErrorCodeID(resultList.get(0));
		// } else {
		// if (errorMessage.equals("") == false) {
		// errorMessage += "/";
		// }
		// errorMessage += resultList.get(listIndex);
		// }
		// }
		// dto.setErrorStrings(errorMessage);
		// // throw new RuntimeException();
		// }
		// } catch (Exception e) {
		// dto.setErrorCodeID("何かの理由で異常終了");
		// // stackTrace出力
		// StringWriter sw = new StringWriter();
		// PrintWriter pw = new PrintWriter(sw);
		// e.printStackTrace(pw);
		// LogUtils.debugByMsg(sw.toString());
		// }

		long endTime = System.currentTimeMillis();

		LogUtils.debugByMsg("Skf2030Fc001-UTTest：処理にかかった時間：" + (endTime - startTime) + "ミリ秒");
		LogUtils.debugByMsg("Skf2030Fc001-UTTest：テスト実行クラス処理終了");
		return dto;
	}

}
