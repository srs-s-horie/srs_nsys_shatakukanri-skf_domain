/*
 * Copyright(c) 2021 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc001;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc001.Skf2100Sc001CompleteDto;

/**
 * Skf2100Sc001 モバイルルーター借用希望申請書（申請者用)申請処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2100Sc001CompleteService extends SkfServiceAbstract<Skf2100Sc001CompleteDto> {

	@Autowired
	private Skf2100Sc001SharedService skf2100Sc001SharedService;	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	
	
	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2100Sc001CompleteDto index(Skf2100Sc001CompleteDto compDto) throws Exception {
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("搬入完了", CodeConstant.C001, FunctionIdConstant.SKF2100_SC001);

		skf2100Sc001SharedService.setCheckFlag(compDto);
		
		// 入力チェック
		if (!checkDispInfo(compDto)) {
			return compDto;
//			throwBusinessExceptionIfErrors(compDto.getResultMessages());
		}
		
		skf2100Sc001SharedService.setMenuScopeSessionBean(menuScopeSessionBean);
		
		// 申請書情報の取得
		Map<String, String> applInfo = new HashMap<String, String>();
		applInfo = skf2100Sc001SharedService.getSkfApplInfo(compDto);
		
		// 次のステータスを搬入済みに設定
		String newStatus = CodeConstant.STATUS_HANNYU_ZUMI;
		applInfo.put("newStatus", newStatus);

		
		// 更新処理
		if (!skf2100Sc001SharedService.saveInfo(applInfo, compDto)) {
			if (compDto.getResultMessages() == null) {
				ServiceHelper.addErrorResultMessage(compDto, null, MessageIdConstant.E_SKF_1075);
			}
			throwBusinessExceptionIfErrors(compDto.getResultMessages());
			return compDto;
		}

		// 申請状況一覧に画面遷移
		TransferPageInfo tpi = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC003);
		tpi.addResultMessage(MessageIdConstant.I_SKF_2047);
		compDto.setTransferPageInfo(tpi);

		return compDto;
	}

	/**
	 * 入力チェックを行う
	 * 
	 * @param applyDto
	 * @return
	 * @throws Exception
	 */
	private boolean checkDispInfo(Skf2100Sc001CompleteDto applyDto) throws Exception {
		boolean result = true;

		List<String> errorTarget = new ArrayList<String>();
		// 必須入力確認
		result = checkControlEmpty(applyDto, errorTarget);
		// フォーマット確認
		result = checkFormat(applyDto, errorTarget, result);

		return result;
	}

	/**
	 * フォーマットチェックを行う
	 * 
	 * @param applyDto
	 * @param validateFlag
	 * @param errorTarget
	 * @param result
	 * @return
	 */
	private boolean checkFormat(Skf2100Sc001CompleteDto applyDto, List<String> errorTarget,
			boolean result) {
		if (!result) {
			return result;
		}
		// 受領日
		if (!SkfCheckUtils.isSkfDateFormat(applyDto.getReceivedDate().trim(), CheckUtils.DateFormatType.YYYYMMDD)) {
			errorTarget.add("receivedDate");
			ServiceHelper.addErrorResultMessage(applyDto, new String[] { "receivedDate" }, MessageIdConstant.E_SKF_1055,
					"受領日");
			result = false;
		}
		

		return result;
	}

	/**
	 * 必須入力チェックを行います
	 * 
	 * @param applyDto
	 * @param validateFlag
	 * @param errorTarget
	 * @return
	 */
	private boolean checkControlEmpty(Skf2100Sc001CompleteDto applyDto, List<String> errorTarget) {
		boolean result = true;
		// 受領日
		if (NfwStringUtils.isEmpty(applyDto.getReceivedDate())) {
			errorTarget.add("receivedDate");
			ServiceHelper.addErrorResultMessage(applyDto, new String[] { "receivedDate" }, 
					MessageIdConstant.E_SKF_1048, "受領日");
			result = false;
		}
		
		// モバイルルーター本体受領チェック
		if (NfwStringUtils.isEmpty(applyDto.getBodyReceiptCheckFlag())) {
			errorTarget.add("bodyReceiptCheckFlag");
			ServiceHelper.addErrorResultMessage(applyDto, new String[] { "bodyReceiptCheckFlag" },
					MessageIdConstant.E_SKF_1048, "モバイルルーター本体受領");
			result = false;
		}
		
		// モバイルルーター貸与手引き受領チェック
		if (NfwStringUtils.isEmpty(applyDto.getHandbookReceiptCheckFlag())) {
			errorTarget.add("handbookReceiptCheckFlag");
			ServiceHelper.addErrorResultMessage(applyDto, new String[] { "handbookReceiptCheckFlag" },
					MessageIdConstant.E_SKF_1048, "モバイルルーター貸与の手引き受領");
			result = false;
		}
		
		// 返却資材受領チェック
		if (NfwStringUtils.isEmpty(applyDto.getMaterialsReceivedCheckFlag())) {
			errorTarget.add("materialsReceivedCheckFlag");
			ServiceHelper.addErrorResultMessage(applyDto, new String[] { "materialsReceivedCheckFlag" },
					MessageIdConstant.E_SKF_1048, "返却用資材一式(着払伝票・緩衝材)受領");
			result = false;
		}
		

		return result;
	}
	
	

}
