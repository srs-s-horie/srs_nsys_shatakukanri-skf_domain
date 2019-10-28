/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skfjoin.domain.service.skfjointest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.skfjoin.domain.dto.skfjointest.SkfJoinTestInitDto;

/**
 * SkfJoinTestInitService データ連携必須入力チェック合同テスト（入退居以外）初期表示クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class SkfJoinTestInitService extends BaseServiceAbstract<SkfJoinTestInitDto> {

	@Autowired
	private static final String DATA_LINKAGE_KEY_JOINTEST = "mapKeyOfJoinTest";

	/**
	 * 画面初期表示のメイン処理
	 */
	@Override
	protected BaseDto index(SkfJoinTestInitDto initDto) throws Exception {
		return initDto;
	}

}
