/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc001;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc001.Skf3090Sc001InitDto;

/**
 * 現物支給価額マスタ一覧画面のInitサービス処理クラス。　 
 * 
 */
@Service
public class Skf3090Sc001InitService extends BaseServiceAbstract<Skf3090Sc001InitDto> {
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	@Autowired
	private Skf3090Sc001SharedService skf3090Sc001SharedService;
	
	
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
	public Skf3090Sc001InitDto index(Skf3090Sc001InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF3090_SC001_TITLE);
 		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, initDto.getPageId());
		
		// 現物支給価額マスタ一覧を取得
		skf3090Sc001SharedService.getGenbutsuShikyuKagakuDataInfo(initDto);
		
		return initDto;
	}
	
	
	
	
}
