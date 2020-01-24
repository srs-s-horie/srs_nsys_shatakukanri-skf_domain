package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc009;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc009.Skf2010Sc009GetApplInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc009.Skf2010Sc009GetApplInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfAttachedFileUtils.SkfAttachedFileUtilsGetKariageBukkenFileInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfAttachedFileUtils.SkfAttachedFileUtilsGetKariageBukkenFileInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc009.Skf2010Sc009GetApplInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfAttachedFileUtils.SkfAttachedFileUtilsGetKariageBukkenFileInfoExpRepository;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;

@Service
public class Skf2010Sc009SharedService {

	@Autowired
	private Skf2010Sc009GetApplInfoExpRepository skf2010Sc009GetApplInfoExpRepository;
	@Autowired
	private SkfAttachedFileUtilsGetKariageBukkenFileInfoExpRepository skfAttachedFileUtilsGetKariageBukkenFileInfoExpRepository;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfAttachedFileUtils skfAttachedFileUtiles;
	@Autowired
	private MenuScopeSessionBean menuScopeSessionBean;

	private String companyCd = CodeConstant.C001;

	public String getBaseScreenName(String applId) {
		String applName = CodeConstant.NONE;

		if (NfwStringUtils.isNotEmpty(applId)) {
			applName = this.getApplName(applId);
		}
		return applName;
	}

	/**
	 * 申請書名を取得します
	 * 
	 * @param applId
	 * @return String
	 */
	private String getApplName(String applId) {
		String applName = "";

		Skf2010Sc009GetApplInfoExp applInfo = new Skf2010Sc009GetApplInfoExp();
		Skf2010Sc009GetApplInfoExpParameter param = new Skf2010Sc009GetApplInfoExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplId(applId);
		applInfo = skf2010Sc009GetApplInfoExpRepository.getApplInfo(param);
		if (applInfo != null && applInfo.getApplName() != null) {
			applName = applInfo.getApplName();
		}

		return applName;
	}

	/**
	 * 添付ファイルをセッションから取得します
	 * 
	 * @param attachedInfo
	 * 
	 * @param applNo
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getAttachedFileInfo(String sessionKey) {
		List<Map<String, Object>> resultAttachedFileList = null;

		resultAttachedFileList = (List<Map<String, Object>>) menuScopeSessionBean.get(sessionKey);
		if (resultAttachedFileList != null) {
			return resultAttachedFileList;
		}

		// 初期化
		resultAttachedFileList = new ArrayList<Map<String, Object>>();
		return resultAttachedFileList;
	}

	/**
	 * テーブルから添付資料情報を取得する（現状は借上候補物件のみ）
	 * 
	 * @param sessionKey
	 * @param applId
	 * @param dto
	 * @return
	 */
	public List<Map<String, Object>> getAttachedFileListByTable(String sessionKey, String applId, String candidateNo) {
		List<Map<String, Object>> resultAttachedFileList = new ArrayList<Map<String, Object>>();
		if (CheckUtils.isEqual(applId, FunctionIdConstant.R0106)) {
			// 借上候補物件の時のみテーブルから読み込む
			List<SkfAttachedFileUtilsGetKariageBukkenFileInfoExp> fileInfoList = new ArrayList<SkfAttachedFileUtilsGetKariageBukkenFileInfoExp>();
			fileInfoList = getKariageBukkenFileList(candidateNo);
			if (fileInfoList != null && fileInfoList.size() > 0) {
				for (SkfAttachedFileUtilsGetKariageBukkenFileInfoExp fileInfo : fileInfoList) {
					Map<String, Object> attachedFileMap = new HashMap<String, Object>();
					int attachedNo = resultAttachedFileList.size();
					attachedFileMap = new HashMap<String, Object>();

					// ファイルタイプ
					String fileType = skfAttachedFileUtiles.getFileTypeInfo(fileInfo.getAttachedName());
					setAttachedFileMap(attachedFileMap, attachedNo, fileInfo.getAttachedName(),
							fileInfo.getFileStream(), fileInfo.getFileSize(), fileType, fileInfo.getInsertDate());

					resultAttachedFileList.add(attachedFileMap);
				}

				// セッションにデータを保存
				menuScopeSessionBean.put(sessionKey, resultAttachedFileList);
			}
		}

		return resultAttachedFileList;
	}

