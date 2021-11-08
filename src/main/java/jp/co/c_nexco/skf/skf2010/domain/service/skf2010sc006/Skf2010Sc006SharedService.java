package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc006;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc006.Skf2010Sc006GetApplHistoryInfoByParameterExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc006.Skf2010Sc006GetApplHistoryInfoByParameterExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc006.Skf2010Sc006GetAttachedFileInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc006.Skf2010Sc006GetAttachedFileInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc006.Skf2010Sc006GetBHSApplStatusInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc006.Skf2010Sc006GetBHSApplStatusInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc006.Skf2010Sc006GetBKSApplStatusInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc006.Skf2010Sc006GetBKSApplStatusInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc006.Skf2010Sc006GetTeijiDataInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc006.Skf2010Sc006GetTeijiDataInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc006.Skf2010Sc006UpdateNyukyoChoshoTsuchiRentalExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfApplHistoryInfoUtils.SkfApplHistoryInfoUtilsGetApplHistoryInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfApplHistoryInfoUtils.SkfApplHistoryInfoUtilsGetApplHistoryInfoForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfAttachedFileUtils.SkfAttachedFileUtilsInsertAttachedFileExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBatchUtils.SkfBatchUtilsGetMultipleTablesUpdateDateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentListExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplHistory;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TAttachedFile;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchi;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchiKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReport;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReportKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc006.Skf2010Sc006GetApplHistoryInfoByParameterExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc006.Skf2010Sc006GetAttachedFileInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc006.Skf2010Sc006GetBHSApplStatusInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc006.Skf2010Sc006GetBKSApplStatusInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc006.Skf2010Sc006GetSendApplMailInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc006.Skf2010Sc006GetTeijiDataInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc006.Skf2010Sc006UpdateNyukyoChoshoTsuchiRentalExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MShainRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010MApplicationRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TApplHistoryRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TAttachedFileRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2020TNyukyoChoshoTsuchiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2040TTaikyoReportRepository;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.LoginUserInfoUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.common.utils.PropertyUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfApplHistoryInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtils;
import jp.co.c_nexco.skf.common.util.SkfCommentUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfMailUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2020Fc001NyukyoKiboSinseiDataImport;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2040Fc001TaikyoTodokeDataImport;
import jp.co.c_nexco.skf.skf2010.domain.dto.common.Skf2010CommonDto;

@Service
public class Skf2010Sc006SharedService {

	private String companyCd = CodeConstant.C001;

	@Autowired
	private Skf2020TNyukyoChoshoTsuchiRepository skf2020TNyukyoChoshoTsuchiRepository;

	@Autowired
	private Skf2010Sc006GetApplHistoryInfoByParameterExpRepository skf2010Sc006GetApplHistoryInfoByParameterExpRepository;
	@Autowired
	private Skf2010TApplHistoryRepository skf2010TApplHistoryRepository;
	@Autowired
	private Skf2040TTaikyoReportRepository skf2040TTaikyoReportRepository;
	@Autowired
	private Skf2010Sc006GetTeijiDataInfoExpRepository skf2010Sc006GetTeijiDataInfoExpRepository;
	@Autowired
	private Skf2010Sc006UpdateNyukyoChoshoTsuchiRentalExpRepository skf2010Sc006UpdateNyukyoChoshoTsuchiRentalExpRepository;
	@Autowired
	private Skf2010Sc006GetSendApplMailInfoExpRepository skf2010Sc006GetSendApplMailInfoExpRepository;
	@Autowired
	private Skf2010Sc006GetBKSApplStatusInfoExpRepository skf2010Sc006GetBKSApplStatusInfoExpRepository;
	@Autowired
	private Skf2010Sc006GetBHSApplStatusInfoExpRepository skf2010Sc006GetBHSApplStatusInfoExpRepository;
	@Autowired
	private Skf2010Sc006GetAttachedFileInfoExpRepository skf2010Sc006GetAttachedFileInfoExpRepository;

	@Autowired
	private Skf2010MApplicationRepository skf2010MApplicationRepository;
	@Autowired
	private Skf1010MShainRepository skf1010MShainRepository;
	@Autowired
	private Skf2010TAttachedFileRepository skf2010TAttachedFileRepository;

	@Autowired
	private Skf2020Fc001NyukyoKiboSinseiDataImport skf2020Fc001NyukyoKiboSinseiDataImport;
	@Autowired
	private Skf2040Fc001TaikyoTodokeDataImport skf2040Fc001TaikyoTodokeDataImport;

