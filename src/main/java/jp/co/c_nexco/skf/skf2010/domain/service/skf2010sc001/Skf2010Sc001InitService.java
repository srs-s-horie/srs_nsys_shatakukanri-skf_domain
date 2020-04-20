/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc001;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc001.Skf2010Sc001GetAllShainInfoExp;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc001.Skf2010Sc001InitDto;

/**
 * 社員名入力支援画面のInitサービス処理クラス。
 * 
 */
@Service
public class Skf2010Sc001InitService extends SkfServiceAbstract<Skf2010Sc001InitDto> {

	@Autowired
	private Skf2010Sc001SharedService skf2010Sc001SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	private String companyCd = CodeConstant.C001;

	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2010Sc001InitDto index(Skf2010Sc001InitDto initDto) throws Exception {
		// 操作ログ登録
		skfOperationLogUtils.setAccessLog("初期表示", companyCd, FunctionIdConstant.SKF2010_SC001);

		initDto.setPageTitleKey(MessageIdConstant.SKF2010_SC001_TITLE);

		// 初期化処理
		init(initDto);

		// 初期表示では検索結果は0件表示
		List<Skf2010Sc001GetAllShainInfoExp> shainInfoList = new ArrayList<Skf2010Sc001GetAllShainInfoExp>();
		initDto.setPopListTableList(skf2010Sc001SharedService.createListTable(shainInfoList));

		return initDto;
	}

	/**
	 * 初期化処理
	 * 
	 * @param initDto
	 */
	private void init(Skf2010Sc001InitDto initDto) {
		initDto.setPopShainNo(CodeConstant.NONE);
		initDto.setPopName(CodeConstant.NONE);
		initDto.setPopNameKk(CodeConstant.NONE);
		initDto.setPopAgency(CodeConstant.NONE);
	}

}
