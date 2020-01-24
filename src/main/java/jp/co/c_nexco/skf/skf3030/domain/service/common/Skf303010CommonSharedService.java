/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3030.domain.service.common;

import org.springframework.stereotype.Service;

/**
 * Skf303010CommonSharedService Skf3030共通処理Service
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf303010CommonSharedService {

	public static final String SEARCH_COL_DETAIL_KEY = "col1";
	public static final String SEARCH_COL_KANRI_COMPANY_KEY = "col2";
	public static final String SEARCH_COL_AGENCY_KEY = "col3";
	public static final String SEARCH_COL_SHATAK_KBN_KEY = "col4";
	public static final String SEARCH_COL_SHATAK_NAME_KEY = "col5";
	public static final String SEARCH_COL_ROOM_NO_KEY = "col6";
	public static final String SEARCH_COL_SHAIN_NO_KEY = "col7";
	public static final String SEARCH_COL_SHAIN_NAME_KEY = "col8";
	public static final String SEARCH_COL_SHATAK_TEIJI_KEY = "col9";
	public static final String SEARCH_COL_BIHIN_TEIJI_KEY = "col10";
	public static final String SEARCH_COL_SIYORYO_KEY = "col11";
	public static final String SEARCH_COL_KYOEKIHI_KEY = "col12";
	public static final String SEARCH_COL_PARKING_KEY = "col13";
	public static final String SEARCH_COL_NYUKYO_DATE_KEY = "col14";
	public static final String SEARCH_COL_TAIKYO_DATE_KEY = "col15";
	public static final String SEARCH_COL_KANRI_JIGYO_KEY = "col16";
	public static final String SEARCH_COL_SOGORIYO_KEY = "col17";
	public static final String SEARCH_COL_SHATAK_KANRI_NO_KEY = "col18";
	public static final String SEARCH_COL_ROOM_KANRI_NO_KEY = "col19";
	public static final String SEARCH_COL_DAICHO_KANRI_ID_KEY = "col20";
	public static final String SEARCH_COL_KANRI_CD_KEY = "col21";
	
	/** 「入退去情報登録」画面へ渡すセッション情報mapのkey */
	public static final String SEARCH_INFO_KANRI_KAISHA_KEY = "skf3030sc001KanriKaishaKey";
	public static final String SEARCH_INFO_AGENCY_KEY = "skf3030sc001AgencyKey";
	public static final String SEARCH_INFO_SHAIN_NO_KEY = "skf3030sc001ShainNoKey";
	public static final String SEARCH_INFO_SHAIN_NAME_KEY = "skf3030sc001ShainNameKey";
	public static final String SEARCH_INFO_SHATAK_NAME_KEY = "skf3030sc001ShatakNameKey";
	public static final String SEARCH_INFO_SHATAK_KBN_KEY = "skf3030sc001ShatakuKbnKey";
	public static final String SEARCH_INFO_SOGORIYO_KEY = "skf3030sc001SogoriyoKey";
	public static final String SEARCH_INFO_NENGETSU_KEY = "skf3030sc001NengetsuKey";
	public static final String SEARCH_INFO_SHIME_SHORI_KEY = "skf3030sc001ShimeShoriKey";
	public static final String SEARCH_INFO_POSITIVE_RENKEI_KEY = "skf3030sc001PositiveRenkeiKey";
	public static final String SEARCH_INFO_KAISHAKAN_SOKIN_KEY = "skf3030sc001KaishakanSokinKey";
	public static final String SEARCH_INFO_GENSEKI_KAISHA_KEY = "skf3030sc001GensekiKaishaKey";
	public static final String SEARCH_INFO_SIKYU_KAISHA_KEY = "skf3030sc001SikyuKaishaKey";
	public static final String SEARCH_INFO_KYOJUSHA_KBN_KEY = "skf3030sc001KyojushaKbnKey";
	public static final String SEARCH_INFO_AKI_HEYA_KEY = "skf3030sc001AkiHeyaKey";
	public static final String SEARCH_INFO_PARKING_KEY = "skf3030sc001ParkingKey";
	public static final String SEARCH_INFO_HONRAI_YOTO_KEY = "skf3030sc001HonraiYotoKey";
	public static final String SEARCH_INFO_HONRAI_KIKAKU_KEY = "skf3030sc001HonraiKikakuKey";
	public static final String SEARCH_INFO_YAKUIN_KEY = "skf3030sc001YakuinKey";
	public static final String SEARCH_INFO_SHUKKOSHA_KEY = "skf3030sc001ShukkoshaKey";
	public static final String SEARCH_INFO_BIKO_KEY = "skf3030sc001BikoKey";
	public static final String SEL_LIST_INFO_SHATAK_NAME_KEY = "skf3030sc001SelShatakNameKey";
	public static final String SEL_LIST_INFO_SHATAK_KANRI_NO_KEY = "skf3030sc001SelShatakKanriNoKey";
	public static final String SEL_LIST_INFO_ROOM_NO_KEY = "skf3030sc001SelRoomNoKey";
	public static final String SEL_LIST_INFO_ROOM_KANRI_NO_KEY = "skf3030sc001SelRoomKanriNoKey";
	public static final String SEL_LIST_INFO_DAICHO_KANRI_ID_KEY = "skf3030sc001SelDaichoKanriIdKey";
	public static final String SEL_LIST_INFO_KANRI_KAISHA_NAME_KEY = "skf3030sc001SelKanriKaishaNameKey";
	public static final String SEL_LIST_INFO_KANRI_CD_KEY = "skf3030sc001SelKanriCdKey";
	public static final String SEL_LIST_INFO_NENGETSU_KEY = "skf3030sc001SelNengetsuKey";
	
	/** 「社宅管理台帳」画面へ渡すセッション情報mapのkey */
	public static final String NYUTAIKYO_INFO_KANRI_KAISHA_KEY = "skf3030sc002KanriKaishaKey";
	public static final String NYUTAIKYO_INFO_AGENCY_KEY = "skf3030sc002AgencyKey";
	public static final String NYUTAIKYO_INFO_SHAIN_NO_KEY = "skf3030sc002ShainNoKey";
	public static final String NYUTAIKYO_INFO_SHAIN_NAME_KEY = "skf3030sc002ShainNameKey";
	public static final String NYUTAIKYO_INFO_SHATAK_NAME_KEY = "skf3030sc002ShatakNameKey";
	public static final String NYUTAIKYO_INFO_SHATAK_KBN_KEY = "skf3030sc002ShatakuKbnKey";
	public static final String NYUTAIKYO_INFO_SOGORIYO_KEY = "skf3030sc002SogoriyoKey";
	public static final String NYUTAIKYO_INFO_NENGETSU_KEY = "skf3030sc002NengetsuKey";
	public static final String NYUTAIKYO_INFO_SHIME_SHORI_KEY = "skf3030sc002ShimeShoriKey";
	public static final String NYUTAIKYO_INFO_POSITIVE_RENKEI_KEY = "skf3030sc002PositiveRenkeiKey";
	public static final String NYUTAIKYO_INFO_KAISHAKAN_SOKIN_KEY = "skf3030sc002KaishakanSokinKey";
	public static final String NYUTAIKYO_INFO_GENSEKI_KAISHA_KEY = "skf3030sc002GensekiKaishaKey";
	public static final String NYUTAIKYO_INFO_SIKYU_KAISHA_KEY = "skf3030sc002SikyuKaishaKey";
	public static final String NYUTAIKYO_INFO_KYOJUSHA_KBN_KEY = "skf3030sc002KyojushaKbnKey";
	public static final String NYUTAIKYO_INFO_AKI_HEYA_KEY = "skf3030sc002AkiHeyaKey";
	public static final String NYUTAIKYO_INFO_PARKING_KEY = "skf3030sc002ParkingKey";
	public static final String NYUTAIKYO_INFO_HONRAI_YOTO_KEY = "skf3030sc002HonraiYotoKey";
	public static final String NYUTAIKYO_INFO_HONRAI_KIKAKU_KEY = "skf3030sc002HonraiKikakuKey";
	public static final String NYUTAIKYO_INFO_YAKUIN_KEY = "skf3030sc002YakuinKey";
	public static final String NYUTAIKYO_INFO_SHUKKOSHA_KEY = "skf3030sc002ShukkoshaKey";
	public static final String NYUTAIKYO_INFO_BIKO_KEY = "skf3030sc002BikoKey";
	public static final String NYUTAIKYO_INFO_UPD_TOGETSU_KEY = "skf3030sc002UpdTogetsuKey";

}
