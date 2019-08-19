/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc007;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
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
	private static final String KEY_AFFIILIATION1_CD = "AFFILIATION1_CODE";
	private static final String KEY_AFFILIATION2_CD = "AFFILIATION2_CODE";
	private static final String KEY_AGENCY_NAME = "AGENCY_NAME";
	private static final String KEY_AFFILIATION1_NAME = "AFFILIaTION1_NAME";
	private static final String KEY_AFFILIATION2_NAME = "AFFILIATION2_NAME";
	private static final String KEY_BUSINESS_AREA_CD = "BUSINESS_AREA_CODE";

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
		// 部等コードテキストボックス操作可否判定
		String affiliation1CdDisabled = CodeConstant.DOUBLE_QUOTATION;
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

		if (Skf309040CommonSharedService.UPDATE_FLAG_NEW.equals(initDto.getUpdateFlag())) {
			/** 新規ボタンから遷移 */
			// ドロップダウンを選択
			Map<String, Object> returnMap = skf3090Sc007SharedService.getDropDownLists(initDto.getCompanyCd(),
					initDto.getAgencyCd(), initDto.getBusinessAreaCd());

			// 画面表示するドロップダウンリストを取得
			companyList.addAll((List<Map<String, Object>>) returnMap.get(Skf3090Sc007SharedService.KEY_COMPANY_LIST));
			businessAreaList.addAll(
					(List<Map<String, Object>>) returnMap.get(Skf3090Sc007SharedService.KEY_BUSINESS_AREA_LIST));

			// 会社ドロップダウンリスト操作可能
			comapnyCdDisabled = "true";
			// 機関コードテキストボックス操作可能
			agencyCdDisabled = "true";
			// 部等コードテキストボックス操作可能
			affiliation1CdDisabled = "ture";
			// 室、チーム又は課コードテキストボックス操作可能
			affiliation2CdDisabled = "true";
			// 機関テキストボックス操作可能
			agencyNameDisabled = "true";
			// 部等テキストボックス操作可能
			affiliation1NameDisabled = "true";
			// 室、チーム又は課テキストボックス操作可能
			affiliation2NameDisabled = "true";
			// 事業領域ドロップダウンリスト操作可能
			businessAreaCdDisabled = "true";

			// テキストボックスの文言を初期化する
			initDto.setAgencyCd(null);
			initDto.setAffiliation1Cd(null);
			initDto.setAffiliation2Cd(null);
			initDto.setAgencyName(null);
			initDto.setAffiliation1Name(null);

		} else {

		}

		return initDto;
	}

}
