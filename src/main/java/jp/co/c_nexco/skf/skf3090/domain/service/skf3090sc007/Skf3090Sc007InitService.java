/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc007;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MSoshiki;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc007.Skf3090Sc007InitDto;
import jp.co.c_nexco.skf.skf3090.domain.service.common.Skf309040CommonSharedService;

/**
 * Skf3090Sc007InitService 組織マスタ登録初期表示処理クラス。
 * 
 */
@Service
public class Skf3090Sc007InitService extends BaseServiceAbstract<Skf3090Sc007InitDto> {

	// 戻り値用Map用定数
	private static final String KEY_COMPANY_CD = "COMPANY_CODE";
	private static final String KEY_AGENCY_CD = "AGENCY_CODE";
	private static final String KEY_AFFILIATION1_CD = "AFFILIATION1_CODE";
	private static final String KEY_AFFILIATION2_CD = "AFFILIATION2_CODE";
	private static final String KEY_AGENCY_NAME = "AGENCY_NAME";
	private static final String KEY_AFFILIATION1_NAME = "AFFILIaTION1_NAME";
	private static final String KEY_AFFILIATION2_NAME = "AFFILIATION2_NAME";
	private static final String KEY_REGIST_FLAG = "REGIST_FLAG";

	// 社宅管理登録フラグ
	private static final String REGIST_FLAG_SHATAKU = "1";

	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	@Autowired
	private Skf3090Sc007SharedService skf3090Sc007SharedService;

	@SuppressWarnings("unchecked")
	@Override
	public Skf3090Sc007InitDto index(Skf3090Sc007InitDto initDto) throws Exception {

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, initDto.getPageId());

		initDto.setPageTitleKey(MessageIdConstant.SKF3090_SC007_TITLE);

		// エラー系DTO値を初期化
		initDto.setAgencyCdError(CodeConstant.DOUBLE_QUOTATION);
		initDto.setAffiliation1CdError(CodeConstant.DOUBLE_QUOTATION);
		initDto.setAffiliation2CdError(CodeConstant.DOUBLE_QUOTATION);
		initDto.setAgencyNameError(CodeConstant.DOUBLE_QUOTATION);
		initDto.setAffiliation1NameError(CodeConstant.DOUBLE_QUOTATION);
		initDto.setAffiliation2NameError(CodeConstant.DOUBLE_QUOTATION);

		// 会社ドロップダウンのインスタンスを生成
		List<Map<String, Object>> companyList = new ArrayList<Map<String, Object>>();

		// 事業領域ドロップダウンのインスタンスを生成
		List<Map<String, Object>> businessAreaList = new ArrayList<Map<String, Object>>();

		// 会社ドロップダウン操作可否判定
		String comapnyCdDisabled = CodeConstant.DOUBLE_QUOTATION;
		// 機関コードテキストボックス操作可否判定
		String agencyCdDisabled = CodeConstant.DOUBLE_QUOTATION;
		// 機関コード名称を検索ボタン操作可否判定
		String agencyCdSearchDisabled = CodeConstant.DOUBLE_QUOTATION;
		// 部等コードテキストボックス操作可否判定
		String affiliation1CdDisabled = CodeConstant.DOUBLE_QUOTATION;
		// 部等コード名称を検索ボタン操作可否判定
		String affiliation1CdSearchDisabled = CodeConstant.DOUBLE_QUOTATION;
		// 室、チーム又は課コードテキストボックス操作可否判定
		String affiliation2CdDisabled = CodeConstant.DOUBLE_QUOTATION;
		// 機関テキストボックス操作可否判定
		String agencyNameDisabled = CodeConstant.DOUBLE_QUOTATION;
		// 部等テキストボックス操作可否判定
		String affiliation1NameDisabled = CodeConstant.DOUBLE_QUOTATION;
		// 室、チーム又は課テキストボックス操作可否判定
		String affiliation2NameDisabled = CodeConstant.DOUBLE_QUOTATION;
		// 事業領域ドロップダウン操作可否判定
		String businessAreaCdDisabled = CodeConstant.DOUBLE_QUOTATION;
		// 登録ボタン操作可否判定
		String registButtonDisabled = CodeConstant.DOUBLE_QUOTATION;
		// 登録ボタン表示可否判定
		String registButtonRemove = CodeConstant.DOUBLE_QUOTATION;
		// 削除ボタン操作可否判定
		String deleteButtonDisabled = CodeConstant.DOUBLE_QUOTATION;

