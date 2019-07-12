/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc005;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShain;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc005.Skf3090Sc005InitDto;
import jp.co.c_nexco.skf.skf3090.domain.service.common.Skf309030CommonSharedService;

/**
 * Skf3090Sc005InitService 従業員マスタ登録初期表示処理クラス.
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3090Sc005InitService extends BaseServiceAbstract<Skf3090Sc005InitDto> {

	// 戻り値Map用定数
	private static final String KEY_COMPANY_CD = "COMPANY_CODE";
	private static final String KEY_SHAIN_NO = "SHAIN_NO";
	private static final String KEY_NAME = "NAME";
	private static final String KEY_NAME_KK = "NAME_KK";
	private static final String KEY_MAIL_ADDRESS = "MAIL_ADDRESS";
	private static final String KEY_RETIRE_DATE = "RETIRE_DATE";
	private static final String KEY_ORIGINAL_COMPANY_CD = "ORIGINAL_COMPANY_CD";
	private static final String KEY_ROLE_ID = "ROLE_ID";
	private static final String KEY_REGIST_FLG = "REGIST_FLG";
	private static final String KEY_UPDATE_DATE = "UPDATE_DATE";

	// 社宅管理登録フラグ
	private static final String REGIST_FLAG_SHATAKU = "1";

	@Autowired
	private Skf3090Sc005SharedService skf3090Sc005SharedService;

	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;

	/**
	 * 画面初期表示のメイン処理
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BaseDto index(Skf3090Sc005InitDto initDto) throws Exception {

		// エラー系のDto値を初期化
		initDto.setShainNoError(CodeConstant.DOUBLE_QUOTATION);
		initDto.setNameError(CodeConstant.DOUBLE_QUOTATION);
		initDto.setNameKkError(CodeConstant.DOUBLE_QUOTATION);
		initDto.setMailAddressError(CodeConstant.DOUBLE_QUOTATION);
		initDto.setRetireDateError(CodeConstant.DOUBLE_QUOTATION);
		initDto.setOriginalCompanyCdError(CodeConstant.DOUBLE_QUOTATION);

		// ドロップダウンリスト用リストのインスタンス生成
		List<Map<String, Object>> companyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> agencyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> affiliation1List = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> affiliation2List = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> businessAreaList = new ArrayList<Map<String, Object>>();

		// 社員番号テキストボックス操作可否設定
		String shainNoDisabled = CodeConstant.DOUBLE_QUOTATION;
		// 削除ボタン表示設定
		String deleteRemoveFlag = "false";

		if (Skf309030CommonSharedService.UPDATE_FLAG_NEW.equals(initDto.getUpdateFlag())) {
			/** 新規ボタンから遷移 */
			// ドロップダウンリストを取得する
			Map<String, Object> returnMap = skf3090Sc005SharedService.getDropDownLists(initDto.getOriginalCompanyCd(),
					initDto.getAgencyCd(), initDto.getAffiliation1Cd(), initDto.getAffiliation2Cd(),
					initDto.getBusinessAreaCd());

			// 画面表示するドロップダウンリストを取得
			companyList.addAll((List<Map<String, Object>>) returnMap.get(Skf3090Sc005SharedService.KEY_COMPANY_LIST));
			agencyList.addAll((List<Map<String, Object>>) returnMap.get(Skf3090Sc005SharedService.KEY_AGENCY_LIST));
			affiliation1List
					.addAll((List<Map<String, Object>>) returnMap.get(Skf3090Sc005SharedService.KEY_AFFILIATION1_LIST));
			affiliation2List
					.addAll((List<Map<String, Object>>) returnMap.get(Skf3090Sc005SharedService.KEY_AFFILIATION2_LIST));
			businessAreaList.addAll(
					(List<Map<String, Object>>) returnMap.get(Skf3090Sc005SharedService.KEY_BUSINESS_AREA_LIST));

			// 社員番号テキストボックス操作可能
			shainNoDisabled = "false";
			// 削除ボタン表示設定（非表示）
			deleteRemoveFlag = "true";
			// テキスト系の文言を初期化する
			initDto.setShainNo(null);
			initDto.setName(null);
			initDto.setNameKk(null);
			initDto.setMailAddress(null);
			initDto.setRetireDate(null);

		} else {
			/** リストテーブルから遷移 */
			// コントロール系の値を取得
			Map<String, Object> returnMap = this.getControlValues(CodeConstant.C001, initDto.getHdnSelectedShainNo());

			if (returnMap == null) {
				// Mapがnullの場合、0件エラー表示
				ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1077);
			} else {
				// 画面表示するドロップダウンリストを取得
				companyList
						.addAll((List<Map<String, Object>>) returnMap.get(Skf3090Sc005SharedService.KEY_COMPANY_LIST));
				agencyList.addAll((List<Map<String, Object>>) returnMap.get(Skf3090Sc005SharedService.KEY_AGENCY_LIST));
				affiliation1List.addAll(
						(List<Map<String, Object>>) returnMap.get(Skf3090Sc005SharedService.KEY_AFFILIATION1_LIST));
				affiliation2List.addAll(
						(List<Map<String, Object>>) returnMap.get(Skf3090Sc005SharedService.KEY_AFFILIATION2_LIST));
				businessAreaList.addAll(
						(List<Map<String, Object>>) returnMap.get(Skf3090Sc005SharedService.KEY_BUSINESS_AREA_LIST));

				// 社員番号テキストボックス操作不可
				shainNoDisabled = "true";
				// 削除ボタン表示設定
				if (NfwStringUtils.isNotEmpty((String) returnMap.get(KEY_REGIST_FLG))
						&& REGIST_FLAG_SHATAKU.equals(returnMap.get(KEY_REGIST_FLG))) {
					// 社宅管理で登録されたデータの場合削除ボタンを表示
					deleteRemoveFlag = "false";
				} else {
					// 社宅管理で登録されたデータではない場合削除ボタンを非表示
					deleteRemoveFlag = "true";
				}

				// リストテーブルから遷移時限定の戻り値セット
				initDto.setShainNo(initDto.getHdnSelectedShainNo());
				initDto.setName((String) returnMap.get(KEY_NAME));
				initDto.setNameKk((String) returnMap.get(KEY_NAME_KK));
				initDto.setMailAddress((String) returnMap.get(KEY_MAIL_ADDRESS));
				initDto.setRetireDate((String) returnMap.get(KEY_RETIRE_DATE));
				initDto.setRoleId((String) returnMap.get(KEY_ROLE_ID));
				initDto.setRegistFlg((String) returnMap.get(KEY_REGIST_FLG));
				initDto.addLastUpdateDate(Skf3090Sc005SharedService.KEY_LAST_UPDATE_DATE,
						(Date) returnMap.get(KEY_UPDATE_DATE));

			}

		}

		// Dtoに戻り値をセット
		initDto.setShainNoDisabled(shainNoDisabled);
		initDto.setDeleteRemoveFlag(deleteRemoveFlag);

		initDto.setCompanyList(companyList);
		initDto.setAgencyList(agencyList);
		initDto.setAffiliation1List(affiliation1List);
		initDto.setAffiliation2List(affiliation2List);
		initDto.setBusinessAreaList(businessAreaList);

		initDto.setPageTitleKey(MessageIdConstant.SKF3090_SC005_TITLE);

		return initDto;
	}

	/**
	 * 画面に表示するコンポーネントの値を取得する。
	 * 
	 * @param companyCd 会社コード
	 * @param shainNo 社員番号
	 * @return 以下を含むMap<br>
	 *         会社コード、社員番号、氏名、氏名カナ、メールアドレス、退職日、原籍会社コード、ロールID、登録フラグ、更新日付（タイムスタンプ）、<br>
	 *         原籍会社ドロップダウンリスト（中日本、高速道路総合研究所限定）、機関ドロップダウンリスト、部等ドロップダウンリスト、室、チーム又は課ドロップダウンリスト、事業領域ドロップダウンリスト
	 */
	private Map<String, Object> getControlValues(String companyCd, String shainNo) {

		// 戻り値用Mapのインスタンス生成
		Map<String, Object> returnMap = new HashMap<String, Object>();

		// 社員情報取得
		Skf1010MShain resultValue = skf3090Sc005SharedService.getShainByPrimaryKey(companyCd, shainNo);

		// 社員情報を取得できなかった場合はNULLをreturn
		if (resultValue == null) {
			return null;
		}

		/** SQLの結果をセット */
		// 会社コード
		returnMap.put(KEY_COMPANY_CD, resultValue.getCompanyCd());
		// 社員番号
		returnMap.put(KEY_SHAIN_NO, resultValue.getShainNo());
		// 氏名
		returnMap.put(KEY_NAME, resultValue.getName());
		// 氏名カナ
		returnMap.put(KEY_NAME_KK, resultValue.getNameKk());
		// メールアドレス
		returnMap.put(KEY_MAIL_ADDRESS, resultValue.getMailAddress());
		// 退職日
		String retireDate = resultValue.getRetireDate();
		if (NfwStringUtils.isNotEmpty(retireDate)) {
			retireDate = skfDateFormatUtils.dateFormatFromString(retireDate, SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
		}
		returnMap.put(KEY_RETIRE_DATE, retireDate);
		// 原籍会社コード
		returnMap.put(KEY_ORIGINAL_COMPANY_CD, resultValue.getOriginalCompanyCd());
		// ロールID
		returnMap.put(KEY_ROLE_ID, resultValue.getRoleId());
		// 登録フラグ
		returnMap.put(KEY_REGIST_FLG, resultValue.getRegistFlg());
		// タイムスタンプ
		returnMap.put(KEY_UPDATE_DATE, resultValue.getUpdateDate());

		/** ドロップダウンリストの取得 */
		returnMap.putAll(skf3090Sc005SharedService.getDropDownLists(resultValue.getOriginalCompanyCd(),
				resultValue.getAgencyCd(), resultValue.getAffiliation1Cd(), resultValue.getAffiliation2Cd(),
				resultValue.getBusinessAreaCd()));

		return returnMap;

	}

}
