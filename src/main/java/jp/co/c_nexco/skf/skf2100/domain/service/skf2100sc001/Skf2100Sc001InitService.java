/*
 * Copyright(c) 2021 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc001;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2100Sc001.Skf2100Sc001GetShainInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2100Sc001.Skf2100Sc001GetShainInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2100Sc001.Skf2100Sc001GetShainInfoExpRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationGuideUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc001.Skf2100Sc001InitDto;
import jp.co.intra_mart.foundation.context.Contexts;
import jp.co.intra_mart.foundation.user_context.model.UserContext;
import jp.co.intra_mart.foundation.user_context.model.UserProfile;

/**
 * Skf2100Sc001 モバイルルーター借用希望申請書（申請者用)初期表示処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2100Sc001InitService extends SkfServiceAbstract<Skf2100Sc001InitDto> {

	@Autowired
	private Skf2100Sc001SharedService skf2100Sc001SharedService;

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfOperationGuideUtils skfOperationGuideUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private Skf2100Sc001GetShainInfoExpRepository skf2100Sc001GetShainInfoExpRepository;

	
	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2100Sc001InitDto index(Skf2100Sc001InitDto initDto) throws Exception {
		// 操作ログ出力
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, FunctionIdConstant.SKF2100_SC001);
		// セッション情報引き渡し
		skf2100Sc001SharedService.setMenuScopeSessionBean(menuScopeSessionBean);
		// セッション情報初期化
		skf2100Sc001SharedService.clearMenuScopeSessionBean();

		// タイトル設定
		initDto.setPageTitleKey(MessageIdConstant.SKF2100_SC001_TITLE);

		skf2100Sc001SharedService.setClearInfo(initDto);
		
		// 申請情報設定
		Map<String, String> applInfo = new HashMap<String, String>();
		applInfo.put("status", initDto.getApplStatus());
		applInfo.put("applNo", initDto.getApplNo());
		applInfo.put("applId", initDto.getApplId());

		// ユーザ情報の設定
		setUserInfo(initDto);

		// 画面内容の設定
		skf2100Sc001SharedService.initializeDisp(initDto);


		// 画面制御処理（活性／非活性）
		skf2100Sc001SharedService.setControlValue(initDto);
		
		// 操作ガイドの設定
		initDto.setOperationGuide(skfOperationGuideUtils.getOperationGuide(FunctionIdConstant.SKF2100_SC001));

		
		// コメント設定の有無
		if (skf2100Sc001SharedService.setCommentBtnDisabled(initDto.getApplNo())) {
			// コメントがあれば表示
			initDto.setCommentViewFlag(true);
		} else {
			// なければ非表示
			initDto.setCommentViewFlag(false);
		}

		// バナー戻るボタン遷移先調整
		String backUrl = "skf/" + FunctionIdConstant.SKF2010_SC003 + "/init"; // デフォルトは申請状況一覧へ
		String pageId = initDto.getPrePageId();
		if (CheckUtils.isEqual(pageId, FunctionIdConstant.SKF2010_SC007)) {
			// 申請条件確認画面から遷移してきた場合、社宅TOPへ遷移させる
			initDto.setPageId(null);
			initDto.setPrePageId(FunctionIdConstant.SKF1010_SC001);
			// 社宅TOPを設定
			backUrl = "skf/" + FunctionIdConstant.SKF1010_SC001 + "/init";
		}
		initDto.setBackUrl(backUrl);

		return initDto;
	}

	/**
	 * ユーザ情報の取得し、設定する。
	 * 
	 * @param initDto
	 */
	@SuppressWarnings("unchecked")
	private void setUserInfo(Skf2100Sc001InitDto initDto) {
		// ユーザコンテキストを取得
		UserContext userContext = Contexts.get(UserContext.class);
		UserProfile profile = userContext.getUserProfile();

		// ユーザIDの取得
		String userId = profile.getUserCd();
		if (NfwStringUtils.isNotEmpty(userId)) {
			initDto.setUserId(userId);
		}

		// セッション情報の取得(代理ログイン情報)
		Map<String, String> resultAlterLoginList = null;
		resultAlterLoginList = (Map<String, String>) menuScopeSessionBean
				.get(SessionCacheKeyConstant.ALTER_LOGIN_USER_INFO_MAP);

		if (resultAlterLoginList != null) {
			initDto.setAlterLoginFlg((resultAlterLoginList.get(SessionCacheKeyConstant.ALTER_LOGIN_SESSION_KEY)));
		} else {
			initDto.setAlterLoginFlg(SkfCommonConstant.NOT_USE);
		}

		// 社員情報取得
		if (SkfCommonConstant.NOT_USE.equals(initDto.getAlterLoginFlg())) {
			// 代行ログインでない場合
			// 社員マスタから社員情報取得
			List<Map<String, String>> shainList = getShainInfo(CodeConstant.C001, userId);

			if (shainList.size() > 0) {
				// リストに値を格納
				initDto.setShainList(shainList);
				// 社員番号の設定
				initDto.setShainNo(shainList.get(0).get("shainNo"));
			}
		} else {
			// 代行ログインの場合

			// 社員番号の設定
			initDto.setShainNo(resultAlterLoginList.get(CodeConstant.ALTER_LOGIN_USER_SHAIN_NO));

			// 代行対象のユーザ情報を取得
			List<Map<String, String>> alterLoginUserInfo = skfLoginUserInfoUtils
					.getAlterLoginUserInfo(initDto.getShainNo());

			initDto.setShainList(alterLoginUserInfo);

		}

	}
	
	/**
	 * 社員情報の設定(取得)
	 * 
	 * @param companyCd
	 * @param userId
	 * @param shainList
	 * @return 取得結果
	 */
	private List<Map<String, String>> getShainInfo(String companyCd, String userId) {

		// 戻り値
		List<Map<String, String>> shainList = new ArrayList<Map<String, String>>();

		// 社員情報情報取得
		List<Skf2100Sc001GetShainInfoExp> resultshainList = new ArrayList<Skf2100Sc001GetShainInfoExp>();
		// DB検索処理
		Skf2100Sc001GetShainInfoExpParameter param = new Skf2100Sc001GetShainInfoExpParameter();
		param.setCompanyCd(companyCd);
		param.setUserId(userId);
		resultshainList = skf2100Sc001GetShainInfoExpRepository.getShainInfo(param);

		// 取得できなかった場合
		if (resultshainList == null) {
			return shainList;
		}

		// mapに取得情報を格納
		Map<String, String> shainMap = new HashMap<String, String>();

		for (Skf2100Sc001GetShainInfoExp dt : resultshainList) {

			// 表示・値を設定
			shainMap = new HashMap<String, String>();
			shainMap.put("shainNo", dt.getShainNo());
			shainMap.put("name", dt.getName());
			shainMap.put("agencyName", dt.getAgencyName());
			shainMap.put("affiliation1Name", dt.getAffiliation1Name());
			shainMap.put("affiliation2Name", dt.getAffiliation2Name());
			shainMap.put("tel", dt.getTel());
			shainMap.put("mailAddress", dt.getMailAddress());

			shainList.add(shainMap);

		}

		// 返却するリストをDebugログで出力
		LogUtils.debugByMsg("社員情報情報のリスト：" + shainList.toString());

		return shainList;
	}
	
}