		if (Skf309040CommonSharedService.UPDATE_FLAG_NEW.equals(initDto.getUpdateFlag())) {
			/** 新規ボタンから遷移 */
			// ドロップダウンリストを取得
			Map<String, Object> returnMap = skf3090Sc007SharedService.getDropDownLists(initDto.getRegistCompanyCd(),
					initDto.getRegistAgencyCd(), initDto.getRegistBusinessAreaCd());

			/** 画面表示するドロップダウンを取得する */
			// 会社ドロップダウン
			companyList.addAll((List<Map<String, Object>>) returnMap.get(Skf3090Sc007SharedService.KEY_COMPANY_LIST));

			// 会社ドロップダウンリスト操作可能
			comapnyCdDisabled = "false";
			// 機関コードテキストボックス操作可能
			agencyCdDisabled = "false";
			// 機関コード名称を検索ボタン操作可能
			agencyCdSearchDisabled = "false";
			// 部等コードテキストボックス操作可能
			affiliation1CdDisabled = "false";
			// 部等コード名称を検索ボタン操作可能
			affiliation1CdSearchDisabled = "false";
			// 室、チーム又は課コードテキストボックス操作可能
			affiliation2CdDisabled = "false";
			// 機関テキストボックス操作可能
			agencyNameDisabled = "false";
			// 部等テキストボックス操作可能
			affiliation1NameDisabled = "false";
			// 室、チーム又は課テキストボックス操作可能
			affiliation2NameDisabled = "false";
			// 事業領域ドロップダウンリスト操作可能
			businessAreaCdDisabled = "false";
			// 登録ボタン操作可能
			registButtonDisabled = "false";
			// 削除ボタン操作不可
			deleteButtonDisabled = "true";

			// テキストボックスの文言を初期化する
			initDto.setRegistAgencyCd(null);
			initDto.setRegistAffiliation1Cd(null);
			initDto.setRegistAffiliation2Cd(null);
			initDto.setRegistAgencyName(null);
			initDto.setRegistAffiliation1Name(null);

		} else {
			/** リストテーブルから遷移 */
			// コントロールの値を取得
			Map<String, Object> returnMap = this.getControlValues(initDto.getHdnCompanyCd(), initDto.getHdnAgencyCd(),
					initDto.getHdnAffiliation1Cd(), initDto.getHdnAffiliation2Cd());

			if (returnMap == null) {
				// Mapがnullの場合、0件エラー表示
				ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1077);
			} else {
				/** 画面表示するドロップダウンを取得 */
				// 会社ドロップダウン
				companyList
						.addAll((List<Map<String, Object>>) returnMap.get(Skf3090Sc007SharedService.KEY_COMPANY_LIST));
				// 事業領域ドロップダウン
				businessAreaList.addAll(
						(List<Map<String, Object>>) returnMap.get(Skf3090Sc007SharedService.KEY_BUSINESS_AREA_LIST));

				// 会社ドロップダウン操作不可
				comapnyCdDisabled = "true";
				// 機関コードテキストボックス操作不可
				agencyCdDisabled = "true";
				// 機関コード名称を検索ボタン操作不可
				agencyCdSearchDisabled = "true";
				// 部等コードテキストボックス操作不可
				affiliation1CdDisabled = "true";
				// 部等コード名称を検索ボタン操作不可
				affiliation1CdSearchDisabled = "true";
				// 室、チーム又は課コードテキストボックス操作不可
				affiliation2CdDisabled = "true";
				// 機関テキストボックス操作不可
				agencyNameDisabled = "true";
				// 部等テキストボックス操作不可
				affiliation1NameDisabled = "true";
				// 室、チーム又は課テキストボックス操作不可
				affiliation2NameDisabled = "true";
				// 事業領域ドロップダウン操作不可
				businessAreaCdDisabled = "true";
				// 登録ボタン表示可否判定
				registButtonRemove = "true";

				// 削除ボタン表示設定
				if (NfwStringUtils.isNotEmpty((String) returnMap.get(KEY_REGIST_FLAG))
						&& REGIST_FLAG_SHATAKU.equals(returnMap.get(KEY_REGIST_FLAG))) {
					// 社宅管理で登録されたデータの場合削除ボタンを活性
					deleteButtonDisabled = "flase";
				} else {
					// 社宅管理で登録されたデータではない場合削除ボタンを非活性
					deleteButtonDisabled = "true";
				}

				initDto.setRegistAgencyCd((String) returnMap.get(KEY_AGENCY_CD));
				initDto.setRegistAffiliation1Cd((String) returnMap.get(KEY_AFFILIATION1_CD));
				initDto.setRegistAffiliation2Cd((String) returnMap.get(KEY_AFFILIATION2_CD));
				initDto.setRegistAgencyName((String) returnMap.get(KEY_AGENCY_NAME));
				initDto.setRegistAffiliation1Name((String) returnMap.get(KEY_AFFILIATION1_NAME));
				initDto.setRegistAffiliation2Name((String) returnMap.get(KEY_AFFILIATION2_NAME));

			}

		}
		// Dtoに戻り値をセット
		initDto.setCompanyCdDisabled(comapnyCdDisabled);
		initDto.setAgencyCdDisabled(agencyCdDisabled);
		initDto.setAgencyCdSearchDisabled(agencyCdSearchDisabled);
		initDto.setAffiliation1CdDisabled(affiliation1CdDisabled);
		initDto.setAffiliation1CdSearchDisabled(affiliation1CdSearchDisabled);
		initDto.setAffiliation2CdDisabled(affiliation2CdDisabled);
		initDto.setAgencyNameDisabled(agencyNameDisabled);
		initDto.setAffiliation1NameDisabled(affiliation1NameDisabled);
		initDto.setAffiliation2NameDisabled(affiliation2NameDisabled);
		initDto.setBusinessAreaCdDisabled(businessAreaCdDisabled);
		initDto.setRegistButtonDisabled(registButtonDisabled);
		initDto.setDeleteButtonDisabled(deleteButtonDisabled);
		initDto.setRegistButtonRemove(registButtonRemove);

		initDto.setCompanyList(companyList);
		initDto.setBusinessAreaList(businessAreaList);

		return initDto;
	}

	/**
	 * 画面に表示するコンポーネントの値を取得する
	 */
	private Map<String, Object> getControlValues(String companyCd, String agencyCd, String affiliation1Cd,
			String affiliation2Cd) {

		// 戻り値用Mapのインスタンスを生成
		Map<String, Object> returnMap = new HashMap<String, Object>();

		// 組織情報を取得
		Skf1010MSoshiki resultValue = skf3090Sc007SharedService.getSoshikiByPrimaryKey(companyCd, agencyCd,
				affiliation1Cd, affiliation2Cd);

		// 組織情報を取得できなかった場合はnullをreturn
		if (resultValue == null) {
			return null;
		}

		/** SQLの結果をセット */
		// 会社コード
		returnMap.put(KEY_COMPANY_CD, resultValue.getCompanyCd());
		// 機関コード
		returnMap.put(KEY_AGENCY_CD, resultValue.getAgencyCd());
		// 部等コード
		returnMap.put(KEY_AFFILIATION1_CD, resultValue.getAffiliation1Cd());
		// 室、チーム又は課コード
		returnMap.put(KEY_AFFILIATION2_CD, resultValue.getAffiliation2Cd());
		// 機関
		returnMap.put(KEY_AGENCY_NAME, resultValue.getAgencyName());
		// 部等
		returnMap.put(KEY_AFFILIATION1_NAME, resultValue.getAffiliation1Name());
		// 室、チーム又は課
		returnMap.put(KEY_AFFILIATION2_NAME, resultValue.getAffiliation2Name());
		// 登録フラグ
		returnMap.put(KEY_REGIST_FLAG, resultValue.getRegistFlg());

		// /** ドロップダウンリストの取得 */
		returnMap.putAll(skf3090Sc007SharedService.getDropDownLists(resultValue.getCompanyCd(),
				resultValue.getAgencyCd(), resultValue.getBusinessAreaCd()));
		return returnMap;

	}

}
