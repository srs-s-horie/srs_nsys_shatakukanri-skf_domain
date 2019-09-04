/*
 * 
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skfbatch.domain.service.UpdateShatakuKanriDaichoShatakuData;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.util.batch.SkfBatchBusinessLogicUtils;
import jp.co.c_nexco.skf.skfbatch.domain.dto.UpdateShatakuKanriDaichoShatakuData.UpdateShatakuKanriDaichoShatakuDataExecuteDto;

/**
 * UpdateShatakuKanriDaichoShatakuDataExecuteService 社宅管理台帳データ登録（社宅情報）更新テストクラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class UpdateShatakuKanriDaichoShatakuDataExecuteService
		extends BaseServiceAbstract<UpdateShatakuKanriDaichoShatakuDataExecuteDto> {

	@Autowired
	SkfBatchBusinessLogicUtils skfBatchBusinessLogicUtils;

	/**
	 * 画面初期表示のメイン処理
	 */
	@Override
	public BaseDto index(UpdateShatakuKanriDaichoShatakuDataExecuteDto dto) throws Exception {

		LogUtils.debugByMsg("updateShatakuKanriDaichoShatakuData-UTTest：テスト実行クラス処理開始");

		// 何も入力されてなければnullに置き換える
		Long teijiNo = null;
		String tmpTeijiNo = dto.getTeijiNo();
		if (NfwStringUtils.isNotEmpty(tmpTeijiNo)) {
			teijiNo = Long.parseLong(tmpTeijiNo);
		}

		String yearMonth = dto.getYearMonth();
		if (NfwStringUtils.isEmpty(yearMonth))
			yearMonth = null;

		String returnStatus = dto.getReturnStatus();
		if (NfwStringUtils.isEmpty(returnStatus))
			returnStatus = null;

		try {
			returnStatus = String
					.valueOf(skfBatchBusinessLogicUtils.updateShatakuKanriDaichoShatakuData(teijiNo, yearMonth));

		} catch (Exception e) {
			returnStatus = "何かの理由で異常終了";
			// stackTrace出力
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			LogUtils.debugByMsg(sw.toString());
		}

		dto.setReturnStatus(returnStatus);

		LogUtils.debugByMsg("updateShatakuKanriDaichoShatakuData-UTTest：テスト実行クラス処理終了");
		return dto;
	}

}