	@Autowired
	private SkfApplHistoryInfoUtils skfApplHistoryInfoUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private SkfAttachedFileUtils skfAttachedFileUtils;
	@Autowired
	private SkfCommentUtils skfCommentUtils;
	@Autowired
	private SkfMailUtils skfMailUtils;

	@Autowired
	private MenuScopeSessionBean menuScopeSessionBean;

	private String sessionKey = SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY;

	protected final String KEY_LAST_UPDATE_DATE_HISTORY = "skf2010_t_appl_history";

	/**
	 * 社宅入居希望等申請情報を取得する
	 * 
	 * @param companyCd
	 * @param shainNo
	 * @param name
	 * @param nameKk
	 * @param agencyName
	 * @return
	 */
	public Skf2020TNyukyoChoshoTsuchi getNyukyoChoshoTsuchiInfo(String companyCd, String applNo) {
		Skf2020TNyukyoChoshoTsuchi nyukyoChoshoTsuchiInfo = new Skf2020TNyukyoChoshoTsuchi();
		Skf2020TNyukyoChoshoTsuchiKey key = new Skf2020TNyukyoChoshoTsuchiKey();
		key.setCompanyCd(companyCd);
		key.setApplNo(applNo);
		nyukyoChoshoTsuchiInfo = skf2020TNyukyoChoshoTsuchiRepository.selectByPrimaryKey(key);
		return nyukyoChoshoTsuchiInfo;
	}

	/**
	 * 退居届の情報を取得します
	 * 
	 * @param companyCd
	 * @param applNo
	 * @return
	 */
	public Skf2040TTaikyoReport getTaikyoReportInfo(String companyCd, String applNo) {
		Skf2040TTaikyoReport taikyoReportInfo = new Skf2040TTaikyoReport();
		Skf2040TTaikyoReportKey param = new Skf2040TTaikyoReportKey();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		taikyoReportInfo = skf2040TTaikyoReportRepository.selectByPrimaryKey(param);
		return taikyoReportInfo;
	}

