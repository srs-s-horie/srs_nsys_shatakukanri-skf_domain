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
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountBihinHenkyakuAgreedExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountBihinHenkyakuAgreedExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountBihinHenkyakuUnfinishedCheckExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountBihinHenkyakuUnfinishedCheckExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountBihinHenkyakuUnfinishedTipsExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountBihinHenkyakuUnfinishedTipsExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountBihinKiboNotApplicationExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountBihinKiboNotApplicationExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountBihinKiboUnfinishedEmplacementExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountBihinKiboUnfinishedEmplacementExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountBihinKiboUnfinishedTipsExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountBihinKiboUnfinishedTipsExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountBihinUnfinishedMovementExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountBihinUnfinishedMovementExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountNyukyoAgreedExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountNyukyoAgreedExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountNyukyoApplicationExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountNyukyoApplicationExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountNyukyoDisagreedExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountNyukyoDisagreedExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountNyukyoUnfinishedCheckExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountNyukyoUnfinishedCheckExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountNyukyoUnfinishedTipsExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountNyukyoUnfinishedTipsExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountNyutaikyo1Exp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountNyutaikyo1ExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountNyutaikyo2Exp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountNyutaikyo2ExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountTaikyoApplicationExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountTaikyoApplicationExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf1010Sc001.Skf1010Sc001GetInformationNewInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountBihinHenkyakuAgreedExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountBihinHenkyakuUnfinishedCheckExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountBihinHenkyakuUnfinishedTipsExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountBihinKiboNotApplicationExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountBihinKiboUnfinishedEmplacementExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountBihinKiboUnfinishedTipsExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountBihinUnfinishedMovementExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountNyukyoAgreedExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountNyukyoApplicationExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountNyukyoDisagreedExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountNyukyoUnfinishedCheckExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountNyukyoUnfinishedTipsExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountNyutaikyo1ExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountNyutaikyo2ExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf1010Sc001.Skf1010Sc001GetOshiraseCountTaikyoApplicationExpRepository;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.skf1010.domain.dto.skf1010sc001.Skf1010Sc001InitDto;

/**
 * 社宅管理TOP画面のInitサービス処理クラス。
 * 
 */
@Service
public class Skf1010Sc001InitService extends BaseServiceAbstract<Skf1010Sc001InitDto> {

	/** 定数 */
	// 申請者
	private static final String APPLICANTS = "SKF_001";
	// 社宅管理業務者
	private static final String SYATAKU_ADMINISTRATOR = "SKF_010";
	// システム管理者
	private static final String SYSTEM_ADMINISTRATOR = "SKF_090";
	// 中サ（給与・厚生）担当
	private static final String NAKANIHON_SALARY_WELFARE = "SKF_020";
	// 中サ（社宅）担当
	private static final String NAKANIHON_SYATAKU = "SKF_021";
	// 中サ（社宅）管理者
	private static final String NAKANIHON_ADIMINISTRATOR = "SKF_030";
	// 会社コード（中日本）
	private final String COMPANYCD = "C001";

	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;

	@Autowired
	private Skf1010Sc001GetInformationNewInfoExpRepository skf1010Sc001GetInformationNewInfoExpRepository;

	@Autowired
	private Skf1010Sc001GetOshiraseCountNyutaikyo1ExpRepository skf1010Sc001GetOshiraseCountNyutaikyo1ExpRepository;

	@Autowired
	private Skf1010Sc001GetOshiraseCountNyutaikyo2ExpRepository skf1010Sc001GetOshiraseCountNyutaikyo2ExpRepository;

	@Autowired
	private Skf1010Sc001GetOshiraseCountNyukyoApplicationExpRepository skf1010Sc001GetOshiraseCountNyukyoApplicationExpRepository;

	@Autowired
	private Skf1010Sc001GetOshiraseCountNyukyoUnfinishedTipsExpRepository skf1010Sc001GetOshiraseCountNyukyoUnfinishedTipsExpRepository;

	@Autowired
	private Skf1010Sc001GetOshiraseCountNyukyoUnfinishedCheckExpRepository skf1010Sc001GetOshiraseCountNyukyoUnfinishedCheckExpRepository;

	@Autowired
	private Skf1010Sc001GetOshiraseCountNyukyoAgreedExpRepository skf1010Sc001GetOshiraseCountNyukyoAgreedExpRepository;

