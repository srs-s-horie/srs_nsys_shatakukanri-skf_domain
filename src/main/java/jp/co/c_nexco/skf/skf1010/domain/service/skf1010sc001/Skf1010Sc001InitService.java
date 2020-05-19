/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf1010.domain.service.skf1010sc001;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetInformationNewInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetInformationNewInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountBihinHenkyakuExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountBihinHenkyakuExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountBihinHenkyakuUnfinishedTipsExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountBihinHenkyakuUnfinishedTipsExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountBihinKiboExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountBihinKiboExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountNyutaikyo1Exp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountNyutaikyo1ExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountNyutaikyo2Exp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountNyutaikyo2ExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountNyutaikyoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountNyutaikyoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetShinseiStatusAdminExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetShinseiStatusAdminExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetShinseiStatusUserExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetShinseiStatusUserExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf1010Sc001.Skf1010Sc001GetInformationNewInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountBihinHenkyakuExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountBihinHenkyakuUnfinishedTipsExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountBihinKiboExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountNyutaikyo1ExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountNyutaikyo2ExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountNyutaikyoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf1010Sc001.Skf1010Sc001GetShinseiStatusAdminExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf1010Sc001.Skf1010Sc001GetShinseiStatusUserExpRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.PropertyUtils;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf1010.domain.dto.skf1010sc001.Skf1010Sc001InitDto;

/**
 * 社宅管理TOP画面のInitサービス処理クラス。
 * 
 */
@Service
public class Skf1010Sc001InitService extends SkfServiceAbstract<Skf1010Sc001InitDto> {

	/** 定数 */
	// 会社コード（中日本）
	private final String COMPANYCD = "C001";
	// ワークフローレベル1
	private final String workFlowLevel1 = "01";
	// ワークフローレベル2
	private final String workFlowLevel2 = "02";
	// 申請者のロールID
	private final String shinseisha = CodeConstant.SHINSEISHA;
	// 社宅管理業務者のロールID
	private final String shatakuGyomuKanri = CodeConstant.SHATAKU_GYOMU_KANRI;
	// システム管理者のロールID
	private final String systemyKanri = CodeConstant.SYSTEM_KANRI;
	// 中サ（給与・厚生）担当のロールID
	private final String nakasaKyuyoTanto = CodeConstant.NAKASA_KYUYO_TANTO;
	// 中サ（社宅）担当のロールID
	private final String nakasaShatakutanto = CodeConstant.NAKASA_SHATAKU_TANTO;
	// 中サ（社宅）管理者のロールID
	private final String nakasashatakuKanri = CodeConstant.NAKASA_SHATAKU_KANRI;

	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;

	@Autowired
	private Skf1010Sc001GetInformationNewInfoExpRepository skf1010Sc001GetInformationNewInfoExpRepository;

	@Autowired
	private Skf1010Sc001GetOshiraseCountNyutaikyo1ExpRepository skf1010Sc001GetOshiraseCountNyutaikyo1ExpRepository;

	@Autowired
	private Skf1010Sc001GetOshiraseCountNyutaikyo2ExpRepository skf1010Sc001GetOshiraseCountNyutaikyo2ExpRepository;

	@Autowired
	private Skf1010Sc001GetOshiraseCountNyutaikyoExpRepository skf1010Sc001GetOshiraseCountNyutaikyoExpRepository;

	@Autowired
	private Skf1010Sc001GetOshiraseCountBihinKiboExpRepository skf1010Sc001GetOshiraseCountBihinKiboExpRepository;

	@Autowired
	private Skf1010Sc001GetOshiraseCountBihinHenkyakuUnfinishedTipsExpRepository skf1010Sc001GetOshiraseCountBihinHenkyakuUnfinishedTipsExpRepository;

	@Autowired
	private Skf1010Sc001GetOshiraseCountBihinHenkyakuExpRepository skf1010Sc001GetOshiraseCountBihinHenkyakuExpRepository;

	@Autowired
	private Skf1010Sc001GetShinseiStatusUserExpRepository skf1010Sc001GetShinseiStatusUserExpRepository;

