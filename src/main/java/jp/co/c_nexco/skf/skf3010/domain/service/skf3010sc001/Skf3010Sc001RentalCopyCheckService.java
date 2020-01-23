/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc001;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc001.Skf3010Sc001RentalCopyCheckDto;

/**
 * Skf3010Sc001RentalCopyCheckService 社宅一覧の借上(複写)ボタン処理クラス。
 * 関連チェックを実施し、
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3010Sc001RentalCopyCheckService extends BaseServiceAbstract<Skf3010Sc001RentalCopyCheckDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	// 社宅区分
	private static final String SHATAKU_KBN_KARIAGE = "2";

	@Override
	public BaseDto index(Skf3010Sc001RentalCopyCheckDto rentalCopyDto) throws Exception {
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("借上(複写)", CodeConstant.C001, FunctionIdConstant.SKF3010_SC001);
		
		// デバッグログ
		LogUtils.debugByMsg("借上(複写)ボタン押下");
		// 社宅区分(選択行)
		String hdnRowShatakuKbn = rentalCopyDto.getHdnRowShatakuKbn();
		// 社宅管理番号(選択行)
		String hdnRowShatakuKanriNo = rentalCopyDto.getHdnRowShatakuKanriNo();

		// 社宅管理番号の有無チェック
		if (Objects.equals(hdnRowShatakuKanriNo, null) || hdnRowShatakuKanriNo.length() < 1) {
			LogUtils.debugByMsg("社宅が選択されていません。");
			ServiceHelper.addErrorResultMessage(rentalCopyDto, null, MessageIdConstant.W_SKF_3003);
			return rentalCopyDto;
		}
		// 社宅区分判定
		if (!SHATAKU_KBN_KARIAGE.equals(hdnRowShatakuKbn)) {
			LogUtils.debugByMsg("借上以外が選択されています。：" + rentalCopyDto.getHdnRowShatakuName());
			ServiceHelper.addErrorResultMessage(rentalCopyDto, null, MessageIdConstant.W_SKF_3002, rentalCopyDto.getHdnRowShatakuName());
			return rentalCopyDto;
		}

		// 画面遷移（借上社宅登録画面(複写)へ）
		TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF3010_SC006, "init");
		rentalCopyDto.setTransferPageInfo(nextPage);

		return rentalCopyDto;
	}
}
