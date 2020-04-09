/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3050.domain.service.skf3050sc002;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import edu.emory.mathcs.backport.java.util.Arrays;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetBihinGenbutsuShikyugokeigakuExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetBihinMeisaiExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetBihinMeisaiExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetCompanyAgencyNameExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetHrRenkeiDataSakuseiMaeSyoriDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetShainSoshikiDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetShainSoshikiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetShatakiKanriDaityoDataTodoufukenDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetShatakiKanriDaityoDataTodoufukenDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetShatakuKanriDaityoSogoriyoDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetShatakuKihonJyohoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetShatakuSyainIdoRirekiDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetShatakuSyainIdoRirekiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetTsukibetuSyoryoRirekiDataJoinDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetTsukibetuSyoryoRirekiDataJoinDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetTsukibetuTyusyajyoRirekiDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001GetTsukibetuTyusyajyoRirekiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateBihinGoukeiExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateBihinMeisaiExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateGenbutsuSanteigakuExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateGetsujiShoriKanriDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateShozokuRirekiExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateTsukibetsuShiyoryoAccountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateTsukibetsuSiyoryorirekiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateTsukibetsuSiyoryorirekiGenbutsuSanteiExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt003.Skf3050Bt003GetPositiveGenbutsuSanteiSakuseiSyoriDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt003.Skf3050Bt003GetPositiveGenbutsuSanteiSakuseiSyoriDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt003.Skf3050Bt003GetPositiveRenkeiDataSakuseiBihinDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt003.Skf3050Bt003GetPositiveRenkeiDataSakuseiBihinDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt003.Skf3050Bt003GetPositiveRenkeiDataSakuseiSyoriDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt003.Skf3050Bt003GetPositiveRenkeiDataSakuseiSyoriDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt003.Skf3050Bt003UpdateGetsujiShoriKanriExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Sc002.Skf3050Sc002GetBatchStartupCntExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Sc002.Skf3050Sc002GetBihinHenkyakuDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Sc002.Skf3050Sc002GetBihinTaiyoDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Sc002.Skf3050Sc002GetGetsujiShoriJoukyouShoukaiExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Sc002.Skf3050Sc002GetGetsujiShoriJoukyouShoukaiExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Sc002.Skf3050Sc002GetHenkanShainBangoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Sc002.Skf3050Sc002GetKariShainBangoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Sc002.Skf3050Sc002GetParkingRirekiDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Sc002.Skf3050Sc002GetParkingRirekiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Sc002.Skf3050Sc002GetShatakuKanriParking1RiyoKaishiExistExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Sc002.Skf3050Sc002PositiveRenkeiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBaseBusinessLogicUtils.SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBaseBusinessLogicUtils.SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MCompany;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3050MAccount;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3050TMonthlyManageData;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001GetBihinGenbutsuShikyugokeigakuExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001GetBihinMeisaiExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001GetCompanyAgencyNameExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001GetDataForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001GetHrRenkeiDataSakuseiMaeSyoriDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001GetShainSoshikiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001GetShatakiKanriDaityoDataTodoufukenDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001GetShatakuKanriDaityoSogoriyoDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001GetShatakuKihonJyohoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001GetShatakuSyainIdoRirekiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001GetTsukibetuSyoryoRirekiDataJoinDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001GetTsukibetuTyusyajyoRirekiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateBihinGoukeiExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateBihinMeisaiExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateGenbutsuSanteigakuExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateGetsujiShoriKanriDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateShozokuRirekiExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateTsukibetsuShiyoryoAccountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateTsukibetsuSiyoryorirekiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt001.Skf3050Bt001UpdateTsukibetsuSiyoryorirekiGenbutsuSanteiExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt003.Skf3050Bt003GetGetsujiShoriKanriForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt003.Skf3050Bt003GetPositiveGenbutsuSanteiSakuseiSyoriDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt003.Skf3050Bt003GetPositiveRenkeiDataSakuseiBihinDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt003.Skf3050Bt003GetPositiveRenkeiDataSakuseiSyoriDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt003.Skf3050Bt003UpdateGetsujiShoriKanriExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Sc002.Skf3050Sc002GetBatchStartupCntExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Sc002.Skf3050Sc002GetBihinHenkyakuDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Sc002.Skf3050Sc002GetBihinTaiyoDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Sc002.Skf3050Sc002GetGetsujiShoriJoukyouShoukaiExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Sc002.Skf3050Sc002GetHenkanShainBangoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Sc002.Skf3050Sc002GetKariShainBangoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Sc002.Skf3050Sc002GetParkingRirekiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Sc002.Skf3050Sc002GetShatakuKanriNyukyoExistExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Sc002.Skf3050Sc002GetShatakuKanriParking1RiyoKaishiExistExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Sc002.Skf3050Sc002GetShatakuKanriParking2RiyoKaishiExistExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Sc002.Skf3050Sc002GetShatakuKanriParkingRiyoShuryoExistExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Sc002.Skf3050Sc002GetShatakuKanriParkingTeijiMisakuseiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Sc002.Skf3050Sc002GetShatakuKanriTaikyoExistExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MCompanyRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3050MAccountRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3050TMonthlyManageDataRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.LoginUserInfoUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.common.utils.PropertyUtils;
import jp.co.c_nexco.nfw.webcore.utils.bean.RowDataBean;
import jp.co.c_nexco.nfw.webcore.utils.bean.SheetDataBean;
import jp.co.c_nexco.nfw.webcore.utils.bean.WorkBookDataBean;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.SkfBatchBusinessLogicUtils;
import jp.co.c_nexco.skf.skf3050.domain.dto.skf3050Sc002common.Skf3050Sc002CommonDto;