	@Autowired
	private Skf1010Sc001GetShinseiStatusAdminExpRepository skf1010Sc001GetShinseiStatusAdminExpRepository;

	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;

	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf1010Sc001InitDto index(Skf1010Sc001InitDto initDto) throws Exception {

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", COMPANYCD, FunctionIdConstant.SKF1010_SC001);

		// 申請全体
		String level1 = CodeConstant.DOUBLE_QUOTATION;
		// 窓口全体
		String level2 = CodeConstant.DOUBLE_QUOTATION;
		// 窓口（社宅を管理する～社員情報を一括更新する）
		String level2_2 = CodeConstant.DOUBLE_QUOTATION;
		// 窓口（月締め処理を行う）
		String level2_3 = CodeConstant.DOUBLE_QUOTATION;
		// 窓口（レンタル備品指示書を作成する、備品搬入・搬出確認リストを作成する）
		String level2_4 = CodeConstant.DOUBLE_QUOTATION;
		// 窓口（法定調書データを管理する）
		String level2_5 = CodeConstant.DOUBLE_QUOTATION;
		// 窓口（年齢による使用料の変更通知）
		String level2_6 = CodeConstant.DOUBLE_QUOTATION;
		// 管理者全体
		String level3 = CodeConstant.DOUBLE_QUOTATION;
		// 管理者（代行ログイン）
		String level3_1 = CodeConstant.DOUBLE_QUOTATION;
		// 管理者（組織マスタメンテナンス）
		String level3_2 = CodeConstant.DOUBLE_QUOTATION;
		// 操作に困ったときは（マニュアル 管理）
		String level4_1 = CodeConstant.DOUBLE_QUOTATION;
		// 未承認処理（全体）
		String level5 = CodeConstant.DOUBLE_QUOTATION;

		initDto.setPageTitleKey(MessageIdConstant.SKF1010_SC001_TITLE);

		/** ログインユーザの情報を取得 */
		Map<String, String> LoginUserInfoMap = new HashMap<String, String>();
		LoginUserInfoMap = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		// ログインユーザのロールIDを取得
		String roleId = LoginUserInfoMap.get("roleId");
		initDto.setRoleId(roleId);
		// ログインユーザのユーザ名を取得する
		String userName = LoginUserInfoMap.get("userName");
		initDto.setUserName(userName);
		// ログインユーザの社員番号を取得する
		String shainNo = LoginUserInfoMap.get("shainNo");
		initDto.setShainNo(shainNo);

		/**
		 * ログインユーザのロールIDによって表示する画面を切り替える。<br>
		 * 表示しないものはtrue、表示するものはfalse。
		 */
		// 申請者
		if (shinseisha.equals(roleId)) {
			level2 = "true";
			level3 = "true";
			level4_1 = "true";
			level5 = "true";
			initDto.setLevel2(level2);
			initDto.setLevel3(level3);
			initDto.setLevel4_1(level4_1);
			initDto.setLevel5(level5);

		}
		// 社宅管理業務者
		else if (shatakuGyomuKanri.equals(roleId) || systemyKanri.equals(roleId)) {

		}

		// 中サ（給与・厚生）担当
		else if (nakasaKyuyoTanto.equals(roleId)) {
			level1 = "true";
			level2_2 = "true";
			level2_3 = "true";
			level2_4 = "true";
			level2_5 = "true";
			level2_6 = "true";
			level3 = "true";
			level4_1 = "true";
			level5 = "true";
			initDto.setLevel1(level1);
			initDto.setLevel2_2(level2_2);
			initDto.setLevel2_3(level2_3);
			initDto.setLevel2_4(level2_4);
			initDto.setLevel2_5(level2_5);
			initDto.setLevel2_6(level2_6);
			initDto.setLevel3(level3);
			initDto.setLevel4_1(level4_1);
			initDto.setLevel5(level5);

		}
		// 中サ（社宅）担当
		else if (nakasaShatakutanto.equals(roleId)) {
			level1 = "true";
			level2_3 = "true";
			level2_5 = "true";
			level2_6 = "true";
			level3 = "true";
			initDto.setLevel1(level1);
			initDto.setLevel2_3(level2_3);
			initDto.setLevel2_5(level2_5);
			initDto.setLevel2_6(level2_6);
			initDto.setLevel3(level3);
		}
		// 中サ（社宅）管理者
		else if (nakasashatakuKanri.equals(roleId)) {
			level3_1 = "true";
			level3_2 = "true";
			initDto.setLevel3_1(level3_1);
			initDto.setLevel3_2(level3_2);
		}

		/** システムに関するお知らせ取得 */
		getInformation(initDto);

		if (nakasaShatakutanto.equals(initDto.getRoleId()) || nakasashatakuKanri.equals(initDto.getRoleId())
				|| systemyKanri.equals(initDto.getRoleId())) {
			// 個人のお知らせ（管理）
			List<Map<String, Object>> oshiraseList2 = new ArrayList<Map<String, Object>>();
			oshiraseList2 = oshiraseAdmin(COMPANYCD, initDto.getShainNo(), 30, initDto.getRoleId());
			initDto.setOshiraseList(oshiraseList2);
		} else {
			// 個人のお知らせ（一般）
			List<Map<String, Object>> oshiraseList = new ArrayList<Map<String, Object>>();
			oshiraseList = oshiraseUser(COMPANYCD, initDto.getShainNo(), 30);
			initDto.setOshiraseList(oshiraseList);
		}

		/**
		 * 未処理情報上部 社宅入居希望等調書の申請が無い入居情報がある。督促メールを送信してください。
		 */
		getOshiraseCount1(initDto);

		/**
		 * 未処理情報上部 退居届の無い退居情報がある。 督促メールを送信してください。
		 */
		String nyutaikyoCount2 = String.valueOf(getOshiraseCount2());
		initDto.setNyutaikyoCount2(nyutaikyoCount2);

		/**
		 * 未処理情報<br>
		 * 社宅入居希望等調書の申請<br>
		 * 社宅希望者に社宅提示が完了していないデータ<br>
		 * 提示社宅の本人確認が完了していないデータ<br>
		 * 入居希望者の同意済のデータ<br>
		 * 入居希望者の同意されなかったデータ<br>
		 */

		// 入退居申請状況区分を配列に格納
		String[] nyutaikyoApplStatusKbns = new String[] { CodeConstant.NYUTAIKYO_APPL_STATUS_SHINSEICHU,
				CodeConstant.NYUTAIKYO_APPL_STATUS_SHINSACHU, CodeConstant.NYUTAIKYO_APPL_STATUS_KAKUNIN_IRAI,
				CodeConstant.NYUTAIKYO_APPL_STATUS_DOI_ZUMI, CodeConstant.NYUTAIKYO_APPL_STATUS_DOI_SHINAI };

		// 件数部分の遷移先を配列に格納
		String[] linkNyutaikyo = new String[] { "/imart/skf/Skf2010Sc005/init", "/imart/skf/Skf3022Sc005/init",
				"/imart/skf/Skf3022Sc005/init", "/imart/skf/Skf2010Sc005/init", "/imart/skf/Skf3022Sc005/init" };

		// メッセージ部分のラベルを配列に格納
		String[] labelNyutaikyo = new String[] { MessageIdConstant.SKF1010_SC001_SYATAKU_KIBO_APPLICATION,
				MessageIdConstant.SKF1010_SC001_SYATAKU_KIBOSYA_UNFINISHED_TIP,
				MessageIdConstant.SKF1010_SC001_SYATAKU_TEIJI_UNFINISHED_CHECK,
				MessageIdConstant.SKF1010_SC001_NYUKYO_KIBOSYA_AGREED,
				MessageIdConstant.SKF1010_SC001_NYUKYO_KIBOSYA_DISAGREED };

		List<Map<String, Object>> nyutaikyoInformationList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < nyutaikyoApplStatusKbns.length; i++) {
			Map<String, Object> dataList = new HashMap<String, Object>();
			dataList = nyukyotaikyo(CodeConstant.SYS_NYUKYO_KBN, nyutaikyoApplStatusKbns[i], labelNyutaikyo[i],
					linkNyutaikyo[i], i + 1);
			nyutaikyoInformationList.add(dataList);
		}

