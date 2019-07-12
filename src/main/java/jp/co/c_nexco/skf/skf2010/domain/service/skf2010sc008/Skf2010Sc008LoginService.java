package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc008;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc008.Skf2010Sc008LoginDto;

/**
 * Skf2010Sc008 代行ログイン画面ログイン処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc008LoginService extends BaseServiceAbstract<Skf2010Sc008LoginDto>{
    
    @Autowired
    private MenuScopeSessionBean sessionBean;
    /**
     * 代行ログイン画面 ログインサービス処理を行う。　
     * 
     * @param loginDto インプットDTO
     * @return 処理結果
     * @throws Exception 例外
     */
    @Override
    public Skf2010Sc008LoginDto index(Skf2010Sc008LoginDto loginDto) throws Exception {

        loginDto.setPageTitleKey(MessageIdConstant.SKF2010_SC008_TITLE);
        // ログイン処理
        sessionBean.put(SessionCacheKeyConstant.ALTER_LOGIN_SESSION_KEY, CodeConstant.LOGIN);
        loginDto.setAlterLoginFlg(CodeConstant.LOGIN);

        // TODO 代行ログイン対象社員情報をセッションに設定する

        return loginDto;
    }
    
}
