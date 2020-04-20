/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc001;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.nfw.webcore.utils.filetransfer.FileOutput;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc001.Skf2060Sc001DownloadDto;

/**
 * TestPrjTop画面のDownloadサービス処理クラス。　 
 * 
 */
@Service
public class Skf2060Sc001DownloadService extends SkfServiceAbstract<Skf2060Sc001DownloadDto> {

	@Autowired
	private Skf2060Sc001SharedService skf2060Sc001SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	
	private String companyCd = CodeConstant.C001;
	@Value("${skf2060.skf2060_sc001.preFileName}")
	private String preFileName;
	@Value("${skf2060.skf2060_sc001.fileStartLine}")
	private Integer fileStartLine;
	

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
			String[] csvData = new String[4];
			//提示ステータス
			csvData[0] = "";
			if(mapData.get("teijiFlg") != null){
				String teijiStatus = "";
				switch(mapData.get("teijiFlg").toString()){
				case "0":
					teijiStatus = "提示可";
					break;
				case "1":
					teijiStatus = "提示中";
					break;
				case "2":
					teijiStatus = "選択済";
					break;				
				}
				csvData[0] = teijiStatus;
			}
			//登録日
			csvData[1] = "";
			if(mapData.get("insertDate") != null){
				csvData[1] = skfDateFormatUtils.dateFormatFromString(mapData.get("insertDate").toString(), "yyyyMMdd");
			}
			//借上所在地
			csvData[2] = "";
			if(mapData.get("shatakuName") != null){
				csvData[2] = mapData.get("shatakuName").toString();
			}
			//社宅所在地
			csvData[3] = "";
			if(mapData.get("address") != null){
				csvData[3] = mapData.get("address").toString();
			}
			rowdatas.add(csvData);
		}
		
		List<String> messageList = new ArrayList<String>();
		
		String fileName = DateTime.now().toString("YYYYMMddHHmmss") + preFileName;
		
		//CSV出力を行う
		//SkfFileOutputUtils.fileOutputCsv(rowdatas, "skf2060.skf2060_sc001.templateFileName", "skf2060fl001", fileStartLine, messageList, null);
		FileOutput.fileOutputCsv(rowdatas, "skf2060.skf2060_sc001.templateFileName", "skf2060fl001", 1, messageList, downloadDto);
		downloadDto.setUploadFileName(fileName);

		return downloadDto;
	}
	
}
