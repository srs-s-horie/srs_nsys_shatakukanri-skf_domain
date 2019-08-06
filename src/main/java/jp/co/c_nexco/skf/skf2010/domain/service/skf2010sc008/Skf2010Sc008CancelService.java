package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc008;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc008.Skf2010Sc008CancelDto;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc008.Skf2010Sc008InitDto;

/**
 * Skf2010Sc008 代行ログイン画面キャンセル処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc008CancelService extends BaseServiceAbstract<Skf2010Sc008CancelDto>{
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	
	// 会社コード
	private String companyCd = CodeConstant.C001;
	
    /**
     * 代行ログイン画面 キャンセルサービス処理を行う。　
     * 
     * @param initDto インプットDTO
     * @return 処理結果
     * @throws Exception 例外
     */
    @Override
    public Skf2010Sc008CancelDto index(Skf2010Sc008CancelDto cancelDto) throws Exception {

        cancelDto.setPageTitleKey(MessageIdConstant.SKF2010_SC008_TITLE);
        
		// 操作ログを出力
		skfOperationLogUtils.setAccessLog("キャンセル", companyCd, cancelDto.getPageId());
        
        // 取得した代行ユーザ情報を初期化する
        cancelDto = new Skf2010Sc008CancelDto();
        
        return cancelDto;
    }
    
}
