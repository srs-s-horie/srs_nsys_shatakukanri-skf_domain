/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc003;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc003.Skf3022Sc003ChangeYoutoDrpDwnAsyncDto;
import jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc003.Skf3022Sc003SharedService;
import jp.co.intra_mart.common.platform.log.Logger;

/**
 * Skf3022Sc003ChangeYoutoDrpDwnAsyncService 使用料入力支援画面の用途ドロップダウン変更時サービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3022Sc003ChangeYoutoDrpDwnAsyncService
	extends AsyncBaseServiceAbstract<Skf3022Sc003ChangeYoutoDrpDwnAsyncDto> {

	@Autowired
	private Skf3022Sc003SharedService skf3022Sc003SharedService;
	/** ロガー。 */
	private static Logger logger = LogUtils.getLogger(SkfFileOutputUtils.class);

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
	public Skf3022Sc003ChangeYoutoDrpDwnAsyncDto index(Skf3022Sc003ChangeYoutoDrpDwnAsyncDto asyncDto) throws Exception {

		// デバッグログ
		logger.info("用途変更");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("用途変更", CodeConstant.C001, FunctionIdConstant.SKF3022_SC003);
		Map<String, String> paramMap = null;
		// パラメータ取得
		paramMap = asyncDto.getMapParam();
		// 使用料再計算処理
		skf3022Sc003SharedService.saiKeisan(paramMap);
		setMapToDto(paramMap, asyncDto);
		return asyncDto;
	}


	/**
	 * 再計算結果DTO設定
	 * 再計算処理結果のMapをDTOに設定する
	 * 
	 * 「※」項目はアドレスとして戻り値になる。 
	 * 
	 * @param paramMap	再計算処理結果
	 * @param initDto	*DTO
	 */
	private void setMapToDto(Map<String, String> paramMap, Skf3022Sc003ChangeYoutoDrpDwnAsyncDto asyncDto) {


		asyncDto.setKijunMenseki2(paramMap.get("kijunMenseki2"));
		asyncDto.setShatakuMenseki2(paramMap.get("shatakuMenseki2"));
		asyncDto.setKeinenChouseinashiShiyoryo2(paramMap.get("keinenChouseinashiShiyoryo2"));
		asyncDto.setKijunTanka2(paramMap.get("kijunTanka2"));
		asyncDto.setPatternShiyoryo2(paramMap.get("patternShiyoryo2"));
		asyncDto.setNenreikasanKeisu(paramMap.get("nenreikasanKeisu"));
		asyncDto.setShatakuShiyoryo2(paramMap.get("shatakuShiyoryo2"));
		asyncDto.setSc003HdnRateShienTanka(paramMap.get("tanka"));
		asyncDto.setSc003HdnRateShienKeinen(paramMap.get("santeiKeinen"));
		asyncDto.setSc003HdnRateShienKeinenZankaRitsu(paramMap.get("keinenZankaristu"));
	}
}
