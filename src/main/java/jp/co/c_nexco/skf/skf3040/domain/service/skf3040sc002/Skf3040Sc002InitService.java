/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3040.domain.service.skf3040sc002;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3040.domain.dto.skf3040sc002.Skf3040Sc002InitDto;

/**
 * Skf3040Sc002InitService 備品搬入・搬出確認リスト出力画面のInitサービス処理クラス。　 
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3040Sc002InitService extends SkfServiceAbstract<Skf3040Sc002InitDto> {
	
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
	public Skf3040Sc002InitDto index(Skf3040Sc002InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF3040_SC002_TITLE);
 		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, FunctionIdConstant.SKF3040_SC002);
		
		// システム処理年月取得
		String sysProcessDate = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu();
		
		// エラーの設定を念のため初期化
		initDto.setCarryingInOutTermFromErr(null);
		initDto.setCarryingInOutTermToErr(null);
		
		// 希望日の設定
		if(true == NfwStringUtils.isNotEmpty(sysProcessDate)){
			// 希望日From
			String startDate = sysProcessDate + "01";
			initDto.setCarryingInOutTermFrom(startDate);
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
			initDto.setCarryingInOutTermTo(endDate);
		}
		
		// 出力状況の設定（出力済みを含まない）
		initDto.setOutSituation(CodeConstant.NO);
		
		return initDto;
	}
	
}
