/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc004;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc004.Skf2060Sc004InitDto;
import jp.co.intra_mart.system.repackage.bouncycastle_1_44.org.bouncycastle.asn1.ocsp.Request;

/**
 * TestPrjTop画面のSearchサービス処理クラス。　 
 * 
 */
@Service
public class Skf2060Sc004SearchService extends BaseServiceAbstract<Skf2060Sc004InitDto> {
	
    @Autowired
    Skf2060Sc004SharedService skf2060Sc004SharedService;

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
	public Skf2060Sc004InitDto index(Skf2060Sc004InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF2060_SC004_TITLE);
		
		// リスト表示データ
        List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
        
        // 検索実行
        listTableData = skf2060Sc004SharedService.getSampleListTableData();
		initDto.setListTableData(listTableData);
		// 一覧最大表示数を設定
		initDto.setListTableMaxRowCount("50"); //TODO
		return initDto;
	}
	
}
