/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc002;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc002.Skf2060Sc002GetKariageTeijiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc002.Skf2060Sc002GetKariageTeijiInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2060TKariageBukken;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2060TKariageBukkenKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2060TKariageTeiji;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2060TKariageTeijiDetail;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2060TKariageTeijiDetailKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2060TKariageTeijiKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc002.Skf2060Sc002GetKariageTeijiInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2060TKariageBukkenRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2060TKariageTeijiDetailRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2060TKariageTeijiRepository;

/**
 * Skf2060Sc002SharedService 借上候補物件確認内共通クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf2060Sc002SharedService {


	@Autowired
	private Skf2060Sc002GetKariageTeijiInfoExpRepository skf2060Sc002GetKariageTeijiInfoExpRepository;
	@Autowired
	private Skf2060TKariageBukkenRepository skf2060TKariageBukkenRepository;
	@Autowired
	private Skf2060TKariageTeijiRepository skf2060TKariageTeijiRepository;
	@Autowired
	private Skf2060TKariageTeijiDetailRepository skf2060TKariageTeijiDetailRepository;
	
	/**
	 * 借上候補物件提示情報を取得する
	 * 
	 * @param initDto
	 * @return 借上候補物件提示情報
	 */
	public List<Skf2060Sc002GetKariageTeijiInfoExp> getKariageTeijiInfo(String companyCd, String applNo){
		
		//ここの固定文字列は定数化するかXMLから取得するように変更必要がある by現行
		String applCheckFlgTrue = "1";
		String applCheckFlgFalse = "0";
		String noSelShatakuName = "選択しない";
		
		//借上候補物件情報の取得
		List<Skf2060Sc002GetKariageTeijiInfoExp> kariageTeijiDataList = new ArrayList<Skf2060Sc002GetKariageTeijiInfoExp>();
		Skf2060Sc002GetKariageTeijiInfoExpParameter param = new Skf2060Sc002GetKariageTeijiInfoExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		param.setApplCheckFlgTrue(applCheckFlgTrue);
		param.setApplCheckFlgFalse(applCheckFlgFalse);
		param.setNoSelShatakuName(noSelShatakuName);
		kariageTeijiDataList = skf2060Sc002GetKariageTeijiInfoExpRepository.getKariageTeijiInfo(param);

		return kariageTeijiDataList;
	}
	
	/**
	 * 借上候補物件情報を取得する（更新用)
	 * 
	 * @param companyCd
	 * @param candidateNo
	 * @return 借上候補物件情報
	 */
	public Skf2060TKariageBukken getKariageBukkenForUpdate(String companyCd, long candidateNo){
		
		//借上候補物件情報の取得
		Skf2060TKariageBukken kariageBukkenData = new Skf2060TKariageBukken();
		Skf2060TKariageBukkenKey key = new Skf2060TKariageBukkenKey();
		key.setCompanyCd(companyCd);
		key.setCandidateNo(candidateNo);
		kariageBukkenData = skf2060TKariageBukkenRepository.selectByPrimaryKey(key);

		return kariageBukkenData;
	}
	
	/**
	 * 借上候補物件提示情報を取得する（更新用)
	 * 
	 * @param companyCd
	 * @param candidateNo
	 * @return 借上候補物件提示情報
	 */
	public Skf2060TKariageTeiji getKariageTeijiForUpdate(String companyCd, String applNo, short teijiKaisu){
		
		//借上候補物件情報の取得
		Skf2060TKariageTeiji kariageTeijiData = new Skf2060TKariageTeiji();
		Skf2060TKariageTeijiKey key = new Skf2060TKariageTeijiKey();
		key.setCompanyCd(companyCd);
		key.setApplNo(applNo);
		key.setTeijiKaisu(teijiKaisu);
		kariageTeijiData = skf2060TKariageTeijiRepository.selectByPrimaryKey(key);

		return kariageTeijiData;
	}
	
	/**
	 * 借上候補物件提示明細情報を取得する（更新用)
	 * 
	 * @param companyCd
	 * @param applNo
	 * @param teijiKaisu
	 * @param candidateNo
	 * @return 借上候補物件提示明細情報
	 */
	public Skf2060TKariageTeijiDetail getKariageTeijiDetailForUpdate(String companyCd, String applNo, short teijiKaisu, long candidateNo){
		
		//借上候補物件情報の取得
		Skf2060TKariageTeijiDetail kariageTeijiDetailData = new Skf2060TKariageTeijiDetail();
		Skf2060TKariageTeijiDetailKey key = new Skf2060TKariageTeijiDetailKey();
		key.setCompanyCd(companyCd);
		key.setApplNo(applNo);
		key.setTeijiKaisu(teijiKaisu);
		key.setCandidateNo(candidateNo);
		kariageTeijiDetailData = skf2060TKariageTeijiDetailRepository.selectByPrimaryKey(key);

		return kariageTeijiDetailData;
	}
	
}
