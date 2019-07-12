package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc008;

import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc008.Skf2010Sc008InitDto;

/**
 * Skf2010Sc008 代行ログイン画面キャンセル処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc008CancelService extends BaseServiceAbstract<Skf2010Sc008InitDto>{
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
    public Skf2010Sc008InitDto index(Skf2010Sc008InitDto initDto) throws Exception {

        initDto.setPageTitleKey(MessageIdConstant.SKF2010_SC008_TITLE);
        
        return initDto;
    }
    
}
