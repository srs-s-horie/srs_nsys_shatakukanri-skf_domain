package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc003;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc003.Skf2010Sc003DeleteApplHistoryExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc003.Skf2010Sc003DeleteDocTableExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc003.Skf2010Sc003DeleteRouterLendingYoteiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc003.Skf2010Sc003GetApplHistoryStatusInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc003.Skf2010Sc003GetApplHistoryStatusInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc003.Skf2010Sc003GetApplHistoryStatusInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc003.Skf2010Sc003GetApplHistoryStatusInfoForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc003.Skf2010Sc003GetRouterLendingYoteiDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc003.Skf2010Sc003GetRouterLendingYoteiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc003.Skf2010Sc003UpdateApplHistoryAgreeStatusExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc003.Skf2010Sc003UpdateRouterLendingYoteiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfApplHistoryInfoUtils.SkfApplHistoryInfoUtilsGetApplHistoryInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBatchUtils.SkfBatchUtilsGetMultipleTablesUpdateDateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBihinInfoUtils.SkfBihinInfoUtilsGetBihinShinseiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2100MMobileRouterWithBLOBs;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2100TRouterLendingYoteiData;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2100TRouterLendingYoteiDataKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc003.Skf2010Sc003DeleteApplHistoryExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc003.Skf2010Sc003DeleteDocTableExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc003.Skf2010Sc003DeleteRouterLendingYoteiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc003.Skf2010Sc003GetApplHistoryStatusInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc003.Skf2010Sc003GetApplHistoryStatusInfoForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc003.Skf2010Sc003GetRouterLendingYoteiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc003.Skf2010Sc003UpdateApplHistoryAgreeStatusExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc003.Skf2010Sc003UpdateRouterLendingYoteiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2100MMobileRouterRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2100TMobileRouterLedgerRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2100TRouterLendingYoteiDataRepository;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfApplHistoryInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfBihinInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2020Fc001NyukyoKiboSinseiDataImport;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2030Fc001BihinKiboShinseiDataImport;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2040Fc001TaikyoTodokeDataImport;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2050Fc001BihinHenkyakuSinseiDataImport;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010Sc003common.Skf2010Sc003CommonDto;