/**
 * Skf3050Sc002SharedService 月次運用管理画面の共通処理
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3050Sc002SharedService {

	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	@Autowired
	private SkfBatchBusinessLogicUtils skfBatchBusinessLogicUtils;
	@Autowired
	private Skf3050Sc002GetGetsujiShoriJoukyouShoukaiExpRepository skf3050Sc002GetGetsujiShoriJoukyouShoukaiExpRepository;
	@Autowired
	private Skf3050TMonthlyManageDataRepository skf3050TMonthlyManageDataRepository;
	@Autowired
	private Skf3050Sc002GetBatchStartupCntExpRepository skf3050Sc002GetBatchStartupCntExpRepository;
	@Autowired
	private Skf3050Sc002GetKariShainBangoExpRepository skf3050Sc002GetKariShainBangoExpRepository;
	@Autowired
	private Skf3050Sc002GetHenkanShainBangoExpRepository skf3050Sc002GetHenkanShainBangoExpRepository;
	@Autowired
	private Skf3050Sc002GetShatakuKanriNyukyoExistExpRepository skf3050Sc002GetShatakuKanriNyukyoExistExpRepository;
	@Autowired
	private Skf3050Sc002GetShatakuKanriParkingTeijiMisakuseiDataExpRepository skf3050Sc002GetShatakuKanriParkingTeijiMisakuseiDataExpRepository;
	@Autowired
	private Skf3050Sc002GetShatakuKanriParking2RiyoKaishiExistExpRepository skf3050Sc002GetShatakuKanriParking2RiyoKaishiExistExpRepository;
	@Autowired
	private Skf3050Sc002GetShatakuKanriParking1RiyoKaishiExistExpRepository skf3050Sc002GetShatakuKanriParking1RiyoKaishiExistExpRepository;
	@Autowired
	private Skf3050Sc002GetParkingRirekiDataExpRepository skf3050Sc002GetParkingRirekiDataExpRepository;
	@Autowired
	private Skf3050Sc002GetShatakuKanriTaikyoExistExpRepository skf3050Sc002GetShatakuKanriTaikyoExistExpRepository;
	@Autowired
	private Skf3050Sc002GetShatakuKanriParkingRiyoShuryoExistExpRepository skf3050Sc002GetShatakuKanriParkingRiyoShuryoExistExpRepository;
	@Autowired
	private Skf3050Sc002GetBihinTaiyoDataExpRepository skf3050Sc002GetBihinTaiyoDataExpRepository;
	@Autowired
	private Skf3050Sc002GetBihinHenkyakuDataExpRepository skf3050Sc002GetBihinHenkyakuDataExpRepository;
	@Autowired
	private Skf3050Bt003GetPositiveGenbutsuSanteiSakuseiSyoriDataExpRepository skf3050Bt003GetPositiveGenbutsuSanteiSakuseiSyoriDataExpRepository;
	@Autowired
	private Skf3050Bt003GetPositiveRenkeiDataSakuseiSyoriDataExpRepository skf3050Bt003GetPositiveRenkeiDataSakuseiSyoriDataExpRepository;
	@Autowired
	private Skf3050Bt003GetPositiveRenkeiDataSakuseiBihinDataExpRepository skf3050Bt003GetPositiveRenkeiDataSakuseiBihinDataExpRepository;
	@Autowired
	private Skf3050Bt003UpdateGetsujiShoriKanriExpRepository skf3050Bt003UpdateGetsujiShoriKanriExpRepository;
	@Autowired
	private Skf3050Bt001UpdateGenbutsuSanteigakuExpRepository skf3050Bt001UpdateGenbutsuSanteigakuExpRepository;
	@Autowired
	private Skf3050Bt001UpdateBihinGoukeiExpRepository skf3050Bt001UpdateBihinGoukeiExpRepository;
	@Autowired
	private Skf3050Bt001GetTsukibetuSyoryoRirekiDataJoinDataExpRepository skf3050Bt001GetTsukibetuSyoryoRirekiDataJoinDataExpRepository;
	@Autowired
	private Skf3050Bt001GetShatakuKanriDaityoSogoriyoDataExpRepository skf3050Bt001GetShatakuKanriDaityoSogoriyoDataExpRepository;
	@Autowired
	private Skf3050Bt001GetTsukibetuTyusyajyoRirekiDataExpRepository skf3050Bt001GetTsukibetuTyusyajyoRirekiDataExpRepository;
	@Autowired
	private Skf3050Bt001GetBihinMeisaiExpRepository skf3050Bt001GetBihinMeisaiExpRepository;
	@Autowired
	private Skf3050Bt001UpdateBihinMeisaiExpRepository skf3050Bt001UpdateBihinMeisaiExpRepository;
	@Autowired
	private Skf3050Bt001GetBihinGenbutsuShikyugokeigakuExpRepository skf3050Bt001GetBihinGenbutsuShikyugokeigakuExpRepository;
	@Autowired
	private Skf3050Bt001UpdateTsukibetsuSiyoryorirekiDataExpRepository skf3050Bt001UpdateTsukibetsuSiyoryorirekiDataExpRepository;
	@Autowired
	private Skf3050Bt001GetShatakuSyainIdoRirekiDataExpRepository skf3050Bt001GetShatakuSyainIdoRirekiDataExpRepository;
	@Autowired
	private Skf3050Bt001GetShainSoshikiDataExpRepository skf3050Bt001GetShainSoshikiDataExpRepository;
	@Autowired
	private Skf3050Bt001GetCompanyAgencyNameExpRepository skf3050Bt001GetCompanyAgencyNameExpRepository;
	@Autowired
	private Skf3050Bt001UpdateShozokuRirekiExpRepository skf3050Bt001UpdateShozokuRirekiExpRepository;
	@Autowired
	private Skf3050Bt001GetShatakiKanriDaityoDataTodoufukenDataExpRepository skf3050Bt001GetShatakiKanriDaityoDataTodoufukenDataExpRepository;
	@Autowired
	private Skf3050Bt001UpdateTsukibetsuSiyoryorirekiGenbutsuSanteiExpRepository skf3050Bt001UpdateTsukibetsuSiyoryorirekiGenbutsuSanteiExpRepository;
	@Autowired
	private Skf3050Bt001GetHrRenkeiDataSakuseiMaeSyoriDataExpRepository skf3050Bt001GetHrRenkeiDataSakuseiMaeSyoriDataExpRepository;
	@Autowired
	private Skf3050Bt001GetShatakuKihonJyohoExpRepository skf3050Bt001GetShatakuKihonJyohoExpRepository;
	@Autowired
	private Skf3050Bt001UpdateTsukibetsuShiyoryoAccountExpRepository skf3050Bt001UpdateTsukibetsuShiyoryoAccountExpRepository;
	@Autowired
	private Skf3050Bt001UpdateGetsujiShoriKanriDataExpRepository skf3050Bt001UpdateGetsujiShoriKanriDataExpRepository;
	@Autowired
	private Skf3050Bt003GetGetsujiShoriKanriForUpdateExpRepository skf3050Bt003GetGetsujiShoriKanriForUpdateExpRepository;
	@Autowired
	private Skf1010MCompanyRepository skf1010MCompanyRepository;
	@Autowired
	private Skf3050MAccountRepository skf3050MAccountRepository;
	@Autowired
	private Skf3050Bt001GetDataForUpdateExpRepository skf3050Bt001GetDataForUpdateExpRepository;

	public static final String GRID_NENGETSU = "col1";
	public static final String GRID_SHIME_SHORI = "col2";
	public static final String GRID_POSITIVE_DATA = "col3";
	public static final String GRID_HDN_SHORINENGETSU = "col4";

	public static final String BILLING_ACT_KBN_MI_JIKKO = "0";
	public static final String BILLING_ACT_KBN_JIKKO_SUMI = "1";
	public static final String BILLING_ACT_KBN_KAIJO_CHU = "2";

	public static final int MONTH_1 = 1;
	public static final int MONTH_3 = 3;
	public static final int YEAR_1 = 12;
	public static final int NENGETSU_LEN = 4;

	public static final String NENGETSU_LIST_SHIME_JIKKOUSUMI = "実行済";
	public static final String SHIME_SHORI_ON = "1";

	public static final String ERRMSG_DOUBLE_START = "二重起動チェックエラーのため";
	public static final String ERRMSG_SHIME_IMPOSSIBLE = "締め状態が変更されているため";

	private static final String BATCH_PARAM_NAME_PRG_ID = "バッチプログラムID";
	private static final String BATCH_PARAM_NAME_COMPANY_CD = "会社コード";
	private static final String BATCH_PARAM_NAME_USER_ID = "ユーザID";
	private static final String BATCH_PARAM_NAME_SHORI_NENGETSU = "処理年月";
	public static final String BATCH_PARAM_NAME_SHIME_SHORI_FLG = "締め処理フラグ";

	public static final int SHIME_SHORI_PARAMETER_NUM = 5;
	public static final int PARAMETER_NUM = 4;

	public static final String SHIME_SHORI_BATCH_NAME = "締め処理";
	public static final String CREATE_POSITIVE_DATA_BATCH_NAME = "POSITIVE連携データ作成";

	public static final String BATCH_PRG_ID_KEY = "batchPrgId";
	public static final String COMPANY_CD_KEY = "batchCompanyCd";
	public static final String USER_ID_KEY = "batchUserId";
	public static final String SHORI_NENGETSU_KEY = "batchShoriNengetsu";
	public static final String SHIME_SHORI_FLG = "batchShimeShoriFlg";
	public static final String SHORI_RESULT_STS_KEY = "shoriSts";
	public static final String SHORI_RESULT_SIYO_UPD_CNT_KEY = "shiyouUpdCount";
	public static final String SHORI_RESULT_GEN_UPD_CNT_KEY = "genUpdCount";

	public static final int RETURN_STATUS_OK = 0;
	public static final int RETURN_STATUS_NG = 9;

	private static final String NENGETSU_KYUUYO_WORD = "月給与";
	private static final String NENGETSU_KYUUYO_1_MONTH_HEDDERWORD = "翌";
	private static final String KARI_SHAIN_NO_RECOG_CD = "K";
	private static final String SHAIN_NO_CHG_AVAILABLE = "1";

	private static final int PARKING_KUKAKU_0 = 0;
	private static final int PARKING_KUKAKU_1 = 1;
	private static final int PARKING_KUKAKU_2 = 2;

	private static final String IF0002 = "IF0002";
	private static final String IF0003 = "IF0003";
	private static final String IF0004 = "IF0004";
	private static final String IF0005 = "IF0005";
	private static final String IF0006 = "IF0006";
	private static final String IF0007 = "IF0007";
	private static final String IF0008 = "IF0008";
	private static final String IF0009 = "IF0009";
	private static final String IF0010 = "IF0010";
	private static final String KEIRIKANJOU_SHATAKU_KASHI_GYOUGAI = "7900010702";
	private static final String KEIRIKANJOU_AZUKARI_SHATAKU_KASHI = "4000112400";
	private static final String KEIRIKANJOU_CHIDAI_YACHIN = "8800040510";
	private static final String KEIRIKANJOU_AZUKARI_KYOUEKIHI = "4000111400";
	private static final String KEIRIKANJOU_AZUKARI_KYOUEKIHI_SHAGAI = "4000112300";

	private static final int START_LINE = 1;

	private static final String KEY_FILE_NAME = "skf3050.skf3050_bt003.file_name";
	private static final String KEY_SHEET_NAME = "skf3050.skf3050_bt003.sheet_name";

	private static final String SHIYORYO_GETSUGAKU_SHORI_KBN = "2";
	private static final String CHUSHAJO_SHIYORYO_GETSUGAKU_SHORI_KBN = "4";
	private static final String SHIME_SHORI = "1";

	private static final String ERRMSG_SHIME_KARISHAIN = "社宅管理台帳データに仮社員番号の社員が存在するため";
	private static final String ERRMSG_SHIME_KARISHAIN_CHG = "社宅管理台帳データに社員番号変更が必要な社員が存在するため";
	private static final String ERRMSG_SHIME_SHATAKU_DAITYO_NYUKYO = "当月入居予定のデータが社宅管理台帳に反映されていないため";
	private static final String ERRMSG_SHIME_SHATAKU_DAITYO_PARKING_TEIJI_MISAKUSEI_DATA = "提示データが作成されていない駐車場利用申請が存在するため";
	private static final String ERRMSG_SHIME_SHATAKU_DAITYO_PARKING_RIYOKAISHI = "当月利用開始予定の駐車場データが社宅管理台帳に反映されていないため";
	private static final String ERRMSG_SHIME_SHATAKU_DAITYO_TAIKYO = "当月退居予定のデータが社宅管理台帳に反映されていないため";
	private static final String ERRMSG_SHIME_SHATAKU_DAITYO_PARKING_RIYOSHURYO = "当月利用終了予定の駐車場データが社宅管理台帳に反映されていないため";
	private static final String ERRMSG_SHATAKUKANRIDAICHOU_0 = "社宅管理台帳相互利用基本";
	private static final String ERRMSG_SHATAKUKANRIDAICHOU_1 = "社宅管理台帳ID";
	private static final String ERRMSG_GENSHIKYU_0 = "現物支給価額設定";
	private static final String ERRMSG_GENSHIKYU_TEKIYO_1 = "適用日";
	private static final String ERRMSG_GENSHIKYU_TODOFU_1 = "都道府県コード";
	private static final String ERRMSG_SHATAKUKIHON_0 = "社宅基本情報";
	private static final String ERRMSG_SHATAKUKIHON_1 = "社宅管理番号";
	private static final String ERRMSG_KAIKEI_0 = "会計転記用計上勘定科目設定";
	private static final String ERRMSG_KAIKEI_ACCOUNT_ID_1 = "勘定科目ID";
	private static final String ERRMSG_PARAM_SAI_0 = "月別使用料履歴（再計算）";
	private static final String ERRMSG_PARAM_GEN_0 = "月別使用料履歴（現物算定額）";

	private static final String ACCOUNT_ID_1 = "1";
	private static final String ACCOUNT_ID_2 = "2";
	private static final String ACCOUNT_ID_3 = "3";
	private static final String ACCOUNT_ID_4 = "4";
	private static final String ACCOUNT_ID_5 = "5";
	private static final String KEIJOU_KAMOKU_ACNT_CD_KEY = "accountCd";
	private static final String KEIJOU_KAMOKU_ACNT_NM_KEY = "accountName";
	private static final String KEIJOU_KAMOKU_RTN_STS_KEY = "returnStatus";
	private static final String KEIJOU_KAMOKU_ERR_MSG_KEY = "errMsg";
	
	private static final String TAISHOUNENDO_NENDO = "年度　";
	private static final String LIST_CNT_KEY = "skf3050.skf3050sc002.list_max_cnt";
	
	@Value("${skf3050.skf3050_bt001.batch_prg_id}")
	private String closeBatchPrgId;
	@Value("${skf3050.skf3050_bt002.batch_prg_id}")
	private String closeCanselBatchPrgId;
	@Value("${skf3050.skf3050_bt003.batch_prg_id}")
	private String createPositiveDataBatchPrgId;
	@Value("${skf3050.skf3050_bt004.batch_prg_id}")
	private String confirmPositiveDataBatchPrgId;

	/**
	 * 年月リストの一覧情報取得
	 * 
	 * @param taishouNendo
	 *            対象期間
	 * @return 年月リストの一覧情報
	 */
	public List<Skf3050Sc002GetGetsujiShoriJoukyouShoukaiExp> getGetsujiShoriJoukyouShoukai(String taishouNendo) {

		Skf3050Sc002GetGetsujiShoriJoukyouShoukaiExpParameter param = new Skf3050Sc002GetGetsujiShoriJoukyouShoukaiExpParameter();
		param.setCycleBillingYymm1(taishouNendo.substring(0, NENGETSU_LEN));

		String nextTaishouNendo = skfDateFormatUtils.addYearMonth(taishouNendo + "01", YEAR_1);
		param.setCycleBillingYymm2(nextTaishouNendo.substring(0, NENGETSU_LEN));

		List<Skf3050Sc002GetGetsujiShoriJoukyouShoukaiExp> rtnData = skf3050Sc002GetGetsujiShoriJoukyouShoukaiExpRepository
				.getGetsujiShoriJoukyouShoukai(param);

		return rtnData;
	}

	/**
	 * 対象年度ドロップダウンリストを作成する。
	 * 
	 * @param sysDateYyyymm
	 *            システム日付
	 * @return 対象年度ドロップダウンリスト
	 */
	public List<Map<String, Object>> createTaishouNendoDropDownList(String sysDateYyyymm) {

		List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();

		if (NfwStringUtils.isEmpty(sysDateYyyymm) || sysDateYyyymm.length() < Skf3050Sc002SharedService.NENGETSU_LEN) {
			return rtnList;
		}

		int standardYyyy = Integer.parseInt(sysDateYyyymm);

		int listMaxCnt = Integer.parseInt(PropertyUtils.getValue(LIST_CNT_KEY));

		for (int i = 0; i < listMaxCnt; i++) {
			Map<String, Object> dropDownMap = new HashMap<String, Object>();

			String addYyyy = String.valueOf(standardYyyy - i);
			dropDownMap.put("value", addYyyy);
			dropDownMap.put("label", addYyyy + TAISHOUNENDO_NENDO);
			rtnList.add(dropDownMap);
		}

		return rtnList;
	}
	
	/**
	 * 締め処理区分、Positive連携区分の情報取得
	 * 
	 * @param cycleBillingYymm
	 *            対象年月
	 * @return 連携データ
	 */
	public Skf3050TMonthlyManageData getShimePositiveRenkeiKbn(String cycleBillingYymm) {

		Skf3050TMonthlyManageData rtnData = skf3050TMonthlyManageDataRepository.selectByPrimaryKey(cycleBillingYymm);

		return rtnData;
	}

	/**
	 * バッチ制御テーブルへ登録する。
	 * 
	 * @param parameter
	 *            パラメータ
	 * @return 結果
	 * @throws ParseException
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int registBatchControl(Map<String, String> parameter, String jikkoBatchId) throws ParseException {

		//取得可否チェック
		String retParameterName = checkParameter(parameter);
		String programId = createPositiveDataBatchPrgId;
		Date sysDate = getSystemDate();

		if (!NfwStringUtils.isEmpty(retParameterName)) {
			//パラメータチェックエラーの場合
			if (!retParameterName.contains(BATCH_PARAM_NAME_COMPANY_CD)) {
				//パラメータの会社コードが設定済みの場合
				//異常終了として、バッチ制御テーブルを登録
				skfBatchBusinessLogicUtils.insertBatchControl(parameter.get(COMPANY_CD_KEY), programId,
						parameter.get(USER_ID_KEY), SkfCommonConstant.ABNORMAL, sysDate, getSystemDate());

				LogUtils.error(MessageIdConstant.E_SKF_1089, retParameterName);
				return CodeConstant.SYS_NG;

			} else {
				LogUtils.error(MessageIdConstant.E_SKF_1089, BATCH_PARAM_NAME_COMPANY_CD);
				return CodeConstant.SYS_NG;
			}
		}

		//プログラムIDの設定
		if (!Objects.equals(jikkoBatchId, parameter.get(BATCH_PRG_ID_KEY))) {
			//異常終了として、バッチ制御テーブルを登録
			skfBatchBusinessLogicUtils.insertBatchControl(parameter.get(COMPANY_CD_KEY), programId,
					parameter.get(USER_ID_KEY), SkfCommonConstant.ABNORMAL, sysDate, getSystemDate());

			LogUtils.errorByMsg("バッチプログラムIDが正しくありません。（バッチプログラムID：" + parameter.get(BATCH_PRG_ID_KEY) + "）");
			return CodeConstant.SYS_NG;
		}

		//処理中として、バッチ制御テーブルを登録
		skfBatchBusinessLogicUtils.insertBatchControl(parameter.get(COMPANY_CD_KEY), programId,
				parameter.get(USER_ID_KEY), SkfCommonConstant.PROCESSING, sysDate, null);

		return CodeConstant.SYS_OK;
	}

	/**
	 * POSITIVE連携データをエクセルに出力する。
	 * 
	 * @param parameter
	 *            パラメータ
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Map<String, Object> createPositiveCooperationData(Map<String, String> parameter) throws Exception {

		String paramShoriNengetsu = parameter.get(SHORI_NENGETSU_KEY);
		String paramCompanyCd = parameter.get(COMPANY_CD_KEY);

		//社員番号ごとの行リスト
		Map<String,Skf3050Sc002PositiveRenkeiInfoExp> shainRowMap = new HashMap<String,Skf3050Sc002PositiveRenkeiInfoExp>();
		
		List<RowDataBean> rowDataBeanList = new ArrayList<>();

		//HR連携データ出力（IF0002）
		shainRowMap = createExcelFilePositiveRenkeiInfo(shainRowMap, paramShoriNengetsu, paramCompanyCd,
				KEIRIKANJOU_SHATAKU_KASHI_GYOUGAI, "", true, false, false, false, IF0002);

		//HR連携データ出力（IF0003）
		shainRowMap = createExcelFilePositiveRenkeiInfo(shainRowMap, paramShoriNengetsu, paramCompanyCd,
				KEIRIKANJOU_SHATAKU_KASHI_GYOUGAI, "", false, true, false, false, IF0003);

		//HR連携データ出力（IF0004）
		shainRowMap = createExcelFilePositiveRenkeiInfo(shainRowMap, paramShoriNengetsu, paramCompanyCd,
				KEIRIKANJOU_AZUKARI_SHATAKU_KASHI, "", true, false, false, false, IF0004);

		//HR連携データ出力（IF0005）
		shainRowMap = createExcelFilePositiveRenkeiInfo(shainRowMap, paramShoriNengetsu, paramCompanyCd,
				KEIRIKANJOU_AZUKARI_SHATAKU_KASHI, "", false, true, false, false, IF0005);

		//HR連携データ出力（IF0006）
		shainRowMap = createExcelFilePositiveRenkeiInfo(shainRowMap, paramShoriNengetsu, paramCompanyCd,
				KEIRIKANJOU_CHIDAI_YACHIN, "", true, false, false, false, IF0006);

		//HR連携データ出力（IF0007）
		shainRowMap = createExcelFilePositiveRenkeiInfo(shainRowMap, paramShoriNengetsu, paramCompanyCd,
				KEIRIKANJOU_CHIDAI_YACHIN, "", false, true, false, false, IF0007);

		//HR連携データ出力（IF0008）
		shainRowMap = createExcelFilePositiveRenkeiInfo(shainRowMap, paramShoriNengetsu, paramCompanyCd, "",
				KEIRIKANJOU_AZUKARI_KYOUEKIHI, false, false, true, false, IF0008);

		//HR連携データ出力（IF0009）
		shainRowMap = createExcelFilePositiveRenkeiInfo(shainRowMap, paramShoriNengetsu, paramCompanyCd, "",
				KEIRIKANJOU_AZUKARI_KYOUEKIHI_SHAGAI, false, false, true, false, IF0009);

		//HR連携データ出力（IF0010）
		shainRowMap = createExcelFilePositiveRenkeiInfo(shainRowMap, paramShoriNengetsu, paramCompanyCd, "", "",
				false, false, false, true, IF0010);

		//▼備品現物支給額データ作成処理▼
		//貸与備品現物データ出力（IF0011）
		shainRowMap = createExcelFilePositiveRenkeiBihinIF0011Info(shainRowMap, paramShoriNengetsu,
				paramCompanyCd);
		
		int targetRow = START_LINE;
		//キーでソート
		Object[] mapKey = shainRowMap.keySet().toArray();
		Arrays.sort(mapKey);
		for(Object shainNo : mapKey){
			Skf3050Sc002PositiveRenkeiInfoExp rowExp = shainRowMap.get(shainNo.toString());
			targetRow++;
			RowDataBean rowData = createRowData(targetRow, shainNo.toString(), paramShoriNengetsu, 
					rowExp.getSiyoryoHoyu(), rowExp.getChusyajyoHoyu(), rowExp.getSiyoryoShagai(), 
					rowExp.getChusyajyoShagai(), rowExp.getSiyoryoKasiage(), rowExp.getChusyajyoKasiage(), 
					rowExp.getKyoekihiHoyu(), rowExp.getKyoekihiShagai(), 
					rowExp.getGenbutugakuShaho(), rowExp.getBihingenbutuShaho());
			if (rowData != null) {
				rowDataBeanList.add(rowData);
			}
		}

		Map<String, Object> rtnMap = outputExcel(rowDataBeanList);

		List<String> result = skf3050Bt003GetGetsujiShoriKanriForUpdateExpRepository
				.getGetsujiShoriKanriForUpdate(paramShoriNengetsu);
		if (result.size() == 0) {
			return null;
		}
		
		//▼月次処理管理データ更新▼
		updateGetsujiShoriKanri(parameter.get(USER_ID_KEY), paramShoriNengetsu);

		return rtnMap;
	}

	/**
	 * バッチ制御テーブルを更新
	 * 
	 * @param endFlag
	 *            終了フラグ
	 * @param companyCd
	 *            会社コード
	 * @param programId
	 *            プログラムID
	 * @param searchEndFlag
	 *            検索用終了フラグ
	 * @throws ParseException
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void endCreatePositiveDataProc(String companyCd, String endFlg) throws ParseException {

		//バッチ制御テーブルを更新
		skfBatchBusinessLogicUtils.updateBatchControl(
				endFlg, companyCd, createPositiveDataBatchPrgId, SkfCommonConstant.PROCESSING);
	}

	/**
	 * システムデータを取得する。
	 * 
	 * @return システムデータ
	 */
	public Date getSystemDate() {

		Date systemDate = skfBaseBusinessLogicUtils.getSystemDateTime();

		return systemDate;
	}

	/**
	 * POSITIVE連携データ作成（Excelファイル作成）
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @param kyuyoCompanyCd
	 *            給与支給会社コード
	 * @param saveFolder
	 *            保存先フォルダ名
	 * @param shatakuKanjouCd
	 *            社宅使用料計上勘定科目コード
	 * @param kyouekiKanjouCd
	 *            共栄費個人負担金計上勘定科目コード
	 * @param shatakuShiyouFlg
	 *            社宅使用料
	 * @param tyushajoShiyouFlg
	 *            駐車場使用料
	 * @param kyouekihiFlg
	 *            共益費（個人負担金）
	 * @param genbutsuSanteiFlg
	 *            現物算定額
	 * @param interfaceId
	 *            インターフェースID
	 * @return 作成件数
	 * @throws Exception
	 */
	private Map<String,Skf3050Sc002PositiveRenkeiInfoExp> createExcelFilePositiveRenkeiInfo(Map<String,Skf3050Sc002PositiveRenkeiInfoExp> shainRowMap, String shoriNengetsu,
			String kyuyoCompanyCd, String shatakuKanjouCd, String kyouekiKanjouCd, boolean shatakuShiyouFlg,
			boolean tyushajoShiyouFlg, boolean kyouekihiFlg, boolean genbutsuSanteiFlg, String interfaceId)
			throws Exception {

		//社宅現物算定(1310)データ出力の場合
		if (IF0010.equals(interfaceId)) {

			//連携データを取得(入居中の社宅のみ）
			List<Skf3050Bt003GetPositiveGenbutsuSanteiSakuseiSyoriDataExp> genbutsuDataList = getPositiveGenbutsuSanteiSakuseiSyoriData(
					shoriNengetsu, kyuyoCompanyCd, genbutsuSanteiFlg);

			//データが取得出来たら
			if (genbutsuDataList.size() > 0) {
//				int targetRow = START_LINE;
				//社員番号
				String shainNo = "";
				//現物算定額合計(入居中のみ）
				Integer genbutsuSum = 0;
				//現物算定額合計
				Integer genbutsuAllSum = 0;
				

//				if (rowDataBeanList.size() != 0) {
//					targetRow = rowDataBeanList.size() + START_LINE;
//				}

				//データ行だけ繰り返す
				for (int i = 0; i < genbutsuDataList.size(); i++) {
					Skf3050Bt003GetPositiveGenbutsuSanteiSakuseiSyoriDataExp genbutsuShori = genbutsuDataList.get(i);

//					targetRow++;

					//処理月翌月の末日(YYYYMMDD)
					String shoriNengetsuMatsujitsu = getTaishoutsukiYokugetsuMatsu(shoriNengetsu);

					//社員番号有無
					if (SkfCheckUtils.isNullOrEmpty(genbutsuShori.getShainNo())) {
						//nullまたは空の場合スキップ
						continue;
					}
					//社員番号が空だった場合
					if(SkfCheckUtils.isNullOrEmpty(shainNo)){
						shainNo = genbutsuShori.getShainNo();
					//社員番号が変更された場合
					}else if(!Objects.equals(shainNo, genbutsuShori.getShainNo())){
						Skf3050Sc002PositiveRenkeiInfoExp setExp = shainRowMap.get(shainNo);
						
						if(genbutsuSum > 0){
							//現物算定額合計(入居中のみ）
							setExp.setGenbutugakuShaho(genbutsuSum.toString());
						}else{
							//現物算定額合計
							setExp.setGenbutugakuShaho(genbutsuAllSum.toString());
						}
						
						shainRowMap.replace(shainNo, setExp);
						//社員番号変更時の初期化処理
						//現物算定額合計(入居中のみ）
						genbutsuSum = 0;
						//現物算定額合計
						genbutsuAllSum = 0;
						shainNo = genbutsuShori.getShainNo();
					}
					
					//社員番号リスト存在確認
					if(!shainRowMap.containsKey(shainNo)){
						//存在していないため、Mapに追加
						Skf3050Sc002PositiveRenkeiInfoExp newExp = new Skf3050Sc002PositiveRenkeiInfoExp();
						newExp.setShainNo(shainNo);
						newExp.setShoriNengetsu(shoriNengetsu);
						shainRowMap.put(shainNo, newExp);
					}
					
					//入居日が処理月翌月の末日より小さく、退居日が空、もしくは処理月翌月初日より大きい場合
					if(genbutsuShori.getNyukyoDate() != null){
						int nyukyoDate = Integer.parseInt(genbutsuShori.getNyukyoDate());
						int shoriNengetsuMatsujitsuNum = Integer.parseInt(shoriNengetsuMatsujitsu);
						if(nyukyoDate <= shoriNengetsuMatsujitsuNum &&
							(SkfCheckUtils.isNullOrEmpty(genbutsuShori.getTaikyoDate()) || Integer.parseInt(genbutsuShori.getTaikyoDate()) > shoriNengetsuMatsujitsuNum)){
							//現物算定額合計(入居中のみ）
							 genbutsuSum += genbutsuShori.getGenbutuSantei();
						}
					}
					

					
					//現物算定額合計
					genbutsuAllSum += genbutsuShori.getGenbutuSantei();
//					String genbutuSantei = "";
//					if (genbutsuShori.getGenbutuSantei() != null) {
//						genbutuSantei = genbutsuShori.getGenbutuSantei().toString();
//					}
//
//					RowDataBean rowData = createRowData(targetRow, genbutsuShori.getShainNo(), shoriNengetsu, "", "",
//							"", "", "", "", "", "", genbutuSantei, "");
//					if (rowData != null) {
//						rowDataBeanList.add(rowData);
//					}
				}
				Skf3050Sc002PositiveRenkeiInfoExp setExp = shainRowMap.get(shainNo);
				
				if(genbutsuSum > 0){
					//現物算定額合計(入居中のみ）
					setExp.setGenbutugakuShaho(genbutsuSum.toString());
				}else{
					//現物算定額合計
					setExp.setGenbutugakuShaho(genbutsuAllSum.toString());
				}
				
				shainRowMap.replace(shainNo, setExp);
			}
		//現物支給額WT（1310）以外
		} else {
			//連携データを取得
			List<Skf3050Bt003GetPositiveRenkeiDataSakuseiSyoriDataExp> sakuseiSyoriDataList = getPositiveRenkeiDataSakuseiSyoriData(
					shoriNengetsu, kyuyoCompanyCd, shatakuKanjouCd, kyouekiKanjouCd, shatakuShiyouFlg,
					tyushajoShiyouFlg, kyouekihiFlg, genbutsuSanteiFlg);

			//該当データが存在する場合
			if (sakuseiSyoriDataList.size() > 0) {
//				int targetRow = START_LINE;
//
//				if (rowDataBeanList.size() != 0) {
//					targetRow = rowDataBeanList.size() + START_LINE;
//				}

				//取得した情報分繰り返す
				for (int i = 0; i < sakuseiSyoriDataList.size(); i++) {

					Skf3050Bt003GetPositiveRenkeiDataSakuseiSyoriDataExp sakuseiSyoriData = sakuseiSyoriDataList.get(i);

					String shainNo = "";
					if (sakuseiSyoriData.getShainNo() != null) {
						shainNo = sakuseiSyoriData.getShainNo();
					}else{
						continue;
					}
					
					//社員番号リスト存在確認
					if(!shainRowMap.containsKey(shainNo)){
						//存在していないため、Mapに追加
						Skf3050Sc002PositiveRenkeiInfoExp newExp = new Skf3050Sc002PositiveRenkeiInfoExp();
						newExp.setShainNo(shainNo);
						newExp.setShoriNengetsu(shoriNengetsu);
						shainRowMap.put(shainNo, newExp);
					}
					
					Skf3050Sc002PositiveRenkeiInfoExp setExp = shainRowMap.get(shainNo);

					String siyoryoHoyu = "";
					String chusyajyoHoyu = "";
					String siyoryoShagai = "";
					String chusyajyoShagai = "";
					String siyoryoKasiage = "";
					String chusyajyoKasiage = "";
					String kyoekihiHoyu = "";
					String kyoekihiShagai = "";
					//社宅使用料、駐車場使用料、共益費、社宅現物算定額
					switch (interfaceId) {
					case IF0002:
						if (sakuseiSyoriData.getRentalTotal() != null) {
							//社宅使用料月額（調整後）
							siyoryoHoyu = sakuseiSyoriData.getRentalTotal().toString();
							setExp.setSiyoryoHoyu(siyoryoHoyu);
						}
						break;
					case IF0003:
						if (sakuseiSyoriData.getParkingRentalTotal() != null) {
							//駐車場使用料月額（調整後）
							chusyajyoHoyu = sakuseiSyoriData.getParkingRentalTotal().toString();
							setExp.setChusyajyoHoyu(chusyajyoHoyu);
						}
						break;
					case IF0004:
						if (sakuseiSyoriData.getRentalTotal() != null) {
							//社宅使用料月額（調整後）
							siyoryoShagai = sakuseiSyoriData.getRentalTotal().toString();
							setExp.setSiyoryoShagai(siyoryoShagai);
						}
						break;
					case IF0005:
						if (sakuseiSyoriData.getParkingRentalTotal() != null) {
							//駐車場使用料月額（調整後）
							chusyajyoShagai = sakuseiSyoriData.getParkingRentalTotal().toString();
							setExp.setChusyajyoShagai(chusyajyoShagai);
						}
						break;
					case IF0006:
						if (sakuseiSyoriData.getRentalTotal() != null) {
							//社宅使用料月額（調整後）
							siyoryoKasiage = sakuseiSyoriData.getRentalTotal().toString();
							setExp.setSiyoryoKasiage(siyoryoKasiage);
						}
						break;
					case IF0007:
						if (sakuseiSyoriData.getParkingRentalTotal() != null) {
							//駐車場使用料月額（調整後）
							chusyajyoKasiage = sakuseiSyoriData.getParkingRentalTotal().toString();
							setExp.setChusyajyoKasiage(chusyajyoKasiage);
						}
						break;
					case IF0008:
						if (sakuseiSyoriData.getKyoekihiPersonTotal() != null) {
							//個人負担共益費月額（調整後）
							kyoekihiHoyu = sakuseiSyoriData.getKyoekihiPersonTotal().toString();
							setExp.setKyoekihiHoyu(kyoekihiHoyu);
						}
						break;
					case IF0009:
						if (sakuseiSyoriData.getKyoekihiPersonTotal() != null) {
							//個人負担共益費月額（調整後）
							kyoekihiShagai = sakuseiSyoriData.getKyoekihiPersonTotal().toString();
							setExp.setKyoekihiShagai(kyoekihiShagai);
						}
						break;
					}

					shainRowMap.replace(shainNo, setExp);
//					targetRow++;
//					RowDataBean rowData = createRowData(targetRow, shainNo, shoriNengetsu, siyoryoHoyu, chusyajyoHoyu,
//							siyoryoShagai, chusyajyoShagai, siyoryoKasiage, chusyajyoKasiage, kyoekihiHoyu,
//							kyoekihiShagai, "", "");
//					if (rowData != null) {
//						rowDataBeanList.add(rowData);
//					}
				}
			}
		}

		return shainRowMap;
	}

	/**
	 * 月別使用料履歴データ取得(現物算定額用)
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @param kyuyoCompanyCd
	 *            給与支給会社コード
	 * @param genbutsuSanteiFlg
	 *            現物算定額フラグ
	 * @return 月別使用料履歴データ
	 */
	private List<Skf3050Bt003GetPositiveGenbutsuSanteiSakuseiSyoriDataExp> getPositiveGenbutsuSanteiSakuseiSyoriData(
			String shoriNengetsu, String kyuyoCompanyCd, boolean genbutsuSanteiFlg) {

		Skf3050Bt003GetPositiveGenbutsuSanteiSakuseiSyoriDataExpParameter param = new Skf3050Bt003GetPositiveGenbutsuSanteiSakuseiSyoriDataExpParameter();

		param.setYearMonth(shoriNengetsu);
		param.setPayCompanyCd(kyuyoCompanyCd);

		if (genbutsuSanteiFlg) {
			param.setGenbutuSantei(0);
			param.setGenbutuSanteiFlg("1");
		} else {
			param.setGenbutuSantei(null);
			param.setGenbutuSanteiFlg("0");
		}

		List<Skf3050Bt003GetPositiveGenbutsuSanteiSakuseiSyoriDataExp> outData = skf3050Bt003GetPositiveGenbutsuSanteiSakuseiSyoriDataExpRepository
				.getPositiveGenbutsuSanteiSakuseiSyoriData(param);

		return outData;
	}

	/**
	 * エクセルシートに出力する１列分のデータを作成する。
	 * 
	 * @param targetRow
	 *            対象列
	 * @param shaiNo
	 *            社員番号
	 * @param shoriNengetsu
	 *            処理年月
	 * @param siyoryoHoyu
	 *            社宅使用料保有
	 * @param chusyajyoHoyu
	 *            駐車場使用料保有
	 * @param siyoryoShagai
	 *            社宅使用料社外
	 * @param chusyajyoShagai
	 *            駐車場使用料社外
	 * @param siyoryoKasiage
	 *            社宅使用料借上
	 * @param chusyajyoKasiage
	 *            駐車場使用料借上
	 * @param kyoekihiHoyu
	 *            共益費保有・借上
	 * @param kyoekihiShagai
	 *            共益費社外
	 * @param genbutugakuShaho
	 *            社宅価格現物(社保)
	 * @param bihingenbutuShaho
	 *            貸与備品現物(課税・社保)
	 * @return エクセルシートに出力する１列分のデータ
	 */
	private RowDataBean createRowData(int targetRow, String shaiNo, String shoriNengetsu, String siyoryoHoyu,
			String chusyajyoHoyu, String siyoryoShagai, String chusyajyoShagai, String siyoryoKasiage,
			String chusyajyoKasiage, String kyoekihiHoyu, String kyoekihiShagai, String genbutugakuShaho,
			String bihingenbutuShaho) {

		if (NfwStringUtils.isEmpty(shaiNo) && NfwStringUtils.isEmpty(shoriNengetsu)
				&& NfwStringUtils.isEmpty(siyoryoHoyu) && NfwStringUtils.isEmpty(chusyajyoHoyu)
				&& NfwStringUtils.isEmpty(siyoryoShagai) && NfwStringUtils.isEmpty(chusyajyoShagai)
				&& NfwStringUtils.isEmpty(siyoryoKasiage) && NfwStringUtils.isEmpty(chusyajyoKasiage)
				&& NfwStringUtils.isEmpty(kyoekihiHoyu) && NfwStringUtils.isEmpty(kyoekihiShagai)
				&& NfwStringUtils.isEmpty(genbutugakuShaho) && NfwStringUtils.isEmpty(bihingenbutuShaho)) {
			return null;
		}

		RowDataBean rtnRowData = new RowDataBean();

		rtnRowData.addCellDataBean("A" + targetRow, "1");
		rtnRowData.addCellDataBean("B" + targetRow, shaiNo == null ? "" : shaiNo);
		rtnRowData.addCellDataBean("C" + targetRow, "1");
		rtnRowData.addCellDataBean("D" + targetRow, "0");
		rtnRowData.addCellDataBean("E" + targetRow, shoriNengetsu == null ? "" : shoriNengetsu);
		rtnRowData.addCellDataBean("F" + targetRow, "");
		rtnRowData.addCellDataBean("G" + targetRow, siyoryoHoyu == null ? "" : siyoryoHoyu);
		rtnRowData.addCellDataBean("H" + targetRow, chusyajyoHoyu == null ? "" : chusyajyoHoyu);
		rtnRowData.addCellDataBean("I" + targetRow, siyoryoShagai == null ? "" : siyoryoShagai);
		rtnRowData.addCellDataBean("J" + targetRow, chusyajyoShagai == null ? "" : chusyajyoShagai);
		rtnRowData.addCellDataBean("K" + targetRow, siyoryoKasiage == null ? "" : siyoryoKasiage);
		rtnRowData.addCellDataBean("L" + targetRow, chusyajyoKasiage == null ? "" : chusyajyoKasiage);
		rtnRowData.addCellDataBean("M" + targetRow, kyoekihiHoyu == null ? "" : kyoekihiHoyu);
		rtnRowData.addCellDataBean("N" + targetRow, kyoekihiShagai == null ? "" : kyoekihiShagai);
		rtnRowData.addCellDataBean("O" + targetRow, genbutugakuShaho == null ? "" : genbutugakuShaho);
		rtnRowData.addCellDataBean("P" + targetRow, bihingenbutuShaho == null ? "" : bihingenbutuShaho);
		rtnRowData.addCellDataBean("Q" + targetRow, "");
		rtnRowData.addCellDataBean("R" + targetRow, "");
		rtnRowData.addCellDataBean("S" + targetRow, "");
		rtnRowData.addCellDataBean("T" + targetRow, "");

		return rtnRowData;
	}

	/**
	 * エクセルファイルを作成して出力する。
	 * 
	 * @param rowDataBeanList
	 *            列ごとのデータリスト
	 * @param sheetName
	 *            シート名
	 * @throws Exception
	 */
	private Map<String, Object> outputExcel(List<RowDataBean> rowDataBeanList) throws Exception {

		SheetDataBean sheetDataBean = new SheetDataBean();
		String sheetName = PropertyUtils.getValue(KEY_SHEET_NAME);
		sheetDataBean.setSheetName(sheetName);
		sheetDataBean.setRowDataBeanList(rowDataBeanList);

		List<SheetDataBean> sheetDataBeanList = new ArrayList<>();
		sheetDataBeanList.add(sheetDataBean);

		String fileName = sheetName + DateTime.now().toString("YYYYMMddHHmmss")
				+ CodeConstant.DOT + CodeConstant.EXTENSION_XLSX;
		WorkBookDataBean wbdb = new WorkBookDataBean(fileName);
		wbdb.setSheetDataBeanList(sheetDataBeanList);

		Map<String, Object> cellparams = new HashMap<>();
		Map<String, Object> resultMap = new HashMap<>();

		String functionId = PropertyUtils.getValue("skf3050.skf3050_bt003.output_file_function_id");

		SkfFileOutputUtils.fileOutputExcel(wbdb, cellparams, KEY_FILE_NAME, functionId, START_LINE, null, resultMap);

		resultMap.put("uploadFileName", fileName);

		// 解放
		sheetDataBean = null;
		sheetDataBeanList = null;
		cellparams = null;
		wbdb = null;

		return resultMap;
	}

	/**
	 * 月別使用料履歴データ取得
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @param kyuyoCompanyCd
	 *            給与支給会社コード
	 * @param shatakuKanjouCd
	 *            社宅使用料計上勘定科目コード
	 * @param kyouekihiKanjouCd
	 *            共益費個人負担金計上勘定科目コード
	 * @param shatakuShiyouFlg
	 *            社宅使用料
	 * @param tyushajoShiyouFlg
	 *            駐車場使用料
	 * @param kyouekihiFlg
	 *            共益費（個人負担金）
	 * @param genbutsuSanteiFlg
	 *            現物算定額
	 * @return 月別使用料履歴データ
	 */
	private List<Skf3050Bt003GetPositiveRenkeiDataSakuseiSyoriDataExp> getPositiveRenkeiDataSakuseiSyoriData(
			String shoriNengetsu, String kyuyoCompanyCd, String shatakuKanjouCd, String kyouekihiKanjouCd,
			boolean shatakuShiyouFlg, boolean tyushajoShiyouFlg, boolean kyouekihiFlg, boolean genbutsuSanteiFlg) {

		Skf3050Bt003GetPositiveRenkeiDataSakuseiSyoriDataExpParameter param = new Skf3050Bt003GetPositiveRenkeiDataSakuseiSyoriDataExpParameter();

		param.setYearMonth(shoriNengetsu);
		param.setPayCompanyCd(kyuyoCompanyCd);

		if (!NfwStringUtils.isEmpty(shatakuKanjouCd)) {
			param.setShatakuAccountCd(shatakuKanjouCd);
			param.setShatakukanjouCd(shatakuKanjouCd);
		} else {
			param.setShatakuAccountCd(null);
			param.setShatakukanjouCd(null);
		}

		if (!NfwStringUtils.isEmpty(kyouekihiKanjouCd)) {
			param.setKyoekiAccountCd(kyouekihiKanjouCd);
			param.setKyouekihiKanjouCd(kyouekihiKanjouCd);
		} else {
			param.setKyoekiAccountCd(null);
			param.setKyouekihiKanjouCd(null);
		}

		if (shatakuShiyouFlg) {
			param.setRentalTotal(0);
			param.setShatakushiyouFlg("1");
		} else {
			param.setShatakushiyouFlg("0");
		}

		if (tyushajoShiyouFlg) {
			param.setParkingRentalTotal(0);
			param.setTyushajoShiyouFlg("1");
		} else {
			param.setTyushajoShiyouFlg("0");
		}

		if (kyouekihiFlg) {
			param.setKyoekihiPersonTotal(0);
			param.setKyouekihiFlg("1");
		} else {
			param.setKyouekihiFlg("0");
		}

		if (genbutsuSanteiFlg) {
			param.setGenbutuSantei(0);
			param.setGenbutsusanteiFlg("1");
		} else {
			param.setGenbutsusanteiFlg("0");
		}

		List<Skf3050Bt003GetPositiveRenkeiDataSakuseiSyoriDataExp> outData = skf3050Bt003GetPositiveRenkeiDataSakuseiSyoriDataExpRepository
				.getPositiveRenkeiDataSakuseiSyoriData(param);

		return outData;
	}

	/**
	 * 貸与データの作成（Excelファイル作成）
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @param kyuyoCompanyCd
	 *            給与支給会社コード
	 * @param saveFolder
	 *            保存先フォルダ名
	 * @param wageType
	 *            ウェッジタイプ
	 * @return 作成件数
	 * @throws Exception
	 */
	private Map<String,Skf3050Sc002PositiveRenkeiInfoExp> createExcelFilePositiveRenkeiBihinIF0011Info(Map<String,Skf3050Sc002PositiveRenkeiInfoExp> shainRowMap,
			String shoriNengetsu, String kyuyoCompanyCd) throws Exception {

		//連携データ備品を取得
		List<Skf3050Bt003GetPositiveRenkeiDataSakuseiBihinDataExp> bihinDataList = getPositiveRenkeiDataSakuseiBihinData(
				shoriNengetsu, kyuyoCompanyCd);

		//該当データが存在する場合
		if (bihinDataList.size() > 0) {
//			int targetRow = START_LINE;
//
//			if (rowDataBeanList.size() != 0) {
//				targetRow = rowDataBeanList.size() + START_LINE;
//			}

			//取得した情報分繰り返す
			for (int i = 0; i < bihinDataList.size(); i++) {
				Skf3050Bt003GetPositiveRenkeiDataSakuseiBihinDataExp bihinData = bihinDataList.get(i);

				String shainNo = "";
				if (!NfwStringUtils.isEmpty(bihinData.getShainNo())) {
					shainNo = bihinData.getShainNo();
				}else{
					continue;
				}
				
				//社員番号リスト存在確認
				if(!shainRowMap.containsKey(shainNo)){
					//存在していないため、Mapに追加
					Skf3050Sc002PositiveRenkeiInfoExp newExp = new Skf3050Sc002PositiveRenkeiInfoExp();
					newExp.setShainNo(shainNo);
					newExp.setShoriNengetsu(shoriNengetsu);
					shainRowMap.put(shainNo, newExp);
				}
				
				Skf3050Sc002PositiveRenkeiInfoExp setExp = shainRowMap.get(shainNo);

				//貸与備品レンタル額
				String bihinGenbutuGoukei = "";
				if (bihinData.getBihinGenbutuGoukei() != null) {
					bihinGenbutuGoukei = bihinData.getBihinGenbutuGoukei().toString();
					setExp.setBihingenbutuShaho(bihinGenbutuGoukei);
				}

				shainRowMap.replace(shainNo, setExp);
//				targetRow++;
//
//				RowDataBean rowData = createRowData(targetRow, shainNo, shoriNengetsu, "", "", "", "", "", "", "", "",
//						"", bihinGenbutuGoukei);
//				if (rowData != null) {
//					rowDataBeanList.add(rowData);
//				}
			}
		}

		return shainRowMap;
	}

	/**
	 * POSITIVE連携備品データ取得
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @param kyuyoCompanyCd
	 *            給与支給会社コード
	 * @return POSITIVE連携備品データ
	 */
	private List<Skf3050Bt003GetPositiveRenkeiDataSakuseiBihinDataExp> getPositiveRenkeiDataSakuseiBihinData(
			String shoriNengetsu, String kyuyoCompanyCd) {

		Skf3050Bt003GetPositiveRenkeiDataSakuseiBihinDataExpParameter param = new Skf3050Bt003GetPositiveRenkeiDataSakuseiBihinDataExpParameter();

		param.setYearMonth(shoriNengetsu);

		if (kyuyoCompanyCd == null) {
			param.setPayCompanyCd(null);
		} else {
			param.setPayCompanyCd(kyuyoCompanyCd);
		}

		List<Skf3050Bt003GetPositiveRenkeiDataSakuseiBihinDataExp> outData = skf3050Bt003GetPositiveRenkeiDataSakuseiBihinDataExpRepository
				.getPositiveRenkeiDataSakuseiBihinData(param);

		return outData;
	}

	/**
	 * 月次処理管理テーブルのデータ更新
	 * 
	 * @param updateUser
	 *            更新ユーザ
	 * @param shoriNengetsu
	 *            処理年月
	 */
	private Integer updateGetsujiShoriKanri(String updateUser, String shoriNengetsu) {

		Skf3050Bt003UpdateGetsujiShoriKanriExpParameter param = new Skf3050Bt003UpdateGetsujiShoriKanriExpParameter();

		if (updateUser == null) {
			param.setUpdateUserId(null);
		} else {
			param.setUpdateUserId(updateUser);
		}

		param.setCycleBillingYymm(shoriNengetsu);
		param.setUpdateProgramId(createPositiveDataBatchPrgId);

		Integer updCnt = skf3050Bt003UpdateGetsujiShoriKanriExpRepository.updateGetsujiShoriKanri(param);

		LogUtils.debugByMsg("月次処理管理テーブル更新件数：" + updCnt + "件");

		return updCnt;
	}

	/**
	 * 基準日を取得する。
	 * 
	 * @param sysProcYymm
	 *            システム処理年月
	 * @return 基準日
	 */
	public String getStandardYear(String sysProcYymm) {

		int standardYyyy = 0;

		if (!NfwStringUtils.isEmpty(sysProcYymm)) {
			String dateYyyy = sysProcYymm.substring(0, NENGETSU_LEN);
			String dateMm = sysProcYymm.substring(NENGETSU_LEN);

			if (Integer.parseInt(dateMm) >= Skf3050Sc002SharedService.MONTH_1 && Integer.parseInt(dateMm) <= MONTH_3) {
				standardYyyy = Integer.parseInt(dateYyyy) - Skf3050Sc002SharedService.MONTH_1;
			} else {
				standardYyyy = Integer.parseInt(dateYyyy);
			}
		}

		String rtn = String.valueOf(standardYyyy);
		return rtn;
	}

	/**
	 * 月次処理一覧グリッドビューリストを作成する。
	 * 
	 * @param sysDateYyyymm
	 *            システム処理年月
	 * @return 月次処理一覧グリッドビューリスト
	 */
	public List<Map<String, Object>> createGetsujiGrid(String sysDateYyyymm) {
		
		List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();
		
		if (NfwStringUtils.isEmpty(sysDateYyyymm) || sysDateYyyymm.length() < NENGETSU_LEN) {
			return rtnList;
		}

		List<Skf3050Sc002GetGetsujiShoriJoukyouShoukaiExp> getsujiList = getGetsujiShoriJoukyouShoukai(sysDateYyyymm);

		for (int i = 0; i < getsujiList.size(); i++) {
			Skf3050Sc002GetGetsujiShoriJoukyouShoukaiExp setData = getsujiList.get(i);
			Map<String, Object> targetMap = new HashMap<String, Object>();

			String nengetsu = "";
			String kyuyoMonth = editKyuyoMonth(setData.getSort());

			if ("1".equals(kyuyoMonth)) {
				nengetsu = setData.getNengetsu() + "(" + NENGETSU_KYUUYO_1_MONTH_HEDDERWORD + kyuyoMonth
						+ NENGETSU_KYUUYO_WORD + ")";
			} else {
				nengetsu = setData.getNengetsu() + "(" + kyuyoMonth + NENGETSU_KYUUYO_WORD + ")";
			}

			targetMap.put(Skf3050Sc002SharedService.GRID_NENGETSU, nengetsu);
			targetMap.put(Skf3050Sc002SharedService.GRID_SHIME_SHORI, setData.getShimeshori());
			targetMap.put(Skf3050Sc002SharedService.GRID_POSITIVE_DATA, setData.getPositiveRenkei());
			targetMap.put(Skf3050Sc002SharedService.GRID_HDN_SHORINENGETSU, setData.getSort());
			rtnList.add(targetMap);
		}

		return rtnList;
	}

	/**
	 * 給与月を編集する。
	 * 
	 * @param inYyyymm
	 *            年月
	 * @return 編集後の月
	 */
	private String editKyuyoMonth(String inYyyymm) {

		if (NfwStringUtils.isEmpty(inYyyymm)) {
			return inYyyymm;
		}

		int nextMonth = 0;
		String inMonth = inYyyymm.substring(4, 6);

		if ("12".equals(inMonth)) {
			nextMonth = MONTH_1;
		} else {
			nextMonth = Integer.parseInt(inMonth) + MONTH_1;
		}

		String outMonth = String.valueOf(nextMonth);

		return outMonth;
	}

	/**
	 * 実行指示予定箇所の強調表示データを取得する。
	 * 
	 * @param gridViewList
	 *            グリッドビューリスト
	 * @param sysDateYyyymm
	 *            システム処理年月
	 */
	public Skf3050Sc002CommonDto getJikkoushijiHighlightData(Skf3050Sc002CommonDto inDto, String sysDateYyyymm) {

		List<Map<String, Object>> gridViewList = inDto.getGetujiGrid();
		inDto.setHdnJikkouShijiYoteiNengetsu(sysDateYyyymm);
		inDto.setHdnJikkouShijiYoteiShoriIdx("");
		inDto.setHdnJikkouShijiYoteiShoriCol("");

		for (int i = 0; i < gridViewList.size(); i++) {
			Map<String, Object> gridViewData = gridViewList.get(i);

			if (Objects.equals(sysDateYyyymm, gridViewData.get(Skf3050Sc002SharedService.GRID_HDN_SHORINENGETSU))) {
				inDto.setHdnJikkouShijiYoteiShoriIdx(String.valueOf(i));

				if (!NENGETSU_LIST_SHIME_JIKKOUSUMI.equals(gridViewData.get(GRID_SHIME_SHORI))) {
					inDto.setHdnJikkouShijiYoteiShoriCol(GRID_SHIME_SHORI);
					break;

				} else {
					inDto.setHdnJikkouShijiYoteiShoriCol(GRID_POSITIVE_DATA);
					break;
				}
			}
		}

		return inDto;
	}

	/**
	 * ボタンの活性/非活性の状態を設定する。
	 * 
	 * @param inDto
	 *            インプットDto
	 * @return インプットDto
	 */
	public Skf3050Sc002CommonDto changeButtonStatus(Skf3050Sc002CommonDto inDto) {

		if (Skf3050Sc002SharedService.GRID_SHIME_SHORI.equals(inDto.getHdnJikkouShijiYoteiShoriCol())) {
			inDto = setButtonStatus(inDto, false, false, true, true, true);

		} else if (Skf3050Sc002SharedService.GRID_POSITIVE_DATA.equals(inDto.getHdnJikkouShijiYoteiShoriCol())) {
			Skf3050TMonthlyManageData renkeiData = getShimePositiveRenkeiKbn(inDto.getHdnJikkouShijiYoteiNengetsu());

			if (renkeiData != null
					&& CodeConstant.LINKDATA_CREATE_KBN_JIKKO_SUMI.equals(renkeiData.getLinkdataCreateKbn())) {
				inDto = setButtonStatus(inDto, true, true, false, false, false);

			} else {
				inDto = setButtonStatus(inDto, true, true, false, false, true);
			}

		} else {
			inDto = setButtonStatus(inDto, true, true, true, true, true);
		}

		return inDto;
	}

	/**
	 * ボタンの状態を設定するhiddenを設定する。
	 * 
	 * @param inDto
	 *            設定対象のDto
	 */
	private Skf3050Sc002CommonDto setButtonStatus(Skf3050Sc002CommonDto inDto, boolean kariKeisanDisabled,
			boolean shimeShoriDisabled, boolean renkeiDataSakuseiDisabled, boolean shimeKaijoDisabled,
			boolean renkeiDataKakuteiDisabled) {

		inDto.setHdnBtnKariKeisanDisabled(kariKeisanDisabled);
		inDto.setHdnBtnShimeShoriDisabled(shimeShoriDisabled);
		inDto.setHdnBtnRenkeiDataSakuseiDisabled(renkeiDataSakuseiDisabled);
		inDto.setHdnBtnShimeKaijoDisabled(shimeKaijoDisabled);
		inDto.setHdnBtnRenkeiDataKakuteiDisabled(renkeiDataKakuteiDisabled);

		return inDto;
	}

	/**
	 * 締め処理実行チェック
	 * 
	 * @param jikkouShijiYoteiNengetsu
	 *            実行指示予定年月
	 * @return エラーメッセージ
	 */
	public String checkShimeShori(String jikkouShijiYoteiNengetsu, String closeBatchFlg) {

		String rtnErrMsg = "";

		//▼二重起動チェック
		boolean notDoubleStartup = checkDoubleStartup();
		if (!notDoubleStartup) {
			rtnErrMsg = ERRMSG_DOUBLE_START;
			return rtnErrMsg;
		}

		//▼締め処理可能チェック
		Skf3050TMonthlyManageData renkeiKbnData = getShimePositiveRenkeiKbn(jikkouShijiYoteiNengetsu);
		if (renkeiKbnData == null) {
			//月次処理管理データが取得できない場合エラー
			rtnErrMsg = ERRMSG_SHIME_IMPOSSIBLE;
			return rtnErrMsg;

		} else {
			if (Skf3050Sc002SharedService.BILLING_ACT_KBN_JIKKO_SUMI.equals(renkeiKbnData.getBillingActKbn())
					|| CodeConstant.LINKDATA_CREATE_KBN_JIKKO_SUMI.equals(renkeiKbnData.getLinkdataCommitKbn())) {
				//締め処理＝実行済、または連携データ確定実行区分＝実行済の場合エラー
				rtnErrMsg = ERRMSG_SHIME_IMPOSSIBLE;
				return rtnErrMsg;
			}
		}

		//締め処理の場合はチェックを行う
		if (SHIME_SHORI_ON.equals(closeBatchFlg)) {

			//▼仮社員番号チェック
			List<String> kariShainNoList = getKariShainBango(jikkouShijiYoteiNengetsu);
			if (kariShainNoList.size() > 0) {
				//仮社員データが存在する場合エラー
				rtnErrMsg = ERRMSG_SHIME_KARISHAIN;
				return rtnErrMsg;
			}

			//▼社員番号変更対象者チェック
			//社員番号変更対象者の取得
			List<String> henkanShainBangoList = getHenkanShainBango(jikkouShijiYoteiNengetsu);
			if (henkanShainBangoList.size() > 0) {
				//社員番号変更対象者が存在する場合エラー
				rtnErrMsg = ERRMSG_SHIME_KARISHAIN_CHG;
				return rtnErrMsg;
			}

			//▼当日入居データ、社宅管理台帳基本テーブルデータ存在チェック
			List<String> teijiNyukyoList = getShatakuKanriNyukyoExists(jikkouShijiYoteiNengetsu);
			if (teijiNyukyoList.size() > 0) {
				//提示データテーブルの台帳作成区分≠作成済の場合エラー
				rtnErrMsg = ERRMSG_SHIME_SHATAKU_DAITYO_NYUKYO;
				return rtnErrMsg;
			}
			
			//▼提示データ未作成の駐車場利用申請データ存在チェック
			List<String> parkingTeijiMisakuseiDataList = getShatakuKanriParkingTeijiMisakuseiData(
					jikkouShijiYoteiNengetsu);
			if (parkingTeijiMisakuseiDataList.size() > 0) {
				//提示データが未作成の駐車場利用申請が存在する場合エラー
				rtnErrMsg = ERRMSG_SHIME_SHATAKU_DAITYO_PARKING_TEIJI_MISAKUSEI_DATA;
				return rtnErrMsg;
			}

			//▼当月駐車場利用開始データ、社宅管理台帳基本テーブルデータ存在チェック
			int parkingRiyoKaishiCount = 0;
			//① 駐車場のみ入居、かつ2台分利用の件数をカウント
			List<String> parking2RiyoKaishiList = getShatakuKanriParking2RiyoKaishiExist(jikkouShijiYoteiNengetsu);
			parkingRiyoKaishiCount += parking2RiyoKaishiList.size();
			//② 駐車場のみ入居、かつ1台分利用の件数をカウント
			int parking1RiyoKaishiCount = getShatakuKanriParking1RiyoKaishiCnt(jikkouShijiYoteiNengetsu);
			parkingRiyoKaishiCount += parking1RiyoKaishiCount;

			if (parkingRiyoKaishiCount > 0) {
				//処理年月内で未承認の駐車場貸出がある場合はエラー
				rtnErrMsg = ERRMSG_SHIME_SHATAKU_DAITYO_PARKING_RIYOKAISHI;
				return rtnErrMsg;
			}

			//▼当日退居データ、社宅管理台帳基本テーブルデータ存在チェック
			List<String> teijiTaikyoDataList = getShatakuKanriTaikyoExist(jikkouShijiYoteiNengetsu);
			if (teijiTaikyoDataList.size() > 0) {
				//提示データテーブルの台帳作成区分≠作成済の場合エラー
				rtnErrMsg = ERRMSG_SHIME_SHATAKU_DAITYO_TAIKYO;
				return rtnErrMsg;
			}

			//▼当月駐車場利用終了データ、社宅管理台帳基本テーブルデータ存在チェック
			List<String> parkingRiyoShuryoDataList = getShatakuKanriParkingRiyoShuryoExist(jikkouShijiYoteiNengetsu);
			if (parkingRiyoShuryoDataList.size() > 0) {
				//処理年月内で未承認の駐車場返却がある場合はエラー
				rtnErrMsg = ERRMSG_SHIME_SHATAKU_DAITYO_PARKING_RIYOSHURYO;
				return rtnErrMsg;
			}
		}

		return rtnErrMsg;
	}

	/**
	 * バッチの二重起動チェック
	 * 
	 * @return 結果
	 */
	public boolean checkDoubleStartup() {

		//締め処理バッチ二重起動チェック
		int shimeShoriBatchCnt = getBatchStartupCnt(closeBatchPrgId);
		if (shimeShoriBatchCnt > 0) {
			return false;
		}

		//締め解除バッチ二重起動チェック
		int shimeKaijoBatchCnt = getBatchStartupCnt(closeCanselBatchPrgId);
		if (shimeKaijoBatchCnt > 0) {
			return false;
		}

		//連携データ作成処理バッチ二重起動チェック
		int createPositiveDataBatchCnt = getBatchStartupCnt(createPositiveDataBatchPrgId);
		if (createPositiveDataBatchCnt > 0) {
			return false;
		}

		//連携データ確定処理バッチ二重起動チェック
		int confirmPositiveDataBatchCnt = getBatchStartupCnt(confirmPositiveDataBatchPrgId);
		if (confirmPositiveDataBatchCnt > 0) {
			return false;
		}

		//上記のバッチ全てが起動していない場合のみ正常とする
		return true;
	}

	/**
	 * バッチ起動件数を取得する。
	 * 
	 * @param batchPrgId
	 *            バッチプログラムID
	 * @return 起動件数
	 */
	private Integer getBatchStartupCnt(String batchPrgId) {

		Skf3050Sc002GetBatchStartupCntExpParameter param = new Skf3050Sc002GetBatchStartupCntExpParameter();
		param.setCompanyCd(CodeConstant.C001);
		param.setEndFlg(SkfCommonConstant.PROCESSING);
		param.setProgramId(batchPrgId);

		Integer rtnCnt = skf3050Sc002GetBatchStartupCntExpRepository.getBatchStartupCnt(param);

		return rtnCnt;
	}

	/**
	 * 仮社員番号を取得する。
	 * 
	 * @param taishouNendo
	 *            対象年度
	 * @return 仮社員番号データ
	 */
	public List<String> getKariShainBango(String taishouNendo) {

		Skf3050Sc002GetKariShainBangoExpParameter param = new Skf3050Sc002GetKariShainBangoExpParameter();
		param.setYearMonth(taishouNendo);
		param.setShainNo(KARI_SHAIN_NO_RECOG_CD);

		List<String> rtnData = skf3050Sc002GetKariShainBangoExpRepository.getKariShainBango(param);

		return rtnData;
	}

	/**
	 * 社員番号変換対象者情報を取得する。
	 * 
	 * @param taishoNengetsu
	 *            対象年月
	 * @return 社員番号変換対象者情報
	 */
	public List<String> getHenkanShainBango(String taishoNengetsu) {

		Skf3050Sc002GetHenkanShainBangoExpParameter param = new Skf3050Sc002GetHenkanShainBangoExpParameter();
		param.setTaishoNengetsu(taishoNengetsu);
		param.setShainNoChangeFlg(SHAIN_NO_CHG_AVAILABLE);

		List<String> rtnData = skf3050Sc002GetHenkanShainBangoExpRepository.getHenkanShainBango(param);

		return rtnData;
	}

	/**
	 * 入退去予定データを取得する。
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 入退去予定データ
	 */
	public List<String> getShatakuKanriNyukyoExists(String shoriNengetsu) {

		List<String> rtnData = skf3050Sc002GetShatakuKanriNyukyoExistExpRepository
				.getShatakuKanriNyukyoExist(shoriNengetsu);

		return rtnData;
	}

	/**
	 * 提示データ未作成駐車場申請データを取得する。
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 提示データ未作成駐車場申請データ
	 */
	public List<String> getShatakuKanriParkingTeijiMisakuseiData(String shoriNengetsu) {

		List<String> rtnData = skf3050Sc002GetShatakuKanriParkingTeijiMisakuseiDataExpRepository
				.getShatakuKanriParkingTeijiMisakuseiData(shoriNengetsu);

		return rtnData;
	}

	/**
	 * 提示データ当月駐車場利用開始データと社宅管理台帳基本データを取得する。
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 提示データ当月入居データ（駐車場2台希望）
	 */
	public List<String> getShatakuKanriParking2RiyoKaishiExist(String shoriNengetsu) {

		List<String> rtnData = skf3050Sc002GetShatakuKanriParking2RiyoKaishiExistExpRepository
				.getShatakuKanriParking2RiyoKaishiExist(shoriNengetsu);

		return rtnData;
	}

	/**
	 * 提示データ当月駐車場利用開始件数を取得する。 1台のみ駐車場追加利用の場合、既に利用中の区画が存在する可能性がある。
	 * そのため、処理年月の駐車場利用履歴を参照し、利用中の区画に対しては締め処理のチェックを行わないようにする。
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 提示データ当月駐車場利用開始件数
	 */
	public int getShatakuKanriParking1RiyoKaishiCnt(String shoriNengetsu) {

		int rtnCnt = 0;
		List<Skf3050Sc002GetShatakuKanriParking1RiyoKaishiExistExp> parkingRiyoKaishiDataList = getShatakuKanriParking1RiyoKaishiExist();

		for (Skf3050Sc002GetShatakuKanriParking1RiyoKaishiExistExp parkingRiyoKaishiData : parkingRiyoKaishiDataList) {
			long shatakuKanriId = 0L;

			if (parkingRiyoKaishiData.getShatakuKanriId() != null) {
				shatakuKanriId = parkingRiyoKaishiData.getShatakuKanriId();
			}

			boolean isCount = false;
			List<Skf3050Sc002GetParkingRirekiDataExp> parkingRirekiDataList = getParkingRirekiData(shoriNengetsu,
					shatakuKanriId);

			if (parkingRirekiDataList.size() > 0) {
				Long parkingKanriNo1 = parkingRirekiDataList.get(0).getParkingKanriNo1();
				Long parkingKanriNo2 = parkingRirekiDataList.get(0).getParkingKanriNo2();

				if (parkingKanriNo1 != null && parkingKanriNo2 == null) {
					isCount = isParkingTogetsuRiyoKaishi(parkingRiyoKaishiData, shoriNengetsu, PARKING_KUKAKU_2);

				} else if (parkingKanriNo1 == null && parkingKanriNo2 != null) {
					isCount = isParkingTogetsuRiyoKaishi(parkingRiyoKaishiData, shoriNengetsu, PARKING_KUKAKU_1);

				} else if (parkingKanriNo1 == null && parkingKanriNo2 == null) {
					isCount = isParkingTogetsuRiyoKaishi(parkingRiyoKaishiData, shoriNengetsu, PARKING_KUKAKU_0);
				}
			}

			if (isCount) {
				rtnCnt++;
			}
		}

		return rtnCnt;
	}

	/**
	 * 提示データ当月駐車場利用開始データと社宅管理台帳基本データを取得する。
	 * 
	 * @return 提示データ当月入居データ（駐車場1台希望）
	 */
	private List<Skf3050Sc002GetShatakuKanriParking1RiyoKaishiExistExp> getShatakuKanriParking1RiyoKaishiExist() {

		List<Skf3050Sc002GetShatakuKanriParking1RiyoKaishiExistExp> rtnData = skf3050Sc002GetShatakuKanriParking1RiyoKaishiExistExpRepository
				.getShatakuKanriParking1RiyoKaishiExist();

		return rtnData;
	}

	/**
	 * 駐車場履歴データを取得する。
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @param shatakuKanriId
	 *            社宅管理台帳ID
	 * @return 駐車場履歴データ
	 */
	private List<Skf3050Sc002GetParkingRirekiDataExp> getParkingRirekiData(String shoriNengetsu, Long shatakuKanriId) {

		Skf3050Sc002GetParkingRirekiDataExpParameter param = new Skf3050Sc002GetParkingRirekiDataExpParameter();
		param.setShatakuKanriId(shatakuKanriId);
		param.setYearMonth(shoriNengetsu);

		List<Skf3050Sc002GetParkingRirekiDataExp> rtnData = skf3050Sc002GetParkingRirekiDataExpRepository
				.getParkingRirekiData(param);

		return rtnData;
	}

	/**
	 * 新規割当の駐車場区画（未承認）の利用開始日が、処理年月内かどうかチェックする。
	 * 1台のみ駐車場追加利用の場合、既に利用中の区画が存在する可能性がある。
	 * そのため、処理年月の駐車場利用履歴を参照し、利用中の区画に対しては締め処理のチェックを行わないようにする。
	 * 
	 * @param parkingRiyoKaishiData
	 *            駐車場利用申請データ
	 * @param shoriNengetsu
	 *            処理年月
	 * @param parkingKukaku
	 *            新規割当をチェックするの駐車場区画
	 * @return true：当月利用(カウント対象)、false：別月利用(カウント対象外)
	 */
	private boolean isParkingTogetsuRiyoKaishi(
			Skf3050Sc002GetShatakuKanriParking1RiyoKaishiExistExp parkingRiyoKaishiData, String shoriNengetsu,
			int parkingKukaku) {

		boolean result = false;

		String parking1StartDate = parkingRiyoKaishiData.getParking1StartDate();
		String parking2StartDate = parkingRiyoKaishiData.getParking2StartDate();

		switch (parkingKukaku) {
		case PARKING_KUKAKU_1:
			// 区画１が新規割当
			if (!NfwStringUtils.isEmpty(parking1StartDate)) {
				if (Objects.equals(shoriNengetsu, parking1StartDate.substring(0, 6))) {
					// 区画１の利用開始年月が処理年月と等しい場合、未反映の当月利用としてカウント対象とする。
					result = true;
				}
			}
			break;

		case PARKING_KUKAKU_2:
			// 区画２が新規割当
			if (!NfwStringUtils.isEmpty(parking2StartDate)) {
				if (Objects.equals(shoriNengetsu, parking2StartDate.substring(0, 6))) {
					// 区画２の利用開始年月が処理年月と等しい場合、未反映の当月利用としてカウント対象とする。
					result = true;
				}
			}
			break;

		case PARKING_KUKAKU_0:
			if (CheckUtils.isEmpty(parking1StartDate) && CheckUtils.isEmpty(parking2StartDate)) {
				// 区画１、区画２の利用開始年月が両方とも未入力の場合、カウント対象外
				break;
			}

			if ((!CheckUtils.isEmpty(parking1StartDate) && (Objects.equals(shoriNengetsu, parking1StartDate.substring(0, 6))))
					|| 
				(!CheckUtils.isEmpty(parking2StartDate)	&& (Objects.equals(shoriNengetsu, parking2StartDate.substring(0, 6))))
				) {
				// 区画１、区画２の利用開始年月どちらかが処理年月と等しい場合、未反映の当月利用としてカウント対象とする。
				result = true;
			}
			break;
		}

		return result;
	}

	/**
	 * 提示データ当月退居データと社宅管理台帳基本データを取得する。
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 提示データ当月退去データ
	 */
	public List<String> getShatakuKanriTaikyoExist(String shoriNengetsu) {

		List<String> outData = skf3050Sc002GetShatakuKanriTaikyoExistExpRepository
				.getShatakuKanriTaikyoExist(shoriNengetsu);

		return outData;
	}

	/**
	 * 提示データ当月駐車場利用終了データと社宅管理台帳基本データを取得する。
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 提示データ当月駐車場利用終了データ
	 */
	public List<String> getShatakuKanriParkingRiyoShuryoExist(String shoriNengetsu) {

		List<String> outData = skf3050Sc002GetShatakuKanriParkingRiyoShuryoExistExpRepository
				.getShatakuKanriParkingRiyoShuryoExist(shoriNengetsu);

		return outData;
	}

	/**
	 * 備品貸与日を取得する。
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 備品貸与日
	 */
	public List<Skf3050Sc002GetBihinTaiyoDataExp> getBihinTaiyoData(String shoriNengetsu) {

		List<Skf3050Sc002GetBihinTaiyoDataExp> outData = skf3050Sc002GetBihinTaiyoDataExpRepository
				.getBihinTaiyoData(shoriNengetsu);

		return outData;
	}

	/**
	 * 備品返却日を取得する。
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 備品返却日
	 */
	public List<Skf3050Sc002GetBihinHenkyakuDataExp> getBihinHenkyakuData(String shoriNengetsu) {

		List<Skf3050Sc002GetBihinHenkyakuDataExp> rtnData = skf3050Sc002GetBihinHenkyakuDataExpRepository
				.getBihinHenkyakuData(shoriNengetsu);

		return rtnData;
	}

	/**
	 * バッチ制御テーブルを登録
	 * 
	 * @param parameter
	 *            パラメータ
	 * @param sysDate
	 *            システム処理日時
	 * @param endList
	 *            管理ログフッタ部の情報リスト
	 * @return 結果
	 * @throws ParseException
	 */
	@Transactional
	public int registCloseBatchControl(Map<String, String> parameter, Date sysDate, List<String> endList)
			throws ParseException {

		String retParameterName = checkParameter(parameter);
		String programId = closeBatchPrgId;

		if (!NfwStringUtils.isEmpty(retParameterName)) {

			if (!retParameterName.contains(BATCH_PARAM_NAME_COMPANY_CD)) {
				int retStat = insertBatchControl(parameter.get(COMPANY_CD_KEY), programId, parameter.get(USER_ID_KEY),
						SkfCommonConstant.ABNORMAL, sysDate, getSystemDate());

				if (retStat < 0) {
					outputManagementLogEndProc(endList, getSystemDate());
					return RETURN_STATUS_NG;
				}

				LogUtils.error(MessageIdConstant.E_SKF_1089, retParameterName);
				outputManagementLogEndProc(endList, null);
				return RETURN_STATUS_NG;

			} else {
				LogUtils.error(MessageIdConstant.E_SKF_1089, BATCH_PARAM_NAME_COMPANY_CD);
				return RETURN_STATUS_NG;
			}
		}

		if (!Objects.equals(closeBatchPrgId, parameter.get(BATCH_PRG_ID_KEY))) {

			int retStat = insertBatchControl(parameter.get(COMPANY_CD_KEY), programId, parameter.get(USER_ID_KEY),
					SkfCommonConstant.ABNORMAL, sysDate, getSystemDate());

			if (retStat < 0) {
				outputManagementLogEndProc(endList, null);
				return RETURN_STATUS_NG;
			}

			LogUtils.error(MessageIdConstant.E_SKF_1090, parameter.get(BATCH_PRG_ID_KEY));
			outputManagementLogEndProc(endList, getSystemDate());
			return RETURN_STATUS_NG;
		}

		int retStat = insertBatchControl(parameter.get(COMPANY_CD_KEY), programId, parameter.get(USER_ID_KEY),
				SkfCommonConstant.PROCESSING, sysDate, null);

		if (retStat < 0) {
			outputManagementLogEndProc(endList, null);
			return RETURN_STATUS_NG;
		}

		return RETURN_STATUS_OK;
	}

	/**
	 * 月別処理管理、月次処理管理を更新
	 * 
	 * @param parameter
	 *            パラメータ
	 * @param endList
	 *            出力ログリスト
	 * @return 処理結果Map
	 * @throws ParseException
	 */
	@Transactional
	public String updateTsukibetsuTsukiji(Map<String, String> parameter, List<String> endList) throws ParseException {

		String rtn = SkfCommonConstant.COMPLETE;
		String paramUserId = parameter.get(USER_ID_KEY);
		String paramShoriNengetsu = parameter.get(SHORI_NENGETSU_KEY);
		
		List<String> lockResult = skf3050Bt001GetDataForUpdateExpRepository.getSkf3030TShatakuRentalRirekiData(paramShoriNengetsu);
		if (lockResult.size() == 0) {
			return SkfCommonConstant.ABNORMAL;
		}
/* AS 締め処理性能向上 */
		List<String> lockBihinMeisaiResult = skf3050Bt001GetDataForUpdateExpRepository.getSkf3030TShatakuBihinRirekiData(paramShoriNengetsu);
		List<String> lockShozokuRirekiResult = skf3050Bt001GetDataForUpdateExpRepository.getSkf3030TShozokuRirekiData(paramShoriNengetsu);
/* AE 締め処理性能向上 */

		updateGenbutsuSanteigaku(paramUserId, paramShoriNengetsu);
		
		updateBihinGoukei(paramUserId, paramShoriNengetsu);

		List<Skf3050Bt001GetTsukibetuSyoryoRirekiDataJoinDataExp> renRirekiDtList = getTsukibetuSyoryoRirekiDataJoinData(
				paramShoriNengetsu);

		int shiyouUpdCount = 0;

		for (int i = 0; i < renRirekiDtList.size(); i++) {
			Skf3050Bt001GetTsukibetuSyoryoRirekiDataJoinDataExp renRirekiRow = renRirekiDtList.get(i);

			List<Skf3050Bt001GetShatakuKanriDaityoSogoriyoDataExp> sougoDt = getShatakuKanriDaityoSogoriyoData(
					renRirekiRow.getShatakuKanriId());

			if (sougoDt.size() <= 0) {
				LogUtils.error(MessageIdConstant.E_SKF_1106, ERRMSG_SHATAKUKANRIDAICHOU_0,
						ERRMSG_SHATAKUKANRIDAICHOU_1 + CodeConstant.COLON + renRirekiRow.getShatakuKanriId());
				rtn = SkfCommonConstant.ABNORMAL;
				break;
			}

			SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp shatakuRentCalcOutputData = getShatakuShiyoryoGetsugaku(
					paramShoriNengetsu, renRirekiRow, sougoDt.get(0).getRent());

			if (!NfwStringUtils.isEmpty(shatakuRentCalcOutputData.getErrMessage())) {
				LogUtils.errorByMsg(shatakuRentCalcOutputData.getErrMessage());
				rtn = SkfCommonConstant.ABNORMAL;
				break;
			}

			BigDecimal shatakuShiyouryouHiwari = getShatakuShiyoryoHiwari(paramShoriNengetsu,
					renRirekiRow.getNyukyoDate(), renRirekiRow.getTaikyoDate(),
					shatakuRentCalcOutputData.getShatakuShiyouryouGetsugaku());
			BigDecimal decimalRentalAdjust = BigDecimal.valueOf(renRirekiRow.getRentalAdjust());
			BigDecimal shatakuShiyouryouTyouseigo = shatakuShiyouryouHiwari.add(decimalRentalAdjust);

			BigDecimal chushajoShiyoryoGetsugaku1 = BigDecimal.ZERO;
			BigDecimal tyuushajouShiyouryouHiwari1 = BigDecimal.ZERO;

			List<Skf3050Bt001GetTsukibetuTyusyajyoRirekiDataExp> tsukiDt1List = getTsukibetuTyusyajyoRirekiData(
					renRirekiRow.getShatakuKanriId(), paramShoriNengetsu,
					Long.parseLong(CodeConstant.PARKING_LEND_NO_FIRST));

			if (tsukiDt1List.size() > 0) {
				SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp chushajoShiyoryoGetsugakuData = getChushajoShiyoryoGetsugaku(
						paramShoriNengetsu, renRirekiRow, sougoDt.get(0).getParkingRental(),
						tsukiDt1List.get(0).getParkingKanriNo());

				if (!NfwStringUtils.isEmpty(chushajoShiyoryoGetsugakuData.getErrMessage())) {
					LogUtils.errorByMsg(chushajoShiyoryoGetsugakuData.getErrMessage());
					rtn = SkfCommonConstant.ABNORMAL;
					break;
				}

				chushajoShiyoryoGetsugaku1 = chushajoShiyoryoGetsugakuData.getChushajouShiyoryou();
				tyuushajouShiyouryouHiwari1 = getChushajoShiyoryoHiwari(paramShoriNengetsu,
						tsukiDt1List.get(0).getParkingStartDate(), tsukiDt1List.get(0).getParkingEndDate(),
						chushajoShiyoryoGetsugaku1);
			}

			BigDecimal chushajoShiyoryoGetsugaku2 = BigDecimal.ZERO;
			BigDecimal tyuushajouShiyouryouHiwari2 = BigDecimal.ZERO;

			List<Skf3050Bt001GetTsukibetuTyusyajyoRirekiDataExp> tsukiDt2List = getTsukibetuTyusyajyoRirekiData(
					renRirekiRow.getShatakuKanriId(), paramShoriNengetsu,
					Long.parseLong(CodeConstant.PARKING_LEND_NO_SECOND));

			if (tsukiDt2List.size() > 0) {
				SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp chushajoShiyoryoGetsugakuData = getChushajoShiyoryoGetsugaku(
						paramShoriNengetsu, renRirekiRow, sougoDt.get(0).getParkingRental(),
						tsukiDt2List.get(0).getParkingKanriNo());

				if (!NfwStringUtils.isEmpty(chushajoShiyoryoGetsugakuData.getErrMessage())) {
					LogUtils.errorByMsg(chushajoShiyoryoGetsugakuData.getErrMessage());
					rtn = SkfCommonConstant.ABNORMAL;
					break;
				}

				chushajoShiyoryoGetsugaku2 = chushajoShiyoryoGetsugakuData.getChushajouShiyoryou();
				tyuushajouShiyouryouHiwari2 = getChushajoShiyoryoHiwari(paramShoriNengetsu,
						tsukiDt2List.get(0).getParkingStartDate(), tsukiDt2List.get(0).getParkingEndDate(),
						chushajoShiyoryoGetsugaku2);
			}

			List<Skf3050Bt001GetBihinMeisaiExp> bihinMeisaiDtList = getBihinMeisai(paramShoriNengetsu,
					renRirekiRow.getShatakuKanriId());
/* AS 締め処理性能向上 */
			if (bihinMeisaiDtList.size() > 0 && (lockBihinMeisaiResult.size() == 0 || lockShozokuRirekiResult.size() == 0)) {
				return SkfCommonConstant.ABNORMAL;
			}
/* AE 締め処理性能向上 */
			for (int j = 0; j < bihinMeisaiDtList.size(); j++) {
/* DS 締め処理性能向上 */
//				List<String> lockBihinMeisaiResult = skf3050Bt001GetDataForUpdateExpRepository.getSkf3030TShatakuBihinRirekiData(paramShoriNengetsu);
//				if (lockBihinMeisaiResult.size() == 0) {
//					return SkfCommonConstant.ABNORMAL;
//				}
/* DE 締め処理性能向上 */

				updateBihinMeisai(renRirekiRow.getShatakuKanriId(), bihinMeisaiDtList.get(j).getBihinPayment(),
						paramUserId, bihinMeisaiDtList.get(j).getBihinCd(), paramShoriNengetsu);

				BigDecimal bihinDt = getBihinGenbutsuShikyugokeigaku(paramShoriNengetsu,
						renRirekiRow.getShatakuKanriId());
				BigDecimal bihinGenbutsuShikyuugakuGoukei = BigDecimal.ZERO;

				if (bihinDt != null) {
					bihinGenbutsuShikyuugakuGoukei = bihinDt;
				}

				BigDecimal decimalParkingRentalAdjust = BigDecimal.valueOf(renRirekiRow.getParkingRentalAdjust());
				BigDecimal tyuushajouShiyouryouTyouseigo = tyuushajouShiyouryouHiwari1.add(tyuushajouShiyouryouHiwari2)
						.add(decimalParkingRentalAdjust);

				Integer kojinHutanKyoekihiGetsugakuTyouseigo = renRirekiRow.getKyoekihiPerson()
						+ renRirekiRow.getKyoekihiPersonAdjust();

				Integer shatakuShiyoryoGetsugaku = shatakuRentCalcOutputData.getShatakuShiyouryouGetsugaku().intValue();
				
				shiyouUpdCount += updateTsukibetsuSiyoryorirekiData(shatakuShiyoryoGetsugaku,
						shatakuShiyouryouHiwari.intValue(), shatakuShiyouryouTyouseigo.intValue(),
						chushajoShiyoryoGetsugaku1.intValue(), tyuushajouShiyouryouHiwari1.intValue(),
						chushajoShiyoryoGetsugaku2.intValue(), tyuushajouShiyouryouHiwari2.intValue(),
						tyuushajouShiyouryouTyouseigo.intValue(), bihinGenbutsuShikyuugakuGoukei.intValue(),
						kojinHutanKyoekihiGetsugakuTyouseigo, paramUserId, paramShoriNengetsu,
						renRirekiRow.getShatakuKanriId());

				String touJigyoCd = "";
				String touJigyoName = "";
				String companyCd = "";
				String agencyCd = "";
				String agencyName = "";
				String affilCd1 = "";
				String affilName1 = "";
				String affilCd2 = "";
				String affilName2 = "";

				String shoriNengetsuAddMonth = skfDateFormatUtils.addYearMonth(paramShoriNengetsu, 1);
				List<Skf3050Bt001GetShatakuSyainIdoRirekiDataExp> shainIdoDt1List = getShatakuSyainIdoRirekiDataSyutoku(
						shoriNengetsuAddMonth, CodeConstant.BEGINNING_END_KBN_MONTH_BEGIN,
						parameter.get(COMPANY_CD_KEY), renRirekiRow.getShainNo());

				if (shainIdoDt1List.size() > 0) {
					Skf3050Bt001GetShatakuSyainIdoRirekiDataExp shainIdoDt1 = shainIdoDt1List.get(0);

					if (!NfwStringUtils.isEmpty(shainIdoDt1.getRirekiBusinessAreaCd())) {
						touJigyoCd = shainIdoDt1.getRirekiBusinessAreaCd();
						touJigyoName = shainIdoDt1.getRirekiBusinessAreaName();
					} else {
						touJigyoCd = shainIdoDt1.getShainBusinessAreaCd();
						touJigyoName = shainIdoDt1.getShainBusinessAreaName();
					}

					if (NfwStringUtils.isEmpty(shainIdoDt1.getRirekiAgencyCd())
							&& NfwStringUtils.isEmpty(shainIdoDt1.getRirekiAffiliation1Cd())
							&& NfwStringUtils.isEmpty(shainIdoDt1.getRirekiAffiliation2Cd())) {
						List<Skf3050Bt001GetShainSoshikiDataExp> dtSoshikiList = getShainSoshikiData(
								parameter.get(COMPANY_CD_KEY), renRirekiRow.getShainNo());

						if (dtSoshikiList.size() > 0) {
							Skf3050Bt001GetShainSoshikiDataExp dtSoshiki = dtSoshikiList.get(0);
							companyCd = dtSoshiki.getOriginalCompanyCd();
							agencyCd = dtSoshiki.getSoshikiAgencyCd();
							agencyName = dtSoshiki.getSoshikiAgencyName();
							affilCd1 = dtSoshiki.getSoshikiAffiliation1Cd();
							affilName1 = dtSoshiki.getSoshikiAffiliation1Name();
							affilCd2 = dtSoshiki.getSoshikiAffiliation2Cd();
							affilName2 = dtSoshiki.getSoshikiAffiliation2Name();
						}

					} else {
						companyCd = shainIdoDt1.getCompanyCd();
						agencyCd = shainIdoDt1.getRirekiAgencyCd();
						agencyName = shainIdoDt1.getRirekiAgencyName();
						affilCd1 = shainIdoDt1.getRirekiAffiliation1Cd();
						affilName1 = shainIdoDt1.getRirekiAffiliation1Name();
						affilCd2 = shainIdoDt1.getRirekiAffiliation2Cd();
						affilName2 = shainIdoDt1.getRirekiAffiliation2Name();
					}
				}

				String preJigyoCd = "";
				String preJigyoName = "";

				List<Skf3050Bt001GetShatakuSyainIdoRirekiDataExp> shainIdoDt2List = getShatakuSyainIdoRirekiDataSyutoku(
						paramShoriNengetsu, CodeConstant.BEGINNING_END_KBN_MONTH_END, parameter.get(COMPANY_CD_KEY),
						renRirekiRow.getShainNo());

				if (shainIdoDt2List.size() > 0) {
					Skf3050Bt001GetShatakuSyainIdoRirekiDataExp shainIdoDt2 = shainIdoDt2List.get(0);

					if (!NfwStringUtils.isEmpty(shainIdoDt2.getRirekiBusinessAreaCd())) {
						preJigyoCd = shainIdoDt2.getRirekiBusinessAreaCd();
						preJigyoName = shainIdoDt2.getRirekiBusinessAreaName();
					} else {
						preJigyoCd = shainIdoDt2.getShainBusinessAreaCd();
						preJigyoName = shainIdoDt2.getShainBusinessAreaName();
					}
				}
/* DS 締め処理性能向上 */
//				List<String> lockShozokuRirekiResult = skf3050Bt001GetDataForUpdateExpRepository.getSkf3030TShozokuRirekiData(paramShoriNengetsu);
//				if (lockShozokuRirekiResult.size() == 0) {
//					return SkfCommonConstant.ABNORMAL;
//				}
/* DE 締め処理性能向上 */
				updateShozokuRireki(companyCd, agencyCd, agencyName, affilCd1, affilName1, affilCd2, affilName2,
						touJigyoCd, touJigyoName, preJigyoCd, preJigyoName, paramUserId,
						renRirekiRow.getShatakuKanriId(), paramShoriNengetsu);
			}
		}

		int genUpdCount = 0;
		List<Skf3050Bt001GetShatakiKanriDaityoDataTodoufukenDataExp> genSanteiDtList = getShatakiKanriDaityoDataTodoufukenData(
				paramShoriNengetsu);

		for (int j = 0; j < genSanteiDtList.size(); j++) {
			Skf3050Bt001GetShatakiKanriDaityoDataTodoufukenDataExp genSanteiRow = genSanteiDtList.get(j);

			if (NfwStringUtils.isEmpty(genSanteiRow.getEffectiveDate())) {
				String errMsg = ERRMSG_GENSHIKYU_TEKIYO_1 + CodeConstant.COLON + getGetsumatsujitu(paramShoriNengetsu)
						+ CodeConstant.COMMA + ERRMSG_GENSHIKYU_TODOFU_1 + CodeConstant.COLON
						+ genSanteiRow.getPrefCd();
				LogUtils.error(MessageIdConstant.E_SKF_1106, ERRMSG_GENSHIKYU_0, errMsg);
				rtn = SkfCommonConstant.ABNORMAL;
				break;
			}

			BigDecimal genSanMenseki = genSanteiRow.getMenseki().add(genSanteiRow.getBarnMenseki());
			genSanMenseki = genSanMenseki.setScale(0, RoundingMode.DOWN);

			BigDecimal genSanteiGaku = calcGenSanteiGaku(genSanMenseki, genSanteiRow);

			genUpdCount += updateTsukibetsuSiyoryorirekiGenbutsuSantei(genSanteiGaku.intValue(), paramUserId,
					paramShoriNengetsu, genSanteiRow.getShatakuKanriId());
		}

		List<Skf3050Bt001GetHrRenkeiDataSakuseiMaeSyoriDataExp> renkeiDataSakuseiMaeDataList = getHrRenkeiDataSakuseiMaeSyoriData(
				paramShoriNengetsu);

		for (int j = 0; j < renkeiDataSakuseiMaeDataList.size(); j++) {
			Skf3050Bt001GetHrRenkeiDataSakuseiMaeSyoriDataExp renkeiDataSakuseiMaeData = renkeiDataSakuseiMaeDataList
					.get(j);

			List<Skf3050Bt001GetShatakuKihonJyohoExp> shatakuKihonDtList = getShatakuKihonJyoho(
					renkeiDataSakuseiMaeData.getShatakuKanriNo());

			if (shatakuKihonDtList.size() <= 0) {
				LogUtils.error(MessageIdConstant.E_SKF_1106, ERRMSG_SHATAKUKIHON_0,
						ERRMSG_SHATAKUKIHON_1 + CodeConstant.COLON + renkeiDataSakuseiMaeData.getShatakuKanriNo());
				rtn = SkfCommonConstant.ABNORMAL;
				break;
			}

			Skf3050Bt001GetShatakuKihonJyohoExp shatakuKihonDt = shatakuKihonDtList.get(0);

			int shiyouryo = renkeiDataSakuseiMaeData.getRentalTotal()
					+ renkeiDataSakuseiMaeData.getParkingRentalTotal();
			Map<String, Object> ret1Map = getKeijouKamoku(shiyouryo, renkeiDataSakuseiMaeData.getPayCompanyCd(),
					shatakuKihonDt.getManegeCompanyCd(), renkeiDataSakuseiMaeData.getKariukeCompanyCd(),
					renkeiDataSakuseiMaeData.getMutualUseKbn(), shatakuKihonDt.getShatakuKbn(),
					CodeConstant.ACCOUNT_KBN_SHATAKU);

			int rtn1Sts = (int) ret1Map.get(KEIJOU_KAMOKU_RTN_STS_KEY);
			if (rtn1Sts == RETURN_STATUS_NG) {
				String msg = (String) ret1Map.get(KEIJOU_KAMOKU_ERR_MSG_KEY);
				LogUtils.error(MessageIdConstant.E_SKF_1106, ERRMSG_KAIKEI_0, msg);
				rtn = SkfCommonConstant.ABNORMAL;
				break;
			}

			Map<String, Object> ret2Map = getKeijouKamoku(renkeiDataSakuseiMaeData.getKyoekihiPersonTotal(),
					renkeiDataSakuseiMaeData.getPayCompanyCd(), shatakuKihonDt.getManegeCompanyCd(),
					renkeiDataSakuseiMaeData.getKariukeCompanyCd(), renkeiDataSakuseiMaeData.getMutualUseKbn(),
					shatakuKihonDt.getShatakuKbn(), CodeConstant.ACCOUNT_KBN_KYOEKI);

			int rtn2Sts = (int) ret2Map.get(KEIJOU_KAMOKU_RTN_STS_KEY);
			if (rtn2Sts == RETURN_STATUS_NG) {
				String msg = (String) ret2Map.get(KEIJOU_KAMOKU_ERR_MSG_KEY);
				LogUtils.error(MessageIdConstant.E_SKF_1106, ERRMSG_KAIKEI_0, msg);
				rtn = SkfCommonConstant.ABNORMAL;
				break;
			}

			String kanjouKamokuCdShataku = (String) ret1Map.get(KEIJOU_KAMOKU_ACNT_CD_KEY);
			String kanjouKamokuMeishouShataku = (String) ret1Map.get(KEIJOU_KAMOKU_ACNT_NM_KEY);
			String kanjouKamokuCdKojin = (String) ret2Map.get(KEIJOU_KAMOKU_ACNT_CD_KEY);
			String kanjouKamokuMeishouKojin = (String) ret2Map.get(KEIJOU_KAMOKU_ACNT_NM_KEY);
			
			updateTsukibetsuShiyoryoAccount(kanjouKamokuCdShataku, kanjouKamokuMeishouShataku, kanjouKamokuCdKojin,
					kanjouKamokuMeishouKojin, paramUserId, renkeiDataSakuseiMaeData.getShatakuKanriId(),
					paramShoriNengetsu);
		}

		if (SHIME_SHORI.equals(parameter.get(SHIME_SHORI_FLG))) {
			List<String> lockGetsujiShoriKanriResult = skf3050Bt001GetDataForUpdateExpRepository.getSkf3050TMonthlyManageData(paramShoriNengetsu);
			if (lockGetsujiShoriKanriResult.size() == 0) {
				return SkfCommonConstant.ABNORMAL;
			}
			
			updateGetsujiShoriKanriData(paramUserId, paramShoriNengetsu);
		}

		outputKanriInfoLog(String.valueOf(shiyouUpdCount), String.valueOf(genUpdCount));

		return rtn;
	}

	/**
	 * バッチ制御テーブルを更新
	 * 
	 * @param endFlag
	 *            終了フラグ
	 * @param companyCd
	 *            会社コード
	 * @param programId
	 *            プログラムID
	 * @param searchEndFlag
	 *            検索用終了フラグ
	 * @return 処理結果
	 * @throws ParseException
	 */
	@Transactional
	public int endShimeProc(String endFlag, String companyCd, String programId, String searchEndFlag)
			throws ParseException {

		int updateCnt = skfBatchBusinessLogicUtils.updateBatchControl(
							endFlag, companyCd, programId, searchEndFlag);

		if (updateCnt > 0) {
			return RETURN_STATUS_OK;
		}

		return RETURN_STATUS_NG;
	}

	/**
	 * ログ出力メッセージを編集する。 メッセージのフォーマットの文字列を置き換える。
	 *
	 * @param msgId
	 *            メッセージID
	 * @param replaceStrArry
	 *            置換文字列
	 * @return 編集後のメッセージ
	 */
	public String editMsg(String msgId, String[] replaceStrArry) {

		String msgFormat = PropertyUtils.getValue(msgId);
		String outMsg = msgFormat;

		for (int i = 0; i < replaceStrArry.length; i++) {
			String regex = "{" + i + "}";
			String replaceStr = replaceStrArry[i];
			outMsg = outMsg.replace(regex, replaceStr);
		}

		return outMsg;
	}

	/**
	 * パラメータ取得可否チェック
	 * 
	 * @param parameter
	 *            パラメータ
	 * @return エラー対象パラメータ名
	 */
	private String checkParameter(Map<String, String> parameter) {

		String retParameterName = "";

		if (isParamEmpty(parameter.get(BATCH_PRG_ID_KEY))) {
			retParameterName = BATCH_PARAM_NAME_PRG_ID;
		}

		if (isParamEmpty(parameter.get(COMPANY_CD_KEY))) {
			retParameterName += CodeConstant.COMMA + BATCH_PARAM_NAME_COMPANY_CD;
		}

		if (isParamEmpty(parameter.get(USER_ID_KEY))) {
			retParameterName += CodeConstant.COMMA + BATCH_PARAM_NAME_USER_ID;
		}

		if (isParamEmpty(parameter.get(SHORI_NENGETSU_KEY))) {
			retParameterName += CodeConstant.COMMA + BATCH_PARAM_NAME_SHORI_NENGETSU;
		}

		if (parameter.size() >= SHIME_SHORI_PARAMETER_NUM) {
			if (isParamEmpty(parameter.get(SHIME_SHORI_FLG))) {
				retParameterName += CodeConstant.COMMA + BATCH_PARAM_NAME_SHIME_SHORI_FLG;
			}
		}

		if (retParameterName.startsWith(CodeConstant.COMMA)) {
			retParameterName = retParameterName.substring(1);
		}

		return retParameterName;
	}

	/**
	 * パラメータが空であるかチェックする。
	 * 
	 * @param inParam
	 *            チェックパラメータ
	 * @return 結果
	 */
	private boolean isParamEmpty(String inParam) {

		if (NfwStringUtils.isEmpty(inParam)) {
			return true;
		}

		String param = inParam.replaceFirst("^[\\h]+", "").replaceFirst("[\\h]+$", "");

		if (NfwStringUtils.isEmpty(param)) {
			return true;
		}

		return false;
	}

	/**
	 * 管理ログの終了処理
	 * 
	 * @param kanriInfoList
	 *            管理情報リスト
	 */
	public void outputManagementLogEndProc(List<String> kanriInfoList, Date sysDate) {

		String strSysDate = CodeConstant.HYPHEN;
		if (sysDate != null) {
			strSysDate = sysDate.toString();
		}

		LogUtils.infoByMsg("処理終了時間：" + strSysDate);

		for (int i = 0; i < kanriInfoList.size(); i++) {
			LogUtils.infoByMsg(kanriInfoList.get(i));
		}
	}

	/**
	 * バッチ制御テーブルを登録する。
	 * 
	 * @param companyCd
	 *            会社コード
	 * @param programId
	 *            プログラムID
	 * @param userId
	 *            ユーザID
	 * @param endFlg
	 *            終了フラグ
	 * @param startDate
	 *            開始日時
	 * @param endDate
	 *            終了日時
	 * @return 登録件数
	 * @throws ParseException
	 */
	private int insertBatchControl(String companyCd, String programId, String userId, String endFlg, Date startDate,
			Date endDate) throws ParseException {

		int insertCount = skfBatchBusinessLogicUtils.insertBatchControl(companyCd, programId, userId, endFlg, startDate,
				endDate);

		return insertCount;
	}

	/**
	 * 月別使用料履歴更新（現物算定額）
	 * 
	 * @param updateUser
	 *            更新ユーザ
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 更新件数
	 */
	private void updateGenbutsuSanteigaku(String updateUser, String shoriNengetsu) {

		Skf3050Bt001UpdateGenbutsuSanteigakuExpParameter param = new Skf3050Bt001UpdateGenbutsuSanteigakuExpParameter();
		param.setUpdateUserId(updateUser);
		param.setYearMonth(shoriNengetsu);
		param.setUpdateProgramId(closeBatchPrgId);

		skf3050Bt001UpdateGenbutsuSanteigakuExpRepository.updateGenbutsuSanteigaku(param);
	}

	/**
	 * 月別使用料履歴更新（備品現物支給合計額）
	 * 
	 * @param updateUser
	 *            更新ユーザ
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 更新件数
	 */
	private void updateBihinGoukei(String updateUser, String shoriNengetsu) {

		Skf3050Bt001UpdateBihinGoukeiExpParameter param = new Skf3050Bt001UpdateBihinGoukeiExpParameter();
		param.setUpdateUserId(updateUser);
		param.setYearMonth(shoriNengetsu);
		param.setUpdateProgramId(closeBatchPrgId);

		skf3050Bt001UpdateBihinGoukeiExpRepository.updateBihinGoukei(param);
	}

	/**
	 * 月別使用料履歴テーブル結合データの社宅使用料月額データ取得
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 月別使用料履歴テーブル結合データ
	 */
	private List<Skf3050Bt001GetTsukibetuSyoryoRirekiDataJoinDataExp> getTsukibetuSyoryoRirekiDataJoinData(
			String shoriNengetsu) {

		Skf3050Bt001GetTsukibetuSyoryoRirekiDataJoinDataExpParameter param = new Skf3050Bt001GetTsukibetuSyoryoRirekiDataJoinDataExpParameter();
		param.setShoriNengetsu(shoriNengetsu);

		List<Skf3050Bt001GetTsukibetuSyoryoRirekiDataJoinDataExp> outData = skf3050Bt001GetTsukibetuSyoryoRirekiDataJoinDataExpRepository
				.getTsukibetuSyoryoRirekiDataJoinData(param);

		return outData;
	}

	/**
	 * 管理ログを出力する。
	 * 
	 * @param shiyouUpdCount
	 *            月別使用料履歴テーブル（社宅使用料、駐車場使用料）更新カウンタ
	 * @param genUpdCount
	 *            月別使用料履歴テーブル（現物算定額）更新カウンタ
	 */
	private void outputKanriInfoLog(String shiyouUpdCount, String genUpdCount) {
		LogUtils.info(MessageIdConstant.I_SKF_1030, ERRMSG_PARAM_SAI_0, shiyouUpdCount);
		LogUtils.info(MessageIdConstant.I_SKF_1030, ERRMSG_PARAM_GEN_0, genUpdCount);
	}

	/**
	 * 社宅管理台帳相互利用基本テーブルのデータ取得
	 * 
	 * @param shatakuKanriDaityouId
	 *            社宅管理台帳ID
	 * @return 社宅管理台帳相互利用基本テーブルデータ
	 */
	private List<Skf3050Bt001GetShatakuKanriDaityoSogoriyoDataExp> getShatakuKanriDaityoSogoriyoData(
			long shatakuKanriDaityouId) {

		List<Skf3050Bt001GetShatakuKanriDaityoSogoriyoDataExp> outData = skf3050Bt001GetShatakuKanriDaityoSogoriyoDataExpRepository
				.getShatakuKanriDaityoSogoriyoData(shatakuKanriDaityouId);

		return outData;
	}

	/**
	 * 社宅使用料月額取得
	 * 
	 * @param shoriNengetsu
	 *            処理年月(yyyyMM)
	 * @param renRirekiRow
	 *            月別使用料履歴レコード
	 * @param shatakuChintairyo
	 *            社宅賃貸料
	 * @return 社宅使用料計算結果保持Entity
	 * @throws ParseException
	 */
	private SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp getShatakuShiyoryoGetsugaku(String shoriNengetsu,
			Skf3050Bt001GetTsukibetuSyoryoRirekiDataJoinDataExp renRirekiRow, int shatakuChintairyo)
			throws ParseException {

		String birthDay = "";

		if (renRirekiRow.getBirthdayYear() != 0) {
			birthDay = String.format("%04d", renRirekiRow.getBirthdayYear());
			birthDay += String.format("%02d", renRirekiRow.getBirthdayMonth());
			birthDay += String.format("%02d", renRirekiRow.getBirthdayDay());

		} else {
			birthDay = "";
		}

		SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp param = new SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp();
		param.setShoriNengetsu(shoriNengetsu);
		param.setShoriKbn(SHIYORYO_GETSUGAKU_SHORI_KBN);
		param.setShiyouryouPatternId(renRirekiRow.getRentalPatternId().toString());
		param.setYakuinKbn(renRirekiRow.getYakuinSannteiKbn());
		param.setShatakuChintairyou(String.valueOf(shatakuChintairyo));
		param.setSeinengappi(birthDay);

		SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp outData = skfBaseBusinessLogicUtils
				.getShatakuShiyouryouKeisan(param);

		return outData;
	}

	/**
	 * 社宅使用料日割額算出
	 * 
	 * @param shoriNengetsu
	 *            処理年月(YYYYMM)
	 * @param nyuukyoDate
	 *            入居日(YYYYMMDD)
	 * @param taikyoDate
	 *            退去日(YYYYMMDD)
	 * @param shatakuRiyouryouGetsugaku
	 *            社宅使用料月額
	 * @return 社宅使用料日割額
	 */
	private BigDecimal getShatakuShiyoryoHiwari(String shoriNengetsu, String nyuukyoDate, String taikyoDate,
			BigDecimal shatakuRiyouryouGetsugaku) {

		String shoriNengetsuShonichi = shoriNengetsu + "01";

		BigDecimal decimalNyuukyoDate = new BigDecimal(nyuukyoDate);
		BigDecimal decimalShoriNengetsuShonichi = new BigDecimal(shoriNengetsuShonichi);

		if (decimalNyuukyoDate.compareTo(decimalShoriNengetsuShonichi) < 0) {
			nyuukyoDate = shoriNengetsuShonichi;
		}

		String shoriNengetsuMatsujitsu = getGetsumatsujitu(shoriNengetsu);
		BigDecimal decimalShoriNengetsuMatsujitsu = new BigDecimal(shoriNengetsuMatsujitsu);

		if (NfwStringUtils.isEmpty(taikyoDate)) {
			taikyoDate = shoriNengetsuMatsujitsu;

		} else {
			BigDecimal decimalTaikyoDate = new BigDecimal(taikyoDate);
			if (decimalTaikyoDate.compareTo(decimalShoriNengetsuMatsujitsu) > 0) {
				taikyoDate = shoriNengetsuMatsujitsu;
			}
		}

		String nextShoriNengetsu = skfDateFormatUtils.addYearMonth(shoriNengetsu, 1);
		BigDecimal decimalNextShoriNengetsu = new BigDecimal(nextShoriNengetsu);
		BigDecimal decimalNyuukyoYymm = new BigDecimal(nyuukyoDate.substring(0, 6));
		BigDecimal taikyoDateYymm = new BigDecimal(taikyoDate.substring(0, 6));
		String beforeShoriNengetsu = skfDateFormatUtils.addYearMonth(shoriNengetsu, -1);
		BigDecimal decimalBeforeShoriNengetsu = new BigDecimal(beforeShoriNengetsu);

		BigDecimal outDate = BigDecimal.ZERO;

		if (Objects.equals(nyuukyoDate, shoriNengetsuShonichi) && Objects.equals(shoriNengetsuMatsujitsu, taikyoDate)) {
			outDate = shatakuRiyouryouGetsugaku;

		} else if (decimalNextShoriNengetsu.compareTo(decimalNyuukyoYymm) <= 0
				|| taikyoDateYymm.compareTo(decimalBeforeShoriNengetsu) <= 0) {
			outDate = BigDecimal.ZERO;

		} else if (!Objects.equals(nyuukyoDate, shoriNengetsuShonichi) || !Objects.equals(taikyoDate, shoriNengetsuMatsujitsu)) {
			BigDecimal taikyoDateDd = new BigDecimal(taikyoDate.substring(6, 8));
			BigDecimal nyuukyoDateDd = new BigDecimal(nyuukyoDate.substring(6, 8));
			BigDecimal shoriNengetsuMatsujitsuDd = new BigDecimal(shoriNengetsuMatsujitsu.substring(6, 8));

			outDate = shatakuRiyouryouGetsugaku.multiply(taikyoDateDd).subtract(nyuukyoDateDd)
					.divide(shoriNengetsuMatsujitsuDd, 0, RoundingMode.DOWN);
		}

		outDate = outDate.setScale(0, RoundingMode.DOWN);

		return outDate;
	}

	/**
	 * 月末日取得
	 * 
	 * @param nengetsu
	 *            処理年月(yyyyMM)
	 * @return 月末日(yyyyMMdd)
	 */
	private String getGetsumatsujitu(String nengetsu) {

		if (NfwStringUtils.isEmpty(nengetsu)) {
			return nengetsu;
		}

		Date dtEnd = skfDateFormatUtils.formatStringToDate(nengetsu + "01");

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dtEnd);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);

		Date changedDate = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat(SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT);

		String outDate = sdf.format(changedDate);

		return outDate;
	}
	
	/**
	 * 対象月翌月の末日取得
	 * 
	 * @param nengetsu
	 *            処理年月(yyyyMM)
	 * @return 対象月末日(yyyyMMdd)
	 */
	private String getTaishoutsukiYokugetsuMatsu(String nengetsu) {

		if (NfwStringUtils.isEmpty(nengetsu)) {
			return nengetsu;
		}

		Date dtEnd = skfDateFormatUtils.formatStringToDate(nengetsu + "01");

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dtEnd);
		calendar.add(Calendar.MONTH, 2);
		calendar.add(Calendar.DAY_OF_MONTH, -1);

		Date changedDate = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat(SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT);

		String outDate = sdf.format(changedDate);

		return outDate;
	}

	/**
	 * 月別駐車場履歴テーブルの駐車場管理番号データ取得
	 * 
	 * @param shatakuKanriDaityouId
	 *            社宅管理台帳ID
	 * @param shoriNengetsu
	 *            処理年月
	 * @param taiyoBangou
	 *            貸与番号
	 * @return 月別駐車場履歴テーブル結合データ
	 */
	private List<Skf3050Bt001GetTsukibetuTyusyajyoRirekiDataExp> getTsukibetuTyusyajyoRirekiData(
			Long shatakuKanriDaityouId, String shoriNengetsu, Long taiyoBangou) {

		Skf3050Bt001GetTsukibetuTyusyajyoRirekiDataExpParameter param = new Skf3050Bt001GetTsukibetuTyusyajyoRirekiDataExpParameter();
		param.setShatakuKanriId(shatakuKanriDaityouId);
		param.setParkingLendNo(taiyoBangou);
		param.setYearMonth(shoriNengetsu);

		List<Skf3050Bt001GetTsukibetuTyusyajyoRirekiDataExp> outData = skf3050Bt001GetTsukibetuTyusyajyoRirekiDataExpRepository
				.getTsukibetuTyusyajyoRirekiData(param);

		return outData;
	}

	/**
	 * 駐車場使用料月額取得
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @param renRirekiRow
	 *            月別使用料履歴レコード
	 * @param chushajoChintairyo
	 *            駐車場賃貸料
	 * @param chushajoKanriBangou
	 *            駐車場管理番号
	 * @return 駐車場使用料月額
	 * @throws ParseException
	 */
	private SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp getChushajoShiyoryoGetsugaku(String shoriNengetsu,
			Skf3050Bt001GetTsukibetuSyoryoRirekiDataJoinDataExp renRirekiRow, Integer chushajoChintairyo,
			Long chushajoKanriBangou) throws ParseException {

		SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp param = new SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp();
		param.setShoriNengetsu(shoriNengetsu);
		param.setShoriKbn(CHUSHAJO_SHIYORYO_GETSUGAKU_SHORI_KBN);
		param.setYakuinKbn(renRirekiRow.getYakuinSannteiKbn());
		param.setChyshajoChintairyou(String.valueOf(chushajoChintairyo));
		param.setShatakuKanriBangou(String.valueOf(renRirekiRow.getShatakuKanriNo()));
		param.setChushajoKanriBangou(String.valueOf(chushajoKanriBangou));
		param.setAuseKbn(renRirekiRow.getAuse());

		SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp outData = skfBaseBusinessLogicUtils
				.getShatakuShiyouryouKeisan(param);

		return outData;
	}

	/**
	 * 駐車場使用料日割額算出
	 * 
	 * @param shoriNengetsu
	 *            処理年月(YYYYMM)
	 * @param parkingStartDate
	 *            駐車場開始日
	 * @param parkingEndDate
	 *            駐車場返却日
	 * @param tyuushajouShiyouryouGetsugaku
	 *            駐車場使用料月額
	 * @return 駐車場使用料日割額
	 */
	private BigDecimal getChushajoShiyoryoHiwari(String shoriNengetsu, String parkingStartDate, String parkingEndDate,
			BigDecimal tyuushajouShiyouryouGetsugaku) {

		String shoriNengetsuShonichi = shoriNengetsu + "01";

		BigDecimal decimalParkingStartDate = new BigDecimal(parkingStartDate);
		BigDecimal decimalShoriNengetsuShonichi = new BigDecimal(shoriNengetsuShonichi);

		if (decimalParkingStartDate.compareTo(decimalShoriNengetsuShonichi) < 0) {
			parkingStartDate = shoriNengetsuShonichi;
		}

		BigDecimal decimalParkingEndDate = BigDecimal.ZERO;
		String shoriNengetsuMatsujitsu = getGetsumatsujitu(shoriNengetsu);
		BigDecimal decimalShoriNengetsuMatsujitsu = new BigDecimal(shoriNengetsuMatsujitsu);

		if (NfwStringUtils.isEmpty(parkingEndDate)) {
			parkingEndDate = shoriNengetsuMatsujitsu;
		} else {
			if (decimalParkingEndDate.compareTo(decimalShoriNengetsuMatsujitsu) > 0) {
				parkingEndDate = shoriNengetsuMatsujitsu;
			}
		}

		String nextShoriNengetsu = skfDateFormatUtils.addYearMonth(shoriNengetsu, 1);
		BigDecimal decimalNextShoriNengetsu = new BigDecimal(nextShoriNengetsu);
		BigDecimal parkingStartDateYymm = new BigDecimal(parkingStartDate.substring(0, 6));
		BigDecimal parkingEndDateYymm = new BigDecimal(parkingEndDate.substring(0, 6));
		String beforeShoriNengetsu = skfDateFormatUtils.addYearMonth(shoriNengetsu, -1);
		BigDecimal decimalBeforeShoriNengetsu = new BigDecimal(beforeShoriNengetsu);

		BigDecimal outDate = BigDecimal.ZERO;

		if (Objects.equals(parkingStartDate, shoriNengetsuShonichi) && Objects.equals(shoriNengetsuMatsujitsu, parkingEndDate)) {
			outDate = tyuushajouShiyouryouGetsugaku;

		} else if (decimalNextShoriNengetsu.compareTo(parkingStartDateYymm) <= 0
				|| parkingEndDateYymm.compareTo(decimalBeforeShoriNengetsu) <= 0) {
			outDate = BigDecimal.ZERO;

		} else if (!Objects.equals(parkingStartDate, shoriNengetsuShonichi) || !Objects.equals(parkingEndDate, shoriNengetsuMatsujitsu)) {
			BigDecimal parkingEndDateDd = new BigDecimal(parkingEndDate.substring(6, 8));
			BigDecimal parkingStartDateDd = new BigDecimal(parkingStartDate.substring(6, 8));
			BigDecimal shoriNengetsuMatsujitsuDd = new BigDecimal(shoriNengetsuMatsujitsu.substring(6, 8));

			outDate = tyuushajouShiyouryouGetsugaku.multiply(parkingEndDateDd)
					.subtract((parkingStartDateDd.add(BigDecimal.ONE)))
					.divide(shoriNengetsuMatsujitsuDd, 0, RoundingMode.DOWN);
		}

		outDate = outDate.setScale(0, RoundingMode.DOWN);

		return outDate;
	}

	/**
	 * 月別備品使用料明細テーブル取得
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @param shatakuDaichoId
	 *            社宅管理台帳ID
	 * @return 月別備品使用料明細データテーブル
	 */
	private List<Skf3050Bt001GetBihinMeisaiExp> getBihinMeisai(String shoriNengetsu, Long shatakuDaichoId) {

		Skf3050Bt001GetBihinMeisaiExpParameter param = new Skf3050Bt001GetBihinMeisaiExpParameter();
		param.setYearMonth(shoriNengetsu);
		param.setShatakuKanriId(shatakuDaichoId);

		List<Skf3050Bt001GetBihinMeisaiExp> outData = skf3050Bt001GetBihinMeisaiExpRepository.getBihinMeisai(param);

		return outData;
	}

	/**
	 * 月別備品使用料明細の備品現物額更新
	 * 
	 * @param shatakuDaichoId
	 *            社宅管理台帳ID
	 * @param bihinGenbutsuGaku
	 *            備品現物支給額
	 * @param userId
	 *            更新ユーザID
	 * @param bihinCd
	 *            備品コード
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 更新件数
	 */
	private void updateBihinMeisai(Long shatakuDaichoId, Integer bihinGenbutsuGaku, String userId, String bihinCd,
			String shoriNengetsu) {

		Skf3050Bt001UpdateBihinMeisaiExpParameter param = new Skf3050Bt001UpdateBihinMeisaiExpParameter();

		if (bihinGenbutsuGaku == null) {
			param.setBihinGenbutsuGaku(null);
		} else {
			param.setBihinGenbutsuGaku(bihinGenbutsuGaku);
		}

		if (NfwStringUtils.isEmpty(userId)) {
			param.setUpdateUser(null);
		} else {
			param.setUpdateUser(userId);
		}

		param.setShatakuKanriId(shatakuDaichoId);
		param.setYearMonth(shoriNengetsu);
		param.setBihinCd(bihinCd);
		param.setUpdateProgramId(closeBatchPrgId);

		skf3050Bt001UpdateBihinMeisaiExpRepository.updateBihinMeisai(param);

	}

	/**
	 * 備品現物支給額合計算出データ取得（会社保有、レンタル、備付）
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @param shataKanriId
	 *            社宅管理ID
	 * @return 備品現物支給額算出データ
	 */
	private BigDecimal getBihinGenbutsuShikyugokeigaku(String shoriNengetsu, Long shataKanriId) {

		Skf3050Bt001GetBihinGenbutsuShikyugokeigakuExpParameter param = new Skf3050Bt001GetBihinGenbutsuShikyugokeigakuExpParameter();

		if (shataKanriId == null) {
			param.setShatakuKanriId(null);
		} else {
			param.setShatakuKanriId(shataKanriId);
		}

		if (NfwStringUtils.isEmpty(shoriNengetsu)) {
			param.setYearMonth(null);
		} else {
			param.setYearMonth(shoriNengetsu);
		}

		BigDecimal outData = skf3050Bt001GetBihinGenbutsuShikyugokeigakuExpRepository
				.getBihinGenbutsuShikyugokeigaku(param);

		return outData;
	}

	/**
	 * 月別使用料履歴テーブルのデータ更新
	 * 
	 * @param rentalMonth
	 *            社宅使用料月額
	 * @param rentalDay
	 *            社宅使用料日割
	 * @param rentalTotal
	 *            社宅使用料月額（調整後）
	 * @param parking1RentalMonth
	 *            駐車場区画1使用料月額
	 * @param parking1RentalDay
	 *            駐車場区画1使用料日割
	 * @param parking2RentalMonth
	 *            駐車場区画2使用料月額
	 * @param parking2RentalDay
	 *            駐車場区画2使用料日割
	 * @param parkingRentalTotal
	 *            駐車場使用料月額（調整後）
	 * @param bihinGenbutuGoukei
	 *            備品現物支給合計額
	 * @param updateUser
	 *            更新ユーザ
	 * @param shoriNengetsu
	 *            処理年月
	 * @param shatakuKanriBangou
	 *            社宅管理台帳ID
	 */
	private Integer updateTsukibetsuSiyoryorirekiData(Integer rentalMonth, Integer rentalDay, Integer rentalTotal,
			Integer parking1RentalMonth, Integer parking1RentalDay, Integer parking2RentalMonth,
			Integer parking2RentalDay, Integer parkingRentalTotal, Integer bihinGenbutuGoukei,
			Integer kojinHutanKyoekiTotal, String updateUser, String shoriNengetsu, Long shatakuKanriBangou) {

		Skf3050Bt001UpdateTsukibetsuSiyoryorirekiDataExpParameter param = new Skf3050Bt001UpdateTsukibetsuSiyoryorirekiDataExpParameter();

		if (rentalMonth == null) {
			param.setRentalMonth(null);
		} else {
			param.setRentalMonth(rentalMonth);
		}

		if (rentalDay == null) {
			param.setRentalDay(null);
		} else {
			param.setRentalDay(rentalDay);
		}

		if (rentalTotal == null) {
			param.setRentalTotal(null);
		} else {
			param.setRentalTotal(rentalTotal);
		}

		if (parking1RentalMonth == null) {
			param.setParking1RentalMonth(null);
		} else {
			param.setParking1RentalMonth(parking1RentalMonth);
		}

		if (parking1RentalDay == null) {
			param.setParking1RentalDay(null);
		} else {
			param.setParking1RentalDay(parking1RentalDay);
		}

		if (parking2RentalMonth == null) {
			param.setParking2RentalMonth(null);
		} else {
			param.setParking2RentalMonth(parking2RentalMonth);
		}

		if (parking2RentalDay == null) {
			param.setParking2RentalDay(null);
		} else {
			param.setParking2RentalDay(parking2RentalDay);
		}

		if (parkingRentalTotal == null) {
			param.setParkingRentalTotal(null);
		} else {
			param.setParkingRentalTotal(parkingRentalTotal);
		}

		if (bihinGenbutuGoukei == null) {
			param.setBihinGenbutuGoukei(null);
		} else {
			param.setBihinGenbutuGoukei(bihinGenbutuGoukei);
		}

		if (kojinHutanKyoekiTotal == null) {
			param.setKyoekihiPersonTotal(null);
		} else {
			param.setKyoekihiPersonTotal(kojinHutanKyoekiTotal);
		}

		if (updateUser == null) {
			param.setUpdateUser(null);
		} else {
			param.setUpdateUser(updateUser);
		}

		param.setYearMonth(shoriNengetsu);
		param.setShatakuKanriId(shatakuKanriBangou);
		param.setUpdateProgramId(closeBatchPrgId);

		Integer outUpdateCnt = skf3050Bt001UpdateTsukibetsuSiyoryorirekiDataExpRepository
				.updateTsukibetsuSiyoryorirekiData(param);

		return outUpdateCnt;
	}

	/**
	 * 社宅社員異動履歴テーブルのデータ取得
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @param begEndKbn
	 *            月初月末区分
	 * @param companyCd
	 *            会社コード
	 * @param shainBangou
	 *            社員番号
	 * @return 社宅社員異動履歴テーブル
	 */
	private List<Skf3050Bt001GetShatakuSyainIdoRirekiDataExp> getShatakuSyainIdoRirekiDataSyutoku(String shoriNengetsu,
			String begEndKbn, String companyCd, String shainBangou) {

		Skf3050Bt001GetShatakuSyainIdoRirekiDataExpParameter param = new Skf3050Bt001GetShatakuSyainIdoRirekiDataExpParameter();
		param.setYearMonth(shoriNengetsu);
		param.setBeginningEndKbn(begEndKbn);
		param.setCompanyCd(companyCd);
		param.setShainNo(shainBangou);

		List<Skf3050Bt001GetShatakuSyainIdoRirekiDataExp> outData = skf3050Bt001GetShatakuSyainIdoRirekiDataExpRepository
				.getShatakuSyainIdoRirekiData(param);

		return outData;
	}

	/**
	 * 組織マスタ情報取得
	 * 
	 * @param companyCd
	 *            会社コード
	 * @param shainBangou
	 *            社員番号
	 * @return 組織マスタテーブル
	 */
	private List<Skf3050Bt001GetShainSoshikiDataExp> getShainSoshikiData(String companyCd, String shainBangou) {

		Skf3050Bt001GetShainSoshikiDataExpParameter param = new Skf3050Bt001GetShainSoshikiDataExpParameter();
		param.setCompanyCd(companyCd);
		param.setShainNo(shainBangou);

		List<Skf3050Bt001GetShainSoshikiDataExp> outData = skf3050Bt001GetShainSoshikiDataExpRepository
				.getShainSoshikiData(param);

		return outData;
	}

	/**
	 * 月別所属情報履歴の当月事業領域、前月事業領域の更新
	 * 
	 * @param companyCd
	 *            会社コード
	 * @param agencyCd
	 *            機関コード
	 * @param agencyName
	 *            機関名称
	 * @param affCd1
	 *            所属1コード
	 * @param affName1
	 *            所属1名称
	 * @param affCd2
	 *            所属2コード
	 * @param affName2
	 *            所属2名称
	 * @param touJigyoryoikiCd
	 *            当月事業領域コード
	 * @param touJigyoryoikiName
	 *            当月事業領域名称
	 * @param preJigyoryoikiCd
	 *            前月事業領域コード
	 * @param preJigyoryoikiName
	 *            前月事業領域名称
	 * @param updateUser
	 *            更新ユーザ
	 * @param shatakuKanriId
	 *            社宅管理台帳ID
	 * @param shoriNengetsu
	 *            処理年月
	 */
	private void updateShozokuRireki(String companyCd, String agencyCd, String agencyName, String affCd1,
			String affName1, String affCd2, String affName2, String touJigyoryoikiCd, String touJigyoryoikiName,
			String preJigyoryoikiCd, String preJigyoryoikiName, String updateUser, Long shatakuKanriId,
			String shoriNengetsu) {

		Skf3050Bt001UpdateShozokuRirekiExpParameter param = new Skf3050Bt001UpdateShozokuRirekiExpParameter();

		if (companyCd == null) {
			param.setCompanyCd(null);
		} else {
			param.setCompanyCd(companyCd);
		}

		String companyName = getCompanyName(companyCd);
		param.setCompanyName(companyName);

		if (agencyCd == null) {
			param.setAgencyCd(null);
		} else {
			param.setAgencyCd(agencyCd);
		}

		if (agencyName == null) {
			param.setAgencyName(null);
		} else {
			param.setAgencyName(agencyName);
		}

		if (affCd1 == null) {
			param.setAffiliation1Cd(null);
		} else {
			param.setAffiliation1Cd(affCd1);
		}

		if (affName1 == null) {
			param.setAffiliation1(null);
		} else {
			param.setAffiliation1(affName1);
		}

		if (affCd2 == null) {
			param.setAffiliation2Cd(null);
		} else {
			param.setAffiliation2Cd(affCd2);
		}

		if (affName2 == null) {
			param.setAffiliation2(null);
		} else {
			param.setAffiliation2(affName2);
		}

		if (touJigyoryoikiCd == null) {
			param.setBusinessAreaCd(null);
		} else {
			param.setBusinessAreaCd(touJigyoryoikiCd);
		}

		if (touJigyoryoikiName == null) {
			param.setBusinessAreaName(null);
		} else {
			param.setBusinessAreaName(touJigyoryoikiName);
		}

		if (preJigyoryoikiCd == null) {
			param.setPreBusinessAreaCd(null);
		} else {
			param.setPreBusinessAreaCd(preJigyoryoikiCd);
		}

		if (preJigyoryoikiName == null) {
			param.setPreBusinessAreaName(null);
		} else {
			param.setPreBusinessAreaName(preJigyoryoikiName);
		}

		if (updateUser == null) {
			param.setUpdateUser(null);
		} else {
			param.setUpdateUser(updateUser);
		}

		param.setShatakuKanriId(shatakuKanriId);
		param.setYearMonth(shoriNengetsu);
		param.setUpdateProgramId(closeBatchPrgId);

		skf3050Bt001UpdateShozokuRirekiExpRepository.updateShozokuRireki(param);

	}

	/**
	 * 会社名取得
	 * 
	 * @param companyCd
	 *            会社コード
	 * @return 会社名
	 */
	private String getCompanyName(String companyCd) {

		String outVal = "";

		if (NfwStringUtils.isEmpty(companyCd)) {
			return outVal;
		}

		List<Skf3050Bt001GetCompanyAgencyNameExp> dataTable = skf3050Bt001GetCompanyAgencyNameExpRepository
				.getCompanyAgencyName(companyCd);

		if (dataTable.size() > 0) {
			outVal = dataTable.get(0).getCompanyName();
		}

		return outVal;
	}

	/**
	 * 社宅管理台帳基本テーブル等の現物算定額算出用データ取得
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 現物算定額算出用データ取得
	 */
	private List<Skf3050Bt001GetShatakiKanriDaityoDataTodoufukenDataExp> getShatakiKanriDaityoDataTodoufukenData(
			String shoriNengetsu) {

		Skf3050Bt001GetShatakiKanriDaityoDataTodoufukenDataExpParameter param = new Skf3050Bt001GetShatakiKanriDaityoDataTodoufukenDataExpParameter();

		if (!NfwStringUtils.isEmpty(shoriNengetsu)) {
			param.setYearMonth(shoriNengetsu);

			String nextShoriNengetsu = skfDateFormatUtils.addYearMonth(shoriNengetsu, 1);
			String yokugetsuMatsu = getGetsumatsujitu(nextShoriNengetsu);
			param.setYokugetsuMatsu(yokugetsuMatsu);

			String yokugetsuGessho = nextShoriNengetsu + "01";
			param.setYokugetsuGessho(yokugetsuGessho);
		}

		List<Skf3050Bt001GetShatakiKanriDaityoDataTodoufukenDataExp> outData = skf3050Bt001GetShatakiKanriDaityoDataTodoufukenDataExpRepository
				.getShatakiKanriDaityoDataTodoufukenData(param);

		return outData;
	}

	/**
	 * 現物算定額を取得 ※小数点以下切り捨て、面積は社宅使用料算定上延べ面積
	 * 
	 * @param genSanMenseki
	 *            対象面積
	 * @param genSanteiRow
	 *            現物算定額算出用データ
	 * @return 現物算定額
	 */
	private BigDecimal calcGenSanteiGaku(BigDecimal genSanMenseki,
			Skf3050Bt001GetShatakiKanriDaityoDataTodoufukenDataExp genSanteiRow) {

		BigDecimal kyojuRiekigaku = BigDecimal.valueOf(genSanteiRow.getKyojuRiekigaku());
		BigDecimal rentalMonth = BigDecimal.valueOf(genSanteiRow.getRentalMonth());
		BigDecimal parking1RentalMonth = BigDecimal.valueOf(genSanteiRow.getParking1RentalMonth());
		BigDecimal parking2RentalMonth = BigDecimal.valueOf(genSanteiRow.getParking2RentalMonth());
		String xmlGenbutsuSanteigakuSanshutsuBunshiKeisu = PropertyUtils
				.getValue("skf.common.settings.genbutsu_bunshi_keisuu");
		BigDecimal genbutsuSanteigakuSanshutsuBunshiKeisu = new BigDecimal(xmlGenbutsuSanteigakuSanshutsuBunshiKeisu);
		String xmlGenbutsuSanteigakuSanshutsuBunboKeisu = PropertyUtils
				.getValue("skf.common.settings.genbutsu_bunbo_keisuu");
		BigDecimal genbutsuSanteigakuSanshutsuBunboKeisu = new BigDecimal(xmlGenbutsuSanteigakuSanshutsuBunboKeisu);

		BigDecimal num_1 = genSanMenseki.multiply(kyojuRiekigaku).multiply(genbutsuSanteigakuSanshutsuBunshiKeisu);
		BigDecimal num_2 = num_1.divide(genbutsuSanteigakuSanshutsuBunboKeisu, 4, RoundingMode.DOWN);
		BigDecimal num_3 = num_2.subtract(rentalMonth).subtract(parking1RentalMonth).subtract(parking2RentalMonth);
		BigDecimal outVal = num_3.setScale(0, RoundingMode.DOWN);

		if (outVal.compareTo(BigDecimal.ZERO) < 0) {
			outVal = BigDecimal.ZERO;
		}

		return outVal;
	}

	/**
	 * 月別使用料履歴テーブル現物算定額のデータ更新
	 * 
	 * @param santeiGaku
	 *            現物算定額
	 * @param updateUser
	 *            更新ユーザ
	 * @param shoriNengetsu
	 *            処理年月
	 * @param shataKanriId
	 *            社宅管理台帳ID
	 */
	private Integer updateTsukibetsuSiyoryorirekiGenbutsuSantei(Integer santeiGaku, String updateUser,
			String shoriNengetsu, Long shataKanriId) {

		Skf3050Bt001UpdateTsukibetsuSiyoryorirekiGenbutsuSanteiExpParameter param = new Skf3050Bt001UpdateTsukibetsuSiyoryorirekiGenbutsuSanteiExpParameter();

		if (santeiGaku == null) {
			param.setGenbutuSantei(null);
		} else {
			param.setGenbutuSantei(santeiGaku);
		}

		if (updateUser == null) {
			param.setUpdateUser(null);
		} else {
			param.setUpdateUser(updateUser);
		}

		param.setYearMonth(shoriNengetsu);
		param.setShatakuKanriId(shataKanriId);
		param.setUpdateProgramId(closeBatchPrgId);

		Integer updateCnt = skf3050Bt001UpdateTsukibetsuSiyoryorirekiGenbutsuSanteiExpRepository
				.updateTsukibetsuSiyoryorirekiGenbutsuSantei(param);

		return updateCnt;
	}

	/**
	 * 月別利用料履歴テーブル取得（HR連携データ作成前処理取得）
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 月別利用料履歴テーブルの処理前データ
	 */
	private List<Skf3050Bt001GetHrRenkeiDataSakuseiMaeSyoriDataExp> getHrRenkeiDataSakuseiMaeSyoriData(
			String shoriNengetsu) {

		List<Skf3050Bt001GetHrRenkeiDataSakuseiMaeSyoriDataExp> outData = skf3050Bt001GetHrRenkeiDataSakuseiMaeSyoriDataExpRepository
				.getHrRenkeiDataSakuseiMaeSyoriData(shoriNengetsu);

		return outData;
	}

	/**
	 * 社宅基本情報テーブル取得
	 * 
	 * @param shatakuKanriBangou
	 *            社宅管理番号
	 * @return 社宅基本情報データ
	 */
	private List<Skf3050Bt001GetShatakuKihonJyohoExp> getShatakuKihonJyoho(Long shatakuKanriBangou) {

		List<Skf3050Bt001GetShatakuKihonJyohoExp> outData = skf3050Bt001GetShatakuKihonJyohoExpRepository
				.getShatakuKihonJyoho(shatakuKanriBangou);

		return outData;
	}

	/**
	 * 勘定科目コード、勘定科目名取得
	 * 
	 * @param shiyouryo
	 *            使用料
	 * @param kyuyoCompanyCd
	 *            給与支給会社コード
	 * @param manageCompanyCd
	 *            管理会社コード
	 * @param kariukeCompanyCd
	 *            借受会社コード
	 * @param sougoRiyouKbn
	 *            相互利用判定区分
	 * @param shatakuKbn
	 *            社宅区分
	 * @param kanjouKamokuKbn
	 *            勘定科目区分
	 * @return 処理結果Map
	 */
	private Map<String, Object> getKeijouKamoku(int shiyouryo, String kyuyoCompanyCd, String manageCompanyCd,
			String kariukeCompanyCd, String sougoRiyouKbn, String shatakuKbn, String kanjouKamokuKbn) {

		Map<String, Object> rtnMap = new HashMap<>();
		String accountId = "";

		if (CodeConstant.ACCOUNT_KBN_SHATAKU.equals(kanjouKamokuKbn)) {
			if (shiyouryo == 0) {
				rtnMap.put(KEIJOU_KAMOKU_ACNT_CD_KEY, "");
				rtnMap.put(KEIJOU_KAMOKU_ACNT_NM_KEY, "");
				rtnMap.put(KEIJOU_KAMOKU_RTN_STS_KEY, RETURN_STATUS_OK);
				return rtnMap;

			} else {
				if (CodeConstant.MUTUAL_USE_KBN_AVAILABLE.equals(sougoRiyouKbn)) {
					if (!Objects.equals(kariukeCompanyCd, kyuyoCompanyCd)) {
						Skf1010MCompany compDt = skf1010MCompanyRepository.selectByPrimaryKey(kyuyoCompanyCd);
						if (compDt != null) {
							accountId = ACCOUNT_ID_1;
						}
					}

				} else {
					if (!Objects.equals(manageCompanyCd, kyuyoCompanyCd)) {
						Skf1010MCompany compDt = skf1010MCompanyRepository.selectByPrimaryKey(kyuyoCompanyCd);
						if (compDt != null) {
							accountId = ACCOUNT_ID_1;
						}
					}
				}
			}

			if (NfwStringUtils.isEmpty(accountId)) {
				if (CodeConstant.KARIAGE.equals(shatakuKbn) || CodeConstant.ITTOU.equals(shatakuKbn)
						|| CodeConstant.C001.equals(kariukeCompanyCd)) {
					accountId = ACCOUNT_ID_2;
				} else {
					accountId = ACCOUNT_ID_3;
				}
			}

		} else {
			if (shiyouryo == 0) {
				rtnMap.put(KEIJOU_KAMOKU_ACNT_CD_KEY, "");
				rtnMap.put(KEIJOU_KAMOKU_ACNT_NM_KEY, "");
				rtnMap.put(KEIJOU_KAMOKU_RTN_STS_KEY, RETURN_STATUS_OK);
				return rtnMap;

			} else {
				if (CodeConstant.MUTUAL_USE_KBN_AVAILABLE.equals(sougoRiyouKbn)) {
					if (!Objects.equals(kariukeCompanyCd, kyuyoCompanyCd)) {
						Skf1010MCompany compDt = skf1010MCompanyRepository.selectByPrimaryKey(kyuyoCompanyCd);
						if (compDt != null) {
							accountId = ACCOUNT_ID_4;
						}
					}

				} else {
					if (!Objects.equals(manageCompanyCd, kyuyoCompanyCd)) {
						Skf1010MCompany compDt = skf1010MCompanyRepository.selectByPrimaryKey(kyuyoCompanyCd);
						if (compDt != null) {
							accountId = ACCOUNT_ID_4;
						}
					}
				}
			}

			if (NfwStringUtils.isEmpty(accountId)) {
				if (CodeConstant.MUTUAL_USE_KBN_AVAILABLE.equals(sougoRiyouKbn)) {
					accountId = ACCOUNT_ID_4;
				} else {
					accountId = ACCOUNT_ID_5;
				}
			}
		}

		Skf3050MAccount kaikeiShatakuDt = skf3050MAccountRepository.selectByPrimaryKey(accountId);

		if (kaikeiShatakuDt != null) {
			rtnMap.put(KEIJOU_KAMOKU_ACNT_CD_KEY, kaikeiShatakuDt.getAccountCode());
			rtnMap.put(KEIJOU_KAMOKU_ACNT_NM_KEY, kaikeiShatakuDt.getAccountName());
			rtnMap.put(KEIJOU_KAMOKU_RTN_STS_KEY, RETURN_STATUS_OK);
			return rtnMap;

		} else {
			String errMsg = ERRMSG_KAIKEI_ACCOUNT_ID_1 + CodeConstant.COLON + accountId;
			rtnMap.put(KEIJOU_KAMOKU_ERR_MSG_KEY, errMsg);
			rtnMap.put(KEIJOU_KAMOKU_RTN_STS_KEY, RETURN_STATUS_NG);
			return rtnMap;
		}
	}

	/**
	 * 月別使用料履歴テーブル．勘定科目更新
	 * 
	 * @param shatakuAccountCd
	 *            社宅使用料計上勘定科目コード
	 * @param shatakuAccountName
	 *            社宅使用料計上勘定科目
	 * @param kyoeiAccountCd
	 *            共益費個人負担金計上勘定科目コード
	 * @param kyoeiAccountName
	 *            共益費個人負担金計上勘定科目
	 * @param updateUser
	 *            更新ユーザ
	 * @param shatakuKanriDaichouId
	 *            社宅管理台帳ID
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 更新件数
	 */
	private int updateTsukibetsuShiyoryoAccount(String shatakuAccountCd, String shatakuAccountName,
			String kyoeiAccountCd, String kyoeiAccountName, String updateUser, Long shatakuKanriDaichouId,
			String shoriNengetsu) {

		Skf3050Bt001UpdateTsukibetsuShiyoryoAccountExpParameter param = new Skf3050Bt001UpdateTsukibetsuShiyoryoAccountExpParameter();

		if (shatakuAccountCd == null) {
			param.setShatakuAccountCd(null);
		} else {
			param.setShatakuAccountCd(shatakuAccountCd);
		}

		if (shatakuAccountName == null) {
			param.setShatakuAccountName(null);
		} else {
			param.setShatakuAccountName(shatakuAccountName);
		}

		if (kyoeiAccountCd == null) {
			param.setKyoekiAccountCd(null);
		} else {
			param.setKyoekiAccountCd(kyoeiAccountCd);
		}

		if (kyoeiAccountName == null) {
			param.setKyoekiAccountName(null);
		} else {
			param.setKyoekiAccountName(kyoeiAccountName);
		}

		if (updateUser == null) {
			param.setUpdateUser(null);
		} else {
			param.setUpdateUser(updateUser);
		}

		param.setYearMonth(shoriNengetsu);
		param.setShatakuKanriId(shatakuKanriDaichouId);
		param.setUpdateProgramId(closeBatchPrgId);

		int updateCnt = skf3050Bt001UpdateTsukibetsuShiyoryoAccountExpRepository.updateTsukibetsuShiyoryoAccount(param);

		return updateCnt;
	}

	/**
	 * 月次処理管理テーブル（締め処理実行区分）更新
	 * 
	 * @param updateUser
	 *            更新ユーザ
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 更新件数
	 */
	private int updateGetsujiShoriKanriData(String updateUser, String shoriNengetsu) {

		Skf3050Bt001UpdateGetsujiShoriKanriDataExpParameter param = new Skf3050Bt001UpdateGetsujiShoriKanriDataExpParameter();

		if (updateUser == null) {
			param.setUpdateUser(null);
		} else {
			param.setUpdateUser(updateUser);
		}

		param.setCycleBillingYymm(shoriNengetsu);
		param.setUpdateProgramId(closeBatchPrgId);

		int updateCnt = skf3050Bt001UpdateGetsujiShoriKanriDataExpRepository.updateGetsujiShoriKanriData(param);

		return updateCnt;
	}

	/**
	 * ユーザーIDを取得する。
	 * 
	 * @return
	 */
	public String getUserId() {

		String outUserId = LoginUserInfoUtils.getUserCd();
		return outUserId;
	}

	/**
	 * ボタン押下時のメッセージを設定する。
	 * 
	 * @param inDto
	 *            対象のDto
	 * @return 設定後のDto
	 */
	public Skf3050Sc002CommonDto setBtnMsg(Skf3050Sc002CommonDto inDto) {

		String targetNengetsu = editDisplayNengetsu(inDto.getHdnJikkouShijiYoteiNengetsu());

		String kariKeisanMsg = editMsg(MessageIdConstant.I_SKF_3077, targetNengetsu);
		inDto.setHdnKariKeisanBtnMsg(kariKeisanMsg);

		String shimeShoriMsg = editMsg(MessageIdConstant.I_SKF_3008, targetNengetsu);
		inDto.setHdnShimeShoriBtnMsg(shimeShoriMsg);

		String renkeiDataSakuseiMsg = editMsg(MessageIdConstant.I_SKF_3009, targetNengetsu);
		inDto.setHdnRenkeiDataSakuseiBtnMsg(renkeiDataSakuseiMsg);

		String shimeKaijoMsg = editMsg(MessageIdConstant.I_SKF_3011, targetNengetsu);
		inDto.setHdnShimeKaijoBtnMsg(shimeKaijoMsg);

		String renkeiDataKakuteiMsg = editMsg(MessageIdConstant.I_SKF_3010, targetNengetsu);
		inDto.setHdnRenkeiDataKakuteiBtnMsg(renkeiDataKakuteiMsg);

		return inDto;
	}

	/**
	 * 表示用の年月を編集する。 例：2020年 3月
	 * 
	 * @param inNengetsu
	 *            編集する年月
	 * @return 編集後の年月
	 */
	public String editDisplayNengetsu(String inNengetsu) {

		if (NfwStringUtils.isEmpty(inNengetsu)) {
			return inNengetsu;
		}

		String year = inNengetsu.substring(0, 4);
		String firstMonth = inNengetsu.substring(4, 5);
		String month = "";

		if ("0".equals(firstMonth)) {
			month = CodeConstant.SPACE + inNengetsu.substring(5, 6);
		} else {
			month = inNengetsu.substring(4);
		}

		String outNengetsu = year + "年" + month + "月";

		return outNengetsu;
	}

	/**
	 * メッセージを編集する。
	 *
	 * @param msgId
	 *            メッセージID
	 * @param replaceStr
	 *            置換文字列
	 * @return 編集後のメッセージ
	 */
	public String editMsg(String msgId, String replaceStr) {

		String msgFormat = PropertyUtils.getValue(msgId);
		String regex = "{0}";
		String outMsg = msgFormat.replace(regex, replaceStr);

		return outMsg;
	}

}
