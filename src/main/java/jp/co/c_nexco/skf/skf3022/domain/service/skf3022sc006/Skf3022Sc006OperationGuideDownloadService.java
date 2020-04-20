/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc006;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.PropertyUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.utils.filetransfer.FileOutput;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc006.Skf3022Sc006OperationGuideDownloadDto;

/**
 * Skf3022Sc006OperationGuideDownloadService 提示データ登録画面：「運用ガイド」押下時のサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 *
 */

@Service
public class Skf3022Sc006OperationGuideDownloadService extends SkfServiceAbstract<Skf3022Sc006OperationGuideDownloadDto> {

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

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("運用ガイド", CodeConstant.C001, FunctionIdConstant.SKF3022_SC006);
		// デバッグログ
		LogUtils.debugByMsg("運用ガイド");
		// ファイル出力処理
//		SkfFileOutputUtils.fileOutputPdf("skf3022.skf3022_sc006.operationGuideFile", "SKF3022MN006", dto);
//		FileOutput.fileDownloadPublic("skf/template/skf3022/skf3022mn006/", "運用ガイド.zip", dto);
		FileOutput.fileDownloadPublic("skf/template/skf3022/skf3022mn006/",
				PropertyUtils.getValue("skf3022.skf3022_sc006.operationGuideFile"), dto);
//		LogUtils.debugByMsg("運用ガイドダウンロード終了");
		return dto;
	}
}
