/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2050.domain.service.skf2050sc001;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.common.utils.PropertyUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationGuideUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2050.domain.dto.skf2050sc001.Skf2050Sc001CarryOutDto;

/**
 * Skf2050Sc001 備品返却申請（申請者用)搬出完了処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2050Sc001CarryOutService extends BaseServiceAbstract<Skf2050Sc001CarryOutDto> {

	private final String COMPLETION_DAY_LABEL = "搬出完了日";
	private final String COMMENT_LABEL = "承認者へのコメント";
	private final String PREFIX_VERIFY_MSG = "搬出完了日が退居日より後ですが";
	private final String SUFFIX_VERIFY_MSG = "(退居日:";

	@Autowired
	private Skf2050Sc001SharedService skf2050Sc001SharedService;

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfOperationGuideUtils skfOperationGuideUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;

	/**
	 * サービス処理を行う。
	 * 
	 * @param coDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public BaseDto index(Skf2050Sc001CarryOutDto coDto) throws Exception {

		if (!skf2050Sc001SharedService.saveBihinHenkyakuInfo(CodeConstant.STATUS_HANSYUTSU_ZUMI, coDto,
				menuScopeSessionBean)) {
			throwBusinessExceptionIfErrors(coDto.getResultMessages());
			return coDto;
		}

		// 前の画面に遷移する
		TransferPageInfo tpi = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC003);
		tpi.addResultMessage(MessageIdConstant.I_SKF_2030);
		coDto.setTransferPageInfo(tpi);

		return coDto;
	}

}
