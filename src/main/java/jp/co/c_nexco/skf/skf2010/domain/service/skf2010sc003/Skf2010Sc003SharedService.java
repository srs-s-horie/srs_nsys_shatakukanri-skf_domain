package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc003;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc003.Skf2010Sc003DeleteApplHistoryExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc003.Skf2010Sc003DeleteDocTableExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc003.Skf2010Sc003GetApplHistoryStatusInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc003.Skf2010Sc003GetApplHistoryStatusInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc003.Skf2010Sc003GetApplHistoryStatusInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc003.Skf2010Sc003GetApplHistoryStatusInfoForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc003.Skf2010Sc003UpdateApplHistoryAgreeStatusExp;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc003.Skf2010Sc003DeleteApplHistoryExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc003.Skf2010Sc003DeleteDocTableExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc003.Skf2010Sc003GetApplHistoryStatusInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc003.Skf2010Sc003GetApplHistoryStatusInfoForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc003.Skf2010Sc003UpdateApplHistoryAgreeStatusExpRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;

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
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;

	private String companyCd = CodeConstant.C001;

	private String datePattern = "yyyy/MM/dd";

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
			param.setAgreDateFrom(agreDateTo);
		}
		if (!CheckUtils.isEmpty(applName)) {
			param.setApplName(applName);
		}
		if (applStatus != null) {
			param.setApplStatus(applStatus);
		}
		resultList = skf2010Sc003GetApplHistoryStatusInfoExpRepository.getApplHistoryStatusInfo(param);

		return resultList;
	}

	/**
	 * 申請情報からリストテーブルのデータを作成します
	 * 
	 * @param applHistoryList
	 * @return
	 */
	public List<Map<String, Object>> createListTable(List<Skf2010Sc003GetApplHistoryStatusInfoExp> applHistoryList) {
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		if (applHistoryList == null || applHistoryList.size() <= 0) {
			return returnList;
		}

		// 汎用コード取得
		Map<String, String> genericCodeMap = new HashMap<String, String>();
		genericCodeMap = skfGenericCodeUtils.getGenericCode("SKF1001");

		for (Skf2010Sc003GetApplHistoryStatusInfoExp applHistoryData : applHistoryList) {
			String applDate = "";
			String agreDate = "";
			String applStatus = "";

			// 日付型を文字列型に変更する
			if (applHistoryData.getApplDate() != null) {
				applDate = skfDateFormatUtils.dateFormatFromDate(applHistoryData.getApplDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			}
			if (applHistoryData.getAgreDate() != null) {
				agreDate = skfDateFormatUtils.dateFormatFromString(applHistoryData.getAgreDate(), datePattern);
			}

			// 申請状況をコードから汎用コードに変更
			if (applHistoryData.getApplStatus() != null) {
				applStatus = genericCodeMap.get(applHistoryData.getApplStatus());
			}

			Map<String, Object> tmpMap = new HashMap<String, Object>();
			tmpMap.put("detail", ""); // 表示（アイコン）
			tmpMap.put("applId", applHistoryData.getApplId()); // 申請書ID
			tmpMap.put("applStatus", applHistoryData.getApplStatus()); // 申請状況
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
				tmpMap.put("delete", ""); // 削除ボタン（アイコン）
			}

			returnList.add(tmpMap);
		}
		return returnList;
	}

	/**
	 * 申請履歴を一時保存に変更します
	 * 
	 * @param applNo
	 * @return
	 */
	public boolean updateApplHistoryCancel(String applNo, String applId) throws Exception {
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

	public boolean deleteApplHistory(String applNo, String applId, List<String> saveTableList) {
		// 排他処理（行ロック）
		Skf2010Sc003GetApplHistoryStatusInfoForUpdateExp updateData = new Skf2010Sc003GetApplHistoryStatusInfoForUpdateExp();
		Skf2010Sc003GetApplHistoryStatusInfoForUpdateExpParameter param = new Skf2010Sc003GetApplHistoryStatusInfoForUpdateExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		param.setApplId(applId);
		updateData = skf2010Sc003GetApplHistoryStatusInfoForUpdateExpRepository
				.getApplHistoryStatusInfoForUpdate(param);
		if (updateData != null) {
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
			if (delCount <= 0) {
				return false;
			}
		}
		return true;
	}
}
