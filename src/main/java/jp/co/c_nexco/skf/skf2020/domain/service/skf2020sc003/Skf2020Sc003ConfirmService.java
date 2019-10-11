/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2020.domain.service.skf2020sc003;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.CopyUtils;
import jp.co.c_nexco.nfw.webcore.app.BaseForm;
import jp.co.c_nexco.nfw.webcore.app.FormHelper;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2020.domain.dto.skf2020sc003.Skf2020Sc003ConfirmDto;

/**
 * Skf2020Sc003 社宅入居希望等調書（アウトソース用）差戻し（否認）処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2020Sc003ConfirmService extends BaseServiceAbstract<Skf2020Sc003ConfirmDto> {

	@Autowired
	private Skf2020Sc003SharedService skf2020sc003SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	private String companyCd = CodeConstant.C001;

	// カンマ区切りフォーマット
	NumberFormat nfNum = NumberFormat.getNumberInstance();

	/**
	 * サービス処理を行う。
	 * 
	 * @param updDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public BaseDto index(Skf2020Sc003ConfirmDto confDto) throws Exception {

		// 操作ログ出力メソッドを呼び出す
		skfOperationLogUtils.setAccessLog("掲示内容を確認処理開始", companyCd, confDto.getPageId());

		confirmClickProcess(confDto);

		// フォームデータを設定
		confDto.setPrePageId(confDto.getPageId());
		BaseForm form = new BaseForm();
		CopyUtils.copyProperties(form, confDto);
		FormHelper.setFormBean(FunctionIdConstant.SKF2010_SC002, form);

		Map<String, Object> attribute = new HashMap<String, Object>();
		attribute.put(SkfCommonConstant.KEY_APPL_NO, confDto.getApplNo());
		attribute.put(SkfCommonConstant.KEY_STATUS, confDto.getApplStatus());

		TransferPageInfo tpi = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC002);
		tpi.setTransferAttributes(attribute);
		confDto.setTransferPageInfo(tpi);

		return confDto;
	}

	private void confirmClickProcess(Skf2020Sc003ConfirmDto confDto) {
		// 頻出データを変数に取得
		String newStatus = confDto.getApplStatus();

		Map<String, String> errorMsg = new HashMap<String, String>();
		if (!skf2020sc003SharedService.saveApplInfo(newStatus, confDto, errorMsg)) {
			ServiceHelper.addErrorResultMessage(confDto, null, errorMsg.get("error"));
			throwBusinessExceptionIfErrors(confDto.getResultMessages());
		}

	}

}
