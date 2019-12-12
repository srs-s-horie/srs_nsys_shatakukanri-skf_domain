/*
 * 
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skfbatc.domain.service.skfbatchutils1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.util.batch.SkfBatchUtils;
import jp.co.c_nexco.skf.skfbatc.domain.dto.skfbatchutils1.SkfBatchUtils1ReacquireDto;

/**
 * SkfBatchUtils1ReacquireService 更新日付再取得 テストクラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class SkfBatchUtils1ReacquireService extends BaseServiceAbstract<SkfBatchUtils1ReacquireDto> {

	@Autowired
	private SkfBatchUtils skfBatchUtils;
	private final String FOR_LOCK_CHECK_TEST = "forUpdateTest";

	/**
	 * 画面初期表示のメイン処理
	 */
	@Override
	public BaseDto index(SkfBatchUtils1ReacquireDto dto) throws Exception {

		LogUtils.debugByMsg("更新日付再取得-UTTest：テスト実行クラス処理開始");

		// 処理時間計測開始
		long startTime = System.currentTimeMillis();

		// 時間取得用
		long localStartTime = System.currentTimeMillis();
		long localEndTime = System.currentTimeMillis();

		/*
		 * 提示データ
		 */
		if (NfwStringUtils.isNotEmpty(dto.getTeijiDataTeijiNo())) {
			localStartTime = System.currentTimeMillis();
			skfBatchUtils.toReacquireUpdateDate(SkfBatchUtils.skf3022_t_teiji_data, dto.getTeijiDataTeijiNo());
			localEndTime = System.currentTimeMillis();
			LogUtils.debugByMsg("更新日付再取得-UTTest：" + (localEndTime - localStartTime) + "ミリ秒");
		}

		// 処理時間計測終了
		long endTime = System.currentTimeMillis();

		LogUtils.debugByMsg("排他制御-UTTest：排他チェックに掛かったトータルの時間：" + (endTime - startTime) + "ミリ秒");

		dto.setReturnStatus("0");
		LogUtils.debugByMsg("排他制御-UTTest：テスト実行クラス処理終了");

		return dto;

	}

}
