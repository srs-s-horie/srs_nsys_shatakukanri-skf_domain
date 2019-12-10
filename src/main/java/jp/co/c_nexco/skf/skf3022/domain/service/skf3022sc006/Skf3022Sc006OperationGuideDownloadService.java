/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc006;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc006.Skf3022Sc006OperationGuideDownloadDto;

/**
 * Skf3022Sc006OperationGuideDownloadService 提示データ登録画面：「運用ガイド」押下時のサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 *
 */

@Service
public class Skf3022Sc006OperationGuideDownloadService extends BaseServiceAbstract<Skf3022Sc006OperationGuideDownloadDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	protected BaseDto index(Skf3022Sc006OperationGuideDownloadDto dto) throws Exception {

		// デバッグログ
		LogUtils.debugByMsg("運用ガイドダウンロード");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("運用ガイドダウンロード", CodeConstant.C001, dto.getPageId());
		// ファイル出力処理
		SkfFileOutputUtils.fileOutputPdf("skf3022.skf3022_sc006.operationGuideFile", "SKF3022MN006", dto);

		return dto;
	}
}
