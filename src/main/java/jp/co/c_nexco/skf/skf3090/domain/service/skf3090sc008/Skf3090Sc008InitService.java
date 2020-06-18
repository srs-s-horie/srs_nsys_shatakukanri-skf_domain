/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc008;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010TInformation;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationGuideUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc008.Skf3090Sc008InitDto;

/**
 * お知らせメンテナンス画面のInitサービス処理クラス。　 
 * 
 */
@Service
public class Skf3090Sc008InitService extends SkfServiceAbstract<Skf3090Sc008InitDto> {
	

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfOperationGuideUtils skfOperationGuideUtils;
	@Autowired
	private Skf3090Sc008SharedService skf3090Sc008SharedService;
	// 会社コード
	private String companyCd = CodeConstant.C001;
	
	//リストテーブル表示最大行数
	@Value("${skf3090.skf3090_sc008.max_row_count}")
	private String listTableMaxRowCount;

	
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
	public Skf3090Sc008InitDto index(Skf3090Sc008InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF3090_SC008_TITLE);
		
		// 操作ログを出力
		skfOperationLogUtils.setAccessLog("初期表示", companyCd, FunctionIdConstant.SKF3090_SC008);
		// 操作ガイドの設定
		initDto.setOperationGuide(skfOperationGuideUtils.getOperationGuide(FunctionIdConstant.SKF3090_SC008));
		
		// 項目初期化
		initDto.setOpenDateBox(CodeConstant.NONE);
		initDto.setNote(CodeConstant.NONE);

		
		//更新日を設定
		Map<String, Date> lastUpdateDateMap = new HashMap<String, Date>();
 		
		//お知らせリストテーブルの設定
		List<Map<String, Object>> informationdataListMap = skf3090Sc008SharedService.getInformationDataListMap(companyCd);
		for(Map<String, Object> informationdataMap:informationdataListMap){
			String openDate = informationdataMap.get("openDate").toString().replace("/", "");
			Skf1010TInformation informationData = skf3090Sc008SharedService.getInformation(companyCd, openDate);
			//お知らせテーブル用更新日
			lastUpdateDateMap.put(initDto.informationLastUpdateDate + openDate, informationData.getUpdateDate());
		}
		
		initDto.setListTableData(informationdataListMap);
		initDto.setListTableMaxRowCount(listTableMaxRowCount);
		
		//更新日設定
		initDto.setLastUpdateDateMap(lastUpdateDateMap);
		
		return initDto;
	}
	
}
