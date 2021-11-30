/*
 * Copyright(c) 2021 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc002;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationGuideUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc002.Skf2100Sc002InitDto;

/**
 * Skf2100Sc002 モバイルルーター借用希望申請書（アウトソース用)初期表示処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2100Sc002InitService extends SkfServiceAbstract<Skf2100Sc002InitDto> {

	@Autowired
	private Skf2100Sc002SharedService skf2100Sc002SharedService;

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfOperationGuideUtils skfOperationGuideUtils;

	
	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2100Sc002InitDto index(Skf2100Sc002InitDto initDto) throws Exception {
		// 操作ログ出力
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, FunctionIdConstant.SKF2100_SC002);
		// セッション情報引き渡し
		skf2100Sc002SharedService.setMenuScopeSessionBean(menuScopeSessionBean);
		// セッション情報初期化
		skf2100Sc002SharedService.clearMenuScopeSessionBean();
		
		//DTOリセット
		skf2100Sc002SharedService.setClearInfo(initDto);

		// タイトル設定
		initDto.setPageTitleKey(MessageIdConstant.SKF2100_SC002_TITLE);
		
		// 申請情報設定
		Map<String, String> applInfo = new HashMap<String, String>();
		applInfo.put("status", initDto.getApplStatus());
		applInfo.put("applNo", initDto.getApplNo());
		applInfo.put("applId", initDto.getApplId());

		// 画面内容の設定
		skf2100Sc002SharedService.setDisplayData(initDto);


		// 画面制御処理（活性／非活性）
		skf2100Sc002SharedService.setControlValue(initDto);
		
		// 操作ガイドの設定
		initDto.setOperationGuide(skfOperationGuideUtils.getOperationGuide(FunctionIdConstant.SKF2100_SC002));

		// バナー戻るボタン遷移先調整
		String backUrl = "skf/" + FunctionIdConstant.SKF2010_SC005 + "/init"; // デフォルトは申請状況一覧へ
		initDto.setBackUrl(backUrl);

		return initDto;
	}


}
