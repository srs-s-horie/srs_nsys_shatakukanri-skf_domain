/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.service.skf3020sc005;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3020.domain.dto.skf3020sc005.Skf3020Sc005SupportDto;

/**
 * Skf3020Sc005SupportService 転任者登録画面の社員入力支援コールバック処理クラス
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3020Sc005SupportService extends SkfServiceAbstract<Skf3020Sc005SupportDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3020Sc005SharedService skf3020Sc005SharedService;

	@Override
	public Skf3020Sc005SupportDto index(Skf3020Sc005SupportDto supportDto) throws Exception {

		skfOperationLogUtils.setAccessLog("社員入力支援画面からのコールバック処理開始", CodeConstant.C001, supportDto.getPageId());

		// 入力支援画面で選択された社員番号
		String shainNo = supportDto.getHdnSelShainNo();
		supportDto.setShainNo("");

		if (NfwStringUtils.isNotEmpty(shainNo)) {
			skf3020Sc005SharedService.setTenninshaInfo(supportDto, shainNo);

		} else {
			skf3020Sc005SharedService.setInitInfo(supportDto);
		}

		skf3020Sc005SharedService.setControlStatus(supportDto);

		skf3020Sc005SharedService.setBtnEnterMsg(supportDto);

		return supportDto;
	}

}
