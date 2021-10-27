/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc008;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc008.Skf2100Sc008AttachedFileDeleteDto;

/**
 * Skf2100Sc008AttachedFileDeleteService 補足ファイル削除処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2100Sc008AttachedFileDeleteService extends SkfServiceAbstract<Skf2100Sc008AttachedFileDeleteDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfDropDownUtils skfDropDownUtils;
	
	/**
	 * サービス処理を行う。
	 * 
	 * @param deleteDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2100Sc008AttachedFileDeleteDto index(Skf2100Sc008AttachedFileDeleteDto deleteDto) throws Exception {

		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("補足資料削除", CodeConstant.C001, FunctionIdConstant.SKF2100_SC008);
				
		//画面入力内容情報の保持
		// ドロップダウンリストを設定
		List<Map<String, Object>> contractKbnDropDownList = skfDropDownUtils
				.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_ROUTER_CONTRACT_KBN, deleteDto.getContractKbnSelect(), true);
		deleteDto.setContractKbnDropDownList(contractKbnDropDownList);
		if(!"true".equals(deleteDto.getHdnChkFaultSelect())){
			deleteDto.setHdnChkFaultSelect(null);
		}
		
		//ファイル名
		String fileName = CodeConstant.DOUBLE_QUOTATION;
		//リンクID
		String hosokuLink = CodeConstant.DOUBLE_QUOTATION;
		//ファイルサイズ
		String fileSize = CodeConstant.DOUBLE_QUOTATION;
		//ファイル情報
		byte[] fileStream = null;
		//ファイル番号
		String fileNo = deleteDto.getFileNo();

		
		//ファイル情報をDTOクリア
		switch (fileNo){
		case "1":
			deleteDto.setHosokuFileName1(fileName);
			deleteDto.setHosokuLink1(hosokuLink);
			deleteDto.setHosokuSize1(fileSize);
			deleteDto.setHosokuFile1(fileStream);
			break;
		case "2":
			deleteDto.setHosokuFileName2(fileName);
			deleteDto.setHosokuLink2(hosokuLink);
			deleteDto.setHosokuSize2(fileSize);
			deleteDto.setHosokuFile2(fileStream);
			break;
		case "3":
			deleteDto.setHosokuFileName3(fileName);
			deleteDto.setHosokuLink3(hosokuLink);
			deleteDto.setHosokuSize3(fileSize);
			deleteDto.setHosokuFile3(fileStream);
			break;
		}


		return deleteDto;
	}
}
