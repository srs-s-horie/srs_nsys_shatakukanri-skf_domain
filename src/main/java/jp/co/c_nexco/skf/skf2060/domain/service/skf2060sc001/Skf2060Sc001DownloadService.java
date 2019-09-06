/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc001;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.nfw.webcore.utils.filetransfer.FileOutput;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc001.Skf2060Sc001DownloadDto;

/**
 * TestPrjTop画面のDownloadサービス処理クラス。　 
 * 
 */
@Service
public class Skf2060Sc001DownloadService extends BaseServiceAbstract<Skf2060Sc001DownloadDto> {

	@Autowired
	private Skf2060Sc001SharedService skf2060Sc001SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	
	private String companyCd = CodeConstant.C001;
	private String fileName = "skf2060.skf2060_sc001.FileId";
	

	/**
	 * サービス処理を行う。　
	 * 
	 * @param downloadDto　DTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2060Sc001DownloadDto index(Skf2060Sc001DownloadDto downloadDto) throws Exception {
		
		// 操作ログを出力
		skfOperationLogUtils.setAccessLog("CSV出力", companyCd, downloadDto.getPageId());
		
		// リストデータ取得用
		boolean itiranFlg = true;
		List<Map<String, Object>> dataParamList = new ArrayList<Map<String, Object>>();
		dataParamList = skf2060Sc001SharedService.getDataParamList(itiranFlg, downloadDto.getShainNo(), downloadDto.getApplNo());
		
		//リストデータが存在しない場合
		if(dataParamList.size() <= 0){
			//エラーメッセージ出力
			ServiceHelper.addErrorResultMessage(downloadDto, null, MessageIdConstant.E_SKF_1019);
			throwBusinessExceptionIfErrors(downloadDto.getResultMessages());
		}
		
		//CSVデータ作成
		List<String[]> rowdatas = new ArrayList<String[]>();
		for(Map<String, Object> mapData : dataParamList){
			
			String[] csvData = new String[]{mapData.get("insertDate").toString(), (String)mapData.get("candidateDate"), mapData.get("shatakuName").toString(),
					mapData.get("address").toString(), mapData.get("attachedName").toString()};
			rowdatas.add(csvData);
		}
		//CSV出力を行う
		FileOutput.fileOutputCsv(rowdatas, fileName, "skf2060r0106", 1, null, downloadDto);
		
		return downloadDto;
	}
	
}
