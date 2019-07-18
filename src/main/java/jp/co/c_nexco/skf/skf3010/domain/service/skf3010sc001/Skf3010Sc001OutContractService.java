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
import jp.co.c_nexco.skf.common.util.SkfShinseiUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc001.Skf3010Sc001OutContractDto;
import jp.co.intra_mart.common.platform.log.Logger;

/**
 * Skf3010Sc001 社宅一覧の契約情報出力ボタン処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3010Sc001OutContractService extends BaseServiceAbstract<Skf3010Sc001OutContractDto> {

	/**
	 * 中身は現状まったくの別物である。
	 * 本クラスは将来的に契約情報出力処理を実装するが
	 * 現状はJSPからのパラメータ受け渡しとページ遷移
	 * バリデーション、関連チェックの確認と結果返却の実装テストをしたものである。
	 */
	// 社宅区分
	private static final String SHATAKU_KBN_KARIAGE = "2";

	/** ロガー。 */
	private static Logger logger = LogUtils.getLogger(SkfFileOutputUtils.class);

	@Value("${skf.common.validate_error}")
	private String validationErrorCode;

	@Override
	public BaseDto index(Skf3010Sc001OutContractDto copyRentalDto) throws Exception {
		// 社宅区分(選択行)
		String hdnRowShatakuKbn = copyRentalDto.getHdnRowShatakuKbn();
		// 社宅管理番号(選択行)
		String hdnRowShatakuKanriNo = copyRentalDto.getHdnRowShatakuKanriNo();
		String hdnRowShatakuName = copyRentalDto.getHdnRowShatakuName();
		String hdnRowAreaKbn = copyRentalDto.getHdnRowAreaKbn();
		String hdnRowEmptyRoomCount = copyRentalDto.getHdnRowEmptyRoomCount();
		String hdnRowEmptyParkingCount = copyRentalDto.getHdnRowEmptyParkingCount();
		// 管理会社コード
		String hdnSelectedCompanyCd = copyRentalDto.getHdnSelectedCompanyCd();
		// 機関コード 
		String hdnAgencyCd = copyRentalDto.getHdnAgencyCd();
		// 社宅区分コード 
		String hdnShatakuKbnCd = copyRentalDto.getHdnShatakuKbnCd();
		// 空き部屋コード
		String hdnEmptyRoomCd = copyRentalDto.getHdnEmptyRoomCd();
		// 利用区分コード
		String hdnUseKbnCd = copyRentalDto.getHdnUseKbnCd();
		// 空き駐車場コード
		String hdnEmptyParkingCd = copyRentalDto.getHdnEmptyParkingCd();
		// 社宅名
		String hdnShatakuName = copyRentalDto.getHdnShatakuName();
		// 社宅住所
		String hdnShatakuAddress = copyRentalDto.getHdnShatakuAddress();
		
		// 社宅管理番号の有無チェック
		if (hdnRowShatakuKanriNo == null || hdnRowShatakuKanriNo.length() < 1) {
			logger.warn("社宅が選択されていません。");
			ServiceHelper.addWarnResultMessage(copyRentalDto, MessageIdConstant.W_SKF_3003);
			return copyRentalDto;
		}
		// 社宅区分判定
		if (!hdnRowShatakuKbn.equals(SHATAKU_KBN_KARIAGE)) {
			logger.warn("借上以外が選択されています。");
			ServiceHelper.addWarnResultMessage(copyRentalDto, MessageIdConstant.W_SKF_3002);
			return copyRentalDto;
		}

		// 画面遷移（申請条件一覧へ）
//		TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF3010_SC006, "init");
		TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF3010_SC001, "init");
//		ServiceHelper.addResultMessage(copyRentalDto, MessageIdConstant.I_SKF_2047);
		nextPage.addResultMessage(MessageIdConstant.I_SKF_2047);
		copyRentalDto.setTransferPageInfo(nextPage);

		return copyRentalDto;
	}
}
