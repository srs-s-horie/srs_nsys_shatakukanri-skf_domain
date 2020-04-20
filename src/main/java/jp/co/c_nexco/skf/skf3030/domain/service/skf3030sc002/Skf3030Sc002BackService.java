/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3030.domain.service.skf3030sc002;

import java.util.HashMap;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import jp.co.c_nexco.nfw.common.bean.ApplicationScopeBean;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.skf3030.domain.dto.skf3030sc002.Skf3030Sc002BackDto;
import jp.co.c_nexco.skf.skf3030.domain.service.common.Skf303010CommonSharedService;

/**
 * Skf3030Sc002InitService 入退居情報登録画面のInitサービス処理クラス。　 
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf3030Sc002BackService extends SkfServiceAbstract<Skf3030Sc002BackDto> {
	
	@Autowired
	private ApplicationScopeBean bean;
	
	/**
	 * サービス処理を行う。　
	 * 
	 * @param backDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public Skf3030Sc002BackDto index(Skf3030Sc002BackDto backDto) throws Exception {
		
		// デバッグログ
		LogUtils.debugByMsg("前の画面へ");
		//社宅管理台帳画面へ遷移
		TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF3030_SC001);
		backDto.setTransferPageInfo(nextPage);
		
		//セッション情報設定
		Map<String, Object> searchInfoSessionMap = new HashMap<String, Object>();

		searchInfoSessionMap.put(Skf303010CommonSharedService.NYUTAIKYO_INFO_KANRI_KAISHA_KEY, backDto.getSerachKanriKaisha());
		searchInfoSessionMap.put(Skf303010CommonSharedService.NYUTAIKYO_INFO_AGENCY_KEY, backDto.getSerachAgency());
		searchInfoSessionMap.put(Skf303010CommonSharedService.NYUTAIKYO_INFO_SHAIN_NO_KEY, backDto.getSerachShainNo());
		searchInfoSessionMap.put(Skf303010CommonSharedService.NYUTAIKYO_INFO_SHAIN_NAME_KEY, backDto.getSerachShainName());
		searchInfoSessionMap.put(Skf303010CommonSharedService.NYUTAIKYO_INFO_SHATAK_NAME_KEY, backDto.getSerachShatakName());
		searchInfoSessionMap.put(Skf303010CommonSharedService.NYUTAIKYO_INFO_SHATAK_KBN_KEY, backDto.getSerachShatakKbn());
		searchInfoSessionMap.put(Skf303010CommonSharedService.NYUTAIKYO_INFO_SOGORIYO_KEY, backDto.getSerachSogoriyo());
		searchInfoSessionMap.put(Skf303010CommonSharedService.NYUTAIKYO_INFO_NENGETSU_KEY, backDto.getSerachNengetsu());
		searchInfoSessionMap.put(Skf303010CommonSharedService.NYUTAIKYO_INFO_SHIME_SHORI_KEY, backDto.getSerachShimeShori());
		searchInfoSessionMap.put(Skf303010CommonSharedService.NYUTAIKYO_INFO_POSITIVE_RENKEI_KEY, backDto.getSerachPositiveRenkei());
		searchInfoSessionMap.put(Skf303010CommonSharedService.NYUTAIKYO_INFO_KAISHAKAN_SOKIN_KEY, backDto.getSerachKaishakanSokin());
		searchInfoSessionMap.put(Skf303010CommonSharedService.NYUTAIKYO_INFO_GENSEKI_KAISHA_KEY, backDto.getSerachGensekiKaisha());
		searchInfoSessionMap.put(Skf303010CommonSharedService.NYUTAIKYO_INFO_SIKYU_KAISHA_KEY, backDto.getSerachKyuyoSikyuKaisha());
		searchInfoSessionMap.put(Skf303010CommonSharedService.NYUTAIKYO_INFO_KYOJUSHA_KBN_KEY, backDto.getSerachKyojushaKbn());
		searchInfoSessionMap.put(Skf303010CommonSharedService.NYUTAIKYO_INFO_AKI_HEYA_KEY, backDto.getSerachAkiHeya());
		searchInfoSessionMap.put(Skf303010CommonSharedService.NYUTAIKYO_INFO_PARKING_KEY, backDto.getSerachParkingSiyoryo());
		searchInfoSessionMap.put(Skf303010CommonSharedService.NYUTAIKYO_INFO_HONRAI_YOTO_KEY, backDto.getSerachHonraiYoto());
		searchInfoSessionMap.put(Skf303010CommonSharedService.NYUTAIKYO_INFO_HONRAI_KIKAKU_KEY, backDto.getSerachHonraiKikaku());
		searchInfoSessionMap.put(Skf303010CommonSharedService.NYUTAIKYO_INFO_YAKUIN_KEY, backDto.getSerachYakuin());
		searchInfoSessionMap.put(Skf303010CommonSharedService.NYUTAIKYO_INFO_SHUKKOSHA_KEY, backDto.getSerachShukkosha());
		searchInfoSessionMap.put(Skf303010CommonSharedService.NYUTAIKYO_INFO_BIKO_KEY, backDto.getSerachBiko());
		bean.put(SessionCacheKeyConstant.SHATAKUKANRI_DAICHO_SEARCH, searchInfoSessionMap);
		
		return backDto;
	}
	

}
