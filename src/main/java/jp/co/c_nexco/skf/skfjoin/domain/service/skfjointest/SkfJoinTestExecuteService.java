/*
 * 
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skfjoin.domain.service.skfjointest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.util.batch.SkfBatchUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2010Fc001ShatakuKanriDaichoShatakuDataUpdate;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2010Fc002ShatakuKanriDaichoBihinDataUpdate;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2030Fc001BihinKiboShinseiDataImport;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2050Fc001BihinHenkyakuSinseiDataImport;
import jp.co.c_nexco.skf.skfjoin.domain.dto.skfjointest.SkfJoinTestExecuteDto;

/**
 * UpdateShatakuKanriDaichoShatakuDataExecuteService 社宅管理台帳データ登録（社宅情報）更新テストクラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class SkfJoinTestExecuteService extends BaseServiceAbstract<SkfJoinTestExecuteDto> {

	@Autowired
	Skf2010Fc001ShatakuKanriDaichoShatakuDataUpdate skf2010Fc001ShatakuKanriDaichoShatakuDataUpdate;
	@Autowired
	Skf2010Fc002ShatakuKanriDaichoBihinDataUpdate skf2010Fc002ShatakuKanriDaichoBihinDataUpdate;
	@Autowired
	Skf2030Fc001BihinKiboShinseiDataImport skf2030Fc001BihinKiboShinseiDataImport;
	@Autowired
	Skf2050Fc001BihinHenkyakuSinseiDataImport skf2050Fc001BihinHenkyakuSinseiDataImport;
	@Autowired
	private SkfBatchUtils skfBatchUtils;

	private static final String DATA_LINKAGE_KEY_COMBATCH1 = "mapKeyOfComBatch1";

	/**
	 * 画面初期表示のメイン処理
	 */
	@Override
	public BaseDto index(SkfJoinTestExecuteDto dto) throws Exception {

		LogUtils.debugByMsg("updateShatakuKanriDaichoShatakuData-UTTest：テスト実行クラス処理開始");

		// 処理結果リスト初期化
		List<String> returnList = null;
		// エラーリストインデックスの受け皿
		int exceptionParameter = 0;

		/** 型変換用定義 */
		Long forInputTeijiNo = null;

		/** Skf2010Fc001社宅管理台帳データ登録（社宅情報）呼び出し */
		// 提示番号型変換
		if (NfwStringUtils.isNotEmpty(dto.getSkf2010Fc001TeijiNo())) {
			forInputTeijiNo = Long.valueOf(dto.getSkf2010Fc001TeijiNo());
		}
		// Skf2010Fc001社宅管理台帳データ登録（社宅情報）呼び出し
		returnList = skf2010Fc001ShatakuKanriDaichoShatakuDataUpdate.doProc(dto.getSkf2010Fc001CompanyCd(),
				forInputTeijiNo, dto.getSkf2010Fc001UserID(), dto.getSkf2010Fc001PageID());
		exceptionParameter = 0;

		/** 型変換定義初期化 */
		forInputTeijiNo = null;

		if (returnList == null) {
			// 一つ前のデータ連携処理が成功したら次のデータ連携
			/** Skf2010Fc002社宅管理台帳データ登録（備品情報）呼び出し */
			// 提示番号型変換
			if (NfwStringUtils.isNotEmpty(dto.getSkf2010Fc002TeijiNo())) {
				forInputTeijiNo = Long.valueOf(dto.getSkf2010Fc002TeijiNo());
			}
			// Skf2010Fc001社宅管理台帳データ登録（社宅情報）呼び出し
			returnList = skf2010Fc002ShatakuKanriDaichoBihinDataUpdate.doProc(dto.getSkf2010Fc002CompanyCd(),
					forInputTeijiNo, dto.getSkf2010Fc002UserID(), dto.getSkf2010Fc002PageID());
			exceptionParameter = 0;

		}

		if (returnList == null) {
			// 一つ前のデータ連携処理が成功したら次のデータ連携
			/** Skf2030Fc001備品希望申請データ連携呼び出し */
			returnList = skf2030Fc001BihinKiboShinseiDataImport.doProc(dto.getSkf2030Fc001CompanyCd(),
					dto.getSkf2030Fc001ShainNo(), dto.getSkf2030Fc001ApplNo(), dto.getSkf2030Fc001ApplStatus(),
					dto.getSkf2030Fc001UserID(), dto.getSkf2030Fc001PageID());
			exceptionParameter = 0;

		}

		if (returnList == null) {
			// 一つ前のデータ連携処理が成功したら次のデータ連携
			/** Skf2050Fc001備品返却確認データ連携呼び出し */
			returnList = skf2050Fc001BihinHenkyakuSinseiDataImport.doProc(dto.getSkf2050Fc001CompanyCd(),
					dto.getSkf2050Fc001ShainNo(), dto.getSkf2050Fc001ApplNo(), dto.getSkf2050Fc001ApplStatus(),
					dto.getSkf2050Fc001UserID(), dto.getSkf2050Fc001PageID());
			exceptionParameter = 0;

		}

		if (returnList == null) {
			dto.setReturnStatus("0");
		} else {
			/** エラーリストを捌く */
			String errorMessage = "";
			for (int listIndex = 0; listIndex < returnList.size(); listIndex++) {
				if (listIndex == exceptionParameter) {
					dto.setReturnStatus(returnList.get(exceptionParameter));
				} else {
					if (errorMessage.equals("") == false) {
						errorMessage += "/";
					}
					errorMessage += returnList.get(listIndex);
				}
			}
			dto.setErrorStrings(errorMessage);
		}

		LogUtils.debugByMsg("updateShatakuKanriDaichoShatakuData-UTTest：テスト実行クラス処理終了");
		return dto;
	}

}
