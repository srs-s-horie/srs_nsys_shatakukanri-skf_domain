/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.service.skf3020sc004;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3020.domain.dto.skf3020sc004.Skf3020Sc004NewDto;

/**
 * Skf3020Sc004RegistService 転任者一覧画面のNewサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3020Sc004Newservice extends BaseServiceAbstract<Skf3020Sc004NewDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;	

	/**
	 * サービス処理を行う。
	 * （転任者情報登録画面への遷移のみのため、操作ログのみ）
	 * 
	 * @param registDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf3020Sc004NewDto index(Skf3020Sc004NewDto newDto) throws Exception {
		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("新規", CodeConstant.C001, newDto.getPageId());		
		
		//社員番号リセット
		newDto.setHdnShainNo("");
		
		return newDto;
	}
	

}
