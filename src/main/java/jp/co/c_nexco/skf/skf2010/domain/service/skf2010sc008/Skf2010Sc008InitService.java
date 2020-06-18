package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc008;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationGuideUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc008.Skf2010Sc008InitDto;

/**
 * Skf2010Sc008 代行ログイン画面初期表示処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc008InitService extends SkfServiceAbstract<Skf2010Sc008InitDto> {

	@Autowired
	private MenuScopeSessionBean sessionBean;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfOperationGuideUtils skfOperationGuideUtils;

	// 会社コード
	private String companyCd = CodeConstant.C001;

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

		// 操作ログを出力
		skfOperationLogUtils.setAccessLog("初期表示", companyCd, FunctionIdConstant.SKF2010_SC008);

		// 操作ガイドの設定
		initDto.setOperationGuide(skfOperationGuideUtils.getOperationGuide(FunctionIdConstant.SKF2010_SC008));

		// セッションから代行ログイン状態を取得
		String sessionVal = (String) sessionBean.get(SessionCacheKeyConstant.ALTER_LOGIN_SESSION_KEY);
		Map<String, Object> alterLoginUserInfoMap = (Map<String, Object>) sessionBean
				.get(SessionCacheKeyConstant.ALTER_LOGIN_USER_INFO_MAP);
		if (CodeConstant.LOGIN.equals(sessionVal)) {
			// 代行ログイン中
			// セッションの代行対象社員の情報を取得する
			initDto.setShainNo((String) alterLoginUserInfoMap.get(CodeConstant.ALTER_LOGIN_USER_SHAIN_NO));
			initDto.setShainName((String) alterLoginUserInfoMap.get(CodeConstant.ALTER_LOGIN_USER_SHAIN_NAME));
			initDto.setAgencyName((String) alterLoginUserInfoMap.get(CodeConstant.ALTER_LOGIN_USER_AGENCY_NAME));
			initDto.setAffiliation1Name(
					(String) alterLoginUserInfoMap.get(CodeConstant.ALTER_LOGIN_USER_AFFILIATION1_NAME));
			initDto.setAffiliation2Name(
					(String) alterLoginUserInfoMap.get(CodeConstant.ALTER_LOGIN_USER_AFFILIATION2_NAME));
		}
		return initDto;
	}

}
