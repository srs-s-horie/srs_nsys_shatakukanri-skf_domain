/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc009;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.SkfAsyncServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc009.Skf2100Sc009SearchAsyncDto;


/**
 * Skf3022Sc001SearchAsyncService モバイルルーター機器取得非同期処理クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf2100Sc009SearchAsyncService
		extends SkfAsyncServiceAbstract<Skf2100Sc009SearchAsyncDto> {
	@Autowired
	private Skf2100Sc009SharedService skf2100Sc009SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;

	@Value("${skf2100.skf2100_sc009.max_row_count}")
	private String maxRowCount;
	@Value("${skf2100.skf2100_sc009.max_search_count}")
	private String maxSearchCount;

	@Override
	public AsyncBaseDto index(Skf2100Sc009SearchAsyncDto asyncDto) throws Exception {

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("検索", CodeConstant.C001, FunctionIdConstant.SKF2100_SC009);

		// リストデータ取得用
		List<Map<String, Object>> routerList = new ArrayList<Map<String, Object>>();

		// 日付関連チェック
		if(!isErrDateFormat(asyncDto)){
			throwBusinessExceptionIfErrors(asyncDto.getResultMessages());
		}
		
		// 検索
		String contractEndDateFrom = skfDateFormatUtils.dateFormatFromString(asyncDto.getSc009ContractEndDateFrom(), "yyyyMMdd");
		String contractEndDateTo = skfDateFormatUtils.dateFormatFromString(asyncDto.getSc009ContractEndDateTo(), "yyyyMMdd");
		
		int count = skf2100Sc009SharedService.getRouterList(
				asyncDto.getRouterNo(), asyncDto.getTel(),
				contractEndDateFrom, contractEndDateTo,
				asyncDto.getBiko(), routerList);
		if (count == 0) {
			ServiceHelper.addWarnResultMessage(asyncDto, MessageIdConstant.W_SKF_1007);
			throwBusinessExceptionIfErrors(asyncDto.getResultMessages());
		} else if (count == -1) {
			ServiceHelper.addErrorResultMessage(asyncDto, null, MessageIdConstant.E_SKF_1046, maxSearchCount);
			throwBusinessExceptionIfErrors(asyncDto.getResultMessages());
		}

		asyncDto.setListTableList(routerList);
		asyncDto.setDataCount(count);
		LogUtils.debugByMsg("モバイルルーター機器情報：" + count + "件");
		return asyncDto;
	}
	
	/**
	 * 整合性チェック
	 * @param comDto
	 * @param errMsg エラーメッセージ<
	 */
	private boolean isErrDateFormat(Skf2100Sc009SearchAsyncDto comDto) throws ParseException{
		

		// 契約終了日のFromとToが入力されている場合
		if(!SkfCheckUtils.isNullOrEmpty(comDto.getSc009ContractEndDateFrom())
			&& !SkfCheckUtils.isNullOrEmpty(comDto.getSc009ContractEndDateTo())){
			if(skfDateFormatUtils.validateDateCorrelation(comDto.getSc009ContractEndDateFrom(),comDto.getSc009ContractEndDateTo())){
				ServiceHelper.addErrorResultMessage(comDto, null, MessageIdConstant.E_SKF_1133,"契約終了日");
				return false;
			}
		}
		
		return true;
	
	}
}
