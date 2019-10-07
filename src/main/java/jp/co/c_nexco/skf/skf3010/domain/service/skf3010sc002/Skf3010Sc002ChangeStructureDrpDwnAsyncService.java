/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc002;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc002.Skf3010Sc002ChangeStructureDrpDwnAsyncDto;

/**
 * Skf3010Sc002ChangeStructureDrpDwnAsyncService 保有社宅登録のドロップダウンリスト変更時非同期処理クラス
 * 基本情報タブの「構造」ドロップダウンチェンジ 
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3010Sc002ChangeStructureDrpDwnAsyncService
		extends AsyncBaseServiceAbstract<Skf3010Sc002ChangeStructureDrpDwnAsyncDto> {

	@Autowired
	private Skf3010Sc002SharedService skf3010Sc002SharedService;

	/**
	 * 基本情報タブ「構造」のドロップダウンリスト変更時イベント
	 */
	@Override
	public AsyncBaseDto index(Skf3010Sc002ChangeStructureDrpDwnAsyncDto asyncDto) throws Exception {

		StringBuilder nextCalcDate = new StringBuilder();
		StringBuilder jituAge = new StringBuilder();
		StringBuilder aging = new StringBuilder();
		nextCalcDate.append(asyncDto.getNextCalcDate());
		jituAge.append(asyncDto.getJituAge());
		aging.append(asyncDto.getAging());
		Boolean result = skf3010Sc002SharedService.setNextCalcDateKeinenJitukeinen(nextCalcDate, jituAge,
						aging, asyncDto.getBuildDate(), asyncDto.getAreaKbnCd(), asyncDto.getStructureKbn());
		if (!result) {
			// 現在設定されている値をクリア
			asyncDto.setBuildDate(null);
			asyncDto.setBuildDate("99999999");
		}

		// 現在設定されている値をクリア
		asyncDto.setNextCalcDate(null);
		asyncDto.setJituAge(null);
		asyncDto.setAging(null);
		// 戻り値設定
		asyncDto.setNextCalcDate(nextCalcDate.toString());
		asyncDto.setJituAge(jituAge.toString());
		asyncDto.setAging(aging.toString());

		return asyncDto;
	}
}
