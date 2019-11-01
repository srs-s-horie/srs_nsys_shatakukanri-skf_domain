/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3070.domain.service.skf3070sc001;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3070Sc001.Skf3070Sc001GetOwnerContractListExpParameter;
import jp.co.c_nexco.skf.skf3070.domain.dto.skf3070Sc001common.Skf3070Sc001CommonDto;

/**
 * Skf3070Sc001 法定調書データ管理画面の共通処理クラス。
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf3070Sc001SheardService {

	/**
	 * 代理人情報一覧を取得して、画面のlistTableに表示するための情報を作成する。
	 * 
	 * @param param
	 * @param initDto
	 * @return
	 */
	protected List<Map<String, Object>> getListTableData(Skf3070Sc001GetOwnerContractListExpParameter param,
			Skf3070Sc001CommonDto dto) {

		// 検索を実行
		// List<Skf3070Sc001GetOwnerContractListExp> kariageExpList =
		// getOwnerContractInfo(param);

		return null;
	}

	public void getDoropDownList() {
		// TODO 自動生成されたメソッド・スタブ

	}

}
