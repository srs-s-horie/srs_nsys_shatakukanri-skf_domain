package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc006;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc006.Skf2010Sc006GetAgreeAuthorityGroupIdExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc006.Skf2010Sc006GetAgreeAuthorityGroupIdExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc006.Skf2010Sc006GetApplHistoryInfoByParameterExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc006.Skf2010Sc006GetApplHistoryInfoByParameterExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc006.Skf2010Sc006GetAttachedFileInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc006.Skf2010Sc006GetAttachedFileInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc006.Skf2010Sc006GetBHSApplStatusInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc006.Skf2010Sc006GetBHSApplStatusInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc006.Skf2010Sc006GetBKSApplStatusInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc006.Skf2010Sc006GetBKSApplStatusInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc006.Skf2010Sc006GetSendApplMailInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc006.Skf2010Sc006GetSendApplMailInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc006.Skf2010Sc006GetTeijiDataInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc006.Skf2010Sc006GetTeijiDataInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc006.Skf2010Sc006UpdateNyukyoChoshoTsuchiRentalExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShain;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShainKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010MApplication;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010MApplicationKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplHistory;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TAttachedFile;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchi;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchiKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc006.Skf2010Sc006GetAgreeAuthorityGroupIdExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc006.Skf2010Sc006GetApplHistoryInfoByParameterExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc006.Skf2010Sc006GetAttachedFileInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc006.Skf2010Sc006GetBHSApplStatusInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc006.Skf2010Sc006GetBKSApplStatusInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc006.Skf2010Sc006GetCommentListExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc006.Skf2010Sc006GetSendApplMailInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc006.Skf2010Sc006GetTeijiDataInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc006.Skf2010Sc006UpdateNyukyoChoshoTsuchiRentalExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MShainRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010MApplicationRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TApplCommentRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TApplHistoryRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TAttachedFileRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2020TNyukyoChoshoTsuchiRepository;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LoginUserInfoUtils;
import jp.co.c_nexco.nfw.common.utils.NfwSendMailUtils;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtils;
import jp.co.c_nexco.skf.common.util.SkfCommentUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;

@Service
public class Skf2010Sc006SharedService {

	private String companyCd = CodeConstant.C001;

	@Autowired
	private Skf2020TNyukyoChoshoTsuchiRepository skf2020TNyukyoChoshoTsuchiRepository;

	@Autowired
	private Skf2010Sc006GetApplHistoryInfoByParameterExpRepository skf2010Sc006GetApplHistoryInfoByParameterExpRepository;
	@Autowired
	private Skf2010Sc006GetAgreeAuthorityGroupIdExpRepository skf2010Sc006GetAgreeAuthorityGroupIdExpRepository;
	@Autowired
	private Skf2010TApplHistoryRepository skf2010TApplHistoryRepository;
	@Autowired
	private Skf2010Sc006GetTeijiDataInfoExpRepository skf2010Sc006GetTeijiDataInfoExpRepository;
	@Autowired
	private Skf2010Sc006GetCommentListExpRepository skf2010Sc006GetCommentListExpRepository;
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
	private Skf2010TApplCommentRepository skf2010TApplCommentRepository;
	@Autowired
	private Skf2010MApplicationRepository skf2010MApplicationRepository;
	@Autowired
	private Skf1010MShainRepository skf1010MShainRepository;
	@Autowired
	private Skf2010TAttachedFileRepository skf2010TAttachedFileRepository;

	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private SkfAttachedFileUtils skfAttachedFileUtils;
	@Autowired
	private SkfCommentUtils skfCommentUtils;

	@Autowired
	private MenuScopeSessionBean menuScopeSessionBean;