		/**
		 * 未処理情報 <br>
		 * 退居届の申請
		 */
		Map<String, Object> dataList = new HashMap<String, Object>();
		dataList = nyukyotaikyo(CodeConstant.SYS_TAIKYO_KBN, nyutaikyoApplStatusKbns[0],
				MessageIdConstant.SKF1010_SC001_TAIKYO_APPLICATION, "/imart/skf/Skf2010Sc005/init",
				nyutaikyoInformationList.size() + 1);
		nyutaikyoInformationList.add(dataList);

		initDto.setNyutaikyoInformationList(nyutaikyoInformationList);

		/**
		 * 未処理情報<br>
		 * 備品希望の申請が無い入居情報<br>
		 * 備品希望の提示が完了していないデータ<br>
		 * 備品希望の搬入が完了していないデータ<br>
		 */

		// 備品提示ステータスを配列に格納
		String[] bihinTeijiStatuses1 = new String[] { CodeConstant.BIHIN_STATUS_MI_SAKUSEI,
				CodeConstant.BIHIN_STATUS_SAKUSEI_CHU, CodeConstant.BIHIN_STATUS_HANNYU_MACHI };

		// メッセージ部分のラベルを配列に格納
		String[] labelBihinKibo = new String[] { MessageIdConstant.SKF1010_SC001_BIHIN_KIBO_NOT_APPLICATION,
				MessageIdConstant.SKF1010_SC001_BIHIN_KIBO_UNFINISHED_TIPS,
				MessageIdConstant.SKF1010_SC001_BIHIN_KIBO_UNFINISHED_HANNYU };

