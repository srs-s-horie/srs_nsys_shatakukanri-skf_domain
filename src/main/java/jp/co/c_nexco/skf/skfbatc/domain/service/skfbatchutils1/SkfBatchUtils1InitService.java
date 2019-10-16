/*
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
import jp.co.c_nexco.skf.skfbatc.domain.dto.skfbatchutils1.SkfBatchUtils1InitDto;

/**
 * Skf3090Sc005InitService 社宅管理台帳データ登録（社宅情報）更新テスト初期表示クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class SkfBatchUtils1InitService extends BaseServiceAbstract<SkfBatchUtils1InitDto> {

	@Autowired
	private SkfBatchUtils skfBatchUtils;

	private final String FOR_LOCK_CHECK_TEST = "forUpdateTest";

	/**
	 * 画面初期表示のメイン処理
	 */
	@Override
	protected BaseDto index(SkfBatchUtils1InitDto initDto) throws Exception {

		LogUtils.debugByMsg("排他制御-UTTest：画面初期表示開始");

		// 社員番号が設定されていたら設定
		String shainNo = null;
		String message = "";
		if (NfwStringUtils.isNotEmpty(initDto.getShainNo())) {
			shainNo = initDto.getShainNo();
			message = "社員番号：「" + shainNo + "」で絞り込んで";
		}
		// 処理時間計測開始
		long startTime = System.currentTimeMillis();
		// 更新候補取得
		Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> forUpdateMap = skfBatchUtils
				.getUpdateDateForUpdateSQL(shainNo);
		// メニュースコープセッションに渡す
		super.menuScopeSessionBean.put(this.FOR_LOCK_CHECK_TEST, forUpdateMap);
		// 処理時間計測終了
		long endTime = System.currentTimeMillis();

		LogUtils.debugByMsg(
				"排他制御-UTTest：画面表示時にUPDATE対象TBLの現在時間を" + message + "取得するのに掛かった時間：" + (endTime - startTime) + "ミリ秒");
		return initDto;
	}

}
