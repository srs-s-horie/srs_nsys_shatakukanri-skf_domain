/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc003;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc003.Skf2060Sc003GetKariageTeijiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc003.Skf2060Sc003GetKariageTeijiInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc003.Skf2060Sc003GetKariageTeijiInfoExpRepository;

/**
 * Skf2060Sc003SharedService 借上候補物件確認内共通クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf2060Sc003SharedService {
	
	@Autowired
	private Skf2060Sc003GetKariageTeijiInfoExpRepository skf2060Sc003GetKariageTeijiInfoExpRepository;
	
	/**
	 * 借上候補物件提示情報を取得する
	 * 
	 * @param initDto
	 * @return 借上候補物件提示情報
	 */
	public List<Skf2060Sc003GetKariageTeijiInfoExp> getKariageTeijiInfo(String companyCd, String applNo){
		
		//ここの固定文字列は定数化するかXMLから取得するように変更必要がある by現行
		String applCheckFlgTrue = "1";
		String applCheckFlgFalse = "0";
		String noSelShatakuName = "選択しない";
		
		//借上候補物件情報の取得
		List<Skf2060Sc003GetKariageTeijiInfoExp> kariageTeijiDataList = new ArrayList<Skf2060Sc003GetKariageTeijiInfoExp>();
		Skf2060Sc003GetKariageTeijiInfoExpParameter param = new Skf2060Sc003GetKariageTeijiInfoExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		param.setApplCheckFlgTrue(applCheckFlgTrue);
		param.setApplCheckFlgFalse(applCheckFlgFalse);
		param.setNoSelShatakuName(noSelShatakuName);
		kariageTeijiDataList = skf2060Sc003GetKariageTeijiInfoExpRepository.getKariageTeijiInfo(param);

		return kariageTeijiDataList;
	}
}
