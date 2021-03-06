package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc004;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetAddressExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetAddressExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetApplHistoryInfoByParameterExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetApplHistoryInfoByParameterExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetApplHistoryInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetApplHistoryInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetApplHistoryInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetApplHistoryInfoForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetApplHistoryStatusInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetApplHistoryStatusInfoForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetApplNoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetApplNoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetAttachedFileInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetAttachedFileInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetCommentListExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetCommentListExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetNYDTaikyoCheckInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetNYDTaikyoCheckInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetSendMailInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetSendMailInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetShatakuInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetShatakuInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetShatakuKiboForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetShatakuKiboForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetShatakuTaikyoInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetShatakuTaikyoInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004InsertBihinHenkyakuInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004InsertTaikyoReportInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004UpdateApplHistoryAgreeStatusExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShain;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShainKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010MApplication;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010MApplicationKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplHistory;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TAttachedFile;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchi;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchiKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2030TBihinKiboShinsei;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2030TBihinKiboShinseiKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReport;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReportKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2050TBihinHenkyakuShinsei;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2050TBihinHenkyakuShinseiKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004DeleteCommentInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004GetAddressExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004GetApplHistoryInfoByParameterExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004GetApplHistoryInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004GetApplHistoryInfoForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004GetApplHistoryStatusInfoForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004GetApplNoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004GetAttachedFileInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004GetCommentInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004GetCommentListExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004GetNYDTaikyoCheckInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004GetSendMailInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004GetShatakuInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004GetShatakuKiboForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004GetShatakuTaikyoInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004InsertBihinHenkyakuInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004InsertTaikyoReportInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004UpdateApplHistoryAgreeStatusExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MShainRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010MApplicationRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TApplCommentRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TApplHistoryRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TAttachedFileRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2020TNyukyoChoshoTsuchiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2030TBihinKiboShinseiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2040TTaikyoReportRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2050TBihinHenkyakuShinseiRepository;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.NfwSendMailUtils;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtiles;
import jp.co.c_nexco.skf.common.util.SkfCommentUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;

@Service
public class Skf2010Sc004SharedService {

	private String companyCd = CodeConstant.C001;

	@Autowired
	private Skf2010Sc004GetShatakuInfoExpRepository skf2010Sc004GetShatakuInfoExpRepository;
	@Autowired
	private Skf2010Sc004GetShatakuTaikyoInfoExpRepository skf2010Sc004GetShatakuTaikyoInfoExpRepository;
	@Autowired
	private Skf2010Sc004GetApplHistoryStatusInfoForUpdateExpRepository skf2010Sc004GetApplHistoryStatusInfoForUpdateExpRepository;
	@Autowired
	private Skf2010Sc004GetAddressExpRepository skf2010Sc004GetAddressExpRepository;
	@Autowired
	private Skf2010Sc004GetShatakuKiboForUpdateExpRepository skf2010Sc004GetShatakuKiboForUpdateExpRepository;
	@Autowired
	private Skf2010Sc004GetNYDTaikyoCheckInfoExpRepository skf2010Sc004GetNYDTaikyoCheckInfoExpRepository;
	@Autowired
	private Skf2010Sc004GetApplHistoryInfoExpRepository skf2010Sc004GetApplHistoryInfoExpRepository;
	@Autowired
	private Skf2010Sc004GetApplHistoryInfoForUpdateExpRepository skf2010Sc004GetApplHistoryInfoForUpdateExpRepository;
	@Autowired
	private Skf2010Sc004GetSendMailInfoExpRepository skf2010Sc004GetSendMailInfoExpRepository;
	@Autowired
	private Skf2010Sc004InsertTaikyoReportInfoExpRepository skf2010Sc004InsertTaikyoReportInfoExpRepository;
	@Autowired
	private Skf2010Sc004InsertBihinHenkyakuInfoExpRepository skf2010Sc004InsertBihinHenkyakuInfoExpRepository;
	@Autowired
	private Skf2010Sc004UpdateApplHistoryAgreeStatusExpRepository skf2010Sc004UpdateApplHistoryAgreeStatusExpRepository;
	@Autowired
	private Skf2010Sc004GetCommentInfoExpRepository skf2010Sc004GetCommentInfoExpRepository;
	@Autowired
	private Skf2010Sc004GetCommentListExpRepository skf2010Sc004GetCommentListExpRepository;
	@Autowired
	private Skf2010Sc004DeleteCommentInfoExpRepository skf2010Sc004DeleteCommentInfoExpRepository;
	@Autowired
	private Skf2010Sc004GetAttachedFileInfoExpRepository skf2010Sc004GetAttachedFileInfoExpRepository;
	@Autowired
	private Skf2010Sc004GetApplNoExpRepository skf2010Sc004GetApplNoExpRepository;

	@Autowired
	private Skf2010TApplCommentRepository skf2010TApplCommentRepository;
	@Autowired
	private Skf2010TApplHistoryRepository skf2010TApplHistoryRepository;
	@Autowired
	private Skf2010Sc004GetApplHistoryInfoByParameterExpRepository skf2010Sc004GetApplHistoryInfoByParameterExpRepository;
	@Autowired
	private Skf2010MApplicationRepository skf2010MApplicationRepository;
	@Autowired
	private Skf1010MShainRepository skf1010MShainRepository;
	@Autowired
	private Skf2010TAttachedFileRepository skf2010TAttachedFileRepository;
	@Autowired
	private Skf2020TNyukyoChoshoTsuchiRepository skf2020TNyukyoChoshoTsuchiRepository;
	@Autowired
	private Skf2040TTaikyoReportRepository skf2040TTaikyoReportRepository;
	@Autowired
	private Skf2030TBihinKiboShinseiRepository skf2030TBihinKiboShinseiRepository;
	@Autowired
	private Skf2050TBihinHenkyakuShinseiRepository skf2050TBihinHenkyakuShinseiRepository;

	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private SkfAttachedFileUtiles skfAttachedFileUtiles;
	@Autowired
	private SkfCommentUtils skfCommentUtils;

