/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2050.domain.service.skf2050sc001;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationGuideUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2050.domain.dto.skf2050sc001.Skf2050Sc001InitDto;

/**
 * Skf2050Sc001 備品返却申請（申請者用)初期表示処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2050Sc001InitService extends BaseServiceAbstract<Skf2050Sc001InitDto> {

	@Autowired
	private Skf2050Sc001SharedService skf2050Sc001SharedService;

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfOperationGuideUtils skfOperationGuideUtils;

	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2050Sc001InitDto index(Skf2050Sc001InitDto initDto) throws Exception {
		// タイトル設定
		initDto.setPageTitleKey(MessageIdConstant.SKF2050_SC001_TITLE);
		// 操作ログ出力
		skfOperationLogUtils.setAccessLog("初期表示処理開始", CodeConstant.C001, FunctionIdConstant.SKF2050_SC001);

		// 画面情報の設定を行う
		if (!skf2050Sc001SharedService.setDisplayData(initDto)) {
			setErr(initDto);
		}
		// 操作ガイド設定
		String operationGuide = skfOperationGuideUtils.getOperationGuide(FunctionIdConstant.SKF2050_SC001);
		initDto.setOperationGuide(operationGuide);

		return initDto;
	}

	/**
	 * 
	 * @param initDto
	 */
	private void setErr(Skf2050Sc001InitDto initDto) {
		// 「同意する」「同意しない」「搬出完了」ボタンを非活性化
		initDto.setMaskPattern("ERR");
		return;

	}

}
