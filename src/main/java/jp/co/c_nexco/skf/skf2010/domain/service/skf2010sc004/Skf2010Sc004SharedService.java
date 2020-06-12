package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc004;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
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
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetAttachedFileInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetAttachedFileInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetBihinShinseiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004GetBihinShinseiInfoExpParameter;
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
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBatchUtils.SkfBatchUtilsGetMultipleTablesUpdateDateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBihinInfoUtils.SkfBihinInfoUtilsGetBihinInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010MApplication;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010MApplicationKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplHistory;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TAttachedFile;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchi;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchiKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReport;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReportKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2050TBihinHenkyakuShinsei;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2050TBihinHenkyakuShinseiKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004GetAddressExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004GetApplHistoryInfoByParameterExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004GetApplHistoryInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004GetApplHistoryInfoForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004GetApplHistoryStatusInfoForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004GetAttachedFileInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004GetBihinShinseiInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004GetCommentListExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004GetNYDTaikyoCheckInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004GetSendMailInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004GetShatakuInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004GetShatakuKiboForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004GetShatakuTaikyoInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004InsertBihinHenkyakuInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004InsertTaikyoReportInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc004.Skf2010Sc004UpdateApplHistoryAgreeStatusExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010MApplicationRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TApplHistoryRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TAttachedFileRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2020TNyukyoChoshoTsuchiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2040TTaikyoReportRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2050TBihinHenkyakuShinseiRepository;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtils;
import jp.co.c_nexco.skf.common.util.SkfBihinInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfCommentUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfShinseiUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2020Fc001NyukyoKiboSinseiDataImport;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2040Fc001TaikyoTodokeDataImport;

