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
import jp.co.c_nexco.nfw.common.utils.PropertyUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.SkfShinseiUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc001.Skf2100Sc001ApplyDto;

/**
 * Skf2100Sc001 モバイルルーター借用希望申請書（申請者用)申請処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2100Sc001ApplyService extends SkfServiceAbstract<Skf2100Sc001ApplyDto> {

	@Autowired
	private Skf2100Sc001SharedService skf2100Sc001SharedService;	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfShinseiUtils skfShinseiUtils;
	
	// メールアドレスチェック正規表現
	private static final String MAIL_ADDRESS_CHECK_REG = "[!-~]+@[!-~]+\\.[!-~]+";
	
	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2100Sc001ApplyDto index(Skf2100Sc001ApplyDto applyDto) throws Exception {
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("申請", CodeConstant.C001, FunctionIdConstant.SKF2100_SC001);

		
		// 入力チェック
		if (!checkDispInfo(applyDto)) {
//			throwBusinessExceptionIfErrors(applyDto.getResultMessages());
			return applyDto;
		}
		
		
		// ログインユーザー情報取得
//		Map<String, String> loginUserInfo = skfLoginUserInfoUtils
//				.getSkfLoginUserInfoFromAlterLogin(menuScopeSessionBean);
		skf2100Sc001SharedService.setMenuScopeSessionBean(menuScopeSessionBean);
		
		// 申請可否チェック
		if(!checkShinsei(applyDto)){
//			throwBusinessExceptionIfErrors(applyDto.getResultMessages());
			return applyDto;
		}
		
		// 申請書情報の取得
		Map<String, String> applInfo = new HashMap<String, String>();
		applInfo = skf2100Sc001SharedService.getSkfApplInfo(applyDto);
		
		// 次のステータスを申請中に設定
		String newStatus = CodeConstant.STATUS_SHINSEICHU;// 申請中
		applInfo.put("newStatus", newStatus);

		
		// 更新処理
		if (!skf2100Sc001SharedService.saveInfo(applInfo, applyDto)) {
			if (applyDto.getResultMessages() == null) {
				ServiceHelper.addErrorResultMessage(applyDto, null, MessageIdConstant.E_SKF_1075);
			}
			throwBusinessExceptionIfErrors(applyDto.getResultMessages());
			return applyDto;
		}

		// 申請状況一覧に画面遷移
		TransferPageInfo tpi = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC003);
		tpi.addResultMessage(MessageIdConstant.I_SKF_2047);
		applyDto.setTransferPageInfo(tpi);

		return applyDto;
	}

	/**
	 * 入力チェックを行う
	 * 
	 * @param applyDto
	 * @return
	 * @throws Exception
	 */
	private boolean checkDispInfo(Skf2100Sc001ApplyDto applyDto) throws Exception {
		boolean result = true;

		List<String> errorTarget = new ArrayList<String>();
		// 必須入力確認
		result = checkControlEmpty(applyDto, errorTarget);
		// フォーマット確認
		result = checkFormat(applyDto, errorTarget, result);
		// バイト数確認
		result = checkByteCount(applyDto, errorTarget, result);
	
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
	private boolean checkFormat(Skf2100Sc001ApplyDto applyDto, List<String> errorTarget,
			boolean result) {
		if (!result) {
			return result;
		}
		
		// メールアドレス
		String mailAddress = (applyDto.getMailAddress() != null) ? applyDto.getMailAddress() : "";
		if(!mailAddress.matches(MAIL_ADDRESS_CHECK_REG) ){
			ServiceHelper.addErrorResultMessage(applyDto, new String[] { "mailAddress" }, MessageIdConstant.E_SKF_3032,
					"メールアドレス");
			result = false;
		}
		
		// 利用開始希望日
		if (!SkfCheckUtils.isSkfDateFormat(applyDto.getUseStartHopeDay().trim(), CheckUtils.DateFormatType.YYYYMMDD)) {
			errorTarget.add("useStartHopeDay");
			ServiceHelper.addErrorResultMessage(applyDto, new String[] { "useStartHopeDay" }, MessageIdConstant.E_SKF_1055,
					"利用開始希望日");
			result = false;
		}
		

		return result;
	}

	/**
	 * バイト数チェックを行います
	 * 
	 * @param applyDto
	 * @param validateFlag
	 * @param errorTarget
	 * @param result
	 * @return
	 * @throws Exception
	 */
	private boolean checkByteCount(Skf2100Sc001ApplyDto applyDto, List<String> errorTarget,
			boolean result) throws Exception {
		if (result) {
			// 電話番号(13バイト)
			if (CheckUtils.isMoreThanByteSize(applyDto.getTel(), 13)) {
				errorTarget.add("tel");
				ServiceHelper.addErrorResultMessage(applyDto, new String[] { "tel" }, MessageIdConstant.E_SKF_1071,
						"電話番号", "13");
				result = false;
			}
			
			// メールアドレス(255バイト)
			if (CheckUtils.isMoreThanByteSize(applyDto.getMailAddress(), 255)) {
				errorTarget.add("mailAddress");
				ServiceHelper.addErrorResultMessage(applyDto, new String[] { "mailAddress" }, MessageIdConstant.E_SKF_1071,
						"メールアドレス", "255");
				result = false;
			}
			
			// コメント(4000バイト
			int commentMaxCnt = Integer.parseInt(PropertyUtils.getValue("skf2100.skf2100_sc001.comment_max_count"));
			if (NfwStringUtils.isNotEmpty(applyDto.getCommentNote())
				&& CheckUtils.isMoreThanByteSize(applyDto.getCommentNote(), commentMaxCnt)) {
				errorTarget.add("commentNote");
				ServiceHelper.addErrorResultMessage(applyDto, new String[] { "commentNote" }, MessageIdConstant.E_SKF_1071,
						"承認者へのコメント", "全角" + commentMaxCnt / 2);
				result = false;
			}
			

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
	private boolean checkControlEmpty(Skf2100Sc001ApplyDto applyDto, List<String> errorTarget) {
		boolean result = true;
		// 電話番号
		if (NfwStringUtils.isEmpty(applyDto.getTel())) {
			errorTarget.add("tel");
			ServiceHelper.addErrorResultMessage(applyDto, new String[] { "tel" }, MessageIdConstant.E_SKF_1048,
					"電話番号");
			result = false;
		}
		
		// メールアドレス
		if (NfwStringUtils.isEmpty(applyDto.getMailAddress())) {
			errorTarget.add("mailAddress");
			ServiceHelper.addErrorResultMessage(applyDto, new String[] { "mailAddress" },
					MessageIdConstant.E_SKF_1048, "メールアドレス");
			result = false;
		}
		
		// 利用開始希望日
		if (NfwStringUtils.isEmpty(applyDto.getUseStartHopeDay())) {
			errorTarget.add("useStartHopeDay");
			ServiceHelper.addErrorResultMessage(applyDto, new String[] { "useStartHopeDay" },
					MessageIdConstant.E_SKF_1048, "利用開始希望日");
			result = false;
		}
		

		return result;
	}
	
	
	/**
	 * 申請可否チェックを行う
	 * 
	 * @param applyDto
	 * @return
	 * @throws Exception
	 */
	private boolean checkShinsei(Skf2100Sc001ApplyDto applyDto ) throws Exception {
		boolean result = true;
		
		// 申請可能かのチェック
		result = skfShinseiUtils.checkRouterShinseiStatus(applyDto.getShainNo(), FunctionIdConstant.R0107, applyDto.getApplNo());
		
		//未返却ルーター確認
		int cnt = skfShinseiUtils.getSksRouterLedgerCount(applyDto.getShainNo());
		if(cnt > 0){
			result = false;
		}
		
		if(!result){
			if(cnt > 0){
				ServiceHelper.addErrorResultMessage(applyDto, null, MessageIdConstant.I_SKF_1005, "貸与中のモバイルルーターが存在し",
						"モバイルルーターの返却申請を", "");
			}else{
				ServiceHelper.addErrorResultMessage(applyDto, null, MessageIdConstant.I_SKF_1005, "承認されていない申請書類が存在し",
						"「社宅申請状況一覧」から確認", "");
			}
		}

		return result;
	}
}