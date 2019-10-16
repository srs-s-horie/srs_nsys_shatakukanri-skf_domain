/*
 * 
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skfbatc.domain.service.skfbatchutils1;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBatchUtils.SkfBatchUtilsGetMultipleTablesUpdateDateExp;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.util.batch.SkfBatchUtils;
import jp.co.c_nexco.skf.skfbatc.domain.dto.skfbatchutils1.SkfBatchUtils1ExecuteDto;

/**
 * UpdateShatakuKanriDaichoShatakuDataExecuteService 社宅管理台帳データ登録（社宅情報）更新テストクラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class SkfBatchUtils1ExecuteService extends BaseServiceAbstract<SkfBatchUtils1ExecuteDto> {

	@Autowired
	private SkfBatchUtils skfBatchUtils;

	private final String FOR_LOCK_CHECK_TEST = "forUpdateTest";

	/**
	 * 画面初期表示のメイン処理
	 */
	@Override
	public BaseDto index(SkfBatchUtils1ExecuteDto dto) throws Exception {

		LogUtils.debugByMsg("排他制御-UTTest：テスト実行クラス処理開始");

		// 処理時間計測開始
		long startTime = System.currentTimeMillis();

		// セッションスコープからMap取得
		@SuppressWarnings("unchecked")
		Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> forUpdateMap = (Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>>) super.menuScopeSessionBean
				.get(FOR_LOCK_CHECK_TEST);

		/** 排他制御チェック処理呼び出し ======↓ */
		// 時間取得用
		long localStartTime = System.currentTimeMillis();
		long localEndTime = System.currentTimeMillis();
		/*
		 * 提示データ
		 */
		if (NfwStringUtils.isNotEmpty(dto.getTeijiDataTeijiNo())) {
			localStartTime = System.currentTimeMillis();
			skfBatchUtils.updatePossibleChecker(forUpdateMap, SkfBatchUtils.skf3022_t_teiji_data,
					dto.getTeijiDataTeijiNo());
			localEndTime = System.currentTimeMillis();
			LogUtils.debugByMsg("排他制御-UTTest：提示データチェック：" + (localEndTime - localStartTime) + "ミリ秒");
		}
		/*
		 * 入退居予定データ
		 */
		if (NfwStringUtils.isNotEmpty(dto.getNtYoteiDataShainNo())) {
			localStartTime = System.currentTimeMillis();
			skfBatchUtils.updatePossibleChecker(forUpdateMap, SkfBatchUtils.skf3021_t_nyutaikyo_yotei_data,
					dto.getNtYoteiDataShainNo(), dto.getNtYoteiDataNyutaikyoKbn());
			localEndTime = System.currentTimeMillis();
			LogUtils.debugByMsg("排他制御-UTTest：入退居データチェック：" + (localEndTime - localStartTime) + "ミリ秒");
		}
		/*
		 * 社宅管理台帳基本
		 */
		if (NfwStringUtils.isNotEmpty(dto.getSLedgerShatakuKanriId())) {
			localStartTime = System.currentTimeMillis();
			skfBatchUtils.updatePossibleChecker(forUpdateMap, SkfBatchUtils.skf3030_t_shataku_ledger,
					dto.getSLedgerShatakuKanriId());
			localEndTime = System.currentTimeMillis();
			LogUtils.debugByMsg("排他制御-UTTest：社宅管理台帳基本チェック：" + (localEndTime - localStartTime) + "ミリ秒");
		}
		/*
		 * 社宅駐車場区画情報マスタ
		 */
		if (NfwStringUtils.isNotEmpty(dto.getSParkingBlockShatakuKanriNo())) {
			localStartTime = System.currentTimeMillis();
			skfBatchUtils.updatePossibleChecker(forUpdateMap, SkfBatchUtils.skf3010_m_shataku_parking_block,
					dto.getSParkingBlockShatakuKanriNo(), dto.getSParkingBlockParkingKanriNo());
			localEndTime = System.currentTimeMillis();
			LogUtils.debugByMsg("排他制御-UTTest：社宅駐車場区画情報チェック：" + (localEndTime - localStartTime) + "ミリ秒");
		}
		/*
		 * 社宅部屋情報マスタ
		 */
		if (NfwStringUtils.isNotEmpty(dto.getSRoomShatakuKanriNo())) {
			localStartTime = System.currentTimeMillis();
			skfBatchUtils.updatePossibleChecker(forUpdateMap, SkfBatchUtils.skf3010_m_shataku_room,
					dto.getSRoomShatakuKanriNo(), dto.getSRoomRoomKanriNo());
			localEndTime = System.currentTimeMillis();
			LogUtils.debugByMsg("排他制御-UTTest：社宅部屋情報マスタチェック：" + (localEndTime - localStartTime) + "ミリ秒");
		}

		/** 排他制御チェック処理呼び出し ======↑ */

		// 処理時間計測終了
		long endTime = System.currentTimeMillis();

		LogUtils.debugByMsg("排他制御-UTTest：排他チェックに掛かったトータルの時間：" + (endTime - startTime) + "ミリ秒");

		dto.setReturnStatus("0");
		LogUtils.debugByMsg("排他制御-UTTest：テスト実行クラス処理終了");
		return dto;
	}

}
