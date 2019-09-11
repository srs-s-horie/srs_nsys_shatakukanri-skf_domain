/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.service.skf3020sc005;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3020.domain.dto.skf3020sc005.Skf3020Sc005SetDto;

/**
 * Skf3020Sc005SetService 転任者登録画面の仮社員番号設定処理クラス
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3020Sc005SetService extends BaseServiceAbstract<Skf3020Sc005SetDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;

	@Override
	public Skf3020Sc005SetDto index(Skf3020Sc005SetDto setDto) throws Exception {

		skfOperationLogUtils.setAccessLog("仮社員番号設定", CodeConstant.C001, setDto.getPageId());

		// 仮社員番号
		String kariShainNo = skfBaseBusinessLogicUtils.getMaxKariShainNo();

		setDto.setTxtShainNo(kariShainNo);

		return setDto;
	}

}