	@Autowired
	private MenuScopeSessionBean menuScopeSessionBean;

	@Value("${skf.common.attached_file_session_key}")
	private String sessionKey;

	private final String ERR_MSG_APPL_NAME = "????????????????????????";
	private final String ERR_MSG_APPL_MAIL_INFO = "????????????????????????????????????";

	/**
	 * ????????????????????????????????????????????????
	 * 
	 * @param companyCd
	 * @param applNo
	 * @return
	 */
	public Skf2020TNyukyoChoshoTsuchi getNyukyoChoshoTsuchiInfo(String applNo) {
		Skf2020TNyukyoChoshoTsuchi nyukyoChoshoTsuchiInfo = new Skf2020TNyukyoChoshoTsuchi();
		Skf2020TNyukyoChoshoTsuchiKey key = new Skf2020TNyukyoChoshoTsuchiKey();
		key.setCompanyCd(companyCd);
		key.setApplNo(applNo);
		nyukyoChoshoTsuchiInfo = skf2020TNyukyoChoshoTsuchiRepository.selectByPrimaryKey(key);
		return nyukyoChoshoTsuchiInfo;
	}

	public Skf2010Sc004GetShatakuTaikyoInfoExp getShatakuTaikyoInfo(String applNo) {
		Skf2010Sc004GetShatakuTaikyoInfoExp shatakuTaikyoInfo = new Skf2010Sc004GetShatakuTaikyoInfoExp();
		Skf2010Sc004GetShatakuTaikyoInfoExpParameter param = new Skf2010Sc004GetShatakuTaikyoInfoExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		shatakuTaikyoInfo = skf2010Sc004GetShatakuTaikyoInfoExpRepository.getShatakuTaikyoInfo(param);

		return shatakuTaikyoInfo;
	}

	/**
	 * ?????????????????????????????????????????????????????????????????????
	 * 
	 * @param applNo
	 * @return
	 */
	public Skf2040TTaikyoReport getTaikkyoReportInfo(String applNo) {
		Skf2040TTaikyoReport taikyoReportInfo = new Skf2040TTaikyoReport();
		Skf2040TTaikyoReportKey key = new Skf2040TTaikyoReportKey();
		key.setCompanyCd(companyCd);
		key.setApplNo(applNo);
		taikyoReportInfo = skf2040TTaikyoReportRepository.selectByPrimaryKey(key);
		return taikyoReportInfo;
	}

	/**
	 * ??????????????????????????????
	 * 
	 * @param shatakuKanriNo
	 * @param shainNo
	 * @return
	 */
	public Skf2010Sc004GetShatakuInfoExp getShatakuInfo(Long shatakuKanriNo, String shainNo) {
		Skf2010Sc004GetShatakuInfoExp shatakuInfo = new Skf2010Sc004GetShatakuInfoExp();
		Skf2010Sc004GetShatakuInfoExpParameter param = new Skf2010Sc004GetShatakuInfoExpParameter();

		Date nowDate = new Date();
		String yearMonth = skfDateFormatUtils.dateFormatFromDate(nowDate, "yyyyMM");
		param.setYearMonth(yearMonth);
		param.setShatakuKanriNo(shatakuKanriNo);
		param.setShainNo(shainNo);

		shatakuInfo = skf2010Sc004GetShatakuInfoExpRepository.getShatakuInfo(param);
		return shatakuInfo;
	}

	/**
	 * ?????????????????????????????????????????????
	 * 
	 * @param applNo
	 * @return
	 */
	public boolean updateApplHistoryCancel(String applNo, String applId) throws Exception {
		boolean result = true;

		// ?????????????????????????????????????????????????????????
		Skf2010Sc004GetApplHistoryStatusInfoForUpdateExp baseUpdateData = new Skf2010Sc004GetApplHistoryStatusInfoForUpdateExp();
		Skf2010Sc004GetApplHistoryStatusInfoForUpdateExpParameter param = new Skf2010Sc004GetApplHistoryStatusInfoForUpdateExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		param.setApplId(applId);
		baseUpdateData = skf2010Sc004GetApplHistoryStatusInfoForUpdateExpRepository
				.getApplHistoryStatusInfoForUpdate(param);
		if (baseUpdateData == null) {
			return false;
		}

		Skf2010Sc004UpdateApplHistoryAgreeStatusExp updateData = new Skf2010Sc004UpdateApplHistoryAgreeStatusExp();
		// ???????????????????????????
		updateData.setCompanyCd(companyCd);
		updateData.setApplNo(applNo);

		updateData.setApplStatus(CodeConstant.STATUS_ICHIJIHOZON);

		int updateResult = skf2010Sc004UpdateApplHistoryAgreeStatusExpRepository
				.updateApplHistoryAgreeStatus(updateData);
		if (updateResult <= 0) {
			result = false;
		}

		return result;
	}

	/**
	 * ?????????????????????????????????????????????
	 * 
	 * @param applNo
	 * @param applStatus
	 * @return
	 */
	public List<Skf2010Sc004GetCommentListExp> getApplCommentList(String applNo, String applStatus) {
		List<Skf2010Sc004GetCommentListExp> returnList = new ArrayList<Skf2010Sc004GetCommentListExp>();
		Skf2010Sc004GetCommentListExpParameter param = new Skf2010Sc004GetCommentListExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		if (applStatus != null && !CheckUtils.isEmpty(applStatus)) {
			param.setApplStatus(applStatus);
		}
		returnList = skf2010Sc004GetCommentListExpRepository.getCommentList(param);

		return returnList;
	}

