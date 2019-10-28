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
		String userID = dto.getUserID();
		String pageID = dto.getPageID();

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
		if (userID != null && userID.equals("null")) {
			LogUtils.debugByMsg("B001-UTTest：ユーザIDのnull変換");
			userID = null;
		}
		if (pageID != null && pageID.equals("null")) {
			pageID = null;
		}

		try {
			LogUtils.debugByMsg("B001-UTTest：データ連携に渡すパラメータ = 会社コード：" + companyCd + " 社員番号：" + shainNo + " 入居申請番号："
					+ applNoNyukyo + " 退居申請番号：" + applNoTaikyo + " 申請ステータス：" + applStatus + " ユーザID：" + userID
					+ " 画面ID：" + pageID);
			List<String> resultList = b2001.doProc(companyCd, shainNo, applNoNyukyo, applNoTaikyo, applStatus, userID,
					pageID);

			if (resultList != null) {
				String errorMessage = "";
				for (int listIndex = 0; listIndex < resultList.size(); listIndex++) {
					if (listIndex == 0) {
						dto.setErrorCodeID(resultList.get(0));
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
