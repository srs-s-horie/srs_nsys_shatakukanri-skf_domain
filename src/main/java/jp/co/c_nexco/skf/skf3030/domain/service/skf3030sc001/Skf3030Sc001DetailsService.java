/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3030.domain.service.skf3030sc001;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc001.Skf3030Sc001GetShatakuKanriDaichoInfoExpParameter;
import jp.co.c_nexco.nfw.common.bean.ApplicationScopeBean;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3030.domain.dto.skf3030sc001.Skf3030Sc001DetailsDto;
import jp.co.c_nexco.skf.skf3030.domain.service.common.Skf303010CommonSharedService;

/**
 * Skf3030Sc001DetailsService 社宅管理台帳詳細処理Service
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf3030Sc001DetailsService extends SkfServiceAbstract<Skf3030Sc001DetailsDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private ApplicationScopeBean bean;

	@Override
	protected BaseDto index(Skf3030Sc001DetailsDto inDto) throws Exception {

		skfOperationLogUtils.setAccessLog("社宅管理台帳「詳細」処理開始", CodeConstant.C001, FunctionIdConstant.SKF3030_SC001);

		createSessionData(inDto);

		TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF3030_SC002, "init");
		inDto.setTransferPageInfo(nextPage);

		return inDto;
	}

	/**
	 * 「入退去情報登録」画面へ渡すセッションデータを作成する。
	 * 
	 * @param inDto
	 *            Skf3030Sc001DetailsDto
	 * @return セッションデータ
	 */
	private void createSessionData(Skf3030Sc001DetailsDto inDto) {
		
		Map<String, Object> listSelDataSessionMap = new HashMap<String, Object>();
	
		int selIdx = Integer.parseInt(inDto.getHdnSelIdx());
		Map<String, Object> searchData = inDto.getSearchDataList().get(selIdx - 1);

		listSelDataSessionMap.put(Skf303010CommonSharedService.SEL_LIST_INFO_SHATAK_NAME_KEY, searchData.get(Skf303010CommonSharedService.SEARCH_COL_SHATAK_NAME_KEY));
		listSelDataSessionMap.put(Skf303010CommonSharedService.SEL_LIST_INFO_SHATAK_KANRI_NO_KEY, searchData.get(Skf303010CommonSharedService.SEARCH_COL_SHATAK_KANRI_NO_KEY));
		listSelDataSessionMap.put(Skf303010CommonSharedService.SEL_LIST_INFO_ROOM_NO_KEY, searchData.get(Skf303010CommonSharedService.SEARCH_COL_ROOM_NO_KEY));
		listSelDataSessionMap.put(Skf303010CommonSharedService.SEL_LIST_INFO_ROOM_KANRI_NO_KEY, searchData.get(Skf303010CommonSharedService.SEARCH_COL_ROOM_KANRI_NO_KEY));
		listSelDataSessionMap.put(Skf303010CommonSharedService.SEL_LIST_INFO_DAICHO_KANRI_ID_KEY, searchData.get(Skf303010CommonSharedService.SEARCH_COL_DAICHO_KANRI_ID_KEY));
		listSelDataSessionMap.put(Skf303010CommonSharedService.SEL_LIST_INFO_KANRI_KAISHA_NAME_KEY, searchData.get(Skf303010CommonSharedService.SEARCH_COL_KANRI_COMPANY_KEY));
		listSelDataSessionMap.put(Skf303010CommonSharedService.SEL_LIST_INFO_KANRI_CD_KEY, searchData.get(Skf303010CommonSharedService.SEARCH_COL_KANRI_CD_KEY));
		listSelDataSessionMap.put(Skf303010CommonSharedService.SEL_LIST_INFO_NENGETSU_KEY, inDto.getHdnYearSelect() + inDto.getHdnMonthSelect());
		bean.put(SessionCacheKeyConstant.SHATAKUKANRI_DAICHO_INFO, listSelDataSessionMap);
		
		Map<String, Object> searchInfoSessionMap = new HashMap<String, Object>();
		Skf3030Sc001GetShatakuKanriDaichoInfoExpParameter searchParam = inDto.getSearchParam();

		searchInfoSessionMap.put(Skf303010CommonSharedService.SEARCH_INFO_KANRI_KAISHA_KEY, searchParam.getKanriKaisha());
		searchInfoSessionMap.put(Skf303010CommonSharedService.SEARCH_INFO_AGENCY_KEY, searchParam.getKanriKikan());
		searchInfoSessionMap.put(Skf303010CommonSharedService.SEARCH_INFO_SHAIN_NO_KEY, searchParam.getShainNo());
		searchInfoSessionMap.put(Skf303010CommonSharedService.SEARCH_INFO_SHAIN_NAME_KEY, searchParam.getShainName());
		searchInfoSessionMap.put(Skf303010CommonSharedService.SEARCH_INFO_SHATAK_NAME_KEY, searchParam.getShatakuName());
		searchInfoSessionMap.put(Skf303010CommonSharedService.SEARCH_INFO_SHATAK_KBN_KEY, searchParam.getShatakuKbn());
		searchInfoSessionMap.put(Skf303010CommonSharedService.SEARCH_INFO_SOGORIYO_KEY, searchParam.getSogoriyo());
		searchInfoSessionMap.put(Skf303010CommonSharedService.SEARCH_INFO_NENGETSU_KEY, searchParam.getNengetsu());
		searchInfoSessionMap.put(Skf303010CommonSharedService.SEARCH_INFO_SHIME_SHORI_KEY, searchParam.getShimeShori());
		searchInfoSessionMap.put(Skf303010CommonSharedService.SEARCH_INFO_POSITIVE_RENKEI_KEY, searchParam.getPositiveRenkei());
		searchInfoSessionMap.put(Skf303010CommonSharedService.SEARCH_INFO_KAISHAKAN_SOKIN_KEY, searchParam.getKaishakanSokin());
		searchInfoSessionMap.put(Skf303010CommonSharedService.SEARCH_INFO_GENSEKI_KAISHA_KEY, searchParam.getGensekiKaisha());
		searchInfoSessionMap.put(Skf303010CommonSharedService.SEARCH_INFO_SIKYU_KAISHA_KEY, searchParam.getKyuyoShikyuKaisha());
		searchInfoSessionMap.put(Skf303010CommonSharedService.SEARCH_INFO_KYOJUSHA_KBN_KEY, searchParam.getKyojushaKbn());
		searchInfoSessionMap.put(Skf303010CommonSharedService.SEARCH_INFO_AKI_HEYA_KEY, searchParam.getAkiHeya());
		searchInfoSessionMap.put(Skf303010CommonSharedService.SEARCH_INFO_PARKING_KEY, searchParam.getAkiChushajo());
		searchInfoSessionMap.put(Skf303010CommonSharedService.SEARCH_INFO_HONRAI_YOTO_KEY, searchParam.getHonraiYoto());
		searchInfoSessionMap.put(Skf303010CommonSharedService.SEARCH_INFO_HONRAI_KIKAKU_KEY, searchParam.getHonraiKikaku());
		searchInfoSessionMap.put(Skf303010CommonSharedService.SEARCH_INFO_YAKUIN_KEY, searchParam.getYakuin());
		searchInfoSessionMap.put(Skf303010CommonSharedService.SEARCH_INFO_SHUKKOSHA_KEY, searchParam.getShukkosha());
		searchInfoSessionMap.put(Skf303010CommonSharedService.SEARCH_INFO_BIKO_KEY, searchParam.getBiko());
		bean.put(SessionCacheKeyConstant.SHATAKUKANRI_DAICHO_SEARCH, searchInfoSessionMap);
		
	}

}
