/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc001;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc001.Skf3022Sc001SearchAsyncDto;
import jp.co.intra_mart.common.platform.log.Logger;


/**
 * Skf3022Sc001SearchAsyncService 社宅部屋情報取得非同期処理クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3022Sc001SearchAsyncService
		extends AsyncBaseServiceAbstract<Skf3022Sc001SearchAsyncDto> {
	@Autowired
	private Skf3022Sc001SharedService skf3022Sc001SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	/** ロガー。 */
	private static Logger logger = LogUtils.getLogger(SkfFileOutputUtils.class);

	@Value("${skf3022.skf3022_sc001.max_search_count}")
	private String maxCount;
	/**
	 * 駐車場情報の取得サービス
	 */
	@Override
	public AsyncBaseDto index(Skf3022Sc001SearchAsyncDto asyncDto) throws Exception {

		// デバッグログ
		logger.info("検索");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("検索", CodeConstant.C001, FunctionIdConstant.SKF3022_SC001);

		// リストデータ取得用
		List<Map<String, Object>> roomList = new ArrayList<Map<String, Object>>();

		// 社宅部屋検索
		int count = skf3022Sc001SharedService.getShatakuRoom(
				asyncDto.getSc001KikakuSelecte(), asyncDto.getShatakuName(),
				asyncDto.getSc001YoutoSelect(), asyncDto.getRoomNo(),
				asyncDto.getSc001EmptyRoomSelect(), asyncDto.getSc001EmptyParkingSelect(), roomList);
		if (count == 0) {
			ServiceHelper.addWarnResultMessage(asyncDto, MessageIdConstant.W_SKF_1007);
			throwBusinessExceptionIfErrors(asyncDto.getResultMessages());
		} else if (count == -1) {
			ServiceHelper.addErrorResultMessage(asyncDto, null, MessageIdConstant.E_SKF_1046, maxCount);
			throwBusinessExceptionIfErrors(asyncDto.getResultMessages());
		}

		asyncDto.setListTableList(roomList);
		asyncDto.setDataCount(count);
		LogUtils.debugByMsg("社宅部屋情報：" + count + "件");
		return asyncDto;
	}
}