	/**
	 * 添付ファイルを取得し、セッションに保存します
	 * 
	 * @param applNo
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getAttachedFileInfo(String applNo, boolean useSessionFlg) {
		List<Map<String, Object>> resultAttachedFileList = null;

		if (useSessionFlg) {
			resultAttachedFileList = (List<Map<String, Object>>) menuScopeSessionBean.get(sessionKey);
			if (resultAttachedFileList != null) {
				return resultAttachedFileList;
			}
		}
		// 初期化
		resultAttachedFileList = new ArrayList<Map<String, Object>>();

		// 添付ファイル管理テーブルから添付ファイル情報を取得
		List<Skf2010Sc006GetAttachedFileInfoExp> attachedFileList = new ArrayList<Skf2010Sc006GetAttachedFileInfoExp>();
		Skf2010Sc006GetAttachedFileInfoExpParameter param = new Skf2010Sc006GetAttachedFileInfoExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		attachedFileList = skf2010Sc006GetAttachedFileInfoExpRepository.getAttachedFileInfo(param);
		if (attachedFileList != null && attachedFileList.size() > 0) {
			int attachedNo = 0;
			Map<String, Object> attachedFileMap = null;
			for (Skf2010Sc006GetAttachedFileInfoExp attachedFileInfo : attachedFileList) {
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

	/**
	 * 申請状況を更新します
	 * 
	 * @param companyCd
	 * @param applNo
	 * @param errMap
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public boolean updateApplStatus(String companyCd, String applNo, String comment, String applTacFlag,
			Date lastUpdateDate, Map<String, String> errMap) throws Exception {
		if ((companyCd == null || CheckUtils.isEmpty(companyCd) || (applNo == null || CheckUtils.isEmpty(applNo)))) {
			return false;
		}
		// 更新対象の申請情報を取得
		Skf2010Sc006GetApplHistoryInfoByParameterExp tApplHistoryData = new Skf2010Sc006GetApplHistoryInfoByParameterExp();
		tApplHistoryData = getApplHistoryInfo(applNo);

		// 排他チェック
		if (!CheckUtils.isEqual(tApplHistoryData.getUpdateDate(), lastUpdateDate)) {
			errMap.put("error", MessageIdConstant.E_SKF_1135);
			return false;
		}

		// 次のステータスを設定する
		Skf2010TApplHistory updateData = new Skf2010TApplHistory();

		String shoninName1 = "";
		String shoninName2 = "";
		String nextStatus = "";
		String mailKbn = "";
		String sendGroupId = "";
		Map<String, String> applInfo = new HashMap<String, String>();
		Date agreeDate = null;

		Date updateDate = new Date();

		// ログインユーザー情報取得
		Map<String, String> loginUserInfoMap = skfLoginUserInfoUtils.getSkfLoginUserInfo();

		// プライマリキー設定
		updateData.setCompanyCd(companyCd);
		updateData.setShainNo(tApplHistoryData.getShainNo());
		updateData.setApplId(tApplHistoryData.getApplId());
		updateData.setApplNo(applNo);
		updateData.setApplDate(tApplHistoryData.getApplDate());

		// 申請情報設定
		applInfo.put("applId", tApplHistoryData.getApplId());
		applInfo.put("applNo", applNo);
		applInfo.put("applStatus", tApplHistoryData.getApplStatus());
		applInfo.put("applShainNo", tApplHistoryData.getShainNo());

		String nowApplStatus = tApplHistoryData.getApplStatus();
		switch (nowApplStatus) {
		case CodeConstant.STATUS_SHINSEICHU:
		case CodeConstant.STATUS_SHINSACHU:
		case CodeConstant.STATUS_DOI_ZUMI:
		case CodeConstant.STATUS_HANNYU_ZUMI:
		case CodeConstant.STATUS_HANSYUTSU_ZUMI:
			// 申請中、審査中、同意済、搬入済、搬出済
			nextStatus = CodeConstant.STATUS_SHONIN1;
			shoninName1 = loginUserInfoMap.get("userName");
			mailKbn = CodeConstant.SHONIN_IRAI_TSUCHI;
			break;
		case CodeConstant.STATUS_SHONIN1:
			// 承認１
			nextStatus = CodeConstant.STATUS_SHONIN_ZUMI;
			shoninName2 = loginUserInfoMap.get("userName");
			mailKbn = CodeConstant.SHONIN_KANRYO_TSUCHI;
			agreeDate = updateDate;
			break;
		default:
			errMap.put("error", "");
			return false;
		}

		// 申請状況
		if (!CheckUtils.isEmpty(nextStatus)) {
			updateData.setApplStatus(nextStatus);
		}
		// 承認者名1
		if (!CheckUtils.isEmpty(shoninName1)) {
			updateData.setAgreName1(shoninName1);
		}
		// 承認者名2
		if (!CheckUtils.isEmpty(shoninName2)) {
			updateData.setAgreName2(shoninName2);
		}
		// 承認日
		if (agreeDate != null) {
			updateData.setAgreDate(updateDate);
		}
		// 添付資料フラグ
		updateData.setApplTacFlg(applTacFlag);

		// 申請情報履歴更新
		int applHistoryRes = skf2010TApplHistoryRepository.updateByPrimaryKeySelective(updateData);
		if (applHistoryRes <= 0) {

			return false;
		}

		// 申請書類IDが"R0100"【入退居希望申請】かつ次のステータスが承認中または承認済の場合、共益費、使用料を更新する
		if (tApplHistoryData.getApplId().equals(FunctionIdConstant.R0100)
				&& (CodeConstant.STATUS_SHONIN1.equals(nextStatus) || CodeConstant.STATUS_SHONIN2.equals(nextStatus)
						|| CodeConstant.STATUS_SHONIN3.equals(nextStatus)
						|| CodeConstant.STATUS_SHONIN4.equals(nextStatus)
						|| CodeConstant.STATUS_SHONIN_ZUMI.equals(nextStatus))) {
			String updateUser = LoginUserInfoUtils.getUserName();

			boolean res = updateShatakuRental(tApplHistoryData.getShainNo(), CodeConstant.NYUTAIKYO_KBN_NYUKYO,
					tApplHistoryData.getApplNo(), updateDate, updateUser);
			if (!res) {
				return false;
			}

		}

		// コメント更新
		String commentNote = comment;
		boolean commentErrorMessage = skfCommentUtils.insertComment(CodeConstant.C001, applInfo.get("applNo"),
				nextStatus, commentNote, errMap);
		if (!commentErrorMessage) {
			return false;
		}

		// 添付ファイル管理テーブル更新処理

		// 承認完了通知の場合のみ
		if (mailKbn.equals(CodeConstant.SHONIN_KANRYO_TSUCHI)) {
			// メール送付
			String annai = CodeConstant.NONE;
			String sendUser = applInfo.get("applShainNo");
			String urlBase = "/skf/Skf2010Sc003/init?SKF2010_SC003&menuflg=1&tokenCheck=0";
			skfMailUtils.sendApplTsuchiMail(mailKbn, applInfo, comment, annai, sendUser, sendGroupId, urlBase);
		}

		return true;
	}

	public Skf2010Sc006GetApplHistoryInfoByParameterExp getApplHistoryInfo(String applNo) {
		Skf2010Sc006GetApplHistoryInfoByParameterExpParameter param = new Skf2010Sc006GetApplHistoryInfoByParameterExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		// 更新対象の申請情報を取得
		Skf2010Sc006GetApplHistoryInfoByParameterExp tApplHistoryData = new Skf2010Sc006GetApplHistoryInfoByParameterExp();
		tApplHistoryData = skf2010Sc006GetApplHistoryInfoByParameterExpRepository.getApplHistoryInfoByParameter(param);

		return tApplHistoryData;
	}

	/**
	 * 再提示処理を行います
	 * 
	 * @param companyCd
	 * @param applNo
	 * @param comment
	 * @param applUpdateDate
	 * @param applTacFlag
	 * @param errMap
	 * @return
	 */
	public boolean representData(String companyCd, String applNo, String comment, String applUpdateDate,
			String applTacFlag, Date lastUpdateDate, Map<String, String> errMap) {
		if ((companyCd == null || CheckUtils.isEmpty(companyCd) || (applNo == null || CheckUtils.isEmpty(applNo)))) {
			return false;
		}
		skfLoginUserInfoUtils.getSkfLoginUserInfo();

		// 更新対象の申請情報を取得
		Skf2010Sc006GetApplHistoryInfoByParameterExp applInfo = new Skf2010Sc006GetApplHistoryInfoByParameterExp();
		applInfo = getApplHistoryInfo(applNo);
		if (applInfo == null) {
			return false;
		}

		// 排他チェック
		if (!CheckUtils.isEqual(applInfo.getUpdateDate(), lastUpdateDate)) {
			errMap.put("error", MessageIdConstant.E_SKF_1135);
			return false;
		}

		String applStatus = "";

		switch (applInfo.getApplId()) {
		case FunctionIdConstant.R0100:
			// 社宅入居希望等調書の場合
			applStatus = getBihinApplStatus(applNo, applInfo.getApplId());
			if (applStatus != null) {
				switch (applStatus) {
				case CodeConstant.STATUS_MISAKUSEI:
				case CodeConstant.STATUS_ICHIJIHOZON:
				case CodeConstant.STATUS_SASHIMODOSHI:
				case CodeConstant.STATUS_HININ:
					break;
				default:
					// 備品希望申請が申請されていた場合、メッセージを表示して処理を中断
					errMap.put("error", MessageIdConstant.SKF2010_SC006_E_SAITEIJI_1);
					return false;
				}
				break;
			}
		case FunctionIdConstant.R0103:
			// 社宅入居希望等調書の場合
			applStatus = getBihinApplStatus(applNo, applInfo.getApplId());
			if (applStatus != null) {
				switch (applStatus) {
				case CodeConstant.STATUS_MISAKUSEI:
				case CodeConstant.STATUS_ICHIJIHOZON:
				case CodeConstant.STATUS_SHINSACHU:
				case CodeConstant.STATUS_KAKUNIN_IRAI:
				case CodeConstant.STATUS_DOI_SHINAI:
					break;
				default:
					// 備品希望申請が申請されていた場合、メッセージを表示して処理を中断
					errMap.put("error", MessageIdConstant.SKF2010_SC006_E_SAITEIJI_2);
					return false;
				}
				break;
			}
		}

		// 「申請書類履歴テーブル」よりステータスを更新
		// 次のステータスを設定する
		Skf2010TApplHistory updateData = new Skf2010TApplHistory();
		// プライマリキー設定
		updateData.setCompanyCd(companyCd);
		updateData.setShainNo(applInfo.getShainNo());
		updateData.setApplId(applInfo.getApplId());
		updateData.setApplNo(applNo);
		updateData.setApplDate(applInfo.getApplDate());

		// 申請状況
		updateData.setApplStatus(CodeConstant.STATUS_SHINSACHU);
		updateData.setApplTacFlg(applTacFlag);
		int applHistoryRes = skf2010TApplHistoryRepository.updateByPrimaryKeySelective(updateData);
		if (applHistoryRes <= 0) {

			return false;
		}

		return true;
	}

