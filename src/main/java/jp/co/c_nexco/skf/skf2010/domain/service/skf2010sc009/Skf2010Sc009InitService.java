/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc009;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc009.Skf2010Sc009InitDto;

/**
 * Skf2010Sc009 添付資料入力支援初期表示処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc009InitService extends BaseServiceAbstract<Skf2010Sc009InitDto> {

	@Autowired
	private Skf2010Sc009SharedService skf2010Sc009SharedService;

	private String sessionKey = SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY;

	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2010Sc009InitDto index(Skf2010Sc009InitDto initDto) throws Exception {

		initDto.setPageTitleKey(MessageIdConstant.SKF2010_SC009_TITLE);

		String applId = initDto.getApplId();
		String candidateNo = initDto.getCandidateNo();

		String applName = getBaseScreenName(applId);
		initDto.setApplName(applName);

		Map<String, String> attachedInfo = new HashMap<String, String>();
		attachedInfo.put("applId", applId);
		attachedInfo.put("candidateNo", candidateNo);

		String sessionKeyString = sessionKey;

		if (CheckUtils.isEqual(applId, FunctionIdConstant.R0106)) {
			sessionKeyString = SessionCacheKeyConstant.KARIAGE_ATTACHED_FILE_SESSION_KEY + candidateNo;
		}

		// 添付資料情報の取得
		List<Map<String, Object>> attachedFileList = skf2010Sc009SharedService.getAttachedFileInfo(sessionKeyString);

		if (attachedFileList.size() == 0) {
			attachedFileList = skf2010Sc009SharedService.getAttachedFileListByTable(sessionKeyString, applId,
					attachedInfo);
		}

		// グリッドビューのバインド
		initDto.setAttachedFileList(skf2010Sc009SharedService.createListTableData(attachedFileList));

		return initDto;
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
