/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.service.skf3020sc002;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3020.domain.dto.skf3020sc002.Skf3020Sc002InitDto;

/**
  * Skf3020Sc002 転任者調書取込初期表示処理クラス
  *
  * @author NEXCOシステムズ
 */
@Service
public class Skf3020Sc002InitService extends BaseServiceAbstract<Skf3020Sc002InitDto> {
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	
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
	public Skf3020Sc002InitDto index(Skf3020Sc002InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF3020_SC002_TITLE);
		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, initDto.getPageId());
		
		// 遷移元画面が「転任者一覧」の場合、「前に戻る」ボタンを表示する。
		if (NfwStringUtils.isNotEmpty(initDto.getPrePageId())
				&& initDto.getPrePageId().equals(FunctionIdConstant.SKF3020_SC004)) {
			initDto.setBackBtnHiddenFlg(false);
			
		} else {
			initDto.setBackBtnHiddenFlg(true);
		}
 		
		return initDto;
	}
	
}
