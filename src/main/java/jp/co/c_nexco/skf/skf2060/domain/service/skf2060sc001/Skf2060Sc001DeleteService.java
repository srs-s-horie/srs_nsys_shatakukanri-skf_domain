/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc001;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc003.Skf2010Sc003DeleteDocTableExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc003.Skf2010Sc003GetApplHistoryStatusInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc003.Skf2010Sc003GetApplHistoryStatusInfoForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001DeleteKariageBukkenExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc001.Skf2060Sc001DeleteKariageBukkenExpRepository;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc001.Skf2060Sc001DeleteDto;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc001.Skf2060Sc001InitDto;

/**
 * TestPrjTop画面のInitサービス処理クラス。　 
 * 
 */
@Service
public class Skf2060Sc001DeleteService extends BaseServiceAbstract<Skf2060Sc001DeleteDto> {
	
	@Autowired
	private Skf2060Sc001SharedService skf2060Sc001SharedService;
	@Autowired
	private Skf2060Sc001DeleteKariageBukkenExpRepository skf2060Sc001DeleteKariageBukkenExpRepository;
	
	private String companyCd = CodeConstant.C001;
	
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
	public Skf2060Sc001DeleteDto index(Skf2060Sc001DeleteDto deleteDto) throws Exception {
		
		deleteDto.setPageTitleKey(MessageIdConstant.SKF2060_SC001_TITLE);
		
		Long candidateNo = Long.parseLong(deleteDto.getHdnCandidateNo());
		//対象の借上候補物件がデータベースの存在するか(排他チェック？）
		boolean getCheck = skf2060Sc001SharedService.getKariageBukken(deleteDto.getHdnCompanyCd(), candidateNo);
		//対象の借上候補物件が存在する場合
 		if(getCheck){
 			//対象の借上候補物件を削除する
 			boolean deleteCheck = deleteKariageBukken(deleteDto.getHdnCompanyCd(), candidateNo);
 			//削除が成功した場合
 			if(deleteCheck){
 				System.out.println("よっしゃああああああああああ");
 			//削除が失敗した場合
 			}else{
 				
 			}
 		//対象の借上候補物件が存在しない場合
 		}else{
 			
 		}
 		
		// リストデータ取得用
		List<Map<String, Object>> dataParamList = new ArrayList<Map<String, Object>>();
		boolean itiranFlg = true;
		dataParamList = skf2060Sc001SharedService.getDataParamList(itiranFlg);
		deleteDto.setListTableData(dataParamList);
		
		return deleteDto;
	}
	
	public boolean deleteKariageBukken(String companyCd, Long candidateNo){
		int deleteCount = 0;
		boolean deleteCheck = true;
		Skf2060Sc001DeleteKariageBukkenExpParameter param = new Skf2060Sc001DeleteKariageBukkenExpParameter();
		
		param.setCompanyCd(companyCd);
		param.setCandidateNo(candidateNo);
		deleteCount = skf2060Sc001DeleteKariageBukkenExpRepository.deleteKariageBukken(param);
		if (deleteCount <= 0) {
			deleteCheck = false;
		}
		return deleteCheck;
	}
}