	/**
	 * ???????????????????????????????????????
	 * 
	 * @param insertData
	 */
	public void insertAttachedFileInfo(Skf2010TAttachedFile insertData) {
		// ???????????????????????????????????????
		skf2010TAttachedFileRepository.insertSelective(insertData);
		return;
	}

	/**
	 * ???????????????????????????????????????????????????
	 * 
	 * @param applNo
	 * @param applId
	 * @return
	 */
	private String getBihinApplStatus(String applNo, String applId) {
		String result = "";
		//
		// switch (applId) {
		// case FunctionIdConstant.R0100:
		// List<Skf2010Sc004GetBKSApplStatusInfoExp> dataBks = new
		// ArrayList<Skf2010Sc004GetBKSApplStatusInfoExp>();
		// Skf2010Sc004GetBKSApplStatusInfoExpParameter paramBks = new
		// Skf2010Sc004GetBKSApplStatusInfoExpParameter();
		// paramBks.setCompanyCd(companyCd);
		// paramBks.setApplNo(applNo);
		// dataBks =
		// skf2010Sc004GetBKSApplStatusInfoExpRepository.getBKSApplStatusInfo(paramBks);
		// if (dataBks != null && dataBks.size() > 0) {
		// result = dataBks.get(0).getApplStatus();
		// }
		// break;
		// case FunctionIdConstant.R0103:
		// // ????????????????????????
		// List<Skf2010Sc004GetBHSApplStatusInfoExp> dataBhs = new
		// ArrayList<Skf2010Sc004GetBHSApplStatusInfoExp>();
		// Skf2010Sc004GetBHSApplStatusInfoExpParameter paramBhs = new
		// Skf2010Sc004GetBHSApplStatusInfoExpParameter();
		// paramBhs.setCompanyCd(companyCd);
		// paramBhs.setApplNo(applNo);
		// dataBhs =
		// skf2010Sc004GetBHSApplStatusInfoExpRepository.getBHSApplStatusInfo(paramBhs);
		// if (dataBhs != null && dataBhs.size() > 0) {
		// result = dataBhs.get(0).getApplStatus();
		// }
		//
		// }

		return result;
	}

	/**
	 * ????????????????????????????????????????????????????????????
	 * 
	 * @param shainNo
	 * @param nyutaikyoKbn
	 * @param applNo
	 * @param updateDate
	 * @param updateUser
	 * @return
	 */
	private boolean updateShatakuRental(String shainNo, String nyutaikyoKbn, String applNo, Date updateDate,
			String updateUser) {
		// List<Skf2010Sc004GetTeijiDataInfoExp> tTaijiDataList = new
		// ArrayList<Skf2010Sc004GetTeijiDataInfoExp>();
		// Skf2010Sc004GetTeijiDataInfoExpParameter param = new
		// Skf2010Sc004GetTeijiDataInfoExpParameter();
		// param.setShainNo(shainNo);
		// param.setNyutaikyoKbn(nyutaikyoKbn);
		// param.setApplNo(applNo);
		// tTaijiDataList =
		// skf2010Sc004GetTeijiDataInfoExpRepository.getTeijiDataInfo(param);
		// if (tTaijiDataList != null && tTaijiDataList.size() > 0) {
		// Skf2010Sc004GetTeijiDataInfoExp tTaijiData = tTaijiDataList.get(0);
		// if (tTaijiData.getRentalAdjust() != null) {
		// Skf2010Sc004UpdateNyukyoChoshoTsuchiRentalExp updateData = new
		// Skf2010Sc004UpdateNyukyoChoshoTsuchiRentalExp();
		// updateData.setCompanyCd(companyCd);
		// updateData.setApplNo(applNo);
		// if (tTaijiData.getRentalAdjust() != null) {
		// updateData.setNewKyoekihi(tTaijiData.getRentalAdjust().toString());
		// }
		// updateData.setKyoekihiPersonKyogichuFlg(tTaijiData.getKyoekihiPersonKyogichuFlg());
		// if (tTaijiData.getKyoekihiPersonAdjust() != null) {
		// updateData.setNewKyoekihi(tTaijiData.getKyoekihiPersonAdjust().toString());
		// }
		// updateData.setParkingRental(tTaijiData.getParkingRental1());
		// updateData.setParkingRental2(tTaijiData.getParkingRental2());
		// int res = skf2010Sc004UpdateNyukyoChoshoTsuchiRentalExpRepository
		// .updateNyukyoChoshoTsuchiRental(updateData);
		// if (res <= 0) {
		// return false;
		// }
		// }
		// }
		return true;
	}

