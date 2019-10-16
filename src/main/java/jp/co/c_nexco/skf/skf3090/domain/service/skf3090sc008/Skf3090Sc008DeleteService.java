/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc008;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010TInformationKey;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010TInformationRepository;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc008.Skf3090Sc008DeleteDto;

/**
 * お知らせメンテナンス画面のDeleteサービス処理クラス。　 
 * 
 */
@Service
public class Skf3090Sc008DeleteService extends BaseServiceAbstract<Skf3090Sc008DeleteDto> {
	
	@Autowired
	private Skf1010TInformationRepository skf1010TInformationRepository;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3090Sc008SharedService skf3090Sc008SharedService;
	// 会社コード
	private String companyCd = CodeConstant.C001;

	
	/**
	 * サービス処理を行う。　
	 * 
	 * @param deleteDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public Skf3090Sc008DeleteDto index(Skf3090Sc008DeleteDto deleteDto) throws Exception {
		
		deleteDto.setPageTitleKey(MessageIdConstant.SKF3090_SC008_TITLE);
		
		// 操作ログを出力
		skfOperationLogUtils.setAccessLog("削除", companyCd, deleteDto.getPageId());
		
		String openDate = deleteDto.getHdnOpenDate().replace("/", "");
		
		//お知らせテーブルから削除
		boolean deleteCheck = this.deleteInformationData(companyCd, openDate);
		//削除失敗
		if(!(deleteCheck)){
	 		ServiceHelper.addErrorResultMessage(deleteDto, null, MessageIdConstant.E_SKF_1076);
	 		throwBusinessExceptionIfErrors(deleteDto.getResultMessages());
		}
		
		//お知らせリストテーブルの設定
		List<Map<String, Object>> informationdataListMap = skf3090Sc008SharedService.getInformationDataListMap(companyCd);
		deleteDto.setListTableData(informationdataListMap);
		
		return deleteDto;
	}
	
	/**
	 * お知らせテーブルへ削除を行う
	 * 
	 * @param companyCd
	 * @param openDate
	 * 
	 * @return 削除できた場合true　削除できなかった場合false
	 */
	public boolean deleteInformationData(String companyCd, String openDate){

		Skf1010TInformationKey key = new Skf1010TInformationKey();
		key.setCompanyCd(companyCd);
		key.setOpenDate(openDate);
		int deleteCount = skf1010TInformationRepository.deleteByPrimaryKey(key);
		
		if (deleteCount <= 0) {
			return false;
		}
		return true;
	}
	
}
