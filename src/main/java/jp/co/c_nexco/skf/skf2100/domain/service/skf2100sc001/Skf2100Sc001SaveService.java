/*
 * Copyright(c) 2021 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc001;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.SkfShinseiUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc001.Skf2100Sc001SaveDto;

/**
 * Skf2100Sc001 モバイルルーター借用希望申請書（申請者用)一時保存処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2100Sc001SaveService extends SkfServiceAbstract<Skf2100Sc001SaveDto> {

	@Autowired
	private Skf2100Sc001SharedService skf2100Sc001SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfShinseiUtils skfShinseiUtils;

	
	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2100Sc001SaveDto index(Skf2100Sc001SaveDto saveDto) throws Exception {
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("一時保存", CodeConstant.C001, FunctionIdConstant.SKF2100_SC001);

		// 申請可否チェック
		if(!checkShinsei(saveDto)){
			return saveDto;
		}
		
		// 申請書情報の取得
		Map<String, String> applInfo = new HashMap<String, String>();
		applInfo = skf2100Sc001SharedService.getSkfApplInfo(saveDto);
		// 次のステータスをデフォルト設定
		String newStatus = CodeConstant.STATUS_ICHIJIHOZON;
		applInfo.put("newStatus", newStatus);

		// 一時保存処理
		if (!skf2100Sc001SharedService.saveInfo(applInfo, saveDto)) {
			throwBusinessExceptionIfErrors(saveDto.getResultMessages());
			return saveDto;
		}

		// 入力情報のクリア
		skf2100Sc001SharedService.setClearInfo(saveDto);

		// 登録済みデータの情報設定
		skf2100Sc001SharedService.setSinseiInfo(saveDto, true);
		
		// 画面表示制御再設定
		skf2100Sc001SharedService.setControlValue(saveDto);

		// 正常終了
		if (CodeConstant.STATUS_MISAKUSEI.equals(applInfo.get("status"))) {
			ServiceHelper.addResultMessage(saveDto, MessageIdConstant.I_SKF_1012);
		} else {
			ServiceHelper.addResultMessage(saveDto, MessageIdConstant.I_SKF_1011);
		}

		return saveDto;
	}

	/**
	 * 申請可否チェックを行う
	 * 
	 * @param applyDto
	 * @return
	 * @throws Exception
	 */
	private boolean checkShinsei(Skf2100Sc001SaveDto saveDto ) throws Exception {
		boolean result = true;
		
		// 申請可能かのチェック
		result = skfShinseiUtils.checkRouterShinseiStatus(saveDto.getShainNo(), FunctionIdConstant.R0107, saveDto.getApplNo());
		
		//未返却ルーター確認
		int cnt = skfShinseiUtils.getSksRouterLedgerCount(saveDto.getShainNo());
		if(cnt > 0){
			result = false;
		}
		
		if(!result){
			if(cnt > 0){
				ServiceHelper.addErrorResultMessage(saveDto, null, MessageIdConstant.I_SKF_1005, "貸与中のモバイルルーターが存在し",
						"モバイルルーターの返却申請を", "");
			}else{
				ServiceHelper.addErrorResultMessage(saveDto, null, MessageIdConstant.I_SKF_1005, "承認されていない申請書類が存在し",
						"「申請状況一覧」から確認", "");
			}
		}

		return result;
	}
}
