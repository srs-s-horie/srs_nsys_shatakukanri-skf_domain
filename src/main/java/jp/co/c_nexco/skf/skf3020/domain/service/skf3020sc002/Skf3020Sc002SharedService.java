/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.service.skf3020sc002;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3020Sc002.Skf3020Sc002GetShainNoCountExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3020Sc002.Skf3020Sc002GetShainNoCountExpRepository;

/**
 * Skf3020Sc002 転任者調書取込の共通処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf3020Sc002SharedService {
	
	@Autowired
	private Skf3020Sc002GetShainNoCountExpRepository skf3020Sc002GetShainNoCountExpRepository;
	
	/**
	 * 存在する社員番号かチェックする。
	 * 
	 * @param shainNo 社員番号
	 * @return チェック結果
	 */
	public boolean checkShainExists(String shainNo) {
		Skf3020Sc002GetShainNoCountExpParameter param = new Skf3020Sc002GetShainNoCountExpParameter();
		param.setShainNo(shainNo);
		// 存在する社員番号件数
		int count = skf3020Sc002GetShainNoCountExpRepository.getShainNoCount(param);

		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

}
