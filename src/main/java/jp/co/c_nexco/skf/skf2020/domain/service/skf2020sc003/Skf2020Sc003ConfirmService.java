/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2020.domain.service.skf2020sc003;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.CopyUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.app.BaseForm;
import jp.co.c_nexco.nfw.webcore.app.FormHelper;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2020.domain.dto.skf2020sc003.Skf2020Sc003ConfirmDto;

/**
 * Skf2020Sc003 社宅入居希望等調書（アウトソース用）差戻し（否認）処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2020Sc003ConfirmService extends SkfServiceAbstract<Skf2020Sc003ConfirmDto> {

	@Autowired
	private Skf2020Sc003SharedService skf2020sc003SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfAttachedFileUtils skfAttachedFileUtils;
	
	private String companyCd = CodeConstant.C001;
	private String sessionKey = SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY;
	//添付ファイルセッション競合チェック用
	private String sessionConflictKey = SessionCacheKeyConstant.COMMON_ATTACHED_FILE_CONFLICT_SESSION_KEY;

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
		skfOperationLogUtils.setAccessLog("提示内容を確認", companyCd, FunctionIdConstant.SKF2020_SC003);
		
		//複数タブによる添付ファイルセッションチェック		
		boolean checkResults = skfAttachedFileUtils.attachedFileSessionConflictCheck(menuScopeSessionBean,confDto.getApplNo());
		
		//申請書管理番号が一致しない
		if (!checkResults) {			
			// セッション情報の削除
			menuScopeSessionBean.remove(sessionKey);
			menuScopeSessionBean.remove(sessionConflictKey);
			ServiceHelper.addErrorResultMessage(confDto, null, MessageIdConstant.I_SKF_1005,"画面(タブ)の二重起動は禁止され","一度ブラウザを閉じて、初めからやり直","");
			throwBusinessExceptionIfErrors(confDto.getResultMessages());
			return confDto;
		}	

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
		confDto.setTransferPageInfo(tpi, true);

		return confDto;
	}

	private void confirmClickProcess(Skf2020Sc003ConfirmDto confDto) {
		// 頻出データを変数に取得
		String newStatus = confDto.getApplStatus();

		Map<String, String> errorMsg = new HashMap<String, String>();
		if (!skf2020sc003SharedService.saveApplInfo(newStatus, confDto, errorMsg)) {
			// エラーメッセージが空ではない場合
			if (NfwStringUtils.isNotEmpty(errorMsg.get("error"))) {
				ServiceHelper.addErrorResultMessage(confDto, null, errorMsg.get("error"));
			}
			throwBusinessExceptionIfErrors(confDto.getResultMessages());
		}

	}

}
