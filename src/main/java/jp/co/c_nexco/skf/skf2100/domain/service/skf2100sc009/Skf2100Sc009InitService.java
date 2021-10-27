/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc009;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2100Sc009.Skf2100Sc009GetListTableDataExp;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc009.Skf2100Sc009InitDto;

/**
 * Skf2100Sc009InitService モバイルルーター機器入力支援画面のInitサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2100Sc009InitService extends SkfServiceAbstract<Skf2100Sc009InitDto> {

	@Autowired
	private Skf2100Sc009SharedService skf2100Sc009SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	@Value("${skf2100.skf2100_sc009.max_row_count}")
	private String maxRowCount;
	@Value("${skf2100.skf2100_sc009.max_search_count}")
	private String maxSearchCount;
	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public Skf2100Sc009InitDto index(Skf2100Sc009InitDto initDto) throws Exception {

		initDto.setPageTitleKey(MessageIdConstant.SKF2100_SC009_TITLE);

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, FunctionIdConstant.SKF2100_SC009);
		
		init(initDto);

		// 初期表示では検索結果は0件表示
		List<Skf2100Sc009GetListTableDataExp> routerList = new ArrayList<Skf2100Sc009GetListTableDataExp>();
		initDto.setListTableList(skf2100Sc009SharedService.createListTable(routerList));

		return initDto;
	}

	/**
	 * 初期化処理
	 * 
	 * 「※」項目はアドレスとして戻り値になる。 
	 * 
	 * @param initDto	*DTO
	 */
	private void init(Skf2100Sc009InitDto initDto) {

		initDto.setListTableList(null);
		initDto.setRouterNo(CodeConstant.DOUBLE_QUOTATION);
		initDto.setTel(CodeConstant.DOUBLE_QUOTATION);
		initDto.setSc009ContractEndDateFrom(CodeConstant.DOUBLE_QUOTATION);
		initDto.setSc009ContractEndDateTo(CodeConstant.DOUBLE_QUOTATION);
		initDto.setBiko(CodeConstant.DOUBLE_QUOTATION);
		initDto.setMaxCount(maxRowCount);
	}
}
