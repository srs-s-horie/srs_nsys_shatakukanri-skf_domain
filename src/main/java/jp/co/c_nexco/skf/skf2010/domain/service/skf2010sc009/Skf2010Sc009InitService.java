/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc009;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc009.Skf2010Sc009InitDto;

/**
 * Skf2010Sc009 添付資料入力支援初期表示処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc009InitService extends SkfServiceAbstract<Skf2010Sc009InitDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
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

		// 操作ログ出力
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, FunctionIdConstant.SKF2010_SC009);

		String applId = initDto.getPopApplId(); // 申請書類ID
		String candidateNo = initDto.getPopCandidateNo(); // 借上候補物件番号

		// 申請書類名取得
		String applName = skf2010Sc009SharedService.getBaseScreenName(applId);
		initDto.setPopApplName(applName);

		// 共通添付ファイルセッションキーを取得
		String sessionKeyString = sessionKey;

		// 申請書類IDがR0106（借上候補物件確認）だった場合
		if (CheckUtils.isEqual(applId, FunctionIdConstant.R0106)) {
			// 借上候補物件専用セッションキー＋借上候補物件番号で専用のセッションキーを作成する
			sessionKeyString = SessionCacheKeyConstant.KARIAGE_ATTACHED_FILE_SESSION_KEY + candidateNo;
		}

		// 添付資料情報の取得
		List<Map<String, Object>> attachedFileList = skf2010Sc009SharedService.getAttachedFileInfo(sessionKeyString);

		if (attachedFileList.size() == 0) {
			attachedFileList = skf2010Sc009SharedService.getAttachedFileListByTable(sessionKeyString, applId,
					candidateNo);
		}

		// グリッドビューのバインド
		initDto.setPopAttachedFileList(skf2010Sc009SharedService.createListTableData(attachedFileList));

		return initDto;
	}

}
