/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc002;

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
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc002.Skf3022Sc002SearchAsyncDto;


/**
 * Skf3022Sc002SearchAsyncService 駐車場情報取得非同期処理クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3022Sc002SearchAsyncService
		extends AsyncBaseServiceAbstract<Skf3022Sc002SearchAsyncDto> {
	@Autowired
	private Skf3022Sc002SharedService skf3022Sc002SharedService;
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	
	@Value("${skf3022.skf3022_sc002.max_search_count}")
	private String maxCount;
	/**
	 * 駐車場情報の取得サービス
	 */
	@Override
	public AsyncBaseDto index(Skf3022Sc002SearchAsyncDto asyncDto) throws Exception {
		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("検索", CodeConstant.C001, FunctionIdConstant.SKF3022_SC002);
				
		// リストデータ取得用
		List<Map<String, Object>> parkingInfoList = new ArrayList<Map<String, Object>>();
		
		//チェック状態
		boolean chushajo = false;
		LogUtils.debugByMsg("空き駐車場："+asyncDto.getAkiParking());
		if("1".equals(asyncDto.getAkiParking())){
			chushajo = true;
		}
		boolean nidaishiyo = false;
		
		//TODO デバッグコード消す
//		asyncDto.setShatakuNo("2015120071");
//		asyncDto.setKaisiDate("20181010");
		//デバッグコードここまで
		
		// 駐車場情報一覧検索
		int count = skf3022Sc002SharedService.getChushajoInfo(asyncDto.getShatakuNo(), asyncDto.getShiyosha(),
				asyncDto.getParkingBiko(), chushajo, nidaishiyo, asyncDto.getKaisiDate(), 
				skfBaseBusinessLogicUtils.getSystemProcessNenGetsu(), parkingInfoList);
		if (count == 0) {
			ServiceHelper.addResultMessage(asyncDto, MessageIdConstant.W_SKF_1007);
		} else if (count == -1) {
			ServiceHelper.addResultMessage(asyncDto, MessageIdConstant.E_SKF_1046, maxCount);
		}
		
		asyncDto.setListTableList(parkingInfoList);
		asyncDto.setDataCount(count);
		LogUtils.debugByMsg("駐車場情報：" + count + "件");
		return asyncDto;
	}

}
