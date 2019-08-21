/*
 * 
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc999;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2020Fc001NyukyoKiboSinseiDataImport;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc999.Skf3090Sc999ExecuteDto;

/**
 * Skf3090Sc005InitService 従業員マスタ登録初期表示処理クラス.
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3090Sc999ExecuteService extends BaseServiceAbstract<Skf3090Sc999ExecuteDto> {

	@Autowired
	Skf2020Fc001NyukyoKiboSinseiDataImport b2001;

	/**
	 * 画面初期表示のメイン処理
	 */
	@Override
	public BaseDto index(Skf3090Sc999ExecuteDto dto) throws Exception {

		LogUtils.debugByMsg("B001-UTTest：テスト実行クラス処理開始");

		// 「null」と入力されている場合NULL値に置き換える
		String companyCd = dto.getCompanyCd();
		String shainNo = dto.getShainNo();
		String applNoNyukyo = dto.getApplNoNyukyo();
		String applNoTaikyo = dto.getApplNoTaikyo();
		String applStatus = dto.getApplStatus();
		if (companyCd != null && companyCd.equals("null")) {
			companyCd = null;
		}
		if (shainNo != null && shainNo.equals("null")) {
			shainNo = null;
		}
		if (applNoNyukyo != null && applNoNyukyo.equals("null")) {
			applNoNyukyo = null;
		}
		if (applNoTaikyo != null && applNoTaikyo.equals("null")) {
			applNoTaikyo = null;
		}
		if (applStatus != null && applStatus.equals("null")) {
			applStatus = null;
		}

		try {
			List<String> resultList = b2001.doProc(companyCd, shainNo, applNoNyukyo, applNoTaikyo, applStatus);

			if (resultList != null) {
				String errorMessage = "";
				for (int listIndex = 0; listIndex < resultList.size(); listIndex++) {
					if (listIndex == Skf2020Fc001NyukyoKiboSinseiDataImport.INDEX_OF_EXCEPTION_MASSAGE_ID) {
						dto.setErrorCodeID(
								resultList.get(Skf2020Fc001NyukyoKiboSinseiDataImport.INDEX_OF_EXCEPTION_MASSAGE_ID));
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

		LogUtils.debugByMsg("B001-UTTest：テスト実行クラス処理終了");
		return dto;
	}

}
