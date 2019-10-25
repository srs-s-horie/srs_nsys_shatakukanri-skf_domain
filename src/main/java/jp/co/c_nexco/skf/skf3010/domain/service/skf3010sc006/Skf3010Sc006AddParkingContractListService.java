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
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc006.Skf3010Sc006AddParkingContractListDto;
import jp.co.intra_mart.common.platform.log.Logger;

/**
 * Skf3010Sc006AddParkingContractListService 保有社宅登録の駐車場契約情報追加ボタン押下サービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3010Sc006AddParkingContractListService extends BaseServiceAbstract<Skf3010Sc006AddParkingContractListDto> {

	@Autowired
	private Skf3010Sc006SharedService skf3010Sc006SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	/** ロガー。 */
	private static Logger logger = LogUtils.getLogger(SkfFileOutputUtils.class);

	/**
	 * 保有社宅登録の契約情報追加ボタン押下時処理を行う。　
	 * 
	 * @param initDto	インプットDTO
	 * @return 処理結果
	 * @throws Exception	例外
	 */
	@Override
	public Skf3010Sc006AddParkingContractListDto index(Skf3010Sc006AddParkingContractListDto initDto) throws Exception {
		// デバッグログ
		logger.info("契約情報追加");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("契約情報追加", CodeConstant.C001, initDto.getPageId());

		skf3010Sc006SharedService.setShatakuInfoDropDownStay(initDto);
		// 契約情報変更モード
		String selectMode = initDto.getParkingSelectMode();
		// 選択タブインデックス
		String selectTabIndex = initDto.getHdnNowSelectTabIndex();
		// 保有社宅登録情報設定
		skf3010Sc006SharedService.changePakingContractInfo(selectMode, initDto.getHdnChangeContractSelectedIndex(),initDto.getHdnDispParkingContractSelectedIndex(), initDto);

		// 選択タブインデックス(契約情報タブ)
//		initDto.setHdnNowSelectTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_CONTRACT);
		initDto.setHdnNowSelectTabIndex(selectTabIndex);


		return initDto;
	}
}