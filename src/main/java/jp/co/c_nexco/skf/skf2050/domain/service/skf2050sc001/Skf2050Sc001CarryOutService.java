/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2050.domain.service.skf2050sc001;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.CopyUtils;
import jp.co.c_nexco.nfw.webcore.app.BaseForm;
import jp.co.c_nexco.nfw.webcore.app.FormHelper;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.skf2050.domain.dto.skf2050sc001.Skf2050Sc001CarryOutDto;

/**
 * Skf2050Sc001 備品返却確認（申請者用)搬出完了処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2050Sc001CarryOutService extends BaseServiceAbstract<Skf2050Sc001CarryOutDto> {

	@Autowired
	private Skf2050Sc001SharedService skf2050Sc001SharedService;

	/**
	 * サービス処理を行う。
	 * 
	 * @param coDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public BaseDto index(Skf2050Sc001CarryOutDto coDto) throws Exception {

		skf2050Sc001SharedService.setMenuScopeSessionBean(menuScopeSessionBean);
		if (!skf2050Sc001SharedService.saveBihinHenkyakuInfo(CodeConstant.STATUS_HANSYUTSU_ZUMI, coDto,
				menuScopeSessionBean)) {
			throwBusinessExceptionIfErrors(coDto.getResultMessages());
			return coDto;
		}

		// フォームデータを設定
		coDto.setPrePageId(FunctionIdConstant.SKF1010_SC001); // 次の画面での「前に戻る」はトップへ
		BaseForm form = new BaseForm();
		CopyUtils.copyProperties(form, coDto);
		FormHelper.setFormBean(FunctionIdConstant.SKF2010_SC003, form);

		// 前の画面に遷移する
		TransferPageInfo tpi = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC003);
		tpi.addResultMessage(MessageIdConstant.I_SKF_2047);
		coDto.setTransferPageInfo(tpi);

		return coDto;
	}

}