	/**
	 * 借上候補物件の添付資料情報を取得する
	 * 
	 * @param candidateNo
	 * @return
	 */
	private List<SkfAttachedFileUtilsGetKariageBukkenFileInfoExp> getKariageBukkenFileList(String candidateNo) {
		List<SkfAttachedFileUtilsGetKariageBukkenFileInfoExp> fileList = new ArrayList<SkfAttachedFileUtilsGetKariageBukkenFileInfoExp>();
		SkfAttachedFileUtilsGetKariageBukkenFileInfoExpParameter param = new SkfAttachedFileUtilsGetKariageBukkenFileInfoExpParameter();
		param.setCompanyCd(companyCd);
		param.setCandidateNo(Long.parseLong(candidateNo));
		fileList = skfAttachedFileUtilsGetKariageBukkenFileInfoExpRepository.getKariageBukkenFileInfo(param);
		return fileList;
	}

	/**
	 * 添付ファイル情報をセッションに保存します
	 * 
	 * @param sessionKey
	 * @param applNo
	 * @param fileName
	 * @param fileStream
	 * @param fileSize
	 * @param fileType
	 * @return
	 */
	public List<Map<String, Object>> setAttachedFileList(String sessionKey, String applNo, String fileName,
			byte[] fileStream, String fileSize, String fileType) {
		List<Map<String, Object>> attachedFileList = getAttachedFileInfo(sessionKey);
		if (attachedFileList == null) {
			attachedFileList = new ArrayList<Map<String, Object>>();
		}
		Map<String, Object> attachedFileMap = new HashMap<String, Object>();
		int attachedNo = attachedFileList.size();
		attachedFileMap = new HashMap<String, Object>();
		// 添付資料番号
		attachedFileMap.put("attachedNo", String.valueOf(attachedNo));
		// 添付資料名
		attachedFileMap.put("attachedName", fileName);
		// ファイルサイズ
		attachedFileMap.put("fileSize", fileSize);
		// 更新日
		String applDate = skfDateFormatUtils.dateFormatFromDate(new Date(), "yyyy/MM/dd HH:mm:ss.SS");
		attachedFileMap.put("applDate", applDate);
		// 添付資料
		attachedFileMap.put("fileStream", fileStream);
		// ファイルタイプ
		attachedFileMap.put("fileType", fileType);

		attachedFileList.add(attachedFileMap);

		// セッションにデータを保存
		menuScopeSessionBean.put(sessionKey, attachedFileList);

		return attachedFileList;
	}

	private void setAttachedFileMap(Map<String, Object> fileInfoMap, int attachedNo, String fileName, byte[] fileStream,
			String fileSize, String fileType, Date updateDate) {
		// 添付資料番号
		fileInfoMap.put("attachedNo", String.valueOf(attachedNo));
		// 添付資料名
		fileInfoMap.put("attachedName", fileName);
		// ファイルサイズ
		fileInfoMap.put("fileSize", fileSize);
		// 更新日
		String applDate = skfDateFormatUtils.dateFormatFromDate(updateDate, "yyyy/MM/dd HH:mm:ss.SS");
		fileInfoMap.put("applDate", applDate);
		// 添付資料
		fileInfoMap.put("fileStream", fileStream);
		// ファイルタイプ
		fileInfoMap.put("fileType", fileType);

		return;
	}

	/**
	 * リストテーブルマッピング
	 * 
	 * @param attachedFileList
	 * @return
	 */
	public List<Map<String, Object>> createListTableData(List<Map<String, Object>> attachedFileList) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		if (attachedFileList == null || attachedFileList.size() <= 0) {
			return resultList;
		}
		for (Map<String, Object> attachedFileMap : attachedFileList) {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("attachedNo", attachedFileMap.get("attachedNo"));
			resultMap.put("attachedName", attachedFileMap.get("attachedName"));
			resultMap.put("fileSize", attachedFileMap.get("fileSize"));
			resultMap.put("applDate", attachedFileMap.get("applDate"));
			resultMap.put("view", "");
			resultMap.put("delete", "");

			resultList.add(resultMap);
		}

		return resultList;
	}

}
