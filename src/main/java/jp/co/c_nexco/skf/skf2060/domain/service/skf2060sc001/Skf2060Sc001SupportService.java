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

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetApplHistoryExp;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc001.Skf2060Sc001SupportDto;

/**
 * TestPrjTop画面のSupportサービス処理クラス。　 
 * 
 */
@Service
public class Skf2060Sc001SupportService extends BaseServiceAbstract<Skf2060Sc001SupportDto> {
	
	@Autowired
	private Skf2060Sc001SharedService skf2060Sc001SharedService;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
    @Autowired
    private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	
	private String companyCd = CodeConstant.C001;
	

	/**
	 * サービス処理を行う。　
	 * 
	 * @param supportDto DTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2060Sc001SupportDto index(Skf2060Sc001SupportDto supportDto) throws Exception {
		
		supportDto.setPageTitleKey(MessageIdConstant.SKF2060_SC001_TITLE);
		
		// 操作ログを出力
		skfOperationLogUtils.setAccessLog("支援", CodeConstant.C001, FunctionIdConstant.SKF2060_SC001);
		
		// リストデータ取得用
		Skf2060Sc001GetApplHistoryExp resultData = new Skf2060Sc001GetApplHistoryExp();
		//会社コードと社員番号からデータ取得
		resultData = skf2060Sc001SharedService.getApplHistoryInfo(companyCd, supportDto.getShainNo(), null);
		//取得できた場合
		if(resultData != null){
			// 提示状況汎用コード取得
	        Map<String, String> candidateStatusGenCodeMap = new HashMap<String, String>();
	        candidateStatusGenCodeMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_STATUS);
			//DateからStringへ
			String presentedDate = skfDateFormatUtils.dateFormatFromDate(resultData.getApplDate(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			//取得した値をDtoに設定
			supportDto.setPresentedStatus(candidateStatusGenCodeMap.get(resultData.getApplStatus()));
			supportDto.setPresentedDate(presentedDate);	
			// 隠し要素に設定
			supportDto.setApplNo(resultData.getApplNo());
			// 隠し要素に更新日時を設定
			Date updateDate = new Date();
			String updateDateFormat = skfDateFormatUtils.dateFormatFromDate(updateDate, SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			supportDto.setUpdateDate(updateDateFormat);
		}
		
		// リストデータ取得用
		List<Map<String, Object>> dataParamList = new ArrayList<Map<String, Object>>();
		boolean itiranFlg = true;
		dataParamList = skf2060Sc001SharedService.getDataParamList(itiranFlg, supportDto.getShainNo(), supportDto.getApplNo());
		supportDto.setListTableData(dataParamList);
		return supportDto;
	}
	
}
