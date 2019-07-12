package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc005;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetAffiliation1ListExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetAffiliation1ListExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetAffiliation2ListExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetAffiliation2ListExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetAgencyNameExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetAgencyNameExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetAgreeAuthorityCountExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetAgreeAuthorityCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetAgreeAuthorityGroupIdExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetAgreeAuthorityGroupIdExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetApplHistoryInfoByParameterExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetApplHistoryInfoByParameterExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetCommentInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetCommentInfoForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetSendApplMailInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetSendApplMailInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetShoninIchiranShoninExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetShoninIchiranShoninExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetTeijiDataInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetTeijiDataInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005UpdateCommentInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005UpdateNyukyoChoshoTsuchiRentalExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShain;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShainKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010MApplication;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010MApplicationKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplCommentKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplHistory;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchi;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchiKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2030TBihinKiboShinsei;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2030TBihinKiboShinseiKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReport;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReportKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2050TBihinHenkyakuShinsei;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2050TBihinHenkyakuShinseiKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc005.Skf2010Sc005GetAffiliation1ListExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc005.Skf2010Sc005GetAffiliation2ListExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc005.Skf2010Sc005GetAgencyNameExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc005.Skf2010Sc005GetAgreeAuthorityCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc005.Skf2010Sc005GetAgreeAuthorityGroupIdExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc005.Skf2010Sc005GetApplHistoryInfoByParameterExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc005.Skf2010Sc005GetCommentInfoForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc005.Skf2010Sc005GetSendApplMailInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc005.Skf2010Sc005GetShoninIchiranShoninExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc005.Skf2010Sc005GetTeijiDataInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc005.Skf2010Sc005UpdateCommentInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc005.Skf2010Sc005UpdateNyukyoChoshoTsuchiRentalExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MOperationGuideRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MShainRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010MApplicationRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TApplCommentRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TApplHistoryRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2020TNyukyoChoshoTsuchiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2030TBihinKiboShinseiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2040TTaikyoReportRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2050TBihinHenkyakuShinseiRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LoginUserInfoUtils;
import jp.co.c_nexco.nfw.common.utils.NfwSendMailUtils;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;