	/**
	 * ????????????????????????????????????????????????????????????
	 * 
	 * @param sendMailKbn
	 * @param applInfo
	 * @param comment
	 * @param annai
	 * @param furikomiStartDate
	 * @param sendUser
	 * @param sendGroundId
	 * @throws Exception
	 */
	private void sendApplTsuchiMail(String sendMailKbn, Map<String, String> applInfo, String comment, String annai,
			String furikomiStartDate, String sendUser, String sendGroundId) throws Exception {

		// ????????????????????????
		Skf2010MApplication skf2010MApplication = new Skf2010MApplication();
		Skf2010MApplicationKey key = new Skf2010MApplicationKey();
		key.setCompanyCd(companyCd);
		key.setApplId(applInfo.get("applId"));
		skf2010MApplication = skf2010MApplicationRepository.selectByPrimaryKey(key);
		if (skf2010MApplication == null) {
			// ????????????????????????????????????????????????????????????
			return;
		}
		String applName = skf2010MApplication.getApplName();

		// ????????????????????????????????????
		String applShainName = "";
		String applDate = "";
		String applAgencyCd = "";

		// ????????????????????????????????????
		// Skf2010Sc004GetSendApplMailInfoExp skfMShainData = new
		// Skf2010Sc004GetSendApplMailInfoExp();
		// List<Skf2010Sc004GetSendApplMailInfoExp> skfMShainList = new
		// ArrayList<Skf2010Sc004GetSendApplMailInfoExp>();
		// Skf2010Sc004GetSendApplMailInfoExpParameter mShainParam = new
		// Skf2010Sc004GetSendApplMailInfoExpParameter();
		// mShainParam.setCompanyCd(companyCd);
		// mShainParam.setShainNo(applInfo.get("applShainNo"));
		// mShainParam.setApplNo(applInfo.get("applNo"));
		// skfMShainList =
		// skf2010Sc004GetSendApplMailInfoExpRepository.getSendApplMailInfo(mShainParam);
		// if (skfMShainList == null || skfMShainList.size() <= 0) {
		// // ????????????????????????????????????????????????????????????????????????
		// return;
		// }
		// skfMShainData = skfMShainList.get(0);
		//
		// if (!CheckUtils.isEmpty(skfMShainData.getName())) {
		// applShainName = skfMShainData.getName();
		// }
		// if (skfMShainData.getApplDate() != null) {
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		// applDate = sdf.format(skfMShainData.getApplDate());
		// }
		// if (!CheckUtils.isEmpty(skfMShainData.getAgencyCd())) {
		// applAgencyCd = skfMShainData.getAgencyCd();
		// }
		//
		// // ??????????????????????????????????????????????????????????????????
		// String outMailAddress = getApplOutMailInfo(applAgencyCd,
		// applInfo.get("applId"));
		//
		// // ???????????????
		// if (!CheckUtils.isEmpty(sendUser)) {
		// String mailAddress = getSendMailAddressByShainNo(companyCd,
		// sendUser);
		//
		// // URL?????????
		// String urlBase =
		// "/skf/Skf2010Sc005/init?SKF2010_SC005&menuflg=1&tokenCheck=0";
		// sendApplTsuchiMailExist(companyCd, sendMailKbn,
		// applInfo.get("applNo"), applDate, applShainName, comment,
		// annai, furikomiStartDate, urlBase, sendUser, mailAddress, null,
		// applName);
		// } else {
		// sendApplTsuchiMailExist(companyCd, sendMailKbn,
		// applInfo.get("applNo"), applDate, applShainName, comment,
		// annai, furikomiStartDate, null, sendUser, outMailAddress, null,
		// applName);
		//
		// }
		//
		return;
	}

	/**
	 * ???????????????????????????????????????????????????????????????
	 * 
	 * @param agencyCd
	 * @param applId
	 * @return
	 */
	private String getApplOutMailInfo(String agencyCd, String applId) {
		String mailAddress = "";

		// ?????????????????????????????????

		if (applId.equals(FunctionIdConstant.R0100)) {
			// ???????????????????????????
		} else if (applId.equals(FunctionIdConstant.R0105)) {
			// ??????????????????
		}

		return mailAddress;
	}

	/**
	 * ???????????????????????????????????????????????????
	 * 
	 * @param companyCd
	 * @param shainNo
	 * @return
	 */
	private String getSendMailAddressByShainNo(String companyCd, String shainNo) {
		String mailAddress = "";
		Skf1010MShainKey key = new Skf1010MShainKey();
		key.setCompanyCd(companyCd);
		key.setShainNo(shainNo);
		Skf1010MShain mShainData = skf1010MShainRepository.selectByPrimaryKey(key);
		if (mShainData != null) {
			mailAddress = mShainData.getMailAddress();
		}
		return mailAddress;
	}

	/**
	 * ???????????????????????????????????????
	 * 
	 * @param companyCd
	 * @param mailKbn
	 * @param applNo
	 * @param applDate
	 * @param applShainName
	 * @param comment
	 * @param annai
	 * @param furikomiStartDate
	 * @param urlBase
	 * @param sendUser
	 * @param mailAddress
	 * @param bccAddress
	 * @param applName
	 * @throws Exception
	 */
	private void sendApplTsuchiMailExist(String companyCd, String mailKbn, String applNo, String applDate,
			String applShainName, String comment, String annai, String furikomiStartDate, String urlBase,
			String sendUser, String mailAddress, String bccAddress, String applName) throws Exception {

		// ????????????????????????
		Map<String, String> replaceMap = new HashMap<String, String>();

		// ???????????????
		replaceMap.put("???to???", mailAddress); // ??????????????????????????????
		replaceMap.put("???applno???", applNo); // ??????????????????
		replaceMap.put("???shinseishorui???", applName); // ???????????????

		// ???????????????
		replaceMap.put("???shainname???", applShainName); // ???????????????
		replaceMap.put("???appldate???", applDate); // ?????????
		replaceMap.put("???reason???", comment); // ??????????????????????????????

		// ??????URL??????
		if (urlBase != null) {
			Map<String, String> urlMap = new HashMap<String, String>();
			String url = NfwSendMailUtils.createShotcutUrl(urlBase, urlMap, 2);
			replaceMap.put("???url???", url);
		}

		// ???????????????
		NfwSendMailUtils.sendMail("SKF_ML06", replaceMap);
		return;
	}

	/**
	 * ???????????????????????????
	 * 
	 * @param shainNo
	 * @return
	 */
	public String getAddressInfo(String shainNo) {
		String address = CodeConstant.NONE;
		Skf2010Sc004GetAddressExp addressData = new Skf2010Sc004GetAddressExp();
		Skf2010Sc004GetAddressExpParameter param = new Skf2010Sc004GetAddressExpParameter();
		param.setShainNo(shainNo);
		addressData = skf2010Sc004GetAddressExpRepository.getAddress(param);
		if (addressData != null) {
			address = addressData.getAddress();
		}

		return address;
	}