	@Value("${skf.common.attached_file_session_key}")
	private String sessionKey;

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
			Map<String, String> errMap) throws Exception {
		if ((companyCd == null || CheckUtils.isEmpty(companyCd) || (applNo == null || CheckUtils.isEmpty(applNo)))) {
			return false;
		}
		// 更新対象の申請情報を取得
		Skf2010Sc006GetApplHistoryInfoByParameterExp tApplHistoryData = new Skf2010Sc006GetApplHistoryInfoByParameterExp();
		tApplHistoryData = getApplHistoryInfo(applNo);

		// 次のステータスを設定する
		Skf2010TApplHistory updateData = new Skf2010TApplHistory();

		String shoninName1 = "";
		String shoninName2 = "";
		String nextStatus = "";
		String mailKbn = "";
		String nextWorkflow = "";
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
			nextWorkflow = CodeConstant.LEVEL_1;
			break;
		case CodeConstant.STATUS_SHONIN1:
			// 承認１
			nextStatus = CodeConstant.STATUS_SHONIN_ZUMI;
			shoninName2 = loginUserInfoMap.get("userName");
			mailKbn = CodeConstant.SHONIN_KANRYO_TSUCHI;
			nextWorkflow = CodeConstant.LEVEL_5;
			agreeDate = updateDate;
			break;
		default:
			errMap.put("error", "");
			return false;
		}

		if (!CheckUtils.isEmpty(nextWorkflow)) {
			// 次のワークフロー設定がある場合、次権限グループを取得
			Skf2010Sc006GetAgreeAuthorityGroupIdExpParameter mApplParam = new Skf2010Sc006GetAgreeAuthorityGroupIdExpParameter();
			Skf2010Sc006GetAgreeAuthorityGroupIdExp mApplResult = new Skf2010Sc006GetAgreeAuthorityGroupIdExp();
			mApplParam.setCompanyCd(companyCd);
			mApplParam.setApplId(applNo);
			mApplParam.setWfLevel(nextWorkflow);
			mApplResult = skf2010Sc006GetAgreeAuthorityGroupIdExpRepository.getAgreeAuthorityGroupId(mApplParam);
			if (mApplResult != null) {
				sendGroupId = mApplResult.getRoleId();
			}
		}

		if (!CheckUtils.isEmpty(sendGroupId)) {
			nextStatus = CodeConstant.STATUS_SHONIN_ZUMI;
			shoninName1 = loginUserInfoMap.get("userName");
			mailKbn = CodeConstant.SHONIN_KANRYO_TSUCHI;
			nextWorkflow = CodeConstant.LEVEL_5;
			applInfo.get("applShainNo");
			agreeDate = updateDate;
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

		// コメント登録者名を設定
		List<String> tmpNameList = new ArrayList<String>();
		if (loginUserInfoMap.get("affiliation2Name") != null) {
			tmpNameList.add(loginUserInfoMap.get("affiliation2Name"));
		}
		tmpNameList.add(loginUserInfoMap.get("userName"));
		String commentName = String.join("\r\n", tmpNameList); // ログインユーザーの名前を取得
		// コメントを更新する
		if (comment != null && !CheckUtils.isEmpty(comment)) {
			boolean commentRes = skfCommentUtils.insertComment(companyCd, applNo, nextStatus, commentName, comment,
					errMap);
			if (!commentRes) {
				return false;
			}
		}

		// 添付ファイル管理テーブル更新処理

		// 承認完了通知の場合のみ
		if (mailKbn.equals(CodeConstant.SHONIN_KANRYO_TSUCHI)) {
			// メール送付
			comment = "";
			String annai = "";
			String furikomiStartDate = "";
			String sendUser = applInfo.get("applShainNo");
			sendApplTsuchiMail(mailKbn, applInfo, comment, annai, furikomiStartDate, sendUser, sendGroupId);
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

	public boolean representData(String companyCd, String applNo, String comment, String applUpdateDate,
			Map<String, String> errMap) {
		if ((companyCd == null || CheckUtils.isEmpty(companyCd) || (applNo == null || CheckUtils.isEmpty(applNo)))) {
			return false;
		}
		// 更新対象の申請情報を取得
		Skf2010Sc006GetApplHistoryInfoByParameterExp applInfo = new Skf2010Sc006GetApplHistoryInfoByParameterExp();
		applInfo = getApplHistoryInfo(applNo);
		if (applInfo == null) {
			return false;
		}

		String applStatus = "";

		switch (applInfo.getApplId()) {
		case FunctionIdConstant.R0100:
			// 社宅入居希望等調書の場合
			applStatus = getBihinApplStatus(applNo, applInfo.getApplId());
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
		case FunctionIdConstant.R0103:
			// 社宅入居希望等調書の場合
			applStatus = getBihinApplStatus(applNo, applInfo.getApplId());
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
		}

		new Date();

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
		int applHistoryRes = skf2010TApplHistoryRepository.updateByPrimaryKeySelective(updateData);
		if (applHistoryRes <= 0) {

			return false;
		}

		return true;
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
	public List<SkfCommentUtilsGetCommentInfoExp> getApplCommentList(String applNo, String applStatus)
			throws IllegalAccessException, InvocationTargetException {
		List<SkfCommentUtilsGetCommentInfoExp> returnList = new ArrayList<SkfCommentUtilsGetCommentInfoExp>();
		returnList = skfCommentUtils.getCommentInfo(companyCd, applNo, applStatus);

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
			if (tTaijiData.getRentalAdjust() != null) {
				Skf2010Sc006UpdateNyukyoChoshoTsuchiRentalExp updateData = new Skf2010Sc006UpdateNyukyoChoshoTsuchiRentalExp();
				updateData.setCompanyCd(companyCd);
				updateData.setApplNo(applNo);
				if (tTaijiData.getRentalAdjust() != null) {
					updateData.setNewKyoekihi(tTaijiData.getRentalAdjust().toString());
				}
				updateData.setKyoekihiPersonKyogichuFlg(tTaijiData.getKyoekihiPersonKyogichuFlg());
				if (tTaijiData.getKyoekihiPersonAdjust() != null) {
					updateData.setNewKyoekihi(tTaijiData.getKyoekihiPersonAdjust().toString());
				}
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
	 * 申請通知メール送信のメイン処理を行います
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

		// 申請書類名を取得
		Skf2010MApplication skf2010MApplication = new Skf2010MApplication();
		Skf2010MApplicationKey key = new Skf2010MApplicationKey();
		key.setCompanyCd(companyCd);
		key.setApplId(applInfo.get("applId"));
		skf2010MApplication = skf2010MApplicationRepository.selectByPrimaryKey(key);
		if (skf2010MApplication == null) {
			// 申請書類名が取れない時はエラーログを出力
			return;
		}
		String applName = skf2010MApplication.getApplName();

		// 申請社員名、申請日を取得
		String applShainName = "";
		String applDate = "";
		String applAgencyCd = "";

		// 申請通知メール情報の取得
		Skf2010Sc006GetSendApplMailInfoExp skfMShainData = new Skf2010Sc006GetSendApplMailInfoExp();
		List<Skf2010Sc006GetSendApplMailInfoExp> skfMShainList = new ArrayList<Skf2010Sc006GetSendApplMailInfoExp>();
		Skf2010Sc006GetSendApplMailInfoExpParameter mShainParam = new Skf2010Sc006GetSendApplMailInfoExpParameter();
		mShainParam.setCompanyCd(companyCd);
		mShainParam.setShainNo(applInfo.get("applShainNo"));
		mShainParam.setApplNo(applInfo.get("applNo"));
		skfMShainList = skf2010Sc006GetSendApplMailInfoExpRepository.getSendApplMailInfo(mShainParam);
		if (skfMShainList == null || skfMShainList.size() <= 0) {
			// 申請通知メール情報が取れない時はエラーログを出力
			return;
		}
		skfMShainData = skfMShainList.get(0);

		if (!CheckUtils.isEmpty(skfMShainData.getName())) {
			applShainName = skfMShainData.getName();
		}
		if (skfMShainData.getApplDate() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			applDate = sdf.format(skfMShainData.getApplDate());
		}
		if (!CheckUtils.isEmpty(skfMShainData.getAgencyCd())) {
			applAgencyCd = skfMShainData.getAgencyCd();
		}

		// アウトソース担当者共通のメールアドレスを取得
		String outMailAddress = getApplOutMailInfo(applAgencyCd, applInfo.get("applId"));

		// メール送信
		if (!CheckUtils.isEmpty(sendUser)) {
			String mailAddress = getSendMailAddressByShainNo(companyCd, sendUser);

			// URLを設定
			String urlBase = "/skf/Skf2010Sc005/init?SKF2010_SC005&menuflg=1&tokenCheck=0";
			sendApplTsuchiMailExist(companyCd, sendMailKbn, applInfo.get("applNo"), applDate, applShainName, comment,
					annai, furikomiStartDate, urlBase, sendUser, mailAddress, null, applName);
		} else {
			sendApplTsuchiMailExist(companyCd, sendMailKbn, applInfo.get("applNo"), applDate, applShainName, comment,
					annai, furikomiStartDate, null, sendUser, outMailAddress, null, applName);

		}

		return;
	}

	/**
	 * アウトソース担当者共通にメールを送信します
	 * 
	 * @param agencyCd
	 * @param applId
	 * @return
	 */
	private String getApplOutMailInfo(String agencyCd, String applId) {
		String mailAddress = "";

		// 設定ファイル情報の取得

		if (applId.equals(FunctionIdConstant.R0100)) {
			// 社宅入居希望等調書
		} else if (applId.equals(FunctionIdConstant.R0105)) {
			// 備品返却申請
		}

		return mailAddress;
	}

	/**
	 * 通知メール用の社員情報を取得します
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
	 * 認識通知メールを送信します
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

		// メール送信テスト
		Map<String, String> replaceMap = new HashMap<String, String>();

		// メール件名
		replaceMap.put("【to】", mailAddress); // 送信先メールアドレス
		replaceMap.put("【applno】", applNo); // 申請書類番号
		replaceMap.put("【shinseishorui】", applName); // 申請書類名

		// メール本文
		replaceMap.put("【shainname】", applShainName); // 申請社員名
		replaceMap.put("【appldate】", applDate); // 申請日
		replaceMap.put("【reason】", comment); // 承認者からのコメント

		// 短縮URL作成
		if (urlBase != null) {
			Map<String, String> urlMap = new HashMap<String, String>();
			String url = NfwSendMailUtils.createShotcutUrl(urlBase, urlMap, 2);
			replaceMap.put("【url】", url);
		}

		// メール送信
		NfwSendMailUtils.sendMail("SKF_ML06", replaceMap);
		return;
	}

}