	@Autowired
	private Skf1010Sc001GetOshiraseCountNyukyoDisagreedExpRepository skf1010Sc001GetOshiraseCountNyukyoDisagreedExpRepository;

	@Autowired
	private Skf1010Sc001GetOshiraseCountTaikyoApplicationExpRepository skf1010Sc001GetOshiraseCountTaikyoApplicationExpRepository;

	@Autowired
	private Skf1010Sc001GetOshiraseCountBihinKiboNotApplicationExpRepository skf1010Sc001GetOshiraseCountBihinKiboNotApplicationExpRepository;

	@Autowired
	private Skf1010Sc001GetOshiraseCountBihinKiboUnfinishedTipsExpRepository skf1010Sc001GetOshiraseCountBihinKiboUnfinishedTipsExpRepository;

	@Autowired
	private Skf1010Sc001GetOshiraseCountBihinKiboUnfinishedEmplacementExpRepository skf1010Sc001GetOshiraseCountBihinKiboUnfinishedEmplacementExpRepository;

	@Autowired
	private Skf1010Sc001GetOshiraseCountBihinHenkyakuUnfinishedTipsExpRepository skf1010Sc001GetOshiraseCountBihinHenkyakuUnfinishedTipsExpRepository;

	@Autowired
	private Skf1010Sc001GetOshiraseCountBihinHenkyakuUnfinishedCheckExpRepository skf1010Sc001GetOshiraseCountBihinHenkyakuUnfinishedCheckExpRepository;

	@Autowired
	private Skf1010Sc001GetOshiraseCountBihinHenkyakuAgreedExpRepository skf1010Sc001GetOshiraseCountBihinHenkyakuAgreedExpRepository;

