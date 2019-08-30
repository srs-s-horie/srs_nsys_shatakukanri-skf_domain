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
import jp.co.c_nexco.nfw.webcore.utils.filetransfer.FileOutput;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc001.Skf2060Sc001DownloadDto;

/**
 * TestPrjTop画面のInitサービス処理クラス。　 
 * 
 */
@Service
public class Skf2060Sc001DownloadService extends BaseServiceAbstract<Skf2060Sc001DownloadDto> {

	@Autowired
	private Skf2060Sc001SharedService skf2060Sc001SharedService;

	
	private String companyCd = CodeConstant.C001;
	private String fileName = "skf2060.skf2060_sc001.FileId";
	

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
	public Skf2060Sc001DownloadDto index(Skf2060Sc001DownloadDto downloadDto) throws Exception {
		
		//TODO 操作ログ
		
		// リストデータ取得用
		boolean itiranFlg = true;
		List<Map<String, Object>> dataParamList = new ArrayList<Map<String, Object>>();
		dataParamList = skf2060Sc001SharedService.getDataParamList(itiranFlg);
		
		//CSVデータ作成
		List<String[]> rowdatas = new ArrayList<String[]>();
		for(Map<String, Object> mapData : dataParamList){
			
			String[] csvData = new String[]{mapData.get("insertDate").toString(), (String)mapData.get("candidateDate"), mapData.get("shatakuName").toString(),
					mapData.get("address").toString(), mapData.get("attachedName").toString()};
			rowdatas.add(csvData);
		}
		
		//Map<String, Object> resultMap = new HashMap<String, Object>();
		
		//SkfFileOutputUtils.fileOutputCsv(rowdatas, fileName, "skf2060r0106", 1, null, resultMap);
		FileOutput.fileOutputCsv(rowdatas, fileName, "skf2060r0106", 1, null, downloadDto);
		
		//downloadDto.setFileData((byte[])resultMap.get("fileData"));
		//downloadDto.setUploadFileName(resultMap.get("uploadFileName").toString());
		//downloadDto.setViewPath(resultMap.get("viewPath").toString());
		
		return downloadDto;
	}
	
}
