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
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.skf.common.SkfAsyncServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc009.Skf2010Sc009DeleteAsyncDto;

/**
 * Skf2010Sc009 添付資料入力支援削除処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc009DeleteAsyncService extends SkfAsyncServiceAbstract<Skf2010Sc009DeleteAsyncDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf2010Sc009SharedService skf2010Sc009SharedService;
	@Autowired
	private MenuScopeSessionBean menuScopeSessionBean;

	private String sessionKey;

	@Value("${skf2010.skf2010_sc009.max_search_count}")
	private String maxSearchCount;
	@Value("${skf2010.skf2010_sc009.max_file_size}")
	private String maxFileSize;
	@Value("${skf.common.validate_error}")
	private String validationErrorCode;

	/**
	 * サービス処理を行う。
	 * 
	 * @param delDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public AsyncBaseDto index(Skf2010Sc009DeleteAsyncDto delDto) throws Exception {

		// 操作ログ出力
		skfOperationLogUtils.setAccessLog("添付資料削除処理", CodeConstant.C001, FunctionIdConstant.SKF2010_SC009);

		String applId = delDto.getPopApplId();
		String attachedNo = delDto.getPopAttachedNo();
		String candidateNo = delDto.getPopCandidateNo();

		// 申請書類名取得
		String applName = skf2010Sc009SharedService.getBaseScreenName(applId);
		delDto.setPopApplName(applName);

		// 申請書類が借上候補物件の場合、専用のセッションキーに切り替える
		sessionKey = SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY;
		if (CheckUtils.isEqual(applId, FunctionIdConstant.R0106)) {
			sessionKey = SessionCacheKeyConstant.KARIAGE_ATTACHED_FILE_SESSION_KEY + candidateNo;
		}

		// 添付資料の削除処理
		List<Map<String, Object>> attachedFileList = skf2010Sc009SharedService.getAttachedFileInfo(sessionKey);
		attachedFileList = deleteAttachedFileInfoList(attachedFileList, attachedNo);

		// グリッドビューのバインド
		delDto.setPopAttachedFileList(skf2010Sc009SharedService.createListTableData(attachedFileList));
		// 添付ファイル情報は不要なのでNULL
		delDto.setAttachedFile(null);

		return delDto;
	}

	/**
	 * 添付ファイル一覧から該当ファイルを削除します
	 * 
	 * @param attachedFileList
	 * @param attachedNo
	 * @return
	 */
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
					// 添付ファイル番号を１進める
					newAttachedNo++;
				}

			}
		}
		// セッション情報置き換え
		menuScopeSessionBean.put(sessionKey, resultList);

		return resultList;

	}

}
