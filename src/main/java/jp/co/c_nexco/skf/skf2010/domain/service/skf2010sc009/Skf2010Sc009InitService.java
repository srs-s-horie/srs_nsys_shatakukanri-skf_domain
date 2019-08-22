/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc009;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
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
	 * @param initDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public Skf2010Sc009InitDto index(Skf2010Sc009InitDto initDto) throws Exception {

		initDto.setPageTitleKey(MessageIdConstant.SKF2010_SC009_TITLE);

		String applName = getBaseScreenName(initDto.getApplId());
		initDto.setApplName(applName);

		// 添付資料情報の取得
		List<Map<String, Object>> attachedFileList = skf2010Sc009SharedService.getAttachedFileInfo(sessionKey);

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
