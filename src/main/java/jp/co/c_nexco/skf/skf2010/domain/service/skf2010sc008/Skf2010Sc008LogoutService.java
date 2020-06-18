package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc008;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc008.Skf2010Sc008LogoutDto;

/**
 * Skf2010Sc008 代行ログイン画面ログアウト処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc008LogoutService extends SkfServiceAbstract<Skf2010Sc008LogoutDto> {

	@Autowired
	private MenuScopeSessionBean sessionBean;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	// 会社コード
	private String companyCd = CodeConstant.C001;

	/**
	 * 代行ログイン画面 ログアウトサービス処理を行う。
	 * 
	 * @param logoutDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2010Sc008LogoutDto index(Skf2010Sc008LogoutDto logoutDto) throws Exception {

		logoutDto.setPageTitleKey(MessageIdConstant.SKF2010_SC008_TITLE);

		// 操作ログを出力
		skfOperationLogUtils.setAccessLog("代行ログアウト", companyCd, FunctionIdConstant.SKF2010_SC008);

		// ログアウト処理
		sessionBean.put(SessionCacheKeyConstant.ALTER_LOGIN_SESSION_KEY, CodeConstant.NONLOGIN);

		// 代行ログイン対象社員情報をセッションからクリアする
		sessionBean.remove(SessionCacheKeyConstant.ALTER_LOGIN_USER_INFO_MAP);
		logoutDto = new Skf2010Sc008LogoutDto();

		// タイトル情報の再設定
		logoutDto.setPageTitleKey(MessageIdConstant.SKF2010_SC008_TITLE);

		return logoutDto;
	}

}
