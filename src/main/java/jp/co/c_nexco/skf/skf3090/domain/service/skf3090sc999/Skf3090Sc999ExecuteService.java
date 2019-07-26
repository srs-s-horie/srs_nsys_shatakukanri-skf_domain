/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc999;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.util.datalinkage.B2001NyukyoKiboSinseiDataImport;
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
	B2001NyukyoKiboSinseiDataImport b2001;

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
		if (companyCd.equals("null")) {
			companyCd = null;
		}
		if (shainNo.equals("null")) {
			shainNo = null;
		}
		if (applNoNyukyo.equals("null")) {
			applNoNyukyo = null;
		}
		if (applNoTaikyo.equals("null")) {
			applNoTaikyo = null;
		}
		if (applStatus.equals("null")) {
			applStatus = null;
		}

		List<String> resultList = b2001.doProc(companyCd, shainNo, applNoNyukyo, applNoTaikyo, applStatus);

		if (resultList != null) {
			dto.setErrorCodeID(resultList.get(B2001NyukyoKiboSinseiDataImport.INDEX_OF_EXCEPTION_MASSAGE_ID));
			if (resultList.size() > 1) {
				dto.setErrorStrings(resultList.get(B2001NyukyoKiboSinseiDataImport.INDEX_OF_EXCEPTION_PARAMETER));
			}
		}

		LogUtils.debugByMsg("B001-UTTest：テスト実行クラス処理終了");
		return dto;
	}

}
