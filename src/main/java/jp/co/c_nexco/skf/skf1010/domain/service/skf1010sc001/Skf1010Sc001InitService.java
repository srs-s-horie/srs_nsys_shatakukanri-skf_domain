/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf1010.domain.service.skf1010sc001;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetInformationNewInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Sc001.Skf1010Sc001GetInformationNewInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf1010Sc001.Skf1010Sc001GetInformationNewInfoExpRepository;
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
		Skf1010Sc001GetInformationNewInfoExp resultData = new Skf1010Sc001GetInformationNewInfoExp();
		Skf1010Sc001GetInformationNewInfoExpParameter param = new Skf1010Sc001GetInformationNewInfoExpParameter();

		param.setCompanyCd(COMPANYCD);
		resultData = skf1010Sc001GetInformationNewInfoExpRepository.getInformationNewInfo(param);
		initDto.setNote(resultData.getNote());

		return initDto;
	}

}
