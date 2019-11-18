/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2050.domain.service.skf2050sc002;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBatchUtils.SkfBatchUtilsGetMultipleTablesUpdateDateExp;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationGuideUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.batch.SkfBatchUtils;
import jp.co.c_nexco.skf.skf2050.domain.dto.skf2050sc002.Skf2050Sc002InitDto;

/**
 * Skf2050Sc002 備品返却確認（アウトソース用)初期表示処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2050Sc002InitService extends BaseServiceAbstract<Skf2050Sc002InitDto> {

	@Autowired
	private Skf2050Sc002SharedService skf2050Sc002SharedService;

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfOperationGuideUtils skfOperationGuideUtils;
	@Autowired
	private SkfBatchUtils skfBatchUtils;

	private String companyCd = CodeConstant.C001;

	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2050Sc002InitDto index(Skf2050Sc002InitDto initDto) throws Exception {
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示処理開始", companyCd, FunctionIdConstant.SKF2050_SC002);
		// タイトル設定
		initDto.setPageTitleKey(MessageIdConstant.SKF2050_SC002_TITLE);

		// 社宅連携バッチ更新排他処理用
		Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> forUpdateMap = skfBatchUtils
				.getUpdateDateForUpdateSQL(initDto.getShainNo());
		menuScopeSessionBean.put(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2050SC002, forUpdateMap);

		// 画面表示設定
		boolean result = skf2050Sc002SharedService.setDisplayData(initDto);
		if (!result) {
			// 初期表示に失敗した場合次処理のボタンを押せなくする。
			initDto.setAllButtonEscape(true);
		}

		// 操作ガイド取得
		String operationGuide = skfOperationGuideUtils.getOperationGuide(FunctionIdConstant.SKF2050_SC002);
		initDto.setOperationGuide(operationGuide);

		return initDto;
	}

}
