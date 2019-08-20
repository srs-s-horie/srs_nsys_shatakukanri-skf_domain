/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc009;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc009.Skf2010Sc009DeleteDto;

/**
 * Skf2010Sc009 添付資料入力支援削除処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc009DeleteService extends BaseServiceAbstract<Skf2010Sc009DeleteDto> {

	@Autowired
	private Skf2010Sc009SharedService skf2010Sc009SharedService;
	@Autowired
	private MenuScopeSessionBean menuScopeSessionBean;

	private String sessionKey = SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY;
	
	@Value("${skf2010.skf2010_sc009.max_search_count}")
	private String maxSearchCount;
	@Value("${skf2010.skf2010_sc009.max_file_size}")
	private String maxFileSize;
	@Value("${skf.common.validate_error}")
	private String validationErrorCode;

	/**
	 * サービス処理を行う。
	 * 
	 * @param delDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public Skf2010Sc009DeleteDto index(Skf2010Sc009DeleteDto delDto) throws Exception {

		delDto.setPageTitleKey(MessageIdConstant.SKF2010_SC009_TITLE);

		String applId = delDto.getApplId();
		String applName = getBaseScreenName(applId);
		delDto.setApplName(applName);

		String attachedNo = delDto.getAttachedNo();

		List<Map<String, Object>> attachedFileList = skf2010Sc009SharedService.getAttachedFileInfo(sessionKey);
		attachedFileList = deleteAttachedFileInfoList(attachedFileList, attachedNo);

		// グリッドビューのバインド
		delDto.setAttachedFileList(skf2010Sc009SharedService.createListTableData(attachedFileList));

		return delDto;
	}

	private List<Map<String, Object>> deleteAttachedFileInfoList(List<Map<String, Object>> attachedFileList,
			String attachedNo) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		if (attachedFileList != null && attachedFileList.size() > 0) {
			int newAttachedNo = 0;
			for (Map<String, Object> attachedFileDataMap : attachedFileList) {
				Map<String, Object> newAttachedFileData = new HashMap<String, Object>();
				if (attachedNo.equals(attachedFileDataMap.get("attachedNo").toString())) {
					// 削除対象は除外
					continue;
				} else {
					// 添付ファイル情報を全渡し
					newAttachedFileData.putAll(attachedFileDataMap);
					// 添付ファイルの数が減っているため添付番号だけを更新する
					newAttachedFileData.put("attachedNo", newAttachedNo);

					resultList.add(newAttachedFileData);
				}

			}
		}

		menuScopeSessionBean.put(sessionKey, resultList);

		return resultList;

	}

	/**
	 * 申請書類名を取得します
	 * 
	 * @param applId
	 * @return String
	 */
	private String getBaseScreenName(String applId) {
		String applName = "";

		if (applId == null || CheckUtils.isEmpty(applId)) {
			return "";
		}
		applName = skf2010Sc009SharedService.getApplName(applId);

		return applName;
	}

}
