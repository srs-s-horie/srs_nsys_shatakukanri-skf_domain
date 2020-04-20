/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2020.domain.service.skf2020sc002;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.CopyUtils;
import jp.co.c_nexco.nfw.webcore.app.BaseForm;
import jp.co.c_nexco.nfw.webcore.app.FormHelper;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2020.domain.dto.skf2020sc002.Skf2020Sc002ConfirmDto;

/**
 * 
 * 入居希望等調書申請画面の申請内容を確認ボタンの登録処理
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf2020Sc002ConfirmService extends SkfServiceAbstract<Skf2020Sc002ConfirmDto> {

	@Autowired
	private Skf2020Sc002SharedService skf2020Sc002SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	@Override
	public BaseDto index(Skf2020Sc002ConfirmDto dto) throws Exception {

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("申請内容を確認", CodeConstant.C001, dto.getPageId());

		// 登録処理
		// ステータスの設定
		Map<String, String> applInfo = skf2020Sc002SharedService.getSkfApplInfo(dto);
		// 申請者用のステータスをデフォルト設定
		String newStatus = CodeConstant.STATUS_ICHIJIHOZON;
		applInfo.put("newStatus", newStatus);

		// 一時保存処理を実行
		if (!skf2020Sc002SharedService.saveInfo(applInfo, dto)) {
			return dto;
		}

		// 排他制御用の値を再設定
		skf2020Sc002SharedService.setApplHistoryUpdateDate(dto);
		skf2020Sc002SharedService.setBihinHenkyakuUpdateDate(dto);
		skf2020Sc002SharedService.setNyukyoChoshoUpdateDate(dto);

		// フォームデータを設定
		dto.setPrePageId(dto.getPageId());
		BaseForm form = new BaseForm();
		CopyUtils.copyProperties(form, dto);
		FormHelper.setFormBean(FunctionIdConstant.SKF2010_SC002, form);

		// 申請書類確認画面に遷移
		TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC002);
		Map<String, Object> attributeMap = new HashMap<String, Object>();
		attributeMap.put(SkfCommonConstant.KEY_APPL_NO, dto.getApplNo());
		attributeMap.put(SkfCommonConstant.KEY_STATUS, dto.getApplStatus());
		nextPage.setTransferAttributes(attributeMap);
		dto.setTransferPageInfo(nextPage, true);

		return dto;
	}
}
