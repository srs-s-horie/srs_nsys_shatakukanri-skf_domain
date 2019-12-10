/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3040.domain.service.skf3040sc001;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3040.domain.dto.skf3040sc001.Skf3040Sc001InitDto;

/**
 * レンタル備品指示書出力画面のInitサービス処理クラス。　 
 * 
 */
@Service
public class Skf3040Sc001InitService extends BaseServiceAbstract<Skf3040Sc001InitDto> {
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	
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
	public Skf3040Sc001InitDto index(Skf3040Sc001InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF3040_SC001_TITLE);
 		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, initDto.getPageId());
		
		// システム処理年月取得
		String sysProcessDate = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu();
		
		// 希望日の設定
		if(true == NfwStringUtils.isNotEmpty(sysProcessDate)){
			// 希望日From
			String startDate = sysProcessDate + "01";
			initDto.setDesiredTermFrom(startDate);
			// 希望日To
			SimpleDateFormat dateFormatFrom = new SimpleDateFormat("yyyyMMdd");
			Date dateFrom = null;
			dateFrom = dateFormatFrom.parse(startDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dateFrom);
			// 1カ月加算
			calendar.add(Calendar.MONTH, 1);
			// 1日減算
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			String endDate = dateFormatFrom.format(calendar.getTime()).toString();
			initDto.setDesiredTermTo(endDate);
		}
		
		// 再発行の設定（発行済みを含まない）
		initDto.setReIssuance(CodeConstant.NO);
		
		return initDto;
	}
	
}
