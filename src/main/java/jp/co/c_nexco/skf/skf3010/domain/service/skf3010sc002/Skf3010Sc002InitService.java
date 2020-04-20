/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc002;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc002common.Skf3010Sc002CommonDto;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc002.Skf3010Sc002InitDto;
import jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc002.Skf3010Sc002SharedService;

/**
 * Skf3010Sc002InitService 保有社宅登録のInitサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3010Sc002InitService extends SkfServiceAbstract<Skf3010Sc002InitDto> {

	@Autowired
	private Skf3010Sc002SharedService skf3010Sc002SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	/**
	 * サービス処理を行う。　
	 * 
	 * @param initDto	インプットDTO
	 * @return 処理結果
	 * @throws Exception	例外
	 */
	@Override
	public Skf3010Sc002InitDto index(Skf3010Sc002InitDto initDto) throws Exception {
		// デバッグログ
		LogUtils.debugByMsg("初期表示");
		initDto.setPageId(FunctionIdConstant.SKF3010_SC002);
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, FunctionIdConstant.SKF3010_SC002);

		// DTO初期化
		skf3010Sc002SharedService.initialize(initDto);
		// 選択タブインデックス
		initDto.setHdnNowSelectTabIndex(null);
		// 契約情報プルダウン
		initDto.setHdnChangeContractSelectedIndex(null);
		// 削除済み契約番号初期化
		initDto.setHdnDeleteContractSelectedValue(null);
		// 保有社宅登録情報設定
		skf3010Sc002SharedService.setHoyuShatakuInfo("", initDto);
		// 選択タブインデックス設定：基本情報タブ
		initDto.setHdnNowSelectTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_KIHON);

		return initDto;
	}
}
