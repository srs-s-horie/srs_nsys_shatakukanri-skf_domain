/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc001;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.SkfShinseiUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc001.Skf3010Sc001OutContractDto;
import jp.co.intra_mart.common.platform.log.Logger;

/**
 * Skf3010Sc001OutContractService 社宅一覧の契約情報出力ボタン処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3010Sc001OutContractService extends BaseServiceAbstract<Skf3010Sc001OutContractDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	/**
	 * 中身は現状まったくの別物である。
	 * 本クラスは将来的に契約情報出力処理を実装するが
	 * 現状はJSPからのパラメータ受け渡しとページ遷移
	 * バリデーション、関連チェックの確認と結果返却の実装テストをしたものである。
	 */
	// 社宅区分
	private static final String SHATAKU_KBN_KARIAGE = "2";
	// 社宅区分
	private static final String SHATAKU_KBN_ITTO = "4";

	/** ロガー。 */
	private static Logger logger = LogUtils.getLogger(SkfFileOutputUtils.class);

	@Value("${skf.common.validate_error}")
	private String validationErrorCode;

	@Override
	public BaseDto index(Skf3010Sc001OutContractDto outContraDto) throws Exception {
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("契約情報出力", CodeConstant.C001, outContraDto.getPageId());
		// デバッグログ
		logger.info("契約情報出力ボタン押下");
		// 社宅区分(選択行)
		String hdnRowShatakuKbn = outContraDto.getHdnRowShatakuKbn();
		// 社宅管理番号(選択行)
		String hdnRowShatakuKanriNo = outContraDto.getHdnRowShatakuKanriNo();
		String hdnRowShatakuName = outContraDto.getHdnRowShatakuName();
		String hdnRowAreaKbn = outContraDto.getHdnRowAreaKbn();
		String hdnRowEmptyRoomCount = outContraDto.getHdnRowEmptyRoomCount();
		String hdnRowEmptyParkingCount = outContraDto.getHdnRowEmptyParkingCount();
		// 管理会社コード
		String hdnSelectedCompanyCd = outContraDto.getHdnSelectedCompanyCd();
		// 機関コード 
		String hdnAgencyCd = outContraDto.getHdnAgencyCd();
		// 社宅区分コード 
		String hdnShatakuKbnCd = outContraDto.getHdnShatakuKbnCd();
		// 空き部屋コード
		String hdnEmptyRoomCd = outContraDto.getHdnEmptyRoomCd();
		// 利用区分コード
		String hdnUseKbnCd = outContraDto.getHdnUseKbnCd();
		// 空き駐車場コード
		String hdnEmptyParkingCd = outContraDto.getHdnEmptyParkingCd();
		// 社宅名
		String hdnShatakuName = outContraDto.getHdnShatakuName();
		// 社宅住所
		String hdnShatakuAddress = outContraDto.getHdnShatakuAddress();
		
		// 社宅管理番号の有無チェック
//		if (hdnRowShatakuKanriNo == null || hdnRowShatakuKanriNo.length() < 1) {
//			logger.warn("社宅が選択されていません。");
//			ServiceHelper.addWarnResultMessage(outContraDto, MessageIdConstant.W_SKF_3003);
//			return outContraDto;
//		}
//		// 社宅区分判定
//		if (!hdnRowShatakuKbn.equals(SHATAKU_KBN_KARIAGE)) {
//			logger.warn("借上以外が選択されています。");
//			ServiceHelper.addWarnResultMessage(outContraDto, MessageIdConstant.W_SKF_3002);
//			return outContraDto;
//		}

		// 正常終了メッセージ設定
		ServiceHelper.addResultMessage(outContraDto, MessageIdConstant.I_SKF_2047);
//		TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF3010_SC006, "init");
//		TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF3010_SC001, "init");
//		ServiceHelper.addResultMessage(copyRentalDto, MessageIdConstant.I_SKF_2047);
//		nextPage.addResultMessage(MessageIdConstant.I_SKF_2047);
//		copyRentalDto.setTransferPageInfo(nextPage);

		return outContraDto;
	}
}
