/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc008;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010TInformation;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc008.Skf3090Sc008EditDto;

/**
 * お知らせマスタメンテナンス画面のEditサービス処理クラス。　 
 * 
 */
@Service
public class Skf3090Sc008EditService extends BaseServiceAbstract<Skf3090Sc008EditDto> {
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3090Sc008SharedService skf3090Sc008SharedService;
	// 会社コード
	private String companyCd = CodeConstant.C001;

	
	/**
	 * サービス処理を行う。　
	 * 
	 * @param editDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public Skf3090Sc008EditDto index(Skf3090Sc008EditDto editDto) throws Exception {
		
		// 操作ログを出力
		skfOperationLogUtils.setAccessLog("編集", companyCd, editDto.getPageId());
		
		//「編集」ボタンを押した行の公開開始日を取得
		String openDate = editDto.getHdnOpenDate(); 
        
		Skf1010TInformation informationData = skf3090Sc008SharedService.getInformation(companyCd, openDate.replace("/", ""));
		
		if(informationData != null){
			//「公開開始日」に取得した公開開始日を設定
			editDto.setOpenDateBox(informationData.getOpenDate());
			//テキストエディターに取得した内容を設定
			editDto.setNote(informationData.getNote());
		}else{
	 		ServiceHelper.addErrorResultMessage(editDto, null, MessageIdConstant.E_SKF_1078);
	 		throwBusinessExceptionIfErrors(editDto.getResultMessages());
		}
		
		//お知らせリストテーブルの設定
		List<Map<String, Object>> informationdataListMap = skf3090Sc008SharedService.getInformationDataListMap(companyCd);
		editDto.setListTableData(informationdataListMap);
		
		return editDto;
	}
	
}
