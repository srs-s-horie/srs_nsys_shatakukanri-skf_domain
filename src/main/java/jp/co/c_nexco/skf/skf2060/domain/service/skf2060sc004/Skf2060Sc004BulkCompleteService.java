/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc004;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.emory.mathcs.backport.java.util.Arrays;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc004.Skf2060Sc004BulkCompleteDto;

/**
 * TestPrjTop画面のSkf2060Sc004BulkCompleteDto（一括完了）サービス処理クラス。　 
 * 
 */
@Service
public class Skf2060Sc004BulkCompleteService extends BaseServiceAbstract<Skf2060Sc004BulkCompleteDto> {
	
    @Autowired
    Skf2060Sc004SharedService skf2060Sc004SharedService;

	/**
	 * サービス処理を行う。　
	 * 
	 * @param bulkCompDto DTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@SuppressWarnings("unchecked")
    @Override
	public Skf2060Sc004BulkCompleteDto index(Skf2060Sc004BulkCompleteDto bulkCompDto) throws Exception {
		
		bulkCompDto.setPageTitleKey(MessageIdConstant.SKF2060_SC004_TITLE);
		
		// 完了チェックボックスのチェック状態を取得
		List<String> completeChkValList = Arrays.asList(bulkCompDto.getCompleteChkVal());
		
		// 完了対象の申請書類履歴テーブルのステータスを「完了」に変更する
		
		// 提示された借上候補物件を選択した場合
		
		    // 選択された借上候補物件の提示フラグを「選択済」に変更
		    // 選択されなかった借上候補物件の提示フラグを「提示可」に変更
		
		
		
		return bulkCompDto;
	}
	
}