/**
 * Skf2010Sc005 承認一覧内部処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc005SharedService {

	@Autowired
	private Skf2010Sc005GetApplHistoryInfoByParameterExpRepository skf2010Sc005GetApplHistoryInfoByParameterExpRepository;
	@Autowired
	private Skf2010Sc005GetShoninIchiranShoninExpRepository skf2010Sc005GetShoninIchiranShoninExpRepository;
	@Autowired
	private Skf2010TApplHistoryRepository skf2010TApplHistoryRepository;
	@Autowired
	private Skf1010MShainRepository skf1010MShainRepository;
	@Autowired
	private Skf2010Sc005GetSendApplMailInfoExpRepository skf2010Sc005GetSendApplMailInfoExpRepository;
	@Autowired
	private Skf2010Sc005GetAgencyNameExpRepository skf2010Sc005GetAgencyNameExpRepository;
	@Autowired
	private Skf2010Sc005GetAffiliation1ListExpRepository skf2010Sc005GetAffiliation1ListExpRepository;
	@Autowired
	private Skf2010Sc005GetAffiliation2ListExpRepository skf2010Sc005GetAffiliation2ListExpRepository;
	@Autowired
	private Skf1010MOperationGuideRepository skf1010MOperationGuideRepository;
	@Autowired
	private Skf2010Sc005GetTeijiDataInfoExpRepository skf2010Sc005GetTeijiDataInfoExpRepository;
	@Autowired
	private Skf2010Sc005GetAgreeAuthorityCountExpRepository skf2010Sc005GetAgreeAuthorityCountExpRepository;
	@Autowired
	private Skf2010Sc005GetAgreeAuthorityGroupIdExpRepository skf2010Sc005GetAgreeAuthorityGroupIdExpRepository;
	@Autowired
	private Skf2010MApplicationRepository skf2010MApplicationRepository;
	@Autowired
	private Skf2010Sc005GetCommentInfoForUpdateExpRepository skf2010Sc005GetCommentInfoForUpdateExpRepository;
	@Autowired
	private Skf2010Sc005UpdateCommentInfoExpRepository skf2010Sc005UpdateCommentInfoExpRepository;
	@Autowired
	private Skf2010TApplCommentRepository skf2010TApplCommentRepository;
	@Autowired
	private Skf2010Sc005UpdateNyukyoChoshoTsuchiRentalExpRepository skf2010Sc005UpdateNyukyoChoshoTsuchiRentalExpRepository;

	@Autowired
	private Skf2020TNyukyoChoshoTsuchiRepository skf2020TNyukyoChoshoTsuchiRepository;
	@Autowired
	private Skf2040TTaikyoReportRepository skf2040TTaikyoReportRepository;
	@Autowired
	private Skf2030TBihinKiboShinseiRepository skf2030TBihinKiboShinseiRepository;
	@Autowired
	private Skf2050TBihinHenkyakuShinseiRepository skf2050TBihinHenkyakuShinseiRepository;
	@Autowired
	private SkfDropDownUtils skfDropDownUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;

	private String companyCd = CodeConstant.C001;

	/**
	 * ドロップダウンリスト作成
	 * 
	 * @param dropDownMap
	 * @param companyCd
	 * @param agencyCd
	 * @param affiliation1Cd
	 * @param affiliation2Cd
	 */
	public void setDropDown(Map<String, Object> dropDownMap, String companyCd, String agencyCd, String affiliation1Cd,
			String affiliation2Cd) {
		List<Map<String, Object>> ddlAgencyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> ddlAffiliation1List = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> ddlAffiliation2List = new ArrayList<Map<String, Object>>();

		ddlAgencyList = skfDropDownUtils.getDdlAgencyByCd(companyCd, agencyCd, true);
		// 部等ドロップダウンをセット
		ddlAffiliation1List = skfDropDownUtils.getDdlAffiliation1ByCd(companyCd, agencyCd, affiliation1Cd, true);

		// 室、チーム又は課ドロップダウンをセット
		ddlAffiliation2List = skfDropDownUtils.getDdlAffiliation2ByCd(companyCd, agencyCd, affiliation1Cd,
				affiliation2Cd, true);

		dropDownMap.put("Agency", ddlAgencyList);
		dropDownMap.put("Affiliation1", ddlAffiliation1List);
		dropDownMap.put("Affiliation2", ddlAffiliation2List);
	}

	/**
	 * 申請情報履歴テーブルからデータを取得します
	 * 
	 * @param param
	 * @return
	 */
	public List<Skf2010Sc005GetShoninIchiranShoninExp> SearchApplList(
			Skf2010Sc005GetShoninIchiranShoninExpParameter param) {
		List<Skf2010Sc005GetShoninIchiranShoninExp> tApplHistory = new ArrayList<Skf2010Sc005GetShoninIchiranShoninExp>();
		tApplHistory = skf2010Sc005GetShoninIchiranShoninExpRepository.getShoninIchiranShonin(param);

		return tApplHistory;
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
	public boolean updateApplStatus(String companyCd, String applNo, Map<String, String> errMap) throws Exception {
		if ((companyCd == null || CheckUtils.isEmpty(companyCd) || (applNo == null || CheckUtils.isEmpty(applNo)))) {
			return false;
		}
		Skf2010Sc005GetApplHistoryInfoByParameterExpParameter param = new Skf2010Sc005GetApplHistoryInfoByParameterExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		// 更新対象の申請情報を取得
		Skf2010Sc005GetApplHistoryInfoByParameterExp tApplHistoryData = new Skf2010Sc005GetApplHistoryInfoByParameterExp();
		tApplHistoryData = skf2010Sc005GetApplHistoryInfoByParameterExpRepository.getApplHistoryInfoByParameter(param);

		// 次のステータスを設定する
		Skf2010TApplHistory updateData = new Skf2010TApplHistory();

		String shoninName1 = "";
		String shoninName2 = "";
		String nextStatus = "";
		String mailKbn = "";
		String nextWorkflow = "";
		String sendUserId = "";
		String sendGroupId = "";
		Map<String, String> applInfo = new HashMap<String, String>();
		Date agreeDate = null;

		Date updateDate = new Date();

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
			shoninName1 = LoginUserInfoUtils.getUserName();
			mailKbn = CodeConstant.SHONIN_IRAI_TSUCHI;
			nextWorkflow = CodeConstant.LEVEL_1;
			break;
		case CodeConstant.STATUS_SHONIN1:
			// 承認１
			nextStatus = CodeConstant.STATUS_SHONIN_ZUMI;
			shoninName2 = LoginUserInfoUtils.getUserName();
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
			Skf2010Sc005GetAgreeAuthorityGroupIdExpParameter mApplParam = new Skf2010Sc005GetAgreeAuthorityGroupIdExpParameter();
			Skf2010Sc005GetAgreeAuthorityGroupIdExp mApplResult = new Skf2010Sc005GetAgreeAuthorityGroupIdExp();
			mApplParam.setCompanyCd(companyCd);
			mApplParam.setApplId(applNo);
			mApplParam.setWfLevel(nextWorkflow);
			mApplResult = skf2010Sc005GetAgreeAuthorityGroupIdExpRepository.getAgreeAuthorityGroupId(mApplParam);
			if (mApplResult != null) {
				sendGroupId = mApplResult.getRoleId();
			}
		}

		if (!CheckUtils.isEmpty(sendGroupId)) {
			nextStatus = CodeConstant.STATUS_SHONIN_ZUMI;
			shoninName1 = LoginUserInfoUtils.getUserName();
			mailKbn = CodeConstant.SHONIN_KANRYO_TSUCHI;
			nextWorkflow = CodeConstant.LEVEL_5;
			sendUserId = applInfo.get("applShainNo");
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

		Skf2010Sc005GetCommentInfoForUpdateExp tmpTApplCommentData = new Skf2010Sc005GetCommentInfoForUpdateExp();
		// 次階層のステータスが”31”【承認中】の場合、承認中コメントを削除
		if (nextStatus.equals(CodeConstant.STATUS_SHONIN1)) {
			Skf2010Sc005GetCommentInfoForUpdateExpParameter tApplCommentParam = new Skf2010Sc005GetCommentInfoForUpdateExpParameter();
			tApplCommentParam.setCompanyCd(companyCd);
			tApplCommentParam.setApplNo(applNo);
			tApplCommentParam.setApplStatus(nextStatus);
			tmpTApplCommentData = skf2010Sc005GetCommentInfoForUpdateExpRepository
					.getCommentInfoForUpdate(tApplCommentParam);
			if (tmpTApplCommentData != null) {

				Skf2010TApplCommentKey key = new Skf2010TApplCommentKey();
				key.setCompanyCd(companyCd);
				key.setApplNo(applNo);
				key.setApplStatus(nextStatus);

				int delComRes = skf2010TApplCommentRepository.deleteByPrimaryKey(key);
				if (delComRes <= 0) {
					return false;
				}
			}
		}
		// 次階層のステータスが”40”【承認済】の場合、承認中コメントを承認済みコメントに更新
		if (nextStatus.equals(CodeConstant.STATUS_SHONIN_ZUMI)) {
			Skf2010Sc005GetCommentInfoForUpdateExpParameter tApplCommentParam = new Skf2010Sc005GetCommentInfoForUpdateExpParameter();
			tApplCommentParam.setCompanyCd(companyCd);
			tApplCommentParam.setApplNo(applNo);
			tApplCommentParam.setApplStatus(nowApplStatus);
			tmpTApplCommentData = skf2010Sc005GetCommentInfoForUpdateExpRepository
					.getCommentInfoForUpdate(tApplCommentParam);
			if (tmpTApplCommentData != null) {
				Skf2010Sc005UpdateCommentInfoExp updData = new Skf2010Sc005UpdateCommentInfoExp();
				updData.setCompanyCd(companyCd);
				updData.setApplNo(applNo);
				updData.setUpdateApplStatus(nextStatus);
				updData.setApplStatus(nowApplStatus);
				int updComRes = skf2010Sc005UpdateCommentInfoExpRepository.updateCommentInfo(updData);
				if (updComRes <= 0) {
					return false;
				}
			}
		}
		// 承認完了通知の場合のみ
		if (mailKbn.equals(CodeConstant.SHONIN_KANRYO_TSUCHI)) {
			// メール送付
			String comment = "";
			String annai = "";
			String furikomiStartDate = "";
			String sendUser = applInfo.get("applShainNo");
			sendApplTsuchiMail(mailKbn, applInfo, comment, annai, furikomiStartDate, sendUser, sendGroupId);
		}

		return true;
	}

	/**
	 * 申請情報からリストテーブルのデータを作成します
	 * 
	 * @param tApplHistoryData
	 * @return
	 */
	public List<Map<String, Object>> createListTable(List<Skf2010Sc005GetShoninIchiranShoninExp> tApplHistoryData) {
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		if (tApplHistoryData.size() <= 0) {
			return returnList;
		}

		// 汎用コード取得
		Map<String, String> genericCodeMap = new HashMap<String, String>();
		genericCodeMap = skfGenericCodeUtils.getGenericCode("SKF1001");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

		for (int i = 0; i < tApplHistoryData.size(); i++) {
			String applDate = "";
			String agreDate = "";
			String applStatus = "";
			// 承認チェックフラグ
			boolean chkSelectAgreeTarget = true;

			Skf2010Sc005GetShoninIchiranShoninExp tmpData = tApplHistoryData.get(i);
			// 承認チェックフラグ判定
			chkSelectAgreeTarget = checkAgree(tmpData);

			String chkAgreeCode = "";
			if (chkSelectAgreeTarget) {
				chkAgreeCode = "〇";
			} else {
				chkAgreeCode = "-";
			}

			// 日付型を文字列型に変更する
			if (tmpData.getApplDate() != null) {
				applDate = sdf.format(tmpData.getApplDate()).toString();
			}
			if (tmpData.getAgreDate() != null) {
				agreDate = sdf.format(tmpData.getAgreDate()).toString();
			}
			// 一時保存は申請日を表示しない
			if (CodeConstant.STATUS_ICHIJIHOZON.equals(tmpData.getApplStatus())) {
				applDate = "";
			}

			// 申請状況をコードから汎用コードに変更
			if (tmpData.getApplStatus() != null) {
				applStatus = genericCodeMap.get(tmpData.getApplStatus());
			}

			Map<String, Object> tmpMap = new HashMap<String, Object>();
			tmpMap.put("applId", tmpData.getApplId()); // 申請書ID
			tmpMap.put("applStatusCd", tmpData.getApplStatus()); // 申請状況コード
			tmpMap.put("chkAgree", chkAgreeCode);
			tmpMap.put("applStatus", applStatus); // 申請状況
			tmpMap.put("applNo", tmpData.getApplNo()); // 申請書番号
			tmpMap.put("applDate", applDate); // 申請日
			tmpMap.put("shainNo", tmpData.getShainNo()); // 社員番号
			tmpMap.put("name", tmpData.getName()); // 申請者名
			tmpMap.put("applName", tmpData.getApplName()); // 申請書類名
			tmpMap.put("agreDate", agreDate); // 承認/修正依頼日
			tmpMap.put("agreName1", tmpData.getAgreName1()); // 承認者名１/修正依頼社名
			tmpMap.put("agreName2", tmpData.getAgreName2()); // 承認者名２/修正依頼社名
			tmpMap.put("detail", ""); // 確認ボタン（アイコン）

			returnList.add(tmpMap);
		}
		return returnList;
	}

	/**
	 * 機関名を取得します
	 * 
	 * @param companyCd
	 * @param agencyCd
	 * @return
	 */
	public String getAgencyName(String companyCd, String agencyCd) {
		String agencyName = "";
		Skf2010Sc005GetAgencyNameExp agencyData = new Skf2010Sc005GetAgencyNameExp();
		Skf2010Sc005GetAgencyNameExpParameter param = new Skf2010Sc005GetAgencyNameExpParameter();
		param.setCompanyCd(companyCd);
		param.setAgencyCd(agencyCd);

		agencyData = skf2010Sc005GetAgencyNameExpRepository.getAgencyName(param);
		if (agencyData != null) {
			agencyName = agencyData.getAgencyName();
		}

		return agencyName;
	}

	/**
	 * 部等の名称を取得します
	 * 
	 * @param companyCd
	 * @param agencyCd
	 * @param affiliation1Cd
	 * @return
	 */
	public String getAffiliation1Name(String companyCd, String agencyCd, String affiliation1Cd) {
		List<Skf2010Sc005GetAffiliation1ListExp> affiliation1MapList = new ArrayList<Skf2010Sc005GetAffiliation1ListExp>();
		String affiliation1Name = "";

		if (affiliation1Cd == null || CheckUtils.isEmpty(affiliation1Cd)) {
			return affiliation1Name;
		}
		Skf2010Sc005GetAffiliation1ListExpParameter param = new Skf2010Sc005GetAffiliation1ListExpParameter();
		param.setCompanyCd(companyCd);
		param.setAgencyCd(agencyCd);
		param.setAffiliation1Cd(affiliation1Cd);
		affiliation1MapList = skf2010Sc005GetAffiliation1ListExpRepository.getAffiliation1List(param);

		if (affiliation1MapList.size() > 0) {
			Skf2010Sc005GetAffiliation1ListExp tmpData = affiliation1MapList.get(0);
			affiliation1Name = tmpData.getAffiliation1Name();
		}

		return affiliation1Name;
	}

	/**
	 * 室、チーム又は課の名称を取得します
	 * 
	 * @param companyCd
	 * @param agencyCd
	 * @param affiliation1Cd
	 * @param affiliation2Cd
	 * @return
	 */
	public String getAffiliation2Name(String companyCd, String agencyCd, String affiliation1Cd, String affiliation2Cd) {
		List<Skf2010Sc005GetAffiliation2ListExp> affiliation2MapList = new ArrayList<Skf2010Sc005GetAffiliation2ListExp>();
		String affiliation2Name = "";

		if (affiliation1Cd == null || CheckUtils.isEmpty(affiliation1Cd)) {
			return affiliation2Name;
		}
		Skf2010Sc005GetAffiliation2ListExpParameter param = new Skf2010Sc005GetAffiliation2ListExpParameter();
		param.setCompanyCd(companyCd);
		param.setAgencyCd(agencyCd);
		param.setAffiliation1Cd(affiliation1Cd);
		param.setAffiliation2Cd(affiliation2Cd);
		affiliation2MapList = skf2010Sc005GetAffiliation2ListExpRepository.getAffiliation2List(param);

		if (affiliation2MapList.size() > 0) {
			Skf2010Sc005GetAffiliation2ListExp tmpData = affiliation2MapList.get(0);
			affiliation2Name = tmpData.getAffiliation2Name();
		}

		return affiliation2Name;
	}

	/**
	 * 社宅入居等申請調書データをマッピングします
	 * 
	 * @param companyCd
	 * @param applNo
	 * @param strList
	 * @return
	 */
	public void setTNyukyoChoshoTsuchi(String companyCd, String applNo, List<String> titleList, List<String> strList) {
		Skf2020TNyukyoChoshoTsuchiKey skf2020TNyukyoChoshoTsuchiKey = new Skf2020TNyukyoChoshoTsuchiKey();
		skf2020TNyukyoChoshoTsuchiKey.setCompanyCd(companyCd);
		skf2020TNyukyoChoshoTsuchiKey.setApplNo(applNo);
		Skf2020TNyukyoChoshoTsuchi result = skf2020TNyukyoChoshoTsuchiRepository
				.selectByPrimaryKey(skf2020TNyukyoChoshoTsuchiKey);
		if (result != null) {
			titleList.add("company_cd");
			strList.add(result.getCompanyCd());
			titleList.add("agency");
			strList.add(result.getAgency());
			titleList.add("affiliation1");
			strList.add(result.getAffiliation1());
			titleList.add("affiliation2");
			strList.add(result.getAffiliation2());

			titleList.add("taiyo_hitsuyo");
			strList.add(result.getTaiyoHitsuyo());
			titleList.add("hitsuyo_riyu");
			strList.add(result.getHitsuyoRiyu());
			titleList.add("fuhitsuyo_riyu");
			strList.add(result.getFuhitsuyoRiyu());
			titleList.add("tokyu");
			strList.add(result.getTokyu());
			titleList.add("tel");
			strList.add(result.getTel());
			titleList.add("new_agency_other");
			strList.add(result.getNewAgencyOther());
			titleList.add("new_agency");
			strList.add(result.getNewAgency());
			titleList.add("new_affiliation1_other");
			strList.add(result.getNewAffiliation1Other());
			titleList.add("new_affiliation1");
			strList.add(result.getNewAffiliation1());
			titleList.add("new_affiliation2_other");
			strList.add(result.getNewAffiliation2Other());
			titleList.add("new_affiliation2");
			strList.add(result.getNewAffiliation2());
			titleList.add("hitsuyo_shataku");
			strList.add(result.getHitsuyoShataku());
			titleList.add("dokyo_relation1");
			strList.add(result.getDokyoRelation1());
			titleList.add("dokyo_name1");
			strList.add(result.getDokyoName1());
			titleList.add("dokyo_age1");
			strList.add(result.getDokyoAge1());
			titleList.add("dokyo_relation2");
			strList.add(result.getDokyoRelation2());
			titleList.add("dokyo_name2");
			strList.add(result.getDokyoName2());
			titleList.add("dokyo_age2");
			strList.add(result.getDokyoAge2());
			titleList.add("dokyo_relation3");
			strList.add(result.getDokyoRelation3());
			titleList.add("dokyo_name3");
			strList.add(result.getDokyoName3());
			titleList.add("dokyo_age3");
			strList.add(result.getDokyoAge3());
			titleList.add("dokyo_relation4");
			strList.add(result.getDokyoRelation4());
			titleList.add("dokyo_name4");
			strList.add(result.getDokyoName4());
			titleList.add("dokyo_age4");
			strList.add(result.getDokyoAge4());
			titleList.add("dokyo_relation5");
			strList.add(result.getDokyoRelation5());
			titleList.add("dokyo_name5");
			strList.add(result.getDokyoName5());
			titleList.add("dokyo_age5");
			strList.add(result.getDokyoAge5());
			titleList.add("dokyo_relation6");
			strList.add(result.getDokyoRelation6());
			titleList.add("dokyo_name6");
			strList.add(result.getDokyoName6());
			titleList.add("dokyo_age6");
			strList.add(result.getDokyoAge6());
			titleList.add("nyukyo_yotei_date");
			strList.add(result.getNyukyoKanoDate());
			titleList.add("parking_umu");
			strList.add(result.getParkingUmu());
			titleList.add("car_no_input_flg");
			strList.add(result.getCarNoInputFlg());
			titleList.add("car_name");
			strList.add(result.getCarName());
			titleList.add("car_no");
			strList.add(result.getCarNo());
			titleList.add("car_expiration_date");
			strList.add(result.getCarExpirationDate());
			titleList.add("car_user");
			strList.add(result.getCarUser());
			titleList.add("parking_use_date");
			strList.add(result.getParkingUseDate());
			titleList.add("now_shataku");
			strList.add(result.getNowShataku());
			titleList.add("now_shataku_name");
			strList.add(result.getNowShatakuName());
			titleList.add("now_shataku_no");
			strList.add(result.getNowShatakuNo());
			titleList.add("now_shataku_kikaku");
			strList.add(result.getNowShatakuKikaku());
			titleList.add("now_shataku_menseki");
			strList.add(result.getNowShatakuMenseki());
			titleList.add("taikyo_yotei");
			strList.add(result.getTaikyoYotei());
			titleList.add("taikyo_yotei_date");
			strList.add(result.getTaikyoYoteiDate());
			titleList.add("taikyo_riyu");
			strList.add(result.getTaikyoRiyu());
			titleList.add("taikyo_riyu_kbn");
			strList.add(result.getTaikyoRiyuKbn());
			titleList.add("taikyogo_renrakusaki");
			strList.add(result.getTaikyogoRenrakusaki());
			titleList.add("tokushu_jijo");
			strList.add(result.getTokushuJijo());
			titleList.add("new_shataku_kanri_no");
			strList.add(result.getNewShatakuKanriNo());
			titleList.add("new_shozaichi");
			strList.add(result.getNewShozaichi());
			titleList.add("new_shataku_name");
			strList.add(result.getNewShatakuName());
			titleList.add("new_shataku_no");
			strList.add(result.getNewShatakuNo());
			titleList.add("new_shataku_kikaku");
			strList.add(result.getNewShatakuKikaku());
			titleList.add("new_shataku_menseki");
			strList.add(result.getNewShatakuMenseki());
			titleList.add("new_rental");
			strList.add(result.getNewRental());
			titleList.add("kyoekihi_person_kyogichu_flg");
			strList.add(result.getKyoekihiPersonKyogichuFlg());
			titleList.add("new_kyoekihi");
			strList.add(result.getNewKyoekihi());
			titleList.add("nyukyo_kano_date");
			strList.add(result.getNyukyoKanoDate());
			titleList.add("parking_area");
			strList.add(result.getParkingArea());
			titleList.add("car_ichi_no");
			strList.add(result.getCarIchiNo());
			titleList.add("parking_rental");
			strList.add(result.getParkingRental());
			titleList.add("parking_kano_date");
			strList.add(result.getParkingKanoDate());
			titleList.add("seiyaku_date");
			strList.add(result.getSeiyakuDate());
			titleList.add("tsuchi_date");
			strList.add(result.getTsuchiDate());
			titleList.add("combo_flg");
			strList.add(result.getComboFlg());
			titleList.add("import_flg");
			strList.add(result.getImportFlg());
			titleList.add("nyukyo_date_flg");
			strList.add(result.getNyukyoDateFlg());
			titleList.add("taikyo_date_flg");
			strList.add(result.getTaikyoDateFlg());
			titleList.add("parking_s_date_flg");
			strList.add(result.getParkingSDateFlg());
			titleList.add("gender");
			String strGender = "";
			if (result.getGender() != null) {
				strGender = result.getGender().toString();
			}
			strList.add(strGender);
			titleList.add("shataku_no");
			String strShatakuNo = "";
			if (result.getShatakuNo() != null) {
				strShatakuNo = result.getShatakuNo().toString();
			}
			strList.add(strShatakuNo);
			titleList.add("room_kanri_no");
			String strRoomKanriNo = "";
			if (result.getRoomKanriNo() != null) {
				strRoomKanriNo = result.getRoomKanriNo().toString();
			}
			strList.add(strRoomKanriNo);
			titleList.add("car_no_input_flg2");
			strList.add(result.getCarNoInputFlg2());
			titleList.add("car_name2");
			strList.add(result.getCarName2());
			titleList.add("car_no2");
			strList.add(result.getCarNo2());
			titleList.add("car_expiration_date2");
			strList.add(result.getCarExpirationDate2());
			titleList.add("car_user2");
			strList.add(result.getCarUser2());
			titleList.add("parking_use_date2");
			strList.add(result.getParkingUseDate2());
			titleList.add("shataku_jotai");
			strList.add(result.getShatakuJotai());
			titleList.add("session_day");
			strList.add(result.getSessionDay());
			titleList.add("session_time");
			strList.add(result.getSessionTime());
			titleList.add("renraku_saki");
			strList.add(result.getRenrakuSaki());
			titleList.add("parking_area2");
			strList.add(result.getParkingArea2());
			titleList.add("car_ichi_no2");
			strList.add(result.getCarIchiNo2());
			titleList.add("parking_rental2");
			strList.add(result.getParkingRental2());
			titleList.add("parking_kano_date2");
			strList.add(result.getParkingKanoDate2());
			titleList.add("bihin_kibo");
			strList.add(result.getBihinKibo());
			titleList.add("key_manager");
			strList.add(result.getKeyManager());
			titleList.add("key_manager_tel");
			strList.add(result.getKeyManagerTel());
			titleList.add("now_parking_area");
			strList.add(result.getNowParkingArea());
			titleList.add("now_car_ichi_no");
			strList.add(result.getNowCarIchiNo());
			titleList.add("now_parking_area2");
			strList.add(result.getNowParkingArea2());
			titleList.add("now_car_ichi_no2");
			strList.add(result.getNowCarIchiNo2());
			titleList.add("now_shataku_kanri_no");
			String strNowShatakuKanriNo = "";
			if (result.getNowShatakuKanriNo() != null) {
				strNowShatakuKanriNo = result.getNowShatakuKanriNo().toString();
			}
			strList.add(strNowShatakuKanriNo);
			titleList.add("now_room_kanri_no");
			String strNowRoomKanriNo = "";
			if (result.getNowRoomKanriNo() != null) {
				strNowRoomKanriNo = result.getNowRoomKanriNo().toString();
			}
			strList.add(strNowRoomKanriNo);
		}
		return;
	}

	/**
	 * 退居（自動車の保管場所返還）届のデータをマッピングします
	 * 
	 * @param companyCd
	 * @param applNo
	 * @param titleList
	 * @param strList
	 */
	public void setTTaikyoReport(String companyCd, String applNo, List<String> titleList, List<String> strList) {
		Skf2040TTaikyoReport result = new Skf2040TTaikyoReport();
		Skf2040TTaikyoReportKey param = new Skf2040TTaikyoReportKey();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		result = skf2040TTaikyoReportRepository.selectByPrimaryKey(param);
		if (result != null) {
			titleList.add("company_cd");
			strList.add(result.getCompanyCd());
			titleList.add("agency");
			strList.add(result.getAgency());
			titleList.add("affiliation1");
			strList.add(result.getAffiliation1());
			titleList.add("affiliation2");
			strList.add(result.getAffiliation2());

			titleList.add("gender");
			strList.add(result.getGender());
			titleList.add("address");
			strList.add(result.getAddress());
			titleList.add("taikyo_date");
			strList.add(result.getTaikyoDate());
			titleList.add("parking_henkan_date");
			strList.add(result.getParkingHenkanDate());
			titleList.add("shataku_taikyo_kbn");
			strList.add(result.getShatakuTaikyoKbn());
			titleList.add("taikyo_area");
			strList.add(result.getTaikyoArea());
			titleList.add("taikyo_riyu");
			strList.add(result.getTaikyoRiyu());
			titleList.add("taikyo_riyu_kbn");
			strList.add(result.getTaikyoRiyuKbn());
			titleList.add("taikyogo_renrakusaki");
			strList.add(result.getTaikyogoRenrakusaki());
			titleList.add("taikyo_date_flg");
			strList.add(result.getTaikyoDateFlg());
			titleList.add("parking_e_date_flg");
			strList.add(result.getParkingEDateFlg());
			titleList.add("shataku_no");
			String strShatakuNo = "";
			if (result.getShatakuNo() != null) {
				strShatakuNo = result.getShatakuNo().toString();
			}
			strList.add(strShatakuNo);
			titleList.add("room_kanri_no");
			String strRoomKanriNo = "";
			if (result.getRoomKanriNo() != null) {
				strRoomKanriNo = result.getRoomKanriNo().toString();
			}
			strList.add(strRoomKanriNo);
			titleList.add("shataku_taikyo_kbn2");
			strList.add(result.getShatakuTaikyoKbn2());
			titleList.add("taikyo_shataku");
			strList.add(result.getTaikyoShataku());
			titleList.add("taikyo_parking1");
			strList.add(result.getTaikyoParking1());
			titleList.add("taikyo_parking2");
			strList.add(result.getTaikyoParking2());
			titleList.add("shataku_jotai");
			strList.add(result.getShatakuJotai());
			titleList.add("session_day");
			strList.add(result.getSessionDay());
			titleList.add("session_time");
			strList.add(result.getSessionTime());
			titleList.add("renraku_saki");
			strList.add(result.getRenrakuSaki());
			titleList.add("parking_address1");
			strList.add(result.getParkingAddress1());
			titleList.add("parking_address2");
			strList.add(result.getParkingAddress2());
		}
		return;
	}

	/**
	 * 備品希望申請のデータをマッピングします
	 * 
	 * @param companyCd
	 * @param applNo
	 * @param titleList
	 * @param strList
	 */
	public void setTBihinKibouShinsei(String companyCd, String applNo, List<String> titleList, List<String> strList) {
		Skf2030TBihinKiboShinsei result = new Skf2030TBihinKiboShinsei();
		Skf2030TBihinKiboShinseiKey param = new Skf2030TBihinKiboShinseiKey();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		result = skf2030TBihinKiboShinseiRepository.selectByPrimaryKey(param);
		if (result != null) {
			titleList.add("company_cd");
			strList.add(result.getCompanyCd());
			titleList.add("nyukyo_appl_no");
			strList.add(result.getNyukyoApplNo());
			titleList.add("agency");
			strList.add(result.getAgency());
			titleList.add("affiliation1");
			strList.add(result.getAffiliation1());
			titleList.add("affiliation2");
			strList.add(result.getAffiliation2());
			titleList.add("tel");
			strList.add(result.getTel());
			titleList.add("tokyu");
			strList.add(result.getTokyu());
			titleList.add("gender");
			strList.add(result.getGender());
			titleList.add("shataku_no");
			strList.add(result.getShainNo());
			titleList.add("room_kanri_no");
			String strRoomKanriNo = "";
			if (result.getRoomKanriNo() != null) {
				strRoomKanriNo = result.getRoomKanriNo().toString();
			}
			strList.add(strRoomKanriNo);
			titleList.add("now_shataku_name");
			strList.add(result.getNowShatakuName());
			titleList.add("now_shataku_no");
			strList.add(result.getNowShatakuNo());
			titleList.add("now_shataku_kikaku");
			strList.add(result.getNowShatakuKikaku());
			titleList.add("now_shataku_menseki");
			strList.add(result.getNowShatakuMenseki());
			titleList.add("renraku_saki");
			strList.add(result.getRenrakuSaki());
			titleList.add("ukeire_dairi_name");
			strList.add(result.getUkeireDairiName());
			titleList.add("ukeire_dairi_apoint");
			strList.add(result.getUkeireDairiApoint());
			titleList.add("session_day");
			strList.add(result.getSessionDay());
			titleList.add("session_time");
			strList.add(result.getSessionTime());
			titleList.add("completion_day");
			strList.add(result.getCompletionDay());
		}
		return;
	}

	/**
	 * 備品返却申請のデータをマッピングします
	 * 
	 * @param companyCd
	 * @param applNo
	 * @param titleList
	 * @param strList
	 */
	public void setTBihinHenkyakuShinsei(String companyCd, String applNo, List<String> titleList,
			List<String> strList) {
		Skf2050TBihinHenkyakuShinsei result = new Skf2050TBihinHenkyakuShinsei();
		Skf2050TBihinHenkyakuShinseiKey param = new Skf2050TBihinHenkyakuShinseiKey();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		result = skf2050TBihinHenkyakuShinseiRepository.selectByPrimaryKey(param);
		if (result != null) {
			titleList.add("company_cd");
			strList.add(result.getCompanyCd());
			titleList.add("taikyo_appl_no");
			strList.add(result.getTaikyoApplNo());
			titleList.add("agency");
			strList.add(result.getAgency());
			titleList.add("affiliation1");
			strList.add(result.getAffiliation1());
			titleList.add("affiliation2");
			strList.add(result.getAffiliation2());
			titleList.add("tel");
			strList.add(result.getTel());
			titleList.add("tokyu");
			strList.add(result.getTokyu());
			titleList.add("gender");
			strList.add(result.getGender());
			titleList.add("shataku_no");
			String strShatakuNo = "";
			if (result.getShatakuNo() != null) {
				strShatakuNo = result.getShatakuNo().toString();
			}
			strList.add(strShatakuNo);
			titleList.add("room_kanri_no");
			String strRoomKanriNo = "";
			if (result.getRoomKanriNo() != null) {
				strRoomKanriNo = result.getRoomKanriNo().toString();
			}
			strList.add(strRoomKanriNo);
			titleList.add("now_shataku_name");
			strList.add(result.getNowShatakuName());
			titleList.add("now_shataku_no");
			strList.add(result.getNowShatakuNo());
			titleList.add("now_shataku_kikaku");
			strList.add(result.getNowShatakuKikaku());
			titleList.add("now_shataku_menseki");
			strList.add(result.getNowShatakuMenseki());
			titleList.add("renraku_saki");
			strList.add(result.getRenrakuSaki());
			titleList.add("tatiai_dairi_name");
			strList.add(result.getTatiaiDairiName());
			titleList.add("tatiai_dairi_apoint");
			strList.add(result.getTatiaiDairiApoint());
			titleList.add("tatiai_dairi_apoint");
			strList.add(result.getTatiaiDairiApoint());
			titleList.add("session_day");
			strList.add(result.getSessionDay());
			titleList.add("session_time");
			strList.add(result.getSessionTime());
			titleList.add("completion_day");
			strList.add(result.getCompletionDay());
		}
		return;
	}

	/**** ここからprivateメソッド ***/

	/**
	 * 一括承認フラグチェック
	 * 
	 * @param tmpData
	 * @return
	 */
	private boolean checkAgree(Skf2010Sc005GetShoninIchiranShoninExp tmpData) {
		// 「一括承認可能フラグ」に、不可が設定されている場合
		if (tmpData != null && "0".equals(tmpData.getSumApprovalFlg())) {
			return false;
		}
		String applStatus = tmpData.getApplStatus();

		// ログインユーザが承認権限を持たない申請書類である場合
		Set<String> roleIds = LoginUserInfoUtils.getRoleIds();
		if (roleIds == null) {
			return false;
		}
		for (String roleId : roleIds) {
			if (!isAgreeAuthority(companyCd, tmpData.getApplId(), roleId, applStatus)) {
				return false;
			}
		}

		String agreName1 = tmpData.getAgreName1();
		String shoninName1 = LoginUserInfoUtils.getUserName();
		// 「承認者名1」いずれかが、セッションのログインユーザ名と同じ場合
		if (agreName1 != null && agreName1.equals(shoninName1)) {
			return false;
		}

		// 社宅入居、退居申請の時
		if (FunctionIdConstant.R0100.equals(tmpData.getApplId())) {
			if (CodeConstant.STATUS_DOI_ZUMI.equals(applStatus) || CodeConstant.STATUS_SHONIN1.equals(applStatus)) {
				// 提示データの共益費が協議中だったら一括承認ボタンを非活性にする
				if (selectKyoekihiKyogi(tmpData.getShainNo(), CodeConstant.NYUTAIKYO_KBN_NYUKYO, tmpData.getApplNo())) {
					return false;
				}
			}
		} else if (FunctionIdConstant.R0104.equals(tmpData.getApplId()) || "R0105".equals(tmpData.getApplId())) {
			// 「申請書類ID」が「備品希望申請」「備品返却確認」のいずれかであり、
			// 「ステータス」が搬入済/搬出済/承認中でいずれでもない場合
			if (!(CodeConstant.STATUS_HANNYU_ZUMI.equals(applStatus)
					&& CodeConstant.STATUS_HANSYUTSU_ZUMI.equals(applStatus)
					&& CodeConstant.STATUS_SHONIN1.equals(applStatus))) {
				return false;
			}
		} else if (FunctionIdConstant.R0103.equals(tmpData.getApplId())) {
			// 「申請書類ID」が「社宅（自動車保管場所）退居届」であり、「ステータス」が承認中でない場合
			if (!CodeConstant.STATUS_SHONIN1.equals(applStatus)) {
				return false;
			}
		} else {
			// 「申請書類ID」が前述のいずれでもなく、「ステータス」が審査中/承認中のいずれでもない場合
			if (!(CodeConstant.STATUS_SHINSEICHU.equals(applStatus) && CodeConstant.STATUS_SHINSACHU.equals(applStatus)
					&& CodeConstant.STATUS_SHONIN1.equals(applStatus))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 提示データの共益費が協議中かチェックする
	 * 
	 * @param shainNo
	 * @param nyutaikyoKbn
	 * @param applNo
	 * @return
	 */
	private boolean selectKyoekihiKyogi(String shainNo, String nyutaikyoKbn, String applNo) {
		boolean result = true;
		List<Skf2010Sc005GetTeijiDataInfoExp> teijiDataList = new ArrayList<Skf2010Sc005GetTeijiDataInfoExp>();
		Skf2010Sc005GetTeijiDataInfoExpParameter param = new Skf2010Sc005GetTeijiDataInfoExpParameter();
		param.setShainNo(shainNo);
		param.setNyutaikyoKbn(nyutaikyoKbn);
		param.setApplNo(applNo);
		teijiDataList = skf2010Sc005GetTeijiDataInfoExpRepository.getTeijiDataInfo(param);
		if (teijiDataList.size() > 0) {
			Skf2010Sc005GetTeijiDataInfoExp teijiData = teijiDataList.get(0);
			if (teijiData.getKyoekihiPersonKyogichuFlg() != null
					&& "1".equals(teijiData.getKyoekihiPersonKyogichuFlg())) {
				result = false;
			}
		}
		return result;
	}

	/**
	 * 承認権限チェックを行う
	 * 
	 * @param companyCd
	 * @param applId
	 * @param roleId
	 * @param applStatus
	 * @return
	 */
	private boolean isAgreeAuthority(String companyCd, String applId, String roleId, String applStatus) {
		boolean result = false;

		// ステータスチェック
		if (CodeConstant.STATUS_SHINSEICHU.equals(applStatus)
				|| CodeConstant.STATUS_SHOZOKUCHO_KAKUNINZUMI.equals(applStatus)
				|| CodeConstant.STATUS_SHINSACHU.equals(applStatus)
				|| CodeConstant.STATUS_TORIKOMI_KANRYO.equals(applStatus)
				|| CodeConstant.STATUS_DOI_ZUMI.equals(applStatus) || CodeConstant.STATUS_DOI_SHINAI.equals(applStatus)
				|| CodeConstant.STATUS_HANSYUTSU_ZUMI.equals(applStatus)
				|| CodeConstant.STATUS_HANNYU_ZUMI.equals(applStatus) || CodeConstant.STATUS_SHONIN1.equals(applStatus)
				|| CodeConstant.STATUS_SHONIN2.equals(applStatus)) {

			String wfLevel = "";
			switch (applStatus) {
			case CodeConstant.STATUS_SHINSEICHU:
			case CodeConstant.STATUS_SHOZOKUCHO_KAKUNINZUMI:
			case CodeConstant.STATUS_SHINSACHU:
			case CodeConstant.STATUS_TORIKOMI_KANRYO:
			case CodeConstant.STATUS_DOI_ZUMI:
			case CodeConstant.STATUS_DOI_SHINAI:
			case CodeConstant.STATUS_HANSYUTSU_ZUMI:
			case CodeConstant.STATUS_HANNYU_ZUMI:
				wfLevel = CodeConstant.LEVEL_1;
				break;
			case CodeConstant.STATUS_SHONIN1:
				wfLevel = CodeConstant.LEVEL_2;
				break;
			case CodeConstant.STATUS_SHONIN2:
				wfLevel = CodeConstant.LEVEL_3;
				break;
			}

			int cnt = 0;
			Skf2010Sc005GetAgreeAuthorityCountExp mApplicationExp = new Skf2010Sc005GetAgreeAuthorityCountExp();
			Skf2010Sc005GetAgreeAuthorityCountExpParameter param = new Skf2010Sc005GetAgreeAuthorityCountExpParameter();
			param.setCompanyCd(companyCd);
			param.setApplId(applId);
			param.setRoleId(roleId);
			param.setWfLevel(wfLevel);
			mApplicationExp = skf2010Sc005GetAgreeAuthorityCountExpRepository.getAgreeAuthorityCount(param);
			if (mApplicationExp != null) {
				cnt = Integer.valueOf(mApplicationExp.getExpr1());
				if (cnt > 0) {
					result = true;
				}
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
		List<Skf2010Sc005GetTeijiDataInfoExp> tTaijiDataList = new ArrayList<Skf2010Sc005GetTeijiDataInfoExp>();
		Skf2010Sc005GetTeijiDataInfoExpParameter param = new Skf2010Sc005GetTeijiDataInfoExpParameter();
		param.setShainNo(shainNo);
		param.setNyutaikyoKbn(nyutaikyoKbn);
		param.setApplNo(applNo);
		tTaijiDataList = skf2010Sc005GetTeijiDataInfoExpRepository.getTeijiDataInfo(param);
		if (tTaijiDataList != null && tTaijiDataList.size() > 0) {
			Skf2010Sc005GetTeijiDataInfoExp tTaijiData = tTaijiDataList.get(0);
			if (tTaijiData.getRentalAdjust() != null) {
				Skf2010Sc005UpdateNyukyoChoshoTsuchiRentalExp updateData = new Skf2010Sc005UpdateNyukyoChoshoTsuchiRentalExp();
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
				int res = skf2010Sc005UpdateNyukyoChoshoTsuchiRentalExpRepository
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
		Skf2010Sc005GetSendApplMailInfoExp skfMShainData = new Skf2010Sc005GetSendApplMailInfoExp();
		List<Skf2010Sc005GetSendApplMailInfoExp> skfMShainList = new ArrayList<Skf2010Sc005GetSendApplMailInfoExp>();
		Skf2010Sc005GetSendApplMailInfoExpParameter mShainParam = new Skf2010Sc005GetSendApplMailInfoExpParameter();
		mShainParam.setCompanyCd(companyCd);
		mShainParam.setShainNo(applInfo.get("applShainNo"));
		mShainParam.setApplNo(applInfo.get("applNo"));
		skfMShainList = skf2010Sc005GetSendApplMailInfoExpRepository.getSendApplMailInfo(mShainParam);
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