	public List<Skf2010Sc004GetShatakuKiboForUpdateExp> getShatakuKiboForUpdate(String applNo) {
		List<Skf2010Sc004GetShatakuKiboForUpdateExp> shatakuKibouList = new ArrayList<Skf2010Sc004GetShatakuKiboForUpdateExp>();

		Skf2010Sc004GetShatakuKiboForUpdateExpParameter shatakuKibouParam = new Skf2010Sc004GetShatakuKiboForUpdateExpParameter();
		shatakuKibouParam.setCompanyCd(companyCd);
		shatakuKibouParam.setApplNo(applNo);
		shatakuKibouList = skf2010Sc004GetShatakuKiboForUpdateExpRepository.getShatakuKiboForUpdate(shatakuKibouParam);

		return shatakuKibouList;
	}

	/**
	 * ???????????????????????????????????????????????????????????????
	 * ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
	 * 
	 * @param nyukyobi
	 * @param taikyobi
	 * @param shiyobi
	 * @param shiyobi2
	 * @param taikyoYotei
	 * @param nyukyoChangeFlag
	 * @param kaishiChangeFlg
	 * @param applNo
	 * @return
	 */
	public boolean updateTaikyobi(String nyukyobi, String taikyobi, String shiyobi, String shiyobi2, String taikyoYotei,
			String nyukyoChangeFlag, String kaishiChangeFlg, String applNo) {
		Skf2020TNyukyoChoshoTsuchi updData = new Skf2020TNyukyoChoshoTsuchi();
		// ??????????????????
		updData.setCompanyCd(companyCd); // ???????????????
		updData.setApplNo(applNo); // ????????????

		// ?????????
		if (!CheckUtils.isEmpty(nyukyobi)) {
			updData.setNyukyoYoteiDate(nyukyobi);
			updData.setNyukyoKanoDate(nyukyobi);
		}
		// ?????????
		if (!CheckUtils.isEmpty(taikyobi)) {
			updData.setTaikyoYoteiDate(taikyobi);
		}
		// ????????????????????????
		if (!CheckUtils.isEmpty(shiyobi)) {
			updData.setParkingUseDate(shiyobi);
		}
		// ????????????????????????2
		if (!CheckUtils.isEmpty(shiyobi2)) {
			updData.setParkingUseDate2(shiyobi2);
		}
		// ????????????
		if (!CheckUtils.isEmpty(taikyoYotei)) {
			updData.setTaikyoYotei(taikyoYotei);
		}
		// ????????????????????????
		if (!CheckUtils.isEmpty(nyukyoChangeFlag)) {
			updData.setNyukyoDateFlg(nyukyoChangeFlag);
		}
		// ????????????????????????
		if (!CheckUtils.isEmpty(kaishiChangeFlg)) {
			updData.setTaikyoDateFlg(kaishiChangeFlg);
		}

		int result = skf2020TNyukyoChoshoTsuchiRepository.updateByPrimaryKeySelective(updData);
		if (result <= 0) {
			return false;
		}
		return true;
	}

	/**
	 * ???????????? ???????????????????????????????????????????????????????????????
	 * 
	 * @param shainNo
	 * @param applNo
	 * @return
	 */
	public boolean getNYDTaikyoCheckInfo(String shainNo, String applNo) {
		boolean result = false;
		List<Skf2010Sc004GetNYDTaikyoCheckInfoExp> dataList = new ArrayList<Skf2010Sc004GetNYDTaikyoCheckInfoExp>();
		Skf2010Sc004GetNYDTaikyoCheckInfoExpParameter param = new Skf2010Sc004GetNYDTaikyoCheckInfoExpParameter();
		param.setShainNo(shainNo);
		param.setApplNo(applNo);
		param.setNyutaikyoKbn(CodeConstant.NYUTAIKYO_KBN_TAIKYO);
		dataList = skf2010Sc004GetNYDTaikyoCheckInfoExpRepository.getNYDTaikyoCheckInfo(param);
		if (dataList != null && dataList.size() > 0) {
			result = true;
		}
		return result;
	}

	/**
	 * ??????????????????????????????????????????????????????????????????
	 * 
	 * @param newApplNo
	 * @param applNo
	 * @param taikyoYotei
	 * @param shatakuKubun
	 * @param henkanbi
	 * @param nowAddress
	 * @param nowShatakuName
	 * @param taikyoChangeFlag
	 * @param henkanChangeFlag
	 * @return
	 */
	public boolean insertTaikyoReportInfo(String newApplNo, String applNo, String taikyoYotei, String shatakuKubun,
			String henkanbi, String nowAddress, String nowShatakuName, String taikyoChangeFlag,
			String henkanChangeFlag) {
		// ???????????????
		Skf2010Sc004InsertTaikyoReportInfoExp insertData = new Skf2010Sc004InsertTaikyoReportInfoExp();
		// ???????????????
		insertData.setCompanyCd(companyCd);
		// ???????????????
		insertData.setNewApplNo(newApplNo);
		// ????????????
		insertData.setApplNo(applNo);
		// ????????????
		insertData.setTaikyoYotei(taikyoYotei);
		// ??????????????????
		insertData.setShatakuTaikyoKbn(shatakuKubun);
		// ??????????????????
		insertData.setParkingHenkanDate(henkanbi);
		// ?????????
		insertData.setNowAddress(nowAddress);
		// ????????????(???????????????)
		insertData.setNowShatakuName(nowShatakuName);
		// ?????????????????????
		String insertUser = "";
		Map<String, String> loginUserInfo = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		if (CheckUtils.isEmpty(insertUser)) {
			insertUser = loginUserInfo.get("name");
		}
		insertData.setInsertUserId(insertUser);
		// ????????????????????????
		insertData.setTaikyoDateFlg(taikyoChangeFlag);
		// ?????????????????????????????????
		insertData.setParkingEDateFlg(henkanChangeFlag);

		int res = skf2010Sc004InsertTaikyoReportInfoExpRepository.insertTaikyoReportInfo(insertData);
		if (res <= 0) {
			return false;
		}
		return true;
	}

