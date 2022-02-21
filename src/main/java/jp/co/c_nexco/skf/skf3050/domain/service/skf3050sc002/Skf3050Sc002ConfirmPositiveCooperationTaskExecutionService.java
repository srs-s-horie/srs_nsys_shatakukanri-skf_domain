/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3050.domain.service.skf3050sc002;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004GetCompanyAgencyNameExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004GetRouterShiyoryoMeisaiDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004GetRouterShiyoryoMeisaiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004GetRouterShiyoryoRirekiJoinDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004GetShainSoshikiDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004GetShainSoshikiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004GetShatakuHeyaDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004GetShatakuHeyaDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004GetShatakuKanriDaityoSogoriyoDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004GetShatakuShainIdoRirekiDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004GetShatakuShainIdoRirekiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004GetShatakuShiyoryoYoyakuDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004GetShatakuShiyoryoYoyakuDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004GetShiyoryoUpdateRecodExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004GetTeijiJoinDataCntExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004GetTsukibetsuTyusyajyoBlockRirekiDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004GetTsukibetsuTyusyajyoBlockRirekiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004GetTsukibetuBihinSiyoryoMeisaiDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004GetTsukibetuBihinSiyoryoMeisaiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004GetTsukibetuShiyoryoRirekiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004GetTsukibetuShiyoryoRirekiJoinDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004GetTsukibetuSougoriyoRirekiDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004GetTsukibetuSougoriyoRirekiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004GetTsukibetuSyozokuRirekiDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004GetTsukibetuSyozokuRirekiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004GetTsukibetuTyusyajyoRirekiDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004GetTsukibetuTyusyajyoRirekiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004InsertGetsujiSyoriKanriDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004InsertTsukibetuBihinSiyoryoMeisaiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004InsertTsukibetuShiyoryoRirekiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004InsertTsukibetuSougoriyoRirekiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004InsertTsukibetuSyozokujyohoRirekiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004InsertTsukibetuTyusyajyoRirekiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004UpdateGetsujiSyoriKanriExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004UpdateShatakuHeyaDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004UpdateShatakuKihonJyohoJikaiSanteiNengappiExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004UpdateShatakuShiyouryouYoyakuDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004UpdateShiyoryoPatternDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004UpdateTsukibetuShiyoryoRirekiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt004.Skf3050Bt004UpdateshatakuTyusyajyoKukakuJyohoTaiyojyokyoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBaseBusinessLogicUtils.SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBaseBusinessLogicUtils.SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfKyoekihiCalcUtils.SkfKyoekihiCalcUtilsInputExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfKyoekihiCalcUtils.SkfKyoekihiCalcUtilsOutputExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfRouterInfoUtils.SkfRouterInfoUtilsGetEquipmentPaymentExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2100TMobileRouterRentalRireki;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2100TMobileRouterRentalRirekiKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2100TMobileRouterRentalRirekiMeisai;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TParkingRireki;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TParkingRirekiKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3050TMonthlyManageData;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004DeleteNyutaikyoDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004DeleteRouterYoteiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004DeleteTeijiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004DeleteTenninshaDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004GetCompanyAgencyNameExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004GetDataForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004GetRouterShiyoryoMeisaiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004GetRouterShiyoryoRirekiJoinDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004GetShainSoshikiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004GetShatakuHeyaDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004GetShatakuKanriDaityoSogoriyoDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004GetShatakuKihonJyohoMasterDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004GetShatakuShainIdoRirekiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004GetShatakuShiyoryoYoyakuDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004GetShiyoryoPatternDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004GetShiyoryoUpdateRecodExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004GetTeijiJoinDataCntExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004GetTsukibetsuTyusyajyoBlockRirekiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004GetTsukibetuBihinSiyoryoMeisaiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004GetTsukibetuShiyoryoRirekiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004GetTsukibetuShiyoryoRirekiJoinDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004GetTsukibetuSougoriyoRirekiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004GetTsukibetuSyozokuRirekiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004GetTsukibetuTyusyajyoRirekiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004InsertGetsujiSyoriKanriDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004InsertRouterRirekiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004InsertTsukibetuBihinSiyoryoMeisaiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004InsertTsukibetuShiyoryoRirekiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004InsertTsukibetuSougoriyoRirekiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004InsertTsukibetuSyozokujyohoRirekiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004InsertTsukibetuTyusyajyoRirekiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004UpdateGetsujiSyoriKanriExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004UpdateShatakuHeyaDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004UpdateShatakuKihonJyohoJikaiSanteiNengappiExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004UpdateShatakuShiyouryouYoyakuDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004UpdateShiyoryoPatternDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004UpdateTsukibetuShiyoryoRirekiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt004.Skf3050Bt004UpdateshatakuTyusyajyoKukakuJyohoTaiyojyokyoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfRollBack.SkfRollBackExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2100TMobileRouterRentalRirekiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3030TParkingRirekiRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.common.utils.PropertyUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfKyoekihiCalcUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.SkfRouterInfoUtils;
import jp.co.c_nexco.skf.skf3050.domain.dto.skf3050sc002.Skf3050Sc002ConfirmPositiveCooperationTaskExecutionDto;

