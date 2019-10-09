/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc003;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.CopyUtils;
import jp.co.c_nexco.nfw.webcore.app.BaseForm;
import jp.co.c_nexco.nfw.webcore.app.FormHelper;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc003.Skf2010Sc003TransferDto;

/**
 * Skf2010Sc003 申請状況一覧取下げ処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc003TransferService extends BaseServiceAbstract<Skf2010Sc003TransferDto> {

	/**
	 * サービス処理を行う。
	 * 
	 * @param cancelDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public BaseDto index(Skf2010Sc003TransferDto transDto) throws Exception {
		String applId = transDto.getApplId();
		String applNo = transDto.getApplNo();
		String applStatus = transDto.getSendApplStatus();

		String nextPageId = FunctionIdConstant.SKF2010_SC004;
		// 「入居希望等調書」「退居（自動車の保管場所返還）届」かつ、ステータスが「一時保存」「差戻し」以外
		if ((CheckUtils.isEqual(applId, FunctionIdConstant.R0100)
				|| CheckUtils.isEqual(applId, FunctionIdConstant.R0103))
				&& !(CheckUtils.isEqual(applStatus, CodeConstant.STATUS_ICHIJIHOZON)
						|| CheckUtils.isEqual(applStatus, CodeConstant.STATUS_SASHIMODOSHI))) {
			nextPageId = FunctionIdConstant.SKF2010_SC004;
		} else {
			switch (applId) {
			case FunctionIdConstant.R0100:
				nextPageId = FunctionIdConstant.SKF2020_SC002;
				break;
			case FunctionIdConstant.R0103:
				nextPageId = FunctionIdConstant.SKF2040_SC001;
				break;
			case FunctionIdConstant.R0104:
				nextPageId = FunctionIdConstant.SKF2030_SC001;
				break;
			case FunctionIdConstant.R0105:
				nextPageId = FunctionIdConstant.SKF2050_SC001;
				break;
			case FunctionIdConstant.R0106:
				nextPageId = FunctionIdConstant.SKF2060_SC002;
				break;
			}
		}

		// フォームデータを設定
		transDto.setPrePageId(FunctionIdConstant.SKF2010_SC003);
		BaseForm form = new BaseForm();
		CopyUtils.copyProperties(form, transDto);
		FormHelper.setFormBean(nextPageId, form);

		Map<String, Object> attribute = new HashMap<String, Object>();
		attribute.put(SkfCommonConstant.KEY_APPL_ID, applId);
		attribute.put(SkfCommonConstant.KEY_APPL_NO, applNo);
		attribute.put(SkfCommonConstant.KEY_STATUS, applStatus);

		TransferPageInfo tpi = TransferPageInfo.nextPage(nextPageId);
		tpi.setTransferAttributes(attribute);
		transDto.setTransferPageInfo(tpi);

		return transDto;
	}

}
