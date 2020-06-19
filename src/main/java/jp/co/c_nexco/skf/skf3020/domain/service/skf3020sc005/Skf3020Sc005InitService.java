/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.service.skf3020sc005;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3020.domain.dto.skf3020sc005.Skf3020Sc005InitDto;

/**
 * Skf3020Sc005InitService 転任者登録画面のInitサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3020Sc005InitService extends SkfServiceAbstract<Skf3020Sc005InitDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3020Sc005SharedService skf3020Sc005SharedService;

	@Override
	public Skf3020Sc005InitDto index(Skf3020Sc005InitDto initDto) throws Exception {

		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, FunctionIdConstant.SKF3020_SC005);

		initDto.setPageTitleKey(MessageIdConstant.SKF3020_SC005_TITLE);

		String hdnRowShainNo = initDto.getHdnRowShainNo();
		// セッション情報存在判定(社員番号存在判定)
		if (NfwStringUtils.isNotEmpty(hdnRowShainNo)) {
			initDto.setHdnRowShainNo(hdnRowShainNo.replace(CodeConstant.ASTERISK, ""));
			skf3020Sc005SharedService.setTenninshaInfo(initDto, hdnRowShainNo);

		} else {
			skf3020Sc005SharedService.setInitInfo(initDto);
		}

		skf3020Sc005SharedService.setControlStatus(initDto);

		skf3020Sc005SharedService.setBtnEnterMsg(initDto);

		return initDto;
	}

}
