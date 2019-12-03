package jp.co.c_nexco.skf.skf3070.domain.service.skf3070sc004;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3070Sc004.Skf3070Sc004GetOwnerInfoExp;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3070.domain.dto.skf3070sc004.Skf3070Sc004InitDto;

/**
 * 賃貸人(代理人)名称入力支援画面のInitサービス処理クラス。
 * 
 */
@Service
public class Skf3070Sc004InitService extends BaseServiceAbstract<Skf3070Sc004InitDto>{
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfDropDownUtils skfDropDownUtils;
	@Autowired
	private Skf3070Sc004SharedService skf3070Sc004SharedService;
	
	private String companyCd = CodeConstant.C001;
	
	@Override
	public Skf3070Sc004InitDto index(Skf3070Sc004InitDto initDto) throws Exception {
		
		skfOperationLogUtils.setAccessLog("初期表示", companyCd, FunctionIdConstant.SKF3070_SC004);
		
		//　タイトル
		initDto.setPageTitleKey(MessageIdConstant.SKF3070_SC004_TITLE);
		
		// 初期化処理
		init(initDto);
		
		// 初期表示では検索結果は0件表示
		List<Skf3070Sc004GetOwnerInfoExp> ownerInfoList = new ArrayList<Skf3070Sc004GetOwnerInfoExp>();
		//リストテーブルデータ取得
		List<Map<String, Object>> popOwnerInfoList = new ArrayList<Map<String, Object>>();
		popOwnerInfoList = skf3070Sc004SharedService.createListTable(ownerInfoList);
		initDto.setPopListTableList(popOwnerInfoList);
		
		return initDto;
	}
	
	
	/**
	 * 初期化処理
	 * 
	 * @param initDto
	 */
	private void init(Skf3070Sc004InitDto initDto) {
		initDto.setPopOwnerName(CodeConstant.NONE);
		initDto.setPopOwnerNameKk(CodeConstant.NONE);
		initDto.setPopAddress(CodeConstant.NONE);
		
		List<Map<String, Object>> businessKbnList = new ArrayList<Map<String, Object>>();
		businessKbnList.addAll(skfDropDownUtils
				.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_KOJIN_HOJIN_KUBUN, null, true));
		initDto.setPopBusinessKbnList(businessKbnList);	
	}
}
	
