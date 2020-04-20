package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc008;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc008.Skf2010Sc008LoginDto;

/**
 * Skf2010Sc008 代行ログイン画面ログイン処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc008LoginService extends SkfServiceAbstract<Skf2010Sc008LoginDto> {

	@Autowired
	private MenuScopeSessionBean sessionBean;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	// 会社コード
	private String companyCd = CodeConstant.C001;

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

		// 操作ログを出力
		skfOperationLogUtils.setAccessLog("代行ログイン", companyCd, loginDto.getPageId());

		// ログイン処理
		sessionBean.put(SessionCacheKeyConstant.ALTER_LOGIN_SESSION_KEY, CodeConstant.LOGIN);

		// 代行ログイン対象社員情報をセッションに設定する
		Map<String, String> alterLoginUserInfoMap = new HashMap<String, String>();

		alterLoginUserInfoMap.put(CodeConstant.ALTER_LOGIN_USER_SHAIN_NO, loginDto.getShainNo());
		alterLoginUserInfoMap.put(CodeConstant.ALTER_LOGIN_USER_SHAIN_NAME, loginDto.getShainName());
		alterLoginUserInfoMap.put(CodeConstant.ALTER_LOGIN_USER_AGENCY, loginDto.getAgency());
		alterLoginUserInfoMap.put(CodeConstant.ALTER_LOGIN_USER_AGENCY_NAME, loginDto.getAgencyName());
		alterLoginUserInfoMap.put(CodeConstant.ALTER_LOGIN_USER_AFFILIATION1, loginDto.getAffiliation1());
		alterLoginUserInfoMap.put(CodeConstant.ALTER_LOGIN_USER_AFFILIATION1_NAME, loginDto.getAffiliation1Name());
		alterLoginUserInfoMap.put(CodeConstant.ALTER_LOGIN_USER_AFFILIATION2, loginDto.getAffiliation2());
		alterLoginUserInfoMap.put(CodeConstant.ALTER_LOGIN_USER_AFFILIATION2_NAME, loginDto.getAffiliation2Name());

		sessionBean.put(SessionCacheKeyConstant.ALTER_LOGIN_USER_INFO_MAP, alterLoginUserInfoMap);

		// 社宅管理TOP画面へ遷移させる
		TransferPageInfo prevPage = TransferPageInfo.nextPage("Skf1010Sc001");
		loginDto.setTransferPageInfo(prevPage);

		return loginDto;
	}
}
