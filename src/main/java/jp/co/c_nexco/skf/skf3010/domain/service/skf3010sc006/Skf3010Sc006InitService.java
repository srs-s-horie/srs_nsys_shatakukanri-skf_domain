/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc006;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc002common.Skf3010Sc002CommonDto;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc006common.Skf3010Sc006CommonDto;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc006.Skf3010Sc006InitDto;
import jp.co.intra_mart.common.platform.log.Logger;

/**
 * Skf3010Sc006InitService　借上社宅登録画面のInitサービス処理クラス。　 
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3010Sc006InitService extends BaseServiceAbstract<Skf3010Sc006InitDto> {
	
	@Autowired
	private Skf3010Sc006SharedService skf3010Sc006SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	/** ロガー。 */
	private static Logger logger = LogUtils.getLogger(SkfFileOutputUtils.class);

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
	public Skf3010Sc006InitDto index(Skf3010Sc006InitDto initDto) throws Exception {
		// デバッグログ
		logger.info("初期表示");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, initDto.getPageId());

		// 契約情報変更モード
		//String selectMode = Skf3010Sc006CommonDto.CONTRACT_MODE_INIT;
		// DTO初期化
		// 選択タブインデックス
		initDto.setHdnNowSelectTabIndex(null);
		// 契約情報プルダウン
		initDto.setHdnChangeContractSelectedIndex(null);
		// 削除済み契約番号初期化
		initDto.setHdnDeleteContractSelectedValue(null);

		// 保有社宅登録情報設定
		skf3010Sc006SharedService.setShatakuInfo("", "",initDto);

		// 選択タブインデックス設定：基本情報タブ
		initDto.setHdnNowSelectTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_KIHON);
		initDto.setShatakuKbn("借上");

		return initDto;
	}
	
}
