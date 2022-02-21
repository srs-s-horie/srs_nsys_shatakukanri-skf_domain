/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc006;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.PropertyUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.utils.filetransfer.FileOutput;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc006.Skf2100Sc006OperationGuideDownloadDto;

/**
 * Skf2100Sc006OperationGuideDownloadService 「運用ガイド」押下時のサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 *
 */

@Service
public class Skf2100Sc006OperationGuideDownloadService extends SkfServiceAbstract<Skf2100Sc006OperationGuideDownloadDto> {

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
	protected BaseDto index(Skf2100Sc006OperationGuideDownloadDto dto) throws Exception {

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("運用ガイド", CodeConstant.C001, FunctionIdConstant.SKF2100_SC006);
		// デバッグログ
		LogUtils.debugByMsg("運用ガイド");
		// ファイル出力処理
		FileOutput.fileDownloadPublic("skf/template/skf2100/skf2100mn006/",
				PropertyUtils.getValue("skf2100.skf2100_sc006.operationGuideFile"), dto);

		return dto;
	}
}
