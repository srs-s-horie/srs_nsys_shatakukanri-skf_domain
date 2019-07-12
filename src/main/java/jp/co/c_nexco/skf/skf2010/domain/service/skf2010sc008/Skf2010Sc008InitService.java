package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc008;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc008.Skf2010Sc008InitDto;

/**
 * Skf2010Sc008 代行ログイン画面初期表示処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc008InitService extends BaseServiceAbstract<Skf2010Sc008InitDto> {
    

    @Autowired
    private MenuScopeSessionBean sessionBean;
    /**
     * 代行ログイン画面 初期表示サービス処理を行う。　
     * 
     * @param initDto インプットDTO
     * @return 処理結果
     * @throws Exception 例外
     */
    @Override
    public Skf2010Sc008InitDto index(Skf2010Sc008InitDto initDto) throws Exception {

        initDto.setPageTitleKey(MessageIdConstant.SKF2010_SC008_TITLE);
        //セッションから代行ログイン状態を取得
        String sessionVal = (String) sessionBean.get(SessionCacheKeyConstant.ALTER_LOGIN_SESSION_KEY);
        if(CodeConstant.LOGIN.equals(sessionVal)){
            //代行ログイン中
            initDto.setAlterLoginFlg(CodeConstant.LOGIN);
        }else{
            //ログイン中でない
            initDto.setAlterLoginFlg(CodeConstant.NONLOGIN);
        }
        
        return initDto;
    }
    
}