		List<Map<String, Object>> bihinKiboInformationList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < labelBihinKibo.length; i++) {
			Map<String, Object> bihinDataList = new HashMap<String, Object>();
			bihinDataList = bihinKibo(CodeConstant.SYS_NYUKYO_KBN, CodeConstant.BIHIN_TAIYO_KBN_HITSUYO,
					bihinTeijiStatuses1[i], labelBihinKibo[i], "/imart/skf/Skf3022Sc005/init",
					nyutaikyoInformationList.size() + 1 + i);
			bihinKiboInformationList.add(bihinDataList);
		}

		initDto.setBihinKiboInformationList(bihinKiboInformationList);

		// 備品返却の提示が未完了の件数の取得
		bihinHenkyakuUnfinishedTips(initDto);

		/**
		 * 未処理情報<br>
		 * 備品返却提示の本人確認が完了していないデータ<br>
		 * 備品返却提示の同意済のデータ<br>
		 * 備品の搬出が完了していないデータ<br>
		 */

		// 備品提示ステータスを配列に格納
		String[] bihinTeijiStatuses2 = new String[] { CodeConstant.BIHIN_STATUS_TEIJI_CHU,
				CodeConstant.BIHIN_STATUS_DOI_SUMI, CodeConstant.BIHIN_STATUS_HANSHUTSU_MACHI };

		// 件数部分の遷移先を配列に格納
		String[] linkBihinHenkyaku = new String[] { "/imart/skf/Skf3022Sc005/init", "/imart/skf/Skf2010Sc005/init",
				"/imart/skf/Skf3022Sc005/init" };

		// メッセージ部分のラベルを配列に格納
		String[] labelBihinHenkyaku = new String[] { MessageIdConstant.SKF1010_SC001_BIHIN_HENKYAKU_UNFINISHED_CHECK,
				MessageIdConstant.SKF1010_SC001_BIHIN_HENKYAKU_AGREED,
				MessageIdConstant.SKF1010_SC001_BIHIN_HENKYAKU_UNFINISHED_MOVEMENT };

		List<Map<String, Object>> bihinHenkyakuInformationList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < labelBihinHenkyaku.length; i++) {
			Map<String, Object> bihinHenkyakuDataList = new HashMap<String, Object>();
			bihinHenkyakuDataList = bihinHenkyaku(CodeConstant.SYS_TAIKYO_KBN, bihinTeijiStatuses2[i],
					labelBihinHenkyaku[i], linkBihinHenkyaku[i],
					nyutaikyoInformationList.size() + bihinKiboInformationList.size() + 1 + i);
			bihinHenkyakuInformationList.add(bihinHenkyakuDataList);
		}

		initDto.setBihinHenkyakuInformationList(bihinHenkyakuInformationList);

		// 一覧画面の検索条件保持セッション情報を破棄する
		// 申請状況一覧画面の検索条件破棄
		menuScopeSessionBean.remove(SessionCacheKeyConstant.SKF2010_SC003_SEARCH_ITEMS_KEY);
		// 承認一覧画面の検索条件破棄
		menuScopeSessionBean.remove(SessionCacheKeyConstant.SKF2010_SC005_SEARCH_ITEMS_KEY);
		// 借上候補物件状況一覧画面検索条件破棄
		menuScopeSessionBean.remove(SessionCacheKeyConstant.SKF2060SC004_SEARCH_COND_SESSION_KEY);
		// 法定調書データ管理画面検索条件破棄
		menuScopeSessionBean.remove(SessionCacheKeyConstant.SKF3070SC001_SEARCH_COND_SESSION_KEY);

		return initDto;
	}

	/** システムに関するお知らせ */
	public void getInformation(Skf1010Sc001InitDto initDto) {
		Skf1010Sc001GetInformationNewInfoExp resultData = new Skf1010Sc001GetInformationNewInfoExp();
		Skf1010Sc001GetInformationNewInfoExpParameter param = new Skf1010Sc001GetInformationNewInfoExpParameter();

		param.setCompanyCd(COMPANYCD);
		resultData = skf1010Sc001GetInformationNewInfoExpRepository.getInformationNewInfo(param);
		/** 「システムに関するお知らせ」のデータがあるかの確認 */
		// 「システムに関するお知らせ」のデータがある場合、最新内容を取得して表示する。
		if (resultData != null) {
			initDto.setNote(resultData.getNote());
		}
		// 「システムに関するお知らせ」のデータが無い場合、空白を設定して、システムに関するお知らせのエリアのみを表示する。
		else {
			initDto.setNote(CodeConstant.DOUBLE_QUOTATION);
		}
	}

	/**
	 * 未処理情報上部 <br>
	 * 社宅入居希望等調書の申請がない入居情報がある。 督促メールを送信してください。
	 * 
	 * @return
	 */
	public void getOshiraseCount1(Skf1010Sc001InitDto initDto) {
		// 入退居区分
		String nyutaikyoKbn = CodeConstant.SYS_NYUKYO_KBN;
		// 入退居申請状況区分
		String nyutaikyoApplStatusKbn = CodeConstant.DOUBLE_QUOTATION;

		int nyutaikyoCount1 = 0;

		Skf1010Sc001GetOshiraseCountNyutaikyo1Exp nyutaikyoData1 = new Skf1010Sc001GetOshiraseCountNyutaikyo1Exp();
		Skf1010Sc001GetOshiraseCountNyutaikyo1ExpParameter param1 = new Skf1010Sc001GetOshiraseCountNyutaikyo1ExpParameter();

		param1.setNyutaikyoKbn(nyutaikyoKbn);
		param1.setNyutaikyoApplStatusKbn(nyutaikyoApplStatusKbn);

		LogUtils.debugByMsg("未処理情報_社宅入居希望等調書の申請が無い入居情報があります。SQL");

		nyutaikyoData1 = skf1010Sc001GetOshiraseCountNyutaikyo1ExpRepository.getOshiraseCountNyutaikyo1(param1);

		if (nyutaikyoData1 != null) {
			nyutaikyoCount1 += nyutaikyoData1.getExpr1();
		}

		String nyutaikyoApplStatusKbnSave = CodeConstant.NYUTAIKYO_APPL_STATUS_ICHIJIHOZON;
		String nyutaikyoApplStatusKbnRepair = CodeConstant.NYUTAIKYO_APPL_STATUS_SHUSEI_IRAI;

		Skf1010Sc001GetOshiraseCountNyutaikyo2Exp nyutaikyoData2 = new Skf1010Sc001GetOshiraseCountNyutaikyo2Exp();
		Skf1010Sc001GetOshiraseCountNyutaikyo2ExpParameter param2 = new Skf1010Sc001GetOshiraseCountNyutaikyo2ExpParameter();

		param2.setNyutaikyoKbn(nyutaikyoKbn);
		param2.setNyutaikyoApplStatusKbn(nyutaikyoApplStatusKbnSave);

		LogUtils.debugByMsg("未処理情報_社宅入居希望等調書の申請が無い入居情報があります。SQL");

		nyutaikyoData2 = skf1010Sc001GetOshiraseCountNyutaikyo2ExpRepository.getOshiraseCountNyutaikyo2(param2);

		if (nyutaikyoData2 != null) {
			nyutaikyoCount1 += nyutaikyoData2.getExpr1();
		}

		Skf1010Sc001GetOshiraseCountNyutaikyo2Exp nyutaikyoData3 = new Skf1010Sc001GetOshiraseCountNyutaikyo2Exp();
		Skf1010Sc001GetOshiraseCountNyutaikyo2ExpParameter param3 = new Skf1010Sc001GetOshiraseCountNyutaikyo2ExpParameter();

		param3.setNyutaikyoKbn(nyutaikyoKbn);
		param3.setNyutaikyoApplStatusKbn(nyutaikyoApplStatusKbnRepair);

		LogUtils.debugByMsg("未処理情報_社宅入居希望等調書の申請が無い入居情報があります。SQL");

		nyutaikyoData3 = skf1010Sc001GetOshiraseCountNyutaikyo2ExpRepository.getOshiraseCountNyutaikyo2(param3);

		if (nyutaikyoData3 != null) {
			nyutaikyoCount1 += nyutaikyoData3.getExpr1();
		}

		initDto.setNyutaikyoCount1(nyutaikyoCount1);
	}

	/**
	 * 未処理情報上部 <br>
	 * 退居届の申請が無い退居情報があります。<br>
	 * 督促メールを送信してください。
	 * 
	 * @return
	 */
	public int getOshiraseCount2() {
		// 入退居区分
		String nyutaikyoKbn = CodeConstant.SYS_TAIKYO_KBN;
		// 入退居申請状況区分
		String nyutaikyoApplStatusKbn = CodeConstant.DOUBLE_QUOTATION;

		int expr1 = 0;

		Skf1010Sc001GetOshiraseCountNyutaikyo1Exp nyutaikyoData1 = new Skf1010Sc001GetOshiraseCountNyutaikyo1Exp();
		Skf1010Sc001GetOshiraseCountNyutaikyo1ExpParameter param1 = new Skf1010Sc001GetOshiraseCountNyutaikyo1ExpParameter();

		param1.setNyutaikyoKbn(nyutaikyoKbn);
		param1.setNyutaikyoApplStatusKbn(nyutaikyoApplStatusKbn);

		LogUtils.debugByMsg("未処理情報_退居届の申請が無い退居情報があります。SQL");

		nyutaikyoData1 = skf1010Sc001GetOshiraseCountNyutaikyo1ExpRepository.getOshiraseCountNyutaikyo1(param1);

		if (nyutaikyoData1 != null) {
			expr1 += nyutaikyoData1.getExpr1();
		}

		String nyutaikyoApplStatusKbnSave = CodeConstant.NYUTAIKYO_APPL_STATUS_ICHIJIHOZON;
		String nyutaikyoApplStatusKbnRepair = CodeConstant.NYUTAIKYO_APPL_STATUS_SHUSEI_IRAI;

		Skf1010Sc001GetOshiraseCountNyutaikyo2Exp nyutaikyoData2 = new Skf1010Sc001GetOshiraseCountNyutaikyo2Exp();
		Skf1010Sc001GetOshiraseCountNyutaikyo2ExpParameter param2 = new Skf1010Sc001GetOshiraseCountNyutaikyo2ExpParameter();

		param2.setNyutaikyoKbn(nyutaikyoKbn);
		param2.setNyutaikyoApplStatusKbn(nyutaikyoApplStatusKbnSave);

		LogUtils.debugByMsg("未処理情報_退居届の申請が無い退居情報があります。SQL");

		nyutaikyoData2 = skf1010Sc001GetOshiraseCountNyutaikyo2ExpRepository.getOshiraseCountNyutaikyo2(param2);

		if (nyutaikyoData2 != null) {
			expr1 += nyutaikyoData2.getExpr1();
		}

		Skf1010Sc001GetOshiraseCountNyutaikyo2Exp nyutaikyoData3 = new Skf1010Sc001GetOshiraseCountNyutaikyo2Exp();
		Skf1010Sc001GetOshiraseCountNyutaikyo2ExpParameter param3 = new Skf1010Sc001GetOshiraseCountNyutaikyo2ExpParameter();

		param3.setNyutaikyoKbn(nyutaikyoKbn);
		param3.setNyutaikyoApplStatusKbn(nyutaikyoApplStatusKbnRepair);

		LogUtils.debugByMsg("未処理情報_退居届の申請が無い退居情報があります。SQL");

		nyutaikyoData3 = skf1010Sc001GetOshiraseCountNyutaikyo2ExpRepository.getOshiraseCountNyutaikyo2(param3);

		if (nyutaikyoData3 != null) {
			expr1 += nyutaikyoData3.getExpr1();
		}

		return expr1;
	}

	/**
	 * 未処理情報 <br>
	 * 「備品の返却の提示が完了していないデータがあります。」の件数
	 */
	public void bihinHenkyakuUnfinishedTips(Skf1010Sc001InitDto initDto) {
		List<Skf1010Sc001GetOshiraseCountBihinHenkyakuUnfinishedTipsExp> bihinHenkyakuUnfinishedTipsData = new ArrayList<Skf1010Sc001GetOshiraseCountBihinHenkyakuUnfinishedTipsExp>();
		Skf1010Sc001GetOshiraseCountBihinHenkyakuUnfinishedTipsExpParameter param = new Skf1010Sc001GetOshiraseCountBihinHenkyakuUnfinishedTipsExpParameter();

		param.setNyutaikyoKbn(CodeConstant.SYS_TAIKYO_KBN);
		param.setBihinTeijiStatus1(CodeConstant.BIHIN_STATUS_SAKUSEI_CHU);
		param.setBihinTeijiStatus2(CodeConstant.BIHIN_STATUS_SAKUSEI_SUMI);
		param.setHonsha(SkfCommonConstant.SISHA_HONSHA);
		param.setTokyo(SkfCommonConstant.SISHA_TOKYO);
		param.setHatiouzi(SkfCommonConstant.SISHA_HATIOUZI);
		param.setNagoya(SkfCommonConstant.SISHA_NAGOYA);
		param.setKanazawa(SkfCommonConstant.SISHA_KANAZAWA);

		LogUtils.debugByMsg("未処理情報_備品の返却提示が完了していないデータがあります。SQL");

		bihinHenkyakuUnfinishedTipsData = skf1010Sc001GetOshiraseCountBihinHenkyakuUnfinishedTipsExpRepository
				.getOshiraseCountBihinHenkyakuUnfinishedTips(param);
		initDto.setBihinHenkyakuUnfinishedTipsHonsha(bihinHenkyakuUnfinishedTipsData.get(0).getCnt());
		initDto.setBihinHenkyakuUnfinishedTipsTokyo(bihinHenkyakuUnfinishedTipsData.get(1).getCnt());
		initDto.setBihinHenkyakuUnfinishedTipsHatiouzi(bihinHenkyakuUnfinishedTipsData.get(2).getCnt());
		initDto.setBihinHenkyakuUnfinishedTipsNagoya(bihinHenkyakuUnfinishedTipsData.get(3).getCnt());
		initDto.setBihinHenkyakuUnfinishedTipsKanazawa(bihinHenkyakuUnfinishedTipsData.get(4).getCnt());

	}

	/**
	 * 未処理情報<br>
	 * 社宅入居希望等調書の申請<br>
	 * 社宅希望者に社宅提示が完了していないデータ<br>
	 * 提示社宅の本人確認が完了していないデータ<br>
	 * 入居希望者の同意済のデータ<br>
	 * 入居希望者の同意されなかったデータ<br>
	 * 退居届の申請<br>
	 * 
	 * @param nyutaikyoKbn
	 * @param nyutaikyoApplStatusKbn
	 * @param label
	 * @param link
	 * @param index
	 * @return
	 */
	public Map<String, Object> nyukyotaikyo(String nyutaikyoKbn, String nyutaikyoApplStatusKbn, String label,
			String link, int index) {

		List<Skf1010Sc001GetOshiraseCountNyutaikyoExp> nyutaikyoData = new ArrayList<Skf1010Sc001GetOshiraseCountNyutaikyoExp>();
		Skf1010Sc001GetOshiraseCountNyutaikyoExpParameter param = new Skf1010Sc001GetOshiraseCountNyutaikyoExpParameter();
		param.setNyutaikyoKbn(nyutaikyoKbn);
		param.setNyutaikyoApplStatusKbn(nyutaikyoApplStatusKbn);
		param.setHonsha(SkfCommonConstant.SISHA_HONSHA);
		param.setTokyo(SkfCommonConstant.SISHA_TOKYO);
		param.setHatiouzi(SkfCommonConstant.SISHA_HATIOUZI);
		param.setNagoya(SkfCommonConstant.SISHA_NAGOYA);
		param.setKanazawa(SkfCommonConstant.SISHA_KANAZAWA);

		Map<String, Object> resultMap = new HashMap<String, Object>();

		nyutaikyoData = skf1010Sc001GetOshiraseCountNyutaikyoExpRepository.getOshiraseCountNyutaikyo(param);
		resultMap.put("label", label);
		resultMap.put("link", link);
		resultMap.put("index", index);
		resultMap.put("nyutaikyoHonsha", nyutaikyoData.get(0).getCnt());
		resultMap.put("nyutaikyoTokyo", nyutaikyoData.get(1).getCnt());
		resultMap.put("nyutaikyoHatiouzi", nyutaikyoData.get(2).getCnt());
		resultMap.put("nyutaikyoNagoya", nyutaikyoData.get(3).getCnt());
		resultMap.put("nyutaikyoKanazawa", nyutaikyoData.get(4).getCnt());

		return resultMap;

	}

	/**
	 * 未処理情報<br>
	 * 備品希望の申請が無い入居情報<br>
	 * 備品希望の提示が完了していないデータ<br>
	 * 備品希望の搬入が完了していないデータ<br>
	 * 
	 * @param nyutaikyoKbn
	 * @param bihinTaiyoKbn
	 * @param bihinTeijiStatus
	 * @param label
	 * @param link
	 * @param index
	 * @return
	 */
	public Map<String, Object> bihinKibo(String nyutaikyoKbn, String bihinTaiyoKbn, String bihinTeijiStatus,
			String label, String link, int index) {
		List<Skf1010Sc001GetOshiraseCountBihinKiboExp> bihinKiboData = new ArrayList<Skf1010Sc001GetOshiraseCountBihinKiboExp>();
		Skf1010Sc001GetOshiraseCountBihinKiboExpParameter param = new Skf1010Sc001GetOshiraseCountBihinKiboExpParameter();
		param.setNyutaikyoKbn(nyutaikyoKbn);
		param.setBihinTaiyoKbn(bihinTaiyoKbn);
		param.setBihinTeijiStatus(bihinTeijiStatus);
		param.setHonsha(SkfCommonConstant.SISHA_HONSHA);
		param.setTokyo(SkfCommonConstant.SISHA_TOKYO);
		param.setHatiouzi(SkfCommonConstant.SISHA_HATIOUZI);
		param.setNagoya(SkfCommonConstant.SISHA_NAGOYA);
		param.setKanazawa(SkfCommonConstant.SISHA_KANAZAWA);

		Map<String, Object> resultMap = new HashMap<String, Object>();

		bihinKiboData = skf1010Sc001GetOshiraseCountBihinKiboExpRepository.getOshiraseCountBihinKibo(param);
		resultMap.put("label", label);
		resultMap.put("link", link);
		resultMap.put("index", index);
		resultMap.put("bihinKiboHonsha", bihinKiboData.get(0).getCnt());
		resultMap.put("bihinKiboTokyo", bihinKiboData.get(1).getCnt());
		resultMap.put("bihinKiboHatiouzi", bihinKiboData.get(2).getCnt());
		resultMap.put("bihinKiboNagoya", bihinKiboData.get(3).getCnt());
		resultMap.put("bihinKiboKanazawa", bihinKiboData.get(4).getCnt());

		return resultMap;

	}

	/**
	 * 未処理情報<br>
	 * 備品返却提示の本人確認が完了していないデータ<br>
	 * 備品返却提示の同意済のデータ<br>
	 * 備品の搬出が完了していないデータ<br>
	 * 
	 * @param nyutaikyoKbn
	 * @param bihinTeijiStatus
	 * @param label
	 * @param link
	 * @param index
	 * @return
	 */
	public Map<String, Object> bihinHenkyaku(String nyutaikyoKbn, String bihinTeijiStatus, String label, String link,
			int index) {
		List<Skf1010Sc001GetOshiraseCountBihinHenkyakuExp> bihinHenkyakuData = new ArrayList<Skf1010Sc001GetOshiraseCountBihinHenkyakuExp>();
		Skf1010Sc001GetOshiraseCountBihinHenkyakuExpParameter param = new Skf1010Sc001GetOshiraseCountBihinHenkyakuExpParameter();
		param.setNyutaikyoKbn(nyutaikyoKbn);
		param.setBihinTeijiStatus(bihinTeijiStatus);
		param.setHonsha(SkfCommonConstant.SISHA_HONSHA);
		param.setTokyo(SkfCommonConstant.SISHA_TOKYO);
		param.setHatiouzi(SkfCommonConstant.SISHA_HATIOUZI);
		param.setNagoya(SkfCommonConstant.SISHA_NAGOYA);
		param.setKanazawa(SkfCommonConstant.SISHA_KANAZAWA);

		Map<String, Object> resultMap = new HashMap<String, Object>();

		bihinHenkyakuData = skf1010Sc001GetOshiraseCountBihinHenkyakuExpRepository.getOshiraseCountBihinHenkyaku(param);
		resultMap.put("label", label);
		resultMap.put("link", link);
		resultMap.put("index", index);
		resultMap.put("bihinHenkyakuHonsha", bihinHenkyakuData.get(0).getCnt());
		resultMap.put("bihinHenkyakuTokyo", bihinHenkyakuData.get(1).getCnt());
		resultMap.put("bihinHenkyakuHatiouzi", bihinHenkyakuData.get(2).getCnt());
		resultMap.put("bihinHenkyakuNagoya", bihinHenkyakuData.get(3).getCnt());
		resultMap.put("bihinHenkyakuKanazawa", bihinHenkyakuData.get(4).getCnt());

		return resultMap;
	}

	// 個人のお知らせ(一般ユーザ)内容取得
	public List<Map<String, Object>> oshiraseUser(String companyCd, String shainNo, int dispTerm) {
		List<Map<String, Object>> oshiraseList = new ArrayList<Map<String, Object>>();
		List<Skf1010Sc001GetShinseiStatusUserExp> oshiraseDataList = new ArrayList<Skf1010Sc001GetShinseiStatusUserExp>();
		Skf1010Sc001GetShinseiStatusUserExpParameter param = new Skf1010Sc001GetShinseiStatusUserExpParameter();

		param.setCompanyCd(companyCd);
		param.setShainNo(shainNo);
		param.setDispTerm(dispTerm);

		// 申請状況コード取得
		Map<String, String> sinseiMap = new HashMap<String, String>();
		sinseiMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_STATUS);

		LogUtils.debugByMsg("個人の知らせ一般。SQL");

		oshiraseDataList = skf1010Sc001GetShinseiStatusUserExpRepository.getShinseiStatusUser(param);
		for (Skf1010Sc001GetShinseiStatusUserExp osiraseData : oshiraseDataList) {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (osiraseData.getApplStatus().equals(CodeConstant.STATUS_KAKUNIN_IRAI)) {
				String updateDate = skfDateFormatUtils.dateFormatFromDate(osiraseData.getUpdateDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				String message = getOshirase("infomation.skf.i_skf_2041", updateDate, osiraseData.getApplName());
				resultMap.put("message", message);
			} else if (osiraseData.getApplStatus().equals(CodeConstant.STATUS_SHONIN_ZUMI)) {
				String updateDate = skfDateFormatUtils.dateFormatFromDate(osiraseData.getUpdateDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				String applDate = skfDateFormatUtils.dateFormatFromDate(osiraseData.getApplDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				String message = getOshirase("infomation.skf.i_skf_2045", updateDate, applDate,
						osiraseData.getApplName(), sinseiMap.get(CodeConstant.STATUS_SHONIN_ZUMI));
				resultMap.put("message", message);
			} else if (osiraseData.getApplStatus().equals(CodeConstant.STATUS_SASHIMODOSHI)) {
				String updateDate = skfDateFormatUtils.dateFormatFromDate(osiraseData.getUpdateDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				String applDate = skfDateFormatUtils.dateFormatFromDate(osiraseData.getApplDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				String message = getOshirase("infomation.skf.i_skf_2045", updateDate, applDate,
						osiraseData.getApplName(), sinseiMap.get(CodeConstant.STATUS_SASHIMODOSHI));
				resultMap.put("message", message);
			} else if (osiraseData.getApplStatus().equals(CodeConstant.STATUS_HININ)) {
				String updateDate = skfDateFormatUtils.dateFormatFromDate(osiraseData.getUpdateDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				String applDate = skfDateFormatUtils.dateFormatFromDate(osiraseData.getApplDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				String message = getOshirase("infomation.skf.i_skf_2045", updateDate, applDate,
						osiraseData.getApplName(), sinseiMap.get(CodeConstant.STATUS_HININ));
				resultMap.put("message", message);
			} else if (osiraseData.getApplStatus().equals(CodeConstant.STATUS_KANRYOU)) {
				String updateDate = skfDateFormatUtils.dateFormatFromDate(osiraseData.getUpdateDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				String applDate = skfDateFormatUtils.dateFormatFromDate(osiraseData.getApplDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				String message = getOshirase("infomation.skf.i_skf_2060", updateDate, applDate,
						osiraseData.getApplName(), sinseiMap.get(CodeConstant.STATUS_KANRYOU));
				resultMap.put("message", message);
			} else if (osiraseData.getApplStatus().equals(CodeConstant.STATUS_HANNYU_MACHI)) {
				String updateDate = skfDateFormatUtils.dateFormatFromDate(osiraseData.getUpdateDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				String applDate = skfDateFormatUtils.dateFormatFromDate(osiraseData.getApplDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				String message = getOshirase("infomation.skf.i_skf_2061", updateDate, applDate,
						osiraseData.getApplName());
				resultMap.put("message", message);
			} else if (osiraseData.getApplStatus().equals(CodeConstant.STATUS_HANSYUTSU_MACHI)) {
				String updateDate = skfDateFormatUtils.dateFormatFromDate(osiraseData.getUpdateDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				String applDate = skfDateFormatUtils.dateFormatFromDate(osiraseData.getApplDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				String message = getOshirase("infomation.skf.i_skf_2062", updateDate, applDate,
						osiraseData.getApplName());
				resultMap.put("message", message);
			} else {
				continue;
			}
			if (resultMap != null) {
				oshiraseList.add(resultMap);
			}
		}

		return oshiraseList;

	}

	// 個人のお知らせ(管理者)内容取得
	public List<Map<String, Object>> oshiraseAdmin(String companyCd, String shainNo, int dispTerm, String roleId) {
		List<Map<String, Object>> oshiraseList2 = new ArrayList<Map<String, Object>>();
		List<Skf1010Sc001GetShinseiStatusAdminExp> oshiraseDataList2 = new ArrayList<Skf1010Sc001GetShinseiStatusAdminExp>();
		Skf1010Sc001GetShinseiStatusAdminExpParameter param = new Skf1010Sc001GetShinseiStatusAdminExpParameter();

		param.setCompanyCd(companyCd);
		param.setShainNo(shainNo);
		param.setDispTerm(dispTerm);
		param.setRoleId(roleId);
		param.setWfLevel1(workFlowLevel1);
		param.setWfLevel2(workFlowLevel2);
		param.setStatus(CodeConstant.STATUS_SHINSEICHU);
		param.setWf2Status(CodeConstant.STATUS_SHONIN1);

		LogUtils.debugByMsg("個人の知らせ管理。SQL");

		oshiraseDataList2 = skf1010Sc001GetShinseiStatusAdminExpRepository.getShinseiStatusAdmin(param);
		for (Skf1010Sc001GetShinseiStatusAdminExp osiraseData2 : oshiraseDataList2) {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (osiraseData2.getApplStatus().equals(CodeConstant.STATUS_SHINSEICHU)
					|| osiraseData2.getApplStatus().equals(CodeConstant.STATUS_SHINSACHU)) {
				String updateDate = skfDateFormatUtils.dateFormatFromDate(osiraseData2.getUpdateDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				String message = getOshirase("infomation.skf.i_skf_2044", updateDate, osiraseData2.getName(),
						osiraseData2.getShainNo(), osiraseData2.getApplName(), osiraseData2.getAgency(),
						osiraseData2.getAffiliation1());
				resultMap.put("message", message);
			} else if (osiraseData2.getApplStatus().equals(CodeConstant.STATUS_DOI_SHINAI)) {
				String updateDate = skfDateFormatUtils.dateFormatFromDate(osiraseData2.getUpdateDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				String message = getOshirase("infomation.skf.i_skf_2042", updateDate, osiraseData2.getName(),
						osiraseData2.getShainNo(), osiraseData2.getApplName(), osiraseData2.getApplName(),
						osiraseData2.getAgency(), osiraseData2.getAffiliation1());
				resultMap.put("message", message);
			} else if (osiraseData2.getApplStatus().equals(CodeConstant.STATUS_DOI_ZUMI)) {
				String updateDate = skfDateFormatUtils.dateFormatFromDate(osiraseData2.getUpdateDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				String message = getOshirase("infomation.skf.i_skf_2043", updateDate, osiraseData2.getName(),
						osiraseData2.getShainNo(), osiraseData2.getApplName(), osiraseData2.getAgency(),
						osiraseData2.getAffiliation1());
				resultMap.put("message", message);
			} else if (osiraseData2.getApplStatus().equals(CodeConstant.STATUS_SENTAKU_SHINAI)) {
				String updateDate = skfDateFormatUtils.dateFormatFromDate(osiraseData2.getUpdateDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				String message = getOshirase("infomation.skf.i_skf_2050", updateDate, osiraseData2.getName(),
						osiraseData2.getShainNo(), osiraseData2.getApplName(), osiraseData2.getApplName(),
						osiraseData2.getAgency(), osiraseData2.getAffiliation1());
				resultMap.put("message", message);
			} else if (osiraseData2.getApplStatus().equals(CodeConstant.STATUS_SENTAKU_ZUMI)) {
				String updateDate = skfDateFormatUtils.dateFormatFromDate(osiraseData2.getUpdateDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				String message = getOshirase("infomation.skf.i_skf_2051", updateDate, osiraseData2.getName(),
						osiraseData2.getShainNo(), osiraseData2.getApplName(), osiraseData2.getAgency(),
						osiraseData2.getAffiliation1());
				resultMap.put("message", message);
			} else if (osiraseData2.getApplStatus().equals(CodeConstant.STATUS_HANNYU_ZUMI)) {
				String updateDate = skfDateFormatUtils.dateFormatFromDate(osiraseData2.getUpdateDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				String message = getOshirase("infomation.skf.i_skf_2055", updateDate, osiraseData2.getName(),
						osiraseData2.getShainNo(), osiraseData2.getApplName(), osiraseData2.getAgency(),
						osiraseData2.getAffiliation1());
				resultMap.put("message", message);
			} else if (osiraseData2.getApplStatus().equals(CodeConstant.STATUS_HANSYUTSU_ZUMI)) {
				String updateDate = skfDateFormatUtils.dateFormatFromDate(osiraseData2.getUpdateDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				String message = getOshirase("infomation.skf.i_skf_2056", updateDate, osiraseData2.getName(),
						osiraseData2.getShainNo(), osiraseData2.getApplName(), osiraseData2.getAgency(),
						osiraseData2.getAffiliation1());
				resultMap.put("message", message);
			} else if (osiraseData2.getApplStatus().equals(CodeConstant.STATUS_SHONIN1)) {
				String updateDate = skfDateFormatUtils.dateFormatFromDate(osiraseData2.getUpdateDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
				String message = getOshirase("infomation.skf.i_skf_2044", updateDate, osiraseData2.getName(),
						osiraseData2.getShainNo(), osiraseData2.getApplName(), osiraseData2.getAgency(),
						osiraseData2.getAffiliation1());
				resultMap.put("message", message);
			} else {
				continue;
			}

			if (resultMap != null) {
				oshiraseList2.add(resultMap);
			}
		}
		return oshiraseList2;
	}

	// 個人のお知らせの内容の文章を整形
	public String getOshirase(String messageId, String... words) {
		String message = PropertyUtils.getValue(messageId);
		for (int i = 0; i < words.length; i++) {
			if (words[i] == null) {
				words[i] = "";
			}
			message = message.replace("{" + i + "}", words[i]);
		}
		return message;

	}
}