	/**
	 * 申請書類履歴保存の処理
	 * 
	 * @param newStatus
	 * @param dto
	 * @param errorMsg
	 * @return
	 */
	public boolean saveApplInfo(String newStatus, String applTacFlag, Skf2010CommonDto dto, Date lastUpdateDate,
			Map<String, String> errorMsg) {
		String shainNo = dto.getShainNo();
		String applNo = dto.getApplNo();
		String applId = dto.getApplId();

		Date agreDate = null;

		// 申請情報を取得する
		SkfApplHistoryInfoUtilsGetApplHistoryInfoForUpdateExp applInfo = new SkfApplHistoryInfoUtilsGetApplHistoryInfoForUpdateExp();
		SkfApplHistoryInfoUtilsGetApplHistoryInfoForUpdateExpParameter param = new SkfApplHistoryInfoUtilsGetApplHistoryInfoForUpdateExpParameter();
		param.setCompanyCd(companyCd);
		param.setShainNo(shainNo);
		param.setApplNo(applNo);
		applInfo = skfApplHistoryInfoUtils.getApplHistoryInfoForUpdate(companyCd, shainNo, applNo, applId);
		if (applInfo == null) {
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
			LogUtils.infoByMsg("申請書類承認／修正依頼／通知 修正依頼共通処理： 申請情報を取得失敗  社員番号：" + dto.getShainNo() + " 申請書番号：" + dto.getApplNo());
			return false;
		}

		// 排他チェック
		if (!CheckUtils.isEqual(applInfo.getUpdateDate(), lastUpdateDate)) {
			errorMsg.put("error", MessageIdConstant.E_SKF_1135);
			return false;
		}

		Map<String, String> userInfo = new HashMap<String, String>();
		userInfo = skfLoginUserInfoUtils.getSkfLoginUserInfo();

		dto.setApplId(applInfo.getApplId());

		switch (newStatus) {
		case CodeConstant.STATUS_SASHIMODOSHI:
		case CodeConstant.STATUS_HININ:
			// 承認者設定処理
			String shonin1 = null;
			String shonin2 = null;
			agreDate = new Date();
			switch (applInfo.getApplStatus()) {
			case CodeConstant.STATUS_SHINSACHU:
			case CodeConstant.STATUS_DOI_ZUMI:
				shonin1 = userInfo.get("userName");
				break;
			case CodeConstant.STATUS_SHONIN1:
				shonin1 = CodeConstant.NONE;
				shonin2 = userInfo.get("userName");
				break;
			}

			boolean resultUpdateApplInfo = skfApplHistoryInfoUtils.updateApplHistoryAgreeStatus(companyCd, shainNo,
					applNo, applInfo.getApplId(), null, applTacFlag, newStatus, agreDate, shonin1, shonin2, null,
					errorMsg);
			if (!resultUpdateApplInfo) {
				ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
				LogUtils.infoByMsg("申請書類承認／修正依頼／通知 修正依頼共通処理： 申請情報更新失敗  社員番号：" + dto.getShainNo() + " 申請書番号：" + dto.getApplNo());
				return false;
			}

			break;
		}

		// 修正依頼、差戻し時はコメントテーブルを更新
		if (newStatus.equals(CodeConstant.STATUS_SASHIMODOSHI) || newStatus.equals(CodeConstant.STATUS_HININ)) {
			String commentNote = dto.getCommentNote();
			boolean commentErrorMessage = skfCommentUtils.insertComment(CodeConstant.C001, applNo, newStatus,
					commentNote, errorMsg);
			if (!commentErrorMessage) {
				return false;
			}
		}

		return true;
	}

