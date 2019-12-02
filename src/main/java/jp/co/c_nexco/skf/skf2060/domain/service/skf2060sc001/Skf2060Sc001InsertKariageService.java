/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc001;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc001.Skf2060Sc001InsertKariageDto;

/**
 * TestPrjTop画面のInsertKariageサービス処理クラス。　 
 * 
 */
@Service
public class Skf2060Sc001InsertKariageService extends BaseServiceAbstract<Skf2060Sc001InsertKariageDto> {
	
	@Autowired
	private Skf2060Sc001SharedService skf2060Sc001SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	
	private String companyCd = CodeConstant.C001;
	
	/**
	 * サービス処理を行う。　
	 * 
	 * @param insertDto DTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2060Sc001InsertKariageDto index(Skf2060Sc001InsertKariageDto insertDto) throws Exception {
		
		insertDto.setPageTitleKey(MessageIdConstant.SKF2060_SC001_TITLE);
		
		// 操作ログを出力
		skfOperationLogUtils.setAccessLog("追加", companyCd, insertDto.getPageId());
		
		boolean insertCheck = false;
		
		//借上候補物件テーブルへ登録
		insertCheck = skf2060Sc001SharedService.insertKariageKohoInfo(companyCd, insertDto.getShatakuName(), insertDto.getAddress());
		
		//登録判定
		if(insertCheck){
			//登録できた場合
			//入力欄の「借上社宅名」と「社宅所在地」を空白にする
			insertDto.setShatakuName("");
			insertDto.setPostalCd("");
			insertDto.setAddress("");
		}else{
			//登録できなかった場合、エラーメッセージ
			ServiceHelper.addErrorResultMessage(insertDto, null, MessageIdConstant.E_SKF_1073);
		}
		
		// リストデータ取得用
		List<Map<String, Object>> dataParamList = new ArrayList<Map<String, Object>>();
		boolean itiranFlg = true;
		dataParamList = skf2060Sc001SharedService.getDataParamList(itiranFlg, insertDto.getShainNo(), insertDto.getApplNo());
		
		//更新日を設定
		Map<String, Date> lastUpdateDateMap = new HashMap<String, Date>();
		for(Map<String, Object> dataParam:dataParamList){
			Date lastUpdateDate = skfDateFormatUtils.formatStringToDate(dataParam.get("lastUpdateDate").toString());
			lastUpdateDateMap.put(insertDto.KariageBukkenLastUpdateDate + dataParam.get("candidateNo").toString(), lastUpdateDate);
		}
		
		insertDto.setListTableData(dataParamList);
		
		return insertDto;
	}
	
}
