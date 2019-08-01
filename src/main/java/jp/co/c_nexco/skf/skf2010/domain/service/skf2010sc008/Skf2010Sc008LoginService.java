package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc008;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
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
public class Skf2010Sc008LoginService extends BaseServiceAbstract<Skf2010Sc008LoginDto> {

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

		// 代行ログイン対象社員情報をセッションに設定する
		Map<String, Object> alterLoginUserInfoMap = new HashMap<String, Object>();

		alterLoginUserInfoMap.put(SessionCacheKeyConstant.ALTER_LOGIN_USER_SHAIN_NO, loginDto.getShainNo());
		alterLoginUserInfoMap.put(SessionCacheKeyConstant.ALTER_LOGIN_USER_SHAIN_NAME, loginDto.getShainName());
		alterLoginUserInfoMap.put(SessionCacheKeyConstant.ALTER_LOGIN_USER_AGENCY, loginDto.getAgency());
		alterLoginUserInfoMap.put(SessionCacheKeyConstant.ALTER_LOGIN_USER_AGENCY_NAME, loginDto.getAgencyName());
		alterLoginUserInfoMap.put(SessionCacheKeyConstant.ALTER_LOGIN_USER_AFFILIATION1, loginDto.getAffiliation1());
		alterLoginUserInfoMap.put(SessionCacheKeyConstant.ALTER_LOGIN_USER_AFFILIATION1_NAME, loginDto.getAffiliation1Name());
		alterLoginUserInfoMap.put(SessionCacheKeyConstant.ALTER_LOGIN_USER_AFFILIATION2, loginDto.getAffiliation2());
		alterLoginUserInfoMap.put(SessionCacheKeyConstant.ALTER_LOGIN_USER_AFFILIATION2_NAME, loginDto.getAffiliation2Name());

		sessionBean.put(SessionCacheKeyConstant.ALTER_LOGIN_USER_INFO_MAP, alterLoginUserInfoMap); 

		//社宅管理TOP画面へ遷移させる
		TransferPageInfo prevPage = TransferPageInfo.nextPage("Skf1010Sc001");
		loginDto.setTransferPageInfo(prevPage);

		return loginDto;
	}

}