	public boolean checkReason(Skf2010CommonDto dto) throws Exception {
		String comment = dto.getCommentNote();

		// コメントの有無チェック
		if (NfwStringUtils.isEmpty(comment)) {
			ServiceHelper.addErrorResultMessage(dto, new String[] { "commentNote" }, MessageIdConstant.E_SKF_1048,
					"申請者へのコメント");
			return false;
		}
		// コメントの入力上限チェック
		int commentMaxLength = Integer.parseInt(PropertyUtils.getValue("skf.common.comment_max_length"));
		if (commentMaxLength < comment.getBytes("UTF-8").length) {
			ServiceHelper.addErrorResultMessage(dto, new String[] { "commentNote" }, MessageIdConstant.E_SKF_1049,
					"申請者へのコメント", String.valueOf(commentMaxLength));
			return false;
		}

		return true;
	}

	/**
	 * 申請コメント情報を取得します
	 * 
	 * @param applNo
	 * @param applStatus
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public List<SkfCommentUtilsGetCommentInfoExp> getApplCommentInfo(String applNo, String applStatus)
			throws IllegalAccessException, InvocationTargetException {
		List<SkfCommentUtilsGetCommentInfoExp> returnList = new ArrayList<SkfCommentUtilsGetCommentInfoExp>();
		returnList = skfCommentUtils.getCommentInfo(companyCd, applNo, applStatus);

		return returnList;
	}

	/**
	 * 申請コメントの一覧を取得します
	 * 
	 * @param applNo
	 * @param applStatus
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public List<SkfCommentUtilsGetCommentListExp> getApplCommentList(String applNo, String applStatus)
			throws IllegalAccessException, InvocationTargetException {
		List<SkfCommentUtilsGetCommentListExp> returnList = new ArrayList<SkfCommentUtilsGetCommentListExp>();
		returnList = skfCommentUtils.getCommentList(companyCd, applNo, applStatus);

		return returnList;
	}

	/**
	 * 添付ファイル情報を登録する
	 * 
	 * @param applNo
	 * @param shainNo
	 * @param attachedFileList
	 * @return
	 */
	@Transactional
	public boolean updateAttachedFileInfo(String applNo, String shainNo, List<Map<String, Object>> attachedFileList) {
		// 添付ファイル管理テーブルを更新する
		Map<String, String> errorMsg = new HashMap<String, String>();
		if (attachedFileList != null && attachedFileList.size() > 0) {
			// 一度全削除してデータを入れ直す
			boolean delRes = skfAttachedFileUtils.deleteAttachedFile(applNo, shainNo, errorMsg);
			if (!delRes) {
				return false;
			}
			for (Map<String, Object> attachedFileMap : attachedFileList) {
				SkfAttachedFileUtilsInsertAttachedFileExp insertData = new SkfAttachedFileUtilsInsertAttachedFileExp();
				insertData = mappingTAttachedFile(attachedFileMap, applNo, shainNo);
				boolean insRes = skfAttachedFileUtils.insertAttachedFile(insertData,errorMsg);
				if (!insRes) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 
	 * 添付ファイル情報をパラメータに設定する
	 * 
	 * @param attachedFileMap
	 * @param applNo
	 * @param shainNo
	 * @return
	 */
	private SkfAttachedFileUtilsInsertAttachedFileExp mappingTAttachedFile(Map<String, Object> attachedFileMap, String applNo,
			String shainNo) {
		SkfAttachedFileUtilsInsertAttachedFileExp resultData = new SkfAttachedFileUtilsInsertAttachedFileExp();

		// 会社コード
		resultData.setCompanyCd(companyCd);
		// 社員番号
		resultData.setShainNo(shainNo);
		// 登録日時
		Date nowDate = new Date();
		resultData.setApplDate(nowDate);
		// 申請番号
		resultData.setApplNo(applNo);
		// 添付番号
		resultData.setAttachedNo(attachedFileMap.get("attachedNo").toString());
		// 添付資料名
		resultData.setAttachedName(attachedFileMap.get("attachedName").toString());
		// ファイル
		resultData.setFileStream((byte[]) attachedFileMap.get("fileStream"));
		// ファイルサイズ
		resultData.setFileSize(attachedFileMap.get("fileSize").toString());
		//　登録者
		String userCd = LoginUserInfoUtils.getUserCd();
		resultData.setUserId(userCd);
		// 登録機能
		resultData.setProgramId(FunctionIdConstant.SKF2010_SC006);
		
		return resultData;
	}

	/**
	 * 備品希望申請の申請状況を取得します
	 * 
	 * @param applNo
	 * @param applId
	 * @return
	 */
	private String getBihinApplStatus(String applNo, String applId) {
		String result = "";

		switch (applId) {
		case FunctionIdConstant.R0100:
			List<Skf2010Sc006GetBKSApplStatusInfoExp> dataBks = new ArrayList<Skf2010Sc006GetBKSApplStatusInfoExp>();
			Skf2010Sc006GetBKSApplStatusInfoExpParameter paramBks = new Skf2010Sc006GetBKSApplStatusInfoExpParameter();
			paramBks.setCompanyCd(companyCd);
			paramBks.setApplNo(applNo);
			dataBks = skf2010Sc006GetBKSApplStatusInfoExpRepository.getBKSApplStatusInfo(paramBks);
			if (dataBks != null && dataBks.size() > 0) {
				result = dataBks.get(0).getApplStatus();
			}
			break;
		case FunctionIdConstant.R0103:
			// 決定通知書の場合
			List<Skf2010Sc006GetBHSApplStatusInfoExp> dataBhs = new ArrayList<Skf2010Sc006GetBHSApplStatusInfoExp>();
			Skf2010Sc006GetBHSApplStatusInfoExpParameter paramBhs = new Skf2010Sc006GetBHSApplStatusInfoExpParameter();
			paramBhs.setCompanyCd(companyCd);
			paramBhs.setApplNo(applNo);
			dataBhs = skf2010Sc006GetBHSApplStatusInfoExpRepository.getBHSApplStatusInfo(paramBhs);
			if (dataBhs != null && dataBhs.size() > 0) {
				result = dataBhs.get(0).getApplStatus();
			}

		}

		return result;
	}

	/**
	 * 社宅入居調書通知テーブル情報を更新します
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
		List<Skf2010Sc006GetTeijiDataInfoExp> tTaijiDataList = new ArrayList<Skf2010Sc006GetTeijiDataInfoExp>();
		Skf2010Sc006GetTeijiDataInfoExpParameter param = new Skf2010Sc006GetTeijiDataInfoExpParameter();
		param.setShainNo(shainNo);
		param.setNyutaikyoKbn(nyutaikyoKbn);
		param.setApplNo(applNo);
		tTaijiDataList = skf2010Sc006GetTeijiDataInfoExpRepository.getTeijiDataInfo(param);
		if (tTaijiDataList != null && tTaijiDataList.size() > 0) {
			Skf2010Sc006GetTeijiDataInfoExp tTaijiData = tTaijiDataList.get(0);
			if (tTaijiData.getRentalAdjust() != CodeConstant.LONG_ZERO) {
				Skf2010Sc006UpdateNyukyoChoshoTsuchiRentalExp updateData = new Skf2010Sc006UpdateNyukyoChoshoTsuchiRentalExp();
				updateData.setCompanyCd(companyCd);
				updateData.setApplNo(applNo);
				updateData.setRentalAdjust(String.valueOf(tTaijiData.getRentalAdjust()));
				updateData.setKyoekihiPersonKyogichuFlg(tTaijiData.getKyoekihiPersonKyogichuFlg());
				updateData.setNewKyoekihi(String.valueOf(tTaijiData.getKyoekihiPersonAdjust()));
				updateData.setParkingRental(tTaijiData.getParkingRental1());
				updateData.setParkingRental2(tTaijiData.getParkingRental2());
				int res = skf2010Sc006UpdateNyukyoChoshoTsuchiRentalExpRepository
						.updateNyukyoChoshoTsuchiRental(updateData);
				if (res <= 0) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 社宅連携処理を実施する
	 * 
	 * @param menuScopeSessionBean
	 * @param applNo
	 * @param applStatus
	 * @param applId
	 * @param pageId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> doShatakuRenkei(MenuScopeSessionBean menuScopeSessionBean, String shainNo, String applNo,
			String applStatus, String applId, String pageId) {
		// ログインユーザー情報取得
		Map<String, String> loginUserInfoMap = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		String userId = loginUserInfoMap.get("userCd");
		// 排他チェック用データ取得
		Map<String, Object> forUpdateObject = (Map<String, Object>) menuScopeSessionBean
				.get(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2010SC006);

		List<String> resultBatch = new ArrayList<String>();

		switch (applId) {
		case FunctionIdConstant.R0100:
			Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> forUpdateMapR0100 = skf2020Fc001NyukyoKiboSinseiDataImport
					.forUpdateMapDownCaster(forUpdateObject);
			skf2020Fc001NyukyoKiboSinseiDataImport.setUpdateDateForUpdateSQL(forUpdateMapR0100);

			// 連携処理開始
			resultBatch = skf2020Fc001NyukyoKiboSinseiDataImport.doProc(companyCd, shainNo, applNo, CodeConstant.NONE,
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

}
