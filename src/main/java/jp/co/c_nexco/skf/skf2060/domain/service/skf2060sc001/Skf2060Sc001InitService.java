/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc001;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc001.Skf2060Sc001InitDto;
import jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc004.Skf3090Sc004SharedService;

/**
 * TestPrjTop画面のInitサービス処理クラス。　 
 * 
 */
@Service
public class Skf2060Sc001InitService extends BaseServiceAbstract<Skf2060Sc001InitDto> {
	
	@Autowired
	private Skf2060Sc001SharedService skf2060Sc001SharedService;
	
	private String companyCd = CodeConstant.C001;
	

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
	public Skf2060Sc001InitDto index(Skf2060Sc001InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF2060_SC001_TITLE);
		
		// リストデータ取得用
		List<Map<String, Object>> dataParamList = new ArrayList<Map<String, Object>>();
		boolean itiranFlg = true;
		dataParamList = skf2060Sc001SharedService.getDataParamList(itiranFlg);
		initDto.setListTableData(dataParamList);
		
		return initDto;
	}
	
}
