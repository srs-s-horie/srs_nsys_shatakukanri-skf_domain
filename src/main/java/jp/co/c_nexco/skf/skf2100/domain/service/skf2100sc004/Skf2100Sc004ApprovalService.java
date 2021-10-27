/*
 * Copyright(c) 2021 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc004;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.common.utils.PropertyUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc004.Skf2100Sc004ApprovalDto;

/**
 * Skf2100Sc004 モバイルルーター返却申請書（アウトソース)承認処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2100Sc004ApprovalService extends SkfServiceAbstract<Skf2100Sc004ApprovalDto> {

	@Autowired
	private Skf2100Sc004SharedService skf2100Sc004SharedService;

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	
	/**
	 * サービス処理を行う。
	 * 
	 * @param dto DTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2100Sc004ApprovalDto index(Skf2100Sc004ApprovalDto dto) throws Exception {
		// 操作ログ出力
		skfOperationLogUtils.setAccessLog("承認", CodeConstant.C001, FunctionIdConstant.SKF2100_SC004);
		
		// ログインユーザー情報取得
		Map<String, String> loginUserInfo = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		skf2100Sc004SharedService.setMenuScopeSessionBean(menuScopeSessionBean);
		
		// 申請情報設定
		Map<String, String> applInfo = new HashMap<String, String>();
		applInfo.put("status", dto.getApplStatus());
		applInfo.put("applNo", dto.getApplNo());
		applInfo.put("applId", dto.getApplId());

		
		// 入力チェック
		boolean validateResult = validateCheck(dto);
		if (!validateResult) {
//			throwBusinessExceptionIfErrors(dto.getResultMessages());
			return dto;
		}
		
		// 処理名設定
		String execName = "apply";

		// 更新処理
		boolean updResult = skf2100Sc004SharedService.updateDispInfo(execName, dto, applInfo, loginUserInfo);
		if (!updResult) {
			throwBusinessExceptionIfErrors(dto.getResultMessages());
			return dto;
		}

		
		// 前の画面に遷移する
		TransferPageInfo tpi = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC005);
		tpi.addResultMessage(MessageIdConstant.I_SKF_2031);
		dto.setTransferPageInfo(tpi);

		return dto;
	}
	/**
	 * 入力チェック
	 * 
	 * @param dto
	 * @param checkFlag
	 * @return
	 * @throws Exception
	 */
	private boolean validateCheck(Skf2100Sc004ApprovalDto dto) throws Exception {

		boolean result = true;
		
		// 窓口返却日未入力の場合
		if (NfwStringUtils.isEmpty(dto.getReturnDay())) {
			ServiceHelper.addErrorResultMessage(dto, new String[] { "returnDay" }, MessageIdConstant.E_SKF_1048,
					"窓口返却日");
			result = false;
		}

		// 申請者へのコメント
		String commentNote = dto.getCommentNote();
		// 値が4000バイトを超えた際はエラー
		int commentMaxLength = Integer.parseInt(PropertyUtils.getValue("skf2100.skf2100_sc004.comment_max_count"));
		if (NfwStringUtils.isNotEmpty(commentNote) && commentMaxLength < commentNote.getBytes("UTF-8").length) {
			ServiceHelper.addErrorResultMessage(dto, new String[] { "commentNote" }, MessageIdConstant.E_SKF_1049,
					"申請者へのコメント", "全角"+commentMaxLength / 2);
			result = false;
		}

		return result;
	}

}