/**
 * Skf2010Sc004 申請内容表示/引戻し共通処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc004SharedService {

	protected final String KEY_LAST_UPDATE_DATE_HISTORY = "skf2010_t_appl_history";

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
	private Skf2010Sc004GetCommentListExpRepository skf2010Sc004GetCommentListExpRepository;
	@Autowired
	private Skf2010Sc004GetAttachedFileInfoExpRepository skf2010Sc004GetAttachedFileInfoExpRepository;
	@Autowired
	private Skf2010Sc004GetBihinShinseiInfoExpRepository skf2010Sc004GetBihinShinseiInfoExpRepository;
	@Autowired
	private Skf2010TApplHistoryRepository skf2010TApplHistoryRepository;
	@Autowired
	private Skf2010Sc004GetApplHistoryInfoByParameterExpRepository skf2010Sc004GetApplHistoryInfoByParameterExpRepository;
	@Autowired
	private Skf2010MApplicationRepository skf2010MApplicationRepository;
	@Autowired
	private Skf2010TAttachedFileRepository skf2010TAttachedFileRepository;
	@Autowired
	private Skf2020TNyukyoChoshoTsuchiRepository skf2020TNyukyoChoshoTsuchiRepository;
	@Autowired
	private Skf2040TTaikyoReportRepository skf2040TTaikyoReportRepository;
	@Autowired
	private Skf2050TBihinHenkyakuShinseiRepository skf2050TBihinHenkyakuShinseiRepository;

	@Autowired
	private Skf2020Fc001NyukyoKiboSinseiDataImport skf2020Fc001NyukyoKiboSinseiDataImport;
	@Autowired
	private Skf2040Fc001TaikyoTodokeDataImport skf2040Fc001TaikyoTodokeDataImport;

	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private SkfAttachedFileUtils skfAttachedFileUtils;
	@Autowired
	private SkfCommentUtils skfCommentUtils;
	@Autowired
	private SkfShinseiUtils skfShinseiUtils;
	@Autowired
	private SkfBihinInfoUtils skfBihinInfoUtils;

	@Autowired
	private MenuScopeSessionBean menuScopeSessionBean;

	private String sessionKey = SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY;

	private final String ERR_MSG_APPL_NAME = "申請書類名の取得";
	private final String ERR_MSG_APPL_MAIL_INFO = "申請通知メール情報の取得";

	/**
	 * 社宅入居希望等申請情報を取得する
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
	 * 退居（自動車の保管場所返還）届情報を取得します
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
	 * 社宅情報を取得します
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
	 * 申請履歴を一時保存に変更します
	 * 
	 * @param applNo
	 * @param errMsg
	 * @return
	 */
	public boolean updateApplHistoryCancel(String applNo, String applId, Date lastUpdateDate,
			Map<String, String> errMsg) throws Exception {
		boolean result = true;

		// 更新対象のデータを取得（行ロック実施）
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

		// 排他チェック
		if (!CheckUtils.isEqual(baseUpdateData.getUpdateDate(), lastUpdateDate)) {
			errMsg.put("error", MessageIdConstant.E_SKF_1135);
			return false;
		}

		Skf2010Sc004UpdateApplHistoryAgreeStatusExp updateData = new Skf2010Sc004UpdateApplHistoryAgreeStatusExp();
		// 更新対象のキー設定
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
	 * 申請コメントの一覧を取得します
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
	 * 添付ファイル情報を登録する
	 * 
	 * @param insertData
	 */
	public void insertAttachedFileInfo(Skf2010TAttachedFile insertData) {
		// 添付ファイル情報を登録する
		skf2010TAttachedFileRepository.insertSelective(insertData);
		return;
	}

	/**
	 * 現住所を取得します
	 * 
	 * @param shainNo
	 * @return
	 */
	public String getAddressInfo(String shainNo) {
		String address = CodeConstant.NONE;
		List<Skf2010Sc004GetAddressExp> addressData = new ArrayList<Skf2010Sc004GetAddressExp>();
		Skf2010Sc004GetAddressExpParameter param = new Skf2010Sc004GetAddressExpParameter();
		param.setShainNo(shainNo);
		addressData = skf2010Sc004GetAddressExpRepository.getAddress(param);
		if (addressData != null && addressData.size() > 0) {
			address = addressData.get(0).getAddress();
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
	 * 入居希望等調書・入居決定通知テーブルの更新
	 * 更新対象：入居可能日、駐車場利用可能日、入居日変更フラグ、退居日変更フラグ、保管場所利用開始日変更フラグ
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
			String nyukyoChangeFlag, String taikyoChangeFlg, String kaishiChangeFlg, String henkanChangeFlg,
			String applNo) {
		Skf2020TNyukyoChoshoTsuchi updData = new Skf2020TNyukyoChoshoTsuchi();
		Skf2020TNyukyoChoshoTsuchiKey key = new Skf2020TNyukyoChoshoTsuchiKey();
		key.setCompanyCd(companyCd);
		key.setApplNo(applNo);
		updData = skf2020TNyukyoChoshoTsuchiRepository.selectByPrimaryKey(key);
		if (updData == null) {
			return false;
		}

		// 更新条件項目
		updData.setCompanyCd(companyCd); // 会社コード
		updData.setApplNo(applNo); // 申請番号

		// 制約日
		updData.setSeiyakuDate(
				skfDateFormatUtils.dateFormatFromDate(new Date(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT));

		// 入居日
		if (!CheckUtils.isEmpty(nyukyobi)) {
			updData.setNyukyoYoteiDate(nyukyobi);
			updData.setNyukyoKanoDate(nyukyobi);
		}
		// 退居日
		if (!CheckUtils.isEmpty(taikyobi)) {
			updData.setTaikyoYoteiDate(taikyobi);
		}
		// 駐車場使用開始日
		if (!CheckUtils.isEmpty(shiyobi)) {
			updData.setParkingUseDate(shiyobi);
			updData.setParkingKanoDate(shiyobi);
		} else {
			updData.setParkingUseDate(null);
			updData.setParkingKanoDate(null);
		}
		// 駐車場使用開始日2
		if (!CheckUtils.isEmpty(shiyobi2)) {
			updData.setParkingUseDate2(shiyobi2);
			updData.setParkingKanoDate2(shiyobi2);
		} else {
			updData.setParkingUseDate2(null);
			updData.setParkingKanoDate2(null);
		}
		// 退居予定
		if (!CheckUtils.isEmpty(taikyoYotei)) {
			updData.setTaikyoYotei(taikyoYotei);
		}
		// 入居日変更フラグ
		if (!CheckUtils.isEmpty(nyukyoChangeFlag)) {
			updData.setNyukyoDateFlg(nyukyoChangeFlag);
		}
		// 駐車場使用開始日変更フラグ
		if (!CheckUtils.isEmpty(kaishiChangeFlg)) {
			updData.setParkingSDateFlg(kaishiChangeFlg);
		}
		// 退居日変更フラグ
		if (!CheckUtils.isEmpty(taikyoChangeFlg)) {
			updData.setTaikyoDateFlg(taikyoChangeFlg);
		}

		int result = skf2020TNyukyoChoshoTsuchiRepository.updateByPrimaryKey(updData);
		if (result <= 0) {
			return false;
		}
		return true;
	}

	/**
	 * 社宅管理 入退居予定データ退居仮レコード判定メソッド
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
	 * 退居（自動車の保管場所返還）届の登録をします
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
		// マッピング
		Skf2010Sc004InsertTaikyoReportInfoExp insertData = new Skf2010Sc004InsertTaikyoReportInfoExp();
		// 会社コード
		insertData.setCompanyCd(companyCd);
		// 新申請番号
		insertData.setNewApplNo(newApplNo);
		// 申請番号
		insertData.setApplNo(applNo);
		// 退居予定
		insertData.setTaikyoYotei(taikyoYotei);
		// 社宅退居区分
		insertData.setShatakuTaikyoKbn(shatakuKubun);
		// 駐車場返還日
		insertData.setParkingHenkanDate(henkanbi);
		// 現住所
		insertData.setNowAddress(nowAddress);
		// 現社宅名(付加文字列)
		insertData.setNowShatakuName(nowShatakuName);
		// 登録ユーザーID
		String insertUserId = CodeConstant.NONE;
		Map<String, String> loginUserInfo = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		insertUserId = loginUserInfo.get("userCd");

		insertData.setInsertUserId(insertUserId);
		insertData.setInsertProgramId(FunctionIdConstant.SKF2010_SC004);
		// 退居日変更フラグ
		insertData.setTaikyoDateFlg(taikyoChangeFlag);
		// 駐車場返還日変更フラグ
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
	 * 備品返却申請テーブルへの書き込みを行います。
	 * 
	 * また、更新では既存データの削除を最初に行います。
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
			// 更新時は既存データを削除する
			Skf2050TBihinHenkyakuShinseiKey key = new Skf2050TBihinHenkyakuShinseiKey();
			key.setCompanyCd(companyCd);
			key.setApplNo(bihinHenkyakuShinseiApplNo);
			int delRes = skf2050TBihinHenkyakuShinseiRepository.deleteByPrimaryKey(key);
			if (delRes <= 0) {
				return false;
			}
		}

		// データ投入処理
		int insertCount = 0;
		String saveDate = skfDateFormatUtils.dateFormatFromDate(new Date(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT);
		Skf2010Sc004InsertBihinHenkyakuInfoExp insertData = new Skf2010Sc004InsertBihinHenkyakuInfoExp();
		// ログインセッションのユーザ情報
		Map<String, String> userInfoMap = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		String userId = userInfoMap.get("userCd");
		insertData.setCompanyCd(companyCd);
		insertData.setApplNo(applNo);
		insertData.setBhsApplNo(bihinHenkyakuShinseiApplNo);
		insertData.setSaveDate(saveDate);
		insertData.setTaikyoApplNo(newApplNo);
		insertData.setInsertUserId(userId);
		insertData.setInsertProgramId(FunctionIdConstant.SKF2010_SC004);
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
		insData.setApplNo(newApplNo);
		insData.setApplDate(applDate);
		insData.setApplId(FunctionIdConstant.R0103);
		insData.setApplStatus(CodeConstant.STATUS_SHINSEICHU);
		insData.setApplTacFlg(SkfCommonConstant.UNAVAILABLE);
		// TODO 連携フラグをどうするか？（現行は初期はNOT_RENKEI）
		insData.setComboFlg(SkfCommonConstant.NOT_RENKEI);

		int res = skf2010TApplHistoryRepository.insertSelective(insData);
		if (res <= 0) {
			return false;
		}
		return true;
	}

	public boolean updateApplHistoryAgreeStatus(String applNo, Date applDate, String applTacFlg, String applStatus,
			String agreDate, String agreName1, String agreName2, Date lastUpdateDate, Map<String, String> errorMsg) {
		Boolean result = true;

		Map<String, String> loginUserInfo = skfLoginUserInfoUtils
				.getSkfLoginUserInfoFromAlterLogin(menuScopeSessionBean);

		// 申請書類履歴テーブルの楽観的排他（データのロック処理）
		Skf2010Sc004GetApplHistoryInfoForUpdateExpParameter param = new Skf2010Sc004GetApplHistoryInfoForUpdateExpParameter();
		Skf2010Sc004GetApplHistoryInfoForUpdateExp lockData = new Skf2010Sc004GetApplHistoryInfoForUpdateExp();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		lockData = skf2010Sc004GetApplHistoryInfoForUpdateExpRepository.getApplHistoryInfoForUpdate(param);
		if (lockData == null) {
			// データ取得エラー
			errorMsg.put("error", MessageIdConstant.W_SKF_1009);
			return false;
		}
		// 排他チェック
		if (!CheckUtils.isEqual(lockData.getUpdateDate(), lastUpdateDate)) {
			errorMsg.put("error", MessageIdConstant.E_SKF_1135);
			return false;
		}

		// 更新するパラメータの設定
		Skf2010Sc004UpdateApplHistoryAgreeStatusExp record = new Skf2010Sc004UpdateApplHistoryAgreeStatusExp();
		// プライマリキー設定
		record.setCompanyCd(companyCd);
		record.setApplNo(applNo);

		// 添付書類有無が設定されている場合
		if (!CheckUtils.isEmpty(applTacFlg)) {
			record.setApplTacFlg(applTacFlg);
		}
		// 申請日時が設定されている場合
		if (applDate != null) {
			record.setApplDate(applDate);
		}
		// ステータスが申請又は所属長確認済み、搬出済み以外の場合（申請時は承認情報を空にする）
		if (!applStatus.equals(CodeConstant.STATUS_SHINSEICHU)
				&& !applStatus.equals(CodeConstant.STATUS_SHOZOKUCHO_KAKUNINZUMI)
				&& !applStatus.equals(CodeConstant.STATUS_HANSYUTSU_ZUMI)) {
			// 承認日が設定されていない場合
			if (CheckUtils.isEmpty(agreDate)) {
				record.setAgreDate(null);
			}
			// 承認者名1が設定されていない場合
			if (CheckUtils.isEmpty(agreName1)) {
				record.setAgreName1(CodeConstant.NONE);
			}
			// 承認者名2が設定されていない場合
			if (CheckUtils.isEmpty(agreName2)) {
				record.setAgreName2(CodeConstant.NONE);
			}
		}
		// 申請状況
		record.setApplStatus(applStatus);
		// 更新者名
		record.setUpdateUserId(loginUserInfo.get("userName"));

		int res = skf2010Sc004UpdateApplHistoryAgreeStatusExpRepository.updateApplHistoryAgreeStatus(record);
		if (res <= 0) {
			// 更新エラーメッセージ
			errorMsg.put("error", MessageIdConstant.E_SKF_1075);
			return false;
		}
		return result;
	}

	/**
	 * 支社担当者、事務所担当者に各種通知メールを送信する。
	 * 
	 * @param mailKbn
	 * @param applInfo
	 * @param comment
	 * @return
	 */
	public boolean sendApplTsuchiMailShiShaJimuShoTanto(String mailKbn, Map<String, String> applInfoMap, String comment,
			Map<String, String> errorMsg) {
		String applId = applInfoMap.get("applId");

		// 申請書類名を取得
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
			sendMailInfo.getName();
		}
		if (sendMailInfo.getApplDate() != null) {
			skfDateFormatUtils.dateFormatFromDate(sendMailInfo.getApplDate(), "yyyy/MM/dd");
		}
		if (!CheckUtils.isEmpty(sendMailInfo.getAgencyCd())) {
			sendMailInfo.getAgencyCd();
		}
		if (!CheckUtils.isEmpty(sendMailInfo.getAffiliation1Cd())) {
			sendMailInfo.getAffiliation1Cd();
		}

		// getSendMailAddressByShiShaJimuShoTanto(applAgencyCd,
		// applAffiliation1Cd);

		return true;
	}

	public Skf2010Sc004GetApplHistoryInfoByParameterExp getApplHistoryInfo(String applNo) {
		Skf2010Sc004GetApplHistoryInfoByParameterExpParameter param = new Skf2010Sc004GetApplHistoryInfoByParameterExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		// 更新対象の申請情報を取得
		Skf2010Sc004GetApplHistoryInfoByParameterExp tApplHistoryData = new Skf2010Sc004GetApplHistoryInfoByParameterExp();
		tApplHistoryData = skf2010Sc004GetApplHistoryInfoByParameterExpRepository.getApplHistoryInfoByParameter(param);

		return tApplHistoryData;
	}

	/**
	 * 添付ファイルを取得し、セッションに保存します
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
		// 初期化
		resultAttachedFileList = new ArrayList<Map<String, Object>>();

		// 添付ファイル管理テーブルから添付ファイル情報を取得
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
				// 添付資料番号
				attachedFileMap.put("attachedNo", String.valueOf(attachedNo));
				// 添付資料名
				attachedFileMap.put("attachedName", attachedFileInfo.getAttachedName());
				// ファイルサイズ
				attachedFileMap.put("fileSize", attachedFileInfo.getFileSize());
				// 更新日
				String applDate = skfDateFormatUtils.dateFormatFromDate(attachedFileInfo.getApplDate(),
						"yyyy/MM/dd HH:mm:ss.SS");
				attachedFileMap.put("applDate", applDate);
				// 添付資料
				attachedFileMap.put("fileStream", attachedFileInfo.getFileStream());
				// ファイルタイプ
				String fileType = skfAttachedFileUtils.getFileTypeInfo(attachedFileInfo.getAttachedName());
				attachedFileMap.put("fileType", fileType);

				resultAttachedFileList.add(attachedFileMap);

				attachedNo++;
			}

			// 取得した添付ファイル情報をセッションに保持
			menuScopeSessionBean.put(sessionKey, resultAttachedFileList);
		}
		return resultAttachedFileList;
	}

	public String insertApplHistoryBihinKibo(String applNo, String applId, String shainNo,
			Map<String, String> errorMsg) {
		String applNoBihinShinsei = CodeConstant.NONE;
		// 保存日時を取得
		Date saveDate = new Date();

		if (applId.equals(FunctionIdConstant.R0100)) {
			// 案内内容が「社宅入居希望等調書」の場合
			// 備品希望申請から申請書類管理番号を取得
			List<Skf2010Sc004GetBihinShinseiInfoExp> bihinShinseiInfoList = new ArrayList<Skf2010Sc004GetBihinShinseiInfoExp>();
			Skf2010Sc004GetBihinShinseiInfoExpParameter bihinParam = new Skf2010Sc004GetBihinShinseiInfoExpParameter();
			bihinParam.setCompanyCd(companyCd);
			bihinParam.setNyukyoApplNo(applNo);
			bihinShinseiInfoList = skf2010Sc004GetBihinShinseiInfoExpRepository.getBihinShinseiInfo(bihinParam);
			if (bihinShinseiInfoList == null || bihinShinseiInfoList.size() <= 0) {
				// データが存在しない場合は何もせずに終了
				return applNoBihinShinsei;
			}
			// 存在する場合は申請書類管理番号を取得
			applNoBihinShinsei = bihinShinseiInfoList.get(0).getApplNo();
			Skf2010Sc004GetApplHistoryInfoExp applHistoryInfo = new Skf2010Sc004GetApplHistoryInfoExp();
			Skf2010Sc004GetApplHistoryInfoExpParameter param = new Skf2010Sc004GetApplHistoryInfoExpParameter();
			param.setCompanyCd(companyCd);
			param.setApplNo(applNoBihinShinsei);
			applHistoryInfo = skf2010Sc004GetApplHistoryInfoExpRepository.getApplHistoryInfo(param);
			// 備品希望申請の申請書類履歴の存在チェック
			if (applHistoryInfo != null) {
				// 存在する場合は何もせずに終了
				return applNoBihinShinsei;
			}
			// 「申請書類履歴テーブル」を登録
			Skf2010TApplHistory record = new Skf2010TApplHistory();
			record.setCompanyCd(companyCd);
			record.setShainNo(shainNo);
			record.setApplNo(applNoBihinShinsei);
			record.setApplId(FunctionIdConstant.R0104);
			record.setApplStatus(CodeConstant.STATUS_ICHIJIHOZON);
			// TODO 連携フラグをどうするか？（現行は初期はNOT_RENKEI）
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
	 * 新申請番号を取得する
	 * 
	 * @param shainNo
	 * @param applId
	 * @return
	 */
	public String getApplNo(String shainNo, String applId) {
		String newApplNo = CodeConstant.NONE;
		newApplNo = skfShinseiUtils.getApplNo(companyCd, shainNo, applId);

		return newApplNo;
	}

	/**
	 * 備品一覧を取得する
	 * 
	 * @param applNo
	 * @return
	 */
	public List<SkfBihinInfoUtilsGetBihinInfoExp> getBihinList(String applNo) {
		List<SkfBihinInfoUtilsGetBihinInfoExp> bihinList = new ArrayList<SkfBihinInfoUtilsGetBihinInfoExp>();
		bihinList = skfBihinInfoUtils.getBihinInfo(companyCd, applNo);
		return bihinList;
	}

	/**
	 * 社宅連携処理を実施する
	 * 
	 * @param menuScopeSessionBean
	 * @param applNo
	 * @param applStatus
	 * @param applId
	 * @param pageId
	 * @paran shainNo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> doShatakuRenkei(MenuScopeSessionBean menuScopeSessionBean, String applNo, String newApplNo,
			String applStatus, String applId, String pageId, String shainNo) {
		// ログインユーザー情報取得
		Map<String, String> loginUserInfoMap = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		String userId = loginUserInfoMap.get("userCd");
		// String shainNo = loginUserInfoMap.get("shainNo");
		// 排他チェック用データ取得
		Map<String, Object> forUpdateObject = (Map<String, Object>) menuScopeSessionBean
				.get(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2010SC004);

		List<String> resultBatch = new ArrayList<String>();

		switch (applId) {
		case FunctionIdConstant.R0100:
			Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> forUpdateMapR0100 = skf2020Fc001NyukyoKiboSinseiDataImport
					.forUpdateMapDownCaster(forUpdateObject);
			skf2020Fc001NyukyoKiboSinseiDataImport.setUpdateDateForUpdateSQL(forUpdateMapR0100);

			// 連携処理開始
			resultBatch = skf2020Fc001NyukyoKiboSinseiDataImport.doProc(companyCd, shainNo, applNo, newApplNo,
					applStatus, userId, pageId);
			break;
		case FunctionIdConstant.R0103:
			Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> forUpdateMapR0103 = skf2040Fc001TaikyoTodokeDataImport
					.forUpdateMapDownCaster(forUpdateObject);
			skf2040Fc001TaikyoTodokeDataImport.setUpdateDateForUpdateSQL(forUpdateMapR0103);

			// 連携処理開始
			resultBatch = skf2040Fc001TaikyoTodokeDataImport.doProc(companyCd, shainNo, applNo, applStatus, userId,
					pageId);
			break;
		default:
			break;
		}

		return resultBatch;
	}

	/**
	 * 社宅貸与不要判定
	 * 
	 * @param applNo
	 * @return
	 */
	public boolean isShatakuTaiyoFuyo(String applNo) {
		boolean result = false;

		Skf2020TNyukyoChoshoTsuchi dtNyukyo = new Skf2020TNyukyoChoshoTsuchi();
		Skf2020TNyukyoChoshoTsuchiKey key = new Skf2020TNyukyoChoshoTsuchiKey();
		key.setCompanyCd(companyCd);
		key.setApplNo(applNo);
		dtNyukyo = skf2020TNyukyoChoshoTsuchiRepository.selectByPrimaryKey(key);
		if (dtNyukyo != null) {
			if (NfwStringUtils.isNotEmpty(dtNyukyo.getTaiyoHitsuyo())
					&& CheckUtils.isEqual(dtNyukyo.getTaiyoHitsuyo(), CodeConstant.ASKED_SHATAKU_FUYOU)) {
				result = true;
			}
		}

		return result;
	}

}
