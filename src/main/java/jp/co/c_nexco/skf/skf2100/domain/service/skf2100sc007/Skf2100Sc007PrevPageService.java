/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc007;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.bean.ApplicationScopeBean;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc007.Skf2100Sc007PrevPageDto;

/**
 * Skf2100Sc007PrevPageService 戻るサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2100Sc007PrevPageService extends SkfServiceAbstract<Skf2100Sc007PrevPageDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private ApplicationScopeBean bean;
	
	/**
	 * 前の画面へ戻るメソッド
	 * 
	 * @param prevPageDto インプットDTO
	 * @return 処理結果
	 */
	@Override
	public Skf2100Sc007PrevPageDto index(Skf2100Sc007PrevPageDto prevPageDto) {

		// デバッグログ
		LogUtils.debugByMsg("戻る");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("戻る", CodeConstant.C001, FunctionIdConstant.SKF2100_SC007);

		//セッション情報削除
		@SuppressWarnings("unchecked")
		Map<String, Object> sessionData = (Map<String, Object>) bean
				.get(SessionCacheKeyConstant.MOBILEROUTER_MASTER_SEARCH);
		if (sessionData != null) {
			bean.remove(SessionCacheKeyConstant.MOBILEROUTER_MASTER_SEARCH);
		}
		
		// 画面遷移（戻る）
		TransferPageInfo nextPage = TransferPageInfo.prevPage(FunctionIdConstant.SKF1010_SC001, "init");
		prevPageDto.setTransferPageInfo(nextPage);
 
		return prevPageDto;
	}
	
}