	@Autowired
	private Skf1010Sc001GetOshiraseCountBihinUnfinishedMovementExpRepository skf1010Sc001GetOshiraseCountBihinUnfinishedMovementExpRepository;

	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf1010Sc001InitDto index(Skf1010Sc001InitDto initDto) throws Exception {

		// 申請全体
		String level1 = CodeConstant.DOUBLE_QUOTATION;
		// 窓口全体
		String level2 = CodeConstant.DOUBLE_QUOTATION;
		// 窓口（申請書類を承認する）
		String level2_1 = CodeConstant.DOUBLE_QUOTATION;
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
		// 操作に困ったときは全体
		String level4 = CodeConstant.DOUBLE_QUOTATION;
		// 操作に困ったときは（マニュアル 管理者）
		String level4_1 = CodeConstant.DOUBLE_QUOTATION;
		// 未承認処理（全体）
		String level5 = CodeConstant.DOUBLE_QUOTATION;
		String note = CodeConstant.DOUBLE_QUOTATION;

		initDto.setPageTitleKey(MessageIdConstant.SKF1010_SC001_TITLE);

		/** ログインユーザの情報を取得 */
		Map<String, String> LoginUserInfoMap = new HashMap<String, String>();
		LoginUserInfoMap = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		// ログインユーザのロールIDを取得
		String roleId = LoginUserInfoMap.get("roleId");
		// ログインユーザのユーザ名を取得する
		String userName = LoginUserInfoMap.get("userName");
		initDto.setUserName(userName);

		/**
		 * ログインユーザのロールIDによって表示する画面を切り替える。<br>
		 * 表示しないものはtrue、表示するものはfalse。
		 */
		// 申請者
		if (APPLICANTS.equals(roleId)) {
			level2 = "true";
			level3 = "true";
			level4_1 = "true";
			level5 = "true";
			initDto.setLevel2(level2);
			initDto.setLevel3(level3);
			initDto.setLevel4_1(level4_1);
			initDto.setLevel5(level5);
		}
		// 社宅管理業務者又はシステム管理者
		else if (SYATAKU_ADMINISTRATOR.equals(roleId) || SYSTEM_ADMINISTRATOR.equals(roleId)) {

		}
		// 中サ（給与・厚生）担当
		else if (NAKANIHON_SALARY_WELFARE.equals(roleId)) {
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
		else if (NAKANIHON_SYATAKU.equals(roleId)) {
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
		else if (NAKANIHON_ADIMINISTRATOR.equals(roleId)) {
			level2_6 = "true";
			level3 = "true";
			initDto.setLevel2_6(level2_6);
			initDto.setLevel3(level3);
		}

		/** システムに関するお知らせ取得 */
		getInformation(initDto);

		/**
		 * 未処理情報 社宅入居希望等調書の申請が無い入居情報があります。督促メールを送信してください。
		 */
		String nyutaikyoCount1 = String.valueOf(getOshiraseCount1());
		initDto.setNyutaikyoCount1(nyutaikyoCount1);

		String nyutaikyoCount2 = String.valueOf(getOshiraseCount2());
		initDto.setNyutaikyoCount2(nyutaikyoCount2);

		// 社宅入居希望等調書の申請の件数取得
		nyukyoApplication(initDto);

		// 入居希望者への社宅提示未完了の件数取得
		nyukyoUnfinishedTips(initDto);

		// 提示社宅の本人確認が未完了の件数取得
		nyukyoUnfinishedCheck(initDto);

		// 入居希望者の同意済の件数取得
		nyukyoAgreed(initDto);

		// 入居希望者の同意されなかった件数の取得
		nyukyoDisagreed(initDto);

		// 退居届の申請の件数取得
		taikyoApplication(initDto);

		// 備品希望の申請が無い入居情報の件数取得
		bihinKiboNotApplication(initDto);

		// 備品希望の提示が未完了の件数取得
		bihinKiboUnfinishedTips(initDto);

		// 備品希望の搬入が未完了の件数の取得
		bihinKiboUnfinishedEmplacement(initDto);

		// 備品返却の提示が未完了の件数の取得
		bihinHenkyakuUnfinishedTips(initDto);

		// 備品返却提示の本人確認が未完了の件数の取得
		bihinHenkyakuUnfinishedCheck(initDto);

		// 備品返却提示の同意済の件数の取得
		bihinHenkyakuAgreed(initDto);

		// 備品の搬出が未完了の件数の取得
		bihinUnfinishedMovement(initDto);

		return initDto;
	}

	/** システムに関するお知らせ */
	public void getInformation(Skf1010Sc001InitDto initDto) {
		Skf1010Sc001GetInformationNewInfoExp resultData = new Skf1010Sc001GetInformationNewInfoExp();
		Skf1010Sc001GetInformationNewInfoExpParameter param = new Skf1010Sc001GetInformationNewInfoExpParameter();

		param.setCompanyCd(COMPANYCD);
		resultData = skf1010Sc001GetInformationNewInfoExpRepository.getInformationNewInfo(param);
		initDto.setNote(resultData.getNote());
	}

	public int getOshiraseCount1() {
		// 入退居区分
		String nyutaikyoKbn = "1";
		// 入退居申請状況区分
		String nyutaikyoApplStatusKbn = CodeConstant.DOUBLE_QUOTATION;

		int expr1 = 0;

		Skf1010Sc001GetOshiraseCountNyutaikyo1Exp nyutaikyoData1 = new Skf1010Sc001GetOshiraseCountNyutaikyo1Exp();
		Skf1010Sc001GetOshiraseCountNyutaikyo1ExpParameter param1 = new Skf1010Sc001GetOshiraseCountNyutaikyo1ExpParameter();

		param1.setNyutaikyoKbn(nyutaikyoKbn);
		param1.setNyutaikyoApplStatusKbn(nyutaikyoApplStatusKbn);

		nyutaikyoData1 = skf1010Sc001GetOshiraseCountNyutaikyo1ExpRepository.getOshiraseCountNyutaikyo1(param1);

		if (nyutaikyoData1 != null) {
			expr1 += nyutaikyoData1.getExpr1();
		}

		String nyutaikyoApplStatusKbnSave = "00";
		String nyutaikyoApplStatusKbnRepair = "50";

		Skf1010Sc001GetOshiraseCountNyutaikyo2Exp nyutaikyoData2 = new Skf1010Sc001GetOshiraseCountNyutaikyo2Exp();
		Skf1010Sc001GetOshiraseCountNyutaikyo2ExpParameter param2 = new Skf1010Sc001GetOshiraseCountNyutaikyo2ExpParameter();

		param2.setNyutaikyoKbn(nyutaikyoKbn);
		param2.setNyutaikyoApplStatusKbn(nyutaikyoApplStatusKbnSave);
		nyutaikyoData2 = skf1010Sc001GetOshiraseCountNyutaikyo2ExpRepository.getOshiraseCountNyutaikyo2(param2);

		if (nyutaikyoData2 != null) {
			expr1 += nyutaikyoData2.getExpr1();
		}

		Skf1010Sc001GetOshiraseCountNyutaikyo2Exp nyutaikyoData3 = new Skf1010Sc001GetOshiraseCountNyutaikyo2Exp();
		Skf1010Sc001GetOshiraseCountNyutaikyo2ExpParameter param3 = new Skf1010Sc001GetOshiraseCountNyutaikyo2ExpParameter();

		param3.setNyutaikyoKbn(nyutaikyoKbn);
		param3.setNyutaikyoApplStatusKbn(nyutaikyoApplStatusKbnRepair);

		nyutaikyoData3 = skf1010Sc001GetOshiraseCountNyutaikyo2ExpRepository.getOshiraseCountNyutaikyo2(param3);

		if (nyutaikyoData3 != null) {
			expr1 += nyutaikyoData3.getExpr1();
		}

		return expr1;
	}

	public int getOshiraseCount2() {
		// 入退居区分
		String nyutaikyoKbn = "2";
		// 入退居申請状況区分
		String nyutaikyoApplStatusKbn = CodeConstant.DOUBLE_QUOTATION;

		int expr1 = 0;

		Skf1010Sc001GetOshiraseCountNyutaikyo1Exp nyutaikyoData1 = new Skf1010Sc001GetOshiraseCountNyutaikyo1Exp();
		Skf1010Sc001GetOshiraseCountNyutaikyo1ExpParameter param1 = new Skf1010Sc001GetOshiraseCountNyutaikyo1ExpParameter();

		param1.setNyutaikyoKbn(nyutaikyoKbn);
		param1.setNyutaikyoApplStatusKbn(nyutaikyoApplStatusKbn);

		nyutaikyoData1 = skf1010Sc001GetOshiraseCountNyutaikyo1ExpRepository.getOshiraseCountNyutaikyo1(param1);

		if (nyutaikyoData1 != null) {
			expr1 += nyutaikyoData1.getExpr1();
		}

		String nyutaikyoApplStatusKbnSave = "00";
		String nyutaikyoApplStatusKbnRepair = "50";

		Skf1010Sc001GetOshiraseCountNyutaikyo2Exp nyutaikyoData2 = new Skf1010Sc001GetOshiraseCountNyutaikyo2Exp();
		Skf1010Sc001GetOshiraseCountNyutaikyo2ExpParameter param2 = new Skf1010Sc001GetOshiraseCountNyutaikyo2ExpParameter();

		param2.setNyutaikyoKbn(nyutaikyoKbn);
		param2.setNyutaikyoApplStatusKbn(nyutaikyoApplStatusKbnSave);
		nyutaikyoData2 = skf1010Sc001GetOshiraseCountNyutaikyo2ExpRepository.getOshiraseCountNyutaikyo2(param2);

		if (nyutaikyoData2 != null) {
			expr1 += nyutaikyoData2.getExpr1();
		}

		Skf1010Sc001GetOshiraseCountNyutaikyo2Exp nyutaikyoData3 = new Skf1010Sc001GetOshiraseCountNyutaikyo2Exp();
		Skf1010Sc001GetOshiraseCountNyutaikyo2ExpParameter param3 = new Skf1010Sc001GetOshiraseCountNyutaikyo2ExpParameter();

		param3.setNyutaikyoKbn(nyutaikyoKbn);
		param3.setNyutaikyoApplStatusKbn(nyutaikyoApplStatusKbnRepair);

		nyutaikyoData3 = skf1010Sc001GetOshiraseCountNyutaikyo2ExpRepository.getOshiraseCountNyutaikyo2(param3);

		if (nyutaikyoData3 != null) {
			expr1 += nyutaikyoData3.getExpr1();
		}

		return expr1;
	}

	/**
	 * 未処理情報 社宅入居希望等調書の申請の件数
	 */
	public void nyukyoApplication(Skf1010Sc001InitDto initDto) {
		List<Skf1010Sc001GetOshiraseCountNyukyoApplicationExp> nyukyoApplicationData = new ArrayList<Skf1010Sc001GetOshiraseCountNyukyoApplicationExp>();
		Skf1010Sc001GetOshiraseCountNyukyoApplicationExpParameter param = new Skf1010Sc001GetOshiraseCountNyukyoApplicationExpParameter();

		nyukyoApplicationData = skf1010Sc001GetOshiraseCountNyukyoApplicationExpRepository
				.getOshiraseCountNyukyoApplication(param);
		initDto.setNyukyoApplicationHonsya(nyukyoApplicationData.get(0).getCnt());
		initDto.setNyukyoApplicationTokyo(nyukyoApplicationData.get(1).getCnt());
		initDto.setNyukyoApplicationHatiouzi(nyukyoApplicationData.get(2).getCnt());
		initDto.setNyukyoApplicationNagoya(nyukyoApplicationData.get(3).getCnt());
		initDto.setNyukyoApplicationKanazawa(nyukyoApplicationData.get(4).getCnt());
	}

	/**
	 * 未処理情報 入居希望者への社宅の提示が未完了の件数
	 */
	public void nyukyoUnfinishedTips(Skf1010Sc001InitDto initDto) {
		List<Skf1010Sc001GetOshiraseCountNyukyoUnfinishedTipsExp> nyukyoUnfinishedTipsData = new ArrayList<Skf1010Sc001GetOshiraseCountNyukyoUnfinishedTipsExp>();
		Skf1010Sc001GetOshiraseCountNyukyoUnfinishedTipsExpParameter param = new Skf1010Sc001GetOshiraseCountNyukyoUnfinishedTipsExpParameter();

		nyukyoUnfinishedTipsData = skf1010Sc001GetOshiraseCountNyukyoUnfinishedTipsExpRepository
				.getOshiraseCountNyukyoUnfinishedTips(param);
		initDto.setNyukyoUnfinishedTipsHonsya(nyukyoUnfinishedTipsData.get(0).getCnt());
		initDto.setNyukyoUnfinishedTipsTokyo(nyukyoUnfinishedTipsData.get(1).getCnt());
		initDto.setNyukyoUnfinishedTipsHatiouzi(nyukyoUnfinishedTipsData.get(2).getCnt());
		initDto.setNyukyoUnfinishedTipsNagoya(nyukyoUnfinishedTipsData.get(3).getCnt());
		initDto.setNyukyoUnfinishedTipsKanazawa(nyukyoUnfinishedTipsData.get(4).getCnt());

	}

	/**
	 * 未処理情報 提示社宅の本人確認が未完了の件数
	 */
	public void nyukyoUnfinishedCheck(Skf1010Sc001InitDto initDto) {
		List<Skf1010Sc001GetOshiraseCountNyukyoUnfinishedCheckExp> nyukyoUnfinishedCheckData = new ArrayList<Skf1010Sc001GetOshiraseCountNyukyoUnfinishedCheckExp>();
		Skf1010Sc001GetOshiraseCountNyukyoUnfinishedCheckExpParameter param = new Skf1010Sc001GetOshiraseCountNyukyoUnfinishedCheckExpParameter();

		nyukyoUnfinishedCheckData = skf1010Sc001GetOshiraseCountNyukyoUnfinishedCheckExpRepository
				.getOshiraseCountNyukyoUnfinishedCheck(param);
		initDto.setNyukyoUnfinishedCheckHonsya(nyukyoUnfinishedCheckData.get(0).getCnt());
		initDto.setNyukyoUnfinishedCheckTokyo(nyukyoUnfinishedCheckData.get(1).getCnt());
		initDto.setNyukyoUnfinishedCheckHatiouzi(nyukyoUnfinishedCheckData.get(2).getCnt());
		initDto.setNyukyoUnfinishedCheckNagoya(nyukyoUnfinishedCheckData.get(3).getCnt());
		initDto.setNyukyoUnfinishedCheckKanazawa(nyukyoUnfinishedCheckData.get(4).getCnt());
	}

	/**
	 * 未処理情報 入居希望者の同意済の件数
	 */
	public void nyukyoAgreed(Skf1010Sc001InitDto initDto) {
		List<Skf1010Sc001GetOshiraseCountNyukyoAgreedExp> nyukyoAgreedData = new ArrayList<Skf1010Sc001GetOshiraseCountNyukyoAgreedExp>();
		Skf1010Sc001GetOshiraseCountNyukyoAgreedExpParameter param = new Skf1010Sc001GetOshiraseCountNyukyoAgreedExpParameter();

		nyukyoAgreedData = skf1010Sc001GetOshiraseCountNyukyoAgreedExpRepository.getOshiraseCountNyukyoAgreed(param);
		initDto.setNyukyoAgreedHonsya(nyukyoAgreedData.get(0).getCnt());
		initDto.setNyukyoAgreedTokyo(nyukyoAgreedData.get(1).getCnt());
		initDto.setNyukyoAgreedHatiouzi(nyukyoAgreedData.get(2).getCnt());
		initDto.setNyukyoAgreedNagoya(nyukyoAgreedData.get(3).getCnt());
		initDto.setNyukyoAgreedKanazawa(nyukyoAgreedData.get(4).getCnt());
	}

	/**
	 * 未処理情報 入居希望者の同意されなかった件数
	 */
	public void nyukyoDisagreed(Skf1010Sc001InitDto initDto) {
		List<Skf1010Sc001GetOshiraseCountNyukyoDisagreedExp> nyukyoDisagreedData = new ArrayList<Skf1010Sc001GetOshiraseCountNyukyoDisagreedExp>();
		Skf1010Sc001GetOshiraseCountNyukyoDisagreedExpParameter param = new Skf1010Sc001GetOshiraseCountNyukyoDisagreedExpParameter();

		nyukyoDisagreedData = skf1010Sc001GetOshiraseCountNyukyoDisagreedExpRepository
				.getOshiraseCountNyukyoDisagreed(param);
		initDto.setNyukyoDisagreedHonsya(nyukyoDisagreedData.get(0).getCnt());
		initDto.setNyukyoDisagreedTokyo(nyukyoDisagreedData.get(1).getCnt());
		initDto.setNyukyoDisagreedHatiouzi(nyukyoDisagreedData.get(2).getCnt());
		initDto.setNyukyoDisagreedNagoya(nyukyoDisagreedData.get(3).getCnt());
		initDto.setNyukyoDisagreedKanazawa(nyukyoDisagreedData.get(4).getCnt());
	}

	/**
	 * 未処理情報 退居届の申請の件数
	 */
	public void taikyoApplication(Skf1010Sc001InitDto initDto) {
		List<Skf1010Sc001GetOshiraseCountTaikyoApplicationExp> taikyoApplicationData = new ArrayList<Skf1010Sc001GetOshiraseCountTaikyoApplicationExp>();
		Skf1010Sc001GetOshiraseCountTaikyoApplicationExpParameter param = new Skf1010Sc001GetOshiraseCountTaikyoApplicationExpParameter();

		taikyoApplicationData = skf1010Sc001GetOshiraseCountTaikyoApplicationExpRepository
				.getOshiraseCountTaikyoApplication(param);
		initDto.setTaikyoApplicationHonsya(taikyoApplicationData.get(0).getCnt());
		initDto.setTaikyoApplicationTokyo(taikyoApplicationData.get(1).getCnt());
		initDto.setTaikyoApplicationHatiouzi(taikyoApplicationData.get(2).getCnt());
		initDto.setTaikyoApplicationNagoya(taikyoApplicationData.get(3).getCnt());
		initDto.setTaikyoApplicationKanazawa(taikyoApplicationData.get(4).getCnt());
	}

	/**
	 * 未処理情報 備品希望の申請が無い入居情報の件数
	 */
	public void bihinKiboNotApplication(Skf1010Sc001InitDto initDto) {
		List<Skf1010Sc001GetOshiraseCountBihinKiboNotApplicationExp> bihinKiboNotApplicationData = new ArrayList<Skf1010Sc001GetOshiraseCountBihinKiboNotApplicationExp>();
		Skf1010Sc001GetOshiraseCountBihinKiboNotApplicationExpParameter param = new Skf1010Sc001GetOshiraseCountBihinKiboNotApplicationExpParameter();

		bihinKiboNotApplicationData = skf1010Sc001GetOshiraseCountBihinKiboNotApplicationExpRepository
				.getOshiraseCountBihinKiboNotApplication(param);
		initDto.setBihinNotApplicationHonsya(bihinKiboNotApplicationData.get(0).getCnt());
		initDto.setBihinNotApplicationTokyo(bihinKiboNotApplicationData.get(1).getCnt());
		initDto.setBihinNotApplicationHatiouzi(bihinKiboNotApplicationData.get(2).getCnt());
		initDto.setBihinNotApplicationNagoya(bihinKiboNotApplicationData.get(3).getCnt());
		initDto.setBihinNotApplicationKanazawa(bihinKiboNotApplicationData.get(4).getCnt());
	}

	/**
	 * 未処理情報 備品希望の提示が未完了の件数
	 */
	public void bihinKiboUnfinishedTips(Skf1010Sc001InitDto initDto) {
		List<Skf1010Sc001GetOshiraseCountBihinKiboUnfinishedTipsExp> bihinKiboUnfinishedTipsData = new ArrayList<Skf1010Sc001GetOshiraseCountBihinKiboUnfinishedTipsExp>();
		Skf1010Sc001GetOshiraseCountBihinKiboUnfinishedTipsExpParameter param = new Skf1010Sc001GetOshiraseCountBihinKiboUnfinishedTipsExpParameter();

		bihinKiboUnfinishedTipsData = skf1010Sc001GetOshiraseCountBihinKiboUnfinishedTipsExpRepository
				.getOshiraseCountBihinKiboUnfinishedTips(param);
		initDto.setBihinKiboUnfinishedTipsHonsya(bihinKiboUnfinishedTipsData.get(0).getCnt());
		initDto.setBihinKiboUnfinishedTipsTokyo(bihinKiboUnfinishedTipsData.get(1).getCnt());
		initDto.setBihinKiboUnfinishedTipsHatiouzi(bihinKiboUnfinishedTipsData.get(2).getCnt());
		initDto.setBihinKiboUnfinishedTipsNagoya(bihinKiboUnfinishedTipsData.get(3).getCnt());
		initDto.setBihinKiboUnfinishedTipsKanazawa(bihinKiboUnfinishedTipsData.get(4).getCnt());
	}

	/**
	 * 未処理情報 備品希望の搬入が未完了の件数
	 */
	public void bihinKiboUnfinishedEmplacement(Skf1010Sc001InitDto initDto) {
		List<Skf1010Sc001GetOshiraseCountBihinKiboUnfinishedEmplacementExp> bihinKinoUnfinishedEmplacementData = new ArrayList<Skf1010Sc001GetOshiraseCountBihinKiboUnfinishedEmplacementExp>();
		Skf1010Sc001GetOshiraseCountBihinKiboUnfinishedEmplacementExpParameter param = new Skf1010Sc001GetOshiraseCountBihinKiboUnfinishedEmplacementExpParameter();

		bihinKinoUnfinishedEmplacementData = skf1010Sc001GetOshiraseCountBihinKiboUnfinishedEmplacementExpRepository
				.getOshiraseCountBihinKiboUnfinishedEmplacement(param);
		initDto.setBihinKiboUnfinishedEmplacementHonsya(bihinKinoUnfinishedEmplacementData.get(0).getCnt());
		initDto.setBihinKiboUnfinishedEmplacementTokyo(bihinKinoUnfinishedEmplacementData.get(1).getCnt());
		initDto.setBihinKiboUnfinishedEmplacementHatiouzi(bihinKinoUnfinishedEmplacementData.get(2).getCnt());
		initDto.setBihinKiboUnfinishedEmplacementNagoya(bihinKinoUnfinishedEmplacementData.get(3).getCnt());
		initDto.setBihinKiboUnfinishedEmplacementKanazawa(bihinKinoUnfinishedEmplacementData.get(4).getCnt());
	}

	/**
	 * 未処理情報 備品返却の提示が未完了の件数
	 */
	public void bihinHenkyakuUnfinishedTips(Skf1010Sc001InitDto initDto) {
		List<Skf1010Sc001GetOshiraseCountBihinHenkyakuUnfinishedTipsExp> bihinHenkyakuUnfinishedTipsData = new ArrayList<Skf1010Sc001GetOshiraseCountBihinHenkyakuUnfinishedTipsExp>();
		Skf1010Sc001GetOshiraseCountBihinHenkyakuUnfinishedTipsExpParameter param = new Skf1010Sc001GetOshiraseCountBihinHenkyakuUnfinishedTipsExpParameter();

		bihinHenkyakuUnfinishedTipsData = skf1010Sc001GetOshiraseCountBihinHenkyakuUnfinishedTipsExpRepository
				.getOshiraseCountBihinHenkyakuUnfinishedTips(param);
		initDto.setBihinHenkyakuUnfinishedTipsHonsya(bihinHenkyakuUnfinishedTipsData.get(0).getCnt());
		initDto.setBihinHenkyakuUnfinishedTipsTokyo(bihinHenkyakuUnfinishedTipsData.get(1).getCnt());
		initDto.setBihinHenkyakuUnfinishedTipsHatiouzi(bihinHenkyakuUnfinishedTipsData.get(2).getCnt());
		initDto.setBihinHenkyakuUnfinishedTipsNagoya(bihinHenkyakuUnfinishedTipsData.get(3).getCnt());
		initDto.setBihinHenkyakuUnfinishedTipsKanazawa(bihinHenkyakuUnfinishedTipsData.get(4).getCnt());
	}

	/**
	 * 未処理情報 備品返却提示の本人確認が未完了の件数
	 */
	public void bihinHenkyakuUnfinishedCheck(Skf1010Sc001InitDto initDto) {
		List<Skf1010Sc001GetOshiraseCountBihinHenkyakuUnfinishedCheckExp> bihinHenkyakuUnfinishedCheckData = new ArrayList<Skf1010Sc001GetOshiraseCountBihinHenkyakuUnfinishedCheckExp>();
		Skf1010Sc001GetOshiraseCountBihinHenkyakuUnfinishedCheckExpParameter param = new Skf1010Sc001GetOshiraseCountBihinHenkyakuUnfinishedCheckExpParameter();

		bihinHenkyakuUnfinishedCheckData = skf1010Sc001GetOshiraseCountBihinHenkyakuUnfinishedCheckExpRepository
				.getOshiraseCountBihinHenkyakuUnfinishedCheck(param);
		initDto.setBihinHenkyakuUnfinishedCheckHonsya(bihinHenkyakuUnfinishedCheckData.get(0).getCnt());
		initDto.setBihinHenkyakuUnfinishedCheckTokyo(bihinHenkyakuUnfinishedCheckData.get(1).getCnt());
		initDto.setBihinHenkyakuUnfinishedCheckHatiouzi(bihinHenkyakuUnfinishedCheckData.get(2).getCnt());
		initDto.setBihinHenkyakuUnfinishedCheckNagoya(bihinHenkyakuUnfinishedCheckData.get(3).getCnt());
		initDto.setBihinHenkyakuUnfinishedCheckKanazawa(bihinHenkyakuUnfinishedCheckData.get(4).getCnt());
	}

	/**
	 * 未処理情報 備品返却提示の同意済の件数
	 */
	public void bihinHenkyakuAgreed(Skf1010Sc001InitDto initDto) {
		List<Skf1010Sc001GetOshiraseCountBihinHenkyakuAgreedExp> bihinHenkyakuAgreedData = new ArrayList<Skf1010Sc001GetOshiraseCountBihinHenkyakuAgreedExp>();
		Skf1010Sc001GetOshiraseCountBihinHenkyakuAgreedExpParameter param = new Skf1010Sc001GetOshiraseCountBihinHenkyakuAgreedExpParameter();

		bihinHenkyakuAgreedData = skf1010Sc001GetOshiraseCountBihinHenkyakuAgreedExpRepository
				.getOshiraseCountBihinHenkyakuAgreed(param);
		initDto.setBihinHenkyakuAgreedHonsya(bihinHenkyakuAgreedData.get(0).getCnt());
		initDto.setBihinHenkyakuAgreedTokyo(bihinHenkyakuAgreedData.get(1).getCnt());
		initDto.setBihinHenkyakuAgreedHatiouzi(bihinHenkyakuAgreedData.get(2).getCnt());
		initDto.setBihinHenkyakuAgreedNagoya(bihinHenkyakuAgreedData.get(3).getCnt());
		initDto.setBihinHenkyakuAgreedKanazawa(bihinHenkyakuAgreedData.get(4).getCnt());
	}

	/**
	 * 未処理情報 備品の搬出が未完了の件数
	 */
	public void bihinUnfinishedMovement(Skf1010Sc001InitDto initDto) {
		List<Skf1010Sc001GetOshiraseCountBihinUnfinishedMovementExp> bihinUnfinishedMovementData = new ArrayList<Skf1010Sc001GetOshiraseCountBihinUnfinishedMovementExp>();
		Skf1010Sc001GetOshiraseCountBihinUnfinishedMovementExpParameter param = new Skf1010Sc001GetOshiraseCountBihinUnfinishedMovementExpParameter();

		bihinUnfinishedMovementData = skf1010Sc001GetOshiraseCountBihinUnfinishedMovementExpRepository
				.getOshiraseCountBihinUnfinishedMovement(param);
		initDto.setBihinUnfinishedMovementHonsya(bihinUnfinishedMovementData.get(0).getCnt());
		initDto.setBihinUnfinishedMovementTokyo(bihinUnfinishedMovementData.get(1).getCnt());
		initDto.setBihinUnfinishedMovementHatiouzi(bihinUnfinishedMovementData.get(2).getCnt());
		initDto.setBihinUnfinishedMovementNagoya(bihinUnfinishedMovementData.get(3).getCnt());
		initDto.setBihinUnfinishedMovementKanazawa(bihinUnfinishedMovementData.get(4).getCnt());
	}
}