/**
 * Skf3050Sc002ConfirmPositiveCooperationTaskExecutionService 月次運用管理画面のPOSITIVE連携データ確定処理
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3050Sc002ConfirmPositiveCooperationTaskExecutionService extends SkfServiceAbstract<Skf3050Sc002ConfirmPositiveCooperationTaskExecutionDto> {

	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3050Sc002SharedService skf3050Sc002SharedService;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private Skf3050Bt004GetDataForUpdateExpRepository skf3050Bt004GetDataForUpdateExpRepository;
	@Autowired
	private Skf3050Bt004GetShiyoryoPatternDataExpRepository skf3050Bt004GetShiyoryoPatternDataExpRepository;
	@Autowired
	private Skf3050Bt004GetShiyoryoUpdateRecodExpRepository skf3050Bt004GetShiyoryoUpdateRecodExpRepository;
	@Autowired
	private SkfRollBackExpRepository skfRollBackExpRepository;
	@Autowired
	private Skf3050Bt004GetTsukibetuShiyoryoRirekiJoinDataExpRepository skf3050Bt004GetTsukibetuShiyoryoRirekiJoinDataExpRepository;
	@Autowired
	private Skf3050Bt004GetShatakuKanriDaityoSogoriyoDataExpRepository skf3050Bt004GetShatakuKanriDaityoSogoriyoDataExpRepository;
	@Autowired
	private Skf3050Bt004GetShatakuHeyaDataExpRepository skf3050Bt004GetShatakuHeyaDataExpRepository;
	@Autowired
	private Skf3050Bt004UpdateShatakuHeyaDataExpRepository skf3050Bt004UpdateShatakuHeyaDataExpRepository;
	@Autowired
	private Skf3050Bt004GetTsukibetsuTyusyajyoBlockRirekiDataExpRepository skf3050Bt004GetTsukibetsuTyusyajyoBlockRirekiDataExpRepository;
	@Autowired
	private Skf3050Bt004UpdateshatakuTyusyajyoKukakuJyohoTaiyojyokyoExpRepository skf3050Bt004UpdateshatakuTyusyajyoKukakuJyohoTaiyojyokyoExpRepository;
	@Autowired
	private Skf3050Bt004DeleteNyutaikyoDataExpRepository skf3050Bt004DeleteNyutaikyoDataExpRepository;
	@Autowired
	private Skf3050Bt004DeleteTenninshaDataExpRepository skf3050Bt004DeleteTenninshaDataExpRepository;
	@Autowired
	private Skf3050Bt004DeleteTeijiDataExpRepository skf3050Bt004DeleteTeijiDataExpRepository;
	@Autowired
	private Skf3050Bt004UpdateGetsujiSyoriKanriExpRepository skf3050Bt004UpdateGetsujiSyoriKanriExpRepository;
	@Autowired
	private Skf3050Bt004InsertGetsujiSyoriKanriDataExpRepository skf3050Bt004InsertGetsujiSyoriKanriDataExpRepository;
	@Autowired
	private Skf3050Bt004GetShatakuKihonJyohoMasterDataExpRepository skf3050Bt004GetShatakuKihonJyohoMasterDataExpRepository;
	@Autowired
	private Skf3050Bt004UpdateShatakuKihonJyohoJikaiSanteiNengappiExpRepository skf3050Bt004UpdateShatakuKihonJyohoJikaiSanteiNengappiExpRepository;
	@Autowired
	private Skf3050Bt004UpdateShiyoryoPatternDataExpRepository skf3050Bt004UpdateShiyoryoPatternDataExpRepository;
	@Autowired
	private Skf3050Bt004GetShatakuShiyoryoYoyakuDataExpRepository skf3050Bt004GetShatakuShiyoryoYoyakuDataExpRepository;
	@Autowired
	private Skf3050Bt004GetTsukibetuShiyoryoRirekiDataExpRepository skf3050Bt004GetTsukibetuShiyoryoRirekiDataExpRepository;
	@Autowired
	private Skf3050Bt004InsertTsukibetuShiyoryoRirekiDataExpRepository skf3050Bt004InsertTsukibetuShiyoryoRirekiDataExpRepository;
	@Autowired
	private Skf3050Bt004UpdateShatakuShiyouryouYoyakuDataExpRepository skf3050Bt004UpdateShatakuShiyouryouYoyakuDataExpRepository;
	@Autowired
	private Skf3050Bt004GetTsukibetuSyozokuRirekiDataExpRepository skf3050Bt004GetTsukibetuSyozokuRirekiDataExpRepository;
	@Autowired
	private Skf3050Bt004GetShatakuShainIdoRirekiDataExpRepository skf3050Bt004GetShatakuShainIdoRirekiDataExpRepository;
	@Autowired
	private Skf3050Bt004GetShainSoshikiDataExpRepository skf3050Bt004GetShainSoshikiDataExpRepository;
	@Autowired
	private Skf3050Bt004GetCompanyAgencyNameExpRepository skf3050Bt004GetCompanyAgencyNameExpRepository;
	@Autowired
	private Skf3050Bt004InsertTsukibetuSyozokujyohoRirekiDataExpRepository skf3050Bt004InsertTsukibetuSyozokujyohoRirekiDataExpRepository;
	@Autowired
	private Skf3050Bt004GetTsukibetuBihinSiyoryoMeisaiDataExpRepository skf3050Bt004GetTsukibetuBihinSiyoryoMeisaiDataExpRepository;
	@Autowired
	private Skf3050Bt004GetTsukibetuTyusyajyoRirekiDataExpRepository skf3050Bt004GetTsukibetuTyusyajyoRirekiDataExpRepository;
	@Autowired
	private Skf3050Bt004InsertTsukibetuBihinSiyoryoMeisaiDataExpRepository skf3050Bt004InsertTsukibetuBihinSiyoryoMeisaiDataExpRepository;
	@Autowired
	private Skf3050Bt004InsertTsukibetuTyusyajyoRirekiDataExpRepository skf3050Bt004InsertTsukibetuTyusyajyoRirekiDataExpRepository;
	@Autowired
	private Skf3050Bt004GetTsukibetuSougoriyoRirekiDataExpRepository skf3050Bt004GetTsukibetuSougoriyoRirekiDataExpRepository;
	@Autowired
	private Skf3050Bt004InsertTsukibetuSougoriyoRirekiDataExpRepository skf3050Bt004InsertTsukibetuSougoriyoRirekiDataExpRepository;
	@Autowired
	private Skf3030TParkingRirekiRepository skf3030TParkingRirekiRepository;
	@Autowired
	private Skf3050Bt004UpdateTsukibetuShiyoryoRirekiDataExpRepository skf3050Bt004UpdateTsukibetuShiyoryoRirekiDataExpRepository;
	@Autowired
	private Skf3050Bt004GetTeijiJoinDataCntExpRepository skf3050Bt004GetTeijiJoinDataCntExpRepository;
	
	//共益費日割計算対応 2021/5/14 add start 
	@Autowired
	private SkfKyoekihiCalcUtils skfKyoekihiCalcUtils;
	//共益費日割計算対応 2021/5/14 add end
	
	//モバイルルーター機能追加対応 2021/9 add start
	@Autowired
	private Skf3050Bt004GetRouterShiyoryoRirekiJoinDataExpRepository skf3050Bt004GetRouterShiyoryoRirekiJoinDataExpRepository;
	@Autowired
	private Skf2100TMobileRouterRentalRirekiRepository skf2100TMobileRouterRentalRirekiRepository;
	@Autowired
	private Skf3050Bt004GetRouterShiyoryoMeisaiDataExpRepository skf3050Bt004GetRouterShiyoryoMeisaiDataExpRepository;
	@Autowired
	private Skf3050Bt004DeleteRouterYoteiDataExpRepository skf3050Bt004DeleteRouterYoteiDataExpRepository;
	@Autowired
	private Skf3050Bt004InsertRouterRirekiDataExpRepository skf3050Bt004InsertRouterRirekiDataExpRepository;
	@Autowired
	private SkfRouterInfoUtils skfRouterInfoUtils;
	private static final String ERRMSG_ROUTERMEISAI_0 = "モバイルルーター使用料明細（当月）";
	//モバイルルーター機能追加対応 2021/9 add end

	private static final String BATCH_NAME = "POSITIVE連携データ確定";
	private static final String ERRMSG_TSUKIBETSUSHOZOKU_0 = "月別所属履歴（当月）";
	private static final String ERRMSG_TSUKIBETSUSOGORIREKI_0 = "月別相互利用履歴（当月）";
	private static final String ERRMSG_TSUKIBETSUSOGO_0 = "社宅管理台帳相互利用基本";
	private static final String ERRMSG_TSUKIBETSUSOGO_1 = "社宅管理台帳ID";
	private static final String JIGYO_RYO_CD_KEY = "jigyoRyoikiCd";
	private static final String JIGYO_RYO_NAME_KEY = "jigyoRyoikiName";

	@Value("${skf3050.skf3050_bt004.batch_prg_id}")
	private String confirmPositiveDataBatchPrgId;

	private int updateShiyouCnt = 0;
	private int updateShatakuCnt = 0;
	private int insertTsukiCnt = 0;

	@Override
	protected BaseDto index(Skf3050Sc002ConfirmPositiveCooperationTaskExecutionDto confirmPositiveCoopDto) throws Exception {
		
		skfOperationLogUtils.setAccessLog("POSITIVE連携データ確定処理", CodeConstant.C001, FunctionIdConstant.SKF3050_SC002);

		confirmPositiveCoopDto.setResultMessages(null);
		String confirmResult = SkfCommonConstant.ABNORMAL;
		String jikkouShijiYoteiNengetsu = confirmPositiveCoopDto.getHdnJikkouShijiYoteiNengetsu();
		
		//▼連携データ確定処理起動事前チェック
		String errMsg = checkBeforeStartup(jikkouShijiYoteiNengetsu);
		if (!"".equals(errMsg)) {
			ServiceHelper.addErrorResultMessage(confirmPositiveCoopDto, null, MessageIdConstant.E_SKF_3026, errMsg);
			throwBusinessExceptionIfErrors(confirmPositiveCoopDto.getResultMessages());
		}
		
		Map<String, String> dataMap = new HashMap<>();
		dataMap.put(Skf3050Sc002SharedService.BATCH_PRG_ID_KEY, confirmPositiveDataBatchPrgId);
		dataMap.put(Skf3050Sc002SharedService.COMPANY_CD_KEY, CodeConstant.C001);
		dataMap.put(Skf3050Sc002SharedService.USER_ID_KEY, skf3050Sc002SharedService.getUserId());
		dataMap.put(Skf3050Sc002SharedService.SHORI_NENGETSU_KEY, jikkouShijiYoteiNengetsu);

		LogUtils.info(MessageIdConstant.I_SKF_1022, BATCH_NAME);

		Date sysDate = skf3050Sc002SharedService.getSystemDate();
		List<String> endList = skf3050Sc002SharedService.setEndList();

		//トランザクションAを開始
		int registResult = skf3050Sc002SharedService.registBatchControl(dataMap, sysDate, endList);

		if (registResult == CodeConstant.SYS_NG) {
			skf3050Sc002SharedService.outputEndProcLog(BATCH_NAME);
			ServiceHelper.addErrorResultMessage(confirmPositiveCoopDto, null, MessageIdConstant.E_SKF_3025, "バッチ制御テーブル更新に失敗したため");
			return confirmPositiveCoopDto;
		}
		try {
			//トランザクションBの開始
			confirmResult = confirmData(dataMap);
		} catch (Exception e) {
			LogUtils.infoByMsg("異常終了:" + BATCH_NAME + "(" + e.getMessage() + ")");
		}
		//トランザクションCの開始
		//終了処理
		skf3050Sc002SharedService.endProc(confirmResult,dataMap.get(
				Skf3050Sc002SharedService.COMPANY_CD_KEY), confirmPositiveDataBatchPrgId,SkfCommonConstant.PROCESSING);

		if (!SkfCommonConstant.ABNORMAL.equals(confirmResult)) {
			String targetNengetsu = skf3050Sc002SharedService.editDisplayNengetsu(jikkouShijiYoteiNengetsu);
			ServiceHelper.addResultMessage(confirmPositiveCoopDto, MessageIdConstant.I_SKF_3090, targetNengetsu);
		} else {
			ServiceHelper.addErrorResultMessage(confirmPositiveCoopDto, null, MessageIdConstant.E_SKF_1079);
			LogUtils.infoByMsg("異常終了:トランザクションB　POSITIVE連携データ確定処理で失敗 :" + BATCH_NAME);
		}
		skf3050Sc002SharedService.outputManagementLogEndProc(endList);
		//管理ログ終了処理
		skf3050Sc002SharedService.outputEndProcLog(BATCH_NAME);
		// 画面リフレッシュ
		TransferPageInfo nextPage = TransferPageInfo.prevPage(FunctionIdConstant.SKF3050_SC002, "init");
		confirmPositiveCoopDto.setTransferPageInfo(nextPage);
		return confirmPositiveCoopDto;
	}

	/**
	 * POSITIVE連携データ確定処理前のチェック
	 * 
	 * @param jikkouShijiYoteiNengetsu 実行指示予定年月
	 * @return エラーメッセージ
	 */
	private String checkBeforeStartup(String jikkouShijiYoteiNengetsu) {

		String errMsg = "";

		//▼二重起動チェック
		boolean notDoubleStartup = skf3050Sc002SharedService.checkDoubleStartup();
		if (!notDoubleStartup) {
			errMsg = Skf3050Sc002SharedService.ERRMSG_DOUBLE_START;
			return errMsg;
		}

		//▼連携データ確定処理可能チェック
		Skf3050TMonthlyManageData renkeiKbnData = skf3050Sc002SharedService.getShimePositiveRenkeiKbn(jikkouShijiYoteiNengetsu);
		if (renkeiKbnData == null) {
			//月次処理管理データが取得できない場合エラー
			errMsg = Skf3050Sc002SharedService.ERRMSG_SHIME_IMPOSSIBLE;
			return errMsg;
		} else {
			// 202109 モバイルルーター機能追加対応 edit start
			if (!CodeConstant.BILLINGACTKBN_JIKKO_SUMI.equals(renkeiKbnData.getBillingActKbn()) || 
				!CodeConstant.BILLINGACTKBN_JIKKO_SUMI.equals(renkeiKbnData.getMobileRouterBillingActKbn()) || 
				!CodeConstant.LINKDATA_CREATE_KBN_JIKKO_SUMI.equals(renkeiKbnData.getLinkdataCreateKbn()) || 
				CodeConstant.LINKDATA_COMMIT_KBN_JIKKO_SUMI.equals(renkeiKbnData.getLinkdataCommitKbn())) {
				//締め処理≠実行済、またはモバイルルーター締め処理≠実行済、またはHR連携データ作成≠実行済、またはHR連携データ確定実行区分＝実行済の場合エラー
				errMsg = Skf3050Sc002SharedService.ERRMSG_SHIME_IMPOSSIBLE;
				return errMsg;
			}
			// 202109 モバイルルーター機能追加対応 edit end
		}
		return errMsg;
	}

	/**
	 * POSITIVE連携データ確定処理
	 * 
	 * @param parameter
	 *            パラメータ
	 * @return 結果
	 * @throws ParseException
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String confirmData(Map<String, String> parameter) throws ParseException {

		String paramShoriNengetsu = parameter.get(Skf3050Sc002SharedService.SHORI_NENGETSU_KEY);
		String paramUserId = parameter.get(Skf3050Sc002SharedService.USER_ID_KEY);
		String paramCompanyCd = parameter.get(Skf3050Sc002SharedService.COMPANY_CD_KEY);

		//▼▼経年調整処理▼▼
		if (!adjustKeinen(paramShoriNengetsu, paramUserId)) {
			endAbnormalProc();
			return SkfCommonConstant.ABNORMAL;
		}

		//次月領域作成
		String nextShoriNengetsu = skfDateFormatUtils.addYearMonth(paramShoriNengetsu, 1);

		if (!makeNextMonthAreaData(paramShoriNengetsu, nextShoriNengetsu, paramCompanyCd, paramUserId)) {
			endAbnormalProc();
			return SkfCommonConstant.ABNORMAL;
		}
		
		//使用料再計算
		if (!reCalcShatakuShiyoryo(nextShoriNengetsu, paramUserId)) {
			endAbnormalProc();
			return SkfCommonConstant.ABNORMAL;
		}

		//▼▼▼社宅部屋情報マスタの更新▼▼▼
		List<Skf3050Bt004GetShatakuHeyaDataExp> shatakuHeyaDtList = getShatakuHeyaData(
				CodeConstant.LEND_JOKYO_TAIKYO_YOTEI, paramShoriNengetsu);

		for (Skf3050Bt004GetShatakuHeyaDataExp shatakuHeyaDt : shatakuHeyaDtList) {

			List<Long> lockResult = skf3050Bt004GetDataForUpdateExpRepository
					.getSkf3010MShatakuRoomData(shatakuHeyaDt.getShatakuKanriNo());
			if (lockResult.size() == 0) {
				endAbnormalProc();
				return SkfCommonConstant.ABNORMAL;
			}

			updateShatakuHeyaData(CodeConstant.LEND_JOKYO_NASHI, paramUserId, shatakuHeyaDt.getShatakuKanriNo(),
					shatakuHeyaDt.getShatakuRoomKanriNo());
		}

		//▼▼▼社宅駐車場区画情報マスタの更新▼▼▼
		List<Skf3050Bt004GetTsukibetsuTyusyajyoBlockRirekiDataExp> tyushaKukakuDtList = getTsukibetsuTyusyajyoBlockRirekiData(
				CodeConstant.LEND_JOKYO_TAIKYO_YOTEI, paramShoriNengetsu);

		for (Skf3050Bt004GetTsukibetsuTyusyajyoBlockRirekiDataExp tyushaKukakuDt : tyushaKukakuDtList) {

			List<Long> lockResult = skf3050Bt004GetDataForUpdateExpRepository
					.getSkf3010MShatakuParkingBlockData(tyushaKukakuDt.getShatakuKanriNo());
			if (lockResult.size() == 0) {
				endAbnormalProc();
				return SkfCommonConstant.ABNORMAL;
			}

			updateShatakuTyusyajyoKukakuJyohoTaiyojyokyo(CodeConstant.LEND_JOKYO_NASHI, paramUserId,
					tyushaKukakuDt.getShatakuKanriNo(), tyushaKukakuDt.getParkingKanriNo());
		}

		//▼▼▼各テーブルデータ削除▼▼▼    
		if (!deleteData()) {
			endAbnormalProc();
			return SkfCommonConstant.ABNORMAL;
		}

		List<String> lockResult = skf3050Bt004GetDataForUpdateExpRepository.getSkf3050TMonthlyManageData(paramShoriNengetsu);
		if (lockResult.size() == 0) {
			endAbnormalProc();
			return SkfCommonConstant.ABNORMAL;
		}
		//▼▼▼月次処理管理データ更新（HR連携確定実行区分＝実行済）▼▼▼
		updateGetsujiSyoriKanri(paramShoriNengetsu, paramUserId);
		//▼▼▼月次処理管理データ登録▼▼▼
		insertGetsujiSyoriKanriData(nextShoriNengetsu, paramUserId);

		outputCntLog();

		return SkfCommonConstant.COMPLETE;

	}

	/**
	 * 経年調整処理
	 * 
	 * @param paramShoriNengetsu
	 *            処理年月
	 * @param paramUserId
	 *            ユーザーID
	 * @return コミット実行判定結果
	 * @throws ParseException
	 */
	private boolean adjustKeinen(String paramShoriNengetsu, String paramUserId) throws ParseException {

		//経年対象月日（MMDD）
		String xmlKeinenTaishouTsukihi = PropertyUtils.getValue("skf.common.settings.keinen_taishou_tsukihi");
		//経年対象月日の月（MM）
		String keinenTaishouTsuki = xmlKeinenTaishouTsukihi.substring(0, 2);
		//処理年月の翌月の月（MM）
		String shoriNengetsuTsuki = skfDateFormatUtils.addYearMonth(paramShoriNengetsu, 1);
		shoriNengetsuTsuki = shoriNengetsuTsuki.substring(4, 6);

		//経年対象月日の月＝当月処理年月翌日の月の場合のみ「経年調整処理」を行う
		if (Objects.equals(keinenTaishouTsuki, shoriNengetsuTsuki)) {
			//▼経年調整処理（今回の経年調整年月日を算出）▼
			String keinenChouseiNengappi = paramShoriNengetsu.substring(0, 4) + xmlKeinenTaishouTsukihi;

			//▼社宅基本情報マスタの更新▼
			List<Long> shatakuDtList = getShatakuKihonJyohoMasterData(keinenChouseiNengappi);

			//社宅基本情報マスタ更新フラグ
//			Boolean updateShatakuKihonFlg = false;⇒最初のデータだけ更新でよいので不要
			for (Long shatakuKanriNo : shatakuDtList) {

				//使用料パターンテーブルよりデータ取得
				List<Long> shiyouPtnDtList = skf3050Bt004GetShiyoryoPatternDataExpRepository
						.getShiyoryoPatternData(shatakuKanriNo);

				if (shiyouPtnDtList.size() > 0) {
					//社宅使用料計算部品を呼び出す
					SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp inputCalcShatakuEntity = createShatakuRentCalcInputEntityPtn1(
							paramShoriNengetsu, String.valueOf(shiyouPtnDtList.get(0)));
					SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp outputCalcShatakuEntity = skfBaseBusinessLogicUtils
							.getShatakuShiyouryouKeisan(inputCalcShatakuEntity);

					//社宅使用料計算部品内でエラーが発生した場合
					if (!NfwStringUtils.isEmpty(outputCalcShatakuEntity.getErrMessage())) {
						//社宅使用料計算部品よりエラーメッセージを出力
						LogUtils.infoByMsg("adjustKeinen, " + outputCalcShatakuEntity.getErrMessage());
						return false;
					}

					List<Long> lockResult = skf3050Bt004GetDataForUpdateExpRepository
							.getSkf3010MShatakuData(shatakuKanriNo);
					if (lockResult.size() == 0) {
						return false;
					}
					//社宅基本情報マスタ更新（同一社宅管理番号は1度のみ更新）
					updateShatakuCnt += updateShatakuKihonJyohoJikaiSanteiNengappi(
							outputCalcShatakuEntity.getJikaiSanshutsuNengappi(), paramUserId, shatakuKanriNo);
				}
			}

			//▼使用料パターンテーブルの更新▼
			List<Skf3050Bt004GetShiyoryoUpdateRecodExp> dtPtnUpdList = skf3050Bt004GetShiyoryoUpdateRecodExpRepository
					.getShiyoryoUpdateRecod(paramShoriNengetsu);

			for (Skf3050Bt004GetShiyoryoUpdateRecodExp ptnDt : dtPtnUpdList) {
				//生年月日取得(YYYYMMDD)
				String birthDay = "";
				if (ptnDt.getBirthdayYear() != 0) {
					birthDay = String.format("%04d", ptnDt.getBirthdayYear());
					birthDay += String.format("%02d", ptnDt.getBirthdayMonth());
					birthDay += String.format("%02d", ptnDt.getBirthdayDay());
				}
				//社宅使用料計算部品を呼び出す
				SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp inputCalcShatakuEntity = createShatakuRentCalcInputEntityPtn2(
						paramShoriNengetsu, String.valueOf(ptnDt.getRentalPatternId()), birthDay);
				SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp outputCalcShatakuEntity = skfBaseBusinessLogicUtils
						.getShatakuShiyouryouKeisan(inputCalcShatakuEntity);

				//社宅使用料計算部品内でエラーが発生した場合
				if (!NfwStringUtils.isEmpty(outputCalcShatakuEntity.getErrMessage())) {
					//社宅使用料計算部品よりエラーメッセージを出力
					LogUtils.infoByMsg("adjustKeinen, " + outputCalcShatakuEntity.getErrMessage());
					return false;
				}

				List<Long> lockResult = skf3050Bt004GetDataForUpdateExpRepository
						.getSkf3030TRentalPatternData(ptnDt.getShatakuKanriNo());
				if (lockResult.size() == 0) {
					return false;
				}
				//使用料パターンテーブル更新
				updateShiyouCnt += updateShiyoryoPatternData(outputCalcShatakuEntity.getSanteiKeinen(),
						outputCalcShatakuEntity.getKijunShiyouryou(), outputCalcShatakuEntity.getTanka(),
						outputCalcShatakuEntity.getShatakuShiyouryouGetsugaku(), paramUserId,
						ptnDt.getRentalPatternId(), ptnDt.getShatakuKanriNo(),
						outputCalcShatakuEntity.getKeinenZankaristu());
			}
		}
		return true;
	}

	/**
	 * 異常終了処理
	 */
	public void endAbnormalProc() {

		updateShiyouCnt = 0;
		updateShatakuCnt = 0;
		insertTsukiCnt = 0;

		outputCntLog();

		skfRollBackExpRepository.rollBack();
	}

	/**
	 * 次月領域データ作成
	 * 
	 * @param paramShoriNengetsu
	 *            処理年月
	 * @param nextShoriNengetsu
	 *            処理年月の次月
	 * @param paramCompanyCd
	 *            会社コード
	 * @param paramUserId
	 *            ユーザーID
	 * @throws ParseException
	 */
	private boolean makeNextMonthAreaData(String paramShoriNengetsu, String nextShoriNengetsu, String paramCompanyCd,
			String paramUserId) throws ParseException {

		//▼▼次月領域作成 START▼▼
		//月別使用料履歴JOINテーブル取得（年月＝処理年月）
		List<Skf3050Bt004GetTsukibetuShiyoryoRirekiJoinDataExp> tsukiJoinDtList = skf3050Bt004GetTsukibetuShiyoryoRirekiJoinDataExpRepository
				.getTsukibetuShiyoryoRirekiJoinData(paramShoriNengetsu);

		for (Skf3050Bt004GetTsukibetuShiyoryoRirekiJoinDataExp tsukiJoinDt : tsukiJoinDtList) {
			Long shatakuKanriId = tsukiJoinDt.getShatakuKanriId();
			//社宅使用料予約データテーブル取得（年月＝処理年月の翌月）1レコードのみ取得
			List<Skf3050Bt004GetShatakuShiyoryoYoyakuDataExp> shatakuYoyakuDtList = getShatakuShiyoryoYoyakuData(
					shatakuKanriId, nextShoriNengetsu);

			//次月データ作成可否フラグがFalseの場合、次ループへ
			if (!checkJigetsuDataSakusei(tsukiJoinDt, shatakuYoyakuDtList.size(), paramShoriNengetsu)) {
				continue;
			}

			//▼次月データ作成処理▼
			//月別使用料履歴テーブル取得（年月＝処理年月の翌月）
			List<Long> tsukiShiyouRirekiDtList = getTsukibetuShiyoryoRirekiData(shatakuKanriId, nextShoriNengetsu);

			//上記データが存在しない場合のみ、次月データ作成を行う
			if (tsukiShiyouRirekiDtList.size() <= 0) {
				//次月の社宅使用料調整金額の取得
				BigDecimal jigetsuShatakuShiyouryouTyouseiKingaku = getJigetsuShatakushiyouryouTyouseiKingaku(
						tsukiJoinDt, shatakuYoyakuDtList, paramShoriNengetsu);
				//次月の個人負担共益費調整金額の取得
				BigDecimal jigetsuKojinKyouekihiTyouseiKingaku = getJigetsuKojinhutanKyouekihiTyouseiKingaku(
						tsukiJoinDt, shatakuYoyakuDtList, paramShoriNengetsu);
				//次月の駐車場使用料調整金額の取得
				BigDecimal jigetsuTyushajoShiyouryouTyouseiKingaku = getJigetsuTyushajoShiyouryouTyouseiKingaku(
						tsukiJoinDt, shatakuYoyakuDtList, paramShoriNengetsu);

				Integer kyouekihiPersonTotal = tsukiJoinDt.getRirekiKyoekihiPerson()
						+ jigetsuKojinKyouekihiTyouseiKingaku.intValue();
				//月別使用料履歴テーブル登録
				insertTsukiCnt += insertTsukibetuShiyoryoRirekiData(tsukiJoinDt, nextShoriNengetsu,
						jigetsuShatakuShiyouryouTyouseiKingaku.intValue(),
						jigetsuKojinKyouekihiTyouseiKingaku.intValue(), kyouekihiPersonTotal,
						jigetsuTyushajoShiyouryouTyouseiKingaku.intValue(), paramUserId);

//				List<String> lockResult = skf3050Bt004GetDataForUpdateExpRepository
//						.getSkf3022TShatakuYoyakuData(nextShoriNengetsu);
//				if (lockResult.size() == 0) {
//					return false;
//				}
				//社宅使用料予約データ更新
				updateShatakuShiyouryouYoyakuData(shatakuKanriId, paramUserId, nextShoriNengetsu);
			}

			//▼月別所属情報履歴データ登録▼
			List<Skf3050Bt004GetTsukibetuSyozokuRirekiDataExp> tsukiShozokuYokugetsuDtList = getTsukibetuSyozokuRirekiData(
					shatakuKanriId, nextShoriNengetsu);

			//月別所属情報履歴データ（年月＝処理年月の翌月）のデータが存在しない場合のみ登録処理を行う
			if (tsukiShozokuYokugetsuDtList.size() == 0) {
				//月別所属情報履歴データ（年月＝処理年月）取得
				List<Skf3050Bt004GetTsukibetuSyozokuRirekiDataExp> tsukiShozokuTougetsuDtList = getTsukibetuSyozokuRirekiData(
						shatakuKanriId, paramShoriNengetsu);

				//当月データが存在しない場合エラー
				if (tsukiShozokuTougetsuDtList.size() <= 0) {
					LogUtils.info(MessageIdConstant.E_SKF_1106, ERRMSG_TSUKIBETSUSHOZOKU_0, shatakuKanriId.toString());
					return false;
				}

				
				//社宅社員異動履歴取得
				List<Skf3050Bt004GetShatakuShainIdoRirekiDataExp> jigetsuShainIdoDtList = getShatakuSyainIdoRirekiData(
						nextShoriNengetsu, CodeConstant.BEGINNING_END_KBN_MONTH_BEGIN, paramCompanyCd,
						tsukiJoinDt.getLedgerShainNo());
				//次月事業領域取得
				Map<String, String> retJigetsuShainIdoDt = getJigyoRyoiki(jigetsuShainIdoDtList);

				//次月事業領域コード
				String jigetsuJigyoCd = retJigetsuShainIdoDt.get(JIGYO_RYO_CD_KEY);
				//次月事業領域名
				String jigetsuJigyoName = retJigetsuShainIdoDt.get(JIGYO_RYO_NAME_KEY);

				String companyCd = "";
				String agencyCd = "";
				String agencyName = "";
				String affilCd1 = "";
				String affilName1 = "";
				String affilCd2 = "";
				String affilName2 = "";

				if (jigetsuShainIdoDtList.size() > 0) {
					if (NfwStringUtils.isEmpty(jigetsuShainIdoDtList.get(0).getRirekiAgencyCd())
							&& NfwStringUtils.isEmpty(jigetsuShainIdoDtList.get(0).getRirekiAffiliation1Cd())
							&& NfwStringUtils.isEmpty(jigetsuShainIdoDtList.get(0).getRirekiAffiliation2Cd())) {
						//組織情報が履歴から取得できない場合、組織マスタから取得する
						List<Skf3050Bt004GetShainSoshikiDataExp> soshikiDtList = getShainSoshikiData(paramCompanyCd,
								tsukiJoinDt.getLedgerShainNo());

						if (soshikiDtList.size() > 0) {
							Skf3050Bt004GetShainSoshikiDataExp soshikiDt = soshikiDtList.get(0);
							companyCd = soshikiDt.getOriginalCompanyCd();
							agencyCd = soshikiDt.getSoshikiAgencyCd();
							agencyName = soshikiDt.getSoshikiAgencyName();
							affilCd1 = soshikiDt.getSoshikiAffiliation1Cd();
							affilName1 = soshikiDt.getSoshikiAffiliation2Cd();
							affilCd2 = soshikiDt.getSoshikiAffiliation2Cd();
							affilName2 = soshikiDt.getSoshikiAffiliation2Name();
						}
					}else{
						//組織情報が履歴から取得できる場合
						companyCd = jigetsuShainIdoDtList.get(0).getCompanyCd();
						agencyCd = jigetsuShainIdoDtList.get(0).getRirekiAgencyCd();
						agencyName = jigetsuShainIdoDtList.get(0).getRirekiAgencyName();
						affilCd1 = jigetsuShainIdoDtList.get(0).getRirekiAffiliation1Cd();
						affilName1 = jigetsuShainIdoDtList.get(0).getRirekiAffiliation1Name();
						affilCd2 = jigetsuShainIdoDtList.get(0).getRirekiAffiliation2Cd();
						affilName2 = jigetsuShainIdoDtList.get(0).getRirekiAffiliation2Name();
					}
					
				}

				//社宅社員異動履歴取得
				List<Skf3050Bt004GetShatakuShainIdoRirekiDataExp> tougetsuShainIdoDtList = getShatakuSyainIdoRirekiData(
						paramShoriNengetsu, CodeConstant.BEGINNING_END_KBN_MONTH_END, paramCompanyCd,
						tsukiJoinDt.getLedgerShainNo());
				//当月事業領域取得（事業領域コード、事業領域名）
				Map<String, String> retTougetsuShainIdoDt = getJigyoRyoiki(tougetsuShainIdoDtList);

				//当月事業領域コード
				String tougetsuJigyoCd = retTougetsuShainIdoDt.get(JIGYO_RYO_CD_KEY);
				//当月事業領域名
				String tougetsuJigyoName = retTougetsuShainIdoDt.get(JIGYO_RYO_NAME_KEY);

				String companyName = getCompanyName(companyCd);
				insertTsukibetuSyozokujyohoRirekiData(shatakuKanriId, nextShoriNengetsu, companyCd, companyName,
						agencyCd, agencyName, affilCd1, affilName1, affilCd2, affilName2, jigetsuJigyoCd,
						jigetsuJigyoName, tougetsuJigyoCd, tougetsuJigyoName, paramUserId);
			}

			//▼月別備品使用料明細データ登録▼
			List<Skf3050Bt004GetTsukibetuBihinSiyoryoMeisaiDataExp> jigetsuTsukiBihinMeisaiDtList = getTsukibetuBihinSiyoryoMeisaiData(
					shatakuKanriId, nextShoriNengetsu);

			//月別備品使用料明細データ（年月＝処理年月の翌月）のデータが存在しない場合のみ登録処理を行う
			if (jigetsuTsukiBihinMeisaiDtList.size() == 0) {
				//月別備品使用料明細データ（年月＝処理年月）取得
				List<Skf3050Bt004GetTsukibetuBihinSiyoryoMeisaiDataExp> tougetsuTsukiBihinMeisaiDtList = getTsukibetuBihinSiyoryoMeisaiData(
						shatakuKanriId, paramShoriNengetsu);

				//備品現物支給額合計額の更新処理を削除
				//備品支給額合計WK
				for (Skf3050Bt004GetTsukibetuBihinSiyoryoMeisaiDataExp tougetsuTsukiBihinMeisaiDt : tougetsuTsukiBihinMeisaiDtList) {
					insertTsukibetuBihinSiyoryoMeisaiData(shatakuKanriId, nextShoriNengetsu, tougetsuTsukiBihinMeisaiDt,
							paramUserId);
				}
			}

			//▼月別駐車場履歴データ登録▼
			List<Skf3050Bt004GetTsukibetuTyusyajyoRirekiDataExp> jigetsuTsukiTyushaDtList = getTsukibetuTyusyajyoRirekiData(
					shatakuKanriId, nextShoriNengetsu);

			//月別駐車場履歴データ（年月＝処理年月の翌月）のデータが存在しない場合のみ登録処理を行う
			if (jigetsuTsukiTyushaDtList.size() == 0) {
				List<Skf3050Bt004GetTsukibetuTyusyajyoRirekiDataExp> tougetsuTsukiTyushaDtList = getTsukibetuTyusyajyoRirekiData(
						shatakuKanriId, paramShoriNengetsu);

				for (Skf3050Bt004GetTsukibetuTyusyajyoRirekiDataExp tougetsuTsukiTyushaDt : tougetsuTsukiTyushaDtList) {
					// 利用終了日設定判定
					if (!NfwStringUtils.isEmpty(tougetsuTsukiTyushaDt.getParkingEndDate())) {
						String endDataYymm = tougetsuTsukiTyushaDt.getParkingEndDate().substring(0, 6);
						// 利用終了日が処理年月と同年月か確認
						if (Integer.parseInt(endDataYymm) <= Integer.parseInt(paramShoriNengetsu)) {
							//取得した駐車場返却日≦処理年月の場合、次ループへ(当月で利用終了の為、次月データインサート対象外とする)
							continue;
						}
					}
					insertTsukibetuTyusyajyoRirekiData(shatakuKanriId, tougetsuTsukiTyushaDt, nextShoriNengetsu,
							paramUserId);
				}
			}

			//▼月別相互利用履歴データ登録▼
			List<Skf3050Bt004GetTsukibetuSougoriyoRirekiDataExp> jigetsuTsukiSogoDtList = getTsukibetuSougoriyoRirekiData(
					shatakuKanriId, nextShoriNengetsu);

			//月別相互利用履歴データ（年月＝処理年月の翌月）のデータが存在しない場合のみ登録処理を行う
			if (jigetsuTsukiSogoDtList.size() == 0) {
				List<Skf3050Bt004GetTsukibetuSougoriyoRirekiDataExp> tougetsuTsukiSogoDtList = getTsukibetuSougoriyoRirekiData(
						shatakuKanriId, paramShoriNengetsu);

				//当月データが存在しない場合エラー
				if (tougetsuTsukiSogoDtList.size() == 0) {
					LogUtils.info(MessageIdConstant.E_SKF_1106, ERRMSG_TSUKIBETSUSOGORIREKI_0,
							shatakuKanriId.toString());
					return false;
				}

				for (Skf3050Bt004GetTsukibetuSougoriyoRirekiDataExp tougetsuTsukiSogoDt : tougetsuTsukiSogoDtList) {
					insertTsukibetuSougoriyoRirekiData(shatakuKanriId, nextShoriNengetsu, tougetsuTsukiSogoDt,
							paramUserId);
				}
			}
		}
		
		//モバイルルーター機能追加対応 2021/09 add start
		// モバイルルーター使用料履歴取得
		List<Skf3050Bt004GetRouterShiyoryoRirekiJoinDataExp> routerJoinDtList = 
				skf3050Bt004GetRouterShiyoryoRirekiJoinDataExpRepository.getRouterShiyoryoRirekiJoinData(paramShoriNengetsu);

		for (Skf3050Bt004GetRouterShiyoryoRirekiJoinDataExp routerJoinDt : routerJoinDtList) {
			Long routerKnairId = routerJoinDt.getMobileRouterKanriId();

			//次月データ作成可否フラグがFalseの場合、次ループへ
			if (!checkRouterJigetsuDataSakusei(routerJoinDt, paramShoriNengetsu)) {
				continue;
			}

			// 次月のモバイルルーター使用料履歴データ取得
			Skf2100TMobileRouterRentalRirekiKey rirekiKey = new Skf2100TMobileRouterRentalRirekiKey();
			rirekiKey.setMobileRouterKanriId(routerKnairId);
			rirekiKey.setYearMonth(nextShoriNengetsu);
			Skf2100TMobileRouterRentalRireki nextRireki = skf2100TMobileRouterRentalRirekiRepository.selectByPrimaryKey(rirekiKey);
			if(nextRireki == null){
				// データなしの場合、次月領域作成
				Skf2100TMobileRouterRentalRireki setRirekiData = new Skf2100TMobileRouterRentalRireki();
				setRirekiData.setMobileRouterKanriId(routerKnairId);
				setRirekiData.setYearMonth(nextShoriNengetsu);
				setRirekiData.setMobileRouterGenbutuGoukei(routerJoinDt.getMobileRouterGenbutuGoukei());
				setRirekiData.setInsertUserId(paramUserId);
				setRirekiData.setInsertProgramId(confirmPositiveDataBatchPrgId);
				skf3050Bt004InsertRouterRirekiDataExpRepository.insertRouterRireki(setRirekiData);
			}
			
			// 次月のモバイルルーター使用料明細データ取得
			Skf3050Bt004GetRouterShiyoryoMeisaiDataExpParameter nextMeisaiParam = new Skf3050Bt004GetRouterShiyoryoMeisaiDataExpParameter();
			nextMeisaiParam.setMobileRouterKanriId(routerKnairId);
			nextMeisaiParam.setYearMonth(nextShoriNengetsu);
			List<Skf3050Bt004GetRouterShiyoryoMeisaiDataExp> nextMeisaiDL = 
					skf3050Bt004GetRouterShiyoryoMeisaiDataExpRepository.getRouterShiyoryoMeisaiData(nextMeisaiParam);
			
			if(nextMeisaiDL == null || nextMeisaiDL.size() <= 0){
				//次月データなし
				// 当月のモバイルルーター使用料明細データ取得
				Skf3050Bt004GetRouterShiyoryoMeisaiDataExpParameter meisaiParam = new Skf3050Bt004GetRouterShiyoryoMeisaiDataExpParameter();
				meisaiParam.setMobileRouterKanriId(routerKnairId);
				meisaiParam.setYearMonth(paramShoriNengetsu);
				List<Skf3050Bt004GetRouterShiyoryoMeisaiDataExp> meisaiDL = 
						skf3050Bt004GetRouterShiyoryoMeisaiDataExpRepository.getRouterShiyoryoMeisaiData(meisaiParam);
				
				if(meisaiDL != null && meisaiDL.size() > 0){
					// 取得データ分次月領域作成
					for(Skf3050Bt004GetRouterShiyoryoMeisaiDataExp meisai : meisaiDL){
						// 次月の汎用備品項目設定取得
						SkfRouterInfoUtilsGetEquipmentPaymentExp equipment = new SkfRouterInfoUtilsGetEquipmentPaymentExp();
						equipment = skfRouterInfoUtils.getEquipmentPayment(meisai.getGeneralEquipmentCd(), nextShoriNengetsu);
						if(equipment == null){
							LogUtils.info(MessageIdConstant.E_SKF_1106, "汎用備品項目設定",
									routerKnairId.toString());
							return false;
						}
						Skf2100TMobileRouterRentalRirekiMeisai inData = new Skf2100TMobileRouterRentalRirekiMeisai();
						inData.setMobileRouterKanriId(routerKnairId);
						inData.setYearMonth(nextShoriNengetsu);
						inData.setGeneralEquipmentCd(meisai.getGeneralEquipmentCd());
						inData.setMobileRouterGenbutsuGaku(equipment.getEquipmentPayment());
						inData.setMobileRouterApplKbn(meisai.getMobileRouterApplKbn());
						inData.setMobileRouterReturnKbn(meisai.getMobileRouterReturnKbn());
						inData.setInsertUserId(paramUserId);
						inData.setInsertProgramId(confirmPositiveDataBatchPrgId);
						skf3050Bt004InsertRouterRirekiDataExpRepository.insertRouterRirekiMeisai(inData);
					}
				}else{
					LogUtils.info(MessageIdConstant.E_SKF_1106, ERRMSG_ROUTERMEISAI_0,
							routerKnairId.toString());
					return false;
				}
			}
			


		}
		//モバイルルーター機能追加対応 2021/09 add end
		//▲▲▲次月領域作成 END▲▲▲

		return true;
	}

	/**
	 * 使用料再計算
	 * 
	 * @param nextShoriNengetsu
	 *            処理年月の次月
	 * @param paramUserId
	 *            ユーザーID
	 * @throws ParseException
	 */
	private boolean reCalcShatakuShiyoryo(String nextShoriNengetsu, String paramUserId) throws ParseException {

		//▼▼▼使用料再計算 START▼▼▼
		List<Skf3050Bt004GetTsukibetuShiyoryoRirekiJoinDataExp> jigetsuTsukiJoinList = skf3050Bt004GetTsukibetuShiyoryoRirekiJoinDataExpRepository
				.getTsukibetuShiyoryoRirekiJoinData(nextShoriNengetsu);

		for (Skf3050Bt004GetTsukibetuShiyoryoRirekiJoinDataExp jigetsuTsukiJoin : jigetsuTsukiJoinList) {
			Long shatakuKanriId = jigetsuTsukiJoin.getShatakuKanriId();
			//社宅管理台帳相互利用基本（社宅賃貸料、駐車場料金）の取得
			List<Skf3050Bt004GetShatakuKanriDaityoSogoriyoDataExp> sougoDtList = skf3050Bt004GetShatakuKanriDaityoSogoriyoDataExpRepository
					.getShatakuKanriDaityoSogoriyoData(shatakuKanriId);

			if (sougoDtList.size() <= 0) {
				LogUtils.info(MessageIdConstant.E_SKF_1106, ERRMSG_TSUKIBETSUSOGO_0,
						ERRMSG_TSUKIBETSUSOGO_1 + CodeConstant.COLON + shatakuKanriId.toString());
				return false;
			}

			//▼社宅使用料月額の取得▼
			SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp inputCalcShatakuEntity = createShatakuRentCalcInputEntityPtn3(
					nextShoriNengetsu, jigetsuTsukiJoin, sougoDtList.get(0).getRent());
			//社宅使用料計算部品呼び出し
			SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp outputCalcShatakuEntity = skfBaseBusinessLogicUtils
					.getShatakuShiyouryouKeisan(inputCalcShatakuEntity);

			//社宅使用料計算部品内でエラーが発生した場合
			if (!NfwStringUtils.isEmpty(outputCalcShatakuEntity.getErrMessage())) {
				//社宅使用料計算部品よりエラーメッセージを出力
				LogUtils.infoByMsg("reCalcShatakuShiyoryo, " + outputCalcShatakuEntity.getErrMessage());
				return false;
			}

			BigDecimal shatakuShiyoryoGetsugaku = outputCalcShatakuEntity.getShatakuShiyouryouGetsugaku();
			//▼社宅使用料日割の取得▼ ※小数点以下切り捨て
			BigDecimal shatakuShiyouryouHiwari = getShatakuShiyoryoHiwari(nextShoriNengetsu,
					jigetsuTsukiJoin.getLedgerNyukyoDate(), jigetsuTsukiJoin.getLedgerTaikyoDate(),
					shatakuShiyoryoGetsugaku);
			
			//▼駐車場使用料月額、日割額の算出（駐車場貸与番号：1）▼
			//駐車場貸与番号1の駐車場使用料月額（1レコード取得：月別駐車場履歴テーブル.駐車場貸与番号=1）
			Skf3030TParkingRireki tsukiData1 = getTsukibetuTyusyajyoRirekiDataSyutokuByLendNo(shatakuKanriId,
					Long.parseLong(CodeConstant.PARKING_LEND_NO_FIRST), nextShoriNengetsu);

			BigDecimal chushajoShiyoryoGetsugaku1 = BigDecimal.ZERO;
			BigDecimal chushajouShiyouryouHiwari1 = BigDecimal.ZERO;

			if (tsukiData1 != null && tsukiData1.getParkingKanriNo() > 0L) {
				//▼1台目の駐車場使用料月額▼
				SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp inputCalcChushaEntity = createShatakuRentCalcInputEntityPtn4(
						nextShoriNengetsu, jigetsuTsukiJoin, sougoDtList.get(0).getParkingRental(),
						tsukiData1.getParkingKanriNo());
				//社宅使用料計算部品呼び出し（区画情報より駐車場使用料月額を取得）
				SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp outputCalcChushaEntity = skfBaseBusinessLogicUtils
						.getShatakuShiyouryouKeisan(inputCalcChushaEntity);

				//社宅使用料計算部品内でエラーが発生した場合
				if (!NfwStringUtils.isEmpty(outputCalcChushaEntity.getErrMessage())) {
					LogUtils.infoByMsg("reCalcShatakuShiyoryo, " + outputCalcChushaEntity.getErrMessage());
					return false;
				}

				chushajoShiyoryoGetsugaku1 = outputCalcChushaEntity.getChushajouShiyoryou();
				//▼1台目の駐車場使用料日割額▼ ※少数点以下切り捨て
				chushajouShiyouryouHiwari1 = getChushajoShiyoryoHiwari(nextShoriNengetsu,
						tsukiData1.getParkingStartDate(), tsukiData1.getParkingEndDate(), chushajoShiyoryoGetsugaku1);
			}

			//▼駐車場使用料月額、日割額の算出（駐車場貸与番号：2）▼
			//駐車場貸与番号2の駐車場使用料月額（1レコード取得：月別駐車場履歴テーブル.駐車場貸与番号=2）
			Skf3030TParkingRireki tsukiData2 = getTsukibetuTyusyajyoRirekiDataSyutokuByLendNo(shatakuKanriId,
					Long.parseLong(CodeConstant.PARKING_LEND_NO_SECOND), nextShoriNengetsu);

			BigDecimal chushajoShiyoryoGetsugaku2 = BigDecimal.ZERO;
			BigDecimal chushajouShiyouryouHiwari2 = BigDecimal.ZERO;

			if (tsukiData2 != null && tsukiData2.getParkingKanriNo() > 0L) {
				//▼2台目の駐車場使用料月額▼
				SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp inputCalcChushaEntity = createShatakuRentCalcInputEntityPtn4(
						nextShoriNengetsu, jigetsuTsukiJoin, sougoDtList.get(0).getParkingRental(),
						tsukiData2.getParkingKanriNo());
				//社宅使用料計算部品呼び出し（区画情報より駐車場使用料月額を取得）
				SkfBaseBusinessLogicUtilsShatakuRentCalcOutputExp outputCalcChushaEntity = skfBaseBusinessLogicUtils
						.getShatakuShiyouryouKeisan(inputCalcChushaEntity);

				//社宅使用料計算部品内でエラーが発生した場合
				if (!NfwStringUtils.isEmpty(outputCalcChushaEntity.getErrMessage())) {
					LogUtils.infoByMsg("reCalcShatakuShiyoryo, " + outputCalcChushaEntity.getErrMessage());
					return false;
				}

				chushajoShiyoryoGetsugaku2 = outputCalcChushaEntity.getChushajouShiyoryou();
				//▼2台目の駐車場使用料日割額▼ ※少数点以下切り捨て
				chushajouShiyouryouHiwari2 = getChushajoShiyoryoHiwari(nextShoriNengetsu,
						tsukiData2.getParkingStartDate(), tsukiData2.getParkingEndDate(), chushajoShiyoryoGetsugaku2);
			}

			//▼▼月別使用料履歴の更新▼▼
			//個人負担共益費調整金額を算出
			//個人負担共益費月額の取得
//			int kojinHutankyouekihiGetsugaku = 0;
			// 共益費日割計算対応 2021/5/14 edit start
			// 社宅利用料計算情報引数
			SkfKyoekihiCalcUtilsInputExp inputEntity = new SkfKyoekihiCalcUtilsInputExp();
			// 処理年月
			inputEntity.setYearMonth(nextShoriNengetsu);
			// 共益費支払月
			inputEntity.setKyoekihiPayMonth(jigetsuTsukiJoin.getRirekiKyoekihiPayMonth());
			// 個人負担共益費月額
			inputEntity.setKyoekihiPerson(jigetsuTsukiJoin.getRirekiKyoekihiPerson().toString());
			// 入居予定日
			inputEntity.setNyukyoDate(jigetsuTsukiJoin.getLedgerNyukyoDate());
			// 退居予定日
			inputEntity.setTaikyoDate(jigetsuTsukiJoin.getLedgerTaikyoDate());
			// 社宅管理台帳ID
			inputEntity.setShatakuKanriId(shatakuKanriId.toString());
			
			// 使用料計算結果取得
			SkfKyoekihiCalcUtilsOutputExp outputEntity = new SkfKyoekihiCalcUtilsOutputExp();
			try {
				outputEntity = skfKyoekihiCalcUtils.getKyoekihiKeisan(inputEntity);
			} catch (ParseException e) {
				outputEntity.setErrMessage(e.getMessage());
			}
			
			if (!NfwStringUtils.isEmpty(outputEntity.getErrMessage())) {
				LogUtils.infoByMsg("reCalcShatakuShiyoryo, " + outputEntity.getErrMessage());
				return false;
			}
			
//			if (!NfwStringUtils.isEmpty(jigetsuTsukiJoin.getLedgerTaikyoDate())) {
//				BigDecimal taikyoDateYymm = new BigDecimal(jigetsuTsukiJoin.getLedgerTaikyoDate().substring(0, 6));
//				BigDecimal decimalNextShoriNengetsu = new BigDecimal(nextShoriNengetsu);
//
//				if (taikyoDateYymm.compareTo(decimalNextShoriNengetsu) <= 0) {
//					//次月以前に退居している場合
//					kojinHutankyouekihiGetsugaku = 0;
//				} else {
//					kojinHutankyouekihiGetsugaku = jigetsuTsukiJoin.getRirekiKyoekihiPerson();
//				}
//
//			} else {
//				kojinHutankyouekihiGetsugaku = jigetsuTsukiJoin.getRirekiKyoekihiPerson();
//			}
			// 共益費日割計算対応 2021/5/14 edit end
			
			List<String> lockResult = skf3050Bt004GetDataForUpdateExpRepository
					.getSkf3030TShatakuRentalRireki(nextShoriNengetsu);
			if (lockResult.size() == 0) {
				return false;
			}

			// 共益費日割計算対応 2021/5/14 edit start
//			int kyoekihiTotal = kojinHutankyouekihiGetsugaku + jigetsuTsukiJoin.getRirekiKyoekihiPersonAdjust();
			int kyoekihiTotal = skfKyoekihiCalcUtils.getKyoekihiPayAfter(jigetsuTsukiJoin.getRirekiKyoekihiPersonAdjust()
					,outputEntity.getKyoekihiMonth(),outputEntity.getNyukyoKasan(),outputEntity.getTaikyoKasan());
			// 共益費日割計算対応 2021/5/14 edit end
			updateTsukibetuShiyoryoRirekiData(shatakuShiyoryoGetsugaku.intValue(), shatakuShiyouryouHiwari.intValue(),
					kyoekihiTotal, chushajoShiyoryoGetsugaku1.intValue(), chushajoShiyoryoGetsugaku2.intValue(),
					chushajouShiyouryouHiwari1.intValue(), chushajouShiyouryouHiwari2.intValue(), paramUserId,
					shatakuKanriId, nextShoriNengetsu);
		}
		//▲▲▲使用料再計算 END▲▲▲

		return true;
	}

	/**
	 * 社宅部屋情報マスタ取得
	 * 
	 * @param lendJokyo
	 *            貸与状況
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 社宅部屋情報
	 */
	private List<Skf3050Bt004GetShatakuHeyaDataExp> getShatakuHeyaData(String lendJokyo, String shoriNengetsu) {

		Skf3050Bt004GetShatakuHeyaDataExpParameter param = new Skf3050Bt004GetShatakuHeyaDataExpParameter();

		if (lendJokyo == null) {
			param.setLendJokyo(null);
		} else {
			param.setLendJokyo(lendJokyo);
		}

		param.setShoriNengetsu(shoriNengetsu);

		List<Skf3050Bt004GetShatakuHeyaDataExp> outData = skf3050Bt004GetShatakuHeyaDataExpRepository
				.getShatakuHeyaData(param);

		return outData;
	}

	/**
	 * 社宅部屋情報テーブル更新
	 * 
	 * @param lendJokyo
	 *            貸与状況
	 * @param updateUser
	 *            更新ユーザ
	 * @param shatakuKanriBangou
	 *            社宅管理番号
	 * @param shatakuHeyaKanriBangou
	 *            社宅部屋管理番号
	 */
	private void updateShatakuHeyaData(String lendJokyo, String updateUser, Long shatakuKanriBangou,
			Long shatakuHeyaKanriBangou) {

		Skf3050Bt004UpdateShatakuHeyaDataExpParameter param = new Skf3050Bt004UpdateShatakuHeyaDataExpParameter();

		if (lendJokyo == null) {
			param.setLendJokyo(null);
		} else {
			param.setLendJokyo(lendJokyo);
		}

		param.setShatakuKanriNo(shatakuKanriBangou);
		param.setShatakuRoomKanriNo(shatakuHeyaKanriBangou);
		param.setUpdateUser(updateUser);
		param.setUpdateProgramId(confirmPositiveDataBatchPrgId);

		skf3050Bt004UpdateShatakuHeyaDataExpRepository.updateShatakuHeyaData(param);

	}

	/**
	 * 月別駐車場情報テーブル、社宅駐車場区画情報マスタ取得
	 * 
	 * @param lendJokyo
	 *            貸与状況
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 月別駐車場情報、社宅駐車場区画情報
	 */
	private List<Skf3050Bt004GetTsukibetsuTyusyajyoBlockRirekiDataExp> getTsukibetsuTyusyajyoBlockRirekiData(
			String lendJokyo, String shoriNengetsu) {

		Skf3050Bt004GetTsukibetsuTyusyajyoBlockRirekiDataExpParameter param = new Skf3050Bt004GetTsukibetsuTyusyajyoBlockRirekiDataExpParameter();
		param.setShoriNengetsu(shoriNengetsu);

		if (lendJokyo == null) {
			param.setParkingLendJokyo(null);
		} else {
			param.setParkingLendJokyo(lendJokyo);
		}

		List<Skf3050Bt004GetTsukibetsuTyusyajyoBlockRirekiDataExp> outData = skf3050Bt004GetTsukibetsuTyusyajyoBlockRirekiDataExpRepository
				.getTsukibetsuTyusyajyoBlockRirekiData(param);

		return outData;
	}

	/**
	 * 社宅駐車場区画情報テーブル更新
	 * 
	 * @param lendJokyo
	 *            貸与状況
	 * @param updateUser
	 *            更新ユーザ
	 * @param shatakuKanriBangou
	 *            社宅管理番号
	 * @param parkingKanriBangou
	 *            駐車場管理番号
	 */
	private void updateShatakuTyusyajyoKukakuJyohoTaiyojyokyo(String lendJokyo, String updateUser,
			Long shatakuKanriBangou, Long parkingKanriBangou) {

		Skf3050Bt004UpdateshatakuTyusyajyoKukakuJyohoTaiyojyokyoExpParameter param = new Skf3050Bt004UpdateshatakuTyusyajyoKukakuJyohoTaiyojyokyoExpParameter();
		param.setShatakuKanriNo(shatakuKanriBangou);
		param.setParkingKanriNo(parkingKanriBangou);
		param.setUpdateUser(updateUser);

		if (lendJokyo == null) {
			param.setParkingLendJokyo(null);
		} else {
			param.setParkingLendJokyo(lendJokyo);
		}
		param.setUpdateProgramId(confirmPositiveDataBatchPrgId);

		skf3050Bt004UpdateshatakuTyusyajyoKukakuJyohoTaiyojyokyoExpRepository
				.updateshatakuTyusyajyoKukakuJyohoTaiyojyokyo(param);

	}

	/**
	 * 対象のDBテーブルデータを削除する。
	 * 
	 * @return 処理結果
	 */
	private boolean deleteData() {

		//削除基準日取得
//		String delDate = getDeleteKijunbi();

//		//▼入退居予定データ削除
//		List<Skf3050Bt004GetNyutaikyoYoteiInfoExp> nyutaikyoYoteiInfoList = skf3050Bt004GetNyutaikyoYoteiInfoExpRepository
//				.getNyutaikyoYoteiInfo(delDate);

		//'①提示データとのひも付けを確認し、なければ削除基準日より古いデータ削除を行う
		//データが存在する場合、データ件数分繰り返し処理を行う。
//		if (nyutaikyoYoteiInfoList.size() > 0) {
//			for (Skf3050Bt004GetNyutaikyoYoteiInfoExp nyutaikyoYoteiInfo : nyutaikyoYoteiInfoList) {
//				List<String> lockResult = skf3050Bt004GetDataForUpdateExpRepository
//						.getSkf3021TNyutaikyoYoteiData(nyutaikyoYoteiInfo.getShainNo());
//
//				if (lockResult.size() != 0) {
//					//提示データが作成済みだったら
//					if (CodeConstant.TEIJI_CREATE_KBN_SAKUSEI_SUMI.equals(nyutaikyoYoteiInfo.getTeijiCreateKbn())) {
//						int teijiNyutaikyoYoteiDateCnt = getTeijiNyutaikyoYoteiDateCnt(delDate,
//								nyutaikyoYoteiInfo.getShainNo(), nyutaikyoYoteiInfo.getNyutaikyoKbn(),
//								nyutaikyoYoteiInfo.getTeijiNo());
//
//						//入退居予定データの削除
//						if (teijiNyutaikyoYoteiDateCnt > 0) {
//							int deleCnt = deleteNyutaikyoYoteiData2(delDate, nyutaikyoYoteiInfo.getShainNo(),
//									nyutaikyoYoteiInfo.getNyutaikyoKbn(), nyutaikyoYoteiInfo.getTeijiNo());
//							if (deleCnt < 0) {
//								return false;
//							}
//						}
//
//					} else {
//						//入退居予定データの削除
//						int deleCnt = deleteNyutaikyoYoteiData1(delDate, nyutaikyoYoteiInfo.getShainNo(),
//								nyutaikyoYoteiInfo.getNyutaikyoKbn());
//						if (deleCnt < 0) {
//							return false;
//						}
//					}
//				} else {
//					return false;
//				}
//			}
//		}

		//②提示データのステータスが“承認”と紐づくデータを削除する
//		List<String> lockNyutaikyoDataResult = skf3050Bt004GetDataForUpdateExpRepository.getDeleteNyutaikyoData();
//		if (lockNyutaikyoDataResult.size() == 0) {
//			return false;
//		}

		skf3050Bt004DeleteNyutaikyoDataExpRepository.deleteNyutaikyoData();

		//▼提示データ削除
		//①削除基準日より古いデータを削除する
		//提示データ取得
//		List<Skf3050Bt004GetTeijiDataInfoExp> teijiDataInfoList = skf3050Bt004GetTeijiDataInfoExpRepository
//				.getTeijiDataInfo(delDate);

//		if (teijiDataInfoList.size() > 0) {
//			for (Skf3050Bt004GetTeijiDataInfoExp teijiDataInfo : teijiDataInfoList) {
//				List<Long> lockBihinDataResult = skf3050Bt004GetDataForUpdateExpRepository
//						.getSkf3022TTeijiBihinData(teijiDataInfo.getTeijiNo());
//				//①提示備品データの削除
//				if (lockBihinDataResult.size() != 0) {
//					int teijiBihinDataDeleCnt = skf3050Bt004DeleteTeijiBihinDataExpRepository
//							.deleteTeijiBihinData(teijiDataInfo.getTeijiNo());
//					if (teijiBihinDataDeleCnt < 0) {
//						return false;
//					}
//				}
//
//				//②提示データの削除
//				List<Long> lockTeijiDataResult = skf3050Bt004GetDataForUpdateExpRepository
//						.getSkf3022TTeijiData(teijiDataInfo.getTeijiNo());
//				if (lockTeijiDataResult.size() != 0) {
//
//					int teijiDataDeleCnt = skf3022TTeijiDataRepository.deleteByPrimaryKey(teijiDataInfo.getTeijiNo());
//					if (teijiDataDeleCnt < 0) {
//						return false;
//					}
//				}
//			}
//		}

		//②提示データのステータスが“承認”のデータを削除する
//		List<String> lockTeijiDataResult = skf3050Bt004GetDataForUpdateExpRepository.getDeleteTeijiData();
//		if (lockTeijiDataResult.size() > 0) {
//			return false;
//		}
		skf3050Bt004DeleteTeijiDataExpRepository.deleteTeijiData();

		//▼転任者調書データ
		//①削除基準日より古いデータを削除する
//		List<String> lockZengetsuTenninshaResult = skf3050Bt004GetDataForUpdateExpRepository
//				.getDeleteZengetsuTenninsha(delDate);
//		if (lockZengetsuTenninshaResult.size() == 0) {
//			return false;
//		}
//		skf3050Bt004DeleteZengetsuTenninshaExpRepository.deleteZengetsuTenninsha(delDate);

		//②提示データのステータスがすべて“承認”のデータを削除する
//		List<String> lockTenninshaDataResult = skf3050Bt004GetDataForUpdateExpRepository.getDeleteTenninshaData();
//		if (lockTenninshaDataResult.size() == 0) {
//			return false;
//		}
		skf3050Bt004DeleteTenninshaDataExpRepository.deleteTenninshaData();
		
		// 202109 モバイルルーター機能追加対応 add start
		// 承認済みモバイルルーター貸出予定データ削除
		skf3050Bt004DeleteRouterYoteiDataExpRepository.deleteRouterYoteiData();
		// 202109 モバイルルーター機能追加対応 add end

		return true;
	}

	/**
	 * 月次処理管理テーブルの更新
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @param updateUser
	 *            更新ユーザー
	 */
	private void updateGetsujiSyoriKanri(String shoriNengetsu, String updateUser) {

		Skf3050Bt004UpdateGetsujiSyoriKanriExpParameter param = new Skf3050Bt004UpdateGetsujiSyoriKanriExpParameter();
		param.setCycleBillingYymm(shoriNengetsu);
		param.setUpdateUser(updateUser);
		param.setUpdateProgramId(confirmPositiveDataBatchPrgId);

		skf3050Bt004UpdateGetsujiSyoriKanriExpRepository.updateGetsujiSyoriKanri(param);

	}

	/**
	 * 月次処理管理テーブルへのデータ追加
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @param insertUser
	 *            登録ユーザー
	 */
	private void insertGetsujiSyoriKanriData(String shoriNengetsu, String insertUser) {

		Skf3050Bt004InsertGetsujiSyoriKanriDataExpParameter param = new Skf3050Bt004InsertGetsujiSyoriKanriDataExpParameter();
		param.setCycleBillingYymm(shoriNengetsu);
		param.setInsertUserId(insertUser);
		param.setBillingActDate("0");
		param.setBillingActKbn("0");
		// 202109 モバイルルーター機能追加対応 add start
		param.setMobileRouterBillingActDate("0");
		param.setMobileRouterBillingActKbn("0");
		// 202109 モバイルルーター機能追加対応 add end
		param.setLinkdataCreateDate("0");
		param.setLinkdataCreateKbn("0");
		param.setLinkdataCommitDate("0");
		param.setLinkdataCommitKbn("0");
		param.setInsertProgramId(confirmPositiveDataBatchPrgId);

		skf3050Bt004InsertGetsujiSyoriKanriDataExpRepository.insertGetsujiSyoriKanriData(param);
	}

	/**
	 * データ登録、更新件数をログに出力する。
	 */
	private void outputCntLog() {
		LogUtils.info(MessageIdConstant.I_SKF_1030, "使用料パターン", String.valueOf(updateShiyouCnt));
		LogUtils.info(MessageIdConstant.I_SKF_1030, "社宅基本情報", String.valueOf(updateShatakuCnt));
		LogUtils.info(MessageIdConstant.I_SKF_1030, "月別使用料履歴", String.valueOf(insertTsukiCnt));
	}

	/**
	 * 社宅基本情報マスターテーブルのデータ取得
	 * 
	 * @param konkaiKeinenChouseiNengappi
	 *            今回経年調整年月日
	 * @return 社宅基本情報マスターテーブルのデータ
	 */
	private List<Long> getShatakuKihonJyohoMasterData(String konkaiKeinenChouseiNengappi) {

		String param = konkaiKeinenChouseiNengappi;
		if (konkaiKeinenChouseiNengappi == null) {
			param = null;
		}

		List<Long> outData = skf3050Bt004GetShatakuKihonJyohoMasterDataExpRepository
				.getShatakuKihonJyohoMasterData(param);

		return outData;
	}

	/**
	 * 社宅使用料計算のEntityを作成する。（パターン1）
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @param rentalPtnId
	 *            レンタルパターンID
	 * @return Entity
	 */
	private SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp createShatakuRentCalcInputEntityPtn1(String shoriNengetsu,
			String rentalPtnId) {

		SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp rtnEntity = new SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp();
		String nextShoriNengetsu = skfDateFormatUtils.addYearMonth(shoriNengetsu, 1);
		rtnEntity.setShoriNengetsu(nextShoriNengetsu);
		rtnEntity.setShoriKbn("2");
		rtnEntity.setShiyouryouPatternId(rentalPtnId);
		rtnEntity.setYakuinKbn(CodeConstant.NASHI);

		return rtnEntity;
	}

	/**
	 * 社宅基本情報マスタ更新
	 * 
	 * @param nextCalcDate
	 *            次回算定年月日
	 * @param updateUser
	 *            更新ユーザ
	 * @param shatakuKanriBangou
	 *            社宅管理番号
	 * @return 更新件数
	 */
	private Integer updateShatakuKihonJyohoJikaiSanteiNengappi(String nextCalcDate, String updateUser,
			Long shatakuKanriBangou) {

		Skf3050Bt004UpdateShatakuKihonJyohoJikaiSanteiNengappiExpParameter param = new Skf3050Bt004UpdateShatakuKihonJyohoJikaiSanteiNengappiExpParameter();

		if (nextCalcDate == null) {
			param.setNextCalculateDate(null);
		} else {
			param.setNextCalculateDate(nextCalcDate);
		}

		if (updateUser == null) {
			param.setUpdateUser(null);
		} else {
			param.setUpdateUser(updateUser);
		}

		param.setShatakuKanriNo(shatakuKanriBangou);
		param.setUpdateProgramId(confirmPositiveDataBatchPrgId);

		Integer updateCnt = skf3050Bt004UpdateShatakuKihonJyohoJikaiSanteiNengappiExpRepository
				.updateShatakuKihonJyohoJikaiSanteiNengappi(param);

		return updateCnt;
	}

	/**
	 * 社宅使用料計算のEntityを作成する。（パターン2）
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @param rentalPtnId
	 *            レンタルパターンID
	 * @return Entity
	 */
	private SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp createShatakuRentCalcInputEntityPtn2(String shoriNengetsu,
			String rentalPtnId, String birthDay) {

		SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp rtnEntity = new SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp();
		String paramShoriNengetsu = skfDateFormatUtils.addYearMonth(shoriNengetsu, 1);
		rtnEntity.setShoriNengetsu(paramShoriNengetsu);
		rtnEntity.setShoriKbn("2");
		rtnEntity.setShiyouryouPatternId(rentalPtnId);
		rtnEntity.setYakuinKbn(CodeConstant.NASHI);
		rtnEntity.setSeinengappi(birthDay);

		return rtnEntity;
	}

	/**
	 * 使用料パターンテーブルのデータ更新
	 * 
	 * @param aging
	 *            経年
	 * @param baseRental
	 *            基本使用料
	 * @param price
	 *            単価
	 * @param rental
	 *            社宅使用料月額
	 * @param updateUser
	 *            更新ユーザ
	 * @param rentalPtnId
	 *            使用料パターンID
	 * @param shatakuKanriBangou
	 *            社宅管理番号
	 * @param zankaritsu
	 *            残価率
	 * @return 更新件数
	 */
	private Integer updateShiyoryoPatternData(Integer aging, BigDecimal baseRental, BigDecimal price, BigDecimal rental,
			String updateUser, Long rentalPtnId, Long shatakuKanriBangou, BigDecimal zankaritsu) {

		Skf3050Bt004UpdateShiyoryoPatternDataExpParameter param = new Skf3050Bt004UpdateShiyoryoPatternDataExpParameter();

		if (aging == null) {
			param.setAging(null);
		} else {
			param.setAging(aging);
		}

		if (baseRental == null) {
			param.setBaseRental(null);
		} else {
			param.setBaseRental(baseRental);
		}

		if (price == null) {
			param.setPrice(null);
		} else {
			param.setPrice(price);
		}

		if (rental == null) {
			param.setRental(null);
		} else {
			param.setRental(rental);
		}

		if (zankaritsu == null) {
			param.setAgingResidualRate(null);
		} else {
			param.setAgingResidualRate(zankaritsu);
		}

		if (updateUser == null) {
			param.setUpdateUser(null);
		} else {
			param.setUpdateUser(updateUser);
		}

		param.setRentalPatternId(rentalPtnId);
		param.setShatakuKanriNo(shatakuKanriBangou);
		param.setUpdateProgramId(confirmPositiveDataBatchPrgId);

		Integer updateCnt = skf3050Bt004UpdateShiyoryoPatternDataExpRepository.updateShiyoryoPatternData(param);

		return updateCnt;
	}

	/**
	 * 社宅使用料予約テーブルのデータ取得
	 * 
	 * @param shatakuKanriId
	 *            社宅管理台帳ID
	 * @param shoriNengetsu
	 *            年月
	 * @return 月別使用料履歴テーブルとの結合データ
	 */
	private List<Skf3050Bt004GetShatakuShiyoryoYoyakuDataExp> getShatakuShiyoryoYoyakuData(Long shatakuKanriId,
			String shoriNengetsu) {

		Skf3050Bt004GetShatakuShiyoryoYoyakuDataExpParameter param = new Skf3050Bt004GetShatakuShiyoryoYoyakuDataExpParameter();

		if (shatakuKanriId == null) {
			param.setShatakuKanriId(null);
		} else {
			param.setShatakuKanriId(shatakuKanriId);
		}

		param.setYearMonth(shoriNengetsu);

		List<Skf3050Bt004GetShatakuShiyoryoYoyakuDataExp> outData = skf3050Bt004GetShatakuShiyoryoYoyakuDataExpRepository
				.getShatakuShiyoryoYoyakuData(param);

		return outData;
	}

	/**
	 * 次月データ作成可否チェック
	 * 
	 * @param tsukiJoinDt
	 *            月別使用料履歴データ
	 * @param shatakuYoyakuDtCnt
	 *            社宅使用料予約データ件数
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 次月データ作成可否フラグ（True:作成する, False:作成しない）
	 */
	private boolean checkJigetsuDataSakusei(Skf3050Bt004GetTsukibetuShiyoryoRirekiJoinDataExp tsukiJoinDt,
			int shatakuYoyakuDtCnt, String shoriNengetsu) {

		//次月データ作成フラグ
		boolean jigetsuFlg = false;

		//退去日が処理年月。提示データが退居の申請ステータスが「承認」。備品貸与区分が有で備品提示ステータスが「承認以外の申請」の社宅提示データ取得
		BigDecimal tsukiJoinDtCnt = getTeijiJoinDataCnt(shoriNengetsu, tsukiJoinDt.getLedgerShainNo());

		//以下条件に当てはまる場合のみ後続処理を行う
		if (BigDecimal.ZERO.compareTo(tsukiJoinDtCnt) < 0) {
			//①退居日が処理年月。提示データが退居の申請ステータスが「承認」、備品貸与区分が「有」で備品提示ステータスが「承認以外」の社宅提示データが存在する場合
			jigetsuFlg = true;

		} else if (NfwStringUtils.isEmpty(tsukiJoinDt.getLedgerTaikyoDate())) {
			//②退居日がNULL
			jigetsuFlg = true;

		} else {
			String taikyoYymm = tsukiJoinDt.getLedgerTaikyoDate().substring(0, 6);
			String nextShoriNengetsu = skfDateFormatUtils.addYearMonth(shoriNengetsu, 1);

			if (Integer.parseInt(taikyoYymm) >= Integer.parseInt(nextShoriNengetsu)) {
				////②退居日が処理年月+1以上
				jigetsuFlg = true;

			} else if (shatakuYoyakuDtCnt > 0) {
				//③社宅使用料予約データが存在する場合
				jigetsuFlg = true;
			}
		}

		return jigetsuFlg;
	}

	/**
	 * 月別使用料履歴テーブルのデータ取得
	 * 
	 * @param shatakuKanriId
	 *            社宅管理台帳ID
	 * @param shoriNengetsu
	 *            年月
	 * @return 月別使用料履歴テーブルデータ
	 */
	private List<Long> getTsukibetuShiyoryoRirekiData(Long shatakuKanriId, String shoriNengetsu) {

		Skf3050Bt004GetTsukibetuShiyoryoRirekiDataExpParameter param = new Skf3050Bt004GetTsukibetuShiyoryoRirekiDataExpParameter();

		param.setShatakuKanriId(shatakuKanriId);
		param.setYearMonth(shoriNengetsu);

		List<Long> outData = skf3050Bt004GetTsukibetuShiyoryoRirekiDataExpRepository
				.getTsukibetuShiyoryoRirekiData(param);

		return outData;
	}

	/**
	 * 次月の社宅使用料調整金額の取得
	 * 
	 * @param tsukiJoinDt
	 *            月別使用料履歴レコード
	 * @param shatakuYoyakuDtList
	 *            社宅使用料予約データ
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 次月の社宅使用料調整金
	 */
	private BigDecimal getJigetsuShatakushiyouryouTyouseiKingaku(
			Skf3050Bt004GetTsukibetuShiyoryoRirekiJoinDataExp tsukiJoinDt,
			List<Skf3050Bt004GetShatakuShiyoryoYoyakuDataExp> shatakuYoyakuDtList, String shoriNengetsu) {

		BigDecimal jigetsuShatakuShiyouryouTyouseiKingaku = BigDecimal.ZERO;
		String taikyoDate = tsukiJoinDt.getLedgerTaikyoDate();

		if (!NfwStringUtils.isEmpty(taikyoDate)) {
			String taikyoYymm = taikyoDate.substring(0, 6);

			if (Integer.parseInt(taikyoYymm) <= Integer.parseInt(shoriNengetsu)) {
				//退居日が当月処理月以前の場合
				if (shatakuYoyakuDtList.size() == 0) {
					jigetsuShatakuShiyouryouTyouseiKingaku = BigDecimal.ZERO;
				} else {
					//社宅使用料予約データ.社宅使用料調整金額を設定
					jigetsuShatakuShiyouryouTyouseiKingaku = BigDecimal
							.valueOf(shatakuYoyakuDtList.get(0).getRentalAdjust());
				}
			}else{
				String nextShoriNengetsu = skfDateFormatUtils.addYearMonth(shoriNengetsu, 1);
				if (Integer.parseInt(taikyoYymm) >= Integer.parseInt(nextShoriNengetsu)) {
					//当月処理月の翌月以降の場合
					if (shatakuYoyakuDtList.size() == 0) {
						//当月処理月の月別使用料履歴.社宅使用料調整金額を設定
						jigetsuShatakuShiyouryouTyouseiKingaku = BigDecimal.valueOf(tsukiJoinDt.getRirekiRentalAdjust());

					} else {
						//社宅使用料予約データ.社宅使用料調整金額を設定
						jigetsuShatakuShiyouryouTyouseiKingaku = BigDecimal.valueOf(shatakuYoyakuDtList.get(0).getRentalAdjust());
					}
				}
				
			}
		}else{
			//退居日が未入力
			if (shatakuYoyakuDtList.size() == 0) {
				//当月処理月の月別使用料履歴.社宅使用料調整金額を設定
				jigetsuShatakuShiyouryouTyouseiKingaku = BigDecimal.valueOf(tsukiJoinDt.getRirekiRentalAdjust());
			} else {
				//社宅使用料予約データ.社宅使用料調整金額を設定
				jigetsuShatakuShiyouryouTyouseiKingaku = BigDecimal.valueOf(shatakuYoyakuDtList.get(0).getRentalAdjust());
			}
			
		}

		return jigetsuShatakuShiyouryouTyouseiKingaku;
	}

	/**
	 * 次月の個人負担共益費調整金額の取得
	 * 
	 * @param tsukiJoinDt
	 *            月別使用料履歴データ
	 * @param shatakuYoyakuDtList
	 *            社宅使用料予約データ
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 次月の個人負担共益費調整金額
	 */
	private BigDecimal getJigetsuKojinhutanKyouekihiTyouseiKingaku(
			Skf3050Bt004GetTsukibetuShiyoryoRirekiJoinDataExp tsukiJoinDt,
			List<Skf3050Bt004GetShatakuShiyoryoYoyakuDataExp> shatakuYoyakuDtList, String shoriNengetsu) {

		//次月の個人負担共益費調整金額
		BigDecimal jigetsuKojinhutanKyouekihiTyouseiKingaku = BigDecimal.ZERO;
		String taikyoDate = tsukiJoinDt.getLedgerTaikyoDate();

		if (!NfwStringUtils.isEmpty(taikyoDate)) {
			String taikyoYymm = taikyoDate.substring(0, 6);

			if (Integer.parseInt(taikyoYymm) <= Integer.parseInt(shoriNengetsu)) {
				//退居日が当月処理月以前の場合
				if (shatakuYoyakuDtList.size() == 0) {
					jigetsuKojinhutanKyouekihiTyouseiKingaku = BigDecimal.ZERO;

				} else {
					//社宅使用料予約データ.個人負担共益費調整金額を設定
					jigetsuKojinhutanKyouekihiTyouseiKingaku = BigDecimal
							.valueOf(shatakuYoyakuDtList.get(0).getKyoekihiPersonAdjust());
				}
			}else{
				String nextShoriNengetsu = skfDateFormatUtils.addYearMonth(shoriNengetsu, 1);
				if (Integer.parseInt(taikyoYymm) >= Integer.parseInt(nextShoriNengetsu)) {
					//当月処理月の翌月以降の場合
					if (shatakuYoyakuDtList.size() == 0) {
						//当月処理月の月別使用料履歴.個人負担共益費調整金額を設定
						jigetsuKojinhutanKyouekihiTyouseiKingaku = BigDecimal.valueOf(tsukiJoinDt.getRirekiKyoekihiPersonAdjust());
					} else {
						//社宅使用料予約データ.個人負担共益費調整金額を設定
						jigetsuKojinhutanKyouekihiTyouseiKingaku = BigDecimal.valueOf(shatakuYoyakuDtList.get(0).getKyoekihiPersonAdjust());
					}
				}
			}
		}else{
			//退居日が未入力
			if (shatakuYoyakuDtList.size() == 0) {
				//当月処理月の月別使用料履歴.個人負担共益費調整金額を設定
				jigetsuKojinhutanKyouekihiTyouseiKingaku = BigDecimal.valueOf(tsukiJoinDt.getRirekiKyoekihiPersonAdjust());
			} else {
				//社宅使用料予約データ.個人負担共益費調整金額を設定
				jigetsuKojinhutanKyouekihiTyouseiKingaku = BigDecimal.valueOf(shatakuYoyakuDtList.get(0).getKyoekihiPersonAdjust());
			}
			
		}

		return jigetsuKojinhutanKyouekihiTyouseiKingaku;
	}

	/**
	 * 次月の駐車場使用料調整金額の取得
	 * 
	 * @param tsukiJoinDt
	 *            月別使用料履歴データ
	 * @param shatakuYoyakuDtList
	 *            社宅使用料予約データ
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 次月の駐車場使用料調整金額
	 */
	private BigDecimal getJigetsuTyushajoShiyouryouTyouseiKingaku(
			Skf3050Bt004GetTsukibetuShiyoryoRirekiJoinDataExp tsukiJoinDt,
			List<Skf3050Bt004GetShatakuShiyoryoYoyakuDataExp> shatakuYoyakuDtList, String shoriNengetsu) {

		//次月の駐車場使用料調整金額
		BigDecimal jigetsuTyushajoShiyouryouTyouseiKingaku = BigDecimal.ZERO;
		String taikyoDate = tsukiJoinDt.getLedgerTaikyoDate();

		if (!NfwStringUtils.isEmpty(taikyoDate)) {
			String taikyoYymm = taikyoDate.substring(0, 6);

			if (Integer.parseInt(taikyoYymm) <= Integer.parseInt(shoriNengetsu)) {
				//退居日が当月処理月以前の場合
				if (shatakuYoyakuDtList.size() == 0) {
					jigetsuTyushajoShiyouryouTyouseiKingaku = BigDecimal.ZERO;

				} else {
					//社宅使用料予約データ.駐車場使用料調整金額を設定
					jigetsuTyushajoShiyouryouTyouseiKingaku = BigDecimal
							.valueOf(shatakuYoyakuDtList.get(0).getParkingRentalAdjust());
				}
			}else{
				String nextShoriNengetsu = skfDateFormatUtils.addYearMonth(shoriNengetsu, 1);
				if (Integer.parseInt(taikyoYymm) >= Integer.parseInt(nextShoriNengetsu)) {
					//当月処理月の翌月以降の場合
					if (shatakuYoyakuDtList.size() == 0) {
						//当月処理月の月別使用料履歴.駐車場使用料調整金額を設定
						jigetsuTyushajoShiyouryouTyouseiKingaku = BigDecimal.valueOf(tsukiJoinDt.getRirekiParkingRentalAdjust());
					} else {
						//社宅使用料予約データ.駐車場使用料調整金額を設定
						jigetsuTyushajoShiyouryouTyouseiKingaku = BigDecimal.valueOf(shatakuYoyakuDtList.get(0).getParkingRentalAdjust());
					}
				}
			}
		}else{
			//退居日が未入力
			if (shatakuYoyakuDtList.size() == 0) {
				//当月処理月の月別使用料履歴.駐車場使用料調整金額を設定
				jigetsuTyushajoShiyouryouTyouseiKingaku = BigDecimal.valueOf(tsukiJoinDt.getRirekiParkingRentalAdjust());
			} else {
				//社宅使用料予約データ.駐車場使用料調整金額を設定
				jigetsuTyushajoShiyouryouTyouseiKingaku = BigDecimal.valueOf(shatakuYoyakuDtList.get(0).getParkingRentalAdjust());
			}
			
		}

		return jigetsuTyushajoShiyouryouTyouseiKingaku;
	}

	/**
	 * 月別使用料履歴テーブルへの登録
	 * 
	 * @param tsukiJoinDt
	 *            月別使用料履歴データ
	 * @param yearMonth
	 *            年月
	 * @param rentalAdjust
	 *            社宅使用料調整金額
	 * @param kyouekihiPersonAdjust
	 *            個人負担共益費調整金額
	 * @param kyouekihiPersonTotal
	 *            個人負担共益費月額（調整後）
	 * @param parkingRentalAdjust
	 *            駐車場使用料調整金額
	 * @param insertUser
	 *            登録ユーザ
	 * @return 登録件数
	 */
	private Integer insertTsukibetuShiyoryoRirekiData(Skf3050Bt004GetTsukibetuShiyoryoRirekiJoinDataExp tsukiJoinDt,
			String shoriNengetsu, Integer rentalAdjust, Integer kyouekihiPersonAdjust, Integer kyouekihiPersonTotal,
			Integer parkingRentalAdjust, String insertUser) {

		Skf3050Bt004InsertTsukibetuShiyoryoRirekiDataExpParameter param = new Skf3050Bt004InsertTsukibetuShiyoryoRirekiDataExpParameter();

		param.setShatakuKanriId(tsukiJoinDt.getShatakuKanriId());
		param.setYearMonth(shoriNengetsu);
		param.setRentalMonth(0);
		param.setRentalDay(0);
		param.setRentalTotal(0);
		param.setParking1RentalMonth(0);
		param.setParking1RentalDay(0);
		param.setParking2RentalMonth(0);
		param.setParking2RentalDay(0);
		param.setParkingRentalTotal(0);
		param.setGenbutuSantei(0);
		param.setBihinGenbutuGoukei(0);
		param.setInsertUserId(insertUser);

		if (tsukiJoinDt.getRirekiRentalPatternId() == null) {
			param.setRentalPatternId(null);
		} else {
			param.setRentalPatternId(tsukiJoinDt.getRirekiRentalPatternId());
		}

		if (tsukiJoinDt.getRirekiYakuinSannteiKbn() == null) {
			param.setYakuinSannteiKbn(null);
		} else {
			param.setYakuinSannteiKbn(tsukiJoinDt.getRirekiYakuinSannteiKbn());
		}

		if (rentalAdjust == null) {
			param.setRentalAdjust(null);
		} else {
			param.setRentalAdjust(rentalAdjust);
		}

		if (tsukiJoinDt.getRirekiKyoekihiPerson() == null) {
			param.setKyoekihiPerson(null);
		} else {
			param.setKyoekihiPerson(tsukiJoinDt.getRirekiKyoekihiPerson());
		}

		if (kyouekihiPersonAdjust == null) {
			param.setKyoekihiPersonAdjust(null);
		} else {
			param.setKyoekihiPersonAdjust(kyouekihiPersonAdjust);
		}

		if (kyouekihiPersonTotal == null) {
			param.setKyoekihiPersonTotal(null);
		} else {
			param.setKyoekihiPersonTotal(kyouekihiPersonTotal);
		}

		if (tsukiJoinDt.getRirekiKyoekihiPayMonth() == null) {
			param.setKyoekihiPayMonth(null);
		} else {
			param.setKyoekihiPayMonth(tsukiJoinDt.getRirekiKyoekihiPayMonth());
		}

		if (parkingRentalAdjust == null) {
			param.setParkingRentalAdjust(null);
		} else {
			param.setParkingRentalAdjust(parkingRentalAdjust);
		}

		if (tsukiJoinDt.getRirekiShatakuAccountCd() == null) {
			param.setShatakuAccountCd(null);
		} else {
			param.setShatakuAccountCd(tsukiJoinDt.getRirekiShatakuAccountCd());
		}

		if (tsukiJoinDt.getRirekiShatakuAccountName() == null) {
			param.setShatakuAccountName(null);
		} else {
			param.setShatakuAccountName(tsukiJoinDt.getRirekiShatakuAccountName());
		}

		if (tsukiJoinDt.getRirekiKyoekiAccountCd() == null) {
			param.setKyoekiAccountCd(null);
		} else {
			param.setKyoekiAccountCd(tsukiJoinDt.getRirekiKyoekiAccountCd());
		}

		if (tsukiJoinDt.getRirekiKyoekiAccountName() == null) {
			param.setKyoekiAccountName(null);
		} else {
			param.setKyoekiAccountName(tsukiJoinDt.getRirekiKyoekiAccountName());
		}
		param.setInsertProgramId(confirmPositiveDataBatchPrgId);

		Integer insertCnt = skf3050Bt004InsertTsukibetuShiyoryoRirekiDataExpRepository
				.insertTsukibetuShiyoryoRirekiData(param);

		return insertCnt;
	}

	/**
	 * 社宅使用料予約データテーブルの「更新済フラグ」更新
	 * 
	 * @param shatakuKanriDaichoId
	 *            社宅管理台帳ID
	 * @param updateUser
	 *            更新ユーザ
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 更新件数
	 */
	private void updateShatakuShiyouryouYoyakuData(Long shatakuKanriDaichoId, String updateUser, String shoriNengetsu) {

		Skf3050Bt004UpdateShatakuShiyouryouYoyakuDataExpParameter param = new Skf3050Bt004UpdateShatakuShiyouryouYoyakuDataExpParameter();

		if (updateUser == null) {
			param.setUpdateUser(null);
		} else {
			param.setUpdateUser(updateUser);
		}

		if (shatakuKanriDaichoId == null) {
			param.setShatakuKanriId(null);
		} else {
			param.setShatakuKanriId(shatakuKanriDaichoId);
		}

		param.setYearMonth(shoriNengetsu);
		param.setUpdateProgramId(confirmPositiveDataBatchPrgId);

		skf3050Bt004UpdateShatakuShiyouryouYoyakuDataExpRepository.updateShatakuShiyouryouYoyakuData(param);
	}

	/**
	 * 月別所属履歴テーブルのデータ取得
	 * 
	 * @param shatakuKanriDaichoId
	 *            社宅管理台帳ID
	 * @param shoriNengetsu
	 *            年月
	 * @return 月別所属履歴データ
	 */
	private List<Skf3050Bt004GetTsukibetuSyozokuRirekiDataExp> getTsukibetuSyozokuRirekiData(Long shatakuKanriDaichoId,
			String shoriNengetsu) {

		Skf3050Bt004GetTsukibetuSyozokuRirekiDataExpParameter param = new Skf3050Bt004GetTsukibetuSyozokuRirekiDataExpParameter();
		param.setShatakuKanriId(shatakuKanriDaichoId);
		param.setYearMonth(shoriNengetsu);

		List<Skf3050Bt004GetTsukibetuSyozokuRirekiDataExp> outData = skf3050Bt004GetTsukibetuSyozokuRirekiDataExpRepository
				.getTsukibetuSyozokuRirekiData(param);

		return outData;
	}

	/**
	 * 事業領域コード、事業領域名の取得
	 * 
	 * @param shainIdoDtList
	 *            次月事業領域データ
	 * @param shoriNengetsu
	 *            処理年月
	 * @param companyCd
	 *            会社コード
	 * @param shainBangou
	 *            社員番号
	 * @param beginEndKbn
	 *            月初月末区分
	 * @return 事業領域データmap
	 */
	private Map<String, String> getJigyoRyoiki(List<Skf3050Bt004GetShatakuShainIdoRirekiDataExp> shainIdoDtList) {

		Map<String, String> rtn = new HashMap<String, String>();

		if (shainIdoDtList.size() <= 0) {
			rtn.put(JIGYO_RYO_CD_KEY, "");
			rtn.put(JIGYO_RYO_NAME_KEY, "");

		} else {
			if (!NfwStringUtils.isEmpty(shainIdoDtList.get(0).getRirekiBusinessAreaCd())) {
				//社宅社員異動履歴から取得
				rtn.put(JIGYO_RYO_CD_KEY, shainIdoDtList.get(0).getRirekiBusinessAreaCd());
				rtn.put(JIGYO_RYO_NAME_KEY, shainIdoDtList.get(0).getRirekiBusinessAreaName());
			} else {
				//社宅社員異動履歴から取得できない場合、社宅社員マスタ・事業領域マスタから取得
				rtn.put(JIGYO_RYO_CD_KEY, shainIdoDtList.get(0).getShainBusinessAreaCd());
				rtn.put(JIGYO_RYO_NAME_KEY, shainIdoDtList.get(0).getAreaBusinessAreaName());
			}
		}

		return rtn;
	}

	/**
	 * 社宅社員異動履歴テーブルのデータ取得
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @param beginEndKbn
	 *            月初月末区分
	 * @param companyCd
	 *            会社コード
	 * @param shainBangou
	 *            社員番号
	 * @return 社宅社員異動履歴データ
	 */
	private List<Skf3050Bt004GetShatakuShainIdoRirekiDataExp> getShatakuSyainIdoRirekiData(String shoriNengetsu,
			String beginEndKbn, String companyCd, String shainBangou) {

		Skf3050Bt004GetShatakuShainIdoRirekiDataExpParameter param = new Skf3050Bt004GetShatakuShainIdoRirekiDataExpParameter();
		param.setYearMonth(shoriNengetsu);
		param.setBeginningEndKbn(beginEndKbn);
		param.setCompanyCd(companyCd);
		param.setShainNo(shainBangou);

		List<Skf3050Bt004GetShatakuShainIdoRirekiDataExp> outData = skf3050Bt004GetShatakuShainIdoRirekiDataExpRepository
				.getShatakuShainIdoRirekiData(param);

		return outData;
	}

	/**
	 * 組織マスタ情報取得
	 * 
	 * @param companyCd
	 *            会社コード
	 * @param shainBangou
	 *            社員番号
	 * @return 組織マスタデータ
	 */
	private List<Skf3050Bt004GetShainSoshikiDataExp> getShainSoshikiData(String companyCd, String shainBangou) {

		Skf3050Bt004GetShainSoshikiDataExpParameter param = new Skf3050Bt004GetShainSoshikiDataExpParameter();
		param.setCompanyCd(companyCd);
		param.setShainNo(shainBangou);

		List<Skf3050Bt004GetShainSoshikiDataExp> outData = skf3050Bt004GetShainSoshikiDataExpRepository
				.getShainSoshikiData(param);

		return outData;
	}

	/**
	 * 会社名取得
	 * 
	 * @param companyCd
	 *            会社コード
	 * @return 会社名取得
	 */
	private String getCompanyName(String companyCd) {

		String outVal = "";

		if (NfwStringUtils.isEmpty(companyCd)) {
			return outVal;
		}

		List<Skf3050Bt004GetCompanyAgencyNameExp> companyData = skf3050Bt004GetCompanyAgencyNameExpRepository
				.getCompanyAgencyName(companyCd);

		if (companyData.size() > 0) {
			outVal = companyData.get(0).getCompanyName();
		}

		return outVal;
	}

	/**
	 * 月別所属情報履歴テーブルへ登録
	 * 
	 * @param shatakuKanriId
	 *            社宅管理台帳ID
	 * @param shoriNengetsu
	 *            年月
	 * @param companyCd
	 *            会社コード
	 * @param companyName
	 *            会社名
	 * @param agencyCd
	 *            機関コード
	 * @param agencyName
	 *            機関名
	 * @param affiliation1Cd
	 *            所属1コード
	 * @param affiliation1
	 *            所属1名
	 * @param affiliation2Cd
	 *            所属2コード
	 * @param affiliation2
	 *            所属2名
	 * @param businessAreaCd
	 *            当月事業領域コード
	 * @param businessAreaName
	 *            当月事業領域名
	 * @param preBusinessAreaCd
	 *            前月事業領域コード
	 * @param preBusinessAreaName
	 *            前月事業領域名
	 * @param insertUser
	 *            登録ユーザー
	 */
	private void insertTsukibetuSyozokujyohoRirekiData(Long shatakuKanriId, String shoriNengetsu, String companyCd,
			String companyName, String agencyCd, String agencyName, String affiliation1Cd, String affiliation1,
			String affiliation2Cd, String affiliation2, String businessAreaCd, String businessAreaName,
			String preBusinessAreaCd, String preBusinessAreaName, String insertUser) {

		Skf3050Bt004InsertTsukibetuSyozokujyohoRirekiDataExpParameter param = new Skf3050Bt004InsertTsukibetuSyozokujyohoRirekiDataExpParameter();

		param.setShatakuKanriId(shatakuKanriId);
		param.setYearMonth(shoriNengetsu);

		if (companyCd == null) {
			param.setCompanyCd(null);
		} else {
			param.setCompanyCd(companyCd);
		}

		if (companyName == null) {
			param.setCompanyName(null);
		} else {
			param.setCompanyName(companyName);
		}

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

		if (affiliation1Cd == null) {
			param.setAffiliation1Cd(null);
		} else {
			param.setAffiliation1Cd(affiliation1Cd);
		}

		if (affiliation1 == null) {
			param.setAffiliation1(null);
		} else {
			param.setAffiliation1(affiliation1);
		}

		if (affiliation2Cd == null) {
			param.setAffiliation2Cd(null);
		} else {
			param.setAffiliation2Cd(affiliation2Cd);
		}

		if (affiliation2 == null) {
			param.setAffiliation2(null);
		} else {
			param.setAffiliation2(affiliation2);
		}

		if (businessAreaCd == null) {
			param.setBusinessAreaCd(null);
		} else {
			param.setBusinessAreaCd(businessAreaCd);
		}

		if (businessAreaName == null) {
			param.setBusinessAreaName(null);
		} else {
			param.setBusinessAreaName(businessAreaName);
		}

		if (preBusinessAreaCd == null) {
			param.setPreBusinessAreaCd(null);
		} else {
			param.setPreBusinessAreaCd(preBusinessAreaCd);
		}

		if (preBusinessAreaName == null) {
			param.setPreBusinessAreaName(null);
		} else {
			param.setPreBusinessAreaName(preBusinessAreaName);
		}

		param.setInsertUserId(insertUser);
		param.setInsertProgramId(confirmPositiveDataBatchPrgId);

		skf3050Bt004InsertTsukibetuSyozokujyohoRirekiDataExpRepository.insertTsukibetuSyozokujyohoRirekiData(param);

	}

	/**
	 * 月別備品使用料明細テーブルのデータ取得
	 * 
	 * @param shatakuKanriDaichoId
	 *            社宅管理台帳ID
	 * @param shoriNengetsu
	 *            年月
	 * @return 月別備品使用料明細データ
	 */
	private List<Skf3050Bt004GetTsukibetuBihinSiyoryoMeisaiDataExp> getTsukibetuBihinSiyoryoMeisaiData(
			Long shatakuKanriDaichoId, String shoriNengetsu) {

		Skf3050Bt004GetTsukibetuBihinSiyoryoMeisaiDataExpParameter param = new Skf3050Bt004GetTsukibetuBihinSiyoryoMeisaiDataExpParameter();
		param.setShatakuKanriId(shatakuKanriDaichoId);
		param.setYearMonth(shoriNengetsu);

		List<Skf3050Bt004GetTsukibetuBihinSiyoryoMeisaiDataExp> outData = skf3050Bt004GetTsukibetuBihinSiyoryoMeisaiDataExpRepository
				.getTsukibetuBihinSiyoryoMeisaiData(param);

		return outData;
	}

	/**
	 * 月別駐車場履歴テーブルのデータ取得
	 * 
	 * @param shatakuKanriDaichoId
	 *            社宅管理台帳ID
	 * @param shoriNengetsu
	 *            年月
	 * @return 月別駐車場履歴データ
	 */
	private List<Skf3050Bt004GetTsukibetuTyusyajyoRirekiDataExp> getTsukibetuTyusyajyoRirekiData(
			Long shatakuKanriDaichoId, String shoriNengetsu) {

		Skf3050Bt004GetTsukibetuTyusyajyoRirekiDataExpParameter param = new Skf3050Bt004GetTsukibetuTyusyajyoRirekiDataExpParameter();
		param.setShatakuKanriId(shatakuKanriDaichoId);
		param.setYearMonth(shoriNengetsu);

		List<Skf3050Bt004GetTsukibetuTyusyajyoRirekiDataExp> outData = skf3050Bt004GetTsukibetuTyusyajyoRirekiDataExpRepository
				.getTsukibetuTyusyajyoRirekiData(param);

		return outData;
	}

	/**
	 * 月別備品使用料明細テーブルへ登録
	 * 
	 * @param shatakuKanriId
	 *            社宅管理台帳ID
	 * @param shoriNengetsu
	 *            年月
	 * @param touTsukiBihinData
	 *            月別備品使用料明細データ
	 * @param insertUser
	 *            登録ユーザー
	 */
	private void insertTsukibetuBihinSiyoryoMeisaiData(Long shatakuKanriId, String shoriNengetsu,
			Skf3050Bt004GetTsukibetuBihinSiyoryoMeisaiDataExp touTsukiBihinData, String insertUser) {

		Skf3050Bt004InsertTsukibetuBihinSiyoryoMeisaiDataExpParameter param = new Skf3050Bt004InsertTsukibetuBihinSiyoryoMeisaiDataExpParameter();
		param.setShatakuKanriId(shatakuKanriId);
		param.setYearMonth(shoriNengetsu);
		param.setBihinCd(touTsukiBihinData.getBihinCd());
		param.setInsertUserId(insertUser);

		if (touTsukiBihinData.getBihinLentStatusKbn() == null) {
			param.setBihinLentStatusKbn(null);
		} else {
			param.setBihinLentStatusKbn(touTsukiBihinData.getBihinLentStatusKbn());
		}

		if (touTsukiBihinData.getBihinApplKbn() == null) {
			param.setBihinApplKbn(null);
		} else {
			param.setBihinApplKbn(touTsukiBihinData.getBihinApplKbn());
		}

		if (touTsukiBihinData.getBihinReturnKbn() == null) {
			param.setBihinReturnKbn(null);
		} else {
			param.setBihinReturnKbn(touTsukiBihinData.getBihinReturnKbn());
		}

		if (touTsukiBihinData.getBihinGenbutsuGaku() == null) {
			param.setBihinGenbutsuGaku(null);
		} else {
			param.setBihinGenbutsuGaku(touTsukiBihinData.getBihinGenbutsuGaku());
		}
		param.setInsertProgramId(confirmPositiveDataBatchPrgId);

		skf3050Bt004InsertTsukibetuBihinSiyoryoMeisaiDataExpRepository.insertTsukibetuBihinSiyoryoMeisaiData(param);

	}

	/**
	 * 月別駐車場履歴テーブルへ登録
	 * 
	 * @param shatakuKanriId
	 *            社宅管理台帳ID
	 * @param tougetsuTsukiTyushaDt
	 *            月別駐車場履歴データ
	 * @param shoriNengetsu
	 *            年月
	 * @param insertUser
	 *            登録ユーザー
	 */
	private void insertTsukibetuTyusyajyoRirekiData(Long shatakuKanriId,
			Skf3050Bt004GetTsukibetuTyusyajyoRirekiDataExp tougetsuTsukiTyushaDt, String shoriNengetsu,
			String insertUser) {

		Skf3050Bt004InsertTsukibetuTyusyajyoRirekiDataExpParameter param = new Skf3050Bt004InsertTsukibetuTyusyajyoRirekiDataExpParameter();
		param.setShatakuKanriId(shatakuKanriId);
		param.setParkingLendNo(tougetsuTsukiTyushaDt.getParkingLendNo());
		param.setYearMonth(shoriNengetsu);
		param.setInsertUserId(insertUser);

		if (tougetsuTsukiTyushaDt.getParkingKanriNo() == null) {
			param.setParkingKanriNo(null);
		} else {
			param.setParkingKanriNo(tougetsuTsukiTyushaDt.getParkingKanriNo());
		}

		if (tougetsuTsukiTyushaDt.getParkingStartDate() == null) {
			param.setParkingStartDate(null);
		} else {
			param.setParkingStartDate(tougetsuTsukiTyushaDt.getParkingStartDate());
		}

		if (tougetsuTsukiTyushaDt.getParkingEndDate() == null) {
			param.setParkingEndDate(null);
		} else {
			param.setParkingEndDate(tougetsuTsukiTyushaDt.getParkingEndDate());
		}
		param.setInsertProgramId(confirmPositiveDataBatchPrgId);

		skf3050Bt004InsertTsukibetuTyusyajyoRirekiDataExpRepository.insertTsukibetuTyusyajyoRirekiData(param);

	}

	/**
	 * 月別相互利用履歴テーブルのデータ取得
	 * 
	 * @param shatakuKanriDaichoId
	 *            社宅管理台帳ID
	 * @param shoriNengetsu
	 *            年月
	 * @return 月別相互利用履歴データ
	 */
	private List<Skf3050Bt004GetTsukibetuSougoriyoRirekiDataExp> getTsukibetuSougoriyoRirekiData(
			Long shatakuKanriDaichoId, String shoriNengetsu) {

		Skf3050Bt004GetTsukibetuSougoriyoRirekiDataExpParameter param = new Skf3050Bt004GetTsukibetuSougoriyoRirekiDataExpParameter();
		param.setShatakuKanriId(shatakuKanriDaichoId);
		param.setYearMonth(shoriNengetsu);

		List<Skf3050Bt004GetTsukibetuSougoriyoRirekiDataExp> outData = skf3050Bt004GetTsukibetuSougoriyoRirekiDataExpRepository
				.getTsukibetuSougoriyoRirekiData(param);

		return outData;
	}

	/**
	 * 月別相互利用履歴テーブルへ登録
	 * 
	 * @param shatakuKanriId
	 *            社宅管理台帳ID
	 * @param shoriNengetsu
	 *            年月
	 * @param tougetsuTsukiSogoDt
	 *            月別相互利用履歴データ
	 * @param insertUser
	 *            登録ユーザー
	 */
	private void insertTsukibetuSougoriyoRirekiData(Long shatakuKanriId, String shoriNengetsu,
			Skf3050Bt004GetTsukibetuSougoriyoRirekiDataExp tougetsuTsukiSogoDt, String insertUser) {

		Skf3050Bt004InsertTsukibetuSougoriyoRirekiDataExpParameter param = new Skf3050Bt004InsertTsukibetuSougoriyoRirekiDataExpParameter();
		param.setShatakuKanriId(shatakuKanriId);
		param.setYearMonth(shoriNengetsu);
		param.setInsertUserId(insertUser);

		if (tougetsuTsukiSogoDt == null || tougetsuTsukiSogoDt.getAssignCompanyCd() == null) {
			param.setAssignCompanyCd(null);
		} else {
			param.setAssignCompanyCd(tougetsuTsukiSogoDt.getAssignCompanyCd());
		}

		if (tougetsuTsukiSogoDt == null || tougetsuTsukiSogoDt.getAssignAgencyName() == null) {
			param.setAssignAgencyName(null);
		} else {
			param.setAssignAgencyName(tougetsuTsukiSogoDt.getAssignAgencyName());
		}

		if (tougetsuTsukiSogoDt == null || tougetsuTsukiSogoDt.getAssignAffiliation1() == null) {
			param.setAssignAffiliation1(null);
		} else {
			param.setAssignAffiliation1(tougetsuTsukiSogoDt.getAssignAffiliation1());
		}

		if (tougetsuTsukiSogoDt == null || tougetsuTsukiSogoDt.getAssignAffiliation2() == null) {
			param.setAssignAffiliation2(null);
		} else {
			param.setAssignAffiliation2(tougetsuTsukiSogoDt.getAssignAffiliation2());
		}

		if (tougetsuTsukiSogoDt == null || tougetsuTsukiSogoDt.getAssignCd() == null) {
			param.setAssignCd(null);
		} else {
			param.setAssignCd(tougetsuTsukiSogoDt.getAssignCd());
		}
		param.setInsertProgramId(confirmPositiveDataBatchPrgId);

		skf3050Bt004InsertTsukibetuSougoriyoRirekiDataExpRepository.insertTsukibetuSougoriyoRirekiData(param);

	}

	/**
	 * 社宅使用料計算のEntityを作成する。（パターン3）
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @param tsukiRirekiData
	 *            月別使用料履歴データ
	 * @param shatakuChintairyo
	 *            社宅賃貸料
	 * @return Entity
	 */
	private SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp createShatakuRentCalcInputEntityPtn3(String shoriNengetsu,
			Skf3050Bt004GetTsukibetuShiyoryoRirekiJoinDataExp tsukiRirekiData, Integer shatakuChintairyo) {

		String birthDay = "";
		if (tsukiRirekiData.getBirthdayYear() != 0) {
			birthDay = String.format("%04d", tsukiRirekiData.getBirthdayYear());
			birthDay += String.format("%02d", tsukiRirekiData.getBirthdayMonth());
			birthDay += String.format("%02d", tsukiRirekiData.getBirthdayDay());
		}

		SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp rtnEntity = new SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp();
		rtnEntity.setShoriNengetsu(shoriNengetsu);
		rtnEntity.setShoriKbn("2");
		rtnEntity.setShiyouryouPatternId(String.valueOf(tsukiRirekiData.getRentalPatternId()));
		rtnEntity.setYakuinKbn(tsukiRirekiData.getRirekiYakuinSannteiKbn());
		rtnEntity.setShatakuChintairyou(String.valueOf(shatakuChintairyo));
		rtnEntity.setSeinengappi(birthDay);

		return rtnEntity;
	}

	/**
	 * 月別駐車場履歴テーブル（貸与番号絞込み）のデータ取得
	 * 
	 * @param shatakuKanriDaichoId
	 *            社宅管理台帳ID
	 * @param parkingLendNo
	 *            貸与番号
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 月別駐車場履歴テーブル（貸与番号絞込み）のデータ
	 */
	private Skf3030TParkingRireki getTsukibetuTyusyajyoRirekiDataSyutokuByLendNo(Long shatakuKanriDaichoId,
			Long parkingLendNo, String shoriNengetsu) {

		Skf3030TParkingRirekiKey param = new Skf3030TParkingRirekiKey();
		param.setShatakuKanriId(shatakuKanriDaichoId);
		param.setParkingLendNo(parkingLendNo);
		param.setYearMonth(shoriNengetsu);

		Skf3030TParkingRireki outData = skf3030TParkingRirekiRepository.selectByPrimaryKey(param);

		return outData;
	}

	/**
	 * 社宅使用料計算のEntityを作成する。（パターン4）
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @param tsukiRirekiData
	 *            月別使用料履歴データ
	 * @param chushajoChintairyo
	 *            駐車場賃貸料
	 * @param chushajoKanriBangou
	 *            駐車場管理番号
	 * @return Entity
	 */
	private SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp createShatakuRentCalcInputEntityPtn4(String shoriNengetsu,
			Skf3050Bt004GetTsukibetuShiyoryoRirekiJoinDataExp tsukiRirekiData, Integer chushajoChintairyo,
			Long chushajoKanriBangou) {

		SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp rtnEntity = new SkfBaseBusinessLogicUtilsShatakuRentCalcInputExp();
		rtnEntity.setShoriNengetsu(shoriNengetsu);
		rtnEntity.setShoriKbn("4");
		rtnEntity.setYakuinKbn(tsukiRirekiData.getRirekiYakuinSannteiKbn());
		rtnEntity.setChyshajoChintairyou(String.valueOf(chushajoChintairyo));
		rtnEntity.setShatakuKanriBangou(String.valueOf(tsukiRirekiData.getLedgerShatakuKanriNo()));
		rtnEntity.setChushajoKanriBangou(String.valueOf(chushajoKanriBangou));
		rtnEntity.setAuseKbn(tsukiRirekiData.getAuse());

		return rtnEntity;
	}

	/**
	 * 社宅使用料日割額算出
	 * 
	 * @param shoriNengetsuYokugetsu
	 *            処理年月翌月(YYYYMM)
	 * @param nyuukyoDate
	 *            入居日(YYYYMMDD)
	 * @param taikyoDate
	 *            退去日(YYYYMMDD)
	 * @param shatakuRiyouryouGetsugaku
	 *            社宅使用料月額
	 * @return 社宅使用料日割額
	 */
	private BigDecimal getShatakuShiyoryoHiwari(String shoriNengetsuYokugetsu, String nyuukyoDate, String taikyoDate,
			BigDecimal shatakuRiyouryouGetsugaku) {

		String shoriNengetsuYokugetsuShonichi = shoriNengetsuYokugetsu + "01";

		
		// 開始日が処理年月の初日より過去の場合、初日とする
		if (NfwStringUtils.isEmpty(nyuukyoDate)) {
			nyuukyoDate = shoriNengetsuYokugetsuShonichi;
		} else {
			BigDecimal decimalNyuukyoDate = new BigDecimal(nyuukyoDate);
			BigDecimal decimalYokugetsuShonichi = new BigDecimal(shoriNengetsuYokugetsuShonichi);
	
			if (decimalNyuukyoDate.compareTo(decimalYokugetsuShonichi) < 0) {
				nyuukyoDate = shoriNengetsuYokugetsuShonichi;
			}
		}

		String shoriNengetsuYokugetsuMatsujitsu = getGetsumatsujitu(shoriNengetsuYokugetsu);
		BigDecimal decimalYokugetsuMatsujitsu = new BigDecimal(shoriNengetsuYokugetsuMatsujitsu);

		if (NfwStringUtils.isEmpty(taikyoDate)) {
			taikyoDate = shoriNengetsuYokugetsuMatsujitsu;

		} else {
			BigDecimal decimaltaikyoDate = new BigDecimal(taikyoDate);

			if (decimaltaikyoDate.compareTo(decimalYokugetsuMatsujitsu) > 0) {
				taikyoDate = shoriNengetsuYokugetsuMatsujitsu;
			}
		}

		BigDecimal rtnVal = BigDecimal.ZERO;

		if (Objects.equals(nyuukyoDate, shoriNengetsuYokugetsuShonichi) && Objects.equals(shoriNengetsuYokugetsuMatsujitsu, taikyoDate)) {
			rtnVal = shatakuRiyouryouGetsugaku;

		} else {
			String shoriNengetsuAdd2Month = skfDateFormatUtils.addYearMonth(shoriNengetsuYokugetsu, 1);
			BigDecimal decimalShoriNengetsuAdd2Month = new BigDecimal(shoriNengetsuAdd2Month);
			BigDecimal nyuukyoDateYymm = new BigDecimal(nyuukyoDate.substring(0, 6));
			BigDecimal taikyoDateYymm = new BigDecimal(taikyoDate.substring(0, 6));
			String shoriNengetsu = skfDateFormatUtils.addYearMonth(shoriNengetsuYokugetsu, -1);
			BigDecimal decimalShoriNengetsu = new BigDecimal(shoriNengetsu);

			if (decimalShoriNengetsuAdd2Month.compareTo(nyuukyoDateYymm) <= 0
					|| taikyoDateYymm.compareTo(decimalShoriNengetsu) <= 0) {
				rtnVal = BigDecimal.ZERO;

			} else if (!Objects.equals(nyuukyoDate, shoriNengetsuYokugetsuShonichi)
					|| !Objects.equals(taikyoDate, shoriNengetsuYokugetsuMatsujitsu)) {
				BigDecimal taikyoDateDd = new BigDecimal(taikyoDate.substring(6, 8));
				BigDecimal nyuukyoDateDd = new BigDecimal(nyuukyoDate.substring(6, 8));

				BigDecimal num_culc_1 = taikyoDateDd.subtract(nyuukyoDateDd).add(BigDecimal.ONE);
				BigDecimal num_culc_2 = shatakuRiyouryouGetsugaku.multiply(num_culc_1);

				BigDecimal yokugetsuMatsujitsuDd = new BigDecimal(shoriNengetsuYokugetsuMatsujitsu.substring(6, 8));
				rtnVal = num_culc_2.divide(yokugetsuMatsujitsuDd, 0, RoundingMode.DOWN);
			}
		}

		return rtnVal;
	}

	/**
	 * 駐車場使用料日割額算出
	 * 
	 * @param shoriNengetsuYokugetsu
	 *            処理年月翌月(YYYYMM)
	 * @param kaishiDate
	 *            駐車場開始日
	 * @param henkyakuDate
	 *            駐車場返却日
	 * @param chushajouShiyouryouGetsugaku
	 *            駐車場使用料月額
	 * @return 駐車場使用料日割額
	 */
	private BigDecimal getChushajoShiyoryoHiwari(String shoriNengetsuYokugetsu, String kaishiDate, String henkyakuDate,
			BigDecimal chushajouShiyouryouGetsugaku) {

		String shoriNengetsuYokugetsuShonichi = shoriNengetsuYokugetsu + "01";

		// 開始日が処理年月の初日より過去の場合、初日とする
		if (NfwStringUtils.isEmpty(kaishiDate)) {
			kaishiDate = shoriNengetsuYokugetsuShonichi;
		} else {
			BigDecimal decimalKaishiDate = new BigDecimal(kaishiDate);
			BigDecimal decimalYokugetsuShonichi = new BigDecimal(shoriNengetsuYokugetsuShonichi);
	
			if (decimalKaishiDate.compareTo(decimalYokugetsuShonichi) < 0) {
				kaishiDate = shoriNengetsuYokugetsuShonichi;
			}
		}

		String shoriNengetsuYokugetsuMatsujitsu = getGetsumatsujitu(shoriNengetsuYokugetsu);
		BigDecimal decimalYokugetsuMatsujitsu = new BigDecimal(shoriNengetsuYokugetsuMatsujitsu);

		if (NfwStringUtils.isEmpty(henkyakuDate)) {
			henkyakuDate = shoriNengetsuYokugetsuMatsujitsu;

		} else {
			BigDecimal decimalHenkyakuDate = new BigDecimal(henkyakuDate);

			if (decimalHenkyakuDate.compareTo(decimalYokugetsuMatsujitsu) > 0) {
				henkyakuDate = shoriNengetsuYokugetsuMatsujitsu;
			}
		}

		BigDecimal rtnVal = BigDecimal.ZERO;

		if (Objects.equals(kaishiDate, shoriNengetsuYokugetsuShonichi)
				&& Objects.equals(shoriNengetsuYokugetsuMatsujitsu, henkyakuDate)) {
			rtnVal = chushajouShiyouryouGetsugaku;

		} else {
			String shoriNengetsuAdd2Month = skfDateFormatUtils.addYearMonth(shoriNengetsuYokugetsu, 1);
			BigDecimal decimalShoriNengetsuAdd2Month = new BigDecimal(shoriNengetsuAdd2Month);
			BigDecimal kaishiDateYymm = new BigDecimal(kaishiDate.substring(0, 6));
			BigDecimal henkyakuDateYymm = new BigDecimal(henkyakuDate.substring(0, 6));
			String shoriNengetsu = skfDateFormatUtils.addYearMonth(shoriNengetsuYokugetsu, -1);
			BigDecimal decimalShoriNengetsu = new BigDecimal(shoriNengetsu);

			if (decimalShoriNengetsuAdd2Month.compareTo(kaishiDateYymm) <= 0
					|| henkyakuDateYymm.compareTo(decimalShoriNengetsu) <= 0) {
				rtnVal = BigDecimal.ZERO;

			} else if (!Objects.equals(kaishiDate, shoriNengetsuYokugetsuShonichi)
					|| !Objects.equals(henkyakuDate, shoriNengetsuYokugetsuMatsujitsu)) {
				BigDecimal henkyakuDateDd = new BigDecimal(henkyakuDate.substring(6, 8));
				BigDecimal kaishiDateDd = new BigDecimal(kaishiDate.substring(6, 8));

				BigDecimal num_culc_1 = henkyakuDateDd.subtract(kaishiDateDd).add(BigDecimal.ONE);
				BigDecimal num_culc_2 = chushajouShiyouryouGetsugaku.multiply(num_culc_1);

				BigDecimal yokugetsuMatsujitsuDd = new BigDecimal(shoriNengetsuYokugetsuMatsujitsu.substring(6, 8));
				rtnVal = num_culc_2.divide(yokugetsuMatsujitsuDd, 0, RoundingMode.DOWN);
			}
		}

		return rtnVal;
	}

	/**
	 * 月別使用料履歴テーブル更新
	 * 
	 * @param shatakuShiyouryouGetsugaku
	 *            社宅使用料月額
	 * @param shatakuShiyouryouHiwari
	 *            社宅使用料日割
	 * @param kyoekihiTotal
	 *            個人負担共益費月額（調整後）
	 * @param tyushajoShiyouryouGetsugaku1
	 *            駐車場使用料月額（貸与番号1）
	 * @param tyushajoShiyouryouGetsugaku2
	 *            駐車場使用料月額（貸与番号2）
	 * @param tyushajoShiyouryouHiwari1
	 *            駐車場使用料日割（貸与番号1）
	 * @param tyushajoShiyouryouHiwari2
	 *            駐車場使用料日割（貸与番号2）
	 * @param updateUser
	 *            更新ユーザ
	 * @param shatakuKanriId
	 *            社宅管理台帳ID
	 * @param shoriNengetsu
	 *            処理年月
	 */
	private void updateTsukibetuShiyoryoRirekiData(Integer shatakuShiyouryouGetsugaku, Integer shatakuShiyouryouHiwari,
			Integer kyoekihiTotal, Integer tyushajoShiyouryouGetsugaku1, Integer tyushajoShiyouryouGetsugaku2,
			Integer tyushajoShiyouryouHiwari1, Integer tyushajoShiyouryouHiwari2, String updateUser,
			Long shatakuKanriId, String shoriNengetsu) {

		Skf3050Bt004UpdateTsukibetuShiyoryoRirekiDataExpParameter param = new Skf3050Bt004UpdateTsukibetuShiyoryoRirekiDataExpParameter();

		if (shatakuShiyouryouGetsugaku == null) {
			param.setRentalMonth(null);
		} else {
			param.setRentalMonth(shatakuShiyouryouGetsugaku);
		}

		if (shatakuShiyouryouHiwari == null) {
			param.setRentalDay(null);
		} else {
			param.setRentalDay(shatakuShiyouryouHiwari);
		}

		if (kyoekihiTotal == null) {
			param.setKyoekihiPersonTotal(null);
		} else {
			param.setKyoekihiPersonTotal(kyoekihiTotal);
		}

		if (tyushajoShiyouryouGetsugaku1 == null) {
			param.setParking1RentalMonth(null);
		} else {
			param.setParking1RentalMonth(tyushajoShiyouryouGetsugaku1);
		}

		if (tyushajoShiyouryouGetsugaku2 == null) {
			param.setParking2RentalMonth(null);
		} else {
			param.setParking2RentalMonth(tyushajoShiyouryouGetsugaku2);
		}

		if (tyushajoShiyouryouHiwari1 == null) {
			param.setParking1RentalDay(null);
		} else {
			param.setParking1RentalDay(tyushajoShiyouryouHiwari1);
		}

		if (tyushajoShiyouryouHiwari2 == null) {
			param.setParking2RentalDay(null);
		} else {
			param.setParking2RentalDay(tyushajoShiyouryouHiwari2);
		}

		param.setUpdateUser(updateUser);
		param.setShatakuKanriId(shatakuKanriId);
		param.setYearMonth(shoriNengetsu);
		param.setUpdateProgramId(confirmPositiveDataBatchPrgId);

		skf3050Bt004UpdateTsukibetuShiyoryoRirekiDataExpRepository.updateTsukibetuShiyoryoRirekiData(param);

	}

	/**
	 * 提示データとの結合データ取得
	 * 
	 * @param shoriNengetsu
	 *            年月
	 * @param shainNo
	 *            社員番号
	 * @return 取得件数
	 */
	private BigDecimal getTeijiJoinDataCnt(String shoriNengetsu, String shainNo) {

		Skf3050Bt004GetTeijiJoinDataCntExpParameter param = new Skf3050Bt004GetTeijiJoinDataCntExpParameter();

		if (shoriNengetsu == null) {
			param.setShoriNendo(null);
		} else {
			param.setShoriNendo(shoriNengetsu);
		}

		if (shainNo == null) {
			param.setShainNo(null);
		} else {
			param.setShainNo(shainNo);
		}

		BigDecimal outCnt = skf3050Bt004GetTeijiJoinDataCntExpRepository.getTeijiJoinDataCnt(param);

		return outCnt;
	}

	/**
	 * 月末日取得
	 * 
	 * @param inDate
	 *            処理年月(YYYYMM)
	 * @return 月末日(YYYYMMDD)
	 */
	private String getGetsumatsujitu(String inDate) {

		if (NfwStringUtils.isEmpty(inDate)) {
			return inDate;
		}

		Date dtEnd = skfDateFormatUtils.formatStringToDate(inDate + "01");

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dtEnd);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);

		Date changedDate = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat(SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT);

		String outDate = sdf.format(changedDate);

		return outDate;
	}
	
	//モバイルルーター機能追加対応 2021/09 add start
	/**
	 * 次月データ作成可否チェック
	 * 
	 * @param routerJoinDt
	 *            モバイルルーター使用料履歴データ
	 * @param shoriNengetsu
	 *            処理年月
	 * @return 次月データ作成可否フラグ（True:作成する, False:作成しない）
	 */
	private boolean checkRouterJigetsuDataSakusei(Skf3050Bt004GetRouterShiyoryoRirekiJoinDataExp routerJoinDt,String shoriNengetsu) {

		//次月データ作成フラグ
		boolean jigetsuFlg = false;

		//以下条件に当てはまる場合のみ後続処理を行う
		if (NfwStringUtils.isEmpty(routerJoinDt.getUseStopDay())) {
			//利用停止日が処理年月の翌月以上、または利用停止日がNULLの場合
			jigetsuFlg = true;

		} else {
			String taikyoYymm = routerJoinDt.getUseStopDay().substring(0, 6);
			String nextShoriNengetsu = skfDateFormatUtils.addYearMonth(shoriNengetsu, 1);

			if (Integer.parseInt(taikyoYymm) >= Integer.parseInt(nextShoriNengetsu)) {
				////利用停止日が処理年月+1以上
				jigetsuFlg = true;

			} 
		}

		return jigetsuFlg;
	}
	//モバイルルーター機能追加対応 2021/09 add end
}