	public String getBihinHenkyakuInfo(String newApplNo) {
		String rtnApplNo = CodeConstant.NONE;

		Skf2050TBihinHenkyakuShinseiKey key = new Skf2050TBihinHenkyakuShinseiKey();
		Skf2050TBihinHenkyakuShinsei bihinHenkyakuShinsei = new Skf2050TBihinHenkyakuShinsei();
		key.setCompanyCd(companyCd);
		key.setApplNo(newApplNo);
		bihinHenkyakuShinsei = skf2050TBihinHenkyakuShinseiRepository.selectByPrimaryKey(key);
		if (bihinHenkyakuShinsei != null) {
			rtnApplNo = bihinHenkyakuShinsei.getApplNo();
		}

		return rtnApplNo;
	}

	/**
	 * ??????????????????????????????????????????????????????????????????
	 * 
	 * ????????????????????????????????????????????????????????????????????????
	 * 
	 * @param bihinHenkyakuShinseiApplNo
	 * @param applNo
	 * @param newApplNo
	 * @param updateFlg
	 * @return
	 */
	public boolean insertBihinHenkyakuInfo(String bihinHenkyakuShinseiApplNo, String applNo, String newApplNo,
			boolean updateFlg) {

		if (updateFlg) {
			// ??????????????????????????????????????????
			Skf2050TBihinHenkyakuShinseiKey key = new Skf2050TBihinHenkyakuShinseiKey();
			key.setCompanyCd(companyCd);
			key.setApplNo(newApplNo);
			int delRes = skf2050TBihinHenkyakuShinseiRepository.deleteByPrimaryKey(key);
			if (delRes <= 0) {
				return false;
			}
		}

		// ?????????????????????
		int insertCount = 0;
		Skf2010Sc004InsertBihinHenkyakuInfoExp insertData = new Skf2010Sc004InsertBihinHenkyakuInfoExp();
		insertData.setCompanyCd(companyCd);
		insertData.setApplNo(applNo);
		insertData.setBhsApplNo(newApplNo);
		insertData.setTaikyoApplNo(bihinHenkyakuShinseiApplNo);
		insertCount = skf2010Sc004InsertBihinHenkyakuInfoExpRepository.insertBihinHenkyakuInfo(insertData);
		if (insertCount <= 0) {
			return false;
		}
		return true;
	}

	public boolean insertApplHistoryInfo(String shainNo, String newApplNo, Date applDate) {
		Skf2010TApplHistory insData = new Skf2010TApplHistory();
		insData.setCompanyCd(companyCd);
		insData.setShainNo(shainNo);
		insData.setApplDate(applDate);
		insData.setApplId(FunctionIdConstant.R0103);
		insData.setApplStatus(CodeConstant.STATUS_SHINSEICHU);
		insData.setApplTacFlg(SkfCommonConstant.UNAVAILABLE);
		// TODO ?????????????????????????????????????????????????????????NOT_RENKEI???
		insData.setComboFlg(SkfCommonConstant.NOT_RENKEI);

		int res = skf2010TApplHistoryRepository.insertSelective(insData);
		if (res <= 0) {
			return false;
		}
		return true;
	}

	public boolean updateApplHistoryAgreeStatus(String applNo, Date applDate, String applTacFlg, String applStatus,
			String agreDate, String agreName1, String agreName2, Map<String, String> errorMsg) {
		Boolean result = true;

		// ?????????????????????????????????????????????????????????????????????????????????
		Skf2010Sc004GetApplHistoryInfoForUpdateExpParameter param = new Skf2010Sc004GetApplHistoryInfoForUpdateExpParameter();
		Skf2010Sc004GetApplHistoryInfoForUpdateExp lockData = new Skf2010Sc004GetApplHistoryInfoForUpdateExp();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		lockData = skf2010Sc004GetApplHistoryInfoForUpdateExpRepository.getApplHistoryInfoForUpdate(param);
		if (lockData == null) {
			// ??????????????????????????????
			errorMsg.put("error", MessageIdConstant.W_SKF_1009);
			return false;
		}
		// ????????????????????????????????????
		Skf2010Sc004UpdateApplHistoryAgreeStatusExp record = new Skf2010Sc004UpdateApplHistoryAgreeStatusExp();
		// ???????????????????????????
		record.setCompanyCd(companyCd);
		record.setApplNo(applNo);

		// ????????????????????????????????????????????????
		if (!CheckUtils.isEmpty(applTacFlg)) {
			record.setApplTacFlg(applTacFlg);
		}
		// ??????????????????????????????????????????
		if (applDate != null) {
			record.setApplDate(applDate);
		}
		// ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		if (!applStatus.equals(CodeConstant.STATUS_SHINSEICHU)
				&& !applStatus.equals(CodeConstant.STATUS_SHOZOKUCHO_KAKUNINZUMI)
				&& !applStatus.equals(CodeConstant.STATUS_HANSYUTSU_ZUMI)) {
			// ??????????????????????????????????????????
			if (CheckUtils.isEmpty(agreDate)) {
				record.setAgreDate(null);
			}
			// ????????????1?????????????????????????????????
			if (CheckUtils.isEmpty(agreName1)) {
				record.setAgreName1(CodeConstant.NONE);
			}
			// ????????????2?????????????????????????????????
			if (CheckUtils.isEmpty(agreName2)) {
				record.setAgreName2(CodeConstant.NONE);
			}
		}
		// ????????????
		record.setApplStatus(applStatus);

