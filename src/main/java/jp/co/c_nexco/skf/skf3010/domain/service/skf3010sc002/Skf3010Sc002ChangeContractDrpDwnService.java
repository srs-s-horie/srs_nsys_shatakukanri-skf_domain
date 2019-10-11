/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc002;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc002common.Skf3010Sc002CommonDto;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc002.Skf3010Sc002ChangeContractDrpDwnDto;
import jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc002.Skf3010Sc002SharedService;
import jp.co.intra_mart.common.platform.log.Logger;

/**
 * Skf3010Sc002ChangeContractDrpDwnService 保有社宅登録の契約情報プルダウン変更サービス処理クラス。
 * 同期処理
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3010Sc002ChangeContractDrpDwnService extends BaseServiceAbstract<Skf3010Sc002ChangeContractDrpDwnDto> {

	@Autowired
	private Skf3010Sc002SharedService skf3010Sc002SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	/** ロガー。 */
	private static Logger logger = LogUtils.getLogger(SkfFileOutputUtils.class);

	/**
	 * サービス処理を行う。　
	 * 
	 * @param initDto	インプットDTO
	 * @return 処理結果
	 * @throws Exception	例外
	 */
	@Override
	public Skf3010Sc002ChangeContractDrpDwnDto index(Skf3010Sc002ChangeContractDrpDwnDto initDto) throws Exception {
		// デバッグログ
		logger.info("契約情報プルダウン変更");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("契約情報プルダウン変更", CodeConstant.C001, initDto.getPageId());

		// 契約情報変更モード
		String selectMode = Skf3010Sc002CommonDto.CONTRACT_MODE_CHANGE;
		// 選択タブインデックス
		String selectTabIndex = initDto.getHdnNowSelectTabIndex();
		// 保有社宅登録情報設定
		skf3010Sc002SharedService.setHoyuShatakuInfo(selectMode, initDto.getHdnChangeContractSelectedIndex(), initDto);

		// 選択タブインデックス(契約情報タブ)
//		initDto.setHdnNowSelectTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_CONTRACT);
		initDto.setHdnNowSelectTabIndex(selectTabIndex);

		return initDto;
	}
}
