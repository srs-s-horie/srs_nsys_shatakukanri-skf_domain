package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc008;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfGetInfoUtils.SkfGetInfoUtilsGetShainInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfGetInfoUtils.SkfGetInfoUtilsGetShainInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfGetInfoUtils.SkfGetInfoUtilsGetShainInfoExpRepository;
import jp.co.c_nexco.skf.common.constants.CodeConstant;

/**
 * Skf2010Sc008 代行ログイン画面 内部処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc008SharedService {

	@Autowired
	private SkfGetInfoUtilsGetShainInfoExpRepository skfGetInfoUtilsGetShainInfoExpRepository;

	/**
	 * 引数で指定された社員番号の情報を取得する。
	 * @param targetShainNo 対象社員番号
	 * @return 対象社員情報エンティティ
	 */
	public SkfGetInfoUtilsGetShainInfoExp getShainInfo(String targetShainNo){
	    SkfGetInfoUtilsGetShainInfoExpParameter param = new SkfGetInfoUtilsGetShainInfoExpParameter();

	    // 会社コード（定数）
	    param.setCompanyCd(CodeConstant.C001);
	    // 社員番号
	    param.setShainNo(targetShainNo);
	    // 検索実行
	    SkfGetInfoUtilsGetShainInfoExp resultEntity = 
	            skfGetInfoUtilsGetShainInfoExpRepository.getShainInfo(param);
	    
	    return resultEntity;
	}
}
