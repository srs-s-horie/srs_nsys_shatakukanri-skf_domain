/*
 * 
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010fc001;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBatchUtils.SkfBatchUtilsGetMultipleTablesUpdateDateExp;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.util.batch.SkfBatchUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2010Fc001ShatakuKanriDaichoShatakuDataUpdate;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010fc001.Skf2010Fc001ExecuteDto;

/**
 * Skf2010Fc001ExecuteService
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf2010Fc001ExecuteService extends BaseServiceAbstract<Skf2010Fc001ExecuteDto> {

	@Autowired
	Skf2010Fc001ShatakuKanriDaichoShatakuDataUpdate skf2010Fc001ShatakuKanriDaichoShatakuDataUpdate;
	@Autowired
	private SkfBatchUtils skfBatchUtils;

	/**
	 * 画面初期表示のメイン処理
	 */
	@Override
	public BaseDto index(Skf2010Fc001ExecuteDto dto) throws Exception {

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
		/** Skf2010Fc001社宅管理台帳データ登録（社宅情報）呼び出し */
		// 排他制御用Map取得
		Object forUpdateObject = menuScopeSessionBean.get(Skf2010Fc001InitService.DATA_LINKAGE_KEY_SKF2010FC001);
		// ダウンキャスト
		Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> forUpdateMap = skf2010Fc001ShatakuKanriDaichoShatakuDataUpdate
				.forUpdateMapDownCaster(forUpdateObject);
		// データ連携に格納
		skf2010Fc001ShatakuKanriDaichoShatakuDataUpdate.setUpdateDateForUpdateSQL(forUpdateMap);

		returnList = skf2010Fc001ShatakuKanriDaichoShatakuDataUpdate.doProc(dto.getSkf2010Fc001CompanyCd(),
				forInputTeijiNo, dto.getSkf2010Fc001UserID(), dto.getSkf2010Fc001PageID());
		exceptionParameter = 0;

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