/**
 * Skf2010Sc005 承認一覧内部処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc003SharedService {

	@Autowired
	private Skf2010Sc003GetApplHistoryStatusInfoExpRepository skf2010Sc003GetApplHistoryStatusInfoExpRepository;
	@Autowired
	private Skf2010Sc003UpdateApplHistoryAgreeStatusExpRepository skf2010Sc003UpdateApplHistoryAgreeStatusExpRepository;
	@Autowired
	private Skf2010Sc003GetApplHistoryStatusInfoForUpdateExpRepository skf2010Sc003GetApplHistoryStatusInfoForUpdateExpRepository;
	@Autowired
	private Skf2010Sc003DeleteDocTableExpRepository skf2010Sc003DeleteDocTableExpRepository;
	@Autowired
	private Skf2010Sc003DeleteApplHistoryExpRepository skf2010Sc003DeleteApplHistoryExpRepository;
	@Autowired
	private Skf2010Sc003UpdateRouterLendingYoteiDataExpRepository skf2010Sc003UpdateRouterLendingYoteiDataExpRepository;
	@Autowired
	private Skf2010Sc003GetRouterLendingYoteiDataExpRepository skf2010Sc003GetRouterLendingYoteiDataExpRepository;
	@Autowired
	private Skf2100MMobileRouterRepository skf2100MMobileRouterRepository;
	@Autowired
	private Skf2100TMobileRouterLedgerRepository skf2100TMobileRouterLedgerRepository;
	@Autowired
	private Skf2010Sc003DeleteRouterLendingYoteiDataExpRepository skf2010Sc003DeleteRouterLendingYoteiDataExpRepository;
	@Autowired
	private SkfApplHistoryInfoUtils skfApplHistoryInfoUtils;
	@Autowired
	private SkfBihinInfoUtils skfBihinInfoUtils;

	@Autowired
	private Skf2020Fc001NyukyoKiboSinseiDataImport skf2020Fc001NyukyoKiboSinseiDataImport;
	@Autowired
	private Skf2040Fc001TaikyoTodokeDataImport skf2040Fc001TaikyoTodokeDataImport;
	@Autowired
	private Skf2030Fc001BihinKiboShinseiDataImport skf2030Fc001BihinKiboShinseiDataImport;
	@Autowired
	private Skf2050Fc001BihinHenkyakuSinseiDataImport skf2050Fc001BihinHenkyakuSinseiDataImport;

	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;

	private String companyCd = CodeConstant.C001;

	public List<Skf2010Sc003GetApplHistoryStatusInfoExp> getApplHistoryStatusInfo(String shainNo, String applDateFrom,
			String applDateTo, String agreDateFrom, String agreDateTo, String applName, List<String> applStatus) {

		List<Skf2010Sc003GetApplHistoryStatusInfoExp> resultList = new ArrayList<Skf2010Sc003GetApplHistoryStatusInfoExp>();
		Skf2010Sc003GetApplHistoryStatusInfoExpParameter param = new Skf2010Sc003GetApplHistoryStatusInfoExpParameter();
		param.setCompanyCd(companyCd);
		if (!CheckUtils.isEmpty(shainNo)) {
			param.setShainNo(shainNo);
		}
		if (!CheckUtils.isEmpty(applDateFrom)) {
			param.setApplDateFrom(applDateFrom);
		}
		if (!CheckUtils.isEmpty(applDateTo)) {
			param.setApplDateTo(applDateTo);
		}
		if (!CheckUtils.isEmpty(agreDateFrom)) {
			param.setAgreDateFrom(agreDateFrom);
		}
		if (!CheckUtils.isEmpty(agreDateTo)) {
			param.setAgreDateTo(agreDateTo);
		}
		if (!CheckUtils.isEmpty(applName)) {
			param.setApplName(applName);
		}
		if (applStatus != null && applStatus.size() != 0) {
			if (applStatus.indexOf(CodeConstant.STATUS_SHINSACHU) >= 0) {
				List<String> applStatusList = new ArrayList<String>();
				applStatusList.addAll(applStatus);
				applStatusList.add(CodeConstant.STATUS_SHONIN1);
				param.setApplStatus(applStatusList);
			} else {
				param.setApplStatus(applStatus);
			}
		}
		resultList = skf2010Sc003GetApplHistoryStatusInfoExpRepository.getApplHistoryStatusInfo(param);

		return resultList;
	}

	/**
	 * 申請情報からリストテーブルのデータを作成します
	 * 
	 * @param applHistoryList
	 * @param dto
	 * @return
	 */
	public List<Map<String, Object>> createListTable(List<Skf2010Sc003GetApplHistoryStatusInfoExp> applHistoryList,
			Skf2010Sc003CommonDto dto) {
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		if (applHistoryList == null || applHistoryList.size() <= 0) {
			return returnList;
		}

		// ログインユーザー情報取得
		Map<String, String> loginUserInfo = skfLoginUserInfoUtils.getSkfLoginUserInfo();

		// 汎用コード取得
		Map<String, String> genericCodeMap = new HashMap<String, String>();
		genericCodeMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_STATUS);

		// 排他処理用最終更新日Map
		Map<String, Date> lastUpdateDateMap = new HashMap<String, Date>();

		for (Skf2010Sc003GetApplHistoryStatusInfoExp applHistoryData : applHistoryList) {
			String applDate = CodeConstant.NONE;
			String agreDate = CodeConstant.NONE;
			String applStatus = CodeConstant.NONE;
			String applStatusCd = CodeConstant.NONE;

			// 日付型を文字列型に変更する
			if (applHistoryData.getApplDate() != null) {
				applDate = skfDateFormatUtils.dateFormatFromDate(applHistoryData.getApplDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}
			if (applHistoryData.getAgreDate() != null) {
				agreDate = skfDateFormatUtils.dateFormatFromDate(applHistoryData.getAgreDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}

			// 申請状況をコードから汎用コードに変更
			if (applHistoryData.getApplStatus() != null) {
				// 申請状況が「31：承認１」で、ログインユーザーが管理者権限でない場合は表示を「審査中」に置き換える
				if (CheckUtils.isEqual(applHistoryData.getApplStatus(), CodeConstant.STATUS_SHONIN1)
						&& !checkAdminRole(loginUserInfo.get("roleId"))) {
					applStatus = genericCodeMap.get(CodeConstant.STATUS_SHINSACHU);
					applStatusCd = CodeConstant.STATUS_SHINSACHU;
				} else {
					applStatus = genericCodeMap.get(applHistoryData.getApplStatus());
					applStatusCd = applHistoryData.getApplStatus();
				}
			}

			Map<String, Object> tmpMap = new HashMap<String, Object>();
			tmpMap.put("detail", ""); // 表示（アイコン）
			tmpMap.put("applId", applHistoryData.getApplId()); // 申請書ID
			tmpMap.put("applStatus", applStatusCd); // 申請状況
			tmpMap.put("applDate", applDate); // 申請日
			tmpMap.put("applStatusText", applStatus); // 申請状況（テキスト）
			tmpMap.put("applNo", applHistoryData.getApplNo()); // 申請書番号
			tmpMap.put("applName", applHistoryData.getApplName()); // 申請書類名
			tmpMap.put("agreDate", agreDate); // 承認/修正依頼日

			// 申請中のみ取消しボタン表示
			if (applHistoryData.getApplStatus().equals(CodeConstant.STATUS_SHINSEICHU)) {
				tmpMap.put("cancel", ""); // 取下げボタン（アイコン）
			}
			// 一時保存、修正依頼（旧差戻し）、差戻し（旧否認）の場合のみ削除ボタン表示
			if (applHistoryData.getApplStatus().equals(CodeConstant.STATUS_ICHIJIHOZON)
					|| applHistoryData.getApplStatus().equals(CodeConstant.STATUS_SASHIMODOSHI)
					|| applHistoryData.getApplStatus().equals(CodeConstant.STATUS_HININ)) {
				// 備品希望申請と備品返却確認のみ削除ボタンを表示しない
				switch (applHistoryData.getApplId()) {
				case FunctionIdConstant.R0104:
				case FunctionIdConstant.R0105:
					break;
				default:
					tmpMap.put("delete", ""); // 削除ボタン（アイコン）
					break;
				}

			}

			returnList.add(tmpMap);

			// 最終更新日セット
			lastUpdateDateMap.put(applHistoryData.getApplNo(), applHistoryData.getUpdateDate());
		}
		// Dtoに最終更新日Mapをセット
		dto.setLastUpdateDateMap(lastUpdateDateMap);

		return returnList;
	}

	/**
	 * 申請履歴を一時保存に変更します
	 * 
	 * @param applNo
	 * @param errorMsg
	 * @param lastUpdateDate
	 * @return
	 */
	public boolean updateApplHistoryCancel(String applNo, String applId, Date lastUpdateDate,
			Map<String, String> errorMsg) throws Exception {
		boolean result = true;

		// 更新対象のデータを取得（行ロック実施）
		Skf2010Sc003GetApplHistoryStatusInfoForUpdateExp baseUpdateData = new Skf2010Sc003GetApplHistoryStatusInfoForUpdateExp();
		Skf2010Sc003GetApplHistoryStatusInfoForUpdateExpParameter param = new Skf2010Sc003GetApplHistoryStatusInfoForUpdateExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		param.setApplId(applId);
		baseUpdateData = skf2010Sc003GetApplHistoryStatusInfoForUpdateExpRepository
				.getApplHistoryStatusInfoForUpdate(param);
		if (baseUpdateData == null) {
			return false;
		}
		// 排他チェック
		if (!CheckUtils.isEqual(baseUpdateData.getUpdateDate(), lastUpdateDate)) {
			errorMsg.put("error", MessageIdConstant.E_SKF_1134);
			return false;
		}

		Skf2010Sc003UpdateApplHistoryAgreeStatusExp updateData = new Skf2010Sc003UpdateApplHistoryAgreeStatusExp();
		// 更新対象のキー設定
		updateData.setCompanyCd(companyCd);
		updateData.setApplNo(applNo);

		updateData.setApplStatus(CodeConstant.STATUS_ICHIJIHOZON);

		int updateResult = skf2010Sc003UpdateApplHistoryAgreeStatusExpRepository
				.updateApplHistoryAgreeStatus(updateData);
		if (updateResult <= 0) {
			result = false;
		}

		return result;
	}

	/**
	 * 申請書類データを削除します
	 * 
	 * @param applNo
	 * @param applId
	 * @param saveTableList
	 * @param lastUpdateDate
	 * @param errorMsg
	 * @return boolean
	 */
	public boolean deleteApplHistory(String applNo, String applId, List<String> saveTableList, Date lastUpdateDate,
			Map<String, String> errorMsg) {
		// 排他処理（行ロック）
		Skf2010Sc003GetApplHistoryStatusInfoForUpdateExp updateData = new Skf2010Sc003GetApplHistoryStatusInfoForUpdateExp();
		Skf2010Sc003GetApplHistoryStatusInfoForUpdateExpParameter param = new Skf2010Sc003GetApplHistoryStatusInfoForUpdateExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		param.setApplId(applId);
		updateData = skf2010Sc003GetApplHistoryStatusInfoForUpdateExpRepository
				.getApplHistoryStatusInfoForUpdate(param);
		if (updateData == null) {
			// データが無かった場合、事前に別ユーザーが削除しているので排他チェックエラーとする
			errorMsg.put("error", MessageIdConstant.E_SKF_1134);
			return false;
		} else {
			// 排他チェック
			if (!CheckUtils.isEqual(updateData.getUpdateDate(), lastUpdateDate)) {
				errorMsg.put("error", MessageIdConstant.E_SKF_1134);
				return false;
			}
			// 申請書類が社宅入居希望等調書だった場合、備品希望申請の申請管理番号を取得
			String bihinApplNo = null;
			if (CheckUtils.isEqual(applId, FunctionIdConstant.R0100)) {
				bihinApplNo = this.getApplNoByBihinKiboShinsei(applNo);

			}
			// 指定された件数分帳票テーブルを削除
			int delCount = 0;
			for (String saveTableName : saveTableList) {
				Skf2010Sc003DeleteDocTableExpParameter saveTableParam = new Skf2010Sc003DeleteDocTableExpParameter();
				saveTableParam.setDocTableName(saveTableName);
				saveTableParam.setCompanyCd(companyCd);
				saveTableParam.setApplNo(applNo);
				int sTRes = skf2010Sc003DeleteDocTableExpRepository.deleteDocTable(saveTableParam);
				if (sTRes > 1) {
					delCount++;
				}
			}
			// 申請書類履歴テーブルを削除
			Skf2010Sc003DeleteApplHistoryExpParameter ahParam = new Skf2010Sc003DeleteApplHistoryExpParameter();
			ahParam.setCompanyCd(companyCd);
			ahParam.setApplId(applId);
			ahParam.setApplNo(applNo);
			int ahRes = skf2010Sc003DeleteApplHistoryExpRepository.deleteApplHistory(ahParam);
			if (ahRes > 0) {
				delCount++;
			}
			if (NfwStringUtils.isNotEmpty(bihinApplNo)) {
				// 備品希望申請があった場合は申請書類履歴を削除
				Skf2010Sc003DeleteApplHistoryExpParameter bihinParam = new Skf2010Sc003DeleteApplHistoryExpParameter();
				bihinParam.setCompanyCd(companyCd);
				bihinParam.setApplId(FunctionIdConstant.R0104);
				bihinParam.setApplNo(bihinApplNo);
				int bihinRes = skf2010Sc003DeleteApplHistoryExpRepository.deleteApplHistory(bihinParam);
				if (bihinRes > 0) {
					delCount++;
				}
			}
			
			// 対象の申請書類IDがR0107（モバイルルーター借用希望申請書）、R0108（モバイルルーター返却申請書）の場合
			if (CheckUtils.isEqual(applId, FunctionIdConstant.R0107) ||
					CheckUtils.isEqual(applId, FunctionIdConstant.R0108)) {
				//貸出予定データから書類管理IDが紐づくデータの削除
				deleteRouterLendingYotei(applId, applNo);
			}

			if (delCount <= 0) {
				return false;
			}
		}
		return true;
	}

	private boolean checkAdminRole(String roleId) {
		
		boolean res = skfLoginUserInfoUtils.isAgreeAuthority(companyCd, null, roleId, CodeConstant.NONE);
		if(res){
			return true;
		}

		return false;
	}

	/**
	 * 社宅連携処理を実施する
	 * 
	 * @param menuScopeSessionBean
	 * @param applNo
	 * @param applStatus
	 * @param applId
	 * @param pageId
	 * @param shainNo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> doShatakuRenkei(MenuScopeSessionBean menuScopeSessionBean, String applNo, String applStatus,
			String applId, String pageId, String shainNo) {
		// ログインユーザー情報取得
		Map<String, String> loginUserInfoMap = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		String userId = loginUserInfoMap.get("userCd");
		// String shainNo = loginUserInfoMap.get("shainNo");
		// 排他チェック用データ取得
		Map<String, Object> forUpdateObject = (Map<String, Object>) menuScopeSessionBean
				.get(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2010SC003);

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
		case FunctionIdConstant.R0104:
			Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> forUpdateMapR0104 = skf2030Fc001BihinKiboShinseiDataImport
					.forUpdateMapDownCaster(forUpdateObject);
			skf2030Fc001BihinKiboShinseiDataImport.setUpdateDateForUpdateSQL(forUpdateMapR0104);

			// 連携処理開始
			resultBatch = skf2030Fc001BihinKiboShinseiDataImport.doProc(companyCd, shainNo, applNo, applStatus, userId,
					pageId);
			break;
		case FunctionIdConstant.R0105:
			Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> forUpdateMapR0105 = skf2050Fc001BihinHenkyakuSinseiDataImport
					.forUpdateMapDownCaster(forUpdateObject);
			skf2050Fc001BihinHenkyakuSinseiDataImport.setUpdateDateForUpdateSQL(forUpdateMapR0105);

			// 連携処理開始
			resultBatch = skf2050Fc001BihinHenkyakuSinseiDataImport.doProc(companyCd, shainNo, applNo, applStatus,
					userId, pageId);
			break;
		case FunctionIdConstant.R0107:
		case FunctionIdConstant.R0108:
			// 正常
			resultBatch = null;
		default:
			break;
		}

		return resultBatch;
	}

	/**
	 * 申請状況を取得する
	 * 
	 * @param applNo
	 * @return applStatus
	 */
	public String getApplStatus(String applNo) {
		String afterApplStatus = CodeConstant.NONE;
		List<SkfApplHistoryInfoUtilsGetApplHistoryInfoExp> tApplHistoryList = new ArrayList<SkfApplHistoryInfoUtilsGetApplHistoryInfoExp>();
		tApplHistoryList = skfApplHistoryInfoUtils.getApplHistoryInfo(companyCd, applNo);
		if (tApplHistoryList.size() > 0) {
			SkfApplHistoryInfoUtilsGetApplHistoryInfoExp tApplHistory = tApplHistoryList.get(0);
			afterApplStatus = tApplHistory.getApplStatus();
		}
		return afterApplStatus;
	}

	/**
	 * 備品希望申請情報を取得する
	 * 
	 * @param applNo
	 * @return
	 */
	public String getApplNoByBihinKiboShinsei(String applNo) {
		SkfBihinInfoUtilsGetBihinShinseiInfoExp bihinKibo = new SkfBihinInfoUtilsGetBihinShinseiInfoExp();
		List<SkfBihinInfoUtilsGetBihinShinseiInfoExp> bihinKiboList = new ArrayList<SkfBihinInfoUtilsGetBihinShinseiInfoExp>();
		bihinKiboList = skfBihinInfoUtils.getBihinShinseiInfoByNyukyoApplNo(companyCd, applNo);
		if (bihinKiboList != null && bihinKiboList.size() > 0) {
			bihinKibo = bihinKiboList.get(0);
			return bihinKibo.getApplNo();
		}

		return null;
	}
	
	
	// モバイルルーター機能追加対応 2021/9 add start
	/**
	 * モバイルルーター貸出予定データテーブルを更新する
	 * 
	 * @param shainNo 社員番号
	 * @param appId 申請書類ID
	 * @param applNo 申請書類管理番号
	 * @return 更新成否
	 */
	
	public int updateRouterLendingYotei(String shainNo,String applId, String applNo,String userId){
		String taiyoHenkyakuKbn = CodeConstant.DOUBLE_QUOTATION;
		// 貸与返却区分設定
		if(FunctionIdConstant.R0107.equals(applId)){
			// 貸与:1
			taiyoHenkyakuKbn = CodeConstant.TAIYO_HENKYAKU_KBN_TAIYO;
		}else{
			// 返却:2
			taiyoHenkyakuKbn = CodeConstant.TAIYO_HENKYAKU_KBN_HENKYAKU;
		}
		
		//更新値設定
		Skf2010Sc003UpdateRouterLendingYoteiDataExpParameter updateData = new  Skf2010Sc003UpdateRouterLendingYoteiDataExpParameter();
		updateData.setShainNo(shainNo);
		updateData.setApplNo(applNo);
		updateData.setTaiyoHenkyakuKbn(taiyoHenkyakuKbn);
		updateData.setRouterApplStatus(CodeConstant.STATUS_ICHIJIHOZON);// ステータス:一時保存
		updateData.setMobileRouterKanriId(null);
		updateData.setMobileRouterNo(null);
		updateData.setRouterLendJokyo(null);
		
		
		int updCount = skf2010Sc003UpdateRouterLendingYoteiDataExpRepository.updateRouterLendingYoteiData(updateData);
		
		return updCount;
	}
	
	/**
	 * モバイルルーター貸出予定データを取得する
	 * ・借用希望申請の場合、書類管理番号が紐づくモバイルルーター貸出予定データ取得
	 *　・・モバイルルーター通しNoが付与されている場合は、モバイルルーターマスタを更新
	 * ・書類管理番号が紐づくモバイルルーター貸出予定データ削除
	 * 
	 * @param shainNo 社員番号
	 * @param appId 申請書類ID
	 * @param applNo 申請書類管理番号
	 * @return 削除件数
	 */
	private int deleteRouterLendingYotei(String applId, String applNo){
		int result = 0;
		String taiyoHenkyakuKbn = CodeConstant.DOUBLE_QUOTATION;
		// 貸与返却区分設定
		if(FunctionIdConstant.R0107.equals(applId)){
			// 貸与:1
			taiyoHenkyakuKbn = CodeConstant.TAIYO_HENKYAKU_KBN_TAIYO;

			//モバイルルーター貸出予定データ取得
			Skf2010Sc003GetRouterLendingYoteiDataExpParameter param = new Skf2010Sc003GetRouterLendingYoteiDataExpParameter();
			param.setApplNo(applNo);
			param.setTaiyoHenkyakuKbn(taiyoHenkyakuKbn);
			
			List<Skf2010Sc003GetRouterLendingYoteiDataExp> dataList = skf2010Sc003GetRouterLendingYoteiDataExpRepository.getRouterLendingYoteiData(param);
			
			for(Skf2010Sc003GetRouterLendingYoteiDataExp data : dataList){
				//モバイルルーター通しNoを取得
				Long routerNo = data.getMobileRouterNo();
				String generalEquipmentCd = data.getGeneralEquipmentCd();
				
				if(Objects.nonNull(routerNo)){
					//「モバイルルーター通しNo」が設定されている場合、モバイルルーターマスタ情報の貸出可否判定を「貸与可」に更新する
					Skf2100MMobileRouterWithBLOBs updParam = new Skf2100MMobileRouterWithBLOBs();
					updParam.setGeneralEquipmentCd(generalEquipmentCd);
					updParam.setMobileRouterNo(routerNo);
					updParam.setRouterLendingJudgment(CodeConstant.ROUTER_LENDING_JUDGMENT_TAIYO);//貸与可
					skf2100MMobileRouterRepository.updateByPrimaryKeySelective(updParam);
				}
				
				//モバイルルーター管理簿IDを取得
				Long kanriId = data.getMobileRouterKanriId();
				if(Objects.nonNull(kanriId)){
					//モバイルルーター管理簿IDが設定されている場合、管理簿データ削除
					skf2100TMobileRouterLedgerRepository.deleteByPrimaryKey(kanriId);
				}
			}
		}
		else{
			// 返却:2
			taiyoHenkyakuKbn = CodeConstant.TAIYO_HENKYAKU_KBN_HENKYAKU;
		}
		
		//モバイルルーター貸出予定データ削除
		Skf2010Sc003DeleteRouterLendingYoteiDataExpParameter delParam = new Skf2010Sc003DeleteRouterLendingYoteiDataExpParameter();
		delParam.setApplNo(applNo);
		delParam.setTaiyoHenkyakuKbn(taiyoHenkyakuKbn);
		result = skf2010Sc003DeleteRouterLendingYoteiDataExpRepository.deleteRouterLendingYoteiData(delParam);
		
		
		return result;
	}
	// モバイルルーター機能追加対応 2021/9 add end
}