		int res = skf2010Sc004UpdateApplHistoryAgreeStatusExpRepository.updateApplHistoryAgreeStatus(record);
		if (res <= 0) {
			// ??????????????????????????????
			errorMsg.put("error", MessageIdConstant.E_SKF_1075);
			return false;
		}
		return result;
	}

	/**
	 * ??????????????????????????????????????????
	 * 
	 * @param applNo
	 * @param applStatus
	 * @param commentName
	 * @param commentNote
	 * @param errorMsg
	 * @return
	 */
	public boolean updateCommentInfo(String applNo, String applStatus, String commentName, String commentNote,
			Map<String, String> errorMsg) {
		// ??????2???????????????4???????????????1????????????
		if (applStatus.equals(CodeConstant.STATUS_SHONIN2) || applStatus.equals(CodeConstant.STATUS_SHONIN_ZUMI)) {
			applStatus = CodeConstant.STATUS_SHONIN1;
		}

		// ??????????????????????????????
		Date commentUpdateDate = null;
		List<SkfCommentUtilsGetCommentInfoExp> commentInfoList = new ArrayList<SkfCommentUtilsGetCommentInfoExp>();
		commentInfoList = skfCommentUtils.getCommentInfo(companyCd, applNo, applStatus);
		if (commentInfoList != null && commentInfoList.size() > 0) {
			commentUpdateDate = commentInfoList.get(0).getCommentDate();
		}
		// ??????????????????????????????????????????????????????1????????????????????????????????????????????????????????????????????????????????????????????????
		if (commentUpdateDate != null && (applStatus.equals(CodeConstant.STATUS_SHONIN1)
				|| applStatus.equals(CodeConstant.STATUS_SHONIN_ZUMI))) {
			boolean delRes = skfCommentUtils.deleteComment(companyCd, applNo, applStatus, errorMsg);
			if (!delRes) {
				return false;
			}
		}
		// ?????????????????????????????????????????????????????????????????????
		if (CheckUtils.isEmpty(commentNote)) {
			return true;
		}

		// ???????????????????????????
		boolean commentResult = true;
		commentResult = skfCommentUtils.insertComment(companyCd, applNo, applStatus, commentName, commentNote,
				errorMsg);

		return commentResult;
	}

	/**
	 * ??????????????????????????????????????????????????????????????????????????????
	 * 
	 * @param mailKbn
	 * @param applInfo
	 * @param comment
	 * @return
	 */
	public boolean sendApplTsuchiMailShiShaJimuShoTanto(String mailKbn, Map<String, String> applInfoMap, String comment,
			Map<String, String> errorMsg) {
		String applId = applInfoMap.get("applId");

		// ????????????????????????
		Skf2010MApplication applInfo = new Skf2010MApplication();
		Skf2010MApplicationKey key = new Skf2010MApplicationKey();
		key.setCompanyCd(companyCd);
		key.setApplId(applId);
		applInfo = skf2010MApplicationRepository.selectByPrimaryKey(key);
		if (applInfo == null || CheckUtils.isEmpty(applInfo.getApplName())) {
			errorMsg.put("error", MessageIdConstant.E_SKF_1066);
			errorMsg.put("errorValue", ERR_MSG_APPL_NAME);
			return false;
		}
		// ????????????????????????????????????????????????????????????????????????
		String applShainName = CodeConstant.NONE;
		String applDate = CodeConstant.NONE;
		String applAgencyCd = CodeConstant.NONE;
		String applAffiliation1Cd = CodeConstant.NONE;

		Skf2010Sc004GetSendMailInfoExp sendMailInfo = new Skf2010Sc004GetSendMailInfoExp();
		Skf2010Sc004GetSendMailInfoExpParameter sendMailParam = new Skf2010Sc004GetSendMailInfoExpParameter();
		sendMailParam.setCompanyCd(companyCd);
		sendMailParam.setApplNo(applInfoMap.get("applNo"));
		sendMailParam.setShainNo(applInfoMap.get("applShainNo"));
		sendMailInfo = skf2010Sc004GetSendMailInfoExpRepository.getSendMailInfo(sendMailParam);
		if (sendMailInfo == null) {
			errorMsg.put("error", MessageIdConstant.E_SKF_1066);
			errorMsg.put("errorValue", ERR_MSG_APPL_MAIL_INFO);
			return false;
		}
		if (!CheckUtils.isEmpty(sendMailInfo.getName())) {
			applShainName = sendMailInfo.getName();
		}
		if (sendMailInfo.getApplDate() != null) {
			applDate = skfDateFormatUtils.dateFormatFromDate(sendMailInfo.getApplDate(), "yyyy/MM/dd");
		}
		if (!CheckUtils.isEmpty(sendMailInfo.getAgencyCd())) {
			applAgencyCd = sendMailInfo.getAgencyCd();
		}
		if (!CheckUtils.isEmpty(sendMailInfo.getAffiliation1Cd())) {
			applAffiliation1Cd = sendMailInfo.getAffiliation1Cd();
		}

		// getSendMailAddressByShiShaJimuShoTanto(applAgencyCd,
		// applAffiliation1Cd);

		return true;
	}

	public Skf2010Sc004GetApplHistoryInfoByParameterExp getApplHistoryInfo(String applNo) {
		Skf2010Sc004GetApplHistoryInfoByParameterExpParameter param = new Skf2010Sc004GetApplHistoryInfoByParameterExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		// ????????????????????????????????????
		Skf2010Sc004GetApplHistoryInfoByParameterExp tApplHistoryData = new Skf2010Sc004GetApplHistoryInfoByParameterExp();
		tApplHistoryData = skf2010Sc004GetApplHistoryInfoByParameterExpRepository.getApplHistoryInfoByParameter(param);

		return tApplHistoryData;
	}

	/**
	 * ??????????????????????????????????????????????????????????????????
	 * 
	 * @param applNo
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getAttachedFileInfo(String applNo) {
		List<Map<String, Object>> resultAttachedFileList = null;

		resultAttachedFileList = (List<Map<String, Object>>) menuScopeSessionBean.get(sessionKey);
		if (resultAttachedFileList != null) {
			return resultAttachedFileList;
		}
		// ?????????
		resultAttachedFileList = new ArrayList<Map<String, Object>>();

		// ???????????????????????????????????????????????????????????????????????????
		List<Skf2010Sc004GetAttachedFileInfoExp> attachedFileList = new ArrayList<Skf2010Sc004GetAttachedFileInfoExp>();
		Skf2010Sc004GetAttachedFileInfoExpParameter param = new Skf2010Sc004GetAttachedFileInfoExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		attachedFileList = skf2010Sc004GetAttachedFileInfoExpRepository.getAttachedFileInfo(param);
		if (attachedFileList != null && attachedFileList.size() > 0) {
			int attachedNo = 0;
			Map<String, Object> attachedFileMap = null;
			for (Skf2010Sc004GetAttachedFileInfoExp attachedFileInfo : attachedFileList) {
				attachedFileMap = new HashMap<String, Object>();
				// ??????????????????
				attachedFileMap.put("attachedNo", String.valueOf(attachedNo));
				// ???????????????
				attachedFileMap.put("attachedName", attachedFileInfo.getAttachedName());
				// ?????????????????????
				attachedFileMap.put("fileSize", attachedFileInfo.getFileSize());
				// ?????????
				String applDate = skfDateFormatUtils.dateFormatFromDate(attachedFileInfo.getApplDate(),
						"yyyy/MM/dd HH:mm:ss.SS");
				attachedFileMap.put("applDate", applDate);
				// ????????????
				attachedFileMap.put("fileStream", attachedFileInfo.getFileStream());
				// ?????????????????????
				String fileType = skfAttachedFileUtiles.getFileTypeInfo(attachedFileInfo.getAttachedName());
				attachedFileMap.put("fileType", fileType);

				resultAttachedFileList.add(attachedFileMap);

				attachedNo++;
			}

			// ???????????????????????????????????????????????????????????????
			menuScopeSessionBean.put(sessionKey, resultAttachedFileList);
		}
		return resultAttachedFileList;
	}

	public String insertApplHistoryBihinKibo(String applNo, String applId, String shainNo,
			Map<String, String> errorMsg) {
		String applNoBihinShinsei = CodeConstant.NONE;
		// ?????????????????????
		Date saveDate = new Date();

		if (applId.equals(FunctionIdConstant.R0100)) {
			// ?????????????????????????????????????????????????????????
			// ?????????????????????????????????????????????????????????
			Skf2030TBihinKiboShinsei bihinKiboShinsei = new Skf2030TBihinKiboShinsei();
			Skf2030TBihinKiboShinseiKey key = new Skf2030TBihinKiboShinseiKey();
			key.setCompanyCd(companyCd);
			key.setApplNo(applNo);
			bihinKiboShinsei = skf2030TBihinKiboShinseiRepository.selectByPrimaryKey(key);
			if (bihinKiboShinsei == null) {
				// ?????????????????????????????????????????????????????????
				return applNoBihinShinsei;
			}
			// ??????????????????????????????????????????????????????
			applNoBihinShinsei = bihinKiboShinsei.getApplNo();
			Skf2010Sc004GetApplHistoryInfoExp applHistoryInfo = new Skf2010Sc004GetApplHistoryInfoExp();
			Skf2010Sc004GetApplHistoryInfoExpParameter param = new Skf2010Sc004GetApplHistoryInfoExpParameter();
			param.setCompanyCd(companyCd);
			param.setApplNo(applNoBihinShinsei);
			applHistoryInfo = skf2010Sc004GetApplHistoryInfoExpRepository.getApplHistoryInfo(param);
			// ????????????????????????????????????????????????????????????
			if (applHistoryInfo != null) {
				// ??????????????????????????????????????????
				return applNoBihinShinsei;
			}
			// ?????????????????????????????????????????????
			Skf2010TApplHistory record = new Skf2010TApplHistory();
			record.setCompanyCd(companyCd);
			record.setShainNo(shainNo);
			record.setApplNo(applNoBihinShinsei);
			record.setApplId(FunctionIdConstant.R0104);
			record.setApplStatus(CodeConstant.STATUS_ICHIJIHOZON);
			// TODO ?????????????????????????????????????????????????????????NOT_RENKEI???
			record.setComboFlg(SkfCommonConstant.NOT_RENKEI);
			record.setApplDate(saveDate);

			int insRes = skf2010TApplHistoryRepository.insertSelective(record);
			if (insRes <= 0) {
				errorMsg.put("error", MessageIdConstant.E_SKF_1073);
				return null;
			}

		}

		return applNoBihinShinsei;
	}

	/**
	 * ??????????????????????????????
	 * 
	 * @param shainNo
	 * @param applId
	 * @return
	 */
	public String getApplNo(String shainNo, String applId) {
		String newApplNo = CodeConstant.NONE;
		Skf2010Sc004GetApplNoExp data = new Skf2010Sc004GetApplNoExp();
		Skf2010Sc004GetApplNoExpParameter param = new Skf2010Sc004GetApplNoExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplId(applId);
		param.setShainNo(shainNo);
		data = skf2010Sc004GetApplNoExpRepository.getApplNo(param);
		if (data != null) {
			newApplNo = data.getMaxApplNo();
		}
		return newApplNo;
	}
}
