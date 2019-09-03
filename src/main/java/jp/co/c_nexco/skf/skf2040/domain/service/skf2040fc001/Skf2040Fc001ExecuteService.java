/*
 * 
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.service.skf2040fc001;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2040Fc001TaikyoTodokeDataImport;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040fc001.Skf2040Fc001ExecuteDto;

/**
 * Skf3090Sc005InitService 従業員マスタ登録初期表示処理クラス.
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf2040Fc001ExecuteService extends BaseServiceAbstract<Skf2040Fc001ExecuteDto> {

	@Autowired
	Skf2040Fc001TaikyoTodokeDataImport skf2040Fc001;

	/**
	 * 画面初期表示のメイン処理
	 */
	@Override
	public BaseDto index(Skf2040Fc001ExecuteDto dto) throws Exception {

		LogUtils.debugByMsg("Skf2040Fc001-UTTest：テスト実行クラス処理開始");

		// 「null」と入力されている場合NULL値に置き換える
		String companyCd = dto.getCompanyCd();
		String shainNo = dto.getShainNo();
		String applNo = dto.getApplNo();
		String applStatus = dto.getApplStatus();
		if (companyCd != null && companyCd.equals("null")) {
			companyCd = null;
		}
		if (shainNo != null && shainNo.equals("null")) {
			shainNo = null;
		}
		if (applNo != null && applNo.equals("null")) {
			applNo = null;
		}
		if (applStatus != null && applStatus.equals("null")) {
			applStatus = null;
		}

		try {
			List<String> resultList = skf2040Fc001.doProc(companyCd, shainNo, applNo, applStatus);

			if (resultList != null) {
				String errorMessage = "";
				for (int listIndex = 0; listIndex < resultList.size(); listIndex++) {
					if (listIndex == Skf2040Fc001TaikyoTodokeDataImport.INDEX_OF_EXCEPTION_MASSAGE_ID) {
						dto.setErrorCodeID(
								resultList.get(Skf2040Fc001TaikyoTodokeDataImport.INDEX_OF_EXCEPTION_MASSAGE_ID));
					} else {
						if (errorMessage.equals("") == false) {
							errorMessage += "/";
						}
						errorMessage += resultList.get(listIndex);
					}
				}
				dto.setErrorStrings(errorMessage);
			}
		} catch (Exception e) {
			dto.setErrorCodeID("何かの理由で異常終了");
			// stackTrace出力
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			LogUtils.debugByMsg(sw.toString());
		}

		LogUtils.debugByMsg("Skf2040Fc001-UTTest：テスト実行クラス処理終了");
		return dto;
	}

}
